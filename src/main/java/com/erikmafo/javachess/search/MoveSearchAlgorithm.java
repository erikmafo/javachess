package com.erikmafo.javachess.search;

import com.erikmafo.javachess.boardrep.PlayableBoard;
import com.erikmafo.javachess.moves.Move;


public abstract class MoveSearchAlgorithm {

    public abstract Move execute(PlayableBoard board, EvaluationFunction evaluationFunction, int depth);

}
