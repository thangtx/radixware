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

package org.radixware.kernel.explorer.editors.monitoring.tree;

import com.trolltech.qt.gui.QIcon;
import java.util.List;


public class ChildItemDecoration {

    private final String id;
    private final String nodeTitle;
    private final QIcon nodeIcon;
    private final List<MonitoringTreeItemDecoration> stateDecorations;
    private final List<MonitoringTreeItemDecoration> otherDecorations;
    private final String nodeTooltip;

    public ChildItemDecoration(String id, String nodeTitle, QIcon nodeIcon, String nodeTooltip, List<MonitoringTreeItemDecoration> stateDecorations, List<MonitoringTreeItemDecoration> otherDecorations) {
        this.nodeTitle = nodeTitle;
        this.nodeIcon = nodeIcon;
        this.stateDecorations = stateDecorations;
        this.otherDecorations = otherDecorations;
        this.nodeTooltip = nodeTooltip;
        this.id = id;
    }

    public String getNodeTitle() {
        return nodeTitle;
    }

    public QIcon getNodeIcon() {
        return nodeIcon;
    }

    public String getId() {
        return id;
    }

    public List<MonitoringTreeItemDecoration> getStateDecorations() {
        return stateDecorations;
    }

    public List<MonitoringTreeItemDecoration> getOtherDecorations() {
        return otherDecorations;
    }

    public String getNodeTooltip() {
        return nodeTooltip;
    }
}
