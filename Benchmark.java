public class Benchmark {

    private static void creationBitCounterIteratif(int itMax, int beeNb, int nc, boolean trace) {
        int n = 1000;
        Objective obj = new BitCounter(n);
        Data D = obj.solutionSample();
        IterativeBCO bco = new IterativeBCO(D, obj, itMax, beeNb, nc);
        if (trace) {
            System.out.println(bco);
            System.out.println("starting point : " + bco.getSolution());
            System.out.println("optimizing ...");
        }
        bco.optimize();
        if (trace) {
            System.out.println(bco);
            System.out.println("solution : " + bco.getSolution());
            System.out.println();
        }
    }

    private static void creationBitCounterThreads(int maxTime, int beeNb, int nc, boolean trace) {
        int n = 1000;
        Objective obj = new BitCounter(n);
        Data D = obj.solutionSample();
        IterativeBCO bco = new IterativeBCO(D, obj, maxTime, beeNb, nc);
        if (trace) {
            System.out.println(bco);
            System.out.println("starting point : " + bco.getSolution());
            System.out.println("optimizing ...");
        }
        bco.optimize();
        if (trace) {
            System.out.println(bco);
            System.out.println("solution : " + bco.getSolution());
            System.out.println();
        }
    }

    private static void creationFermat(int maxTime, int beeNb, int nc) {
        int exp = 2;
        int ndigits = 10;
        Objective obj = new Fermat(exp, ndigits);
        Data D = obj.solutionSample();
        IterativeBCO bco = new IterativeBCO(D, obj, maxTime, beeNb, nc);
        System.out.println(bco);
        System.out.println("starting point : " + bco.getSolution());
        System.out.println("optimizing ...");
        bco.optimize();
        System.out.println(bco);
        System.out.println("solution : " + bco.getSolution());
        Data x = new Data(bco.solution, 0, ndigits - 1);
        Data y = new Data(bco.solution, ndigits, 2 * ndigits - 1);
        Data z = new Data(bco.solution, 2 * ndigits, 3 * ndigits - 1);
        System.out.print("equivalent to the equation : " + x.posLongValue() + "^" + exp + " + " + y.posLongValue() + "^" + exp);
        if (bco.objValue == 0.0)
            System.out.print(" == ");
        else
            System.out.print(" ?= ");
        System.out.println(z.posLongValue() + "^" + exp);
        System.out.println();
    }


    public static void main(String[] args) {

        int maxTime = 60000;  // number of iterations
        int BEESNUMBER = 100;  // number of bees
        int NC = 10;
        boolean trace = true; // activer ou non les détails d'execution

        long startTime = System.currentTimeMillis();

        creationBitCounterIteratif(maxTime, BEESNUMBER, NC, trace);
        System.out.println("Temps d'exec en séquentiel : " + (System.currentTimeMillis() - startTime) + " ms");

        startTime = System.currentTimeMillis();
        creationBitCounterThreads(maxTime, BEESNUMBER, NC, trace);

        System.out.println("Temps d'exec avec Threads : " + (System.currentTimeMillis() - startTime) + " ms");

    }
}
