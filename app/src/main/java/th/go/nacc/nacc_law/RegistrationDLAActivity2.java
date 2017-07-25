package th.go.nacc.nacc_law;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import th.go.nacc.nacc_law.model.Amphur;
import th.go.nacc.nacc_law.model.JobTitle;
import th.go.nacc.nacc_law.model.Member;
import th.go.nacc.nacc_law.model.Province;
import th.go.nacc.nacc_law.model.Subordinate;

/**
 * Created by nontachai on 7/19/15 AD.
 */
public class RegistrationDLAActivity2 extends AbstractActivity {

    /*
    private static final int SUBORDINATE_ID_PAO = 1;
    private static final int SUBORDINATE_ID_SAO = 2;
    private static final int SUBORDINATE_ID_MUNICIPAL_NAKORN = 3;
    private static final int SUBORDINATE_ID_MUNICIPAL_MUANG = 4;
    private static final int SUBORDINATE_ID_MUNICIPAL_TUMBOL = 5;
    */

    private Member mMember;

    private int lawSection;

    private List<Province> mProvinces;
    private List<Subordinate> mItems;
    private List<JobTitle> mJobTitles;

    private View mLayoutAmphur, mLayoutItem, mLayoutProvince;
    private Spinner mSpinnerSubordinate, mSpinnerProvince, mSpinnerAmphur, mSpinnerItem, mSpinnerJobTitle,spinnerType;

    //private CheckBox mChkSpouse;
    private RadioButton mRdoOwn, mRdoIsSpouse;

    private EditText mTxtPosition;
    private TextView mLblItem;

    private Province mProvinceSelected;
    private Amphur mAmphurSelected;
    private Subordinate mItemSelected;
    private JobTitle mJobTitleSelected;
    String[] title = {"ประชาชน", "เจ้าหน้าที่ของรัฐ", "คู่สมรสของเจ้าหน้าที่ของรัฐ"};
    private TextView btnType;

    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );

        setContentView( R.layout.activity_registration_dla2 );

//        final ActionBar actionBar = getActionBar();

        Bundle data = getIntent().getExtras();

        if ( data != null ) {
            lawSection = data.getInt( "law_section" );

            mMember = data.getParcelable( "member" );
        }

//        actionBar.setDisplayHomeAsUpEnabled(true);
//
//        BitmapDrawable background = new BitmapDrawable( getResources(), BitmapFactory.decodeResource(getResources(), R.drawable.top_bar));
//        background.setTileModeX(android.graphics.Shader.TileMode.REPEAT);
//        actionBar.setBackgroundDrawable(background);



        setTitle( "ลงทะเบียน" );

        _outletObject ();

        _getProvince();
    }

    @Override
    public void refresh() {
        if ( mProvinces == null || mProvinces.size() <= 0 ) {
            _getProvince();
        } else if ( mJobTitles == null || mJobTitles.size() <= 0 ) {
            _getJobTitles();
        } else {
            _getSubordinateItem();
        }
    }

    private void _getJobTitles () {
        JobTitle.get( this, new JobTitle.OnResponseListener() {
            @Override
            public void onGetListener(List<JobTitle> jobTitles, Error error) {
                if ( error == null ) {
                    mJobTitles = jobTitles;

                    if ( mJobTitles != null && mJobTitles.size() > 0 ) {
                        mJobTitleSelected = mJobTitles.get( 0 );
                    }

                    List<String> list = new ArrayList<String>();

                    for ( int i = 0, maxItem = mJobTitles.size(); i < maxItem; i++ ) {
                        JobTitle item = mJobTitles.get( i );

                        list.add( item.getName() );
                    }

                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(RegistrationDLAActivity2.this,
                            R.layout.row_type2, list);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    mSpinnerJobTitle.setAdapter(dataAdapter);
                } else {
                    retryService(error.getMessage());
                }
            }
        });
    }
    private void _getProvince () {
        Province.get(this, new Province.OnGetProvinceListener() {
            @Override
            public void onResult(List<Province> provinces, Error error) {
                if ( error == null ) {

                    mProvinces = provinces;


                    List<String> list = new ArrayList<String>();

                    for ( int i = 0, maxProvince = provinces.size(); i < maxProvince; i++ ) {
                        Province province = provinces.get( i );

                        list.add( province.getName() );
                    }

                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(RegistrationDLAActivity2.this,
                            R.layout.row_type2, list);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    mSpinnerProvince.setAdapter(dataAdapter);

                    _getJobTitles();
                } else {
                    retryService(error.getMessage());
                }
            }
        });
    }

    private void _getSubordinateItem () {
        mLayoutItem.setVisibility( View.GONE );
        Subordinate.get(this, mSpinnerSubordinate.getSelectedItemPosition() + 1, mAmphurSelected.getId(), new Subordinate.OnGetItemListener() {
            @Override
            public void onResult(List<Subordinate> items, Error error) {

                mLayoutItem.setVisibility( View.VISIBLE );

                if ( error == null ) {
                    mItems = items;

                    if ( mItems != null && mItems.size() > 0) {
                        List<String> list = new ArrayList<String>();

                        for ( int i = 0, maxItem = mItems.size(); i < maxItem; i++ ) {
                            Subordinate item = mItems.get( i );

                            list.add( item.getName() );
                        }

                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(RegistrationDLAActivity2.this,
                                R.layout.row_type2, list);
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        mSpinnerItem.setAdapter(dataAdapter);
                    } else {
                        List<String> list = new ArrayList<String>();

                        list.add( "ไม่พบข้อมูลรายชื่อ " + getSubordinateType());

                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(RegistrationDLAActivity2.this,
                                R.layout.row_type2, list);
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        mSpinnerItem.setAdapter(dataAdapter);
                    }
                } else {
                    retryService( error.getMessage() );
                }
            }
        });
    }

    private String getSubordinateType ( ) {
        switch ( mSpinnerSubordinate.getSelectedItemPosition() ) {
            case 0:
                return "องค์การบริหารส่วนจังหวัด";
            case 1:
                return "เทศบาลนคร";
            case 2:
                return "เทศบาลเมือง";
            case 3:
                return "เทศบาลตำบล";
            case 4:
                return "องค์การบริหารส่วนตำบล";
            case 5:
                return "กรุงเทพมหานคร";
            case 6:
                return "เมืองพัทยา";
        }

        return "";
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            backAction(null);
            //finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void _showAmphurComponent () {
        mLayoutAmphur.setVisibility(View.VISIBLE);
        mLayoutItem.setVisibility(View.GONE);

        if ( mProvinces.size() > 0 ) {
            mSpinnerProvince.setSelection(0);

            mProvinceSelected = mProvinces.get( 0 );

            _showAmphurItems(0);
        }
    }

    private void _showAmphurItems ( int provincePosition ) {
        mProvinceSelected = mProvinces.get( provincePosition );

        List<String> list = new ArrayList<String>();

        for ( int i = 0, maxProvince = mProvinceSelected.getAmphurs().size(); i < maxProvince; i++ ) {
            Amphur amphur = mProvinceSelected.getAmphurs().get(i);

            list.add( amphur.getName() );
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(RegistrationDLAActivity2.this,
                R.layout.row_type2, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mSpinnerAmphur.setAdapter(dataAdapter);

        if ( mProvinceSelected.getAmphurs().size() > 0 ) {
            mAmphurSelected = mProvinceSelected.getAmphurs().get(0);

            _getSubordinateItem();
        }
    }

    private void _outletObject () {
        mLayoutProvince = findViewById( R.id.layoutProvince );
        mLayoutAmphur = findViewById( R.id.layoutAmphur );
        mLayoutItem = findViewById( R.id.layoutItem );



        //mChkSpouse = (CheckBox) findViewById( R.id.chkIsSpouse );
        mRdoIsSpouse = (RadioButton) findViewById( R.id.rdoIsSpouse );
        mRdoOwn = (RadioButton) findViewById( R.id.rdoOwn );

        mRdoOwn.setChecked( true );

        mTxtPosition = (EditText) findViewById( R.id.txtPosition );

        mSpinnerSubordinate = (Spinner) findViewById( R.id.spinnerSubordinate );

        mSpinnerProvince = (Spinner) findViewById( R.id.spinnerProvince );
        mSpinnerAmphur = (Spinner) findViewById( R.id.spinnerAmphur );
        mSpinnerItem = (Spinner) findViewById( R.id.spinnerItem );
        mSpinnerJobTitle = (Spinner) findViewById( R.id.spinnerJobTitle );
        spinnerType = (Spinner)findViewById(R.id.btn_type);

        mTxtPosition.setVisibility( View.GONE );

        mLblItem = (TextView) findViewById( R.id.lblItem );

        ArrayAdapter<String> dataAdaptear = new ArrayAdapter<String>(RegistrationDLAActivity2.this,
                R.layout.row_type2, title);
        dataAdaptear.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerType.setAdapter(dataAdaptear);
        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if(i>0){

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        mSpinnerSubordinate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                switch ( position ) {
                    case 0:
                        if ( mProvinces != null && mProvinces.size() > 0 ) {
                            mProvinceSelected = mProvinces.get( 0 );
                        }

                        mLayoutProvince.setVisibility( View.VISIBLE );
                        mLayoutAmphur.setVisibility(View.GONE);
                        mLayoutItem.setVisibility(View.GONE);
                        break;
                    case 1:
                        mLblItem.setText("รายชื่อเทศบาลนคร");

                        mLayoutProvince.setVisibility(View.VISIBLE);
                        _showAmphurComponent();
                        break;
                    case 2:
                        mLblItem.setText( "รายชื่อเทศบาลเมือง" );

                        mLayoutProvince.setVisibility(View.VISIBLE);
                        _showAmphurComponent();
                        break;
                    case 3:
                        mLblItem.setText( "รายชื่อเทศบาลตำบล" );

                        mLayoutProvince.setVisibility(View.VISIBLE);
                        _showAmphurComponent();
                        break;
                    case 4:
                        mLblItem.setText("รายชื่ออบต.");

                        mLayoutProvince.setVisibility(View.VISIBLE);
                        _showAmphurComponent();
                        break;
                    case 5:
                        mLayoutProvince.setVisibility( View.GONE );
                        mLayoutAmphur.setVisibility( View.GONE );
                        mLayoutItem.setVisibility( View.GONE );
                        break;
                    case 6:
                        mLayoutProvince.setVisibility( View.GONE );
                        mLayoutAmphur.setVisibility(View.GONE);
                        mLayoutItem.setVisibility(View.GONE);
                        break;
                    default:
                        _showAmphurComponent();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });


        mSpinnerProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                if ( mSpinnerSubordinate.getSelectedItemPosition() == 0 ) {
                    mProvinceSelected = mProvinces.get( position );
                    return;
                }


                _showAmphurItems(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        mSpinnerAmphur.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                mAmphurSelected = mProvinceSelected.getAmphurs().get(position);

                _getSubordinateItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        mSpinnerItem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (mItems.size() > 0) {
                    mItemSelected = mItems.get(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mSpinnerJobTitle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if ( mJobTitles.size() > 0 ) {
                    mJobTitleSelected = mJobTitles.get( i );

                    if ( mJobTitleSelected.getIsOther() == 1 ) {
                        mTxtPosition.setVisibility( View.VISIBLE );
                    } else {
                        mTxtPosition.setVisibility( View.GONE );
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        List<String> list = new ArrayList<String>();
        list.add("องค์การบริหารส่วนจังหวัด");
        list.add("เทศบาลนคร");
        list.add("เทศบาลเมือง");
        list.add("เทศบาลตำบล");
        list.add("องค์การบริหารส่วนตำบล");
        list.add( "กรุงเทพมหานคร" );
        list.add("เมืองพัทยา");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                R.layout.row_type2, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mSpinnerSubordinate.setAdapter(dataAdapter);

    }

    @Override
    public void onBackPressed() {
        backAction( null );
    }

    public void backAction ( View v ) {

        LayoutInflater li = LayoutInflater.from(this);
        View alertView = li.inflate(R.layout.alertlayout, null);

        TextView textView1 = (TextView) alertView.findViewById( R.id.textView1 );

        textView1.setText( "คุณต้องการกลับไปหน้าลงทะเบียนใช่หรือไม่่?" );

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(alertView);

        //final EditText userInput = (EditText) alertView.findViewById(R.id.editTextDialogUserInput);

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("ใช่", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
//                        Intent intent = new Intent ( RegistrationDLAActivity2.this, RegistrationActivity.class );
//
//                        mMember.setIsDLAMember( 0 );
//
//                        intent.putExtra( "member", mMember );
//
//                        startActivity(intent);

                        finish();
                    }
                })
                .setNegativeButton("ไม่ใช่", null);

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();

        /*
        TextView textView = (TextView) dialog.findViewById(android.R.id.message);
        Typeface face = Typeface.createFromAsset(getAssets(), "Font-Black.ttf");
        textView.setTypeface(face);
        */

    }

    public void registerAction ( View v ) {


        if ( mSpinnerSubordinate.getSelectedItemPosition() > 0 && mSpinnerSubordinate.getSelectedItemPosition() < 5 ) {

            if ( mItemSelected == null ) {
                simpleAlert( "กรุณาเลือกรายชื่อ" + getSubordinateType());
                return;
            }
        }

        if ( mSpinnerSubordinate.getSelectedItemPosition() < 5 ) {
            if (  mProvinceSelected == null ) {
                simpleAlert( "กรุณาเลือกจังหวัด" );
                return;
            }
        }

        if ( mJobTitleSelected == null ) {
            simpleAlert( "กรุณาเลือกตำแหน่ง" );
            return;
        } else {
            if ( mJobTitleSelected.getIsOther() == 1 ) {
                if ( TextUtils.isEmpty( mTxtPosition.getText() ) ) {
                    simpleAlert( "กรุณาระบุตำแหน่ง" );
                    return;
                }
            }
        }





        mMember.setIsDLAMember(1);
        mMember.setJobTitle( mJobTitleSelected.getId());
        if ( mJobTitleSelected.getIsOther() == 1 ) {
            mMember.setPosition(mTxtPosition.getText().toString());
        } else {
            mMember.setPosition( "" );
        }
        mMember.setSubordinate(mSpinnerSubordinate.getSelectedItemPosition() + 1);


        if ( mSpinnerSubordinate.getSelectedItemPosition() < 5 ) { // not bangkok & pattaya
            mMember.setSubordinateProvince(mProvinceSelected.getId());

            if ( mSpinnerSubordinate.getSelectedItemPosition() != 0 ) { // not อบจ
                if ( mItemSelected != null ) {
                    mMember.setSubordinateItem(mItemSelected.getId());
                }
            }
        }


        //mMember.setIsSpouse(mChkSpouse.isChecked() ? 1 : 0);

        if ( mRdoIsSpouse.isChecked() ) {
            mMember.setIsSpouse( 1 );
        } else {
            mMember.setIsSpouse( 0 );
        }

        Member.register(this, mMember, new Member.OnRegisterListener() {
            @Override
            public void registerResult(Member member, Error error) {
                if (error == null) {

                    // get prompts.xml view
                    LayoutInflater li = LayoutInflater.from(RegistrationDLAActivity2.this);
                    View alertView = li.inflate(R.layout.alertlayout, null);

                    TextView textView1 = (TextView) alertView.findViewById(R.id.textView1);

                    textView1.setText("ลงทะเบียนเรียบร้อย คุณสามารถเข้าทำแบบทดสอบได้แล้ว");

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(RegistrationDLAActivity2.this);

                    // set prompts.xml to alertdialog builder
                    alertDialogBuilder.setView(alertView);

                    //final EditText userInput = (EditText) alertView.findViewById(R.id.editTextDialogUserInput);

                    // set dialog message
                    alertDialogBuilder
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    finish();

                                    Intent intent = new Intent(RegistrationDLAActivity2.this, LawLevelListActivity.class);

                                    intent.putExtra("law_section", lawSection);

                                    startActivity(intent);
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
    }
}
