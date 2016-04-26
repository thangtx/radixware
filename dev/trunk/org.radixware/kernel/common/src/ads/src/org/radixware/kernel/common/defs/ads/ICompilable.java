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

package org.radixware.kernel.common.defs.ads;

import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.enums.EDefType;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.types.Id;


public interface ICompilable {

    Module getModule();

    Layer getLayer();

    Id getId();

    String getQualifiedName();
    
    EDefType getDefinitionType();
    
    Definition getDefinition();
}
