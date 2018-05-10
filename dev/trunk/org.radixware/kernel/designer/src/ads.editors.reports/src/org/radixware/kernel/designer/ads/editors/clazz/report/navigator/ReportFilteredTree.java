package org.radixware.kernel.designer.ads.editors.clazz.report.navigator;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeSelectionModel;
import org.radixware.kernel.common.components.FilteredTreeView;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObject.RenameEvent;
import org.radixware.kernel.common.defs.RadixObject.RenameListener;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportForm;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportPropertyCell;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportSummaryCell;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportWidget;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.IReportNavigatorObject;
import org.radixware.kernel.common.utils.events.IRadixEventListener;
import org.radixware.kernel.common.utils.events.RadixEvent;
import org.radixware.kernel.designer.ads.editors.clazz.report.AdsReportWidgetNamePanel;
import org.radixware.kernel.designer.ads.editors.clazz.report.diagram.AbstractAdsReportWidget;
import org.radixware.kernel.designer.ads.editors.clazz.report.diagram.AdsReportFormDiagram;
import org.radixware.kernel.designer.ads.editors.clazz.report.diagram.AdsReportSelectableWidget;
import org.radixware.kernel.designer.ads.editors.clazz.report.diagram.AdsReportWidgetUtils;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;

public class ReportFilteredTree extends FilteredTreeView{

    public ReportFilteredTree() {
        setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
    }

    
    private AdsReportFormDiagram diagram;
    private final TreeSelectionListener treeSelectionListener = new TreeSelectionListener() {

        @Override
        public void valueChanged(TreeSelectionEvent e) {
            Object[] objects = getSelectedUserObjects();
            List<RadixObject> userObjects = new ArrayList<>();
            if (objects != null) {
                if (objects.length == 1) {
                    Object object = objects[0];
                    if (object instanceof RadixObject) {
                        AbstractAdsReportWidget widget = findWidget((RadixObject) object);
                        if (widget instanceof AdsReportSelectableWidget) {
                            Item item = findItemByUserObject(object);
                            if (item != null && item instanceof ReportWidgetItem) {
                                addAction(((ReportWidgetItem) item).getRenameItemAction(), RenameWidgetAction.NAME, RenameWidgetAction.KEY);
                            }
                        }
                        userObjects.add((RadixObject) object);
                    }
                } else {
                    for (Object object : objects) {
                        if (object instanceof RadixObject) {
                            userObjects.add((RadixObject) object);
                        }
                    }
                }
            }
            diagram.removeSelectionListener(selectionListener);
            diagram.setSelectedObjects(userObjects);
            diagram.addSelectionListener(selectionListener);
        }
    };
    private MouseAdapter mouseListener = new MouseAdapter() {

        private static final int DOUBLE_CLICK = 2;

        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == DOUBLE_CLICK) {
                final Object userObject = getLastSelectedUserObject();
                if (userObject instanceof RadixObject) {
                    final RadixObject radixObject = (RadixObject) userObject;
                    DialogUtils.goToObject(radixObject);
                }
            }
        }
    };
    final AdsReportForm.IChangeListener changeListener = new AdsReportForm.IChangeListener() {

        @Override
        public void onEvent(final AdsReportForm.ChangedEvent e) {
            if (diagram == null){
                return;
            }
            AdsReportForm form = diagram.getForm();
            switch (e.type) {
                case ADD:
                case REMOVE:
                    RadixObject radixObject = e.radixObject;
                    if (e.type == AdsReportForm.ChangedEvent.ChangeEventType.ADD) {
                        RadixObject parent = null;
                        if (radixObject instanceof IReportNavigatorObject) {
                            parent = ((IReportNavigatorObject) radixObject).getParent();
                        }

                        if (parent != null) {
                            Item item = findItemByUserObject(parent);
                            if (item != null) {
                                updateTree(radixObject, form.getMode(), item);
                                return;
                            }
                        }
                    }

                    if (e.type == AdsReportForm.ChangedEvent.ChangeEventType.REMOVE) {
                        Item item = findItemByUserObject(radixObject);
                        if (item != null) {
                            item.remove();
                            return;
                        }
                    }
                case MODE: case LOADING:
                    getRoot().clear();
                    updateTree(form, form.getMode(), getRoot());
            }
        }
    };
    final IRadixEventListener<RadixEvent> selectionListener = new IRadixEventListener<RadixEvent>() {

            @Override
            public void onEvent(final RadixEvent e) {
                if (diagram == null){
                    return;
                }
                removeTreeSelectionListener(treeSelectionListener);
                Set<RadixObject> userObjects = new HashSet<>(diagram.getSelectedObjects());
                setSelection(userObjects);
                addTreeSelectionListener(treeSelectionListener);
                if (userObjects.size() == 1) {
                    RadixObject object = userObjects.iterator().next();
                    Item item = findItemByUserObject(object);
                    if (item != null && item instanceof ReportWidgetItem) {
                        addAction(((ReportWidgetItem) item).getRenameItemAction(), RenameWidgetAction.NAME, RenameWidgetAction.KEY);
                    }
                }
            }
        };
    
    public AdsReportFormDiagram getDiagram() {
        return diagram;
    }

    public void open(final AdsReportFormDiagram diagram) {
       if (diagram == this.diagram) {
            return;
        }
        
        if (this.diagram != null) {
            AdsReportForm form = diagram.getForm();
            removeTreeSelectionListener(treeSelectionListener);
            diagram.removeSelectionListener(selectionListener);
            removeMouseListener(mouseListener);
            form.addChangeListener(changeListener);
        }
        
        this.diagram = diagram;
        AdsReportForm form = diagram.getForm();
        updateTree(form, form.getMode(), getRoot());
        addTreeSelectionListener(treeSelectionListener);
        diagram.addSelectionListener(selectionListener);
        addMouseListener(mouseListener);
        form.addChangeListener(changeListener);
    }
    
        
    private void updateTree(RadixObject radixObject, AdsReportForm.Mode mode, FilteredTreeView.Item parent) { 
        if (radixObject instanceof IReportNavigatorObject){
            IReportNavigatorObject reportNavigatorObject = (IReportNavigatorObject) radixObject;
            if (reportNavigatorObject.isDiagramModeSupported(mode)){
                ReportWidgetItem n = addNode(radixObject, parent);
                List<RadixObject> children = reportNavigatorObject.getChildren();
                if (children != null){
                    for (RadixObject child : children){
                        updateTree(child, mode, n);
                    }
                }
            }
        }
    }
    
    private ReportWidgetItem addNode(RadixObject radixObject, FilteredTreeView.Item parent) {
        ReportWidgetItem node = new ReportWidgetItem(radixObject);
        parent.add(node, false);
        return node;
    }

    public class ReportWidgetItem extends FilteredTreeView.Item {
        private RenameListener renameListener = new RenameListener() {
            @Override
            public void onEvent(RenameEvent e) {
                setDisplayName(getRadixObject().getName());
                reload();
            }
        };
        private final RenameWidgetAction renameItemAction;
        public ReportWidgetItem(RadixObject radixObject) {
            super(radixObject);
            final Icon icon = radixObject.getIcon().getIcon();

            setDisplayName(radixObject.getName());
            setIcon(icon);
            renameItemAction = radixObject instanceof AdsReportWidget ? new RenameWidgetAction(this) : null;
        }

        public RadixObject getRadixObject() {
            return (RadixObject) getUserObject();
        }

        @Override
        public String getToolTipText() {
            return getRadixObject().getToolTip();
        }

        @Override
        protected void onRegister() {
            addRenameListener();
        }

        @Override
        protected void onUnregister() {
            removeRenameListener();
        }
        
        public void addRenameListener(){
            getRadixObject().addRenameListener(renameListener);
        }
        
        public void removeRenameListener(){
            getRadixObject().removeRenameListener(renameListener);
        }

        public RenameWidgetAction getRenameItemAction() {
            return renameItemAction;
        }

        @Override
        public void beforePaint() {
            RadixObject radixObject = getRadixObject();
            if (radixObject instanceof AdsReportPropertyCell){
                AdsReportPropertyCell cell = (AdsReportPropertyCell) radixObject;
                if (cell.getPropertyId() != null && cell.findProperty() == null){
                    setColor(Color.RED);
                } else {
                    setColor(Color.BLACK);
                }
            }
            if (radixObject instanceof AdsReportSummaryCell){
                AdsReportSummaryCell cell = (AdsReportSummaryCell) radixObject;
                if (cell.getPropertyId() != null && cell.findProperty() == null){
                    setColor(Color.RED);
                } else {
                    setColor(Color.BLACK);
                }
            }
        }
    }
    
    protected AbstractAdsReportWidget findWidget(RadixObject radixObject) {
        return diagram.findWidget(radixObject);
    }

    @Override
    public JPopupMenu getPopupMenu(Item item) {
        if (item instanceof ReportWidgetItem){
            ReportWidgetItem reportWidgetItem = (ReportWidgetItem) item;
            AbstractAdsReportWidget widget = findWidget(reportWidgetItem.getRadixObject());
            JPopupMenu menu = AdsReportWidgetUtils.getPopupMenu(widget);
            RenameWidgetAction action = reportWidgetItem.getRenameItemAction();
            if (action != null) {
                menu.addSeparator();
                menu.add(action);
            }
            return menu;
        }
        return super.getPopupMenu(item);
    }
    
     public static class RenameWidgetAction extends AbstractAction{
        private final ReportWidgetItem item; 
        private static String KEY = "F2";
        private static String NAME = "Rename";
        
        public RenameWidgetAction(ReportWidgetItem item){
            super(NAME);
            this.item = item;
            putValue(Action.MNEMONIC_KEY, KeyEvent.VK_F2);
            putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KEY));
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            item.removeRenameListener();
            RadixObject radixObject = item.getRadixObject();
            if (radixObject instanceof AdsReportWidget){
                AdsReportWidgetNamePanel.showModal((AdsReportWidget) radixObject);
            }
            item.addRenameListener();
        }
         
     }
     
    @Override
    protected void addAction(Action a, String name, String key) {
        getActionMap().put(name, a);
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(key), name);
        getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(key), name);
        super.addAction(a, name, key);
    }
}
