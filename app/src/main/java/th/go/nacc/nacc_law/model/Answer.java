package th.go.nacc.nacc_law.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nontachai on 7/19/15 AD.
 */
public class Answer implements Parcelable {
    private int id;
    private String answer;
    private int isRight;
    private boolean selected;

    public Answer () { }

    public Answer (Parcel in ) {
        id = in.readInt();
        answer = in.readString();
        isRight = in.readInt();
        selected = in.readByte() == 1;
    }

    public static Answer convertToObject ( JSONObject data ) {
        if ( data == null ) return null;

        Answer answer = new Answer();

        answer.id = data.optInt( "id" );
        answer.answer = data.optString("answer");
        answer.isRight = data.optInt( "is_right" );

        return answer;
    }

    public static List<Answer> convertToArray ( JSONArray datas ) {
        if ( datas == null ) return new ArrayList<Answer>();

        List<Answer> answers = new ArrayList<Answer>();

        for ( int i = 0, max = datas.length(); i < max; i++ ) {
            Answer answer = convertToObject( datas.optJSONObject( i ));

            if ( answer == null ) continue;;

            answers.add( answer );
        }
        return answers;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public int getIsRight() {
        return isRight;
    }

    public void setIsRight(int isRight) {
        this.isRight = isRight;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean isSelected) {
        this.selected = isSelected;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt( id );
        parcel.writeString( answer );
        parcel.writeInt(isRight);
        parcel.writeByte( (byte) (selected? 1: 0));
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator<Answer>() {
        @Override
        public Answer createFromParcel(Parcel parcel) {
            return new Answer(parcel);
        }

        @Override
        public Answer[] newArray(int size) {
            return new Answer[size];
        }
    };
}
