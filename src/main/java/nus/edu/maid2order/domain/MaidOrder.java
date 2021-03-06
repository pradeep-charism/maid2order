package nus.edu.maid2order.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class MaidOrder {

    @Id
    private Long maidOrderId;

    private Long maidId;
    private Long customerId;
    private Long maidUsagePlanId;
    private Double maidUsageBill;

    public MaidOrder(Long maidId, Long customerId, Long maidUsagePlanId) {
        this.maidId = maidId;
        this.customerId = customerId;
        this.maidUsagePlanId = maidUsagePlanId;
        this.maidOrderId = customerId;
    }

    public Double calculateCost(MaidUsagePlan plan) {
        return plan.getCost();
    }

    public Long getMaidOrderId() {
        return maidOrderId;
    }

    public Long getMaidId() {
        return maidId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public Long getMaidUsagePlanId() {
        return maidUsagePlanId;
    }
}
