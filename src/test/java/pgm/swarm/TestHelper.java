package pgm.swarm;

public class TestHelper {
    public double getRandomNumber(double min, double max) {
        return ((Math.random() * (max - min)) + min);
    }
}
