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

package org.radixware.kernel.common.msdl;

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
import org.radixware.kernel.common.exceptions.SmioException;
import org.radixware.kernel.common.sc.SapClientOptions;
import org.radixware.kernel.common.trace.LocalTracer;
import org.radixware.kernel.common.trace.TraceItem;
import org.radixware.kernel.common.utils.FileUtils;
import org.radixware.kernel.common.utils.Hex;
import org.radixware.kernel.common.utils.ValueFormatter;
import org.radixware.kernel.common.utils.net.SapAddress;
import org.radixware.schemas.types.BinBase64;

/**
 *
 * @author npopov
 */
public class MsdlPreprocessorAccessAas implements IMsdlPreprocessorAccess {

    private final String preprocessorClassGuid;
    private final String aasAddress;
    private final org.radixware.kernel.common.sc.ServiceClient aasClient;
    private final String requestTemplate;

    private static final String TEMPLATE_FILE_NAME = "MsdlPreprocessorAasRequest.template";
    private static final String CLASS_NAME_MARKER = "CLASS_NAME_MARKER";
    private static final String METHOD_NAME_MARKER = "METHOD_NAME_MARKER";
    private static final String REMOTE_METHOD_MARKER = "REMOTE_METHOD_MARKER";
    private static final String READ_METHOD_GUID = "mthDUCQ4NV2P5G6XLBKMLQ44OY5UY";
    private static final String WRITE_METHOD_GUID = "mthNNKFBQMVKFG5PHHK5DYXMUV36I";
    private static final String SELECT_METHOD_GUID = "mthNHCMHV3TL5FZ5CZSG25QLFHYBM";
    private static final String BIN_DATA_MARKER = "BIN_DATA_MARKER";
    private static final String SERVICE_WSDL = "http://schemas.radixware.org/aas.wsdl";
    private static final int INVOKE_TIMEOUT_SEC = 5;

    public MsdlPreprocessorAccessAas(String preprocessorClassGuid, String aasAddress) throws IOException {
        this.preprocessorClassGuid = preprocessorClassGuid;
        this.aasAddress = aasAddress;

        final org.radixware.kernel.common.trace.LocalTracer localTracer = new LocalTracer() {
            @Override
            public void put(EEventSeverity severity, String localizedMess, String code, List<String> words, boolean isSensitive) {
                if (EEventSeverity.NONE.getValue() == getMinSeverity()) {
                    return;
                }
                if (words == null || words.isEmpty()) {
                    words = Collections.singletonList(localizedMess);
                }
                Logger.getLogger(MsdlPreprocessorAccessAas.class.getName()).log(Level.INFO, new TraceItem(null, severity, code, words, EEventSource.AAS_CLIENT.getValue(), isSensitive).toString());
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

        aasClient = new org.radixware.kernel.common.sc.ServiceClient(localTracer, null) {
            @Override
            protected List<org.radixware.kernel.common.sc.SapClientOptions> refresh(Long systemId, final Long thisInstanceId, String serviceUri) throws ServiceCallException {
                final SapClientOptions sap = new SapClientOptions();
                sap.setName("MsdlPreprocessorSap");
                sap.setAddress(new SapAddress(ValueFormatter.parseInetSocketAddress(MsdlPreprocessorAccessAas.this.aasAddress)));
                sap.setSecurityProtocol(EPortSecurityProtocol.NONE);
                sap.setPriority(50);
                return Collections.singletonList(sap);
            }
        };

        requestTemplate = FileUtils.readTextStream(MsdlPreprocessorAccessAas.class.getResourceAsStream(TEMPLATE_FILE_NAME), "UTF-8");
    }

    @Override
    public byte[] read(String methodName, byte[] arg) throws SmioException {
        try {
            return Hex.decode(invokeRemote(READ_METHOD_GUID, methodName, arg));
        } catch (Throwable ex) {
            throw new SmioException("Error on invoke read method", ex);
        }
    }

    @Override
    public byte[] write(String methodName, byte[] arg) throws SmioException {
        try {
            return Hex.decode(invokeRemote(WRITE_METHOD_GUID, methodName, arg));
        } catch (Throwable ex) {
            throw new SmioException("Error on invoke write method", ex);
        }
    }

    @Override
    public String select(String methodName, byte[] arg) throws SmioException {
        try {
            return invokeRemote(SELECT_METHOD_GUID, methodName, arg);
        } catch (Throwable ex) {
            throw new SmioException("Error on invoke select method", ex);
        }
    }

    private String invokeRemote(String remoteMethodName, String methodName, byte[] arg) throws ServiceCallException, XmlException, ServiceCallTimeout, ServiceCallFault, InterruptedException {
        String rqStr = requestTemplate.replaceFirst(CLASS_NAME_MARKER, preprocessorClassGuid);
        rqStr = rqStr.replaceFirst(REMOTE_METHOD_MARKER, remoteMethodName);
        rqStr = rqStr.replaceFirst(METHOD_NAME_MARKER, methodName);
        BinBase64 binData = BinBase64.Factory.newInstance();
        binData.setByteArrayValue(arg);
        rqStr = rqStr.replaceFirst(BIN_DATA_MARKER, binData.stringValue());

        XmlObject rq = XmlObject.Factory.parse(rqStr);
        XmlObject rs = aasClient.invokeService(rq, XmlObject.class, Long.valueOf(1), null, SERVICE_WSDL, 0, INVOKE_TIMEOUT_SEC, null);
        XmlCursor c = rs.newCursor();
        try {
            //Same as xpath: $this//aas:ReturnValue/aas:Str";
            c.toFirstChild();
            c.toFirstChild();
            return c.getTextValue();
        } finally {
            c.dispose();
        }
    }

}
