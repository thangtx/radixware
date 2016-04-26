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

package org.radixware.kernel.designer.common.dialogs.components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.defs.ads.type.IAdsTypedObject;
import org.radixware.kernel.common.resources.RadixWareIcons;
import org.radixware.kernel.designer.common.dialogs.choosetype.ChooseType;
import org.radixware.kernel.designer.common.dialogs.choosetype.ITypeFilter;
import org.radixware.kernel.designer.common.dialogs.choosetype.TypeEditDisplayer;
import org.radixware.kernel.designer.common.dialogs.components.TypeEditorComponent.TypeContext;
import org.radixware.kernel.designer.common.dialogs.components.values.ContextableValueEditorComponent;
import org.radixware.kernel.designer.common.dialogs.components.values.EditorLayout;

/**
 * The editor panel for choice type
 *
 */
public class TypeEditorComponent extends ContextableValueEditorComponent<AdsTypeDeclaration, TypeContext> {

    public static class TypeContext {

        private IAdsTypedObject typedObject;
        private AdsDefinition context;

        public TypeContext(IAdsTypedObject typedObject, AdsDefinition context) {
            this.typedObject = typedObject;
            this.context = context;
        }

        public IAdsTypedObject getTypedObject() {
            return typedObject;
        }

        public AdsDefinition getContextDefinition() {
            return context;
        }

        public void commit(AdsTypeDeclaration type) {
        }
    }

    public static class LocalModel extends ContextableValueEditorComponent.DefaultSampleEditorModel<AdsTypeDeclaration, TypeContext> {

        @Override
        protected AdsTypeDeclaration getValueFromContext(TypeContext context) {
            return context != null ? context.getTypedObject().getType() : AdsTypeDeclaration.UNDEFINED;
        }

        @Override
        public void updateValue(Object... params) {
        }

        @Override
        protected void commitImpl() {
            getContext().commit(getValue());
        }

        @Override
        public AdsTypeDeclaration getNullValue() {
            return AdsTypeDeclaration.UNDEFINED;
        }

    }
    private final JTextField editor = new JTextField();
    private final JPanel panel = new AdvancePanel();

    public TypeEditorComponent() {
        super(new LocalModel());

        panel.setLayout(new EditorLayout());

        editor.setEditable(false);
        editor.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() > 0) {
                    takeValueEvent();
                }
            }
        });

        panel.add(editor, EditorLayout.LEADER_CONSTRAINT);

        final JButton selectButton;
        selectButton = new JButton(RadixWareIcons.DIALOG.CHOOSE.getIcon());
        selectButton.setToolTipText(NbBundle.getMessage(TypeEditorComponent.class, "TypeEditorComponent.TypeChooseButton.Text"));
        selectButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                takeValueEvent();
            }
        });
        panel.add(selectButton);
        getModel().setValue(AdsTypeDeclaration.UNDEFINED);
    }

    private void takeValueEvent() {
        AdsTypeDeclaration oldValue = getValue();
        if (takeValue()) {
            fireValueChange(getValue(), oldValue);
        }
    }

    protected boolean takeValue() {
        TypeContext context = getModel().getContext();

        AdsTypeDeclaration newType;

        ITypeFilter typeFilter = new ChooseType.DefaultTypeFilter(context.getContextDefinition(), context.getTypedObject());

//        if (!isSetValue()) {
//            newType = ChooseType.getInstance().chooseType(typeFilter);
//        } else {
            TypeEditDisplayer typeEditDisplayer = new TypeEditDisplayer();
            newType = typeEditDisplayer.editType(getModel().getValue(), typeFilter);
//        }

        if (newType != null && !newType.equals(getModel().getValue())) {
            getModel().setValue(newType);
            updateEditorComponent();
            return true;
        }
        return false;
    }

    public final void open(AdsDefinition context, IAdsTypedObject object) {
        open(new TypeContext(object, context));
    }

    @Override
    protected void updateEditorComponent() {
        if (getModel().getContext() != null) {
            editor.setText(getValue().getName(getModel().getContext().getContextDefinition()));
        } else {
            editor.setText("");
        }
    }

    @Override
    public JPanel getEditorComponent() {
        return panel;
    }

    @Override
    protected void connectEditorComponent() {
    }

    @Override
    protected void disconnectEditorComponent() {
    }

    @Override
    protected void updateModelValue() {
    }
}
