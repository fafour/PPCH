package th.go.nacc.nacc_law.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import th.go.nacc.nacc_law.NewsDetailActivity2;
import th.go.nacc.nacc_law.R;
import th.go.nacc.nacc_law.adapter.AdapterNews;
import th.go.nacc.nacc_law.application.MyApplication;
import th.go.nacc.nacc_law.model.News;
import th.go.nacc.nacc_law.utils.JsonParser;
import th.go.nacc.nacc_law.utils.ServiceConnection;


public class NewsFragment extends AbstractFragment {


    private ListView mListView;
    private AdapterNews mAdapter;
    private ServiceConnection serviceConnection;
    ArrayList<News> list = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_thought, container, false);
        setTitle("ข่าวสาร");
        serviceConnection = new ServiceConnection(getActivity());

        mListView = (ListView) view.findViewById(R.id.listView);
        mAdapter = new AdapterNews(getActivity());
        mListView.setAdapter(mAdapter);

        btnBack().setVisibility(View.GONE);
        getData();

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Intent intent = new Intent(getActivity(), LawContentDetailActivity.class);
//                intent.putExtra("title","ประชาสัมพันธ์");
//                intent.putExtra("content", list.get(i));
//                intent.putParcelableArrayListExtra("contents", (ArrayList<? extends Parcelable>) list);
//                startActivity(intent);

                Intent intent = new Intent(getActivity(), NewsDetailActivity2.class);

                intent.putExtra("news", list.get(i));
                //intent.putExtra( "news_id", mCurrentNews.getId() );

                startActivity(intent);
            }
        });
        return view;
    }

    void getData() {
        mAdapter.clear();
        serviceConnection.get(true, MyApplication.getInstance().URL_NEWS, new ServiceConnection.CallBackListener() {
            @Override
            public void callback(String result) {
                list = JsonParser.parseNews(result);
                mAdapter.addAll(list);
            }

            @Override
            public void fail(String result) {

            }
        });
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
