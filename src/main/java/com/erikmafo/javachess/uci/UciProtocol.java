package com.erikmafo.javachess.uci;

import com.erikmafo.javachess.parser.FenParseException;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;


/**
 * Created by erikf on 14.07.2016.
 */
public class UciProtocol {

    public static class GUI2ENGINE {

        public static final String UCI = "uci";
        public static final String IS_READY = "isready";
        public static final String UCI_NEW_GAME = "ucinewgame";
        public static final String POSITION = "position";
        public static final String GO = "go";
        public static final String PONDER_HIT = "ponderhit";
        public static final String STOP = "stop";
        public static final String QUIT = "quit";
        public static final String SET_OPTION = "setoption";

    }

    private final UciEngine uciEngine;
    private final UciOutput uciOutput;

    public UciProtocol(UciEngine uciEngine, UciOutput uciOutput) {
        this.uciEngine = uciEngine;
        this.uciOutput = uciOutput;
    }

    public void processInput(String uciCommand) throws UciProtocolException{

        String[] strArray = uciCommand.split(" ");

        String command = strArray[0];

        String[] args = Arrays.copyOfRange(strArray, 1, strArray.length);

        if (GUI2ENGINE.UCI.equals(command)) {
            handleUci(uciOutput);
        } else if (GUI2ENGINE.IS_READY.equals(command)) {
            handleIsReady(uciOutput);
        } else if (GUI2ENGINE.UCI_NEW_GAME.equals(command)) {
            uciEngine.uciNewGame();
        } else if (GUI2ENGINE.POSITION.equals(command)) {
            handlePosition(uciOutput, args);
        } else if (GUI2ENGINE.GO.equals(command)) {
            handleGo(uciOutput, args);
        } else if (GUI2ENGINE.PONDER_HIT.equals(command)) {
            uciEngine.ponderHit();
        } else if (GUI2ENGINE.STOP.equals(command)) {
            uciEngine.stop();
        } else if (GUI2ENGINE.QUIT.equals(command)) {
            uciEngine.quit();
        } else if (GUI2ENGINE.SET_OPTION.equals(command)) {
            handleSetOption(args);
        }

    }

    private void handleSetOption(String[] args) throws UciProtocolException {
        if (args.length < 4) {
            throw new UciProtocolException("Unable to handle set option. To few arguments.");
        }
        if ("name".equalsIgnoreCase(args[0]) && "value".equalsIgnoreCase(args[2])) {
            uciEngine.setOption(args[1], args[3]);
        } else {
            throw new UciProtocolException("settoption arguments should be formatted as: name <name> value <value>");
        }
    }

    private void handleUci(UciOutput uciOutput) {
        UciEngineDescription engineDescription = uciEngine.uci();
        uciOutput.write("uciok");
        uciOutput.write("id name " + engineDescription.getName());
        uciOutput.write("id author " + engineDescription.getAuthor());
    }

    private void handleIsReady(UciOutput uciOutput) {

        if (uciEngine.isReady())  {
            uciOutput.write("readyok");
        }
    }

    private void handlePosition(UciOutput uciOutput, String[] args) {
        try {
            uciEngine.position(args[0], Arrays.copyOfRange(args, 1, args.length));
        } catch (FenParseException e) {
            uciOutput.write("info invalid fen string");
        }
    }

    private void handleGo(UciOutput uciOutput, String[] args) {

        Search search = new Search.Builder().build();

        CompletableFuture<UciSearchResult> future = uciEngine
                .go(search, searchInfo -> uciOutput.write("info " + searchInfo));

        future.whenComplete((uciSearchResult, throwable) -> {
            String rsp = "bestmove " + uciSearchResult.bestMove();
            if (uciSearchResult.ponder() != null) {
                rsp += " ponder " + uciSearchResult.ponder();
            }
            uciOutput.write(rsp);
        });

    }


}
