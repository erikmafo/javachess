package com.erikmafo.chess2.search;

import com.erikmafo.chess.piece.PieceColor;
import com.erikmafo.chess2.evaluation.EvaluationFunction;
import com.erikmafo.chess2.movegeneration.Move;
import com.erikmafo.chess2.movegeneration.Board;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by erikmafo on 19.11.17.
 */

@RunWith(JUnitParamsRunner.class)
public class AlphaBetaSearchAlgorithmTest {

    private Board board = mock(Board.class);
    private EvaluationFunction evaluationFunction;
    private TranspositionTable transpositionTable = mock(TranspositionTable.class);

    private SearchAlgorithm searchAlgorithm;
    private PieceColor firstToMove = PieceColor.WHITE;

    private Move onePointIncreaseWhiteMove = createMoveMock("onePointIncreaseWhiteMove");
    private Move onePointIncreaseBlackMove = createMoveMock("onePointIncreaseBlackMove");
    private Move neutralMove = createMoveMock("neutralMove");

    private List<Move> played = new ArrayList<>();

    private Move createMoveMock(String name) {

        Move move = mock(Move.class, name);
        doAnswer(invocationOnMock -> played.add(move)).when(board).play(move);
        doAnswer(invocationOnMock -> played.remove(played.size() - 1)).when(board).undoLastMove();

        return move;
    }

    @Before
    public void setUp() throws Exception {

        when(board.colorToMove()).then(invocationOnMock -> getColorToMove());

        evaluationFunction = b -> {

            int sign = getColorToMove().isWhite() ? 1 : -1;

            int score = 0;

            for (Move move : played) {

                if (move.equals(onePointIncreaseWhiteMove)) {
                    score += 1;
                } else if (move.equals(onePointIncreaseBlackMove)) {
                    score -= 1;
                }
            }

            return sign * score;
        };

        searchAlgorithm = new AlphaBetaSearchAlgorithm(transpositionTable, evaluationFunction);

    }

    private PieceColor getColorToMove() {
        return played.size() % 2 == 0 ? firstToMove : firstToMove.opponent();
    }

    private void setFirstToMove(PieceColor firstToMove) {
        this.firstToMove = firstToMove;
    }

    @Test
    @Parameters({"WHITE", "BLACK"})
    public void findBestMoveOneStepAhead(PieceColor firstToMove) throws Exception {

        //Given
        setFirstToMove(firstToMove);
        Move bestMove = firstToMove.isWhite() ? onePointIncreaseWhiteMove : onePointIncreaseBlackMove;
        when(board.generateMoves()).then(invocationOnMock -> Arrays.asList(onePointIncreaseWhiteMove, onePointIncreaseBlackMove, neutralMove));

        //When
        SearchResult result = searchAlgorithm.execute(board, 1);

        //Then
        assertThat("should find best move", result.getBestMove(), is(bestMove));
    }


    @Test
    public void findBestMoveForWhiteTwoStepsAhead() throws Exception {

        //Given
        when(board.generateMoves()).then(invocationOnMock -> {

            List<Move> moves = null;

            if (played.isEmpty()) { // white to move
                moves =  Arrays.asList(onePointIncreaseWhiteMove, onePointIncreaseBlackMove, neutralMove);
            } else if (played.size() == 1) { // black to move

                Move playedByWhite = played.get(0);

                if (playedByWhite.equals(onePointIncreaseWhiteMove)) {
                    moves = Arrays.asList(onePointIncreaseBlackMove, onePointIncreaseWhiteMove, neutralMove);
                } else if (playedByWhite.equals(neutralMove)) {
                    moves = Arrays.asList(onePointIncreaseWhiteMove);
                } else if (playedByWhite.equals(onePointIncreaseBlackMove)) {
                    moves = Arrays.asList(onePointIncreaseWhiteMove, neutralMove);
                } else {
                    Assert.fail("Unexpected move: <" + playedByWhite + "> was played by white");
                }

            } else {
                moves = Arrays.asList(neutralMove);
            }

            return moves;
        });

        //When
        SearchResult result = searchAlgorithm.execute(board, 2);

        //Then
        assertThat("should find best move", result.getBestMove(), is(neutralMove));

    }





}