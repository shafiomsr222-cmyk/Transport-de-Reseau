package sae.poo;

import fr.ulille.but.sae_s2_2026.*;

public class LiaisonVirtuelle implements Connexion {
    private final Lieu DEPART;
    private final Lieu ARRIVEE;
    private final ModaliteTransport MODALITE;

    public LiaisonVirtuelle(Lieu depart, Lieu arrivee, ModaliteTransport modalite) {
        this.DEPART = depart;
        this.ARRIVEE = arrivee;
        this.MODALITE = modalite;
    }

    @Override public Lieu getDepart() { return DEPART; }
    @Override public Lieu getArrivee() { return ARRIVEE; }
    @Override public ModaliteTransport getModalite() { return MODALITE; }
}