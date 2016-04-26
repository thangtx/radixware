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
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityObjectClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.type.AdsClassType;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.adsdef.SelectorPresentationDefinition;


public class CreatePresentationsList extends RadixObject {

    public interface ICreatePresentationListOwner {

        public CreatePresentationsList getCreatePresentationsList();
    }

    public static class Factory {

        public static CreatePresentationsList newInstance(AdsSelectorPresentationDef owner) {
            return new CreatePresentationsList(owner, null);
        }

        public static CreatePresentationsList newInstance(ObjectPropertyPresentation owner) {
            return new CreatePresentationsList(owner, null);
        }

        @SuppressWarnings("deprecation")
        public static CreatePresentationsList loadFrom(AdsSelectorPresentationDef owner, SelectorPresentationDefinition xDef) {
            final List<Id> ids = new LinkedList<Id>();
            if (xDef.isSetCreationEditorPresentationId()) {
                ids.add(xDef.getCreationEditorPresentationId());
            }
            if (xDef.isSetCreationEditorPresentationIds()) {
                for (Id id : xDef.getCreationEditorPresentationIds()) {
                    if (!ids.contains(id)) {
                        ids.add(id);

                    }
                }
            }
            return new CreatePresentationsList(owner, ids);
        }

        @SuppressWarnings("deprecation")
        public static CreatePresentationsList loadFrom(ObjectPropertyPresentation owner, org.radixware.schemas.adsdef.PropertyPresentation.Object xDef) {
            final List<Id> ids = new LinkedList<Id>();
            if (xDef.isSetObjectCreationPresentationId()) {
                final String idAsStr = xDef.getObjectCreationPresentationId();
                final Id id = Id.Factory.loadFrom(idAsStr);
                if (id != null) {
                    ids.add(id);
                }
            }
            if (xDef.isSetCreationEditorPresentationIds()) {
                for (Id id : xDef.getCreationEditorPresentationIds()) {
                    if (!ids.contains(id)) {
                        ids.add(id);
                    }
                }
            }
            return new CreatePresentationsList(owner, ids);
        }

        public static CreatePresentationsList newCopy(ObjectPropertyPresentation owner, CreatePresentationsList src) {
            return new CreatePresentationsList(owner, src.getPresentationIds());
        }

        public static CreatePresentationsList newCopy(AdsSelectorPresentationDef owner, CreatePresentationsList src) {
            return new CreatePresentationsList(owner, src.getPresentationIds());
        }
    }

    public class PresentationRef {

        private final Id id;

        private PresentationRef(Id id) {
            this.id = id;
        }

        public Id getPresentationId() {
            return id;
        }

        public AdsEditorPresentationDef findEditorPresentation() {
            return CreatePresentationsList.this.findEditorPresentation(id);
        }
    }
    private List<Id> ids = null;

    private CreatePresentationsList(RadixObject container, List<Id> ids) {
        super("Creation Editor Presentation List");
        setContainer(container);
        if (ids != null && !ids.isEmpty()) {
            this.ids = ids;
        }
    }

    public void appendTo(org.radixware.schemas.adsdef.PropertyPresentation.Object xDef) {
        synchronized (this) {
            if (ids != null && !ids.isEmpty()) {
                xDef.setCreationEditorPresentationIds(ids);
            }
        }
    }

    public void appendTo(SelectorPresentationDefinition xDef) {
        synchronized (this) {
            if (ids != null && !ids.isEmpty()) {
                xDef.setCreationEditorPresentationIds(ids);
            }
        }
    }

    public boolean isEmpty() {
        return ids == null || ids.isEmpty();
    }

    public AdsEntityObjectClassDef findTargetClass() {
        RadixObject container = getContainer();
        if (container instanceof AdsSelectorPresentationDef) {
            return ((AdsSelectorPresentationDef) container).getOwnerClass();
        } else if (container instanceof ObjectPropertyPresentation) {
            AdsPropertyDef prop = ((ObjectPropertyPresentation) container).getOwnerProperty();
            if (prop == null) {
                return null;
            }
            AdsTypeDeclaration decl = prop.getValue().getType();
            if (decl == null) {
                return null;
            }
            AdsType type = decl.resolve(prop).get();
            if (type instanceof AdsClassType) {
                AdsClassDef clazz = ((AdsClassType) type).getSource();
                if (clazz instanceof AdsEntityObjectClassDef) {
                    return (AdsEntityObjectClassDef) clazz;
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public List<Id> getPresentationIds() {
        synchronized (this) {
            if (ids == null) {
                return Collections.emptyList();
            } else {
                return new ArrayList<Id>(ids);
            }
        }
    }

    public List<PresentationRef> getPresentationRefs() {
        synchronized (this) {
            if (ids == null) {
                return Collections.emptyList();
            } else {
                List<PresentationRef> list = new ArrayList<PresentationRef>(ids.size());
                for (Id id : ids) {
                    list.add(new PresentationRef(id));
                }
                return list;
            }
        }
    }

    public void addPresentationId(Id id) {
        synchronized (this) {
            if (ids == null) {
                ids = new LinkedList<Id>();
                ids.add(id);
                setEditState(EEditState.MODIFIED);
            } else if (!ids.contains(id)) {
                ids.add(id);
                setEditState(EEditState.MODIFIED);
            }
        }
    }

    public void removePresentationId(Id id) {
        synchronized (this) {
            if (ids == null) {
                return;
            }
            if (ids.remove(id)) {
                if (ids.isEmpty()) {
                    ids = null;
                }
                setEditState(EEditState.MODIFIED);
            }
        }
    }

    public void movePresentationUp(Id id) {
        movePresentation(id, -1);
    }

    public void movePresentationDn(Id id) {
        movePresentation(id, 1);
    }

    private void movePresentation(Id id, int dir) {
        synchronized (this) {
            if (ids == null || ids.isEmpty()) {
                return;
            }
            int index = ids.indexOf(id);
            if (index < 0) {
                return;
            }
            int replaceWith = index + dir;
            if (replaceWith < 0 || replaceWith >= ids.size()) {
                return;
            }

            Id old = ids.get(replaceWith);
            ids.set(replaceWith, id);
            ids.set(index, old);
            setEditState(EEditState.MODIFIED);
        }
    }

    @Override
    public void collectDependences(List<Definition> list) {
        super.collectDependences(list);
        if (ids != null && !ids.isEmpty()) {
            AdsEntityObjectClassDef clazz = findTargetClass();
            if (clazz != null) {
                for (Id id : ids) {
                    AdsEditorPresentationDef epr = findEditorPresentation(clazz, id);
                    if (epr != null) {
                        list.add(epr);
                    }
                }
            }
        }
    }

    private AdsEditorPresentationDef findEditorPresentation(AdsEntityObjectClassDef clazz, Id id) {
        return clazz.getPresentations().getEditorPresentations().findById(id, EScope.ALL).get();
    }

    private AdsEditorPresentationDef findEditorPresentation(Id id) {
        AdsEntityObjectClassDef clazz = findTargetClass();
        if (clazz != null) {
            return findEditorPresentation(clazz, id);
        } else {
            return null;
        }
    }
}
