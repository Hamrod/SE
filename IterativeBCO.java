public class IterativeBCO extends BCO {

    public IterativeBCO(Data startPoint, Objective obj, long maxTime, int nbBee, int nc) {
        super(startPoint, obj, maxTime, nbBee, nc);
    }

    /**
     * Simule une ruche en suivant le principe du BCO (voir BeeColonyOpt.pdf a la racine du projet)
     */
    @Override
    public void optimize() {
        long startime = System.currentTimeMillis();

        // Les Abeille arretent de chercher quand le temps maximum est atteint
        // Ou que la solution optimale a été trouvée
        while (System.currentTimeMillis() - startime < this.maxTime && objValue > 0) {

            // Début de la recherche pour chaque Abeille
            // Étape 2
            for (Bee bee : bees) {
                bee.init(bee.solution, maxTime);
                bee.optimize();
            }

            // Début étape 3
            BackToHive();

            // Étape 8
            // Selection de la meilleure solution partielle
            this.objValue = bees.get(0).objValue;
            this.solution = bees.get(0).solution;
        }
    }

    public static void main(String[] args) {

        int ITMAX = 20000;  // number of iterations
        int BEESNUMBER = 200;  // number of bees
        int NC = 10;

        int n = 500;
        Objective obj = new BitCounter(n);
        Data D = obj.solutionSample();
        IterativeBCO bco = new IterativeBCO(D, obj, ITMAX, BEESNUMBER, NC);
        System.out.println(bco);
        System.out.println("starting point : " + bco.getSolution());
        System.out.println("optimizing ...");
        bco.optimize();
        System.out.println(bco);
        System.out.println("solution : " + bco.getSolution());
        System.out.println();
    }
}
