package fragments;

import java.util.ArrayList;

import listener.Products_Listener;
import model.Product;
import utils.Utils;
import webservices.LoadProductsFragment;
import webservices.TaskCompleted;
import adapters.ProductListAdapter;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.Watson.OnCreateOptionsMenuListener;
import android.support.v4.app.Watson.OnOptionsItemSelectedListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;


import com.keshima.bazr.R;

public class ProductListFragment extends Fragment implements Products_Listener,
		OnItemClickListener, OnCreateOptionsMenuListener,
		OnOptionsItemSelectedListener, LocationListener 
{
	
	public static String MODULE = "ProductListFragment";
	public static String TAG = "";

	public static ListView productListView;
	public static String category;
	public static TaskCompleted taskCompletedCallback;
	public static Product selectedProduct;
	public static ArrayList<Product> products;
	public static ProductListFragment fragment;
	Spinner category_spinner, distance_spinner, price_spinner;
	String[] category_array, distance_array = { "DISTANCE-ALL", "0-3km", "3-5km",
			"5-10km", "10-30km" }, price_array = { "PRICE-ALL", "<5000",
			"5000-10000", "10000-20000", "20000-30000", ">30000" };
	private String filter_by_category;
	public static boolean filtered, category_filtered, price_filtered,
			distance_filtered;
	public static int category_filter_position, price_filter_position,
			dostance_filter_position;
	private Location mylocation;
	public static ArrayList<Location> locations;
	public static FragmentActivity mActivity;
	boolean refreshed;
	Fragment mFragment;
	ProductListAdapter adapter;
	LocationManager locationManager;
	Criteria criteria;
	String bestProvider;

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		TAG="onCreate";
		Log.d(MODULE,TAG);
		
		mFragment = this;
		mActivity=getActivity();
		
		setHasOptionsMenu(true);
		locations = new ArrayList<Location>();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,	Bundle savedInstanceState) 
	{
		TAG="onCreateView";
		Log.d(MODULE,TAG);
		
		View rootView = inflater.inflate(R.layout.activity_product_list,container, false);
		locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
		criteria = new Criteria();
		bestProvider = locationManager.getBestProvider(criteria, true).trim();
		
		if(bestProvider!=null)
		{
			if(bestProvider.equals(""))
			{
				Location location = locationManager.getLastKnownLocation(bestProvider);
				if (location != null) 
				{
					onLocationChanged(location);
				}
				locationManager.requestLocationUpdates(bestProvider, 20000, 0, this);
			}			
		}
				
		category = getArguments().getString("category");
		productListView = (ListView) rootView.findViewById(R.id.product_list);
		
		products = new ArrayList<Product>();
		
		filter();
		
		productListView.setOnItemClickListener(this);
		
		category_spinner = (Spinner) rootView.findViewById(R.id.category_spinner);
		distance_spinner = (Spinner) rootView.findViewById(R.id.distance_spinner);
		price_spinner = (Spinner) rootView.findViewById(R.id.price_spinner);

		category_array = new String[CategoryGridFragment.Lst_Categories.size() + 1];
		category_array[0] = "CATEGORY-ALL";
		
		for (int i = 0; i < CategoryGridFragment.Lst_Categories.size(); i++) 
		{

			category_array[i + 1] = CategoryGridFragment.Lst_Categories.get(i).getName();
		}

		ArrayAdapter<String> categoryArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,category_array);
		ArrayAdapter<String> distanceArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,distance_array);
		ArrayAdapter<String> priceArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,price_array);

		categoryArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		distanceArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		priceArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		category_spinner.setAdapter(categoryArrayAdapter);
		distance_spinner.setAdapter(distanceArrayAdapter);
		price_spinner.setAdapter(priceArrayAdapter);
		
		final ProductListFragment obj = this;
		
		category_spinner.setOnItemSelectedListener(new OnItemSelectedListener() 
		{

			@Override
			public void onItemSelected(AdapterView<?> parent,View view, int position, long id) 
			{
				TAG="category_spinner-onItemSelected";
				Log.d(MODULE,TAG);
				
				category_filter_position = position;
				if (position > 0) 
				{
					filtered = true;
					category_filtered = true;
					category = category_array[position];					
					filter();
				} 
				else 
				{
					category_filtered = false;
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent)
			{
					

			}
		});

		price_spinner.setOnItemSelectedListener(new OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,int position, long id) 
			{
				TAG="price_spinner-onItemSelected";
				Log.d(MODULE,TAG);				
				price_filter_position = position;
				if (position > 0) 
				{
					filtered = true;
					price_filtered = true;
					filter();
				} 
				else 
				{
					price_filtered = false;
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) 
			{
				
			}
		});

		distance_spinner.setOnItemSelectedListener(new OnItemSelectedListener()
		{

			@Override
			public void onItemSelected(AdapterView<?> parent,View view, int position, long id)
			{
				TAG="distance_spinner-onItemSelected";
				Log.d(MODULE,TAG);		
				// TODO Auto-generated method stub
				dostance_filter_position = position;
				if (position > 0)
				{
					filtered = true;
					distance_filtered = true;
					filter();

				}
				else 
				{
					distance_filtered = false;
				}

			}
			@Override
			public void onNothingSelected(AdapterView<?> parent)
			{
					
			}
		});
		return rootView;
	}

	protected void filter()
	{
		TAG="filter";
		Log.d(MODULE,TAG);	
		
		//Log.d(MODULE,TAG + " category_filter_position : " + category_filter_position);	
		//Log.d(MODULE,TAG + " price_filter_position : " + price_filter_position);
		//Log.d(MODULE,TAG + " dostance_filter_position : " + dostance_filter_position);
		
		new LoadProductsFragment(ProductListFragment.this,category, category_filtered,
				 price_filtered,distance_filtered, category_filter_position,
				 price_filter_position, dostance_filter_position).execute();
	}
	
	protected void filterByDistance(int position) 
	{
		TAG="filterByDistance";
		Log.d(MODULE,TAG);	
		
		int minDistance = 0, maxDistance = 0;
		switch (position) 
		{

			case -1:
				break;
			case 1:
				minDistance = 0;
				maxDistance = 3;
				break;
			case 2:
				minDistance = 3;
				maxDistance = 5;
				break;
			case 3:
				minDistance = 5;
				maxDistance = 10;
				break;
			case 4:
				minDistance = 10;
				maxDistance = 30;
				break;
	
			default:
				break;
		}

		for (int i = 0; i < products.size(); i++) 
		{
			boolean duplicate = false;
			double distance = products.get(i).getDistance();
			Log.e("distance", String.valueOf(distance));
			if (distance >= minDistance && distance <= maxDistance)
			{

			}
			else 
			{
				products.remove(i);
				i -= 1;
			}
		}
	}

	protected void filterByPrice(int position)
	{
		TAG="filterByPrice";
		Log.d(MODULE,TAG);	
		
		int minPrice = 0, maxPrice = 0;
		switch (position)
		{

			case -1:
				break;
			case 1:
				minPrice = 0;
				maxPrice = 5000;
				break;
			case 2:
				minPrice = 5000;
				maxPrice = 10000;
				break;
			case 3:
				minPrice = 10000;
				maxPrice = 20000;
				break;
			case 4:
				minPrice = 20000;
				maxPrice = 30000;
				break;
	
			case 5:
				minPrice = 30000;
				maxPrice = 100000;
				break;
	
			default:
				break;
		}

		for (int i = 0; i < products.size(); i++)
		{
			boolean duplicate = false;
			int price = Integer.parseInt(products.get(i).getPrice());
			if (price >= minPrice && price <= maxPrice) 
			{

			}
			else 
			{
				Log.d(MODULE,TAG + " >>> price added " + String.valueOf(price));
				products.remove(i);
				i -= 1;
			}
		}
	}

	public void setList(ArrayList<Product> products)
	{
		// TODO Auto-generated method stub
		// this.products = products;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,	long id)
	{
		TAG="product list fragment-onItemClick";
		Log.d(MODULE,TAG);	

		selectedProduct = products.get(position);
		Fragment fragment = new ProductDetailFragment();
		Bundle bundle = new Bundle();
		bundle.putString("isComingFrom", "ProductListFragment");
		fragment.setArguments(bundle);
		getFragmentManager().beginTransaction()	.add(R.id.content_frame, fragment)
				.hide(this).addToBackStack("ProductDetailFragment").commit();
	}
	
	@Override
	public void onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu,com.actionbarsherlock.view.MenuInflater inflater)
	{
		menu.clear();
		inflater.inflate(R.menu.product_list_menu, menu);
		menu.findItem(R.id.map).setVisible(true);
	}

	@Override
	public boolean onOptionsItemSelected(com.actionbarsherlock.view.MenuItem item)
	{
		switch (item.getItemId()) 
		{
				
			case R.id.refresh:				
				 TAG="refresh clicked";
				 Log.d(MODULE,TAG);					 
				 	filter();
				 break;				 
			case R.id.map:
				 TAG="map clicked";
				 Log.d(MODULE,TAG);	
				 if (products.size() > 0)moveToMap();
				 else Utils.showAlert(getActivity(), "No Products Found");
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
		
		Bundle bundle = new Bundle();
		bundle.putString("isComingFrom", "ProductListFragment");
		bundle.putParcelableArrayList("b_product_list",products);
		bundle.putParcelableArrayList("b_locations_list",locations);
		
		fragment.setArguments(bundle);
		
		FragmentManager fm = getFragmentManager();
		ft = fm.beginTransaction();		
		
		ft.add(R.id.content_frame, fragment);
		ft.hide(this);
		ft.addToBackStack("MapFragment");		
		ft.commit();
	}

	@Override
	public void onLocationChanged(Location location)
	{
		//TAG="onLocationChanged";
		//Log.d(MODULE,TAG);	
		
		mylocation = location;
		//Log.d(MODULE,TAG + " >>> lat : " + String.valueOf(location.getLatitude()));
		try 
		{
			if (!refreshed) 
			{
				adapter = new ProductListAdapter(getActivity(), products,mylocation);
				productListView.setAdapter(adapter);
				refreshed = true;
			}
		} 
		catch (NullPointerException e)
		{
		}
	}

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
	public void onProductLoadResult(ArrayList<Product> Lst_Products) 
	{
		this.products = Lst_Products; 
		TAG="onProductLoadResult";
		Log.d(MODULE,TAG);	
		
		if (price_filtered) 
		{
			filterByPrice(price_filter_position);
		}
		
		Log.d(MODULE,TAG + "distance filtered" + String.valueOf(distance_filtered));
		
		if (distance_filtered) 
		{
			filterByDistance(dostance_filter_position);
		}

		adapter = new ProductListAdapter(mActivity,products,mylocation);
		productListView.setAdapter(adapter);

		String lat, lon;
		double lt, lg;
		
		for (int i = 0; i < products.size(); i++)
		{

			String adds = products.get(i).getLocation().toString();
			String addrs[] = adds.split("|||");
			lat = adds.substring(0, adds.indexOf("|||"));
			lon = adds.substring(lat.length() + 3,adds.indexOf("|||", lat.length() + 3));

			Log.e("latit ", lat);
			Log.e("longit ", lon);			

			lt = Double.parseDouble(lat);
			lg = Double.parseDouble(lon);
			
			Location location;

			location = new Location("productLocs");
			location.setLatitude(lt);
			location.setLongitude(lg);
			locations.add(location);
		}
		
	}

}
