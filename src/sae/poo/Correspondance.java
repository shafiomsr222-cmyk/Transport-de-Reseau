package sae.poo;

import fr.ulille.but.sae_s2_2026.Connexion;
import fr.ulille.but.sae_s2_2026.Lieu;
import fr.ulille.but.sae_s2_2026.ModaliteTransport;

public class Correspondance implements Connexion {

    //Initialisation des variable 
    private Station stationDepart;  //ville de départ
    private Station stationArrivee;   //station d'arrivée
    private CoutConnexion coutCorrespondance; //cout de correspondance

    public Correspondance(Station stationDepart, Station stationArrivee, double prix, double co2, double temps) {
        this.stationDepart = stationDepart;
        this.stationArrivee = stationArrivee;

        // Encapsulation : la classe instancie elle-même ses coûts
        this.coutCorrespondance = new CoutConnexion();
        this.coutCorrespondance.addCout(TypeCout.PRIX, prix);
        this.coutCorrespondance.addCout(TypeCout.CO2, co2);
        this.coutCorrespondance.addCout(TypeCout.TEMPS, temps);
    }

    //Accesseur
    @Override
    public Lieu getDepart() {
        return stationDepart;
    }

    @Override
    public Lieu getArrivee() {
        return stationArrivee;
    }

    @Override
    public ModaliteTransport getModalite() {
        return stationArrivee.getModaliteTransport();
    }

    //Acceseur de l'Enumération type de cout 

    public double getCoutPrix() {
        return coutCorrespondance.getCout(TypeCout.PRIX);
    }

    public double getCoutTemps() {
        return coutCorrespondance.getCout(TypeCout.TEMPS);
    }

    public double getCoutCo2() {
        return coutCorrespondance.getCout(TypeCout.CO2);
    }

    //Affichage des correspondance

    @Override
    public String toString() {
        return "Correspondance à " + this.stationDepart.getVille() + " (" + this.stationDepart.getModaliteTransport() + " -> " + this.stationArrivee.getModaliteTransport() + ")";
    }
}