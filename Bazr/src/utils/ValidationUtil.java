package utils;

import android.util.Log;
import android.util.Patterns;
import android.webkit.URLUtil;
 
public class ValidationUtil
{

	public static final String MODULE = "ValidationUtil";
	public static String TAG = "";

	public static boolean isValidEmailId(String strEmail)
	{
		// TODO Auto-generated method stub
		TAG = "isValidEmailId";
		Log.d(MODULE, TAG);
		
		boolean isValid = true;
		
		try 
		{
			if (strEmail == null) 
			{
		        isValid = false;
		    } 
			else 
			{
		        isValid = Patterns.EMAIL_ADDRESS.matcher(strEmail).matches();
		    }
			
		}
		catch (Exception e) 
		{
			Log.e(MODULE, TAG + ", Exception Occurs " + e);
		}
		
		return isValid;
	}
	
	public static boolean isValidWebsite(String strWebsite)
	{
		// TODO Auto-generated method stub
		TAG = "isValidWebsite";
		Log.d(MODULE, TAG);
		
		boolean isValid = true;
		
		try 
		{
			if (strWebsite == null) 
			{
		        isValid = false;
		    } 
			else 
			{
		        isValid = URLUtil.isValidUrl(strWebsite);
		    }
			
		} 
		catch (Exception e) 
		{
			Log.e(MODULE, TAG + ", Exception Occurs " + e);
		}
		
		return isValid;
	}
	
	public static boolean isValidPhoneNO(String strPhone)
	{
		// TODO Auto-generated method stub
		TAG = "isValidPhoneNO";
		Log.d(MODULE, TAG);
		
		boolean isValid = true;
		
		try 
		{
			if (strPhone == null) 
			{
		        isValid = false;
		    } 
			else 
			{
		        isValid = Patterns.PHONE.matcher(strPhone).matches();		        
		    }
			
		} 
		catch (Exception e) 
		{
			Log.e(MODULE, TAG + ", Exception Occurs " + e);
		}
		
		return isValid;
	}	
}
