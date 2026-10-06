package sae.poo;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Stocke la liste des voyages effectués par un voyageur.
 * Sérialisable : peut être sauvegardée et rechargée depuis un fichier binaire.
 */
public class HistoriqueVoyage implements Serializable {

    private List<Voyage> voyages;

    public HistoriqueVoyage() {
        this.voyages = new ArrayList<>();
    }

    /** Ajoute un voyage à l'historique. */
    public void ajouterVoyage(Voyage v) {
        this.voyages.add(v);
    }

    public List<Voyage> getVoyages() {
        return this.voyages;
    }

    /**
     * Sauvegarde l'historique dans un fichier binaire via la sérialisation Java.
     */
    public void sauvegarder(String chemin) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(chemin))) {
            oos.writeObject(this);
        }
    }

    /**
     * Charge un historique depuis un fichier binaire.
     */
    public static HistoriqueVoyage charger(String chemin) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(chemin))) {
            return (HistoriqueVoyage) ois.readObject();
        }
    }

    /**
     * Retourne la liste des coûts selon un critère, dans l'ordre des voyages enregistrés.
     * Utile pour tracer l'évolution d'un critère dans le temps.
     */
    public List<Double> evolutionCout(TypeCout type) {
        List<Double> result = new ArrayList<>();
        for (Voyage v : this.voyages) {
            result.add(v.getCout(type));
        }
        return result;
    }

    @Override
    public String toString() {
        if (this.voyages.isEmpty()) return "Aucun voyage enregistré.";
        StringBuilder sb = new StringBuilder();
        for (Voyage v : this.voyages) {
            sb.append(v.toString()).append("\n");
        }
        return sb.toString();
    }
}