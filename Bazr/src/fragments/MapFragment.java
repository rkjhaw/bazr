package fragments;

import java.util.ArrayList;

import utils.Utils;
import model.Product;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.Watson.OnCreateOptionsMenuListener;
import android.support.v4.app.Watson.OnOptionsItemSelectedListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.MenuItem;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.keshima.bazr.R;

public class MapFragment extends SherlockFragment implements LocationListener,OnOptionsItemSelectedListener, OnCreateOptionsMenuListener,OnMarkerClickListener 
{
	public static String MODULE = "MapFragment";
	public static String TAG = "";

	MapView mMapView;
	private GoogleMap googleMap;
	private MarkerOptions currentMarker;
	private MarkerOptions marker;
	ArrayList<Location> locations;
	ArrayList<MarkerOptions> markers;
	public static Product selectedProduct;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		TAG="onCreate";
		Log.d(MODULE,TAG);
		
		setHasOptionsMenu(true);
		locations = ProductListFragment.locations;
		markers = new ArrayList<MarkerOptions>();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) 
	{
		TAG="onCreateView";
		Log.d(MODULE,TAG);
		
		View v = inflater.inflate(R.layout.map_fragment, container, false);
		mMapView = (MapView) v.findViewById(R.id.mapView);
		mMapView.onCreate(savedInstanceState);
		mMapView.onResume();// needed to get the map to display immediately
		try 
		{
			MapsInitializer.initialize(getActivity().getApplicationContext());
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}

		googleMap = mMapView.getMap();
		// latitude and longitude
		double latitude = 17.385044;
		double longitude = 78.486671;
		googleMap.setOnMarkerClickListener(this);
		googleMap.setMyLocationEnabled(true);
		// create marker
		LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		String bestProvider = locationManager.getBestProvider(criteria, true);
		if(bestProvider!=null)
		{
			Location location = locationManager.getLastKnownLocation(bestProvider);
			if (location != null) 
			{
				onLocationChanged(location);
			}
		}		
		locationManager.requestLocationUpdates(bestProvider, 20000, 0, this);
		Bitmap.Config conf = Bitmap.Config.ARGB_8888;
		Bitmap bmp = Bitmap.createBitmap(80, 80, conf);
		Canvas canvas1 = new Canvas(bmp);
		// paint defines the text color,
		// stroke width, size
		Paint color = new Paint();
		color.setTextSize(25);
		color.setColor(Color.BLACK);
		Bitmap mark = BitmapFactory.decodeResource(getResources(),R.drawable.marker);
		mark = Bitmap.createScaledBitmap(mark, 60, 60, false);
		// modify canvas
		canvas1.drawBitmap(BitmapFactory.decodeResource(getResources(),	R.drawable.pointer), 0, 0, color);
		canvas1.drawText("User Name!", 30, 30, color);
		for (int i = 0; i < ProductListFragment.products.size(); i++) 
		{

			MarkerOptions prodMarker = new MarkerOptions().position(new LatLng(locations.get(i).getLatitude(),locations.get(i).getLongitude()))
					.title(ProductListFragment.products.get(i).getProductId())
					.anchor(0.5f, 1);
			prodMarker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE));
			// prodMarker.icon(BitmapDescriptorFactory.fromBitmap(bmp));
			markers.add(prodMarker);
			googleMap.addMarker(markers.get(i));
		}

		if (locations.size() > 0) 
		{
			CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(locations.get(0).getLatitude(),locations.get(0).getLongitude())).zoom(6).build();
			googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
		}
		
		return v;
	}

	@Override
	public void onResume() 
	{
		super.onResume();
		
		TAG="onResume";
		Log.d(MODULE,TAG);
		
		mMapView.onResume();
	}

	@Override
	public void onPause() 
	{
		super.onPause();
		
		TAG="onPause";
		Log.d(MODULE,TAG);	
		
		mMapView.onPause();
	}

	@Override
	public void onDestroy() 
	{
		super.onDestroy();
		
		TAG="onDestroy";
		Log.d(MODULE,TAG);	
		
		mMapView.onDestroy();
	}

	@Override
	public void onLowMemory() 
	{
		super.onLowMemory();
		
		TAG="onLowMemory";
		Log.d(MODULE,TAG);
		
		mMapView.onLowMemory();
	}

	@Override
	public void onLocationChanged(Location location) 
	{
		TAG="onLocationChanged";
		Log.d(MODULE,TAG);
		
		double longitude = location.getLongitude();
		double latitude = location.getLatitude();

		googleMap.clear();
		LatLng latLng = new LatLng(latitude, longitude);
		currentMarker = new MarkerOptions().position(latLng).title("I am here");

		currentMarker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

		googleMap.addMarker(currentMarker).showInfoWindow();
		Log.d(MODULE, TAG + " Marker size is : " + markers.size());
		Log.d(MODULE, TAG + " locations size is : " + locations.size());
		for (int i = 0; i < locations.size(); i++) 
		{

			/*
			 * MarkerOptions prodMarker = new MarkerOptions().position( new
			 * LatLng(locations.get(i).getLatitude(), locations.get(i)
			 * .getLongitude()))
			 * .title(ProductListFragment.products.get(i).getProductName())
			 * .anchor(0.5f, 1);
			 * 
			 * prodMarker.icon(BitmapDescriptorFactory
			 * .defaultMarker(BitmapDescriptorFactory.HUE_ROSE));
			 * 
			 * // prodMarker.icon(BitmapDescriptorFactory.fromBitmap(bmp));
			 * 
			 * markers.add(prodMarker);
			 */
			
			if (markers.size() > 0 && (markers.size() == (locations.size())))
				googleMap.addMarker(markers.get(i)).showInfoWindow();
		}
		googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
		googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras){}

	@Override
	public void onProviderEnabled(String provider) {}
	@Override
	public void onProviderDisabled(String provider) {}
	@Override
	public void onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu,com.actionbarsherlock.view.MenuInflater inflater) 
	{
		menu.clear();
		inflater.inflate(R.menu.map_menu, menu);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		TAG="onOptionsItemSelected";
		Log.d(MODULE,TAG);
		
		getFragmentManager().popBackStack();
		return true;
	}

	@Override
	public boolean onMarkerClick(Marker m)
	{
		TAG="onMarkerClick";
		Log.d(MODULE,TAG);

		boolean isClickedCorrectly = false;

		String id = m.getTitle();

		for (int i = 0; i < markers.size(); i++)
		{
			if (ProductListFragment.products.get(i).getProductId().equals(id)) 
			{
				selectedProduct = ProductListFragment.products.get(i);
				isClickedCorrectly = true;
				break;
			}
		}
		if (isClickedCorrectly)	moveToProductDetailFragment();
		else Utils.showAlert(getActivity(),	"Please choose the customer's location");
		return true;
	}

	private void moveToProductDetailFragment() 
	{
		TAG="moveToProductDetailFragment";
		Log.d(MODULE,TAG);
		
		ProductDetailFragment.selectedProduct = ProductListFragment.products.get(0);
		Fragment fragment = new ProductDetailFragment();
		Bundle bundle = new Bundle();
		bundle.putString("isComingFrom", "MapFragment");
		fragment.setArguments(bundle);
		FragmentTransaction ft;
		FragmentManager fm = getFragmentManager();
		ft = fm.beginTransaction();
		// ft.hide(getFragmentManager().findFragmentByTag("ProductListFragment"));
		ft.add(R.id.content_frame, fragment);
		ft.addToBackStack("ProductDetailFragment");
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		ft.commit();
	}

}