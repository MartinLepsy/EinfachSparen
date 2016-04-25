package martinigt.einfachsparen.model;

import java.util.Date;

/**
 * Created by martin on 16.04.16.
 */
public class Expense extends Transaction {

    public Expense(long periodeId) {
        this.setPeriodId(periodeId);
    }

    public  Expense() {

    }

    /**
     *
     * @param id
     * @param periodeId
     * @param hoehe
     * @param bezeichnung
     * @param tag
     * @param datum
     * @param istStandard
     */
    public Expense(int id, int periodeId, double hoehe, String bezeichnung, String tag, Date datum,
                   boolean istStandard) {
        this.setId(id);
        this.setPeriodId(periodeId);
        this.setValue(hoehe);
        this.setName(bezeichnung);
        this.setTag(tag);
        this.setDate(datum);
        this.setStandard(istStandard);
    }

    public Expense(boolean isStandard, String name, String tag, double value, Date date) {
        super(isStandard,name, tag, value, date);
    }

    public Expense cloneAsNonDefault() {
        return new Expense(false, this.getName(), this.getTag(), this.getValue(),
                new Date());
    }
}
