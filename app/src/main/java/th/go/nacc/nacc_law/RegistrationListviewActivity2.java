package th.go.nacc.nacc_law;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import th.go.nacc.nacc_law.application.MyApplication;
import th.go.nacc.nacc_law.model.Member;
import th.go.nacc.nacc_law.model.Type2;
import th.go.nacc.nacc_law.utils.ServiceConnection;

/**
 * Created by nontachai on 7/17/15 AD.
 */
public class RegistrationListviewActivity2 extends AbstractActivity {

    private EditText mTxtName, mTxtLastname, mTxtIDCard;


    Member mMember;
    ServiceConnection serviceConnection;
    ArrayList<Type2> mHomeList = new ArrayList<>();
    private LinearLayout rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_registration_listview);

        serviceConnection = new ServiceConnection(this);
        Bundle data = getIntent().getExtras();

        if (data != null) {

            mMember = data.getParcelable("member");


        }




        setTitle("ลงทะเบียน");

        initMockupData();

         rootView = (LinearLayout)findViewById(R.id.rootview);
       setLayout(mHomeList);
    }

    void setLayout(ArrayList<Type2> list){
        for(Type2 type2:list){
            LinearLayout root = (LinearLayout) View.inflate(this,R.layout.row_type2,null);
            TextView tvHead = (TextView)root.findViewById(R.id.tv_header);
            TextView tvTitle = (TextView)root.findViewById(R.id.tv_title);
            tvTitle.setText(type2.Name);
            rootView.addView(root);
            if(!type2.SubType.isEmpty()){
                setLayout(type2.SubType);
            }
        }
    }

    private void initMockupData() {

        serviceConnection.get(true, MyApplication.getInstance().URL_ALL, new ServiceConnection.CallBackListener() {
            @Override
            public void callback(String result) {

            }

            @Override
            public void fail(String result) {

            }
        });
//        ArrayList<Type2> homeList = new ArrayList<>();
//
//        for (int i = 0; i < 10; i++) {
//            Type2 type2 = new Type2();
//            type2.Name= "ชั้น"+i;
//            for (int j = 0; j < 3; j++) {
//                Type2 type11 = new Type2();
//                type11.Name = "ชั้น"+i+" "+j;
//                type2.SubType.add(type11);
//            }
//            homeList.add(type2);
//        }
//        mHomeList = homeList;
    }
}
