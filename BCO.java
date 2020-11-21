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

    }
}
