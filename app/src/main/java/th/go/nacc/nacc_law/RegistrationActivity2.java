package th.go.nacc.nacc_law;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import th.go.nacc.nacc_law.application.MyApplication;
import th.go.nacc.nacc_law.model.Member;
import th.go.nacc.nacc_law.model.Type;
import th.go.nacc.nacc_law.utils.JsonParser;
import th.go.nacc.nacc_law.utils.ServiceConnection;

/**
 * Created by nontachai on 7/17/15 AD.
 */
public class RegistrationActivity2 extends AbstractActivity {

    private EditText mTxtName, mTxtLastname, mTxtIDCard;

    private Button btnRegister;
    private TextView btnType;
    private TextView btnProvince;
    private LinearLayout layoutProvince;
    private TextView btnSubonate;
    private LinearLayout layoutSubonate;
    private LinearLayout layoutDistrict;
    private LinearLayout layoutName;
    private LinearLayout layoutSubonate2;
    private LinearLayout layoutPosition;
    private LinearLayout layoutMinistry;
    private LinearLayout layoutDepartment;
    private LinearLayout layoutEnterprice;
    private LinearLayout layoutFree;
    private TextView btnSubonate2;
    private TextView btnPosition;
    String positionType = "1";
    Type type;
    private TextView btnDistrict;
    int provinceID = -1;
    private TextView btnListName;
    int amphurID = -1;
    int positionID = -1;
    int subonate2 = -1;
    int subonateOrigin = -1;
    private TextView btnDepartMent;
    private TextView btnEnterprise;
    String ministry = "0";
    private TextView btnMinistry;
    private Member mMember;
    int listnameID = -1;
    String typeRoot = "0";
    int subonate = -1;
    String other = "";


    ServiceConnection serviceConnection;
    private ArrayList<Type> listOrganize = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_registration);

        serviceConnection = new ServiceConnection(this);
        Bundle data = getIntent().getExtras();

        if (data != null) {

            mMember = data.getParcelable("member");

            setTitle("บัญชีผู้ใช้");
        }else{
            setTitle("ลงทะเบียน/เข้าสู่ระบบ");
        }

        _outletObject();
    }


    private void _outletObject() {
        mTxtName = (EditText) findViewById(R.id.txtName);
        mTxtLastname = (EditText) findViewById(R.id.txtLastname);
        mTxtIDCard = (EditText) findViewById(R.id.txtIDCard);
        btnType = (TextView) findViewById(R.id.btn_type);
        btnProvince = (TextView) findViewById(R.id.btn_province);
        layoutProvince = (LinearLayout) findViewById(R.id.layout_province);
        btnSubonate = (TextView) findViewById(R.id.btn_subonate);
        btnSubonate2 = (TextView) findViewById(R.id.btn_subonate2);
        btnPosition = (TextView) findViewById(R.id.btn_position);
        btnDistrict = (TextView) findViewById(R.id.btn_district);
        btnListName = (TextView) findViewById(R.id.btn_list_name);
        btnDepartMent = (TextView) findViewById(R.id.btn_department);
        btnEnterprise = (TextView) findViewById(R.id.btn_enterprise);
        btnMinistry = (TextView) findViewById(R.id.btn_ministry);

        btnRegister = (Button) findViewById(R.id.btn_register);

        layoutSubonate = (LinearLayout) findViewById(R.id.layout_subonate);

        layoutSubonate2 = (LinearLayout) findViewById(R.id.layout_subonate2);
        layoutDistrict = (LinearLayout) findViewById(R.id.layout_district);
        layoutName = (LinearLayout) findViewById(R.id.layout_list_name);
        layoutPosition = (LinearLayout) findViewById(R.id.layout_position);
        layoutMinistry = (LinearLayout) findViewById(R.id.layout_ministry);
        layoutDepartment = (LinearLayout) findViewById(R.id.layout_department);
        layoutEnterprice = (LinearLayout) findViewById(R.id.layout_enterprise);
        layoutFree = (LinearLayout) findViewById(R.id.layout_free);

        layoutSubonate.setVisibility(View.GONE);
        layoutSubonate2.setVisibility(View.GONE);
        layoutDistrict.setVisibility(View.GONE);
        layoutName.setVisibility(View.GONE);
        layoutPosition.setVisibility(View.GONE);
        layoutMinistry.setVisibility(View.GONE);
        layoutDepartment.setVisibility(View.GONE);
        layoutEnterprice.setVisibility(View.GONE);
        layoutFree.setVisibility(View.GONE);

        if(mMember==null){
            btnRegister.setBackgroundResource(R.drawable.bg_rec_green);
            btnRegister.setText("ลงทะเบียน/เข้าสู่ระบบ");
        }else{
            btnRegister.setBackgroundResource(R.drawable.bg_rec_violent);
            btnRegister.setText("บันทึกข้อมูล");
        }

        //mTxtName.setText("นนทชัย");
        //mTxtLastname.setText("ทรัพย์ทวีพงศ์");
        //mTxtIDCard.setText("2200100021748");

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerAction();
            }
        });

        getOrganize();
    }
    @Override
    public void onResume() {
        super.onResume();
        btnClick();
    }


    void reset() {
        layoutSubonate.setVisibility(View.GONE);
        layoutSubonate2.setVisibility(View.GONE);
        layoutDistrict.setVisibility(View.GONE);
        layoutName.setVisibility(View.GONE);
        layoutPosition.setVisibility(View.GONE);
        layoutMinistry.setVisibility(View.GONE);
        layoutDepartment.setVisibility(View.GONE);
        layoutEnterprice.setVisibility(View.GONE);
        layoutFree.setVisibility(View.GONE);
    }

    void btnClick(){
        btnType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(RegistrationActivity2.this, RegisterTypeActivity.class);
                in.putExtra("type", 0);
                startActivityForResult(in, 1111);

                provinceID = -1;
                amphurID = -1;
                positionID = -1;
                ministry = "0";
                subonate2 = -1;
                subonateOrigin = -1;
                subonate =-1;
                listnameID = -1;

                btnProvince.setText("จังหวัด");
                btnSubonate.setText("สังกัด");
                btnSubonate2.setText("สังกัด(ย่อย)");


            }
        });
        btnProvince.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(RegistrationActivity2.this, RegisterTypeActivity.class);
                in.putExtra("type", 1);
                startActivityForResult(in, 1112);

                amphurID = -1;
                listnameID = -1;

                btnDistrict.setText("อำเภอ");
                btnListName.setText("รายชื่อ อปท");
            }
        });
        btnSubonate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(RegistrationActivity2.this, RegisterTypeActivity.class);
                in.putExtra("type", 2);
                startActivityForResult(in, 1113);

                provinceID = -1;
                amphurID = -1;
                positionID = -1;
                ministry = "0";
                subonate2 = -1;
                subonateOrigin = -1;
                listnameID = -1;

                btnProvince.setText("จังหวัด");
                btnSubonate2.setText("สังกัด(ย่อย)");
                btnEnterprise.setText("รัฐวิสาหกิจ");

                btnDistrict.setText("อำเภอ");
                btnListName.setText("รายชื่อ อปท");
                btnPosition.setText("ตำแหน่ง");
                btnDepartMent.setText("กรม");


            }
        });
        btnSubonate2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(RegistrationActivity2.this, RegisterTypeActivity.class);
                in.putExtra("type", 3);
                in.putExtra("sub", type.SubType);
                startActivityForResult(in, 1114);

                provinceID = -1;
                amphurID = -1;
                positionID = -1;
                ministry = "0";
                listnameID = -1;

                btnDepartMent.setText("กรม");
                btnPosition.setText("ตำแหน่ง");
                btnDistrict.setText("อำเภอ");
                btnListName.setText("รายชื่อ อปท");
                btnProvince.setText("จังหวัด");

            }
        });
        btnPosition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(RegistrationActivity2.this, RegisterTypeActivity.class);
                in.putExtra("type", 4);
                in.putExtra("positionType", positionType);
                startActivityForResult(in, 1115);

            }
        });
        btnDistrict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(RegistrationActivity2.this, RegisterTypeActivity.class);
                in.putExtra("type", 5);
                in.putExtra("provinceID", provinceID);
                startActivityForResult(in, 1116);

                listnameID = -1;
                btnListName.setText("รายชื่อ อปท");


            }
        });

        btnListName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(RegistrationActivity2.this, RegisterTypeActivity.class);
                in.putExtra("type", 6);
                in.putExtra("amphurID", amphurID);
                in.putExtra("subonate2", subonate2);
                startActivityForResult(in, 1117);

            }
        });
        btnEnterprise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(RegistrationActivity2.this, RegisterTypeActivity.class);
                in.putExtra("type", 7);
                in.putExtra("sub", type.SubType);
                startActivityForResult(in, 1118);
            }
        });
        btnDepartMent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(RegistrationActivity2.this, RegisterTypeActivity.class);
                in.putExtra("type", 8);
                in.putExtra("ministry", ministry);
                startActivityForResult(in, 1119);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1111) {
            if (resultCode == RESULT_OK) {
                Type type = (Type) data.getSerializableExtra("model");
                reset();
                if (type.id.equals("0")) {
                    layoutProvince.setVisibility(View.VISIBLE);
                    layoutSubonate.setVisibility(View.GONE);
                } else {
                    layoutSubonate.setVisibility(View.VISIBLE);
                    layoutProvince.setVisibility(View.GONE);
                }
                typeRoot = type.id;
                btnType.setText(type.Name);
            }
        } else if (requestCode == 1112) {
            if (resultCode == RESULT_OK) {
                Type type = (Type) data.getSerializableExtra("model");

                layoutProvince.setVisibility(View.VISIBLE);
                btnProvince.setText(type.Name);
                provinceID = Integer.parseInt(type.id);
            }
        } else if (requestCode == 1113) {
            if (resultCode == RESULT_OK) {
                type = (Type) data.getSerializableExtra("model");
                layoutSubonate.setVisibility(View.VISIBLE);
                if (type.Name.contains("ส่วนท้องถิ่น")) {
                    positionType = "1";
                    layoutSubonate2.setVisibility(View.VISIBLE);
                    layoutEnterprice.setVisibility(View.GONE);
                } else if (type.Name.equals("กระทรวง")) {
                    positionType = "2";
                    reset();
                    layoutSubonate.setVisibility(View.VISIBLE);
                    layoutSubonate2.setVisibility(View.VISIBLE);
                    layoutEnterprice.setVisibility(View.GONE);
                    layoutProvince.setVisibility(View.GONE);

                } else if (type.Name.contains("รัฐวิสาหกิจ")) {
                    reset();
                    layoutSubonate.setVisibility(View.VISIBLE);
                    layoutEnterprice.setVisibility(View.VISIBLE);
                    layoutProvince.setVisibility(View.GONE);

                } else if (type.Name.contains("อื่นๆ")) {
                    reset();
                    layoutSubonate.setVisibility(View.VISIBLE);
                    other();
                    layoutProvince.setVisibility(View.GONE);
                } else {
                    reset();
                    layoutSubonate.setVisibility(View.VISIBLE);
                    layoutSubonate2.setVisibility(View.VISIBLE);
                    layoutProvince.setVisibility(View.GONE);
                }

                layoutDepartment.setVisibility(View.GONE);
                btnSubonate.setText(type.Name);
                subonate = Integer.parseInt(type.id);

            }
        } else if (requestCode == 1114) {
            if (resultCode == RESULT_OK) {
                Type type = (Type) data.getSerializableExtra("model");
                if (type.Name.contains("ส่วนจังหวัด")) {

                    layoutPosition.setVisibility(View.VISIBLE);
                    layoutProvince.setVisibility(View.VISIBLE);
                    layoutDistrict.setVisibility(View.GONE);
                    layoutName.setVisibility(View.GONE);
                } else if (type.Name.contains("ส่วนตำบล") || type.Name.contains("เทศบาล")) {
                    layoutSubonate2.setVisibility(View.VISIBLE);
                    layoutPosition.setVisibility(View.VISIBLE);
                    layoutProvince.setVisibility(View.VISIBLE);
                    layoutDistrict.setVisibility(View.VISIBLE);
                    layoutName.setVisibility(View.VISIBLE);
                } else {
                    if (this.type != null && this.type.Name.equals("กระทรวง")) {
                        layoutPosition.setVisibility(View.VISIBLE);
                        ministry = type.id;
                        subonate2 = Integer.parseInt(type.id);
                        subonateOrigin = Integer.parseInt(type.id);
                        layoutDepartment.setVisibility(View.VISIBLE);
                        Log.i("กระทรวง", type.Name + "  " + type.id);
                    } else {
                        layoutPosition.setVisibility(View.GONE);
                        layoutProvince.setVisibility(View.GONE);
                        layoutDistrict.setVisibility(View.GONE);
                        layoutName.setVisibility(View.GONE);
                        layoutDepartment.setVisibility(View.GONE);
                    }
                    layoutSubonate2.setVisibility(View.VISIBLE);
                }


                subonate2 = Integer.parseInt(type.id);

                layoutSubonate2.setVisibility(View.VISIBLE);
                btnSubonate2.setText(type.Name);
                subonateOrigin = Integer.parseInt(type.id);
            }
        } else if (requestCode == 1115) {
            if (resultCode == RESULT_OK) {
                Type type = (Type) data.getSerializableExtra("model");
                layoutPosition.setVisibility(View.VISIBLE);
                btnPosition.setText(type.Name);
                positionID = Integer.parseInt(type.id)+1;
            }
        } else if (requestCode == 1116) {
            if (resultCode == RESULT_OK) {
                Type type = (Type) data.getSerializableExtra("model");
                layoutDistrict.setVisibility(View.VISIBLE);
                btnDistrict.setText(type.Name);
                amphurID = Integer.parseInt(type.id);

            }
        } else if (requestCode == 1117) {
            if (resultCode == RESULT_OK) {
                Type type = (Type) data.getSerializableExtra("model");
                layoutName.setVisibility(View.VISIBLE);
                listnameID = Integer.parseInt(type.id);
                btnListName.setText(type.Name);

            }
        } else if (requestCode == 1118) {
            if (resultCode == RESULT_OK) {
                Type type = (Type) data.getSerializableExtra("model");

                layoutEnterprice.setVisibility(View.VISIBLE);
                btnEnterprise.setText(type.Name);
                subonateOrigin = Integer.parseInt(type.id);
                subonate2 = Integer.parseInt(type.id);
            }
        } else if (requestCode == 1119) {
            if (resultCode == RESULT_OK) {
                Type type = (Type) data.getSerializableExtra("model");
//                Log.i("กรม",type.Name);
                Log.i("กรม", type.Name + "  " + type.id);
                ministry = type.id;
                btnDepartMent.setText(type.Name);
            }
        }
    }

    public void registerAction() {
        if (mMember == null) {
            mMember = new Member();
        }
        if (TextUtils.isEmpty(mTxtName.getText())
                || TextUtils.isEmpty(mTxtLastname.getText())
                || TextUtils.isEmpty(mTxtIDCard.getText())) {
            simpleAlert("กรุณากรอกรหัสบัตรประชาชน ชื่อและนามสกุลให้เรียบร้อย และตรวจสอบให้ถูกต้อง");
            return;
        }

        if (mTxtIDCard.getText().toString().length() < 13) {
            simpleAlert("กรุณากรอกรหัสบัตรประชาชน 13 หลักให้ครบถ้วน!");
            return;
        }


        if (subonate == 0) {
            if (subonate2 == 1) {

                if (provinceID == -1) {
                    simpleAlert("กรุณาเลือกจังหวัด");
                    return;
                }
                if (positionID == -1) {
                    simpleAlert("กรุณาเลือกตำแหน่ง");
                    return;
                }
            }

            if (subonate2 > 1 && subonate2 < 6) {

                if (listnameID == 0) {
                    simpleAlert("กรุณาเลือกรายชื่อ อปท");
                    return;
                }

                if (listnameID == -1) {
                    simpleAlert("กรุณาเลือกรายชื่อ อปท");
                    return;
                }
                if (provinceID == -1) {
                    simpleAlert("กรุณาเลือกจังหวัด");
                    return;
                }
                if (positionID == -1) {
                    simpleAlert("กรุณาเลือกตำแหน่ง");
                    return;
                }
            }
        }
//        if (subonate2 == 0) {
//
//        } else if (subonate2 < 5) {
//            if (provinceID == -1) {
//                simpleAlert("กรุณาเลือกจังหวัด");
//                return;
//            }
//        }


        if (subonate == 1) {
            if (positionID == -1) {
                simpleAlert("กรุณาเลือกตำแหน่ง");
                return;
            }
            if (subonate2 == -1) {
                {
                    simpleAlert("กรุณาเลือกสังกัดย่อย");
                    return;
                }
            }
        }

        if (subonate == 2 || subonate == 3) {
            if (subonate2 == -1) {
                {
                    simpleAlert("กรุณาเลือกสังกัดย่อย");
                    return;
                }
            }

        }

        if (subonate == 4) {
            if (subonateOrigin == -1) {
                {
                    simpleAlert("กรุณาเลือกรัฐวิสาหกิจ");
                    return;
                }
            }

        }


        if(btnType.getText().equals("เจ้าหน้าที่ของรัฐ") ||btnType.getText().equals("คู่สมรสของเจ้าหน้าที่ของรัฐ") ){
            if (subonate2 == -1) {
                {
                    simpleAlert("กรุณาเลือกสังกัดย่อย");
                    return;
                }
            }

        }else {
            if (provinceID == -1) {
                simpleAlert("กรุณาเลือกจังหวัด");
                return;
            }

        }

        if (typeRoot.equals("0")) {
            mMember.setIsDLAMember(0);
        } else {
            if (subonate2 == -1) {
                {
                    simpleAlert("กรุณาเลือกสังกัดย่อย");
                }
            }
            mMember.setIsDLAMember(1);
        }

        mMember.setName(mTxtName.getText().toString());
        mMember.setLastname(mTxtLastname.getText().toString());
        mMember.setIdcard(mTxtIDCard.getText().toString());
        mMember.setAmphur(amphurID);

        mMember.setPosition("");

        mMember.setJobTitle(positionID);



        if (btnSubonate2.getText().toString().equals("องค์การบริหารส่วนจังหวัด")) {
            mMember.setSubordinate(1);
        } else if (btnSubonate2.getText().toString().equals("เทศบาลนคร")) {
            mMember.setSubordinate(2);
        } else if (btnSubonate2.getText().toString().equals("เทศบาลเมือง")) {
            mMember.setSubordinate(3);
        } else if (btnSubonate2.getText().toString().equals("เทศบาลตำบล")) {
            mMember.setSubordinate(4);
        } else if (btnSubonate2.getText().toString().equals("องค์การบริหารส่วนตำบล")) {
            mMember.setSubordinate(5);
        } else if (btnSubonate2.getText().toString().equals("กรุงเทพมหานคร")) {
            mMember.setSubordinate(6);
        } else if (btnSubonate2.getText().toString().equals("เมืองพัทยา")) {
            mMember.setSubordinate(7);
        } else {
            mMember.setSubordinate(subonateOrigin);
        }


//        if (subonate2 < 5) { // not bangkok & pattaya
//            mMember.setSubordinateProvince(provinceID);
//
//
//            mMember.setSubordinateItem(amphurID);
//
//
//        }
        mMember.setSubordinateProvince(provinceID);
        mMember.setSubordinateItem(listnameID);


//        if(btnSubonate2.getText().toString().equals("อื่นๆ")){
//            if(other.isEmpty()){
//                simpleAlert("กรุณากรอกสังกัดอื่นๆ");
//               return;
//            }
//        }
        mMember.setSubordinateOther(other);
        //mMember.setIsSpouse(mChkSpouse.isChecked() ? 1 : 0);


        if (typeRoot.equals("1")) {
            mMember.setIsSpouse(1);
        } else if (typeRoot.equals("2")) {

            mMember.setIsSpouse(0);
        }
        mMember.setMinistryID(ministry);

        if (mMember.getId() == 0) {
            Member.register(this, mMember, new Member.OnRegisterListener() {
                @Override
                public void registerResult(Member member, Error error) {
                    if (error == null) {

                        // get prompts.xml view
                        LayoutInflater li = LayoutInflater.from(RegistrationActivity2.this);
                        View alertView = li.inflate(R.layout.alertlayout, null);

                        TextView textView1 = (TextView) alertView.findViewById(R.id.textView1);

                        textView1.setText("ลงทะเบียนเรียบร้อย คุณสามารถเข้าทำแบบทดสอบได้แล้ว");

                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(RegistrationActivity2.this);

                        // set prompts.xml to alertdialog builder
                        alertDialogBuilder.setView(alertView);

                        //final EditText userInput = (EditText) alertView.findViewById(R.id.editTextDialogUserInput);

                        // set dialog message
                        alertDialogBuilder
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        setResult(RESULT_OK);
                                        finish();
                                    }
                                });

                        // create alert dialog
                        AlertDialog alertDialog = alertDialogBuilder.create();

                        // show it
                        alertDialog.show();

                    /*
                    AlertDialog dialog = new AlertDialog.Builder(RegistrationDLAActivity.this)
                            .setMessage("ลงทะเบียนเรียบร้อย คุณสามารถเข้าทำแบบทดสอบได้แล้ว")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    finish();

                                    Intent intent = new Intent(RegistrationDLAActivity.this, LawLevelListActivity.class);

                                    intent.putExtra("law_section", lawSection);

                                    startActivity(intent);
                                }
                            })
                            .setCancelable(false)
                            .show();*/

                    } else {
                        simpleAlert(error.getMessage());
                    }
                }
            });
        } else {

            Member.updateDLAInfo(this, mMember, new Member.OnUpdateListener() {
                @Override
                public void updateResult(Member member, Error error) {
                    if (error == null) {
                        LayoutInflater li = LayoutInflater.from(RegistrationActivity2.this);
                        View promptsView = li.inflate(R.layout.alertlayout, null);

                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(RegistrationActivity2.this);

                        // set prompts.xml to alertdialog builder
                        alertDialogBuilder.setView(promptsView);

                        TextView lbl = (TextView) promptsView.findViewById(R.id.textView1);

                        lbl.setText("แก้ไขข้อมูลเรียบร้อยแล้ว");
                        // set dialog message
                        alertDialogBuilder
                                .setCancelable(false)
                                .setPositiveButton("ตกลง",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                setResult(RESULT_OK);
                                                finish();
                                            }
                                        });

                        // create alert dialog
                        AlertDialog alertDialog = alertDialogBuilder.create();

                        // show it
                        alertDialog.show();
                    } else {
                        simpleAlert(error.getMessage());
                    }
                }
            });
        }
    }

    void other() {
        LayoutInflater li = LayoutInflater.from(RegistrationActivity2.this);
        View promptsView = li.inflate(R.layout.alert_other, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(RegistrationActivity2.this);
        alertDialogBuilder.setTitle("สังกัดอื่นๆ");
        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final EditText lbl = (EditText) promptsView.findViewById(R.id.edt_name);

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("ตกลง",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                other = lbl.getText().toString();
                                if(other.isEmpty()){
                                    simpleAlert("กรุณากรอกสังกัดอื่นๆ");

                                }else{
                                    subonate2 = 0;
                                    subonateOrigin = 0;
                                    registerAction();
                                }

//                                finish();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    void getOrganize() {
        serviceConnection.get(true, MyApplication.getInstance().URL_ORGANIZATION, new ServiceConnection.CallBackListener() {
            @Override
            public void callback(String result) {
                listOrganize = JsonParser.parseType(result);
                if (mMember != null) {
                    mTxtName.setText(mMember.getName());
                    mTxtLastname.setText(mMember.getLastname());
                    mTxtIDCard.setText(mMember.getIdcard());
                    subonate2 = mMember.getSubordinate();

//                    amphurID = mMember.getSubordinateItem();
//
//                    listnameID = mMember.getSubordinateItem();


                    if (mMember.getIsDLAMember() == 1) {


                        if (mMember.getIsSpouse() == 1) {
                            btnType.setText("เจ้าหน้าที่ของรัฐ");
                            typeRoot = "1";
                        } else {
                            btnType.setText("คู่สมรสของเจ้าหน้าที่ของรัฐ");
                            typeRoot = "2";
                        }


                        for (Type type : listOrganize) {

                            Log.i("AAA", type.Name + " " + mMember.getSubname());
                            if (type.Name.equals(mMember.getSubname())) {

                                RegistrationActivity2.this.type = type;

                                break;
                            } else if (type.Name.equals(mMember.getOrgName())) {
                                RegistrationActivity2.this.type = type;

                                break;
                            }


                        }

                        if (mMember.getOrgName().contains("ส่วนท้องถิ่น")) {
                            positionType = "1";
                            layoutSubonate2.setVisibility(View.VISIBLE);
                            btnPosition.setText(mMember.getJobtitleName());
                            btnProvince.setText(mMember.getProvinceName());
                            provinceID = mMember.getSubordinateProvince();
                            amphurID = mMember.getAmphurId();
                            listnameID = mMember.getSubordinateItem();
                            positionID = mMember.getJobTitle();
                            subonate2 = mMember.getSubordinate();

                            btnDistrict.setText(mMember.getThumbonName());
                            btnListName.setText(mMember.getAmphurName());

                        } else if (mMember.getOrgName().equals("กระทรวง")) {
                            positionType = "2";
                            positionID = mMember.getJobTitle();
                            btnPosition.setText(mMember.getJobtitleName());
//                    reset();
                            subonate2 = mMember.getSubordinate();
                            btnPosition.setText(mMember.getJobtitleName());
                            provinceID = mMember.getSubordinateProvince();
                            ministry = mMember.getMinistryID();
                            layoutSubonate.setVisibility(View.VISIBLE);
                            layoutSubonate2.setVisibility(View.VISIBLE);

                            btnDepartMent.setText(mMember.getMinistryName());


                        } else if (mMember.getOrgName().contains("รัฐวิสาหกิจ")) {
//                    reset();
                            subonateOrigin = mMember.getSubordinate();
                            subonate2 = mMember.getSubordinate();
                            layoutSubonate.setVisibility(View.VISIBLE);
                            layoutEnterprice.setVisibility(View.VISIBLE);
                            layoutSubonate2.setVisibility(View.GONE);
                            btnEnterprise.setText(mMember.getSubname());

                        } else if (!mMember.getSubordinateOther().isEmpty()) {
                            subonate2 = mMember.getSubordinate();
                            layoutSubonate2.setVisibility(View.VISIBLE);
                            btnSubonate.setText("อื่นๆ");
                            btnSubonate2.setText(mMember.getSubordinateOther());
                        } else {
                            layoutSubonate2.setVisibility(View.VISIBLE);
                        }
                        layoutSubonate.setVisibility(View.VISIBLE);
                        layoutDepartment.setVisibility(View.GONE);
                        btnSubonate.setText(mMember.getOrgName());
                        try {
                            subonate = Integer.parseInt(mMember.getOrgid());
                        } catch (Exception e) {

                        }
                        //-----------------------------------------
                        if (mMember.getSubname().contains("ส่วนจังหวัด")) {

                            layoutPosition.setVisibility(View.VISIBLE);
                            layoutProvince.setVisibility(View.VISIBLE);
                            layoutDistrict.setVisibility(View.GONE);
                            layoutName.setVisibility(View.GONE);
                        } else if (mMember.getSubname().contains("ส่วนตำบล") || mMember.getSubname().contains("เทศบาล")) {
                            layoutSubonate2.setVisibility(View.VISIBLE);
                            layoutPosition.setVisibility(View.VISIBLE);
                            layoutProvince.setVisibility(View.VISIBLE);
                            layoutDistrict.setVisibility(View.VISIBLE);
                            layoutName.setVisibility(View.VISIBLE);

                        } else {
                            if (mMember.getSubordinate() != 0 && mMember.getSubordinate() >= 8 && mMember.getSubordinate() <= 27) {
                                layoutPosition.setVisibility(View.VISIBLE);
                                ministry = mMember.getMinistryID();
                                layoutDepartment.setVisibility(View.VISIBLE);
                                btnDepartMent.setText(mMember.getMinistryName());
                                layoutProvince.setVisibility(View.GONE);
                                btnPosition.setText(mMember.getJobtitleName());
                            } else {
                                layoutPosition.setVisibility(View.GONE);
                                layoutProvince.setVisibility(View.GONE);
                                layoutDistrict.setVisibility(View.GONE);
                                layoutName.setVisibility(View.GONE);
                                layoutDepartment.setVisibility(View.GONE);
                            }
//                            layoutSubonate2.setVisibility(View.VISIBLE);
                        }
//                        layoutSubonate2.setVisibility(View.VISIBLE);
                        btnSubonate2.setText(mMember.getSubname());
                        subonate2 = mMember.getSubordinate();
                        subonateOrigin = mMember.getSubordinate();


//                btnSubonate.setText(mMember.getOrgName());
//
//                layoutSubonate.setVisibility(View.VISIBLE);
//                layoutProvince.setVisibility(View.GONE);
//                layoutSubonate2.setVisibility(View.VISIBLE);
                    } else {
                        reset();
                        btnType.setText("ประชาชน");
                        layoutProvince.setVisibility(View.VISIBLE);
                        btnProvince.setText(mMember.getProvinceName());
                        provinceID = mMember.getSubordinateProvince();
                        typeRoot = "0";

                    }

                }


            }

            @Override
            public void fail(String result) {

            }
        });
    }
}
