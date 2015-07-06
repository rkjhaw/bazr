package fragments;

import java.util.ArrayList;

import listener.Address_Received_Listener;
import listener.Product_Uploaded_Listener;
import listener.SubCategory_Listener;
import model.SubCategory;
import utils.Utils;
import webservices.GetAddress_WebService;
import webservices.ImageUploadTask;
import webservices.LoadSubCategories;
import adapters.SpinnerAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.keshima.bazr.FragmentChangeActivity;
import com.keshima.bazr.R;


public class ProductUploadFragment extends Fragment implements 
	SubCategory_Listener, OnClickListener,	LocationListener, OnItemSelectedListener,Product_Uploaded_Listener,Address_Received_Listener
{
	
	public static String MODULE = "ProductUploadFragment";
	public static String TAG = "";
	

	
	private static final int RESULT_LOAD_IMAGE = 2;
	ImageView image;
	TextView selected_image_txt, camera_label, browse_label;
	Button browse, upload, camera;
	private String selectedImagePath;
	EditText edtxt_product_name, edtxt_product_price, edtxt_product_location,
			edtxt_product_pname, edtxt_product_email, edtxt_product_number,
			edtxt_product_description;

	String str_product_name, str_product_price, str_product_location,
			str_product_pname, str_product_email, str_product_number,
			str_product_description, str_latitude, str_longitude,
			str_category_id,str_sub_category_id;
	private Uri selectedImage;
	private Bitmap photo;
	boolean isLoginned;
	public static FragmentManager fm;	

	private String picturePath;
	

	public static final int MEDIA_TYPE_IMAGE = 1;
	
	public long totalSize;
	private Location myLoaction;
	protected LocationManager locationManager;
	protected LocationListener locationListener;
	private String address, sellerId;
	private boolean locationEnabled;
	private Spinner category_spinner,subcategory_spinner;
	private String[] category_array,subcategory_arry;
	
	private ArrayList<SubCategory> Lst_SubCategories;	
	protected String server_image_path;
	Fragment mFragment;
	FragmentActivity mActivity;
	@Override
	public void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		TAG="onCreateView";
		Log.d(MODULE,TAG);
		
		mFragment=this;
		mActivity=mFragment.getActivity();
		
		
		
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
	{
		TAG="onCreateView";
		Log.d(MODULE,TAG);
		
		View rootView = inflater.inflate(R.layout.upload_item, container, false);
		sellerId = FragmentChangeActivity.prefs.getString("sellerId", "null");
		isLoginned = FragmentChangeActivity.prefs.getBoolean("loginned", false);

		Log.e("loginned", String.valueOf(isLoginned));
		camera = (Button) rootView.findViewById(R.id.camera);
		browse = (Button) rootView.findViewById(R.id.browse);
		upload = (Button) rootView.findViewById(R.id.upload);

		browse.setOnClickListener(this);
		upload.setOnClickListener(this);
		camera.setOnClickListener(this);

		selected_image_txt = (TextView) rootView.findViewById(R.id.selected_image);
		camera_label = (TextView) rootView.findViewById(R.id.camera_label);
		// browse_label = (TextView) rootView.findViewById(R.id.browse_label);
		edtxt_product_name = (EditText) rootView.findViewById(R.id.edtxt_product_name);
		edtxt_product_price = (EditText) rootView.findViewById(R.id.edtxt_product_price);
		edtxt_product_location = (EditText) rootView.findViewById(R.id.edtxt_product_location);
		edtxt_product_pname = (EditText) rootView.findViewById(R.id.edtxt_product_pname);
		edtxt_product_email = (EditText) rootView.findViewById(R.id.edtxt_product_email);
		edtxt_product_number = (EditText) rootView.findViewById(R.id.edtxt_product_number);
		edtxt_product_description = (EditText) rootView.findViewById(R.id.edtxt_product_description);

		image = (ImageView) rootView.findViewById(R.id.uploaded_image);
		image.setOnClickListener(this);

		locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,	0, this);
		category_spinner = (Spinner) rootView.findViewById(R.id.category_spinner);
		subcategory_spinner = (Spinner) rootView.findViewById(R.id.sub_category_spinner);
		
		fm = getFragmentManager();

		subcategory_arry = new String[1];
		subcategory_arry[0] = "Select Sub Category";
		
		category_array = new String[CategoryGridFragment.Lst_Categories.size()+1];
		category_array[0]="Select Category";
		
		for (int i = 0; i < CategoryGridFragment.Lst_Categories.size(); i++) 
		{
			category_array[i+1] = CategoryGridFragment.Lst_Categories.get(i).getName();
		}
		
		return rootView;
	}
	
	@Override
	public void onStart() 
	{
		super.onStart();
		
		TAG="onStart";
		Log.d(MODULE,TAG);
		
		SetCategoryAdapter();
		SetSubCategoryAdapter();
	}

	@Override
	public void onClick(View v) 
	{
		TAG="onClick";
		Log.d(MODULE,TAG);
		
		switch (v.getId())
		{
			case R.id.browse:
				 browseImage();
				 break;	
			case R.id.upload:
				 locationEnabled = Utils.isLocationEnabled(getActivity());
				 if (!locationEnabled) Utils.showSettingsAlert(getActivity());
				 else 
				 {
					if (myLoaction != null)	uploadProduct();					
					else Utils.showAlert(getActivity(),getString(R.string.msg_wait_gps));
				 }	
				 break;	
			case R.id.camera:
				 openCamera();
				 break;	
			default:
				break;
		}
	}

	public void SetCategoryAdapter()
	{
		TAG="SetCategoryAdapter";
		Log.d(MODULE,TAG);
		
		SpinnerAdapter adapter = new SpinnerAdapter(mActivity,R.layout.view_spinner_text,category_array);
		category_spinner.setAdapter(adapter);
		category_spinner.setOnItemSelectedListener(this);
	}
	
	public void SetSubCategoryAdapter()
	{
		TAG="SetSubCategoryAdapter";
		Log.d(MODULE,TAG);
		
		SpinnerAdapter adapter = new SpinnerAdapter(mActivity,R.layout.view_spinner_text,subcategory_arry);
		subcategory_spinner.setAdapter(adapter);
		subcategory_spinner.setOnItemSelectedListener(this);
	}
	
	private void openCamera()
	{
		TAG="openCamera";
		Log.d(MODULE,TAG);
			
		
		if (getActivity().getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA))
		{
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, Environment.getExternalStorageDirectory().getPath());
			getActivity().startActivityForResult(intent, 100);
		} 
		else 
		{
			Toast.makeText(getActivity(), "Camera not supported",Toast.LENGTH_LONG).show();
		}
	}
	
	
	private void uploadProduct() 
	{
		TAG="uploadProduct";
		Log.d(MODULE,TAG);
		
		if(IsValid())
		{
			new ImageUploadTask(this, selectedImagePath,
					server_image_path, sellerId, str_product_name,
					str_category_id,str_sub_category_id, str_product_price, str_product_pname,
					str_product_email, str_product_number, str_latitude,
					str_longitude, address, address, str_product_description)
					.execute();
		}
		
	}

	public boolean IsValid()
	{
		TAG="IsValid";
		Log.d(MODULE,TAG);
		
		str_product_name = edtxt_product_name.getText().toString();
		str_product_price = edtxt_product_price.getText().toString();
		str_product_location = edtxt_product_location.getText().toString();
		str_product_pname = edtxt_product_pname.getText().toString();
		str_product_email = edtxt_product_email.getText().toString();
		str_product_number = edtxt_product_number.getText().toString();
		str_product_description = edtxt_product_description.getText().toString();
		str_latitude = String.valueOf(myLoaction.getLatitude());
		str_longitude = String.valueOf(myLoaction.getLongitude());
		
		boolean Isvalid = true;
		
		if(str_category_id.equals("")) 
		{
			Isvalid = false;
			Toast.makeText(mActivity, "Please Select Category", Toast.LENGTH_LONG).show();
		}
		else if(str_sub_category_id.equals("")) 
		{
			Isvalid = false;
			Toast.makeText(mActivity, "Please Select Sub Category", Toast.LENGTH_LONG).show();
		}
		else if(str_product_pname.equals("")) 
		{
			Isvalid = false;
			Toast.makeText(mActivity, "Please select product name", Toast.LENGTH_LONG).show();
		}
		
		return Isvalid;
	}
	public void SetSubCategories()
	{
		subcategory_arry = new String[Lst_SubCategories.size()+1];
		subcategory_arry[0]="Select Sub Category";
		
		for (int i = 0; i < Lst_SubCategories.size(); i++) 
		{
			subcategory_arry[i+1] = Lst_SubCategories.get(i).getName();
		}
		
		SetSubCategoryAdapter();
	}
	private void browseImage()
	{
		TAG="browseImage";
		Log.d(MODULE,TAG);
		
		Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		getActivity().startActivityForResult(i, RESULT_LOAD_IMAGE);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		TAG="onActivityResult";
		Log.d(MODULE,TAG);
		
		if (data != null) 
		{
			if (requestCode == RESULT_LOAD_IMAGE) 
			{
				uploadImage(data);
			} 
			else if (requestCode == 100 && resultCode == -1)
			{
				uploadImageFromCamera(data);
			}
		}
	}

	private void uploadImageFromCamera(Intent data) 
	{
		TAG="uploadImageFromCamera";
		Log.d(MODULE,TAG);
		
		selectedImage = data.getData();
		
		if(selectedImage!=null)
		{
			TAG="uploadImageFromCamera-selectedImage Not null";
			Log.d(MODULE,TAG);
						
			// Cursor to get image uri to display
			String[] filePathColumn = { MediaStore.Images.Media.DATA };
			Cursor cursor = getActivity().getContentResolver().query(selectedImage,filePathColumn, null, null, null);
			cursor.moveToFirst();

			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			picturePath = cursor.getString(columnIndex);
			selectedImagePath = picturePath;
			cursor.close();
			Log.v("picture ppath", picturePath);
			Bitmap photo = (Bitmap) data.getExtras().get("data");
			camera_label.setText("Change the Photo");
			image.setImageBitmap(photo);
		}
		
	}

	private void uploadImage(Intent data)
	{
		TAG="uploadImage";
		Log.d(MODULE,TAG);
		
		Uri selectedImageUri = data.getData();
		selectedImagePath = Utils.getPathFromUri(selectedImageUri,getActivity());
		System.out.println("Image Path : " + selectedImagePath);
		selected_image_txt.setText(selectedImagePath);
		image.setImageURI(selectedImageUri);
	}

	@Override
	public void onLocationChanged(final Location location) 
	{
		//TAG="onLocationChanged";
		//Log.d(MODULE,TAG);	
		this.myLoaction =location;
		new GetAddress_WebService(this, location).execute();
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras){}

	@Override
	public void onProviderEnabled(String provider) {}

	@Override
	public void onProviderDisabled(String provider) {}

	

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,long id) 
	{
		TAG="onItemSelected";
		Log.d(MODULE,TAG);	
		
		if(position!=0)
		{
			switch (parent.getId())
			{
				case R.id.category_spinner:	
					 str_category_id = CategoryGridFragment.Lst_Categories.get(position-1).getId();
					 new LoadSubCategories(mFragment,str_category_id).execute();				
					 break;	
				case R.id.sub_category_spinner:	
					 str_sub_category_id = Lst_SubCategories.get(position-1).getSubcategoryid();				 
					 break;					 
				default:
					 break;
			}
		}
		else
		{
			switch (parent.getId())
			{
				case R.id.category_spinner:	
					 str_category_id = "";								
					 break;	
				case R.id.sub_category_spinner:	
					 str_sub_category_id = "";
					 break;					 
				default:
					 break;
			}
		}		
	}	

	@Override
	public void onNothingSelected(AdapterView<?> parent)
	{
		
	}

	@Override
	public void onSubCategoriesLoaded(ArrayList<SubCategory> Lst_SubCategories)
	{
		this.Lst_SubCategories = new ArrayList<SubCategory>();
		this.Lst_SubCategories = Lst_SubCategories;		
		SetSubCategories();
	}
	@Override
	public void onProductUploadResult(boolean Succes, String Str_Code,String Str_Message)
	{
		TAG="onProductUploadResult";
		Log.d(MODULE,TAG);	
				
		Toast.makeText(mActivity, Str_Message,Toast.LENGTH_LONG).show();
		if(Str_Code.equals("3")) moveToMyActivity();			
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
	public void onAddressReceivedResult(final String Str_Message) 
	{
	
		mActivity.runOnUiThread(new Runnable()
		{			
			@Override
			public void run() 
			{
				if (getActivity() != null) 
				{
					address=Str_Message;
					edtxt_product_location.setText("");
					edtxt_product_location.setText(address);
				}				
			}
		});
	}
	

}
	
