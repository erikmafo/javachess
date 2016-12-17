package com.erikmafo.chess.search.evaluation;

import com.erikmafo.chess.board.Square;
import com.erikmafo.chess.pieces.Piece;

/**
 * Created by erikf on 12/17/2016.
 */
public class PositionalEvaluationTable {

    public enum GamePhase {
        OPENING,
        MIDDLE_GAME,
        END_GAME
    }



    /**
     * Returns an integer score that indicates how good the square is for the specified piece.
     *
     * Pawns are encouraged to stay in the center and advance forward
     * Knights are encouraged to control the center and stay away from edges to increase mobility
     * Bishops are also encouraged to control the center and stay away from edges and corners:
     * During the middle game kings are encouraged to stay in the corners, while in the end game
     * kings are encouraged to move towards the center.
     *
     * <br/>
     *
     * Thanks to www.chessbin.com
     *
     * @param piece - the piece to evaluate position
     * @param square - the square where the piece is located
     * @return - a integer score
     */
    public int getPositionalScore(Piece piece, Square square, GamePhase gamePhase) {

        int score;

        switch (piece.getType()) {
            case PAWN:
                score = getPawnScore(piece, square);
                break;
            case BISHOP:
                score = bishopTable[square.getIndex64()];
                break;
            case KNIGHT:
                score = knightTable[square.getIndex64()];
                break;
            case KING:
                score = getKingScore(square, gamePhase);
                break;
            case ROOK:
            case QUEEN:
                score = 0;
                break;
            default:
                throw new AssertionError();
        }


        return score;
    }

    private int getPawnScore(Piece piece, Square square) {
        int score;
        switch (piece.getColor()) {
            case BLACK:
                score = blackPawnTable[square.getIndex64()];
                break;
            case WHITE:
                score = whitePawnTable[square.getIndex64()];
                break;
            default:
                throw new AssertionError();
        }
        return score;
    }

    private int getKingScore(Square square, GamePhase phase) {
        int score;
        switch (phase) {
            case OPENING:
            case MIDDLE_GAME:
                score = kingTable[square.getIndex64()];
                break;
            case END_GAME:
                score = kingTableEndGame[square.getIndex64()];
                break;
            default:
                throw new AssertionError();
        }
        return score;
    }



    private static short[] whitePawnTable = new short[]
            {
                    0,  0,  0,  0,  0,  0,  0,  0,
                    50, 50, 50, 50, 50, 50, 50, 50,
                    10, 10, 20, 30, 30, 20, 10, 10,
                    5,  5, 10, 27, 27, 10,  5,  5,
                    0,  0,  0, 25, 25,  0,  0,  0,
                    5, -5,-10,  0,  0,-10, -5,  5,
                    5, 10, 10,-25,-25, 10, 10,  5,
                    0,  0,  0,  0,  0,  0,  0,  0
            };

    private static short[] blackPawnTable = new short[]
            {
                    0,  0,  0,  0,  0,  0,  0,  0,
                    5, 10, 10,-25,-25, 10, 10,  5,
                    5, -5,-10,  0,  0,-10, -5,  5,
                    0,  0,  0, 25, 25,  0,  0,  0,
                    5,  5, 10, 27, 27, 10,  5,  5,
                    10, 10, 20, 30, 30, 20, 10, 10,
                    50, 50, 50, 50, 50, 50, 50, 50,
                    0,  0,  0,  0,  0,  0,  0,  0
            };


    private static short[] knightTable = new short[]
            {
                    -50,-40,-30,-30,-30,-30,-40,-50,
                    -40,-20,  0,  0,  0,  0,-20,-40,
                    -30,  0, 10, 15, 15, 10,  0,-30,
                    -30,  5, 15, 20, 20, 15,  5,-30,
                    -30,  0, 15, 20, 20, 15,  0,-30,
                    -30,  5, 10, 15, 15, 10,  5,-30,
                    -40,-20,  0,  5,  5,  0,-20,-40,
                    -50,-40,-20,-30,-30,-20,-40,-50,
            };

    private static short[] bishopTable = new short[]
            {
                    -20,-10,-10,-10,-10,-10,-10,-20,
                    -10,  0,  0,  0,  0,  0,  0,-10,
                    -10,  0,  5, 10, 10,  5,  0,-10,
                    -10,  5,  5, 10, 10,  5,  5,-10,
                    -10,  0, 10, 10, 10, 10,  0,-10,
                    -10, 10, 10, 10, 10, 10, 10,-10,
                    -10,  5,  0,  0,  0,  0,  5,-10,
                    -20,-10,-40,-10,-10,-40,-10,-20,
            };

    private static short[] kingTable = new short[]
            {
                    -30, -40, -40, -50, -50, -40, -40, -30,
                    -30, -40, -40, -50, -50, -40, -40, -30,
                    -30, -40, -40, -50, -50, -40, -40, -30,
                    -30, -40, -40, -50, -50, -40, -40, -30,
                    -20, -30, -30, -40, -40, -30, -30, -20,
                    -10, -20, -20, -20, -20, -20, -20, -10,
                    20,  20,   0,   0,   0,   0,  20,  20,
                    20,  30,  10,   0,   0,  10,  30,  20
            };

    private static short[] kingTableEndGame = new short[]
            {
                    -50,-40,-30,-20,-20,-30,-40,-50,
                    -30,-20,-10,  0,  0,-10,-20,-30,
                    -30,-10, 20, 30, 30, 20,-10,-30,
                    -30,-10, 30, 40, 40, 30,-10,-30,
                    -30,-10, 30, 40, 40, 30,-10,-30,
                    -30,-10, 20, 30, 30, 20,-10,-30,
                    -30,-30,  0,  0,  0,  0,-30,-30,
                    -50,-30,-30,-30,-30,-30,-30,-50
            };


}
