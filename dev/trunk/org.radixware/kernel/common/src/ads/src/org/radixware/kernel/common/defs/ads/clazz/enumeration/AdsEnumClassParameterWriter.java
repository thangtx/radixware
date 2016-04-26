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

/*
 * 9/27/11 12:01 PM
 */
package org.radixware.kernel.common.defs.ads.clazz.enumeration;

import org.radixware.kernel.common.defs.ads.src.AbstractDefinitionWriter;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport.UsagePurpose;
import org.radixware.kernel.common.scml.CodePrinter;


public class AdsEnumClassParameterWriter extends AbstractDefinitionWriter<AdsFieldParameterDef> {
    
    public AdsEnumClassParameterWriter(JavaSourceSupport support, AdsFieldParameterDef field, UsagePurpose usagePurpose) {
        super(support, field, usagePurpose);
    }
    
    @Override
    public boolean writeExecutable(CodePrinter printer) {
        return true;
    }
}
