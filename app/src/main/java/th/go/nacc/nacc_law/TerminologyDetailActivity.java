package th.go.nacc.nacc_law;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import th.go.nacc.nacc_law.application.MyApplication;
import th.go.nacc.nacc_law.helper.StringHelper;
import th.go.nacc.nacc_law.model.Terminology;
import th.go.nacc.nacc_law.model.TerminologyPDF;

/**
 * Created by nontachai on 7/11/15 AD.
 */
public class TerminologyDetailActivity extends AbstractActivity {

    private Terminology mTerm;

    private TextView mLblTitle;

    private WebView mWebView;

    private ProgressDialog mDownloadingDialog;

    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );

        setContentView( R.layout.activity_terminology_detail );

        Bundle data = getIntent().getExtras();

        if ( data != null ) {
            mTerm = data.getParcelable("term");
        }


//        final ActionBar actionBar = getActionBar();
//
//
//        actionBar.setDisplayHomeAsUpEnabled(true);
//
//        BitmapDrawable background = new BitmapDrawable( getResources(), BitmapFactory.decodeResource(getResources(), R.drawable.top_bar));
//        background.setTileModeX(android.graphics.Shader.TileMode.REPEAT);
//        actionBar.setBackgroundDrawable(background);

        setTitle("ข้อมูลเชิงอรรถ");

        _outletObject();
    }

    private void _outletObject () {
        mDownloadingDialog = new ProgressDialog(this);
        mDownloadingDialog.setMessage("Downloading file. Please wait...");
        mDownloadingDialog.setIndeterminate(false);
        mDownloadingDialog.setMax(100);
        mDownloadingDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mDownloadingDialog.setCancelable(true);

        mLblTitle = (TextView) findViewById( R.id.lblTitle );
        mWebView = (WebView) findViewById( R.id.webView );
        mWebView.setBackgroundColor(0x00000000);

        WebViewClient webClient = new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                if ( url.startsWith( "mailto:" )) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    startActivity(intent);
                    return true;
                } else if ( url.startsWith( "http://" ) || url.startsWith( "https://" )) {
                    if ( url.contains( "line.me")) {

                        Intent i = new Intent ( Intent.ACTION_VIEW );
                        i.setData( Uri.parse( url ) );

                        startActivity(i);
                    } else {

                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(url));
                        startActivity(intent);
                    }

                    return true;
                } else if ( url.startsWith( "tel:" )) {
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(url));

                    //EndCallListener callListener = new EndCallListener();
                    //TelephonyManager mTM = (TelephonyManager)getContext().getSystemService(Context.TELEPHONY_SERVICE);
                    //mTM.listen(callListener, PhoneStateListener.LISTEN_CALL_STATE);

                    startActivity(intent);
                    return true;
                } else if ( url.startsWith( "term://" )) {
                    Intent intent = new Intent ( TerminologyDetailActivity.this, TerminologyDetailActivity.class );

                    url = url.replace( "term://", "" );

                    String[] params = url.split( "=" );

                    if ( params.length > 1 ) {

                        try {
                            int termID = Integer.parseInt(params[1]);

                            for ( int i = 0, max = MyApplication.getInstance().terms.size(); i < max; i++ ) {
                                if ( MyApplication.getInstance().terms.get( i ).getId() == termID ) {
                                    intent.putExtra( "term", MyApplication.getInstance().terms.get( i ));
                                    break;
                                }
                            }
                            startActivity(intent);
                        } catch ( Exception e ) {
                            e.printStackTrace();
                        }
                    }

                    return true;
                } else if ( url.startsWith( "termPDF://" ) || url.startsWith( "termpdf://") ) {
                    url = url.replace( "termPDF://", "" );
                    url = url.replace( "termpdf://", "" );

                    String[] params = url.split( "=" );

                    if ( params.length > 1 ) {

                        try {
                            int termPDFID = Integer.parseInt(params[1]);

                            TerminologyPDF termPDF = null;

                            for ( int i = 0, max = MyApplication.getInstance().termsPDF.size(); i < max; i++ ) {
                                if ( MyApplication.getInstance().termsPDF.get( i ).getId() == termPDFID ) {
                                    termPDF = MyApplication.getInstance().termsPDF.get( i );
                                    break;
                                }
                            }

                            if ( termPDF != null ) {

                                // save preference
                                SharedPreferences sharedpreferences = getSharedPreferences(MyApplication.TAG, Context.MODE_PRIVATE);

                                String localFilepath = sharedpreferences.getString(termPDF.getLink(), null);

                                if ( localFilepath != null ) {
                                    File pdfFile = new File( localFilepath );

                                    if ( pdfFile.exists() ) {
                                        openFile( pdfFile.getAbsolutePath() );
                                    } else {
                                        new DownloadFileFromURL().execute( termPDF );
                                    }
                                } else {
                                    new DownloadFileFromURL().execute( termPDF );
                                }
                            }
                        } catch ( Exception e ) {
                            e.printStackTrace();
                        }
                    }

                    return true;
                }

                return false;
            }
        };

        mWebView.setWebViewClient( webClient );

        WebSettings settings = mWebView.getSettings();

        DisplayMetrics metrics = getResources().getDisplayMetrics();

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        //int height = size.y;

        int criteria = width;

        if ( height < width ) {
            criteria = height;
        }

        settings.setDefaultFontSize((int) (Math.ceil(((criteria / metrics.density) / 320.0f) * 13 * (metrics.scaledDensity / metrics.density))));

        if (Build.VERSION.SDK_INT >= 11) mWebView.setLayerType(WebView.LAYER_TYPE_NONE, null);

        //mWebView.getSettings().setJavaScriptEnabled( true );
        //if (Build.VERSION.SDK_INT >= 11) mWebView.setLayerType(WebView.LAYER_TYPE_NONE, null);

        _updateContent();
    }

    private void _updateContent () {
        mLblTitle.setText( mTerm.getWording());

        String detail = "<html> " +
                "<head> " +
                //"<meta name=\"viewport\" content=\"target-densitydpi=medium-dpi\" />"+
                "<style ='text/css'> " +
                "@font-face { font-family: 'CustomFont'; font-weight: normal; font-style: normal; src: url('Font-Bold.ttf' ); } " +
                "@font-face { font-family: 'CustomFont'; font-weight: bold; font-style: normal; src: url('Font-Black.ttf' ); } " +
                "@font-face { font-family: 'CustomFont'; font-weight: bold; font-style: italic; src: url('Font-BlackItalic.ttf' ); } " +
                "@font-face { font-family: 'CustomFont'; font-weight: normal; font-style: italic; src: url('Font-BoldItalic.ttf' ); } " +
                "body {background: white; margin: 0px; font-family: 'CustomFont', Verdana, Arial; padding: " + (int)getResources().getDimension( R.dimen.little_margin_gap ) + "px} " +
                "a, a:hover, a:active { color: #1E90FF; text-decoration: underline;} " +
                "</style>" +
                "</head>" +
                "<body>" + mTerm.getDetail() + "</body>" +
                "</html>";

        mWebView.loadDataWithBaseURL("file:///android_asset/", detail, "text/html", "UTF-8", null);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * Background Async Task to download file
     * */
    class DownloadFileFromURL extends AsyncTask<TerminologyPDF, String, String> {

        /**
         * Before starting background thread
         * Show Progress Bar Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDownloadingDialog.show();
            //showDialog(progress_bar_type);
        }

        /**
         * Downloading file in background thread
         * */
        @Override
        protected String doInBackground(TerminologyPDF... termPDFs) {
            int count;
            if ( termPDFs.length <= 0 ) return null;

            String filePath = null;

            try {
                TerminologyPDF termPDF = termPDFs[0];

                URL url = new URL( termPDF.getLinkPath());
                URLConnection conection = url.openConnection();
                conection.connect();
                // this will be useful so that you can show a tipical 0-100% progress bar
                int lenghtOfFile = conection.getContentLength();

                // download the file
                InputStream input = new BufferedInputStream(url.openStream(), 8192);

                String filename = StringHelper.randomString(15) + ".pdf";

                // Output stream
                String strFileDir = Environment.getExternalStorageDirectory().toString() +
                        File.separator +
                        MyApplication.getInstance().EXTERNAL_STORAGE;

                File fileDir = new File( strFileDir );


                if ( fileDir.exists() == false ) {
                    fileDir.mkdir();
                }

                filePath = strFileDir + File.separator + filename;

                OutputStream output = new FileOutputStream( filePath );

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress(""+(int)((total*100)/lenghtOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();

                // save preference
                SharedPreferences sharedpreferences = getSharedPreferences(MyApplication.TAG, Context.MODE_PRIVATE);

                SharedPreferences.Editor editor = sharedpreferences.edit();

                editor.putString( termPDF.getLink(), filePath );

                editor.commit();
            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return filePath;
        }

        /**
         * Updating progress bar
         * */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage

            mDownloadingDialog.setProgress( Integer.parseInt(progress[0]) );
            //pDialog.setProgress(Integer.parseInt(progress[0]));
        }

        /**
         * After completing background task
         * Dismiss the progress dialog
         * **/
        @Override
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after the file was downloaded
            //dismissDialog(progress_bar_type);
            mDownloadingDialog.dismiss();


            openFile( file_url );
        }

    }

    private void openFile ( String url ) {
        File file = new File( url );
        Intent target = new Intent(Intent.ACTION_VIEW);
        target.setDataAndType(Uri.fromFile(file),"application/pdf");
        target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

        Intent intent = Intent.createChooser(target, "Open File");
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            // Instruct the user to install a PDF reader here, or something
            new AlertDialog.Builder( TerminologyDetailActivity.this )
                    .setTitle( "ไม่สามารถเปิดไฟล์ PDF ได้" )
                    .setMessage( "คุณต้องดาวน์โหลดโปรแกรมสำหรับอ่าน PDF ก่อน จึงจะสามารถเปิดอ่านไฟล์ได้! ต้องการดาวน์โหลดหรือไม่?")
                    .setPositiveButton("ต้องการ", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            final String appPackageName = "com.adobe.reader";
                            try {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                            } catch (android.content.ActivityNotFoundException anfe) {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                            }
                        }
                    })
                    .setNegativeButton( "ไม่ต้องการ", null )
                    .show();
        }
    }
}
