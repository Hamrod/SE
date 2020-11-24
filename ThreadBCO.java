import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ThreadBCO extends BCO implements Runnable{


    public ThreadBCO(Data startPoint, Objective obj, long maxTime, int nbBee, int nc) {
        super(startPoint, obj, maxTime, nbBee, nc);
    }


    // PROBABLEMENT INUTILE
    @Override
    public void run() {/*
        for (Bee bee : bees) {
            bee.init(bee.solution, maxTime);
            bee.optimize();
        }*/
        System.out.println("test 3");
    }


    @Override
    public void optimize() {
        long startime = System.currentTimeMillis();

        Thread threadBee1 = new Thread(() -> {


            for(int i = 0; i <50; i++){
                bees.get(i).optimize();
            }
            System.out.println("test 1 ");
            /*

            for (Bee bee : bees) {
                bee.init(bee.solution, maxTime);
                bee.optimize();
            }*/
        }, "ThreadBee1");

        Thread threadBee2 = new Thread(() -> {


            for(int i = 50; i <100; i++){
                bees.get(i).optimize();
            }
            System.out.println("test 2 ");
            /*
            for (Bee bee : bees) {
                bee.init(bee.solution, maxTime);
                bee.optimize();
            }*/
        }, "ThreadBee1");

        //Thread thread1 = new Thread(this, "test");
        //thread1.start();
        boolean start = true;


        while (System.currentTimeMillis() - startime < this.maxTime && objValue > 0) {

            if (start){
                threadBee1.start();
                threadBee2.start();
                start = !start;
            } else {
                threadBee1.run();
                threadBee2.run();
            }

            //while (!(threadBee1.getState() != Thread.State.TERMINATED &&  threadBee2.getState() != Thread.State.TERMINATED)){}
            // Run de tous les threads

            Collections.sort(bees, Comparator.comparingDouble(b -> obj.value(b.solution)));

            List<Bee> explorateurs = new ArrayList<>();
            List<Bee> suiveurs = new ArrayList<>();

            for (Bee bee : bees) {
                bee.choice(bees.indexOf(bee));

                if (!bee.isFollower()){
                    explorateurs.add(bee);
                }
                else {
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
