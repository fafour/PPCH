package th.go.nacc.nacc_law.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import th.go.nacc.nacc_law.R;


public class AboutFragment extends AbstractFragment {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);


        setTitle("เกี่ยวกับสำนักงาน ป.ป.ช.");


//        ImageView imageView = (ImageView)view.findViewById(R.id.imageView);
//        Picasso.with(getActivity()).load("http://click.senate.go.th/wp-content/uploads/2014/03/8978.jpg").into(imageView);



        return view;
    }



}
