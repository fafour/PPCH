package th.go.nacc.nacc_law.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import th.go.nacc.nacc_law.LawContentDetailActivity;
import th.go.nacc.nacc_law.R;
import th.go.nacc.nacc_law.adapter.AdapterEducation;
import th.go.nacc.nacc_law.application.MyApplication;
import th.go.nacc.nacc_law.model.SectionContent;
import th.go.nacc.nacc_law.utils.JsonParser;
import th.go.nacc.nacc_law.utils.ServiceConnection;


public class ThoughtFragment extends AbstractFragment {


    private ListView mListView;
    private AdapterEducation mAdapter;
    private ServiceConnection serviceConnection;
    ArrayList<SectionContent> list = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_thought, container, false);
        setTitle("สื่อการเรียนรู้ปรับฐานความคิด");
        serviceConnection = new ServiceConnection(getActivity());

        mListView = (ListView) view.findViewById(R.id.listView);
        mAdapter = new AdapterEducation(getActivity());
        mListView.setAdapter(mAdapter);



        getData();

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), LawContentDetailActivity.class);
                intent.putExtra("title","สื่อการเรียนรู้ปรับฐานความคิด");
                intent.putExtra("content", list.get(i));
                intent.putParcelableArrayListExtra("contents", (ArrayList<? extends Parcelable>) list);
                startActivity(intent);
            }
        });
        return view;
    }

    void getData() {
        mAdapter.clear();
        serviceConnection.get(true, MyApplication.getInstance().URL_THOUGHT, new ServiceConnection.CallBackListener() {
            @Override
            public void callback(String result) {
                list = JsonParser.parseEducation(result);
                mAdapter.addAll(list);
            }

            @Override
            public void fail(String result) {

            }
        });
    }



}
