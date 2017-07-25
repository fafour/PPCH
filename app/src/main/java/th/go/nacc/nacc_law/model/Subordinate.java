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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import th.go.nacc.nacc_law.AbstractActivity;
import th.go.nacc.nacc_law.R;
import th.go.nacc.nacc_law.application.MyApplication;

/**
 * Created by nontachai on 7/20/15 AD.
 */
public class Subordinate implements Parcelable {
    private int id;
    private String name;
    private int amphur;

    public Subordinate () { }

    public Subordinate ( Parcel in ) {
        id = in.readInt();
        name = in.readString();
        amphur = in.readInt();
    }

    public Subordinate ( int id, String name, int amphur ) {
        this.id = id;
        this.name = name;
        this.amphur = amphur;
    }

    public static Subordinate convertToObject ( JSONObject data ) {
        if ( data == null ) return null;

        Subordinate item = new Subordinate(
                data.optInt( "id" ),
                data.optString( "name" ),
                data.optInt( "amphur" )
        );

        return item;
    }

    public static List<Subordinate> convertToArray ( JSONArray datas ) {
        if ( datas == null ) return new ArrayList<Subordinate>();

        List<Subordinate> items = new ArrayList<Subordinate>();

        for ( int i = 0, max = datas.length(); i < max; i++ ) {
            Subordinate item = convertToObject( datas.optJSONObject( i ));

            if ( item == null ) continue;;

            items.add( item );
        }

        return items;
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

    public int getAmphur() {
        return amphur;
    }

    public void setAmphur(int amphur) {
        this.amphur = amphur;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt( id );
        parcel.writeString(name);
        parcel.writeInt( amphur );
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator<Subordinate>() {
        @Override
        public Subordinate createFromParcel(Parcel parcel) {
            return new Subordinate(parcel);
        }

        @Override
        public Subordinate[] newArray(int size) {
            return new Subordinate[size];
        }
    };

    public static void get ( final Context context, final int subordinate, final int amphur, final  OnGetItemListener listerner ) {
        MyApplication myApplication = (MyApplication) context.getApplicationContext();

        String url = String.format(
                "%s/getSubordinateItem/%s",
                MyApplication.getInstance().API_HOSTNAME,
                MyApplication.getInstance().API_KEY
        );

        // show progress
        ((AbstractActivity)context).showProgress ( true );

        StringRequest request = new StringRequest(
                Request.Method.POST,
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

                                listerner.onResult( null, serverError );

                                return;
                            } else {

                                JSONArray datas = responseObject.optJSONArray("data");


                                List<Subordinate> items = convertToArray( datas );

                                listerner.onResult(items, null);

                                return;
                            }
                        } catch ( JSONException e ) {
                            Error parseError = new Error(
                                    context.getResources().getString(R.string.error_json_parse)
                            );

                            listerner.onResult( null, parseError );

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
                            listerner.onResult(null, networkError);
                        }

                        return;
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put( "subordinate", String.valueOf( subordinate ));
                params.put( "amphur", String.valueOf( amphur ));

                return params;
            }
        };

        MyApplication.getInstance().addToRequestQueue( request );

    }

    public interface OnGetItemListener {
        public void onResult ( List<Subordinate> items, Error error );
    }

}
