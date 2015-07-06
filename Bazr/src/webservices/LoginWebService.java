package webservices;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import listener.Login_Listener;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONException;
import org.json.JSONObject;

import utils.Constants;

import com.google.android.gms.appdatasearch.GetRecentContextCall;
import com.keshima.bazr.FragmentChangeActivity;
import com.keshima.bazr.R;

import fragments.LoginFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;


@SuppressWarnings("deprecation")
public class LoginWebService extends AsyncTask<Void, Void, String>
{

	public static String MODULE = "LoginWebService";
	public static String TAG = "";
	
	Login_Listener mCallBack;
	ProgressDialog dialog;
	String category;
	Context ctx;
	String email, password;
	LoginFragment fragment;
	private InputStream is;
	private String webservice_reply;
	boolean success;
	private String Str_Code, Str_SellerId,Str_Message;
	Fragment mFragment;

	public LoginWebService(LoginFragment ctx,String email, String password,Fragment mFragment) 
	{
		TAG="LoginWebService";
		Log.d(MODULE,TAG);	
		
		
		this.ctx = ctx.getActivity();
		fragment = ctx;
		this.email = email;
		this.password = password;
		this.mFragment = mFragment;
		mCallBack = (Login_Listener) mFragment;
	}

	@Override
	protected void onPreExecute() 
	{
		super.onPreExecute();
		
		TAG="LoginWebService";
		Log.d(MODULE,TAG);	
		
		dialog = new ProgressDialog(ctx);
		dialog.setMessage("Logging in...");
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		dialog.show();
	}

	@SuppressWarnings("deprecation")
	@Override
	protected String doInBackground(Void... parameters) 
	{
		TAG="doInBackground";
		Log.d(MODULE,TAG);	
		
		// TODO Auto-generated method stub
		/*
		 * private long sellerId; private String sellerName; private String
		 * emailId; private String sellerPassword; private String mobile;
		 * private String address; private String userType;
		 */
		try 
		{
			HttpClient httpclient = new DefaultHttpClient();
			HttpContext httpContext = new BasicHttpContext();
			String url = Constants.SERVER_URL + "login?" + "&emailId="
					+ URLEncoder.encode(email, "UTF-8") + "&sellerPassword="
					+ URLEncoder.encode(password, "UTF-8");
			Log.e("url", url);
			HttpGet httpget = new HttpGet(url);
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
			JSONObject json = new JSONObject(webservice_reply);
			Str_Code = json.getString("Code");
			if(Str_Code.equals("3"))	
			{
				success = true;
				Str_SellerId = json.getString("sellerId");
				Str_Message = json.getString("Message");
				FragmentChangeActivity.prefs.edit().putString("sellerId", Str_SellerId).commit();
			}
			else if(Str_Code.equals("2"))	
			{
				success = false;
				Str_Message = json.getString("Message");
			}			
		} 
		catch (UnsupportedEncodingException e) 
		{
			success = false;
			Str_Message = this.ctx.getString(R.string.msg_unexpected_login_error);
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			success = false;
			Str_Message = this.ctx.getString(R.string.msg_unexpected_login_error);
			e.printStackTrace();
		} 
		catch (JSONException e)
		{
			success = false;
			Str_Message = this.ctx.getString(R.string.msg_unexpected_login_error);
			e.printStackTrace();
		}
		Log.e("webservice_reply", webservice_reply);
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
	
		mCallBack.onLoginResult(success,Str_Code, Str_Message);
	}

}
