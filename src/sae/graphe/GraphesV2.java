package sae.graphe;

import fr.ulille.but.sae_s2_2026.AlgorithmeKPCC;
import fr.ulille.but.sae_s2_2026.Chemin;
import fr.ulille.but.sae_s2_2026.ModaliteTransport;
import fr.ulille.but.sae_s2_2026.MultiGrapheOrienteValue;

import java.util.List;


public class GraphesV2 {

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
        MonLieu S = new MonLieu("Strasbourg");
        MonLieu L = new MonLieu("Lyon");
        MonLieu M = new MonLieu("Marseille");
        MonLieu B = new MonLieu("Bordeaux");
        MonLieu PB = new MonLieu("ParisBus");
        MonLieu PT = new MonLieu("ParisTrain");
        MonLieu PA = new MonLieu("ParisAvion");
        MonLieu BB = new MonLieu("BordeauxBus");
        MonLieu BT = new MonLieu("BordeauxTrain");
        MonLieu BA = new MonLieu("BordeauxAvion");
        MonLieu SB = new MonLieu("StrasbourgBus");
        MonLieu ST = new MonLieu("StrasbourgTrain");
        MonLieu SA = new MonLieu("StrasbourgAvion");
        MonLieu LB = new MonLieu("LyonBus");
        MonLieu LT = new MonLieu("LyonTrain");
        MonLieu LA = new MonLieu("LyonAvion");
        MonLieu MB = new MonLieu("MarseilleBus");
        MonLieu MT = new MonLieu("MarseilleTrain");
        MonLieu MA = new MonLieu("MarseilleAvion");

        //l'ajoutation dans la graphe

        ville.ajouterSommet(L);
        ville.ajouterSommet(M);
        ville.ajouterSommet(S);
        ville.ajouterSommet(B);
        ville.ajouterSommet(P);
        ville.ajouterSommet(PB);
        ville.ajouterSommet(PT);
        ville.ajouterSommet(PA);
        ville.ajouterSommet(BB);
        ville.ajouterSommet(BT);
        ville.ajouterSommet(BA);
        ville.ajouterSommet(SB);
        ville.ajouterSommet(ST);
        ville.ajouterSommet(SA);
        ville.ajouterSommet(LB);
        ville.ajouterSommet(LT);
        ville.ajouterSommet(LA);
        ville.ajouterSommet(MB);
        ville.ajouterSommet(MT);
        ville.ajouterSommet(MA);

        //Les connexions entres les sommets 
        // les arretes entres paris Lyon
        maConnexion plBus = new maConnexion(PB, LB, ModaliteTransport.BUS);
        maConnexion plTrain = new maConnexion(PT, LT, ModaliteTransport.TRAIN);
        maConnexion plAvion = new maConnexion(PA, LA, ModaliteTransport.AVION);
        maConnexion lpBus = new maConnexion(LB, PB, ModaliteTransport.BUS);
        maConnexion lpTrain = new maConnexion(LT, PT, ModaliteTransport.TRAIN);
        maConnexion lpAvion = new maConnexion(LA, PA, ModaliteTransport.AVION);

        //Les arretes entres paris bordeaux
        maConnexion pbBus = new maConnexion(PB, BB, ModaliteTransport.BUS);
        maConnexion pbTrain = new maConnexion(PT, BT, ModaliteTransport.TRAIN);
        maConnexion pbAvion = new maConnexion(PA, BA, ModaliteTransport.AVION);
        maConnexion bpBus = new maConnexion(BB, PB, ModaliteTransport.BUS);
        maConnexion bpTrain = new maConnexion(BT, PT, ModaliteTransport.TRAIN);
        maConnexion bpAvion = new maConnexion(BA, PA, ModaliteTransport.AVION);

        // les arretes entre paris starsbourg
        maConnexion psBus = new maConnexion(PB, SB, ModaliteTransport.BUS);
        maConnexion psTrain = new maConnexion(PT, ST, ModaliteTransport.TRAIN);
        maConnexion spBus = new maConnexion(SB, PB, ModaliteTransport.BUS);
        maConnexion spTrain = new maConnexion(ST, PT, ModaliteTransport.TRAIN);

        //les arretes entre lyon strasbourg
        maConnexion lsBus = new maConnexion(LB, SB, ModaliteTransport.BUS);
        maConnexion lsTrain = new maConnexion(LT, ST, ModaliteTransport.TRAIN);
        maConnexion slBus = new maConnexion(SB, LB, ModaliteTransport.BUS);
        maConnexion slTrain = new maConnexion(ST, LT, ModaliteTransport.TRAIN);

        // les arrets entre lyon marseille
        maConnexion lmBus = new maConnexion(LB, MB, ModaliteTransport.BUS);
        maConnexion lmTrain = new maConnexion(LT, MT, ModaliteTransport.TRAIN);
        maConnexion lmAvion = new maConnexion(LA, MA, ModaliteTransport.AVION);
        maConnexion mlBus = new maConnexion(MB, LB, ModaliteTransport.BUS);
        maConnexion mlTrain = new maConnexion(MT, LT, ModaliteTransport.TRAIN);
        maConnexion mlAvion = new maConnexion(MA, LA, ModaliteTransport.AVION);

        // les  arrets entres Bordeaux marseille
        maConnexion bmBus = new maConnexion(BB, MB, ModaliteTransport.BUS);
        maConnexion bmTrain = new maConnexion(BT, MT, ModaliteTransport.TRAIN);
        maConnexion mbBus = new maConnexion(MB, BB, ModaliteTransport.BUS);
        maConnexion mbTrain = new maConnexion(MT, BT, ModaliteTransport.TRAIN);

        //les arretes entres marseille starsbourg
        maConnexion msTrain = new maConnexion(MT, ST, ModaliteTransport.TRAIN);
        maConnexion msAvion = new maConnexion(MA, SA, ModaliteTransport.AVION);
        maConnexion smTrain = new maConnexion(ST, MT, ModaliteTransport.TRAIN);
        maConnexion smAvion = new maConnexion(SA, MA, ModaliteTransport.AVION);

        //Les arretes entre PARIS

        maConnexion ppb = new maConnexion(P, PB, ModaliteTransport.BUS);
        maConnexion ppt = new maConnexion(P, PT, ModaliteTransport.BUS);
        maConnexion ppa = new maConnexion(P, PA, ModaliteTransport.BUS);
        maConnexion pap = new maConnexion(PA, P, ModaliteTransport.BUS);
        maConnexion ptp = new maConnexion(PT, P, ModaliteTransport.BUS);
        maConnexion pbp = new maConnexion(PB, P, ModaliteTransport.BUS);


        // Les arretes entres Lyon
        maConnexion llb = new maConnexion(L, LB, ModaliteTransport.BUS);
        maConnexion llt = new maConnexion(L, LT, ModaliteTransport.BUS);
        maConnexion lla = new maConnexion(L, LA, ModaliteTransport.BUS);
        maConnexion lal = new maConnexion(LA, L, ModaliteTransport.BUS);
        maConnexion lbl = new maConnexion(LB, L, ModaliteTransport.BUS);
        maConnexion ltl = new maConnexion(LT, L, ModaliteTransport.BUS);

        // les arretes entre Bordeaux

        maConnexion bbb = new maConnexion(B, BB, ModaliteTransport.BUS);
        maConnexion bt = new maConnexion(B, BT, ModaliteTransport.BUS);
        maConnexion ba = new maConnexion(B, BA, ModaliteTransport.BUS);
        maConnexion bb = new maConnexion(BB, B, ModaliteTransport.BUS);
        maConnexion tb = new maConnexion(BT, B, ModaliteTransport.BUS);
        maConnexion ab = new maConnexion(BA, B, ModaliteTransport.BUS);

        // les arretes entre Strasbourg

        maConnexion ssb = new maConnexion(S, SB, ModaliteTransport.BUS);
        maConnexion sst = new maConnexion(S, ST, ModaliteTransport.BUS);
        maConnexion ssa = new maConnexion(S, SA, ModaliteTransport.BUS);
        maConnexion sas = new maConnexion(SA, S, ModaliteTransport.BUS);
        maConnexion sbs = new maConnexion(SB, S, ModaliteTransport.BUS);
        maConnexion sts = new maConnexion(ST, S, ModaliteTransport.BUS);


        // les arret entre Marseille

        maConnexion mmb = new maConnexion(M, MB, ModaliteTransport.BUS);
        maConnexion mmt = new maConnexion(M, MT, ModaliteTransport.BUS);
        maConnexion mma = new maConnexion(M, MA, ModaliteTransport.BUS);
        maConnexion mam = new maConnexion(MA, M, ModaliteTransport.BUS);
        maConnexion mbm = new maConnexion(MB, M, ModaliteTransport.BUS);
        maConnexion mtm = new maConnexion(MT, M, ModaliteTransport.BUS);

        //les arrets

        //Entre Paris
        ville.ajouterArete(ppb, 2.55);
        ville.ajouterArete(ppt, 2.55);
        ville.ajouterArete(ppa, 13.0);
        ville.ajouterArete(pbp, 2.55);
        ville.ajouterArete(ptp, 2.55);
        ville.ajouterArete(pap, 13.0);

        //Entre Lyon
        ville.ajouterArete(llb, 2.10);
        ville.ajouterArete(llt, 2.10);
        ville.ajouterArete(lla, 10.0);
        ville.ajouterArete(lal, 10.0);
        ville.ajouterArete(lbl, 2.10);
        ville.ajouterArete(ltl, 2.10);


        //Entre Bordeaux

        ville.ajouterArete(bbb, 1.90);
        ville.ajouterArete(bt, 1.90);
        ville.ajouterArete(ba, 8.0);
        ville.ajouterArete(ab, 8.0);
        ville.ajouterArete(tb, 1.90);
        ville.ajouterArete(bb, 1.90);


        //Entre Strasbourg

        ville.ajouterArete(ssb, 1.90);
        ville.ajouterArete(sst, 1.90);
        ville.ajouterArete(ssa, 9.0);
        ville.ajouterArete(sas, 9.0);
        ville.ajouterArete(sts, 1.90);
        ville.ajouterArete(sbs, 1.90);

        // Entre Marseille

        ville.ajouterArete(mma, 7.0);
        ville.ajouterArete(mmb, 1.70);
        ville.ajouterArete(mmt, 1.70);
        ville.ajouterArete(mtm, 1.70);
        ville.ajouterArete(mbm, 1.70);
        ville.ajouterArete(mam, 7.0);

        // paris lyon
        ville.ajouterArete(plBus, 25.0);
        ville.ajouterArete(lpBus, 25.0);
        ville.ajouterArete(plTrain, 60.0);
        ville.ajouterArete(lpTrain, 60.0);
        ville.ajouterArete(plAvion, 110.0);
        ville.ajouterArete(lpAvion, 110.0);

        //paris bordeaux
        ville.ajouterArete(pbBus, 30.0);
        ville.ajouterArete(bpBus, 30.0);
        ville.ajouterArete(pbTrain, 75.0);
        ville.ajouterArete(bpTrain, 75.0);
        ville.ajouterArete(pbAvion, 120.0);
        ville.ajouterArete(bpAvion, 120.0);


        //paris starsbourg
        ville.ajouterArete(psBus, 26.0);
        ville.ajouterArete(spBus, 26.0);
        ville.ajouterArete(psTrain, 65.0);
        ville.ajouterArete(spTrain, 65.0);

        //lyon strasbourg
        ville.ajouterArete(lsBus, 27.0);
        ville.ajouterArete(slBus, 27.0);
        ville.ajouterArete(lsTrain, 58.0);
        ville.ajouterArete(slTrain, 58.0);


        // lyon marseille
        ville.ajouterArete(lmBus, 18.0);
        ville.ajouterArete(lmTrain, 45.0);
        ville.ajouterArete(lmAvion, 90.0);
        ville.ajouterArete(mlBus, 18.0);
        ville.ajouterArete(mlTrain, 45.0);
        ville.ajouterArete(mlAvion, 90.0);

        // Bordeaux marseille
        ville.ajouterArete(bmBus, 32.0);
        ville.ajouterArete(mbBus, 32.0);
        ville.ajouterArete(bmTrain, 85.0);
        ville.ajouterArete(mbTrain, 85.0);

        // Marseille strasbourg
        ville.ajouterArete(msTrain, 95.0);
        ville.ajouterArete(smTrain, 95.0);
        ville.ajouterArete(msAvion, 140.0);
        ville.ajouterArete(smAvion, 140.0);


        List<Chemin> l = AlgorithmeKPCC.kpcc(ville, S, B, 4);


        for (int i = 0; i < l.size(); i++) {
            System.out.println(GraphesV2.m(l.get(i)));
        }


    }


}


