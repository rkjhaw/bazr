package webservices;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import model.Product;
import model.ProductImage;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONObject;

import utils.Constants;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

public class LoadProductDetails extends AsyncTask<Void, Void, String> 
{
	TaskCompleted taskCompletedCallback;
	ProgressDialog dialog;
	String id;
	Context ctx;
	public static ArrayList<Product> products;

	FragmentActivity activity;
	Fragment mFragment;

	private InputStream is;
	private String webservice_reply;

	public LoadProductDetails(Fragment mFragment, String id) 
	{
		this.mFragment = mFragment;
		this.id = id;
	}

	@Override
	protected void onPreExecute() 
	{
		super.onPreExecute();
		dialog = new ProgressDialog(ctx);
		dialog.setMessage("Loading Categories");
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		dialog.show();
	}

	@SuppressWarnings("deprecation")
	@Override
	protected String doInBackground(Void... parameters)
	{
		// TODO Auto-generated method stub

		try 
		{

			HttpClient httpclient = new DefaultHttpClient();
			HttpContext httpContext = new BasicHttpContext();
			HttpGet httpget = new HttpGet(Constants.SERVER_URL+"getProductInfoById/"+ id);
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
		String productId, sellerId, subCategoryId, categoryId, productName, contactPerson, price, location, email, contactNo, description, date, status, latitude, longitude;
		JSONObject productImages;
		ArrayList<ProductImage> productImageArrayList = null;
		ProductImage productImage;
		String productImageId, productProductId, productFileName, productImagePath;

		try 
		{

			res = new JSONArray(webservice_reply);
			products = new ArrayList<Product>();
			productImageArrayList = new ArrayList<ProductImage>();
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
				latitude = res.getJSONObject(i).getString("latitude");
				longitude = res.getJSONObject(i).getString("longitude");

				productImages = res.getJSONObject(i).getJSONObject("productImage");

				/* for (int j = 0; j < productImages.length(); j++) { */
				productImageId = productImages./* getJSONObject(j). */getString("imageId");
				productProductId = productImages/* .getJSONObject(j) */.getString("productId");
				productFileName = productImages./* getJSONObject(j). */getString("fileName");
				productImagePath = productImages/* .getJSONObject(j) */.getString("imagePath");
				productImage = new ProductImage(productImageId,productProductId, productFileName, productImagePath);
				productImageArrayList.add(productImage);
				/* } */

				products.add(new Product(productId, sellerId, subCategoryId,
						categoryId, productName, contactPerson, price,
						location, email, contactNo, description, date, status,
						productImageArrayList, latitude, longitude));

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

		/*
		 * Log.e("products size ", String.valueOf(products.size()));
		 * activity.setList(products); ProductDetailActivity.products =
		 * products;
		 * 
		 * taskCompletedCallback.onTaskCompleted(activity); //
		 * activity.setList(categories);
		 */
	}

}
