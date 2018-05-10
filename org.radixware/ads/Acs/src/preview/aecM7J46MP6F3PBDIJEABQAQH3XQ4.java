
/* Radix::Acs::AppRole - Server Executable*/

/*Radix::Acs::AppRole-Entity Class*/

package org.radixware.ads.Acs.server;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::AppRole")
public final published class AppRole  extends org.radixware.ads.Types.server.Entity  implements org.radixware.ads.CfgManagement.server.ICfgReferencedObject,org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {


	/**Metainformation accessor method*/
	@SuppressWarnings("unused")
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return AppRole_mi.rdxMeta;}

	/*Radix::Acs::AppRole:Nested classes-Nested Classes*/

	/*Radix::Acs::AppRole:Properties-Properties*/

	/*Radix::Acs::AppRole:definition-Column-Based Property*/










































	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::AppRole:definition")
	public published  org.radixware.schemas.adsdef.AdsDefinitionDocument getDefinition() {
		return definition;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::AppRole:definition")
	public published   void setDefinition(org.radixware.schemas.adsdef.AdsDefinitionDocument val) {
		definition = val;
	}

	/*Radix::Acs::AppRole:description-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::AppRole:description")
	public published  Str getDescription() {
		return description;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::AppRole:description")
	public published   void setDescription(Str val) {

		internal[description] = val;
		if (hasDefinition && definition.AdsDefinition != null && definition.AdsDefinition.AdsRoleDefinition != null) {
		    definition.AdsDefinition.AdsRoleDefinition.setDescription(val);
		}
	}

	/*Radix::Acs::AppRole:guid-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::AppRole:guid")
	public published  Str getGuid() {
		return guid;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::AppRole:guid")
	public published   void setGuid(Str val) {
		guid = val;
	}

	/*Radix::Acs::AppRole:hasDefinition-Dynamic Property*/



	protected Bool hasDefinition=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::AppRole:hasDefinition")
	public published  Bool getHasDefinition() {

		return definition!=null;
	}

	/*Radix::Acs::AppRole:isReadOnly-Dynamic Property*/



	protected Bool isReadOnly=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::AppRole:isReadOnly")
	public  Bool getIsReadOnly() {

		return !AcsUtils.isCurUserHasRole(java.util.Collections.singleton(idof[Arte::SuperAdmin]));
	}

	/*Radix::Acs::AppRole:lastUpdateTime-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::AppRole:lastUpdateTime")
	public published  java.sql.Timestamp getLastUpdateTime() {
		return lastUpdateTime;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::AppRole:lastUpdateTime")
	public published   void setLastUpdateTime(java.sql.Timestamp val) {
		lastUpdateTime = val;
	}

	/*Radix::Acs::AppRole:lastUpdateUserName-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::AppRole:lastUpdateUserName")
	public published  Str getLastUpdateUserName() {
		return lastUpdateUserName;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::AppRole:lastUpdateUserName")
	public published   void setLastUpdateUserName(Str val) {
		lastUpdateUserName = val;
	}

	/*Radix::Acs::AppRole:needsCompilation-Dynamic Property*/



	protected Bool needsCompilation=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::AppRole:needsCompilation")
	public published  Bool getNeedsCompilation() {

		return hasDefinition == true && runtime==null;
	}

	/*Radix::Acs::AppRole:roleClassGuid-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::AppRole:roleClassGuid")
	public published  Str getRoleClassGuid() {
		return roleClassGuid;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::AppRole:roleClassGuid")
	public published   void setRoleClassGuid(Str val) {
		roleClassGuid = val;
	}

	/*Radix::Acs::AppRole:runtime-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::AppRole:runtime")
	public published  java.sql.Blob getRuntime() {
		return runtime;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::AppRole:runtime")
	public published   void setRuntime(java.sql.Blob val) {
		runtime = val;
	}

	/*Radix::Acs::AppRole:title-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::AppRole:title")
	public published  Str getTitle() {
		return title;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::AppRole:title")
	public published   void setTitle(Str val) {

		internal[title] = val;
		if (hasDefinition && definition.AdsDefinition != null && definition.AdsDefinition.AdsRoleDefinition != null) {
		    definition.AdsDefinition.AdsRoleDefinition.setName(val);
		}

	}

	/*Radix::Acs::AppRole:changeLog-User Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::AppRole:changeLog")
	public published  org.radixware.ads.CfgManagement.server.ChangeLog getChangeLog() {
		return changeLog;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::AppRole:changeLog")
	public published   void setChangeLog(org.radixware.ads.CfgManagement.server.ChangeLog val) {
		changeLog = val;
	}

	/*Radix::Acs::AppRole:changeLogXmlFromUDSDesigner-Dynamic Property*/



	protected org.radixware.schemas.commondef.ChangeLogDocument changeLogXmlFromUDSDesigner=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::AppRole:changeLogXmlFromUDSDesigner")
	private final  org.radixware.schemas.commondef.ChangeLogDocument getChangeLogXmlFromUDSDesigner() {
		return changeLogXmlFromUDSDesigner;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::AppRole:changeLogXmlFromUDSDesigner")
	private final   void setChangeLogXmlFromUDSDesigner(org.radixware.schemas.commondef.ChangeLogDocument val) {
		changeLogXmlFromUDSDesigner = val;
	}































































































	/*Radix::Acs::AppRole:Methods-Methods*/

	/*Radix::Acs::AppRole:afterInit-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::AppRole:afterInit")
	protected published  void afterInit (org.radixware.kernel.server.types.Entity src, org.radixware.kernel.common.enums.EEntityInitializationPhase phase) {
		guid = Types::Id.Factory.newInstance(Meta::DefinitionIdPrefix:APPLICATION_ROLE).toString();
	}

	/*Radix::Acs::AppRole:beforeUpdate-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::AppRole:beforeUpdate")
	protected published  boolean beforeUpdate () {
		lastUpdateTime = new DateTime(System.currentTimeMillis());
		lastUpdateUserName = Arte::Arte.getUserName();

		if (changeLogXmlFromUDSDesigner != null) {
		    CfgManagement::ChangeLog.import(this, idof[AppRole:changeLog], true,
		            changeLogXmlFromUDSDesigner.getChangeLog(), null);
		    changeLogXmlFromUDSDesigner = null;
		}

		return super.beforeUpdate();
	}

	/*Radix::Acs::AppRole:loadByPidStr-System Method*/

	@SuppressWarnings("unused")
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::AppRole:loadByPidStr")
	 static  org.radixware.ads.Acs.server.AppRole loadByPidStr (Str pidAsStr, boolean checkExistance) {
	org.radixware.kernel.server.types.Pid pid = new org.radixware.kernel.server.types.Pid(org.radixware.ads.mdlPEKYFVDRVZHGZCBQQDY2NOYFOY.server.pdcArte______________________.__getArteInstanceInternal(),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblM7J46MP6F3PBDIJEABQAQH3XQ4"),pidAsStr);
		try{
		return (
		org.radixware.ads.Acs.server.AppRole) org.radixware.ads.mdlPEKYFVDRVZHGZCBQQDY2NOYFOY.server.pdcArte______________________.__getArteInstanceInternal().getEntityObject(pid,null,checkExistance);
	}catch(org.radixware.kernel.server.exceptions.EntityObjectNotExistsError e){
	return null;
	}

	}

	/*Radix::Acs::AppRole:loadByPK-System Method*/

	@SuppressWarnings("unused")
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::AppRole:loadByPK")
	public static published  org.radixware.ads.Acs.server.AppRole loadByPK (Str guid, boolean checkExistance) {
	final java.util.HashMap<org.radixware.kernel.common.types.Id,Object> pkValsMap = new java.util.HashMap<org.radixware.kernel.common.types.Id,Object>(5);
			if(guid==null) return null;
			pkValsMap.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("col6FMTUQ76F3PBDIJEABQAQH3XQ4"),guid);
	org.radixware.kernel.server.types.Pid pid = new org.radixware.kernel.server.types.Pid(org.radixware.ads.mdlPEKYFVDRVZHGZCBQQDY2NOYFOY.server.pdcArte______________________.__getArteInstanceInternal(),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblM7J46MP6F3PBDIJEABQAQH3XQ4"),pkValsMap);
		try{
		return (
		org.radixware.ads.Acs.server.AppRole) org.radixware.ads.mdlPEKYFVDRVZHGZCBQQDY2NOYFOY.server.pdcArte______________________.__getArteInstanceInternal().getEntityObject(pid,null,checkExistance);
	}catch(org.radixware.kernel.server.exceptions.EntityObjectNotExistsError e){
	return null;
	}

	}

	/*Radix::Acs::AppRole:create-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::AppRole:create")
	 static  org.radixware.ads.Acs.server.AppRole create (Str guid) {
		AppRole obj = new AppRole();
		obj.init();
		obj.guid = guid;
		return obj;
	}

	/*Radix::Acs::AppRole:exportAll-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::AppRole:exportAll")
	 static  void exportAll (org.radixware.ads.CfgManagement.server.CfgExportData data, java.util.Iterator<org.radixware.ads.Acs.server.AppRole> iter) {
		ImpExpAdsXsd:AppRolesDocument groupDoc = ImpExpAdsXsd:AppRolesDocument.Factory.newInstance();
		groupDoc.addNewAppRoles();

		if (iter == null) {
		    AppRolesCursor c = AppRolesCursor.open();
		    iter = new EntityCursorIterator<AppRole>(c, idof[AppRolesCursor:appRole]);
		}

		try {
		    while (iter.hasNext()) {
		        AppRole role = iter.next();
		        ImpExpAdsXsd:AppRoleDocument singleDoc = role.exportToXml();
		        data.children.add(new CfgExportData(role, null, idof[CfgItem.AppRoleSingle], singleDoc));
		        groupDoc.AppRoles.addNewItem().set(singleDoc.AppRole);
		    }
		} finally {
		    EntityCursorIterator.closeIterator(iter);
		}

		data.itemClassId = idof[CfgItem.AppRoleGroup];
		data.object = null;
		data.data = null;
		data.fileContent = groupDoc;
	}

	/*Radix::Acs::AppRole:exportThis-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::AppRole:exportThis")
	  void exportThis (org.radixware.ads.CfgManagement.server.CfgExportData data) {
		data.itemClassId = idof[CfgItem.AppRoleSingle];
		data.object = this;
		data.data = exportToXml();
		data.fileContent = data.data;

	}

	/*Radix::Acs::AppRole:exportToXml-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::AppRole:exportToXml")
	private final  org.radixware.ads.Acs.common.ImpExpAdsXsd.AppRoleDocument exportToXml () {
		ImpExpAdsXsd:AppRoleDocument doc = ImpExpAdsXsd:AppRoleDocument.Factory.newInstance();
		ImpExpAdsXsd:AppRole xml = doc.addNewAppRole();
		xml.Guid = guid;
		xml.Title = title;
		if (definition != null && definition.AdsDefinition != null)
		    xml.Definition = definition.AdsDefinition;
		xml.Description = description;

		if (changeLog != null) {
		    xml.addNewChangeLog().set(changeLog.export());
		}

		return doc;
	}

	/*Radix::Acs::AppRole:importAll-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::AppRole:importAll")
	 static  void importAll (org.radixware.ads.Acs.common.ImpExpAdsXsd.AppRoles xml, org.radixware.ads.CfgManagement.server.ICfgImportHelper helper) {
		for (ImpExpAdsXsd:AppRole x : xml.ItemList) {
		    importOne(x, helper);
		    if (helper.wasCancelled())
		        break;
		}

	}

	/*Radix::Acs::AppRole:importOne-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::AppRole:importOne")
	 static  org.radixware.ads.Acs.server.AppRole importOne (org.radixware.ads.Acs.common.ImpExpAdsXsd.AppRole xml, org.radixware.ads.CfgManagement.server.ICfgImportHelper helper) {
		if (xml == null)
		    return null;

		Str guid = xml.Guid;
		AppRole obj = AppRole.loadByPK(guid, true);
		if (obj == null) {
		    obj = create(guid);
		    obj.importThis(xml, helper);
		} else
		    switch (helper.getActionIfObjExists(obj)) {
		        case UPDATE:
		            obj.importThis(xml, helper);
		            break;
		        case NEW:
		            obj = create(Arte::Arte.generateGuid());
		            obj.importThis(xml, helper);
		            break;
		        case CANCELL:
		            helper.reportObjectCancelled(obj);
		            break;
		    }
		return obj;
	}

	/*Radix::Acs::AppRole:importThis-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::AppRole:importThis")
	  void importThis (org.radixware.ads.Acs.common.ImpExpAdsXsd.AppRole xml, org.radixware.ads.CfgManagement.server.ICfgImportHelper helper) {
		title = xml.Title;
		description = xml.Description;
		if (xml.Definition != null && xml.Definition.AdsRoleDefinition != null) {
		    org.radixware.schemas.adsdef.AdsDefinitionDocument xDoc = org.radixware.schemas.adsdef.AdsDefinitionDocument.Factory.newInstance();
		    xDoc.addNewAdsDefinition().AdsRoleDefinition = xml.Definition.AdsRoleDefinition;
		    xDoc.AdsDefinition.AdsRoleDefinition.Id = Types::Id.Factory.loadFrom(guid);
		    definition = xDoc;
		    runtime = null;
		    if (!compile()) {
		        helper.reportWarnings(this, "Compilation error"); 
		    }
		}

		CfgManagement::ChangeLog.import(this, idof[AppRole:changeLog], xml.isSetChangeLog(), xml.getChangeLog(), helper);

		helper.createOrUpdateAndReport(this);
	}

	/*Radix::Acs::AppRole:updateFromCfgItem-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::AppRole:updateFromCfgItem")
	  void updateFromCfgItem (org.radixware.ads.Acs.server.CfgItem.AppRoleSingle cfg, org.radixware.ads.CfgManagement.server.ICfgImportHelper helper) {
		importThis(cfg.myData.AppRole, helper);


	}

	/*Radix::Acs::AppRole:onCommand_Import-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::AppRole:onCommand_Import")
	public  org.radixware.schemas.types.StrDocument onCommand_Import (org.radixware.ads.Acs.common.ImpExpAdsXsd.AppRoleDocument input, org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValHandlersById) {
		CfgManagement::CfgImportHelper.Interactive helper = new CfgImportHelper.Interactive(false, true);
		importThis(input.AppRole, helper);
		return helper.getResultsAsHtmlStr();

	}

	/*Radix::Acs::AppRole:compile-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::AppRole:compile")
	public  boolean compile () {

		final Utils::UDSCompiler compiler = new Utils::UDSCompiler();
		return compiler.compileAppRole(Types::Id.Factory.loadFrom(this.guid), null, new Utils::UDSCompiler.ResultRequestor() {
		    public void accept(byte[] data,String mainClassLocalName, org.radixware.kernel.common.defs.ads.AdsDefinition compileDef) {
		        try {
		            //System.out.println(mainClassLocalName);
		            if (data == null) {
		                runtime = null;
		            } else {
		                Blob blob = Arte::Arte.createTemporaryBlob();
		                blob.setBytes(1, data);
		                runtime = blob;
		            }
		            roleClassGuid = mainClassLocalName;            
		        } catch (Exceptions::SQLException e) {
		            Arte::Trace.error(org.radixware.kernel.common.utils.ExceptionTextFormatter.exceptionStackToString(e), Arte::EventSource:UserFunc);
		        }        
		    }
		});
	}

	/*Radix::Acs::AppRole:onCommand_Compile-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::AppRole:onCommand_Compile")
	public  org.radixware.kernel.server.types.FormHandler.NextDialogsRequest onCommand_Compile (org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValHandlersById) {
		if (!compile()) {
		    return new FormHandlerNextDialogsRequest(new FormHandlerMessageBoxRequest(Client.Resources::DialogType:Error, null, Client.Resources::DialogButtonType:Close, null, "Compilation failed. For details, see the event log."), null);
		} else {
		    return new FormHandlerNextDialogsRequest(new FormHandlerMessageBoxRequest(Client.Resources::DialogType:Information, null, Client.Resources::DialogButtonType:Close, null, "Compilation completed successfully"), null);
		}
	}

	/*Radix::Acs::AppRole:getCfgReferenceExtGuid-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::AppRole:getCfgReferenceExtGuid")
	public published  Str getCfgReferenceExtGuid () {
		return guid;
	}

	/*Radix::Acs::AppRole:getCfgReferencePropId-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::AppRole:getCfgReferencePropId")
	public published  org.radixware.kernel.common.types.Id getCfgReferencePropId () {
		return idof[AppRole:guid];
	}




	@Override
	public org.radixware.kernel.server.types.FormHandler.NextDialogsRequest execCommand(org.radixware.kernel.common.types.Id cmdId, org.radixware.kernel.common.types.Id propId, org.apache.xmlbeans.XmlObject input, org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValsById, org.apache.xmlbeans.XmlObject output) throws org.radixware.kernel.common.exceptions.AppException,java.lang.InterruptedException{
		if(cmdId == cmd5UMGTFSOORA7LBW4ARAK74I7TI){
			org.radixware.schemas.types.StrDocument result = onCommand_Import((org.radixware.ads.Acs.common.ImpExpAdsXsd.AppRoleDocument)org.radixware.kernel.common.utils.XmlObjectProcessor.getAppCommandInputData(getClass().getClassLoader(),input,org.radixware.ads.Acs.common.ImpExpAdsXsd.AppRoleDocument.class),newPropValsById);
			if(result != null)
				output.set(result);
			return null;
		} else if(cmdId == cmdR6RR7IJLIJAN3FANR5JYHG6QYA){
			org.radixware.kernel.server.types.FormHandler.NextDialogsRequest result = onCommand_Compile(newPropValsById);
		return result;
	} else 
		return super.execCommand(cmdId,propId,input,newPropValsById,output);
}


}

/* Radix::Acs::AppRole - Server Meta*/

/*Radix::Acs::AppRole-Entity Class*/

package org.radixware.ads.Acs.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class AppRole_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecM7J46MP6F3PBDIJEABQAQH3XQ4"),"AppRole",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBZPQ4TD4V5CNHMIEKJ6JBTJFUU"),

						org.radixware.kernel.common.enums.EClassType.ENTITY,

						/*Radix::Acs::AppRole:Presentations-Entity Presentations*/
						/*Class presentation attributes*/
						 new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef(/*Owner Class Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("aecM7J46MP6F3PBDIJEABQAQH3XQ4"),
							/*Owner Class Name*/
							"AppRole",
							/*Title Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBZPQ4TD4V5CNHMIEKJ6JBTJFUU"),
							/*Property presentations*/

							/*Radix::Acs::AppRole:Properties-Properties*/
							new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef[]{

									/*Radix::Acs::AppRole:definition:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGQVSU7TJYRFCZFU7TADGWIRGHY"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Acs::AppRole:description:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPJZG5RYZF7PBDIJEABQAQH3XQ4"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Acs::AppRole:guid:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col6FMTUQ76F3PBDIJEABQAQH3XQ4"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Acs::AppRole:hasDefinition:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdEHFTROTHHJDPDOCTBXBOJAC3ZI"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Acs::AppRole:isReadOnly:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdPZ5BW3BSXBE2RBOY2D3QOUWQ7M"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Acs::AppRole:lastUpdateTime:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colWSYOEXVDBRAHRM4KKO5H3QANS4"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Acs::AppRole:lastUpdateUserName:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDA2H7YX6XRGOFCEEKSO65GQN7A"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Acs::AppRole:needsCompilation:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdYJ74C6AIXNB27BADDMBJYKLIN4"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Acs::AppRole:roleClassGuid:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIXVB57MI2RADLC5IAUFV5QB67Y"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Acs::AppRole:runtime:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPJH2TGKX3JAIDCK2GK4H763RDQ"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,true,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Acs::AppRole:title:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMFRUYXP6F3PBDIJEABQAQH3XQ4"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Acs::AppRole:changeLog:PropertyPresentation-Object Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadParentTitlePropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruEYW22P42DVAO5JTUAFUG2EMX5E"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,null,null,null,null,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprVRZPY6RJKBHWFLK46ATZLXFW3U")},null,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr6HHEKF43V5BUPOUOE7WW7JP2PU")},java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.TITLE,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Acs::AppRole:changeLogXmlFromUDSDesigner:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdQTZ7TUYLCVAPREYFWLZIE2OUM4"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT))
							},
							/*Commands*/
							new org.radixware.kernel.server.meta.presentations.RadCommandDef[]{

									new org.radixware.kernel.server.meta.presentations.RadCommandDef(
									/*Radix::Acs::AppRole:Export-Object Command*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdX2MHBEYNHNCAHAH2IVXSJDRUDM"),"Export",org.radixware.kernel.common.enums.ECommandScope.OBJECT,null,org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_FIXED,org.radixware.kernel.common.enums.ECommandNature.FORM_IN_OUT,org.radixware.kernel.common.types.Id.Factory.loadFrom("aecM7J46MP6F3PBDIJEABQAQH3XQ4"),true,true),

									new org.radixware.kernel.server.meta.presentations.RadCommandDef(
									/*Radix::Acs::AppRole:Import-Object Command*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("cmd5UMGTFSOORA7LBW4ARAK74I7TI"),"Import",org.radixware.kernel.common.enums.ECommandScope.OBJECT,null,org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_FIXED,org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,org.radixware.kernel.common.types.Id.Factory.loadFrom("aecM7J46MP6F3PBDIJEABQAQH3XQ4"),false,true),

									new org.radixware.kernel.server.meta.presentations.RadCommandDef(
									/*Radix::Acs::AppRole:Compile-Object Command*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdR6RR7IJLIJAN3FANR5JYHG6QYA"),"Compile",org.radixware.kernel.common.enums.ECommandScope.OBJECT,null,org.radixware.kernel.common.enums.ECommandAccessibility.ALWAYS,org.radixware.kernel.common.enums.ECommandNature.XML_IN_FORM_OUT,org.radixware.kernel.common.types.Id.Factory.loadFrom("aecM7J46MP6F3PBDIJEABQAQH3XQ4"),false,true)
							},
							/*Sortings*/
							new org.radixware.kernel.server.meta.presentations.RadSortingDef[]{

									new org.radixware.kernel.server.meta.presentations.RadSortingDef(
									/*Radix::Acs::AppRole:Title-Sorting*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("srt4XDUO7EF7JDQHCKNGCN3EV46JQ"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecM7J46MP6F3PBDIJEABQAQH3XQ4"),"Title",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOGEVUI6JKNF6JPMVJNB3Q7GJOM"),new org.radixware.kernel.server.meta.presentations.RadSortingDef.Item[]{

											new org.radixware.kernel.server.meta.presentations.RadSortingDef.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMFRUYXP6F3PBDIJEABQAQH3XQ4"),org.radixware.kernel.common.enums.EOrder.ASC)
									},"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware")
							},
							/*Filters*/
							null,
							/*Editor presentations*/
							new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef[]{

									new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef(
									/*Radix::Acs::AppRole:General-Editor Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprSIJFTYEHKVAQLBWEFIMTRLF5AQ"),
									"General",
									org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
									null,
									2192,
									null,

									/*Radix::Acs::AppRole:General:Children-Explorer Items*/
										null
									,
									org.radixware.kernel.server.types.Restrictions.Factory.newInstance(0,null,null,null),
									(org.radixware.kernel.server.meta.presentations.RadPropertyPresentationAttributes[]) null,
									org.radixware.kernel.common.enums.EEditorPresentationRightsInheritanceMode.NONE,
									null,null)
							},
							/*Selector presentations*/
							new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef[]{

									new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef(
									/*Radix::Acs::AppRole:General-Selector Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("sprOBP7OWB2JNC67FC7U7RDMNFMMA"),"General",null,144,new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn[]{

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMFRUYXP6F3PBDIJEABQAQH3XQ4"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPJZG5RYZF7PBDIJEABQAQH3XQ4"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col6FMTUQ76F3PBDIJEABQAQH3XQ4"),false)
									},new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.Addons(org.radixware.kernel.common.types.Id.Factory.loadFrom("srt4XDUO7EF7JDQHCKNGCN3EV46JQ"),true,null,false,null,true,null,false,false,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprSIJFTYEHKVAQLBWEFIMTRLF5AQ")},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprSIJFTYEHKVAQLBWEFIMTRLF5AQ")},org.radixware.kernel.server.types.Restrictions.Factory.newInstance(32,null,null,null),null)
							},
							/*Default selector presentation Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("sprOBP7OWB2JNC67FC7U7RDMNFMMA"),
							/*Presentation Map*/
							null,
							/*Object title format*/

							/*Radix::Acs::AppRole:TitleFormat-Object Title Format*/

							new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecM7J46MP6F3PBDIJEABQAQH3XQ4"),new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.TitleItem[]{

									new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.TitleItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecM7J46MP6F3PBDIJEABQAQH3XQ4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colMFRUYXP6F3PBDIJEABQAQH3XQ4"),"",org.radixware.kernel.common.enums.ETitleNullFormat.SAME_AS_NOT_NULL,null)
							},null,org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.DefinitionContextType.ENTITY),
							/*Class catalogs*/
							null,
							/*Restrictions*/
							org.radixware.kernel.server.types.Restrictions.Factory.newInstance(0,null,null,null),null,0),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblM7J46MP6F3PBDIJEABQAQH3XQ4"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("pdcEntity____________________"),

						/*Radix::Acs::AppRole:Properties-Properties*/
						new org.radixware.kernel.server.meta.clazzes.RadPropDef[]{

								/*Radix::Acs::AppRole:definition-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGQVSU7TJYRFCZFU7TADGWIRGHY"),"definition",null,org.radixware.kernel.common.enums.EValType.XML,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Acs::AppRole:description-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPJZG5RYZF7PBDIJEABQAQH3XQ4"),"description",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXV35CME5JVG3DI2U5CYYDMHQEY"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Acs::AppRole:guid-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col6FMTUQ76F3PBDIJEABQAQH3XQ4"),"guid",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4H6AAGJKINAT7PSYSDTOFSXDJI"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Acs::AppRole:hasDefinition-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdEHFTROTHHJDPDOCTBXBOJAC3ZI"),"hasDefinition",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsF2MX2KYM75GVZIOUMEXHFT53DI"),org.radixware.kernel.common.enums.EValType.BOOL,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadAccessorProxy.R_ACCESSOR_INSTANCE),

								/*Radix::Acs::AppRole:isReadOnly-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdPZ5BW3BSXBE2RBOY2D3QOUWQ7M"),"isReadOnly",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEA6G5LJHDFAQJHTMH6M5P46PCQ"),org.radixware.kernel.common.enums.EValType.BOOL,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadAccessorProxy.R_ACCESSOR_INSTANCE),

								/*Radix::Acs::AppRole:lastUpdateTime-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colWSYOEXVDBRAHRM4KKO5H3QANS4"),"lastUpdateTime",null,org.radixware.kernel.common.enums.EValType.DATE_TIME,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Acs::AppRole:lastUpdateUserName-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDA2H7YX6XRGOFCEEKSO65GQN7A"),"lastUpdateUserName",null,org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Acs::AppRole:needsCompilation-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdYJ74C6AIXNB27BADDMBJYKLIN4"),"needsCompilation",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJMKZBJOQYBDPLIRRQD6GUQUMHY"),org.radixware.kernel.common.enums.EValType.BOOL,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadAccessorProxy.R_ACCESSOR_INSTANCE),

								/*Radix::Acs::AppRole:roleClassGuid-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIXVB57MI2RADLC5IAUFV5QB67Y"),"roleClassGuid",null,org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Acs::AppRole:runtime-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPJH2TGKX3JAIDCK2GK4H763RDQ"),"runtime",null,org.radixware.kernel.common.enums.EValType.BLOB,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Acs::AppRole:title-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMFRUYXP6F3PBDIJEABQAQH3XQ4"),"title",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4QWTMNQ4NJARTMH45D72HK4QYA"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Acs::AppRole:changeLog-User Property*/

								new org.radixware.kernel.server.meta.clazzes.RadUserRefPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruEYW22P42DVAO5JTUAFUG2EMX5E"),"changeLog",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEYCDXBU5ZNEFNA7B6DEFBU42JI"),org.radixware.kernel.common.enums.EValType.OBJECT,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("tblM7J46MP6F3PBDIJEABQAQH3XQ4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecVHY5COSHWNA7PPJ65FW3OGJ4ZQ"),false,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Acs::AppRole:changeLogXmlFromUDSDesigner-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdQTZ7TUYLCVAPREYFWLZIE2OUM4"),"changeLogXmlFromUDSDesigner",null,org.radixware.kernel.common.enums.EValType.XML,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE)
						},

						/*Radix::Acs::AppRole:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthD77F2HBL2FGSVPJHR3SEAMPWMA"),"afterInit",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("src",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprMT7N3DE3R5H4NBJAO43SZPH6ZI")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("phase",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprR4Z2EO2Y7VCWPG3MLWIFAUHTEY"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthPT5PMP2WVZGUDB7OTLOE2SBWKE"),"beforeUpdate",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth_loadByPidStr_____________"),"loadByPidStr",true,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("pidAsStr",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprPidAsStr__________________")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("checkExistance",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprCheckExisstance___________"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth_loadByPK_________________"),"loadByPK",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("guid",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr6FMTUQ76F3PBDIJEABQAQH3XQ4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("checkExistance",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprCheckExisstance___________"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth54JDQOIZM5GHRIVTWK6AYEWYHI"),"create",true,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("guid",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprRKD4DQJZ45BCBCWQDGOHYJNCVY"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthDB7EHCYBCZGZZDXCY4XXI23TLE"),"exportAll",true,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("data",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprSY7BLG7O3BHKBIUZS7UE5KVS3Y")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("iter",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprNZ7N537KABHVZMCS64BYAZPGV4"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthCVZNC2UCFNH6XBGPJTOUWB4EKI"),"exportThis",false,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("data",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprSY7BLG7O3BHKBIUZS7UE5KVS3Y"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth63MARFJMDBGBNEJATBU2HCGM2E"),"exportToXml",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,null,org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthZO5XGMP76BGXVL2F3OOZUW6H7Q"),"importAll",true,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("xml",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr2RLOK42HYZCS3LYDTUKUPR2RD4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("helper",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr5AEE44BN5VCFBEDIUMN2FNENKA"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthTIGI5QQZZFD6VFHALC6NTLG34U"),"importOne",true,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("xml",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr2RLOK42HYZCS3LYDTUKUPR2RD4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("helper",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprUAHGASFHCNFIBNEQK3K2GZ7XLQ"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthH6FNOI7QKFBMDFTBABV6IVXKQM"),"importThis",false,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("xml",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr2RLOK42HYZCS3LYDTUKUPR2RD4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("helper",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprPK55TF3CIRCULIX6OX7FD4N2LQ"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthOV7LAREKKVDW7LYCHKFZYTVIFM"),"updateFromCfgItem",false,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("cfg",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprVAMRIOYK4ZGAJIMTDCHQBGSUZ4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("helper",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr5IUSIB24WNFVZOBXWL2TL57JUQ"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthcmd5UMGTFSOORA7LBW4ARAK74I7TI"),"onCommand_Import",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("input",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprAXZ74RRJWJDNLJNUKMCOP5QHRI")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("newPropValHandlersById",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprH6TTI5N3J5EPTLOCRUG6KF7CXY"))
								},org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthW2IA22TGKBBPZALWPTXJ3LVVU4"),"compile",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthcmdR6RR7IJLIJAN3FANR5JYHG6QYA"),"onCommand_Compile",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("newPropValHandlersById",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprUUVXSPOBIRGYPAAGMY6MI74KSE"))
								},org.radixware.kernel.common.enums.EValType.JAVA_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthQXRW7PZ4QBBEJAJL57JFPES7EQ"),"getCfgReferenceExtGuid",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthTIFZEEQC2NEUNPFIRAGRH2B7EY"),"getCfgReferencePropId",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.JAVA_CLASS)
						},
						null,
						org.radixware.kernel.common.enums.EAccessAreaType.NONE,null,null,false);
}

/* Radix::Acs::AppRole - Desktop Executable*/

/*Radix::Acs::AppRole-Entity Class*/

package org.radixware.ads.Acs.explorer;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::AppRole")
public interface AppRole {
	/*Group model adapter class. This is the base class for 
	all of selector presentation models of this entity*/
	public static class DefaultGroupModel extends org.radixware.kernel.common.client.models.GroupModel{
		public DefaultGroupModel(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadSelectorPresentationDef def){super(userSession,def);}





		public org.radixware.ads.Acs.explorer.AppRole.AppRole_DefaultModel getEntity(int i) throws InterruptedException, org.radixware.kernel.common.client.exceptions.BrokenEntityObjectException, org.radixware.kernel.common.exceptions.ServiceClientException { return (org.radixware.ads.Acs.explorer.AppRole.AppRole_DefaultModel )  super.getEntity(i);}
	}


































































































	/*Radix::Acs::AppRole:guid:guid-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::AppRole:guid:guid")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::AppRole:guid:guid")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Guid getGuid();
	/*Radix::Acs::AppRole:lastUpdateUserName:lastUpdateUserName-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::AppRole:lastUpdateUserName:lastUpdateUserName")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::AppRole:lastUpdateUserName:lastUpdateUserName")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public LastUpdateUserName getLastUpdateUserName();
	/*Radix::Acs::AppRole:definition:definition-Presentation Property*/


	public class Definition extends org.radixware.kernel.common.client.models.items.properties.PropertyXml{
		public Definition(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Class<org.radixware.schemas.adsdef.AdsDefinitionDocument> getValClass(){
			return org.radixware.schemas.adsdef.AdsDefinitionDocument.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.schemas.adsdef.AdsDefinitionDocument dummy = x == null ? null : (org.radixware.schemas.adsdef.AdsDefinitionDocument)org.radixware.kernel.common.utils.XmlObjectProcessor.castToXmlClass(getClass().getClassLoader(),(org.apache.xmlbeans.XmlObject)x,org.radixware.schemas.adsdef.AdsDefinitionDocument.class);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::AppRole:definition:definition")
		public  org.radixware.schemas.adsdef.AdsDefinitionDocument getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::AppRole:definition:definition")
		public   void setValue(org.radixware.schemas.adsdef.AdsDefinitionDocument val) {
			Value = val;
		}
	}
	public Definition getDefinition();
	/*Radix::Acs::AppRole:roleClassGuid:roleClassGuid-Presentation Property*/


	public class RoleClassGuid extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public RoleClassGuid(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::AppRole:roleClassGuid:roleClassGuid")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::AppRole:roleClassGuid:roleClassGuid")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public RoleClassGuid getRoleClassGuid();
	/*Radix::Acs::AppRole:title:title-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::AppRole:title:title")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::AppRole:title:title")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Title getTitle();
	/*Radix::Acs::AppRole:runtime:runtime-Presentation Property*/


	public class Runtime extends org.radixware.kernel.common.client.models.items.properties.PropertyBlob{
		public Runtime(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.types.Bin dummy = ((org.radixware.kernel.common.types.Bin)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::AppRole:runtime:runtime")
		public  org.radixware.kernel.common.types.Bin getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::AppRole:runtime:runtime")
		public   void setValue(org.radixware.kernel.common.types.Bin val) {
			Value = val;
		}
	}
	public Runtime getRuntime();
	/*Radix::Acs::AppRole:description:description-Presentation Property*/


	public class Description extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public Description(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::AppRole:description:description")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::AppRole:description:description")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Description getDescription();
	/*Radix::Acs::AppRole:lastUpdateTime:lastUpdateTime-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::AppRole:lastUpdateTime:lastUpdateTime")
		public  java.sql.Timestamp getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::AppRole:lastUpdateTime:lastUpdateTime")
		public   void setValue(java.sql.Timestamp val) {
			Value = val;
		}
	}
	public LastUpdateTime getLastUpdateTime();
	/*Radix::Acs::AppRole:hasDefinition:hasDefinition-Presentation Property*/


	public class HasDefinition extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public HasDefinition(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::AppRole:hasDefinition:hasDefinition")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::AppRole:hasDefinition:hasDefinition")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public HasDefinition getHasDefinition();
	/*Radix::Acs::AppRole:isReadOnly:isReadOnly-Presentation Property*/


	public class IsReadOnly extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public IsReadOnly(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::AppRole:isReadOnly:isReadOnly")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::AppRole:isReadOnly:isReadOnly")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public IsReadOnly getIsReadOnly();
	/*Radix::Acs::AppRole:changeLogXmlFromUDSDesigner:changeLogXmlFromUDSDesigner-Presentation Property*/


	public class ChangeLogXmlFromUDSDesigner extends org.radixware.kernel.common.client.models.items.properties.PropertyXml{
		public ChangeLogXmlFromUDSDesigner(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Class<org.radixware.schemas.commondef.ChangeLogDocument> getValClass(){
			return org.radixware.schemas.commondef.ChangeLogDocument.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.schemas.commondef.ChangeLogDocument dummy = x == null ? null : (org.radixware.schemas.commondef.ChangeLogDocument)org.radixware.kernel.common.utils.XmlObjectProcessor.castToXmlClass(getClass().getClassLoader(),(org.apache.xmlbeans.XmlObject)x,org.radixware.schemas.commondef.ChangeLogDocument.class);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::AppRole:changeLogXmlFromUDSDesigner:changeLogXmlFromUDSDesigner")
		public  org.radixware.schemas.commondef.ChangeLogDocument getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::AppRole:changeLogXmlFromUDSDesigner:changeLogXmlFromUDSDesigner")
		public   void setValue(org.radixware.schemas.commondef.ChangeLogDocument val) {
			Value = val;
		}
	}
	public ChangeLogXmlFromUDSDesigner getChangeLogXmlFromUDSDesigner();
	/*Radix::Acs::AppRole:needsCompilation:needsCompilation-Presentation Property*/


	public class NeedsCompilation extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public NeedsCompilation(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::AppRole:needsCompilation:needsCompilation")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::AppRole:needsCompilation:needsCompilation")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public NeedsCompilation getNeedsCompilation();
	/*Radix::Acs::AppRole:changeLog:changeLog-Presentation Property*/


	public class ChangeLog extends org.radixware.kernel.common.client.models.items.properties.PropertyObject{
		public ChangeLog(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadParentRefPropertyDef def){
			super(model,def);
		}
		@Override
		public 
		org.radixware.ads.CfgManagement.explorer.ChangeLog.ChangeLog_DefaultModel openEntityModel()  throws org.radixware.kernel.common.exceptions.ServiceClientException, InterruptedException{
		     return (org.radixware.ads.CfgManagement.explorer.ChangeLog.ChangeLog_DefaultModel)super.openEntityModel();
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.client.types.Reference dummy = ((org.radixware.kernel.common.client.types.Reference)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::AppRole:changeLog:changeLog")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::AppRole:changeLog:changeLog")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public ChangeLog getChangeLog();
	public static class Import extends org.radixware.kernel.common.client.models.items.Command{
		protected Import(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public org.radixware.schemas.types.StrDocument send(org.radixware.ads.Acs.common.ImpExpAdsXsd.AppRoleDocument input ) throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,input);
			org.apache.xmlbeans.XmlObject xmlResponse = processResponse(response, null);
			return (org.radixware.schemas.types.StrDocument)org.radixware.kernel.common.utils.XmlObjectProcessor.castToXmlClass(getClass().getClassLoader(),xmlResponse,org.radixware.schemas.types.StrDocument.class);
		}

	}

	public static class Compile extends org.radixware.kernel.common.client.models.items.Command{
		protected Compile(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public void send() throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,null);
			processResponse(response, null);
		}

	}

	public static class Export extends org.radixware.kernel.common.client.models.items.Command{
		protected Export(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}

	}



}

/* Radix::Acs::AppRole - Desktop Meta*/

/*Radix::Acs::AppRole-Entity Class*/

package org.radixware.ads.Acs.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class AppRole_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::Acs::AppRole:Presentations-Entity Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecM7J46MP6F3PBDIJEABQAQH3XQ4"),
			"Radix::Acs::AppRole",
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("imgRWDDDR2YBZED5B3BUYIQRTLAQQ"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("sprOBP7OWB2JNC67FC7U7RDMNFMMA"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBZPQ4TD4V5CNHMIEKJ6JBTJFUU"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRCMVYGTFSRCLFESDGH6DLJ5VKI"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBZPQ4TD4V5CNHMIEKJ6JBTJFUU"),0,

			/*Radix::Acs::AppRole:Properties-Properties*/
			new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

					/*Radix::Acs::AppRole:definition:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGQVSU7TJYRFCZFU7TADGWIRGHY"),
						"definition",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecM7J46MP6F3PBDIJEABQAQH3XQ4"),
						org.radixware.kernel.common.enums.EValType.XML,
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
						null,
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::AppRole:description:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPJZG5RYZF7PBDIJEABQAQH3XQ4"),
						"description",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXV35CME5JVG3DI2U5CYYDMHQEY"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecM7J46MP6F3PBDIJEABQAQH3XQ4"),
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

						/*Radix::Acs::AppRole:description:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,true),
						null,
						null,
						null,
						true,-1,-1,1,
						true,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::AppRole:guid:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col6FMTUQ76F3PBDIJEABQAQH3XQ4"),
						"guid",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4H6AAGJKINAT7PSYSDTOFSXDJI"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecM7J46MP6F3PBDIJEABQAQH3XQ4"),
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
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::Acs::AppRole:guid:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,100,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::AppRole:hasDefinition:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdEHFTROTHHJDPDOCTBXBOJAC3ZI"),
						"hasDefinition",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsF2MX2KYM75GVZIOUMEXHFT53DI"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecM7J46MP6F3PBDIJEABQAQH3XQ4"),
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
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Acs::AppRole:hasDefinition:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecM7J46MP6F3PBDIJEABQAQH3XQ4"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::AppRole:isReadOnly:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdPZ5BW3BSXBE2RBOY2D3QOUWQ7M"),
						"isReadOnly",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEA6G5LJHDFAQJHTMH6M5P46PCQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecM7J46MP6F3PBDIJEABQAQH3XQ4"),
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
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Acs::AppRole:isReadOnly:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecM7J46MP6F3PBDIJEABQAQH3XQ4"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::AppRole:lastUpdateTime:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colWSYOEXVDBRAHRM4KKO5H3QANS4"),
						"lastUpdateTime",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecM7J46MP6F3PBDIJEABQAQH3XQ4"),
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
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Acs::AppRole:lastUpdateTime:PropertyPresentation:Edit Options:-Edit Mask Date Time*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskDateTime(org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,null,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::AppRole:lastUpdateUserName:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDA2H7YX6XRGOFCEEKSO65GQN7A"),
						"lastUpdateUserName",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecM7J46MP6F3PBDIJEABQAQH3XQ4"),
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

						/*Radix::Acs::AppRole:lastUpdateUserName:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,250,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::AppRole:needsCompilation:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdYJ74C6AIXNB27BADDMBJYKLIN4"),
						"needsCompilation",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJMKZBJOQYBDPLIRRQD6GUQUMHY"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecM7J46MP6F3PBDIJEABQAQH3XQ4"),
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
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Acs::AppRole:needsCompilation:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecM7J46MP6F3PBDIJEABQAQH3XQ4"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::AppRole:roleClassGuid:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIXVB57MI2RADLC5IAUFV5QB67Y"),
						"roleClassGuid",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecM7J46MP6F3PBDIJEABQAQH3XQ4"),
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

						/*Radix::Acs::AppRole:roleClassGuid:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,100,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::AppRole:runtime:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPJH2TGKX3JAIDCK2GK4H763RDQ"),
						"runtime",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecM7J46MP6F3PBDIJEABQAQH3XQ4"),
						org.radixware.kernel.common.enums.EValType.BLOB,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						true,
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
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::AppRole:title:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMFRUYXP6F3PBDIJEABQAQH3XQ4"),
						"title",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4QWTMNQ4NJARTMH45D72HK4QYA"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecM7J46MP6F3PBDIJEABQAQH3XQ4"),
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

						/*Radix::Acs::AppRole:title:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,250,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::AppRole:changeLog:PropertyPresentation-Object Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruEYW22P42DVAO5JTUAFUG2EMX5E"),
						"changeLog",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecM7J46MP6F3PBDIJEABQAQH3XQ4"),
						org.radixware.kernel.common.enums.EValType.OBJECT,
						org.radixware.kernel.common.enums.EPropNature.USER,
						5,
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
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecVHY5COSHWNA7PPJ65FW3OGJ4ZQ"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblVHY5COSHWNA7PPJ65FW3OGJ4ZQ"),
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprVRZPY6RJKBHWFLK46ATZLXFW3U")},
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr6HHEKF43V5BUPOUOE7WW7JP2PU")},
						null,
						0L,
						0L,false,false),

					/*Radix::Acs::AppRole:changeLogXmlFromUDSDesigner:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdQTZ7TUYLCVAPREYFWLZIE2OUM4"),
						"changeLogXmlFromUDSDesigner",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecM7J46MP6F3PBDIJEABQAQH3XQ4"),
						org.radixware.kernel.common.enums.EValType.XML,
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
						null,
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false)
			},
			new org.radixware.kernel.common.client.meta.RadCommandDef[]{
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::Acs::AppRole:Export-Object Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdX2MHBEYNHNCAHAH2IVXSJDRUDM"),
						"Export",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecM7J46MP6F3PBDIJEABQAQH3XQ4"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZCSZ3EGV6NAO5MHCLMZYYDXLVU"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("imgVORNT5ZFORBEHLSYMJNUWJK6II"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("afhJ6CZYK3YZBFPXHMXQG55JCWG5Q"),
						org.radixware.kernel.common.enums.ECommandNature.FORM_IN_OUT,
						true,
						false,
						true,
						org.radixware.kernel.common.enums.ECommandScope.OBJECT,
						null,
						org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_FIXED,
						true),
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::Acs::AppRole:Import-Object Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmd5UMGTFSOORA7LBW4ARAK74I7TI"),
						"Import",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecM7J46MP6F3PBDIJEABQAQH3XQ4"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5LK4HXBJFVHDDN3TG3QB32CIZA"),
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
					/*Radix::Acs::AppRole:Compile-Object Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdR6RR7IJLIJAN3FANR5JYHG6QYA"),
						"Compile",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecM7J46MP6F3PBDIJEABQAQH3XQ4"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPK25P4EEM5HM5LEHRSASLAXGCQ"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("imgWZZPS3ZBBNFVNHFNSKTITBMHQE"),
						null,
						org.radixware.kernel.common.enums.ECommandNature.XML_IN_FORM_OUT,
						true,
						true,
						false,
						org.radixware.kernel.common.enums.ECommandScope.OBJECT,
						null,
						org.radixware.kernel.common.enums.ECommandAccessibility.ALWAYS)
			},
			null,
			new org.radixware.kernel.common.client.meta.RadSortingDef[]{

					new org.radixware.kernel.common.client.meta.RadSortingDef(
					/*Radix::Acs::AppRole:Title-Sorting*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("srt4XDUO7EF7JDQHCKNGCN3EV46JQ"),
						"Title",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecM7J46MP6F3PBDIJEABQAQH3XQ4"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOGEVUI6JKNF6JPMVJNB3Q7GJOM"),
						new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem[]{

								new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMFRUYXP6F3PBDIJEABQAQH3XQ4"),
								false)
						})
			},
			null,
			new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprSIJFTYEHKVAQLBWEFIMTRLF5AQ"),org.radixware.kernel.common.types.Id.Factory.loadFrom("sprOBP7OWB2JNC67FC7U7RDMNFMMA")},
			false,true,false);
}

/* Radix::Acs::AppRole - Web Executable*/

/*Radix::Acs::AppRole-Entity Class*/

package org.radixware.ads.Acs.web;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::AppRole")
public interface AppRole {
	/*Group model adapter class. This is the base class for 
	all of selector presentation models of this entity*/
	public static class DefaultGroupModel extends org.radixware.kernel.common.client.models.GroupModel{
		public DefaultGroupModel(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadSelectorPresentationDef def){super(userSession,def);}





		public org.radixware.ads.Acs.web.AppRole.AppRole_DefaultModel getEntity(int i) throws InterruptedException, org.radixware.kernel.common.client.exceptions.BrokenEntityObjectException, org.radixware.kernel.common.exceptions.ServiceClientException { return (org.radixware.ads.Acs.web.AppRole.AppRole_DefaultModel )  super.getEntity(i);}
	}


































































































	/*Radix::Acs::AppRole:guid:guid-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::AppRole:guid:guid")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::AppRole:guid:guid")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Guid getGuid();
	/*Radix::Acs::AppRole:lastUpdateUserName:lastUpdateUserName-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::AppRole:lastUpdateUserName:lastUpdateUserName")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::AppRole:lastUpdateUserName:lastUpdateUserName")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public LastUpdateUserName getLastUpdateUserName();
	/*Radix::Acs::AppRole:definition:definition-Presentation Property*/


	public class Definition extends org.radixware.kernel.common.client.models.items.properties.PropertyXml{
		public Definition(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Class<org.radixware.schemas.adsdef.AdsDefinitionDocument> getValClass(){
			return org.radixware.schemas.adsdef.AdsDefinitionDocument.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.schemas.adsdef.AdsDefinitionDocument dummy = x == null ? null : (org.radixware.schemas.adsdef.AdsDefinitionDocument)org.radixware.kernel.common.utils.XmlObjectProcessor.castToXmlClass(getClass().getClassLoader(),(org.apache.xmlbeans.XmlObject)x,org.radixware.schemas.adsdef.AdsDefinitionDocument.class);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::AppRole:definition:definition")
		public  org.radixware.schemas.adsdef.AdsDefinitionDocument getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::AppRole:definition:definition")
		public   void setValue(org.radixware.schemas.adsdef.AdsDefinitionDocument val) {
			Value = val;
		}
	}
	public Definition getDefinition();
	/*Radix::Acs::AppRole:roleClassGuid:roleClassGuid-Presentation Property*/


	public class RoleClassGuid extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public RoleClassGuid(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::AppRole:roleClassGuid:roleClassGuid")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::AppRole:roleClassGuid:roleClassGuid")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public RoleClassGuid getRoleClassGuid();
	/*Radix::Acs::AppRole:title:title-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::AppRole:title:title")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::AppRole:title:title")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Title getTitle();
	/*Radix::Acs::AppRole:runtime:runtime-Presentation Property*/


	public class Runtime extends org.radixware.kernel.common.client.models.items.properties.PropertyBlob{
		public Runtime(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.types.Bin dummy = ((org.radixware.kernel.common.types.Bin)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::AppRole:runtime:runtime")
		public  org.radixware.kernel.common.types.Bin getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::AppRole:runtime:runtime")
		public   void setValue(org.radixware.kernel.common.types.Bin val) {
			Value = val;
		}
	}
	public Runtime getRuntime();
	/*Radix::Acs::AppRole:description:description-Presentation Property*/


	public class Description extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public Description(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::AppRole:description:description")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::AppRole:description:description")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Description getDescription();
	/*Radix::Acs::AppRole:lastUpdateTime:lastUpdateTime-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::AppRole:lastUpdateTime:lastUpdateTime")
		public  java.sql.Timestamp getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::AppRole:lastUpdateTime:lastUpdateTime")
		public   void setValue(java.sql.Timestamp val) {
			Value = val;
		}
	}
	public LastUpdateTime getLastUpdateTime();
	/*Radix::Acs::AppRole:hasDefinition:hasDefinition-Presentation Property*/


	public class HasDefinition extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public HasDefinition(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::AppRole:hasDefinition:hasDefinition")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::AppRole:hasDefinition:hasDefinition")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public HasDefinition getHasDefinition();
	/*Radix::Acs::AppRole:isReadOnly:isReadOnly-Presentation Property*/


	public class IsReadOnly extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public IsReadOnly(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::AppRole:isReadOnly:isReadOnly")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::AppRole:isReadOnly:isReadOnly")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public IsReadOnly getIsReadOnly();
	/*Radix::Acs::AppRole:changeLogXmlFromUDSDesigner:changeLogXmlFromUDSDesigner-Presentation Property*/


	public class ChangeLogXmlFromUDSDesigner extends org.radixware.kernel.common.client.models.items.properties.PropertyXml{
		public ChangeLogXmlFromUDSDesigner(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Class<org.radixware.schemas.commondef.ChangeLogDocument> getValClass(){
			return org.radixware.schemas.commondef.ChangeLogDocument.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.schemas.commondef.ChangeLogDocument dummy = x == null ? null : (org.radixware.schemas.commondef.ChangeLogDocument)org.radixware.kernel.common.utils.XmlObjectProcessor.castToXmlClass(getClass().getClassLoader(),(org.apache.xmlbeans.XmlObject)x,org.radixware.schemas.commondef.ChangeLogDocument.class);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::AppRole:changeLogXmlFromUDSDesigner:changeLogXmlFromUDSDesigner")
		public  org.radixware.schemas.commondef.ChangeLogDocument getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::AppRole:changeLogXmlFromUDSDesigner:changeLogXmlFromUDSDesigner")
		public   void setValue(org.radixware.schemas.commondef.ChangeLogDocument val) {
			Value = val;
		}
	}
	public ChangeLogXmlFromUDSDesigner getChangeLogXmlFromUDSDesigner();
	/*Radix::Acs::AppRole:needsCompilation:needsCompilation-Presentation Property*/


	public class NeedsCompilation extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public NeedsCompilation(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::AppRole:needsCompilation:needsCompilation")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::AppRole:needsCompilation:needsCompilation")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public NeedsCompilation getNeedsCompilation();
	/*Radix::Acs::AppRole:changeLog:changeLog-Presentation Property*/


	public class ChangeLog extends org.radixware.kernel.common.client.models.items.properties.PropertyObject{
		public ChangeLog(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadParentRefPropertyDef def){
			super(model,def);
		}
		@Override
		public 
		org.radixware.ads.CfgManagement.web.ChangeLog.ChangeLog_DefaultModel openEntityModel()  throws org.radixware.kernel.common.exceptions.ServiceClientException, InterruptedException{
		     return (org.radixware.ads.CfgManagement.web.ChangeLog.ChangeLog_DefaultModel)super.openEntityModel();
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.client.types.Reference dummy = ((org.radixware.kernel.common.client.types.Reference)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::AppRole:changeLog:changeLog")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::AppRole:changeLog:changeLog")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public ChangeLog getChangeLog();
	public static class Import extends org.radixware.kernel.common.client.models.items.Command{
		protected Import(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public org.radixware.schemas.types.StrDocument send(org.radixware.ads.Acs.common.ImpExpAdsXsd.AppRoleDocument input ) throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,input);
			org.apache.xmlbeans.XmlObject xmlResponse = processResponse(response, null);
			return (org.radixware.schemas.types.StrDocument)org.radixware.kernel.common.utils.XmlObjectProcessor.castToXmlClass(getClass().getClassLoader(),xmlResponse,org.radixware.schemas.types.StrDocument.class);
		}

	}

	public static class Compile extends org.radixware.kernel.common.client.models.items.Command{
		protected Compile(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public void send() throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,null);
			processResponse(response, null);
		}

	}

	public static class Export extends org.radixware.kernel.common.client.models.items.Command{
		protected Export(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}

	}



}

/* Radix::Acs::AppRole - Web Meta*/

/*Radix::Acs::AppRole-Entity Class*/

package org.radixware.ads.Acs.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class AppRole_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::Acs::AppRole:Presentations-Entity Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecM7J46MP6F3PBDIJEABQAQH3XQ4"),
			"Radix::Acs::AppRole",
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("imgRWDDDR2YBZED5B3BUYIQRTLAQQ"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("sprOBP7OWB2JNC67FC7U7RDMNFMMA"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBZPQ4TD4V5CNHMIEKJ6JBTJFUU"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRCMVYGTFSRCLFESDGH6DLJ5VKI"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBZPQ4TD4V5CNHMIEKJ6JBTJFUU"),0,

			/*Radix::Acs::AppRole:Properties-Properties*/
			new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

					/*Radix::Acs::AppRole:definition:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGQVSU7TJYRFCZFU7TADGWIRGHY"),
						"definition",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecM7J46MP6F3PBDIJEABQAQH3XQ4"),
						org.radixware.kernel.common.enums.EValType.XML,
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
						null,
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::AppRole:description:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPJZG5RYZF7PBDIJEABQAQH3XQ4"),
						"description",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXV35CME5JVG3DI2U5CYYDMHQEY"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecM7J46MP6F3PBDIJEABQAQH3XQ4"),
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

						/*Radix::Acs::AppRole:description:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,true),
						null,
						null,
						null,
						true,-1,-1,1,
						true,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::AppRole:guid:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col6FMTUQ76F3PBDIJEABQAQH3XQ4"),
						"guid",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4H6AAGJKINAT7PSYSDTOFSXDJI"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecM7J46MP6F3PBDIJEABQAQH3XQ4"),
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
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::Acs::AppRole:guid:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,100,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::AppRole:hasDefinition:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdEHFTROTHHJDPDOCTBXBOJAC3ZI"),
						"hasDefinition",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsF2MX2KYM75GVZIOUMEXHFT53DI"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecM7J46MP6F3PBDIJEABQAQH3XQ4"),
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
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Acs::AppRole:hasDefinition:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecM7J46MP6F3PBDIJEABQAQH3XQ4"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::AppRole:isReadOnly:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdPZ5BW3BSXBE2RBOY2D3QOUWQ7M"),
						"isReadOnly",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEA6G5LJHDFAQJHTMH6M5P46PCQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecM7J46MP6F3PBDIJEABQAQH3XQ4"),
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
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Acs::AppRole:isReadOnly:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecM7J46MP6F3PBDIJEABQAQH3XQ4"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::AppRole:lastUpdateTime:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colWSYOEXVDBRAHRM4KKO5H3QANS4"),
						"lastUpdateTime",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecM7J46MP6F3PBDIJEABQAQH3XQ4"),
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
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Acs::AppRole:lastUpdateTime:PropertyPresentation:Edit Options:-Edit Mask Date Time*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskDateTime(org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,null,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::AppRole:lastUpdateUserName:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDA2H7YX6XRGOFCEEKSO65GQN7A"),
						"lastUpdateUserName",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecM7J46MP6F3PBDIJEABQAQH3XQ4"),
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

						/*Radix::Acs::AppRole:lastUpdateUserName:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,250,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::AppRole:needsCompilation:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdYJ74C6AIXNB27BADDMBJYKLIN4"),
						"needsCompilation",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJMKZBJOQYBDPLIRRQD6GUQUMHY"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecM7J46MP6F3PBDIJEABQAQH3XQ4"),
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
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Acs::AppRole:needsCompilation:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecM7J46MP6F3PBDIJEABQAQH3XQ4"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::AppRole:roleClassGuid:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIXVB57MI2RADLC5IAUFV5QB67Y"),
						"roleClassGuid",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecM7J46MP6F3PBDIJEABQAQH3XQ4"),
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

						/*Radix::Acs::AppRole:roleClassGuid:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,100,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::AppRole:runtime:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPJH2TGKX3JAIDCK2GK4H763RDQ"),
						"runtime",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecM7J46MP6F3PBDIJEABQAQH3XQ4"),
						org.radixware.kernel.common.enums.EValType.BLOB,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						true,
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
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::AppRole:title:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMFRUYXP6F3PBDIJEABQAQH3XQ4"),
						"title",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4QWTMNQ4NJARTMH45D72HK4QYA"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecM7J46MP6F3PBDIJEABQAQH3XQ4"),
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

						/*Radix::Acs::AppRole:title:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,250,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::AppRole:changeLog:PropertyPresentation-Object Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruEYW22P42DVAO5JTUAFUG2EMX5E"),
						"changeLog",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecM7J46MP6F3PBDIJEABQAQH3XQ4"),
						org.radixware.kernel.common.enums.EValType.OBJECT,
						org.radixware.kernel.common.enums.EPropNature.USER,
						5,
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
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecVHY5COSHWNA7PPJ65FW3OGJ4ZQ"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblVHY5COSHWNA7PPJ65FW3OGJ4ZQ"),
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprVRZPY6RJKBHWFLK46ATZLXFW3U")},
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr6HHEKF43V5BUPOUOE7WW7JP2PU")},
						null,
						0L,
						0L,false,false),

					/*Radix::Acs::AppRole:changeLogXmlFromUDSDesigner:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdQTZ7TUYLCVAPREYFWLZIE2OUM4"),
						"changeLogXmlFromUDSDesigner",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecM7J46MP6F3PBDIJEABQAQH3XQ4"),
						org.radixware.kernel.common.enums.EValType.XML,
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
						null,
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false)
			},
			new org.radixware.kernel.common.client.meta.RadCommandDef[]{
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::Acs::AppRole:Compile-Object Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdR6RR7IJLIJAN3FANR5JYHG6QYA"),
						"Compile",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecM7J46MP6F3PBDIJEABQAQH3XQ4"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPK25P4EEM5HM5LEHRSASLAXGCQ"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("imgWZZPS3ZBBNFVNHFNSKTITBMHQE"),
						null,
						org.radixware.kernel.common.enums.ECommandNature.XML_IN_FORM_OUT,
						true,
						true,
						false,
						org.radixware.kernel.common.enums.ECommandScope.OBJECT,
						null,
						org.radixware.kernel.common.enums.ECommandAccessibility.ALWAYS)
			},
			null,
			new org.radixware.kernel.common.client.meta.RadSortingDef[]{

					new org.radixware.kernel.common.client.meta.RadSortingDef(
					/*Radix::Acs::AppRole:Title-Sorting*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("srt4XDUO7EF7JDQHCKNGCN3EV46JQ"),
						"Title",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecM7J46MP6F3PBDIJEABQAQH3XQ4"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOGEVUI6JKNF6JPMVJNB3Q7GJOM"),
						new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem[]{

								new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMFRUYXP6F3PBDIJEABQAQH3XQ4"),
								false)
						})
			},
			null,
			new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprSIJFTYEHKVAQLBWEFIMTRLF5AQ"),org.radixware.kernel.common.types.Id.Factory.loadFrom("sprOBP7OWB2JNC67FC7U7RDMNFMMA")},
			false,true,false);
}

/* Radix::Acs::AppRole:General - Desktop Meta*/

/*Radix::Acs::AppRole:General-Editor Presentation*/

package org.radixware.ads.Acs.explorer;
public final class General_mi{
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new org.radixware.kernel.common.client.meta.RadEditorPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprSIJFTYEHKVAQLBWEFIMTRLF5AQ"),
	"General",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
	null,
	org.radixware.kernel.common.types.Id.Factory.loadFrom("aecM7J46MP6F3PBDIJEABQAQH3XQ4"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("tblM7J46MP6F3PBDIJEABQAQH3XQ4"),
	null,
	null,

	/*Radix::Acs::AppRole:General:Editor Pages-Editor Presentation Pages*/
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

			/*Radix::Acs::AppRole:General:Main-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgE4N64Z2UC5DXPONJ6EFC7I2E5A"),"Main",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecM7J46MP6F3PBDIJEABQAQH3XQ4"),null,null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMFRUYXP6F3PBDIJEABQAQH3XQ4"),0,0,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPJZG5RYZF7PBDIJEABQAQH3XQ4"),0,1,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdEHFTROTHHJDPDOCTBXBOJAC3ZI"),0,3,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruEYW22P42DVAO5JTUAFUG2EMX5E"),0,2,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col6FMTUQ76F3PBDIJEABQAQH3XQ4"),0,5,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdYJ74C6AIXNB27BADDMBJYKLIN4"),0,4,1,false,false)
			},null)
	},
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgE4N64Z2UC5DXPONJ6EFC7I2E5A"))}
	,

	/*Radix::Acs::AppRole:General:Children-Explorer Items*/
		null
	,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
	null,
	0,
	null,
	(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
	2192,0,0,null);
}
/* Radix::Acs::AppRole:General - Web Meta*/

/*Radix::Acs::AppRole:General-Editor Presentation*/

package org.radixware.ads.Acs.web;
public final class General_mi{
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new org.radixware.kernel.common.client.meta.RadEditorPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprSIJFTYEHKVAQLBWEFIMTRLF5AQ"),
	"General",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
	null,
	org.radixware.kernel.common.types.Id.Factory.loadFrom("aecM7J46MP6F3PBDIJEABQAQH3XQ4"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("tblM7J46MP6F3PBDIJEABQAQH3XQ4"),
	null,
	null,

	/*Radix::Acs::AppRole:General:Editor Pages-Editor Presentation Pages*/
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

			/*Radix::Acs::AppRole:General:Main-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgE4N64Z2UC5DXPONJ6EFC7I2E5A"),"Main",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecM7J46MP6F3PBDIJEABQAQH3XQ4"),null,null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMFRUYXP6F3PBDIJEABQAQH3XQ4"),0,0,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPJZG5RYZF7PBDIJEABQAQH3XQ4"),0,1,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdEHFTROTHHJDPDOCTBXBOJAC3ZI"),0,3,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruEYW22P42DVAO5JTUAFUG2EMX5E"),0,2,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col6FMTUQ76F3PBDIJEABQAQH3XQ4"),0,5,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdYJ74C6AIXNB27BADDMBJYKLIN4"),0,4,1,false,false)
			},null)
	},
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgE4N64Z2UC5DXPONJ6EFC7I2E5A"))}
	,

	/*Radix::Acs::AppRole:General:Children-Explorer Items*/
		null
	,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
	null,
	0,
	null,
	(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
	2192,0,0,null);
}
/* Radix::Acs::AppRole:General:Model - Desktop Executable*/

/*Radix::Acs::AppRole:General:Model-Entity Model Class*/

package org.radixware.ads.Acs.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::AppRole:General:Model")
public class General:Model  extends org.radixware.ads.Acs.explorer.AppRole.AppRole_DefaultModel {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return General:Model_mi.rdxMeta; }



	public General:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadEditorPresentationDef def){super(userSession,def);}

	/*Radix::Acs::AppRole:General:Model:Nested classes-Nested Classes*/

	/*Radix::Acs::AppRole:General:Model:Properties-Properties*/

	/*Radix::Acs::AppRole:General:Model:guid-Presentation Property*/




	public class Guid extends org.radixware.ads.Acs.explorer.AppRole.col6FMTUQ76F3PBDIJEABQAQH3XQ4{
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

		/*Radix::Acs::AppRole:General:Model:guid:EmbeddedClass-Property Presentation Embedded Class*/




		/*Radix::Acs::AppRole:General:Model:guid:Nested classes-Nested Classes*/

		/*Radix::Acs::AppRole:General:Model:guid:Properties-Properties*/

		/*Radix::Acs::AppRole:General:Model:guid:Methods-Methods*/

		/*Radix::Acs::AppRole:General:Model:guid:getTextOptions-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::AppRole:General:Model:guid:getTextOptions")
		protected published  org.radixware.kernel.common.client.text.ITextOptions getTextOptions (java.util.EnumSet<org.radixware.kernel.common.client.enums.ETextOptionsMarker> markers, org.radixware.kernel.common.client.text.ITextOptions options) {
			final Client.Text::ITextOptions textOptions = super.getTextOptions(markers, options);
			if (markers.contains(Client.Text::TextOptionsMarker.SELECTOR_ROW)) {
			    return textOptions;
			} else {
			    return textOptions.decreaseFontSize(4);
			}
		}













		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::AppRole:General:Model:guid")
		public published  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::AppRole:General:Model:guid")
		public published   void setValue(Str val) {
			Value = val;
		}
	}
	public Guid getGuid(){return (Guid)getProperty(col6FMTUQ76F3PBDIJEABQAQH3XQ4);}








	/*Radix::Acs::AppRole:General:Model:Methods-Methods*/

	/*Radix::Acs::AppRole:General:Model:onCommand_Import-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::AppRole:General:Model:onCommand_Import")
	protected  void onCommand_Import (org.radixware.ads.Acs.explorer.AppRole.Import command) {
		java.io.File file = CfgManagement::DesktopUtils.openFileForImport(findNearestView(), command.getTitle());
		if (file == null)
		    return;

		try {
		    ImpExpAdsXsd:AppRoleDocument input = ImpExpAdsXsd:AppRoleDocument.Factory.parse(file);
		    Common.Dlg::ClientUtils.viewImportResult(command.send(input));
		    if(getView() != null){
		        getView().reread();
		    }
		} catch (Exceptions::XmlException | Exceptions::ServiceClientException | Exceptions::IOException e) {
		    showException(e);
		} catch (Exceptions::InterruptedException e) {
		}
	}

	/*Radix::Acs::AppRole:General:Model:onCommand_Compile-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::AppRole:General:Model:onCommand_Compile")
	public  void onCommand_Compile (org.radixware.ads.Acs.explorer.AppRole.Compile command) {
		try{
		    command.send();
		}catch(Exceptions::InterruptedException  e){
		        showException(e);
		}catch(Exceptions::ServiceClientException  e){
		        showException(e);
		}
	}

	/*Radix::Acs::AppRole:General:Model:isCommandEnabled-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::AppRole:General:Model:isCommandEnabled")
	public published  boolean isCommandEnabled (org.radixware.kernel.common.client.meta.RadCommandDef command) {
		if(command.getId() == idof[AppRole:Compile]){
		    return hasDefinition.Value == Boolean.TRUE && super.isCommandEnabled(command);
		}
		return super.isCommandEnabled(command);
	}
	public final class Import extends org.radixware.ads.Acs.explorer.AppRole.Import{
		protected Import(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_Import( this );
		}

	}

	public final class Compile extends org.radixware.ads.Acs.explorer.AppRole.Compile{
		protected Compile(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_Compile( this );
		}

	}















}

/* Radix::Acs::AppRole:General:Model - Desktop Meta*/

/*Radix::Acs::AppRole:General:Model-Entity Model Class*/

package org.radixware.ads.Acs.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class General:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aemSIJFTYEHKVAQLBWEFIMTRLF5AQ"),
						"General:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::Acs::AppRole:General:Model:Properties-Properties*/
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

/* Radix::Acs::AppRole:General:Model - Web Executable*/

/*Radix::Acs::AppRole:General:Model-Entity Model Class*/

package org.radixware.ads.Acs.web;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::AppRole:General:Model")
public class General:Model  extends org.radixware.ads.Acs.web.AppRole.AppRole_DefaultModel {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return General:Model_mi.rdxMeta; }



	public General:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadEditorPresentationDef def){super(userSession,def);}

	/*Radix::Acs::AppRole:General:Model:Nested classes-Nested Classes*/

	/*Radix::Acs::AppRole:General:Model:Properties-Properties*/

	/*Radix::Acs::AppRole:General:Model:guid-Presentation Property*/




	public class Guid extends org.radixware.ads.Acs.web.AppRole.col6FMTUQ76F3PBDIJEABQAQH3XQ4{
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

		/*Radix::Acs::AppRole:General:Model:guid:EmbeddedClass-Property Presentation Embedded Class*/




		/*Radix::Acs::AppRole:General:Model:guid:Nested classes-Nested Classes*/

		/*Radix::Acs::AppRole:General:Model:guid:Properties-Properties*/

		/*Radix::Acs::AppRole:General:Model:guid:Methods-Methods*/

		/*Radix::Acs::AppRole:General:Model:guid:getTextOptions-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::AppRole:General:Model:guid:getTextOptions")
		protected published  org.radixware.kernel.common.client.text.ITextOptions getTextOptions (java.util.EnumSet<org.radixware.kernel.common.client.enums.ETextOptionsMarker> markers, org.radixware.kernel.common.client.text.ITextOptions options) {
			final Client.Text::ITextOptions textOptions = super.getTextOptions(markers, options);
			if (markers.contains(Client.Text::TextOptionsMarker.SELECTOR_ROW)) {
			    return textOptions;
			} else {
			    return textOptions.decreaseFontSize(4);
			}
		}













		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::AppRole:General:Model:guid")
		public published  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::AppRole:General:Model:guid")
		public published   void setValue(Str val) {
			Value = val;
		}
	}
	public Guid getGuid(){return (Guid)getProperty(col6FMTUQ76F3PBDIJEABQAQH3XQ4);}








	/*Radix::Acs::AppRole:General:Model:Methods-Methods*/

	/*Radix::Acs::AppRole:General:Model:onCommand_Compile-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::AppRole:General:Model:onCommand_Compile")
	public  void onCommand_Compile (org.radixware.ads.Acs.web.AppRole.Compile command) {
		try{
		    command.send();
		}catch(Exceptions::InterruptedException  e){
		        showException(e);
		}catch(Exceptions::ServiceClientException  e){
		        showException(e);
		}
	}

	/*Radix::Acs::AppRole:General:Model:isCommandEnabled-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::AppRole:General:Model:isCommandEnabled")
	public published  boolean isCommandEnabled (org.radixware.kernel.common.client.meta.RadCommandDef command) {
		if(command.getId() == idof[AppRole:Compile]){
		    return hasDefinition.Value == Boolean.TRUE && super.isCommandEnabled(command);
		}
		return super.isCommandEnabled(command);
	}
	public final class Compile extends org.radixware.ads.Acs.web.AppRole.Compile{
		protected Compile(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_Compile( this );
		}

	}













}

/* Radix::Acs::AppRole:General:Model - Web Meta*/

/*Radix::Acs::AppRole:General:Model-Entity Model Class*/

package org.radixware.ads.Acs.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class General:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aemSIJFTYEHKVAQLBWEFIMTRLF5AQ"),
						"General:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::Acs::AppRole:General:Model:Properties-Properties*/
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

/* Radix::Acs::AppRole:General - Desktop Meta*/

/*Radix::Acs::AppRole:General-Selector Presentation*/

package org.radixware.ads.Acs.explorer;
public final class General_mi extends org.radixware.kernel.common.client.meta.RadSelectorPresentationDef{
	public static final org.radixware.kernel.common.client.meta.RadSelectorPresentationDef rdxMeta = new General_mi();
	private General_mi(){
		super(org.radixware.kernel.common.types.Id.Factory.loadFrom("sprOBP7OWB2JNC67FC7U7RDMNFMMA"),
		"General",
		org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
		null,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("aecM7J46MP6F3PBDIJEABQAQH3XQ4"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("tblM7J46MP6F3PBDIJEABQAQH3XQ4"),
		null,
		null,
		null,
		null,
		false,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("srt4XDUO7EF7JDQHCKNGCN3EV46JQ"),
		null,
		false,
		false,
		null,
		32,
		null,
		144,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprSIJFTYEHKVAQLBWEFIMTRLF5AQ")},
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprSIJFTYEHKVAQLBWEFIMTRLF5AQ")},
		false,true,false,0,0);
		columns = new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn[]{

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMFRUYXP6F3PBDIJEABQAQH3XQ4"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPJZG5RYZF7PBDIJEABQAQH3XQ4"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col6FMTUQ76F3PBDIJEABQAQH3XQ4"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null)
		};
	}
}
/* Radix::Acs::AppRole:General - Web Meta*/

/*Radix::Acs::AppRole:General-Selector Presentation*/

package org.radixware.ads.Acs.web;
public final class General_mi extends org.radixware.kernel.common.client.meta.RadSelectorPresentationDef{
	public static final org.radixware.kernel.common.client.meta.RadSelectorPresentationDef rdxMeta = new General_mi();
	private General_mi(){
		super(org.radixware.kernel.common.types.Id.Factory.loadFrom("sprOBP7OWB2JNC67FC7U7RDMNFMMA"),
		"General",
		org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
		null,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("aecM7J46MP6F3PBDIJEABQAQH3XQ4"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("tblM7J46MP6F3PBDIJEABQAQH3XQ4"),
		null,
		null,
		null,
		null,
		false,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("srt4XDUO7EF7JDQHCKNGCN3EV46JQ"),
		null,
		false,
		false,
		null,
		32,
		null,
		144,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprSIJFTYEHKVAQLBWEFIMTRLF5AQ")},
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprSIJFTYEHKVAQLBWEFIMTRLF5AQ")},
		false,true,false,0,0);
		columns = new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn[]{

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMFRUYXP6F3PBDIJEABQAQH3XQ4"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPJZG5RYZF7PBDIJEABQAQH3XQ4"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col6FMTUQ76F3PBDIJEABQAQH3XQ4"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null)
		};
	}
}
/* Radix::Acs::AppRole:General:Model - Desktop Executable*/

/*Radix::Acs::AppRole:General:Model-Group Model Class*/

package org.radixware.ads.Acs.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::AppRole:General:Model")
public class General:Model  extends org.radixware.ads.Acs.explorer.AppRole.DefaultGroupModel {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return General:Model_mi.rdxMeta; }



	public General:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadSelectorPresentationDef def){super(userSession,def);}

	/*Radix::Acs::AppRole:General:Model:Nested classes-Nested Classes*/

	/*Radix::Acs::AppRole:General:Model:Properties-Properties*/

	/*Radix::Acs::AppRole:General:Model:Methods-Methods*/

	/*Radix::Acs::AppRole:General:Model:onCommand_LaunchEditor-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::AppRole:General:Model:onCommand_LaunchEditor")
	public  void onCommand_LaunchEditor (org.radixware.ads.Acs.explorer.AppRoleGroup.LaunchEditor command) {
		Explorer.Utils::UserDesignerLauncher. start(getEnvironment(),Explorer.Utils::UserDesignerMode:RoleEditor);
	}

	/*Radix::Acs::AppRole:General:Model:onCommand_Import-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::AppRole:General:Model:onCommand_Import")
	protected  void onCommand_Import (org.radixware.ads.Acs.explorer.AppRoleGroup.Import command) {
		java.io.File file = CfgManagement::DesktopUtils.openFileForImport(findNearestView(), command.getTitle());
		if (file == null)
		    return;

		try {
		    ImpExpAdsXsd:AppRolesDocument xDoc;
		    try {
		        xDoc = ImpExpAdsXsd:AppRolesDocument.Factory.parse(file);
		    } catch (Exceptions::XmlException ex) {
		         ImpExpAdsXsd:AppRoleDocument x = ImpExpAdsXsd:AppRoleDocument.Factory.parse(file);
		        xDoc = ImpExpAdsXsd:AppRolesDocument.Factory.newInstance();
		        xDoc.addNewAppRoles().ItemList.add(x.AppRole);
		    }
		    Common.Dlg::ClientUtils.viewImportResult(command.send(xDoc));
		    reread();
		} catch (Exceptions::Exception e) {
		    showException(e);
		}


	}

	/*Radix::Acs::AppRole:General:Model:isCommandAccessible-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::AppRole:General:Model:isCommandAccessible")
	protected published  boolean isCommandAccessible (org.radixware.kernel.common.client.meta.RadCommandDef commandDef) {
		if (super.isCommandAccessible(commandDef)) {
		    if (commandDef.Id == idof[AppRoleGroup:LaunchEditor]) {
		        if (!org.radixware.kernel.common.client.RunParams.isUDSAccessible()) {
		            return false;
		        }
		    }
		    return true;
		} else
		    return false;
	}
	public final class Import extends org.radixware.ads.Acs.explorer.AppRoleGroup.Import{
		protected Import(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_Import( this );
		}

	}

	public final class LaunchEditor extends org.radixware.ads.Acs.explorer.AppRoleGroup.LaunchEditor{
		protected LaunchEditor(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_LaunchEditor( this );
		}

	}















}

/* Radix::Acs::AppRole:General:Model - Desktop Meta*/

/*Radix::Acs::AppRole:General:Model-Group Model Class*/

package org.radixware.ads.Acs.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class General:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("agmOBP7OWB2JNC67FC7U7RDMNFMMA"),
						"General:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::Acs::AppRole:General:Model:Properties-Properties*/
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

/* Radix::Acs::AppRole:General:Model - Web Executable*/

/*Radix::Acs::AppRole:General:Model-Group Model Class*/

package org.radixware.ads.Acs.web;


@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::AppRole:General:Model")
public class General:Model  extends org.radixware.ads.Acs.web.AppRole.DefaultGroupModel {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return General:Model_mi.rdxMeta; }


	public General:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadSelectorPresentationDef def){super(userSession,def);}

	/*Radix::Acs::AppRole:General:Model:Nested classes-Nested Classes*/

	/*Radix::Acs::AppRole:General:Model:Properties-Properties*/

	/*Radix::Acs::AppRole:General:Model:Methods-Methods*/

	/*Radix::Acs::AppRole:General:Model:isCommandAccessible-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::AppRole:General:Model:isCommandAccessible")
	protected published  boolean isCommandAccessible (org.radixware.kernel.common.client.meta.RadCommandDef commandDef) {
		if (super.isCommandAccessible(commandDef)) {
		    if (commandDef.Id == idof[AppRoleGroup:LaunchEditor]) {
		        if (!org.radixware.kernel.common.client.RunParams.isUDSAccessible()) {
		            return false;
		        }
		    }
		    return true;
		} else
		    return false;
	}


}

/* Radix::Acs::AppRole:General:Model - Web Meta*/

/*Radix::Acs::AppRole:General:Model-Group Model Class*/

package org.radixware.ads.Acs.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class General:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("agmOBP7OWB2JNC67FC7U7RDMNFMMA"),
						"General:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::Acs::AppRole:General:Model:Properties-Properties*/
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

/* Radix::Acs::AppRole - Localizing Bundle */
package org.radixware.ads.Acs.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class AppRole - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Describes the user-defined role. Roles of this type can be assigned to a user along with system roles. User-defined roles are managed by the RadixWare User Definitions Designer application.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Класс описывает роль, определяемую пользователем. Роли такого вида могут назначаться пользователю наряду с системными ролями. Управление пользовательскими ролями осуществляется с помощью приложения RadixWare User Definitions Designer.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2EWG4GXWEFFP3HB2QGWRMGFQWI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Role GUID");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"GUID");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4H6AAGJKINAT7PSYSDTOFSXDJI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Name");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Название");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4QWTMNQ4NJARTMH45D72HK4QYA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Import");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Импортировать");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5LK4HXBJFVHDDN3TG3QB32CIZA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Compilation error");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ошибка компиляции");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5QGXPTQHS5FDPL3EP6UDA2XZL4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Compilation completed successfully");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Компиляция успешно завершена");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6PYQ5POM2VCBHL3JBMG2FQHCSA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Application Role");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Прикладная роль");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBZPQ4TD4V5CNHMIEKJ6JBTJFUU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Read-only");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Только чтение");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEA6G5LJHDFAQJHTMH6M5P46PCQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Definitions specified");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Имеет определение");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsF2MX2KYM75GVZIOUMEXHFT53DI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Compilation required");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Требует компиляции");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJMKZBJOQYBDPLIRRQD6GUQUMHY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"By name");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"По названию");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOGEVUI6JKNF6JPMVJNB3Q7GJOM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Application Role");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Прикладная роль");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOULRB2HHOJFIFHTA6EFXIB2WP4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Compile");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Компилировать");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPK25P4EEM5HM5LEHRSASLAXGCQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Application Roles");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Прикладные роли");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQR673HL5MZAPBPBNUP3T7H4PW4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Application Roles");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Прикладные роли");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRCMVYGTFSRCLFESDGH6DLJ5VKI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Description");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Описание");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXV35CME5JVG3DI2U5CYYDMHQEY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Compilation failed. For details, see the event log.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Компиляция завершилась с ошибками. Подробности в журнале событий");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZB6RO6Z7IRHVHDHYBOBI5K2YRY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Export");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Экспортировать");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZCSZ3EGV6NAO5MHCLMZYYDXLVU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(AppRole - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbaecM7J46MP6F3PBDIJEABQAQH3XQ4"),"AppRole - Localizing Bundle",$$$items$$$);
}
