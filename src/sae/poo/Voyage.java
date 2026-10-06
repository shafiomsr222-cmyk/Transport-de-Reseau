package sae.poo;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 * Représente un trajet effectué par un voyageur.
 * Contient les informations de coût, la date, et les étapes du trajet.
 * Sérialisable pour la persistance sur disque.
 */
public class Voyage implements Serializable {

    private final String VILLE_DEPART;
    private final String VILLE_ARRIVEE;
    private final double PRIX;
    private final double TEMPS;
    private final double CO2;
    private final LocalDate DATE;
    private final List<String> ETAPES;

    public Voyage(String villeDepart, String villeArrivee, double prix, double temps, double co2, LocalDate date, List<String> etapes) {
        this.VILLE_DEPART = villeDepart;
        this.VILLE_ARRIVEE = villeArrivee;
        this.PRIX = prix;
        this.TEMPS = temps;
        this.CO2 = co2;
        this.DATE = date;
        this.ETAPES = etapes;
    }

    public String getVilleDepart()  { return VILLE_DEPART; }
    public String getVilleArrivee() { return VILLE_ARRIVEE; }
    public double getPrix()         { return PRIX; }
    public double getTemps()        { return TEMPS; }
    public double getCo2()          { return CO2; }
    public LocalDate getDate()      { return DATE; }
    public List<String> getEtapes() { return ETAPES; }

    /**
     * Retourne le coût selon le critère demandé.
     */
    public double getCout(TypeCout type) {
        return switch (type) {
            case PRIX  -> PRIX;
            case TEMPS -> TEMPS;
            case CO2   -> CO2;
        };
    }

    @Override
    public String toString() {
        return "Voyage de " + VILLE_DEPART + " à " + VILLE_ARRIVEE
                + " | Prix: " + PRIX + "€, Temps: " + TEMPS + "min, CO2: " + CO2 + "kg"
                + " | Date: " + DATE
                + "\n  Étapes: " + ETAPES;
    }
}