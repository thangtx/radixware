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
package org.radixware.kernel.server.test;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.exceptions.ServiceCallException;
import org.radixware.kernel.common.exceptions.ServiceCallFault;
import org.radixware.kernel.common.exceptions.ServiceCallTimeout;
import org.radixware.kernel.common.sc.SapClientOptions;
import org.radixware.kernel.common.sc.ServiceClient;
import org.radixware.kernel.common.trace.LocalTracer;
import org.radixware.kernel.common.trace.TraceItem;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.Hex;
import org.radixware.kernel.common.utils.ValueFormatter;
import org.radixware.kernel.common.utils.net.SapAddress;
import org.radixware.kernel.server.arte.services.aas.ArteAccessService;
import org.radixware.schemas.aas.InvokeMess;
import org.radixware.schemas.aas.InvokeRq;
import org.radixware.schemas.aasWsdl.InvokeDocument;

public class IsTestsRemoteRunner {

    private static final int INVOKE_TIMEOUT_SEC = Integer.getInteger("rdx.tests.runner.invoke.timeout.sec", 6 * 60 * 60);

    private static final class RunParams {

        public static final String AAS_ADDRESS = "-aasAddress";
        public static final String RESULT_FILE_NAME = "-resultFileName";
        public static final String TESTCASE_PID = "-testCasePid";
        public static final String USER_NAME = "-userName";
        public static final String RESULT_ON_CLIENT = "-resultOnClient";
    }

    public static void main(String[] args) throws ServiceCallException, ServiceCallTimeout, ServiceCallFault, InterruptedException {
        if (args.length != 8 && args.length != 9) {
            System.out.println("Invalid argument count. Actual: " + args.length + ". Expected: 8 or 9");
            printUsage();
            System.exit(1);
        }

        Map<String, String> params = new HashMap<String, String>();

        int haveResultOnClient = 0;

        for (int i = 0; i < args.length; i++) {
            if (args[i].equals(RunParams.AAS_ADDRESS)) {
                params.put(RunParams.AAS_ADDRESS, args[i + 1]);
                i++;
            }
            if (args[i].equals(RunParams.RESULT_FILE_NAME)) {
                params.put(RunParams.RESULT_FILE_NAME, args[i + 1]);
                i++;
            }
            if (args[i].equals(RunParams.TESTCASE_PID)) {
                params.put(RunParams.TESTCASE_PID, args[i + 1]);
                i++;
            }

            if (args[i].equals(RunParams.USER_NAME)) {
                params.put(RunParams.USER_NAME, args[i + 1]);
                i++;
            }
            if (args[i].equals(RunParams.RESULT_ON_CLIENT)) {
                params.put(RunParams.RESULT_ON_CLIENT, "");
                haveResultOnClient++;
            }
        }

        if (params.size() != 4 + haveResultOnClient) {
            System.out.println("Not all required arguments are specified");
            printUsage();
            System.exit(1);
        }

        try {
            new IsTestsRemoteRunner(params.get(RunParams.AAS_ADDRESS), params.get(RunParams.TESTCASE_PID), params.get(RunParams.RESULT_FILE_NAME), params.get(RunParams.USER_NAME), params.containsKey(RunParams.RESULT_ON_CLIENT)).run();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static void printUsage() {
        System.out.println("Usage:");
        System.out.println("java -cp Server.jar org.radixware.kernel.server.test -aasAddress <aasHost:aasPort> -userName <userName> -testCasePid <TestCase Pid> -resultFileName <path to result file> [-resultOnClient]");
    }
//
    private static final Id RUN_ALL_TESTS_FOR_IS_TESTCASE_CLASS_ID = Id.Factory.loadFrom("acl7B5SI2L2CZBB7COXUQX33KU3QI");
    private static final String RUN_ALL_TESTS_FOR_IS_TESTCASE_MTH_RUN_ID = "mthL76A62L67NECLN3JUTXU4ZBAVA";
    private static final String RUN_ALL_TESTS_FOR_IS_TESTCASE_MTH_RUN_FOR_REMOTE_ID = "mthK4VVUUU7HFCYXGGVHCQO76G5SE";
//
    private final SapClientOptions sapOpt;
    private final String testCasePid;
    private final String resultFilePath;
    private final String userName;
    private final boolean resultsOnClient;

    public IsTestsRemoteRunner(String aasAddress, String testCasePid, String resultFilePath, String userName, boolean resultsOnClient) {
        this.testCasePid = testCasePid;
        this.resultFilePath = resultFilePath;
        this.userName = userName;
        sapOpt = new SapClientOptions();
        sapOpt.setName("IsAasSap");
        sapOpt.setAddress(new SapAddress(ValueFormatter.parseInetSocketAddress(aasAddress)));
        sapOpt.setPriority(50);
        this.resultsOnClient = resultsOnClient;
    }

    public void run() throws ServiceCallException, ServiceCallTimeout, ServiceCallFault, InterruptedException, UnsupportedEncodingException, IOException {
        new AasClient(sapOpt, userName).invoke(testCasePid, resultFilePath, resultsOnClient);
    }

    private static final class AasClient extends ServiceClient {

        private final SapClientOptions sapOpt;
        private final String userName;

        public AasClient(final SapClientOptions sapOpt, String userName) {

            super(new LocalTracer() {
                @Override
                public void put(EEventSeverity severity, String localizedMess, String code, List<String> words, boolean isSensitive) {
                    if (words == null || words.isEmpty()) {
                        words = Collections.singletonList(localizedMess);
                    }
                    System.out.println(new TraceItem(null, severity, code, words, EEventSource.AAS_CLIENT.getValue(), isSensitive).toString());
                }

                @Override
                public long getMinSeverity() {
                    return EEventSeverity.DEBUG.getValue().longValue();
                }

                @Override
                public long getMinSeverity(String eventSource) {
                    return getMinSeverity();
                }
            }, null);
            this.sapOpt = sapOpt;
            this.userName = userName;

            final Thread mainThread = Thread.currentThread();

            Runtime.getRuntime().addShutdownHook(new Thread() {
                @Override
                public void run() {
                    mainThread.interrupt();
                    try {
                        mainThread.join(5000);
                    } catch (InterruptedException ex) {
                        return;
                    }
                }
            });

        }

        private void check() throws ServiceCallException, ServiceCallTimeout, ServiceCallFault, InterruptedException {
            final InvokeDocument invokeXml = InvokeDocument.Factory.newInstance();
            final InvokeRq invokeRq = invokeXml.addNewInvoke().addNewInvokeRq();
            invokeRq.setClassName("java.lang.System");
            invokeRq.setMethodId("currentTimeMillis");
            final XmlObject responseXml = invokeService(invokeXml, InvokeMess.class, Long.valueOf(1), null, ArteAccessService.SERVICE_WSDL, 0, 90);
        }

        protected void invoke(final String testCaseId, final String resultFilePath, boolean resultOnClient) throws ServiceCallException, ServiceCallTimeout, ServiceCallFault, InterruptedException, UnsupportedEncodingException, IOException {
            check();//will throw exception if aas is unavailable
            final InvokeDocument invokeXml = InvokeDocument.Factory.newInstance();
            final InvokeRq invokeRq = invokeXml.addNewInvoke().addNewInvokeRq();
            invokeRq.setClassId(RUN_ALL_TESTS_FOR_IS_TESTCASE_CLASS_ID);
            invokeRq.setPID(testCaseId);
            invokeRq.setUser(userName);
            if (resultOnClient) {
                invokeRq.setMethodId(RUN_ALL_TESTS_FOR_IS_TESTCASE_MTH_RUN_FOR_REMOTE_ID);
                InvokeMess rs = (InvokeMess) invokeService(invokeXml, InvokeMess.class, Long.valueOf(1), null, ArteAccessService.SERVICE_WSDL, 0, INVOKE_TIMEOUT_SEC);
                if (rs.getInvokeRs().getReturnValue().isSetStr()) {
                    File resultFile = new File(resultFilePath);
                    if (resultFile.getParentFile() != null) {
                        resultFile.getParentFile().mkdirs();
                    }
                    Files.write(resultFile.toPath(), Hex.decode(rs.getInvokeRs().getReturnValue().getStr()));
                } else {
                    throw new IllegalStateException("Response doesn't contain results");
                }
            } else {
                invokeRq.setMethodId(RUN_ALL_TESTS_FOR_IS_TESTCASE_MTH_RUN_ID);
                invokeRq.addNewParameters().addNewItem().setStr(resultFilePath);
                invokeService(invokeXml, InvokeMess.class, Long.valueOf(1), null, ArteAccessService.SERVICE_WSDL, 0, INVOKE_TIMEOUT_SEC);
            }
        }

        public XmlObject invokeService(XmlObject rqEnvBody, Class resultClass, Long systemId, final Long thisInstanceId, String serviceUri, int keepConnectTimeSec, int timeoutSec) throws ServiceCallException, ServiceCallTimeout, ServiceCallFault, InterruptedException {
            return super.invokeService(rqEnvBody, resultClass, systemId, thisInstanceId, serviceUri, keepConnectTimeSec, timeoutSec, null);
        }

        @Override
        protected List<SapClientOptions> refresh(Long systemId, final Long thisInstanceId, String serviceUri) throws ServiceCallException {
            return Collections.singletonList(sapOpt);
        }
    }
}
