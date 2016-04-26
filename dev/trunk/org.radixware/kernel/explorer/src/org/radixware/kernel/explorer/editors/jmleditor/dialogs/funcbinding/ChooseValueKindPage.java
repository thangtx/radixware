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

import com.trolltech.qt.gui.QComboBox;
import com.trolltech.qt.gui.QGroupBox;
import com.trolltech.qt.gui.QHBoxLayout;
import com.trolltech.qt.gui.QRadioButton;
import com.trolltech.qt.gui.QSizePolicy;
import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QWizardPage;
import org.radixware.kernel.common.client.utils.ValueConverter;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityObjectClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodDef;
import org.radixware.kernel.common.defs.ads.clazz.members.MethodParameter;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.defs.ads.userfunc.AdsUserFuncDef;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.schemas.reports.ParametersBindingType;

public class ChooseValueKindPage extends QWizardPage {

    private final QRadioButton rbNullVal;
    private final QRadioButton rbValEditor;
    private final QRadioButton rbParam;
    private final QRadioButton rbDbObjest;
    private final QRadioButton rbDbProperty;
    private final QRadioButton rbUfOwner;
    private final BindingValueEditorProvider bindingValEditor;
    private final QComboBox cbParam;
    private final LinkedFuncParamWizard wizard;

    public ChooseValueKindPage(final LinkedFuncParamWizard parent, final ParametersBindingType.ParameterBinding xBinding) {
        super(parent);
        this.wizard = parent;
        final QVBoxLayout layout = new QVBoxLayout();
        this.setObjectName("ChooseParamKindPage");
        this.setTitle(Application.translate("JmlEditor", "Specify Binding Kind"));
        final QGroupBox groupBox = new QGroupBox("");
        groupBox.setObjectName("groupBox");

        boolean allowConstant = true;
        boolean allowDbLink = true;
        boolean allowParam = true;

        if (parent.getTargetParamType() == null) {
            allowConstant = false;
            allowDbLink = false;
            allowParam = false;
        } else {
            if (parent.getTargetParamType().getArrayDimensionCount() > 0) {
                allowConstant = false;
            }
        }
        QHBoxLayout valLayout = null;
        if (allowConstant) {
            valLayout = new QHBoxLayout();
            rbValEditor = new QRadioButton(Application.translate("JmlEditor", "Set value"));
            rbValEditor.setObjectName("rbValEditor");
            rbValEditor.clicked.connect(this, "valueSelected()");
            valLayout.setContentsMargins(20, 0, 0, 0);

            bindingValEditor = new BindingValueEditorProvider(parent.getTargetParamType(), parent.getFlow().getUserFunc(), parent.getEnvironment(), this);
            bindingValEditor.valueChanged.connect(this, "valueChanged(Object)");
            if (bindingValEditor.isSet()) {
                bindingValEditor.getEditor().setSizePolicy(QSizePolicy.Policy.MinimumExpanding, QSizePolicy.Policy.Fixed);
                valLayout.addWidget(bindingValEditor.getEditor());
            }
        } else {
            bindingValEditor = null;
            rbValEditor = null;
        }
        QHBoxLayout paramLayout = null;
        if (allowParam) {
            rbParam = new QRadioButton(Application.translate("JmlEditor", "Parameter"));
            rbParam.setObjectName("rbParamType");
            rbParam.clicked.connect(this, "paramTypeSelected()");

            paramLayout = new QHBoxLayout();
            paramLayout.setContentsMargins(20, 0, 0, 0);
            cbParam = new QComboBox();
            cbParam.currentIndexChanged.connect(this, "parameterChanged(Integer)");
            cbParam.setSizePolicy(QSizePolicy.Policy.Expanding, QSizePolicy.Policy.Preferred);
            fillParamComboBox(xBinding.isSetParameter() ? xBinding.getParameter() : null);
            //      paramLayout.addWidget(lbDimension);
            paramLayout.addWidget(cbParam/*,0, Qt.AlignmentFlag.AlignLeft*/);
        } else {
            rbParam = null;
            cbParam = null;

        }

        rbNullVal = new QRadioButton(Application.translate("JmlEditor", "NULL"));
        rbNullVal.setObjectName("rbNullVal");
        rbNullVal.clicked.connect(this, "nullValSelected()");
        if (allowDbLink) {
            rbDbObjest = new QRadioButton(Application.translate("JmlEditor", "DB Object"));
            rbDbObjest.setObjectName("rbDbObjest");
            rbDbObjest.clicked.connect(this, "dbObjestSelected()");

            rbUfOwner = new QRadioButton(Application.translate("JmlEditor", "User Function Owner"));
            rbUfOwner.setObjectName("rbUfOwner");
            rbUfOwner.clicked.connect(this, "ufOwnerSelected()");

            rbDbProperty = new QRadioButton(Application.translate("JmlEditor", "Property of DB Object"));
            rbDbProperty.setObjectName("rbDbProperty");
            rbDbProperty.clicked.connect(this, "dbPropSelected()");

            final AdsDefinition def = AdsUserFuncDef.Lookup.findTopLevelDefinition(parent.getFlow().getUserFunc(), parent.getFlow().getUserFunc().getOwnerClassId());
            final AdsTypeDeclaration ownerType = AdsTypeDeclaration.Factory.newInstance((AdsClassDef) def);
            final boolean isUfOwnerEnabled = AdsUserFuncDef.areTypesBindable(parent.getFlow().getUserFunc(), parent.getTargetParamType(), ownerType);
            rbUfOwner.setVisible(isUfOwnerEnabled);

            final boolean isDbObjectEnabled = parent.getTargetParamType().getTypeId() == EValType.USER_CLASS;
            rbDbObjest.setVisible(isDbObjectEnabled);
        } else {
            rbDbObjest = null;
            rbUfOwner = null;
            rbDbProperty = null;
        }

        final QVBoxLayout vbox = new QVBoxLayout();
        vbox.setObjectName("vbox");
        vbox.addWidget(rbNullVal);
        if (allowConstant && bindingValEditor.isSet()) {
            vbox.addWidget(rbValEditor);
            vbox.addLayout(valLayout);
        }
        if (allowDbLink) {
            vbox.addWidget(rbDbObjest);
            vbox.addWidget(rbDbProperty);
            vbox.addWidget(rbUfOwner);
        }
        if (allowParam) {
            vbox.addWidget(rbParam);
            vbox.addLayout(paramLayout);
        }

        groupBox.setLayout(vbox);
        layout.addWidget(groupBox);
        this.setLayout(layout);
        init(xBinding);
    }

    @Override
    public void initializePage() {
        wizard.getFlow().setMode(WizardFlow.Mode.CHOOSE_KIND);
    }

    private void init(final ParametersBindingType.ParameterBinding xBinding) {
        final boolean isDbObject = xBinding.isSetExternalValue() && xBinding.getExternalValue().isSetOwnerPID() && xBinding.getExternalValue().isSetOwnerClassId();
        if (isDbObject) {
            final boolean isDbProperty = xBinding.getExternalValue().isSetValue() && xBinding.getExternalValue().getValue().getId() != null;
            if (isDbProperty) {
                rbDbProperty.setChecked(true);
                dbPropSelected();
            } else {
                if (xBinding.getExternalValue().getOwnerPID().equals(wizard.getFlow().getUserFunc().getOwnerPid())) {
                    Id ownerEntityId;
                    final AdsDefinition ownerClass = AdsUserFuncDef.Lookup.findTopLevelDefinition(wizard.getFlow().getUserFunc(), wizard.getFlow().getUserFunc().getOwnerClassId());
                    if (ownerClass instanceof AdsEntityObjectClassDef) {
                        ownerEntityId = ((AdsEntityObjectClassDef) ownerClass).getEntityId();
                    } else {
                        ownerEntityId = wizard.getFlow().getUserFunc().getOwnerClassId();
                    }
                    if (ownerEntityId == xBinding.getExternalValue().getOwnerClassId()) {
                        rbUfOwner.setChecked(true);
                        ufOwnerSelected();
                    } else {
                        rbDbObjest.setChecked(true);
                        dbObjestSelected();
                    }
                } else {
                    rbDbObjest.setChecked(true);
                    dbObjestSelected();
                }
            }
        } else {
            if (xBinding.isSetExternalValue() && xBinding.getExternalValue().isSetValue()) {

                Object val = null;
                if (wizard.getTargetParamType().getTypeId() != EValType.USER_CLASS) {
                    val = ValueConverter.easPropXmlVal2ObjVal(xBinding.getExternalValue().getValue(), Utils.convertType(wizard.getTargetParamType()), null);
                }
                if (val == null) {
                    rbNullVal.setChecked(true);
                    nullValSelected();
                } else {
                    if (rbValEditor != null) {
                        rbValEditor.setChecked(true);
                    }
                    bindingValEditor.setValue(val);
                    valueSelected();
                }

            } else if (xBinding.isSetParameter()) {
                if (rbParam != null) {
                    paramTypeSelected();
                    rbParam.setChecked(xBinding.isSetParameter());
                }
            } else {
                rbNullVal.setChecked(true);
                nullValSelected();
                /*final boolean isDbObjectEnabled = wizard.getTargetParamType().getTypeId() == EValType.USER_CLASS;
                 if (rbDbObjest != null && isDbObjectEnabled) {
                 rbDbObjest.setChecked(true);
                 dbObjestSelected();
                 }else if(rbValEditor!=null){
                 rbValEditor.setChecked(true);
                 valueSelected();
                 }*/
            }
        }
    }

    private void fillParamComboBox(final Id paramId) {
        if (cbParam != null) {
            final AdsMethodDef.Profile profile = wizard.getFlow().getUserFunc().findProfile();
            if (profile != null && wizard.getTargetParamType() != null) {
                for (MethodParameter p : profile.getParametersList()) {
                    //if(p.getType().isBasedOn(type.getTypeId())){
                    if (AdsUserFuncDef.areTypesBindable(wizard.getFlow().getUserFunc(), wizard.getTargetParamType(), p.getType())) {
                        cbParam.addItem(p.getName(), p.getId());
                        if (paramId != null && paramId.equals(p.getId())) {
                            cbParam.setCurrentIndex(cbParam.count() - 1);
                        }
                    }
                }
            }
            if (cbParam.count() == 0) {
                cbParam.setEnabled(false);
                rbParam.setEnabled(false);
            }
        }
    }

    private void setParamEnable(final boolean enable) {
        if (cbParam != null) {
            if (enable && cbParam.count() > 0) {
                parameterChanged(cbParam.currentIndex());
            }
            cbParam.setEnabled(enable);
            this.setFinalPage(enable);
        }
    }

    private void setValueEnable(final boolean enable) {
        if (bindingValEditor != null) {
            bindingValEditor.setEditorEnabled(enable);
        }
    }

    @Override
    public int nextId() {
        return wizard.getFlow().getNextStepId();
    }

    @SuppressWarnings("unused")
    private void valueChanged(final Object val) {
        wizard.getFlow().setValue(val);
    }

    @SuppressWarnings("unused")
    private void parameterChanged(final Integer index) {
        if (cbParam != null) {
            final Id paramId = (Id) cbParam.itemData(index);
            wizard.getFlow().setParameterId(paramId);
        }
    }

    @SuppressWarnings("unused")
    private void nullValSelected() {
        setParamEnable(false);
        setValueEnable(false);
        setFinalPage(true);
        wizard.getFlow().setValue(null);
        wizard.getFlow().setTargetType(WizardFlow.TargetType.VALUE);
        //wizard.getFlow().setTargetType(WizardFlow.TargetType.VALUE);
    }

    @SuppressWarnings("unused")
    private void valueSelected() {
        setParamEnable(false);
        setValueEnable(true);
        setFinalPage(true);
        wizard.getFlow().setTargetType(WizardFlow.TargetType.VALUE);
    }

    @SuppressWarnings("unused")
    private void paramTypeSelected() {
        setParamEnable(true);
        setValueEnable(false);
        setFinalPage(true);
        wizard.getFlow().setTargetType(WizardFlow.TargetType.PARAMETER);
    }

    @SuppressWarnings("unused")
    private void dbObjestSelected() {
        setParamEnable(false);
        setValueEnable(false);
        setFinalPage(false);
        wizard.getFlow().setTargetType(WizardFlow.TargetType.OBJECT);
    }

    @SuppressWarnings("unused")
    private void dbPropSelected() {
        setParamEnable(false);
        setValueEnable(false);
        setFinalPage(false);
        wizard.getFlow().setTargetType(WizardFlow.TargetType.PROPERTY);
    }

    @SuppressWarnings("unused")
    private void ufOwnerSelected() {
        setParamEnable(false);
        setValueEnable(false);
        setFinalPage(true);
        wizard.getFlow().setTargetType(WizardFlow.TargetType.OWNER);

    }

    @Override
    public void cleanupPage() {
        wizard.getFlow().leaveMode(WizardFlow.Mode.CHOOSE_KIND);
        super.cleanupPage();
    }

    @Override
    public boolean validatePage() {
        if (wizard.getFlow().getTargetType()==WizardFlow.TargetType.VALUE){
            return bindingValEditor==null ? true : bindingValEditor.checkInput();
        }else{
            return true;
        }
    }
    
    
}
