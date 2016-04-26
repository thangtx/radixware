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

package org.radixware.kernel.common.defs.ads.clazz;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.check.RadixProblem.WarningSuppressionSupport;
import org.radixware.kernel.common.defs.*;
import org.radixware.kernel.common.defs.ClipboardSupport.Transfer;
import org.radixware.kernel.common.defs.Dependences.Dependence;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.ads.*;
import org.radixware.kernel.common.defs.ads.clazz.algo.AdsAlgoClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.*;
import org.radixware.kernel.common.defs.ads.clazz.form.AdsFormHandlerClassDef;
import org.radixware.kernel.common.defs.ads.clazz.form.AdsFormModelClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.*;
import org.radixware.kernel.common.defs.ads.clazz.members.IModelPublishableProperty.Support;
import org.radixware.kernel.common.defs.ads.clazz.members.Properties;
import org.radixware.kernel.common.defs.ads.clazz.presentation.*;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsClassCatalogDef.ClassReference;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsClassCatalogDef.Topic;
import org.radixware.kernel.common.defs.ads.clazz.sql.AdsCursorClassDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.AdsProcedureClassDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.AdsStatementClassDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportClassDef;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsParagraphExplorerItemDef;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsParagraphModelClassDef;
import org.radixware.kernel.common.defs.ads.module.AdsPath;
import org.radixware.kernel.common.defs.ads.module.ModuleDefinitions;
import org.radixware.kernel.common.defs.ads.radixdoc.ClassRadixdoc;
import org.radixware.kernel.common.defs.ads.src.IJavaSource;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport.UsagePurpose;
import org.radixware.kernel.common.defs.ads.src.clazz.AdsClassWriter;
import org.radixware.kernel.common.defs.ads.type.*;
import org.radixware.kernel.common.enums.*;
import static org.radixware.kernel.common.enums.EClassType.ALGORITHM;
import static org.radixware.kernel.common.enums.EClassType.APPLICATION;
import static org.radixware.kernel.common.enums.EClassType.DYNAMIC;
import static org.radixware.kernel.common.enums.EClassType.ENTITY;
import static org.radixware.kernel.common.enums.EClassType.ENTITY_GROUP;
import static org.radixware.kernel.common.enums.EClassType.ENUMERATION;
import static org.radixware.kernel.common.enums.EClassType.EXCEPTION;
import static org.radixware.kernel.common.enums.EClassType.FORM_HANDLER;
import static org.radixware.kernel.common.enums.EClassType.INTERFACE;
import static org.radixware.kernel.common.enums.EClassType.PRESENTATION_ENTITY_ADAPTER;
import static org.radixware.kernel.common.enums.EClassType.REPORT;
import static org.radixware.kernel.common.enums.EClassType.SQL_CURSOR;
import static org.radixware.kernel.common.enums.EClassType.SQL_PROCEDURE;
import static org.radixware.kernel.common.enums.EClassType.SQL_STATEMENT;
import org.radixware.kernel.common.jml.IJmlSource;
import org.radixware.kernel.common.jml.Jml;
import org.radixware.kernel.common.jml.JmlTagId;
import org.radixware.kernel.common.radixdoc.DocumentOptions;
import org.radixware.kernel.common.radixdoc.IRadixdocPage;
import org.radixware.kernel.common.radixdoc.IRadixdocProvider;
import org.radixware.kernel.common.radixdoc.RadixdocSupport;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.IWalker;
import org.radixware.kernel.common.utils.IdPrefixes;
import org.radixware.kernel.common.utils.IterableWalker;
import org.radixware.kernel.common.utils.RadixObjectWalker;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.schemas.adsdef.AccessRules;
import org.radixware.schemas.adsdef.AdsDefinitionElementType;
import org.radixware.schemas.adsdef.ClassDefinition;
import org.radixware.schemas.radixdoc.Page;
import org.radixware.schemas.xscml.JmlType;

/**
 * Top level class definition
 *
 */
public abstract class AdsClassDef extends AdsTitledDefinition implements
        IOverwritable, IAdsTypeSource, IJavaSource, IJmlSource,
        IModelPublishableProperty.Provider, IClientDefinition, ITransparency,
        IAccessible, IAdsClassMember, IRadixdocProvider {

    public static class SourcePart extends Jml implements IAdsClassMember {

        private ERuntimeEnvironmentType env;
        private String description = "";

        private SourcePart(String name) {
            super(null, name);

        }

        private SourcePart(String name, JmlType xJml) {
            super(null, name);
            if (xJml != null) {
                loadFrom(xJml);
            }
        }

        public void appendTo(org.radixware.schemas.adsdef.ClassSource.Src xDef, ESaveMode mode) {
            super.appendTo(xDef, mode);
            xDef.setName(getName());
            if (description != null && !description.isEmpty()) {
                xDef.setDescription(description);
            }
        }

        @Override
        protected void insertToolTipPrefix(StringBuilder sb) {
            super.insertToolTipPrefix(sb);
        }

        @Override
        protected void appendAdditionalToolTip(StringBuilder sb) {
            super.appendAdditionalToolTip(sb);
        }

        @Override
        protected void loadFrom(JmlType xJml) {
            super.loadFrom(xJml);
            if (xJml.isSetDescription()) {
                this.description = xJml.getDescription();
            }
        }

        @Override
        public ERuntimeEnvironmentType getUsageEnvironment() {
            if (env == null) {
                return super.getUsageEnvironment();
            } else {
                return env;
            }
        }

        public void setUsageEnvironment(ERuntimeEnvironmentType env) {
            if (this.env != env) {
                this.env = env;
                setEditState(EEditState.MODIFIED);
            }
        }

        public void setDescription(String text) {
            this.description = text;
            setEditState(EEditState.MODIFIED);
        }

        @Override
        public String getDescription() {
            return description;
        }

        @Override
        public AdsClassDef getOwnerClass() {
            RadixObject owner = getContainer();
            while (owner != null) {
                if (owner instanceof AdsClassDef) {
                    return (AdsClassDef) owner;
                }
                owner = owner.getContainer();
            }
            return null;
        }
    }

    public static class ClassSource extends ClassCodeLocalObjects<SourcePart> implements IJmlSource {

        private final String namePrefix;

        public ClassSource(AdsClassDef owner, String namePrefix) {
            super(owner);
            this.namePrefix = namePrefix;
        }

        void loadFrom(JmlType oldStyleSource) {
            if (oldStyleSource != null) {
                SourcePart p = new SourcePart(namePrefix + getOwnerClass().getId().toString(), oldStyleSource);
                add(p);
            }
        }

        public void loadFrom(org.radixware.schemas.adsdef.ClassSource xDef) {
            if (xDef != null) {
                List<org.radixware.schemas.adsdef.ClassSource.Src> srcList = xDef.getSrcList();
                if (srcList != null) {

                    for (org.radixware.schemas.adsdef.ClassSource.Src s : srcList) {
                        String name = s.getName();
                        if (name == null || name.isEmpty()) {
                            name = namePrefix + Id.Factory.newInstance(EDefinitionIdPrefix.ADS_DYNAMIC_CLASS).toString();
                        }
                        SourcePart p = new SourcePart(name, s);
                        p.setUsageEnvironment(s.getEnvironment());
                        add(p);
                    }
                }
            }
        }

        public SourcePart ensureFirst() {
            if (isEmpty()) {
                initDefault();
            }
            return get(0);
        }

        public void appendTo(org.radixware.schemas.adsdef.ClassSource xDef, ESaveMode saveMode) {
            if (!isEmpty()) {
                for (SourcePart p : this) {
                    org.radixware.schemas.adsdef.ClassSource.Src s = xDef.addNewSrc();
                    p.appendTo(s, saveMode);
                    if (p.env != null) {
                        s.setEnvironment(p.env);
                    }
                }
            }
        }

        private void initDefault() {
            clear();
            SourcePart p = new SourcePart(namePrefix + getOwnerClass().getId().toString());
            add(p);
        }

        public SourcePart addPart() {
            SourcePart p = new SourcePart(namePrefix + Id.Factory.newInstance(EDefinitionIdPrefix.ADS_DYNAMIC_CLASS).toString());
            add(p);
            return p;
        }

        @Override
        public Jml getSource(String name) {
            for (SourcePart p : this) {
                if (p.getName().equals(name)) {
                    return p;
                }
            }
            return null;
        }
    }

    public static class Resources extends RadixObjects<Resources.Resource> {

        public class Resource extends RadixObject {

            private String data;

            public String getData() {
                return data;
            }

            public void setData(String data) {
                if (!Utils.equals(data, this.data)) {
                    this.data = data;
                    setEditState(EEditState.MODIFIED);
                }
            }

            public Resource(String name) {
                super(name);
            }

            public Resource(ClassDefinition.Resources.Resource xRes) {
                super(xRes.getName());
                this.data = xRes.getData();
            }

            public void appendTo(ClassDefinition.Resources xRs) {
                ClassDefinition.Resources.Resource xRes = xRs.addNewResource();
                xRes.setName(getName());
                xRes.setData(data);
            }

            @Override
            public ENamingPolicy getNamingPolicy() {
                return ENamingPolicy.UNIQUE_IDENTIFIER;
            }
        }

        public Resources(AdsClassDef clazz) {
            super(clazz);
        }

        public void loadFrom(ClassDefinition xDef) {
            if (xDef != null && xDef.getResources() != null) {
                for (ClassDefinition.Resources.Resource xRes : xDef.getResources().getResourceList()) {
                    add(new Resource(xRes));
                }
            }
        }

        public void appendTo(ClassDefinition xDef) {
            if (isEmpty()) {
                return;
            }
            ClassDefinition.Resources xRs = xDef.addNewResources();
            for (Resource rs : this) {
                rs.appendTo(xRs);
            }
        }
    }

    public static final class Factory {

        public static AdsClassDef loadFrom(final XmlObject xmlObject) {
            if (xmlObject instanceof ClassDefinition) {
                final ClassDefinition xDef = (ClassDefinition) xmlObject;
                switch (xDef.getType()) {
                    case ALGORITHM:
                        return AdsAlgoClassDef.Factory.loadFrom(xDef);
                    case DYNAMIC:
                        return AdsDynamicClassDef.Factory.loadFrom(xDef);
                    case APPLICATION:
                        return AdsApplicationClassDef.Factory.loadFrom(xDef);
                    case ENTITY:
                        return AdsEntityClassDef.Factory.loadFrom(xDef);
                    case ENTITY_GROUP:
                        return AdsEntityGroupClassDef.Factory.loadFrom(xDef);
                    case PRESENTATION_ENTITY_ADAPTER:
                        return AdsPresentationEntityAdapterClassDef.Factory.loadFrom(xDef);
                    case EXCEPTION:
                        return AdsExceptionClassDef.Factory.loadFrom(xDef);
                    case FORM_HANDLER:
                        return AdsFormHandlerClassDef.Factory.loadFrom(xDef);
                    case INTERFACE:
                        return AdsInterfaceClassDef.Factory.loadFrom(xDef);
                    case REPORT:
                        return AdsReportClassDef.Factory.loadFrom(xDef);
                    case SQL_CURSOR:
                        return AdsCursorClassDef.Factory.loadFrom(xDef);
                    case SQL_PROCEDURE:
                        return AdsProcedureClassDef.Factory.loadFrom(xDef);
                    case SQL_STATEMENT:
                        return AdsStatementClassDef.Factory.loadFrom(xDef);
                    case ENUMERATION:
                        return AdsEnumClassDef.Factory.loadFrom(xDef);
                    default:
                        break;
                }
            }
            return null;
        }
    }

    public static class Problems extends AdsDefinitionProblems {

        public static final int METHODS_WITH_SAME_PROFILE_AND_DIFFERENT_IDS = 100000;

        protected Problems(AdsClassDef owner, List<Integer> warnings) {
            super(owner);
            if (warnings != null) {
                int arr[] = new int[warnings.size()];
                for (int i = 0; i < arr.length; i++) {
                    arr[i] = warnings.get(i);
                }
                setSuppressedWarnings(arr);
            }
        }

        @Override
        public boolean canSuppressWarning(int code) {
            return code == METHODS_WITH_SAME_PROFILE_AND_DIFFERENT_IDS || super.canSuppressWarning(code);
        }
    }
    //-----------------------------------------------------------------------
    public static final String CLASS_HEADER_NAME = "Header";
    public static final String CLASS_BODY_NAME = "Body";
    //-----------------------------------------------------------------------

    //-----------------------------------------------------------------------
    private class AdsClassTransfer extends AdsClipboardSupport.AdsTransfer<AdsClassDef> {

        private Id[] prevPath;

        public AdsClassTransfer(AdsClassDef source) {
            super(source);
            prevPath = source.getIdPath();
        }

        private boolean accept(Id[] path) {
            if (path.length < prevPath.length) {
                return false;
            }

            for (int i = 0; i < prevPath.length; i++) {
                if (!Objects.equals(path[i], prevPath[i])) {
                    return false;
                }
            }

            return true;
        }

        private Id[] fixPath(Id[] path, Id[] currPath) {
            Id[] newPath = new Id[path.length - prevPath.length + currPath.length];

            System.arraycopy(currPath, 0, newPath, 0, currPath.length);
            System.arraycopy(path, prevPath.length, newPath, currPath.length, path.length - prevPath.length);

            return newPath;
        }

        @Override
        public void afterPaste() {
            super.afterPaste();
            final RadixObject obj = getObject();


            if (obj instanceof AdsDynamicClassDef) {
                AdsDynamicClassDef cls = (AdsDynamicClassDef) obj;

                final Id[] currPath = cls.getIdPath();

                cls.visitAll(new IVisitor() {
                    @Override
                    public void accept(RadixObject radixObject) {
                        if (radixObject instanceof JmlTagId) {
                            JmlTagId tagId = (JmlTagId) radixObject;

                            Id[] path = tagId.getPath().asArray();
                            if (AdsClassTransfer.this.accept(path)) {
                                tagId.setPath(new AdsPath(fixPath(path, currPath)));
                            }
                        }
                    }
                });
            }

            if (obj instanceof AdsClassDef) {
                final AdsClassDef clazz = (AdsClassDef) obj;
                for (AdsMethodDef method : clazz.getMethods().getLocal()) {
                    if (method.getHierarchy().findOverridden().get() != null) {
                        method.setOverride(true);
                    } else {
                        method.setOverride(false);
                    }
                    if (method.getHierarchy().findOverwritten().get() != null) {
                        method.setOverwrite(true);
                    } else {
                        method.setOverwrite(false);
                    }
                }
                for (AdsPropertyDef prop : clazz.getProperties().getLocal()) {
                    if (prop.getHierarchy().findOverridden().get() != null) {
                        prop.setOverride(true);
                    } else {
                        prop.setOverride(false);
                    }
                    if (prop.getHierarchy().findOverwritten().get() != null) {
                        prop.setOverwrite(true);
                    } else {
                        prop.setOverwrite(false);
                    }
                }

                if (!clazz.isNested()) {
                    clazz.getAccessFlags().setStatic(false);
                }
            }

            if (obj instanceof AdsEntityObjectClassDef) {
                changeOrder((AdsEntityObjectClassDef) obj);
            }

            if (obj instanceof AdsEnumClassDef) {
                final AdsEnumClassDef enumDef = (AdsEnumClassDef) obj;
                for (AdsEnumClassFieldDef field : enumDef.getFields().getLocal()) {
                    if (field.getHierarchy().findOverwritten().get() != null) {
                        field.setOverwrite(true);
                    } else {
                        field.setOverwrite(false);
                    }
                }
            }
        }

        private void changeOrder(AdsEntityObjectClassDef entity) {
            for (final AdsClassCatalogDef classCatalog : entity.getPresentations().getClassCatalogs().getLocal()) {
                if (classCatalog.isVirtual()) {
                    final AdsClassCatalogDef.Virtual virtual = (AdsClassCatalogDef.Virtual) classCatalog;

                    final Map<Id, TreeSet<Double>> order = new HashMap<>();

                    for (final AdsClassCatalogDef catalog : virtual.getAll()) {
                        if (catalog != virtual) {
                            collectItemOrders(catalog.getClassRefList(), order);
                            collectItemOrders(catalog.getTopicList(), order);
                        }
                    }

                    final List<Topic> topicList = virtual.getTopicList();
                    if (!topicList.isEmpty()) {
                        updateItemOrders(topicList, order);
                    }
                    final ClassReference currRef = virtual.getClassReference();
                    if (currRef != null) {
                        updateItemOrders(Collections.singletonList(currRef), order);
                    }
                }
            }
        }
        final Id ROOT_ID = Id.Factory.loadFrom("ROOT-CLASS-CATALOG-ID");
        final double CONTROL_STEP = 1;

        private void collectItemOrders(Collection<? extends AdsClassCatalogDef.IClassCatalogItem> items, Map<Id, TreeSet<Double>> map) {
            for (AdsClassCatalogDef.IClassCatalogItem item : items) {
                final Id parentId = item.getParentTopicId() != null ? item.getParentTopicId() : ROOT_ID;
                TreeSet<Double> orderSet = map.get(parentId);
                if (orderSet == null) {
                    orderSet = new TreeSet<>();
                    map.put(parentId, orderSet);
                }

                orderSet.add(item.getOrder());
            }
        }

        private void updateItemOrders(Collection<? extends AdsClassCatalogDef.IClassCatalogItem> items, Map<Id, TreeSet<Double>> map) {
            for (AdsClassCatalogDef.IClassCatalogItem item : items) {
                final Id parentId = item.getParentTopicId() != null ? item.getParentTopicId() : ROOT_ID;
                final TreeSet<Double> orderSet = map.get(parentId);

                if (orderSet != null && orderSet.contains(item.getOrder())) {
                    Double higher = orderSet.higher(item.getOrder());
                    double newOrder = higher != null ? (higher + item.getOrder()) / 2.0 : item.getOrder() + CONTROL_STEP;

                    item.setOrder(newOrder);
                }
            }
        }
    }

    private class AdsClassClipboardSupport extends ClassClipboardSupport<AdsClassDef> {

        public AdsClassClipboardSupport(AdsClassDef c) {
            super(c);
        }

        @Override
        public boolean canCopy() {
            switch (AdsClassDef.this.getClassDefType()) {
                case ALGORITHM:
                case PRESENTATION_ENTITY_ADAPTER:
                case DYNAMIC:
                case APPLICATION:
                case EXCEPTION:
                case FORM_HANDLER:
                case INTERFACE:
                case REPORT:
                case ENTITY:
                case ENTITY_GROUP:
                case SQL_CURSOR:
                case SQL_PROCEDURE:
                case SQL_STATEMENT:
                case ENUMERATION:
                    return true;
                default:
                    return false;
            }
        }

        @Override
        protected XmlObject copyToXml() {
            final ClassDefinition xDef = ClassDefinition.Factory.newInstance();
            AdsClassDef.this.appendTo(xDef, ESaveMode.NORMAL);
            return xDef;
        }

        @Override
        protected Method getDecoderMethod() {
            try {
                return AdsClassDef.Factory.class.getDeclaredMethod("loadFrom", XmlObject.class);
            } catch (NoSuchMethodException | SecurityException e) {
                Logger.getLogger(AdsClassDef.class.getName()).log(Level.SEVERE, null, e);
                return null;
            }
        }

        @Override
        public boolean isEncodedFormatSupported() {
            return true;
        }

        @Override
        protected Transfer<AdsClassDef> newTransferInstance() {
            return new AdsClassTransfer(radixObject);
        }

        @Override
        protected AdsClassDef loadFrom(final XmlObject xmlObject) {
            return AdsClassDef.Factory.loadFrom(xmlObject);
//                if (xmlObject instanceof ClassDefinition) {
//                    final ClassDefinition xDef = (ClassDefinition) xmlObject;
//                    switch (xDef.getType()) {
//                        case ALGORITHM:
//                            return AdsAlgoClassDef.Factory.loadFrom(xDef);
//                        case DYNAMIC:
//                            return AdsDynamicClassDef.Factory.loadFrom(xDef);
//                        case APPLICATION:
//                            return AdsApplicationClassDef.Factory.loadFrom(xDef);
//                        case ENTITY:
//                            return AdsEntityClassDef.Factory.loadFrom(xDef);
//                        case ENTITY_GROUP:
//                            return AdsEntityGroupClassDef.Factory.loadFrom(xDef);
//                        case PRESENTATION_ENTITY_ADAPTER:
//                            return AdsPresentationEntityAdapterClassDef.Factory.loadFrom(xDef);
//                        case EXCEPTION:
//                            return AdsExceptionClassDef.Factory.loadFrom(xDef);
//                        case FORM_HANDLER:
//                            return AdsFormHandlerClassDef.Factory.loadFrom(xDef);
//                        case INTERFACE:
//                            return AdsInterfaceClassDef.Factory.loadFrom(xDef);
//                        case REPORT:
//                            return AdsReportClassDef.Factory.loadFrom(xDef);
//                        case SQL_CURSOR:
//                            return AdsCursorClassDef.Factory.loadFrom(xDef);
//                        case SQL_PROCEDURE:
//                            return AdsProcedureClassDef.Factory.loadFrom(xDef);
//                        case SQL_STATEMENT:
//                            return AdsStatementClassDef.Factory.loadFrom(xDef);
//                        case ENUMERATION:
//                            return AdsEnumClassDef.Factory.loadFrom(xDef);
//                        default:
//                            break;
//                    }
//                }
//                return super.loadFrom(xmlObject);
        }
    }
    //-----------------------------------------------------------------------
    private final Methods methods;
    private final Properties properties;
    private final NestedClasses nestedClasses;
    private final AdsPropertyGroup propertyGroup;
    private final AdsMethodGroup methodGroup;
    private final Inheritance inheritance;
    private final AdsAccessFlags accessFlags;
    private final AdsTypeDeclaration.TypeArguments typeArguments;
    private Problems warningsSupport = null;
    private List<Id> apiPropIds = null;
    private final ClassSource header;
    private final ClassSource body;
    private final Resources resources;
    //-----------------------------------------------------------------------

    protected AdsClassDef(Id id, String name) {
        super(id, name, null);
        this.typeArguments = AdsTypeDeclaration.TypeArguments.Factory.newInstance(this);
        this.methodGroup = AdsMethodGroup.Factory.newInstance(this);
        this.propertyGroup = AdsPropertyGroup.Factory.newInstance(this);
        this.methods = Methods.Factory.newInstance(this);
        this.properties = Properties.Factory.newInstance(this);
        this.inheritance = Inheritance.Factory.newInstance(this);
        this.accessFlags = AdsAccessFlags.Factory.newInstance(this);
        this.header = new ClassSource(this, CLASS_HEADER_NAME);
        this.header.initDefault();
        this.body = new ClassSource(this, CLASS_BODY_NAME);
        this.body.initDefault();

        this.nestedClasses = NestedClasses.Factory.newInstance(this);
        this.resources = new Resources(this);
    }

    /**
     * Creates new overwrite instanece
     */
    protected AdsClassDef(AdsClassDef source) {
        this(source.getId(), source.getName());
    }

    @SuppressWarnings("unchecked")
    protected AdsClassDef(Id id, ClassDefinition xClass) {
        super(id, xClass);
        this.typeArguments = AdsTypeDeclaration.TypeArguments.Factory.loadFrom(this, xClass.getTypeArguments());
        this.inheritance = Inheritance.Factory.loadFrom(this, xClass);
        this.propertyGroup = AdsPropertyGroup.Factory.loadFrom(this, xClass.getPropertyGroup());
        this.methodGroup = AdsMethodGroup.Factory.loadFrom(this, xClass.getMethodGroup());
        this.properties = Properties.Factory.loadFrom(this, xClass.getProperties());
        this.methods = Methods.Factory.loadFrom(this, xClass.getMethods());
        this.accessFlags = AdsAccessFlags.Factory.loadFrom(this, xClass.getAccessRules());
        if (xClass.getAccessRules() != null && xClass.getAccessRules().getIsFinal()) {
            this.setFinal(true);
        }
        this.header = new ClassSource(this, CLASS_HEADER_NAME);
        this.body = new ClassSource(this, CLASS_BODY_NAME);
        if (xClass.getHeader() != null) {
            this.header.loadFrom(xClass.getHeader());
        } else {
            this.header.loadFrom(xClass.getHeaders());
        }
        if (this.header.isEmpty()) {
            this.header.initDefault();
        }
        if (xClass.getBody() != null) {
            this.body.loadFrom(xClass.getBody());
        } else {
            this.body.loadFrom(xClass.getBodies());
        }

        if (this.body.isEmpty()) {
            this.body.initDefault();
        }

        if (xClass.isSetSuppressedWarnings()) {
            List<Integer> list = xClass.getSuppressedWarnings();
            if (!list.isEmpty()) {
                this.warningsSupport = instantiateWarningsSupport(list);
            }
        }
        if (xClass.getPropIds() != null) {
            this.apiPropIds = new ArrayList<>(xClass.getPropIds());
        }

        this.nestedClasses = NestedClasses.Factory.loadFrom(this, xClass.getInnerClasses());
        this.resources = new Resources(this);
        this.resources.loadFrom(xClass);
    }

    @Override
    public void afterOverwrite() {
        this.getMethods().getLocal().clear();
        this.getProperties().getLocal().clear();
        this.getBody().clear();
        this.getBody().initDefault();
        this.getHeader().clear();
        this.getHeader().initDefault();
        this.getPropertyGroup().getChildGroups().clear();
        this.getPropertyGroup().clearMembers();
        this.getMethodGroup().getChildGroups().clear();
        this.getMethodGroup().clearMembers();
        this.getNestedClasses().getLocal().clear();
        if (this instanceof IAdsPresentableClass) {
            ((IAdsPresentableClass) this).getPresentations().afterOverwrite();
        }
        this.getResources().clear();
    }

    @Override
    public boolean allowOverwrite() {
        return true;
    }

    @Override
    public AdsClassDef getOwnerClass() {
        return null;
    }

    protected Problems instantiateWarningsSupport(List<Integer> list) {
        return new Problems(this, list);
    }

    @Override
    public WarningSuppressionSupport getWarningSuppressionSupport(boolean createIfAbsent) {
        synchronized (this) {
            if (warningsSupport == null && createIfAbsent) {
                warningsSupport = instantiateWarningsSupport(null);
            }
            return warningsSupport;
        }
    }

    public Resources getResources() {
        return resources;
    }

    public boolean hasConstructors() {
        for (AdsMethodDef m : getMethods().getLocal()) {
            if (m.isConstructor()) {
                return true;
            }
        }
        return false;
    }

    public Collection<AdsMethodDef> getConstructors() {
        final ArrayList<AdsMethodDef> result = new ArrayList<>();
        for (AdsMethodDef m : getMethods().getLocal()) {
            if (m.isConstructor()) {
                result.add(m);
            }
        }
        return result;
    }

    public Inheritance getInheritance() {
        return inheritance;
    }

    public AdsTypeDeclaration.TypeArguments getTypeArguments() {
        return typeArguments;
    }

    public Methods getMethods() {
        return methods;
    }

    public Properties getProperties() {
        return properties;
    }

    public AdsMethodGroup getMethodGroup() {
        return methodGroup;
    }

    public AdsPropertyGroup getPropertyGroup() {
        return propertyGroup;
    }

    protected AdsClassDef(ClassDefinition xClass) {
        this(xClass.getId(), xClass);
    }

    public NestedClasses getNestedClasses() {
        return nestedClasses;
    }

    public List<Id> getApiPropIds() {
        if (apiPropIds != null) {
            return new ArrayList<>(apiPropIds);
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public ERuntimeEnvironmentType getClientEnvironment() {
        ERuntimeEnvironmentType env = getUsageEnvironment();
        if (env.isClientEnv()) {
            return env;
        } else {
            return ERuntimeEnvironmentType.COMMON_CLIENT;
        }
    }

    public boolean canChangeClientEnvironment() {
        return true;
    }

    public void appendTo(ClassDefinition xDef, AdsDefinition.ESaveMode saveMode) {
        super.appendTo(xDef, saveMode);
        xDef.setType(this.getClassDefType());

        inheritance.appendTo(xDef);
        AccessRules rules = xDef.getAccessRules();
        if (rules == null) {
            rules = xDef.addNewAccessRules();
        }
        accessFlags.appendTo(rules);

        properties.appendTo(xDef, saveMode);
        methods.appendTo(xDef, saveMode);

        nestedClasses.appendTo(xDef, saveMode);

        if (saveMode != ESaveMode.API) {
            propertyGroup.appendTo(xDef.addNewPropertyGroup(), saveMode);
            methodGroup.appendTo(xDef.addNewMethodGroup(), saveMode);
        }
        if (getTransparence() != null) {
            getTransparence().appendTo(xDef.getAccessRules().addNewTransparence());
        }
        if (header != null && !header.isEmpty()) {
            header.appendTo(xDef.addNewHeaders(), saveMode);
        }
        if (body != null && !body.isEmpty()) {
            body.appendTo(xDef.addNewBodies(), saveMode);
        }
        if (!typeArguments.isEmpty()) {
            typeArguments.appendTo(xDef.addNewTypeArguments());
        }

        if (saveMode == ESaveMode.API) {
            List<Id> ids = calcApiPropIds(getClientEnvironment());

            if (!ids.isEmpty()) {
                xDef.setPropIds(ids);
            }
        }
        if (saveMode == ESaveMode.NORMAL) {
            if (warningsSupport != null && !warningsSupport.isEmpty()) {

                int[] warnings = warningsSupport.getSuppressedWarnings();
                List<Integer> list = new ArrayList<>(warnings.length);
                for (int w : warnings) {
                    list.add(Integer.valueOf(w));
                }
                xDef.setSuppressedWarnings(list);
            }
        }
        resources.appendTo(xDef);
    }

    private List<Id> calcApiPropIds(ERuntimeEnvironmentType env) {
        List<Id> result = new LinkedList<>();
        IModelPublishableProperty.Support pubSupport = getModelPublishablePropertySupport();
        if (pubSupport != null) {
            for (IModelPublishableProperty prop : pubSupport.list(env, EScope.LOCAL_AND_OVERWRITE, null)) {
                if (prop instanceof IAdsPresentableProperty) {
                    if (prop.isTransferable(env)) {
                        ServerPresentationSupport support = ((IAdsPresentableProperty) prop).getPresentationSupport();
                        if (support != null) {
                            PropertyPresentation pps = support.getPresentation();
                            if (pps != null && pps.isPresentable()) {
                                result.add(prop.getId());
                            }
                        }
                    }
                }
            }
        }
        return result;
    }

    @Override
    public void visitChildren(IVisitor visitor, VisitorProvider provider) {
        super.visitChildren(visitor, provider);
        properties.visit(visitor, provider);
        methods.visit(visitor, provider);
        propertyGroup.visit(visitor, provider);
        methodGroup.visit(visitor, provider);
        header.visit(visitor, provider);
        body.visit(visitor, provider);
        nestedClasses.visit(visitor, provider);
        resources.visit(visitor, provider);
        if (accessFlags != null) {
            accessFlags.visit(visitor, provider);
        }
        if (this instanceof IAdsPresentableClass) {
            ClassPresentations prs = ((IAdsPresentableClass) this).getPresentations();
            if (prs != null) {
                prs.visit(visitor, provider);
            }
        }
        this.inheritance.visit(visitor, provider);
    }

    @Override
    public void appendTo(AdsDefinitionElementType xDefRoot, ESaveMode saveMode) {//has mean
        appendTo(xDefRoot.addNewAdsClassDefinition(), saveMode);
    }

    public abstract EClassType getClassDefType();

    /**
     * Returns text of outer definitions of class
     */
    public ClassSource getHeader() {
        return header;
    }

    /**
     * Returns text of inner definitions of class
     */
    public ClassSource getBody() {
        return body;
    }

    protected class ClassHierarchy extends DefaultHierarchy<AdsClassDef> {

        @Override
        public SearchResult<AdsClassDef> findOverwritten() {
            if (!isNested()) {
                return super.findOverwritten();
            }

            final List<AdsClassDef> ovrClasses = new LinkedList<>();
            getOwnerClass().getHierarchy().findOverwritten().save(ovrClasses);
            final Set<AdsClassDef> looked = new HashSet<>();

            final List<AdsClassDef> resultSet = new LinkedList<>();
            findInClasses(ovrClasses, resultSet, looked);
            if (resultSet.isEmpty()) {
                return SearchResult.empty();
            } else {
                return SearchResult.list(resultSet);
            }
        }

        private void findInClasses(List<AdsClassDef> ovrs, List<AdsClassDef> resultSet, final Set<AdsClassDef> looked) {
            List<AdsClassDef> searchNext = null;
            for (final AdsClassDef ovr : ovrs) {
                if (looked.contains(ovr)) {
                    continue;
                }
                looked.add(ovr);
                final AdsClassDef ovr_member = findMember(ovr, getId());
                if (ovr_member != null && !resultSet.contains(ovr_member)) {
                    resultSet.add(ovr_member);
                } else {
                    if (searchNext == null) {
                        searchNext = new LinkedList<>();
                    }
                    searchNext.add(ovr);
                }
            }
            if (searchNext != null) {
                for (final AdsClassDef clazz : searchNext) {
                    final List<AdsClassDef> newOvrs = clazz.getHierarchy().findOverwritten().all();
                    findInClasses(newOvrs, resultSet, looked);
                }
            }
        }

        private AdsClassDef findMember(AdsClassDef clazz, Id id) {
            return clazz.getNestedClasses().findById(id, EScope.LOCAL).get();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public Hierarchy<AdsClassDef> getHierarchy() {
        return new ClassHierarchy();
    }

    @Override
    public AdsAccessFlags getAccessFlags() {
        return accessFlags;
    }
    // separate and static to fix deadlock
    private AtomicBoolean provideLocalDependencesOnly = new AtomicBoolean(false);

    private class ClassDependenceProvider extends DefaultDependenceProvider {

        private final Map<Id, Dependence> moduleId2Dependence = new HashMap<>();
        private boolean inited = false;
        private final Object initLock = new Object();

        public ClassDependenceProvider() {
            super(AdsClassDef.this);
        }

        @Override
        public void collect(final Map<Id, Dependence> moduleId2Dependence) {

            try {
                provideLocalDependencesOnly.set(true);
                super.collect(moduleId2Dependence);

                List<AdsTypeDeclaration> refs = getInheritance().getInerfaceRefList(EScope.LOCAL);

                final AdsTypeDeclaration superClass = getInheritance().getSuperClassRef();
                if (superClass != null) {
                    if (refs == null) {
                        refs = new ArrayList<>();
                    }
                    refs.add(0, superClass);
                }

                if (refs != null) {
                    for (AdsTypeDeclaration decl : refs) {
                        final AdsType type = decl.resolve(AdsClassDef.this).get();
                        if (type instanceof AdsDefinitionType) {
                            final Definition source = ((AdsDefinitionType) type).getSource();
                            if (source != null) {
                                source.getDependenceProvider().collect(moduleId2Dependence);
                            }
                        }
                    }
                }
            } finally {
                provideLocalDependencesOnly.set(false);

            }
        }

        @Override
        public Map<Id, Dependence> get() {
            synchronized (initLock) {
                if (!inited) {
                    try {
                        collect(moduleId2Dependence);
                    } finally {
                        inited = true;
                    }
                }
            }
            return moduleId2Dependence;
        }
    }

    private class DependenceProviderLink extends ObjectLink<ClassDependenceProvider> {

        private ClassDependenceProvider provider;

        @Override
        protected ClassDependenceProvider search() {
            return new ClassDependenceProvider();
        }

        public ClassDependenceProvider get() {
            provider = find();
            if (provider == null) {
                provider = update();
            }
            return provider;
        }
    }
    private final DependenceProviderLink providerLink = new DependenceProviderLink();

    @Override
    public IDependenceProvider getDependenceProvider() {
        if (provideLocalDependencesOnly.get()) {
            return super.getDependenceProvider();
        } else {
            return providerLink.get();
        }
    }

    @Override
    public SearchResult<? extends AdsDefinition> findComponentDefinition(final Id id) {
        if (IdPrefixes.isAdsMethodId(id)) {
            return getMethods().findById(id, EScope.ALL);
        } else if (IdPrefixes.isAdsPropertyId(id)) {
            return getProperties().findById(id, EScope.ALL);
        } else if (IdPrefixes.isAdsNestedClassId(id)) {
            SearchResult<? extends AdsDefinition> nested = getNestedClasses().findById(id, EScope.ALL);
            if (nested.isEmpty()) {
                return super.findComponentDefinition(id);
            } else {
                return nested;
            }
        } else {
            return super.findComponentDefinition(id);
        }
    }

    public boolean setUsageEnvironment(final ERuntimeEnvironmentType env) {
        return false;
    }

    @Override
    public AdsTransparence getTransparence() {
        return null;
    }

    @Override
    public AdsType getType(final EValType typeId, final String extStr) {
        return AdsClassType.Factory.newInstance(this);
    }

    protected class ClassJavaSourceSupport extends JavaSourceSupport {

        public ClassJavaSourceSupport() {
            super(AdsClassDef.this);
        }

        @Override
        @SuppressWarnings("unchecked")
        public CodeWriter getCodeWriter(final UsagePurpose purpose) {
            return new AdsClassWriter(this, AdsClassDef.this, purpose);
        }

        @Override
        public EnumSet<ERuntimeEnvironmentType> getSupportedEnvironments() {
            final AdsTransparence transparece = AdsClassDef.this.getTransparence();
            if (transparece != null && transparece.isTransparent() && !transparece.isExtendable()) {
                return EnumSet.of(ERuntimeEnvironmentType.SERVER, ERuntimeEnvironmentType.EXPLORER);
            }
            switch (getClassDefType()) {
                case ENTITY:
                case ENTITY_GROUP:
                case APPLICATION:
                case FORM_HANDLER:
                case ALGORITHM:
                    return EnumSet.of(ERuntimeEnvironmentType.SERVER, ERuntimeEnvironmentType.EXPLORER, ERuntimeEnvironmentType.WEB);
                case ENTITY_MODEL:
                case GROUP_MODEL:
                case FILTER_MODEL:
                case PARAGRAPH_MODEL:
                case REPORT_MODEL:
                case FORM_MODEL:
                    return EnumSet.of(ERuntimeEnvironmentType.EXPLORER, ERuntimeEnvironmentType.WEB);
                case REPORT:
//                    if (((AdsReportClassDef) AdsClassDef.this).isUserReport()) {
//                        return EnumSet.of(ERuntimeEnvironmentType.SERVER);
//                    } else {
                    return EnumSet.of(ERuntimeEnvironmentType.SERVER, ERuntimeEnvironmentType.EXPLORER, ERuntimeEnvironmentType.WEB);
                //}
                default:
                    break;
            }

            return getUsageEnvironment() == ERuntimeEnvironmentType.COMMON
                    ? EnumSet.of(ERuntimeEnvironmentType.SERVER, ERuntimeEnvironmentType.EXPLORER, ERuntimeEnvironmentType.COMMON, ERuntimeEnvironmentType.COMMON_CLIENT, ERuntimeEnvironmentType.WEB)
                    : (getUsageEnvironment() == ERuntimeEnvironmentType.COMMON_CLIENT && isDual() ? EnumSet.of(ERuntimeEnvironmentType.EXPLORER, ERuntimeEnvironmentType.WEB) : EnumSet.of(getUsageEnvironment()));
            //return ;
        }

        @Override
        public Set<CodeType> getSeparateFileTypes(final ERuntimeEnvironmentType e) {
            Set<CodeType> defaultResult = getSeparateFileTypesImpl(e);
            if (isCodeEditable()) {
                return defaultResult;
            } else {
                if (defaultResult.contains(CodeType.META)) {
                    return EnumSet.of(CodeType.META);
                } else {
                    return EnumSet.noneOf(CodeType.class);
                }
            }
        }

        @Override
        public Set<CodeType> getSeparateFileTypesForSearch(ERuntimeEnvironmentType sc) {
            return getSeparateFileTypesImpl(sc);
        }

        private Set<CodeType> getSeparateFileTypesImpl(final ERuntimeEnvironmentType e) {
            if (AdsClassDef.this.isNested()) {
                return EnumSet.noneOf(CodeType.class);
            }
            final AdsTransparence transparece = AdsClassDef.this.getTransparence();
            if (transparece != null && transparece.isTransparent() && !transparece.isExtendable()) {
                return (e == ERuntimeEnvironmentType.SERVER || e == ERuntimeEnvironmentType.COMMON_CLIENT) && (getUsageEnvironment() == ERuntimeEnvironmentType.COMMON || getUsageEnvironment() == e)
                        ? EnumSet.of(CodeType.META) : EnumSet.noneOf(CodeType.class);
            }
            ERuntimeEnvironmentType clientEnv = null;
            boolean isModelClass = false;


            switch (getClassDefType()) {
                case ENTITY:
                case APPLICATION:
                case FORM_HANDLER:
                    if (e.isClientEnv()) {
                        //AdsEntityObjectClassDef clazz = (AdsEntityObjectClassDef) AdsClassDef.this;
                        ERuntimeEnvironmentType env = AdsClassDef.this.getClientEnvironment();
                        if (e != ERuntimeEnvironmentType.COMMON_CLIENT && (e == env || env == ERuntimeEnvironmentType.COMMON_CLIENT)) {
                            return EnumSet.of(CodeType.EXCUTABLE, CodeType.META);
                        } else {
                            return EnumSet.noneOf(CodeType.class);
                        }
                    } else {
                        if (e == ERuntimeEnvironmentType.SERVER) {
                            return EnumSet.of(CodeType.EXCUTABLE, CodeType.META);
                        } else {
                            return EnumSet.noneOf(CodeType.class);
                        }
                    }
                case ALGORITHM:
                    if (e.isClientEnv()) {
                        if (e == ERuntimeEnvironmentType.COMMON_CLIENT) {
                            return EnumSet.noneOf(CodeType.class);
                        } else {
                            return EnumSet.of(CodeType.META);
                        }
                    } else {
                        if (e == ERuntimeEnvironmentType.SERVER) {
                            return EnumSet.of(CodeType.EXCUTABLE, CodeType.META);
                        } else {
                            return EnumSet.noneOf(CodeType.class);
                        }
                    }
                case ENTITY_GROUP:
                case REPORT:
                    if (e == ERuntimeEnvironmentType.SERVER || e == ERuntimeEnvironmentType.EXPLORER || e == ERuntimeEnvironmentType.WEB) {
                        return EnumSet.of(CodeType.EXCUTABLE, CodeType.META);
                    } else {
                        return EnumSet.noneOf(CodeType.class);
                    }
                case ENTITY_MODEL:
//                    AdsEditorPresentationDef epr = ((AdsEntityModelClassDef) AdsClassDef.this).getOwnerEditorPresentation();
//                    clientEnv = epr.getClientEnvironment();
//                    isModelClass = true;
                    clientEnv = ((AdsEntityModelClassDef) AdsClassDef.this).getClientEnvironment();
                    isModelClass = true;
                    break;
                case GROUP_MODEL:
                    AdsSelectorPresentationDef spr = ((AdsGroupModelClassDef) AdsClassDef.this).getOwnerSelectorPresentation();
                    clientEnv = spr.getClientEnvironment();
                    isModelClass = true;
                    break;
                case FILTER_MODEL:
                    clientEnv = ((AdsFilterModelClassDef) AdsClassDef.this).getOwnerFilterDef().getClientEnvironment();
                    isModelClass = true;
                    break;
                case PARAGRAPH_MODEL:
                    AdsParagraphExplorerItemDef par = ((AdsParagraphModelClassDef) AdsClassDef.this).getOwnerParagraph();
                    clientEnv = par.getClientEnvironment();
                    isModelClass = true;
                    break;
                case REPORT_MODEL:
                    isModelClass = true;
                    break;
                case FORM_MODEL:
                    AdsFormHandlerClassDef form = ((AdsFormModelClassDef) AdsClassDef.this).getOwnerClass();
                    clientEnv = form.getClientEnvironment();
                    isModelClass = true;
                    break;
                default:
                    break;
            }
            if (isModelClass) {

                if (clientEnv != null && clientEnv != ERuntimeEnvironmentType.COMMON_CLIENT) {
                    if (e == clientEnv) {
                        return EnumSet.of(CodeType.EXCUTABLE, CodeType.META);
                    } else {
                        return EnumSet.noneOf(CodeType.class);
                    }
                } else {
                    if (e == ERuntimeEnvironmentType.WEB || e == ERuntimeEnvironmentType.EXPLORER) {
                        return EnumSet.of(CodeType.EXCUTABLE, CodeType.META);
                    } else {
                        return EnumSet.noneOf(CodeType.class);
                    }
                }
            }

            //return e == getUsageEnvironment() ? EnumSet.of(CodeType.EXCUTABLE) : null;
            Set<CodeType> set = EnumSet.noneOf(CodeType.class);
            if (e == getUsageEnvironment() || (getUsageEnvironment() == ERuntimeEnvironmentType.COMMON_CLIENT && isDual() && e.isClientEnv() && e != getUsageEnvironment())) {
                set.add(CodeType.EXCUTABLE);
            }
            if (e != ERuntimeEnvironmentType.COMMON) {
                set.add(CodeType.META);
            }

            return set;
        }
    }

    @Override
    public JavaSourceSupport getJavaSourceSupport() {
        return new ClassJavaSourceSupport();
    }

    @Override
    public Jml getSource(final String name) {
        if (name == null) {
            return null;
        }
        if (name.startsWith(AdsClassDef.CLASS_HEADER_NAME)) {
            return header.getSource(name);
        } else if (name.startsWith(AdsClassDef.CLASS_BODY_NAME)) {
            return body.getSource(name);
        } else {
            return null;
        }

    }

    @Override
    public ClipboardSupport<? extends AdsClassDef> getClipboardSupport() {
        return new AdsClassClipboardSupport(this);
    }

    @Override
    public boolean isSuitableContainer(final AdsDefinitions collection) {
        switch (AdsClassDef.this.getClassDefType()) {
            case ALGORITHM:
            case PRESENTATION_ENTITY_ADAPTER:
            case DYNAMIC:
            case APPLICATION:
            case EXCEPTION:
            case FORM_HANDLER:
            case INTERFACE:
            case REPORT:
            case ENTITY:
            case ENTITY_GROUP:
            case SQL_CURSOR:
            case SQL_PROCEDURE:
            case SQL_STATEMENT:
            case ENUMERATION:
                return collection instanceof ModuleDefinitions;
            default:
                return false;
        }
    }

    @Override
    public EDefType getDefinitionType() {
        return EDefType.CLASS;
    }

    @Override
    public Support getModelPublishablePropertySupport() {
        return new AdsClassModelPropertyPublishingSupport(this);
    }

    @Override
    public void setFinal(boolean isFinal) {
        super.setFinal(isFinal);
        if (isFinal && getAccessFlags().isAbstract()) {
            getAccessFlags().setAbstract(false);
        }
    }

    @Override
    public boolean canBeFinal() {
        return super.canBeFinal() && !getAccessFlags().isAbstract() && getClassDefType() != EClassType.INTERFACE;
    }

    @Override
    public EAccess getMinimumAccess() {
        return isNested() ? EAccess.PRIVATE : EAccess.DEFAULT;
    }

    @Override
    public boolean isDeprecated() {
        return getAccessFlags().isDeprecated();
    }

    public boolean isDual() {
        return false;
    }

    @Override
    public boolean canChangeAccessMode() {
        return isNested() || super.canChangeAccessMode();
    }

    /**
     *
     * @return {@code true} if class is anonymous, i.e. class is inner and
     * without name.
     */
    public boolean isAnonymous() {
        return false;
    }

    /**
     * Indicates that class is nested. Nested class is a class declared entirely
     * within the body of another class or interface. Nested class may be both
     * static and non-static.
     *
     * @return {@code true} if class is nested, {@code false} otherwise.
     */
    public boolean isNested() {
        // implementation by default, JIC
        return isAnonymous();
    }

    /**
     * Indicates that class is nested and non-static, in other words, the
     * instance of this class can't existing without instance of enclosing
     * class. Instance of these classes has a reference to an instance of the
     * enclosing class.
     *
     * @return {@code true} if class is nested and non-static, {@code false}
     * otherwise.
     */
    public boolean isInner() {
        return isNested() && !getAccessFlags().isStatic();
    }

    /**
     * Gets top-level outer (enclosing) class.
     *
     * @return top-level outer (enclosing) class (may be this, can't be null).
     */
    public AdsClassDef getTopLevelEnclosingClass() {
        AdsClassDef ownerClass = this;

        while (ownerClass != null && ownerClass.isNested()) {
            ownerClass = ownerClass.getOwnerClass();
        }
        return ownerClass;
    }

    /**
     * Gets list of classes from current (or top-level enclosing) class to
     * top-level enclosing non-nested class (or current).
     */
    public List<AdsClassDef> getNestedClassesChain(boolean includeThis, final boolean startFromTopLevelEnclosing) {
        return getNestedClassWalker(includeThis).walk(
                new IterableWalker.Acceptor<AdsClassDef, List<AdsClassDef>>(new ArrayList<AdsClassDef>()) {
            @Override
            public void accept(AdsClassDef object) {
                if (startFromTopLevelEnclosing) {
                    getResult().add(0, object);
                } else {
                    getResult().add(object);
                }
            }
        });
    }

    /**
     * Walk along the nested hierarchy startint from this.
     *
     * @return instance of NestedClassWalker
     */
    public IWalker<AdsClassDef> getNestedClassWalker() {
        return getNestedClassWalker(true);
    }

    /**
     * Walk along the nested hierarchy startint from this if {@code skipThis} is
     * {@code true} or first enclosing class otherwise.
     *
     * @return instance of NestedClassWalker
     */
    public IWalker<AdsClassDef> getNestedClassWalker(boolean includeThis) {
        return new NestedClassWalker(this, includeThis);
    }

    private static final class NestedClassWalker extends RadixObjectWalker<AdsClassDef> {

        final boolean includeThis;

        public NestedClassWalker(AdsClassDef object, boolean includeThis) {
            super(AdsClassDef.class, object);
            this.includeThis = includeThis;
        }

        @Override
        protected boolean stopWalk(AdsClassDef object) {
            return object == null || !object.isNested();
        }

        @Override
        protected boolean skip(AdsClassDef object) {
            return super.skip(object) || (!includeThis && object == this.object);
        }
    }

    public boolean isCodeEditable() {
        if (isReadOnly()) {
            return false;
        }
        AdsDefinition ovr = getHierarchy().findOverwritten().get();
        if (ovr == null) {
            return true;
        } else {

            AdsDefinition topLevel = ovr.findTopLevelDef();
            if (topLevel == null) {
                return false;
            } else {
                return topLevel.getSaveMode() == ESaveMode.NORMAL;
            }

        }
    }

    public String getRuntimeLocalClassName() {

        if (isNested()) {
            final StringBuilder sb = new StringBuilder();
            boolean first = true;
            for (final AdsClassDef adsClassDef : getNestedClassesChain(true, true)) {
                if (first) {
                    first = false;
                } else {
                    sb.append('.');
                }
                if (adsClassDef instanceof PropertyPresentationEmbeddedClass) {
                    sb.append(adsClassDef.getOwnerDef().getId().toString());
                } else {
                    sb.append(adsClassDef.getId().toString());
                }
            }
            return sb.toString();
        }

        return getId().toString();
    }

    @Override
    public RadixdocSupport<? extends RadixObject> getRadixdocSupport() {
        return new RadixdocSupport<AdsClassDef>(this) {
            @Override
            public IRadixdocPage document(Page page, DocumentOptions options) {
                return new ClassRadixdoc(getSource(), page, options);
            }
        };
    }
    
    protected void onSuperClassChanged(AdsTypeDeclaration newSuperClass){
    }

    @Override
    public boolean isRadixdocProvider() {
        return true;
    }
}
