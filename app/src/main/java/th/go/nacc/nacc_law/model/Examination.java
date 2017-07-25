package th.go.nacc.nacc_law.model;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import th.go.nacc.nacc_law.AbstractActivity;
import th.go.nacc.nacc_law.R;
import th.go.nacc.nacc_law.application.MyApplication;

/**
 * Created by nontachai on 7/19/15 AD.
 */
public class Examination implements Parcelable {

    private int examinationLevel;
    private String examinationLevelName;
    private String examinationLevelDetail;
    private int section;
    private int member;
    private int totalQuestion;
    private int correctAnswer = 0;
    private int wrongAnswer = 0;
    private int currentQuestion = 1;
    private String start;
    private List<Question> questions;

    public Examination() {
    }

    public Examination(Parcel in) {
        examinationLevel = in.readInt();
        examinationLevelName = in.readString();
        examinationLevelDetail = in.readString();
        section = in.readInt();
        member = in.readInt();
        totalQuestion = in.readInt();
        correctAnswer = in.readInt();
        wrongAnswer = in.readInt();
        currentQuestion = in.readInt();
        start = in.readString();
        questions = in.readArrayList(Question.class.getClassLoader());
    }

    public static Examination convertToObject(int memberID, int sectionID, ExaminationLevelList levelList, JSONArray questions) {
        if (questions == null) return null;

        Examination examination = new Examination();
        examination.setMember(memberID);
        examination.setSection(sectionID);
        examination.setExaminationLevel(levelList.getId());
        examination.setExaminationLevelName(levelList.getName());
        examination.setExaminationLevelDetail(levelList.getDetail());
        examination.setTotalQuestion(questions.length());
        examination.setQuestions(Question.convertToArray(questions));


        return examination;
    }

    public int getExaminationLevel() {
        return examinationLevel;
    }

    public void setExaminationLevel(int examinationLevel) {
        this.examinationLevel = examinationLevel;
    }

    public String getExaminationLevelName() {
        return examinationLevelName;
    }

    public void setExaminationLevelName(String examinationLevelName) {
        this.examinationLevelName = examinationLevelName;
    }

    public String getExaminationLevelDetail() {
        return examinationLevelDetail;
    }

    public void setExaminationLevelDetail(String examinationLevelDetail) {
        this.examinationLevelDetail = examinationLevelDetail;
    }

    public int getSection() {
        return section;
    }

    public void setSection(int section) {
        this.section = section;
    }

    public int getMember() {
        return member;
    }

    public void setMember(int member) {
        this.member = member;
    }

    public int getTotalQuestion() {
        return totalQuestion;
    }

    public void setTotalQuestion(int totalQuestion) {
        this.totalQuestion = totalQuestion;
    }

    public int getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(int correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public int getWrongAnswer() {
        return wrongAnswer;
    }

    public void setWrongAnswer(int wrongAnswer) {
        this.wrongAnswer = wrongAnswer;
    }

    public int getCurrentQuestion() {
        return currentQuestion;
    }

    public void setCurrentQuestion(int currentQuestion) {
        this.currentQuestion = currentQuestion;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public static void requestCertificate(final Context context, final int historyID, final String email, final OnRequestCertificateListener listerner) {
        MyApplication myApplication = (MyApplication) context.getApplicationContext();

        String url = String.format(
                "%s/requestCertificate/%s",
                MyApplication.getInstance().API_HOSTNAME,
                MyApplication.getInstance().API_KEY
        );

        //Log.i("EXAMINATION_TAG", "URL: " + url);

        ((AbstractActivity) context).showProgress(true);

        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String s) {
                        Log.i("REQUEST_CERTIFICATE", "RESPONSE: " + s);

                        // hide progress
                        ((AbstractActivity) context).showProgress(false);

                        try {
                            JSONObject responseObject = new JSONObject(s);

                            if (responseObject.optInt("status") == 0) {
                                JSONObject error = responseObject.optJSONObject("error");

                                Error serverError = new Error(error.optString("detail"));

                                listerner.requestResult(false, serverError);

                                return;
                            } else {

                                boolean result = responseObject.optBoolean("data");

                                listerner.requestResult(result, null);
                                return;
                            }
                        } catch (JSONException e) {
                            Error parseError = new Error(
                                    context.getResources().getString(R.string.error_json_parse)
                            );

                            listerner.requestResult(false, parseError);

                            return;
                        }

                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // hide progress
                        ((AbstractActivity) context).showProgress(false);

                        Error networkError = null;

                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            networkError = new Error(context.getString(R.string.error_network_timeout));
                        } else if (error instanceof AuthFailureError) {
                            networkError = new Error(context.getString(R.string.error_auth_failure));
                        } else if (error instanceof ServerError) {
                            networkError = new Error(context.getString(R.string.error_server));
                        } else if (error instanceof NetworkError) {
                            networkError = new Error(context.getString(R.string.error_network));
                        } else if (error instanceof ParseError) {
                            networkError = new Error(context.getString(R.string.error_json_parse));
                        }

                        if (networkError != null) {
                            listerner.requestResult(false, networkError);
                        }

                        return;
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("history_id", String.valueOf(historyID));
                params.put("email", email);

                return params;
            }
        };

        MyApplication.getInstance().addToRequestQueue(request);

    }

    public static void send(final Context context, final Examination examination, final OnSendExaminationListener listerner) {
        MyApplication myApplication = (MyApplication) context.getApplicationContext();

        String url = String.format(
                "%s/sendExamination/%s",
                MyApplication.getInstance().API_HOSTNAME,
                MyApplication.getInstance().API_KEY
        );

        //Log.i("EXAMINATION_TAG", "URL: " + url);

        ((AbstractActivity) context).showProgress(true);

        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String s) {
                        Log.i("SEND_EXAM", "RESPONSE: " + s);

                        // hide progress
                        ((AbstractActivity) context).showProgress(false);

                        try {
                            JSONObject responseObject = new JSONObject(s);

                            if (responseObject.optInt("status") == 0) {
                                JSONObject error = responseObject.optJSONObject("error");

                                Error serverError = new Error(error.optString("detail"));

                                listerner.sendResult(null, -1, null, serverError);

                                return;
                            } else {

                                JSONObject data = responseObject.optJSONObject("data");

                                JSONObject history = data.optJSONObject("history");

                                String certificatePath = data.optString("certificate");

                                listerner.sendResult(examination, history.optInt("id"), certificatePath, null);
                                return;
                            }
                        } catch (JSONException e) {
                            Error parseError = new Error(
                                    context.getResources().getString(R.string.error_json_parse)
                            );

                            listerner.sendResult(null, -1, null, parseError);

                            return;
                        }

                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // hide progress
                        ((AbstractActivity) context).showProgress(false);

                        Error networkError = null;

                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            networkError = new Error(context.getString(R.string.error_network_timeout));
                        } else if (error instanceof AuthFailureError) {
                            networkError = new Error(context.getString(R.string.error_auth_failure));
                        } else if (error instanceof ServerError) {
                            networkError = new Error(context.getString(R.string.error_server));
                        } else if (error instanceof NetworkError) {
                            networkError = new Error(context.getString(R.string.error_network));
                        } else if (error instanceof ParseError) {
                            networkError = new Error(context.getString(R.string.error_json_parse));
                        }

                        if (networkError != null) {
                            listerner.sendResult(null, -1, null, networkError);
                        }

                        return;
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("member_id", String.valueOf(examination.getMember()));
                params.put("examination_level", String.valueOf(examination.getExaminationLevel()));
                params.put("total_question", String.valueOf(examination.getTotalQuestion()));
                params.put("correct_answer", String.valueOf(examination.getCorrectAnswer()));
                params.put("wrong_answer", String.valueOf(examination.getWrongAnswer()));
//                params.put( "correct_answer", String.valueOf(15));
//                params.put( "wrong_answer", String.valueOf(0));
                for (int i = 0, max = examination.getQuestions().size(); i < max; i++) {
                    Question question = examination.getQuestions().get(i);

                    params.put("question[" + String.valueOf(i) + "]", String.valueOf(question.getId()));
                    params.put("answer[" + String.valueOf(i) + "]", String.valueOf(question.getMemberAnswerID()));
                }

                return params;
            }
        };

        MyApplication.getInstance().addToRequestQueue(request);

    }

    public static void get(final Context context, final int memberID, final int sectionID, final ExaminationLevelList levelList, final OnGetExaminationListener listerner) {
        MyApplication myApplication = (MyApplication) context.getApplicationContext();

        String url = String.format(
                "%s/getSectionQuestionair/%s",
                MyApplication.getInstance().API_HOSTNAME,
                MyApplication.getInstance().API_KEY
        );

        //Log.i("EXAMINATION_TAG", "URL: " + url);

        ((AbstractActivity) context).showProgress(true);

        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String s) {
                        // hide progress
                        ((AbstractActivity) context).showProgress(false);

                        try {
                            JSONObject responseObject = new JSONObject(s);

                            if (responseObject.optInt("status") == 0) {
                                JSONObject error = responseObject.optJSONObject("error");

                                Error serverError = new Error(error.optString("detail"));

                                listerner.examinationResult(null, serverError);

                                return;
                            } else {

                                JSONArray datas = responseObject.optJSONArray("data");
                                Log.i("aaa", datas.toString());

                                Examination examination = Examination.convertToObject(memberID, sectionID, levelList, datas);

                                listerner.examinationResult(examination, null);
                                /*
                                Initial initial = Initial.convertToObject( data );

                                listerner.examinationResult( examination, null );
                                */
                                return;
                            }
                        } catch (JSONException e) {
                            Error parseError = new Error(
                                    context.getResources().getString(R.string.error_json_parse)
                            );

                            listerner.examinationResult(null, parseError);

                            return;
                        }

                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // hide progress
                        ((AbstractActivity) context).showProgress(false);

                        Error networkError = null;

                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            networkError = new Error(context.getString(R.string.error_network_timeout));
                        } else if (error instanceof AuthFailureError) {
                            networkError = new Error(context.getString(R.string.error_auth_failure));
                        } else if (error instanceof ServerError) {
                            networkError = new Error(context.getString(R.string.error_server));
                        } else if (error instanceof NetworkError) {
                            networkError = new Error(context.getString(R.string.error_network));
                        } else if (error instanceof ParseError) {
                            networkError = new Error(context.getString(R.string.error_json_parse));
                        }

                        if (networkError != null) {
                            listerner.examinationResult(null, networkError);
                        }

                        return;
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("section_id", String.valueOf(sectionID));
                params.put("level_id", String.valueOf(levelList.getId()));

                return params;
            }
        };

        MyApplication.getInstance().addToRequestQueue(request);

    }

    public static void getCertificate(final Context context, final int memberID, final int levelID, final OnGetCertificateListener listerner) {
        MyApplication myApplication = (MyApplication) context.getApplicationContext();

        String url = String.format(
                "%s/getCertificateByHistory/%s",
                MyApplication.getInstance().API_HOSTNAME,
                MyApplication.getInstance().API_KEY
        );

        //Log.i("EXAMINATION_TAG", "URL: " + url);

        ((AbstractActivity) context).showProgress(true);

        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String s) {

                        // hide progress
                        ((AbstractActivity) context).showProgress(false);

                        try {
                            JSONObject responseObject = new JSONObject(s);

                            if (responseObject.optInt("status") == 0) {
                                JSONObject error = responseObject.optJSONObject("error");

                                Error serverError = new Error(error.optString("detail"));

                                listerner.certificateResult(0, null, serverError);

                                return;
                            } else {

                                JSONObject data = responseObject.optJSONObject("data");

                                if (data != null) {
                                    listerner.certificateResult(data.optInt("id"), data.optString("certificate"), null);
                                } else {
                                    listerner.certificateResult(0, null, null);
                                }
                                return;
                            }
                        } catch (JSONException e) {
                            Error parseError = new Error(
                                    context.getResources().getString(R.string.error_json_parse)
                            );

                            listerner.certificateResult(0, null, parseError);

                            return;
                        }

                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // hide progress
                        ((AbstractActivity) context).showProgress(false);

                        Error networkError = null;

                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            networkError = new Error(context.getString(R.string.error_network_timeout));
                        } else if (error instanceof AuthFailureError) {
                            networkError = new Error(context.getString(R.string.error_auth_failure));
                        } else if (error instanceof ServerError) {
                            networkError = new Error(context.getString(R.string.error_server));
                        } else if (error instanceof NetworkError) {
                            networkError = new Error(context.getString(R.string.error_network));
                        } else if (error instanceof ParseError) {
                            networkError = new Error(context.getString(R.string.error_json_parse));
                        }

                        if (networkError != null) {
                            listerner.certificateResult(0, null, networkError);
                        }

                        return;
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("member_id", String.valueOf(memberID));
                params.put("level_id", String.valueOf(levelID));

                return params;
            }
        };

        MyApplication.getInstance().addToRequestQueue(request);

    }


    public interface OnGetExaminationListener {
        public void examinationResult(Examination examination, Error error);
    }

    public interface OnSendExaminationListener {
        public void sendResult(Examination examination, int historyID, String certificatePath, Error error);
    }

    public interface OnGetCertificateListener {
        public void certificateResult(int historyID, String certificatePath, Error error);
    }

    public interface OnRequestCertificateListener {
        public void requestResult(boolean result, Error error);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(examinationLevel);
        parcel.writeString(examinationLevelName);
        parcel.writeString(examinationLevelDetail);
        parcel.writeInt(section);
        parcel.writeInt(member);
        parcel.writeInt(totalQuestion);
        parcel.writeInt(correctAnswer);
        parcel.writeInt(wrongAnswer);
        parcel.writeInt(currentQuestion);
        parcel.writeString(start);
        parcel.writeList(questions);
        //parcel.writeTypedList(questions);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator<Examination>() {
        @Override
        public Examination createFromParcel(Parcel parcel) {
            return new Examination(parcel);
        }

        @Override
        public Examination[] newArray(int size) {
            return new Examination[size];
        }
    };
}
