package th.go.nacc.nacc_law;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import th.go.nacc.nacc_law.application.MyApplication;
import th.go.nacc.nacc_law.da.ExaminationDA;
import th.go.nacc.nacc_law.helper.ZoomOutPageTransformer;
import th.go.nacc.nacc_law.model.Answer;
import th.go.nacc.nacc_law.model.Examination;
import th.go.nacc.nacc_law.model.Question;

/**
 * Created by nontachai on 7/10/15 AD.
 */
public class LawExamActivity extends AbstractActivity {

    private ExpandableListView mListView;

    private TextView mBtnNext, mBtnPrev;

    private LawExamAdapter mAdapter;

    private int lawSection;

    private Question mCurrentQuestion;
    ArrayList<String> images = new ArrayList<>();
    private ViewPager pager;
    FrameLayout layoutImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_law_exam);
        Bundle data = getIntent().getExtras();

        if (data != null) {
            lawSection = data.getInt("law_section");
        }


        if (lawSection == 100) {
            setTitle("แบบทดสอบมาตรา 100");
        } else if (lawSection == 103) {
            setTitle("แบบทดสอบมาตรา 103");
        }


        _outletObject();

        getCurrentQuestion();


//        images.add("http://socialnews.teenee.com/penkhao/img3/12947.jpg");
        layoutImage = (FrameLayout) findViewById(R.id.pager_container);
        pager = (ViewPager) findViewById(R.id.coverflow);
//        mContainer.setViewPager(pager);
        PagerAdapter adapter = new MyPagerAdapter();
        pager.setPageTransformer(true, new ZoomOutPageTransformer(true));
        pager.setAdapter(adapter);
        //Necessary or the pager will only have one extra page to show
        // make this at least however many pages you can see
//        pager.setOffscreenPageLimit(adapter.getCount());
        pager.setOffscreenPageLimit(3);
        //A little space between pages
        pager.setPageMargin((int) getResources().getDimension(R.dimen.image_padding));
        //If hardware acceleration is enabled, you should also remove
        // clipping on the pager for its children.
        pager.setClipChildren(false);


        images.clear();

        if (!mCurrentQuestion.getClip().isEmpty()) {
            try {
                String image = "http://img.youtube.com/vi/" + mCurrentQuestion.getClip().split("v=")[1] + "/0.jpg";
                images.add(image);
            } catch (Exception e) {

            }
        }
        if (!mCurrentQuestion.getImage().isEmpty()) {
            images.add(mCurrentQuestion.getImage());
        }

        if(images.isEmpty()){
            layoutImage.setVisibility(View.GONE);
        }else{
            layoutImage.setVisibility(View.VISIBLE);
        }
    }


    private void getCurrentQuestion() {
        int currentQuestion = MyApplication.getInstance().getExamination().getCurrentQuestion();

        if (currentQuestion - 1 < MyApplication.getInstance().getExamination().getQuestions().size()) {
            mCurrentQuestion = MyApplication.getInstance().getExamination().getQuestions().get(currentQuestion - 1);

            mAdapter.notifyDataSetChanged();
        }

        layoutImage = (FrameLayout) findViewById(R.id.pager_container);
        pager = (ViewPager) findViewById(R.id.coverflow);

        PagerAdapter adapter = new MyPagerAdapter();
        pager.setPageTransformer(true, new ZoomOutPageTransformer(true));
        pager.setAdapter(adapter);
        //Necessary or the pager will only have one extra page to show
        // make this at least however many pages you can see
//        pager.setOffscreenPageLimit(adapter.getCount());
        pager.setOffscreenPageLimit(3);
        //A little space between pages
        pager.setPageMargin((int) getResources().getDimension(R.dimen.image_padding));
        //If hardware acceleration is enabled, you should also remove
        // clipping on the pager for its children.
        pager.setClipChildren(false);

        configNextButton();
    }


    private void configNextButton() {

        if (MyApplication.getInstance().getExamination().getCurrentQuestion() == 1) {
            mBtnPrev.setVisibility(View.GONE);
        } else {
            mBtnPrev.setVisibility(View.VISIBLE);
        }

        Answer answer1 = mCurrentQuestion.getAnswer1();
        Answer answer2 = mCurrentQuestion.getAnswer2();
        Answer answer3 = mCurrentQuestion.getAnswer3();
        Answer answer4 = mCurrentQuestion.getAnswer4();

        if (answer1.isSelected() || answer2.isSelected() || answer3.isSelected() || answer4.isSelected()) {
            mBtnNext.setEnabled(true);
            mBtnNext.setVisibility(View.VISIBLE);
//            mBtnNext.setBackgroundResource(R.drawable.btn_green_orange);

            if (MyApplication.getInstance().getExamination().getCurrentQuestion() == MyApplication.getInstance().getExamination().getTotalQuestion()) {
                mBtnNext.setText("ประมวลผลคะแนน");
            } else {
                mBtnNext.setText("ข้อถัดไป");
            }
        } else {
            mBtnNext.setEnabled(false);
            mBtnNext.setVisibility(View.GONE);
//            mBtnNext.setBackgroundResource(R.color.black_gray);
        }

    }

    private void _outletObject() {
        mListView = (ExpandableListView) findViewById(R.id.listView);
//        mListView.setExpanded(true);

        mBtnNext = (TextView) findViewById(R.id.btnNext);
        mBtnPrev = (TextView) findViewById(R.id.btnPrev);
        mBtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextAction(view);
            }
        });
        mBtnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prevAction(view);
            }
        });
        mAdapter = new LawExamAdapter(this);

        mListView.setAdapter(mAdapter);
        mListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                return true;
            }
        });

        mListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long l) {

                mCurrentQuestion.getAnswer1().setSelected(false);
                mCurrentQuestion.getAnswer2().setSelected(false);
                mCurrentQuestion.getAnswer3().setSelected(false);
                mCurrentQuestion.getAnswer4().setSelected(false);

                Answer answerSelected = mAdapter.getChild(groupPosition, childPosition);

                answerSelected.setSelected(true);

                mAdapter.notifyDataSetChanged();

                configNextButton();

                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            backAction(null);
        }

        return super.onOptionsItemSelected(item);
    }

    public void prevAction(View v) {
        MyApplication.getInstance().getExamination().setCurrentQuestion(
                MyApplication.getInstance().getExamination().getCurrentQuestion() - 1
        );

        getCurrentQuestion();
    }

    public void nextAction(View v) {

        // check correct answer
        Answer answer1 = mCurrentQuestion.getAnswer1();
        Answer answer2 = mCurrentQuestion.getAnswer2();
        Answer answer3 = mCurrentQuestion.getAnswer3();
        Answer answer4 = mCurrentQuestion.getAnswer4();

        boolean isMemberAnswerCorrect = false;

        if (answer1.isSelected()) {
            if (answer1.getId() == mCurrentQuestion.getCorrectAnswerID()) {
                isMemberAnswerCorrect = true;
            }

            mCurrentQuestion.setMemberAnswerID(answer1.getId());
        }

        if (answer2.isSelected()) {
            if (answer2.getId() == mCurrentQuestion.getCorrectAnswerID()) {
                isMemberAnswerCorrect = true;
            }

            mCurrentQuestion.setMemberAnswerID(answer2.getId());
        }

        if (answer3.isSelected()) {
            if (answer3.getId() == mCurrentQuestion.getCorrectAnswerID()) {
                isMemberAnswerCorrect = true;
            }

            mCurrentQuestion.setMemberAnswerID(answer3.getId());
        }

        if (answer4.isSelected()) {
            if (answer4.getId() == mCurrentQuestion.getCorrectAnswerID()) {
                isMemberAnswerCorrect = true;
            }

            mCurrentQuestion.setMemberAnswerID(answer4.getId());
        }

        // set member answerid to question
        ExaminationDA.saveMemberAnswer(LawExamActivity.this, mCurrentQuestion);

        MyApplication.getInstance().getExamination().setCorrectAnswer(ExaminationDA.countCorrectAnswer(LawExamActivity.this));
        MyApplication.getInstance().getExamination().setWrongAnswer(ExaminationDA.countWrongAnswer(LawExamActivity.this));
        /*
        // summary examination correct_answer, wrong_answer and next current question
        if ( isMemberAnswerCorrect ) {
            MyApplication.getInstance().getExamination().setCorrectAnswer(
                    MyApplication.getInstance().getExamination().getCorrectAnswer() + 1
            );
        } else {
            MyApplication.getInstance().getExamination().setWrongAnswer(
                    MyApplication.getInstance().getExamination().getWrongAnswer() + 1
            );
        }
        */

        if (MyApplication.getInstance().getExamination().getCurrentQuestion() < MyApplication.getInstance().getExamination().getTotalQuestion()) {


            MyApplication.getInstance().getExamination().setCurrentQuestion(
                    MyApplication.getInstance().getExamination().getCurrentQuestion() + 1
            );

            ExaminationDA.updateSummary(LawExamActivity.this, MyApplication.getInstance().getExamination());

            // get next current question
            getCurrentQuestion();

        } else {
            sendExamination();

        }


    }

    @Override
    public void refresh() {
        sendExamination();
    }

    private void sendExamination() {
        ExaminationDA.updateSummary(LawExamActivity.this, MyApplication.getInstance().getExamination());

        // send service to save member history and for summary report purpose
        Examination.send(this, MyApplication.getInstance().getExamination(), new Examination.OnSendExaminationListener() {
            @Override
            public void sendResult(Examination examination, int historyID, String certificatePath, Error error) {
                if (error == null) {
                    Intent intent = new Intent(LawExamActivity.this, LawExamResultActivity.class);

                    intent.putExtra("law_section", lawSection);
                    intent.putExtra("examination", MyApplication.getInstance().getExamination());

                    intent.putExtra("history_id", historyID);
                    intent.putExtra("certificate", certificatePath);

                    startActivity(intent);

                    ExaminationDA.clearExamination(LawExamActivity.this);

                    MyApplication.getInstance().setExamination(null);

                    finish();
                } else {
                    retryService(error.getMessage());
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        backAction(null);
    }

    public void backAction(View v) {

        // get prompts.xml view
        LayoutInflater li = LayoutInflater.from(this);
        View alertView = li.inflate(R.layout.alertlayout, null);

        TextView textView1 = (TextView) alertView.findViewById(R.id.textView1);

        textView1.setText("คุณต้องการออกจากแบบทดสอบนี้ใช่หรือไม่");

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(alertView);

        //final EditText userInput = (EditText) alertView.findViewById(R.id.editTextDialogUserInput);

        // set dialog message
        alertDialogBuilder
                //.setTitle( "รับเกียรติบัตร" )
                .setCancelable(false)
                .setPositiveButton("ใช่", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(LawExamActivity.this, MainActivity.class);

                        intent.putExtra("page", 1);

                        startActivity(intent);

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

    private class LawExamAdapter extends BaseExpandableListAdapter {

        private Context mContext;
        private LayoutInflater mInflater;

        private class ViewHolder {
            public TextView lblAnswer;
            public ImageView imgCheck;
        }

        public LawExamAdapter(Context context) {
            this.mContext = context;
            this.mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getGroupCount() {
            return 2;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            if (groupPosition == 0) return 0;
            else if (groupPosition == 1) return 4;
            return 0;
        }

        @Override
        public Object getGroup(int groupPosition) {
            return null;
        }

        @Override
        public Answer getChild(int groupPosition, int childPosition) {
            switch (childPosition) {
                case 0:
                    return mCurrentQuestion.getAnswer1();
                case 1:
                    return mCurrentQuestion.getAnswer2();
                case 2:
                    return mCurrentQuestion.getAnswer3();
                case 3:
                    return mCurrentQuestion.getAnswer4();

            }
            return null;
        }

        @Override
        public long getGroupId(int groupPosition) {
            return 0;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return 0;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean b, View view, ViewGroup viewGroup) {
            if (groupPosition == 0) {
                View row = view;

                ExpandableListView mExpandableListView = (ExpandableListView) viewGroup;
                mExpandableListView.expandGroup(groupPosition);

                if (row == null) {
                    row = mInflater.inflate(R.layout.group_law_exam_header, viewGroup, false);
                }

                TextView lblLevel = (TextView) row.findViewById(R.id.lblLevel);
                TextView lblNumberStatus = (TextView) row.findViewById(R.id.lblNumberStatus);

                lblLevel.setText(MyApplication.getInstance().getExamination().getExaminationLevelName());
                lblNumberStatus.setText(MyApplication.getInstance().getExamination().getCurrentQuestion() + " / " + MyApplication.getInstance().getExamination().getTotalQuestion());

                return row;
            } else if (groupPosition == 1) {
                View row = view;

                ExpandableListView mExpandableListView = (ExpandableListView) viewGroup;
                mExpandableListView.expandGroup(groupPosition);

                if (row == null) {
                    row = mInflater.inflate(R.layout.group_law_exam_question, viewGroup, false);
                }

                TextView lblTitle = (TextView) row.findViewById(R.id.lblTitle);

                lblTitle.setText(mCurrentQuestion.getSubject());

                return row;
            }

            return null;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean b, View view, ViewGroup viewGroup) {

            View row = view;
            ViewHolder holder;

            if (row == null) {
                row = mInflater.inflate(R.layout.row_exam_answer, viewGroup, false);

                holder = new ViewHolder();
                holder.lblAnswer = (TextView) row.findViewById(R.id.lblAnswer);
                holder.imgCheck = (ImageView) row.findViewById(R.id.imgCheck);
            } else {
                holder = (ViewHolder) row.getTag();
            }

            Answer answer = getChild(groupPosition, childPosition);

            holder.lblAnswer.setText(answer.getAnswer());

            if (answer.isSelected()) {
                holder.imgCheck.setImageResource(R.drawable.list_cheked);
                holder.lblAnswer.setTextColor(getResources().getColor(R.color.colorPrimary));
            } else {
                holder.imgCheck.setImageResource(R.drawable.list_unchecked);
                holder.lblAnswer.setTextColor(getResources().getColor(R.color.black));
            }

            row.setTag(holder);

            return row;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }

    private class MyPagerAdapter extends PagerAdapter {
        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
//            Transformation transformation = new RoundedTransformationBuilder()
//                    .cornerRadiusDp(10)
//                    .oval(false)
//                    .build();

            LayoutInflater inflater = LayoutInflater.from(LawExamActivity.this);
            ViewGroup view = (ViewGroup) inflater.inflate(R.layout.pager_item,
                    container, false);
//            View view = View.inflate(LawContentDetailActivity.this, R.layout.pager_item, container);
            ImageView play = (ImageView) view.findViewById(R.id.play);
            if (images.get(position).contains("youtube")) {
                play.setVisibility(View.VISIBLE);
            } else {
                play.setVisibility(View.GONE);
            }
            play.setImageResource(R.drawable.play);
            play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent in = new Intent(LawExamActivity.this, PlayerYoutube.class);
//                    in.putExtra("link", mContent.getClip().split("v=")[1]);
                    startActivity(in);
                }
            });
            ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent in = new Intent(LawExamActivity.this, PhotoViewActivity.class);
                    in.putExtra("image", images.get(position));
                    startActivity(in);
                }
            });
            Picasso.with(LawExamActivity.this).load(images.get(position)).into(imageView);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return images.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return (view == object);
        }
    }
}
