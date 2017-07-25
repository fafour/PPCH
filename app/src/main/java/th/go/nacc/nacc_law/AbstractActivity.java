package th.go.nacc.nacc_law;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import th.go.nacc.nacc_law.application.MyApplication;
import th.go.nacc.nacc_law.model.Initial;

/**
 * Created by nontachai on 7/11/15 AD.
 */
public class AbstractActivity extends AppCompatActivity {

    private ProgressDialog mProgress;

    private TextView mLblActionBarTitle;
    private ImageView btnText;
    TextView btnEdit;
    private ImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (this.getSupportActionBar() != null) {
            this.getSupportActionBar().setDisplayShowCustomEnabled(true);
            this.getSupportActionBar().setDisplayShowTitleEnabled(false);
            this.getSupportActionBar().setDisplayShowHomeEnabled(false);

            this.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

            LayoutInflater inflator = LayoutInflater.from(this);
            View v = inflator.inflate(R.layout.actionbar_custom_view, null);

            //if you need to customize anything else about the text, do it here.
            //I'm using a custom TextView with a custom font in my layout xml so all I need to do is set title
            mLblActionBarTitle = (TextView) v.findViewById(R.id.title);
             btnBack = (ImageView) v.findViewById(R.id.btn_back);
            btnText = (ImageView) v.findViewById(R.id.btn_text);
            btnEdit = (TextView)v.findViewById(R.id.btn_edit);
            btnBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();
                }
            });
            //assign the view to the actionbar
            this.getSupportActionBar().setCustomView(v, new android.support.v7.app.ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            Toolbar parent = (Toolbar) v.getParent();
            parent.setContentInsetsAbsolute(0, 0);
            /*
            int titleId = getResources().getIdentifier("action_bar_title", "id",
                    "android");
            TextView yourTextView = (TextView) findViewById(titleId);
            yourTextView.setTypeface(Typeface.createFromAsset( getAssets(), "Font-Black.ttf"));
            */
        }


        SharedPreferences sharedpreferences = getSharedPreferences(MyApplication.TAG, Context.MODE_PRIVATE);

        String initialToken = sharedpreferences.getString("initial", null);

        if (initialToken != null) {
            try {
                JSONObject responseObject = new JSONObject(initialToken);

                if (responseObject.optInt("status") == 1) {
                    Initial initial = Initial.convertToObject(responseObject.optJSONObject("data"));

                    _setDataToApplication(initial);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    protected void setTitle(String title) {
        mLblActionBarTitle.setText(title);

    }
    protected View btnBack() {
        return btnBack;
    }
    protected View btnText() {
        return btnText;
    }
    protected View btnEdit() {
        return btnEdit;
    }
    protected void disableActionBar() {
        if (this.getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
    }

    public void refresh() {
        // blank, for subclass implement
    }

    protected void _setDataToApplication(Initial initial) {
        MyApplication.getInstance().sections = initial.getSections();
        MyApplication.getInstance().contents100 = initial.getContent100();
        MyApplication.getInstance().contents103 = initial.getContent103();
        MyApplication.getInstance().pdf100 = initial.getPdf100();
        MyApplication.getInstance().pdf103 = initial.getPdf103();
        MyApplication.getInstance().terms = initial.getTerms();
        MyApplication.getInstance().termsPDF = initial.getTermsPDF();
//        MyApplication.getInstance().contents.clear();
//        MyApplication.getInstance().contents.addAll(initial.getContent100());
//        MyApplication.getInstance().contents.addAll(initial.getContent103());
//        MyApplication.getInstance().pdf.clear();
//        MyApplication.getInstance().pdf.addAll(initial.getPdf100());
//        MyApplication.getInstance().pdf.addAll(initial.getPdf103());
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

    public void retryService(String message) {

        // get prompts.xml view
        LayoutInflater li = LayoutInflater.from(this);
        View alertView = li.inflate(R.layout.alertlayout, null);

        TextView textView1 = (TextView) alertView.findViewById(R.id.textView1);

        textView1.setText(message);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(alertView);

        //final EditText userInput = (EditText) alertView.findViewById(R.id.editTextDialogUserInput);

        // set dialog message
        alertDialogBuilder
                //.setTitle( "รับเกียรติบัตร" )
                .setCancelable(false)
                .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        refresh();
                    }
                })
                .setNegativeButton("Cancel", null);

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();

        /*
        AlertDialog dialog = new AlertDialog.Builder( this )
                .setMessage(message)
                .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        refresh();
                    }
                })
                .setNegativeButton( "Cancel", null )
                .setCancelable( false )
                .show();
                */
    }

    public void simpleAlert(String message) {

        // get prompts.xml view
        LayoutInflater li = LayoutInflater.from(this);
        View alertView = li.inflate(R.layout.alertlayout, null);

        TextView textView1 = (TextView) alertView.findViewById(R.id.textView1);

        textView1.setText(message);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(alertView);

        //final EditText userInput = (EditText) alertView.findViewById(R.id.editTextDialogUserInput);

        // set dialog message
        alertDialogBuilder
                //.setTitle( "รับเกียรติบัตร" )
                .setCancelable(false)
                .setPositiveButton("OK", null);

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();

    }

    public void simpleAlertAndFinish(String message) {

        // get prompts.xml view
        LayoutInflater li = LayoutInflater.from(this);
        View alertView = li.inflate(R.layout.alertlayout, null);

        TextView textView1 = (TextView) alertView.findViewById(R.id.textView1);

        textView1.setText(message);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(alertView);

        //final EditText userInput = (EditText) alertView.findViewById(R.id.editTextDialogUserInput);

        // set dialog message
        alertDialogBuilder
                //.setTitle( "รับเกียรติบัตร" )
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();


    }
}
