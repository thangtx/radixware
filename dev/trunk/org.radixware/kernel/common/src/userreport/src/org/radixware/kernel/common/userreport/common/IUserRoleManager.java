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

package org.radixware.kernel.common.userreport.common;

import java.io.File;
import org.radixware.kernel.common.defs.ads.rights.AdsRoleDef;
import org.radixware.kernel.common.repository.ads.fs.IRepositoryAdsDefinition;
import org.radixware.kernel.common.userreport.repository.role.AppRole;
import org.radixware.kernel.common.userreport.repository.role.AppRoles;
import org.radixware.kernel.common.userreport.repository.role.RolesModuleRepository;


public interface IUserRoleManager {
    
    //public void removeRolesChangeListener(ChangeListener listener);
    //public boolean hasRolesListeners();
    //public void addRolesChangeListener(ChangeListener listener);    
    //public void fireRolesChange();    
    void saveImpl(final File runtimeFile,final AdsRoleDef role);
    AppRole create();
    boolean delete(final AppRole role);
    IRepositoryAdsDefinition[] listDefinitions(final RolesModuleRepository repository);
    File uploadRole(final AppRole role,final RolesModuleRepository repository);
    IUserDefChangeSuppert createAppRoleChangeSuppert(AppRoles appRoles);
}
