import java.util.List;
import java.util.Random;

public class Bee extends binMeta {

    // Nombre d'iteration maximum d'un agent
    private int nc;

    private boolean follower;

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

    public boolean isFollower() {
        return follower;
    }

    /**
     * Détermine qui une abeille suiveuse va suivre
     *
     * @param explorateurs
     */
    public void danse(List<Bee> explorateurs) {
        //donne l'indice de la bee a suivre
        int index = (int) (Math.random() * explorateurs.size()*0.40);
        System.out.println(index);

        Bee explor = explorateurs.get(index);
        this.solution = explor.getSolution();

    }

    /**
     * Détermine si une abeille change d'état (suiveuse / exploratrice) suivant son rang par rapport aux autres.
     * Une abeille ayant de mauvaise performances a plsu de chance de devenir une suiveuse
     *
     * @param index rang de l'abeille par rapport aux autres
     */
    public void choice(int index) {
        double R = Math.random();
        double seuil = 1 - index * 0.9;
        //Le seuil permet de gerer le pourcentage de chance qu'une abeille a de suivre/explorer
        // la courbe est volontairement raide ce qui permet que les meilleurs explorateurs aient plus de chance de continuer
        // contrairement aux pires qui ont plus de chance de se mettre a suivre

        if (R > seuil) {
            follower = !follower;
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
