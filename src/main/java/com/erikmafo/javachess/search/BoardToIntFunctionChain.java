package com.erikmafo.javachess.search;

import com.erikmafo.javachess.board.Board;

import java.util.Arrays;
import java.util.List;

/**
 * Created by erikmafo on 04.12.16.
 */
public class BoardToIntFunctionChain implements BoardToIntFunction{


    private final List<BoardToIntFunction> functions;


    public BoardToIntFunctionChain(List<BoardToIntFunction> functions) {
        this.functions = functions;
    }

    public BoardToIntFunctionChain(BoardToIntFunction... functions) {
        this.functions = Arrays.asList(functions);
    }


    @Override
    public int applyAsInt(Board board) {

        int score = 0;

        for (BoardToIntFunction function : functions) {
            score += function.applyAsInt(board);
        }

        return score;
    }
}
