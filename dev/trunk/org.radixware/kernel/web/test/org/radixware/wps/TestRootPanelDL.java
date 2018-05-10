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
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.meta.mask.EditMaskInt;
import org.radixware.kernel.common.client.meta.mask.EditMaskList;
import org.radixware.kernel.common.client.meta.mask.EditMaskNum;
import org.radixware.kernel.common.client.meta.mask.EditMaskStr;
import org.radixware.kernel.common.client.widgets.IButton;
import org.radixware.kernel.common.client.widgets.IButton.ClickHandler;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.wps.WebServer;
import org.radixware.wps.WpsEnvironment;
import org.radixware.wps.dialogs.EnterPasswordDialog;
import org.radixware.wps.dialogs.IDialogDisplayer;
import org.radixware.wps.icons.WpsIcon;
import org.radixware.wps.rwt.*;
import org.radixware.wps.rwt.tree.Node.DefaultNode;
import org.radixware.wps.views.editors.valeditors.*;


public class TestRootPanelDL extends RootPanel {

    private WpsEnvironment env;

    public TestRootPanelDL(WpsEnvironment env) {
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
        //add(container);
        container.setTop(10);
        container.setLeft(10);
        container.setWidth(250);
        container.setHeight(200);
        final Panel panel = new Panel();
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

        row = panel.getTable().addRow();
        cell = row.addCell();
        cell.setVfit(true);
        cell.setHfit(true);

        final TextField tf = new TextField("Textfield tf");

        DecoratedLabel dl1 = new DecoratedLabel("DL1 default text");
        add(dl1);
        dl1.setTop(10);
        dl1.setLeft(10);
        dl1.setWidth(150);
        dl1.setLabelOpacity(0.9);
        dl1.appendLine("multistring text multistring text " + "\n" + "multistring text" + "\n");

        String dltext = dl1.getText();
        dl1.appendLine("dl1 contains this text: " + dltext);

        Splitter splitter = new Splitter();
        add(splitter);
        splitter.setLeft(10);
        splitter.setTop(124);
        splitter.add(20, dl1);//

        final DecoratedLabel dl2 = new DecoratedLabel("Default Text for dl2");

        PushButton dl2button = new PushButton("I am new button");
        dl2button.addClickHandler(new ClickHandler() {

            String dl2HId = dl2.getHtmlId();

            @Override
            public void onClick(IButton source) {
                MessageBox.confirmation(env.getDialogDisplayer(), "dl2buttonTest", "dl2_HtmlID = " + dl2HId).execDialog();//.addButton(EDialogButtonType.OK);
            }
        });

        add(dl2);
        dl2.setTop(10);
        dl2.setLeft(180);
        dl2.setWidth(150);
        dl2.appendLine("append 2 line");
        dl2.appendLine("append 3 line");
        dl2.setForeground(Color.red);//цвет шрифта
        dl2.setBackground(Color.white);//цвет фона dnw
        dl2.setLabelOpacity(.8);//прозрачность

        dl2.appendLine("Label Opacity = " + dl2.getOpacity());
        dl2.appendObject(dl2button);//adds only one button
        dl2.appendObject(tf); //add textfield

        //dl2.setText("Replacing all the text in the dl2");//setText into the 2nd line
        PushButton dl3button = new PushButton("Create DL14");
        dl2.appendObject(dl3button);

        dl3button.addClickHandler(
                new ClickHandler() {

                    @Override
                    public void onClick(IButton source) {
                        DecoratedLabel dl14 = new DecoratedLabel("New DecoratedLabel");
                        dl14.setLeft(180);
                        dl14.setTop(240);
                        add(dl14);
                    }
                });

        panel.getAnchors().setLeft(new Anchors.Anchor(1, 10, dl2));//?
        panel.getAnchors().setTop(new Anchors.Anchor(1, 10, dl2));//?

        //dl2.appendObject(dl1);

        cell = row.addCell();

        //cell.setComponent(b2);

        row = panel.getTable().addRow();
        cell = row.addCell();

        cell.setComponent(tf);

        cell.setColSpan(
                2);

        Panel p = new Panel();

        //add(p);

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
            intController.setEditMask(new EditMaskInt(Long.valueOf(200), Long.valueOf(10000), (byte) 4, null, 2, Character.valueOf('`'), (byte) 10));

            row = p.getTable().addRow();
            cell = row.addCell();
            cell.setComponent((UIObject) intController.getValEditor());
            intController.setValue(Long.valueOf(300));
            ((UIObject) intController.getValEditor()).setEnabled(false);
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
        final TextField tf2 = new TextField();

        {
            row = p.getTable().addRow();
            cell = row.addCell();
            cell.setComponent(tf2);
        }

        PushButton checkFocus = new PushButton("Check Focus Request");
        checkFocus.setVisible(false);

        checkFocus.setLeft(
                10);
        checkFocus.setTop(
                400);

        add(checkFocus);
        final TextField tftf = new TextField("Should be focused");
        tftf.setVisible(false);

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


        final DecoratedLabel dl3 = new DecoratedLabel("I`m another dl " + getHtmlId());//
        //String textdl3 = dl3.getObjectName(); 

        PushButton testVerticalBox = new PushButton("Test Vertical Box");
        add(testVerticalBox);
        testVerticalBox.setVisible(false);
        testVerticalBox.setTop(450);
        testVerticalBox.setLeft(0);
        dl3.setTop(10);
        dl3.setLeft(400);
        dl3.setBorderRadius(15);
        dl3.setBorderColor(Color.darkGray);
        dl3.setHorizontalAlign(Alignment.CENTER);
        add(dl3);

        DecoratedLabel dl6 = new DecoratedLabel(null);
        add(dl6);
        dl6.setTop(60);
        dl6.setLeft(400);

        DecoratedLabel dl7 = new DecoratedLabel(null);
        dl7.appendObject(new PushButton("Empty DL"));
        add(dl7);
        dl7.setTop(100);
        dl7.setLeft(400);

        final Grid wideGrid = new Grid();
        wideGrid.setBorderColor(Color.black);
        wideGrid.setWidth(600);
        //wideGrid.setHeight(300);
        wideGrid.addColumn("One");
        wideGrid.addColumn("Two");
        wideGrid.addColumn("Three");

        for (int i = 0; i < 12; i++) {
            wideGrid.addRow();
        }

        wideGrid.setHeaderVisible(false);


        for (int i = 0; i < wideGrid.getRowCount(); i++) {
            for (int j = 0; j < wideGrid.getColumnCount(); j++) {
                wideGrid.getRow(i).getCell(j).setValue(i * j);
            }
        }

        DecoratedLabel dl8 = new DecoratedLabel("DL8 " + getHtmlId() + " (with grid)");
        dl8.appendObject(wideGrid);
        dl8.setBackground(Color.LIGHT_GRAY);
        dl8.setTop(350);
        dl8.setLeft(180);
        //dl8.setWidth(300);
        dl8.setBorderColor(Color.CYAN);
        wideGrid.setStickToRight(true);
        add(dl8);
        String dl8text = dl8.getText();
        dl2.appendLine(dl8text);

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
                ((UIObject) controller.getValEditor()).getAnchors().setRight(new UIObject.Anchors.Anchor(1, -10));
                ((UIObject) controller.getValEditor()).setLeft(10);
                ((UIObject) controller.getValEditor()).setTop(10);
                //dialog.add(((UIObject) controller.getValEditor()));
                dialog.addCloseAction(EDialogButtonType.CLOSE);
                dialog.execDialog();

            }
        });

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

                //dialog.add(grid);
                grid.getAnchors().setRight(new UIObject.Anchors.Anchor(1, -10));
                grid.getAnchors().setBottom(new UIObject.Anchors.Anchor(1, -10));
                grid.setLeft(10);
                grid.setTop(10);
                dialog.setWidth(400);
                dialog.setHeight(400);
                dialog.addCloseAction(EDialogButtonType.OK);
                dialog.execDialog();
            }
        });

        wideGrid.setBorderColor(Color.black);
        //wideGrid.setWidth(600);
        //wideGrid.setHeight(300);
        wideGrid.addColumn("One");
        wideGrid.addColumn("Two");
        wideGrid.addColumn("Three");

        DecoratedLabel dl9 = new DecoratedLabel("Default  text for  dl9");
        dl9.appendLine(dl9.getToolTip());

        //dl9.appendObject(newGrid);//dnw
        dl9.setTop(150);
        dl9.setLeft(400);
        dl9.appendObject(new CalendarWidget(env));
        add(dl9);
        dl9.appendLine("dl9_parent: " + dl9.getParent().toString());
        //dl9.appendLine("dl9 getHtml(): " + dl9.getHtml().toString());

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

        final PushButton button = new PushButton("Приклеить последнюю колонку");
        button.setVisible(false);
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
        button2.setLeft(10);
        button2.setTop(540);
        button2.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(IButton source) {
                MessageBox.exceptionBox(getDialogDisplayer(), "Exceptino", "Text message", "Details:", "Some\nDetails\nText").execDialog();
            }
        });

        PushButton button3 = new PushButton("Enter password");
        button3.setTop(510);
        button3.setLeft(10);
        button3.getAnchors().setLeft(new UIObject.Anchors.Anchor(1, 10, button2));
        add(button3);
        button3.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(IButton source) {
                new EnterPasswordDialog(env).execDialog();
            }
        });
        PushButton aaa = new PushButton("Toggle grid header");
        aaa.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(IButton source) {
                wideGrid.setHeaderVisible(!wideGrid.isHeaderVisible());
            }
        });
        ToolButton tbt = new ToolButton();

        PushButton button5 = new PushButton("Show html message");

        final CheckBox testCheckBox2 = new CheckBox();
        testCheckBox2.setText("Test check box2");
        testCheckBox2.setSelected(true);

        DecoratedLabel dl4 = new DecoratedLabel("DecoratedLabel_4");
        dl4.setTop(10);
        dl4.setLeft(600);
        dl4.appendObject(aaa);

        dl4.setBorderColor(Color.LIGHT_GRAY);
        //dl4.setHorizontalAlign(Alignment.CENTER);
        //dl4.setVerticalAlign(Alignment.MIDDLE);
        dl4.setLabelOpacity(2.6);
        dl4.setWidth(200);
        dl4.appendLine("append text to dl4");
        add(dl4);
        
        
        dl4.appendObject(testCheckBox2);
        dl4.appendObject(button5);
        
        dl4.setBorderWidth(5);
        dl4.setBackground(Color.yellow);
        dl4.appendObject(tbt);
                        
        dl4.setObjectAlign(aaa, Alignment.RIGHT);     
        dl4.setObjectAlign(button5, Alignment.CENTER);
        
        PushButton button4 = new PushButton("Add grid rows");
        button4.setTop(200);
        button4.setLeft(10);
        button4.getAnchors().setLeft(new UIObject.Anchors.Anchor(1, 10, button3));
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

        PushButton button6 = new PushButton("Button");
        button6.addClickHandler(
                new ClickHandler() {

                    @Override
                    public void onClick(IButton source) {
                        //container.setBackground(Color.gray);
                        // panel.setBackground(Color.gray);
                        env.getMainWindow().setBackground(Color.gray);

                    }
                });

        DecoratedLabel dl10 = new DecoratedLabel("default text for dl 10");
        
        dl10.appendObject(button6);
        dl10.setLeft(10);
        dl10.setTop(200);
       // dl10.setObjectAlign(button6, Alignment.RIGHT);
        add(dl10);
        add(button2);
        //dl10.appendLine("This button parent: "+button2.getParent().toString());
        dl10.appendLine("This button object name: " + button2.getObjectName());

        DecoratedLabel dl11 = new DecoratedLabel("DecoratedLabel dl11");
        dl11.setLeft(10);
        dl11.setTop(360);
        add(dl11);
        ListBox lb = new ListBox();
        lb.setHeight(50);
        lb.setBackground(Color.YELLOW);
        dl11.appendObject(lb);
        
        final PushButton b = new PushButton("PushButton");
        
        final DecoratedLabel dl12 = new DecoratedLabel("Text for dl12");
        dl12.setTop(10);
        dl12.setLeft(900);
        dl12.appendLine(null);
        dl12.appendLine("Text text text text");
        dl12.appendObject(b);
        dl12.setObjectAlign(b, Alignment.CENTER);
        dl12.appendLine("Text \n");
        dl12.appendLine("\n");
        add(dl12);
        dl12.setWidth(300);
        
        PushButton b2 = new PushButton("PushButton2");//remove Ojbect
        b2.addClickHandler(
                new ClickHandler() {
                    @Override
                    public void onClick(IButton source) {
                       //dl12.removeObject(b);
                       env.updateToCurrentVersion();
                    }
                });
        
        
        DecoratedLabel dl13 = new DecoratedLabel("Decoratedlabel 13");
        dl13.appendLine("decorated label decorated label decorated label decorated label decorated label decorated label decorated label decorated label decorated label decorated label ");
        dl13.setBorderColor(Color.yellow);
        dl13.setWidth(450);
        dl13.setTop(180);
        dl13.setLeft(600);
        dl13.appendObject(b2);
        dl13.setObjectAlign(b2, Alignment.LEFT);
       // dl13.setObjectAlign(b2, Alignment.INHERIT);
        add(dl13);
        
        button5.getAnchors().setLeft(new UIObject.Anchors.Anchor(1, 10, button3));
        //add(button5);
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

    }
}