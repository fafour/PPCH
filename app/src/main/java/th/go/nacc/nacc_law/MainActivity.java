package th.go.nacc.nacc_law;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import th.go.nacc.nacc_law.application.MyApplication;
import th.go.nacc.nacc_law.fragment.HomeFragment;
import th.go.nacc.nacc_law.fragment.LawTestLevelListFragment;
import th.go.nacc.nacc_law.fragment.NewsFragment;
import th.go.nacc.nacc_law.fragment.ProfileFragment;
import th.go.nacc.nacc_law.fragment.SearchFragment2;
import th.go.nacc.nacc_law.gcm.QuickstartPreferences;
import th.go.nacc.nacc_law.gcm.RegistrationIntentService;
import th.go.nacc.nacc_law.model.News;
import th.go.nacc.nacc_law.utils.SPUtils;
import th.go.nacc.nacc_law.utils.ServiceConnection;
import th.go.nacc.nacc_law.utils.UtilApps;


public class MainActivity extends AbstractActivity {

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "DashboardActivity";
    private ProgressDialog mProgress;

    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private TextView btnHome;
    private TextView btnTest;
    int page = 0;
    private TextView btnProfile;
    private TextView btnSearch;
    private TextView btnNews;

    private View mLayoutCurrentNews;
    private TextView mLblNewsTitle;


    private News mCurrentNews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        page = getIntent().getIntExtra("page", 0);
//        disableActionBar();
//        setTitle("หน้าหลัก");
//        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                SharedPreferences sharedPreferences =
//                        PreferenceManager.getDefaultSharedPreferences(context);
//                boolean sentToken = sharedPreferences
//                        .getBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false);
//                if (sentToken) {
//                    //mInformationTextView.setText(getString(R.string.gcm_send_message));
//                } else {
//                    //mInformationTextView.setText(getString(R.string.token_error_message));
//                }
//            }
//        };
//
//
//        if (checkPlayServices()) {
//            // Start IntentService to register this application with GCM.
//            Intent intent = new Intent(this, RegistrationIntentService.class);
//            startService(intent);
//        }

        blind();

    }


    void blind() {
        btnHome = (TextView) findViewById(R.id.btn_home);
        btnTest = (TextView) findViewById(R.id.btn_test);
        btnProfile = (TextView) findViewById(R.id.btn_profile);
        btnSearch = (TextView) findViewById(R.id.btn_search);
        btnNews = (TextView) findViewById(R.id.btn_news);

        mLayoutCurrentNews = findViewById(R.id.layoutCurrentNews);
        mLblNewsTitle = (TextView) findViewById(R.id.lblNewsTitle);

        mLayoutCurrentNews.setVisibility(View.GONE);

//        final HomeFragment homeFragment = new HomeFragment();
//        final LawTestLevelListFragment lawTestLevelListFragment = new LawTestLevelListFragment();
//        final ProfileFragment profileFragment = new ProfileFragment();
//        final SearchFragment searchFragment = new SearchFragment();


        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                defBtn();
                view.setSelected(true);
                replaceFragment(new HomeFragment());
            }
        });
        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                defBtn();
                view.setSelected(true);
                if (MyApplication.getInstance().getMember() == null) {
                    Intent intent = new Intent(MainActivity.this, RegistrationActivity2.class);
//                    Intent intent = new Intent(MainActivity.this, ActivityRegistrationType.class);
                    startActivityForResult(intent, 1234);
                } else {
                    replaceFragment(new LawTestLevelListFragment());
                }

            }
        });
        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                defBtn();
                view.setSelected(true);
                if (MyApplication.getInstance().getMember() == null) {
                    Intent intent = new Intent(MainActivity.this, RegistrationActivity2.class);
//                    Intent intent = new Intent(MainActivity.this, ActivityRegistrationType.class);
                    startActivityForResult(intent, 1235);
                } else {
                    replaceFragment(new ProfileFragment());
                }
            }
        });
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                defBtn();
                view.setSelected(true);
                replaceFragment(new SearchFragment2());
            }
        });
        btnNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                defBtn();
                view.setSelected(true);
                replaceFragment(new NewsFragment());
            }
        });
        if (page == 0) {
            btnHome.performClick();
        } else if (page == 1) {
            btnTest.performClick();
        } else if (page == 2) {
            btnSearch.performClick();
        } else if (page == 4) {
            btnProfile.performClick();
        }
        String a = SPUtils.getString(this, "installAndroid");
        if (!a.isEmpty()) {

        } else {
            ServiceConnection serviceConnection = new ServiceConnection(this);
            serviceConnection.get(false, MyApplication.getInstance().URL_INSTALL, new ServiceConnection.CallBackListener() {
                @Override
                public void callback(String result) {
                    SPUtils.set(MainActivity.this, "installAndroid", "OK");
                }

                @Override
                public void fail(String result) {

                }
            });
        }


        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                SharedPreferences sharedPreferences =
                        PreferenceManager.getDefaultSharedPreferences(context);
                boolean sentToken = sharedPreferences
                        .getBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false);
                if (sentToken) {
                    //mInformationTextView.setText(getString(R.string.gcm_send_message));
                } else {
                    //mInformationTextView.setText(getString(R.string.token_error_message));
                }
            }
        };

        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }

        _getCurrentNews();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1234) {
            if (resultCode == RESULT_OK) {
                btnTest.performClick();
            }else{

            }
        } if (requestCode == 1235) {
            if (resultCode == RESULT_OK) {
            }else{

            }
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        setContentView(R.layout.activity_main);
        blind();

    }

    public void showProgress(boolean isShow) {
        if (mProgress == null) {
            mProgress = new ProgressDialog(this);
            mProgress.setCancelable(false);
            mProgress.setMessage("Loading");
        }

        if (isShow) {
            if (mProgress.isShowing() == false) {
                mProgress.show();
            }
        } else {
            if (mProgress.isShowing()) {
                mProgress.dismiss();
            }
        }
    }

    private void _getCurrentNews() {
        if (UtilApps.isNewsClose(this)) {
            return;
        }
        News.getCurrentNews(this, new News.OnGetCurrentNewsListener() {
            @Override
            public void onResult(News news, Error error) {
                if (error == null) {
                    if (news != null) {

                        mCurrentNews = news;

                        // show news on top of page for a 15 seconds
                        mLayoutCurrentNews.setVisibility(View.VISIBLE);
                        mLblNewsTitle.setText(news.getTitle());

                        new android.os.Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mLayoutCurrentNews.setVisibility(View.GONE);
                            }
                        }, 10000);


                    }
                    //simpleAlert( news.getTitle() );
                } else {
                    //simpleAlert( error.getMessage() );
                }
            }
        });
    }

    void defBtn() {
        btnHome.setSelected(false);
        btnTest.setSelected(false);
        btnProfile.setSelected(false);
        btnSearch.setSelected(false);
        btnNews.setSelected(false);
    }


    @Override
    protected void onResume() {
        super.onResume();


        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));

    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }


    private void replaceFragment(Fragment fragment) {
        if (fragment.isAdded()) {
            return; //or return false/true, based on where you are calling from
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

// Replace whatever is in the fragment_container view with this fragment,
// and add the transaction to the back stack if needed
        transaction.replace(R.id.frame, fragment);
//        transaction.addToBackStack(null);

// Commit the transaction
        transaction.commit();
    }

    public void replaceFragmentBack(Fragment fragment) {
        if (fragment.isAdded()) {
            return; //or return false/true, based on where you are calling from
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

// Replace whatever is in the fragment_container view with this fragment,
// and add the transaction to the back stack if needed
        transaction.replace(R.id.frame, fragment);
        transaction.addToBackStack(null);

// Commit the transaction
        transaction.commit();
    }
//    @Override
//    protected void onResume() {
//        super.onResume();
//
//
//        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
//                new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));
//    }
//
//    @Override
//    protected void onPause() {
//        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
//        super.onPause();
//    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }


    public void closeCurrentNewsAction(View v) {
        mLayoutCurrentNews.setVisibility(View.GONE);
        UtilApps.setisNewsClose(MainActivity.this, true);
    }

    public void viewNewsDetailAction(View v) {
        mLayoutCurrentNews.setVisibility(View.GONE);

        if (mCurrentNews == null) return;

        Intent intent = new Intent(this, NewsDetailActivity2.class);

        intent.putExtra("news", mCurrentNews);
        //intent.putExtra( "news_id", mCurrentNews.getId() );

        startActivity(intent);
    }
}
