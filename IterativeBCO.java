import java.util.Arrays;
import java.util.Collections;

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
        int currentSolution = 0;
        boolean dernierIdentiques = false;

        // Les Abeille arretent de chercher quand le temps maximum est atteints
        // Ou que la solution optimale a été trouvée
        while (System.currentTimeMillis() - startime < this.maxTime && objValue > 0 && !dernierIdentiques) {

            // Début de la recherche pour chaque Abeille
            // Étape 2
            for (Bee bee : bees) {
                bee.init(bee.solution, maxTime);
                bee.optimize();
            }

            BackToHive();

            // Selection de la meilleure solution partielle
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

    private static void creationBitCounter(int itMax,int beeNb,int nc) {
        int n = 500;
        Objective obj = new BitCounter(n);
        Data D = obj.solutionSample();
        IterativeBCO bco = new IterativeBCO(D, obj, itMax, beeNb, nc);
        System.out.println(bco);
        System.out.println("starting point : " + bco.getSolution());
        System.out.println("optimizing ...");
        bco.optimize();
        System.out.println(bco);
        System.out.println("solution : " + bco.getSolution());
        System.out.println();
    }
    
    private static void creationFermat(int itMax,int beeNb,int nc) {
        int exp = 2;
        int ndigits = 10;
        Objective obj = new Fermat(exp,ndigits);
        Data D = obj.solutionSample();
        IterativeBCO bco = new IterativeBCO(D, obj, itMax, beeNb, nc);
        System.out.println(bco);
        System.out.println("starting point : " + bco.getSolution());
        System.out.println("optimizing ...");
        bco.optimize();
        System.out.println(bco);
        System.out.println("solution : " + bco.getSolution());
        Data x = new Data(bco.solution,0,ndigits-1);
        Data y = new Data(bco.solution,ndigits,2*ndigits-1);
        Data z = new Data(bco.solution,2*ndigits,3*ndigits-1);
        System.out.print("equivalent to the equation : " + x.posLongValue() + "^" + exp + " + " + y.posLongValue() + "^" + exp);
        if (bco.objValue == 0.0)
            System.out.print(" == ");
        else
            System.out.print(" ?= ");
        System.out.println(z.posLongValue() + "^" + exp);
        System.out.println();
    }


    public static void main(String[] args) {

        int ITMAX = 20000;  // number of iterations
        int BEESNUMBER = 200;  // number of bees
        int NC = 10;

        creationBitCounter(ITMAX,BEESNUMBER,NC);
        //creationFermat(ITMAX,BEESNUMBER,NC);
    }
}
