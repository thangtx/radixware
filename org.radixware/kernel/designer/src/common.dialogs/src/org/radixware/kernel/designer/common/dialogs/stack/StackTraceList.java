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

package org.radixware.kernel.designer.common.dialogs.stack;

import java.awt.Color;
import java.awt.Component;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.swing.AbstractAction;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionListener;
import org.openide.util.lookup.Lookups;
import org.radixware.kernel.common.defs.*;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsUserReportClassDef;
import org.radixware.kernel.common.defs.ads.src.IJavaSource;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport.UsagePurpose;
import org.radixware.kernel.common.defs.ads.src.SrcPositionLocator;
import org.radixware.kernel.common.defs.ads.xml.IXmlDefinition;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.exceptions.NoConstItemWithSuchValueError;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.resources.RadixWareIcons;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinition;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinitionCfg;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.designer.common.dialogs.utils.ModalDisplayer;
import org.radixware.kernel.designer.common.general.editors.OpenInfo;


public class StackTraceList extends JPanel {

    private static class StackTreeModelItem {

        boolean isStackString = false;
        int lineNumber = -1;
        String text;
        List<SrcPositionLocator.SrcLocation> locations = new LinkedList<>();
        ERuntimeEnvironmentType environment;
        List<Definition> defs = new LinkedList<>();

        @Override
        public String toString() {
            if (!locations.isEmpty()) {
                String name = "";
                for (SrcPositionLocator.SrcLocation location : locations){
                    name += location.getRadixObject().getQualifiedName() + "; ";
                }
                return name.substring(0, name.length() - 2);
            } else {
                return text;
            }
        }
    }

    private static class StackTreeModel implements ListModel {

        private static class ItemInfo {

            final ERuntimeEnvironmentType environment;
            final int line;
            final Set<Definition> defs;

            public ItemInfo(ERuntimeEnvironmentType environment, int line, Set<Definition> defs) {
                this.environment = environment;
                this.line = line;
                if (defs == null){
                    this.defs = new HashSet<>();
                } else {
                    this.defs = new HashSet<>(defs);
                }
            }
        }

        private static ERuntimeEnvironmentType getEnvironment(String name) {
            if ("common_client".equals(name)) {
                return ERuntimeEnvironmentType.COMMON_CLIENT;
            }

            return ERuntimeEnvironmentType.getForValue(name);
        }
        private static final ItemInfo EMPTY_INFO = new ItemInfo(null, -1, null);
        private static final Pattern RADIX_SOURCE_LINE = Pattern.compile(".+\\.ads\\.mdl[A-Z0-9_]{26}\\..+\\(.+\\.java\\:[0-9]+\\)");
        private static final Pattern RADIX_ID = Pattern.compile("^[a-z]{3,6}[A-Z0-9_]{26}$");

        private static ItemInfo getLineNumber(String string, Branch branch, final Map<RadixObject, SrcPositionLocator.SrcLocation> defs) {

            if (!RADIX_SOURCE_LINE.matcher(string).matches()) {
                return EMPTY_INFO;
            }

            final int lparen = string.indexOf("(");
            final int javaMarker = string.indexOf(".java", lparen + 1);
            final int colon = string.indexOf(":", javaMarker + 1);
            final int rparen = string.indexOf(")", colon + 1);
            final int moduleIdIndex = string.indexOf(".mdl");

            final String lineNumberStr = string.substring(colon + 1, rparen);
            final int lineNumber = Integer.decode(lineNumberStr);

            try {
                ERuntimeEnvironmentType environment = null;
                Definition targetDef = null;
                Set<Definition> targetDefs = new HashSet<>();
                if (branch == null) {
                    return EMPTY_INFO;
                }
                if (moduleIdIndex < 0 || lparen < 0 || moduleIdIndex > lparen) {
                    return EMPTY_INFO;
                }

                final String invokedMethodStr = string.substring(moduleIdIndex + 1, lparen);
                final String[] ids = invokedMethodStr.split("\\.");
                if (ids.length > 1) {
                    final Id moduleId = Id.Factory.loadFrom(ids[0]);
                    final String envSelector = ids[1];
                    final String asStr = ids[ids.length - 1];
                    final Id methodId = Id.Factory.loadFrom(asStr);

                    final boolean isId = RADIX_ID.matcher(asStr).matches();

                    environment = getEnvironment(envSelector);
                    if (ids.length > 2) {
                        String definitionIdCandidate = ids[2];
                        final int dollarIdx = definitionIdCandidate.indexOf("$");
                        if (dollarIdx >= 0) {
                            definitionIdCandidate = definitionIdCandidate.substring(0, dollarIdx);
                        }
                        if (definitionIdCandidate.length() > 3) {
                            final Id definitionId = Id.Factory.loadFrom(definitionIdCandidate);
                            final List<Module> modules = new LinkedList<>();
                            branch.visit(new IVisitor() {
                                @Override
                                public void accept(RadixObject radixObject) {
                                    modules.add((Module) radixObject);
                                }
                            }, new VisitorProvider() {
                                @Override
                                public boolean isTarget(RadixObject radixObject) {
                                    if (radixObject instanceof Module) {
                                        String finalId = String.valueOf(JavaSourceSupport.getModulePackageName((Module) radixObject));
                                        Id id = Id.Factory.loadFrom(finalId);
                                        if (id == moduleId) {
                                            return true;
                                        }
                                    }
                                    return false;
                                }
                            });

                            for (final Module module : modules) {

                                Definition def = null;
                                Module m = module;
                                while (m != null) {
                                    def = (AdsDefinition) m.find(new VisitorProvider() {
                                        @Override
                                        public boolean isTarget(RadixObject radixObject) {
                                            if (radixObject instanceof AdsUserReportClassDef) {
                                                AdsUserReportClassDef ur = (AdsUserReportClassDef) radixObject;
                                                if (ur.getRuntimeId() == definitionId) {
                                                    return true;
                                                }
                                            }
                                            return radixObject instanceof Definition && ((Definition) radixObject).getId() == definitionId;
                                        }
                                    });
                                    if (def != null) {
                                        if (isId) {
                                            targetDef = (Definition) def.find(new VisitorProvider() {
                                                @Override
                                                public boolean isTarget(RadixObject radixObject) {
                                                    return radixObject instanceof Definition && ((Definition) radixObject).getId() == methodId;
                                                }
                                            });
                                        }
                                        break;
                                    }
                                    m = m.findOverwritten();
                                }

                                if (def instanceof IJavaSource && !(def instanceof IXmlDefinition)) {
                                    JavaSourceSupport.CodeWriter writer = ((IJavaSource) def).getJavaSourceSupport().getCodeWriter(JavaSourceSupport.UsagePurpose.getPurpose(environment, JavaSourceSupport.CodeType.EXCUTABLE));
                                    final char[] src;
                                    if (writer != null) {
                                        CodePrinter printer = CodePrinter.Factory.newJavaPrinter();
                                        if (writer.writeCode(printer)) {
                                            src = printer.getContents();
                                        } else {
                                            continue;
                                        }
                                    } else {
                                        continue;
                                    }
                                    final SrcPositionLocator locator = SrcPositionLocator.Factory.newInstance((IJavaSource) def, src);
                                    final int position = JavaSourceSupport.lineNumber2Position(src, lineNumber);
                                    final SrcPositionLocator.SrcLocation loc = locator.calc(position, position);

                                    if (targetDef != null) {
                                        if (loc.getRadixObject() == null || loc.getRadixObject().getOwnerDefinition() == null) {
                                            continue;
                                        }

                                        final Definition locDef = loc.getRadixObject().getOwnerDefinition();
                                        if (locDef.getId() != methodId) {
                                            continue;
                                        }
                                        
                                        targetDefs.add(targetDef);
                                    }
                                    if (isId && loc.getRadixObject() != null && loc.getRadixObject().getOwnerDefinition() != null && loc.getRadixObject().getOwnerDefinition().getId() != methodId && asStr.startsWith(loc.getRadixObject().getOwnerDefinition().getId().getPrefix().getValue())) {
                                        continue;
                                    }
                                    
                                    if (!defs.containsKey(def)){
                                        defs.put(loc.getRadixObject(), loc);
                                    }
                                    
                                    if (!isId || targetDefs.isEmpty()) break;
                                }
                            }
                        }
                    }
                }
                return new ItemInfo(environment, lineNumber, targetDefs);
            } catch (NumberFormatException | NoConstItemWithSuchValueError e) {
                final String msg = String.format(e.getMessage() + " => %s", string);
                Logger.getLogger(StackTraceList.class.getName()).log(Level.INFO, msg);
                return EMPTY_INFO;
            }
        }
        private final LinkedList<StackTreeModelItem> items = new LinkedList<>();
        private LinkedList<ListDataListener> listeners = null;

        StackTreeModel() {
        }

        private void setStackStrings(Branch branch, List<String> stack) {
            items.clear();
            final SrcPositionLocator.SrcLocation[] def = new SrcPositionLocator.SrcLocation[1];

            for (final String line : stack) {
                if (line.contains("access$")) {
                    continue;
                }
                final StackTreeModelItem item = new StackTreeModelItem();
                item.text = line;
                item.isStackString = true;
                def[0] = null;
                Map<RadixObject, SrcPositionLocator.SrcLocation> locations = new HashMap<>();
                final ItemInfo info = getLineNumber(line, branch, locations);
                if (info != null) {
                    item.lineNumber = info.line;
                    item.environment = info.environment;
                    item.defs.addAll(info.defs);
                }
                item.locations.addAll(locations.values());
                items.add(item);
            }
            fireChange();
        }

        @Override
        public int getSize() {
            return items.size();
        }

        @Override
        public Object getElementAt(int index) {
            return items.get(index);
        }

        @Override
        public void addListDataListener(ListDataListener l) {
            synchronized (this) {
                if (listeners == null) {
                    listeners = new LinkedList<>();
                }
                listeners.add(l);
            }
        }

        @Override
        public void removeListDataListener(ListDataListener l) {
            synchronized (this) {
                if (listeners != null) {
                    listeners.remove(l);
                }
            }
        }

        private void fireChange() {
            ListDataEvent e = new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, 0, getSize());
            if (listeners != null) {
                for (ListDataListener l : listeners) {
                    l.contentsChanged(e);
                }
            }
        }
    }

    private final class Renderer extends DefaultListCellRenderer {

        @Override
        public Component getListCellRendererComponent(JList tree, Object value, int index, boolean isSelected, boolean hasFocus) {
            Component c = super.getListCellRendererComponent(tree, value, index, isSelected, hasFocus);
            StackTreeModelItem item = (StackTreeModelItem) value;
            JLabel label = new JLabel(item.text);
            label.setOpaque(true);

            RadixObject object = null;
            String name = "";
            if (!item.locations.isEmpty()){
                object = item.locations.get(0).getRadixObject().getOwnerDefinition();
                if (item.locations.size() > 1){
                    name = object.getName() + "...";
                } else {
                    name = object.getQualifiedName();
                }
            } else if (!item.defs.isEmpty()) {
                object = item.defs.get(0);
                if (item.defs.size() > 1){
                    name = object.getName() + "(mispositioning)"+ "...";
                } else {
                    name = object.getQualifiedName() + "(mispositioning)";
                }
            }
            if (object != null) {
                label.setForeground(c.getForeground());
                label.setText(name);
                label.setIcon(object.getIcon().getIcon());
            } else {
                label.setForeground(isSelected ? c.getForeground() : Color.GRAY);
                label.setIcon(RadixWareIcons.JAVA.JAVA.getIcon());
            }
            label.setBackground(c.getBackground());
            return label;
        }
    }
    /**
     * Creates new form StackTraceList
     */
    private Branch branch = null;
    private String traceText = "";
    private final StackTreeModel model;

    public StackTraceList() {
        initComponents();

        model = new StackTreeModel();
        stackTree.setModel(model);

        final JPopupMenu menu = new JPopupMenu();
        final JMenuItem copyToClipboard = menu.add(new AbstractAction("Copy to clipboard") {
            @Override
            public void actionPerformed(ActionEvent e) {
                final Object selectedValue = stackTree.getSelectedValue();

                if (selectedValue != null) {
                    final Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

                    final StringSelection stringSelection = new StringSelection(selectedValue.toString());
                    clipboard.setContents(stringSelection, stringSelection);
                }

            }
        });
        final JMenuItem goToSource = menu.add(new AbstractAction("Go to source") {
            @Override
            public void actionPerformed(ActionEvent e) {
                goToSource();
            }
        });

        stackTree.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    final int count = e.getClickCount();
                    if (count == 2) {
                        gotToSelectedItem();
                    }
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                check(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                check(e);
            }

            private void check(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    final int size = stackTree.getModel().getSize();
                    copyToClipboard.setEnabled(size > 0);
                    goToSource.setEnabled(size > 0);

                    stackTree.setSelectedIndex(stackTree.locationToIndex(e.getPoint()));
                    menu.show(stackTree, e.getX(), e.getY());
                }
            }
        });
        stackTree.setCellRenderer(new Renderer());
    }

    void gotToSelectedItem() {
        if (stackTree.getSelectedIndex() >= 0 && stackTree.getSelectedIndex() < model.getSize()) {
            StackTreeModelItem item = (StackTreeModelItem) model.getElementAt(stackTree.getSelectedIndex());
            final ChooseStackTraceItemPanel panel = new ChooseStackTraceItemPanel();
            ModalDisplayer displayer = new ModalDisplayer(panel, "Choose definition");
            
            if (!item.locations.isEmpty()) {
                SrcPositionLocator.SrcLocation location = null;
                if (item.locations.size() == 1){
                    location = item.locations.get(0);
                } else {
                    panel.open(item.locations);
                    if (displayer.showModal()){
                        location = panel.getSelectedLocation();
                    }
                }
                if (location != null){
                    DialogUtils.goToObject(location.getRadixObject(), new OpenInfo(location.getScml(), Lookups.fixed(location)));
                }
            } else if (!item.defs.isEmpty()) {
                Definition definition = null;
                if (item.locations.size() == 1){
                    definition = item.defs.get(0);
                } else {
                    panel.open(item.defs);
                    if (displayer.showModal()){
                        definition = panel.getSelectedDefinition();
                    }
                }
                
                if (definition != null){
                    DialogUtils.goToObject(definition);
                }
            }
        }
    }

    boolean isExposableSelection() {
        if (stackTree.getSelectedIndex() >= 0 && stackTree.getSelectedIndex() < model.getSize()) {
            StackTreeModelItem item = (StackTreeModelItem) model.getElementAt(stackTree.getSelectedIndex());
            return !item.locations.isEmpty() || !item.defs.isEmpty();
        } else {
            return false;
        }
    }

    void addSelectionListener(ListSelectionListener listener) {
        stackTree.getSelectionModel().addListSelectionListener(listener);
    }

    public void goToSource() {

        if (stackTree.getSelectedIndex() >= 0 && stackTree.getSelectedIndex() < model.getSize()) {
            final StackTreeModelItem item = (StackTreeModelItem) model.getElementAt(stackTree.getSelectedIndex());
            final ChooseStackTraceItemPanel panel = new ChooseStackTraceItemPanel();
            ModalDisplayer displayer = new ModalDisplayer(panel, "Choose definition");
            
            if (item != null && !item.locations.isEmpty()) {
                SrcPositionLocator.SrcLocation location = null;
                if (item.locations.size() == 1){
                    location = item.locations.get(0);
                } else {
                    panel.open(item.locations);
                    if (displayer.showModal()){
                        location = panel.getSelectedLocation();
                    }
                }
                if (location != null){
                    DialogUtils.viewSource(location.getRadixObject(), item.environment, JavaSourceSupport.CodeType.EXCUTABLE, item.lineNumber);
                }
                
            } else if (item != null && !item.defs.isEmpty()) {
                Definition definition = null;
                if (item.locations.size() == 1){
                    definition = item.defs.get(0);
                } else {
                    panel.open(item.defs);
                    if (displayer.showModal()){
                        definition = panel.getSelectedDefinition();
                    }
                }
                
                if (definition != null){
                    DialogUtils.viewSource(definition, item.environment, JavaSourceSupport.CodeType.EXCUTABLE, item.lineNumber);
                }
            }
        }
    }

    Branch getBranch() {
        return branch;
    }

    void setBranch(final Branch branch) {
        this.branch = branch;
        parseStackTrace(traceText);
    }

    void parseStackTrace(String traceText) {
        synchronized (this) {
            this.traceText = traceText;

            if (traceText == null || traceText.isEmpty()) {
                model.setStackStrings(branch, Collections.EMPTY_LIST);
                return;
            }

            final String[] split = traceText.split("(\\n|\\s)at\\s|\\n");
            final List<String> lines = new ArrayList<>();

            for (final String str : split) {
                if (str != null && !str.trim().isEmpty()) {
                    lines.add(str.trim());
                }
            }
            model.setStackStrings(branch, lines);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        stackTree = new javax.swing.JList();

        stackTree.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(stackTree);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JList stackTree;
    // End of variables declaration//GEN-END:variables
}
