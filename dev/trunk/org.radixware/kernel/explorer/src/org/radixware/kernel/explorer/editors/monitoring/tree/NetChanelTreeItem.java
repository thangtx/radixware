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
import org.radixware.kernel.common.client.models.Model;
import org.radixware.kernel.common.enums.EEventContextType;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.editors.monitoring.UnitsWidget;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.kernel.explorer.env.ExplorerIcon;

public class NetChanelTreeItem extends TreeItem {

    private final Boolean isListener;

    public NetChanelTreeItem(UnitsWidget.IdsGetter idsGetter, final MetricInfoGetter.ChannelInfo channelInfo, final Model groupModel, QTreeWidgetItem parent) {
        super(idsGetter, channelInfo, groupModel, parent);
        this.isListener = channelInfo.isListener();
        calcItemText(channelInfo); 
    }

    @Override
    public boolean operator_less(QTreeWidgetItem qtwi) {
        if (qtwi instanceof TreeItem) {
            if(((UnitNetPortHandlerTreeItem) parent()).isSortByName()) {
                TreeItem otherItem = (TreeItem) qtwi;
                if(getMetricInfo() != null && otherItem.getMetricInfo() != null) {
                    return getPureTitle(this).compareToIgnoreCase(getPureTitle(otherItem)) > 0;
                }
            }
        }
        return super.operator_less(qtwi);
    }
    
    private String getPureTitle(TreeItem item) {
        return item.getMetricInfo().getPureTitle() == null ? "" : item.getMetricInfo().getPureTitle();
    }

    @Override
    public EEventContextType getContextType() {
        return EEventContextType.NET_CHANNEL;
    }

    private void calcItemText(final MetricInfoGetter.ChannelInfo channelInfo) {
        MetricData md = new MetricData(MetricData.Type.TEXT);
        md.addIcon(channelInfo.getIcon());
        this.setData(0, ItemDataRole.UserRole, md);
        this.setText(0, getTitle());
        String toolTip = isListener ? Application.translate("SystemMonitoring", "Net listener")
                : Application.translate("SystemMonitoring", "Net client");
        this.setToolTip(0, toolTip);
    }

    @Override
    public void updateMetricDate(MetricInfoGetter metricInfo) {
        MetricInfoGetter.ChannelInfo channelInfo = metricInfo.getChannelInfo(getId());
        if (channelInfo != null) {
            MetricData md = (MetricData) this.data(1, ItemDataRole.UserRole);
            if (md == null) {
                md = createMetricData(channelInfo);
            }
            //if (isListener) {
            Long curSessionCnt = channelInfo.getCurSessionCnt();
            Long maxSessionCnt = channelInfo.getMaxSessionCnt();
            if (isListener && maxSessionCnt > 1) {
                //(Long) metricGetter.getPropValue(getEntityModel(), maxSessionCnt);
                if (curSessionCnt != null) {
                    md.setVal(curSessionCnt.doubleValue() / maxSessionCnt * 100);
                }
            } else {
                    //boolean writeConnectState=channelInfo.isWriteConnectState();
                //if(writeConnectState){
                //   boolean isConnected=channelInfo.isConnected();//(Boolean)getEntityModel().getProperty(connectPropId).getValueObject(); 
                Double val = curSessionCnt.doubleValue();
                setItemConnectText(md, val);
                //}
            }
            updateText(1);
        }
    }

    private MetricData createMetricData(MetricInfoGetter.ChannelInfo channelInfo) {
        MetricData md = new MetricData();
        //if (isListener) {    
        Long val = channelInfo.getCurSessionCnt();
        Long connectCnt = channelInfo.getMaxSessionCnt();
        if (val != null && connectCnt > 1) {
            md.setType(MetricData.Type.DIAGRAM);
            if (connectCnt != null && connectCnt != 0) {
                val = val / connectCnt;
            }
            md.setVal(val.doubleValue());
            this.setData(1, ItemDataRole.UserRole, md);
            md.addIcon(channelInfo.getIcon());
            String toolTip = Application.translate("SystemMonitoring", "The relation between the number of server connections and the maximum number of connections");//"Количество серверных соединений по отношению к максимальному количеству коннектов"
            this.setToolTip(1, toolTip);
        } else {
            md.setType(MetricData.Type.TEXT);
            setItemConnectText(md, val == null ? null : val.doubleValue());
        }
        return md;
    }

    private void setItemConnectText(MetricData md, Double val) {
        if (val != null && val == 1) {
            QIcon stateIcon = ExplorerIcon.getQIcon(ExplorerIcon.Connection.CONNECT);
            md.getIconList().clear();
            md.addIcon(stateIcon);
            String text = Application.translate("SystemMonitoring", "Connect");
            this.setData(1, ItemDataRole.UserRole, md);
            this.setText(1, text);
        } else {
            if (val != null) {
                QIcon stateIcon = ExplorerIcon.getQIcon(ExplorerIcon.Connection.DISCONNECT);
                md.getIconList().clear();
                md.addIcon(stateIcon);
                String text = Application.translate("SystemMonitoring", "Disconnect");
                this.setText(1, text);
            }
            this.setData(1, ItemDataRole.UserRole, md);
        }
    }

    @Override
    protected QIcon getStateIcon() {
        return null; //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected Id getTableId() {
        return idsGetter.getChannelTableId();
    }
}
