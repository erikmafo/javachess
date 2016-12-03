package com.erikmafo.javachess.movegenerator;

/**
 * Created by erikmafo on 03.12.16.
 */
public class MoveGeneratorFactory {


    public MoveGenerator newInstance(MoveGenerationStrategy strategy) {

        return MoveGenerators.newPseudoLegalMoveGenerator();

    }


}
