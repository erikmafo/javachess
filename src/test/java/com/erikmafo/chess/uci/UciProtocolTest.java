package com.erikmafo.chess.uci;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by erikmafo on 03.12.16.
 */
public class UciProtocolTest {

    private final UciEngine engine = mock(UciEngine.class);
    private final UciOutput output = mock(UciOutput.class);
    private UciProtocol uciProtocol;


    @Before
    public void setup() throws Exception {
        uciProtocol = new UciProtocol(engine, output);
    }

    @Test
    public void uci() throws Exception {

        String name = "simplechess";
        String author = "Erik";

        UciEngineDescription uciEngineDescription = new UciEngineDescription(name, author, new ArrayList<>());

        when(engine.uci()).thenReturn(uciEngineDescription);

        uciProtocol.processInput("uci");

        verify(output).write("uciok");
        verify(output).write("id name " + name);
        verify(output).write("id author " + author);
    }

    @Test
    public void isReady() throws Exception {

        when(engine.isReady()).thenReturn(true);

        uciProtocol.processInput("isready");

        verify(output).write("readyok");
    }


    @Test
    public void uciNewGame() throws Exception {

        uciProtocol.processInput("ucinewgame");

        verify(engine).uciNewGame();

    }

    @Test
    public void positionWithStartPosition() throws Exception {
        uciProtocol.processInput("position startpos");
        verify(engine).position("startpos");
    }

    @Test
    public void positionWithMoves() throws Exception {
        uciProtocol.processInput("position startpos e2e4 e7e5");
        verify(engine).position("startpos", "e2e4", "e7e5");
    }

    @Test
    public void go() throws Exception {

        CompletableFuture<UciSearchResult> future = new CompletableFuture<>();
        when(engine.go(any(Search.class), any(SearchProgressListener.class))).thenReturn(future);
        uciProtocol.processInput("go");

        future.complete(new UciSearchResult("e2e4", "d2d4"));

        verify(output).write("bestmove e2e4 ponder d2d4");

    }

    @Test
    public void goResponseWithoutPonder() throws Exception {

        CompletableFuture<UciSearchResult> future = new CompletableFuture<>();
        when(engine.go(any(Search.class), any(SearchProgressListener.class))).thenReturn(future);
        uciProtocol.processInput("go");

        future.complete(new UciSearchResult("e2e4", null));

        verify(output).write("bestmove e2e4");

    }


    @Test
    public void stop() throws Exception {

        uciProtocol.processInput("stop");

        verify(engine).stop();

    }

    @Test
    public void quit() throws Exception {

        uciProtocol.processInput("quit");

        verify(engine).quit();

    }

    @Test
    public void ponderHit() throws Exception {

        uciProtocol.processInput("ponderhit");

        verify(engine).ponderHit();

    }

    @Test
    public void setOption() throws Exception {

        uciProtocol.processInput("setoption name Style value Risky");

        verify(engine).setOption("Style", "Risky");

    }

}