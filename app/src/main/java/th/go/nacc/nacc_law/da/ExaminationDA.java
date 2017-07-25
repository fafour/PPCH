package th.go.nacc.nacc_law.da;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import th.go.nacc.nacc_law.helper.DatabaseHelper;
import th.go.nacc.nacc_law.model.Answer;
import th.go.nacc.nacc_law.model.Examination;
import th.go.nacc.nacc_law.model.Question;

/**
 * Created by nontachai on 7/19/15 AD.
 */
public class ExaminationDA {

    public static void clearExamination(Context context) {

        DatabaseHelper dbHelper = new DatabaseHelper(context);

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        try {
            db.delete(DatabaseHelper.TABLE_EXAMINATION, null, null);
            db.delete(DatabaseHelper.TABLE_QUESTION, null, null);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            db.close();
            dbHelper.close();
        }
    }

    public static boolean isExistExamination(Context context) {

        DatabaseHelper dbHelper = new DatabaseHelper(context);

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        boolean isExist = false;

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM examination", null);

            if (cursor.getCount() > 0) {
                isExist = true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            db.close();
            dbHelper.close();
        }

        return isExist;
    }

    public static Examination saveExamination(Context context, Examination examination) {

        clearExamination(context);

        DatabaseHelper dbHelper = new DatabaseHelper(context);

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.beginTransaction();

        try {
            // save to examination table
            ContentValues examValue = new ContentValues();
            examValue.put(DatabaseHelper.EXAMINATION_KEY_LEVEL, String.valueOf(examination.getExaminationLevel()));
            examValue.put(DatabaseHelper.EXAMINATION_KEY_LEVEL_NAME, examination.getExaminationLevelName());
            examValue.put(DatabaseHelper.EXAMINATION_KEY_LEVEL_DETAIL, examination.getExaminationLevelDetail());
            examValue.put(DatabaseHelper.EXAMINATION_KEY_MEMBER, String.valueOf(examination.getMember()));
            examValue.put(DatabaseHelper.EXAMINATION_KEY_SECTION, String.valueOf(examination.getSection()));
            examValue.put(DatabaseHelper.EXAMINATION_KEY_TOTAL_QUESTION, String.valueOf(examination.getTotalQuestion()));
            examValue.put(DatabaseHelper.EXAMINATION_KEY_CORRECT_ANSWER, String.valueOf(examination.getCorrectAnswer()));
            examValue.put(DatabaseHelper.EXAMINATION_KEY_WRONG_ANSWER, String.valueOf(examination.getWrongAnswer()));
            examValue.put(DatabaseHelper.EXAMINATION_KEY_CURRENT_QUESTION, String.valueOf(examination.getCurrentQuestion()));
            examValue.put(DatabaseHelper.EXAMINATION_KEY_START, examination.getStart());

            db.insert(DatabaseHelper.TABLE_EXAMINATION, null, examValue);

            for (int i = 0, maxQuestion = examination.getQuestions().size(); i < maxQuestion; i++) {
                Question question = examination.getQuestions().get(i);

                // looping to save question
                ContentValues questionValues = new ContentValues();
                questionValues.put(DatabaseHelper.QUESTION_KEY_QID, question.getId());
                questionValues.put(DatabaseHelper.QUESTION_KEY_SUBJECT, question.getSubject());

                questionValues.put(DatabaseHelper.QUESTION_KEY_IMAGE, question.getImage());
                questionValues.put(DatabaseHelper.QUESTION_KEY_AUDIO, question.getAudio());
                questionValues.put(DatabaseHelper.QUESTION_KEY_CLIP, question.getClip());

                if (question.getAnswer1() != null) {
                    questionValues.put(DatabaseHelper.QUESTION_KEY_FIRST_ANSWER, question.getAnswer1().getAnswer());
                    questionValues.put(DatabaseHelper.QUESTION_KEY_FIRST_ANSWERID, String.valueOf(question.getAnswer1().getId()));
                }

                if (question.getAnswer2() != null) {
                    questionValues.put(DatabaseHelper.QUESTION_KEY_SECOND_ANSWER, question.getAnswer2().getAnswer());
                    questionValues.put(DatabaseHelper.QUESTION_KEY_SECOND_ANSWERID, String.valueOf(question.getAnswer2().getId()));
                }

                if (question.getAnswer3() != null) {
                    questionValues.put(DatabaseHelper.QUESTION_KEY_THIRD_ANSWER, question.getAnswer3().getAnswer());
                    questionValues.put(DatabaseHelper.QUESTION_KEY_THIRD_ANSWERID, String.valueOf(question.getAnswer3().getId()));
                }

                if (question.getAnswer4() != null) {
                    questionValues.put(DatabaseHelper.QUESTION_KEY_FOURTH_ANSWER, question.getAnswer4().getAnswer());
                    questionValues.put(DatabaseHelper.QUESTION_KEY_FOURTH_ANSWERID, String.valueOf(question.getAnswer4().getId()));
                }

                questionValues.put(DatabaseHelper.QUESTION_KEY_MEMBER_ANSWERID, String.valueOf(question.getMemberAnswerID()));
                questionValues.put(DatabaseHelper.QUESTION_KEY_CORRECT_ANSWERID, String.valueOf(question.getCorrectAnswerID()));
                questionValues.put(DatabaseHelper.QUESTION_KEY_EXPLAIN, question.getExplain());

                db.insert(DatabaseHelper.TABLE_QUESTION, null, questionValues);
            }

            db.setTransactionSuccessful();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
            dbHelper.close();
        }

        return getExamination(context);
    }

    public static int countCorrectAnswer(Context context) {
        DatabaseHelper dbHelper = new DatabaseHelper(context);

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        int total = 0;

        try {
            Cursor cursor = db.rawQuery("SELECT COUNT(*) as total FROM question WHERE " +
                    DatabaseHelper.QUESTION_KEY_MEMBER_ANSWERID +
                    " = " + DatabaseHelper.QUESTION_KEY_CORRECT_ANSWERID, null);


            if (cursor.getCount() > 0) {
                cursor.moveToFirst();

                total = cursor.getInt(0);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            db.close();
            dbHelper.close();
        }

        return total;
    }

    public static int countWrongAnswer(Context context) {
        DatabaseHelper dbHelper = new DatabaseHelper(context);

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        int total = 0;

        try {
            Cursor cursor = db.rawQuery("SELECT COUNT(*) as total FROM question WHERE " +
                    DatabaseHelper.QUESTION_KEY_MEMBER_ANSWERID +
                    " != " + DatabaseHelper.QUESTION_KEY_CORRECT_ANSWERID, null);


            if (cursor.getCount() > 0) {
                cursor.moveToFirst();

                total = cursor.getInt(0);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            db.close();
            dbHelper.close();
        }

        return total;
    }

    public static void start(Context context) {
        DatabaseHelper dbHelper = new DatabaseHelper(context);

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        try {
            // save to examination table
            ContentValues examValue = new ContentValues();
            examValue.put(DatabaseHelper.EXAMINATION_KEY_CURRENT_QUESTION, String.valueOf(1));

            db.update(DatabaseHelper.TABLE_EXAMINATION, examValue, null, null);

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            db.close();
            dbHelper.close();
        }
    }

    public static Examination getExamination(Context context) {

        DatabaseHelper dbHelper = new DatabaseHelper(context);

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Examination examination = null;

        try {
            String queryExamination = "SELECT * FROM " + DatabaseHelper.TABLE_EXAMINATION;

            Cursor cursor = db.rawQuery(queryExamination, null);

            if (cursor.getCount() > 0) {
                cursor.moveToFirst();

                examination = new Examination();

                examination.setExaminationLevel(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.EXAMINATION_KEY_LEVEL)));
                examination.setExaminationLevelName(cursor.getString(cursor.getColumnIndex(DatabaseHelper.EXAMINATION_KEY_LEVEL_NAME)));
                examination.setExaminationLevelDetail(cursor.getString(cursor.getColumnIndex(DatabaseHelper.EXAMINATION_KEY_LEVEL_DETAIL)));
                examination.setMember(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.EXAMINATION_KEY_MEMBER)));
                examination.setSection(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.EXAMINATION_KEY_SECTION)));
                examination.setStart(cursor.getString(cursor.getColumnIndex(DatabaseHelper.EXAMINATION_KEY_START)));
                examination.setTotalQuestion(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.EXAMINATION_KEY_TOTAL_QUESTION)));
                examination.setCorrectAnswer(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.EXAMINATION_KEY_CORRECT_ANSWER)));
                examination.setWrongAnswer(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.EXAMINATION_KEY_WRONG_ANSWER)));
                examination.setCurrentQuestion(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.EXAMINATION_KEY_CURRENT_QUESTION)));

                cursor.close();

                String queryQuestion = "SELECT * FROM " + DatabaseHelper.TABLE_QUESTION;

                Cursor questionCursor = db.rawQuery(queryQuestion, null);

                if (questionCursor.getCount() > 0) {
                    questionCursor.moveToFirst();

                    List<Question> questions = new ArrayList<Question>();

                    do {
                        Question question = new Question();

                        question.setId(questionCursor.getInt(questionCursor.getColumnIndex(DatabaseHelper.QUESTION_KEY_QID)));
                        question.setSubject(questionCursor.getString(questionCursor.getColumnIndex(DatabaseHelper.QUESTION_KEY_SUBJECT)));
                        question.setMemberAnswerID(questionCursor.getInt(questionCursor.getColumnIndex(DatabaseHelper.QUESTION_KEY_MEMBER_ANSWERID)));
                        question.setCorrectAnswerID(questionCursor.getInt(questionCursor.getColumnIndex(DatabaseHelper.QUESTION_KEY_CORRECT_ANSWERID)));
                        question.setAudio(questionCursor.getString(questionCursor.getColumnIndex(DatabaseHelper.QUESTION_KEY_AUDIO)));
                        question.setImage(questionCursor.getString(questionCursor.getColumnIndex(DatabaseHelper.QUESTION_KEY_IMAGE)));
                        question.setClip(questionCursor.getString(questionCursor.getColumnIndex(DatabaseHelper.QUESTION_KEY_CLIP)));

                        Answer answer1 = new Answer();
                        Answer answer2 = new Answer();
                        Answer answer3 = new Answer();
                        Answer answer4 = new Answer();

                        answer1.setId(questionCursor.getInt(questionCursor.getColumnIndex(DatabaseHelper.QUESTION_KEY_FIRST_ANSWERID)));
                        answer1.setAnswer(questionCursor.getString(questionCursor.getColumnIndex(DatabaseHelper.QUESTION_KEY_FIRST_ANSWER)));
                        answer1.setIsRight(answer1.getId() == question.getCorrectAnswerID() ? 1 : 0);

                        answer2.setId(questionCursor.getInt(questionCursor.getColumnIndex(DatabaseHelper.QUESTION_KEY_SECOND_ANSWERID)));
                        answer2.setAnswer(questionCursor.getString(questionCursor.getColumnIndex(DatabaseHelper.QUESTION_KEY_SECOND_ANSWER)));
                        answer2.setIsRight(answer2.getId() == question.getCorrectAnswerID() ? 1 : 0);

                        answer3.setId(questionCursor.getInt(questionCursor.getColumnIndex(DatabaseHelper.QUESTION_KEY_THIRD_ANSWERID)));
                        answer3.setAnswer(questionCursor.getString(questionCursor.getColumnIndex(DatabaseHelper.QUESTION_KEY_THIRD_ANSWER)));
                        answer3.setIsRight(answer3.getId() == question.getCorrectAnswerID() ? 1 : 0);

                        answer4.setId(questionCursor.getInt(questionCursor.getColumnIndex(DatabaseHelper.QUESTION_KEY_FOURTH_ANSWERID)));
                        answer4.setAnswer(questionCursor.getString(questionCursor.getColumnIndex(DatabaseHelper.QUESTION_KEY_FOURTH_ANSWER)));
                        answer4.setIsRight(answer4.getId() == question.getCorrectAnswerID() ? 1 : 0);

                        question.setAnswer1(answer1);
                        question.setAnswer2(answer2);
                        question.setAnswer3(answer3);
                        question.setAnswer4(answer4);

                        questions.add(question);
                        questionCursor.moveToNext();
                    } while (!questionCursor.isAfterLast());

                    examination.setQuestions(questions);
                } else {
                    examination.setQuestions(new ArrayList<Question>());
                }

                questionCursor.close();

                //examination.setQuestions( questions );

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            db.close();
            dbHelper.close();
        }
        return examination;
    }

    public static void saveMemberAnswer(Context context, Question question) {
        DatabaseHelper dbHelper = new DatabaseHelper(context);

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        try {
            // save to examination table
            ContentValues questionValues = new ContentValues();
            questionValues.put(DatabaseHelper.QUESTION_KEY_MEMBER_ANSWERID, String.valueOf(question.getMemberAnswerID()));

            db.update(
                    DatabaseHelper.TABLE_QUESTION,
                    questionValues,
                    DatabaseHelper.QUESTION_KEY_QID + " = ?",
                    new String[]{String.valueOf(question.getId())}
            );

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            db.close();
            dbHelper.close();
        }
    }

    public static void updateSummary(Context context, Examination examination) {
        DatabaseHelper dbHelper = new DatabaseHelper(context);

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        try {
            // save to examination table
            ContentValues examValues = new ContentValues();
            examValues.put(DatabaseHelper.EXAMINATION_KEY_CURRENT_QUESTION, String.valueOf(examination.getCurrentQuestion()));
            examValues.put(DatabaseHelper.EXAMINATION_KEY_CORRECT_ANSWER, String.valueOf(examination.getCorrectAnswer()));
            examValues.put(DatabaseHelper.EXAMINATION_KEY_WRONG_ANSWER, String.valueOf(examination.getWrongAnswer()));

            db.update(
                    DatabaseHelper.TABLE_EXAMINATION,
                    examValues,
                    null, null
            );

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            db.close();
            dbHelper.close();
        }
    }

}
