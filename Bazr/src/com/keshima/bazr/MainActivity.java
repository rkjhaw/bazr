package com.keshima.bazr;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

@SuppressLint("NewApi")
public class MainActivity extends Activity implements OnClickListener {

	Button lang_english, lang_hindi, lang_kannada;
	private String selectedLanguage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_language_select);
		// getActionBar().setIcon(R.drawable.ic_launcher);
		lang_english = (Button) findViewById(R.id.lang_english);
		lang_hindi = (Button) findViewById(R.id.lang_hindi);
		lang_kannada = (Button) findViewById(R.id.lang_kannada);

		lang_english.setOnClickListener(this);
		lang_hindi.setOnClickListener(this);
		lang_kannada.setOnClickListener(this);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.lang_english:
			selectedLanguage = "english";
			break;
		case R.id.lang_hindi:
			selectedLanguage = "hindi";
			break;

		case R.id.lang_kannada:
			selectedLanguage = "kannada";
			break;

		default:
			break;
		}

		
		
	}
}
