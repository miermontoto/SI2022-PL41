package g41.si2022.ui.panels;

import java.util.Map;
import java.util.TreeMap;

import javax.swing.JComponent;
import javax.swing.JTabbedPane;

import g41.si2022.ui.SwingMain;
import g41.si2022.ui.Tab;

public abstract class TabbedFrame {
    JTabbedPane tabbedPane;
    SwingMain main;
    Map<String, Tab> tabs;

    TabbedFrame(SwingMain main) {
        this.main = main;
        tabbedPane = new JTabbedPane();
        tabs = new TreeMap<>();
        main.setNavigation(true);
    }

    void addTabs() {
        tabs.forEach((name, tab) -> {
        	tabbedPane.addTab(name, tab);
        });
    }
    
    public Map<String, Tab> getTabs () {
    	return this.tabs;
    }

    public JComponent getComponent() {
        return tabbedPane;
    }
}
