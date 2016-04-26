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

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObjectIcon;
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.AdsDefinitions;
import org.radixware.kernel.common.enums.EDefType;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.defs.ads.clazz.algo.AdsAlgoClassDef;
import org.radixware.kernel.common.defs.ads.clazz.algo.generation.AdsIncludeWriter;
import org.radixware.kernel.common.defs.ads.module.AdsSearcher;
import org.radixware.kernel.common.defs.ads.src.IJavaSource;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.type.AdsDefinitionType;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.defs.ads.type.IAdsTypedObject;
import org.radixware.kernel.common.jml.IJmlSource;
import org.radixware.kernel.common.jml.Jml;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.utils.Utils;


public class AdsIncludeObject extends AdsBaseObject implements IJavaSource, IJmlSource {

    private Id algoId = null;
    private AdsDefinitions<Param> params = ObjectFactory.createList(this);
    private Jml preExecute;

    protected AdsIncludeObject() {
        this(ObjectFactory.createNodeId(), DEFAULT_NAME);
    }

    protected AdsIncludeObject(Id id, String name) {
        super(Kind.INCLUDE, id, name);
        preExecute = Jml.Factory.newInstance(this, "PreExecute");
    }

    protected AdsIncludeObject(final AdsIncludeObject node) {
        super(node);
        this.algoId = node.algoId;
        preExecute = Jml.Factory.newCopy(this, node.preExecute);
        for (Param param : node.params) {
            params.add(new Param(param));
        }
    }

    protected AdsIncludeObject(org.radixware.schemas.algo.ScopeDef.Nodes.Node xNode) {
        super(xNode);
        algoId = xNode.getAlgoId();
        preExecute = Jml.Factory.loadFrom(this, xNode.getPreExecute(), "Source");
        for (org.radixware.schemas.algo.ScopeDef.Nodes.Node.Params.Param xParam : xNode.getParams().getParamList()) {
            params.add(new Param(xParam));
        }
    }

    @Override
    public boolean isSourcePin(AdsPin pin) {
        return pins.indexOf(pin) > 0;
    }

    @Override
    public boolean isTargetPin(AdsPin pin) {
        return pins.indexOf(pin) == 0;
    }

    public Jml getPreExecute() {
        return preExecute;
    }

    @Override
    public Jml getSource(String name) {
        return getPreExecute();
    }

    public void setPreExecute(Jml preExecute) {
        if (!Utils.equals(this.preExecute, preExecute)) {
            this.preExecute = preExecute;
            setEditState(EEditState.MODIFIED);
        }
    }

    public RadixObjects<Param> getParams() {
        return params;
    }

    public Id getAlgoId() {
        return algoId;
    }
    
    public AdsAlgoClassDef getAlgoDef() {
        if (algoId == null) {
            return null;
        }
        return (AdsAlgoClassDef) AdsSearcher.Factory.newAdsDefinitionSearcher(getOwnerDefinition()).findById(algoId).get();
//        try {
//            return (AdsAlgoClassDef) AdsSearcher.Factory.newAdsDefinitionSearcher(getOwnerDefinition()).findById(algoId);
//        } catch (Exception e) {
//            return null;
//        }
    }

    public void setAlgoDef(AdsAlgoClassDef algoDef) {
        if (!Utils.equals(getAlgoDef(), algoDef)) {
            algoId = (algoDef == null) ? null : algoDef.getId();
            setEditState(EEditState.MODIFIED);
        }
    }

    @Override
    public void collectDependences(List<Definition> list) {
        super.collectDependences(list);
        AdsAlgoClassDef algoDef = getAlgoDef();
        if (algoDef != null) {
            list.add(algoDef);
        }
    }

    boolean equalsParamsTo(AdsAlgoClassDef algoDef) {
        List<Param> l1 = params.list();
        List<AdsAlgoClassDef.Param> l2 = algoDef.getParams().list();

        Iterator<Param> e1 = l1.iterator();
        Iterator<AdsAlgoClassDef.Param> e2 = l2.iterator();

        while (e1.hasNext() && e2.hasNext()) {
            Param o1 = e1.next();
            AdsAlgoClassDef.Param o2 = e2.next();
            if (!o1.equalsTo(o2)) {
                return false;
            }
        }
        return !(e1.hasNext() || e2.hasNext());
    }

    @Override
    public void sync() {
        AdsAlgoClassDef algoDef = getAlgoDef();
        syncOrigPins(algoDef != null ? algoDef.getPage() : null, true);

        if (algoDef != null) {
            if (equalsParamsTo(algoDef)) {
                return;
            }
        }

        HashMap<Id, Id> id2id = new HashMap<Id, Id>();
        for (Param p : params) {
            id2id.put(p.getOrigId(), p.getId());
        }

        params.clear();
        if (algoDef != null) {
            for (AdsAlgoClassDef.Param p : algoDef.getParams()) {
                Id id = id2id.containsKey(p.getId()) ? id2id.get(p.getId()) : ObjectFactory.createBlockParamId();
                params.add(new Param(id, p.getName(), p.getId(), p.getType()));
            }
        }
    }

    private class IncludeJavaSourceSupport extends JavaSourceSupport {

        public IncludeJavaSourceSupport() {
            super(AdsIncludeObject.this);
        }

        @Override
        public CodeWriter getCodeWriter(UsagePurpose purpose) {
            return new AdsIncludeWriter(this, AdsIncludeObject.this, purpose);
        }
    }

    @Override
    public JavaSourceSupport getJavaSourceSupport() {
        return new IncludeJavaSourceSupport();
    }

    @Override
    public void appendTo(org.radixware.schemas.algo.ScopeDef.Nodes.Node xNode, ESaveMode saveMode) {
        super.appendTo(xNode, saveMode);
        xNode.setAlgoId(algoId);
        preExecute.appendTo(xNode.addNewPreExecute(), saveMode);
        org.radixware.schemas.algo.ScopeDef.Nodes.Node.Params xParams = xNode.addNewParams();
        for (Param param : params) {
            param.appendTo(xParams.addNewParam(), saveMode);
        }
    }

    @Override
    public void visitChildren(IVisitor visitor, VisitorProvider provider) {
        super.visitChildren(visitor, provider);
        params.visit(visitor, provider);
        preExecute.visit(visitor, provider);
    }
    private static final String TYPE_TITLE = "Include Node";

    @Override
    public String getTypeTitle() {
        return TYPE_TITLE;
    }

    static public class Param extends AdsDefinition implements IAdsTypedObject {

        private Id origId = null;
        private AdsTypeDeclaration type;

        public Param(Id id, String name, Id origId, AdsTypeDeclaration type) {
            super(id, name);
            this.origId = origId;
            this.type = type;
        }

        public Param(final Param param) {
            super(ObjectFactory.createBlockParamId(), param.getName());
            origId = param.origId;
            type = AdsTypeDeclaration.Factory.newCopy(param.type);
        }

        public Param(org.radixware.schemas.algo.ScopeDef.Nodes.Node.Params.Param xParam) {
            super(xParam);
            origId = xParam.getOrigId();
            type = AdsTypeDeclaration.Factory.loadFrom(xParam.getType());
        }

        public void appendTo(org.radixware.schemas.algo.ScopeDef.Nodes.Node.Params.Param xParam, ESaveMode saveMode) {
            super.appendTo(xParam, saveMode);
            xParam.setOrigId(origId);
            type.appendTo(xParam.addNewType());
        }

        public Id getOrigId() {
            return origId;
        }

        @Override
        public AdsTypeDeclaration getType() {
            return type;
        }

        @Override
        public boolean isTypeAllowed(EValType type) {
            return type != null && type.isAllowedForMethodParameter();
        }

        @Override
        public boolean isTypeRefineAllowed(EValType type) {
            return false;
        }

        @Override
        public VisitorProvider getTypeSourceProvider(EValType toRefine) {
            return IAdsTypedObject.TypeProviderFactory.getDefaultTypeSourceProvider(toRefine, getUsageEnvironment());
        }

        @Override
        public RadixIcon getIcon() {
            return type != null && type.getTypeId() != null ? RadixObjectIcon.getForValType(type.getTypeId()) : super.getIcon();
        }

        @Override
        public void collectDependences(List<Definition> list) {
            super.collectDependences(list);
            AdsType resolvedType = type.resolve((AdsDefinition) getDefinition()).get();
            if (resolvedType instanceof AdsDefinitionType) {
                Definition def = ((AdsDefinitionType) resolvedType).getSource();
                if (def != null) {
                    list.add(def);
                }
            }
        }

        @Override
        public EDefType getDefinitionType() {
            return EDefType.INCLUDE_NODE_PARAM;
        }
        private static final String TYPE_TITLE = "Include Node";

        @Override
        public String getTypeTitle() {
            return TYPE_TITLE;
        }

        boolean equalsTo(AdsAlgoClassDef.Param param) {
            assert param != null;
            return Utils.equals(origId, param.getId()) && Utils.equals(getName(), param.getName()) && Utils.equals(type, param.getType());
        }
    }
}
