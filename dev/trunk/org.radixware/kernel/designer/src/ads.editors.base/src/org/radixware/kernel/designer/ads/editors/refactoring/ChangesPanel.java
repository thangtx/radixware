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


package org.radixware.kernel.designer.ads.editors.refactoring;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JPanel;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.radixware.kernel.designer.ads.editors.refactoring.components.TreeNode;
import org.radixware.kernel.designer.ads.editors.refactoring.components.TreePanel;


public class ChangesPanel extends JPanel {

    public static class ResultNode extends TreeNode<RefactoringResult> {

        public ResultNode(Children children, RefactoringResult object) {
            super(children, object);
        }

        @Override
        public String getDisplayName() {
            return getObject().getDescription();
        }
    }

    public static class ChangeNode extends TreeNode<RefactoringResult.Change> {

        private class OpenChangeAction extends AbstractAction {

            @Override
            public void actionPerformed(ActionEvent e) {
                getObject().open();
            }
        }

        public ChangeNode(Children children, RefactoringResult.Change object) {
            super(children, object);
        }

        @Override
        public String getDisplayName() {

            return getObject().getDescription();
        }

        @Override
        public Action getPreferredAction() {
            return new OpenChangeAction();
        }

        @Override
        public Image getIcon(int type) {
            return getObject().getIcon();
        }
    }

    public static class ResultChildren extends Children.Array {

        private static Collection<Node> createNodes(RefactoringResult result) {
            final List<Node> nodes = new ArrayList<>();

            for (final RefactoringResult.Change change : result.getChanges()) {
                nodes.add(new ChangeNode(Children.LEAF, change));
            }

            return nodes;
        }

        public ResultChildren(RefactoringResult result) {
            super(createNodes(result));
        }
    }
    private final TreePanel treePanel = new TreePanel();

    public ChangesPanel() {

        setLayout(new BorderLayout());
        add(treePanel, BorderLayout.CENTER);
        treePanel.setRootVisible(true);
    }

    public void open(RefactoringResult result) {
        treePanel.open(createRootNode(result));
    }

    private Node createRootNode(RefactoringResult result) {
        return new ResultNode(new ResultChildren(result), result);
    }
}
