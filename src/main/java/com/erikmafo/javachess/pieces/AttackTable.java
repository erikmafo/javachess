package com.erikmafo.javachess.pieces;

import com.erikmafo.javachess.boardrep.BoardCoordinate;

import java.util.HashSet;
import java.util.Set;


public class AttackTable {

    private Set<BoardCoordinate> emptyTargets = new HashSet<>();
    private Set<BoardCoordinate> occupiedTargets = new HashSet<>();

    public void clear() {
        emptyTargets.clear();
        occupiedTargets.clear();
    }

    public void addEmptyTarget(BoardCoordinate target) {
        emptyTargets.add(target);
    }

    public void addOccupiedTarget(BoardCoordinate target) {
        occupiedTargets.add(target);
    }


    public Set<BoardCoordinate> getEmptyTargets() {
        return emptyTargets;
    }

    public Set<BoardCoordinate> getOccupiedTargets() {
        return occupiedTargets;
    }

    public boolean attacksPieceAt(BoardCoordinate boardCoordinate) {
        return occupiedTargets.contains(boardCoordinate);
    }

    public int size() {
        return emptyTargets.size() + occupiedTargets.size();
    }

    public boolean attacksSquareAt(BoardCoordinate coordinate) {
        return emptyTargets.contains(coordinate) || occupiedTargets.contains(coordinate);
    }
}
