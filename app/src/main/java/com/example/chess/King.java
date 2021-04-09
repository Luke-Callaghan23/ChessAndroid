package com.example.chess;

import android.graphics.Point;
import android.os.Build;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;

//import java.;
import com.example.chess.ui.home.BoardFragment;

import java.util.ArrayList;

/**
 * 
 * @author Luke Callaghan -- ljc170
 * @author Rahul Hebbar   -- rjh242
 * 
 */
/**
 * Class for the King piece
 * 
 */
public class King extends Piece
{
	
	public Point leftCastle;
	public Point rightCastle;


	/**
	 * Value indicating whether this piece has moved before
	 */
	private boolean moved;
	
	
	/**
	 * The two rooks of this King's same color.  Used for castling.
	 */
	private Rook left, right;
	
	/**
	 * 
	 * Constructs a King by calling Piece super-constructor
	 * 
	 * @param color		'w'/'b' depending if the piece is white or black
	 * @param location	Point location of this piece
	 */
	public King(char color, Point location, ImageView iv) { super(color, location, iv); }

	/**
	 * 
	 * Gets a list of all the valid moves for this King
	 * 
	 * @return 		ArrayList of all valid moves for this King
	 */
	@RequiresApi(api = Build.VERSION_CODES.N)
	public ArrayList<Point> getMoves(Board board)
	{

		leftCastle  = null;
		rightCastle = null;

		Point curPoint;
		ArrayList<Point> moves = new ArrayList<Point>();

		Piece curPiece;

		int row = this.location.x, 
			col = this.location.y;
		
		//x-axis
		for(int rMod = -1; rMod < 2; rMod++)
		{
			//y-axis
			for(int cMod = -1; cMod < 2; cMod++)
			{
				//gets current point
				curPoint = new Point(row + rMod, col + cMod);

				//checks if current point is in range
				if (inRange.test(curPoint))
				{
					//gets piece at current point
					curPiece = board.getPiece(curPoint);

					//checks if this piece beats the piece at the current point
					if (beats.apply(this, curPiece)) 
					{
						moves.add(curPoint);
					}
				}
			}
		}
		
		
		//check if can castle
		if(!moved)
		{
			if(!this.checkCheck())
			{
				if(left != null && !left.getMoved())
				{
					if (board.onBoard(left))
					{

						int rows = this.location.x;
						int cols = this.location.y;

						Point pOne, pTwo, pThree;

						if(board.getPiece(pOne = new Point(rows, cols - 1)) instanceof Empty
								&& !checkCheck(pOne))
						{
							if(board.getPiece(pTwo = new Point(rows, cols - 2)) instanceof Empty
									&& !checkCheck(pTwo))
							{
								if(board.getPiece(pThree = new Point(rows, cols - 3)) instanceof Empty
										&& !checkCheck(pThree))
								{

									leftCastle = pTwo;
									moves.add(pTwo);
								}
							}
						}
					}
					else
					{
						left = null;
					}
				}
				if(left != null && !right.getMoved())
				{
					if (board.onBoard(right))
					{
						int rows = this.location.x;
						int cols = this.location.y;

						Point pOne, pTwo;

						if(board.getPiece(pOne = new Point(rows, cols + 1)) instanceof Empty
								&& !checkCheck(pOne))
						{
							if(board.getPiece(pTwo = new Point(rows, cols + 2)) instanceof Empty
									&& !checkCheck(pTwo))
							{

								rightCastle = pTwo;
								moves.add(pTwo);
							}
						}
					}
					else
					{
						right = null;
					}
				}
			}
		}
		return moves;
	}

	public void setLeft(Rook left)
	{
		this.left = left;
	}
	public void setRight(Rook right)
	{
		this.right = right;
	}


	/**
	 * Gets the string interpretation of this King
	 * 
	 * @return 		String interpretation of this King
	 */
	public String toString() {
		return color + "K";
	}

	/**
	 * Generic check check to be called on King's current position
	 * 
	 * @return		boolean indication whether the King at its current position is
	 * 				checked
	 */
	@RequiresApi(api = Build.VERSION_CODES.N)
	public boolean checkCheck()
	{
		return checkCheck(this.getLocation());
	}

	@RequiresApi(api = Build.VERSION_CODES.N)
	public boolean checkCheck(Board b)
	{
		return checkCheck(this.getLocation(), b);
	}
	
	/**
	 * 
	 * Checks if this King is being targeted by any enemy piece -- and is 
	 * therefore checked
	 * 
	 * @param loc	Location to be checked
	 * @return		A boolean indicating whether or not this King is checked
	 */
	@RequiresApi(api = Build.VERSION_CODES.N)
	public boolean checkCheck(Point loc)
	{
		return checkCheck(loc, BoardFragment.b);
	}

	@RequiresApi(api = Build.VERSION_CODES.N)
	public boolean checkCheck(Point loc, Board board)
	{
		//Get all pieces
		ArrayList<Piece> pieceReport = board.getAllPieces();

		//x-axis
		for(int rMod = -1; rMod < 2; rMod++)
		{
			//y-axis
			for(int cMod = -1; cMod < 2; cMod++)
			{
				if (inRange.test(new Point(loc.x + rMod, loc.y + cMod)))
				{
					if (!(rMod == 0 && cMod == 0))
					{
						if (board.getPiece(new Point(loc.x + rMod, loc.y + cMod)) instanceof King) return true;
					}

				}
			}
		}



		ArrayList<Piece> enemies = new ArrayList<Piece>();
		for(Piece p : pieceReport)
		{
			if(p.getColor() != color && !(p instanceof Empty))
			{
				enemies.add(p);
			}
		}
		boolean checked = false;
		OUTER: for(Piece enemy : enemies)
		{
			if(!(enemy instanceof King))
			{
				ArrayList<Point> moves = enemy.getMoves(board);

				for(Point move: moves)
				{
					if(move.equals(loc))
					{

						if (!(enemy instanceof Pawn) || move.y != enemy.location.y)
						{
							checked = true;
							break OUTER;

						}

					}
				}
			}
		}
		return checked;
	}


	/**
	 * Checks if this king has no more remaining moves left to make -- and is 
	 * therefore checkmated.
	 * 
	 * @return		A boolean indicating whether or not this king is checkmated
	 */
	@RequiresApi(api = Build.VERSION_CODES.N)
	public boolean checkMate()
	{
		//If the king is not checked, then it is not checkmated
		if(!this.checkCheck())
		{
			return false;
		}
		Board board = BoardFragment.b.copy();

		Object[] sameColors = board.getAllPieces().stream().filter((piece -> {
			return piece.getColor() == color;
		})).toArray();

		Board activeBoard;

		for (Object o : sameColors)
		{
			Piece curTeammate = (Piece) o;
			ArrayList<Point> curMoves = curTeammate.getMoves();


			for (Point p : curMoves)
			{
				activeBoard = board.copy();

				King[] mes = activeBoard.getKings();
				King me;
				if (this.getColor() == 'w')
				{
					me = mes[0];
				}
				else
				{
					me = mes[1];
				}

				activeBoard.movePiece(curTeammate.getLocation(), p, false);

				if (!me.checkCheck(activeBoard))
				{
					return false;
				}


			}

		}


		//If no valid moves, king is check mated
		return true;
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
	 * left getter
	 * 
	 * @return		left rook
	 */
	public Rook getLeft()
	{
		return this.left;
	}
	/**
	 * Right getter
	 * 
	 * @return		right rook
	 */
	public Rook getRight()
	{
		return this.right;
	}


	public int getMovedTurn()
	{
		return movedTurn;
	}

	private int movedTurn;


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
	
	/**
	 * Sets the two Rooks of this King's own color
	 * 
	 * @param castlers		Array of this king's rooks
	 */
	public void setCastlers(Rook[] castlers)
	{
		this.left  = castlers[0];
		this.right = castlers[1]; 
	}



	public Piece copy()
	{
		King ret =  new King(this.getColor(), this.getLocation(), null);
		ret.rightCastle = rightCastle != null ? new Point(rightCastle.x, rightCastle.y) : null;
		ret.leftCastle  = leftCastle  != null ? new Point(leftCastle.x, leftCastle.y)   : null;
		ret.setMoved(moved);
		return ret;
	}


}
