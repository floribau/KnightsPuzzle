# Knights Puzzle

## Rules
The goal is to increase the score of the board by the following rules:
1) Each black knight increases the score by 3, each white knight by 2
2) Each pair of neighboring knights of different colors increases the score by 2
3) Each knight increases the score by the sum of its row and column indices
4) Each pair of knights that can capture each other (no matter the color) decrease the score by 3

## Algorithms
I implemented three different Local Search Algorithms:
1) Hill Climbing (with sideways stepping)
2) Simulated Annealing
3) Beam Search

The best found solution of my algorithms had a score of 368
