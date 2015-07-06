package webservices;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import listener.SubCategory_Listener;
import model.SubCategory;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;

import utils.Constants;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

public class LoadSubCategories extends AsyncTask<Void, Void, String> 
{
	public static String MODULE = "ProductUploadFragment";
	public static String TAG = "";
	
	SubCategory_Listener mCallBack;
	ProgressDialog dialog;
	
	public static ArrayList<SubCategory> subcategories;

	FragmentActivity mActivity;
	Fragment mFragment;
	String Str_CategoryId="";

	private InputStream is;
	private String webservice_reply;

	public LoadSubCategories(Fragment mFragment,String Str_CategoryId) 
	{
		TAG="LoadSubCategories";
		Log.d(MODULE,TAG);
		this.mFragment = mFragment;		
		this.mActivity = mFragment.getActivity();		
		this.Str_CategoryId = Str_CategoryId;
		mCallBack=(SubCategory_Listener)mFragment;
	}

	@Override
	protected void onPreExecute() 
	{
		
		super.onPreExecute();
		
		TAG="onPreExecute";
		Log.d(MODULE,TAG);
		
		dialog = new ProgressDialog(mActivity);
		dialog.setMessage("Loading Categories");
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		dialog.setCancelable(false);
		dialog.show();
	}

	@Override
	protected String doInBackground(Void... parameters) 
	{
		TAG="doInBackground";
		Log.d(MODULE,TAG);
		
		try 
		{
			HttpClient httpclient = new DefaultHttpClient();
			HttpContext httpContext = new BasicHttpContext();
			HttpGet httpget = new HttpGet(Constants.SERVER_URL+"getSubCategoryByCatId?categoryId="+Str_CategoryId);
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
			// Read the reply and place it in a buffer
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
		String name, imagePath, subcategoryid,categoryid;
		
		try
		{

			res = new JSONArray(webservice_reply);
			subcategories = new ArrayList<SubCategory>();
			for (int i = 0; i < res.length(); i++) 
			{
				name = res.getJSONObject(i).getString("subCategoryName");
				subcategoryid = res.getJSONObject(i).getString("subCategoryId");
				categoryid = res.getJSONObject(i).getString("categoryId");				
				imagePath = res.getJSONObject(i).getString("imagePath");
				subcategories.add(new SubCategory(subcategoryid,categoryid, name, imagePath));
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
		super.onPostExecute(result);
		
		TAG="onPostExecute";
		Log.d(MODULE,TAG);
		
		if (dialog.isShowing())
			dialog.dismiss();
		Log.e("subcategories size ", String.valueOf(subcategories.size()));
		mCallBack.onSubCategoriesLoaded(subcategories);
	}

}
