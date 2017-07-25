package th.go.nacc.nacc_law;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import th.go.nacc.nacc_law.application.MyApplication;
import th.go.nacc.nacc_law.component.ButtonPlus;
import th.go.nacc.nacc_law.da.ExaminationDA;
import th.go.nacc.nacc_law.gcm.QuickstartPreferences;
import th.go.nacc.nacc_law.gcm.RegistrationIntentService;
import th.go.nacc.nacc_law.model.Initial;
import th.go.nacc.nacc_law.model.News;


public class DashboardActivity extends AbstractActivity {

    private Button mBtnLaw100, mBtnLaw103;
    private ButtonPlus mImgMemberBox;

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "DashboardActivity";


    private BroadcastReceiver mRegistrationBroadcastReceiver;

    private View mLayoutCurrentNews;
    private TextView mLblNewsTitle;


    private News mCurrentNews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        _outletObject();

        _getData();

        checkMemberLoggedIn();


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

    private void _getCurrentNews () {
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

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        setContentView(R.layout.activity_dashboard);

        _outletObject();

        checkMemberLoggedIn();
    }

    @Override
    public void refresh () {
        _getData();
    }

    private void _getData () {
        Log.i("TAG", "GET DATA");

        // save preference
        SharedPreferences sharedpreferences = getSharedPreferences(MyApplication.TAG, Context.MODE_PRIVATE);

        
        String initialToken = sharedpreferences.getString("initial", null);

        boolean showProgress = false;

        if ( initialToken == null ) {
            showProgress = true;
        }

        final boolean existsCache = showProgress;

        Initial.get(this, showProgress, new Initial.OnResponseListener() {
            @Override
            public void onGetListener(boolean isUpdated, Initial initial, Error error) {
                if (error == null) {
                    if (isUpdated) {
                        _setDataToApplication(initial);
                    }
                } else {
                    if (existsCache) {
                        retryService(error.getMessage());
                    }
                }
            }
        });
    }



    @Override
    protected  void onDestroy () {
        super.onDestroy();

        MyApplication.getInstance().cancelAllPendingRequests();
    }

    private void _outletObject () {
        mBtnLaw100 = (Button) findViewById( R.id.btnLaw100 );
        mBtnLaw103 = (Button) findViewById( R.id.btnLaw103 );
        mImgMemberBox = (ButtonPlus) findViewById( R.id.imgMemberBox);

        mLayoutCurrentNews = findViewById( R.id.layoutCurrentNews );
        mLblNewsTitle = (TextView)findViewById( R.id.lblNewsTitle );

        mLayoutCurrentNews.setVisibility(View.GONE);
    }

    @Override
    protected void onResume () {
        super.onResume();

        checkMemberLoggedIn();

        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    private void checkMemberLoggedIn() {
        if ( MyApplication.getInstance().getMember() == null ) {
            mImgMemberBox.setVisibility( View.GONE );
        } else {
            mImgMemberBox.setVisibility( View.VISIBLE );
            mImgMemberBox.setText( "คุณ" + MyApplication.getInstance().getMember().getName() );
        }
    }

    public void lawContentAction ( View v ) {


        if ( MyApplication.getInstance().sections == null
                || MyApplication.getInstance().sections.size() == 0 ) {
            _getData();
            return;
        }

        Intent intent = new Intent( this, SectionPortalActivity.class );

        if ( v == mBtnLaw100 ) {
            intent.putExtra("law_section", 100);
        } else if ( v == mBtnLaw103 ) {
            intent.putExtra( "law_section", 103 );
        }


        startActivity( intent );
    }

    public void aboutUsAction ( View v ) {
        Intent intent = new Intent( Intent.ACTION_VIEW, Uri.parse( "http://www.nacc.go.th/main.php?filename=pr_nacc" ));

        startActivity( intent );
    }

    public void memberAction ( View v ) {

        LayoutInflater li = LayoutInflater.from(this);
        View alertView = li.inflate(R.layout.alertlayout, null);

        TextView textView1 = (TextView) alertView.findViewById( R.id.textView1 );

        textView1.setText( "คุณต้องการแก้ไขข้อมูลส่วนตัว หรือออกจากระบบหรือไม่?" );

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(alertView);

        //final EditText userInput = (EditText) alertView.findViewById(R.id.editTextDialogUserInput);

        // set dialog message
        alertDialogBuilder
                //.setTitle( "รับเกียรติบัตร" )
                .setCancelable(false)
                .setPositiveButton("แก้ไขข้อมูล", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent ( DashboardActivity.this, EditMemberActivity.class );

                        startActivity( intent );
                    }
                })
                .setNegativeButton("ออกจากระบบ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        new AlertDialog.Builder(DashboardActivity.this)
                                .setMessage("คุณต้องการออกจากระบบใช่หรือไม่?")
                                .setPositiveButton("ใช่", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        if (ExaminationDA.isExistExamination(DashboardActivity.this)) {
                                            ExaminationDA.clearExamination(DashboardActivity.this);
                                        }

                                        MyApplication.getInstance().clearMemberSession();

                                        simpleAlert("ออกจากระบบเรียบร้อยแล้ว!");

                                        checkMemberLoggedIn();
                                    }
                                })
                                .setNegativeButton("ยกเลิก", null)
                                .show();

                    }
                })
                .setNeutralButton("ยกเลิก", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();

        /*
        new AlertDialog.Builder( this )
                .setMessage( "คุณต้องการแก้ไขข้อมูลส่วนตัว หรือออกจากระบบหรือไม่?" )
                .setPositiveButton("แก้ไขข้อมูล", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setNegativeButton("ออกจากระบบ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        new AlertDialog.Builder( DashboardActivity.this )
                                .setMessage( "คุณต้องการออกจากระบบใช่หรือไม่?" )
                                .setPositiveButton("ใช่", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        if (ExaminationDA.isExistExamination(DashboardActivity.this)) {
                                            ExaminationDA.clearExamination(DashboardActivity.this);
                                        }

                                        MyApplication.getInstance().clearMemberSession();

                                        simpleAlert("ออกจากระบบเรียบร้อยแล้ว!");

                                        checkMemberLoggedIn();
                                    }
                                })
                                .setNegativeButton( "ยกเลิก", null )
                                .show();

                    }
                })
                .setNeutralButton("ยกเลิก", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .show();*/
    }



    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
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


    public void closeCurrentNewsAction ( View v ) {
        mLayoutCurrentNews.setVisibility( View.GONE );
    }

    public void viewNewsDetailAction ( View v ) {
        mLayoutCurrentNews.setVisibility( View.GONE );

        if ( mCurrentNews == null ) return;

        Intent intent = new Intent( this, NewsDetailActivity2.class );

        intent.putExtra( "news", mCurrentNews );
        //intent.putExtra( "news_id", mCurrentNews.getId() );

        startActivity( intent );
    }

}
