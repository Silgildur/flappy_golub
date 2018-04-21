import java.util.Random;

public class PipePair {
    int startX;
    long startTime;
    final static int pipeSpeed = 20;
    final static int pipeWidth = 50;
    int yGapCoorddinate;
    final static int gapHeight = 200;
    boolean isCounted = false;
    final static int promegutokMezhduPipePairs = 600;
    final static Random gapRandomizer = new Random();


    PipePair(int startX, int height) {
        startTime = System.currentTimeMillis();
        yGapCoorddinate = gapRandomizer.nextInt(height - 500) + 250;
        this.startX = startX;
    }

    int calculateXCoordinate() {
        return (int) (startX - pipeSpeed * (System.currentTimeMillis() - startTime) / 60);
    }

}
