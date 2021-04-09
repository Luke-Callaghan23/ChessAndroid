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

import com.example.chess.ui.home.BoardFragment;

import java.util.ArrayList;

/**
 * 
 * Class for the Pawn Piece
 *
 */
public class Pawn extends Piece 
{

	public Point passant;


	/**
	 * 
	 * Constructs a Pawn by calling Piece super-constructor
	 * 
	 * @param color		'w'/'b' depending if the piece is white or black
	 * @param location	Point location of this piece
	 */
	public Pawn(char color, Point location, ImageView iv) { super(color, location, iv); }

	/**
	 * Boolean indicating whether or not this pawn has moved before.  Used for
	 * determining if this pawn can move forward 2 tiles or only one.
	 */
	private boolean moved = false;

	
	/**
	 * 
	 * Gets a list of all the valid moves for this Pawn
	 * 
	 * @return 		ArrayList of all valid moves for this Pawn
	 */
	@RequiresApi(api = Build.VERSION_CODES.N)
	public ArrayList<Point> getMoves(Board board)
	{

		passant = null;

		ArrayList<Point> moves = new ArrayList<Point>();

		//gets the direction in the row-axis that this pawn moves (white -> -1, black -> 1)
		int rDir = (this.color == 'w') ? -1 : 1;

		
		
		//variables for target point/piece
		Point targPoint; 
		Piece targPiece;

		//Checking tile in front of pawn
		targPoint = new Point(this.location.x + (1 * rDir), this.location.y);
		if (inRange.test(targPoint))
		{
			targPiece = board.getPiece(targPoint);
			if (targPiece instanceof Empty)
			{
				moves.add(targPoint);

				//Checking tile two steps ahead of pawn (if this pawn has never moved)
				if(!moved)
				{
					targPoint = new Point(this.location.x + (2 * rDir), this.location.y);
					if (inRange.test(targPoint))
					{

						targPiece = board.getPiece(targPoint);
						if(targPiece instanceof Empty)
						{
							moves.add(targPoint);
						}
					}
				}
			}
		}
		//Checking tile diagonally right and left from this pawn
		//		loop 0 -> left
		//		loop 1 -> right
		for(int loop = 0; loop < 2; loop++)
		{
			//calculating column direction
			int cDir = (loop == 0) ? -1 : 1;

			targPoint = new Point(this.location.x + (1 * rDir), this.location.y + (1 * cDir));
			if(inRange.test(targPoint))
			{
				targPiece = board.getPiece(targPoint);
				//checks if targPiece is enemy
				if(this.getColor() != targPiece.getColor() && targPiece.getColor() != ' ')
				{
					moves.add(targPoint);
				}
			}
		}
		
		//If the last move was a double move by a pawn,
		//		check if we can en passant
		if(MainActivity.bf.lastDoubleMoved != null)
		{
			Point loc  = this.getLocation();
			Point oLoc = MainActivity.bf.lastDoubleMoved.getLocation();

			if(oLoc.x == loc.x && Math.abs(oLoc.y - loc.y) == 1)
			{
				Point up = new Point(loc.x + 1 * rDir, loc.y + (oLoc.y - loc.y));
				if(board.getPiece(up) instanceof Empty)
				{
					passant = up;
					moves.add(up);
				}
			}
		}
		
		return moves;
	}

	/**
	 * Gets the string interpretation of this Pawn
	 * 
	 * @return 		String interpretation of this Pawn
	 */
	public String toString() 
	{
		return color + "p";
	}

	public int getMovedTurn()
	{
		return movedTurn;
	}

	private int movedTurn;

	/**
	 * Sets the boolean indicating whether this Pawn has moved before
	 * 
	 * @param moved		value that this.moved will be set to
	 */
	public void setMoved(boolean moved) 
	{
		this.movedTurn = moved ? MainActivity.bf.turn : -1;
		this.moved = moved;
	}


	public Piece copy()
	{
		Pawn ret =  new Pawn(this.getColor(), this.getLocation(), null);
		ret.passant = passant != null ? new Point(passant.x, passant.y) : null;
		ret.setMoved(moved);
		return ret;
	}

}
