package main.java.gui;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.*;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

import main.java.locale.Translatable;
import main.java.log.Logger;

/**
 * Что требуется сделать:
 * 1. Метод создания меню перегружен функционалом и трудно читается. 
 * Следует разделить его на серию более простых методов (или вообще выделить отдельный класс).
 *
 */
public class MainApplicationFrame extends JFrame implements Serializable, Settable {
    private final JDesktopPane desktopPane = new JDesktopPane();
    private ResourceBundle currentBundle = getDefaultBundle();

    public MainApplicationFrame() {
        //Make the big window be indented 50 pixels from each edge
        //of the screen.
        int inset = 50;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset,
            screenSize.width  - inset*2,
            screenSize.height - inset*2);

        setContentPane(desktopPane);

        LogWindow logWindow = createLogWindow();
        addWindow(logWindow);

        GameWindow gameWindow = new GameWindow();
        gameWindow.setSize(400,  400);
        addWindow(gameWindow);

        RobotCoordinatesWindow coordWindow = new RobotCoordinatesWindow(gameWindow.getModel());
        coordWindow.setSize(300 ,800);
        addWindow(coordWindow);

        setJMenuBar(generateMenuBar());
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                exitMainWindow();
            }
        });

        readSettings();
        translate();
    }
    
    protected LogWindow createLogWindow() {
        LogWindow logWindow = new LogWindow(Logger.getDefaultLogSource());
        logWindow.setLocation(10,10);
        logWindow.setSize(300, 800);
        setMinimumSize(logWindow.getSize());
        logWindow.pack();
        Logger.debug("Протокол работает");
        return logWindow;
    }
    
    protected void addWindow(JInternalFrame frame) {
        desktopPane.add(frame);
        frame.setVisible(true);
        frame.addInternalFrameListener(new InternalFrameAdapter() {
            public void internalFrameClosing(InternalFrameEvent e) {
                if (confirmClosing(frame)) {
                    frame.setVisible(false);
                    frame.removeInternalFrameListener(this);
                    if (frame instanceof LogWindow) {
                        ((LogWindow)frame).freeMemory();
                    }
                    frame.dispose();
                };
            }
        });
    }

    private boolean confirmClosing(Component window) {
        Object[] options = {currentBundle.getString("accept"), currentBundle.getString("dispose")};
        int answer = JOptionPane.showOptionDialog(window,
                currentBundle.getString("exitMessage"),
                currentBundle.getString("exit"),
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null, options, options[0]);
        return answer == 0;
    }
    
//    protected JMenuBar createMenuBar() {
//        JMenuBar menuBar = new JMenuBar();
// 
//        //Set up the lone menu.
//        JMenu menu = new JMenu("Document");
//        menu.setMnemonic(KeyEvent.VK_D);
//        menuBar.add(menu);
// 
//        //Set up the first menu item.
//        JMenuItem menuItem = new JMenuItem("New");
//        menuItem.setMnemonic(KeyEvent.VK_N);
//        menuItem.setAccelerator(KeyStroke.getKeyStroke(
//                KeyEvent.VK_N, ActionEvent.ALT_MASK));
//        menuItem.setActionCommand("new");
////        menuItem.addActionListener(this);
//        menu.add(menuItem);
// 
//        //Set up the second menu item.
//        menuItem = new JMenuItem("Quit");
//        menuItem.setMnemonic(KeyEvent.VK_Q);
//        menuItem.setAccelerator(KeyStroke.getKeyStroke(
//                KeyEvent.VK_Q, ActionEvent.ALT_MASK));
//        menuItem.setActionCommand("quit");
////        menuItem.addActionListener(this);
//        menu.add(menuItem);
// 
//        return menuBar;
//    }
    
    private JMenuBar generateMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(generateLookAndFeelMenu());
        menuBar.add(generateTestMenu());
        menuBar.add(generateLangMenu());
        menuBar.add(generateExitMenu());
        return menuBar;
    }

    private JMenu generateLookAndFeelMenu() {
        JMenu lookAndFeelMenu = new JMenu(currentBundle.getString("mode"));
        lookAndFeelMenu.setMnemonic(KeyEvent.VK_V);
        {
            JMenuItem systemLookAndFeel = new JMenuItem(currentBundle.getString("system"), KeyEvent.VK_S);
            systemLookAndFeel.addActionListener((event) -> {
                setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                this.invalidate();
            });
            lookAndFeelMenu.add(systemLookAndFeel);
        }
        {
            JMenuItem crossplatformLookAndFeel = new JMenuItem(currentBundle.getString("universal"), KeyEvent.VK_S);
            crossplatformLookAndFeel.addActionListener((event) -> {
                setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                this.invalidate();
            });
            lookAndFeelMenu.add(crossplatformLookAndFeel);
        }
        return lookAndFeelMenu;
    }

    private JMenu generateTestMenu() {
        JMenu testMenu = new JMenu(currentBundle.getString("tests"));
        testMenu.setMnemonic(KeyEvent.VK_T);
        {
            JMenuItem addLogMessageItem = new JMenuItem(currentBundle.getString("logMessage"), KeyEvent.VK_S);
            addLogMessageItem.addActionListener((event) -> {
                Logger.debug(currentBundle.getString("testMessage"));
            });
            testMenu.add(addLogMessageItem);
        }
        return testMenu;
    }

    private JMenu generateLangMenu() {
        JMenu langMenu = new JMenu(currentBundle.getString("lang"));
        langMenu.setMnemonic(KeyEvent.VK_L);
        {
            JMenuItem russian = new JMenuItem("Русский");
            russian.addActionListener((event) -> {
                Locale.setDefault(new Locale("ru"));
                currentBundle = ResourceBundle.getBundle("main.java.locale.Resource", Locale.getDefault());
                translate();
            });

            JMenuItem english = new JMenuItem("English");
            english.addActionListener((event) -> {
                Locale.setDefault(new Locale("en"));
                currentBundle = ResourceBundle.getBundle("main.java.locale.Resource", Locale.getDefault());
                translate();
            });

            langMenu.add(russian);
            langMenu.add(english);
        }
        return langMenu;
    }

    private JMenu generateExitMenu() {
        JMenu exitMenu = new JMenu(currentBundle.getString("exit"));
        exitMenu.setMnemonic(KeyEvent.VK_E);
        {
            JMenuItem exitMenuItem = new JMenuItem(currentBundle.getString("exitButton"));
            exitMenuItem.addActionListener((event) -> {
                exitMainWindow();
            });
            exitMenu.add(exitMenuItem);
        }
        return exitMenu;
    }

    private void translate() {
        for (JInternalFrame frame: desktopPane.getAllFrames()) {
            ((Translatable)frame).translate(currentBundle);
        }
        setJMenuBar(generateMenuBar());
    }

    private void serialize() {
        File file = new File("data.bin");
        try (OutputStream os = new FileOutputStream(file)) {
            try (ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(os))) {
                    oos.writeObject(this);
                    for (JInternalFrame frame: desktopPane.getAllFrames()) {
                        oos.writeObject(frame);
                    }
                    oos.flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
        }
    }

    private Object writeReplace() {
        Settings settings = new Settings(getSize(), getLocationOnScreen(), getState(), getClass().getSimpleName());
        return settings;
    }

    private void exitMainWindow() {
        if (confirmClosing(MainApplicationFrame.this)) {
            serialize();
            MainApplicationFrame.this.setVisible(false);
            MainApplicationFrame.this.dispose();
            System.exit(0);
        }
    }

    private void readSettings() {
        File file = new File("data.bin");
        if (file.exists()) {
            try (InputStream is = new FileInputStream(file)) {
                try (ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(is))) {
                    Settings settings = (Settings) ois.readObject();
                    setSettings(settings);
                    for (int i = 0; i < desktopPane.getAllFrames().length; i++) {
                        settings = (Settings) ois.readObject();
                        for (JInternalFrame frame: desktopPane.getAllFrames()) {
                            if (frame.getClass().getSimpleName().equals(settings.windowName)) {
                                ((Settable)frame).setSettings(settings);
                            }
                        }
                    }
                }
                catch (EOFException ex) {
                    // just ignore
                }
                catch (IOException | ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private static ResourceBundle getDefaultBundle() {
        String defLang = Locale.getDefault().getLanguage();
        if ("en".equals(defLang) || "ru".equals(defLang)) {
            return ResourceBundle.getBundle("main.java.locale.Resource", Locale.getDefault());
        }
        return ResourceBundle.getBundle("main.java.locale.Resource", new Locale("en"));
    }

    public void setSettings(Settings settings) {
        setState(settings.state);
        setBounds(settings.location.x, settings.location.y,
                settings.screenSize.width,
                settings.screenSize.height);
    }
    
    private void setLookAndFeel(String className) {
        try {
            UIManager.setLookAndFeel(className);
            SwingUtilities.updateComponentTreeUI(this);
        }
        catch (ClassNotFoundException | InstantiationException
            | IllegalAccessException | UnsupportedLookAndFeelException e) {
            // just ignore
        }
    }
}
