package utils;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.util.List;
 
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import android.app.Activity;
import android.util.Log;

public class ConnectionUtil 
{
	public static final String MODULE = "ConnectionUtil";
	public static String TAG = "";
	
    static String response = null;
    public final static int GET = 1;
    public final static int POST = 2;
    Activity _activity;
    public ConnectionUtil() 
    {
   
    }
 
    /**
     * Making service call
     * @url - url to make request
     * @method - http request method
     * */
    public String makeServiceCall(String url, int method,Activity activity) 
    {
        return this.makeServiceCall(url, method, null,activity);
    }
 
    /**
     * Making service call
     * @url - url to make request
     * @method - http request method
     * @params - http request params
     * */
    public String makeServiceCall(String url, int method,  List<NameValuePair> params,Activity activity) 
    {    	
					
    		try 
    		{
    			TAG = "makeServiceCall";    			
                // http client
	            DefaultHttpClient httpClient = new DefaultHttpClient();
	            HttpEntity httpEntity = null;
	            HttpResponse httpResponse = null;
	            // Checking http request method type
	            if (method == POST) 
	            {
	                HttpPost httpPost = new HttpPost(url);
	                // adding post params
	                if (params != null) 
	                {
	                     httpPost.setEntity(new UrlEncodedFormEntity(params));
	                } 
	                httpResponse = httpClient.execute(httpPost); 
	            } 
	            else if (method == GET) 
	            {
	                // appending params to url
	                if (params != null) 
	                {
	                    String paramString = URLEncodedUtils.format(params, "utf-8");
	                    url += "?" + paramString;
	                }
	                Log.e(MODULE, TAG + " URL : " + url);
	                HttpGet httpGet = new HttpGet(url); 
	                httpResponse = httpClient.execute(httpGet); 
	            }
	            System.out.println(url);
	            httpEntity = httpResponse.getEntity();
	            response = EntityUtils.toString(httpEntity); 	           
        	}        	
	         
	        catch (UnsupportedEncodingException e) 
	        {
	            e.printStackTrace();
	            Log.e(MODULE, TAG + " Exception Occurs : " + e);
	        }
	        catch (ClientProtocolException e) 
	        {
	            e.printStackTrace();
	            Log.e(MODULE, TAG + " Exception Occurs : " + e);
	        }
	        catch (UnknownHostException e) 
	        {
	        	response="UH";	        
	        	Log.e(MODULE, TAG + " Exception Occurs : " + e);
	        }
    		catch (ConnectException e) 
	        {
    			response="INA";	      
    			Log.e(MODULE, TAG + " Exception Occurs : " + e);
	        }
    		catch (IOException e) 
	        {
    			e.printStackTrace();
    			Log.e(MODULE, TAG + " Exception Occurs : " + e);
	        }
        
        
        return response; 
    }
}