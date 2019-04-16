package main.java.gui;

import java.awt.Point;
import java.awt.Dimension;
import java.io.Serializable;

public class Settings implements Serializable {
    public String windowName;
    public Dimension screenSize;
    public Point location;
    public int state;

    public Settings(Dimension size, Point loc, int state, String name) {
        screenSize = size;
        location = loc;
        this.state = state;
        windowName = name;
    }
}
