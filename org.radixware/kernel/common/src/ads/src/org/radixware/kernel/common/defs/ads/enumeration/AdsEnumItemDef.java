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

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.defs.*;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.IEnumDef.IItem;
import org.radixware.kernel.common.defs.ads.AdsClipboardSupport;
import org.radixware.kernel.common.defs.ads.AdsDefinition.Hierarchy;
import org.radixware.kernel.common.defs.ads.AdsTitledDefinition;
import org.radixware.kernel.common.defs.ads.module.AdsSearcher;
import org.radixware.kernel.common.defs.ads.src.IJavaSource;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.src.enumeration.AdsEnumItemWriter;
import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.enums.EAccess;
import org.radixware.kernel.common.enums.EDefType;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.environment.IRadixEnvironment;
import org.radixware.kernel.common.exceptions.DefinitionError;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.adsdef.EnumDefinition;
import org.radixware.schemas.adsdef.EnumItemDefinition;
import org.radixware.schemas.adsdef.UsageDescription;


public abstract class AdsEnumItemDef extends AdsTitledDefinition implements IEnumDef.IItem, IJavaSource {

    public static final class Factory {

        public static AdsEnumItemDef newInstance() {
            return new ItemImpl("newItem");
        }

        static AdsEnumItemDef newInstance(OverridePlatformItem source) {
            return new ItemImpl(source);
        }

        public static AdsEnumItemDef newTemporaryInstance(RadixObject parent) {
            AdsEnumItemDef result = newInstance();
            result.setContainer(parent);
            return result;
        }

        public static AdsEnumItemDef loadFrom(EnumItemDefinition xDef) {
            return new ItemImpl(xDef);
        }
    }

    public enum EditPossibility {

        NAME,
        VALUE,
        TITLE,
        ICON,
        DOMAIN,
        DEPRECATION,
        OVERWRITE,
        REMOVE
    }
    private ValAsStr valAsStr;
    private Id iconId;
    private boolean isDeprecated;

    AdsEnumItemDef(EnumItemDefinition xDef) {
        super(xDef);
        this.iconId = Id.Factory.loadFrom(xDef.getIconId());
        this.valAsStr = ValAsStr.Factory.loadFrom(xDef.getValue());
        this.isDeprecated = xDef.getIsDeprecated();
    }

    AdsEnumItemDef(String name) {
        this(Id.Factory.newInstance(EDefinitionIdPrefix.CONST_ITEM), name, null);
    }

    protected AdsEnumItemDef(Id id, String name, ValAsStr value) {
        super(id, name, null);
        this.valAsStr = value;
    }

    @Override
    public ValAsStr getValue() {
        IItem source = findSourceItem();
        if (source != null) {
            return source.getValue();
        } else {
            return valAsStr;
        }
    }

    @Override
    public String getName() {
        IItem source = findSourceItem();
        if (source != null) {
            return source.getName();
        } else {
            return super.getName();
        }
    }

    public boolean setValue(ValAsStr value) {
        if (!getEditPossibility().contains(EditPossibility.VALUE)) {
            throw new DefinitionError("Value of item " + this.getQualifiedName() + " can not be modified", this);
        }
        return updateValue(value);
    }

    boolean updateValue(ValAsStr value) {
        if (value != null && !value.equals(valAsStr)) {
            valAsStr = value;
            this.setEditState(EEditState.MODIFIED);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String getTitle(final IRadixEnvironment env) {
        return getTitle(env.getClientLanguage());
    }

    @Override
    public String getTitle(final IRadixEnvironment env, final EIsoLanguage lang) {
        return getTitle(lang);
    }

    @Override
    public boolean setTitle(EIsoLanguage language, String value) {
        if (!getEditPossibility().contains(EditPossibility.TITLE)) {
            throw new DefinitionError("Title of item " + this.getQualifiedName() + " can not be modified", this);
        } else {
            final String title = getTitle(language);
            if (!Objects.equals(title, value)) {
                return super.setTitle(language, value);
            } else {
                return false;
            }
        }
    }

    public AdsEnumDef getOwnerEnum() {
        for (RadixObject owner = getContainer(); owner != null; owner = owner.getContainer()) {
            if (owner instanceof AdsEnumDef) {
                return (AdsEnumDef) owner;
            }
        }
        return null;
    }

    void appendToItemSet(EnumDefinition.Items xItemSet, boolean eraseExtInfo, ESaveMode saveMode) {
        appendTo(xItemSet.addNewItem(), eraseExtInfo, saveMode);
    }

    protected void appendTo(EnumItemDefinition xItem, boolean eraseExtInfo, ESaveMode saveMode) {
        super.appendTo(xItem, saveMode);
        if (saveMode != ESaveMode.USAGE) {
            if (iconId != null) {
                xItem.setIconId(iconId.toString());
            }
        }
        if (valAsStr != null) {
            xItem.setValue(valAsStr.toString());
        }
        xItem.setIsDeprecated(isDeprecated);

        if (!eraseExtInfo) {
//            if (isOverwrite()) {
//                xItem.setIsOverwrite(true);
//            }

            if (isVirtual()) {
                xItem.setIsVirtual(true);
            }

            String platformName = getPlatformItemName();
            if (platformName != null) {
                xItem.setPublishedName(platformName);
            }
        }
    }

    @Override
    public boolean isOverwrite() {
        return false;
    }

    @Override
    public boolean setOverwrite(boolean flag) {
        return false;
    }

    @Override
    public EDefType getDefinitionType() {
        return EDefType.ENUM_ITEM;
    }

    @Override
    public boolean isDeprecated() {
        return isDeprecated || super.isDeprecated() || (getOwnerEnum() != null && getOwnerEnum().isDeprecated());
    }

    public Id getIconId() {
        return iconId;
    }

    public void setIconId(Id id) {
        iconId = id;
        setEditState(EEditState.MODIFIED);
    }

    public void setDeprecated(boolean isDeprecated) {
        this.isDeprecated = isDeprecated;
        setEditState(EEditState.MODIFIED);
    }

    public boolean isVirtual() {
        return false;
    }

    public EnumSet<EditPossibility> getEditPossibility() {
        return EnumSet.of(EditPossibility.DEPRECATION,
                EditPossibility.DOMAIN,
                EditPossibility.ICON,
                EditPossibility.NAME,
                EditPossibility.REMOVE,
                EditPossibility.TITLE,
                EditPossibility.VALUE);
    }

    public abstract AdsEnumItemDef findSourceItem();

    @Override
    public JavaSourceSupport getJavaSourceSupport() {
        return new JavaSourceSupport(this) {
            @Override
            public CodeWriter getCodeWriter(UsagePurpose purpose) {
                return new AdsEnumItemWriter(this, AdsEnumItemDef.this, purpose);
            }
        };
    }

    private class ItemHierarchy extends DefaultHierarchy<AdsEnumItemDef> {

        @Override
        public SearchResult<AdsEnumItemDef> findOverridden() {
            return SearchResult.empty();
        }

        @Override
        public SearchResult<AdsEnumItemDef> findOverwritten() {
            SearchResult<AdsEnumDef> owner = AdsEnumItemDef.this.getOwnerEnum().getHierarchy().findOverwritten();
            List<AdsEnumItemDef> result = new LinkedList<>();
            for (AdsEnumDef e : owner.all()) {
                AdsEnumItemDef item = e.getItems().findById(getId(), EScope.LOCAL).get();
                if (item != null && !result.contains(item)) {
                    result.add(item);
                }
            }
            if (result.isEmpty()) {
                return SearchResult.empty();
            } else {
                return SearchResult.list(result);
            }
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public Hierarchy<AdsEnumItemDef> getHierarchy() {
        return new ItemHierarchy();
    }

    public String getPlatformItemName() {
        return null;
    }

    @Override
    public RadixIcon getIcon() {
        switch (getOwnerEnum().getItemType()) {
            case INT:
                return RadixObjectIcon.ENUM_ITEM_INT;
            case CHAR:
                return RadixObjectIcon.ENUM_ITEM_CHAR;
            case STR:
                return RadixObjectIcon.ENUM_ITEM_STR;
            default:
                return RadixObjectIcon.UNKNOWN;
        }
    }

    @Override
    public ClipboardSupport<? extends AdsEnumItemDef> getClipboardSupport() {
        return new AdsClipboardSupport<AdsEnumItemDef>(this) {
            @Override
            protected XmlObject copyToXml() {
                EnumItemDefinition xDef = EnumItemDefinition.Factory.newInstance();
                appendTo(xDef, true, ESaveMode.NORMAL);
                return xDef;
            }

            @Override
            protected AdsEnumItemDef loadFrom(XmlObject xmlObject) {
                if (xmlObject instanceof EnumItemDefinition) {
                    return new ItemImpl((EnumItemDefinition) xmlObject);
                } else {
                    return super.loadFrom(xmlObject);
                }
            }
        };
    }

    @Override
    public void collectDependences(List<Definition> list) {
        super.collectDependences(list);
        if (iconId != null) {
            AdsSearcher.Factory.newImageSearcher(this).findById(iconId).save(list);
        }
    }

    public int getViewOrder() {
        return getOwnerEnum().getViewOrder().getItemOrder(this);
    }

    public boolean isPlatformItemPublisher() {
        return false;
    }

    @Override
    public Collection<Id> getDomainIds() { // by BAO 21.07.09 RADIX-341: deploing enumitem2domain indices to DB
        return Collections.unmodifiableList(Arrays.asList(getDomains().getDomaindIds()));
    }

    @Override
    public String getTypeTitle() {
        return "Enumeration Item";
    }

    @Override
    public String getTypesTitle() {
        return "Enumeration Items";
    }

    @Override
    public boolean delete() {
        ViewOrder order = getOwnerEnum().getViewOrder();
        if (super.delete()) {
            order.setOrdered(this, false);
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected EAccess getDefaultAccess() {
        return super.getMinimumAccess();
    }

    @Override
    public EAccess getMinimumAccess() {
        return EAccess.PUBLIC;
    }

    @Override
    public EAccess getAccessMode() {
        return EAccess.PUBLIC;
    }

    @Override
    public boolean isPublished() {
        AdsEnumDef owner = getOwnerEnum();
        return owner == null ? false : owner.isPublished();
    }

    @Override
    public void appendToUsage(UsageDescription xDef) {
        super.appendToUsage(xDef);
        appendTo(xDef.addNewEnumItem(), true, ESaveMode.USAGE);
    } 

    @Override
    protected void appendAdditionalToolTip(StringBuilder sb) {
        sb.append("<br>Base Type:<br>&nbsp;");
        sb.append(getOwnerEnum().getItemType());
    }
    
    
}
