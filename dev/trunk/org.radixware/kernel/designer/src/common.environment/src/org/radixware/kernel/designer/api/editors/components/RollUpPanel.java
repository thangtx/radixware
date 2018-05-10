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

package org.radixware.kernel.designer.api.editors.components;

import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.Border;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.ITransparency;
import org.radixware.kernel.common.defs.ads.type.AdsTransparence;
import org.radixware.kernel.designer.api.ApiEditorManager;
import org.radixware.kernel.designer.api.ApiFilter;
import org.radixware.kernel.designer.api.EditorState;
import org.radixware.kernel.designer.api.IApiEditor;
import org.radixware.kernel.designer.api.editors.OpenMode;
import org.radixware.kernel.designer.common.dialogs.components.CollapsablePanel;


public class RollUpPanel extends CollapsablePanel implements IStatableEditor {

    private static class DefinitionContentProvider implements CollapsablePanel.IContentProvider {

        private final RadixObject member;
        private IApiEditor<RadixObject> editor;
        private ApiFilter filter;

        public DefinitionContentProvider(RadixObject member, ApiFilter filter) {
            this.member = member;
            this.filter = filter;
        }

        @Override
        public JPanel getContent() {
            if (member == null || !member.isInBranch()) {
                return null;
            }

            if (editor == null) {
                editor = ApiEditorManager.findEditor(member);
                if (editor != null) {
                    editor.open(OpenMode.EMBEDDED, filter);
                    editor.getComponent().setBorder(BorderFactory.createEmptyBorder(4, 20, 0, 0));
                }
            } else {
                editor.update();
            }
            return editor != null ? (JPanel) editor.getComponent() : null;
        }

        IApiEditor<RadixObject> getEditor() {
            return editor;
        }
    }

    private static class TopComponent extends JPanel implements ITopComponent {

        private static abstract class ActionListenerAdapter extends MouseAdapter implements ActionListener {
        }
        private final Map<ActionListener, ActionListenerAdapter> listeners;
        private boolean isExpand;
        private JLabel iconLabel;
        private LinkLable titleLabel;
        private JLabel descriptionLabel;
        private RadixObject object;

        public TopComponent(final RadixObject object, boolean isExpand) {
            setLayout(new GridBagLayout());

            this.object = object;
            this.listeners = new HashMap<>();


            iconLabel = new JLabel();
            iconLabel.setOpaque(false);
            titleLabel = new LinkLable(object, LinkGenerator.getDefault().getLinkedString(object));
            titleLabel.setIcon(object.getIcon().getIcon());
            titleLabel.setOpaque(false);
            titleLabel.setBorder(BorderFactory.createEmptyBorder(2, 0, 2, 0));
//            setOpaque(true);

            GridBagConstraints c = new GridBagConstraints();

            c.gridx = 0;
            add(Box.createHorizontalStrut(8), c);

            c = new GridBagConstraints();
            c.gridx = 1;
            c.insets.right = 4;
            add(iconLabel, c);

            c = new GridBagConstraints();
            c.gridx = 2;
            c.fill = GridBagConstraints.VERTICAL;
            c.weightx = 1;
            c.anchor = GridBagConstraints.LINE_START;
            c.insets = new Insets(0, 0, 0, 0);
            add(titleLabel, c);

            setBorder(UIManager.getBorder("TitledBorder.border"));

            titleLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            descriptionLabel = new JLabel();
            descriptionLabel.setVisible(false);
            c = new GridBagConstraints();
            c.gridx = 0;
            c.gridy = 1;
            c.fill = GridBagConstraints.VERTICAL;
            c.gridwidth = 3;
            c.anchor = GridBagConstraints.LINE_START;
            c.insets = new Insets(4, 30, 0, 0);
            add(descriptionLabel, c);

            setExpanded(isExpand);
        }

        @Override
        protected void paintBorder(Graphics g) {
            final Border border = getBorder();
            if (border != null) {
                border.paintBorder(this, g, 0, getHeight() - 1, getWidth(), getHeight());
            }
        }

        @Override
        public JPanel getComponent() {
            return this;
        }

        @Override
        public void addActionListener(final ActionListener listener) {
            final ActionListenerAdapter mouseAdapter = new ActionListenerAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.isConsumed()) {
                        return;
                    }
                    if (e.getButton() == MouseEvent.BUTTON1) {
//                        setExpanded(!isExpanded());
                        actionPerformed(new ActionEvent(e, e.getID(), "click"));
                    }
                }

                @Override
                public void actionPerformed(ActionEvent e) {
                    setExpanded(!isExpanded());
                    listener.actionPerformed(e);
                }
            };

            titleLabel.addActionListener(mouseAdapter);
            iconLabel.addMouseListener(mouseAdapter);
            this.addMouseListener(mouseAdapter);
            listeners.put(listener, mouseAdapter);
        }

        @Override
        public void removeActionListener(ActionListener listener) {
            final ActionListenerAdapter mouseAdapter = listeners.get(listener);
            if (mouseAdapter != null) {
                titleLabel.removeActionListener(mouseAdapter);
                iconLabel.removeMouseListener(mouseAdapter);
                this.removeMouseListener(mouseAdapter);
            }
        }

        @Override
        public boolean isExpanded() {
            return isExpand;
        }

        @Override
        public void setTitle(String title) {
            titleLabel.setText(title);
        }

        @Override
        public final void setExpanded(boolean expand) {
            isExpand = expand;

            iconLabel.setIcon((isExpand ? UIManager.getIcon("Tree.expandedIcon") : UIManager.getIcon("Tree.collapsedIcon")));

            update();
        }

        private void update() {
            final String description = DescriptionService.getInstance().getDescription(object);
            if (description != null && !description.isEmpty()) {
                descriptionLabel.setText("<html><body style='width: 800px;'>" + description + "</body></html>");
                descriptionLabel.setVisible(true);
            } else {
                descriptionLabel.setVisible(false);
            }

            StringBuilder sb = new StringBuilder();
            sb.append(LinkGenerator.getDefault().getLinkedString(object).toHtml().replace("</body>", "").replace("</html>", ""));
            if (object instanceof ITransparency){
                ITransparency transparency = (ITransparency) object;
                if (AdsTransparence.isTransparent(transparency)){
                    sb.append("<i> wrapper </i>");
                }
            }
            final String htmlSignature = sb.toString();
            titleLabel.setText(htmlSignature);
        }
    }
    private final RadixObject object;

    public RollUpPanel(RadixObject object, boolean expand, ApiFilter filter) {
        this(new TopComponent(object, expand), new DefinitionContentProvider(object, filter), object, expand);
    }

    private RollUpPanel(ITopComponent component, IContentProvider contentProvider, RadixObject object, boolean expand) {
        super(component, contentProvider, expand);
        this.object = object;
    }

    public RadixObject getSourceObject() {
        return object;
    }

    private IApiEditor<RadixObject> getEditor() {
        return ((DefinitionContentProvider) getContentProvider()).getEditor();
    }

    @Override
    public EditorState getState() {
        final IApiEditor<RadixObject> editor = getEditor();
        final EditorState state = EditorState.Factory.createComplex(getOpenState());

        if (editor != null) {
            state.putEditorState(getSourceObject(), editor.getState());
        }

        return state;
    }

    private EditorState.OpenState getOpenState() {
        return isExpanded() ? EditorState.OpenState.OPENED : EditorState.OpenState.CLOSED;
    }

    @Override
    public void setEditorState(EditorState state) {
        if (state == null) {
            return;
        }
        if (state.getState() == EditorState.OpenState.CLOSED) {
            collapse();
            return;
        }

        expand();
        final IApiEditor<RadixObject> editor = getEditor();
        if (editor != null) {
            editor.setEditorState(state.getEditorState(getSourceObject()));
        }
    }
}
