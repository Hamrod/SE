import java.util.*;

public class ThreadBCO extends BCO{

    private final int nbBee;
    List<Bee> explorateurs = new ArrayList<>();
    List<Bee> suiveurs = new ArrayList<>();

    private List<BeeThread> omarEtFred = new ArrayList<>();
    private int fredEtJamy;
    private BeeThread fredEtGeorges;

    public ThreadBCO(Data startPoint, Objective obj, long maxTime, int nbBee, int nc, int nbThread) {
        super(startPoint, obj, maxTime, nbBee, nc);
        if (nbThread > 0) {
            fredEtJamy = nbThread;
        } else fredEtJamy = 1;
        this.nbBee = nbBee;
    }

    @Override
    public void optimize() {
        long startime = System.currentTimeMillis();

        // Les Abeille arretent de chercher quand le temps maximum est atteints
        // Ou que la solution optimale a été trouvée
        while (System.currentTimeMillis() - startime < this.maxTime && objValue > 0) {

            for (int i = 1 ; i <= fredEtJamy ; i++){
                fredEtGeorges = new BeeThread(nbBee/fredEtJamy * (i-1), nbBee/fredEtJamy * i, bees);
                omarEtFred.add(fredEtGeorges);
                fredEtGeorges.start();
            }

            boolean stillAlive;
            //Attente de la fin d'activité de tous les threads
            do {
                stillAlive = false;
                for (int i = 0 ; i < fredEtJamy ; i++){
                    stillAlive = stillAlive || omarEtFred.get(i).isAlive();
                }
            }
            while (stillAlive);

            omarEtFred.clear();

            // Tri des abeilles selon leur résultat
            // Etape 4
            Collections.sort(bees, (b1, b2) -> {
                return Double.compare(obj.value(b1.solution), obj.value(b2.solution));
            });

            explorateurs.clear();
            suiveurs.clear();

            // Choix de chaque abeille => Continuer ou changer de role
            // Etape 5
            for (Bee bee : bees) {
                bee.choice(bees.indexOf(bee));

                if (!bee.isFollower()) {
                    explorateurs.add(bee);
                } else {
                    suiveurs.add(bee);
                }
            }

            for (Bee bee : suiveurs) {
                bee.danse(explorateurs);
            }

            this.objValue = bees.get(0).objValue;
            this.solution = bees.get(0).solution;


        }
    }

    public static void main(String[] args) {

        int ITMAX = 60000;  // number of iterations
        int BEESNUMBER = 100;  // number of bees
        int NC = 10;

        int n = 5000;
        Objective obj = new BitCounter(n);
        Data D = obj.solutionSample();
        ThreadBCO bco = new ThreadBCO(D, obj, ITMAX, BEESNUMBER, NC, 10);
        System.out.println(bco);
        System.out.println("starting point : " + bco.getSolution());
        System.out.println("optimizing ...");
        bco.optimize();
        System.out.println(bco);
        System.out.println("solution : " + bco.getSolution());
        System.out.println();

    }

}
