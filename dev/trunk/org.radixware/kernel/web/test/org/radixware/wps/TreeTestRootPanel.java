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
import org.radixware.kernel.common.client.meta.mask.EditMaskNum;
import org.radixware.kernel.common.client.widgets.IButton;
import org.radixware.wps.WebServer;
import org.radixware.wps.WpsEnvironment;
import org.radixware.wps.dialogs.IDialogDisplayer;
import org.radixware.wps.rwt.*;
import org.radixware.wps.rwt.tree.Node;
import org.radixware.wps.rwt.tree.Tree;
import org.radixware.wps.text.WpsTextOptions;


public class TreeTestRootPanel extends RootPanel {

    private WpsEnvironment env;

    public TreeTestRootPanel(WpsEnvironment env) {
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
        final Container container = new Container();
        container.setVCoverage(100);
        container.setHCoverage(100);
        add(container);

        final Tree grid = new Tree();
        System.out.println("\ngrid = "+grid);
        grid.setPersistenceKey("test_grid");
        grid.setWidth(1000);
        grid.setHeight(300);
        grid.setTop(10);
        grid.setLeft(10);
        container.add(grid);



        grid.addColumn("One").setPersistenceKey("cone");
        grid.addColumn("Two").setPersistenceKey("ctwo");
        grid.addColumn("Tree").setPersistenceKey("cthree");

        //  grid.getColumn(1).setInitialWidth(400);

        grid.getColumn(0).setInitialWidth(150);
        grid.getColumn(1).setInitialWidth(50);
        grid.getColumn(3).setFixedWidth(15);
        
        double max = 200;
                double min = 0;
                byte precision = Byte.MIN_VALUE;
                long scale = 1;
                Character del = null;
                final EditMaskNum mask = new EditMaskNum(BigDecimal.valueOf(min), BigDecimal.valueOf(max), scale, del, precision);
        PushButton autoFill = new PushButton("Auto Fill");
        autoFill.addClickHandler(new IButton.ClickHandler() {

            @Override
            public void onClick(IButton source) {
                final WpsTextOptions options = WpsTextOptions.getDefault(env).changeBackgroundColor(Color.GRAY);
                Node.DefaultNode parent = null;
                for (int i = 0; i < 20; i++) {
                    Node.DefaultNode node = new Node.DefaultNode();
                    node.setCellValue(0, "One - " + String.valueOf(i));

                    node.setCellValue(1, "Two - " + String.valueOf(i));
                    node.setCellValue(2, "Three - " + String.valueOf(i));
//                    if (i % 2 == 0) {
//                        node.setBackground(Color.red);
//                    }
//                    if (i % 3 == 0) {
//                        node.setBackground(Color.blue);
//                    }
//                    if (i % 2 == 1) {
//                        node.setBackground(Color.green);
//                    }
                    node.setTextOptions(0,options);
                    node.expand();
                    if (parent == null) {
                        grid.setRootNode(node);
                        parent = node;
                    } else {
                        parent.add(node);
                        if (i % 2 == 0) {
                            parent = node;
                        } else if (i % 3 == 0) {
                            if (parent.getParent() != null) {
                                parent = (Node.DefaultNode) parent.getParent();
                            }
                        }
                    }
                }
                
                System.out.println("!!!! mask setter = "+mask);
                //grid.getColumn(1).getEditingOptions().setEditMask(mask);
            }
        });


        autoFill.setTop(350);
        autoFill.setLeft(10);
        container.add(autoFill);

        PushButton hideFirstColumn = new PushButton("Hide first column");

        hideFirstColumn.setTop(350);
        hideFirstColumn.getAnchors().setLeft(new Anchors.Anchor(1, 10, autoFill));

        hideFirstColumn.addClickHandler(new IButton.ClickHandler() {

            @Override
            public void onClick(IButton source) {
                grid.getColumn(0).setVisible(!grid.getColumn(0).isVisible());
            }
        });
        container.add(hideFirstColumn);

        PushButton hideSecondColumn = new PushButton("Hide second column");

        hideSecondColumn.setTop(350);
        hideSecondColumn.getAnchors().setLeft(new Anchors.Anchor(1, 10, hideFirstColumn));


        hideSecondColumn.addClickHandler(new IButton.ClickHandler() {

            @Override
            public void onClick(IButton source) {
                grid.getColumn(1).setVisible(!grid.getColumn(1).isVisible());
            }
        });
        container.add(hideSecondColumn);


        PushButton hideLastColumn = new PushButton("Hide last column");

        hideLastColumn.setTop(350);
        hideLastColumn.getAnchors().setLeft(new Anchors.Anchor(1, 10, hideSecondColumn));

        hideLastColumn.addClickHandler(new IButton.ClickHandler() {

            @Override
            public void onClick(IButton source) {
                grid.getColumn(2).setVisible(!grid.getColumn(2).isVisible());
            }
        });
        container.add(hideLastColumn);


//        PushButton stickLastColumn = new PushButton("Stick last column");
//
//        stickLastColumn.setTop(350);
//        stickLastColumn.getAnchors().setLeft(new Anchors.Anchor(1, 10, hideLastColumn));
//
//        stickLastColumn.addClickHandler(new IButton.ClickHandler() {
//
//            @Override
//            public void onClick(IButton source) {
//                grid.setStickToRight(!grid.isStickToRight());
//            }
//        });
//        container.add(stickLastColumn);
//
//        PushButton addRow = new PushButton("Add row");
//
//        addRow.setTop(350);
//        addRow.getAnchors().setLeft(new Anchors.Anchor(1, 10, stickLastColumn));
//
//        final int[] addedRows = new int[]{1};
//        addRow.addClickHandler(new IButton.ClickHandler() {
//
//            @Override
//            public void onClick(IButton source) {
//                Grid.Row row = grid.addRow();
//                for (int i = 0; i < 3; i++) {
//                    row.getCell(i).setValue("Added " + String.valueOf(i) + " " + String.valueOf(addedRows[0]));
//                    addedRows[0]++;
//                }
//            }
//        });
//        container.add(addRow);

//        PushButton insertRow = new PushButton("Insert row");
//
//        insertRow.setTop(350);
//        insertRow.getAnchors().setLeft(new Anchors.Anchor(1, 10, addRow));
//
//        final int[] insertedRows = new int[]{1};
//        final List<Integer> inserted = new LinkedList<>();
//        insertRow.addClickHandler(new IButton.ClickHandler() {
//
//            @Override
//            public void onClick(IButton source) {
//                int inspos = insertedRows[0] + 1;
//                inserted.add(inspos);
//                Grid.Row row = grid.insertRow(inspos);
//                for (int i = 0; i < 3; i++) {
//                    row.getCell(i).setValue("Inserted " + String.valueOf(i) + " " + String.valueOf(insertedRows[0]));
//                    row.setBackground(new Color(23 * (insertedRows[i] + 1)));
//                    row.getCell(0).setBackground(Color.white);
//                    insertedRows[0] += 2;
//                }
//            }
//        });
//        container.add(insertRow);
//        PushButton swapRows = new PushButton("SwapR Rows");
//
//        swapRows.setTop(350);
//        swapRows.getAnchors().setLeft(new Anchors.Anchor(1, 10, insertRow));
//
//        swapRows.addClickHandler(new IButton.ClickHandler() {
//
//            @Override
//            public void onClick(IButton source) {
//                if (inserted.size() >= 2) {
//                    grid.swapRows(inserted.get(0), inserted.get(1));
//                }
//            }
//        });
//        container.add(swapRows);

        PushButton fixSize = new PushButton("Fix Last Column Width");

        fixSize.setTop(350);
        fixSize.getAnchors().setLeft(new Anchors.Anchor(1, 10, hideLastColumn));

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

//        PushButton clearRows = new PushButton("Clear Rows");
//
//        clearRows.setTop(350);
//        clearRows.getAnchors().setLeft(new Anchors.Anchor(1, 10, fixSize));
//        clearRows.addClickHandler(new IButton.ClickHandler() {
//
//            @Override
//            public void onClick(IButton source) {
//                grid.clearRows();
//            }
//        });
//        container.add(clearRows);


    }
}
