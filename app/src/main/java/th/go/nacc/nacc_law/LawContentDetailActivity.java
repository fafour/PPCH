package th.go.nacc.nacc_law;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import th.go.nacc.nacc_law.application.MyApplication;
import th.go.nacc.nacc_law.helper.StringHelper;
import th.go.nacc.nacc_law.helper.TouchyWebView;
import th.go.nacc.nacc_law.helper.ZoomOutPageTransformer;
import th.go.nacc.nacc_law.model.SectionContent;
import th.go.nacc.nacc_law.model.TerminologyPDF;

/**
 * Created by nontachai on 7/10/15 AD.
 */
public class LawContentDetailActivity extends AbstractActivity {

    private int lawSection;

    private List<SectionContent> mAllContents;
    private SectionContent mContent;

    private TextView mLblTitle;

    private TouchyWebView mWebView;

    private ProgressDialog mDownloadingDialog;

    private Button mBtnNext, mBtnPrev;
    private LinearLayout layoutText;
    ArrayList<String> images = new ArrayList<>();
    private boolean isPLAYING;

    MediaPlayer mp;
    private SeekBar progressBar;
    private final Handler handler = new Handler();
    private int mediaFileLengthInMilliseconds = 0;
    String title = "";
    private FrameLayout layoutImage;
    private LinearLayout layoutAudio;
    private ViewPager pager;
    private ImageView btnPlay;
    private TextView tvTime;
    private Runnable notification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_law_content_detail);

        Bundle data = getIntent().getExtras();

        if (data != null) {
            lawSection = data.getInt("law_section");
            mContent = data.getParcelable("content");
            mAllContents = data.getParcelableArrayList("contents");
            title = data.getString("title");
        }

        mp = new MediaPlayer();

        if (!mContent.getClip().isEmpty()) {
            try {
                String image = "http://img.youtube.com/vi/" + mContent.getClip().split("v=")[1] + "/0.jpg";
                images.add(image);
            } catch (Exception e) {

            }
        }
        if (!mContent.getImage().isEmpty()) {
            images.add(mContent.getImage());
        }

//        images.add("http://socialnews.teenee.com/penkhao/img3/12947.jpg");
//        images.add("https://pbs.twimg.com/profile_images/2152422809/401479_315252105187066_315174978528112_870892_1922620152_n.jpg");
//        images.add("http://campus.campus-star.com/app/uploads/2016/05/%E0%B8%99%E0%B9%88%E0%B8%B2%E0%B8%A3%E0%B8%B1%E0%B8%81%E0%B9%84%E0%B8%A1%E0%B9%88%E0%B9%80%E0%B8%9A%E0%B8%B2-%E0%B9%82%E0%B8%8B%E0%B8%9F%E0%B8%B5%E0%B9%88-%E0%B8%8A%E0%B8%B1%E0%B8%8D%E0%B8%8D%E0%B8%B2-%E0%B8%AB%E0%B8%A3%E0%B8%B7%E0%B8%AD-%E0%B8%A2%E0%B8%B5%E0%B8%99%E0%B8%AA%E0%B9%8C-Project-X-%E0%B9%83%E0%B8%99%E0%B8%8A%E0%B8%B8%E0%B8%94%E0%B8%99%E0%B8%B1%E0%B8%81%E0%B8%A8%E0%B8%B6%E0%B8%81%E0%B8%A9%E0%B8%B2-41.jpg");

//        final ActionBar actionBar = getActionBar();
//
//
//        actionBar.setDisplayHomeAsUpEnabled(true);
//
//        BitmapDrawable background = new BitmapDrawable(getResources(), BitmapFactory.decodeResource(getResources(), R.drawable.top_bar));
//        background.setTileModeX(android.graphics.Shader.TileMode.REPEAT);
//        actionBar.setBackgroundDrawable(background);

        setTitle(String.valueOf(lawSection));
        btnText().setVisibility(View.VISIBLE);
        btnText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (layoutText.isShown()) {
                    layoutText.setVisibility(View.GONE);
                } else {
                    layoutText.setVisibility(View.VISIBLE);
                }
            }
        });

        if (lawSection == 100) {
            setTitle("มาตรา 100");
        } else if (lawSection == 103) {
            setTitle("มาตรา 103");
        } else if (lawSection == 1) {
            setTitle("มาตรา 100");
        } else if (lawSection == 2) {
            setTitle("มาตรา 103");
        } else {
            setTitle(title);
        }

        _outletObject();
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu, menu);
//        return true;
//    }


    private void _outletObject() {
        mDownloadingDialog = new ProgressDialog(this);
        mDownloadingDialog.setMessage("Downloading file. Please wait...");
        mDownloadingDialog.setIndeterminate(false);
        mDownloadingDialog.setMax(100);
        mDownloadingDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mDownloadingDialog.setCancelable(true);

        btnPlay = (ImageView) findViewById(R.id.btn_play);
        progressBar = (SeekBar) findViewById(R.id.progressBar);
        layoutAudio = (LinearLayout) findViewById(R.id.layout_audio);
        layoutImage = (FrameLayout) findViewById(R.id.pager_container);

        SeekBar seekBar = (SeekBar) findViewById(R.id.seek);
        layoutText = (LinearLayout) findViewById(R.id.layout_text);
        tvTime = (TextView) findViewById(R.id.tv_time);


        pager = (ViewPager) findViewById(R.id.coverflow);
//        mContainer.setViewPager(pager);
        PagerAdapter adapter = new MyPagerAdapter();
        pager.setPageTransformer(true, new ZoomOutPageTransformer(true));
        pager.setAdapter(adapter);
        //Necessary or the pager will only have one extra page to show
        // make this at least however many pages you can see
//        pager.setOffscreenPageLimit(adapter.getCount());
        pager.setOffscreenPageLimit(3);
        //A little space between pages
//        pager.setPageMargin(-30);
        //If hardware acceleration is enabled, you should also remove
        // clipping on the pager for its children.
//        pager.setClipChildren(false);


        mBtnNext = (Button) findViewById(R.id.btnNext);
        mBtnPrev = (Button) findViewById(R.id.btnPrev);

        mLblTitle = (TextView) findViewById(R.id.lblTitle);
        mWebView = (TouchyWebView) findViewById(R.id.webView);
        mWebView.setBackgroundColor(0x00000000);

        if (images.isEmpty()) {
            layoutImage.setVisibility(View.GONE);
        }

        WebViewClient webClient = new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                if (url.startsWith("mailto:")) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    startActivity(intent);
                    return true;
                } else if (url.startsWith("http://") || url.startsWith("https://")) {
                    if (url.contains("line.me")) {

                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));

                        startActivity(i);
                    } else {

                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(url));
                        startActivity(intent);
                    }

                    return true;
                } else if (url.startsWith("tel:")) {
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(url));

                    //EndCallListener callListener = new EndCallListener();
                    //TelephonyManager mTM = (TelephonyManager)getContext().getSystemService(Context.TELEPHONY_SERVICE);
                    //mTM.listen(callListener, PhoneStateListener.LISTEN_CALL_STATE);

                    startActivity(intent);
                    return true;
                } else if (url.startsWith("term://")) {

                    Intent intent = new Intent(LawContentDetailActivity.this, TerminologyDetailActivity.class);

                    url = url.replace("term://", "");

                    String[] params = url.split("=");

                    if (params.length > 1) {

                        try {
                            int termID = Integer.parseInt(params[1]);

                            for (int i = 0, max = MyApplication.getInstance().terms.size(); i < max; i++) {
                                if (MyApplication.getInstance().terms.get(i).getId() == termID) {
                                    intent.putExtra("term", MyApplication.getInstance().terms.get(i));
                                    break;
                                }
                            }
                            startActivity(intent);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    return true;
                } else if (url.startsWith("termPDF://") || url.startsWith("termpdf://")) {
                    url = url.replace("termPDF://", "");
                    url = url.replace("termpdf://", "");

                    String[] params = url.split("=");

                    if (params.length > 1) {

                        try {
                            int termPDFID = Integer.parseInt(params[1]);

                            TerminologyPDF termPDF = null;

                            for (int i = 0, max = MyApplication.getInstance().termsPDF.size(); i < max; i++) {
                                if (MyApplication.getInstance().termsPDF.get(i).getId() == termPDFID) {
                                    termPDF = MyApplication.getInstance().termsPDF.get(i);
                                    break;
                                }
                            }

                            if (termPDF != null) {

                                // save preference
                                SharedPreferences sharedpreferences = getSharedPreferences(MyApplication.TAG, Context.MODE_PRIVATE);

                                String localFilepath = sharedpreferences.getString(termPDF.getLink(), null);

                                if (localFilepath != null) {
                                    File pdfFile = new File(localFilepath);

                                    if (pdfFile.exists()) {
                                        openFile(pdfFile.getAbsolutePath());
                                    } else {
                                        new DownloadFileFromURL().execute(termPDF);
                                    }
                                } else {
                                    new DownloadFileFromURL().execute(termPDF);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    return true;
                }

                return false;
            }
        };

        mWebView.setWebViewClient(webClient);

        final WebSettings settings = mWebView.getSettings();
//        settings.setTextSize(WebSettings.TextSize.SMALLER);
//        settings.setDefaultFontSize(10);
        final int def = settings.getDefaultFontSize();
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                int a = def + i;
                Log.i("asasd", a + "");
                settings.setDefaultFontSize(a);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        DisplayMetrics metrics = getResources().getDisplayMetrics();

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        //int height = size.y;

        int criteria = width;

        if (height < width) {
            criteria = height;
        }

//        settings.setDefaultFontSize((int) (Math.ceil(((criteria / metrics.density) / 320.0f) * 15 * (metrics.scaledDensity / metrics.density))));

        if (Build.VERSION.SDK_INT >= 11) mWebView.setLayerType(WebView.LAYER_TYPE_NONE, null);

        //mWebView.getSettings().setJavaScriptEnabled( true );
        //if (Build.VERSION.SDK_INT >= 11) mWebView.setLayerType(WebView.LAYER_TYPE_NONE, null);

        if (!mContent.getAudio().isEmpty()) {
            btnPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (view.isSelected()) {
                        stopPlaying();
                        view.setSelected(false);
                    } else {
                        onRadioClick(mContent.getAudio());
                        view.setSelected(true);
                    }

                }
            });
        } else {
            layoutAudio.setVisibility(View.GONE);
        }


        _updateContent();
    }

    private void _updateContent() {
        int currentPosition = _getCurrentPosition();

        if (currentPosition == 0) {
            mBtnPrev.setVisibility(View.GONE);
        } else {
            mBtnPrev.setVisibility(View.VISIBLE);
        }

        if (currentPosition == mAllContents.size() - 1) {
            mBtnNext.setVisibility(View.GONE);
        } else {
            mBtnNext.setVisibility(View.VISIBLE);
        }

        mLblTitle.setText(String.valueOf(_getCurrentPosition() + 1) + ". " + mContent.getTitle());

        String detail = "<html> " +
                "<head> " +
                //"<meta name=\"viewport\" content=\"target-densitydpi=medium-dpi\" />"+
                "<style ='text/css'> " +
                "@font-face { font-family: 'CustomFont'; font-weight: normal; font-style: normal; src: url('Font-Bold.ttf' ); } " +
                "@font-face { font-family: 'CustomFont'; font-weight: bold; font-style: normal; src: url('Font-Black.ttf' ); } " +
                "@font-face { font-family: 'CustomFont'; font-weight: bold; font-style: italic; src: url('Font-BlackItalic.ttf' ); } " +
                "@font-face { font-family: 'CustomFont'; font-weight: normal; font-style: italic; src: url('Font-BoldItalic.ttf' ); } " +
                "body {background: white; margin: 0px; font-family: 'CustomFont', Verdana, Arial; padding: " + (int) getResources().getDimension(R.dimen.little_margin_gap) + "px} " +
                "a, a:hover, a:active { color: #1E90FF; text-decoration: underline;} " +
                "</style>" +
                "</head>" +
                "<body>" + mContent.getDetail() + "</body>" +
                "</html>";

        mWebView.loadDataWithBaseURL("file:///android_asset/", detail, "text/html", "UTF-8", null);

        images.clear();
        if (!mContent.getClip().isEmpty()) {
            try {
                String image = "http://img.youtube.com/vi/" + mContent.getClip().split("v=")[1] + "/0.jpg";
                images.add(image);
            } catch (Exception e) {

            }
        }
        if (!mContent.getImage().isEmpty()) {
            images.add(mContent.getImage());
        }

        if (images.isEmpty()) {
            layoutImage.setVisibility(View.GONE);
        } else {
            layoutImage.setVisibility(View.VISIBLE);
        }


        PagerAdapter adapter = new MyPagerAdapter();
        pager.setPageTransformer(true, new ZoomOutPageTransformer(true));
        pager.setAdapter(adapter);
        //Necessary or the pager will only have one extra page to show
        // make this at least however many pages you can see
//        pager.setOffscreenPageLimit(adapter.getCount());
        pager.setOffscreenPageLimit(3);
        //A little space between pages
        pager.setPageMargin((int) getResources().getDimension(R.dimen.image_padding));
        //If hardware acceleration is enabled, you should also remove
        // clipping on the pager for its children.
        pager.setClipChildren(false);

        if (!mContent.getAudio().isEmpty()) {


            layoutAudio.setVisibility(View.VISIBLE);

            btnPlay.setSelected(false);
            try {
                handler.removeCallbacks(notification);
            } catch (Exception e) {

            }
            progressBar.setProgress(0);
            tvTime.setText("00:00");

            btnPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (view.isSelected()) {
                        stopPlaying();
                        view.setSelected(false);
                    } else {
                        onRadioClick(mContent.getAudio());
                        view.setSelected(true);
                    }

                }
            });
        } else {
            layoutAudio.setVisibility(View.GONE);
        }


        stopPlaying();

    }

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
//        if (id == R.id.new_game) {
//            if (layoutText.isShown()) {
//                layoutText.setVisibility(View.GONE);
//            } else {
//                layoutText.setVisibility(View.VISIBLE);
//            }
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    private int _getCurrentPosition() {
        int position = 0;

        for (int i = 0, max = mAllContents.size(); i < max; i++) {
            if (mAllContents.get(i).getId() == mContent.getId()) {
                position = i;
                break;
            }
        }

        return position;
    }

    public void prevContentAction(View v) {
        int currPosition = _getCurrentPosition();
        int prevPosition = currPosition - 1;

        if (currPosition == 0) {
            prevPosition = mAllContents.size() - 1;
        }

        mContent = mAllContents.get(prevPosition);

        _updateContent();


    }

    public void nextContentAction(View v) {
        int currPosition = _getCurrentPosition();
        int nextPosition = currPosition + 1;

        if (currPosition == (mAllContents.size() - 1)) {
            nextPosition = 0;
        }

        mContent = mAllContents.get(nextPosition);

        _updateContent();


    }


    /**
     * Background Async Task to download file
     */
    class DownloadFileFromURL extends AsyncTask<TerminologyPDF, String, String> {

        /**
         * Before starting background thread
         * Show Progress Bar Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDownloadingDialog.show();
            //showDialog(progress_bar_type);
        }

        /**
         * Downloading file in background thread
         */
        @Override
        protected String doInBackground(TerminologyPDF... termPDFs) {
            int count;
            if (termPDFs.length <= 0) return null;

            String filePath = null;

            try {
                TerminologyPDF termPDF = termPDFs[0];

                URL url = new URL(termPDF.getLinkPath());
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

                File fileDir = new File(strFileDir);


                if (fileDir.exists() == false) {
                    fileDir.mkdir();
                }

                filePath = strFileDir + File.separator + filename;

                OutputStream output = new FileOutputStream(filePath);

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));

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

                editor.putString(termPDF.getLink(), filePath);

                editor.commit();
            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return filePath;
        }

        /**
         * Updating progress bar
         */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage

            mDownloadingDialog.setProgress(Integer.parseInt(progress[0]));
            //pDialog.setProgress(Integer.parseInt(progress[0]));
        }

        /**
         * After completing background task
         * Dismiss the progress dialog
         **/
        @Override
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after the file was downloaded
            //dismissDialog(progress_bar_type);
            mDownloadingDialog.dismiss();


            openFile(file_url);
        }

    }

    private void openFile(String url) {
        File file = new File(url);
        Intent target = new Intent(Intent.ACTION_VIEW);
        target.setDataAndType(Uri.fromFile(file), "application/pdf");
        target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

        Intent intent = Intent.createChooser(target, "Open File");
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            // Instruct the user to install a PDF reader here, or something
            new AlertDialog.Builder(LawContentDetailActivity.this)
                    .setTitle("ไม่สามารถเปิดไฟล์ PDF ได้")
                    .setMessage("คุณต้องดาวน์โหลดโปรแกรมสำหรับอ่าน PDF ก่อน จึงจะสามารถเปิดอ่านไฟล์ได้! ต้องการดาวน์โหลดหรือไม่?")
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
                    .setNegativeButton("ไม่ต้องการ", null)
                    .show();
        }
    }

    private class MyPagerAdapter extends PagerAdapter {
        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
//            Transformation transformation = new RoundedTransformationBuilder()
//                    .cornerRadiusDp(10)
//                    .oval(false)
//                    .build();

            LayoutInflater inflater = LayoutInflater.from(LawContentDetailActivity.this);
            ViewGroup view = (ViewGroup) inflater.inflate(R.layout.pager_item,
                    container, false);
//            View view = View.inflate(LawContentDetailActivity.this, R.layout.pager_item, container);
            ImageView play = (ImageView) view.findViewById(R.id.play);
            if (images.get(position).contains("youtube")) {
                play.setVisibility(View.VISIBLE);
            } else {
                play.setVisibility(View.GONE);
            }
            play.setImageResource(R.drawable.play);
            play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent in = new Intent(LawContentDetailActivity.this, PlayerYoutube.class);
                    in.putExtra("link", mContent.getClip().split("v=")[1]);
                    startActivity(in);
                }
            });
            ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
            Picasso.with(LawContentDetailActivity.this).load(images.get(position)).into(imageView);
            container.addView(view);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent in = new Intent(LawContentDetailActivity.this, PhotoViewActivity.class);
                    in.putExtra("image", images.get(position));
                    startActivity(in);
                }
            });
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return images.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return (view == object);
        }
    }

    public void onRadioClick(String url) {

        if (!isPLAYING) {
            isPLAYING = true;
            mp = new MediaPlayer();
            try {
                mp.setDataSource(url);
                mp.prepare();
                mp.start();
                mediaFileLengthInMilliseconds = mp.getDuration();
                primarySeekBarProgressUpdater();
            } catch (IOException e) {
//                Log.e(LOG_TAG, "prepare() failed");
            }
        } else {
            isPLAYING = false;
            stopPlaying();
        }
    }

    private void stopPlaying() {
        if (mp != null) {

            mp.release();
            mp = null;
        }
    }

    @Override
    protected void onStop() {
        stopPlaying();

        super.onStop();
    }

    private void primarySeekBarProgressUpdater() {

        // math
        // construction
        // give
        // a
        // percentage
        // of
        // "was playing"/"song length"
        if (mp != null && mp.isPlaying()) {

            progressBar.setMax(mp.getDuration() / 1000);
            progressBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                    if (mp != null && b) {
                        mp.seekTo(i * 1000);
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });

            int mCurrentPosition = mp.getCurrentPosition() / 1000;
            progressBar.setProgress(mCurrentPosition);

//            progressBar.setProgress((int) (((float) mp
//                    .getCurrentPosition() / mediaFileLengthInMilliseconds) * 100)); // This

//            DecimalFormat REAL_FORMATTER = new DecimalFormat("##:##");

//            String aa = String.format("%1$02d:%1$02d",
//                    TimeUnit.MILLISECONDS.toMinutes(mp.getDuration() - mp.getCurrentPosition()),
//                    TimeUnit.MILLISECONDS.toSeconds((mp.getDuration() - mp.getCurrentPosition())) -
//                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(mp.getDuration() - mp.getCurrentPosition()))
//            );
            NumberFormat f = new DecimalFormat("00");
            long a = TimeUnit.MILLISECONDS.toMinutes(mp.getDuration() - mp.getCurrentPosition());
            long b = TimeUnit.MILLISECONDS.toSeconds((mp.getDuration() - mp.getCurrentPosition())) -
                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(mp.getDuration() - mp.getCurrentPosition()));
            tvTime.setText(f.format(a)+":"+f.format(b));


            notification = new Runnable() {
                public void run() {
                    primarySeekBarProgressUpdater();
                }
            };
            handler.postDelayed(notification, 1000);
        }
    }
}
