
/* Radix::SelectorAddons::EasSelectorAddons.Filters - Server Executable*/

/*Radix::SelectorAddons::EasSelectorAddons.Filters-Application Class*/

package org.radixware.ads.SelectorAddons.Dlg.server;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons.Filters")
public published class EasSelectorAddons.Filters  extends org.radixware.ads.SelectorAddons.Dlg.server.EasSelectorAddons  implements org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {


	/**Metainformation accessor method*/
	@SuppressWarnings("unused")
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return EasSelectorAddons.Filters_mi.rdxMeta;}

	/*Radix::SelectorAddons::EasSelectorAddons.Filters:Nested classes-Nested Classes*/

	/*Radix::SelectorAddons::EasSelectorAddons.Filters:Properties-Properties*/

	/*Radix::SelectorAddons::EasSelectorAddons.Filters:baseFilterGuid-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons.Filters:baseFilterGuid",line=41)
	public  Str getBaseFilterGuid() {
		return baseFilterGuid;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons.Filters:baseFilterGuid",line=47)
	public   void setBaseFilterGuid(Str val) {
		baseFilterGuid = val;
	}

	/*Radix::SelectorAddons::EasSelectorAddons.Filters:condition-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons.Filters:condition",line=70)
	public  java.sql.Clob getCondition() {
		return condition;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons.Filters:condition",line=76)
	public   void setCondition(java.sql.Clob val) {
		condition = val;
	}

	/*Radix::SelectorAddons::EasSelectorAddons.Filters:parameters-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons.Filters:parameters",line=99)
	public  java.sql.Clob getParameters() {
		return parameters;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons.Filters:parameters",line=105)
	public   void setParameters(java.sql.Clob val) {
		parameters = val;
	}

	/*Radix::SelectorAddons::EasSelectorAddons.Filters:filtersList-Dynamic Property*/



	protected org.radixware.ads.SelectorAddons.Dlg.common.SelectorAddonsInfoXsd.FiltersListDocument filtersList=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons.Filters:filtersList",line=127)
	public  org.radixware.ads.SelectorAddons.Dlg.common.SelectorAddonsInfoXsd.FiltersListDocument getFiltersList() {

		if (internal[filtersList]==null && tableGuid!=null && isTableExists()){
		    internal[filtersList] = getFiltersInfoByTable(tableGuid);
		}
		return internal[filtersList];
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons.Filters:filtersList",line=137)
	public   void setFiltersList(org.radixware.ads.SelectorAddons.Dlg.common.SelectorAddonsInfoXsd.FiltersListDocument val) {
		filtersList = val;
	}

	/*Radix::SelectorAddons::EasSelectorAddons.Filters:isConditionDefined-Dynamic Property*/



	protected Bool isConditionDefined=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons.Filters:isConditionDefined",line=159)
	public  Bool getIsConditionDefined() {

		return Bool.valueOf(isConditionDefined() || baseFilterGuid!=null);
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons.Filters:isConditionDefined",line=166)
	public   void setIsConditionDefined(Bool val) {
		isConditionDefined = val;
	}



























































	/*Radix::SelectorAddons::EasSelectorAddons.Filters:Methods-Methods*/

	/*Radix::SelectorAddons::EasSelectorAddons.Filters:beforeCreate-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons.Filters:beforeCreate",line=234)
	protected published  boolean beforeCreate (org.radixware.kernel.server.types.Entity src) {
		final Types::Id newFilterId = Types::Id.Factory.newInstance(Meta::DefinitionIdPrefix:FILTER);
		guid = newFilterId.toString();
		if (!isConditionDefined()){
		    condition = null;//normalize empty condition
		}
		return super.beforeCreate(src);
	}

	/*Radix::SelectorAddons::EasSelectorAddons.Filters:getFiltersInfoByTable-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons.Filters:getFiltersInfoByTable",line=246)
	private final  org.radixware.ads.SelectorAddons.Dlg.common.SelectorAddonsInfoXsd.FiltersListDocument getFiltersInfoByTable (Str tableGuid) {
		final Types::Id tableId = Types::Id.Factory.loadFrom(tableGuid);
		SelectorAddonsInfoXsd:FiltersListDocument document = SelectorAddonsInfoXsd:FiltersListDocument.Factory.newInstance();
		final SelectorAddonsInfoXsd:FiltersListDocument.FiltersList filters = document.addNewFiltersList();
		final java.util.Collection<Meta::ClassDef> classes;
		try{
		    classes = getArte().DefManager.getAllClassDefsBasedOnTableByTabId(tableId);
		}
		catch(Exceptions::DefinitionError err){
		    return document;
		}
		final java.util.Collection<Types::Id> filterIds = new java.util.LinkedList<Types::Id>();
		for (Meta::ClassDef classDef: classes){
		    for(org.radixware.kernel.server.meta.presentations.RadFilterDef filter: classDef.Presentation.getFilters()){
		        if (!filterIds.contains(filter.getId())){            
		            filterIds.add(filter.getId());
		            final Meta::ClassDef ownerClass = getArte().DefManager.getClassDef(filter.TitleOwnerDefId);
		            final SelectorAddonsInfoXsd:FilterInfo filterInfo = filters.addNewFilterInfo();
		            filterInfo.Id = filter.getId();
		            filterInfo.TableId = tableId;
		            filterInfo.OwnerClassId = ownerClass.getId();
		            filterInfo.OwnerClassName = ownerClass.Name;            
		            if (filter.TitleId==null){
		                filterInfo.Name = filter.Name;
		            }
		            else{
		                filterInfo.Name = 
		                    org.radixware.kernel.common.types.MultilingualString.get(getArte(), filter.TitleOwnerDefId, filter.TitleId);
		            }   
		            filterInfo.FullName = filter.QualifiedName;            
		        }
		    }   
		}
		return document;
	}

	/*Radix::SelectorAddons::EasSelectorAddons.Filters:isBaseFilterExists-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons.Filters:isBaseFilterExists",line=285)
	private final  boolean isBaseFilterExists () {
		final SelectorAddonsInfoXsd:FiltersListDocument filters = filtersList;
		if (filters!=null && filters.FiltersList.FilterInfoList!=null && !filters.FiltersList.FilterInfoList.isEmpty()){    
		    for (SelectorAddonsInfoXsd:FilterInfo filterInfo: filters.FiltersList.FilterInfoList){        
		        if (filterInfo.Id.toString().equals(baseFilterGuid))
		            return true;
		    }
		}
		return false;
	}

	/*Radix::SelectorAddons::EasSelectorAddons.Filters:checkProperties-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons.Filters:checkProperties",line=300)
	protected  void checkProperties () {
		super.checkProperties();
		if (baseFilterGuid!=null && !isBaseFilterExists()){
		    throw new InvalidPropertyValueError(idof[EasSelectorAddons.Filters], idof[EasSelectorAddons.Filters:baseFilterGuid], String.format("Base filter #%s not found",baseFilterGuid));
		}
		if (baseFilterGuid==null && !isConditionDefined()){
		    throw new PropertyIsMandatoryError(idof[EasSelectorAddons.Filters], idof[EasSelectorAddons.Filters:condition]);
		}
	}

	/*Radix::SelectorAddons::EasSelectorAddons.Filters:isValid-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons.Filters:isValid",line=314)
	protected  boolean isValid () {
		return super.isValid() && (baseFilterGuid==null || isBaseFilterExists());
	}

	/*Radix::SelectorAddons::EasSelectorAddons.Filters:isConditionDefined-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons.Filters:isConditionDefined",line=321)
	private final  boolean isConditionDefined () {
		if (condition==null){
		    return false;
		}
		else{
		    final Meta::XscmlXsd:Sqml conditionXml;
		    try{
		         conditionXml = Meta::XscmlXsd:Sqml.Factory.parse(condition.getCharacterStream());
		    }    
		    catch(Exceptions::IOException ex){
		        trace(Arte::EventSeverity:Error,String.format("Cannot parse filter condition: %s",ex.getMessage()));
		        return false;        
		    }
		    catch(Exceptions::SQLException ex){
		        trace(Arte::EventSeverity:Error,String.format("Cannot parse filter condition: %s",ex.getMessage()));
		        return false;        
		    }
		    catch(Exceptions::XmlException ex){
		        trace(Arte::EventSeverity:Error,String.format("Cannot parse filter condition: %s",ex.getMessage()));
		        return false;
		    }
		    if (conditionXml.ItemList==null || conditionXml.ItemList.isEmpty()){
		        return false;
		    }
		    for (Meta::XscmlXsd:Sqml.Item sqmlItem: conditionXml.ItemList){
		        if (!sqmlItem.isSetSql()){
		            return true; 
		        }
		        else if (!sqmlItem.Sql.trim().isEmpty()){
		            return true;
		        }
		    }
		    return false;
		}
	}

	/*Radix::SelectorAddons::EasSelectorAddons.Filters:appendToXml-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons.Filters:appendToXml",line=360)
	public  void appendToXml (org.radixware.schemas.groupsettings.CustomFiltersDocument.CustomFilters filters) throws java.io.IOException,java.sql.SQLException,org.apache.xmlbeans.XmlException {
		final GroupSettingsXsd:CustomFilter filterXml = filters.addNewFilter();
		filterXml.Id = Types::Id.Factory.loadFrom(guid);
		filterXml.Name = title;
		filterXml.IsActive = isActive==null ? true : isActive.booleanValue();
		filterXml.DefinitionType = Meta::DefType:Filter;
		filterXml.TableId = Types::Id.Factory.loadFrom(tableGuid);
		filterXml.BaseFilterId = Types::Id.Factory.loadFrom(baseFilterGuid);
		if (selPresentations!=null){
		    final java.util.List<Types::Id> allowedPresentations = new java.util.LinkedList<Types::Id>();
		    for (Str presentationId: selPresentations){
		        allowedPresentations.add(Types::Id.Factory.loadFrom(presentationId));
		    }
		    filterXml.SelectorPresentations = allowedPresentations;
		}
		if (condition!=null && condition.length()>0)
		    filterXml.Condition = Meta::XscmlXsd:Sqml.Factory.parse(condition.getCharacterStream());
		if (parameters!=null && parameters.length()>0)
		    filterXml.Parameters = GroupSettingsXsd:FilterParameters.Factory.parse(parameters.getCharacterStream());
	}

	/*Radix::SelectorAddons::EasSelectorAddons.Filters:beforeUpdate-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons.Filters:beforeUpdate",line=385)
	protected published  boolean beforeUpdate () {
		if (!isConditionDefined()){
		    condition = null;//normalize empty condition
		}
		return super.beforeUpdate();
	}

	/*Radix::SelectorAddons::EasSelectorAddons.Filters:exportThis-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons.Filters:exportThis",line=395)
	  void exportThis (org.radixware.ads.CfgManagement.server.CfgExportData data) {
		data.itemClassId = idof[CfgItem.EasFiltersSingle];
		data.object = this;
		data.objectRid = guid;
		GroupSettingsXsd:CustomFiltersDocument xDoc = GroupSettingsXsd:CustomFiltersDocument.Factory.newInstance();
		xDoc.addNewCustomFilters();
		try {
		    appendToXml(xDoc.CustomFilters);
		    data.data = xDoc;
		    data.fileContent = data.data;
		} catch (Exceptions::IOException | Exceptions::SQLException | Exceptions::XmlException e) {
		    throw new AppError("Error on export Common filter '" + calcTitle() + "'", e);
		}
	}

	/*Radix::SelectorAddons::EasSelectorAddons.Filters:loadByPK-System Method*/

	@SuppressWarnings("unused")
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons.Filters:loadByPK",line=414)
	public static published  org.radixware.ads.SelectorAddons.Dlg.server.EasSelectorAddons.Filters loadByPK (Str guid, boolean checkExistance) {
	final java.util.HashMap<org.radixware.kernel.common.types.Id,Object> pkValsMap = new java.util.HashMap<org.radixware.kernel.common.types.Id,Object>(5);
			if(guid==null) return null;
			pkValsMap.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("colTOTOEP36JFH35MA7BJYRU67GKU"),guid);
	org.radixware.kernel.server.types.Pid pid = new org.radixware.kernel.server.types.Pid(org.radixware.ads.mdlPEKYFVDRVZHGZCBQQDY2NOYFOY.server.pdcArte______________________.__getArteInstanceInternal(),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblHQSOHRWZH5ESLD2Y6IVF6XFBE4"),pkValsMap);
		try{
		return (
		org.radixware.ads.SelectorAddons.Dlg.server.EasSelectorAddons.Filters) org.radixware.ads.mdlPEKYFVDRVZHGZCBQQDY2NOYFOY.server.pdcArte______________________.__getArteInstanceInternal().getEntityObject(pid,null,checkExistance);
	}catch(org.radixware.kernel.server.exceptions.EntityObjectNotExistsError e){
	return null;
	}

	}

	/*Radix::SelectorAddons::EasSelectorAddons.Filters:loadByPidStr-System Method*/

	@SuppressWarnings("unused")
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons.Filters:loadByPidStr",line=432)
	public static published  org.radixware.ads.SelectorAddons.Dlg.server.EasSelectorAddons.Filters loadByPidStr (Str pidAsStr, boolean checkExistance) {
	org.radixware.kernel.server.types.Pid pid = new org.radixware.kernel.server.types.Pid(org.radixware.ads.mdlPEKYFVDRVZHGZCBQQDY2NOYFOY.server.pdcArte______________________.__getArteInstanceInternal(),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblHQSOHRWZH5ESLD2Y6IVF6XFBE4"),pidAsStr);
		try{
		return (
		org.radixware.ads.SelectorAddons.Dlg.server.EasSelectorAddons.Filters) org.radixware.ads.mdlPEKYFVDRVZHGZCBQQDY2NOYFOY.server.pdcArte______________________.__getArteInstanceInternal().getEntityObject(pid,null,checkExistance);
	}catch(org.radixware.kernel.server.exceptions.EntityObjectNotExistsError e){
	return null;
	}

	}

	/*Radix::SelectorAddons::EasSelectorAddons.Filters:exportAll-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons.Filters:exportAll",line=446)
	 static  void exportAll (org.radixware.ads.CfgManagement.server.CfgExportData data, java.util.Iterator<org.radixware.ads.SelectorAddons.Dlg.server.EasSelectorAddons> iter, Str tableId) {
		GroupSettingsXsd:CustomFiltersDocument groupDoc = GroupSettingsXsd:CustomFiltersDocument.Factory.newInstance();
		GroupSettingsXsd:CustomFiltersDocument.CustomFilters xFilters = groupDoc.addNewCustomFilters();

		if (iter == null) {
		    SelectorAddons.Db::SelectorAddonsCursor c = SelectorAddons.Db::SelectorAddonsCursor.open(tableId, idof[EasSelectorAddons.Filters].toString());
		    iter = new EntityCursorIterator<EasSelectorAddons>(c, idof[SelectorAddons.Db::SelectorAddonsCursor:addon]);
		}

		try {
		    while (iter.hasNext()) {
		        try {
		            EasSelectorAddons addon = iter.next();
		            if (addon instanceof EasSelectorAddons.Filters == false) {
		                continue;
		            }
		            EasSelectorAddons.Filters f = (EasSelectorAddons.Filters) addon;
		            GroupSettingsXsd:CustomFiltersDocument singleDoc = GroupSettingsXsd:CustomFiltersDocument.Factory.newInstance(); 
		            f.appendToXml(singleDoc.addNewCustomFilters());
		            xFilters.addNewFilter().set(singleDoc.CustomFilters.FilterList.get(0));
		            data.children.add(new CfgExportData(f, null, idof[CfgItem.EasFiltersSingle], singleDoc));
		        } catch (Exceptions::IOException | Exceptions::SQLException | Exceptions::XmlException e) {
		            Arte::Trace.error("Error on export common filter: " + Arte::Trace.exceptionStackToString(e), Arte::EventSource:AppCfgPackage);
		        }
		    }
		} finally {
		    EntityCursorIterator.closeIterator(iter);
		}

		Arte::TypesXsd:StrDocument xTableId = null;
		if (tableId != null) {
		    xTableId = Arte::TypesXsd:StrDocument.Factory.newInstance();
		    xTableId.Str = tableId;
		}
		data.itemClassId = idof[CfgItem.EasFiltersGroup];
		data.object = null;
		data.data = xTableId;
		data.fileContent = groupDoc;
	}



	@Override
	public org.radixware.kernel.server.types.FormHandler.NextDialogsRequest execCommand(org.radixware.kernel.common.types.Id cmdId, org.radixware.kernel.common.types.Id propId, org.apache.xmlbeans.XmlObject input, org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValsById, org.apache.xmlbeans.XmlObject output) throws org.radixware.kernel.common.exceptions.AppException,java.lang.InterruptedException{
		if(cmdId == cmdGBE4YFYAGZB6TD5XGW43D7QYWU){
			final org.radixware.schemas.utils.RPCResponseDocument result =  rpc_callcmdGBE4YFYAGZB6TD5XGW43D7QYWU_implementation((org.radixware.schemas.utils.RPCRequestDocument)org.radixware.kernel.common.utils.XmlObjectProcessor.getAppCommandInputData(getClass().getClassLoader(),input,org.radixware.schemas.utils.RPCRequestDocument.class));
			if(result != null)
				output.set(result);
			return null;
		} else 
			return super.execCommand(cmdId,propId,input,newPropValsById,output);
	}
	private final org.radixware.schemas.utils.RPCResponseDocument rpc_callcmdGBE4YFYAGZB6TD5XGW43D7QYWU_implementation(org.radixware.schemas.utils.RPCRequestDocument input){
		final org.radixware.kernel.common.utils.RPCProcessor.ServerSideInvocation invoker = new org.radixware.kernel.common.utils.RPCProcessor.ServerSideInvocation(input){
			@Override
			protected  org.radixware.kernel.common.enums.EValType getReturnType(){
				return org.radixware.kernel.common.enums.EValType.XML;
			}
			protected  Object invokeImpl(Object[] arguments){
				final Str p0 = arguments[0] == null ? null : (Str)arguments[0];
				Object $res$ =org.radixware.ads.SelectorAddons.Dlg.server.EasSelectorAddons.Filters.this.mthABII4AHMDBEJ3HSOI24SC2OQXQ(p0);
				return $res$;
			}
		};
		return invoker.invoke();
	}


}

/* Radix::SelectorAddons::EasSelectorAddons.Filters - Server Meta*/

/*Radix::SelectorAddons::EasSelectorAddons.Filters-Application Class*/

package org.radixware.ads.SelectorAddons.Dlg.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class EasSelectorAddons.Filters_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("aclLQBIKZWZABEIXKZOG4W3L6I5XI"),"EasSelectorAddons.Filters",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsSUYUAJP7WZC5XBCTKRQWEALHEE"),

						org.radixware.kernel.common.enums.EClassType.APPLICATION,

						/*Radix::SelectorAddons::EasSelectorAddons.Filters:Presentations-Entity Object Presentations*/
						/*Class presentation attributes*/
						 new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef(/*Owner Class Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("aclLQBIKZWZABEIXKZOG4W3L6I5XI"),
							/*Owner Class Name*/
							"EasSelectorAddons.Filters",
							/*Title Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsSUYUAJP7WZC5XBCTKRQWEALHEE"),
							/*Property presentations*/

							/*Radix::SelectorAddons::EasSelectorAddons.Filters:Properties-Properties*/
							new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef[]{

									/*Radix::SelectorAddons::EasSelectorAddons.Filters:baseFilterGuid:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXT2U3TRV6VGAPEULXGUEZMPOAU"),org.radixware.kernel.common.enums.EEditPossibility.ONLY_IN_EDITOR,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::SelectorAddons::EasSelectorAddons.Filters:condition:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col6LHR4RTUVNEYXKKGYXZZSB74PY"),org.radixware.kernel.common.enums.EEditPossibility.ONLY_IN_EDITOR,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::SelectorAddons::EasSelectorAddons.Filters:parameters:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colUZMKYBFP4VAWBHFP5NM7MIEZRU"),org.radixware.kernel.common.enums.EEditPossibility.ONLY_IN_EDITOR,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::SelectorAddons::EasSelectorAddons.Filters:filtersList:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdERYDI62VORCZZAANZ74CCFC3ZA"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::SelectorAddons::EasSelectorAddons.Filters:isConditionDefined:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdTWUGN4HMCFAQXE4GYZFLHUJXNI"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT))
							},
							/*Commands*/
							new org.radixware.kernel.server.meta.presentations.RadCommandDef[]{

									new org.radixware.kernel.server.meta.presentations.RadCommandDef(
									/*Radix::SelectorAddons::EasSelectorAddons.Filters:remoteCall_getFiltersInfoByTable-Remote Call Command*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdGBE4YFYAGZB6TD5XGW43D7QYWU"),"remoteCall_getFiltersInfoByTable",org.radixware.kernel.common.enums.ECommandScope.OBJECT,null,org.radixware.kernel.common.enums.ECommandAccessibility.ALWAYS,org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,org.radixware.kernel.common.types.Id.Factory.loadFrom("aclLQBIKZWZABEIXKZOG4W3L6I5XI"),false,true),

									new org.radixware.kernel.server.meta.presentations.RadCommandDef(
									/*Radix::SelectorAddons::EasSelectorAddons.Filters:export-Object Command*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdDIVOCT5STFC3FAOIAOJMOYBMQU"),"export",org.radixware.kernel.common.enums.ECommandScope.OBJECT,null,org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_FIXED,org.radixware.kernel.common.enums.ECommandNature.FORM_IN_OUT,org.radixware.kernel.common.types.Id.Factory.loadFrom("aclLQBIKZWZABEIXKZOG4W3L6I5XI"),false,true)
							},
							/*Sortings*/
							null,
							/*Filters*/
							null,
							/*Editor presentations*/
							new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef[]{

									new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef(
									/*Radix::SelectorAddons::EasSelectorAddons.Filters:Edit-Editor Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("epr43LE34LJRVHNRPIZLU4ONQKJEA"),
									"Edit",
									org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprFGKCGE2H6FGXDD4YKLBKW77X3U"),
									36002,
									null,

									/*Radix::SelectorAddons::EasSelectorAddons.Filters:Edit:Children-Explorer Items*/
										null
									,
									null,
									(org.radixware.kernel.server.meta.presentations.RadPropertyPresentationAttributes[]) null,
									org.radixware.kernel.common.enums.EEditorPresentationRightsInheritanceMode.NONE,
									null),

									new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef(
									/*Radix::SelectorAddons::EasSelectorAddons.Filters:Create-Editor Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprL2VKQPIHWRA2JIIFBTXMDHADQI"),
									"Create",
									org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
									org.radixware.kernel.common.types.Id.Factory.loadFrom("epr43LE34LJRVHNRPIZLU4ONQKJEA"),
									36016,
									null,

									/*Radix::SelectorAddons::EasSelectorAddons.Filters:Create:Children-Explorer Items*/
										null
									,
									null,
									(org.radixware.kernel.server.meta.presentations.RadPropertyPresentationAttributes[]) null,
									org.radixware.kernel.common.enums.EEditorPresentationRightsInheritanceMode.NONE,
									null)
							},
							/*Selector presentations*/
							new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef[]{

									new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef(
									/*Radix::SelectorAddons::EasSelectorAddons.Filters:General-Selector Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("sprQWY42P52BFF3JGZLM2EPVKHC7U"),"General",org.radixware.kernel.common.types.Id.Factory.loadFrom("sprIIEIWQINNRCG5OAYT2TYA6NNE4"),42,new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn[]{

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdJWOOWTAQA5EPHJHYBAYNLMEKAU"),false),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdTWUGN4HMCFAQXE4GYZFLHUJXNI"),false),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colTOTOEP36JFH35MA7BJYRU67GKU"),false),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDCQEQLHEC5F6VO633LHAE6BDMQ"),false),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col6QH5TILJHRDDTC4CHYLQ6HLECA"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colZ23Q7UUURBEAHGCRRSQEKZH76I"),true)
									},new org.radixware.kernel.server.meta.presentations.RadConditionDef("<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:PropSqlName TableId=\"tblHQSOHRWZH5ESLD2Y6IVF6XFBE4\" PropId=\"col36K2MUPKPRGPPAGLVPAUJ52PQE\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> = </xsc:Sql></xsc:Item><xsc:Item><xsc:Id Path=\"aclLQBIKZWZABEIXKZOG4W3L6I5XI\"/></xsc:Item></xsc:Sqml>","<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),null,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr43LE34LJRVHNRPIZLU4ONQKJEA")},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprL2VKQPIHWRA2JIIFBTXMDHADQI")},null,org.radixware.kernel.common.types.Id.Factory.loadFrom("eccRYQ6UPM3UFFZLMREUPPTJ3CM3Y"))
							},
							/*Default selector presentation Id*/
							null,
							/*Presentation Map*/
							org.radixware.kernel.common.utils.Maps.fromArrays(new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprFGKCGE2H6FGXDD4YKLBKW77X3U")},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr43LE34LJRVHNRPIZLU4ONQKJEA")}),
							/*Object title format*/
							null,
							/*Class catalogs*/
							new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog[]{

									new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog(
									/*Radix::SelectorAddons::EasSelectorAddons.Filters:General-Static Class Catalog*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eccRYQ6UPM3UFFZLMREUPPTJ3CM3Y"),org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.ELinkMode.FINAL,new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.Item[]{
										new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.ClassRef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aclLQBIKZWZABEIXKZOG4W3L6I5XI"),null,10.0,true,org.radixware.kernel.common.types.Id.Factory.loadFrom("aclLQBIKZWZABEIXKZOG4W3L6I5XI"),null)}
									)
							},
							/*Restrictions*/
							null,null,0),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblHQSOHRWZH5ESLD2Y6IVF6XFBE4"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHQSOHRWZH5ESLD2Y6IVF6XFBE4"),

						/*Radix::SelectorAddons::EasSelectorAddons.Filters:Properties-Properties*/
						new org.radixware.kernel.server.meta.clazzes.RadPropDef[]{

								/*Radix::SelectorAddons::EasSelectorAddons.Filters:baseFilterGuid-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXT2U3TRV6VGAPEULXGUEZMPOAU"),"baseFilterGuid",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRYC5EYTBHBCPPMTI5OEFHN2IC4"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::SelectorAddons::EasSelectorAddons.Filters:condition-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col6LHR4RTUVNEYXKKGYXZZSB74PY"),"condition",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBDMIM6YYTNGBJM5NSQOYK77IFE"),org.radixware.kernel.common.enums.EValType.CLOB,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::SelectorAddons::EasSelectorAddons.Filters:parameters-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colUZMKYBFP4VAWBHFP5NM7MIEZRU"),"parameters",null,org.radixware.kernel.common.enums.EValType.CLOB,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::SelectorAddons::EasSelectorAddons.Filters:filtersList-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdERYDI62VORCZZAANZ74CCFC3ZA"),"filtersList",null,org.radixware.kernel.common.enums.EValType.XML,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::SelectorAddons::EasSelectorAddons.Filters:isConditionDefined-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdTWUGN4HMCFAQXE4GYZFLHUJXNI"),"isConditionDefined",null,org.radixware.kernel.common.enums.EValType.BOOL,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE)
						},

						/*Radix::SelectorAddons::EasSelectorAddons.Filters:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthFMXUFM5J25E4TNM4PR73F6V3E4"),"beforeCreate",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("src",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr43M3VXMU4ZA2ROUH6N3UNLQPJU"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthABII4AHMDBEJ3HSOI24SC2OQXQ"),"getFiltersInfoByTable",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("tableGuid",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprFJKRDRFLQRCG7CIT3VPPB3WETM"))
								},org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth6UAFN5QIMFG53K77URUDUZEQ34"),"isBaseFilterExists",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthWKZ3XBJ67ZB5TEPE4T6HDKKIZY"),"checkProperties",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthD6QMQHGF6FBKDLIX27CFDNFIUE"),"isValid",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth4TAEY325F5C5PD57QYSHZCKRLY"),"isConditionDefined",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthSM3LEHMYXFFQLKICOB2DEXFIPU"),"appendToXml",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("filters",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprHZABR424AVG4NMP37Q3BFIEBFE"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthPT5PMP2WVZGUDB7OTLOE2SBWKE"),"beforeUpdate",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthXTOLSGNXNVFEVOEL3VX2YMLEXM"),"exportThis",false,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("data",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprSY7BLG7O3BHKBIUZS7UE5KVS3Y"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth_loadByPK_________________"),"loadByPK",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("guid",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprTOTOEP36JFH35MA7BJYRU67GKU")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("checkExistance",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprCheckExisstance___________"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth_loadByPidStr_____________"),"loadByPidStr",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("pidAsStr",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprPidAsStr__________________")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("checkExistance",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprCheckExisstance___________"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthC3O4MUCF4JE4HPOQXZSM6MPJWE"),"exportAll",true,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("data",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprSY7BLG7O3BHKBIUZS7UE5KVS3Y")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("iter",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprZX6GUQ6XR5DOLBSBLANZAJ7YSE")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("tableId",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprUPCDU2CA45HPDNZBSDICPME6FQ"))
								},null)
						},
						null,
						null,null,null,false);
}

/* Radix::SelectorAddons::EasSelectorAddons.Filters - Desktop Executable*/

/*Radix::SelectorAddons::EasSelectorAddons.Filters-Application Class*/

package org.radixware.ads.SelectorAddons.Dlg.explorer;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons.Filters")
public interface EasSelectorAddons.Filters   extends org.radixware.ads.SelectorAddons.Dlg.explorer.EasSelectorAddons  {























































































































	/*Radix::SelectorAddons::EasSelectorAddons.Filters:condition:condition-Presentation Property*/


	public class Condition extends org.radixware.kernel.common.client.models.items.properties.PropertyClob{
		public Condition(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons.Filters:condition:condition",line=893)
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons.Filters:condition:condition",line=899)
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Condition getCondition();
	/*Radix::SelectorAddons::EasSelectorAddons.Filters:parameters:parameters-Presentation Property*/


	public class Parameters extends org.radixware.kernel.common.client.models.items.properties.PropertyClob{
		public Parameters(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons.Filters:parameters:parameters",line=932)
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons.Filters:parameters:parameters",line=938)
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Parameters getParameters();
	/*Radix::SelectorAddons::EasSelectorAddons.Filters:baseFilterGuid:baseFilterGuid-Presentation Property*/


	public class BaseFilterGuid extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public BaseFilterGuid(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons.Filters:baseFilterGuid:baseFilterGuid",line=971)
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons.Filters:baseFilterGuid:baseFilterGuid",line=977)
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public BaseFilterGuid getBaseFilterGuid();
	/*Radix::SelectorAddons::EasSelectorAddons.Filters:filtersList:filtersList-Presentation Property*/


	public class FiltersList extends org.radixware.kernel.common.client.models.items.properties.PropertyXml{
		public FiltersList(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Class<org.radixware.ads.SelectorAddons.Dlg.common.SelectorAddonsInfoXsd.FiltersListDocument> getValClass(){
			return org.radixware.ads.SelectorAddons.Dlg.common.SelectorAddonsInfoXsd.FiltersListDocument.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.ads.SelectorAddons.Dlg.common.SelectorAddonsInfoXsd.FiltersListDocument dummy = x == null ? null : (org.radixware.ads.SelectorAddons.Dlg.common.SelectorAddonsInfoXsd.FiltersListDocument)org.radixware.kernel.common.utils.XmlObjectProcessor.castToXmlClass(getClass().getClassLoader(),(org.apache.xmlbeans.XmlObject)x,org.radixware.ads.SelectorAddons.Dlg.common.SelectorAddonsInfoXsd.FiltersListDocument.class);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons.Filters:filtersList:filtersList",line=1014)
		public  org.radixware.ads.SelectorAddons.Dlg.common.SelectorAddonsInfoXsd.FiltersListDocument getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons.Filters:filtersList:filtersList",line=1020)
		public   void setValue(org.radixware.ads.SelectorAddons.Dlg.common.SelectorAddonsInfoXsd.FiltersListDocument val) {
			Value = val;
		}
	}
	public FiltersList getFiltersList();
	/*Radix::SelectorAddons::EasSelectorAddons.Filters:isConditionDefined:isConditionDefined-Presentation Property*/


	public class IsConditionDefined extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public IsConditionDefined(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons.Filters:isConditionDefined:isConditionDefined",line=1053)
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons.Filters:isConditionDefined:isConditionDefined",line=1059)
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public IsConditionDefined getIsConditionDefined();
	public static class Export extends org.radixware.kernel.common.client.models.items.Command{
		protected Export(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}

	}

	public static class RemoteCall_getFiltersInfoByTable extends org.radixware.kernel.common.client.models.items.Command{
		protected RemoteCall_getFiltersInfoByTable(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public org.radixware.schemas.utils.RPCResponseDocument send(org.radixware.schemas.utils.RPCRequestDocument input ) throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,input);
			org.apache.xmlbeans.XmlObject xmlResponse = processResponse(response, null);
			return (org.radixware.schemas.utils.RPCResponseDocument)org.radixware.kernel.common.utils.XmlObjectProcessor.castToXmlClass(getClass().getClassLoader(),xmlResponse,org.radixware.schemas.utils.RPCResponseDocument.class);
		}

	}



}

/* Radix::SelectorAddons::EasSelectorAddons.Filters - Desktop Meta*/

/*Radix::SelectorAddons::EasSelectorAddons.Filters-Application Class*/

package org.radixware.ads.SelectorAddons.Dlg.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class EasSelectorAddons.Filters_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::SelectorAddons::EasSelectorAddons.Filters:Presentations-Entity Object Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aclLQBIKZWZABEIXKZOG4W3L6I5XI"),
			"Radix::SelectorAddons::EasSelectorAddons.Filters",
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHQSOHRWZH5ESLD2Y6IVF6XFBE4"),
			null,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsSUYUAJP7WZC5XBCTKRQWEALHEE"),null,null,0,

			/*Radix::SelectorAddons::EasSelectorAddons.Filters:Properties-Properties*/
			new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

					/*Radix::SelectorAddons::EasSelectorAddons.Filters:baseFilterGuid:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXT2U3TRV6VGAPEULXGUEZMPOAU"),
						"baseFilterGuid",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRYC5EYTBHBCPPMTI5OEFHN2IC4"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclLQBIKZWZABEIXKZOG4W3L6I5XI"),
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						false,false,
						false,
						true,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("cpeLQZWX7ZNQJAZHLXLH3Z3NF6SOE"),
						true,

						/*Radix::SelectorAddons::EasSelectorAddons.Filters:baseFilterGuid:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,50,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::SelectorAddons::EasSelectorAddons.Filters:condition:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col6LHR4RTUVNEYXKKGYXZZSB74PY"),
						"condition",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBDMIM6YYTNGBJM5NSQOYK77IFE"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclLQBIKZWZABEIXKZOG4W3L6I5XI"),
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
						org.radixware.kernel.common.enums.EEditPossibility.ONLY_IN_EDITOR,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::SelectorAddons::EasSelectorAddons.Filters:condition:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::SelectorAddons::EasSelectorAddons.Filters:parameters:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colUZMKYBFP4VAWBHFP5NM7MIEZRU"),
						"parameters",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclLQBIKZWZABEIXKZOG4W3L6I5XI"),
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
						org.radixware.kernel.common.enums.EEditPossibility.ONLY_IN_EDITOR,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::SelectorAddons::EasSelectorAddons.Filters:parameters:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::SelectorAddons::EasSelectorAddons.Filters:filtersList:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdERYDI62VORCZZAANZ74CCFC3ZA"),
						"filtersList",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclLQBIKZWZABEIXKZOG4W3L6I5XI"),
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
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
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

					/*Radix::SelectorAddons::EasSelectorAddons.Filters:isConditionDefined:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdTWUGN4HMCFAQXE4GYZFLHUJXNI"),
						"isConditionDefined",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclLQBIKZWZABEIXKZOG4W3L6I5XI"),
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::SelectorAddons::EasSelectorAddons.Filters:isConditionDefined:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aclLQBIKZWZABEIXKZOG4W3L6I5XI"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false)
			},
			new org.radixware.kernel.common.client.meta.RadCommandDef[]{
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::SelectorAddons::EasSelectorAddons.Filters:remoteCall_getFiltersInfoByTable-Remote Call Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdGBE4YFYAGZB6TD5XGW43D7QYWU"),
						"remoteCall_getFiltersInfoByTable",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclLQBIKZWZABEIXKZOG4W3L6I5XI"),
						null,
						null,
						null,
						org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,
						false,
						false,
						false,
						org.radixware.kernel.common.enums.ECommandScope.RPC,
						null,
						org.radixware.kernel.common.enums.ECommandAccessibility.ALWAYS),
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::SelectorAddons::EasSelectorAddons.Filters:export-Object Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdDIVOCT5STFC3FAOIAOJMOYBMQU"),
						"export",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclLQBIKZWZABEIXKZOG4W3L6I5XI"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsSWAJ2ZYPSVDU7HFG2WK23535FU"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("imgVORNT5ZFORBEHLSYMJNUWJK6II"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("afhSDDPOLVAUJDQPCSAESKEYXNL5U"),
						org.radixware.kernel.common.enums.ECommandNature.FORM_IN_OUT,
						true,
						false,
						false,
						org.radixware.kernel.common.enums.ECommandScope.OBJECT,
						null,
						org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_FIXED,
						true)
			},
			null,
			null,
			null,
			new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr43LE34LJRVHNRPIZLU4ONQKJEA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("eprL2VKQPIHWRA2JIIFBTXMDHADQI"),org.radixware.kernel.common.types.Id.Factory.loadFrom("sprQWY42P52BFF3JGZLM2EPVKHC7U")},
			true,false,false);
}

/* Radix::SelectorAddons::EasSelectorAddons.Filters - Web Meta*/

/*Radix::SelectorAddons::EasSelectorAddons.Filters-Application Class*/

package org.radixware.ads.SelectorAddons.Dlg.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class EasSelectorAddons.Filters_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::SelectorAddons::EasSelectorAddons.Filters:Presentations-Entity Object Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aclLQBIKZWZABEIXKZOG4W3L6I5XI"),
			"Radix::SelectorAddons::EasSelectorAddons.Filters",
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHQSOHRWZH5ESLD2Y6IVF6XFBE4"),
			null,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsSUYUAJP7WZC5XBCTKRQWEALHEE"),null,null,0,
			null,
			null,
			null,
			null,
			null,
			null,
			false,false,false);
}

/* Radix::SelectorAddons::EasSelectorAddons.Filters:Edit - Desktop Meta*/

/*Radix::SelectorAddons::EasSelectorAddons.Filters:Edit-Editor Presentation*/

package org.radixware.ads.SelectorAddons.Dlg.explorer;
public final class Edit_mi{
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new org.radixware.kernel.common.client.meta.RadEditorPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epr43LE34LJRVHNRPIZLU4ONQKJEA"),
	"Edit",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
	org.radixware.kernel.common.types.Id.Factory.loadFrom("eprFGKCGE2H6FGXDD4YKLBKW77X3U"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("aclLQBIKZWZABEIXKZOG4W3L6I5XI"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("tblHQSOHRWZH5ESLD2Y6IVF6XFBE4"),
	null,
	org.radixware.kernel.common.types.Id.Factory.loadFrom("imgJAFMGUWJY5H5PFIPGPERL5ESMA"),

	/*Radix::SelectorAddons::EasSelectorAddons.Filters:Edit:Editor Pages-Editor Presentation Pages*/
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

			/*Radix::SelectorAddons::EasSelectorAddons.Filters:Edit:General-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgTVUJTUPRFVEQFA4XZPO34BWM5E"),"General",org.radixware.kernel.common.types.Id.Factory.loadFrom("aclLQBIKZWZABEIXKZOG4W3L6I5XI"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNOALWTLFZRAOVFCOXEYSGLAE5Q"),null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col6QH5TILJHRDDTC4CHYLQ6HLECA"),0,1,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colZ23Q7UUURBEAHGCRRSQEKZH76I"),0,3,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colTZ42HZBXFFBPVL6P4MTIMOAHQE"),0,5,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colVTTB5PHSCFG5TAUCLOI27MO5XM"),0,4,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDCQEQLHEC5F6VO633LHAE6BDMQ"),0,0,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXT2U3TRV6VGAPEULXGUEZMPOAU"),0,2,1,false,false)
			},null),

			/*Radix::SelectorAddons::EasSelectorAddons.Filters:Edit:Condition-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadCustomEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgNV7ENS7ODJBOXPOJ6O4FPXTCAU"),"Condition",org.radixware.kernel.common.types.Id.Factory.loadFrom("aclLQBIKZWZABEIXKZOG4W3L6I5XI"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3NRNA2EONJEK3JZ6EWK3PWFVAI"),null,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("cepLURUDU744NDA5OPXAAQ3T65MCQ"))
	},
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgTVUJTUPRFVEQFA4XZPO34BWM5E")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgNV7ENS7ODJBOXPOJ6O4FPXTCAU")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgZG6UTMVC6ND6TPIYVCGMT3YWIY"))}
	,

	/*Radix::SelectorAddons::EasSelectorAddons.Filters:Edit:Children-Explorer Items*/
		null
	,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
	null,
	0,
	null,
	(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
	36002,0,0);
}
/* Radix::SelectorAddons::EasSelectorAddons.Filters:Edit:Model - Desktop Executable*/

/*Radix::SelectorAddons::EasSelectorAddons.Filters:Edit:Model-Entity Model Class*/

package org.radixware.ads.SelectorAddons.Dlg.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons.Filters:Edit:Model")
public class Edit:Model  extends org.radixware.ads.SelectorAddons.Dlg.explorer.EasSelectorAddons.Filters.EasSelectorAddons.Filters_DefaultModel.eprFGKCGE2H6FGXDD4YKLBKW77X3U_ModelAdapter {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return Edit:Model_mi.rdxMeta; }



	public Edit:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadEditorPresentationDef def){super(userSession,def);}

	/*Radix::SelectorAddons::EasSelectorAddons.Filters:Edit:Model:Nested classes-Nested Classes*/

	/*Radix::SelectorAddons::EasSelectorAddons.Filters:Edit:Model:Properties-Properties*/

	/*Radix::SelectorAddons::EasSelectorAddons.Filters:Edit:Model:filtersForCurrentTable-Dynamic Property*/



	protected org.radixware.ads.SelectorAddons.Dlg.common.SelectorAddonsInfoXsd.FiltersListDocument filtersForCurrentTable=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons.Filters:Edit:Model:filtersForCurrentTable",line=1422)
	protected  org.radixware.ads.SelectorAddons.Dlg.common.SelectorAddonsInfoXsd.FiltersListDocument getFiltersForCurrentTable() {
		return filtersForCurrentTable;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons.Filters:Edit:Model:filtersForCurrentTable",line=1428)
	protected   void setFiltersForCurrentTable(org.radixware.ads.SelectorAddons.Dlg.common.SelectorAddonsInfoXsd.FiltersListDocument val) {
		filtersForCurrentTable = val;
	}

	/*Radix::SelectorAddons::EasSelectorAddons.Filters:Edit:Model:btnClearBaseFilter-Dynamic Property*/



	protected com.trolltech.qt.gui.QToolButton btnClearBaseFilter=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons.Filters:Edit:Model:btnClearBaseFilter",line=1450)
	private final  com.trolltech.qt.gui.QToolButton getBtnClearBaseFilter() {
		return btnClearBaseFilter;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons.Filters:Edit:Model:btnClearBaseFilter",line=1456)
	private final   void setBtnClearBaseFilter(com.trolltech.qt.gui.QToolButton val) {
		btnClearBaseFilter = val;
	}

	/*Radix::SelectorAddons::EasSelectorAddons.Filters:Edit:Model:baseFilterGuid-Presentation Property*/




	public class BaseFilterGuid extends org.radixware.ads.SelectorAddons.Dlg.explorer.EasSelectorAddons.Filters.EasSelectorAddons.Filters_DefaultModel.eprFGKCGE2H6FGXDD4YKLBKW77X3U_ModelAdapter.colXT2U3TRV6VGAPEULXGUEZMPOAU{
		public BaseFilterGuid(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons.Filters:Edit:Model:baseFilterGuid",line=1490)
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons.Filters:Edit:Model:baseFilterGuid",line=1496)
		public   void setValue(Str val) {

			internal[baseFilterGuid] = val;
			if (btnClearBaseFilter!=null)
			    btnClearBaseFilter.setVisible(val!=null);
			updateConditionPage();
		}
	}
	public BaseFilterGuid getBaseFilterGuid(){return (BaseFilterGuid)getProperty(colXT2U3TRV6VGAPEULXGUEZMPOAU);}

	/*Radix::SelectorAddons::EasSelectorAddons.Filters:Edit:Model:conditionEditorWithBaseFilter-Dynamic Property*/



	protected org.radixware.ads.SelectorAddons.Dlg.explorer.CommonFilterConditionEditor conditionEditorWithBaseFilter=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons.Filters:Edit:Model:conditionEditorWithBaseFilter",line=1524)
	private final  org.radixware.ads.SelectorAddons.Dlg.explorer.CommonFilterConditionEditor getConditionEditorWithBaseFilter() {
		return conditionEditorWithBaseFilter;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons.Filters:Edit:Model:conditionEditorWithBaseFilter",line=1530)
	private final   void setConditionEditorWithBaseFilter(org.radixware.ads.SelectorAddons.Dlg.explorer.CommonFilterConditionEditor val) {
		conditionEditorWithBaseFilter = val;
	}

	/*Radix::SelectorAddons::EasSelectorAddons.Filters:Edit:Model:conditionEditorWithoutBaseFilter-Dynamic Property*/



	protected org.radixware.ads.SelectorAddons.Dlg.explorer.CommonFilterConditionEditor conditionEditorWithoutBaseFilter=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons.Filters:Edit:Model:conditionEditorWithoutBaseFilter",line=1552)
	private final  org.radixware.ads.SelectorAddons.Dlg.explorer.CommonFilterConditionEditor getConditionEditorWithoutBaseFilter() {
		return conditionEditorWithoutBaseFilter;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons.Filters:Edit:Model:conditionEditorWithoutBaseFilter",line=1558)
	private final   void setConditionEditorWithoutBaseFilter(org.radixware.ads.SelectorAddons.Dlg.explorer.CommonFilterConditionEditor val) {
		conditionEditorWithoutBaseFilter = val;
	}

	/*Radix::SelectorAddons::EasSelectorAddons.Filters:Edit:Model:conditionStackedWidget-Dynamic Property*/



	protected com.trolltech.qt.gui.QStackedWidget conditionStackedWidget=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons.Filters:Edit:Model:conditionStackedWidget",line=1580)
	private final  com.trolltech.qt.gui.QStackedWidget getConditionStackedWidget() {
		return conditionStackedWidget;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons.Filters:Edit:Model:conditionStackedWidget",line=1586)
	private final   void setConditionStackedWidget(com.trolltech.qt.gui.QStackedWidget val) {
		conditionStackedWidget = val;
	}








	/*Radix::SelectorAddons::EasSelectorAddons.Filters:Edit:Model:Methods-Methods*/

	/*Radix::SelectorAddons::EasSelectorAddons.Filters:Edit:Model:beforeOpenView-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons.Filters:Edit:Model:beforeOpenView",line=1603)
	protected published  void beforeOpenView () {
		super.beforeOpenView();
		updateBaseFilterEditorState();
	}

	/*Radix::SelectorAddons::EasSelectorAddons.Filters:Edit:Model:getDisplayString-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons.Filters:Edit:Model:getDisplayString",line=1612)
	public published  Str getDisplayString (org.radixware.kernel.common.types.Id propertyId, java.lang.Object propertyValue, Str defaultDisplayString, boolean isInherited) {
		if (idof[EasSelectorAddons.Filters:baseFilterGuid].equals(propertyId) && propertyValue!=null){
		    final Str filterName = getCurrentBaseFilterName();
		    return filterName==null ? "#"+defaultDisplayString : filterName;
		}
		return super.getDisplayString(propertyId, propertyValue, defaultDisplayString, isInherited);
	}

	/*Radix::SelectorAddons::EasSelectorAddons.Filters:Edit:Model:afterRead-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons.Filters:Edit:Model:afterRead",line=1624)
	protected published  void afterRead () {
		super.afterRead();
		if (!isInSelectorRowContext()){
		    filtersForCurrentTable = filtersList.Value;  
		}
		if (btnClearBaseFilter!=null){
		    btnClearBaseFilter.setVisible(baseFilterGuid.Value!=null);
		}
		updateConditionPage();
	}

	/*Radix::SelectorAddons::EasSelectorAddons.Filters:Edit:Model:getFiltersInfoForCurrentTable-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons.Filters:Edit:Model:getFiltersInfoForCurrentTable",line=1638)
	private final  org.radixware.ads.SelectorAddons.Dlg.common.SelectorAddonsInfoXsd.FiltersListDocument getFiltersInfoForCurrentTable () {
		if (filtersForCurrentTable==null && tableGuid.Value!=null && wasRead()){
		    try{
		       filtersForCurrentTable = remoteCall_getFiltersInfoByTable(tableGuid.Value);
		    }
		    catch(Exceptions::ServiceClientException ex){
		        showException(ex);
		    }
		    catch(Exceptions::InterruptedException ex){
		        
		    }    
		}
		return filtersForCurrentTable;
	}

	/*Radix::SelectorAddons::EasSelectorAddons.Filters:Edit:Model:getCurrentBaseFilterName-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons.Filters:Edit:Model:getCurrentBaseFilterName",line=1656)
	private final  Str getCurrentBaseFilterName () {
		final SelectorAddonsInfoXsd:FilterInfo filterInfo = getCurrentBaseFilterInfo();
		return filterInfo==null ? null : filterInfo.Name;
	}

	/*Radix::SelectorAddons::EasSelectorAddons.Filters:Edit:Model:beforeOpenPropEditorDialog-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons.Filters:Edit:Model:beforeOpenPropEditorDialog",line=1665)
	public published  void beforeOpenPropEditorDialog (org.radixware.kernel.common.client.models.items.properties.Property property, org.radixware.kernel.common.client.models.PropEditorModel propEditorModel) {
		if (property==baseFilterGuid){
		    SelectorAddons.Dlg::ChooseBaseFilterDialog:Model chooseFilterModel = (SelectorAddons.Dlg::ChooseBaseFilterDialog:Model)propEditorModel;    
		    chooseFilterModel.filtersInfo = getFiltersInfoForCurrentTable();
		    chooseFilterModel.tableId = Types::Id.Factory.loadFrom(tableGuid.Value);
		    chooseFilterModel.currentFilterId = baseFilterGuid.Value == null ? null : Types::Id.Factory.loadFrom(baseFilterGuid.Value);
		}
		super.beforeOpenPropEditorDialog(property, propEditorModel);
	}

	/*Radix::SelectorAddons::EasSelectorAddons.Filters:Edit:Model:updateBaseFilterEditorState-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons.Filters:Edit:Model:updateBaseFilterEditorState",line=1678)
	private final  void updateBaseFilterEditorState () {
		final boolean canChooseBaseFilter = tableGuid.Value!=null &&
		                            isTableExists() &&
		                            getFiltersInfoForCurrentTable()!=null &&
		                            getFiltersInfoForCurrentTable().FiltersList.FilterInfoList!=null &&
		                            !getFiltersInfoForCurrentTable().FiltersList.FilterInfoList.isEmpty();
		baseFilterGuid.setReadonly(!canChooseBaseFilter);
		baseFilterGuid.setCanOpenPropEditorDialog(canChooseBaseFilter);
	}

	/*Radix::SelectorAddons::EasSelectorAddons.Filters:Edit:Model:createPropertyEditor-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons.Filters:Edit:Model:createPropertyEditor",line=1692)
	public published  org.radixware.kernel.common.client.widgets.IModelWidget createPropertyEditor (org.radixware.kernel.common.types.Id propertyId) {
		if (idof[EasSelectorAddons.Filters:baseFilterGuid].equals(propertyId)){
		    final Explorer.Widgets::PropStrEditor editor = new PropStrEditor(baseFilterGuid);    
		    if (btnClearBaseFilter==null){
		        btnClearBaseFilter = new com.trolltech.qt.gui.QToolButton(editor);
		        btnClearBaseFilter.setIcon(Explorer.Icons::ExplorerIcon.getQIcon(Explorer.Icons::CommonOperationIcons.CLEAR));
		        btnClearBaseFilter.setToolTip("Clear value");
		        btnClearBaseFilter.setVisible(baseFilterGuid.Value!=null);
		        btnClearBaseFilter.clicked.connect(this,idof[EasSelectorAddons.Filters:Edit:Edit:Model:clearBaseFilter].toString()+"()");            
		    }        
		    editor.addButton(btnClearBaseFilter);
		    return editor;
		}
		else{
		    return super.createPropertyEditor(propertyId);
		}
	}

	/*Radix::SelectorAddons::EasSelectorAddons.Filters:Edit:Model:clearBaseFilter-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons.Filters:Edit:Model:clearBaseFilter",line=1713)
	protected final  void clearBaseFilter () {
		baseFilterGuid.Value = null;
	}

	/*Radix::SelectorAddons::EasSelectorAddons.Filters:Edit:Model:clean-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons.Filters:Edit:Model:clean",line=1721)
	public published  void clean () {
		super.clean();
		btnClearBaseFilter = null;
		conditionEditorWithBaseFilter = null;
		conditionEditorWithoutBaseFilter = null;
		conditionStackedWidget = null;
		filtersForCurrentTable = null;
	}

	/*Radix::SelectorAddons::EasSelectorAddons.Filters:Edit:Model:EditorPageView_opened-Presentation Slot Method*/

	@org.radixware.kernel.common.lang.ReflectiveCallable
	@SuppressWarnings("unused")
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons.Filters:Edit:Model:EditorPageView_opened",line=1735)
	public  void EditorPageView_opened (com.trolltech.qt.gui.QWidget widget) {
		{
		    final com.trolltech.qt.gui.QVBoxLayout layout = new com.trolltech.qt.gui.QVBoxLayout(widget);
		    conditionStackedWidget = new com.trolltech.qt.gui.QStackedWidget();
		    layout.addWidget(conditionStackedWidget);
		}
		{//Создание виджетов, которые будут показаны, когда задан базовый фильтр
		    final Explorer.Views::Splitter splitter = 
		        new Splitter(null, getEnvironment().getConfigStore(), getConfigStoreGroupName(), 1./3.);
		    splitter.setOrientation(com.trolltech.qt.core.Qt.Orientation.Vertical);
		    conditionStackedWidget.addWidget(splitter);
		    {//Виджеты для показа условия базового фильтра
		        final com.trolltech.qt.gui.QGroupBox gbBaseFilterCondition = new com.trolltech.qt.gui.QGroupBox(splitter);
		        gbBaseFilterCondition.setTitle("Base filter condition:");
		        final com.trolltech.qt.gui.QVBoxLayout groupBoxLayout = new com.trolltech.qt.gui.QVBoxLayout(gbBaseFilterCondition);
		        final SelectorAddons.Dlg::BaseFilterConditionEditor conditionEditor = new BaseFilterConditionEditor(this,gbBaseFilterCondition);
		        groupBoxLayout.addWidget(conditionEditor);
		        splitter.addWidget(gbBaseFilterCondition);
		        conditionEditor.bind();
		    }
		    {//Виджеты для редактирования условия общего фильтра
		        final com.trolltech.qt.gui.QGroupBox gbCommonFilterCondition = new com.trolltech.qt.gui.QGroupBox(splitter);
		        gbCommonFilterCondition.setTitle("Common filter condition:");
		        final com.trolltech.qt.gui.QVBoxLayout groupBoxLayout = new com.trolltech.qt.gui.QVBoxLayout(gbCommonFilterCondition);
		        conditionEditorWithBaseFilter = new CommonFilterConditionEditor(this,gbCommonFilterCondition);
		        groupBoxLayout.addWidget(conditionEditorWithBaseFilter);
		        splitter.addWidget(gbCommonFilterCondition);        
		        conditionEditorWithBaseFilter.TextEditor.setFocus();
		    }    
		}
		{//Создание редактора условия, который будет показан, когда базовый фильтр отсутствует:
		    conditionEditorWithoutBaseFilter  = new CommonFilterConditionEditor(this,widget);
		    conditionStackedWidget.addWidget(conditionEditorWithoutBaseFilter);
		    conditionEditorWithoutBaseFilter.TextEditor.setFocus();
		}
		updateConditionPage();
	}

	/*Radix::SelectorAddons::EasSelectorAddons.Filters:Edit:Model:getCurrentBaseFilterDef-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons.Filters:Edit:Model:getCurrentBaseFilterDef",line=1776)
	public final  org.radixware.kernel.common.client.meta.filters.RadFilterDef getCurrentBaseFilterDef () {
		final SelectorAddonsInfoXsd:FilterInfo filterInfo = getCurrentBaseFilterInfo();
		if (filterInfo==null){
		    return null;
		}
		else{
		    final Explorer.Meta::PresentationClassDef classDef = getEnvironment().getDefManager().getClassPresentationDef(filterInfo.OwnerClassId);
		    return classDef.getFilterDefById(filterInfo.Id);
		}
	}

	/*Radix::SelectorAddons::EasSelectorAddons.Filters:Edit:Model:getCurrentBaseFilterInfo-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons.Filters:Edit:Model:getCurrentBaseFilterInfo",line=1790)
	private final  org.radixware.ads.SelectorAddons.Dlg.common.SelectorAddonsInfoXsd.FilterInfo getCurrentBaseFilterInfo () {
		if (baseFilterGuid.Value==null){
		    return null;
		}
		else{
		    final SelectorAddonsInfoXsd:FiltersListDocument filtersListDoc = getFiltersInfoForCurrentTable();
		    if (filtersListDoc!=null && filtersListDoc.FiltersList.FilterInfoList!=null){
		        for (SelectorAddonsInfoXsd:FilterInfo filterInfo: filtersListDoc.FiltersList.FilterInfoList){
		            if (baseFilterGuid.Value.equals(filterInfo.Id.toString())){
		                return filterInfo;
		            }
		        }        
		    }
		    return null;
		}
	}

	/*Radix::SelectorAddons::EasSelectorAddons.Filters:Edit:Model:updateConditionPage-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons.Filters:Edit:Model:updateConditionPage",line=1810)
	protected  void updateConditionPage () {
		final boolean isConditionEnabled = !isNew() || (tableGuid.Value!=null && isTableExists());
		getEditorPage(idof[EasSelectorAddons.Filters:Edit:Condition]).setEnabled(isConditionEnabled);
		if (conditionStackedWidget!=null){
		    if (baseFilterGuid==null || getCurrentBaseFilterDef()==null){
		        conditionEditorWithBaseFilter.unsubscribe();
		        conditionStackedWidget.setCurrentIndex(1);
		        conditionEditorWithoutBaseFilter.bind() ;
		    }
		    else{
		        conditionEditorWithoutBaseFilter.unsubscribe();
		        conditionStackedWidget.setCurrentIndex(0);
		        conditionEditorWithBaseFilter.bind();
		    }
		}
	}

	/*Radix::SelectorAddons::EasSelectorAddons.Filters:Edit:Model:showException-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons.Filters:Edit:Model:showException",line=1831)
	public published  void showException (Str title, java.lang.Throwable ex) {
		if(ex instanceof Exceptions::ServiceCallFault){
		    final Explorer.Exceptions::ModelException modelException = 
		            Explorer.Exceptions::ModelException.create(this,(Exceptions::ServiceCallFault)ex);
		    if (modelException instanceof Explorer.Exceptions::PropertyIsMandatoryException){
		        final Explorer.Exceptions::PropertyIsMandatoryException propIsMandatoryEx = 
		           (Explorer.Exceptions::PropertyIsMandatoryException)modelException;
		        if (propIsMandatoryEx.isOwnProperty() && 
		            idof[EasSelectorAddons.Filters:condition].equals(propIsMandatoryEx.PropertyId)
		            ){
		                processEmtyConditionException(propIsMandatoryEx);
		                return;
		            }        
		    }
		}
		else if (ex instanceof Explorer.Exceptions::PropertyIsMandatoryException){
		    final Explorer.Exceptions::PropertyIsMandatoryException propIsMandatoryEx = 
		       (Explorer.Exceptions::PropertyIsMandatoryException)ex;
		    if (propIsMandatoryEx.isOwnProperty() && 
		        idof[EasSelectorAddons.Filters:condition].equals(propIsMandatoryEx.PropertyId)
		        ){
		            processEmtyConditionException(propIsMandatoryEx);
		            return;
		        }    
		}
		super.showException(title, ex);
	}

	/*Radix::SelectorAddons::EasSelectorAddons.Filters:Edit:Model:getSelectorRowStyle-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons.Filters:Edit:Model:getSelectorRowStyle",line=1863)
	public published  org.radixware.kernel.common.enums.ESelectorRowStyle getSelectorRowStyle () {
		return !isNew() && isConditionDefined.Value==Bool.FALSE ? Explorer.Env::SelectorStyle:Bad : super.getSelectorRowStyle();
	}

	/*Radix::SelectorAddons::EasSelectorAddons.Filters:Edit:Model:processEmtyConditionException-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons.Filters:Edit:Model:processEmtyConditionException",line=1870)
	private final  void processEmtyConditionException (org.radixware.kernel.common.client.exceptions.PropertyIsMandatoryException exception) {
		getEnvironment().messageError(exception.getTitle(getEnvironment().getMessageProvider()),"Filter condition must be defined");
		if (getEditorPage(idof[EasSelectorAddons.Filters:Edit:Condition]).setFocused()){
		    getProperty(idof[EasSelectorAddons.Filters:condition]).setFocused();
		}

	}

	/*Radix::SelectorAddons::EasSelectorAddons.Filters:Edit:Model:onChangeTableGuid-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons.Filters:Edit:Model:onChangeTableGuid",line=1882)
	protected  void onChangeTableGuid () {
		super.onChangeTableGuid();
		filtersForCurrentTable = null;
		baseFilterGuid.Value = null;
		condition.Value = null;
		parameters.Value = null;
		updateBaseFilterEditorState();
	}

	/*Radix::SelectorAddons::EasSelectorAddons.Filters:Edit:Model:remoteCall_getFiltersInfoByTable-Remote Procedure Call Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons.Filters:Edit:Model:remoteCall_getFiltersInfoByTable",line=1894)
	private final  org.radixware.ads.SelectorAddons.Dlg.common.SelectorAddonsInfoXsd.FiltersListDocument remoteCall_getFiltersInfoByTable (Str tableGuid) throws org.radixware.kernel.common.exceptions.ServiceClientException,java.lang.InterruptedException {
		final org.radixware.ads.SelectorAddons.Dlg.explorer.EasSelectorAddons.Filters.RemoteCall_getFiltersInfoByTable _cmd_cmdGBE4YFYAGZB6TD5XGW43D7QYWU_instance_ = (org.radixware.ads.SelectorAddons.Dlg.explorer.EasSelectorAddons.Filters.RemoteCall_getFiltersInfoByTable)getCommand(org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdGBE4YFYAGZB6TD5XGW43D7QYWU"));
		final java.util.List<org.radixware.kernel.common.utils.RPCProcessor.ArgumentInfo> $remote$call$arg$list$store$ = new java.util.LinkedList<org.radixware.kernel.common.utils.RPCProcessor.ArgumentInfo>();
		$remote$call$arg$list$store$.add(new org.radixware.kernel.common.utils.RPCProcessor.ArgumentInfo(org.radixware.kernel.common.enums.EValType.STR,tableGuid));
		final org.radixware.kernel.common.utils.RPCProcessor.ClientSideInvocation $invoker_instance$ = new org.radixware.kernel.common.utils.RPCProcessor.ClientSideInvocation($remote$call$arg$list$store$){
			protected org.radixware.schemas.utils.RPCResponseDocument invokeImpl(org.radixware.schemas.utils.RPCRequestDocument rq) throws org.radixware.kernel.common.exceptions.ServiceClientException, InterruptedException{
				return _cmd_cmdGBE4YFYAGZB6TD5XGW43D7QYWU_instance_.send(rq);
			}
		};
		final Object $rpc$call$result$ = $invoker_instance$.invoke();
		return $rpc$call$result$ == null ? null : $rpc$call$result$ instanceof org.radixware.ads.SelectorAddons.Dlg.common.SelectorAddonsInfoXsd.FiltersListDocument ? (org.radixware.ads.SelectorAddons.Dlg.common.SelectorAddonsInfoXsd.FiltersListDocument) $rpc$call$result$ : (org.radixware.ads.SelectorAddons.Dlg.common.SelectorAddonsInfoXsd.FiltersListDocument) org.radixware.kernel.common.utils.XmlObjectProcessor.castToXmlClass(getClass().getClassLoader(),(org.apache.xmlbeans.XmlObject)$rpc$call$result$,org.radixware.ads.SelectorAddons.Dlg.common.SelectorAddonsInfoXsd.FiltersListDocument.class);

	}

	/*Radix::SelectorAddons::EasSelectorAddons.Filters:Edit:Model:onCommand_export-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons.Filters:Edit:Model:onCommand_export",line=1911)
	private final  void onCommand_export (org.radixware.ads.SelectorAddons.Dlg.explorer.EasSelectorAddons.Filters.Export command) {
		java.io.File file = CfgManagement::DesktopUtils.openFileForExport(findNearestView(), command.getTitle());
		if (file != null) {
		    final GroupSettingsXsd:CustomFiltersDocument document = GroupSettingsXsd:CustomFiltersDocument.Factory.newInstance();
		    final GroupSettingsXsd:CustomFiltersDocument.CustomFilters filters = document.addNewCustomFilters();
		    final GroupSettingsXsd:CustomFilter filterXml = filters.addNewFilter();
		    filterXml.Id = Types::Id.Factory.loadFrom(guid.Value);
		    filterXml.Name = title.Value;
		    filterXml.IsActive = isActive.Value == null ? true : isActive.Value.booleanValue();
		    filterXml.DefinitionType = Meta::DefType:Filter;
		    filterXml.TableId = Types::Id.Factory.loadFrom(tableGuid.Value);
		    filterXml.BaseFilterId = Types::Id.Factory.loadFrom(baseFilterGuid.Value);
		    if (selPresentations.Value != null) {
		        final java.util.List<Types::Id> allowedPresentations = new java.util.LinkedList<Types::Id>();
		        for (Str presentationId : selPresentations.Value) {
		            allowedPresentations.add(Types::Id.Factory.loadFrom(presentationId));
		        }
		        filterXml.SelectorPresentations = allowedPresentations;
		    }
		    try {
		        if (condition.Value != null && !condition.Value.isEmpty())
		            filterXml.Condition = Meta::XscmlXsd:Sqml.Factory.parse(condition.Value);
		        if (parameters.Value != null && !parameters.Value.isEmpty())
		            filterXml.Parameters = GroupSettingsXsd:FilterParameters.Factory.parse(parameters.Value);
		    } catch (Exceptions::XmlException ex) {
		        showException(ex);
		        return;
		    }
		    try {
		        document.save(file);
		    } catch (Exceptions::IOException ex) {
		        showException(ex);
		    }
		}
	}
	public final class Export extends org.radixware.ads.SelectorAddons.Dlg.explorer.EasSelectorAddons.Filters.Export{
		protected Export(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}

	}

	public static class RemoteCall_getFiltersInfoByTable extends org.radixware.ads.SelectorAddons.Dlg.explorer.EasSelectorAddons.Filters.RemoteCall_getFiltersInfoByTable{
		protected RemoteCall_getFiltersInfoByTable(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public org.radixware.schemas.utils.RPCResponseDocument send(org.radixware.schemas.utils.RPCRequestDocument input ) throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,input);
			org.apache.xmlbeans.XmlObject xmlResponse = processResponse(response, null);
			return (org.radixware.schemas.utils.RPCResponseDocument)org.radixware.kernel.common.utils.XmlObjectProcessor.castToXmlClass(getClass().getClassLoader(),xmlResponse,org.radixware.schemas.utils.RPCResponseDocument.class);
		}

	}















}

/* Radix::SelectorAddons::EasSelectorAddons.Filters:Edit:Model - Desktop Meta*/

/*Radix::SelectorAddons::EasSelectorAddons.Filters:Edit:Model-Entity Model Class*/

package org.radixware.ads.SelectorAddons.Dlg.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Edit:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aem43LE34LJRVHNRPIZLU4ONQKJEA"),
						"Edit:Model",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aemFGKCGE2H6FGXDD4YKLBKW77X3U"),
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::SelectorAddons::EasSelectorAddons.Filters:Edit:Model:Properties-Properties*/
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

/* Radix::SelectorAddons::EasSelectorAddons.Filters:Edit:Condition:View - Desktop Executable*/

/*Radix::SelectorAddons::EasSelectorAddons.Filters:Edit:Condition:View-Custom Page Editor for Desktop*/

package org.radixware.ads.SelectorAddons.Dlg.explorer;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons.Filters:Edit:Condition:View")
public class View extends org.radixware.kernel.explorer.views.EditorPageView {
	final private com.trolltech.qt.gui.QFont DEFAULT_FONT = org.radixware.kernel.explorer.text.ExplorerTextOptions.Factory.getDefault().getQFont();
	public View EditorPageView;
	public View getEditorPageView(){ return EditorPageView;}
	public View(org.radixware.kernel.common.client.IClientEnvironment userSession,
	org.radixware.kernel.common.client.views.IView parent, org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef page) {
		super(userSession,(org.radixware.kernel.explorer.views.IExplorerView)parent, page);
	}
	public void open(org.radixware.kernel.common.client.models.Model model) {
		super.open(model);
		EditorPageView = this;
		EditorPageView.setObjectName("EditorPageView");
		EditorPageView.setFont(DEFAULT_FONT);
		EditorPageView.opened.connect(model, "mthKY3L7ZYFPFBJBAXF2EXZJROTIU(com.trolltech.qt.gui.QWidget)");
		opened.emit(this);
	}
	public final org.radixware.ads.SelectorAddons.Dlg.explorer.Edit:Model getModel() {
		return (org.radixware.ads.SelectorAddons.Dlg.explorer.Edit:Model) super.getModel();
	}

}

/* Radix::SelectorAddons::EasSelectorAddons.Filters:Edit:Condition:WebView - Web Executable*/

/*Radix::SelectorAddons::EasSelectorAddons.Filters:Edit:Condition:WebView-Rwt Custom Page Editor*/

package org.radixware.ads.SelectorAddons.Dlg.web;
@SuppressWarnings({"unchecked","rawtypes"})
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons.Filters:Edit:Condition:WebView")
public class WebView extends org.radixware.wps.views.editor.EditorPageView{
	public WebView widget;
	public WebView getWidget(){ return  widget;}
	public WebView(org.radixware.kernel.common.client.IClientEnvironment env,org.radixware.kernel.common.client.views.IView view
	,org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef page){
		super(env,view,page);
	}
	public void open(org.radixware.kernel.common.client.models.Model model){
		super.open(model);
		//============ Radix::SelectorAddons::EasSelectorAddons.Filters:Edit:Condition:WebView:widget ==============
		this.widget = this;
		fireOpened();
	}
}

/* Radix::SelectorAddons::EasSelectorAddons.Filters:Create - Desktop Meta*/

/*Radix::SelectorAddons::EasSelectorAddons.Filters:Create-Editor Presentation*/

package org.radixware.ads.SelectorAddons.Dlg.explorer;
public final class Create_mi{
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new org.radixware.kernel.common.client.meta.RadEditorPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprL2VKQPIHWRA2JIIFBTXMDHADQI"),
	"Create",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
	org.radixware.kernel.common.types.Id.Factory.loadFrom("epr43LE34LJRVHNRPIZLU4ONQKJEA"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("aclLQBIKZWZABEIXKZOG4W3L6I5XI"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("tblHQSOHRWZH5ESLD2Y6IVF6XFBE4"),
	null,
	null,

	/*Radix::SelectorAddons::EasSelectorAddons.Filters:Create:Editor Pages-Editor Presentation Pages*/
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

			/*Radix::SelectorAddons::EasSelectorAddons.Filters:Create:General-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgBP4DOWF2BFGTXAOF7T2V5FRUOM"),"General",org.radixware.kernel.common.types.Id.Factory.loadFrom("aclLQBIKZWZABEIXKZOG4W3L6I5XI"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXRCVKJQPG5DTFNIEF3GUL6XCZE"),null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col6QH5TILJHRDDTC4CHYLQ6HLECA"),0,0,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colZ23Q7UUURBEAHGCRRSQEKZH76I"),0,3,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDCQEQLHEC5F6VO633LHAE6BDMQ"),0,1,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXT2U3TRV6VGAPEULXGUEZMPOAU"),0,2,1,false,false)
			},null)
	},
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgBP4DOWF2BFGTXAOF7T2V5FRUOM")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgNV7ENS7ODJBOXPOJ6O4FPXTCAU")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgZG6UTMVC6ND6TPIYVCGMT3YWIY"))}
	,

	/*Radix::SelectorAddons::EasSelectorAddons.Filters:Create:Children-Explorer Items*/
		null
	,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
	null,
	0,
	null,
	(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
	36016,0,0);
}
/* Radix::SelectorAddons::EasSelectorAddons.Filters:Create:Model - Desktop Executable*/

/*Radix::SelectorAddons::EasSelectorAddons.Filters:Create:Model-Entity Model Class*/

package org.radixware.ads.SelectorAddons.Dlg.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons.Filters:Create:Model")
public class Create:Model  extends org.radixware.ads.SelectorAddons.Dlg.explorer.Edit:Model  {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return Create:Model_mi.rdxMeta; }



	public Create:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadEditorPresentationDef def){super(userSession,def);}

	/*Radix::SelectorAddons::EasSelectorAddons.Filters:Create:Model:Nested classes-Nested Classes*/

	/*Radix::SelectorAddons::EasSelectorAddons.Filters:Create:Model:Properties-Properties*/

	/*Radix::SelectorAddons::EasSelectorAddons.Filters:Create:Model:filtersList-Presentation Property*/




	public class FiltersList extends org.radixware.ads.SelectorAddons.Dlg.explorer.EasSelectorAddons.Filters.EasSelectorAddons.Filters_DefaultModel.eprFGKCGE2H6FGXDD4YKLBKW77X3U_ModelAdapter.prdERYDI62VORCZZAANZ74CCFC3ZA{
		public FiltersList(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Class<org.radixware.ads.SelectorAddons.Dlg.common.SelectorAddonsInfoXsd.FiltersListDocument> getValClass(){
			return org.radixware.ads.SelectorAddons.Dlg.common.SelectorAddonsInfoXsd.FiltersListDocument.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.ads.SelectorAddons.Dlg.common.SelectorAddonsInfoXsd.FiltersListDocument dummy = x == null ? null : (org.radixware.ads.SelectorAddons.Dlg.common.SelectorAddonsInfoXsd.FiltersListDocument)org.radixware.kernel.common.utils.XmlObjectProcessor.castToXmlClass(getClass().getClassLoader(),(org.apache.xmlbeans.XmlObject)x,org.radixware.ads.SelectorAddons.Dlg.common.SelectorAddonsInfoXsd.FiltersListDocument.class);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons.Filters:Create:Model:filtersList",line=2156)
		public  org.radixware.ads.SelectorAddons.Dlg.common.SelectorAddonsInfoXsd.FiltersListDocument getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons.Filters:Create:Model:filtersList",line=2162)
		public   void setValue(org.radixware.ads.SelectorAddons.Dlg.common.SelectorAddonsInfoXsd.FiltersListDocument val) {
			Value = val;
		}
	}
	public FiltersList getFiltersList(){return (FiltersList)getProperty(prdERYDI62VORCZZAANZ74CCFC3ZA);}








	/*Radix::SelectorAddons::EasSelectorAddons.Filters:Create:Model:Methods-Methods*/

	/*Radix::SelectorAddons::EasSelectorAddons.Filters:Create:Model:afterPrepareCreate-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons.Filters:Create:Model:afterPrepareCreate",line=2181)
	protected published  boolean afterPrepareCreate () {
		tableGuid.setReadonly(getSrcPid()!=null);
		title.Value = "New Filter";
		return super.afterPrepareCreate();
	}

	/*Radix::SelectorAddons::EasSelectorAddons.Filters:Create:Model:onChangeTableGuid-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons.Filters:Create:Model:onChangeTableGuid",line=2191)
	protected  void onChangeTableGuid () {
		filtersList.setValueObject(null);
		super.onChangeTableGuid();
		updateConditionPage();
	}


}

/* Radix::SelectorAddons::EasSelectorAddons.Filters:Create:Model - Desktop Meta*/

/*Radix::SelectorAddons::EasSelectorAddons.Filters:Create:Model-Entity Model Class*/

package org.radixware.ads.SelectorAddons.Dlg.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Create:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aemL2VKQPIHWRA2JIIFBTXMDHADQI"),
						"Create:Model",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aem43LE34LJRVHNRPIZLU4ONQKJEA"),
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::SelectorAddons::EasSelectorAddons.Filters:Create:Model:Properties-Properties*/
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

/* Radix::SelectorAddons::EasSelectorAddons.Filters:General - Desktop Meta*/

/*Radix::SelectorAddons::EasSelectorAddons.Filters:General-Selector Presentation*/

package org.radixware.ads.SelectorAddons.Dlg.explorer;
public final class General_mi extends org.radixware.kernel.common.client.meta.RadSelectorPresentationDef{
	public static final org.radixware.kernel.common.client.meta.RadSelectorPresentationDef rdxMeta = new General_mi();
	private General_mi(){
		super(org.radixware.kernel.common.types.Id.Factory.loadFrom("sprQWY42P52BFF3JGZLM2EPVKHC7U"),
		"General",
		org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("sprIIEIWQINNRCG5OAYT2TYA6NNE4"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("aclLQBIKZWZABEIXKZOG4W3L6I5XI"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("tblHQSOHRWZH5ESLD2Y6IVF6XFBE4"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCTBPEOXLOBAT5EA5ZJFNJ35GIY"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("imgJAFMGUWJY5H5PFIPGPERL5ESMA"),
		null,
		null,
		false,
		null,
		null,
		false,
		false,
		null,
		0,null,
		42,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprL2VKQPIHWRA2JIIFBTXMDHADQI")},
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr43LE34LJRVHNRPIZLU4ONQKJEA")},
		false,true,false,0,0);
		columns = new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn[]{

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdJWOOWTAQA5EPHJHYBAYNLMEKAU"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdTWUGN4HMCFAQXE4GYZFLHUJXNI"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colTOTOEP36JFH35MA7BJYRU67GKU"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDCQEQLHEC5F6VO633LHAE6BDMQ"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col6QH5TILJHRDDTC4CHYLQ6HLECA"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colZ23Q7UUURBEAHGCRRSQEKZH76I"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null)
		};
	}
}
/* Radix::SelectorAddons::EasSelectorAddons.Filters:General:Model - Desktop Executable*/

/*Radix::SelectorAddons::EasSelectorAddons.Filters:General:Model-Group Model Class*/

package org.radixware.ads.SelectorAddons.Dlg.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons.Filters:General:Model")
public class General:Model  extends org.radixware.ads.SelectorAddons.Dlg.explorer.EasSelectorAddons.Filters.DefaultGroupModel {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return General:Model_mi.rdxMeta; }



	public General:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadSelectorPresentationDef def){super(userSession,def);}

	/*Radix::SelectorAddons::EasSelectorAddons.Filters:General:Model:Nested classes-Nested Classes*/

	/*Radix::SelectorAddons::EasSelectorAddons.Filters:General:Model:Properties-Properties*/

	/*Radix::SelectorAddons::EasSelectorAddons.Filters:General:Model:exportCmdInput-Dynamic Property*/



	protected org.radixware.ads.SelectorAddons.Dlg.common.SelectorAddonsImpExpXsd.ExportParametersDocument exportCmdInput=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons.Filters:General:Model:exportCmdInput",line=2312)
	private final  org.radixware.ads.SelectorAddons.Dlg.common.SelectorAddonsImpExpXsd.ExportParametersDocument getExportCmdInput() {
		return exportCmdInput;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons.Filters:General:Model:exportCmdInput",line=2318)
	private final   void setExportCmdInput(org.radixware.ads.SelectorAddons.Dlg.common.SelectorAddonsImpExpXsd.ExportParametersDocument val) {
		exportCmdInput = val;
	}






	/*Radix::SelectorAddons::EasSelectorAddons.Filters:General:Model:Methods-Methods*/

	/*Radix::SelectorAddons::EasSelectorAddons.Filters:General:Model:export-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons.Filters:General:Model:export",line=2332)
	public  void export (Str tableGuid) throws java.lang.InterruptedException,org.radixware.kernel.common.exceptions.ServiceClientException {
		final SelectorAddonsImpExpXsd:ExportParametersDocument input = SelectorAddonsImpExpXsd:ExportParametersDocument.Factory.newInstance();
		final SelectorAddonsImpExpXsd:ExportParametersDocument.ExportParameters parameters = input.addNewExportParameters();
		parameters.TableGuid = tableGuid;
		exportCmdInput = input;
		getCommand(idof[EasSelectorAddonsGroup:exportFilters]).execute();
	}

	/*Radix::SelectorAddons::EasSelectorAddons.Filters:General:Model:onCommand_exportFilters-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons.Filters:General:Model:onCommand_exportFilters",line=2343)
	public  void onCommand_exportFilters (org.radixware.ads.SelectorAddons.Dlg.explorer.EasSelectorAddonsGroup.ExportFilters command) {
		try{
		    command.send(exportCmdInput);
		}catch(Exceptions::InterruptedException  e){
		        showException(e);
		}catch(Exceptions::ServiceClientException  e){
		        showException(e);
		}
	}
	public final class ExportFilters extends org.radixware.ads.SelectorAddons.Dlg.explorer.EasSelectorAddonsGroup.ExportFilters{
		protected ExportFilters(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_exportFilters( this );
		}

	}













}

/* Radix::SelectorAddons::EasSelectorAddons.Filters:General:Model - Desktop Meta*/

/*Radix::SelectorAddons::EasSelectorAddons.Filters:General:Model-Group Model Class*/

package org.radixware.ads.SelectorAddons.Dlg.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class General:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("agmQWY42P52BFF3JGZLM2EPVKHC7U"),
						"General:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::SelectorAddons::EasSelectorAddons.Filters:General:Model:Properties-Properties*/
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

/* Radix::SelectorAddons::EasSelectorAddons.Filters - Localizing Bundle */
package org.radixware.ads.SelectorAddons.Dlg.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class EasSelectorAddons.Filters - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Base filter condition:");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Условие базового фильтра:");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2UTGLWJXOFG6VFBMN657GYT4C4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"C:\\distrib\\trunk\\org.radixware\\ads\\SelectorAddons"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Filter Condition");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Условие фильтра");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3NRNA2EONJEK3JZ6EWK3PWFVAI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"C:\\distrib\\trunk\\org.radixware\\ads\\SelectorAddons"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Base filter #%s not found");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Не найден базовый фильтр #%s");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4X3PTMJNLFHQJGKSM7RQEDP434"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"C:\\distrib\\trunk\\org.radixware\\ads\\SelectorAddons"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Filter condition");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Условие фильтра");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBDMIM6YYTNGBJM5NSQOYK77IFE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"C:\\distrib\\trunk\\org.radixware\\ads\\SelectorAddons"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Cannot parse filter condition: %s");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Невозможно прочитать условие фильтра: %s");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCDDVVS24HVHEZOAQ3QWUVKG6JM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"C:\\distrib\\trunk\\org.radixware\\ads\\SelectorAddons"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Common Filters");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Общие фильтры");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCTBPEOXLOBAT5EA5ZJFNJ35GIY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"C:\\distrib\\trunk\\org.radixware\\ads\\SelectorAddons"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"New Filter");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Новый фильтр");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFKY2WOKKUZBFHJZUYU2T6SZCRA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"C:\\distrib\\trunk\\org.radixware\\ads\\SelectorAddons"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Cannot parse filter condition: %s");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Невозможно прочитать условие фильтра: %s");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGO6MFOVIZFE2DOQ7W2X56HB5IM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"C:\\distrib\\trunk\\org.radixware\\ads\\SelectorAddons"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"General");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Общее");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNOALWTLFZRAOVFCOXEYSGLAE5Q"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"C:\\distrib\\trunk\\org.radixware\\ads\\SelectorAddons"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Base filter");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Базовый фильтр");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRYC5EYTBHBCPPMTI5OEFHN2IC4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"C:\\distrib\\trunk\\org.radixware\\ads\\SelectorAddons"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Common Filter");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Общий фильтр");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsSUYUAJP7WZC5XBCTKRQWEALHEE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"C:\\distrib\\trunk\\org.radixware\\ads\\SelectorAddons"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Export Filter");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Экспортировать фильтр");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsSWAJ2ZYPSVDU7HFG2WK23535FU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"C:\\distrib\\trunk\\org.radixware\\ads\\SelectorAddons"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Common filter condition:");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Условие общего фильтра:");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTM2YYNQOLZCYDEMZNRALU7ZSTI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"C:\\distrib\\trunk\\org.radixware\\ads\\SelectorAddons"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Filter condition must be defined");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Должно быть задано условие фильтра");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTRXI3RIW6ZDL3DXGT3WACAJN6Q"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"C:\\distrib\\trunk\\org.radixware\\ads\\SelectorAddons"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Cannot parse filter condition: %s");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Невозможно прочитать условие фильтра: %s");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsU6HQDT2I5BENDED7FTDCK2BLUY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"C:\\distrib\\trunk\\org.radixware\\ads\\SelectorAddons"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Clear value");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Очистить значение");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVHAQNRAHHFFRPKFZ5OS22AV4GQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"C:\\distrib\\trunk\\org.radixware\\ads\\SelectorAddons"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"General");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Общее");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXRCVKJQPG5DTFNIEF3GUL6XCZE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"C:\\distrib\\trunk\\org.radixware\\ads\\SelectorAddons"));
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(EasSelectorAddons.Filters - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbaclLQBIKZWZABEIXKZOG4W3L6I5XI"),"EasSelectorAddons.Filters - Localizing Bundle",$$$items$$$);
}
