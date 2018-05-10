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
package org.radixware.kernel.server.meta.roles;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.radixware.kernel.common.defs.dds.DdsAccessPartitionFamilyDef;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;

import org.radixware.kernel.server.types.Restrictions;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.enums.EDrcPredefinedRoleId;
import org.radixware.kernel.common.enums.EDrcResourceType;
import org.radixware.kernel.common.enums.EDrcServerResource;
import org.radixware.kernel.server.arte.Release;
import org.radixware.kernel.common.meta.RadTitledDef;
import org.radixware.kernel.common.types.MultilingualString;
import org.radixware.kernel.server.arte.Arte;

public final class RadRoleDef extends RadTitledDef {

    private boolean isAbstract;
    private boolean isDeprecated;
    private final boolean isVirtualAppRole;
    private final boolean isInvalid;
    private Release release;
    private final Map<String, Restrictions> resourceRestrictions;
    private List<RadRoleDef> ancestors = null;
    private List<Id> families = null;
    private final Id[] ancestorIds;
    private final Id[] apFamilyIds;
    private final String layerUri;
    private static final String HASH_SEP = "~";
    private Id descriptionId = null;

    public RadRoleDef(
            final Arte arte,
            final Id id,
            final String name,
            final String description,
            final Id titleId,
            final Id[] ancestorIds,
            final Id[] apFamilyIds,
            final RadRoleResource[] resources,
            final boolean isAbstract,
            final boolean isDeprecated) {
        this(arte, id, name, description, titleId, ancestorIds, apFamilyIds, resources, isAbstract, isDeprecated, false);
    }

    public RadRoleDef(
            final Release release,
            final Id id,
            final String name,
            final String description,
            final Id titleId,
            final Id[] ancestorIds,
            final Id[] apFamilyIds,
            final RadRoleResource[] resources,
            final boolean isAbstract,
            final boolean isDeprecated) {
        this(release, id, name, description, titleId, ancestorIds, apFamilyIds, resources, isAbstract, isDeprecated, false, false);
    }
    
    public RadRoleDef(
            final Release release,
            final Id id,
            final String name,
            final String description,
            final Id descriptionId,
            final Id titleId,
            final Id[] ancestorIds,
            final Id[] apFamilyIds,
            final RadRoleResource[] resources,
            final boolean isAbstract,
            final boolean isDeprecated) {
        this(release, id, name, description, titleId, ancestorIds, apFamilyIds, resources, isAbstract, isDeprecated);
        this.descriptionId = descriptionId;
    }

    public RadRoleDef(
            final Arte arte,
            final Id id,
            final String name,
            final String description,
            final Id titleId,
            final Id[] ancestorIds,
            final Id[] apFamilyIds,
            final RadRoleResource[] resources,
            final boolean isAbstract,
            final boolean isDeprecated,
            final boolean isVirtualAppRole) {
        this(arte.getDefManager().getReleaseCache().getRelease(), id, name, description, titleId, ancestorIds, apFamilyIds, resources, isAbstract, isDeprecated, isVirtualAppRole, false);
    }

    public RadRoleDef(
            final Arte arte,
            final Id id,
            final String name,
            final String description,
            final Id titleId,
            final Id[] ancestorIds,
            final Id[] apFamilyIds,
            final RadRoleResource[] resources,
            final boolean isAbstract,
            final boolean isDeprecated,
            final boolean isVirtualAppRole,
            final boolean isInvalid
    ) {
        this(arte.getDefManager().getReleaseCache().getRelease(), id, name, description, titleId, ancestorIds, apFamilyIds, resources, isAbstract, isDeprecated, isVirtualAppRole, isInvalid);
    }

    public RadRoleDef(
            final Release release,
            final Id id,
            final String name,
            final String description,
            final Id titleId,
            final Id[] ancestorIds,
            final Id[] apFamilyIds,
            final RadRoleResource[] resources,
            final boolean isAbstract,
            final boolean isDeprecated,
            final boolean isVirtualAppRole,
            final boolean isInvalid
    ) {
        this(release, id, name, description, titleId, ancestorIds, apFamilyIds, resources, isAbstract, isDeprecated, isVirtualAppRole, isInvalid, null);
    }

    public RadRoleDef(
            final Release release,
            final Id id,
            final String name,
            final String description,
            final Id titleId,
            final Id[] ancestorIds,
            final Id[] apFamilyIds,
            final RadRoleResource[] resources,
            final boolean isAbstract,
            final boolean isDeprecated,
            final boolean isVirtualAppRole,
            final boolean isInvalid,
            final String layerUri
    ) {
        super(id, name, id, titleId);
        this.setDescription(description);
        this.isAbstract = isAbstract;
        this.isDeprecated = isDeprecated;
        this.isVirtualAppRole = isVirtualAppRole;
        this.isInvalid = isInvalid;
        this.ancestorIds = ancestorIds == null ? null : Arrays.copyOf(ancestorIds, ancestorIds.length);
        this.apFamilyIds = apFamilyIds == null ? null : Arrays.copyOf(apFamilyIds, apFamilyIds.length);
        this.layerUri = layerUri;
        resourceRestrictions = new HashMap<>();

        if (resources != null) {
            for (RadRoleResource res : resources) {
                resourceRestrictions.put(generateResHashKey(res), res.getRestrictions());
            }
        }
        link(release);
    }

    @Override
    public void link() {
        super.link();
        getAPFamilies();//force loading
        getAncestors();//force loading
    }

    public boolean isAbstract() {
        return isAbstract;
    }

    @Override
    public boolean isDeprecated() {
        return isDeprecated;
    }

    public boolean isInvalid() {
        return isInvalid;
    }

    public final String getTitle() {
        return getTitle(Arte.get());//could be app role, release.getVirtualReleaseEnv could be insufficient
    }

    public String getLayerUri() {
        return layerUri;
    }

    @Override
    public String getName() {
        final Arte contextArte = Arte.get();
        if (EDefinitionIdPrefix.APPLICATION_ROLE == getId().getPrefix() && contextArte != null) {
            return contextArte.getRights().getRoleTitleById(getId());
        }
        return super.getName();
    }

    private void link(final Release release) {
        this.release = release;
    }

    public final Restrictions getResourceRestrictions(
            final EDrcResourceType type,
            final Id defId,
            final Id subDefId) {
        return getResourceRestrictions(generateResHashKey(type, defId, subDefId));
    }

    public final Restrictions getResourceRestrictions(final String resHashKey) {
        return getResourceRestrictions(resHashKey, null);
    }

    public final Restrictions getResourceRestrictions(final String resHashKey, final List<String> inheritFromResHashKeys) {
        if (getId().toString().equals(EDrcPredefinedRoleId.SUPER_ADMIN.getValue())) {
            return Restrictions.ZERO;
        }
        Restrictions restr = resourceRestrictions.get(resHashKey);
        if (restr == null && inheritFromResHashKeys != null) {
            for (String hashKey : inheritFromResHashKeys) {
                restr = resourceRestrictions.get(hashKey);
                if (restr != null) {
                    break;
                }
            }
        }
        if (restr != null) {//has own restriction (defined in this role)
            if (restr.getIsAccessRestricted()) {
                return Restrictions.FULL; // access restricted in this role
            } else {
                return restr;
            }
        }
        //calculation of total inherited restriction
        Restrictions inheritedRestr = Restrictions.FULL;
        for (RadRoleDef ancestor : getAncestors()) {
            final Restrictions ancRestr = ancestor.getResourceRestrictions(resHashKey, inheritFromResHashKeys);
            inheritedRestr = Restrictions.Factory.and(ancRestr, inheritedRestr);
        }
        return inheritedRestr;
    }

    public List<RadRoleDef> getAncestors() {
        if (ancestors == null) {
            if (ancestorIds != null && ancestorIds.length != 0) {
                ancestors = new ArrayList<>(ancestorIds.length);
                for (Id ancestorId : ancestorIds) {
                    ancestors.add(findRoleDef(ancestorId));
                }
                ancestors = Collections.unmodifiableList(ancestors);
            } else {
                ancestors = Collections.emptyList();
            }
        }
        return ancestors;
    }

    private RadRoleDef findRoleDef(final Id roleId) {
        if (roleId.getPrefix() == EDefinitionIdPrefix.APPLICATION_ROLE) {
            return Arte.get().getDefManager().getRoleDef(roleId);
        } else {
            return release.getRoleDef(roleId);
        }
    }

    public List<Id> getAPFamilies() {
        if (isVirtualAppRole) {
            final List<Id> result = new ArrayList<>();
            for (DdsAccessPartitionFamilyDef apf : release.getAccessPartitionFamilyDefs()) {
                result.add(apf.getId());
            }
            return result;
        } else {
            if (families == null) {
                if (apFamilyIds != null && apFamilyIds.length != 0) {
                    families = new ArrayList<>(apFamilyIds.length);
                    families.addAll(Arrays.asList(apFamilyIds));
                    families = Collections.unmodifiableList(families);
                } else {
                    families = Collections.emptyList();
                }
            }
            return families;
        }
    }

    public static String generateResHashKey(final RadRoleResource res) {
        return generateResHashKey(res.getType(), res.getDefId(), res.getSubDefId());
    }

    public static String generateResHashKey(
            final EDrcResourceType type,
            final Id defId,
            final Id subDefId) {
        return generateResHashKey(type, defId != null ? defId.toString() : null, subDefId != null ? subDefId.toString() : null);
    }

    public static String generateResHashKey(
            final EDrcResourceType type,
            final EDrcServerResource res) {
        return generateResHashKey(type, res.getValue(), null);
    }

    private static String generateResHashKey(
            final EDrcResourceType type,
            final String defId,
            final String subDefId) {
        final StringBuilder hash = new StringBuilder(type.toString());
        hash.append(HASH_SEP);
        if (defId != null) {
            hash.append(defId);
        }
        hash.append(HASH_SEP);
        if (subDefId != null) {
            hash.append(subDefId);
        }
        return hash.toString();
    }

    @Override
    public String getDescription() {
        String result = MultilingualString.get(Arte.get(), getId(), descriptionId);                
        
        return result == null ? super.getDescription() : result;
    }

    @Override
    public Id getDescriptionId() {
        return descriptionId;
    }
}
