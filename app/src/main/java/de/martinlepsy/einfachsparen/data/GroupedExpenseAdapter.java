package de.martinlepsy.einfachsparen.data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Locale;

import de.martinlepsy.einfachsparen.R;
import de.martinlepsy.einfachsparen.model.GroupedTransactionByCategory;

/**
 * Created by martin on 11.10.17.
 */
public class GroupedExpenseAdapter extends ArrayAdapter<GroupedTransactionByCategory> {

    private Context context;

    private ArrayList<GroupedTransactionByCategory> values;

    private Locale currentLocale;

    private Currency currentCurrency;

    public GroupedExpenseAdapter(Context context, ArrayList<GroupedTransactionByCategory> values) {
        super(context, -1, values);
        this.context = context;
        this.values = values;

        currentLocale = Locale.getDefault();
        currentCurrency = Currency.getInstance((currentLocale));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.row_grouped_transaction, parent, false);
        TextView groupValue = (TextView) rowView.findViewById(R.id.grouped_transaction_listItem_value);
        TextView groupName = (TextView) rowView.findViewById(R.id.transaction_group_listItem_name);

        groupValue.setText(String.format("%1.2f %s", values.get(position).getValue(), currentCurrency.getSymbol()));
        groupName.setText(values.get(position).getCategoryName());

        return rowView;
    }
}
