package webservices;

import java.util.ArrayList;
import listener.SignUp_Listener;
import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import com.keshima.bazr.R;
import utils.ConnectionUtil;
import utils.Constants;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

public class SignUpWebService extends AsyncTask<Void, Void, Void>
{
	public static String MODULE = "SignUpWebService";
	public static String TAG = "";
	
	SignUp_Listener mCallBack;
	ProgressDialog dialog;
	boolean success;
	private String Str_Code, Str_Message;
	Fragment mFragment;
	FragmentActivity mActivity;
	ArrayList<NameValuePair> params;
	
	public SignUpWebService(Fragment mFragment,ArrayList<NameValuePair> params) 
	{
		
		TAG="SignUpWebService";
		Log.d(MODULE,TAG);	
		
		this.mFragment = mFragment;
		this.mActivity = mFragment.getActivity();	
		this.params = params;
		mCallBack = (SignUp_Listener) mFragment;
	}

	@Override
	protected void onPreExecute()
	{
		super.onPreExecute();
		
		TAG="onPreExecute";
		Log.d(MODULE,TAG);	
		
		dialog = new ProgressDialog(mActivity);
		dialog.setMessage("Registering...");
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		dialog.show();
	}

	
	@Override
	protected Void doInBackground(Void... parameters) 
	{
		TAG="doInBackground";
		Log.d(MODULE,TAG);	
	
		try 
		{
			ConnectionUtil ObjCon = new ConnectionUtil();			
			String Str_Response = ObjCon.makeServiceCall(Constants.G_SIGNUP_URL, ConnectionUtil.GET, params, mActivity);
			Log.e(MODULE,TAG + "Response from Server : " + Str_Response);	
			try
			{
				if (Str_Response != null && (!Str_Response.equals("UH")) && (!Str_Response.equals("INA"))) 
				{
	               try 
	               {
	            	   JSONObject json = new JSONObject(Str_Response);
		       		   Str_Code = json.getString("Code");
		       		   if(Str_Code.equals("3"))	
		       		   {
		       			   success = true;
		       			   Str_Message = json.getString("Message");				
		       		   }
		       		   else if(Str_Code.equals("2"))	
		       		   {
		       				success = false;
		       				Str_Message = json.getString("Message");
		       		   }		
		       		   success = true;
	               }
	               catch (JSONException e) 
	               {
						e.printStackTrace();
						success = false;
						Str_Message = this.mActivity.getString(R.string.msg_unexpected_error);
	               }
				}
				else if((Str_Response.equals("UH")) || (Str_Response.equals("INA")))
		        {
					Str_Message = this.mActivity.getString(R.string.msg_failed_to_connect_Server);	
					success = false;					         	
		        }
		        else
		        {
		        	success = false;
					Str_Message = this.mActivity.getString(R.string.msg_unexpected_error);					
		        }
			}
			catch (Exception e) 
			{			
				e.printStackTrace();
				success = false;
				Str_Message = this.mActivity.getString(R.string.msg_unexpected_error);
				
			} 			
		} 
		catch (Exception e) 
		{			
			e.printStackTrace();
			success = false;
			Str_Message = this.mActivity.getString(R.string.msg_unexpected_error);			
		} 		
		
		return null;

	}

	@Override
	protected void onPostExecute(Void result) 
	{
		super.onPostExecute(result);
		
		TAG="onPostExecute";
		Log.d(MODULE,TAG);	
		
		if (dialog.isShowing())
			dialog.dismiss();
		mCallBack.onSignUpResult(success, Str_Code, Str_Message);
	}

}
