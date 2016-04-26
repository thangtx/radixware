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

package org.radixware.kernel.explorer.editors.xml;

import com.trolltech.qt.core.QEvent;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.core.Qt.ContextMenuPolicy;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.xml.namespace.QName;

import org.apache.xmlbeans.SchemaLocalAttribute;
import org.apache.xmlbeans.SchemaParticle;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlObject;

import com.trolltech.qt.gui.QAction;
import com.trolltech.qt.gui.QHeaderView;
import com.trolltech.qt.gui.QMenu;
import com.trolltech.qt.gui.QPalette;
import com.trolltech.qt.gui.QTableWidget;
import com.trolltech.qt.gui.QTableWidgetItem;
import com.trolltech.qt.gui.QToolButton;
import com.trolltech.qt.gui.QTreeWidget;
import com.trolltech.qt.gui.QTreeWidgetItem;
import com.trolltech.qt.gui.QWidget;
import com.trolltech.qt.internal.QSignalEmitterInternal;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.ETextOptionsMarker;
import org.radixware.kernel.explorer.editors.valeditors.ValEditor;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.kernel.explorer.text.ExplorerTextOptions;

public class TreeWindow extends QWidget {

    private Ui_TreeWindow tw = new Ui_TreeWindow();
    private final Map<String,Charset> charsetsByXPath = new HashMap<>(8);
    private boolean treeModified = false;
    private XmlEditor editor;
    private XmlObject xml = null;
    private final TreeBuilder treeBuilder;

    public TreeWindow(XmlEditor tree) {
        super((QWidget) null);
        editor = tree;
        tw.setupUi(this);
        additionalSetup();
        treeBuilder = new TreeBuilder(tree.getEnvironment(), tw.treeWidget);
    }

    public IClientEnvironment getEnvironment() {
        return editor.getEnvironment();
    }

    public void addToEditorsNamespaceMap(String namespace, String prefix) {
        editor.addToNamespaceMap(namespace, prefix);
    }

    private void additionalSetup() {
        tw.gridLayout.setMargin(0);
        tw.treeWidget.clear();
        tw.treeWidget.setColumnCount(2);
        tw.treeWidget.header().setStretchLastSection(true);
        LinkedList<String> labels = new LinkedList<String>();
        labels.add(Application.translate("XmlEditor", "Name"));
        labels.add(Application.translate("XmlEditor", "Value"));
        tw.treeWidget.setHeaderLabels(labels);
        tw.treeWidget.setColumnWidth(0, 250);
        tw.treeWidget.itemSelectionChanged.connect(this, "setupToolsForItem()");
        tw.treeWidget.itemExpanded.connect(this, "onItemExpand(QTreeWidgetItem)");
        tw.treeWidget.itemCollapsed.connect(this, "updateTree()");
        setupTables();
        tw.treeWidget.setContextMenuPolicy(ContextMenuPolicy.ActionsContextMenu);
    }

    private static class UpdateTreeWidgetEvent extends QEvent {//added by yremizov

        private final QTreeWidgetItem treeItem;
        private final QTreeWidget tree;

        public UpdateTreeWidgetEvent(QTreeWidgetItem item) {
            super(QEvent.Type.User);
            treeItem = item;
            tree = item != null ? item.treeWidget() : null;
        }

        public UpdateTreeWidgetEvent(QTreeWidget treeWidget) {
            super(QEvent.Type.User);
            tree = treeWidget;
            treeItem = null;
        }

        public void process() {
            if (tree != null) {
                tree.resizeColumnToContents(1);
            }
            unselectEditors();
        }

        //remove selection in editors under expanded item.  qt bug ?
        private void unselectEditors() {
            if (treeItem != null) {
                QWidget itemWidget;
                ValEditor editor;
                for (int i = 0; i < treeItem.childCount(); i++) {
                    itemWidget = tree.itemWidget(treeItem.child(i), 1);
                    if (itemWidget instanceof ValEditor) {
                        editor = (ValEditor) itemWidget;
                        editor.getLineEdit().setSelection(0, 0);
                    }
                }
            }
        }
    }

    @Override
    protected void customEvent(final QEvent event) {
        if (event instanceof UpdateTreeWidgetEvent) {
            ((UpdateTreeWidgetEvent) event).process();
        } else {
            super.customEvent(event);
        }
    }

    private void updateTree() {
        //changed by yremizov:
        //valEditors have not their actual size at this moment,
        //so start resizing after paint operation complit.
        com.trolltech.qt.gui.QApplication.postEvent(this, new UpdateTreeWidgetEvent(tw.treeWidget));
    }

    @SuppressWarnings("unused")//slot for tw.treeWidget.itemExpanded
    private void onItemExpand(final QTreeWidgetItem item) {
        if (item.childCount() == 0 && (item instanceof XTreeTag)) {
            treeBuilder.addChildItems((XTreeTag) item);
        }
        //changed by yremizov:
        //valEditors have not their actual size at this moment,
        //so start resizing after paint operation complit.
        com.trolltech.qt.gui.QApplication.postEvent(this, new UpdateTreeWidgetEvent(item));
    }

    private void setupTables() {
        tw.tableWidget.verticalHeader().setVisible(true);
        tw.tableWidget.verticalHeader().setFixedWidth(30);
        QTableWidgetItem hHeader1_2 = new QTableWidgetItem();
        hHeader1_2.setText(Application.translate("XmlEditor", "Code"));
        QTableWidgetItem hHeader1_3 = new QTableWidgetItem();
        hHeader1_3.setText(Application.translate("XmlEditor", "Message"));
        tw.tableWidget.setColumnCount(2);
        tw.tableWidget.setColumnWidth(1, 100);
        tw.tableWidget.horizontalHeader().setResizeMode(1, QHeaderView.ResizeMode.Stretch);
        tw.tableWidget.horizontalHeader().setResizeMode(0, QHeaderView.ResizeMode.Interactive);
        tw.tableWidget.setHorizontalHeaderItem(0, hHeader1_2);
        tw.tableWidget.setHorizontalHeaderItem(1, hHeader1_3);
        tw.tableWidget.setRowCount(0);
        tw.tableWidget.verticalHeader().sectionClicked.connect(this, "toError(Integer)");
    }

    private void setupColors(final boolean isReadonly) {//added by yremizov: setup of color
        final ExplorerTextOptions options;
        if (isReadonly){
            options = ExplorerTextOptions.Factory.getOptions(ETextOptionsMarker.READONLY_VALUE);
        }
        else{
            options = ExplorerTextOptions.getDefault();
        }
        {//tree settings:
            final QPalette palette = new QPalette(tw.treeWidget.viewport().palette());
            palette.setColor(tw.treeWidget.viewport().backgroundRole(), options.getBackground());
            tw.treeWidget.viewport().setPalette(palette);
        }
        {//table settings
            final QPalette palette = new QPalette(tw.tableWidget.viewport().palette());
            palette.setColor(tw.tableWidget.backgroundRole(), options.getForeground());
            palette.setColor(tw.tableWidget.foregroundRole(), options.getBackground());
            tw.tableWidget.viewport().setPalette(palette);
        }
    }

    @SuppressWarnings("unused")
    private void toError(Integer i) {
        TableEditError tee = (TableEditError) tw.tableWidget.verticalHeaderItem(i);
        tee.toErrorLocation();
    }

    public void open(boolean r, XmlObject doc) {
        xml = doc;//editor.getCurrentDocument();
        treeBuilder.addRootItems(xml);
        updateTree();
        setupColors(r);//added by yremizov: setup of color
    }

    public void setupToolsForItem() {
        if (editor.isReadOnlyMode() || tw.treeWidget.currentItem()==null){
            return;
        }
        QTreeWidgetItem cItem = tw.treeWidget.currentItem();

        while (!tw.treeWidget.actions().isEmpty()) {
            tw.treeWidget.removeAction(tw.treeWidget.actions().get(0));
        }

        if (cItem instanceof XTreeAttribute) {
            setupAttributeMenus((XTreeAttribute) cItem);
        }

        if (cItem instanceof XTreeTag) {
            setupTagMenus((XTreeTag) cItem);
        }

        QAction an = editor.getMenuItem(editor.mTree, "addnode");
        tw.treeWidget.addAction(an);
        QAction acn = editor.getMenuItem(editor.mTree, "addchild");
        tw.treeWidget.addAction(acn);
        QAction addattr = editor.getMenuItem(editor.mTree, "addattr");
        tw.treeWidget.addAction(addattr);
        QAction addattrnext = editor.getMenuItem(editor.mTree, "addattrnext");
        tw.treeWidget.addAction(addattrnext);

        if (cItem != null) {
            Object widget = cItem.data(0, Qt.ItemDataRole.UserRole);
            if (widget != null && widget instanceof XComboBox) {
                XComboBox combo = (XComboBox) widget;
                List<String> items = combo.getValues();

                if (!items.isEmpty()) {
                    QMenu choiceMenu = new QMenu(editor);
                    QAction choiceAction = new QAction(Application.translate("XmlEditor", "Change Tag to..."), editor);
                    choiceAction.setMenu(choiceMenu);
                    tw.treeWidget.addAction(choiceAction);

                    for (String i : items) {
                        QAction action = new QAction(i, editor);
                        action.triggered.connect(this, "choiceMenuActionTriggered(Boolean)");
                        choiceMenu.addAction(action);
                    }
                }
            }
        }

    }

    private void choiceMenuActionTriggered(Boolean val) {
        QTreeWidgetItem cItem = tw.treeWidget.currentItem();
        if (cItem==null){
            return;
        }
        QSignalEmitterInternal obj = QTreeWidget.signalSender();
        if (obj instanceof QAction && cItem instanceof XTreeTag) {
            QAction sender = (QAction) obj;
            String name = sender.text();
            XComboBox.choiceTagChanged(name, (XTreeTag) cItem, treeBuilder);
        }
    }

    public QWidget getErrorWidget() {
        return tw.widget3;
    }

    public QTableWidget getErrorTable() {
        return tw.tableWidget;
    }

    private void __addingAttr(XTreeTag parent) {
        QAction addattr = editor.getMenuItem(editor.mTree, "addattr");
        if (parent != null) {
            SchemaLocalAttribute[] defs = XElementTools.getAttributesDefs(parent.getSchemaType());
            if (defs != null
                    && defs.length != 0) {
                QToolButton w = editor.attrButton;
                QMenu amenu = new QMenu(editor);
                addattr.setMenu(amenu);
                setAttrMenu(parent, defs, w, amenu, addattr, "addAttribute(QAction)");
            } else {
                addattr.setDisabled(true);
                editor.attrButton.setDisabled(true);
            }
        } else {
            addattr.setDisabled(true);
            editor.attrButton.setDisabled(true);
        }
    }

    private void __addingAttrNext(XTreeTag parent) {
        QAction addattr = editor.getMenuItem(editor.mTree, "addattrnext");
        if (parent != null) {
            SchemaLocalAttribute[] defs = XElementTools.getAttributesDefs(parent.getSchemaType());
            if (defs != null
                    && defs.length != 0) {
                QToolButton w = editor.nextAttrButton;
                QMenu amenu = new QMenu(editor);
                addattr.setMenu(amenu);
                setAttrMenu(parent, defs, w, amenu, addattr, "addAttributeNext(QAction)");
            } else {
                addattr.setDisabled(true);
                editor.nextAttrButton.setDisabled(true);
            }
        } else {
            addattr.setDisabled(true);
            editor.nextAttrButton.setDisabled(true);
        }
    }

    private void setupAttributeMenus(XTreeAttribute cItem) {
        QAction acn = editor.getMenuItem(editor.mTree, "addchild");
        acn.setDisabled(true);
        editor.nextButton.setDisabled(true);
        QAction addattr = editor.getMenuItem(editor.mTree, "addattrnext");
        addattr.setDisabled(true);
        editor.nextAttrButton.setDisabled(true);
        //DELETE ATTRIBUTE
        QAction del = editor.getMenuItem(editor.mTree, "delete");
        if (cItem.isOdd()
                || cItem.getDefinition().getUse() == SchemaLocalAttribute.OPTIONAL) {
            del.setDisabled(false);
            editor.deleteAction.setDisabled(false);
        } else {
            del.setDisabled(true);
            editor.deleteAction.setDisabled(true);
        }
        //ADD ATTRIBUTE TO PARENT
        __addingAttr((XTreeTag) cItem.parent());
        //ADD NODE TO CURRENT LEVEL
        QAction an = editor.getMenuItem(editor.mTree, "addnode");
        XTreeTag parent = (XTreeTag) cItem.parent();
        if (parent != null) {
            SchemaParticle[] currentLevel = XElementTools.takeContentModel(parent.getSchemaType());
            if (currentLevel != null) {
                QToolButton w = editor.addButton;
                QMenu anBar = new QMenu(editor);
                an.setMenu(anBar);
                setMenu(an, anBar, w, parent, currentLevel, "addNode(QAction)");
            } else {
                editor.addButton.setDisabled(true);
                an.setDisabled(true);
            }
        } else {
            editor.addButton.setDisabled(true);
            an.setDisabled(true);
        }
    }

    private void setAttrMenu(XTreeTag e, SchemaLocalAttribute[] defs, QToolButton w, QMenu amenu, QAction a, String action) {
        w.setDisabled(false);
        a.setDisabled(false);
        amenu.clear();
        if (w.menu() != null)//check added by yremizov
        {
            w.menu().clear();
        }
        QMenu m = new QMenu(w);
        int che = 0;
        for (SchemaLocalAttribute i : defs) {
            XmlObject x = e.getNode();
            che++;

            if (i.getUse() != SchemaLocalAttribute.PROHIBITED) {
                if (XElementTools.getAttribute(x, i.getName().getLocalPart()) == null) {
                    addMenuItem(m, i.getName().getLocalPart());
                    addMenuItem(amenu, i.getName().getLocalPart());
                }
            }
        }
        w.setMenu(m);
        w.actions().clear();
        if (!amenu.actions().isEmpty()) {
            w.setPopupMode(QToolButton.ToolButtonPopupMode.InstantPopup);
            for (QAction ac : amenu.actions()) {
                w.addAction(ac);
            }
            w.menu().triggered.connect(this, action);
            amenu.triggered.connect(this, action);
        } else {
            w.setDisabled(true);
            a.setDisabled(true);
        }
    }

    private void setupTagMenus(XTreeTag cItem) {
        //DELETE NODE
        XTreeTag parent = (XTreeTag) cItem.parent();
        int minO = cItem.getMinOccurs();
        QAction del = editor.getMenuItem(editor.mTree, "delete");
        if (parent != null) {
            if (cItem.isOdd() == true) {
                editor.deleteAction.setDisabled(false);
                del.setDisabled(false);
            } else {
                if (minO == 0) {
                    editor.deleteAction.setDisabled(false);
                    del.setDisabled(false);
                } else {
                    int count = parent.sameChildsCount(cItem.getNode().getDomNode().getLocalName());
                    if (count > minO) {
                        editor.deleteAction.setDisabled(false);
                        del.setDisabled(false);
                    } else {
                        editor.deleteAction.setDisabled(true);
                        del.setDisabled(true);
                    }
                }
            }
        } else {
            editor.deleteAction.setDisabled(true);
            del.setDisabled(true);
        }
        //ADD NODE TO CURRENT LEVEL
        QAction an = editor.getMenuItem(editor.mTree, "addnode");
        if (parent != null) {
            SchemaParticle[] currentLevel = XElementTools.takeContentModel(parent.getSchemaType());
            if (currentLevel != null) {
                QToolButton w = editor.addButton;
                QMenu anBar = new QMenu(w);
                an.setMenu(anBar);
                setMenu(an, anBar, w, parent, currentLevel, "addNode(QAction)");
            } else {
                editor.addButton.setDisabled(true);
                an.setDisabled(true);
            }
        } else {
            editor.addButton.setDisabled(true);
            an.setDisabled(true);
        }
        //ADD NODE TO NEXT LEVEL (CHILD OF CURRENT NODE)
        SchemaParticle[] nextLevel = XElementTools.takeContentModel(cItem.getSchemaType());
        QAction acn = editor.getMenuItem(editor.mTree, "addchild");
        if (nextLevel != null) {
            QToolButton w = editor.nextButton;
            QMenu acnBar = new QMenu(w);
            acn.setMenu(acnBar);
            setMenu(acn, acnBar, w, cItem, nextLevel, "addNodeNext(QAction)");
        } else {
            editor.nextButton.setDisabled(true);
            acn.setDisabled(true);
        }
        //ADD ATTRIBUTE TO PARENT
        __addingAttr((XTreeTag) cItem.parent());
        //ADD ATTRIBUTE TO CURRENT NODE
        __addingAttrNext(cItem);
    }

    private void setMenu(QAction barOwner, QMenu bar, QToolButton w, XTreeTag parent, SchemaParticle[] model, String action) {
        w.setDisabled(false);
        barOwner.setDisabled(false);
        w.menu().clear();
        bar.clear();
        QMenu m = new QMenu(w);
        if (XElementTools.getContentModelType(parent.getSchemaType()) != SchemaParticle.CHOICE) {
            for (SchemaParticle p : model) {
                if (p.getParticleType() != SchemaParticle.WILDCARD) {
                    setMenuFromParticle(parent, m, p);
                    setMenuFromParticle(parent, bar, p);
                } else {
                    QAction a = new QAction(null, Application.translate("XmlEditor", "Add"), this);
                    a.setText(Application.translate("XmlEditor", "Any Element..."));
                    a.setDisabled(true);
                    m.addAction(a);
                }
            }
        } else {
            if (parent.getSchemaType().getContentModel() != null) {
                setMenuFromParticle(parent, m, parent.getSchemaType().getContentModel());
                setMenuFromParticle(parent, bar, parent.getSchemaType().getContentModel());
            }
        }
        w.setMenu(m);
        w.actions().clear();
        if (!w.menu().actions().isEmpty()) {
            w.setPopupMode(QToolButton.ToolButtonPopupMode.InstantPopup);
            for (QAction ac : m.actions()) {
                w.addAction(ac);
            }
            bar.triggered.connect(this, action);
            w.menu().triggered.connect(this, action);
        } else {
            w.setDisabled(true);
            barOwner.setDisabled(true);
        }
    }

    private void setMenuFromParticle(XTreeTag parent, QMenu m, SchemaParticle p) {
        int pType = p.getParticleType();
        int maxO = XElementTools.getMaxOccurs(parent.getSchemaType(), p);//p.getIntMaxOccurs();
        if (maxO != 0) {
            if (pType == SchemaParticle.ELEMENT) {
                if (parent.hasAChild(p.getName().getLocalPart(), 0) == XElementTools.NO_CHILDS) {
                    addMenuItem(m, p);
                } else {
                    String local = p.getName().getLocalPart();
                    int count = parent.sameChildsCount(local);
                    maxO = p.getIntMaxOccurs();
                    if (maxO == -1
                            || count < maxO) {
                        addMenuItem(m, p);
                    }
                }
            } else {
                if (parent.hasSequencedChild(p, 0) == XElementTools.NO_CHILDS) {
                    addSequencedMenuItem(m, p);
                } else {
                    int seq = parent.sameSequencesCount(p);
                    if (maxO == -1
                            || seq < maxO) {
                        addSequencedMenuItem(m, p);
                    }
                }
            }
        }
    }

    private void addMenuItem(QMenu m, SchemaParticle p) {
        QAction a = new QAction(null, Application.translate("XmlEditor", "Add"), this);
        a.setText(p.getName().getLocalPart());
        m.addAction(a);
    }

    private void addMenuItem(QMenu m, String n) {
        QAction a = new QAction(null, Application.translate("XmlEditor", "Add"), this);
        a.setText(n);
        m.addAction(a);
    }

    private void addSequencedMenuItem(QMenu m, SchemaParticle p) {
        QAction a = new QAction(null, Application.translate("XmlEditor", "Add"), this);
        String title = XElementTools.getTitleForSequence(p);
        a.setText(title);
        m.addAction(a);
    }

    @SuppressWarnings("unused")
    private void addNode(QAction a) {
        QTreeWidgetItem cItem = tw.treeWidget.currentItem();
        if (cItem != null) {
            XTreeTag parent = (XTreeTag) cItem.parent();
            prepareInsert(parent, a);
        }
        updateTree();
    }

    @SuppressWarnings("unused")
    private void addNodeNext(QAction a) {
        XTreeTag cItem = (XTreeTag) tw.treeWidget.currentItem();
        if (cItem != null) {
            prepareInsert(cItem, a);
        }
        updateTree();
    }

    private void prepareInsert(XTreeTag parent, QAction a) {        
        if (parent.childCount()==0 && parent.childIndicatorPolicy()==QTreeWidgetItem.ChildIndicatorPolicy.ShowIndicator){            
            treeBuilder.addChildItems(parent);
        }
        String nodeName = a.text();
        SchemaParticle def = null;
        SchemaParticle[] all = XElementTools.takeContentModel(parent.getSchemaType());
        if (XElementTools.getContentModelType(parent.getSchemaType()) == SchemaParticle.CHOICE) {
            def = parent.getSchemaType().getContentModel();
            def = def.getParticleChild(0);
            QName qnode = def.getName();
            XmlCursor c = parent.getNode().newCursor();
            c.toEndToken();
            insertingNode(qnode, parent.getNode(), c, parent);
        } else {
            def = XElementTools.getModelByName(all, nodeName);
            int i = XElementTools.getModelIndexByName(all, nodeName);
            if (def != null
                    && i != XElementTools.NO_CHILDS) {
                XmlObject doc = parent.getNode();
                XmlCursor c = doc.newCursor();
                QName next = null;
                boolean stop = false;
                XmlCursor cP = doc.newCursor();
                while (i < all.length - 1
                        && !stop) {
                    if (cP.toChild(all[i + 1].getName())) {
                        next = all[i + 1].getName();
                        stop = true;
                    } else {
                        i++;
                    }
                }
                if (next != null) {
                    c.toChild(next);
                } else {
                    c.toEndToken();
                }
                if (def.getParticleType() == SchemaParticle.ELEMENT) {
                    insertingNode(def.getName(), doc, c, parent);
                } else {
                    if (def.getParticleType() == SchemaParticle.CHOICE) {
                        def = def.getParticleChild(0);
                        insertingNode(def.getName(), doc, c, parent);
                    } else {
                        insertSequence(def, doc, c, parent);
                    }
                }
            }
        }
    }

    private void insertSequence(SchemaParticle parties, XmlObject doc, XmlCursor c, XTreeTag parent) {
        try {
            ArrayList<XmlObject> ins = putNodesToDocument(parties, doc, c);
            int index = parent.hasSequencedChild(parties, 0);
            buildInsertedSequence(ins);
            __buildNewNodesInTree(ins, parent, index);
        } catch (IllegalArgumentException exep) {
            Application.messageError(exep.getMessage());
        }
    }

    private ArrayList<XmlObject> putNodesToDocument(SchemaParticle parties, XmlObject doc, XmlCursor c) {
        ArrayList<XmlObject> result = new ArrayList<>();
        SchemaParticle[] childs = parties.getParticleChildren();
        if (childs != null) {
            for (SchemaParticle p : childs) {
                if (p.getParticleType() == SchemaParticle.ELEMENT) {
                    c.insertElement(p.getName());
                    c = doc.newCursor();
                    c.toChild(p.getName());
                    XmlObject inserted = c.getObject();
                    result.add(inserted);
                    c.toEndToken();
                    c.toNextToken();
                } else {
                    if (p.getParticleType() == SchemaParticle.CHOICE) {
                        ArrayList<XmlObject> r = putNodesToDocument(p.getParticleChild(0), doc, c);
                        for (XmlObject i : r) {
                            result.add(i);
                        }
                    } else {
                        ArrayList<XmlObject> r = putNodesToDocument(p, doc, c);
                        for (XmlObject i : r) {
                            result.add(i);
                        }
                    }
                }
            }
        }
        return result;
    }

    private void buildInsertedSequence(ArrayList<XmlObject> ins) {
        XmlBuilder xb = new XmlBuilder();
        for (int i = 0; i <= ins.size() - 1; i++) {
            XmlObject item = ins.get(i);
            xb.buildAttributes(item);
            ArrayList<Integer> inserts = new ArrayList<>();
            xb.buildNodeContent(item, null, inserts);
        }
    }

    private void insertingNode(QName newname, XmlObject doc, XmlCursor c, XTreeTag parent) {
        try {
            c.insertElement(newname);
            buildAddedNode(doc, newname, parent);
        } catch (IllegalArgumentException exep) {
            Application.messageError(exep.getMessage());
        }
    }

    private void buildAddedNode(XmlObject doc, QName newname, XTreeTag parent) {
        int atrs = XElementTools.getCurrentAttributes(parent.getNode()).size();
        int j = parent.hasAChild(newname.getLocalPart(), 0) + atrs;
        int clones = parent.sameChildsCount(newname.getLocalPart());
        XmlCursor c = doc.newCursor();
        c.toChild(newname);
        for (int ch = 1; ch <= clones; ch++) {
            c.toNextSibling(newname);
        }
        XmlObject inserted = c.getObject();
        XmlBuilder xb = new XmlBuilder();
        xb.buildAttributes(inserted);
        ArrayList<Integer> inserts = new ArrayList<>();
        xb.buildNodeContent(inserted, inserts);

        ArrayList<XmlObject> newS = new ArrayList<>();
        newS.add(inserted);
        __buildNewNodesInTree(newS, parent, j + clones - 1);
    }

    private void __buildNewNodesInTree(ArrayList<XmlObject> sequence, XTreeTag parent, int index) {
        String br = parent.text(0);
        if (br.contains("/")) {
            int slash = br.indexOf("/");
            br = br.substring(0, slash) + ">";
            parent.setText(0, br);
        }
        for (int i = 0; i <= sequence.size() - 1; i++) {
            XmlObject objToBuild = sequence.get(i);
            treeBuilder.insertChildItems(parent, objToBuild, index + 1);
        }
        parent.applyChanges();
        setModified(true);
        XTreeTag brChild = (XTreeTag) parent.child(index);
        parent.setExpanded(true);
        tw.treeWidget.setCurrentItem(brChild);
        setupToolsForItem();
    }

    //**************************************************************************
    @SuppressWarnings("unused")
    private void addAttribute(QAction a) {
        QTreeWidgetItem current = tw.treeWidget.currentItem();
        if (current!=null && current.parent() != null) {
            XTreeTag parent = (XTreeTag) current.parent();
            __addAttribute(a.text(), parent);
        }
        updateTree();
    }

    @SuppressWarnings("unused")
    private void addAttributeNext(QAction a) {
        XTreeTag current = (XTreeTag) tw.treeWidget.currentItem();
        if (current!=null){
            __addAttribute(a.text(), current);
        }
        updateTree();
    }

    private void __addAttribute(String local, XTreeTag parent) {
        SchemaLocalAttribute def = XElementTools.getAttrDef(local, parent.getSchemaType());
        String value = "";
        if (def.isDefault()) {
            value = def.getDefaultValue().getStringValue();
        }
        XmlCursor c = parent.getNode().newCursor();
        try {
            c.toNextToken();
            c.insertAttributeWithValue(def.getName(), value);
            XmlObject atrNode = XElementTools.getAttribute(parent.getNode(), def.getName().getLocalPart());
            int index = XElementTools.getAttributeIndex(parent.getNode(), local);
            XTreeAttribute atr = new XTreeAttribute(this, atrNode, def, false);
            parent.insertChild(index, atr);
            if (parent.text(0).contains("/")) {
                int slash = parent.text(0).indexOf("/");
                parent.setText(0, parent.text(0).substring(0, slash) + ">");
            }
            if (atr.getEditor() != null) {
                tw.treeWidget.setItemWidget(atr, 1, atr.getEditor());
            }
            tw.treeWidget.setCurrentItem(atr);
        } catch (IllegalArgumentException iae) {
            Application.messageError(Application.translate("XmlEditor", "Attribute cannot be added") + ":\n" + iae.getMessage());
        }
    }
    //**************************************************************************

    public void deleteCurrentNode() {
        final XTreeElement c = (XTreeElement) tw.treeWidget.currentItem();
        if (c==null){
            return;
        }
        final XTreeElement parentItem = (XTreeElement)c.parent();
        if (c instanceof XTreeTag) {
            XmlObject p = XElementTools.getParentNode(c.getNode());
            p.getDomNode().removeChild(c.getNode().getDomNode());
            parentItem.removeChild(c);
            parentItem.applyChanges();
            setModified(true);
        }
        if (c instanceof XTreeAttribute) {
            XTreeTag p = (XTreeTag) c.parent();
            final XmlObject p_node = p.getNode();
            final QName name;
            {
                final XmlCursor cursor = c.getNode().newCursor();
                try{
                    name = cursor.getName();
                }finally{
                    cursor.dispose();
                }
            }
            {
                final XmlCursor cursor = p_node.newCursor();
                try{
                    cursor.removeAttribute(name);
                }finally{
                    cursor.dispose();
                }
                
            }            
            parentItem.removeChild(c);
            parentItem.applyChanges();
            setModified(true);
        }
        updateTree();
    }
    //**************************************************************************

    public void setModified(boolean m) {
        treeModified = m;
    }

    public boolean isModified() {
        return treeModified;
    }

    public QTreeWidget getTree() {
        return tw.treeWidget;
    }

    public XmlEditor getEditor() {
        return editor;
    }
    
    final void setCharsetForXPath(final String xPath, final Charset charset){
        charsetsByXPath.put(xPath, charset);
    }
    
    final Charset findCharsetForXPath(final String xPath){
        return charsetsByXPath.get(xPath);
    }
}