public class BCO extends binMeta {

    int nbBee;
    int nbMoveBee;

    public BCO(Data startPoint,Objective obj,long maxTime) {
        try {
            String msg = "Impossible de créer l'objet BCO : ";
            if (maxTime <= 0) throw new Exception(msg + "le temps d'exec mac est inf a 0");
            this.maxTime = maxTime;
            if (startPoint == null) throw new Exception(msg + "le StartPoint est null");
            this.solution = startPoint;
            if (obj == null) throw new Exception(msg + "l'objectif est null");
            this.obj = obj;
            this.objValue = this.obj.value(this.solution);
            this.metaName = "BCO";
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    @Override
    public void optimize() {

    }
}
