package com.erikmafo.javachess.search;

import com.erikmafo.javachess.board.Board;
import com.erikmafo.javachess.move.Move;
import com.erikmafo.javachess.movegenerator.MoveGenerator;
import com.erikmafo.javachess.pieces.PieceColor;
import com.erikmafo.javachess.search.evaluation.BoardToIntFunction;
import com.erikmafo.javachess.search.transposition.TranspositionTable;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.*;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

/**
 * Created by erikmafo on 27.11.16.
 */
@RunWith(JUnitParamsRunner.class)
public class AlphaBetaSearchTest {

    private BoardToIntFunction evaluationFunction;
    private Board board = mock(Board.class);

    private MoveGenerator moveGenerator = mock(MoveGenerator.class);
    private TranspositionTable transpositionTable = mock(TranspositionTable.class);
    private AlphaBetaSearch alphaBetaSearch = new AlphaBetaSearch(moveGenerator, transpositionTable);


    private PieceColor firstToMove = PieceColor.WHITE;

    private Move goodForWhite = createMoveMock("good for white");
    private Move goodForBlack = createMoveMock("good for black");
    private Move neutral = createMoveMock("neutral");

    private List<Move> played = new ArrayList<>();

    private Move createMoveMock(String name) {

        Move move = mock(Move.class, name);
        doAnswer(invocationOnMock -> played.add(move)).when(move).play();
        doAnswer(invocationOnMock -> played.remove(move)).when(move).undo();

        return move;
    }

    @Before
    public void setUp() throws Exception {

        when(board.getColorToMove()).then(invocationOnMock -> getColorToMove());


        evaluationFunction = b -> {

            int sign = getColorToMove().isWhite() ? 1 : -1;

            int score = 0;

            for (Move move : played) {

                if (move.equals(goodForWhite)) {
                    score += 1;
                } else if (move.equals(goodForBlack)) {
                    score -= 1;
                }
            }

            return sign * score;
        };

    }

    private PieceColor getColorToMove() {
        return played.size() % 2 == 0 ? firstToMove : firstToMove.getOpposite();
    }

    private void setFirstToMove(PieceColor firstToMove) {
        this.firstToMove = firstToMove;
    }

    @Test
    @Parameters({"WHITE", "BLACK"})
    public void findBestMoveOneStepAhead(PieceColor firstToMove) throws Exception {

        setFirstToMove(firstToMove);

        Move bestMove = firstToMove.isWhite() ? goodForWhite : goodForBlack;

        when(moveGenerator.generateMoves(board)).then(invocationOnMock -> Arrays.asList(goodForWhite, goodForBlack, neutral));

        SearchResult result = alphaBetaSearch.execute(board, evaluationFunction, 1);

        assertThat("best move should be <" + bestMove + "> since first to move is " + firstToMove,
                result.getBestMove(), is(bestMove));
    }


    @Test
    public void findBestMoveForWhiteTwoStepsAhead() throws Exception {

        when(moveGenerator.generateMoves(board)).then(invocationOnMock -> {

            List<Move> moves;

            if (played.isEmpty()) { // white to move
                moves =  Arrays.asList(goodForWhite, goodForBlack, neutral);
            } else if (played.size() == 1) { // black to move

                Move playedByWhite = played.get(0);

                if (playedByWhite.equals(goodForWhite)) {
                    moves = Arrays.asList(goodForBlack, goodForWhite, neutral);
                } else if (playedByWhite.equals(neutral)) {
                    moves = Arrays.asList(goodForWhite);
                } else if (playedByWhite.equals(goodForBlack)) {
                    moves = Arrays.asList(goodForWhite, neutral);
                } else {
                    throw new AssertionError("Unexpected move: <" + playedByWhite + "> was played by white");
                }

            } else { // in case of quiescence search
                moves = Arrays.asList(neutral);
            }

            return moves;
        });


        SearchResult result = alphaBetaSearch.execute(board, evaluationFunction, 2);

        assertThat("playing "
                        + neutral
                        + " gives the white best position after 2 moves,"
                        + " assuming optimal play by black.",
                result.getBestMove(), is(neutral));

    }





}