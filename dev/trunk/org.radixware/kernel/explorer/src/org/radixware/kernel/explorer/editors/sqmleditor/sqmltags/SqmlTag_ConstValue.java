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
import org.radixware.kernel.common.client.enums.EDefinitionDisplayMode;
import org.radixware.kernel.common.client.meta.sqml.ISqmlEnumDef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlEnumItem;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.exceptions.DefinitionError;
import org.radixware.kernel.common.html.Html;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.editors.sqmleditor.tageditors.ConstValue_Dialog;

import org.radixware.kernel.explorer.editors.xscmleditor.XscmlEditor;
import org.radixware.schemas.xscml.Sqml;
import org.radixware.schemas.xscml.Sqml.Item.ConstValue;


public class SqmlTag_ConstValue extends SqmlTag {

    private ConstValue constValue;
    private ISqmlEnumItem constSetItem;
    private ISqmlEnumDef constSet;
    private static final String PATH = "org.radixware.explorer/S_E/SYNTAX_SQML/SQML_CONSTANT_VALUE";

    public SqmlTag_ConstValue(final IClientEnvironment environment,final ConstValue constValue,final long pos,final EDefinitionDisplayMode showMode) {
        super(environment, pos);
        if (constValue != null) {
            constSet = environment.getSqmlDefinitions().findEnumById(constValue.getEnumId());
            if (constSet != null) {
                setConstVal(constValue);
                constSetItem = constSet.findItemById(constValue.getItemId());
                if (constSetItem != null) {
                    setIsDeprecated(constSet.isDeprecated());
                    fullName = constSet.getFullName() + ":" + constSetItem.getShortName();
                    setDisplayedInfo(showMode);
                } else {
                    setNotValid(constValue.getSql());
                }                
            } else {
                setNotValid(constValue.getSql());
            }
        }
    }

    public SqmlTag_ConstValue(final IClientEnvironment environment, final ISqmlEnumItem constValue,final long pos, final EDefinitionDisplayMode showMode) {
        super(environment, pos, constValue==null?false :constValue.isDeprecated());
        constSetItem = constValue;
        if (constValue != null) {
            final Id enumId = constValue.getOwnerEnumId();
            final Id ItemId = constValue.getId();
            final String val = constValue.getValueAsString();
            constSet = environment.getSqmlDefinitions().findEnumById(constSetItem.getOwnerEnumId());
            if (constSet != null) {
                setConstVal(ItemId, enumId, val);
                fullName = constSet.getFullName() + ":" + constSetItem.getShortName();
                setDisplayedInfo(showMode);
            } else {
                setNotValid(constValue.getValueAsString());
            }
        }
    }

    private void setConstValueInfo(final EDefinitionDisplayMode showMode) {
        final Id constSetId = constSetItem.getOwnerEnumId();
        try {
            constSet = environment.getSqmlDefinitions().findEnumById(constSetId);
            setConstVal(constSetItem.getId(), constSetId, constSetItem.getValueAsString()/*c.getValByName(constSetItem.getName()).toString()*/);
            final String defName = constSet.getFullName();//==null ? ("#"+constSet.getId()) : constSet.getName();
            fullName = defName + ":" + constSetItem.getShortName();
            setDisplayedInfo(showMode);
        } catch (DefinitionError ex) {
            environment.getTracer().put(EEventSeverity.WARNING, "constSetId not found", constSetId.toString());
            setNotValid(constSetId.toString());
        }
    }

    private void setNotValid(final String val) {
        setValid(false);
        final String s = "???<ConstSetId>-'<" + val + ">'???";
        setDisplayedInfo(s, s);
    }

    private void setConstVal(final ConstValue constVal) {
        constValue = ConstValue.Factory.newInstance();
        constValue.setEnumId(constVal.getEnumId());
        constValue.setItemId(constVal.getItemId());
        constValue.setSql(constVal.getSql());
    }

    private void setConstVal(final Id itemId, final Id enumId,final String sql) {
        constValue = ConstValue.Factory.newInstance();
        constValue.setEnumId(enumId);
        constValue.setItemId(itemId);
        constValue.setSql(sql);
    }

    @Override
    public boolean showEditDialog(final XscmlEditor editText, final EDefinitionDisplayMode showMode) {
        if (constSetItem != null) {
            final ConstValue_Dialog dialog = new ConstValue_Dialog(editText.getEnvironment(), editText, constSetItem, showMode);
            if (dialog.exec() == 1) {
                this.constSetItem = dialog.getConstSetItem();
                setConstValueInfo(showMode);
                return true;
            }
        }
        return false;
    }

    private String getToolTip(final String constset,final String item) {
        if (!isValid()) {
            return "";
        }
        final StringBuilder sb=new StringBuilder();
        sb.append("<b>Constantset item:</b><br>&nbsp;&nbsp;&nbsp;&nbsp;").append(item);
        sb.append("</br><br><b>Constantset:</b></br><br>&nbsp;&nbsp;&nbsp;&nbsp;").append(constset);
        sb.append("</br>");           
        return sb.toString();
    }

    @Override
    public final boolean setDisplayedInfo(final EDefinitionDisplayMode showMode) {
        if (isValid()){
            String name;
            final String constSetName = Html.string2HtmlString(constSet.getShortName());//(constSet.getName()==null ? "#"+constSetItem.getId() : constSet.getName());
            if (EDefinitionDisplayMode.SHOW_TITLES == showMode) {
                //name=getNameWithoutModule(constSetName);
                name = constSetName + ":" + Html.string2HtmlString(constSetItem.getTitle());
                setDisplayedInfo(getToolTip(constSetName, constSetItem.getShortName()), name);
            } else if (EDefinitionDisplayMode.SHOW_SHORT_NAMES == showMode) {
                //name=getNameWithoutModule();
                name = constSetName + ":" + Html.string2HtmlString(constSetItem.getShortName());
                setDisplayedInfo(getToolTip(constSetName, constSetItem.getTitle()), name);
            } else {
                name = fullName;
                setDisplayedInfo(getToolTip(Html.string2HtmlString(constSet.getTitle()), Html.string2HtmlString(constSetItem.getTitle())), name);
            }
            return true;
        }
        return false;
    }

    private SqmlTag_ConstValue(final IClientEnvironment environment, final SqmlTag_ConstValue source) {
        super(environment, source);
    }

    @Override
    public SqmlTag_ConstValue copy() {
        final SqmlTag_ConstValue res = new SqmlTag_ConstValue(environment, this);
        res.constValue = constValue;
        res.constSet = constSet;
        res.constSetItem = constSetItem;
        return res;
    }

    @Override
    public void addTagToSqml(final XmlObject itemTag) {
        final Sqml.Item item = (Sqml.Item) itemTag;
        item.setConstValue(this.constValue);
    }

    @Override
    protected String getSettingsPath() {
        return PATH;
    }
}