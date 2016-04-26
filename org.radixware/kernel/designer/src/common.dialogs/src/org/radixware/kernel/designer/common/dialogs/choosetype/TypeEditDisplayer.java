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

package org.radixware.kernel.designer.common.dialogs.choosetype;

import java.awt.Dimension;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.defs.ads.type.IAdsTypedObject;
import org.radixware.kernel.designer.common.dialogs.utils.ModalDisplayer;


public final class TypeEditDisplayer extends ModalDisplayer implements ChangeListener {

    private TypeEditDisplayer(TypeEditPanel panel) {
        super(panel);

        setTitle(NbBundle.getMessage(TypeEditPanel.class, "TypeEdit-DialogTitle"));

        panel.setSelectedAction(new Runnable() {

            @Override
            public void run() {
                if (getDialogDescriptor().isValid()) {
                    close(true);
                }
            }
        });

        panel.addChangeListener(this);
        panel.setMinimumSize(new Dimension(325, 175));
        panel.setPreferredSize(panel.getMinimumSize());
    }

    public TypeEditDisplayer() {
        this(new TypeEditPanel(true, false));

        setTitle(NbBundle.getMessage(TypeEditPanel.class, "TypeEdit-DialogTitle"));

        TypeEditPanel panel = getComponent();

        panel.addChangeListener(this);
        panel.setMinimumSize(new Dimension(325, 175));
        panel.setPreferredSize(panel.getMinimumSize());
    }

    @Override
    protected void apply() {
        if (isEdited) {
            AdsTypeDeclaration copy = AdsTypeDeclaration.Factory.newCopy(getComponent().getCurrentType());
            LastUsedTypeCollection.getInstance(TypeEditPanel.LAST_USED_PATH).addLastUsed(copy);
        }
        LastUsedTypeCollection.getInstance(TypeEditPanel.LAST_USED_PATH).saveLastUsed();
    }

    @Override
    protected void beforeShow() {
        updateState();
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        if (e.getSource().equals(getComponent())) {
            isEdited = true;
            getDialogDescriptor().setValid(getComponent().isComplete());
        }
    }

    @Override
    public TypeEditPanel getComponent() {
        return (TypeEditPanel) super.getComponent();
    }
    private boolean isEdited = false;

    public boolean isEdited() {
        return isEdited;
    }

    private void updateState() {
        getDialogDescriptor().setValid(getComponent().isComplete());
    }

    public AdsTypeDeclaration editType(TypeEditorModel model) {

        getComponent().open(model);

        if (showModal()) {
            if (isEdited()) {
                return getComponent().getCurrentType();
            }
            return getComponent().getModel().getType();
        }
        return null;
    }

    public AdsTypeDeclaration editType(AdsTypeDeclaration currentType, Definition context, IAdsTypedObject typedObject) {

        ITypeFilter typeFilter = new ChooseType.DefaultTypeFilter(context, typedObject);
        TypeEditorModel model = new TypeEditorModel(currentType, typeFilter);
        return editType(model);
    }

    public AdsTypeDeclaration editType(AdsTypeDeclaration currentType, ITypeFilter typeFilter) {
        TypeEditorModel model = new TypeEditorModel(currentType, typeFilter);
        return editType(model);
    }
}