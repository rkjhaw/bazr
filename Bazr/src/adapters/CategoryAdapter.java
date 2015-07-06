package adapters;


import java.util.ArrayList;

import utils.Constants;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.keshima.bazr.AppController;
import com.keshima.bazr.R;

import model.Category;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CategoryAdapter extends BaseAdapter
{
	Context ctx;
	ArrayList<Category> categories;
	LayoutInflater inflater;	
	ImageLoader imageLoader;
	
	public CategoryAdapter(Context ctx, ArrayList<Category> categories)
	{
		// TODO Auto-generated constructor stub
		this.ctx = ctx;
		this.categories = categories;
		inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		imageLoader = AppController.getInstance().getImageLoader();	
	}

	@Override
	public int getCount() 
	{
		return this.categories.size();
	}

	@Override
	public Category getItem(int position) 
	{
		// TODO Auto-generated method stub
		return this.categories.get(position);
	}

	@Override
	public long getItemId(int position) 
	{
		// TODO Auto-generated method stub
		return position;
	}

	class Holder 
	{
		NetworkImageView image;
		TextView name;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		// TODO Auto-generated method stub
		Holder holder;
		if (convertView == null)
		{
			holder = new Holder();
			convertView = inflater.inflate(R.layout.category_item, parent,false);
			holder.image = (NetworkImageView) convertView.findViewById(R.id.img);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			convertView.setTag(holder);
		} 
		else 
		{
			holder = (Holder) convertView.getTag();
		}
		holder.name.setText(categories.get(position).getName());
		Log.d("imgurl", "http://52.74.244.5:8080/"+ categories.get(position).getImagePath());
		String StrImageUrl = Constants.CATEGORY_IMAGE_URL + categories.get(position).getImagePath();
		makeImageRequest(holder.image,StrImageUrl);
		return convertView;
	}	
	
	private void makeImageRequest(NetworkImageView imgView,String Str_Url) 
	{
		ImageLoader imageLoader = AppController.getInstance().getImageLoader();	
		if(imageLoader!=null) imgView.setImageUrl(Str_Url, imageLoader);			
	}

}
