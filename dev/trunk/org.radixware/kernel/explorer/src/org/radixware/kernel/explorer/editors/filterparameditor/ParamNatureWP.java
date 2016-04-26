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

import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QGroupBox;
import com.trolltech.qt.gui.QRadioButton;
import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QWizardPage;
import org.radixware.kernel.explorer.env.Application;


final class ParamNatureWP extends QWizardPage{

    
    private final QRadioButton rbPropertyBasedParam;
    private final QRadioButton rbCustomParam;

    public ParamNatureWP(){
        super();
        setAutoFillBackground(true);
        final QGroupBox gbParamNature = new QGroupBox(this);
        rbPropertyBasedParam =
            new QRadioButton(Application.translate("SqmlEditor", "Property-based parameter.\nSelect to create new parameter with derived attributes."),gbParamNature);
        rbPropertyBasedParam.setChecked(true);
        rbCustomParam =
                new QRadioButton(Application.translate("SqmlEditor", "User-defined parameter.\nSelect to create parameter with your own attributes."),gbParamNature);
        final QVBoxLayout groupBoxLayout = new QVBoxLayout(gbParamNature);
        final QVBoxLayout mainVLayout = new QVBoxLayout(this);

        gbParamNature.setObjectName("gbParamNature");
        rbPropertyBasedParam.setObjectName("rbPropertyBasedParam");
        rbCustomParam.setObjectName("rbCustomParam");
        groupBoxLayout.addWidget(rbPropertyBasedParam);
        groupBoxLayout.addWidget(rbCustomParam);
        groupBoxLayout.addStretch(1);
        mainVLayout.addWidget(gbParamNature);
        mainVLayout.setAlignment(new Qt.Alignment(Qt.AlignmentFlag.AlignVCenter));
        setTitle(Application.translate("SqmlEditor", "What type of parameter you want to create?"));
        setFinalPage(false);
    }

    @Override
    public int nextId() {
        if (rbPropertyBasedParam.isChecked()){
            return ParameterCreationWizard.WizardPages.BASE_PROPERTY.ordinal();
        }
        else{
            return ParameterCreationWizard.WizardPages.VALUE_TYPE.ordinal();
        }
    }



}
