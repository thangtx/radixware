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
package org.radixware.kernel.utils.traceview.window;

import java.awt.Color;
import static java.awt.Component.LEFT_ALIGNMENT;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.table.TableRowSorter;
import org.radixware.kernel.utils.traceview.TraceViewSettings.EIcon;
import org.radixware.kernel.utils.traceview.TraceViewSettings.ESeverity;
import org.radixware.kernel.utils.traceview.utils.ContextFilter;
import org.radixware.kernel.utils.traceview.utils.ContextSet;

class FilterPanelRight extends JPanel {

    private class AllActionListener implements ActionListener {

        private final int index;

        public AllActionListener(int index) {
            this.index = index;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            allCheckBoxesActionPerformed(index);
        }

    }

    private class FilterActionListener implements ActionListener {

        private final int index;

        public FilterActionListener(int index) {
            this.index = index;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            checkActionPerformed(index);
        }
    }

    private final ActionListener allSeverityListener = new AllActionListener(0);
    private final ActionListener allSourceListener = new AllActionListener(2);
    private final ActionListener allContextListener = new AllActionListener(3);

    private final ActionListener severityListener = new FilterActionListener(0);
    private final ActionListener sourceListener = new FilterActionListener(2);

    private final List<String> sourceNames;
    private ArrayList<String> arrayListName;
    private final Map<String, JCheckBox> severity = new HashMap<>();
    private final Map<String, JCheckBox> source = new HashMap<>();
    private final Map<String, JCheckBox> contextNonSelected = new HashMap<>();
    private final Map<String, JCheckBox> contextSelected = new HashMap<>();
    private final Map<String, ContextPanel> contextPanel;
    private List<ContextPanel> resultOfFindContext = new ArrayList();
    private static final Logger logger = Logger.getLogger(WindowMode.class.getName());

    private final JCheckBox severityAllCheckBox = getAllCheckBox(allSeverityListener);
    private final JCheckBox sourceAllCheckBox = getAllCheckBox(allSourceListener);
    private final JCheckBox contextAllCheckBox = getAllCheckBox(allContextListener);
    private final JTextField findFieldContext = new JTextField("");

    private final transient TableRowSorter sorter;
    private final List<RowFilter<Object, Object>> filters;
    private final TabSettings settings;

    public FilterPanelRight(List<String> sourceNames, TableRowSorter sorter, List<RowFilter<Object, Object>> filters, TabSettings settings, ArrayList arrayListName) {
        this.sorter = sorter;
        this.filters = filters;
        this.settings = settings;
        this.sourceNames = sourceNames;
        this.arrayListName = arrayListName;
        contextPanel = createContextPanelMap();
        createFilterPanelRight();
        applySettings();
    }

    private void applySettings() {
        String[] ctxs = settings.getContextList().toArray(new String[0]);
        settings.clearContextList();
        for (String ctx : ctxs) {
            addContext(ctx);
        }

        String[] contextSelected = settings.getContextSelectedList().toArray(new String[0]);
        settings.clearContextSelectedList();
        for (String ctx : contextSelected) {
            contextNonSelected.get(ctx).doClick();
        }

        String[] severitySelected = settings.getSeveritySelectedList().toArray(new String[0]);
        settings.clearSeveritySelectedList();
        for (String sev : severitySelected) {
            severity.get(sev).doClick();
        }

        String[] sourceSelected = settings.getSourceSelectedList().toArray(new String[0]);
        settings.clearSourceSelectedList();
        for (String sev : sourceSelected) {
            source.get(sev).doClick();
        }
    }

    private static List<String> getSeverityList() {
        List<String> severityList = new ArrayList<>();
        for (ESeverity sev : ESeverity.values()) {
            if (sev != ESeverity.ALARM) {
                severityList.add(sev.getFullNameInLowercase());
            }
        }
        return severityList;
    }

    private void createFilterPanelRight() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        add(new JLabel("Severity Levels:"));
        add(severityAllCheckBox);
        createCheckBox(severity, getSeverityList(), severityListener);
        add(new JLabel("    "));

        add(new JLabel("Sources:"));
        add(sourceAllCheckBox);
        createCheckBox(source, sourceNames, sourceListener);
        add(new JLabel("    "));

        add(new JLabel("Find Context:"));
        findFieldContext.setMaximumSize(new Dimension(2000, 20));
        add(findFieldContext);
        findFieldContext.addActionListener(findContextListener);

        add(getContextPanel());
        add(contextAllCheckBox);

        for (Entry<String, ContextPanel> checkBox : contextPanel.entrySet()) {
            ContextPanel panel = checkBox.getValue();
            add(panel);
            panel.setVisible(false);
        }
    }

    ActionListener findContextListener = new java.awt.event.ActionListener() {
        @Override
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            applyActionPerformed(findFieldContext);
        }
    };

    private void applyActionPerformed(JTextField findFieldContext) {
        String key = findFieldContext.getText();
        Pattern p = Pattern.compile(".*?" + key + ".*?");
        Boolean isContainsContexts;
        for (int i = 0; i < resultOfFindContext.size(); i++) {
            ContextPanel lastContext = resultOfFindContext.get(i);
            lastContext.setBackground(null);
            lastContext.setBackgroundCheckBox(null);
        }
        if (!key.isEmpty()) {
            for (Entry<String, ContextPanel> checkBox : contextPanel.entrySet()) {
                String context = checkBox.getKey();
                ContextPanel checkBoxValue = checkBox.getValue();
                Matcher m = p.matcher(context);
                isContainsContexts = m.matches();
                if (isContainsContexts) {
                    resultOfFindContext.add(checkBoxValue);
                    checkBoxValue.setBackground(Color.yellow);
                    checkBoxValue.setBackgroundCheckBox(Color.yellow);
                }
            }
        } else {
            resultOfFindContext.clear();
        }

        if (!resultOfFindContext.isEmpty()) {
            scrollRectToVisible(resultOfFindContext.get(0).getBounds());
        }

    }

    public void clickContextCheckBox(String contextName) {
        if (contextSelected.containsKey(contextName)) {
            contextNonSelected.put(contextName, contextSelected.get(contextName));
            contextSelected.remove(contextName);
        } else {
            contextSelected.put(contextName, contextNonSelected.get(contextName));
            contextNonSelected.remove(contextName);
        }

        List<RowFilter<Object, Object>> tmpFilters = new ArrayList<>();
        ContextSet cs = createContextSet();
        settings.clearContextSelectedList();
        ContextFilter contextFilter = new ContextFilter(cs, 5);
        tmpFilters.add(contextFilter);

        if (tmpFilters.isEmpty()) {
            tmpFilters.add(RowFilter.regexFilter("", 3));
        }
        filters.set(5, contextFilter);
        sorter.setRowFilter(RowFilter.andFilter(filters));

        contextAllCheckBoxUpdate();
    }

    private void contextAllCheckBoxUpdate() {
        if (contextNonSelected.isEmpty() && contextSelected.isEmpty()) {
            contextAllCheckBox.setSelected(false);
        } else {
            contextAllCheckBox.setSelected(contextNonSelected.isEmpty());
        }
    }

    private Map<String, ContextPanel> createContextPanelMap() {
        Map<String, ContextPanel> contextPanelMap = new HashMap<>();

        for (String ctxt : arrayListName) {
            ContextPanel panel = new ContextPanel(ctxt, this);
            contextPanelMap.put(ctxt, panel);
        }

        return contextPanelMap;
    }

    private static JCheckBox getAllCheckBox(ActionListener listener) {
        JCheckBox all = new JCheckBox("All");
        all.addActionListener(listener);
        return all;
    }

    private void createCheckBox(final Map<String, JCheckBox> map, List<String> names, ActionListener listener) {
        for (String name : names) {
            JCheckBox checkBox = new JCheckBox(name);
            map.put(name, checkBox);
            checkBox.addActionListener(listener);
            add(checkBox);
        }
    }

    private void addAllContext() {
        for (String name : arrayListName) {
            if (!contextSelected.containsKey(name) && !contextNonSelected.containsKey(name)) {
                ContextPanel panel = contextPanel.get(name);
                panel.setVisible(true);
                contextNonSelected.put(name, panel.getCheckBox());
                settings.addNewContext(name);
            }
        }
        contextAllCheckBox.setSelected(false);
        updateUI();
    }

    private void removeAllContext() {
        for (Entry<String, JCheckBox> checkBox : contextSelected.entrySet()) {
            checkBox.getValue().setSelected(false);
            contextPanel.get(checkBox.getKey()).setVisible(false);
        }
        for (Entry<String, JCheckBox> checkBox : contextNonSelected.entrySet()) {
            contextPanel.get(checkBox.getKey()).setVisible(false);
        }
        contextSelected.clear();
        contextNonSelected.clear();
        checkActionPerformed(3);
        contextAllCheckBox.setSelected(false);
        settings.clearContextList();
        updateUI();
    }

    public void removeContextPanel(String name, boolean isSelected) {
        if (isSelected) {
            contextSelected.get(name).setSelected(false);
            contextSelected.remove(name);
        } else {
            contextNonSelected.remove(name);
        }
        contextPanel.get(name).setVisible(false);
        settings.removeContext(name);
        checkActionPerformed(3);
        updateUI();
    }

    private JPanel getContextPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.add(new JLabel("Contexts:   "));

        panel.add(createButton(EIcon.ADD_ALL.getIcon(), "Add all context", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addAllContext();
            }
        }));

        panel.add(createButton(EIcon.REMOVE_ALL.getIcon(), "Remove all context", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeAllContext();
            }
        }));

        panel.add(createButton(EIcon.CONTEXT_ADD.getIcon(), "Add new context", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String context = JOptionPane.showInputDialog(null, "Enter one or more comma separated contexts: ", "Add New Context", JOptionPane.QUESTION_MESSAGE);
                if (context != null) {
                    parseContext(context);
                }
            }
        }));

        panel.setAlignmentX(LEFT_ALIGNMENT);

        return panel;
    }

    private static JButton createButton(ImageIcon icon, String tooltip, ActionListener listener) {
        JButton button = new JButton(icon);
        button.setPreferredSize(new Dimension(20, 20));
        button.setMaximumSize(new Dimension(20, 20));
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setToolTipText(tooltip);
        button.addActionListener(listener);
        return button;
    }

    private JCheckBox getAllCheckBoxesByIndex(int index) {
        return index == 0
                ? severityAllCheckBox
                : (index == 2
                        ? sourceAllCheckBox
                        : contextAllCheckBox);
    }

    private void allCheckBoxesActionPerformed(int index) {
        boolean isSelected = getAllCheckBoxesByIndex(index).isSelected();
        ContextSet cs = new ContextSet();
        ContextFilter contextFilter = new ContextFilter(cs, 5);
        if (index == 0) {
            for (Entry<String, JCheckBox> checkBox : severity.entrySet()) {
                checkBox.getValue().setSelected(isSelected);
            }
        } else if (index == 2) {
            for (Entry<String, JCheckBox> checkBox : source.entrySet()) {
                checkBox.getValue().setSelected(isSelected);
            }
        } else {
            if (isSelected) {
                for (Entry<String, JCheckBox> checkBox : contextNonSelected.entrySet()) {
                    checkBox.getValue().setSelected(isSelected);
                    contextSelected.put(checkBox.getKey(), checkBox.getValue());
                }
                contextNonSelected.clear();
            } else {
                for (Entry<String, JCheckBox> checkBox : contextSelected.entrySet()) {
                    checkBox.getValue().setSelected(isSelected);
                    contextNonSelected.put(checkBox.getKey(), checkBox.getValue());
                }
                contextSelected.clear();
            }
            cs = createAllContextSet();
            contextFilter = new ContextFilter(cs, 5);
            filters.set(5, contextFilter);
        }

        filters.set(index, RowFilter.regexFilter("", index));
        sorter.setRowFilter(RowFilter.andFilter(filters));
    }

    private void checkActionPerformed(int index) {
        List<RowFilter<Object, Object>> tmpFilters = new ArrayList<>();

        boolean hasNonSelected = false;
        if (index == 0) {
            settings.clearSeveritySelectedList();
            for (Entry<String, JCheckBox> checkBox : severity.entrySet()) {
                if (checkBox.getValue().isSelected()) {
                    settings.addSeveritySelectedItem(checkBox.getKey());
                    tmpFilters.add(RowFilter.regexFilter(ESeverity.getSeverity(checkBox.getKey()).getFullNameInUppercase(), index));
                } else {
                    hasNonSelected = true;
                }
            }
        } else if (index == 2) {
            settings.clearSourceSelectedList();
            for (Entry<String, JCheckBox> checkBox : source.entrySet()) {
                if (checkBox.getValue().isSelected()) {
                    settings.addSourceSelectedItem(checkBox.getKey());
                    tmpFilters.add(RowFilter.regexFilter(new StringBuilder("^").append(Pattern.quote(checkBox.getKey())).append("$").toString(), index));
                } else {
                    hasNonSelected = true;
                }
            }
        } else {
            settings.clearContextSelectedList();
            List<String> selected = new ArrayList<>();
            for (Entry<String, JCheckBox> checkBox : contextNonSelected.entrySet()) {
                if (checkBox.getValue().isSelected()) {
                    selected.add(checkBox.getKey());
                }
            }

            for (String name : selected) {
                contextSelected.put(name, contextNonSelected.get(name));
                contextNonSelected.remove(name);
            }
            List<String> nonSelected = new ArrayList<>();
            for (Entry<String, JCheckBox> checkBox : contextSelected.entrySet()) {
                if (checkBox.getValue().isSelected()) {
                    settings.addContextSelectedItem(checkBox.getKey());
                } else {
                    nonSelected.add(checkBox.getKey());
                }
            }

            for (String name : nonSelected) {
                contextNonSelected.put(name, contextSelected.get(name));
                contextSelected.remove(name);
            }
            ContextSet cs = createContextSet();
            ContextFilter contextFilter = new ContextFilter(cs, 5);
            filters.set(5, contextFilter);
            contextAllCheckBoxUpdate();
        }

        if (index != 3) {
            getAllCheckBoxesByIndex(index).setSelected(!hasNonSelected);
        }

        if (tmpFilters.isEmpty()) {
            tmpFilters.add(RowFilter.regexFilter("", index));
        }

        filters.set(index, RowFilter.orFilter(tmpFilters));
        sorter.setRowFilter(RowFilter.andFilter(filters));
    }

    private ContextSet createAllContextSet() {
        ContextSet cs = new ContextSet();
        for (int k = 0; k < arrayListName.size(); k++) {
            cs.add();
            cs.set(k, true);
        }
        return cs;
    }

    private ContextSet createContextSet() {
        ContextSet cs = new ContextSet();
        for (int k = 0; k < arrayListName.size(); k++) {
            cs.add();
        }
        for (Entry<String, JCheckBox> checkBox : contextSelected.entrySet()) {
            if (checkBox.getValue().isSelected()) {
                int indx = arrayListName.indexOf(checkBox.getKey());
                if (indx != -1) {
                    cs.set(indx, true);
                }
            }
        }
        return cs;
    }

    private int addContext(String ctx) { //1 exist, -1 not exist, 0 added
        if (arrayListName.contains(ctx)) {
            if (contextSelected.containsKey(ctx) || contextNonSelected.containsKey(ctx)) {
                return 1;
            } else {
                ContextPanel panel = contextPanel.get(ctx);
                panel.setVisible(true);
                contextNonSelected.put(ctx, panel.getCheckBox());
                settings.addNewContext(ctx);
                invalidate();
                revalidate();
                return 0;
            }
        } else {
            return -1;
        }
    }

    public void parseContext(String context) {
        if (!context.equals("[]") && context.startsWith("[") && context.endsWith("]")) {
            context = context.substring(1, context.length() - 1);
        }

        if (context.contains(",")) {
            int exist = 0;
            int added = 0;
            int notExist = 0;
            for (String cont : context.split(",")) {
                switch (addContext(cont)) {
                    case 1:
                        exist++;
                        break;
                    case -1:
                        notExist++;
                        break;
                    case 0:
                        added++;
                        break;
                }
            }
            JOptionPane.showMessageDialog(null, ((added != 0) ? "New contexts added: " + added + ". " : "")
                    + ((exist != 0) ? "Already exists contexts: " + exist + ". " : "")
                    + ((notExist != 0) ? "Contexts does not exist: " + notExist + ". " : ""));
        } else {
            switch (addContext(context)) {
                case 1:
                    JOptionPane.showMessageDialog(null, "This context already exists!");
                    break;
                case -1:
                    JOptionPane.showMessageDialog(null, "This context does not exist!");
                    break;
                default:
                    break;
            }
        }
    }
}
