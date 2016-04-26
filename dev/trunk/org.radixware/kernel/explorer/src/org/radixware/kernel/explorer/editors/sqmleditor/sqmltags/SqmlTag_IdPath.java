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

import com.trolltech.qt.gui.QIcon;
import com.trolltech.qt.gui.QWidget;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.EDefinitionDisplayMode;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.env.ITaskWaiter;
import org.radixware.kernel.common.client.env.ReleaseRepository;
import org.radixware.kernel.common.client.meta.sqml.ISqmlBrokenDefinition;
import org.radixware.kernel.common.client.meta.sqml.ISqmlColumnDef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlDefinition;
import org.radixware.kernel.common.client.meta.sqml.ISqmlDefinitions;
import org.radixware.kernel.common.client.meta.sqml.ISqmlDomainDef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlTableDef;
import org.radixware.kernel.common.client.views.IDialog;
import org.radixware.kernel.common.enums.EDefType;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.dialogs.ChooseSqmlDefinitionDialog;
import org.radixware.kernel.explorer.editors.xscmleditor.XscmlEditor;
import org.radixware.kernel.explorer.env.ExplorerIcon;
import org.radixware.kernel.explorer.models.SqmlTreeModel;
import org.radixware.schemas.xscml.Sqml;



public class SqmlTag_IdPath extends SqmlTag{
    
    private ISqmlDefinition definition;
    private org.radixware.schemas.xscml.Sqml.Item.Id idPath;
    private static final String path = "org.radixware.explorer/S_E/SYNTAX_SQML/SQML_ID_PATH";
    private boolean isTable;
    
    public SqmlTag_IdPath(final IClientEnvironment environment, final ISqmlDefinition definition, final long pos, final EDefinitionDisplayMode showMode, final boolean isTable){
        super(environment, pos,definition==null?false :definition.isDeprecated());
        this.definition = definition;
        this.isTable=isTable;
        valid = !(definition instanceof ISqmlBrokenDefinition);
        updateIdPath();
        if (valid){
            setDisplayedInfo(showMode);
        }
        else if(definition!=null){
            setDisplayedInfo("",definition.getDisplayableText(showMode));
        }
    }
    
    public SqmlTag_IdPath(final IClientEnvironment environment, final org.radixware.schemas.xscml.Sqml.Item.Id idPath, final long pos, final EDefinitionDisplayMode showMode) {
        super(environment,pos);
        this.idPath = (org.radixware.schemas.xscml.Sqml.Item.Id)idPath.copy();
        definition = environment.getSqmlDefinitions().findDefinitionByIdPath(idPath.getPath());        
        valid = definition!=null && !(definition instanceof ISqmlBrokenDefinition);        
        if (valid){
            setIsDeprecated(definition.isDeprecated());
            setDisplayedInfo(showMode);
        }
        else{
            if (definition==null){
                final StringBuilder idPathBuilder = new StringBuilder();
                idPathBuilder.append("???");
                if (idPath.getPath()!=null){
                    for (Id id: idPath.getPath()){
                        idPathBuilder.append(id.toString());
                    }
                }
                idPathBuilder.append("???");
                setDisplayedInfo("", idPathBuilder.toString());
            }
            else{
                setDisplayedInfo("", definition.getDisplayableText(showMode));
            }
        }
    }
    
    @Override
    public final boolean setDisplayedInfo(final EDefinitionDisplayMode showMode) {
        if (isValid()){
            final StringBuilder displayStringBuilder = new StringBuilder();
            displayStringBuilder.append("id[");        
            if ((definition instanceof ISqmlColumnDef) && showMode!=EDefinitionDisplayMode.SHOW_FULL_NAMES){
                final ISqmlTableDef tableDef = ((ISqmlColumnDef)definition).getOwnerTable();
                displayStringBuilder.append(tableDef.getDisplayableText(showMode));
                displayStringBuilder.append('.');
            }
            displayStringBuilder.append(definition.getDisplayableText(showMode));
            displayStringBuilder.append(']');
            setDisplayedInfo(calcToolTip(showMode),displayStringBuilder.toString());
            return true;                    
        }
        return false;
    }                
    
    private String calcToolTip(final EDefinitionDisplayMode showMode){
        if (definition instanceof ISqmlColumnDef){
            final SqmlTag_PropSqlName propTag = 
                new SqmlTag_PropSqlName(environment, (ISqmlColumnDef)definition, 0, org.radixware.schemas.xscml.Sqml.Item.PropSqlName.Owner.TABLE, showMode, null);                    
            return propTag.getToolTip();
        }
        else if (definition instanceof ISqmlTableDef){//this.getToolTip()
            ISqmlTableDef table=(ISqmlTableDef)definition;
            String s=isTable?"Table":"Class";
            return "<b>"+s+":  </b>" + table.getFullName();
        }
        else if (definition instanceof ISqmlDomainDef){    
            ISqmlDomainDef domain=(ISqmlDomainDef)definition;
            return "<b>Domain:  </b>" + domain.getFullName();
        }
        else{
            return "";
        }
        //return this.getToolTip();
    }

    @Override
    public boolean showEditDialog(final XscmlEditor editText, final EDefinitionDisplayMode showMode) {                
        if (!editText.isReadOnly()){
            final List<ISqmlDefinition> topLevelDefinitions;
            final List<ISqmlDefinition> definitionPath = new LinkedList<>();        
            final EnumSet<SqmlTreeModel.ItemType> defTypes;
            final String dialogTitle;
            final QIcon dialogIcon;
            if (definition instanceof ISqmlTableDef){
                topLevelDefinitions = null;
                defTypes = EnumSet.of(SqmlTreeModel.ItemType.MODULE_INFO);
                definitionPath.add(definition);
                dialogTitle = environment.getMessageProvider().translate("SqmlEditor", "Select Table");
                dialogIcon = ExplorerIcon.getQIcon(ClientIcon.Definitions.CLASS);
            }else if (definition instanceof ISqmlColumnDef){            
                topLevelDefinitions = null;
                definitionPath.add( ((ISqmlColumnDef)definition).getOwnerTable() );
                definitionPath.add(definition);
                defTypes = EnumSet.of(SqmlTreeModel.ItemType.MODULE_INFO, SqmlTreeModel.ItemType.PROPERTY, SqmlTreeModel.ItemType.PROPERTY_OBJECT);
                dialogTitle = environment.getMessageProvider().translate("SqmlEditor", "Select Column");
                dialogIcon = ExplorerIcon.getQIcon(ClientIcon.Definitions.COLUMN);
            }else if (definition instanceof ISqmlDomainDef){
                topLevelDefinitions = new ArrayList<>();
                topLevelDefinitions.addAll(environment.getSqmlDefinitions().getDomains());
                for (ISqmlDomainDef domainDef=(ISqmlDomainDef)definition; domainDef!=null; domainDef=domainDef.getParentDomain()){
                    definitionPath.add(0, domainDef);
                }
                defTypes = EnumSet.noneOf(SqmlTreeModel.ItemType.class);
                dialogTitle = environment.getMessageProvider().translate("SqmlEditor", "Select Domain");                
                dialogIcon = ExplorerIcon.getQIcon(ClientIcon.Definitions.DOMAIN);
            }else{
                return false;
            }
            final ISqmlDefinition choosedDef = 
                chooseAdsDefinition(defTypes, showMode, dialogTitle, dialogIcon, definitionPath, topLevelDefinitions, editText);
            if (choosedDef!=null){
                definition = choosedDef;
                updateIdPath();
                setDisplayedInfo(showMode);
                return true;
            }            
        }
        return false;
    }
    
    private ISqmlDefinition chooseAdsDefinition(final EnumSet<SqmlTreeModel.ItemType> itemTypes,
                                                    final EDefinitionDisplayMode showMode,
                                                    final String dialogTitle,
                                                    final QIcon dlgIcon,
                                                    final List<ISqmlDefinition> defPath,
                                                    final List<ISqmlDefinition> topLevelDefinitions,                                                    
                                                    final QWidget parent){        
        final List<ISqmlDefinition> sqmls;
        if (topLevelDefinitions==null){
            final ITaskWaiter taskWaiter = environment.getApplication().newTaskWaiter();
            try {
                final Collection<ReleaseRepository.DefinitionInfo> defs = environment.getDefManager().getRepository().getDefinitions(EDefType.CLASS);
                final ISqmlDefinition[] classes = taskWaiter.runAndWait(new Callable<ISqmlDefinition[]>() {
                    @Override
                    public ISqmlDefinition[] call() throws Exception {
                        return defInfosToSqmlDef(defs);
                    }
                });
                sqmls = Arrays.asList(classes);
            } catch (ExecutionException | InterruptedException ex) {
                environment.messageError(environment.getMessageProvider().translate("SqmlEditor", "Couldn't read classes"));
                return null;
            } finally {
                taskWaiter.close();
            }                        
        }else{
            sqmls = topLevelDefinitions;
        }
        final SqmlTreeModel sqmlModel = new SqmlTreeModel(environment, sqmls, itemTypes);
        sqmlModel.setMarkDeprecatedItems(true);
        sqmlModel.setDisplayMode(showMode);
        final ChooseSqmlDefinitionDialog dlg = new ChooseSqmlDefinitionDialog(environment, sqmlModel, defPath, false, parent);
        dlg.setWindowTitle(dialogTitle);        
        dlg.setWindowIcon(dlgIcon);
        if (dlg.execDialog() == IDialog.DialogResult.ACCEPTED) {
            return dlg.getCurrentItem();
        }
        return null;
    }
        
    private ISqmlDefinition[] defInfosToSqmlDef(final Collection<ReleaseRepository.DefinitionInfo> defs) {
        final List<ISqmlDefinition> list = new LinkedList<>();
        final ISqmlDefinitions sqmls = environment.getSqmlDefinitions();
        for (ReleaseRepository.DefinitionInfo i : defs) {
            EDefinitionIdPrefix prefix = i.id.getPrefix();
            if (prefix == EDefinitionIdPrefix.ADS_ENTITY_CLASS || prefix == EDefinitionIdPrefix.ADS_APPLICATION_CLASS) {
                final ISqmlDefinition foundDef = sqmls.findTableById(i.id);
                if (foundDef != null) {
                    list.add(foundDef);
                }
            }
        }
        return list.<ISqmlDefinition>toArray(new ISqmlDefinition[0]);
    }
    
    private SqmlTag_IdPath(final IClientEnvironment environment, SqmlTag_IdPath source) {
        super(environment, source);
    }                

    @Override
    public SqmlTag_IdPath copy() {
        SqmlTag_IdPath res = new SqmlTag_IdPath(environment, this);
        res.idPath = (org.radixware.schemas.xscml.Sqml.Item.Id)idPath.copy();
        res.definition = definition;
        return res;
    }
        
    private void updateIdPath(){
        if (definition!=null){
            idPath = org.radixware.schemas.xscml.Sqml.Item.Id.Factory.newInstance();
           
            List<Id> ids=new ArrayList<>();
            ids.addAll(Arrays.<Id>asList(definition.getIdPath()));
            if( isTable && !ids.get(0).getPrefix().equals(EDefinitionIdPrefix.DDS_TABLE)){
                Id tableId=Id.Factory.changePrefix(definition.getId(), EDefinitionIdPrefix.DDS_TABLE);
                ids.clear();
                ids.add(tableId);                          
            }
            idPath.setPath(ids);   
        }
    }

    @Override
    public void addTagToSqml(final XmlObject itemTag) {
        Sqml.Item item = (Sqml.Item) itemTag;
        item.setId(idPath);
    }            

    @Override
    protected String getSettingsPath() {
        return path;
    }
}
