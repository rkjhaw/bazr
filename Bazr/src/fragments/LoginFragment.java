package fragments;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

import listener.Login_Listener;
import model.Category;

import org.json.JSONException;
import org.json.JSONObject;

import utils.Utils;
import webservices.LoginWebService;
import webservices.TaskCompleted;
import android.content.Context;
import android.content.SharedPreferences;
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

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
import com.keshima.bazr.FragmentChangeActivity;
import com.keshima.bazr.R;

public class LoginFragment extends Fragment implements OnClickListener,	Login_Listener 
{

	public static String MODULE = "LoginFragment";
	public static String TAG = "";
	
	Button btn_fblogin, signup, btn_login;
	// Your Facebook APP ID
	private static String APP_ID = "416358841868997"; // Replace your App ID
	public static String code;
	public static boolean success;
	private Facebook facebook;
	@SuppressWarnings("deprecation")
	private AsyncFacebookRunner mAsyncRunner;
	String FILENAME = "AndroidSSO_data";
	private SharedPreferences mPrefs;
	EditText edtxt_email, edtxt_password;
	String email, password;
	public static FragmentManager fm;	
	public FragmentActivity mActivity;
	@Override
	public void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		TAG="onCreate";
		Log.d(MODULE,TAG);	
		
		mActivity=getActivity();
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
	{
		TAG="onCreateView";
		Log.d(MODULE,TAG);
		
		View rootView = inflater.inflate(R.layout.login_layout, container,false);
		btn_fblogin = (Button) rootView.findViewById(R.id.btn_fblogin);
		signup = (Button) rootView.findViewById(R.id.signup);
		btn_login = (Button) rootView.findViewById(R.id.btn_login);

		edtxt_email = (EditText) rootView.findViewById(R.id.editText1);
		edtxt_password = (EditText) rootView.findViewById(R.id.editText2);
		fm = getFragmentManager();
		btn_fblogin.setOnClickListener(this);
		btn_login.setOnClickListener(this);
		signup.setOnClickListener(this);
		facebook = new Facebook(APP_ID);
		mAsyncRunner = new AsyncFacebookRunner(facebook);
		
		return rootView;
	}

	@Override
	public void onClick(View v) 
	{
		
		TAG="onClick";
		Log.d(MODULE,TAG);
		
		switch (v.getId()) 
		{
			case R.id.btn_fblogin:	
				 loginToFacebook();	
				 break;	
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

	@SuppressWarnings("deprecation")
	public void loginToFacebook() 
	{
		TAG="loginToFacebook";
		Log.d(MODULE,TAG);		

		mPrefs = getActivity().getPreferences(Context.MODE_PRIVATE);
		String access_token = mPrefs.getString("access_token", null);
		long expires = mPrefs.getLong("access_expires", 0);
		if (access_token != null)
		{
			facebook.setAccessToken(access_token);
			Log.d("FB Sessions", "" + facebook.isSessionValid());
		}
		if (expires != 0) 
		{
			facebook.setAccessExpires(expires);
		}
		if (!facebook.isSessionValid())
		{
			facebook.authorize(getActivity(), new String[] { "email","publish_actions" }, new DialogListener() {

			@Override
			public void onCancel() {}
			@Override
			public void onComplete(Bundle values) 
			{
					// Function to handle complete event
					// Edit Preferences and update facebook acess_token
					Log.e("fb", "oncomplete");
					SharedPreferences.Editor editor = mPrefs.edit();
					editor.putString("access_token", facebook.getAccessToken());
					editor.putLong("access_expires",facebook.getAccessExpires());
					editor.commit();
					getProfileInformation();
			}

			@Override
			public void onError(DialogError error)
			{
					

			}
			@Override
			public void onFacebookError(FacebookError fberror) 
			{
				

			}});
		}
		else 
		{
			Log.e("fb", "session invlaid");
			getProfileInformation();
		}
	}

	@SuppressWarnings("deprecation")
	public void getProfileInformation()
	{
		mAsyncRunner.request("me", new RequestListener() 
		{
			@Override
			public void onComplete(String response, Object state) 
			{
				Log.v("Profile", response);
				String json = response;
				try 
				{
					JSONObject profile = new JSONObject(json);
					// getting name of the user
					String name = profile.getString("name");
					// getting email of the user
					String email = profile.getString("email");
					// String password = profile.getString("password");
					getActivity().runOnUiThread(new Runnable() 
					{
						@Override
						public void run() 
						{
							moveToMyActivity();
						}
					});
					// new LoginWebService(this, taskCompletedCallback, email,
					// password).execute();

				}
				catch (JSONException e)
				{
					e.printStackTrace();
				}
			}

			@Override
			public void onIOException(IOException e, Object state) {}
			@Override
			public void onFileNotFoundException(FileNotFoundException e,Object state) {}
			@Override
			public void onMalformedURLException(MalformedURLException e,	Object state) {}
			@Override
			public void onFacebookError(FacebookError e, Object state) {}
		});
	}

	private void moveToMyActivity() 
	{
		TAG="moveToMyActivity";
		Log.d(MODULE,TAG);
		
		FragmentChangeActivity.prefs.edit().putBoolean("loginned", true).commit();
		Fragment fragment = new MyActvitiesFragment();
		FragmentTransaction ft;
		ft = fm.beginTransaction();
		// ft.hide(getFragmentManager().findFragmentByTag("ProductListFragment"));
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

}
