package th.go.nacc.nacc_law;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import uk.co.senab.photoview.PhotoView;

public class PhotoViewActivity extends AbstractActivity {

    private String NameSeed, SeedId, Img, type;
    private TextView TitleTextView;
    private ImageView ItemImageView;
    private ViewGroup viewGroup;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photoview);


        String url = getIntent().getStringExtra("image");
        final PhotoView photoView = (PhotoView) findViewById(R.id.photo);
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressbar);

        Picasso.with(this).load(url).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                progressBar.setVisibility(View.GONE);
                photoView.setImageBitmap(bitmap);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });


    }


}
