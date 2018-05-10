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

package org.radixware.kernel.common.meta;

import java.io.IOException;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.enums.EDocGroup;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.types.Id;


public class RadDefinition extends Definition {

    public RadDefinition(Id id, String name) {
        super(id, name);
    }

    public RadDefinition(Id id) {
        super(id);
    }

    @Override
    public void save() throws IOException {
        throw new UnsupportedOperationException("\"Save\" operation is not supported by runtime application metainformation");
    }

    //force all lazy loading before shared meta will be published for ARTE instances
    public void link() {
    }

    @Override
    public ERuntimeEnvironmentType getDocEnvironment() {
        return null;
    }

    @Override
    public EDocGroup getDocGroup() {
        return EDocGroup.NONE;
    }
}
