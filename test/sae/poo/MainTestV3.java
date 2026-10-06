package sae.poo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import java.lang.reflect.Method;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests de non-régression pour la V3 du projet.
 *
 * Ces tests vérifient :
 * - La structure et le comportement de Voyageur (poids multi-critères, contraintes max, historique)
 * - La structure de Plateforme (construireGraphe, getPoidsConnexion, respecteContraintes)
 * - La compatibilité des enums TypeCout
 * - Le bon comportement de Voyage et HistoriqueVoyage
 */
public class MainTestV3 {

    // ─── VOYAGEUR : structure multi-critères ──────────────────────────────────

    @Test
    @DisplayName("Voyageur expose getPreferences() ou getPoids() (multi-critères)")
    void voyageurDoitAvoirUneStructureMultiCriteres() {
        boolean hasGetPreferences = false;
        boolean hasGetPoids = false;

        for (Method method : Voyageur.class.getDeclaredMethods()) {
            if (method.getName().equals("getPreferences")) hasGetPreferences = true;
            if (method.getName().equals("getPoids"))       hasGetPoids = true;
        }

        assertTrue(hasGetPreferences || hasGetPoids,
            "En V3, Voyageur doit exposer une structure multi-critères (Map ou getters de poids).");
    }

    @Test
    @DisplayName("Voyageur ne expose plus getPremierCritere()")
    void voyageurNeDoitPlusDependreUniquementDePremierCritere() {
        boolean hasPremierCritereGetter = false;
        for (Method method : Voyageur.class.getDeclaredMethods()) {
            if (method.getName().equals("getPremierCritere")) {
                hasPremierCritereGetter = true;
                break;
            }
        }
        assertFalse(hasPremierCritereGetter,
            "En V3, Voyageur ne doit plus exposer getPremierCritere().");
    }

    @Test
    @DisplayName("Voyageur : le constructeur (String, double, double, double) existe")
    void voyageurConstructeurV3Existe() throws Exception {
        Voyageur user = new Voyageur("Alice", 0.5, 0.3, 0.2);
        assertNotNull(user);
        assertEquals("Alice", user.getName());
    }

    @Test
    @DisplayName("Voyageur : getPoids() retourne les bons poids")
    void voyageurGetPoidsRetourneLesBonsPoids() {
        Voyageur user = new Voyageur("Alice", 0.5, 0.3, 0.2);
        assertEquals(0.5, user.getPoids(TypeCout.PRIX),  1e-9);
        assertEquals(0.3, user.getPoids(TypeCout.TEMPS), 1e-9);
        assertEquals(0.2, user.getPoids(TypeCout.CO2),   1e-9);
    }

    @Test
    @DisplayName("Voyageur : getPreferences() retourne une Map non vide")
    void voyageurGetPreferencesRetourneMapNonVide() {
        Voyageur user = new Voyageur("Bob", 0.6, 0.3, 0.1);
        Map<TypeCout, Double> prefs = user.getPreferences();
        assertNotNull(prefs, "getPreferences ne doit pas renvoyer null.");
        assertFalse(prefs.isEmpty(), "La Map de préférences ne doit pas être vide.");
        assertEquals(3, prefs.size(), "La Map doit contenir 3 critères (PRIX, TEMPS, CO2).");
    }

    @Test
    @DisplayName("Voyageur : getPreferences() retourne une copie défensive (pas de modification externe)")
    void voyageurGetPreferencesEstCopieDefensive() {
        Voyageur user = new Voyageur("Alice", 0.5, 0.3, 0.2);
        Map<TypeCout, Double> prefs = user.getPreferences();
        prefs.put(TypeCout.PRIX, 999.0); // modification de la copie
        assertEquals(0.5, user.getPoids(TypeCout.PRIX), 1e-9,
            "getPreferences() doit retourner une copie, pas la référence interne.");
    }

    @Test
    @DisplayName("Voyageur : constructeur anonyme (sans nom) génère un nom automatique")
    void voyageurConstructeurAnonymeFonctionne() {
        Voyageur user = new Voyageur(0.4, 0.4, 0.2);
        assertNotNull(user.getName());
        assertTrue(user.getName().startsWith("Unknown"),
            "Le nom généré automatiquement doit commencer par 'Unknown'.");
    }

    // ─── VOYAGEUR : contraintes max ───────────────────────────────────────────

    @Test
    @DisplayName("Voyageur : contraintes max par défaut sont null (pas de limite)")
    void voyageurContraintesMaxDefautSontNull() {
        Voyageur user = new Voyageur("Alice", 0.5, 0.3, 0.2);
        assertNull(user.getMaxPrix(),  "maxPrix doit être null par défaut (pas de contrainte).");
        assertNull(user.getMaxTemps(), "maxTemps doit être null par défaut.");
        assertNull(user.getMaxCo2(),   "maxCo2 doit être null par défaut.");
    }

    @Test
    @DisplayName("Voyageur : setMaxPrix/Temps/Co2 fonctionnent correctement")
    void voyageurSetContraintesFonctionne() {
        Voyageur user = new Voyageur("Alice", 0.5, 0.3, 0.2);
        user.setMaxPrix(150.0);
        user.setMaxTemps(300.0);
        user.setMaxCo2(50.0);
        assertEquals(150.0, user.getMaxPrix(),  1e-9);
        assertEquals(300.0, user.getMaxTemps(), 1e-9);
        assertEquals(50.0,  user.getMaxCo2(),   1e-9);
    }

    @Test
    @DisplayName("Voyageur : setPoids met à jour une préférence existante")
    void voyageurSetPoidsMetAJourLaPreference() {
        Voyageur user = new Voyageur("Alice", 0.5, 0.3, 0.2);
        user.setPoids(TypeCout.PRIX, 0.8);
        assertEquals(0.8, user.getPoids(TypeCout.PRIX), 1e-9);
    }

    // ─── VOYAGEUR : historique ────────────────────────────────────────────────

    @Test
    @DisplayName("Voyageur : getHistoriqueVoyage() n'est pas null au démarrage")
    void voyageurHistoriqueInitialNonNull() {
        Voyageur user = new Voyageur("Alice", 0.5, 0.3, 0.2);
        assertNotNull(user.getHistoriqueVoyage(),
            "L'historique doit être initialisé (non null) à la création du voyageur.");
    }

    @Test
    @DisplayName("Voyageur : ajouterVoyage() ajoute bien un voyage à l'historique")
    void voyageurAjouterVoyageFonctionne() {
        Voyageur user = new Voyageur("Alice", 0.5, 0.3, 0.2);
        Voyage voyage = new Voyage("Paris", "Lyon", 80.0, 120.0, 10.0,
                java.time.LocalDate.now(), java.util.List.of("Paris-TRAIN", "Lyon-TRAIN"));

        user.ajouterVoyage(voyage);

        assertEquals(1, user.getHistoriqueVoyage().getVoyages().size(),
            "Après ajout, l'historique doit contenir 1 voyage.");
    }

    // ─── VOYAGE ───────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Voyage : getCout() retourne la bonne valeur selon le TypeCout")
    void voyageGetCoutRetourneLaBonneValeur() {
        Voyage v = new Voyage("Paris", "Lyon", 80.0, 120.0, 5.0,
                java.time.LocalDate.now(), java.util.List.of("Paris-TRAIN", "Lyon-TRAIN"));

        assertEquals(80.0,  v.getCout(TypeCout.PRIX),  1e-9);
        assertEquals(120.0, v.getCout(TypeCout.TEMPS), 1e-9);
        assertEquals(5.0,   v.getCout(TypeCout.CO2),   1e-9);
    }

    @Test
    @DisplayName("Voyage : tous les getters fonctionnent correctement")
    void voyageGettersFonctionnent() {
        java.time.LocalDate today = java.time.LocalDate.now();
        java.util.List<String> etapes = java.util.List.of("Paris-TRAIN", "Lyon-TRAIN");
        Voyage v = new Voyage("Paris", "Lyon", 80.0, 120.0, 5.0, today, etapes);

        assertEquals("Paris", v.getVilleDepart());
        assertEquals("Lyon",  v.getVilleArrivee());
        assertEquals(80.0,  v.getPrix(),  1e-9);
        assertEquals(120.0, v.getTemps(), 1e-9);
        assertEquals(5.0,   v.getCo2(),   1e-9);
        assertEquals(today, v.getDate());
        assertEquals(etapes, v.getEtapes());
    }

    // ─── HISTORIQUE VOYAGE ────────────────────────────────────────────────────

    @Test
    @DisplayName("HistoriqueVoyage : evolutionCout() retourne la liste des coûts dans l'ordre")
    void historiqueEvolutionCoutRetourneLaBonneListe() {
        HistoriqueVoyage historique = new HistoriqueVoyage();

        historique.ajouterVoyage(new Voyage("Paris", "Lyon",   80.0, 100.0, 5.0,
                java.time.LocalDate.now(), java.util.List.of()));
        historique.ajouterVoyage(new Voyage("Lyon",  "Marseille", 50.0, 80.0, 3.0,
                java.time.LocalDate.now(), java.util.List.of()));

        java.util.List<Double> evolution = historique.evolutionCout(TypeCout.PRIX);

        assertEquals(2, evolution.size());
        assertEquals(80.0, evolution.get(0), 1e-9);
        assertEquals(50.0, evolution.get(1), 1e-9);
    }

    @Test
    @DisplayName("HistoriqueVoyage : vide au démarrage, toString() ne plante pas")
    void historiqueVideToStringNePlantesPas() {
        HistoriqueVoyage historique = new HistoriqueVoyage();
        assertTrue(historique.getVoyages().isEmpty());
        assertDoesNotThrow(() -> historique.toString());
    }

    // ─── PLATEFORME : structure ───────────────────────────────────────────────

    @Test
    @DisplayName("Plateforme : construireGraphe(Voyageur) existe")
    void plateformeDoitConstruireLeGrapheAvecVoyageur() {
        boolean found = false;
        for (Method method : Plateforme.class.getDeclaredMethods()) {
            if (method.getName().equals("construireGraphe")
                    && method.getParameterCount() == 1
                    && method.getParameterTypes()[0].equals(Voyageur.class)) {
                found = true;
                break;
            }
        }
        assertTrue(found, "Plateforme doit avoir construireGraphe(Voyageur user).");
    }

    @Test
    @DisplayName("Plateforme : getPoidsConnexion(Connexion, Voyageur) existe")
    void plateformeDoitCalculerUnPoidsCombine() {
        boolean found = false;
        for (Method method : Plateforme.class.getDeclaredMethods()) {
            if (method.getName().equals("getPoidsConnexion")
                    && method.getParameterCount() == 2
                    && method.getParameterTypes()[0].getSimpleName().equals("Connexion")
                    && method.getParameterTypes()[1].equals(Voyageur.class)) {
                found = true;
                break;
            }
        }
        assertTrue(found,
            "getPoidsConnexion doit prendre (Connexion, Voyageur) pour calculer un coût combiné.");
    }

    @Test
    @DisplayName("Plateforme : respecteContraintes() existe")
    void plateformePeutVerifierLeRespectDesContraintes() {
        boolean found = false;
        for (Method method : Plateforme.class.getDeclaredMethods()) {
            if (method.getName().equals("respecteContraintes")) {
                found = true;
                break;
            }
        }
        assertTrue(found,
            "Plateforme doit proposer respecteContraintes() pour filtrer les trajets.");
    }

    @Test
    @DisplayName("Plateforme : instanciable avec les fichiers CSV du projet")
    void plateformePeutEtreInstancieeAvecLesCsvDuProjet() {
        assertDoesNotThrow(() -> {
            Voyageur user = new Voyageur("Alice", 0.5, 0.3, 0.2);
            Plateforme plateforme = new Plateforme(
                    "ressources/Poo-graphe.csv",
                    "ressources/Poo-correspondances.csv",
                    user);
            assertNotNull(plateforme);
        });
    }

    @Test
    @DisplayName("Plateforme : getVilles() retourne une liste non null après chargement CSV")
    void plateformeGetVillesNonNull() {
        Voyageur user = new Voyageur("Alice", 0.5, 0.3, 0.2);
        Plateforme plateforme = new Plateforme(
                "ressources/Poo-graphe.csv",
                "ressources/Poo-correspondances.csv",
                user);
        assertNotNull(plateforme.getVilles());
        assertFalse(plateforme.getVilles().isEmpty(),
            "La liste des villes ne doit pas être vide après chargement du CSV.");
    }

    @Test
    @DisplayName("Plateforme : upperFirstLettre() met la 1ère lettre en majuscule")
    void plateformeUpperFirstLettreFonctionne() {
        Voyageur user = new Voyageur("Alice", 0.5, 0.3, 0.2);
        Plateforme plateforme = new Plateforme(
                "ressources/Poo-graphe.csv",
                "ressources/Poo-correspondances.csv",
                user);
        assertEquals("Train", plateforme.upperFirstLettre("TRAIN"));
        assertEquals("Bus",   plateforme.upperFirstLettre("BUS"));
    }

    // ─── ENUMS ────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("TypeCout : les 3 valeurs PRIX, TEMPS, CO2 existent")
    void lesEnumsDuProjetRestentCompatibles() {
        assertNotNull(TypeCout.valueOf("PRIX"));
        assertNotNull(TypeCout.valueOf("TEMPS"));
        assertNotNull(TypeCout.valueOf("CO2"));
        assertEquals(3, TypeCout.values().length,
            "TypeCout doit avoir exactement 3 valeurs : PRIX, TEMPS, CO2.");
    }

    // ─── VILLE & STATION ──────────────────────────────────────────────────────

    @Test
    @DisplayName("Ville : equals() fonctionne sur le nom")
    void villeEqualsFonctionne() {
        Ville v1 = new Ville("Paris");
        Ville v2 = new Ville("Paris");
        Ville v3 = new Ville("Lyon");
        assertEquals(v1, v2);
        assertNotEquals(v1, v3);
    }

    @Test
    @DisplayName("Ville : hashCode cohérent avec equals()")
    void villeHashCodeCoherent() {
        Ville v1 = new Ville("Paris");
        Ville v2 = new Ville("Paris");
        assertEquals(v1.hashCode(), v2.hashCode());
    }

    @Test
    @DisplayName("Station : equals() dépend de la ville ET de la modalité")
    void stationEqualsFonctionne() {
        Ville paris = new Ville("Paris");
        fr.ulille.but.sae_s2_2026.ModaliteTransport train =
                fr.ulille.but.sae_s2_2026.ModaliteTransport.TRAIN;
        fr.ulille.but.sae_s2_2026.ModaliteTransport bus =
                fr.ulille.but.sae_s2_2026.ModaliteTransport.BUS;

        Station s1 = new Station(paris, train);
        Station s2 = new Station(paris, train);
        Station s3 = new Station(paris, bus);

        assertEquals(s1, s2,    "Même ville + même modalité = égaux.");
        assertNotEquals(s1, s3, "Même ville + modalité différente = inégaux.");
    }

    // ─── COUT CONNEXION ───────────────────────────────────────────────────────

    @Test
    @DisplayName("CoutConnexion : addCout() et getCout() fonctionnent correctement")
    void coutConnexionAddEtGetFonctionnent() {
        CoutConnexion cout = new CoutConnexion();
        cout.addCout(TypeCout.PRIX, 42.5);
        cout.addCout(TypeCout.TEMPS, 90.0);
        cout.addCout(TypeCout.CO2, 7.3);

        assertEquals(42.5, cout.getCout(TypeCout.PRIX),  1e-9);
        assertEquals(90.0, cout.getCout(TypeCout.TEMPS), 1e-9);
        assertEquals(7.3,  cout.getCout(TypeCout.CO2),   1e-9);
    }

    @Test
    @DisplayName("CoutConnexion : constructeur vide crée une map vide")
    void coutConnexionConstructeurVideFonctionne() {
        CoutConnexion cout = new CoutConnexion();
        assertNull(cout.getCout(TypeCout.PRIX),
            "Une CoutConnexion vide doit retourner null pour un critère non défini.");
    }
}