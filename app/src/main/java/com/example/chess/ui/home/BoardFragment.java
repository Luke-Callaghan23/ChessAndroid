package com.example.chess.ui.home;

import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.chess.Bishop;
import com.example.chess.Board;
import com.example.chess.ChessDialog;
import com.example.chess.Empty;
import com.example.chess.King;
import com.example.chess.Knight;
import com.example.chess.MainActivity;
import com.example.chess.Move;
import com.example.chess.Pawn;
import com.example.chess.Piece;
import com.example.chess.Queen;
import com.example.chess.R;
import com.example.chess.Rook;
import com.example.chess.Recording;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import java.util.Stack;

public class BoardFragment extends Fragment
{


    public static Recording playBack;


    private static Stack<Move> recording;
//
//    public static Point whiteRightCastle;
//    public static Point whiteLeftCastle;
//    public static Point blackRightCastle;
//    public static Point blackLeftCastle;
//    public static Piece doubleMoved;
//    public static Point enPassant;
//
//    private static King whiteKing;
//    private static King blackKing;
//
//    private static boolean whiteChecked;
//    private static boolean blackChecked;

//    private Point[] castleUndo;
//    private Piece passantUndo;

    private Point selectedTile;
    private ArrayList<Point> attackablePoints = new ArrayList<Point>();
    public Piece selected;
    public char[] turns = { 'w', 'b' };
    public int turn = 0;


    private BoardViewModel homeViewModel;

    public static MainActivity main;

    public static Board b;

    private static BoardFragment bf;

    private View root;


    private boolean noUndo = false;

    public static HashMap<Integer, Integer> rankups;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {
        homeViewModel =
                ViewModelProviders.of(this).get(BoardViewModel.class);
        root = inflater.inflate(R.layout.fragment_playing, container, false);
        final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        rankups = new HashMap<Integer, Integer>();

        View v = root.findViewById(R.id.grid);

        main = (MainActivity) getParentFragment().getActivity();

        recording = new Stack<Move>();
        assert getParentFragment() != null;
        b = new Board(v, main);

        MainActivity.bf = this;
        bf = this;


        if (playBack != null)
        {
            ((Button) root.findViewById(R.id.resign)).setEnabled(false);
            ((Button) root.findViewById(R.id.undo)).setEnabled(false);
            ((Button) root.findViewById(R.id.draw)).setEnabled(false);
            ((Button) root.findViewById(R.id.ai)).setEnabled(false);
            ((Button) root.findViewById(R.id.turn)).setEnabled(true);
            noUndo = true;
        }


        return root;
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public void boardClick(View view)
    {
        if (playBack == null)
        {
            //Board click that is called by the boardClick on click event
            //      in this overload, we calculate row and column
            //      and then call the actual board click method
            String tag = view.getTag().toString();
            if(tag.matches("board_[1-8]_[1-8]"))
            {
                String[] rowCol = tag.split("_");
                int row = Integer.parseInt(rowCol[1]);
                int col = Integer.parseInt(rowCol[2]);

                row -= 1;
                col -= 1;

                boardClick(row, col);
            }
        }

    }


    /*

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void boardClick(int row, int col)
    {
        if (attackablePoints != null && attackablePoints.size() != 0)
        {
            for (Point p : attackablePoints)
            {
                View prev = (main.findViewById(R.id.grid));
                RelativeLayout rl = prev.findViewWithTag("position_" + (p.x + 1) + "_" + (p.y + 1));

                rl.removeView(rl.findViewWithTag("attack_" + (p.x + 1) + "_" + (p.y + 1)));

            }

        }

        RelativeLayout relativeLayout = (RelativeLayout) main.findViewById(R.id.grid)
                .findViewWithTag("position_" + (row + 1) + "_" + (col + 1));


        Point cur = new Point(row, col);

        if (prevShine == null || !prevShine.equals(cur))
        {
            if (prevShine != null)
            {
                View prev = (main.findViewById(R.id.grid));
                RelativeLayout rl = prev.findViewWithTag("position_" + (prevShine.x + 1) + "_" + (prevShine.y + 1));
                rl.removeView(rl.findViewWithTag("shine"));
            }

            boolean inAttckPoints = false;
            for (Point cAttck : attackablePoints)
            {
                if (cAttck.equals(cur))
                {
                    inAttckPoints = true;
                    break;
                }
            }

            if (inAttckPoints)
            {
                //Code for moving a piece

                char color = selected.getColor();

                if (turns[turn % 2] == color || color == ' ')
                {
                    Point origPoint = selected.getLocation();
                    overwritten = b.movePiece(selected.getLocation(), cur);
                    View grid = main.findViewById(R.id.grid);
                    RelativeLayout orig   = grid.findViewWithTag("position_" + (origPoint.x + 1) + "_" + (origPoint.y + 1));
                    RelativeLayout moveTo = grid.findViewWithTag("position_" + (row + 1) + "_" + (col + 1));

                    orig.removeView(selected.getView());
                    if (!(overwritten instanceof Empty)) moveTo.removeView(overwritten.getView());
                    moveTo.addView(selected.getView());

                    turn++;

                    TextView colorText = main.findViewById(R.id.colorText);
                    colorText.setText((turns[turn % 2] == 'w') ? "White" : "Black");

                    Move thisMove = new Move(selected, overwritten, origPoint, cur);

                    if (selected instanceof King) ((King) selected).setMoved(true);
                    if (selected instanceof King) ((King) selected).setMoved(true);
                    if (selected instanceof Rook) ((Rook) selected).setMoved(true);
                    if (selected instanceof Pawn) ((Pawn) selected).setMoved(true);

                    recording.push(thisMove);


                    doCheck();

                    if (askRankup)
                    {

                        if (selected instanceof Pawn)
                        {
                            if (turns[(turn - 1) % 2] == 'w')
                            {
                                if (row == 0)
                                {
                                    Bundle bundle = new Bundle();
                                    bundle.putInt(ChessDialog.MESSAGE_TYPE, ChessDialog.TYPE_RANKUP);
                                    bundle.putInt(ChessDialog.RANKUP_ROW, row);
                                    bundle.putInt(ChessDialog.RANKUP_COL, col);
                                    bundle.putString(ChessDialog.MESSAGE_KEY, "What rank would you like to rank up to?");
                                    DialogFragment newFragment = new ChessDialog();
                                    newFragment.setArguments(bundle);
                                    newFragment.show(getParentFragmentManager(), "badfields");
                                }
                            }
                            else if (turns[(turn - 1) % 2] == 'b')
                            {
                                if (row == 7)
                                {
                                    Bundle bundle = new Bundle();
                                    bundle.putInt(ChessDialog.MESSAGE_TYPE, ChessDialog.TYPE_RANKUP);
                                    bundle.putInt(ChessDialog.RANKUP_ROW, row);
                                    bundle.putInt(ChessDialog.RANKUP_COL, col);
                                    bundle.putString(ChessDialog.MESSAGE_KEY, "What rank would you like to rank up to?!");
                                    DialogFragment newFragment = new ChessDialog();
                                    newFragment.setArguments(bundle);
                                    newFragment.show(getParentFragmentManager(), "badfields");
                                }
                            }
                        }
                    }






                    if (turns[(turn - 1) % 2] == 'w')
                    {
                        if (cur.equals(whiteLeftCastle))
                        {
                            //Move rook 8,1 to 8,4
                            Piece wLR = b.getPiece(new Point(7, 0));
                            View wLRV = wLR.getView();


                            RelativeLayout wLRL = main.findViewById(R.id.grid)
                                    .findViewById(R.id.position_8_1);


                            wLRL.removeView(wLRV);

                            b.movePiece(wLR, new Point(7,3));

                            ((Rook) wLR).setMoved(true);


                            RelativeLayout target = main.findViewById(R.id.position_8_4);

                            target.addView(wLRV);


                            castleUndo = new Point[2];
                            castleUndo[0] = new Point(7,0);
                            castleUndo[1] = new Point(7,3);



                            blackLeftCastle = null;
                            whiteLeftCastle = null;
                            blackRightCastle = null;
                            whiteRightCastle = null;
                            passantUndo = null;
                            selected = null;
                            attackablePoints = new ArrayList<Point>();
                            doubleMoved = null;


                            if (askRankup)
                                ((Button) main.findViewById(R.id.undo)).setEnabled(true);


                            return;

                        }
                        else if (cur.equals(whiteRightCastle))
                        {
                            //Move rook 8,8 to 8,6
                            Piece wRR = b.getPiece(new Point(7, 7));
                            View wRRV = wRR.getView();



                            RelativeLayout wRRL = main.findViewById(R.id.grid)
                                    .findViewById(R.id.position_8_8);

                            wRRL.removeView(wRRV);

                            b.movePiece(wRR, new Point(7,5));

                            ((Rook) wRR).setMoved(true);



                            RelativeLayout target = main.findViewById(R.id.position_8_6);

                            target.addView(wRRV);

                            castleUndo = new Point[2];
                            castleUndo[0] = new Point(7,7);
                            castleUndo[1] = new Point(7,5);

                            blackLeftCastle = null;
                            whiteLeftCastle = null;
                            blackRightCastle = null;
                            whiteRightCastle = null;
                            passantUndo = null;
                            selected = null;
                            attackablePoints = new ArrayList<Point>();
                            doubleMoved = null;
                            ((Button) main.findViewById(R.id.undo)).setEnabled(true);


                            return;
                        }
                    }
                    else
                    {
                        if (cur.equals(blackLeftCastle))
                        {
                            //Move rook 1,1 to 1,4
                            Piece bLR = b.getPiece(new Point(0, 0));
                            View bLRV = bLR.getView();


                            RelativeLayout bLRL = main.findViewById(R.id.grid)
                                    .findViewById(R.id.position_1_1);


                            bLRL.removeView(bLRV);


                            b.movePiece(bLR, new Point(0,3));

                            ((Rook) bLR).setMoved(true);


                            RelativeLayout target = main.findViewById(R.id.position_1_4);

                            target.addView(bLRV);

                            castleUndo = new Point[2];
                            castleUndo[0] = new Point(0,0);
                            castleUndo[1] = new Point(0,3);

                            blackLeftCastle = null;
                            whiteLeftCastle = null;
                            blackRightCastle = null;
                            whiteRightCastle = null;
                            passantUndo = null;
                            selected = null;
                            attackablePoints = new ArrayList<Point>();
                            doubleMoved = null;
                            if (askRankup)
                                ((Button) main.findViewById(R.id.undo)).setEnabled(true);


                            return;

                        }
                        else if (cur.equals(blackRightCastle))
                        {
                            //Move rook 1,8 to 1,6
                            Piece bRR = b.getPiece(new Point(0,7));
                            View bRRV = bRR.getView();



                            RelativeLayout bRRL = main.findViewById(R.id.grid)
                                    .findViewById(R.id.position_1_8);

                            bRRL.removeView(bRRV);


                            b.movePiece(bRR, new Point(0,5));

                            ((Rook) bRR).setMoved(true);

                            RelativeLayout target = main.findViewById(R.id.position_1_6);

                            target.addView(bRRV);

                            castleUndo = new Point[2];
                            castleUndo[0] = new Point(0,7);
                            castleUndo[1] = new Point(0,5);

                            blackLeftCastle = null;
                            whiteLeftCastle = null;
                            blackRightCastle = null;
                            whiteRightCastle = null;
                            passantUndo = null;
                            selected = null;
                            attackablePoints = new ArrayList<Point>();
                            doubleMoved = null;
                            if (askRankup)
                                ((Button) main.findViewById(R.id.undo)).setEnabled(true);


                            return;
                        }
                    }







                    if (selected instanceof Pawn)
                    {
                        if (doubleMoved != null)
                        {

                            if (cur.equals(enPassant))
                            {
                                //THe piece to be removed in the enPassant should be
                                //      on the same row as the origPoint.x and the same col
                                //      as cur.y

                                Piece passed = b.getPiece(new Point(origPoint.x, cur.y));

                                b.removePiece(passed.getLocation());

                                ((RelativeLayout) main.findViewById(R.id.grid)
                                        .findViewWithTag("position_" + (passed.getLocation().x + 1) + "_" + (passed.getLocation().y + 1)))
                                        .removeView(passed.getView());


                                blackLeftCastle = null;
                                whiteLeftCastle = null;
                                blackRightCastle = null;
                                whiteRightCastle = null;
                                passantUndo = passed;
                                castleUndo = null;
                                selected = null;
                                attackablePoints = new ArrayList<Point>();
                                doubleMoved = null;
                                if (askRankup)
                                    ((Button) main.findViewById(R.id.undo)).setEnabled(true);
                                return;
                            }
                        }

                        if (Math.abs(origPoint.x - row) == 2)
                        {

                            blackLeftCastle = null;
                            whiteLeftCastle = null;
                            blackRightCastle = null;
                            whiteRightCastle = null;
                            doubleMoved = selected;
                            passantUndo = null;
                            castleUndo = null;
                            selected = null;
                            attackablePoints = new ArrayList<Point>();
                            if (askRankup)
                                ((Button) main.findViewById(R.id.undo)).setEnabled(true);
                            return;
                        }
                    }


                    blackLeftCastle = null;
                    whiteLeftCastle = null;
                    blackRightCastle = null;
                    whiteRightCastle = null;
                    passantUndo = null;
                    castleUndo = null;
                    selected = null;
                    attackablePoints = new ArrayList<Point>();
                    doubleMoved = null;
                    if (askRankup)
                        ((Button) main.findViewById(R.id.undo)).setEnabled(true);

                }
                else
                {
                    //Display some error message for trying to move a piece that is not yours

                    selected = null;
                    attackablePoints = new ArrayList<Point>();
                    Bundle bundle = new Bundle();
                    bundle.putInt(ChessDialog.MESSAGE_TYPE, ChessDialog.TYPE_MESSAGE);
                    bundle.putString(ChessDialog.MESSAGE_KEY, "You can't attempt to move a piece that isn't yours!");
                    DialogFragment newFragment = new ChessDialog();
                    newFragment.setArguments(bundle);
                    newFragment.show(getParentFragmentManager(), "badfields");
                    return;


                }


            }
            else
            {

                //Code for targeting a spot on the board
                LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT
                );
                ImageView imageView = new ImageView(main);
                imageView.setTag("shine");
                imageView.setBackgroundColor(Color.parseColor("#FFFB33"));
                imageView.setAlpha(0.7f);
                imageParams.width  = ViewGroup.LayoutParams.MATCH_PARENT;
                imageParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
                imageView.setLayoutParams(imageParams);
                relativeLayout.addView(imageView);
                prevShine = new Point(row, col);
                selected = b.getPiece(prevShine);
                attackablePoints = selected.getMoves();
                for (Point p : attackablePoints)
                {
                    View prev = (main.findViewById(R.id.grid));
                    RelativeLayout rl = prev.findViewWithTag("position_" + (p.x + 1) + "_" + (p.y + 1));
                    LinearLayout.LayoutParams attackParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                    ImageView attackView = new ImageView(main);
                    attackView.setTag("attack_" + (p.x + 1) + "_" + (p.y + 1));
                    attackView.setBackgroundColor(Color.parseColor("#ff0000"));
                    attackView.setAlpha(0.4f);
                    attackParams.width  = ViewGroup.LayoutParams.MATCH_PARENT;
                    attackParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
                    attackView.setLayoutParams(attackParams);
                    rl.addView(attackView);
                }
            }
        }
        else
        {
            View shine = relativeLayout.findViewWithTag("shine");
            relativeLayout.removeView(shine);
            prevShine = null;
            attackablePoints = new ArrayList<Point>();
        }
    }

*/


    public Move[] getLastMoves()
    {
        return lastMoves;
    }

    public void setLastMoves(Move[] lastMoves)
    {
        this.lastMoves = lastMoves;
    }

    private Move[] lastMoves = null;
    public Piece lastDoubleMoved;
    private Piece prevLastDoubleMoved;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void boardClick(int row, int col)
    {
        boardClick(row, col, -1);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void boardClick(int row, int col, int rankTo)
    {
        //If there is a click with the red attackablePoints displayed, then we need to remove them no matter what
        cleanAttacking();

        RelativeLayout relativeLayout = (RelativeLayout) main.findViewById(R.id.grid)
                .findViewWithTag("position_" + (row + 1) + "_" + (col + 1));


        Point cur = new Point(row, col);

        //If the selected tile doesn't exist, or it is not the
        //      currently selected tile
        if (selectedTile == null || !selectedTile.equals(cur))
        {
            if (selectedTile != null)
            {
                View prev = (main.findViewById(R.id.grid));
                RelativeLayout rl = prev.findViewWithTag("position_" + (selectedTile.x + 1) + "_" + (selectedTile.y + 1));
                rl.removeView(rl.findViewWithTag("shine"));
            }

            if (attackablePoints.contains(cur))
            {

                if (selected.getColor() == turns[turn % 2])
                {

                    Piece doubleMovedBackup = lastDoubleMoved;

                    //Attempt to move the piece
                    Object[] result = b.movePiece(selected.getLocation(), cur);

                    String message = (String) result[0];
                    lastMoves      = (Move[]) result[1];

                    if (message == null || message.equals("checkmate")
                            || message.equals("checked-opponent") || message.equals("rankup")
                            || message.equals("auto-rankup"))
                    {
                        //If there was a successful move:
                        turn++;

                        //Change display for current player turn
                        TextView colorText = main.findViewById(R.id.colorText);
                        colorText.setText((turns[turn % 2] == 'w') ? "White" : "Black");

                        //The only move that gets put into the recording
                        //      arrlist is the first move
                        //Other moves, during playback will be handled during playback
                        recording.push(lastMoves[0]);

                        //Move sprites around
                        for (Move move : lastMoves)
                        {
                            //For each move in the moves array, move the
                            //      drawn pieces accordingly

                            Piece mover = move.getMoved();
                            Piece overwritten = move.getOverwritten();

                            Point from = move.getMoveFrom();
                            Point to   = move.getMoveTo();

                            View moveView = mover.getView();
                            View overView = overwritten.getView();


                            View grid = main.findViewById(R.id.grid);

                            if (!from.equals(-1, -1))
                            {
                                RelativeLayout rlMover       = grid.findViewWithTag("position_" + (from.x + 1) + "_" + (from.y + 1));
                                RelativeLayout rlOverwritten = grid.findViewWithTag("position_" + (to.x + 1) + "_" + (to.y + 1));

                                //Moving views
                                rlMover.removeView(moveView);
                                if (!(overwritten instanceof Empty)) rlOverwritten.removeView(overView);
                                rlOverwritten.addView(moveView);

                            }
                            else
                            {
                                RelativeLayout rlOverwritten = grid.findViewWithTag("position_" + (to.x + 1) + "_" + (to.y + 1));
                                rlOverwritten.removeView(overView);
                            }


                        }

                        if (message != null)
                        {

                            if (message.equals("auto-rankup") || rankTo != -1)
                            {
                                rankup(rankTo == -1 ? 0 : rankTo, lastMoves[0].getMoveTo().x, lastMoves[0].getMoveTo().y);
                                turn--;
                                int res = b.doCheck();
                                turn++;
                                if (res == 3) message = "checked-opponent";
                                else if (res == 4) message = "checkmate";
                            }
                            if (message.equals("checkmate"))
                            {
                                //Display check mate message

                                Bundle b = new Bundle();
                                b.putInt(ChessDialog.MESSAGE_TYPE, ChessDialog.TYPE_RESIGN);
                                b.putChar(ChessDialog.RESIGN_COLOR, 'w');
                                b.putString(ChessDialog.MESSAGE_KEY, ((turns[turn % 2] == 'w') ? "White" : "Black") +  " checkmated.  " +
                                        ((turns[turn % 2] == 'w') ? "Black" : "White") + " wins!" + ((playBack == null) ? "\n\nWould you like to save your recording?" : ""));
                                b.putBoolean(ChessDialog.ASK_RECO, playBack == null);
                                DialogFragment newFragment = new ChessDialog();
                                newFragment.setArguments(b);
                                newFragment.show(MainActivity.bf.getParentFragmentManager(), "badfields");

                            }
                            else if (message.equals("checked-opponent") && !noUndo)
                            {
                                //Display opponent checked message

                                Bundle b = new Bundle();
                                b.putInt(ChessDialog.MESSAGE_TYPE, ChessDialog.TYPE_MESSAGE);
                                b.putChar(ChessDialog.RESIGN_COLOR, 'w');
                                b.putString(ChessDialog.MESSAGE_KEY, ((turns[turn % 2] == 'w') ? "White" : "Black") +  " king is checked!");
                                b.putBoolean(ChessDialog.ASK_RECO, true);
                                DialogFragment newFragment = new ChessDialog();
                                newFragment.setArguments(b);
                                newFragment.show(MainActivity.bf.getParentFragmentManager(), "badfields");


                            }


                        }

                        ((Button) main.findViewById(R.id.undo)).setEnabled(!noUndo);

                    }
                    else
                    {
                        //Display error message (either you checked yourself, or you stayed in check)

                        Bundle bundle = new Bundle();
                        bundle.putInt(ChessDialog.MESSAGE_TYPE, ChessDialog.TYPE_MESSAGE);
                        bundle.putString(ChessDialog.MESSAGE_KEY,  message.equals("still-checked") ? "You must make a move to take your king out of a checked position!" : "That would check your king!");
                        DialogFragment newFragment = new ChessDialog();
                        newFragment.setArguments(bundle);
                        newFragment.show(getParentFragmentManager(), "badfields");

                    }




                    attackablePoints = new ArrayList<Point>();
                    selected = null;
                    selectedTile = null;


                    //If double moved did not change over the course of the move, then \
                    //      we turn it to null
                    if (doubleMovedBackup == lastDoubleMoved) lastDoubleMoved = null;

                    if (lastDoubleMoved != null) prevLastDoubleMoved = lastDoubleMoved;
                }
                else
                {

                    //If selected is not your color, display an error message

                    selected = null;
                    attackablePoints = new ArrayList<Point>();
                    Bundle bundle = new Bundle();
                    bundle.putInt(ChessDialog.MESSAGE_TYPE, ChessDialog.TYPE_MESSAGE);
                    bundle.putString(ChessDialog.MESSAGE_KEY, "You can't attempt to move a piece that isn't yours!");
                    DialogFragment newFragment = new ChessDialog();
                    newFragment.setArguments(bundle);
                    newFragment.show(getParentFragmentManager(), "badfields");
                    return;
                }
            }
            else
            {
                //Else, then we move the selected cursor to a new tile
                //Code for targeting a spot on the board
                LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT
                );
                ImageView imageView = new ImageView(main);
                imageView.setTag("shine");
                imageView.setBackgroundColor(Color.parseColor("#FFFB33"));
                imageView.setAlpha(0.7f);
                imageParams.width  = ViewGroup.LayoutParams.MATCH_PARENT;
                imageParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
                imageView.setLayoutParams(imageParams);
                relativeLayout.addView(imageView);
                selectedTile = new Point(row, col);
                selected = b.getPiece(selectedTile);
                attackablePoints = selected.getMoves();
                for (Point p : attackablePoints)
                {
                    View prev = (main.findViewById(R.id.grid));
                    RelativeLayout rl = prev.findViewWithTag("position_" + (p.x + 1) + "_" + (p.y + 1));
                    LinearLayout.LayoutParams attackParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                    ImageView attackView = new ImageView(main);
                    attackView.setTag("attack_" + (p.x + 1) + "_" + (p.y + 1));
                    attackView.setBackgroundColor(Color.parseColor("#ff0000"));
                    attackView.setAlpha(0.4f);
                    attackParams.width  = ViewGroup.LayoutParams.MATCH_PARENT;
                    attackParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
                    attackView.setLayoutParams(attackParams);
                    rl.addView(attackView);
                }
            }

        }
        else
        {
            //Code for when you have selected a point and then you click the
            //      the same point again
            cleanSelected();
        }

    }


    private void cleanAttacking()
    {
        if (attackablePoints != null && attackablePoints.size() != 0)
        {

            for (Point p : attackablePoints)
            {
                View prev = (main.findViewById(R.id.grid));
                RelativeLayout rl = prev.findViewWithTag("position_" + (p.x + 1) + "_" + (p.y + 1));

                rl.removeView(rl.findViewWithTag("attack_" + (p.x + 1) + "_" + (p.y + 1)));

            }

        }
    }

    private void cleanSelected()
    {
        View shine = main.findViewById(R.id.grid)
                .findViewWithTag("shine");
        RelativeLayout shineLayout = shine != null ? (RelativeLayout) shine.getParent() : null;
        if (shineLayout != null) shineLayout.removeView(shine);
        attackablePoints = new ArrayList<Point>();
        selectedTile = null;
    }

    public void undo(View view)
    {
        undo(true);
    }



    public void undo(boolean decTurn)
    {
        if (!recording.empty())
        {

            cleanAttacking();
            cleanSelected();

            b.undo(lastMoves);

            lastDoubleMoved = prevLastDoubleMoved;

            //Move sprites around
            for (Move move : lastMoves)
            {
                //For each move in the moves array, move the
                //      drawn pieces accordingly
                Piece mover = move.getMoved();
                Piece overwritten = move.getOverwritten();

                Point from = move.getMoveFrom();
                Point to   = move.getMoveTo();

                View moveView = mover.getView();
                View overView = overwritten.getView();


                View grid = main.findViewById(R.id.grid);

                if (from.equals(-1, -1))
                {
                    RelativeLayout rlTo = grid.findViewWithTag("position_" + (to.x + 1) + "_" + (to.y + 1));
                    rlTo.addView(overView);
                }
                else if (to.equals(-1, -1))
                {
                    RelativeLayout rlFrom = grid.findViewWithTag("position_" + (from.x + 1) + "_" + (from.y + 1));
                    rlFrom.removeView(moveView);
                }
                else
                {
                    RelativeLayout rlFrom = grid.findViewWithTag("position_" + (from.x + 1) + "_" + (from.y + 1));
                    RelativeLayout rlTo   = grid.findViewWithTag("position_" + (to.x + 1) + "_" + (to.y + 1));

                    //Moving views
                    rlTo.removeView(moveView);
                    if (!(overwritten instanceof Empty)) rlTo.addView(overView);
                    rlFrom.addView(moveView);
                }
            }
            turn--;

            //Change display for current player turn
            TextView colorText = main.findViewById(R.id.colorText);
            colorText.setText((turns[turn % 2] == 'w') ? "White" : "Black");
            ((Button) main.findViewById(R.id.undo)).setEnabled(false);
        }
    }

    public void draw(View view)
    {
        Bundle bundle = new Bundle();
        bundle.putInt(ChessDialog.MESSAGE_TYPE, ChessDialog.TYPE_DRAW);
        bundle.putString(ChessDialog.MESSAGE_KEY, "" + (turns[turn % 2] == 'w' ? "White" : "Black") + " has requested a draw!  Does " + (turns[turn % 2] != 'w' ? "White" : "Black") + " accept?");
        DialogFragment newFragment = new ChessDialog();
        newFragment.setArguments(bundle);
        newFragment.show(getParentFragmentManager(), "badfields");
        return;
    }


    public void resign(View view)
    {
        selected = null;
        attackablePoints = new ArrayList<Point>();
        Bundle bundle = new Bundle();
        bundle.putInt(ChessDialog.MESSAGE_TYPE, ChessDialog.TYPE_RESIGN);
        bundle.putChar(ChessDialog.RESIGN_COLOR, turns[turn % 2]);
        bundle.putBoolean(ChessDialog.ASK_RECO, true);

        bundle.putString(ChessDialog.MESSAGE_KEY, "" + (turns[turn % 2] == 'w' ? "White" : "Black") + " has resigned!  " + (turns[turn % 2] != 'w' ? "White" : "Black") + " wins!" + ((playBack == null) ? "\n\nWould you like to save your recording?" : ""));
        DialogFragment newFragment = new ChessDialog();
        newFragment.setArguments(bundle);
        newFragment.show(getParentFragmentManager(), "badfields");
        return;
    }


    public void rankup(int rank, int row, int col)
    {
        switch (rank)
        {
            case 0:
            {
                RelativeLayout rl = main.findViewById(R.id.grid)
                        .findViewWithTag("position_" + (row + 1) + "_" + (col + 1));

                Piece current = BoardFragment.b.getPiece(new Point(row, col));

                rl.removeView(current.getView());

                ImageView iv = new ImageView(main);
                iv.setImageResource(row == 0 ?
                        R.drawable.white_queen :
                        R.drawable.black_queen);

                LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

                iv.setLayoutParams(imageParams);

                rl.addView(iv);
                Piece newPiece = new Queen(row == 0 ? 'w' : 'b', new Point(row, col), iv);

                BoardFragment.b.setPiece(newPiece, new Point(row, col));

                main.bf.rankups.put(main.bf.turn - 1, 0);


                break;
            }
            case 1:
            {

                RelativeLayout rl = main.findViewById(R.id.grid)
                        .findViewWithTag("position_" + (row + 1) + "_" + (col + 1));

                Piece current = BoardFragment.b.getPiece(new Point(row, col));

                rl.removeView(current.getView());

                ImageView iv = new ImageView(main);
                iv.setImageResource(row == 0 ?
                        R.drawable.white_rook :
                        R.drawable.black_rook);

                LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

                iv.setLayoutParams(imageParams);

                rl.addView(iv);
                Piece newPiece = new Rook(row == 0 ? 'w' : 'b', new Point(row, col), iv);

                BoardFragment.b.setPiece(newPiece, new Point(row, col));

                main.bf.rankups.put(main.bf.turn - 1, 1);

                break;
            }
            case 2:
            {
                RelativeLayout rl = main.findViewById(R.id.grid)
                        .findViewWithTag("position_" + (row + 1) + "_" + (col + 1));

                Piece current = BoardFragment.b.getPiece(new Point(row, col));

                rl.removeView(current.getView());

                ImageView iv = new ImageView(main);
                iv.setImageResource(row == 0 ?
                        R.drawable.white_bishop :
                        R.drawable.black_bishop);

                LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

                iv.setLayoutParams(imageParams);

                rl.addView(iv);
                Piece newPiece = new Bishop(row == 0 ? 'w' : 'b', new Point(row, col), iv);

                BoardFragment.b.setPiece(newPiece, new Point(row, col));

                main.bf.rankups.put(main.bf.turn - 1, 2);


                break;
            }
            case 3:
            {

                RelativeLayout rl = main.findViewById(R.id.grid)
                        .findViewWithTag("position_" + (row + 1) + "_" + (col + 1));

                Piece current = BoardFragment.b.getPiece(new Point(row, col));

                rl.removeView(current.getView());

                ImageView iv = new ImageView(main);
                iv.setImageResource(row == 0 ?
                        R.drawable.white_knight :
                        R.drawable.black_knight);

                LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

                iv.setLayoutParams(imageParams);

                rl.addView(iv);
                Piece newPiece = new Knight(row == 0 ? 'w' : 'b', new Point(row, col), iv);

                BoardFragment.b.setPiece(newPiece, new Point(row, col));

                main.bf.rankups.put(main.bf.turn - 1, 3);

                break;
            }
            case 4:
            {
                break;
            }
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public void ai(View view)
    {
        ArrayList<Piece> pieces = b.getAllPieces();
        Object[] myColor = pieces.stream().filter((piece) -> {
            return piece.getColor() == turns[turn % 2];
        }).toArray();

        King me = b.getKings()[turn %  2];

        int[] arr = new Random().ints(1, 0, myColor.length).toArray();
        int index = arr[0];

        Piece aiPiece = (Piece) myColor[index];

        ArrayList<Point> aiMoves = aiPiece.getMoves();
        if (aiMoves.size() > 0)
        {
            arr = new Random().ints(1, 0, aiMoves.size()).toArray();
            index = arr[0];

            b.copiedBoard = true;

            Object[] result = b.movePiece(new Point(aiPiece.getLocation().x, aiPiece.getLocation().y), aiMoves.get(index));

            String message = (String) result[0];
            Move[] moves   = (Move[]) result[1];

            if (message != null && (message.equals("checked-self") || message.equals("still-checked")))
            {

                ai(view);
                return;
            }




            System.out.println(aiPiece.getLocation());
            System.out.println(aiMoves.get(index));
            System.out.println(b);

            turn++;
            b.undo(moves);
            turn--;


            boardClick(aiPiece.getLocation().x, aiPiece.getLocation().y);
            boardClick(aiMoves.get(index).x, aiMoves.get(index).y);


            b.copiedBoard = false;

        }
        else
        {
            //Else, pick again
            ai(view);
        }
    }


    private int playBackTurn = 0;
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void turn(View view) throws InterruptedException
    {
        b.copiedBoard = true;
        try
        {
            Point[] pTurn = playBack.getTurn(playBackTurn);

            Point to   = pTurn[1];
            Point from = pTurn[0];

            Integer rank = playBack.getRank(playBackTurn++);

            if (rank == null)
            {
                rank = -1;
            }

            boardClick(from.x, from.y, rank);
            boardClick(to.x, to.y, rank);



        }
        catch (Exception e)
        {
            String resign = playBack.getResign();


            String win = null;

            if (resign.charAt(0) == 'd')
            {
                win = "DRAW!";
            }
            else
            {
                win = (resign.charAt(0) == 'w' ? "White" : "Black") + " has resigned!  " + (resign.charAt(0) != 'w' ? "White" : "Black") + " wins!";
            }


            Bundle bundle = new Bundle();
            bundle.putInt(ChessDialog.MESSAGE_TYPE, ChessDialog.TYPE_RESIGN);
            bundle.putChar(ChessDialog.RESIGN_COLOR, turns[turn % 2]);
            bundle.putString(ChessDialog.MESSAGE_KEY, win);
            bundle.putBoolean(ChessDialog.ASK_RECO, false);
            DialogFragment newFragment = new ChessDialog();
            newFragment.setArguments(bundle);
            newFragment.show(getParentFragmentManager(), "badfields");

        }
        b.copiedBoard = false;
    }




    public static void newGame(String recordingName, char resignColor)
    {

        if (recordingName != null)
            createRecording(recordingName, resignColor);

        BottomNavigationView bottomNavigationView;
        bottomNavigationView = (BottomNavigationView)main.findViewById(R.id.nav_view);
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);

    }

    private static void createRecording(String recordingName, char resignColor)
    {
        StringBuilder recordingContentsSB = new StringBuilder();
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Date date = new Date();
        recordingContentsSB.append(recordingName);
        recordingContentsSB.append("\n");
        recordingContentsSB.append(dateFormat.format(date));
        recordingContentsSB.append("\n");


        recordingContentsSB.append(resignColor);
        recordingContentsSB.append("\n");
        Stack<Move> q = new Stack<Move>();
        while (!recording.empty())
        {
            q.push(recording.pop());
        }

        int turnCtr = 0;
        while (!q.isEmpty())
        {
            recordingContentsSB.append(q.pop().toString());

            Integer rankup = rankups.get(turnCtr);

            if (rankup != null)
            {
                recordingContentsSB.append(';');
                recordingContentsSB.append(rankup);
            }

            recordingContentsSB.append("\n");
            turnCtr++;
        }


        String recordingStr = recordingContentsSB.toString();
        writeInternal(recordingName, recordingStr);
    }


    private static void writeInternal(String recordingName, String contents)
    {
        File file = new File(main.getFilesDir(), "recordings");
        if (!file.exists())
        {
            file.mkdir();
        }

        try
        {
            File recordingFile = new File(file, recordingName);
            FileWriter writer = new FileWriter(recordingFile);
            writer.write(contents);
            writer.flush();
            writer.close();

            Toast.makeText(main, "Recording saved!", Toast.LENGTH_LONG).show();

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }




}

