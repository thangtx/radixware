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

package org.radixware.kernel.explorer.editors.sqmleditor.sqmltags;

import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.explorer.editors.xscmleditor.TagInfo;


public abstract class SqmlTag extends TagInfo {

    public SqmlTag(final IClientEnvironment environment,final long pos,final boolean isDeprecated) {
        super(environment, pos, isDeprecated);
    }
    
    public SqmlTag(final IClientEnvironment environment,final long pos) {
        super(environment, pos,false);
    }

    public SqmlTag(final IClientEnvironment environment,final SqmlTag tag) {
        super(environment, tag);
    }
    
    public abstract void addTagToSqml(XmlObject itemTag);
}