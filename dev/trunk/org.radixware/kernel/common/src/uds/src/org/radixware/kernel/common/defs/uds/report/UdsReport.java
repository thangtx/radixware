package org.radixware.kernel.common.defs.uds.report;

import java.io.File;
import java.io.IOException;
import org.radixware.kernel.common.defs.ClipboardSupport;
import org.radixware.kernel.common.defs.uds.UdsLocalizingBundleDef;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportClassDef;
import org.radixware.kernel.common.defs.ads.localization.AdsLocalizingBundleDef;
import org.radixware.kernel.common.defs.uds.IInnerLocalizingDef;
import org.radixware.kernel.common.defs.uds.IUdsFileDefinition;
import org.radixware.kernel.common.enums.ENamingPolicy;
import org.radixware.schemas.adsdef.UserReportDefinitionType;

public class UdsReport extends AdsReportClassDef implements IInnerLocalizingDef{
    private final boolean isSetActivateVersionAfterImport;
    private final boolean activateVersionAfterImport;
    private final AdsLocalizingBundleDef innerBundle;

    public UdsReport(UserReportDefinitionType xDef) {
        super(xDef.getReport());
        if (isSetActivateVersionAfterImport = xDef.isSetActivateVersionAfterImport()){
            activateVersionAfterImport = xDef.getActivateVersionAfterImport();
        } else {
            activateVersionAfterImport = false;
        }
        innerBundle = new UdsLocalizingBundleDef(getId(), this);
        if (xDef.getStrings() != null) {
            innerBundle.loadStrings(xDef.getStrings());
        }
    }
    
    public UdsReport(UserReportDefinitionType xDef, RadixObject container) {
        this(xDef);
        setContainer(container);
    }


    public void appendTo(UserReportDefinitionType xmlObject) {
        UserReportDefinitionType reportDef = (UserReportDefinitionType) xmlObject;
        UserReportDefinitionType current = UserReportDefinitionType.Factory.newInstance();
        this.appendTo(current.addNewReport(), AdsDefinition.ESaveMode.NORMAL);
        AdsLocalizingBundleDef sb = findExistingLocalizingBundle();
        if (sb != null) {
            sb.appendTo(current.addNewStrings(), AdsDefinition.ESaveMode.NORMAL);
        }

        if (isSetActivateVersionAfterImport) {
            current.setActivateVersionAfterImport(activateVersionAfterImport);
        }
        reportDef.set(current);
    }

    @Override
    public void visitChildren(IVisitor visitor, VisitorProvider provider) {
        super.visitChildren(visitor, provider);
        innerBundle.visit(visitor, provider);
    }

    @Override
    public AdsLocalizingBundleDef findLocalizingBundleImpl(boolean createIfNotExists) {
        return innerBundle;
    }

    @Override
    public boolean isSaveable() {
        return false;
    }

    @Override
    public void save() throws IOException {
        RadixObject ro = getContainer();
        if (ro != null){
            ro.save();
        }
    }

    @Override
    public ENamingPolicy getNamingPolicy() {
        return ENamingPolicy.CONST;
    }

    public static String getCLASS_BODY_NAME() {
        return CLASS_BODY_NAME;
    }

    @Override
    public ClipboardSupport<? extends AdsClassDef> getClipboardSupport() {
        return null;
    }

    @Override
    public File getFile() {
        RadixObject owner = getContainer();
        if (owner != null) {
            return owner.getFile();
        } else {
            return null;
        }
    }

    @Override
    public boolean delete() {
        return false;
    }

    @Override
    public boolean canDelete() {
        return super.canDelete() && !(getContainer() instanceof IUdsFileDefinition);
    }
    
    
    
    
}
