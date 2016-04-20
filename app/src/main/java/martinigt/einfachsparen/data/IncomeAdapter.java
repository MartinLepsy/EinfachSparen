package martinigt.einfachsparen.data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import martinigt.einfachsparen.R;
import martinigt.einfachsparen.model.Income;

/**
 * Created by martin on 20.04.16.
 */
public class IncomeAdapter extends ArrayAdapter<Income> {

    private final Context context;

    private final ArrayList<Income> values;

    public IncomeAdapter(Context context, ArrayList<Income> values) {
        super(context, -1, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.row_income, parent, false);
        TextView incomeValue = (TextView) rowView.findViewById(R.id.income_listItem_value);
        TextView incomeName = (TextView) rowView.findViewById(R.id.income_listItem_name);

        incomeValue.setText(""+values.get(position).getValue());
        incomeName.setText(values.get(position).getName());

        return rowView;


    }

}
