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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import th.go.nacc.nacc_law.AbstractActivity;
import th.go.nacc.nacc_law.R;
import th.go.nacc.nacc_law.application.MyApplication;

/**
 * Created by nontachai on 7/27/15 AD.
 */
public class News implements Parcelable {
    public int id;
    public String title;
    public String detail;
    public String link;
    public String startDate;
    public String endDate;

    public String clip;
    public String audio;


    private String image;

    public String getClip() {
        return clip;
    }

    public void setClip(String clip) {
        this.clip = clip;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public News() {
    }

    public News(Parcel in) {
        id = in.readInt();
        title = in.readString();
        detail = in.readString();
        link = in.readString();
        startDate = in.readString();
        endDate = in.readString();
        image = in.readString();
        clip = in.readString();
        audio = in.readString();
    }

    public static News convertToObject(JSONObject data) {
        if (data == null) return null;

        News news = new News();

        news.id = data.optInt("id");
        news.title = data.optString("title");
        news.detail = data.optString("detail");
        news.link = data.optString("link");
        news.startDate = data.optString("startDate");
        news.endDate = data.optString("endDate");
        news.image = data.optString("image");
        news.audio = data.optString("audio");
        news.clip = data.optString("clip");
        return news;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(title);
        parcel.writeString(detail);
        parcel.writeString(link);
        parcel.writeString(startDate);
        parcel.writeString(endDate);
        parcel.writeString(image);
        parcel.writeString(audio);
        parcel.writeString(clip);
    }

    public static void getCurrentNews(final Context context, final OnGetCurrentNewsListener listerner) {
        MyApplication myApplication = (MyApplication) context.getApplicationContext();

        String url = String.format(
                "%s/getCurrentNews/%s",
                MyApplication.getInstance().API_HOSTNAME,
                MyApplication.getInstance().API_KEY
        );

        //((AbstractActivity) context).showProgress(true);


        StringRequest request = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String s) {

                        //((AbstractActivity) context).showProgress(false);

                        try {
                            JSONObject responseObject = new JSONObject(s);

                            if (responseObject.optInt("status") == 0) {
                                JSONObject error = responseObject.optJSONObject("error");

                                Error serverError = new Error(error.optString("detail"));

                                listerner.onResult(null, serverError);

                                return;
                            } else {

                                if (responseObject.isNull("data")) {
                                    listerner.onResult(null, null);
                                } else {
                                    News currentNews = News.convertToObject(responseObject.optJSONObject("data"));

                                    listerner.onResult(currentNews, null);
                                }
                                return;
                            }
                        } catch (JSONException e) {
                            Error parseError = new Error(
                                    context.getResources().getString(R.string.error_json_parse)
                            );

                            listerner.onResult(null, parseError);

                            return;
                        }

                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // hide progress
                        //((AbstractActivity) context).showProgress(false);

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
                            listerner.onResult(null, networkError);
                        }

                        return;
                    }
                }
        );

        MyApplication.getInstance().addToRequestQueue(request);

    }

    public static void getNewsDetail(final Context context, final int newsID, final OnGetDetailListener listerner) {
        MyApplication myApplication = (MyApplication) context.getApplicationContext();

        String url = String.format(
                "%s/getNewsDetail/%s",
                MyApplication.getInstance().API_HOSTNAME,
                MyApplication.getInstance().API_KEY
        );

        ((AbstractActivity) context).showProgress(true);


        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String s) {

                        Log.i("TAG", "GET DETAIL: " + s);

                        ((AbstractActivity) context).showProgress(false);

                        try {
                            JSONObject responseObject = new JSONObject(s);

                            if (responseObject.optInt("status") == 0) {
                                JSONObject error = responseObject.optJSONObject("error");

                                Error serverError = new Error(error.optString("detail"));

                                listerner.onResult(null, serverError);

                                return;
                            } else {

                                if (responseObject.isNull("data")) {
                                    listerner.onResult(null, null);
                                } else {
                                    News news = convertToObject(responseObject.getJSONArray("data").getJSONObject(0));

                                    listerner.onResult(news, null);
                                }
                                return;
                            }
                        } catch (JSONException e) {
                            Error parseError = new Error(
                                    context.getResources().getString(R.string.error_json_parse)
                            );

                            listerner.onResult(null, parseError);

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
                            listerner.onResult(null, networkError);
                        }

                        return;
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("news_id", String.valueOf(newsID));
                return params;
            }
        };

        MyApplication.getInstance().addToRequestQueue(request);

    }

    public interface OnGetCurrentNewsListener {
        public void onResult(News news, Error error);
    }

    public interface OnGetDetailListener {
        public void onResult(News news, Error error);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator<News>() {
        @Override
        public News createFromParcel(Parcel parcel) {
            return new News(parcel);
        }

        @Override
        public News[] newArray(int i) {
            return new News[i];
        }
    };
}
