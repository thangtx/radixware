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
import java.util.EnumSet;
import org.radixware.kernel.common.client.enums.EKeyboardModifier;
import org.radixware.kernel.common.client.widgets.IButton;
import org.radixware.wps.WpsEnvironment;
import org.radixware.wps.dialogs.IDialogDisplayer;
import org.radixware.wps.rwt.Grid.Row;
import org.radixware.wps.rwt.*;


public class GridTestRootPanel extends RootPanel {

    private final WpsEnvironment env;

    public GridTestRootPanel(final WpsEnvironment env) {
        this.env = env;
        final Container container = new Container();
        container.setVCoverage(100);
        container.setHCoverage(100);
        add(container);

        final Grid grid = new Grid();
        grid.setPersistenceKey("test_grid");
        grid.setWidth(600);
        grid.setHeight(300);
        grid.setTop(10);
        grid.setLeft(10);
        container.add(grid);
        
        //final 


        grid.addColumn("One").setPersistenceKey("cone");
        grid.addColumn("Two").setPersistenceKey("ctwo");
        grid.addColumn("Tree").setPersistenceKey("cthree");

        grid.getColumn(0).setInitialWidth(200);
        grid.setRowHeaderVisible(true);
        
        grid.addRowHeaderClickListener(new Grid.RowHeaderClickListener() {
            @Override
            public void onClick(final Row row, final EnumSet<EKeyboardModifier> keyboardModifiers) {
                env.messageInformation("Debug", "Row Header click\nRow: "+grid.getRowIndex(row)+"\nkeyboard modifiers: "+keyboardModifiers.toString());
            }
        });
        
        grid.addRowHeaderDoubleClickListener(new Grid.RowHeaderDoubleClickListener() {
            @Override
            public void onDoubleClick(final Row row, final EnumSet<EKeyboardModifier> keyboardModifiers) {
                env.messageInformation("Debug", "Row Header double click\nRow: "+grid.getRowIndex(row)+"\nkeyboard modifiers: "+keyboardModifiers.toString());
            }
        });
        
        grid.getCornerHeaderCell().addClickListener(new Grid.CornerHeaderCell.ClickListener() {
            @Override
            public void onClick(final EnumSet<EKeyboardModifier> modifiers) {
                env.messageInformation("Debug", "Corner header cell click\nkeyboard modifiers: "+modifiers.toString());
            }
        });
        
        grid.getCornerHeaderCell().addDoubleClickListener(new Grid.CornerHeaderCell.DoubleClickListener() {
            @Override
            public void onDoubleClick(final EnumSet<EKeyboardModifier> modifiers) {
                env.messageInformation("Debug", "Corner header cell double click\nkeyboard modifiers: "+modifiers.toString());
            }
        });
        
        grid.addColumnHeaderClickListener(new Grid.ColumnHeaderClickListener() {
            @Override
            public void onClick(final Grid.Column column, final EnumSet<EKeyboardModifier> keyboardModifiers) {
                env.messageInformation("Debug", "Column header click\nColumn: "+column.getIndex()+"\nkeyboard modifiers: "+keyboardModifiers.toString());
            }
        });

        PushButton autoFill = new PushButton("Auto Fill");
        autoFill.addClickHandler(new IButton.ClickHandler() {

            @Override
            public void onClick(IButton source) {
                for (int i = 0; i < 10; i++) {
                    Row row = grid.addRow();
                    row.getCell(0).setValue("One - " + String.valueOf(i));

                    row.getCell(1).setValue("Two - " + String.valueOf(i));
                    row.getCell(2).setValue("Three - " + String.valueOf(i));
                    if (i % 2 == 0) {
                        row.getCell(0).setBackground(Color.red);
                    }
                    if (i % 3 == 0) {
                        row.getCell(1).setBackground(Color.blue);
                    }
                    if (i % 2 == 1) {
                        row.getCell(2).setBackground(Color.green);
                    }
                }
                grid.setCurrentCell(grid.getRow(2).getCell(0));
            }
        });

        autoFill.setTop(350);
        autoFill.setLeft(10);        
        container.add(autoFill);
        
        PushButton clearRows = new PushButton("Clear Rows");

        clearRows.setTop(350);
        clearRows.getAnchors().setLeft(new Anchors.Anchor(1, 10, autoFill));
        clearRows.addClickHandler(new IButton.ClickHandler() {

            @Override
            public void onClick(IButton source) {
                grid.clearRows();
            }
        });
        container.add(clearRows);        


        PushButton hideFirstColumn = new PushButton("Hide first column");

        hideFirstColumn.setTop(400);
        hideFirstColumn.setLeft(10);

        hideFirstColumn.addClickHandler(new IButton.ClickHandler() {

            @Override
            public void onClick(IButton source) {
                grid.getColumn(0).setVisible(!grid.getColumn(0).isVisible());
            }
        });
        container.add(hideFirstColumn);

        PushButton hideSecondColumn = new PushButton("Hide second column");

        hideSecondColumn.setTop(400);
        hideSecondColumn.getAnchors().setLeft(new Anchors.Anchor(1, 10, hideFirstColumn));


        hideSecondColumn.addClickHandler(new IButton.ClickHandler() {

            @Override
            public void onClick(IButton source) {
                grid.getColumn(1).setVisible(!grid.getColumn(1).isVisible());
            }
        });
        container.add(hideSecondColumn);


        PushButton hideLastColumn = new PushButton("Hide last column");

        hideLastColumn.setTop(400);
        hideLastColumn.getAnchors().setLeft(new Anchors.Anchor(1, 10, hideSecondColumn));

        hideLastColumn.addClickHandler(new IButton.ClickHandler() {

            @Override
            public void onClick(IButton source) {
                grid.getColumn(2).setVisible(!grid.getColumn(2).isVisible());
            }
        });
        container.add(hideLastColumn);

        PushButton addRow = new PushButton("Add row");

        addRow.setTop(450);
        addRow.setLeft(10);

        final int[] addedRows = new int[]{1};
        addRow.addClickHandler(new IButton.ClickHandler() {

            @Override
            public void onClick(IButton source) {
                Row row = grid.addRow();
                for (int i = 0; i < 3; i++) {
                    row.getCell(i).setValue("Added " + String.valueOf(i) + " " + String.valueOf(addedRows[0]));
                    addedRows[0]++;
                }
            }
        });
        container.add(addRow);

        PushButton insertRow = new PushButton("Insert row");

        insertRow.setTop(450);
        insertRow.getAnchors().setLeft(new Anchors.Anchor(1, 10, addRow));
        
        insertRow.addClickHandler(new IButton.ClickHandler() {

            @Override
            public void onClick(IButton source) {
                final int inspos = grid.getRowIndex(grid.getCurrentRow());
                Row row = grid.insertRow(inspos);
                for (int i = 0; i < 3; i++) {
                    row.getCell(i).setValue("Inserted " + String.valueOf(i) + " " + String.valueOf(inspos));
                    row.setBackground(new Color(23 * (inspos + 1)));
                    row.getCell(0).setBackground(Color.white);
                }
            }
        });
        container.add(insertRow);
        PushButton swapRows = new PushButton("SwapR Rows");

        swapRows.setTop(450);
        swapRows.getAnchors().setLeft(new Anchors.Anchor(1, 10, insertRow));

        swapRows.addClickHandler(new IButton.ClickHandler() {

            @Override
            public void onClick(IButton source) {
                if (grid.getRowCount()>2){
                    grid.swapRows(0, 1);
                }
                if (grid.getRowCount()>4){
                    grid.swapRows(3, 2);
                }
            }
        });
        container.add(swapRows);

        PushButton fixSize = new PushButton("Fix Last Column Width");

        fixSize.setTop(500);
        fixSize.setLeft(10);

        fixSize.addClickHandler(new IButton.ClickHandler() {

            @Override
            public void onClick(IButton source) {
                if (grid.getColumn(grid.getColumnCount() - 1).isSetFixedWidth()) {
                    grid.getColumn(grid.getColumnCount() - 1).unsetFixedWidth();
                } else {
                    grid.getColumn(grid.getColumnCount() - 1).setFixedWidth(15);
                }
            }
        });
        container.add(fixSize);
        
        PushButton stickLastColumn = new PushButton("Stick last column");

        stickLastColumn.setTop(500);
        stickLastColumn.getAnchors().setLeft(new Anchors.Anchor(1, 10, fixSize));

        stickLastColumn.addClickHandler(new IButton.ClickHandler() {

            @Override
            public void onClick(IButton source) {
                grid.setStickToRight(!grid.isStickToRight());
            }
        });
        container.add(stickLastColumn);
        
        PushButton rowHeaderVisibility = new PushButton("Row header visibility");

        rowHeaderVisibility.setTop(550);
        rowHeaderVisibility.setLeft(10);

        rowHeaderVisibility.addClickHandler(new IButton.ClickHandler() {
            @Override
            public void onClick(final IButton source) {
                grid.setRowHeaderVisible(!grid.isRowHeaderVisible());
            }
        });
        container.add(rowHeaderVisibility);
        
        
        PushButton rowHeaderTitles = new PushButton("Row Header titles");

        rowHeaderTitles.setTop(550);
        rowHeaderTitles.getAnchors().setLeft(new Anchors.Anchor(1, 10, rowHeaderVisibility));

        rowHeaderTitles.addClickHandler(new IButton.ClickHandler() {

            @Override
            public void onClick(IButton source) {
                grid.getCornerHeaderCell().setTitle("V");
                for (int r=0; r<grid.getRowCount(); r++){
                    grid.getRow(r).setTitle("row"+String.valueOf(r));
                }
            }
        });
        container.add(rowHeaderTitles);
        
//
//        Grid gridWithAutoWidthColumns = new Grid();
//        container.add(gridWithAutoWidthColumns);
//        gridWithAutoWidthColumns.setTop(400);
//        gridWithAutoWidthColumns.setWidth(300);
//        gridWithAutoWidthColumns.setHeight(300);
//        Grid.Column c = gridWithAutoWidthColumns.addColumn("One");
//        c = gridWithAutoWidthColumns.addColumn("Two");
//
//        c = gridWithAutoWidthColumns.addColumn("Three");
//        c.setFixedWidth(15);
//        for (int i = 0; i < 20; i++) {
//            Row row = gridWithAutoWidthColumns.addRow();
//            row.getCell(0).setValue(i);
//            row.getCell(1).setValue(i * 2);
//            row.getCell(2).setValue(i * 3);
//        }
//        Tree tree = new Tree();
//        container.add(tree);
//        tree.setTop(400);
//        tree.setLeft(400);
//        tree.setWidth(300);
//        tree.setHeight(300);
//        Tree.Column tc = tree.addColumn("One");
//        tc = tree.addColumn("Two");
//        tc = tree.addColumn("Three");

//        Node.DefaultNode root = new Node.DefaultNode("Root");
//        Node.DefaultNode parent = root;
//        for (int i = 0; i < 50; i++) {
//            Node.DefaultNode child = new Node.DefaultNode("Child#" + i + " of " + parent.getDisplayName());
//            child.setCellValue(0, "value for 0");
//            child.setCellValue(1, "value for 1");
//            child.setCellValue(2, "value for 2");
//            parent.add(child);
//            if (i % 3 == 0) {
//                parent = child;
//            }
//        }
//        tree.setRootNode(root);
//        tree.setRootVisible(true);
//        root.expand();
    }

    @Override
    protected IDialogDisplayer getDialogDisplayer() {
        return env.getDialogDisplayer();
    }

    @Override
    public IMainView getExplorerView() {
        return null;
    }

    @Override
    public void closeExplorerView() {
    }
}
