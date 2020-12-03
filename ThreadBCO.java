import java.util.*;

public class ThreadBCO extends BCO{

    private final int quart1;
    private final int quart2;
    private final int quart3;
    List<Bee> explorateurs = new ArrayList<>();
    List<Bee> suiveurs = new ArrayList<>();


    public ThreadBCO(Data startPoint, Objective obj, long maxTime, int nbBee, int nc) {
        super(startPoint, obj, maxTime, nbBee, nc);
        this.quart1 = nbBee / 4;
        this.quart2 = nbBee / 2;
        this.quart3 = 3 * nbBee / 4;
    }

    @Override
    public void optimize() {
        long startime = System.currentTimeMillis();

        // Les Abeille arretent de chercher quand le temps maximum est atteints
        // Ou que la solution optimale a été trouvée
        while (System.currentTimeMillis() - startime < this.maxTime && objValue > 0) {

            // Création des Threads
            BeeThread beeThread1 = new BeeThread( 0, quart1,bees);
            BeeThread beeThread2 = new BeeThread( quart1, quart2,bees);
            BeeThread beeThread3 = new BeeThread( quart2, quart3,bees);
            BeeThread beeThread4 = new BeeThread( quart3, bees.size(),bees);

            // Run de tous les threads
            // Début de la recherche pour chaque Abeille
            // Étape 2
            beeThread1.start();
            beeThread2.start();
            beeThread3.start();
            beeThread4.start();

            //Attente de la fin d'activité de tous les threads
            while (beeThread1.isAlive() || beeThread2.isAlive() || beeThread3.isAlive() || beeThread4.isAlive()) {
            }

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

        int ITMAX = 2000;  // number of iterations
        int BEESNUMBER = 100;  // number of bees
        int NC = 10;

        int n = 500;
        Objective obj = new BitCounter(n);
        Data D = obj.solutionSample();
        ThreadBCO bco = new ThreadBCO(D, obj, ITMAX, BEESNUMBER, NC);
        System.out.println(bco);
        System.out.println("starting point : " + bco.getSolution());
        System.out.println("optimizing ...");
        bco.optimize();
        System.out.println(bco);
        System.out.println("solution : " + bco.getSolution());
        System.out.println();

    }

}
