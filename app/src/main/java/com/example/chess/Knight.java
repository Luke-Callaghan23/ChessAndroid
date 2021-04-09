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
 * Class for the Knight Piece
 *
 */
public class Knight extends Piece 
{
	/**
	 * 
	 * Constructs a Knight by calling Piece super-constructor
	 * 
	 * @param color		'w'/'b' depending if the piece is white or black
	 * @param location	Point location of this piece
	 */
	public Knight(char color, Point location, ImageView iv) { super(color, location, iv); }

	/**
	 * 
	 * Gets a list of all valid moves for this Knight
	 * 
	 * @return		ArrayList of all valid moved for this knight
	 */
	@RequiresApi(api = Build.VERSION_CODES.N)
	public ArrayList<Point> getMoves(Board board)
	{
		Point curPoint;
		ArrayList<Point> moves = new ArrayList<Point>();

		Piece curPiece;
		//Piece[][] board = Chess.GetBoard().GetBoard();

		int row = this.location.x,
			col = this.location.y;

		//y-axis
		for (int rMod = 0; rMod < 2; rMod++)
		{
			//x-axis
			for (int cMod = 0; cMod < 2; cMod++)
			{
				//rot 90 deg
				for (int inner = 0; inner < 2; inner++)
				{
					int fRow = (rMod * 4) - 2,
						fCol = (cMod * 2) - 1,
						temp;
					//If inner == 1, swap row/col modifiers
					//		(rotates current piece target)
					if (inner == 1)
					{
						temp = fCol;
						fCol = fRow;
						fRow = temp;
					}

					//get current point
					curPoint = new Point(row + fRow, col + fCol);
					//check if current point is in range
					if (inRange.test(curPoint))
					{
						//get piece at current point
						curPiece = board.getPiece(curPoint);

						//check if this piece beats the piece at current point
						if (beats.apply(this, curPiece))
						{
							moves.add(curPoint);
						}
					}
				}
			}
		}
		return moves;
	}

	/**
	 * Gets the string interpretation of this Knight
	 * 
	 * @return 		String interpretation of this Knight
	 */
	public String toString() 
	{
		return color + "N";
	}

	public Piece copy()
	{
		return new Knight(this.getColor(), this.getLocation(), null);
	}


}

