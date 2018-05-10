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

package org.radixware.kernel.common.client.widgets.selector;

import java.util.Collection;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.dialogs.IMessageBox;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.env.SettingNames;
import org.radixware.kernel.common.client.exceptions.BrokenEntityObjectException;
import org.radixware.kernel.common.client.models.GroupModel;
import org.radixware.kernel.common.client.types.Icon;
import org.radixware.kernel.common.client.types.Pid;
import org.radixware.kernel.common.client.views.IProgressHandle;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.common.enums.EDialogIconType;
import org.radixware.kernel.common.exceptions.ServiceClientException;


public class SelectorModelDataLoader {

    private static final class ButtonIcon extends ClientIcon {

        public static final ButtonIcon YES_BUTTON = new ButtonIcon("classpath:images/ok.svg");
        public static final ButtonIcon NO_BUTTON = new ButtonIcon("classpath:images/cancel.svg");
        public static final ButtonIcon YES_TO_ALL_BUTTON = new ButtonIcon("classpath:images/all.svg");

        private ButtonIcon(final String fileName) {
            super(fileName, true);
        }
    }
    private final static String ROWS_LIMIT_CONFIG_PATH =
            SettingNames.SYSTEM + "/" + SettingNames.SELECTOR_GROUP + "/" + SettingNames.Selector.COMMON_GROUP + "/" + SettingNames.Selector.Common.ROWS_LIMIT_FOR_SEARCH;
    private final IClientEnvironment environment;
    private IProgressHandle progressHandle;
    private final String updateSelectorWidgetProgress;
    private boolean checkForLoadingLimitEnabled = true;
    private boolean showDontAskButtonInConfirmationDialog = true;
    private boolean showProgress = true;
    private int loadedObjectsCount = 0;
    private int loadingLimit;
    private int loadingDelta;
    private String confirmationMessage;
    private String continueButtonText;
    private String dontAskButtonText;
    private String interruptButtonText;
    private String progressHeader;
    private String progressTitle;

    public SelectorModelDataLoader(final IClientEnvironment environment) {
        this.environment = environment;
        loadingDelta = environment.getConfigStore().readInteger(ROWS_LIMIT_CONFIG_PATH, 100);
        loadingLimit = loadingDelta;
        progressHandle = environment.getProgressHandleManager().newStandardProgressHandle();
        continueButtonText = null;
        dontAskButtonText = environment.getMessageProvider().translate("Selector", "Continue Operation and don't Ask Again");
        interruptButtonText = null;
        updateSelectorWidgetProgress = environment.getMessageProvider().translate("Selector", "Processing Loaded Objects");
    }

    public int getLoadingLimit() {
        return loadingDelta;
    }

    public void setLoadingLimit(final int limit) {
        loadingDelta = limit;
    }

    public String getConfirmationMessageText() {
        return confirmationMessage;
    }

    public void setConfirmationMessageText(final String newText) {
        confirmationMessage = newText;
    }

    public String getContinueOperationButtonText() {
        return continueButtonText;
    }

    public void setContinueOperationButtonText(final String buttonText) {
        this.continueButtonText = buttonText;
    }

    public String getDontAskButtonText() {
        return dontAskButtonText;
    }

    public void setDontAskButtonText(final String buttonText) {
        this.dontAskButtonText = buttonText;
    }

    public String getInterruptOperationButtonText() {
        return interruptButtonText;
    }

    public void setInterruptOperationButtonText(final String buttonText) {
        this.interruptButtonText = buttonText;
    }

    public boolean isDontAskButtonVisibleInConfirmationDialog() {
        return showDontAskButtonInConfirmationDialog;
    }

    public void setDontAskButtonVisibleInConfirmationDialog(final boolean isVisible) {
        showDontAskButtonInConfirmationDialog = isVisible;
    }

    public void setProgressHeader(final String header) {
        progressHeader = header;
    }

    public String getProgressHeader() {
        return progressHeader;
    }

    public void setProgressTitleTemplate(final String template) {
        progressTitle = template;
    }

    public String getProgressTitleTemplate() {
        return progressTitle;
    }

    public void setProgressHandle(final IProgressHandle progressHandle) {
        this.progressHandle = progressHandle;
    }

    public void setStartProgress(final boolean start) {
        showProgress = start;
    }

    public boolean isProgressDialogEnabled() {
        return showProgress;
    }

    public boolean loadMore(final ISelectorWidgetDelegate delegate) throws ServiceClientException, InterruptedException {
        if (delegate.canReadMore()) {
            if (loadingDelta > 0 && checkForLoadingLimitEnabled) {
                if (loadedObjectsCount >= loadingLimit) {
                    if (askToContinue()) {
                        loadingLimit += loadingDelta;
                    } else {
                        return false;
                    }
                }
            }            
            final int rowsCountBeforeRead = delegate.rowCount();
            final GroupModel groupModel = delegate.getChildGroup();
            if (groupModel==null){
                return false;
            }
            if (rowsCountBeforeRead < groupModel.getEntitiesCount()) {
                boolean res = delegate.readMore();
                return res;
            } else {
                final boolean loadResult = delegate.readMore();
                if (loadResult) {
                    loadedObjectsCount += delegate.rowCount() - rowsCountBeforeRead;
                }
                return loadResult;
            }
        } else {
            return false;
        }
    }
    
    private boolean loadMore(final GroupModel model) throws ServiceClientException, InterruptedException {
        if (model.hasMoreRows()) {
            if (loadingDelta > 0 && checkForLoadingLimitEnabled) {
                if (loadedObjectsCount >= loadingLimit) {
                    if (askToContinue()) {
                        loadingLimit += loadingDelta;
                    } else {
                        return false;
                    }
                }
            }
            final int rowsCountBeforeRead = model.getEntitiesCount();
            try {
                model.getEntity(model.getEntitiesCount());
            } catch (BrokenEntityObjectException ex) {
                Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
            }
            loadedObjectsCount += model.getEntitiesCount() - rowsCountBeforeRead;
            return true;
        } else {
            return false;
        }
    }

    public int loadAll(final ISelectorWidgetDelegate delegate) throws ServiceClientException, InterruptedException {
        if (progressHeader != null && !progressHeader.isEmpty()) {
            progressHandle.setTitle(progressHeader);
        }
        final GroupModel groupModel = delegate.getChildGroup();
        if (groupModel==null){
            return delegate.rowCount();
        }
        final int initialEntitiesCount = groupModel.getEntitiesCount();
        final boolean isCheckForLoadingLimitInitiallyEnabled = checkForLoadingLimitEnabled;
        if (showProgress) {
            progressHandle.startProgress(null, true);
        }
        try {
            while (loadMore(groupModel) && !progressHandle.wasCanceled()) {//loadMore(groupModel) --> delegate.readMore()
                if (progressTitle != null && !progressTitle.isEmpty()) {
                    progressHandle.setText(String.format(progressTitle, String.valueOf(getLoadedObjectsCount())));
                }
            }
        } finally {
            checkForLoadingLimitEnabled = isCheckForLoadingLimitInitiallyEnabled;
            if (showProgress) {
                progressHandle.finishProgress();
            }
            onLoadingFinished(delegate, groupModel, initialEntitiesCount);
        }
        return delegate.rowCount();
    }

    public int findObjectByPid(final ISelectorWidgetDelegate delegate, final Collection<Pid> pids) throws ServiceClientException, InterruptedException {
        final GroupModel groupModel = delegate.getChildGroup();
        if (groupModel==null){
            return -1;
        }        
        int searchResult = findObjectByPid(groupModel, 0, pids);
        if (searchResult > -1) {
            return searchResult;
        }
        int startIndex = groupModel.getEntitiesCount();
        if (progressHeader != null && !progressHeader.isEmpty()) {
            progressHandle.setTitle(progressHeader);
        }
        final int initialEntitiesCount = groupModel.getEntitiesCount();
        final boolean isCheckForLoadingLimitInitiallyEnabled = checkForLoadingLimitEnabled;
        if (showProgress) {
            progressHandle.startProgress(null, true);
        }
        try {
            while (searchResult < 0 && loadMore(delegate) && !progressHandle.wasCanceled()) {//loadMore(groupModel) --> delegate.readMore()
                searchResult = findObjectByPid(groupModel, startIndex, pids);
                startIndex = groupModel.getEntitiesCount();
                if (progressTitle != null && !progressTitle.isEmpty()) {
                    progressHandle.setText(String.format(progressTitle, String.valueOf(getLoadedObjectsCount())));
                }
            }
        } finally {
            checkForLoadingLimitEnabled = isCheckForLoadingLimitInitiallyEnabled;
            if (showProgress) {
                progressHandle.finishProgress();
            }
            onLoadingFinished(delegate, groupModel, initialEntitiesCount);
        }
        return searchResult;
    }

    private void onLoadingFinished(final ISelectorWidgetDelegate delegate, final GroupModel groupModel, final int initialEntitiesCount) {
        final boolean forceShowProgress = groupModel.getEntitiesCount() - initialEntitiesCount > 200;
        if (showProgress) {
            progressHandle.startProgress(updateSelectorWidgetProgress, false, forceShowProgress);
        }
        try {
            delegate.updateRowsCount(groupModel);
        } finally {
            if (showProgress) {
                progressHandle.finishProgress();
            }

            delegate.increaseRowsLimit();

        }
    }

    private int findObjectByPid(final GroupModel groupModel, final int startIndex, final Collection<Pid> pids) throws ServiceClientException, InterruptedException {
        for (int i = startIndex, count = groupModel.getEntitiesCount(); i < count; i++) {
            try {
                if (pids.contains(groupModel.getEntity(i).getPid())) {
                    return i;
                }
            } catch (BrokenEntityObjectException exception) {
                if (pids.contains(exception.getPid())) {
                    return i;
                }
            }
        }
        return -1;
    }   
    
    private boolean askToContinue() {        
        final String confirmationTitle =
                environment.getMessageProvider().translate("Selector", "Confirm to Proceed Operation");
        final String secondConfirmationMessage =
                environment.getMessageProvider().translate("Selector", "This operation may take a lot of resources and time.\nDo you really want to proceed the operation?");        
        while (true) {
            final EDialogButtonType result = confirmToProceed();
            if (result == EDialogButtonType.YES_TO_ALL) {
                if (environment.messageConfirmation(confirmationTitle, secondConfirmationMessage)) {
                    checkForLoadingLimitEnabled = false;
                    return true;
                }
            } else if (result == EDialogButtonType.YES) {
                return true;
            } else {
                return false;
            }
        }
    }
    
    private EDialogButtonType confirmToProceed(){
        final String message =
                String.format(getConfirmationMessageText(), String.valueOf(getLoadedObjectsCount()), String.valueOf(loadingDelta));        

        final String confirmationTitle =
                environment.getMessageProvider().translate("Selector", "Confirm to Proceed Operation");
        final String secondConfirmationMessage =
                environment.getMessageProvider().translate("Selector", "This operation may take a lot of resources and time.\nDo you really want to proceed the operation?");
        final IMessageBox messageBox = environment.newMessageBoxDialog(message, confirmationTitle, EDialogIconType.QUESTION, null);
        {
            final String continueBtntext;
            if (getContinueOperationButtonText()==null){
                continueBtntext = environment.getMessageProvider().translate("ExplorerDialog", "&Yes");
            }else{
                continueBtntext = getContinueOperationButtonText();
            }            
            final Icon btnIcon = environment.getApplication().getImageManager().getIcon(ButtonIcon.YES_BUTTON);
            messageBox.addButton(EDialogButtonType.YES, continueBtntext, btnIcon);
        }

        if (showDontAskButtonInConfirmationDialog) {            
            final Icon btnIcon = environment.getApplication().getImageManager().getIcon(ButtonIcon.YES_TO_ALL_BUTTON);
            messageBox.addButton(EDialogButtonType.YES_TO_ALL, dontAskButtonText, btnIcon);            
        }
        {
            final String interruptBtnText;
            if (getInterruptOperationButtonText()==null){
                interruptBtnText = environment.getMessageProvider().translate("ExplorerDialog", "&No");
            }else{
                interruptBtnText = interruptButtonText;
            }             
            final Icon btnIcon = environment.getApplication().getImageManager().getIcon(ButtonIcon.NO_BUTTON);
            messageBox.addButton(EDialogButtonType.NO, interruptBtnText, btnIcon);
        }
        messageBox.removeButton(EDialogButtonType.OK);
        environment.getProgressHandleManager().blockProgress();
        try {
            return messageBox.execMessageBox();
        } finally {
            environment.getProgressHandleManager().unblockProgress();
        }        
    }

    public int getLoadedObjectsCount() {
        return loadedObjectsCount;
    }

    public void resetCounters() {
        loadedObjectsCount = 0;
        loadingLimit = loadingDelta;
    }
}
