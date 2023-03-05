package g41.si2022.ui;

import java.util.Map;
import java.util.TreeMap;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

public abstract class TabbedFrame {
    JTabbedPane tabbedPane;
    JFrame frame;
    SwingMain main;
    Map<String, Tab> tabs;

    TabbedFrame(SwingMain main) {
        this.main = main;
        frame = new JFrame();
        frame.setVisible(true);
        frame.setSize(640, 480);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        tabbedPane = new JTabbedPane();
        frame.add(tabbedPane);
        tabs = new TreeMap<>();
    }

    void addTabs() {
        tabs.forEach((name, tab) -> tabbedPane.addTab(name, tab));
    }
}
