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
import org.radixware.kernel.common.repository.dds.DdsUpdateInfo;
import org.radixware.kernel.common.repository.dds.DdsUpdateInfo.BaseLayerInfo;


public class BaseLayerInfoList extends JList {

    public interface EditingBaseLayerChangeListener {

        public void editingBaseLayerChanged(BaseLayerInfo baseLayer);
    }

    private final class BaseLayerModel {

        BaseLayerInfo baseLayer;
        boolean red;

        BaseLayerModel(BaseLayerInfo baseLayer, boolean red) {
            this.baseLayer = baseLayer;
            this.red = red;
        }
    }

    private class BaseLayerInfoListModel extends AbstractListModel {

        private DdsUpdateInfo updateInfo = null;
        private final ArrayList<BaseLayerModel> layerModels = new ArrayList<BaseLayerModel>();

        public BaseLayerInfoListModel() {
        }

        public void setDdsUpdateInfo(DdsUpdateInfo updateInfo) {
            this.updateInfo = updateInfo;
            update();
        }

        private void update() {
            int sz = getSize();
            layerModels.clear();
            fireIntervalRemoved(this, 0, Math.max(sz - 1, 0));
            if (updateInfo != null) {
                ArrayList<BaseLayerInfo> arr = new ArrayList<BaseLayerInfo>();
                arr.addAll(updateInfo.getBaseLayersInfo().list());
//                RadixObjectsUtils.sortByName(arr);
                for (BaseLayerInfo info : arr) {
                    if (info.findBaseLayer() == null) {
                        layerModels.add(new BaseLayerModel(info, true));
                    } else {
                        layerModels.add(new BaseLayerModel(info, false));
                    }
                }
                fireIntervalAdded(this, 0, Math.max(getSize() - 1, 0));
            }
        }

        @Override
        public int getSize() {
            return layerModels.size();
        }

        @Override
        public Object getElementAt(int index) {
            if (index >= getSize() || index < 0) {
                return null;
            }
            return layerModels.get(index);
        }

        public void updateRow(int row) {
            fireContentsChanged(this, row, row);
        }

        public int getIndexOfBaseLayerModel(BaseLayerModel model) {
            return layerModels.indexOf(model);
        }

        public void updateData() {
            update();
        }
    }

    private class BaseLayerInfoListCellRenderer extends DefaultListCellRenderer {

        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            JComponent pattern = (JComponent) super.getListCellRendererComponent(list, "NULL", index, isSelected, cellHasFocus);
            if (value == null) {
                return pattern;
            }
            BaseLayerModel layerModel = (BaseLayerModel) value;
            JLabel label = new JLabel(layerModel.baseLayer.getUri());
            label.setIcon(layerModel.baseLayer.getIcon().getIcon());
            label.setOpaque(true);
            label.setBackground(pattern.getBackground());
            if (layerModel.red) {
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
    private final BaseLayerInfoListModel model;
    private final ArrayList<EditingBaseLayerChangeListener> listeners = new ArrayList<EditingBaseLayerChangeListener>();

    public BaseLayerInfoList() {
        super();
        this.setModel(model = new BaseLayerInfoListModel());
        this.setCellRenderer(new BaseLayerInfoListCellRenderer());
        this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                fireEditingBaseLayerInfoChanged();
            }
        });
    }

    public void setDdsUpdateInfo(DdsUpdateInfo updateInfo) {
        this.clearSelection();
        model.setDdsUpdateInfo(updateInfo);
    }

    public void updateRow(int row) {
        model.updateRow(row);
    }

    public int getCurrentBaseLayerInfoIdx() {
        return this.getSelectedIndex();
    }

    public void fireEditingBaseLayerInfoChanged() {
        BaseLayerInfo layerInfo = getSelectedBaseLayerInfo();
        for (EditingBaseLayerChangeListener listener : listeners) {
            listener.editingBaseLayerChanged(layerInfo);
        }
    }

    public BaseLayerInfo getSelectedBaseLayerInfo() {
        int idx = getCurrentBaseLayerInfoIdx();
        if (idx != -1) {
            return ((BaseLayerModel) model.getElementAt(idx)).baseLayer;
        }
        return null;
    }

    public boolean canRemoveSelectedBaseLayerInfo() {
        int idx = getCurrentBaseLayerInfoIdx();
        if (idx != -1) {
            return ((BaseLayerModel) model.getElementAt(idx)).red;
        }
        return false;
    }

    public void update() {
        model.updateData();
    }

    public void addEditingBaseLayerChangeListener(EditingBaseLayerChangeListener listener) {
        listeners.add(listener);
    }

    public void removeEditingBaseLayerChangeListener(EditingBaseLayerChangeListener listener) {
        listeners.remove(listener);
    }
}
