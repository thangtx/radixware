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

import org.radixware.kernel.common.defs.dds.DdsModule;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.designer.api.editors.OpenMode;
import org.radixware.kernel.designer.api.ApiFilter;
import org.radixware.kernel.designer.api.editors.components.MembersBrick;
import java.awt.GridBagConstraints;
import org.radixware.kernel.designer.api.IApiEditor;
import org.radixware.kernel.designer.api.IApiEditorFactory;
import org.radixware.kernel.designer.api.editors.Brick;
import org.radixware.kernel.designer.api.editors.BrickFactory;
import static org.radixware.kernel.designer.api.editors.BrickFactory.MEMBERS;
import org.radixware.kernel.designer.common.annotations.registrators.ApiEditorFactoryRegistration;

class DdsModuleApiEditor<T extends DdsModule> extends DefinitionApiEditor<T> {

    public DdsModuleApiEditor(T source) {
        super(source);
    }

    @ApiEditorFactoryRegistration
    public static final class Factory implements IApiEditorFactory<DdsModule> {

        @Override
        public IApiEditor<DdsModule> newInstance(DdsModule classDef) {
            return new DdsModuleApiEditor<>(classDef);
        }
    }

    @Override
    public boolean isEmbedded() {
        return false;
    }

    private static final class DdsModuleMembersBreak extends MembersBrick<DdsModule> {

        public DdsModuleMembersBreak(DdsModule source, GridBagConstraints constraints) {
            super(source, constraints);
        }
        
        @Override
        protected void beforeBuild(OpenMode mode, final ApiFilter filter) {
            beforeBuild(getSource().getModelManager().findModel(), mode, filter);
        }
    }

    private static final class DdsModuleBrickFactory extends BrickFactory {

        @Override
        public Brick<? extends RadixObject> create(int key, RadixObject object, GridBagConstraints constraints) {

            switch (key) {
                case MEMBERS:
                    return new DdsModuleApiEditor.DdsModuleMembersBreak((DdsModule) object, constraints);
            }
            return super.create(key, object, constraints);
        }
    }

    @Override
    protected BrickFactory createFactory() {
        return new DdsModuleBrickFactory();
    }

}