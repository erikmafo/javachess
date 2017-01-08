package com.erikmafo.chess.gui.components;

public class BoardLocation {
    private final int file;
    private final int rank;

    public BoardLocation(int file, int rank) {
        this.file = file;
        this.rank = rank;
    }

    public int getFile() {
        return file;
    }

    public int getRank() {
        return rank;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BoardLocation)) return false;

        BoardLocation that = (BoardLocation) o;

        if (file != that.file) return false;
        return rank == that.rank;
    }

    @Override
    public int hashCode() {
        int result = file;
        result = 31 * result + rank;
        return result;
    }


    private final char[] files = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'};

    public String getName() {
        return "" + files[file] + (rank +1);
    }

    @Override
    public String toString() {
        return getName();
    }


}
