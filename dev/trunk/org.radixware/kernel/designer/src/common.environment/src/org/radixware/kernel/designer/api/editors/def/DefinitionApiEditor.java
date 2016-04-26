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

package org.radixware.kernel.designer.api.editors.def;

import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.designer.api.IApiEditor;
import org.radixware.kernel.designer.api.IApiEditorFactory;
import org.radixware.kernel.designer.common.annotations.registrators.ApiEditorFactoryRegistration;


class DefinitionApiEditor<T extends Definition> extends RadixObjectApiEditor<T> {

    @ApiEditorFactoryRegistration
    public static final class Factory implements IApiEditorFactory<Definition> {

        @Override
        public IApiEditor<Definition> newInstance(Definition classDef) {
            return new DefinitionApiEditor<>(classDef);
        }
    }

    public DefinitionApiEditor(T source) {
        super(source);
    }

    @Override
    public boolean isEmbedded() {
        return false;
    }
}
