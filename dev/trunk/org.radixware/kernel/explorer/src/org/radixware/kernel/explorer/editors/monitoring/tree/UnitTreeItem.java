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

import com.trolltech.qt.core.Qt.ItemDataRole;
import com.trolltech.qt.gui.QIcon;
import com.trolltech.qt.gui.QTreeWidgetItem;
import java.text.DecimalFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.common.client.models.Model;
import org.radixware.kernel.common.enums.EEventContextType;
import org.radixware.kernel.common.enums.EUnitType;
import org.radixware.kernel.common.exceptions.NoConstItemWithSuchValueError;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.editors.monitoring.UnitsWidget;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.kernel.explorer.env.ExplorerIcon;


public class UnitTreeItem extends TreeItem {

    private final Long itemType;
    private volatile Long orderHint = Long.MAX_VALUE;
    private EUnitState state;
    private Boolean isInstanceStarted;
   
    @Override
    public EEventContextType getContextType() {
        return EEventContextType.SYSTEM_UNIT;
    }

    public static class Factory {

        private Factory() {
        }

        public static UnitTreeItem newInstance(UnitsWidget.IdsGetter idsGetter, final MetricInfoGetter.UnitInfo metricInfo, final boolean isInstanceStarted, final Model groupModel, QTreeWidgetItem parent) {

            if (metricInfo.getUnitType().equals(EUnitType.ARTE.getValue())) {
                return new UnitArteTreeItem(idsGetter, metricInfo, isInstanceStarted, groupModel, parent);
            }
            if (metricInfo.getUnitType().equals(EUnitType.JOB_EXECUTOR.getValue())) {
                return new UnitJobExecTreeItem(idsGetter, metricInfo, isInstanceStarted, groupModel, parent);
            }
            if (metricInfo.getUnitType().equals(EUnitType.NET_PORT_HANDLER.getValue())) {
                return new UnitNetPortHandlerTreeItem(idsGetter, metricInfo, isInstanceStarted, groupModel, parent);
            }
            return new UnitTreeItem(idsGetter, metricInfo, isInstanceStarted, groupModel, parent);
        }
    }

   @Override
    public boolean operator_less(QTreeWidgetItem qtwi) {
        if (qtwi instanceof UnitTreeItem) {
            UnitTreeItem unitTreeItem = (UnitTreeItem) qtwi;
            if (itemType != null && unitTreeItem.getType() != null) {
                int res = itemType.compareTo(unitTreeItem.getType());
                return res == 0 ? getId() > unitTreeItem.getId() : res > 0;
            }
        }
        return super.operator_less(qtwi);
    }

    public static enum EUnitState {

        NOT_USED, STARTED, STOPPED, INSTANCE_NOT_STARTED, HANG
    }

    protected UnitTreeItem(UnitsWidget.IdsGetter idsGetter, final MetricInfoGetter.UnitInfo metricInfo, Boolean isInstanceStarted, final Model groupModel, QTreeWidgetItem parent) {
        super(idsGetter, metricInfo, groupModel, parent);
        this.itemType = metricInfo.getUnitType();
        this.isInstanceStarted = isInstanceStarted;
    }
    
    

    private EUnitState calcState(boolean isStarted, boolean isUsed, boolean isInstanceStarted) {
        if (!isUsed) {
            return EUnitState.NOT_USED;
        } else if (!isInstanceStarted) {
            return EUnitState.INSTANCE_NOT_STARTED;
        } else if (isStarted) {
            return EUnitState.STARTED;
        } else if (!isStarted) {
            return EUnitState.STOPPED;
        }
        return EUnitState.HANG;
    }

    public Long getOrderHint() {
        return orderHint;
    }

    public void setOrderHint(Long orderHint) {
        this.orderHint = orderHint == null ? Long.MAX_VALUE : orderHint;
    }
    
    public EUnitState getState() {
        return state;
    }

    @Override
    public void updateMetricDate(MetricInfoGetter metricInfo) {
        MetricInfoGetter.UnitInfo unitInfo = metricInfo.getUnitInfo(getId());
        if (unitInfo != null) {
            MetricData md = (MetricData) this.data(0, ItemDataRole.UserRole);
            Boolean isStarted = unitInfo.isStarted();
            Boolean isUsed = unitInfo.isUse();
            EUnitState newState = calcState(isStarted, isUsed, isInstanceStarted);
            if (md == null) {
                this.state = newState;
                md = new MetricData(MetricData.Type.TEXT);
                md.addIcon(getStateIcon());
                md.addIcon(unitInfo.getIcon());
                this.setData(0, ItemDataRole.UserRole, md);
                this.setText(0, getTitle());
                this.setToolTip(0, getToolTip());
            } else {
                if (parent() instanceof InstanceTreeItem) {
                    isInstanceStarted = ((InstanceTreeItem) parent()).isStarted();
                } else {
                    throw new IllegalStateException();
                }
                if (newState != state) {
                    state = newState;
                    md.getIconList().remove(0);
                    md.addIcon(0, getStateIcon());
                    updateText(0);
                }
            }
        }
    }

    private String getToolTip() {
        String strUnitType = "";
        try {
            strUnitType = EUnitType.getForValue(Long.valueOf(itemType)).getName();
        } catch (NoConstItemWithSuchValueError ex) {
            Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
        }

        String toolTip = Application.translate("SystemMonitoring", "Unit Type") + ": " + strUnitType + " \n";
        toolTip += Application.translate("SystemMonitoring", "State") + ": ";

        if (state == EUnitState.NOT_USED) {
            toolTip += Application.translate("SystemMonitoring", "Not used");
        } else if (state == EUnitState.STARTED) {
            toolTip += Application.translate("SystemMonitoring", "Started");
        } else if (state == EUnitState.STOPPED) {
            toolTip += Application.translate("SystemMonitoring", "Stopped");
        } else if (state == EUnitState.INSTANCE_NOT_STARTED) {
            toolTip += Application.translate("SystemMonitoring", "Instance not started");
        } else {
            toolTip += Application.translate("SystemMonitoring", "Hang");
        }
        return toolTip;
    }

    protected MetricData createDiagrammMetricDate(Long maxVal) {
        MetricData md = new MetricData(MetricData.Type.DIAGRAM);
        Double val = md.getVal();
        if (val != null && maxVal != 0) {
            val = val / maxVal * 100;
            md.setVal(val);
            DecimalFormat f = new DecimalFormat("0.0");
            md.setText(f.format(val) + "%");
        }
        return md;
    }

    @Override
    protected QIcon getStateIcon() {
        if (state == EUnitState.NOT_USED) {
            return ExplorerIcon.getQIcon(UnitsWidget.UnitsWidgetIcons.NOT_USED);
        }
        if (state == EUnitState.STARTED) {
            return ExplorerIcon.getQIcon(UnitsWidget.UnitsWidgetIcons.STARTED);
        }
        if (state == EUnitState.STOPPED) {
            return ExplorerIcon.getQIcon(UnitsWidget.UnitsWidgetIcons.STOPPED);
        }
        if (state == EUnitState.INSTANCE_NOT_STARTED) {
            return ExplorerIcon.getQIcon(UnitsWidget.UnitsWidgetIcons.INSTANCE_NOT_STARTED);
        }
        if (state == EUnitState.HANG) {
            return ExplorerIcon.getQIcon(UnitsWidget.UnitsWidgetIcons.HANG);
        }
        return null;
    }

    public Long getType() {
        return itemType;
    }

    public boolean isDiagramm() {
        return itemType.equals(EUnitType.ARTE.getValue())
                || itemType.equals(EUnitType.JOB_EXECUTOR.getValue());
    }

    @Override
    protected Id getTableId() {
        return idsGetter.getUnitTableId();
    }
}
