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

- Board Representation
    - Bitboard
    - Fifty Move, En Passant, Castling
    - Move Generation
    - Forsyth-Edwards Notation (FEN)
    - Perft Testing
- Search and Move Ordering
    - PV Table (Principal Variation Table)
    - Most Valuable Victim Least Valuable Attacker (MVVLVA)
    - Iterative Deepening
    - Alpha-beta Pruning
    - Quiescence Search
- Misc
    - UCI (Universal Chess Interface)
    - Future Improvements

### Board Representation

#### Bitboard

#### Fifty Move, En Passant, Castling

The [fifty-move rule](https://en.wikipedia.org/wiki/Fifty-move_rule)  

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

### Search and Move Ordering

#### PV Table (Principal Variation Table)

#### Most Valuable Victim Least Valuable Attacker (MVVLVA)

#### Iterative Deepening

#### Alpha-beta Pruning

#### Quiescence Search

### Misc

#### UCI (Universal Chess Interface)

#### Future Improvements
