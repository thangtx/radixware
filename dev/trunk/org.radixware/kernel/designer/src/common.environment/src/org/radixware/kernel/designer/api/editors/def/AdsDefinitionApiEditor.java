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

import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.designer.api.IApiEditor;
import org.radixware.kernel.designer.api.IApiEditorFactory;
import org.radixware.kernel.designer.api.editors.DefaultApiEditor;
import org.radixware.kernel.designer.common.annotations.registrators.ApiEditorFactoryRegistration;


class AdsDefinitionApiEditor<T extends AdsDefinition> extends DefinitionApiEditor<T> {

    @ApiEditorFactoryRegistration
    public static final class Factory implements IApiEditorFactory<AdsDefinition> {

        @Override
        public IApiEditor<AdsDefinition> newInstance(AdsDefinition classDef) {
            return new AdsDefinitionApiEditor<>(classDef);
        }
    }

    public AdsDefinitionApiEditor(T source) {
        super(source);
    }

    @Override
    public boolean isEmbedded() {
        return !getSource().isTopLevelDefinition();
    }
}
