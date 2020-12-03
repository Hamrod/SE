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
}
