package com.example.chess;

import android.graphics.Point;

import java.io.Serializable;

public class Move implements Serializable
{
    private Piece moved;
    private Piece overwritten;
    private Point moveFrom;
    private Point moveTo;

    public Piece getMoved() {
        return moved;
    }

    public void setMoved(Piece moved) {
        this.moved = moved;
    }

    public Piece getOverwritten() {
        return overwritten;
    }

    public void setOverwritten(Piece overwritten) {
        this.overwritten = overwritten;
    }

    public Point getMoveFrom() {
        return moveFrom;
    }

    public void setMoveFrom(Point moveFrom) {
        this.moveFrom = moveFrom;
    }

    public Point getMoveTo() {
        return moveTo;
    }

    public void setMoveTo(Point moveTo) {
        this.moveTo = moveTo;
    }


    public Move(Piece moved, Piece overwritten, Point moveFrom, Point moveTo)
    {
        this.moved = moved;
        this.overwritten = overwritten;
        this.moveFrom = moveFrom;
        this.moveTo = moveTo;
    }

    public String toString()
    {
        return "" + moveFrom.x +  "," + moveFrom.y + ";" + moveTo.x + "," + moveTo.y;
    }

}
