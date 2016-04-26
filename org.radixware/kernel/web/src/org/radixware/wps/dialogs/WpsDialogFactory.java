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

package org.radixware.wps.dialogs;

import java.sql.Timestamp;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import org.radixware.kernel.common.auth.PasswordRequirements;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.dialogs.*;
import org.radixware.kernel.common.client.exceptions.StandardProblemHandler;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.meta.RadSelectorPresentationDef;
import org.radixware.kernel.common.client.meta.mask.EditMaskDateTime;
import org.radixware.kernel.common.client.models.groupsettings.Filters;
import org.radixware.kernel.common.client.models.groupsettings.Sortings;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.client.types.InstantiatableClass;
import org.radixware.kernel.common.client.views.IArrayEditorDialog;
import org.radixware.kernel.common.client.views.IDialog;
import org.radixware.kernel.common.client.views.ModificationsList;
import org.radixware.kernel.common.client.widgets.IWidget;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.common.enums.EDialogIconType;
import static org.radixware.kernel.common.enums.EDialogIconType.CRITICAL;
import static org.radixware.kernel.common.enums.EDialogIconType.INFORMATION;
import static org.radixware.kernel.common.enums.EDialogIconType.QUESTION;
import static org.radixware.kernel.common.enums.EDialogIconType.WARNING;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.wps.WpsEnvironment;
import org.radixware.wps.rwt.MessageBox;


public class WpsDialogFactory implements DialogFactory {

    @Override
    public IChangePasswordDialog newChangePasswordDialog(final IClientEnvironment environment) {
        return new ChangePasswordDialog((WpsEnvironment) environment);
    }

    @Override
    public IEnterPasswordDialog newEnterPasswordDialog(final IClientEnvironment environment) {
        return new EnterPasswordDialog((WpsEnvironment) environment);
    }

    @Override
    public IDialog newExceptionBoxDialog(final IClientEnvironment environment, final IWidget parent, final String title, final String message, final String exceptionDetails, final String exceptionTrace) {
        final Set<EDialogButtonType> b = EnumSet.of(EDialogButtonType.CLOSE);

        return new MessageBox(((WpsEnvironment) environment).getDialogDisplayer(), title, message, exceptionDetails, exceptionTrace, b, EDialogIconType.CRITICAL);
    }
    
    @Override
    public IDialog newMessageWithDetailsDialog(final IClientEnvironment environment, final IWidget parent, final String title, final String message, final String messageDetails, final EDialogIconType iconType){
        final Set<EDialogButtonType> b = EnumSet.of(EDialogButtonType.CLOSE);
        final String dialogTitle = title==null || title.isEmpty() ? getDefaultDialogTitle(iconType, environment.getMessageProvider()) : title;
        return new MessageBox(((WpsEnvironment) environment).getDialogDisplayer(), dialogTitle, message, null,  messageDetails, b, iconType);
    }

    @Override
    public IFiltersManagerDialog newFiltersManagerDialog(final IClientEnvironment environment, final IWidget parent, final Filters filters) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ISortingsManagerDialog newSortingsManagerDialog(final IClientEnvironment environment, final IWidget parent, final Sortings sortings) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ISelectInstantiatableClassDialog newInstantiatableClassDialog(final IClientEnvironment environment, final IWidget parentWidget, final List<InstantiatableClass> classes, final String configGroup, final boolean sortByTitles) {
        return new SelectInstantiatableClassDialog((WpsEnvironment) environment, classes, configGroup, sortByTitles);
    }

    @Override
    public IMandatoryPropertiesConfirmationDialog newMandatoryPropertiesConfirmationDialog(final IClientEnvironment environment, final IWidget parentWidget, final List<String> propertyTitles) {
        final MandatoryPropertiesConfirmationDialog dialog = new MandatoryPropertiesConfirmationDialog((WpsEnvironment)environment);
        dialog.setPropertyTitles(propertyTitles);
        return dialog;
    }

    @Override
    public IArrayEditorDialog newArrayEditorDialog(final IClientEnvironment environment, final EValType valType, final Class<?> valClass, final boolean readonly, final IWidget widget) {
        return new ArrayEditorDialog(environment, valType, valClass, readonly, ((WpsEnvironment) environment).getDialogDisplayer());
    }

    @Override
    public IArrayEditorDialog newArrayEditorDialog(final IClientEnvironment environment, final RadSelectorPresentationDef presentation, final boolean readonly, final IWidget widget) {
        return new ArrayEditorDialog(environment, presentation, readonly, ((WpsEnvironment) environment).getDialogDisplayer());
    }

    @Override
    public IArrayEditorDialog newArrayEditorDialog(final Property property, final IWidget widget) {
        return new ArrayEditorDialog(property, ((WpsEnvironment) property.getEnvironment()).getDialogDisplayer());
    }

    @Override
    public IAskForApplyChangesDialog newAskForApplyChangesDialiog(final IClientEnvironment environment, final IWidget parentWidget, final ModificationsList modifications) {
        return new AskForApplyChangesDialog(environment, ((WpsEnvironment) environment).getDialogDisplayer(), modifications);
    }

    @Override
    public IDisplayProblemsDialog newDisplayProblemsDialog(final IClientEnvironment environment, final IWidget parentWidget, final StandardProblemHandler problemsHandler) {
        return new DisplayProblemsDialog(environment, ((WpsEnvironment) environment).getDialogDisplayer(), problemsHandler);
    }

    @Override
    public IDateTimeDialog newDateTimeDialog(final IClientEnvironment environment, final IWidget parent, final EditMaskDateTime mask, final Timestamp initialVal) {
        final EditMaskDateTime actualMask = mask == null ? new EditMaskDateTime("dd.MM.yyyy", null, null) : mask;
        return new DateTimeDialog((WpsEnvironment) environment, actualMask, initialVal);
    }

    @Override
    public IDateTimeDialog newDateTimeDialog(final IClientEnvironment environment, final IWidget parent, final String timeFormat, final Timestamp initialVal) {
        final EditMaskDateTime mask = new EditMaskDateTime(timeFormat == null ? "dd.MM.yyyy" : timeFormat, null, null);
        return new DateTimeDialog((WpsEnvironment) environment, mask, initialVal);
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
        return new SelectEasSessionDialog((WpsEnvironment)environment);
    }
}
