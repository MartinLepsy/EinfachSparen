package de.martinlepsy.einfachsparen.helper;

import android.graphics.Color;

import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.List;

import de.martinlepsy.einfachsparen.R;
import de.martinlepsy.einfachsparen.model.DashboardChartDataPoint;

/**
 * Created by LM on 20.02.2018.
 */

public class BudgetBurndownChartConverter {

    public static LineData getChartDataFromDashboard(List<DashboardChartDataPoint> dataPoint) {
        LineData result = null;
        List<Entry> plannedBudgetEntries = new ArrayList<>();
        List<Entry> currentBudgetEntries = new ArrayList<>();
        //int[] currentBudgetColors = new int[dataPoint.size()];
        int counter = 0;
        for (DashboardChartDataPoint currentPoint :
                dataPoint) {
            Entry currentPlannedBudgetEntry = new Entry(counter, currentPoint.getPlannedBudget());
            plannedBudgetEntries.add(currentPlannedBudgetEntry);
            Entry currentBudgetEntry = new Entry(counter, currentPoint.getCurrentBudget());
            currentBudgetEntries.add(currentBudgetEntry);
            //currentBudgetColors[counter] = currentPoint.getCurrentBudget() > currentPoint.getPlannedBudget() ?
            //        Color.GREEN : Color.RED;
            counter++;
        }
        LineDataSet plannedBudgetDataSet = new LineDataSet(plannedBudgetEntries, "Geplantes Budget");
        formatDataSet(plannedBudgetDataSet, Color.BLACK);
        LineDataSet currentBudgetDataSet = new LineDataSet(currentBudgetEntries, "Tatsächliches Budget");
        formatDataSet(currentBudgetDataSet, Color.GREEN);
        //currentBudgetDataSet.setColors(currentBudgetColors);
        List<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(plannedBudgetDataSet);
        dataSets.add(currentBudgetDataSet);
        result = new LineData(dataSets);
        return result;
    }

    private static void formatDataSet(LineDataSet dataSet, int color) {
        dataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        dataSet.setDrawValues(false);
        dataSet.setDrawCircles(false);
        dataSet.setColor(Color.BLACK);
        dataSet.setMode(LineDataSet.Mode.LINEAR);
        dataSet.setColor(color);
    }


}
