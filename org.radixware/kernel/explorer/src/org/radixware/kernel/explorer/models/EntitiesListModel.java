/*
* Copyright (c) 2008-2016, Compass Plus Limited. All rights reserved.
*
* This Source Code Form is subject to the terms of the Mozilla Public
* License, v. 2.0. If a copy of the MPL was not distributed with this
* file, You can obtain one at http://mozilla.org/MPL/2.0/.
* This Source Code is distributed WITHOUT ANY WARRANTY; including any 
* implied warranties but not limited to warranty of MERCHANTABILITY 
* or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
* License, v. 2.0. for more details.
*/

package org.radixware.kernel.explorer.models;

import com.trolltech.qt.core.QAbstractItemModel;
import com.trolltech.qt.core.QModelIndex;
import com.trolltech.qt.core.QObject;
import com.trolltech.qt.core.QTimerEvent;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QColor;
import com.trolltech.qt.gui.QFont;
import com.trolltech.qt.gui.QIcon;
import java.awt.Color;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.eas.IResponseListener;
import org.radixware.kernel.common.client.eas.RequestHandle;
import org.radixware.kernel.common.client.eas.SelectRequestHandle;
import org.radixware.kernel.common.client.exceptions.BrokenEntityObjectException;
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.client.models.GroupModel;
import org.radixware.kernel.common.client.models.IEntitySelectionController;
import org.radixware.kernel.common.client.types.Pid;
import org.radixware.kernel.common.client.types.Reference;
import org.radixware.kernel.common.client.utils.ValueConverter;
import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.explorer.text.ExplorerTextOptions;
import org.radixware.kernel.explorer.utils.WidgetUtils;

public class EntitiesListModel extends QAbstractItemModel{
    
    private final static Qt.ItemFlags ENTITY_MODEL_FLAGS = new Qt.ItemFlags(Qt.ItemFlag.ItemIsEnabled, Qt.ItemFlag.ItemIsSelectable);
    private final static Qt.ItemFlags EMPTY_FLAGS = new Qt.ItemFlags();
    private final static Qt.Alignment LEFT_ALIGNMENT = new Qt.Alignment(Qt.AlignmentFlag.AlignLeft);
    private final static Qt.Alignment CENTER_ALIGNMENT = new Qt.Alignment(Qt.AlignmentFlag.AlignCenter);
    private final static int WAITING_FOR_FIRST_PAGE_VMARGIN = 4;
    private final static int SHOW_PLEASE_WAIT_MIN_TIME_MILLS = 300;
    
    private final GroupModel groupModel;
    private final List<EntityModel> entities = new ArrayList<>();    
    private final IResponseListener asyncResponseListener = new IResponseListener() {

        @Override
        public void registerRequestHandle(final RequestHandle handle) {            
        }

        @Override
        public void unregisterRequestHandle(final RequestHandle handle) {            
        }

        @Override
        public void onResponseReceived(final XmlObject response, final RequestHandle handle) {
            EntitiesListModel.this.onAsyncLoad();
        }

        @Override
        public void onServiceClientException(final ServiceClientException exception, final RequestHandle handle) {
            EntitiesListModel.this.onAsyncException(exception);
        }

        @Override
        public void onRequestCancelled(XmlObject request, RequestHandle handler) {            
            EntitiesListModel.this.onAsyncCancelled();
        }
    };
    private ExplorerTextOptions textOptions = ExplorerTextOptions.Factory.getOptions(Color.black, Color.white);
    private SelectRequestHandle asyncRequestHandle;
    private int showPleaseWaitTimer;
    private String pleaseWaitTitle;
    private final String errorTitle;
    private boolean loadingBlocked;
    private boolean hasMoreRows;
    private boolean asyncMode;
    private boolean readingInProgress;
    private boolean wasException;    
    
    public EntitiesListModel(final GroupModel model, final QObject parent, final boolean async){
        super(parent);
        this.groupModel = model;
        hasMoreRows = model.hasMoreRows();
        asyncMode = async;
        pleaseWaitTitle = getEnvironment().getMessageProvider().translate("Wait Dialog", "Please Wait...");
        errorTitle = getEnvironment().getMessageProvider().translate("Selector", "Error on Receiving Data");
    }
    
    public EntitiesListModel(final GroupModel model, final QObject parent){
        this(model, parent, false);
    }

    @Override
    public final int columnCount(final QModelIndex parent) {
        return 1;
    }

    @Override
    public Object data(final QModelIndex index, final int role) {
        if (index == null) {
            return null;
        }
        switch (role) {
            case Qt.ItemDataRole.CheckStateRole:
                return getCheckState(index);
            case Qt.ItemDataRole.DecorationRole:
                return getDecoration(index);
            case Qt.ItemDataRole.DisplayRole:
                return getDisplay(index);
            case Qt.ItemDataRole.FontRole:
                return getFont(index);
            case Qt.ItemDataRole.TextAlignmentRole:
                final Qt.Alignment alignment = getTextAlignment(index);
                return alignment==null ? null : alignment.value();
            case Qt.ItemDataRole.UserRole:
                return getEntityModel(index);
            case Qt.ItemDataRole.BackgroundRole: {
                return getBackground(index);
            }
            case Qt.ItemDataRole.ForegroundRole: {
                return getForeground(index);
            }
            case Qt.ItemDataRole.ToolTipRole:{
                return getToolTip(index);
            }
            case WidgetUtils.MODEL_ITEM_ROW_NAME_DATA_ROLE:
            case WidgetUtils.MODEL_ITEM_CELL_NAME_DATA_ROLE:{
                return getRowName(index);
            }
            case WidgetUtils.MODEL_ITEM_CELL_VALUE_DATA_ROLE:{
                return getCellValueAsStr(index);
            }
            case WidgetUtils.MODEL_ITEM_CELL_VALUE_IS_NULL_DATA_ROLE:{
                return isCellValueNull(index);
            }            
            default:
                return null;
        }
    }

    @Override
    public final QModelIndex index(final int row, final int column, final QModelIndex parent) {
        if (parent!=null || row<0 || row>=rowCount(null) || column!=0){
            return null;
        }
        final EntityModel entityModel = getEntityModel(row);
        if (entityModel==null){
            return createIndex(row, column, row);
        }else{
            return createIndex(row, column, entityModel.getPid().hashCode());
        }
    }

    @Override
    public final QModelIndex parent(final QModelIndex child) {
        return null;
    }

    @Override
    public final int rowCount(final QModelIndex parent) {
        if (parent==null){
            if (isAsyncMode() && hasMoreRows){
                if (isEmpty()){
                    return WAITING_FOR_FIRST_PAGE_VMARGIN*2 + 1;
                }else{
                    return getLoadedEntitiesCount() + 1;
                }
            }
            return getLoadedEntitiesCount();
        }else{
            return 0;
        }
    }

    @Override
    public Qt.ItemFlags flags(final QModelIndex index) {
        return index==null || isWaitingItem(index.row()) ? EMPTY_FLAGS : ENTITY_MODEL_FLAGS;
    }

    @Override
    public void fetchMore(final QModelIndex parent) {
        if (parent==null || !isDataLoadingBlocked()){
            if (groupModel.getEntitiesCount()>getLoadedEntitiesCount() && updateEntitiesList()){
                return;
            }
            if (isAsyncMode()){                
                asyncRequestHandle = groupModel.readMoreAsync();
                asyncRequestHandle.addListener(asyncResponseListener);
                readingInProgress = true;
                if (getLoadedEntitiesCount()==0){
                    showPleaseWaitTimer = startTimer(SHOW_PLEASE_WAIT_MIN_TIME_MILLS);
                }
            }else{
                readingInProgress = true;
                try{
                    groupModel.getEntity(groupModel.getEntitiesCount());
                    updateEntitiesList();
                }catch(InterruptedException exception){
                    //ignore
                }catch(ServiceClientException exception){
                    wasException = true;
                    final String message = 
                        getEnvironment().getMessageProvider().translate("ExplorerException", "Error on receiving data");
                    getEnvironment().getTracer().error(message, exception);
                }catch(BrokenEntityObjectException exception){
                    updateEntitiesList();                    
                }finally{
                    readingInProgress = false;
                }                
            }
        }
    }

    @Override
    public final boolean canFetchMore(final QModelIndex parent) {
        return !readingInProgress && !wasException && hasMoreRows && parent==null && !isDataLoadingBlocked();
    }
    
    protected final boolean isWaitingItem(final int row){
        return row>=getLoadedEntitiesCount();
    }
    
    public final EntityModel getEntityModel(final QModelIndex index){
        return getEntityModel(index.row());
    }
    
    public final EntityModel getEntityModel(final int row){
        return row<entities.size() ? entities.get(row) : null;
    }
    
    protected final int getLoadedEntitiesCount(){
        return entities.size();
    }
    
    private void addEntity(final EntityModel entity){
        entities.add(entity);
    }
    
    private void clearEntities(){
        entities.clear();
    }
    
    protected final boolean isEmpty(){
        return getLoadedEntitiesCount()==0;
    }
    
    protected Qt.CheckState getCheckState(final QModelIndex index){
        return null;
    }

    protected QIcon getDecoration(final QModelIndex index){
        return null;
    }
    
    protected String getDisplay(final QModelIndex index){
        final EntityModel entityModel = getEntityModel(index);
        if (entityModel==null){
            if (waitingForFirstPage()){
                if (index.row()==WAITING_FOR_FIRST_PAGE_VMARGIN){
                    return firstPageError() ? errorTitle : getPleaseWaitTitle();
                }else{
                    return "";
                }
            }else{
                return inErrorMode() ? errorTitle : getPleaseWaitTitle();
            }
        }else{
            return entityModel.getTitle();
        }
    }
    
    private boolean waitingForFirstPage(){
        return isAsyncMode() && getLoadedEntitiesCount()==0 && hasMoreRows;
    }
    
    private boolean firstPageError(){
        return inErrorMode() && isEmpty();
    }
    
    protected final IClientEnvironment getEnvironment(){
        return groupModel.getEnvironment();
    }
    
    public ExplorerTextOptions getTextOptions(){
        return textOptions;
    }
    
    protected QFont getFont(final QModelIndex index){
        return getTextOptions().getQFont();
    }
    
    protected QColor getBackground(final QModelIndex index){
        return getTextOptions().getBackground();
    }
    
    protected QColor getForeground(final QModelIndex index){
        if (firstPageError() || (inErrorMode() && getEntityModel(index)==null)){
            return QColor.red;
        }else{
            return getTextOptions().getForeground();
        }
    }
    
    protected Qt.Alignment getTextAlignment(final QModelIndex index){
        return isWaitingItem(index.row()) ? CENTER_ALIGNMENT : LEFT_ALIGNMENT;
    }
    
    protected String getToolTip(final QModelIndex index){
        return "";
    }
    
    private String getRowName(final QModelIndex index){
        final EntityModel entity = getEntityModel(index);
        final Pid pid = entity==null ? null : entity.getPid();
        return pid==null ? null : pid.toStr();
    }
    
    private String getCellValueAsStr(final QModelIndex index){
        final EntityModel entity = getEntityModel(index);
        final Reference reference = entity==null ? null : new Reference(entity);
        final ValAsStr valAsStr = ValueConverter.obj2ValAsStr(reference, EValType.PARENT_REF);
        return valAsStr==null ? null : valAsStr.toString();
    }
    
    private Boolean isCellValueNull(final QModelIndex index){
        return getEntityModel(index)==null;
    }
    
    public final void setTextOptions(ExplorerTextOptions options){
        if (options!=null && !options.equals(textOptions)){
            textOptions = options;
            notifyAllRowsChanged();
        }
    }

    public boolean isAsyncMode() {
        return asyncMode;
    }

    public void setAsyncMode(final boolean isAsync) {
        if (isAsync!=asyncMode){            
            final int rowsCount = rowCount(null);
            if (isAsync){
                beginInsertRows(null, rowsCount, rowsCount);
            }else{
                beginRemoveRows(null, rowsCount-1, rowsCount-1);
            }            
            this.asyncMode = isAsync;
            if (isAsync){
                endInsertRows();
            }else{
                endRemoveRows();
            }
        }
    }

    public final String getPleaseWaitTitle() {
        return pleaseWaitTitle;
    }

    public final void setPleaseWaitTitle(final String pleaseWaitTitle) {
        if (!Objects.equals(pleaseWaitTitle, this.pleaseWaitTitle)){
            this.pleaseWaitTitle = pleaseWaitTitle;
            if (isAsyncMode()){
                if (waitingForFirstPage()){
                    notifyAllRowsChanged();
                }else if (!isEmpty()){
                    notifyLastRowChanged();
                }
            }
        }
    }        
    
    private void notifyAllRowsChanged(){
        final int rowCount = rowCount(null);
        if (rowCount>0){
            final QModelIndex firstIndex = index(0, 0, null);
            final QModelIndex lastIndex = index(rowCount-1, 0, null);
            dataChanged.emit(firstIndex, lastIndex);
        }
    }
    
    private void notifyLastRowChanged(){
        final int rowCount = rowCount(null);
        if (rowCount>0){
            final QModelIndex lastIndex = index(rowCount-1, 0, null);
            dataChanged.emit(lastIndex, lastIndex);
        }
    }
    
    private boolean updateEntitiesList(){
        final int oldCount = getLoadedEntitiesCount();
        final int lastEntityIndex;
        if (oldCount==0){
            lastEntityIndex = -1;
        }else{
            lastEntityIndex = groupModel.findEntityByPid(getEntityModel(oldCount-1).getPid());
        }
        final List<EntityModel> newEntities = new LinkedList<>();
        final IEntitySelectionController selectionController = groupModel.getEntitySelectionController();
        for (int i=lastEntityIndex+1, count=groupModel.getEntitiesCount(); i<count; i++){
            try{
                final EntityModel entityModel = groupModel.getEntity(i);
                if (selectionController!=null && selectionController.isEntityChoosable(entityModel)){
                    newEntities.add(entityModel);
                }
            }catch(InterruptedException | ServiceClientException exception){
                //do not expect this exceptions here
                getEnvironment().getTracer().error(exception);
            }catch(BrokenEntityObjectException exception){
                //ignoring broken entity
            }
        }
        if (newEntities.isEmpty()){
            if (groupModel.hasMoreRows()){
                fetchMore(null);
            }else if (isAsyncMode()){
                if (oldCount==0 && rowCount()>0){
                    beginRemoveRows(null, 0, rowCount()-1);
                    hasMoreRows = false;
                    endRemoveRows();
                }else if (rowCount()>oldCount){
                    beginRemoveRows(null, oldCount, rowCount()-1);
                    hasMoreRows = false;
                    endRemoveRows();
                }
            }
            return false;
        }else{
            final int delta;
            if (isAsyncMode()){
                if (oldCount==0){
                    delta = newEntities.size() - rowCount() +  (groupModel.hasMoreRows() ? 1 : 0);
                }else{
                    delta = newEntities.size() - (hasMoreRows && !groupModel.hasMoreRows() ? 1 : 0);
                }
            }else{
                delta = newEntities.size();
            }
            if (delta>0){
                beginInsertRows(null, oldCount, oldCount+delta-1);
            }else if (delta<0){
                beginRemoveRows(null, rowCount()+delta, rowCount()-1);
            }
            hasMoreRows = groupModel.hasMoreRows();
            for (EntityModel entityModel: newEntities){
                addEntity(entityModel);
            }
            if (delta>0){
                endInsertRows();
            }else if (delta<0){
                endRemoveRows();
            }else{
                notifyLastRowChanged();
            }
            return true;
        }
    }        

    private void onAsyncLoad() {
        if (showPleaseWaitTimer==0){
            updateEntitiesList();
            readingInProgress = false; 
        }
        asyncRequestHandle = null;
    }
    
    private void onAsyncException(final ServiceClientException exception) {        
        if (showPleaseWaitTimer!=0){
            killTimer(showPleaseWaitTimer);
            showPleaseWaitTimer = 0;
        }
        wasException = true;
        if (isEmpty()){            
            notifyAllRowsChanged();
        }else{
            notifyLastRowChanged();
        }
        asyncRequestHandle = null;
    }
    
    private void onAsyncCancelled(){
        if (showPleaseWaitTimer!=0){
            killTimer(showPleaseWaitTimer);
            showPleaseWaitTimer = 0;
        }
        asyncRequestHandle = null;
    }

    @Override
    protected void timerEvent(final QTimerEvent timerEvent) {
        if (timerEvent.timerId()==showPleaseWaitTimer){
            killTimer(showPleaseWaitTimer);
            showPleaseWaitTimer = 0;
            if (asyncRequestHandle==null && readingInProgress){
                updateEntitiesList();
                readingInProgress = false; 
            }
        }
        super.timerEvent(timerEvent);
    }    
    
    public final void reload(){
        if (asyncRequestHandle!=null){
            asyncRequestHandle.cancel();
            asyncRequestHandle = null;
        }
        readingInProgress = false;
        wasException = false;
        hasMoreRows = true;
        clearEntities();        
        groupModel.reset();
        reset();
    }        
    
    public final void setDataLoadingBlocked(final boolean isBlocked){
        loadingBlocked = isBlocked;
    }
    
    public boolean isDataLoadingBlocked(){
        return loadingBlocked;
    }
    
    public final void cancelAsyncDataLoading(){
        if (asyncRequestHandle!=null){
            asyncRequestHandle.cancel();
            asyncRequestHandle = null;
            readingInProgress = false;
        }
    }
    
    public final boolean inErrorMode(){
        return wasException;
    }
}