package de.martinlepsy.einfachsparen.helper;

import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.List;

import de.martinlepsy.einfachsparen.model.DashboardChartDataPoint;

/**
 * Created by LM on 20.02.2018.
 */

public class BudgetBurndownChartConverter {

    public static ChartData getChartDataFromDashboard(List<DashboardChartDataPoint> dataPoint) {
        ChartData result = null;
        List<Entry> plannedBudgetEntries = new ArrayList<>();
        List<Entry> currentBudgetEntries = new ArrayList<>();
        int counter = 0;
        for (DashboardChartDataPoint currentPoint :
                dataPoint) {
            Entry currentPlannedBudgetEntry = new Entry(counter, currentPoint.getPlannedBudget());
            plannedBudgetEntries.add(currentPlannedBudgetEntry);
            Entry currentBudgetEntry = new Entry(counter, currentPoint.getCurrentBudget());
            currentBudgetEntries.add(currentBudgetEntry);
            counter++;
        }
        LineDataSet plannedBudgetDataSet = new LineDataSet(plannedBudgetEntries, "Geplantes Budget");
        plannedBudgetDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        LineDataSet currentBudgetDataSet = new LineDataSet(currentBudgetEntries, "Tatsächliches Budget");
        currentBudgetDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        List<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(plannedBudgetDataSet);
        dataSets.add(currentBudgetDataSet);
        result = new LineData(dataSets);
        return result;
    }


}
