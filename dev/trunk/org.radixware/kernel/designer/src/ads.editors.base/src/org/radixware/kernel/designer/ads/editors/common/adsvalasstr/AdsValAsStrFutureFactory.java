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
 * 11/29/11 9:53 AM
 */
package org.radixware.kernel.designer.ads.editors.common.adsvalasstr;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.text.JTextComponent;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.ads.AdsValAsStr;
import org.radixware.kernel.common.jml.Jml;
import org.radixware.kernel.common.resources.RadixWareIcons;
import org.radixware.kernel.designer.common.dialogs.components.StringPopupEditor;
import org.radixware.kernel.designer.common.dialogs.components.values.IModalEditor;
import org.radixware.kernel.designer.common.dialogs.components.values.IValueEditorFactory;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;


final class AdsValAsStrFutureFactory {

    private static abstract class Feature implements IValueEditorFactory.IFeature<AdsValAsStrEditor> {

        protected AdsValAsStrEditor editor;
        protected JComponent component;

        @Override
        public JComponent install(AdsValAsStrEditor editor) {
            this.editor = editor;

            component = createComponent(editor);
            assert component != null : "Null component";
            return component;
        }

        @Override
        public void enabled(boolean on) {
            component.setVisible(on);
        }

        abstract JComponent createComponent(AdsValAsStrEditor editor);
    }

    private static abstract class ButtonFeature extends Feature {

        final JButton button;

        public ButtonFeature(String toolTip, Icon icon) {
            button = new JButton();

            button.setFocusable(false);
            button.setIcon(icon);
            button.setToolTipText(toolTip);

            button.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    invoke();
                }
            });
        }

        @Override
        JButton createComponent(AdsValAsStrEditor editor) {
            return button;
        }
    }

    private static final class JmlSwitchFeature extends ButtonFeature {

        public JmlSwitchFeature() {
            super(NbBundle.getMessage(AdsValAsStrEditor.class, "AdsValAsStrEditorComponent.ValAsStrToJml.Text"), RadixWareIcons.JAVA.JAVA.getIcon(13, 13));
        }

        @Override
        public void checkCondition() {
            enabled(editor.isOpened() && (!editor.isSetValue() || !editor.getValue().typeEquals(AdsValAsStr.EValueType.JML)
                    && editor.getContext().isValueTypeAvailable(AdsValAsStr.EValueType.JML)));
        }

        private void switchToJml() {

            if (!editor.getValue().typeEquals(AdsValAsStr.EValueType.JML)) {

                final AdsValAsStr adsValAsStr = AdsValAsStr.Converter.convertTo(AdsValAsStr.EValueType.JML, editor.getContext());

                final Jml jml;
                if (adsValAsStr != null && adsValAsStr.typeEquals(AdsValAsStr.EValueType.JML)) {
                    jml = adsValAsStr.getJml();
                } else {
                    jml = Jml.Factory.newInstance(editor.getContext().getContextDefinition(), "");
                }

                final String message = NbBundle.getMessage(AdsValAsStrEditor.class, "ValAsStrToJmlConvertConfirmMessage");
                if (DialogUtils.messageConfirmation(message)) {
                    editor.setValue(AdsValAsStr.Factory.newInstance(jml));
                    openJmlEditor();
                }
            }
        }

        private void openJmlEditor() {
            if (editor.getSingleEditor() instanceof IModalEditor) {
                ((IModalEditor) editor.getSingleEditor()).show();
            }
        }

        @Override
        public void invoke() {
            switchToJml();
        }
    }

    private static final class ValAsStrSwitchFeature extends ButtonFeature {

        public ValAsStrSwitchFeature() {
            super(NbBundle.getMessage(AdsValAsStrEditor.class, "AdsValAsStrEditorComponent.JmlToValAsStr.Text"), RadixWareIcons.JAVA.CLASS.getIcon(13, 13));
        }

        @Override
        public void checkCondition() {
            enabled(editor.isOpened() && editor.isSetValue() && !editor.getValue().typeEquals(AdsValAsStr.EValueType.VAL_AS_STR)
                    && editor.getContext().isValueTypeAvailable(AdsValAsStr.EValueType.VAL_AS_STR));
        }

        private void setMode(AdsValAsStr.EValueType type) {
            AdsValAsStr value = AdsValAsStr.Converter.convertTo(AdsValAsStr.EValueType.VAL_AS_STR, editor.getContext());

            final String message = NbBundle.getMessage(AdsValAsStrEditor.class, "JmlToValAsStrConvertConfirmMessage");
            if (DialogUtils.messageConfirmation(message)) {
                editor.setValue(value);
            }
        }

        @Override
        public void invoke() {
            setMode(AdsValAsStr.EValueType.VAL_AS_STR);
        }
    }

    private static final class NullValueFeature extends ButtonFeature {

        public NullValueFeature() {
            super(NbBundle.getMessage(AdsValAsStrEditor.class, "AdsValAsStrEditor.ClearButton.Text"), RadixWareIcons.DELETE.CLEAR.getIcon(13, 13));
        }

        @Override
        public void checkCondition() {
            enabled(editor.isOpened() && editor.isSetValue() && editor.getContext().isValueTypeAvailable(AdsValAsStr.EValueType.NULL));
        }

        private void resetValue() {
            String msg = NbBundle.getMessage(AdsValAsStrEditor.class, "AdsValAsStrEditor.ClearDialogMessage");
            boolean confirm = DialogUtils.messageConfirmation(msg);

            if (confirm) {
                editor.setValue(AdsValAsStr.NULL_VALUE);
                editor.requestFocus();
            }
        }

        @Override
        public void invoke() {
            resetValue();
        }
    }

    private static final class PopupEditorFeature extends ButtonFeature {

        int height = 200;
        private StringPopupEditor popupEditor;

        public PopupEditorFeature() {
            super(NbBundle.getMessage(AdsValAsStrEditor.class, "AdsValAsStrEditor.PopupButton.ToolTipText"),
                    RadixWareIcons.DIALOG.CHOOSE.getIcon(13, 13));
        }

        @Override
        public void checkCondition() {
            enabled(editor.getSingleEditor() instanceof StringEditorComponent);
        }

        private StringPopupEditor getPopupEditor(int width, int height) {
            final JTextComponent baseComponent = ((StringEditorComponent) editor.getSingleEditor()).getTextComponent();
            final StringPopupEditor popup = new StringPopupEditor(new JTextArea(), width, height);
            final StringEditorComponent alternateEditor = new StringEditorComponent(popup.getEditor());

            popup.addPopupMenuListener(new PopupMenuListener() {

                @Override
                public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                    editor.connectExternalEditor(alternateEditor);

                    SwingUtilities.invokeLater(new Runnable() {

                        @Override
                        public void run() {
                            setCaretPosition(popup.getEditor(), getCaretPosition(baseComponent));
                            popup.getEditor().requestFocusInWindow();
                        }
                    });
                }

                @Override
                public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                    editor.connectExternalEditor(alternateEditor);
                    popupEditor = null;

                    SwingUtilities.invokeLater(new Runnable() {

                        @Override
                        public void run() {
//                            setCaretPosition(baseComponent, popup.getEditor().getCaretPosition());
                            baseComponent.requestFocusInWindow();
                        }
                    });
                }

                @Override
                public void popupMenuCanceled(PopupMenuEvent e) {
                    popupMenuWillBecomeInvisible(e);
                }
            });

            return popup;
        }

        @Override
        public void invoke() {

            if (popupEditor != null) {
                popupEditor.hide();
                return;
            }

            final Component editorComponent = editor.getSingleEditor().getEditorComponent();

            popupEditor = getPopupEditor(editorComponent.getWidth(), height);
            popupEditor.show(editor, 0, editorComponent.getHeight());

        }

        private int getCaretPosition(JTextComponent component) {
            if (component != null) {
                return component.getCaretPosition();
            }
            return -1;
        }

        private void setCaretPosition(JTextComponent component, int caretPosition) {
            if (component != null && caretPosition >= 0) {
                component.setCaretPosition(caretPosition);
            }
        }
    }

    public static Feature getNullValueFeature() {
        return new NullValueFeature();
    }

    public static Feature getValAsStrSwitchFeature() {
        return new ValAsStrSwitchFeature();
    }

    public static Feature getJmlSwitchFeature() {
        return new JmlSwitchFeature();
    }

    public static Feature getPopupEditorFeature() {
        return new PopupEditorFeature();
    }
}
