package com.erikmafo.javachess.uci;

import java.io.IOException;
import java.io.PrintStream;

/**
 * Created by erikf on 14.07.2016.
 */
public class UciOutput implements AutoCloseable {

    private final PrintStream printStream;
    private final Object mutex = new Object();

    public UciOutput(PrintStream printStream) {
        this.printStream = printStream;
    }

    public void write(String uciCommand) {
        synchronized (mutex) {
            printStream.println(uciCommand);
        }
    }



    @Override
    public void close() throws IOException {
        synchronized (mutex) {
            printStream.close();
        }
    }
}
