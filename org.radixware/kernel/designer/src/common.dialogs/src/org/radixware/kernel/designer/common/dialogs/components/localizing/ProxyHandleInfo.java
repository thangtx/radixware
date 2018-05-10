package org.radixware.kernel.designer.common.dialogs.components.localizing;

import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ads.localization.AdsMultilingualStringDef;
import org.radixware.kernel.common.defs.localization.IMultilingualStringDef;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.types.Id;

public abstract class ProxyHandleInfo extends HandleInfo {
    
    protected IMultilingualStringDef adsMultilingualStringDef = null;
    private boolean isProxyState = false;
    
    public void loadString(){
        if (!isProxyState){
            IMultilingualStringDef currentString = super.getAdsMultilingualStringDef();
            if (adsMultilingualStringDef == null && currentString != null){
                adsMultilingualStringDef = currentString.cloneString(null);
            }
        } else {
            if (adsMultilingualStringDef == null) {
                updateProxyString();
            }
        }
    }
    
    protected void updateProxyString(){};

    @Override
    public IMultilingualStringDef getAdsMultilingualStringDef() {
        if (updatedProxyState()){
            return adsMultilingualStringDef;
        } else {
            return super.getAdsMultilingualStringDef();
        }
    }

    @Override
    protected void addAdsMultilingualStringDef(IMultilingualStringDef adsMultilingualStringDef) {
        if (updatedProxyState()){
            this.adsMultilingualStringDef = adsMultilingualStringDef;
        } else {
            super.addAdsMultilingualStringDef(adsMultilingualStringDef);
        }
    }

    @Override
    public void removeAdsMultilingualStringDef() {
        adsMultilingualStringDef = null;
        super.removeAdsMultilingualStringDef();
    }

    public abstract boolean isProxyState();
    
    public boolean updatedProxyState(){
        boolean realProxyState = isProxyState();
        if (realProxyState != isProxyState){
            isProxyState = realProxyState;
            loadString();
            
        }
        return isProxyState;
    }
    
    protected void onLanguagesPatternChange(EIsoLanguage language, String newStringValue) {
        if (updatedProxyState() && adsMultilingualStringDef != null) {
            adsMultilingualStringDef.setValue(language, newStringValue);
        }
    }
    
    public abstract boolean commit();
}
