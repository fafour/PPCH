package th.go.nacc.nacc_law.model;

import android.content.Context;
import android.provider.Settings;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import th.go.nacc.nacc_law.AbstractActivity;
import th.go.nacc.nacc_law.R;
import th.go.nacc.nacc_law.application.MyApplication;

/**
 * Created by nontachai on 7/27/15 AD.
 */
public class DeviceToken {

    public static void send ( final Context context, final String token, final boolean isShowProgress, final  OnSendDeviceListener listerner ) {
        MyApplication myApplication = (MyApplication) context.getApplicationContext();

        String url = String.format(
                "%s/setDeviceToken/%s",
                MyApplication.getInstance().API_HOSTNAME,
                MyApplication.getInstance().API_KEY
        );

        if ( isShowProgress ) {
            // hide progress
            ((AbstractActivity) context).showProgress(true);
        }


        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String s) {

                        if ( isShowProgress ) {
                            // hide progress
                            ((AbstractActivity) context).showProgress(false);
                        }


                        Log.i("SEND_DEVICE", "RESPONSE: " + s);

                        try {
                            JSONObject responseObject = new JSONObject( s );

                            if ( responseObject.optInt ( "status" ) == 0 ) {
                                JSONObject error = responseObject.optJSONObject( "error" );

                                Error serverError = new Error( error.optString( "detail" ) );

                                listerner.onResult(serverError);

                                return;
                            } else {

                                boolean result = responseObject.optBoolean("data");

                                listerner.onResult(null);
                                return;
                            }
                        } catch ( JSONException e ) {
                            Error parseError = new Error(
                                    context.getResources().getString(R.string.error_json_parse)
                            );

                            listerner.onResult(parseError );

                            return;
                        }

                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if ( isShowProgress ) {
                            // hide progress
                            ((AbstractActivity) context).showProgress(false);
                        }

                        Error networkError = null;

                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            networkError = new Error( context.getString( R.string.error_network_timeout ));
                        } else if (error instanceof AuthFailureError) {
                            networkError = new Error( context.getString( R.string.error_auth_failure ));
                        } else if (error instanceof ServerError) {
                            networkError = new Error( context.getString( R.string.error_server ));
                        } else if (error instanceof NetworkError) {
                            networkError = new Error( context.getString( R.string.error_network ));
                        } else if (error instanceof ParseError) {
                            networkError = new Error( context.getString( R.string.error_json_parse ));
                        }

                        if ( networkError != null ) {
                            listerner.onResult(networkError);
                        }

                        return;
                    }
                }
        ) {


            @Override
            protected Map<String, String> getParams()  {
                Map<String, String> params = new HashMap<String, String>();

                String android_id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);

                params.put( "device_token", token );
                params.put( "device_id", android_id );
                params.put( "device_type", "android" );

                Log.i( "TAG", "DEVICE TOKEN: " + token );
                Log.i( "TAG", "DEVICE ID: " + android_id );
                Log.i( "TAG", "DEVICE TYPE: " + "android" );
                return params;
            }
        };

        MyApplication.getInstance().addToRequestQueue( request );

    }

    public interface OnSendDeviceListener {
        public void onResult ( Error error );
    }
}
