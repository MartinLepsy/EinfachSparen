package martinigt.einfachsparen.model;

import java.util.Date;

/**
 * Created by martin on 16.04.16.
 */
public class Income extends Transaction {

    public Income(int periodenId) {
        this.setPeriodId(periodenId);
    }

    public Income() {

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
    public Income(int id, int periodeId, double hoehe, String bezeichnung, String tag, Date datum,
                  boolean istStandard) {
        this.setId(id);
        this.setPeriodId(periodeId);
        this.setValue(hoehe);
        this.setName(bezeichnung);
        this.setTag(tag);
        this.setDate(datum);
        this.setStandard(istStandard);
    }

    public Income(boolean isStandard, String name, String tag, double value, Date date) {
        super(isStandard,name, tag, value, date);
    }

    public Income cloneAsNonDefault() {
        return new Income(false, this.getName(), this.getTag(), this.getValue(),
                new Date());
    }
}
