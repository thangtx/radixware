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

package org.radixware.kernel.explorer.editors.scmleditor.sqml.tags;

import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.meta.sqml.ISqmlEnumDef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlEnumItem;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.editors.scmleditor.TagInfo;
import org.radixware.kernel.explorer.editors.scml.IScmlItem;
import org.radixware.kernel.explorer.editors.scmleditor.sqml.tags.translator.ConstValueTranslator;
import org.radixware.schemas.xscml.Sqml.Item.ConstValue;


public class SqmlTag_ConstValue extends TagInfo{
    private ConstValue constValue;
    private static final String PATH = "org.radixware.explorer/S_E/SYNTAX_SQML/SQML_CONSTANT_VALUE";

    public SqmlTag_ConstValue(final IClientEnvironment environment, final ConstValue constValue) {
        super(environment);
        this.constValue = constValue==null ? null : (ConstValue)constValue.copy();
        ISqmlEnumItem constSetItem =null;
        valid=false;
        if (this.constValue != null) {
            final ISqmlEnumDef constSet = environment.getSqmlDefinitions().findEnumById(this.constValue.getEnumId());
            if (constSet != null) {
                //setConstVal(constValue);
                constSetItem = constSet.findItemById(this.constValue.getItemId());                
                if (constSetItem != null) {
                    valid=true;
                    //fullName = constSet.getFullName() + ":" + constSetItem.getShortName();
                    //setDisplayedInfo(showMode);
                } //else {
                  //  setNotValid(constValue.getSql());
                //}
            } //else {
              //  setNotValid(constValue.getSql());
            //}
            translator=new ConstValueTranslator(constSet,constSetItem,valid);
        }
    }

    public SqmlTag_ConstValue(final IClientEnvironment environment,final ISqmlEnumItem constSetItem) {
        super(environment);
        valid=false;
        ISqmlEnumDef constSet=null;
        if (constSetItem != null) {
            final Id enumId = constSetItem.getOwnerEnumId();
            final Id ItemId = constSetItem.getId();
            final String val = constSetItem.getValueAsString();
            constSet = environment.getSqmlDefinitions().findEnumById(constSetItem.getOwnerEnumId());
            if (constSet != null) {
                valid=true;
                setConstVal(ItemId, enumId, val);
                //fullName = constSet.getFullName() + ":" + constSetItem.getShortName();
                //setDisplayedInfo(showMode);
            } //else {
              //  setNotValid(constValue.getValueAsString());
            //}
        }
        translator=new ConstValueTranslator(constSet,constSetItem,valid);
    }
    
    @Override
    public XmlObject saveToXml() {
        return constValue;
    }

   /* private void setConstValueInfo(EDefinitionDisplayMode showMode) {
        Id constSetId = constSetItem.getOwnerEnumId();
        try {
            constSet = environment.getSqmlDefinitions().findEnumById(constSetId);
            setConstVal(constSetItem.getId(), constSetId, constSetItem.getValueAsString());
            String defName = constSet.getFullName();//==null ? ("#"+constSet.getId()) : constSet.getName();
            fullName = defName + ":" + constSetItem.getShortName();
            setDisplayedInfo(showMode);
        } catch (DefinitionError ex) {
            environment.getTracer().put(EEventSeverity.WARNING, "constSetId not found", constSetId.toString());
            setNotValid(constSetId.toString());
        }
    }*/

    /*private void setNotValid(String val) {
        valid = false;
        String s = "???<ConstSetId>-'<" + val + ">'???";
        setDisplayedInfo(s, s);
    }

    private void setConstVal(ConstValue constVal) {
        constValue = ConstValue.Factory.newInstance();
        constValue.setEnumId(constVal.getEnumId());
        constValue.setItemId(constVal.getItemId());
        constValue.setSql(constVal.getSql());
    }*/

    private void setConstVal(final Id itemId, final Id enumId, final String sql) {
        constValue = ConstValue.Factory.newInstance();
        constValue.setEnumId(enumId);
        constValue.setItemId(itemId);
        constValue.setSql(sql);
    }

    /*@Override
    public boolean showEditDialog(XscmlEditor editText, EDefinitionDisplayMode showMode) {
        if (constSetItem != null) {
            ConstValue_Dialog dialog = new ConstValue_Dialog(editText.getEnvironment(), editText, constSetItem, showMode);
            if (dialog.exec() == 1) {
                this.constSetItem = dialog.getConstSetItem();
                setConstValueInfo(showMode);
                return true;
            }
        }
        return false;
    }*/

   /* private String getToolTip(String constset, String item) {
        if (!valid) {
            return "";
        }
        String res = "<b>Constantset item:</b>";
        res += "<br>&nbsp;&nbsp;&nbsp;&nbsp;" + item + "</br>";
        res += "<br><b>Constantset:</b></br>";
        res += "<br>&nbsp;&nbsp;&nbsp;&nbsp;" + constset + "</br>";
        return res;
    }

    @Override
    public boolean setDisplayedInfo(EDefinitionDisplayMode showMode) {
        if (valid()){
            String name;
            final String constSetName = constSet.getShortName();//(constSet.getName()==null ? "#"+constSetItem.getId() : constSet.getName());
            if (EDefinitionDisplayMode.SHOW_TITLES == showMode) {
                //name=getNameWithoutModule(constSetName);
                name = constSetName + ":" + constSetItem.getTitle();
                setDisplayedInfo(getToolTip(constSetName, constSetItem.getShortName()), name);
            } else if (EDefinitionDisplayMode.SHOW_SHORT_NAMES == showMode) {
                //name=getNameWithoutModule();
                name = constSetName + ":" + constSetItem.getShortName();
                setDisplayedInfo(getToolTip(constSetName, constSetItem.getTitle()), name);
            } else {
                name = fullName;
                setDisplayedInfo(getToolTip(constSet.getTitle(), constSetItem.getTitle()), name);
            }
            return true;
        }
        return false;
    }

    private SqmlTag_ConstValue(final IClientEnvironment environment, SqmlTag_ConstValue source) {
        super(environment, source);
    }*/


    
    @Override  
    public IScmlItem  getCopy() {
        return new SqmlTag_ConstValue( environment,  constValue);
        //SqmlTag_ConstValue res = new SqmlTag_ConstValue(environment, this);
        //res.constValue = constValue;
        //res.constSet = constSet;
        //res.constSetItem = constSetItem;
        //return res;
    }

    /*@Override
    public void addTagToSqml(XmlObject itemTag) {
        Sqml.Item item = (Sqml.Item) itemTag;
        item.setConstValue(this.constValue);
    }*/

    @Override
    protected String getSettingsPath() {
        return PATH;
    }
}
