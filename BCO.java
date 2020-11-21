import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BCO extends binMeta {

    int nc;
    List<Bee> bees;

    public BCO(Data startPoint,Objective obj,long maxTime, int nbBee, int nc) {
        try {
            String msg = "Impossible de créer l'objet BCO : ";
            if (maxTime <= 0) throw new Exception(msg + "le temps d'exec mac est inf a 0");
            this.maxTime = maxTime;
            if (nc <= 0) throw new Exception(msg + "le nc est inf a 0");
            this.nc = nc;
            if (nbBee <= 0) throw new Exception(msg + "le nbBee est inf a 0");
            if (startPoint == null) throw new Exception(msg + "le StartPoint est null");
            this.solution = startPoint;
            if (obj == null) throw new Exception(msg + "l'objectif est null");
            this.obj = obj;
            this.objValue = this.obj.value(this.solution);
            this.metaName = "BCO";

            bees = new ArrayList<>();
            for (int i = 0 ; i < nbBee ; i++) {
                bees.add(new Bee(startPoint, obj, maxTime, nc));
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    @Override
    public void optimize() {
        System.out.println(bees);
        for (Bee bee : bees) {
            bee.optimize();
        }
        Collections.sort(bees, (b1, b2) -> {
            return Double.compare(obj.value(b1.solution), obj.value(b2.solution));
        });
        System.out.println(bees);
        this.solution = bees.get(0).solution;
    }

    // main
    public static void main(String[] args) {
        int ITMAX = 1000;  // number of iterations
        int BEESNUMBER = 100;  // number of bees
        int NC = 5;

        // BitCounter
        int n = 50;
        Objective obj = new BitCounter(n);
        Data D = obj.solutionSample();
        BCO bco = new BCO(D, obj, ITMAX, BEESNUMBER, NC);
        System.out.println(bco);
        System.out.println("starting point : " + bco.getSolution());
        System.out.println("optimizing ...");
        bco.optimize();
        System.out.println(bco);
        System.out.println("solution : " + bco.getSolution());
        System.out.println();
    }
}
