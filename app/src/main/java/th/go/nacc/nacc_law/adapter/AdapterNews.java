package th.go.nacc.nacc_law.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import th.go.nacc.nacc_law.R;
import th.go.nacc.nacc_law.model.News;


public class AdapterNews extends ArrayAdapter<News> {


    public AdapterNews(Context context) {
        super(context, R.layout.row_news, R.id.lblTitle);

    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View vi = super.getView(position, convertView, parent);
        final News bank = getItem(position);
        ImageView imgBullet = (ImageView) vi.findViewById(R.id.imgBullet);
//        TextView tvDes = (TextView) vi.findViewById(R.id.tv_des);
        TextView lblTitle = (TextView) vi.findViewById(R.id.lblTitle);

//        tvDes.setText(bank.getLink());
        lblTitle.setText(bank.getTitle());
        if(!bank.getImage().isEmpty()){
            Picasso.with(getContext()).load(bank.getImage()).into(imgBullet);
        }

//        TextView lblNo = (TextView) vi.findViewById(R.id.lblNo);
//        lblNo.setText(String.valueOf(position + 1));
        return vi;
    }


}
