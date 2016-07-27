package de.martinlepsy.einfachsparen.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by martin on 16.04.16.
 */
public class Transaction implements Serializable {

    public Transaction() {

    }

    public Transaction(boolean isStandard, String name, String tag, double value , Date date,
                       TransactionType type, int copiedFromId) {
        this.isStandard = isStandard;
        this.name = name;
        this.tag = tag;
        this.value = value;
        this.date = date;
        this.type = type;
        this.fromStandardId = copiedFromId;
    }

    private int id;

    private long periodId;

    private boolean isStandard;

    private String name;

    private double value;

    private String tag;

    private Date date;

    private TransactionType type;

    private int fromStandardId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getPeriodId() {
        return periodId;
    }

    public void setPeriodId(long periodId) {
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


    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public int getFromStandardId() {
        return fromStandardId;
    }

    public void setFromStandardId(int fromStandardId) {
        this.fromStandardId = fromStandardId;
    }

    public Transaction cloneAsNonDefault() {
        return new Transaction(false, this.getName(), this.getTag(), this.getValue(),
                new Date(),this.getType(), this.getId());
    }
}
