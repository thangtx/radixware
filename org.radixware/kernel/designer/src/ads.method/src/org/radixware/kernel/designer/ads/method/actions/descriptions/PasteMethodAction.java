package org.radixware.kernel.designer.ads.method.actions.descriptions;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.List;
import javax.swing.AbstractAction;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlOptions;
import org.openide.actions.PasteAction;
import org.openide.util.Exceptions;
import org.openide.util.actions.SystemAction;
import org.radixware.kernel.common.defs.ExtendableDefinitions;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodParameters;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodThrowsList;
import org.radixware.kernel.common.defs.ads.clazz.members.MethodParameter;
import org.radixware.kernel.common.defs.ads.clazz.members.MethodReturnValue;
import org.radixware.kernel.common.defs.ads.localization.AdsLocalizingBundleDef;
import org.radixware.kernel.common.defs.ads.localization.AdsMultilingualStringDef;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.defs.localization.IMultilingualStringDef;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.designer.common.dialogs.utils.ClipboardUtils;
import org.radixware.schemas.adsdef.LocalizedString;
import org.radixware.schemas.adsdef.MethodDescriptionInfo;
import org.radixware.schemas.adsdef.MethodDescriptionInfo.ExceptionsDescription.ExceptionDescription;

public class PasteMethodAction extends AbstractAction{
    final AdsMethodDef method;

    public PasteMethodAction(AdsMethodDef method) {
        super("Paste descriptions", SystemAction.get(PasteAction.class).getIcon());
        this.method = method;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
//        ClipboardUtils.paste(new MethodDescriptionInfo(method), null);
        beforePaste();
        Clipboard c = ClipboardUtils.getClipboard();
        if (c.isDataFlavorAvailable(DataFlavor.stringFlavor)) {
            try {
                String text = (String) c.getData(DataFlavor.stringFlavor);
                org.radixware.schemas.adsdef.MethodDescriptionInfo descriptionInfo = org.radixware.schemas.adsdef.MethodDescriptionInfo.Factory.parse(text);

                if (descriptionInfo.isSetMethodDescription()) {
                    org.radixware.schemas.adsdef.MethodDescriptionInfo.MethodDescription methodDescription = descriptionInfo.getMethodDescription();
                    if (methodDescription.isSetString()) {
                        if (method.isDescriptionInherited()) {
                           method.setDescriptionInherited(false);
                        }
                        method.setDescriptionId(updateString(methodDescription.getString(), method.getDescriptionId()));
                        if (method.getDescription() != null && !method.getDescription().isEmpty()) {
                            method.setDescription("");
                        }
                    }
                }

                AdsMethodDef.Profile profile = method.getProfile();
                
                MethodReturnValue returnValue = profile.getReturnValue();
                if (returnValue != null && !returnValue.getType().isVoid()){
                    if (descriptionInfo.isSetReturnTypeDescription()) {
                        org.radixware.schemas.adsdef.MethodDescriptionInfo.ReturnTypeDescription returnTypeDescription = descriptionInfo.getReturnTypeDescription();
                        if (returnTypeDescription.isSetString()) {
                            if (returnValue.isDescriptionInherited()) {
                                returnValue.setDescriptionInherited(false);
                            }
                            returnValue.setDescriptionId(updateString(returnTypeDescription.getString(), returnValue.getDescriptionId()));
                            if (returnValue.getDescription() != null && !returnValue.getDescription().isEmpty()) {
                                returnValue.setDescription("");
                            }
                        }
                    }
                }

                AdsMethodParameters methodParameters = profile.getParametersList();
                if (descriptionInfo.isSetParametersDescription()) {
                    List<org.radixware.schemas.adsdef.MethodDescriptionInfo.ParametersDescription.ParameterDescription> parametersDescription = descriptionInfo.getParametersDescription().getParameterDescriptionList();
                    for (int i = 0; i < methodParameters.size() && i < parametersDescription.size(); i++) {
                        MethodParameter p = methodParameters.get(i);
                        org.radixware.schemas.adsdef.MethodDescriptionInfo.ParametersDescription.ParameterDescription parameterDescription = parametersDescription.get(i);
                        if (parameterDescription.isSetString()) {
                            if (p.isDescriptionInherited()) {
                                p.setDescriptionInherited(false);
                            }
                            p.setDescriptionId(updateString(parameterDescription.getString(), p.getDescriptionId()));
                            if (p.getDescription() != null && !p.getDescription().isEmpty()) {
                                p.setDescription("");
                            }
                        }
                    }
                }
                
                AdsMethodThrowsList methodThrowsList = profile.getThrowsList();
                if (descriptionInfo.isSetExceptionsDescription()) {
                    List<ExceptionDescription> exceptionDescriptions = descriptionInfo.getExceptionsDescription().getExceptionDescriptionList();
                    for (int i = 0; i < methodThrowsList.size(); i++) {
                        AdsMethodThrowsList.ThrowsListItem item = methodThrowsList.get(i);
                        
                        ExceptionDescription currentExceptionDescription = null;
                        if (item.getException() != null) {
                            for (int j = 0; j < exceptionDescriptions.size(); j++) {
                                ExceptionDescription exceptionDescription = exceptionDescriptions.get(j);
                                AdsTypeDeclaration another = AdsTypeDeclaration.Factory.loadFrom(exceptionDescription);
                                if (item.getException().equalsTo(method, another)) {
                                    currentExceptionDescription = exceptionDescription;
                                    break;
                                }
                            }
                        }
                     
                        if (currentExceptionDescription != null) {
                            if (currentExceptionDescription.isSetString()) {
                                item.setDescriptionId(updateString(currentExceptionDescription.getString(), item.getDescriptionId()));
                                if (item.getDescription() != null && !item.getDescription().isEmpty()){
                                    item.setDescription("");
                                }
                            }
                        }

                    }
                }

            } catch (UnsupportedFlavorException | IOException | XmlException ex) {
                Exceptions.printStackTrace(ex);
            }

            afterPaste();
        }
    }
    
    protected void beforePaste(){
    }
   
    protected void afterPaste() {
    }

    public boolean canPaste() {
        Clipboard c = ClipboardUtils.getClipboard();
        if (c.isDataFlavorAvailable(DataFlavor.stringFlavor)) {

            String text = null;
            try {
                text = (String) c.getData(DataFlavor.stringFlavor);
            } catch (UnsupportedFlavorException | IOException ex) {
                Exceptions.printStackTrace(ex);
            }
            
            if (text != null){
                try {
                    org.radixware.schemas.adsdef.MethodDescriptionInfo descriptionInfo = org.radixware.schemas.adsdef.MethodDescriptionInfo.Factory.parse(text, new XmlOptions().setValidateOnSet());
                    return descriptionInfo.validate();
                } catch (XmlException ex) {
                }
            }
            
        }
        return false;
    }
    
    private Id updateString(LocalizedString src, Id currentDescriptionId) {
        AdsMultilingualStringDef description = AdsMultilingualStringDef.Factory.loadFrom(src);
        AdsLocalizingBundleDef adsLocalizingBundleDef = method.findLocalizingBundle();
        
        AdsMultilingualStringDef currentDescription = null;
        if (currentDescriptionId != null){
            currentDescription = adsLocalizingBundleDef.findLocalizedString(currentDescriptionId);
        }
        if (currentDescription != null){
            for (IMultilingualStringDef.StringStorage storage : description.getValues(ExtendableDefinitions.EScope.LOCAL)){
                if (!storage.getValue().equals(currentDescription.getValue(storage.getLanguage()))){
                    currentDescription.setValue(storage.getLanguage(), storage.getValue());
                }
            }
            return currentDescriptionId;
        } else {
            IMultilingualStringDef adsMultilingualStringDef = AdsMultilingualStringDef.Factory.newInstance(description);
            adsLocalizingBundleDef.getStrings().getLocal().add((AdsMultilingualStringDef) adsMultilingualStringDef);
            return adsMultilingualStringDef.getId();
        }
    }


}
