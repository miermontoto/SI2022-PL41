package g41.si2022.ui.panels;

import java.util.Map;
import java.util.TreeMap;

import javax.swing.JComponent;
import javax.swing.JTabbedPane;

import g41.si2022.mvc.View;
import g41.si2022.ui.SwingMain;

public abstract class TabbedFrame {
    JTabbedPane tabbedPane;
    SwingMain main;
    Map<String, View> tabs;

    TabbedFrame(SwingMain main) {
        this.main = main;
        tabbedPane = new JTabbedPane();
        tabbedPane.addChangeListener((e) -> {
        	if (tabbedPane.getSelectedComponent() != null &&
        		((View) tabbedPane.getSelectedComponent()).isViewCreated())
        		((View) tabbedPane.getSelectedComponent()).initVolatileData();
        });
        tabs = new TreeMap<>();
        main.setNavigation(true);
    }

    void addTabs() {
        tabs.forEach((name, tab) -> {
        	tabbedPane.addTab(name, tab);
        });
    }

    public Map<String, View> getTabs () {
    	return this.tabs;
    }

    public JComponent getComponent() {
        return tabbedPane;
    }
}
