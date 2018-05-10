package org.radixware.kernel.common.defs.uds.userfunc;

import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.userfunc.xml.UserFuncImportInfo;
import org.radixware.kernel.common.defs.ads.userfunc.xml.UserFuncLibInfo;
import org.radixware.kernel.common.defs.uds.IUdsFileDefinition;
import org.radixware.kernel.common.defs.uds.UdsDefinitionIcon;
import org.radixware.kernel.common.defs.uds.files.UdsFileRadixObjects;
import org.radixware.kernel.common.defs.uds.files.UdsXmlFile;
import org.radixware.kernel.common.resources.icons.RadixIcon;

public class UdsUserFuncLib extends UdsFileRadixObjects implements IUdsFileDefinition{
    private final String xPath;

    protected UdsUserFuncLib(UserFuncLibInfo libInfo) {
        super(libInfo.getName());
        this.xPath = libInfo.getXPath();
        for (UserFuncImportInfo info : libInfo.getUserFuncs()) {
            UdsDummyUserFuncDef def = UdsUserFuncDef.Factory.loadFrom((UserFuncImportInfo) info, (RadixObject) this);
            if (def != null) {
                if (def.getXPath() == null || findByXPath(def.getXPath()) == null) {
                    add(def);
                }
            }
        }
    }

    @Override
    public UdsXmlFile getOwnerUdsFile() {
        for (RadixObject owner = getContainer(); owner != null; owner = owner.getContainer()) {
            if (owner instanceof UdsXmlFile) {
                return (UdsXmlFile) owner;
            }
        }
        return null;
    }

    @Override
    public String getXPath() {
        return xPath;
    }

    @Override
    public RadixIcon getIcon() {
        return UdsDefinitionIcon.USERLIB;
    }

    @Override
    public void appendTo(XmlObject xmlObject) {
        if (!isEmpty()) {
            for (RadixObject def : this) {
                if (def instanceof IUdsFileDefinition) {
                    IUdsFileDefinition uf = (IUdsFileDefinition) def;
                    XmlObject[] objects = xmlObject.selectPath(uf.getXPath());
                    if (objects != null && objects.length > 0) {
                        ((IUdsFileDefinition) def).appendTo(objects[0]);
                    }
                }
            }
        }
    }
    
    public static final class Factory {
        public static UdsUserFuncLib loadFrom(UserFuncLibInfo xDef){
            return new UdsUserFuncLib(xDef);
        }
    }
    
}
