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
package org.radixware.kernel.common.dialogs.chooseobject;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.EventListenerList;

/**
 *
 * @author dlastochkin
 */
abstract class BaseSelectableObjectsPanel extends JPanel {

    protected List<SelectableObjectDelegate> objects = null;

    protected SelectableObjectsTable selectableObjectsTable;

    protected EventListenerList actionSupport = new EventListenerList();

    public BaseSelectableObjectsPanel(Collection<ISelectableObject> objects) {
        this.objects = new ArrayList<>();
        if (objects != null) {
            for (ISelectableObject object : objects) {
                this.objects.add(new SelectableObjectDelegate(object));
            }
            sortObjects();
        }
    }

    public void update() {
        this.removeAll();
        this.initComponents();
        this.updateUI();
    }

    abstract protected void initComponents();

    protected JScrollPane initSelectableObjectsTable() {
        selectableObjectsTable = new SelectableObjectsTable(objects);

        JScrollPane result = new JScrollPane(selectableObjectsTable);

        selectableObjectsTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                selectableObjectsTableMouseReleased(evt);
            }
        });

        return result;
    }

    private void selectableObjectsTableMouseReleased(java.awt.event.MouseEvent evt) {
        if (evt.getClickCount() == 2 && !selectableObjectsTable.getSelected().isEmpty()) {
            for (ActionListener actionListener : actionSupport.getListeners(ActionListener.class)) {
                actionListener.actionPerformed(null);
            }
        }
    }

    public void addActionListener(ActionListener listener) {
        actionSupport.add(ActionListener.class, listener);
    }

    public void removeActionListener(ActionListener listener) {
        actionSupport.remove(ActionListener.class, listener);
    }

    public void addObjects(Collection<ISelectableObject> objects) {
        for (ISelectableObject object : objects) {
            this.objects.add(new SelectableObjectDelegate(object));
            selectableObjectsTable.addRow(new SelectableObjectDelegate(object));
        }
        sortObjects();
    }

    public void removeSelectedObjects() {
        objects.removeAll(selectableObjectsTable.getSelected());
    }

    public List<ISelectableObject> getSelectedObjects() {
        List<ISelectableObject> result = new ArrayList<>();
        for (SelectableObjectDelegate object : selectableObjectsTable.getSelected()) {
            result.add(object.getObject());
        }
        return result;
    }

    public void addObject(ISelectableObject object) {
        objects.add(new SelectableObjectDelegate(object));        
        selectableObjectsTable.addRow(new SelectableObjectDelegate(object));
        sortObjects();
    }

    public void removeSelectedObject() {
        objects.remove(selectableObjectsTable.getSelected().get(0));
    }

    public ISelectableObject getSelectedObject() {
        return selectableObjectsTable.getSelected().get(0).getObject();
    }

    public Collection<ISelectableObject> getAllObjects() {
        List<ISelectableObject> result = new ArrayList<>();
        for (SelectableObjectDelegate object : objects) {
            result.add(object.getObject());
        }

        return result;
    }

    public void removeAllObjects() {
        objects.clear();
    }

    private void sortObjects() {
        Collections.sort(this.objects, new Comparator<SelectableObjectDelegate>() {
            @Override
            public int compare(SelectableObjectDelegate o1, SelectableObjectDelegate o2) {
                String str1 = o1.getTitle() + o1.getLocation();
                String str2 = o2.getTitle() + o2.getLocation();
                
                return str1.compareTo(str2);
            }
        });
    }
}
