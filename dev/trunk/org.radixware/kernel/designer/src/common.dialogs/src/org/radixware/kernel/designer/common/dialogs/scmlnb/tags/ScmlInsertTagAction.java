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

package org.radixware.kernel.designer.common.dialogs.scmlnb.tags;

import org.radixware.kernel.designer.common.dialogs.scmlnb.library.*;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.dds.DdsDefinition;
import org.radixware.kernel.common.jml.Jml;
import org.radixware.kernel.common.scml.Scml;
import org.radixware.kernel.common.scml.Scml.Tag;
import org.radixware.kernel.common.sqml.Sqml;
import org.radixware.kernel.designer.common.dialogs.scmlnb.ScmlEditor;
import org.radixware.kernel.designer.common.dialogs.scmlnb.ScmlEditorPane;


public abstract class ScmlInsertTagAction extends ScmlToolBarAction {

    public ScmlInsertTagAction(String name, ScmlEditor editor) {
        super(name, editor);
        PropertyChangeListener listener = new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getNewValue() instanceof Boolean) {
                    updateState();
                }
            }
        };
        editor.getPane().addPropertyChangeListener(ScmlEditorPane.CURRENT_POSITION_EDITABLE, listener);
    }

    @Override
    public final void actionPerformed(ActionEvent e, ScmlEditorPane pane) {
        if (pane.isCurrentPositionEditable()) {
            List<Tag> tags = createTags();
            if (tags != null && tags.size() > 0) {
                pane.replaceSelection("");
                boolean first = true;
                for (Tag tag : tags) {
                    if (first) {
                        first = false;
                    } else {
                        pane.insertString(", ");
                    }
                    insertTag(tag, pane);
                }
            }
        }
        pane.requestFocusInWindow();
    }

    protected void insertTag(Tag tag, ScmlEditorPane pane) {
        pane.insertTag(tag);
    }

    @Override
    public void updateState() {
        if (getPane().isCurrentPositionEditable()) {
            setEnabled(true);
        } else {
            setEnabled(false);
        }
    }

    protected abstract List<Tag> createTags();

    public static boolean isDdsSqml(Scml scml) {
        return (scml.getDefinition() instanceof DdsDefinition);
    }

    public static boolean isAdsSqml(Scml scml) {
        return (scml.getDefinition() instanceof AdsDefinition);
    }

    public static boolean isJml(Scml scml) {
        if (scml instanceof Jml) {
            return true;
        }
        return false;
    }

    public static boolean isSqml(Scml scml) {
        if (scml instanceof Sqml) {
            return true;
        }
        return false;
    }
}
