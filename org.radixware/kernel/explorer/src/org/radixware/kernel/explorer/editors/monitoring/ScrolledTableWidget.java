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

package org.radixware.kernel.explorer.editors.monitoring;

import com.trolltech.qt.gui.QHeaderView.ResizeMode;
import com.trolltech.qt.gui.QTableWidget;
import com.trolltech.qt.gui.QWidget;
import java.util.ArrayList;
import java.util.List;
import org.radixware.kernel.common.client.exceptions.BrokenEntityObjectException;
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.client.models.GroupModel;
import org.radixware.kernel.common.exceptions.ServiceClientException;


public class ScrolledTableWidget extends QTableWidget {
        private final UnitsWidget unitsWidget;
        private int entityIndex;
        private GroupModel sourceGroup;
        //private final Id raisetimeId;
        //private final Id severityId;
       // private final Id msgId;
        private final int columnCnt=3;
        private int rowLoadCount=50;

        public ScrolledTableWidget(final QWidget parent,final UnitsWidget unitsWidget, final GroupModel sourceGroup, boolean isContext) {
            super(parent);
            this.unitsWidget=unitsWidget;
            this.sourceGroup = sourceGroup;
            /*entityIndex = 0;
            IdsGetter idsGetter=unitsWidget.getIdsGetter();
            if (isContext) {
                raisetimeId = idsGetter.getPropId(UnitsWidget.EPropertyName.EVENTLOG_RAISETIME_CNTX);
                severityId = idsGetter.getPropId(UnitsWidget.EPropertyName.EVENTLOG_SEVERIRY_CNTX);
                msgId = idsGetter.getPropId(UnitsWidget.EPropertyName.EVENTLOG_MESSAGE_CNTX);
            } else {
                raisetimeId = idsGetter.getPropId(UnitsWidget.EPropertyName.EVENTLOG_RAISETIME);
                severityId = idsGetter.getPropId(UnitsWidget.EPropertyName.EVENTLOG_SEVERIRY);
                msgId = idsGetter.getPropId(UnitsWidget.EPropertyName.EVENTLOG_MESSAGE);
            }
            createTableUi();
            this.verticalScrollBar().maximum();*/
        }
        
        public GroupModel  getSourceGroup(){
            return sourceGroup;
        }
        
        public int getRowCount(){
            return entityIndex;
        }
        
        public  void update(final GroupModel sourceGroup) throws InterruptedException{            
            //int curItemRow=0;
            //if(this.currentItem()!=null){
            //    curItemRow=this.currentItem().row();
            //}
            if(!this.signalsBlocked()){
                try{                 
                   setUpdatesEnabled(false);
                   this.blockSignals(true);

                   clearContents();
                   setRowCount(0);
                   this.sourceGroup = sourceGroup;
                   int rowCount=entityIndex<=0 ? rowLoadCount : entityIndex;
                   entityIndex = 0; 

                   fillTable(rowCount);

                }finally{
                   this.blockSignals(false);
                   //if(curItemRow<this.rowCount()){
                   //    this.setCurrentCell(curItemRow, 0);
                   //    this.scrollToItem(this.currentItem());
                   //}
                   setUpdatesEnabled(true);               
                }
            }
        }

        private void createTableUi() {
            try {
                this.setSelectionBehavior(SelectionBehavior.SelectRows);
                this.setVerticalHeader(null);
                this.verticalScrollBar().valueChanged.connect(this, "scrolled(int)");
                fillTable(rowLoadCount);
            } catch (InterruptedException ex) {
            }
        }

        private  int fillTable(int rowCount) throws InterruptedException {
            try{
                if ((sourceGroup != null) && (entityIndex >= 0)) {
                    rowCount = rowCount + entityIndex;
                    while (entityIndex < rowCount) {
                        EntityModel entity;                    
                        try{
                            entity = sourceGroup.getEntity(entityIndex);
                        }
                        catch(BrokenEntityObjectException exception){
                            entityIndex++;
                            continue;//ignoring broken entity model;
                        }
                        if (entity != null) {
                            if (entityIndex == 0) {
                                this.setColumnCount(columnCnt);
                                List<String> columnName = new ArrayList<>();
                                //columnName.add( ClientValueFormatter.capitalizeIfNecessary(sourceGroup.getEnvironment(),EventLogPanel.getPropTitle(entity, raisetimeId)));
                                //columnName.add(EventLogPanel.getPropTitle(entity, severityId));
                                //columnName.add(EventLogPanel.getPropTitle(entity, msgId));
                                this.setHorizontalHeaderLabels(columnName);
                            }
                            //Timestamp dateTime = (Timestamp) EventLogPanel.getPropValue(entity, raisetimeId);
                           // EEventSeverity severity = (EEventSeverity) EventLogPanel.getPropValue(entity, severityId);
                            //String msg = (String) EventLogPanel.getPropValue(entity, msgId);
                            this.insertRow(entityIndex);
                            //this.setItem(entityIndex, 0, new QTableWidgetItem(dateTime.toString()));
                            //this.setItem(entityIndex, 1, new QTableWidgetItem(severity.getName()));
                           // this.setItem(entityIndex, 2, new QTableWidgetItem(msg));
                            entityIndex++;
                        } else {
                            entityIndex = -1;
                            break;
                        }
                    }
                }
            } catch (ServiceClientException ex) {
                sourceGroup.showException(ex);
            }
            this.resizeColumnsToContents();
            this.horizontalHeader().setResizeMode(2, ResizeMode.Stretch);
            return entityIndex;
        }

        private void loadMoreRows( int index) { 
            int curIndex = currentRow();
            try {
                this.blockSignals(true);                
                fillTable(rowLoadCount);                
            } catch (InterruptedException ex) {               
            }finally{
                this.blockSignals(false);
                if ((curIndex > 0) && (index < rowCount())) {
                    setCurrentCell(curIndex, 0);
                }
            }
        }

        @SuppressWarnings("unused")
        private void scrolled(int min) {
           if ((this.verticalScrollBar().maximum() > 0) && (min >= this.verticalScrollBar().maximum()) && (entityIndex >= 0)) {
              //QApplication.postEvent(unitsWidget.getUnitsTree(), new ScrollEventLogEvent(this));
           }
        }
        
        public void scroll(){          
               //unitsWidget.getEventTimer().stop();                
               loadMoreRows( entityIndex);
               //unitsWidget.getEventTimer().start();
        }
}
