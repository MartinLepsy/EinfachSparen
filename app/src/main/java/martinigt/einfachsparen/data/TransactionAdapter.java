package martinigt.einfachsparen.data;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Locale;

import martinigt.einfachsparen.R;
import martinigt.einfachsparen.model.Transaction;
import martinigt.einfachsparen.model.TransactionType;

/**
 * Created by martin on 20.04.16.
 */
public class TransactionAdapter extends ArrayAdapter<Transaction> {

    private final Context context;

    private final ArrayList<Transaction> values;

    private Locale currentLocale;

    private Currency currentCurrency;

    private DateFormat df;

    public TransactionAdapter(Context context, ArrayList<Transaction> values) {
        super(context, -1, values);
        this.context = context;
        this.values = values;
        df = DateFormat.getDateInstance();

        currentLocale = Locale.getDefault();
        currentCurrency = Currency.getInstance((currentLocale));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.row_transaction, parent, false);
        TextView transactionValue = (TextView) rowView.findViewById(R.id.transaction_listItem_value);
        TextView transactionName = (TextView) rowView.findViewById(R.id.transaction_listItem_name);
        TextView transactionAdditionalInfo = (TextView) rowView.findViewById(R.id.transaction_listItem_additionalInfo);

        if (values.get(position).getTag() != null && values.get(position).getTag().length() > 0) {
            transactionAdditionalInfo.setText(String.format("%s - %s", values.get(position).getTag(),
                    df.format(values.get(position).getDate())));
        }
        else {
            transactionAdditionalInfo.setText(String.format("%s", df.format(values.get(position).getDate())));
        }

        transactionValue.setText(String.format("%1.2f %s", values.get(position).getValue(), currentCurrency.getSymbol()));
        transactionName.setText(values.get(position).getName());

        if (values.get(position).getType() == TransactionType.EXPENSE) {
            transactionValue.setTextColor(Color.parseColor("#cc0000"));
        }
        else {
            transactionValue.setTextColor(Color.parseColor("#458B00"));
        }

        return rowView;
    }

    public ArrayList<Transaction> getValues() {
        return values;
    }
}
