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
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
import th.go.nacc.nacc_law.model.SectionPDF;

/**
 * Created by nontachai on 7/10/15 AD.
 */
public class LawPDFDetailActivity extends AbstractActivity {

    private int lawSection;
    private SectionPDF mPDF;

    private TextView mLblName;

    private ProgressDialog mDownloadingDialog;

    private Button mBtnDownloadOpen;

    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );

        setContentView(R.layout.activity_law_pdf_detail);

        Bundle data = getIntent().getExtras();

        if ( data != null ) {
            lawSection = data.getInt ( "law_section" );
            mPDF = data.getParcelable( "pdf" );
        }

        final ActionBar actionBar = getActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);

        BitmapDrawable background = new BitmapDrawable( getResources(), BitmapFactory.decodeResource(getResources(), R.drawable.top_bar));
        background.setTileModeX(android.graphics.Shader.TileMode.REPEAT);
        actionBar.setBackgroundDrawable(background);

        setTitle( "ดาวน์โหลดข้อมูลเพิ่มเติม" );

        _outletObject();

        _checkFileDownloaded();
    }

    private void _outletObject () {
        mDownloadingDialog = new ProgressDialog(this);
        mDownloadingDialog.setMessage("Downloading file. Please wait...");
        mDownloadingDialog.setIndeterminate(false);
        mDownloadingDialog.setMax(100);
        mDownloadingDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mDownloadingDialog.setCancelable(true);

        mBtnDownloadOpen = (Button) findViewById( R.id.btnDownloadOpen );

        mLblName = (TextView) findViewById( R.id.lblName );

        mLblName.setText(mPDF.getTitle());
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

    private void _checkFileDownloaded () {
        // save preference
        SharedPreferences sharedpreferences = getSharedPreferences(MyApplication.TAG, Context.MODE_PRIVATE);

        String localFilepath = sharedpreferences.getString(mPDF.getLink(), null);

        if ( localFilepath != null ) {
            File pdfFile = new File( localFilepath );

            if ( pdfFile.exists() ) {
                mBtnDownloadOpen.setText("Open");
            } else {
                mBtnDownloadOpen.setText( "Download" );
            }

        } else {
            mBtnDownloadOpen.setText( "Download" );
        }
    }

    public void downloadAction ( View v ) {
        // save preference
        SharedPreferences sharedpreferences = getSharedPreferences(MyApplication.TAG, Context.MODE_PRIVATE);

        String localFilepath = sharedpreferences.getString(mPDF.getLink(), null);

        if ( localFilepath != null ) {
            File pdfFile = new File( localFilepath );

            if ( pdfFile.exists() ) {
                openFile( pdfFile.getAbsolutePath() );
            } else {
                new DownloadFileFromURL().execute(  );
            }
        } else {
            new DownloadFileFromURL().execute( );
        }
    }



    /**
     * Background Async Task to download file
     * */
    class DownloadFileFromURL extends AsyncTask<Void, String, String> {

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
        protected String doInBackground(Void... params) {
            int count;

            String filePath = null;

            try {


                URL url = new URL( mPDF.getLinkPath());
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

                editor.putString( mPDF.getLink(), filePath );

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


            _checkFileDownloaded();

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
            new AlertDialog.Builder( this )
                    .setTitle("ไม่สามารถเปิดไฟล์ PDF ได้")
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
