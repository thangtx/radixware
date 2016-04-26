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
import org.radixware.kernel.common.client.meta.sqml.ISqmlEnumItem;
import org.radixware.kernel.common.client.types.Icon;
import org.radixware.kernel.explorer.editors.sqmleditor.SqmlEditor;
import org.radixware.kernel.explorer.editors.scml.IScml;
import org.radixware.kernel.explorer.editors.scml.ScmlTag;
import org.radixware.kernel.explorer.editors.scmleditor.sqml.tags.SqmlTag_ConstValue;
import org.radixware.schemas.xscml.Sqml.Item.ConstValue;


public class SqmlTagConstValueKind extends SqmlTagKind{
    
    public SqmlTagConstValueKind(final IClientEnvironment environment,SqmlEditor parent){
        super(environment,parent);
    }

    @Override
    public ScmlTag createTag(IScml context) {
         ISqmlEnumItem enumItem=parent.getSqmlProcessor().chooseEnumItem(parent.isReadonly(), null);
         return  new SqmlTag_ConstValue( environment,  enumItem);
    }

    @Override
    public String getDescription() {
        return environment.getMessageProvider().translate("SqmlEditor", "Insert Constant");
    }

    @Override
    public Icon getIcon() {
        //icon = ExplorerIcon.getQIcon(SqmlEditor.SqmlEditorIcons.Definitions.CONSTSET);
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ScmlTag loadFromXml(IScml context, XmlObject xml) {
        ConstValue constValue=(ConstValue)xml;
        return new SqmlTag_ConstValue( environment,  constValue);
    }
    
}
