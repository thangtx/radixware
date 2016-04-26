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

package org.radixware.kernel.designer.common.dialogs.chooseobject;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.Action;
import javax.swing.InputMap;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.EventListenerList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.BadLocationException;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.openide.util.ChangeSupport;
import org.openide.util.NbBundle;
import org.openide.util.RequestProcessor;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.clazz.algo.AdsAlgoClassDef;
import org.radixware.kernel.common.defs.ads.clazz.algo.object.AdsAppObject;
import org.radixware.kernel.common.defs.ads.clazz.algo.object.AdsIncludeObject;
import org.radixware.kernel.common.defs.ads.clazz.algo.object.AdsVarObject;
import org.radixware.kernel.common.utils.Utils;

public class RadixObjectsPanel extends javax.swing.JPanel {

    private static final int BRIGHTER_COLOR_COMPONENT = 10;
    private final JLabel messageLabel;
    private ChooseRadixObjectCfg cfg = null;
    private List<RadixObject> selectedDefinitions = Collections.emptyList();
    private boolean multipleSelectionAllowed = false;
    private boolean containsScrollPane;
    private Collection<? extends RadixObject> allowedObjects = null;
    private Searcher searcher = null;
    //private Definition objectToSelectAtUpdate = null;
    private JTextField edSearch = null;
    private JCheckBox cbCaseSensitive = null;
    private PatternListener pl = null;
    private int currentStepNumber = ChooseDefinitionCfg.FIRST_STEP;
    private RadixObject commonOwner = null;

    public RadixObjectsPanel() {
        initComponents();
        containsScrollPane = true;

        Color bgColorBrighter = new Color(
                Math.min(getBackground().getRed() + BRIGHTER_COLOR_COMPONENT, 255),
                Math.min(getBackground().getGreen() + BRIGHTER_COLOR_COMPONENT, 255),
                Math.min(getBackground().getBlue() + BRIGHTER_COLOR_COMPONENT, 255));

        messageLabel = new JLabel();
        messageLabel.setBackground(bgColorBrighter);
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        messageLabel.setEnabled(true);
        messageLabel.setText(NbBundle.getMessage(RadixObjectsPanel.class, "TXT_NoObjectsFound", Definition.DEFINITION_TYPES_TITLE));
        messageLabel.setFont(matchesList.getFont());
        matchesList.setCellRenderer(new ItemNameRenderer(matchesList));

        SelectionListener sl = new SelectionListener();
        matchesList.addListSelectionListener(sl);

        matchesList.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent event) {
                final String tooltip;
                final int index = matchesList.locationToIndex(event.getPoint());
                final ListModel model = matchesList.getModel();
                if (index >= 0 && model != null && index < model.getSize()) {
                    RadixObject radixObject = (RadixObject) model.getElementAt(index);
                    if (event.getX() > matchesList.getWidth() / 2 && radixObject.getOwnerForQualifedName() != null) {
                        radixObject = radixObject.getOwnerForQualifedName();
                    }
                    tooltip = radixObject.getToolTip();
                } else {
                    tooltip = null;
                }
                matchesList.setToolTipText(tooltip);
            }
        });
    }

    public void attachExternalComponents(JTextField edSearch, JCheckBox cbCaseSensitive) {
        synchronized (this) {
            if (this.edSearch != null) {
                edSearch.getDocument().removeDocumentListener(pl);
                edSearch.removeKeyListener(pl);
            }
            if (this.cbCaseSensitive != null) {
                cbCaseSensitive.removeItemListener(pl);
            }

            this.edSearch = edSearch;
            this.cbCaseSensitive = cbCaseSensitive;

            if (pl == null) {
                pl = new PatternListener();
            }

            if (edSearch != null) {
                edSearch.getDocument().addDocumentListener(pl);
                edSearch.addKeyListener(pl);
            }

            if (cbCaseSensitive != null) {
                cbCaseSensitive.addItemListener(pl);
            }

            update();
        }
    }

    public void open(ChooseRadixObjectCfg cfg) {
        synchronized (this) {
            open(cfg, this.multipleSelectionAllowed);
        }
    }

    public void open(ChooseRadixObjectCfg cfg, boolean multipleSelectionAllowed) {
        synchronized (this) {
            open(cfg, multipleSelectionAllowed, this.currentStepNumber, this.commonOwner);
        }
    }

    private Collection<? extends RadixObject> calcGrouped(Collection<? extends RadixObject> definitions) {
        final int stepCount = this.cfg.getStepCount();
        if (stepCount == 1) {
            return definitions;
        }

        final Set<RadixObject> result = new HashSet<RadixObject>(); // Set, because owners will equals for some props of one class
        for (RadixObject definition : definitions) {
            RadixObject topDefinition = definition;
            if (cfg.isForAlgo() && this.currentStepNumber == 1) {
                if (!(topDefinition instanceof AdsAppObject
                        || topDefinition instanceof AdsIncludeObject
                        || topDefinition instanceof AdsVarObject
                        || topDefinition instanceof AdsAlgoClassDef.Param
                        || topDefinition instanceof AdsAlgoClassDef.Var)) {
                    continue;
                }
            } else {
                for (int i = this.currentStepNumber; i < stepCount; i++) {
                    RadixObject nextTop = topDefinition.getOwnerForQualifedName();

                    topDefinition = nextTop;
                }
            }
            if (checkCommonOwner(topDefinition)) {
                result.add(topDefinition);
            }
        }

        return result;
    }

    private boolean checkCommonOwner(RadixObject topDefinition) {
        if (this.commonOwner == null) {
            return true;
        } else {
            RadixObject match = this.commonOwner;
            RadixObject test = topDefinition.getOwnerForQualifedName();
            if (match != null) {
                if (match == test) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }
    }

    private Collection<? extends RadixObject> collectAllowedObjects() {
        ChooseRadixObjectCfg curCfg = cfg;
        ProgressHandle progressHandle = ProgressHandleFactory.createHandle(cfg.getTypesTitle() + " gathering...");
        try {
            progressHandle.start();
            if (curCfg != null) {
                Collection<? extends RadixObject> result = curCfg.collectAllowedObjects();
                VisitorProvider provider = curCfg.getProvider();
                if (provider != null) {
                    provider.setCancelled(false);
                }
                result = calcGrouped(result);
                return result;
            } else {
                return Collections.emptyList();
            }
        } finally {
            progressHandle.finish();
        }
    }

    public void open(ChooseRadixObjectCfg cfg, boolean multipleSelectionAllowed, int currentStepNumber, RadixObject commonOwner) {
        synchronized (this) {
            this.cfg = cfg;
            this.multipleSelectionAllowed = multipleSelectionAllowed;
            this.currentStepNumber = currentStepNumber;
            this.commonOwner = commonOwner;

            if (cfg.getDisplayMode() == EChooseDefinitionDisplayMode.NAME_AND_LOCATION) {
                matchesList.setCellRenderer(new ItemNameRenderer(matchesList));
            } else {
                matchesList.setCellRenderer(new ItemQualifiedNameRenderer(matchesList));
            }

            matchesList.setSelectionMode(multipleSelectionAllowed ? ListSelectionModel.MULTIPLE_INTERVAL_SELECTION : ListSelectionModel.SINGLE_SELECTION);
            this.searcher = null; // research
            this.allowedObjects = null;
            update();
        }
    }

    public void update() {
        synchronized (this) {
            if (searcher == null && cfg != null) {
                // research
                setListPanelContent(NbBundle.getMessage(RadixObjectsPanel.class, "TXT_Searching"));
                final ListModel model = new DefinitionsPanelModel();
                matchesList.setModel(model);
                searcher = new Searcher();
                RequestProcessor.getDefault().post(searcher, 220); // 220 - from Netbeans sources, otherwise dialog will wait searching before displaying.
            } else if (allowedObjects != null) {
                // search completed
                final String searchText = getSearchText();
                final boolean isCaseSensitive = isCaseSensitive();

                final ListModel model = new DefinitionsPanelModel(cfg.getAdditionTextProvider(), allowedObjects, searchText, isCaseSensitive, cfg.getDisplayMode());
                matchesList.setModel(model);

                if (model.getSize() > 0) {
//                    boolean selected = false;
//                    if (objectToSelectAtUpdate != null) {
//                        for (int i = 0; i < model.getSize(); i++) {
//                            if (objectToSelectAtUpdate.equals(model.getElementAt(i))) {
//                                matchesList.setSelectedIndex(i);
//                                selected = true;
//                                break;
//                            }
//                        }
//                    }
//
//                    if (!selected) {
                    matchesList.setSelectedIndex(0);
                    //}

                    setListPanelContent(null);
                } else {
                    setListPanelContent(NbBundle.getMessage(RadixObjectsPanel.class, "TXT_NoObjectsFound", cfg.getTypesTitle()));
                }
                //objectToSelectAtUpdate = null;
            }

            //final List<Definition> oldSelection = selectedDefinitions;
        }
    }

    private class Searcher implements Runnable {

        @Override
        public void run() {
            Collection<? extends RadixObject> allowedObjects = collectAllowedObjects();
            synchronized (RadixObjectsPanel.this) {
                RadixObjectsPanel.this.allowedObjects = allowedObjects;
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        update();
                    }
                });
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("deprecation")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        matchesScrollPane1 = new javax.swing.JScrollPane();
        matchesList = new javax.swing.JList();

        setFocusable(false);
        setLayout(new java.awt.BorderLayout());

        matchesScrollPane1.setBorder(null);
        matchesScrollPane1.setFocusable(false);

        matchesList.setFocusable(false);
        matchesList.setVisibleRowCount(15);
        matchesList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                matchesListMouseReleased(evt);
            }
        });
        matchesScrollPane1.setViewportView(matchesList);

        add(matchesScrollPane1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void matchesListMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_matchesListMouseReleased
        if (evt.getClickCount() == 2 && hasSelection()) {
            for (ActionListener actionListener : actionSupport.getListeners(ActionListener.class)) {
                actionListener.actionPerformed(null);
            }
        }
    }//GEN-LAST:event_matchesListMouseReleased
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JList matchesList;
    private javax.swing.JScrollPane matchesScrollPane1;
    // End of variables declaration//GEN-END:variables

    private String getSearchText() {
        synchronized (this) {
            if (edSearch == null) {
                return null;
            }
            try {
                String text = edSearch.getDocument().getText(0, edSearch.getDocument().getLength());
                return text;
            } catch (BadLocationException ex) {
                return null;
            }
        }
    }

    private boolean isCaseSensitive() {
        synchronized (this) {
            return cbCaseSensitive != null && cbCaseSensitive.isSelected();
        }
    }

    private void setListPanelContent(String message) {
        if (message == null && !containsScrollPane) {
            this.remove(messageLabel);
            this.add(matchesScrollPane1);
            containsScrollPane = true;
            revalidate();
            repaint();
        } else if (message != null) {
            messageLabel.setText(message);
            if (containsScrollPane) {
                this.remove(matchesScrollPane1);
                this.add(messageLabel);
                containsScrollPane = false;
            }
            revalidate();
            repaint();
        }
    }

    private class SelectionListener implements ListSelectionListener {

        @Override
        public void valueChanged(ListSelectionEvent ev) {
            checkAndFireSelectionChanged();
        }
    }

    private List<RadixObject> calcSelection() {
        int[] selectedIndices = matchesList.getSelectedIndices();
        if (selectedIndices != null && selectedIndices.length > 0) {
            final List<RadixObject> result = new ArrayList<>(selectedIndices.length);
            for (int idx : selectedIndices) {
                result.add((RadixObject) matchesList.getModel().getElementAt(idx));
            }
            return result;
        } else {
            return Collections.emptyList();
        }
    }

    private void checkAndFireSelectionChanged() {
        final boolean changed;

        synchronized (this) {
            final List<RadixObject> newSelection = calcSelection();
            changed = !Utils.equals(selectedDefinitions, newSelection);
            if (changed) {
                selectedDefinitions = newSelection;
            }
        }

        if (changed) {
            changeSupport.fireChange();
        }
    }

    private String listActionFor(KeyEvent ev) {
        InputMap map = matchesList.getInputMap();
        Object o = map.get(KeyStroke.getKeyStrokeForEvent(ev));
        if (o instanceof String) {
            return (String) o;
        } else {
            return null;
        }
    }

    private boolean boundScrollingKey(KeyEvent ev) {
        if (!this.isShowing()) {
            return false;
        }
        String action = listActionFor(ev);
        return "selectPreviousRow".equals(action)
                || "selectNextRow".equals(action)
                || "scrollUp".equals(action)
                || "scrollDown".equals(action);
    }

    private void delegateScrollingKey(KeyEvent ev) {
        String action = listActionFor(ev);

        if ("selectNextRow".equals(action)
                && matchesList.getSelectedIndex() == matchesList.getModel().getSize() - 1) {
            matchesList.setSelectedIndex(0);
            matchesList.ensureIndexIsVisible(0);
            return;
        } else if ("selectPreviousRow".equals(action)
                && matchesList.getSelectedIndex() == 0) {
            int last = matchesList.getModel().getSize() - 1;
            matchesList.setSelectedIndex(last);
            matchesList.ensureIndexIsVisible(last);
            return;
        }

        // Plain delegation
        Action a = matchesList.getActionMap().get(action);
        if (a != null) {
            a.actionPerformed(new ActionEvent(matchesList, 0, action));
        }
    }

    private class PatternListener implements DocumentListener, ItemListener, KeyListener {

        // Search text pattern listener ----------------------------------------------------
        @Override
        public void changedUpdate(DocumentEvent e) {
            update();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            update();
        }

        @Override
        public void insertUpdate(DocumentEvent e) {
            update();
        }

        @Override
        public void keyPressed(KeyEvent evt) {
            if (boundScrollingKey(evt)) {
                delegateScrollingKey(evt);
            }
        }

        @Override
        public void keyReleased(KeyEvent evt) {
            if (boundScrollingKey(evt)) {
                delegateScrollingKey(evt);
            }
        }

        @Override
        public void keyTyped(KeyEvent evt) {
            if (boundScrollingKey(evt)) {
                delegateScrollingKey(evt);
            }
        }

        // Case Sensitive Listener -------------------------------------------------------
        @Override
        public void itemStateChanged(final ItemEvent e) {
            update();
        }
    }

    public boolean hasSelection() {
        synchronized (this) {
            return !selectedDefinitions.isEmpty();
        }
    }

    public List<RadixObject> getSelection() {
        synchronized (this) {
            return new ArrayList<RadixObject>(selectedDefinitions);
        }
    }

    public RadixObject getSelected() {
        synchronized (this) {
            if (!selectedDefinitions.isEmpty()) {
                return selectedDefinitions.get(0);
            } else {
                return null;
            }
        }
    }
//    /**
//     * Set selected object.
//     * Because model is updated on separate thread, the object will be selected with delay.
//     */
//    public void setSelected(Definition definition) {
//        synchronized (this) {
//            if (definition != null) {
//                objectToSelectAtUpdate = definition;
//                update();
//            }
//        }
//    }
    protected ChangeSupport changeSupport = new ChangeSupport(this);

    public void addChangeListener(ChangeListener l) {
        changeSupport.addChangeListener(l);
    }

    public void removeChangeListener(ChangeListener l) {
        changeSupport.removeChangeListener(l);
    }
    protected EventListenerList actionSupport = new EventListenerList();

    /**
     * Add listener to listen item doble-click.
     */
    public void addActionListener(ActionListener l) {
        actionSupport.add(ActionListener.class, l);
    }

    public void removeActionListener(ActionListener l) {
        actionSupport.remove(ActionListener.class, l);
    }
}
