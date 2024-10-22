import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class LocalSearch {

  final static int MAX_SIDEWAYS_STEPS = 100;
  final static int INITIAL_TEMP = 1000;
  final static int BEAM_WIDTH = 3;
  final static double ALPHA = 0.99;
  final static double T_MIN = 0.01;
  final static double ACCEPTANCE_FACTOR = 0.8;

  /**
   * Implementation of hill climbing with sideways stepping
   * @return the state at a local maximum, ideally global maximum
   */
  public static State hillClimbing() {
    State current = new State();
    int iteration = 1;
    int sidewaysSteps = 0;
    while (true) {
      HashSet<State> neighbors = getNeighbors(current);
      State bestNeighbor = getBestState(neighbors);
      int currentScore = current.score();
      int bestNeighborScore = bestNeighbor.score();
      if (bestNeighborScore > currentScore) {
        // best neighbor is better than current score
        current = bestNeighbor;
        sidewaysSteps = 0;
      } else if (bestNeighborScore == currentScore && sidewaysSteps < MAX_SIDEWAYS_STEPS) {
        // allow sideways stepping if maximum not yet reached
        current = bestNeighbor;
        sidewaysSteps += 1;
      } else {
        // no better state and no sideways stepping possible
        return current;
      }
      if (iteration > 1000) {
        System.out.println("1000 iterations reached");
        return current;
      }
      iteration++;
    }
  }

  /**
   * Implementation of simulated annealing
   * @return the state at a local maximum, ideally global maximum
   */
  public static State simulatedAnnealing() {
    State current = new State();
    int round = 1;
    int noImprovementCounter = 0;
    while (true) {
      double t = cooling(round);
      if (t < T_MIN) {
        // System.out.println("Finished in round " + round);
        return current;
      }
      State next = getRandomNeighbor(current);
      double delta = next.score() - current.score();
      if (delta > 0) {
        // next state better than current state
        current = next;
        noImprovementCounter = 0;
      } else {
        // no improvement
        /*
        noImprovementCounter ++;
        if (noImprovementCounter > 50) {
          // reheating if no improvement is achieved for several
          round = 1;
        }
        */
        double p = Math.random();
        if (p < acceptanceProbability(delta, t)) {
          // accept some worse states by chance
          current = next;
        }
      }
      round++;
    }
  }

  /**
   * Implementation of beam search
   * @return the state at a local maximum, ideally global maximum
   */
  public static State beamSearch() {
    List<State> states = new ArrayList<>();
    for (int i = 0; i < BEAM_WIDTH; i++) {
      states.add(new State());
    }

    int bestScore = Integer.MIN_VALUE;
    int noImprovementCounter = 0;
    while (true) {
      if (noImprovementCounter > 50) {
        // terminate if no improvement over 50 iterations
        return states.get(0);
      }
      HashSet<State> neighbors = new HashSet<>();
      for (State state : states) {
        neighbors.addAll(getNeighbors(state));
      }
      ArrayList<State> list = new ArrayList<>(neighbors);
      list.sort((o1, o2) -> Double.compare(o2.score(), o1.score()));
      states = list.subList(0, BEAM_WIDTH);

      int oldBestScore = bestScore;
      bestScore = states.get(0).score();
      if (bestScore > oldBestScore) {
        noImprovementCounter = 0;
      } else {
        noImprovementCounter++;
      }
    }


  }

  /**
   * Calculates all neighbors for a given state.
   * A neighbor is defined as a state that has the entry at one field changed compared to the current state
   * @param state the state of which the neighbors should be retrieved
   * @return HashSet with all neighbors
   */
  public static HashSet<State> getNeighbors(State state) {
    HashSet<State> neighbors = new HashSet<>();
    char[][] board = state.getBoard();
    for (int i = 0; i < board.length; i++) {
      for (int j = 0; j < board[0].length; j++) {
        State newStateA = new State(state);
        State newStateB = new State(state);
        switch (board[i][j]) {
          case 'B' -> {
            newStateA.getBoard()[i][j] = 'W';
            newStateB.getBoard()[i][j] = '-';
          }
          case 'W' -> {
            newStateA.getBoard()[i][j] = 'B';
            newStateB.getBoard()[i][j] = '-';
          }
          case '-' -> {
            newStateA.getBoard()[i][j] = 'B';
            newStateB.getBoard()[i][j] = 'W';
          }
        }
        neighbors.add(newStateA);
        neighbors.add(newStateB);
      }
    }
    return neighbors;
  }

  /**
   * Returns a random State object from the neighbors list
   * @param state the state of which the random neighbor should be retrieved
   * @return a random neighbor State
   */
  public static State getRandomNeighbor(State state) {
    ArrayList<State> neighbors = new ArrayList<>(getNeighbors(state));
    return neighbors.get((int) (Math.random() * neighbors.size()));
  }

  /**
   * Returns the State in a given Set that has the best score value
   * @param states the HashSet of States
   * @return the State object with the best score value
   */
  public static State getBestState(HashSet<State> states) {
    List<State> list = new ArrayList<>(states);
    list.sort((o1, o2) -> Double.compare(o2.score(), o1.score()));
    return list.get(0);
  }

  /**
   * Implements the cooling schedule for simulated annealing
   * @param k specifies the round
   * @return temperature as double
   */
  public static double cooling(int k) {
    // exponential decay
    return INITIAL_TEMP * Math.pow(ALPHA, k);
  }

  /**
   * Implements the acceptance probability for simulated annealing
   * and uses the metropolis acceptance criterion
   * @param delta the delta between current state score and best next state score
   * @param temperature temperature calculated by cooling schedule
   * @return probability as double
   */
  public static double acceptanceProbability(double delta, double temperature) {
    // modified metropolis acceptance criterion
    return Math.exp(-delta / Math.pow(temperature, ACCEPTANCE_FACTOR));
  }
}
