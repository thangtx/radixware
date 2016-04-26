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

package org.radixware.wps.views.editor.property;

import org.radixware.kernel.common.client.errors.ObjectNotFoundError;
import org.radixware.kernel.common.client.errors.ParentRefSetterError;
import org.radixware.kernel.common.client.errors.SettingPropertyValueError;
import org.radixware.kernel.common.client.meta.filters.RadFilterParamDef;
import org.radixware.kernel.common.client.meta.RadParentRefPropertyDef;
import org.radixware.kernel.common.client.meta.RadSelectorPresentationDef;
import org.radixware.kernel.common.client.models.items.properties.PropertyRef;
import org.radixware.kernel.common.client.types.RefEditingHistory;
import org.radixware.kernel.common.client.types.Reference;
import org.radixware.kernel.common.utils.Utils;


public class PropRefEditor extends PropReferenceEditor {

    public PropRefEditor(PropertyRef property) {
        super(property);
    }

    @Override
    protected void selectEntity() {
        final PropertyRef property = (PropertyRef) getProperty();
        boolean tryAgain;
        Reference newValue;
        do {
            try {
                newValue = property.selectParent();
            } catch (InterruptedException e) {
                getEnvironment().processException(e);
                return;
            } catch (Exception exception) {
                final String msg = getEnvironment().getMessageProvider().translate("ExplorerException", "Can't open selector for \'%s\':\n%s");
                controller.processException(exception, getEnvironment().getMessageProvider().translate("ExplorerException", "Error on opening selector"), msg);
                return;
            }

            tryAgain = false;
            if (!Utils.equals(controller.getPropertyValue(), newValue)) {
                try {
                    property.setValueObject(newValue);
                } catch (ParentRefSetterError error) {
                    if (error.getCause() instanceof ObjectNotFoundError && newValue != null) {
                        final ObjectNotFoundError objNotFound = (ObjectNotFoundError) error.getCause();
                        if (objNotFound.inContextOf(newValue)) {
                            final String title = getEnvironment().getMessageProvider().translate("ExplorerMessage", "Selected Object Does not Exist");
                            getEnvironment().processException(title, objNotFound);
                            tryAgain = true;
                            continue;
                        }
                    }
                    getEnvironment().processException(new SettingPropertyValueError(property, error));
                } catch (Exception exception) {
                    getEnvironment().processException(new SettingPropertyValueError(property, exception));
                }
            }
        } while (tryAgain);
        if (!Utils.equals(controller.getPropertyValue(), getCurrentValueInEditor())) { //case when bind method was not called (ex. org.radixware.kernel.explorer.widgets.selector.WrapModelDelegate).            
            refresh(property);
        }
    }

    private RadSelectorPresentationDef getParentSelectorPresentation() {
        RadSelectorPresentationDef presentation;
        if (getProperty().getDefinition() instanceof RadParentRefPropertyDef) {
            presentation = ((RadParentRefPropertyDef) getProperty().getDefinition()).getParentSelectorPresentation();
        } else if (getProperty().getDefinition() instanceof RadFilterParamDef) {
            presentation = ((RadFilterParamDef) getProperty().getDefinition()).getParentSelectorPresentation();
        } else {
            presentation = null;
        }
        return presentation;
    }

    @Override
    protected void enableEditingHistory(final String settingPath) {
        final RadSelectorPresentationDef parentPresentation = getParentSelectorPresentation();
        if (parentPresentation == null) {
            setEditingHistory(null);
        } else {
            final RefEditingHistory history = new RefEditingHistory(getEnvironment(), settingPath, parentPresentation.getTableId(), parentPresentation.getId());
            setEditingHistory(history);
        }
    }
}
