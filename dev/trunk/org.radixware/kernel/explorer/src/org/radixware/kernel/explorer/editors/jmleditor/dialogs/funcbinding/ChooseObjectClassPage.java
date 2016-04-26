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

import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QWizardPage;
import java.util.EnumSet;
import java.util.Set;
import org.radixware.kernel.common.defs.ExtendableDefinitions;
import org.radixware.kernel.common.defs.IFilter;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsApplicationClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.defs.ads.userfunc.AdsUserFuncDef;
import org.radixware.kernel.common.enums.EDefType;
import org.radixware.kernel.explorer.editors.jmleditor.dialogs.ChooseDefinitionPanel;


public class ChooseObjectClassPage extends QWizardPage {

    private ChooseDefinitionPanel chooseDefPanel;
    private final QVBoxLayout layout;
    private final LinkedFuncParamWizard wizard;

    public ChooseObjectClassPage(final LinkedFuncParamWizard parent) {
        super(parent);
        this.wizard = parent;
        this.setObjectName("ChooseDefPage");
        this.setFinalPage(false);
        layout = new QVBoxLayout();
        layout.setMargin(0);
        this.setLayout(layout);
    }

    @Override
    public boolean isComplete() {
        return wizard.getFlow().getClassId() != null;
    }

    public void clear() {
        if (layout.indexOf(chooseDefPanel) != -1) {
            layout.removeWidget(chooseDefPanel);
        }
        chooseDefPanel.hide();
        layout.update();
    }

    @Override
    public int nextId() {
        return wizard.getFlow().getNextStepId();
    }

    @Override
    public void initializePage() {
        wizard.getFlow().setMode(WizardFlow.Mode.CHOOSE_CLASS);
        final Set<EDefType> selectedTypes = EnumSet.noneOf(EDefType.class);
        selectedTypes.add(EDefType.CLASS);
        chooseDefPanel = new ChooseDefinitionPanel(wizard.getFlow(), null, wizard.getFlow().getUserFunc(), selectedTypes, true, true, false, new IFilter<AdsDefinition>() {
            @Override
            public boolean isTarget(final AdsDefinition radixObject) {

                if (radixObject instanceof AdsEntityClassDef || radixObject instanceof AdsApplicationClassDef) {
                    final AdsClassDef clazz = (AdsClassDef) radixObject;
                    final AdsTypeDeclaration classType = AdsTypeDeclaration.Factory.newInstance(clazz);

                    if (wizard.getFlow().getTargetType() == WizardFlow.TargetType.OBJECT) {
                        if (AdsTypeDeclaration.isAssignable(wizard.getTargetParamType(), classType, wizard.getFlow().getUserFunc())) {
                            return true;
                        } else {
                            return false;
                        }
                    } else {
                        for (AdsPropertyDef prop : clazz.getProperties().get(ExtendableDefinitions.EScope.ALL)) {
                            if (prop.getValue() == null || prop.getValue().getType() == null) {
                                continue;
                            }
                            if (prop.isPublished() && AdsUserFuncDef.areTypesBindable(wizard.getFlow().getUserFunc(), wizard.getTargetParamType(), prop.getValue().getType())) {
                                return true;
                            }
                        }
                        return false;
                    }
                } else {
                    return false;
                }
            }
        }, null);
        chooseDefPanel.setObjectName("ChooseDefinitionPanel");
        layout.addWidget(chooseDefPanel);
    }

    @Override
    public void cleanupPage() {
        clear();
        chooseDefPanel.closeTread();
        wizard.getFlow().leaveMode(WizardFlow.Mode.CHOOSE_CLASS);
    }
}
