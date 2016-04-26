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


package org.radixware.kernel.designer.ads.editors.refactoring.components;

import java.awt.Image;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.resources.RadixWareIcons;
import org.radixware.kernel.designer.ads.editors.refactoring.OperationStatus;
import static org.radixware.kernel.designer.ads.editors.refactoring.OperationStatus.EEventType.ERROR;
import static org.radixware.kernel.designer.ads.editors.refactoring.OperationStatus.EEventType.WARNING;


public class RefactoringStatusPanel extends CardPanel {

    private static final String STATUS_CARD = "Status";

    private static class EventTypeNode extends TreeNode<OperationStatus.EEventType> {

        public EventTypeNode(Children children, OperationStatus.EEventType type) {
            super(children, type);
        }

        @Override
        public Image getIcon(int type) {
            switch (getObject()) {
                case ERROR:
                    return RadixWareIcons.EVENT_LOG.ERROR.getImage();
                case WARNING:
                    return RadixWareIcons.EVENT_LOG.WARNING.getImage();
            }
            
            return RadixWareIcons.EVENT_LOG.NONE.getImage();
        }

        @Override
        public String getDisplayName() {
            switch (getObject()) {
                case ERROR:
                    return "Errors";
                case WARNING:
                    return "Warnings";
            }
            return "Info";
        }
    }

    private static class EventNode extends TreeNode<OperationStatus.Event> {

        private OperationStatus.Event object;

        public EventNode(Children children, OperationStatus.Event object) {
            super(children, object);
            this.object = object;
        }

        @Override
        public Image getIcon(int type) {
            return null;
        }

        @Override
        public String getDisplayName() {
            return object.getMessage();
        }
    }

    private static class EventChildren extends Children.Array {

        private static Collection<Node> createNodes(Collection<OperationStatus.Event> events) {
            final List<Node> nodes = new ArrayList<>();

            for (final OperationStatus.Event event : events) {
                nodes.add(new EventNode(Children.LEAF, event));
            }

            return nodes;
        }

        public EventChildren(Collection<OperationStatus.Event> events) {
            super(createNodes(events));
        }
    }

    private static class RootChildren extends Children.Array {

        private static Collection<Node> createNodes(OperationStatus status) {
            final List<Node> nodes = new ArrayList<>();
            final Set<OperationStatus.Event> errors = status.getEvents(OperationStatus.EEventType.ERROR);

            if (!errors.isEmpty()) {
                nodes.add(new EventTypeNode(new EventChildren(errors), OperationStatus.EEventType.ERROR));
            }

            final Set<OperationStatus.Event> warnings = status.getEvents(OperationStatus.EEventType.WARNING);
            if (!warnings.isEmpty()) {
                nodes.add(new EventTypeNode(new EventChildren(warnings), OperationStatus.EEventType.WARNING));
            }

            return nodes;
        }

        public RootChildren(OperationStatus status) {
            super(createNodes(status));
        }
    }
    private final TreePanel statusTreePanel = new TreePanel();
   
    public RefactoringStatusPanel() {
        add(statusTreePanel, STATUS_CARD);
    }

    public void open(OperationStatus status) {

        if (!status.isEmpty()) {
            statusTreePanel.open(createRootNode(status));
            showCard(STATUS_CARD);
        } else {
            setStatus(NbBundle.getMessage(RefactoringStatusPanel.class, "RefactoringStatusPanel.lblStatus.NoErrors"));
        }
    }

    private Node createRootNode(OperationStatus status) {
        return new TreeNode<>(new RootChildren(status), null);
    }
}
