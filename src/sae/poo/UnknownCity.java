package sae.poo;

public class UnknownCity extends RuntimeException {
    private String mauvaiseVille;

    public UnknownCity() {
        super();
    }

    public UnknownCity(String message) {
        super(message);
    }

    public UnknownCity(String message, String mauvaiseVille) {
        super(message);
        this.mauvaiseVille = mauvaiseVille;
    }

    public String getMauvaiseVille() {
        return this.mauvaiseVille;
    }
}
