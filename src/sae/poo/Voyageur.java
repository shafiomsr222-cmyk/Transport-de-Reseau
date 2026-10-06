package sae.poo;

import java.io.IOException;
import java.io.Serializable;
import java.util.EnumMap;
import java.util.Map;

/**
 * Représente un utilisateur de la plateforme.
 *
 * Fonctionnalités :
 * - Pondération multi-critères (PRIX, CO2, TEMPS) pour le calcul du score combiné
 * - Contraintes max (prix, temps, CO2) pour filtrer les trajets inacceptables
 * - Historique des voyages sérialisable (sauvegarde et chargement sur disque)
 */
public class Voyageur implements Serializable {

    private static int unknownNumber = 1;

    private String name;

    /** Poids de chaque critère pour le calcul du score combiné. */
    private Map<TypeCout, Double> preferences;

    /** Contraintes maximales acceptables par le voyageur (null = pas de contrainte). */
    private Double maxPrix;
    private Double maxTemps;
    private Double maxCo2;

    /** Historique des voyages effectués. */
    private HistoriqueVoyage historique;

    /**
     * Crée un voyageur nommé avec des poids de préférences.
     *
     * @param name       Nom du voyageur
     * @param poidsPrix  Poids du critère PRIX  (ex: 0.5)
     * @param poidsTemps Poids du critère TEMPS (ex: 0.3)
     * @param poidsCo2   Poids du critère CO2   (ex: 0.2)
     */
    public Voyageur(String name, double poidsPrix, double poidsTemps, double poidsCo2) {
        this.name = name;
        this.preferences = new EnumMap<>(TypeCout.class);
        this.preferences.put(TypeCout.PRIX, poidsPrix);
        this.preferences.put(TypeCout.TEMPS, poidsTemps);
        this.preferences.put(TypeCout.CO2, poidsCo2);
        this.maxPrix  = null;
        this.maxTemps = null;
        this.maxCo2   = null;
        this.historique = new HistoriqueVoyage();
    }

    /** Crée un voyageur anonyme avec des poids de préférences. */
    public Voyageur(double poidsPrix, double poidsTemps, double poidsCo2) {
        this("Unknown" + unknownNumber++, poidsPrix, poidsTemps, poidsCo2);
    }

    // ─── Getters / Setters ───────────────────────────────────────────────────

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getPoids(TypeCout type) {
        return this.preferences.getOrDefault(type, 0.0);
    }

    public void setPoids(TypeCout type, double valeur) {
        this.preferences.put(type, valeur);
    }

    public Map<TypeCout, Double> getPreferences() {
        return new EnumMap<>(this.preferences);
    }

    public Double getMaxPrix()  { return maxPrix; }
    public Double getMaxTemps() { return maxTemps; }
    public Double getMaxCo2()   { return maxCo2; }

    public void setMaxPrix(Double maxPrix)   { this.maxPrix = maxPrix; }
    public void setMaxTemps(Double maxTemps) { this.maxTemps = maxTemps; }
    public void setMaxCo2(Double maxCo2)     { this.maxCo2 = maxCo2; }

    // ─── Historique ──────────────────────────────────────────────────────────

    /** Ajoute un voyage à l'historique du voyageur. */
    public void ajouterVoyage(Voyage v) {
        this.historique.ajouterVoyage(v);
    }

    public HistoriqueVoyage getHistoriqueVoyage() {
        return this.historique;
    }

    /**
     * Sauvegarde l'historique des voyages dans un fichier binaire.
     *
     * @param chemin Chemin du fichier de sauvegarde (ex: "historique_alice.dat")
     */
    public void sauvegarder(String chemin) throws IOException {
        this.historique.sauvegarder(chemin);
    }

    /**
     * Charge l'historique des voyages depuis un fichier binaire.
     *
     * @param chemin Chemin du fichier à charger
     */
    public void chargerHistorique(String chemin) throws IOException, ClassNotFoundException {
        this.historique = HistoriqueVoyage.charger(chemin);
    }

    // ─── Affichage ───────────────────────────────────────────────────────────

    @Override
    public String toString() {
        return this.name + " " + this.preferences
                + " [maxPrix=" + maxPrix
                + ", maxTemps=" + maxTemps
                + ", maxCo2=" + maxCo2 + "]";
    }
}