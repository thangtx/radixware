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
import org.radixware.kernel.common.defs.ads.msdl.AdsMsdlSchemeDef;
import org.radixware.kernel.common.userreport.repository.msdl.MsdlScheme;
import org.radixware.kernel.common.userreport.repository.msdl.MsdlSchemes;
import org.radixware.kernel.common.userreport.repository.msdl.MsdlSchemesModuleRepository;


public interface IMsdlSchemeManager {    
     void save(final File runtimeFile,final AdsMsdlSchemeDef msdlScheme);
     boolean delete(final MsdlScheme msdlScheme);
     void listDefinitions(final MsdlSchemesModuleRepository repository);
     File uploadMsdlScheme(final MsdlScheme msdlScheme, final MsdlSchemesModuleRepository repository);
     MsdlScheme create();
     IUserDefChangeSuppert createMsdlChangeSuppert(MsdlSchemes msdlSchemes);
}
