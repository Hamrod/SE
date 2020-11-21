import java.util.Random;

public class Bee extends binMeta {

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

    @Override
    public void optimize() {
        Random R = new Random();
        Data D = new Data(this.solution);
        long startime = System.currentTimeMillis();
        int iteration = 0;

        // main loop
        while ((System.currentTimeMillis() - startime < this.maxTime) || iteration < nc)
        {
            // the random walker can walk in a neighbourhood of D
            // (Hamming distance is randomly selected among 1, 2 and 3)
            int h = 1 + R.nextInt(3);

            // generating a new solution in the neighbour of D with Hamming distance h
            Data newD = D.randomSelectInNeighbour(h);

            // evaluating the quality of the generated solution
            double value = obj.value(newD);
            if (this.objValue > value)
            {
                this.objValue = value;
                this.solution = new Data(newD);
            }

            // the walk continues from the new generated solution
            D = newD;
            iteration++;
        }
    }
}
