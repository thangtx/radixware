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

package org.radixware.kernel.designer.ads.editors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.radixware.kernel.designer.common.dialogs.scmlnb.library.ScmlToolBarAction;
import org.radixware.kernel.designer.common.editors.jml.JmlEditor;


public class CustomizableJmlEditor extends JmlEditor {

    public interface IActionInstaller {

        void install(CustomizableJmlEditor editor);
    }

    private final List<ScmlToolBarAction> customActions = new ArrayList<>();

    public CustomizableJmlEditor() {
        super();
    }

    public CustomizableJmlEditor(IActionInstaller installer) {
        super();

        if (installer != null) {
            installer.install(this);
            update();
        }
    }

    public CustomizableJmlEditor(ScmlToolBarAction... actions) {
        super();

        if (actions != null) {
            customActions.addAll(Arrays.asList(actions));
        }

        update();
    }

    @Override
    protected ScmlToolBarAction[] createScmlToolBarActions() {

        // REWORK : yet not initialized
        if (customActions == null || customActions.isEmpty()) {
            return super.createScmlToolBarActions();
        }

        final ScmlToolBarAction[] superActions = super.createScmlToolBarActions();
        final List<ScmlToolBarAction> actions = new ArrayList<>(customActions);
        if (superActions != null) {
            actions.addAll(Arrays.asList(superActions));
        }

        return actions.toArray(new ScmlToolBarAction[0]);
    }

    public final void addCustomAction(ScmlToolBarAction action) {
        customActions.add(action);
        update();
    }

    public final void addCustomActions(ScmlToolBarAction... actions) {
        for (final ScmlToolBarAction action : actions) {
            customActions.add(action);
        }
        update();
    }

    public final void addCustomActions(Collection<ScmlToolBarAction> actions) {
        customActions.addAll(actions);
        update();
    }
}
