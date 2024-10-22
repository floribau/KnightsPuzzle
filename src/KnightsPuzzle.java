import java.time.Duration;
import java.time.LocalTime;

public class KnightsPuzzle {

  public static void main(String[] args) {
    State bestState = null;
    int bestScore = Integer.MIN_VALUE;
    double sumScore = 0.0;
    int iterations = 100;

    LocalTime startTime = LocalTime.now();
    for (int i = 0; i < iterations; i++) {

      State maximum = LocalSearch.hillClimbing();
      // State maximum = LocalSearch.simulatedAnnealing();
      // State maximum = LocalSearch.beamSearch();

      int score = maximum.score();
      if (score > bestScore) {
        bestState = maximum;
        bestScore = score;
      }
      sumScore += score;
      System.out.println("Completed iteration " + i);
    }
    LocalTime endTime = LocalTime.now();

    System.out.println("\nBest local maximum found:\n" + bestState);
    assert bestState != null;
    System.out.println("Best score: " + bestState.score());
    System.out.println("Average score: " + sumScore / iterations);
    System.out.println("Average time per iteration: " +
        (double) Duration.between(startTime, endTime).toMillis() / iterations + "ms");
  }

}
