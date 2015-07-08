package fragments;

import utils.Utils;
import adapters.NavigationListAdapter;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.facebook.login.LoginManager;
import com.keshima.bazr.FragmentChangeActivity;
import com.keshima.bazr.R;

public class NavigationListFragment extends Fragment implements	OnItemClickListener 
{

	public static String MODULE = "NavigationListFragment";
	public static String TAG = "";
	
	static ListView naviagtionList;

	static String[] menus = {"Home", "Login", "Categories", "Sell Items",
			"MyActivities", "Settings", "Share with Friends", "Help/About",
			"Logout" },

	menuNotLoginned = { "Home", "Login", "Categories","Share with Friends", "Help/About" },

	menuLoginned = { "Home", "Categories", "Sell Items", "MyActivities",
			"Settings", "Share with Friends", "Help/About", "Logout" };
	public static boolean loginned;
	static Context ctx;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		
		TAG="onCreate";
		Log.d(MODULE,TAG);
		
		View rootView = inflater.inflate(R.layout.navigation_list, container,false);		
		loginned = FragmentChangeActivity.prefs.getBoolean("loginned", false);
		
		ctx = getActivity();
		
		if (loginned) menus = menuLoginned;
		else menus = menuNotLoginned;		
		
		naviagtionList = (ListView) rootView.findViewById(R.id.navigation_list);
		naviagtionList.setOnItemClickListener(this);
		naviagtionList.setAdapter(new NavigationListAdapter(getActivity(),menus));
		return rootView;
	}

	public static void refresh()
	{
		TAG="refresh";
		Log.d(MODULE,TAG);
		
		loginned = FragmentChangeActivity.prefs.getBoolean("loginned", false);
		if (loginned) menus = menuLoginned;
		else menus = menuNotLoginned;		
		naviagtionList.setAdapter(new NavigationListAdapter(ctx, menus));
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,	long id) 
	{
		// TODO Auto-generated method stub
		TAG="onItemClick";
		Log.d(MODULE,TAG);
		
		Fragment fragment = null;
		view.setSelected(true);
		
		switch (menus[position]) 
		{
			case "Home":
				fragment = new CategoryGridFragment();
				break;

			case "Login":
				 fragment = new LoginFragment();
				 break;

			case "Categories":
				 fragment = new CategoryGridFragment();
				 break;

			case "Sell Items":
				 fragment = new ProductUploadFragment();
				 break;

			case "MyActivities":
				 fragment = new MapFragment();
				 break;

			case "Settings":
				 fragment = new SettingsFragment();
				 break;

			case "Share with Friends":
				 Utils.showAlert(getActivity(), "In Development Proocess");
				 break;

			case "Help/About":
				 Utils.showAlert(getActivity(), "In Development Proocess");
				 break;

			case "Logout":
				  FragmentChangeActivity.prefs.edit().putBoolean("loginned", false).commit();
				  LoginManager.getInstance().logOut();
				  Toast.makeText(getActivity(), "Logout Successfully",Toast.LENGTH_LONG).show();
				  loginned = false;
				  menus = menuNotLoginned;
				  naviagtionList.setAdapter(new NavigationListAdapter(getActivity(),menus));
				  break;

			default:
				break;
		}
		if (fragment != null)switchFragment(fragment);
	}

	private void switchFragment(Fragment fragment) 
	{
		TAG="switchFragment";
		Log.d(MODULE,TAG);
		
		if (getActivity() == null)
			return;			
		if (getActivity() instanceof FragmentChangeActivity) 
		{
			if (Utils.isConnectingToInternet(getActivity())) 
			{
				FragmentChangeActivity fca = (FragmentChangeActivity) getActivity();
				fca.switchContent(fragment);
			} 
			else
			{
				Utils.showAlert(getActivity(), "No Internet Connection ");
			}
		}
	}

}
