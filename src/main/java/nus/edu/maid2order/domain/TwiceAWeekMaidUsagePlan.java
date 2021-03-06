package nus.edu.maid2order.domain;

import javax.persistence.Entity;

@Entity
public class TwiceAWeekMaidUsagePlan extends MaidUsagePlan {

    public TwiceAWeekMaidUsagePlan() {
        super(UsagePlan.TWICE_A_WEEK);
    }

}
