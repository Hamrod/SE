public class Benchmark {

    private static void creationBitCounterBench(int maxTime, int beeNb, int nc, boolean trace) {
        long startTime = System.currentTimeMillis();

        int n = 1000;
        Objective obj = new BitCounter(n);
        Data D = obj.solutionSample();
        IterativeBCO bcoIteratif = new IterativeBCO(D, obj, maxTime, beeNb, nc);
        if (trace) {
            System.out.println(bcoIteratif);
            System.out.println("starting point : " + bcoIteratif.getSolution());
            System.out.println("Optimizing It...");
        }
        bcoIteratif.optimize();
        if (trace) {
            System.out.println(bcoIteratif);
            System.out.println("solution : " + bcoIteratif.getSolution());
            System.out.println();
        }

        System.out.println("Temps d'exec en séquentiel : " + (System.currentTimeMillis() - startTime) + " ms");
        startTime = System.currentTimeMillis();

        ThreadBCO bcoThread = new ThreadBCO(D, obj, maxTime, beeNb, nc);
        if (trace) {
            System.out.println(bcoThread);
            System.out.println("starting point : " + bcoIteratif.getSolution());
            System.out.println("Optimizing with Threads...");
        }

        bcoThread.optimize();
        if (trace) {
            System.out.println(bcoThread);
            System.out.println("solution : " + bcoThread.getSolution());
            System.out.println();
        }
        System.out.println("Temps d'exec avec Threads : " + (System.currentTimeMillis() - startTime) + " ms");
    }

    private static void creationFermatBench(int maxTime, int beeNb, int nc, boolean trace) {
        long startTime = System.currentTimeMillis();

        int exp = 2;
        int ndigits = 10;
        Objective obj = new Fermat(exp, ndigits);
        Data D = obj.solutionSample();
        IterativeBCO bcoIteratif = new IterativeBCO(D, obj, maxTime, beeNb, nc);
        if (trace) {
            System.out.println(bcoIteratif);
            System.out.println("starting point : " + bcoIteratif.getSolution());
            System.out.println("Optimizing It...");
        }

        bcoIteratif.optimize();
        if (trace) {
            System.out.println(bcoIteratif);
            System.out.println("solution : " + bcoIteratif.getSolution());
        }
        Data x = new Data(bcoIteratif.solution, 0, ndigits - 1);
        Data y = new Data(bcoIteratif.solution, ndigits, 2 * ndigits - 1);
        Data z = new Data(bcoIteratif.solution, 2 * ndigits, 3 * ndigits - 1);
        if (trace) {
            System.out.print("equivalent to the equation : " + x.posLongValue() + "^" + exp + " + " + y.posLongValue() + "^" + exp);
            if (bcoIteratif.objValue == 0.0)
                System.out.print(" == ");
            else
                System.out.print(" ?= ");
            System.out.println(z.posLongValue() + "^" + exp);
        }
        System.out.println("Temps d'exec en séquentiel : " + (System.currentTimeMillis() - startTime) + " ms");
        startTime = System.currentTimeMillis();

        ThreadBCO bcoThread = new ThreadBCO(D, obj, maxTime, beeNb, nc);
        if (trace) {
            System.out.println(bcoThread);
            System.out.println("starting point : " + bcoThread.getSolution());
            System.out.println("Optimizing with Threads...");
        }

        bcoThread.optimize();
        if (trace) {
            System.out.println(bcoThread);
            System.out.println("solution : " + bcoThread.getSolution());
        }
        Data x1 = new Data(bcoThread.solution, 0, ndigits - 1);
        Data y1 = new Data(bcoThread.solution, ndigits, 2 * ndigits - 1);
        Data z1 = new Data(bcoThread.solution, 2 * ndigits, 3 * ndigits - 1);
        if (trace) {
            System.out.print("equivalent to the equation : " + x1.posLongValue() + "^" + exp + " + " + y1.posLongValue() + "^" + exp);
            if (bcoThread.objValue == 0.0)
                System.out.print(" == ");
            else
                System.out.print(" ?= ");
            System.out.println(z1.posLongValue() + "^" + exp);
        }

        System.out.println("Temps d'exec avec Threads : " + (System.currentTimeMillis() - startTime) + " ms");

    }

    private static void creationColortBench(int maxTime, int beeNb, int nc, boolean trace) {
        long startTime = System.currentTimeMillis();
        int n = 4;
        int m = 14;
        ColorPartition cp = new ColorPartition(n, m);
        Data D = cp.solutionSample();
        IterativeBCO bcoIteratif = new IterativeBCO(D, cp, maxTime, beeNb, nc);
        if (trace) {
            System.out.println(bcoIteratif);
            System.out.println("starting point : " + bcoIteratif.getSolution());
            System.out.println("optimizing ...");
        }
        bcoIteratif.optimize();
        if (trace) {
            System.out.println(bcoIteratif);
            System.out.println("solution : " + bcoIteratif.getSolution());
            cp.value(bcoIteratif.solution);
            System.out.println("corresponding to the matrix :\n" + cp.show());
        }

        System.out.println("Temps d'exec en séquentiel : " + (System.currentTimeMillis() - startTime) + " ms");
        startTime = System.currentTimeMillis();


        ThreadBCO bcoThread = new ThreadBCO(D, cp, maxTime, beeNb, nc);
        if (trace) {
            System.out.println(bcoThread);
            System.out.println("starting point : " + bcoThread.getSolution());
            System.out.println("optimizing ...");
        }
        bcoThread.optimize();
        if (trace) {
            System.out.println(bcoThread);
            System.out.println("solution : " + bcoThread.getSolution());
            cp.value(bcoThread.solution);
            System.out.println("corresponding to the matrix :\n" + cp.show());
        }
        System.out.println("Temps d'exec avec Threads : " + (System.currentTimeMillis() - startTime) + " ms");


    }


    public static void main(String[] args) {

        int maxTime = 120000;  // number of iterations
        int BEESNUMBER = 100;  // number of bees
        int NC = 10;
        System.out.println("Début du benchmark... \n");
        creationBitCounterBench(maxTime, BEESNUMBER, NC, false);
        creationFermatBench(maxTime, BEESNUMBER, NC, false);
        creationColortBench(maxTime, BEESNUMBER, NC, false);
        System.out.println("Fin du benchmark... \n");
    }
}
