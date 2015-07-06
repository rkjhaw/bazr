package utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import com.actionbarsherlock.view.Menu;
import com.google.android.gms.maps.model.LatLng;

public class Utils {

	private Context _context;
	public static Menu menu;

	public Utils(Context context) {
		this._context = context;
	}

	public static boolean isLocationEnabled(Context context) {
		int locationMode = 0;
		String locationProviders;

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			try {
				locationMode = Settings.Secure.getInt(
						context.getContentResolver(),
						Settings.Secure.LOCATION_MODE);

			} catch (SettingNotFoundException e) {
				e.printStackTrace();
			}

			return locationMode != Settings.Secure.LOCATION_MODE_OFF;

		} else {
			locationProviders = Settings.Secure.getString(
					context.getContentResolver(),
					Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
			return !TextUtils.isEmpty(locationProviders);
		}

	}

	public static void showSettingsAlert(final Context ctx) {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(ctx);

		// Setting Dialog Title
		alertDialog.setTitle("Error!");

		// Setting Dialog Message
		alertDialog.setMessage("You need to enable GPS to get your location");

		// On pressing Settings button
		alertDialog.setPositiveButton("Enable",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent(
								Settings.ACTION_LOCATION_SOURCE_SETTINGS);
						ctx.startActivity(intent);
					}
				});

		alertDialog.show();
	}
	
	public static boolean isOnline(Context ctx) 
	{
		boolean haveConnectedWifi = false;
		boolean haveConnectedMobile = false;
		ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(android.content.Context.CONNECTIVITY_SERVICE);
		NetworkInfo[] netInfo = cm.getAllNetworkInfo();
		for (NetworkInfo ni : netInfo) 
		{
			if (ni.getTypeName().equalsIgnoreCase("WIFI"))
				if (ni.isConnected())
					haveConnectedWifi = true;
			if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
				if (ni.isConnected())
					haveConnectedMobile = true;
		}
		return haveConnectedWifi || haveConnectedMobile;
	}	


	public static boolean isConnectingToInternet(Context ctx) {
		ConnectivityManager connectivity = (ConnectivityManager) ctx
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null)
				for (int i = 0; i < info.length; i++)
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}

		}
		return false;
	}

	public static void CopyStream(InputStream is, OutputStream os) {
		final int buffer_size = 1024;
		try {

			byte[] bytes = new byte[buffer_size];
			for (;;) {
				// Read byte from input stream

				int count = is.read(bytes, 0, buffer_size);
				if (count == -1)
					break;

				// Write byte from output stream
				os.write(bytes, 0, count);
			}
		} catch (Exception ex) {
		}
	}

	public static String getPathFromUri(Uri uri, Activity activity) {
		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = activity
				.managedQuery(uri, projection, null, null, null);
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}

	public static void showAlert(final Context ctx, String msg) {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(ctx);

		// Setting Dialog Title
		alertDialog.setTitle("Error!");

		// Setting Dialog Message
		alertDialog.setMessage(msg);

		// On pressing Settings button
		alertDialog.setPositiveButton("Ok",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});

		alertDialog.show();
	}

	public String multipartRequest(String urlTo, String post, String filepath,
			String filefield) throws ParseException, IOException {
		HttpURLConnection connection = null;
		DataOutputStream outputStream = null;
		InputStream inputStream = null;

		String twoHyphens = "--";
		String boundary = "*****" + Long.toString(System.currentTimeMillis())
				+ "*****";
		String lineEnd = "\r\n";

		String result = "";

		int bytesRead, bytesAvailable, bufferSize;
		byte[] buffer;
		int maxBufferSize = 1 * 1024 * 1024;

		String[] q = filepath.split("/");
		int idx = q.length - 1;

		try {
			File file = new File(filepath);
			FileInputStream fileInputStream = new FileInputStream(file);

			URL url = new URL(urlTo);
			connection = (HttpURLConnection) url.openConnection();

			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setUseCaches(false);

			connection.setRequestMethod("POST");
			connection.setRequestProperty("Connection", "Keep-Alive");
			connection.setRequestProperty("User-Agent",
					"Android Multipart HTTP Client 1.0");
			connection.setRequestProperty("Content-Type",
					"multipart/form-data; boundary=" + boundary);

			outputStream = new DataOutputStream(connection.getOutputStream());
			outputStream.writeBytes(twoHyphens + boundary + lineEnd);
			outputStream.writeBytes("Content-Disposition: form-data; name=\""
					+ filefield + "\"; filename=\"" + q[idx] + "\"" + lineEnd);
			outputStream.writeBytes("Content-Type: image/jpeg" + lineEnd);
			outputStream.writeBytes("Content-Transfer-Encoding: binary"
					+ lineEnd);
			outputStream.writeBytes(lineEnd);

			bytesAvailable = fileInputStream.available();
			bufferSize = Math.min(bytesAvailable, maxBufferSize);
			buffer = new byte[bufferSize];

			bytesRead = fileInputStream.read(buffer, 0, bufferSize);
			while (bytesRead > 0) {
				outputStream.write(buffer, 0, bufferSize);
				bytesAvailable = fileInputStream.available();
				bufferSize = Math.min(bytesAvailable, maxBufferSize);
				bytesRead = fileInputStream.read(buffer, 0, bufferSize);
			}

			outputStream.writeBytes(lineEnd);

			// Upload POST Data
			String[] posts = post.split("&");
			int max = posts.length;
			for (int i = 0; i < max; i++) {
				outputStream.writeBytes(twoHyphens + boundary + lineEnd);
				String[] kv = posts[i].split("=");
				outputStream
						.writeBytes("Content-Disposition: form-data; name=\""
								+ kv[0] + "\"" + lineEnd);
				outputStream.writeBytes("Content-Type: text/plain" + lineEnd);
				outputStream.writeBytes(lineEnd);
				outputStream.writeBytes(kv[1]);
				outputStream.writeBytes(lineEnd);
			}

			outputStream.writeBytes(twoHyphens + boundary + twoHyphens
					+ lineEnd);

			inputStream = connection.getInputStream();
			result = this.convertStreamToString(inputStream);

			fileInputStream.close();
			inputStream.close();
			outputStream.flush();
			outputStream.close();

			return result;
		} catch (Exception e) {
			Log.e("MultipartRequest", "Multipart Form Upload Error");
			e.printStackTrace();
			return "error";
		}
	}

	private String convertStreamToString(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	public static double CalculationByDistance(LatLng StartP, LatLng EndP) {
		int Radius = 6371;// radius of earth in Km
		double lat1 = StartP.latitude;
		double lat2 = EndP.latitude;
		double lon1 = StartP.longitude;
		double lon2 = EndP.longitude;
		double dLat = Math.toRadians(lat2 - lat1);
		double dLon = Math.toRadians(lon2 - lon1);
		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
				+ Math.cos(Math.toRadians(lat1))
				* Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
				* Math.sin(dLon / 2);
		double c = 2 * Math.asin(Math.sqrt(a));
		double valueResult = Radius * c;
		double km = valueResult / 1;
		DecimalFormat newFormat = new DecimalFormat("####");
		double kmInDec = Double.valueOf(newFormat.format(km));
		double meter = valueResult / 1000;
		double feet = valueResult / 1000 * (3.2);

		double meterInDec = Integer.valueOf(newFormat.format(meter));
		Log.i("Radius Value", "" + valueResult + "   KM  " + kmInDec
				+ " Meter   " + meterInDec);

		return kmInDec;
	}

	public static int getScreenWidth(Context context) {
		int columnWidth;
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();

		final Point point = new Point();
		try {
			display.getSize(point);
		} catch (java.lang.NoSuchMethodError ignore) { // Older device
			point.x = display.getWidth();
			point.y = display.getHeight();
		}
		columnWidth = point.x;
		return columnWidth;
	}
	
	public static int getScreenOrientation(FragmentActivity mActivity)
	{
	    Display getOrient = mActivity.getWindowManager().getDefaultDisplay();
	    Point size = new Point();

	    getOrient.getSize(size);

	    int orientation;
	    if (size.x < size.y)
	    {
	        orientation = Configuration.ORIENTATION_PORTRAIT;
	    }
	    else
	    {
	        orientation = Configuration.ORIENTATION_LANDSCAPE;
	    }
	    return orientation;
	}
	
	public static String getCompleteAddressString(FragmentActivity mActivity,double LATITUDE, double LONGITUDE)
	{
		//TAG="getCompleteAddressString";
		//Log.d(MODULE,TAG);
		
		String strAdd = "";

		try 
		{
			Geocoder geocoder = new Geocoder(mActivity, Locale.getDefault());
			List<Address> addresses = geocoder.getFromLocation(LATITUDE,LONGITUDE, 1);
			if (addresses != null) 
			{
				Address returnedAddress = addresses.get(0);
				StringBuilder strReturnedAddress = new StringBuilder("");
				for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++)
				{
					strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
				}
				strAdd = strReturnedAddress.toString();
				//Log.e(MODULE,TAG + " Address >>>> " + strAdd);
			} 
			else 
			{
				//Log.e(MODULE,TAG  + " Address is null " );
				strAdd="NA";
			}
		}
		catch (Exception e) 
		{
			//Log.e(MODULE,TAG);
			e.printStackTrace();	
			strAdd="NA";
		}
		return strAdd;
	}
	
	public static String GetDifference(Date endDate)
	{
		String Str_Difference="";
		//HH converts hour in 24 hours format (0-23), day calculation
		SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		
		Calendar c = Calendar.getInstance();
		System.out.println("Current time => " + c.getTime());
		String Str_StartDate = format.format(c.getTime());
		 
		Date d1 = null;
		Date d2 = endDate;
		String Str_EndDate = format.format(endDate);
		 
		try 
		{
				
			d1 = format.parse(Str_StartDate);
			d2 = format.parse(Str_EndDate);
			//in milliseconds
			long diff = Math.abs(d2.getTime() - d1.getTime());
		 
			long diffSeconds = diff / 1000 % 60;
			long diffMinutes = diff / (60 * 1000) % 60;
			long diffHours = diff / (60 * 60 * 1000) % 24;
			long diffDays = diff / (24 * 60 * 60 * 1000);
		 
			System.out.print(diffDays + " days, ");
			System.out.print(diffHours + " hours, ");
			System.out.print(diffMinutes + " minutes, ");
			System.out.print(diffSeconds + " seconds.");
			
			Str_Difference = Long.toString(diffDays);
		 
			if(diffDays < 0)
			{
				if(diffHours < 1)
				{
					if(diffMinutes < 1)
					{
						Str_Difference = Long.toString(diffDays) + " seconds ago";
					}
					else if((diffMinutes > 1) && (diffMinutes < 60))
					{
						Str_Difference = Long.toString(diffDays) + " minutes ago";
					}
					else if((diffHours > 1) && (diffHours < 24))
					{
						Str_Difference = Long.toString(diffDays) + " hours ago";
					}					
				}				
			}
			else if((diffDays > 1))
			{
				Str_Difference = Long.toString(diffDays) + " days ago";
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return Str_Difference;
	}

}