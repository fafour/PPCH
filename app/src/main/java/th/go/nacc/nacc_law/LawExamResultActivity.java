package th.go.nacc.nacc_law;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.lzyzsd.circleprogress.DonutProgress;

import java.util.ArrayList;

import th.go.nacc.nacc_law.application.MyApplication;
import th.go.nacc.nacc_law.model.Examination;

/**
 * Created by nontachai on 7/11/15 AD.
 */
public class LawExamResultActivity extends AbstractActivity {

    private static Examination mExamination;

    private int lawSection;

    private int historyID;
    private String certificatePath;

    private TextView mLblTitle, mLblPoint, mLblResultText;
    private View mBtnGetCert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_law_exam_result);

        Bundle data = getIntent().getExtras();

        if (data != null) {
            lawSection = data.getInt("law_section");
            mExamination = data.getParcelable("examination");
            certificatePath = data.getString("certificate");
            historyID = data.getInt("history_id");
        }


//        final ActionBar actionBar = getActionBar();
//
//
//        actionBar.setDisplayHomeAsUpEnabled(true);
//
//        BitmapDrawable background = new BitmapDrawable( getResources(), BitmapFactory.decodeResource(getResources(), R.drawable.top_bar));
//        background.setTileModeX(android.graphics.Shader.TileMode.REPEAT);
//        actionBar.setBackgroundDrawable(background);

        if (lawSection == 100) {
            setTitle("ผลการทดสอบมาตรา 100");
        } else if (lawSection == 103) {
            setTitle("ผลการทดสอบมาตรา 103");
        }

        _outletObject();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable("examination", mExamination);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        mExamination = savedInstanceState.getParcelable("examination");
    }

    private void _outletObject() {
        mLblTitle = (TextView) findViewById(R.id.lblTitle);
        mLblPoint = (TextView) findViewById(R.id.lblPoint);
        mLblResultText = (TextView) findViewById(R.id.lblResultText);
        ImageView imagePass = (ImageView)findViewById(R.id.image_pass);
        DonutProgress donutProgress = (DonutProgress) findViewById(R.id.donut_progress);
        mBtnGetCert = findViewById(R.id.btnGetCert);

        mLblTitle.setText(MyApplication.getInstance().getMember().getName() + " " + MyApplication.getInstance().getMember().getLastname());

        /*
        Toast.makeText( this,
                "TOTAL QUESTION: " + mExamination.getTotalQuestion() +
                        ", CORRECT: " + mExamination.getCorrectAnswer() +
                        ", WRONG: " + mExamination.getWrongAnswer(), Toast.LENGTH_LONG).show();
                        */

        float point = ((mExamination.getCorrectAnswer() * 1.0f) / (mExamination.getTotalQuestion() * 1.0f)) * 100;
//        float point = 100;
        mLblPoint.setText(String.format("%.0f", point));
        donutProgress.setProgress(point);

        if (point >= 80.0f) {
            // ผ่าน
            mLblResultText.setTextColor(getResources().getColor(R.color.green));
            mLblResultText.setText("คุณสอบผ่านระดับ" + mExamination.getExaminationLevelDetail());
            mBtnGetCert.setVisibility(View.VISIBLE);
            imagePass.setImageResource(R.drawable.pass);
        } else {
            // ไม่ผ่าน
            mLblResultText.setTextColor(getResources().getColor(R.color.red));
            mLblResultText.setText("คุณไม่ผ่านการทดสอบในครั้งนี้");
            mBtnGetCert.setVisibility(View.GONE);
            imagePass.setImageResource(R.drawable.fail);
        }
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        if (id == android.R.id.home) {
//            finish();
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    public void getCertAction(View v) {
        Intent intent = new Intent(this, CertificateResultActivity.class);
        intent.putExtra("certificate", certificatePath);
        intent.putExtra("history_id", historyID);
        startActivity(intent);
    }

    public void solveAction(View v) {
        Intent intent = new Intent(this, LawExamSolveActivity.class);

        intent.putExtra("law_section", lawSection);

        intent.putParcelableArrayListExtra("questions", (ArrayList<? extends Parcelable>) mExamination.getQuestions());

        startActivity(intent);
    }
}
