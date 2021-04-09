package com.example.chess;

import android.graphics.Point;

import java.util.ArrayList;

public class Empty extends Piece
{

    /**
     * Constructs an empty piece, by calling super constructor
     * @param location		location of piece
     */
    public Empty(Point location) { super(' ', location, null); }
    /**
     * Blank cannot move, return null;
     */
    public ArrayList<Point> getMoves(Board b) { return new ArrayList<Point>(); }
    /**
     * Returns string
     */
    public String toString()
    {
        if((location.x + location.y) % 2 == 0)
        {
            return "  ";
        }
        return "##";
    }

    public Piece copy()
    {
        return new Empty(this.getLocation());
    }

}
