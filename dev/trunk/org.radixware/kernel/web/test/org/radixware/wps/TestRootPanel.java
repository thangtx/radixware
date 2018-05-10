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

package org.radixware.wps;

import java.awt.Color;
import java.math.BigDecimal;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.meta.mask.EditMaskInt;
import org.radixware.kernel.common.client.meta.mask.EditMaskList;
import org.radixware.kernel.common.client.meta.mask.EditMaskNone;
import org.radixware.kernel.common.client.meta.mask.EditMaskNum;
import org.radixware.kernel.common.client.meta.mask.EditMaskStr;
import org.radixware.kernel.common.client.views.IProgressHandle;
import org.radixware.kernel.common.client.widgets.IButton;
import org.radixware.kernel.common.client.widgets.IButton.ClickHandler;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.wps.WebServer;
import org.radixware.wps.WpsEnvironment;
import org.radixware.wps.dialogs.EnterPasswordDialog;
import org.radixware.wps.dialogs.IDialogDisplayer;
import org.radixware.wps.icons.WpsIcon;
import org.radixware.wps.rwt.IGrid.CellEditor;
import org.radixware.wps.rwt.IGrid.CellEditorProvider;
import org.radixware.wps.rwt.ListBox.ListBoxItem;
import org.radixware.wps.rwt.*;
import org.radixware.wps.rwt.tree.Node;
import org.radixware.wps.rwt.tree.Node.DefaultNode;
import org.radixware.wps.rwt.tree.Tree;
import org.radixware.wps.views.editors.valeditors.*;


public class TestRootPanel extends RootPanel {

    private WpsEnvironment env;

    public TestRootPanel(WpsEnvironment env) {
        this.env = env;
        this.env.checkThreadState();
        ((WebServer.WsThread) Thread.currentThread()).userSession = env;
        this.createTestUI();
    }

    @Override
    protected IDialogDisplayer getDialogDisplayer() {
        return env.getDialogDisplayer();
    }

    @Override
    public IMainView getExplorerView() {
        throw new UnsupportedOperationException("Explorer view is not supported by TestRoot");
    }

    @Override
    public void closeExplorerView() {
        throw new UnsupportedOperationException("Explorer view is not supported by TestRoot");
    }

    private void createTestUI() {
        final GroupBox container = new GroupBox();
        container.setTitle("Group");
        add(container);
        container.setTop(10);
        container.setLeft(10);
        container.setWidth(250);
        container.setHeight(200);

        Panel panel = new Panel();
        container.add(panel);
        panel.setVCoverage(100);
        panel.setHCoverage(100);
        Label l = new Label("Click Me!");
        Panel.Table.Row row = panel.getTable().addRow();
        Panel.Table.Row.Cell cell = row.addCell();
        cell.setComponent(l);
        cell.setHfit(true);
        cell.setVfit(true);
        cell = row.addCell();
        cell.setComponent(new TextField("some text"));
        cell.setVfit(true);

        PushButton b = new PushButton("Click Me!");
        row = panel.getTable().addRow();
        cell = row.addCell();
        cell.setVfit(true);
        cell.setHfit(true);
        cell.setComponent(b);


        PushButton b2 = new PushButton("Start Progress");
        b2.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(IButton source) {
//                IProgressHandle ph = env.getProgressHandleManager().newProgressHandle();
//                ph.startProgress("Progress", false);
//                IProgressHandle ph2 = env.getProgressHandleManager().newProgressHandle();
//                ph2.startProgress("Determinate", false);
//                ph2.setMaximumValue(100);
//                for (int i = 0; i < 100; i++) {
//                    try {
//                        Thread.sleep(50);
//                        ph2.setValue(i);
//                    } catch (InterruptedException ex) {
//                        Logger.getLogger(TestRootPanel.class.getName()).log(Level.SEVERE, null, ex);
//                    }
//
//                }
//
//                List<IProgressHandle> handles = new LinkedList<IProgressHandle>();
//                for (int i = 0; i < 100; i++) {
//                    try {
//                        Thread.sleep(50);
//                        if (i % 10 == 0) {
//                            IProgressHandle ph3 = env.getProgressHandleManager().newProgressHandle();
//                            handles.add(ph3);
//                            ph3.startProgress("AAA" + i, DEBUG);
//                        }
//                    } catch (InterruptedException ex) {
//                        Logger.getLogger(TestRootPanel.class.getName()).log(Level.SEVERE, null, ex);
//                    }
//                }
//                for (int i = handles.size() - 1; i >= 0; i--) {
//                    IProgressHandle h = handles.get(i);
//                    try {
//                        Thread.sleep(50);
//                        h.finishProgress();
//                    } catch (InterruptedException ex) {
//                        Logger.getLogger(TestRootPanel.class.getName()).log(Level.SEVERE, null, ex);
//                    }
//                }
//                ph2.finishProgress();
//                ph.finishProgress();
//                env.messageInformation("Information", "Progress finished");

                for (int i = 0; i < 50; i++) {
                    IProgressHandle ph = env.getProgressHandleManager().newProgressHandle();
                    ph.startProgress("Progress", false);

                    if (i % 10 == 0) {
                        IProgressHandle ph2 = env.getProgressHandleManager().newProgressHandle();
                        ph.startProgress("Progress", false);
                        ph.setMaximumValue(200);
                        for (int j = 0; j < 200; j++) {
                            try {
                                Thread.sleep(10);
                                ph.setValue(j);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(TestRootPanel.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                        ph.finishProgress();
                    }
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(TestRootPanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    ph.finishProgress();
                }
            }
        });
        cell = row.addCell();

        cell.setComponent(b2);
        final TextField tf = new TextField();
        row = panel.getTable().addRow();
        cell = row.addCell();

        cell.setComponent(tf);

        cell.setColSpan(
                2);

//        
//        Panel p = new Panel();
//        add(p);
//        p.setLeft(220);
//        p.setTop(10);
//        p.setWidth(200);
//        p.setHeight(200);
//        p.setBackground(Color.red);
//        final TextField tf2 = new TextField();
//        
//        row = p.getTable().addRow();
//        cell = row.addCell();
//        cell.setComponent(tf2);
//        
//        
        Panel p = new Panel();

        add(p);

        p.setLeft(
                270);
        p.setTop(
                10);
        p.setWidth(
                200);
        p.setHeight(
                200);
        p.setBackground(Color.red);

        {
            final ValNumEditorController numController = new ValNumEditorController(env);
            numController.setEditMask(new EditMaskNum(null, null, 1, new Character('`'), (byte) 2));

            row = p.getTable().addRow();
            cell = row.addCell();
            cell.setComponent((UIObject) numController.getValEditor());
            numController.setValue(BigDecimal.valueOf(1234.56789));
        }
        final ValIntEditorController intController = new ValIntEditorController(env);
        {

            intController.setEditMask(new EditMaskInt(Long.valueOf(200), null, (byte) 4, null, 2, Character.valueOf('`'), (byte) 10));

            row = p.getTable().addRow();
            cell = row.addCell();
            cell.setComponent((UIObject) intController.getValEditor());
            intController.setValue(Long.valueOf(300));
        }

        {
            final ValBoolEditorController boolController = new ValBoolEditorController(env);

            row = p.getTable().addRow();
            cell = row.addCell();
            cell.setComponent((UIObject) boolController.getValEditor());
            ((UIObject) boolController.getValEditor()).setEnabled(false);
        }
        final EditMaskList editMask = new EditMaskList();

        for (int i = 1;
                i
                <= 10; i++) {
            editMask.addItem("Very very very  very very  very very very very long item title " + String.valueOf(i), Long.valueOf(i));
        }
        final ValListEditorController listController = new ValListEditorController(env, editMask);

        {
            listController.getValEditor().setValue(Long.valueOf(7));
            //listController.setMandatory(true);
            //listController.setReadOnly(true);
            row = p.getTable().addRow();
            cell = row.addCell();
            cell.setComponent((UIObject) listController.getValEditor());
            listController.setLabelVisible(true);
            listController.setLabel("List controller label");
        }
        final ValStrEditorController strController = new ValStrEditorController(env);

        {

            //strController.setEditMask(new EditMaskStr("-->AAAA--NNNN--XXXX;\0", true, true, -1, false));
            strController.setEditMask(new EditMaskStr("", true, true, -1, false));

            row = p.getTable().addRow();
            cell = row.addCell();
            cell.setComponent((UIObject) strController.getValEditor());
            strController.setValue("Hello\nworld!");
            strController.addMemo();
        }
        
        final ValArrEditorController<ArrStr> arrController = new ValArrEditorController<>(env, EValType.ARR_STR, null);
        final EditMaskStr arrItemEditMask = new EditMaskStr();
        arrItemEditMask.setArrayItemNoValueStr("<empty item>");
        arrController.setItemEditMask(arrItemEditMask);
        final EditMaskNone arrEditMask = new EditMaskNone();
        arrEditMask.setArrayItemNoValueStr("<empty item>");        
        arrController.setEditMask(arrEditMask);
        {
            row = p.getTable().addRow();
            cell = row.addCell();
            cell.setComponent((UIObject) arrController.getValEditor());
            arrController.setValue(new ArrStr("item1","item2",null,"item4"));
        }
        
        final TextField tf2 = new TextField();

        {
            row = p.getTable().addRow();
            cell = row.addCell();
            cell.setComponent(tf2);
        }

        b.addClickHandler(
                new ClickHandler() {

                    @Override
                    public void onClick(final IButton source) {
                        tf2.setText(strController.getValue());
                        //tf2.setText(String.valueOf(listController.getValEditor().getValue()));
                        //final EditMaskInt newMaskInt = (EditMaskInt)EditMask.newCopy(intController.getEditMask());
                        //newMaskInt.setMaxValue(1000);
                        //intController.setEditMask(newMaskInt);
                        //strController.setValue("Привет,\nмир\0");
                        //strController.setValue("Hello,\nworld\0");
                    }
                });


        ListBox box = new ListBox();

        add(box);

        box.setLeft(
                500);
        box.setTop(
                10);
        box.setWidth(
                150);
        box.setHeight(
                200);
        ListBox.ListBoxItem item = new ListBox.ListBoxItem();

        item.setText(
                "1");
        item.setToolTip(
                "2");
        box.add(item);
        item = new ListBox.ListBoxItem();

        item.setText(
                "3");
        item.setToolTip(
                "4");
        box.add(item);
        item = new ListBox.ListBoxItem();

        item.setText(
                "5");
        item.setToolTip(
                "6");
        box.add(item);

        box.addDoubleClickListener(
                new ListBox.DoubleClickListener() {

                    @Override
                    public void itemDoubleClick(ListBoxItem item) {
                        getEnvironment().messageInformation(item.getToolTip(), item.getText());
                    }
                });

        PushButton checkFocus = new PushButton("Check Focus Request");

        checkFocus.setLeft(
                10);
        checkFocus.setTop(
                400);

        add(checkFocus);
        final TextField tftf = new TextField("Should be focused");

        tftf.setLeft(
                150);
        tftf.setTop(
                400);
        tftf.setWidth(
                200);
        add(tftf);

        checkFocus.addClickHandler(
                new ClickHandler() {

                    @Override
                    public void onClick(IButton source) {
                        tftf.setFocused(true);
                    }
                });






        PushButton testVerticalBox = new PushButton("Test Vertical Box");
        add(testVerticalBox);
        testVerticalBox.setTop(450);
        testVerticalBox.setLeft(0);
        testVerticalBox.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(IButton source) {
                final VerticalBoxContainer verticalBox = new VerticalBoxContainer();

                final Dialog dialog = new Dialog(env.getDialogDisplayer(), "Test Vertical Box");

                dialog.addCustomAction("Top", Alignment.LEFT);
                dialog.addCustomAction("Center", Alignment.LEFT);
                dialog.addCustomAction("Bottom", Alignment.LEFT);
                dialog.addCustomActionListener(new Dialog.CustomActionListener() {

                    @Override
                    public void onAction(String action) {
                        if ("Bottom".equals(action)) {
                            verticalBox.setAlignment(Alignment.BOTTOM);
                        } else if ("Center".equals(action)) {
                            verticalBox.setAlignment(Alignment.CENTER);
                        } else {
                            verticalBox.setAlignment(Alignment.TOP);
                        }
                    }
                });
                dialog.addCloseAction(EDialogButtonType.OK);

                dialog.add(verticalBox);

                fillLinearBox(verticalBox);
                dialog.execDialog();
            }
        });
        PushButton testHorizontalBox = new PushButton("Test Horizontal Box");
        add(testHorizontalBox);
        testHorizontalBox.setTop(450);
        testHorizontalBox.setLeft(150);
        testHorizontalBox.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(IButton source) {
                final HorizontalBoxContainer horizontalBox = new HorizontalBoxContainer();
                final Dialog dialog = new Dialog(env.getDialogDisplayer(), "Test Horizontal Box");
                dialog.addCustomAction("Left", Alignment.LEFT);
                dialog.addCustomAction("Center", Alignment.LEFT);
                dialog.addCustomAction("Right", Alignment.LEFT);
                dialog.addCustomActionListener(new Dialog.CustomActionListener() {

                    @Override
                    public void onAction(String action) {
                        if ("Center".equals(action)) {
                            horizontalBox.setAlignment(Alignment.CENTER);
                        } else if ("Right".equals(action)) {
                            horizontalBox.setAlignment(Alignment.RIGHT);
                        } else {
                            horizontalBox.setAlignment(Alignment.LEFT);
                        }
                    }
                });
                dialog.addCloseAction(EDialogButtonType.OK);
                dialog.add(horizontalBox);
                fillLinearBox(horizontalBox);
                dialog.execDialog();
            }
        });


        ButtonBox bbox = new ButtonBox();
        PushButton yesButton = bbox.addButton(EDialogButtonType.YES);
        bbox.addButton(EDialogButtonType.NO);
        bbox.addButton(EDialogButtonType.APPLY, Alignment.LEFT);
        yesButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(IButton source) {
                Dialog dialog = new Dialog("Test val editor resize");
                dialog.setWidth(300);
                dialog.setHeight(300);

                ValStrEditorController controller = new ValStrEditorController(getEnvironment());
                controller.setValue("aaa");
                ((UIObject) controller.getValEditor()).getAnchors().setRight(new Anchors.Anchor(1, -10));
                ((UIObject) controller.getValEditor()).setLeft(10);
                ((UIObject) controller.getValEditor()).setTop(10);
                dialog.add(((UIObject) controller.getValEditor()));
                dialog.addCloseAction(EDialogButtonType.CLOSE);
                dialog.execDialog();
            }
        });

        add(bbox);

        bbox.setLeft(1100);
        bbox.setTop(100);
        bbox.setWidth(400);

        bbox.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(IButton source) {
                //MessageBox.information(env.getDialogDisplayer(), source.getTitle(), source.getTitle()).execMessageBox();

                Dialog dialog = new Dialog("Test labeled edit grid");
                final LabeledEditGrid grid = new LabeledEditGrid(new LabeledEditGrid.AbstractEditor2LabelMatcher() {

                    @Override
                    protected UIObject createLabelComonent(UIObject editorComponent) {
                        return new Label("");
                    }
                });
                Label label = new Label("Some text");
                label.setTextWrapDisabled(true);

                grid.addEditor(label, 0, 0);
                ValIntEditorController intEdit = new ValIntEditorController(getEnvironment());
                grid.addEditor((UIObject) intEdit.getValEditor(), 1, 0);
                label = new Label("Another text");
                label.setTextWrapDisabled(true);

                grid.addEditor(label, 0, 1);
                HorizontalBoxContainer hbox = new HorizontalBoxContainer();

                grid.setColumnWidth(0, 1);

                ValIntEditorController intEdit2 = new ValIntEditorController(getEnvironment());
                UIObject intEditObj = (UIObject) intEdit2.getValEditor();
                hbox.add(intEditObj);
                CheckBox cb = new CheckBox();
                hbox.setHeight(30);
                hbox.add(cb);
                hbox.setVCoverage(100);
                cb.setWidth(20);
                hbox.setAutoSize(intEditObj, true);

                grid.addEditor(hbox, 1, 1);

                dialog.add(grid);
                grid.getAnchors().setRight(new Anchors.Anchor(1, -10));
                grid.getAnchors().setBottom(new Anchors.Anchor(1, -10));
                grid.setLeft(10);
                grid.setTop(10);
                dialog.setWidth(400);
                dialog.setHeight(400);
                dialog.addCloseAction(EDialogButtonType.OK);
                dialog.execDialog();
            }
        });



        final Tree tree = new Tree();
        tree.setTop(450);
        tree.setLeft(1050);
        tree.setWidth(300);
        tree.setHeight(200);
        tree.setPersistenceKey("TEST_TREE");


        PushButton removeNode = new PushButton("Remove selected node");
        removeNode.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(IButton source) {
                Node node = tree.getSelectedNode();
                if (node != null) {
                    node.remove();
                }
            }
        });
        PushButton insertNode = new PushButton("Insert instead selected");
        insertNode.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(IButton source) {
                DefaultNode node = (DefaultNode) tree.getSelectedNode();
                if (node != null) {
                    if (node.getChildCount() == 0) {
                        node.add(new DefaultNode(node.getDisplayName() + "-added"));
                    } else {
                        DefaultNode parent = (DefaultNode) node.getParentNode();
                        if (parent != null) {
                            parent.add(parent.indexOfChild(node), new DefaultNode(node.getDisplayName() + "-added"));
                        }
                    }
                }
            }
        });
        DefaultNode root = new DefaultNode();
        DefaultNode current = root;
        int total = 0;
        while (total < 20) {
            for (int i = 0; i < 10; i++) {
                total++;
                DefaultNode child = new DefaultNode(current.getDisplayName() + "." + i);
                current.add(child);

                if (i % 3 == 0) {
                    current = child;
                } else if (i % 5 == 0) {
                    if (current != null) {
                        current = (DefaultNode) current.getParentNode();
                    }
                }
                if (i % 2 == 0) {
                    child.setIcon((WpsIcon) getEnvironment().getApplication().getImageManager().getIcon(ClientIcon.Connection.CONNECT));
                }
            }

        }
        PushButton addTreeColumn = new PushButton("Add column");
        add(addTreeColumn);
        addTreeColumn.setLeft(1050);
        addTreeColumn.setTop(250);

        tree.getTreeColumn().setPersistenceKey("TEST_TREE_ROOT");

//        tree.getTreeColumn().setCellEditorProvider(new CellEditorProvider() {
//
//            @Override
//            public CellEditor newCellEditor(int r, int c) {
//                return new CellEditor() {
//
//                    private ValStrEditorController controller = new ValStrEditorController(getEnvironment());
//
//                    @Override
//                    public void setValue(int r, int c, Object value) {
//                        controller.setValue((String) value);
//                    }
//
//                    @Override
//                    public Object getValue() {
//                        return controller.getValue();
//                    }
//
//                    @Override
//                    public void applyChanges() {
//                        //
//                    }
//
//                    @Override
//                    public void cancelChanges() {
//                        //
//                    }
//
//                    @Override
//                    public UIObject getUI() {
//                        return (UIObject) controller.getValEditor();
//                    }
//                };
//            }
//        });

        addTreeColumn.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(IButton source) {
                String id = String.valueOf(tree.getColumnCount()) + "1";
                String title = "Column " + id;
                final Tree.Column c = tree.addColumn(title);
                c.setPersistenceKey("TEST_TREE_" + id);

//                c.setCellEditorProvider(new CellEditorProvider() {
//
//                    @Override
//                    public CellEditor newCellEditor(int r, int col) {
//                        return new CellEditor() {
//
//                            private ValStrEditorController controller = new ValStrEditorController(getEnvironment());
//
//                            @Override
//                            public void setValue(int r, int c, Object value) {
//                                controller.setValue((String) value);
//                            }
//
//                            @Override
//                            public Object getValue() {
//                                return controller.getValue();
//                            }
//
//                            @Override
//                            public void applyChanges() {
//                                tree.getSelectedNode().setCellValue(c, getValue());
//                            }
//
//                            @Override
//                            public void cancelChanges() {
//                                //
//                            }
//
//                            @Override
//                            public UIObject getUI() {
//                                return (UIObject) controller.getValEditor();
//                            }
//                        };
//                    }
//                });


            }
        });
        add(insertNode);
        add(removeNode);
        insertNode.setLeft(1050);
        insertNode.setTop(300);
        removeNode.setLeft(1050);
        removeNode.setTop(350);
        PushButton toggleRoot = new PushButton("Toggle root item");
        add(toggleRoot);
        toggleRoot.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(IButton source) {
                tree.setRootVisible(!tree.isRootVisible());
            }
        });
        toggleRoot.setLeft(1050);
        toggleRoot.setTop(400);
        tree.setBorderColor(Color.black);
        add(tree);
        tree.setRootVisible(true);
        tree.setRootNode(root);

        final Grid wideGrid = new Grid();
        add(wideGrid);
        wideGrid.setTop(450);
        wideGrid.setLeft(400);
        wideGrid.setBorderColor(Color.black);
        wideGrid.setWidth(600);
        wideGrid.setHeight(300);
        wideGrid.addColumn("One");
        wideGrid.addColumn("Two");
        wideGrid.addColumn("Three");

        for (int i = 0; i < 100; i++) {
            wideGrid.addRow();
        }

        wideGrid.setHeaderVisible(false);


        for (int i = 0; i < wideGrid.getRowCount(); i++) {
            for (int j = 0; j < wideGrid.getColumnCount(); j++) {
                wideGrid.getRow(i).getCell(j).setValue(i * j);
            }
        }

        final PushButton button = new PushButton("Приклеить последнюю колонку");
        button.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(IButton source) {
                wideGrid.setStickToRight(!wideGrid.isStickToRight());
                button.setText(wideGrid.isStickToRight() ? "Отклеить последнюю колонку" : "Приклеить последнюю колонку");
            }
        });
        button.setTop(400);
        button.setLeft(400);
        add(button);
        final PushButton button2 = new PushButton("Test Message Box With Details");
        button2.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(IButton source) {
                MessageBox.exceptionBox(getDialogDisplayer(), "Exceptino", "Text message", "Details:", "Some\nDetails\nText").execDialog();
            }
        });
        button2.setTop(400);
        button2.setLeft(600);
        add(button2);
        PushButton button3 = new PushButton("Enter password");
        button3.setTop(400);
        button3.getAnchors().setLeft(new Anchors.Anchor(1, 10, button2));
        add(button3);
        button3.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(IButton source) {
                new EnterPasswordDialog(env).execDialog();
            }
        });

        PushButton aaa = new PushButton("Toggle grid header");
        aaa.setTop(600);
        aaa.setLeft(100);

        add(aaa);
        aaa.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(IButton source) {
                wideGrid.setHeaderVisible(!wideGrid.isHeaderVisible());
            }
        });

        PushButton button4 = new PushButton("Add grid rows");
        button4.setTop(400);
        button4.getAnchors().setLeft(new Anchors.Anchor(1, 10, button3));
        add(button4);
        button4.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(IButton source) {
                int init = wideGrid.getRowCount();
                for (int i = 0; i < 10; i++) {
                    Grid.Row row = wideGrid.addRow();
                    for (int j = 0; j < wideGrid.getColumnCount(); j++) {
                        row.getCell(j).setValue(init + i);
                    }
                }
            }
        });
        PushButton button5 = new PushButton("Show html message");
        button5.setTop(400);
        button5.getAnchors().setLeft(new Anchors.Anchor(1, 10, button3));
        add(button5);
        button5.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(IButton source) {
                MessageBox.information(env.getDialogDisplayer(), "aaa", "<html><body><table style=\"border: solid 1px red; width: 100%\">"
                        + "<tr style=\"background-color: green\"><td>1</td><td>2</td><td>3</td></tr>"
                        + "<tr style=\"background-color: blue\"><td>1</td><td>2</td><td>3</td></tr>"
                        + "<tr style=\"background-color: orange\"><td>1</td><td>2</td><td>3</td></tr>"
                        + "<tr style=\"background-color: navy\"><td>1</td><td>2</td><td>3</td></tr>"
                        + "</table></body></html>").execDialog();
            }
        });


        final CheckBox testCheckBox = new CheckBox();
        add(testCheckBox);
        testCheckBox.setTop(10);
        testCheckBox.setLeft(700);
        testCheckBox.setText("Test check box");
        testCheckBox.setSelected(true);

        final CheckBox testCheckBox2 = new CheckBox();
        add(testCheckBox2);
        testCheckBox2.setTop(30);
        testCheckBox2.setLeft(700);
        testCheckBox2.setText("Test check box2");
        testCheckBox2.setSelected(true);


        PushButton selectTestCheckBox = new PushButton("Set check box selected");
        selectTestCheckBox.setTop(50);
        selectTestCheckBox.setLeft(700);
        selectTestCheckBox.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(IButton source) {
                if (!testCheckBox.isSelected()) {
                    testCheckBox.setSelected(true);
                    testCheckBox2.setSelected(true);
                } else {
                    testCheckBox.setSelected(false);
                    testCheckBox2.setSelected(false);
                }
            }
        });
        add(selectTestCheckBox);


        /*
         * final TextField tf3 = new TextField("test"); add(tf3);
         * tf3.setTop(400); *
         *
         * TextArea area = new TextArea(); add(area); area.setTop(450);
         * area.setLeft(10); area.setWidth(600); area.setHeight(300);
         * area.addTextListener(new TextChangeListener() {
         *
         * @Override public void textChanged(String oldText, String newText) {
         * tf3.setText(newText); } });
         */
    }

    private void fillLinearBox(final LinearBoxContainer linearBox) {
        ClickHandler autoH = new ClickHandler() {

            @Override
            public void onClick(IButton source) {
                UIObject obj = (UIObject) source;
                boolean set = !linearBox.isAutoSize(obj);
                linearBox.setAutoSize(obj, set);
            }
        };
        PushButton pb = new PushButton("Button 1");
        linearBox.add(pb);
        pb.addClickHandler(autoH);
        pb = new PushButton("Button 2");
        linearBox.add(pb);
        pb.addClickHandler(autoH);
        final PushButton hidden = pb = new PushButton("Button 3");
        linearBox.add(pb);
        pb.addClickHandler(autoH);
        pb = new PushButton("Button 4");
        linearBox.add(pb);
        pb.addClickHandler(autoH);
        pb = new PushButton("Button 5");
        linearBox.add(pb);
        pb.addClickHandler(autoH);
        pb = new PushButton("Hide");
        linearBox.add(pb);
        pb.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(IButton source) {
                hidden.setVisible(!hidden.isVisible());
            }
        });
        linearBox.setLeft(0);
        linearBox.setTop(0);
        linearBox.getAnchors().setBottom(new Anchors.Anchor(1, -2));
        linearBox.getAnchors().setRight(new Anchors.Anchor(1, -2));



    }
}
