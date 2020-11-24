import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class IterativeBCO extends BCO {

    public IterativeBCO(Data startPoint, Objective obj, long maxTime, int nbBee, int nc) {
        super(startPoint, obj, maxTime, nbBee, nc);
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


    /**
     * Simule une ruche en suivant le principe du BCO (voir BeeColonyOpt.pdf a la racine du projet)
     */
    @Override
    public void optimize() {
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

            List<Bee> explorateurs = new ArrayList<>();
            List<Bee> suiveurs = new ArrayList<>();

            // Choix de chaque abeille => Continuer ou changer de role
            // Etape 5
            for (Bee bee : bees) {
               bee.choice(bees.indexOf(bee));

               if (!bee.isFollower()){
                   explorateurs.add(bee);
               }
               else {
                   suiveurs.add(bee);
               }
            }
            // Choix pour chaque suiveuse de quel abeille suivre
            // Etape 7
            for (Bee bee : suiveurs) {
                bee.danse(explorateurs);
            }

            // Selection de la meilleure solution partielle
            this.objValue = bees.get(0).objValue;
            this.solution = bees.get(0).solution;
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

        int ITMAX = 2000;  // number of iterations
        int BEESNUMBER = 200;  // number of bees
        int NC = 10;

        creationBitCounter(ITMAX,BEESNUMBER,NC);
        creationFermat(ITMAX,BEESNUMBER,NC);

    }
}
