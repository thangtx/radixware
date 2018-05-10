
/* Radix::Testing::TestCase.AbstractGroup - Server Executable*/

/*Radix::Testing::TestCase.AbstractGroup-Application Class*/

package org.radixware.ads.Testing.server;

import java.util.List;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase.AbstractGroup")
public abstract published class TestCase.AbstractGroup  extends org.radixware.ads.Testing.server.TestCase  implements org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {

	protected static class TraceBufferImpl implements org.radixware.kernel.server.trace.TraceBuffer {

	    private final TestCase testCase;
	    private final StringBuilder sb = new StringBuilder();
	    private Arte::EventSeverity maxSeverity = Arte::EventSeverity:Debug;
	    private Arte::EventSeverity traceLevel = Arte::EventSeverity:Debug;

	    public TraceBufferImpl(final TestCase testCase, Arte::EventSeverity traceLevel) {
	        this.testCase = testCase;
	        this.traceLevel = traceLevel != null ? traceLevel : Arte::EventSeverity:Debug;
	    }

	    public void put(org.radixware.kernel.common.trace.TraceItem traceItem) {
	        final Arte::EventSeverity severity = traceItem.severity;
	        if (severity != null && severity.Value.longValue() >= traceLevel.Value.longValue()) {
	            sb.append(traceItem.toString());
	            sb.append("\n");
	        }
	        
	        if (severity != null && severity.Value.longValue() > maxSeverity.Value.longValue()) {
	            if (severity.Value.longValue() > Arte::EventSeverity:Event.Value.longValue() && testCase.isExpected(traceItem) == Bool.TRUE) {
	                return;
	            }
	            maxSeverity = severity;
	        }
	    }

	    public String toString() {
	        return sb.toString();
	    }
	}
	/**Metainformation accessor method*/
	@SuppressWarnings("unused")
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return TestCase.AbstractGroup_mi.rdxMeta;}

	/*Radix::Testing::TestCase.AbstractGroup:Nested classes-Nested Classes*/

	/*Radix::Testing::TestCase.AbstractGroup:Properties-Properties*/

	/*Radix::Testing::TestCase.AbstractGroup:testData-Dynamic Property*/



	protected org.radixware.kernel.server.test.TestData testData=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase.AbstractGroup:testData")
	protected published  org.radixware.kernel.server.test.TestData getTestData() {
		return testData;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase.AbstractGroup:testData")
	protected published   void setTestData(org.radixware.kernel.server.test.TestData val) {
		testData = val;
	}

	/*Radix::Testing::TestCase.AbstractGroup:topLevelTestId-Dynamic Property*/



	protected Int topLevelTestId=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase.AbstractGroup:topLevelTestId")
	private final  Int getTopLevelTestId() {

		return internal[topLevelTestId] != null ? internal[topLevelTestId] : id;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase.AbstractGroup:topLevelTestId")
	private final   void setTopLevelTestId(Int val) {
		topLevelTestId = val;
	}









































	/*Radix::Testing::TestCase.AbstractGroup:Methods-Methods*/

	/*Radix::Testing::TestCase.AbstractGroup:execute-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase.AbstractGroup:execute")
	public published  org.radixware.kernel.common.enums.EEventSeverity execute () {
		testData = new TestData(id.toString());
		failEventSeverity = Arte::EventSeverity:None;

		final org.radixware.kernel.server.aio.ServiceManifestLoader manifestLoader = new org.radixware.kernel.server.aio.ServiceManifestServerLoader() {
		    protected java.sql.Connection getDbConnection() {
		        return Arte::Arte.getDbConnection();
		    }

		    protected org.radixware.kernel.server.arte.Arte getArte() {
		        return Arte::Arte.getInstance();
		    }
		};

		final org.radixware.kernel.common.trace.LocalTracer localTracer = new org.radixware.kernel.common.trace.LocalTracer() {
		    public void put(Arte::EventSeverity severity, String localizedMess, String code, List<String> words, boolean isSensitive) {
		        Arte::Trace.put(severity, localizedMess, Arte::EventSource:TestCase);
		    }

		    public long getMinSeverity() {
		        return Arte::Trace.getMinSeverity();
		    }

		    public long getMinSeverity(String eventSource) {
		        return Arte::Trace.getMinSeverity(Arte::EventSource.getForValue(eventSource));
		    }
		};

		final org.radixware.kernel.common.sc.ServiceClient aasClient = new org.radixware.kernel.common.sc.ServiceClient(localTracer, null) {
		    @Override
		    protected List<org.radixware.kernel.common.sc.SapClientOptions> refresh(Long systemId, final Long thisInstanceId, String serviceUri) throws Exceptions::ServiceCallException {
		        return manifestLoader.readSaps(1l, Arte::Arte.getInstance().Instance.Id, org.radixware.kernel.server.arte.services.aas.ArteAccessService.SERVICE_WSDL, Arte::Arte.getInstance().Instance.ScpName, 0);
		    }
		};

		try {
		    aasClient.setScpName(Arte::Arte.getInstance().Instance.ScpName);
		} catch (Exceptions::ServiceCallException ex) {
		    throw new Error("Error while setting scp for aas client", ex);
		}

		Arte::EventSeverity result = Arte::EventSeverity:Event;

		Arte::Trace.enterContext(Arte::EventContextType:TestCase, id);
		try {
		    for (Int id : getChildren()) {
		        TestCase test = TestCase.loadByPK(id, false);
		        if (!test.active.booleanValue()) {
		            continue;
		        }

		        Arte::EventSeverity localResult = null;
		        if (test instanceof TestCase.AbstractGroup) {
		            TestCase.AbstractGroup childGroup = (TestCase.AbstractGroup) test;
		            try {
		                childGroup.topLevelTestId = topLevelTestId;
		                childGroup.run();

		                TestData childGroupData = childGroup.testData;
		                testData.getChildren().add(childGroupData);

		                localResult = childGroup.result;
		            } catch (Exceptions::Throwable ex) {
		                Arte::Trace.error(java.text.MessageFormat.format("Test {0} from group {1} raised an exception:...", id, id, Arte::Trace.exceptionStackToString(ex)), Arte::EventSource:TestCase);
		                return Arte::EventSeverity:Error;
		            }
		        } else {
		            try {
		                final org.radixware.schemas.aasWsdl.InvokeDocument invokeDoc = org.radixware.schemas.aasWsdl.InvokeDocument.Factory.newInstance();
		                final org.radixware.schemas.aas.InvokeRq invokeRq = invokeDoc.addNewInvoke().addNewInvokeRq();
		                invokeRq.User = Arte::Arte.getUserName();
		                invokeRq.ClassId = idof[TestCase.AbstractGroup];
		                invokeRq.MethodId = idof[TestCase.AbstractGroup:runTestForAasCall].toString();
		                invokeRq.addNewParameters().addNewItem().Int = id;
		                invokeRq.ensureParameters().addNewItem().Int = topLevelTestId;

		                TestData td = null;

		                try {
		                    final org.radixware.schemas.aas.InvokeMess invokeMess = (org.radixware.schemas.aas.InvokeMess) aasClient.invokeService(invokeDoc, org.radixware.schemas.aas.InvokeMess.class, Long.valueOf(1), null, org.radixware.kernel.server.arte.services.aas.ArteAccessService.SERVICE_WSDL, 0, 3 * 60 * 60, 10, Arte::AadcManager.getMyMember());
		                    final org.radixware.schemas.aas.InvokeRs rs = invokeMess == null ? null : invokeMess.InvokeRs;

		                    if (rs != null && rs.isSetReturnValue() && rs.ReturnValue.isSetStr()) {
		                        final java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(Utils::Hex.decode(rs.ReturnValue.Str));
		                        try {
		                            final java.io.ObjectInputStream ios = new java.io.ObjectInputStream(bis);
		                            td = (TestData) ios.readObject();

		                            localResult = td.getResult();
		                        } catch (Exceptions::Exception ex) {
		                            td = createFailData(id, "Recieved wrong result: " + new String(Utils::Hex.decode(rs.ReturnValue.Str)));
		                            result = Arte::EventSeverity:Error;
		                            resultCommentAsStr = test.calcTitle() + ": " + test.resultCommentAsStr;
		                        }
		                    } else {
		                        td = createFailData(id, "No message was returned");
		                        result = Arte::EventSeverity:Error;
		                        resultCommentAsStr = test.calcTitle() + ": " + test.resultCommentAsStr;
		                    }
		                } catch (Exceptions::Throwable th) {
		                    if (th instanceof Exceptions::InterruptedException) {
		                        throw th;
		                    }
		                    final Str stackTrace = Arte::Trace.exceptionStackToString(th);

		                    td = createFailData(id, "Error executing test case\n: " + stackTrace);
		                    result = Arte::EventSeverity:Error;
		                    resultCommentAsStr = test.calcTitle() + ": " + test.resultCommentAsStr;

		                    Arte::Trace.put(eventCode["Error executing test case #%1: %2"], String.valueOf(id), "\n" + stackTrace);
		                }

		                testData.getChildren().add(td);
		            } catch (Exceptions::Throwable t) {
		                if (t instanceof Exceptions::InterruptedException) {
		                    return result;
		                } else {
		                    throw new Error("Error executing test case #" + id, t);
		                }
		            }
		        }

		        if (localResult == null) {
		            Arte::Trace.error(java.text.MessageFormat.format("Test {0} from group {1} finished with result NULL. Please check test content", id, id), Arte::EventSource:TestCase);
		            return Arte::EventSeverity:Error;
		        }

		        if (localResult.Value.longValue() > result.Value.longValue()) {
		            result = localResult;
		            resultCommentAsStr = test.calcTitle() + ": " + test.resultCommentAsStr;
		        }

		        if (isStopAfterTestFail() && result.Value.longValue() > Arte::EventSeverity:Event.Value.longValue()) {
		            return result;
		        }
		    }
		} finally {
		    Arte::Trace.leaveContext(Arte::EventContextType:TestCase, id);
		}

		return result;
	}

	/*Radix::Testing::TestCase.AbstractGroup:getChildren-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase.AbstractGroup:getChildren")
	public abstract published  java.util.List<Int> getChildren ();

	/*Radix::Testing::TestCase.AbstractGroup:createFailData-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase.AbstractGroup:createFailData")
	private final  org.radixware.kernel.server.test.TestData createFailData (Int testId, Str message) {
		TestData td = new TestData(testId.toString());
		td.onStart();
		td.onFinish();
		td.failed(message);
		return td;
	}

	/*Radix::Testing::TestCase.AbstractGroup:appendGroupNames-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase.AbstractGroup:appendGroupNames")
	private static  void appendGroupNames (org.radixware.ads.Testing.server.TestCase testCase, java.lang.StringBuilder groupChain) {
		final TestCase group = testCase.groupRef;
		if (group != null) {
		    appendGroupNames(group, groupChain);

		    groupChain.append(group.id).append(") ");
		    if (group.classTitle != null) {
		        groupChain.append(group.classTitle);
		    }
		    if (group.notes != null) {
		        groupChain.append(" - ").append(group.notes.replace('.', '_'));
		    }
		    groupChain.append(".");
		}
	}

	/*Radix::Testing::TestCase.AbstractGroup:runTestForAasCall-User Method*/

	@org.radixware.kernel.common.lang.ReflectiveCallable
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase.AbstractGroup:runTestForAasCall")
	public static published  Str runTestForAasCall (Int testId, Int callerTestId) {
		org.radixware.kernel.server.trace.Trace trace = Arte::Arte.getInstance().Trace;

		if (testId == null) {
		    throw new AppError("No test id given");
		}

		if (callerTestId == null) {
		    throw new AppError("Caller test id not defined");
		}

		final TestCase testCase = TestCase.loadByPK(testId, true);
		TestData testData = new TestData(testId.toString());

		testData.setNotes(testCase.notes);
		testData.setNotificationEmail(testCase.notificationEmail);
		testData.onStart();

		try {
		    if (Arte::Arte.getUserName() == null) {
		        testCase.actor = "Integration Server";
		    }
		    
		    final java.lang.StringBuilder groupChain = new java.lang.StringBuilder();
		    appendGroupNames(testCase, groupChain);
		    testData.setTitle(groupChain.toString() + testCase.id + ") " + (testCase.classTitle == null ? "<no name>" : testCase.classTitle));
		    Arte::EventSeverity result = null;
		    
		    final TestCase.AbstractGroup callerTest = (TestCase.AbstractGroup) TestCase.loadByPK(callerTestId, true);
		    TraceBufferImpl traceBuffer = new TraceBufferImpl(testCase, callerTest.getTraceLevelForAasCall());
		    Object traceTarget = null;
		    
		    try {
		        traceTarget = trace.addTargetBuffer(testCase.traceProfile, traceBuffer);
		        final Int runSectionId = Arte::Arte.enterCachingSession();
		        try {
		            try {
		                testCase.run();
		                result = testCase.result;
		                testData.setResult(result);
		            } catch (Exceptions::Throwable e) {
		                testData.onFinish();
		                testData.failed(Arte::Trace.exceptionStackToString(e));
		            }
		        } finally {
		            try {
		                Arte::Arte.leaveCachingSession(runSectionId);
		                Arte::Arte.getInstance().getCache().clear(runSectionId, Int.MAX_VALUE);
		            } catch (Exceptions::Throwable t) {
		                try {
		                    if ((testData.getError() == null) && (result != null) && (result.Value.longValue() <= Arte::EventSeverity:Event.Value.longValue())) {
		                        testData.onFinish();
		                        testData.failed(Arte::Trace.exceptionStackToString(t));
		                    } else {
		                        StringBuilder errorSb = new StringBuilder();
		                        if (testData.getError() != null) {
		                            errorSb.append("Execution error:\n");
		                            errorSb.append(testData.getError());
		                        }
		                        errorSb.append("Result comment as str:\n");
		                        errorSb.append(testCase.resultCommentAsStr);
		                        errorSb.append('\n');
		                        errorSb.append("Flushing data to database error:\n");
		                        errorSb.append(Arte::Trace.exceptionStackToString(t));
		                        testData.failed(errorSb.toString());
		                    }
		                    result = null;//indicate that error happend
		                } finally {
		                    Arte::Arte.rollback();
		                }
		            }
		        }
		    } finally {
		        if (traceTarget != null) {
		            trace.delTarget(traceTarget);
		        }
		    }
		    if (result != null) {
		        testData.onFinish();
		        if (result.Value.longValue() > Arte::EventSeverity:Event.Value.longValue()) {
		            testData.failed(testCase.resultCommentAsStr);
		        } 
		    }    
		    testData.setStdout(traceBuffer.toString() + "\nDuration: " + testCase.getDurationStr());
		} catch (Exceptions::Throwable t) {
		    testData.onFinish();
		    testData.failed(Arte::Trace.exceptionStackToString(t));
		}

		testCase.lastIsExecDate = Arte::Arte.getCurrentTime();
		if (testData.isOk()) {
		    testCase.lastIsSuccessDate = testCase.lastIsExecDate;
		    testCase.seqIsFailCount = 0;
		} else {
		    if (testCase.seqIsFailCount == null) {
		        testCase.seqIsFailCount = 1;
		    } else {
		        testCase.seqIsFailCount = testCase.seqIsFailCount.longValue() + 1;
		    }    
		}

		try {
		    final java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream();
		    final java.io.ObjectOutputStream oos = new java.io.ObjectOutputStream(bos);
		    oos.writeObject(testData);
		    return Utils::Hex.encode(bos.toByteArray());
		} catch (Exceptions::IOException ex) {
		    throw new AppError("Error while running testcase #" + testId + ":\n" + Arte::Trace.exceptionStackToString(ex));
		}
	}

	/*Radix::Testing::TestCase.AbstractGroup:isStopAfterTestFail-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase.AbstractGroup:isStopAfterTestFail")
	protected published  boolean isStopAfterTestFail () {
		return false;
	}

	/*Radix::Testing::TestCase.AbstractGroup:getTraceLevelForAasCall-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase.AbstractGroup:getTraceLevelForAasCall")
	protected abstract published  org.radixware.kernel.common.enums.EEventSeverity getTraceLevelForAasCall ();


	@Override
	public org.radixware.kernel.server.types.FormHandler.NextDialogsRequest execCommand(org.radixware.kernel.common.types.Id cmdId, org.radixware.kernel.common.types.Id propId, org.apache.xmlbeans.XmlObject input, org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValsById, org.apache.xmlbeans.XmlObject output) throws org.radixware.kernel.common.exceptions.AppException,java.lang.InterruptedException{

			return super.execCommand(cmdId,propId,input,newPropValsById,output);
	}


}

/* Radix::Testing::TestCase.AbstractGroup - Server Meta*/

/*Radix::Testing::TestCase.AbstractGroup-Application Class*/

package org.radixware.ads.Testing.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class TestCase.AbstractGroup_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("aclPJQ6F4H7UBDWJMTQUMZFCOMZ5Q"),"TestCase.AbstractGroup",null,

						org.radixware.kernel.common.enums.EClassType.APPLICATION,

						/*Radix::Testing::TestCase.AbstractGroup:Presentations-Entity Object Presentations*/
						/*Class presentation attributes*/
						 new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef(/*Owner Class Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("aclPJQ6F4H7UBDWJMTQUMZFCOMZ5Q"),
							/*Owner Class Name*/
							"TestCase.AbstractGroup",
							/*Title Id*/
							null,
							/*Property presentations*/

							/*Radix::Testing::TestCase.AbstractGroup:Properties-Properties*/
							null,
							/*Commands*/
							null,
							/*Sortings*/
							null,
							/*Filters*/
							null,
							/*Editor presentations*/
							null,
							/*Selector presentations*/
							null,
							/*Default selector presentation Id*/
							null,
							/*Presentation Map*/
							null,
							/*Object title format*/
							null,
							/*Class catalogs*/
							null,
							/*Restrictions*/
							null,null,0),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblHW4OSVMS27NRDISQAAAAAAAAAA"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),

						/*Radix::Testing::TestCase.AbstractGroup:Properties-Properties*/
						new org.radixware.kernel.server.meta.clazzes.RadPropDef[]{

								/*Radix::Testing::TestCase.AbstractGroup:testData-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdLHELNB6KHNC5XANNAYLQOGNAI4"),"testData",null,org.radixware.kernel.common.enums.EValType.USER_CLASS,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Testing::TestCase.AbstractGroup:topLevelTestId-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdEW36OMEIUFET3BMEOFRM63LKRA"),"topLevelTestId",null,org.radixware.kernel.common.enums.EValType.INT,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE)
						},

						/*Radix::Testing::TestCase.AbstractGroup:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthG2ORZGNH27NRDISQAAAAAAAAAA"),"execute",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.INT),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthMKGKB4FK5BDC7L527USXI3KNNA"),"getChildren",false,true,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.JAVA_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthVA25VGJWUBCKTJKY2OACY2EZYU"),"createFailData",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("testId",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprU3YPHONCZZCLFL2Q77IFLRYJJE")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("message",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprJQXTFA7BFNAJJEFDFDB22G36QA"))
								},org.radixware.kernel.common.enums.EValType.JAVA_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthLD2LY6LNCNBOHCUQJ4OYCQM3CA"),"appendGroupNames",true,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("testCase",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprYDTYORZEEZHN7EMDKGFST3XSZQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("groupChain",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprXKXA24QITJH4PFMHOCSMZXBB5I"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth7FDSED6545DDDGPZ54UF6BGQZ4"),"runTestForAasCall",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("testId",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr5EE2V2OG7NCULDJHCJHI6HIICM")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("callerTestId",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprK55VJIQSKVC6LMYNOE3Z5YE3LE"))
								},org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthG6UZCOPXGVD4TAWLASVY3K74T4"),"isStopAfterTestFail",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth4JG27OGWZRERLNHYAE247CKPCU"),"getTraceLevelForAasCall",false,true,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,org.radixware.kernel.common.enums.EValType.INT)
						},
						null,
						null,null,null,false);
}

/* Radix::Testing::TestCase.AbstractGroup - Desktop Executable*/

/*Radix::Testing::TestCase.AbstractGroup-Application Class*/

package org.radixware.ads.Testing.explorer;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase.AbstractGroup")
public interface TestCase.AbstractGroup   extends org.radixware.ads.Testing.explorer.TestCase  {
























}

/* Radix::Testing::TestCase.AbstractGroup - Desktop Meta*/

/*Radix::Testing::TestCase.AbstractGroup-Application Class*/

package org.radixware.ads.Testing.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class TestCase.AbstractGroup_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::Testing::TestCase.AbstractGroup:Presentations-Entity Object Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aclPJQ6F4H7UBDWJMTQUMZFCOMZ5Q"),
			"Radix::Testing::TestCase.AbstractGroup",
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),
			null,
			null,
			null,null,null,0,

			/*Radix::Testing::TestCase.AbstractGroup:Properties-Properties*/
			null,
			null,
			null,
			null,
			null,
			null,
			true,false,false);
}

/* Radix::Testing::TestCase.AbstractGroup - Web Executable*/

/*Radix::Testing::TestCase.AbstractGroup-Application Class*/

package org.radixware.ads.Testing.web;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase.AbstractGroup")
public interface TestCase.AbstractGroup   extends org.radixware.ads.Testing.web.TestCase  {
























}

/* Radix::Testing::TestCase.AbstractGroup - Web Meta*/

/*Radix::Testing::TestCase.AbstractGroup-Application Class*/

package org.radixware.ads.Testing.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class TestCase.AbstractGroup_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::Testing::TestCase.AbstractGroup:Presentations-Entity Object Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aclPJQ6F4H7UBDWJMTQUMZFCOMZ5Q"),
			"Radix::Testing::TestCase.AbstractGroup",
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),
			null,
			null,
			null,null,null,0,

			/*Radix::Testing::TestCase.AbstractGroup:Properties-Properties*/
			null,
			null,
			null,
			null,
			null,
			null,
			true,false,false);
}

/* Radix::Testing::TestCase.AbstractGroup - Localizing Bundle */
package org.radixware.ads.Testing.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class TestCase.AbstractGroup - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Error executing test case #%1: %2");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ошибка при выполнении теста #%1: %2");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2EDPCXUIANCNRI6B7COX72DXBQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.EVENT_CODE,org.radixware.kernel.common.enums.EEventSeverity.ERROR,"App.TestCase",null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Test {0} from group {1} raised an exception:\n{2}");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Тест {0} группы {1} привел к возникновению исключительной ситуации\n{2}");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHWR53UQCFJCW7CF435RZFZUXRY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Integration Server");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Сервер интеграции");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMMWLFGTV55CE3AMUUJNMDDIYPU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Error executing test case #");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ошибка при выполнении теста #");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUMNOFRLSOJGMNL4XAKIYDDAMK4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Test {0} from group {1} finished with result NULL. Please check test content");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Тест {0} группы {1} привел завершился с результатом NULL. Пожалуйста, проверьте корректность теста");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWBAM7FF56BDA3O55UBQWTULRRQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(TestCase.AbstractGroup - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbaclPJQ6F4H7UBDWJMTQUMZFCOMZ5Q"),"TestCase.AbstractGroup - Localizing Bundle",$$$items$$$);
}
