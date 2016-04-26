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

package org.radixware.kernel.common.defs.ads.enumeration;

import java.lang.reflect.Method;
import java.util.*;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.check.RadixProblem.WarningSuppressionSupport;
import org.radixware.kernel.common.defs.*;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.ads.AdsClipboardSupport;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.AdsDefinitionProblems;
import org.radixware.kernel.common.defs.ads.AdsDefinitions;
import org.radixware.kernel.common.defs.ads.AdsTitledDefinition;
import org.radixware.kernel.common.defs.ads.module.ModuleDefinitions;
import org.radixware.kernel.common.defs.ads.platform.IPlatformClassPublisher;
import org.radixware.kernel.common.defs.ads.platform.IPlatformClassPublisher.IPlatformClassPublishingSupport;
import org.radixware.kernel.common.defs.ads.platform.RadixPlatformEnum;
import org.radixware.kernel.common.defs.ads.radixdoc.EnumRadixdoc;
import org.radixware.kernel.common.defs.ads.src.IJavaSource;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport.CodeType;
import org.radixware.kernel.common.defs.ads.src.enumeration.AdsEnumWriter;
import org.radixware.kernel.common.defs.ads.type.AdsEnumType;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import org.radixware.kernel.common.defs.ads.type.IAdsTypeSource;
import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.enums.*;
import org.radixware.kernel.common.exceptions.DefinitionError;
import org.radixware.kernel.common.exceptions.NoConstItemWithSuchValueError;
import org.radixware.kernel.common.exceptions.RadixObjectError;
import org.radixware.kernel.common.radixdoc.DocumentOptions;
import org.radixware.kernel.common.radixdoc.IRadixdocPage;
import org.radixware.kernel.common.radixdoc.IRadixdocProvider;
import org.radixware.kernel.common.radixdoc.RadixdocSupport;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.adsdef.AdsDefinitionElementType;
import org.radixware.schemas.adsdef.EnumDefinition;
import org.radixware.schemas.adsdef.EnumItemDefinition;
import org.radixware.schemas.adsdef.UsageDescription;
import org.radixware.schemas.radixdoc.Page;


public class AdsEnumDef extends AdsTitledDefinition implements IAdsTypeSource,
        IEnumDef, IOverwritable, IJavaSource, IPlatformClassPublisher, IRadixdocProvider {

    @Override
    public boolean allowOverwrite() {
        return true;
    }

    @Override
    public AdsType getType(EValType typeId, String extStr) {
        switch (typeId) {
            case ARR_CHAR:
            case ARR_INT:
            case ARR_STR:
                return AdsEnumType.Factory.newArrayInstance(this);
            case CHAR:
            case INT:
            case STR:
                return AdsEnumType.Factory.newInstance(this);
            default:
                return null;
        }
    }

    @Override
    public JavaSourceSupport getJavaSourceSupport() {
        return new JavaSourceSupport(this) {
            @Override
            public CodeWriter getCodeWriter(UsagePurpose purpose) {
                return new AdsEnumWriter(this, AdsEnumDef.this, purpose);
            }

            @Override
            public EnumSet<ERuntimeEnvironmentType> getSupportedEnvironments() {
                return EnumSet.of(ERuntimeEnvironmentType.COMMON);
            }

            @Override
            public Set<CodeType> getSeparateFileTypes(ERuntimeEnvironmentType e) {
                return e == ERuntimeEnvironmentType.COMMON ? EnumSet.of(CodeType.EXCUTABLE, CodeType.META) : null;
            }
        };
    }

    @Override
    public IPlatformClassPublishingSupport getPlatformClassPublishingSupport() {
        return null;
    }

    @Override
    public void afterOverwrite() {
        this.getItems().getLocal().clear();
    }

    public static class Factory {

        public static AdsEnumDef loadFrom(EnumDefinition xDef) {
            if (xDef.isSetPublishing()) {
                return new AdsPlatformEnumDef(xDef);
            }
            return new AdsEnumDef(xDef);
        }

        public static AdsEnumDef newInstance() {
            return new AdsEnumDef("NewEnumeration");
        }

        public static AdsEnumDef newInstance(RadixPlatformEnum source) {
            if (source == null) {
                return newInstance();
            } else {
                return new AdsPlatformEnumDef(source);
            }
        }
    }

    private final class ItemsListHierarchyIterator extends HierarchyIterator<ExtendableDefinitions<AdsEnumItemDef>> {

        private List<ExtendableDefinitions<AdsEnumItemDef>> next;
        private EnumHierarchyIterator internal;

        protected ItemsListHierarchyIterator(ExtendableDefinitions<AdsEnumItemDef> init, EScope scope, HierarchyIterator.Mode mode) {
            super(mode);
            this.next = null;
            this.internal = new EnumHierarchyIterator(AdsEnumDef.this, scope, mode);
        }

        @Override
        public boolean hasNext() {
            if (next == null && internal.hasNext()) {
                List<ExtendableDefinitions<AdsEnumItemDef>> result = new LinkedList<>();
                while (internal.hasNext()) {
                    Chain<AdsEnumDef> nextClass = internal.next();
                    for (AdsEnumDef enumDef : nextClass) {
                        ExtendableDefinitions<AdsEnumItemDef> list = findInstance(enumDef);
                        if (list != null && !result.contains(list)) {
                            result.add(list);
                        }
                    }
                    if (!result.isEmpty()) {
                        break;
                    }
                }
                if (result.isEmpty()) {
                    next = null;
                } else {
                    next = new ArrayList<>(result);
                }
            }
            return next != null;
        }

        @Override
        public Chain<ExtendableDefinitions<AdsEnumItemDef>> next() {
            if (hasNext()) {
                Chain<ExtendableDefinitions<AdsEnumItemDef>> result = Chain.newInstance(next);
                next = null;
                return result;
            } else {
                return Chain.empty();
            }
        }

        public ExtendableDefinitions<AdsEnumItemDef> findInstance(AdsEnumDef def) {
            return def.getItems();
        }
    }

    public class Items extends ExtendableDefinitions<AdsEnumItemDef> implements IEnumDef.IItems<AdsEnumItemDef> {

        private Items(EnumDefinition xDef) {
            super(AdsEnumDef.this);
            if (xDef.getItems() != null) {
                for (EnumItemDefinition xItem : xDef.getItems().getItemList()) {
                    if (xItem.getIsVirtual()) {
                        if (AdsEnumDef.this instanceof AdsPlatformEnumDef) {
                            getLocal().add(new VirtualPlatformItem(xItem));
                        } else {
                            //skip item
                        }
                    } else {
                        if (xItem.getIsOverwrite()) {
                            if (AdsEnumDef.this instanceof AdsPlatformEnumDef) {
                                getLocal().add(new OverridePlatformItem(xItem));
                            } else {
                                getLocal().add(new OverrideAdsItem(xItem));
                            }
                        } else {
                            getLocal().add(new ItemImpl(xItem));
                        }
                    }
                }
            }
        }

        private Items() {
            super(AdsEnumDef.this);
        }

        @Override
        public IItem findItemById(Id itemId, EScope scope) {
            return (IItem) findById(itemId, scope).get();
        }

//        public AdsEnumItemDef findByName(String name, EScope scope) {
//            synchronized (this) {
//                for (AdsEnumItemDef item : this.get(scope)) {
//                    if (Utils.equals(item.getName(), name)) {
//                        return item;
//                    }
//                }
//                return null;
//            }
//        }
        private void appendTo(EnumDefinition xDef, boolean eraseExtInfo, ESaveMode saveMode) {
            if (!this.getLocal().isEmpty()) {
                EnumDefinition.Items items = xDef.addNewItems();
                for (AdsEnumItemDef item : this.getLocal()) {
                    item.appendToItemSet(items, eraseExtInfo, saveMode);
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
        }

        @Override
        protected HierarchyIterator<ExtendableDefinitions<AdsEnumItemDef>> newIterator(EScope scope, HierarchyIterator.Mode mode) {
            return new ItemsListHierarchyIterator(this, scope, mode);
        }

        @Override
        public List<AdsEnumItemDef> list(EScope scope) {
            return this.get(scope);
        }

        public AdsEnumItemDef findByValue(String value, EScope scope) {
            final HierarchyIterator<ExtendableDefinitions<AdsEnumItemDef>> iter = newIterator(scope, HierarchyIterator.Mode.FIND_FIRST);
            final ValAsStr match = ValAsStr.Factory.loadFrom(value);
            while ((iter.hasNext())) {
                ExtendableDefinitions<AdsEnumItemDef> set = iter.next().first();
                for (AdsEnumItemDef item : set.getLocal()) {
                    if (Objects.equals(match, item.getValue())) {
                        return item;
                    }
                }
            }
            return null;
        }

//        public AdsEnumItemDef findByName(String name) {
//            HierarchyIterator<ExtendableDefinitions<AdsEnumItemDef>> iter = newIterator(EScope.LOCAL_AND_OVERWRITE);
//            while (iter.hasNext()) {
//                Definitions<AdsEnumItemDef> locals = iter.next().getLocal();
//                for (AdsEnumItemDef item : locals) {
//                    if (item.getName().equals(name)) {
//                        return item;
//                    }
//                }
//            }
//            return null;
//        }
        @Override
        public AdsEnumItemDef overwrite(AdsEnumItemDef source) {
            AdsEnumItemDef realSource = findById(source.getId(), EScope.ALL).get();
            if (realSource == null) {
                throw new RadixObjectError("Enum item can not be overwritten", this);
            }
            AdsEnumItemDef result = null;
            if (realSource.getOwnerEnum() == AdsEnumDef.this) {//item defined write here
                if (realSource instanceof VirtualPlatformItem) {
                    result = new OverridePlatformItem((VirtualPlatformItem) realSource);
                } else {
                    throw new RadixObjectError("Enum item can not be overwritten", this);
                }
                if (result != null) {
                    this.getLocal().remove(realSource);
                }
            } else {
                if (realSource instanceof VirtualPlatformItem) {
                    throw new RadixObjectError("Virtual item can be overwritten only in publishing enum", this);
                } else {
                    result = new OverrideAdsItem(realSource);
                }
            }

            if (result != null) {
                getLocal().add(result);
            }
            return result;
        }

        @Override
        public String getName() {
            return "Items";
        }
    }
    protected EValType itemType;
    private final ViewOrder viewOrder;
    private final ValueRanges valueRanges;
    private boolean isIdEnum = false;
    private final Items items;
    private boolean isDeprecated = false;
    private EEnumDefinitionItemViewFormat itemsViewFormat;
    private Problems warningsSupport = null;

    protected AdsEnumDef(EnumDefinition xDef) {
        super(xDef);
        this.itemType = EValType.getForValue(xDef.getValType());
        this.items = new Items(xDef);
        this.viewOrder = new ViewOrder(this, xDef.getViewOrder());
        this.valueRanges = new ValueRanges(this, xDef.getValueRanges());
        if (xDef.isSetIsIdEnum()) {
            this.isIdEnum = xDef.getIsIdEnum();
        } else {
            this.isIdEnum = false;
        }
        if (xDef.isSetIsDeprecated()) {
            isDeprecated = xDef.getIsDeprecated();
        }

        if (xDef.isSetItemsViewFormat()) {
            itemsViewFormat = xDef.getItemsViewFormat();
        }
        
        if (xDef.isSetSuppressedWarnings()) {
            List<Integer> list = xDef.getSuppressedWarnings();
            if (!list.isEmpty()) {
                this.warningsSupport = instantiateWarningsSupport(list);
            }
        }
    }

    private AdsEnumDef(AdsEnumDef source) {
        this(source.getId(), source.getName());
        this.itemType = source.getItemType();
        this.isDeprecated = source.isDeprecated;
    }

    private AdsEnumDef(String name) {
        this(Id.Factory.newInstance(EDefinitionIdPrefix.ADS_ENUMERATION), name);
    }

    protected AdsEnumDef(Id id, String name) {
        super(id, name, null);
        this.itemType = EValType.INT;
        this.items = new Items();
        this.viewOrder = new ViewOrder(this);
        this.valueRanges = new ValueRanges(this);
    }

    public EEnumDefinitionItemViewFormat getItemsViewFormat() {
        return itemsViewFormat;
    }

    public void setItemsViewFormat(EEnumDefinitionItemViewFormat itemsViewFormat) {
        if (this.itemsViewFormat != itemsViewFormat) {
            this.itemsViewFormat = itemsViewFormat;
            setEditState(EEditState.MODIFIED);
        }
    }

    public ViewOrder getViewOrder() {
        return viewOrder;
    }

    public ValueRanges getValueRanges() {
        return valueRanges;
    }

    /**
     * Returns item set for enumeration
     */
    @Override
    public Items getItems() {
        return items;
    }

    /**
     * Changes enumeration item type
     *
     * @throws {@linkplain DefinitionError} if enumeration is already added to a
     * module
     */
    public void setItemType(EValType type) {
        if (this.getContainer() != null) {
            throw new DefinitionError("Type modifications allowed during initial setup process only.", this);
        }
        this.itemType = type;
    }

    /**
     * Returns enumeration item type
     */
    @Override
    public EValType getItemType() {
        return this.itemType;
    }

    @Override
    public void visitChildren(IVisitor visitor, VisitorProvider provider) {
        super.visitChildren(visitor, provider);
        items.visit(visitor, provider);
        valueRanges.visit(visitor, provider);
    }

    @Override
    public void appendTo(AdsDefinitionElementType xDefRoot, ESaveMode saveMode) {//has mean
        appendTo(xDefRoot.addNewAdsEnumDefinition(), saveMode);
    }

    protected void appendTo(EnumDefinition xDef, boolean eraseExtInfo, ESaveMode saveMode) {
        super.appendTo(xDef, saveMode);
        xDef.setValType(this.getItemType().getValue());
        this.items.appendTo(xDef, eraseExtInfo, saveMode);
        this.viewOrder.appendTo(xDef);
        this.valueRanges.appendTo(xDef);
        if (isDeprecated) {
            xDef.setIsDeprecated(isDeprecated);
        }
        if (isIdEnum) {
            xDef.setIsIdEnum(true);
        }

        if (itemsViewFormat != null) {
            xDef.setItemsViewFormat(itemsViewFormat);
        }
    }

    protected void appendTo(EnumDefinition xDef, ESaveMode saveMode) {
        appendTo(xDef, false, saveMode);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Hierarchy<AdsEnumDef> getHierarchy() {
        return new DefaultHierarchy<>();
    }

    /**
     * Tries to find basis enumeration
     */
    public AdsEnumDef findBasis() {
        return this.getHierarchy().findOverwritten().get();
    }

    /**
     * Tries to find published platform enumeration
     */
    public RadixPlatformEnum findPublishedPlatformEnum() {
        return null;
    }

    /**
     * Returns published platform enumeration class name.
     */
    public String getPublishedPlatformEnumName() {
        AdsEnumDef ovr = getHierarchy().findOverwritten().get();
        if (ovr == null) {
            return null;
        } else {
            return ovr.getPublishedPlatformEnumName();
        }
    }

    /**
     * Changes published platform enumeration class name.
     *
     * @returns true if change is successfull.
     */
    public boolean setPublishedPlatformEnumName(String enumClassName) {
        return false;
    }

    /**
     * Returns published DDS enumeration Id
     */
    public Id getPublishedDdsEnumId() {
        return null;
    }

    /**
     * Answers is enumeration extendable or not (meanfull only for enumerations
     * that publishes platform or dds enumerations)
     */
    public boolean isExtendable() {
        return true;
    }

    /**
     * Changes enumeration extensibility
     */
    public boolean setExtendable(boolean extendable) {
        if (!canChangeExtensibility()) {
            return false;
        }
        if (extendable) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Returns true if the enumeration extensibility may be changed
     */
    public boolean canChangeExtensibility() {
        return false;
    }

    @Override
    public RadixIcon getIcon() {
        switch (this.itemType) {
            case INT:
                return RadixObjectIcon.ENUM_INT;
            case STR:
                return RadixObjectIcon.ENUM_STR;
            case CHAR:
                return RadixObjectIcon.ENUM_CHAR;
            default:
                return RadixObjectIcon.UNKNOWN;
        }
    }

    @Override
    public boolean isSaveable() {
        return true;
    }
    public static final String ADS_ENUM_TYPE_TITLE = "Enumeration";

    @Override
    public String getTypeTitle() {
        if (this.itemType == EValType.STR && isIdEnum()) {
            return "ID Container";
        }
        return ADS_ENUM_TYPE_TITLE;
    }
    public static final String ADS_ENUM_TYPES_TITLE = "Enumerations";

    @Override
    public String getTypesTitle() {
        return ADS_ENUM_TYPES_TITLE;
    }

    /**
     * Performs item synchronization. Meanfull only for enumerations based on
     * platform or DDS enumerations
     */
    public boolean syncPublishedItems() {
        return false;
    }

    /**
     * @return true if enumeration based on native java enumerated type, false
     * otherwise.
     */
    public boolean isPlatformEnumPublisher() {
        return false;
    }

    @Override
    public Set<ERuntimeEnvironmentType> getTypeUsageEnvironments() {
        return EnumSet.of(ERuntimeEnvironmentType.COMMON);
    }

    @Override
    public ClipboardSupport<? extends AdsEnumDef> getClipboardSupport() {
        return new AdsClipboardSupport<AdsEnumDef>(this) {
            @Override
            protected XmlObject copyToXml() {
                EnumDefinition xDef = EnumDefinition.Factory.newInstance();
                appendTo(xDef, true, ESaveMode.NORMAL);
                return xDef;
            }

            @Override
            protected AdsEnumDef loadFrom(XmlObject xmlObject) {
                if (xmlObject instanceof EnumDefinition) {
                    return Factory.loadFrom((EnumDefinition) xmlObject);
                } else {
                    return super.loadFrom(xmlObject);
                }
            }

            @Override
            protected Method getDecoderMethod() {
                try {
                    return AdsEnumDef.Factory.class.getDeclaredMethod("loadFrom", EnumDefinition.class);
                } catch (NoSuchMethodException | SecurityException ex) {
                    return null;
                }
            }
        };
    }

    @Override
    public boolean isSuitableContainer(AdsDefinitions collection) {
        return collection instanceof ModuleDefinitions;
    }

    @Override
    public EDefType getDefinitionType() {
        return EDefType.ENUMERATION;
    }

    @Override
    public SearchResult<? extends AdsDefinition> findComponentDefinition(Id id) {
        if (id == null) {
            return null;
        }
        if (id.getPrefix() == EDefinitionIdPrefix.CONST_ITEM) {
            return getItems().findById(id, EScope.ALL);
        }
        return super.findComponentDefinition(id);
    }

    @Override
    public ESaveMode getSaveMode() {
        return super.getSaveMode();
    }

    @Override
    public EAccess getMinimumAccess() {
        return EAccess.DEFAULT;
    }

    @Override
    public void appendToUsage(UsageDescription xDef) {
        super.appendToUsage(xDef);
        xDef.setValueType(getItemType());
    }

    public boolean isIdEnum() {
        return isIdEnum;
    }

    public boolean setIdEnum(boolean isIdEnum) {
        if (isIdEnum != this.isIdEnum) {
            if (isIdEnum) {
                if (!canBeIdEnum()) {
                    return false;
                }
            }
            this.isIdEnum = isIdEnum;
            setEditState(EEditState.MODIFIED);
            return true;
        } else {
            return false;
        }
    }

    public boolean canBeIdEnum() {
        return canBeIdEnum(new LinkedList<String>());
    }

    public boolean canBeIdEnum(List<String> rejectDetails) {
        if (getItemType() != EValType.STR) {
            rejectDetails.add("Only string enumeration can be ID container");
            return false;
        }
        AdsEnumDef ovr = getHierarchy().findOverwritten().get();
        if (ovr != null && !ovr.isIdEnum()) {
            rejectDetails.add("Overwritten enumeration  " + ovr.getQualifiedName() + " is not ID container");
            return false;
        }
        for (AdsEnumItemDef item : items.get(EScope.LOCAL)) {
            String string = (String) item.getValue().toObject(EValType.STR);

            if (string.length() >= 3) {
                final String prefixAsStr = string.substring(0, 3);
                try {
                    EDefinitionIdPrefix.getForValue(prefixAsStr);
                } catch (NoConstItemWithSuchValueError error) {
                    rejectDetails.add("Unknown id prefix: '" + prefixAsStr + "' at element" + item.getQualifiedName());
                    return false;
                }
            } else {
                rejectDetails.add("Value of element " + item.getQualifiedName() + " is too short. Should be at least 4 symbols");
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean isDeprecated() {
        return isDeprecated || super.isDeprecated();
    }

    public void setDeprecated(boolean isDeprecated) {
        if (this.isDeprecated != isDeprecated) {
            this.isDeprecated = isDeprecated;
            setEditState(EEditState.MODIFIED);
            fireNameChange();
        }
    }

    @Override
    public RadixdocSupport<? extends RadixObject> getRadixdocSupport() {
        return new RadixdocSupport<AdsEnumDef>(this) {
            @Override
            public IRadixdocPage document(Page page, DocumentOptions options) {
                return new EnumRadixdoc(AdsEnumDef.this, page, options);
            }
        };
    }

    @Override
    public boolean isRadixdocProvider() {
        return true;
    }

    @Override
    protected void appendAdditionalToolTip(StringBuilder sb) {
        sb.append("<br>Base Type:<br>&nbsp;");
        sb.append(getItemType());
    }
    
    public static class Problems extends AdsDefinitionProblems {

        public static final int PLATFORM_ENUM_ITEM_IS_NOT_PUBLISHED = 1000000;

        protected Problems(AdsEnumDef owner, List<Integer> warnings) {
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
            return code == PLATFORM_ENUM_ITEM_IS_NOT_PUBLISHED;
        }
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
}
