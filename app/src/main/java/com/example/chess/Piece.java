package com.example.chess;
/**
 * 
 * @author Luke Callaghan -- ljc170
 * @author Rahul Hebbar   -- rjh242
 * 
 */
import android.graphics.Point;
import android.view.View;
import android.widget.ImageView;

import com.example.chess.ui.home.BoardFragment;

import java.util.ArrayList;
import java.util.function.BiFunction;
import java.util.function.Predicate;



/**
 * 
 * Abstract Piece class that defines the behaviors of all Piece subclasses
 *
 */
public abstract class Piece 
{
	protected char color;
	
	/**
	 * Color getter
	 * 
	 * @return piece's color
	 * 
	 */
	public char getColor() 
	{
		return color;
	}

	/**
	 * Color setter
	 * 
	 * @param color		piece's new color
	 * 
	 */
	public void setColor(char color) 
	{
		this.color = color;
	}

	/**
	 * Location getter
	 * 
	 * @return piece's location
	 * 
	 */
	public Point getLocation()
	{
		return location;
	}

	/**
	 * Location getter
	 * 
	 * @param location	new piece's location
	 * 
	 */
	public void setLocation(Point location) 
	{
		this.location = location;
	}

	private ImageView iv;




	protected Point location;
	/**
	 * Constructs a new Piece
	 * 
	 * @param color		char color 'w'/'b'/' '
	 * @param location	Point current location of piece
	 */
	public Piece(char color, Point location, ImageView iv)
	{
		this.color = color;
		this.location = location;
		this.iv = iv;
	}
	
	/**
	 * Checks if a given point is within the range of the chess board
	 * 		( 0 <= x and y <= 7)
	 * 
	 * @param p		Point we are checking
	 * @return boolean indicating whether the given point was in range or not
	 */
	protected static Predicate<Point> inRange = p -> 
		p.x >= 0 && p.x <= 7 && p.y >= 0 && p.y <= 7;
		
		
	/**
	 * Checks if input piece one can beat input piece two
	 * 
	 * @param self 		piece one
	 * @param other 	piece two
	 * @return 			boolean indicating whether piece one can beat piece two	
	 */
	protected BiFunction<Piece, Piece, Boolean> beats = (self, other) ->
		self.color != other.color;
	
	
	
	/**
	 * Gets the list of Points that this piece can move to
	 * 		Overridden by all sub-classes
	 * @return List of Points that piece can move to
	 */
	public ArrayList<Point> getMoves()
	{
		return getMoves(BoardFragment.b);
	}



	public abstract Piece copy();
	public abstract ArrayList<Point> getMoves(Board board);

	/**
	 * Gets String interpretation of a piece
	 * @return String interpretation of piece
	 */
	public abstract String toString();

	public View getView()
	{
		return this.iv;
	}


}
