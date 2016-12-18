package com.erikmafo.chess.uci;

import com.erikmafo.chess.utils.parser.FenParseException;

import java.util.concurrent.CompletableFuture;

/**
 * Created by erikf on 13.07.2016.
 */
public interface UciEngine {


    UciEngineDescription uci();


    /**
     * Switch the debug mode of the engine on and off.
     * In debug mode the engine should sent additional infos to the GUI, e.g. with the "info string" command,
     * to help debugging, e.g. the commands that the engine has received etc.
     * This mode should be switched off by default and this command can be sent
     * any time, also when the engine is thinking.
     */
    void debug(boolean d);


    /**
     * This is used to synchronize the engine with the GUI. When the GUI has sent a command or
     * multiple commands that can take some time to complete,
     * this command can be used to wait for the engine to be ready again or
     * to ping the engine to find out if it is still alive.
     * E.g. this should be sent after setting the path to the tablebases as this can take some time.
     * This command is also required once before the engine is asked to do any search
     * to wait for the engine to finish initializing.
     * This command must always be answered with "readyok" and can be sent also when the engine is calculating
     * in which case the engine should also immediately answer with "readyok" without stopping the search.
     */
    boolean isReady();

    /**
     * Should be called when the user wants to change the internal parameters of the engine. For the
     * "button" type no value is needed.
     * <p>
     * One string will be sent for each parameter and this will only be sent when the engine is waiting.
     * <p>
     * The name of the option in  should not be case sensitive and can inludes spaces like also the value.
     * <p>
     * The substrings "value" and "name" should be avoided in  and  to allow unambiguous parsing, for example
     * do not use  = "draw value".
     * <p>
     * Here are some strings for the example below:
     * "setoption name Nullmove value true\n"
     * "setoption name Selectivity value 3\n"
     * "setoption name Style value Risky\n"
     * "setoption name Clear Hash\n"
     * "setoption name NalimovPath value c:\chess\tb\4;c:\chess\tb\5\n"
     *
     * @param name
     * @param value
     */
    void setOption(String name, String value);

    void uciNewGame();

    void position(String fenOrStartPosition, String... moves) throws FenParseException;

    CompletableFuture<UciSearchResult> go(Search search, SearchProgressListener listener);

    void stop();

    void ponderHit();

    void quit();


}
