package com.erikmafo.javachess.board;


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
                cachedValues[square.index] = square;
            }

        }
    }

    // data
    private final int index;
    private final int rank;
    private final int file;


    Square(int index) {
        this.index = index;
        this.rank = index / 16;
        this.file = index % 16;
    }

    public boolean hasNext(Offset direction) {
        return ((index + direction.getValue()) & 0x88) == 0;
    }

    private int next(int x88sq, int x88Offset) {
        int next = x88sq + x88Offset;
        if ((next & 0x88) != 0 ) {
            return -1;
        }
        return next;
    }


    public int getRank() {
        return rank;
    }

    public int getFile() {
        return file;
    }

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
     * the current square.
     *
     * @param offset
     * @return a square, or <code>OFF_BOARD</code>
     */
    public Square next(Offset offset) {
        int newIndex;
        if ((newIndex = next(index, offset.getValue())) > -1) {
            return cachedValues[newIndex];
        }
        return OFF_BOARD;
    }

    public boolean isOnBoard() {
        return this != OFF_BOARD;
    }

}
