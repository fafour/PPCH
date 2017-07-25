package th.go.nacc.nacc_law.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import th.go.nacc.nacc_law.LawContentDetailActivity;
import th.go.nacc.nacc_law.LawPDFDetailActivity;
import th.go.nacc.nacc_law.R;
import th.go.nacc.nacc_law.application.MyApplication;
import th.go.nacc.nacc_law.model.SectionContent;
import th.go.nacc.nacc_law.model.SectionPDF;

import static th.go.nacc.nacc_law.R.drawable.pdf;


public class SearchFragment2 extends AbstractFragment {


    private ExpandableListView mListView;
    private LawContentAdapter mAdapter;

    public List<SectionPDF> pdf100 = new ArrayList<>();
    public List<SectionContent> contents100 = new ArrayList<>();
    public List<SectionPDF> pdf103 = new ArrayList<>();
    public List<SectionContent> contents103 = new ArrayList<>();
    String search = "";
    private EditText edtSearch;
    private TextView btnSearch;
    private int lawSection = 100;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        setTitle("ค้นหา กฏหมาย ป.ป.ช.");

        mListView = (ExpandableListView) view.findViewById(R.id.listView);
        edtSearch = (EditText) view.findViewById(R.id.edt_search);
        btnSearch = (TextView) view.findViewById(R.id.btn_cancel);
        pdf100.clear();
        contents100.clear();
        pdf103.clear();
        contents103.clear();

        final TextView btn100 = (TextView) view.findViewById(R.id.btn_100);
        final TextView btn103 = (TextView) view.findViewById(R.id.btn_103);
        btn100.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn100.setSelected(true);
                btn103.setSelected(false);
                lawSection = 100;
                _outletObject();
            }
        });
        btn103.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn100.setSelected(false);
                btn103.setSelected(true);
                lawSection = 103;
                _outletObject();
            }
        });
        btn100.setSelected(true);

        pdf100.addAll(MyApplication.getInstance().pdf100);
        contents100.addAll(MyApplication.getInstance().contents100);
        pdf103.addAll(MyApplication.getInstance().pdf103);
        contents103.addAll(MyApplication.getInstance().contents103);

        edtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    pdf100.clear();

                    for (int k = 0; k < MyApplication.getInstance().pdf100.size(); k++) {
                        if (MyApplication.getInstance().pdf100.get(k).getTitle().contains(edtSearch.getText().toString())) {
                            pdf100.add(MyApplication.getInstance().pdf100.get(k));

                        }
                    }

                    pdf103.clear();

                    for (int k = 0; k < MyApplication.getInstance().pdf103.size(); k++) {
                        if (MyApplication.getInstance().pdf103.get(k).getTitle().contains(edtSearch.getText().toString())) {

                            pdf103.add(MyApplication.getInstance().pdf103.get(k));

                        }
                    }

                    contents100.clear();

                    for (int k = 0; k < MyApplication.getInstance().contents100.size(); k++) {
                        if (MyApplication.getInstance().contents100.get(k).getTitle().contains(edtSearch.getText().toString()) ||
                                MyApplication.getInstance().contents100.get(k).getDetail().contains(edtSearch.getText().toString())) {
                            contents100.add(MyApplication.getInstance().contents100.get(k));

                        }
                    }

                    contents103.clear();

                    for (int k = 0; k < MyApplication.getInstance().contents103.size(); k++) {
                        if (MyApplication.getInstance().contents103.get(k).getTitle().contains(edtSearch.getText().toString()) ||
                                MyApplication.getInstance().contents103.get(k).getDetail().contains(edtSearch.getText().toString())) {
                            contents103.add(MyApplication.getInstance().contents103.get(k));

                        }
                    }


                    _outletObject();


                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(edtSearch.getWindowToken(), 0);


                    return true;
                }
                return false;
            }
        });


        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtSearch.setText("");
                pdf100.clear();
                contents100.clear();
                pdf103.clear();
                contents103.clear();

                pdf100.addAll(MyApplication.getInstance().pdf100);
                contents100.addAll(MyApplication.getInstance().contents100);
                pdf103.addAll(MyApplication.getInstance().pdf103);
                contents103.addAll(MyApplication.getInstance().contents103);

                _outletObject();
            }
        });
        btnBack().setVisibility(View.GONE);

        _outletObject();

        return view;
    }


    private void _outletObject() {


        mAdapter = new LawContentAdapter(getActivity());

        mListView.setAdapter(mAdapter);
        mListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int groupPosition, long childPosition) {
                return true;
            }
        });

        mListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long l) {

                if (groupPosition == 0) {
                    Intent intent = new Intent(getActivity(), LawContentDetailActivity.class);

                    intent.putExtra("law_section", lawSection);

                    if (lawSection == 100) {
                        intent.putExtra("content", contents100.get(childPosition));
                        intent.putParcelableArrayListExtra("contents", (ArrayList<? extends Parcelable>) contents100);
                    } else if (lawSection == 103) {
                        intent.putExtra("content", contents103.get(childPosition));
                        intent.putParcelableArrayListExtra("contents", (ArrayList<? extends Parcelable>) contents103);
                    }

                    startActivity(intent);
                } else if (groupPosition == 1) {
                    Intent intent = new Intent(getActivity(), LawPDFDetailActivity.class);

                    intent.putExtra("law_section", lawSection);

                    if (lawSection == 100) {
                        intent.putExtra("pdf", pdf100.get(childPosition));
                    } else if (lawSection == 103) {
                        intent.putExtra("pdf", pdf103.get(childPosition));
                    }

                    startActivity(intent);
                }

                return false;
            }
        });
    }


    private class LawContentAdapter extends BaseExpandableListAdapter {

        private Context mContext;

        private LayoutInflater mInflater;

        private class ViewHolder {
            TextView lblNo, lblTitle;
        }

        public LawContentAdapter(Context context) {
            this.mContext = context;
            this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getGroupCount() {
            if (lawSection == 100) {
                if (pdf100 != null
                        && pdf100.size() > 0) {
                    return 2;
                } else {
                    return 1;
                }
            } else if (lawSection == 103) {
                if (pdf103 != null
                        && pdf103.size() > 0) {
                    return 2;
                } else {
                    return 1;
                }
            }
            return 0;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            if (lawSection == 100) {
                if (groupPosition == 0) return contents100.size();
                else if (groupPosition == 1) return pdf100.size();
                return 0;
            } else if (lawSection == 103) {
                if (groupPosition == 0) return contents103.size();
                else if (groupPosition == 1) return pdf103.size();
                return 0;
            }

            return 0;
        }

        @Override
        public Object getGroup(int groupPosition) {
            return null;
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
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
                row = mInflater.inflate(R.layout.group_law_content, viewGroup, false);
            }
//            row.setVisibility(View.GONE);
            TextView lblTitle = (TextView) row.findViewById(R.id.lblTitle);
            ImageView imgIcon = (ImageView) row.findViewById(R.id.imgIcon);

            if (groupPosition == 0) {
                lblTitle.setText("เนื้อหาสาระ");
                imgIcon.setImageResource(R.drawable.document_edit);
            } else if (groupPosition == 1) {
                lblTitle.setText("ข้อมูลเพิ่มเติม");
                imgIcon.setImageResource(pdf);
            }

            return row;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean b, View view, ViewGroup viewGroup) {
            View row = view;
            ViewHolder holder;

            if (row == null) {
                row = mInflater.inflate(R.layout.row_law_content, viewGroup, false);

                holder = new ViewHolder();
                holder.lblNo = (TextView) row.findViewById(R.id.lblNo);
                holder.lblTitle = (TextView) row.findViewById(R.id.lblTitle);
            } else {
                holder = (ViewHolder) row.getTag();
            }

            row.setTag(holder);

            holder.lblNo.setText(String.valueOf(childPosition + 1));

            if (groupPosition == 0) {
                SectionContent content = null;

                if (lawSection == 100) {
                    content = contents100.get(childPosition);
                } else if (lawSection == 103) {
                    content = contents103.get(childPosition);
                }

                if (content != null) {
                    holder.lblTitle.setText(content.getTitle());
                }
            } else if (groupPosition == 1) {
                SectionPDF pdf = null;

                if (lawSection == 100) {
                    pdf = pdf100.get(childPosition);
                } else if (lawSection == 103) {
                    pdf = pdf103.get(childPosition);
                }

                if (pdf != null) {
                    holder.lblTitle.setText(pdf.getTitle());
                }
            }

            return row;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
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
