
/* Radix::Reports::ReportPubList - Server Executable*/

/*Radix::Reports::ReportPubList-Entity Class*/

package org.radixware.ads.Reports.server;

import java.util.*;


@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPubList")
public abstract published class ReportPubList  extends org.radixware.ads.Types.server.Entity  implements org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {

	private static class Context {

	    private final ReportPubList pubList; // can be null, if not created for current context
	    private final Context parent;
	    private List<ReportPubTopic> topics = null;
	    private List<ReportPub> pubs = null;
	    private Map<Int, ReportPubTopic> id2Topic = null;
	    private Map<Int, ReportPub> id2Pub = null;
	    private String location = null;

	    public Context(Types::Id pubListClassId, ReportsXsd:ReportPubContext xContext) {
	        this.pubList = ReportPubList.choose(pubListClassId, xContext.PubContextClassId, xContext.PubContextId);

	        if (xContext.isSetParent()) {
	            this.parent = new Context(pubListClassId, xContext.getParent());
	        } else {
	            this.parent = null;
	        }
	    }

	    public ReportPubList getPubList() {
	        return pubList;
	    }

	    public String getLocation() {
	        if (location == null) {
	            if (pubList != null) {
	                if (pubList.contextPid != null) {
	                    final Types::Entity entity = pubList.getPubContext();
	                    if (entity != null) {
	                        location = "Context" + ": " + entity.getClassDefinitionTitle() + " '" + entity.calcTitle() + "'";
	                    } else {
	                        location = "";
	                    }
	                } else {
	                    location = "Context: Common Reports";
	                }
	                location += "\n" + "Publication list" + ": " + pubList.id + " - " + pubList.getClassDefinitionTitle();
	            } else {
	                location = "";
	            }
	        }
	        return location;
	    }

	    public List<ReportPubTopic> getTopics() {
	        if (pubList == null) {
	            return Collections.emptyList();
	        } else {
	            if (topics == null) {
	                topics = new ArrayList<ReportPubTopic>();
	                final Reports.Db::ReportPubTopicCursor cursor = Reports.Db::ReportPubTopicCursor.open(pubList);
	                try {
	                    while (cursor.next()) {
	                        topics.add(cursor.topic);
	                    }
	                } finally {
	                    cursor.close();
	                }
	            }
	            return topics;
	        }
	    }

	    public List<ReportPub> getPubs() {
	        if (pubList == null) {
	            return Collections.emptyList();
	        } else {
	            if (pubs == null) {
	                pubs = new ArrayList<ReportPub>();
	                final Reports.Db::ReportPubCursor cursor = Reports.Db::ReportPubCursor.open(pubList);
	                try {
	                    while (cursor.next()) {
	                        pubs.add(cursor.reportPub);
	                    }
	                } finally {
	                    cursor.close();
	                }
	            }
	            return pubs;
	        }
	    }

	    public Context getParentContext() {
	        return parent;
	    }

	    public Context findFirstContext() {
	        for (Context context = this; context != null; context = context.getParentContext()) {
	            if (context.getPubList() != null) {
	                return context;
	            }
	        }
	        return null;
	    }

	    public ReportPubTopic getTopicById(Int topicId) {
	        if (id2Topic == null) {
	            id2Topic = new HashMap<Int, ReportPubTopic>();
	            for (ReportPubTopic topic : getTopics()) {
	                id2Topic.put(topic.id, topic);
	            }
	        }
	        return id2Topic.get(topicId);
	    }

	    public ReportPub getPubById(Int pubId) {
	        if (id2Pub == null) {
	            id2Pub = new HashMap<Int, ReportPub>();
	            for (ReportPub pub : getPubs()) {
	                id2Pub.put(pub.id, pub);
	            }
	        }
	        return id2Pub.get(pubId);
	    }

	    public ReportPubTopic getFinalTopic(ReportPubTopic topic) {
	        final Int inheritedTopicId = topic.inheritedTopicId;
	        if (inheritedTopicId == null) {
	            return topic;
	        }
	        for (Context context = getParentContext(); context != null; context = context.getParentContext()) {
	            final ReportPubTopic result = context.getTopicById(inheritedTopicId);
	            if (result != null) {
	                return result;
	            }
	        }
	        return null;
	    }

	    public ReportPub getFinalPub(ReportPub pub) {
	        final Int inheritedPubId = pub.inheritedPubId;
	        if (inheritedPubId == null) {
	            return pub;
	        }
	        for (Context context = getParentContext(); context != null; context = context.getParentContext()) {
	            final ReportPub result = context.getPubById(inheritedPubId);
	            if (result != null) {
	                return result;
	            }
	        }
	        return null;
	    }

	    public Context getFinalContext(ReportPub pub) {
	        final Int inheritedPubId = pub.inheritedPubId;
	        if (inheritedPubId == null) {
	            return this;
	        }
	        for (Context context = getParentContext(); context != null; context = context.getParentContext()) {
	            final ReportPub result = context.getPubById(inheritedPubId);
	            if (result != null) {
	                return context;
	            }
	        }
	        return null;
	    }

	    public Context getFinalContext(ReportPubTopic topic) {
	        final Int inheritedTopicId = topic.inheritedTopicId;
	        if (inheritedTopicId == null) {
	            return this;
	        }
	        for (Context context = getParentContext(); context != null; context = context.getParentContext()) {
	            final ReportPubTopic result = context.getTopicById(inheritedTopicId);
	            if (result != null) {
	                return context;
	            }
	        }
	        return null;
	    }
	}

	private static enum EMode {

	    FOR_EXECUTE,
	    FOR_INHERIT,
	    FOR_CUSTOMER
	}

	/**
	 * Build tree to choose report publication: expand inheritance.
	 */
	private static class TreeBuilder {

	    private final Types::Entity contextObject;
	    private final Context context;
	    private final EMode mode;

	    public TreeBuilder(Context context, EMode mode, Types::Entity contextObject) {
	        this.context = context;
	        this.mode = mode;
	        this.contextObject = contextObject;
	    }

	    private static String getLocation(Context context, ReportPub pub) {
	        String location = context.getLocation();
	        if (!location.isEmpty()) {
	            location += "\n";
	        }
	        location += "Publication" + ": " + pub.id + " - " + pub.finalTitle;
	        if (pub.reportFullClassName != null && !pub.reportFullClassName.isEmpty()) {            
	            location += "\n" + "Report class: " + pub.reportFullClassName;
	        }
	        return location;
	    }

	    private static String getLocation(Context context, ReportPubTopic topic) {
	        String location = context.getLocation();
	        if (!location.isEmpty()) {
	            location += "\n";
	        }
	        location += "Topic" + ": " + topic.id + " - " + topic.title;
	        return location;
	    }

	    private void addTopic(final ReportsXsd:ReportPubTopic xParentTopic, ReportPubTopic topic, Context context) {
	        final ReportPubTopic finalTopic = context.getFinalTopic(topic);
	        final Context finalContext = (finalTopic != null ? context.getFinalContext(topic) : null);

	        if (finalTopic != null) {
	            final ReportsXsd:ReportPubTopic xTopic = (xParentTopic.isSetTopics() ? xParentTopic.getTopics().addNewTopic() : xParentTopic.addNewTopics().addNewTopic());
	            if (mode == EMode.FOR_INHERIT) { // topic can be selected only for inheritance
	                xTopic.Pid = finalTopic.getPid().toString();
	            }

	            xTopic.Title = topic.title; // not finalTopic, because topic title is not inherited
	            xTopic.Description = topic.description;
	            xTopic.Location = getLocation(finalContext, topic);

	            if (finalContext != null) {
	                for (ReportPubTopic subTopic : finalContext.getTopics()) {
	                    if (subTopic.parentTopicId == finalTopic.id) {
	                        addTopic(xTopic, subTopic, finalContext);
	                    }
	                }

	                for (ReportPub pub : finalContext.getPubs()) {
	                    if (pub.parentTopicId == finalTopic.id) {
	                        addPub(xTopic, pub);
	                    }
	                }
	            }
	        }
	    }

	    private void addPub(final ReportsXsd:ReportPubTopic xParentTopic, ReportPub pub) {
	        final ReportPub finalPub = context.getFinalPub(pub);
	        if (finalPub != null) {
	            if ((mode == EMode.FOR_EXECUTE || mode == EMode.FOR_CUSTOMER) && !finalPub.isEnabled.booleanValue()) {
	                Arte::Trace.debug("Report publication #" + finalPub.id + " skipped: disabled", Arte::EventSource:ArteReports);
	                return;
	            }

	            // check context
	            if ((mode == EMode.FOR_EXECUTE || mode == EMode.FOR_CUSTOMER) && !finalPub.check(contextObject, false)) {
	                Arte::Trace.debug("Report publication #" + finalPub.id + " skipped: condition failed", Arte::EventSource:ArteReports);
	                return;
	            }

	            // check role
	            if (mode == EMode.FOR_EXECUTE && finalPub.userRoleGuids != null) {
	                ArrStr chiefAdminRole = new ArrStr();
	                chiefAdminRole.add(idof[Arte::SuperAdmin].toString());
	                
	                if (!Acs::AcsUtils.isCurUserHasRole(finalPub.userRoleGuids) && !Acs::AcsUtils.isCurUserHasRole(chiefAdminRole)) {
	                    Arte::Trace.debug("Report publication #" + finalPub.id + " skipped: access denied", Arte::EventSource:ArteReports);
	                    return;
	                }
	            }

	            // check role
	            if (mode == EMode.FOR_CUSTOMER && finalPub.isForCustomer != true) {
	                Arte::Trace.debug("Report publication #" + finalPub.id + " skipped: not for customer", Arte::EventSource:ArteReports);
	                return;
	            }

	            // check parameter values in publication for suitability to report
	            if ((mode == EMode.FOR_EXECUTE || mode == EMode.FOR_CUSTOMER) && !finalPub.checkSuitability(contextObject)) {
	                //.("Report publication #"+finalPub.+" skipped: suitability failed.", );
	                return;
	            }

	            final ReportsXsd:ReportPub xPub = (xParentTopic.isSetPubs() ? xParentTopic.getPubs().addNewPub() : xParentTopic.addNewPubs().addNewPub());
	            xPub.Pid = finalPub.getPid().toString();
	            xPub.ReportClassId = Types::Id.Factory.loadFrom(finalPub.reportClassGuid);
	            xPub.Title = finalPub.finalTitle;
	            xPub.Description = finalPub.description;
	            if (finalPub.exportFormats != null && !finalPub.exportFormats.isEmpty()) {
	                xPub.SupportedFormats = new ArrayList<ReportExportFormat>(finalPub.exportFormats);
	            }
	            if (mode != EMode.FOR_CUSTOMER) {
	                xPub.Location = getLocation(context.getFinalContext(pub), pub);
	                xPub.ReportClassName = pub.reportClassName;
	            }
	            if (mode == EMode.FOR_CUSTOMER) {
	                Types::Id contextParamId = null;
	                try {
	                    Types::Report report = ReportsServerUtils.instantiateReportByClassId(xPub.ReportClassId);
	                    contextParamId = report.ContextParameterId;
	                } catch (Throwable e) {
	                    ;
	                }
	                ReportsXsd:ParametersBindingType bindings = finalPub.getParametersBinding();
	                xPub.addNewParams();
	                for (Meta::PropDef propDef : Meta::Utils.getClassProperties(xPub.ReportClassId)) {
	                    if (propDef.Id.getPrefix() != Meta::DefinitionIdPrefix:PARAMETER
	                            || propDef.Id == contextParamId)
	                        continue;
	                    org.radixware.kernel.server.meta.presentations.RadClassPresentationDef classPresentation = Meta::Utils.getClassDef(xPub.ReportClassId).getPresentation();
	                    if (classPresentation == null) {
	                        continue;
	                    }
	                    org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef pres = classPresentation.getPropPresById(propDef.Id);
	                    if (pres == null) //нередактируемый
	                        continue;
	                    
	                    boolean isFinal = false;
	                    Arte::EasXsd:Property defaulVal = null;
	                    if (bindings != null)
	                        for (ReportsXsd:ParametersBindingType.ParameterBinding b : bindings.ParameterBindingList) {
	                            if (b.ParameterId == propDef.Id) {
	                                isFinal = b.Final == true || b.Restriction != ReportParameterRestriction:NONE;
	                                defaulVal = b.Value;
	                                break;
	                            }
	                        }
	                    if (isFinal)
	                        continue;
	                    ReportsXsd:ReportPub.Params.Param par = xPub.Params.addNewParam();
	                    par.Id = propDef.Id;
	                    par.setIsMandatory(pres.isIsNotNull(classPresentation));
	                    par.Type = propDef.ValType;
	                    par.Name = propDef.Name;
	                    par.Title = propDef.Title;
	                    par.DefaultVal = defaulVal;
	                }
	                if (bindings != null) { //sort parameters by binding order
	                    for (ReportsXsd:ParametersBindingType.ParameterBinding b : bindings.ParameterBindingList) {
	                        for (ReportsXsd:ReportPub.Params.Param p : xPub.Params.ParamList) {
	                            if (p.Id == b.ParameterId) { //move to the end
	                                xPub.Params.ParamList.add(p);
	                                xPub.Params.ParamList.remove(p);
	                                break;
	                            }
	                        }
	                    }
	                }
	            }
	        } else {
	            Arte::Trace.warning("Inherited report publication not found for pubjication #" + pub.id, Arte::EventSource:ArteReports);
	        }
	    }

	    public void buildTree(ReportsXsd:ReportPubTopic xRootTopic) {
	        // add root topics
	        for (ReportPubTopic topic : context.getTopics()) {
	            if (topic.parentTopicId == null) { // is root
	                addTopic(xRootTopic, topic, context);
	            }
	        }

	        // add root publications
	        for (ReportPub pub : context.getPubs()) {
	            if (pub.parentTopicId == null) { // is root
	                addPub(xRootTopic, pub);
	            }
	        }
	    }
	}

	private static void buildTreeList(EMode mode, Types::Id pubListClassId, ReportsXsd:ReportPubContext xContext, ReportsXsd:ReportPubTopic xRoot) {
	    final Context context = new Context(pubListClassId, xContext).findFirstContext();

	    final Types::Entity contextObject;
	    if ((mode == EMode.FOR_EXECUTE || mode == EMode.FOR_CUSTOMER) && context != null) {
	        final ReportPubList pubList = context.getPubList();
	        contextObject = pubList.getContextObject(xContext.PubContextId);
	    } else {
	        contextObject = null;
	    }

	    if (context != null) {
	        final TreeBuilder treeBuilder = new TreeBuilder(context, mode, contextObject);
	        treeBuilder.buildTree(xRoot);
	    }
	}

	/**Metainformation accessor method*/
	@SuppressWarnings("unused")
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return ReportPubList_mi.rdxMeta;}

	/*Radix::Reports::ReportPubList:Nested classes-Nested Classes*/

	/*Radix::Reports::ReportPubList:Properties-Properties*/

	/*Radix::Reports::ReportPubList:classGuid-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPubList:classGuid")
	public published  Str getClassGuid() {
		return classGuid;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPubList:classGuid")
	public published   void setClassGuid(Str val) {
		classGuid = val;
	}

	/*Radix::Reports::ReportPubList:id-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPubList:id")
	public published  Int getId() {
		return id;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPubList:id")
	public published   void setId(Int val) {
		id = val;
	}

	/*Radix::Reports::ReportPubList:lastUpdateTime-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPubList:lastUpdateTime")
	public published  java.sql.Timestamp getLastUpdateTime() {
		return lastUpdateTime;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPubList:lastUpdateTime")
	public published   void setLastUpdateTime(java.sql.Timestamp val) {
		lastUpdateTime = val;
	}

	/*Radix::Reports::ReportPubList:lastUpdateUserName-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPubList:lastUpdateUserName")
	public published  Str getLastUpdateUserName() {
		return lastUpdateUserName;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPubList:lastUpdateUserName")
	public published   void setLastUpdateUserName(Str val) {
		lastUpdateUserName = val;
	}

	/*Radix::Reports::ReportPubList:contextPid-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPubList:contextPid")
	public published  Str getContextPid() {
		return contextPid;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPubList:contextPid")
	public published   void setContextPid(Str val) {
		contextPid = val;
	}

	/*Radix::Reports::ReportPubList:pubContextClassGuid-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPubList:pubContextClassGuid")
	public published  Str getPubContextClassGuid() {
		return pubContextClassGuid;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPubList:pubContextClassGuid")
	public published   void setPubContextClassGuid(Str val) {
		pubContextClassGuid = val;
	}

	/*Radix::Reports::ReportPubList:className-Dynamic Property*/



	protected Str className=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPubList:className")
	public published  Str getClassName() {

		return this.getClassDefinitionTitle();
		//return .(.loadFrom());

	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPubList:className")
	public published   void setClassName(Str val) {
		className = val;
	}

	/*Radix::Reports::ReportPubList:pubContext-Dynamic Property*/



	protected org.radixware.ads.Types.server.Entity pubContext=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPubList:pubContext")
	private final  org.radixware.ads.Types.server.Entity getPubContext() {
		return pubContext;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPubList:pubContext")
	private final   void setPubContext(org.radixware.ads.Types.server.Entity val) {
		pubContext = val;
	}













































































	/*Radix::Reports::ReportPubList:Methods-Methods*/

	/*Radix::Reports::ReportPubList:onChanged-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPubList:onChanged")
	protected published  void onChanged () {
		lastUpdateTime = Arte::Arte.getCurrentTime();
		lastUpdateUserName = Arte::Arte.getUserName();

	}

	/*Radix::Reports::ReportPubList:beforeCreate-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPubList:beforeCreate")
	protected published  boolean beforeCreate (org.radixware.kernel.server.types.Entity src) {
		onChanged();
		return super.beforeCreate(src);
	}

	/*Radix::Reports::ReportPubList:beforeUpdate-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPubList:beforeUpdate")
	protected published  boolean beforeUpdate () {
		onChanged();
		return super.beforeUpdate();
	}

	/*Radix::Reports::ReportPubList:choose-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPubList:choose")
	public static published  org.radixware.ads.Reports.server.ReportPubList choose (org.radixware.kernel.common.types.Id pubListClassId, org.radixware.kernel.common.types.Id pubContextClassId, Str pubContextId) {
		final Reports.Db::ReportPubListCursor cursor = Reports.Db::ReportPubListCursor.open(
		        (pubListClassId != null ? pubListClassId.toString() : null),
		        (pubContextClassId != null ? pubContextClassId.toString() : null),
		        pubContextId);

		try {
		    if (cursor.next()) {
		        return cursor.pubList;
		    } else {
		        return null;
		    }
		} finally {
		    cursor.close();
		}

	}

	/*Radix::Reports::ReportPubList:buildTreeListForInherit-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPubList:buildTreeListForInherit")
	public static published  void buildTreeListForInherit (org.radixware.kernel.common.types.Id pubListClassId, org.radixware.schemas.reports.ReportPubContext xContext, org.radixware.schemas.reports.ReportPubTopic xRoot) {
		buildTreeList(EMode.FOR_INHERIT, pubListClassId, xContext, xRoot); // see body
	}

	/*Radix::Reports::ReportPubList:getContextObject-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPubList:getContextObject")
	public abstract published  org.radixware.ads.Types.server.Entity getContextObject (Str contextId);

	/*Radix::Reports::ReportPubList:getPubContext-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPubList:getPubContext")
	public published  org.radixware.ads.Types.server.Entity getPubContext () {
		if (this.contextPid==null)
		    return null;

		final Types::Id classId = Types::Id.Factory.loadFrom(this.pubContextClassGuid);
		final Types::Id entityId = Arte::Arte.getDefManager().getClassEntityId(classId);
		final Types::Pid contextPid = new Pid(Arte::Arte.getInstance(), entityId, this.contextPid);
		final Types::Entity entity = Arte::Arte.getEntityObject(contextPid);
		return entity;
	}

	/*Radix::Reports::ReportPubList:buildTreeListForExecute-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPubList:buildTreeListForExecute")
	public static published  void buildTreeListForExecute (org.radixware.kernel.common.types.Id pubListClassId, org.radixware.schemas.reports.ReportPubContext xContext, org.radixware.schemas.reports.ReportPubTopic xRoot) {
		buildTreeList(EMode.FOR_EXECUTE, pubListClassId, xContext, xRoot); // see body

		//buildTreeList(EMode.FOR_CUSTOMER, pubListClassId, xContext, xRoot); // see body
	}

	/*Radix::Reports::ReportPubList:buildTreeListForCustomer-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPubList:buildTreeListForCustomer")
	public static published  void buildTreeListForCustomer (org.radixware.kernel.common.types.Id pubListClassId, org.radixware.schemas.reports.ReportPubContext xContext, org.radixware.schemas.reports.ReportPubTopic xRoot) {
		buildTreeList(EMode.FOR_CUSTOMER, pubListClassId, xContext, xRoot); // see body
	}


	@Override
	public org.radixware.kernel.server.types.FormHandler.NextDialogsRequest execCommand(org.radixware.kernel.common.types.Id cmdId, org.radixware.kernel.common.types.Id propId, org.apache.xmlbeans.XmlObject input, org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValsById, org.apache.xmlbeans.XmlObject output) throws org.radixware.kernel.common.exceptions.AppException,java.lang.InterruptedException{

			return super.execCommand(cmdId,propId,input,newPropValsById,output);
	}


}

/* Radix::Reports::ReportPubList - Server Meta*/

/*Radix::Reports::ReportPubList-Entity Class*/

package org.radixware.ads.Reports.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class ReportPubList_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecQ5T72ARKKBHHNBHOH3LPATQA6M"),"ReportPubList",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsE7D6ECCLNVEDJNUL5LO5N7SMPY"),

						org.radixware.kernel.common.enums.EClassType.ENTITY,

						/*Radix::Reports::ReportPubList:Presentations-Entity Presentations*/
						/*Class presentation attributes*/
						 new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef(/*Owner Class Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("aecQ5T72ARKKBHHNBHOH3LPATQA6M"),
							/*Owner Class Name*/
							"ReportPubList",
							/*Title Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsE7D6ECCLNVEDJNUL5LO5N7SMPY"),
							/*Property presentations*/

							/*Radix::Reports::ReportPubList:Properties-Properties*/
							new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef[]{

									/*Radix::Reports::ReportPubList:classGuid:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colYKEM5Q2LR5BORNW7UDQGGHK7BU"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Reports::ReportPubList:id:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQ3B6R3LVVBGDZFTHVOYGQXIGW4"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Reports::ReportPubList:lastUpdateTime:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colLZJGEHVT2ZGKTBYIF7MQCY6FMA"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Reports::ReportPubList:lastUpdateUserName:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3HPYVDGDE5HGJN2DFHFEN354VQ"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Reports::ReportPubList:contextPid:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colULDE4U54JZHEFOCELCQKPNYX6M"),org.radixware.kernel.common.enums.EEditPossibility.PROGRAMMATICALLY,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Reports::ReportPubList:pubContextClassGuid:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colWZ4H7UJ6RFA6FMTXPZBFRNV52M"),org.radixware.kernel.common.enums.EEditPossibility.PROGRAMMATICALLY,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Reports::ReportPubList:className:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdBEDPSBGZ5RA6ZHGFNAMNREERSU"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT))
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
									/*Radix::Reports::ReportPubList:Common-Editor Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprSMKGLY4AKBDWRB4XEFTYEFZKUA"),
									"Common",
									org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
									null,
									2192,
									null,

									/*Radix::Reports::ReportPubList:Common:Children-Explorer Items*/
										new org.radixware.kernel.server.meta.presentations.RadExplorerItemDef[]{

												/*Radix::Reports::ReportPubList:Common:ReportPubTopic-Child Ref Explorer Item*/

												new org.radixware.kernel.server.meta.presentations.RadChildRefExplorerItemDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiX2G7FDSYMBF2HO3EIFDP45JDBY"),
													org.radixware.kernel.common.types.Id.Factory.loadFrom("aecQ5T72ARKKBHHNBHOH3LPATQA6M"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aec34F7JPGYHJEJHBCE7ISQKJN3IY"),
													org.radixware.kernel.common.types.Id.Factory.loadFrom("sprYMAA3X6RZFFI7IACEO42ZEEGOI"),
													new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),
													org.radixware.kernel.common.types.Id.Factory.loadFrom("refXPXHRZKMBVBGNPKHUIH3HU4J64"),
													null,
													null),

												/*Radix::Reports::ReportPubList:Common:ReportPub-Child Ref Explorer Item*/

												new org.radixware.kernel.server.meta.presentations.RadChildRefExplorerItemDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiT5VMZMVDNNCK3OTTBHF726ZNTI"),
													org.radixware.kernel.common.types.Id.Factory.loadFrom("aecQ5T72ARKKBHHNBHOH3LPATQA6M"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecUNC67IF5OBCX7NOPNLQOCUG374"),
													org.radixware.kernel.common.types.Id.Factory.loadFrom("sprYYUTYV7EYRF4TEAHGYK5SWGV6E"),
													new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),
													org.radixware.kernel.common.types.Id.Factory.loadFrom("refJLB3T4LDYNGENHZH4FUXEMEU7E"),
													null,
													null)
										}
									,
									org.radixware.kernel.server.types.Restrictions.Factory.newInstance(0,null,null,null),
									(org.radixware.kernel.server.meta.presentations.RadPropertyPresentationAttributes[]) null,
									org.radixware.kernel.common.enums.EEditorPresentationRightsInheritanceMode.NONE,
									null,null)
							},
							/*Selector presentations*/
							new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef[]{

									new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef(
									/*Radix::Reports::ReportPubList:Common-Selector Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("spr7U6ZGF3DHBA4DOFJDLFXMQLCQY"),"Common",null,144,new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn[]{

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQ3B6R3LVVBGDZFTHVOYGQXIGW4"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colYKEM5Q2LR5BORNW7UDQGGHK7BU"),true)
									},new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.Addons(null,false,new org.radixware.kernel.common.types.Id[]{},false,null,false,new org.radixware.kernel.common.types.Id[]{},false,true,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprSMKGLY4AKBDWRB4XEFTYEFZKUA")},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprSMKGLY4AKBDWRB4XEFTYEFZKUA")},org.radixware.kernel.server.types.Restrictions.Factory.newInstance(0,null,null,null),null)
							},
							/*Default selector presentation Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("spr7U6ZGF3DHBA4DOFJDLFXMQLCQY"),
							/*Presentation Map*/
							null,
							/*Object title format*/

							/*Radix::Reports::ReportPubList:TitleFormat-Object Title Format*/

							new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecQ5T72ARKKBHHNBHOH3LPATQA6M"),new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.TitleItem[]{

									new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.TitleItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecQ5T72ARKKBHHNBHOH3LPATQA6M"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colQ3B6R3LVVBGDZFTHVOYGQXIGW4"),"{0}) ",org.radixware.kernel.common.enums.ETitleNullFormat.SAME_AS_NOT_NULL,null),

									new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.TitleItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecQ5T72ARKKBHHNBHOH3LPATQA6M"),org.radixware.kernel.common.types.Id.Factory.loadFrom("prdBEDPSBGZ5RA6ZHGFNAMNREERSU"),"{0}",org.radixware.kernel.common.enums.ETitleNullFormat.SAME_AS_NOT_NULL,null)
							},null,org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.DefinitionContextType.ENTITY),
							/*Class catalogs*/
							null,
							/*Restrictions*/
							org.radixware.kernel.server.types.Restrictions.Factory.newInstance(0,null,null,null),null,0),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblQ5T72ARKKBHHNBHOH3LPATQA6M"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("pdcEntity____________________"),

						/*Radix::Reports::ReportPubList:Properties-Properties*/
						new org.radixware.kernel.server.meta.clazzes.RadPropDef[]{

								/*Radix::Reports::ReportPubList:classGuid-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colYKEM5Q2LR5BORNW7UDQGGHK7BU"),"classGuid",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRBM6AY6U65B5LHTO2AB4MU3XIQ"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Reports::ReportPubList:id-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQ3B6R3LVVBGDZFTHVOYGQXIGW4"),"id",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVSCYON3XFFC4BAMJGCCVYOJRYA"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Reports::ReportPubList:lastUpdateTime-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colLZJGEHVT2ZGKTBYIF7MQCY6FMA"),"lastUpdateTime",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls43WTEOA4UFHWNOOLTPHR4DE3EI"),org.radixware.kernel.common.enums.EValType.DATE_TIME,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Reports::ReportPubList:lastUpdateUserName-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3HPYVDGDE5HGJN2DFHFEN354VQ"),"lastUpdateUserName",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMMBREP2FUJGE5LH4UPH75FI7ZU"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Reports::ReportPubList:contextPid-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colULDE4U54JZHEFOCELCQKPNYX6M"),"contextPid",null,org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Reports::ReportPubList:pubContextClassGuid-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colWZ4H7UJ6RFA6FMTXPZBFRNV52M"),"pubContextClassGuid",null,org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Reports::ReportPubList:className-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdBEDPSBGZ5RA6ZHGFNAMNREERSU"),"className",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsV7ASDZ6T75BQREBTDCDISUVPMI"),org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Reports::ReportPubList:pubContext-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdFR3WI6XRWFHRJCI5RMG4P4LONA"),"pubContext",null,org.radixware.kernel.common.enums.EValType.USER_CLASS,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE)
						},

						/*Radix::Reports::ReportPubList:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthVUVPHZBKFJCPVF6QZNY76LKN2E"),"onChanged",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthFMXUFM5J25E4TNM4PR73F6V3E4"),"beforeCreate",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("src",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprOIYLRP5S4FFCTL27BUUCDRSV4E"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthPT5PMP2WVZGUDB7OTLOE2SBWKE"),"beforeUpdate",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthCIT46Z65PFH55JJMTOBM4X36JI"),"choose",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("pubListClassId",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprFPHM2NB4SFHCBHCK4KO43D5BDE")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("pubContextClassId",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprH3NSREG4QRDD3K4IITLCSMTGUY")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("pubContextId",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprGM52DLWQ5VDCVKHAIGVLZUURRI"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth3YNHIP3HD5DERDDVXDIYPE6FNY"),"buildTreeListForInherit",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("pubListClassId",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr56RFDCGJKRBDHCI767J5OU6OLY")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("xContext",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprDKUF25A5QJHD3ABO4U4E4APDDQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("xRoot",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprEPHG7IFCUBEQJAXFBYIKJRZVEE"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthXF6J24NDWFH4DDXG4YMPQRFBWQ"),"getContextObject",false,true,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("contextId",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprGYZV5SLH75E37FUYCPL5KXDATM"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthEOMXQFUM4FD6FK4WU5CQE4HVTA"),"getPubContext",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthU2HIXEWPGZC4ZCHRHUJPO6KYTA"),"buildTreeListForExecute",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("pubListClassId",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprHN4IUMJR3JELTJJBB6SL4OLVSA")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("xContext",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprWV552QYFNVC5PK3JIIILMINCNA")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("xRoot",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr4VHKLZ2S5FFVNOI5ZVJ6NBOJDY"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth7ADB6GNSBNB6PPGRGBZC7KECHA"),"buildTreeListForCustomer",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("pubListClassId",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprQT6RKLHB55HZXBDTWR7ERRECE4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("xContext",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprYH3OECCDEVF3NCTKJ5R5FUNRUY")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("xRoot",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprUOF6VEVPSFDGNJKEQ6CD2AWZWE"))
								},null)
						},
						null,
						org.radixware.kernel.common.enums.EAccessAreaType.NONE,null,null,false);
}

/* Radix::Reports::ReportPubList - Desktop Executable*/

/*Radix::Reports::ReportPubList-Entity Class*/

package org.radixware.ads.Reports.explorer;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPubList")
public interface ReportPubList {
	/*Group model adapter class. This is the base class for 
	all of selector presentation models of this entity*/
	public static class DefaultGroupModel extends org.radixware.kernel.common.client.models.GroupModel{
		public DefaultGroupModel(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadSelectorPresentationDef def){super(userSession,def);}
		public org.radixware.ads.Reports.explorer.ReportPubList.ReportPubList_DefaultModel getEntity(int i) throws InterruptedException, org.radixware.kernel.common.client.exceptions.BrokenEntityObjectException, org.radixware.kernel.common.exceptions.ServiceClientException { return (org.radixware.ads.Reports.explorer.ReportPubList.ReportPubList_DefaultModel )  super.getEntity(i);}
	}

























































	/*Radix::Reports::ReportPubList:lastUpdateUserName:lastUpdateUserName-Presentation Property*/


	public class LastUpdateUserName extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public LastUpdateUserName(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPubList:lastUpdateUserName:lastUpdateUserName")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPubList:lastUpdateUserName:lastUpdateUserName")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public LastUpdateUserName getLastUpdateUserName();
	/*Radix::Reports::ReportPubList:lastUpdateTime:lastUpdateTime-Presentation Property*/


	public class LastUpdateTime extends org.radixware.kernel.common.client.models.items.properties.PropertyDateTime{
		public LastUpdateTime(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPubList:lastUpdateTime:lastUpdateTime")
		public  java.sql.Timestamp getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPubList:lastUpdateTime:lastUpdateTime")
		public   void setValue(java.sql.Timestamp val) {
			Value = val;
		}
	}
	public LastUpdateTime getLastUpdateTime();
	/*Radix::Reports::ReportPubList:id:id-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPubList:id:id")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPubList:id:id")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public Id getId();
	/*Radix::Reports::ReportPubList:contextPid:contextPid-Presentation Property*/


	public class ContextPid extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public ContextPid(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPubList:contextPid:contextPid")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPubList:contextPid:contextPid")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ContextPid getContextPid();
	/*Radix::Reports::ReportPubList:pubContextClassGuid:pubContextClassGuid-Presentation Property*/


	public class PubContextClassGuid extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public PubContextClassGuid(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPubList:pubContextClassGuid:pubContextClassGuid")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPubList:pubContextClassGuid:pubContextClassGuid")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public PubContextClassGuid getPubContextClassGuid();
	/*Radix::Reports::ReportPubList:classGuid:classGuid-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPubList:classGuid:classGuid")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPubList:classGuid:classGuid")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ClassGuid getClassGuid();
	/*Radix::Reports::ReportPubList:className:className-Presentation Property*/


	public class ClassName extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public ClassName(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPubList:className:className")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPubList:className:className")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ClassName getClassName();


}

/* Radix::Reports::ReportPubList - Desktop Meta*/

/*Radix::Reports::ReportPubList-Entity Class*/

package org.radixware.ads.Reports.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class ReportPubList_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::Reports::ReportPubList:Presentations-Entity Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecQ5T72ARKKBHHNBHOH3LPATQA6M"),
			"Radix::Reports::ReportPubList",
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("imgSW5B57335NAWFEO24G5D4QZOWU"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("spr7U6ZGF3DHBA4DOFJDLFXMQLCQY"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsE7D6ECCLNVEDJNUL5LO5N7SMPY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQXHPR67O4BCI5FGJPHMWSG5C3U"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsE7D6ECCLNVEDJNUL5LO5N7SMPY"),0,

			/*Radix::Reports::ReportPubList:Properties-Properties*/
			new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

					/*Radix::Reports::ReportPubList:classGuid:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colYKEM5Q2LR5BORNW7UDQGGHK7BU"),
						"classGuid",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRBM6AY6U65B5LHTO2AB4MU3XIQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecQ5T72ARKKBHHNBHOH3LPATQA6M"),
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

						/*Radix::Reports::ReportPubList:classGuid:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,50,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Reports::ReportPubList:id:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQ3B6R3LVVBGDZFTHVOYGQXIGW4"),
						"id",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVSCYON3XFFC4BAMJGCCVYOJRYA"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecQ5T72ARKKBHHNBHOH3LPATQA6M"),
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

						/*Radix::Reports::ReportPubList:id:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Reports::ReportPubList:lastUpdateTime:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colLZJGEHVT2ZGKTBYIF7MQCY6FMA"),
						"lastUpdateTime",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls43WTEOA4UFHWNOOLTPHR4DE3EI"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecQ5T72ARKKBHHNBHOH3LPATQA6M"),
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

						/*Radix::Reports::ReportPubList:lastUpdateTime:PropertyPresentation:Edit Options:-Edit Mask Date Time*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskDateTime(org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,null,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Reports::ReportPubList:lastUpdateUserName:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3HPYVDGDE5HGJN2DFHFEN354VQ"),
						"lastUpdateUserName",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMMBREP2FUJGE5LH4UPH75FI7ZU"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecQ5T72ARKKBHHNBHOH3LPATQA6M"),
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

						/*Radix::Reports::ReportPubList:lastUpdateUserName:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,250,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Reports::ReportPubList:contextPid:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colULDE4U54JZHEFOCELCQKPNYX6M"),
						"contextPid",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecQ5T72ARKKBHHNBHOH3LPATQA6M"),
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
						org.radixware.kernel.common.enums.EEditPossibility.PROGRAMMATICALLY,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Reports::ReportPubList:contextPid:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,100,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Reports::ReportPubList:pubContextClassGuid:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colWZ4H7UJ6RFA6FMTXPZBFRNV52M"),
						"pubContextClassGuid",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecQ5T72ARKKBHHNBHOH3LPATQA6M"),
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
						org.radixware.kernel.common.enums.EEditPossibility.PROGRAMMATICALLY,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Reports::ReportPubList:pubContextClassGuid:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,50,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Reports::ReportPubList:className:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdBEDPSBGZ5RA6ZHGFNAMNREERSU"),
						"className",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsV7ASDZ6T75BQREBTDCDISUVPMI"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecQ5T72ARKKBHHNBHOH3LPATQA6M"),
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

						/*Radix::Reports::ReportPubList:className:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false)
			},
			null,
			null,
			null,
			null,
			new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprSMKGLY4AKBDWRB4XEFTYEFZKUA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("spr7U6ZGF3DHBA4DOFJDLFXMQLCQY")},
			true,false,false);
}

/* Radix::Reports::ReportPubList - Web Executable*/

/*Radix::Reports::ReportPubList-Entity Class*/

package org.radixware.ads.Reports.web;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPubList")
public interface ReportPubList {
	/*Group model adapter class. This is the base class for 
	all of selector presentation models of this entity*/
	public static class DefaultGroupModel extends org.radixware.kernel.common.client.models.GroupModel{
		public DefaultGroupModel(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadSelectorPresentationDef def){super(userSession,def);}
		public org.radixware.ads.Reports.web.ReportPubList.ReportPubList_DefaultModel getEntity(int i) throws InterruptedException, org.radixware.kernel.common.client.exceptions.BrokenEntityObjectException, org.radixware.kernel.common.exceptions.ServiceClientException { return (org.radixware.ads.Reports.web.ReportPubList.ReportPubList_DefaultModel )  super.getEntity(i);}
	}

























































	/*Radix::Reports::ReportPubList:lastUpdateUserName:lastUpdateUserName-Presentation Property*/


	public class LastUpdateUserName extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public LastUpdateUserName(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPubList:lastUpdateUserName:lastUpdateUserName")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPubList:lastUpdateUserName:lastUpdateUserName")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public LastUpdateUserName getLastUpdateUserName();
	/*Radix::Reports::ReportPubList:lastUpdateTime:lastUpdateTime-Presentation Property*/


	public class LastUpdateTime extends org.radixware.kernel.common.client.models.items.properties.PropertyDateTime{
		public LastUpdateTime(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPubList:lastUpdateTime:lastUpdateTime")
		public  java.sql.Timestamp getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPubList:lastUpdateTime:lastUpdateTime")
		public   void setValue(java.sql.Timestamp val) {
			Value = val;
		}
	}
	public LastUpdateTime getLastUpdateTime();
	/*Radix::Reports::ReportPubList:id:id-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPubList:id:id")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPubList:id:id")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public Id getId();
	/*Radix::Reports::ReportPubList:contextPid:contextPid-Presentation Property*/


	public class ContextPid extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public ContextPid(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPubList:contextPid:contextPid")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPubList:contextPid:contextPid")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ContextPid getContextPid();
	/*Radix::Reports::ReportPubList:pubContextClassGuid:pubContextClassGuid-Presentation Property*/


	public class PubContextClassGuid extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public PubContextClassGuid(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPubList:pubContextClassGuid:pubContextClassGuid")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPubList:pubContextClassGuid:pubContextClassGuid")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public PubContextClassGuid getPubContextClassGuid();
	/*Radix::Reports::ReportPubList:classGuid:classGuid-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPubList:classGuid:classGuid")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPubList:classGuid:classGuid")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ClassGuid getClassGuid();
	/*Radix::Reports::ReportPubList:className:className-Presentation Property*/


	public class ClassName extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public ClassName(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPubList:className:className")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPubList:className:className")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ClassName getClassName();


}

/* Radix::Reports::ReportPubList - Web Meta*/

/*Radix::Reports::ReportPubList-Entity Class*/

package org.radixware.ads.Reports.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class ReportPubList_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::Reports::ReportPubList:Presentations-Entity Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecQ5T72ARKKBHHNBHOH3LPATQA6M"),
			"Radix::Reports::ReportPubList",
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("imgSW5B57335NAWFEO24G5D4QZOWU"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("spr7U6ZGF3DHBA4DOFJDLFXMQLCQY"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsE7D6ECCLNVEDJNUL5LO5N7SMPY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQXHPR67O4BCI5FGJPHMWSG5C3U"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsE7D6ECCLNVEDJNUL5LO5N7SMPY"),0,

			/*Radix::Reports::ReportPubList:Properties-Properties*/
			new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

					/*Radix::Reports::ReportPubList:classGuid:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colYKEM5Q2LR5BORNW7UDQGGHK7BU"),
						"classGuid",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRBM6AY6U65B5LHTO2AB4MU3XIQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecQ5T72ARKKBHHNBHOH3LPATQA6M"),
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

						/*Radix::Reports::ReportPubList:classGuid:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,50,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Reports::ReportPubList:id:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQ3B6R3LVVBGDZFTHVOYGQXIGW4"),
						"id",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVSCYON3XFFC4BAMJGCCVYOJRYA"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecQ5T72ARKKBHHNBHOH3LPATQA6M"),
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

						/*Radix::Reports::ReportPubList:id:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Reports::ReportPubList:lastUpdateTime:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colLZJGEHVT2ZGKTBYIF7MQCY6FMA"),
						"lastUpdateTime",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls43WTEOA4UFHWNOOLTPHR4DE3EI"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecQ5T72ARKKBHHNBHOH3LPATQA6M"),
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

						/*Radix::Reports::ReportPubList:lastUpdateTime:PropertyPresentation:Edit Options:-Edit Mask Date Time*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskDateTime(org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,null,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Reports::ReportPubList:lastUpdateUserName:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3HPYVDGDE5HGJN2DFHFEN354VQ"),
						"lastUpdateUserName",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMMBREP2FUJGE5LH4UPH75FI7ZU"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecQ5T72ARKKBHHNBHOH3LPATQA6M"),
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

						/*Radix::Reports::ReportPubList:lastUpdateUserName:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,250,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Reports::ReportPubList:contextPid:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colULDE4U54JZHEFOCELCQKPNYX6M"),
						"contextPid",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecQ5T72ARKKBHHNBHOH3LPATQA6M"),
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
						org.radixware.kernel.common.enums.EEditPossibility.PROGRAMMATICALLY,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Reports::ReportPubList:contextPid:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,100,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Reports::ReportPubList:pubContextClassGuid:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colWZ4H7UJ6RFA6FMTXPZBFRNV52M"),
						"pubContextClassGuid",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecQ5T72ARKKBHHNBHOH3LPATQA6M"),
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
						org.radixware.kernel.common.enums.EEditPossibility.PROGRAMMATICALLY,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Reports::ReportPubList:pubContextClassGuid:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,50,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Reports::ReportPubList:className:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdBEDPSBGZ5RA6ZHGFNAMNREERSU"),
						"className",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsV7ASDZ6T75BQREBTDCDISUVPMI"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecQ5T72ARKKBHHNBHOH3LPATQA6M"),
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

						/*Radix::Reports::ReportPubList:className:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false)
			},
			null,
			null,
			null,
			null,
			new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprSMKGLY4AKBDWRB4XEFTYEFZKUA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("spr7U6ZGF3DHBA4DOFJDLFXMQLCQY")},
			true,false,false);
}

/* Radix::Reports::ReportPubList:Common - Desktop Meta*/

/*Radix::Reports::ReportPubList:Common-Editor Presentation*/

package org.radixware.ads.Reports.explorer;
public final class Common_mi{
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new org.radixware.kernel.common.client.meta.RadEditorPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprSMKGLY4AKBDWRB4XEFTYEFZKUA"),
	"Common",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
	null,
	org.radixware.kernel.common.types.Id.Factory.loadFrom("aecQ5T72ARKKBHHNBHOH3LPATQA6M"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("tblQ5T72ARKKBHHNBHOH3LPATQA6M"),
	null,
	null,

	/*Radix::Reports::ReportPubList:Common:Editor Pages-Editor Presentation Pages*/
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

			/*Radix::Reports::ReportPubList:Common:Common-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgJ5KQVPGTVVDLNBEFPOQII6VWV4"),"Common",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecQ5T72ARKKBHHNBHOH3LPATQA6M"),null,null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQ3B6R3LVVBGDZFTHVOYGQXIGW4"),0,0,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colLZJGEHVT2ZGKTBYIF7MQCY6FMA"),0,2,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3HPYVDGDE5HGJN2DFHFEN354VQ"),0,3,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdBEDPSBGZ5RA6ZHGFNAMNREERSU"),0,1,1,false,false)
			},null)
	},
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgJ5KQVPGTVVDLNBEFPOQII6VWV4"))}
	,

	/*Radix::Reports::ReportPubList:Common:Children-Explorer Items*/
		new org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemDef[]{

				/*Radix::Reports::ReportPubList:Common:ReportPubTopic-Child Ref Explorer Item*/

				new org.radixware.kernel.common.client.meta.explorerItems.RadChildRefExplorerItemDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiX2G7FDSYMBF2HO3EIFDP45JDBY"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aecQ5T72ARKKBHHNBHOH3LPATQA6M"),null,
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aec34F7JPGYHJEJHBCE7ISQKJN3IY"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("sprYMAA3X6RZFFI7IACEO42ZEEGOI"),
					65535,
					null,
					16560,true),

				/*Radix::Reports::ReportPubList:Common:ReportPub-Child Ref Explorer Item*/

				new org.radixware.kernel.common.client.meta.explorerItems.RadChildRefExplorerItemDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiT5VMZMVDNNCK3OTTBHF726ZNTI"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aecQ5T72ARKKBHHNBHOH3LPATQA6M"),null,
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aecUNC67IF5OBCX7NOPNLQOCUG374"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("sprYYUTYV7EYRF4TEAHGYK5SWGV6E"),
					65535,
					null,
					16560,true)
		}
	,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
	null,
	0,
	null,
	(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
	2192,0,0,null);
}
/* Radix::Reports::ReportPubList:Common - Web Meta*/

/*Radix::Reports::ReportPubList:Common-Editor Presentation*/

package org.radixware.ads.Reports.web;
public final class Common_mi{
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new org.radixware.kernel.common.client.meta.RadEditorPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprSMKGLY4AKBDWRB4XEFTYEFZKUA"),
	"Common",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
	null,
	org.radixware.kernel.common.types.Id.Factory.loadFrom("aecQ5T72ARKKBHHNBHOH3LPATQA6M"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("tblQ5T72ARKKBHHNBHOH3LPATQA6M"),
	null,
	null,

	/*Radix::Reports::ReportPubList:Common:Editor Pages-Editor Presentation Pages*/
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

			/*Radix::Reports::ReportPubList:Common:Common-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgJ5KQVPGTVVDLNBEFPOQII6VWV4"),"Common",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecQ5T72ARKKBHHNBHOH3LPATQA6M"),null,null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQ3B6R3LVVBGDZFTHVOYGQXIGW4"),0,0,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colLZJGEHVT2ZGKTBYIF7MQCY6FMA"),0,2,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3HPYVDGDE5HGJN2DFHFEN354VQ"),0,3,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdBEDPSBGZ5RA6ZHGFNAMNREERSU"),0,1,1,false,false)
			},null)
	},
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgJ5KQVPGTVVDLNBEFPOQII6VWV4"))}
	,

	/*Radix::Reports::ReportPubList:Common:Children-Explorer Items*/
		new org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemDef[]{

				/*Radix::Reports::ReportPubList:Common:ReportPubTopic-Child Ref Explorer Item*/

				new org.radixware.kernel.common.client.meta.explorerItems.RadChildRefExplorerItemDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiX2G7FDSYMBF2HO3EIFDP45JDBY"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aecQ5T72ARKKBHHNBHOH3LPATQA6M"),null,
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aec34F7JPGYHJEJHBCE7ISQKJN3IY"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("sprYMAA3X6RZFFI7IACEO42ZEEGOI"),
					65535,
					null,
					16560,true),

				/*Radix::Reports::ReportPubList:Common:ReportPub-Child Ref Explorer Item*/

				new org.radixware.kernel.common.client.meta.explorerItems.RadChildRefExplorerItemDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiT5VMZMVDNNCK3OTTBHF726ZNTI"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aecQ5T72ARKKBHHNBHOH3LPATQA6M"),null,
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aecUNC67IF5OBCX7NOPNLQOCUG374"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("sprYYUTYV7EYRF4TEAHGYK5SWGV6E"),
					65535,
					null,
					16560,true)
		}
	,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
	null,
	0,
	null,
	(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
	2192,0,0,null);
}
/* Radix::Reports::ReportPubList:Common:Model - Desktop Executable*/

/*Radix::Reports::ReportPubList:Common:Model-Entity Model Class*/

package org.radixware.ads.Reports.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPubList:Common:Model")
public class Common:Model  extends org.radixware.ads.Reports.explorer.ReportPubList.ReportPubList_DefaultModel {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return Common:Model_mi.rdxMeta; }



	public Common:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadEditorPresentationDef def){super(userSession,def);}

	/*Radix::Reports::ReportPubList:Common:Model:Nested classes-Nested Classes*/

	/*Radix::Reports::ReportPubList:Common:Model:Properties-Properties*/

	/*Radix::Reports::ReportPubList:Common:Model:Methods-Methods*/


}

/* Radix::Reports::ReportPubList:Common:Model - Desktop Meta*/

/*Radix::Reports::ReportPubList:Common:Model-Entity Model Class*/

package org.radixware.ads.Reports.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Common:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aemSMKGLY4AKBDWRB4XEFTYEFZKUA"),
						"Common:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::Reports::ReportPubList:Common:Model:Properties-Properties*/
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

/* Radix::Reports::ReportPubList:Common:Model - Web Executable*/

/*Radix::Reports::ReportPubList:Common:Model-Entity Model Class*/

package org.radixware.ads.Reports.web;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPubList:Common:Model")
public class Common:Model  extends org.radixware.ads.Reports.web.ReportPubList.ReportPubList_DefaultModel {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return Common:Model_mi.rdxMeta; }



	public Common:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadEditorPresentationDef def){super(userSession,def);}

	/*Radix::Reports::ReportPubList:Common:Model:Nested classes-Nested Classes*/

	/*Radix::Reports::ReportPubList:Common:Model:Properties-Properties*/

	/*Radix::Reports::ReportPubList:Common:Model:Methods-Methods*/


}

/* Radix::Reports::ReportPubList:Common:Model - Web Meta*/

/*Radix::Reports::ReportPubList:Common:Model-Entity Model Class*/

package org.radixware.ads.Reports.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Common:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aemSMKGLY4AKBDWRB4XEFTYEFZKUA"),
						"Common:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::Reports::ReportPubList:Common:Model:Properties-Properties*/
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

/* Radix::Reports::ReportPubList:Common - Desktop Meta*/

/*Radix::Reports::ReportPubList:Common-Selector Presentation*/

package org.radixware.ads.Reports.explorer;
public final class Common_mi extends org.radixware.kernel.common.client.meta.RadSelectorPresentationDef{
	public static final org.radixware.kernel.common.client.meta.RadSelectorPresentationDef rdxMeta = new Common_mi();
	private Common_mi(){
		super(org.radixware.kernel.common.types.Id.Factory.loadFrom("spr7U6ZGF3DHBA4DOFJDLFXMQLCQY"),
		"Common",
		org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
		null,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("aecQ5T72ARKKBHHNBHOH3LPATQA6M"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("tblQ5T72ARKKBHHNBHOH3LPATQA6M"),
		null,
		null,
		null,
		new org.radixware.kernel.common.types.Id[]{},
		false,
		null,
		new org.radixware.kernel.common.types.Id[]{},
		false,
		true,
		null,
		0,
		null,
		144,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprSMKGLY4AKBDWRB4XEFTYEFZKUA")},
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprSMKGLY4AKBDWRB4XEFTYEFZKUA")},
		false,true,false,0,0);
		columns = new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn[]{

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQ3B6R3LVVBGDZFTHVOYGQXIGW4"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colYKEM5Q2LR5BORNW7UDQGGHK7BU"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null)
		};
	}
	@Override
	protected org.radixware.kernel.common.client.models.Model createModelImpl(org.radixware.kernel.common.client.IClientEnvironment userSession) {    return new org.radixware.ads.Reports.explorer.ReportPubList.DefaultGroupModel(userSession,this);
	}
}
/* Radix::Reports::ReportPubList:Common - Web Meta*/

/*Radix::Reports::ReportPubList:Common-Selector Presentation*/

package org.radixware.ads.Reports.web;
public final class Common_mi extends org.radixware.kernel.common.client.meta.RadSelectorPresentationDef{
	public static final org.radixware.kernel.common.client.meta.RadSelectorPresentationDef rdxMeta = new Common_mi();
	private Common_mi(){
		super(org.radixware.kernel.common.types.Id.Factory.loadFrom("spr7U6ZGF3DHBA4DOFJDLFXMQLCQY"),
		"Common",
		org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
		null,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("aecQ5T72ARKKBHHNBHOH3LPATQA6M"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("tblQ5T72ARKKBHHNBHOH3LPATQA6M"),
		null,
		null,
		null,
		new org.radixware.kernel.common.types.Id[]{},
		false,
		null,
		new org.radixware.kernel.common.types.Id[]{},
		false,
		true,
		null,
		0,
		null,
		144,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprSMKGLY4AKBDWRB4XEFTYEFZKUA")},
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprSMKGLY4AKBDWRB4XEFTYEFZKUA")},
		false,true,false,0,0);
		columns = new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn[]{

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQ3B6R3LVVBGDZFTHVOYGQXIGW4"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colYKEM5Q2LR5BORNW7UDQGGHK7BU"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null)
		};
	}
	@Override
	protected org.radixware.kernel.common.client.models.Model createModelImpl(org.radixware.kernel.common.client.IClientEnvironment userSession) {    return new org.radixware.ads.Reports.web.ReportPubList.DefaultGroupModel(userSession,this);
	}
}
/* Radix::Reports::ReportPubList - Localizing Bundle */
package org.radixware.ads.Reports.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class ReportPubList - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Last updated");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Время последней модификации");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls43WTEOA4UFHWNOOLTPHR4DE3EI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Report Publication List");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Список публикаций отчетов");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsE7D6ECCLNVEDJNUL5LO5N7SMPY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Topic");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Раздел");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsIJHON6WM7NDPVEZLSFM4TM5NDA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Report class: ");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Класс отчета: ");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsIV7NAV3CBRG7NA6MWEERVW5NZQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Publication list");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Список публикаций");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLZGTB6FWFNHSHCNVL7E47RW2BY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Last updated by");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Автор последней модификации");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMMBREP2FUJGE5LH4UPH75FI7ZU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Report Publication Lists");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Списки публикаций отчетов");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQXHPR67O4BCI5FGJPHMWSG5C3U"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Class GUID");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"GUID класса");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRBM6AY6U65B5LHTO2AB4MU3XIQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Class of publication list");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Класс списка публикаций");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsV7ASDZ6T75BQREBTDCDISUVPMI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"ID");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ид.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVSCYON3XFFC4BAMJGCCVYOJRYA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Context: Common Reports");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Контекст: Общие отчеты");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWZ5ZHR6H7JD3FCDDKADEUS3GFE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Context");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Контекст");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZA4AN4T7IVECDJCCVY4V7CDVT4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Publication");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Публикация");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZHHKPCBKOVB2ZOJAHI3GELN534"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"null"));
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(ReportPubList - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbaecQ5T72ARKKBHHNBHOH3LPATQA6M"),"ReportPubList - Localizing Bundle",$$$items$$$);
}
