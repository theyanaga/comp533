package com.theyanaga.grading;

public enum OrderType {
    MONITOR("Monitor"),
    CONDITION("Condition"),
    URGENT("Urgent");

    private String label;

    private OrderType(String label) {this.label = label;}

    public String getLabel() {
        return label;
    }
}
