package org.radixware.kernel.common.defs.uds.report;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.userfunc.xml.UserReportInfo;
import org.radixware.kernel.common.defs.uds.IUdsDirectoryRadixObject;
import org.radixware.kernel.common.defs.uds.IUdsFileDefinition;
import org.radixware.kernel.common.defs.uds.files.UdsFileRadixObjects;
import org.radixware.kernel.common.defs.uds.files.UdsXmlFile;
import org.radixware.kernel.common.defs.uds.module.UdsModule;
import org.radixware.kernel.common.exceptions.RadixObjectError;
import org.radixware.kernel.common.utils.FileUtils;
import org.radixware.kernel.common.utils.XmlFormatter;
import org.radixware.schemas.adsdef.AdsUserReportExchangeDocument;
import org.radixware.schemas.adsdef.UserReportDefinitionType;
import org.radixware.schemas.adsdef.UserReportExchangeType;

public class UdsExchangeReport extends UdsFileRadixObjects implements IUdsFileDefinition{
    private final String xPath;
    private final String fileName;
    private long modificationStamp;
    
    public UdsExchangeReport(UserReportInfo info){
        this(info.getType(), null, info.getXPath());
    }
    
    public UdsExchangeReport(UserReportExchangeType xEx, String fileName) {
       this(xEx, fileName, null);
    }

    public UdsExchangeReport(UserReportExchangeType xEx, String fileName, String xPath) {
        super(xEx.getName());
        for (UserReportDefinitionType xReportDef : xEx.getAdsUserReportDefinitionList()) {
            if (xReportDef.getReport() != null) {
                UdsReport report = new UdsReport(xReportDef, this);
                add(report);

            }
        }
        this.xPath = xPath;
        this.fileName = fileName;
    }
    

    public String getFileName() {
        return fileName;
    }

    @Override
    public File getFile() {
        if (xPath != null || fileName == null){
            return super.getFile();
        }
       
        return new File(super.getFile(), fileName);
    }
    
    public IUdsDirectoryRadixObject getOwnerDirectory() {
        for (RadixObject owner = getContainer(); owner != null; owner = owner.getContainer()) {
            if (owner instanceof IUdsDirectoryRadixObject) {
                return (IUdsDirectoryRadixObject) owner;
            }
        }
        return null;
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
    public void appendTo(XmlObject xmlObject) {
        AdsUserReportExchangeDocument xDoc;
        try {
            xDoc = AdsUserReportExchangeDocument.Factory.parse(xmlObject.getDomNode());
        
        UserReportExchangeType xEx = xDoc.getAdsUserReportExchange();
        appendTo(xEx);
        xmlObject.set(xEx);
        } catch (XmlException ex) {
            Logger.getLogger(UdsExchangeReport.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void save() throws IOException {
        UdsXmlFile ownerDefinition = getOwnerUdsFile();
        if (ownerDefinition != null) {
            ownerDefinition.save();
            return;
        }
        
        File file  = getFile();
        if (file == null){
            return;
        }
        AdsUserReportExchangeDocument xDoc;
        try {
            xDoc = AdsUserReportExchangeDocument.Factory.parse(file);
        } catch (XmlException ex) {
            Logger.getLogger(UdsExchangeReport.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
        UserReportExchangeType xEx = xDoc.getAdsUserReportExchange();
        appendTo(xEx);
        XmlFormatter.save(xDoc, file);
        setEditState(EEditState.NONE);
        setFileLastModifiedTime(file.lastModified());
    }
    
    protected void appendTo(UserReportExchangeType xEx) {
        xEx.setName(getName());
        List<UserReportDefinitionType> list = new ArrayList<>();
        for (RadixObject obj : this) {
            UserReportDefinitionType xDef = UserReportDefinitionType.Factory.newInstance();
            UdsReport uf = (UdsReport) obj;
            uf.appendTo(xDef);
            list.add(xDef);
        }
        xEx.assignAdsUserReportDefinitionList(list);
    }

    @Override
    public void visitChildren(IVisitor visitor, VisitorProvider provider) {
        super.visitChildren(visitor, provider);
    }
    
    @Override
    public boolean isSaveable() {
        return xPath == null;
    }

    @Override
    public UdsModule getModule() {
        return (UdsModule) super.getModule();
    }
    
    
    @Override
    public boolean delete() {
        final File file = getFile();

        if (file != null) {
            if (!super.delete()) {
                return false;
            }

            try {
                FileUtils.deleteFileExt(file);
            } catch (IOException cause) {
                throw new RadixObjectError("Unable to delete UDS file.", this, cause);
            }
        }
        return true;
    }
    
    public void setFileLastModifiedTime(long stamp) {
        this.modificationStamp = stamp;
    }

    public long getFileLastModifiedTime() {
        return this.modificationStamp;
    }
    
    @Override
    public boolean canDelete() {
        return super.canDelete();
    }
}
