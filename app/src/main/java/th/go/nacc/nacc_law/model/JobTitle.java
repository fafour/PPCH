package th.go.nacc.nacc_law.model;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import th.go.nacc.nacc_law.AbstractActivity;
import th.go.nacc.nacc_law.R;
import th.go.nacc.nacc_law.application.MyApplication;

/**
 * Created by nontachai on 8/18/15 AD.
 */
public class JobTitle implements Parcelable {

    private int id;
    private String name;
    private int isOther;

    public int getSub_id() {
        return sub_id;
    }

    public void setSub_id(int sub_id) {
        this.sub_id = sub_id;
    }

    private int sub_id;

    public JobTitle ( int id, String name, int isOther ,int sub_id) {
        this.id = id;
        this.name = name;
        this.isOther = isOther;
        this.sub_id = sub_id;
    }

    public JobTitle () { }

    public JobTitle ( Parcel in ) {
        id = in.readInt();
        name = in.readString();
        isOther = in.readInt();
        sub_id = in.readInt();
    }


    public static JobTitle convertToObject ( JSONObject data ) {
        if ( data == null ) return null;

        JobTitle jobTitle = new JobTitle(
                data.optInt( "id" ),
                data.optString( "name" ),
                data.optInt( "is_other" ),
                data.optInt( "sub_id" )
        );

        return jobTitle;
    }

    public static List<JobTitle> convertToArray ( JSONArray datas ) {
        if ( datas == null ) return new ArrayList<JobTitle>();

        List<JobTitle> jobTitles = new ArrayList<JobTitle>();

        for ( int i = 0, max = datas.length(); i < max; i++ ) {
            JobTitle jobTitle = convertToObject( datas.optJSONObject( i ));

            if ( jobTitle == null ) continue;

            jobTitles.add( jobTitle );
        }

        return jobTitles;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIsOther() {
        return isOther;
    }

    public void setIsOther(int isOther) {
        this.isOther = isOther;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt( id );
        parcel.writeString(name);
        parcel.writeInt( isOther );
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator<JobTitle>() {
        @Override
        public JobTitle createFromParcel(Parcel parcel) {
            return new JobTitle( parcel );
        }

        @Override
        public JobTitle[] newArray(int i) {
            return new JobTitle[i];
        }
    };


    public static void get ( final Context context, final  OnResponseListener listerner ) {
        MyApplication myApplication = (MyApplication) context.getApplicationContext();

        String url = String.format(
                "%s/getJobTitle/%s",
                MyApplication.getInstance().API_HOSTNAME,
                MyApplication.getInstance().API_KEY
        );


        // show progress
        ((AbstractActivity)context).showProgress ( true );

        StringRequest request = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String s) {

                        // hide progress
                        ((AbstractActivity)context).showProgress ( false );

                        try {
                            JSONObject responseObject = new JSONObject( s );

                            if ( responseObject.optInt ( "status" ) == 0 ) {
                                JSONObject error = responseObject.optJSONObject( "error" );

                                Error serverError = new Error( error.optString( "detail" ) );

                                listerner.onGetListener( null, serverError );

                                return;
                            } else {

                                JSONArray data = responseObject.optJSONArray("data");

                                List<JobTitle> jobTitles = convertToArray(data);

                                listerner.onGetListener( jobTitles, null );

                                return;
                            }
                        } catch ( JSONException e ) {
                            Error parseError = new Error(
                                    context.getResources().getString(R.string.error_json_parse)
                            );

                            listerner.onGetListener( null, parseError );

                            return;
                        }

                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // hide progress
                        ((AbstractActivity)context).showProgress ( false );

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
                            listerner.onGetListener(null, networkError);
                        }

                        return;
                    }
                }
        );

        MyApplication.getInstance().addToRequestQueue( request );

    }

    public interface OnResponseListener {
        public void onGetListener ( List<JobTitle> jobTitles, Error error);
    }
}
