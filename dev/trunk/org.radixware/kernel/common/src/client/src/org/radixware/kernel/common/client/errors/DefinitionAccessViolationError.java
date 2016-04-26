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

package org.radixware.kernel.common.client.errors;

import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.DefManager;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.meta.RadClassPresentationDef;
import org.radixware.kernel.common.client.meta.RadEditorPresentationDef;
import org.radixware.kernel.common.client.meta.TitledDefinition;
import org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemDef;
import org.radixware.kernel.common.client.meta.explorerItems.RadParagraphDef;
import org.radixware.kernel.common.enums.EDefType;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.exceptions.DefinitionError;
import org.radixware.kernel.common.exceptions.NoConstItemWithSuchValueError;
import org.radixware.kernel.common.exceptions.ServiceCallFault;
import org.radixware.kernel.common.types.Id;


public final class DefinitionAccessViolationError extends AccessViolationError{        
    
    private static final long serialVersionUID = 8322009042700016417L;
    
    private final String faultMessage;
    private final String displayMessage;
            
    public DefinitionAccessViolationError(final IClientEnvironment environment, final ServiceCallFault source){
        super(source.getFaultCode(), source.getFaultString(), source.getDetail());
        final String sourceMessage = source.getMessage();
        if (sourceMessage==null || sourceMessage.isEmpty()){
            faultMessage = "";
            displayMessage = "";
        }else{
            final String[] arrStr = sourceMessage.split("\n");
            if (arrStr.length<3 || arrStr[0].isEmpty() || arrStr[1].isEmpty()){
                faultMessage = sourceMessage;
                displayMessage = sourceMessage;
            }else{
                final EDefType defType;
                try{
                    defType = EDefType.getForValue(Long.parseLong(arrStr[0]));
                }catch(NumberFormatException | NoConstItemWithSuchValueError error){//NOPMD
                    faultMessage = sourceMessage;
                    displayMessage = sourceMessage;
                    return;
                }
                final String[] IdsPath = arrStr[1].split(" ");                
                final StringBuilder faultMessageBuilder = new StringBuilder(arrStr[2]);
                for (int i=3; i<arrStr.length; i++){
                    faultMessageBuilder.append('\n');
                    faultMessageBuilder.append(arrStr[i]);
                }
                faultMessage = faultMessageBuilder.toString();
                final String message = parseMessage(defType, IdsPath, environment);
                displayMessage = message==null ? faultMessage : message;
            }
        }
    }
    
    @Override
    public String getMessage() {
        return faultMessage;
    }

    @Override
    public String getCauseExMessage() {
        return displayMessage;
    }

    @Override
    public String getLocalizedMessage(final MessageProvider messageProvider) {
        return displayMessage;
    }        
    
    private static String parseMessage(final EDefType defType, final String[] idPath, final IClientEnvironment environment){
        final TitledDefinition definition;
        try{
            definition = getDefinition(defType, idPath, environment.getDefManager());
        }catch(DefinitionError error){
            return null;
        }
        if (definition==null){
            return null;
        }        
        final MessageProvider mp = environment.getMessageProvider();
        final String messageTemplate;
        switch(defType){
            case SELECTOR_PRESENTATION:{
                messageTemplate = mp.translate("ExplorerError", "Unable to access selector presentation \'%1$s\': insufficient privileges");
                break;
            }
            case EDITOR_PRESENTATION:{
                messageTemplate = mp.translate("ExplorerError", "Unable to access editor presentation \'%1$s\': insufficient privileges");
                break;
            }
            case CLASS_PROPERTY:{
                messageTemplate = mp.translate("ExplorerError", "Unable to access value of property \'%1$s\': insufficient privileges");
                break;
            }
            case SCOPE_COMMAND:
            case CONTEXTLESS_COMMAND:{                
                messageTemplate = mp.translate("ExplorerError", "Unable to execute command \'%1$s\': insufficient privileges");
                break;
            }            
            case EXPLORER_ITEM:{
                messageTemplate = mp.translate("ExplorerError", "Unable to access explorer item \'%1$s\': insufficient privileges");
                break;
            }case PARAGRAPH:{
                messageTemplate = mp.translate("ExplorerError", "Unable to access explorer root \'%1$s\': insufficient privileges");
                break;
            }
            default:
                return null;
        }
        final String definitionTitle = definition.hasTitle() ? definition.getTitle() : definition.getName();
        return String.format(messageTemplate,definitionTitle);
    }
    
    private static TitledDefinition getDefinition(final EDefType defType, final String[] idPath, final DefManager defManager){
        switch(defType){
            case SELECTOR_PRESENTATION:{
                final Id presentationId = Id.Factory.loadFrom(idPath.length>1 ? idPath[1] : idPath[0]);
                return defManager.getSelectorPresentationDef(presentationId);
            }
            case EDITOR_PRESENTATION:{
                final Id presentationId = Id.Factory.loadFrom(idPath.length>1 ? idPath[1] : idPath[0]);
                return defManager.getEditorPresentationDef(presentationId);
            }
            case CLASS_PROPERTY:{
                if (idPath.length<2){
                    return null;
                }
                final Id classId = Id.Factory.loadFrom(idPath[0]);
                final RadClassPresentationDef classDef = defManager.getClassPresentationDef(classId);
                return classDef.getPropertyDefById(Id.Factory.loadFrom(idPath[1]));
            }
            case SCOPE_COMMAND:{
                if (idPath.length<2){
                    return null;
                }
                final Id classId = Id.Factory.loadFrom(idPath[0]);
                final RadClassPresentationDef classDef = defManager.getClassPresentationDef(classId);
                return classDef.getCommandDefById(Id.Factory.loadFrom(idPath[1]));
            }
            case CONTEXTLESS_COMMAND:{
                final Id commandId = Id.Factory.loadFrom(idPath[0]);
                return defManager.getContextlessCommandDef(commandId);
            }
            case EXPLORER_ITEM:{
                if (idPath.length<2){
                    return null;
                }                
                final Id ownerId = Id.Factory.loadFrom(idPath[0]);
                if (ownerId.getPrefix()== EDefinitionIdPrefix.PARAGRAPH){
                    final RadParagraphDef paragraph = defManager.getParagraphDef(ownerId);
                    final RadExplorerItemDef explorerItemDef = paragraph.getChildrenExplorerItems().findExplorerItem(Id.Factory.loadFrom(idPath[1]));
                    if (explorerItemDef==null || explorerItemDef.hasTitle() || explorerItemDef.getModelDefinition() instanceof TitledDefinition==false){
                        return explorerItemDef;
                    }
                    return (TitledDefinition)explorerItemDef.getModelDefinition();
                }else{
                    final RadEditorPresentationDef presentation;
                    final int explorerItemIdIndex;
                    if (ownerId.getPrefix()==EDefinitionIdPrefix.EDITOR_PRESENTATION){
                        presentation = defManager.getEditorPresentationDef(ownerId);
                        explorerItemIdIndex = 1;
                    }else{
                        if (idPath.length<3){
                            return null;
                        }
                        presentation = defManager.getEditorPresentationDef(Id.Factory.loadFrom(idPath[1]));
                        explorerItemIdIndex = 2;
                    }
                    final Id explorerItemId = Id.Factory.loadFrom(idPath[explorerItemIdIndex]);
                    final RadExplorerItemDef explorerItemDef = presentation.getChildrenExplorerItems().findExplorerItem(explorerItemId);
                    if (explorerItemDef==null || explorerItemDef.hasTitle() || explorerItemDef.getModelDefinition() instanceof TitledDefinition==false){
                        return explorerItemDef;
                    }
                    return (TitledDefinition)explorerItemDef.getModelDefinition();
                }
            }case PARAGRAPH:{
                final Id paragraphId = Id.Factory.loadFrom(idPath[0]);
                return defManager.getParagraphDef(paragraphId);
            }
            default:
                return null;
        }        
    }
    
}
