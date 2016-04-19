package martinigt.einfachsparen.model;

import java.util.Date;

import martinigt.einfachsparen.helper.Helper;

/**
 * Created by martin on 16.04.16.
 */
public class Period {

    private int id;

    private Date start;

    private Date end;

    private  String name;

    private double plannedSaving;

    public Period()
    {
        id = -1;
        start = new Date();
        //name = Helper.getMonthForDate(start);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPlannedSaving() {
        return plannedSaving;
    }

    public void setPlannedSaving(double plannedSaving) {
        this.plannedSaving = plannedSaving;
    }
}
