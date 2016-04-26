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

package org.radixware.kernel.designer.common.general.editors;

import org.radixware.kernel.designer.common.annotations.registrators.EditorFactoryRegistration;
import org.radixware.kernel.designer.common.general.extensions.ExtensionsManager;



class EditorFactoriesManager extends ExtensionsManager<IEditorFactory> {

    public EditorFactoriesManager() {
        super(EditorFactoryRegistration.SECTION_NAME, IEditorFactory.class);
    }
    private static final EditorFactoriesManager EDITOR_FACTORIES_MANAGER_INSTANCE = new EditorFactoriesManager();

    public static EditorFactoriesManager getDefault() {
        return EDITOR_FACTORIES_MANAGER_INSTANCE;
    }
}
