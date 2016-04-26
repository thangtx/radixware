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

package org.radixware.kernel.explorer.editors.jmleditor.jmltags;

import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.EDefinitionDisplayMode;
import org.radixware.kernel.common.jml.Jml;
import org.radixware.kernel.common.scml.Scml;
import org.radixware.kernel.explorer.editors.xscmleditor.TagInfo;
import org.radixware.schemas.xscml.JmlType;


public class JmlTag extends TagInfo {

    protected Scml.Tag tag;

    public JmlTag(final IClientEnvironment environment, final long pos,final boolean isDeprecated/*,String path*/) {
        super(environment, pos,isDeprecated/*,path*/);
    }

    public JmlTag(final IClientEnvironment environment, final JmlTag tag) {
        super(environment, tag); 
    }
    
    public Scml.Tag getTag() {
        return tag;
    }

    @Override
    public TagInfo copy() {
        return new JmlTag(environment, this);
    }
    
    @Override
    public TagInfo copyWithScmlItem() {
        final JmlTag jmlItem=new JmlTag(environment, this);
        if(tag!=null){
            final Jml.Tag tagId=(Jml.Tag)tag;
            final JmlType.Item item=JmlType.Item.Factory.newInstance();
            tagId.appendTo(item);
            jmlItem.tag = Jml.Tag.Factory.loadFrom(item);
        }
        return jmlItem;
    }

    @Override
    public boolean setDisplayedInfo(final EDefinitionDisplayMode showMode) {
        String name = fullName;
        if ((EDefinitionDisplayMode.SHOW_SHORT_NAMES == showMode) && (name.indexOf("::") != -1)) {
            if(name.indexOf('<')!=-1){
                final String s = name.substring(name.indexOf('<') + 1, name.length()-1);
                name = name.substring(0,name.indexOf('<')+1) + getNameWithoutModule(s)+">";
            } else{           
                name = getNameWithoutModule(name);
            }
            setDisplayedInfo(null, name);
        } else {
            setDisplayedInfo(null, name);
        }
        return true;
    }
    /*protected void setDisplayedInfo( String title,String name,EDefinitionDisplayMode showMode){
    this.fullName=name;
    this.title=title;
    if(title!=null)
    getCharFormat().setToolTip(getToolTip());
    setDisplayedInfo(showMode);
    }

    protected void setDisplayedInfo(String title,String displayName){
    this.displayName=displayName;
    this.title=title;
    if(title!=null)
    getCharFormat().setToolTip(getToolTip());
    calcEndTagPos();
    // endPos=startPos+displayName.length();
    }*/
    @Override
    protected String getSettingsPath() {
        return null;
    }
}
