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

import java.awt.GridBagConstraints;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodDef;
import org.radixware.kernel.common.defs.ads.clazz.members.MethodReturnValue;
import org.radixware.kernel.designer.api.ApiFilter;
import org.radixware.kernel.designer.api.IApiEditor;
import org.radixware.kernel.designer.api.IApiEditorFactory;
import org.radixware.kernel.designer.api.editors.Brick;
import org.radixware.kernel.designer.api.editors.BrickFactory;
import static org.radixware.kernel.designer.api.editors.BrickFactory.MEMBERS;
import org.radixware.kernel.designer.api.editors.OpenMode;
import org.radixware.kernel.designer.api.editors.SimpleBrick;
import org.radixware.kernel.designer.api.editors.components.MembersBrick;
import org.radixware.kernel.designer.api.editors.components.OverviewBrick;
import org.radixware.kernel.designer.common.annotations.registrators.ApiEditorFactoryRegistration;
import org.radixware.kernel.designer.common.dialogs.components.description.DescriptionEditor;
import org.radixware.kernel.designer.common.dialogs.components.description.MixedDescriptionWrapper;


public class AdsMethodApiEditor extends AdsDefinitionApiEditor<AdsMethodDef> {

    private static final class MethodMembersBreak extends MembersBrick<AdsMethodDef> {

        public MethodMembersBreak(AdsMethodDef source, GridBagConstraints constraints) {
            super(source, constraints);
        }

        @Override
        protected void beforeBuild(OpenMode mode, ApiFilter filter) {
            addMembers("Parameters", getSource().getProfile().getParametersList().list(), filter, false);
            addMembers("Thrown exceptions", getSource().getProfile().getThrowsList().list(), filter, false);
        }
    }

    private static final class MethodOverviewBrick extends OverviewBrick<AdsMethodDef> {

        public MethodOverviewBrick(AdsMethodDef object, GridBagConstraints constraints, BrickFactory factory) {
            super(object, constraints, factory);
        }

        @Override
        protected void buildDescription() {
            super.buildDescription();
            
            final AdsMethodDef method = getSource();
            if (method.isConstructor()) {
                return;
            }

            final MethodReturnValue returnValue = method.getProfile().getReturnValue();
            if (returnValue == null || returnValue.getType().isVoid()) {
                return;
            }

            final DescriptionEditor descriptionEditor = new DescriptionEditor("Return value description");
            
            final MixedDescriptionWrapper wrapper = MixedDescriptionWrapper.Factory.tryNewInstance(returnValue);
            if (wrapper != null){
               descriptionEditor.open(wrapper);
            } else {
                descriptionEditor.tryOpen(returnValue);
            }

            final GridBagConstraints constr = new GridBagConstraints();
            constr.gridx = 0;
            constr.fill = GridBagConstraints.BOTH;
            constr.anchor = GridBagConstraints.CENTER;

            getBricks().add(new SimpleBrick(source, descriptionEditor, constr, DESCRIPTION, null));
        }
    }

    private static final class MethodBrickFactory extends BrickFactory {

        @Override
        public Brick<? extends RadixObject> create(int key, RadixObject object, GridBagConstraints constraints) {

            switch (key) {
                case MEMBERS:
                    return new MethodMembersBreak((AdsMethodDef) object, constraints);
                case OVERVIEW:
                    return new MethodOverviewBrick((AdsMethodDef) object, constraints, this);
            }
            return super.create(key, object, constraints);
        }
    }

    public AdsMethodApiEditor(AdsMethodDef source) {
        super(source);
    }

    @ApiEditorFactoryRegistration
    public static final class Factory implements IApiEditorFactory<AdsMethodDef> {

        @Override
        public IApiEditor<AdsMethodDef> newInstance(AdsMethodDef method) {
            return new AdsMethodApiEditor(method);
        }
    }

    @Override
    protected BrickFactory createFactory() {
        return new MethodBrickFactory();
    }
}
