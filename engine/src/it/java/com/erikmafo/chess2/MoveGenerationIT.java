package com.erikmafo.chess2;

import com.erikmafo.chess.piece.PieceColor;
import com.erikmafo.chess.piece.PieceType;
import com.erikmafo.chess.utils.parser.FenParseException;
import com.erikmafo.chess2.movegeneration.Board;
import com.erikmafo.chess2.movegeneration.Move;
import com.erikmafo.chess2.utils.Fen;
import junitparams.FileParameters;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import junitparams.mappers.DataMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.BufferedReader;
import java.io.Reader;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;


import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;


@RunWith(JUnitParamsRunner.class)
public class MoveGenerationIT {

    @Test
    @FileParameters(value = "src/it/resources/perft.csv")
    public void perftTestSuite(String fen, String... perftResults) throws Exception {
        for (String perftResult : perftResults) {

            if (fen.equalsIgnoreCase("4k2r/8/8/8/8/8/8/4K3 w k - 0 1")) {
                System.out.print("");
            }

            //Given
            String[] parts = perftResult.split(" ");
            int depth = Integer.parseInt(parts[0].substring(1));
            int expectedLeafNodes = Integer.parseInt(parts[1]);

            //When
            Board board = Fen.toX88Board(fen);
            LeafNodeCount leafNodeCount = new LeafNodeCount();
            countGeneratedMovesRecursive(leafNodeCount, board, depth);

            //Then
            assertThat("count correct number of leaf nodes at depth " + depth +  " starting from position: " + fen,
                    leafNodeCount.total, is(expectedLeafNodes));

            if (depth == 3)
                break;
        }
    }

    @Parameterized.Parameters
    public static Collection<Object[]> perftData() {

        String position1 = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
        String position2 = "r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq -";
        String position3 = "8/2p5/3p4/KP5r/1R3p1k/8/4P1P1/8 w - -";
        String position4 = "r3k2r/Pppp1ppp/1b3nbN/nP6/BBP1P3/q4N2/Pp1P2PP/R2Q1RK1 w kq - 0 1";

        return Arrays.asList(
                // Fen | Depth | Moves | Captures | E.P | Castles | Promotions | Checks
                new Object[] {position1, 1, 20, 0, 0, 0, 0, 0},
                new Object[] {position1, 2, 400, 0, 0, 0, 0, 0},
                new Object[] {position1, 3, 8_902, 34, 0, 0, 0, 12},
                new Object[] {position1, 4, 197_281, 1576, 0, 0, 0, 469},
                new Object[] {position1, 5, 4_865_609, 82719, 258, 0, 0, 27351},
                //new Object[] {position1, 6, 119_060_324, 2812008, 5248, 0, 0, 809099},

                new Object[] {position2, 1, 48, 8, 0, 2, 0, 0},
                new Object[] {position2, 2, 2039, 351, 1, 91, 0, 3},
                new Object[] {position2, 3, 97862, 17102, 45, 3162, 0, 993},
                new Object[] {position2, 4, 4085603, 757163, 1929, 128013, 15172, 25523},

                new Object[] {position3, 1, 14, 1, 0, 0, 0, 2},
                new Object[] {position3, 2, 191, 14, 0, 0, 0, 10},
                new Object[] {position3, 3, 2812, 209, 2, 0, 0, 267},
                new Object[] {position3, 4, 43238, 3348, 123, 0, 0, 1680},
                new Object[] {position3, 5, 674624, 52051, 1165, 0, 0, 52950},
                new Object[] {position3, 6, 706045033, 210369132, 212, 10882006, 81102984, 26973664},

                new Object[] {position4, 1, 6, 0, 0, 0, 0, 0},
                new Object[] {position4, 2, 264, 87, 0, 6, 48, 10},
                new Object[] {position4, 3, 9467, 1021, 4, 0, 120, 28}
        );
    }

    @Test
    @Parameters(method = "perftData")
    public void perftTest(String fen, int depth, int nodes, int captures, int enPassent, int catles, int promotions, int checks) throws Exception {

        //Given
        Board board = Fen.toX88Board(fen);

        //When
        LeafNodeCount leafNodeCount = new LeafNodeCount();
        countGeneratedMovesRecursive(leafNodeCount, board, depth);

        //Then
        assertThat("is correct en passent count", leafNodeCount.enPassent, is(enPassent));
        assertThat("is correct promotion count", leafNodeCount.promotions, is(promotions));
        assertThat("is correct check count", leafNodeCount.checks, is(checks));
        assertThat("is correct capture count", leafNodeCount.captures, is(captures));
        assertThat("is correct leaf node count", leafNodeCount.total, is(nodes));
        assertThat("is correct castles count", leafNodeCount.castles, is(catles));
    }

    private void countGeneratedMovesRecursive(LeafNodeCount leafNodeCount, Board board, int depthLeft) {

        assert depthLeft >= 0;

        if (depthLeft == 0) {
            return;
        }

        for (Move move : board.generateMoves()) {
            board.play(move);
            if (!board.isChecked(move.getMovingColor())) {
                countGeneratedMovesRecursive(leafNodeCount, board, depthLeft - 1);
                if (depthLeft == 1) {// only leafNodeCount leaf nodes
                    updateCount(leafNodeCount, board, move);
                }
            }
            board.undoLastMove();
        }
    }

    private void updateCount(LeafNodeCount leafNodeCount, Board board, Move move) {
        leafNodeCount.total++;

        switch (move.kind()) {
            case EN_PASSENT:
                leafNodeCount.enPassent++;
                break;
            case KNIGHT_PROMOTION:
            case BISHOP_PROMOTION:
            case ROOK_PROMOTION:
            case QUEEN_PROMOTION:
                leafNodeCount.promotions++;
                break;
            case KING_SIDE_CASTLE:
            case QUEEN_SIDE_CASTLE:
                leafNodeCount.castles++;
                break;
        }

        if (move.getCapturedPieceType() != null) {
            leafNodeCount.captures++;
        }

        if (board.isChecked(board.colorToMove())) {
            leafNodeCount.checks++;
        }
    }

    private static class LeafNodeCount {
        int total;
        int checks;
        int captures;
        int enPassent;
        int promotions;
        int castles;
    }
}
