package com.example.chess;



import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompatSideChannelService;
import androidx.fragment.app.DialogFragment;

import com.example.chess.ui.home.BoardFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChessDialog extends DialogFragment
{
    public static final String MESSAGE_KEY = "message_key";
    public static final String MESSAGE_TYPE = "message_type";
    public static final String MESSAGE_FRAGMENT = "message_fragment";
    public static final String RESIGN_COLOR = "resign_color";
    public static final String ASK_RECO = "ask_reco";

    public static final int TYPE_MESSAGE = 0;
    public static final int TYPE_RESIGN  = 1;
    public static final int TYPE_DRAW    = 2;
    public static final int TYPE_RECORDING_NAME = 3;
    public static final int TYPE_RANKUP = 4;
    public static final int TYPE_PICK_RECORDING = 5;
    public static final String RANKUP_ROW = "row";
    public static final String RANKUP_COL = "col";

    private enum e { QUEEN, ROOK, BISHOP, KNIGHT, PAWN };

    private int myType;

    private AlertDialog myDialog;

    private MainActivity main;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        Bundle bundle = getArguments();
        AlertDialog.Builder builder =
                new AlertDialog.Builder(getActivity());

        myType = bundle.getInt(MESSAGE_TYPE);
        main = (MainActivity) getParentFragment().getActivity();
        switch(myType)
        {
            case TYPE_MESSAGE:
            {
                builder.setMessage(bundle.getString(MESSAGE_KEY))
                        .setPositiveButton("OK", (dialog, id) -> {  });
                break;
            }
            case TYPE_RESIGN:
            {
                builder.setMessage(bundle.getString(MESSAGE_KEY));

                if (bundle.getBoolean(ChessDialog.ASK_RECO))
                {

                    builder.setPositiveButton("Save", (dialog, id) -> {
                            Bundle b = new Bundle();
                            b.putInt(ChessDialog.MESSAGE_TYPE, ChessDialog.TYPE_RECORDING_NAME);
                            b.putChar(ChessDialog.RESIGN_COLOR, bundle.getChar(ChessDialog.RESIGN_COLOR));
                            b.putString(ChessDialog.MESSAGE_KEY, "What would you like to name your recording?");
                            DialogFragment newFragment = new ChessDialog();
                            newFragment.setArguments(b);
                            newFragment.show(getParentFragmentManager(), "badfields");
                        })
                        .setNegativeButton("Don't save", (dialog, id) -> {
                            main.bf.playBack = null;
                            main.bf.turn = 0;
                            BottomNavigationView bottomNavigationView;
                            bottomNavigationView = (BottomNavigationView)main.findViewById(R.id.nav_view);
                            bottomNavigationView.setSelectedItemId(R.id.navigation_home);
                        });
                }
                else
                {
                    builder.setPositiveButton("Ok", (dialog, id) -> {
                        main.bf.playBack = null;
                        main.bf.turn = 0;
                        BottomNavigationView bottomNavigationView;
                        bottomNavigationView = (BottomNavigationView)main.findViewById(R.id.nav_view);
                        bottomNavigationView.setSelectedItemId(R.id.navigation_home);
                    });
                }
                break;
            }
            case TYPE_DRAW:
            {
                builder.setMessage(bundle.getString(MESSAGE_KEY))
                        .setPositiveButton("Accept", (dialog, id) -> {
                            Bundle b = new Bundle();
                            b.putInt(ChessDialog.MESSAGE_TYPE, ChessDialog.TYPE_RESIGN);
                            b.putBoolean(ChessDialog.ASK_RECO, true);
                            b.putChar(ChessDialog.RESIGN_COLOR, 'd');
                            b.putString(ChessDialog.MESSAGE_KEY, "Would you like to save your recording?");
                            DialogFragment newFragment = new ChessDialog();
                            newFragment.setArguments(b);
                            newFragment.show(getParentFragmentManager(), "badfields");
                        })
                        .setNegativeButton("Decline", (dialog, id) -> {

                        });
                break;
            }
            case TYPE_RECORDING_NAME:
            {
                builder.setMessage(bundle.getString(MESSAGE_KEY));

                final EditText input = new EditText(main);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT
                );
                input.setLayoutParams(lp);
                input.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if(s.toString().equals(""))
                        {
                            myDialog.getButton(Dialog.BUTTON_POSITIVE).setEnabled(false);
                        }
                        else
                        {
                            myDialog.getButton(Dialog.BUTTON_POSITIVE).setEnabled(true);
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                builder.setView(input)
                        .setPositiveButton("OK", (dialog, id) -> {
                            if (input.getText() != null && input.getText() != null
                                    && !input.getText().toString().equals(""))
                            {
                                char c = bundle.getChar(ChessDialog.RESIGN_COLOR);
                                BoardFragment.newGame(input.getText().toString(), c);
                            }
                        });

                break;
            }
            case TYPE_RANKUP:
            {
                final CharSequence[] ranks = new CharSequence[]{ "Queen", "Rook", "Bishop", "Knight", "Pawn" };
                int col = bundle.getInt(RANKUP_COL);
                int row = bundle.getInt(RANKUP_ROW);
                int checkedItem = 0;

                builder
                    .setSingleChoiceItems(ranks, checkedItem, null)
                    .setPositiveButton("Ok", (dialog, which) -> {
                        int selectedPosition = ((AlertDialog)dialog).getListView().getCheckedItemPosition();
                        main.bf.rankup(selectedPosition, row, col);

                        main.bf.turn--;

                        int result = main.bf.b.doCheck();

                        main.bf.turn++;

                        if (result == 4)
                        {
                            //If resulted in a checkMate

                            Bundle b = new Bundle();
                            b.putInt(ChessDialog.MESSAGE_TYPE, ChessDialog.TYPE_RESIGN);
                            b.putChar(ChessDialog.RESIGN_COLOR, 'w');
                            b.putString(ChessDialog.MESSAGE_KEY, ((MainActivity.bf.turns[MainActivity.bf.turn % 2] == 'w') ? "White" : "Black") +  " checkmated.  " +
                                    ((MainActivity.bf.turns[MainActivity.bf.turn % 2] == 'w') ? "Black" : "White") + " wins!\n\nWould you like to save the recording?");
                            b.putBoolean(ChessDialog.ASK_RECO, true);
                            DialogFragment newFragment = new ChessDialog();
                            newFragment.setArguments(b);
                            newFragment.show(MainActivity.bf.getParentFragmentManager(), "badfields");


                        }
                        else if (result == 3)
                        {
                            //If resulted in a check (against oponent)

                            Bundle b = new Bundle();
                            b.putInt(ChessDialog.MESSAGE_TYPE, ChessDialog.TYPE_MESSAGE);
                            b.putString(ChessDialog.MESSAGE_KEY, ((MainActivity.bf.turns[MainActivity.bf.turn % 2] == 'w') ? "White" : "Black") +  " king is checked!");
                            DialogFragment newFragment = new ChessDialog();
                            newFragment.setArguments(b);
                            newFragment.show(MainActivity.bf.getParentFragmentManager(), "badfields");

                        }

                        Move[] oldMoves = main.bf.getLastMoves();
                        Move rankupMove = new Move(main.bf.b.getPiece(new Point(row, col)), new Empty(new Point(row, col)),
                                new Point(row, col), new Point(-1, -1));
                        Move[] newMoves = new Move[] { oldMoves[0], rankupMove };
                        main.bf.setLastMoves(newMoves);


                    });
                break;

            }
            case TYPE_PICK_RECORDING:
            {
                builder.setMessage(bundle.getString(MESSAGE_KEY))
                        .setPositiveButton("Yes", (dialog, id) -> {
                            BottomNavigationView bottomNavigationView;
                            bottomNavigationView = (BottomNavigationView)main.findViewById(R.id.nav_view);
                            bottomNavigationView.setSelectedItemId(R.id.navigation_home);
                        })
                        .setNegativeButton("No", (dialog, id) -> {
                            main.bf.playBack = null;
                        });
                break;
            }
        }
        myDialog = builder.create();
        myDialog.setCanceledOnTouchOutside(false);
        return myDialog;
    }

    public void onStart()
    {
        super.onStart();
        if (myType == TYPE_RECORDING_NAME)
        {
            myDialog.getButton(Dialog.BUTTON_POSITIVE).setEnabled(false);
        }

    }


}
