import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class IterativeBCO extends BCO {

    public IterativeBCO(Data startPoint, Objective obj, long maxTime, int nbBee, int nc) {
        super(startPoint, obj, maxTime, nbBee, nc);
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

            List<Bee> explorateurs = new ArrayList<Bee>();
            List<Bee> suiveurs = new ArrayList<Bee>();
            //System.out.println(bees);
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


            // Selection de la meilleure solution partielle
            this.objValue = bees.get(0).objValue;
            this.solution = bees.get(0).solution;
        }
    }

    // main
    public static void main(String[] args) {
        int ITMAX = 2000;  // number of iterations
        int BEESNUMBER = 200;  // number of bees
        int NC = 10;

        // BitCounter
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
