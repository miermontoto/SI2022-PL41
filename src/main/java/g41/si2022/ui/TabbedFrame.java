package g41.si2022.ui;

import java.util.Map;
import java.util.TreeMap;

import javax.swing.JComponent;
import javax.swing.JTabbedPane;

public abstract class TabbedFrame {
    JTabbedPane tabbedPane;
    SwingMain main;
    Map<String, Tab> tabs;

    TabbedFrame(SwingMain main) {
        this.main = main;
        tabbedPane = new JTabbedPane();
        tabs = new TreeMap<>();
        main.setNavigation(true);

        tabbedPane.addChangeListener(e -> {
			Tab tab = (Tab) tabbedPane.getSelectedComponent();
			if (tab != null) tab.initController();
		});
    }

    void addTabs() {
        tabs.forEach((name, tab) -> tabbedPane.addTab(name, tab));
    }

    JComponent getComponent() {
        return tabbedPane;
    }
}
