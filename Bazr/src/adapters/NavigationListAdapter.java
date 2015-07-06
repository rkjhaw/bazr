package adapters;

import java.util.ArrayList;

import com.keshima.bazr.FragmentChangeActivity;
import com.keshima.bazr.R;

import model.Category;
import model.Product;
import utils.ImageLoader;
import android.R.menu;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class NavigationListAdapter extends BaseAdapter {
	Context ctx;
	LayoutInflater inflater;
	String[] menus;

	public NavigationListAdapter(Context ctx, String[] menus) {
		// TODO Auto-generated constructor stub
		this.ctx = ctx;
		inflater = (LayoutInflater) ctx
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.menus = menus;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return this.menus.length;

	}

	@Override
	public String getItem(int position) {
		// TODO Auto-generated method stub
		return this.menus[position];
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	class Holder {
		TextView name;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Holder holder;
		if (convertView == null) {
			holder = new Holder();
			convertView = inflater.inflate(R.layout.navigation_list_item,
					parent, false);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();

		}
		holder.name.setText(menus[position]);

		return convertView;
	}
}
