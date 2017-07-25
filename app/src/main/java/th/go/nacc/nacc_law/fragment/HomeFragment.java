package th.go.nacc.nacc_law.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import th.go.nacc.nacc_law.MainActivity;
import th.go.nacc.nacc_law.R;
import th.go.nacc.nacc_law.application.MyApplication;
import th.go.nacc.nacc_law.model.Initial;


public class HomeFragment extends AbstractFragment {
    final static int FOR_MEDIA = 1;
    final static int FORCE_NONE = 0;
    final static int FORCE_SPEAKER = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        setTitle("หน้าหลัก");
        LinearLayout btn1 = (LinearLayout) view.findViewById(R.id.btn_1);
        LinearLayout btn2 = (LinearLayout) view.findViewById(R.id.btn_2);
        LinearLayout btn3 = (LinearLayout) view.findViewById(R.id.btn_3);
        LinearLayout btn4 = (LinearLayout) view.findViewById(R.id.btn_4);
        LinearLayout btn5 = (LinearLayout) view.findViewById(R.id.btn_5);
        LinearLayout btn6 = (LinearLayout) view.findViewById(R.id.btn_6);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).replaceFragmentBack(new LawFragment());
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).replaceFragmentBack(new EducationFragment());
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).replaceFragmentBack(new ThoughtFragment());
            }
        });
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).replaceFragmentBack(new LawAboutFragment());
            }
        });
        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customAlert();
//                new AlertDialog.Builder(getActivity()).setTitle("สายด่วน ป.ป.ช.")
//                        .setMessage("1205")
//                        .setPositiveButton(android.R.string.ok,
//                                new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog,
//                                                        int which) {
//                                        // continue with delete
//                                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "1205"));
//                                        startActivity(intent);
//                                    }
//                                }).show();
            }
        });
        btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).replaceFragmentBack(new AboutFragment());
            }
        });
        _getData();


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setTitle("หน้าหลัก");
        btnBack().setVisibility(View.GONE);
        if(getView() == null){
            return;
        }

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){
                    android.os.Process.killProcess(android.os.Process.myPid());
                    System.exit(1);

                    return true;
                }
                return false;
            }
        });
    }


    private void _getData() {
        Log.i("TAG", "GET DATA");

        // save preference
        SharedPreferences sharedpreferences = getActivity().getSharedPreferences(MyApplication.TAG, Context.MODE_PRIVATE);


        String initialToken = sharedpreferences.getString("initial", null);

        boolean showProgress = false;

        if (initialToken == null) {
            showProgress = true;
        }

        final boolean existsCache = showProgress;

        Initial.get(getActivity(), showProgress, new Initial.OnResponseListener() {
            @Override
            public void onGetListener(boolean isUpdated, Initial initial, Error error) {
                if (error == null) {
                    if (isUpdated) {
                        _setDataToApplication(initial);
                    }
                } else {
                    if (existsCache) {
                        retryService(error.getMessage());
                    }
                }
            }
        });
    }


    @Override
    public void onStop() {
        super.onStop();
        MyApplication.getInstance().cancelAllPendingRequests();
    }


    void customAlert() {

        // get prompts.xml view
        LayoutInflater li = LayoutInflater.from(getActivity());
        View alertView = li.inflate(R.layout.alertlayout_call, null);

//        TextView textView1 = (TextView) alertView.findViewById(R.id.textView1);
//
//        textView1.setText(message);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(alertView);

        //final EditText userInput = (EditText) alertView.findViewById(R.id.editTextDialogUserInput);

        // set dialog message
        alertDialogBuilder
                .setNegativeButton("ยกเลิก", null)
                .setPositiveButton("โทร", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {


                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "1205"));
                        startActivity(intent);
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();

    }



}
