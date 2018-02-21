package de.martinlepsy.einfachsparen.helper;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.YAxis;

/**
 * Created by LM on 21.02.2018.
 */

public class BudgetBurndownChartFormatter {

    public static void configureBurndownChart(LineChart budgetBurndownChart) {
        // Rechte Achse verstecken
        YAxis rightAxis = budgetBurndownChart.getAxisRight();
        rightAxis.setEnabled(false);
        budgetBurndownChart.setDrawGridBackground(false);
        budgetBurndownChart.setDrawBorders(false);

        Description description = new Description();
        description.setText("Budgetentwicklung");
        budgetBurndownChart.setDescription(description);

        budgetBurndownChart.getAxisLeft().setDrawGridLines(false);
        budgetBurndownChart.getXAxis().setDrawGridLines(false);
    }
}
