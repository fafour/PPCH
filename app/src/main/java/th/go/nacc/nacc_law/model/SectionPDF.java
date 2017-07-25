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
public class SectionPDF implements Parcelable {

    private int id;
    private int section;
    private String title;
    private String link;
    private String linkPath;
    private int seq;

    public SectionPDF () { }

    public SectionPDF ( Parcel in ) {
        id = in.readInt();
        section = in.readInt();
        title = in.readString();
        link = in.readString();
        linkPath = in.readString();
        seq = in.readInt();
    }

    public SectionPDF ( int id, int section, String title, String link, String linkPath, int seq ) {
        this.id = id;
        this.section = section;
        this.title = title;
        this.link = link;
        this.linkPath = linkPath;
        this.seq = seq;
    }

    public static SectionPDF convertToObject ( JSONObject data ) {
        if ( data == null ) return null;

        SectionPDF pdf = new SectionPDF(
                data.optInt( "id" ),
                data.optInt( "section" ),
                data.optString( "title" ),
                data.optString( "link" ),
                data.optString( "link_path" ),
                data.optInt( "seq" )
        );

        return pdf;
    }

    public static List<SectionPDF> convertToArray ( JSONArray datas ) {
        if ( datas == null ) return new ArrayList<SectionPDF>();

        List<SectionPDF> pdfs = new ArrayList<SectionPDF>();

        for ( int i = 0, max = datas.length(); i < max; i++ ) {
            SectionPDF pdf = convertToObject( datas.optJSONObject( i ) );

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

    public int getSection() {
        return section;
    }

    public void setSection(int section) {
        this.section = section;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt( id );
        parcel.writeInt( section );
        parcel.writeString(title);
        parcel.writeString(link);
        parcel.writeString(linkPath);
        parcel.writeInt( seq );
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator<SectionPDF>() {

        @Override
        public SectionPDF createFromParcel(Parcel parcel) {
            return new SectionPDF( parcel );
        }

        @Override
        public SectionPDF[] newArray(int i) {
            return new SectionPDF[i];
        }
    };
}
