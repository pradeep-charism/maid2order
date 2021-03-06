package nus.edu.maid2order.domain;

public enum UsagePlan {
    ONE_TIME(111L, 100.00),
    ONCE_A_FORTNIGHT(222L, 50.00),
    ONCE_A_WEEK(333L, 100.00),
    TWICE_A_WEEK(444L, 200.00);

    Long id;
    Double cost;

    UsagePlan(Long id, double cost) {
        this.id = id;
        this.cost = cost;
    }

    public Double getCost() {
        return cost;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "UsagePlan{" +
                "id=" + id +
                ", cost=" + cost +
                '}';
    }
}
