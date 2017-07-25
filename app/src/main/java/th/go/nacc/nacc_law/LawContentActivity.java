package th.go.nacc.nacc_law;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import th.go.nacc.nacc_law.application.MyApplication;
import th.go.nacc.nacc_law.model.SectionContent;
import th.go.nacc.nacc_law.model.SectionPDF;

/**
 * Created by nontachai on 7/10/15 AD.
 */
public class LawContentActivity extends AbstractActivity {

    private int lawSection;

    private ExpandableListView mListView;
    private LawContentAdapter mAdapter;

    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );


        setContentView( R.layout.activity_law_content );

        Bundle data = getIntent().getExtras();

        if ( data != null ) {
            lawSection = data.getInt( "law_section" );
        }


        final ActionBar actionBar = getActionBar();


        actionBar.setDisplayHomeAsUpEnabled(true);

        BitmapDrawable background = new BitmapDrawable( getResources(), BitmapFactory.decodeResource(getResources(), R.drawable.top_bar));
        background.setTileModeX(android.graphics.Shader.TileMode.REPEAT);
        actionBar.setBackgroundDrawable(background);



        if ( lawSection == 100 ) {
            setTitle( "มาตรา 100" );
        } else if ( lawSection == 103 ) {
            setTitle( "มาตรา 103" );
        }

        _outletObject();
    }

    private void _outletObject () {
        mListView = (ExpandableListView) findViewById(R.id.listView);

        mAdapter = new LawContentAdapter( this );

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
                    Intent intent = new Intent(LawContentActivity.this, LawContentDetailActivity.class);

                    intent.putExtra("law_section", lawSection);

                    if ( lawSection == 100 ) {
                        intent.putExtra("content", MyApplication.getInstance().contents100.get(childPosition));
                        intent.putParcelableArrayListExtra("contents", (ArrayList<? extends Parcelable>) MyApplication.getInstance().contents100);
                    } else if ( lawSection == 103 ) {
                        intent.putExtra( "content", MyApplication.getInstance().contents103.get( childPosition ));
                        intent.putParcelableArrayListExtra("contents", (ArrayList<? extends Parcelable>) MyApplication.getInstance().contents103);
                    }

                    startActivity(intent);
                } else if (groupPosition == 1) {
                    Intent intent = new Intent(LawContentActivity.this, LawPDFDetailActivity.class);

                    intent.putExtra( "law_section", lawSection );

                    if ( lawSection == 100 ) {
                        intent.putExtra( "pdf", MyApplication.getInstance().pdf100.get( childPosition ));
                    } else if ( lawSection == 103 ) {
                        intent.putExtra( "pdf", MyApplication.getInstance().pdf103.get( childPosition ));
                    }

                    startActivity(intent);
                }

                return false;
            }
        });
    }

    public void doExamAction ( View v ) {

        // Is exists member logged-in? If not, show registration activity otherwise show level list
        if ( MyApplication.getInstance().getMember() == null ) {
            Intent intent = new Intent(this, RegistrationActivity.class);

            intent.putExtra("law_section", lawSection);

            startActivity(intent);
        } else {
            Intent intent = new Intent( this, LawLevelListActivity.class );

            intent.putExtra("law_section", lawSection);

            startActivity( intent );
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


    private class LawContentAdapter extends BaseExpandableListAdapter {

        private Context mContext;

        private LayoutInflater mInflater;

        private class ViewHolder {
            TextView lblNo, lblTitle;
        }

        public LawContentAdapter ( Context context ) {
            this.mContext = context;
            this.mInflater = (LayoutInflater) mContext.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        }

        @Override
        public int getGroupCount() {
            if ( lawSection == 100 ) {
                if (MyApplication.getInstance().pdf100 != null
                        && MyApplication.getInstance().pdf100.size() > 0 ) {
                    return 2;
                } else {
                    return 1;
                }
            } else if ( lawSection == 103 ) {
                if (MyApplication.getInstance().pdf103 != null
                        && MyApplication.getInstance().pdf103.size() > 0 ) {
                    return 2;
                } else {
                    return 1;
                }
            }
            return 0;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            if ( lawSection == 100 ) {
                if ( groupPosition == 0 ) return MyApplication.getInstance().contents100.size();
                else if ( groupPosition == 1 ) return MyApplication.getInstance().pdf100.size();
                return 0;
            } else if ( lawSection == 103 ) {
                if ( groupPosition == 0 ) return MyApplication.getInstance().contents103.size();
                else if ( groupPosition == 1 ) return MyApplication.getInstance().pdf103.size();
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

            if ( row == null ) {
                row = mInflater.inflate( R.layout.group_law_content, viewGroup, false );
            }

            TextView lblTitle = (TextView) row.findViewById( R.id.lblTitle );
            ImageView imgIcon = (ImageView) row.findViewById( R.id.imgIcon );

            if ( groupPosition == 0 ) {
                lblTitle.setText( "เนื้อหาสาระ" );
                imgIcon.setImageResource( R.drawable.document_edit );
            } else if ( groupPosition == 1 ) {
                lblTitle.setText( "ข้อมูลเพิ่มเติม" );
                imgIcon.setImageResource(R.drawable.pdf);
            }

            return row;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean b, View view, ViewGroup viewGroup) {
            View row = view;
            ViewHolder holder;

            if ( row == null ) {
                row = mInflater.inflate( R.layout.row_law_content, viewGroup, false );

                holder = new ViewHolder();
                holder.lblNo = (TextView) row.findViewById( R.id.lblNo );
                holder.lblTitle = (TextView) row.findViewById( R.id.lblTitle );
            } else {
                holder = (ViewHolder)row.getTag();
            }

            row.setTag( holder );

            holder.lblNo.setText(String.valueOf(childPosition + 1));

            if ( groupPosition == 0 ) {
                SectionContent content = null;

                if ( lawSection == 100 ) {
                    content = MyApplication.getInstance().contents100.get( childPosition );
                } else if ( lawSection == 103 ) {
                    content = MyApplication.getInstance().contents103.get( childPosition );
                }

                if ( content != null ) {
                    holder.lblTitle.setText( content.getTitle() );
                }
            } else if ( groupPosition == 1 ) {
                SectionPDF pdf = null;

                if ( lawSection == 100 ) {
                    pdf = MyApplication.getInstance().pdf100.get( childPosition );
                } else if ( lawSection == 103 ) {
                    pdf = MyApplication.getInstance().pdf103.get( childPosition );
                }

                if ( pdf != null ) {
                    holder.lblTitle.setText( pdf.getTitle() );
                }
            }

            return row;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }
}
