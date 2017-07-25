package th.go.nacc.nacc_law.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by nontachai on 7/19/15 AD.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;

    private static final String DATABASE_NAME = "nacc_law";


    public static final String TABLE_EXAMINATION = "examination";
    public static final String TABLE_QUESTION = "question";

    public static final String EXAMINATION_KEY_LEVEL = "examination_level";
    public static final String EXAMINATION_KEY_SECTION = "section";
    public static final String EXAMINATION_KEY_MEMBER = "member";
    public static final String EXAMINATION_KEY_TOTAL_QUESTION = "total_question";
    public static final String EXAMINATION_KEY_CORRECT_ANSWER = "correct_answer";
    public static final String EXAMINATION_KEY_WRONG_ANSWER = "wrong_answer";
    public static final String EXAMINATION_KEY_CURRENT_QUESTION = "current_number_question";
    public static final String EXAMINATION_KEY_START = "start";
    public static final String EXAMINATION_KEY_LEVEL_NAME = "examination_level_name";
    public static final String EXAMINATION_KEY_LEVEL_DETAIL = "examination_level_detail";

    public static final String QUESTION_KEY_QID = "qid";
    public static final String QUESTION_KEY_SUBJECT = "qsubject";
    public static final String QUESTION_KEY_FIRST_ANSWER = "first_answer";
    public static final String QUESTION_KEY_FIRST_ANSWERID = "first_answerid";
    public static final String QUESTION_KEY_SECOND_ANSWER = "second_answer";
    public static final String QUESTION_KEY_SECOND_ANSWERID = "second_answerid";
    public static final String QUESTION_KEY_THIRD_ANSWER = "third_answer";
    public static final String QUESTION_KEY_THIRD_ANSWERID = "third_answerid";
    public static final String QUESTION_KEY_FOURTH_ANSWER = "fourth_answer";
    public static final String QUESTION_KEY_FOURTH_ANSWERID = "fourth_answerid";
    public static final String QUESTION_KEY_MEMBER_ANSWERID = "member_answerid";
    public static final String QUESTION_KEY_CORRECT_ANSWERID = "correct_answerid";
    public static final String QUESTION_KEY_EXPLAIN = "explain";
    public static final String QUESTION_KEY_AUDIO = "audio";
    public static final String QUESTION_KEY_CLIP = "clip";
    public static final String QUESTION_KEY_IMAGE = "image";

    public DatabaseHelper ( Context context ) {
        super ( context, DATABASE_NAME, null, DATABASE_VERSION );
    }

    @Override
    public void onCreate (SQLiteDatabase db ) {
        // initialize database
        String createExaminationTable = "CREATE TABLE " + TABLE_EXAMINATION + " ( " +
                EXAMINATION_KEY_LEVEL + " INTEGER, " +
                EXAMINATION_KEY_LEVEL_NAME + " TEXT, " +
                EXAMINATION_KEY_LEVEL_DETAIL + " TEXT, " +
                EXAMINATION_KEY_SECTION + " INTEGER, " +
                EXAMINATION_KEY_MEMBER + " INTEGER, " +
                EXAMINATION_KEY_TOTAL_QUESTION + " INTEGER, " +
                EXAMINATION_KEY_CORRECT_ANSWER + " INTEGER, " +
                EXAMINATION_KEY_WRONG_ANSWER + " INTEGER, " +
                EXAMINATION_KEY_CURRENT_QUESTION + " INTEGER, " +
                EXAMINATION_KEY_START + " TEXT" +
                ")";

        db.execSQL( createExaminationTable );


        String createQuestionTable = "CREATE TABLE " + TABLE_QUESTION + " ( " +
                QUESTION_KEY_QID + " INTEGER, " +
                QUESTION_KEY_SUBJECT + " TEXT, " +
                QUESTION_KEY_FIRST_ANSWER + " TEXT, " +
                QUESTION_KEY_FIRST_ANSWERID + " INTEGER, " +
                QUESTION_KEY_SECOND_ANSWER + " TEXT, " +
                QUESTION_KEY_SECOND_ANSWERID + " INTEGER, " +
                QUESTION_KEY_THIRD_ANSWER + " TEXT, " +
                QUESTION_KEY_THIRD_ANSWERID + " INTEGER, " +
                QUESTION_KEY_FOURTH_ANSWER + " TEXT, " +
                QUESTION_KEY_FOURTH_ANSWERID + " INTEGER, " +
                QUESTION_KEY_MEMBER_ANSWERID + " INTEGER, " +
                QUESTION_KEY_CORRECT_ANSWERID + " INTEGER, " +
                QUESTION_KEY_EXPLAIN + " TEXT, " +
                QUESTION_KEY_AUDIO + " TEXT, " +
                QUESTION_KEY_IMAGE + " TEXT, " +
                QUESTION_KEY_CLIP + " TEXT " +
                ")";

        db.execSQL( createQuestionTable );
    }

    @Override
    public void onUpgrade ( SQLiteDatabase db, int oldVersion, int newVersion ) {
        // if upgrade version
    }
}
