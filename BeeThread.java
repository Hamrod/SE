import java.util.List;

public class BeeThread extends Thread implements Runnable{
    int startingPoint;
    int endingPoint;
    List<Bee> bees;

    public BeeThread(int startingPoint, int endingPoint, List<Bee> bees){
        this.startingPoint = startingPoint;
        this.endingPoint = endingPoint;
        this.bees = bees;
    }


    @Override
    public void run() {
        for (int i = startingPoint; i < endingPoint; i++) {
            bees.get(i).optimize();
        }
    }
}
