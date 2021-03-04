package nus.edu.maid2order.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class MaidUsagePlan {

    @Id
    @GeneratedValue
    private Long id;
    private UsagePlan usagePlan;

    public MaidUsagePlan(UsagePlan plan) {
        this.usagePlan = plan;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
