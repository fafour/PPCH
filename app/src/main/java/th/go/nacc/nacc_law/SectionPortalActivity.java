package th.go.nacc.nacc_law;

import android.app.ActionBar;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import th.go.nacc.nacc_law.application.MyApplication;

/**
 * Created by nontachai on 9/3/15 AD.
 */
public class SectionPortalActivity extends AbstractActivity {

    private int lawSection;

    @Override
    public void onCreate ( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );


        setContentView( R.layout.activity_section_portal );

        Bundle data = getIntent().getExtras();

        if ( data != null ) {
            lawSection = data.getInt( "law_section" );
        }


        final ActionBar actionBar = getActionBar();


        actionBar.setDisplayHomeAsUpEnabled(true);

        BitmapDrawable background = new BitmapDrawable( getResources(), BitmapFactory.decodeResource(getResources(), R.drawable.top_bar));
        background.setTileModeX(android.graphics.Shader.TileMode.REPEAT);
        actionBar.setBackgroundDrawable(background);



        if ( lawSection == 100 ) {
            setTitle( "มาตรา 100" );
        } else if ( lawSection == 103 ) {
            setTitle( "มาตรา 103" );
        }

        _outletObject();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        setContentView(R.layout.activity_section_portal);
        _outletObject();
    }


    private void _outletObject () {

    }

    public void openContentAction ( View v ) {
        Intent intent = new Intent( this, LawContentActivity.class );

        intent.putExtra("law_section", lawSection);

        startActivity(intent);
    }

    public void openExamAction ( View v ) {
        // Is exists member logged-in? If not, show registration activity otherwise show level list
        if ( MyApplication.getInstance().getMember() == null ) {
            Intent intent = new Intent(this, RegistrationActivity.class);

            intent.putExtra("law_section", lawSection);

            startActivity(intent);
        } else {
            Intent intent = new Intent( this, LawLevelListActivity.class );

            intent.putExtra("law_section", lawSection);

            startActivity( intent );
        }
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
}
