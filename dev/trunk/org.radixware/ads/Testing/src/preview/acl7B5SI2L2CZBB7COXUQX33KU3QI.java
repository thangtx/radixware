
/* Radix::Testing::TestCase.IsTests - Server Executable*/

/*Radix::Testing::TestCase.IsTests-Application Class*/

package org.radixware.ads.Testing.server;

import java.util.List;
import java.util.ArrayList;


@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase.IsTests")
public class TestCase.IsTests  extends org.radixware.ads.Testing.server.TestCase.AbstractGroup  implements org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {

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
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return TestCase.IsTests_mi.rdxMeta;}

	/*Radix::Testing::TestCase.IsTests:Nested classes-Nested Classes*/

	/*Radix::Testing::TestCase.IsTests:Properties-Properties*/

	/*Radix::Testing::TestCase.IsTests:resultFileName-Dynamic Property*/



	protected Str resultFileName=(Str)org.radixware.kernel.common.defs.value.ValAsStr.fromStr("",org.radixware.kernel.common.enums.EValType.STR);











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase.IsTests:resultFileName")
	public  Str getResultFileName() {
		return resultFileName;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase.IsTests:resultFileName")
	public   void setResultFileName(Str val) {
		resultFileName = val;
	}

	/*Radix::Testing::TestCase.IsTests:os-Dynamic Property*/



	protected java.io.OutputStream os=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase.IsTests:os")
	public  java.io.OutputStream getOs() {
		return os;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase.IsTests:os")
	public   void setOs(java.io.OutputStream val) {
		os = val;
	}

	/*Radix::Testing::TestCase.IsTests:persistentResultFileName-User Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase.IsTests:persistentResultFileName")
	public  Str getPersistentResultFileName() {
		return persistentResultFileName;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase.IsTests:persistentResultFileName")
	public   void setPersistentResultFileName(Str val) {
		persistentResultFileName = val;
	}

	/*Radix::Testing::TestCase.IsTests:emailRoutingKey-User Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase.IsTests:emailRoutingKey")
	private final  Str getEmailRoutingKey() {
		return emailRoutingKey;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase.IsTests:emailRoutingKey")
	private final   void setEmailRoutingKey(Str val) {
		emailRoutingKey = val;
	}

	/*Radix::Testing::TestCase.IsTests:sendEmailNotificationsToAuthors-User Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase.IsTests:sendEmailNotificationsToAuthors")
	private final  Bool getSendEmailNotificationsToAuthors() {
		return sendEmailNotificationsToAuthors;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase.IsTests:sendEmailNotificationsToAuthors")
	private final   void setSendEmailNotificationsToAuthors(Bool val) {
		sendEmailNotificationsToAuthors = val;
	}

	/*Radix::Testing::TestCase.IsTests:AWAIT_NOTIFICATIONS_SENDED_MILLIS-Dynamic Property*/



	protected int AWAIT_NOTIFICATIONS_SENDED_MILLIS=30000;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase.IsTests:AWAIT_NOTIFICATIONS_SENDED_MILLIS")
	private final  int getAWAIT_NOTIFICATIONS_SENDED_MILLIS() {
		return AWAIT_NOTIFICATIONS_SENDED_MILLIS;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase.IsTests:AWAIT_NOTIFICATIONS_SENDED_MILLIS")
	private final   void setAWAIT_NOTIFICATIONS_SENDED_MILLIS(int val) {
		AWAIT_NOTIFICATIONS_SENDED_MILLIS = val;
	}

	/*Radix::Testing::TestCase.IsTests:testsToExecute-User Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase.IsTests:testsToExecute")
	private final  org.radixware.kernel.server.types.ArrEntity<org.radixware.ads.Testing.server.TestCase> getTestsToExecute() {
		return testsToExecute;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase.IsTests:testsToExecute")
	private final   void setTestsToExecute(org.radixware.kernel.server.types.ArrEntity<org.radixware.ads.Testing.server.TestCase> val) {
		testsToExecute = val;
	}

	/*Radix::Testing::TestCase.IsTests:BASE_TEST_CASE_URL_SYS_PROP-Dynamic Property*/



	protected Str BASE_TEST_CASE_URL_SYS_PROP=(Str)org.radixware.kernel.common.defs.value.ValAsStr.fromStr("RDX_TESTCASE_BASE_URL",org.radixware.kernel.common.enums.EValType.STR);











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase.IsTests:BASE_TEST_CASE_URL_SYS_PROP")
	private final  Str getBASE_TEST_CASE_URL_SYS_PROP() {
		return BASE_TEST_CASE_URL_SYS_PROP;
	}

	/*Radix::Testing::TestCase.IsTests:NOTIFICATION_AWAIT_TIMEOUT_SYS_PROP-Dynamic Property*/



	protected Str NOTIFICATION_AWAIT_TIMEOUT_SYS_PROP=(Str)org.radixware.kernel.common.defs.value.ValAsStr.fromStr("RDX_TESTCASE_NOTIFICATION_AWAIT_MILLIS",org.radixware.kernel.common.enums.EValType.STR);











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase.IsTests:NOTIFICATION_AWAIT_TIMEOUT_SYS_PROP")
	private final  Str getNOTIFICATION_AWAIT_TIMEOUT_SYS_PROP() {
		return NOTIFICATION_AWAIT_TIMEOUT_SYS_PROP;
	}

	/*Radix::Testing::TestCase.IsTests:DEBUG_OUTPUT_SYS_PROP-Dynamic Property*/



	protected Str DEBUG_OUTPUT_SYS_PROP=(Str)org.radixware.kernel.common.defs.value.ValAsStr.fromStr("RDX_TESTCASE_DEBUG_OUTPUT",org.radixware.kernel.common.enums.EValType.STR);











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase.IsTests:DEBUG_OUTPUT_SYS_PROP")
	private final  Str getDEBUG_OUTPUT_SYS_PROP() {
		return DEBUG_OUTPUT_SYS_PROP;
	}

	/*Radix::Testing::TestCase.IsTests:traceLevelForIS-User Property*/








			















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase.IsTests:traceLevelForIS")
	public  org.radixware.kernel.common.enums.EEventSeverity getTraceLevelForIS() {
		return traceLevelForIS;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase.IsTests:traceLevelForIS")
	public   void setTraceLevelForIS(org.radixware.kernel.common.enums.EEventSeverity val) {
		traceLevelForIS = val;
	}

	/*Radix::Testing::TestCase.IsTests:isCheckResultFileName-Dynamic Property*/



	protected Bool isCheckResultFileName=(Bool)org.radixware.kernel.common.defs.value.ValAsStr.fromStr("1",org.radixware.kernel.common.enums.EValType.BOOL);











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase.IsTests:isCheckResultFileName")
	private final  Bool getIsCheckResultFileName() {
		return isCheckResultFileName;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase.IsTests:isCheckResultFileName")
	private final   void setIsCheckResultFileName(Bool val) {
		isCheckResultFileName = val;
	}

























































































	/*Radix::Testing::TestCase.IsTests:Methods-Methods*/

	/*Radix::Testing::TestCase.IsTests:execute-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase.IsTests:execute")
	public published  org.radixware.kernel.common.enums.EEventSeverity execute () {
		if (os == null && (resultFileName == null || resultFileName.isEmpty())) {
		    fail("Output file name not specified");
		}

		if (Arte::Arte.getUserName() == null) {
		    actor = "Integration Server";
		}

		final TestSuiteData suiteData = new TestSuiteData();

		suiteData.onStart();

		final Arte::EventSeverity result = super.execute();

		int[] testsResult = {0, 0};
		addTestDataToSuite(suiteData, testData, testsResult);

		int successfullCnt = testsResult[0];
		int failedCnt = testsResult[1];

		suiteData.onFinish();

		try {
		    System.out.println(System.currentTimeMillis() + " Suite: " + suiteData + ", file: " + resultFileName + ", os: " + os);
		    if (os != null) {
		        suiteData.writeToStream(os);
		    } else {
		        suiteData.writeToFile(resultFileName);
		    }
		} catch (Exceptions::IOException e) {
		    resultCommentAsStr = "Error writing results: " + Arte::Trace.exceptionStackToString(e);
		    return Arte::EventSeverity:Error;
		}

		resultCommentAsStr = "Successfull: " + successfullCnt + "; failed: " + failedCnt;

		if (sendEmailNotificationsToAuthors != null && sendEmailNotificationsToAuthors.booleanValue()) {
		    sendNotificationsToAuthors(suiteData);
		}

		return result;
	}

	/*Radix::Testing::TestCase.IsTests:run-User Method*/

	@org.radixware.kernel.common.lang.ReflectiveCallable
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase.IsTests:run")
	public published  void run (Str resultFileName) {
		resultFileName = resultFileName;
		isCheckResultFileName = true;
		run();
	}

	/*Radix::Testing::TestCase.IsTests:preCheck-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase.IsTests:preCheck")
	public published  void preCheck () {
		super.preCheck();
		if (os != null) {
		    return;
		}
		if(resultFileName == null || resultFileName.isEmpty()) {
		    resultFileName = persistentResultFileName;
		}
		if(resultFileName == null || resultFileName.isEmpty()) {
		    throw new InvalidPropertyValueError(idof[TestCase.IsTests], idof[TestCase.IsTests:persistentResultFileName], "Output file must be specified");
		}

		if (isCheckResultFileName.booleanValue()) {
		    Str fileName = resultFileName.replace("\\", "/");
		    java.io.File resultFile = new java.io.File(fileName);
		    java.io.File parent = resultFile.getParentFile();
		    if (parent != null && !parent.exists()) {
		        throw new InvalidPropertyValueError(idof[TestCase.IsTests], idof[TestCase.IsTests:persistentResultFileName], Utils::MessageFormatter.format("Output directory \"{0}\" doesn't exists", parent.getPath()));
		    }
		}
	}

	/*Radix::Testing::TestCase.IsTests:runTestForAasCall-User Method*/

	@org.radixware.kernel.common.lang.ReflectiveCallable
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase.IsTests:runTestForAasCall")
	@Deprecated
	public static published  Str runTestForAasCall (Int testId, Int callerTestId) {
		return TestCase.AbstractGroup.runTestForAasCall(testId, callerTestId);
	}

	/*Radix::Testing::TestCase.IsTests:executeForRemoteCall-User Method*/

	@org.radixware.kernel.common.lang.ReflectiveCallable
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase.IsTests:executeForRemoteCall")
	public published  Str executeForRemoteCall () {
		os = new java.io.ByteArrayOutputStream();
		isCheckResultFileName = false;
		run();
		try {
		    return Utils::Hex.encode(((java.io.ByteArrayOutputStream) os).toByteArray());
		} catch (Exception ex) {
		    resultCommentAsStr = "Unable to write results: " + ex.getMessage();
		    return null;
		}
	}

	/*Radix::Testing::TestCase.IsTests:createNotificationMessage-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase.IsTests:createNotificationMessage")
	private final  org.radixware.ads.PersoComm.server.OutMessage createNotificationMessage (Str address, java.util.List<org.radixware.kernel.server.test.TestData> failedTests, Str testStartDate, Str baseTestCaseUrl) {
		PersoComm::OutMessage msg = new PersoComm::OutMessage();
		msg.init();
		msg.subject = Str.format("Integration Server: %d failed tests (%s)%n", failedTests.size(), testStartDate);
		msg.importance = PersoComm::Importance:Normal;
		msg.sourceEntityGuid = idof[TestCase.IsTests].toString();
		msg.sourcePid = Str.valueOf(id);
		msg.createTime = Arte::Arte.getCurrentTime();
		msg.dueTime = msg.createTime;
		msg.expireTime = null;
		msg.channelKind = PersoComm::ChannelKind:Email;
		msg.histMode = PersoComm::HistMode:Show;
		msg.routingKey = emailRoutingKey;
		msg.body = createMessageBody(failedTests, baseTestCaseUrl);

		try {
		    msg.address = address;
		    msg.create();
		    return msg;
		} catch (Exceptions::DatabaseError e) {
		    Arte::Trace.error(String.format("Error creating a notification on unsuccessful tests to the address '%s'",
		            msg.address) + "\n" + e.getMessage(), Arte::EventSource:TestCase);
		    msg.discard();
		    return null;
		}
	}

	/*Radix::Testing::TestCase.IsTests:sendNotificationsToAuthors-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase.IsTests:sendNotificationsToAuthors")
	private final  void sendNotificationsToAuthors (org.radixware.kernel.server.test.TestSuiteData testSuite) {
		boolean debug = false;
		if (System.getenv(DEBUG_OUTPUT_SYS_PROP) != null) {
		    try {
		        debug = Bool.parseBoolean(System.getenv(DEBUG_OUTPUT_SYS_PROP));
		    } catch(Exceptions::Exception ex) {
		        //ignore
		    }
		}        
		final StringBuilder dbgSb = new java.lang.StringBuilder();
		final String dbgMsgSource = "IS Test Cases";

		//1. Gather email to messages map
		java.util.Map<Str, java.util.List<TestData>> email2FailedTests = new java.util.HashMap<>();
		for (TestData testData : testSuite) {
		    if (testData.isOk() || testData.getNotificationEmail() == null || testData.getNotificationEmail().isEmpty()) {
		        continue;
		    }
		    java.util.List<TestData> failedTests = email2FailedTests.get(testData.getNotificationEmail());
		    if (failedTests == null) {
		        failedTests = new java.util.LinkedList<TestData>();
		        email2FailedTests.put(testData.getNotificationEmail(), failedTests);
		    }
		    failedTests.add(testData);
		}


		//2. Create email messages
		final java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("dd MMM yyyy HH:mm:ss");
		String testStartDate = dateFormat.format(Arte::Arte.getCurrentTime());
		final java.util.Map<Str, PersoComm::OutMessage> msgToSend = new java.util.HashMap<>();
		String baseTestCaseURL = System.getenv(BASE_TEST_CASE_URL_SYS_PROP);
		if (baseTestCaseURL != null && !baseTestCaseURL.endsWith("/")) {
		    baseTestCaseURL += "/";
		}
		for (Str address : email2FailedTests.keySet()) {
		    PersoComm::OutMessage mes = createNotificationMessage(address, email2FailedTests.get(address),
		        testStartDate, baseTestCaseURL);
		    if (mes != null) {
		        msgToSend.put(address, mes);
		    }
		}

		Arte::Arte.commit(); //commit created messages


		//3. Wait for messages sending
		long startTime = System.currentTimeMillis();
		long timeout = AWAIT_NOTIFICATIONS_SENDED_MILLIS;
		if (System.getenv(NOTIFICATION_AWAIT_TIMEOUT_SYS_PROP) != null) {
		    try {
		        timeout = Int.parseLong(System.getenv(NOTIFICATION_AWAIT_TIMEOUT_SYS_PROP));
		    } catch(Exceptions::Exception ex) {
		        //ignore
		    }
		}

		if (debug) {
		    dbgSb.append("Start sending notifications on following addresses: ");
		    for (Str address : msgToSend.keySet()) {
		        dbgSb.append(address);
		        dbgSb.append(" ");
		    }
		    Utils::Debug.sout(dbgMsgSource, dbgSb.toString());
		    dbgSb.setLength(0);
		}

		while (!msgToSend.isEmpty() && (System.currentTimeMillis() - startTime) <= timeout) {
		    java.util.Iterator<java.util.Map.Entry<Str, PersoComm::OutMessage>> iter = msgToSend.entrySet().iterator();
		    while(iter.hasNext()) {
		        final java.util.Map.Entry<Str, PersoComm::OutMessage> addr2Msg = iter.next();
		        if (!addr2Msg.Value.isInDatabase(true)) {
		            if (debug) {
		                Utils::Debug.sout(dbgMsgSource, "Notification was sent to: " + addr2Msg.Key);
		            }
		            iter.remove();
		        }
		    }
		    if (!msgToSend.isEmpty()) {
		        try {
		            Java.Lang::Thread.sleep(1000);
		        } catch (Exceptions::InterruptedException ex) {
		            //ignore
		        }
		    }
		}

		if (!msgToSend.isEmpty()) {
		    java.lang.StringBuilder errMsg = new java.lang.StringBuilder("Timeout on send notifications about failed tests has expired. Notifications to following addresses was not delivered:");
		    for (java.util.Map.Entry<Str, PersoComm::OutMessage> msg : msgToSend.entrySet()) {
		        errMsg.append('\n').append(msg.Key);
		        try {
		            msg.Value.delete();
		        } catch (Exceptions::EntityObjectNotExistsError ex) {
		            errMsg.append(" - ").append(ex.getMessage());
		        }
		    }
		    Arte::Trace.error(errMsg.toString(), Arte::EventSource:TestCase);
		    if (debug) {
		        Utils::Debug.sout(dbgMsgSource, errMsg.toString());
		    }
		} else if (debug) {
		    Utils::Debug.sout(dbgMsgSource, "All notifications successfully delivered.");
		}
	}

	/*Radix::Testing::TestCase.IsTests:buildTestCaseUrlOnIs-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase.IsTests:buildTestCaseUrlOnIs")
	private final  Str buildTestCaseUrlOnIs (Str baseUrlOnIs, org.radixware.kernel.server.test.TestData testData) {
		final java.lang.StringBuilder urlBuilder = new java.lang.StringBuilder();
		urlBuilder.append(baseUrlOnIs);
		urlBuilder.append(testData.getTitle()).append("/");

		//Notes used as test name on IS,
		//so it must be valid java identifier name
		if (testData.getNotes() != null && !testData.getNotes().isEmpty()) {
		    urlBuilder.append(getJavaIdentifierName(testData.getNotes(), '_'));
		} else {
		    urlBuilder.append("_");
		}

		try {
		    java.net.URL url = new java.net.URL(urlBuilder.toString());
		    final java.net.URI uri = new java.net.URI(url.getProtocol(), url.getUserInfo(), url.getHost(),
		            url.getPort(), url.getPath(), url.getQuery(), url.getRef());
		    return uri.toURL().toString();
		} catch (java.net.MalformedURLException | java.net.URISyntaxException ex) {
		    Arte::Trace.error(Arte::Trace.exceptionStackToString(ex), Arte::EventSource:TestCase);
		}

		return null;
	}

	/*Radix::Testing::TestCase.IsTests:getJavaIdentifierName-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase.IsTests:getJavaIdentifierName")
	private static  Str getJavaIdentifierName (Str str, char replacement) {
		if (str == null || str.isEmpty()) {
		    return str;
		}
		final char[] chars = str.toCharArray();
		boolean wasStrChanged = false;
		if (!java.lang.Character.isJavaIdentifierStart(chars[0])) {
		    chars[0] = replacement;
		    wasStrChanged = true;
		}
		for (int index = 1; index < chars.length; index++) {
		    if (!java.lang.Character.isJavaIdentifierPart(chars[index])) {
		        chars[index] = replacement;
		        wasStrChanged = true;
		    }
		}
		return wasStrChanged ? Str.valueOf(chars) : str;
	}

	/*Radix::Testing::TestCase.IsTests:createMessageBody-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase.IsTests:createMessageBody")
	private final  Str createMessageBody (java.util.List<org.radixware.kernel.server.test.TestData> failedTests, Str baseTestCaseUrl) {
		final java.lang.StringBuilder bodyBuilder = new java.lang.StringBuilder("Following tests execution was failed:\n\n");
		for (TestData testData : failedTests) {
		    bodyBuilder.append(testData.getTitle());
		    if (testData.getNotes() != null && !testData.getNotes().isEmpty()) {
		        bodyBuilder.append(": ").append(testData.getNotes());
		    }
		    if (baseTestCaseUrl != null) {
		        String url = buildTestCaseUrlOnIs(baseTestCaseUrl, testData);
		        if (url != null) {
		            bodyBuilder.append("\nURL: ").append(url);
		        }
		    }
		    bodyBuilder.append("\n\n");
		}
		return bodyBuilder.toString();
	}

	/*Radix::Testing::TestCase.IsTests:collectTestCasesFromGroups-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase.IsTests:collectTestCasesFromGroups")
	private final  void collectTestCasesFromGroups (Int testId, java.util.Set<Int> result) {
		final TestCase testCase = TestCase.loadByPK(testId, true);
		if (testCase instanceof TestCase.Group) {    
		    final List<Int> childIds = new java.util.ArrayList<>();
		    try (GroupTestCasesCursor childrenCursor = GroupTestCasesCursor.open(testId);) {
		        while (childrenCursor.next()) {
		            final TestCase childTest = TestCase.loadByPK(childrenCursor.childId, true);
		            if (childTest.active.booleanValue()) {
		                childIds.add(childrenCursor.childId);
		            }
		        }
		    }
		    for (Int childId : childIds) {
		        collectTestCasesFromGroups(childId, result);
		    }
		} else {
		    result.add(testId);
		}
	}

	/*Radix::Testing::TestCase.IsTests:getChildren-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase.IsTests:getChildren")
	public published  java.util.List<Int> getChildren () {
		final List<Int> testIds = new ArrayList<Int>();
		if (testsToExecute != null) {
		    for (TestCase test : testsToExecute) {
		        testIds.add(test.id);
		    }
		} else {
		    final IntegrationServerTestCasesCursor cursor = IntegrationServerTestCasesCursor.open();
		    try {
		        while (cursor.next()) {
		            try {
		                TestCase test = TestCase.loadByPK(cursor.id, false);
		                if (test.groupIdByParent == null) {
		                    testIds.add(cursor.id);
		                } 
		            } catch (Exceptions::DefinitionNotFoundError err) {
		                Arte::Trace.warning("TestCase skipped: " + err.getMessage(), Arte::EventSource:TestCase);
		            }                                   
		        }
		    } finally {
		        cursor.close();
		    }
		}
		return testIds;
	}

	/*Radix::Testing::TestCase.IsTests:beforeInit-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase.IsTests:beforeInit")
	protected published  boolean beforeInit (org.radixware.kernel.server.types.PropValHandlersByIdMap initPropValsById, org.radixware.kernel.server.types.Entity src, org.radixware.kernel.common.enums.EEntityInitializationPhase phase) {
		traceLevelForIS = Arte::EventSeverity:Debug;
		return super.beforeInit(initPropValsById, src, phase);
	}

	/*Radix::Testing::TestCase.IsTests:getIgnoredPropsList-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase.IsTests:getIgnoredPropsList")
	protected published  java.util.List<org.radixware.kernel.common.types.Id> getIgnoredPropsList () {
		List<Types::Id> ignoredProps = super.getIgnoredPropsList();
		if (ignoredProps == null) {
		    ignoredProps = new java.util.ArrayList<Types::Id>(1);
		}
		ignoredProps.add(idof[TestCase.IsTests:testsToExecute]);
		ignoredProps.add(idof[TestCase.IsTests:persistentResultFileName]);
		return ignoredProps;
	}

	/*Radix::Testing::TestCase.IsTests:addTestDataToSuite-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase.IsTests:addTestDataToSuite")
	private final  void addTestDataToSuite (org.radixware.kernel.server.test.TestSuiteData testSuite, org.radixware.kernel.server.test.TestData testData, int[] results) {
		if (testData.getChildren().isEmpty()) {
		    testSuite.regTest(testData);
		    if (testData.isOk()) {
		        results[0]++;
		    } else {
		        results[1]++;
		    }
		} else {
		    for (TestData childData : testData.getChildren()) {
		        addTestDataToSuite(testSuite, childData, results);    
		    }
		}

	}

	/*Radix::Testing::TestCase.IsTests:getTraceLevelForAasCall-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase.IsTests:getTraceLevelForAasCall")
	protected published  org.radixware.kernel.common.enums.EEventSeverity getTraceLevelForAasCall () {
		return traceLevelForIS;
	}


	@Override
	public org.radixware.kernel.server.types.FormHandler.NextDialogsRequest execCommand(org.radixware.kernel.common.types.Id cmdId, org.radixware.kernel.common.types.Id propId, org.apache.xmlbeans.XmlObject input, org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValsById, org.apache.xmlbeans.XmlObject output) throws org.radixware.kernel.common.exceptions.AppException,java.lang.InterruptedException{

			return super.execCommand(cmdId,propId,input,newPropValsById,output);
	}


}

/* Radix::Testing::TestCase.IsTests - Server Meta*/

/*Radix::Testing::TestCase.IsTests-Application Class*/

package org.radixware.ads.Testing.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class TestCase.IsTests_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("acl7B5SI2L2CZBB7COXUQX33KU3QI"),"TestCase.IsTests",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsP43OQXEQ55EGBALANVMCUK3EK4"),

						org.radixware.kernel.common.enums.EClassType.APPLICATION,

						/*Radix::Testing::TestCase.IsTests:Presentations-Entity Object Presentations*/
						/*Class presentation attributes*/
						 new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef(/*Owner Class Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("acl7B5SI2L2CZBB7COXUQX33KU3QI"),
							/*Owner Class Name*/
							"TestCase.IsTests",
							/*Title Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsP43OQXEQ55EGBALANVMCUK3EK4"),
							/*Property presentations*/

							/*Radix::Testing::TestCase.IsTests:Properties-Properties*/
							new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef[]{

									/*Radix::Testing::TestCase.IsTests:resultFileName:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdP7QPL3RRYZC7ZBGWTWZGUQZPRE"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Testing::TestCase.IsTests:persistentResultFileName:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pru4AQTKS6YTBFUTLUF7OGLTTVAK4"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Testing::TestCase.IsTests:emailRoutingKey:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruSEPTLOYFSVEMTGSYQV3VMBDD7A"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Testing::TestCase.IsTests:sendEmailNotificationsToAuthors:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruZDRCFATJ7VGNHPZMMNZV6FA564"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Testing::TestCase.IsTests:testsToExecute:PropertyPresentation-Parent Ref Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadParentTitlePropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruMCZPIXFKNFG3HA5PIHETZ5NXTE"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("sprLEXXOTV4O5AEBO3VECZLSIGRCY"),new org.radixware.kernel.server.meta.presentations.RadConditionDef("<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:PropSqlName TableId=\"tblHW4OSVMS27NRDISQAAAAAAAAAA\" PropId=\"colQP4LKSN3IFHITPAQXU7J72H6FA\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> >= 1</xsc:Sql></xsc:Item></xsc:Sqml>","<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),org.radixware.kernel.server.types.Restrictions.Factory.newInstance(133693439,null,null,null),null,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Testing::TestCase.IsTests:traceLevelForIS:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruLWQKJ3LZERGLDNHPU2WVYA2GOU"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT))
							},
							/*Commands*/
							null,
							/*Sortings*/
							null,
							/*Filters*/
							null,
							/*Editor presentations*/
							new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef[]{

									new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef(
									/*Radix::Testing::TestCase.IsTests:IsTests-Editor Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprJJSKUSIRS5ARJEVQQ5G3R6XQNE"),
									"IsTests",
									org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprBMFQWKU227NRDISQAAAAAAAAAA"),
									3250,
									null,

									/*Radix::Testing::TestCase.IsTests:IsTests:Children-Explorer Items*/
										null
									,
									null,
									(org.radixware.kernel.server.meta.presentations.RadPropertyPresentationAttributes[]) null,
									org.radixware.kernel.common.enums.EEditorPresentationRightsInheritanceMode.FROM_REPLACED,
									null)
							},
							/*Selector presentations*/
							null,
							/*Default selector presentation Id*/
							null,
							/*Presentation Map*/
							org.radixware.kernel.common.utils.Maps.fromArrays(new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprBMFQWKU227NRDISQAAAAAAAAAA")},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprJJSKUSIRS5ARJEVQQ5G3R6XQNE")}),
							/*Object title format*/
							null,
							/*Class catalogs*/
							new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog[]{

									new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog(
									/*Radix::Testing::TestCase.IsTests:All-Dynamic Class Catalog*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eccKGGFFVPJAHOBDA26AAMPGXSZKU"),org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.ELinkMode.VIRTUAL,new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.Item[]{
										new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.ClassRef(org.radixware.kernel.common.types.Id.Factory.loadFrom("acl7B5SI2L2CZBB7COXUQX33KU3QI"),org.radixware.kernel.common.types.Id.Factory.loadFrom("cctCE37MB2JGQAESF2YAAIFIUYFRE"),25.0,true,org.radixware.kernel.common.types.Id.Factory.loadFrom("acl7B5SI2L2CZBB7COXUQX33KU3QI"),null)}
									)
							},
							/*Restrictions*/
							null,null,0),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblHW4OSVMS27NRDISQAAAAAAAAAA"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclPJQ6F4H7UBDWJMTQUMZFCOMZ5Q"),

						/*Radix::Testing::TestCase.IsTests:Properties-Properties*/
						new org.radixware.kernel.server.meta.clazzes.RadPropDef[]{

								/*Radix::Testing::TestCase.IsTests:resultFileName-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdP7QPL3RRYZC7ZBGWTWZGUQZPRE"),"resultFileName",null,org.radixware.kernel.common.enums.EValType.STR,null,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Testing::TestCase.IsTests:os-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdEM26XVOJHZCCLP7G5OCGGF636M"),"os",null,org.radixware.kernel.common.enums.EValType.JAVA_CLASS,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Testing::TestCase.IsTests:persistentResultFileName-User Property*/

								new org.radixware.kernel.server.meta.clazzes.RadUserPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pru4AQTKS6YTBFUTLUF7OGLTTVAK4"),"persistentResultFileName",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5GKO27ND65GAJIK6NRJZMOJTQI"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("tblHW4OSVMS27NRDISQAAAAAAAAAA"),false,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Testing::TestCase.IsTests:emailRoutingKey-User Property*/

								new org.radixware.kernel.server.meta.clazzes.RadUserPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruSEPTLOYFSVEMTGSYQV3VMBDD7A"),"emailRoutingKey",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGWZQKQASUBFZZGDRLSAIYZH7HY"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("tblHW4OSVMS27NRDISQAAAAAAAAAA"),false,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Testing::TestCase.IsTests:sendEmailNotificationsToAuthors-User Property*/

								new org.radixware.kernel.server.meta.clazzes.RadUserPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruZDRCFATJ7VGNHPZMMNZV6FA564"),"sendEmailNotificationsToAuthors",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPB6MAYLW5FFO7CIV33V63OBPLE"),org.radixware.kernel.common.enums.EValType.BOOL,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DEFINE_ALWAYS,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("0")),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblHW4OSVMS27NRDISQAAAAAAAAAA"),false,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Testing::TestCase.IsTests:AWAIT_NOTIFICATIONS_SENDED_MILLIS-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdJRVJXF654FE6TH2VJSJG77QT6E"),"AWAIT_NOTIFICATIONS_SENDED_MILLIS",null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Testing::TestCase.IsTests:testsToExecute-User Property*/

								new org.radixware.kernel.server.meta.clazzes.RadUserRefPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruMCZPIXFKNFG3HA5PIHETZ5NXTE"),"testsToExecute",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsK4A3ESS2UJFU3B4JQQDO5654GQ"),org.radixware.kernel.common.enums.EValType.ARR_REF,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("tblHW4OSVMS27NRDISQAAAAAAAAAA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),false,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Testing::TestCase.IsTests:BASE_TEST_CASE_URL_SYS_PROP-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd5IUR42HUKJDQ5MCH4W6HSWFHL4"),"BASE_TEST_CASE_URL_SYS_PROP",null,org.radixware.kernel.common.enums.EValType.STR,null,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("RDX_TESTCASE_BASE_URL")),org.radixware.kernel.server.meta.clazzes.RadPropReadAccessorProxy.R_ACCESSOR_INSTANCE),

								/*Radix::Testing::TestCase.IsTests:NOTIFICATION_AWAIT_TIMEOUT_SYS_PROP-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdGHJQ6U2LTNETTCUGXMA5X2WEVQ"),"NOTIFICATION_AWAIT_TIMEOUT_SYS_PROP",null,org.radixware.kernel.common.enums.EValType.STR,null,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("RDX_TESTCASE_NOTIFICATION_AWAIT_MILLIS")),org.radixware.kernel.server.meta.clazzes.RadPropReadAccessorProxy.R_ACCESSOR_INSTANCE),

								/*Radix::Testing::TestCase.IsTests:DEBUG_OUTPUT_SYS_PROP-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdTWSKJBNMSNACBMNQIQOFDIASDA"),"DEBUG_OUTPUT_SYS_PROP",null,org.radixware.kernel.common.enums.EValType.STR,null,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("RDX_TESTCASE_DEBUG_OUTPUT")),org.radixware.kernel.server.meta.clazzes.RadPropReadAccessorProxy.R_ACCESSOR_INSTANCE),

								/*Radix::Testing::TestCase.IsTests:traceLevelForIS-User Property*/

								new org.radixware.kernel.server.meta.clazzes.RadUserPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruLWQKJ3LZERGLDNHPU2WVYA2GOU"),"traceLevelForIS",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsK2GL5IE7VZE2BKROICPA5S3P74"),org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("acsFRO5RTQOG7NRDJH2ACQMTAIZT4"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("0")),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblHW4OSVMS27NRDISQAAAAAAAAAA"),false,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Testing::TestCase.IsTests:isCheckResultFileName-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdOBYB5PYKU5D2FHHOFEYY5TNNKQ"),"isCheckResultFileName",null,org.radixware.kernel.common.enums.EValType.BOOL,null,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("1")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE)
						},

						/*Radix::Testing::TestCase.IsTests:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthG2ORZGNH27NRDISQAAAAAAAAAA"),"execute",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.INT),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthL76A62L67NECLN3JUTXU4ZBAVA"),"run",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("resultFileName",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprNACKYMAXGZH5NOY7FRW7A4CF3M"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth5XATY4FH27NRDISQAAAAAAAAAA"),"preCheck",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthINDPQ7M4JNE2DD5GASFAMNSZAE"),"runTestForAasCall",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("testId",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr5EE2V2OG7NCULDJHCJHI6HIICM")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("callerTestId",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprK55VJIQSKVC6LMYNOE3Z5YE3LE"))
								},org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthK4VVUUU7HFCYXGGVHCQO76G5SE"),"executeForRemoteCall",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthG4H3BIFPYRC5TPFRLC2MMSSXBY"),"createNotificationMessage",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("address",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr7MMT5PMM6JCGNFLSBMJDA46BU4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("failedTests",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprZC755EFBH5CVNM3CWKQ2I7GQ6M")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("testStartDate",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprLXBK6RADUFF33OI77C23BNVSUM")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("baseTestCaseUrl",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr6U2AMKNHNBFOBOVYI2PKBMRRYM"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthS654QFNKL5AJDLFUM3LKB7VAZY"),"sendNotificationsToAuthors",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("testSuite",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprHQDJDS5FMJF33BTLSQWXGF3XU4"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthOUUDJBFB3FDZXO6HBG2M3OLVWU"),"buildTestCaseUrlOnIs",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("baseUrlOnIs",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprD7ABQ5ZVNVA5NE2K33CJLPSNAM")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("testData",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprW3Z4D3F2HFBODIZKVGRG5FDR5U"))
								},org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth64PO3XR72BA73LZ5HCBL7UNQCY"),"getJavaIdentifierName",true,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("str",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr6QRWNAVY2ZDKJO4VECU7BRSKTE")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("replacement",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr3TQKVYSC3JALXGXS2R76IRAAQM"))
								},org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthVWEBW3DZHVC33EFHQXVGT3QV7M"),"createMessageBody",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("failedTests",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprIBBCVHRJNRDDNBLWGSUY5TABFQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("baseTestCaseUrl",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprWZ7NYSAOXVDSFBSVSMUILZWFTI"))
								},org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthCGRSNRRPX5EMJK223FADMORCCI"),"collectTestCasesFromGroups",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("testId",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprMDNANJJEXFDGPOCVINDQEG4XW4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("result",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprSVVUMKUW4BE6LAZ4GBKLBJKAEE"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthMKGKB4FK5BDC7L527USXI3KNNA"),"getChildren",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.JAVA_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthGZDY76GQDVEENNFB33JRO5ADCU"),"beforeInit",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("initPropValsById",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprPBY6FCKHWJAFNHW7MNKGZGPPXY")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("src",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprPQPT4MQPFRB3FBVZKHCCLHSWHA")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("phase",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr7JJIBSY7LZBUDKTM5QUHUZKZTU"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthU2YYM5SLUFAZLI3UUSBOLDLMPM"),"getIgnoredPropsList",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,org.radixware.kernel.common.enums.EValType.JAVA_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthQENEDVVHNNF4BBNO5XPOXBLDPA"),"addTestDataToSuite",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("testSuite",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprENFTPBPH7ZCXHGFYWYMRXJN4WU")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("testData",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr24ISHGKUUZC6RK57E5A42LVHDU")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("results",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprFWHXGDH3SJFPJP3ZPVC7EWX5GY"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth4JG27OGWZRERLNHYAE247CKPCU"),"getTraceLevelForAasCall",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,org.radixware.kernel.common.enums.EValType.INT)
						},
						null,
						null,null,null,false);
}

/* Radix::Testing::TestCase.IsTests - Desktop Executable*/

/*Radix::Testing::TestCase.IsTests-Application Class*/

package org.radixware.ads.Testing.explorer;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase.IsTests")
public interface TestCase.IsTests   extends org.radixware.ads.Testing.explorer.TestCase.AbstractGroup  {



















































































































	/*Radix::Testing::TestCase.IsTests:resultFileName:resultFileName-Presentation Property*/


	public class ResultFileName extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public ResultFileName(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase.IsTests:resultFileName:resultFileName")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase.IsTests:resultFileName:resultFileName")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ResultFileName getResultFileName();
	/*Radix::Testing::TestCase.IsTests:persistentResultFileName:persistentResultFileName-Presentation Property*/


	public class PersistentResultFileName extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public PersistentResultFileName(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase.IsTests:persistentResultFileName:persistentResultFileName")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase.IsTests:persistentResultFileName:persistentResultFileName")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public PersistentResultFileName getPersistentResultFileName();
	/*Radix::Testing::TestCase.IsTests:traceLevelForIS:traceLevelForIS-Presentation Property*/


	public class TraceLevelForIS extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public TraceLevelForIS(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public void setServerValue(org.radixware.kernel.common.client.models.items.properties.PropertyValue val){
			Object x = val.getValue();
			org.radixware.kernel.common.enums.EEventSeverity dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EEventSeverity ? (org.radixware.kernel.common.enums.EEventSeverity)x : org.radixware.kernel.common.enums.EEventSeverity.getForValue(((Int)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.INT)).longValue()));
			val.refineValue(dummy);
			super.setServerValue(val);
		}
		@Override
		public Class<org.radixware.kernel.common.enums.EEventSeverity> getValClass(){
			return org.radixware.kernel.common.enums.EEventSeverity.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.enums.EEventSeverity dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EEventSeverity ? (org.radixware.kernel.common.enums.EEventSeverity)x : org.radixware.kernel.common.enums.EEventSeverity.getForValue(((Int)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.INT)).longValue()));
			setValue(dummy);
		}





				














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase.IsTests:traceLevelForIS:traceLevelForIS")
		public  org.radixware.kernel.common.enums.EEventSeverity getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase.IsTests:traceLevelForIS:traceLevelForIS")
		public   void setValue(org.radixware.kernel.common.enums.EEventSeverity val) {
			Value = val;
		}
	}
	public TraceLevelForIS getTraceLevelForIS();
	/*Radix::Testing::TestCase.IsTests:testsToExecute:testsToExecute-Presentation Property*/


	public class TestsToExecute extends org.radixware.kernel.common.client.models.items.properties.PropertyArrRef{
		public TestsToExecute(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadParentRefPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.client.types.ArrRef dummy = ((org.radixware.kernel.common.client.types.ArrRef)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase.IsTests:testsToExecute:testsToExecute")
		public  org.radixware.kernel.common.client.types.ArrRef getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase.IsTests:testsToExecute:testsToExecute")
		public   void setValue(org.radixware.kernel.common.client.types.ArrRef val) {
			Value = val;
		}
	}
	public TestsToExecute getTestsToExecute();
	/*Radix::Testing::TestCase.IsTests:emailRoutingKey:emailRoutingKey-Presentation Property*/


	public class EmailRoutingKey extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public EmailRoutingKey(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase.IsTests:emailRoutingKey:emailRoutingKey")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase.IsTests:emailRoutingKey:emailRoutingKey")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public EmailRoutingKey getEmailRoutingKey();
	/*Radix::Testing::TestCase.IsTests:sendEmailNotificationsToAuthors:sendEmailNotificationsToAuthors-Presentation Property*/


	public class SendEmailNotificationsToAuthors extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public SendEmailNotificationsToAuthors(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Bool dummy = ((Bool)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase.IsTests:sendEmailNotificationsToAuthors:sendEmailNotificationsToAuthors")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase.IsTests:sendEmailNotificationsToAuthors:sendEmailNotificationsToAuthors")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public SendEmailNotificationsToAuthors getSendEmailNotificationsToAuthors();


}

/* Radix::Testing::TestCase.IsTests - Desktop Meta*/

/*Radix::Testing::TestCase.IsTests-Application Class*/

package org.radixware.ads.Testing.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class TestCase.IsTests_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::Testing::TestCase.IsTests:Presentations-Entity Object Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("acl7B5SI2L2CZBB7COXUQX33KU3QI"),
			"Radix::Testing::TestCase.IsTests",
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aclPJQ6F4H7UBDWJMTQUMZFCOMZ5Q"),
			null,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsP43OQXEQ55EGBALANVMCUK3EK4"),null,null,0,

			/*Radix::Testing::TestCase.IsTests:Properties-Properties*/
			new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

					/*Radix::Testing::TestCase.IsTests:resultFileName:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdP7QPL3RRYZC7ZBGWTWZGUQZPRE"),
						"resultFileName",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acl7B5SI2L2CZBB7COXUQX33KU3QI"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom(""),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Testing::TestCase.IsTests:resultFileName:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Testing::TestCase.IsTests:persistentResultFileName:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pru4AQTKS6YTBFUTLUF7OGLTTVAK4"),
						"persistentResultFileName",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5GKO27ND65GAJIK6NRJZMOJTQI"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acl7B5SI2L2CZBB7COXUQX33KU3QI"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.USER,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::Testing::TestCase.IsTests:persistentResultFileName:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Testing::TestCase.IsTests:emailRoutingKey:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruSEPTLOYFSVEMTGSYQV3VMBDD7A"),
						"emailRoutingKey",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGWZQKQASUBFZZGDRLSAIYZH7HY"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acl7B5SI2L2CZBB7COXUQX33KU3QI"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.USER,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Testing::TestCase.IsTests:emailRoutingKey:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Testing::TestCase.IsTests:sendEmailNotificationsToAuthors:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruZDRCFATJ7VGNHPZMMNZV6FA564"),
						"sendEmailNotificationsToAuthors",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPB6MAYLW5FFO7CIV33V63OBPLE"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acl7B5SI2L2CZBB7COXUQX33KU3QI"),
						org.radixware.kernel.common.enums.EValType.BOOL,
						org.radixware.kernel.common.enums.EPropNature.USER,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("0"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::Testing::TestCase.IsTests:sendEmailNotificationsToAuthors:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("acl7B5SI2L2CZBB7COXUQX33KU3QI"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Testing::TestCase.IsTests:testsToExecute:PropertyPresentation-Parent Ref Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruMCZPIXFKNFG3HA5PIHETZ5NXTE"),
						"testsToExecute",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsK4A3ESS2UJFU3B4JQQDO5654GQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acl7B5SI2L2CZBB7COXUQX33KU3QI"),
						org.radixware.kernel.common.enums.EValType.ARR_REF,
						org.radixware.kernel.common.enums.EPropNature.USER,
						4,
						null,
						null,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						false,false,
						false,
						false,
						null,
						false,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLVQETWBQSVCG7IGM3NQK33UX3A"),
						null,
						null,
						true,-1,-1,1,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblHW4OSVMS27NRDISQAAAAAAAAAA"),
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("sprLEXXOTV4O5AEBO3VECZLSIGRCY"),
						133693439,
						133693439,false),

					/*Radix::Testing::TestCase.IsTests:traceLevelForIS:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruLWQKJ3LZERGLDNHPU2WVYA2GOU"),
						"traceLevelForIS",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsK2GL5IE7VZE2BKROICPA5S3P74"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acl7B5SI2L2CZBB7COXUQX33KU3QI"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.USER,
						4,
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acsFRO5RTQOG7NRDJH2ACQMTAIZT4"),
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("0"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::Testing::TestCase.IsTests:traceLevelForIS:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acsFRO5RTQOG7NRDJH2ACQMTAIZT4"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.NONE,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false)
			},
			null,
			null,
			null,
			null,
			new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprJJSKUSIRS5ARJEVQQ5G3R6XQNE")},
			true,false,false);
}

/* Radix::Testing::TestCase.IsTests - Web Meta*/

/*Radix::Testing::TestCase.IsTests-Application Class*/

package org.radixware.ads.Testing.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class TestCase.IsTests_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::Testing::TestCase.IsTests:Presentations-Entity Object Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("acl7B5SI2L2CZBB7COXUQX33KU3QI"),
			"Radix::Testing::TestCase.IsTests",
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aclPJQ6F4H7UBDWJMTQUMZFCOMZ5Q"),
			null,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsP43OQXEQ55EGBALANVMCUK3EK4"),null,null,0,
			null,
			null,
			null,
			null,
			null,
			null,
			false,false,false);
}

/* Radix::Testing::TestCase.IsTests:IsTests - Desktop Meta*/

/*Radix::Testing::TestCase.IsTests:IsTests-Editor Presentation*/

package org.radixware.ads.Testing.explorer;
public final class IsTests_mi{
	private static final class IsTests_DEF extends org.radixware.kernel.common.client.meta.RadEditorPresentationDef{
		IsTests_DEF(){
			super(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprJJSKUSIRS5ARJEVQQ5G3R6XQNE"),
			"IsTests",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("eprBMFQWKU227NRDISQAAAAAAAAAA"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("acl7B5SI2L2CZBB7COXUQX33KU3QI"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("tblHW4OSVMS27NRDISQAAAAAAAAAA"),
			null,
			null,

			/*Radix::Testing::TestCase.IsTests:IsTests:Editor Pages-Editor Presentation Pages*/
			new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

					/*Radix::Testing::TestCase.IsTests:IsTests:Parameters-Editor Page*/

					 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgW337T5LHLTOBDCJFAALOMT5GDM"),"Parameters",org.radixware.kernel.common.types.Id.Factory.loadFrom("acl7B5SI2L2CZBB7COXUQX33KU3QI"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsB6YDLYKYJZCSZDILOHJCN3OGPY"),null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("pru4AQTKS6YTBFUTLUF7OGLTTVAK4"),0,1,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruSEPTLOYFSVEMTGSYQV3VMBDD7A"),0,2,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruZDRCFATJ7VGNHPZMMNZV6FA564"),0,3,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruMCZPIXFKNFG3HA5PIHETZ5NXTE"),0,0,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruLWQKJ3LZERGLDNHPU2WVYA2GOU"),0,4,1,false,false)
					},null)
			},
			new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
				new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epg5F62VEGTBXOBDNTPAAMPGXSZKU")),
				new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgW337T5LHLTOBDCJFAALOMT5GDM"))}
			,

			/*Radix::Testing::TestCase.IsTests:IsTests:Children-Explorer Items*/
				null
			,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
			null,
			0,
			null,
			(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
			3250,0,0);
		}
		@Override
		protected org.radixware.kernel.common.client.models.Model createModelImpl(org.radixware.kernel.common.client.IClientEnvironment userSession) {    return new org.radixware.ads.Testing.explorer.TestCase.IsTests.TestCase.IsTests_DefaultModel.eprBMFQWKU227NRDISQAAAAAAAAAA_ModelAdapter(userSession,this);
		}
	}
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new IsTests_DEF(); 
;
}
/* Radix::Testing::TestCase.IsTests - Localizing Bundle */
package org.radixware.ads.Testing.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class TestCase.IsTests - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Output file");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Выходной файл");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5GKO27ND65GAJIK6NRJZMOJTQI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Error writing results: ");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ошибка при записи результатов: ");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAAKH7AAUU5GWNAPOBZETHLZMMI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Parameters");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Параметры");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsB6YDLYKYJZCSZDILOHJCN3OGPY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Output directory \"{0}\" doesn\'t exists");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Директория \"{0}\" не существует");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBSARV2KRIJBK3BEBK6GML5VANI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Integration Server");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Сервер интеграции");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDGM3YDBXQFHWREBTALOQSXEXUE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Email routing key");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ключ маршрутизации для email");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGWZQKQASUBFZZGDRLSAIYZH7HY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Timeout on send notifications about failed tests has expired. Notifications to following addresses was not delivered:");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Тайм-аут рассылки оповещений о провалившихся тестах истёк. Оповещения для следующих адресов не были доставлены:");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJHFIRURDBZE2XPA7ZB5MM3VBSE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.EVENT_CODE,org.radixware.kernel.common.enums.EEventSeverity.EVENT,"App",java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Trace level for IS");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Уровень трассировки для СИ");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsK2GL5IE7VZE2BKROICPA5S3P74"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Test cases to be executed");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Тесты для выполнения");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsK4A3ESS2UJFU3B4JQQDO5654GQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"All active tests");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Все активные тесты");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLVQETWBQSVCG7IGM3NQK33UX3A"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Error creating a notification on unsuccessful tests to the address \'%s\'");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ошибка создания оповещения о проваленных тестах на адрес \'%s\'");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsND4JIP5GV5EVZM5EXEMKLD2GJQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Output file name not specified");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Не указано имя выходного файла");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOEZWO25MLJHVFNJX57DW2L5ADQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Run All Test Cases on IS");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Запустить все тесты для СИ");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsP43OQXEQ55EGBALANVMCUK3EK4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Send Email notifications");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Рассылать оповещения на email");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPB6MAYLW5FFO7CIV33V63OBPLE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Output file must be specified");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Должен быть указан выходной файл");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQ7HXL3C6UVET5CADWAFIMIGVTY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(TestCase.IsTests - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbacl7B5SI2L2CZBB7COXUQX33KU3QI"),"TestCase.IsTests - Localizing Bundle",$$$items$$$);
}
