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
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import th.go.nacc.nacc_law.LawContentDetailActivity;
import th.go.nacc.nacc_law.R;
import th.go.nacc.nacc_law.adapter.AdapterEducation;
import th.go.nacc.nacc_law.application.MyApplication;
import th.go.nacc.nacc_law.model.SectionContent;
import th.go.nacc.nacc_law.utils.JsonParser;
import th.go.nacc.nacc_law.utils.ServiceConnection;


public class EducationFragment extends AbstractFragment {


    private ListView mListView;
    private AdapterEducation mAdapter;
    private int lawSection = 1;
    private ServiceConnection serviceConnection;
    ArrayList<SectionContent> list = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_education, container, false);
        setTitle("สื่อการเรียนรู้");
        final TextView btn100 = (TextView) view.findViewById(R.id.btn_100);
        final TextView btn103 = (TextView) view.findViewById(R.id.btn_103);
        serviceConnection = new ServiceConnection(getActivity());

        mListView = (ListView) view.findViewById(R.id.listView);
        mAdapter = new AdapterEducation(getActivity());
        mListView.setAdapter(mAdapter);
        btn100.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn100.setSelected(true);
                btn103.setSelected(false);
                lawSection = 1;
                getData(lawSection);

            }
        });
        btn103.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn100.setSelected(false);
                btn103.setSelected(true);
                lawSection = 2;
                getData(lawSection);
            }
        });
        btn100.setSelected(true);

        getData(lawSection);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), LawContentDetailActivity.class);
                intent.putExtra("law_section", lawSection);
                intent.putExtra("content", list.get(i));
                intent.putParcelableArrayListExtra("contents", (ArrayList<? extends Parcelable>) list);


                startActivity(intent);
            }
        });
        return view;
    }

    void getData(int section) {
        mAdapter.clear();
        Map<String, Object> maps = new HashMap<>();
        maps.put("section_id", section);
        serviceConnection.post(true, MyApplication.getInstance().URL_EDUCATION, maps, new ServiceConnection.CallBackListener() {
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
