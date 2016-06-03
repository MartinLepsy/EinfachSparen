package de.martinlepsy.einfachsparen.data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.ArrayList;

import de.martinlepsy.einfachsparen.R;
import de.martinlepsy.einfachsparen.model.Period;

/**
 * Created by martin on 13.05.16.
 */
public class PeriodAdapter extends ArrayAdapter<Period> {

    private final Context context;

    private final ArrayList<Period> values;

    private DateFormat df;

    private DatabaseHelper dbHelper;

    private Period currentPeriod;

    public PeriodAdapter(Context context, ArrayList<Period> values) {
        super(context, -1, values);
        dbHelper = new DatabaseHelper(context);
        PeriodDbHelper periodDbHelper = new PeriodDbHelper(dbHelper);
        currentPeriod = periodDbHelper.getCurrentPeriod();
        this.context = context;
        this.values = values;
        df = DateFormat.getDateInstance();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.row_period, parent, false);
        TextView periodName = (TextView) rowView.findViewById(R.id.period_listItem_name);
        TextView periodDates = (TextView) rowView.findViewById(R.id.period_listItem_dates);

        boolean isCurrentPeriod = false;

        if (currentPeriod != null) {
            isCurrentPeriod = values.get(position).getId() == currentPeriod.getId();
        }

        String name = values.get(position).getName();

        if (isCurrentPeriod) {
            name = name + " " + context.getString(R.string.current);
        }

        periodName.setText(name);
        periodDates.setText(df.format(values.get(position).getStart().getTime()) +
                " - " + df.format(values.get(position).getEnd().getTime()));

        return rowView;
    }

    public ArrayList<Period> getValues() {
        return values;
    }
}
