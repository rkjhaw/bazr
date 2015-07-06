package fragments;

import com.keshima.bazr.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class SettingsFragment extends Fragment implements OnClickListener {

	Button btn_personal_info, btn_language, btn_more;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.settings_layout, container,
				false);
		btn_personal_info = (Button) rootView
				.findViewById(R.id.btn_personal_info);
		btn_language = (Button) rootView.findViewById(R.id.btn_language);
		btn_more = (Button) rootView.findViewById(R.id.btn_more);

		btn_personal_info.setOnClickListener(this);
		btn_language.setOnClickListener(this);
		btn_more.setOnClickListener(this);

		return rootView;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Fragment fragment;

		switch (v.getId()) {
		case R.id.btn_personal_info:

			break;
		case R.id.btn_language:

			break;

		case R.id.btn_more:

			break;

		default:
			break;
		}
	}

}
