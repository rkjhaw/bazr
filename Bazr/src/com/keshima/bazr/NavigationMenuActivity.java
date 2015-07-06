package com.keshima.bazr;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivity;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import fragments.CategoryGridFragment;
import adapters.NavigationListAdapter;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

public class NavigationMenuActivity extends SlidingFragmentActivity {
	SlidingMenu slidigMenu;
	LayoutInflater inflater;
	View behindView;
	ListView navigationList;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		/*
		 * setContentView(R.layout.activity_sliding_menu);
		 */inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		behindView = inflater.inflate(R.layout.navigation_list, null);

		setContentView(R.layout.activity_sliding_menu);
		Fragment fragment = new CategoryGridFragment();

		getSupportFragmentManager().beginTransaction()
				.replace(R.id.fragment_container, fragment).commit();

		setBehindContentView(behindView);

		navigationList = (ListView) behindView
				.findViewById(R.id.navigation_list);
		

		slidigMenu = (SlidingMenu) findViewById(R.id.slidingmenulayout);
		SlidingMenu sm = getSlidingMenu();
		sm.setShadowWidthRes(R.dimen.shadow_width);
		sm.setShadowDrawable(R.drawable.shadow);
		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		sm.setFadeDegree(0.35f);
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);

	}

}
