package com.keshima.bazr;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import utils.Utils;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Base64;
import android.util.Log;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import fragments.CategoryGridFragment;
import fragments.NavigationListFragment;

public class FragmentChangeActivity extends BaseActivity
{
	public static String MODULE = "FragmentChangeActivity";
	public static String TAG = "";

	private Fragment mContent;
	public static SharedPreferences prefs;

	public FragmentChangeActivity() 
	{
		super(R.string.app_name);
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);		
		TAG="onCreate";
		Log.d(MODULE,TAG);
		// set the Above View
		FacebookSdk.sdkInitialize(getApplicationContext());
		GetKeyHash();
		mContent = new CategoryGridFragment();
		prefs = getSharedPreferences("prefs", Context.MODE_PRIVATE);
		// set the Above View
		setContentView(R.layout.content_frame);
		
		FragmentTransaction ft;
		FragmentManager fm = getSupportFragmentManager();
		if (Utils.isConnectingToInternet(this))
		{
			ft = fm.beginTransaction();
			ft.replace(R.id.content_frame, mContent, "CategoryGridFragment");
			ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
			ft.commit();
		} 
		else
		{
			Utils.showAlert(this, "No Internet Connection");
		}
		// set the Behind View
		setBehindContentView(R.layout.menu_frame);
		getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.blue))); 
		getSupportFragmentManager().beginTransaction().replace(R.id.menu_frame, new NavigationListFragment()).commit();
		// customize the SlidingMenu
		getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
	}

	@Override
	public void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);		
	}

	public void switchContent(Fragment fragment) 
	{
		TAG="switchContent";
		Log.d(MODULE,TAG);
		
		Log.d(MODULE,TAG + " Fragment is " + fragment);
		
		mContent = fragment;
		FragmentManager manager = getSupportFragmentManager();
		if (manager.getBackStackEntryCount() > 0)
		{
			FragmentManager.BackStackEntry first = manager.getBackStackEntryAt(0);
			manager.popBackStack(first.getId(),FragmentManager.POP_BACK_STACK_INCLUSIVE);
		}
		
		FragmentTransaction ft;
		FragmentManager fm = getSupportFragmentManager();
		if (Utils.isConnectingToInternet(this)) 
		{
			ft = fm.beginTransaction();
			ft.replace(R.id.content_frame, mContent, "CategoryGridFragment");
			ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
			ft.commit();
			toggle();
		} 
		else
		{
			Utils.showAlert(this, "No Internet Connection");
		}
	}

	@Override
	public void onBackPressed() 
	{
		super.onBackPressed();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		TAG="onActivityResult";
		Log.d(MODULE,TAG);

		if (requestCode == 2 || requestCode == 100)
		{
			Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
			fragment.onActivityResult(requestCode, resultCode, data);
		}
		else
		{
			super.onActivityResult(requestCode, resultCode, data);
		}

	}

	/*
	 * public boolean onCreateOptionsMenu(Menu menu) { MenuInflater inflater =
	 * getSupportMenuInflater(); inflater.inflate(R.menu.product_list_menu,
	 * menu); return true; }
	 * 
	 * @Override public boolean onOptionsItemSelected(MenuItem item) { switch
	 * (item.getItemId()) { case R.id.refresh: // Do Activity menu item stuff
	 * here Log.e("menu ", "refresh");
	 * 
	 * return true; case R.id.map: // Not implemented here Log.e("menu ",
	 * "map"); return false; default: break; }
	 * 
	 * return false; }
	 */
	
	public void GetKeyHash()
	{
		try 
		{

			PackageInfo info = getPackageManager().getPackageInfo(getPackageName(),PackageManager.GET_SIGNATURES);
			for (Signature signature : info.signatures) 
			{
				MessageDigest md = MessageDigest.getInstance("SHA");
				md.update(signature.toByteArray());
				Log.e("KeyHash:",Base64.encodeToString(md.digest(), Base64.DEFAULT));
			}
		}
		catch (NameNotFoundException e) 
		{
			Log.e("name not found", e.toString());
		} 
		catch (NoSuchAlgorithmException e) 
		{
			Log.e("no such an algorithm", e.toString());
		}
	}
	
	@Override
	protected void onDestroy() 
	{
		super.onDestroy();
		LoginManager.getInstance().logOut();
	}

}
