package com.erikmafo.chess.movegenerator;

import com.erikmafo.chess.board.Board;
import com.erikmafo.chess.move.Move;
import org.jetbrains.annotations.Contract;

import java.util.List;

/**
 * Created by erikmafo on 11.12.16.
 */
public interface MoveGenerator {


    /**
     * Generates moves for the active color on this {@link Board}.
     *
     * @param board
     * @return
     */
    @Contract(value = "null -> fail", pure = true)
    List<Move> generateMoves(Board board);



}
