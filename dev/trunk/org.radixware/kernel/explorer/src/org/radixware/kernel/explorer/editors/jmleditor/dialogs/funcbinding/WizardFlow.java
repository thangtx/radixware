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

import com.trolltech.qt.core.QModelIndex;
import com.trolltech.qt.gui.QWizard;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityObjectClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.userfunc.AdsUserFuncDef;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.editors.jmleditor.dialogs.IChooseDefFromList;
import org.radixware.kernel.explorer.editors.jmleditor.dialogs.ListModel;
import org.radixware.kernel.explorer.editors.jmleditor.dialogs.ListModelForRadixObj;

public class WizardFlow implements IChooseDefFromList {

    static final int CHOOSE_PARAMETER_KIND = 0;
    static final int CHOOSE_ENTITY = 1;
    static final int CHOOSE_DB_OBJECT = 2;
    static final int CHOOSE_DB_PROP = 3;

    public enum TargetType {

        VALUE,
        PARAMETER,
        OBJECT,        
        PROPERTY,
        UNKNOWN,
        OWNER
    }

    public enum Mode {

        CHOOSE_CLASS,
        CHOOSE_PROPERTY,
        CHOOSE_OBJECT,
        CHOOSE_KIND
    }
    private Mode mode = Mode.CHOOSE_KIND;
    private AdsEntityObjectClassDef clazz;
    private AdsPropertyDef property;
    private TargetType targetType = TargetType.UNKNOWN;
    private final LinkedFuncParamWizard wizard;
    //private boolean stepCompleted = false;
    private String selectedEntityPid;
    private Id calleyParameterId;
    private Object value;

    public WizardFlow(final LinkedFuncParamWizard wizard) {
        this.wizard = wizard;
    }

    public int getNextStepId() {
        switch (targetType) {
            case PROPERTY:
            case OBJECT: {
                switch (mode) {
                    case CHOOSE_KIND:
                        return CHOOSE_ENTITY;
                    case CHOOSE_CLASS:
                        return targetType == TargetType.OBJECT ? CHOOSE_DB_OBJECT : CHOOSE_DB_PROP;
                    case CHOOSE_PROPERTY:
                        return CHOOSE_DB_OBJECT;
                }
            }
        }
        return -1;
    }

    public AdsUserFuncDef getUserFunc() {
        return wizard.userFunc;
    }

    public void setTargetType(final TargetType tt) {
        this.targetType = tt;
        updateWizardButtons();
    }

    public String getSelectedEntityPid() {
        return selectedEntityPid;
    }

    public void setSelectedEntityPid(final String pid) {
        selectedEntityPid = pid;
    }

    public Id getPropertyId() {
        return property == null ? null : property.getId();
    }

    public Id getClassId() {
        return clazz == null ? null : clazz.getId();
    }

    public AdsClassDef getClassDef() {
        return clazz;
    }

    public TargetType getTargetType() {
        return targetType;
    }

    public void leaveMode(final Mode mode) {
        switch (mode) {
            case CHOOSE_CLASS:
                setMode(Mode.CHOOSE_KIND);
                break;
            case CHOOSE_PROPERTY:
                setMode(Mode.CHOOSE_CLASS);
                break;
            case CHOOSE_OBJECT:
                switch (targetType) {
                    case PROPERTY:
                        setMode(Mode.CHOOSE_PROPERTY);
                        break;
                    case OBJECT:
                        setMode(Mode.CHOOSE_CLASS);
                        break;
                }
                break;
        }
        updateWizardButtons();
    }

    public void setMode(final Mode mode) {
        this.mode = mode;
        switch (mode) {
            case CHOOSE_CLASS:
                clazz = null;
                property = null;
                selectedEntityPid = null;
                break;
            case CHOOSE_PROPERTY:
                property = null;
                selectedEntityPid = null;
                break;
            case CHOOSE_OBJECT:
                selectedEntityPid = null;
                break;
        }
        updateWizardButtons();
    }

    private void updateWizardButtons() {
        wizard.button(QWizard.WizardButton.NextButton).setVisible(false);
        wizard.button(QWizard.WizardButton.NextButton).setEnabled(false);
        wizard.button(QWizard.WizardButton.FinishButton).setVisible(false);
        wizard.button(QWizard.WizardButton.FinishButton).setEnabled(false);
        switch (targetType) {
            case PROPERTY:
                switch (mode) {
                    case CHOOSE_CLASS:
                        if (clazz != null) {
                            wizard.button(QWizard.WizardButton.NextButton).setVisible(true);
                            wizard.button(QWizard.WizardButton.NextButton).setEnabled(true);
                        }
                        break;
                    case CHOOSE_PROPERTY:
                        if (clazz != null && property != null) {
                            wizard.button(QWizard.WizardButton.NextButton).setVisible(true);
                            wizard.button(QWizard.WizardButton.NextButton).setEnabled(true);
                        }
                        break;
                    case CHOOSE_OBJECT: {
                        wizard.button(QWizard.WizardButton.FinishButton).setVisible(true);
                        wizard.button(QWizard.WizardButton.FinishButton).setEnabled(true);
                    }
                    break;
                    default:
                        wizard.button(QWizard.WizardButton.NextButton).setVisible(true);
                        wizard.button(QWizard.WizardButton.NextButton).setEnabled(true);

                }
                break;
            case OBJECT:
                switch (mode) {
                    case CHOOSE_CLASS:
                        if (clazz != null) {
                            wizard.button(QWizard.WizardButton.NextButton).setVisible(true);
                            wizard.button(QWizard.WizardButton.NextButton).setEnabled(true);
                        }
                        break;
                    case CHOOSE_OBJECT: {
                        wizard.button(QWizard.WizardButton.FinishButton).setVisible(true);
                        wizard.button(QWizard.WizardButton.FinishButton).setEnabled(true);
                        break;
                    }
                    default:
                        wizard.button(QWizard.WizardButton.NextButton).setVisible(true);
                        wizard.button(QWizard.WizardButton.NextButton).setEnabled(true);

                }
                break;
            default:
                wizard.button(QWizard.WizardButton.FinishButton).setVisible(true);
                wizard.button(QWizard.WizardButton.FinishButton).setEnabled(true);
        }
    }

    public Id getParameterId() {
        return calleyParameterId;
    }

    public void setParameterId(final Id id) {
        calleyParameterId = id;
    }

    @Override
    public void onItemClick(final QModelIndex modelIndex) {
        setCurItem(modelIndex);
    }

    public Object getValue() {
        return value;
    }

    public void setValue(final Object value) {
        this.value = value;
    }

    @Override
    public void onItemDoubleClick(final QModelIndex modelIndex) {
        if (setCurItem(modelIndex)) {
            final boolean hasNextPage = wizard.button(QWizard.WizardButton.NextButton).isEnabled() && wizard.button(QWizard.WizardButton.NextButton).isVisible();
            if (hasNextPage) {
                wizard.next();
            } else {
                wizard.accept();
            }
        }
    }

    @Override
    public boolean setCurItem(final QModelIndex modelIndex) {
        if (modelIndex != null) {
            switch (mode) {
                case CHOOSE_CLASS:
                    if (modelIndex.model() instanceof ListModel) {
                        final AdsUserFuncDef.Lookup.DefInfo di = ((ListModel) modelIndex.model()).getDefList().get(modelIndex.row());
                        if (di != null && di.getDefinition() != null) {
                            setSelectedDefinition(di.getDefinition());
                        }
                    }
                    break;
                case CHOOSE_PROPERTY:
                    if (modelIndex.model() instanceof ListModelForRadixObj) {
                        final RadixObject curProp = ((ListModelForRadixObj) modelIndex.model()).getDefList().get(modelIndex.row());
                        if (curProp != null && (curProp instanceof AdsPropertyDef)) {
                            setSelectedDefinition((AdsDefinition) curProp);
                        }
                    }
                    break;
            }
        }
        updateWizardButtons();
        return false;

    }

    @Override
    public boolean setSelectedDefinition(final AdsDefinition def) {
        switch (mode) {
            case CHOOSE_CLASS:
                if (def instanceof AdsEntityObjectClassDef) {
                    clazz = (AdsEntityObjectClassDef) def;
                    return true;
                } else {
                    clazz = null;
                }
                break;
            case CHOOSE_PROPERTY:
                if (def instanceof AdsPropertyDef) {
                    final AdsPropertyDef prop = (AdsPropertyDef) def;
                    property = prop;
                    return true;
                } else {
                    property = null;
                }
                break;
        }
        return false;
    }
}
