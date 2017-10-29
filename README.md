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
    - [Most Valuable Victim Least Valuable Attacker (MVVLVA)](#most-valuable-victim-least-valuable-attacker-mvvlva)
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

#### Most Valuable Victim Least Valuable Attacker (MVVLVA)

#### Iterative Deepening

#### Alpha-beta Pruning

#### Quiescence Search

### Misc

#### UCI (Universal Chess Interface)

#### Future Improvements

- Invoking fifty-move rule when available
- Castling only available when neither king nor rook have moved