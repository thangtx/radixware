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
package org.radixware.kernel.common.client.models;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.SettingNames;
import org.radixware.kernel.common.client.exceptions.BrokenEntityObjectException;
import org.radixware.kernel.common.client.exceptions.FileException;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.models.items.SelectorColumnModelItem;
import org.radixware.kernel.common.client.types.Pid;
import org.radixware.kernel.common.client.types.PropertyValuesWriteOptions;
import org.radixware.kernel.common.client.views.IDialog;
import org.radixware.kernel.common.client.views.IProgressHandle;
import org.radixware.kernel.common.client.widgets.IWidget;
import org.radixware.kernel.common.client.widgets.selector.ISelectorDataExportOptionsDialog;
import org.radixware.kernel.common.client.widgets.selector.ISelectorWidgetDelegate;
import org.radixware.kernel.common.client.widgets.selector.SelectorModelDataLoader;
import org.radixware.kernel.common.enums.ESelectionMode;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.types.Id;

public abstract class GroupModelWriter {

    protected final static class ExportGroupModel extends ProxyGroupModel {

        private int keepEntitiesCount;
        private boolean keepHasMoreRows;
        private int keepReadPageSize;

        public ExportGroupModel(final GroupModel groupModel) {
            super(groupModel);
        }

        @Override
        public int mapEntityIndexToSource(int index) {
            return index;
        }

        @Override
        public int mapEntityIndexFromSource(int index) {
            return index;
        }

        @Override
        public void invalidate() {
        }

        public void keepState() {
            keepEntitiesCount = getSourceGroupModel().getEntitiesCount();
            keepHasMoreRows = getSourceGroupModel().hasMoreRows();
            keepReadPageSize = getSourceGroupModel().getReadPageSize();
        }

        public void restoreState() {
            final int count = getEntitiesCount();
            if (count > keepEntitiesCount) {
                for (int row = count - 1; row > keepEntitiesCount - 1; row--) {
                    removeRow(row);
                }
            }
            if (keepHasMoreRows) {
                getSourceGroupModel().setHasMoreRows(true);
            }
            getSourceGroupModel().setReadPageSize(keepReadPageSize);
        }

        @Override
        protected void readMore() throws ServiceClientException, InterruptedException {
            getSourceGroupModel().readMore();
        }

        @Override
        public int getEntitiesCount() {
            return getSourceGroupModel().getEntitiesCount();
        }

        @Override
        public void removeRow(final int row) {
            getSourceGroupModel().removeRow(row);
        }

        @Override
        public int findEntityByPid(final Pid pid) {
            return getSourceGroupModel().findEntityByPid(pid);
        }

    }

    protected final static class SelectorDataExportDelegate implements ISelectorWidgetDelegate {

        private final GroupModel group;

        public SelectorDataExportDelegate(final GroupModel groupModel) {
            group = groupModel;
        }

        @Override
        public int rowCount() {
            return group.getEntitiesCount();
        }

        @Override
        public boolean readMore() throws ServiceClientException, InterruptedException {
            try {
                return group.getEntity(group.getEntitiesCount()) != null;
            } catch (BrokenEntityObjectException exception) {
                //ignore
            }
            return true;
        }

        @Override
        public boolean canReadMore() {
            return group.hasMoreRows();
        }

        @Override
        public GroupModel getChildGroup() {
            return group;
        }

        @Override
        public void updateRowsCount(final GroupModel g) {
            throw new UnsupportedOperationException("Not supported.");
        }

        @Override
        public void increaseRowsLimit() {
            throw new UnsupportedOperationException("Not supported.");
        }

    }

    final protected GroupModel groupModel;
    private PropertyValuesWriteOptions options;
    enum WriteResult{SUCCESS, TRYAGAIN, ERROR};

    private final static String ROWS_LIMIT_FOR_NAVIGATION_CONFIG_PATH =
            SettingNames.SYSTEM + "/" + SettingNames.SELECTOR_GROUP + "/" + SettingNames.Selector.COMMON_GROUP + "/" + SettingNames.Selector.Common.ROWS_LIMIT_FOR_NAVIGATION;
    
    public GroupModelWriter(final GroupModel groupModel) {
        this.groupModel = groupModel;
    }
    
    public File write(final IWidget parentWidget){
        boolean needToContinue = true;
        while (needToContinue) {
            final ISelectorDataExportOptionsDialog dialog = createExportOptionsDialog();
            if (dialog.execDialog()==IDialog.DialogResult.ACCEPTED){
                WriteResult result = writeInternal(dialog);
                if (result == WriteResult.SUCCESS) {
                    needToContinue = false;
                    return dialog.getFile();
                } else if (result == WriteResult.ERROR) {
                    //write something
                    return null;
                }
            }else{
                return null;
            }
        }
        return null;
    }
    
    private WriteResult writeInternal(ISelectorDataExportOptionsDialog dialog) {
        boolean needToDeleteFile = false;
        final IClientEnvironment environment = groupModel.getEnvironment();
        final MessageProvider messageProvider = environment.getMessageProvider();
        final String exceptionTitle = messageProvider.translate("Selector", "Failed to Export Objects");
        try {
            final File file = dialog.getFile();
            options = dialog.getPropertyValuesWriteOptions();
            options.setGroupModelTitle(groupModel.getTitle());
            options.setGroupModelWindowTitle(groupModel.getWindowTitle());
            final List<SelectorColumnModelItem> columns = new LinkedList<>();
            if (dialog.exportColumnTitles()) {
                for (Id columnId : options.getColumnsToExport()) {
                    columns.add(groupModel.getSelectorColumn(columnId));
                }
            }
            final EntityObjectsWriter writer = createEntityObjectsWriter(file, dialog, columns);
            final EntityObjectsSelection selection = groupModel.getSelection().getNormalized();
            final IProgressHandle progressHandle = environment.getProgressHandleManager().newStandardProgressHandle();
            final ExportGroupModel proxyGroup = new ExportGroupModel(groupModel);
            proxyGroup.keepState();
            groupModel.setReadPageSize(200);
            try {
                progressHandle.setTitle(messageProvider.translate("Selector", "Exporting Objects"));
                if (selection.isEmpty()){
                    progressHandle.startProgress(messageProvider.translate("Selector", "Exporting Objects..."), true);
                }else{
                    progressHandle.startProgress(messageProvider.translate("Selector", "Exporting Selected Objects..."), true);
                }
                final String progressMessageTemplate =
                        messageProvider.translate("Selector", "Exporting Objects...\nNumber of Exported Objects: %1s");
                int numberOfExportedObjects = 0;
                final int maxRows = dialog.getMaxRows();
                if (selection.getSelectionMode()==ESelectionMode.INCLUSION){
                    final Collection<Pid> selectedObjects = selection.getSelectedObjects();
                    for (Pid selectedObject: selectedObjects){
                        final int row = proxyGroup.findEntityByPid(selectedObject);
                        if (row>-1){                                
                            final EntityModel entity;
                            try{
                                entity = proxyGroup.getEntity(row);
                                try{
                                    if (!writer.writeEntityObject(entity)) {
                                        needToDeleteFile = true;
                                        return WriteResult.TRYAGAIN;
                                    }
                                    if (numberOfExportedObjects % 30 == 0) {
                                        writer.flush();
                                    }
                                }catch(IOException exception){
                                    groupModel.showException(exceptionTitle, new FileException(environment, FileException.EExceptionCode.CANT_WRITE, file.getAbsolutePath()));
                                    return WriteResult.ERROR;
                                }
                                numberOfExportedObjects++;
                                if (maxRows>0 && numberOfExportedObjects>=maxRows){
                                    break;
                                }
                                progressHandle.setText(String.format(progressMessageTemplate, String.valueOf(numberOfExportedObjects)));
                            }catch(BrokenEntityObjectException exception){
                            }catch(InterruptedException exception){
                                break;
                            }catch(ServiceClientException exception){
                                environment.getTracer().error(exception);
                                return WriteResult.ERROR;
                            }
                        }
                    }
                }else{
                    final String confirmMovingToExportMessage =
                            messageProvider.translate("Selector", "Number of loaded objects is %1s.\nDo you want to load next %2s objects?");
                    final SelectorModelDataLoader dataLoader = new SelectorModelDataLoader(environment);

                    dataLoader.setConfirmationMessageText(confirmMovingToExportMessage);
                    final int rowsLoadingLimit = environment.getConfigStore().readInteger(ROWS_LIMIT_FOR_NAVIGATION_CONFIG_PATH, 1000);
                    dataLoader.setLoadingLimit(maxRows > 0 ? -1 : rowsLoadingLimit);
                    dataLoader.resetCounters();
                    final ISelectorWidgetDelegate swDelegate = new SelectorDataExportDelegate(proxyGroup);
                    try {
                        int row = 0;
                        do {
                            for (int count = swDelegate.rowCount(); row < count && (row < maxRows || maxRows < 0) && !progressHandle.wasCanceled(); row++) {
                                final EntityModel entity;
                                try{
                                    entity = proxyGroup.getEntity(row);
                                }catch(BrokenEntityObjectException exception){
                                    continue;
                                }
                                if (!selection.isEmpty() && !selection.isObjectSelected(entity)){
                                    continue;
                                }
                                try {
                                    if (!writer.writeEntityObject(entity)) {
                                        needToDeleteFile = true;
                                        return WriteResult.TRYAGAIN;
                                    } 
                                    if (row % 30 == 0) {
                                        writer.flush();
                                    }
                                } catch (IOException exception) {
                                    groupModel.showException(exceptionTitle, new FileException(environment, FileException.EExceptionCode.CANT_WRITE, file.getAbsolutePath()));
                                    return WriteResult.ERROR;
                                }
                                numberOfExportedObjects++;
                            }
                            progressHandle.setText(String.format(progressMessageTemplate, String.valueOf(row)));
                        } while (!progressHandle.wasCanceled() && (row < maxRows || maxRows < 0) && dataLoader.loadMore(swDelegate));
                    } catch (ServiceClientException exception) {
                        groupModel.showException(exceptionTitle, exception);
                        return WriteResult.ERROR;
                    } catch (InterruptedException exception) {
                        groupModel.showException(exceptionTitle, exception);
                    }
                }
            } finally {
                progressHandle.finishProgress();
                proxyGroup.restoreState();
                try {
                    writer.close();
                    if (needToDeleteFile) {
                        file.delete();
                    }
                } catch (IOException exception) {
                    groupModel.showException(exceptionTitle, new FileException(environment, FileException.EExceptionCode.CANT_WRITE, file.getAbsolutePath()));
                    return WriteResult.ERROR;
                } catch (Exception ex) {
                    groupModel.showException(exceptionTitle, new FileException(environment, FileException.EExceptionCode.CANT_WRITE, file.getAbsolutePath()));
                }
            }
            return WriteResult.SUCCESS;
        }   catch (FileNotFoundException | UnsupportedEncodingException | InvalidFormatException ex) {
            groupModel.getEnvironment().messageException(exceptionTitle, exceptionTitle, ex);
            return WriteResult.ERROR;
        }
    }
    
    public PropertyValuesWriteOptions getOptions() {
        return options;
    }

    protected abstract ISelectorDataExportOptionsDialog createExportOptionsDialog();

    protected abstract EntityObjectsWriter createEntityObjectsWriter(final File file, final ISelectorDataExportOptionsDialog dialog,
                                                       final List<SelectorColumnModelItem> columns) throws FileNotFoundException, 
                                                       UnsupportedEncodingException, InvalidFormatException;
}
