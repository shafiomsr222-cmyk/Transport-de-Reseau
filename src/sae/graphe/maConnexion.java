package sae.graphe;

import fr.ulille.but.sae_s2_2026.Connexion;
import fr.ulille.but.sae_s2_2026.Lieu;
import fr.ulille.but.sae_s2_2026.ModaliteTransport;

public class maConnexion implements Connexion {
    private final Lieu arrive;
    private final Lieu depart;
    private final ModaliteTransport transport;

    public maConnexion(Lieu b, Lieu a, ModaliteTransport t) {
        this.arrive = a;
        this.depart = b;
        this.transport = t;
    }

    @Override
    public Lieu getArrivee() {
        // TODO Auto-generated method stub
        return this.arrive;
    }

    public Lieu getDepart() {
        return this.depart;
    }

    @Override
    public ModaliteTransport getModalite() {
        // TODO Auto-generated method stub
        return this.transport;
    }

    public String toString() {
        return this.depart + "->" + this.arrive + "(" + this.transport + ")";
    }

}
