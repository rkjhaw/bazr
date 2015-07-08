package fragments;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageContainer;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.android.volley.toolbox.NetworkImageView;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareButton;
import com.facebook.share.widget.ShareDialog;
import com.keshima.bazr.AppController;
import com.keshima.bazr.R;

import model.Category;
import model.Product;
import model.ProductImage;
import utils.Utils;
import webservices.TaskCompleted;
import adapters.ImageAdapter;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.Watson.OnCreateOptionsMenuListener;
import android.support.v4.app.Watson.OnOptionsItemSelectedListener;
import android.support.v4.view.ViewPager;
import android.text.SpannableString;
import android.text.method.ScrollingMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ProductDetailFragment extends Fragment implements TaskCompleted, OnCreateOptionsMenuListener,	OnOptionsItemSelectedListener 
{

	public static String MODULE = "ProductDetailFragment";
	public static String TAG = "";
	
	String id;
	TaskCompleted taskCompletedCallback;
	public static Product selectedProduct;
	ViewPager pager;
	TextView tv_product_name,tv_price,tv_postedon,tv_distance,tv_description,tv_email,tv_mobileno;
	Button moreImages0;
	ImageView imageview_call,imageview_email,imageview_facebook;
	private String comingFrom;
	FragmentActivity mActivity;
	ArrayList<ProductImage> Lst_Images;
	ImageLoader imageLoader;
	

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		
		TAG="onCreate";
		Log.d(MODULE,TAG);
		
		mActivity = getActivity();
		
		setHasOptionsMenu(true);
		imageLoader = AppController.getInstance().getImageLoader();	
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) 
	{
		
		TAG="onCreateView";
		Log.d(MODULE,TAG);
		
		View rootView = inflater.inflate(R.layout.product_details, container,false);
		// id = getIntent().getExtras().getString("product_id");
		taskCompletedCallback = new ProductDetailFragment();		
		comingFrom = getArguments().getString("isComingFrom");
		imageview_call = (ImageView) rootView.findViewById(R.id.imageview_call);
		imageview_email = (ImageView) rootView.findViewById(R.id.imageview_email);
		imageview_facebook =(ImageView) rootView.findViewById(R.id.imageview_facebook);
		
		if (comingFrom.equalsIgnoreCase("ProductListFragment")) 
		{
			selectedProduct = ProductListFragment.selectedProduct;
		} 
		else 
		{
			selectedProduct = MapFragment.selectedProduct;
		}

		pager = (ViewPager) rootView.findViewById(R.id.pager);
		
		tv_product_name = (TextView) rootView.findViewById(R.id.textview_product_detail_name);
		tv_price = (TextView) rootView.findViewById(R.id.textview_product_detail_price);
		tv_postedon =  (TextView) rootView.findViewById(R.id.textview_product_detail_postedon);
		tv_description = (TextView) rootView.findViewById(R.id.textview_product_detail_description);
		tv_distance = (TextView) rootView.findViewById(R.id.textview_product_detail_distance);
		tv_email = (TextView) rootView.findViewById(R.id.textview_product_detail_email);
		tv_mobileno = (TextView) rootView.findViewById(R.id.textview_product_detail_mobileno);
			
		return rootView;
	}

	
	OnClickListener _OnClickListener = new OnClickListener()
	{		
		@Override
		public void onClick(View v) 
		{
			TAG="onClick";
			Log.d(MODULE,TAG);
			
			switch (v.getId())
			{
				case R.id.imageview_call:	
					 Intent intent = new Intent(Intent.ACTION_DIAL);
					 intent.setData(Uri.parse("tel:" + selectedProduct.getContactNo()));
					 startActivity(intent);	
					 break;	
				case R.id.imageview_email:
					 sendMail();
					 break;	
				case R.id.imageview_facebook:
					 ShareOnFacebook();
					 break;	
				default:
					 break;
			}		
		}
	};
	
	@Override
	public void onStart()
	{		
		super.onStart();
		
		TAG="onStart";
		Log.d(MODULE,TAG);
		
		SetProperties();
		Lst_Images = new ArrayList<ProductImage>();
		Lst_Images = selectedProduct.getProductImage();
		ImageAdapter adapter = new ImageAdapter(mActivity,Lst_Images);
		pager.setAdapter(adapter);
	}
	public void SetProperties()
	{
		TAG="SetProperties";
		Log.d(MODULE,TAG);
		

		tv_product_name.setText(selectedProduct.getProductName());
		
		String Str_Price= "Price : Rs. " + selectedProduct.getPrice();
		SpannableString Span_Price=  new SpannableString(Str_Price);
		Span_Price.setSpan(new ForegroundColorSpan(Color.BLACK), 0, 7, 0);// set color
		tv_price.setText(Span_Price);
	
		String Str_Distance= "Distance : " + Double.toString(selectedProduct.getDistance()) + " KM";
		SpannableString Span_Distance=  new SpannableString(Str_Distance);
		Span_Distance.setSpan(new ForegroundColorSpan(Color.BLACK), 0, 10, 0);// set color		
		tv_distance.setText(Span_Distance);
		
		String Str_Email= "Email : " + selectedProduct.getEmail();
		SpannableString Span_Email=  new SpannableString(Str_Email);
		Span_Email.setSpan(new ForegroundColorSpan(Color.BLACK), 0, 7, 0);// set color		
		tv_email.setText(Span_Email);
		
		String Str_ContactNumber= "Contact Number : " + selectedProduct.getContactNo();
		SpannableString Span_ContactNo =  new SpannableString(Str_ContactNumber);
		Span_ContactNo.setSpan(new ForegroundColorSpan(Color.BLACK), 0, 16, 0);// set color		
		tv_mobileno.setText(Span_ContactNo);
		
		tv_description.setMovementMethod(new ScrollingMovementMethod());
		tv_description.setText(selectedProduct.getDescription());
		
		SimpleDateFormat parserSDF=new SimpleDateFormat("MMM dd,yyyy HH:mm:ss",Locale.US);
		Date EndDate=null;
		try 
		{
			EndDate = parserSDF.parse(selectedProduct.getDate());
		} 
		catch (ParseException e)
		{
			e.printStackTrace();
		}
		tv_postedon.setText(Utils.GetDifference(EndDate));
		
		imageview_call.setOnClickListener(_OnClickListener);
		imageview_email.setOnClickListener(_OnClickListener);
		imageview_facebook.setOnClickListener(_OnClickListener);
	}

	private void sendMail() 
	{
		TAG="sendMail";
		Log.d(MODULE,TAG);
		
		Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "suren.btech.it@gmail.com", null));
		emailIntent.putExtra(Intent.EXTRA_SUBJECT, "EXTRA_SUBJECT");
		startActivity(Intent.createChooser(emailIntent, "Send email..."));
	}

	@Override
	public void onTaskCompleted() {}
	@Override
	public void onTaskCompleted(ArrayList<Category> categories) {}

	@Override
	public void onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu,com.actionbarsherlock.view.MenuInflater inflater) 
	{
		menu.clear();
		inflater.inflate(R.menu.product_list_menu, menu);
		menu.findItem(R.id.refresh).setVisible(false);
	}

	@Override
	public boolean onOptionsItemSelected(com.actionbarsherlock.view.MenuItem item) 
	{
		switch (item.getItemId())
		{
			case R.id.map:
				 moveToMap();
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
		
		ArrayList<Product> Lst_Products = new ArrayList<Product>();
		Lst_Products.add(selectedProduct);
		
		ArrayList<Location> Lst_Locations = new ArrayList<Location>();
		Lst_Locations.add( AddLocation());
		
		bundle.putString("isComingFrom", "ProductListFragment");
		bundle.putParcelableArrayList("b_product_list",Lst_Products);
		bundle.putParcelableArrayList("b_locations_list",Lst_Locations);
		
		fragment.setArguments(bundle);
		
		FragmentManager fm = getFragmentManager();
		ft = fm.beginTransaction();		
		ft.add(R.id.content_frame, fragment);
		ft.addToBackStack("MapFragment");
		ft.hide(this);		
		ft.commit();
	}

	public Location AddLocation()
	{
		TAG="AddLocation";
		Log.d(MODULE,TAG);
		
		String adds = selectedProduct.getLocation().toString();
		
		Log.d(MODULE,TAG + " - adds : " + adds);
		
		String lat, lon;
		double lt=0.0, lg=0.0;
		
		lat = adds.substring(0, adds.indexOf("|||"));
		lon = adds.substring(lat.length() + 3,adds.indexOf("|||", lat.length() + 3));		
		
		Log.d("latit ", lat);
		Log.d("longit ", lon);		
		
		try
		{
			lt = Double.parseDouble(lat);
			lg = Double.parseDouble(lon);
		}

		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
		Location location;

		location = new Location("productLocs");
		location.setLatitude(lt);
		location.setLongitude(lg);
		
		return location;
	}
	
	public void ShareOnFacebook()
	{
		String Str_ImgUrl = Lst_Images.get(0).getImagePath();
		ImageLoader imageLoader = AppController.getInstance().getImageLoader();	
		if(imageLoader!=null) imageLoader.get(Str_ImgUrl, imageListener);	
		
	}
	
	ImageListener imageListener = new ImageListener()
	{		
		@Override
		public void onErrorResponse(VolleyError arg0) 
		{
						
		}
		
		@Override
		public void onResponse(ImageContainer response, boolean arg1) 
		{
			SharePhoto photo = new SharePhoto.Builder().setBitmap(response.getBitmap()).build();
			SharePhotoContent content = new SharePhotoContent.Builder()
	        .addPhoto(photo)
	        .build();
			ShareDialog.show(mActivity, content);
		}
	};
}
