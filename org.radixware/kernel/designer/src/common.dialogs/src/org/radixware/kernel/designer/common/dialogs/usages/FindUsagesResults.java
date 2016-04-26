/*
 * Copyright (c) 2008-2015, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. This Source Code is distributed
 * WITHOUT ANY WARRANTY; including any implied warranties but not limited to
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * Mozilla Public License, v. 2.0. for more details.
 */

package org.radixware.kernel.designer.common.dialogs.usages;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTabbedPane;
import javax.swing.text.DefaultEditorKit;
import org.openide.awt.MouseUtils;
import org.openide.awt.TabbedPaneFactory;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.designer.common.dialogs.results.ResultsToolbar;
import org.radixware.kernel.common.resources.RadixWareIcons;


public class FindUsagesResults extends TopComponent {

    private class FindUsagesResultsPanel extends JPanel {

        private final ResultsToolbar toolbar;
        private final FindUsagesResultsTree tree;

        public FindUsagesResultsPanel() {
            super(new BorderLayout());

            tree = new FindUsagesResultsTree();
            add(tree, BorderLayout.CENTER);

            toolbar = new ResultsToolbar(tree);
            add(toolbar, BorderLayout.NORTH);

            tree.setFilter(toolbar);
        }

        public FindUsagesResultsTree getFindUsagesResultsTree() {
            return tree;
        }
    }

    /**
     * Class to showing popup menu
     */
    private class PopupListener extends MouseUtils.PopupMouseAdapter {

        /**
         * Called when the sequence of mouse events should lead to actual
         * showing popup menu
         */
        protected void showPopup(MouseEvent e) {
            pop.show(FindUsagesResults.this, e.getX(), e.getY());
        }
    } // end of PopupListener

    private class CloseListener implements PropertyChangeListener {

        public void propertyChange(java.beans.PropertyChangeEvent evt) {
            if (TabbedPaneFactory.PROP_CLOSE.equals(evt.getPropertyName())) {
                removeFindUsagesResultsPanel((FindUsagesResultsPanel) evt.getNewValue());
            }
        }
    }

    private class Close extends AbstractAction {

        public Close() {
            super(NbBundle.getMessage(FindUsagesResults.class, "LBL_CloseWindow"));
        }

        public void actionPerformed(ActionEvent e) {
            removeFindUsagesResultsPanel(null);
        }
    }

    private final class CloseAll extends AbstractAction {

        public CloseAll() {
            super(NbBundle.getMessage(FindUsagesResults.class, "LBL_CloseAll"));
        }

        public void actionPerformed(ActionEvent e) {
            close();
        }
    }

    private class CloseAllButCurrent extends AbstractAction {

        public CloseAllButCurrent() {
            super(NbBundle.getMessage(FindUsagesResults.class, "LBL_CloseAllButCurrent"));
        }

        public void actionPerformed(ActionEvent e) {
            closeAllButCurrent();
        }
    }
//    private final class PrevNextAction extends javax.swing.AbstractAction {
//        private boolean prev;
//
//        public PrevNextAction (boolean prev) {
//            this.prev = prev;
//        }
//
//        public void actionPerformed (java.awt.event.ActionEvent actionEvent) {
//            RefactoringPanel panel = getCurrentPanel();
//            if (panel != null) {
//                if (prev) {
//                    panel.selectPrevUsage();
//                } else {
//                    panel.selectNextUsage();
//                }
//            }
//        }
//    }
    private static FindUsagesResults instance;
    private static final String PREFERRED_ID = "FindUsagesResults";
    private transient boolean isVisible = false;
    private final JPopupMenu pop;
    private final PopupListener listener;
    private final CloseListener closeL;
    private final Map<Definition, FindUsagesResultsPanel> resultsPanelForDefinition =
            new HashMap<Definition, FindUsagesResultsPanel>();
    private final Map<FindUsagesResultsPanel, Definition> definitionForResultsPanel =
            new HashMap<FindUsagesResultsPanel, Definition>();

//    private final JTabbedPane tabbedPane;
    protected FindUsagesResults() {
        super();

        setLayout(new BorderLayout());
        setName(NbBundle.getMessage(FindUsagesResults.class, "CTL_FindUsagesResults"));
        setToolTipText(NbBundle.getMessage(FindUsagesResults.class, "HINT_FindUsagesResults"));
        setIcon(RadixWareIcons.TREE.DEPENDENCIES.getImage());

        setFocusable(true);
        setMinimumSize(new Dimension(1, 1));
//        getAccessibleContext().setAccessibleDescription(
//            NbBundle.getMessage(FindUsagesResults.class, "ACSD_usagesPanel")
//        );
        pop = new JPopupMenu();
        pop.add(new Close());
        pop.add(new CloseAll());
        pop.add(new CloseAllButCurrent());
        listener = new PopupListener();
        closeL = new CloseListener();
//        setFocusCycleRoot(true);
        JLabel label = new JLabel(NbBundle.getMessage(FindUsagesResults.class, "LBL_NoUsages"));
        label.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        this.add(label, BorderLayout.CENTER);
//        initActions();

//        tabbedPane = TabbedPaneFactory.createCloseButtonTabbedPane();
//        tabbedPane.addChangeListener(new ChangeListener() {
//
//            @Override
//            public void stateChanged(ChangeEvent e) {
//                update();
//            }
//        });


//        add(tabbedPane, BorderLayout.CENTER);

        getActionMap().put(DefaultEditorKit.copyAction, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FindUsagesResultsPanel panel = getCurrentPanel();
                if (panel != null) {
                    panel.getFindUsagesResultsTree().copyToClipboard();
                }
            }
        });
    }

//    private void initActions() {
//        ActionMap map = getActionMap();
//
//        map.put("jumpNext", new PrevNextAction (false)); // NOI18N
//        map.put("jumpPrev", new PrevNextAction (true)); // NOI18N
//    }
    void removeFindUsagesResultsPanel(FindUsagesResultsPanel panel) {
//        RefactoringPanel.checkEventThread();
        Component comp = getComponentCount() > 0 ? getComponent(0) : null;
        if (comp instanceof JTabbedPane) {
            JTabbedPane tabs = (JTabbedPane) comp;
            if (panel == null) {
                panel = (FindUsagesResultsPanel) tabs.getSelectedComponent();
            }
            tabs.remove(panel);
            if (tabs.getComponentCount() == 4) {
                Component c = tabs.getComponent(3);
                tabs.removeMouseListener(listener);
                tabs.removePropertyChangeListener(closeL);
                remove(tabs);
                add(c, BorderLayout.CENTER);
            }
        } else {
            if (comp != null) {
                remove(comp);
            }
            isVisible = false;
            close();
        }
        if (panel != null) {
            panel.getFindUsagesResultsTree().clear();
            resultsPanelForDefinition.remove(definitionForResultsPanel.get(panel));
            definitionForResultsPanel.remove(panel);
        }
        validate();
    }

    public static synchronized FindUsagesResults getDefault() {
        if (instance == null) {
            instance = new FindUsagesResults();
        }
        return instance;
    }

    @Override
    protected String preferredID() {
        return PREFERRED_ID;
    }

//    @Override
//    public HelpCtx getHelpCtx() {
//        return new HelpCtx(RefactoringPanelContainer.class.getName() + (isRefactoring ? ".refactoring-preview" : ".find-usages") ); //NOI18N
//    }
    private FindUsagesResultsPanel getCurrentPanel() {
        if (getComponentCount() > 0) {
            Component comp = getComponent(0);
            if (comp instanceof JTabbedPane) {
                JTabbedPane tabs = (JTabbedPane) comp;
                return (FindUsagesResultsPanel) tabs.getSelectedComponent();
            } else {
                if (comp instanceof FindUsagesResultsPanel) {
                    return (FindUsagesResultsPanel) comp;
                }
            }
        }
        return null;
    }

    @Override
    protected void componentActivated() {
        super.componentActivated();
        JPanel panel = getCurrentPanel();
        if (panel != null) {
            panel.requestFocus();
        }
    }

    @Override
    protected void componentClosed() {
        List<FindUsagesResultsPanel> panels = new ArrayList<FindUsagesResultsPanel>(definitionForResultsPanel.keySet());
        for (FindUsagesResultsPanel panel : panels) {
            removeFindUsagesResultsPanel(panel);
        }
//        clear();
//        isVisible = false;
//        if (getComponentCount() == 0) {
//            return ;
//        }
//        Component comp = getComponent(0);
//        if (comp instanceof JTabbedPane) {
//            JTabbedPane pane = (JTabbedPane) comp;
//            Component[] c =  pane.getComponents();
//            for (int i = 0; i< c.length; i++) {
//                ((RefactoringPanel) c[i]).close();
//            }
//        } else if (comp instanceof RefactoringPanel) {
//            ((RefactoringPanel) comp).close();
//        }
    }

    /**
     * Obtain the FindUsagesResults instance. Never call {@link #getDefault}
     * directly!
     */
    public static synchronized FindUsagesResults findInstance() {
        TopComponent win = WindowManager.getDefault().findTopComponent(PREFERRED_ID);
        if (win == null) {
            Logger.getLogger(FindUsagesResults.class.getName()).warning(
                    "Cannot find " + PREFERRED_ID + " component. It will not be located properly in the window system.");
            return getDefault();
        }
        if (win instanceof FindUsagesResults) {
            return (FindUsagesResults) win;
        }
        Logger.getLogger(FindUsagesResults.class.getName()).warning(
                "There seem to be multiple components with the '" + PREFERRED_ID
                + "' ID. That is a potential source of errors and unexpected behavior.");
        return getDefault();
    }

    /**
     * replaces this in object stream
     */
    @Override
    public Object writeReplace() {
        return new ResolvableHelper();
    }

    final static class ResolvableHelper implements Serializable {

        private static final long serialVersionUID = 1L;

        public Object readResolve() {
            return FindUsagesResults.getDefault();
        }
    }

//    private void update() {
//        Set<FindUsagesResultsPanel> panels =
//                new HashSet<FindUsagesResultsPanel>(tabbedPane.getComponentCount());
//        for (Component comp : tabbedPane.getComponents()) {
//            if (comp instanceof FindUsagesResultsPanel) {
//                panels.add((FindUsagesResultsPanel)comp);
//            }
//        }
//        for (FindUsagesResultsPanel panel : new ArrayList<FindUsagesResultsPanel>(definitionForResultsPanel.keySet())) {
//            if (!panels.contains(panel)) {
//                resultsPanelForDefinition.remove(definitionForResultsPanel.get(panel));
//                definitionForResultsPanel.remove(panel);
//            }
//        }
//    }
    public void clear(Definition definition) {
        FindUsagesResultsPanel panel = resultsPanelForDefinition.get(definition);
        if (panel != null) {
            removeFindUsagesResultsPanel(panel);
        }
//        List<FindUsagesResultsPanel> panels = new ArrayList<FindUsagesResultsPanel>(definitionForResultsPanel.keySet());
//        for (FindUsagesResultsPanel panel : panels) {
//            removeFindUsagesResultsPanel(panel);
//        }
//        if (getComponentCount() > 0) {
//            Component comp = getComponent(0);
//            if (comp instanceof JTabbedPane) {
//                JTabbedPane tabs = (JTabbedPane) comp;
//                tabs.removeAll();
//            } else {
//                if (comp instanceof FindUsagesResultsPanel) {
//                    removeFindUsagesResultsPanel((FindUsagesResultsPanel)comp);
//                }
//            }
//        }
//        definitionForResultsPanel.clear();
//        resultsPanelForDefinition.clear();
    }

    void closeAllButCurrent() {
        Component comp = getComponent(0);
        if (comp instanceof JTabbedPane) {
            JTabbedPane tabs = (JTabbedPane) comp;
            Component current = tabs.getSelectedComponent();
            Component[] c = tabs.getComponents();
            for (int i = 0; i < c.length; i++) {
                if (c[i] != current && c[i] instanceof FindUsagesResultsPanel) {
//                    ((RefactoringPanel) c[i]).close();
                    removeFindUsagesResultsPanel((FindUsagesResultsPanel) c[i]);
                }
            }
        }
    }

    private void addNewFindUsagesResultsPanel(Definition definition) {
        FindUsagesResultsPanel panel = new FindUsagesResultsPanel();
        resultsPanelForDefinition.put(definition, panel);
        definitionForResultsPanel.put(panel, definition);

//        RefactoringPanel.checkEventThread();
        if (getComponentCount() == 0) {
            add(panel, BorderLayout.CENTER);
        } else {
            Component comp = getComponent(0);
            if (comp instanceof JTabbedPane) {
                ((JTabbedPane) comp).addTab(definition.getName() + "  ", definition.getIcon().getIcon(),
                        panel, "Usages of " + definition.getName()); //NOI18N
                ((JTabbedPane) comp).setSelectedComponent(panel);
                comp.validate();
            } else if (comp instanceof JLabel) {
                remove(comp);
                add(panel, BorderLayout.CENTER);
            } else {
                remove(comp);
                JTabbedPane pane = TabbedPaneFactory.createCloseButtonTabbedPane();
                pane.addMouseListener(listener);
                pane.addPropertyChangeListener(closeL);
                add(pane, BorderLayout.CENTER);
                if (comp instanceof FindUsagesResultsPanel
                        && definitionForResultsPanel.containsKey((FindUsagesResultsPanel) comp) && definitionForResultsPanel.get((FindUsagesResultsPanel) comp) != null) {
                    Definition def = definitionForResultsPanel.get((FindUsagesResultsPanel) comp);
                    pane.addTab(def.getName() + "  ", def.getIcon().getIcon(),
                            comp, "Usages of " + def.getName()); //NOI18N
                } else {
                    pane.addTab(comp.getName() + "  ", null, comp, ((JPanel) comp).getToolTipText()); //NOI18N
                }
                pane.addTab(definition.getName() + "  ", definition.getIcon().getIcon(),
                        panel, "Usages of " + definition.getName()); //NOI18N
                pane.setSelectedComponent(panel);
                pane.validate();
            }
        }
        if (!isVisible) {
            isVisible = true;
            open();
        }
        validate();
        requestActive();
    }

    private void setSelectedComponent(FindUsagesResultsPanel panel) {
        Component comp = getComponentCount() > 0 ? getComponent(0) : null;
        if (comp instanceof JTabbedPane) {
            JTabbedPane tabs = (JTabbedPane) comp;
            tabs.setSelectedComponent(panel);
        }
    }

    public void add(Definition definition, RadixObject userSource, RadixObject usageSource) {
        if (!resultsPanelForDefinition.containsKey(definition)) {
            addNewFindUsagesResultsPanel(definition);
        }
        FindUsagesResultsPanel panel = resultsPanelForDefinition.get(definition);
        panel.getFindUsagesResultsTree().add(userSource, usageSource);
        setSelectedComponent(panel);
    }

    public boolean isEmpty(Definition definition) {
        return !resultsPanelForDefinition.containsKey(definition);
//        for (FindUsagesResultsPanel panel : definitionForResultsPanel.keySet()) {
//            if (!panel.getFindUsagesResultsTree().getRoot().isEmpty()) {
//                return false;
//            }
//        }
//        return true;
    }

    @Override
    public int getPersistenceType() {
        return PERSISTENCE_ALWAYS;
    }
}
