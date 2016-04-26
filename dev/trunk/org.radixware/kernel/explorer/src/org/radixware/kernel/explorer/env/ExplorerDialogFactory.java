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

package org.radixware.kernel.explorer.env;

import com.trolltech.qt.gui.QIcon;
import com.trolltech.qt.gui.QWidget;
import java.sql.Timestamp;
import java.util.List;
import org.radixware.kernel.common.auth.PasswordRequirements;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.dialogs.DialogFactory;
import org.radixware.kernel.common.client.dialogs.IAskForApplyChangesDialog;
import org.radixware.kernel.common.client.dialogs.IChangePasswordDialog;
import org.radixware.kernel.common.client.dialogs.IDateTimeDialog;
import org.radixware.kernel.common.client.dialogs.IDisplayProblemsDialog;
import org.radixware.kernel.common.client.dialogs.IEnterPasswordDialog;
import org.radixware.kernel.common.client.dialogs.IFiltersManagerDialog;
import org.radixware.kernel.common.client.dialogs.IMandatoryPropertiesConfirmationDialog;
import org.radixware.kernel.common.client.dialogs.ISelectEasSessionDialog;
import org.radixware.kernel.common.client.dialogs.ISelectInstantiatableClassDialog;
import org.radixware.kernel.common.client.dialogs.ISortingsManagerDialog;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.exceptions.StandardProblemHandler;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.meta.RadSelectorPresentationDef;
import org.radixware.kernel.common.client.meta.mask.EditMaskDateTime;
import org.radixware.kernel.common.client.models.groupsettings.Filters;
import org.radixware.kernel.common.client.models.groupsettings.Sortings;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.client.types.InstantiatableClass;
import org.radixware.kernel.common.client.utils.ClientValueFormatter;
import org.radixware.kernel.common.client.views.IArrayEditorDialog;
import org.radixware.kernel.common.client.views.IDialog;
import org.radixware.kernel.common.client.views.ModificationsList;
import org.radixware.kernel.common.client.widgets.IWidget;
import org.radixware.kernel.common.enums.EDialogIconType;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.explorer.dialogs.ArrayEditorDialog;
import org.radixware.kernel.explorer.dialogs.AskForApplyChangesDialog;
import org.radixware.kernel.explorer.dialogs.ChangePasswordDialog;
import org.radixware.kernel.explorer.dialogs.DateTimeDialog;
import org.radixware.kernel.explorer.dialogs.DisplayProblemsDialog;
import org.radixware.kernel.explorer.dialogs.EnterPasswordDialog;
import org.radixware.kernel.explorer.dialogs.ExceptionMessageDialog;
import org.radixware.kernel.explorer.dialogs.FiltersManagerDialog;
import org.radixware.kernel.explorer.dialogs.MandatoryPropertiesConfirmationDialog;
import org.radixware.kernel.explorer.dialogs.SelectEasSessionDialog;
import org.radixware.kernel.explorer.dialogs.SelectInstantiatableClassDialog;
import org.radixware.kernel.explorer.dialogs.SortingsManagerDialog;


public class ExplorerDialogFactory implements DialogFactory {

    @Override
    public IChangePasswordDialog newChangePasswordDialog(final IClientEnvironment environment) {
        return new ChangePasswordDialog(environment, (QWidget) environment.getMainWindow());
    }

    @Override
    public IEnterPasswordDialog newEnterPasswordDialog(final IClientEnvironment environment) {
        return new EnterPasswordDialog(environment, (QWidget) environment.getMainWindow());
    }

    @Override
    public IDialog newExceptionBoxDialog(final IClientEnvironment environment, final IWidget parent, final String title, final String message, final String exceptionDetails, final String exceptionTrace) {
        final ExceptionMessageDialog dialog =
                new ExceptionMessageDialog((ExplorerSettings) environment.getConfigStore(), (QWidget)parent);        
        dialog.setTitle(ClientValueFormatter.capitalizeIfNecessary(environment, title));
        dialog.setMessage(message);
        dialog.setDetails(exceptionDetails, exceptionTrace);
        return dialog;
    }

    @Override
    public IDialog newMessageWithDetailsDialog(final IClientEnvironment environment, final IWidget parent, final String title, final String message, final String messageDetails, final EDialogIconType iconType) {
        final ExceptionMessageDialog dialog =
                new ExceptionMessageDialog((ExplorerSettings) environment.getConfigStore(), (QWidget)parent);
        final String dialogTitle;
        if (title==null || title.isEmpty()){
            dialogTitle = getDefaultDialogTitle(iconType, environment.getMessageProvider());
        }else{
            dialogTitle = ClientValueFormatter.capitalizeIfNecessary(environment, title);
        }
        dialog.setTitle(dialogTitle);
        dialog.setMessage(message);
        dialog.setDetails(messageDetails, null);
        dialog.setIcon(dialogIconType2QIcon(iconType));
        return dialog;
    }
        
    @Override
    public IFiltersManagerDialog newFiltersManagerDialog(final IClientEnvironment environment, final IWidget parent, final Filters filters) {
        return new FiltersManagerDialog(environment, (QWidget) parent, filters);
    }
    
    @Override
    public ISortingsManagerDialog newSortingsManagerDialog(final IClientEnvironment environment, final IWidget parent, final Sortings sortings) {
        return new SortingsManagerDialog(environment, (QWidget)parent, sortings);
    }    

    @Override
    public ISelectInstantiatableClassDialog newInstantiatableClassDialog(final IClientEnvironment environment, final IWidget parentWidget, final List<InstantiatableClass> classes, final String configGroup, final boolean sortByTitles) {
        return new SelectInstantiatableClassDialog(environment, (QWidget) parentWidget, classes, configGroup, sortByTitles);
    }

    @Override
    public IMandatoryPropertiesConfirmationDialog newMandatoryPropertiesConfirmationDialog(final IClientEnvironment environment, final IWidget parentWidget, final List<String> propertyTitles) {
        final MandatoryPropertiesConfirmationDialog dialog = new MandatoryPropertiesConfirmationDialog(environment, (QWidget)parentWidget);
        dialog.setPropertyTitles(propertyTitles);
        return dialog;
    }

    @Override
    public IArrayEditorDialog newArrayEditorDialog(final IClientEnvironment environment, final EValType valType, final Class<?> valClass, final boolean readonly, final IWidget parentWidget) {
        return new ArrayEditorDialog(environment, valType, valClass, readonly, (QWidget)parentWidget);
    }

    @Override
    public IArrayEditorDialog newArrayEditorDialog(final IClientEnvironment environment, final RadSelectorPresentationDef presentation, final boolean readonly, final IWidget parentWidget) {
        return new ArrayEditorDialog(environment, presentation, readonly, (QWidget)parentWidget);
    }

    @Override
    public IArrayEditorDialog newArrayEditorDialog(final Property property, final IWidget parentWidget) {
        return new ArrayEditorDialog(property, (QWidget)parentWidget);
    }

    @Override
    public IAskForApplyChangesDialog newAskForApplyChangesDialiog(final IClientEnvironment environment, final IWidget parentWidget, final ModificationsList modifications) {
        return new AskForApplyChangesDialog(environment,(QWidget)parentWidget, modifications);
    }

    @Override
    public IDisplayProblemsDialog newDisplayProblemsDialog(final IClientEnvironment environment, final IWidget parentWidget, final StandardProblemHandler problemsHandler) {
        return new DisplayProblemsDialog(environment, (QWidget)parentWidget, problemsHandler);
    }

    @Override
    public IDateTimeDialog newDateTimeDialog(final IClientEnvironment environment, final IWidget parent, final EditMaskDateTime mask, final Timestamp initialVal) {
        final DateTimeDialog dialog = new DateTimeDialog(environment, (QWidget)parent);
        if (mask==null || !mask.timeFieldPresent(environment.getLocale())){
            dialog.setTimeFieldVisible(false);            
        }
        else{
            dialog.setTimeDisplayFormat(mask.getInputTimeFormat(environment.getLocale()));
        }
        if (initialVal!=null){
            dialog.setCurrentDateTime(initialVal);
        }
        return dialog;
    }

    @Override
    public IDateTimeDialog newDateTimeDialog(final IClientEnvironment environment, final IWidget parent, final String timeFormat, final Timestamp initialVal) {
        final DateTimeDialog dialog = new DateTimeDialog(environment, (QWidget)parent);        
        if (initialVal==null){
            dialog.setTimeFieldVisible(false);            
        }
        else{
            dialog.setTimeDisplayFormat(timeFormat);
        }
        if (initialVal!=null){
            dialog.setCurrentDateTime(initialVal);
        }        
        return dialog;
    }    
    
    private static QIcon dialogIconType2QIcon(final EDialogIconType icon) {
        if (icon == null) {
            return null;
        }
        switch (icon) {
            case INFORMATION:
                return ExplorerIcon.getQIcon(ClientIcon.Message.INFORMATION);
            case WARNING:
                return ExplorerIcon.getQIcon(ClientIcon.Message.WARNING);
            case CRITICAL:
                return ExplorerIcon.getQIcon(ClientIcon.Message.ERROR);
            case QUESTION:
                return ExplorerIcon.getQIcon(ClientIcon.Message.CONFIRMATION);
            default:
                return null;
        }
    }
    
    private static String getDefaultDialogTitle(final EDialogIconType icon, final MessageProvider mp){
        if (icon==null){
            return mp.translate("ExplorerDialog", "Message");
        }else{
            switch(icon){
                case CRITICAL:
                    return mp.translate("ExplorerDialog", "Error");
                case INFORMATION:
                    return mp.translate("ExplorerDialog", "Information");
                case WARNING:
                    return mp.translate("ExplorerDialog", "Warning");                    
                case QUESTION:
                    return mp.translate("ExplorerDialog", "Confirmation");
                default:
                    return mp.translate("ExplorerDialog", "Message");                
            }
        }
    }

    @Override
    public ISelectEasSessionDialog newSelectEasSessionDialog(final IClientEnvironment environment, final IWidget parent) {
        return new SelectEasSessionDialog(environment, (QWidget)parent);
    }
}
