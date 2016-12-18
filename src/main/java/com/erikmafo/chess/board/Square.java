package com.erikmafo.chess.board;

/**
 * Represents a chess square.
 *
 * @author Erik Folstad
 */
public enum Square {

    OFF_BOARD(-1),
    A1(0), B1(1), C1(2), D1(3), E1(4), F1(5), G1(6), H1(7),
    A2(16), B2(17), C2(18), D2(19), E2(20), F2(21), G2(22), H2(23),
    A3(32), B3(33), C3(34), D3(35), E3(36), F3(37), G3(38), H3(39),
    A4(48), B4(49), C4(50), D4(51), E4(52), F4(53), G4(54), H4(55),
    A5(64), B5(65), C5(66), D5(67), E5(68), F5(69), G5(70), H5(71),
    A6(80), B6(81), C6(82), D6(83), E6(84), F6(85), G6(86), H6(87),
    A7(96), B7(97), C7(98), D7(99), E7(100), F7(101), G7(102), H7(103),
    A8(112), B8(113), C8(114), D8(115), E8(116), F8(117), G8(118), H8(119);

    private static final Square[] cachedValues = new Square[128];

    static {
        for (Square square : values()) {
            if (square != OFF_BOARD) {
                cachedValues[square.x88Value] = square;
            }

        }
    }

    private final int x88Value;
    private final int rank;
    private final int file;
    private final int index64;


    Square(int x88Value) {
        this.x88Value = x88Value;
        this.rank = x88Value / 16;
        this.file = x88Value % 16;
        this.index64 = 8 * rank + file;
    }

    /**
     * Returns whether there exists a square that results from applying the specified offset.
     *
     * @param offset
     * @return a boolean
     */
    public boolean hasNext(Offset offset) {
        return ((x88Value + offset.getValue()) & 0x88) == 0;
    }

    private int next(int x88sq, int x88Offset) {
        int next = x88sq + x88Offset;
        if ((next & 0x88) != 0) {
            return -1;
        }
        return next;
    }

    /**
     * Gets the 0x88 representation of this board.
     *
     * @return an integer between 0 and 119
     */
    public int getX88Value() {
        return x88Value;
    }

    /**
     * @return an integer between 0 and 63
     */
    public int getIndex64() { return index64; }

    /**
     * Gets the rank index of this square.
     *
     * @return an integer between 0 and 7
     */
    public int getRank() {
        return rank;
    }

    /**
     * Gets the file index of this square
     *
     * @return an integer between 0 and 7
     */
    public int getFile() {
        return file;
    }

    /**
     * Returns whether this square is between two specified.
     *
     * This is true if and only if a queen is able to slide between
     * these squares and would have to pass this square on the way.
     *
     * @param from a square
     * @param to a square
     * @return a boolean
     */
    public boolean isBetween(Square from, Square to) {
        if (this == from || this == to) {
            return false;
        }
        boolean isBetween = false;

        // on same rank or file?
        if (from.rank == to.rank && rank == from.rank) {
            isBetween = (file > from.file) ? (file < to.file) : (file > to.file);
        } else if (from.file == to.file && file == from.file) {
            isBetween = (rank > from.rank) ? (rank < to.rank) : (rank > to.rank);
        }
        // on same left or right diagonal?
        else if ((from.rank + from.file) == (to.rank + to.file)
                && (from.rank + from.file) == (rank + file)) {
            // same up left diagonal
            isBetween = (file > from.file) ? (file < to.file) : (file > to.file);
        } else if ((from.rank - from.file) == (to.rank - to.file)
                && (from.rank - from.file) == (rank - file)) {
            isBetween = (file > from.file) ? (file < to.file) : (file > to.file);
        }

        return isBetween;
    }


    /**
     * Returns the corresponding Square.
     *
     * @param fileIndex - an integer between 0 and 7
     * @param rankIndex - an integer between 0 and 7
     * @return - a board coordinate
     * @throws IllegalArgumentException if the fileIndex or rankIndex is out of range
     */
    public static Square valueOf(int fileIndex, int rankIndex) {
        if (rankIndex < 0 || rankIndex > 7 || fileIndex < 0 || fileIndex > 7) {
            throw new IllegalArgumentException();
        }
        return cachedValues[16 * rankIndex + fileIndex];
    }


    /**
     * Returns the unique <code>Square</code> that results from applying the specified {@link Offset} to
     * this square.
     *
     * @param offset the offset to be applied
     * @return a square, or <literal>OFF_BOARD</literal>
     */
    public Square next(Offset offset) {
        return next(offset, 1);
    }

    /**
     * Applies the offset a specified number of times to this square.
     * <br/>
     * If the second argument is negative the offset is applied in the opposite direction.
     *
     * @param offset the offset to apply to this square
     * @param times  an integer between -7 and 7
     * @return the square that results from applying an offset a given number of times, or <literal>OFF_BOARD</literal>
     */
    public Square next(Offset offset, int times) {
        int newIndex;
        if ((newIndex = next(x88Value, times * offset.getValue())) > -1) {
            return cachedValues[newIndex];
        }
        return OFF_BOARD;
    }


    /**
     * Returns whether this is a valid square.
     *
     * @return true if this square is not equal to <code>OFF_BOARD</code> and false otherwise
     */
    public boolean isOnBoard() {
        return this != OFF_BOARD;
    }

}
