package org.radixware.kernel.designer.environment.upload;

import java.io.File;
import java.io.IOException;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.uds.report.UdsExchangeReport;

public class UdsReportUploder extends RadixObjectUploader<UdsExchangeReport>{

    public UdsReportUploder(UdsExchangeReport radixObject) {
        super(radixObject);
    }

    @Override
    public void close() {
        UdsExchangeReport report = getRadixObject();
        if (report.getXPath() != null) {
            return;
        }
        UdsUploaderUtils.close(report);
    }

    @Override
    public void updateChildren() {
    }

    @Override
    public void reload() throws IOException {
        UdsUploaderUtils.reload(getRadixObject());
    }

    @Override
    public long getRememberedFileTime() {
        return getRadixObject().getFileLastModifiedTime();
    }

    @Override
    public RadixObject uploadChild(File file) throws IOException {
        return null;
    }
    
}
