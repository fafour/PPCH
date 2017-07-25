package th.go.nacc.nacc_law;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import th.go.nacc.nacc_law.model.Member;

/**
 * Created by nontachai on 7/17/15 AD.
 */
public class RegistrationActivity extends AbstractActivity {

    private EditText mTxtName, mTxtLastname, mTxtIDCard;


    private int lawSection;

    private Member mMember;

    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );

        setContentView( R.layout.activity_registration );

        final ActionBar actionBar = getActionBar();

        Bundle data = getIntent().getExtras();

        if ( data != null ) {
            lawSection = data.getInt( "law_section" );

            mMember = data.getParcelable("member");
        }

        actionBar.setDisplayHomeAsUpEnabled(true);

        BitmapDrawable background = new BitmapDrawable( getResources(), BitmapFactory.decodeResource(getResources(), R.drawable.top_bar));
        background.setTileModeX(android.graphics.Shader.TileMode.REPEAT);
        actionBar.setBackgroundDrawable(background);

        setTitle( "ลงทะเบียน" );


        _outletObject ();
    }


    private void _outletObject () {
        mTxtName = (EditText) findViewById( R.id.txtName );
        mTxtLastname = (EditText) findViewById( R.id.txtLastname );
        mTxtIDCard = (EditText) findViewById( R.id.txtIDCard );

        //mTxtName.setText("นนทชัย");
        //mTxtLastname.setText("ทรัพย์ทวีพงศ์");
        //mTxtIDCard.setText("2200100021748");

        if ( mMember != null ) {
            mTxtName.setText( mMember.getName() );
            mTxtLastname.setText( mMember.getLastname() );
            mTxtIDCard.setText( mMember.getIdcard() );
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

    public void loginAction ( View v ) {
        if (TextUtils.isEmpty( mTxtName.getText() )
                || TextUtils.isEmpty( mTxtLastname.getText())
                || TextUtils.isEmpty( mTxtIDCard.getText() ) ) {
            simpleAlert("กรุณากรอกรหัสบัตรประชาชน ชื่อและนามสกุลให้เรียบร้อย และตรวจสอบให้ถูกต้อง");
            return;
        }

        if ( mTxtIDCard.getText().toString().length() < 13 ) {
            simpleAlert( "กรุณากรอกรหัสบัตรประชาชน 13 หลักให้ครบถ้วน!");
            return;
        }

        Member.login(this, mTxtName.getText().toString(), mTxtLastname.getText().toString(), mTxtIDCard.getText().toString(), new Member.OnLoginListener() {
            @Override
            public void loginResult(final Member member, Error error) {
                if ( error == null ) {
                    if ( member == null ) {
                        // alert to action
                        LayoutInflater li = LayoutInflater.from(RegistrationActivity.this);
                        View alertView = li.inflate(R.layout.alertlayout, null);

                        TextView textView1 = (TextView) alertView.findViewById( R.id.textView1 );

                        textView1.setText( "คุณเป็นสมาชิก (หรือคู่สมรสของสมาชิก) ขององค์กรปกครองส่วนท้องถิ่นหรือไม่?" );

                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(RegistrationActivity.this);

                        // set prompts.xml to alertdialog builder
                        alertDialogBuilder.setView(alertView);

                        //final EditText userInput = (EditText) alertView.findViewById(R.id.editTextDialogUserInput);

                        // set dialog message
                        alertDialogBuilder
                                .setCancelable(false)
                                .setPositiveButton("ใช่", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        fillInDLADetail();
                                    }
                                })
                                .setNegativeButton("ไม่ใช่", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        // send register with is_dla_member
                                        register ( false );
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

                        finish();

                        // start examination level list
                        Intent intent = new Intent( RegistrationActivity.this, LawLevelListActivity.class );

                        intent.putExtra( "law_section", lawSection );

                        startActivity(intent);

                        /*
                        TextView textView = (TextView) dialog.findViewById(android.R.id.message);
                        Typeface face = Typeface.createFromAsset(getAssets(), "Font-Black.ttf");
                        textView.setTypeface(face);
                        */
                    }
                } else {
                    simpleAlert( error.getMessage() );
                }
            }
        });

    }



    private void register ( boolean isDLAMember ) {
        Member member = new Member();
        member.setName( mTxtName.getText().toString() );
        member.setLastname(mTxtLastname.getText().toString());
        member.setIdcard( mTxtIDCard.getText().toString() );
        member.setIsDLAMember( isDLAMember? 1: 0 );

        Member.register(this, member, new Member.OnRegisterListener() {
            @Override
            public void registerResult(Member member, Error error) {
                if ( error == null ) {

                    // alert to action
                    LayoutInflater li = LayoutInflater.from(RegistrationActivity.this);
                    View alertView = li.inflate(R.layout.alertlayout, null);

                    TextView textView1 = (TextView) alertView.findViewById( R.id.textView1 );

                    textView1.setText( "ลงทะเบียนเรียบร้อย คุณสามารถเข้าทำแบบทดสอบได้แล้ว" );

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(RegistrationActivity.this);

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

                                    // start examination level list
                                    Intent intent = new Intent( RegistrationActivity.this, LawLevelListActivity.class );

                                    intent.putExtra( "law_section", lawSection );

                                    startActivity(intent);
                                }
                            });

                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();

                    // show it
                    alertDialog.show();

                    /*
                    TextView textView = (TextView) dialog.findViewById(android.R.id.message);
                    Typeface face = Typeface.createFromAsset(getAssets(), "Font-Black.ttf");
                    textView.setTypeface(face);
                    */

                } else {
                    simpleAlert( error.getMessage() );
                }
            }
        });
    }

    private void fillInDLADetail (  ) {
        Member member = new Member();
        member.setName( mTxtName.getText().toString() );
        member.setLastname(mTxtLastname.getText().toString());
        member.setIdcard( mTxtIDCard.getText().toString() );
        member.setIsDLAMember( 1 );

        Intent intent = new Intent ( this, RegistrationDLAActivity.class );

        intent.putExtra( "law_section", lawSection );
        intent.putExtra( "member", member );

        startActivity(intent);

        finish();
    }
}
