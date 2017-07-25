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
public class Section implements Parcelable {
    private int id;
    private String name;
    private String alias;
    private String detail;

    public Section () {}

    public Section ( Parcel in ) {
        id = in.readInt();
        name = in.readString();
        alias = in.readString();
        detail = in.readString();
    }

    public Section (int id, String name, String alias, String detail ) {
        this.id = id;
        this.name = name;
        this.alias = alias;
        this.detail = detail;
    }

    public static Section convertToObject ( JSONObject data ) {
        if ( data == null ) return null;

        Section section = new Section(
                data.optInt( "id" ),
                data.optString( "name" ),
                data.optString( "alias" ),
                data.optString( "detail" )
        );

        return section;
    }

    public static List<Section> convertToArray ( JSONArray datas ) {
        if ( datas == null ) return new ArrayList<Section>();

        List<Section> sections = new ArrayList<Section>();

        for ( int i = 0, max = datas.length(); i < max; i++ ) {
            Section section = convertToObject( datas.optJSONObject( i ));

            if ( section == null ) continue;

            sections.add( section );
        }

        return sections;
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

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
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
        parcel.writeString( name );
        parcel.writeString( alias );
        parcel.writeString( detail );
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator<Section>() {

        @Override
        public Section createFromParcel(Parcel parcel) {
            return new Section( parcel );
        }

        @Override
        public Section[] newArray(int i) {
            return new Section[i];
        }
    };

}
