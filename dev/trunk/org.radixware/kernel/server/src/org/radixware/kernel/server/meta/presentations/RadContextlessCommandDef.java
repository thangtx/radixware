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

import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.enums.ECommandNature;
import org.radixware.kernel.common.enums.EDrcResourceType;
import org.radixware.kernel.server.arte.Arte;
import org.radixware.kernel.server.arte.Release;
import org.radixware.kernel.common.meta.RadDefinition;
import org.radixware.kernel.server.meta.roles.RadRoleDef;
import org.radixware.kernel.server.types.Restrictions;

public final class RadContextlessCommandDef extends RadDefinition {

    private final IRadContextlessCommandExecutor cmdExecutor;
    private final ECommandNature nature;
    private final boolean traceGuiActivity;

    /*
     * public TDbuContextlessCommandDef(String CODE_ASSISTANCE, String string,
     * CDbuCommandNature XML_IN_OUT, CalculteComplCommand calculteComplCommand)
     * { throw new UnsupportedOperationException("Not yet implemented");
	}
     */
//Constructor
    public RadContextlessCommandDef(
            final Arte arte,
            final Id id,
            final String name,
            final ECommandNature nature,
            final IRadContextlessCommandExecutor cmdExecutor) {
        this(arte.getDefManager().getReleaseCache().getRelease(), id, name, nature, cmdExecutor, true);
    }
    
    public RadContextlessCommandDef(
            final Release release,
            final Id id,
            final String name,
            final ECommandNature nature,
            final IRadContextlessCommandExecutor cmdExecutor) {
        this(release, id, name, nature, cmdExecutor, true);
    }
    
    public RadContextlessCommandDef(
            final Release release,
            final Id id,
            final String name,
            final ECommandNature nature,
            final IRadContextlessCommandExecutor cmdExecutor, 
            boolean traceGuiActivity) {
        super(id, name);
        this.nature = nature;
        this.cmdExecutor = cmdExecutor;
        this.traceGuiActivity = traceGuiActivity;
        link(release);
    }
    
    
    private Release release = null;

    private void link(final Release release) {
        this.release = release;
    }

    //roles
    public boolean getCurUserCanAccess(final Arte arte) {
        return arte.getRights().getCurUserHasRole(getRoleIdsStr(arte));
    }
    //no cache because of application roles
    //private String roleList = null;

    private final String getRoleIdsStr(final Arte arte) {
        //	if (roleList == null) {
			/*
         * predefined roles refactoring final StringBuilder lst = new
         * StringBuilder(CDrcPredefinedRoleID.SUPER_ADMIN.toString());
         */
        final StringBuilder lst = new StringBuilder();
        final Collection<RadRoleDef> roles = arte.getDefManager().getRoleDefs();
        if (!roles.isEmpty()) {
            final String resHashKey = RadRoleDef.generateResHashKey(EDrcResourceType.CONTEXTLESS_COMMAND, getId(), null);
            Restrictions restr;
            for (RadRoleDef role : roles) {
                restr = role.getResourceRestrictions(resHashKey);
                if (!restr.getIsAccessRestricted()) {
                    lst.append(',');
                    lst.append(role.getId().toString());
                }
            }
        }
        return lst.toString();
//	//		roleList = lst.toString();
//		}
//		return roleList;
    }

    /**
     * @return the cmdExecutor
     */
    public IRadContextlessCommandExecutor getCmdExecutor() {
        return cmdExecutor;
    }

    /**
     * @return the nature
     */
    public ECommandNature getNature() {
        return nature;
    }

    public boolean getUsrActionsIsTraced() {
        return traceGuiActivity;
    }
}
