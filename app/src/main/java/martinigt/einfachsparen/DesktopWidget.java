package martinigt.einfachsparen;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import martinigt.einfachsparen.data.DatabaseHelper;
import martinigt.einfachsparen.data.PeriodDbHelper;
import martinigt.einfachsparen.data.TransactionDbHelper;
import martinigt.einfachsparen.model.Dashboard;
import martinigt.einfachsparen.model.Period;
import martinigt.einfachsparen.model.Transaction;
import martinigt.einfachsparen.model.TransactionType;

/**
 * Implementation of App Widget functionality.
 */
public class DesktopWidget extends AppWidgetProvider {

    private static final String ACTION_ADD_10 = "ADD10_CLICK";
    private static final String ACTION_ADD_20 = "ADD20_CLICK";
    private static final String ACTION_ADD_50 = "ADD50_CLICK";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int count = appWidgetIds.length;

        for (int i = 0; i < count; i++) {
            int widgetId = appWidgetIds[i];

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                    R.layout.desktop_widget);

            updateBudgetPerDay(context, remoteViews);

            setActionForAddExpenseButton(R.id.widget10Button, ACTION_ADD_10, context, remoteViews,
                    appWidgetIds);
            setActionForAddExpenseButton(R.id.widget20Button, ACTION_ADD_20, context, remoteViews,
                    appWidgetIds);
            setActionForAddExpenseButton(R.id.widget50Button, ACTION_ADD_50, context, remoteViews,
                    appWidgetIds);

            appWidgetManager.updateAppWidget(widgetId, remoteViews);
        }
    }

    private void updateBudgetPerDay(Context context, RemoteViews remoteViews) {
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        PeriodDbHelper periodHelper = new PeriodDbHelper(dbHelper);
        TransactionDbHelper transactionDbHelper = new TransactionDbHelper(dbHelper);

        Period currentPeriod = periodHelper.getCurrentPeriod();

        if (currentPeriod != null) {
            Locale currentLocale = Locale.getDefault();
            Currency myCurrency = Currency.getInstance((currentLocale));
            Dashboard dashboard = new Dashboard();
            dashboard.setPeriod(currentPeriod);
            dashboard.setPeriodExpenses(transactionDbHelper.getAllExpensesForPeriod(currentPeriod.getId()));
            dashboard.setPeriodIncome(transactionDbHelper.getAllIncomesForPeriod(currentPeriod.getId()));
            dashboard.recalculate();
            remoteViews.setTextViewText(R.id.budgetPerDayWidget,
                    String.format("%1.0f %s", dashboard.getBudgetPerDay(), myCurrency.getSymbol()));
        }
    }

    private void setActionForAddExpenseButton(int viewId, String action, Context context,
                                              RemoteViews remoteViews, int[] appWidgetIds){
        Intent addIntent = new Intent(context, DesktopWidget.class);
        addIntent.setAction(action);
        addIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
        PendingIntent pendingAddIntent = PendingIntent.getBroadcast(context,
                0, addIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(viewId, pendingAddIntent);
    }

    /**
     * A general technique for calling the onUpdate method,
     * requiring only the context parameter.
     *
     * @author John Bentley, based on Android-er code.
     * @see <a href="http://android-er.blogspot.com
     * .au/2010/10/update-widget-in-onreceive-method.html">
     * Android-er > 2010-10-19 > Update Widget in onReceive() method</a>
     */
    private void onUpdate(Context context) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance
                (context);

        // Uses getClass().getName() rather than MyWidget.class.getName() for
        // portability into any App Widget Provider Class
        ComponentName thisAppWidgetComponentName =
                new ComponentName(context.getPackageName(),getClass().getName()
                );
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(
                thisAppWidgetComponentName);
        onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (ACTION_ADD_10.equals(intent.getAction())) {
            addExpense(context, 10);
        }
        else if (ACTION_ADD_20.equals(intent.getAction())) {
            addExpense(context, 20);
        }
        else if (ACTION_ADD_50.equals(intent.getAction())) {
            addExpense(context, 50);
        }
        onUpdate(context);
    }

    private void addExpense(Context context, int value) {
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        TransactionDbHelper transactionDbHelper = new TransactionDbHelper(dbHelper);
        PeriodDbHelper periodDbHelper = new PeriodDbHelper(dbHelper);

        DateFormat df = DateFormat.getDateInstance();

        Date now = new Date();
        String expenseName = String.format("%s %s",context.getString(R.string.expense_from_home_screen_title),
                df.format(now));

        Transaction newExpense = new Transaction();
        newExpense.setDate(now);
        newExpense.setPeriodId(periodDbHelper.getCurrentPeriod().getId());
        newExpense.setValue(value);
        newExpense.setType(TransactionType.EXPENSE);
        newExpense.setName(expenseName);
        newExpense.setTag("");

        transactionDbHelper.addTransaction(newExpense);

        Locale currentLocale = Locale.getDefault();
        Currency myCurrency = Currency.getInstance((currentLocale));
        Toast.makeText(context.getApplicationContext(), String.format("%d %s %s %s", value, myCurrency.getSymbol(),
                expenseName, context.getString(R.string.widget_expense_added)),
                Toast.LENGTH_SHORT).show();

    }
}

