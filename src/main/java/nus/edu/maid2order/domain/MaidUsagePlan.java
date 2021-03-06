package nus.edu.maid2order.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@AllArgsConstructor
@Entity
@NoArgsConstructor
public class MaidUsagePlan {

    @Id
    private Long maidUsagePlanId;

    private UsagePlan usagePlan;
    private Double cost;

    public MaidUsagePlan(UsagePlan usagePlan) {
        this.maidUsagePlanId = usagePlan.getId();
        this.usagePlan = usagePlan;
        this.cost = usagePlan.getCost();
    }

    public Double getCost() {
        return cost;
    }

    public Long getMaidUsagePlanId() {
        return maidUsagePlanId;
    }

    public UsagePlan getUsagePlan() {
        return usagePlan;
    }

}
