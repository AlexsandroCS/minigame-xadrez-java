package application;

import chess.ChessException;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Program {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ChessMatch chessMatch = new ChessMatch();
        List<ChessPiece> captured = new ArrayList<>();

        while(!chessMatch.getCheckMate()){
            try {
                UI.clearScreen();
                UI.printMatch(chessMatch, captured);
                System.out.print("Posição da peça a ser movida | Source: ");
                ChessPosition source = UI.readChessPosition(sc);

                boolean[][] possibleMoves = chessMatch.possibleMoves(source);
                UI.clearScreen();
                UI.printBoard(chessMatch.getPieces(),possibleMoves);

                System.out.print("Posição para mover a peça escolhida | Target: ");
                ChessPosition target = UI.readChessPosition(sc);

                ChessPiece capturedPiece = chessMatch.performChessMove(source, target);

                if (capturedPiece != null){
                    captured.add(capturedPiece);
                }
            }
            catch (ChessException | InputMismatchException e){
                System.out.println(e.getMessage());
                System.out.println("Pressione enter para continuar! | Press enter for continue!");
                sc.nextLine();
            }
        }
        UI.clearScreen();
        UI.printMatch(chessMatch,captured);
    }
}