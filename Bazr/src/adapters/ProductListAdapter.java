package adapters;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.google.android.gms.maps.model.LatLng;
import com.keshima.bazr.AppController;
import com.keshima.bazr.R;

import fragments.ProductListFragment;
import model.Product;
import utils.Constants;
import utils.Utils;
import android.content.Context;
import android.location.Location;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ProductListAdapter extends BaseAdapter
{
	Context ctx;
	ArrayList<Product> products;
	LayoutInflater inflater;
	ImageLoader imageLoader;
	Location myLocation;

	public ProductListAdapter(Context ctx, ArrayList<Product> products,	Location mylocation)
	{
		this.ctx = ctx;
		this.products = products;
		inflater = (LayoutInflater) ctx	.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		imageLoader = AppController.getInstance().getImageLoader();	
		this.myLocation = mylocation;
	}

	@Override
	public int getCount() 
	{
		return this.products.size();
	}

	@Override
	public Product getItem(int position) 
	{		
		return this.products.get(position);
	}

	@Override
	public long getItemId(int position) 
	{
		return position;
	}

	class Holder
	{
		NetworkImageView image;
		TextView name,price, description, distance, text_date;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		
		Holder holder;
		if (convertView == null) 
		{
			holder = new Holder();
			
			convertView = inflater.inflate(R.layout.product_item, parent, false);
			holder.name = (TextView) convertView.findViewById(R.id.textview_product_list_name);
			holder.image = (NetworkImageView) convertView.findViewById(R.id.image_view_product_list_item);
			holder.price = (TextView) convertView.findViewById(R.id.textview_product_list_price);
			holder.description = (TextView) convertView.findViewById(R.id.textview_product_list_description);
			holder.distance = (TextView) convertView.findViewById(R.id.textview_product_list_distance);
			holder.text_date = (TextView) convertView.findViewById(R.id.textview_product_list_date);
			
			convertView.setTag(holder);
		} 
		else 
		{
			holder = (Holder) convertView.getTag();
		}
		holder.name.setText(products.get(position).getProductName());
		holder.price.setText("Rs." + products.get(position).getPrice());
		holder.description.setText(products.get(position).getDescription());
		if(holder.text_date!=null)
		{
			SimpleDateFormat parserSDF=new SimpleDateFormat("MMM dd,yyyy HH:mm:ss");
			Date EndDate=null;
			try 
			{
				EndDate = parserSDF.parse(products.get(position).getDate());
			} 
			catch (ParseException e)
			{
				e.printStackTrace();
			}
			holder.text_date.setText(Utils.GetDifference(EndDate));
		}
		
		if (myLocation != null) 
		{
			Location pLoaction = new Location("pLocation");
			pLoaction.setLatitude(Double.parseDouble(products.get(position).getLatitude()));
			pLoaction.setLongitude(Double.parseDouble(products.get(position).getLongitude()));
			LatLng myLatLng = new LatLng(myLocation.getLatitude(),myLocation.getLongitude());
			LatLng pLatLng = new LatLng(pLoaction.getLatitude(),pLoaction.getLongitude());
			String distance = String.valueOf(Utils.CalculationByDistance(myLatLng, pLatLng)) + "km";
			ProductListFragment.products.get(position).setDisatnce(distance);
			holder.distance.setText(distance);
		} 
		else
		{
			ProductListFragment.products.get(position).setDisatnce("5km");
			holder.distance.setText("5km");
		}
		String StrImageUrl = Constants.CATEGORY_IMAGE_URL + products.get(position).getProductImage().get(0).getImagePath();
		makeImageRequest(holder.image,StrImageUrl);		
		return convertView;
	}
	
	private void makeImageRequest(NetworkImageView imgView,String Str_Url) 
	{
		ImageLoader imageLoader = AppController.getInstance().getImageLoader();	
		if(imageLoader!=null) imgView.setImageUrl(Str_Url, imageLoader);			
	}
}
