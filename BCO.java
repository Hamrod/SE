import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class BCO extends binMeta {

    int nc;
    List<Bee> bees;
    List<Bee> explorateurs = new ArrayList<>();
    List<Bee> suiveurs = new ArrayList<>();

    public BCO(Data startPoint, Objective obj, long maxTime, int nbBee, int nc) {
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
     * Trie la liste des abeilles selon leurs performances
     * Détermine si une abeille devient "recruteuse" ou "suiveuse" selon sa place dans la liste d'abeilles
     * Détermine quelle "recruteuse" une "suiveuse" va suivre
     */
    void BackToHive() {
        Collections.sort(bees, (b1, b2) -> {
            return Double.compare(obj.value(b1.solution), obj.value(b2.solution));
        });

        List<Bee> suiveuses = new ArrayList<>();
        List<Bee> recruteuse = new ArrayList<>();

        for (Bee bee : bees) {
            double R = Math.random() * bees.size();
            double seuil = bees.size() - (bees.indexOf(bee) + 1) * 0.9;
            //Le seuil permet de gerer le pourcentage de chance qu'une abeille a de suivre/recruter
            // la courbe est volontairement raide ce qui permet que les meilleurs exploratrices aient plus de chance de continuer
            // contrairement aux pires qui ont plus de chance de se mettre a suivre

            if (R > seuil) {
                suiveuses.add(bee);
            } else {
                recruteuse.add(bee);
            }
        }

        for (Bee suiveuse : suiveuses) {
            boolean choisi = false;
            while (!choisi) {
                double seuil = 0;
                double max = recruteuse.size();
                double R = Math.random() * recruteuse.size();
                for (int i = 1; i <= 100; i++) {
                    seuil += max * (0.5 / i);
                    max = 100 - seuil;
                    if (R < seuil) {
                        suiveuse.solution = recruteuse.get(i).getSolution();
                        suiveuse.objValue = recruteuse.get(i).getObjVal();
                        choisi = true;
                        break;
                    }
                }
            }
        }
    }

}
