import java.util.*;

public class ThreadBCO extends BCO{

    private final int nbBee;
    List<Bee> explorateurs = new ArrayList<>();
    List<Bee> suiveurs = new ArrayList<>();

    private final List<BeeThread> listeThread = new ArrayList<>();
    private final int nbThread;


    public ThreadBCO(Data startPoint, Objective obj, long maxTime, int nbBee, int nc, int nbThread) {
        super(startPoint, obj, maxTime, nbBee, nc);
        if (nbThread > 0) {
            this.nbThread = nbThread;
        } else this.nbThread = 1;
        this.nbBee = nbBee;
    }

    @Override
    public void optimize() {
        long startime = System.currentTimeMillis();
        int currentSolution = 0;
        boolean dernierIdentiques = false;

        // Les Abeille arretent de chercher quand le temps maximum est atteints
        // Ou que la solution optimale a été trouvée
        while (System.currentTimeMillis() - startime < this.maxTime && objValue > 0 && !dernierIdentiques) {

            //Parcours des threads, crétion et lancement
            for (int i = 1 ; i <= nbThread ; i++){
                BeeThread thread;thread = new BeeThread(nbBee/nbThread * (i-1), nbBee/nbThread * i, bees);
                listeThread.add(thread);
                thread.start();
            }

            //Attente de la fin d'activité de tous les threads
            boolean stillAlive;
            do {
                stillAlive = false;
                for (int i = 0 ; i < nbThread ; i++){
                    stillAlive = stillAlive || listeThread.get(i).isAlive();
                }
            }
            while (stillAlive);

            listeThread.clear();

            BackToHive();


            this.objValue = bees.get(0).objValue;
            this.solution = bees.get(0).solution;

            // Regarde si les dix dernières solutions sont identiques, si oui arrête la recherche
            solutions[currentSolution] = solution;
            currentSolution++;
            currentSolution %= solutions.length;
            dernierIdentiques = true;

            for (int i = 1; i < solutions.length; i++) {
                if (solutions[i] == null) {
                    dernierIdentiques = false;
                    break;
                }
                dernierIdentiques = dernierIdentiques && (solutions[i-1] == solutions[i]);
            }

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
