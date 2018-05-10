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
import java.util.ArrayList;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.meta.sqml.ISqmlModifiableParameter;
import org.radixware.kernel.common.client.meta.sqml.ISqmlTableDef;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.explorer.env.Application;


final class ParamAttributesWP extends QWizardPage {

    private final ParameterEditorWidget editor;
    private final List<String> restrictedNames;
    private ISqmlModifiableParameter parameter;

    public ParamAttributesWP(IClientEnvironment environment, final ISqmlTableDef context, final List<String> restrictedNames) {
        super();
        setTitle(Application.translate("SqmlEditor", "Define parameter attributes"));
        editor = new ParameterEditorWidget(environment, context, false, this);
        this.restrictedNames = restrictedNames;
        final QVBoxLayout layout = new QVBoxLayout(this);
        layout.addWidget(editor);
        setFinalPage(true);
    }

    public void setParameter(final ISqmlModifiableParameter paramDef) {
        parameter = paramDef;
    }

    @Override
    public void initializePage() {
        final List<EFilterParamAttribute> attributes = new ArrayList<EFilterParamAttribute>();
        attributes.add(EFilterParamAttribute.NAME);
        attributes.add(EFilterParamAttribute.IS_MANDATORY);
        final EValType valType = parameter.getType();
        final boolean isParentRef = valType == EValType.PARENT_REF || valType == EValType.ARR_REF;
        final boolean isBoolean = valType == EValType.BOOL || valType == EValType.ARR_BOOL;
        final boolean isEnumType =
                valType == EValType.INT || valType == EValType.CHAR || valType == EValType.STR
                || valType == EValType.ARR_INT || valType == EValType.ARR_CHAR || valType == EValType.ARR_STR;
        if (parameter.getBasePropertyId() == null) {
            if (isEnumType) {
                attributes.add(EFilterParamAttribute.ENUM);
            }
            attributes.add(EFilterParamAttribute.EDIT_MASK);
            attributes.add(EFilterParamAttribute.NULL_TITLE);
            if (isParentRef) {
                attributes.add(EFilterParamAttribute.SELECTOR_PRESENTATION);
                if (attributes.contains(EFilterParamAttribute.EDIT_MASK)) {
                    attributes.remove(EFilterParamAttribute.EDIT_MASK);
                }                
            }            
            if (isBoolean) {
                if (attributes.contains(EFilterParamAttribute.EDIT_MASK)) {
                    attributes.remove(EFilterParamAttribute.EDIT_MASK);
                }
            }
        }

        if (isParentRef) {
            attributes.add(EFilterParamAttribute.ADDITIONAL_SELECTOR_CONDITION);
            attributes.add(EFilterParamAttribute.PARENT_REF_EDITING_MODE);            
        }else{
            attributes.add(EFilterParamAttribute.DEFAULT_VALUE);
        }
        if (parameter.canHavePersistentValue()) {
            attributes.add(EFilterParamAttribute.PERSISTENT_VALUE_DEFINED);
            attributes.add(EFilterParamAttribute.PERSISTENT_VALUE);
        }
        editor.open(attributes, restrictedNames);
        editor.readAttributes(parameter);
    }

    @Override
    public boolean validatePage() {
        return editor.writeAttributes(parameter);
    }

    @Override
    public void cleanupPage() {
        editor.clear();
    }
}
