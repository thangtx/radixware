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

package org.radixware.kernel.common.defs.ads.clazz.presentation;

import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.defs.ClipboardSupport;
import org.radixware.kernel.common.defs.ClipboardSupport.Transfer;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.AdsClipboardSupport;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassMember;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.localization.AdsMultilingualStringDef;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.defs.ads.src.IJavaSource;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.src.clazz.presentation.AdsObjectTitleFormatWriter;
import org.radixware.kernel.common.defs.ads.type.AdsClassType;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.defs.localization.ILocalizedDef;
import org.radixware.kernel.common.defs.localization.IMultilingualStringDef;
import org.radixware.kernel.common.enums.EAccess;
import org.radixware.kernel.common.enums.EDefType;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.exceptions.DefinitionError;
import org.radixware.kernel.common.exceptions.RadixObjectError;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.adsdef.ObjectTitleFormatDefinition;


public class AdsObjectTitleFormatDef extends AdsClassMember implements IJavaSource {

    public static final class Factory {

        public static final AdsObjectTitleFormatDef loadFrom(ObjectTitleFormatDefinition xDef) {
            if (xDef == null) {
                return newInstance(null);
            }
            return new AdsObjectTitleFormatDef(null, xDef);
        }

        public static final AdsObjectTitleFormatDef newInstance() {
            return new AdsObjectTitleFormatDef(null);
        }

        public static final AdsObjectTitleFormatDef loadFrom(AdsDefinition owner, ObjectTitleFormatDefinition xDef) {
            if (xDef == null) {
                return newInstance(owner);
            }
            return new AdsObjectTitleFormatDef(owner, xDef);
        }

        public static final AdsObjectTitleFormatDef newInstance(AdsDefinition owner) {
            return new AdsObjectTitleFormatDef(owner);
        }

        public static final AdsObjectTitleFormatDef newCopy(AdsDefinition owner, AdsObjectTitleFormatDef source) {
            return new AdsObjectTitleFormatDef(owner, source);
        }
    }

    public static class TitleItem extends RadixObject implements ILocalizedDef {

        @Override
        public void collectUsedMlStringIds(Collection<MultilingualStringInfo> ids) {
            ids.add(new MultilingualStringInfo(TitleItem.this) {
                @Override
                public Id getId() {
                    return patternId;
                }

                @Override
                public void updateId(Id newId) {
                    patternId = newId;
                }

                @Override
                public EAccess getAccess() {
                    return TitleItem.this.getOwnerTitleFormat().getAccessMode();
                }

                @Override
                public String getContextDescription() {
                    return "Object Title Format";
                }

                @Override
                public boolean isPublished() {
                    return TitleItem.this.getOwnerTitleFormat().isPublished();
                }
            });
        }

        @Override
        public AdsMultilingualStringDef findLocalizedString(Id stringId) {
            return this.getOwnerTitleFormat().findLocalizedString(stringId);
        }

        public static class Factory {

            public static final TitleItem newInstance(AdsPropertyDef prop) {
                return new TitleItem(prop);
            }
        }
        private String pattern;
        private Id patternId;
        private Id propertyId;

        private TitleItem(final ObjectTitleFormatDefinition.TitleItem xDef) {
            this.pattern = xDef.getPattern();
            this.patternId = xDef.getPatternId();
            this.propertyId = xDef.getPropId();
        }

        private TitleItem(final AdsDefinition owner, final TitleItem source) {
            this.pattern = source.pattern;
            this.patternId = null;
            if (source.patternId != null) {
                final IMultilingualStringDef string = source.findLocalizedString(patternId);
                if (string != null) {
                    final IMultilingualStringDef clone = string.cloneString(owner.findLocalizingBundle());
                    this.patternId = clone.getId();
                }
            }
            this.propertyId = source.getPropertyId();
        }

        private TitleItem(final AdsPropertyDef prop) {
            this.pattern = "{0}";
            this.patternId = null;
            this.propertyId = prop.getId();
        }

        public boolean isMultilingual() {
            return this.patternId != null;
        }

        public boolean setMultilingual(boolean multilingual) {
            if (getOwnerTitleFormat() instanceof Unmodifiable) {
                throw new RadixObjectError("Definition is not modifiable.", this);
            }
            if (multilingual) {
                String defaultValue = this.pattern;
                if (defaultValue == null) {
                    defaultValue = "";

                }
                AdsModule module = getOwnerTitleFormat().getModule();
                EnumSet<EIsoLanguage> languages = EnumSet.noneOf(EIsoLanguage.class);
                if (module != null) {
                    languages.addAll(module.getSegment().getLayer().getLanguages());
                }
                if (languages.isEmpty()) {
                    languages.add(EIsoLanguage.ENGLISH);
                }
                for (EIsoLanguage l : languages) {
                    Id id = getOwnerTitleFormat().setLocalizedStringValue(l, patternId, defaultValue);
                    if (id == null) {
                        patternId = null;
                        return false;
                    } else {
                        if (patternId == null) {
                            patternId = id;
                        }
                    }
                }
            } else {
                this.patternId = null;
            }
            setEditState(EEditState.MODIFIED);
            return true;
        }

        public Id getPatternId() {
            return patternId;
        }

        public Id getPropertyId() {
            return propertyId;
        }

        public void setPropertyId(Id id) {
            propertyId = id;
            setEditState(EEditState.MODIFIED);
        }

        public boolean moveUp() {
            TitleItems items = (TitleItems) getContainer();
            int index = items.indexOf(this);
            if (index > 0) {
                items.remove(this);
                items.add(index - 1, this);
                return true;
            } else {
                return false;
            }
        }

        public boolean moveDn() {
            TitleItems items = (TitleItems) getContainer();
            int index = items.indexOf(this);
            if (index < items.size() - 1) {
                items.remove(this);
                items.add(index + 1, this);
                return true;
            } else {
                return false;
            }
        }

        public String getPattern() {
            return pattern;
        }

        public void setPattern(String newPattern) {
            if (getOwnerTitleFormat() instanceof Unmodifiable) {
                throw new RadixObjectError("Definition is not modifiable.", this);
            }
            if (isMultilingual()) {
                throw new RadixObjectError("Language must be specified for title item pattern.", this);
            }
            this.pattern = newPattern;
            setEditState(EEditState.MODIFIED);
        }

        public String getPattern(EIsoLanguage language) {
            if (isMultilingual()) {
                return getOwnerTitleFormat().getLocalizedStringValue(language, patternId);
            } else {
                return null;

            }
        }

        public boolean setPattern(EIsoLanguage language, String value) {
            if (getOwnerTitleFormat() instanceof Unmodifiable) {
                throw new RadixObjectError("Definition is not modifiable.", this);
            }
            if (isMultilingual()) {
                Id id = getOwnerTitleFormat().setLocalizedStringValue(language, patternId, value);
                if (id != null) {
                    this.patternId = id;
                    setEditState(EEditState.MODIFIED);
                    return true;
                } else {
                    return false;
                }
            } else {
                throw new RadixObjectError("Language must not be specified.", this);
            }
        }

        public AdsPropertyDef findProperty() {
            AdsDefinition ownerDef = getOwnerTitleFormat();
            AdsClassDef propOwner = null;
            while (ownerDef != null) {
                if (ownerDef instanceof AdsEditorPresentationDef) {
                    propOwner = ((AdsEditorPresentationDef) ownerDef).getOwnerClass();
                    break;
                } else if (ownerDef instanceof AdsPropertyDef) {
                    AdsPropertyDef prop = (AdsPropertyDef) ownerDef;
                    AdsTypeDeclaration decl = prop.getValue().getType();
                    if (decl.getTypeId() == EValType.PARENT_REF || decl.getTypeId() == EValType.ARR_REF) {
                        AdsType type = decl.resolve(prop).get();
                        if (type instanceof AdsClassType) {
                            propOwner = ((AdsClassType) type).getSource();
                        }
                    } else {
                        propOwner = prop.getOwnerClass();
                    }
                    break;
                } else if (ownerDef instanceof AdsClassDef) {
                    propOwner = (AdsClassDef) ownerDef;
                    break;
                } else {
                    ownerDef = ownerDef.getOwnerDef();
                }
            }
            if (propOwner == null) {
                return null;
            }
            return propOwner.getProperties().findById(propertyId, EScope.ALL).get();
        }

        private AdsObjectTitleFormatDef getOwnerTitleFormat() {
            for (RadixObject owner = getContainer(); owner != null; owner = owner.getContainer()) {
                if (owner instanceof AdsObjectTitleFormatDef) {
                    return (AdsObjectTitleFormatDef) owner;
                }
            }
            return null;
        }

        private void appendTo(ObjectTitleFormatDefinition.TitleItem xDef) {
            xDef.setPattern(pattern);
            xDef.setPatternId(patternId);
            xDef.setPropId(propertyId);
        }

        @Override
        public void collectDependences(List<Definition> list) {
            super.collectDependences(list);
            AdsPropertyDef prop = findProperty();
            if (prop != null) {
                list.add(prop);

            }
            if (patternId != null) {
                AdsMultilingualStringDef str = getOwnerTitleFormat().findLocalizedString(patternId);
                if (str != null) {
                    list.add(str);
                }
            }
        }

        @Override
        public ClipboardSupport<TitleItem> getClipboardSupport() {
            return new AdsClipboardSupport<TitleItem>(this) {
                @Override
                protected XmlObject copyToXml() {
                    ObjectTitleFormatDefinition.TitleItem xDef = ObjectTitleFormatDefinition.TitleItem.Factory.newInstance();
                    appendTo(xDef);
                    return xDef;
                }

                @Override
                protected TitleItem loadFrom(XmlObject xmlObject) {
                    if (xmlObject instanceof ObjectTitleFormatDefinition.TitleItem) {
                        return new TitleItem((ObjectTitleFormatDefinition.TitleItem) xmlObject);
                    } else {
                        return super.loadFrom(xmlObject);
                    }
                }
            };
        }
    }

    private class TitleItems extends RadixObjects<TitleItem> {

        private TitleItems() {
            super(AdsObjectTitleFormatDef.this);
        }

        private TitleItems(List<ObjectTitleFormatDefinition.TitleItem> items) {
            super(AdsObjectTitleFormatDef.this);
        }

        @Override
        public void add(TitleItem object) {
            if (getOwnerDefinition() instanceof Unmodifiable) {
                throw new RadixObjectError("Definition is not modifiable.", this);
            }
            super.add(object);
        }

        @Override
        public void add(int index, TitleItem object) {
            if (getOwnerDefinition() instanceof Unmodifiable) {
                throw new RadixObjectError("Definition is not modifiable.", this);
            }

            super.add(index, object);
        }

        @Override
        public void clear() {
            if (getOwnerDefinition() instanceof Unmodifiable) {
                throw new RadixObjectError("Definition is not modifiable.", this);
            }

            super.clear();
        }

        @Override
        public boolean remove(TitleItem object) {
            if (getOwnerDefinition() instanceof Unmodifiable) {
                throw new RadixObjectError("Definition is not modifiable.", this);
            }

            return super.remove(object);
        }

        @Override
        public TitleItem remove(int index) {
            if (getOwnerDefinition() instanceof Unmodifiable) {
                throw new RadixObjectError("Definition is not modifiable.", this);
            }

            return super.remove(index);
        }

        @Override
        protected ClipboardSupport.CanPasteResult canPaste(List<Transfer> objectsInClipboard, ClipboardSupport.DuplicationResolver resolver) {
            for (Transfer t : objectsInClipboard) {
                if (!canPaste(t)) {
                    return ClipboardSupport.CanPasteResult.NO;
                }
            }
            return ClipboardSupport.CanPasteResult.YES;
        }

        private boolean canPaste(Transfer objectInClipboard) {
            return objectInClipboard.getObject() instanceof TitleItem;
        }
    }
    //  private final IAdsPresentationPoperties propertySet;
    private Id nullTitleId;
    private final TitleItems titleItems;

    protected AdsObjectTitleFormatDef(AdsDefinition owner, ObjectTitleFormatDefinition xDef) {
        super(xDef.getId(), "TitleFormat");
        setContainer(owner);
        this.nullTitleId = xDef.getNullTitleId();
        List<ObjectTitleFormatDefinition.TitleItem> items = xDef.getTitleItemList();
        if (items != null && !items.isEmpty()) {
            titleItems = new TitleItems(items);
            for (ObjectTitleFormatDefinition.TitleItem item : items) {
                titleItems.add(new TitleItem(item));
            }
        } else {
            titleItems = new TitleItems();
        }
    }

    protected AdsObjectTitleFormatDef(AdsDefinition owner) {
        super(Id.Factory.newInstance(EDefinitionIdPrefix.ENTITY_TITLE_FORMAT), "TitleFormat");
        this.setContainer(owner);
        this.nullTitleId = null;
        this.titleItems = new TitleItems();
    }

    protected AdsObjectTitleFormatDef(AdsDefinition owner, AdsObjectTitleFormatDef source) {
        super(Id.Factory.newInstance(EDefinitionIdPrefix.ENTITY_TITLE_FORMAT), "TitleFormat");
        this.setContainer(owner);
        this.nullTitleId = null;
        if (source.nullTitleId != null) {
            IMultilingualStringDef string = source.findLocalizedString(source.nullTitleId);
            if (string != null) {
                IMultilingualStringDef clone = string.cloneString(findLocalizingBundle());
                this.nullTitleId = clone.getId();
            }
        }

        this.titleItems = new TitleItems();
        for (TitleItem item : source.getItems()) {
            TitleItem newItem = new TitleItem(owner, item);
            this.titleItems.add(newItem);
        }
    }

    public RadixObjects<TitleItem> getItems() {
        return titleItems;
    }

    public void appendTo(ObjectTitleFormatDefinition xDef, ESaveMode saveMode) {
        super.appendTo(xDef, saveMode);
        if (!titleItems.isEmpty()) {
            for (TitleItem t : titleItems) {
                t.appendTo(xDef.addNewTitleItem());
            }
        }
        if (nullTitleId != null) {
            xDef.setNullTitleId(nullTitleId);
        }
    }

    public String getNullValTitle(EIsoLanguage language) {
        return getLocalizedStringValue(language, nullTitleId);
    }

    public Id getNullValTitleId() {
        return nullTitleId;
    }

    public void setNullValTitleId(Id id) {
        nullTitleId = id;
        setEditState(EEditState.MODIFIED);
    }

    public boolean setNullValTitle(EIsoLanguage language, String value) {
        if (this instanceof Unmodifiable) {
            throw new DefinitionError("Definition is not modifiable.", this);
        }
        Id id = this.setLocalizedStringValue(language, nullTitleId, value);
        if (id != null) {
            nullTitleId = id;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void visitChildren(IVisitor visitor, VisitorProvider provider) {
        super.visitChildren(visitor, provider);
        this.titleItems.visit(visitor, provider);
    }

    private class Unmodifiable extends AdsObjectTitleFormatDef {

        public Unmodifiable(AdsObjectTitleFormatDef source) {
            super(source.getOwnerDef());
        }
    }

    public AdsObjectTitleFormatDef unmodifiableInstance() {
        return this;
    }

    @Override
    public JavaSourceSupport getJavaSourceSupport() {
        return new JavaSourceSupport(this) {
            @Override
            public CodeWriter getCodeWriter(UsagePurpose purpose) {
                return new AdsObjectTitleFormatWriter(this, AdsObjectTitleFormatDef.this, purpose);
            }
        };
    }

    @Override
    public void collectDependences(List<Definition> list) {
        super.collectDependences(list);
        if (nullTitleId != null) {
            AdsMultilingualStringDef str = findLocalizedString(nullTitleId);
            if (str != null) {
                list.add(str);
            }
        }
    }

    @Override
    public void collectUsedMlStringIds(Collection<MultilingualStringInfo> ids) {
        super.collectUsedMlStringIds(ids);
        ids.add(new MultilingualStringInfo(AdsObjectTitleFormatDef.this) {
            @Override
            public Id getId() {
                return nullTitleId;
            }

            @Override
            public void updateId(Id newId) {
                nullTitleId = newId;
            }

            @Override
            public EAccess getAccess() {
                return AdsObjectTitleFormatDef.this.getAccessMode();
            }

            @Override
            public String getContextDescription() {
                return "Title Instead of Null";
            }

            @Override
            public boolean isPublished() {
                return AdsObjectTitleFormatDef.this.isPublished();
            }
        });
    }

    @Override
    public ClipboardSupport<AdsObjectTitleFormatDef> getClipboardSupport() {
        return new AdsClipboardSupport<AdsObjectTitleFormatDef>(this) {
            @Override
            protected XmlObject copyToXml() {
                ObjectTitleFormatDefinition xDef = ObjectTitleFormatDefinition.Factory.newInstance();
                appendTo(xDef, ESaveMode.NORMAL);
                return xDef;
            }

            @Override
            protected AdsObjectTitleFormatDef loadFrom(XmlObject xmlObject) {
                if (xmlObject instanceof ObjectTitleFormatDefinition) {
                    return Factory.loadFrom((ObjectTitleFormatDefinition) xmlObject);
                } else {
                    return super.loadFrom(xmlObject);
                }
            }
        };
    }

    @Override
    public EDefType getDefinitionType() {
        return EDefType.TITLE_FORMAT;
    }

    @Override
    public RadixIcon getIcon() {
        return AdsDefinitionIcon.OBJECT_TITLE_FORMAT;
    }

}
