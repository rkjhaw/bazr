package adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class LanguageAdapter extends BaseAdapter {

	String languages[] = { "ENGLISH", "HINDI", "KANNADA" };

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return languages.length;
	}

	@Override
	public String getItem(int position) {
		// TODO Auto-generated method stub
		return languages[position];
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if(convertView==null)
		{
			
		}
		else
		{
			
		}
		return null;
	}

}
