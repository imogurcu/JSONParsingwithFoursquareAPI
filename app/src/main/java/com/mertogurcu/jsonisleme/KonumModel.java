package com.mertogurcu.jsonisleme;

/**
 * Created by mertogrc on 4/8/17.
 */

public class KonumModel {

    private String name;
    private double enlem;
    private double boylam;
    private String uzaklik;

    public String getUzaklik() {
        return uzaklik;
    }

    public void setUzaklik(String uzaklik) {
        this.uzaklik = uzaklik;
    }

    public double getEnlem() {
        return enlem;
    }

    public void setEnlem(double enlem) {
        this.enlem = enlem;
    }

    public double getBoylam() {
        return boylam;
    }

    public void setBoylam(double boylam) {
        this.boylam = boylam;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
