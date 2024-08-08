package com.sqli.gdmr.Enums;

public enum GroupeSanguin {
    A_POSITIF("A+"),
    A_NEGATIF("A-"),
    B_POSITIF("B+"),
    B_NEGATIF("B-"),
    O_POSITIF("O+"),
    O_NEGATIF("O-"),
    AB_POSITIF("AB+"),
    AB_NEGATIF("AB-");

    private final String label;

    GroupeSanguin(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}