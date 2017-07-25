package th.go.nacc.nacc_law.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import th.go.nacc.nacc_law.CertificateResultActivity;
import th.go.nacc.nacc_law.LawExamActivity;
import th.go.nacc.nacc_law.R;
import th.go.nacc.nacc_law.application.MyApplication;
import th.go.nacc.nacc_law.da.ExaminationDA;
import th.go.nacc.nacc_law.model.Examination;
import th.go.nacc.nacc_law.model.ExaminationLevelList;
import th.go.nacc.nacc_law.model.Section;

/**
 * Created by nontachai on 7/10/15 AD.
 */
public class LawTestLevelListFragment extends AbstractFragment {

    private ListView mListView;

    private LevelListAdapter mAdapter;

    private int lawSection = 100;
    private Section mSection;

    private List<ExaminationLevelList> mLevelList = new ArrayList<ExaminationLevelList>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_law_level_list, container, false);

        setTitle("แบบทดสอบ");

        for ( int i = 0, max = MyApplication.getInstance().sections.size(); i < max; i++ ) {
            Section section = MyApplication.getInstance().sections.get( i );

            if ( section.getAlias().equals( "section_" + String.valueOf( lawSection) ) ) {
                mSection = section;
                break;
            }
        }

        final TextView btn100 = (TextView) view.findViewById(R.id.btn_100);
        final TextView btn103 = (TextView) view.findViewById(R.id.btn_103);

        btn100.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn100.setSelected(true);
                btn103.setSelected(false);
                lawSection=100;
                for ( int i = 0, max = MyApplication.getInstance().sections.size(); i < max; i++ ) {
                    Section section = MyApplication.getInstance().sections.get( i );

                    if ( section.getAlias().equals( "section_" + String.valueOf( lawSection) ) ) {
                        mSection = section;
                        break;
                    }
                }
                _getData();


            }
        });
        btn103.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn100.setSelected(false);
                btn103.setSelected(true);
                lawSection=103;
                for ( int i = 0, max = MyApplication.getInstance().sections.size(); i < max; i++ ) {
                    Section section = MyApplication.getInstance().sections.get( i );

                    if ( section.getAlias().equals( "section_" + String.valueOf( lawSection) ) ) {
                        mSection = section;
                        break;
                    }
                }
                _getData();


            }
        });
        btn100.setSelected(true);

        btnBack().setVisibility(View.GONE);

        mListView = (ListView) view.findViewById( R.id.listView );

        mAdapter = new LevelListAdapter( getActivity() );

        mListView.setAdapter( mAdapter );
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                final ExaminationLevelList levelList = mAdapter.getItem(position);

                if (levelList.getMaxPoint() == -1 || levelList.getMaxPoint() < 80 ) {
                    askToConfirmEnterToExam(levelList);
                } else {
                    // ask for get certificate or enter to test
                    String[] items = new String[]{"ทำแบบทดสอบใหม่", "รับเกียรติบัตร", "ยกเลิก"};

                    AlertDialog dialog = new AlertDialog.Builder(getActivity())
                            .setTitle("แบบทดสอบ " + levelList.getDetail())
                            //.setMessage( "คุณต้องการรับเกียรติบัตรจากการทำแบบทดสอบครั้งก่อน หรือต้องการเข้าทำแบบทดสอบใหม่อีกครั้ง")
                            .setItems(items, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    switch (i) {
                                        case 0:
                                            askToConfirmEnterToExam(levelList);
                                            break;
                                        case 1:
                                            Examination.getCertificate(getActivity(), MyApplication.getInstance().getMember().getId(), levelList.getId(), new Examination.OnGetCertificateListener() {
                                                @Override
                                                public void certificateResult(int historyID, String rsCertificatePath, Error error) {
                                                    if (error == null) {
                                                        Intent intent = new Intent(getActivity(), CertificateResultActivity.class);

                                                        intent.putExtra("certificate", rsCertificatePath);
                                                        intent.putExtra("history_id", historyID);

                                                        startActivity(intent);
                                                    } else {
                                                        simpleAlert(error.getMessage());
                                                    }
                                                }
                                            });
                                            break;
                                        case 2:
                                            break;

                                    }
                                }
                            })
                            .show();

                    /*
                    TextView textView = (TextView) dialog.findViewById(android.R.id.message);
                    Typeface face = Typeface.createFromAsset(getAssets(), "Font-Black.ttf");
                    textView.setTypeface(face);
                    */
                }
            }
        });

        _getData();

        return view;
    }



    private void _getData () {
        ExaminationLevelList.get(getActivity(), mSection.getId(), MyApplication.getInstance().getMember().getId(), new ExaminationLevelList.OnGetListener() {
            @Override
            public void getResult(List<ExaminationLevelList> levelLists, Error error) {
                if (error == null) {
                    mLevelList = levelLists;

                    mAdapter.notifyDataSetChanged();
                } else {

                    retryService( error.getMessage() );
                }
            }
        });
    }

    @Override
    public void refresh () {
        _getData();
    }



    private void askToConfirmEnterToExam ( final ExaminationLevelList levelList ) {

        // get prompts.xml view
        LayoutInflater li = LayoutInflater.from(getActivity());
        View alertView = li.inflate(R.layout.alertlayout, null);

        TextView textView1 = (TextView) alertView.findViewById( R.id.textView1 );

        textView1.setText( "คุณต้องการเข้าทำแบบทดสอบ " + levelList.getDetail() + " ใช่หรือไม่?" );

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(alertView);

        // set dialog message
        alertDialogBuilder
                //.setTitle( "รับเกียรติบัตร" )
                .setCancelable(false)
                .setPositiveButton("ใช่", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        getExamQuestion(levelList);
                    }
                })
                .setNegativeButton( "ยกเลิก", null );

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();

        /*
        AlertDialog dialog = new AlertDialog.Builder( LawLevelListActivity.this )
                .setTitle( "เข้าทำแบบทดสอบ" )
                .setMessage( "คุณต้องการเข้าทำแบบทดสอบ " + levelList.getDetail() + " ใช่หรือไม่?" )
                .setPositiveButton("ใช่", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        getExamQuestion( levelList );

                    }
                })
                .setNegativeButton( "ยกเลิก", null )
                .show();
        */

        /*
        TextView textView = (TextView) dialog.findViewById(android.R.id.message);
        Typeface face = Typeface.createFromAsset(getAssets(), "Font-Black.ttf");
        textView.setTypeface(face);
        */
    }


    private void getExamQuestion ( final ExaminationLevelList levelList ) {
        Examination.get(getActivity(), MyApplication.getInstance().getMember().getId(), mSection.getId(), levelList, new Examination.OnGetExaminationListener() {
            @Override
            public void examinationResult(Examination examination, Error error) {
                if ( error == null ) {
                    /*
                    Log.i( "EXAMINATION_RESULT", "LEVEL LIST ID: " + examination.getExaminationLevel());
                    Log.i( "EXAMINATION_RESULT", "SECTION ID: " + examination.getSection());
                    Log.i( "EXAMINATION_RESULT", "MEMBER ID: " + examination.getMember());
                    Log.i( "EXAMINATION_RESULT", "TOTAL QUESTION: " + examination.getTotalQuestion());

                    for ( int i = 0, maxQuestion = examination.getQuestions().size(); i < maxQuestion; i++ ) {
                        Question question = examination.getQuestions().get( i );

                        Log.i( "EXAMINATION_RESULT", "QUESTION " + (i+1) + " ID: " + question.getId());
                        Log.i( "EXAMINATION_RESULT", "QUESTION " + (i+1) + " SUBJECT: " + question.getSubject());
                        Log.i( "EXAMINATION_RESULT", "QUESTION " + (i+1) + " CORRENT ANSWER ID: " + question.getCorrectAnswerID());


                        Log.i( "EXAMINATION_RESULT", "QUESTION " + (i+1) + " ANSWER 1: " + question.getAnswer1().getAnswer() + ", IS RIGHT: " + question.getAnswer1().getIsRight());
                        Log.i( "EXAMINATION_RESULT", "QUESTION " + (i+1) + " ANSWER 2: " + question.getAnswer2().getAnswer() + ", IS RIGHT: " + question.getAnswer2().getIsRight());
                        Log.i( "EXAMINATION_RESULT", "QUESTION " + (i+1) + " ANSWER 3: " + question.getAnswer3().getAnswer() + ", IS RIGHT: " + question.getAnswer3().getIsRight());
                        Log.i( "EXAMINATION_RESULT", "QUESTION " + (i+1) + " ANSWER 4: " + question.getAnswer4().getAnswer() + ", IS RIGHT: " + question.getAnswer4().getIsRight());

                    }
                    */

                    // set current timestamp of start examination!
                    examination.setStart(String.valueOf(System.currentTimeMillis()));

                    // save examination to db and get from db into variable
                    examination = ExaminationDA.saveExamination(getActivity(), examination);

                    /*
                    Log.i( "EXAMINATION_AFTER", "LEVEL LIST ID: " + examination.getExaminationLevel());
                    Log.i( "EXAMINATION_AFTER", "SECTION ID: " + examination.getSection());
                    Log.i("EXAMINATION_AFTER", "MEMBER ID: " + examination.getMember());
                    Log.i( "EXAMINATION_AFTER", "TOTAL QUESTION: " + examination.getTotalQuestion());

                    for ( int i = 0, maxQuestion = examination.getQuestions().size(); i < maxQuestion; i++ ) {
                        Question question = examination.getQuestions().get( i );

                        Log.i( "EXAMINATION_AFTER", "QUESTION " + (i+1) + " ID: " + question.getId());
                        Log.i( "EXAMINATION_AFTER", "QUESTION " + (i+1) + " SUBJECT: " + question.getSubject());
                        Log.i( "EXAMINATION_AFTER", "QUESTION " + (i+1) + " CORRENT ANSWER ID: " + question.getCorrectAnswerID());


                        Log.i("EXAMINATION_AFTER", "QUESTION " + (i + 1) + " ANSWER 1: " + question.getAnswer1().getAnswer() + ", IS RIGHT: " + question.getAnswer1().getIsRight());
                        Log.i( "EXAMINATION_AFTER", "QUESTION " + (i+1) + " ANSWER 2: " + question.getAnswer2().getAnswer() + ", IS RIGHT: " + question.getAnswer2().getIsRight());
                        Log.i( "EXAMINATION_AFTER", "QUESTION " + (i+1) + " ANSWER 3: " + question.getAnswer3().getAnswer() + ", IS RIGHT: " + question.getAnswer3().getIsRight());
                        Log.i( "EXAMINATION_AFTER", "QUESTION " + (i+1) + " ANSWER 4: " + question.getAnswer4().getAnswer() + ", IS RIGHT: " + question.getAnswer4().getIsRight());

                    }
                    */

                    MyApplication.getInstance().setExamination ( examination );

                    if ( MyApplication.getInstance().getExamination() == null ) {
                        simpleAlert( "ไม่พบข้อมูลแบบทดสอบ กรุณาติดต่อผู้ดูแลระบบ" );
                    } else {

                        // start examination
                        ExaminationDA.start( getActivity() );
                        MyApplication.getInstance().getExamination().setCurrentQuestion( 1 );

                        Intent intent = new Intent(getActivity(), LawExamActivity.class);

                        intent.putExtra( "law_section", lawSection );

                        startActivity(intent);

//                        getActivity().finish();
                    }
                } else {
                    simpleAlert(error.getMessage());
                }
            }
        });

    }



    private class LevelListAdapter extends BaseAdapter {

        private Context mContext;
        private LayoutInflater mInflater;

        private class ViewHolder {
            private TextView lblPoint, lblLevel, lblName;
            private RatingBar ratingBar;
        }

        public LevelListAdapter ( Context context ) {
            this.mContext = context;
            this.mInflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        }


        @Override
        public int getCount() {
            return mLevelList.size();
        }

        @Override
        public ExaminationLevelList getItem(int i) {
            return mLevelList.get( i );
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            View row = view;
            ViewHolder holder;


            if ( row == null ) {
                row = mInflater.inflate( R.layout.row_law_exam_level, viewGroup, false );

                holder = new ViewHolder();
                holder.lblPoint = (TextView) row.findViewById( R.id.lblPoint );
                holder.lblLevel = (TextView) row.findViewById( R.id.lblLevel );
                holder.lblName = (TextView) row.findViewById( R.id.lblName );
                holder.ratingBar = (RatingBar) row.findViewById( R.id.rating );
            } else {
                holder = (ViewHolder) row.getTag();
            }


            row.setTag( holder );

            ExaminationLevelList levelList = getItem( position );

            holder.lblName.setText( levelList.getDetail() );
            holder.lblLevel.setText( String.valueOf( position+1) );
            LayerDrawable stars = (LayerDrawable) holder.ratingBar.getProgressDrawable();
            stars.getDrawable(2).setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
            holder.ratingBar.setRating(position+1);

            if ( levelList.getMaxPoint() == -1 ) {
                holder.lblPoint.setText( "ยังไม่มีบันทึกคะแนน" );
//                holder.lblPoint.setTextColor(getResources().getColor(R.color.orange));
            } else {
                holder.lblPoint.setText( "คะแนนสูงสุด " + String.valueOf( levelList.getMaxPoint() ) + " คะแนน" );
//                holder.lblPoint.setTextColor(getResources().getColor(R.color.blue));
            }

            return row;
        }
    }
    @Override
    public void onResume() {
        super.onResume();
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
}
