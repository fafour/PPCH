package th.go.nacc.nacc_law.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import th.go.nacc.nacc_law.R;
import th.go.nacc.nacc_law.model.Type;


public class AdapterType extends ArrayAdapter<Type> {


    public AdapterType(Context context) {
        super(context, R.layout.row_type, R.id.lblTitle);

    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View vi = super.getView(position, convertView, parent);
        final Type bank = getItem(position);
        TextView lblTitle = (TextView) vi.findViewById(R.id.lblTitle);

        lblTitle.setText(bank.Name);


        return vi;
    }


}
