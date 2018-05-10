package org.radixware.kernel.common.defs.uds.userfunc;

import java.io.File;
import java.io.IOException;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.check.RadixProblem;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodDef;
import org.radixware.kernel.common.defs.ads.localization.AdsLocalizingBundleDef;
import static org.radixware.kernel.common.defs.ads.userfunc.UdsUtils.*;
import org.radixware.kernel.common.defs.ads.userfunc.xml.UserFuncImportInfo;
import org.radixware.kernel.common.defs.uds.IUdsFileDefinition;
import org.radixware.kernel.common.defs.uds.files.UdsXmlFile;
import org.radixware.kernel.common.enums.ENamingPolicy;
import org.radixware.schemas.adsdef.LocalizingBundleDefinition;
import org.radixware.schemas.xscml.JmlType;

public class UdsDummyUserFuncDef extends UdsUserFuncDef implements IUdsFileDefinition{
    private final String xPath;
    
    public UdsDummyUserFuncDef(UserFuncImportInfo userFuncImportInfo, RadixObject owner) {
        super(userFuncImportInfo, false);
        if (owner != null){
            setContainer(owner);
        }
        xPath = userFuncImportInfo.getXPath();
    }

    @Override
    public File getFile() {
        UdsXmlFile file = getOwnerUdsFile();
        if (file != null){
            return file.getFile();
        }
        return null;
    }
    
    
    
    @Override
    public boolean isSaveable() {
        return false;
    }

    @Override
    public String getXPath() {
        return xPath;
    }

    @Override
    public void appendTo(XmlObject xmlObject) {
        XmlObject[] jmlTypes = xmlObject.selectChildren(JAVA_SRC_QNAME);
        if (jmlTypes.length > 0) {
            XmlObject jmlXmlObject = jmlTypes[0];
            JmlType newJmlType = JmlType.Factory.newInstance();
            getSource().appendTo(newJmlType, ESaveMode.NORMAL);
            jmlXmlObject.set(newJmlType);
        }
        AdsLocalizingBundleDef bundle = findExistingLocalizingBundle();
        XmlObject[] bundles = xmlObject.selectChildren(STRINGS_QNAME);
        if (bundle != null){
            LocalizingBundleDefinition strings = LocalizingBundleDefinition.Factory.newInstance();
            bundle.appendTo(strings, ESaveMode.NORMAL);
            if (bundles.length == 0 && strings.getStringList() != null && !strings.getStringList().isEmpty()) {
               
                XmlCursor cur = null;
                try{
                    cur = xmlObject.newCursor();
                    cur.toLastChild();
                    cur.toEndToken();
                    cur.toNextToken();
                    cur.insertElement(STRINGS_QNAME);
                    bundles = xmlObject.selectChildren(STRINGS_QNAME);
                } finally {
                    if (cur != null) cur.dispose();
                }
            }
            
            if (bundles.length > 0) {
                XmlObject bundleXmlObject = bundles[0];
                bundleXmlObject.set(strings);
            }
        }
    }

    public UdsXmlFile getOwnerUdsFile(){
        for (RadixObject owner = getContainer(); owner != null; owner = owner.getContainer()) {
            if (owner instanceof UdsXmlFile) {
                return (UdsXmlFile) owner;
            }
        }
        return null;
    }
    
    @Override
    public void save() throws IOException {
        UdsXmlFile ownerDefinition = getOwnerUdsFile();
        if (ownerDefinition != null) {
            ownerDefinition.save();
        }
    }

    @Override
    public RadixProblem.WarningSuppressionSupport getWarningSuppressionSupport(boolean createIfAbsent) {
        return null;
    }

    @Override
    public ENamingPolicy getNamingPolicy() {
        return ENamingPolicy.CALC;
    }

    @Override
    public String getName() {
        checkName();
        return super.getName();
    }
    
    public void checkName() {
        if (isInBranch()) {
            String name = super.getName();
            if (name == null || name.isEmpty()) {
                AdsMethodDef method = findMethod();
                if (method != null) {
                    setName(method.getName());
                }
            }
        }
    }

}
