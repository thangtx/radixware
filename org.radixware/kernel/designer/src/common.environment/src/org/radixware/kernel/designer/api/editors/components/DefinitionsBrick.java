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

import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.Border;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.designer.api.ApiFilter;
import org.radixware.kernel.designer.api.EditorState;
import org.radixware.kernel.designer.api.editors.Brick;
import org.radixware.kernel.designer.api.editors.OpenMode;
import org.radixware.kernel.designer.common.dialogs.names.NamingService;


public class DefinitionsBrick<T extends RadixObject> extends Brick<T> {

    private static final class DefinitionListPanel extends JPanel {

        int itemCount = 0;

        public DefinitionListPanel() {
            setLayout(new GridBagLayout());
        }

        void addEditor(JComponent editor) {
            if (editor != null) {
                GridBagConstraints c = new GridBagConstraints();
                c.fill = GridBagConstraints.BOTH;
                c.gridx = 0;
                c.gridy = itemCount++;
                c.weightx = 1;
                c.anchor = GridBagConstraints.LINE_START;
                add(editor, c);
            }
        }
    }
    
    private final List<RadixObject> apiMembers;
    private final boolean embedded;

    private static GridBagConstraints getConstraints() {
        final GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        return c;
    }

    public DefinitionsBrick(T source, String tag, List<RadixObject> apiMembers, boolean isEmbedded) {
        super(source, getConstraints(), tag);
        this.apiMembers = apiMembers;
        this.embedded = isEmbedded;
    }

    private JComponent createRow(RadixObject member, ApiFilter filter) {
        if (embedded) {
            return createEmbeddedItem(member, filter);
        } else {
            return createLinkItem(member);
        }
    }

    private JComponent createEmbeddedItem(RadixObject member, ApiFilter filter) {
        return new RollUpPanel(member, false, filter);
    }

    private JComponent createLinkItem(RadixObject member) {
        final JPanel panel = new JPanel() {
            @Override
            protected void paintBorder(Graphics g) {
                final Border border = getBorder();
                if (border != null) {
                    border.paintBorder(this, g, 0, getHeight() - 1, getWidth(), getHeight());
                }
            }
        };

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        final RefLabel ref = new RefLabel(member,
                NamingService.getDefault().getHtmlName(member),
                member.getToolTip(), member.getIcon());

        ref.setBorder(BorderFactory.createEmptyBorder(2, 0, 2, 0));
        panel.add(ref);

        final String description = DescriptionService.getInstance().getDescription(member);
        if (description != null && !description.isEmpty()) {
            JLabel descriptionLabel = new JLabel();
            descriptionLabel.setText("<html><body style='width: 800px;'>" + description + "</body></html>");
            panel.add(descriptionLabel);
        }
        panel.setBorder(UIManager.getBorder("TitledBorder.border"));
        return panel;
    }

    @Override
    protected void beforeBuild(OpenMode mode, ApiFilter filter) {
        super.beforeBuild(mode, filter);

        for (final RadixObject member : apiMembers) {
            getBricks().add(new DefinitionBrick(member, createRow(member, filter)));
        }
    }
    
    BorderedRollUpPanel definitions;

    @Override
    protected JComponent buildView(OpenMode mode, ApiFilter filter) {

        final DefinitionListPanel panel = new DefinitionListPanel();
        for (Brick brick : getBricks()) {
            panel.addEditor(brick.getView(mode, filter));
        }

        definitions = new BorderedRollUpPanel(panel, getTag(), null, true);
        return definitions;
    }

    @Override
    public void setEditorState(EditorState state) {
        if (state == null || definitions == null) {
            return;
        }
        if (state.getState() == EditorState.OpenState.OPENED) {
            definitions.expand();
            super.setEditorState(state);
        } else {
            definitions.collapse();
        }
    }

    @Override
    public EditorState.OpenState getOpenState() {
        if (definitions == null) {
            return EditorState.OpenState.OPENED;
        }
        return definitions.isExpanded() ? EditorState.OpenState.OPENED : EditorState.OpenState.CLOSED;
    }
}
