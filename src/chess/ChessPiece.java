package chess;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;

public abstract class ChessPiece extends Piece {
    // Atributos.
    private Color color;
    private int moveCount;

    // Constructors.
    public ChessPiece(Board board, Color color) {
        super(board);
        this.color = color;
    }

    // Getters e Setters.
    public Color getColor() {
        return color;
    }
    public ChessPosition getChessPosition(){
        return ChessPosition.fromPosition(position);
    }
    public int getMoveCount() {
        return moveCount;
    }

    // Métodos.
    public void increaseMoveCount(){
        moveCount++;
    }
    public void decreaseMoveCount(){
        moveCount--;
    }
    protected boolean isThereOpponentPiece(Position position){
        ChessPiece p = (ChessPiece)getBoard().piece(position);

        return p != null && p.getColor() != color;
    }
}
