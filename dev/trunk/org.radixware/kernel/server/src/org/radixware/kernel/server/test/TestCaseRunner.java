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

//import org.radixware.kernel.server.SrvRunParams;
//import org.radixware.kernel.common.utils.net.SocketsDisconnectWatcher;
//import java.net.InetSocketAddress;
//import java.net.SocketAddress;
//import java.sql.Clob;
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Properties;
//import oracle.jdbc.OracleConnection;
//import oracle.jdbc.pool.OracleDataSource;
//import org.apache.xmlbeans.XmlObject;
//import org.radixware.kernel.common.enums.EEventSeverity;
//import org.radixware.kernel.common.enums.EIsoLanguage;
//import org.radixware.kernel.common.exceptions.ResourceUsageException;
//import org.radixware.kernel.common.exceptions.ResourceUsageTimeout;
//import org.radixware.kernel.common.exceptions.ServiceCallException;
//import org.radixware.kernel.common.exceptions.ServiceCallFault;
//import org.radixware.kernel.common.exceptions.ServiceCallTimeout;
//import org.radixware.kernel.common.exceptions.ServiceProcessFault;
//import org.radixware.kernel.common.types.IKernelIntEnum;
//import org.radixware.kernel.common.types.Id;
//import org.radixware.kernel.common.utils.ExceptionTextFormatter;
//import org.radixware.kernel.common.utils.SoapFormatter.ResponseTraceItem;
//import org.radixware.kernel.common.utils.Utils;
//import org.radixware.kernel.server.arte.Arte;
//import org.radixware.kernel.server.arte.ArteSocket;
//import org.radixware.kernel.server.exceptions.ArteSocketException;
//import org.radixware.kernel.server.exceptions.ArteSocketTimeout;
//import org.radixware.kernel.server.instance.Instance;
//import org.radixware.kernel.server.types.Entity;
//import org.radixware.kernel.server.types.Pid;
//import org.radixware.kernel.server.units.arte.ArteUnit;
//import static org.mockito.Mockito.*;

@Deprecated
public class TestCaseRunner {

//	public static void main(final String[] args) throws SQLException, Throwable {
//        String resultFileName = "TEST-RDX_TESTCASE.xml";
//		if (args.length != 0) {
//			try {
//				final int len = args.length;
//				for (int i = 0; i < len; i++) {
//					try {
//						if (args[i].equals(SrvRunParams.DB_URL)) {
//							SrvRunParams.setDbUrl(args[++i]);
//						} else if (args[i].equals(SrvRunParams.DB_SCHEMA)) {
//							SrvRunParams.setDbSchema(args[++i]);
//						} else if (args[i].equals(SrvRunParams.USER)) {
//							SrvRunParams.setUser(args[++i]);
//						} else if (args[i].equals(SrvRunParams.DB_PWD)) {
//							SrvRunParams.setDbPwd(args[++i]);
//						} else if (args[i].equals(SrvRunParams.EXTERNAL_AUTH)) {
//							SrvRunParams.setExternalAuth(true);
//						} else if (args[i].equals("-resultFileName")) {
//    						resultFileName = args[++i];
//						} else {
//							throw new SrvRunParams.UnsupportedParamException(args[i]);
//
//						}
//					} catch (ArrayIndexOutOfBoundsException e) {
//						throw new SrvRunParams.EmptyParamValueException(args[i - 1]);
//					}
//				}
//			} catch (SrvRunParams.ParamException e) {
//				System.err.println(e.getMessage());
//				return;
//			}
//
//		}
//        final TestSuiteData suitData = new TestSuiteData();
//        suitData.onStart();
//        final OracleDataSource oraDataSource = new OracleDataSource();
//        try {
//			oraDataSource.setURL(SrvRunParams.getDbUrl());
//            final String proxyOraUser;
//            if (SrvRunParams.getIsExternalAuthOn()){
//                oraDataSource.setUser("");
//                oraDataSource.setPassword("");
//                proxyOraUser = SrvRunParams.getDbSchema();
//            } else {
//                oraDataSource.setUser(SrvRunParams.getUser());
//                oraDataSource.setPassword(SrvRunParams.getPwd());
//                if (Utils.equals(SrvRunParams.getDbSchema(),SrvRunParams.getUser())){
//                    proxyOraUser = null;
//                } else
//                    proxyOraUser = SrvRunParams.getDbSchema();
//            }
//            final Connection dbConnection = openNewDbConnection(oraDataSource, proxyOraUser);
//            try {
//                dbConnection.setAutoCommit(false);
//
//                final Instance inst = mock(Instance.class);
//                when(inst.getSensitiveTracingFinishMillis()).thenReturn(Long.MAX_VALUE);
//                final ArteUnit unit = mock(ArteUnit.class);
//                when(unit.getInstance()).thenReturn(inst);
//                final Arte arte = new Arte(inst);
//                try {
//                    arte.init(dbConnection, new TestArteSocket(), 0);
//                    final PreparedStatement st = dbConnection.prepareStatement("select id from rdx_testcase where runOnIs <> 0");
//                    final ArrayList<String> tests = new ArrayList<String>(1024);
//                    try {
//                        final ResultSet rs = st.executeQuery();
//                        try {
//                            while (rs.next()) {
//                                tests.add(rs.getString(1));
//                            }
//                        } finally {
//                            rs.close();
//                        }
//                    } finally {
//                        st.close();
//                    }
//                    for (String testId : tests ) {
//                        System.out.println("======== Loading test case # " + testId);
//                        TestData testData = new TestData(testId);
//                        testData.onStart();
//                        try {
//                            arte.startTransaction(arte.getActualVersion(), null, "TestCaseRunner", "TestCaseRunner", EIsoLanguage.ENGLISH);
//                            final Pid pid = new Pid(arte, TEST_CASE_TBL_ID, testId);
//                            final Entity test = arte.getEntityObject(pid);
//                            testData.setTitle(test.calcTitle());
//                            System.out.println("======== Starting " + testData.getTitle());
//                            test.execCommand(TEST_CASE_EXEC_CMD_ID, null, null, null, null);
//                            final Object res = test.getProp(TEST_CASE_RES_PROP_ID);
//                            final int intRes;
//                            if (res instanceof IKernelIntEnum)
//                                intRes = ((IKernelIntEnum)res).getValue().intValue();
//                            else
//                                intRes = ((Long)res).intValue();
//                            final boolean ok = intRes <= EEventSeverity.EVENT.getValue().intValue();
//                            final Clob resComment = (Clob)test.getProp(TEST_CASE_RES_COMMENT_PROP_ID);
//                            final String resCommentStr = resComment == null ?  null : resComment.getSubString(1, (int) resComment.length());
//                            System.out.println(resCommentStr);
//                            arte.endTransaction(true);
//                            testData.onFinish();
//                            if (!ok)
//                                testData.failed(resCommentStr);
//                            suitData.regTest(testData);
//                            if (ok)
//                                System.out.println("======== " + testData.getTitle() + " finished");
//                            else {
//                                System.out.println("======== " + testData.getTitle() + " FAILED");
//                            }
//                        } catch (Throwable e){
//                            arte.endTransaction(false);
//                            testData.onFinish();
//                            final String resComment = "Unhandled exception: \n" + ExceptionTextFormatter.exceptionStackToString(e);
//                            System.out.println("======== " + testData.getTitle()+ " FAILED: \n");
//                            System.out.println(resComment);
//                        }
//                    }
//                } finally {
//                    arte.close();
//                }
//            } finally {
//                dbConnection.close();
//            }
//        } finally {
//            oraDataSource.close();
//        }
//        suitData.onFinish();
//        suitData.writeToFile(resultFileName);
//        System.exit(0);//return;
//	}
//	private static final Connection openNewDbConnection(final OracleDataSource oraDataSrc, final String proxyOraUser) throws SQLException {
//		final Connection c = oraDataSrc.getConnection();
//		if (proxyOraUser != null) {
//			final Properties properties = new Properties();
//			properties.put("PROXY_USER_NAME", proxyOraUser);
//			((OracleConnection) c).openProxySession(OracleConnection.PROXYTYPE_USER_NAME, properties);
//		}
//		return c;
//
//	}
//
//    private static final class TestArteSocket extends ArteSocket {
//
//        @Override
//        public XmlObject recvRequest(int timeout) throws ArteSocketException, ArteSocketTimeout, InterruptedException {
//            throw new UnsupportedOperationException("Not supported yet.");
//        }
//
//        @Override
//        public XmlObject invokeResource(XmlObject obj, Class resultClass, int timeout) throws ResourceUsageException, ResourceUsageTimeout, InterruptedException {
//            throw new UnsupportedOperationException("Not supported yet.");
//        }
//
//        @Override
//        public XmlObject invokeInternalService(XmlObject obj, Class resultClass, String service, int keepConnectTime, int timeout) throws ServiceCallException, ServiceCallTimeout, ServiceCallFault, InterruptedException {
//            throw new UnsupportedOperationException("Not supported yet.");
//        }
//
//        @Override
//        public XmlObject invokeService(XmlObject obj, Class resultClass, Long systemId, String service, String scpName, int keepConnectTime, int timeout) throws ServiceCallException, ServiceCallTimeout, ServiceCallFault, InterruptedException {
//            throw new UnsupportedOperationException("Not supported yet.");
//        }
//
//        @Override
//        public void sendResponse(XmlObject obj, boolean keepConnect) throws ArteSocketException, ArteSocketTimeout, InterruptedException {
//            throw new UnsupportedOperationException("Not supported yet.");
//        }
//
//        @Override
//        public void sendFault(ServiceProcessFault fault, List<ResponseTraceItem> trace) throws ArteSocketException, ArteSocketTimeout, InterruptedException {
//            throw new UnsupportedOperationException("Not supported yet.");
//        }
//
//        @Override
//        public boolean breakSignaled() {
//            return false;
//        }
//
//        @Override
//        public InetSocketAddress getSapAddress() {
//            throw new UnsupportedOperationException("Not supported yet.");
//        }
//
//        @Override
//        public SocketAddress getRemoteAddress() {
//            throw new UnsupportedOperationException("Not supported yet.");
//        }
//
//        @Override
//        public long getSapId() {
//            return 0;
//        }
//
//        private final SocketsDisconnectWatcher w = new SocketsDisconnectWatcher();
//        @Override
//        public SocketsDisconnectWatcher getSocketsDisconnectWatcher() {
//            return w;
//        }
//    }
//    private static final Id TEST_CASE_TBL_ID = Id.Factory.loadFrom("tblHW4OSVMS27NRDISQAAAAAAAAAA");
//    private static final Id TEST_CASE_EXEC_CMD_ID = Id.Factory.loadFrom("cmdHGOOKC3J3DNRDISQAAAAAAAAAA");
//    private static final Id TEST_CASE_RES_PROP_ID = Id.Factory.loadFrom("colYWL4NXUS27NRDISQAAAAAAAAAA");
//    private static final Id TEST_CASE_RES_COMMENT_PROP_ID = Id.Factory.loadFrom("colC46NAASN3PNRDISQAAAAAAAAAA");
}

