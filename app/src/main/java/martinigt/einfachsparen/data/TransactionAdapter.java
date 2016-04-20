package martinigt.einfachsparen.data;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import martinigt.einfachsparen.R;
import martinigt.einfachsparen.model.Expense;
import martinigt.einfachsparen.model.Transaction;

/**
 * Created by martin on 20.04.16.
 */
public class TransactionAdapter extends ArrayAdapter<Transaction> {

    private final Context context;

    private final ArrayList<Transaction> values;

    public TransactionAdapter(Context context, ArrayList<Transaction> values) {
        super(context, -1, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.row_transaction, parent, false);
        TextView transactionValue = (TextView) rowView.findViewById(R.id.transaction_listItem_value);
        TextView transactionName = (TextView) rowView.findViewById(R.id.transaction_listItem_name);

        transactionValue.setText(""+values.get(position).getValue());
        transactionName.setText(values.get(position).getName());

        if (values.get(position) instanceof Expense) {
            transactionValue.setTextColor(Color.parseColor("#cc0000"));
        }
        else {
            transactionValue.setTextColor(Color.parseColor("#458B00"));
        }

        return rowView;
    }
}
