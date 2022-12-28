package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessPiece;
import chess.Color;

public class Pawn extends ChessPiece {

    public Pawn(Board board, Color color) {
        super(board, color);
    }

    @Override
    public String toString() {
        return "P";
    }

    @Override
    public boolean[][] possibleMoves() {
        boolean[][] mat = new boolean[getBoard().getRows()][getBoard().getColumns()];

        Position p = new Position(0, 0);

        if (getColor() == Color.WHITE){
            // Fazer peão Branco andar.
            // Whait Pawn walking.
            p.setValues(position.getRow() -1, position.getColumn());
            if (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)){
                mat[p.getRow()][p.getColumn()] = true;
            }
            // Fazer peão Branco andar 2 casa em sua jogada inicial e verica se a casa que o peão vai ser jogado está livre.
            p.setValues(position.getRow() -2, position.getColumn());

            Position p2 = new Position(position.getRow() -1, position.getColumn());

            if (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p) && getBoard().positionExists(p2) && !getBoard().thereIsAPiece(p2) && getMoveCount() == 0){
                mat[p.getRow()][p.getColumn()] = true;
            }
            // Verificando peça adversária das diagonais.
            p.setValues(position.getRow() -1, position.getColumn() -1);
            if (getBoard().positionExists(p) && isThereOpponentPiece(p)){
                mat[p.getRow()][p.getColumn()] = true;
            }
            p.setValues(position.getRow() -1, position.getColumn() +1);
            if (getBoard().positionExists(p) && isThereOpponentPiece(p)){
                mat[p.getRow()][p.getColumn()] = true;
            }
        }
        else {
            // Fazer peão Black andar.
            // Black Pawn walking.
            p.setValues(position.getRow() +1, position.getColumn());
            if (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)){
                mat[p.getRow()][p.getColumn()] = true;
            }
            // Fazer peão Black andar 2 casa em sua jogada inicial e verica se a casa que o peão vai ser jogado está livre.
            p.setValues(position.getRow() +2, position.getColumn());

            Position p2 = new Position(position.getRow() +1, position.getColumn());

            if (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p) && getBoard().positionExists(p2) && !getBoard().thereIsAPiece(p2) && getMoveCount() == 0){
                mat[p.getRow()][p.getColumn()] = true;
            }
            // Verificando peça adversária das diagonais.
            p.setValues(position.getRow() +1, position.getColumn() -1);
            if (getBoard().positionExists(p) && isThereOpponentPiece(p)){
                mat[p.getRow()][p.getColumn()] = true;
            }
            p.setValues(position.getRow() +1, position.getColumn() +1);
            if (getBoard().positionExists(p) && isThereOpponentPiece(p)){
                mat[p.getRow()][p.getColumn()] = true;
            }
        }
        return mat;
    }

}