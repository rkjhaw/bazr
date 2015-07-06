package com.keshima.bazr;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import fragments.NavigationListFragment;

public class BaseActivity extends SlidingFragmentActivity 
{
	
	public static String MODULE = "BaseActivity";
	public static String TAG = "";
	
	private int mTitleRes;
	protected Fragment mFrag;	

	public BaseActivity(int titleRes)
	{
		TAG = "BaseActivity";
		Log.d(MODULE, TAG);
		
		mTitleRes = titleRes;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		
		TAG = "onCreate";
		Log.d(MODULE, TAG);
		setTitle(mTitleRes);
		// set the Behind View
		setBehindContentView(R.layout.menu_frame);
		
		if (savedInstanceState == null) 
		{
			FragmentTransaction t = this.getSupportFragmentManager().beginTransaction();
			mFrag = new NavigationListFragment();
			t.replace(R.id.menu_frame, mFrag);
			t.commit();
		} 
		else
		{
			mFrag = new NavigationListFragment();
		}
		
		// customize the SlidingMenu
		SlidingMenu sm = getSlidingMenu();
		sm.setShadowDrawable(R.drawable.shadow);
		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		sm.setFadeDegree(0.35f);
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onOptionsItemSelected(com.actionbarsherlock.view.MenuItem item) 
	{
		// TODO Auto-generated method stub
		switch (item.getItemId()) 
		{
			case android.R.id.home:
				toggle();
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
