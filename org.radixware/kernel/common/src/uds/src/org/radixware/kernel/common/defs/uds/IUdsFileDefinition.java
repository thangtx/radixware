package org.radixware.kernel.common.defs.uds;

import java.io.File;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.defs.uds.files.UdsXmlFile;

public interface IUdsFileDefinition {
    public UdsXmlFile getOwnerUdsFile();
    
    public String getXPath();
    
    public void appendTo(XmlObject xmlObject);
    
    public File getFile();
    
}
