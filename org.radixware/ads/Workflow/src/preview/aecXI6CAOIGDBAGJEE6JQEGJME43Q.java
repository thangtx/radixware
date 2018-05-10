
/* Radix::Workflow::ProcessType - Server Executable*/

/*Radix::Workflow::ProcessType-Entity Class*/

package org.radixware.ads.Workflow.server;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType")
public published class ProcessType  extends org.radixware.ads.Types.server.Entity  implements org.radixware.ads.CfgManagement.server.ICfgReferencedObject,org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {


	/**Metainformation accessor method*/
	@SuppressWarnings("unused")
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return ProcessType_mi.rdxMeta;}

	/*Radix::Workflow::ProcessType:Nested classes-Nested Classes*/

	/*Radix::Workflow::ProcessType:CfgObjectLookupAdvizor-Server Dynamic Class*/


	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType:CfgObjectLookupAdvizor")
	private static class CfgObjectLookupAdvizor  implements org.radixware.ads.CfgManagement.server.ICfgObjectLookupAdvizor,org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {


		/**Metainformation accessor method*/
		@SuppressWarnings("unused")
		public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return ProcessType_mi.rdxMeta_adcODCETZCJBNBVXMMR6EPWJLSERA;}

		/*Radix::Workflow::ProcessType:CfgObjectLookupAdvizor:Nested classes-Nested Classes*/

		/*Radix::Workflow::ProcessType:CfgObjectLookupAdvizor:Properties-Properties*/





























		/*Radix::Workflow::ProcessType:CfgObjectLookupAdvizor:Methods-Methods*/

		/*Radix::Workflow::ProcessType:CfgObjectLookupAdvizor:getCfgObjectsByExtGuid-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType:CfgObjectLookupAdvizor:getCfgObjectsByExtGuid")
		public published  java.util.List<org.radixware.ads.Types.server.Entity> getCfgObjectsByExtGuid (Str extGuid, boolean considerContext, org.radixware.ads.Types.server.Entity context) {
			if (extGuid == null) {
			    return java.util.Collections.emptyList();
			}
			final java.util.ArrayList<Types::Entity> pTypes = new java.util.ArrayList<Types::Entity>(1);
			final ProcessType pType = ProcessType.loadByPK(extGuid, true);
			if (pType != null) {
			    pTypes.add(pType);
			}
			return pTypes;
		}


	}

	/*Radix::Workflow::ProcessType:Properties-Properties*/

	/*Radix::Workflow::ProcessType:classGuid-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType:classGuid")
	public published  Str getClassGuid() {
		return classGuid;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType:classGuid")
	public published   void setClassGuid(Str val) {
		classGuid = val;
	}

	/*Radix::Workflow::ProcessType:algoClassGuid-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType:algoClassGuid")
	public published  Str getAlgoClassGuid() {
		return algoClassGuid;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType:algoClassGuid")
	public published   void setAlgoClassGuid(Str val) {
		algoClassGuid = val;
	}

	/*Radix::Workflow::ProcessType:clerkRoleGuids-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType:clerkRoleGuids")
	public published  org.radixware.kernel.common.types.ArrStr getClerkRoleGuids() {
		return clerkRoleGuids;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType:clerkRoleGuids")
	public published   void setClerkRoleGuids(org.radixware.kernel.common.types.ArrStr val) {
		clerkRoleGuids = val;
	}

	/*Radix::Workflow::ProcessType:guid-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType:guid")
	public published  Str getGuid() {
		return guid;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType:guid")
	public published   void setGuid(Str val) {
		guid = val;
	}

	/*Radix::Workflow::ProcessType:notes-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType:notes")
	public published  Str getNotes() {
		return notes;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType:notes")
	public published   void setNotes(Str val) {
		notes = val;
	}

	/*Radix::Workflow::ProcessType:processStoreDays-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType:processStoreDays")
	public published  Int getProcessStoreDays() {
		return processStoreDays;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType:processStoreDays")
	public published   void setProcessStoreDays(Int val) {
		processStoreDays = val;
	}

	/*Radix::Workflow::ProcessType:title-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType:title")
	public published  Str getTitle() {
		return title;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType:title")
	public published   void setTitle(Str val) {
		title = val;
	}

	/*Radix::Workflow::ProcessType:traceProfile-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType:traceProfile")
	public published  Str getTraceProfile() {
		return traceProfile;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType:traceProfile")
	public published   void setTraceProfile(Str val) {
		traceProfile = val;
	}

	/*Radix::Workflow::ProcessType:algoClassTitle-Dynamic Property*/



	protected Str algoClassTitle=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType:algoClassTitle")
	public published  Str getAlgoClassTitle() {

		String s = null;
		if (algoClassGuid != null) {
		   s = Meta::Utils.getDefinitionTitle(Types::Id.Factory.loadFrom(algoClassGuid));
		   if (s == null)
		       s = Meta::Utils.getDefinitionName(Types::Id.Factory.loadFrom(algoClassGuid));
		}
		return s;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType:algoClassTitle")
	public published   void setAlgoClassTitle(Str val) {
		algoClassTitle = val;
	}

	/*Radix::Workflow::ProcessType:algoBaseClassId-Dynamic Property*/



	protected Str algoBaseClassId=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType:algoBaseClassId")
	public published  Str getAlgoBaseClassId() {

		return String.valueOf(getAlgoBaseClassId());
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType:algoBaseClassId")
	public published   void setAlgoBaseClassId(Str val) {
		algoBaseClassId = val;
	}

	/*Radix::Workflow::ProcessType:adminRoles-Dynamic Property*/



	@Deprecated
	protected Str adminRoles=null;













	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType:adminRoles")
	@Deprecated
	public published  Str getAdminRoles() {

		return Meta::Utils.getRoleArrTitle(adminRoleGuids);

	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType:adminRoles")
	@Deprecated
	public published   void setAdminRoles(Str val) {
		adminRoles = val;
	}

	/*Radix::Workflow::ProcessType:clerkRoles-Dynamic Property*/



	@Deprecated
	protected Str clerkRoles=null;













	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType:clerkRoles")
	@Deprecated
	public published  Str getClerkRoles() {

		return Meta::Utils.getRoleArrTitle(clerkRoleGuids);
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType:clerkRoles")
	@Deprecated
	public published   void setClerkRoles(Str val) {
		clerkRoles = val;
	}

	/*Radix::Workflow::ProcessType:classTitle-Dynamic Property*/



	protected Str classTitle=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType:classTitle")
	public published  Str getClassTitle() {

		return getClassDefinitionTitle();
	}

	/*Radix::Workflow::ProcessType:adminRoleGuids-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType:adminRoleGuids")
	public published  org.radixware.kernel.common.types.ArrStr getAdminRoleGuids() {
		return adminRoleGuids;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType:adminRoleGuids")
	public published   void setAdminRoleGuids(org.radixware.kernel.common.types.ArrStr val) {
		adminRoleGuids = val;
	}













































































































	/*Radix::Workflow::ProcessType:Methods-Methods*/

	/*Radix::Workflow::ProcessType:loadByPidStr-System Method*/

	@SuppressWarnings("unused")
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType:loadByPidStr")
	public static published  org.radixware.ads.Workflow.server.ProcessType loadByPidStr (Str pidAsStr, boolean checkExistance) {
	org.radixware.kernel.server.types.Pid pid = new org.radixware.kernel.server.types.Pid(org.radixware.ads.mdlPEKYFVDRVZHGZCBQQDY2NOYFOY.server.pdcArte______________________.__getArteInstanceInternal(),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblXI6CAOIGDBAGJEE6JQEGJME43Q"),pidAsStr);
		try{
		return (
		org.radixware.ads.Workflow.server.ProcessType) org.radixware.ads.mdlPEKYFVDRVZHGZCBQQDY2NOYFOY.server.pdcArte______________________.__getArteInstanceInternal().getEntityObject(pid,null,checkExistance);
	}catch(org.radixware.kernel.server.exceptions.EntityObjectNotExistsError e){
	return null;
	}

	}

	/*Radix::Workflow::ProcessType:loadByPK-System Method*/

	@SuppressWarnings("unused")
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType:loadByPK")
	public static published  org.radixware.ads.Workflow.server.ProcessType loadByPK (Str guid, boolean checkExistance) {
	final java.util.HashMap<org.radixware.kernel.common.types.Id,Object> pkValsMap = new java.util.HashMap<org.radixware.kernel.common.types.Id,Object>(5);
			if(guid==null) return null;
			pkValsMap.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("colAEAAPJXBX5BIXHBNJOYP3FBIR4"),guid);
	org.radixware.kernel.server.types.Pid pid = new org.radixware.kernel.server.types.Pid(org.radixware.ads.mdlPEKYFVDRVZHGZCBQQDY2NOYFOY.server.pdcArte______________________.__getArteInstanceInternal(),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblXI6CAOIGDBAGJEE6JQEGJME43Q"),pkValsMap);
		try{
		return (
		org.radixware.ads.Workflow.server.ProcessType) org.radixware.ads.mdlPEKYFVDRVZHGZCBQQDY2NOYFOY.server.pdcArte______________________.__getArteInstanceInternal().getEntityObject(pid,null,checkExistance);
	}catch(org.radixware.kernel.server.exceptions.EntityObjectNotExistsError e){
	return null;
	}

	}

	/*Radix::Workflow::ProcessType:beforeCreate-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType:beforeCreate")
	protected published  boolean beforeCreate (org.radixware.kernel.server.types.Entity src) {
		if (guid == null) {
		    guid = Arte::Arte.generateGuid();
		}
		return super.beforeCreate(src);
	}

	/*Radix::Workflow::ProcessType:startProcess-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType:startProcess")
	protected published  org.radixware.ads.Workflow.server.Process startProcess (java.util.Vector<java.lang.Class<?>> paramTypes, java.util.Vector<java.lang.Object> paramVals) {
		return ServerUtil.start(this, paramTypes, paramVals);

	}

	/*Radix::Workflow::ProcessType:getAlgoBaseClassId-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType:getAlgoBaseClassId")
	protected published  org.radixware.kernel.common.types.Id getAlgoBaseClassId () {
		return idof[Types::Algorithm];
	}

	/*Radix::Workflow::ProcessType:getAlgorithm-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType:getAlgorithm")
	protected published  org.radixware.ads.Types.server.Algorithm getAlgorithm () {
		Types::Id id;
		try {
		    id = (Types::Id) Arte::Arte.invokeByClassId (
		        Types::Id.Factory.loadFrom(algoClassGuid), "getWorkingClassId", 
		        new Class<?>[] {},  new Object[] {}  );
		} catch (Exceptions::Exception e){
		    if (e instanceof RuntimeException)
		        throw (RuntimeException)e;
		    throw new AppError(e.getMessage(), e); 
		}

		if (id != null)
		    return (Types::Algorithm) Arte::Arte.newObject(id);
		return null;

	}

	/*Radix::Workflow::ProcessType:create-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType:create")
	public static published  org.radixware.ads.Workflow.server.ProcessType create (org.radixware.kernel.common.types.Id classId, Str guid) {
		ProcessType obj = (ProcessType) Arte::Arte.newObject(classId);
		obj.init();
		obj.guid = guid;
		return obj;
	}

	/*Radix::Workflow::ProcessType:exportAll-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType:exportAll")
	 static  void exportAll (org.radixware.ads.CfgManagement.server.CfgExportData data, java.util.Iterator<org.radixware.ads.Workflow.server.ProcessType> iter) {
		ImpExpXsd:ProcessTypeGroupDocument groupDoc = ImpExpXsd:ProcessTypeGroupDocument.Factory.newInstance();
		groupDoc.addNewProcessTypeGroup();

		if (iter == null) {
		    Workflow.Db::ProcessTypesCursor c = Workflow.Db::ProcessTypesCursor.open();
		    iter = new EntityCursorIterator<ProcessType>(c, idof[Workflow.Db::ProcessTypesCursor:processType]);
		}

		try {
		    while (iter.hasNext()) {
		        try {
		            ProcessType pt = iter.next();
		            ImpExpXsd:ProcessTypeDocument singleDoc = pt.exportToXml();
		            data.children.add(new CfgExportData(pt, null, idof[CfgItem.ProcessTypeSingle], singleDoc));
		            groupDoc.ProcessTypeGroup.addNewItem().set(singleDoc.ProcessType);
		        } catch (Exceptions::DefinitionNotFoundError ex) {
		            Arte::Trace.debug("Error on export ProcessType:\n" + Arte::Trace.exceptionStackToString(ex), Arte::EventSource:AppCfgPackage);
		        }
		    }
		} finally {
		    EntityCursorIterator.closeIterator(iter);
		}

		data.itemClassId = idof[CfgItem.ProcessTypeGroup];
		data.object = null;
		data.data = null;
		data.fileContent = groupDoc;
	}

	/*Radix::Workflow::ProcessType:exportThis-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType:exportThis")
	  void exportThis (org.radixware.ads.CfgManagement.server.CfgExportData data) {
		data.itemClassId = idof[CfgItem.ProcessTypeSingle];
		data.object = this;
		data.objectRid = guid;
		data.data = exportToXml();
		data.fileContent = data.data;

	}

	/*Radix::Workflow::ProcessType:exportToXml-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType:exportToXml")
	protected published  org.radixware.ads.Workflow.common.ImpExpXsd.ProcessTypeDocument exportToXml () {
		ImpExpXsd:ProcessTypeDocument doc = ImpExpXsd:ProcessTypeDocument.Factory.newInstance();
		ImpExpXsd:ProcessType xml = doc.addNewProcessType();
		xml.Guid = guid;
		xml.Title = title;
		xml.AlgoClassGuid = algoClassGuid;
		xml.TraceProfile = traceProfile;
		xml.AdminRoleGuids = adminRoleGuids;
		xml.ClerkRoleGuids = clerkRoleGuids;
		xml.ProcessStoreDays = processStoreDays;
		xml.Notes = notes;
		exportExtensionProps(xml);
		CfgManagement::ImpExpUtils.exportEntity(xml, this, true, null);

		return doc;
	}

	/*Radix::Workflow::ProcessType:importAll-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType:importAll")
	public static published  void importAll (org.radixware.ads.Workflow.common.ImpExpXsd.ProcessTypeGroup xml, org.radixware.ads.CfgManagement.server.ICfgImportHelper helper) {
		for (ImpExpXsd:ProcessType x : xml.ItemList) {
		    importOne(x, helper);
		    if (helper.wasCancelled())
		        break;
		}

	}

	/*Radix::Workflow::ProcessType:importOne-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType:importOne")
	public static published  org.radixware.ads.Workflow.server.ProcessType importOne (org.radixware.ads.Workflow.common.ImpExpXsd.ProcessType xml, org.radixware.ads.CfgManagement.server.ICfgImportHelper helper) {
		if (xml == null)
		    return null;

		Str guid = xml.Guid;
		ProcessType obj = ProcessType.loadByPK(guid, true);
		if (obj == null) {
		    obj = create(xml.ClassId, guid);
		    obj.importThis(xml, helper);
		} else
		    switch (helper.getActionIfObjExists(obj)) {
		        case UPDATE:
		            obj.importThis(xml, helper);
		            break;
		        case NEW:
		            obj = create(xml.ClassId, Arte::Arte.generateGuid());
		            obj.importThis(xml, helper);
		            break;
		        case CANCELL:
		            helper.reportObjectCancelled(obj);
		            break;
		    }
		return obj;
	}

	/*Radix::Workflow::ProcessType:importThis-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType:importThis")
	public published  void importThis (org.radixware.ads.Workflow.common.ImpExpXsd.ProcessType xml, org.radixware.ads.CfgManagement.server.ICfgImportHelper helper) {
		title = xml.Title;
		algoClassGuid = xml.AlgoClassGuid;
		if (xml.AdminRoleGuids != null) {
		    adminRoleGuids = new ArrStr(xml.AdminRoleGuids);
		    if (!Meta::Utils.checkRoleArr(adminRoleGuids))
		        helper.reportWarnings(this, "Invalid administrator role");
		} else
		    adminRoleGuids = null;
		if (xml.ClerkRoleGuids != null) {
		    clerkRoleGuids = new ArrStr(xml.ClerkRoleGuids);
		    if (!Meta::Utils.checkRoleArr(clerkRoleGuids))
		        helper.reportWarnings(this, "Invalid operator role");
		} else
		    clerkRoleGuids = null;
		traceProfile = xml.TraceProfile;
		processStoreDays = xml.ProcessStoreDays;
		notes = xml.Notes;
		importExtensionProps(xml);

		helper.createOrUpdateAndReport(this);

		CfgManagement::ImpExpUtils.importProps(xml, this, helper);


	}

	/*Radix::Workflow::ProcessType:updateFromCfgItem-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType:updateFromCfgItem")
	public published  void updateFromCfgItem (org.radixware.ads.Workflow.server.CfgItem.ProcessTypeSingle cfg, org.radixware.ads.CfgManagement.server.ICfgImportHelper helper) {
		importThis(cfg.myData.ProcessType, helper);


	}

	/*Radix::Workflow::ProcessType:exportExtensionProps-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType:exportExtensionProps")
	protected published  void exportExtensionProps (org.radixware.ads.Workflow.common.ImpExpXsd.ProcessType xml) {
		//for overwrite
	}

	/*Radix::Workflow::ProcessType:importExtensionProps-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType:importExtensionProps")
	protected published  void importExtensionProps (org.radixware.ads.Workflow.common.ImpExpXsd.ProcessType xml) {
		//for overwirite
	}

	/*Radix::Workflow::ProcessType:onCommand_Import-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType:onCommand_Import")
	protected published  org.radixware.schemas.types.StrDocument onCommand_Import (org.radixware.ads.Workflow.common.ImpExpXsd.ProcessTypeDocument input, org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValHandlersById) {
		CfgManagement::CfgImportHelper.Interactive helper = new CfgImportHelper.Interactive(false, true);
		importThis(input.ProcessType, helper);
		return helper.getResultsAsHtmlStr();

	}

	/*Radix::Workflow::ProcessType:copyPropVal-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType:copyPropVal")
	public published  void copyPropVal (org.radixware.kernel.server.meta.clazzes.RadPropDef prop, org.radixware.kernel.server.types.Entity src) {
		if (idof[ProcessType:guid] == prop.getId()) {
		    return;
		}
		super.copyPropVal(prop, src);
	}

	/*Radix::Workflow::ProcessType:getCfgReferenceExtGuid-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType:getCfgReferenceExtGuid")
	public published  Str getCfgReferenceExtGuid () {
		return guid;
	}

	/*Radix::Workflow::ProcessType:getCfgReferencePropId-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType:getCfgReferencePropId")
	public published  org.radixware.kernel.common.types.Id getCfgReferencePropId () {
		return idof[ProcessType:guid];
	}



	@Override
	public org.radixware.kernel.server.types.FormHandler.NextDialogsRequest execCommand(org.radixware.kernel.common.types.Id cmdId, org.radixware.kernel.common.types.Id propId, org.apache.xmlbeans.XmlObject input, org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValsById, org.apache.xmlbeans.XmlObject output) throws org.radixware.kernel.common.exceptions.AppException,java.lang.InterruptedException{
		if(cmdId == cmdRQKTKAFR3BA7ZP3EONGZ4P62VI){
			org.radixware.schemas.types.StrDocument result = onCommand_Import((org.radixware.ads.Workflow.common.ImpExpXsd.ProcessTypeDocument)org.radixware.kernel.common.utils.XmlObjectProcessor.getAppCommandInputData(getClass().getClassLoader(),input,org.radixware.ads.Workflow.common.ImpExpXsd.ProcessTypeDocument.class),newPropValsById);
			if(result != null)
				output.set(result);
			return null;
		} else 
			return super.execCommand(cmdId,propId,input,newPropValsById,output);
	}


}

/* Radix::Workflow::ProcessType - Server Meta*/

/*Radix::Workflow::ProcessType-Entity Class*/

package org.radixware.ads.Workflow.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class ProcessType_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecXI6CAOIGDBAGJEE6JQEGJME43Q"),"ProcessType",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsE5GLGFYXZNC4DEF7GBKVANPDIY"),

						org.radixware.kernel.common.enums.EClassType.ENTITY,

						/*Radix::Workflow::ProcessType:Presentations-Entity Presentations*/
						/*Class presentation attributes*/
						 new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef(/*Owner Class Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("aecXI6CAOIGDBAGJEE6JQEGJME43Q"),
							/*Owner Class Name*/
							"ProcessType",
							/*Title Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsE5GLGFYXZNC4DEF7GBKVANPDIY"),
							/*Property presentations*/

							/*Radix::Workflow::ProcessType:Properties-Properties*/
							new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef[]{

									/*Radix::Workflow::ProcessType:classGuid:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDIZMB2WMQFGFNKGNDPBZKRCHIM"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Workflow::ProcessType:algoClassGuid:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colE62KHOUF45E6JDWSYCA7TICXAI"),org.radixware.kernel.common.enums.EEditPossibility.ONLY_IN_EDITOR,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Workflow::ProcessType:clerkRoleGuids:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col6SKMJNSZZNFVDNGCP26PG6QPEM"),org.radixware.kernel.common.enums.EEditPossibility.ONLY_IN_EDITOR,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Workflow::ProcessType:guid:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colAEAAPJXBX5BIXHBNJOYP3FBIR4"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Workflow::ProcessType:notes:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXPT3ITROQNCC5OQ67AAKUEY2IQ"),org.radixware.kernel.common.enums.EEditPossibility.ONLY_IN_EDITOR,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Workflow::ProcessType:processStoreDays:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNWJ6DLDISBBWZKPVZUMMGLM5LI"),org.radixware.kernel.common.enums.EEditPossibility.ONLY_IN_EDITOR,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Workflow::ProcessType:title:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGG5VO4FGVBAMFB7SU4TOCRK7JE"),org.radixware.kernel.common.enums.EEditPossibility.ONLY_IN_EDITOR,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Workflow::ProcessType:traceProfile:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colCGY7I2E76ZF7LNL4OVRXERFG7I"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Workflow::ProcessType:algoClassTitle:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdPB3MR5M2DJA45AN54L4PE3DAI4"),org.radixware.kernel.common.enums.EEditPossibility.ONLY_IN_EDITOR,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Workflow::ProcessType:algoBaseClassId:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd6R44AVB3PJBSDKZNDR6VJCDEPY"),org.radixware.kernel.common.enums.EEditPossibility.PROGRAMMATICALLY,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Workflow::ProcessType:adminRoles:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdJ5XPG7DBYZAF3CXPIOH2DTYQJE"),org.radixware.kernel.common.enums.EEditPossibility.ONLY_IN_EDITOR,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Workflow::ProcessType:clerkRoles:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdQ5EFB5BHPVHPRLR7UT6ZQ2KKSM"),org.radixware.kernel.common.enums.EEditPossibility.ONLY_IN_EDITOR,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Workflow::ProcessType:classTitle:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd7WLWUH7XWFDXLEANP2KDS2QERY"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Workflow::ProcessType:adminRoleGuids:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col6KIGNQEL7NEWFDQMOEIO4POSTQ"),org.radixware.kernel.common.enums.EEditPossibility.ONLY_IN_EDITOR,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT))
							},
							/*Commands*/
							new org.radixware.kernel.server.meta.presentations.RadCommandDef[]{

									new org.radixware.kernel.server.meta.presentations.RadCommandDef(
									/*Radix::Workflow::ProcessType:Export-Object Command*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdKLFZCC72RBBH3JQBLLJMESTOBE"),"Export",org.radixware.kernel.common.enums.ECommandScope.OBJECT,null,org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_EXISTENT,org.radixware.kernel.common.enums.ECommandNature.FORM_IN_OUT,org.radixware.kernel.common.types.Id.Factory.loadFrom("aecXI6CAOIGDBAGJEE6JQEGJME43Q"),true,true),

									new org.radixware.kernel.server.meta.presentations.RadCommandDef(
									/*Radix::Workflow::ProcessType:Import-Object Command*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdRQKTKAFR3BA7ZP3EONGZ4P62VI"),"Import",org.radixware.kernel.common.enums.ECommandScope.OBJECT,null,org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_FIXED,org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,org.radixware.kernel.common.types.Id.Factory.loadFrom("aecXI6CAOIGDBAGJEE6JQEGJME43Q"),false,true),

									new org.radixware.kernel.server.meta.presentations.RadCommandDef(
									/*Radix::Workflow::ProcessType:CfgReports-Object Command*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdCXSNTQ4O5BE3RPMMGDOJFIQM5Y"),"CfgReports",org.radixware.kernel.common.enums.ECommandScope.OBJECT,null,org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_FIXED,org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,org.radixware.kernel.common.types.Id.Factory.loadFrom("aecXI6CAOIGDBAGJEE6JQEGJME43Q"),false,true)
							},
							/*Sortings*/
							new org.radixware.kernel.server.meta.presentations.RadSortingDef[]{

									new org.radixware.kernel.server.meta.presentations.RadSortingDef(
									/*Radix::Workflow::ProcessType:Title-Sorting*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("srtXKQWFA3UPVBDTKWQW3ZLSDQRYM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecXI6CAOIGDBAGJEE6JQEGJME43Q"),"Title",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFBK3KQPUOBG45AZESLN5B2ANCM"),new org.radixware.kernel.server.meta.presentations.RadSortingDef.Item[]{

											new org.radixware.kernel.server.meta.presentations.RadSortingDef.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGG5VO4FGVBAMFB7SU4TOCRK7JE"),org.radixware.kernel.common.enums.EOrder.ASC)
									},"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware")
							},
							/*Filters*/
							null,
							/*Editor presentations*/
							new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef[]{

									new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef(
									/*Radix::Workflow::ProcessType:General-Editor Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprMVL6HOH2I5FNDBDHBTXNMT5QDQ"),
									"General",
									org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
									null,
									2176,
									null,

									/*Radix::Workflow::ProcessType:General:Children-Explorer Items*/
										new org.radixware.kernel.server.meta.presentations.RadExplorerItemDef[]{

												/*Radix::Workflow::ProcessType:General:Process-Child Ref Explorer Item*/

												new org.radixware.kernel.server.meta.presentations.RadChildRefExplorerItemDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiUMZFQPRC5NFXXOBPHI6BHE2SQ4"),
													org.radixware.kernel.common.types.Id.Factory.loadFrom("aecXI6CAOIGDBAGJEE6JQEGJME43Q"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRDXQVFY6PLNRDANMABIFNQAABA"),
													org.radixware.kernel.common.types.Id.Factory.loadFrom("sprVE2DLMH2XTNRDF5NABIFNQAABA"),
													new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),
													org.radixware.kernel.common.types.Id.Factory.loadFrom("ref5CVV7PTXI5B3DDPSSSLQJRTOKI"),
													null,
													null)
										}
									,
									org.radixware.kernel.server.types.Restrictions.Factory.newInstance(0,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdKLFZCC72RBBH3JQBLLJMESTOBE"),org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdCXSNTQ4O5BE3RPMMGDOJFIQM5Y")},null,null),
									(org.radixware.kernel.server.meta.presentations.RadPropertyPresentationAttributes[]) null,
									org.radixware.kernel.common.enums.EEditorPresentationRightsInheritanceMode.NONE,
									null,null)
							},
							/*Selector presentations*/
							new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef[]{

									new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef(
									/*Radix::Workflow::ProcessType:General-Selector Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("spr4DFZXOAU7BBU7NDXFVJ23NNZWM"),"General",null,128,new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn[]{

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGG5VO4FGVBAMFB7SU4TOCRK7JE"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdPB3MR5M2DJA45AN54L4PE3DAI4"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXPT3ITROQNCC5OQ67AAKUEY2IQ"),true)
									},new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.Addons(org.radixware.kernel.common.types.Id.Factory.loadFrom("srtXKQWFA3UPVBDTKWQW3ZLSDQRYM"),true,null,true,null,true,null,false,true,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprMVL6HOH2I5FNDBDHBTXNMT5QDQ")},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprMVL6HOH2I5FNDBDHBTXNMT5QDQ")},org.radixware.kernel.server.types.Restrictions.Factory.newInstance(16416,null,null,null),org.radixware.kernel.common.types.Id.Factory.loadFrom("ecc7ZLDPECXHNBE3FGE6FKVSXTP5U"))
							},
							/*Default selector presentation Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("spr4DFZXOAU7BBU7NDXFVJ23NNZWM"),
							/*Presentation Map*/
							null,
							/*Object title format*/

							/*Radix::Workflow::ProcessType:TitleFormat-Object Title Format*/

							new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecXI6CAOIGDBAGJEE6JQEGJME43Q"),new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.TitleItem[]{

									new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.TitleItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecXI6CAOIGDBAGJEE6JQEGJME43Q"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colGG5VO4FGVBAMFB7SU4TOCRK7JE"),"{0}",org.radixware.kernel.common.enums.ETitleNullFormat.SAME_AS_NOT_NULL,null)
							},org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsW7KDCTRGTJAJHFDM4L2TKWKVIA"),org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.DefinitionContextType.ENTITY),
							/*Class catalogs*/
							new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog[]{

									new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog(
									/*Radix::Workflow::ProcessType:All-Dynamic Class Catalog*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("ecc7ZLDPECXHNBE3FGE6FKVSXTP5U"),org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.ELinkMode.VIRTUAL,new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.Item[]{
									}
									)
							},
							/*Restrictions*/
							org.radixware.kernel.server.types.Restrictions.Factory.newInstance(0,null,null,null),null,0),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblXI6CAOIGDBAGJEE6JQEGJME43Q"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("pdcEntity____________________"),

						/*Radix::Workflow::ProcessType:Properties-Properties*/
						new org.radixware.kernel.server.meta.clazzes.RadPropDef[]{

								/*Radix::Workflow::ProcessType:classGuid-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDIZMB2WMQFGFNKGNDPBZKRCHIM"),"classGuid",null,org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Workflow::ProcessType:algoClassGuid-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colE62KHOUF45E6JDWSYCA7TICXAI"),"algoClassGuid",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRRCVUKJTXBE6RI66HRIHK43ADM"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Workflow::ProcessType:clerkRoleGuids-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col6SKMJNSZZNFVDNGCP26PG6QPEM"),"clerkRoleGuids",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAB5ECBSFXVFYFCSLBJKQJJNBL4"),org.radixware.kernel.common.enums.EValType.ARR_STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Workflow::ProcessType:guid-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colAEAAPJXBX5BIXHBNJOYP3FBIR4"),"guid",null,org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Workflow::ProcessType:notes-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXPT3ITROQNCC5OQ67AAKUEY2IQ"),"notes",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsURLGEQMWTFELDNUCBCK4G3FM3I"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Workflow::ProcessType:processStoreDays-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNWJ6DLDISBBWZKPVZUMMGLM5LI"),"processStoreDays",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFNZCFQRTGVGLRDMD2IHSX7LUII"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("14")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Workflow::ProcessType:title-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGG5VO4FGVBAMFB7SU4TOCRK7JE"),"title",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMQFFYCIKAVHZHL2MAELGDDRYOE"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Workflow::ProcessType:traceProfile-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colCGY7I2E76ZF7LNL4OVRXERFG7I"),"traceProfile",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2HDL4YPMGJGFLBAKFJ7J7U5A2Q"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("Event")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Workflow::ProcessType:algoClassTitle-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdPB3MR5M2DJA45AN54L4PE3DAI4"),"algoClassTitle",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJCKKQVHXU5H45CEZQMEOSIGBHI"),org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Workflow::ProcessType:algoBaseClassId-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd6R44AVB3PJBSDKZNDR6VJCDEPY"),"algoBaseClassId",null,org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Workflow::ProcessType:adminRoles-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdJ5XPG7DBYZAF3CXPIOH2DTYQJE"),"adminRoles",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTCT36BSO2JH4DCVXLMJ3Z3XB3M"),org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Workflow::ProcessType:clerkRoles-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdQ5EFB5BHPVHPRLR7UT6ZQ2KKSM"),"clerkRoles",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMR2K2JNYKBDGXKMEQQK2RLXZSU"),org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Workflow::ProcessType:classTitle-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd7WLWUH7XWFDXLEANP2KDS2QERY"),"classTitle",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVU7Q7FYEH5H4JASAFMEUNIVLCY"),org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadAccessorProxy.R_ACCESSOR_INSTANCE),

								/*Radix::Workflow::ProcessType:adminRoleGuids-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col6KIGNQEL7NEWFDQMOEIO4POSTQ"),"adminRoleGuids",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsORPGK57FZFFJRBMZ55IK67ZQBU"),org.radixware.kernel.common.enums.EValType.ARR_STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE)
						},

						/*Radix::Workflow::ProcessType:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth_loadByPidStr_____________"),"loadByPidStr",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("pidAsStr",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprD3JHITV3TBDOPAN6SLU6BWR7WQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("checkExistance",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprB64TO7KDMBCYXA3UHF5CT2TEMQ"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth_loadByPK_________________"),"loadByPK",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("guid",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprTXZ4QZ3G6BD7HPUQTUGKJWUG54")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("checkExistance",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr77Y5I3K5CJHB7JILHSI6ODLCUA"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthFMXUFM5J25E4TNM4PR73F6V3E4"),"beforeCreate",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("src",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprPGXS7TBZAFHMBEHKTV4OBJ6RGA"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthTMJQKB6IBVHU7J6QQEGOJM6X2I"),"startProcess",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("paramTypes",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprCKPN4UDWTZEC7DUICWZKLYWJY4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("paramVals",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr4X2CAFVSNFF4TCNCAFPN7DZPUQ"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthW2VGCDWAWVBFFCHQA6A2OTRBLU"),"getAlgoBaseClassId",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth5GDD7A6FCNC7ZCVEGPUQR2HTVM"),"getAlgorithm",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthVKEDR7FF4VGAFBY3NILSXSSKZM"),"create",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("classId",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprAN77DG5DZJCD7MU6A7PYJ4RTW4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("guid",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprRKD4DQJZ45BCBCWQDGOHYJNCVY"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthAE52AUU5UVBHVPUBJYFSDS446E"),"exportAll",true,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("data",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprSY7BLG7O3BHKBIUZS7UE5KVS3Y")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("iter",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprZX6GUQ6XR5DOLBSBLANZAJ7YSE"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthV7AMZCG6WZGL7GSSLC63JT26GQ"),"exportThis",false,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("data",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprSY7BLG7O3BHKBIUZS7UE5KVS3Y"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthIVPMLUG27REFHHMWMNUNMTJN7I"),"exportToXml",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth55X7QOPPKZDHVO2FKSVTRY7V3M"),"importAll",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("xml",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr2RLOK42HYZCS3LYDTUKUPR2RD4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("helper",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr5AEE44BN5VCFBEDIUMN2FNENKA"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthZXSWAMTDDRE63E4ZPOYF342QPE"),"importOne",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("xml",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr2RLOK42HYZCS3LYDTUKUPR2RD4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("helper",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprUAHGASFHCNFIBNEQK3K2GZ7XLQ"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthG5XD7CD2BNB63CS4LLUUONMBZU"),"importThis",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("xml",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr2RLOK42HYZCS3LYDTUKUPR2RD4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("helper",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprPK55TF3CIRCULIX6OX7FD4N2LQ"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthCZ7ND7KONNG47HBYIW7LSQAAMY"),"updateFromCfgItem",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("cfg",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprVAMRIOYK4ZGAJIMTDCHQBGSUZ4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("helper",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr5IUSIB24WNFVZOBXWL2TL57JUQ"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth5IHXYRPWQNFSZLJAPMAYU4LY4U"),"exportExtensionProps",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("xml",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprNIXALLELIJBBTPVND6ZO6BDFDI"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthDGYM4XDCUFBHFGUSDZ4WSPRLOQ"),"importExtensionProps",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("xml",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprG3IV4Y6AZFBMXDLHSLJGP3G22Q"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthcmdRQKTKAFR3BA7ZP3EONGZ4P62VI"),"onCommand_Import",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("input",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr5NI264QXPVBADDLLEID5KYNOA4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("newPropValHandlersById",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprOPGDILIJRVHGXMTZDOHJLPK4DU"))
								},org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth2ID6LLB4ORG75JFEH45HYYK75U"),"copyPropVal",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("prop",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprDI7CDHHPCJFORBMKTDRAHYPDJY")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("src",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprU5JVVZS7RREOJBVTUJOL2HKUEI"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthQXRW7PZ4QBBEJAJL57JFPES7EQ"),"getCfgReferenceExtGuid",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthTIFZEEQC2NEUNPFIRAGRH2B7EY"),"getCfgReferencePropId",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.JAVA_CLASS)
						},
						null,
						org.radixware.kernel.common.enums.EAccessAreaType.NONE,null,null,false);
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta_adcODCETZCJBNBVXMMR6EPWJLSERA = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("adcODCETZCJBNBVXMMR6EPWJLSERA"),"CfgObjectLookupAdvizor",null,

						org.radixware.kernel.common.enums.EClassType.ENTITY,
						null,
						null,
						null,

						/*Radix::Workflow::ProcessType:CfgObjectLookupAdvizor:Properties-Properties*/
						null,

						/*Radix::Workflow::ProcessType:CfgObjectLookupAdvizor:Methods-Methods*/
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

/* Radix::Workflow::ProcessType - Desktop Executable*/

/*Radix::Workflow::ProcessType-Entity Class*/

package org.radixware.ads.Workflow.explorer;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType")
public interface ProcessType {
	/*Group model adapter class. This is the base class for 
	all of selector presentation models of this entity*/
	public static class DefaultGroupModel extends org.radixware.kernel.common.client.models.GroupModel{
		public DefaultGroupModel(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadSelectorPresentationDef def){super(userSession,def);}





		public org.radixware.ads.Workflow.explorer.ProcessType.ProcessType_DefaultModel getEntity(int i) throws InterruptedException, org.radixware.kernel.common.client.exceptions.BrokenEntityObjectException, org.radixware.kernel.common.exceptions.ServiceClientException { return (org.radixware.ads.Workflow.explorer.ProcessType.ProcessType_DefaultModel )  super.getEntity(i);}
	}







































































































	/*Radix::Workflow::ProcessType:adminRoleGuids:adminRoleGuids-Presentation Property*/


	public class AdminRoleGuids extends org.radixware.kernel.common.client.models.items.properties.PropertyArrStr{
		public AdminRoleGuids(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType:adminRoleGuids:adminRoleGuids")
		public  org.radixware.kernel.common.types.ArrStr getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType:adminRoleGuids:adminRoleGuids")
		public   void setValue(org.radixware.kernel.common.types.ArrStr val) {
			Value = val;
		}
	}
	public AdminRoleGuids getAdminRoleGuids();
	/*Radix::Workflow::ProcessType:clerkRoleGuids:clerkRoleGuids-Presentation Property*/


	public class ClerkRoleGuids extends org.radixware.kernel.common.client.models.items.properties.PropertyArrStr{
		public ClerkRoleGuids(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType:clerkRoleGuids:clerkRoleGuids")
		public  org.radixware.kernel.common.types.ArrStr getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType:clerkRoleGuids:clerkRoleGuids")
		public   void setValue(org.radixware.kernel.common.types.ArrStr val) {
			Value = val;
		}
	}
	public ClerkRoleGuids getClerkRoleGuids();
	/*Radix::Workflow::ProcessType:guid:guid-Presentation Property*/


	public class Guid extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public Guid(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType:guid:guid")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType:guid:guid")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Guid getGuid();
	/*Radix::Workflow::ProcessType:traceProfile:traceProfile-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType:traceProfile:traceProfile")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType:traceProfile:traceProfile")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public TraceProfile getTraceProfile();
	/*Radix::Workflow::ProcessType:classGuid:classGuid-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType:classGuid:classGuid")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType:classGuid:classGuid")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ClassGuid getClassGuid();
	/*Radix::Workflow::ProcessType:algoClassGuid:algoClassGuid-Presentation Property*/


	public class AlgoClassGuid extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public AlgoClassGuid(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType:algoClassGuid:algoClassGuid")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType:algoClassGuid:algoClassGuid")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public AlgoClassGuid getAlgoClassGuid();
	/*Radix::Workflow::ProcessType:title:title-Presentation Property*/


	public class Title extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public Title(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType:title:title")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType:title:title")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Title getTitle();
	/*Radix::Workflow::ProcessType:processStoreDays:processStoreDays-Presentation Property*/


	public class ProcessStoreDays extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public ProcessStoreDays(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType:processStoreDays:processStoreDays")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType:processStoreDays:processStoreDays")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public ProcessStoreDays getProcessStoreDays();
	/*Radix::Workflow::ProcessType:notes:notes-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType:notes:notes")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType:notes:notes")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Notes getNotes();
	/*Radix::Workflow::ProcessType:algoBaseClassId:algoBaseClassId-Presentation Property*/


	public class AlgoBaseClassId extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public AlgoBaseClassId(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType:algoBaseClassId:algoBaseClassId")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType:algoBaseClassId:algoBaseClassId")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public AlgoBaseClassId getAlgoBaseClassId();
	/*Radix::Workflow::ProcessType:classTitle:classTitle-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType:classTitle:classTitle")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType:classTitle:classTitle")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ClassTitle getClassTitle();
	/*Radix::Workflow::ProcessType:adminRoles:adminRoles-Presentation Property*/


	public class AdminRoles extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public AdminRoles(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Deprecated
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}













		@Deprecated

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType:adminRoles:adminRoles")
		public  Str getValue() {
			return Value;
		}
		@Deprecated

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType:adminRoles:adminRoles")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public AdminRoles getAdminRoles();
	/*Radix::Workflow::ProcessType:algoClassTitle:algoClassTitle-Presentation Property*/


	public class AlgoClassTitle extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public AlgoClassTitle(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType:algoClassTitle:algoClassTitle")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType:algoClassTitle:algoClassTitle")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public AlgoClassTitle getAlgoClassTitle();
	/*Radix::Workflow::ProcessType:clerkRoles:clerkRoles-Presentation Property*/


	public class ClerkRoles extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public ClerkRoles(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Deprecated
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}













		@Deprecated

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType:clerkRoles:clerkRoles")
		public  Str getValue() {
			return Value;
		}
		@Deprecated

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType:clerkRoles:clerkRoles")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ClerkRoles getClerkRoles();
	public static class CfgReports extends org.radixware.kernel.common.client.models.items.Command{
		protected CfgReports(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public void send() throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,null);
			processResponse(response, null);
		}

	}

	public static class Export extends org.radixware.kernel.common.client.models.items.Command{
		protected Export(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}

	}

	public static class Import extends org.radixware.kernel.common.client.models.items.Command{
		protected Import(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public org.radixware.schemas.types.StrDocument send(org.radixware.ads.Workflow.common.ImpExpXsd.ProcessTypeDocument input ) throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,input);
			org.apache.xmlbeans.XmlObject xmlResponse = processResponse(response, null);
			return (org.radixware.schemas.types.StrDocument)org.radixware.kernel.common.utils.XmlObjectProcessor.castToXmlClass(getClass().getClassLoader(),xmlResponse,org.radixware.schemas.types.StrDocument.class);
		}

	}



}

/* Radix::Workflow::ProcessType - Desktop Meta*/

/*Radix::Workflow::ProcessType-Entity Class*/

package org.radixware.ads.Workflow.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class ProcessType_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::Workflow::ProcessType:Presentations-Entity Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecXI6CAOIGDBAGJEE6JQEGJME43Q"),
			"Radix::Workflow::ProcessType",
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("imgYTQLIJ3AFIXRTYR3VKVAGGLM346GSPPC"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("spr4DFZXOAU7BBU7NDXFVJ23NNZWM"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsE5GLGFYXZNC4DEF7GBKVANPDIY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYIENLJL6SRHBNDLGCYXS7NA5AA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsE5GLGFYXZNC4DEF7GBKVANPDIY"),0,

			/*Radix::Workflow::ProcessType:Properties-Properties*/
			new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

					/*Radix::Workflow::ProcessType:classGuid:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDIZMB2WMQFGFNKGNDPBZKRCHIM"),
						"classGuid",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecXI6CAOIGDBAGJEE6JQEGJME43Q"),
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

						/*Radix::Workflow::ProcessType:classGuid:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,100,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Workflow::ProcessType:algoClassGuid:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colE62KHOUF45E6JDWSYCA7TICXAI"),
						"algoClassGuid",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRRCVUKJTXBE6RI66HRIHK43ADM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecXI6CAOIGDBAGJEE6JQEGJME43Q"),
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
						org.radixware.kernel.common.enums.EEditPossibility.ONLY_IN_EDITOR,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("cpeBHKYXTAFPFGYJB5O5U73JODGSI"),
						true,

						/*Radix::Workflow::ProcessType:algoClassGuid:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,100,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Workflow::ProcessType:clerkRoleGuids:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col6SKMJNSZZNFVDNGCP26PG6QPEM"),
						"clerkRoleGuids",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAB5ECBSFXVFYFCSLBJKQJJNBL4"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecXI6CAOIGDBAGJEE6JQEGJME43Q"),
						org.radixware.kernel.common.enums.EValType.ARR_STR,
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
						org.radixware.kernel.common.enums.EEditPossibility.ONLY_IN_EDITOR,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,true,
						false,
						true,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("cpeVBBSP3NKG5HVVODE7MOX4U7DWI"),
						true,

						/*Radix::Workflow::ProcessType:clerkRoleGuids:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7MMIIZX3ENBJLACC7U2E4JMNHM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQDPLQ7UV3JBQNGNODDAJIQGRAA"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJJLDOUV7UREDTAKWKL3TGDLB7E"),
						false,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Workflow::ProcessType:guid:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colAEAAPJXBX5BIXHBNJOYP3FBIR4"),
						"guid",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecXI6CAOIGDBAGJEE6JQEGJME43Q"),
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

						/*Radix::Workflow::ProcessType:guid:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,50,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Workflow::ProcessType:notes:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXPT3ITROQNCC5OQ67AAKUEY2IQ"),
						"notes",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsURLGEQMWTFELDNUCBCK4G3FM3I"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecXI6CAOIGDBAGJEE6JQEGJME43Q"),
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
						org.radixware.kernel.common.enums.EEditPossibility.ONLY_IN_EDITOR,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Workflow::ProcessType:notes:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,true),
						null,
						null,
						null,
						true,-1,-1,1,
						true,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Workflow::ProcessType:processStoreDays:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNWJ6DLDISBBWZKPVZUMMGLM5LI"),
						"processStoreDays",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFNZCFQRTGVGLRDMD2IHSX7LUII"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecXI6CAOIGDBAGJEE6JQEGJME43Q"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("14"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ONLY_IN_EDITOR,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::Workflow::ProcessType:processStoreDays:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Workflow::ProcessType:title:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGG5VO4FGVBAMFB7SU4TOCRK7JE"),
						"title",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMQFFYCIKAVHZHL2MAELGDDRYOE"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecXI6CAOIGDBAGJEE6JQEGJME43Q"),
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
						org.radixware.kernel.common.enums.EEditPossibility.ONLY_IN_EDITOR,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Workflow::ProcessType:title:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Workflow::ProcessType:traceProfile:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colCGY7I2E76ZF7LNL4OVRXERFG7I"),
						"traceProfile",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2HDL4YPMGJGFLBAKFJ7J7U5A2Q"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecXI6CAOIGDBAGJEE6JQEGJME43Q"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("Event"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						true,false,
						false,
						true,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("cpeF3QUTT7XY3NRDB6ZAALOMT5GDM"),
						true,

						/*Radix::Workflow::ProcessType:traceProfile:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Workflow::ProcessType:algoClassTitle:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdPB3MR5M2DJA45AN54L4PE3DAI4"),
						"algoClassTitle",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJCKKQVHXU5H45CEZQMEOSIGBHI"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecXI6CAOIGDBAGJEE6JQEGJME43Q"),
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
						org.radixware.kernel.common.enums.EEditPossibility.ONLY_IN_EDITOR,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						true,false,
						false,
						true,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("cpeBHKYXTAFPFGYJB5O5U73JODGSI"),
						true,

						/*Radix::Workflow::ProcessType:algoClassTitle:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Workflow::ProcessType:algoBaseClassId:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd6R44AVB3PJBSDKZNDR6VJCDEPY"),
						"algoBaseClassId",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecXI6CAOIGDBAGJEE6JQEGJME43Q"),
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
						org.radixware.kernel.common.enums.EEditPossibility.PROGRAMMATICALLY,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Workflow::ProcessType:algoBaseClassId:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Workflow::ProcessType:adminRoles:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdJ5XPG7DBYZAF3CXPIOH2DTYQJE"),
						"adminRoles",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTCT36BSO2JH4DCVXLMJ3Z3XB3M"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecXI6CAOIGDBAGJEE6JQEGJME43Q"),
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
						org.radixware.kernel.common.enums.EEditPossibility.ONLY_IN_EDITOR,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						true,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("cpeVBBSP3NKG5HVVODE7MOX4U7DWI"),
						true,

						/*Radix::Workflow::ProcessType:adminRoles:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6IDICKIKQNDWDCVYLBU5XFRH5Q"),
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,true),

					/*Radix::Workflow::ProcessType:clerkRoles:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdQ5EFB5BHPVHPRLR7UT6ZQ2KKSM"),
						"clerkRoles",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMR2K2JNYKBDGXKMEQQK2RLXZSU"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecXI6CAOIGDBAGJEE6JQEGJME43Q"),
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
						org.radixware.kernel.common.enums.EEditPossibility.ONLY_IN_EDITOR,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						true,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("cpeVBBSP3NKG5HVVODE7MOX4U7DWI"),
						true,

						/*Radix::Workflow::ProcessType:clerkRoles:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6IDICKIKQNDWDCVYLBU5XFRH5Q"),
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,true),

					/*Radix::Workflow::ProcessType:classTitle:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd7WLWUH7XWFDXLEANP2KDS2QERY"),
						"classTitle",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVU7Q7FYEH5H4JASAFMEUNIVLCY"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecXI6CAOIGDBAGJEE6JQEGJME43Q"),
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
						org.radixware.kernel.common.types.Id.Factory.loadFrom("cpeBHKYXTAFPFGYJB5O5U73JODGSI"),
						true,

						/*Radix::Workflow::ProcessType:classTitle:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Workflow::ProcessType:adminRoleGuids:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col6KIGNQEL7NEWFDQMOEIO4POSTQ"),
						"adminRoleGuids",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsORPGK57FZFFJRBMZ55IK67ZQBU"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecXI6CAOIGDBAGJEE6JQEGJME43Q"),
						org.radixware.kernel.common.enums.EValType.ARR_STR,
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
						org.radixware.kernel.common.enums.EEditPossibility.ONLY_IN_EDITOR,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,true,
						false,
						true,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("cpeVBBSP3NKG5HVVODE7MOX4U7DWI"),
						true,

						/*Radix::Workflow::ProcessType:adminRoleGuids:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6ZAHRYA2AJDM7FQN6P44TVXI2U"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWCMZJT6KDZB6PO5UEJJNWMGCSA"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7ARF6TTIYRH5XIKVOLZ22S77ZM"),
						false,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false)
			},
			new org.radixware.kernel.common.client.meta.RadCommandDef[]{
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::Workflow::ProcessType:Export-Object Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdKLFZCC72RBBH3JQBLLJMESTOBE"),
						"Export",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecXI6CAOIGDBAGJEE6JQEGJME43Q"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVYOXIXVKK5F4BMLNMCXI5FHXSQ"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("imgVORNT5ZFORBEHLSYMJNUWJK6II"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("afhKKIYUMFUXFAUZA3O2PAJN4FUAE"),
						org.radixware.kernel.common.enums.ECommandNature.FORM_IN_OUT,
						true,
						false,
						true,
						org.radixware.kernel.common.enums.ECommandScope.OBJECT,
						null,
						org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_EXISTENT),
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::Workflow::ProcessType:Import-Object Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdRQKTKAFR3BA7ZP3EONGZ4P62VI"),
						"Import",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecXI6CAOIGDBAGJEE6JQEGJME43Q"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUENNN6BHZVCYTAGXTTBT6E5SBU"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("imgMUCCZVMA4JC4NMTVVEGYXEXU5Q"),
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
					/*Radix::Workflow::ProcessType:CfgReports-Object Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdCXSNTQ4O5BE3RPMMGDOJFIQM5Y"),
						"CfgReports",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecXI6CAOIGDBAGJEE6JQEGJME43Q"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGXTR75EMLNHGPGC4MIX7FZ6EAE"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("imgRHBTGK3WKVEXZMIUWW2MKPBHLA"),
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
			null,
			new org.radixware.kernel.common.client.meta.RadSortingDef[]{

					new org.radixware.kernel.common.client.meta.RadSortingDef(
					/*Radix::Workflow::ProcessType:Title-Sorting*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("srtXKQWFA3UPVBDTKWQW3ZLSDQRYM"),
						"Title",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecXI6CAOIGDBAGJEE6JQEGJME43Q"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFBK3KQPUOBG45AZESLN5B2ANCM"),
						new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem[]{

								new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGG5VO4FGVBAMFB7SU4TOCRK7JE"),
								false)
						})
			},
			null,
			new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprMVL6HOH2I5FNDBDHBTXNMT5QDQ"),org.radixware.kernel.common.types.Id.Factory.loadFrom("spr4DFZXOAU7BBU7NDXFVJ23NNZWM")},
			true,true,false);
}

/* Radix::Workflow::ProcessType - Web Executable*/

/*Radix::Workflow::ProcessType-Entity Class*/

package org.radixware.ads.Workflow.web;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType")
public interface ProcessType {
	/*Group model adapter class. This is the base class for 
	all of selector presentation models of this entity*/
	public static class DefaultGroupModel extends org.radixware.kernel.common.client.models.GroupModel{
		public DefaultGroupModel(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadSelectorPresentationDef def){super(userSession,def);}





		public org.radixware.ads.Workflow.web.ProcessType.ProcessType_DefaultModel getEntity(int i) throws InterruptedException, org.radixware.kernel.common.client.exceptions.BrokenEntityObjectException, org.radixware.kernel.common.exceptions.ServiceClientException { return (org.radixware.ads.Workflow.web.ProcessType.ProcessType_DefaultModel )  super.getEntity(i);}
	}







































































































	/*Radix::Workflow::ProcessType:adminRoleGuids:adminRoleGuids-Presentation Property*/


	public class AdminRoleGuids extends org.radixware.kernel.common.client.models.items.properties.PropertyArrStr{
		public AdminRoleGuids(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType:adminRoleGuids:adminRoleGuids")
		public  org.radixware.kernel.common.types.ArrStr getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType:adminRoleGuids:adminRoleGuids")
		public   void setValue(org.radixware.kernel.common.types.ArrStr val) {
			Value = val;
		}
	}
	public AdminRoleGuids getAdminRoleGuids();
	/*Radix::Workflow::ProcessType:clerkRoleGuids:clerkRoleGuids-Presentation Property*/


	public class ClerkRoleGuids extends org.radixware.kernel.common.client.models.items.properties.PropertyArrStr{
		public ClerkRoleGuids(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType:clerkRoleGuids:clerkRoleGuids")
		public  org.radixware.kernel.common.types.ArrStr getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType:clerkRoleGuids:clerkRoleGuids")
		public   void setValue(org.radixware.kernel.common.types.ArrStr val) {
			Value = val;
		}
	}
	public ClerkRoleGuids getClerkRoleGuids();
	/*Radix::Workflow::ProcessType:guid:guid-Presentation Property*/


	public class Guid extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public Guid(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType:guid:guid")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType:guid:guid")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Guid getGuid();
	/*Radix::Workflow::ProcessType:traceProfile:traceProfile-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType:traceProfile:traceProfile")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType:traceProfile:traceProfile")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public TraceProfile getTraceProfile();
	/*Radix::Workflow::ProcessType:classGuid:classGuid-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType:classGuid:classGuid")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType:classGuid:classGuid")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ClassGuid getClassGuid();
	/*Radix::Workflow::ProcessType:algoClassGuid:algoClassGuid-Presentation Property*/


	public class AlgoClassGuid extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public AlgoClassGuid(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType:algoClassGuid:algoClassGuid")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType:algoClassGuid:algoClassGuid")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public AlgoClassGuid getAlgoClassGuid();
	/*Radix::Workflow::ProcessType:title:title-Presentation Property*/


	public class Title extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public Title(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType:title:title")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType:title:title")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Title getTitle();
	/*Radix::Workflow::ProcessType:processStoreDays:processStoreDays-Presentation Property*/


	public class ProcessStoreDays extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public ProcessStoreDays(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType:processStoreDays:processStoreDays")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType:processStoreDays:processStoreDays")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public ProcessStoreDays getProcessStoreDays();
	/*Radix::Workflow::ProcessType:notes:notes-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType:notes:notes")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType:notes:notes")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Notes getNotes();
	/*Radix::Workflow::ProcessType:algoBaseClassId:algoBaseClassId-Presentation Property*/


	public class AlgoBaseClassId extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public AlgoBaseClassId(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType:algoBaseClassId:algoBaseClassId")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType:algoBaseClassId:algoBaseClassId")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public AlgoBaseClassId getAlgoBaseClassId();
	/*Radix::Workflow::ProcessType:classTitle:classTitle-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType:classTitle:classTitle")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType:classTitle:classTitle")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ClassTitle getClassTitle();
	/*Radix::Workflow::ProcessType:adminRoles:adminRoles-Presentation Property*/


	public class AdminRoles extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public AdminRoles(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Deprecated
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}













		@Deprecated

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType:adminRoles:adminRoles")
		public  Str getValue() {
			return Value;
		}
		@Deprecated

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType:adminRoles:adminRoles")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public AdminRoles getAdminRoles();
	/*Radix::Workflow::ProcessType:algoClassTitle:algoClassTitle-Presentation Property*/


	public class AlgoClassTitle extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public AlgoClassTitle(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType:algoClassTitle:algoClassTitle")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType:algoClassTitle:algoClassTitle")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public AlgoClassTitle getAlgoClassTitle();
	/*Radix::Workflow::ProcessType:clerkRoles:clerkRoles-Presentation Property*/


	public class ClerkRoles extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public ClerkRoles(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Deprecated
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}













		@Deprecated

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType:clerkRoles:clerkRoles")
		public  Str getValue() {
			return Value;
		}
		@Deprecated

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType:clerkRoles:clerkRoles")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ClerkRoles getClerkRoles();
	public static class CfgReports extends org.radixware.kernel.common.client.models.items.Command{
		protected CfgReports(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public void send() throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,null);
			processResponse(response, null);
		}

	}

	public static class Export extends org.radixware.kernel.common.client.models.items.Command{
		protected Export(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}

	}

	public static class Import extends org.radixware.kernel.common.client.models.items.Command{
		protected Import(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public org.radixware.schemas.types.StrDocument send(org.radixware.ads.Workflow.common.ImpExpXsd.ProcessTypeDocument input ) throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,input);
			org.apache.xmlbeans.XmlObject xmlResponse = processResponse(response, null);
			return (org.radixware.schemas.types.StrDocument)org.radixware.kernel.common.utils.XmlObjectProcessor.castToXmlClass(getClass().getClassLoader(),xmlResponse,org.radixware.schemas.types.StrDocument.class);
		}

	}



}

/* Radix::Workflow::ProcessType - Web Meta*/

/*Radix::Workflow::ProcessType-Entity Class*/

package org.radixware.ads.Workflow.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class ProcessType_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::Workflow::ProcessType:Presentations-Entity Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecXI6CAOIGDBAGJEE6JQEGJME43Q"),
			"Radix::Workflow::ProcessType",
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("imgYTQLIJ3AFIXRTYR3VKVAGGLM346GSPPC"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("spr4DFZXOAU7BBU7NDXFVJ23NNZWM"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsE5GLGFYXZNC4DEF7GBKVANPDIY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYIENLJL6SRHBNDLGCYXS7NA5AA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsE5GLGFYXZNC4DEF7GBKVANPDIY"),0,

			/*Radix::Workflow::ProcessType:Properties-Properties*/
			new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

					/*Radix::Workflow::ProcessType:classGuid:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDIZMB2WMQFGFNKGNDPBZKRCHIM"),
						"classGuid",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecXI6CAOIGDBAGJEE6JQEGJME43Q"),
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

						/*Radix::Workflow::ProcessType:classGuid:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,100,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Workflow::ProcessType:algoClassGuid:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colE62KHOUF45E6JDWSYCA7TICXAI"),
						"algoClassGuid",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRRCVUKJTXBE6RI66HRIHK43ADM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecXI6CAOIGDBAGJEE6JQEGJME43Q"),
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
						org.radixware.kernel.common.enums.EEditPossibility.ONLY_IN_EDITOR,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						true,

						/*Radix::Workflow::ProcessType:algoClassGuid:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,100,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Workflow::ProcessType:clerkRoleGuids:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col6SKMJNSZZNFVDNGCP26PG6QPEM"),
						"clerkRoleGuids",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAB5ECBSFXVFYFCSLBJKQJJNBL4"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecXI6CAOIGDBAGJEE6JQEGJME43Q"),
						org.radixware.kernel.common.enums.EValType.ARR_STR,
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
						org.radixware.kernel.common.enums.EEditPossibility.ONLY_IN_EDITOR,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,true,
						false,
						true,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("cpe7J5XAWOKZFHQTPMFLHATY3NMWQ"),
						true,

						/*Radix::Workflow::ProcessType:clerkRoleGuids:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7MMIIZX3ENBJLACC7U2E4JMNHM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQDPLQ7UV3JBQNGNODDAJIQGRAA"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJJLDOUV7UREDTAKWKL3TGDLB7E"),
						false,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Workflow::ProcessType:guid:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colAEAAPJXBX5BIXHBNJOYP3FBIR4"),
						"guid",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecXI6CAOIGDBAGJEE6JQEGJME43Q"),
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

						/*Radix::Workflow::ProcessType:guid:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,50,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Workflow::ProcessType:notes:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXPT3ITROQNCC5OQ67AAKUEY2IQ"),
						"notes",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsURLGEQMWTFELDNUCBCK4G3FM3I"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecXI6CAOIGDBAGJEE6JQEGJME43Q"),
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
						org.radixware.kernel.common.enums.EEditPossibility.ONLY_IN_EDITOR,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Workflow::ProcessType:notes:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,true),
						null,
						null,
						null,
						true,-1,-1,1,
						true,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Workflow::ProcessType:processStoreDays:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNWJ6DLDISBBWZKPVZUMMGLM5LI"),
						"processStoreDays",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFNZCFQRTGVGLRDMD2IHSX7LUII"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecXI6CAOIGDBAGJEE6JQEGJME43Q"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("14"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ONLY_IN_EDITOR,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::Workflow::ProcessType:processStoreDays:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Workflow::ProcessType:title:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGG5VO4FGVBAMFB7SU4TOCRK7JE"),
						"title",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMQFFYCIKAVHZHL2MAELGDDRYOE"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecXI6CAOIGDBAGJEE6JQEGJME43Q"),
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
						org.radixware.kernel.common.enums.EEditPossibility.ONLY_IN_EDITOR,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Workflow::ProcessType:title:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Workflow::ProcessType:traceProfile:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colCGY7I2E76ZF7LNL4OVRXERFG7I"),
						"traceProfile",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2HDL4YPMGJGFLBAKFJ7J7U5A2Q"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecXI6CAOIGDBAGJEE6JQEGJME43Q"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("Event"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						true,false,
						false,
						true,
						null,
						true,

						/*Radix::Workflow::ProcessType:traceProfile:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Workflow::ProcessType:algoClassTitle:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdPB3MR5M2DJA45AN54L4PE3DAI4"),
						"algoClassTitle",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJCKKQVHXU5H45CEZQMEOSIGBHI"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecXI6CAOIGDBAGJEE6JQEGJME43Q"),
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
						org.radixware.kernel.common.enums.EEditPossibility.ONLY_IN_EDITOR,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						true,false,
						false,
						true,
						null,
						true,

						/*Radix::Workflow::ProcessType:algoClassTitle:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Workflow::ProcessType:algoBaseClassId:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd6R44AVB3PJBSDKZNDR6VJCDEPY"),
						"algoBaseClassId",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecXI6CAOIGDBAGJEE6JQEGJME43Q"),
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
						org.radixware.kernel.common.enums.EEditPossibility.PROGRAMMATICALLY,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Workflow::ProcessType:algoBaseClassId:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Workflow::ProcessType:adminRoles:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdJ5XPG7DBYZAF3CXPIOH2DTYQJE"),
						"adminRoles",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTCT36BSO2JH4DCVXLMJ3Z3XB3M"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecXI6CAOIGDBAGJEE6JQEGJME43Q"),
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
						org.radixware.kernel.common.enums.EEditPossibility.ONLY_IN_EDITOR,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						true,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("cpe7J5XAWOKZFHQTPMFLHATY3NMWQ"),
						true,

						/*Radix::Workflow::ProcessType:adminRoles:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6IDICKIKQNDWDCVYLBU5XFRH5Q"),
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,true),

					/*Radix::Workflow::ProcessType:clerkRoles:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdQ5EFB5BHPVHPRLR7UT6ZQ2KKSM"),
						"clerkRoles",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMR2K2JNYKBDGXKMEQQK2RLXZSU"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecXI6CAOIGDBAGJEE6JQEGJME43Q"),
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
						org.radixware.kernel.common.enums.EEditPossibility.ONLY_IN_EDITOR,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						true,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("cpe7J5XAWOKZFHQTPMFLHATY3NMWQ"),
						true,

						/*Radix::Workflow::ProcessType:clerkRoles:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6IDICKIKQNDWDCVYLBU5XFRH5Q"),
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,true),

					/*Radix::Workflow::ProcessType:classTitle:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd7WLWUH7XWFDXLEANP2KDS2QERY"),
						"classTitle",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVU7Q7FYEH5H4JASAFMEUNIVLCY"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecXI6CAOIGDBAGJEE6JQEGJME43Q"),
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
						true,

						/*Radix::Workflow::ProcessType:classTitle:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Workflow::ProcessType:adminRoleGuids:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col6KIGNQEL7NEWFDQMOEIO4POSTQ"),
						"adminRoleGuids",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsORPGK57FZFFJRBMZ55IK67ZQBU"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecXI6CAOIGDBAGJEE6JQEGJME43Q"),
						org.radixware.kernel.common.enums.EValType.ARR_STR,
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
						org.radixware.kernel.common.enums.EEditPossibility.ONLY_IN_EDITOR,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,true,
						false,
						true,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("cpe7J5XAWOKZFHQTPMFLHATY3NMWQ"),
						true,

						/*Radix::Workflow::ProcessType:adminRoleGuids:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6ZAHRYA2AJDM7FQN6P44TVXI2U"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWCMZJT6KDZB6PO5UEJJNWMGCSA"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7ARF6TTIYRH5XIKVOLZ22S77ZM"),
						false,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false)
			},
			new org.radixware.kernel.common.client.meta.RadCommandDef[]{
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::Workflow::ProcessType:CfgReports-Object Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdCXSNTQ4O5BE3RPMMGDOJFIQM5Y"),
						"CfgReports",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecXI6CAOIGDBAGJEE6JQEGJME43Q"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGXTR75EMLNHGPGC4MIX7FZ6EAE"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("imgRHBTGK3WKVEXZMIUWW2MKPBHLA"),
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
			null,
			new org.radixware.kernel.common.client.meta.RadSortingDef[]{

					new org.radixware.kernel.common.client.meta.RadSortingDef(
					/*Radix::Workflow::ProcessType:Title-Sorting*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("srtXKQWFA3UPVBDTKWQW3ZLSDQRYM"),
						"Title",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecXI6CAOIGDBAGJEE6JQEGJME43Q"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFBK3KQPUOBG45AZESLN5B2ANCM"),
						new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem[]{

								new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGG5VO4FGVBAMFB7SU4TOCRK7JE"),
								false)
						})
			},
			null,
			new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprMVL6HOH2I5FNDBDHBTXNMT5QDQ"),org.radixware.kernel.common.types.Id.Factory.loadFrom("spr4DFZXOAU7BBU7NDXFVJ23NNZWM")},
			true,true,false);
}

/* Radix::Workflow::ProcessType:General - Desktop Meta*/

/*Radix::Workflow::ProcessType:General-Editor Presentation*/

package org.radixware.ads.Workflow.explorer;
public final class General_mi{
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new org.radixware.kernel.common.client.meta.RadEditorPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprMVL6HOH2I5FNDBDHBTXNMT5QDQ"),
	"General",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
	null,
	org.radixware.kernel.common.types.Id.Factory.loadFrom("aecXI6CAOIGDBAGJEE6JQEGJME43Q"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("tblXI6CAOIGDBAGJEE6JQEGJME43Q"),
	null,
	org.radixware.kernel.common.types.Id.Factory.loadFrom("img6ZK2AB6ZSBFNZNLO6MCJIB3I54"),

	/*Radix::Workflow::ProcessType:General:Editor Pages-Editor Presentation Pages*/
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

			/*Radix::Workflow::ProcessType:General:General-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgMEYMXXQ64JAAPIFVV2N7NGMCBM"),"General",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecXI6CAOIGDBAGJEE6JQEGJME43Q"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsH5C6B633VBFZNFKVWEV6EYJR4Q"),null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGG5VO4FGVBAMFB7SU4TOCRK7JE"),0,4,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdPB3MR5M2DJA45AN54L4PE3DAI4"),0,5,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colCGY7I2E76ZF7LNL4OVRXERFG7I"),0,8,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNWJ6DLDISBBWZKPVZUMMGLM5LI"),0,6,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXPT3ITROQNCC5OQ67AAKUEY2IQ"),0,9,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd7WLWUH7XWFDXLEANP2KDS2QERY"),0,0,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col6KIGNQEL7NEWFDQMOEIO4POSTQ"),0,1,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col6SKMJNSZZNFVDNGCP26PG6QPEM"),0,7,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdJ5XPG7DBYZAF3CXPIOH2DTYQJE"),0,2,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdQ5EFB5BHPVHPRLR7UT6ZQ2KKSM"),0,3,1,false,false)
			},null)
	},
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgMEYMXXQ64JAAPIFVV2N7NGMCBM"))}
	,

	/*Radix::Workflow::ProcessType:General:Children-Explorer Items*/
		new org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemDef[]{

				/*Radix::Workflow::ProcessType:General:Process-Child Ref Explorer Item*/

				new org.radixware.kernel.common.client.meta.explorerItems.RadChildRefExplorerItemDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiUMZFQPRC5NFXXOBPHI6BHE2SQ4"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aecXI6CAOIGDBAGJEE6JQEGJME43Q"),null,
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRDXQVFY6PLNRDANMABIFNQAABA"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("sprVE2DLMH2XTNRDF5NABIFNQAABA"),
					0,
					null,
					16560,true)
		}
	,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
	null,
	0,
	null,
	(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
	2176,0,0,null);
}
/* Radix::Workflow::ProcessType:General - Web Meta*/

/*Radix::Workflow::ProcessType:General-Editor Presentation*/

package org.radixware.ads.Workflow.web;
public final class General_mi{
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new org.radixware.kernel.common.client.meta.RadEditorPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprMVL6HOH2I5FNDBDHBTXNMT5QDQ"),
	"General",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
	null,
	org.radixware.kernel.common.types.Id.Factory.loadFrom("aecXI6CAOIGDBAGJEE6JQEGJME43Q"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("tblXI6CAOIGDBAGJEE6JQEGJME43Q"),
	null,
	org.radixware.kernel.common.types.Id.Factory.loadFrom("img6ZK2AB6ZSBFNZNLO6MCJIB3I54"),

	/*Radix::Workflow::ProcessType:General:Editor Pages-Editor Presentation Pages*/
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

			/*Radix::Workflow::ProcessType:General:General-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgMEYMXXQ64JAAPIFVV2N7NGMCBM"),"General",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecXI6CAOIGDBAGJEE6JQEGJME43Q"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsH5C6B633VBFZNFKVWEV6EYJR4Q"),null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGG5VO4FGVBAMFB7SU4TOCRK7JE"),0,4,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdPB3MR5M2DJA45AN54L4PE3DAI4"),0,5,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colCGY7I2E76ZF7LNL4OVRXERFG7I"),0,8,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNWJ6DLDISBBWZKPVZUMMGLM5LI"),0,6,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXPT3ITROQNCC5OQ67AAKUEY2IQ"),0,9,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd7WLWUH7XWFDXLEANP2KDS2QERY"),0,0,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col6KIGNQEL7NEWFDQMOEIO4POSTQ"),0,1,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col6SKMJNSZZNFVDNGCP26PG6QPEM"),0,7,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdJ5XPG7DBYZAF3CXPIOH2DTYQJE"),0,2,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdQ5EFB5BHPVHPRLR7UT6ZQ2KKSM"),0,3,1,false,false)
			},null)
	},
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgMEYMXXQ64JAAPIFVV2N7NGMCBM"))}
	,

	/*Radix::Workflow::ProcessType:General:Children-Explorer Items*/
		new org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemDef[]{

				/*Radix::Workflow::ProcessType:General:Process-Child Ref Explorer Item*/

				new org.radixware.kernel.common.client.meta.explorerItems.RadChildRefExplorerItemDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiUMZFQPRC5NFXXOBPHI6BHE2SQ4"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aecXI6CAOIGDBAGJEE6JQEGJME43Q"),null,
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRDXQVFY6PLNRDANMABIFNQAABA"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("sprVE2DLMH2XTNRDF5NABIFNQAABA"),
					0,
					null,
					16560,true)
		}
	,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
	null,
	0,
	null,
	(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
	2176,0,0,null);
}
/* Radix::Workflow::ProcessType:General:Model - Desktop Executable*/

/*Radix::Workflow::ProcessType:General:Model-Entity Model Class*/

package org.radixware.ads.Workflow.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType:General:Model")
public class General:Model  extends org.radixware.ads.Workflow.explorer.ProcessType.ProcessType_DefaultModel implements org.radixware.ads.Acs.common_client.IArrRoleHolder,org.radixware.ads.Workflow.common_client.IAlgoHolder,org.radixware.ads.Reports.common_client.IReportPubModel  {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return General:Model_mi.rdxMeta; }



	public General:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadEditorPresentationDef def){super(userSession,def);}

	/*Radix::Workflow::ProcessType:General:Model:Nested classes-Nested Classes*/

	/*Radix::Workflow::ProcessType:General:Model:Properties-Properties*/

	/*Radix::Workflow::ProcessType:General:Model:adminRoleGuids-Presentation Property*/




	public class AdminRoleGuids extends org.radixware.ads.Workflow.explorer.ProcessType.col6KIGNQEL7NEWFDQMOEIO4POSTQ{
		public AdminRoleGuids(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.types.ArrStr dummy = ((org.radixware.kernel.common.types.ArrStr)x);
			setValue(dummy);
		}

		/*Radix::Workflow::ProcessType:General:Model:adminRoleGuids:EmbeddedClass-Property Presentation Embedded Class*/




		/*Radix::Workflow::ProcessType:General:Model:adminRoleGuids:Nested classes-Nested Classes*/

		/*Radix::Workflow::ProcessType:General:Model:adminRoleGuids:Properties-Properties*/

		/*Radix::Workflow::ProcessType:General:Model:adminRoleGuids:Methods-Methods*/

		/*Radix::Workflow::ProcessType:General:Model:adminRoleGuids:createPropertyEditor-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType:General:Model:adminRoleGuids:createPropertyEditor")
		public published  org.radixware.kernel.common.client.views.IPropEditor createPropertyEditor () {
			return new RoleArrPropEditor(this);
		}













		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType:General:Model:adminRoleGuids")
		public  org.radixware.kernel.common.types.ArrStr getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType:General:Model:adminRoleGuids")
		public   void setValue(org.radixware.kernel.common.types.ArrStr val) {
			Value = val;
		}
	}
	public AdminRoleGuids getAdminRoleGuids(){return (AdminRoleGuids)getProperty(col6KIGNQEL7NEWFDQMOEIO4POSTQ);}

	/*Radix::Workflow::ProcessType:General:Model:clerkRoleGuids-Presentation Property*/




	public class ClerkRoleGuids extends org.radixware.ads.Workflow.explorer.ProcessType.col6SKMJNSZZNFVDNGCP26PG6QPEM{
		public ClerkRoleGuids(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.types.ArrStr dummy = ((org.radixware.kernel.common.types.ArrStr)x);
			setValue(dummy);
		}

		/*Radix::Workflow::ProcessType:General:Model:clerkRoleGuids:EmbeddedClass-Property Presentation Embedded Class*/




		/*Radix::Workflow::ProcessType:General:Model:clerkRoleGuids:Nested classes-Nested Classes*/

		/*Radix::Workflow::ProcessType:General:Model:clerkRoleGuids:Properties-Properties*/

		/*Radix::Workflow::ProcessType:General:Model:clerkRoleGuids:Methods-Methods*/

		/*Radix::Workflow::ProcessType:General:Model:clerkRoleGuids:createPropertyEditor-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType:General:Model:clerkRoleGuids:createPropertyEditor")
		public published  org.radixware.kernel.common.client.views.IPropEditor createPropertyEditor () {
			return new RoleArrPropEditor(this);
		}













		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType:General:Model:clerkRoleGuids")
		public  org.radixware.kernel.common.types.ArrStr getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType:General:Model:clerkRoleGuids")
		public   void setValue(org.radixware.kernel.common.types.ArrStr val) {
			Value = val;
		}
	}
	public ClerkRoleGuids getClerkRoleGuids(){return (ClerkRoleGuids)getProperty(col6SKMJNSZZNFVDNGCP26PG6QPEM);}










	/*Radix::Workflow::ProcessType:General:Model:Methods-Methods*/

	/*Radix::Workflow::ProcessType:General:Model:beforeOpenPropEditorDialog-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType:General:Model:beforeOpenPropEditorDialog")
	public published  void beforeOpenPropEditorDialog (org.radixware.kernel.common.client.models.items.properties.Property property, org.radixware.kernel.common.client.models.PropEditorModel propEditorModel) {
		if (property == algoClassTitle) {
		    Workflow.Dlg::AlgoChoiceDlg:Model m = (Workflow.Dlg::AlgoChoiceDlg:Model)propEditorModel;    
		    m.classRestriction = Common::CommonXsd:ClassRestriction.Factory.newInstance();
		    m.classRestriction.addNewSuperClass().DacId = Types::Id.Factory.loadFrom(algoBaseClassId.getValueAsString());
		    m.classRestriction.Abstract = false;
		}
	}

	/*Radix::Workflow::ProcessType:General:Model:getRolesIdsProp-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType:General:Model:getRolesIdsProp")
	public published  org.radixware.kernel.common.client.models.items.properties.PropertyArrStr getRolesIdsProp (org.radixware.kernel.common.types.Id roleNamePropId) {
		if (roleNamePropId == idof[ProcessType:adminRoles])
		   return adminRoleGuids;
		if (roleNamePropId == idof[ProcessType:clerkRoles])
		   return clerkRoleGuids;
		return null;
	}

	/*Radix::Workflow::ProcessType:General:Model:getAlgoPropId-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType:General:Model:getAlgoPropId")
	public published  org.radixware.kernel.common.client.models.items.properties.PropertyStr getAlgoPropId (org.radixware.kernel.common.types.Id algoNamePropId) {
		if (algoNamePropId == idof[ProcessType:algoClassTitle])
		   return algoClassGuid;
		return null;
	}

	/*Radix::Workflow::ProcessType:General:Model:getRolesTitlesProp-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType:General:Model:getRolesTitlesProp")
	public published  org.radixware.kernel.common.client.models.items.properties.PropertyStr getRolesTitlesProp (org.radixware.kernel.common.types.Id editedRolePropId) {
		if (editedRolePropId == idof[ProcessType:adminRoles])
		   return adminRoles;
		if (editedRolePropId == idof[ProcessType:clerkRoles])
		   return clerkRoles;
		return null;
	}

	/*Radix::Workflow::ProcessType:General:Model:getContextClassId-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType:General:Model:getContextClassId")
	public published  org.radixware.kernel.common.types.Id getContextClassId () {
		return idof[ProcessType];

	}

	/*Radix::Workflow::ProcessType:General:Model:getContextId-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType:General:Model:getContextId")
	public published  Str getContextId () {
		return this.guid.Value;
	}

	/*Radix::Workflow::ProcessType:General:Model:getParentContext-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType:General:Model:getParentContext")
	public published  org.radixware.ads.Reports.common_client.IReportPubModel getParentContext () {
		return new Reports::CommonReportContext();
	}

	/*Radix::Workflow::ProcessType:General:Model:getReportPubModel-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType:General:Model:getReportPubModel")
	public static  org.radixware.ads.Reports.common_client.IReportPubModel getReportPubModel (org.radixware.kernel.common.client.IClientEnvironment userSession, Str processTypeGuid) {
		try {
		    Explorer.Types::Pid processTypePid = new Pid(
		        idof[Workflow::ProcessType], processTypeGuid);
		    return (ProcessType:General:Model)openContextlessModel(userSession,
		        processTypePid, idof[ProcessType], idof[ProcessType:General]);
		} catch(Exceptions::InterruptedException e) {
		    return null;
		} catch(Exceptions::ServiceClientException e) {
		    return new Reports::CommonReportContext();    
		//    userSession.(ex);
		//    return null;
		}
	}

	/*Radix::Workflow::ProcessType:General:Model:onCommand_CfgReports-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType:General:Model:onCommand_CfgReports")
	protected  void onCommand_CfgReports (org.radixware.ads.Workflow.explorer.ProcessType.CfgReports command) {
		////update inner editor
		//try{
		//    boolean fOk = update();
		//    if(!fOk)
		//        return;
		//}catch( ex){
		//    return;
		//}catch(Exception ex){
		//    .(ex);
		//}

		Reports::ReportsExplorerUtils.cfgReports(
		        Environment,
		        idof[Reports:PubContext:ProcessType], // pubListDomainId
		        this); // IReportPubModel

	}

	/*Radix::Workflow::ProcessType:General:Model:onCommand_Import-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType:General:Model:onCommand_Import")
	protected published  void onCommand_Import (org.radixware.ads.Workflow.explorer.ProcessType.Import command) {
		java.io.File file = CfgManagement::DesktopUtils.openFileForImport(findNearestView(), command.getTitle());
		if (file == null)
		    return;

		try {
		    ImpExpXsd:ProcessTypeDocument input = ImpExpXsd:ProcessTypeDocument.Factory.parse(file);
		    Common.Dlg::ClientUtils.viewImportResult(command.send(input));
		    getView().reread();
		} catch (Exceptions::XmlException | Exceptions::ServiceClientException | Exceptions::IOException e) {
		    showException(e);
		} catch (Exceptions::InterruptedException e) {
		}
	}
	public final class CfgReports extends org.radixware.ads.Workflow.explorer.ProcessType.CfgReports{
		protected CfgReports(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_CfgReports( this );
		}

	}

	public final class Import extends org.radixware.ads.Workflow.explorer.ProcessType.Import{
		protected Import(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_Import( this );
		}

	}















}

/* Radix::Workflow::ProcessType:General:Model - Desktop Meta*/

/*Radix::Workflow::ProcessType:General:Model-Entity Model Class*/

package org.radixware.ads.Workflow.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class General:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aemMVL6HOH2I5FNDBDHBTXNMT5QDQ"),
						"General:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::Workflow::ProcessType:General:Model:Properties-Properties*/
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

/* Radix::Workflow::ProcessType:General:Model - Web Executable*/

/*Radix::Workflow::ProcessType:General:Model-Entity Model Class*/

package org.radixware.ads.Workflow.web;


@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType:General:Model")
public class General:Model  extends org.radixware.ads.Workflow.web.ProcessType.ProcessType_DefaultModel implements org.radixware.ads.Acs.common_client.IArrRoleHolder,org.radixware.ads.Workflow.common_client.IAlgoHolder,org.radixware.ads.Reports.common_client.IReportPubModel  {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return General:Model_mi.rdxMeta; }



	public General:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadEditorPresentationDef def){super(userSession,def);}

	/*Radix::Workflow::ProcessType:General:Model:Nested classes-Nested Classes*/

	/*Radix::Workflow::ProcessType:General:Model:Properties-Properties*/

	/*Radix::Workflow::ProcessType:General:Model:adminRoleGuids-Presentation Property*/




	public class AdminRoleGuids extends org.radixware.ads.Workflow.web.ProcessType.col6KIGNQEL7NEWFDQMOEIO4POSTQ{
		public AdminRoleGuids(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.types.ArrStr dummy = ((org.radixware.kernel.common.types.ArrStr)x);
			setValue(dummy);
		}

		/*Radix::Workflow::ProcessType:General:Model:adminRoleGuids:EmbeddedClass-Property Presentation Embedded Class*/




		/*Radix::Workflow::ProcessType:General:Model:adminRoleGuids:Nested classes-Nested Classes*/

		/*Radix::Workflow::ProcessType:General:Model:adminRoleGuids:Properties-Properties*/

		/*Radix::Workflow::ProcessType:General:Model:adminRoleGuids:Methods-Methods*/

		/*Radix::Workflow::ProcessType:General:Model:adminRoleGuids:createPropertyEditor-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType:General:Model:adminRoleGuids:createPropertyEditor")
		public published  org.radixware.kernel.common.client.views.IPropEditor createPropertyEditor () {
			return new RoleArrPropEditorWeb(this);
		}













		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType:General:Model:adminRoleGuids")
		public  org.radixware.kernel.common.types.ArrStr getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType:General:Model:adminRoleGuids")
		public   void setValue(org.radixware.kernel.common.types.ArrStr val) {
			Value = val;
		}
	}
	public AdminRoleGuids getAdminRoleGuids(){return (AdminRoleGuids)getProperty(col6KIGNQEL7NEWFDQMOEIO4POSTQ);}

	/*Radix::Workflow::ProcessType:General:Model:clerkRoleGuids-Presentation Property*/




	public class ClerkRoleGuids extends org.radixware.ads.Workflow.web.ProcessType.col6SKMJNSZZNFVDNGCP26PG6QPEM{
		public ClerkRoleGuids(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.types.ArrStr dummy = ((org.radixware.kernel.common.types.ArrStr)x);
			setValue(dummy);
		}

		/*Radix::Workflow::ProcessType:General:Model:clerkRoleGuids:EmbeddedClass-Property Presentation Embedded Class*/




		/*Radix::Workflow::ProcessType:General:Model:clerkRoleGuids:Nested classes-Nested Classes*/

		/*Radix::Workflow::ProcessType:General:Model:clerkRoleGuids:Properties-Properties*/

		/*Radix::Workflow::ProcessType:General:Model:clerkRoleGuids:Methods-Methods*/

		/*Radix::Workflow::ProcessType:General:Model:clerkRoleGuids:createPropertyEditor-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType:General:Model:clerkRoleGuids:createPropertyEditor")
		public published  org.radixware.kernel.common.client.views.IPropEditor createPropertyEditor () {
			return new RoleArrPropEditorWeb(this);
		}













		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType:General:Model:clerkRoleGuids")
		public  org.radixware.kernel.common.types.ArrStr getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType:General:Model:clerkRoleGuids")
		public   void setValue(org.radixware.kernel.common.types.ArrStr val) {
			Value = val;
		}
	}
	public ClerkRoleGuids getClerkRoleGuids(){return (ClerkRoleGuids)getProperty(col6SKMJNSZZNFVDNGCP26PG6QPEM);}










	/*Radix::Workflow::ProcessType:General:Model:Methods-Methods*/

	/*Radix::Workflow::ProcessType:General:Model:beforeOpenPropEditorDialog-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType:General:Model:beforeOpenPropEditorDialog")
	public published  void beforeOpenPropEditorDialog (org.radixware.kernel.common.client.models.items.properties.Property property, org.radixware.kernel.common.client.models.PropEditorModel propEditorModel) {

	}

	/*Radix::Workflow::ProcessType:General:Model:getRolesIdsProp-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType:General:Model:getRolesIdsProp")
	public published  org.radixware.kernel.common.client.models.items.properties.PropertyArrStr getRolesIdsProp (org.radixware.kernel.common.types.Id roleNamePropId) {
		if (roleNamePropId == idof[ProcessType:adminRoles])
		   return adminRoleGuids;
		if (roleNamePropId == idof[ProcessType:clerkRoles])
		   return clerkRoleGuids;
		return null;
	}

	/*Radix::Workflow::ProcessType:General:Model:getAlgoPropId-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType:General:Model:getAlgoPropId")
	public published  org.radixware.kernel.common.client.models.items.properties.PropertyStr getAlgoPropId (org.radixware.kernel.common.types.Id algoNamePropId) {
		if (algoNamePropId == idof[ProcessType:algoClassTitle])
		   return algoClassGuid;
		return null;
	}

	/*Radix::Workflow::ProcessType:General:Model:getRolesTitlesProp-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType:General:Model:getRolesTitlesProp")
	public published  org.radixware.kernel.common.client.models.items.properties.PropertyStr getRolesTitlesProp (org.radixware.kernel.common.types.Id editedRolePropId) {
		if (editedRolePropId == idof[ProcessType:adminRoles])
		   return adminRoles;
		if (editedRolePropId == idof[ProcessType:clerkRoles])
		   return clerkRoles;
		return null;
	}

	/*Radix::Workflow::ProcessType:General:Model:getContextClassId-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType:General:Model:getContextClassId")
	public published  org.radixware.kernel.common.types.Id getContextClassId () {
		return idof[ProcessType];

	}

	/*Radix::Workflow::ProcessType:General:Model:getContextId-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType:General:Model:getContextId")
	public published  Str getContextId () {
		return this.guid.Value;
	}

	/*Radix::Workflow::ProcessType:General:Model:getParentContext-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType:General:Model:getParentContext")
	public published  org.radixware.ads.Reports.common_client.IReportPubModel getParentContext () {
		return new Reports::CommonReportContext();
	}

	/*Radix::Workflow::ProcessType:General:Model:getReportPubModel-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType:General:Model:getReportPubModel")
	public static  org.radixware.ads.Reports.common_client.IReportPubModel getReportPubModel (org.radixware.kernel.common.client.IClientEnvironment userSession, Str processTypeGuid) {
		try {
		    Explorer.Types::Pid processTypePid = new Pid(
		        idof[Workflow::ProcessType], processTypeGuid);
		    return (ProcessType:General:Model)openContextlessModel(userSession,
		        processTypePid, idof[ProcessType], idof[ProcessType:General]);
		} catch(Exceptions::InterruptedException e) {
		    return null;
		} catch(Exceptions::ServiceClientException e) {
		    return new Reports::CommonReportContext();    
		//    userSession.(ex);
		//    return null;
		}
	}

	/*Radix::Workflow::ProcessType:General:Model:onCommand_CfgReports-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType:General:Model:onCommand_CfgReports")
	protected  void onCommand_CfgReports (org.radixware.ads.Workflow.web.ProcessType.CfgReports command) {
		////update inner editor
		//try{
		//    boolean fOk = update();
		//    if(!fOk)
		//        return;
		//}catch( ex){
		//    return;
		//}catch(Exception ex){
		//    .(ex);
		//}

		Reports::ReportsExplorerUtils.cfgReports(
		        Environment,
		        idof[Reports:PubContext:ProcessType], // pubListDomainId
		        this); // IReportPubModel

	}
	public final class CfgReports extends org.radixware.ads.Workflow.web.ProcessType.CfgReports{
		protected CfgReports(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_CfgReports( this );
		}

	}













}

/* Radix::Workflow::ProcessType:General:Model - Web Meta*/

/*Radix::Workflow::ProcessType:General:Model-Entity Model Class*/

package org.radixware.ads.Workflow.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class General:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aemMVL6HOH2I5FNDBDHBTXNMT5QDQ"),
						"General:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::Workflow::ProcessType:General:Model:Properties-Properties*/
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

/* Radix::Workflow::ProcessType:General - Desktop Meta*/

/*Radix::Workflow::ProcessType:General-Selector Presentation*/

package org.radixware.ads.Workflow.explorer;
public final class General_mi extends org.radixware.kernel.common.client.meta.RadSelectorPresentationDef{
	public static final org.radixware.kernel.common.client.meta.RadSelectorPresentationDef rdxMeta = new General_mi();
	private General_mi(){
		super(org.radixware.kernel.common.types.Id.Factory.loadFrom("spr4DFZXOAU7BBU7NDXFVJ23NNZWM"),
		"General",
		org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
		null,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("aecXI6CAOIGDBAGJEE6JQEGJME43Q"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("tblXI6CAOIGDBAGJEE6JQEGJME43Q"),
		null,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("img6ZK2AB6ZSBFNZNLO6MCJIB3I54"),
		null,
		null,
		true,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("srtXKQWFA3UPVBDTKWQW3ZLSDQRYM"),
		null,
		false,
		true,
		null,
		16416,
		null,
		128,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprMVL6HOH2I5FNDBDHBTXNMT5QDQ")},
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprMVL6HOH2I5FNDBDHBTXNMT5QDQ")},
		false,true,false,0,0);
		columns = new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn[]{

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGG5VO4FGVBAMFB7SU4TOCRK7JE"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdPB3MR5M2DJA45AN54L4PE3DAI4"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXPT3ITROQNCC5OQ67AAKUEY2IQ"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null)
		};
	}
}
/* Radix::Workflow::ProcessType:General - Web Meta*/

/*Radix::Workflow::ProcessType:General-Selector Presentation*/

package org.radixware.ads.Workflow.web;
public final class General_mi extends org.radixware.kernel.common.client.meta.RadSelectorPresentationDef{
	public static final org.radixware.kernel.common.client.meta.RadSelectorPresentationDef rdxMeta = new General_mi();
	private General_mi(){
		super(org.radixware.kernel.common.types.Id.Factory.loadFrom("spr4DFZXOAU7BBU7NDXFVJ23NNZWM"),
		"General",
		org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
		null,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("aecXI6CAOIGDBAGJEE6JQEGJME43Q"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("tblXI6CAOIGDBAGJEE6JQEGJME43Q"),
		null,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("img6ZK2AB6ZSBFNZNLO6MCJIB3I54"),
		null,
		null,
		true,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("srtXKQWFA3UPVBDTKWQW3ZLSDQRYM"),
		null,
		false,
		true,
		null,
		16416,
		null,
		128,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprMVL6HOH2I5FNDBDHBTXNMT5QDQ")},
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprMVL6HOH2I5FNDBDHBTXNMT5QDQ")},
		false,true,false,0,0);
		columns = new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn[]{

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGG5VO4FGVBAMFB7SU4TOCRK7JE"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdPB3MR5M2DJA45AN54L4PE3DAI4"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXPT3ITROQNCC5OQ67AAKUEY2IQ"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null)
		};
	}
}
/* Radix::Workflow::ProcessType:General:Model - Desktop Executable*/

/*Radix::Workflow::ProcessType:General:Model-Group Model Class*/

package org.radixware.ads.Workflow.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType:General:Model")
public class General:Model  extends org.radixware.ads.Workflow.explorer.ProcessType.DefaultGroupModel {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return General:Model_mi.rdxMeta; }



	public General:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadSelectorPresentationDef def){super(userSession,def);}

	/*Radix::Workflow::ProcessType:General:Model:Nested classes-Nested Classes*/

	/*Radix::Workflow::ProcessType:General:Model:Properties-Properties*/

	/*Radix::Workflow::ProcessType:General:Model:Methods-Methods*/

	/*Radix::Workflow::ProcessType:General:Model:onCommand_Import-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType:General:Model:onCommand_Import")
	protected published  void onCommand_Import (org.radixware.ads.Workflow.explorer.ProcessTypeGroup.Import command) {
		java.io.File file = CfgManagement::DesktopUtils.openFileForImport(findNearestView(), command.getTitle());
		if (file == null)
		    return;

		try {
		    ImpExpXsd:ProcessTypeGroupDocument xDoc;
		    try {
		        xDoc = ImpExpXsd:ProcessTypeGroupDocument.Factory.parse(file);
		    } catch (Exceptions::XmlException ex) {
		        ImpExpXsd:ProcessTypeDocument x = ImpExpXsd:ProcessTypeDocument.Factory.parse(file);
		        xDoc = ImpExpXsd:ProcessTypeGroupDocument.Factory.newInstance();
		        xDoc.addNewProcessTypeGroup().ItemList.add(x.ProcessType);
		    }
		    Common.Dlg::ClientUtils.viewImportResult(command.send(xDoc));
		    reread();
		} catch (Exceptions::XmlException | Exceptions::ServiceClientException | Exceptions::IOException e) {
		    showException(e);
		} catch (Exceptions::InterruptedException e) {
		}
	}
	public final class Import extends org.radixware.ads.Workflow.explorer.ProcessTypeGroup.Import{
		protected Import(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_Import( this );
		}

	}













}

/* Radix::Workflow::ProcessType:General:Model - Desktop Meta*/

/*Radix::Workflow::ProcessType:General:Model-Group Model Class*/

package org.radixware.ads.Workflow.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class General:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("agm4DFZXOAU7BBU7NDXFVJ23NNZWM"),
						"General:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::Workflow::ProcessType:General:Model:Properties-Properties*/
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

/* Radix::Workflow::ProcessType:General:Model - Web Executable*/

/*Radix::Workflow::ProcessType:General:Model-Group Model Class*/

package org.radixware.ads.Workflow.web;


@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Workflow::ProcessType:General:Model")
public class General:Model  extends org.radixware.ads.Workflow.web.ProcessType.DefaultGroupModel {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return General:Model_mi.rdxMeta; }



	public General:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadSelectorPresentationDef def){super(userSession,def);}

	/*Radix::Workflow::ProcessType:General:Model:Nested classes-Nested Classes*/

	/*Radix::Workflow::ProcessType:General:Model:Properties-Properties*/

	/*Radix::Workflow::ProcessType:General:Model:Methods-Methods*/


}

/* Radix::Workflow::ProcessType:General:Model - Web Meta*/

/*Radix::Workflow::ProcessType:General:Model-Group Model Class*/

package org.radixware.ads.Workflow.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class General:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("agm4DFZXOAU7BBU7NDXFVJ23NNZWM"),
						"General:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::Workflow::ProcessType:General:Model:Properties-Properties*/
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

/* Radix::Workflow::ProcessType - Localizing Bundle */
package org.radixware.ads.Workflow.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class ProcessType - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Invalid operator role");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Некорректная роль оператора");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls23CNQHKMHVGFJCNI3RWSDUFL7U"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Trace profile");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Профиль трассировки");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2HDL4YPMGJGFLBAKFJ7J7U5A2Q"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<unrestricted access>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<неограниченный доступ>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6IDICKIKQNDWDCVYLBU5XFRH5Q"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<unrestricted access>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<неограниченный доступ>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6ZAHRYA2AJDM7FQN6P44TVXI2U"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<prohibited>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<запрещено>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7ARF6TTIYRH5XIKVOLZ22S77ZM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<unrestricted access>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<неограниченный доступ>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7MMIIZX3ENBJLACC7U2E4JMNHM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Operator roles");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Роли оператора");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAB5ECBSFXVFYFCSLBJKQJJNBL4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Process Type");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Тип процесса");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsE5GLGFYXZNC4DEF7GBKVANPDIY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"By name");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"По названию");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFBK3KQPUOBG45AZESLN5B2ANCM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Storage period for closed process (days)");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Хранение завершенного процесса (дней)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFNZCFQRTGVGLRDMD2IHSX7LUII"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Configure Reports");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Настроить публикации отчетов");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGXTR75EMLNHGPGC4MIX7FZ6EAE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"General");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Общее");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsH5C6B633VBFZNFKVWEV6EYJR4Q"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Algorithm");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Алгоритм");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJCKKQVHXU5H45CEZQMEOSIGBHI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<prohibited>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<запрещено>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJJLDOUV7UREDTAKWKL3TGDLB7E"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Name");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Название");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMQFFYCIKAVHZHL2MAELGDDRYOE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Operator roles");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Роли оператора");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMR2K2JNYKBDGXKMEQQK2RLXZSU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Administrator roles");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Роли администратора");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsORPGK57FZFFJRBMZ55IK67ZQBU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<prohibited>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<запрещено>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQDPLQ7UV3JBQNGNODDAJIQGRAA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Algorithm");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Алгоритм");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRRCVUKJTXBE6RI66HRIHK43ADM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Administrator roles");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Роли администратора");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTCT36BSO2JH4DCVXLMJ3Z3XB3M"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Import");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Импортировать");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUENNN6BHZVCYTAGXTTBT6E5SBU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Notes");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Примечания");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsURLGEQMWTFELDNUCBCK4G3FM3I"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Class");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Класс");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVU7Q7FYEH5H4JASAFMEUNIVLCY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Export");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Экспортировать");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVYOXIXVKK5F4BMLNMCXI5FHXSQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<not defined>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<не задано>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsW7KDCTRGTJAJHFDM4L2TKWKVIA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<prohibited>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<запрещено>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWCMZJT6KDZB6PO5UEJJNWMGCSA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Invalid administrator role");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Некорректная роль администратора");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsY5FZJYILWNCBVBDBW45DEDRZWY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Process Types");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Типы процессов");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYIENLJL6SRHBNDLGCYXS7NA5AA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(ProcessType - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbaecXI6CAOIGDBAGJEE6JQEGJME43Q"),"ProcessType - Localizing Bundle",$$$items$$$);
}
