package th.go.nacc.nacc_law.fragment;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import th.go.nacc.nacc_law.R;
import th.go.nacc.nacc_law.application.MyApplication;
import th.go.nacc.nacc_law.model.Initial;

/**
 * Created by nontachai on 7/11/15 AD.
 */
public abstract class AbstractFragment extends Fragment {

    private ProgressDialog mProgress;

    private TextView mLblActionBarTitle;
    TextView btnEdit;
    private ImageView btnBack;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (((AppCompatActivity) getActivity()).getSupportActionBar() != null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(false);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowCustomEnabled(true);

//            BitmapDrawable background = new BitmapDrawable(getResources(), BitmapFactory.decodeResource(getResources(), R.color.white));
//            ((AppCompatActivity) getActivity()).getSupportActionBar().setBackgroundDrawable(background);
            LayoutInflater inflator = LayoutInflater.from(getActivity());
            View v = inflator.inflate(R.layout.actionbar_custom_view, null);

            //if you need to customize anything else about the text, do it here.
            //I'm using a custom TextView with a custom font in my layout xml so all I need to do is set title
            mLblActionBarTitle = (TextView) v.findViewById(R.id.title);
             btnBack = (ImageView) v.findViewById(R.id.btn_back);
            btnEdit = (TextView)v.findViewById(R.id.btn_edit);
            //assign the view to the actionbar
//            ((AppCompatActivity) getActivity()).getSupportActionBar().setCustomView(v);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setCustomView(v, new android.support.v7.app.ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            Toolbar parent = (Toolbar) v.getParent();
            parent.setContentInsetsAbsolute(0, 0);
            /*
            int titleId = getResources().getIdentifier("action_bar_title", "id",
                    "android");
            TextView yourTextView = (TextView) findViewById(titleId);
            yourTextView.setTypeface(Typeface.createFromAsset( getAssets(), "Font-Black.ttf"));
            */
            btnBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getActivity().onBackPressed();
                    setTitle("หน้าหลัก");
                    btnBack.setVisibility(View.GONE);
                }
            });

        }


        SharedPreferences sharedpreferences = getActivity().getSharedPreferences(MyApplication.TAG, Context.MODE_PRIVATE);

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


//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_home, container, false);
//
//        return view;
//    }

    protected View btnBack() {
        return btnBack;
    }

    protected void setTitle(String title) {
        mLblActionBarTitle.setText(title);

    }
    protected View btnEdit() {
        return btnEdit;
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
    }

    public void showProgress(boolean isShow) {
        if (mProgress == null) {
            mProgress = new ProgressDialog(getActivity());
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
        LayoutInflater li = LayoutInflater.from(getActivity());
        View alertView = li.inflate(R.layout.alertlayout, null);

        TextView textView1 = (TextView) alertView.findViewById(R.id.textView1);

        textView1.setText(message);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

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
        LayoutInflater li = LayoutInflater.from(getActivity());
        View alertView = li.inflate(R.layout.alertlayout, null);

        TextView textView1 = (TextView) alertView.findViewById(R.id.textView1);

        textView1.setText(message);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

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
        LayoutInflater li = LayoutInflater.from(getActivity());
        View alertView = li.inflate(R.layout.alertlayout, null);

        TextView textView1 = (TextView) alertView.findViewById(R.id.textView1);

        textView1.setText(message);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

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
                        getActivity().finish();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();


    }


    @Override
    public void onResume() {
        super.onResume();

        if(getView() == null){
            return;
        }

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){
                    setTitle("หน้าหลัก");
                    btnBack.setVisibility(View.GONE);
                    getActivity().onBackPressed();
                    return true;
                }
                return false;
            }
        });
    }
}
