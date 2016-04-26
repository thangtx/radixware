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
import java.util.ArrayList;
import java.util.List;
import org.radixware.kernel.common.defs.ExtendableDefinitions;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.userfunc.AdsUserFuncDef;
import org.radixware.kernel.explorer.editors.jmleditor.dialogs.ChooseObjectPanel;


public class ChoosePropertyPage extends QWizardPage {

    private final ChooseObjectPanel chooseObjectPanel;
    //private boolean enabled = false;
    private final LinkedFuncParamWizard wizard;

    public ChoosePropertyPage(final LinkedFuncParamWizard parent) {
        super(parent);
        this.wizard = parent;
        this.setObjectName("InvocatePage");
        final QVBoxLayout layout = new QVBoxLayout();
        layout.setMargin(0);
        chooseObjectPanel = new ChooseObjectPanel(wizard.getFlow(), null);
        chooseObjectPanel.setObjectName("ChooseObjectPanel");
        layout.addWidget(chooseObjectPanel);
        this.setLayout(layout);
    }

    @Override
    public void initializePage() {
        wizard.getFlow().setMode(WizardFlow.Mode.CHOOSE_PROPERTY);
        final List<RadixObject> allowedDefinitions = getProps();
        chooseObjectPanel.update(allowedDefinitions);
       /* if (allowedDefinitions.size() > 0) {
            enabled = true;
        } else {
            enabled = false;
        }*/
    }

    @Override
    public boolean isComplete() {
        return wizard.getFlow().getPropertyId() != null;
    }

    @Override
    public int nextId() {
        return wizard.getFlow().getNextStepId();
    }

    private List<RadixObject> getProps() {
        final List<RadixObject> allowedDefinitions = new ArrayList<>();
        final List<AdsPropertyDef> propList = wizard.getFlow().getClassDef().getProperties().get(ExtendableDefinitions.EScope.ALL);
        for (AdsPropertyDef prop : propList) {
            if (prop.isPublished() && prop.getValue() != null && prop.getValue().getType() != null) {
                if (AdsUserFuncDef.areTypesBindable(wizard.getFlow().getUserFunc(), wizard.getTargetParamType(), prop.getValue().getType())) {
                    allowedDefinitions.add(prop);
                }
            }
        }
        return allowedDefinitions;
    }

    @Override
    public void cleanupPage() {
        wizard.getFlow().leaveMode(WizardFlow.Mode.CHOOSE_PROPERTY);
        super.cleanupPage();
    }
}
