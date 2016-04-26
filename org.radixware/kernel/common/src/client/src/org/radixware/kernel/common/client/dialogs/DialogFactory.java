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

package org.radixware.kernel.common.client.dialogs;

import java.sql.Timestamp;
import java.util.List;
import org.radixware.kernel.common.auth.PasswordRequirements;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.exceptions.StandardProblemHandler;
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
import org.radixware.kernel.common.enums.EDialogIconType;
import org.radixware.kernel.common.enums.EValType;


public interface DialogFactory {
    
    public IChangePasswordDialog newChangePasswordDialog(IClientEnvironment environment);

    public IEnterPasswordDialog newEnterPasswordDialog(IClientEnvironment environment);
    
    public IDialog newExceptionBoxDialog(IClientEnvironment environment, IWidget parent, String title, String message, String exceptionDetails, String exceptionTrace);
    
    public IDialog newMessageWithDetailsDialog(IClientEnvironment environment, IWidget parent, String title, String message, String messageDetails, EDialogIconType iconType);

    public IFiltersManagerDialog newFiltersManagerDialog(IClientEnvironment environment, IWidget parent, Filters filters);
    
    public ISortingsManagerDialog newSortingsManagerDialog(IClientEnvironment environment, IWidget parent, Sortings sortings);

    public ISelectInstantiatableClassDialog newInstantiatableClassDialog(IClientEnvironment environment, IWidget parentWidget, List<InstantiatableClass> classes, String configGroup, boolean sortByTitles);

    public IMandatoryPropertiesConfirmationDialog newMandatoryPropertiesConfirmationDialog(IClientEnvironment environment, final IWidget parentWidget, final List<String> propertyTitles);

    public IArrayEditorDialog newArrayEditorDialog(IClientEnvironment environment, EValType valType, Class<?> valClass, boolean readonly, IWidget widget);
    
    public IArrayEditorDialog newArrayEditorDialog(IClientEnvironment environment, RadSelectorPresentationDef presentation, boolean readonly, IWidget widget);
    
    public IArrayEditorDialog newArrayEditorDialog(Property property,IWidget widget);
    
    public IDisplayProblemsDialog newDisplayProblemsDialog(IClientEnvironment environment, IWidget parentWidget, StandardProblemHandler problemsHandler);
    
    public IAskForApplyChangesDialog newAskForApplyChangesDialiog(IClientEnvironment environment, IWidget parentWidget, ModificationsList modifications);
    
    public IDateTimeDialog newDateTimeDialog(IClientEnvironment environment, IWidget parent, EditMaskDateTime mask, Timestamp initialVal);
    
    public IDateTimeDialog newDateTimeDialog(IClientEnvironment environment, IWidget parent, String timeFormat, Timestamp initialVal);
    
    public ISelectEasSessionDialog newSelectEasSessionDialog(IClientEnvironment environment, IWidget parent);
}
