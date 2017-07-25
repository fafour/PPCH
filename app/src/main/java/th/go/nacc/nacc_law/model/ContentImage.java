package th.go.nacc.nacc_law.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

/**
 * Created by nontachai on 7/27/15 AD.
 */
public class ContentImage implements Parcelable {

    private String s;
    private String m;
    private String l;
    private String o;


    public ContentImage () { }

    public ContentImage ( Parcel in ) {
        s = in.readString();
        m = in.readString();
        l = in.readString();
        o = in.readString();
    }

    public ContentImage ( String s, String m, String l, String o) {
        this.s = s;
        this.m = m;
        this.l = l;
        this.o = o;
    }

    public static ContentImage convertToObject ( JSONObject data ) {
        if ( data == null ) return null;

        ContentImage image = new ContentImage(
                data.optString( "s" ),
                data.optString( "m" ),
                data.optString( "l" ),
                data.optString( "o" )
        );

        return image;
    }
    public String getS() {
        return s;
    }

    public void setS(String s) {
        this.s = s;
    }

    public String getM() {
        return m;
    }

    public void setM(String m) {
        this.m = m;
    }

    public String getL() {
        return l;
    }

    public void setL(String l) {
        this.l = l;
    }

    public String getO() {
        return o;
    }

    public void setO(String o) {
        this.o = o;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString( s );
        parcel.writeString( m );
        parcel.writeString( l );
        parcel.writeString( o );
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator<ContentImage>() {
        @Override
        public ContentImage createFromParcel(Parcel parcel) {
            return new ContentImage(parcel);
        }

        @Override
        public ContentImage[] newArray(int size) {
            return new ContentImage[size];
        }
    };
}
