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

package org.radixware.kernel.designer.common.dialogs.scmlnb;

import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import org.radixware.kernel.common.scml.Scml;


public abstract class ScmlEditorRegistry {

    private static final DefaultScmlEditorRegistry registry = new DefaultScmlEditorRegistry();

    protected ScmlEditorRegistry() {
        //protected
    }

    public abstract int getLastScmlOffset(Scml scml);

    public abstract void setLastScmlOffset(Scml scml, int offset);

    public abstract ScmlEditorPane getEditorPane(Scml scml);

    public abstract void setEditorPane(Scml scml, ScmlEditorPane pane);

    public static ScmlEditorRegistry getDefault() {
        return registry;
    }

    private static class DefaultScmlEditorRegistry extends ScmlEditorRegistry {

        private final Map<Scml, Integer> scml2offset = Collections.synchronizedMap(new WeakHashMap<Scml, Integer>());
        private final Map<Scml, ScmlEditorPane> scml2editorPane = Collections.synchronizedMap(new WeakHashMap<Scml, ScmlEditorPane>());

        @Override
        public int getLastScmlOffset(Scml scml) {
            Integer offset = scml2offset.get(scml);
            if (offset == null) {
                return 0;
            }
            return offset;
        }

        @Override
        public void setLastScmlOffset(Scml scml, int offset) {
            scml2offset.put(scml, offset);
        }

        @Override
        public ScmlEditorPane getEditorPane(Scml scml) {
            return scml2editorPane.get(scml);
        }

        @Override
        public void setEditorPane(Scml scml, ScmlEditorPane editorPane) {
            scml2editorPane.put(scml, editorPane);
        }
    }
}
