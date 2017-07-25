package th.go.nacc.nacc_law.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nontachai on 7/10/15 AD.
 */
public class Terminology implements Parcelable {

    private int id;
    private String wording;
    private String detail;

    public Terminology() { }

    public Terminology ( Parcel in ) {
        id = in.readInt();
        wording = in.readString();
        detail = in.readString();
    }

    public Terminology ( int id, String wording, String detail ) {
        this.id = id;
        this.wording = wording;
        this.detail = detail;
    }

    public static Terminology convertToObject ( JSONObject data ) {
        if ( data == null ) return null;

        Terminology term = new Terminology(
                data.optInt( "id" ),
                data.optString( "wording" ),
                data.optString( "description" )
        );

        return term;
    }

    public static List<Terminology> convertToArray ( JSONArray datas ) {
        if ( datas == null ) return new ArrayList<Terminology>();

        List<Terminology> terms = new ArrayList<Terminology>();

        for ( int i = 0, max = datas.length(); i < max; i++ ) {
            Terminology term = convertToObject( datas.optJSONObject( i ));

            if ( term == null ) continue;;

            terms.add( term );
        }
        return terms;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWording() {
        return wording;
    }

    public void setWording(String wording) {
        this.wording = wording;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt( id );
        parcel.writeString(wording);
        parcel.writeString( detail );
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator<Terminology>() {
        @Override
        public Terminology createFromParcel(Parcel parcel) {
            return new Terminology(parcel);
        }

        @Override
        public Terminology[] newArray(int i) {
            return new Terminology[i];
        }
    };
}
