package com.erikmafo.javachess.search;

import com.erikmafo.javachess.board.Board;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by erikmafo on 04.12.16.
 */
public class BoardToIntFunctionChain implements BoardToIntFunction{


    private final List<BoardToIntFunction> functions;


    public BoardToIntFunctionChain(List<BoardToIntFunction> functions) {
        this.functions = new ArrayList<>(functions);
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


    public static class Builder {

        private final List<BoardToIntFunction> functions = new ArrayList<>();


        public Builder addFunction(BoardToIntFunction function) {
            functions.add(function);
            return this;
        }

        public BoardToIntFunction build() {
            return new BoardToIntFunctionChain(functions);
        }


    }

}
