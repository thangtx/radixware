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

package org.radixware.kernel.common.client.env;

import java.util.Map;
import org.radixware.kernel.common.client.IClientEnvironment;

public class EntryPointRequest {
    
    private final IClientEnvironment environment;
    private final String entryPointName;
    private final Map<String, String> parameters;
    
    public EntryPointRequest(IClientEnvironment environment, String entryPointName, Map<String, String> parameters) {
        this.environment = environment;
        this.entryPointName = entryPointName;
        this.parameters = parameters;
    }
    
    public IClientEnvironment getEnvironment() {
        return environment;
    }

    public String getEntryPointName() {
        return entryPointName;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }
}
