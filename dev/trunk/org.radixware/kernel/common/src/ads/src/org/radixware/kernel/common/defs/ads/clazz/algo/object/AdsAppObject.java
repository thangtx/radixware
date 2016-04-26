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

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObjectIcon;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;
import org.radixware.kernel.common.defs.ads.AdsDefinitions;

import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.algo.AdsAlgoClassDef;
import org.radixware.kernel.common.defs.ads.clazz.algo.generation.AdsAppWriter;
import org.radixware.kernel.common.defs.ads.localization.AdsMultilingualStringDef;
import org.radixware.kernel.common.defs.ads.module.AdsSearcher;
import org.radixware.kernel.common.defs.ads.src.IJavaSource;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.type.AdsDefinitionType;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.defs.ads.type.IAdsTypedObject;
import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.enums.EAccess;
import org.radixware.kernel.common.enums.EDefType;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.EDwfFormClerkAutoSelect;
import org.radixware.kernel.common.enums.EDwfFormPriority;
import org.radixware.kernel.common.enums.EDwfFormSubmitVariant;
import org.radixware.kernel.common.enums.ENamingPolicy;
import org.radixware.kernel.common.enums.EPersoCommImportance;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.jml.IJmlSource;
import org.radixware.kernel.common.jml.Jml;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.common.defs.ads.clazz.algo.generation.AppUtils;
import org.radixware.kernel.common.defs.localization.ILocalizedDef;
import org.radixware.kernel.common.enums.ERestriction;


public class AdsAppObject extends AdsBaseObject implements IJavaSource, IJmlSource {

    public static final String PRE_EXECUTE_NAME = "PreExecute";
    public static final String POST_EXECUTE_NAME = "PostExecute";
    public static final String OVERDUE_EXECUTE_NAME = "OverdueExecute";
    private String clazz;
    private AdsDefinitions<Prop> props = ObjectFactory.createList(this);
    private Jml preExecute;
    private Jml postExecute;
    private Jml onOverdue;
    public static final String APP_PATH = "org.radixware.kernel.server.algo.";
    public static final AdsTypeDeclaration ID_TYPE = AdsAlgoClassDef.ID_TYPE;
    public static final String DIALOG_CREATOR_CLASS_NAME = APP_PATH + "AppBlockDialogCreatorExecutor";
    public static final String DIALOG_DUPLICATOR_CLASS_NAME = APP_PATH + "AppBlockDialogDuplicatorExecutor";
    public static final String DOC_MANAGER_CLASS_NAME = APP_PATH + "AppBlockDocManagerCreatorExecutor";
    public static final String EDITOR_FORM_CLASS_NAME = APP_PATH + "AppBlockEditorFormCreatorExecutor";
    public static final String NET_FORM_CLASS_NAME = APP_PATH + "AppBlockNetPort";
    public static final String PERSO_COM_CLASS_NAME = "adcW2GCG7MB2XOBDNF5ABIFNQAABA";
    public static final String REPORT_GENERATOR_CLASS_NAME = APP_PATH + "AppBlockReportGenerator";
    public static final String SELECTOR_FORM_CLASS_NAME = APP_PATH + "AppBlockSelectorFormCreatorExecutor";
    public static final String WAIT_CLASS_NAME = APP_PATH + "AppBlockWait";

    protected AdsAppObject(String clazz) {
        this(ObjectFactory.createNodeId(), DEFAULT_NAME, clazz);
    }

    protected AdsAppObject(Id id, String name, String clazz) {
        super(Kind.APP, id, name);
        this.clazz = clazz;
        preExecute = Jml.Factory.newInstance(this, PRE_EXECUTE_NAME);
        postExecute = Jml.Factory.newInstance(this, POST_EXECUTE_NAME);
        onOverdue = Jml.Factory.newInstance(this, OVERDUE_EXECUTE_NAME);
        pins.add(new AdsPin()); // entry
        pins.add(new AdsPin()); // leave
    }

    protected AdsAppObject(final AdsAppObject node) {
        super(node);
        this.clazz = node.clazz;
        preExecute = Jml.Factory.newCopy(this, node.preExecute);
        postExecute = Jml.Factory.newCopy(this, node.postExecute);
        onOverdue = Jml.Factory.newCopy(this, node.onOverdue);
        for (Prop prop : node.props) {
            props.add(new Prop(prop));
        }
    }

    protected AdsAppObject(org.radixware.schemas.algo.ScopeDef.Nodes.Node xNode) {
        super(xNode);
        clazz = xNode.getClazz();
        preExecute = Jml.Factory.loadFrom(this, xNode.getPreExecute(), PRE_EXECUTE_NAME);
        postExecute = Jml.Factory.loadFrom(this, xNode.getPostExecute(), POST_EXECUTE_NAME);
        if (postExecute == null) {
            postExecute = Jml.Factory.newInstance(this, POST_EXECUTE_NAME);
        }
        onOverdue = Jml.Factory.loadFrom(this, xNode.getOnOverdue(), OVERDUE_EXECUTE_NAME);
        if (onOverdue == null) {
            onOverdue = Jml.Factory.newInstance(this, OVERDUE_EXECUTE_NAME);
        }
        for (org.radixware.schemas.algo.ScopeDef.Nodes.Node.Props.Prop xProp : xNode.getProps().getPropList()) {
            props.add(new Prop(xProp));
        }
    }

    public String getClazz() {
        return clazz;
    }

    public Jml getPreExecute() {
        return preExecute;
    }

    public Jml getPostExecute() {
        return postExecute;
    }

    public Jml getOnOverdue() {
        return onOverdue;
    }

    @Override
    public Jml getSource(String name) {
        if (name == null) {
            return null;
        }
        if (name.equals(PRE_EXECUTE_NAME)) {
            return getPreExecute();
        }
        if (name.equals(POST_EXECUTE_NAME)) {
            return getPostExecute();
        }
        if (name.equals(OVERDUE_EXECUTE_NAME)) {
            return getOnOverdue();
        }
        return null;
    }

    public void setPreExecute(Jml preExecute) {
        if (!Utils.equals(this.preExecute, preExecute)) {
            this.preExecute = preExecute;
            setEditState(EEditState.MODIFIED);
        }
    }

    public void setPostExecute(Jml postExecute) {
        if (!Utils.equals(this.postExecute, postExecute)) {
            this.postExecute = postExecute;
            setEditState(EEditState.MODIFIED);
        }
    }

    public void setOnOverdue(Jml onOverdue) {
        if (!Utils.equals(this.onOverdue, onOverdue)) {
            this.onOverdue = onOverdue;
            setEditState(EEditState.MODIFIED);
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

    @Override
    public void appendTo(org.radixware.schemas.algo.ScopeDef.Nodes.Node xNode, ESaveMode saveMode) {
        super.appendTo(xNode, saveMode);
        xNode.setClazz(clazz);
        preExecute.appendTo(xNode.addNewPreExecute(), saveMode);
        postExecute.appendTo(xNode.addNewPostExecute(), saveMode);
        onOverdue.appendTo(xNode.addNewOnOverdue(), saveMode);
        org.radixware.schemas.algo.ScopeDef.Nodes.Node.Props xProps = xNode.addNewProps();
        for (Prop prop : props) {
            prop.appendTo(xProps.addNewProp(), saveMode);
        }
    }

    public Prop getPropByName(String name) {
        if (name != null) {
            for (Prop prop : props) {
                if (name.equals(prop.getName())) {
                    return prop;
                }
            }
        }
        return null;
    }

    public Prop getPropBySourceId(Id sourceId) {
        if (sourceId != null) {
            for (Prop prop : props) {
                if (sourceId.equals(prop.getSourceId())) {
                    return prop;
                }
            }
        }
        return null;
    }

    public Prop getPropById(Id id) {
        return props.findById(id);
    }

    public AdsDefinitions<Prop> getProps() {
        return props;
    }
    public static final int OVERRIDE_SOURCE_ID = 1;
    public static final int OVERRIDE_MODE = 2;
    public static final int OVERRIDE_TYPE = 4;
    public static final int OVERRIDE_PERSISTENT = 8;
    public static final int OVERRIDE_VALUE = 16;

    public void registerProp(Prop prop, int ovr) {
        Prop p = getPropByName(prop.getName());
        if (p != null) {
            if ((ovr & OVERRIDE_SOURCE_ID) != 0) {
                p.setSourceId(prop.getSourceId());
            }
            if ((ovr & OVERRIDE_MODE) != 0) {
                p.setMode(prop.getMode());
            }
            if ((ovr & OVERRIDE_TYPE) != 0) {
                p.setType(prop.getType());
            }
            if ((ovr & OVERRIDE_PERSISTENT) != 0) {
                p.setPersistent(prop.getPersistent());
            }
            if ((ovr & OVERRIDE_VALUE) != 0) {
                p.setValue(prop.getValue());
            }
        } else {
            props.add(prop);
        }
    }

    public void registerProp(Prop prop) {
        registerProp(prop, OVERRIDE_SOURCE_ID | OVERRIDE_MODE | OVERRIDE_TYPE | OVERRIDE_PERSISTENT);
    }

    public void unregisterProp(Prop prop) {
        props.remove(prop);
    }

    @Override
    public void visitChildren(IVisitor visitor, VisitorProvider provider) {
        super.visitChildren(visitor, provider);
        props.visit(visitor, provider);
        preExecute.visit(visitor, provider);
        postExecute.visit(visitor, provider);
        onOverdue.visit(visitor, provider);
    }

    @Override
    public RadixIcon getIcon() {
        return AdsDefinitionIcon.WORKFLOW.calcIcon(getClazz());
    }

    private class AppJavaSourceSupport extends JavaSourceSupport {

        public AppJavaSourceSupport() {
            super(AdsAppObject.this);
        }

        @Override
        public CodeWriter getCodeWriter(UsagePurpose purpose) {
            return new AdsAppWriter(this, AdsAppObject.this, purpose);
        }
    }

    @Override
    public JavaSourceSupport getJavaSourceSupport() {
        return new AppJavaSourceSupport();
    }
    private static final String TYPE_TITLE = "Applied node";

    @Override
    public String getTypeTitle() {
        return TYPE_TITLE;
    }

    public void setUserProps(List<Prop> userProps) {
        HashSet<Id> oldSourceIds = new HashSet<Id>();
        HashSet<Id> newSourceIds = new HashSet<Id>();

        for (Prop prop : props) {
            if (prop.getSourceId() != null) {
                oldSourceIds.add(prop.getSourceId());
            }
        }

        for (Prop prop : userProps) {
            newSourceIds.add(prop.getSourceId());
        }

        if (oldSourceIds.equals(newSourceIds)) {
            for (Prop p : userProps) {
                Prop prop = getPropBySourceId(p.getSourceId());
                prop.setName(p.getName());
                prop.setMode(p.getMode());
                prop.setType(p.getType());
                prop.setPersistent(p.getPersistent());
                prop.setValue(p.getValue());
            }
            return;
        }

        // clear user properties
        HashMap<Id, Id> sourceId2id = new HashMap<Id, Id>();
        for (int i = 0; i < props.size();) {
            AdsAppObject.Prop prop = props.get(i);
            if (prop.getSourceId() != null) {
                sourceId2id.put(prop.getSourceId(), prop.getId());
                props.remove(i);
            } else {
                i++;
            }
        }

        // register user properties
        for (Prop prop : userProps) {
            Id id = sourceId2id.get(prop.getSourceId());
            if (id != null) {
                registerProp(new Prop(id, prop.getName(), prop.getSourceId(), prop.getMode(), prop.getType(), prop.getPersistent(), prop.getValue()));
            } else {
                registerProp(prop);
            }
        }

        setEditState(EEditState.MODIFIED);
    }

    @Override
    public void collectDependences(List<Definition> list) {
        super.collectDependences(list);
        final AdsClassDef app = getUserDef();
        if (app != null) {
            list.add(app);
        }
    }

    static public class Prop extends AdsDefinition implements IAdsTypedObject, ILocalizedDef {

        final static public int PRIVATE = 1;
        final static public int PUBLIC = 2;
        final static public int SETTING = 4;
        private Id sourceId;
        private int mode;
        private AdsTypeDeclaration type;
        private boolean persistent;
        private ValAsStr value;

        public Prop(Id id, String name, Id sourceId, int mode, AdsTypeDeclaration type, boolean persistent, ValAsStr value) {
            super(id, name);
            this.sourceId = sourceId;
            this.mode = mode;
            this.type = type;
            this.persistent = persistent;
            this.value = value;
        }

        public Prop(String name, Id sourceId, int mode, AdsTypeDeclaration type, boolean persistent, ValAsStr value) {
            this(ObjectFactory.createBlockParamId(), name, sourceId, mode, type, persistent, value);
        }

        public Prop(String name, Id sourceId, int mode, AdsTypeDeclaration type, boolean persistent, String value) {
            this(name, sourceId, mode, type, persistent, ValAsStr.Factory.loadFrom(value));
        }

        public Prop(String name, Id sourceId, int mode, AdsTypeDeclaration type, boolean persistent) {
            this(name, sourceId, mode, type, persistent, (ValAsStr) null);
        }

        public Prop(final Prop prop) {
            super(ObjectFactory.createBlockParamId(), prop.getName());
            sourceId = prop.sourceId;
            mode = prop.mode;
            type = AdsTypeDeclaration.Factory.newCopy(prop.type);
            persistent = prop.persistent;
            value = prop.value;
        }

        public Prop(org.radixware.schemas.algo.ScopeDef.Nodes.Node.Props.Prop xProp) {
            super(xProp);
            sourceId = xProp.getSourceId();
            mode = xProp.getMode();
            type = AdsTypeDeclaration.Factory.loadFrom(xProp.getType());
            persistent = xProp.getPersistent();
            value = ValAsStr.Factory.loadFrom(xProp.getValue());
        }

        @Override
        public AdsTypeDeclaration getType() {
            return type;
        }

        public void setType(AdsTypeDeclaration type) {
            if (!AdsTypeDeclaration.equals(this, this.type, type)) {
                this.type = type;
                setEditState(EEditState.MODIFIED);
            }
        }

        public Id getSourceId() {
            return sourceId;
        }

        public void setSourceId(Id sourceId) {
            if (!Utils.equals(this.sourceId, sourceId)) {
                this.sourceId = sourceId;
                setEditState(EEditState.MODIFIED);
            }
        }

        public int getMode() {
            return mode;
        }

        public void setMode(int mode) {
            if (!Utils.equals(this.mode, mode)) {
                this.mode = mode;
                setEditState(EEditState.MODIFIED);
            }
        }

        public boolean getPersistent() {
            return persistent;
        }

        public void setPersistent(boolean persistent) {
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
        public boolean isTypeAllowed(EValType type) {
            return type != null;
        }

        @Override
        public boolean isTypeRefineAllowed(EValType type) {
            return false;
        }

        @Override
        public RadixIcon getIcon() {
            return type != null && type.getTypeId() != null ? RadixObjectIcon.getForValType(type.getTypeId()) : super.getIcon();
        }

        @Override
        public VisitorProvider getTypeSourceProvider(EValType toRefine) {
            return IAdsTypedObject.TypeProviderFactory.getDefaultTypeSourceProvider(toRefine, getUsageEnvironment());
        }

        public void appendTo(org.radixware.schemas.algo.ScopeDef.Nodes.Node.Props.Prop xProp, ESaveMode saveMode) {
            super.appendTo(xProp, saveMode);
            xProp.setSourceId(sourceId);
            xProp.setMode(mode);
            type.appendTo(xProp.addNewType());
            xProp.setPersistent(persistent);
            xProp.setValue(value != null ? value.toString() : null);
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
            if (sourceId != null) {
                AdsDefinition def = AdsSearcher.Factory.newAdsDefinitionSearcher(this).findById(sourceId).get();
                if (def != null) {
                    list.add(def);
                }
            }
            List<AdsMultilingualStringDef> stringDefs = findLocalizedStrings();
            if (stringDefs != null) {
                list.addAll(stringDefs);
            }
        }

        @Override
        public EDefType getDefinitionType() {
            return EDefType.APP_NODE_PROP;
        }
        private static final String TYPE_TITLE = "Applied Node Parameter";

        @Override
        public String getTypeTitle() {
            return TYPE_TITLE;
        }

        private void collectMapMlStringIds(Collection<MultilingualStringInfo> ids) {
            if (!AppUtils.SUBMITVARIANT_STR_MAP_TYPE.equals(type) || value == null) {
                return;
            }

            final Map<EDwfFormSubmitVariant, String> map = org.radixware.kernel.common.defs.ads.clazz.algo.generation.AppUtils.parseAsStrMap(value.toString());
            if (map == null) {
                return;
            }

            for (Map.Entry<EDwfFormSubmitVariant, String> e : map.entrySet()) {
                if (e.getValue() == null || !e.getValue().startsWith(EDefinitionIdPrefix.ADS_LOCALIZED_STRING.getValue())) {
                    continue;
                }

                final EDwfFormSubmitVariant key = e.getKey();
                final Id id = Id.Factory.loadFrom(e.getValue());

                ids.add(new MultilingualStringInfo(this) {
                    @Override
                    public Id getId() {
                        return id;
                    }

                    @Override
                    public void updateId(Id newId) {
                        if (newId != null) {
                            Map<EDwfFormSubmitVariant, String> map = null;
                            if (value != null) {
                                map = org.radixware.kernel.common.defs.ads.clazz.algo.generation.AppUtils.parseAsStrMap(value.toString());
                            }
                            if (map == null) {
                                map = new HashMap<EDwfFormSubmitVariant, String>();
                            }
                            map.put(key, newId.toString());
                            value = ValAsStr.Factory.loadFrom(org.radixware.kernel.common.defs.ads.clazz.algo.generation.AppUtils.mergeAsStrMap(map));
                        }
                        setEditState(EEditState.MODIFIED);
                    }

                    @Override
                    public EAccess getAccess() {
                        return getAccessMode();
                    }

                    @Override
                    public String getContextDescription() {
                        return "Applied Node Parameter Value";
                    }

                    @Override
                    public boolean isPublished() {
                        return Prop.this.isPublished();
                    }
                });
            }
        }

        private void collectValMlStringIds(Collection<MultilingualStringInfo> ids) {
            if (!AdsAlgoClassDef.ID_TYPE.equals(type) || value == null) {
                return;
            }

            final Id id = Id.Factory.loadFrom(value.toString());
            if (id == null || id.getPrefix() != EDefinitionIdPrefix.ADS_LOCALIZED_STRING) {
                return;
            }

            ids.add(new MultilingualStringInfo(this) {
                @Override
                public Id getId() {
                    return id;
                }

                @Override
                public void updateId(Id newId) {
                    if (newId == null) {
                        value = null;
                    } else {
                        value = ValAsStr.Factory.loadFrom(newId.toString());
                    }
                    setEditState(EEditState.MODIFIED);
                }

                @Override
                public EAccess getAccess() {
                    return getAccessMode();
                }

                @Override
                public String getContextDescription() {
                    return "Applied Node Parameter Value";
                }

                @Override
                public boolean isPublished() {
                    return Prop.this.isPublished();
                }
            });
        }

        @Override
        public void collectUsedMlStringIds(Collection<MultilingualStringInfo> ids) {
            collectMapMlStringIds(ids);
            collectValMlStringIds(ids);
        }

        public List<AdsMultilingualStringDef> findLocalizedStrings() {
            if (value == null) {
                return null;
            }

            final String v = value.toString();
            final List<AdsMultilingualStringDef> list = new LinkedList<AdsMultilingualStringDef>();

            if (AdsAlgoClassDef.ID_TYPE.equals(type)) {
                final Id id = Id.Factory.loadFrom(v);
                if (id.getPrefix() == EDefinitionIdPrefix.ADS_LOCALIZED_STRING) {
                    final AdsMultilingualStringDef stringDef = findLocalizedString(id);
                    if (stringDef != null) {
                        list.add(stringDef);
                    }
                }
            }

            if (org.radixware.kernel.common.defs.ads.clazz.algo.generation.AppUtils.SUBMITVARIANT_STR_MAP_TYPE.equals(type)) {
                final Map<EDwfFormSubmitVariant, String> map = org.radixware.kernel.common.defs.ads.clazz.algo.generation.AppUtils.parseAsStrMap(value.toString());
                for (Map.Entry<EDwfFormSubmitVariant, String> e : map.entrySet()) {
                    if (e.getValue() == null || !e.getValue().startsWith(EDefinitionIdPrefix.ADS_LOCALIZED_STRING.getValue())) {
                        continue;
                    }

                    final Id id = Id.Factory.loadFrom(e.getValue());
                    final AdsMultilingualStringDef stringDef = findLocalizedString(id);
                    if (stringDef != null) {
                        list.add(stringDef);
                    }
                }
            }

            return list;
        }

        @Override
        public ENamingPolicy getNamingPolicy() {
            return ENamingPolicy.FREE;
        }
    }

    public boolean isUserObject() {
        return !clazz.contains(".");
    }

    public AdsClassDef getUserDef() {
        if (isUserObject()) {
            return AdsSearcher.Factory.newAdsClassSearcher(this).findById(Id.Factory.loadFrom(clazz)).get();
        }
        return null;
    }

    public void syncProperties() {
        final Set<Prop> ps = ObjectFactory.clazz2Prop.get(clazz);
        if (ps != null) {
            for (Prop p : ps) {
                registerProp(new Prop(p));
            }
            return;
        }

        if (clazz.equals(NET_FORM_CLASS_NAME)) {
            registerProp(new Prop("sendMessSchemaId", null, Prop.PRIVATE | Prop.SETTING, ID_TYPE, true));
            registerProp(new Prop("recvMessSchemaId", null, Prop.PRIVATE | Prop.SETTING, ID_TYPE, true));
            registerProp(new Prop("sendMessClass", null, Prop.PRIVATE, AdsTypeDeclaration.Factory.newInstance(EValType.STR), true));
            registerProp(new Prop("recvMessClass", null, Prop.PRIVATE, AdsTypeDeclaration.Factory.newInstance(EValType.STR), true));
            registerProp(new Prop("sendMess", null, Prop.PUBLIC, AdsTypeDeclaration.Factory.newInstance(EValType.BIN), true));
            registerProp(new Prop("recvMess", null, Prop.PUBLIC, AdsTypeDeclaration.Factory.newInstance(EValType.BIN), true));
            registerProp(new Prop("portUnitIdParamId", null, Prop.PRIVATE | Prop.SETTING, ID_TYPE, true));
            registerProp(new Prop("sidParamId", null, Prop.PRIVATE | Prop.SETTING, ID_TYPE, true));
            registerProp(new Prop("smioSendParameters", null, Prop.PUBLIC, AdsTypeDeclaration.Factory.newInstance(EValType.XML, null, "org.radixware.schemas.msdl.Structure", 0), true));
            registerProp(new Prop("smioRecvParameters", null, Prop.PUBLIC, AdsTypeDeclaration.Factory.newInstance(EValType.XML, null, "org.radixware.schemas.msdl.Structure", 0), true));
            registerProp(new Prop("toSend", null, Prop.PUBLIC, AdsTypeDeclaration.Factory.newInstance(EValType.BOOL), true, "1"));
            registerProp(new Prop("toClose", null, Prop.PUBLIC, AdsTypeDeclaration.Factory.newInstance(EValType.BOOL), true, "0"));
            registerProp(new Prop("recvTimeout", null, Prop.PUBLIC, AdsTypeDeclaration.Factory.newInstance(EValType.NUM), true));
            registerProp(new Prop("strobId", null, Prop.PUBLIC | Prop.SETTING, ID_TYPE, true));
            registerProp(new Prop("strobed", null, Prop.PUBLIC, AdsTypeDeclaration.Factory.newInstance(EValType.BOOL), true));
            return;
        }

        if (clazz.equals(PERSO_COM_CLASS_NAME)) {
            registerProp(new Prop("channelKind", null, Prop.PUBLIC | Prop.SETTING, AdsTypeDeclaration.Factory.newInstance(EValType.STR, Id.Factory.loadFrom("acsZ5EEISSCVXOBDCLUAALOMT5GDM")), true));
            registerProp(new Prop("subject", null, Prop.PUBLIC | Prop.SETTING, AdsTypeDeclaration.Factory.newInstance(EValType.STR), true));
            registerProp(new Prop("body", null, Prop.PUBLIC | Prop.SETTING, AdsTypeDeclaration.Factory.newInstance(EValType.STR), true));
            registerProp(new Prop("importance", null, Prop.PUBLIC | Prop.SETTING, AdsTypeDeclaration.Factory.newInstance(EValType.INT, Id.Factory.loadFrom("acsTUGILCRUVXOBDCLUAALOMT5GDM")), true, String.valueOf(EPersoCommImportance.NORMAL.getValue())));
            registerProp(new Prop("address", null, Prop.PUBLIC, AdsTypeDeclaration.Factory.newInstance(EValType.STR), true));
            registerProp(new Prop("dueTime", null, Prop.PUBLIC, AdsTypeDeclaration.Factory.newInstance(EValType.NUM), true));
            registerProp(new Prop("responseTimeout", null, Prop.PUBLIC, AdsTypeDeclaration.Factory.newInstance(EValType.NUM), true));
            registerProp(new Prop("responseMessage", null, Prop.PUBLIC, AdsTypeDeclaration.Factory.newInstance(EValType.XML, null, "org.radixware.schemas.personalcommunications.MessageDocument", 0), true));
            registerProp(new Prop("responseParams", null, Prop.PUBLIC, AppUtils.STR_STR_MAP_TYPE, true));
            registerProp(new Prop("timeoutWid", null, Prop.PRIVATE, AdsTypeDeclaration.Factory.newInstance(EValType.INT), true));
            return;
        }

        if (clazz.equals(REPORT_GENERATOR_CLASS_NAME)) {
            return;
        }

        if (clazz.equals(WAIT_CLASS_NAME)) {
            registerProp(new Prop("timeout", null, Prop.PUBLIC | Prop.SETTING, AdsTypeDeclaration.Factory.newInstance(EValType.NUM), true, ValAsStr.Factory.newInstance(BigDecimal.valueOf(0), EValType.NUM)));
            registerProp(new Prop("timeoutWID", null, Prop.PUBLIC, AdsTypeDeclaration.Factory.newInstance(EValType.INT), true));
            registerProp(new Prop("strobId", null, Prop.PUBLIC | Prop.SETTING, ID_TYPE, true));
            registerProp(new Prop("strobed", null, Prop.PUBLIC, AdsTypeDeclaration.Factory.newInstance(EValType.BOOL), true));
            return;
        }

        registerProp(new Prop("clerkAutoSelect", null, Prop.PUBLIC | Prop.SETTING, AdsTypeDeclaration.Factory.newInstance(EValType.INT, Id.Factory.loadFrom("acs5BNQFFX65BDVNNMEURUUKTDQBY")), true, String.valueOf(EDwfFormClerkAutoSelect.NONE.getValue())));
        registerProp(new Prop("clerk", null, Prop.PUBLIC | Prop.SETTING, AdsTypeDeclaration.Factory.newInstance(EValType.OBJECT, Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4")), true));
        registerProp(new Prop("captureProcess", null, Prop.PUBLIC | Prop.SETTING, AdsTypeDeclaration.Factory.newInstance(EValType.BOOL), true, "0"));
        registerProp(new Prop("titleStrId", null, Prop.SETTING, ID_TYPE, true));
        registerProp(new Prop("headerTitleStrId", null, Prop.SETTING, ID_TYPE, true));
        registerProp(new Prop("footerTitleStrId", null, Prop.SETTING, ID_TYPE, true));
        registerProp(new Prop("title", null, Prop.PUBLIC | Prop.SETTING, AdsTypeDeclaration.Factory.newInstance(EValType.STR), true));
        registerProp(new Prop("headerTitle", null, Prop.PUBLIC | Prop.SETTING, AdsTypeDeclaration.Factory.newInstance(EValType.STR), true));
        registerProp(new Prop("footerTitle", null, Prop.PUBLIC | Prop.SETTING, AdsTypeDeclaration.Factory.newInstance(EValType.STR), true));
        registerProp(new Prop("priority", null, Prop.PUBLIC | Prop.SETTING, AdsTypeDeclaration.Factory.newInstance(EValType.INT, Id.Factory.loadFrom("acsJ4EC2QUIXLNRDF5JABIFNQAABA")), true, String.valueOf(EDwfFormPriority.NORMAL.getValue())));
        registerProp(new Prop("dueDelay", null, Prop.PUBLIC | Prop.SETTING, AdsTypeDeclaration.Factory.newInstance(EValType.NUM), true, "0"));
        registerProp(new Prop("overDueDelay", null, Prop.PUBLIC | Prop.SETTING, AdsTypeDeclaration.Factory.newInstance(EValType.NUM), true));
        registerProp(new Prop("processOverDue", null, Prop.PRIVATE | Prop.SETTING, AdsTypeDeclaration.Factory.newInstance(EValType.BOOL), true, "0"));
        registerProp(new Prop("clerkRoleGuids", null, Prop.PUBLIC | Prop.SETTING, AdsTypeDeclaration.Factory.newInstance(EValType.ARR_STR), true));
        registerProp(new Prop("adminRoleGuids", null, Prop.PUBLIC | Prop.SETTING, AdsTypeDeclaration.Factory.newInstance(EValType.ARR_STR), true));
        registerProp(new Prop("accessArea", null, Prop.PUBLIC | Prop.SETTING, AdsTypeDeclaration.Factory.newInstance(EValType.USER_CLASS, Id.Factory.loadFrom("adcYEOBYVKNGTOBDGANABIFNQAABA")), false));
        registerProp(new Prop("contentSaving", null, Prop.PUBLIC | Prop.SETTING, AdsTypeDeclaration.Factory.newInstance(EValType.BOOL), true, "0"));
        registerProp(new Prop("timeout", null, Prop.PUBLIC | Prop.SETTING, AdsTypeDeclaration.Factory.newInstance(EValType.NUM), true));
//        registerProp(new Prop("form", null, Prop.PUBLIC, AdsTypeDeclaration.Factory.newInstance(EValType.OBJECT, Id.Factory.loadFrom("aecVP66JC4HXLNRDF5JABIFNQAABA")), true));
        registerProp(new Prop("submitVariant", null, Prop.PUBLIC, AdsTypeDeclaration.Factory.newInstance(EValType.STR, Id.Factory.loadFrom("acsQ4A5NN4KXLNRDF5JABIFNQAABA")), true));
        registerProp(new Prop("timeoutWID", null, Prop.PUBLIC, AdsTypeDeclaration.Factory.newInstance(EValType.INT), true));
        registerProp(new Prop("submitWID", null, Prop.PUBLIC, AdsTypeDeclaration.Factory.newInstance(EValType.INT), true));
        registerProp(new Prop("overdueWID", null, Prop.PUBLIC, AdsTypeDeclaration.Factory.newInstance(EValType.INT), true));
        registerProp(new Prop("submitVariants", null, Prop.PRIVATE | Prop.SETTING, AdsTypeDeclaration.Factory.newInstance(EValType.STR), true, ""));
        registerProp(new Prop("submitVariantsTitle", null, Prop.PUBLIC | Prop.SETTING, AppUtils.SUBMITVARIANT_STR_MAP_TYPE, true, (ValAsStr) null));
        registerProp(new Prop("submitVariantsTitleId", null, Prop.PRIVATE | Prop.SETTING, AppUtils.SUBMITVARIANT_STR_MAP_TYPE, true, (ValAsStr) null));
        registerProp(new Prop("submitVariantsNeedConfirm", null, Prop.PUBLIC | Prop.SETTING, AppUtils.SUBMITVARIANT_BOOL_MAP_TYPE, true, (ValAsStr) null));
        registerProp(new Prop("submitVariantsNeedUpdate", null, Prop.PUBLIC | Prop.SETTING, AppUtils.SUBMITVARIANT_BOOL_MAP_TYPE, true, (ValAsStr) null));
        registerProp(new Prop("submitVariantsVisible", null, Prop.PUBLIC | Prop.SETTING, AppUtils.SUBMITVARIANT_BOOL_MAP_TYPE, true, (ValAsStr) null));

        if (clazz.equals(DOC_MANAGER_CLASS_NAME)) {
            registerProp(new Prop("context", null, Prop.PUBLIC | Prop.SETTING, AdsTypeDeclaration.Factory.newInstance(EValType.BLOB), true));
            return;
        }

        if (clazz.equals(DIALOG_CREATOR_CLASS_NAME)) {
            registerProp(new Prop("class", null, Prop.PUBLIC | Prop.SETTING, ID_TYPE, true));
            registerProp(new Prop("adminPresentationId", null, Prop.PUBLIC | Prop.SETTING, ID_TYPE, true));
            registerProp(new Prop("presentationIds", null, Prop.PUBLIC | Prop.SETTING, AdsTypeDeclaration.Factory.newInstance(EValType.STR), true));
            final Prop propClass = getPropByName("class");
            if (propClass.getValue() != null) {
                registerProp(new Prop("dialog", null, Prop.PUBLIC, AdsTypeDeclaration.Factory.newInstance(EValType.OBJECT, Id.Factory.loadFrom(String.valueOf(propClass.getValue()))), true), AdsAppObject.OVERRIDE_SOURCE_ID | AdsAppObject.OVERRIDE_MODE | AdsAppObject.OVERRIDE_PERSISTENT);
            } else {
                registerProp(new Prop("dialog", null, Prop.PUBLIC, AdsTypeDeclaration.Factory.newInstance(EValType.OBJECT, Id.Factory.loadFrom("aclMSJV23P2XTNRDF5NABIFNQAABA")), true), AdsAppObject.OVERRIDE_SOURCE_ID | AdsAppObject.OVERRIDE_MODE | AdsAppObject.OVERRIDE_PERSISTENT);
            }
            registerProp(new Prop("userPropIds", null, Prop.PRIVATE, AdsTypeDeclaration.Factory.newInstance(EValType.STR), true, ""));
            registerProp(new Prop("form", null, Prop.PUBLIC, AdsTypeDeclaration.Factory.newInstance(EValType.OBJECT, Id.Factory.loadFrom("aecVP66JC4HXLNRDF5JABIFNQAABA")), true), AdsAppObject.OVERRIDE_SOURCE_ID | AdsAppObject.OVERRIDE_MODE | AdsAppObject.OVERRIDE_PERSISTENT);
            return;
        }

        if (clazz.equals(DIALOG_DUPLICATOR_CLASS_NAME)) {
            registerProp(new Prop("class", null, Prop.PUBLIC | Prop.SETTING, ID_TYPE, true));
            registerProp(new Prop("adminPresentationId", null, Prop.PUBLIC | Prop.SETTING, ID_TYPE, true));
            registerProp(new Prop("presentationIds", null, Prop.PUBLIC | Prop.SETTING, AdsTypeDeclaration.Factory.newInstance(EValType.STR), true));
            final Prop propClass = getPropByName("class");
            if (propClass.getValue() != null) {
                registerProp(new Prop("sourceDialog", null, Prop.PUBLIC, AdsTypeDeclaration.Factory.newInstance(EValType.OBJECT, Id.Factory.loadFrom(String.valueOf(propClass.getValue()))), true), AdsAppObject.OVERRIDE_SOURCE_ID | AdsAppObject.OVERRIDE_MODE | AdsAppObject.OVERRIDE_PERSISTENT);
            } else {
                registerProp(new Prop("sourceDialog", null, Prop.PUBLIC, AdsTypeDeclaration.Factory.newInstance(EValType.OBJECT, Id.Factory.loadFrom("aclMSJV23P2XTNRDF5NABIFNQAABA")), true), AdsAppObject.OVERRIDE_SOURCE_ID | AdsAppObject.OVERRIDE_MODE | AdsAppObject.OVERRIDE_PERSISTENT);
            }
            registerProp(new Prop("sourceBlockId", null, Prop.SETTING | Prop.PRIVATE, ID_TYPE, true));
            registerProp(new Prop("overrideMask", null, Prop.PUBLIC | Prop.SETTING, AdsTypeDeclaration.Factory.newInstance(EValType.STR), true));
            registerProp(new Prop("userPropIds", null, Prop.PRIVATE, AdsTypeDeclaration.Factory.newInstance(EValType.STR), true, ""));
            registerProp(new Prop("form", null, Prop.PUBLIC, AdsTypeDeclaration.Factory.newInstance(EValType.OBJECT, Id.Factory.loadFrom("aecVP66JC4HXLNRDF5JABIFNQAABA")), true), AdsAppObject.OVERRIDE_SOURCE_ID | AdsAppObject.OVERRIDE_MODE | AdsAppObject.OVERRIDE_PERSISTENT);
            return;
        }

        if (clazz.equals(EDITOR_FORM_CLASS_NAME)) {
            final Prop p = getPropByName("classId");
            if (p != null) {
                p.setName("objClassId");
            }
            registerProp(new Prop("object", null, Prop.PUBLIC, AdsTypeDeclaration.Factory.newInstance(EValType.OBJECT, Id.Factory.loadFrom("pdcEntity____________________")), true), AdsAppObject.OVERRIDE_SOURCE_ID | AdsAppObject.OVERRIDE_MODE | AdsAppObject.OVERRIDE_PERSISTENT);
            registerProp(new Prop("objClassId", null, Prop.PUBLIC | Prop.SETTING, ID_TYPE, true));
            registerProp(new Prop("creationPresentationId", null, Prop.PUBLIC | Prop.SETTING, ID_TYPE, true));
            registerProp(new Prop("adminPresentationId", null, Prop.PUBLIC | Prop.SETTING, ID_TYPE, true));
            registerProp(new Prop("presentationIds", null, Prop.PUBLIC | Prop.SETTING, AdsTypeDeclaration.Factory.newInstance(EValType.STR), true));
            registerProp(new Prop("restrictions", null, Prop.PUBLIC | Prop.SETTING, AdsTypeDeclaration.Factory.newInstance(EValType.INT), true, String.valueOf(ERestriction.UPDATE.getValue())));
            registerProp(new Prop("form", null, Prop.PUBLIC, AdsTypeDeclaration.Factory.newInstance(EValType.OBJECT, Id.Factory.loadFrom("aclILXI4BP2XTNRDF5NABIFNQAABA")), true), AdsAppObject.OVERRIDE_SOURCE_ID | AdsAppObject.OVERRIDE_MODE | AdsAppObject.OVERRIDE_PERSISTENT);
            return;
        }

        if (clazz.equals(SELECTOR_FORM_CLASS_NAME)) {
            Prop p = getPropByName("classId");
            if (p != null) {
                p.setName("objClassId");
            }
            p = getPropByName("parentClassId");
            if (p != null) {
                p.setName("parentObjClassId");
            }
            p = getPropByName("parentObjClassId");
            if (p != null) {
                unregisterProp(p);
            }
            p = getPropByName("parentPresentationId");
            if (p != null) {
                unregisterProp(p);
            }
            p = getPropByName("parentItemId");
            if (p != null) {
                unregisterProp(p);
            }
            p = getPropByName("parentObject");
            if (p != null) {
                unregisterProp(p);
            }
            registerProp(new Prop("object", null, Prop.PUBLIC, AdsTypeDeclaration.Factory.newInstance(EValType.OBJECT, Id.Factory.loadFrom("pdcEntity____________________")), true), AdsAppObject.OVERRIDE_SOURCE_ID | AdsAppObject.OVERRIDE_MODE | AdsAppObject.OVERRIDE_PERSISTENT);
            registerProp(new Prop("objClassId", null, Prop.PUBLIC | Prop.SETTING, ID_TYPE, true));
            registerProp(new Prop("presentationId", null, Prop.PUBLIC | Prop.SETTING, ID_TYPE, true));
            registerProp(new Prop("condition", null, Prop.PUBLIC | Prop.SETTING, AdsTypeDeclaration.Factory.newInstance(EValType.STR), true));
            registerProp(new Prop("restrictions", null, Prop.PUBLIC | Prop.SETTING, AdsTypeDeclaration.Factory.newInstance(EValType.INT), true, String.valueOf(ERestriction.UPDATE.getValue())));
            registerProp(new Prop("form", null, Prop.PUBLIC, AdsTypeDeclaration.Factory.newInstance(EValType.OBJECT, Id.Factory.loadFrom("aclO2T5FNHYXTNRDF5NABIFNQAABA")), true), AdsAppObject.OVERRIDE_SOURCE_ID | AdsAppObject.OVERRIDE_MODE | AdsAppObject.OVERRIDE_PERSISTENT);
        }
    }
}