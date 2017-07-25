package th.go.nacc.nacc_law.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nontachai on 7/20/15 AD.
 */
public class Amphur implements Parcelable {
    private int id;
    private String name;
    private int province;

    public Amphur () {}

    public Amphur ( Parcel in ) {
        id = in.readInt();
        name = in.readString();
        province = in.readInt();
    }

    public Amphur ( int id, String name, int province ) {
        this.id = id;
        this.name = name;
        this.province = province;
    }

    public static Amphur convertToObject ( JSONObject data ) {
        if ( data == null ) return null;

        Amphur amphur = new Amphur(
                data.optInt( "id" ),
                data.optString( "name" ),
                data.optInt( "province" )
        );

        return amphur;
    }

    public static List<Amphur> convertToArray ( JSONArray datas ) {
        if ( datas == null ) return new ArrayList<Amphur>();

        List<Amphur> amphurs = new ArrayList<Amphur>();

        for ( int i = 0, max = datas.length(); i < max; i++ ) {
            Amphur amphur = convertToObject( datas.optJSONObject( i ));

            if ( amphur == null ) continue;;

            amphurs.add( amphur );
        }

        return amphurs;
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

    public int getProvince() {
        return province;
    }

    public void setProvince(int province) {
        this.province = province;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt( id );
        parcel.writeString(name);
        parcel.writeInt( province );
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator<Amphur>() {
        @Override
        public Amphur createFromParcel(Parcel parcel) {
            return new Amphur(parcel);
        }

        @Override
        public Amphur[] newArray(int size) {
            return new Amphur[size];
        }
    };
}
