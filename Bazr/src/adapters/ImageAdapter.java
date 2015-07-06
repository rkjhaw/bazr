package adapters;

import java.util.ArrayList;

import utils.Constants;

import model.ProductImage;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.keshima.bazr.AppController;
import com.keshima.bazr.R;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class ImageAdapter extends PagerAdapter 
{
	 
    Context mContext;
    LayoutInflater mLayoutInflater;
    ArrayList<ProductImage> Lst_Images;
    ImageLoader imageLoader;
 
    public ImageAdapter(Context context,ArrayList<ProductImage> Lst_Images) 
    {
        mContext = context;
        this.Lst_Images = Lst_Images;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader = AppController.getInstance().getImageLoader();	
    }
 
    @Override
    public int getCount()
    {
        return Lst_Images.size();
    }
 
    @Override
    public boolean isViewFromObject(View view, Object object)
    {
        return view == ((LinearLayout) object);
    }
 
    @Override
    public Object instantiateItem(ViewGroup container, int position) 
    {
        View itemView = mLayoutInflater.inflate(R.layout.view_image_product_detail, container, false);
 
        NetworkImageView imageView = (NetworkImageView) itemView.findViewById(R.id.image_view_product_detail);
        String StrImageUrl = Constants.CATEGORY_IMAGE_URL + Lst_Images.get(position).getImagePath();
		makeImageRequest(imageView,StrImageUrl);		
        container.addView(itemView);
        
        return itemView;
    }
 
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) 
    {
        container.removeView((LinearLayout) object);
    }
    
    private void makeImageRequest(NetworkImageView imgView,String Str_Url) 
	{
		ImageLoader imageLoader = AppController.getInstance().getImageLoader();	
		if(imageLoader!=null) imgView.setImageUrl(Str_Url, imageLoader);			
	}
}