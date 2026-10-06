package sae.graphe;

import fr.ulille.but.sae_s2_2026.AlgorithmeKPCC;
import fr.ulille.but.sae_s2_2026.Chemin;
import fr.ulille.but.sae_s2_2026.ModaliteTransport;
import fr.ulille.but.sae_s2_2026.MultiGrapheOrienteValue;

import java.util.List;


public class GraphesV1 {

    public static String m(Chemin s) {
        String mot = "";
        for (int i = 0; i < s.aretes().size(); i++) {
            mot = mot + s.aretes().get(i).getModalite() + " de " + s.aretes().get(i).getDepart() + " à " +
                    s.aretes().get(i).getArrivee() + " avec itinéraire " + " " + s.poids() + "\n";
        }
        return mot;

    }

    public static void main(String[] arg) {
        // l'intialisation de graphe
        MultiGrapheOrienteValue ville = new MultiGrapheOrienteValue();

        // Les sommets
        MonLieu P = new MonLieu("Paris");
        MonLieu B = new MonLieu("Bordeaux");
        MonLieu S = new MonLieu("Strasbourg");
        MonLieu L = new MonLieu("Lyon");
        MonLieu M = new MonLieu("Marseille");

        //l'ajoutation dans la graphe

        ville.ajouterSommet(P);
        ville.ajouterSommet(B);
        ville.ajouterSommet(S);
        ville.ajouterSommet(L);
        ville.ajouterSommet(M);

        //Les connexions entres les sommets
        // les arretes entres paris Lyon
        maConnexion plBus = new maConnexion(P, L, ModaliteTransport.BUS);
        maConnexion lpBus = new maConnexion(L, P, ModaliteTransport.BUS);
        maConnexion plTrain = new maConnexion(P, L, ModaliteTransport.TRAIN);
        maConnexion lpTrain = new maConnexion(L, P, ModaliteTransport.TRAIN);
        maConnexion plAvion = new maConnexion(P, L, ModaliteTransport.AVION);
        maConnexion lpAvion = new maConnexion(L, P, ModaliteTransport.AVION);

        //Les arretes entres paris bordeaux
        maConnexion pbBus = new maConnexion(P, B, ModaliteTransport.BUS);
        maConnexion pbTrain = new maConnexion(P, B, ModaliteTransport.TRAIN);
        maConnexion pbAvion = new maConnexion(P, B, ModaliteTransport.AVION);
        maConnexion bpBus = new maConnexion(B, P, ModaliteTransport.BUS);
        maConnexion bpTrain = new maConnexion(B, P, ModaliteTransport.TRAIN);
        maConnexion bpAvion = new maConnexion(B, P, ModaliteTransport.AVION);

        // les arretes entre paris starsbourg
        maConnexion psBus = new maConnexion(P, S, ModaliteTransport.BUS);
        maConnexion psTrain = new maConnexion(P, S, ModaliteTransport.TRAIN);
        maConnexion spBus = new maConnexion(S, P, ModaliteTransport.BUS);
        maConnexion spTrain = new maConnexion(S, P, ModaliteTransport.TRAIN);

        //les arretes entre lyon strasbourg
        maConnexion lsBus = new maConnexion(L, S, ModaliteTransport.BUS);
        maConnexion lsTrain = new maConnexion(L, S, ModaliteTransport.TRAIN);
        maConnexion slBus = new maConnexion(S, L, ModaliteTransport.BUS);
        maConnexion slTrain = new maConnexion(S, L, ModaliteTransport.TRAIN);

        // les arrets entre lyon marseille
        maConnexion lmBus = new maConnexion(L, M, ModaliteTransport.BUS);
        maConnexion lmTrain = new maConnexion(L, M, ModaliteTransport.TRAIN);
        maConnexion lmAvion = new maConnexion(L, M, ModaliteTransport.AVION);
        maConnexion mlBus = new maConnexion(M, L, ModaliteTransport.BUS);
        maConnexion mlTrain = new maConnexion(M, L, ModaliteTransport.TRAIN);
        maConnexion mlAvion = new maConnexion(M, L, ModaliteTransport.AVION);

        // les  arrets entres Bordeaux marseille
        maConnexion bmBus = new maConnexion(B, M, ModaliteTransport.BUS);
        maConnexion bmTrain = new maConnexion(B, M, ModaliteTransport.TRAIN);
        maConnexion mbBus = new maConnexion(M, B, ModaliteTransport.BUS);
        maConnexion mbTrain = new maConnexion(M, B, ModaliteTransport.TRAIN);

        //les arretes entres marseille starsbourg
        maConnexion msTrain = new maConnexion(M, S, ModaliteTransport.TRAIN);
        maConnexion msAvion = new maConnexion(M, S, ModaliteTransport.AVION);
        maConnexion smTrain = new maConnexion(S, M, ModaliteTransport.TRAIN);
        maConnexion smAvion = new maConnexion(S, M, ModaliteTransport.AVION);

        //les arrets
        // paris lyon
        ville.ajouterArete(plBus, 5.5);
        ville.ajouterArete(lpBus, 5.5);
        ville.ajouterArete(plTrain, 2.0);
        ville.ajouterArete(lpTrain, 2.0);
        ville.ajouterArete(plAvion, 2.5);
        ville.ajouterArete(lpAvion, 2.5);

        //paris bordeaux
        ville.ajouterArete(pbBus, 6.5);
        ville.ajouterArete(bpBus, 6.5);
        ville.ajouterArete(pbTrain, 2.1);
        ville.ajouterArete(bpTrain, 2.1);
        ville.ajouterArete(pbAvion, 2.8);
        ville.ajouterArete(bpAvion, 2.8);


        //paris starsbourg
        ville.ajouterArete(psBus, 5.5);
        ville.ajouterArete(spBus, 5.5);
        ville.ajouterArete(psTrain, 1.8);
        ville.ajouterArete(spTrain, 1.8);

        //lyon strasbourg
        ville.ajouterArete(lsBus, 5.5);
        ville.ajouterArete(slBus, 5.5);
        ville.ajouterArete(lsTrain, 2.2);
        ville.ajouterArete(slTrain, 2.2);


        // lyon marseille
        ville.ajouterArete(lmBus, 3.5);
        ville.ajouterArete(lmTrain, 1.7);
        ville.ajouterArete(lmAvion, 2.2);
        ville.ajouterArete(mlBus, 3.5);
        ville.ajouterArete(mlTrain, 1.7);
        ville.ajouterArete(mlAvion, 2.2);

        // Bordeaux marseille
        ville.ajouterArete(bmBus, 7.2);
        ville.ajouterArete(mbBus, 7.2);
        ville.ajouterArete(bmTrain, 3.2);
        ville.ajouterArete(mbTrain, 3.2);

        // Marseille strasbourg
        ville.ajouterArete(msTrain, 4.5);
        ville.ajouterArete(smTrain, 4.5);
        ville.ajouterArete(msAvion, 3.2);
        ville.ajouterArete(smAvion, 3.2);


        List<Chemin> l = AlgorithmeKPCC.kpcc(ville, P, M, 4);


        for (int i = 0; i < l.size(); i++) {
            System.out.println(GraphesV1.m(l.get(i)));
        }


    }


}


