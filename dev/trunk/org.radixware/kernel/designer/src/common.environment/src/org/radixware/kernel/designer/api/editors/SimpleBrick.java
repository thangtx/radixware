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
import java.util.Collections;
import java.util.List;
import javax.swing.JComponent;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.designer.api.ApiFilter;
import org.radixware.kernel.designer.api.EditorState;
import org.radixware.kernel.designer.api.editors.components.IStatableEditor;


public class SimpleBrick<T extends RadixObject> extends Brick<T> {
    protected final JComponent component;
    protected final IStatableEditor stateAgent;
    
    public SimpleBrick(T source, JComponent component, GridBagConstraints constraints, String tag, IStatableEditor stateAgent) {
        super(source, constraints, tag);
        this.component = component;
        this.stateAgent = stateAgent;
    }

    @Override
    protected JComponent buildView(OpenMode mode, ApiFilter filter) {
        return component;
    }

    @Override
    public List<Brick> getBricks() {
        return Collections.EMPTY_LIST;
    }

    @Override
    protected void setDefaultInsets(GridBagConstraints constraints) {
        //
    }

    @Override
    public void setEditorState(EditorState state) {
        if (stateAgent != null) {
            stateAgent.setEditorState(state);
        } else if (component instanceof IStatableEditor) {
            IStatableEditor editor = (IStatableEditor)component;
            editor.setEditorState(state);
        }
    }

    @Override
    public EditorState getState() {
        if (stateAgent != null) {
            return stateAgent.getState();
        } else if (component instanceof IStatableEditor) {
            IStatableEditor editor = (IStatableEditor)component;
            return editor.getState();
        }
        return null;
    }
}
