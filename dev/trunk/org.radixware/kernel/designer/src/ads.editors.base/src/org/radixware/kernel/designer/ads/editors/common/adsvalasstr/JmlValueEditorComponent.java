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

/*
 * 9/23/11 2:05 PM
 */
package org.radixware.kernel.designer.ads.editors.common.adsvalasstr;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.JTextField;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ads.AdsValAsStr;
import org.radixware.kernel.common.jml.Jml;
import org.radixware.kernel.common.jml.JmlDisplayer;
import org.radixware.kernel.common.resources.RadixWareIcons;
import org.radixware.kernel.designer.common.dialogs.components.values.BuildingPanel;
import org.radixware.kernel.designer.common.dialogs.components.values.EditorLayout;
import org.radixware.kernel.designer.common.dialogs.components.values.IModalEditor;


public class JmlValueEditorComponent extends BaseEditorComponent<JmlValueEditorComponent.LocalModel> implements IModalEditor {

    public static final class LocalModel extends BaseEditorComponent.BaseEditorModel<Jml> {

        public void edit() {

            final Jml value = getLocalValue();

            if (value == null) {
                final Definition context = getContext().getContextDefinition();
                setLocalValue(Jml.Factory.newInstance(context, context.getName()));
                fireValueChange(getValue(), getValue());
            }

            if (value != null) {
                edit(value);
            }
        }

        public boolean edit(Jml jml) {
            if (jml != null) {
                final JmlEditorDisplayer jmlDisplayer = new JmlEditorDisplayer(jml, getDialogTitle());
                final AdsValAsStr oldValue = getValue();

                jmlDisplayer.showModal();
                fireValueChange(getValue(), oldValue);
                return true;
            }
            return false;
        }

        private String getDialogTitle() {
            String title = "";
            if (isOpened()) {
                title += getContext().getContextDefinition().getName();
            }
            return title;
        }

        @Override
        protected Jml toLocal(AdsValAsStr value) {
            if (value != null) {
                return value.getJml();
            }
            return null;
        }

        @Override
        protected AdsValAsStr toExternal(Jml local) {
            return AdsValAsStr.Factory.newInstance(local);
        }

        @Override
        public void updateValue(Object... params) {
        }

    }

    private final JTextField editor;
    private final JButton btnSelect;
    private final BuildingPanel<Integer> panel;

    public JmlValueEditorComponent() {
        super(new LocalModel());
        panel = new BuildingPanel<>();

        panel.setLayout(new EditorLayout());

        editor = new JTextField();
        editor.setEditable(false);
        editor.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                show();
            }

        });

        panel.getComponentSequence().addComponent(0, editor, EditorLayout.LEADER_CONSTRAINT);

        btnSelect = new JButton(RadixWareIcons.EDIT.EDIT.getIcon(13, 13));
        btnSelect.setToolTipText(NbBundle.getMessage(JmlValueEditorComponent.class, "JmlValueEditorComponent.JmlEditorButton.Text"));
        btnSelect.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                show();
            }

        });
        panel.getComponentSequence().addComponent(1, btnSelect, null);
        panel.getComponentSequence().build();
    }

    @Override
    public BuildingPanel<Integer> getEditorComponent() {
        return panel;
    }

    @Override
    protected void updateModelValue() {
    }

    @Override
    protected void updateEditorComponent() {
        final Jml local = getModel().getLocalValue();
        if (local != null) {
            editor.setText(JmlDisplayer.toReadableString(local));
        } else {
            editor.setText(AdsValAsStr.NULL_VALUE.toString());
        }
    }

    @Override
    protected void connectEditorComponent() {
    }

    @Override
    protected void disconnectEditorComponent() {
    }

    @Override
    public int getBaseline(int width, int height) {
        return editor.getBaseline(width, height);
    }

    @Override
    public void show() {
        if (isOpened() && editor.isEnabled()) {
            getModel().edit();
        }
    }
}
