package com.erikmafo.javachess.boardrep;


public enum BoardCoordinate {

    OFF_BOARD(-1),
    A1(0), B1(1), C1(2), D1(3), E1(4), F1(5), G1(6), H1(7),
    A2(16), B2(17), C2(18), D2(19), E2(20), F2(21), G2(22), H2(23),
    A3(32), B3(33), C3(34), D3(35), E3(36), F3(37), G3(38), H3(39),
    A4(48), B4(49), C4(50), D4(51), E4(52), F4(53), G4(54), H4(55),
    A5(64), B5(65), C5(66), D5(67), E5(68), F5(69), G5(70), H5(71),
    A6(80), B6(81), C6(82), D6(83), E6(84), F6(85), G6(86), H6(87),
    A7(96), B7(97), C7(98), D7(99), E7(100), F7(101), G7(102), H7(103),
    A8(112), B8(113), C8(114), D8(115), E8(116), F8(117), G8(118), H8(119);

    private static final BoardCoordinate[] cachedValues = new BoardCoordinate[128];

    static {
        for (BoardCoordinate boardCoordinate : values()) {
            if (boardCoordinate != OFF_BOARD) {
                cachedValues[boardCoordinate.index] = boardCoordinate;
            }

        }
    }

    // data
    private final int index;
    private final int rank;
    private final int file;


    BoardCoordinate(int index) {
        this.index = index;
        this.rank = index / 16;
        this.file = index % 16;
    }

    public boolean hasNext(Offset direction) {
        return (index + direction.getValue() & 0x88) == 0;
    }


    public int getRank() {
        return rank;
    }

    public int getFile() {
        return file;
    }

    public boolean isBetween(BoardCoordinate from, BoardCoordinate to) {
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


    public static BoardCoordinate valueOf(int file, int rank) {
        if (rank < 0 || rank > 7 || file < 0 || file > 7) {
            return OFF_BOARD;
        }
        return cachedValues[16 * rank + file];
    }


    public BoardCoordinate next(Offset offset) {
        int newIndex = index + offset.getValue();
        if (!hasNext(offset)) {
            return OFF_BOARD;
        }
        return cachedValues[newIndex];
    }

    public boolean isOnBoard() {
        return this != OFF_BOARD;
    }

}
