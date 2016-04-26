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
import org.radixware.kernel.common.client.exceptions.ClientException;
import org.radixware.kernel.common.client.meta.sqml.ISqmlColumnDef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlEnumDef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlEnumItem;
import org.radixware.kernel.common.client.meta.sqml.ISqmlTableDef;
import org.radixware.kernel.common.client.types.Pid;
import org.radixware.kernel.common.client.utils.ValueConverter;
import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.exceptions.DefinitionError;
import org.radixware.kernel.common.exceptions.NoConstItemWithSuchValueError;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.exceptions.WrongFormatError;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.editors.sqmleditor.tageditors.TypifiedValue_Dialog;
import org.radixware.kernel.explorer.editors.xscmleditor.TagInfo;
import org.radixware.kernel.explorer.editors.xscmleditor.XscmlEditor;
import org.radixware.schemas.xscml.Sqml;
import org.radixware.schemas.xscml.Sqml.Item.TypifiedValue;


public class SqmlTag_TypifiedValue extends SqmlTag {

    private TypifiedValue typifiedValue;
    private ISqmlColumnDef prop;
    private Object val;
    private boolean isLiteral;
    private static final String PATH = "org.radixware.explorer/S_E/SYNTAX_SQML/SQML_TYPIFIED_VALUE";
    //private EDefinitionDisplayMode showMode;

    public SqmlTag_TypifiedValue(final IClientEnvironment environment,final ISqmlColumnDef prop,final Object value,final long pos,final EDefinitionDisplayMode showMode) {
        super(environment, pos, false);
        this.prop = prop;
        this.val = value;
        this.isLiteral = true;
        try {
            final ISqmlTableDef presentationClassDef = prop.getOwnerTable();
            if (presentationClassDef != null) {
                final Id tableId = presentationClassDef.getId();
                setTypifiedValue(prop, tableId, isLiteral);
                getPropInfo(showMode);
            } else {
                setNotValid();
            }
        } catch (DefinitionError ex) {
            final String mess = environment.getMessageProvider().translate("SqmlEditor", "table #%s not found");
            environment.getTracer().put(EEventSeverity.WARNING, String.format(mess, prop.getOwnerTableId()), prop.getOwnerTableId().toString());
            setNotValid();
        }
    }

    public SqmlTag_TypifiedValue(final IClientEnvironment environment,final TypifiedValue typifiedValue,final long pos,final EDefinitionDisplayMode showMode) {
        super(environment, pos);
        this.typifiedValue = typifiedValue;
        final Id tableId = typifiedValue.getTableId();
        final Id propId = typifiedValue.getPropId();

        try {
            final ISqmlTableDef presentationClassDef = environment.getSqmlDefinitions().findTableById(tableId);
            if(presentationClassDef!=null){
                prop = presentationClassDef.getColumns().getColumnById(propId);
                if (prop != null) {
                    this.val = ValAsStr.fromStr(typifiedValue.getValue(), ValueConverter.serverValType2ClientValType(prop.getType()));
                    isLiteral = typifiedValue.getLiteral();
                    setTypifiedValue(typifiedValue);
                    getPropInfo(showMode);
                } else {
                    setNotValid();
                }
            }else {
                setNotValid();
            }
        } catch (DefinitionError ex) {
            final String mess = environment.getMessageProvider().translate("SqmlEditor", "table #%s not found");
            environment.getTracer().warning(String.format(mess, tableId));
            setNotValid();
        } catch (WrongFormatError error) {
            final String mess = environment.getMessageProvider().translate("SqmlEditor", "Can't restore typified value '%s': %s");
            final String reason = ClientException.getExceptionReason(environment.getMessageProvider(), error);
            environment.getTracer().error(String.format(mess, typifiedValue.getValue(), reason));
            setNotValid();
        }
    }

    private void getPropInfo(final EDefinitionDisplayMode showMode) {
        if (val == null) {
            setDisplayedInfo("", "null");
        } else if (prop.getEnums().size()==1) {
            setConstSetPropInfo(showMode);
        } else if (prop.getType() == EValType.PARENT_REF) {
            try {
                final String pid = ((Pid) val).getDefaultEntityTitle(environment.getEasSession());//((Reference) val).getPid().getDefaultEntityTitle();
                setDisplayedInfo("", pid);
            } catch (ServiceClientException ex) {
                setNotValid();
                environment.processException(ex);
                //Logger.getLogger(SqmlTag_TypifiedValue.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                setNotValid();
                //Logger.getLogger(SqmlTag_TypifiedValue.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            final String s = prop.getEditMask().toStr(environment,val);
            setDisplayedInfo("", s);
        }
    }

    private boolean setConstSetPropInfo(final EDefinitionDisplayMode showMode) {
        final ISqmlEnumDef enumDef = prop.getEnums().iterator().next();        
        final ISqmlEnumItem item = enumDef.findItemByValue(val.toString());
        if (item != null) {
            final StringBuilder sbName=new StringBuilder();
            String s;
            fullName = enumDef.getFullName() + ":" + item.getShortName();
            if (EDefinitionDisplayMode.SHOW_TITLES == showMode) {
                sbName.append(TagInfo.getNameWithoutModule(enumDef.getTitle()));
                sbName.append(':').append(item.getTitle());
                s = enumDef.getFullName() + ":" + item.getFullName();
            } else if (EDefinitionDisplayMode.SHOW_SHORT_NAMES == showMode) {
                sbName.append(TagInfo.getNameWithoutModule(fullName));
                s = enumDef.getTitle() + ":" + item.getTitle();
            } else {
                sbName.append(fullName);
                s = enumDef.getTitle() + ":" + item.getTitle();
            }
            setDisplayedInfo(s, sbName.toString());
            return true;
        }
        throw new NoConstItemWithSuchValueError("Constant item with Value=\"" + val + "\" was not found", val);
    }

    /* private String getNameWithoutModul(String name){
    int index=name.lastIndexOf("::");
    if(index!=-1)
    return name.substring(index+2,name.length());
    return name;
    }*/
    private void setNotValid() {
        valid = false;
        final String s = "???'<" + val + ">'???";
        setDisplayedInfo(s, s);
    }

    private void setTypifiedValue(final ISqmlColumnDef prop,final Id tableId,final boolean isLiteral) {
        this.typifiedValue = TypifiedValue.Factory.newInstance();
        this.typifiedValue.setTableId(tableId);
        this.typifiedValue.setPropId(prop.getId());
        this.typifiedValue.setLiteral(isLiteral);
        if (val != null) {
            EValType type=prop.getType();
            if(type==EValType.ANY){
                type=EValType.STR;
            }
            this.typifiedValue.setValue(ValAsStr.toStr(val, type)/*ValueConverter.objVal2ValAsStr(setValue(val),prop.getType())*/);
        }
    }

    private void setTypifiedValue(final TypifiedValue typifiedValue) {
        this.typifiedValue = TypifiedValue.Factory.newInstance();
        this.typifiedValue.setTableId(typifiedValue.getTableId());
        this.typifiedValue.setPropId(typifiedValue.getPropId());
        this.typifiedValue.setValue(typifiedValue.getValue());
        this.typifiedValue.setDisplayValue(typifiedValue.getDisplayValue());
        this.typifiedValue.setLiteral(typifiedValue.getLiteral());
    }

    @Override
    public boolean showEditDialog(final XscmlEditor editText,final EDefinitionDisplayMode showMode) {
        if (valid) {
            try {
                if (typifiedValue != null) {
                    isLiteral = typifiedValue.getLiteral();
                }
                final ISqmlTableDef presentationClassDef = prop.getOwnerTable();
                TypifiedValue_Dialog dialog = new TypifiedValue_Dialog(environment, prop, editText, val, showMode, isLiteral);
                if ((dialog.exec() == 1) && (dialog.getProperty() != null)) {
                    prop = dialog.getProperty();
                    if (dialog.getValue() != null) {
                        val = dialog.getValue().get(0);
                    } else {
                        val = null;
                    }
                    isLiteral = dialog.isLiteral();
                    setTypifiedValue(prop, presentationClassDef.getId(), isLiteral);
                    getPropInfo(showMode);
                    return true;
                }
            } catch (DefinitionError ex) {
                final String mess = environment.getMessageProvider().translate("SqmlEditor", "table #%s not found");
                environment.getTracer().put(EEventSeverity.WARNING, String.format(mess, prop.getOwnerTableId()), prop.getOwnerTableId().toString());
            }
        }
        return false;
    }

    @Override
    public boolean setDisplayedInfo(final EDefinitionDisplayMode showMode) {
        if (isValid() && (prop.getEnums().size()==1)) {
            setConstSetPropInfo(showMode);
        }
        return isValid();
    }

    private SqmlTag_TypifiedValue(final IClientEnvironment environment,final SqmlTag_TypifiedValue source) {
        super(environment, source);
    }

    @Override
    public SqmlTag_TypifiedValue copy() {
        final SqmlTag_TypifiedValue res = new SqmlTag_TypifiedValue(environment, this);
        res.typifiedValue = typifiedValue;
        res.prop = prop;
        res.val = val;
        res.isLiteral = isLiteral;
        return res;
    }

    @Override
    public void addTagToSqml(final XmlObject itemTag) {
        final Sqml.Item item = (Sqml.Item) itemTag;
        item.setTypifiedValue(this.typifiedValue);
    }

    @Override
    protected String getSettingsPath() {
        return PATH;
    }

    public TypifiedValue getTypifiedValue() {
        return typifiedValue;
    }
}
