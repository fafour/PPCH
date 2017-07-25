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
 * Created by nontachai on 7/20/15 AD.
 */
public class Province implements Parcelable {

    private int id;
    private String name;
    private List<Amphur> amphurs;

    public Province () { }

    public Province ( Parcel in ) {
        id = in.readInt();
        name = in.readString();
        amphurs = in.readArrayList(Amphur.class.getClassLoader());
    }

    public Province ( int id, String name ) {
        this.id = id;
        this.name = name;
        this.amphurs = new ArrayList<Amphur>();
    }

    public Province ( int id, String name, List<Amphur> amphurs ) {
        this.id = id;
        this.name = name;
        this.amphurs = amphurs;
    }

    public static Province convertToObject ( JSONObject data ) {
        if ( data == null ) return null;

        Province province = new Province(
                data.optInt( "id" ),
                data.optString( "name" ),
                Amphur.convertToArray( data.optJSONArray( "amphurs" ))
        );

        return province;
    }

    public static List<Province> convertToArray ( JSONArray datas ) {
        if ( datas == null ) return new ArrayList<Province>();

        List<Province> provinces = new ArrayList<Province>();

        for ( int i = 0, max = datas.length(); i < max; i++ ) {
            Province province = convertToObject( datas.optJSONObject( i ));

            if ( province == null ) continue;;

            provinces.add( province );
        }
        return provinces;
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

    public List<Amphur> getAmphurs() {
        return amphurs;
    }

    public void setAmphurs(List<Amphur> amphurs) {
        this.amphurs = amphurs;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt( id );
        parcel.writeString(name);
        parcel.writeList(amphurs);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator<Province>() {
        @Override
        public Province createFromParcel(Parcel parcel) {
            return new Province(parcel);
        }

        @Override
        public Province[] newArray(int size) {
            return new Province[size];
        }
    };

    public static void get ( final Context context, final  OnGetProvinceListener listerner ) {
        MyApplication myApplication = (MyApplication) context.getApplicationContext();

        String url = String.format(
                "%s/getProvinceAmphur/%s",
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

                                listerner.onResult( null, serverError );

                                return;
                            } else {

                                JSONArray datas = responseObject.optJSONArray("data");


                                List<Province> provinces = convertToArray( datas );

                                listerner.onResult(provinces, null);

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
        );

        MyApplication.getInstance().addToRequestQueue( request );

    }

    public interface OnGetProvinceListener {
        public void onResult ( List<Province> provinces, Error error );
    }
}
