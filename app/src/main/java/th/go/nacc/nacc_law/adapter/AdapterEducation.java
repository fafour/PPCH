package th.go.nacc.nacc_law.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import th.go.nacc.nacc_law.R;
import th.go.nacc.nacc_law.model.SectionContent;


public class AdapterEducation extends ArrayAdapter<SectionContent> {


    public AdapterEducation(Context context) {
        super(context, R.layout.row_law_content, R.id.lblNo);

    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View vi = super.getView(position, convertView, parent);
        final SectionContent bank = getItem(position);
        TextView lblNo = (TextView) vi.findViewById(R.id.lblNo);
        TextView lblTitle = (TextView) vi.findViewById(R.id.lblTitle);

        lblNo.setText(String.valueOf(position + 1));
        lblTitle.setText(bank.getTitle());

        return vi;
    }


}
