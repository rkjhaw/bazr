package fragments;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import listener.Category_Listener;
import model.Category;
import utils.ImageUtils;
import utils.Utils;
import webservices.LoadCategory;
import webservices.TaskCompleted;
import adapters.CategoryAdapter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.Watson.OnOptionsItemSelectedListener;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

import com.keshima.bazr.R;

public class CategoryGridFragment extends Fragment implements Category_Listener,OnItemClickListener,
		android.support.v4.app.Watson.OnCreateOptionsMenuListener,
		OnOptionsItemSelectedListener 
{
	
	

	public static String MODULE = "CategoryGridFragment";
	public static String TAG = "";

	CategoryGridFragment mFragment;
	
	GridView categoryGridView;
	public static ArrayList<Category> Lst_Categories = new ArrayList<Category>();	
	boolean serverReady;
	private int columnWidth;
	FragmentActivity mActivity;

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		TAG="onCreate";
		Log.d(MODULE,TAG);
		
		mFragment=this;
		mActivity=mFragment.getActivity();
		Lst_Categories = new ArrayList<Category>();
		
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		TAG="onCreateView";
		Log.d(MODULE,TAG);
		
		View rootView = inflater.inflate(R.layout.activity_category_grid,container, false);		
		setHasOptionsMenu(true);		
					
		categoryGridView = (GridView) rootView.findViewById(R.id.category_grid);
		SetGridView();
		new LoadCategory(mFragment).execute();
		categoryGridView.setOnItemClickListener(this);
		try
		{
			PackageInfo info = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(),	PackageManager.GET_SIGNATURES);
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

		return rootView;
	}

	public void SetGridView()
	{
		TAG="SetGridView";
		Log.d(MODULE,TAG);
		
		if(Utils.getScreenOrientation(mActivity)==Configuration.ORIENTATION_PORTRAIT) 
			categoryGridView.setNumColumns(2);
		else  
			categoryGridView.setNumColumns(3);
	}
	
	public void setList(ArrayList<Category> list) 
	{
		TAG="setList";
		Log.d(MODULE,TAG);
		Lst_Categories = new ArrayList<Category>();
		Lst_Categories = list;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,long id)
	{
		TAG="onItemClick";
		Log.d(MODULE,TAG);
		
		Fragment fragment = new ProductListFragment();
		Bundle bundle = new Bundle();
		bundle.putString("category", Lst_Categories.get(position).getName());
		fragment.setArguments(bundle);

		try 
		{
			getFragmentManager().beginTransaction().add(R.id.content_frame, fragment)
					.hide(getFragmentManager().findFragmentByTag("CategoryGridFragment"))
					.addToBackStack("ProductListFragment").commit();
		} 
		catch (NullPointerException e) 
		{
			getFragmentManager().beginTransaction().add(R.id.content_frame, fragment)
					.hide(getFragmentManager().findFragmentByTag("CategoryGridFragment"))
					.addToBackStack("ProductListFragment").commit();
		}

	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();		
	}

	@Override
	public void onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu,com.actionbarsherlock.view.MenuInflater inflater) 
	{
		// TODO Auto-generated method stub
		menu.clear();
		inflater.inflate(R.menu.product_list_menu, menu);
		menu.findItem(R.id.map).setVisible(false);
	}

	@Override
	public boolean onOptionsItemSelected(com.actionbarsherlock.view.MenuItem item)
	{
		TAG="onOptionsItemSelected";
		Log.d(MODULE,TAG);
		
		switch (item.getItemId()) 
		{
			case R.id.refresh:
				 new LoadCategory(mFragment).execute();
				 break;
			default:
				 break;
		}
		return true;
	}

	private void moveToMap() 
	{
		TAG="moveToMap";
		Log.d(MODULE,TAG);
		
		Fragment fragment = new MapFragment();
		FragmentTransaction ft;
		FragmentManager fm = getFragmentManager();
		ft = fm.beginTransaction();
		// ft.hide(getFragmentManager().findFragmentByTag("ProductListFragment"));
		ft.add(R.id.content_frame, fragment);
		ft.addToBackStack("ProductDetailFragment");
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		ft.commit();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig)
	{
		TAG="onConfigurationChanged";
		Log.d(MODULE,TAG);				
		categoryGridView.setNumColumns(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE ? 3	: 2);
		super.onConfigurationChanged(newConfig);
	}
	private void InitilizeGridLayout(int NUM_OF_COLUMNS)
	{
		TAG="InitilizeGridLayout";
		Log.d(MODULE,TAG);	
		
		Resources r = getResources();
		float padding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,10, r.getDisplayMetrics());
		columnWidth = (int) ((Utils.getScreenWidth(getActivity()) - ((NUM_OF_COLUMNS + 1) * padding)) / NUM_OF_COLUMNS);

		categoryGridView.setNumColumns(NUM_OF_COLUMNS);
		categoryGridView.setColumnWidth(columnWidth);

		categoryGridView.setStretchMode(GridView.NO_STRETCH);
		categoryGridView.setPadding((int) padding, (int) padding,(int) padding, (int) padding);
		categoryGridView.setHorizontalSpacing((int) padding);
		categoryGridView.setVerticalSpacing((int) padding);
	}

	@Override
	public void onCategoriesLoaded(ArrayList<Category> Lst_Categories,String Str_Message) 
	{
		TAG="onCategoriesLoaded";
		Log.d(MODULE,TAG);
		
		CategoryGridFragment.Lst_Categories = new ArrayList<Category>();
		CategoryGridFragment.Lst_Categories = Lst_Categories;
		
		if (Lst_Categories.size() > 0)
		{
			Log.d(MODULE ,TAG + " >>> category size is not 0");
			categoryGridView.setAdapter(new CategoryAdapter(getActivity(),Lst_Categories));
		}
		else 
		{
			Log.d(MODULE ,TAG +  ">>> category size is 0");
			Toast.makeText(mActivity, Str_Message, Toast.LENGTH_LONG).show();
		}
		
	}

}
