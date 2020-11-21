import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BCO extends binMeta {

    int nc;
    List<Bee> bees;

    public BCO(Data startPoint,Objective obj,long maxTime, int nbBee, int nc) {
        try {
            String msg = "Impossible to init BCO object: ";
            if (maxTime <= 0) throw new Exception(msg + "the maximum execution time is 0 or even negative");
            this.maxTime = maxTime;
            if (nc <= 0) throw new Exception(msg + "the number of constructive moves is 0 or even negative");
            this.nc = nc;
            if (nbBee <= 0) throw new Exception(msg + "the number of bees is 0 or even negative");
            if (startPoint == null) throw new Exception(msg + "the reference to the starting point is null");
            this.solution = startPoint;
            if (obj == null) throw new Exception(msg + "the reference to the objective is null");
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

    /**
     * Simule une ruche en suivant le principe du BCO (voir BeeColonyOpt.pdf a la racine du projet)
     */
    @Override
    public void optimize() {
        System.out.println(bees);
        long startime = System.currentTimeMillis();

        // Les Abeille arretent de chercher quand le temps maximum est atteints
        // Ou que la solution optimale a été trouvée
        while (System.currentTimeMillis() - startime < this.maxTime && objValue > 0) {

            // Début de la recherche pour chaque Abeille
            // Étape 2
            for (Bee bee : bees) {
                bee.init(bee.solution, maxTime);
                bee.optimize();
            }

            // Tri des Abeilles suivant leurs performances
            // Etape 4
            Collections.sort(bees, (b1, b2) -> {
                return Double.compare(obj.value(b1.solution), obj.value(b2.solution));
            });
            System.out.println(bees);

            // Selction de la meilleure solution partielle
            this.objValue = bees.get(0).objValue;
            this.solution = bees.get(0).solution;
        }
    }

    // main
    public static void main(String[] args) {
        int ITMAX = 10000;  // number of iterations
        int BEESNUMBER = 200;  // number of bees
        int NC = 10;

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
