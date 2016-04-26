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

package org.radixware.kernel.designer.api.editors;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.designer.api.ApiFilter;
import org.radixware.kernel.designer.api.EditorState;
import org.radixware.kernel.designer.api.editors.components.IStatableEditor;


public class ApiEditorModel<T extends RadixObject> implements IStatableEditor {

    private final List<Brick> bricks = new ArrayList<>();
    protected final T source;
    private JComponent view;
    
    public ApiEditorModel(T source) {
        this.source = source;
    }

    protected void beforeBuild(OpenMode mode, ApiFilter filter) {
    }

    public final JComponent getView(OpenMode mode, ApiFilter filter) {
        beforeBuild(mode, filter);
        view = buildView(mode, filter);
        return view;
    }
    
    protected JComponent buildView(OpenMode mode, ApiFilter filter) {
        final JPanel panel = new JPanel(new GridBagLayout());

        int row = 0;
        for (final Brick brick : bricks) {
            brick.constraints.gridy = row;

            setDefaultInsets(brick.constraints);

            panel.add(brick.getView(mode, filter), brick.constraints);

            row += brick.constraints.gridheight > 0 ? brick.constraints.gridheight : 1;
        }

        addFoot(panel, row);

        return panel;
    }

    private void addFoot(JPanel panel, int row) {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.weighty = 1;
        constraints.weightx = 1;
        constraints.gridy = row;
        constraints.gridx = 0;

        panel.add(new JLabel(), constraints);
    }

    public List<Brick> getBricks() {
        return bricks;
    }

    protected void setDefaultInsets(GridBagConstraints constraints) {
        if (constraints.insets == null) {
            constraints.insets = new Insets(4, 4, 4, 4);
        } else {
            constraints.insets.left = Math.max(constraints.insets.left, 4);
            constraints.insets.top = Math.max(constraints.insets.top, 4);
            constraints.insets.right = Math.max(constraints.insets.right, 4);
            constraints.insets.bottom = Math.max(constraints.insets.bottom, 4);
        }
    }

    public EditorState.OpenState getOpenState() {
        return EditorState.OpenState.OPENED;
    }

    @Override
    public void setEditorState(EditorState state) {
        if (state != null && state.isComplex()) {
            for (final Brick brick : getBricks()) {
                
                final EditorState editorState;
                if (brick.isEditor()) {
                    editorState = state.getEditorState(brick.getSource());
                } else {
                    editorState = state.getControllState(brick.getTag());
                }
                if (editorState != null) {
                    brick.setEditorState(editorState);
                }
            }
        }
    }

    @Override
    public EditorState getState() {

        if (!bricks.isEmpty()) {
            final EditorState state = EditorState.Factory.createComplex(getOpenState());

            for (final Brick brick : bricks) {
                if (brick.isEditor()) {
                    state.putEditorState(brick.getSource(), brick.getState());
                } else {
                    state.putControllState(brick.getTag(), brick.getState());
                }
            }

            return state;
        }

        return EditorState.Factory.createSimple(getOpenState());
    }

    public final T getSource() {
        return source;
    }

    public final JComponent getCurrentView() {
        return view;
    }
}
