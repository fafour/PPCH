package th.go.nacc.nacc_law;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import th.go.nacc.nacc_law.helper.ZoomOutPageTransformer;
import th.go.nacc.nacc_law.model.News;

/**
 * Created by nontachai on 7/27/15 AD.
 */
public class NewsDetailActivity2 extends AbstractActivity {

    private int mNewsID;
    private News mNews;

    private boolean isComeFromNotification = false;

    private TextView mLblTitle, mLblDetail;

//    private Button mBtnLink;

//    private NetworkImageView mImgNews;

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
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );

        setContentView(R.layout.activity_news_detail2);

        Bundle data = getIntent().getExtras();

        if ( data != null ) {
            Log.i( "TAG", "DATA != NULL" );
            mNews = data.getParcelable( "news" );

            mNewsID = data.getInt("news_id");

            isComeFromNotification = data.getBoolean( "from_notification" );
        } else {
            Log.i( "TAG", "DATA == NULL" );
        }

//        final ActionBar actionBar = getActionBar();
//
//
//        actionBar.setDisplayHomeAsUpEnabled(true);
//
//        BitmapDrawable background = new BitmapDrawable( getResources(), BitmapFactory.decodeResource(getResources(), R.drawable.top_bar));
//        background.setTileModeX(android.graphics.Shader.TileMode.REPEAT);
//        actionBar.setBackgroundDrawable(background);



        setTitle("ข่าวสาร");

        _outletObject();

        //Log.i( "TAG", "NEWS TITLE: " +mNews.getTitle());
        if ( mNews != null ) {
            setDataToView();
        } else {
            _getData();
        }
    }

    public void refresh () {
        _getData();
    }

    private void _getData () {
        News.getNewsDetail(this, mNewsID, new News.OnGetDetailListener() {
            @Override
            public void onResult(News news, Error error) {
                if (error == null) {
                    if ( news != null ) {
                        mNews = news;

                        setDataToView();
                    }

                } else {
                    retryService(error.getMessage());
                }
            }
        });
    }

    private void _outletObject () {
        btnPlay = (ImageView) findViewById(R.id.btn_play);
        progressBar = (SeekBar) findViewById(R.id.progressBar);
        layoutAudio = (LinearLayout) findViewById(R.id.layout_audio);
        layoutImage = (FrameLayout) findViewById(R.id.pager_container);

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
        pager.setPageMargin(-50);
        //If hardware acceleration is enabled, you should also remove
        // clipping on the pager for its children.
        pager.setClipChildren(false);

        mLblTitle = (TextView) findViewById( R.id.lblTitle );
        mLblDetail = (TextView) findViewById( R.id.lblDetail );
//        mBtnLink = (Button) findViewById( R.id.btnLink );

//        mImgNews = (NetworkImageView) findViewById( R.id.imgNews );

//        Display display = getWindowManager().getDefaultDisplay();
//        Point size = new Point();
//        display.getSize(size);
//        int width = size.x;
//
//        ViewGroup.LayoutParams layoutParams = mImgNews.getLayoutParams();
//
//        layoutParams.height = (int)(width * 0.7f);
//
//        mImgNews.setLayoutParams(layoutParams);
//
//
//        mBtnLink.setVisibility( View.GONE );

    }

    private void setDataToView () {

        mp = new MediaPlayer();

        if (!mNews.getClip().isEmpty()) {
            try {
                String image = "http://img.youtube.com/vi/" + mNews.getClip().split("v=")[1] + "/0.jpg";
                images.add(image);
            } catch (Exception e) {

            }
        }
//        if (!mNews.getImage().isEmpty()) {
//            images.add(mNews.getImage());
//        }

        mLblTitle.setText( mNews.getTitle() );
        mLblDetail.setText( mNews.getDetail() );

        if ( mNews.getDetail() == null || mNews.getDetail().equals( "" ) ) {
            mLblDetail.setVisibility(View.GONE );
        } else {
            mLblDetail.setVisibility(View.VISIBLE );
        }


//        if ( mNews.getLink() == null || mNews.getLink().equals("" ) ) {
//            mBtnLink.setVisibility( View.GONE );
//        } else {
//            mBtnLink.setVisibility( View.VISIBLE );
//        }

//        if ( mNews.getImage() == null ) {
//            mImgNews.setVisibility( View.GONE );
//        } else {
//            mImgNews.setVisibility( View.VISIBLE );
//            mImgNews.setImageUrl(mNews.getImage(), MyApplication.getInstance().getmImageLoader());
//        }

        images.clear();
        if (!mNews.getClip().isEmpty()) {
            try {
                String image = "http://img.youtube.com/vi/" + mNews.getClip().split("v=")[1] + "/0.jpg";
                images.add(image);
            } catch (Exception e) {

            }
        }
        if (!mNews.getImage().isEmpty()) {
            images.add(mNews.getImage());
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

        if (!mNews.getAudio().isEmpty()) {


            layoutAudio.setVisibility(View.VISIBLE);

            btnPlay.setSelected(false);
            try {
                handler.removeCallbacks(notification);
            }catch (Exception e){

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
                        onRadioClick(mNews.getAudio());
                        view.setSelected(true);
                    }

                }
            });
        } else {
            layoutAudio.setVisibility(View.GONE);
        }


        stopPlaying();


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            if ( isComeFromNotification ) {
                Intent intent = new Intent(this, DashboardActivity.class);

                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                startActivity( intent );

                finish();
            } else {
                finish();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    public void linkAction ( View v ) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mNews.getLink()));

            startActivity(intent);
        }catch (Exception e){

        }
    }


    @Override
    public void onBackPressed() {

        if ( isComeFromNotification ) {
            Intent intent = new Intent(this, MainActivity.class);

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            startActivity( intent );
        }

        super.onBackPressed();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable( "news", mNews);
        outState.putInt( "news_id", mNewsID );
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        mNews = savedInstanceState.getParcelable( "news" );
        mNewsID = savedInstanceState.getInt( "news_id" );
    }

    private class MyPagerAdapter extends PagerAdapter {
        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
//            Transformation transformation = new RoundedTransformationBuilder()
//                    .cornerRadiusDp(10)
//                    .oval(false)
//                    .build();

            LayoutInflater inflater = LayoutInflater.from(NewsDetailActivity2.this);
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
                    Intent in = new Intent(NewsDetailActivity2.this, PlayerYoutube.class);
                    in.putExtra("link", mNews.getClip().split("v=")[1]);
                    startActivity(in);
                }
            });
            ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
            Picasso.with(NewsDetailActivity2.this).load(images.get(position)).into(imageView);
            container.addView(view);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent in = new Intent(NewsDetailActivity2.this, PhotoViewActivity.class);
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
