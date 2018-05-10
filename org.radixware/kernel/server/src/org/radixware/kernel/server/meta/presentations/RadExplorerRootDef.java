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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.radixware.kernel.common.enums.EDrcResourceType;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.server.arte.Arte;
import org.radixware.kernel.server.arte.Release;
import org.radixware.kernel.server.meta.roles.RadRoleDef;
import org.radixware.kernel.server.types.Restrictions;

public class RadExplorerRootDef extends RadParagraphExplorerItemDef {

    public RadExplorerRootDef(
            final Arte arte,
            final Id id,
            final Id titleId,
            final String layerUri,
            final RadExplorerItemDef[] children,
            final boolean isHidden,
            final RadParagraphExplorerItemDef base) {
        this(arte.getDefManager().getReleaseCache().getRelease(), id, titleId, layerUri, children, isHidden, base);
    }

    public RadExplorerRootDef(
            final Release release,
            final Id id,
            final Id titleId,
            final String layerUri,
            final RadExplorerItemDef[] children,
            final boolean isHidden,
            final RadParagraphExplorerItemDef base) {
        super(release, id, id, titleId, layerUri, children, isHidden, base);
    }

    public RadExplorerRootDef(
            final Arte arte,
            final Id id,
            final Id titleId,
            final String layerUri,
            final RadExplorerItemDef[] children,
            final boolean isHidden,
            final RadParagraphExplorerItemDef base,
            final Id[] explicitEis) {
        this(arte.getDefManager().getReleaseCache().getRelease(), id, titleId, layerUri, children, isHidden, base, explicitEis);
    }

    public RadExplorerRootDef(
            final Release release,
            final Id id,
            final Id titleId,
            final String layerUri,
            final RadExplorerItemDef[] children,
            final boolean isHidden,
            final RadParagraphExplorerItemDef base,
            final Id[] explicitEis) {
        super(release, id, id, titleId, layerUri, children, isHidden, base, explicitEis);
    }

//  DRC
    public boolean getCurUserCanAccess() {
        return Arte.get().getRights().getCurUserHasRole(getRoleIdsStr());
    }

    public boolean getCurUserCanAccessItemById(final Id childExplrItemId) {
        return Arte.get().getRights().getCurUserHasRole(getItemRoleIdsStrById(childExplrItemId));
    }

    private String getItemRoleIdsStrById(final Id childExplrItemId) {
        final Map<Id, String> childRoleListById = new HashMap<>();
        final StringBuilder lst = new StringBuilder();
        final Collection<RadRoleDef> roles = Arte.get().getDefManager().getRoleDefs();

        if (!roles.isEmpty()) {
            final String rootRoleList = getRoleIdsStr();
            final String resHashKey = RadRoleDef.generateResHashKey(EDrcResourceType.EXPLORER_ROOT_ITEM, getId(), childExplrItemId);
            Restrictions restr;
            for (RadRoleDef role : roles) {
                restr = role.getResourceRestrictions(resHashKey);
                final String roleIdAsString = role.getId().toString();
                if (!restr.getIsAccessRestricted() && rootRoleList.contains(roleIdAsString)) {
                    if (lst.length() > 0) {
                        lst.append(',');
                    }
                    lst.append(roleIdAsString);//RADIX-12101
                }
            }
        }

        final String itemsRoleList = lst.toString();
        childRoleListById.put(childExplrItemId, itemsRoleList);
        return itemsRoleList;
    }
    //cache is disabled because of application roles
    //private String roleList = null;

    private final String getRoleIdsStr() {
        //  if (roleList == null) {
            /*
         * predefined roles refactoring final StringBuilder lst = new
         * StringBuilder(CDrcPredefinedRoleID.SUPER_ADMIN.toString());
         */
        final StringBuilder lst = new StringBuilder();
        final Collection<RadRoleDef> roles = Arte.get().getDefManager().getRoleDefs();
        if (!roles.isEmpty()) {
            final String resHashKey = RadRoleDef.generateResHashKey(EDrcResourceType.EXPLORER_ROOT_ITEM, getId(), null);
            Restrictions restr;
            for (RadRoleDef role : roles) {
                if (role != null) {
                    try {
                    restr = role.getResourceRestrictions(resHashKey);
                    if (!restr.getIsAccessRestricted()) {
                        lst.append(',');
                        lst.append(role.getId());
                    }
                    } catch (Exception ex) {
                        Arte.get().getTrace().put(EEventSeverity.WARNING, "Exception while checking role '#" + role.getId() + " " + role.getName() + "' access to explorer root item: " + ExceptionTextFormatter.throwableToString(ex), EEventSource.EAS);
                    }
                }
            }
        }
        return lst.toString();
        //roleList = lst.toString();
//        }
//        return roleList;
    }
}
