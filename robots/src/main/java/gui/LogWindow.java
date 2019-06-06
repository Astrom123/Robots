package main.java.gui;

import java.awt.TextArea;
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Point;
import java.beans.PropertyVetoException;
import java.io.Serializable;
import java.util.ResourceBundle;

import javax.swing.JPanel;
import javax.swing.JInternalFrame;

import main.java.locale.Translatable;
import main.java.log.LogChangeListener;
import main.java.log.LogEntry;
import main.java.log.LogWindowSource;

public class LogWindow extends JInternalFrame implements LogChangeListener, Serializable, Settable, Translatable {
    private LogWindowSource m_logSource;
    private TextArea m_logContent;

    public LogWindow(LogWindowSource logSource) {
        super("Протокол работы", true, true, true, true);
        m_logSource = logSource;
        m_logSource.registerListener(this);
        m_logContent = new TextArea("");
        m_logContent.setSize(200, 500);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_logContent, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
        updateLogContent();
    }

    private void updateLogContent() {
        StringBuilder content = new StringBuilder();
        for (LogEntry entry : m_logSource.all()) {
            content.append(entry.getMessage()).append("\n");
        }
        m_logContent.setText(content.toString());
        m_logContent.invalidate();
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

    public void freeMemory()
    {
        m_logSource.unregisterListener(this);
    }
    
    @Override
    public void onLogChanged()
    {
        EventQueue.invokeLater(this::updateLogContent);
    }

    public void translate(ResourceBundle bundle) {
        setTitle(bundle.getString("logWindow"));
    }
}
