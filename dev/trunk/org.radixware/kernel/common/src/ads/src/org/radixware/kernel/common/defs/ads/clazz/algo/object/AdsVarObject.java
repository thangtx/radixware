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

package org.radixware.kernel.common.defs.ads.clazz.algo.object;

import java.util.List;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.RadixObjectIcon;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.common.AdsVisitorProviders;
import org.radixware.kernel.common.defs.ads.type.AdsDefinitionType;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.defs.ads.type.IAdsTypedObject;
import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.Utils;


public class AdsVarObject extends AdsBaseObject implements IAdsTypedObject {

    private AdsTypeDeclaration type = AdsTypeDeclaration.Factory.newInstance(EValType.INT);
    private boolean persistent = true;
    private ValAsStr value = null;

    protected AdsVarObject() {
        this(ObjectFactory.createNodeId(), DEFAULT_NAME);
    }

    protected AdsVarObject(Id id, String name) {
        super(Kind.VAR, id, name);
    }

    protected AdsVarObject(final AdsVarObject node) {
        super(node);
        type = AdsTypeDeclaration.Factory.newCopy(node.type);
        persistent = node.persistent;
        value = node.value;
    }

    protected AdsVarObject(org.radixware.schemas.algo.ScopeDef.Nodes.Node xNode) {
        super(xNode);
        type = AdsTypeDeclaration.Factory.loadFrom(xNode.getType());
        persistent = xNode.getPersistent();
        value = ValAsStr.Factory.loadFrom(xNode.getValue());
    }

    public void setType(AdsTypeDeclaration type) {
        if (!Utils.equals(this.type, type)) {
            this.type = type;
            setEditState(EEditState.MODIFIED);
        }
    }

    public boolean isValPersistent() {
        return persistent;
    }

    public void setValPersistent(boolean persistent) {
        if (!Utils.equals(this.persistent, persistent)) {
            this.persistent = persistent;
            setEditState(EEditState.MODIFIED);
        }
    }

    public ValAsStr getValue() {
        return value;
    }

    public void setValue(ValAsStr value) {
        if (!Utils.equals(this.value, value)) {
            this.value = value;
            setEditState(EEditState.MODIFIED);
        }
    }

    public void setValue(String value) {
        setValue(ValAsStr.Factory.loadFrom(value));
    }

    @Override
    public AdsTypeDeclaration getType() {
        return type;
    }

    @Override
    public boolean isTypeAllowed(EValType type) {
        return type != null;
    }

    @Override
    public boolean isTypeRefineAllowed(EValType type) {
        switch (type) {
            case ARR_INT:
            case ARR_CHAR:
            case ARR_STR:
            case INT:
            case STR:
            case CHAR:
                return true;
            default:
                return false;
        }
    }

    @Override
    public VisitorProvider getTypeSourceProvider(EValType toRefine) {
        if (toRefine != null && toRefine.isEnumAssignableType()) {
            return AdsVisitorProviders.newEnumBasedTypeProvider(toRefine);
        }
        return IAdsTypedObject.TypeProviderFactory.getDefaultTypeSourceProvider(toRefine, getUsageEnvironment());
    }

    @Override
    public RadixIcon getIcon() {
        return RadixObjectIcon.getForValType(type.getTypeId());
    }

    @Override
    public void appendTo(org.radixware.schemas.algo.ScopeDef.Nodes.Node xNode, ESaveMode saveMode) {
        super.appendTo(xNode, saveMode);
        type.appendTo(xNode.addNewType());
        xNode.setPersistent(persistent);
        xNode.setValue(value != null ? value.toString() : null);
    }

    @Override
    public void collectDependences(List<Definition> list) {
        super.collectDependences(list);
        if (type != null) {
            AdsType resolvedType = type.resolve((AdsDefinition) getDefinition()).get();
            if (resolvedType instanceof AdsDefinitionType) {
                Definition def = ((AdsDefinitionType) resolvedType).getSource();
                if (def != null) {
                    list.add(def);
                }
            }
        }
    }
    private static final String TYPE_TITLE = "Variable Node";

    @Override
    public String getTypeTitle() {
        return TYPE_TITLE;
    }
}
