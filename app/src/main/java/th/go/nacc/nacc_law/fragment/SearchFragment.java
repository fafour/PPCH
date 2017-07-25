//package th.go.nacc.nacc_law.fragment;
//
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.os.Parcelable;
//import android.support.annotation.Nullable;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseExpandableListAdapter;
//import android.widget.EditText;
//import android.widget.ExpandableListView;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import th.go.nacc.nacc_law.LawContentDetailActivity;
//import th.go.nacc.nacc_law.LawPDFDetailActivity;
//import th.go.nacc.nacc_law.R;
//import th.go.nacc.nacc_law.application.MyApplication;
//import th.go.nacc.nacc_law.model.SectionContent;
//import th.go.nacc.nacc_law.model.SectionPDF;
//
//
//public class SearchFragment extends AbstractFragment {
//
//
//    private ExpandableListView mListView;
//    private LawContentAdapter mAdapter;
//
//    public List<SectionPDF> pdf = new ArrayList<>();
//    public List<SectionContent> contents = new ArrayList<>();
//    String search = "";
//    private EditText edtSearch;
//    private TextView btnSearch;
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_search, container, false);
//        setTitle("ค้นหา กฏหมาย ป.ป.ช.");
//
//        mListView = (ExpandableListView) view.findViewById(R.id.listView);
//         edtSearch = (EditText) view.findViewById(R.id.edt_search);
//         btnSearch = (TextView) view.findViewById(R.id.btn_cancel);
//        pdf.clear();
//        contents.clear();
//
//        final TextView btn100 = (TextView) view.findViewById(R.id.btn_100);
//        final TextView btn103 = (TextView) view.findViewById(R.id.btn_103);
//        btn100.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                btn100.setSelected(true);
//                btn103.setSelected(false);
//                _outletObject();
//            }
//        });
//        btn103.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                btn100.setSelected(false);
//                btn103.setSelected(true);
//                _outletObject();
//            }
//        });
//        btn100.setSelected(true);
//
//        pdf.addAll(MyApplication.getInstance().pdf);
//        contents.addAll(MyApplication.getInstance().contents);
//
//        edtSearch.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                pdf.clear();
//                for (int k = 0; k < MyApplication.getInstance().pdf.size(); k++) {
//                    if (MyApplication.getInstance().pdf.get(k).getTitle().contains(edtSearch.getText().toString())) {
//                        pdf.add(MyApplication.getInstance().pdf.get(k));
//                    }
//                }
//                contents.clear();
//                for (int k = 0; k < MyApplication.getInstance().contents.size(); k++) {
//                    if (MyApplication.getInstance().contents.get(k).getTitle().contains(edtSearch.getText().toString()) ||
//                            MyApplication.getInstance().contents.get(k).getDetail().contains(edtSearch.getText().toString())) {
//                        contents.add(MyApplication.getInstance().contents.get(k));
//                    }
//                }
//                _outletObject();
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//            }
//        });
//
//        btnSearch.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                edtSearch.setText("");
////                pdf.clear();
////                contents.clear();
////                pdf.addAll(MyApplication.getInstance().pdf);
////                contents.addAll(MyApplication.getInstance().contents);
////                _outletObject();
//            }
//        });
//
//
//        _outletObject();
//
//        return view;
//    }
//
//
//    private void _outletObject() {
//
//
//        mAdapter = new LawContentAdapter(getActivity());
//
//        mListView.setAdapter(mAdapter);
//        mListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
//            @Override
//            public boolean onGroupClick(ExpandableListView expandableListView, View view, int groupPosition, long childPosition) {
//                return true;
//            }
//        });
//
//        mListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
//            @Override
//            public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long l) {
//
//                if (groupPosition == 0) {
//                    Intent intent = new Intent(getActivity(), LawContentDetailActivity.class);
//
//                    intent.putExtra("law_section", contents.get(childPosition).getSection());
//
////                    if (lawSection == 100) {
//                    intent.putExtra("content",contents.get(childPosition));
//                    intent.putParcelableArrayListExtra("contents", (ArrayList<? extends Parcelable>) contents);
////                    } else if (lawSection == 103) {
////                        intent.putExtra("content", MyApplication.getInstance().contents103.get(childPosition));
////                        intent.putParcelableArrayListExtra("contents", (ArrayList<? extends Parcelable>) MyApplication.getInstance().contents103);
////                    }
//
//                    startActivity(intent);
//                } else if (groupPosition == 1) {
//                    Intent intent = new Intent(getActivity(), LawPDFDetailActivity.class);
//
////                    intent.putExtra("law_section", lawSection);
//                    intent.putExtra("law_section", pdf.get(childPosition).getSection());
////                    if (lawSection == 100) {
//                    intent.putExtra("pdf", pdf.get(childPosition));
////                    } else if (lawSection == 103) {
////                        intent.putExtra("pdf", MyApplication.getInstance().pdf103.get(childPosition));
////                    }
//
//                    startActivity(intent);
//                }
//
//                return false;
//            }
//        });
//    }
//
//
//    private class LawContentAdapter extends BaseExpandableListAdapter {
//
//        private Context mContext;
//
//        private LayoutInflater mInflater;
//
//        private class ViewHolder {
//            TextView lblNo, lblTitle;
//        }
//
//        public LawContentAdapter(Context context) {
//            this.mContext = context;
//            this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        }
//
//        @Override
//        public int getGroupCount() {
////            if (lawSection == 100) {
//            if (pdf != null
//                    && pdf.size() > 0) {
//                return 2;
//            } else {
//                return 1;
//            }
////            } else if (lawSection == 103) {
////                if (MyApplication.getInstance().pdf103 != null
////                        && MyApplication.getInstance().pdf103.size() > 0) {
////                    return 2;
////                } else {
////                    return 1;
////                }
////            }
////            return 0;
//        }
//
//        @Override
//        public int getChildrenCount(int groupPosition) {
////            if (lawSection == 100) {
//            if (groupPosition == 0) return contents.size();
//            else if (groupPosition == 1) return pdf.size();
//            return 0;
////            } else if (lawSection == 103) {
////                if (groupPosition == 0) return MyApplication.getInstance().contents103.size();
////                else if (groupPosition == 1) return MyApplication.getInstance().pdf103.size();
////                return 0;
////            }
//
////            return 0;
//        }
//
//        @Override
//        public Object getGroup(int groupPosition) {
//            return null;
//        }
//
//        @Override
//        public Object getChild(int groupPosition, int childPosition) {
//            return null;
//        }
//
//        @Override
//        public long getGroupId(int groupPosition) {
//            return 0;
//        }
//
//        @Override
//        public long getChildId(int groupPosition, int childPosition) {
//            return 0;
//        }
//
//        @Override
//        public boolean hasStableIds() {
//            return false;
//        }
//
//        @Override
//        public View getGroupView(int groupPosition, boolean b, View view, ViewGroup viewGroup) {
//            View row = view;
//
//
//            ExpandableListView mExpandableListView = (ExpandableListView) viewGroup;
//            mExpandableListView.expandGroup(groupPosition);
//
//            if (row == null) {
//                row = mInflater.inflate(R.layout.group_law_content, viewGroup, false);
//            }
////            row.setVisibility(View.GONE);
//            TextView lblTitle = (TextView) row.findViewById(R.id.lblTitle);
//            ImageView imgIcon = (ImageView) row.findViewById(R.id.imgIcon);
//
//            if (groupPosition == 0) {
//                lblTitle.setText("เนื้อหาสาระ");
//                imgIcon.setImageResource(R.drawable.document_edit);
//            } else if (groupPosition == 1) {
//                lblTitle.setText("ข้อมูลเพิ่มเติม");
//                imgIcon.setImageResource(R.drawable.pdf);
//            }
//
//            return row;
//        }
//
//        @Override
//        public View getChildView(int groupPosition, int childPosition, boolean b, View view, ViewGroup viewGroup) {
//            View row = view;
//            ViewHolder holder;
//
//            if (row == null) {
//                row = mInflater.inflate(R.layout.row_law_content, viewGroup, false);
//
//                holder = new ViewHolder();
//                holder.lblNo = (TextView) row.findViewById(R.id.lblNo);
//                holder.lblTitle = (TextView) row.findViewById(R.id.lblTitle);
//            } else {
//                holder = (ViewHolder) row.getTag();
//            }
//
//            row.setTag(holder);
//
//            holder.lblNo.setText(String.valueOf(childPosition + 1));
//
//            if (groupPosition == 0) {
//                SectionContent content = null;
//
////                if (lawSection == 100) {
//                content = contents.get(childPosition);
////                } else if (lawSection == 103) {
////                    content = MyApplication.getInstance().contents103.get(childPosition);
////                }
//
//                if (content != null) {
//                    holder.lblTitle.setText(content.getTitle());
//                }
//            } else if (groupPosition == 1) {
//                SectionPDF pdfa = null;
//
////                if (lawSection == 100) {
//                pdfa =pdf.get(childPosition);
////                } else if (lawSection == 103) {
////                    pdf = MyApplication.getInstance().pdf103.get(childPosition);
////                }
//
//                if (pdfa != null) {
//                    holder.lblTitle.setText(pdfa.getTitle());
//                }
//            }
//
//            return row;
//        }
//
//        @Override
//        public boolean isChildSelectable(int groupPosition, int childPosition) {
//            return true;
//        }
//    }
//
//}
