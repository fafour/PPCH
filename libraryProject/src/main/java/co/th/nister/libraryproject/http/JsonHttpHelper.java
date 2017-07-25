package co.th.nister.libraryproject.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyStore;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;

public class JsonHttpHelper 
{
	private String jsonData = "";
	
	private Context mContext;
	
	private Handler handler;
	
	public static HttpClient getNewHttpClient() {
	    try {
	        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
	        trustStore.load(null, null);

	        SSLSocketFactory sf = new WebClientDevWrapper(trustStore);
	        sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

	        HttpParams params = new BasicHttpParams();
	        
			HttpConnectionParams.setConnectionTimeout( params, 10000 );
			
			HttpConnectionParams.setSoTimeout(params, 10000 );
			
	        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
	        HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

	        SchemeRegistry registry = new SchemeRegistry();
	        registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
	        registry.register(new Scheme("https", sf, 443));

	        ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

	        return new DefaultHttpClient(ccm, params);
	    } catch (Exception e) {
	        return new DefaultHttpClient();
	    }
	}
	
	public JsonHttpHelper ( Context context, String url ) throws IOException
	{
		mContext = context;
		
		HttpParams httpParameters = new BasicHttpParams();
		
		HttpConnectionParams.setConnectionTimeout( httpParameters, 10000 );
		
		HttpConnectionParams.setSoTimeout(httpParameters, 10000 );
		
		//DefaultHttpClient httpClient = new DefaultHttpClient ( httpParameters );
		HttpClient httpClient = getNewHttpClient();
		
		HttpGet httpGet = new HttpGet ( url );
		
		try 
		{
			HttpResponse httpResponse = httpClient.execute ( httpGet );
			
			final int statusCode = httpResponse.getStatusLine ( ).getStatusCode ( );
			
			if ( statusCode == HttpStatus.SC_OK ) 
			{
				HttpEntity httpEntity = httpResponse.getEntity ( );
				
				InputStream is = httpEntity.getContent ( );
				
				BufferedReader reader = new BufferedReader ( new InputStreamReader ( is ) );
				
				String tempLine = "";
				
				while ( ( tempLine = reader.readLine ( ) ) != null ) 
				{
					this.jsonData = this.jsonData + tempLine;
				}
			}
		}
		catch ( IOException e ) 
		{
			//handler = new Handler();
			//handler.post( r );
			//Toast.makeText( mContext, mContext.getString( R.string.network_unavailable ), Toast.LENGTH_LONG ).show ( );
			
			e.printStackTrace();
			throw e;
			
		}
	}
	
	public static String postRequest (String path, JSONObject params) throws Exception 
	{
	    //instantiates httpclient to make request
	    DefaultHttpClient httpclient = new DefaultHttpClient();

	    //url with the post data
	    HttpPost httpost = new HttpPost(path);

	    //convert parameters into JSON object
	    //JSONObject holder = getJsonObjectFromMap(params);

	    //passes the results to a string builder/entity
	    StringEntity se = new StringEntity(params.toString());

	    //sets the post request as the resulting string
	    httpost.setEntity(se);
	    //sets a request header so the page receving the request
	    //will know what to do with it
	    httpost.setHeader("Accept", "application/json");
	    httpost.setHeader("Content-type", "application/json");

	    //Handles what is returned from the page 
	    BasicResponseHandler responseHandler = new BasicResponseHandler();
	    return httpclient.execute(httpost, responseHandler);
	}
	
	
	public static String postMethod ( String urlRequest, List<NameValuePair> params ) throws IOException {
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost( urlRequest );
		
		String jsonData = "";
		
		try {
			httpPost.setEntity( new UrlEncodedFormEntity( params ) );
			HttpResponse response = httpClient.execute( httpPost );
			
			final int statusCode = response.getStatusLine ( ).getStatusCode ( );
			
			if ( statusCode == HttpStatus.SC_OK ) 
			{
				HttpEntity httpEntity = response.getEntity ( );
				
				InputStream is = httpEntity.getContent ( );
				
				BufferedReader reader = new BufferedReader ( new InputStreamReader ( is ) );
				
				String tempLine = "";
				
				
				while ( ( tempLine = reader.readLine ( ) ) != null ) 
				{
					jsonData = jsonData + tempLine;
				}
			}
			
			return jsonData;
		} catch ( ClientProtocolException e ) {
			e.printStackTrace();
			
			return jsonData;
		}
	}
	
	public static String postMethod ( String urlRequest, String paramters ) throws IOException
	{
		HttpURLConnection connection;
		OutputStreamWriter request = null;
		
		URL url = null;
		String response = null;
		
		
		
		try {
			url = new URL( urlRequest );
			connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput( true );
			connection.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded" );
			connection.setRequestMethod( "POST" );
			
			request = new OutputStreamWriter( connection.getOutputStream() );
			request.write( paramters );
			request.flush();
			request.close();
			
			/*
			try {
				String line = "";
				InputStreamReader isr = new InputStreamReader( connection.getInputStream() );
				BufferedReader reader = new BufferedReader( isr );
				
				StringBuilder sb = new StringBuilder();
				while ( (line = reader.readLine()) != null )
				{
					sb.append( line + "\n" );
				}
				
				// Response from server after login process will be stored in response variable
				response = sb.toString();
				
				isr.close();
				reader.close();
			} catch ( FileNotFoundException e ) {
				e.printStackTrace();
				response = "";
			}
			*/
			
			InputStream in = connection.getInputStream();
			String encoding = connection.getContentEncoding();
			encoding = encoding == null ? "UTF-8" : encoding;
			response = IOUtils.toString(in, encoding);
			
			in.close();
		} catch ( IOException e )
		{
			// Error
			throw e;
		}
		
		return response;
	}
	
	public static String getMethod ( String url ) throws IOException
	{
		
		HttpParams httpParameters = new BasicHttpParams();
		
		HttpConnectionParams.setConnectionTimeout( httpParameters, 10000 );
		
		HttpConnectionParams.setSoTimeout(httpParameters, 10000 );
		
		//DefaultHttpClient httpClient = new DefaultHttpClient ( httpParameters );
		HttpClient httpClient = getNewHttpClient();
		
		HttpGet httpGet = new HttpGet ( url );
		
		String jsonData = "";
		try 
		{
			HttpResponse httpResponse = httpClient.execute ( httpGet );
			
			final int statusCode = httpResponse.getStatusLine ( ).getStatusCode ( );
			
			if ( statusCode == HttpStatus.SC_OK ) 
			{
				HttpEntity httpEntity = httpResponse.getEntity ( );
				
				InputStream is = httpEntity.getContent ( );
				
				BufferedReader reader = new BufferedReader ( new InputStreamReader ( is ) );
				
				String tempLine = "";
				
				
				while ( ( tempLine = reader.readLine ( ) ) != null ) 
				{
					jsonData = jsonData + tempLine;
				}
			}
		}
		catch ( IOException e ) 
		{
			//handler = new Handler();
			//handler.post( r );
			//Toast.makeText( mContext, mContext.getString( R.string.network_unavailable ), Toast.LENGTH_LONG ).show ( );
			
			e.printStackTrace();
			throw e;
			
		}
		
		return jsonData;
	}
	
	private static JSONObject getJsonObjectFromMap(Map params) throws JSONException {

	    //all the passed parameters from the post request
	    //iterator used to loop through all the parameters
	    //passed in the post request
	    Iterator iter = params.entrySet().iterator();

	    //Stores JSON
	    JSONObject holder = new JSONObject();

	    //using the earlier example your first entry would get email
	    //and the inner while would get the value which would be 'foo@bar.com' 
	    //{ fan: { email : 'foo@bar.com' } }

	    //While there is another entry
	    while (iter.hasNext()) 
	    {
	        //gets an entry in the params
	        Map.Entry pairs = (Map.Entry)iter.next();

	        //creates a key for Map
	        String key = (String)pairs.getKey();

	        //Create a new map
	        Map m = (Map)pairs.getValue();   

	        //object for storing Json
	        JSONObject data = new JSONObject();

	        //gets the value
	        Iterator iter2 = m.entrySet().iterator();
	        while (iter2.hasNext()) 
	        {
	            Map.Entry pairs2 = (Map.Entry)iter2.next();
	            data.put((String)pairs2.getKey(), (String)pairs2.getValue());
	        }

	        //puts email and 'foo@bar.com'  together in map
	        holder.put(key, data);
	    }
	    return holder;
	}
	
	private Runnable r = new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			
		}
	};
	
	public String getJSONData ( )
	{
		return this.jsonData;
	}
}
