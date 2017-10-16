JFLAGS = -g
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
	  src/com/kasparov/BoardCastleLink.java \
	  src/com/kasparov/BoardColor.java \
	  src/com/kasparov/BoardConstants.java \
	  src/com/kasparov/BoardFile.java \
	  src/com/kasparov/BoardPiece.java \
	  src/com/kasparov/BoardRank.java \
	  src/com/kasparov/BoardSquare.java \
	  src/com/kasparov/BoardStructure.java \
	  src/com/kasparov/Main.java \
	  src/com/kasparov/MakeMove.java \
	  src/com/kasparov/Move.java \
	  src/com/kasparov/MoveGenerator.java \
	  src/com/kasparov/MoveList.java \
	  src/com/kasparov/PerftSuite.java \
	  src/com/kasparov/PerftTest.java \
	  src/com/kasparov/PositionEvaluator.java \
	  src/com/kasparov/PositionKey.java \
	  src/com/kasparov/PVEntry.java \
	  src/com/kasparov/PVTable.java \
	  src/com/kasparov/Search.java \
	  src/com/kasparov/SearchEntry.java \
	  src/com/kasparov/SquareAttacked.java \
	  src/com/kasparov/Time.java \
	  src/com/kasparov/UCI.java \
	  src/com/kasparov/UndoStructure.java \
	  src/com/kasparov/Validate.java

default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class
