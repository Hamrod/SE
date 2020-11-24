public class Benchmark {

    private static void creationBitCounterIteratif(int itMax,int beeNb,int nc) {
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

    private static void creationBitCounterThreads(int itMax,int beeNb,int nc) {
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

        int ITMAX = 2000;  // number of iterations
        int BEESNUMBER = 200;  // number of bees
        int NC = 10;

        long startTime = System.currentTimeMillis();

        creationBitCounterIteratif(ITMAX,BEESNUMBER,NC);
        
        System.out.println("temps d'exec en séquentiel : "+  (startTime - System.currentTimeMillis()) + " s");

        creationBitCounterThreads(ITMAX,BEESNUMBER,NC);

        startTime = System.currentTimeMillis();
        System.out.println("temps d'exec avec Threads : "+  (startTime - System.currentTimeMillis()) + " s");

    }
}
