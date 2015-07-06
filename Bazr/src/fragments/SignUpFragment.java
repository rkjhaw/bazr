package fragments;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import listener.SignUp_Listener;
import utils.Utils;
import utils.ValidationUtil;
import webservices.SignUpWebService;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.keshima.bazr.R;

public class SignUpFragment extends Fragment implements OnClickListener,SignUp_Listener
{
	public static String MODULE = "SignUpFragment";
	public static String TAG = "";
	
	EditText edtxt_name, edtxt_email, edtxt_password, edtxt_contact_no,	edtxt_address;
	String Str_Name, Str_Email, Str_Password, Str_Contact_No, Str_Address;
	Button btn_Register;
	private Location myLocation;
	protected LocationManager locationManager;
	boolean locationEnabled;
	public static boolean success;
	public static Context context;
	public static String code;
	public static FragmentManager fm;
	public FragmentActivity mActivity;
	
	@Override
	public void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		TAG="onCreate";
		Log.d(MODULE,TAG);	
		
		mActivity=getActivity();
		fm = getFragmentManager();
		locationEnabled = Utils.isLocationEnabled(getActivity());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) 
	{
		TAG="onCreateView";
		Log.d(MODULE,TAG);
		
		View rootView = inflater.inflate(R.layout.register_layout, container,false);
		edtxt_name = (EditText) rootView.findViewById(R.id.name);
		edtxt_email = (EditText) rootView.findViewById(R.id.email);
		edtxt_password = (EditText) rootView.findViewById(R.id.password);
		edtxt_contact_no = (EditText) rootView.findViewById(R.id.number);
		edtxt_address = (EditText) rootView.findViewById(R.id.address);

		btn_Register = (Button) rootView.findViewById(R.id.btn_login);
		btn_Register.setOnClickListener(this);
		locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,	0, locationListener);
		Log.e("location enabled is ", String.valueOf(locationEnabled));
		
		return rootView;
	}

	LocationListener locationListener = new LocationListener()
	{
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras)    
		{						
		}		
		@Override
		public void onProviderEnabled(String provider) 
		{		
		}		
		@Override
		public void onProviderDisabled(String provider) 
		{			
		}		
		@Override
		public void onLocationChanged(Location location)
		{	
			//TAG="onLocationChanged";
			//Log.d(MODULE,TAG);		
			if (mActivity != null) 
			{
				myLocation = location;
				Str_Address = "";
				Str_Address = String.valueOf(Utils.getCompleteAddressString(mActivity,location.getLatitude(), location.getLongitude()));
				edtxt_address.setText("");
				edtxt_address.setText(Str_Address);
			}
		}
	};
	
	private void enableLocationService()
	{
		TAG="enableLocationService";
		Log.d(MODULE,TAG);
		
		Utils.showSettingsAlert(getActivity());
		locationEnabled = true;
	}

	@Override
	public void onClick(View v)
	{		
		switch (v.getId()) 
		{
			case R.id.btn_login:
				 signUp();
				 break;	
			default:
				 break;
		}
	}

	private void signUp() 
	{
		TAG="signUp";
		Log.d(MODULE,TAG);

		if (!locationEnabled) 
		{
			enableLocationService();
		} 
		
		else if(myLocation!=null)
		{
			if(IsValid())
			{
				if(Utils.isOnline(mActivity)) new SignUpWebService(this,GetSignUpParams()).execute();
				else Toast.makeText(mActivity, getResources().getString(R.string.msg_no_wifi),Toast.LENGTH_LONG).show();
			}			
		}
		else Utils.showAlert(getActivity(),getString(R.string.msg_wait_gps));	
	}
	
	public ArrayList<NameValuePair> GetSignUpParams()
	{
		TAG = "GetSignUpParams";
		Log.d(MODULE, TAG);
		
		ArrayList<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(2);
		try
		{			
			nameValuePair.add(new BasicNameValuePair("sellerName",Str_Name));	
			nameValuePair.add(new BasicNameValuePair("emailId",Str_Email));	
			nameValuePair.add(new BasicNameValuePair("sellerPassword",Str_Password));
			nameValuePair.add(new BasicNameValuePair("mobile",Str_Contact_No));
			nameValuePair.add(new BasicNameValuePair("address",Str_Address));
			nameValuePair.add(new BasicNameValuePair("userType",""));
			if(myLocation!=null)
			{
				nameValuePair.add(new BasicNameValuePair("longitude",Double.toString(myLocation.getLatitude())));
				nameValuePair.add(new BasicNameValuePair("latitude",Double.toString(myLocation.getLatitude())));
			}
											
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}		
		return nameValuePair;
	}
	
	public boolean IsValid()
	{
		TAG="IsValid";
		Log.d(MODULE,TAG);	
		
		boolean IsValid=true;
		
		Str_Name = edtxt_name.getText().toString().trim();
		Str_Email = edtxt_email.getText().toString().trim();
		Str_Password = edtxt_password.getText().toString().trim();
		Str_Contact_No = edtxt_contact_no.getText().toString().trim();
		Str_Address = edtxt_address.getText().toString().trim();
		
		if(Str_Name.length()==0)
		{
			IsValid=false;
			Toast.makeText(mActivity, getResources().getString(R.string.msg_blank_name),Toast.LENGTH_LONG).show();
		}	
		else if(Str_Email.length()==0)
		{
			IsValid=false;
			Toast.makeText(mActivity, getResources().getString(R.string.msg_blank_email),Toast.LENGTH_LONG).show();
		}
		else if(!ValidationUtil.isValidEmailId(Str_Email))
		{
			IsValid=false;
			Toast.makeText(mActivity, getResources().getString(R.string.msg_valid_email),Toast.LENGTH_LONG).show();
		}		
		else if(Str_Password.length()==0)
		{
			IsValid=false;
			Toast.makeText(mActivity, getResources().getString(R.string.msg_blank_password),Toast.LENGTH_LONG).show();
		}
		else if(Str_Contact_No.length()==0)
		{
			IsValid=false;
			Toast.makeText(mActivity, getResources().getString(R.string.msg_blank_contactno),Toast.LENGTH_LONG).show();
		}
		return IsValid;
	}

	public void Goto_LoginFragment()
	{
		TAG="Goto_LoginFragment";
		Log.d(MODULE,TAG);
		
		Fragment fragment = new LoginFragment();
		FragmentTransaction ft;

		ft = fm.beginTransaction();
		ft.add(R.id.content_frame, fragment);
		ft.addToBackStack("SignUpFragment");
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		ft.commit();
	}
	@Override
	public void onSignUpResult(boolean Succes, String Str_Code,	String Str_Message) 
	{
		TAG="onSignUpResult";
		Log.d(MODULE,TAG);
		
		Toast.makeText(mActivity, Str_Message,Toast.LENGTH_LONG).show();
		if(Str_Code!=null) if(Str_Code.equals("3")) Goto_LoginFragment();			
	}
}
