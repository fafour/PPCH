package th.go.nacc.nacc_law.model;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
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
 * Created by nontachai on 7/19/15 AD.
 */
public class ExaminationLevelList implements Parcelable {
    private int id;
    private String name;
    private String detail;
    private int section;
    private int maxPoint;

    public ExaminationLevelList () { }

    public ExaminationLevelList ( Parcel in ) {
        id = in.readInt();
        name = in.readString();
        detail = in.readString();
        section = in.readInt();
        maxPoint = in.readInt();
    }

    public ExaminationLevelList (
            int id,
            String name,
            String detail,
            int section,
            int maxPoint ) {
        this.id = id;
        this.name = name;
        this.detail = detail;
        this.section = section;
        this.maxPoint = maxPoint;
    }

    public static ExaminationLevelList convertToObject ( JSONObject data ) {
        if ( data == null ) return null;

        ExaminationLevelList levelList = new ExaminationLevelList();

        levelList.id = data.optInt( "id" );
        levelList.name = data.optString("name");
        levelList.detail = data.optString("detail");
        levelList.section = data.optInt("section");
        levelList.maxPoint = data.optInt( "max_point");

        return levelList;
    }

    public static List<ExaminationLevelList> convertToArray ( JSONArray datas ) {
        if ( datas == null ) return new ArrayList<ExaminationLevelList>();

        List<ExaminationLevelList> levelLists = new ArrayList<ExaminationLevelList>();

        for ( int i = 0, max = datas.length(); i < max; i++ ) {
            ExaminationLevelList levelList = convertToObject( datas.optJSONObject( i ));

            if ( levelList == null ) continue;;

            levelLists.add( levelList );
        }
        return levelLists;
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

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public int getSection() {
        return section;
    }

    public void setSection(int section) {
        this.section = section;
    }

    public int getMaxPoint() {
        return maxPoint;
    }

    public void setMaxPoint(int maxPoint) {
        this.maxPoint = maxPoint;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt( id );
        parcel.writeString( name );
        parcel.writeString( detail );
        parcel.writeInt( section );
        parcel.writeInt( maxPoint );
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator<ExaminationLevelList>() {
        @Override
        public ExaminationLevelList createFromParcel(Parcel parcel) {
            return new ExaminationLevelList(parcel);
        }

        @Override
        public ExaminationLevelList[] newArray(int size) {
            return new ExaminationLevelList[size];
        }
    };


    public static void get ( final Context context, final int sectionID, final int memberID, final  OnGetListener listerner ) {
        MyApplication myApplication = (MyApplication) context.getApplicationContext();

        String url = String.format(
                "%s/getSectionExaminationLevel/%s",
                MyApplication.getInstance().API_HOSTNAME,
                MyApplication.getInstance().API_KEY
        );

        //Log.i("LOGIN_TAG", "URL: " + url);

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

                                listerner.getResult(new ArrayList<ExaminationLevelList>(), serverError);

                                return;
                            } else {

                                JSONArray datas = responseObject.optJSONArray("data");

                                listerner.getResult( ExaminationLevelList.convertToArray( datas), null );

                                return;
                            }
                        } catch ( JSONException e ) {
                            Error parseError = new Error(
                                    context.getResources().getString(R.string.error_json_parse)
                            );

                            listerner.getResult(new ArrayList<ExaminationLevelList>(), parseError);

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
                            listerner.getResult(new ArrayList<ExaminationLevelList>(), networkError);
                        }

                        return;
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();

                param.put( "section_id", String.valueOf ( sectionID ) );
                param.put( "member_id", String.valueOf ( memberID ) );

                return param;
            }
        };

        MyApplication.getInstance().addToRequestQueue( request );

    }

    public interface OnGetListener {
        public void getResult ( List<ExaminationLevelList> levelLists, Error error );
    }
}
