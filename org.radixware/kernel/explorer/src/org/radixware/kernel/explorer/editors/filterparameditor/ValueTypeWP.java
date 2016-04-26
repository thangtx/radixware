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
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.meta.filters.RadFilterUserParamDef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlModifiableParameter;
import org.radixware.kernel.common.client.meta.sqml.ISqmlParameterFactory;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.explorer.env.Application;


public class ValueTypeWP extends QWizardPage {

    private final ValueTypeAttributeEditor valTypeEditor;
    private final ISqmlParameterFactory parameterFactory; 
    private ISqmlModifiableParameter newParamDef;

    public ValueTypeWP(final IClientEnvironment environment, final ISqmlParameterFactory paramFactory) {
        super();
        parameterFactory = paramFactory;
        valTypeEditor = new ValueTypeAttributeEditor(environment, true, false, this);
        final QVBoxLayout layout = new QVBoxLayout(this);
        setTitle(Application.translate("SqmlEditor", "Select parameter type"));
        layout.addWidget(valTypeEditor.getEditorWidget());
        setFinalPage(false);
    }

    @Override
    public void initializePage() {
        valTypeEditor.onDoubleClick.connect(wizard(), "next()");
    }

    @Override
    public boolean validatePage() {
        final EValType valType = valTypeEditor.getAttributeValue();
        newParamDef = parameterFactory.createModifableParameter(valType);
        return true;
    }

    public ISqmlModifiableParameter getResultParameter() {
        return newParamDef;
    }

    @Override
    public int nextId() {
        return ParameterCreationWizard.WizardPages.ATTRIBUTES.ordinal();
    }
}
