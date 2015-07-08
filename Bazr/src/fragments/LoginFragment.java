package fragments;


import java.util.ArrayList;
import java.util.List;

import listener.Login_Listener;
import utils.Utils;
import webservices.LoginWebService;
import android.content.Intent;
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

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.keshima.bazr.FragmentChangeActivity;
import com.keshima.bazr.R;

public class LoginFragment extends Fragment implements OnClickListener,	Login_Listener 
{

	public static String MODULE = "LoginFragment";
	public static String TAG = "";
	
	Button btn_fblogin, signup, btn_login;
	//Your Facebook APP ID
	//Replace your App ID-416358841868997
	public static String code;
	public static boolean success;	
	String FILENAME = "AndroidSSO_data";
	EditText edtxt_email, edtxt_password;
	String email, password;
	public static FragmentManager fm;	
	public FragmentActivity mActivity;
	LoginButton loginButton;
	CallbackManager callbackManager;

	
	@Override
	public void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		TAG="onCreate";
		Log.d(MODULE,TAG);	
		
		mActivity=getActivity();
		
		
        callbackManager = CallbackManager.Factory.create();
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
	{
		TAG="onCreateView";
		Log.d(MODULE,TAG);
		
		View rootView = inflater.inflate(R.layout.login_layout, container,false);
		
		loginButton = (LoginButton) rootView.findViewById(R.id.login_button);
		signup = (Button) rootView.findViewById(R.id.signup);
		btn_login = (Button) rootView.findViewById(R.id.btn_login);

		edtxt_email = (EditText) rootView.findViewById(R.id.editText1);
		edtxt_password = (EditText) rootView.findViewById(R.id.editText2);
		fm = getFragmentManager();
		
		SetFacebook();
		
		btn_login.setOnClickListener(this);
		signup.setOnClickListener(this);
		
		
		return rootView;
	}

	
	public void SetFacebook()
	{
		List<String> permissions = new ArrayList<String>();
		permissions.add("user_friends");
		permissions.add("public_profile");
		loginButton.setReadPermissions(permissions);
		// If using in a fragment
		loginButton.setFragment(this);  		
		// Other app specific specialization
		// Callback registration
		loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() 
		{
			@Override
			public void onSuccess(LoginResult loginResult)
			{
			  Toast.makeText(mActivity, "Logged in successfully", Toast.LENGTH_SHORT).show();
			  moveToMyActivity();			
			}	
			@Override
			public void onCancel() 
			{
				Toast.makeText(mActivity, "Facebook Login canceled", Toast.LENGTH_SHORT).show();       
			}
	
			@Override
			public void onError(FacebookException exception) 
			{
				Toast.makeText(mActivity,getString(R.string.msg_unexpected_login_error), Toast.LENGTH_SHORT).show();  
			}
		});    
	}
	
	@Override
	public void onResume() 
	{
		// TODO Auto-generated method stub
		super.onResume();
		
	}
	
	@Override
	public void onPause() 
	{
		super.onPause();
        // Call the 'deactivateApp' method to log an app event for use in analytics and advertising
	    // reporting.  Do so in the onPause methods of the primary Activities that an app may be
	    // launched into.
	   
	 }
	
	@Override
	public void onClick(View v) 
	{
		
		TAG="onClick";
		Log.d(MODULE,TAG);
		
		switch (v.getId()) 
		{
			
			case R.id.signup:	
				 moveToSignUpPage();	
				 break;	
			case R.id.btn_login:	
				 manualLogin();	
				 break;	
			default:
				break;
		}
	}

	private void manualLogin()
	{
		TAG="manualLogin";
		Log.d(MODULE,TAG);		
		
		if(IsValid())
		{
			if(Utils.isOnline(mActivity)) new LoginWebService(this, email, password,this).execute();
			else Toast.makeText(mActivity, getResources().getString(R.string.msg_no_wifi),Toast.LENGTH_LONG).show();
		}		
	}
	public boolean IsValid()
	{
		TAG="IsValid";
		Log.d(MODULE,TAG);	
		
		boolean IsValid=true;
		
		email = edtxt_email.getText().toString().trim();
		password = edtxt_password.getText().toString().trim();
		
		if(email.length()==0)
		{
			IsValid=false;
			Toast.makeText(mActivity, getResources().getString(R.string.msg_blank_email),Toast.LENGTH_LONG).show();
		}
		
		else if(password.length()==0)
		{
			IsValid=false;
			Toast.makeText(mActivity, getResources().getString(R.string.msg_blank_password),Toast.LENGTH_LONG).show();
		}
		return IsValid;
	}
	private void moveToSignUpPage() 
	{
		TAG="moveToSignUpPage";
		Log.d(MODULE,TAG);
		
		Fragment fragment = new SignUpFragment();
		FragmentTransaction ft;
		ft = fm.beginTransaction();
		// ft.hide(getFragmentManager().findFragmentByTag("ProductListFragment"));
		ft.add(R.id.content_frame, fragment);
		ft.addToBackStack("ProductDetailFragment");
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		ft.commit();
	}

	

	private void moveToMyActivity() 
	{
		TAG="moveToMyActivity";
		Log.d(MODULE,TAG);
		
		FragmentChangeActivity.prefs.edit().putBoolean("loginned", true).commit();
		Fragment fragment = new MyActvitiesFragment();
		FragmentTransaction ft;
		ft = fm.beginTransaction();
		//ft.hide(getFragmentManager().findFragmentByTag("ProductListFragment"));
		ft.add(R.id.content_frame, fragment);
		ft.addToBackStack("MyActvitiesFragment");
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		ft.commit();
		NavigationListFragment.refresh();
	}

	@Override
	public void onLoginResult(boolean Succes, String Str_Code,String Str_Message) 
	{
		TAG="onLoginResult";
		Log.d(MODULE,TAG);
		
		Toast.makeText(mActivity, Str_Message,Toast.LENGTH_LONG).show();
		if(Str_Code.equals("3")) moveToMyActivity();		
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		super.onActivityResult(requestCode, resultCode, data);
		TAG="onLoginActivityResult";
		Log.d(MODULE,TAG + " requestCode : " + requestCode);
	    if(callbackManager.onActivityResult(requestCode, resultCode, data)) return;
	}
	
	
}
