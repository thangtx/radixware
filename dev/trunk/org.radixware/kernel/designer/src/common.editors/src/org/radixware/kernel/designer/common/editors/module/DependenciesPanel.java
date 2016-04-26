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

package org.radixware.kernel.designer.common.editors.module;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import org.openide.DialogDescriptor;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.view.OutlineView;
import org.openide.explorer.view.Visualizer;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.nodes.Sheet;
import org.openide.util.RequestProcessor;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.Dependences;
import org.radixware.kernel.common.defs.Dependences.Dependence;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObjectIcon;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.VisitorProviderFactory;
import org.radixware.kernel.common.exceptions.RadixObjectError;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.resources.RadixWareIcons;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.types.Arr;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.designer.common.dialogs.actions.AbstractRadixAction;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinition;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinitionCfg;
import org.radixware.kernel.designer.common.dialogs.usages.FindUsages;
import org.radixware.kernel.designer.common.dialogs.usages.FindUsagesCfg;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.designer.common.dialogs.utils.ModalDisplayer;


final class DependenciesPanel extends JPanel implements ExplorerManager.Provider {

    private static class NotNullDefList extends ArrayList<Definition> {

        private final RadixObject source;

        public NotNullDefList(RadixObject source) {
            this.source = source;
        }

        @Override
        public boolean add(Definition e) {
            if (e == null) {
                throw new RadixObjectError("Attempt to add in collect dependencies", source);
            }
            return super.add(e);
        }
    }

    private static class DependenceNode extends AbstractNode implements Comparable<DependenceNode> {

        private final Object object;

        public DependenceNode(Children children, Object object) {
            super(children);
            this.object = object;
        }

        @Override
        public Image getOpenedIcon(int type) {
            return getIcon(type);
        }

        @Override
        public int compareTo(DependenceNode o) {
            return getDisplayName().compareTo(o.getDisplayName());
        }

        public Object getObject() {
            return object;
        }
    }

    private static final class ModuleNode extends DependenceNode {

        public ModuleNode(Dependences.Dependence dependence) {
            super(new Children.SortedArray(), dependence);

            final Sheet.Set set = new Sheet.Set();
            set.put(new ForcedProperty(dependence));
            getSheet().put(set);

            update();
        }

        public Dependences.Dependence getDependence() {
            return (Dependences.Dependence) getObject();
        }

        public void update() {
            final List<Module> modules = getDependence().findDependenceModule(null);
            final String displayName;
            final String toolTip;
            final Color color;
            if (modules.isEmpty()) {
                toolTip = "";
                displayName = String.valueOf(getDependence().getDependenceModuleId());
//                    color = Color.RED;
            } else {
                StringBuilder sb = new StringBuilder();
                boolean first = true;
                for (Module module : modules) {
                    if (first) {
                        first = false;
                    } else {
                        sb.append(", ");
                    }
                    sb.append(module.getQualifiedName());
                }
                displayName = sb.toString();
                toolTip = modules.get(0).getToolTip();
//                    color = Color.BLACK;
            }
            setDisplayName(displayName);
            setShortDescription(toolTip);
//                setColor(color);
        }

        @Override
        public Image getIcon(int type) {
            final List<Module> modules = getDependence().findDependenceModule(null);
            final RadixIcon icon = modules.isEmpty() ? RadixObjectIcon.UNKNOWN : modules.get(0).getIcon();
            return icon.getImage();
        }

        public void add(DefinitionNode defNode) {
            getChildren().add(new Node[]{defNode});
        }

        public void remove(DefinitionNode defNode) {
            getChildren().remove(new Node[]{defNode});
        }

        @Override
        public Action getPreferredAction() {
            List<Module> findDependenceModule = getDependence().findDependenceModule(null);
            if (!findDependenceModule.isEmpty()){
                return new GoToObjectAction(findDependenceModule.get(0));
            } else {
                return null;
            }
        }
    }

    private static final class DefinitionNode extends DependenceNode {

        public DefinitionNode(Definition definition) {
            super(Children.LEAF, definition);
            update();
        }

        public Definition getDefinition() {
            return (Definition) getObject();
        }

        public void update() {
            final String displayName = getDefinition().getQualifiedName(getDefinition().getModule());
            setDisplayName(displayName);
            setShortDescription(getDefinition().getToolTip());
        }

        @Override
        public Image getIcon(int type) {
            final RadixIcon icon = getDefinition().getIcon();
            return icon.getImage();
        }

        @Override
        public Action getPreferredAction() {
            return new GoToObjectAction(getDefinition());
        }
    }

    private static class GoToObjectAction extends AbstractRadixAction {

        private final RadixObject radixObject;

        public GoToObjectAction(RadixObject radixObject) {
            super("go-to-object");
            this.radixObject = radixObject;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (radixObject != null) {
                DialogUtils.goToObject(radixObject);
            }
        }
    }

    private static final class ForcedProperty extends Node.Property<Boolean> {

        private final Dependences.Dependence dependence;
        public ForcedProperty(Dependences.Dependence dependence) {
            super(Boolean.class);
            setName(FORCED_PROPERTY);
            this.dependence = dependence;
        }

        public Dependences.Dependence getObject() {
            return dependence;
        }

        @Override
        public Boolean getValue() throws IllegalAccessException, InvocationTargetException {
            return getObject().isForced();
        }

        @Override
        public boolean canRead() {
            return true;
        }

        @Override
        public boolean canWrite() {
            return !dependence.isReadOnly();
        }

        @Override
        public void setValue(Boolean value) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
            getObject().setForced(value);
        }
    }

    private static Set<Definition> groupDefinitionsByParent(Set<Definition> set) {
        final Set<Definition> result = new HashSet<>();
        for (Definition def1 : set) {
            boolean add = true;
            for (Definition def2 : set) {
                if (def2.isParentOf(def1)) {
                    add = false;
                    break;
                }
            }
            if (add) {
                result.add(def1);
            }
        }
        return result;
    }

    private static boolean isOnlyDependencies(Object[] selected) {
        for (Object obj : selected) {
            if (!(obj instanceof Dependences.Dependence)) {
                return false;
            }
        }
        return true;
    }

    private static boolean isOnlyValidObjects(Object[] selected) {
        for (Object obj : selected) {
            if (obj instanceof Dependences.Dependence) {
                final Dependences.Dependence dependence = (Dependences.Dependence) obj;
                if (dependence.findDependenceModule(null) == null) {
                    return false;
                }
            }
        }
        return true;
    }
    private static final String INSERT = "insert";
    private static final String DELETE = "delete";
    public static final String FORCED_PROPERTY = "Forced";
    private final OutlineView view;
    private final ExplorerManager manager = new ExplorerManager();
    private final Module module;
    private ListSelectionListener selectionListener = new ListSelectionListener() {
        @Override
        public void valueChanged(ListSelectionEvent e) {
            updateState();
        }
    };
    //
    private final AbstractAction addDependenciesAction = new AbstractAction("Add", RadixWareIcons.CREATE.ADD.getIcon()) {
        @Override
        public void actionPerformed(ActionEvent e) {
            addDependencies();
        }
    };
    private final AbstractAction removeSelectedDependenciesAction = new AbstractAction("Remove", RadixWareIcons.DELETE.DELETE.getIcon()) {
        @Override
        public void actionPerformed(ActionEvent e) {
            removeSelectedDependencies();
        }
    };
    private final AbstractAction gotoSelectedObjectAction = new AbstractAction("Go To Object", RadixWareIcons.ARROW.GO_TO_OBJECT.getIcon()) {
        @Override
        public void actionPerformed(ActionEvent e) {
            gotoSelectedObject();
        }
    };
    private final AbstractAction findUsagesAction = new AbstractAction("Find Usages", RadixWareIcons.TREE.DEPENDENCIES.getIcon()) {
        @Override
        public void actionPerformed(ActionEvent e) {
            findUsages();
        }
    };
    private final AbstractAction actualizeDependenciesAction = new AbstractAction("Actualize", RadixWareIcons.EDIT.FIX.getIcon()) {
        @Override
        public void actionPerformed(ActionEvent e) {
            actualizeDependencies();
        }
    };
    private final Action[] actions = new Action[]{addDependenciesAction, removeSelectedDependenciesAction, gotoSelectedObjectAction, findUsagesAction, null, actualizeDependenciesAction};
    //

    public DependenciesPanel(Module module) {
        this.module = module;

        addDependenciesAction.putValue(Action.SHORT_DESCRIPTION, "Add new dependencies (Ins)");
        removeSelectedDependenciesAction.putValue(Action.SHORT_DESCRIPTION, "Remove selected dependencies (Del)");
        gotoSelectedObjectAction.putValue(Action.SHORT_DESCRIPTION, "Go to selected object (Dbl Click)");
        actualizeDependenciesAction.putValue(Action.SHORT_DESCRIPTION, "Remove unused and add required dependencies automatically");
        findUsagesAction.putValue(Action.SHORT_DESCRIPTION, "Find what objects required selected objects");

        setLayout(new BorderLayout());
        view = new OutlineView();
        view.getOutline().setRootVisible(false);
        view.getOutline().setShowGrid(false);
        view.setBorder(BorderFactory.createEtchedBorder(1));
        view.addPropertyColumn(FORCED_PROPERTY, FORCED_PROPERTY);
        view.getOutline().getColumnModel().getColumn(0).setHeaderValue("Dependence");
        view.getOutline().setDragEnabled(false);
        view.setDragSource(false);
        
        view.setPopupAllowed(false);

        add(new JLabel("Dependencies and used definitions:"), BorderLayout.PAGE_START);
        add(view, BorderLayout.CENTER);

        final Box buttonsBox = DialogUtils.createVerticalBox(actions);
        buttonsBox.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 0));
        add(buttonsBox, BorderLayout.LINE_END);

        view.getOutline().getSelectionModel().addListSelectionListener(selectionListener);
        // hot keys
        final InputMap inputMap = getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_INSERT, 0), INSERT);
        getActionMap().put(INSERT, addDependenciesAction);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), DELETE);
        getActionMap().put(DELETE, removeSelectedDependenciesAction);

        int width = view.getFontMetrics(view.getFont()).stringWidth(FORCED_PROPERTY) * 2;
        view.getOutline().getColumn(FORCED_PROPERTY).setMaxWidth(width);


        getExplorerManager().setRootContext(new AbstractNode(new Children.SortedArray()));
    }

    @Override
    public ExplorerManager getExplorerManager() {
        return manager;
    }

    public void collapseNode(Node n) {
        view.collapseNode(n);
    }

    public void expandNode(Node n) {
        view.expandNode(n);
    }

    public OutlineView GetOutlineView() {
        return view;
    }
    public boolean showConfirmDialog(List<Dependences.Dependence> actualizeList){
        JPanel panelInfo = new JPanel();
        panelInfo.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
        DefaultTableModel model = new DefaultTableModel(){
                @Override
                public boolean isCellEditable(int i, int i1) {
                    return false;
                };
        };
        model.addColumn("");
        model.addColumn("");
        panelInfo.setLayout(new BoxLayout(panelInfo, BoxLayout.PAGE_AXIS));
        ArrayList<DependenciesPanel.ModuleNode> dependenceList = new ArrayList<>();

        final Node rootNode = getExplorerManager().getRootContext();
        final Map<Dependences.Dependence, DependenciesPanel.ModuleNode> dep2Item = new HashMap<>();
        for (int i = 0; i < rootNode.getChildren().getNodesCount(); i++) {
            final DependenciesPanel.ModuleNode item = (DependenciesPanel.ModuleNode) rootNode.getChildren().getNodeAt(i);
            dep2Item.put(item.getDependence(), item);
        }
        
        for (Dependences.Dependence dependence : actualizeList) {
            DependenciesPanel.ModuleNode item = dep2Item.get(dependence);
            if (item == null) {
               dependenceList.add(new DependenciesPanel.ModuleNode(dependence));
            }
            dep2Item.remove(dependence);
        }

        int count = dependenceList.size();
        if (!dependenceList.isEmpty()){
            Collections.sort(dependenceList);
            for (DependenciesPanel.ModuleNode dependence : dependenceList){
                model.addRow(new Object[]{dependence.getIcon(0),dependence.getDisplayName()});
            }
        }
   
        if (!dep2Item.isEmpty()){

            dependenceList = new ArrayList<> (dep2Item.values());
            Collections.sort(dependenceList);
            for (DependenciesPanel.ModuleNode item : dependenceList) {
                model.addRow(new Object[]{item.getIcon(0),item.getDisplayName()});
            }
            
        }
        JLabel label = new JLabel("In the list of dependencies will be made the following changes:");
        label.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        panelInfo.add(label);
        initTable(panelInfo, model,count);
        label.setAlignmentX(CENTER_ALIGNMENT);
        
        final ModalDisplayer dg = new ModalDisplayer(panelInfo,"Update of the dependencies list");
        dg.getDialog().setMaximumSize(panelInfo.getMaximumSize());
        return dg.showModal();

    }
    
    private void initTable(JPanel container,DefaultTableModel model,final int newDependanceCount){
        JTable table = new JTable(model);
        DefaultTableCellRenderer imageRenderer = new DefaultTableCellRenderer() {
            JLabel label;

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                label = new JLabel();
                if (value instanceof Image) {
                    label.setIcon(new ImageIcon((Image) value));
                    return label;
                }
                return label;
            }

        };
        DefaultTableCellRenderer modulesRenderer = new DefaultTableCellRenderer() {

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component  component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column); 
                if (row < newDependanceCount){
                    component.setForeground(Color.GREEN.darker().darker());
                } else {
                    component.setForeground(Color.RED.darker());
                }
                return component;
            }

        };
        table.getTableHeader().setReorderingAllowed(false);
        TableColumn imageColumn = table.getColumnModel().getColumn(0); 
        imageColumn.setCellRenderer(imageRenderer);
        imageColumn.setMaxWidth(20);
        table.getColumnModel().getColumn(1).setCellRenderer(modulesRenderer);
        table.setTableHeader(null);
        JScrollPane scrollPane = new JScrollPane(table);
        container.add(scrollPane);
    }
    
    public void update() {
        final Map<Dependences.Dependence, DependenciesPanel.ModuleNode> dep2Item = new HashMap<>();
        final Node rootNode = getExplorerManager().getRootContext();
        for (int i = 0; i < rootNode.getChildren().getNodesCount(); i++) {
            final DependenciesPanel.ModuleNode item = (DependenciesPanel.ModuleNode) rootNode.getChildren().getNodeAt(i);
            dep2Item.put(item.getDependence(), item);
        }

        // add new, update existed
        for (Dependences.Dependence dependence : getModule().getDependences()) {
            DependenciesPanel.ModuleNode item = dep2Item.get(dependence);
            if (item == null) {
                item = new DependenciesPanel.ModuleNode(dependence);
                rootNode.getChildren().add(new Node[]{item});
            } else {
                item.update();
            }
            dep2Item.remove(dependence);
        }

        // remove old
        for (DependenciesPanel.ModuleNode item : dep2Item.values()) {
            rootNode.getChildren().remove(new Node[]{item});
        }

        updateUsages();
    }

    private void updateUsages() {
        final Set<Definition> allUsedDefinitions = collectUsedDefs();

        final Node rootNode = getExplorerManager().getRootContext();
        for (int i = 0; i < rootNode.getChildren().getNodesCount(); i++) {
            final DependenciesPanel.ModuleNode dependenceNode = (DependenciesPanel.ModuleNode) rootNode.getChildren().getNodeAt(i);
            final Dependences.Dependence dependence = dependenceNode.getDependence();
            final Id moduleId = dependence.getDependenceModuleId();

            final Map<Definition, DependenciesPanel.DefinitionNode> definition2Item = new HashMap<>();
            for (int j = 0; j < dependenceNode.getChildren().getNodesCount(); j++) {
                final DependenciesPanel.DefinitionNode definitionItem = (DependenciesPanel.DefinitionNode) dependenceNode.getChildren().getNodeAt(j);
                definition2Item.put(definitionItem.getDefinition(), definitionItem);
            }

            // add new, update existed
            for (Definition definition : allUsedDefinitions) {
                if (definition != null && definition.getModule() != null && Utils.equals(definition.getModule().getId(), moduleId)) {
                    DependenciesPanel.DefinitionNode defItem = definition2Item.get(definition);
                    if (defItem == null) {
                        defItem = new DependenciesPanel.DefinitionNode(definition);
                        dependenceNode.add(defItem);
                    } else {
                        defItem.update();
                    }
                    definition2Item.remove(definition);
                } 
            }

            // remove old
            for (DependenciesPanel.DefinitionNode definitionItem : definition2Item.values()) {
                dependenceNode.remove(definitionItem);
            }

            if (dependenceNode.getChildren().getNodesCount() == 0) {
                expandNode(dependenceNode);
            }
        }
    }

    private Set<Definition> collectUsedDefs() {
        final Set<Definition> allUsedDefinitions = new HashSet<>();
        getModule().visitChildren(new IVisitor() {
            @Override
            public void accept(RadixObject radixObject) {
                List<Definition> usedDefinitions = new NotNullDefList(radixObject);
                radixObject.collectDirectDependences(usedDefinitions);
                allUsedDefinitions.addAll(usedDefinitions);
            }
        }, VisitorProviderFactory.createDefaultVisitorProvider());
        final Set<Definition> result = groupDefinitionsByParent(allUsedDefinitions);
        return result;
    }

    private void findUsages() {
        final Set<RadixObject> roots = new HashSet<>();
        for (Object userObject : getSelectedUserObjects()) {
            if (userObject instanceof Dependence) {
                final Dependence dependence = (Dependence) userObject;
                final List<Module> m = dependence.findDependenceModule(null);
                if (m != null) {
                    roots.addAll(m);
                }
            } else if (userObject instanceof RadixObject) {
                roots.add((RadixObject) userObject);
            }
        }
        RequestProcessor.getDefault().post(new Runnable() {
            @Override
            public void run() {
                final FindUsagesCfg cfg = new FindUsagesCfg(module);
                cfg.setSearchType(FindUsagesCfg.ESearchType.FIND_USED);
                cfg.setRoots(roots);
                FindUsages.search(cfg);
            }
        });
    }

    private void actualizeDependencies() {
        final List<Dependences.Dependence> snapshot = createSnapshot();
        final Module mdl = getModule();
        List<Dependences.Dependence> actualizeList = new ArrayList<>();
        if (mdl.getDependences().actualize(true)) {
            boolean result = showConfirmDialog(mdl.getDependences().list());
            mdl.getDependences().confirmationOfActualization(result);
            if (result){
                update();
                selectNew(snapshot);
            }
        } else {
            DialogUtils.messageInformation("Dependencies are already actual.");
        }
    }

    private Module getModule() {
        return module;
    }

    private void selectNew(List<Dependences.Dependence> snapshot) {
        final List<Dependences.Dependence> newDependencies = getModule().getDependences().list();
        newDependencies.removeAll(snapshot);
        final Set<Dependences.Dependence> selection = new HashSet<>(newDependencies);
        setSelection(selection);
    }

    private void gotoSelectedObject() {
        final Object object = getSelectedUserObject();
        if (object instanceof Dependences.Dependence) {
            final Dependences.Dependence dependence = (Dependences.Dependence) object;
            final List<Module> m = dependence.findDependenceModule(null);
            if (!m.isEmpty()) {
                DialogUtils.goToObject(m.get(0));
            }
        } else if (object instanceof RadixObject) {
            final RadixObject radixObject = (RadixObject) object;
            DialogUtils.goToObject(radixObject);
        }
    }

    private List<Dependences.Dependence> createSnapshot() {
        return getModule().getDependences().list();
    }

    private void addDependencies() {
        final Layer layer = getModule().getSegment().getLayer();
        final ChooseDefinitionCfg cfg = ChooseDefinitionCfg.Factory.newInstance(layer, new VisitorProvider() {
            @Override
            public boolean isTarget(RadixObject radixObject) {
                if (radixObject instanceof Module) {
                    final Module m = (Module) radixObject;
                    if (Utils.equals(module.getId(), m.getId())) {
                        return false;
                    }
                    if (module.getDependences().contains(m)) {
                        return false;
                    }
                    return true;
                } else {
                    return false;
                }
            }
        });
        cfg.setTypeTitle(Module.MODULE_TYPE_TITLE);
        cfg.setTypesTitle(Module.MODULES_TYPES_TITLE);
        final Collection<? extends Definition> chosen = ChooseDefinition.chooseDefinitions(cfg);
        if (chosen != null) {
            final List<Dependences.Dependence> snapshot = createSnapshot();
            for (Definition def : chosen) {
                final Module m = (Module) def;
                getModule().getDependences().add(m);
            }
            update();
            selectNew(snapshot);
        }
    }

    private void removeSelectedDependencies() {
        for (final Object userObject : getSelectedUserObjects()) {
            if (userObject instanceof Dependences.Dependence) {
                final Dependences.Dependence dependence = (Dependences.Dependence) userObject;
                getModule().getDependences().remove(dependence.getDependenceModuleId());
            }
        }
        update();
    }

    public void updateState() {
        boolean enabled = !getModule().isReadOnly();
        addDependenciesAction.setEnabled(enabled);
        actualizeDependenciesAction.setEnabled(enabled);
        // FIXME
        final Object[] selected = getSelectedUserObjects();
        removeSelectedDependenciesAction.setEnabled(enabled && selected != null && selected.length > 0 && isOnlyDependencies(selected));
        gotoSelectedObjectAction.setEnabled(selected != null && selected.length == 1 && isOnlyValidObjects(selected));
        findUsagesAction.setEnabled(selected != null && selected.length > 0 && isOnlyValidObjects(selected));
    }

    private Object[] getSelectedUserObjects() {
        int[] selectedRows = view.getOutline().getSelectedRows();

        if (selectedRows.length > 0) {
            final Object[] objects = new Object[selectedRows.length];

            for (int i = 0; i < selectedRows.length; ++i) {
                final Object value = view.getOutline().getValueAt(selectedRows[i], 0);
                try {
                    final Node node = Visualizer.findNode(value);
                    if (node instanceof DependenceNode) {
                        objects[i] = ((DependenceNode) node).getObject();
                    }
                } catch (ClassCastException e) {
                    Logger.getLogger(DependenciesPanel.class.getName()).log(Level.INFO, "", e);
                }

                if (objects[i] == null) {
                    objects[i] = value;
                }
            }
            return objects;
        }
        return new Object[0];
    }

    private Object getSelectedUserObject() {
        final Object[] selectedUserObjects = getSelectedUserObjects();
        if (selectedUserObjects.length > 1 || selectedUserObjects.length == 0) {
            return null;
        }
        return selectedUserObjects[0];
    }

    private void setSelection(Set<Dependences.Dependence> selection) {
    }
}
