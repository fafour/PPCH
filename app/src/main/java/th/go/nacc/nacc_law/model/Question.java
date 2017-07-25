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
public class Question implements Parcelable {
    private int id;
    private int section;
    private String subject;
    private int examinationLevel;
    private Answer answer1;
    private Answer answer2;
    private Answer answer3;
    private Answer answer4;
    private int memberAnswerID = -1;
    private int correctAnswerID;
    private String explain;
    private String clip;
    private String image;
    private String audio;


    public Question() {
    }

    public Question(Parcel in) {
        id = in.readInt();
        section = in.readInt();
        subject = in.readString();
        examinationLevel = in.readInt();
        answer1 = (Answer) in.readParcelable(Answer.class.getClassLoader());
        answer2 = (Answer) in.readParcelable(Answer.class.getClassLoader());
        answer3 = (Answer) in.readParcelable(Answer.class.getClassLoader());
        answer4 = (Answer) in.readParcelable(Answer.class.getClassLoader());
        memberAnswerID = in.readInt();
        correctAnswerID = in.readInt();
        explain = in.readString();
        clip = in.readString();
        image = in.readString();
        audio = in.readString();
    }

    public static Question convertToObject(JSONObject data) {
        if (data == null) return null;

        Question question = new Question();

        question.id = data.optInt("id");
        question.section = data.optInt("section");
        question.subject = data.optString("question");
        question.examinationLevel = data.optInt("examination_level");
        question.clip = data.optString("clip");
        question.image = data.optString("image");
        question.audio = data.optString("audio");

        List<Answer> answers = Answer.convertToArray(data.optJSONArray("answers"));

        for (int i = 0, maxAnswer = answers.size(); i < maxAnswer; i++) {
            Answer answer = answers.get(i);

            if (answer.getIsRight() == 1) {
                question.setCorrectAnswerID(answer.getId());
            }
        }

        if (answers.size() > 0) question.answer1 = answers.get(0);
        if (answers.size() > 1) question.answer2 = answers.get(1);
        if (answers.size() > 2) question.answer3 = answers.get(2);
        if (answers.size() > 3) question.answer4 = answers.get(3);

        if (data.isNull("explain")) {
            question.explain = "";
        } else {
            question.explain = data.optString("explain");
        }

        return question;
    }

    public static List<Question> convertToArray(JSONArray datas) {
        if (datas == null) return new ArrayList<Question>();

        List<Question> questions = new ArrayList<Question>();

        for (int i = 0, max = datas.length(); i < max; i++) {
            Question question = convertToObject(datas.optJSONObject(i));

            if (question == null) continue;

            questions.add(question);
        }

        return questions;
    }

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

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public int getExaminationLevel() {
        return examinationLevel;
    }

    public void setExaminationLevel(int examinationLevel) {
        this.examinationLevel = examinationLevel;
    }

    public Answer getAnswer1() {
        return answer1;
    }

    public void setAnswer1(Answer answer1) {
        this.answer1 = answer1;
    }

    public Answer getAnswer2() {
        return answer2;
    }

    public void setAnswer2(Answer answer2) {
        this.answer2 = answer2;
    }

    public Answer getAnswer3() {
        return answer3;
    }

    public void setAnswer3(Answer answer3) {
        this.answer3 = answer3;
    }

    public Answer getAnswer4() {
        return answer4;
    }

    public void setAnswer4(Answer answer4) {
        this.answer4 = answer4;
    }

    public int getMemberAnswerID() {
        return memberAnswerID;
    }

    public void setMemberAnswerID(int memberAnswerID) {
        this.memberAnswerID = memberAnswerID;
    }

    public int getCorrectAnswerID() {
        return correctAnswerID;
    }

    public void setCorrectAnswerID(int correctAnswerID) {
        this.correctAnswerID = correctAnswerID;
    }

    public String getExplain() {
        return explain;
    }

    public void setExplain(String explain) {
        this.explain = explain;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(id);
        parcel.writeInt(section);
        parcel.writeString(subject);
        parcel.writeInt(examinationLevel);
        parcel.writeParcelable(answer1, flags);
        parcel.writeParcelable(answer2, flags);
        parcel.writeParcelable(answer3, flags);
        parcel.writeParcelable(answer4, flags);
        parcel.writeInt(memberAnswerID);
        parcel.writeInt(correctAnswerID);
        parcel.writeString(explain);
        parcel.writeString(clip);
        parcel.writeString(image);
        parcel.writeString(audio);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator<Question>() {
        @Override
        public Question createFromParcel(Parcel parcel) {
            return new Question(parcel);
        }

        @Override
        public Question[] newArray(int size) {
            return new Question[size];
        }
    };
}
