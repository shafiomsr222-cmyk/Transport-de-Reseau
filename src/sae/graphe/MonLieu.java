package sae.graphe;

import fr.ulille.but.sae_s2_2026.Lieu;

public class MonLieu implements Lieu {
    private final String name;

    public MonLieu(String n) {
        this.name = n;
    }

    public String toString() {
        return this.name;
    }

}
