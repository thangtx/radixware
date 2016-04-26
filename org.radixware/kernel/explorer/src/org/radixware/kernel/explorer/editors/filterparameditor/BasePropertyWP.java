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

package org.radixware.kernel.explorer.editors.filterparameditor;

import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QWizardPage;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.meta.RadClassPresentationDef;
import org.radixware.kernel.common.client.meta.filters.RadFilterUserParamDef;
import org.radixware.kernel.common.client.meta.RadPropertyDef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlColumnDef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlModifiableParameter;
import org.radixware.kernel.common.client.meta.sqml.ISqmlParameterFactory;
import org.radixware.kernel.common.client.meta.sqml.ISqmlTableDef;
import org.radixware.kernel.common.exceptions.DefinitionError;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.env.Application;


final class BasePropertyWP extends QWizardPage {

    private final TargetPropertyAttributeEditor targetPropertyEditor;
    private final ISqmlParameterFactory parameterFactory;
    private ISqmlModifiableParameter newParamDef;    

    public BasePropertyWP(IClientEnvironment environment, final ISqmlTableDef context, final ISqmlParameterFactory paramFactory) {
        super();
        parameterFactory = paramFactory;
        targetPropertyEditor = new TargetPropertyAttributeEditor(environment, context, false, this);
        setTitle(Application.translate("SqmlEditor", "Select base property or column"));
        final QVBoxLayout layout = new QVBoxLayout(this);
        layout.addWidget(targetPropertyEditor.getEditorWidget());
        setFinalPage(false);

    }

    @Override
    public void initializePage() {
        targetPropertyEditor.onDoubleClick.connect(wizard(), "next()");
    }

    @Override
    public boolean validatePage() {
        if (targetPropertyEditor.getAttributeValue() != null) {
            final ISqmlColumnDef column = targetPropertyEditor.getAttributeValue();
            RadPropertyDef baseProperty = null;
            if (column.hasProperty()) {
                final Id classId = column.getOwnerTable().getId();
                try {
                    final RadClassPresentationDef classDef = targetPropertyEditor.getEnvironment().getApplication().getDefManager().getClassPresentationDef(classId);
                    if (classDef.isPropertyDefExistsById(column.getId())) {
                        baseProperty = classDef.getPropertyDefById(column.getId());
                    }
                } catch (DefinitionError ex) {
                    Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
                }
            }
            final RadFilterUserParamDef paramDef;
            if (baseProperty == null) {
                newParamDef = parameterFactory.createModifableParameter(column);
            } else {
                newParamDef = parameterFactory.createModifableParameter(baseProperty);
            }
            return true;
        } else {
            return false;
        }
    }

    public ISqmlModifiableParameter getResultParameter() {
        return newParamDef;
    }

    @Override
    public int nextId() {
        return ParameterCreationWizard.WizardPages.ATTRIBUTES.ordinal();
    }
}
