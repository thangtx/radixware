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

package org.radixware.kernel.designer.debugger.impl.ui;

import java.util.LinkedList;
import java.util.List;
import org.netbeans.spi.viewmodel.UnknownTypeException;
import org.netbeans.spi.debugger.ui.Constants;
import org.netbeans.spi.viewmodel.ModelListener;
import org.netbeans.spi.viewmodel.NodeModel;
import org.netbeans.spi.viewmodel.NodeModelFilter;
import org.netbeans.spi.viewmodel.TableModel;


public abstract class AbstractTableModel implements NodeModelFilter, TableModel, Constants {

    private final List<ModelListener> listeners = new LinkedList<ModelListener>();

    @Override
    public String getDisplayName(NodeModel original, Object node) throws UnknownTypeException {
        return original.getDisplayName(node);
    }

    @Override
    public String getIconBase(NodeModel original, Object node) throws UnknownTypeException {
        return original.getIconBase(node);
    }

    @Override
    public String getShortDescription(NodeModel original, Object node) throws UnknownTypeException {
        return original.getShortDescription(node);
    }

    @Override
    public boolean isReadOnly(Object node, String columnID) throws UnknownTypeException {
        return true;
    }

    @Override
    public void setValueAt(Object node, String columnID, Object value) throws UnknownTypeException {
    }

    @Override
    public void addModelListener(ModelListener l) {
        synchronized (listeners) {
            if (!listeners.contains(l)) {
                listeners.add(l);
            }
        }
    }

    @Override
    public void removeModelListener(ModelListener l) {
        synchronized (listeners) {
            listeners.remove(l);
        }
    }
}
