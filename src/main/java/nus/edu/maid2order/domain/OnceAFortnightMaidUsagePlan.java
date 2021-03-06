package nus.edu.maid2order.domain;

import javax.persistence.Entity;

@Entity
public class OnceAFortnightMaidUsagePlan extends MaidUsagePlan {

    public OnceAFortnightMaidUsagePlan() {
        super(UsagePlan.ONCE_A_FORTNIGHT);
    }

}
