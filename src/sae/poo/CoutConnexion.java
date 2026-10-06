package sae.poo;

import java.util.HashMap;

public class CoutConnexion {
    private HashMap<TypeCout, Double> cout = new HashMap<TypeCout, Double>();

    public CoutConnexion(HashMap<TypeCout, Double> cout) {
        this.cout = cout;
    }

    public CoutConnexion() {
        this(new HashMap<TypeCout, Double>());
    }

    public void addCout(TypeCout type, double critere) {
        this.cout.put(type, critere);
    }

    public Double getCout(TypeCout type) {
        return this.cout.get(type);
    }

    public double getCoutdouble(TypeCout type) {
        return this.cout.get(type).doubleValue();
    }


}
