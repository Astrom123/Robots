package main.java.locale;

import java.util.ListResourceBundle;

public class Resource_en extends ListResourceBundle {

    @Override
    protected Object[][] getContents() {
        return new Object[][] {
                {"gameArea", "Game area"},
                {"logWindow", "Work protocol"},
                {"startMessage", "Working"},
                {"accept", "Yes"},
                {"dispose", "No"},
                {"exitMessage", "Close the window?"},
                {"exit", "Exit"},
                {"exitButton", "Exit from app"},
                {"mode", "Visual mode"},
                {"system", "System mode"},
                {"universal", "Universal mode"},
                {"lang", "Language"},
                {"tests", "Tests"},
                {"logMessage", "Message to log"},
                {"testMessage", "New string"},
                {"robotCoordsWindow", "Coordinates of robot"},
                {"coordinates", "Robot coordinates"}};
        }
    }
