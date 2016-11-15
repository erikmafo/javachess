package com.erikmafo.javachess.boardrep;

import com.erikmafo.javachess.pieces.PieceColor;
import com.erikmafo.javachess.pieces.PieceType;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


public enum Offset {

    UP(16),
    UP_RIGHT(17),
    RIGHT(1),
    DOWN_RIGHT(-15),
    DOWN(-16),
    DOWN_LEFT(-17),
    LEFT(-1),
    UP_LEFT(15),
    KNIGHT_LEAP_2UP_LEFT(31),
    KNIGHT_LEAP_2UP_RIGHT(33),
    KNIGHT_LEAP_UP_2RIGHT(18),
    KNIGHT_LEAP_UP_2LEFT(14),
    KNIGHT_LEAP_2DOWN_RIGHT(-31),
    KNIGHT_LEAP_2DOWN_LEFT(-33),
    KNIGHT_LEAP_DOWN_2LEFT(-18),
    KNIGHT_LEAP_DOWN_2RIGHT(-14);

    private static final Set<Offset> QUEEN_ATTACK_OFFSETS;
    private static final Set<Offset> ROOK_ATTACK_OFFSETS;
    private static final Set<Offset> BISHOP_ATTACK_OFFSETS;
    private static final Set<Offset> KING_ATTACK_OFFSETS;
    private static final Set<Offset> WHITE_PAWN_ATTACK_OFFSETS;
    private static final Set<Offset> BLACK_PAWN_ATTACK_OFFSETS;
    private static final Set<Offset> KNIGHT_ATTACK_OFFSETS;


    static {
        Offset[] temp = {KNIGHT_LEAP_2UP_LEFT,
                KNIGHT_LEAP_2UP_RIGHT,
                KNIGHT_LEAP_UP_2RIGHT, KNIGHT_LEAP_UP_2LEFT,
                KNIGHT_LEAP_2DOWN_RIGHT, KNIGHT_LEAP_2DOWN_LEFT, KNIGHT_LEAP_DOWN_2LEFT, KNIGHT_LEAP_DOWN_2RIGHT};
        KNIGHT_ATTACK_OFFSETS =
                Collections.unmodifiableSet(new HashSet<>(Arrays.asList(temp)));
        QUEEN_ATTACK_OFFSETS = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(
                UP, UP_RIGHT, RIGHT, DOWN_RIGHT, DOWN, DOWN_LEFT, LEFT, UP_LEFT)));

        KING_ATTACK_OFFSETS = QUEEN_ATTACK_OFFSETS;

        ROOK_ATTACK_OFFSETS = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(UP, RIGHT, DOWN, LEFT)));

        BISHOP_ATTACK_OFFSETS = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(
                UP_RIGHT, DOWN_RIGHT, DOWN_LEFT, UP_LEFT)));

        BLACK_PAWN_ATTACK_OFFSETS = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(DOWN_LEFT, DOWN_RIGHT)));

        WHITE_PAWN_ATTACK_OFFSETS = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(UP_LEFT, UP_RIGHT)));

    }


    private final int value;

    Offset(int value) {
        this.value = value;
    }

    int getValue() {
        return value;
    }


    public static Set<Offset> getOffsets(PieceColor pieceColor, PieceType pieceType) {
        Set<Offset> offsets = null;
        switch (pieceType) {
            case BISHOP:
                offsets = BISHOP_ATTACK_OFFSETS;
                break;
            case KNIGHT:
                offsets = KNIGHT_ATTACK_OFFSETS;
                break;
            case ROOK:
                offsets = ROOK_ATTACK_OFFSETS;
                break;
            case QUEEN:
                offsets = QUEEN_ATTACK_OFFSETS;
                break;
            case KING:
                offsets = KING_ATTACK_OFFSETS;
                break;
            case PAWN:
                return pieceColor == PieceColor.WHITE ? WHITE_PAWN_ATTACK_OFFSETS :
                        BLACK_PAWN_ATTACK_OFFSETS;
        }
        return offsets;
    }


    public static Set<Offset> getWhitePawnAttackOffsets() {
        return WHITE_PAWN_ATTACK_OFFSETS;
    }

    public static Set<Offset> getBlackPawnAttackOffsets() {
        return BLACK_PAWN_ATTACK_OFFSETS;
    }

    public static Set<Offset> getKnightAttackOffsets() {
        return KNIGHT_ATTACK_OFFSETS;
    }

    public static Set<Offset> getQueenAttackOffsets() {
        return QUEEN_ATTACK_OFFSETS;
    }

    public static Set<Offset> getRookAttackOffsets() {
        return ROOK_ATTACK_OFFSETS;
    }

    public static Set<Offset> getBishopAttackOffsets() {
        return BISHOP_ATTACK_OFFSETS;
    }

    public static Set<Offset> getKingAttackOffsets() {
        return KING_ATTACK_OFFSETS;
    }
}
