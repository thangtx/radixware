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

package org.radixware.kernel.designer.ads.editors.clazz.property;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.event.ChangeListener;
import org.openide.util.ChangeSupport;
import org.radixware.kernel.common.components.ExtendableTextField;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.resources.RadixWareIcons;
import org.radixware.kernel.designer.common.dialogs.choosetype.TypeEditDisplayer;
import org.radixware.kernel.designer.common.dialogs.components.state.StateManager;


public class PropertyTypeChoosePanel extends ExtendableTextField {

    private StateManager stateManager = new StateManager(this);

    public PropertyTypeChoosePanel() {
        JButton cfgButton = addButton(RadixWareIcons.DIALOG.CHOOSE.getIcon(13, 13));
        cfgButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                configureType();
            }
        });
    }
    private AdsPropertyDef property;

    public void open(AdsPropertyDef property) {
        this.property = property;
        updateState();
    }

    public void updateState() {
        this.setValue(property.getValue().getType().getQualifiedName(property));
    }

    public void configureType() {
        TypeEditDisplayer typeEditDisplayer = new TypeEditDisplayer();
        AdsTypeDeclaration newType = typeEditDisplayer.editType(property.getValue().getType(), property, property.getValue());

        if (newType != null) {
            property.getValue().setType(newType);
            updateState();
            changeSupport.fireChange();
        }
    }

    public boolean isComplete() {
        if (property.getValue().getType() != null && property.getValue().getType() != AdsTypeDeclaration.UNDEFINED) {
            stateManager.ok();
            return true;
        } else {
            stateManager.error("Property type is undefined");
            return false;
        }
    }
    ChangeSupport changeSupport = new ChangeSupport(this);

    public void removeChangeListener(ChangeListener listener) {
        changeSupport.removeChangeListener(listener);
    }

    public void addChangeListener(ChangeListener listener) {
        changeSupport.addChangeListener(listener);
    }
}
