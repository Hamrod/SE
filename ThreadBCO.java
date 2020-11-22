import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ThreadBCO extends BCO implements Runnable{


    public ThreadBCO(Data startPoint, Objective obj, long maxTime, int nbBee, int nc) {
        super(startPoint, obj, maxTime, nbBee, nc);
    }

    @Override
    public void run() {
        for (Bee bee : bees) {
            bee.init(bee.solution, maxTime);
            bee.optimize();
        }
    }


    @Override
    public void optimize() {
        long startime = System.currentTimeMillis();

        Thread threadBee1 = new Thread(() -> {
            System.out.println("test");

            for (Bee bee : bees) {
                bee.init(bee.solution, maxTime);
                bee.optimize();
            }
        }, "ThreadBee1");


        Thread thread1 = new Thread(this, "test");
        thread1.start();

        while (System.currentTimeMillis() - startime < this.maxTime && objValue > 0) {

            thread1.run();
            // Run de tous les threads

            Collections.sort(bees, (b1, b2) -> {
                return Double.compare(obj.value(b1.solution), obj.value(b2.solution));
            });

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

        //int ITMAX = 2000;  // number of iterations
        //int BEESNUMBER = 100;  // number of bees
        //int NC = 10;

    }

}
