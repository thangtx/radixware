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

import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QMouseEvent;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.EDefinitionDisplayMode;
import org.radixware.kernel.common.client.errors.ObjectNotFoundError;
import org.radixware.kernel.common.client.meta.RadClassPresentationDef;
import org.radixware.kernel.common.client.meta.RadSelectorPresentationDef;
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.client.models.GroupModel;
import org.radixware.kernel.common.client.types.Pid;
import org.radixware.kernel.common.client.utils.DialogUtils;
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.jml.JmlTagDbEntity;
import org.radixware.kernel.common.scml.Scml;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.dialogs.SelectEntityDialog;
import org.radixware.kernel.explorer.editors.xscmleditor.XscmlEditor;
import org.radixware.kernel.explorer.utils.WidgetUtils;


public class JmlTag_DbEntity extends JmlTag {

    private static final String PATH = "org.radixware.explorer/S_E/SYNTAX_JML/JML_TAG_DB_ENTITY";
    private IDbEntityTitleProvider titleProvider;    

    private JmlTag_DbEntity(final IClientEnvironment environment, final JmlTag_DbEntity source) {
        super(environment, source);
    }

    @Override
    public JmlTag_DbEntity copy() {
        final JmlTag_DbEntity res = new JmlTag_DbEntity(environment, this);
        res.tag = tag;
        res.setTitleProvider(titleProvider);
        return res;
    }  

    public JmlTag_DbEntity(final IClientEnvironment environment,final JmlTagDbEntity tag, final long pos, final EDefinitionDisplayMode showMode, IDbEntityTitleProvider titleProvider, final boolean isReadOnly) {
        super(environment, pos, false);         
        this.titleProvider = titleProvider;
        updateTag(tag, showMode, !isReadOnly);
    }
    
    private void updateTag(final JmlTagDbEntity tag, final EDefinitionDisplayMode showMode, final boolean isActualizeNeeded){ 
        this.tag = tag;
        if(tag.getTitle() == null || isActualizeNeeded) {
            tag.setTitle(actualize(tag.getTableId(),  tag.getPidAsStr(), tag.isUFOwnerRef()));
        }
        final StringBuilder sb=new StringBuilder();
        if(tag.getTitle() != null){ 
            sb.append("<br>Title: ").append(tag.getTitle());
            setDisplayedInfo(tag.getToolTip()+sb.toString(), tag.getDisplayName(), showMode);
        }else{
            setDisplayedInfo(tag.getToolTip(), "???"+tag.getDisplayName()+"???", showMode);
        }
    }
    
    private String actualize(final Id tableId, final String pidAsStr, final boolean isOwnerEntityRef){
        String res=null;
        try{
            if (tableId != null && (pidAsStr != null || isOwnerEntityRef)) {
                if (titleProvider != null && titleProvider.getTitle(tableId, pidAsStr) != null) {
                    res = titleProvider.getTitle(tableId, pidAsStr);
                } else {
                    final Pid pid = new Pid(tableId, pidAsStr);
                    res = pid.getDefaultEntityTitle(environment.getEasSession());
                }
            }
        } catch (InterruptedException ex) {
        } catch (ServiceClientException ex) {
            setValid(false);
            //environment.processException(ex);
            //final String mess = Application.translate("SqmlEditor", "Can not actualize entity for #%s");
            //environment.getTracer().warning(String.format(mess, pidAsStr));
        } 
        return res;
    }
       
    @Override
    public boolean setDisplayedInfo(final EDefinitionDisplayMode showMode) {
        String name = fullName;
        if ((EDefinitionDisplayMode.SHOW_SHORT_NAMES == showMode) && (name.indexOf("::") != -1)) {
            String notValidPrefix="???";
            if(!name.startsWith(notValidPrefix)){
                notValidPrefix=null;
            }
            name = (notValidPrefix==null ? "" : notValidPrefix) + getNameWithoutModule(name);
            setDisplayedInfo(null,/*s+*/ name);
        } else {
            setDisplayedInfo(null, name);
        }
        return true;
    }

    @Override
    protected String getSettingsPath() {
        return PATH;
    }    
    
    @Override
    public boolean showEditDialog(final XscmlEditor editText, final EDefinitionDisplayMode showMode) {        
        final RadClassPresentationDef classDef = environment.getApplication().getDefManager().getClassPresentationDef(((JmlTagDbEntity)tag).getTableId());
        final RadSelectorPresentationDef selPresDef = classDef.getDefaultSelectorPresentation();
        final GroupModel groupModel = GroupModel.openTableContextlessSelectorModel(WidgetUtils.findNearestModel(editText), selPresDef);

        final SelectEntityDialog dialog = new SelectEntityDialog(groupModel, false);
        if (com.trolltech.qt.gui.QDialog.DialogCode.resolve(dialog.exec()).equals(com.trolltech.qt.gui.QDialog.DialogCode.Accepted)) {
            final EntityModel entity=dialog.getSelectedEntity();
            final RadixObjects<Scml.Item>  items=tag.getOwnerScml().getItems();
            final int index=items.indexOf(tag);
            items.remove(tag);
            tag=JmlTagDbEntity.Factory.newInstance(entity.getClassId(), entity.getPid().toString(), false);
            items.add(index, tag);
            updateTag((JmlTagDbEntity)tag, showMode, !editText.isReadOnly());
            return true;
        }else{
            return false;
        }
    }
    
    static public interface IDbEntityTitleProvider {
        String getTitle(Id tableId, String pidAsStr);
    }

    public IDbEntityTitleProvider getTitleProvider() {
        return titleProvider;
    }

    public void setTitleProvider(IDbEntityTitleProvider titleProvider) {
        this.titleProvider = titleProvider;
    }

    @Override
    public void onMouseReleased(QMouseEvent e, IClientEnvironment env) {
        //ctrl + click, event was checked in XscmlEditor
        Id tableId = null;
        String pidAsStr = null;
        if (tag instanceof JmlTagDbEntity) {
            final JmlTagDbEntity dbTag = (JmlTagDbEntity) tag;
            tableId = dbTag.getTableId();
            pidAsStr = dbTag.getPidAsStr();
        }
        if (tableId != null && pidAsStr != null) {
            try {
                DialogUtils.showEntityEditor(tableId, pidAsStr, env);
            } catch (InterruptedException | ServiceClientException ex) {
                final String cause;
                if (ex instanceof ObjectNotFoundError) {
                    cause = "Object not found:\n" + ex.getMessage();
                } else {
                    cause = ex.getClass().getName() + "\n" + ex.getMessage();
                }
                environment.messageError("Can not open editor dialog. " + cause);
            }
        }
    }
    
    @Override
    public Qt.CursorShape getCursorShape(QMouseEvent e) {
        return Qt.CursorShape.PointingHandCursor;
    }
    
}
