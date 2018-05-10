/*
 * Copyright (c) 2008-2015, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. This Source Code is distributed
 * WITHOUT ANY WARRANTY; including any implied warranties but not limited to
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * Mozilla Public License, v. 2.0. for more details.
 */
package org.radixware.kernel.common.defs.ads.clazz.sql.report.preview;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.enums.EPortSecurityProtocol;
import org.radixware.kernel.common.exceptions.ServiceCallException;
import org.radixware.kernel.common.exceptions.ServiceCallFault;
import org.radixware.kernel.common.exceptions.ServiceCallTimeout;
import org.radixware.kernel.common.sc.SapClientOptions;
import org.radixware.kernel.common.sc.ServiceClient;
import org.radixware.kernel.common.trace.LocalTracer;
import org.radixware.kernel.common.trace.TraceItem;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.FileUtils;
import org.radixware.kernel.common.utils.ValueFormatter;
import org.radixware.kernel.common.utils.net.SapAddress;
import org.radixware.schemas.types.BinBase64;

public class AasReportPreviewServiceClient {

    private static final String SERVICE_WSDL = "http://schemas.radixware.org/aas.wsdl";
    private static final String REPORT_ID_MARKER = "REPORT_ID_MARKER";
    private static final String REPORT_LAYER_MARKER = "REPORT_LAYER_MARKER";
    private static final String EXPORT_FORMAT_MARKER = "EXPORT_FORMAT_MARKER";
    private static final String REPORT_JAR_MARKER = "REPORT_JAR_MARKER";
    private static final String TEST_DATA_ZIP_MARKER = "TEST_DATA_ZIP_MARKER";
    private static final String EXPORT_LOCALE_MARKER = "EXPORT_LOCALE_MARKER";
    private static final String TEMPLATE_FILE_NAME = "ReportPreviewAasRequest.template";

    private final String aasAddress;
    private final ServiceClient aasClient;
    private final String requestTemplate;
    private final int requestTimeout;

    public AasReportPreviewServiceClient(String aasAddress, int requestTimeout) throws IOException {
        this.aasAddress = aasAddress;
        this.requestTimeout = requestTimeout;

        final LocalTracer localTracer = new LocalTracer() {
            @Override
            public void put(EEventSeverity severity, String localizedMess, String code, List<String> words, boolean isSensitive) {
                if (EEventSeverity.NONE.getValue() == getMinSeverity()) {
                    return;
                }
                if (words == null || words.isEmpty()) {
                    words = Collections.singletonList(localizedMess);
                }
                Logger.getLogger(AasReportPreviewServiceClient.class.getName()).log(Level.INFO, new TraceItem(null, severity, code, words, EEventSource.AAS_CLIENT.getValue(), isSensitive).toString());
            }

            @Override
            public long getMinSeverity() {
                //disabled by default
                return EEventSeverity.NONE.getValue();
            }

            @Override
            public long getMinSeverity(String eventSource) {
                return getMinSeverity();
            }
        };

        aasClient = new ServiceClient(localTracer, null) {

            @Override
            protected List<SapClientOptions> refresh(Long systemId, Long currentInstanceId, String serviceUri) throws ServiceCallException {
                final SapClientOptions sap = new SapClientOptions();
                sap.setName("ReportPreviewSap");
                sap.setAddress(new SapAddress(ValueFormatter.parseInetSocketAddress(AasReportPreviewServiceClient.this.aasAddress)));
                sap.setSecurityProtocol(EPortSecurityProtocol.NONE);
                sap.setPriority(50);
                return Collections.singletonList(sap);
            }
        };
        requestTemplate = FileUtils.readTextStream(AasReportPreviewServiceClient.class.getResourceAsStream(TEMPLATE_FILE_NAME), "UTF-8");
    }
    
    public byte[] preview(Id reportId, String reportLayerUri, String exportFormat, byte[] reportJar, byte[] testDataZip, String exportLocale) throws ServiceCallException, ServiceCallTimeout, ServiceCallFault, InterruptedException  {
        BinBase64 reportJarBytes = BinBase64.Factory.newInstance();
        reportJarBytes.setByteArrayValue(reportJar);

        BinBase64 testDataZipBytes = BinBase64.Factory.newInstance();
        testDataZipBytes.setByteArrayValue(testDataZip);

        String rqStr = requestTemplate.replaceFirst(REPORT_ID_MARKER, reportId.toString());
        rqStr = rqStr.replaceFirst(REPORT_LAYER_MARKER, reportLayerUri);
        rqStr = rqStr.replaceFirst(EXPORT_FORMAT_MARKER, exportFormat);
        rqStr = rqStr.replaceFirst(REPORT_JAR_MARKER, reportJarBytes.stringValue());
        rqStr = rqStr.replaceFirst(TEST_DATA_ZIP_MARKER, testDataZip == null ? "" : testDataZipBytes.stringValue());
        rqStr = rqStr.replaceFirst(EXPORT_LOCALE_MARKER, exportLocale);
        
        try {
            XmlObject rq = XmlObject.Factory.parse(rqStr);            
            XmlObject rs = aasClient.invokeService(rq, XmlObject.class, Long.valueOf(1), null, SERVICE_WSDL, 0, requestTimeout, null);
            
            XmlCursor c = rs.newCursor();
            try {
                //Same as xpath: $this//aas:ReturnValue/aas:Str";
                c.toFirstChild();
                c.toFirstChild();

                String rsStr = c.getTextValue();
                BinBase64 resultBytes = BinBase64.Factory.newInstance();
                resultBytes.set(rsStr);
                
                return resultBytes.getByteArrayValue();
            } finally {
                c.dispose();
            }
        } catch (XmlException ex) {
            Logger.getLogger(AasReportPreviewServiceClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new byte[0];
    }

}
