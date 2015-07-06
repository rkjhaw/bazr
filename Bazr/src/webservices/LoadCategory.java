package webservices;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import listener.Category_Listener;
import model.Category;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;

import com.keshima.bazr.R;

import utils.ConnectionUtil;
import utils.Constants;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

public class LoadCategory extends AsyncTask<Void, Void, String> 
{

	public static String MODULE = "LoadCategory";
	public static String TAG = "";
	
	Category_Listener mCallBack;;
	ProgressDialog dialog;
	Context ctx;
	public static ArrayList<Category> categories=new ArrayList<Category>();
	private String Str_Message="";

	Fragment mFragment;
	FragmentActivity mActivity;

	private InputStream is;
	private String webservice_reply;	
	
	public LoadCategory(Fragment mFragment) 
	{
		TAG="LoadCategory";
		Log.d(MODULE,TAG);	
			
		mActivity = mFragment.getActivity();
		mCallBack =(Category_Listener) mFragment;	
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

		categories = new ArrayList<Category>();		
		String Str_Response = "";
		
		try 
		{
			ConnectionUtil ObjConnection = new ConnectionUtil();
			Str_Response = ObjConnection.makeServiceCall(Constants.G_CATEGORY_LIST_URL, ConnectionUtil.GET, mActivity);
			
			Log.e(MODULE,TAG + "Response from Server : " + Str_Response);	
			try
			{
				if (Str_Response != null && (!Str_Response.equals("UH")) && (!Str_Response.equals("INA"))) 
				{
	               try 
	               {
						JSONArray res;
						String name, imagePath, id;				
						try
						{				
							res = new JSONArray(Str_Response);								
							if(res.length() > 0)
							{
								for (int i = 0; i < res.length(); i++) 
								{
									name = res.getJSONObject(i).getString("categoryName");
									id = res.getJSONObject(i).getString("categoryId");
									imagePath = res.getJSONObject(i).getString("imagePath");
									categories.add(new Category(id, name, imagePath));
								}
							}			
						} 
						catch (Exception e)
						{
							e.printStackTrace();
							Str_Message = this.mActivity.getString(R.string.msg_unexpected_error);	
						}
	               }
	               catch (Exception e)
	   			   {
	   					e.printStackTrace();
	   					Str_Message = this.mActivity.getString(R.string.msg_unexpected_error);	
	   			   }
				}
				else if((Str_Response.equals("UH")) || (Str_Response.equals("INA")))
		        {
					Str_Message = this.mActivity.getString(R.string.msg_failed_to_connect_Server);									         	
		        }
		        else
		        {
		        	Str_Message = this.mActivity.getString(R.string.msg_unexpected_error);					
		        }
			}
			catch (Exception e)
			{
				e.printStackTrace();
				Str_Message = this.mActivity.getString(R.string.msg_unexpected_error);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			Str_Message = this.mActivity.getString(R.string.msg_unexpected_error);
		}
		return Str_Response;
	}

	@Override
	protected void onPostExecute(String result)
	{
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		if (dialog.isShowing())
			dialog.dismiss();
		Log.e("cat size ", String.valueOf(categories.size()));
		mCallBack.onCategoriesLoaded(categories,Str_Message);
	}

}
