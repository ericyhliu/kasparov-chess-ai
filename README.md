# Kasparov Chess AI

<p>
  <a href="https://travis-ci.com/eliucs/kasparov"><img 
  src="https://travis-ci.com/eliucs/kasparov.svg?token=bcVBJb9UixL3HoJpm9WE&branch=master"></a>
</p>

This project is a UCI ([Universal Chess Interface](http://wbec-ridderkerk.nl/html/UCIProtocol.html))
compatible chess engine built in Java. The following provides documentation for
various aspects of the project including board representation with bitboards,
searching with iterative deepening with alpha-beta pruning, and quiescence
search to deal with the horizon effect.

## Table of Contents

- [Board Representation](#board-representation)
    - [Bitboard](#bitboard)
    - [Fifty Move, En Passant, Castling](#fifty-move-en-passant-castling)
    - [Move Generation](#move-generation)
    - [Forsyth-Edwards Notation (FEN)](#forsyth-edwards-notation-fen)
    - [Perft Testing](#perft-testing)
- [Search and Move Ordering](#search-and-move-ordering)
    - [PV Table (Principal Variation Table)](#pv-table-principal-variation-table)
    - [Most Valuable Victim Least Valuable Aggressor (MVVLVA)](#most-valuable-victim-least-valuable-attacker-mvvlva)
    - [Iterative Deepening](#iterative-deepening)
    - [Alpha-beta Pruning](#alpha-beta-pruning)
    - [Quiescence Search](#quiescence-search)
- [Misc](#misc)
    - [UCI (Universal Chess Interface)](#uci-universal-chess-interface)
    - [Future Improvements](#future-improvements)

### Board Representation

#### Bitboard

A [bitboard](https://en.wikipedia.org/wiki/Bitboard) is used to represent the 
current state of the chess board. Using a 64-bit number (`long` in Java, and
ignoring the signed bit), each bit maps to a square on the 64-square board. 
For example, in the `BoardStructure` class, bitboards are used to set the 
locations of the pawns, and one is used for white, black, and both colored 
pawns.

#### Fifty Move, En Passant, Castling

The [fifty-move rule](https://en.wikipedia.org/wiki/Fifty-move_rule) is a rule
that a draw can be made if no capture has been made or no pawn has moved in the
last fifty moves. In the `BoardStructure` class, we keep track of the fifty move
with a counter `fiftyMove`, and whenever we make a move in the `MakeMove` class,
if a non-capture, non-pawn move was made, `fiftyMove` gets incremented, 
otherwise it is reset to zero. Note: a future improvement needs to be made 
for the program to draw whenever the fifty-move rule can be invoked.

[En passant](https://en.wikipedia.org/wiki/En_passant) is a special pawn capture
move. When a pawn makes a double-step move, an enemy pawn can capture it as if 
it only advanced one square. It needs to be made immediately on the next move, 
otherwise it is lost. The "en passant square" refers to the destination square
itself to which the enemy pawn must move. In the `BoardStructure` class, we keep 
track of the current active (if any) en passant square with `enPassant`. 
Whenever we make a pawn start move (that is, when a pawn starts off at
ranks 2 or 7), the `enPassant` square is set.

[Castling](https://en.wikipedia.org/wiki/Castling) is a move that involves the
king and the rook, where the king moves 2 squares towards the rook, and rook 
"jumps" over the king. However, it is only available if neither the king nor
the rooks have moved yet. In the `BoardStructure` class, we keep track of the 
castling availability with `castlePerm`, which is an integer. The first bit 
represents white's king side castling availability, the second bit represents
white's queen side, the third bit represents black's king side, and the fourth
bit represents blacks queen side. Note: a future improvement needs to be made
so that it is only available if neither the king nor the rook has moved.

#### Move Generation

[Move generation](https://chessprogramming.wikispaces.com/Move+Generation) is
the process of checking all legal moves from a given position of the chess 
board, for a particular side to move. These moves are stored in a list, called
the move list. Moves are represented with a 32-bit number. We generate the move
list by iterating through every piece and determining all of its possible, valid
destination squares on the board, that is, the destination square must be 
empty and not offboard. The method of generating all of the move directions is
with an array `pieceDirections` that maps each piece to an array of all of its
directions.

For example, with the knight we must check 8 positions:

```
8 . . . . . . . . 
7 . . . . . . . . 
6 . . . . . . . . 
5 . . . x . x . . 
4 . . x . . . x . 
3 . . . . k . . . 
2 . . x . . . x . 
1 . . . x . x . . 
  a b c d e f g h
```

This corresponds to the directions `{-8, -19, -21, -12,   8,  19,   21,  12}`.

#### Forsyth-Edwards Notation (FEN)

[Forsyth-Edwards Notation (FEN)](https://en.wikipedia.org/wiki/Forsyth%E2%80%93Edwards_Notation)
is a notation used to represent a particular position of the chess board. It is 
in the format:

```
X1/X2/X3/X4/X5/X6/X7/X8 <side> <castling> <en passant square> <half moves> <full moves>
```

Each of `X1 .. X8` represent the ranks 8 to 1, and the contents of each square
from files A to H are described for each rank. White pieces are represented as
`p = pawn, n = knight, b = bishop, r = rook, q = queen, k = king`,
and the black pieces are the same but uppercase. Consecutive empty squares are
represented as numbers 1 to 8. Side represents the current side to move (either
`w` for white or `b` for black). Castling represent current castling 
availability (`K = white kingside, Q = white queenside, k = black kingside, q = black queen side`).
The en passant square is represented in algebraic notation. The half move 
counter counts the number of half moves made since the last capture or pawn
move, and this is used to implement the fifty-move rule. The full move counter
counts the total number of full moves made in the game.

#### Perft Testing

[Perft testing (performance testing)](https://chessprogramming.wikispaces.com/Perft)
is a method of rigorously testing the validity of the chess engine to 
generate all legal moves for a specified depth, and counting the number of moves
generated. If the count matches with the test suite expected results, then we
can be fairly confident in the correctness of the program. We say `Perft(X)` to
denote depth `X`.

Perft testing is done with `PerftSuiteTest.java`, which runs the test suite 
specified in `perft/perftsuite.txt`, which provides 126 positions in FEN, and 
the expected number of moves generated for depths `Perft(1)` to `Perft(6)`.

### Search and Move Ordering

#### PV Table (Principal Variation Table)

[Principle variation](https://chessprogramming.wikispaces.com/Principal+variation) 
refers to the sequence of moves that the program considers the best next moves 
to be played. During the iterative deepening loop, we need to consider the PV 
during the current iteration. The approach in this program is to store the PV
found during search as a `PVEntry`, which is stored in an array inside the 
`PVTable`. 

The `PVTable` works as a hash table, as we hash the board's `positionKey` as an 
index into the array where we store the `PVEntry`. Similarly, to retrieve the 
last `PVEntry` based on the current board `positionKey`, we hash the 
`positionKey` to get an index to lookup in the array. The `PVTable` size is 
fixed at `150000`, a reasonably large size to prevent hash collisions.

#### Most Valuable Victim Least Valuable Aggressor (MVVLVA)

[Most Valuable Victim Least Valuable Aggressor (MVVLVA)](https://chessprogramming.wikispaces.com/MVV-LVA)
is exactly what its name implies, and is a heuristic used for ordering capture
moves. The principle is to use the least valuable piece on the current side to
capture the most valuable piece on the opposing side. By order of most valuable
to least valuable, excluding the king, we have: queen, rook, bishop, knight,
pawn.

#### Iterative Deepening

[Iterative deepening](https://chessprogramming.wikispaces.com/Iterative+Deepening)
is a graph search strategy where depth-first search is done to a limited depth,
and increasing depth limits each time until the search time has been exhausted.
It combines the space efficiency of depth-first search with the "completeness"
of breadth first search. This is implemented, along with alpha-beta search and 
quiescence search, in the `Search` class.

The approach in this program is that that we iterate from depths 1 to the 
maximum depth (given by `searchEntry.getDepth()`), we run alpha-beta search 
limited to that depth. This can be visualized as:

```
            1
    /       |       \
   2        2        2
 / | \    / | \    / | \
3  3  3  3  3  3  3  3  3
```

We search only up to depth 1 down the game tree on the first iteration, then up 
to 2, then 3, and so on. Note that although we do repeat visits, such as if we 
go to a depth of 3 repeating visiting nodes at depth 1 and 2 multiple times, it
turns out that because the game tree grows exponentially, most of the nodes at
at the leaves of the tree, so repeating visiting nodes at the top of the tree do 
not majorly affect the overall search time.

#### Alpha-beta Pruning

Alpha-beta is an improvement over the minimax algorithm.

We start off with minimax. [Minimax](https://chessprogramming.wikispaces.com/Minimax) 
is an algorithm to determine the score of the best move after a certain depth, 
for zero-sum games. The main idea behind it is that one side's gain must 
translate to the other side's loss, i.e. as we go down the game tree, at odd 
depths we search for positions such that we maximize the score, and at even 
depths we search for positions such that we minimize the score. However, to 
minimize we can simply negate the maximizing score (this is also known as 
[negamax](https://chessprogramming.wikispaces.com/Negamax)). 

The approach done in this program is to recursively call negamax, negating it
each time until the base case, which is a depth of 0, where the 
`PositionEvaluator` class is used to determine a score for that position. The 
following provides brief pseudocode:

```
miniMaxSearch(int depth):
    if (depth == 0)
        return PositionEvaluator.evaluatePosition()
    max = -inf
    moveList = MoveGenerator.generateAllMoves()
    for move in moveList:
        score = -miniMaxSearch(depth - 1)
        if (score > max):
            max = score
    return max
```

[Alpha-beta pruning](https://chessprogramming.wikispaces.com/Alpha-Beta) is an
algorithm that improves upon minimax in that it introduces two values, `alpha`
and `beta`, which represent the current lower bound for the score for the 
maximizing player and the current upper bound for the score against the 
minimizing player, respectively. This value is nearly equivalent to the `max`
in the minimax algorithm. However, as we go down the game tree, if we are at
a stage in the algorithm such that in the next branch of the game tree, the 
maximizer can do at least as well as the currently explored option (or vice 
versa), then that branch can be eliminated entirely, because there would be no
point exploring that branch since we already have a lower bound, and thus option
worse should be ignored. This induces what is called a beta cutoff.

The following provides brief pseudocode:

```
alphaBetaSearch(int alpha, int beta, int depth):
    if (depth == 0)
        return PositionEvaluator.evaluatePosition()
    moveList = MoveGenerator.generateAllMoves()
    for move in moveList:
        score = -alphaBetaSearch(alpha, beta, depth - 1)
        if (score >= beta)
            return beta
        if (score > alpha):
            alpha = score
    return alpha
```

#### Quiescence Search

[Quiescence search](https://chessprogramming.wikispaces.com/Quiescence+Search) 
is used to lessen the effects of the horizon effect, where in the original 
alpha-beta search as above evaluates the position, instead we perform an 
additional search after. We search tactical exchanges (i.e. capture moves).

The [horizon effect](https://en.wikipedia.org/wiki/Horizon_effect) is a problem
that occurs due to the depth limitation of the search, where a detrimental move
may not be able to be avoided because the program cannot search to the depth 
of the error, i.e. a significant erroroneous move exists just beyond the search 
depth of the program, but is not detected.

The approach is that we first look at what is called standing pat, which is 
essentially a lower bound on the score of if we do nothing, if it is at least 
as good as alpha, then we return alpha. Then we generate all of the capture 
moves, and continue searching from there, and then using `PositionEvaluator` 
to evaluate the position.

We can take advantage of the fact that generally, the amount of capture moves
at any given position is relatively few, so the branching factor resulting from
looking at further capture moves is low. Compared to if we were to increase the
depth of alpha-beta instead, we would have been searching for potentially 
hundreds of thousands more positions than a few hundred.

### Misc

#### UCI (Universal Chess Interface)

The [Universal Chess Interface (UCI)](http://wbec-ridderkerk.nl/html/UCIProtocol.html)
is a common protocol used to communicate with a GUI .

The program can parse UCI `position` and `go` commands:

```
position startpos [moves <m1 m2 ... mn>]
```

Sets up the board position to be the starting FEN position, with the option
of initially making moves `m1 .. mn`.

```
position fen <fenString> [moves <m1 m2 ... mn>]
```

Sets up the board position to be specified FEN string (in double quotes), with
the option of initially making moves `m1 .. mn`.

```
go [winc <i>] [binc <i>] [wtime <t>] [btime <t>] [movestogo <m>] [depth <d>]
```

Runs the search, with the given options. `winc` and `binc` refer to the white 
and black increment per move in milliseconds, `wtime` and `btime` refer to the 
time left given to white and black in milliseconds, `movetime` refers to the 
maximum time allowed to make a move in milliseconds, `movestogo` refers to the 
remaining number of moves to go, and `depth` refers to the maximum search depth.

#### Future Improvements

- Invoking fifty-move rule when available
- Castling only available when neither king nor rook have moved

#### Resources

- [Chess Programming Wiki](https://chessprogramming.wikispaces.com)
- [UCI Protocol](http://wbec-ridderkerk.nl/html/UCIProtocol.html)
- [Rules of Chess](http://www.chessvariants.com/d.chess/chess.html)
- [Minimax Search and Alpha-beta Pruning](https://www.cs.cornell.edu/courses/cs312/2002sp/lectures/rec21.htm)