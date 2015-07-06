package adapters;

import com.keshima.bazr.R;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


public class SpinnerAdapter extends ArrayAdapter<String> 
{
	FragmentActivity mActivity;
	String[] objects;
	
	public SpinnerAdapter(FragmentActivity mActivity, int txtViewResourceId, String[] objects) 
	{
		super(mActivity, txtViewResourceId, objects);
		this.mActivity = mActivity;
		this.objects = objects;
	}

	@Override
	public View getDropDownView(int position, View cnvtView, ViewGroup prnt)
	{
		return getCustomView(position, cnvtView, prnt);
	}
	@Override
	public View getView(int pos, View cnvtView, ViewGroup prnt)
	{
		return getCustomView(pos, cnvtView, prnt);
	}
	public View getCustomView(int position, View convertView,ViewGroup parent) 
	{
		LayoutInflater inflater = mActivity.getLayoutInflater();
		View mySpinner = inflater.inflate(R.layout.view_spinner_text, parent,false);
		TextView main_text = (TextView) mySpinner.findViewById(R.id.spinnerText);
		main_text.setText(objects[position]);

		return mySpinner;
	}
}



