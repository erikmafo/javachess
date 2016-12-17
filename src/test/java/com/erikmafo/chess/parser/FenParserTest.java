package com.erikmafo.chess.parser;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by erikmafo on 04.12.16.
 */
@RunWith(JUnitParamsRunner.class)
public class FenParserTest {


    private FenParser fenParser = new FenParser();


    private Object[] validFenStrings() {
        return new Object[]{
                "rnbqkbnr/pp1ppppp/8/2p5/4P3/8/PPPP1PPP/RNBQKBNR w KQkq c6 0 2"
        };
    }

    @Test
    @Parameters(method = "validFenStrings")
    public void parsingValidFenShouldNotResultInException(String fen) throws Exception {

        fenParser.parse(fen);


    }
}