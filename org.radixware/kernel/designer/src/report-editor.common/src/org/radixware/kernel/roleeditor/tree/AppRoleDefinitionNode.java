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

package org.radixware.kernel.roleeditor.tree;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import javax.swing.Action;
import org.openide.util.actions.SystemAction;
import org.radixware.kernel.common.defs.ads.rights.AdsRoleDef;
import org.radixware.kernel.common.userreport.repository.role.AppRole;
import org.radixware.kernel.designer.tree.ads.nodes.defs.AdsRoleNode;


public class AppRoleDefinitionNode extends AdsRoleNode {

    private final AppRole appRole;
    private final DeleteAppRoleAction.Cookie deleteCookie;

    public AppRoleDefinitionNode(final AppRole appRole,final AdsRoleDef descriptor) {
        super(descriptor);
        this.appRole = appRole;
        deleteCookie = new DeleteAppRoleAction.Cookie(appRole);
        if (!appRole.isReadOnly()) {
            addCookie(deleteCookie);
        }
       
        appRole.addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(final PropertyChangeEvent evt) {
                if (appRole.isReadOnly()) {
                    removeCookie(deleteCookie);
                } else {
                    addCookie(deleteCookie);
                }
            }
        });
    }

    @Override
    public void addCustomActions(final List<Action> actions) {
        super.addCustomActions(actions);
        actions.add(SystemAction.get(DeleteAppRoleAction.class));
    }
}
