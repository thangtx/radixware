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

import java.lang.reflect.Method;
import java.util.*;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.check.RadixProblem;
import org.radixware.kernel.common.check.RadixProblem.ProblemFixSupport;
import org.radixware.kernel.common.check.RadixProblem.WarningSuppressionSupport;
import org.radixware.kernel.common.defs.*;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.ads.*;
import org.radixware.kernel.common.defs.ads.AdsDefinition.Hierarchy;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassMember;
import org.radixware.kernel.common.defs.ads.clazz.IAccessible;
import org.radixware.kernel.common.defs.ads.clazz.PropertyGroups;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityObjectClassDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsSortingDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.IAdsPresentableProperty;
import org.radixware.kernel.common.defs.ads.clazz.presentation.PropertyPresentation;
import org.radixware.kernel.common.defs.ads.clazz.sql.AdsSqlClassDef;
import org.radixware.kernel.common.defs.ads.common.JavaSignatures;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.defs.ads.module.AdsPath;
import org.radixware.kernel.common.defs.ads.profiling.AdsProfileSupport;
import org.radixware.kernel.common.defs.ads.src.IJavaSource;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport.UsagePurpose;
import org.radixware.kernel.common.defs.ads.src.clazz.AdsPropertyWriter;
import org.radixware.kernel.common.defs.ads.type.*;
import org.radixware.kernel.common.defs.ads.type.AdsClassType.EntityObjectType;
import org.radixware.kernel.common.defs.localization.ILocalizedDescribable;
import org.radixware.kernel.common.defs.localization.ILocalizingBundleDef;
import org.radixware.kernel.common.defs.localization.LocalizedDescribableRef;
import org.radixware.kernel.common.enums.*;
import org.radixware.kernel.common.exceptions.NoConstItemWithSuchValueError;
import org.radixware.kernel.common.jml.IJmlSource;
import org.radixware.kernel.common.jml.Jml;
import org.radixware.kernel.common.jml.JmlTagInvocation;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.scml.LineMatcher.ILocationDescriptor;
import org.radixware.kernel.common.scml.Scml;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.common.utils.events.IRadixEventListener;
import org.radixware.kernel.common.utils.events.RadixEvent;
import org.radixware.kernel.common.utils.events.RadixEventSource;
import org.radixware.schemas.adsdef.*;
import org.radixware.schemas.commondef.CbInfo;
import org.radixware.schemas.commondef.ProfileInfo;
import org.radixware.schemas.xscml.JmlType;

public abstract class AdsPropertyDef extends AdsClassMember implements IDeprecatedInheritable, IOverridable<AdsPropertyDef>, IOverwritable<AdsPropertyDef>, IJavaSource, IJmlSource, IModelPublishableProperty, ITransparency, IAccessible, ILocalizedDescribable.Inheritable {

    public static final class Problems extends AdsDefinitionProblems {

        public static final int COLUMN_DEFAULT_VALUE_MISMATCH = 2000;
        public static final int OVERRIDE_FINAL_PROPERTY = 2001;
        public static final int PARENT_REFERENCE_IS_ALREADY_PUBLISHED = 2002;
        public static final int NULL_INITIAL_VALUE_FOR_DEFINEABLE_PROPERTY = 2003;
        public static final int AUDIT_UPDATE_SHOULD_BE_ENABLED = 2004;
        public static final int READ_LOB_SEPARATELY = 2005;
        public static final int NO_TITLE_FOR_AUDIT_PROPERTY = 2006;

        private Problems(AdsPropertyDef prop, List<Integer> warnings) {
            super(prop);
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
            switch (code) {
                case COLUMN_DEFAULT_VALUE_MISMATCH:
                case PARENT_REFERENCE_IS_ALREADY_PUBLISHED:
                case NULL_INITIAL_VALUE_FOR_DEFINEABLE_PROPERTY:
                case AUDIT_UPDATE_SHOULD_BE_ENABLED:
                case READ_LOB_SEPARATELY:
                case NO_TITLE_FOR_AUDIT_PROPERTY:
                    return true;
                default:
                    return super.canSuppressWarning(code);
            }
        }

        private class InitValSync implements Fix {

            final ColumnPropertyValue val;

            public InitValSync(ColumnPropertyValue val) {
                this.val = val;
            }

            @Override
            public void fix() {
                val.updateInitValFromColumn();
            }

            @Override
            public String getDescription() {
                return "Synchronyze with column initial value";
            }
        }

        @Override
        public boolean canFix(int code, List<Fix> fixes) {
            switch (code) {
                case COLUMN_DEFAULT_VALUE_MISMATCH:
                    if (owner instanceof ColumnProperty && ((AdsPropertyDef) owner).getValue() instanceof ColumnPropertyValue) {
                        fixes.add(new InitValSync((ColumnPropertyValue) ((AdsPropertyDef) owner).getValue()));
                        return true;
                    }
                    return false;

                default:
                    return false;
            }
        }
    }

    public static class Factory {

        protected Factory() {
        }

        public static AdsPropertyDef loadFrom(AbstractPropertyDefinition xProp) {
            return loadFrom(null, xProp);
        }

        public static AdsPropertyDef loadFrom(RadixObject context, AbstractPropertyDefinition xProp) {

            if (xProp.getAccessRules() != null && xProp.getAccessRules().isSetTransparence()) {
                return new AdsTransparentPropertyDef(xProp);
            }

            final EPropNature nature = xProp.getNature();
            switch (nature) {
                case DETAIL_PROP:
                    try {
                        final EValType valType = xProp.getType().getTypeId();
                        if (valType == EValType.PARENT_REF) {
                            return new AdsDetailRefPropertyDef(xProp);
                        } else {
                            return new AdsDetailColumnPropertyDef(xProp);
                        }
                    } catch (NoConstItemWithSuchValueError e) {
                        return null;
                    }
                case DYNAMIC:
                    return new AdsDynamicPropertyDef(context, xProp);
                case EVENT_CODE:
                    return new AdsEventCodePropertyDef(xProp);
                case SQL_CLASS_PARAMETER:
                    return new AdsParameterPropertyDef(xProp);
                case EXPRESSION:
                    return new AdsExpressionPropertyDef(xProp);
                case FIELD:
                    return new AdsFieldPropertyDef(xProp);
                case FIELD_REF:
                    return new AdsFieldRefPropertyDef(xProp);
//                case FORM_PROPERTY:
//                    return new AdsFormPropertyDef(xProp);
                case GROUP_PROPERTY:
                    return new AdsGroupPropertyDef(xProp);
                case INNATE: {
                    try {
                        final EValType valType = xProp.getType().getTypeId();
                        if (valType == EValType.PARENT_REF) {
                            return new AdsInnateRefPropertyDef(xProp);
                        } else {
                            return new AdsInnateColumnPropertyDef(xProp);
                        }
                    } catch (NoConstItemWithSuchValueError e) {

                        return null;
                    }
                }
                case PARENT_PROP:
                    return new AdsParentPropertyDef(xProp);
                case PROPERTY_PRESENTATION:
                    return new AdsPropertyPresentationPropertyDef(xProp);
                case USER:
                    return new AdsUserPropertyDef(xProp);

            }
            return null;
        }
    }

    public final class Profile extends RadixObject {

        private Profile(AdsPropertyDef owner) {
            setContainer(owner);
        }

        @Override
        public ENamingPolicy getNamingPolicy() {
            return ENamingPolicy.CALC;
        }

        @Override
        public String getName() {
            AdsPropertyDef owner = (AdsPropertyDef) getContainer();

            if (owner == null) {
                return "UNDEFINED";
            }

            AdsTypeDeclaration decl = owner.getTypedObject().getType();
            if (decl == null) {
                decl = AdsTypeDeclaration.UNDEFINED;
            }
            return decl.getQualifiedName(owner) + " " + owner.getName();
        }

        public char[] getSignature(AdsClassDef context) {
            synchronized (this) {
                return JavaSignatures.generateSignature(context == null ? getOwnerClass() : context, AdsPropertyDef.this);
            }
        }
    }

    protected static class PropertyClipboardSupport extends AdsClipboardSupport<AdsPropertyDef> {

        private static class AdsPropertyTransfer extends AdsTransfer<AdsPropertyDef> {

            public AdsPropertyTransfer(AdsPropertyDef source) {
                super(source);
            }

            public AdsPropertyTransfer(AdsPropertyDef source, CbInfo info) {
                super(source, info);
            }

            @Override
            public void afterPaste() {
                super.afterPaste();
                updateThisRefs();

            }

            private void updateThisRefs() {
                AdsPropertyDef prop = getObject();
                if (prop != null) {
                    if (prop.getter != null) {
                        for (ERuntimeEnvironmentType env : ERuntimeEnvironmentType.values()) {
                            Jml jml = prop.getter.getSource(env);
                            if (jml != null) {
                                updateJml(jml, prop);
                            }
                        }
                    }
                    if (prop.setter != null) {
                        for (ERuntimeEnvironmentType env : ERuntimeEnvironmentType.values()) {
                            Jml jml = prop.setter.getSource(env);
                            if (jml != null) {
                                updateJml(jml, prop);
                            }
                        }
                    }
                }
            }

            private void updateJml(Jml jml, AdsPropertyDef prop) {

                for (Scml.Item item : jml.getItems()) {
                    if (item instanceof JmlTagInvocation) {
                        JmlTagInvocation invoke = (JmlTagInvocation) item;
                        if (invoke.isInternalPropertyAccessor()) {
                            invoke.setPath(new AdsPath(prop));
                        }
                    }
                }
            }

            @Override
            public void afterPaste(Definition ownerDef) {
                super.afterPaste(ownerDef);
                updateThisRefs();
            }
        }

        public PropertyClipboardSupport(AdsPropertyDef radixObject) {
            super(radixObject);
        }

        @Override
        protected XmlObject copyToXml() {
            PropertyDefinition xDef = PropertyDefinition.Factory.newInstance();
            radixObject.appendTo(xDef, ESaveMode.NORMAL);
            return xDef;
        }

        @Override
        protected AdsPropertyDef loadFrom(XmlObject xmlObject) {
            if (xmlObject instanceof PropertyDefinition) {
                AdsPropertyDef prop = Factory.loadFrom((PropertyDefinition) xmlObject);
                if (prop.getUsageEnvironment() == ERuntimeEnvironmentType.EXPLORER || prop.getUsageEnvironment() == ERuntimeEnvironmentType.WEB) {
                    prop.setUsageEnvironment(null);
                }
                return prop;
            } else {
                return super.loadFrom(xmlObject);
            }
        }

        @Override
        protected boolean isIdChangeRequired(RadixObject copyRoot) {
            if (copyRoot != radixObject) {
                if (super.isIdChangeRequired(copyRoot)) {
                    AdsPropertyDef ovr = radixObject.getHierarchy().findOverridden().get();
                    if (ovr != null) {
                        return false;
                    } else {
                        return true;
                    }
                } else {
                    return false;
                }
            } else {
                return super.isIdChangeRequired(copyRoot);
            }
        }

        @Override
        protected Method getDecoderMethod() {
            try {
                return Factory.class.getDeclaredMethod("loadFrom", AbstractPropertyDefinition.class);
            } catch (Throwable e) {//this is normal to catch Throwable here
                return null;
            }
        }

        @Override
        public boolean isEncodedFormatSupported() {
            return true;
        }

        @Override
        protected Transfer<AdsPropertyDef> newTransferInstance() {
            return new AdsPropertyTransfer(radixObject);
        }

        @Override
        protected Transfer<AdsPropertyDef> newTransferInstance(CbInfo info) {
            return new AdsPropertyTransfer(radixObject, info);
        }
    }
    protected final PropertyValue value;
    private final AdsAccessFlags accessFlags;
    private final ValueInheritanceRules valueInheritanceRules;
    private boolean isConst;
    private boolean isOverride;
    private Getter getter;
    private Setter setter;
    private final Profile profile;
    private Problems warningsSupport = null;
    private ERuntimeEnvironmentType usageEnvironment;
    private EAccess readAccess;
    private EAccess writeAccess;
    private boolean isDescriptionInherited;
    private boolean isSetIsDescriptionInherited = true;
    protected LocalizedDescribableRef describableRef;

    protected AdsPropertyDef(final AdsPropertyDef source, final boolean forOverride) {
        super(source.getId(), source.getName());
        this.value = PropertyValue.Factory.newCopy(this, source.value);
        this.accessFlags = AdsAccessFlags.Factory.newCopy(this, source.accessFlags);
        this.isConst = source.isConst;
        this.isOverride = true;
        this.isDescriptionInherited = source.isDescriptionInherited ? source.isDescriptionInherited : isDescriptionInheritable();

        if (forOverride) {
            this.getter = null;
            this.setter = null;
        }

        this.valueInheritanceRules = ValueInheritanceRules.Factory.newCopy(this, source.valueInheritanceRules);
        this.profile = new Profile(this);
    }

    public boolean isSetReadAccess() {
        return readAccess != null;
    }

    public boolean isSetWriteAccess() {
        return writeAccess != null;
    }

    public boolean isSetIsDescriptionInherited() {
        return isSetIsDescriptionInherited;
    }

    /**
     * Gets read access mode of current property.
     *
     * @return specific read access mode if method {@code isSetReadAccess}
     * return {@code true}, access mode of property otherwise.
     */
    public EAccess getReadAccess() {
        return (EAccess) Utils.nvl(readAccess, getAccessMode());
    }

    @Override
    public boolean isDescriptionInheritable() {
        if (getDescriptionId() == null && getDescription() != null && !getDescription().isEmpty()) {
            return false;
        }

        return (isOverride() || getHierarchy().findOverridden().get() != null)
                || (isOverwrite() || getHierarchy().findOverwritten().get() != null);
    }

    @Override
    public void setDescriptionInherited(boolean inherit) {
        if (!isDescriptionInheritable()) {
            return;
        }

        boolean isInherit = isDescriptionInherited();
        if (inherit != isInherit) {
            isDescriptionInherited = inherit;
            isSetIsDescriptionInherited = true;
            setEditState(EEditState.MODIFIED);
        }
    }

    @Override
    public boolean isDescriptionInherited() {
        return isDescriptionInherited;
    }

    @Override
    public void setDescriptionId(Id id) {
        if (isDescriptionInherited()) {
            return;
        }

        super.setDescriptionId(id);
    }

    @Override
    public Id getDescriptionId() {
        return getDescriptionId(isDescriptionInherited());
    }

    public Id getDescriptionId(boolean inherited) {
        if (inherited) {
            Definition owner = getDescriptionLocation(inherited);
            if (owner == this) {
                return null;
            } else {
                return owner.getDescriptionId();
            }
        }
        return super.getDescriptionId();
    }

    @Override
    public Definition getDescriptionLocation() {
        return getDescriptionLocation(isDescriptionInherited());
    }

    @Override
    public Definition getDescriptionLocation(boolean inherit) {
        Definition owner = super.getDescriptionLocation();
        if (owner == this && inherit) {
            if (describableRef != null) {
                if (describableRef.getVersion() == ILocalizingBundleDef.version.get()) {
                    ILocalizedDescribable def = describableRef.get();
                    if (def instanceof Definition) {
                        return (Definition) def;
                    }
                }
            }
            if (getOwnerClass() == null) {
                return owner;
            }
            MemberHierarchyIterator<AdsPropertyDef> iter = getHierarchyIterator(EScope.ALL, HierarchyIterator.Mode.FIND_ALL);

            while (iter.hasNext()) {
                AdsPropertyDef ovr = iter.next().first();
                if (!ovr.isDescriptionInherited()) {
                    describableRef = new LocalizedDescribableRef(ovr);
                    return ovr;
                }
            }
        }
        return owner;
    }

    /**
     * Gets write access mode of current property.
     *
     * @return specific write access mode if method {@code isSetWriteAccess}
     * return {@code true}, access mode of property otherwise.
     */
    public EAccess getWriteAccess() {
        return (EAccess) Utils.nvl(writeAccess, getAccessMode());
    }

    public void setReadAccess(EAccess readAccess) {
        if (this.readAccess != readAccess) {
            this.readAccess = readAccess;
            setEditState(EEditState.MODIFIED);
        }
    }

    public void setWriteAccess(EAccess writeAccess) {
        if (this.writeAccess != writeAccess) {
            this.writeAccess = writeAccess;
            setEditState(EEditState.MODIFIED);
        }
    }

    public Profile getProfile() {
        return profile;
    }

    /**
     * Answers is property available for sql or not
     */
    public boolean hasDbRepresentation() {
        switch (getNature()) {
            case INNATE:
            case USER:
            case PARENT_PROP:
            case DETAIL_PROP:
            case EXPRESSION:
                return true;
            default:
                return false;
        }
    }

    @Override
    public void afterOverride() {
        cleanupGetters();
        setOverride(true);

        if (isOverwrite()) {
            setOverwrite(!getHierarchy().findOverwritten().isEmpty());
        }

        setDescriptionInherited(true);
        if (isDescriptionInherited() && super.getDescriptionId() != null) {
            super.setDescriptionId(null);
        } 
    }

    @Override
    public void afterOverwrite() {
        cleanupGetters();
        setOverwrite(true);

        setDescriptionInherited(true);
        if (isDescriptionInherited() && super.getDescriptionId() != null) {
            super.setDescriptionId(null);
        }
    }

    @Override
    public boolean allowOverwrite() {
        return true;
    }

    private void cleanupGetters() {
        synchronized (this) {
            if (this.getter != null) {
                this.getter.delete();
                this.getter = null;
            }
            if (this.setter != null) {
                this.setter.delete();
                this.setter = null;
            }
            if (this.getterAndSetterChangeSupport != null) {
                getterAndSetterChangeSupport.fireEvent(new GetterAndSetterChangeEvent(true, false, true, false));
            }
        }
    }

    @SuppressWarnings("unchecked")
    protected AdsPropertyDef(final AbstractPropertyDefinition xDef) {
        super(xDef);
        this.value = PropertyValue.Factory.loadFrom(this, xDef);
        this.accessFlags = AdsAccessFlags.Factory.loadFrom(this, xDef.getAccessRules());
        if (xDef.getAccessRules() != null && xDef.getAccessRules().getIsFinal()) {
            this.setFinal(true);
        }
        usageEnvironment = xDef.getEnvironment();
        this.isConst = xDef.getIsConst();
        if (xDef instanceof PropertyDefinition) {
            PropertyDefinition xProp = (PropertyDefinition) xDef;

            this.isOverride = xProp.getIsOverride();

            if (xProp.isSetGetterSource()) {//old style definition
                this.getter = new Getter(xProp.getGetterSource(), xProp.getGetterProfileInfo());
            } else {
                if (xProp.isSetGetterSources()) {
                    this.getter = new Getter(xProp.getGetterSources(), xProp.getGetterProfileInfo());
                }
            }
            if (xProp.isSetSetterSource()) {
                this.setter = new Setter(xProp.getSetterSource(), xProp.getSetterProfileInfo());
            } else {
                if (xProp.isSetSetterSources()) {
                    this.setter = new Setter(xProp.getSetterSources(), xProp.getSetterProfileInfo());
                }
            }

            if (xProp.isSetReadAccess()) {
                this.readAccess = xProp.getReadAccess();
            }

            if (xProp.isSetWriteAccess()) {
                this.writeAccess = xProp.getWriteAccess();
            }

            if (xProp.isSetSuppressedWarnings()) {
                List<Integer> list = xProp.getSuppressedWarnings();
                if (!list.isEmpty()) {
                    this.warningsSupport = new Problems(this, list);
                }
            }
            if (xProp.isSetIsDescriptionInherited()) {
                this.isDescriptionInherited = xProp.getIsDescriptionInherited();
            } else {
                this.isDescriptionInherited = isDescriptionInheritable()
                        ? xDef.isSetDescriptionId() ? xDef.getDescriptionId() == null : true
                        : false;
                isSetIsDescriptionInherited = false;
            }
            this.valueInheritanceRules = ValueInheritanceRules.Factory.loadFrom(this, xProp.getValInheritance(), xProp.getInitializationPolicy());
        } else {
            this.valueInheritanceRules = ValueInheritanceRules.Factory.newInstance(this);
        }
        this.profile = new Profile(this);
    }

    protected AdsPropertyDef(Id id, String name) {
        super(id, name);
        this.value = PropertyValue.Factory.newInstance(this);
        this.accessFlags = AdsAccessFlags.Factory.newInstance(this);
        this.isConst = false;
        this.isOverride = false;
        this.valueInheritanceRules = ValueInheritanceRules.Factory.newInstance(this);
        this.getter = null;
        this.setter = null;
        this.profile = new Profile(this);
        this.isDescriptionInherited = isDescriptionInheritable();
    }

    protected abstract AdsPropertyDef createOvr(boolean forOverride);

    public abstract EPropNature getNature();

    @Override
    public AdsAccessFlags getAccessFlags() {
        return accessFlags;
    }

    public boolean isConst() {
        return isConst;
    }

    public void setConst(boolean isConst) {
        if (this.isConst != isConst) {
            this.isConst = isConst;

            /*
             * because accessibility modifiers on accessors may only be used if
             * the property has both a get and a set accessor
             */
            if (isConst) {
                if (isSetReadAccess()) {
                    setAccessMode(getReadAccess());
                }

                setWriteAccess(null);
                setReadAccess(null);
            }

            setEditState(EEditState.MODIFIED);
        }
    }

    public boolean isOverride() {
        return this.isOverride;
    }

    public boolean isTransferableAsMeta(ERuntimeEnvironmentType env) {
        return isTransferable(env, true);
    }

    @Override
    public boolean isTransferable(ERuntimeEnvironmentType env) {
        return isTransferable(env, false);
    }

    private boolean isTransferable(ERuntimeEnvironmentType env, boolean asMeta) {
        if (!getAccessFlags().isStatic()) {
            AdsTypeDeclaration decl = getValue().getType();
            if (decl == null || decl.getTypeId() == null) {
                return false;
            }
            PropertyPresentation propPresentation = null;
            switch (decl.getTypeId()) {
                case USER_CLASS:
                case JAVA_CLASS:
                case JAVA_TYPE:
                    return false;
                case PARENT_REF:
                case OBJECT:
                    AdsType type = decl.resolve(this).get();
                    if (type instanceof EntityObjectType) {
                        AdsEntityObjectClassDef clazz = ((EntityObjectType) type).getSource();
                        if (clazz == null) {
                            return false;
                        }
                        ERuntimeEnvironmentType clazzEnv = clazz.getClientEnvironment();
                        if (clazzEnv != ERuntimeEnvironmentType.COMMON_CLIENT && clazzEnv != env) {
                            //On reference type environment mismatch check edit possibility
                            if (asMeta) {
                                boolean match = false;
                                final ServerPresentationSupport support = ((IAdsPresentableProperty) this).getPresentationSupport();
                                if (support != null) {
                                    propPresentation = support.getPresentation();
                                    if (propPresentation != null) {
                                        if (propPresentation.getEditOptions().getEditPossibility() == EEditPossibility.NEVER) {
                                            match = true;
                                        } else {
                                            ERuntimeEnvironmentType editEnv = propPresentation.getEditOptions().getEditEnvironment();
                                            if (editEnv != ERuntimeEnvironmentType.COMMON_CLIENT && editEnv != env) {
                                                match = true;
                                            }
                                        }
                                    }
                                }
                                if (!match) {
                                    return false;
                                } else {
                                    break;
                                }
                            } else {
                                return false;
                            }
                        }
                    } else {
                        return false;
                    }
            }
            if (this instanceof IAdsPresentableProperty) {
                if (propPresentation == null) {
                    ServerPresentationSupport support = ((IAdsPresentableProperty) this).getPresentationSupport();
                    if (support != null) {
                        propPresentation = support.getPresentation();
                    }
                }

                boolean simpleMatch = propPresentation != null && propPresentation.isPresentable();
                if (!simpleMatch) {
                    return false;
                } else {
                    ERuntimeEnvironmentType ce = getClientEnvironment();
                    if (ce == env || ce == ERuntimeEnvironmentType.COMMON_CLIENT) {
                        return true;
                    } else {
                        return false;
                    }
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public void setOverride(boolean ovr) {
        this.isOverride = ovr;
        setEditState(EEditState.MODIFIED);
    }

    @Override
    public void appendToUsage(UsageDescription xDef) {
        super.appendToUsage(xDef);
        appendHeaderTo(xDef.addNewProperty(), ESaveMode.USAGE);
    }

    protected void appendHeaderTo(AbstractPropertyDefinition xDef, ESaveMode saveMode) {
        super.appendTo(xDef, saveMode);
        xDef.setNature(getNature());
        xDef.setIsConst(isConst);
        xDef.setEnvironment(usageEnvironment);
        AccessRules rules = xDef.getAccessRules();
        if (rules == null) {
            rules = xDef.addNewAccessRules();
        }
        accessFlags.appendTo(rules);
        value.appendTo(xDef);
    }

    public void appendTo(PropertyDefinition xDef, ESaveMode saveMode) {
        appendHeaderTo(xDef, saveMode);

        valueInheritanceRules.appendTo(xDef);
        xDef.setIsOverride(isOverride());
        if (isSetIsDescriptionInherited()) {
            xDef.setIsDescriptionInherited(isDescriptionInherited);
        }

        //----------------------------GETTER-------------------------------
        if (getter != null && saveMode == ESaveMode.NORMAL) {
            final PropAccessorSources getterSources = xDef.addNewGetterSources();
            getter.sources.appendTo(getterSources, saveMode);
            if (getter.isProfileable() && getter.getProfileSupport().isProfiled()) {
                getter.getProfileSupport().appendTo(xDef.addNewGetterProfileInfo());
            }
        }

        if (isSetReadAccess()) {
            xDef.setReadAccess(getReadAccess());
        }

        //-----------------------------SETTER------------------------------
        if (setter != null && saveMode == ESaveMode.NORMAL) {
            final PropAccessorSources setterSources = xDef.addNewSetterSources();
            setter.sources.appendTo(setterSources, saveMode);
            if (setter.isProfileable() && setter.getProfileSupport().isProfiled()) {
                setter.getProfileSupport().appendTo(xDef.addNewSetterProfileInfo());
            }
        }

        if (isSetWriteAccess()) {
            xDef.setWriteAccess(getWriteAccess());
        }
        //----------------------------------------------------------------

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
    }

    /**
     * Returns property value - component of property containing type dependent
     * information
     */
    public PropertyValue getValue() {
        return value;
    }

    @Override
    public boolean canChangeAccessMode() {
        return true;
    }
    private final Hierarchy<AdsPropertyDef> hierarchy = new MemberHierarchy<AdsPropertyDef>(this, true) {
        @Override
        protected AdsPropertyDef findMember(AdsClassDef clazz, Id id, EScope scope) {
            return clazz.getProperties().findById(id, scope).get();
        }
    };

    @Override
    @SuppressWarnings("unchecked")
    public Hierarchy<AdsPropertyDef> getHierarchy() {
        return hierarchy;
    }

    @Override
    public void visitChildren(IVisitor visitor, VisitorProvider provider) {
        super.visitChildren(visitor, provider);
        if (this.getter != null) {
            this.getter.visit(visitor, provider);
        }
        if (this.setter != null) {
            this.setter.visit(visitor, provider);
        }
        this.accessFlags.visit(visitor, provider);
        this.valueInheritanceRules.visit(visitor, provider);
        if (this instanceof IAdsPresentableProperty) {
            ServerPresentationSupport presentation = ((IAdsPresentableProperty) this).getPresentationSupport();
            if (presentation != null) {
                presentation.getPresentation().visit(visitor, provider);
            }
        }
        if (value != null) {
            value.visit(visitor, provider);
        }
    }

    @Override
    public ERuntimeEnvironmentType getDocEnvironment() {
        return getOwnerDef().getDocEnvironment();
    }

    @Override
    public EDocGroup getDocGroup() {
        return EDocGroup.PROPERTY;
    }

    public boolean isGetterDefined(EScope scope) {
        if (scope == EScope.LOCAL) {
            return this.getter != null;
        }

        MemberHierarchyIterator<AdsPropertyDef> iter = getHierarchyIterator(scope, HierarchyIterator.Mode.FIND_ALL);

        while (iter.hasNext()) {
            if (iter.next().first().getter != null) {
                return true;
            }
        }
        return false;
    }

    public void setGetterDefined(boolean define) {
        if (define) {
            if (this.getter != null) {
                return;
            } else {
                this.getter = new Getter();
                setEditState(EEditState.MODIFIED);
            }
        } else {
            if (this.getter == null) {
                return;
            } else {
                if (this.getter.delete()) {
                    this.getter = null;
                    setEditState(EEditState.MODIFIED);
                }
            }
        }
        getterAndSetterStateChanged(!define, define, false, false);
    }

    public SearchResult<Getter> findGetter(EScope scope) {
        return findAccessor(scope, new AccessorProvider<Getter>() {
            @Override
            public Getter getAccessor(AdsPropertyDef prop) {
                return prop.getter;
            }
        });
    }

    private interface AccessorProvider<T extends Accessor> {

        public T getAccessor(AdsPropertyDef prop);
    }

    private <T extends Accessor> SearchResult<T> findAccessor(EScope scope, AccessorProvider<T> provider) {
        if (scope == EScope.LOCAL) {
            return SearchResult.single(provider.getAccessor(this));
        }

        MemberHierarchyIterator<AdsPropertyDef> iter = getHierarchyIterator(scope, HierarchyIterator.Mode.FIND_ALL);

        List<T> result = new LinkedList<>();
        Set<AdsPropertyDef> lookup = new HashSet<>();
        while (iter.hasNext()) {
            HierarchyIterator.Chain<AdsPropertyDef> chain = iter.next();
            boolean chainCompleted = true;
            for (AdsPropertyDef prop : chain) {
                if (lookup.contains(prop)) {
                    return SearchResult.empty();
                }
                if (provider.getAccessor(prop) != null) {
                    result.add(provider.getAccessor(prop));
                } else {
                    chainCompleted = false;
                }
                lookup.add(prop);
            }
            if (chainCompleted) {
                break;
            }
        }
        if (result.isEmpty()) {
            return SearchResult.empty();
        } else {
            return SearchResult.list(result);
        }
    }

    MemberHierarchyIterator<AdsPropertyDef> getHierarchyIterator(EScope scope, HierarchyIterator.Mode mode) {
        return new MemberHierarchyIterator<AdsPropertyDef>(this, scope, mode) {
            @Override
            public AdsPropertyDef findInClass(AdsClassDef clazz) {
                return clazz.getProperties().findById(getId(), EScope.LOCAL).get();
            }
        };
    }

    public boolean isSetterDefined(EScope scope) {
        if (scope == EScope.LOCAL) {
            return this.setter != null;
        }

        MemberHierarchyIterator<AdsPropertyDef> iter = getHierarchyIterator(scope, HierarchyIterator.Mode.FIND_ALL);

        while (iter.hasNext()) {
            if (iter.next().first().setter != null) {
                return true;
            }
        }
        return false;
    }

    public void setSetterDefined(boolean define) {
        if (define) {
            if (this.setter != null) {
                return;
            } else {
                this.setter = new Setter();
                setEditState(EEditState.MODIFIED);
            }
        } else {
            if (this.setter == null) {
                return;
            } else {
                if (this.setter.delete()) {
                    this.setter = null;
                    setEditState(EEditState.MODIFIED);
                }
            }
        }
        getterAndSetterStateChanged(false, false, !define, define);
    }

    public SearchResult<Setter> findSetter(EScope scope) {
        return findAccessor(scope, new AccessorProvider<Setter>() {
            @Override
            public Setter getAccessor(AdsPropertyDef prop) {
                return prop.setter;
            }
        });
    }

    public ValueInheritanceRules getValueInheritanceRules() {
        return valueInheritanceRules;
    }

    @Override
    public RadixIcon getIcon() {
        return AdsDefinitionIcon.Property.calcIcon(this);
    }

    protected class PropertyJavaSourceSupport extends JavaSourceSupport {

        protected PropertyJavaSourceSupport() {
            super(AdsPropertyDef.this);
        }

        @Override
        public CodeWriter getCodeWriter(UsagePurpose purpose) {
            return AdsPropertyWriter.Factory.newInstance(this, AdsPropertyDef.this, purpose);
        }
    }

    @Override
    public JavaSourceSupport getJavaSourceSupport() {
        return new PropertyJavaSourceSupport();
    }

    @Override
    public void collectDependences(List<Definition> list) {
        super.collectDependences(list);
        //    this.getValue().collectDependences(list);
    }

    @Override
    public Jml getSource(String name) {
        Jml result = null;
        if (getter != null) {
            result = getter.getSource(name);
        }
        if (result != null) {
            return result;
        } else if (setter != null) {
            result = setter.getSource(name);
        }
        return result;
    }

    @Override
    public ClipboardSupport<AdsPropertyDef> getClipboardSupport() {
        return new PropertyClipboardSupport(this);
    }

    @Override
    public boolean isSuitableContainer(AdsDefinitions collection) {
        if (collection instanceof Properties.LocalProperties) {
            AdsClassDef collectionOwner = ((Properties.LocalProperties) collection).getOwnerClass();
            if (collectionOwner == null) {
                return false;
            } else {
                switch (getNature()) {
                    case DYNAMIC:
                        return true;
                    case EVENT_CODE:
                        return collectionOwner.getClassDefType() != EClassType.INTERFACE;
                    case DETAIL_PROP:
                    case EXPRESSION:
                    case PARENT_PROP:
                    case INNATE:
                    case USER:
                        return collectionOwner.getClassDefType() == EClassType.ENTITY || collectionOwner.getClassDefType() == EClassType.APPLICATION;
                    case FIELD:
                    case FIELD_REF:
                    case SQL_CLASS_PARAMETER:
                        return collectionOwner instanceof AdsSqlClassDef;
//                    case FORM_PROPERTY:
//                        return collectionOwner.getClassDefType() == EClassType.FORM_HANDLER;
                    case PROPERTY_PRESENTATION:
                        return collectionOwner.getClassDefType() == EClassType.ENTITY_MODEL || collectionOwner.getClassDefType() == EClassType.FORM_MODEL || collectionOwner.getClassDefType() == EClassType.REPORT_MODEL || collectionOwner.getClassDefType() == EClassType.FILTER_MODEL;
                    default:
                        return false;
                }
            }
        } else {
            return (collection instanceof PropertyGroups);
        }

    }

    @Override
    public EDefType getDefinitionType() {
        return EDefType.CLASS_PROPERTY;
    }

    public class GetterAndSetterChangeEvent extends RadixEvent {

        public final boolean getterRemoved;
        public final boolean getterCreated;
        public final boolean setterRemoved;
        public final boolean setterCreated;

        private GetterAndSetterChangeEvent(boolean getterRemoved, boolean getterCreated, boolean setterRemoved, boolean setterCreated) {
            this.getterRemoved = getterRemoved;
            this.getterCreated = getterCreated;
            this.setterRemoved = setterRemoved;
            this.setterCreated = setterCreated;
        }
    }

    private void getterAndSetterStateChanged(boolean getterRemoved, boolean getterCreated, boolean setterRemoved, boolean setterCreated) {
        synchronized (this) {
            if (getterAndSetterChangeSupport != null) {
                getterAndSetterChangeSupport.fireEvent(new GetterAndSetterChangeEvent(getterRemoved, getterCreated, setterRemoved, setterCreated));
            }
        }
    }

    public interface GetterAndSeterEventListener extends IRadixEventListener<GetterAndSetterChangeEvent> {
    }
    private RadixEventSource<GetterAndSeterEventListener, GetterAndSetterChangeEvent> getterAndSetterChangeSupport = null;

    public RadixEventSource<GetterAndSeterEventListener, GetterAndSetterChangeEvent> getGetterAndSetterChangeSupport() {
        synchronized (this) {
            if (getterAndSetterChangeSupport == null) {
                getterAndSetterChangeSupport = new RadixEventSource<GetterAndSeterEventListener, AdsPropertyDef.GetterAndSetterChangeEvent>();
            }
            return getterAndSetterChangeSupport;
        }
    }

    public abstract class Accessor extends RadixObject implements AdsProfileSupport.IProfileable, IJmlSource {

        protected final AdsSources sources;
        protected final AdsProfileSupport profileSupport = new AdsProfileSupport(this);

        protected Accessor(final String srcName) {
            this.sources = new AdsSources(this) {
                @Override
                protected String defaultName() {
                    return srcName;
                }
            };
            setContainer(AdsPropertyDef.this);
        }

        protected Accessor(PropAccessorSources xDef, ProfileInfo xProf, final String srcName) {
            this.sources = new AdsSources(this) {
                @Override
                protected String defaultName() {
                    return srcName;
                }
            };
            this.sources.loadFrom(xDef);
            setContainer(AdsPropertyDef.this);
            if (xProf != null && xProf.isSetTimingSectionId()) {
                profileSupport.loadFrom(xProf);
            }
        }

        protected Accessor(JmlType xDef, ProfileInfo xProf, final String srcName) {
            this.sources = new AdsSources(this) {
                @Override
                protected String defaultName() {
                    return srcName;
                }
            };
            this.sources.loadFrom(xDef);
            setContainer(AdsPropertyDef.this);
            if (xProf != null && xProf.isSetTimingSectionId()) {
                profileSupport.loadFrom(xProf);
            }
        }

        public abstract EAccess getAccessMode();

        public abstract void setAccessMode(EAccess access);

        public abstract boolean isSpecifiedAccessMode();

//        public boolean isDefined() {
//            return source != null;
//        }
        @Override
        public Jml getSource(String name) {
            return sources.getSource(name);
        }

        public Jml getSource(ERuntimeEnvironmentType env) {
            return sources.getSource(env);
        }

        @Override
        public RadixIcon getIcon() {
            return AdsPropertyDef.this.getIcon();
        }

        public AdsSources getSources() {
            return sources;
        }

        @Override
        public void visitChildren(IVisitor visitor, VisitorProvider provider) {
            super.visitChildren(visitor, provider);
            sources.visit(visitor, provider);
        }

        @Override
        public AdsProfileSupport getProfileSupport() {
            if (isProfileable()) {
                return profileSupport;
            } else {
                return null;
            }
        }

        @Override
        public boolean isProfileable() {
            return getUsageEnvironment() == ERuntimeEnvironmentType.SERVER;
        }

        @Override
        public AdsDefinition getAdsDefinition() {
            return AdsPropertyDef.this;
        }

        private class AccRemovedEvent extends RemovedEvent {

            public AccRemovedEvent() {
                super(Accessor.this);
            }
        }

        @Override
        public boolean delete() {
            getRemoveSupport().fireEvent(new AccRemovedEvent());
            setContainer(null);
            return true;
        }

        @Override
        public ENamingPolicy getNamingPolicy() {
            return ENamingPolicy.CALC;
        }

        public ILocationDescriptor getLocationDescriptor(ERuntimeEnvironmentType env) {
            return this.sources.getSource(env).getLocationDescriptor();
        }
    }

    public class Getter extends Accessor {

        protected Getter() {
            super("get");
        }

        protected Getter(JmlType xDef, ProfileInfo xProf) {
            super(xDef, xProf, "get");
        }

        protected Getter(PropAccessorSources xDef, ProfileInfo xProf) {
            super(xDef, xProf, "get");
        }

        @Override
        public String getName() {
            StringBuilder builder = new StringBuilder();
            builder.append(getValue().getType().getQualifiedName(this));
            builder.append(" get()");
            return builder.toString();
        }

        @Override
        public boolean delete() {
            AdsPropertyDef.this.getter = null;
            return super.delete();
        }

        @Override
        public EAccess getAccessMode() {
            return AdsPropertyDef.this.getReadAccess();
        }

        @Override
        public void setAccessMode(EAccess access) {
            AdsPropertyDef.this.setReadAccess(access);
        }

        @Override
        public boolean isSpecifiedAccessMode() {
            return AdsPropertyDef.this.isSetReadAccess();
        }
    }

    public class Setter extends Accessor {

        protected Setter() {
            super("set");
        }

        protected Setter(JmlType xDef, ProfileInfo xProf) {
            super(xDef, xProf, "set");
        }

        protected Setter(PropAccessorSources xDef, ProfileInfo xProf) {
            super(xDef, xProf, "set");
        }

        @Override
        public String getName() {
            StringBuilder builder = new StringBuilder();
            builder.append("void set(");
            builder.append(getValue().getType().getQualifiedName(this));
            builder.append(" val)");
            return builder.toString();
        }

        @Override
        public boolean delete() {
            AdsPropertyDef.this.setter = null;
            return super.delete();
        }

        @Override
        public EAccess getAccessMode() {
            return AdsPropertyDef.this.getWriteAccess();
        }

        @Override
        public void setAccessMode(EAccess access) {
            AdsPropertyDef.this.setWriteAccess(access);
        }

        @Override
        public boolean isSpecifiedAccessMode() {
            return AdsPropertyDef.this.isSetWriteAccess();
        }
    }

    @Override
    public boolean delete() {
        AdsClassDef ownerClass = getOwnerClass();
        if (super.delete()) {
            if (ownerClass != null) {
                ownerClass.getPropertyGroup().memberDeleted(this);
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String getToolTip() {
        return getToolTip(EIsoLanguage.ENGLISH);
    }

    protected String getValueTypeForToolTip(final RadixObject context) {
        String tooltipType = StringEscapeUtils.escapeHtml(getValue().getType().getQualifiedName(this));
        if (context instanceof IEnvDependent) {
            final ERuntimeEnvironmentType environmentType = ((IEnvDependent) context).getUsageEnvironment();
            if (environmentType.isClientEnv()) {
                return "Property&lt;" + tooltipType + "&gt;";
            }
        }
        return tooltipType;
    }

    @Override
    public String getToolTip(final EIsoLanguage language) {
        return getToolTip(language, null);
    }

    @Override
    public String getToolTip(final EIsoLanguage language, final RadixObject context) {
        final StringBuilder sb = new StringBuilder();
        sb.append("<html>");

        final String typeTitle = getTypeTitle();
        final String objectName = getValue().getType().getHtmlName(this, true) + " " + getName();
        sb.append("<b>").
                append(typeTitle).
                append(" '").
                append(objectName).
                append("'</b>");

        AdsType type = getValue().getType().resolve(this).get();
        boolean typeAdded = false;
        if (type instanceof AdsDefinitionType) {
            Definition def = ((AdsDefinitionType) type).getSource();
            if (def != null) {
                Utils.appendReferenceToolTipHtml(def, "Type", sb);
                typeAdded = true;
            }
        }
        if (!typeAdded) {
            sb.append("<br>Type:<br>&nbsp;");
            sb.append(getValueTypeForToolTip(context));
        }
        if (type instanceof AdsEnumType) {
            sb.append(" (").append((getValue().getType().getTypeId().getName())).append(")");
        }
        boolean isPresentable = false;
        if (AdsPropertyDef.this instanceof AdsServerSidePropertyDef) {
            ServerPresentationSupport support = ((AdsServerSidePropertyDef) AdsPropertyDef.this).getPresentationSupport();
            if (support != null && support.getPresentation() != null && support.getPresentation().isPresentable()) {
                isPresentable = true;
            }
        }
        final StringBuilder attributesBuilder = new StringBuilder();
        if (accessFlags.isAbstract()) {
            attributesBuilder.append("&nbsp;Abstract ");
        } else {
            if (accessFlags.isFinal()) {
                attributesBuilder.append("&nbsp;Final ");
            }
            if (accessFlags.isStatic()) {
                if (attributesBuilder.length() > 0) {
                    attributesBuilder.append(",");
                }
                attributesBuilder.append("&nbsp;Static");
            }
        }
        if (isPresentable) {
            if (attributesBuilder.length() > 0) {
                attributesBuilder.append(",");
            }
            attributesBuilder.append("&nbsp;Presentable");
        }

        if (attributesBuilder.length() > 0) {
            sb.append("<br>Attributes:<br>");
            sb.append(attributesBuilder.toString());
        }

        sb.append("<br>Access mode:<br>&nbsp;");
        if (accessFlags.isPrivate()) {
            sb.append("Private");
        } else if (accessFlags.isProtected()) {
            sb.append("Protected");
        } else if (accessFlags.isPublic()) {
            sb.append("Public");
        }

        appendLocationToolTip(sb);
        if (isInBranch()) { // otherwise, appendAdditionalToolTip can throw NullPointerException.
            appendAdditionalToolTip(sb);
        }

        AdsPropertyDef ovr = getHierarchy().findOverridden().get();
        if (ovr != null) {
            Utils.appendReferenceToolTipHtml(ovr, "Overrides", sb);
        }

        ovr = getHierarchy().findOverwritten().get();
        if (ovr != null) {
            Utils.appendReferenceToolTipHtml(ovr, "Overwrites", sb);
        }

        final String description = StringEscapeUtils.escapeHtml(getDescriptionForToolTip(language)).replace("\n", "<br>&nbsp;");

        if (description != null && !description.isEmpty()) {
            sb.append("<br><b>Description: </b><br>&nbsp;").append(description);
        }
        appendDetailsToolTip(sb);
        return sb.toString();
    }

    protected void appendDetailsToolTip(StringBuilder sb) {
    }

    @Override
    public IAdsTypedObject getTypedObject() {
        return getValue();
    }

    @Override
    public WarningSuppressionSupport getWarningSuppressionSupport(boolean createIfAbsent) {
        synchronized (this) {
            if (warningsSupport == null && createIfAbsent) {
                warningsSupport = new Problems(this, null);
            }
            return warningsSupport;
        }
    }

    @Override
    public ProblemFixSupport getProblemFixSupport() {
        synchronized (this) {
            if (warningsSupport == null) {
                return new Problems(this, null);
            } else {
                return warningsSupport;
            }
        }
    }

    @Override
    public void setFinal(boolean isFinal) {
        super.setFinal(isFinal);
        if (getAccessFlags().isAbstract()) {
            getAccessFlags().setAbstract(false);
        }
    }

    @Override
    public boolean canBeFinal() {
        return super.canBeFinal() && !getAccessFlags().isAbstract();
    }

    @Override
    public EAccess getMinimumAccess() {
        return EAccess.PRIVATE;
    }

    @Override
    public boolean isFinal() {
        if (getAccessMode() == EAccess.PRIVATE) {
            return true;
        } else if (getAccessFlags().isStatic()) {
            return false;
        } else {
            return super.isFinal();
        }
    }

    @Override
    public boolean canChangeFinality() {
        if (getAccessMode() == EAccess.PRIVATE || getAccessFlags().isStatic()) {
            return false;
        } else {
            return super.canChangeFinality();
        }
    }

    @Override
    public boolean isDeprecated() {
        return getAccessFlags().isDeprecated();
    }

    @Override
    public AdsTransparence getTransparence() {
        return null;
    }

    public boolean canDefineAccessors() {
        return true;
    }

    public boolean isJavaAccessibleFor(AdsClassDef clazz) {
        if (clazz.getUsageEnvironment() != getUsageEnvironment()) {
            if (this.getUsageEnvironment() != ERuntimeEnvironmentType.COMMON) {
                return false;
            }
        }
        AdsModule thisModule = getModule();
        if (thisModule == null) {
            return false;
        }
        AdsModule clazzModule = clazz.getModule();
        if (clazzModule == null) {
            return false;
        }
        boolean sameModule = false;
        if (thisModule != clazzModule) {
            if (thisModule.getId() == clazzModule.getId()) {
                AdsModule ovr = clazzModule.findOverwritten();
                while (ovr != null) {
                    if (ovr == thisModule) {
                        sameModule = true;
                        break;
                    }
                    ovr = ovr.findOverwritten();
                }
            }
        } else {
            sameModule = true;
        }
        if (sameModule) {
            return getAccessMode() != EAccess.PRIVATE;
        } else {
            if (clazz.getInheritance().isSubclassOf(getOwnerClass())) {
                return getOwnerClass().getAccessMode() == EAccess.PUBLIC && !getAccessMode().isLess(EAccess.PROTECTED);
            } else {
                return getOwnerClass().getAccessMode() == EAccess.PUBLIC && !getAccessMode().isLess(EAccess.PUBLIC);
            }
        }
    }

    @Override
    public ERuntimeEnvironmentType getUsageEnvironment() {
        AdsClassDef clazz = getOwnerClass();
        if (clazz != null) {
            ERuntimeEnvironmentType env = clazz.getUsageEnvironment();
            if (env.isClientEnv()) {
                if (env == ERuntimeEnvironmentType.COMMON_CLIENT) {
                    if (usageEnvironment == null || !usageEnvironment.isClientEnv()) {
                        return ERuntimeEnvironmentType.COMMON_CLIENT;
                    } else {
                        return usageEnvironment;
                    }
                } else {
                    return env;
                }
            }
        }

        if (usageEnvironment == null) {
            return super.getUsageEnvironment();
        } else {
            return usageEnvironment;
        }

    }

    public void setUsageEnvironment(ERuntimeEnvironmentType env) {
        if (this.usageEnvironment != env) {
            if (env == getUsageEnvironment() && usageEnvironment != null) {
                this.usageEnvironment = null;
                if (getter != null) {
                    getter.sources.onOwnerEnvironmentChange();
                }
                if (setter != null) {
                    setter.sources.onOwnerEnvironmentChange();
                }
                setEditState(EEditState.MODIFIED);
            } else {
                this.usageEnvironment = env;
                if (getter != null) {
                    getter.sources.onOwnerEnvironmentChange();
                }
                if (setter != null) {
                    setter.sources.onOwnerEnvironmentChange();
                }
                setEditState(EEditState.MODIFIED);
            }
        }
    }

    public ERuntimeEnvironmentType getClientEnvironment() {
        AdsClassDef clazz = getOwnerClass();
        if (clazz != null) {
//            if (!clazz.getUsageEnvironment().isClientEnv()) {
//                return ERuntimeEnvironmentType.COMMON_CLIENT;
//            }
            if (clazz.getClientEnvironment() == ERuntimeEnvironmentType.COMMON_CLIENT) {
                if (clazz.isDual()) {
                    ERuntimeEnvironmentType env = getUsageEnvironment();
                    if (env.isClientEnv()) {
                        return env;
                    }
                    return ERuntimeEnvironmentType.COMMON_CLIENT;
                } else {
                    return ERuntimeEnvironmentType.COMMON_CLIENT;
                }
            } else {
                return clazz.getClientEnvironment();
            }
        } else {
            return ERuntimeEnvironmentType.COMMON_CLIENT;
        }
    }

    public boolean canChangeClientEnvironment() {
        AdsClassDef clazz = getOwnerClass();
        if (clazz != null) {
            if (!clazz.getUsageEnvironment().isClientEnv()) {
                return false;
            }
            if (clazz.getClientEnvironment() == ERuntimeEnvironmentType.COMMON_CLIENT) {
                if (clazz.isDual()) {
                    ERuntimeEnvironmentType env = getUsageEnvironment();
                    if (env.isClientEnv()) {
                        return true;
                    }
                    return false;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean canBeUsedInSorting() {
        return canBeUsedInSorting(null, null);
    }

    public boolean canBeUsedInSorting(AdsSortingDef sorting, IProblemHandler problemHandler) {
        if (getNature() == EPropNature.DYNAMIC || getNature() == EPropNature.EVENT_CODE) {
            if (problemHandler != null) {
                problemHandler.accept(RadixProblem.Factory.newError(sorting, "Dynamic property " + getQualifiedName() + " can not be used in sorting"));
            }
            return false;
        }
        AdsTypeDeclaration type = getValue().getType();
        if (type == null || type.getTypeId() == null) {
            if (problemHandler != null) {
                problemHandler.accept(RadixProblem.Factory.newError(sorting, "Property " + getQualifiedName() + " can not be used in sorting, because it's type is unknown"));
            }
            return false;
        }
        EValType valType = type.getTypeId();

        if (valType.isArrayType()) {
            if (problemHandler != null) {
                problemHandler.accept(RadixProblem.Factory.newError(sorting, "Property " + getQualifiedName() + " can not be used in sorting, because it's type is collection"));
            }
            return false;
        }
        switch (valType) {
            case BLOB:
            case CLOB:
            case PARENT_REF:
            case OBJECT:
                if (problemHandler != null) {
                    problemHandler.accept(RadixProblem.Factory.newError(sorting, "Property " + getQualifiedName() + " can not be used in sorting, because it's type is not simply comparable (" + valType.getName() + ")"));
                }
                return false;
        }
        if (getNature() == EPropNature.PARENT_PROP) {
            AdsParentPropertyDef prop = (AdsParentPropertyDef) this;
            List<AdsPropertyDef> path = new LinkedList<>();
            AdsPropertyDef original = prop.getParentInfo().findOriginalProperty(path);
            if (original == null) {
                if (problemHandler != null) {
                    problemHandler.accept(RadixProblem.Factory.newError(sorting, "Parent property " + getQualifiedName() + " can not be used in sorting, because it's target cannot be found"));
                }
                return false;
            } else {
                if (original.getNature() == EPropNature.DYNAMIC) {
                    if (problemHandler != null) {
                        problemHandler.accept(RadixProblem.Factory.newError(sorting, "Parent property " + getQualifiedName() + " can not be used in sorting, because it's target property is dynamic"));
                    }
                    return false;
                }
            }
            for (AdsPropertyDef ref : path) {
                if (ref.getNature() == EPropNature.DYNAMIC) {
                    if (problemHandler != null) {
                        problemHandler.accept(RadixProblem.Factory.newError(sorting, "Parent property " + getQualifiedName() + " can not be used in sorting, because it's reference path item " + ref.getQualifiedName() + " is dynamic reference"));
                    }
                    return false;
                }
            }
        }
        return true;

    }

    @Override
    protected void insertToolTipPrefix(StringBuilder sb) {
        super.insertToolTipPrefix(sb);
        String access = getDefaultAccess().getName().toUpperCase().charAt(0) + getDefaultAccess().getName().substring(1, getDefaultAccess().getName().length());
        sb.append("<b>").append(access).append(" ").append(getClientEnvironment().getName()).append("</b>&nbsp;");
    }

    @Override
    public String getTypesTitle() {
        return "Properties";
    }

    @Override
    public boolean needsDocumentation() {
        AdsPropertyDef ovr = getHierarchy().findOverridden().get();

        if (ovr == null) {
            ovr = getHierarchy().findOverwritten().get();
        }
        if (ovr == null) {
            return true;
        } else {
            return false;
        }
    }
}
