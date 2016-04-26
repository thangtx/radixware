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

package org.radixware.kernel.designer.dds.editors.scripts;

import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import javax.swing.AbstractListModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.radixware.kernel.common.repository.dds.DdsScripts;
import org.radixware.kernel.common.repository.dds.DdsUpdateInfo;


public class UpdateInfoList extends JList {

    public interface EditingUpdateInfoChangeListener {

        public void editingUpdateInfoChanged(DdsUpdateInfo updateInfo);
    }

    private final class UpdateInfoModel {

        DdsUpdateInfo updateInfo;
        boolean red;

        UpdateInfoModel(DdsUpdateInfo updateInfo, boolean red) {
            this.updateInfo = updateInfo;
            this.red = red;
        }
    }

    private class UpdateInfoListModel extends AbstractListModel {

        private final DdsScripts scripts;
        private final ArrayList<UpdateInfoModel> updateInfos = new ArrayList<UpdateInfoModel>();

        public UpdateInfoListModel(DdsScripts scripts) {
            this.scripts = scripts;
            update();
        }

        private void update() {
            updateInfos.clear();
            for (DdsUpdateInfo updateInfo : scripts.getUpdatesInfo().list()) {
                if (updateInfo.findScript() == null) {
                    updateInfos.add(new UpdateInfoModel(updateInfo, true));
                } else {
                    updateInfos.add(new UpdateInfoModel(updateInfo, false));
                }
            }
//            RadixObjectsUtils.sortByName(updateInfos);
        }

        @Override
        public int getSize() {
            return updateInfos.size();
        }

        @Override
        public Object getElementAt(int index) {
            if (index >= getSize() || index < 0) {
                return null;
            }
            return updateInfos.get(index);
        }

        public void updateRow(int row) {
            fireContentsChanged(this, row, row);
        }

        public void updateData() {
            update();
            fireContentsChanged(this, 0, getSize() - 1);
        }
    }

    private class UpdateInfoListCellRenderer extends DefaultListCellRenderer {

        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            JComponent pattern = (JComponent) super.getListCellRendererComponent(list, "NULL", index, isSelected, cellHasFocus);
            if (value == null) {
                return pattern;
            }
            UpdateInfoModel updateInfo = (UpdateInfoModel) value;
            JLabel label = new JLabel(updateInfo.updateInfo.getUpdateFileName());
            label.setIcon(updateInfo.updateInfo.getIcon().getIcon());
            label.setOpaque(true);
            label.setBackground(pattern.getBackground());
            if (updateInfo.red) {
                label.setForeground(Color.RED);
            } else {
                label.setForeground(pattern.getForeground());
            }
            if (cellHasFocus) {
                label.setBorder(pattern.getBorder());
            }
            return label;
        }
    }
    private final UpdateInfoListModel model;
    private final ArrayList<EditingUpdateInfoChangeListener> listeners = new ArrayList<EditingUpdateInfoChangeListener>();

    public UpdateInfoList(DdsScripts scripts) {
        super();
        this.setModel(model = new UpdateInfoListModel(scripts));
        this.setCellRenderer(new UpdateInfoListCellRenderer());
        this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                fireEditingUpdateInfoChanged();
            }
        });
    }

    public void updateRow(int row) {
        model.updateRow(row);
    }

    public int getCurrentUpdateInfoIdx() {
        return this.getSelectedIndex();
    }

    public void fireEditingUpdateInfoChanged() {
        DdsUpdateInfo updateInfo = getSelectedUpdateInfo();
        for (EditingUpdateInfoChangeListener listener : listeners) {
            listener.editingUpdateInfoChanged(updateInfo);
        }
    }

    public DdsUpdateInfo getSelectedUpdateInfo() {
        int idx = getCurrentUpdateInfoIdx();
        if (idx != -1) {
            return ((UpdateInfoModel) model.getElementAt(idx)).updateInfo;
        }
        return null;
    }

    public void update() {
        model.updateData();
    }

    public void addEditingUpdateInfoChangeListener(EditingUpdateInfoChangeListener listener) {
        listeners.add(listener);
    }

    public void removeEditingUpdateInfoChangeListener(EditingUpdateInfoChangeListener listener) {
        listeners.remove(listener);
    }
}
