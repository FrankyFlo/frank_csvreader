package org.example;

public class Standort {
    private String stadt;
    private String land;

    public Standort(String land, String stadt) {
        this.stadt = stadt;
        this.land = land;
    }

    public String getStadt() {
        return stadt;
    }

    public String getLand() {
        return land;
    }

    @Override
    public String toString() {
        return "Standort{" +
                "stadt='" + stadt + '\'' +
                ", land='" + land + '\'' +
                '}';
    }
}
