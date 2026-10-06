package sae.poo;

public class InvalidCSVData extends RuntimeException {
    private final int ligne;
    private final String raison;

    public InvalidCSVData() {
        this.ligne = -1;
        this.raison = null;
    }

    public InvalidCSVData(String message, int ligne, String raison) {
        super(message);
        this.ligne = ligne;
        this.raison = raison;
    }

    public int getLigne() {
        return this.ligne;
    }

    public String getRaison() {
        return this.raison;
    }
}