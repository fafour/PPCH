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

import th.go.nacc.nacc_law.application.MyApplication;
import th.go.nacc.nacc_law.model.Member;

/**
 * Created by nontachai on 8/18/15 AD.
 */
public class EditMemberActivity extends AbstractActivity {

    private EditText mTxtName, mTxtLastname, mTxtIDCard;


    @Override
    protected  void onCreate ( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);

        setContentView( R.layout.activity_edit_member );

        final ActionBar actionBar = getActionBar();

        Bundle data = getIntent().getExtras();

        actionBar.setDisplayHomeAsUpEnabled(true);

        BitmapDrawable background = new BitmapDrawable( getResources(), BitmapFactory.decodeResource(getResources(), R.drawable.top_bar));
        background.setTileModeX(android.graphics.Shader.TileMode.REPEAT);
        actionBar.setBackgroundDrawable(background);

        setTitle("แก้ไขข้อมูล");

        _outletObject();
    }

    private void _outletObject () {
        mTxtName = (EditText) findViewById( R.id.txtName );
        mTxtLastname = (EditText) findViewById( R.id.txtLastname );
        mTxtIDCard = (EditText) findViewById( R.id.txtIDCard );

        //mTxtName.setText("นนทชัย");
        //mTxtLastname.setText("ทรัพย์ทวีพงศ์");
        //mTxtIDCard.setText("2200100021748");

        if ( MyApplication.getInstance().getMember() != null ) {
            mTxtName.setText( MyApplication.getInstance().getMember().getName() );
            mTxtLastname.setText( MyApplication.getInstance().getMember().getLastname() );
            mTxtIDCard.setText( MyApplication.getInstance().getMember().getIdcard() );
            //mTxtIDCard.setEnabled(false);
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

    public void editDLAAction ( View v ) {

        if ( MyApplication.getInstance().getMember().getIsDLAMember() == 0 ) {
            LayoutInflater li = LayoutInflater.from( EditMemberActivity.this );
            View promptsView = li.inflate(R.layout.alertlayout, null);

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder( EditMemberActivity.this);

            // set prompts.xml to alertdialog builder
            alertDialogBuilder.setView(promptsView);

            TextView lbl = (TextView) promptsView.findViewById( R.id.textView1 );

            lbl.setText( "คุณยังไม่ได้เป็นสมาชิก อปท. ต้องการใส่รายละเอียดเกี่ยวกับการเป็นสมาชิก อปท. ใช่หรือไม่" );
            // set dialog message
            alertDialogBuilder
                    .setCancelable(false)
                    .setPositiveButton("ใข่",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Intent intent = new Intent(EditMemberActivity.this, EditDLAInfoActivity.class);

                                    startActivity(intent);
                                }
                            })
                    .setNegativeButton("ไม่ใช่", null);

            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();

            // show it
            alertDialog.show();
        } else {
            Intent intent = new Intent(this, EditDLAInfoActivity.class);

            startActivity(intent);
        }
    }

    public void updateAction ( View v ) {
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



        Member.updateInfo(this,
                mTxtName.getText().toString(),
                mTxtLastname.getText().toString(),
                mTxtIDCard.getText().toString(),
                MyApplication.getInstance().getMember().getId(), new Member.OnUpdateListener() {
                    @Override
                    public void updateResult(Member member, Error error) {
                        if ( error == null ) {
                            LayoutInflater li = LayoutInflater.from( EditMemberActivity.this );
                            View promptsView = li.inflate(R.layout.alertlayout, null);

                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder( EditMemberActivity.this);

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
                            simpleAlert( error.getMessage() );
                        }
                    }
                });
    }
}
