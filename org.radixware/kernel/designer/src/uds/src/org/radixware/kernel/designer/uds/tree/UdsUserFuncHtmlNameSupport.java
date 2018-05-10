package org.radixware.kernel.designer.uds.tree;


import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodDef;
import org.radixware.kernel.common.defs.uds.userfunc.UdsUserFuncDef;
import org.radixware.kernel.designer.common.annotations.registrators.HtmlNameSupportFactoryRegistration;
import org.radixware.kernel.designer.common.general.displaying.HtmlNameSupport;
import org.radixware.kernel.designer.common.general.displaying.HtmlNameSupportsManager;
import org.radixware.kernel.designer.common.general.displaying.IHtmlNameSupportFactory;

public class UdsUserFuncHtmlNameSupport extends HtmlNameSupport {

        private UdsUserFuncDef obj;
        HtmlNameSupport profileNameSupport;

        public UdsUserFuncHtmlNameSupport(UdsUserFuncDef obj) {
            super(obj);
            this.obj = obj;
        }

        @Override
        public String getDisplayName() {
            if (obj != null) {
                if (profileNameSupport != null) {
                    return profileNameSupport.getDisplayName();
                }
//                AdsMethodDef def = obj.findMethod();
//                if (def != null){
//                    profileNameSupport = HtmlNameSupportsManager.newInstance(def);
//                    return profileNameSupport.getDisplayName();
//                }
            }
            return super.getDisplayName();
        }
    
    @HtmlNameSupportFactoryRegistration
    public static final class Factory implements IHtmlNameSupportFactory {

        /**
         * Registeren in layer.xml
         */
        public Factory() {
        }

        @Override
        public HtmlNameSupport newInstance(RadixObject object) {
            return new UdsUserFuncHtmlNameSupport((UdsUserFuncDef) object);
        }
    }
}