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


public class MonitoringTreeItemDecoration {

    private final QIcon icon;
    private final String text;
    private final String toolTip;
    private final long index;
    private final ChildItemDecoration childItemDecoration;
    
    public MonitoringTreeItemDecoration(QIcon icon, String text) {
        this(icon, text, null, 0, null);
    }

    public MonitoringTreeItemDecoration(QIcon icon, String text, long index) {
        this(icon, text, null, index, null);
    }
    
    public MonitoringTreeItemDecoration(ChildItemDecoration child, long index) {
        this(null, null, null, index, child);
    }

    private MonitoringTreeItemDecoration(QIcon icon, String text, String toolTip, long index, ChildItemDecoration childItemDecoration) {
        this.icon = icon;
        this.text = text;
        this.toolTip = toolTip;
        this.childItemDecoration = childItemDecoration;
        this.index = index;
    }
    
    public QIcon getIcon() {
        return icon;
    }

    public String getText() {
        return text;
    }
    
    public String getToolTip() {
        return toolTip;
    }
    
    public Long getIndex() {
        return index;
    }

    public ChildItemDecoration getChildItemDecoration() {
        return childItemDecoration;
    }
    
}
