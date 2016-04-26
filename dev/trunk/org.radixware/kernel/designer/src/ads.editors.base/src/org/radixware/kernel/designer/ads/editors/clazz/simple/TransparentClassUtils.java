
package org.radixware.kernel.designer.ads.editors.clazz.simple;

import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.defs.ads.platform.IPlatformClassPublisher;
import org.radixware.kernel.common.defs.ads.type.AdsTransparence;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.repository.ads.AdsSegment;

 final class TransparentClassUtils {

    public static final AdsClassDef findTransparentSuperClass(AdsClassDef adsClassDef){
        if (!AdsTransparence.isTransparent(adsClassDef)) {
            return null;
        }
        
        AdsTypeDeclaration superDeclaration = getTransparentSuperClassRef(adsClassDef);
        
        return findTransparentByType(adsClassDef,superDeclaration);
    }
    
    public static final AdsClassDef findTransparentByType(AdsClassDef adsClassDef,AdsTypeDeclaration type){
        if (!AdsTransparence.isTransparent(adsClassDef)) {
            return null;
        }
        
        if (type == null){
            return null;
        }
        
        AdsSegment segment = getSegment(adsClassDef);
        if (segment == null) {
            return null;
        }
        
        IPlatformClassPublisher publisher = segment.getBuildPath().getPlatformPublishers().findPublisherByName(type.getExtStr());
        if (publisher == null) {
            return null;
        } else if (publisher instanceof AdsClassDef) {
            AdsTransparence t = ((AdsClassDef) publisher).getTransparence();
                if (t != null && t.isTransparent()) {
                    return (AdsClassDef) publisher;
                }
        }
        
        return null;
    }
    
    public static final AdsTypeDeclaration getTransparentSuperClassRef(AdsClassDef adsClassDef){
        if (!AdsTransparence.isTransparent(adsClassDef)) {
            return null;
        }

        AdsSegment segment = getSegment(adsClassDef);
        if (segment == null) {
            return null;
        }
        
        return segment.getBuildPath().getPlatformClassBaseClass(adsClassDef.getTransparence().getPublishedName(), adsClassDef.getUsageEnvironment());
        
    }
    
    private static AdsSegment getSegment(AdsClassDef adsClassDef){
        AdsModule module = adsClassDef.getModule();
        if (module == null) {
            return null;
        }
        
        AdsSegment segment = (AdsSegment) module.getSegment();
        return segment;
    }
    
}
