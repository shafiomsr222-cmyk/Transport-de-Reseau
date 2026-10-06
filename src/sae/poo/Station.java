package sae.poo;

import fr.ulille.but.sae_s2_2026.Lieu;
import fr.ulille.but.sae_s2_2026.ModaliteTransport;

public class Station implements Lieu {
    private final Ville ville;
    private final ModaliteTransport modaliteTransport;

    public Station(Ville ville, ModaliteTransport modaliteTransport){
        this.ville = ville;
        this.modaliteTransport = modaliteTransport;
    }

    public Ville getVille() {
        return ville;
    }

    public ModaliteTransport getModaliteTransport() {
        return modaliteTransport;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Station o = (Station) obj;
        if (!this.ville.equals(o.ville)) return false;
        if (this.modaliteTransport != o.modaliteTransport) return false;
        return true;
    }

    @Override
    public int hashCode() {
        return this.ville.hashCode() + this.modaliteTransport.hashCode();
    }

    @Override
    public String toString() {
        return this.ville.toString() + "-" + this.modaliteTransport.toString();
    }
}