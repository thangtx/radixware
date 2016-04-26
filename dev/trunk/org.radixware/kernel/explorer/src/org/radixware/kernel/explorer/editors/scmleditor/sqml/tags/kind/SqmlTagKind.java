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

package org.radixware.kernel.explorer.editors.scmleditor.sqml.tags.kind;

import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.types.Icon;
import org.radixware.kernel.explorer.editors.sqmleditor.SqmlEditor;
import org.radixware.kernel.explorer.editors.scml.IScml;
import org.radixware.kernel.explorer.editors.scml.IScmlTagKind;
import org.radixware.kernel.explorer.editors.scml.ScmlTag;


public class SqmlTagKind implements IScmlTagKind{
    protected final IClientEnvironment environment;
    protected final SqmlEditor parent;
    
    public SqmlTagKind(final IClientEnvironment environment,SqmlEditor parent){
        this.environment=environment;
        this.parent=parent;
    }

    @Override
    public ScmlTag createTag(IScml context) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getDescription() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Icon getIcon() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ScmlTag loadFromXml(IScml context, XmlObject xml) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
