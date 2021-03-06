package nus.edu.maid2order.domain;

import javax.persistence.Entity;

@Entity
public class OneTimeMaidUsagePlan extends MaidUsagePlan {

    public OneTimeMaidUsagePlan() {
        super(UsagePlan.ONE_TIME);
    }
}
