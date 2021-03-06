package nus.edu.maid2order.domain;

import lombok.Data;

@Data
public class MaidUsageBill {

    private Long maidId;
    private Long customerId;
    private UsagePlan usagePlan;
    private Double cost;

    public MaidUsageBill(Long maidId, Long customerId, UsagePlan usagePlan, Double cost) {
        this.maidId = maidId;
        this.customerId = customerId;
        this.usagePlan = usagePlan;
        this.cost = cost;
    }
}
