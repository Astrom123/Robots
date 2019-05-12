package main.java.gui;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import java.awt.TextArea;
import java.awt.BorderLayout;
import java.awt.Point;
import java.beans.PropertyVetoException;
import java.io.Serializable;
import java.util.Observer;
import java.util.Observable;

public class RobotCoordinatesWindow extends JInternalFrame implements Serializable, Settable, Observer {

    private TextArea m_content;
    private Robot m_robot;

    public RobotCoordinatesWindow(Robot robot) {
        super("Координаты робота", true, true, true, true);
        m_robot = robot;
        robot.addObserver(this);
        m_content = new TextArea("");
        m_content.setSize(400, 300);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_content, BorderLayout.CENTER);
        getContentPane().add(panel);
    }

    @Override
    public void update(Observable o, Object key)
    {
        m_content.append(Integer.toString(m_robot.getX()) + " " + Integer.toString(m_robot.getY()) + "\n");
    }

    private Object writeReplace() {
        int state = isIcon() ? 1 : 0;
        Point location = isIcon() ? null : getLocation();
        Settings settings = new Settings(getSize(), location, state, getClass().getSimpleName());
        return settings;
    }

    public void setSettings(Settings settings) {
        if (settings.state == 1) {
            try {
                setIcon(true);
            } catch (PropertyVetoException e) {
                e.printStackTrace();
            }
        }
        if (settings.location == null) {
            setSize(settings.screenSize);
        } else {
            setBounds(settings.location.x, settings.location.y,
                    settings.screenSize.width,
                    settings.screenSize.height);
        }
    }
}
