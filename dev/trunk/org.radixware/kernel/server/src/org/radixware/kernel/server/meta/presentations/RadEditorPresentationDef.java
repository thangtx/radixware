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
package org.radixware.kernel.server.meta.presentations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.server.meta.clazzes.RadPropDef;

import org.radixware.kernel.server.types.Entity;
import org.radixware.kernel.server.types.Restrictions;
import java.util.Map;
import org.radixware.kernel.common.enums.EDrcResourceType;
import org.radixware.kernel.common.enums.EEditPossibility;
import org.radixware.kernel.common.enums.EEditorPresentationRightsInheritanceMode;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.enums.EPresentationAttrInheritance;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.exceptions.DefinitionNotFoundError;
import org.radixware.kernel.common.exceptions.IllegalUsageError;
import org.radixware.kernel.server.arte.services.eas.EasValueConverter;
import org.radixware.kernel.server.meta.roles.RadRoleDef;

public class RadEditorPresentationDef extends RadPresentationDef {

    private static final int MAX_REPLACE_ITER_COUNT = 100;
    //Inheritance bits used by server

    static public final class CInheritance {

        static public final int CUSTOM_EDITOR = EPresentationAttrInheritance.CUSTOM_DIALOG.getValue().intValue();
        static public final int RESTRICTIONS = EPresentationAttrInheritance.RESTRICTIONS.getValue().intValue();
        static public final int CHILDREN = EPresentationAttrInheritance.CHILDREN.getValue().intValue();
        static public final int OBJECT_TITLE_FORMAT = EPresentationAttrInheritance.OBJECT_TITLE_FORMAT.getValue().intValue();
        static public final int PAGES = EPresentationAttrInheritance.PAGES.getValue().intValue();
        static public final int PROPERTY_ATTRIBUTES = EPresentationAttrInheritance.PROPERTY_PRESENTATION_ATTRIBUTES.getValue().intValue();
        static public final int RIGHTS_INHERITANCE_MODE = EPresentationAttrInheritance.RIGHTS_INHERITANCE_MODE.getValue().intValue();
    }
    //
    private RadEntityTitleFormatDef objectTitleFormat = null;
    private List<RadExplorerItemDef> children = null;
    private final EEditorPresentationRightsInheritanceMode rightsInheritMode;
    private final Id inheritRightsFromPresId;
    private final ERuntimeEnvironmentType environmentType;
    private final Map<Id, RadExplorerItemDef> eiById = new HashMap<>();
    private boolean childrenLinked = false;
    private RadEditorPresentationDef basePresentation = null;
    private boolean isBasePresLinked = false;
    private final Collection<RadPropertyPresentationAttributes> propertyAttributes;
    private Map<Id, RadPropertyPresentationAttributes> propertyAttributesById;
    private final Id[] explicitelyInheritedChildren;

    public RadEditorPresentationDef(
            final Id id,
            final String name,
            final ERuntimeEnvironmentType environmentType,
            final Id basePresentationId,
            final int inheritanceMask,
            final RadEntityTitleFormatDef objectTitleFormat,
            final RadExplorerItemDef[] children,
            final Restrictions restrictions,
            final RadPropertyPresentationAttributes[] propertyAttributes,
            final EEditorPresentationRightsInheritanceMode rightsInheritMode,
            final Id inheritRightsFromPresId) {
        this(id, name, environmentType, basePresentationId, inheritanceMask, objectTitleFormat, children, restrictions, propertyAttributes, rightsInheritMode, inheritRightsFromPresId, null);
    }

    /**
     * Constructor
     *
     * @param id
     * @param name
     * @param environmentType
     * @param basePresentationId
     * @param inheritanceMask
     * @param dbuProps virtual columns added in presentation
     * @param objectTitleFormat not null if OBJECT_TITLE_FORMAT is not inherited
     * @param restrictions
     * @param children not null if CHILDREN is not inherited
     * @param propertyAttributes
     * @param rightsInheritMode
     * @param inheritRightsFromPresId
     * @param explicitelyInheritedChildren
     * @param enabledCommandIds not null if ANY_COMMAND restricted
     */
    public RadEditorPresentationDef(
            final Id id,
            final String name,
            final ERuntimeEnvironmentType environmentType,
            final Id basePresentationId,
            final int inheritanceMask,
            final RadEntityTitleFormatDef objectTitleFormat,
            final RadExplorerItemDef[] children,
            final Restrictions restrictions,
            final RadPropertyPresentationAttributes[] propertyAttributes,
            final EEditorPresentationRightsInheritanceMode rightsInheritMode,
            final Id inheritRightsFromPresId,
            final Id[] explicitelyInheritedChildren) {
        super(id, name, basePresentationId, inheritanceMask, null, restrictions);
        if ((this.getInheritanceMask() & CInheritance.OBJECT_TITLE_FORMAT) == 0) {
            this.objectTitleFormat = objectTitleFormat;
        }
        if (children != null) {
            this.children = Collections.unmodifiableList(Arrays.asList(children));
        } else {
            this.children = Collections.emptyList();
        }

        if (propertyAttributes == null) {
            this.propertyAttributes = Collections.emptyList();
        } else {
            this.propertyAttributes = Collections.unmodifiableList(Arrays.asList(propertyAttributes));
        }
        this.environmentType = environmentType;
        this.rightsInheritMode = rightsInheritMode;
        this.inheritRightsFromPresId = inheritRightsFromPresId;
        this.explicitelyInheritedChildren = explicitelyInheritedChildren;
    }

    @Override
    protected void link(final RadClassPresentationDef classPresentation) {
        super.link(classPresentation);
        for (RadExplorerItemDef ei : children) {
            ei.link(classPresentation.getClassDef().getRelease());
        }
    }

    @Override
    public void link() {
        super.link();
        getObjectTitleFormat();
        getChildren();
        getBasePresentation();
        findPropertyPresentationAttributes(null);
    }

    public final RadEntityTitleFormatDef getObjectTitleFormat() {
        if (objectTitleFormat != null) // own or already linked
        {
            return objectTitleFormat;
        }
        final RadEditorPresentationDef basePres = getBasePresentation();
        if (basePres != null && (getInheritanceMask() & CInheritance.OBJECT_TITLE_FORMAT) != 0) {
            //title format is inherited from base presentation
            objectTitleFormat = basePres.getObjectTitleFormat(); // linking for feature
        }
        if (objectTitleFormat == null) {
            objectTitleFormat = getClassPresentation().getDefaultObjectTitleFormat();
        }
        return objectTitleFormat;
    }

    public final ERuntimeEnvironmentType getRuntimeEnvironmentType() {
        return environmentType;
    }

    public final boolean hasCompatibleClientRuntimeEnvironmentType(final ERuntimeEnvironmentType clientEnvironmentType) {
        if (clientEnvironmentType == null || environmentType == ERuntimeEnvironmentType.COMMON_CLIENT) {
            return true;
        } else {
            return environmentType == clientEnvironmentType;
        }
    }

    public final RadExplorerItemDef findChildExplorerItemById(final Id eiId) {
        //XXX: remove synchronization
        synchronized (eiById) {
            RadExplorerItemDef ei = eiById.get(eiId);
            if (ei != null) // already indexed
            {
                return ei;
            }
            // search
            ei = RadExplorerItemDef.findEiById(getChildren(), eiId, new ArrayList<RadExplorerItemDef>());
            if (ei == null) {
                throw new DefinitionNotFoundError(eiId);
            }
            //adding to index
            eiById.put(eiId, ei);
            return ei;
        }
    }

    public final List<RadExplorerItemDef> getChildren() {
        if (childrenLinked) {
            return children;
        }
        final RadEditorPresentationDef basePres = getBasePresentation();
        if (basePres != null && (getInheritanceMask() & CInheritance.CHILDREN) != 0) {
            //children are inherited from base presentation
            if (children == null || children.isEmpty()) {
                children = basePres.getChildren(); // linking for future
            } else {
                final List<RadExplorerItemDef> inherited = basePres.getChildren();//inherited item to be processed
                final ArrayList<RadExplorerItemDef> own = new ArrayList<RadExplorerItemDef>(children.size());
                own.addAll(children); //own item to be processed
                final ArrayList<RadExplorerItemDef> res = new ArrayList<RadExplorerItemDef>(inherited.size());
                RadExplorerItemDef c;
                for (RadExplorerItemDef i : inherited) {
                    c = i;
                    for (RadExplorerItemDef o : children) {
                        if (o.getId().equals(c.getId())) {
                            c = o;
                            own.remove(o);//own item processed
                            break;
                        }
                    }
                    res.add(c);
                }
                res.addAll(own);//add unprocessed own item

                children = Collections.unmodifiableList(res);
            }
        } else {
            if (explicitelyInheritedChildren != null && explicitelyInheritedChildren.length > 0 && basePres != null) {
                children = addInherited();
            }

        }
        childrenLinked = true;
        return children;
    }

    private final List<RadExplorerItemDef> addInherited() {
        List<RadExplorerItemDef> result = null;
        if (explicitelyInheritedChildren != null && explicitelyInheritedChildren.length > 0) {
            nextId:
            for (Id eiId : explicitelyInheritedChildren) {
                RadEditorPresentationDef par = getBasePresentation();
                while (par != null) {
                    for (RadExplorerItemDef item : par.getChildren()) {
                        if (item.getId() == eiId) {
                            if (result == null) {
                                result = new LinkedList<>();
                                result.addAll(children);
                            }
                            result.add(item);
                            continue nextId;
                        }
                    }
                    par = par.getBasePresentation();
                }
            }
        }
        return result == null ? children : result;
    }

    public final Restrictions getTotalRestrictions(final Entity entity) {
        return getTotalRestrictions(entity.getCurUserApplicableRoleIds());
    }

    @Override
    protected RadEditorPresentationDef getBasePresentation() {
        if (!isBasePresLinked) {
            if (getBasePresentationId() == null) {
                basePresentation = null;
            } else {
                basePresentation = getClassPresentation().getEditorPresentationById(getBasePresentationId());
            }
            isBasePresLinked = true;
        }
        return basePresentation;
    }

    public final boolean isPropertyForbidden(final Id propertyId) {
        final RadPropertyPresentationAttributes attributes = findPropertyPresentationAttributes(propertyId);
        if (attributes != null && attributes.isPresentable() != null) {
            return !attributes.isPresentable().booleanValue();
        }
        if ((this.getInheritanceMask() & CInheritance.PROPERTY_ATTRIBUTES) == 0 || getBasePresentationId() == null) {
            return false;
        } else {
            return getBasePresentation().isPropertyForbidden(propertyId);
        }
    }

    @Override
    public final EEditPossibility getPropEditPossibilityByPropId(final RadClassPresentationDef classPres, final Id propertyId) {
        final RadPropertyPresentationAttributes attributes = findPropertyPresentationAttributes(propertyId);
        if (attributes != null && attributes.getEditPossibility() != null) {
            return attributes.getEditPossibility();
        }
        if ((this.getInheritanceMask() & CInheritance.PROPERTY_ATTRIBUTES) == 0 || getBasePresentationId() == null) {
            return super.getPropEditPossibilityByPropId(classPres, propertyId);
        } else {
            return getBasePresentation().getPropEditPossibilityByPropId(classPres, propertyId);
        }
    }

    private RadPropertyPresentationAttributes findPropertyPresentationAttributes(final Id propertyId) {
        if (propertyAttributesById == null) {
            propertyAttributesById = new HashMap<>(16);
            RadPropertyPresentationAttributes result = null;
            for (RadPropertyPresentationAttributes attributes : propertyAttributes) {
                propertyAttributesById.put(attributes.getPropertyId(), attributes);
                if (propertyId.equals(attributes.getPropertyId())) {
                    result = attributes;
                }
            }
            return result;
        }
        return propertyAttributesById.get(propertyId);
    }

    @Override
    public Collection<RadPropDef> getUsedPropDefs(final RadClassPresentationDef classPres) {
        final Collection<RadPropDef> classProps = classPres.getClassDef().getProps();
        final Collection<RadPropDef> usedPropDefs = new ArrayList<>(classProps.size());
        for (RadPropDef p : classProps) {
            if (!isPropertyForbidden(p.getId())
                    && EasValueConverter.SUPPORTED_VAL_TYPES.contains(p.getValType()) && //RADIX-3252
                    (classPres.getPropPresById(p.getId()) != null) //RADIX-3163
                    ) {
                usedPropDefs.add(p);
            }
        }
        return Collections.unmodifiableCollection(usedPropDefs);
    }

    private Id getInheritRightsFromPresId() {
        if ((this.getInheritanceMask() & CInheritance.RIGHTS_INHERITANCE_MODE) != 0) {
            if (getBasePresentationId() == null) {
                return null;
            }
            return getBasePresentation().getInheritRightsFromPresId();
        } else {
            switch (rightsInheritMode) {
                case FROM_REPLACED:
                    return getClassPresentation().getReplacedPresentationId(getId());
                case FROM_DEFINED:
                    return inheritRightsFromPresId;
                default:
                    return null;
            }
        }
    }

    @Override
    protected Restrictions calcRoleRestr(final RadRoleDef role) {
        final String resHashKey = RadRoleDef.generateResHashKey(EDrcResourceType.EDITOR_PRESENTATION, getClassPresentation().getId(), getId());
        List<String> inheritFromResHashKeys = null;
        Id inheritFromPresId = getInheritRightsFromPresId();
        if (inheritFromPresId != null) {
            inheritFromResHashKeys = new ArrayList<>();
            int i = 0;
            do {
                if (i++ > MAX_REPLACE_ITER_COUNT) {
                    throw new IllegalUsageError("Editor presentations replacement operation count limit(" + String.valueOf(MAX_REPLACE_ITER_COUNT) + ") is exceeded.");
                }
                final RadEditorPresentationDef inheritFromPres = getClassPresentation().getEditorPresentationById(inheritFromPresId);
                inheritFromResHashKeys.add(RadRoleDef.generateResHashKey(EDrcResourceType.EDITOR_PRESENTATION, inheritFromPres.getClassPresentation().getId(), inheritFromPres.getId()));
                inheritFromPresId = inheritFromPres.getInheritRightsFromPresId();
            } while (inheritFromPresId != null);
        }
        return role.getResourceRestrictions(resHashKey, inheritFromResHashKeys);
    }
}
