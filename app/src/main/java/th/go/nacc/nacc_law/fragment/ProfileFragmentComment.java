package th.go.nacc.nacc_law.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import th.go.nacc.nacc_law.R;
import th.go.nacc.nacc_law.application.MyApplication;
import th.go.nacc.nacc_law.utils.ServiceConnection;


public class ProfileFragmentComment extends AbstractFragment {

    int point = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_comment, container, false);
        setTitle("บัญชีผู้ใช้");

        final LinearLayout layoutRate = (LinearLayout) view.findViewById(R.id.layout_rate);
        for (int i = 0; i < layoutRate.getChildCount(); i++) {


            final int finalI = i;
            layoutRate.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    for (int j = 0; j < layoutRate.getChildCount(); j++) {
                        layoutRate.getChildAt(j).setSelected(false);
                    }
                    view.setSelected(true);
                    point = finalI + 1;
                }
            });

        }

        final ServiceConnection serviceConnection = new ServiceConnection(getActivity());

        final EditText txtLastname = (EditText) view.findViewById(R.id.txtLastname);
        Button btnOK = (Button) view.findViewById(R.id.btn_ok);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (txtLastname.getText().toString().isEmpty()) {

                    return;
                }
                if (point == 0) {
                    return;
                }
                Map<String, Object> maps = new HashMap<>();
                maps.put("comment", txtLastname.getText().toString());
                maps.put("point", point);
                maps.put("member_id", MyApplication.getInstance().getMember().getId());
                serviceConnection.post(true, MyApplication.getInstance().URL_COMMENT, maps, new ServiceConnection.CallBackListener() {
                    @Override
                    public void callback(String result) {
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            if (jsonObject.getInt("status") == 1) {
                                getActivity().onBackPressed();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void fail(String result) {

                    }
                });
            }
        });
        btnBack().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTitle("บัญชีผู้ใช้");
                btnBack().setVisibility(View.GONE);
                btnEdit().setVisibility(View.VISIBLE);
                getActivity().onBackPressed();
            }
        });
        return view;
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
                    setTitle("บัญชีผู้ใช้");
                    btnBack().setVisibility(View.GONE);
                    btnEdit().setVisibility(View.VISIBLE);
                    getActivity().onBackPressed();
                    return true;
                }
                return false;
            }
        });
    }


}
