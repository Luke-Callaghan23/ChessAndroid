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
 * Class for the Bishop Piece
 *
 */
public class Bishop extends Piece 
{
	/**
	 * 
	 * Constructs a Bishop by calling Piece super-constructor
	 * 
	 * @param color		'w'/'b' depending if the piece is white or black
	 * @param location	Point location of this piece
	 */
	public Bishop(char color, Point location, ImageView iv) { super(color, location, iv); }


	/**
	 * 
	 * Gets a list of all the valid moves for this Bishop
	 * 
	 * @return 		ArrayList of all valid moves for this Bishop
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


		//4-directions
		//	0-> up-left
		//	1-> up-right
		//	2-> down-left
		//	3-> down-right
		boolean[] stops = new boolean[4];

		//double for-loop checks all 4 directions of movement (inner)
		//		at each (outer) iteration of increasing distance (1-4)
		
		//each iteration of mod is another tile of distance from current piece
		for (int mod = 1; mod <= 8; mod++)
		{
			//checks 4 directions
			for(int stop = 0; stop < 4; stop++)
			{
				if(!stops[stop])
				{
					//converts iteration of inner loop to -1/1 for rows/cols
					//	0 -> -1, -1
					//	1 -> -1,  1
					//	2 ->  1, -1
					//	3 ->  1,  1
					int dirModRow = (stop / 2 != 0) ? 1 : -1;
					int dirModCol = (stop % 2 != 0) ? 1 : -1;

					//Gets current point on map
					curPoint = new Point(row + mod * dirModRow, col + mod * dirModCol);
					if(inRange.test(curPoint))
					{
						//Gets piece at current point
						curPiece = board.getPiece(curPoint);

						//Checks if this piece at current point
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
	 * Gets the string interpretation of this Bishop
	 * 
	 * @return 		String interpretation of this Bishop
	 */
	public String toString() 
	{
		return color + "B";
	}


	public Piece copy()
	{
		return new Bishop(this.getColor(), this.getLocation(), null);
	}

}
