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
import org.radixware.kernel.common.client.meta.sqml.ISqmlColumnDef;
import org.radixware.kernel.common.client.types.Icon;
import org.radixware.kernel.explorer.editors.sqmleditor.SqmlEditor;
import org.radixware.kernel.explorer.editors.scml.IScml;
import org.radixware.kernel.explorer.editors.scml.ScmlTag;
import org.radixware.kernel.explorer.editors.scmleditor.sqml.tags.SqmlTag_PropSqlName;
import org.radixware.schemas.xscml.Sqml.Item.PropSqlName;
import org.radixware.schemas.xscml.Sqml.Item.PropSqlName.Owner;


public class SqmlTagPropSqlNameKind extends SqmlTagKind{
    
    public SqmlTagPropSqlNameKind(final IClientEnvironment environment,SqmlEditor parent){
        super(environment,parent);
    }

    @Override
    public ScmlTag createTag(IScml context) {
         ISqmlColumnDef column=parent.getSqmlProcessor().chooseSqmlColumn(null, null, parent.isReadonly(), null);
         return new SqmlTag_PropSqlName( environment,  column, Owner.TABLE, null);
    }

    @Override
    public String getDescription() {
        return environment.getMessageProvider().translate("SqmlEditor", "Insert Column");
    }

    @Override
    public Icon getIcon() {
        //icon = ExplorerIcon.getQIcon(SqmlEditor.SqmlEditorIcons.IMG_INSERT_COLUMN)
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ScmlTag loadFromXml(IScml context, XmlObject xml) {
        PropSqlName propSqlName=(PropSqlName)xml;
        return new SqmlTag_PropSqlName( environment,  propSqlName);
    }
}
