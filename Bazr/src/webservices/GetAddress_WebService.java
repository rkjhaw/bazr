package webservices;

import listener.Address_Received_Listener;
import utils.Utils;
import fragments.LoginFragment;
import android.app.ProgressDialog;
import android.location.Location;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;


@SuppressWarnings("deprecation")
public class GetAddress_WebService extends AsyncTask<Void, Void, String>
{

	public static String MODULE = "LoginWebService";
	public static String TAG = "";
	
	Address_Received_Listener mCallBack;
	ProgressDialog dialog;
	String category;
	FragmentActivity mActivity;
	String email, password;
	LoginFragment fragment;
	boolean success;
	private Location myLoaction;
	private String Str_Message="";
	Fragment mFragment;

	public GetAddress_WebService(Fragment mFragment,Location myLoaction) 
	{
		TAG="LoginWebService";
		Log.d(MODULE,TAG);	
		
		this.mFragment = mFragment;
		this.mActivity = mFragment.getActivity();
		this.myLoaction = myLoaction;		
		mCallBack = (Address_Received_Listener) mFragment;
	}

	@Override
	protected void onPreExecute() 
	{
		super.onPreExecute();
		
		TAG="onPreExecute";
		Log.d(MODULE,TAG);			
	}

	@SuppressWarnings("deprecation")
	@Override
	protected String doInBackground(Void... parameters) 
	{
		TAG="doInBackground";
		Log.d(MODULE,TAG);	
		
		String address = String.valueOf(Utils.getCompleteAddressString(mActivity,myLoaction.getLatitude(), myLoaction.getLongitude()));
		
		return address;
	}

	@Override
	protected void onPostExecute(String result)
	{
		super.onPostExecute(result);
		
		TAG="onPostExecute";
		Log.d(MODULE,TAG+result);	
		mCallBack.onAddressReceivedResult(result);
	}

}
