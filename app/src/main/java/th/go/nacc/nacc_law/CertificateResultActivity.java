package th.go.nacc.nacc_law;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;

import com.android.volley.toolbox.NetworkImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import th.go.nacc.nacc_law.application.MyApplication;
import th.go.nacc.nacc_law.model.Examination;

/**
 * Created by nontachai on 7/11/15 AD.
 */
public class CertificateResultActivity extends AbstractActivity {

    private int historyID;
    private String certificatePath;

    private NetworkImageView mImgCertificate;
    private View mViewAction;

    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );

        setContentView(R.layout.activity_certificate_result);

        Bundle data = getIntent().getExtras();

        if ( data != null ) {
            certificatePath = data.getString("certificate");
            historyID = data.getInt( "history_id" );
        }

//        final ActionBar actionBar = getActionBar();
//
//
//        actionBar.setDisplayHomeAsUpEnabled(true);
//
//        BitmapDrawable background = new BitmapDrawable( getResources(), BitmapFactory.decodeResource(getResources(), R.drawable.top_bar));
//        background.setTileModeX(android.graphics.Shader.TileMode.REPEAT);
//        actionBar.setBackgroundDrawable(background);

        _outletObject();

        setTitle("ใบเกียรติบัตร");
    }

    private void _outletObject () {
        mImgCertificate = (NetworkImageView) findViewById( R.id.imgCertficiate );
        mViewAction = findViewById( R.id.viewAction );
//        certificatePath = "http://socialnews.teenee.com/penkhao/img3/12947.jpg";
        if ( certificatePath != null && !certificatePath.equals("") ) {
            mImgCertificate.setImageUrl(certificatePath, MyApplication.getInstance().getmImageLoader());
        } else {
            mViewAction.setVisibility( View.GONE );
            //_getCertificatePath();
        }

    }

    /*
    private void _getCertificatePath ( ) {
        Examination.getCertificate(this, historyID, new Examination.OnGetCertificateListener() {
            @Override
            public void certificateResult(String rsCertificatePath, Error error) {
                if ( error == null ) {
                    mViewAction.setVisibility( View.VISIBLE );

                    certificatePath = rsCertificatePath;

                    mImgCertificate.setImageUrl(certificatePath, MyApplication.getInstance().getmImageLoader());
                } else {
                    simpleAlert( error.getMessage() );
                }
            }
        });
    }
    */

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        if (id == android.R.id.home) {
//            finish();
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    public void requestEmailAction ( View v ) {

        Drawable mDrawable = mImgCertificate.getDrawable();
        Bitmap mBitmap = ((BitmapDrawable)mDrawable).getBitmap();

        String path = MediaStore.Images.Media.insertImage(getContentResolver(),
                mBitmap, "Image Description", null);

        Uri uri = Uri.parse(path);



        try {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("image/*");
            i.putExtra(Intent.EXTRA_SUBJECT, "ใบเกียรติบัตร");
            i.putExtra(Intent.EXTRA_STREAM, uri);
            startActivity(Intent.createChooser(i, "Send Image..."));
        } catch(Exception e) {
            //e.toString();
        }

//        Picasso.with(this).load(certificatePath).into(new Target() {
//            @Override
//            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
//                showProgress( false );
//
//                String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "Nur", null);
//
//                Uri uri = Uri.parse(path);
//
//
//                //final Intent emailIntent = new Intent(android.content.Intent.ACTION_SENDTO);
//
////                Intent emailIntent = new Intent(Intent.ACTION_SEND);
//////                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "ใบเกียรติบัตร");
//////                emailIntent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.gameindy.asuraonline");
////                emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
//////                emailIntent.setType("message/rfc822");
////                emailIntent.setType("image/*");
////
////
////
////
////                startActivity(Intent.createChooser(emailIntent, "Send Image..."));
//
//
//
//            }
//
//            @Override
//            public void onBitmapFailed(Drawable errorDrawable) {
//                showProgress( false );
//            }
//
//            @Override
//            public void onPrepareLoad(Drawable placeHolderDrawable) {
//                showProgress( true );
//            }
//        });

//        new SaveImageTask().execute();
        /*
        // get prompts.xml view
        LayoutInflater li = LayoutInflater.from( this );
        View promptsView = li.inflate(R.layout.prompts, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final EditText userInput = (EditText) promptsView
                .findViewById(R.id.editTextDialogUserInput);

        // set dialog message
        alertDialogBuilder
                //.setTitle( "รับเกียรติบัตร" )
                .setCancelable(false)
                .setPositiveButton("ส่ง",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // get user input and set it to result
                                // edit text
                                //result.setText(userInput.getText());
                                if (isValidEmail(userInput.getText())) {
                                    requestCertificate(userInput.getText().toString());
                                } else {
                                    simpleAlert("อีเมล์ไม่ถูกต้อง กรุณาตรวจสอบอีกครั้ง!");
                                }

                            }
                        })
                .setNegativeButton("ยกเลิก",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
        */
    }

    private boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    private void requestCertificate ( final String email ) {
        Examination.requestCertificate(this, historyID, email, new Examination.OnRequestCertificateListener() {
            @Override
            public void requestResult(boolean result, Error error) {
                if ( error == null ) {
                    if ( result ) {
                        simpleAlert( "ส่งเกียรติบัตรไปยังอีเมล์ " + email + " เรียบร้อยแล้ว");
                    } else {
                        simpleAlert( "ไม่สามารถส่งอีเมล์ได้ กรุณาตรวจสอบอีเมล์อีกครั้ง หรือแจ้งผู้ดูแลระบบ" );
                    }
                } else {
                    simpleAlert( error.getMessage() );
                }
            }
        });
    }


    private class SaveImageTask extends AsyncTask<Void,Void,Bitmap> {
        @Override
        protected void onPreExecute() {
            showProgress( true );
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            showProgress( false );

            if ( bitmap == null ) {
                simpleAlert( "ไม่สามารถส่งอีเมล์ได้ กรุณาลองใหม่อีกครั้งภายหลัง");
            } else {
                Bitmap mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);




                View view  = new View(CertificateResultActivity.this);

                view.draw(new Canvas(mutableBitmap));

                String path = MediaStore.Images.Media.insertImage(getContentResolver(), mutableBitmap, "Nur", null);

                Uri uri = Uri.parse(path);




                //final Intent emailIntent = new Intent(android.content.Intent.ACTION_SENDTO);

                Intent emailIntent = new Intent(Intent.ACTION_SEND);
//                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "ใบเกียรติบัตร");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.gameindy.asuraonline");
                emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
//                emailIntent.setType("message/rfc822");
                emailIntent.setType("image/*");




                startActivity(Intent.createChooser(emailIntent, "Send Image..."));
            }
            super.onPostExecute(bitmap);
        }

        @Override
        protected Bitmap doInBackground(Void... voids) {

            URL url = null;
            try {
                url = new URL(certificatePath);

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                connection.setDoInput(true);

                connection.connect();

                InputStream input = connection.getInputStream();

                Bitmap immutableBpm = BitmapFactory.decodeStream(input);

                return immutableBpm;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}
