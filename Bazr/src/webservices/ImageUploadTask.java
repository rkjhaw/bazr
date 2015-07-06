package webservices;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import listener.Product_Uploaded_Listener;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import com.keshima.bazr.FragmentChangeActivity;
import com.keshima.bazr.R;

import utils.Constants;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

public class ImageUploadTask extends AsyncTask<Void, Void, Void> 
{
	public static String MODULE = "ImageUploadTask-Product";
	public static String TAG = "";

	Context ctx;
	String filePath, serverPath;
	Product_Uploaded_Listener mCallBack;
	ProgressDialog dialog;
	boolean success;
	Fragment mFragment;
	FragmentActivity mActivity;
	private String Str_Code, Str_ProductId,Str_Message="";

	String sellerId, str_product_name, str_category_name,str_sub_category_name, str_product_price,
			str_product_pname, str_product_email, str_product_number,
			str_latitude, str_longitude, address, address2,
			str_product_description;

	public ImageUploadTask(Fragment mFragment,String filePath, String serverPath,
			String sellerId,String str_product_name, String str_category_name,String str_sub_category_name,
			String str_product_price, String str_product_pname,
			String str_product_email, String str_product_number,
			String str_latitude, String str_longitude, String address,
			String address2, String str_product_description)
	{
		
		TAG="ImageUploadTask";
		Log.d(MODULE,TAG);
		
		// TODO Auto-generated constructor stub
		this.mFragment = mFragment;
		this.mActivity = mFragment.getActivity();
		
		this.filePath = filePath;
		this.serverPath = serverPath;
		mCallBack = (Product_Uploaded_Listener) mFragment;
		
		
		this.sellerId = sellerId;
		this.str_product_name = str_product_name;
		this.str_category_name = str_category_name;
		this.str_sub_category_name = str_sub_category_name;
		this.str_product_price = str_product_price;
		this.str_product_pname = str_product_pname;
		this.str_product_email = str_product_email;
		this.str_product_number = str_product_number;
		this.str_latitude = str_latitude;
		this.str_longitude = str_longitude;
		this.address = address;
		this.address2 = address2;
		this.str_product_description = str_product_description;		
		
		Log.d(MODULE,TAG + " sellerId>>>" + sellerId);
		Log.d(MODULE,TAG + " str_product_name>>>"+str_product_name);
		Log.d(MODULE,TAG + " str_category_name>>>"+str_category_name);
		Log.d(MODULE,TAG + " str_sub_category_name>>>"+str_sub_category_name);
		Log.d(MODULE,TAG + " str_product_price>>>"+str_product_price);
		Log.d(MODULE,TAG + " str_product_pname>>>"+str_product_pname);
		Log.d(MODULE,TAG + " str_product_email>>>"+str_product_email);
		Log.d(MODULE,TAG + " str_product_number>>>"+str_product_number);
		Log.d(MODULE,TAG + " str_latitude>>>"+str_latitude);
		Log.d(MODULE,TAG + " str_longitude>>>"+str_longitude);
		Log.d(MODULE,TAG + " address>>>"+address);
		Log.d(MODULE,TAG + " address2>>>"+address2);
		Log.d(MODULE,TAG + " str_product_description>>>"+str_product_description);		
		
	}

	@Override
	protected void onPreExecute() 
	{
		super.onPreExecute();
		
		TAG="onPreExecute";
		Log.d(MODULE,TAG);
		
		dialog = new ProgressDialog(this.mActivity);
		dialog.setMessage("Image uploading");
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);		
		dialog.show();
	}

	@Override
	protected Void doInBackground(Void... params)
	{
		TAG="doInBackground";
		Log.d(MODULE,TAG);
		
		uploadFile(filePath, serverPath);
		return null;
	}

	@SuppressWarnings("deprecation")
	private void uploadFile(String filePath2, String serverPath2) 
	{

		TAG="uploadFile";
		Log.d(MODULE,TAG);
		
		MultipartEntityBuilder mBuilder;
		
		try 
		{
			mBuilder = MultipartEntityBuilder.create();
			mBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

			File file = new File(filePath2);

			if (!file.exists())
				return;

			mBuilder.addPart("files", new FileBody(file));
			mBuilder.addPart("sellerId", new StringBody(sellerId,ContentType.TEXT_PLAIN));
			mBuilder.addPart("productName", new StringBody(str_product_name,ContentType.TEXT_PLAIN));
			mBuilder.addPart("categoryId", new StringBody(str_category_name,ContentType.TEXT_PLAIN));
			mBuilder.addPart("subCategoryId", new StringBody(str_sub_category_name,ContentType.TEXT_PLAIN));
			mBuilder.addPart("price", new StringBody(str_product_price,ContentType.TEXT_PLAIN));
			mBuilder.addPart("contactPerson", new StringBody(str_product_pname,ContentType.TEXT_PLAIN));
			mBuilder.addPart("email", new StringBody(str_product_email,ContentType.TEXT_PLAIN));
			mBuilder.addPart("contactNo", new StringBody(str_product_number,ContentType.TEXT_PLAIN));
			mBuilder.addPart("location", new StringBody(str_latitude + "|||" + str_longitude + "|||" + address,ContentType.TEXT_PLAIN));
			mBuilder.addPart("address", new StringBody(address,ContentType.TEXT_PLAIN));
			mBuilder.addPart("latitude", new StringBody(str_latitude,ContentType.TEXT_PLAIN));
			mBuilder.addPart("longitude", new StringBody(str_longitude,ContentType.TEXT_PLAIN));
			mBuilder.addPart("description", new StringBody(str_product_description,ContentType.TEXT_PLAIN));

			Log.d(MODULE,TAG + ">>> files " + ":" + sellerId);
			Log.d(MODULE,TAG + ">>> sellerId " + ":" + sellerId);
			Log.d(MODULE,TAG + ">>> productName " + ":"+ str_product_name);
			Log.d(MODULE,TAG + ">>> categoryId " + ":"+ str_category_name);
			Log.d(MODULE,TAG + ">>> subCategoryId " + ":"+ str_sub_category_name);
			Log.d(MODULE,TAG + ">>> price " + ":"+ str_product_price);
			Log.d(MODULE,TAG + ">>> contactPerson " + ":"+ str_product_pname);
			Log.d(MODULE,TAG + ">>> email " + ":"+ str_product_email);
			Log.d(MODULE,TAG + ">>> contactNo " + ":"+ str_product_number);
			Log.d(MODULE,TAG + ">>> location " + ":"+  ":"+ str_latitude + "|||" + str_longitude + "|||" + address);
			Log.d(MODULE,TAG + ">>> address " + ":"+ address);
			Log.d(MODULE,TAG + ">>> latitude " + ":"+ str_latitude);
			Log.d(MODULE,TAG + ">>> longitude " + ":"+ str_longitude);
			Log.d(MODULE,TAG + ">>> description " + ":"+ str_product_description);
			
			DefaultHttpClient httpClient = new DefaultHttpClient();

			HttpPost httpPost = new HttpPost(Constants.SERVER_URL+"saveProductInfoAndImage");
			httpPost.setEntity(mBuilder.build());
           
			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity httpEntity = httpResponse.getEntity();

			//Reading the response:
			InputStream is = httpEntity.getContent();

			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			StringBuilder sb = new StringBuilder();
			String line;

			while ((line = reader.readLine()) != null)
				sb.append(line + "\n");
			
			is.close();
			httpEntity.consumeContent();
			// Do something with the string returned: sb.toString()			
			String Str_Response = sb.toString();
			Log.d(MODULE,TAG + "Result is : " + Str_Response.toString());
			
			try
			{
				JSONObject json = new JSONObject(Str_Response);
				Str_Code = json.getString("Code");
				if(Str_Code.equals("3"))	
				{
					success = true;
					Str_ProductId = json.getString("productId");
					Str_Message = json.getString("Message");					
				}
				else if(Str_Code.equals("2"))	
				{
					success = false;
					Str_Message = json.getString("Message");
				}		
			}
			
			catch (JSONException e)
			{
				success = false;
				Str_Message = this.mActivity.getString(R.string.msg_unexpected_error);
				Str_Code="10";
				e.printStackTrace();
			}

		} 
		catch (Exception e)
		{
			success = false;
			Str_Message = this.mActivity.getString(R.string.msg_unexpected_error);
			Str_Code="10";
			e.printStackTrace();
		}
	}

	@Override
	protected void onPostExecute(Void result)
	{
		super.onPostExecute(result);
		if (dialog.isShowing())
			dialog.dismiss();		
		mCallBack.onProductUploadResult(success, Str_Code, Str_Message);
	}

}
