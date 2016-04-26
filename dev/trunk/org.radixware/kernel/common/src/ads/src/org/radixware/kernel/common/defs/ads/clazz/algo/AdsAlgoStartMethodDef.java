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

package org.radixware.kernel.common.defs.ads.clazz.algo;

import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.clazz.AdsMethodGroup;
import org.radixware.kernel.common.defs.ads.clazz.algo.generation.AdsStartMethodWriter;
import org.radixware.kernel.common.defs.ads.clazz.members.*;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodThrowsList.ThrowsListItem;
import org.radixware.schemas.adsdef.AbstractMethodDefinition;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport.UsagePurpose;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.enums.EMethodNature;
import org.radixware.kernel.common.enums.EParamDirection;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.schemas.adsdef.AbstractMethodDefinition;


public class AdsAlgoStartMethodDef extends AdsUserMethodDef {

    public enum SubNature {

        STARTPROCESS("startProcess"),
        START("start"),
        EXECUTE("execute");
        final private String name;

        private SubNature(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public static SubNature getForName(final String name) {
            for (SubNature sn : SubNature.values()) {
                if (Utils.equals(sn.name, name)) {
                    return sn;
                }
            }
            return null;
        }

        public static SubNature getForMethod(final AdsAlgoStartMethodDef method) {
            return getForName(method.getName());
        }
        /*
         public static SubNature getForMethod(final AdsAlgoStartMethodDef method) {
         Profile profile = method.getProfile();
         AdsMethodParameters params = profile.getParametersList();
         if ("start".equals(method.getName())) {
         assert params.size() > 0: "params list empty";
         return params.get(0).getName().equals("process") ?
         STARTPROCESS : START;
         }
         return EXECUTE;
         }
         */
    }
    private static final Id ID_START_PROCESS = Id.Factory.loadFrom("mthAlgoStartProcess__________");
    private static final Id ID_START_TRACE_PROFILE = Id.Factory.loadFrom("mthAlgoStartTraceProfile_____");
    private static final Id ID_EXECUTE = Id.Factory.loadFrom("mthAlgoExecute_______________");

    public static class Factory {

        public static AdsAlgoStartMethodDef newStartProcess(AdsAlgoClassDef algo) {
            return new AdsAlgoStartMethodDef(ID_START_PROCESS, algo, SubNature.STARTPROCESS);
        }

        public static AdsAlgoStartMethodDef newStartTraceProfile(AdsAlgoClassDef algo) {
            return new AdsAlgoStartMethodDef(ID_START_TRACE_PROFILE, algo, SubNature.START);
        }

        public static AdsAlgoStartMethodDef newExecute(AdsAlgoClassDef algo) {
            return new AdsAlgoStartMethodDef(ID_EXECUTE, algo, SubNature.EXECUTE);
        }
    }
    private SubNature subNature;

    private AdsAlgoStartMethodDef(Id id, AdsAlgoClassDef algo, SubNature subNature) {
        super(id, subNature.getName());
        getProfile().getAccessFlags().setStatic(true);
        this.setReflectiveCallable(true);
        this.subNature = subNature;
        update(algo);
    }

    public AdsAlgoStartMethodDef(AbstractMethodDefinition xMethod) {
        super(xMethod);
        this.subNature = SubNature.getForMethod(this);
        this.setReflectiveCallable(true);
    }

    public void update() {
        update((AdsAlgoClassDef) getOwnerClass());
    }
    private static final Id clientDataParamId = Id.Factory.loadFrom("mprClientData________________");
    private static final Id processParamId = Id.Factory.loadFrom("mprProcess___________________");
    private static final Id processTypeParamId = Id.Factory.loadFrom("mprProcessType___________________");

    private void update(AdsAlgoClassDef algo) {
        Profile profile = getProfile();
        AdsMethodParameters params = profile.getParametersList();
        MethodReturnValue ret = profile.getReturnValue();
        AdsMethodThrowsList th = profile.getThrowsList();

        AdsTypeDeclaration processType = algo.getProcessType();
        AdsTypeDeclaration processTypeType = algo.getProcessTypeType();

        params.clear();
        RadixObject container = getContainer();

        Id oldId = getId();
        AdsMethodGroup group = algo.getMethodGroup().findGroupByMethodId(getId());

        algo.getMethods().getLocal().unregister(this);
        setContainerNoFire(null);

        switch (subNature) {
            case EXECUTE:
                params.add(MethodParameter.Factory.newInstance(clientDataParamId, "clientData", AdsTypeDeclaration.Factory.newPlatformClass("Object")));
                ret.setType(AdsTypeDeclaration.Factory.voidType());
                th.clear();
                th.add(ThrowsListItem.Factory.newInstance(AdsTypeDeclaration.Factory.newPlatformClass("org.radixware.kernel.common.exceptions.AppException"), "", null));
                setId(ID_EXECUTE);
                break;
            case STARTPROCESS:
                params.add(MethodParameter.Factory.newInstance(processParamId, "process", processType));
                params.add(MethodParameter.Factory.newInstance(clientDataParamId, "clientData", AdsTypeDeclaration.Factory.newPlatformClass("Object")));
                ret.setType(AdsTypeDeclaration.Factory.newInstance(EValType.BOOL));
                setId(ID_START_PROCESS);
                break;
            case START:
                params.add(MethodParameter.Factory.newInstance(processTypeParamId, "type", processTypeType));
                params.add(MethodParameter.Factory.newInstance(clientDataParamId, "clientData", AdsTypeDeclaration.Factory.newPlatformClass("Object")));
                ret.setType(processType);
                th.clear();
                th.add(ThrowsListItem.Factory.newInstance(AdsTypeDeclaration.Factory.newPlatformClass("org.radixware.kernel.common.exceptions.AppException"), "", null));
                setId(ID_START_TRACE_PROFILE);
                break;
        }
        setOverwrite(algo.isOverwrite());
        setContainerNoFire(container);
        algo.getMethods().getLocal().register(this);
        if (group != null) {
            group.removeMemberId(oldId);
            group.addMember(this);
        }
        for (AdsAlgoClassDef.Param p : algo.getParams()) {
            AdsTypeDeclaration type = p.getType();
            EParamDirection direction = p.getDirection();
            if (direction.equals(EParamDirection.OUT) || direction.equals(EParamDirection.BOTH)) {
                type = type.toArrayType(type.getArrayDimensionCount()+1);
            }
            params.add(MethodParameter.Factory.newInstance(p.getId(), p.getName(), type));
        }
    }

    protected class StartMethodJavaSourceSupport extends MethodJavaSourceSupport {

        public StartMethodJavaSourceSupport(AdsMethodDef method) {
            super(method);
        }

        @Override
        public CodeWriter getCodeWriter(UsagePurpose purpose) {
            return new AdsStartMethodWriter(this, AdsAlgoStartMethodDef.this, purpose);
        }
    }

    @Override
    public JavaSourceSupport getJavaSourceSupport() {
        return new StartMethodJavaSourceSupport(this);
    }

    @Override
    public EMethodNature getNature() {
        return EMethodNature.ALGO_START;
    }

    public SubNature getSubNature() {
        return subNature;
    }
    private static final String TYPE_TITLE = "Algorithm Start Method";

    @Override
    public String getTypeTitle() {
        return TYPE_TITLE;
    }
}
