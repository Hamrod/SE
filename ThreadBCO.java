import java.util.*;

public class ThreadBCO extends BCO {

    private final int quart1;
    private final int quart2;
    private final int quart3;

    public ThreadBCO(Data startPoint, Objective obj, long maxTime, int nbBee, int nc) {
        super(startPoint, obj, maxTime, nbBee, nc);
        this.quart1 = nbBee / 4;
        this.quart2 = nbBee / 2;
        this.quart3 = 3 * nbBee / 4;
    }

    private int tri(List<Bee> beesList, int begin, int end) {
        int pivot = beesList.size()-1;
        int i = (begin-1);

        for (int j = begin; j < end; j++) {
            if (beesList.get(j).solution.compareTo(beesList.get(pivot).solution) <= 0) {
                i++;

                Bee swapTemp = beesList.get(i);
                beesList.set(i,beesList.get(j));
                beesList.set(j,swapTemp);
            }
        }

        Bee swapTemp = beesList.get(i);
        beesList.set(i, beesList.get(beesList.size()-1));
        beesList.set(beesList.size()-1,  swapTemp);

        return i+1;
    }


    @Override
    public void optimize() {
        long startime = System.currentTimeMillis();

        while (System.currentTimeMillis() - startime < this.maxTime && objValue > 0) {

            Thread threadBee1 = new Thread(() -> {
                for (int i = 0; i < quart1; i++) {
                    bees.get(i).optimize();
                }
            }, "ThreadBee1");

            Thread threadBee2 = new Thread(() -> {
                for (int i = quart1; i < quart2; i++) {
                    bees.get(i).optimize();
                }
            }, "ThreadBee1");

            Thread threadBee3 = new Thread(() -> {
                for (int i = quart2; i < quart3; i++) {
                    bees.get(i).optimize();
                }
            }, "ThreadBee3");

            Thread threadBee4 = new Thread(() -> {
                for (int i = quart3; i < bees.size(); i++) {
                    bees.get(i).optimize();
                }
            }, "ThreadBee4");

            // Run de tous les threads
            threadBee1.start();
            threadBee2.start();
            threadBee3.start();
            threadBee4.start();

            while (threadBee1.isAlive() && threadBee2.isAlive() && threadBee3.isAlive() && threadBee4.isAlive()) {
            }

            Collections.sort(bees, (b1, b2) -> {
                return Double.compare(obj.value(b1.solution), obj.value(b2.solution));
            });

            List<Bee> explorateurs = new ArrayList<>();
            List<Bee> suiveurs = new ArrayList<>();

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
