package de.martinlepsy.einfachsparen.model;

import java.util.Date;

/**
 * Created by martin on 27.10.17.
 */
public class DashboardChartDataPoint {

    private Date date;

    private float currentBudget;

    private float plannedBudget;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public float getCurrentBudget() {
        return currentBudget;
    }

    public void setCurrentBudget(float currentBudget) {
        this.currentBudget = currentBudget;
    }

    public float getPlannedBudget() {
        return plannedBudget;
    }

    public void setPlannedBudget(float plannedBudget) {
        this.plannedBudget = plannedBudget;
    }
}
