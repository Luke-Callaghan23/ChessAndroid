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
 * Class for the Rook Piece
 *
 */
public class Rook extends Piece
{
	/**
	 * Value indicating whether this piece has moved before
	 */
	private boolean moved;
	
	/**
	 * 
	 * Constructs a Rook by calling Piece super-constructor
	 * 
	 * @param color		'w'/'b' depending if the piece is white or black
	 * @param location	Point location of this piece
	 */
	public Rook(char color, Point location, ImageView iv) { super(color, location, iv); }

	/**
	 * 
	 * Gets a list of all the valid moves for this Rook
	 * 
	 * @return 		ArrayList of all valid moves for this Rook
	 */
	@RequiresApi(api = Build.VERSION_CODES.N)
	public ArrayList<Point> getMoves(Board board)
	{
		//List of moves to be returned
		ArrayList<Point> moves = new ArrayList<Point>();


		//Iterator items
		Point curPoint;
		Piece curPiece;

		//Row and collumn of this piece
		int row = this.location.x,
			col = this.location.y;

		//If stops[n] == true, stop going in a certain direction
		boolean[] stops;

		//y-axis
		stops = new boolean[2];
		for (int rMod = 1; rMod < 8; rMod++)
		{
			//if stop == 0, check positive direction
			//else, check negative direction
			for (int stop = 0; stop < 2; stop++)
			{
				//if stops[stop] = true, don't check; stop
				if (!stops[stop])
				{
					int dir = (stop == 0) ? 1 : -1;
					curPoint = new Point(row + rMod * dir, col);
					if(inRange.test(curPoint))
					{
						curPiece = board.getPiece(curPoint);
						if(beats.apply(this, curPiece))
						{
							moves.add(curPoint);
							stops[stop] = (curPiece.getColor() != ' ');
						}
						else
						{
							stops[stop] = true;
						}
					}
					else
					{
						stops[stop] = true;
					}
				}
			}
		}
		//x-axis
		stops = new boolean[2];
		for (int cMod = 1; cMod < 8; cMod++)
		{
			//if stop == 0, check positive direction
			//else, check negative direction
			for (int stop = 0; stop < 2; stop++)
			{
				//if stops[stop] == 0, check positive direction
				//else, check negative direction
				if (!stops[stop])
				{
					int dir = (stop == 0) ? 1 : -1;
					curPoint = new Point(row, col + cMod * dir);
					if (inRange.test(curPoint))
					{
						curPiece = board.getPiece(curPoint);
						if (beats.apply(this, curPiece))
						{
							moves.add(curPoint);
							stops[stop] = (curPiece.getColor() != ' ');
						}
						else
						{
							stops[stop] = true;
						}
					}
					else
					{
						stops[stop] = true;
					}
				}
			}
		}
		return moves;
	}

	
	/**
	 * Moved getter
	 * 
	 * @return		boolean indicating whether this piece has been moved or not
	 */
	public boolean getMoved()
	{
		return this.moved;
	}
	
	/**
	 * Moved setter
	 * 
	 * @param moved		boolean to set this.moved to
	 */
	public void setMoved(boolean moved)
	{
		this.movedTurn = moved ? MainActivity.bf.turn : -1;
		this.moved = moved;
	}


	public int getMovedTurn()
	{
		return movedTurn;
	}

	private int movedTurn;


	/**
	 * Gets the string interpretation of this Rook
	 * 
	 * @return 		String interpretation of this Rook
	 */
	public String toString()
	{
		return color + "R";
	}


	public Piece copy()
	{
		Rook ret = new Rook(this.getColor(), this.getLocation(), null);
		ret.setMoved(moved);
		return ret;
	}

}
