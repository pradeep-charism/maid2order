package nus.edu.maid2order.domain;

import javax.persistence.Entity;

@Entity
public class OnceAWeekMaidUsagePlan extends MaidUsagePlan {

    public OnceAWeekMaidUsagePlan() {
        super(UsagePlan.ONCE_A_WEEK);
    }
}
