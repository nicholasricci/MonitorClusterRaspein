/**
 * @author Nicholas Ricci
 */
package it.unibo.monitorhpc.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

import android.os.AsyncTask;
import android.util.Log;

public class DownloadRaspberriesDetails extends AsyncTask<Void, Void, String>{
	private static final String ERROR_TAG = "Download AsyncTask Error";
	private static final String DEBUG_TAG = "Download AsyncTask Debug";
	
	DownloadRaspberriesDetailsInterface downloadRaspberriesDetailsInterface;
	
	String url = null;
	
	public DownloadRaspberriesDetails(String url, DownloadRaspberriesDetailsInterface downloadRaspberriesDetailsInterface){
		this.url = url;
		this.downloadRaspberriesDetailsInterface = downloadRaspberriesDetailsInterface;
	}
	
	@Override
	protected String doInBackground(Void... params) {
		String result = "";
		try {
			result = downloadUrl(this.url);
		} catch (IOException e) {
			Log.e(ERROR_TAG, e.getMessage());
		}
		return result;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		
		downloadRaspberriesDetailsInterface.getResult(result);
	}
	
	public interface DownloadRaspberriesDetailsInterface{
		public void getResult(String result);
	}
	
	public void setDownloadRaspberriesDetailsInterface(DownloadRaspberriesDetailsInterface downloadRaspberriesDetailsInterface){
		this.downloadRaspberriesDetailsInterface = downloadRaspberriesDetailsInterface;
	}
	
	private String downloadUrl(String myurl) throws IOException {
	    InputStream is = null;
	    // Only display the first 500 characters of the retrieved
	    // web page content.
	    int len = 500;
	        
	    try {
	        URL url = new URL(myurl);
	        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	        conn.setReadTimeout(10000 /* milliseconds */);
	        conn.setConnectTimeout(15000 /* milliseconds */);
	        conn.setRequestMethod("GET");
	        conn.setDoInput(true);
	        // Starts the query
	        conn.connect();
	        int response = conn.getResponseCode();
	        Log.d(DEBUG_TAG, "The response is: " + response);
	        is = conn.getInputStream();

	        // Convert the InputStream into a string
	        String contentAsString = readIt(is, len);
	        return contentAsString;
	        
	    // Makes sure that the InputStream is closed after the app is
	    // finished using it.
	    } finally {
	        if (is != null) {
	            is.close();
	        } 
	    }
	}
	
	// Reads an InputStream and converts it to a String.
	private String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
	    Reader reader = null;
	    reader = new InputStreamReader(stream, "UTF-8");        
	    char[] buffer = new char[len];
	    reader.read(buffer);
	    return new String(buffer);
	}
}
