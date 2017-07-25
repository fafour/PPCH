//package th.go.nacc.nacc_law;
//
//import android.content.Intent;
//import android.graphics.Point;
//import android.net.Uri;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.Display;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.TextView;
//
//import com.android.volley.toolbox.NetworkImageView;
//
//import th.go.nacc.nacc_law.application.MyApplication;
//import th.go.nacc.nacc_law.model.News;
//
///**
// * Created by nontachai on 7/27/15 AD.
// */
//public class NewsDetailActivity extends AbstractActivity {
//
//    private int mNewsID;
//    private News mNews;
//
//    private boolean isComeFromNotification = false;
//
//    private TextView mLblTitle, mLblDetail;
//
//    private Button mBtnLink;
//
//    private NetworkImageView mImgNews;
//
//
//
//    @Override
//    protected void onCreate ( Bundle savedInstanceState ) {
//        super.onCreate( savedInstanceState );
//
//        setContentView(R.layout.activity_news_detail);
//
//        Bundle data = getIntent().getExtras();
//
//        if ( data != null ) {
//            Log.i( "TAG", "DATA != NULL" );
//            mNews = data.getParcelable( "news" );
//
//            mNewsID = data.getInt("news_id");
//
//            isComeFromNotification = data.getBoolean( "from_notification" );
//        } else {
//            Log.i( "TAG", "DATA == NULL" );
//        }
//
////        final ActionBar actionBar = getActionBar();
////
////
////        actionBar.setDisplayHomeAsUpEnabled(true);
////
////        BitmapDrawable background = new BitmapDrawable( getResources(), BitmapFactory.decodeResource(getResources(), R.drawable.top_bar));
////        background.setTileModeX(android.graphics.Shader.TileMode.REPEAT);
////        actionBar.setBackgroundDrawable(background);
//
//
//
//        setTitle("ข่าวสารประชาสัมพันธ์");
//
//        _outletObject();
//
//        //Log.i( "TAG", "NEWS TITLE: " +mNews.getTitle());
//        if ( mNews != null ) {
//            setDataToView();
//        } else {
//            _getData();
//        }
//    }
//
//    public void refresh () {
//        _getData();
//    }
//
//    private void _getData () {
//        News.getNewsDetail(this, mNewsID, new News.OnGetDetailListener() {
//            @Override
//            public void onResult(News news, Error error) {
//                if (error == null) {
//                    if ( news != null ) {
//                        mNews = news;
//
//                        setDataToView();
//                    }
//
//                } else {
//                    retryService(error.getMessage());
//                }
//            }
//        });
//    }
//
//    private void _outletObject () {
//        mLblTitle = (TextView) findViewById( R.id.lblTitle );
//        mLblDetail = (TextView) findViewById( R.id.lblDetail );
//        mBtnLink = (Button) findViewById( R.id.btnLink );
//
//        mImgNews = (NetworkImageView) findViewById( R.id.imgNews );
//
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
//
//    }
//
//    private void setDataToView () {
//
//        mLblTitle.setText( mNews.getTitle() );
//        mLblDetail.setText( mNews.getDetail() );
//
//        if ( mNews.getDetail() == null || mNews.getDetail().equals( "" ) ) {
//            mLblDetail.setVisibility(View.GONE );
//        } else {
//            mLblDetail.setVisibility(View.VISIBLE );
//        }
//
//
//        if ( mNews.getLink() == null || mNews.getLink().equals("" ) ) {
//            mBtnLink.setVisibility( View.GONE );
//        } else {
//            mBtnLink.setVisibility( View.VISIBLE );
//        }
//
//        if ( mNews.getImage() == null ) {
//            mImgNews.setVisibility( View.GONE );
//        } else {
//            mImgNews.setVisibility( View.VISIBLE );
//            mImgNews.setImageUrl(mNews.getImage(), MyApplication.getInstance().getmImageLoader());
//        }
//
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        if (id == android.R.id.home) {
//            if ( isComeFromNotification ) {
//                Intent intent = new Intent(this, DashboardActivity.class);
//
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
//                startActivity( intent );
//
//                finish();
//            } else {
//                finish();
//            }
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
//
//    public void linkAction ( View v ) {
//        Intent intent = new Intent( Intent.ACTION_VIEW, Uri.parse(mNews.getLink()));
//
//        startActivity( intent );
//    }
//
//
//    @Override
//    public void onBackPressed() {
//
//        if ( isComeFromNotification ) {
//            Intent intent = new Intent(this, MainActivity.class);
//
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
//            startActivity( intent );
//        }
//
//        super.onBackPressed();
//    }
//
//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//
//        outState.putParcelable( "news", mNews);
//        outState.putInt( "news_id", mNewsID );
//    }
//
//    @Override
//    protected void onRestoreInstanceState(Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//
//        mNews = savedInstanceState.getParcelable( "news" );
//        mNewsID = savedInstanceState.getInt( "news_id" );
//    }
//}
