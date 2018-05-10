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

import org.radixware.kernel.common.defs.dds.DdsModelDef;
import java.awt.GridBagConstraints;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.dds.DdsDefinition;
import org.radixware.kernel.designer.api.IApiEditor;
import org.radixware.kernel.designer.api.IApiEditorFactory;
import org.radixware.kernel.designer.api.editors.Brick;
import org.radixware.kernel.designer.api.editors.BrickFactory;
import static org.radixware.kernel.designer.api.editors.BrickFactory.OVERVIEW;
import org.radixware.kernel.designer.api.editors.components.OverviewBrick;
import org.radixware.kernel.designer.common.annotations.registrators.ApiEditorFactoryRegistration;

class DdsDefinitionApiEditor<T extends DdsDefinition> extends DefinitionApiEditor<T> {

    @ApiEditorFactoryRegistration
    public static final class Factory implements IApiEditorFactory<DdsDefinition> {

        @Override
        public IApiEditor<DdsDefinition> newInstance(DdsDefinition classDef) {
            return new DdsDefinitionApiEditor<>(classDef);
        }
    }

    public DdsDefinitionApiEditor(T source) {
        super(source);
    }

    @Override
    public boolean isEmbedded() {
        return !getSource().isTopLevelDefinition();
    }

    private static final class DdsDefinitionOverviewBrick extends OverviewBrick<DdsDefinition> {

        public DdsDefinitionOverviewBrick(DdsDefinition object, GridBagConstraints constraints, BrickFactory factory) {
            super(object, constraints, factory);
        }

        @Override
        protected RadixObject getLocation() {
            RadixObject location = super.getLocation();

            if (location instanceof DdsModelDef) {
                location = location.getOwnerDefinition();
            }

            return location;
        }
    }

    private static final class DdsDefinitionBrickFactory extends BrickFactory {

        @Override
        public Brick<? extends RadixObject> create(int key, RadixObject object, GridBagConstraints constraints) {

            switch (key) {
                case OVERVIEW:
                    return new DdsDefinitionOverviewBrick((DdsDefinition) object, constraints, this);
            }
            return super.create(key, object, constraints);
        }
    }

    @Override
    protected BrickFactory createFactory() {
        return new DdsDefinitionApiEditor.DdsDefinitionBrickFactory();
    }
}
