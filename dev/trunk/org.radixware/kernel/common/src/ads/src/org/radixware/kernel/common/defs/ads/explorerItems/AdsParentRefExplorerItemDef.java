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
package org.radixware.kernel.common.defs.ads.explorerItems;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.defs.ClipboardSupport;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.AdsClipboardSupport;
import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;
import org.radixware.kernel.common.defs.ads.AdsDefinitions;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityObjectClassDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPresentationDef;
import org.radixware.kernel.common.defs.ads.command.AdsCommandDef;
import org.radixware.kernel.common.defs.ads.common.AdsUtils;
import org.radixware.kernel.common.defs.ads.common.Restrictions;
import org.radixware.kernel.common.defs.ads.module.AdsSearcher;
import org.radixware.kernel.common.defs.ads.radixdoc.ParentRefExpItemRadixdoc;
import org.radixware.kernel.common.defs.dds.DdsReferenceDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.defs.localization.IMultilingualStringDef;
import org.radixware.kernel.common.enums.EExplorerItemAttrInheritance;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.enums.ERestriction;
import org.radixware.kernel.common.exceptions.DefinitionError;
import org.radixware.kernel.common.radixdoc.DocumentOptions;
import org.radixware.kernel.common.radixdoc.IRadixdocPage;
import org.radixware.kernel.common.radixdoc.IRadixdocProvider;
import org.radixware.kernel.common.radixdoc.RadixdocSupport;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.schemas.adsdef.ChildExplorerItems;
import org.radixware.schemas.radixdoc.Page;

public class AdsParentRefExplorerItemDef extends AdsNodeExplorerItemDef implements IRadixdocProvider {

    public static final class Factory {

        public static final AdsParentRefExplorerItemDef loadFrom(ChildExplorerItems.Item.ParentRef xDef) {
            return new AdsParentRefExplorerItemDef(xDef);
        }

        public static final AdsParentRefExplorerItemDef newTemporaryInstance(RadixObject container) {
            AdsParentRefExplorerItemDef item = new AdsParentRefExplorerItemDef(null, null);
            item.setContainer(container);
            return item;
        }

        public static final AdsParentRefExplorerItemDef newInstance(AdsEntityObjectClassDef referencedClass, DdsReferenceDef ref) {
            DdsTableDef table = referencedClass.findTable(referencedClass);
            if (table == null) {
                throw new DefinitionError("Context table not found.");
            }
            if (!Utils.equals(table.getId(), ref.getParentTableId())) {
                throw new DefinitionError("Context class does not based on parent table of given reference.");
            }

            return new AdsParentRefExplorerItemDef(referencedClass, ref);
        }
    }
    private Id parentReferenceId;
    private List<Id> editorPresentationIds;
    private final Restrictions restrictions;

    protected AdsParentRefExplorerItemDef(ChildExplorerItems.Item.ParentRef xDef) {
        super(xDef);
        if (xDef.getEditorPresentationIds() != null && !xDef.getEditorPresentationIds().isEmpty()) {
            this.editorPresentationIds = new ArrayList<>(xDef.getEditorPresentationIds());
        } else {
            this.editorPresentationIds = null;
        }

        this.parentReferenceId = xDef.getParentReferenceId();
        this.restrictions = Restrictions.Factory.newInstance(this, xDef.getRestrictions());
    }

    protected AdsParentRefExplorerItemDef(AdsEntityObjectClassDef clazz, DdsReferenceDef ref) {
        super();
        this.classId = clazz == null ? null : clazz.getId();
        this.parentReferenceId = ref == null ? null : ref.getId();
        this.restrictions = Restrictions.Factory.newInstance(this, 0L);
    }

    public boolean addEditorPresentationId(Id id) {
        if (editorPresentationIds == null) {
            editorPresentationIds = new ArrayList<>();
        }
        if (editorPresentationIds.contains(id)) {
            return false;
        }
        if (editorPresentationIds.add(id)) {
            setEditState(EEditState.MODIFIED);
            return true;
        }
        return false;
    }

    public boolean removeEditorPresentationId(Id id) {
        if (editorPresentationIds == null) {
            return false;
        }
        if (editorPresentationIds.remove(id)) {
            setEditState(EEditState.MODIFIED);
            return true;
        }
        return false;
    }

    public List<Id> getEditorPresentationIds() {
        if (editorPresentationIds == null) {
            return Collections.emptyList();
        } else {
            return new ArrayList<>(editorPresentationIds);
        }
    }

    public void appendTo(ChildExplorerItems.Item.ParentRef xDef, ESaveMode saveMode) {
        super.appendTo(xDef, saveMode);
        if (editorPresentationIds != null && !editorPresentationIds.isEmpty()) {
            xDef.setEditorPresentationIds(editorPresentationIds);
        }
        if (parentReferenceId != null) {
            xDef.setParentReferenceId(parentReferenceId);
        }
        xDef.setRestrictions(ERestriction.toBitField(restrictions.getRestriction()));
    }

    public DdsReferenceDef findParentReference() {
        return AdsSearcher.Factory.newDdsReferenceSearcher(this).findById(parentReferenceId).get();
    }

    public Id getParentReferenceId() {
        return parentReferenceId;
    }

    public void setParentReferenceId(Id id) {
        this.parentReferenceId = id;
        setEditState(EEditState.MODIFIED);
        fireNameChange();
    }

    public class EditorPresentationInfo {

        private Id id;

        public Id getId() {
            return id;
        }

        private EditorPresentationInfo(Id id) {
            this.id = id;
        }

        public AdsEditorPresentationDef findEditorPresentation() {
            AdsEntityObjectClassDef clazz = findReferencedEntityClass();
            if (clazz == null) {
                return null;
            } else {
                return clazz.getPresentations().getEditorPresentations().findById(id, EScope.ALL).get();
            }
        }
    }

    public List<EditorPresentationInfo> getEditorPresentationInfos() {
        if (editorPresentationIds == null || editorPresentationIds.isEmpty()) {
            return Collections.emptyList();
        } else {
            final ArrayList<EditorPresentationInfo> infos = new ArrayList<>(editorPresentationIds.size());
            for (Id id : editorPresentationIds) {
                infos.add(new EditorPresentationInfo(id));
            }
            return infos;
        }
    }

    public Restrictions getRestrictions() {
        AdsEntityObjectClassDef ref = findReferencedEntityClass();
        AdsEntityClassDef clazz = null;
        if (ref != null) {
            clazz = ref.findRootBasis();
        }
        if (clazz != null) {
            if (isRestrictionInherited()) {
                return clazz.getPresentations().getRestrictions();
            } else {
                return Restrictions.Factory.complementarInstance(restrictions, clazz.getPresentations().getRestrictions());
            }
        } else {
            return restrictions;
        }
    }

    @Override
    public String getName() {
        String ownName = super.getName();
        if (ownName == null || ownName.isEmpty() || "EI".equals(ownName)) {
            AdsEntityObjectClassDef referencedClass = findReferencedEntityClass();
            if (referencedClass == null) {
                if (classId != null) {
                    return classId.toString();
                } else {
                    return "<undefined>";
                }
            } else {
                return referencedClass.getName();// getQualifiedName(this); // RADIX-1113
            }
        } else {
            return ownName;
        }
    }

    @Override
    public RadixIcon getIcon() {
        return AdsDefinitionIcon.PARAGRAPH_EXPLORER_ITEM;
    }

    @Override
    protected void collectDependencesImpl(final boolean direct, final boolean forModule, List<Definition> list) {
        super.collectDependencesImpl(direct, forModule, list);
        DdsReferenceDef ref = findParentReference();
        if (ref != null) {
            list.add(ref);
        }
        if (!forModule) {
            List<EditorPresentationInfo> elist = getEditorPresentationInfos();
            for (EditorPresentationInfo info : elist) {
                AdsEditorPresentationDef e = info.findEditorPresentation();
                if (e != null) {
                    list.add(e);
                }
            }
        }
        if (!isRestrictionInherited()) {
            Restrictions restrs = getRestrictions();
            if (restrs != null && restrs.isDenied(ERestriction.ANY_COMMAND)) {
                AdsEntityObjectClassDef clazz = findReferencedEntityClass();
                if (clazz != null) {
                    for (Id id : restrs.getEnabledCommandIds()) {
                        AdsCommandDef cmd = clazz.getPresentations().getCommands().findById(id, EScope.ALL).get();
                        if (cmd != null) {
                            list.add(cmd);
                        }
                    }
                }
            }
        }
    }

    @Override
    public ClipboardSupport<AdsParentRefExplorerItemDef> getClipboardSupport() {
        return new AdsClipboardSupport<AdsParentRefExplorerItemDef>(this) {
            @Override
            protected XmlObject copyToXml() {
                ChildExplorerItems.Item.ParentRef xDef = ChildExplorerItems.Item.ParentRef.Factory.newInstance();
                appendTo(xDef, ESaveMode.NORMAL);
                return xDef;
            }

            @Override
            protected AdsParentRefExplorerItemDef loadFrom(XmlObject xmlObject) {
                if (xmlObject instanceof ChildExplorerItems.Item.ParentRef) {
                    return Factory.loadFrom((ChildExplorerItems.Item.ParentRef) xmlObject);
                } else {
                    return super.loadFrom(xmlObject);
                }
            }

            @Override
            protected Method getDecoderMethod() {
                try {
                    return Factory.class.getDeclaredMethod("loadFrom", ChildExplorerItems.Item.ParentRef.class);
                } catch (NoSuchMethodException | SecurityException ex) {
                    return null;
                }
            }
        };
    }

    @Override
    public boolean isSuitableContainer(AdsDefinitions collection) {
        if (super.isSuitableContainer(collection)) {
            RadixObject owner = collection.getContainer();
            if (owner instanceof ExplorerItems.Children) {
                return ((ExplorerItems.Children) owner).getOwnerExplorerItems().getPlacement() == ExplorerItems.EPlacement.EDITOR_PRESENTATION;
            } else {
                return false;
            }
        }
        return false;
    }

    private AdsEditorPresentationDef findEditorPresentation() {
        if (editorPresentationIds != null) {
            AdsEntityObjectClassDef clazz = findReferencedEntityClass();
            if (clazz != null) {
                for (Id id : editorPresentationIds) {
                    AdsEditorPresentationDef epr = clazz.getPresentations().getEditorPresentations().findById(id, EScope.ALL).get();
                    if (epr != null) {
                        return epr;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public String getTitle(EIsoLanguage lang) {
        if (isTitleInherited()) {
            final AdsEditorPresentationDef editorPresentation = findEditorPresentation();
            if (editorPresentation != null) {
                return editorPresentation.getTitle(lang);
            }
            final AdsEntityObjectClassDef referencedEntityClass = findReferencedEntityClass();
            if (referencedEntityClass != null) {
                return referencedEntityClass.getPresentations().getObjectTitle(lang);
            }
            return null;
        }

        return getLocalizedStringValue(lang, titleId);
    }

    @Override
    public Id getTitleId() {
        if (isTitleInherited()) {
            final AdsEditorPresentationDef editorPresentation = findEditorPresentation();
            if (editorPresentation != null) {
                return editorPresentation.getTitleId();
            }
            final AdsEntityObjectClassDef referencedEntityClass = findReferencedEntityClass();
            if (referencedEntityClass != null) {
                return referencedEntityClass.getPresentations().getObjectTitleId();
            }
            return null;
        }
        return titleId;
    }

    @Override
    public Definition findOwnerTitleDefinition() {
        if (isTitleInherited()) {
            final AdsEditorPresentationDef editorPresentation = findEditorPresentation();
            return AdsUtils.findTitleOwnerDefinition(editorPresentation != null ? editorPresentation : findReferencedEntityClass());
        }
        return this;
    }

    @Override
    public boolean setTitleInherited(boolean inherit) {
        if (inherit == isTitleInherited()) {
            return false;
        }

        if (inherit) {
            this.inheritanceMask.add(EExplorerItemAttrInheritance.TITLE);
            setEditState(EEditState.MODIFIED);
            return true;
        } else {
            IMultilingualStringDef string = findTitleStorage();
            if (string != null) {
                IMultilingualStringDef clone = string.cloneString(findLocalizingBundle());
                titleId = clone.getId();
            }
            this.inheritanceMask.remove(EExplorerItemAttrInheritance.TITLE);
            setEditState(EEditState.MODIFIED);
            return true;
        }
    }

    public IMultilingualStringDef findTitleStorage() {
        if (isTitleInherited()) {
            Definition ownerTitleDefinition = findOwnerTitleDefinition();
            if (ownerTitleDefinition != null) {
                return ownerTitleDefinition.findLocalizedString(getTitleId());
            } else {
                return null;
            }
        } else {
            return findLocalizedString(titleId);
        }
    }

    @Override
    public boolean isRadixdocProvider() {
        return true;
    }

    @Override
    public RadixdocSupport<? extends RadixObject> getRadixdocSupport() {
        return new RadixdocSupport<AdsParentRefExplorerItemDef>(this) {
            @Override
            public IRadixdocPage document(Page page, DocumentOptions options) {
                return new ParentRefExpItemRadixdoc(getSource(), page, options);
            }
        };
    }
}