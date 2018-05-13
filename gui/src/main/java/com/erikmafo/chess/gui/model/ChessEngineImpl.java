package com.erikmafo.chess.gui.model;

import com.erikmafo.chess.board.Square;
import com.erikmafo.chess.piece.Piece;
import com.erikmafo.chess.piece.PieceColor;
import com.erikmafo.chess.utils.parser.FenParseException;
import com.erikmafo.chess2.movegeneration.Board;
import com.erikmafo.chess2.movegeneration.Move;
import com.erikmafo.chess2.utils.Fen;
import javafx.concurrent.Task;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by erikmafo on 03.12.17.
 */
public class ChessEngineImpl implements ChessEngine {

    private Board board;
    private final ExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    @Override
    public Map<Square, Piece> getSquarePieceMap() {

        Map<Square, Piece> pieceMap = new HashMap<>();

        for (Square square : Square.values()) {
            Piece piece = board.at(square);

            if (piece != null) {
                pieceMap.put(square, piece);
            }
        }


        return pieceMap;
    }

    @Override
    public PieceColor getColorToMove() {
        return board.colorToMove();
    }

    @Override
    public void setPosition(String fen, ChessMove... moves) throws FenParseException {
        setPosition(fen, Arrays.asList(moves));
    }

    private ChessMove convertToGuiMove(Move move) {
        return new ChessMove(move.from(), move.to());
    }


    private List<Move> getLegalMoves() {
        List<Move> pseudoLegalMoves = board.generateMoves();
        List<Move> legalMoves = new ArrayList<>();

        for (Move move : pseudoLegalMoves) {
            board.play(move);
            if (!board.isChecked(move.getMovingColor())) {
                legalMoves.add(move);
            }
            board.undoLastMove();
        }

        return legalMoves;
    }


    private Move convertToEngineMove(ChessMove guiMove) {
        Move result = null;

        for (Move move : getLegalMoves()) {
            if (move.from() == guiMove.getFrom() && move.to() == guiMove.getTo()) {
                result = move;
                break;
            }
        }

        return result;
    }


    @Override
    public void setPosition(String fen, List<ChessMove> moves) throws FenParseException {
        board = Fen.toX88Board(fen);
        for (ChessMove move : moves) {
            board.play(convertToEngineMove(move));
        }
    }

    @Override
    public boolean isLegal(ChessMove chessMove) {
        return convertToEngineMove(chessMove) != null;
    }

    @Override
    public Task<EngineSearchResult> go() {
        Task<EngineSearchResult> task = new Task<EngineSearchResult>() {
            @Override
            protected EngineSearchResult call() throws Exception {
                List<Move> legalMoves = getLegalMoves();
                int rand = ThreadLocalRandom.current().nextInt(0, legalMoves.size());
                ChessMove bestMove = convertToGuiMove(legalMoves.get(rand));
                return new EngineSearchResult(bestMove);
            }
        };

        executorService.submit(task);

        return task;
    }

    @Override
    public void shutdown() {
        executorService.shutdown();
    }
}
