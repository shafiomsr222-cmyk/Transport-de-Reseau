package sae.graphe;

import fr.ulille.but.sae_s2_2026.ModaliteTransport;
import fr.ulille.but.sae_s2_2026.MultiGrapheOrienteValue;

class UseMultigraphe {
    public static void main(String[] args) {
        MultiGrapheOrienteValue g1 = new MultiGrapheOrienteValue();
        MonLieu a = new MonLieu("a");
        MonLieu b = new MonLieu("b");
        maConnexion c1 = new maConnexion(a, b, ModaliteTransport.TRAIN);
        g1.ajouterSommet(a);
        g1.ajouterSommet(b);
        g1.ajouterArete(c1, 1.0);

        System.out.println(g1);
    }


}