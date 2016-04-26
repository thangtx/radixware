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

package org.radixware.kernel.common.repository.ads.fs;

import org.radixware.kernel.common.repository.fs.IJarDataProvider;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;


public interface IKernelLookup {

    public String[] listModules(ERuntimeEnvironmentType env);

    public IJarDataProvider[] listJars(String moduleName, ERuntimeEnvironmentType env);

    public IJarDataProvider[] listAllJars(ERuntimeEnvironmentType env);       
}
