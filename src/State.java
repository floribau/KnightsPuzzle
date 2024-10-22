import java.util.Arrays;

public class State {
    private final char[][] board;

  /**
   * Creates a new State object with random board init
   */
  public State() {
      // random state constructor
      board = new char[8][8];
      for (int i=0; i<board.length; i++) {
        for(int j=0; j<board[0].length; j++) {
          char[] pieces = {'B', 'W', '-'};
          board[i][j] = pieces[(int) (Math.random() * 3)];
        }
      }
    }

  /**
   * Creates a new State object and copies the given state's board
   * @param state the State to copy the board from
   */
  public State(State state) {
      // copy constructor
      board = new char[8][8];
      for (int i=0; i<board.length; i++) {
        System.arraycopy(state.board[i], 0, board[i], 0, board[0].length);
      }
    }

  /**
   * 1) Each black knight increases the score by 3, each white knight by 2.<br>
   * 2) Each pair of neighboring knights of different colors increases the score by 2.<br>
   * 3) Each knight increases the score by the sum of its row and column indices.<br>
   * 4) Each pair of knights that can capture each other (no matter the color) decrease the score by 3.<br>
   * @return score value
   */
  public int score() {
      int score = 0;
      for (int i=0; i<board.length; i++) {
        for(int j=0; j<board[0].length; j++) {
          char piece = board[i][j];
          if (piece == 'B' || piece == 'W') {
            if (piece == 'B') {
              // black knights
              score += 3;
              // neighboring pairs of knights of different colors
              if (i + 1 < 8 && board[i + 1][j] == 'W') {
                score += 1;
              }
              if (j + 1 < 8 && board[i][j + 1] == 'W') {
                score += 1;
              }
            }

            else {
              // white knights
              score += 2;
              // neighboring pairs of knights of different colors
              if (i + 1 < 8 && board[i + 1][j] == 'B') {
                score += 1;
              }
              if (j + 1 < 8 && board[i][j + 1] == 'B') {
                score += 1;
              }
            }
            // sum of indices of each knight
            score += i + j;
            // pairs of knights that can capture each other
            int[][] offsets = {{-2,1}, {-1,2},{1,2}, {2,1}};
            for (int[] offset : offsets) {
              int iCapture = i + offset[0];
              int jCapture = j + offset[1];
              if (iCapture >= 0 && iCapture < 8 && jCapture < 8 &&
                  (board[iCapture][jCapture] == 'B' || board[iCapture][jCapture] == 'W')) {
                score -= 3;
              }
            }
          }
        }
      }
      return score;
    }

  public char[][] getBoard() {
    return board;
  }

  public String toString() {
    StringBuilder res = new StringBuilder();
    for(char[] row : board) {
      for (char field : row) {
        res.append("[").append(field).append("]");
      }
      res.append("\n");
    }
    return res.toString().strip();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    State state = (State) o;
    return Arrays.deepEquals(board, state.board);
  }

  @Override
  public int hashCode() {
    return Arrays.deepHashCode(board);
  }
}
