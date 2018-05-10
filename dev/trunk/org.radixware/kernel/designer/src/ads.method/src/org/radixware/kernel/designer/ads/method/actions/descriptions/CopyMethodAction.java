package org.radixware.kernel.designer.ads.method.actions.descriptions;

import java.awt.datatransfer.Clipboard;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.openide.actions.CopyAction;
import org.openide.util.actions.SystemAction;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodParameters;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodThrowsList;
import org.radixware.kernel.common.defs.ads.clazz.members.MethodParameter;
import org.radixware.kernel.common.defs.ads.clazz.members.MethodReturnValue;
import org.radixware.kernel.common.defs.ads.localization.AdsMultilingualStringDef;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.designer.common.dialogs.utils.ClipboardUtils;
import static org.radixware.kernel.designer.common.dialogs.utils.ClipboardUtils.getClipboard;
import org.radixware.schemas.adsdef.LocalizedString;

public class CopyMethodAction extends AbstractAction{
    final AdsMethodDef method;
    public CopyMethodAction(AdsMethodDef method) {
        super("Copy descriptions", SystemAction.get(CopyAction.class).getIcon());
        this.method = method;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        beforeCopy();
//        MethodDescriptionInfo descriptionInfo = new MethodDescriptionInfo(method);
//        ClipboardUtils.copyToClipboard(descriptionInfo);
        
        org.radixware.schemas.adsdef.MethodDescriptionInfo descriptionInfo = org.radixware.schemas.adsdef.MethodDescriptionInfo.Factory.newInstance();
        Id descriptionId = method.getDescriptionId();
        if (!method.isDescriptionInherited() && descriptionId != null){
            AdsMultilingualStringDef string = method.findLocalizedString(descriptionId);
            if (string != null){
                string.appendTo(descriptionInfo.addNewMethodDescription().addNewString());
            }
        }
        
        AdsMethodDef.Profile profile = method.getProfile();
        
        MethodReturnValue methodReturnValue = profile.getReturnValue();
        if (methodReturnValue != null && !methodReturnValue.getType().isVoid()){
            descriptionId = methodReturnValue.getDescriptionId();
            if (descriptionId != null ){
                AdsMultilingualStringDef string = method.findLocalizedString(descriptionId);
                if (string != null){
                    string.appendTo(descriptionInfo.addNewReturnTypeDescription().addNewString());
                }
            }
        }
        
        AdsMethodParameters methodParameters = profile.getParametersList();
        if (!methodParameters.isEmpty()) {
            org.radixware.schemas.adsdef.MethodDescriptionInfo.ParametersDescription parametersDescription = descriptionInfo.addNewParametersDescription();
            for (MethodParameter adsMethodParameter : methodParameters) {
                org.radixware.schemas.adsdef.MethodDescriptionInfo.ParametersDescription.ParameterDescription parameterDescription = parametersDescription.addNewParameterDescription();
                descriptionId = adsMethodParameter.getDescriptionId();
                if (descriptionId != null) {
                    AdsMultilingualStringDef string = method.findLocalizedString(descriptionId);
                    if (string != null) {
                        string.appendTo(parameterDescription.addNewString());
                    }
                }
            }
        }
        
        AdsMethodThrowsList adsMethodThrowsList = profile.getThrowsList();
        if (!adsMethodThrowsList.isEmpty()) {
            org.radixware.schemas.adsdef.MethodDescriptionInfo.ExceptionsDescription thrownExceptionsDescription = descriptionInfo.addNewExceptionsDescription();
            for (AdsMethodThrowsList.ThrowsListItem throwsListItem : adsMethodThrowsList) {
                org.radixware.schemas.adsdef.MethodDescriptionInfo.ExceptionsDescription.ExceptionDescription exceptionDescription = thrownExceptionsDescription.addNewExceptionDescription();
                AdsTypeDeclaration typeDeclaration = throwsListItem.getException();
                if (typeDeclaration != null){
                    typeDeclaration.appendTo(exceptionDescription);
                    descriptionId = throwsListItem.getDescriptionId();
                    if (descriptionId != null) {
                        AdsMultilingualStringDef string = method.findLocalizedString(descriptionId);
                        if (string != null) {
                            string.appendTo(exceptionDescription.addNewString());
                        }
                    }
                }
            }
        }
        
        
        ClipboardUtils.copyToClipboard(descriptionInfo.xmlText());
        afterCopy();
    }
    
    protected void beforeCopy(){
    }
    
    protected void afterCopy(){
    }

}
