package th.go.nacc.nacc_law.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import th.go.nacc.nacc_law.EditMemberActivity;
import th.go.nacc.nacc_law.MainActivity;
import th.go.nacc.nacc_law.R;
import th.go.nacc.nacc_law.RegistrationActivity2;
import th.go.nacc.nacc_law.RegistrationDLAActivity2;
import th.go.nacc.nacc_law.application.MyApplication;
import th.go.nacc.nacc_law.da.ExaminationDA;
import th.go.nacc.nacc_law.model.Member;


public class ProfileFragment extends AbstractFragment {

    TextView cardID;
    TextView name;
    TextView lastName;
    Button btnLogout;
    private Button btnComment;
    private TextView tvType;
    private TextView tvProvince;
    private CardView layoutProfile;
    private LinearLayout provider;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        setTitle("บัญชีผู้ใช้");

        provider =  (LinearLayout) view.findViewById(R.id.provider);
        cardID = (TextView) view.findViewById(R.id.txtIDCard);
        name = (TextView) view.findViewById(R.id.txtName);
        lastName = (TextView) view.findViewById(R.id.txtLastname);
        btnLogout = (Button) view.findViewById(R.id.btn_logout);
        btnComment = (Button) view.findViewById(R.id.btn_comment);

        tvType= (TextView) view.findViewById(R.id.tv_type);
        tvProvince = (TextView) view.findViewById(R.id.tv_province);
        btnEdit().setVisibility(View.VISIBLE);
         layoutProfile = (CardView)view.findViewById(R.id.layout_profile);

        btnBack().setVisibility(View.GONE);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setTitle("บัญชีผู้ใช้");
        btnEdit().setVisibility(View.VISIBLE);
        MyApplication.getInstance().checkMemberSession();

        if (MyApplication.getInstance().getMember() == null) {
            btnLogout.setText("เข้าสู่ระบบ");
            btnLogout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), RegistrationActivity2.class);
                    startActivity(intent);
                }
            });
            btnComment.setVisibility(View.GONE);
            cardID.setText("");
            name.setText("");
            lastName.setText("");
            btnEdit().setVisibility(View.GONE);
            layoutProfile.setVisibility(View.GONE);
        } else {
            layoutProfile.setVisibility(View.VISIBLE);
            btnLogout.setText("ออกจากระบบ");
            btnComment.setVisibility(View.VISIBLE);
            cardID.setText(MyApplication.getInstance().getMember().getIdcard());
            name.setText(MyApplication.getInstance().getMember().getName());
            lastName.setText(MyApplication.getInstance().getMember().getLastname());
            if(MyApplication.getInstance().getMember().getIsDLAMember()==1){
                tvType.setText("เจ้าหน้าที่รัฐ");
                provider.setVisibility(View.GONE);
            }else{
                tvType.setText("ประชาชน");
                provider.setVisibility(View.VISIBLE);
            }

            tvProvince.setText(MyApplication.getInstance().getMember().getProvinceName().replace("null",""));

            btnEdit().setVisibility(View.VISIBLE);
            btnEdit().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent in = new Intent(getActivity(),RegistrationActivity2.class);
                    in.putExtra("member",MyApplication.getInstance().getMember());
                    startActivity(in);
                }
            });
            btnLogout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new AlertDialog.Builder(getActivity())
                            .setMessage("คุณต้องการออกจากระบบใช่หรือไม่?")
                            .setPositiveButton("ใช่", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    if (ExaminationDA.isExistExamination(getActivity())) {
                                        ExaminationDA.clearExamination(getActivity());
                                    }

                                    MyApplication.getInstance().clearMemberSession();

                                    simpleAlert("ออกจากระบบเรียบร้อยแล้ว!");
//                                    cardID.setText("");
//                                    name.setText("");
//                                    lastName.setText("");
//                                    btnLogout.setText("เข้าสู่ระบบ");
                                    onResume();
                                }
                            })
                            .setNegativeButton("ยกเลิก", null)
                            .show();
                }
            });

//            mImgMemberBox.setText("คุณ" + MyApplication.getInstance().getMember().getName());
        }
        btnComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).replaceFragmentBack(new ProfileFragmentComment());
            }
        });

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

    void memberAction() {

        LayoutInflater li = LayoutInflater.from(getActivity());
        View alertView = li.inflate(R.layout.alertlayout, null);

        TextView textView1 = (TextView) alertView.findViewById(R.id.textView1);

        textView1.setText("คุณต้องการแก้ไขข้อมูลส่วนตัว หรือออกจากระบบหรือไม่?");

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(alertView);

        //final EditText userInput = (EditText) alertView.findViewById(R.id.editTextDialogUserInput);

        // set dialog message
        alertDialogBuilder
                //.setTitle( "รับเกียรติบัตร" )
                .setCancelable(false)
                .setPositiveButton("แก้ไขข้อมูล", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(getActivity(), EditMemberActivity.class);

                        startActivity(intent);
                    }
                })
                .setNegativeButton("ออกจากระบบ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        new AlertDialog.Builder(getActivity())
                                .setMessage("คุณต้องการออกจากระบบใช่หรือไม่?")
                                .setPositiveButton("ใช่", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        if (ExaminationDA.isExistExamination(getActivity())) {
                                            ExaminationDA.clearExamination(getActivity());
                                        }

                                        MyApplication.getInstance().clearMemberSession();


                                        simpleAlert("ออกจากระบบเรียบร้อยแล้ว!");

//                                        checkMemberLoggedIn();
                                    }
                                })
                                .setNegativeButton("ยกเลิก", null)
                                .show();

                    }
                })
                .setNeutralButton("ยกเลิก", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();


    }

    public void loginAction(View v) {


        if (TextUtils.isEmpty(name.getText())
                || TextUtils.isEmpty(lastName.getText())
                || TextUtils.isEmpty(cardID.getText())) {
            simpleAlert("กรุณากรอกรหัสบัตรประชาชน ชื่อและนามสกุลให้เรียบร้อย และตรวจสอบให้ถูกต้อง");
            return;
        }

        if (cardID.getText().toString().length() < 13) {
            simpleAlert("กรุณากรอกรหัสบัตรประชาชน 13 หลักให้ครบถ้วน!");
            return;
        }

        Member.login(getActivity(), name.getText().toString(), lastName.getText().toString(), cardID.getText().toString(), new Member.OnLoginListener() {
            @Override
            public void loginResult(final Member member, Error error) {
                if (error == null) {
                    if (member == null) {
                        // alert to action
                        LayoutInflater li = LayoutInflater.from(getActivity());
                        View alertView = li.inflate(R.layout.alertlayout, null);

                        TextView textView1 = (TextView) alertView.findViewById(R.id.textView1);

                        textView1.setText("สมัครสมาชิก?");

                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

                        // set prompts.xml to alertdialog builder
                        alertDialogBuilder.setView(alertView);

                        //final EditText userInput = (EditText) alertView.findViewById(R.id.editTextDialogUserInput);

                        // set dialog message
                        alertDialogBuilder
                                .setCancelable(false)
                                .setPositiveButton("ใช่", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Member member = new Member();
                                        member.setName( name.getText().toString() );
                                        member.setLastname(lastName.getText().toString());
                                        member.setIdcard( cardID.getText().toString() );
                                        member.setIsDLAMember( 1 );

                                        Intent intent = new Intent ( getActivity(), RegistrationDLAActivity2.class );
                                        intent.putExtra( "member", member );

                                        startActivity(intent);


                                    }
                                })
                                .setNegativeButton("ไม่ใช่", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        // send register with is_dla_member

                                    }
                                });

                        // create alert dialog
                        AlertDialog alertDialog = alertDialogBuilder.create();

                        // show it
                        alertDialog.show();

                        /*
                        AlertDialog dialog = new AlertDialog.Builder( RegistrationActivity.this )
                                .setMessage("คุณเป็นสมาชิก (หรือคู่สมรสของสมาชิก) ขององค์กรปกครองส่วนท้องถิ่นหรือไม่?")
                                .setPositiveButton("ใช่", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        // open new page for specify detail of dla organization
                                        fillInDLADetail();
                                    }
                                })
                                .setNegativeButton("ไม่ใช่", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        // send register with is_dla_member
                                        register ( false );
                                    }
                                })
                                .setCancelable( false )
                                .show();
                                */

                    } else {
                        /*
                        // alert for ask to edit information
                        AlertDialog dialog = new AlertDialog.Builder( RegistrationActivity.this )
                                .setMessage( "เข้าสู่ระบบเรียบร้อยแล้ว คุณต้องการแก้ไขข้อมูลส่วนตัวหรือไม่?")
                                .setPositiveButton("ต้องการ", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                })
                                .setNegativeButton("ไม่ต้องการ", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        finish();

                                        // start examination level list
                                        Intent intent = new Intent( RegistrationActivity.this, LawLevelListActivity.class );

                                        intent.putExtra( "law_section", lawSection );

                                        startActivity(intent);
                                    }
                                })
                                .show();

                        */


                    }
                } else {
                    simpleAlert(error.getMessage());
                }
            }
        });

    }
}
