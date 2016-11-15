package com.erikmafo.javachess.pieces;


import com.erikmafo.javachess.boardrep.BoardCoordinate;
import com.erikmafo.javachess.boardrep.ReadableBoard;
import com.erikmafo.javachess.moves.Move;
import com.erikmafo.javachess.moves.MoveGenerator;
import com.erikmafo.javachess.moves.MoveGenerators;

import java.util.List;


public class Piece {

    private BoardCoordinate coordinate;
    private MoveGenerator moveGenerator;
    private AttackTable attackTable;
    private boolean isCaptured;

    private PieceColor pieceColor;
    private PieceType pieceType;


    public void capture() {
        isCaptured = true;
    }

    public void unCapture() {
        isCaptured = false;
    }


    public boolean isCaptured() {
        return isCaptured;
    }

    public Piece(PieceColor pieceColor, PieceType pieceType, BoardCoordinate coordinate) {
        this.pieceColor = pieceColor;
        this.pieceType = pieceType;
        this.coordinate = coordinate;
        moveGenerator = MoveGenerators.valueOf(pieceColor, pieceType);
        attackTable = new AttackTable();
    }

    public BoardCoordinate getCoordinate() {
        return coordinate;
    }

    public PieceColor getPieceColor() {
        return pieceColor;
    }

    public PieceType getPieceType() {
        return pieceType;
    }


    public void move(BoardCoordinate newCoordinate) {
        this.coordinate = newCoordinate;
    }

    public void moveBackwards(BoardCoordinate oldCoordinate) {
        this.coordinate = oldCoordinate;
    }

    public void setPieceType(PieceType newPieceType) {
        pieceType = newPieceType;
        moveGenerator = MoveGenerators.valueOf(pieceColor, newPieceType);
    }

    public void findPossibleMoves(ReadableBoard readableBoard, List<Move> moves) {
        //moveGenerator.findAttackSquares(readableBoard, coordinate, attackTable);
        moveGenerator.findPossibleMoves(readableBoard, coordinate, attackTable, moves);
    }

    public void updateAttackTable(ReadableBoard board) {
        moveGenerator.findAttackSquares(board, coordinate, attackTable);
    }

    public boolean attacksPieceAt(BoardCoordinate kingLocation) {
        return attackTable.attacksPieceAt(kingLocation);
    }

    public int getNumberOfAttacks() {
        return attackTable.size();
    }

    public boolean attacksSquareAt(BoardCoordinate coordinate) {
        return attackTable.attacksSquareAt(coordinate);
    }
}






