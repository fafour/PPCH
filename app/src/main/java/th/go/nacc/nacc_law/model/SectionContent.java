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
public class SectionContent implements Parcelable {

    private int id;
    private int section;
    private String title;
    private String detail;
    private int seq;
    private String clip;
    private String image;
    private String audio;

    public SectionContent () { }

    public String getClip() {
        return clip;
    }

    public void setClip(String clip) {
        this.clip = clip;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public SectionContent (Parcel in ) {
        id = in.readInt();
        section = in.readInt();
        title = in.readString();
        detail = in.readString();
        seq = in.readInt();
        clip = in.readString();
        image  = in.readString();
        audio = in.readString();
    }

    public SectionContent ( int id, int section, String title, String detail, int seq ,String clip,String image,String audio) {
        this.id = id;
        this.section = section;
        this.title = title;
        this.detail = detail;
        this.seq = seq;
        this.clip = clip;
        this.image = image;
        this.audio = audio;
    }

    public static SectionContent convertToObject ( JSONObject data ) {
        if ( data == null ) return null;

        SectionContent content = new SectionContent(
                data.optInt( "id" ),
                data.optInt( "section" ),
                data.optString( "title" ),
                data.optString( "detail" ),
                data.optInt( "seq" ),
                data.optString( "clip" ),
                data.optString( "image" ),
                data.optString( "audio" )
        );

        return content;
    }

    public static List<SectionContent> convertToArray ( JSONArray datas ) {
        if ( datas == null ) return new ArrayList<SectionContent>();

        List<SectionContent> contents = new ArrayList<SectionContent>();

        for ( int i = 0, max = datas.length(); i < max; i++ ) {
            SectionContent content = convertToObject( datas.optJSONObject( i ));

            if ( content == null ) continue;

            contents.add( content );
        }

        return contents;
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

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
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
        parcel.writeString(detail);
        parcel.writeInt( seq );
        parcel.writeString(clip);
        parcel.writeString(image);
        parcel.writeString(audio);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator<SectionContent>() {

        @Override
        public SectionContent createFromParcel(Parcel parcel) {
            return new SectionContent( parcel );
        }

        @Override
        public SectionContent[] newArray(int i) {
            return new SectionContent[i];
        }
    };
}
