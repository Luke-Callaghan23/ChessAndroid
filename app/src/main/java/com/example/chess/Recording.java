package com.example.chess;

import android.graphics.Point;

import java.util.HashMap;

public class Recording
{
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Point[] getMoveFroms() {
        return moveFroms;
    }

    public void setMoveFroms(Point[] moveFroms) {
        this.moveFroms = moveFroms;
    }

    public Point[] getMoveTos() {
        return moveTos;
    }

    public void setMoveTos(Point[] moveTos) {
        this.moveTos = moveTos;
    }

    private String title;
    private String date;
    private String resign;
    private Point[] moveFroms;
    private Point[] moveTos;
    private HashMap<Integer, Integer> rankups;


    public String getResign()
    {
        return resign;
    }


    public static Recording fromString(String contents)
    {
        Recording fs = new Recording();

        String[] lines = contents.split("\n");

        fs.title = lines[0];
        fs.date  = lines[1];
        fs.resign= lines[2];


        fs.rankups = new HashMap<Integer, Integer>();
        fs.moveFroms = new Point[lines.length - 3];
        fs.moveTos = new Point[lines.length - 3];

        for(int loop = 3; loop < lines.length; loop++)
        {
            String[] fromToSplit = lines[loop].split(";");
            String from = fromToSplit[0];
            String to   = fromToSplit[1];

            Point fromPt = pointFromString(from);
            Point toPt   = pointFromString(to);

            fs.moveFroms[loop - 3] = fromPt;
            fs.moveTos[loop - 3]   = toPt;

            if (fromToSplit.length == 3)
            {
                fs.rankups.put(loop - 3, Integer.parseInt(fromToSplit[2]));
            }

        }

        return fs;
    }

    public Integer getRank(int n) { return rankups.get(n); }

    public Point[] getTurn(int n)
    {
        return new Point[] { moveFroms[n], moveTos[n] };
    }


    private static Point pointFromString(String pt)
    {
        String[] rowCols = pt.split(",");
        int rows = Integer.parseInt(rowCols[0]);
        int cols = Integer.parseInt(rowCols[1]);
        return new Point(rows, cols);
    }

    public int getDateAsInt()
    {
        String[] splits = date.split("\\\\|/");
        int MM = Integer.parseInt(splits[0]);
        int dd = Integer.parseInt(splits[1]);
        int yyyy = Integer.parseInt(splits[2]);

        //Return general date... averages all months to 30 days
        //      and all years to 365 days
        return dd + (30 * MM) + (365 * yyyy);

    }

    public String toString()
    {
        return this.title + " " + this.date;
    }

}