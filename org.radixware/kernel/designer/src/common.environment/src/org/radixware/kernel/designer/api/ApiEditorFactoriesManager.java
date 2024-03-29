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

package org.radixware.kernel.designer.api;

import org.radixware.kernel.designer.common.annotations.registrators.ApiEditorFactoryRegistration;
import org.radixware.kernel.designer.common.general.editors.IEditorFactory;
import org.radixware.kernel.designer.common.general.extensions.ExtensionsManager;


public class ApiEditorFactoriesManager extends ExtensionsManager<IApiEditorFactory> {

    public ApiEditorFactoriesManager() {
        super(ApiEditorFactoryRegistration.SECTION_NAME, IApiEditorFactory.class);
    }
    private static final ApiEditorFactoriesManager API_EDITOR_FACTORIES_MANAGER_INSTANCE = new ApiEditorFactoriesManager();

    public static ApiEditorFactoriesManager getDefault() {
        return API_EDITOR_FACTORIES_MANAGER_INSTANCE;
    }
}
