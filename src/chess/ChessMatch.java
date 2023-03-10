package chess;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import chess.pieces.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ChessMatch {
    private int turn;
    private Color currentPlayer;
    private Board board;
    private boolean check;
    private boolean checkMate;

    private List<Piece> piecesOnTheBoard = new ArrayList<>();
    private List<Piece> capturedPieces = new ArrayList<>();



    // Construtor/Constructors.
    public ChessMatch(){
        board = new Board(8,8);
        turn = 1;
        currentPlayer = Color.WHITE;
        initialSetup();
    }

    // Getters e Setters
    public int getTurn(){
        return turn;
    }
    public Color getCurrentPlayer(){
        return currentPlayer;
    }
    public boolean getCheck(){
        return check;
    }
    public boolean getCheckMate(){
        return checkMate;
    }

    // Métodos/Methods.
    public ChessPiece[][] getPieces(){
        ChessPiece[][] mat = new ChessPiece[board.getRows()][board.getColumns()];
        for (int i = 0; i < board.getRows(); i++){
            for (int j = 0; j < board.getColumns(); j++){
                mat[i][j] = (ChessPiece) board.piece(i,j);
            }
        }
        return mat;
    }
    public boolean[][] possibleMoves(ChessPosition sourcePosition){
        Position position = sourcePosition.toPosition();
        validateSourcePosition(position);

        return board.piece(position).possibleMoves();
    }
    public ChessPiece performChessMove(ChessPosition sourcePosition, ChessPosition targetPosition){
        Position source = sourcePosition.toPosition();
        Position target = targetPosition.toPosition();
        validateSourcePosition(source);
        validateTargetPosition(source,target);
        Piece capturedPiece = makeMove(source,target);

        if (testCheck(currentPlayer)){
            undoMove(source,target,capturedPiece);
            throw new ChessException("Você não pode colocar o seu rei em check! | You can't put yourself in check!");
        }

        check = (testCheck(opponent(currentPlayer))) ? true : false;

        if (testCheckMate(opponent(currentPlayer))){
            checkMate = true;
        }
        else {
            nextTurn();
        }

        return (ChessPiece) capturedPiece;
    }
    private Piece makeMove(Position source, Position target){
        ChessPiece p = (ChessPiece) board.removePiece(source);
        p.increaseMoveCount();
        Piece capturedPiece = board.removePiece(target);
        board.placePiece(p, target);

        if (capturedPiece != null){
            piecesOnTheBoard.remove(capturedPiece);
            capturedPieces.add(capturedPiece);
        }
        return capturedPiece;
    }
    private void undoMove(Position source, Position target, Piece capturedPiece){
        ChessPiece p = (ChessPiece) board.removePiece(target);
        p.decreaseMoveCount();
        board.placePiece(p, source);

        if (capturedPiece != null){
            board.placePiece(capturedPiece,target);
            capturedPieces.remove(capturedPiece);
            piecesOnTheBoard.add(capturedPiece);
        }
    }
    public void validateSourcePosition(Position position){
        if (!board.thereIsAPiece(position)) {
            throw new ChessException("Não há nenhuma peça na posição de origem! | There is no piece on source position!");
        }
        if (currentPlayer != ((ChessPiece)board.piece(position)).getColor()){
            throw new ChessException("A peça escolhida não é sua! | The chosen piece is not yours!");
        }
        if (!board.piece(position).isThereAnyPossibleMove()){
            throw new ChessException("Não há movimentos possíveis para a peça escolhida! | There is no possible moves for the chosen piece!");
        }
    }
    private void validateTargetPosition(Position source, Position target){
        if (!board.piece(source).possibleMove(target)){
            throw new ChessException("A peça escolhida não pode se mover para essa posição! | The chosen can't move to target position!");
        }
    }
    private void nextTurn(){
        turn++;
        currentPlayer = (currentPlayer == Color.WHITE) ? Color.BLACK : Color.WHITE;
    }
    private Color opponent(Color color){
        return (color == Color.WHITE) ? Color.BLACK : Color.WHITE;
    }
    private ChessPiece king(Color color){
        List<Piece> list = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == color).collect(Collectors.toList());

        for (Piece p : list){
            if (p instanceof King){
                return (ChessPiece) p;
            }
        }
        throw new IllegalStateException("Não existe o rei da cor "+color+" no tabuleiro! | There is no "+color+" king on the board!");
    }
    private boolean testCheck(Color color){
        Position kingPosition = king(color).getChessPosition().toPosition();
        List<Piece> opponentPieces = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == opponent(color)).collect(Collectors.toList());

        for (Piece p : opponentPieces){
            boolean[][] mat = p.possibleMoves();
            if (mat[kingPosition.getRow()][kingPosition.getColumn()]){
                return true;
            }
        }
        return false;
    }

    private boolean testCheckMate(Color color){
        if (!testCheck(color)){
            return false;
        }

        List<Piece> list = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == color).collect(Collectors.toList());

        for (Piece p : list){
            boolean[][] mat = p.possibleMoves();
            for (int i = 0; i < board.getRows(); i++){
                for (int j = 0; j < board.getColumns(); j++){
                    if (mat[i][j]){
                        Position source = ((ChessPiece)p).getChessPosition().toPosition();
                        Position target = new Position(i,j);
                        Piece capturedPiece = makeMove(source,target);
                        boolean testCheck = testCheck(color);
                        undoMove(source,target,capturedPiece);
                        if (!testCheck){
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    private void placeNewPiece(char column, int row, ChessPiece piece){
        board.placePiece(piece,new ChessPosition(column,row).toPosition());
        piecesOnTheBoard.add(piece);
    }
    private void initialSetup() {
        // Peças Branco |White Pieces.
        // Torre | Rook.
        placeNewPiece('A',1, new Rook(board, Color.WHITE));
        placeNewPiece('H',1, new King(board, Color.WHITE));

        // Cavalo | Knight.
        placeNewPiece('B',1, new Knight(board, Color.WHITE));
        placeNewPiece('G',1, new Knight(board, Color.WHITE));

        // Bispo | Bishop.
        placeNewPiece('C',1, new Bishop(board, Color.WHITE));
        placeNewPiece('F',1, new Bishop(board, Color.WHITE));

        // Rei | King.
        placeNewPiece('E',1, new King(board, Color.WHITE));

        // Rainha | Queen.
        placeNewPiece('D',1, new Queen(board, Color.WHITE));

        // Peão | Pawn
        placeNewPiece('A',2, new Pawn(board, Color.WHITE));
        placeNewPiece('B',2, new Pawn(board, Color.WHITE));
        placeNewPiece('C',2, new Pawn(board, Color.WHITE));
        placeNewPiece('D',2, new Pawn(board, Color.WHITE));
        placeNewPiece('E',2, new Pawn(board, Color.WHITE));
        placeNewPiece('F',2, new Pawn(board, Color.WHITE));
        placeNewPiece('G',2, new Pawn(board, Color.WHITE));
        placeNewPiece('H',2, new Pawn(board, Color.WHITE));

        // Peças Preto | Black Pieces.
        // Torre | Rook.
        placeNewPiece('A',8, new Rook(board, Color.BLACK));
        placeNewPiece('H',8, new Rook(board, Color.BLACK));

        // Cavalo | Knight.
        placeNewPiece('B',8, new Knight(board, Color.BLACK));
        placeNewPiece('G',8, new Knight(board, Color.BLACK));

        // Bispo | Bishop.
        placeNewPiece('C',8, new Bishop(board, Color.BLACK));
        placeNewPiece('F',8, new Bishop(board, Color.BLACK));

        // Rei | King.
        placeNewPiece('E',8, new King(board, Color.BLACK));

        // Rainha | Queen.
        placeNewPiece('D',8, new Queen(board, Color.BLACK));

        // Peão | Pawn.
        placeNewPiece('A',7, new Pawn(board, Color.BLACK));
        placeNewPiece('B',7, new Pawn(board, Color.BLACK));
        placeNewPiece('C',7, new Pawn(board, Color.BLACK));
        placeNewPiece('D',7, new Pawn(board, Color.BLACK));
        placeNewPiece('E',7, new Pawn(board, Color.BLACK));
        placeNewPiece('F',7, new Pawn(board, Color.BLACK));
        placeNewPiece('G',7, new Pawn(board, Color.BLACK));
        placeNewPiece('H',7, new Pawn(board, Color.BLACK));
    }
}