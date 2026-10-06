package sae.poo;

import fr.ulille.but.sae_s2_2026.Lieu;

public class Ville implements Lieu {
    private String name;

    public Ville(String name){
        this.name=name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Ville o = (Ville) obj;
        if(!this.name.equals(o.name)) return false;
        return true;
    }

    @Override
    public int hashCode() {
        return this.name.hashCode();
    }

    @Override
    public String toString() {
        return this.name;
    }
}
