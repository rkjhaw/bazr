package webservices;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import listener.Products_Listener;
import model.Product;
import model.ProductImage;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONObject;

import utils.ConnectionUtil;
import utils.Constants;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;


public class LoadProductsFragment extends AsyncTask<Void, Void, String> 
{

	Products_Listener mCallBack;
	ProgressDialog dialog;
	String category;
	private ArrayList<Product> Lst_Products = new ArrayList<Product>();
	Fragment mFragment;
	FragmentActivity mActivity;
	private InputStream is;
	private String webservice_reply;
	boolean category_filtered, price_filtered, distance_filtered;
	int category_filter_position, price_filter_position,dostance_filter_position;
	

	public LoadProductsFragment(Fragment mFragment, String category,			
			boolean category_filtered, boolean price_filtered,
			boolean distance_filtered, int category_filter_position,
			int price_filter_position, int dostance_filter_position) 
	{
		// TODO Auto-generated constructor stub
		this.mFragment = mFragment;
		this.mActivity = mFragment.getActivity();		
		this.category = category;		
		this.category_filtered = category_filtered;
		this.price_filtered = price_filtered;
		this.distance_filtered = distance_filtered;
		this.category_filter_position = category_filter_position;
		this.price_filter_position = price_filter_position;
		this.dostance_filter_position = dostance_filter_position;
		mCallBack = (Products_Listener) mFragment;
	}

	@Override
	protected void onPreExecute() 
	{
		super.onPreExecute();
		dialog = new ProgressDialog(mActivity);
		dialog.setMessage("Loading Categories");
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		dialog.show();
	}

	@Override
	protected String doInBackground(Void... parameters) 
	{
		Lst_Products = new ArrayList<Product>();
		
		try 
		{
			
			HttpClient httpclient = new DefaultHttpClient();
			HttpContext httpContext = new BasicHttpContext();
			HttpGet httpget = new HttpGet(Constants.SERVER_URL+ "getUploadListByCategoryName/" + category);
			HttpResponse httpresponse = httpclient.execute(httpget, httpContext);
			HttpEntity httpentity = httpresponse.getEntity();
			is = httpentity.getContent();
		} 
		catch (ClientProtocolException e) 
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		try 
		{
			BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = bufferedreader.readLine()) != null)
			{
				sb.append(line + "\n");
			}
			is.close();
			webservice_reply = sb.toString();
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		Log.e("webservice_reply", webservice_reply);

		JSONArray res;
		String productId, sellerId, subCategoryId, categoryId, productName, contactPerson, price, location = null, email, contactNo, description, date, status, latitude = null, longitude = null;
		ArrayList<ProductImage> productImageArrayList = new ArrayList<ProductImage>();
		ProductImage productImage;
		String productImageId, productProductId, productFileName, productImagePath;

		try
		{

			res = new JSONArray(webservice_reply);			
			
			for (int i = 0; i < res.length(); i++)
			{
				productId = res.getJSONObject(i).getString("productId");
				sellerId = res.getJSONObject(i).getString("sellerId");
				subCategoryId = res.getJSONObject(i).getString("subCategoryId");
				categoryId = res.getJSONObject(i).getString("categoryId");
				productName = res.getJSONObject(i).getString("productName");
				contactPerson = res.getJSONObject(i).getString("contactPerson");
				price = res.getJSONObject(i).getString("price");
				location = res.getJSONObject(i).getString("location");
				email = res.getJSONObject(i).getString("email");
				contactNo = res.getJSONObject(i).getString("contactNo");
				description = res.getJSONObject(i).getString("description");
				date = res.getJSONObject(i).getString("date");
				status = res.getJSONObject(i).getString("status");
				if (location.split("|||").length == 3) 
				{
					latitude = location.split("|||")[0];
					longitude = location.split("|||")[1];
				}
				latitude = location.substring(0, location.indexOf("|||"));
				longitude = location.substring(latitude.length() + 3,location.indexOf("|||", latitude.length() + 3));
				
				
				//JSONArray productImages = res.getJSONObject(i).getJSONArray("productImage");
				JSONObject productImages = res.getJSONObject(i).getJSONObject("productImage");
				
				//if(productImages.length()>0)
				//{
					productImageArrayList = new ArrayList<ProductImage>();
					
					//for (int j = 0; j < productImages.length(); j++) 
					//{ 
						//JSONObject ObjProductImage = productImages.getJSONObject(j);
						productImageId = productImages.getString("imageId");
						productProductId = productImages.getString("productId");
						productFileName = productImages.getString("fileName");
						productImagePath = productImages.getString("imagePath");
						productImage = new ProductImage(productImageId,productProductId, productFileName, productImagePath);
						productImageArrayList.add(productImage);
					//} 
				//}				

				Product p = new Product(productId, sellerId, subCategoryId,
						categoryId, productName, contactPerson, price,
						location, email, contactNo, description, date, status,
						productImageArrayList, latitude, longitude);

				p.setLatitude(latitude);
				p.setLongitude(longitude);
				Lst_Products.add(p);
			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return webservice_reply;
	}

	@Override
	protected void onPostExecute(String result)
	{
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		if (dialog.isShowing())
			dialog.dismiss();				
		Log.e("products size ", String.valueOf(Lst_Products.size()));
		mCallBack.onProductLoadResult(Lst_Products);
	}

}
