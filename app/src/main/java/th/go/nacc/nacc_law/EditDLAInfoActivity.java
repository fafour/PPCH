package th.go.nacc.nacc_law;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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

import th.go.nacc.nacc_law.application.MyApplication;
import th.go.nacc.nacc_law.model.Amphur;
import th.go.nacc.nacc_law.model.JobTitle;
import th.go.nacc.nacc_law.model.Member;
import th.go.nacc.nacc_law.model.Province;
import th.go.nacc.nacc_law.model.Subordinate;

/**
 * Created by nontachai on 8/18/15 AD.
 */
public class EditDLAInfoActivity extends AbstractActivity {

    private Member mMember;

    private int lawSection;

    private List<Province> mProvinces;
    private List<Subordinate> mItems;
    private List<JobTitle> mJobTitles;

    private View mLayoutAmphur, mLayoutItem, mLayoutProvince, mLayoutContent;
    private Spinner mSpinnerSubordinate, mSpinnerProvince, mSpinnerAmphur, mSpinnerItem, mSpinnerJobTitle;

    //private CheckBox mChkSpouse;
    private RadioButton mRdoOwn, mRdoIsSpouse;

    private EditText mTxtPosition;
    private TextView mLblItem;

    private Province mProvinceSelected;
    private Amphur mAmphurSelected;
    private Subordinate mItemSelected;
    private JobTitle mJobTitleSelected;

    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );

        setContentView( R.layout.activity_edit_member_dla );

//        final ActionBar actionBar = getActionBar();
//
//        Bundle data = getIntent().getExtras();
//
//        actionBar.setDisplayHomeAsUpEnabled(true);
//
//        BitmapDrawable background = new BitmapDrawable( getResources(), BitmapFactory.decodeResource(getResources(), R.drawable.top_bar));
//        background.setTileModeX(android.graphics.Shader.TileMode.REPEAT);
//        actionBar.setBackgroundDrawable(background);

        setTitle("แก้ไขข้อมูลสมาชิก อปท.");

        _outletObject();

        _getProvince();
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

                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(EditDLAInfoActivity.this,
                            android.R.layout.simple_spinner_item, list);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    mSpinnerJobTitle.setAdapter(dataAdapter);

                    _checkInitialField();

                    mLayoutContent.setVisibility( View.VISIBLE );
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
                if (error == null) {

                    mProvinces = provinces;


                    List<String> list = new ArrayList<String>();

                    for (int i = 0, maxProvince = provinces.size(); i < maxProvince; i++) {
                        Province province = provinces.get(i);

                        list.add(province.getName());
                    }

                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(EditDLAInfoActivity.this,
                            android.R.layout.simple_spinner_item, list);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    mSpinnerProvince.setAdapter(dataAdapter);

                    _checkInitialField();

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

                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(EditDLAInfoActivity.this,
                                android.R.layout.simple_spinner_item, list);
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        mSpinnerItem.setAdapter(dataAdapter);

                        if ( MyApplication.getInstance().getMember().getSubordinateItem() > 0 ) {
                            for ( int i = 0, max = mItems.size(); i < max; i++) {
                                if ( mItems.get( i ).getId() == MyApplication.getInstance().getMember().getSubordinateItem() ) {
                                    mItemSelected = mItems.get( i );
                                    mSpinnerItem.setSelection( i );
                                    break;
                                }
                            }
                        }
                    } else {
                        List<String> list = new ArrayList<String>();

                        list.add( "ไม่พบข้อมูลรายชื่อ " + getSubordinateType());

                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(EditDLAInfoActivity.this,
                                android.R.layout.simple_spinner_item, list);
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

    private void _showAmphurComponent () {
        mLayoutAmphur.setVisibility(View.VISIBLE);
        mLayoutItem.setVisibility(View.GONE);

        if ( mProvinces.size() > 0 && mProvinceSelected == null ) {
            mSpinnerProvince.setSelection(0);

            mProvinceSelected = mProvinces.get(0);

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

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(EditDLAInfoActivity.this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mSpinnerAmphur.setAdapter(dataAdapter);


        Log.i( "TAG", "AMPHUR SELECTED: " + MyApplication.getInstance().getMember().getAmphur() );

        if ( MyApplication.getInstance().getMember().getAmphur() > 0 ) {
            if ( mProvinceSelected != null ) {
                for (int i = 0, max = mProvinceSelected.getAmphurs().size(); i < max; i++) {
                    if ( mProvinceSelected.getAmphurs().get( i ).getId() == MyApplication.getInstance().getMember().getAmphur()) {
                        mAmphurSelected = mProvinceSelected.getAmphurs().get( i );
                        mSpinnerAmphur.setSelection( i );
                        break;
                    }
                }
            }
        }

        if ( mAmphurSelected == null ) {
            if ( mProvinceSelected.getAmphurs().size() > 0 ) {
                mAmphurSelected = mProvinceSelected.getAmphurs().get(0);

                _getSubordinateItem();
            }
        }

    }

    private void _outletObject () {
        mLayoutProvince = findViewById( R.id.layoutProvince );
        mLayoutAmphur = findViewById( R.id.layoutAmphur );
        mLayoutItem = findViewById( R.id.layoutItem );
        mLayoutContent = findViewById( R.id.layoutContent );

        //mChkSpouse = (CheckBox) findViewById( R.id.chkIsSpouse );
        mRdoOwn = (RadioButton) findViewById( R.id.rdoOwn );
        mRdoIsSpouse = (RadioButton) findViewById( R.id.rdoIsSpouse );

        mRdoOwn.setChecked( true );

        mTxtPosition = (EditText) findViewById( R.id.txtPosition );

        mSpinnerSubordinate = (Spinner) findViewById( R.id.spinnerSubordinate );
        mSpinnerProvince = (Spinner) findViewById( R.id.spinnerProvince );
        mSpinnerAmphur = (Spinner) findViewById( R.id.spinnerAmphur );
        mSpinnerItem = (Spinner) findViewById( R.id.spinnerItem );
        mSpinnerJobTitle = (Spinner) findViewById( R.id.spinnerJobTitle );

        mTxtPosition.setVisibility( View.GONE );

        mLblItem = (TextView) findViewById( R.id.lblItem );

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
                if (mJobTitles.size() > 0) {
                    mJobTitleSelected = mJobTitles.get(i);

                    if (mJobTitleSelected.getIsOther() == 1) {
                        mTxtPosition.setVisibility(View.VISIBLE);
                    } else {
                        mTxtPosition.setVisibility(View.GONE);
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
        list.add("กรุงเทพมหานคร");
        list.add("เมืองพัทยา");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mSpinnerSubordinate.setAdapter(dataAdapter);


        _checkInitialField();

    }

    private void _checkInitialField () {

        if ( MyApplication.getInstance().getMember().getSubordinate() <= 0 ) return;

        if ( mProvinces == null || mProvinces.size() <= 0 ) {
            mLayoutContent.setVisibility( View.GONE );
        } else {
            if ( mJobTitles == null || mJobTitles.size() <= 0 ) {
                mLayoutContent.setVisibility( View.GONE );
            } else {
                mLayoutContent.setVisibility(View.VISIBLE);

                mSpinnerSubordinate.setSelection( MyApplication.getInstance().getMember().getSubordinate() - 1);

                int positionIndex = -1;


                if ( MyApplication.getInstance().getMember().getSubordinateProvince() > 0 ) {
                    for (int i = 0, max = mProvinces.size(); i < max; i++) {
                        if (mProvinces.get(i).getId() == MyApplication.getInstance().getMember().getSubordinateProvince()) {
                            positionIndex = i;
                            mProvinceSelected = mProvinces.get(i);
                            mSpinnerProvince.setSelection( i );
                            break;
                        }
                    }
                }


                switch (MyApplication.getInstance().getMember().getSubordinate() - 1) {
                    case 0:
                        mLayoutProvince.setVisibility(View.VISIBLE);

                        mLayoutAmphur.setVisibility(View.GONE);
                        mLayoutItem.setVisibility(View.GONE);
                        break;
                    case 1:
                        mLayoutProvince.setVisibility( View.VISIBLE );

                        if ( mProvinceSelected != null ) {
                            _showAmphurItems(positionIndex);
                        }
                        break;
                    case 2:
                        mLayoutProvince.setVisibility( View.VISIBLE );

                        if ( mProvinceSelected != null ) {
                            _showAmphurItems(positionIndex);
                        }

                        break;
                    case 3:
                        mLayoutProvince.setVisibility( View.VISIBLE );

                        if ( mProvinceSelected != null ) {
                            _showAmphurItems( positionIndex);
                        }
                        break;
                    case 4:
                        mLayoutProvince.setVisibility( View.VISIBLE );

                        if ( mProvinceSelected != null ) {
                            _showAmphurItems( positionIndex );
                        }

                        break;
                    case 5:
                        mLayoutProvince.setVisibility( View.GONE );
                        mLayoutAmphur.setVisibility(View.GONE);
                        mLayoutItem.setVisibility(View.GONE);
                        break;
                    case 6:
                        mLayoutProvince.setVisibility( View.GONE );
                        mLayoutAmphur.setVisibility(View.GONE);
                        mLayoutItem.setVisibility(View.GONE);
                        break;

                }

                if ( MyApplication.getInstance().getMember().getIsSpouse() == 1 ) {
                    //mChkSpouse.setChecked( true );
                    mRdoIsSpouse.setChecked( true );
                } else {
                    //mChkSpouse.setChecked( false );
                    mRdoOwn.setChecked( true );
                }

                if ( MyApplication.getInstance().getMember().getJobTitle() > 0 ) {
                    if ( mJobTitles != null ) {
                        for ( int i = 0, max = mJobTitles.size(); i < max; i++ ) {
                            if ( mJobTitles.get( i ).getId() == MyApplication.getInstance().getMember().getJobTitle()) {
                                mJobTitleSelected = mJobTitles.get( i );

                                mSpinnerJobTitle.setSelection( i );
                                break;
                            }
                        }
                    }
                }

                if ( mJobTitleSelected.getIsOther() == 1 ) {
                    mTxtPosition.setText( MyApplication.getInstance().getMember().getPosition() );
                    mTxtPosition.setVisibility( View.VISIBLE );
                } else {
                    mTxtPosition.setVisibility( View.GONE );
                }
            }
        }


    }

    public void backAction ( View v ) {
        finish();
    }

    public void saveAction ( View v ) {

        Member member = MyApplication.getInstance().getMember();

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
                if ( TextUtils.isEmpty(mTxtPosition.getText()) ) {
                    simpleAlert( "กรุณาระบุตำแหน่ง" );
                    return;
                }
            }
        }

        member.setIsDLAMember(1);
        member.setJobTitle( mJobTitleSelected.getId());
        if ( mJobTitleSelected.getIsOther() == 1 ) {
            member.setPosition(mTxtPosition.getText().toString());
        } else {
            member.setPosition( "" );
        }
        member.setSubordinate(mSpinnerSubordinate.getSelectedItemPosition() + 1);


        if ( mSpinnerSubordinate.getSelectedItemPosition() < 5 ) { // not bangkok & pattaya
            member.setSubordinateProvince(mProvinceSelected.getId());

            if ( mSpinnerSubordinate.getSelectedItemPosition() != 0 ) { // not อบจ
                if ( mItemSelected != null ) {
                    member.setSubordinateItem(mItemSelected.getId());
                }
            }
        }


        //member.setIsSpouse(mChkSpouse.isChecked() ? 1 : 0);

        if ( mRdoOwn.isChecked() ) {
            member.setIsSpouse( 0 );
        } else if ( mRdoIsSpouse.isChecked() ){
            member.setIsSpouse( 1 );
        }

        Member.updateDLAInfo(this, member, new Member.OnUpdateListener() {
            @Override
            public void updateResult(Member member, Error error) {
                if ( error == null ) {
                    LayoutInflater li = LayoutInflater.from( EditDLAInfoActivity.this );
                    View promptsView = li.inflate(R.layout.alertlayout, null);

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder( EditDLAInfoActivity.this);

                    // set prompts.xml to alertdialog builder
                    alertDialogBuilder.setView(promptsView);

                    TextView lbl = (TextView) promptsView.findViewById( R.id.textView1 );

                    lbl.setText( "แก้ไขข้อมูลเรียบร้อยแล้ว" );
                    // set dialog message
                    alertDialogBuilder
                            .setCancelable(false)
                            .setPositiveButton("ตกลง",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            finish();
                                        }
                                    });

                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();

                    // show it
                    alertDialog.show();
                } else {
                    simpleAlert( error.getMessage());
                }
            }
        });
    }

}
