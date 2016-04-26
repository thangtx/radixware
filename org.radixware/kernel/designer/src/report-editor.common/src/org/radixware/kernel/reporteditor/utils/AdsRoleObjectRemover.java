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

package org.radixware.kernel.reporteditor.utils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.netbeans.api.progress.ProgressUtils;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProviderFactory;
import org.radixware.kernel.common.defs.ads.rights.AdsRoleDef;
import org.radixware.kernel.common.userreport.repository.role.AppRole;
import org.radixware.kernel.designer.common.annotations.registrators.ObjectRemoverFactoryRegistration;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.designer.common.general.nodes.operation.IObjectRemoverFactory;
import org.radixware.kernel.designer.common.general.nodes.operation.ObjectRemover;
import org.radixware.kernel.reporteditor.common.UserExtensionManager;


public class AdsRoleObjectRemover extends ObjectRemover {

    private final AdsRoleDef role;

    public AdsRoleObjectRemover(AdsRoleDef prop) {
        this.role = prop;
    }

    @Override
    public void removeObject() {
        ProgressUtils.showProgressDialogAndRun(new Runnable() {
            @Override
            public void run() {
                List<AppRole> users = new LinkedList<>();

                List<AppRole> reports = UserExtensionManager.getInstance().getAppRoles().getRoles();
                AppRole roleToRemove = null;
                for (AppRole r : reports) {

                    AdsRoleDef report = r.findRoleDefinition();
                    if (report == role) {
                        roleToRemove = r;
                    }
                    final List<Definition> deps = new ArrayList<>();

                    if (report != null && report != role && !report.isParentOf(role)) {
                        deps.clear();
                        report.visit(new IVisitor() {
                            @Override
                            public void accept(RadixObject radixObject) {
                                radixObject.collectDirectDependences(deps);
                            }
                        }, VisitorProviderFactory.createDefaultVisitorProvider());


                        if (deps.contains(role)) {
                            users.add(r);
                        }
                    }
                }
                if (!users.isEmpty()) {
                    StringBuilder message = new StringBuilder("Application role  " + role.getName() + " is used by following application roles:\n");
                    for (AppRole report : users) {
                        message.append("- ").append(report.getName()).append("\n");
                    }
                    message.append("Continue?");
                    if (!DialogUtils.messageConfirmation(message.toString())) {
                        return;
                    }

                }
                if (role.isInBranch() && role.canDelete()) {
                    role.delete();
                }
                if (roleToRemove != null) {
                    roleToRemove.delete();
                };

            }
        }, "Delete Report Parameter");


    }

    @ObjectRemoverFactoryRegistration
    public static final class Factory implements IObjectRemoverFactory<AdsRoleDef> {

        @Override
        public ObjectRemover newInstance(AdsRoleDef obj) {
            return new AdsRoleObjectRemover(obj);
        }
    }
}
