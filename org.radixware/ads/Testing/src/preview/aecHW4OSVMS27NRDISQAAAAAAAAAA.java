
/* Radix::Testing::TestCase - Server Executable*/

/*Radix::Testing::TestCase-Entity Class*/

package org.radixware.ads.Testing.server;

import org.radixware.kernel.common.types.IKernelEnum;


@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase")
public abstract published class TestCase  extends org.radixware.ads.Types.server.Entity  implements org.radixware.ads.CfgManagement.server.ICfgReferencedObject,org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {

	protected static class TraceBufferImpl implements org.radixware.kernel.server.trace.TraceBuffer {

	    private final TestCase testCase;
	    private Arte::EventSeverity maxSeverity = Arte::EventSeverity:Debug;

	    public TraceBufferImpl(final TestCase testCase) {
	        this.testCase = testCase;
	    }

	    public void put(org.radixware.kernel.common.trace.TraceItem traceItem) {
	        final Arte::EventSeverity severity = traceItem.severity;
	        if (severity != null && severity.Value.longValue() > maxSeverity.Value.longValue()) {
	            if (severity.Value.longValue() > Arte::EventSeverity:Event.Value.longValue() && testCase.isExpected(traceItem) == Bool.TRUE) {
	                return;
	            }
	            maxSeverity = severity;
	        }
	    }
	}
	/**Metainformation accessor method*/
	@SuppressWarnings("unused")
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return TestCase_mi.rdxMeta;}

	/*Radix::Testing::TestCase:Nested classes-Nested Classes*/

	/*Radix::Testing::TestCase:CfgObjectLookupAdvizor-Server Dynamic Class*/


	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:CfgObjectLookupAdvizor")
	private static class CfgObjectLookupAdvizor  implements org.radixware.ads.CfgManagement.server.ICfgObjectLookupAdvizor,org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {


		/**Metainformation accessor method*/
		@SuppressWarnings("unused")
		public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return TestCase_mi.rdxMeta_adcNJYOKUF6BFH4FCUYD4QHB5MVV4;}

		/*Radix::Testing::TestCase:CfgObjectLookupAdvizor:Nested classes-Nested Classes*/

		/*Radix::Testing::TestCase:CfgObjectLookupAdvizor:Properties-Properties*/





























		/*Radix::Testing::TestCase:CfgObjectLookupAdvizor:Methods-Methods*/

		/*Radix::Testing::TestCase:CfgObjectLookupAdvizor:getCfgObjectsByExtGuid-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:CfgObjectLookupAdvizor:getCfgObjectsByExtGuid")
		public published  java.util.List<org.radixware.ads.Types.server.Entity> getCfgObjectsByExtGuid (Str extGuid, boolean considerContext, org.radixware.ads.Types.server.Entity context) {
			if (extGuid == null) {
			    return java.util.Collections.emptyList();
			}
			java.util.ArrayList<Types::Entity> tests = new java.util.ArrayList<Types::Entity>(1);
			try (final GetTestCaseByExtGuidCursor cursor = GetTestCaseByExtGuidCursor.open(extGuid)) {
			    while (cursor.next()) {
			        tests.add(cursor.testCase);
			    }
			}
			return tests;
		}


	}

	/*Radix::Testing::TestCase:Properties-Properties*/

	/*Radix::Testing::TestCase:resultComment-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:resultComment")
	public published  java.sql.Clob getResultComment() {
		return resultComment;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:resultComment")
	public published   void setResultComment(java.sql.Clob val) {
		resultComment = val;
	}

	/*Radix::Testing::TestCase:parentId-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:parentId")
	public published  Int getParentId() {
		return parentId;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:parentId")
	public published   void setParentId(Int val) {
		parentId = val;
	}

	/*Radix::Testing::TestCase:startTime-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:startTime")
	public published  java.sql.Timestamp getStartTime() {
		return startTime;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:startTime")
	public published   void setStartTime(java.sql.Timestamp val) {
		startTime = val;
	}

	/*Radix::Testing::TestCase:notes-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:notes")
	public published  Str getNotes() {
		return notes;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:notes")
	public published   void setNotes(Str val) {
		notes = val;
	}

	/*Radix::Testing::TestCase:parent-Parent Reference*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:parent")
	public published  org.radixware.ads.Testing.server.TestCase getParent() {
		return parent;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:parent")
	public published   void setParent(org.radixware.ads.Testing.server.TestCase val) {
		parent = val;
	}

	/*Radix::Testing::TestCase:actor-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:actor")
	public published  Str getActor() {
		return actor;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:actor")
	public published   void setActor(Str val) {
		actor = val;
	}

	/*Radix::Testing::TestCase:id-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:id")
	public published  Int getId() {
		return id;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:id")
	public published   void setId(Int val) {
		id = val;
	}

	/*Radix::Testing::TestCase:traceProfile-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:traceProfile")
	public published  Str getTraceProfile() {
		return traceProfile;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:traceProfile")
	public published   void setTraceProfile(Str val) {
		traceProfile = val;
	}

	/*Radix::Testing::TestCase:finishTime-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:finishTime")
	public published  java.sql.Timestamp getFinishTime() {
		return finishTime;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:finishTime")
	public published   void setFinishTime(java.sql.Timestamp val) {
		finishTime = val;
	}

	/*Radix::Testing::TestCase:classGuid-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:classGuid")
	public published  Str getClassGuid() {
		return classGuid;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:classGuid")
	public published   void setClassGuid(Str val) {
		classGuid = val;
	}

	/*Radix::Testing::TestCase:result-Column-Based Property*/








			















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:result")
	public published  org.radixware.kernel.common.enums.EEventSeverity getResult() {
		return result;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:result")
	public published   void setResult(org.radixware.kernel.common.enums.EEventSeverity val) {
		result = val;
	}

	/*Radix::Testing::TestCase:active-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:active")
	public published  Bool getActive() {
		return active;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:active")
	public published   void setActive(Bool val) {
		active = val;
	}

	/*Radix::Testing::TestCase:groupId-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:groupId")
	public published  Int getGroupId() {
		return groupId;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:groupId")
	public published   void setGroupId(Int val) {
		groupId = val;
	}

	/*Radix::Testing::TestCase:groupRef-Parent Reference*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:groupRef")
	public published  org.radixware.ads.Testing.server.TestCase getGroupRef() {
		return groupRef;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:groupRef")
	public published   void setGroupRef(org.radixware.ads.Testing.server.TestCase val) {
		groupRef = val;
	}

	/*Radix::Testing::TestCase:extGuid-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:extGuid")
	public published  Str getExtGuid() {
		return extGuid;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:extGuid")
	public published   void setExtGuid(Str val) {
		extGuid = val;
	}

	/*Radix::Testing::TestCase:seq-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:seq")
	public published  Int getSeq() {
		return seq;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:seq")
	public published   void setSeq(Int val) {
		seq = val;
	}

	/*Radix::Testing::TestCase:seqTitle-Dynamic Property*/



	protected Str seqTitle=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:seqTitle")
	protected published  Str getSeqTitle() {

		if(seq.longValue() <= 0) {
		    return "";
		}
		return Int.toString(seq.longValue());
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:seqTitle")
	protected published   void setSeqTitle(Str val) {
		seqTitle = val;
	}

	/*Radix::Testing::TestCase:classTitle-Dynamic Property*/



	protected Str classTitle=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:classTitle")
	public published  Str getClassTitle() {

		return getClassDefinitionTitle();
	}

	/*Radix::Testing::TestCase:failEventSeverity-Dynamic Property*/



	protected org.radixware.kernel.common.enums.EEventSeverity failEventSeverity=org.radixware.kernel.common.enums.EEventSeverity.getForValue((Int)org.radixware.kernel.common.defs.value.ValAsStr.fromStr("2",org.radixware.kernel.common.enums.EValType.INT));











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:failEventSeverity")
	public published  org.radixware.kernel.common.enums.EEventSeverity getFailEventSeverity() {
		return failEventSeverity;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:failEventSeverity")
	public published   void setFailEventSeverity(org.radixware.kernel.common.enums.EEventSeverity val) {
		failEventSeverity = val;
	}

	/*Radix::Testing::TestCase:isGroup-Dynamic Property*/



	protected Bool isGroup=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:isGroup")
	public published  Bool getIsGroup() {

		return false;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:isGroup")
	public published   void setIsGroup(Bool val) {
		isGroup = val;
	}

	/*Radix::Testing::TestCase:thereIsChilds-Dynamic Property*/



	protected Bool thereIsChilds=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:thereIsChilds")
	public published  Bool getThereIsChilds() {

		return hasChildTests();
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:thereIsChilds")
	public published   void setThereIsChilds(Bool val) {
		thereIsChilds = val;
	}

	/*Radix::Testing::TestCase:resultCommentAsStr-Dynamic Property*/



	protected Str resultCommentAsStr=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:resultCommentAsStr")
	public published  Str getResultCommentAsStr() {

		return Arte::Arte.objVal2valAsStr(resultComment, Meta::ValType:CLOB);
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:resultCommentAsStr")
	public published   void setResultCommentAsStr(Str val) {

		resultComment = (java.sql.Clob)Arte::Arte.valAsStr2ObjVal(val, Meta::ValType:CLOB);
	}

	/*Radix::Testing::TestCase:parentIdByGroup-Expression Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:parentIdByGroup")
	  Int getParentIdByGroup() {
		return parentIdByGroup;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:parentIdByGroup")
	   void setParentIdByGroup(Int val) {
		parentIdByGroup = val;
	}

	/*Radix::Testing::TestCase:hasChildsByParent-Expression Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:hasChildsByParent")
	private final  Bool getHasChildsByParent() {
		return hasChildsByParent;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:hasChildsByParent")
	private final   void setHasChildsByParent(Bool val) {
		hasChildsByParent = val;
	}

	/*Radix::Testing::TestCase:canHaveChilds-Dynamic Property*/



	protected Bool canHaveChilds=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:canHaveChilds")
	protected published  Bool getCanHaveChilds() {

		return canHaveChilds();
	}

	/*Radix::Testing::TestCase:isParent-Dynamic Property*/



	protected Bool isParent=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:isParent")
	 published  Bool getIsParent() {

		return canHaveChildsByParent();
	}

	/*Radix::Testing::TestCase:pathInTree-Dynamic Property*/



	protected org.radixware.kernel.common.types.ArrStr pathInTree=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:pathInTree")
	  org.radixware.kernel.common.types.ArrStr getPathInTree() {
		return pathInTree;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:pathInTree")
	   void setPathInTree(org.radixware.kernel.common.types.ArrStr val) {
		pathInTree = val;
	}

	/*Radix::Testing::TestCase:authorName-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:authorName")
	public published  Str getAuthorName() {
		return authorName;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:authorName")
	public published   void setAuthorName(Str val) {
		authorName = val;
	}

	/*Radix::Testing::TestCase:author-Parent Reference*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:author")
	public published  org.radixware.ads.Acs.server.User getAuthor() {
		return author;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:author")
	public published   void setAuthor(org.radixware.ads.Acs.server.User val) {
		author = val;
	}

	/*Radix::Testing::TestCase:lastIsExecDate-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:lastIsExecDate")
	public published  java.sql.Timestamp getLastIsExecDate() {
		return lastIsExecDate;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:lastIsExecDate")
	public published   void setLastIsExecDate(java.sql.Timestamp val) {
		lastIsExecDate = val;
	}

	/*Radix::Testing::TestCase:lastIsSuccessDate-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:lastIsSuccessDate")
	public published  java.sql.Timestamp getLastIsSuccessDate() {
		return lastIsSuccessDate;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:lastIsSuccessDate")
	public published   void setLastIsSuccessDate(java.sql.Timestamp val) {
		lastIsSuccessDate = val;
	}

	/*Radix::Testing::TestCase:notificationEmail-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:notificationEmail")
	public published  Str getNotificationEmail() {

		if (internal[notificationEmail] == null && author != null) {
		    return author.email;
		}
		return internal[notificationEmail];
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:notificationEmail")
	public published   void setNotificationEmail(Str val) {
		notificationEmail = val;
	}

	/*Radix::Testing::TestCase:seqIsFailCount-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:seqIsFailCount")
	public published  Int getSeqIsFailCount() {
		return seqIsFailCount;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:seqIsFailCount")
	public published   void setSeqIsFailCount(Int val) {
		seqIsFailCount = val;
	}

	/*Radix::Testing::TestCase:cpuNanos-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:cpuNanos")
	public published  Int getCpuNanos() {
		return cpuNanos;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:cpuNanos")
	public published   void setCpuNanos(Int val) {
		cpuNanos = val;
	}

	/*Radix::Testing::TestCase:dbNanos-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:dbNanos")
	public published  Int getDbNanos() {
		return dbNanos;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:dbNanos")
	public published   void setDbNanos(Int val) {
		dbNanos = val;
	}

	/*Radix::Testing::TestCase:extNanos-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:extNanos")
	public published  Int getExtNanos() {
		return extNanos;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:extNanos")
	public published   void setExtNanos(Int val) {
		extNanos = val;
	}

	/*Radix::Testing::TestCase:queueNanos-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:queueNanos")
	public published  Int getQueueNanos() {
		return queueNanos;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:queueNanos")
	public published   void setQueueNanos(Int val) {
		queueNanos = val;
	}

	/*Radix::Testing::TestCase:execDurationStr-Dynamic Property*/



	protected Str execDurationStr=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:execDurationStr")
	public published  Str getExecDurationStr() {
		return execDurationStr;
	}

	/*Radix::Testing::TestCase:IGNORE_COPY_PROPS-Dynamic Property*/



	protected static java.util.Set<org.radixware.kernel.common.types.Id> IGNORE_COPY_PROPS=java.util.Collections.unmodifiableSet(new java.util.HashSet<Types::Id>() {
	    {       
	        this.add(idof[TestCase:extGuid]);
	        this.add(idof[TestCase:startTime]);
	        this.add(idof[TestCase:finishTime]);
	        this.add(idof[TestCase:resultComment]);
	        this.add(idof[TestCase:result]);
	    }
	});;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:IGNORE_COPY_PROPS")
	private static final  java.util.Set<org.radixware.kernel.common.types.Id> getIGNORE_COPY_PROPS() {
		return IGNORE_COPY_PROPS;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:IGNORE_COPY_PROPS")
	private static final   void setIGNORE_COPY_PROPS(java.util.Set<org.radixware.kernel.common.types.Id> val) {
		IGNORE_COPY_PROPS = val;
	}

	/*Radix::Testing::TestCase:groupIdByParent-Expression Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:groupIdByParent")
	  Int getGroupIdByParent() {
		return groupIdByParent;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:groupIdByParent")
	   void setGroupIdByParent(Int val) {
		groupIdByParent = val;
	}





























































































































































































































































	/*Radix::Testing::TestCase:Methods-Methods*/

	/*Radix::Testing::TestCase:preCheck-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:preCheck")
	public published  void preCheck () {

	}

	/*Radix::Testing::TestCase:traceDebug-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:traceDebug")
	public final published  void traceDebug (Str mess) {
		Arte::Trace.debug(mess, Arte::EventSource:TestCase);


	}

	/*Radix::Testing::TestCase:traceWarning-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:traceWarning")
	public final published  void traceWarning (Str mess) {
		Arte::Trace.warning(mess,Arte::EventSource:TestCase);

	}

	/*Radix::Testing::TestCase:execute-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:execute")
	public abstract published  org.radixware.kernel.common.enums.EEventSeverity execute ();

	/*Radix::Testing::TestCase:traceError-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:traceError")
	public final published  void traceError (Str mess) {
		Arte::Trace.error(mess,Arte::EventSource:TestCase);

	}

	/*Radix::Testing::TestCase:run-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:run")
	public final published  void run () {
		Int id = id;
		Arte::Trace.enterContext(Arte::EventContextType:TestCase, id);
		Object traceTargetHandler = Arte::Trace.addContextProfile(traceProfile, this);

		org.radixware.kernel.server.monitoring.ArteWaitStats beforeStats = null;
		Object traceTarget = null;
		final org.radixware.kernel.server.trace.Trace trace = Arte::Arte.getInstance().Trace;;
		try {
		    resultCommentAsStr = "No errors occurred during test";
		    try {
		        preCheck();
		    } catch (Throwable e) {
		        result = Arte::EventSeverity:Error;
		        resultCommentAsStr = Arte::Arte.getCurrentTime() + ": Test precheck failed: " + Arte::Trace.exceptionStackToString(e);
		        traceError(resultCommentAsStr);
		        finishTime = Arte::Arte.getCurrentTime();
		        return;
		    }

		    TraceBufferImpl traceBuffer = new TraceBufferImpl(this);
		    traceTarget = trace.addTargetBuffer(traceProfile, traceBuffer, "TestCase[" + id + "]");

		    traceEvent("Test started: " + calcTitle());
		    try {
		        startTime = Arte::Arte.getCurrentTime();

		        if (Arte::Arte.getUserName() != null) {
		            actor = Arte::Arte.getUserName();
		        }

		        Arte::Arte.commit();//testcase can do rollbacks, but we want to store startTime, etc.

		        beforeStats = Arte::Arte.getInstance().getProfiler().WaitStatsSnapshot;
		        result = execute();

		        if (result == null) {
		            result = Arte::EventSeverity:Event;
		        }

		        if (result.Value.longValue() <= Arte::EventSeverity:Event.Value.longValue()) {
		            Arte::EventSeverity failSeverity = failEventSeverity == null ? Arte::EventSeverity:Warning : failEventSeverity;
		            if (traceBuffer.maxSeverity.Value.longValue() >= failSeverity.Value.longValue()) {
		                result = Arte::EventSeverity:Error;
		                resultCommentAsStr = "Event log contains a record with severity equal or higher than " + failSeverity.Name;
		            }
		        }

		        finishTime = Arte::Arte.getCurrentTime();

		        Arte::Arte.commit();
		    } catch (Throwable e) {
		        Arte::Arte.rollback();
		        result = Arte::EventSeverity:Error;
		        resultCommentAsStr = Arte::Arte.getCurrentTime() + ": Test execution failed: " + Arte::Trace.exceptionStackToString(e);
		        traceError(resultCommentAsStr);
		        finishTime = Arte::Arte.getCurrentTime();
		    }
		    traceEvent("Test finished with result " + result.Name);

		} finally {
		    if (beforeStats != null) {
		        final org.radixware.kernel.server.monitoring.ArteWaitStats afterStats = Arte::Arte.getInstance().getProfiler().WaitStatsSnapshot;
		        final org.radixware.kernel.server.monitoring.ArteWaitStats resultStats = beforeStats.substractFrom(afterStats);
		        cpuNanos = resultStats.CpuNanos;
		        dbNanos = resultStats.DbNanos;
		        extNanos = resultStats.ExtNanos;
		        queueNanos = Arte::Arte.getInstance().getInstance().isUseActiveArteLimits() ? resultStats.QueueNanos : -1;
		    }

		    try {
		        afterExecute();
		        update();
		    } catch (Exceptions::Throwable e) {
		        traceError(Utils::ExceptionTextFormatter.exceptionStackToString(e));
		    }

		    Arte::Trace.leaveContext(Arte::EventContextType:TestCase, id); //проблема геттера (кидает breakError в finally)
		    Arte::Trace.delContextProfile(traceTargetHandler);
		    if (traceTarget != null) {
		        trace.delTarget(traceTarget);
		    }
		}


	}

	/*Radix::Testing::TestCase:traceEvent-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:traceEvent")
	public final published  void traceEvent (Str mess) {
		Arte::Trace.event(mess,Arte::EventSource:TestCase);

	}

	/*Radix::Testing::TestCase:onCommand_Rerun-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:onCommand_Rerun")
	public published  void onCommand_Rerun (org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValHandlersById) {
		run();


	}

	/*Radix::Testing::TestCase:assertTrue-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:assertTrue")
	public static published  void assertTrue (Str message, boolean condition) {
		if (!condition) {
		    fail(message);
		}
	}

	/*Radix::Testing::TestCase:assertTrue-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:assertTrue")
	public static published  void assertTrue (boolean condition) {
		assertTrue(null, condition);
	}

	/*Radix::Testing::TestCase:assertFalse-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:assertFalse")
	public static published  void assertFalse (Str message, boolean condition) {
		if (condition) {
		    fail(message);
		}
	}

	/*Radix::Testing::TestCase:assertFalse-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:assertFalse")
	public static published  void assertFalse (boolean condition) {
		assertFalse(null, condition);
	}

	/*Radix::Testing::TestCase:fail-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:fail")
	protected static published  void fail (Str message) {
		throw new AssertionError(message == null ? "" : message);
	}

	/*Radix::Testing::TestCase:fail-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:fail")
	protected static published  void fail () {
		fail(null);
	}

	/*Radix::Testing::TestCase:assertClobsEquals-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:assertClobsEquals")
	private static  void assertClobsEquals (Str message, java.sql.Clob expected, java.sql.Clob actual) {
		final String expectedValue;
		try {
		    expectedValue = expected.getSubString(1, (int) expected.length());
		} catch (Exceptions::SQLException ex) {
		    throw new Error(ex.getMessage(), ex);
		}
		final String actualValue;
		try {
		    actualValue = actual.getSubString(1, (int) actual.length());
		} catch (Exceptions::SQLException ex) {
		    throw new Error(ex.getMessage(), ex);
		}
		assertEquals(message, expectedValue, actualValue);

	}

	/*Radix::Testing::TestCase:assertEquals-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:assertEquals")
	public static published  void assertEquals (Str message, java.lang.Object expected, java.lang.Object actual) {
		if (expected == null && actual == null) {
		    return;
		}
		if (expected != null && expected.equals(actual)) {
		    return;
		}
		if ((expected instanceof Clob) && (actual instanceof Clob)) {
		    assertClobsEquals(message, (Clob) expected, (Clob) actual);
		    return;
		}
		fail(format(message, expected, actual));

	}

	/*Radix::Testing::TestCase:assertEquals-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:assertEquals")
	public static published  void assertEquals (java.lang.Object expected, java.lang.Object actual) {
		assertEquals(null, expected, actual);
	}

	/*Radix::Testing::TestCase:assertEquals-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:assertEquals")
	public static published  void assertEquals (long expected, long actual) {
		assertEquals(null, expected, actual);
	}

	/*Radix::Testing::TestCase:assertEquals-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:assertEquals")
	public static published  void assertEquals (Str message, long expected, long actual) {
		assertEquals(message, (Long) expected, (Long) actual);
	}

	/*Radix::Testing::TestCase:assertNotNull-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:assertNotNull")
	public published  void assertNotNull (Str message, java.lang.Object object) {
		assertTrue(message, object != null);
	}

	/*Radix::Testing::TestCase:assertNotNull-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:assertNotNull")
	public published  void assertNotNull (java.lang.Object object) {
		assertNotNull(null, object);
	}

	/*Radix::Testing::TestCase:assertNull-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:assertNull")
	public published  void assertNull (Str message, java.lang.Object object) {
		assertTrue(message, object == null);
	}

	/*Radix::Testing::TestCase:assertNull-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:assertNull")
	public published  void assertNull (java.lang.Object object) {
		assertNull(null, object);
	}

	/*Radix::Testing::TestCase:format-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:format")
	private static  Str format (Str message, java.lang.Object expected, java.lang.Object actual) {
		String formatted = "";
		if (message != null && !message.equals("")) {
		    formatted = message + " ";
		}
		String expectedString = String.valueOf(expected);
		String actualString = String.valueOf(actual);
		if (expected instanceof IKernelEnum) {
		    expectedString = ((IKernelEnum)expected).Value + " (" + expectedString + ")";
		}
		if (actual instanceof IKernelEnum) {
		    actualString = ((IKernelEnum)actual).Value + " (" + actualString + ")";
		}
		if (expectedString.equals(actualString)) {
		    return formatted + "expected: "
		            + formatClassAndValue(expected, expectedString)
		            + " but was: " + formatClassAndValue(actual, actualString);
		} else {
		    return formatted + "expected:<" + expectedString + "> but was:<"
		            + actualString + ">";
		}

	}

	/*Radix::Testing::TestCase:formatClassAndValue-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:formatClassAndValue")
	private static  Str formatClassAndValue (java.lang.Object value, Str valueString) {
		String className = value == null ? "null" : value.getClass().getName();
		return className + "<" + valueString + ">";

	}

	/*Radix::Testing::TestCase:isExpected-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:isExpected")
	public published  Bool isExpected (org.radixware.kernel.common.trace.TraceItem traceItem) {
		return false;
	}

	/*Radix::Testing::TestCase:afterInit-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:afterInit")
	protected published  void afterInit (org.radixware.kernel.server.types.Entity src, org.radixware.kernel.common.enums.EEntityInitializationPhase phase) {
		actor = Arte::Arte.getUserName();
		result = Arte::EventSeverity:None;
		startTime = Arte::Arte.getCurrentTime();
		finishTime = startTime;

		super.afterInit(src, phase);
	}

	/*Radix::Testing::TestCase:loadByPK-System Method*/

	@SuppressWarnings("unused")
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:loadByPK")
	public static published  org.radixware.ads.Testing.server.TestCase loadByPK (Int id, boolean checkExistance) {
	final java.util.HashMap<org.radixware.kernel.common.types.Id,Object> pkValsMap = new java.util.HashMap<org.radixware.kernel.common.types.Id,Object>(5);
			if(id==null) return null;
			pkValsMap.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("colN3UZ2Z4S27NRDISQAAAAAAAAAA"),id);
	org.radixware.kernel.server.types.Pid pid = new org.radixware.kernel.server.types.Pid(org.radixware.ads.mdlPEKYFVDRVZHGZCBQQDY2NOYFOY.server.pdcArte______________________.__getArteInstanceInternal(),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblHW4OSVMS27NRDISQAAAAAAAAAA"),pkValsMap);
		try{
		return (
		org.radixware.ads.Testing.server.TestCase) org.radixware.ads.mdlPEKYFVDRVZHGZCBQQDY2NOYFOY.server.pdcArte______________________.__getArteInstanceInternal().getEntityObject(pid,null,checkExistance);
	}catch(org.radixware.kernel.server.exceptions.EntityObjectNotExistsError e){
	return null;
	}

	}

	/*Radix::Testing::TestCase:beforeCreate-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:beforeCreate")
	protected published  boolean beforeCreate (org.radixware.kernel.server.types.Entity src) {
		if(extGuid == null){
		    java.util.UUID uuid = java.util.UUID.randomUUID();
		    long hi = uuid.getMostSignificantBits();
		    long lo = uuid.getLeastSignificantBits();
		    extGuid = Utils::Hex.encode(java.nio.ByteBuffer.allocate(16).putLong(hi).putLong(lo).array());
		}
		if (groupId != null) {
		    seq = ((TestCase.Group) groupRef).getChildsCountFromDb() + 1;
		} else {
		    seq = 0;
		}

		return super.beforeCreate(src);
	}

	/*Radix::Testing::TestCase:copyPropVal-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:copyPropVal")
	public published  void copyPropVal (org.radixware.kernel.server.meta.clazzes.RadPropDef prop, org.radixware.kernel.server.types.Entity src) {
		if (!IGNORE_COPY_PROPS.contains(prop.getId())) {
		    super.copyPropVal(prop, src);
		}
	}

	/*Radix::Testing::TestCase:beforeDelete-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:beforeDelete")
	protected published  boolean beforeDelete () {
		if (groupId != null) {
		    DecreaseChildSeqNumbersStatement.execute(groupId, seq);
		}
		if (isGroup.booleanValue()) {
		    ResetChildSeqNumbersStatement.execute(id);
		}
		return super.beforeDelete();
	}

	/*Radix::Testing::TestCase:exportToXml-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:exportToXml")
	protected  org.radixware.ads.Testing.common.ImpExpXsd.TestCaseDocument exportToXml (boolean exportChilds, boolean isRoot) {
		ImpExpXsd:TestCaseDocument xDoc = ImpExpXsd:TestCaseDocument.Factory.newInstance();
		ImpExpXsd:TestCase xTest = xDoc.addNewTestCase();
		xTest.ExtGuid = extGuid;
		xTest.GroupGuid = groupRef != null ? groupRef.extGuid : null;
		xTest.Notes = notes;
		xTest.RunOnIs = active;
		xTest.TraceProfile = traceProfile;

		if (parent != null) {
		    xTest.ParentGuid = parent.extGuid;
		    xTest.ParentClassGuid = parent.getClassDefinitionId();
		} else if (canHaveChildsByParent()) {
		    xTest.ParentGuid = null;
		    xTest.ParentClassGuid = getClassDefinitionId();
		}

		if (exportChilds) {
		    exportChilds(xTest);
		}

		exportExtensionProps(xTest, isRoot);
		CfgManagement::ImpExpUtils.exportEntity(xTest, this, true, getIgnoredPropsList());
		return xDoc;
	}

	/*Radix::Testing::TestCase:exportExtensionProps-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:exportExtensionProps")
	protected published  void exportExtensionProps (org.radixware.ads.Testing.common.ImpExpXsd.TestCase xml, boolean isRoot) {
		//for overwrite
	}

	/*Radix::Testing::TestCase:exportThis-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:exportThis")
	protected  void exportThis (org.radixware.ads.CfgManagement.server.CfgExportData data, boolean exportChilds, boolean isRoot) {
		data.itemClassId = idof[CfgItem.TestCaseSingle];
		data.object = this;
		data.objectRid = extGuid;

		//Экспортируем рекурсивно детей в xml только для корневого элемента, 
		//для дочерних элементов это не нужно и будет только занимать лишнее место 
		//в файле экспорта пакета конфигурации
		data.data = exportToXml(isRoot && exportChilds, isRoot); 
		data.fileContent = data.data;

		if (exportChilds) {
		    exportChilds(data);
		}
	}

	/*Radix::Testing::TestCase:loadByPidStr-System Method*/

	@SuppressWarnings("unused")
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:loadByPidStr")
	public static published  org.radixware.ads.Testing.server.TestCase loadByPidStr (Str pidAsStr, boolean checkExistance) {
	org.radixware.kernel.server.types.Pid pid = new org.radixware.kernel.server.types.Pid(org.radixware.ads.mdlPEKYFVDRVZHGZCBQQDY2NOYFOY.server.pdcArte______________________.__getArteInstanceInternal(),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblHW4OSVMS27NRDISQAAAAAAAAAA"),pidAsStr);
		try{
		return (
		org.radixware.ads.Testing.server.TestCase) org.radixware.ads.mdlPEKYFVDRVZHGZCBQQDY2NOYFOY.server.pdcArte______________________.__getArteInstanceInternal().getEntityObject(pid,null,checkExistance);
	}catch(org.radixware.kernel.server.exceptions.EntityObjectNotExistsError e){
	return null;
	}

	}

	/*Radix::Testing::TestCase:loadByGuid-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:loadByGuid")
	public static published  org.radixware.ads.Testing.server.TestCase loadByGuid (Str extGuid, org.radixware.kernel.common.types.Id classId) {
		GetTestCaseByExtGuidCursor cursor = GetTestCaseByExtGuidCursor.open(extGuid);
		try {
		    if (cursor.next())
		        if (classId == null || cursor.testCase.getClassDefinitionId() == classId) {
		            return cursor.testCase;
		        }
		    return null;
		} finally {
		    cursor.close();
		}
	}

	/*Radix::Testing::TestCase:create-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:create")
	public static published  org.radixware.ads.Testing.server.TestCase create (org.radixware.kernel.common.types.Id classId, Str extGuid) {
		TestCase obj = (TestCase) Arte::Arte.newObject(classId);
		obj.init();
		obj.extGuid = extGuid;
		return obj;
	}

	/*Radix::Testing::TestCase:updateFromCfgItem-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:updateFromCfgItem")
	public published  void updateFromCfgItem (org.radixware.ads.Testing.server.CfgItem.TestCaseSingle cfgItem, org.radixware.ads.CfgManagement.server.ICfgImportHelper helper) {
		Int groupId = cfgItem.getGroupRef() != null ? cfgItem.getGroupRef().id : null;
		Int parentId = cfgItem.getParentRef() != null ? cfgItem.getParentRef().id : null;

		importThis(cfgItem.myData.TestCase, groupId, parentId, helper, false);
	}

	/*Radix::Testing::TestCase:importThis-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:importThis")
	public published  void importThis (org.radixware.ads.Testing.common.ImpExpXsd.TestCase xml, Int groupId, Int parentId, org.radixware.ads.CfgManagement.server.ICfgImportHelper helper, boolean importChilds) {
		reportOwnerChanged(helper, parentId, parentId, groupId, groupId);

		traceProfile = xml.TraceProfile;
		notes = xml.Notes;
		active = xml.RunOnIs;
		groupId = groupId;
		parentId = parentId;
		authorName = null; //RADIX-13035

		if (isNewObject() && groupId != null) {
		    seq = ((TestCase.Group) groupRef).getChildsCountFromDb() + 1;
		}

		helper.createOrUpdateAndReport(this);

		if (groupId != null) {
		    reread(); //perform reread() to actualize value of 
		    checkForProblemTest(helper);
		}

		importExtensionProps(xml, helper);
		CfgManagement::ImpExpUtils.importProps(xml, this, helper);

		if (importChilds && xml.isSetChilds()) {
		    for (ImpExpXsd:TestCase xChild : xml.Childs.ItemList) {
		        if (isGroup.booleanValue()) {
		            xChild.GroupGuid = extGuid;
		        } else {
		            xChild.ParentGuid = extGuid;
		        }
		        importOne(xChild, helper);
		    }
		}
	}

	/*Radix::Testing::TestCase:importExtensionProps-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:importExtensionProps")
	protected published  void importExtensionProps (org.radixware.ads.Testing.common.ImpExpXsd.TestCase xml, org.radixware.ads.CfgManagement.server.ICfgImportHelper helper) {
		//for overwrite
	}

	/*Radix::Testing::TestCase:importOne-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:importOne")
	public static published  org.radixware.ads.Testing.server.TestCase importOne (org.radixware.ads.Testing.common.ImpExpXsd.TestCase xml, org.radixware.ads.CfgManagement.server.ICfgImportHelper helper) {
		if (xml == null)
		    return null;

		Str guid = xml.ExtGuid;
		TestCase obj = TestCase.loadByGuid(guid, null);

		TestCase.Group group = null;
		Int groupId = null;
		TestCase parent = null;
		Int parentId = null;
		if (xml.isSetGroupGuid() && xml.GroupGuid != null) {
		    group = (TestCase.Group) TestCase.loadByGuid(xml.GroupGuid, idof[TestCase.Group]);
		    groupId = group != null ? group.id : null;
		} 
		if (xml.isSetParentGuid() && xml.ParentGuid != null) {
		    parent = TestCase.loadByGuid(xml.ParentGuid, null);
		    parentId = parent != null ? parent.id : null;
		}

		if (obj == null) {
		    obj = TestCase.create(xml.ClassId, guid);
		    obj.importThis(xml, groupId, parentId, helper, true);
		} else {
		    switch (helper.getActionIfObjExists(obj)) {
		        case UPDATE:
		            obj.importThis(xml, groupId, parentId, helper, true);
		            break;
		        case NEW:
		            obj = TestCase.create(xml.ClassId, Arte::Arte.generateGuid());
		            obj.importThis(xml, groupId, parentId, helper, true);
		            break;
		        case CANCELL:
		            helper.reportObjectCancelled(obj);
		            break;
		    }
		}
		return obj;
	}

	/*Radix::Testing::TestCase:onCommand_Import-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:onCommand_Import")
	public published  org.radixware.schemas.types.StrDocument onCommand_Import (org.radixware.ads.Testing.common.ImpExpXsd.TestCaseDocument input, org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValHandlersById) {
		CfgManagement::CfgImportHelper.Interactive helper = new CfgImportHelper.Interactive(false, true);
		Int groupId = groupRef != null ? groupRef.id : null;
		Int parentId = parent != null ? parent.id : null;

		importThis(input.TestCase, groupId, parentId, helper, true);
		return helper.getResultsAsHtmlStr();
	}

	/*Radix::Testing::TestCase:exportAll-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:exportAll")
	 static  void exportAll (org.radixware.ads.CfgManagement.server.CfgExportData data, Int ownerTestId, java.util.Iterator<org.radixware.ads.Testing.server.TestCase> iter) {
		ImpExpXsd:TestCaseGroupDocument groupDoc = ImpExpXsd:TestCaseGroupDocument.Factory.newInstance();
		groupDoc.addNewTestCaseGroup();

		if (ownerTestId != null) {
		    TestCase ownerTest = TestCase.loadByPK(ownerTestId, false);
		    groupDoc.TestCaseGroup.OwnerClassGuid = Types::Id.Factory.loadFrom(ownerTest.classGuid);
		    groupDoc.TestCaseGroup.OwnerExtGuid = ownerTest.extGuid;
		    groupDoc.TestCaseGroup.IsOwnerGroup = ownerTest.isGroup;
		}

		if (iter == null) {
		    ChildTestCasesByOwnerCursor c = ChildTestCasesByOwnerCursor.open(ownerTestId);
		    iter = new CfgManagement::EntityCursorIterator<TestCase>(c, idof[ChildTestCasesByOwnerCursor:testCase]);
		}

		try {
		    while (iter.hasNext()) {
		        try {
		            TestCase c = iter.next();
		            CfgManagement::CfgExportData childData = new CfgExportData();
		            c.exportThis(childData, true, true);
		            data.children.add(childData);
		            ImpExpXsd:TestCaseDocument xDoc = (ImpExpXsd:TestCaseDocument) childData.fileContent;
		            groupDoc.TestCaseGroup.addNewItem().set(xDoc.TestCase);
		        } catch (Exceptions::DefinitionNotFoundError e) {
		            Arte::Trace.error("Error on export test cases: " + Arte::Trace.exceptionStackToString(e), Arte::EventSource:AppCfgPackage);
		        }
		    }
		} finally {
		    CfgManagement::EntityCursorIterator.closeIterator(iter);
		}

		data.itemClassId = idof[CfgItem.TestCaseGroup];
		data.object = null;
		data.data = groupDoc;
		data.fileContent = data.data;
	}

	/*Radix::Testing::TestCase:importAll-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:importAll")
	public static published  void importAll (org.radixware.ads.Testing.common.ImpExpXsd.TestCaseGroup xml, org.radixware.ads.CfgManagement.server.ICfgImportHelper helper) {
		for (ImpExpXsd:TestCase x : xml.ItemList) {
		    if (xml.OwnerExtGuid != null) {
		        if (xml.IsOwnerGroup.booleanValue()) {
		            x.GroupGuid = xml.OwnerExtGuid;
		        } else {
		            x.ParentGuid = xml.OwnerExtGuid;
		        }
		    }
		    
		    importOne(x, helper);
		    if (helper.wasCancelled())
		        break;
		}
	}

	/*Radix::Testing::TestCase:exportChilds-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:exportChilds")
	private final  void exportChilds (org.radixware.ads.Testing.common.ImpExpXsd.TestCase xTest) {
		if (hasChildTests()) {
		    xTest.addNewChilds();
		    for (TestCase child : getChildTests()) {
		        xTest.Childs.addNewItem().set(child.exportToXml(true, false).TestCase);
		    }
		}
	}

	/*Radix::Testing::TestCase:exportChilds-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:exportChilds")
	private final  void exportChilds (org.radixware.ads.CfgManagement.server.CfgExportData data) {
		if (hasChildTests()) {
		    for (TestCase child : getChildTests()) {
		        CfgManagement::CfgExportData childData = new CfgExportData();
		        child.exportThis(childData, true, false);
		        data.children.add(childData);
		    }
		}
	}

	/*Radix::Testing::TestCase:checkForProblemTest-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:checkForProblemTest")
	private final  void checkForProblemTest (org.radixware.ads.CfgManagement.server.ICfgImportHelper helper) {
		if (parentIdByGroup != parentId) {
		    String ownParentGuid = parentId != null ? parent.extGuid : null;
		    String parentByGroupGuid = null;
		    if (parentIdByGroup != null) {
		        TestCase parentByGroup = TestCase.loadByPK(parentIdByGroup, true);
		        parentByGroupGuid = parentByGroup.extGuid;
		    }
		    helper.reportWarnings(this,
		            Str.format("Not correct references values: own parent test case [guid=%s] and parent test case inherited from group [guid=%s] are different.",
		                    parentByGroupGuid, ownParentGuid));
		}
	}

	/*Radix::Testing::TestCase:canHaveChilds-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:canHaveChilds")
	private final  boolean canHaveChilds () {
		return canHaveChildsByParent() || isGroup.booleanValue();
	}

	/*Radix::Testing::TestCase:canHaveChildsByParent-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:canHaveChildsByParent")
	  boolean canHaveChildsByParent () {
		final java.util.List<Types::Id> ids = getAllowedChildClassIds();
		return ids != null && !ids.isEmpty();
	}

	/*Radix::Testing::TestCase:getAllowedChildClassIds-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:getAllowedChildClassIds")
	protected published  java.util.List<org.radixware.kernel.common.types.Id> getAllowedChildClassIds () {
		return null;
	}

	/*Radix::Testing::TestCase:getChildTests-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:getChildTests")
	private final  java.util.List<org.radixware.ads.Testing.server.TestCase> getChildTests () {
		if (!hasChildTests()) {
		    return java.util.Collections.emptyList();
		}

		java.util.ArrayList<TestCase> result = new java.util.ArrayList<>();
		ChildTestCasesByOwnerCursor childsCur = ChildTestCasesByOwnerCursor.open(id);
		try {
		    while (childsCur.next()) {
		        TestCase child = childsCur.testCase;
		        if (!isProblemItem(child)) {
		            result.add(child);
		        }
		    }
		} finally {
		    childsCur.close();
		}

		return result;
	}

	/*Radix::Testing::TestCase:hasChildTests-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:hasChildTests")
	  boolean hasChildTests () {
		if (canHaveChildsByParent()) {
		    return hasChildsByParent != null ? hasChildsByParent.booleanValue() : false;
		}
		return false;
	}

	/*Radix::Testing::TestCase:isProblemItem-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:isProblemItem")
	private final  boolean isProblemItem (org.radixware.ads.Testing.server.TestCase child) {
		return child.groupId != null && child.parentId != child.parentIdByGroup;
	}

	/*Radix::Testing::TestCase:getIgnoredPropsList-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:getIgnoredPropsList")
	protected published  java.util.List<org.radixware.kernel.common.types.Id> getIgnoredPropsList () {
		return null;
	}

	/*Radix::Testing::TestCase:beforeInit-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:beforeInit")
	protected published  boolean beforeInit (org.radixware.kernel.server.types.PropValHandlersByIdMap initPropValsById, org.radixware.kernel.server.types.Entity src, org.radixware.kernel.common.enums.EEntityInitializationPhase phase) {
		authorName = Arte::Arte.getUserName();
		return super.beforeInit(initPropValsById, src, phase);
	}

	/*Radix::Testing::TestCase:reportOwnerChanged-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:reportOwnerChanged")
	private final  void reportOwnerChanged (org.radixware.ads.CfgManagement.server.ICfgImportHelper helper, Int prevParentId, Int newParentId, Int prevGroupId, Int newGroupId) {
		if (isNewObject()) {
		    return;
		}
		if (!java.util.Objects.equals(prevParentId, newParentId)) {
		    helper.reportWarnings(this, Str.format("Parent test case was changed from '%s' to '%s'", prevParentId, newParentId));
		} else if (!java.util.Objects.equals(prevGroupId, newGroupId)) {
		    helper.reportWarnings(this, Str.format("Test case group was changed from '%s' to '%s'", prevGroupId, newGroupId));
		}
	}

	/*Radix::Testing::TestCase:getCfgReferenceExtGuid-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:getCfgReferenceExtGuid")
	public published  Str getCfgReferenceExtGuid () {
		return extGuid;
	}

	/*Radix::Testing::TestCase:getCfgReferencePropId-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:getCfgReferencePropId")
	public published  org.radixware.kernel.common.types.Id getCfgReferencePropId () {
		return idof[TestCase:extGuid];
	}

	/*Radix::Testing::TestCase:getDurationStr-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:getDurationStr")
	public published  Str getDurationStr () {
		//Example: CPU = 2ms, DB = 17 ms, EXT = 0 ms, QUEUE = 0 ms (Total 19 ms)
		if (cpuNanos == null || dbNanos == null || extNanos == null || queueNanos == null) {
		    return "";
		}
		final long qNanos = queueNanos.longValue() == -1 ? 0 : queueNanos.longValue();
		final long total = cpuNanos.longValue() + dbNanos.longValue()
		        + extNanos.longValue() + qNanos;     
		return Str.format("CPU = %d ns, DB = %d ns, EXT = %d ns, QUEUE = %d ns (Total %d ns)", cpuNanos, dbNanos, extNanos, qNanos, total);
	}

	/*Radix::Testing::TestCase:afterExecute-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:afterExecute")
	protected published  void afterExecute () {

	}




	@Override
	public org.radixware.kernel.server.types.FormHandler.NextDialogsRequest execCommand(org.radixware.kernel.common.types.Id cmdId, org.radixware.kernel.common.types.Id propId, org.apache.xmlbeans.XmlObject input, org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValsById, org.apache.xmlbeans.XmlObject output) throws org.radixware.kernel.common.exceptions.AppException,java.lang.InterruptedException{
		if(cmdId == cmdHGOOKC3J3DNRDISQAAAAAAAAAA){
			onCommand_Rerun(newPropValsById);
			return null;
		} else if(cmdId == cmdM5Y74G3BRFHTRGIHVZN5WY5IXA){
			org.radixware.schemas.types.StrDocument result = onCommand_Import((org.radixware.ads.Testing.common.ImpExpXsd.TestCaseDocument)org.radixware.kernel.common.utils.XmlObjectProcessor.getAppCommandInputData(getClass().getClassLoader(),input,org.radixware.ads.Testing.common.ImpExpXsd.TestCaseDocument.class),newPropValsById);
			if(result != null)
				output.set(result);
			return null;
		} else 
			return super.execCommand(cmdId,propId,input,newPropValsById,output);
	}


}

/* Radix::Testing::TestCase - Server Meta*/

/*Radix::Testing::TestCase-Entity Class*/

package org.radixware.ads.Testing.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class TestCase_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),"TestCase",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBJFDUM24OXOBDFKUAAMPGXUWTQ"),

						org.radixware.kernel.common.enums.EClassType.ENTITY,

						/*Radix::Testing::TestCase:Presentations-Entity Presentations*/
						/*Class presentation attributes*/
						 new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef(/*Owner Class Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),
							/*Owner Class Name*/
							"TestCase",
							/*Title Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBJFDUM24OXOBDFKUAAMPGXUWTQ"),
							/*Property presentations*/

							/*Radix::Testing::TestCase:Properties-Properties*/
							new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef[]{

									/*Radix::Testing::TestCase:resultComment:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colC46NAASN3PNRDISQAAAAAAAAAA"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Testing::TestCase:parentId:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEZ4SH34S27NRDISQAAAAAAAAAA"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Testing::TestCase:startTime:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGBJZDIUS27NRDISQAAAAAAAAAA"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Testing::TestCase:notes:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHWP3QCET27NRDISQAAAAAAAAAA"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Testing::TestCase:parent:PropertyPresentation-Parent Ref Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadParentTitlePropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colM7PLLGVJ27NRDISQAAAAAAAAAA"),org.radixware.kernel.common.enums.EEditPossibility.PROGRAMMATICALLY,false,false,null,null,new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),org.radixware.kernel.server.types.Restrictions.Factory.newInstance(133693439,null,null,null),null,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_SELECTOR)),

									/*Radix::Testing::TestCase:actor:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMWOGO4MS27NRDISQAAAAAAAAAA"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Testing::TestCase:id:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colN3UZ2Z4S27NRDISQAAAAAAAAAA"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Testing::TestCase:traceProfile:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPMEM3RSM3PNRDISQAAAAAAAAAA"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_SELECT_CONDITION,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_SELECTOR)),

									/*Radix::Testing::TestCase:finishTime:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQKCERAMS27NRDISQAAAAAAAAAA"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Testing::TestCase:result:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colYWL4NXUS27NRDISQAAAAAAAAAA"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Testing::TestCase:active:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQP4LKSN3IFHITPAQXU7J72H6FA"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Testing::TestCase:groupId:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3AN3AUHYHNHNFBQ3J4PRDXRRXE"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Testing::TestCase:groupRef:PropertyPresentation-Parent Ref Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadParentTitlePropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3T77RRLFUBHDLA7X4GCO27SCOU"),org.radixware.kernel.common.enums.EEditPossibility.PROGRAMMATICALLY,false,false,null,null,new org.radixware.kernel.server.meta.presentations.RadConditionDef("<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:PropSqlName TableId=\"aecHW4OSVMS27NRDISQAAAAAAAAAA\" PropId=\"colQQJY664T27NRDISQAAAAAAAAAA\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>=</xsc:Sql></xsc:Item><xsc:Item><xsc:Id Path=\"aclRFAF35NQEZHWPKJAMOFJZA4RRQ\"/></xsc:Item></xsc:Sqml>","<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),org.radixware.kernel.server.types.Restrictions.Factory.newInstance(133693439,null,null,null),null,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_SELECTOR)),

									/*Radix::Testing::TestCase:extGuid:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJGTICYXJ35G5TJ26LFHQAX2QRY"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Testing::TestCase:seq:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3ZQVM54K5FEL7D7RH4HIYI62Y4"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Testing::TestCase:seqTitle:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdYRLSFFVV2ZFIJFFAT34VLBDB4Y"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Testing::TestCase:classTitle:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdZSHM4TXE3XNRDISQAAAAAAAAAA"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.noneOf(org.radixware.kernel.common.enums.EPropAttrInheritance.class)),

									/*Radix::Testing::TestCase:failEventSeverity:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdV7KTVRF6PJBPLHTS5N72A7W7PU"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Testing::TestCase:isGroup:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdR7AAOIBTTNDT5JUMOATGCT22JQ"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Testing::TestCase:thereIsChilds:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdQO2OHKBQTNB2LK2NR3TUUWDQWQ"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Testing::TestCase:canHaveChilds:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd4CDENELMOJFFVP5WDQGC72CQRA"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Testing::TestCase:isParent:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdGDMZ3TJQRFHTZLUGK5MYZ5LUOA"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Testing::TestCase:pathInTree:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd27YHYAOF2RAKXOKNUKIM53FSMU"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Testing::TestCase:authorName:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colCFZB5S5MFFAPJM2ENTFD66CEZI"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Testing::TestCase:author:PropertyPresentation-Parent Ref Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadParentTitlePropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colL4QQIGK7MFGF5FCW7HHEA4YIZI"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,null,null,new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),org.radixware.kernel.server.types.Restrictions.Factory.newInstance(133693439,null,null,null),null,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_SELECTOR)),

									/*Radix::Testing::TestCase:lastIsExecDate:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colI5ELDBK7XVDQDA626NCPOMNAXM"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Testing::TestCase:lastIsSuccessDate:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPI3FLY6XA5DV3LZKE3RO4PQFFQ"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Testing::TestCase:notificationEmail:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colUSPDDDJLZ5AINICL3FIA4KD7GU"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Testing::TestCase:seqIsFailCount:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKTWEBHYK2JGENOQY3SHZ27BQ6I"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Testing::TestCase:cpuNanos:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colLLFER2QMUFH5BME327Q4IPNJNM"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Testing::TestCase:dbNanos:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colYH7NIOG57JEHPD7QO4GMHI7K54"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Testing::TestCase:extNanos:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colG4EPHGTTE5BM3DLJN55VNIVOBE"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Testing::TestCase:queueNanos:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMDKXQBA6KZFHZH6FTKW7KHF4Z4"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Testing::TestCase:execDurationStr:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdROKBL5544NGPXNKFHAAELEX3SM"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT))
							},
							/*Commands*/
							new org.radixware.kernel.server.meta.presentations.RadCommandDef[]{

									new org.radixware.kernel.server.meta.presentations.RadCommandDef(
									/*Radix::Testing::TestCase:Rerun-Object Command*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdHGOOKC3J3DNRDISQAAAAAAAAAA"),"Rerun",org.radixware.kernel.common.enums.ECommandScope.OBJECT,null,org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_FIXED,org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),false,true),

									new org.radixware.kernel.server.meta.presentations.RadCommandDef(
									/*Radix::Testing::TestCase:changeGroupSettings-Object Command*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdTMPJ4Q6TXJAWJAN2YGZRKV2XSI"),"changeGroupSettings",org.radixware.kernel.common.enums.ECommandScope.OBJECT,null,org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_EXISTENT,org.radixware.kernel.common.enums.ECommandNature.FORM_IN_OUT,org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),false,true),

									new org.radixware.kernel.server.meta.presentations.RadCommandDef(
									/*Radix::Testing::TestCase:Export-Object Command*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdOLNLKH7WMFGEPK4G5C4SIA2HQA"),"Export",org.radixware.kernel.common.enums.ECommandScope.OBJECT,null,org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_FIXED,org.radixware.kernel.common.enums.ECommandNature.FORM_IN_OUT,org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),false,true),

									new org.radixware.kernel.server.meta.presentations.RadCommandDef(
									/*Radix::Testing::TestCase:Import-Object Command*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdM5Y74G3BRFHTRGIHVZN5WY5IXA"),"Import",org.radixware.kernel.common.enums.ECommandScope.OBJECT,null,org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_FIXED,org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),false,true)
							},
							/*Sortings*/
							new org.radixware.kernel.server.meta.presentations.RadSortingDef[]{

									new org.radixware.kernel.server.meta.presentations.RadSortingDef(
									/*Radix::Testing::TestCase:By Start Time-Sorting*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("srt34WFCNN2RREXPDEKUMG5SZUB6A"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),"By Start Time",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGYMZYCC66FAHHMX22GMCEPFNR4"),new org.radixware.kernel.server.meta.presentations.RadSortingDef.Item[]{

											new org.radixware.kernel.server.meta.presentations.RadSortingDef.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3ZQVM54K5FEL7D7RH4HIYI62Y4"),org.radixware.kernel.common.enums.EOrder.ASC),

											new org.radixware.kernel.server.meta.presentations.RadSortingDef.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGBJZDIUS27NRDISQAAAAAAAAAA"),org.radixware.kernel.common.enums.EOrder.DESC)
									},"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),

									new org.radixware.kernel.server.meta.presentations.RadSortingDef(
									/*Radix::Testing::TestCase:By ID desc-Sorting*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("srtA7QU7WNG27NRDISQAAAAAAAAAA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),"By ID desc",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQVFDUM24OXOBDFKUAAMPGXUWTQ"),new org.radixware.kernel.server.meta.presentations.RadSortingDef.Item[]{

											new org.radixware.kernel.server.meta.presentations.RadSortingDef.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("colN3UZ2Z4S27NRDISQAAAAAAAAAA"),org.radixware.kernel.common.enums.EOrder.DESC)
									},"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:Sql>index_desc(</xsc:Sql></xsc:Item><xsc:Item><xsc:ThisTableSqlName/></xsc:Item><xsc:Item><xsc:Sql> </xsc:Sql></xsc:Item><xsc:Item><xsc:IndexDbName TableId=\"tblHW4OSVMS27NRDISQAAAAAAAAAA\"/></xsc:Item><xsc:Item><xsc:Sql>) </xsc:Sql></xsc:Item></xsc:Sqml>","org.radixware"),

									new org.radixware.kernel.server.meta.presentations.RadSortingDef(
									/*Radix::Testing::TestCase:By Actor-Sorting*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("srtSMTOVB726JGMJBEJ666HYZSW6I"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),"By Actor",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAUV36YGJI5DTFG2E5WYN54UINQ"),new org.radixware.kernel.server.meta.presentations.RadSortingDef.Item[]{

											new org.radixware.kernel.server.meta.presentations.RadSortingDef.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMWOGO4MS27NRDISQAAAAAAAAAA"),org.radixware.kernel.common.enums.EOrder.ASC)
									},"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware")
							},
							/*Filters*/
							new org.radixware.kernel.server.meta.presentations.RadFilterDef[]{

									new org.radixware.kernel.server.meta.presentations.RadFilterDef(
									/*Radix::Testing::TestCase:By Class-Filter*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("fltBQPIEXVBZRES3ICWCEACXNIKKI"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),"By Class",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEWLIOPBOFJG2DFUJXWYDMP343Q"),"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:DbFuncCall FuncId=\"dfn3OXAHT4FYLORDBBJABIFNQAABA\"/></xsc:Item><xsc:Item><xsc:Sql>(</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblHW4OSVMS27NRDISQAAAAAAAAAA\" PropId=\"colQQJY664T27NRDISQAAAAAAAAAA\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>, </xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmA3FXJ53QVZDVTLHFCPTSUYRD2I\"/></xsc:Item><xsc:Item><xsc:Sql>)=1 or exists(\n    select </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblHW4OSVMS27NRDISQAAAAAAAAAA\" PropId=\"colN3UZ2Z4S27NRDISQAAAAAAAAAA\" Owner=\"TABLE\"/></xsc:Item><xsc:Item><xsc:Sql> \n    from </xsc:Sql></xsc:Item><xsc:Item><xsc:TableSqlName TableId=\"tblHW4OSVMS27NRDISQAAAAAAAAAA\"/></xsc:Item><xsc:Item><xsc:Sql>\n    where </xsc:Sql></xsc:Item><xsc:Item><xsc:DbFuncCall FuncId=\"dfn3OXAHT4FYLORDBBJABIFNQAABA\"/></xsc:Item><xsc:Item><xsc:Sql>(</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblHW4OSVMS27NRDISQAAAAAAAAAA\" PropId=\"colQQJY664T27NRDISQAAAAAAAAAA\" Owner=\"TABLE\"/></xsc:Item><xsc:Item><xsc:Sql>, </xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmA3FXJ53QVZDVTLHFCPTSUYRD2I\"/></xsc:Item><xsc:Item><xsc:Sql>)=1 \n    start with </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblHW4OSVMS27NRDISQAAAAAAAAAA\" PropId=\"colEZ4SH34S27NRDISQAAAAAAAAAA\" Owner=\"TABLE\"/></xsc:Item><xsc:Item><xsc:Sql>=</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecHW4OSVMS27NRDISQAAAAAAAAAA\" PropId=\"colN3UZ2Z4S27NRDISQAAAAAAAAAA\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> or </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblHW4OSVMS27NRDISQAAAAAAAAAA\" PropId=\"col3AN3AUHYHNHNFBQ3J4PRDXRRXE\" Owner=\"TABLE\"/></xsc:Item><xsc:Item><xsc:Sql>=</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecHW4OSVMS27NRDISQAAAAAAAAAA\" PropId=\"colN3UZ2Z4S27NRDISQAAAAAAAAAA\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>\n    connect by prior </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblHW4OSVMS27NRDISQAAAAAAAAAA\" PropId=\"colN3UZ2Z4S27NRDISQAAAAAAAAAA\" Owner=\"TABLE\"/></xsc:Item><xsc:Item><xsc:Sql>=</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblHW4OSVMS27NRDISQAAAAAAAAAA\" PropId=\"colEZ4SH34S27NRDISQAAAAAAAAAA\" Owner=\"TABLE\"/></xsc:Item><xsc:Item><xsc:Sql> or prior </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblHW4OSVMS27NRDISQAAAAAAAAAA\" PropId=\"colN3UZ2Z4S27NRDISQAAAAAAAAAA\" Owner=\"TABLE\"/></xsc:Item><xsc:Item><xsc:Sql>=</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblHW4OSVMS27NRDISQAAAAAAAAAA\" PropId=\"col3AN3AUHYHNHNFBQ3J4PRDXRRXE\" Owner=\"TABLE\"/></xsc:Item><xsc:Item><xsc:Sql>\n)</xsc:Sql></xsc:Item></xsc:Sqml>",null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>",org.radixware.kernel.common.types.Id.Factory.loadFrom("srt34WFCNN2RREXPDEKUMG5SZUB6A"),true,null,false,"org.radixware")
							},
							/*Editor presentations*/
							new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef[]{

									new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef(
									/*Radix::Testing::TestCase:General-Editor Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprBMFQWKU227NRDISQAAAAAAAAAA"),
									"General",
									org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
									null,
									2192,
									null,

									/*Radix::Testing::TestCase:General:Children-Explorer Items*/
										new org.radixware.kernel.server.meta.presentations.RadExplorerItemDef[]{

												/*Radix::Testing::TestCase:General:EventLog-Entity Explorer Item*/

												new org.radixware.kernel.server.meta.presentations.RadTableExplorerItemDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("xpi3VAI7YDSGZAL3NQC42JXD34APY"),
													org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
													org.radixware.kernel.common.types.Id.Factory.loadFrom("sprKQ5WOSDJZ5AQFOGPY4PK3O2XTA"),
													new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),
													null,
													null),

												/*Radix::Testing::TestCase:General:Children-Entity Explorer Item*/

												new org.radixware.kernel.server.meta.presentations.RadTableExplorerItemDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiIHA74FUWNZB4JHZPOT62ZLCW54"),
													org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),
													org.radixware.kernel.common.types.Id.Factory.loadFrom("sprNGT5DE5G27NRDISQAAAAAAAAAA"),
													new org.radixware.kernel.server.meta.presentations.RadConditionDef("<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:PropSqlName TableId=\"aecHW4OSVMS27NRDISQAAAAAAAAAA\" PropId=\"col3AN3AUHYHNHNFBQ3J4PRDXRRXE\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>=</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecHW4OSVMS27NRDISQAAAAAAAAAA\" PropId=\"colN3UZ2Z4S27NRDISQAAAAAAAAAA\" Owner=\"PARENT\"/></xsc:Item><xsc:Item><xsc:Sql> or (\n    (</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecHW4OSVMS27NRDISQAAAAAAAAAA\" PropId=\"colNKRUTYDCBVG5DB7QCUUVBW7U5I\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> is null or </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecHW4OSVMS27NRDISQAAAAAAAAAA\" PropId=\"colNKRUTYDCBVG5DB7QCUUVBW7U5I\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>!=</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecHW4OSVMS27NRDISQAAAAAAAAAA\" PropId=\"colN3UZ2Z4S27NRDISQAAAAAAAAAA\" Owner=\"PARENT\"/></xsc:Item><xsc:Item><xsc:Sql>) and </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecHW4OSVMS27NRDISQAAAAAAAAAA\" PropId=\"colEZ4SH34S27NRDISQAAAAAAAAAA\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>=</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecHW4OSVMS27NRDISQAAAAAAAAAA\" PropId=\"colN3UZ2Z4S27NRDISQAAAAAAAAAA\" Owner=\"PARENT\"/></xsc:Item><xsc:Item><xsc:Sql>\n)</xsc:Sql></xsc:Item></xsc:Sqml>","<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),
													null,
													org.radixware.kernel.common.types.Id.Factory.loadFrom("eccKGGFFVPJAHOBDA26AAMPGXSZKU"))
										}
									,
									org.radixware.kernel.server.types.Restrictions.Factory.newInstance(0,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdHGOOKC3J3DNRDISQAAAAAAAAAA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdTMPJ4Q6TXJAWJAN2YGZRKV2XSI")},null,null),
									(org.radixware.kernel.server.meta.presentations.RadPropertyPresentationAttributes[]) null,
									org.radixware.kernel.common.enums.EEditorPresentationRightsInheritanceMode.NONE,
									null,null),

									new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef(
									/*Radix::Testing::TestCase:Web-Editor Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("epr4WBLCNMXSFEI3M2LSYDLSIK6Q4"),
									"Web",
									org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
									null,
									2192,
									null,

									/*Radix::Testing::TestCase:Web:Children-Explorer Items*/
										null
									,
									org.radixware.kernel.server.types.Restrictions.Factory.newInstance(0,null,null,null),
									(org.radixware.kernel.server.meta.presentations.RadPropertyPresentationAttributes[]) null,
									org.radixware.kernel.common.enums.EEditorPresentationRightsInheritanceMode.NONE,
									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprBMFQWKU227NRDISQAAAAAAAAAA"),null)
							},
							/*Selector presentations*/
							new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef[]{

									new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef(
									/*Radix::Testing::TestCase:General-Selector Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("sprNGT5DE5G27NRDISQAAAAAAAAAA"),"General",null,2192,new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn[]{

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3AN3AUHYHNHNFBQ3J4PRDXRRXE"),false),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3ZQVM54K5FEL7D7RH4HIYI62Y4"),false),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd4CDENELMOJFFVP5WDQGC72CQRA"),false),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdQO2OHKBQTNB2LK2NR3TUUWDQWQ"),false),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdR7AAOIBTTNDT5JUMOATGCT22JQ"),false),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colN3UZ2Z4S27NRDISQAAAAAAAAAA"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdYRLSFFVV2ZFIJFFAT34VLBDB4Y"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdZSHM4TXE3XNRDISQAAAAAAAAAA"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHWP3QCET27NRDISQAAAAAAAAAA"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMWOGO4MS27NRDISQAAAAAAAAAA"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGBJZDIUS27NRDISQAAAAAAAAAA"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQKCERAMS27NRDISQAAAAAAAAAA"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colYWL4NXUS27NRDISQAAAAAAAAAA"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colC46NAASN3PNRDISQAAAAAAAAAA"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEZ4SH34S27NRDISQAAAAAAAAAA"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQP4LKSN3IFHITPAQXU7J72H6FA"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colL4QQIGK7MFGF5FCW7HHEA4YIZI"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colUSPDDDJLZ5AINICL3FIA4KD7GU"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colI5ELDBK7XVDQDA626NCPOMNAXM"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPI3FLY6XA5DV3LZKE3RO4PQFFQ"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKTWEBHYK2JGENOQY3SHZ27BQ6I"),true)
									},new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.Addons(org.radixware.kernel.common.types.Id.Factory.loadFrom("srt34WFCNN2RREXPDEKUMG5SZUB6A"),true,null,true,null,true,null,false,true,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprBMFQWKU227NRDISQAAAAAAAAAA")},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprBMFQWKU227NRDISQAAAAAAAAAA")},org.radixware.kernel.server.types.Restrictions.Factory.newInstance(271272,null,null,null),org.radixware.kernel.common.types.Id.Factory.loadFrom("eccKGGFFVPJAHOBDA26AAMPGXSZKU")),

									new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef(
									/*Radix::Testing::TestCase:FlatList-Selector Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("sprLEXXOTV4O5AEBO3VECZLSIGRCY"),"FlatList",null,0,new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn[]{

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdQO2OHKBQTNB2LK2NR3TUUWDQWQ"),false),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdR7AAOIBTTNDT5JUMOATGCT22JQ"),false),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colN3UZ2Z4S27NRDISQAAAAAAAAAA"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdYRLSFFVV2ZFIJFFAT34VLBDB4Y"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdZSHM4TXE3XNRDISQAAAAAAAAAA"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHWP3QCET27NRDISQAAAAAAAAAA"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMWOGO4MS27NRDISQAAAAAAAAAA"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGBJZDIUS27NRDISQAAAAAAAAAA"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQKCERAMS27NRDISQAAAAAAAAAA"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colYWL4NXUS27NRDISQAAAAAAAAAA"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colC46NAASN3PNRDISQAAAAAAAAAA"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEZ4SH34S27NRDISQAAAAAAAAAA"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQP4LKSN3IFHITPAQXU7J72H6FA"),true)
									},new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.Addons(org.radixware.kernel.common.types.Id.Factory.loadFrom("srtA7QU7WNG27NRDISQAAAAAAAAAA"),false,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("srtA7QU7WNG27NRDISQAAAAAAAAAA")},false,null,false,new org.radixware.kernel.common.types.Id[]{},false,false,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprBMFQWKU227NRDISQAAAAAAAAAA")},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprBMFQWKU227NRDISQAAAAAAAAAA")},org.radixware.kernel.server.types.Restrictions.Factory.newInstance(32,null,null,null),null)
							},
							/*Default selector presentation Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("sprNGT5DE5G27NRDISQAAAAAAAAAA"),
							/*Presentation Map*/
							null,
							/*Object title format*/

							/*Radix::Testing::TestCase:TitleFormat-Object Title Format*/

							new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.TitleItem[]{

									new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.TitleItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colN3UZ2Z4S27NRDISQAAAAAAAAAA"),"{0}) ",org.radixware.kernel.common.enums.ETitleNullFormat.SAME_AS_NOT_NULL,null),

									new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.TitleItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("prdZSHM4TXE3XNRDISQAAAAAAAAAA"),"{0} - ",org.radixware.kernel.common.enums.ETitleNullFormat.SAME_AS_NOT_NULL,null),

									new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.TitleItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colHWP3QCET27NRDISQAAAAAAAAAA"),"{0}",org.radixware.kernel.common.enums.ETitleNullFormat.SAME_AS_NOT_NULL,null)
							},null,org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.DefinitionContextType.ENTITY),
							/*Class catalogs*/
							new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog[]{

									new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog(
									/*Radix::Testing::TestCase:All-Dynamic Class Catalog*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eccKGGFFVPJAHOBDA26AAMPGXSZKU"),org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.ELinkMode.VIRTUAL,new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.Item[]{
										new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.Topic(org.radixware.kernel.common.types.Id.Factory.loadFrom("cctCE37MB2JGQAESF2YAAIFIUYFRE"),null,1461.5625,org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsIVTUVFR755A7VFN4X2RN3HKEXM")),
										new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.Topic(org.radixware.kernel.common.types.Id.Factory.loadFrom("cctWTU3YLBP7NBSLHVPVJNJ2WVHRU"),null,1510.0,org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsH3BATTPRYFEHTNA527MATBRRZI")),
										new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.Topic(org.radixware.kernel.common.types.Id.Factory.loadFrom("cctQZQQ5AKZLBAJ7GBZ57NY5WB22M"),null,1550.0,org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYODR5A2C55CLRJUJPPVR6L4EOI")),
										new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.Topic(org.radixware.kernel.common.types.Id.Factory.loadFrom("cctXACECBY2KJE47HZWCZEMUDENLM"),null,1555.0,org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMGAL6SE6TFBZVCQRNTLVJOYN4E")),
										new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.Topic(org.radixware.kernel.common.types.Id.Factory.loadFrom("cctIZUU23K5JJB5JCECVMJAKYK7RU"),null,1471.25,org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUU7MCHSFMBDPFC7Y6HLT3KGY3M")),
										new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.Topic(org.radixware.kernel.common.types.Id.Factory.loadFrom("cctV4XCRINWJBFLXDBYWMIXPCKKPM"),null,1540.0,org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJF3XOOHOCZDH7HQREVBFFJY7M4"))}
									)
							},
							/*Restrictions*/
							org.radixware.kernel.server.types.Restrictions.Factory.newInstance(8192,null,null,null),null,0),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblHW4OSVMS27NRDISQAAAAAAAAAA"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("pdcEntity____________________"),

						/*Radix::Testing::TestCase:Properties-Properties*/
						new org.radixware.kernel.server.meta.clazzes.RadPropDef[]{

								/*Radix::Testing::TestCase:resultComment-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colC46NAASN3PNRDISQAAAAAAAAAA"),"resultComment",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOBFDUM24OXOBDFKUAAMPGXUWTQ"),org.radixware.kernel.common.enums.EValType.CLOB,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Testing::TestCase:parentId-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEZ4SH34S27NRDISQAAAAAAAAAA"),"parentId",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsN5FDUM24OXOBDFKUAAMPGXUWTQ"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Testing::TestCase:startTime-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGBJZDIUS27NRDISQAAAAAAAAAA"),"startTime",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNZFDUM24OXOBDFKUAAMPGXUWTQ"),org.radixware.kernel.common.enums.EValType.DATE_TIME,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Testing::TestCase:notes-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHWP3QCET27NRDISQAAAAAAAAAA"),"notes",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNVFDUM24OXOBDFKUAAMPGXUWTQ"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Testing::TestCase:parent-Parent Reference*/

								new org.radixware.kernel.server.meta.clazzes.RadInnateRefPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colM7PLLGVJ27NRDISQAAAAAAAAAA"),"parent",null,org.radixware.kernel.common.types.Id.Factory.loadFrom("refZ3L7OMMT27NRDISQAAAAAAAAAA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Testing::TestCase:actor-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMWOGO4MS27NRDISQAAAAAAAAAA"),"actor",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNRFDUM24OXOBDFKUAAMPGXUWTQ"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Testing::TestCase:id-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colN3UZ2Z4S27NRDISQAAAAAAAAAA"),"id",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNNFDUM24OXOBDFKUAAMPGXUWTQ"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Testing::TestCase:traceProfile-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPMEM3RSM3PNRDISQAAAAAAAAAA"),"traceProfile",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNJFDUM24OXOBDFKUAAMPGXUWTQ"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("Debug")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Testing::TestCase:finishTime-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQKCERAMS27NRDISQAAAAAAAAAA"),"finishTime",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNFFDUM24OXOBDFKUAAMPGXUWTQ"),org.radixware.kernel.common.enums.EValType.DATE_TIME,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Testing::TestCase:classGuid-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQQJY664T27NRDISQAAAAAAAAAA"),"classGuid",null,org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Testing::TestCase:result-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colYWL4NXUS27NRDISQAAAAAAAAAA"),"result",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNBFDUM24OXOBDFKUAAMPGXUWTQ"),org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("acsFRO5RTQOG7NRDJH2ACQMTAIZT4"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Testing::TestCase:active-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQP4LKSN3IFHITPAQXU7J72H6FA"),"active",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsS6CNAK6UGVHUZOK2UKXFMVTW6Q"),org.radixware.kernel.common.enums.EValType.BOOL,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("1")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Testing::TestCase:groupId-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3AN3AUHYHNHNFBQ3J4PRDXRRXE"),"groupId",null,org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Testing::TestCase:groupRef-Parent Reference*/

								new org.radixware.kernel.server.meta.clazzes.RadInnateRefPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3T77RRLFUBHDLA7X4GCO27SCOU"),"groupRef",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXUGTSHLNLZAZ5BUI7QF47DGFXE"),org.radixware.kernel.common.types.Id.Factory.loadFrom("ref2VO3FCZ4RRBTZO2UKNRBRWALQY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Testing::TestCase:extGuid-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJGTICYXJ35G5TJ26LFHQAX2QRY"),"extGuid",null,org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Testing::TestCase:seq-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3ZQVM54K5FEL7D7RH4HIYI62Y4"),"seq",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsC6YFHCMLMRGT3II3DDLRF6YCI4"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("0")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Testing::TestCase:seqTitle-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdYRLSFFVV2ZFIJFFAT34VLBDB4Y"),"seqTitle",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMNQIG6HQIRARFD224IAWTHH6BQ"),org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Testing::TestCase:classTitle-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdZSHM4TXE3XNRDISQAAAAAAAAAA"),"classTitle",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEMSKREJXYRBARPN3PY5NLBLPC4"),org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadAccessorProxy.R_ACCESSOR_INSTANCE),

								/*Radix::Testing::TestCase:failEventSeverity-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdV7KTVRF6PJBPLHTS5N72A7W7PU"),"failEventSeverity",null,org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("acsFRO5RTQOG7NRDJH2ACQMTAIZT4"),org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("2")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Testing::TestCase:isGroup-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdR7AAOIBTTNDT5JUMOATGCT22JQ"),"isGroup",null,org.radixware.kernel.common.enums.EValType.BOOL,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Testing::TestCase:thereIsChilds-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdQO2OHKBQTNB2LK2NR3TUUWDQWQ"),"thereIsChilds",null,org.radixware.kernel.common.enums.EValType.BOOL,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Testing::TestCase:resultCommentAsStr-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd5AM5QSYIGZBWFFRWMT47NIP5AQ"),"resultCommentAsStr",null,org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Testing::TestCase:parentIdByGroup-Expression Property*/

								new org.radixware.kernel.server.meta.clazzes.RadSqmlPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNKRUTYDCBVG5DB7QCUUVBW7U5I"),"parentIdByGroup",null,org.radixware.kernel.common.enums.EValType.INT,"NUMBER(9,0)",null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:Sql>select </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblHW4OSVMS27NRDISQAAAAAAAAAA\" PropId=\"colEZ4SH34S27NRDISQAAAAAAAAAA\" Owner=\"TABLE\"/></xsc:Item><xsc:Item><xsc:Sql> from </xsc:Sql></xsc:Item><xsc:Item><xsc:TableSqlName TableId=\"tblHW4OSVMS27NRDISQAAAAAAAAAA\"/></xsc:Item><xsc:Item><xsc:Sql>\nwhere </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblHW4OSVMS27NRDISQAAAAAAAAAA\" PropId=\"colEZ4SH34S27NRDISQAAAAAAAAAA\" Owner=\"TABLE\"/></xsc:Item><xsc:Item><xsc:Sql> is not null and rownum &lt; 2\nstart with </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblHW4OSVMS27NRDISQAAAAAAAAAA\" PropId=\"colN3UZ2Z4S27NRDISQAAAAAAAAAA\" Owner=\"TABLE\"/></xsc:Item><xsc:Item><xsc:Sql>=</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecHW4OSVMS27NRDISQAAAAAAAAAA\" PropId=\"col3AN3AUHYHNHNFBQ3J4PRDXRRXE\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>\nCONNECT BY PRIOR </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblHW4OSVMS27NRDISQAAAAAAAAAA\" PropId=\"col3AN3AUHYHNHNFBQ3J4PRDXRRXE\" Owner=\"TABLE\"/></xsc:Item><xsc:Item><xsc:Sql>=</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblHW4OSVMS27NRDISQAAAAAAAAAA\" PropId=\"colN3UZ2Z4S27NRDISQAAAAAAAAAA\" Owner=\"TABLE\"/></xsc:Item></xsc:Sqml>",true,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Testing::TestCase:hasChildsByParent-Expression Property*/

								new org.radixware.kernel.server.meta.clazzes.RadSqmlPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colS6SWOPYLLZCG7GLRQFMDR247JU"),"hasChildsByParent",null,org.radixware.kernel.common.enums.EValType.BOOL,"NUMBER(1,0)",null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:Sql>select 1 from </xsc:Sql></xsc:Item><xsc:Item><xsc:TableSqlName TableId=\"tblHW4OSVMS27NRDISQAAAAAAAAAA\"/></xsc:Item><xsc:Item><xsc:Sql> where </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblHW4OSVMS27NRDISQAAAAAAAAAA\" PropId=\"colN3UZ2Z4S27NRDISQAAAAAAAAAA\" Owner=\"TABLE\"/></xsc:Item><xsc:Item><xsc:Sql>=</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblHW4OSVMS27NRDISQAAAAAAAAAA\" PropId=\"colN3UZ2Z4S27NRDISQAAAAAAAAAA\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> and exists(select id from </xsc:Sql></xsc:Item><xsc:Item><xsc:TableSqlName TableId=\"tblHW4OSVMS27NRDISQAAAAAAAAAA\"/></xsc:Item><xsc:Item><xsc:Sql> where </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblHW4OSVMS27NRDISQAAAAAAAAAA\" PropId=\"colEZ4SH34S27NRDISQAAAAAAAAAA\" Owner=\"TABLE\"/></xsc:Item><xsc:Item><xsc:Sql> = </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblHW4OSVMS27NRDISQAAAAAAAAAA\" PropId=\"colN3UZ2Z4S27NRDISQAAAAAAAAAA\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>)</xsc:Sql></xsc:Item></xsc:Sqml>",true,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Testing::TestCase:canHaveChilds-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd4CDENELMOJFFVP5WDQGC72CQRA"),"canHaveChilds",null,org.radixware.kernel.common.enums.EValType.BOOL,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadAccessorProxy.R_ACCESSOR_INSTANCE),

								/*Radix::Testing::TestCase:isParent-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdGDMZ3TJQRFHTZLUGK5MYZ5LUOA"),"isParent",null,org.radixware.kernel.common.enums.EValType.BOOL,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadAccessorProxy.R_ACCESSOR_INSTANCE),

								/*Radix::Testing::TestCase:pathInTree-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd27YHYAOF2RAKXOKNUKIM53FSMU"),"pathInTree",null,org.radixware.kernel.common.enums.EValType.ARR_STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Testing::TestCase:authorName-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colCFZB5S5MFFAPJM2ENTFD66CEZI"),"authorName",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsESLW3G2YARA4HJQG6XW5E54LQE"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Testing::TestCase:author-Parent Reference*/

								new org.radixware.kernel.server.meta.clazzes.RadInnateRefPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colL4QQIGK7MFGF5FCW7HHEA4YIZI"),"author",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBDSC6QRKOZF33C2U7PFDJLEPHM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("ref4INHMXMGB5HXDK4REZJPSRPH4I"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Testing::TestCase:lastIsExecDate-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colI5ELDBK7XVDQDA626NCPOMNAXM"),"lastIsExecDate",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWGQI2LNWCRCZBBTIBBE3LTDVAE"),org.radixware.kernel.common.enums.EValType.DATE_TIME,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Testing::TestCase:lastIsSuccessDate-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPI3FLY6XA5DV3LZKE3RO4PQFFQ"),"lastIsSuccessDate",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2QR2LMMP4VEDZFCZQSCZRZEC2M"),org.radixware.kernel.common.enums.EValType.DATE_TIME,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Testing::TestCase:notificationEmail-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colUSPDDDJLZ5AINICL3FIA4KD7GU"),"notificationEmail",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVKBIY5VZYZC2TOUJDYOKIVVTXA"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Testing::TestCase:seqIsFailCount-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKTWEBHYK2JGENOQY3SHZ27BQ6I"),"seqIsFailCount",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXMNVUVSRWVHARIGIRJC2Q5RRCY"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("0")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Testing::TestCase:cpuNanos-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colLLFER2QMUFH5BME327Q4IPNJNM"),"cpuNanos",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHZCZSIIAXVEJTN2KFUA2GNBPB4"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Testing::TestCase:dbNanos-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colYH7NIOG57JEHPD7QO4GMHI7K54"),"dbNanos",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsSZXNAOXR6BFFTLS6IOEVLYYT7E"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Testing::TestCase:extNanos-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colG4EPHGTTE5BM3DLJN55VNIVOBE"),"extNanos",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXK62QTDRFZBABPMJQ2NYOEIV5E"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Testing::TestCase:queueNanos-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMDKXQBA6KZFHZH6FTKW7KHF4Z4"),"queueNanos",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOYUI5F4WVZFAJHRHEQHTSDURZU"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Testing::TestCase:execDurationStr-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdROKBL5544NGPXNKFHAAELEX3SM"),"execDurationStr",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLSYXL7ZSXBF4RO23EKYZICMQU4"),org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadAccessorProxy.R_ACCESSOR_INSTANCE),

								/*Radix::Testing::TestCase:IGNORE_COPY_PROPS-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdGBA6UP64OZABZDQENIDZJC7JTY"),"IGNORE_COPY_PROPS",null,org.radixware.kernel.common.enums.EValType.JAVA_CLASS,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Testing::TestCase:groupIdByParent-Expression Property*/

								new org.radixware.kernel.server.meta.clazzes.RadSqmlPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMFEA34MFZ5BFRC4MMU4L7BU2XI"),"groupIdByParent",null,org.radixware.kernel.common.enums.EValType.INT,"NUMBER(9,0)",null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:Sql>select </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblHW4OSVMS27NRDISQAAAAAAAAAA\" PropId=\"col3AN3AUHYHNHNFBQ3J4PRDXRRXE\" Owner=\"TABLE\"/></xsc:Item><xsc:Item><xsc:Sql> from </xsc:Sql></xsc:Item><xsc:Item><xsc:TableSqlName TableId=\"tblHW4OSVMS27NRDISQAAAAAAAAAA\"/></xsc:Item><xsc:Item><xsc:Sql>\nwhere </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblHW4OSVMS27NRDISQAAAAAAAAAA\" PropId=\"col3AN3AUHYHNHNFBQ3J4PRDXRRXE\" Owner=\"TABLE\"/></xsc:Item><xsc:Item><xsc:Sql> is not null and rownum &lt; 2\nstart with </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblHW4OSVMS27NRDISQAAAAAAAAAA\" PropId=\"colN3UZ2Z4S27NRDISQAAAAAAAAAA\" Owner=\"TABLE\"/></xsc:Item><xsc:Item><xsc:Sql>=</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecHW4OSVMS27NRDISQAAAAAAAAAA\" PropId=\"colEZ4SH34S27NRDISQAAAAAAAAAA\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>\nCONNECT BY PRIOR </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblHW4OSVMS27NRDISQAAAAAAAAAA\" PropId=\"colEZ4SH34S27NRDISQAAAAAAAAAA\" Owner=\"TABLE\"/></xsc:Item><xsc:Item><xsc:Sql>=</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblHW4OSVMS27NRDISQAAAAAAAAAA\" PropId=\"colN3UZ2Z4S27NRDISQAAAAAAAAAA\" Owner=\"TABLE\"/></xsc:Item></xsc:Sqml>",true,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE)
						},

						/*Radix::Testing::TestCase:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth5XATY4FH27NRDISQAAAAAAAAAA"),"preCheck",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthCXPHPC5K27NRDISQAAAAAAAAAA"),"traceDebug",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("mess",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr56A3BSL62VELHCNWWFBNKIKYB4"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthDDPHPC5K27NRDISQAAAAAAAAAA"),"traceWarning",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("mess",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprFCYOQREAZ5FULNNWJQMXBC7FJI"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthG2ORZGNH27NRDISQAAAAAAAAAA"),"execute",false,true,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.INT),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthHPCYBENK27NRDISQAAAAAAAAAA"),"traceError",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("mess",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprKNCBHF3AYZGT7FP7RMB7Q4Y47U"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthJQDKUODJ3DNRDISQAAAAAAAAAA"),"run",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthUX5AEZVK27NRDISQAAAAAAAAAA"),"traceEvent",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("mess",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprYF2HVH77OFDX7JBXJ4OEM46V4I"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthcmdHGOOKC3J3DNRDISQAAAAAAAAAA"),"onCommand_Rerun",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("newPropValHandlersById",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr5JVSU2K7DREVHED2PGMSC4HVFA"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth6SYF27KUDRDVHB3VT3KZWBLA6I"),"assertTrue",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("message",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr5XVGB6H2TRAU3MMRAJSTK4TRNY")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("condition",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprXYGD4LNSNBHFFA6F4WEGYFORO4"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthJ3AVITK5UBBFNODKMUUVCAFCIY"),"assertTrue",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("condition",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprGMJAU2PYSZF5HH2BZDWUELZ7RE"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthT7BROO7KRNCX7JHDS2TFMQTOXY"),"assertFalse",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("message",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprQYFJJBTQP5B2JA26SVIA73MKAA")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("condition",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprXV3UHEWIH5C6ZMV7Y6UTYUWRMM"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthSD47QXIAS5DZ3MBDZQQL6W244Q"),"assertFalse",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("condition",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprOEUGRVFNMNEAVON4IOVWBM2DGI"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth5QJ47Q6XX5CZBPIRBU52AS5NSA"),"fail",true,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("message",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr3SQLGYOTWFANXIVDYNTUXA2Y3Y"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth2FTDOHNUYVABLIYL3WGU7P5QZY"),"fail",true,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthYLBMS7EFMFBO3PP6NISNFTIB7M"),"assertClobsEquals",true,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("message",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprLS7UVOPMK5BEPMGRB7SEPKMOP4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("expected",org.radixware.kernel.common.enums.EValType.CLOB,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprTBBDJ2N2WVGA7PAUKXSWBHVNTQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("actual",org.radixware.kernel.common.enums.EValType.CLOB,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprLBTOGPLL7BE7VJG3SLTJNI7VRQ"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthPJTXZQLHDFGWBMJRNFBLIG3OZM"),"assertEquals",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("message",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprQPN5KECCCBEYJAIYZEUR5DBLJY")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("expected",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprLIAYINHSRRCNRA6CSGIION4VCU")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("actual",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprP54CAHBIQZFEBDYE7A7RS5FXJA"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth3MCQPKQC6JBA3MX55ZTQ3D46VU"),"assertEquals",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("expected",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprP4D3B7UNGVEARGJSVAHAJLTREE")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("actual",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprRHHISFJJGZAEXB2BWMYBQ4TGC4"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthXNLJZLP3LJGFNDY74NBRGKWNQ4"),"assertEquals",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("expected",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprJONZRGML4RAGTJBMQIGMQ5CWAI")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("actual",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr3B3RAZEQWBGDHIYUCREIK2A6ZE"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth6V3SFI54N5FJDPD6VVR4VSHESM"),"assertEquals",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("message",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprLNSCFF5RRVCKPK3QFWJK6YEFVY")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("expected",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr772A5TMRDVGATPBPH3SABDWARM")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("actual",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprXDT22WER4RAF7LPNKUJL4Q26U4"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthUCYN5HCYHFBDLB3OGGJB4RJK4Y"),"assertNotNull",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("message",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprACGM3LADIJFUTE3K53C6ZHWUCA")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("object",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr5KVTPRN35RHQ3BXX3EXVWTN5RQ"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthFBWGWAUMO5HNRNS6672X5PHDPE"),"assertNotNull",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("object",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprLNRQWQM67NEATGA6VKYJV4PD3A"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthTZPU7JDPKFEIHMOTRPLGKSWRUA"),"assertNull",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("message",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr52LBU66GCJAQJG2FNLCRJTEGH4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("object",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprSCLGNQJFMJHFREBCNLN35QSH4M"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthQ2VGN4XEHVDUZPIPVXAURNYPZA"),"assertNull",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("object",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprFW7VVUWLJNGTPBLG6INFLEHONM"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthL2VGBEZXBZAZZIP674RYALND6Q"),"format",true,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("message",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprSVHWZKBRDBCVHFXEDQKXXELBKA")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("expected",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprANPEIRD2K5AUNP4H5XNDUWQLJM")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("actual",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr4KE24OEGCFECBC52H3JUW5RZP4"))
								},org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthESCQUIEVWVAK5PVJX2QQQXRVD4"),"formatClassAndValue",true,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("value",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprKLP2RCS2OZCSNMBCPVKT4NXNIQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("valueString",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprJGRQTQA2ORHB5PRJ43IGXGWB3M"))
								},org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthNKGGYMI4KRAMDOFE26HU3LKHKM"),"isExpected",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("traceItem",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprXCSYKCEHONFAROIAWWQYJ2VWOQ"))
								},org.radixware.kernel.common.enums.EValType.BOOL),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthD77F2HBL2FGSVPJHR3SEAMPWMA"),"afterInit",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("src",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprJNPEFKH4DZGF3JV4LCXNPMHNHM")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("phase",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprKZQG3IRBK5EPBO2HXSZOKVGUYM"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth_loadByPK_________________"),"loadByPK",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("id",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprN3UZ2Z4S27NRDISQAAAAAAAAAA")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("checkExistance",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprCheckExisstance___________"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthFMXUFM5J25E4TNM4PR73F6V3E4"),"beforeCreate",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("src",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr5LBQWSP2LFDKHECCWVHGAU2QKU"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth2ID6LLB4ORG75JFEH45HYYK75U"),"copyPropVal",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("prop",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr2NNN4Q3KPFEW3ESNEFOMHZU5MY")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("src",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprSLJJAI3SLNBAPL6P5UH4FGF5BA"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthKNZ5Z7AS6RFRPIX6ZD5JDKEC34"),"beforeDelete",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth5KJGXJULWBGKDDBAKVMRW7HNPA"),"exportToXml",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("exportChilds",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprGR327WCOTZBWZDSWHNJ7H4UJNI")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("isRoot",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr6SOURFU6FBDV5CHIO4MJI6H7S4"))
								},org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth3BP4CLZO7ZETHDYPIJRBNGAQXY"),"exportExtensionProps",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("xml",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr7ZNGXTOFHJGFDF4I3D6D5VFAHE")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("isRoot",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr7GEKXCJQAJCCDDEMYKVG4G6USA"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthL37P4BEB7BG5DJXDQC2PLDBAPQ"),"exportThis",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("data",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprMPL7D7ZY5VFRNGBQHY7VJSFTVU")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("exportChilds",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprYOYBZA67EBHVNMP2PGDP7JAKU4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("isRoot",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprOQ6Q2YKUGNDGXBVFZCFY7MZLLQ"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth_loadByPidStr_____________"),"loadByPidStr",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("pidAsStr",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprPidAsStr__________________")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("checkExistance",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprCheckExisstance___________"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthTRYYMH7AZJFPRDTQ4MFV7ZHYA4"),"loadByGuid",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("extGuid",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprG6EPTWQS5FABLEAXDCA25QIMIY")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("classId",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprBMCRUPMGUNENHICIJLTCBSKW5M"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthJ5GLJY2Q3NGU3PT3RJS4CCH7EI"),"create",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("classId",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprPNWZ7CC3UZFTJCVXKDRVBDPCDI")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("extGuid",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprBH3M3WUTQRA2VMWL24CDYGY5WI"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthZOBF2Y7RW5GHRIISAWDFABPXNA"),"updateFromCfgItem",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("cfgItem",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr5EAV5RVH6NDWRIJUUQDAZBUZ3Y")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("helper",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprCQCAF5OBPRH5RO7SQTIB4WJI5Y"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthS4ISN6D2LZCILMVPUQAKJ6RIJM"),"importThis",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("xml",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr7XUDTQC2U5GKBIAQBMMOMY2PII")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("groupId",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprBVFME7XLHVDNRKF3YIDXDQUSBQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("parentId",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprUPDIT4YWOBDZVI5ZPMJ2LTF7RQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("helper",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr6V6ZOL2SZBE7NDADQMBROPXWUI")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("importChilds",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr4FDMCI3G45DTDELOHNBEHQ4IMI"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthDMC3HC6MOZHXVGBUDB3FCHYEK4"),"importExtensionProps",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("xml",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprCMS44XIB6BEWZHH5D5LODNHMLU")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("helper",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprPRXIDEGBRZGUPK2PK2TEKSC7JU"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthWBHXNX5SN5FM7OIOO6CFBZT6HM"),"importOne",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("xml",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr2RLOK42HYZCS3LYDTUKUPR2RD4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("helper",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprUAHGASFHCNFIBNEQK3K2GZ7XLQ"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthcmdM5Y74G3BRFHTRGIHVZN5WY5IXA"),"onCommand_Import",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("input",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprDI4QEDBHZVDO5FR3ZKX2LTZ3FQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("newPropValHandlersById",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprCL4NTKZWTNEFDE7SSPNWPCVCQ4"))
								},org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthAKCTMIS2BBCJJHTLNWKA6FZDXU"),"exportAll",true,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("data",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprSY7BLG7O3BHKBIUZS7UE5KVS3Y")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("ownerTestId",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprNDH2WV4MAJHHRKACZPO27UEAIE")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("iter",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprZSBNBSJ4SVB67OQSKZWABDMCVQ"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth4G2UIWI2TNGGJAKT3ONSRKX33A"),"importAll",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("xml",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr2RLOK42HYZCS3LYDTUKUPR2RD4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("helper",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr5AEE44BN5VCFBEDIUMN2FNENKA"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthIXFKBKZ3MZATPG243BJK4U7TWU"),"exportChilds",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("xTest",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprC52ZWQL5UZDS7O5PMY6UWZRWWI"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth433D3RL6SJFPDCH7ZJ5CEQD2TY"),"exportChilds",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("data",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprBBF25623JRBINH4RLJR2DDXEME"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthFZ5ZVE3YOJHSNNTZYKFYV3PQTQ"),"checkForProblemTest",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("helper",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr2VYRDSVQTNDQLFWY25LKTC6FHM"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthPROG4VJKQZFAHBU7AEDG6CKXG4"),"canHaveChilds",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthQOWHGOMGJJG73ASHN575GHBUVQ"),"canHaveChildsByParent",false,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth3NFP5J7MTZCCHNQPQUEDFIXYRI"),"getAllowedChildClassIds",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,org.radixware.kernel.common.enums.EValType.JAVA_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthMK5BX3256RGWXCUKEU3M25FDW4"),"getChildTests",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,null,org.radixware.kernel.common.enums.EValType.JAVA_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthNPFMHF35MFCMBKEGMWEPTZ6PNA"),"hasChildTests",false,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth3CG6MM4COJD4JKJFM6D5NDL3UI"),"isProblemItem",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("child",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr6KA2NUXMJNBHHA5OGWE4ZUHSRA"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthU2YYM5SLUFAZLI3UUSBOLDLMPM"),"getIgnoredPropsList",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,org.radixware.kernel.common.enums.EValType.JAVA_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthGZDY76GQDVEENNFB33JRO5ADCU"),"beforeInit",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("initPropValsById",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprLQYC2RRFNFDLNKNRMRA7GA5WII")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("src",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr7WPKHW5PDJCDTHWX7SDJQ3RZT4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("phase",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprJN6L6U4B5FCOFIBAVAVDJ3BJ2U"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthFBEGDZABWRDQ7PPGBNMPMLV67A"),"reportOwnerChanged",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("helper",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprUU4DICWAKBB77CEK2ZMTVQG6RI")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("prevParentId",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprWZL5YQRP7RGPXJURAKKS73EA4I")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("newParentId",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr3JI2GCHENRDE7OS327H4W4KRME")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("prevGroupId",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr2XQJZH56PVCBPOZWDY43IZHOQQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("newGroupId",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprDMXCLGRACNBJVFKHOOJNT6LOKE"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthQXRW7PZ4QBBEJAJL57JFPES7EQ"),"getCfgReferenceExtGuid",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthTIFZEEQC2NEUNPFIRAGRH2B7EY"),"getCfgReferencePropId",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.JAVA_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth4T6TOADMIRAVVFHOHZUUI5ECOA"),"getDurationStr",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthJY4KYWCWIVCX7CUODHX27BYNMY"),"afterExecute",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,null)
						},
						null,
						org.radixware.kernel.common.enums.EAccessAreaType.NONE,null,null,false);
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta_adcNJYOKUF6BFH4FCUYD4QHB5MVV4 = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("adcNJYOKUF6BFH4FCUYD4QHB5MVV4"),"CfgObjectLookupAdvizor",null,

						org.radixware.kernel.common.enums.EClassType.ENTITY,
						null,
						null,
						null,

						/*Radix::Testing::TestCase:CfgObjectLookupAdvizor:Properties-Properties*/
						null,

						/*Radix::Testing::TestCase:CfgObjectLookupAdvizor:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthJPAOFWUPW5GIJKM3O7SE3KNB7Q"),"getCfgObjectsByExtGuid",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("extGuid",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprUHB7QZBKHFD7LBNC422X3EGQ4M")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("considerContext",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr2F6PLICI4ZCSRGMFEYGOH3ZVTE")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("context",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprB4UJM7ASHRBXJGTBF5AZUBXFX4"))
								},org.radixware.kernel.common.enums.EValType.JAVA_CLASS)
						},
						null,
						null,null,null,false);
}

/* Radix::Testing::TestCase - Desktop Executable*/

/*Radix::Testing::TestCase-Entity Class*/

package org.radixware.ads.Testing.explorer;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase")
public interface TestCase {
	/*Group model adapter class. This is the base class for 
	all of selector presentation models of this entity*/
	public static class DefaultGroupModel extends org.radixware.kernel.common.client.models.GroupModel{
		public DefaultGroupModel(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadSelectorPresentationDef def){super(userSession,def);}

		/*Radix::Testing::TestCaseGroup:testId:testId-Presentation Property*/




		public class TestId extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
			public TestId(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
				super(model,def);
			}
			@Override
			public Object getValueObject(){return getValue();}
			@Override
			public void setValueObject(final Object x){
				Int dummy = ((Int)x);
				setValue(dummy);
			}












			@SuppressWarnings({"unused","unchecked"})
			@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCaseGroup:testId:testId")
			public  Int getValue() {
				return Value;
			}

			@SuppressWarnings({"unused","unchecked"})
			@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCaseGroup:testId:testId")
			public   void setValue(Int val) {
				Value = val;
			}
		}
		public TestId getTestId(){return (TestId)getProperty(pgpL4XG335VT5HJ7E7RSMVKFPF3ZM);}

		/*Radix::Testing::TestCaseGroup:testGroupId:testGroupId-Presentation Property*/




		public class TestGroupId extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
			public TestGroupId(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
				super(model,def);
			}
			@Override
			public Object getValueObject(){return getValue();}
			@Override
			public void setValueObject(final Object x){
				Int dummy = ((Int)x);
				setValue(dummy);
			}












			@SuppressWarnings({"unused","unchecked"})
			@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCaseGroup:testGroupId:testGroupId")
			public  Int getValue() {
				return Value;
			}

			@SuppressWarnings({"unused","unchecked"})
			@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCaseGroup:testGroupId:testGroupId")
			public   void setValue(Int val) {
				Value = val;
			}
		}
		public TestGroupId getTestGroupId(){return (TestGroupId)getProperty(pgpYZUQHHDC45CQJIS4PKLM73TMDQ);}

		/*Radix::Testing::TestCaseGroup:testParentId:testParentId-Presentation Property*/




		public class TestParentId extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
			public TestParentId(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
				super(model,def);
			}
			@Override
			public Object getValueObject(){return getValue();}
			@Override
			public void setValueObject(final Object x){
				Int dummy = ((Int)x);
				setValue(dummy);
			}












			@SuppressWarnings({"unused","unchecked"})
			@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCaseGroup:testParentId:testParentId")
			public  Int getValue() {
				return Value;
			}

			@SuppressWarnings({"unused","unchecked"})
			@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCaseGroup:testParentId:testParentId")
			public   void setValue(Int val) {
				Value = val;
			}
		}
		public TestParentId getTestParentId(){return (TestParentId)getProperty(pgpIZ3LCFNB45HB5FS4XPQCJ32JZA);}











		public org.radixware.ads.Testing.explorer.TestCase.TestCase_DefaultModel getEntity(int i) throws InterruptedException, org.radixware.kernel.common.client.exceptions.BrokenEntityObjectException, org.radixware.kernel.common.exceptions.ServiceClientException { return (org.radixware.ads.Testing.explorer.TestCase.TestCase_DefaultModel )  super.getEntity(i);}
	}














































































































































































































	/*Radix::Testing::TestCase:groupId:groupId-Presentation Property*/


	public class GroupId extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public GroupId(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Int dummy = ((Int)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:groupId:groupId")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:groupId:groupId")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public GroupId getGroupId();
	/*Radix::Testing::TestCase:groupRef:groupRef-Presentation Property*/


	public class GroupRef extends org.radixware.kernel.common.client.models.items.properties.PropertyRef{
		public GroupRef(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadParentRefPropertyDef def){
			super(model,def);
		}
		@Override
		public 
		org.radixware.ads.Testing.explorer.TestCase.TestCase_DefaultModel openEntityModel()  throws org.radixware.kernel.common.exceptions.ServiceClientException, InterruptedException{
		     return (org.radixware.ads.Testing.explorer.TestCase.TestCase_DefaultModel)super.openEntityModel();
		}
		@Override
		public 
		org.radixware.ads.Testing.explorer.TestCase.DefaultGroupModel openGroupModel(){
		     return (org.radixware.ads.Testing.explorer.TestCase.DefaultGroupModel)super.openGroupModel();
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.client.types.Reference dummy = ((org.radixware.kernel.common.client.types.Reference)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:groupRef:groupRef")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:groupRef:groupRef")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public GroupRef getGroupRef();
	/*Radix::Testing::TestCase:seq:seq-Presentation Property*/


	public class Seq extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public Seq(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Int dummy = ((Int)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:seq:seq")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:seq:seq")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public Seq getSeq();
	/*Radix::Testing::TestCase:resultComment:resultComment-Presentation Property*/


	public class ResultComment extends org.radixware.kernel.common.client.models.items.properties.PropertyClob{
		public ResultComment(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:resultComment:resultComment")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:resultComment:resultComment")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ResultComment getResultComment();
	/*Radix::Testing::TestCase:authorName:authorName-Presentation Property*/


	public class AuthorName extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public AuthorName(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:authorName:authorName")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:authorName:authorName")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public AuthorName getAuthorName();
	/*Radix::Testing::TestCase:parentId:parentId-Presentation Property*/


	public class ParentId extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public ParentId(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Int dummy = ((Int)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:parentId:parentId")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:parentId:parentId")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public ParentId getParentId();
	/*Radix::Testing::TestCase:extNanos:extNanos-Presentation Property*/


	public class ExtNanos extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public ExtNanos(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Int dummy = ((Int)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:extNanos:extNanos")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:extNanos:extNanos")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public ExtNanos getExtNanos();
	/*Radix::Testing::TestCase:startTime:startTime-Presentation Property*/


	public class StartTime extends org.radixware.kernel.common.client.models.items.properties.PropertyDateTime{
		public StartTime(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			java.sql.Timestamp dummy = ((java.sql.Timestamp)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:startTime:startTime")
		public  java.sql.Timestamp getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:startTime:startTime")
		public   void setValue(java.sql.Timestamp val) {
			Value = val;
		}
	}
	public StartTime getStartTime();
	/*Radix::Testing::TestCase:notes:notes-Presentation Property*/


	public class Notes extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public Notes(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:notes:notes")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:notes:notes")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Notes getNotes();
	/*Radix::Testing::TestCase:lastIsExecDate:lastIsExecDate-Presentation Property*/


	public class LastIsExecDate extends org.radixware.kernel.common.client.models.items.properties.PropertyDateTime{
		public LastIsExecDate(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			java.sql.Timestamp dummy = ((java.sql.Timestamp)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:lastIsExecDate:lastIsExecDate")
		public  java.sql.Timestamp getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:lastIsExecDate:lastIsExecDate")
		public   void setValue(java.sql.Timestamp val) {
			Value = val;
		}
	}
	public LastIsExecDate getLastIsExecDate();
	/*Radix::Testing::TestCase:extGuid:extGuid-Presentation Property*/


	public class ExtGuid extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public ExtGuid(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:extGuid:extGuid")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:extGuid:extGuid")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ExtGuid getExtGuid();
	/*Radix::Testing::TestCase:seqIsFailCount:seqIsFailCount-Presentation Property*/


	public class SeqIsFailCount extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public SeqIsFailCount(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Int dummy = ((Int)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:seqIsFailCount:seqIsFailCount")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:seqIsFailCount:seqIsFailCount")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public SeqIsFailCount getSeqIsFailCount();
	/*Radix::Testing::TestCase:author:author-Presentation Property*/


	public class Author extends org.radixware.kernel.common.client.models.items.properties.PropertyRef{
		public Author(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadParentRefPropertyDef def){
			super(model,def);
		}
		@Override
		public 
		org.radixware.ads.Acs.explorer.User.User_DefaultModel openEntityModel()  throws org.radixware.kernel.common.exceptions.ServiceClientException, InterruptedException{
		     return (org.radixware.ads.Acs.explorer.User.User_DefaultModel)super.openEntityModel();
		}
		@Override
		public 
		org.radixware.ads.Acs.explorer.User.DefaultGroupModel openGroupModel(){
		     return (org.radixware.ads.Acs.explorer.User.DefaultGroupModel)super.openGroupModel();
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.client.types.Reference dummy = ((org.radixware.kernel.common.client.types.Reference)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:author:author")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:author:author")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public Author getAuthor();
	/*Radix::Testing::TestCase:cpuNanos:cpuNanos-Presentation Property*/


	public class CpuNanos extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public CpuNanos(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Int dummy = ((Int)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:cpuNanos:cpuNanos")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:cpuNanos:cpuNanos")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public CpuNanos getCpuNanos();
	/*Radix::Testing::TestCase:parent:parent-Presentation Property*/


	public class Parent extends org.radixware.kernel.common.client.models.items.properties.PropertyRef{
		public Parent(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadParentRefPropertyDef def){
			super(model,def);
		}
		@Override
		public 
		org.radixware.ads.Testing.explorer.TestCase.TestCase_DefaultModel openEntityModel()  throws org.radixware.kernel.common.exceptions.ServiceClientException, InterruptedException{
		     return (org.radixware.ads.Testing.explorer.TestCase.TestCase_DefaultModel)super.openEntityModel();
		}
		@Override
		public 
		org.radixware.ads.Testing.explorer.TestCase.DefaultGroupModel openGroupModel(){
		     return (org.radixware.ads.Testing.explorer.TestCase.DefaultGroupModel)super.openGroupModel();
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.client.types.Reference dummy = ((org.radixware.kernel.common.client.types.Reference)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:parent:parent")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:parent:parent")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public Parent getParent();
	/*Radix::Testing::TestCase:queueNanos:queueNanos-Presentation Property*/


	public class QueueNanos extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public QueueNanos(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Int dummy = ((Int)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:queueNanos:queueNanos")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:queueNanos:queueNanos")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public QueueNanos getQueueNanos();
	/*Radix::Testing::TestCase:actor:actor-Presentation Property*/


	public class Actor extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public Actor(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:actor:actor")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:actor:actor")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Actor getActor();
	/*Radix::Testing::TestCase:id:id-Presentation Property*/


	public class Id extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public Id(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Int dummy = ((Int)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:id:id")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:id:id")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public Id getId();
	/*Radix::Testing::TestCase:lastIsSuccessDate:lastIsSuccessDate-Presentation Property*/


	public class LastIsSuccessDate extends org.radixware.kernel.common.client.models.items.properties.PropertyDateTime{
		public LastIsSuccessDate(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			java.sql.Timestamp dummy = ((java.sql.Timestamp)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:lastIsSuccessDate:lastIsSuccessDate")
		public  java.sql.Timestamp getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:lastIsSuccessDate:lastIsSuccessDate")
		public   void setValue(java.sql.Timestamp val) {
			Value = val;
		}
	}
	public LastIsSuccessDate getLastIsSuccessDate();
	/*Radix::Testing::TestCase:traceProfile:traceProfile-Presentation Property*/


	public class TraceProfile extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public TraceProfile(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:traceProfile:traceProfile")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:traceProfile:traceProfile")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public TraceProfile getTraceProfile();
	/*Radix::Testing::TestCase:finishTime:finishTime-Presentation Property*/


	public class FinishTime extends org.radixware.kernel.common.client.models.items.properties.PropertyDateTime{
		public FinishTime(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			java.sql.Timestamp dummy = ((java.sql.Timestamp)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:finishTime:finishTime")
		public  java.sql.Timestamp getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:finishTime:finishTime")
		public   void setValue(java.sql.Timestamp val) {
			Value = val;
		}
	}
	public FinishTime getFinishTime();
	/*Radix::Testing::TestCase:active:active-Presentation Property*/


	public class Active extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public Active(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:active:active")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:active:active")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public Active getActive();
	/*Radix::Testing::TestCase:notificationEmail:notificationEmail-Presentation Property*/


	public class NotificationEmail extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public NotificationEmail(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:notificationEmail:notificationEmail")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:notificationEmail:notificationEmail")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public NotificationEmail getNotificationEmail();
	/*Radix::Testing::TestCase:dbNanos:dbNanos-Presentation Property*/


	public class DbNanos extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public DbNanos(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Int dummy = ((Int)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:dbNanos:dbNanos")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:dbNanos:dbNanos")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public DbNanos getDbNanos();
	/*Radix::Testing::TestCase:result:result-Presentation Property*/


	public class Result extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public Result(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:result:result")
		public  org.radixware.kernel.common.enums.EEventSeverity getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:result:result")
		public   void setValue(org.radixware.kernel.common.enums.EEventSeverity val) {
			Value = val;
		}
	}
	public Result getResult();
	/*Radix::Testing::TestCase:pathInTree:pathInTree-Presentation Property*/


	public class PathInTree extends org.radixware.kernel.common.client.models.items.properties.PropertyArrStr{
		public PathInTree(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.types.ArrStr dummy = ((org.radixware.kernel.common.types.ArrStr)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:pathInTree:pathInTree")
		public  org.radixware.kernel.common.types.ArrStr getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:pathInTree:pathInTree")
		public   void setValue(org.radixware.kernel.common.types.ArrStr val) {
			Value = val;
		}
	}
	public PathInTree getPathInTree();
	/*Radix::Testing::TestCase:canHaveChilds:canHaveChilds-Presentation Property*/


	public class CanHaveChilds extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public CanHaveChilds(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:canHaveChilds:canHaveChilds")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:canHaveChilds:canHaveChilds")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public CanHaveChilds getCanHaveChilds();
	/*Radix::Testing::TestCase:isParent:isParent-Presentation Property*/


	public class IsParent extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public IsParent(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:isParent:isParent")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:isParent:isParent")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public IsParent getIsParent();
	/*Radix::Testing::TestCase:thereIsChilds:thereIsChilds-Presentation Property*/


	public class ThereIsChilds extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public ThereIsChilds(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:thereIsChilds:thereIsChilds")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:thereIsChilds:thereIsChilds")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public ThereIsChilds getThereIsChilds();
	/*Radix::Testing::TestCase:isGroup:isGroup-Presentation Property*/


	public class IsGroup extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public IsGroup(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:isGroup:isGroup")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:isGroup:isGroup")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public IsGroup getIsGroup();
	/*Radix::Testing::TestCase:execDurationStr:execDurationStr-Presentation Property*/


	public class ExecDurationStr extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public ExecDurationStr(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:execDurationStr:execDurationStr")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:execDurationStr:execDurationStr")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ExecDurationStr getExecDurationStr();
	/*Radix::Testing::TestCase:failEventSeverity:failEventSeverity-Presentation Property*/


	public class FailEventSeverity extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public FailEventSeverity(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:failEventSeverity:failEventSeverity")
		public  org.radixware.kernel.common.enums.EEventSeverity getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:failEventSeverity:failEventSeverity")
		public   void setValue(org.radixware.kernel.common.enums.EEventSeverity val) {
			Value = val;
		}
	}
	public FailEventSeverity getFailEventSeverity();
	/*Radix::Testing::TestCase:seqTitle:seqTitle-Presentation Property*/


	public class SeqTitle extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public SeqTitle(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:seqTitle:seqTitle")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:seqTitle:seqTitle")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public SeqTitle getSeqTitle();
	/*Radix::Testing::TestCase:classTitle:classTitle-Presentation Property*/


	public class ClassTitle extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public ClassTitle(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:classTitle:classTitle")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:classTitle:classTitle")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ClassTitle getClassTitle();
	public static class Rerun extends org.radixware.kernel.common.client.models.items.Command{
		protected Rerun(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public void send() throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,null);
			processResponse(response, null);
		}

	}

	public static class Import extends org.radixware.kernel.common.client.models.items.Command{
		protected Import(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public org.radixware.schemas.types.StrDocument send(org.radixware.ads.Testing.common.ImpExpXsd.TestCaseDocument input ) throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,input);
			org.apache.xmlbeans.XmlObject xmlResponse = processResponse(response, null);
			return (org.radixware.schemas.types.StrDocument)org.radixware.kernel.common.utils.XmlObjectProcessor.castToXmlClass(getClass().getClassLoader(),xmlResponse,org.radixware.schemas.types.StrDocument.class);
		}

	}

	public static class Export extends org.radixware.kernel.common.client.models.items.Command{
		protected Export(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}

	}

	public static class ChangeGroupSettings extends org.radixware.kernel.common.client.models.items.Command{
		protected ChangeGroupSettings(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}

	}



}

/* Radix::Testing::TestCase - Desktop Meta*/

/*Radix::Testing::TestCase-Entity Class*/

package org.radixware.ads.Testing.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class TestCase_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::Testing::TestCase:Presentations-Entity Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),
			"Radix::Testing::TestCase",
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("imgBP4452EV3NADFBNW75KWBB46V4"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("sprNGT5DE5G27NRDISQAAAAAAAAAA"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBJFDUM24OXOBDFKUAAMPGXUWTQ"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBNFDUM24OXOBDFKUAAMPGXUWTQ"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBJFDUM24OXOBDFKUAAMPGXUWTQ"),8192,

			/*Radix::Testing::TestCase:Properties-Properties*/
			new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

					/*Radix::Testing::TestCase:resultComment:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colC46NAASN3PNRDISQAAAAAAAAAA"),
						"resultComment",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOBFDUM24OXOBDFKUAAMPGXUWTQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),
						org.radixware.kernel.common.enums.EValType.CLOB,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Testing::TestCase:resultComment:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						true,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Testing::TestCase:parentId:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEZ4SH34S27NRDISQAAAAAAAAAA"),
						"parentId",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsN5FDUM24OXOBDFKUAAMPGXUWTQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Testing::TestCase:parentId:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Testing::TestCase:startTime:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGBJZDIUS27NRDISQAAAAAAAAAA"),
						"startTime",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNZFDUM24OXOBDFKUAAMPGXUWTQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),
						org.radixware.kernel.common.enums.EValType.DATE_TIME,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Testing::TestCase:startTime:PropertyPresentation:Edit Options:-Edit Mask Date Time*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskDateTime("dd.MM.yyyy HH:mm:ss.zzz",null,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Testing::TestCase:notes:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHWP3QCET27NRDISQAAAAAAAAAA"),
						"notes",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNVFDUM24OXOBDFKUAAMPGXUWTQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Testing::TestCase:notes:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,true),
						null,
						null,
						null,
						true,-1,-1,1,
						true,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Testing::TestCase:parent:PropertyPresentation-Parent Ref Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colM7PLLGVJ27NRDISQAAAAAAAAAA"),
						"parent",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),
						org.radixware.kernel.common.enums.EValType.PARENT_REF,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						20,
						null,
						null,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.PROGRAMMATICALLY,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,
						null,
						null,
						null,
						null,
						true,-1,-1,1,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblHW4OSVMS27NRDISQAAAAAAAAAA"),
						null,
						null,
						null,
						133693439,
						133693439,false),

					/*Radix::Testing::TestCase:actor:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMWOGO4MS27NRDISQAAAAAAAAAA"),
						"actor",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNRFDUM24OXOBDFKUAAMPGXUWTQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Testing::TestCase:actor:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,250,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Testing::TestCase:id:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colN3UZ2Z4S27NRDISQAAAAAAAAAA"),
						"id",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNNFDUM24OXOBDFKUAAMPGXUWTQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::Testing::TestCase:id:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Testing::TestCase:traceProfile:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPMEM3RSM3PNRDISQAAAAAAAAAA"),
						"traceProfile",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNJFDUM24OXOBDFKUAAMPGXUWTQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						28,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("Debug"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						true,
						true,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("cpeF3QUTT7XY3NRDB6ZAALOMT5GDM"),
						false,

						/*Radix::Testing::TestCase:traceProfile:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Testing::TestCase:finishTime:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQKCERAMS27NRDISQAAAAAAAAAA"),
						"finishTime",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNFFDUM24OXOBDFKUAAMPGXUWTQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),
						org.radixware.kernel.common.enums.EValType.DATE_TIME,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Testing::TestCase:finishTime:PropertyPresentation:Edit Options:-Edit Mask Date Time*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskDateTime("dd.MM.yyyy HH:mm:ss.zzz",null,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Testing::TestCase:result:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colYWL4NXUS27NRDISQAAAAAAAAAA"),
						"result",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNBFDUM24OXOBDFKUAAMPGXUWTQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acsFRO5RTQOG7NRDJH2ACQMTAIZT4"),
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Testing::TestCase:result:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acsFRO5RTQOG7NRDJH2ACQMTAIZT4"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.NONE,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Testing::TestCase:active:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQP4LKSN3IFHITPAQXU7J72H6FA"),
						"active",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsS6CNAK6UGVHUZOK2UKXFMVTW6Q"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),
						org.radixware.kernel.common.enums.EValType.BOOL,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("1"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::Testing::TestCase:active:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Testing::TestCase:groupId:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3AN3AUHYHNHNFBQ3J4PRDXRRXE"),
						"groupId",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Testing::TestCase:groupId:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Testing::TestCase:groupRef:PropertyPresentation-Parent Ref Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3T77RRLFUBHDLA7X4GCO27SCOU"),
						"groupRef",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXUGTSHLNLZAZ5BUI7QF47DGFXE"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),
						org.radixware.kernel.common.enums.EValType.PARENT_REF,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						20,
						null,
						null,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.PROGRAMMATICALLY,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,
						null,
						null,
						null,
						null,
						true,-1,-1,1,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblHW4OSVMS27NRDISQAAAAAAAAAA"),
						null,
						null,
						null,
						133693439,
						133693439,false),

					/*Radix::Testing::TestCase:extGuid:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJGTICYXJ35G5TJ26LFHQAX2QRY"),
						"extGuid",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::Testing::TestCase:extGuid:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,100,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Testing::TestCase:seq:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3ZQVM54K5FEL7D7RH4HIYI62Y4"),
						"seq",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsC6YFHCMLMRGT3II3DDLRF6YCI4"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("0"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::Testing::TestCase:seq:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Testing::TestCase:seqTitle:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdYRLSFFVV2ZFIJFFAT34VLBDB4Y"),
						"seqTitle",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMNQIG6HQIRARFD224IAWTHH6BQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Testing::TestCase:seqTitle:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Testing::TestCase:classTitle:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdZSHM4TXE3XNRDISQAAAAAAAAAA"),
						"classTitle",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEMSKREJXYRBARPN3PY5NLBLPC4"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
						0,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Testing::TestCase:classTitle:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Testing::TestCase:failEventSeverity:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdV7KTVRF6PJBPLHTS5N72A7W7PU"),
						"failEventSeverity",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
						4,
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acsFRO5RTQOG7NRDJH2ACQMTAIZT4"),
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("2"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Testing::TestCase:failEventSeverity:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acsFRO5RTQOG7NRDJH2ACQMTAIZT4"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.NONE,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Testing::TestCase:isGroup:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdR7AAOIBTTNDT5JUMOATGCT22JQ"),
						"isGroup",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),
						org.radixware.kernel.common.enums.EValType.BOOL,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Testing::TestCase:isGroup:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Testing::TestCase:thereIsChilds:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdQO2OHKBQTNB2LK2NR3TUUWDQWQ"),
						"thereIsChilds",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),
						org.radixware.kernel.common.enums.EValType.BOOL,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Testing::TestCase:thereIsChilds:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Testing::TestCase:canHaveChilds:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd4CDENELMOJFFVP5WDQGC72CQRA"),
						"canHaveChilds",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),
						org.radixware.kernel.common.enums.EValType.BOOL,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Testing::TestCase:canHaveChilds:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Testing::TestCase:isParent:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdGDMZ3TJQRFHTZLUGK5MYZ5LUOA"),
						"isParent",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),
						org.radixware.kernel.common.enums.EValType.BOOL,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Testing::TestCase:isParent:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Testing::TestCase:pathInTree:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd27YHYAOF2RAKXOKNUKIM53FSMU"),
						"pathInTree",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),
						org.radixware.kernel.common.enums.EValType.ARR_STR,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Testing::TestCase:pathInTree:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Testing::TestCase:authorName:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colCFZB5S5MFFAPJM2ENTFD66CEZI"),
						"authorName",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsESLW3G2YARA4HJQG6XW5E54LQE"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Testing::TestCase:authorName:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,250,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Testing::TestCase:author:PropertyPresentation-Parent Ref Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colL4QQIGK7MFGF5FCW7HHEA4YIZI"),
						"author",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBDSC6QRKOZF33C2U7PFDJLEPHM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),
						org.radixware.kernel.common.enums.EValType.PARENT_REF,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						20,
						null,
						null,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,
						null,
						null,
						null,
						null,
						true,-1,-1,1,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblSY4KIOLTGLNRDHRZABQAQH3XQ4"),
						null,
						null,
						null,
						133693439,
						133693439,false),

					/*Radix::Testing::TestCase:lastIsExecDate:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colI5ELDBK7XVDQDA626NCPOMNAXM"),
						"lastIsExecDate",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWGQI2LNWCRCZBBTIBBE3LTDVAE"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),
						org.radixware.kernel.common.enums.EValType.DATE_TIME,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Testing::TestCase:lastIsExecDate:PropertyPresentation:Edit Options:-Edit Mask Date Time*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskDateTime("dd.MM.yyyy HH:mm:ss",null,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Testing::TestCase:lastIsSuccessDate:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPI3FLY6XA5DV3LZKE3RO4PQFFQ"),
						"lastIsSuccessDate",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2QR2LMMP4VEDZFCZQSCZRZEC2M"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),
						org.radixware.kernel.common.enums.EValType.DATE_TIME,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Testing::TestCase:lastIsSuccessDate:PropertyPresentation:Edit Options:-Edit Mask Date Time*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskDateTime("dd.MM.yyyy HH:mm:ss",null,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Testing::TestCase:notificationEmail:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colUSPDDDJLZ5AINICL3FIA4KD7GU"),
						"notificationEmail",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVKBIY5VZYZC2TOUJDYOKIVVTXA"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Testing::TestCase:notificationEmail:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,100,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Testing::TestCase:seqIsFailCount:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKTWEBHYK2JGENOQY3SHZ27BQ6I"),
						"seqIsFailCount",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXMNVUVSRWVHARIGIRJC2Q5RRCY"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("0"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Testing::TestCase:seqIsFailCount:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Testing::TestCase:cpuNanos:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colLLFER2QMUFH5BME327Q4IPNJNM"),
						"cpuNanos",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHZCZSIIAXVEJTN2KFUA2GNBPB4"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Testing::TestCase:cpuNanos:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999999999999L,999999999999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Testing::TestCase:dbNanos:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colYH7NIOG57JEHPD7QO4GMHI7K54"),
						"dbNanos",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsSZXNAOXR6BFFTLS6IOEVLYYT7E"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Testing::TestCase:dbNanos:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999999999999L,999999999999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Testing::TestCase:extNanos:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colG4EPHGTTE5BM3DLJN55VNIVOBE"),
						"extNanos",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXK62QTDRFZBABPMJQ2NYOEIV5E"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Testing::TestCase:extNanos:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999999999999L,999999999999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Testing::TestCase:queueNanos:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMDKXQBA6KZFHZH6FTKW7KHF4Z4"),
						"queueNanos",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOYUI5F4WVZFAJHRHEQHTSDURZU"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Testing::TestCase:queueNanos:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999999999999L,999999999999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Testing::TestCase:execDurationStr:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdROKBL5544NGPXNKFHAAELEX3SM"),
						"execDurationStr",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLSYXL7ZSXBF4RO23EKYZICMQU4"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Testing::TestCase:execDurationStr:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false)
			},
			new org.radixware.kernel.common.client.meta.RadCommandDef[]{
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::Testing::TestCase:Rerun-Object Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdHGOOKC3J3DNRDISQAAAAAAAAAA"),
						"Rerun",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQZFDUM24OXOBDFKUAAMPGXUWTQ"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("imgWZZPS3ZBBNFVNHFNSKTITBMHQE"),
						null,
						org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,
						true,
						false,
						false,
						org.radixware.kernel.common.enums.ECommandScope.OBJECT,
						null,
						org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_FIXED,
						true),
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::Testing::TestCase:changeGroupSettings-Object Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdTMPJ4Q6TXJAWJAN2YGZRKV2XSI"),
						"changeGroupSettings",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsT2UNPM4G5RB3LIB3OR5XY7V7O4"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("imgLKD45TOL55E2RGGU56KPXZ4CZM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("afh3GLOPZ3IO5HETKYDTNFVDIBCPI"),
						org.radixware.kernel.common.enums.ECommandNature.FORM_IN_OUT,
						true,
						false,
						false,
						org.radixware.kernel.common.enums.ECommandScope.OBJECT,
						null,
						org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_EXISTENT),
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::Testing::TestCase:Export-Object Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdOLNLKH7WMFGEPK4G5C4SIA2HQA"),
						"Export",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsD2EAAQ6WEVHT3A45EW7AVIY2W4"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("imgVORNT5ZFORBEHLSYMJNUWJK6II"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("afhBR4FWDDRQFD4FI6BHGD5LNUGZI"),
						org.radixware.kernel.common.enums.ECommandNature.FORM_IN_OUT,
						true,
						false,
						false,
						org.radixware.kernel.common.enums.ECommandScope.OBJECT,
						null,
						org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_FIXED,
						true),
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::Testing::TestCase:Import-Object Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdM5Y74G3BRFHTRGIHVZN5WY5IXA"),
						"Import",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2CDE6W3JQFCDNDLFHL3SGS4AHQ"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("imgMUCCZVMA4JC4NMTVVEGYXEXU5Q"),
						null,
						org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,
						true,
						false,
						false,
						org.radixware.kernel.common.enums.ECommandScope.OBJECT,
						null,
						org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_FIXED,
						true)
			},
			new org.radixware.kernel.common.client.meta.filters.RadFilterDef[]{

					new org.radixware.kernel.common.client.meta.filters.RadFilterDef(
					/*Radix::Testing::TestCase:By Class-Filter*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("fltBQPIEXVBZRES3ICWCEACXNIKKI"),
						"By Class",
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEWLIOPBOFJG2DFUJXWYDMP343Q"),
						"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:DbFuncCall FuncId=\"dfn3OXAHT4FYLORDBBJABIFNQAABA\"/></xsc:Item><xsc:Item><xsc:Sql>(</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblHW4OSVMS27NRDISQAAAAAAAAAA\" PropId=\"colQQJY664T27NRDISQAAAAAAAAAA\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>, </xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmA3FXJ53QVZDVTLHFCPTSUYRD2I\"/></xsc:Item><xsc:Item><xsc:Sql>)=1 or exists(\n    select </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblHW4OSVMS27NRDISQAAAAAAAAAA\" PropId=\"colN3UZ2Z4S27NRDISQAAAAAAAAAA\" Owner=\"TABLE\"/></xsc:Item><xsc:Item><xsc:Sql> \n    from </xsc:Sql></xsc:Item><xsc:Item><xsc:TableSqlName TableId=\"tblHW4OSVMS27NRDISQAAAAAAAAAA\"/></xsc:Item><xsc:Item><xsc:Sql>\n    where </xsc:Sql></xsc:Item><xsc:Item><xsc:DbFuncCall FuncId=\"dfn3OXAHT4FYLORDBBJABIFNQAABA\"/></xsc:Item><xsc:Item><xsc:Sql>(</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblHW4OSVMS27NRDISQAAAAAAAAAA\" PropId=\"colQQJY664T27NRDISQAAAAAAAAAA\" Owner=\"TABLE\"/></xsc:Item><xsc:Item><xsc:Sql>, </xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmA3FXJ53QVZDVTLHFCPTSUYRD2I\"/></xsc:Item><xsc:Item><xsc:Sql>)=1 \n    start with </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblHW4OSVMS27NRDISQAAAAAAAAAA\" PropId=\"colEZ4SH34S27NRDISQAAAAAAAAAA\" Owner=\"TABLE\"/></xsc:Item><xsc:Item><xsc:Sql>=</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecHW4OSVMS27NRDISQAAAAAAAAAA\" PropId=\"colN3UZ2Z4S27NRDISQAAAAAAAAAA\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> or </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblHW4OSVMS27NRDISQAAAAAAAAAA\" PropId=\"col3AN3AUHYHNHNFBQ3J4PRDXRRXE\" Owner=\"TABLE\"/></xsc:Item><xsc:Item><xsc:Sql>=</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecHW4OSVMS27NRDISQAAAAAAAAAA\" PropId=\"colN3UZ2Z4S27NRDISQAAAAAAAAAA\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>\n    connect by prior </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblHW4OSVMS27NRDISQAAAAAAAAAA\" PropId=\"colN3UZ2Z4S27NRDISQAAAAAAAAAA\" Owner=\"TABLE\"/></xsc:Item><xsc:Item><xsc:Sql>=</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblHW4OSVMS27NRDISQAAAAAAAAAA\" PropId=\"colEZ4SH34S27NRDISQAAAAAAAAAA\" Owner=\"TABLE\"/></xsc:Item><xsc:Item><xsc:Sql> or prior </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblHW4OSVMS27NRDISQAAAAAAAAAA\" PropId=\"colN3UZ2Z4S27NRDISQAAAAAAAAAA\" Owner=\"TABLE\"/></xsc:Item><xsc:Item><xsc:Sql>=</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblHW4OSVMS27NRDISQAAAAAAAAAA\" PropId=\"col3AN3AUHYHNHNFBQ3J4PRDXRRXE\" Owner=\"TABLE\"/></xsc:Item><xsc:Item><xsc:Sql>\n)</xsc:Sql></xsc:Item></xsc:Sqml>",
						null,
						null,
						false,
						true,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("srt34WFCNN2RREXPDEKUMG5SZUB6A"),
						new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef[]{

								new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmA3FXJ53QVZDVTLHFCPTSUYRD2I"),
								"classGuid",
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsB766TPV46NHENJOHOSAVUJ7C3A"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("tblHW4OSVMS27NRDISQAAAAAAAAAA"),
								null,
								org.radixware.kernel.common.enums.EValType.STR,
								null,
								null,
								true,
								false,
								true,
								org.radixware.kernel.common.types.Id.Factory.loadFrom("cpeWDJ5YYNVQ3OBDCKEAALOMT5GDM"),
								true,
								null,
								null,
								false,
								null,
								null)
						},

						/*Radix::Testing::TestCase:By Class:Editor Pages-Filter Pages*/
						null,
						new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
						}
						,
						null)
			},
			new org.radixware.kernel.common.client.meta.RadSortingDef[]{

					new org.radixware.kernel.common.client.meta.RadSortingDef(
					/*Radix::Testing::TestCase:By Start Time-Sorting*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("srt34WFCNN2RREXPDEKUMG5SZUB6A"),
						"By Start Time",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGYMZYCC66FAHHMX22GMCEPFNR4"),
						new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem[]{

								new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3ZQVM54K5FEL7D7RH4HIYI62Y4"),
								false),

								new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGBJZDIUS27NRDISQAAAAAAAAAA"),
								true)
						}),

					new org.radixware.kernel.common.client.meta.RadSortingDef(
					/*Radix::Testing::TestCase:By ID desc-Sorting*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("srtA7QU7WNG27NRDISQAAAAAAAAAA"),
						"By ID desc",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQVFDUM24OXOBDFKUAAMPGXUWTQ"),
						new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem[]{

								new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colN3UZ2Z4S27NRDISQAAAAAAAAAA"),
								true)
						}),

					new org.radixware.kernel.common.client.meta.RadSortingDef(
					/*Radix::Testing::TestCase:By Actor-Sorting*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("srtSMTOVB726JGMJBEJ666HYZSW6I"),
						"By Actor",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAUV36YGJI5DTFG2E5WYN54UINQ"),
						new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem[]{

								new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMWOGO4MS27NRDISQAAAAAAAAAA"),
								false)
						})
			},
			new org.radixware.kernel.common.client.meta.RadReferenceDef[]{

					new org.radixware.kernel.common.client.meta.RadReferenceDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("ref2VO3FCZ4RRBTZO2UKNRBRWALQY"),"TestCase=>TestCase (groupId=>id)",org.radixware.kernel.common.types.Id.Factory.loadFrom("tblHW4OSVMS27NRDISQAAAAAAAAAA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblHW4OSVMS27NRDISQAAAAAAAAAA"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("col3AN3AUHYHNHNFBQ3J4PRDXRRXE")},new String[]{"groupId"},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colN3UZ2Z4S27NRDISQAAAAAAAAAA")},new String[]{"id"}),

					new org.radixware.kernel.common.client.meta.RadReferenceDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("ref4INHMXMGB5HXDK4REZJPSRPH4I"),"TestCase=>User (authorName=>name)",org.radixware.kernel.common.types.Id.Factory.loadFrom("tblHW4OSVMS27NRDISQAAAAAAAAAA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblSY4KIOLTGLNRDHRZABQAQH3XQ4"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colCFZB5S5MFFAPJM2ENTFD66CEZI")},new String[]{"authorName"},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colEF6ODCYWY3NBDGMCABQAQH3XQ4")},new String[]{"name"}),

					new org.radixware.kernel.common.client.meta.RadReferenceDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("refZ3L7OMMT27NRDISQAAAAAAAAAA"),"TestCase=>TestCase (parentId=>id)",org.radixware.kernel.common.types.Id.Factory.loadFrom("tblHW4OSVMS27NRDISQAAAAAAAAAA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblHW4OSVMS27NRDISQAAAAAAAAAA"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colEZ4SH34S27NRDISQAAAAAAAAAA")},new String[]{"parentId"},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colN3UZ2Z4S27NRDISQAAAAAAAAAA")},new String[]{"id"})
			},
			new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprBMFQWKU227NRDISQAAAAAAAAAA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("epr4WBLCNMXSFEI3M2LSYDLSIK6Q4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("sprNGT5DE5G27NRDISQAAAAAAAAAA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("sprLEXXOTV4O5AEBO3VECZLSIGRCY")},
			true,false,false);
}

/* Radix::Testing::TestCase - Web Executable*/

/*Radix::Testing::TestCase-Entity Class*/

package org.radixware.ads.Testing.web;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase")
public interface TestCase {
	/*Group model adapter class. This is the base class for 
	all of selector presentation models of this entity*/
	public static class DefaultGroupModel extends org.radixware.kernel.common.client.models.GroupModel{
		public DefaultGroupModel(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadSelectorPresentationDef def){super(userSession,def);}

		/*Radix::Testing::TestCaseGroup:testId:testId-Presentation Property*/




		public class TestId extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
			public TestId(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
				super(model,def);
			}
			@Override
			public Object getValueObject(){return getValue();}
			@Override
			public void setValueObject(final Object x){
				Int dummy = ((Int)x);
				setValue(dummy);
			}












			@SuppressWarnings({"unused","unchecked"})
			@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCaseGroup:testId:testId")
			public  Int getValue() {
				return Value;
			}

			@SuppressWarnings({"unused","unchecked"})
			@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCaseGroup:testId:testId")
			public   void setValue(Int val) {
				Value = val;
			}
		}
		public TestId getTestId(){return (TestId)getProperty(pgpL4XG335VT5HJ7E7RSMVKFPF3ZM);}

		/*Radix::Testing::TestCaseGroup:testGroupId:testGroupId-Presentation Property*/




		public class TestGroupId extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
			public TestGroupId(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
				super(model,def);
			}
			@Override
			public Object getValueObject(){return getValue();}
			@Override
			public void setValueObject(final Object x){
				Int dummy = ((Int)x);
				setValue(dummy);
			}












			@SuppressWarnings({"unused","unchecked"})
			@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCaseGroup:testGroupId:testGroupId")
			public  Int getValue() {
				return Value;
			}

			@SuppressWarnings({"unused","unchecked"})
			@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCaseGroup:testGroupId:testGroupId")
			public   void setValue(Int val) {
				Value = val;
			}
		}
		public TestGroupId getTestGroupId(){return (TestGroupId)getProperty(pgpYZUQHHDC45CQJIS4PKLM73TMDQ);}

		/*Radix::Testing::TestCaseGroup:testParentId:testParentId-Presentation Property*/




		public class TestParentId extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
			public TestParentId(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
				super(model,def);
			}
			@Override
			public Object getValueObject(){return getValue();}
			@Override
			public void setValueObject(final Object x){
				Int dummy = ((Int)x);
				setValue(dummy);
			}












			@SuppressWarnings({"unused","unchecked"})
			@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCaseGroup:testParentId:testParentId")
			public  Int getValue() {
				return Value;
			}

			@SuppressWarnings({"unused","unchecked"})
			@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCaseGroup:testParentId:testParentId")
			public   void setValue(Int val) {
				Value = val;
			}
		}
		public TestParentId getTestParentId(){return (TestParentId)getProperty(pgpIZ3LCFNB45HB5FS4XPQCJ32JZA);}











		public org.radixware.ads.Testing.web.TestCase.TestCase_DefaultModel getEntity(int i) throws InterruptedException, org.radixware.kernel.common.client.exceptions.BrokenEntityObjectException, org.radixware.kernel.common.exceptions.ServiceClientException { return (org.radixware.ads.Testing.web.TestCase.TestCase_DefaultModel )  super.getEntity(i);}
	}














































































































































































































	/*Radix::Testing::TestCase:groupId:groupId-Presentation Property*/


	public class GroupId extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public GroupId(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Int dummy = ((Int)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:groupId:groupId")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:groupId:groupId")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public GroupId getGroupId();
	/*Radix::Testing::TestCase:groupRef:groupRef-Presentation Property*/


	public class GroupRef extends org.radixware.kernel.common.client.models.items.properties.PropertyRef{
		public GroupRef(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadParentRefPropertyDef def){
			super(model,def);
		}
		@Override
		public 
		org.radixware.ads.Testing.web.TestCase.TestCase_DefaultModel openEntityModel()  throws org.radixware.kernel.common.exceptions.ServiceClientException, InterruptedException{
		     return (org.radixware.ads.Testing.web.TestCase.TestCase_DefaultModel)super.openEntityModel();
		}
		@Override
		public 
		org.radixware.ads.Testing.web.TestCase.DefaultGroupModel openGroupModel(){
		     return (org.radixware.ads.Testing.web.TestCase.DefaultGroupModel)super.openGroupModel();
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.client.types.Reference dummy = ((org.radixware.kernel.common.client.types.Reference)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:groupRef:groupRef")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:groupRef:groupRef")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public GroupRef getGroupRef();
	/*Radix::Testing::TestCase:seq:seq-Presentation Property*/


	public class Seq extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public Seq(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Int dummy = ((Int)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:seq:seq")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:seq:seq")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public Seq getSeq();
	/*Radix::Testing::TestCase:resultComment:resultComment-Presentation Property*/


	public class ResultComment extends org.radixware.kernel.common.client.models.items.properties.PropertyClob{
		public ResultComment(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:resultComment:resultComment")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:resultComment:resultComment")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ResultComment getResultComment();
	/*Radix::Testing::TestCase:authorName:authorName-Presentation Property*/


	public class AuthorName extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public AuthorName(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:authorName:authorName")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:authorName:authorName")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public AuthorName getAuthorName();
	/*Radix::Testing::TestCase:parentId:parentId-Presentation Property*/


	public class ParentId extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public ParentId(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Int dummy = ((Int)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:parentId:parentId")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:parentId:parentId")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public ParentId getParentId();
	/*Radix::Testing::TestCase:extNanos:extNanos-Presentation Property*/


	public class ExtNanos extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public ExtNanos(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Int dummy = ((Int)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:extNanos:extNanos")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:extNanos:extNanos")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public ExtNanos getExtNanos();
	/*Radix::Testing::TestCase:startTime:startTime-Presentation Property*/


	public class StartTime extends org.radixware.kernel.common.client.models.items.properties.PropertyDateTime{
		public StartTime(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			java.sql.Timestamp dummy = ((java.sql.Timestamp)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:startTime:startTime")
		public  java.sql.Timestamp getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:startTime:startTime")
		public   void setValue(java.sql.Timestamp val) {
			Value = val;
		}
	}
	public StartTime getStartTime();
	/*Radix::Testing::TestCase:notes:notes-Presentation Property*/


	public class Notes extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public Notes(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:notes:notes")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:notes:notes")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Notes getNotes();
	/*Radix::Testing::TestCase:lastIsExecDate:lastIsExecDate-Presentation Property*/


	public class LastIsExecDate extends org.radixware.kernel.common.client.models.items.properties.PropertyDateTime{
		public LastIsExecDate(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			java.sql.Timestamp dummy = ((java.sql.Timestamp)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:lastIsExecDate:lastIsExecDate")
		public  java.sql.Timestamp getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:lastIsExecDate:lastIsExecDate")
		public   void setValue(java.sql.Timestamp val) {
			Value = val;
		}
	}
	public LastIsExecDate getLastIsExecDate();
	/*Radix::Testing::TestCase:extGuid:extGuid-Presentation Property*/


	public class ExtGuid extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public ExtGuid(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:extGuid:extGuid")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:extGuid:extGuid")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ExtGuid getExtGuid();
	/*Radix::Testing::TestCase:seqIsFailCount:seqIsFailCount-Presentation Property*/


	public class SeqIsFailCount extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public SeqIsFailCount(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Int dummy = ((Int)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:seqIsFailCount:seqIsFailCount")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:seqIsFailCount:seqIsFailCount")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public SeqIsFailCount getSeqIsFailCount();
	/*Radix::Testing::TestCase:author:author-Presentation Property*/


	public class Author extends org.radixware.kernel.common.client.models.items.properties.PropertyRef{
		public Author(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadParentRefPropertyDef def){
			super(model,def);
		}
		@Override
		public 
		org.radixware.ads.Acs.web.User.User_DefaultModel openEntityModel()  throws org.radixware.kernel.common.exceptions.ServiceClientException, InterruptedException{
		     return (org.radixware.ads.Acs.web.User.User_DefaultModel)super.openEntityModel();
		}
		@Override
		public 
		org.radixware.ads.Acs.web.User.DefaultGroupModel openGroupModel(){
		     return (org.radixware.ads.Acs.web.User.DefaultGroupModel)super.openGroupModel();
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.client.types.Reference dummy = ((org.radixware.kernel.common.client.types.Reference)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:author:author")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:author:author")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public Author getAuthor();
	/*Radix::Testing::TestCase:cpuNanos:cpuNanos-Presentation Property*/


	public class CpuNanos extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public CpuNanos(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Int dummy = ((Int)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:cpuNanos:cpuNanos")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:cpuNanos:cpuNanos")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public CpuNanos getCpuNanos();
	/*Radix::Testing::TestCase:parent:parent-Presentation Property*/


	public class Parent extends org.radixware.kernel.common.client.models.items.properties.PropertyRef{
		public Parent(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadParentRefPropertyDef def){
			super(model,def);
		}
		@Override
		public 
		org.radixware.ads.Testing.web.TestCase.TestCase_DefaultModel openEntityModel()  throws org.radixware.kernel.common.exceptions.ServiceClientException, InterruptedException{
		     return (org.radixware.ads.Testing.web.TestCase.TestCase_DefaultModel)super.openEntityModel();
		}
		@Override
		public 
		org.radixware.ads.Testing.web.TestCase.DefaultGroupModel openGroupModel(){
		     return (org.radixware.ads.Testing.web.TestCase.DefaultGroupModel)super.openGroupModel();
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.client.types.Reference dummy = ((org.radixware.kernel.common.client.types.Reference)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:parent:parent")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:parent:parent")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public Parent getParent();
	/*Radix::Testing::TestCase:queueNanos:queueNanos-Presentation Property*/


	public class QueueNanos extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public QueueNanos(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Int dummy = ((Int)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:queueNanos:queueNanos")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:queueNanos:queueNanos")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public QueueNanos getQueueNanos();
	/*Radix::Testing::TestCase:actor:actor-Presentation Property*/


	public class Actor extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public Actor(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:actor:actor")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:actor:actor")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Actor getActor();
	/*Radix::Testing::TestCase:id:id-Presentation Property*/


	public class Id extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public Id(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Int dummy = ((Int)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:id:id")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:id:id")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public Id getId();
	/*Radix::Testing::TestCase:lastIsSuccessDate:lastIsSuccessDate-Presentation Property*/


	public class LastIsSuccessDate extends org.radixware.kernel.common.client.models.items.properties.PropertyDateTime{
		public LastIsSuccessDate(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			java.sql.Timestamp dummy = ((java.sql.Timestamp)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:lastIsSuccessDate:lastIsSuccessDate")
		public  java.sql.Timestamp getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:lastIsSuccessDate:lastIsSuccessDate")
		public   void setValue(java.sql.Timestamp val) {
			Value = val;
		}
	}
	public LastIsSuccessDate getLastIsSuccessDate();
	/*Radix::Testing::TestCase:traceProfile:traceProfile-Presentation Property*/


	public class TraceProfile extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public TraceProfile(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:traceProfile:traceProfile")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:traceProfile:traceProfile")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public TraceProfile getTraceProfile();
	/*Radix::Testing::TestCase:finishTime:finishTime-Presentation Property*/


	public class FinishTime extends org.radixware.kernel.common.client.models.items.properties.PropertyDateTime{
		public FinishTime(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			java.sql.Timestamp dummy = ((java.sql.Timestamp)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:finishTime:finishTime")
		public  java.sql.Timestamp getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:finishTime:finishTime")
		public   void setValue(java.sql.Timestamp val) {
			Value = val;
		}
	}
	public FinishTime getFinishTime();
	/*Radix::Testing::TestCase:active:active-Presentation Property*/


	public class Active extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public Active(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:active:active")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:active:active")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public Active getActive();
	/*Radix::Testing::TestCase:notificationEmail:notificationEmail-Presentation Property*/


	public class NotificationEmail extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public NotificationEmail(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:notificationEmail:notificationEmail")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:notificationEmail:notificationEmail")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public NotificationEmail getNotificationEmail();
	/*Radix::Testing::TestCase:dbNanos:dbNanos-Presentation Property*/


	public class DbNanos extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public DbNanos(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Int dummy = ((Int)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:dbNanos:dbNanos")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:dbNanos:dbNanos")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public DbNanos getDbNanos();
	/*Radix::Testing::TestCase:result:result-Presentation Property*/


	public class Result extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public Result(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:result:result")
		public  org.radixware.kernel.common.enums.EEventSeverity getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:result:result")
		public   void setValue(org.radixware.kernel.common.enums.EEventSeverity val) {
			Value = val;
		}
	}
	public Result getResult();
	/*Radix::Testing::TestCase:pathInTree:pathInTree-Presentation Property*/


	public class PathInTree extends org.radixware.kernel.common.client.models.items.properties.PropertyArrStr{
		public PathInTree(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.types.ArrStr dummy = ((org.radixware.kernel.common.types.ArrStr)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:pathInTree:pathInTree")
		public  org.radixware.kernel.common.types.ArrStr getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:pathInTree:pathInTree")
		public   void setValue(org.radixware.kernel.common.types.ArrStr val) {
			Value = val;
		}
	}
	public PathInTree getPathInTree();
	/*Radix::Testing::TestCase:canHaveChilds:canHaveChilds-Presentation Property*/


	public class CanHaveChilds extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public CanHaveChilds(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:canHaveChilds:canHaveChilds")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:canHaveChilds:canHaveChilds")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public CanHaveChilds getCanHaveChilds();
	/*Radix::Testing::TestCase:isParent:isParent-Presentation Property*/


	public class IsParent extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public IsParent(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:isParent:isParent")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:isParent:isParent")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public IsParent getIsParent();
	/*Radix::Testing::TestCase:thereIsChilds:thereIsChilds-Presentation Property*/


	public class ThereIsChilds extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public ThereIsChilds(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:thereIsChilds:thereIsChilds")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:thereIsChilds:thereIsChilds")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public ThereIsChilds getThereIsChilds();
	/*Radix::Testing::TestCase:isGroup:isGroup-Presentation Property*/


	public class IsGroup extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public IsGroup(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:isGroup:isGroup")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:isGroup:isGroup")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public IsGroup getIsGroup();
	/*Radix::Testing::TestCase:execDurationStr:execDurationStr-Presentation Property*/


	public class ExecDurationStr extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public ExecDurationStr(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:execDurationStr:execDurationStr")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:execDurationStr:execDurationStr")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ExecDurationStr getExecDurationStr();
	/*Radix::Testing::TestCase:failEventSeverity:failEventSeverity-Presentation Property*/


	public class FailEventSeverity extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public FailEventSeverity(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:failEventSeverity:failEventSeverity")
		public  org.radixware.kernel.common.enums.EEventSeverity getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:failEventSeverity:failEventSeverity")
		public   void setValue(org.radixware.kernel.common.enums.EEventSeverity val) {
			Value = val;
		}
	}
	public FailEventSeverity getFailEventSeverity();
	/*Radix::Testing::TestCase:seqTitle:seqTitle-Presentation Property*/


	public class SeqTitle extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public SeqTitle(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:seqTitle:seqTitle")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:seqTitle:seqTitle")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public SeqTitle getSeqTitle();
	/*Radix::Testing::TestCase:classTitle:classTitle-Presentation Property*/


	public class ClassTitle extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public ClassTitle(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:classTitle:classTitle")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:classTitle:classTitle")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ClassTitle getClassTitle();
	public static class Rerun extends org.radixware.kernel.common.client.models.items.Command{
		protected Rerun(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public void send() throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,null);
			processResponse(response, null);
		}

	}

	public static class Import extends org.radixware.kernel.common.client.models.items.Command{
		protected Import(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public org.radixware.schemas.types.StrDocument send(org.radixware.ads.Testing.common.ImpExpXsd.TestCaseDocument input ) throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,input);
			org.apache.xmlbeans.XmlObject xmlResponse = processResponse(response, null);
			return (org.radixware.schemas.types.StrDocument)org.radixware.kernel.common.utils.XmlObjectProcessor.castToXmlClass(getClass().getClassLoader(),xmlResponse,org.radixware.schemas.types.StrDocument.class);
		}

	}

	public static class Export extends org.radixware.kernel.common.client.models.items.Command{
		protected Export(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}

	}

	public static class ChangeGroupSettings extends org.radixware.kernel.common.client.models.items.Command{
		protected ChangeGroupSettings(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}

	}



}

/* Radix::Testing::TestCase - Web Meta*/

/*Radix::Testing::TestCase-Entity Class*/

package org.radixware.ads.Testing.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class TestCase_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::Testing::TestCase:Presentations-Entity Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),
			"Radix::Testing::TestCase",
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("imgBP4452EV3NADFBNW75KWBB46V4"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("sprNGT5DE5G27NRDISQAAAAAAAAAA"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBJFDUM24OXOBDFKUAAMPGXUWTQ"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBNFDUM24OXOBDFKUAAMPGXUWTQ"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBJFDUM24OXOBDFKUAAMPGXUWTQ"),8192,

			/*Radix::Testing::TestCase:Properties-Properties*/
			new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

					/*Radix::Testing::TestCase:resultComment:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colC46NAASN3PNRDISQAAAAAAAAAA"),
						"resultComment",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOBFDUM24OXOBDFKUAAMPGXUWTQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),
						org.radixware.kernel.common.enums.EValType.CLOB,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Testing::TestCase:resultComment:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						true,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Testing::TestCase:parentId:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEZ4SH34S27NRDISQAAAAAAAAAA"),
						"parentId",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsN5FDUM24OXOBDFKUAAMPGXUWTQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Testing::TestCase:parentId:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Testing::TestCase:startTime:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGBJZDIUS27NRDISQAAAAAAAAAA"),
						"startTime",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNZFDUM24OXOBDFKUAAMPGXUWTQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),
						org.radixware.kernel.common.enums.EValType.DATE_TIME,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Testing::TestCase:startTime:PropertyPresentation:Edit Options:-Edit Mask Date Time*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskDateTime("dd.MM.yyyy HH:mm:ss.zzz",null,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Testing::TestCase:notes:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHWP3QCET27NRDISQAAAAAAAAAA"),
						"notes",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNVFDUM24OXOBDFKUAAMPGXUWTQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Testing::TestCase:notes:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,true),
						null,
						null,
						null,
						true,-1,-1,1,
						true,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Testing::TestCase:parent:PropertyPresentation-Parent Ref Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colM7PLLGVJ27NRDISQAAAAAAAAAA"),
						"parent",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),
						org.radixware.kernel.common.enums.EValType.PARENT_REF,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						20,
						null,
						null,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.PROGRAMMATICALLY,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,
						null,
						null,
						null,
						null,
						true,-1,-1,1,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblHW4OSVMS27NRDISQAAAAAAAAAA"),
						null,
						null,
						null,
						133693439,
						133693439,false),

					/*Radix::Testing::TestCase:actor:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMWOGO4MS27NRDISQAAAAAAAAAA"),
						"actor",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNRFDUM24OXOBDFKUAAMPGXUWTQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Testing::TestCase:actor:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,250,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Testing::TestCase:id:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colN3UZ2Z4S27NRDISQAAAAAAAAAA"),
						"id",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNNFDUM24OXOBDFKUAAMPGXUWTQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::Testing::TestCase:id:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Testing::TestCase:traceProfile:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPMEM3RSM3PNRDISQAAAAAAAAAA"),
						"traceProfile",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNJFDUM24OXOBDFKUAAMPGXUWTQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						28,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("Debug"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						true,
						true,
						null,
						false,

						/*Radix::Testing::TestCase:traceProfile:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Testing::TestCase:finishTime:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQKCERAMS27NRDISQAAAAAAAAAA"),
						"finishTime",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNFFDUM24OXOBDFKUAAMPGXUWTQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),
						org.radixware.kernel.common.enums.EValType.DATE_TIME,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Testing::TestCase:finishTime:PropertyPresentation:Edit Options:-Edit Mask Date Time*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskDateTime("dd.MM.yyyy HH:mm:ss.zzz",null,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Testing::TestCase:result:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colYWL4NXUS27NRDISQAAAAAAAAAA"),
						"result",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNBFDUM24OXOBDFKUAAMPGXUWTQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acsFRO5RTQOG7NRDJH2ACQMTAIZT4"),
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Testing::TestCase:result:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acsFRO5RTQOG7NRDJH2ACQMTAIZT4"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.NONE,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Testing::TestCase:active:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQP4LKSN3IFHITPAQXU7J72H6FA"),
						"active",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsS6CNAK6UGVHUZOK2UKXFMVTW6Q"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),
						org.radixware.kernel.common.enums.EValType.BOOL,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("1"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::Testing::TestCase:active:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Testing::TestCase:groupId:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3AN3AUHYHNHNFBQ3J4PRDXRRXE"),
						"groupId",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Testing::TestCase:groupId:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Testing::TestCase:groupRef:PropertyPresentation-Parent Ref Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3T77RRLFUBHDLA7X4GCO27SCOU"),
						"groupRef",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXUGTSHLNLZAZ5BUI7QF47DGFXE"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),
						org.radixware.kernel.common.enums.EValType.PARENT_REF,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						20,
						null,
						null,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.PROGRAMMATICALLY,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,
						null,
						null,
						null,
						null,
						true,-1,-1,1,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblHW4OSVMS27NRDISQAAAAAAAAAA"),
						null,
						null,
						null,
						133693439,
						133693439,false),

					/*Radix::Testing::TestCase:extGuid:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJGTICYXJ35G5TJ26LFHQAX2QRY"),
						"extGuid",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::Testing::TestCase:extGuid:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,100,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Testing::TestCase:seq:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3ZQVM54K5FEL7D7RH4HIYI62Y4"),
						"seq",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsC6YFHCMLMRGT3II3DDLRF6YCI4"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("0"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::Testing::TestCase:seq:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Testing::TestCase:seqTitle:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdYRLSFFVV2ZFIJFFAT34VLBDB4Y"),
						"seqTitle",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMNQIG6HQIRARFD224IAWTHH6BQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Testing::TestCase:seqTitle:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Testing::TestCase:classTitle:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdZSHM4TXE3XNRDISQAAAAAAAAAA"),
						"classTitle",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEMSKREJXYRBARPN3PY5NLBLPC4"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
						0,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Testing::TestCase:classTitle:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Testing::TestCase:failEventSeverity:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdV7KTVRF6PJBPLHTS5N72A7W7PU"),
						"failEventSeverity",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
						4,
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acsFRO5RTQOG7NRDJH2ACQMTAIZT4"),
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("2"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Testing::TestCase:failEventSeverity:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acsFRO5RTQOG7NRDJH2ACQMTAIZT4"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.NONE,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Testing::TestCase:isGroup:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdR7AAOIBTTNDT5JUMOATGCT22JQ"),
						"isGroup",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),
						org.radixware.kernel.common.enums.EValType.BOOL,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Testing::TestCase:isGroup:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Testing::TestCase:thereIsChilds:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdQO2OHKBQTNB2LK2NR3TUUWDQWQ"),
						"thereIsChilds",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),
						org.radixware.kernel.common.enums.EValType.BOOL,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Testing::TestCase:thereIsChilds:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Testing::TestCase:canHaveChilds:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd4CDENELMOJFFVP5WDQGC72CQRA"),
						"canHaveChilds",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),
						org.radixware.kernel.common.enums.EValType.BOOL,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Testing::TestCase:canHaveChilds:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Testing::TestCase:isParent:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdGDMZ3TJQRFHTZLUGK5MYZ5LUOA"),
						"isParent",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),
						org.radixware.kernel.common.enums.EValType.BOOL,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Testing::TestCase:isParent:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Testing::TestCase:pathInTree:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd27YHYAOF2RAKXOKNUKIM53FSMU"),
						"pathInTree",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),
						org.radixware.kernel.common.enums.EValType.ARR_STR,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Testing::TestCase:pathInTree:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Testing::TestCase:authorName:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colCFZB5S5MFFAPJM2ENTFD66CEZI"),
						"authorName",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsESLW3G2YARA4HJQG6XW5E54LQE"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Testing::TestCase:authorName:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,250,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Testing::TestCase:author:PropertyPresentation-Parent Ref Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colL4QQIGK7MFGF5FCW7HHEA4YIZI"),
						"author",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBDSC6QRKOZF33C2U7PFDJLEPHM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),
						org.radixware.kernel.common.enums.EValType.PARENT_REF,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						20,
						null,
						null,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,
						null,
						null,
						null,
						null,
						true,-1,-1,1,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblSY4KIOLTGLNRDHRZABQAQH3XQ4"),
						null,
						null,
						null,
						133693439,
						133693439,false),

					/*Radix::Testing::TestCase:lastIsExecDate:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colI5ELDBK7XVDQDA626NCPOMNAXM"),
						"lastIsExecDate",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWGQI2LNWCRCZBBTIBBE3LTDVAE"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),
						org.radixware.kernel.common.enums.EValType.DATE_TIME,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Testing::TestCase:lastIsExecDate:PropertyPresentation:Edit Options:-Edit Mask Date Time*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskDateTime("dd.MM.yyyy HH:mm:ss",null,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Testing::TestCase:lastIsSuccessDate:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPI3FLY6XA5DV3LZKE3RO4PQFFQ"),
						"lastIsSuccessDate",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2QR2LMMP4VEDZFCZQSCZRZEC2M"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),
						org.radixware.kernel.common.enums.EValType.DATE_TIME,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Testing::TestCase:lastIsSuccessDate:PropertyPresentation:Edit Options:-Edit Mask Date Time*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskDateTime("dd.MM.yyyy HH:mm:ss",null,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Testing::TestCase:notificationEmail:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colUSPDDDJLZ5AINICL3FIA4KD7GU"),
						"notificationEmail",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVKBIY5VZYZC2TOUJDYOKIVVTXA"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Testing::TestCase:notificationEmail:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,100,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Testing::TestCase:seqIsFailCount:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKTWEBHYK2JGENOQY3SHZ27BQ6I"),
						"seqIsFailCount",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXMNVUVSRWVHARIGIRJC2Q5RRCY"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("0"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Testing::TestCase:seqIsFailCount:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Testing::TestCase:cpuNanos:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colLLFER2QMUFH5BME327Q4IPNJNM"),
						"cpuNanos",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHZCZSIIAXVEJTN2KFUA2GNBPB4"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Testing::TestCase:cpuNanos:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999999999999L,999999999999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Testing::TestCase:dbNanos:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colYH7NIOG57JEHPD7QO4GMHI7K54"),
						"dbNanos",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsSZXNAOXR6BFFTLS6IOEVLYYT7E"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Testing::TestCase:dbNanos:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999999999999L,999999999999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Testing::TestCase:extNanos:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colG4EPHGTTE5BM3DLJN55VNIVOBE"),
						"extNanos",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXK62QTDRFZBABPMJQ2NYOEIV5E"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Testing::TestCase:extNanos:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999999999999L,999999999999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Testing::TestCase:queueNanos:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMDKXQBA6KZFHZH6FTKW7KHF4Z4"),
						"queueNanos",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOYUI5F4WVZFAJHRHEQHTSDURZU"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Testing::TestCase:queueNanos:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999999999999L,999999999999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Testing::TestCase:execDurationStr:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdROKBL5544NGPXNKFHAAELEX3SM"),
						"execDurationStr",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLSYXL7ZSXBF4RO23EKYZICMQU4"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Testing::TestCase:execDurationStr:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false)
			},
			new org.radixware.kernel.common.client.meta.RadCommandDef[]{
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::Testing::TestCase:Rerun-Object Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdHGOOKC3J3DNRDISQAAAAAAAAAA"),
						"Rerun",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQZFDUM24OXOBDFKUAAMPGXUWTQ"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("imgWZZPS3ZBBNFVNHFNSKTITBMHQE"),
						null,
						org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,
						true,
						false,
						false,
						org.radixware.kernel.common.enums.ECommandScope.OBJECT,
						null,
						org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_FIXED,
						true),
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::Testing::TestCase:changeGroupSettings-Object Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdTMPJ4Q6TXJAWJAN2YGZRKV2XSI"),
						"changeGroupSettings",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsT2UNPM4G5RB3LIB3OR5XY7V7O4"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("imgLKD45TOL55E2RGGU56KPXZ4CZM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("afh3GLOPZ3IO5HETKYDTNFVDIBCPI"),
						org.radixware.kernel.common.enums.ECommandNature.FORM_IN_OUT,
						true,
						false,
						false,
						org.radixware.kernel.common.enums.ECommandScope.OBJECT,
						null,
						org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_EXISTENT),
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::Testing::TestCase:Import-Object Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdM5Y74G3BRFHTRGIHVZN5WY5IXA"),
						"Import",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2CDE6W3JQFCDNDLFHL3SGS4AHQ"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("imgMUCCZVMA4JC4NMTVVEGYXEXU5Q"),
						null,
						org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,
						true,
						false,
						false,
						org.radixware.kernel.common.enums.ECommandScope.OBJECT,
						null,
						org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_FIXED,
						true)
			},
			new org.radixware.kernel.common.client.meta.filters.RadFilterDef[]{

					new org.radixware.kernel.common.client.meta.filters.RadFilterDef(
					/*Radix::Testing::TestCase:By Class-Filter*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("fltBQPIEXVBZRES3ICWCEACXNIKKI"),
						"By Class",
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEWLIOPBOFJG2DFUJXWYDMP343Q"),
						"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:DbFuncCall FuncId=\"dfn3OXAHT4FYLORDBBJABIFNQAABA\"/></xsc:Item><xsc:Item><xsc:Sql>(</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblHW4OSVMS27NRDISQAAAAAAAAAA\" PropId=\"colQQJY664T27NRDISQAAAAAAAAAA\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>, </xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmA3FXJ53QVZDVTLHFCPTSUYRD2I\"/></xsc:Item><xsc:Item><xsc:Sql>)=1 or exists(\n    select </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblHW4OSVMS27NRDISQAAAAAAAAAA\" PropId=\"colN3UZ2Z4S27NRDISQAAAAAAAAAA\" Owner=\"TABLE\"/></xsc:Item><xsc:Item><xsc:Sql> \n    from </xsc:Sql></xsc:Item><xsc:Item><xsc:TableSqlName TableId=\"tblHW4OSVMS27NRDISQAAAAAAAAAA\"/></xsc:Item><xsc:Item><xsc:Sql>\n    where </xsc:Sql></xsc:Item><xsc:Item><xsc:DbFuncCall FuncId=\"dfn3OXAHT4FYLORDBBJABIFNQAABA\"/></xsc:Item><xsc:Item><xsc:Sql>(</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblHW4OSVMS27NRDISQAAAAAAAAAA\" PropId=\"colQQJY664T27NRDISQAAAAAAAAAA\" Owner=\"TABLE\"/></xsc:Item><xsc:Item><xsc:Sql>, </xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmA3FXJ53QVZDVTLHFCPTSUYRD2I\"/></xsc:Item><xsc:Item><xsc:Sql>)=1 \n    start with </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblHW4OSVMS27NRDISQAAAAAAAAAA\" PropId=\"colEZ4SH34S27NRDISQAAAAAAAAAA\" Owner=\"TABLE\"/></xsc:Item><xsc:Item><xsc:Sql>=</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecHW4OSVMS27NRDISQAAAAAAAAAA\" PropId=\"colN3UZ2Z4S27NRDISQAAAAAAAAAA\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> or </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblHW4OSVMS27NRDISQAAAAAAAAAA\" PropId=\"col3AN3AUHYHNHNFBQ3J4PRDXRRXE\" Owner=\"TABLE\"/></xsc:Item><xsc:Item><xsc:Sql>=</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecHW4OSVMS27NRDISQAAAAAAAAAA\" PropId=\"colN3UZ2Z4S27NRDISQAAAAAAAAAA\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>\n    connect by prior </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblHW4OSVMS27NRDISQAAAAAAAAAA\" PropId=\"colN3UZ2Z4S27NRDISQAAAAAAAAAA\" Owner=\"TABLE\"/></xsc:Item><xsc:Item><xsc:Sql>=</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblHW4OSVMS27NRDISQAAAAAAAAAA\" PropId=\"colEZ4SH34S27NRDISQAAAAAAAAAA\" Owner=\"TABLE\"/></xsc:Item><xsc:Item><xsc:Sql> or prior </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblHW4OSVMS27NRDISQAAAAAAAAAA\" PropId=\"colN3UZ2Z4S27NRDISQAAAAAAAAAA\" Owner=\"TABLE\"/></xsc:Item><xsc:Item><xsc:Sql>=</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblHW4OSVMS27NRDISQAAAAAAAAAA\" PropId=\"col3AN3AUHYHNHNFBQ3J4PRDXRRXE\" Owner=\"TABLE\"/></xsc:Item><xsc:Item><xsc:Sql>\n)</xsc:Sql></xsc:Item></xsc:Sqml>",
						null,
						null,
						false,
						true,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("srt34WFCNN2RREXPDEKUMG5SZUB6A"),
						new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef[]{

								new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmA3FXJ53QVZDVTLHFCPTSUYRD2I"),
								"classGuid",
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsB766TPV46NHENJOHOSAVUJ7C3A"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("tblHW4OSVMS27NRDISQAAAAAAAAAA"),
								null,
								org.radixware.kernel.common.enums.EValType.STR,
								null,
								null,
								true,
								false,
								true,
								null,
								true,
								null,
								null,
								false,
								null,
								null)
						},

						/*Radix::Testing::TestCase:By Class:Editor Pages-Filter Pages*/
						null,
						new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
						}
						,
						null)
			},
			new org.radixware.kernel.common.client.meta.RadSortingDef[]{

					new org.radixware.kernel.common.client.meta.RadSortingDef(
					/*Radix::Testing::TestCase:By Start Time-Sorting*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("srt34WFCNN2RREXPDEKUMG5SZUB6A"),
						"By Start Time",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGYMZYCC66FAHHMX22GMCEPFNR4"),
						new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem[]{

								new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3ZQVM54K5FEL7D7RH4HIYI62Y4"),
								false),

								new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGBJZDIUS27NRDISQAAAAAAAAAA"),
								true)
						}),

					new org.radixware.kernel.common.client.meta.RadSortingDef(
					/*Radix::Testing::TestCase:By ID desc-Sorting*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("srtA7QU7WNG27NRDISQAAAAAAAAAA"),
						"By ID desc",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQVFDUM24OXOBDFKUAAMPGXUWTQ"),
						new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem[]{

								new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colN3UZ2Z4S27NRDISQAAAAAAAAAA"),
								true)
						}),

					new org.radixware.kernel.common.client.meta.RadSortingDef(
					/*Radix::Testing::TestCase:By Actor-Sorting*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("srtSMTOVB726JGMJBEJ666HYZSW6I"),
						"By Actor",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAUV36YGJI5DTFG2E5WYN54UINQ"),
						new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem[]{

								new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMWOGO4MS27NRDISQAAAAAAAAAA"),
								false)
						})
			},
			new org.radixware.kernel.common.client.meta.RadReferenceDef[]{

					new org.radixware.kernel.common.client.meta.RadReferenceDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("ref2VO3FCZ4RRBTZO2UKNRBRWALQY"),"TestCase=>TestCase (groupId=>id)",org.radixware.kernel.common.types.Id.Factory.loadFrom("tblHW4OSVMS27NRDISQAAAAAAAAAA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblHW4OSVMS27NRDISQAAAAAAAAAA"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("col3AN3AUHYHNHNFBQ3J4PRDXRRXE")},new String[]{"groupId"},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colN3UZ2Z4S27NRDISQAAAAAAAAAA")},new String[]{"id"}),

					new org.radixware.kernel.common.client.meta.RadReferenceDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("ref4INHMXMGB5HXDK4REZJPSRPH4I"),"TestCase=>User (authorName=>name)",org.radixware.kernel.common.types.Id.Factory.loadFrom("tblHW4OSVMS27NRDISQAAAAAAAAAA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblSY4KIOLTGLNRDHRZABQAQH3XQ4"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colCFZB5S5MFFAPJM2ENTFD66CEZI")},new String[]{"authorName"},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colEF6ODCYWY3NBDGMCABQAQH3XQ4")},new String[]{"name"}),

					new org.radixware.kernel.common.client.meta.RadReferenceDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("refZ3L7OMMT27NRDISQAAAAAAAAAA"),"TestCase=>TestCase (parentId=>id)",org.radixware.kernel.common.types.Id.Factory.loadFrom("tblHW4OSVMS27NRDISQAAAAAAAAAA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblHW4OSVMS27NRDISQAAAAAAAAAA"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colEZ4SH34S27NRDISQAAAAAAAAAA")},new String[]{"parentId"},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colN3UZ2Z4S27NRDISQAAAAAAAAAA")},new String[]{"id"})
			},
			new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr4WBLCNMXSFEI3M2LSYDLSIK6Q4")},
			true,false,false);
}

/* Radix::Testing::TestCase:General - Desktop Meta*/

/*Radix::Testing::TestCase:General-Editor Presentation*/

package org.radixware.ads.Testing.explorer;
public final class General_mi{
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new org.radixware.kernel.common.client.meta.RadEditorPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprBMFQWKU227NRDISQAAAAAAAAAA"),
	"General",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
	null,
	org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("tblHW4OSVMS27NRDISQAAAAAAAAAA"),
	null,
	null,

	/*Radix::Testing::TestCase:General:Editor Pages-Editor Presentation Pages*/
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

			/*Radix::Testing::TestCase:General:General-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epg5F62VEGTBXOBDNTPAAMPGXSZKU"),"General",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPFFDUM24OXOBDFKUAAMPGXUWTQ"),null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colN3UZ2Z4S27NRDISQAAAAAAAAAA"),0,0,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdZSHM4TXE3XNRDISQAAAAAAAAAA"),0,2,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPMEM3RSM3PNRDISQAAAAAAAAAA"),0,3,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMWOGO4MS27NRDISQAAAAAAAAAA"),0,5,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGBJZDIUS27NRDISQAAAAAAAAAA"),0,7,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQKCERAMS27NRDISQAAAAAAAAAA"),0,8,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colYWL4NXUS27NRDISQAAAAAAAAAA"),0,9,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colC46NAASN3PNRDISQAAAAAAAAAA"),0,10,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHWP3QCET27NRDISQAAAAAAAAAA"),0,6,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3ZQVM54K5FEL7D7RH4HIYI62Y4"),0,1,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colL4QQIGK7MFGF5FCW7HHEA4YIZI"),0,4,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("ppgGS5UCEHPSJF2PBXTBWHVEEIP4I"),0,13,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdROKBL5544NGPXNKFHAAELEX3SM"),0,11,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQP4LKSN3IFHITPAQXU7J72H6FA"),0,12,1,false,false)
			},new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PropertiesGroup[]{

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PropertiesGroup(org.radixware.kernel.common.types.Id.Factory.loadFrom("ppgGS5UCEHPSJF2PBXTBWHVEEIP4I"),"IS props",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsB5R27FFIFJHNTMVZVYMJHRFETE"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),false,true,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colUSPDDDJLZ5AINICL3FIA4KD7GU"),0,0,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colI5ELDBK7XVDQDA626NCPOMNAXM"),0,2,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPI3FLY6XA5DV3LZKE3RO4PQFFQ"),0,3,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKTWEBHYK2JGENOQY3SHZ27BQ6I"),0,1,1,false,false)
					})
			}),

			/*Radix::Testing::TestCase:General:Parameters-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgW337T5LHLTOBDCJFAALOMT5GDM"),"Parameters",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPJFDUM24OXOBDFKUAAMPGXUWTQ"),null,null,null),

			/*Radix::Testing::TestCase:General:Events-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadContainerEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epg7BIMYSOU2JFUHJFWJJGELYTFZA"),"Events",null,null,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("xpi3VAI7YDSGZAL3NQC42JXD34APY"))
	},
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epg5F62VEGTBXOBDNTPAAMPGXSZKU")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgW337T5LHLTOBDCJFAALOMT5GDM")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epg7BIMYSOU2JFUHJFWJJGELYTFZA"))}
	,

	/*Radix::Testing::TestCase:General:Children-Explorer Items*/
		new org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemDef[]{

				/*Radix::Testing::TestCase:General:EventLog-Entity Explorer Item*/

				new org.radixware.kernel.common.client.meta.explorerItems.RadSelectorExplorerItemDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("xpi3VAI7YDSGZAL3NQC42JXD34APY"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),null,
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("sprKQ5WOSDJZ5AQFOGPY4PK3O2XTA"),
					0,
					null,
					16560,true),

				/*Radix::Testing::TestCase:General:Children-Entity Explorer Item*/

				new org.radixware.kernel.common.client.meta.explorerItems.RadSelectorExplorerItemDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiIHA74FUWNZB4JHZPOT62ZLCW54"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),null,
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("sprNGT5DE5G27NRDISQAAAAAAAAAA"),
					0,
					null,
					176,true)
		}
	, new 
	org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[]{new org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprBMFQWKU227NRDISQAAAAAAAAAA"),
			new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("xpi3VAI7YDSGZAL3NQC42JXD34APY")},null)}
	,
	null,
	0,
	null,
	(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
	2192,0,0,null);
}
/* Radix::Testing::TestCase:General:Model - Desktop Executable*/

/*Radix::Testing::TestCase:General:Model-Entity Model Class*/

package org.radixware.ads.Testing.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:General:Model")
public class General:Model  extends org.radixware.ads.Testing.explorer.TestCase.TestCase_DefaultModel implements org.radixware.ads.System.common_client.IEventContextProvider  {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return General:Model_mi.rdxMeta; }

	final Explorer.EditMask::EditMaskInt maskDuration = new EditMaskInt(null, null, (byte) 0, null, 1l, Meta::TriadDelimeterType:DEFAULT, null, (byte) 10);

	public General:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadEditorPresentationDef def){super(userSession,def);}

	/*Radix::Testing::TestCase:General:Model:Nested classes-Nested Classes*/

	/*Radix::Testing::TestCase:General:Model:ChangeGroupSettingsCommand-Common Dynamic Class*/


	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:General:Model:ChangeGroupSettingsCommand")
	public class ChangeGroupSettingsCommand  extends org.radixware.kernel.common.client.models.items.Command  {



		/*Radix::Testing::TestCase:General:Model:ChangeGroupSettingsCommand:Nested classes-Nested Classes*/

		/*Radix::Testing::TestCase:General:Model:ChangeGroupSettingsCommand:Properties-Properties*/

		/*Radix::Testing::TestCase:General:Model:ChangeGroupSettingsCommand:Methods-Methods*/

		/*Radix::Testing::TestCase:General:Model:ChangeGroupSettingsCommand:ChangeGroupSettingsCommand-User Method*/

		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:General:Model:ChangeGroupSettingsCommand:ChangeGroupSettingsCommand")
		public  ChangeGroupSettingsCommand (org.radixware.kernel.common.client.models.Model model, org.radixware.kernel.common.client.meta.RadCommandDef command) {
			super(model,command);
		}

		/*Radix::Testing::TestCase:General:Model:ChangeGroupSettingsCommand:afterExecute-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:General:Model:ChangeGroupSettingsCommand:afterExecute")
		protected published  void afterExecute (org.radixware.kernel.common.client.models.FormModel previousForm, org.radixware.kernel.common.client.models.FormModel nextForm, org.radixware.kernel.common.types.Id propertyId, org.apache.xmlbeans.XmlObject response) {
			//super.(previousForm, nextForm, propertyId, response);
			if (previousForm instanceof ChangeGroupSettings:Model) {
			    
			    //Dirty hack:
			    //we don't want test case be modified, so
			    //remember pathInTree and return previous value (null) to property PathInTree
			    ArrStr pathInTree = TestCase:General:Model.this.pathInTree.Value;
			    TestCase:General:Model.this.pathInTree.Value = null;
			    
			    final Explorer.Models::GroupModel parentGroupModel;
			    if (getEntityContext() instanceof Explorer.Context::InSelectorEditingContext) {
			        parentGroupModel = ((Explorer.Context::InSelectorEditingContext) getEntityContext()).getGroupModel();
			    } else {
			        parentGroupModel = ((Explorer.Context::SelectorRowContext) getEntityContext()).parentGroupModel;
			    }
			    if (parentGroupModel instanceof TestCase:General:Model) {
			        try {
			            ((TestCase:General:Model) parentGroupModel).afterUpdateTestGroupSettings(TestCase:General:Model.this,
			                    pathInTree);
			        } catch (Throwable e) {
			            getEnvironment().getTracer().error(e);
			        }
			    }

			}
		}


	}

	/*Radix::Testing::TestCase:General:Model:Properties-Properties*/

	/*Radix::Testing::TestCase:General:Model:groupRef-Presentation Property*/




	public class GroupRef extends org.radixware.ads.Testing.explorer.TestCase.col3T77RRLFUBHDLA7X4GCO27SCOU{
		public GroupRef(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadParentRefPropertyDef def){
			super(model,def);
		}
		@Override
		public 
		org.radixware.ads.Testing.explorer.TestCase.TestCase_DefaultModel openEntityModel()  throws org.radixware.kernel.common.exceptions.ServiceClientException, InterruptedException{
		     return (org.radixware.ads.Testing.explorer.TestCase.TestCase_DefaultModel)super.openEntityModel();
		}
		@Override
		public 
		org.radixware.ads.Testing.explorer.TestCase.DefaultGroupModel openGroupModel(){
		     return (org.radixware.ads.Testing.explorer.TestCase.DefaultGroupModel)super.openGroupModel();
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.client.types.Reference dummy = ((org.radixware.kernel.common.client.types.Reference)x);
			setValue(dummy);
		}

		/*Radix::Testing::TestCase:General:Model:groupRef:EmbeddedClass-Property Presentation Embedded Class*/




		/*Radix::Testing::TestCase:General:Model:groupRef:Nested classes-Nested Classes*/

		/*Radix::Testing::TestCase:General:Model:groupRef:Properties-Properties*/

		/*Radix::Testing::TestCase:General:Model:groupRef:Methods-Methods*/

		/*Radix::Testing::TestCase:General:Model:groupRef:isVisible-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:General:Model:groupRef:isVisible")
		public published  boolean isVisible () {
			if(getClassId() == idof[TestCase.Group])
			    return false;

			return super.isVisible();
		}













		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:General:Model:groupRef")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:General:Model:groupRef")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public GroupRef getGroupRef(){return (GroupRef)getProperty(col3T77RRLFUBHDLA7X4GCO27SCOU);}

	/*Radix::Testing::TestCase:General:Model:seq-Presentation Property*/




	public class Seq extends org.radixware.ads.Testing.explorer.TestCase.col3ZQVM54K5FEL7D7RH4HIYI62Y4{
		public Seq(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Int dummy = ((Int)x);
			setValue(dummy);
		}

		/*Radix::Testing::TestCase:General:Model:seq:EmbeddedClass-Property Presentation Embedded Class*/




		/*Radix::Testing::TestCase:General:Model:seq:Nested classes-Nested Classes*/

		/*Radix::Testing::TestCase:General:Model:seq:Properties-Properties*/

		/*Radix::Testing::TestCase:General:Model:seq:Methods-Methods*/

		/*Radix::Testing::TestCase:General:Model:seq:isVisible-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:General:Model:seq:isVisible")
		public published  boolean isVisible () {
			if(getClassId() == idof[TestCase.Group] || groupId.Value == null)
			    return false;

			return super.isVisible();
		}













		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:General:Model:seq")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:General:Model:seq")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public Seq getSeq(){return (Seq)getProperty(col3ZQVM54K5FEL7D7RH4HIYI62Y4);}

	/*Radix::Testing::TestCase:General:Model:lastIsExecDate-Presentation Property*/




	public class LastIsExecDate extends org.radixware.ads.Testing.explorer.TestCase.colI5ELDBK7XVDQDA626NCPOMNAXM{
		public LastIsExecDate(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			java.sql.Timestamp dummy = ((java.sql.Timestamp)x);
			setValue(dummy);
		}

		/*Radix::Testing::TestCase:General:Model:lastIsExecDate:EmbeddedClass-Property Presentation Embedded Class*/




		/*Radix::Testing::TestCase:General:Model:lastIsExecDate:Nested classes-Nested Classes*/

		/*Radix::Testing::TestCase:General:Model:lastIsExecDate:Properties-Properties*/

		/*Radix::Testing::TestCase:General:Model:lastIsExecDate:Methods-Methods*/

		/*Radix::Testing::TestCase:General:Model:lastIsExecDate:isVisible-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:General:Model:lastIsExecDate:isVisible")
		public published  boolean isVisible () {
			return super.isVisible();// && .Value;
		}













		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:General:Model:lastIsExecDate")
		public  java.sql.Timestamp getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:General:Model:lastIsExecDate")
		public   void setValue(java.sql.Timestamp val) {
			Value = val;
		}
	}
	public LastIsExecDate getLastIsExecDate(){return (LastIsExecDate)getProperty(colI5ELDBK7XVDQDA626NCPOMNAXM);}

	/*Radix::Testing::TestCase:General:Model:lastIsSuccessDate-Presentation Property*/




	public class LastIsSuccessDate extends org.radixware.ads.Testing.explorer.TestCase.colPI3FLY6XA5DV3LZKE3RO4PQFFQ{
		public LastIsSuccessDate(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			java.sql.Timestamp dummy = ((java.sql.Timestamp)x);
			setValue(dummy);
		}

		/*Radix::Testing::TestCase:General:Model:lastIsSuccessDate:EmbeddedClass-Property Presentation Embedded Class*/




		/*Radix::Testing::TestCase:General:Model:lastIsSuccessDate:Nested classes-Nested Classes*/

		/*Radix::Testing::TestCase:General:Model:lastIsSuccessDate:Properties-Properties*/

		/*Radix::Testing::TestCase:General:Model:lastIsSuccessDate:Methods-Methods*/

		/*Radix::Testing::TestCase:General:Model:lastIsSuccessDate:isVisible-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:General:Model:lastIsSuccessDate:isVisible")
		public published  boolean isVisible () {
			return super.isVisible();// && .Value;
		}













		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:General:Model:lastIsSuccessDate")
		public  java.sql.Timestamp getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:General:Model:lastIsSuccessDate")
		public   void setValue(java.sql.Timestamp val) {
			Value = val;
		}
	}
	public LastIsSuccessDate getLastIsSuccessDate(){return (LastIsSuccessDate)getProperty(colPI3FLY6XA5DV3LZKE3RO4PQFFQ);}

	/*Radix::Testing::TestCase:General:Model:notificationEmail-Presentation Property*/




	public class NotificationEmail extends org.radixware.ads.Testing.explorer.TestCase.colUSPDDDJLZ5AINICL3FIA4KD7GU{
		public NotificationEmail(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}

		/*Radix::Testing::TestCase:General:Model:notificationEmail:EmbeddedClass-Property Presentation Embedded Class*/




		/*Radix::Testing::TestCase:General:Model:notificationEmail:Nested classes-Nested Classes*/

		/*Radix::Testing::TestCase:General:Model:notificationEmail:Properties-Properties*/

		/*Radix::Testing::TestCase:General:Model:notificationEmail:Methods-Methods*/

		/*Radix::Testing::TestCase:General:Model:notificationEmail:isVisible-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:General:Model:notificationEmail:isVisible")
		public published  boolean isVisible () {
			return super.isVisible();// && .Value;
		}













		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:General:Model:notificationEmail")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:General:Model:notificationEmail")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public NotificationEmail getNotificationEmail(){return (NotificationEmail)getProperty(colUSPDDDJLZ5AINICL3FIA4KD7GU);}

	/*Radix::Testing::TestCase:General:Model:seqIsFailCount-Presentation Property*/




	public class SeqIsFailCount extends org.radixware.ads.Testing.explorer.TestCase.colKTWEBHYK2JGENOQY3SHZ27BQ6I{
		public SeqIsFailCount(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Int dummy = ((Int)x);
			setValue(dummy);
		}

		/*Radix::Testing::TestCase:General:Model:seqIsFailCount:EmbeddedClass-Property Presentation Embedded Class*/




		/*Radix::Testing::TestCase:General:Model:seqIsFailCount:Nested classes-Nested Classes*/

		/*Radix::Testing::TestCase:General:Model:seqIsFailCount:Properties-Properties*/

		/*Radix::Testing::TestCase:General:Model:seqIsFailCount:Methods-Methods*/

		/*Radix::Testing::TestCase:General:Model:seqIsFailCount:isVisible-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:General:Model:seqIsFailCount:isVisible")
		public published  boolean isVisible () {
			return super.isVisible();// && .Value;
		}













		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:General:Model:seqIsFailCount")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:General:Model:seqIsFailCount")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public SeqIsFailCount getSeqIsFailCount(){return (SeqIsFailCount)getProperty(colKTWEBHYK2JGENOQY3SHZ27BQ6I);}

	/*Radix::Testing::TestCase:General:Model:runOnIs-Presentation Property*/




	public class RunOnIs extends org.radixware.ads.Testing.explorer.TestCase.colQP4LKSN3IFHITPAQXU7J72H6FA{
		public RunOnIs(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
			if (aemBMFQWKU227NRDISQAAAAAAAAAA.this.getDefinition().isPropertyDefExistsById(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKTWEBHYK2JGENOQY3SHZ27BQ6I"))) {
				this.addDependent(aemBMFQWKU227NRDISQAAAAAAAAAA.this.getProperty(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKTWEBHYK2JGENOQY3SHZ27BQ6I")));
			}
			if (aemBMFQWKU227NRDISQAAAAAAAAAA.this.getDefinition().isPropertyDefExistsById(org.radixware.kernel.common.types.Id.Factory.loadFrom("colUSPDDDJLZ5AINICL3FIA4KD7GU"))) {
				this.addDependent(aemBMFQWKU227NRDISQAAAAAAAAAA.this.getProperty(org.radixware.kernel.common.types.Id.Factory.loadFrom("colUSPDDDJLZ5AINICL3FIA4KD7GU")));
			}
			if (aemBMFQWKU227NRDISQAAAAAAAAAA.this.getDefinition().isPropertyDefExistsById(org.radixware.kernel.common.types.Id.Factory.loadFrom("colI5ELDBK7XVDQDA626NCPOMNAXM"))) {
				this.addDependent(aemBMFQWKU227NRDISQAAAAAAAAAA.this.getProperty(org.radixware.kernel.common.types.Id.Factory.loadFrom("colI5ELDBK7XVDQDA626NCPOMNAXM")));
			}
			if (aemBMFQWKU227NRDISQAAAAAAAAAA.this.getDefinition().isPropertyDefExistsById(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPI3FLY6XA5DV3LZKE3RO4PQFFQ"))) {
				this.addDependent(aemBMFQWKU227NRDISQAAAAAAAAAA.this.getProperty(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPI3FLY6XA5DV3LZKE3RO4PQFFQ")));
			}
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Bool dummy = ((Bool)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:General:Model:runOnIs")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:General:Model:runOnIs")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public RunOnIs getRunOnIs(){return (RunOnIs)getProperty(colQP4LKSN3IFHITPAQXU7J72H6FA);}

	/*Radix::Testing::TestCase:General:Model:execDurationStr-Presentation Property*/




	public class ExecDurationStr extends org.radixware.ads.Testing.explorer.TestCase.prdROKBL5544NGPXNKFHAAELEX3SM{
		public ExecDurationStr(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:General:Model:execDurationStr")
		public  Str getValue() {

			if (cpuNanos.Value == null || dbNanos.Value == null || extNanos.Value == null || queueNanos.Value == null) {
			    return null;
			}
			//Default constructor creates mask with TriadDelimeterType=NONE :-(
			final long total = cpuNanos.Value.longValue() + dbNanos.Value.longValue()
			        + extNanos.Value.longValue() + (queueNanos.Value.longValue() == -1 ? 0 : queueNanos.Value.longValue());
			return maskDuration.toStr(getEnvironment(), total / 1000000l)
			        + "/" + maskDuration.toStr(getEnvironment(), cpuNanos.Value.longValue() / 1000000l)
			        + "/" + maskDuration.toStr(getEnvironment(), dbNanos.Value.longValue() / 1000000l)
			        + "/" + maskDuration.toStr(getEnvironment(), extNanos.Value.longValue() / 1000000l)
			        + (queueNanos.Value.longValue() == -1 ? "" : "/" + maskDuration.toStr(getEnvironment(), queueNanos.Value.longValue() / 1000000l))
			        + " (ms)";
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:General:Model:execDurationStr")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ExecDurationStr getExecDurationStr(){return (ExecDurationStr)getProperty(prdROKBL5544NGPXNKFHAAELEX3SM);}

	/*Radix::Testing::TestCase:General:Model:queueNanos-Presentation Property*/




	public class QueueNanos extends org.radixware.ads.Testing.explorer.TestCase.colMDKXQBA6KZFHZH6FTKW7KHF4Z4{
		public QueueNanos(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
			if (aemBMFQWKU227NRDISQAAAAAAAAAA.this.getDefinition().isPropertyDefExistsById(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdROKBL5544NGPXNKFHAAELEX3SM"))) {
				this.addDependent(aemBMFQWKU227NRDISQAAAAAAAAAA.this.getProperty(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdROKBL5544NGPXNKFHAAELEX3SM")));
			}
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Int dummy = ((Int)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:General:Model:queueNanos")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:General:Model:queueNanos")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public QueueNanos getQueueNanos(){return (QueueNanos)getProperty(colMDKXQBA6KZFHZH6FTKW7KHF4Z4);}

	/*Radix::Testing::TestCase:General:Model:extNanos-Presentation Property*/




	public class ExtNanos extends org.radixware.ads.Testing.explorer.TestCase.colG4EPHGTTE5BM3DLJN55VNIVOBE{
		public ExtNanos(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
			if (aemBMFQWKU227NRDISQAAAAAAAAAA.this.getDefinition().isPropertyDefExistsById(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdROKBL5544NGPXNKFHAAELEX3SM"))) {
				this.addDependent(aemBMFQWKU227NRDISQAAAAAAAAAA.this.getProperty(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdROKBL5544NGPXNKFHAAELEX3SM")));
			}
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Int dummy = ((Int)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:General:Model:extNanos")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:General:Model:extNanos")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public ExtNanos getExtNanos(){return (ExtNanos)getProperty(colG4EPHGTTE5BM3DLJN55VNIVOBE);}

	/*Radix::Testing::TestCase:General:Model:dbNanos-Presentation Property*/




	public class DbNanos extends org.radixware.ads.Testing.explorer.TestCase.colYH7NIOG57JEHPD7QO4GMHI7K54{
		public DbNanos(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
			if (aemBMFQWKU227NRDISQAAAAAAAAAA.this.getDefinition().isPropertyDefExistsById(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdROKBL5544NGPXNKFHAAELEX3SM"))) {
				this.addDependent(aemBMFQWKU227NRDISQAAAAAAAAAA.this.getProperty(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdROKBL5544NGPXNKFHAAELEX3SM")));
			}
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Int dummy = ((Int)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:General:Model:dbNanos")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:General:Model:dbNanos")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public DbNanos getDbNanos(){return (DbNanos)getProperty(colYH7NIOG57JEHPD7QO4GMHI7K54);}

	/*Radix::Testing::TestCase:General:Model:cpuNanos-Presentation Property*/




	public class CpuNanos extends org.radixware.ads.Testing.explorer.TestCase.colLLFER2QMUFH5BME327Q4IPNJNM{
		public CpuNanos(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
			if (aemBMFQWKU227NRDISQAAAAAAAAAA.this.getDefinition().isPropertyDefExistsById(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdROKBL5544NGPXNKFHAAELEX3SM"))) {
				this.addDependent(aemBMFQWKU227NRDISQAAAAAAAAAA.this.getProperty(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdROKBL5544NGPXNKFHAAELEX3SM")));
			}
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Int dummy = ((Int)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:General:Model:cpuNanos")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:General:Model:cpuNanos")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public CpuNanos getCpuNanos(){return (CpuNanos)getProperty(colLLFER2QMUFH5BME327Q4IPNJNM);}






























	/*Radix::Testing::TestCase:General:Model:Methods-Methods*/

	/*Radix::Testing::TestCase:General:Model:onCommand_Rerun-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:General:Model:onCommand_Rerun")
	public published  void onCommand_Rerun (org.radixware.ads.Testing.explorer.TestCase.Rerun command) {
		final Client.Types::IProgressHandle progress = Environment.getProgressHandleManager().newStandardProgressHandle();
		progress.startProgress("Running test...",true);
		try {
		    command.send();
		} catch (Exceptions::ServiceClientException e) {
		    showException(e);    
		} catch (InterruptedException e){
		    //test executing was canceled by user - do nothing.
		    return;
		} finally{
		    try{
		        if (!progress.wasCanceled()){//if test execution was not canceled...
		            afterRerun();
		        }
		    } catch(Exceptions::ServiceClientException e){
		        showException(e);
		    } catch(InterruptedException e){
		        //reading new data was canceled by user - do nothing               
		    } finally{
		        progress.finishProgress();
		    }
		}
	}

	/*Radix::Testing::TestCase:General:Model:getContextId-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:General:Model:getContextId")
	public published  Str getContextId () {
		return String.valueOf(id.Value);
	}

	/*Radix::Testing::TestCase:General:Model:getContextType-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:General:Model:getContextType")
	public published  Str getContextType () {
		return Arte::EventContextType:TestCase.Value;
	}

	/*Radix::Testing::TestCase:General:Model:expandInSelector-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:General:Model:expandInSelector")
	public  void expandInSelector (org.radixware.kernel.common.client.views.IView view) {



	}

	/*Radix::Testing::TestCase:General:Model:afterRerun-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:General:Model:afterRerun")
	protected  void afterRerun () throws java.lang.InterruptedException,org.radixware.kernel.common.exceptions.ServiceClientException {
		if (getEntityView() != null) {//and editor opened
		    getEntityView().Actions.RereadAction.trigger();//reread editor
		} else {
		    read();//reread object data
		}
	}

	/*Radix::Testing::TestCase:General:Model:onCommand_Import-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:General:Model:onCommand_Import")
	public published  void onCommand_Import (org.radixware.ads.Testing.explorer.TestCase.Import command) {
		java.io.File file = CfgManagement::DesktopUtils.openFileForImport(findNearestView(), command.getTitle());
		if (file == null)
		    return;

		try {
		    ImpExpXsd:TestCaseDocument input = ImpExpXsd:TestCaseDocument.Factory.parse(file);
		    Common.Dlg::ClientUtils.viewImportResult(command.send(input));
		    getView().reread();
		} catch (Exceptions::XmlException | Exceptions::ServiceClientException | Exceptions::IOException e) {
		    showException(e);
		} catch (Exceptions::InterruptedException e) {
		}
	}

	/*Radix::Testing::TestCase:General:Model:afterRead-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:General:Model:afterRead")
	protected published  void afterRead () {
		super.afterRead();

		if (!isInSelectorRowContext()) {
		    if (queueNanos.Value != null && queueNanos.Value.longValue() != -1) {
		        if (!execDurationStr.getTitle().endsWith("/ACT)")) {
		            execDurationStr.setTitle(execDurationStr.getTitle().substring(0, execDurationStr.getTitle().length() - 1) + "/ACT)");
		        }
		    } else if (execDurationStr.getTitle().endsWith("/ACT)")) {
		        execDurationStr.setTitle(execDurationStr.getTitle().replace("/ACT", ""));
		    }
		}
	}

	/*Radix::Testing::TestCase:General:Model:beforePrepareCreate-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:General:Model:beforePrepareCreate")
	protected published  boolean beforePrepareCreate () {
		if (this.getEntityContext() instanceof Explorer.Context::InSelectorCreatingContext) {
		    final Explorer.Models::GroupModel groupModel = getOwnerSelectorModel();
		    final TestCase parent = (TestCase) groupModel.getGroupContext().getHolderEntityModel();

		    if (parent != null) {
		        if (parent.isGroup.Value.booleanValue()) {
		            final TestCase:General:Model group = (TestCase:General:Model) parent;
		            groupId.setValue(group.id.Value);
		            groupRef.setValue(new Reference(group));
		            parentId.setValue(group.parentId.Value);
		            Explorer.Types::Reference groupParentRef = (Explorer.Types::Reference) group.parent.Value;
		            if (groupParentRef != null) {
		                parent.setValue(groupParentRef);
		            }
		        }
		        if (parent.isParent.Value.booleanValue()) {
		            parentId.setValue(parent.id.Value);
		            parent.setValue(new Reference((TestCase:General:Model) parent));
		        }
		    }
		}

		return super.beforePrepareCreate();
	}
	public final class Rerun extends org.radixware.ads.Testing.explorer.TestCase.Rerun{
		protected Rerun(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_Rerun( this );
		}

	}

	public final class Import extends org.radixware.ads.Testing.explorer.TestCase.Import{
		protected Import(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_Import( this );
		}

	}

















}

/* Radix::Testing::TestCase:General:Model - Desktop Meta*/

/*Radix::Testing::TestCase:General:Model-Entity Model Class*/

package org.radixware.ads.Testing.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class General:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aemBMFQWKU227NRDISQAAAAAAAAAA"),
						"General:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::Testing::TestCase:General:Model:Properties-Properties*/
						null,
						null,
						null,
						null,
						null,
						null,
						false,
						false,
						false
						);
}

/* Radix::Testing::TestCase:Web - Desktop Meta*/

/*Radix::Testing::TestCase:Web-Editor Presentation*/

package org.radixware.ads.Testing.explorer;
public final class Web_mi{
	private static final class Web_DEF extends org.radixware.kernel.common.client.meta.RadEditorPresentationDef{
		Web_DEF(){
			super(org.radixware.kernel.common.types.Id.Factory.loadFrom("epr4WBLCNMXSFEI3M2LSYDLSIK6Q4"),
			"Web",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("tblHW4OSVMS27NRDISQAAAAAAAAAA"),
			null,
			null,

			/*Radix::Testing::TestCase:Web:Editor Pages-Editor Presentation Pages*/
			new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

					/*Radix::Testing::TestCase:Web:General-Editor Page*/

					 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgUCA4GRVBPZE5TGVAEGVILKDJM4"),"General",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),null,null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMWOGO4MS27NRDISQAAAAAAAAAA"),0,0,2,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdZSHM4TXE3XNRDISQAAAAAAAAAA"),0,1,2,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQKCERAMS27NRDISQAAAAAAAAAA"),1,2,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colN3UZ2Z4S27NRDISQAAAAAAAAAA"),0,5,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHWP3QCET27NRDISQAAAAAAAAAA"),0,6,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEZ4SH34S27NRDISQAAAAAAAAAA"),1,5,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colYWL4NXUS27NRDISQAAAAAAAAAA"),0,3,2,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colC46NAASN3PNRDISQAAAAAAAAAA"),0,4,2,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQP4LKSN3IFHITPAQXU7J72H6FA"),0,7,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGBJZDIUS27NRDISQAAAAAAAAAA"),0,2,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPMEM3RSM3PNRDISQAAAAAAAAAA"),0,8,1,false,false)
					},null)
			},
			new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
				new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgUCA4GRVBPZE5TGVAEGVILKDJM4"))}
			,

			/*Radix::Testing::TestCase:Web:Children-Explorer Items*/
				null
			,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
			null,
			0,
			null,
			(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
			2192,0,0,null);
		}
		@Override
		protected org.radixware.kernel.common.client.models.Model createModelImpl(org.radixware.kernel.common.client.IClientEnvironment userSession) {    return new org.radixware.ads.Testing.explorer.TestCase.TestCase_DefaultModel(userSession,this);
		}
	}
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new Web_DEF(); 
;
}
/* Radix::Testing::TestCase:Web - Web Meta*/

/*Radix::Testing::TestCase:Web-Editor Presentation*/

package org.radixware.ads.Testing.web;
public final class Web_mi{
	private static final class Web_DEF extends org.radixware.kernel.common.client.meta.RadEditorPresentationDef{
		Web_DEF(){
			super(org.radixware.kernel.common.types.Id.Factory.loadFrom("epr4WBLCNMXSFEI3M2LSYDLSIK6Q4"),
			"Web",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("tblHW4OSVMS27NRDISQAAAAAAAAAA"),
			null,
			null,

			/*Radix::Testing::TestCase:Web:Editor Pages-Editor Presentation Pages*/
			new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

					/*Radix::Testing::TestCase:Web:General-Editor Page*/

					 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgUCA4GRVBPZE5TGVAEGVILKDJM4"),"General",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),null,null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMWOGO4MS27NRDISQAAAAAAAAAA"),0,0,2,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdZSHM4TXE3XNRDISQAAAAAAAAAA"),0,1,2,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQKCERAMS27NRDISQAAAAAAAAAA"),1,2,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colN3UZ2Z4S27NRDISQAAAAAAAAAA"),0,5,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHWP3QCET27NRDISQAAAAAAAAAA"),0,6,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEZ4SH34S27NRDISQAAAAAAAAAA"),1,5,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colYWL4NXUS27NRDISQAAAAAAAAAA"),0,3,2,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colC46NAASN3PNRDISQAAAAAAAAAA"),0,4,2,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQP4LKSN3IFHITPAQXU7J72H6FA"),0,7,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGBJZDIUS27NRDISQAAAAAAAAAA"),0,2,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPMEM3RSM3PNRDISQAAAAAAAAAA"),0,8,1,false,false)
					},null)
			},
			new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
				new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgUCA4GRVBPZE5TGVAEGVILKDJM4"))}
			,

			/*Radix::Testing::TestCase:Web:Children-Explorer Items*/
				null
			,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
			null,
			0,
			null,
			(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
			2192,0,0,null);
		}
		@Override
		protected org.radixware.kernel.common.client.models.Model createModelImpl(org.radixware.kernel.common.client.IClientEnvironment userSession) {    return new org.radixware.ads.Testing.web.TestCase.TestCase_DefaultModel(userSession,this);
		}
	}
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new Web_DEF(); 
;
}
/* Radix::Testing::TestCase:General - Desktop Meta*/

/*Radix::Testing::TestCase:General-Selector Presentation*/

package org.radixware.ads.Testing.explorer;
public final class General_mi extends org.radixware.kernel.common.client.meta.RadSelectorPresentationDef{
	public static final org.radixware.kernel.common.client.meta.RadSelectorPresentationDef rdxMeta = new General_mi();
	private General_mi(){
		super(org.radixware.kernel.common.types.Id.Factory.loadFrom("sprNGT5DE5G27NRDISQAAAAAAAAAA"),
		"General",
		org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
		null,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("tblHW4OSVMS27NRDISQAAAAAAAAAA"),
		null,
		null,
		null,
		null,
		true,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("srt34WFCNN2RREXPDEKUMG5SZUB6A"),
		null,
		false,
		true,
		null,
		271272,
		null,
		2192,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprBMFQWKU227NRDISQAAAAAAAAAA")},
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprBMFQWKU227NRDISQAAAAAAAAAA")},
		false,true,false,0,0);
		columns = new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn[]{

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3AN3AUHYHNHNFBQ3J4PRDXRRXE"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3ZQVM54K5FEL7D7RH4HIYI62Y4"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd4CDENELMOJFFVP5WDQGC72CQRA"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdQO2OHKBQTNB2LK2NR3TUUWDQWQ"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdR7AAOIBTTNDT5JUMOATGCT22JQ"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colN3UZ2Z4S27NRDISQAAAAAAAAAA"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdYRLSFFVV2ZFIJFFAT34VLBDB4Y"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.RESIZE_BY_CONTENT,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdZSHM4TXE3XNRDISQAAAAAAAAAA"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHWP3QCET27NRDISQAAAAAAAAAA"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMWOGO4MS27NRDISQAAAAAAAAAA"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGBJZDIUS27NRDISQAAAAAAAAAA"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQKCERAMS27NRDISQAAAAAAAAAA"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colYWL4NXUS27NRDISQAAAAAAAAAA"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colC46NAASN3PNRDISQAAAAAAAAAA"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEZ4SH34S27NRDISQAAAAAAAAAA"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQP4LKSN3IFHITPAQXU7J72H6FA"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colL4QQIGK7MFGF5FCW7HHEA4YIZI"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colUSPDDDJLZ5AINICL3FIA4KD7GU"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colI5ELDBK7XVDQDA626NCPOMNAXM"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPI3FLY6XA5DV3LZKE3RO4PQFFQ"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKTWEBHYK2JGENOQY3SHZ27BQ6I"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null)
		};
	}
}
/* Radix::Testing::TestCase:General:Model - Desktop Executable*/

/*Radix::Testing::TestCase:General:Model-Group Model Class*/

package org.radixware.ads.Testing.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:General:Model")
public class General:Model  extends org.radixware.ads.Testing.explorer.TestCase.DefaultGroupModel {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return General:Model_mi.rdxMeta; }



	public General:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadSelectorPresentationDef def){super(userSession,def);}

	/*Radix::Testing::TestCase:General:Model:Nested classes-Nested Classes*/

	/*Radix::Testing::TestCase:General:Model:RestrictedSortings-Desktop Dynamic Class*/


	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:General:Model:RestrictedSortings")
	private static final class RestrictedSortings  extends org.radixware.kernel.common.client.models.groupsettings.Sortings  {



		/*Radix::Testing::TestCase:General:Model:RestrictedSortings:Nested classes-Nested Classes*/

		/*Radix::Testing::TestCase:General:Model:RestrictedSortings:Properties-Properties*/

		/*Radix::Testing::TestCase:General:Model:RestrictedSortings:Methods-Methods*/

		/*Radix::Testing::TestCase:General:Model:RestrictedSortings:isAcceptable-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:General:Model:RestrictedSortings:isAcceptable")
		public published  boolean isAcceptable (org.radixware.kernel.common.client.meta.RadSortingDef sorting, org.radixware.kernel.common.client.models.FilterModel currentFilter) {
			if (sorting.getId() == idof[TestCase:By Start Time]){
			    return super.isAcceptable(sorting, currentFilter);
			}else{
			    return false;
			}
		}

		/*Radix::Testing::TestCase:General:Model:RestrictedSortings:canCreateNew-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:General:Model:RestrictedSortings:canCreateNew")
		public published  boolean canCreateNew (org.radixware.kernel.common.client.models.FilterModel filter) {
			return false;
		}

		/*Radix::Testing::TestCase:General:Model:RestrictedSortings:RestrictedSortings-User Method*/

		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:General:Model:RestrictedSortings:RestrictedSortings")
		public  RestrictedSortings (org.radixware.kernel.common.client.models.GroupModel groupModel) {
			super(groupModel);
		}


	}

	/*Radix::Testing::TestCase:General:Model:Properties-Properties*/

	/*Radix::Testing::TestCase:General:Model:restrictedSortings-Dynamic Property*/



	protected org.radixware.ads.Testing.explorer.General:Model.RestrictedSortings restrictedSortings=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:General:Model:restrictedSortings")
	private final  org.radixware.ads.Testing.explorer.General:Model.RestrictedSortings getRestrictedSortings() {
		return restrictedSortings;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:General:Model:restrictedSortings")
	private final   void setRestrictedSortings(org.radixware.ads.Testing.explorer.General:Model.RestrictedSortings val) {
		restrictedSortings = val;
	}






	/*Radix::Testing::TestCase:General:Model:Methods-Methods*/

	/*Radix::Testing::TestCase:General:Model:createView-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:General:Model:createView")
	public published  org.radixware.kernel.common.client.views.IView createView () {
		  return new TestCaseSelectorView(getEnvironment(), idof[TestCase:General:Children],idof[TestCase:thereIsChilds] );
	}

	/*Radix::Testing::TestCase:General:Model:beforeReadFirstPage-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:General:Model:beforeReadFirstPage")
	protected published  void beforeReadFirstPage () {
		super.beforeReadFirstPage();

		if (getGroupContext().RootGroupContext == null && getGroupContext().getHolderEntityModel() == null) {
		    try {
		        setCondition(Explorer.Utils::SqmlExpression.and(
		                Explorer.Utils::SqmlExpression.is_null(Explorer.Utils::SqmlExpression.this_property(idof[Meta::TestCase], idof[TestCase:groupId])),
		                Explorer.Utils::SqmlExpression.is_null(Explorer.Utils::SqmlExpression.this_property(idof[Meta::TestCase], idof[TestCase:parentId])))
		        );
		    } catch (Exceptions::Throwable ex) {
		        ex.printStackTrace();
		    }
		}
	}

	/*Radix::Testing::TestCase:General:Model:afterUpdateTestGroupSettings-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:General:Model:afterUpdateTestGroupSettings")
	public  void afterUpdateTestGroupSettings (org.radixware.ads.Testing.explorer.General:Model model, org.radixware.kernel.common.types.ArrStr pathInTree) {
		Client.Views::ISelector selector = getGroupView();
		if (selector != null) {
		    try {
		        selector.reread(null);
		    } catch (Throwable ex) {
		        getEnvironment().processException(ex);
		    }

		    Client.Views::IView view = selector;
		    while (view != null) {
		        if (view instanceof Explorer.Views::SelectorView) {
		            Explorer.Views::SelectorView selectorView = (Explorer.Views::SelectorView) view;
		            Client.Views::ISelectorWidget widget = selectorView.getSelectorWidget();
		            if (widget instanceof Explorer.Widgets::SelectorTree) {
		                Explorer.Widgets::SelectorTree tree = (Explorer.Widgets::SelectorTree) widget;
		                try {
		                    com.trolltech.qt.core.QModelIndex index = tree.pathToIndex(pathInTree, true);
		                    if (index != null) {
		                        tree.setCurrentIndex(index);
		                    }
		                } catch (Throwable ex) {
		                    ex.printStackTrace();
		                }
		            }
		            break;
		        }
		        view = view.findParentView();
		    }
		}
	}

	/*Radix::Testing::TestCase:General:Model:rereadChilds-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:General:Model:rereadChilds")
	private final  void rereadChilds () {
		if (getView() != null) {
		    Client.Views::ISelectorWidget widget = ((Explorer.Views::SelectorView) getView()).getSelectorWidget();
		    if (widget instanceof Explorer.Widgets::SelectorTree) {
		        try {
		            final com.trolltech.qt.core.QModelIndex currentIndex = ((Explorer.Widgets::SelectorTree) widget).currentIndex();
		            if (currentIndex != null) {
		                ((Explorer.Widgets::SelectorTree) widget).reread(currentIndex.parent(), getGroupView().getCurrentEntity().getPid());
		            }
		        } catch (Exceptions::InterruptedException | Exceptions::ServiceClientException ex) {
		            getEnvironment().getTracer().error("Error on update selector tree", ex);
		        }
		    }
		}

	}

	/*Radix::Testing::TestCase:General:Model:onCommand_MoveDown-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:General:Model:onCommand_MoveDown")
	public  void onCommand_MoveDown (org.radixware.ads.Testing.explorer.TestCaseGroup.MoveDown command) {
		try {
		    Arte::TypesXsd:IntDocument xIn = Arte::TypesXsd:IntDocument.Factory.newInstance();
		    xIn.Int = ((TestCase) getGroupView().getCurrentEntity()).id.Value;
		    command.send(xIn);

		    rereadChilds();
		} catch (Exceptions::InterruptedException e) {
		    showException(e);
		} catch (Exceptions::ServiceClientException e) {
		    showException(e);
		}
	}

	/*Radix::Testing::TestCase:General:Model:onCommand_MoveUp-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:General:Model:onCommand_MoveUp")
	public  void onCommand_MoveUp (org.radixware.ads.Testing.explorer.TestCaseGroup.MoveUp command) {
		try {
		    Arte::TypesXsd:IntDocument xIn = Arte::TypesXsd:IntDocument.Factory.newInstance();
		    xIn.Int = ((TestCase) getGroupView().getCurrentEntity()).id.Value;
		    command.send(xIn);
		    
		    rereadChilds();
		} catch (Exceptions::InterruptedException e) {
		    showException(e);
		} catch (Exceptions::ServiceClientException e) {
		    showException(e);
		}
	}

	/*Radix::Testing::TestCase:General:Model:afterInsertChildItem-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:General:Model:afterInsertChildItem")
	public published  void afterInsertChildItem (org.radixware.kernel.common.client.views.IExplorerItemView childItem) {
		super.afterInsertChildItem(childItem);

		if (childItem.getModel() instanceof TestCase) {
		    TestCase tc = (TestCase) childItem.getModel();
		    final boolean showChilds = tc.canHaveChilds.Value.booleanValue();
		    for (int i = 0; i < childItem.getChildsCount(); i++) {
		        Client.Views::IExplorerItemView item = childItem.getChild(i);
		        if (item.getExplorerItemId() == idof[TestCase:General:Children]) {
		            item.setVisible(showChilds);
		        }
		    }
		}
	}

	/*Radix::Testing::TestCase:General:Model:onCommand_Import-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:General:Model:onCommand_Import")
	public  void onCommand_Import (org.radixware.ads.Testing.explorer.TestCaseGroup.Import command) {
		java.io.File file = CfgManagement::DesktopUtils.openFileForImport(findNearestView(), command.getTitle());
		if (file == null)
		    return;

		ImpExpXsd:TestCaseGroupDocument xIn;

		try {
		    try {
		        xIn = ImpExpXsd:TestCaseGroupDocument.Factory.parse(file);
		    } catch (Exceptions::XmlException ex) {
		        ImpExpXsd:TestCaseDocument xSingleTest = ImpExpXsd:TestCaseDocument.Factory.parse(file);
		        xIn = ImpExpXsd:TestCaseGroupDocument.Factory.newInstance();
		        xIn.addNewTestCaseGroup().addNewItem().set(xSingleTest.TestCase);
		    }
		    
		    TestCase contextTest = (TestCase) findOwnerByClass(TestCase.class);
		    if (contextTest != null) {
		        xIn.TestCaseGroup.OwnerExtGuid = contextTest.extGuid.Value;
		        xIn.TestCaseGroup.IsOwnerGroup = contextTest.isGroup.Value;
		    }
		    
		    Common.Dlg::ClientUtils.viewImportResult(command.send(xIn));
		    getView().reread();
		} catch (Exceptions::XmlException | Exceptions::ServiceClientException | Exceptions::IOException e) {
		    showException(e);
		} catch (Exceptions::InterruptedException e) {
		}
	}

	/*Radix::Testing::TestCase:General:Model:getSortings-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:General:Model:getSortings")
	public published  org.radixware.kernel.common.client.models.groupsettings.Sortings getSortings () {
		if (!(getGroupContext().getHolderEntityModel() instanceof TestCase.Group)) {
		    return super.getSortings();
		}
		if (restrictedSortings == null) {
		    restrictedSortings = new TestCase:General:Model.RestrictedSortings(this);
		}
		return restrictedSortings;
	}

	/*Radix::Testing::TestCase:General:Model:clean-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:General:Model:clean")
	public published  void clean () {
		super.clean();
		restrictedSortings = null;
	}
	public final class MoveUp extends org.radixware.ads.Testing.explorer.TestCaseGroup.MoveUp{
		protected MoveUp(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_MoveUp( this );
		}

	}

	public final class Import extends org.radixware.ads.Testing.explorer.TestCaseGroup.Import{
		protected Import(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_Import( this );
		}

	}

	public final class MoveDown extends org.radixware.ads.Testing.explorer.TestCaseGroup.MoveDown{
		protected MoveDown(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_MoveDown( this );
		}

	}

















}

/* Radix::Testing::TestCase:General:Model - Desktop Meta*/

/*Radix::Testing::TestCase:General:Model-Group Model Class*/

package org.radixware.ads.Testing.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class General:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("agmNGT5DE5G27NRDISQAAAAAAAAAA"),
						"General:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::Testing::TestCase:General:Model:Properties-Properties*/
						null,
						null,
						null,
						null,
						null,
						null,
						false,
						false,
						false
						);
}

/* Radix::Testing::TestCase:FlatList - Desktop Meta*/

/*Radix::Testing::TestCase:FlatList-Selector Presentation*/

package org.radixware.ads.Testing.explorer;
public final class FlatList_mi extends org.radixware.kernel.common.client.meta.RadSelectorPresentationDef{
	public static final org.radixware.kernel.common.client.meta.RadSelectorPresentationDef rdxMeta = new FlatList_mi();
	private FlatList_mi(){
		super(org.radixware.kernel.common.types.Id.Factory.loadFrom("sprLEXXOTV4O5AEBO3VECZLSIGRCY"),
		"FlatList",
		org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
		null,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHW4OSVMS27NRDISQAAAAAAAAAA"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("tblHW4OSVMS27NRDISQAAAAAAAAAA"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5JSBTLM2ZNFD5PTMQNEFE3UKV4"),
		null,
		null,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("srtA7QU7WNG27NRDISQAAAAAAAAAA")},
		false,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("srtA7QU7WNG27NRDISQAAAAAAAAAA"),
		new org.radixware.kernel.common.types.Id[]{},
		false,
		false,
		null,
		32,
		null,
		0,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprBMFQWKU227NRDISQAAAAAAAAAA")},
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprBMFQWKU227NRDISQAAAAAAAAAA")},
		false,true,false,0,0);
		columns = new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn[]{

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdQO2OHKBQTNB2LK2NR3TUUWDQWQ"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdR7AAOIBTTNDT5JUMOATGCT22JQ"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colN3UZ2Z4S27NRDISQAAAAAAAAAA"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdYRLSFFVV2ZFIJFFAT34VLBDB4Y"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdZSHM4TXE3XNRDISQAAAAAAAAAA"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHWP3QCET27NRDISQAAAAAAAAAA"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMWOGO4MS27NRDISQAAAAAAAAAA"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGBJZDIUS27NRDISQAAAAAAAAAA"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQKCERAMS27NRDISQAAAAAAAAAA"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colYWL4NXUS27NRDISQAAAAAAAAAA"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colC46NAASN3PNRDISQAAAAAAAAAA"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEZ4SH34S27NRDISQAAAAAAAAAA"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQP4LKSN3IFHITPAQXU7J72H6FA"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBLPIOJNMDNGL7PKJ3NJX4ZDNLU"))
		};
	}
	@Override
	protected org.radixware.kernel.common.client.models.Model createModelImpl(org.radixware.kernel.common.client.IClientEnvironment userSession) {    return new org.radixware.ads.Testing.explorer.TestCase.DefaultGroupModel(userSession,this);
	}
}
/* Radix::Testing::TestCase:By Class:Model - Desktop Executable*/

/*Radix::Testing::TestCase:By Class:Model-Filter Model Class*/

package org.radixware.ads.Testing.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:By Class:Model")
public class By Class:Model  extends org.radixware.kernel.common.client.models.FilterModel  {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return By Class:Model_mi.rdxMeta; }



	public By Class:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.filters.RadFilterDef def){super(userSession,def);}

	/*Radix::Testing::TestCase:By Class:Model:Nested classes-Nested Classes*/

	/*Radix::Testing::TestCase:By Class:Model:Properties-Properties*/








	/*Radix::Testing::TestCase:By Class:Model:Methods-Methods*/

	/*Radix::Testing::TestCase:By Class:Model:beforeOpenPropEditorDialog-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:By Class:Model:beforeOpenPropEditorDialog")
	public published  void beforeOpenPropEditorDialog (org.radixware.kernel.common.client.models.items.properties.Property property, org.radixware.kernel.common.client.models.PropEditorModel propEditorModel) {
		if (property.getId().equals(idof[TestCase:By Class:classGuid])) {
		    // dlg = new (Environment);
		    Common.Dlg::ClassChoiceDlg:Model model = (Common.Dlg::ClassChoiceDlg:Model) propEditorModel;

		    Common::CommonXsd:ClassRestriction restr = Common::CommonXsd:ClassRestriction.Factory.newInstance();
		    restr.addNewSuperClass().DacId = idof[TestCase];
		    restr.Deprecated = false;

		    model.parClassRestriction = restr;
		    model.parTitle = classGuid.Title;
		} else {
		    super.beforeOpenPropEditorDialog(property, propEditorModel);
		}

	}

	/*Radix::Testing::TestCase:By Class:Model:getDisplayString-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:By Class:Model:getDisplayString")
	public published  Str getDisplayString (org.radixware.kernel.common.types.Id propertyId, java.lang.Object propertyValue, Str defaultDisplayString, boolean isInherited) {
		if (propertyId.equals(idof[TestCase:By Class:classGuid])) {
		    
		    Str classGuidStr = (Str)propertyValue;
		    if (classGuidStr == null) {
		        return "<not defined>";
		    }
		    return getEnvironment().getDefManager().getClassPresentationDef(Types::Id.Factory.loadFrom(classGuidStr)).getObjectTitle();
		}
		return super.getDisplayString(propertyId, propertyValue, defaultDisplayString, isInherited);
	}

	/*Radix::Testing::TestCase:By Class:classGuid:classGuid-Presentation Property*/




	public class ClassGuid extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public ClassGuid(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:By Class:classGuid:classGuid")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:By Class:classGuid:classGuid")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ClassGuid getClassGuid(){return (ClassGuid)getProperty(prmA3FXJ53QVZDVTLHFCPTSUYRD2I);}


}

/* Radix::Testing::TestCase:By Class:Model - Desktop Meta*/

/*Radix::Testing::TestCase:By Class:Model-Filter Model Class*/

package org.radixware.ads.Testing.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class By Class:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("fmcBQPIEXVBZRES3ICWCEACXNIKKI"),
						"By Class:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::Testing::TestCase:By Class:Model:Properties-Properties*/
						null,
						null,
						null,
						null,
						null,
						null,
						false,
						false,
						false
						);
}

/* Radix::Testing::TestCase:By Class:Model - Web Executable*/

/*Radix::Testing::TestCase:By Class:Model-Filter Model Class*/

package org.radixware.ads.Testing.web;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:By Class:Model")
public class By Class:Model  extends org.radixware.kernel.common.client.models.FilterModel  {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return By Class:Model_mi.rdxMeta; }



	public By Class:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.filters.RadFilterDef def){super(userSession,def);}

	/*Radix::Testing::TestCase:By Class:Model:Nested classes-Nested Classes*/

	/*Radix::Testing::TestCase:By Class:Model:Properties-Properties*/








	/*Radix::Testing::TestCase:By Class:Model:Methods-Methods*/

	/*Radix::Testing::TestCase:By Class:Model:beforeOpenPropEditorDialog-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:By Class:Model:beforeOpenPropEditorDialog")
	public published  void beforeOpenPropEditorDialog (org.radixware.kernel.common.client.models.items.properties.Property property, org.radixware.kernel.common.client.models.PropEditorModel propEditorModel) {

	}

	/*Radix::Testing::TestCase:By Class:Model:getDisplayString-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:By Class:Model:getDisplayString")
	public published  Str getDisplayString (org.radixware.kernel.common.types.Id propertyId, java.lang.Object propertyValue, Str defaultDisplayString, boolean isInherited) {
		if (propertyId.equals(idof[TestCase:By Class:classGuid])) {
		    
		    Str classGuidStr = (Str)propertyValue;
		    if (classGuidStr == null) {
		        return "<not defined>";
		    }
		    return getEnvironment().getDefManager().getClassPresentationDef(Types::Id.Factory.loadFrom(classGuidStr)).getObjectTitle();
		}
		return super.getDisplayString(propertyId, propertyValue, defaultDisplayString, isInherited);
	}

	/*Radix::Testing::TestCase:By Class:classGuid:classGuid-Presentation Property*/




	public class ClassGuid extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public ClassGuid(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:By Class:classGuid:classGuid")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase:By Class:classGuid:classGuid")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ClassGuid getClassGuid(){return (ClassGuid)getProperty(prmA3FXJ53QVZDVTLHFCPTSUYRD2I);}


}

/* Radix::Testing::TestCase:By Class:Model - Web Meta*/

/*Radix::Testing::TestCase:By Class:Model-Filter Model Class*/

package org.radixware.ads.Testing.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class By Class:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("fmcBQPIEXVBZRES3ICWCEACXNIKKI"),
						"By Class:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::Testing::TestCase:By Class:Model:Properties-Properties*/
						null,
						null,
						null,
						null,
						null,
						null,
						false,
						false,
						false
						);
}

/* Radix::Testing::TestCase - Localizing Bundle */
package org.radixware.ads.Testing.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class TestCase - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Import");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Импортировать");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2CDE6W3JQFCDNDLFHL3SGS4AHQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Last successfully executed on IS");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Дата последнего успешного выполнения на СИ");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2QR2LMMP4VEDZFCZQSCZRZEC2M"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Test Cases");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Тесты");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5JSBTLM2ZNFD5PTMQNEFE3UKV4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"By operator");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"По исполнителю");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAUV36YGJI5DTFG2E5WYN54UINQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Integration Server (IS)");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Сервер интеграции (СИ)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsB5R27FFIFJHNTMVZVYMJHRFETE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Class");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Класс");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsB766TPV46NHENJOHOSAVUJ7C3A"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Responsible person");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ответственный пользователь");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBDSC6QRKOZF33C2U7PFDJLEPHM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Test Case");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Тест");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBJFDUM24OXOBDFKUAAMPGXUWTQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Run on IS");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Выполнять на СИ");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBLPIOJNMDNGL7PKJ3NJX4ZDNLU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Test Cases");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Тесты");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBNFDUM24OXOBDFKUAAMPGXUWTQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Not correct references values: own parent test case [guid=%s] and parent test case inherited from group [guid=%s] are different.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Некорректное значение ссылок: собственное значение родительского теста [guid=%s] и родительский тест унаследованный от группы [guid=%s] не совпадают.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBVU66A43VFEJFMSYJRGOQJW77U"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Sequence number");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Порядковый номер");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsC6YFHCMLMRGT3II3DDLRF6YCI4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Export Test");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Экспортировать тест");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsD2EAAQ6WEVHT3A45EW7AVIY2W4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Parent test case was changed from \'%s\' to \'%s\'");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Родительский элемент для теста был изменен с \'%s\' на \'%s\'");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEBTMPXUFGZE73FTALAWQLDHB2E"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Class name");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Имя класса");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEMSKREJXYRBARPN3PY5NLBLPC4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Responsible person");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ответственный пользователь");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsESLW3G2YARA4HJQG6XW5E54LQE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"By class");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"По классу");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEWLIOPBOFJG2DFUJXWYDMP343Q"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<not defined>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<не задан>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsF4F5OEHUEFBKXHDFWHK55KW2C4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"By start time");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"По времени начала");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGYMZYCC66FAHHMX22GMCEPFNR4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"MSDL");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"MSDL");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsH3BATTPRYFEHTNA527MATBRRZI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"CPU (ns)");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"CPU (ns)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHZCZSIIAXVEJTN2KFUA2GNBPB4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"System");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Система");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsIVTUVFR755A7VFN4X2RN3HKEXM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Running test...");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Выполнение теста...");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJDIXDFTASNDT5AU2NBEN6FMUZY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"User-Defined Functions");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Пользовательские функции");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJF3XOOHOCZDH7HQREVBFFJY7M4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Duration (Total/CPU/DB/EXT)");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Длительность (Общая/CPU/DB/EXT)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLSYXL7ZSXBF4RO23EKYZICMQU4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Test case group was changed from \'%s\' to \'%s\'");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Группа для теста была изменена с \'%s\' на \'%s\'");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMDSWWX36V5DTFLVR6D5XRD2TSE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"ESB");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"ESB");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMGAL6SE6TFBZVCQRNTLVJOYN4E"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"No");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"№");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMNQIG6HQIRARFD224IAWTHH6BQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Parent case ID");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ид. родительского теста ");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsN5FDUM24OXOBDFKUAAMPGXUWTQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Result");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Результат");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNBFDUM24OXOBDFKUAAMPGXUWTQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Completed");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Время завершения");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNFFDUM24OXOBDFKUAAMPGXUWTQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Trace profile");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Профиль трассировки");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNJFDUM24OXOBDFKUAAMPGXUWTQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"ID");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ид.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNNFDUM24OXOBDFKUAAMPGXUWTQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Operator");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Исполнитель");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNRFDUM24OXOBDFKUAAMPGXUWTQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Notes");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Примечания");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNVFDUM24OXOBDFKUAAMPGXUWTQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Started");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Время начала");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNZFDUM24OXOBDFKUAAMPGXUWTQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Result description");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Описание результата");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOBFDUM24OXOBDFKUAAMPGXUWTQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Event log contains a record with severity equal or higher than ");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"В журнале событий была запись с уровнем значимости выше, чем ");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOUN4PTPBSBHA3KXZGLBU6G3PNM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"ACT (ns)");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"ACT (ns)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOYUI5F4WVZFAJHRHEQHTSDURZU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"General");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Общее");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPFFDUM24OXOBDFKUAAMPGXUWTQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Parameters");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Параметры");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPJFDUM24OXOBDFKUAAMPGXUWTQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"By ID descending");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"По убыванию ид.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQVFDUM24OXOBDFKUAAMPGXUWTQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Rerun");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Выполнить заново");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQZFDUM24OXOBDFKUAAMPGXUWTQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Active");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Активен");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsS6CNAK6UGVHUZOK2UKXFMVTW6Q"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"DB (ns)");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"DB (ns)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsSZXNAOXR6BFFTLS6IOEVLYYT7E"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Change Test Case Group Membership Settings");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Изменить параметры принадлежности теста к группе");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsT2UNPM4G5RB3LIB3OR5XY7V7O4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Misc");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Разное");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUU7MCHSFMBDPFC7Y6HLT3KGY3M"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Email for notifications");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Email для оповещения");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVKBIY5VZYZC2TOUJDYOKIVVTXA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Last executed on IS");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Дата последнего выполнения на СИ");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWGQI2LNWCRCZBBTIBBE3LTDVAE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"EXT (ns)");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"EXT (ns)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXK62QTDRFZBABPMJQ2NYOEIV5E"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Number of successive failures on IS");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Количество неуспешных выполнений подряд на СИ");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXMNVUVSRWVHARIGIRJC2Q5RRCY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Member of Group");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Входит в группу");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXUGTSHLNLZAZ5BUI7QF47DGFXE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Personal Communications");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Персональные коммуникации");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYODR5A2C55CLRJUJPPVR6L4EOI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(TestCase - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbaecHW4OSVMS27NRDISQAAAAAAAAAA"),"TestCase - Localizing Bundle",$$$items$$$);
}
