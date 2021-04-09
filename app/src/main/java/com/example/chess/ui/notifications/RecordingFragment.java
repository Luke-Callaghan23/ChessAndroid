package com.example.chess.ui.notifications;

import android.database.Observable;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.chess.ChessDialog;
import com.example.chess.MainActivity;
import com.example.chess.R;
import com.example.chess.Recording;
import com.example.chess.ui.home.BoardFragment;

import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

public class
RecordingFragment extends Fragment {

    private RecordingViewModel notificationsViewModel;

    private static MainActivity main;

    public static ListView gl;

    Observable<Recording> obs = new Observable<Recording>() {
        @NonNull
        @Override
        public String toString() {
            return super.toString();
        }
    };

    static Recording[] rec;
    @RequiresApi(api = Build.VERSION_CODES.O)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                ViewModelProviders.of(this).get(RecordingViewModel.class);
        View root = inflater.inflate(R.layout.fragment_recording, container, false);
        notificationsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });

        main = (MainActivity) getParentFragment().getActivity();
        //gl = (ListView) main.findViewById(R.id.gameList);

        gl = (ListView) root.findViewById(R.id.gameList);

        recordingsDir = main.getFilesDir() + File.separator + "recordings";

        MainActivity.rf = this;

        try {
            rec = readRecordings();

            if(rec != null && rec.length > 0)
            {
                ArrayAdapter<Recording> arrayAdapter = new ArrayAdapter<Recording>(
                        main,
                        android.R.layout.simple_list_item_1,
                        rec);

                gl.setAdapter(arrayAdapter);
            }
        }
        catch (IOException e){
            System.err.println(e);
        }


        gl.setOnItemClickListener((parent, view, position, id) -> {
            Recording target = rec[position];
            main.bf.playBack = target;
            Bundle bundle = new Bundle();
            bundle.putInt(ChessDialog.MESSAGE_TYPE, ChessDialog.TYPE_PICK_RECORDING);
            bundle.putString(ChessDialog.MESSAGE_KEY, "Load recording: " + target.toString());
            DialogFragment newFragment = new ChessDialog();
            newFragment.setArguments(bundle);
            newFragment.show(getParentFragmentManager(), "badfields");
        });

        return root;
    }
    public static void gameTitle(View view)
    {
        if(rec.length > 0)
        {
            Arrays.sort(rec, new Comparator<Recording>() {
                @Override
                public int compare(Recording o1, Recording o2)
                {
                    return o1.getTitle().toUpperCase().compareTo(o2.getTitle().toUpperCase());
                }
            });

            ArrayAdapter<Recording> arrayAdapter = new ArrayAdapter<Recording>(
                    main,
                    android.R.layout.simple_list_item_1,
                    rec);
            gl.setAdapter(arrayAdapter);
        }
    }

    public static void date(View view)
    {
        if(rec.length > 0)
        {
            Arrays.sort(rec, new Comparator<Recording>() {
                @Override
                public int compare(Recording o1, Recording o2)
                {
                    if(o1.getDateAsInt() > o2.getDateAsInt())
                    {
                        return 1;
                    }
                    else if(o1.getDateAsInt() < o2.getDateAsInt())
                    {
                        return -1;
                    }
                    return 0;
                }
            });

            ArrayAdapter<Recording> arrayAdapter = new ArrayAdapter<Recording>(
                    main,
                    android.R.layout.simple_list_item_1,
                    rec);
            gl.setAdapter(arrayAdapter);
        }
    }

    private static String recordingsDir;

    @RequiresApi(api = Build.VERSION_CODES.O)
    private static Recording[] readRecordings() throws IOException {
        Scanner scan;
        File recordingsLoc = new File(recordingsDir);
        File[] recordingFiles = recordingsLoc.listFiles();

        if (recordingFiles != null)
        {
            Recording[] recordings = new Recording[recordingFiles.length];



            for(int loop = 0; loop < recordingFiles.length; loop++)
            {
                File cFile = recordingFiles[loop];

                String contents = new String(Files.readAllBytes(cFile.toPath()));

                Recording cRecording = Recording.fromString(contents);

                recordings[loop] = cRecording;
            }

            for(Recording re : recordings)
            {
                System.out.println(re);
            }

            return recordings;

        }
        else
        {
            return null;
        }
    }

    public void pickRecording(View view)
    {
        ListView lv = (ListView) view;
        System.out.println(lv.getSelectedItem());
    }
}