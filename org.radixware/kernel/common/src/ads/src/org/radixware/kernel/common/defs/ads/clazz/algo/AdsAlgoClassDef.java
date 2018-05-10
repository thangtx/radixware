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

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.defs.ClipboardSupport;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObjectIcon;
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.kernel.common.defs.RadixObjects.ContainerChangedEvent;
import org.radixware.kernel.common.defs.RadixObjects.ContainerChangesListener;
import org.radixware.kernel.common.defs.RadixObjects.EChangeType;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.AdsClipboardSupport;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;
import org.radixware.kernel.common.defs.ads.AdsDefinitions;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.IAdsPresentableClass;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.defs.ads.clazz.algo.AdsAlgoClassDef.Param;
import org.radixware.kernel.common.defs.ads.clazz.algo.generation.AdsAlgoWriter;
import org.radixware.kernel.common.defs.ads.clazz.algo.object.*;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityObjectClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AlgoClassPresentations;
import org.radixware.kernel.common.defs.ads.clazz.presentation.ClassPresentations;
import org.radixware.kernel.common.defs.ads.module.AdsSearcher;
import org.radixware.kernel.common.defs.ads.radixdoc.ClassRadixdoc;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.src.WriterUtils;
import org.radixware.kernel.common.defs.ads.type.AdsDefinitionType;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.defs.ads.type.IAdsTypedObject;
import org.radixware.kernel.common.enums.EClassType;
import org.radixware.kernel.common.enums.EDefType;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.EDocGroup;
import org.radixware.kernel.common.enums.EParamDirection;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.radixdoc.DocumentOptions;
import org.radixware.kernel.common.radixdoc.IRadixdocPage;
import org.radixware.kernel.common.radixdoc.RadixdocSupport;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.schemas.adsdef.ClassDefinition;
import org.radixware.schemas.algo.AlgoDef;
import org.radixware.schemas.radixdoc.ContentContainer;
import org.radixware.schemas.radixdoc.Page;
import org.radixware.schemas.radixdoc.Table;


public class AdsAlgoClassDef extends AdsClassDef implements ContainerChangesListener, IAdsPresentableClass {

    public static final class Factory {

        public static AdsAlgoClassDef loadFrom(ClassDefinition classDef) {
            return new AdsAlgoClassDef(classDef);
        }

        public static AdsAlgoClassDef newInstance() {
            return new AdsAlgoClassDef("NewAlgorithmClass");
        }
    }
    public static final AdsTypeDeclaration ID_TYPE = AdsTypeDeclaration.Factory.newPlatformClass(new String(WriterUtils.RADIX_ID_CLASS_NAME));
    public static final String PLATFORM_CLASS_NAME = "org.radixware.kernel.server.types.Algorithm";
    public static final Id PREDEFINED_ID = Id.Factory.loadFrom("pdcAlgorithm_________________");
    public static final Id PROCESS_CLASS_ID = Id.Factory.loadFrom("aecRDXQVFY6PLNRDANMABIFNQAABA");
    public static final Id PROCESS_TYPE_CLASS_ID = Id.Factory.loadFrom("aecXI6CAOIGDBAGJEE6JQEGJME43Q");
    private AdsDefinitions<Param> params = ObjectFactory.createList(this);
    private AdsDefinitions<Var> vars = ObjectFactory.createList(this);

    {
        vars.add(new Var(EXCEPTION_VAR_ID, AdsTypeDeclaration.Factory.newPlatformClass("Throwable")));
        vars.add(new Var(TIME_VAR_ID, AdsTypeDeclaration.Factory.newInstance(EValType.DATE_TIME)));
        vars.add(new Var(PROCESS_VAR_ID));
    }
    private Id replacedId = null;
    private Id replacementId = null;
    private Id processId = null;
    private AdsPage page = null;
    private final transient AlgoClassPresentations presentations;
    private static final AdsTypeDeclaration PDC_ALGO = AdsTypeDeclaration.Factory.newInstance(EValType.USER_CLASS, PREDEFINED_ID);

    protected AdsAlgoClassDef(ClassDefinition xDef) {
        this(xDef, PDC_ALGO);
    }

    protected AdsAlgoClassDef(ClassDefinition xDef, AdsTypeDeclaration superClass) {
        super(xDef);
        AlgoDef xAlgo = xDef.getAlgorithm();
        for (org.radixware.schemas.algo.AlgoDef.Params.Param xParam : xAlgo.getParams().getParamList()) {
            params.add(new Param(xParam));
        }
        presentations = (AlgoClassPresentations) ClassPresentations.Factory.loadFrom(this, xDef.getPresentations());
        page = ObjectFactory.createPage(xAlgo.getPage(), this);
        replacedId = xAlgo.getReplacedId();
        replacementId = xAlgo.getReplacementId();
        processId = xAlgo.getProcessId();
        processId = xAlgo.getProcessId();

        syncAlgoStartMethods();
        params.getContainerChangesSupport().addEventListener(this);
        getInheritance().setSuperClassRef(superClass);

        //   setEditState(EEditState.NONE);
    }

    protected AdsAlgoClassDef(String name) {
        this(name, PDC_ALGO);
    }

    protected AdsAlgoClassDef(String name, AdsTypeDeclaration superClass) {
        super(Id.Factory.newInstance(EDefinitionIdPrefix.ADS_ALGORITHM_CLASS), name);
        presentations = (AlgoClassPresentations) ClassPresentations.Factory.newInstance(this);
        page = ObjectFactory.createPage(this);
        page.add(ObjectFactory.createNode(AdsBaseObject.Kind.START, null, new Point(300, 10)));
        page.add(ObjectFactory.createNode(AdsBaseObject.Kind.RETURN, null, new Point(300, 700)));

        syncAlgoStartMethods();
        params.getContainerChangesSupport().addEventListener(this);
        getInheritance().setSuperClassRef(superClass);
    }

    @Override
    public AlgoClassPresentations getPresentations() {
        return presentations;
    }

    protected AdsAlgoClassDef(AdsAlgoClassDef source) {
        super(source);
        for (Param param : source.params) {
            params.add(new Param(param));
        }

        replacedId = source.getReplacedId();
        replacementId = source.getReplacementId();

        processId = source.getProcessId();
        page = ObjectFactory.createPage(source.getPage(), this);

        syncAlgoStartMethods();
        params.getContainerChangesSupport().addEventListener(this);
        getInheritance().setSuperClassRef(AdsTypeDeclaration.Factory.newInstance(EValType.USER_CLASS, PREDEFINED_ID));
        presentations = (AlgoClassPresentations) ClassPresentations.Factory.newInstance(this);
    }

    private void syncAlgoStartMethods() {
        List<AdsAlgoStartMethodDef.SubNature> subList = new ArrayList<>(
                Arrays.asList(
                AdsAlgoStartMethodDef.SubNature.EXECUTE,
                AdsAlgoStartMethodDef.SubNature.STARTPROCESS,
                AdsAlgoStartMethodDef.SubNature.START));

        List<AdsMethodDef> methods = new ArrayList<>(getMethods().getLocal().list());
        for (int i = 0; i < methods.size(); i++) {
            AdsMethodDef m = methods.get(i);
            if (!(m instanceof AdsAlgoStartMethodDef)) {
                continue;
            }
            AdsAlgoStartMethodDef mf = (AdsAlgoStartMethodDef) m;
            AdsAlgoStartMethodDef.SubNature subNature = AdsAlgoStartMethodDef.SubNature.getForMethod(mf);
            if (subNature != null) { // check subNature
                subList.remove(subNature);
                mf.update();
            } else {
                getMethods().getLocal().remove(m);
            }
        }
        for (AdsAlgoStartMethodDef.SubNature subNature : subList) {
            switch (subNature) {
                case STARTPROCESS:
                    AdsAlgoStartMethodDef startProcess = AdsAlgoStartMethodDef.Factory.newStartProcess(this);
                    getMethods().getLocal().add(startProcess);
                    break;
                case START:
                    AdsAlgoStartMethodDef startProcessType = AdsAlgoStartMethodDef.Factory.newStartTraceProfile(this);
                    getMethods().getLocal().add(startProcessType);
                    break;
                case EXECUTE:
                    AdsAlgoStartMethodDef execute = AdsAlgoStartMethodDef.Factory.newExecute(this);
                    getMethods().getLocal().add(execute);
                    break;
            }
        }
    }

    @Override
    public void onEvent(ContainerChangedEvent e) {
        syncAlgoStartMethods();
    }

    public Id getReplacedId() {
        return replacedId;
    }

    public AdsAlgoClassDef getReplacedDef() {
        if (replacedId != null) {
            return (AdsAlgoClassDef) AdsSearcher.Factory.newAdsClassSearcher(this).findById(replacedId).get();
        }
        return null;
    }

    public void setReplacedId(Id replacedId) {
        if (!Utils.equals(this.replacedId, replacedId)) {
            this.replacedId = replacedId;
            setEditState(EEditState.MODIFIED);
        }
    }

    public Id getReplacementId() {
        return replacementId;
    }

    public AdsAlgoClassDef getReplacementDef() {
        if (replacementId != null) {
            return (AdsAlgoClassDef) AdsSearcher.Factory.newAdsClassSearcher(this).findById(replacementId).get();
        }
        return null;
    }

    public void setReplacementId(Id replacementId) {
        if (!Utils.equals(this.replacementId, replacementId)) {
            this.replacementId = replacementId;
            setEditState(EEditState.MODIFIED);
        }
    }

    public Id getProcessId() {
        return processId != null ? processId : PROCESS_CLASS_ID;
    }

    public AdsEntityObjectClassDef getProcessDef() {
        return (AdsEntityObjectClassDef) AdsSearcher.Factory.newAdsDefinitionSearcher(this).findById(getProcessId()).get();
    }

    public AdsTypeDeclaration getProcessType() {
        return AdsTypeDeclaration.Factory.newInstance(EValType.OBJECT, getProcessId());
    }

    public AdsTypeDeclaration getProcessTypeType() {
        return AdsTypeDeclaration.Factory.newInstance(EValType.OBJECT, AdsAlgoClassDef.PROCESS_TYPE_CLASS_ID);
    }

    public void setProcessId(Id processId) {
        if (!Utils.equals(this.processId, processId)) {
            this.processId = processId;
            syncAlgoStartMethods();
            setEditState(EEditState.MODIFIED);
        }
    }

    public AdsDefinitions<Param> getParams() {
        return params;
    }

    public AdsPage getPage() {
        return page;
    }

    public Long getSubType() {
        return EClassType.ALGORITHM.getValue();
    }

    @Override
    public void appendTo(ClassDefinition xDef, ESaveMode saveMode) {
        super.appendTo(xDef, saveMode);
        AlgoDef xAlgo = xDef.addNewAlgorithm();
        org.radixware.schemas.algo.AlgoDef.Params xParams = xAlgo.addNewParams();
        for (Param param : params) {
            param.appendTo(xParams.addNewParam(), saveMode);
        }
        page.appendTo(xAlgo.addNewPage(), saveMode);
        xAlgo.setReplacedId(replacedId);
        xAlgo.setReplacementId(replacementId);
        xAlgo.setProcessId(processId);
    }

    @Override
    public EClassType getClassDefType() {
        return EClassType.ALGORITHM;
    }

    @Override
    public RadixIcon getIcon() {
        return AdsDefinitionIcon.CLASS_ALGORITHM;
    }

    @Override
    public void visitChildren(IVisitor visitor, VisitorProvider provider) {
        super.visitChildren(visitor, provider);
        vars.visit(visitor, provider);
        params.visit(visitor, provider);
        page.visit(visitor, provider);
        presentations.visit(visitor, provider);
    }

    /*
     * vars
     */
    final public static String EXCEPTION_VAR_NAME = "exception";
    final public static String TIME_VAR_NAME = "time";
    final public static String PROCESS_VAR_NAME = "process";
    final public static Id EXCEPTION_VAR_ID = Id.Factory.loadFrom("glb54X4WUHFBJHJZI4NUILEW7KSEI");
    final public static Id TIME_VAR_ID = Id.Factory.loadFrom("glbEU44533ZX5C5DAFHNZ6XQXWSZM");
    final public static Id PROCESS_VAR_ID = Id.Factory.loadFrom("glbRMA7HHFLDNDTDM4YACHN7OQ3R4");
    final private static HashMap<Id, String> varId2Name = new HashMap<>(3);

    static {
        varId2Name.put(EXCEPTION_VAR_ID, EXCEPTION_VAR_NAME);
        varId2Name.put(TIME_VAR_ID, TIME_VAR_NAME);
        varId2Name.put(PROCESS_VAR_ID, PROCESS_VAR_NAME);
    }

    public static String getVarNameById(Id id) {
        return varId2Name.get(id);
    }

    public class Var extends AdsDefinition implements IAdsTypedObject {

        private AdsTypeDeclaration type;

        public Var(Id id, AdsTypeDeclaration type) {
            super(id, getVarNameById(id));
            this.type = type;
        }

        public Var(Id id) {
            this(id, null);
        }

        @Override
        public AdsTypeDeclaration getType() {
            if (PROCESS_VAR_ID.equals(getId())) {
                return getProcessType();
            }
            return type;
        }

        @Override
        public boolean isTypeAllowed(EValType type) {
            return true;
        }

        @Override
        public boolean isTypeRefineAllowed(EValType type) {
            return false;
        }

        @Override
        public ERuntimeEnvironmentType getUsageEnvironment() {
            return super.getUsageEnvironment();
        }

        @Override
        public VisitorProvider getTypeSourceProvider(EValType toRefine) {
            return IAdsTypedObject.TypeProviderFactory.getDefaultTypeSourceProvider(toRefine, getUsageEnvironment());
        }

        @Override
        public RadixIcon getIcon() {
            AdsTypeDeclaration decl = getType();
            return decl != null && decl.getTypeId() != null ? RadixObjectIcon.getForValType(decl.getTypeId()) : super.getIcon();
        }

        @Override
        public EDefType getDefinitionType() {
            return EDefType.ALGO_VAR;
        }

        @Override
        public void collectDependences(List<Definition> list) {
            super.collectDependences(list);
            AdsType resolvedType = getType().resolve((AdsDefinition) getDefinition()).get();
            if (resolvedType instanceof AdsDefinitionType) {
                Definition def = ((AdsDefinitionType) resolvedType).getSource();
                if (def != null) {
                    list.add(def);
                }
            }
        }
        private static final String TYPE_TITLE = "Variable";

        @Override
        public String getTypeTitle() {
            return TYPE_TITLE;
        }

        @Override
        public ClipboardSupport<Var> getClipboardSupport() {
            return new AdsClipboardSupport<Var>(this) {
//                @Override
//                public boolean canCopy() {
//                    return false;
//                }
                @Override
                protected boolean isIdChangeRequired(RadixObject copyRoot) {
                    return !varId2Name.containsKey(getId());
                }
            };
        }
    };

    /*
     * parameters
     */
    static public class Param extends AdsDefinition implements IAdsTypedObject {

        private EParamDirection direction;
        private AdsTypeDeclaration type;

        public Param(Id id, String name, EParamDirection direction, AdsTypeDeclaration type) {
            super(id, name);
            this.direction = direction;
            this.type = type;
        }

        public Param(String name, EParamDirection direction, AdsTypeDeclaration type) {
            this(ObjectFactory.createParamId(), name, direction, type);
        }

        public Param(final Param param) {
            this(ObjectFactory.createParamId(), param.getName(), param.getDirection(), param.getType());
        }

        public Param(org.radixware.schemas.algo.AlgoDef.Params.Param xParam) {
            super(xParam);
            direction = EParamDirection.getForValue(xParam.getDirection() == null ? EParamDirection.IN.getValue() : xParam.getDirection());
            type = AdsTypeDeclaration.Factory.loadFrom(xParam.getType());
        }

        @Override
        public EDefType getDefinitionType() {
            return EDefType.ALGO_PARAM;
        }

        public void appendTo(org.radixware.schemas.algo.AlgoDef.Params.Param xParam, ESaveMode saveMode) {
            super.appendTo(xParam, saveMode);
            xParam.setDirection(direction.getValue());
            type.appendTo(xParam.addNewType());
        }

        @Override
        public AdsTypeDeclaration getType() {
            return type;
        }

        @Override
        public boolean setName(String name) {
            boolean result = super.setName(name);
            if (result) {
                fireEvent();
            }
            return result;

        }

        public boolean setType(AdsTypeDeclaration type) {
            if (!Utils.equals(this.type, type)) {
                this.type = type;
                setEditState(EEditState.MODIFIED);
                fireEvent();
                return true;
            }
            return false;
        }

        public EParamDirection getDirection() {
            return direction;
        }

        public boolean setDirection(EParamDirection direction) {
            if (!Utils.equals(this.direction, direction)) {
                this.direction = direction;
                setEditState(EEditState.MODIFIED);
                fireEvent();
                return true;
            }
            return false;
        }

        @SuppressWarnings("unchecked")
        private void fireEvent() {
            RadixObjects container = (RadixObjects) getContainer();
            container.getContainerChangesSupport().fireEvent(new ContainerChangedEvent(this, EChangeType.MODIFY));
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
            AdsTypeDeclaration decl = getType();
            return decl != null && decl.getTypeId() != null ? RadixObjectIcon.getForValType(decl.getTypeId()) : super.getIcon();
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
        private static final String TYPE_TITLE = "Parameter";

        @Override
        public String getTypeTitle() {
            return TYPE_TITLE;
        }

        @Override
        public ClipboardSupport<Param> getClipboardSupport() {
            return new AdsClipboardSupport<Param>(this) {
//                @Override
//                public boolean canCopy() {
//                    return false;
//                }
                @Override
                protected boolean isIdChangeRequired(RadixObject copyRoot) {
                    return false;
                }
            };
        }

        @Override
        public EDocGroup getDocGroup() {
            return EDocGroup.PROPERTY;
        }
    }

    private class AlgoJavaSourceSupport extends ClassJavaSourceSupport {

        @Override
        @SuppressWarnings("unchecked")
        public CodeWriter getCodeWriter(UsagePurpose purpose) {
            return new AdsAlgoWriter(this, AdsAlgoClassDef.this, purpose);
        }
    }

    @Override
    public JavaSourceSupport getJavaSourceSupport() {
        return new AlgoJavaSourceSupport();
    }

    private class AdsAlgoClassClipboardSupport extends AdsClipboardSupport<AdsAlgoClassDef> {

        public AdsAlgoClassClipboardSupport() {
            super(AdsAlgoClassDef.this);
        }

        @Override
        protected XmlObject copyToXml() {
            org.radixware.schemas.adsdef.ClassDefinition xClassDef = org.radixware.schemas.adsdef.ClassDefinition.Factory.newInstance();
            AdsAlgoClassDef.this.appendTo(xClassDef, ESaveMode.NORMAL);
            return xClassDef;
        }

        @Override
        protected AdsAlgoClassDef loadFrom(XmlObject xmlObject) {
            org.radixware.schemas.adsdef.ClassDefinition xClassDef = (org.radixware.schemas.adsdef.ClassDefinition) xmlObject;
            return AdsAlgoClassDef.Factory.loadFrom(xClassDef);
        }
    }

    @Override
    public ClipboardSupport<? extends AdsAlgoClassDef> getClipboardSupport() {
        return new AdsAlgoClassClipboardSupport();
    }

    @Override
    public ERuntimeEnvironmentType getUsageEnvironment() {
        return ERuntimeEnvironmentType.SERVER;
    }

    @Override
    public Set<ERuntimeEnvironmentType> getTypeUsageEnvironments() {
        return EnumSet.of(ERuntimeEnvironmentType.SERVER);
    }

    @Override
    public boolean isSaveable() {
        return true;
    }
    private static final String TYPE_TITLE = "Algorithm Class";

    @Override
    public String getTypeTitle() {
        return TYPE_TITLE;
    }

    @Override
    public RadixdocSupport<? extends RadixObject> getRadixdocSupport() {
        return new RadixdocSupport<AdsClassDef>(this) {
            @Override
            public IRadixdocPage document(Page page, DocumentOptions options) {
                return new ClassRadixdoc(getSource(), page, options) {
                    @Override
                    protected void writeClassDefInfo(ContentContainer overview, Table overviewTable) {
                        AdsAlgoClassDef algoCl = (AdsAlgoClassDef) source;
                        getClassWriter().addStr2MslIdRow(overviewTable, "Title", algoCl.getLocalizingBundleId(), algoCl.getTitleId());
                        getClassWriter().addStr2RefRow(overviewTable, "Replacement", algoCl.getReplacementDef(), source);
                        getClassWriter().addStr2RefRow(overviewTable, "Replaced", algoCl.getReplacedDef(), source);
                        getClassWriter().addStr2RefRow(overviewTable, "Process", algoCl.getProcessDef(), source);

                        if (!algoCl.getParams().isEmpty()) {
                            Table paramsTable = getClassWriter().setBlockCollapsibleAndAddTable(overview.addNewBlock(), "Parameters", "Name", "Column", "Type");
                            for (AdsAlgoClassDef.Param param : algoCl.getParams()) {
                                getClassWriter().addAllStrRow(paramsTable, param.getName(), param.getDirection().getName(), param.getType().toString());
                            }
                        }
                    }
                };
            }
        };
    }
}
