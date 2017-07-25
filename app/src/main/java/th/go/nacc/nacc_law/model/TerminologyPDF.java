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
public class TerminologyPDF implements Parcelable {

    private int id;
    private String wording;
    private String link;
    private String linkPath;

    public TerminologyPDF () { }

    public TerminologyPDF ( Parcel in ) {
        id = in.readInt();
        wording = in.readString();
        link = in.readString();
        linkPath = in.readString();
    }

    public TerminologyPDF ( int id, String wording, String link, String linkPath ) {
        this.id = id;
        this.wording = wording;
        this.link = link;
        this.linkPath = linkPath;
    }

    public static TerminologyPDF convertToObject ( JSONObject data ) {
        if ( data == null ) return null;

        TerminologyPDF pdf = new TerminologyPDF(
                data.optInt( "id" ),
                data.optString( "wording" ),
                data.optString( "link" ),
                data.optString( "link_path" )
        );

        return pdf;
    }

    public static List<TerminologyPDF> convertToArray ( JSONArray datas ) {
        if ( datas == null ) return new ArrayList<TerminologyPDF>();

        List<TerminologyPDF> pdfs = new ArrayList<TerminologyPDF>();

        for ( int i = 0, max = datas.length(); i < max; i++ ) {
            TerminologyPDF pdf = convertToObject( datas.optJSONObject( i ) );

            if ( pdf == null ) continue;;

            pdfs.add( pdf );
        }

        return pdfs;
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

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getLinkPath() {
        return linkPath;
    }

    public void setLinkPath(String linkPath) {
        this.linkPath = linkPath;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt( id );
        parcel.writeString(wording);
        parcel.writeString(link);
        parcel.writeString( linkPath );
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator<TerminologyPDF>() {
        @Override
        public TerminologyPDF createFromParcel(Parcel parcel) {
            return new TerminologyPDF(parcel);
        }

        @Override
        public TerminologyPDF[] newArray(int i) {
            return new TerminologyPDF[i];
        }
    };
}
