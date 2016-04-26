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
package org.radixware.kernel.explorer.editors.jmleditor.dialogs.funcbinding;

import com.trolltech.qt.gui.QWidget;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.utils.ValueConverter;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.defs.ads.userfunc.AdsUserFuncDef;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.explorer.editors.jmleditor.dialogs.BaseWizard;
import static org.radixware.kernel.explorer.editors.jmleditor.dialogs.funcbinding.WizardFlow.TargetType.OBJECT;
import static org.radixware.kernel.explorer.editors.jmleditor.dialogs.funcbinding.WizardFlow.TargetType.OWNER;
import static org.radixware.kernel.explorer.editors.jmleditor.dialogs.funcbinding.WizardFlow.TargetType.PARAMETER;
import static org.radixware.kernel.explorer.editors.jmleditor.dialogs.funcbinding.WizardFlow.TargetType.PROPERTY;
import static org.radixware.kernel.explorer.editors.jmleditor.dialogs.funcbinding.WizardFlow.TargetType.VALUE;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.kernel.explorer.env.ExplorerSettings;
import org.radixware.schemas.reports.ParametersBindingType;
import org.radixware.schemas.reports.ParametersBindingType.ParameterBinding;

public class LinkedFuncParamWizard extends BaseWizard {

    final AdsUserFuncDef userFunc;
    private final IClientEnvironment environment;
    private final AdsTypeDeclaration type;
    private final WizardFlow flow = new WizardFlow(this);

    public AdsTypeDeclaration getTargetParamType() {
        return type;
    }

    public WizardFlow getFlow() {
        return flow;
    }

    public IClientEnvironment getEnvironment() {
        return environment;
    }

    public ParametersBindingType.ParameterBinding getBinding() {
        final ParametersBindingType.ParameterBinding xResult = ParametersBindingType.ParameterBinding.Factory.newInstance();

        switch (flow.getTargetType()) {
            case PARAMETER:
                xResult.setParameter(flow.getParameterId());
                break;
            case PROPERTY: {
                final ParameterBinding.ExternalValue xPropRef = xResult.addNewExternalValue();
                xPropRef.setOwnerPID(flow.getSelectedEntityPid());
                xPropRef.setOwnerClassId(flow.getClassId());
                final org.radixware.schemas.eas.Property xProp = xPropRef.addNewValue();
                xProp.setId(flow.getPropertyId());
            }
            break;
            case OBJECT: {
                final ParameterBinding.ExternalValue xPropRef = xResult.addNewExternalValue();
                xPropRef.setOwnerPID(flow.getSelectedEntityPid());
                xPropRef.setOwnerClassId(flow.getClassId());
            }
            break;
            case VALUE: {
                if (Utils.convertType(getTargetParamType()) != EValType.USER_CLASS) {
                    final ParameterBinding.ExternalValue xPropRef = xResult.addNewExternalValue();
                    ValueConverter.objVal2EasPropXmlVal(flow.getValue(), Utils.convertType(getTargetParamType()), xPropRef.addNewValue());
                }
            }
            break;
            case OWNER: {
                final ParameterBinding.ExternalValue xPropRef = xResult.addNewExternalValue();
                xPropRef.setOwnerPID(userFunc.getOwnerPid());
                xPropRef.setOwnerClassId(userFunc.getOwnerClassId());
            }
            break;
        }
        return xResult;
    }

    public LinkedFuncParamWizard(final QWidget parent, final AdsUserFuncDef userFunc, final AdsTypeDeclaration type, final ParametersBindingType.ParameterBinding xBinding, final IClientEnvironment environment) {
        super(parent, (ExplorerSettings) environment.getConfigStore(), "LinkedFuncParamWizard");
        this.type = type;
        this.userFunc = userFunc;
        this.environment = environment;
        setPage(WizardFlow.CHOOSE_PARAMETER_KIND, new ChooseValueKindPage(this, xBinding));
        setPage(WizardFlow.CHOOSE_ENTITY, new ChooseObjectClassPage(this));
        setPage(WizardFlow.CHOOSE_DB_PROP, new ChoosePropertyPage(this));
        setPage(WizardFlow.CHOOSE_DB_OBJECT, new ChooseDbObjectPage(this));

        setWindowTitle(Application.translate("JmlEditor", "Setup Parameter Binding"));
    }
}
