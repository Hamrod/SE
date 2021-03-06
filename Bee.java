import java.util.List;
import java.util.Random;

 public class Bee extends binMeta {

    // Nombre d'iteration maximum d'un agent
    private int nc;

    public Bee(Data startPoint,Objective obj,long maxTime, int nc)
    {
        try
        {
            String msg = "Impossible to create Bee object: ";
            if (maxTime <= 0) throw new Exception(msg + "the maximum execution time is 0 or even negative");
            this.maxTime = maxTime;
            if (nc <= 0) throw new Exception(msg + "the number of constructive moves is 0 or even negative");
            this.nc = nc;
            if (startPoint == null) throw new Exception(msg + "the reference to the starting point is null");
            this.solution = startPoint;
            if (obj == null) throw new Exception(msg + "the reference to the objective is null");
            this.obj = obj;
            this.objValue = this.obj.value(this.solution);
            this.metaName = "Bee";
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.exit(1);
        }
    }


    public void init(Data startPoint, long maxTime) {
        try
        {
            String msg = "Impossible to init Bee object: ";
            if (maxTime <= 0) throw new Exception(msg + "the maximum execution time is 0 or even negative");
            this.maxTime = maxTime;
            if (startPoint == null) throw new Exception(msg + "the reference to the starting point is null");
            this.solution = startPoint;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Fonction de recherche d'une Abeille, inspiré du modele de RandomWalk.
     * Incrémente intération a chaque passage pour repecter le nombre de mouvement constructif lors d'une recherche ( comme décrit dans l'algo)
     *
     */
    @Override
    public void optimize() {
        Random R = new Random();
        Data D = new Data(this.solution);
        long startime = System.currentTimeMillis();
        int iteration = 0;


        while ((System.currentTimeMillis() - startime < this.maxTime) && iteration < nc && objValue > 0)
        {

            int h = 1 + R.nextInt(3);
            Data newD = D.randomSelectInNeighbour(h);
            double value = obj.value(newD);
            if (this.objValue > value)
            {
                this.objValue = value;
                this.solution = new Data(newD);
            }
            D = newD;
            iteration++;
        }
    }
 }
