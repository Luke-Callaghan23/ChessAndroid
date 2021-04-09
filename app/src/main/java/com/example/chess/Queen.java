package com.example.chess;

/**
 * 
 * @author Luke Callaghan -- ljc170
 * @author Rahul Hebbar   -- rjh242
 * 
 */

import android.graphics.Point;
import android.os.Build;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;

/**
 * 
 * Class for the Queen Piece
 *
 */
public class Queen extends Piece 
{
	/**
	 * Because a Queen's moveset is just a Rook's moveset combined with a Bishop's
	 * moveset, each Queen has its own Rook + Bishop and uses them to determine
	 * it's moveset by combining the moveset of the two of them.
	 */
	/**
	 * A Queen's cardinal moveset is the same a Rook's moveset
	 */
	private Rook cardinal;
	/**
	 * A Queen's cross moveset is the same a Bishop's moveset
	 */
	private Bishop  cross;
	/**
	 * 
	 * Constructs a Queen by calling Piece super-constructor
	 * 
	 * @param color		'w'/'b' depending if the piece is white or black
	 * @param location	Point location of this piece
	 */
	public Queen(char color, Point location, ImageView iv)
	{ 
		super(color, location, iv);
		cardinal = new Rook(this.color, this.location, null);
		cross    = new Bishop(this.color, this.location, null);
	}

	/**
	 * 
	 * Gets a list of all the valid moves for this Queen
	 * 
	 * @return 		ArrayList of all valid moves for this Queen
	 */
	@RequiresApi(api = Build.VERSION_CODES.N)
	public ArrayList<Point> getMoves(Board board)
	{
		//Since a Queen's moves are just a rooks moves + a bishop's moves,
		//      moves location of tempBish/tempRook to current position
		cardinal.setLocation(this.location);
		cross.setLocation(this.location);

		//Adds moveset of bishop and rook to one list
		ArrayList<Point> moves = cardinal.getMoves(board);
		moves.addAll(cross.getMoves(board));

		//returns list
		return moves;
	}

	/**
	 * Gets the string interpretation of this Queen
	 * 
	 * @return 		String interpretation of this Queen
	 */
	public String toString() 
	{
		return color + "Q";
	}


	public Piece copy()
	{
		return new Queen(this.getColor(), this.getLocation(), null);
	}

}
