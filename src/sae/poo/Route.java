package sae.poo;

import fr.ulille.but.sae_s2_2026.Connexion;
import fr.ulille.but.sae_s2_2026.ModaliteTransport;


public class Route implements Connexion {
    private final Station depart;
    private final Station arrivee;
    private final ModaliteTransport transport;
    private final CoutConnexion coutConnexion;

    public Route(Station depart, Station arrivee, ModaliteTransport Transport, double prix, double co, double temp) {
        this.depart = depart;
        this.arrivee = arrivee;
        this.transport = Transport;
        this.coutConnexion = new CoutConnexion();
        this.coutConnexion.addCout(TypeCout.PRIX, prix);
        this.coutConnexion.addCout(TypeCout.CO2, co);
        this.coutConnexion.addCout(TypeCout.TEMPS, temp);
    }

    public Station getDepart() {
        return depart;
    }

    public Station getArrivee() {
        return arrivee;
    }

    public double getCoutPrix() {
        return coutConnexion.getCout(TypeCout.PRIX);
    }

    public double getCoutTemps() {
        return coutConnexion.getCout(TypeCout.TEMPS);
    }

    public double getCoutCo2() {
        return coutConnexion.getCout(TypeCout.CO2);
    }

    @Override
    public ModaliteTransport getModalite() {
        return this.transport;
    }

    @Override
    public String toString() {
        return this.depart + " -> " + this.arrivee + " (" + this.transport + ")";
    }
}
