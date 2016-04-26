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

package org.radixware.kernel.designer.common.dialogs.utils;

import java.awt.Color;
import java.awt.Dimension;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import net.miginfocom.swing.MigLayout;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.openide.DialogDescriptor;
import org.openide.util.RequestProcessor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.utils.RadixObjectsUtils;
import org.radixware.kernel.common.components.TreeView;


class MovePanel extends JPanel {

    private final MoveAnalyzer analizer;
    private List<IMoveTransformation> transformations = null;
    private final TreeView treeView = new TreeView();
    private final JLabel progressHandleLabel;
    private final JComponent progressHandleComponent;
    private DialogDescriptor dialogDescriptor = null;

    private class MoveAnalyzer extends Move implements Runnable {

        private final ProgressHandle progressHandle;

        public MoveAnalyzer(List<RadixObject> movedObjects, RadixObject destination, ProgressHandle progressHandle) {
            super(movedObjects, destination);
            this.progressHandle = progressHandle;

        }

        @Override
        public void run() {
            MovePanel.this.transformations = calcTransformations(progressHandle);

            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    update();
                }
            });
        }
    }
    private static final String ANALIZE_DEPENDENCIES = "Analize Dependencies";

    public MovePanel(List<RadixObject> movedObjects, RadixObject destination) {
        this.setLayout(new MigLayout("fill, nogrid, hidemode 1"));
        this.setPreferredSize(new Dimension(400, 200));

        final JLabel topLabel = new JLabel("Required refactoring and causes:");
        this.add(topLabel, "width max, wrap");

        this.add(treeView, "width max, height max, wrap");

        final ProgressHandle progressHandle = ProgressHandleFactory.createHandle(ANALIZE_DEPENDENCIES);

        progressHandleLabel = new JLabel(ANALIZE_DEPENDENCIES);
        this.add(progressHandleLabel, "width max, wrap");

        progressHandleComponent = ProgressHandleFactory.createProgressComponent(progressHandle);
        this.add(progressHandleComponent, "width max, wrap");

        this.analizer = new MoveAnalyzer(movedObjects, destination, progressHandle);
        RequestProcessor.getDefault().post(analizer, 220); // 220 - from Netbeans sources, otherwise dialog will wait searching before displaying.
    }

    private void update() {
        progressHandleComponent.setVisible(false);
        progressHandleLabel.setVisible(false);

        final Map<String, TreeView.Node> displayName2Node = new HashMap<String, TreeView.Node>();
        for (IMoveTransformation transformation : transformations) {
            final String displayName = transformation.getDisplayName();
            TreeView.Node node = displayName2Node.get(displayName);
            if (node == null) {
                node = new TreeView.Node(transformation);
                node.setDisplayName(displayName);
                node.setColor(transformation.isPossible() ? Color.BLUE : Color.RED);
                node.setIcon(transformation.getIcon().getIcon());
                treeView.getRootNode().add(node);
                displayName2Node.put(displayName, node);
                node.setExpanded(false);
            }

            final TreeView.Node causeNode = new TreeView.Node(transformation);
            causeNode.setDisplayName(transformation.getCause());
            node.add(causeNode);
        }

        if (transformations.isEmpty()) {
            final TreeView.Node emptyNode = new TreeView.Node(this);
            emptyNode.setDisplayName("No refactoring required.");
            treeView.getRootNode().add(emptyNode);
        }

        dialogDescriptor.setValid(true);
    }

    private void performTransformations() {
        for (IMoveTransformation transformation : transformations) {
            if (transformation.isPossible()) {
                transformation.perform();
            }
        }
    }

    private static class MovePanelModalDisplayer extends ModalDisplayer {

        private final MovePanel movePanel;

        public MovePanelModalDisplayer(MovePanel movePanel, String title) {
            super(movePanel, title);
            this.movePanel = movePanel;
        }

        @Override
        protected void apply() {
            movePanel.performTransformations();
        }

        @Override
        protected boolean canClose() {
            if (movePanel.transformations == null) {
                return false;
            }
            for (IMoveTransformation transformation : movePanel.transformations) {
                if (!transformation.isPossible()) {
                    return DialogUtils.messageConfirmation(
                            "Some changes can not be resolved automatically.\n"
                            + "Continue refactoring move?");
                }
            }
            return true;
        }
    }

    public static boolean showModal(List<RadixObject> movedObjects, RadixObject destination) {
        final MovePanel movePanel = new MovePanel(movedObjects, destination);
        final String title;

        if (movedObjects.size() == 1) {
            final RadixObject movedObject = movedObjects.get(0);
            title = "Move " + movedObject.getTypeTitle() + " '" + movedObject.getQualifiedName() + "'";
        } else {
            title = "Move " + movedObjects.size() + " " + RadixObjectsUtils.getCommonTypeTitle(movedObjects);
        }

        final ModalDisplayer modalDisplayer = new MovePanelModalDisplayer(movePanel, title);
        final DialogDescriptor dialogDescriptor = modalDisplayer.getDialogDescriptor();
        movePanel.dialogDescriptor = dialogDescriptor;
        dialogDescriptor.setValid(false);

        return modalDisplayer.showModal();
    }
}
