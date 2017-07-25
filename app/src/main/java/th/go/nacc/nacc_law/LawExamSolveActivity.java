package th.go.nacc.nacc_law;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import th.go.nacc.nacc_law.model.Answer;
import th.go.nacc.nacc_law.model.Question;

/**
 * Created by nontachai on 7/11/15 AD.
 */
public class LawExamSolveActivity extends AbstractActivity {

    private ExpandableListView mListView;

    private LawExamAdapter mAdapter;

    private int lawSection;
    private List<Question> mQuestions = new ArrayList<Question>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_law_exam_solve);

        Bundle data = getIntent().getExtras();

        if (data != null) {
            lawSection = data.getInt("law_section");
            mQuestions = data.getParcelableArrayList("questions");

            /*
            for ( int i = 0, maxQuestion = mQuestions.size(); i < maxQuestion; i++ ) {
                Question question = mQuestions.get( i );

                Answer answer1 = question.getAnswer1();
                Answer answer2 = question.getAnswer2();
                Answer answer3 = question.getAnswer3();
                Answer answer4 = question.getAnswer4();

                if ( answer1.getId() == question.getCorrectAnswerID() ) {
                    answer1.setSelected( true );
                }

            }
            */
        }

//        final ActionBar actionBar = getActionBar();
//
//
//        actionBar.setDisplayHomeAsUpEnabled(true);
//
//        BitmapDrawable background = new BitmapDrawable(getResources(), BitmapFactory.decodeResource(getResources(), R.drawable.top_bar));
//        background.setTileModeX(android.graphics.Shader.TileMode.REPEAT);
//        actionBar.setBackgroundDrawable(background);

        _outletObject();

        if (lawSection == 103) {
            setTitle("เฉลยคำตอบมาตรา 103");
        } else if (lawSection == 100) {
            setTitle("เฉลยคำตอบมาตรา 100");
        }
    }

    private void _outletObject() {
        mListView = (ExpandableListView) findViewById(R.id.listView);

        mAdapter = new LawExamAdapter(this);

        mListView.setAdapter(mAdapter);
        mListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                return true;
            }
        });
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

    private class LawExamAdapter extends BaseExpandableListAdapter {

        private Context mContext;
        private LayoutInflater mInflater;

        private class ViewHolder {
            public ImageView imgCheck;
            public TextView lblAnswer;
        }


        public LawExamAdapter(Context context) {


            this.mContext = context;
            this.mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getGroupCount() {
            return mQuestions.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {

            Question question = getGroup(groupPosition);

            if (question.getExplain() != null && !question.getExplain().equals("")) {
                return 5;
            }

            return 4;
        }

        @Override
        public Question getGroup(int groupPosition) {
            return mQuestions.get(groupPosition);
        }

        @Override
        public Answer getChild(int groupPosition, int childPosition) {

            Question question = getGroup(groupPosition);

            switch (childPosition) {
                case 0:
                    return question.getAnswer1();
                case 1:
                    return question.getAnswer2();
                case 2:
                    return question.getAnswer3();
                case 3:
                    return question.getAnswer4();
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
            View row = view;

            ExpandableListView mExpandableListView = (ExpandableListView) viewGroup;
            mExpandableListView.expandGroup(groupPosition);

            if (row == null) {
                row = mInflater.inflate(R.layout.group_law_exam_question, viewGroup, false);
            }

            TextView lblTitle = (TextView) row.findViewById(R.id.lblTitle);

            Question question = getGroup(groupPosition);

            lblTitle.setText(question.getSubject());

            return row;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean b, View view, ViewGroup viewGroup) {

            View row = view;
            ViewHolder holder;

            if (row == null) {
                row = mInflater.inflate(R.layout.row_exam_answer, viewGroup, false);

                holder = new ViewHolder();
                holder.imgCheck = (ImageView) row.findViewById(R.id.imgCheck);
                holder.lblAnswer = (TextView) row.findViewById(R.id.lblAnswer);
            } else {
                holder = (ViewHolder) row.getTag();
            }

            row.setTag(holder);

            Question question = getGroup(groupPosition);
            Answer answer = getChild(groupPosition, childPosition);

            if (answer == null) {
                holder.imgCheck.setImageResource(R.drawable.list_wrong);
                holder.lblAnswer.setText("คำอธิบาย: " + question.getExplain());
                holder.lblAnswer.setTextColor(getResources().getColor(R.color.red));
            } else {
                holder.lblAnswer.setText(answer.getAnswer());

                if (answer.isSelected()) {
                    if (answer.getId() == question.getCorrectAnswerID()) {
                        holder.imgCheck.setImageResource(R.drawable.list_right);
                        holder.lblAnswer.setTextColor(getResources().getColor(R.color.green));
                    }else{
                        holder.imgCheck.setImageResource(R.drawable.list_wrong);
                        holder.lblAnswer.setTextColor(getResources().getColor(R.color.red));
                    }
                } else {

                    if (answer.getId() == question.getCorrectAnswerID()) {
                        holder.imgCheck.setImageResource(R.drawable.list_right);
                        holder.lblAnswer.setTextColor(getResources().getColor(R.color.green));
                    } else {
                        holder.imgCheck.setImageResource(R.drawable.list_unchecked);
                        holder.lblAnswer.setTextColor(Color.BLACK);
                    }
                }


            }

            return row;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return false;
        }
    }
}
