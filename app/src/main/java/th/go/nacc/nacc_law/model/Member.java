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
 * Created by nontachai on 7/19/15 AD.
 */
public class Member implements Parcelable {
    private int id;
    private String name;
    private String lastname;
    private String idcard;
    private int isDLAMember;
    private int subordinate;
    private int subordinateProvince;
    private int subordinateItem;
    private String subordinateOther;
    private int amphur;
    private int isSpouse;

    private int amphurId;


    public String getOrgid() {
        return orgid;
    }

    public void setOrgid(String orgid) {
        this.orgid = orgid;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    private int jobTitle;
    private String subname;
    private String orgid;
    private String orgName;

    private String thumbonName;
    private String amphurName;

    public String getThumbonName() {
        return thumbonName;
    }

    public void setThumbonName(String thumbonName) {
        this.thumbonName = thumbonName;
    }

    public String getAmphurName() {
        return amphurName;
    }

    public void setAmphurName(String amphurName) {
        this.amphurName = amphurName;
    }

    public String getSubname() {
        return subname;
    }

    public void setSubname(String subname) {
        this.subname = subname;
    }

    public String getMinistryName() {
        return ministryName;
    }

    public void setMinistryName(String ministryName) {
        this.ministryName = ministryName;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getJobtitleName() {
        return jobtitleName;
    }

    public void setJobtitleName(String jobtitleName) {
        this.jobtitleName = jobtitleName;
    }

    public String getMuangName() {
        return muangName;
    }

    public void setMuangName(String muangName) {
        this.muangName = muangName;
    }

    private String ministryName;
    private String provinceName;
    private String jobtitleName;
    private String muangName;


    public String getMinistryID() {
        return ministryID;
    }

    public void setMinistryID(String ministryID) {
        this.ministryID = ministryID;
    }

    private String position;
    private String ministryID;

    public Member() {
    }

    public Member(Parcel in) {
        id = in.readInt();
        name = in.readString();
        lastname = in.readString();
        idcard = in.readString();
        isDLAMember = in.readInt();
        subordinate = in.readInt();
        subordinateProvince = in.readInt();
        subordinateItem = in.readInt();
        amphurId = in.readInt();
        subordinateOther = in.readString();
        amphur = in.readInt();
        isSpouse = in.readInt();
        jobTitle = in.readInt();
        position = in.readString();
        ministryID = in.readString();
        subname = in.readString();
        ministryName = in.readString();
        provinceName = in.readString();
        jobtitleName = in.readString();
        muangName = in.readString();
        orgid = in.readString();
        orgName = in.readString();
        amphurName = in.readString();
        thumbonName = in.readString();
    }

    public static Member convertToObject(JSONObject data)  {
        if (data == null) return null;

        Member member = new Member();

        member.id = data.optInt("id");
        member.name = data.optString("name");
        member.lastname = data.optString("lastname");
        member.idcard = data.optString("idcard");
        member.isDLAMember = data.optInt("is_dla_member");
        member.subordinate = data.optInt("subordinate");
        member.subordinateProvince = data.optInt("subordinate_province");
        member.subordinateItem = data.optInt("subordinate_item");
        member.amphurId = data.optInt("amphur");
        member.subordinateOther = data.optString("subordinate_other").replace("null","");
        member.ministryID = data.optString("ministry_id");
        if (!data.isNull("amphur")) {
            member.amphur = data.optInt("amphur");
        }

        member.isSpouse = data.optInt("is_spouse");
        member.jobTitle = data.optInt("job_title");
        member.position = data.optString("position").replace("null","");

        member.subname = data.optString("subname");
        member.ministryName = data.optString("ministryname").replace("null","");
        member.provinceName = data.optString("provinceName").replace("null","");
        member.jobtitleName = data.optString("jobtitleName").replace("null","");
        member.muangName = data.optString("muangName").replace("null","");
        member.orgName = data.optString("orgName").replace("null","");
        member.orgid = data.optString("orgid");

        member.amphurName = data.optString("amphurName").replace("null","");
        member.thumbonName = data.optString("thumbonName").replace("null","");


        if (!member.subordinateOther.isEmpty()) {
            member.orgName = "อื่นๆ";
            member.subname = data.optString("subordinate_other");
        }


//        //----------------------------------------------------------------------------------------------------------------
//        try {
//            member.id = data.getInt("id");
//            member.name = data.getString("name");
//            member.lastname = data.getString("lastname");
//            member.idcard = data.getString("idcard");
//            member.isDLAMember = data.getInt("is_dla_member");
//            member.subordinate = data.getInt("subordinate");
//            member.subordinateProvince = data.getInt("subordinate_province");
//            member.subordinateItem = data.getInt("subordinate_item");
//            member.amphurId = data.getInt("amphurId");
//            member.subordinateOther = data.getString("subordinate_other").replace("null","");
//            member.ministryID = data.getString("ministry_id");
//            if (!data.isNull("amphur")) {
//                member.amphur = data.getInt("amphur");
//            }
//
//
//            member.isSpouse = data.getInt("is_spouse");
//            member.jobTitle = data.getInt("job_title");
//            member.position = data.getString("position").replace("null","");
//
//            member.subname = data.getString("subname");
//            member.ministryName = data.getString("ministryName").replace("null","");
//            member.provinceName = data.getString("provinceName").replace("null","");
//            member.jobtitleName = data.getString("jobtitleName").replace("null","");
//            member.muangName = data.getString("muangName").replace("null","");
//            member.orgName = data.getString("orgName").replace("null","");
//            member.orgid = data.getString("orgid");
//
//            member.amphurName = data.getString("amphurName").replace("null","");
//            member.thumbonName = data.getString("thumbonName").replace("null","");
//
//
//            if (!member.subordinateOther.isEmpty()) {
//                member.orgName = "อื่นๆ";
//                member.subname = data.getString("subordinate_other");
//            }
//
//        }catch (JSONException e){
//
//        }


        return member;
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

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public int getIsDLAMember() {
        return isDLAMember;
    }

    public void setIsDLAMember(int isDLAMember) {
        this.isDLAMember = isDLAMember;
    }

    public int getSubordinate() {
        return subordinate;
    }

    public void setSubordinate(int subordinate) {
        this.subordinate = subordinate;
    }

    public int getSubordinateProvince() {
        return subordinateProvince;
    }

    public void setSubordinateProvince(int subordinateProvince) {
        this.subordinateProvince = subordinateProvince;
    }

    public int getSubordinateItem() {
        return subordinateItem;
    }

    public void setSubordinateItem(int subordinateItem) {
        this.subordinateItem = subordinateItem;
    }

    public String getSubordinateOther() {
        return subordinateOther;
    }

    public void setSubordinateOther(String subordinateOther) {
        this.subordinateOther = subordinateOther;
    }

    public int getAmphurId() {
        return amphurId;
    }

    public void setAmphurId(int amphurId) {
        this.amphurId = amphurId;
    }


    public int getAmphur() {
        return amphur;
    }

    public void setAmphur(int amphur) {
        this.amphur = amphur;
    }

    public int getIsSpouse() {
        return isSpouse;
    }

    public void setIsSpouse(int isSpouse) {
        this.isSpouse = isSpouse;
    }

    public int getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(int jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeString(lastname);
        parcel.writeString(idcard);
        parcel.writeInt(isDLAMember);
        parcel.writeInt(subordinate);
        parcel.writeInt(subordinateProvince);
        parcel.writeInt(subordinateItem);
        parcel.writeInt(amphurId);
        parcel.writeString(subordinateOther);
        parcel.writeInt(amphur);
        parcel.writeInt(isSpouse);
        parcel.writeInt(jobTitle);
        parcel.writeString(position);
        parcel.writeString(ministryID);
        parcel.writeString(subname);
        parcel.writeString(ministryName);
        parcel.writeString(provinceName);
        parcel.writeString(jobtitleName);
        parcel.writeString(muangName);
        parcel.writeString(orgid);
        parcel.writeString(orgName);
        parcel.writeString(thumbonName);
        parcel.writeString(amphurName);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator<Member>() {
        @Override
        public Member createFromParcel(Parcel parcel) {
            return new Member(parcel);
        }

        @Override
        public Member[] newArray(int size) {
            return new Member[size];
        }
    };


    public static void register(final Context context, final Member member, final OnRegisterListener listener) {
        MyApplication myApplication = (MyApplication) context.getApplicationContext();

        String url = String.format(
                "%s/register/%s",
                MyApplication.getInstance().API_HOSTNAME,
                MyApplication.getInstance().API_KEY
        );

        Log.i("REGISTER_TAG", "URL: " + url);


        // show progress
        ((AbstractActivity) context).showProgress(true);

        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String s) {

                        // hide progress
                        ((AbstractActivity) context).showProgress(false);
                        Log.i("RESPONE", s);
                        try {
                            JSONObject responseObject = new JSONObject(s);

                            if (responseObject.optInt("status") == 0) {
                                JSONObject error = responseObject.optJSONObject("error");

                                Error serverError = new Error(error.optString("detail"));

                                listener.registerResult(null, serverError);

                                return;
                            } else {

                                JSONObject data = responseObject.optJSONArray("data").getJSONObject(0);

                                MyApplication.getInstance().saveMemberSession(s);

                                Member member = Member.convertToObject(data);
                                listener.registerResult(member, null);

                                return;
                            }
                        } catch (JSONException e) {
                            Error parseError = new Error(
                                    context.getResources().getString(R.string.error_json_parse)
                            );

                            listener.registerResult(null, parseError);

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
                            listener.registerResult(null, networkError);
                        }

                        return;
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();

                param.put("name", member.getName());
                param.put("lastname", member.getLastname());
                param.put("idcard", member.getIdcard());
                param.put("is_dla_member", String.valueOf(member.getIsDLAMember()));
                param.put("subordinate_province", String.valueOf(member.getSubordinateProvince()));
//                param.put("orgid", member.getOrgid());
//                param.put("orgName", member.getOrgName());

                if (member.getIsDLAMember() == 1) {
                    param.put("subordinate", String.valueOf(member.getSubordinate()));
//                    param.put("subordinate_province", String.valueOf(member.getSubordinateProvince()));
                    param.put("subordinate_item", String.valueOf(member.getSubordinateItem()));
                    param.put("subordinate_other", String.valueOf(member.getSubordinateOther()));
                    param.put("is_spouse", String.valueOf(member.getIsSpouse()));
                    param.put("job_title", String.valueOf(member.getJobTitle()));
                    param.put("position", member.getPosition());
                    param.put("ministry_id", member.getMinistryID());



                }

                return param;
            }
        };

        MyApplication.getInstance().addToRequestQueue(request);
    }

    public static void login(final Context context, final String name, final String lastname, final String idcard, final OnLoginListener listerner) {
        MyApplication myApplication = (MyApplication) context.getApplicationContext();

        String url = String.format(
                "%s/login/%s",
                MyApplication.getInstance().API_HOSTNAME,
                MyApplication.getInstance().API_KEY
        );

        Log.i("LOGIN_TAG", "URL: " + url);

        // show progress
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

                                listerner.loginResult(null, serverError);

                                return;
                            } else {

                                JSONObject data = responseObject.optJSONObject("data");

                                if (data == null) {
                                    listerner.loginResult(null, null);
                                } else {

                                    MyApplication.getInstance().saveMemberSession(s);

                                    Member member = Member.convertToObject(data);
                                    listerner.loginResult(member, null);
                                }

                                return;
                            }
                        } catch (JSONException e) {
                            Error parseError = new Error(
                                    context.getResources().getString(R.string.error_json_parse)
                            );

                            listerner.loginResult(null, parseError);

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
                            listerner.loginResult(null, networkError);
                        }

                        return;
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();

                param.put("name", name);
                param.put("lastname", lastname);
                param.put("idcard", idcard);

                return param;
            }
        };

        MyApplication.getInstance().addToRequestQueue(request);

    }


    public static void updateInfo(final Context context, final String name, final String lastname, final String idcard, final int memberID, final OnUpdateListener listerner) {
        MyApplication myApplication = (MyApplication) context.getApplicationContext();

        String url = String.format(
                "%s/updateInfo/%s",
                MyApplication.getInstance().API_HOSTNAME,
                MyApplication.getInstance().API_KEY
        );


        // show progress
        ((AbstractActivity) context).showProgress(true);

        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String s) {

                        Log.i("EDIT_INFO", s);
                        // hide progress
                        ((AbstractActivity) context).showProgress(false);

                        try {
                            JSONObject responseObject = new JSONObject(s);

                            if (responseObject.optInt("status") == 0) {
                                JSONObject error = responseObject.optJSONObject("error");

                                Error serverError = new Error(error.optString("detail"));

                                listerner.updateResult(null, serverError);

                                return;
                            } else {

                                JSONObject data = responseObject.optJSONObject("data");

                                if (data == null) {
                                    listerner.updateResult(null, null);
                                } else {

                                    MyApplication.getInstance().saveMemberSession(s);

                                    Member member = Member.convertToObject(data);
                                    listerner.updateResult(member, null);
                                }

                                return;
                            }
                        } catch (JSONException e) {
                            Error parseError = new Error(
                                    context.getResources().getString(R.string.error_json_parse)
                            );

                            listerner.updateResult(null, parseError);

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
                            listerner.updateResult(null, networkError);
                        }

                        return;
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();

                param.put("name", name);
                param.put("lastname", lastname);
                param.put("idcard", idcard);
                param.put("id", String.valueOf(memberID));

                return param;
            }
        };

        MyApplication.getInstance().addToRequestQueue(request);

    }

    public static void updateDLAInfo(final Context context, final Member member, final OnUpdateListener listerner) {
        MyApplication myApplication = (MyApplication) context.getApplicationContext();

        String url = String.format(
                "%s/updateDLAInfo/%s",
                MyApplication.getInstance().API_HOSTNAME,
                MyApplication.getInstance().API_KEY
        );


        // show progress
        ((AbstractActivity) context).showProgress(true);

        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String s) {

                        Log.i("EDIT_INFO", s);
                        // hide progress
                        ((AbstractActivity) context).showProgress(false);

                        try {
                            JSONObject responseObject = new JSONObject(s);

                            if (responseObject.optInt("status") == 0) {
                                JSONObject error = responseObject.optJSONObject("error");

                                Error serverError = new Error(error.optString("detail"));

                                listerner.updateResult(null, serverError);

                                return;
                            } else {

                                JSONObject data = responseObject.optJSONArray("data").getJSONObject(0);

                                if (data == null) {
                                    listerner.updateResult(null, null);
                                } else {

                                    MyApplication.getInstance().saveMemberSession(s);

                                    Member member = Member.convertToObject(data);
                                    listerner.updateResult(member, null);
                                }

                                return;
                            }
                        } catch (JSONException e) {
                            Error parseError = new Error(
                                    context.getResources().getString(R.string.error_json_parse)
                            );

                            listerner.updateResult(null, parseError);

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
                            listerner.updateResult(null, networkError);
                        }

                        return;
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();


                param.put("name", member.getName());
                param.put("lastname", member.getLastname());
                param.put("idcard", member.getIdcard());
                param.put("is_dla_member", String.valueOf(member.getIsDLAMember()));
                param.put("id", String.valueOf(member.getId()));
                param.put("subordinate_province", String.valueOf(member.getSubordinateProvince()));
//                param.put("orgid", member.getOrgid());
//                param.put("orgName", member.getOrgName());

                if (member.getIsDLAMember() == 1) {
                    param.put("subordinate", String.valueOf(member.getSubordinate()));

                    param.put("subordinate_item", String.valueOf(member.getSubordinateItem()));
                    param.put("subordinate_other", String.valueOf(member.getSubordinateOther()));
                    param.put("is_spouse", String.valueOf(member.getIsSpouse()));
                    param.put("job_title", String.valueOf(member.getJobTitle()));
                    param.put("position", member.getPosition());
                    param.put("ministry_id", member.getMinistryID());

                }

                Log.i("POST", "ministryID " + member.getMinistryID() + "   sub " + member.getSubordinate());

                return param;
            }
        };

        MyApplication.getInstance().addToRequestQueue(request);

    }

    public interface OnLoginListener {
        public void loginResult(Member member, Error error);
    }

    public interface OnRegisterListener {
        public void registerResult(Member member, Error error);
    }

    public interface OnUpdateListener {
        public void updateResult(Member member, Error error);
    }
}
