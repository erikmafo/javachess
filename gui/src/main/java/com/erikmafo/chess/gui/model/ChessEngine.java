package com.erikmafo.chess.gui.model;

import com.erikmafo.chess.board.Board;
import com.erikmafo.chess.board.Square;
import com.erikmafo.chess.move.Move;
import com.erikmafo.chess.move.Moves;
import com.erikmafo.chess.piece.Piece;
import com.erikmafo.chess.piece.PieceColor;
import com.erikmafo.chess.search.AlphaBetaSearch;
import com.erikmafo.chess.search.IterativeDeepening;
import com.erikmafo.chess.search.SearchResult;
import com.erikmafo.chess.search.evaluation.*;
import com.erikmafo.chess.utils.parser.FenParseException;
import com.erikmafo.chess.utils.parser.FenParser;
import javafx.concurrent.Task;

import java.time.Clock;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by erikmafo on 07.01.17.
 */
public class ChessEngine {


    private Board board;
    private final FenParser fenParser;
    private final ExecutorService executorService;

    public ChessEngine(FenParser fenParser, ExecutorService executorService) {
        this.fenParser = fenParser;
        this.executorService = executorService;
    }

    public Map<Square, Piece> getSquarePieceMap() {
        if (board == null) {
            throw new IllegalStateException();
        }

        Map<Square, Piece> squarePieceMap = new EnumMap<>(Square.class);

        for (Square square : board.getOccupiedSquares()) {
            squarePieceMap.put(square, board.getNullablePiece(square));
        }

        return squarePieceMap;
    }

    public PieceColor getColorToMove() {
        if (board == null) {
            throw new IllegalStateException();
        }

        return board.getColorToMove();
    }


    public void setPosition(String fen, ChessMove... moves) throws FenParseException {
        setPosition(fen, Arrays.asList(moves));
    }


    public void setPosition(String fen, List<ChessMove> moves) throws FenParseException {
        board = fenParser.parse(fen);

        for (ChessMove move : moves) {
            applyMove(move);
        }

    }

    private void applyMove(ChessMove chessMove) {

        Move move = Moves.valueOf(board, chessMove.getFrom(), chessMove.getTo());

        if (move == null) {
            throw new IllegalArgumentException("Illegal move");
        } else {
            move.play();
        }

    }


    public boolean isLegal(ChessMove chessMove) {
        return Moves.valueOf(board, chessMove.getFrom(), chessMove.getTo()) != null;
    }


    public Task<EngineSearchResult> go() {

        if (board == null) {
            throw new IllegalStateException("Set position before calling go");
        }

        Task<EngineSearchResult> resultTask = new Task<EngineSearchResult>() {
            @Override
            protected EngineSearchResult call() throws Exception {
                BoardToIntFunction evaluation = new BoardToIntFunctionChain.Builder()
                        .addFunction(new MaterialBoardEvaluation())
                        .addFunction(new CastlingEvaluation())
                        //.addFunction(new MobilityEvaluation())
                        .addFunction(new PositionalEvaluation())
                        .build();

                IterativeDeepening iterativeDeepening =
                        new IterativeDeepening(board, new AlphaBetaSearch(), evaluation, Clock.systemUTC());

                SearchResult result;
                try {
                    result = iterativeDeepening.execute(3, 10, TimeUnit.SECONDS);
                } catch (RuntimeException ex) {
                    Logger.getLogger(ChessEngine.class.getName()).log(Level.SEVERE, null, ex);
                    throw ex;
                }

                return new EngineSearchResult(toChessMove(result.getBestMove()));
            }
        };


        executorService.submit(resultTask);

        return resultTask;
    }


    private ChessMove toChessMove(Move move) {
        return new ChessMove(move.getFrom(), move.getTo());
    }


    public void shutdown() {
        executorService.shutdown();
    }
}
