package martinigt.einfachsparen.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by martin on 16.04.16.
 */
public class Transaction implements Serializable {

    public Transaction() {

    }

    public Transaction(boolean isStandard, String name, String tag, double value , Date date) {
        this.isStandard = isStandard;
        this.name = name;
        this.tag = tag;
        this.value = value;
        this.date = date;
    }

    private int id;

    private int periodId;

    private boolean isStandard;

    private String name;

    private double value;

    private String tag;

    private Date date;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPeriodId() {
        return periodId;
    }

    public void setPeriodId(int periodId) {
        this.periodId = periodId;
    }

    public boolean isStandard() {
        return isStandard;
    }

    public void setStandard(boolean standard) {
        this.isStandard = standard;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }


}
