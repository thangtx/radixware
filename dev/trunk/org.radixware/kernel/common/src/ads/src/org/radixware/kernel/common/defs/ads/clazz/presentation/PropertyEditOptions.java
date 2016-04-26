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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.SearchResult;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.ColumnProperty;
import org.radixware.kernel.common.defs.ads.clazz.presentation.editmask.EditMask;
import org.radixware.kernel.common.defs.ads.type.IAdsTypedObject;
import org.radixware.kernel.common.defs.dds.DdsColumnDef;
import org.radixware.kernel.common.enums.EAccess;
import org.radixware.kernel.common.enums.EEditPossibility;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.exceptions.NoConstItemWithSuchValueError;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.adsdef.PropertyPresentation.Editing;


public class PropertyEditOptions extends EditOptions {

    static class PropertyEditOptionsDelegate extends PropertyEditOptions {

        PropertyEditOptions source;

        public PropertyEditOptionsDelegate(PropertyPresentation context, PropertyEditOptions source) {
            super(context, (Editing) null);
            this.source = source;
        }

        @Override
        public void appendTo(Editing xDef) {
            //do not save
        }

        @Override
        public EEditPossibility getEditPossibility() {
            return source.getEditPossibility();
        }

        @Override
        public List<Id> getObjectEditorPresentations() {
            return source.getObjectEditorPresentations();
        }

        @Override
        public void setEditPossibility(EEditPossibility editPossibility) {
            reportReadOnly();
        }

        @Override
        public boolean setObjectEditorPresentations(List<Id> ep) {
            reportReadOnly();
            return false;
        }

        @Override
        public void setReadSeparately(boolean readSeparately) {
            reportReadOnly();
        }

        private void reportReadOnly() {
            throw new RadixError("An attempt to modify read only object");
        }

        @Override
        public Id getCustomDialogId(ERuntimeEnvironmentType env) {
            return source.getCustomDialogId(env);
        }

        @Override
        public EditMask getEditMask() {
            return source.getEditMask();
        }

        @Override
        public String getNullValTitle(EIsoLanguage language) {
            return source.getNullValTitle(language);
        }

        @Override
        public Id getNullValTitleId() {
            return source.getNullValTitleId();
        }

        @Override
        public boolean isCustomEditOnly() {
            return source.isCustomEditOnly();
        }

        @Override
        public boolean isMemo() {
            return source.isMemo();
        }

        @Override
        public boolean isNotNull() {
            return source.isNotNull();
        }

        @Override
        public boolean isShowDialogButton() {
            return source.isShowDialogButton();
        }

        @Override
        public boolean isStoreEditHistory() {
            return source.isStoreEditHistory();
        }

        @Override
        public boolean isDuplicatesEnabled() {
            return source.isDuplicatesEnabled();
        }

        @Override
        public boolean isReadSeparately() {
            return source.isReadSeparately();
        }

        @Override
        public Id getNullArrayElementTitleId() {
            return source.getNullArrayElementTitleId();
        }

        @Override
        public boolean isArrayElementMandatory() {
            return source.isArrayElementMandatory();
        }

        @Override
        public void setArrayElementMandatory(boolean isArrayElementMandatory) {
            reportReadOnly();
        }

        @Override
        public void setDuplicatesEnabled(boolean isDuplicatesEnabled) {
            reportReadOnly();
        }

        @Override
        public void setNullArrayElementTitleId(Id nullArrayElementTitleId) {
            reportReadOnly();
        }

        @Override
        public int getFirstArrayItemIndex() {
            return source.getFirstArrayItemIndex();
        }

        @Override
        public void setFirstArrayItemIndex(int firstArrayItemIndex) {
            reportReadOnly();

        }

        @Override
        public int getMaxArrayItemCount() {
            return source.getMaxArrayItemCount();
        }

        @Override
        public void setMaxArrayItemCount(int maxArrayItemCount) {
            reportReadOnly();
        }

        @Override
        public int getMinArrayItemCount() {
            return source.getMinArrayItemCount();
        }

        @Override
        public void setMinArrayItemCount(int minArrayItemCount) {
            reportReadOnly();
        }
    }

    @Override
    public IAdsTypedObject getTypedObject() {
        return getOwnerPropertyPresentation().getOwnerProperty().getValue();
    }

    static final class Factory {

        public static PropertyEditOptions newInstance(PropertyPresentation context) {
            return new PropertyEditOptions(context, (Editing) null);
        }

        public static PropertyEditOptions newInstance(PropertyPresentation context, PropertyEditOptions source) {
            return new PropertyEditOptions(context, source);
        }

        public static PropertyEditOptions loadFrom(PropertyPresentation context, Editing xEditing) {
            if (xEditing == null) {
                return newInstance(context);
            }
            return new PropertyEditOptions(context, xEditing);
        }
    }
    private EEditPossibility editPossibility;
    private boolean isDuplicatesEnabled;
    private boolean readSeparately;
    private List<Id> editorPresentationIds;
    private Id nullArrayElementTitleId = null;
    private int minArrayItemCount = -1;
    private int maxArrayItemCount = -1;
    private int firstArrayItemIndex = 1;
    private boolean isArrayElementMandatory = false;

    protected PropertyEditOptions(PropertyPresentation context, PropertyEditOptions source) {
        super(context, source);
        this.editPossibility = source.editPossibility;
        this.isDuplicatesEnabled = source.isDuplicatesEnabled;
        this.editorPresentationIds = source.editorPresentationIds;
        this.readSeparately = source.readSeparately;
        this.minArrayItemCount = source.minArrayItemCount;
        this.maxArrayItemCount = source.maxArrayItemCount;
        this.firstArrayItemIndex = source.firstArrayItemIndex;
    }

    protected PropertyEditOptions(PropertyPresentation context, Editing xEditing) {
        super(context, xEditing);
        if (xEditing == null) {
            this.editPossibility = EEditPossibility.ALWAYS;
            if (context != null) {
                AdsPropertyDef prop = context.getOwnerProperty();

                if (prop instanceof ColumnProperty && ((ColumnProperty) prop).getColumnInfo() != null) {
                    DdsColumnDef column = ((ColumnProperty) prop).getColumnInfo().findColumn();
                    if (column != null && column.isPrimaryKey()) {
                        this.editPossibility = EEditPossibility.ON_CREATE;
                    }
                }
            }
            this.isDuplicatesEnabled = true;
            this.readSeparately = false;

            this.editorPresentationIds = null;
        } else {
            if (xEditing.isSetEditPossibility()) {
                try {
                    this.editPossibility = EEditPossibility.getForValue(xEditing.getEditPossibility());
                } catch (NoConstItemWithSuchValueError e) {
                    this.editPossibility = EEditPossibility.ALWAYS;
                }
            } else {
                this.editPossibility = EEditPossibility.ALWAYS;
            }
            this.isDuplicatesEnabled = xEditing.isSetIsDuplicatesEnabled() ? xEditing.getIsDuplicatesEnabled() : false;
            this.readSeparately = xEditing.isSetReadSeparately() ? xEditing.getReadSeparately() : false;

            if (xEditing.isSetObjectEditorPresentationIdList()) {
                this.editorPresentationIds = xEditing.getObjectEditorPresentationIdList();
            } else {
                if (xEditing.isSetObjectEditorPresentationId()) {
                    this.editorPresentationIds = Collections.singletonList(xEditing.getObjectEditorPresentationId());
                } else {
                    this.editorPresentationIds = null;
                }
            }

            if (xEditing.isSetIsArrayElementMandatory()) {
                this.isArrayElementMandatory = xEditing.getIsArrayElementMandatory();
            }
            this.nullArrayElementTitleId = xEditing.getNullArrayTitleId();
            if (xEditing.isSetArrayBaseIndex()) {
                firstArrayItemIndex = xEditing.getArrayBaseIndex();
            }
            if (xEditing.isSetArrayMaxSize()) {
                maxArrayItemCount = xEditing.getArrayMaxSize();
            }
            if (xEditing.isSetArrayMinSize()) {
                minArrayItemCount = xEditing.getArrayMinSize();
            }
        }
    }

    private PropertyPresentation getOwnerPropertyPresentation() {
        for (RadixObject owner = getContainer(); owner != null; owner = owner.getContainer()) {
            if (owner instanceof PropertyPresentation) {
                return (PropertyPresentation) owner;
            }
        }
        return null;
    }

    public boolean isArrayElementMandatory() {
        return isArrayElementMandatory;
    }

    public void setArrayElementMandatory(boolean isArrayElementMandatory) {
        if (this.isArrayElementMandatory != isArrayElementMandatory) {
            this.isArrayElementMandatory = isArrayElementMandatory;
            setEditState(EEditState.MODIFIED);
        }
    }

    public Id getNullArrayElementTitleId() {
        return nullArrayElementTitleId;
    }

    public void setNullArrayElementTitleId(Id nullArrayElementTitleId) {
        if (this.nullArrayElementTitleId != nullArrayElementTitleId) {
            this.nullArrayElementTitleId = nullArrayElementTitleId;
            setEditState(EEditState.MODIFIED);
        }
    }

    public EEditPossibility getEditPossibility() {
        return editPossibility;
    }

    public void setEditPossibility(EEditPossibility editPossibility) {
        this.editPossibility = editPossibility;
        setEditState(EEditState.MODIFIED);
    }

    public boolean isDuplicatesEnabled() {
        return isDuplicatesEnabled;
    }

    public void setDuplicatesEnabled(boolean isDuplicatesEnabled) {
        this.isDuplicatesEnabled = isDuplicatesEnabled;
        setEditState(EEditState.MODIFIED);
    }

    public boolean isReadSeparately() {
        return readSeparately;
    }

    public void setReadSeparately(boolean readSeparately) {
        this.readSeparately = readSeparately;
        setEditState(EEditState.MODIFIED);
    }

    public int getFirstArrayItemIndex() {
        return firstArrayItemIndex;
    }

    public void setFirstArrayItemIndex(int firstArrayItemIndex) {
        if (this.firstArrayItemIndex != firstArrayItemIndex) {
            this.firstArrayItemIndex = firstArrayItemIndex;
            setEditState(EEditState.MODIFIED);
        }

    }

    public int getMaxArrayItemCount() {
        return maxArrayItemCount;
    }

    public void setMaxArrayItemCount(int maxArrayItemCount) {
        if (this.maxArrayItemCount != maxArrayItemCount) {
            this.maxArrayItemCount = maxArrayItemCount;
            setEditState(EEditState.MODIFIED);
        }
    }

    public int getMinArrayItemCount() {
        return minArrayItemCount;
    }

    public void setMinArrayItemCount(int minArrayItemCount) {
        if (this.minArrayItemCount != minArrayItemCount) {
            this.minArrayItemCount = minArrayItemCount;
            setEditState(EEditState.MODIFIED);
        }
    }

    public void appendTo(Editing xDef) {
        super.appendTo(xDef);
        if (this.editPossibility != EEditPossibility.ALWAYS) {
            xDef.setEditPossibility(this.editPossibility.getValue().longValue());
        }
        if (isDuplicatesEnabled) {
            xDef.setIsDuplicatesEnabled(isDuplicatesEnabled);
        }

        if (readSeparately) {
            xDef.setReadSeparately(readSeparately);
        }

        if (editorPresentationIds != null) {
            xDef.setObjectEditorPresentationIdList(editorPresentationIds);
        }

        if (nullArrayElementTitleId != null) {
            xDef.setNullArrayTitleId(nullArrayElementTitleId);

        }
        if (this.isArrayElementMandatory) {
            xDef.setIsArrayElementMandatory(true);
        }
        if (minArrayItemCount > 0) {
            xDef.setArrayMinSize(minArrayItemCount);
        }
        if (maxArrayItemCount >= 0) {
            xDef.setArrayMaxSize(maxArrayItemCount);
        }
        if (this.firstArrayItemIndex != 1) {
            xDef.setArrayBaseIndex(firstArrayItemIndex);
        }
    }

    protected PropertyPresentation getOwnerPresentation() {
        for (RadixObject owner = getContainer(); owner != null; owner = owner.getContainer()) {
            if (owner instanceof PropertyPresentation) {
                return (PropertyPresentation) owner;
            }
        }
        return null;
    }

    public List<Id> getObjectEditorPresentations() {
        if (this.editorPresentationIds == null) {
            return Collections.emptyList();
        }
        return Collections.unmodifiableList(this.editorPresentationIds);
    }

    public boolean setObjectEditorPresentations(List<Id> ids) {
        if (ids == null || ids.isEmpty()) {
            this.editorPresentationIds = null;
        } else {
            this.editorPresentationIds = new ArrayList<>(ids);

        }
        setEditState(EEditState.MODIFIED);
        return true;
    }

    public AdsEditorPresentationDef findObjectEditorPresentation(Id id) {

        if (id == null) {
            return null;
        }

        final EntityObjectPresentations prs = getOwnerPresentation().findReferencedPresentations();
        if (prs == null) {
            return null;
        }

        final SearchResult<AdsEditorPresentationDef> result = prs.getEditorPresentations().findById(id, EScope.ALL);
        if (!result.isEmpty()) {
            return result.get();
        }
        return null;
    }

    public List<AdsEditorPresentationDef> findObjectEditorPresentations() {

        final List<AdsEditorPresentationDef> editorPresentation = new ArrayList<>();
        for (final Id id : getObjectEditorPresentations()) {
            final AdsEditorPresentationDef presentation = findObjectEditorPresentation(id);
            if (presentation != null) {
                editorPresentation.add(presentation);
            }
        }

        return editorPresentation;
    }

    @Override
    public void visitChildren(IVisitor visitor, VisitorProvider provider) {
        super.visitChildren(visitor, provider);
    }

    @Override
    public void collectDependences(List<Definition> list) {
        super.collectDependences(list);
        final List<AdsEditorPresentationDef> presentations = findObjectEditorPresentations();
        if (presentations != null) {
            list.addAll(presentations);
        }
    }

    @Override
    public void collectUsedMlStringIds(Collection<MultilingualStringInfo> ids) {
        super.collectUsedMlStringIds(ids);
        ids.add(new MultilingualStringInfo(PropertyEditOptions.this) {
            @Override
            public Id getId() {
                return nullArrayElementTitleId;
            }

            @Override
            public void updateId(Id newId) {
                nullArrayElementTitleId = newId;
            }

            @Override
            public EAccess getAccess() {
                PropertyPresentation pp = getOwnerPropertyPresentation();
                if (pp == null) {
                    return EAccess.DEFAULT;
                }
                AdsPropertyDef def = pp.getOwnerProperty();
                if (def == null) {
                    return EAccess.DEFAULT;
                }
                return def.getAccessMode();
            }

            @Override
            public String getContextDescription() {
                return "Title for Null array element";
            }

            @Override
            public boolean isPublished() {
                PropertyPresentation pp = getOwnerPropertyPresentation();
                if (pp == null) {
                    return false;
                }
                AdsPropertyDef def = pp.getOwnerProperty();
                if (def == null) {
                    return false;
                }
                return def.isPublished();
            }
        });
    }
}
