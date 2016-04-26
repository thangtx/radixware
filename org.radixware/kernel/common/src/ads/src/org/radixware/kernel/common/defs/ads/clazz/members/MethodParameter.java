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

package org.radixware.kernel.common.defs.ads.clazz.members;

import org.radixware.kernel.common.defs.IDefinition;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodDef.MethodValue;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.adsdef.ParameterDeclaration;


public class MethodParameter extends MethodValue implements IDefinition {

    public static final class Factory {

        public static final MethodParameter newInstance() {
            return newInstance("arg", AdsTypeDeclaration.Factory.newInstance(EValType.INT));
        }

        public static final MethodParameter newInstance(String name, AdsTypeDeclaration type) {
            return new MethodParameter(name, null, null, type, false);
        }

        public static final MethodParameter newInstance(Id id, String name, AdsTypeDeclaration type) {
            return new MethodParameter(id, name, null, null, type, false);
        }

        public static final MethodParameter newInstance(String name, AdsTypeDeclaration type, boolean isVariable) {
            return new MethodParameter(name, null, null, type, isVariable);
        }

        public static final MethodParameter newInstance(MethodParameter source) {
            return new MethodParameter(source.getName(), source.getDescription(), null,
                    AdsTypeDeclaration.Factory.newCopy(source.getType()), source.isVariable());
        }

        public static MethodParameter newInstance(String name, String description, Id descriptionId, AdsTypeDeclaration decl, boolean isVariable) {
            return new MethodParameter(name, description, descriptionId, decl, isVariable);

        }

        @Deprecated
        public static MethodParameter loadFrom(ParameterDeclaration declaration) {
            return loadFrom(null, declaration);
        }

        public static MethodParameter loadFrom(AdsMethodParameters context, ParameterDeclaration declaration) {
            return new MethodParameter(context, declaration);
        }

//        @Deprecated
//        public static final MethodParameter newTemporaryInstance(MethodParameter source) {
//            final MethodParameter parameter = new MethodParameter(source.getName(), source.getDescription(), null,
//                    AdsTypeDeclaration.Factory.newCopy(source.getType()), source.isVariable());
//            parameter.setContainer(source.getContainer());
//            return parameter;
//        }
        public static final MethodParameter newTemporaryInstance(RadixObject container) {
            MethodParameter p = newInstance();
            p.setContainer(container);
            return p;
        }

        public static final MethodParameter newTemporaryInstance(RadixObject container, String name, AdsTypeDeclaration type, boolean isVariable) {
            final MethodParameter parameter = newInstance(name, type, isVariable);
            parameter.setContainer(container);
            return parameter;
        }
    }
    private boolean isVariable = false;
    private Id id;

    protected MethodParameter(String name, String description, Id descriptionId, AdsTypeDeclaration decl, boolean isVariable) {
        super(decl, name, description, descriptionId);
        this.id = Id.Factory.newInstance(EDefinitionIdPrefix.ADS_METHOD_PARAMETER);
        this.isVariable = isVariable;
    }

    protected MethodParameter(Id id, String name, String description, Id descriptionId, AdsTypeDeclaration decl, boolean isVariable) {
        super(decl, name, description, descriptionId);
        this.id = Id.Factory.changePrefix(id, EDefinitionIdPrefix.ADS_METHOD_PARAMETER);
        this.isVariable = isVariable;
    }

    protected MethodParameter(AdsMethodParameters context, ParameterDeclaration declaration) {
        super(AdsTypeDeclaration.Factory.loadFrom(declaration.getType()), declaration.getName(), declaration.getDescription(), declaration.getDescriptionId());
        if (declaration.getId() == null) {
            //this.id = declaration.getId() != null ? declaration.getId() : Id.Factory.newInstance(EDefinitionIdPrefix.ADS_METHOD_PARAMETER);
            if (context == null) {
                this.id = Id.Factory.newInstance(EDefinitionIdPrefix.ADS_METHOD_PARAMETER);
            } else {
                int selfIndex = context.size();
                Id tmp = Id.Factory.changePrefix(context.getDefinition().getId(), EDefinitionIdPrefix.ADS_METHOD_PARAMETER);
                this.id = Id.Factory.loadFrom(tmp.toString() + "_" + String.valueOf(selfIndex));
            }
        } else {
            this.id = declaration.getId();
        }
        this.isVariable = declaration.getVariable();
    }

    /**
     * Adds parameter to xml representation
     */
    public void appendTo(ParameterDeclaration xDef) {
        xDef.setName(getName());
        String desc = getDescription();
        if (desc != null && !desc.isEmpty()) {
            xDef.setDescription(getDescription());
        }
        getType().appendTo(xDef.addNewType());

        if (isVariable()) {
            xDef.setVariable(true);
        }

        if (isDescriptionIdChanged()) {
            xDef.setDescriptionId(getDescriptionId());
        }
        xDef.setId(getId());
    }

    @Override
    public Id getId() {
        return id;
    }

    public boolean isVariable() {
        return isVariable;
    }

    public void setVariable(boolean isVariable) {
        if (this.isVariable != isVariable) {
            this.isVariable = isVariable;
            setEditState(EEditState.MODIFIED);
        }
    }
}
