package th.go.nacc.nacc_law.application;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.multidex.MultiDex;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import th.go.nacc.nacc_law.model.Examination;
import th.go.nacc.nacc_law.model.Member;
import th.go.nacc.nacc_law.model.Section;
import th.go.nacc.nacc_law.model.SectionContent;
import th.go.nacc.nacc_law.model.SectionPDF;
import th.go.nacc.nacc_law.model.Terminology;
import th.go.nacc.nacc_law.model.TerminologyPDF;
import th.go.nacc.nacc_law.utils.LruBitmapCache;

/**
 * Created by nontachai on 7/11/15 AD.
 */
public class MyApplication extends Application  {

//    public final String API_HOSTNAME = "http://110.164.162.170/nacc/index.php/service/";
    public final String API_HOSTNAME = "http://nacc-staging.siamsoftwaresolution.com/index.php/service/";

    //public final String API_HOSTNAME = "http://192.168.1.4:8888/NACC_Prod/index.php/service/";
    //public final String API_HOSTNAME = "http://client.nister.co.th/nacc/index.php/service/";
    public final String API_KEY = "mSqGcW29JhZPo2Kj2oZqdqPNaquGX9kjUXvt2l3waonuqaIfzvBa8lbiu1ny8MX";
    public final String URL_EDUCATION = API_HOSTNAME+"getEducation/";
    public final String URL_THOUGHT = API_HOSTNAME+"getthought/";
    public final String URL_LAW_ABOUT = API_HOSTNAME+"getrelevantlaws/";
    public final String URL_NEWS = API_HOSTNAME+"getNews/";
    public final String URL_COMMENT = API_HOSTNAME+"comments/";
    public final String URL_ORGANIZATION = API_HOSTNAME+"getorganization/";
    public final String URL_MINISTRY = API_HOSTNAME+"ministry/";
    public final String URL_ALL = API_HOSTNAME+"  getapiall/";
    public final String URL_INSTALL = API_HOSTNAME+"detectDevice/";

    public final String GCM_SERVER_API_KEY = "AIzaSyDk648pKhHkC6vezMt2RnciUka-uRFy13c";
    public final String GCM_SENDER_ID = "184975351738";

    public final String EXTERNAL_STORAGE = "nacc";
    // data
    public List<Section> sections;
    public List<SectionContent> contents100;
    public List<SectionContent> contents103;
    public List<SectionPDF> pdf100;
    public List<SectionPDF> pdf103;

    public List<Terminology> terms;
    public List<TerminologyPDF> termsPDF;

//    public List<SectionPDF> pdf=new ArrayList<>();
//    public List<SectionContent> contents=new ArrayList<>();

    private Member member;

    private Examination examination;

    public static final String TAG = MyApplication.class.getSimpleName();

    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;

    private static MyApplication mInstance;

    @Override
    public void onCreate () {
        super.onCreate();
        mInstance = this;
        MultiDex.install(this);

        checkMemberSession();
    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static synchronized  MyApplication getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue () {
        if ( mRequestQueue == null ) {
            mRequestQueue = Volley.newRequestQueue( getApplicationContext());
        }

        return mRequestQueue;
    }

    public ImageLoader getmImageLoader () {
        getRequestQueue();
        if ( mImageLoader == null ) {
            mImageLoader = new ImageLoader( this.mRequestQueue, new LruBitmapCache());
        }

        return mImageLoader;
    }

    public <T> void addToRequestQueue (Request<T> req, String tag ) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add( req );
    }

    public <T> void addToRequestQueue ( Request<T> req ) {
        req.setTag( TAG );
        getRequestQueue().add( req );
    }

    public void cancelPendingRequests (Object tag ) {
        if ( mRequestQueue != null ) {
            mRequestQueue.cancelAll( tag );
        }
    }

    public void cancelAllPendingRequests () {
        if ( mRequestQueue != null ) {
            mRequestQueue.cancelAll( TAG );
        }
    }

    public Member getMember () {
        return member;
    }

    public void setExamination ( Examination examination ) {
        this.examination = examination;
    }

    public Examination getExamination () {
        return this.examination;
    }

    public void saveMemberSession ( String token ) {
        SharedPreferences sharedpreferences = getSharedPreferences(MyApplication.TAG, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedpreferences.edit();

        editor.putString( "login_token", token );

        editor.commit();

        checkMemberSession();
    }

    public void clearMemberSession () {
        SharedPreferences sharedpreferences = getSharedPreferences(MyApplication.TAG, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedpreferences.edit();

        editor.putString( "login_token", null );

        editor.commit();

        member = null;
    }

    public void checkMemberSession () {
        SharedPreferences sharedpreferences = getSharedPreferences(MyApplication.TAG, Context.MODE_PRIVATE);

        String loginToken = sharedpreferences.getString( "login_token", null );
//        Log.i("USER",loginToken);
        if ( loginToken != null && !loginToken.equals( "" ) ) {
            try {
                JSONObject jsonObject = new JSONObject( loginToken );

                if ( jsonObject.optInt ( "status" ) == 1 ) {
                    member = Member.convertToObject( jsonObject.getJSONArray("data").getJSONObject(0));
                } else {
                    member = null;
                }
            } catch ( JSONException e ) {
                member = null;
                e.printStackTrace();
            }
        }
    }
}
