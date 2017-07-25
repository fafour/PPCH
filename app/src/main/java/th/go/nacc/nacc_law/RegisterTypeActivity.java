package th.go.nacc.nacc_law;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import th.go.nacc.nacc_law.adapter.AdapterType;
import th.go.nacc.nacc_law.application.MyApplication;
import th.go.nacc.nacc_law.model.Amphur;
import th.go.nacc.nacc_law.model.JobTitle;
import th.go.nacc.nacc_law.model.Province;
import th.go.nacc.nacc_law.model.Subordinate;
import th.go.nacc.nacc_law.model.Type;
import th.go.nacc.nacc_law.model.TypeSub;
import th.go.nacc.nacc_law.utils.JsonParser;
import th.go.nacc.nacc_law.utils.ServiceConnection;

/**
 * Created by nontachai on 7/10/15 AD.
 */
public class RegisterTypeActivity extends AbstractActivity {

    private int lawSection = 0;
    String positionType = "1";
    String id = "";
    int idProvince = 0;
    ArrayList<TypeSub> listSub = new ArrayList<>();

    private ListView mListView;
    private AdapterType mAdapter;

    String[] title = {"ประชาชน", "เจ้าหน้าที่ของรัฐ", "คู่สมรสของเจ้าหน้าที่ของรัฐ"};
    private ServiceConnection serviceConnection;
    int amphurID = 0;
    int positionID = 0;
    int subonate2 = 0;
    String ministry="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_register_type);

        Bundle data = getIntent().getExtras();

        if (data != null) {
            lawSection = data.getInt("type");
            positionType = data.getString("positionType");
            id = data.getString("id");
            listSub = (ArrayList<TypeSub>) data.getSerializable("sub");
            idProvince = data.getInt("provinceID");
            positionID = data.getInt("positionID");
            amphurID = data.getInt("amphurID");
            subonate2 = data.getInt("subonate2");
            ministry = data.getString("ministry");
        }

        mListView = (ListView) findViewById(R.id.listView);

        mAdapter = new AdapterType(this);

        mListView.setAdapter(mAdapter);

        serviceConnection = new ServiceConnection(this);


        if (lawSection == 0) {
            setTitle("ประเภทผู้ใช้งาน");
            for (int i = 0; i < title.length; i++) {
                Type type = new Type();
                type.id = i + "";
                type.Name = title[i];
                mAdapter.add(type);
            }
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent in = getIntent();
                    in.putExtra("model", mAdapter.getItem(i));
                    setResult(RESULT_OK, in);
                    finish();
                }
            });
        } else if (lawSection == 1) {
            setTitle("จังหวัด");
            _getProvince();
        } else if (lawSection == 2) {
            setTitle("สังกัด");
            getOrganize();
        } else if (lawSection == 3) {
            setTitle("สังกัด(ย่อย)");
            for (int i = 0; i < listSub.size(); i++) {
                Type type = new Type();

                type.Name = listSub.get(i).Name;
                type.id =listSub.get(i).id;
                if (listSub.get(i).Name.equals("องค์การบริหารส่วนจังหวัด")) {
                    type.id = "1";
                } else if (listSub.get(i).Name.equals("เทศบาลนคร")) {
                    type.id = "2";
                } else if (listSub.get(i).Name.equals("เทศบาลเมือง")) {
                    type.id = "3";
                } else if (listSub.get(i).Name.equals("เทศบาลตำบล")) {
                    type.id = "4";
                } else if (listSub.get(i).Name.equals("องค์การบริหารส่วนตำบล")) {
                    type.id = "5";
                } else if (listSub.get(i).Name.equals("กรุงเทพมหานคร")) {
                    type.id = "6";
                } else if (listSub.get(i).Name.equals("เมืองพัทยา")) {
                    type.id = "7";
                }

                mAdapter.add(type);
            }

        } else if (lawSection == 4) {
            setTitle("ตำแหน่ง");
            _getJobTitles();
        } else if (lawSection == 5) {
            setTitle("อำเภอ");
            getDistrict(idProvince);
        } else if (lawSection == 6) {
            setTitle("รายชื่อ อปท.");
            getListName(subonate2, amphurID);
        }else if (lawSection == 7) {
            setTitle("รัฐวิสาหกิจ");
            for (int i = 0; i < listSub.size(); i++) {
                Type type = new Type();

                type.Name = listSub.get(i).Name;
                type.id =listSub.get(i).id;

                mAdapter.add(type);
            }
        }else if (lawSection == 8) {
            setTitle("กรม");
            getMinistry(ministry);
        }

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent in = getIntent();
                in.putExtra("model", mAdapter.getItem(i));
                setResult(RESULT_OK, in);
                finish();
            }
        });
    }

    private void _getProvince() {
        Province.get(this, new Province.OnGetProvinceListener() {
            @Override
            public void onResult(List<Province> provinces, Error error) {
                if (error == null) {


                    for (int i = 0, maxProvince = provinces.size(); i < maxProvince; i++) {
                        Province province = provinces.get(i);

                        Type type = new Type();
                        type.Name = province.getName();
                        type.id = province.getId() + "";

                        mAdapter.add(type);
                    }

                    mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            Intent in = getIntent();
                            in.putExtra("model", mAdapter.getItem(i));
                            setResult(RESULT_OK, in);
                            finish();
                        }
                    });
                } else {
                    retryService(error.getMessage());
                }
            }
        });
    }

    void getOrganize() {
        serviceConnection.get(true, MyApplication.getInstance().URL_ORGANIZATION, new ServiceConnection.CallBackListener() {
            @Override
            public void callback(String result) {
                ArrayList<Type> list = JsonParser.parseType(result);
                mAdapter.addAll(list);
                mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent in = getIntent();
                        in.putExtra("model", mAdapter.getItem(i));
                        setResult(RESULT_OK, in);
                        finish();
                    }
                });
            }

            @Override
            public void fail(String result) {

            }
        });
    }

    void getMinistry(String id) {
        Map<String, Object> maps = new HashMap<>();
        maps.put("subordinate_id", id);
        serviceConnection.post(true, MyApplication.getInstance().URL_MINISTRY, maps, new ServiceConnection.CallBackListener() {
            @Override
            public void callback(String result) {
                ArrayList<Type> list = JsonParser.parseMinistry(result);
                mAdapter.addAll(list);
                mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent in = getIntent();
                        in.putExtra("model", mAdapter.getItem(i));
                        setResult(RESULT_OK, in);
                        finish();
                    }
                });
            }

            @Override
            public void fail(String result) {

            }
        });
    }

    private void _getJobTitles() {
        JobTitle.get(this, new JobTitle.OnResponseListener() {
            @Override
            public void onGetListener(List<JobTitle> jobTitles, Error error) {
                if (error == null) {
                    for (int i = 0, maxProvince = jobTitles.size(); i < maxProvince; i++) {
                        JobTitle province = jobTitles.get(i);
                        if (positionType.equals(province.getSub_id() + "")) {
                            Type type = new Type();
                            type.Name = province.getName();
                            type.id = i + "";

                            mAdapter.add(type);
                        }

                    }

                    mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            Intent in = getIntent();
                            in.putExtra("model", mAdapter.getItem(i));
                            setResult(RESULT_OK, in);
                            finish();
                        }
                    });
                } else {
                    retryService(error.getMessage());
                }
            }
        });
    }

    private void getDistrict(final int provinceID) {
        Province.get(this, new Province.OnGetProvinceListener() {
            @Override
            public void onResult(List<Province> provinces, Error error) {
                if (error == null) {


                    for (int i = 0, maxProvince = provinces.size(); i < maxProvince; i++) {
                        for (int j = 0; j < provinces.get(i).getAmphurs().size(); j++) {
                            if (provinces.get(i).getId() == provinceID) {
                                Amphur province = provinces.get(i).getAmphurs().get(j);

                                Type type = new Type();
                                type.Name = province.getName();
                                type.id = province.getId() + "";

                                mAdapter.add(type);
                            }

                        }

                    }

                    mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            Intent in = getIntent();
                            in.putExtra("model", mAdapter.getItem(i));
                            setResult(RESULT_OK, in);
                            finish();
                        }
                    });
                } else {
                    retryService(error.getMessage());
                }
            }
        });
    }

    void getListName(int position, int amphur) {
        Subordinate.get(this, position, amphur, new Subordinate.OnGetItemListener() {
            @Override
            public void onResult(List<Subordinate> items, Error error) {


                if (error == null) {

                    if (items != null && items.size() > 0) {

                        for (int i = 0, maxItem = items.size(); i < maxItem; i++) {
                            Subordinate item = items.get(i);

                            Type type = new Type();
                            type.Name = item.getName();
                            type.id = item.getId() + "";

                            mAdapter.add(type);
                        }

                    } else {
                        List<String> list = new ArrayList<String>();

                        list.add("ไม่พบข้อมูลรายชื่อ");

                        Type type = new Type();
                        type.Name = "ไม่พบข้อมูลรายชื่อ";
                        type.id = "0";

                        mAdapter.add(type);
                    }
                } else {
                    retryService(error.getMessage());
                }
            }
        });
    }
}
