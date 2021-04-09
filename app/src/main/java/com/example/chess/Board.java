package com.example.chess;

import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.example.chess.ui.home.BoardFragment;

import java.util.ArrayList;

/**
 * 
 * @author Luke Callaghan -- ljc170
 * @author Rahul Hebbar   -- rjh242
 *
 */

/**
 * 
 * Class for the Chess Board
 *
 */
public class Board 
{

	public boolean copiedBoard = false;

	
	
	/**
	 * white king and black king
	 */
	private King whiteKing, blackKing;
	
	/**
	 * white rooks and black rooks
	 */
//	private Rook leftWhite, rightWhite, leftBlack, rightBlack;
	
	/**
	 * Initial board state
	 */
//	private final static String[][] INITIAL = {
//		{"bR", null, null, null, "bK", null, null, "bR"},
//		{"bp", "bp", "bp", "bp", "bp", "bp", "bp", "bp"},
//		{null, null, null, null, null, null, null, null},
//		{null, null, null, null, null, null, null, null},
//		{null, null, null, null, null, null, null, null},
//		{null, null, null, null, null, null, null, null},
//		{"wp", "wp", "wp", "wp", "wp", "wp", "wp", "wp"},
//		{"wR", null, null, null, "wK", null, null, "wR"}
//	};
	private final static String[][] INITIAL = {
		{ "bR", "bN", "bB", "bQ", "bK", "bB", "bN", "bR" },
		{ "bp", "bp", "bp", "bp", "bp", "bp", "bp", "bp" },
		{ null, null, null, null, null, null, null, null },
		{ null, null, null, null, null, null, null, null },
		{ null, null, null, null, null, null, null, null },
		{ null, null, null, null, null, null, null, null },
		{ "wp", "wp", "wp", "wp", "wp", "wp", "wp", "wp" },
		{ "wR", "wN", "wB", "wQ", "wK", "wB", "wN", "wR" }
	};
	private final static String[][] TEST = {
		{ null, null, null, null, "bK", null, null, null },
		{ "wR", null, null, null, null, null, null, null },
		{ null, "wR", null, null, null, null, null, null },
		{ null, null, null, null, null, null, null, null },
		{ null, null, null, null, null, null, null, null },
		{ null, null, null, null, null, null, null, null },
		{ "wp", "wp", "wp", "wp", "wp", "wp", "wp", "wp" },
		{ null, null, null, null, "wK", null, null, null }
	};
	private Piece[][] board;
	/**
	 * Constructs a board of the INITIAL board state
	 * When new Board() is called with no parameters, creates a board
	 * 		with the initial board map.
	 */
	public Board(View view, AppCompatActivity ap)
	{
		this(INITIAL, view, ap);
	}


	public boolean onBoard(Piece target)
	{
		for (Piece[] row : board)
			for (Piece piece : row)
				if (piece == target) return true;
		return false;
	}


	public Piece movePiece(Piece piece, Point target)
	{
		Piece ret;
		Point oldPoint = piece.getLocation();
		board[oldPoint.x][oldPoint.y] = new Empty(oldPoint);
		piece.setLocation(target);
		ret = board[target.x][target.y];
		board[target.x][target.y] = piece;
		return ret;
	}


	/**
	 * Returns a piece on the board
	 * 
	 * @param point		row/col point of the piece we're looking for
	 * @return			Piece on the board at @param point
	 */
	public Piece getPiece(Point point)
	{
		return board[point.x][point.y];
	}
	
	
	
	
	/**
	 * Gets all pieces on the map that are not instances of
	 * the empty piece
	 * 
	 * @return ArrayList of all non-empty pieces on the board
	 */
	public ArrayList<Piece> getAllPieces()
	{
		ArrayList<Piece> pieces = new ArrayList<Piece>();
		for(Piece[] row : board)
		{
			for(Piece element : row)
			{

				if(element != null && !(element instanceof Empty)) pieces.add(element);
			}
		}
		return pieces;
	}
	
	/**
	 * Gets two kings on the board
	 * 
	 * @return array of both king pieces 
	 * 			[0] = white king
	 *			[1] = black king
	 */
	public King[] getKings()
	{
		return new King[] {whiteKing, blackKing};
	}
	
	
	
	/**
	 * Constructs a board of board state determined by 2D string array
	 * 
	 * @param boardState	2D string array to build the board from
	 */
	public Board(String[][] boardState, View view, AppCompatActivity ap)
	{
		Rook leftWhite = null, rightWhite = null;
		Rook leftBlack = null, rightBlack = null;
		this.board = new Piece[8][8];
		for(int rows = 0; rows < 8; rows++)
		{
			for(int cols = 0; cols < 8; cols++)
			{

				RelativeLayout rl = null;
				if (view != null)
				{
					rl = (RelativeLayout) view.findViewWithTag("position_" + (rows + 1) + "_" + (cols + 1));

				}
				String curPiece = boardState[rows][cols];
				
				if(curPiece == null)
				{
					board[rows][cols] = new Empty(new Point(rows, cols));
					continue;
				}
				
				char color = curPiece.charAt(0);
				switch(curPiece.charAt(1))
				{
					case 'R':
					{
						ImageView iv = new ImageView(ap);
						iv.setImageResource(color == 'w' ?
								R.drawable.white_rook :
								R.drawable.black_rook);

						LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(
								LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

						iv.setLayoutParams(imageParams);

						rl.addView(iv);
						board[rows][cols] = new Rook(color, new Point(rows, cols), iv);

						//cols == 0
						if(cols == 0)
						{
							//rows == 0
							if(rows == 0)
							{
								leftBlack = (Rook)board[rows][cols];
							}
							//rows == 7
							else
							{
								leftWhite = (Rook)board[rows][cols];
							}
						}
						//cols == 7
						else
						{
							//rows == 0
							if(rows == 0)
							{
								rightBlack = (Rook)board[rows][cols];
							}
							//rows == 7
							else
							{
								rightWhite = (Rook)board[rows][cols];
							}
						}
						break;
					}
					case 'N':
					{

						ImageView iv = new ImageView(ap);
						iv.setImageResource(color == 'w' ?
								R.drawable.white_knight :
								R.drawable.black_knight);

						LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(
								LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

						iv.setLayoutParams(imageParams);

						rl.addView(iv);
						board[rows][cols] = new Knight(color, new Point(rows, cols), iv);
						break;
					}
					case 'B':
					{
						ImageView iv = new ImageView(ap);
						iv.setImageResource(color == 'w' ?
								R.drawable.white_bishop :
								R.drawable.black_bishop);

						LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(
								LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

						iv.setLayoutParams(imageParams);

						rl.addView(iv);
						board[rows][cols] = new Bishop(color, new Point(rows, cols), iv);


						break;
					}
					case 'Q':
					{

						ImageView iv = new ImageView(ap);
						iv.setImageResource(color == 'w' ?
								R.drawable.white_queen :
								R.drawable.black_queen);

						LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(
								LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

						iv.setLayoutParams(imageParams);

						rl.addView(iv);

						board[rows][cols] = new Queen(color, new Point(rows, cols), iv);

						break;
					}
					case 'K':
					{
						ImageView iv = new ImageView(ap);
						iv.setImageResource(color == 'w' ?
								R.drawable.white_king :
								R.drawable.black_king);

						LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(
								LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

						iv.setLayoutParams(imageParams);
						rl.addView(iv);
						board[rows][cols] = new King(color, new Point(rows, cols), iv);

						if(color == 'w')
						{
							whiteKing = (King)board[rows][cols];
						}
						else
						{
							blackKing = (King)board[rows][cols];
						}


						break;
					}
					case 'p':
					{
						ImageView iv = new ImageView(ap);
						iv.setImageResource(color == 'w' ?
								R.drawable.white_pawn :
								R.drawable.black_pawn);

						LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(
								LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
						iv.setLayoutParams(imageParams);

						rl.addView(iv);
						board[rows][cols] = new Pawn(color, new Point(rows, cols), iv);
						break;
					}
				}
			}
		}
		whiteKing.setCastlers(new Rook[]{ leftWhite, rightWhite });
		blackKing.setCastlers(new Rook[]{ leftBlack, rightBlack });
	}
	/**
	 * 
	 * Returns a String with the current board state data
	 * 
	 * 
	 * @return A String that contains the current board state data
	 */
	public String toString()
	{
		String boardStr = "";
		for(int outer = 0; outer < 8; outer++)
		{
			for(int inner = 0; inner < 8; inner++)
			{
				boardStr += (board[outer][inner] + " ");
			}
			boardStr += (" " + (9 - (outer + 1)) + "\n");
		}
		boardStr += " a  b  c  d  e  f  g  h";
		return boardStr;
	}

	private boolean whiteChecked;
	private boolean blackChecked;

	@RequiresApi(api = Build.VERSION_CODES.N)
	public Object[] movePiece(Point piece, Point target)
	{
		return movePiece(piece, target, true);
	}



	//Returns a 2-wide array containing:
	//		[0] -> a string message to relay to the caller, normally null
	//		[1] -> an array of Move s for the pieces that moved during this turn
	@RequiresApi(api = Build.VERSION_CODES.N)
	public Object[] movePiece(Point piece, Point target, boolean doChecks)
	{
		//Initializing return values
		String message = null;
		Move[] moves = null;

		//Finding the piece to be moved
		Piece mover = board[piece.x][piece.y];

		Point tempPassant = (mover instanceof Pawn) ? ((Pawn) mover).passant : null;

		whiteChecked = whiteKing.checkCheck();
		blackChecked = blackKing.checkCheck();

		//Move the piece
		Piece overwritten = movePiece(mover, target);

		//Check if the move produced some kind of check
		int result = doChecks ? doCheck() : 0;

		//If the current move does not remove the player from check
		//		or places the player's self in check
		if (result == 1 || result == 2)
		{
			//Move the mover piece back to it's original
			//		spot
			movePiece(mover, piece);
			//Move the overwritten piece to where the mover moved to
			movePiece(overwritten, target);

			//No moves occurred, so return a nullptr
			return new Object[] { result == 1 ? "checked-self" : "still-checked", null };
		}


		//Now that we know the move was successful

		//First move is piece -> target
		Move one = new Move(mover, overwritten, piece, target);

		//If the mover was a pawn
		if (mover instanceof Pawn)
		{
			if (Math.abs(piece.x - target.x) == 2)
			{
				//Setting the doubleMoved variable
				//		for doing enPassant
				MainActivity.bf.lastDoubleMoved = mover;
				moves = new Move[] { one };

			}
			else if (target.x == 0)
			{
				//If a white pawn is to be ranked up

				if (!copiedBoard)
				{
					Bundle bundle = new Bundle();
					bundle.putInt(ChessDialog.MESSAGE_TYPE, ChessDialog.TYPE_RANKUP);
					bundle.putInt(ChessDialog.RANKUP_ROW, target.x);
					bundle.putInt(ChessDialog.RANKUP_COL, target.y);
					bundle.putString(ChessDialog.MESSAGE_KEY, "What rank would you like to rank up to?");
					DialogFragment newFragment = new ChessDialog();
					newFragment.setArguments(bundle);
					newFragment.show(MainActivity.bf.getParentFragmentManager(), "badfields");
					message = "rankup";

				}
				else
				{
					message = "auto-rankup";
				}




				moves = new Move[] { one };

			}
			else if (target.x == 7)
			{
				//If a black pawn is to be ranked up

				if (!copiedBoard)
				{
					Bundle bundle = new Bundle();
					bundle.putInt(ChessDialog.MESSAGE_TYPE, ChessDialog.TYPE_RANKUP);
					bundle.putInt(ChessDialog.RANKUP_ROW, target.x);
					bundle.putInt(ChessDialog.RANKUP_COL, target.y);
					bundle.putString(ChessDialog.MESSAGE_KEY, "What rank would you like to rank up to?");
					DialogFragment newFragment = new ChessDialog();
					newFragment.setArguments(bundle);
					newFragment.show(MainActivity.bf.getParentFragmentManager(), "badfields");

					message = "rankup";
				}
				else
				{
					message = "auto-rankup";
				}



				moves = new Move[] { one };

			}
			else if (target.equals(tempPassant))
			{

				//If the pawn's move is to kill passing pawn

				//Get passing pawn and its location
				Piece passant = MainActivity.bf.lastDoubleMoved;
				Point passantLoc = passant.getLocation();

				//Create the Empty piece that will be replacing it
				Empty replace = new Empty(passantLoc);

				//Set the empty piece to the location of the passant
				setPiece(replace, passantLoc);

				//Now that we've removed a pawn from the board, we have to
				//		do another check check to make sure that move didn't lead to a
				//		self check
				result = doChecks ? doCheck() : result;

				//If the passing pawn's disappearance has checked your king
				if (result == 1 || result == 2)
				{
					//Move the mover piece back to it's original
					//		spot
					movePiece(mover, piece);
					//Move the overwritten piece to where the mover moved to
					movePiece(overwritten, target);

					//Move the passing pawn back onto the board
					setPiece(passant, passantLoc);

					//No moves occurred, so return a nullptr
					return new Object[] { result == 1 ? "checked-self" : "still-checked", null };
				}

				//Make the second move:
				//		the called will know that moveFrom = Point(-1, -1) means that we
				//		just want to remove the sprite in moveTo and replace it with nothing
				Move two = new Move(replace, passant, new Point(-1, -1), passantLoc);

				//Reset the double moved for safety
				MainActivity.bf.lastDoubleMoved = null;
				moves  = new Move[] { one, two };
			}
			else
			{
				moves = new Move[] { one };
			}
		}
		//If the mover was a King
		else if (mover instanceof King)
		{

			//Check for castling:

			Move two = null;

			if (target.equals(((King) mover).leftCastle))
			{
				//If the move was swapping with the left castle

				//If moving left castle, rook destination is (King row, 3)
				int targRow = target.x;
				int targCol = 3;

				Point curRook  = new Point(target.x, 0);
				Point moveRook = new Point(targRow, targCol);

				//Get the rook to move
				Piece rMover = getPiece(curRook);

				((Rook) rMover).setMoved(true);

				//Moving the piece and getting the overwritten piece,
				//		the overwritten piece *should* always be an Empty
				Piece rOverwritten = movePiece(rMover, moveRook);

				//Set the second move
				two = new Move(rMover, rOverwritten, curRook, moveRook);

			}
			else if (target.equals(((King) mover).rightCastle))
			{
				//If the move was swapping with the right castle

				//If moving right castle, rook destination is (King row, 5)
				int targRow = target.x;
				int targCol = 5;

				Point curRook  = new Point(target.x, 7);
				Point moveRook = new Point(targRow, targCol);

				//Get the rook to move
				Piece rMover = getPiece(curRook);

				((Rook) rMover).setMoved(true);

				//Moving the piece and getting the overwritten piece,
				//		the overwritten piece *should* always be an Empty
				Piece rOverwritten = movePiece(rMover, moveRook);

				//Set the second move
				two = new Move(rMover, rOverwritten, curRook, moveRook);
			}
			else
			{
				moves = new Move[] { one };
			}

			if (two != null)
			{
				//We need to move both the King and the castling rook
				moves = new Move[] { one, two };

				//After castling is done, we need to check for check again,
				//		just to check if we check the enemy king post cast
				result = doChecks ? doCheck() : result;

			}

		}
		else
		{

			//The only piece to be moved is the mover
			moves = new Move[] { one };
		}


		//Setting 'moved' status of each of the pieces
		if (mover instanceof King) ((King) mover).setMoved(true);
		if (mover instanceof Rook) ((Rook) mover).setMoved(true);
		if (mover instanceof Pawn) ((Pawn) mover).setMoved(true);


		if (result == 3)
		{
			//If there was a check mate
			message = "checked-opponent";
		}
		else if (result == 4)
		{
			//If there was a check on the opponent
			message = "checkmate";
		}

		return new Object[] { message, moves };
	}

	public void undo(Move[] moves)
	{
		for (Move move: moves)
		{
//			Move two = new Move(replace, passant, new Point(-1, -1), passantLoc);

			Piece overwritten = move.getOverwritten();
			Piece mover       = move.getMoved();
			Point from        = move.getMoveFrom();
			Point to          = move.getMoveTo();


			if (mover instanceof Pawn && ((Pawn) mover).getMovedTurn() == MainActivity.bf.turn - 1) ((Pawn) mover).setMoved(false);
			if (mover instanceof King && ((King) mover).getMovedTurn() == MainActivity.bf.turn - 1) ((King) mover).setMoved(false);
			if (mover instanceof Rook && ((Rook) mover).getMovedTurn() == MainActivity.bf.turn - 1) ((Rook) mover).setMoved(false);


			if (!from.equals(-1, -1)) setPiece(mover, from);
			if (to.equals(-1, -1)) setPiece(move.getOverwritten(), from);
			else setPiece(overwritten, to);

		}
	}



	//Return:
	//		0 -> no checks
	//		1 -> checked self
	//		2 -> did not remove self from check
	//		3 -> checked opponent
	//		4 -> checkMate
	@RequiresApi(api = Build.VERSION_CODES.N)
	public int doCheck()
	{
		//Set your king and the enemy king
		King enemy, me;
		if (MainActivity.bf.turns[MainActivity.bf.turn % 2] == 'w')
		{
			me = whiteKing;
			enemy = blackKing;
		}
		else
		{
			me = blackKing;
			enemy = whiteKing;
		}

		//If you are checked, return 2 if you were already checked, and 1 if you were not
		//		already checked
		if (me.checkCheck()) return (me == whiteKing ? whiteChecked : blackChecked) ? 2 : 1;
		else if (enemy.checkMate())  return 4;
		else if (enemy.checkCheck()) return 3;

		//If no checks return 0
		return 0;
	}


	/*
	@RequiresApi(api = Build.VERSION_CODES.N)
	public void doCheck()
	{
		if (whiteKing.checkCheck())
		{
			if (whiteKing.checkMate())
			{
				//First check if there is a check mate
				Bundle bundle = new Bundle();
				bundle.putInt(ChessDialog.MESSAGE_TYPE, ChessDialog.TYPE_RESIGN);
				bundle.putChar(ChessDialog.RESIGN_COLOR, 'w');
				bundle.putString(ChessDialog.MESSAGE_KEY, "White checkmated.  Black wins!");
				bundle.putBoolean(ChessDialog.ASK_RECO, true);
				DialogFragment newFragment = new ChessDialog();
				newFragment.setArguments(bundle);
				newFragment.show(MainActivity.bf.getParentFragmentManager(), "badfields");
				return;
			}

			if (MainActivity.bf.turns[(MainActivity.bf.turn - 1) % 2] == 'w')
			{
				//If white king is checked, and it is white turn, then the next move must remove the king from check
				//      undo the turn and display an error message

				MainActivityundo(null);

				selected = null;
				attackablePoints = new ArrayList<Point>();

				if (display)
				{
					Bundle bundle = new Bundle();
					bundle.putInt(ChessDialog.MESSAGE_TYPE, ChessDialog.TYPE_MESSAGE);
					bundle.putString(ChessDialog.MESSAGE_KEY, whiteChecked ? "You must make a move to take your king out of a checked position!" : "That would check your king!");
					DialogFragment newFragment = new ChessDialog();
					newFragment.setArguments(bundle);
					newFragment.show(getParentFragmentManager(), "badfields");
					checkedSelf = true;

				}

				return;
			}
			else if(!blackKing.checkCheck())
			{
				whiteChecked = true;

				TextView tv = main.findViewById(R.id.checked);
				tv.setText(tv.getText().toString() + " White");

				selected = null;
				attackablePoints = new ArrayList<Point>();
				Bundle bundle = new Bundle();
				bundle.putInt(ChessDialog.MESSAGE_TYPE, ChessDialog.TYPE_MESSAGE);
				bundle.putString(ChessDialog.MESSAGE_KEY, "White King checked!");
				DialogFragment newFragment = new ChessDialog();
				newFragment.setArguments(bundle);
				newFragment.show(getParentFragmentManager(), "badfields");


			}
		}
		else if (whiteChecked && turns[(turn - 1) % 2] == 'w')
		{

			//In the case that the white king .checkCheck is false but whiteChecked is true, that
			//      means that the current move has taken the white king out of check

			whiteChecked = false;
			TextView tv = main.findViewById(R.id.checked);
			if (blackChecked)
			{
				tv.setText("Black");
			}
			else
			{
				tv.setText("");
			}
		}
		if(blackKing.checkCheck())
		{

			if (blackKing.checkMate())
			{
				//First check checkmates

				Bundle bundle = new Bundle();
				bundle.putInt(ChessDialog.MESSAGE_TYPE, ChessDialog.TYPE_RESIGN);
				bundle.putChar(ChessDialog.RESIGN_COLOR, 'b');
				bundle.putString(ChessDialog.MESSAGE_KEY, "White checkmated.  Black wins!");
				bundle.putBoolean(ChessDialog.ASK_RECO, true);

				DialogFragment newFragment = new ChessDialog();
				newFragment.setArguments(bundle);
				newFragment.show(getParentFragmentManager(), "badfields");
				return;
			}

			if (turns[(turn - 1) % 2] == 'b')
			{

				//If it is black's turn and black is checked, then we undo the turn
				//      and display an error message

				undo(null);

				attackablePoints = new ArrayList<Point>();

				if (display)
				{
					Bundle bundle = new Bundle();
					bundle.putInt(ChessDialog.MESSAGE_TYPE, ChessDialog.TYPE_MESSAGE);
					bundle.putString(ChessDialog.MESSAGE_KEY,  blackChecked ? "You must make a move to take your king out of a checked position!" : "That would check your king!");
					DialogFragment newFragment = new ChessDialog();
					newFragment.setArguments(bundle);
					newFragment.show(getParentFragmentManager(), "badfields");

				}
				checkedSelf = true;
				return;
			}
			else if(!whiteKing.checkCheck())
			{
				blackChecked = true;

				TextView tv = main.findViewById(R.id.checked);
				tv.setText(tv.getText().toString() + " Black");

				selected = null;
				attackablePoints = new ArrayList<Point>();
				Bundle bundle = new Bundle();
				bundle.putInt(ChessDialog.MESSAGE_TYPE, ChessDialog.TYPE_MESSAGE);
				bundle.putString(ChessDialog.MESSAGE_KEY, "Black King checked!");
				DialogFragment newFragment = new ChessDialog();
				newFragment.setArguments(bundle);
				newFragment.show(getParentFragmentManager(), "badfields");

			}
		}
		else if (blackChecked && turns[(turn - 1) % 2] == 'b')
		{
			blackChecked = false;
			TextView tv = main.findViewById(R.id.checked);
			if (whiteChecked)
			{
				tv.setText(" Black");
			}
			else
			{
				tv.setText("");
			}
		}
	}

	*/



	/**
	 * Sets a Piece to a location on the board.  Unlike movePiece, this method
	 * is used when the piece that is being set is not already on the board when
	 * the method is called.
	 * 
	 * @param piece			Piece to be set
	 * @param location		Location to set the piece to
	 * @return				Piece that was overwritten by the moved Piece
	 */
	public Piece setPiece(Piece piece, Point location)
	{
		Piece ret = board[location.x][location.y];
		board[location.x][location.y] = piece;
		piece.setLocation(location);
		return ret;
	}


	public Board() { }


	public Board copy()
	{

		Rook whiteLeft = null, whiteRight = null;
		Rook blackLeft = null, blackRight = null;

		Board b = new Board();
		Piece[][] newBoard = new Piece[8][8];
		for(int row = 0; row < 8; row++)
		{
			for (int col = 0; col < 8; col++)
			{
				Piece curPiece = board[row][col];
				newBoard[row][col] = curPiece.copy();

				if      (curPiece == whiteKing) b.whiteKing = (King) newBoard[row][col];
				else if (curPiece == blackKing) b.blackKing = (King) newBoard[row][col];
				else if (curPiece == whiteKing.getLeft ()) whiteLeft  = (Rook) curPiece;
				else if (curPiece == whiteKing.getRight()) whiteRight = (Rook) curPiece;
				else if (curPiece == blackKing.getLeft ()) blackLeft  = (Rook) curPiece;
				else if (curPiece == blackKing.getRight()) blackRight = (Rook) curPiece;

			}
		}

		b.whiteKing.setLeft(whiteLeft);
		b.whiteKing.setRight(whiteRight);
		b.blackKing.setLeft(blackLeft);
		b.blackKing.setRight(blackRight);
		b.copiedBoard = true;
		b.board = newBoard;
		return b;
	}


	/**
	 * Removes a piece from the board at a given location
	 * 
	 * @param location
	 */
	public void removePiece(Point location) 
	{
		board[location.x][location.y] = new Empty(location);
	}

	public void addPiece(Piece pPuttingBack, Point moveFrom)
	{
		pPuttingBack.setLocation(moveFrom);
		board[moveFrom.x][moveFrom.y] = pPuttingBack;
	}
}
