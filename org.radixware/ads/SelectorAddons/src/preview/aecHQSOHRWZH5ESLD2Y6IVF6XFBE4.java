
/* Radix::SelectorAddons::EasSelectorAddons - Server Executable*/

/*Radix::SelectorAddons::EasSelectorAddons-Entity Class*/

package org.radixware.ads.SelectorAddons.Dlg.server;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons")
public abstract published class EasSelectorAddons  extends org.radixware.ads.Types.server.Entity  implements org.radixware.ads.CfgManagement.server.ICfgReferencedObject,org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {


	/**Metainformation accessor method*/
	@SuppressWarnings("unused")
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return EasSelectorAddons_mi.rdxMeta;}

	/*Radix::SelectorAddons::EasSelectorAddons:Nested classes-Nested Classes*/

	/*Radix::SelectorAddons::EasSelectorAddons:Properties-Properties*/

	/*Radix::SelectorAddons::EasSelectorAddons:classGuid-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons:classGuid")
	public  Str getClassGuid() {
		return classGuid;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons:classGuid")
	public   void setClassGuid(Str val) {
		classGuid = val;
	}

	/*Radix::SelectorAddons::EasSelectorAddons:guid-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons:guid")
	public  Str getGuid() {
		return guid;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons:guid")
	public   void setGuid(Str val) {
		guid = val;
	}

	/*Radix::SelectorAddons::EasSelectorAddons:isActive-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons:isActive")
	public  Bool getIsActive() {
		return isActive;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons:isActive")
	public   void setIsActive(Bool val) {
		isActive = val;
	}

	/*Radix::SelectorAddons::EasSelectorAddons:lastUpdateTime-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons:lastUpdateTime")
	public  java.sql.Timestamp getLastUpdateTime() {
		return lastUpdateTime;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons:lastUpdateTime")
	public   void setLastUpdateTime(java.sql.Timestamp val) {
		lastUpdateTime = val;
	}

	/*Radix::SelectorAddons::EasSelectorAddons:lastUpdateUser-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons:lastUpdateUser")
	public  Str getLastUpdateUser() {
		return lastUpdateUser;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons:lastUpdateUser")
	public   void setLastUpdateUser(Str val) {
		lastUpdateUser = val;
	}

	/*Radix::SelectorAddons::EasSelectorAddons:selPresentations-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons:selPresentations")
	public  org.radixware.kernel.common.types.ArrStr getSelPresentations() {
		return selPresentations;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons:selPresentations")
	public   void setSelPresentations(org.radixware.kernel.common.types.ArrStr val) {
		selPresentations = val;
	}

	/*Radix::SelectorAddons::EasSelectorAddons:seq-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons:seq")
	public  Int getSeq() {
		return seq;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons:seq")
	public   void setSeq(Int val) {
		seq = val;
	}

	/*Radix::SelectorAddons::EasSelectorAddons:tableGuid-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons:tableGuid")
	public  Str getTableGuid() {
		return tableGuid;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons:tableGuid")
	public   void setTableGuid(Str val) {
		tableGuid = val;
	}

	/*Radix::SelectorAddons::EasSelectorAddons:title-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons:title")
	public  Str getTitle() {
		return title;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons:title")
	public   void setTitle(Str val) {
		title = val;
	}

	/*Radix::SelectorAddons::EasSelectorAddons:isValid-Dynamic Property*/



	protected Bool isValid=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons:isValid")
	public  Bool getIsValid() {

		return isValid();
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons:isValid")
	public   void setIsValid(Bool val) {
		isValid = val;
	}

	/*Radix::SelectorAddons::EasSelectorAddons:selectorPresentationsList-Dynamic Property*/



	protected org.radixware.ads.SelectorAddons.Dlg.common.SelectorAddonsInfoXsd.SelectorPresentationsListDocument selectorPresentationsList=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons:selectorPresentationsList")
	public  org.radixware.ads.SelectorAddons.Dlg.common.SelectorAddonsInfoXsd.SelectorPresentationsListDocument getSelectorPresentationsList() {

		if (internal[selectorPresentationsList]==null && tableGuid!=null && isTableExists()){
		    internal[selectorPresentationsList] = getSelectorPresentationsByTable(tableGuid);
		}
		return internal[selectorPresentationsList];
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons:selectorPresentationsList")
	public   void setSelectorPresentationsList(org.radixware.ads.SelectorAddons.Dlg.common.SelectorAddonsInfoXsd.SelectorPresentationsListDocument val) {
		selectorPresentationsList = val;
	}

	/*Radix::SelectorAddons::EasSelectorAddons:tableName-Dynamic Property*/



	protected Str tableName=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons:tableName")
	public  Str getTableName() {

		if(internal[tableName]==null && tableGuid!=null && isTableExists()){
		    Types::Id[] ids=new Types::Id[]{Types::Id.Factory.loadFrom(tableGuid)};
		    org.radixware.kernel.common.defs.dds.DdsDefinition def=getArte().DefManager.getDdsDef(ids);
		    internal[tableName]=def.Name;
		}
		return internal[tableName];
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons:tableName")
	public   void setTableName(Str val) {
		tableName = val;
	}





































































































	/*Radix::SelectorAddons::EasSelectorAddons:Methods-Methods*/

	/*Radix::SelectorAddons::EasSelectorAddons:beforeUpdate-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons:beforeUpdate")
	protected published  boolean beforeUpdate () {
		onChange();
		return super.beforeUpdate();
	}

	/*Radix::SelectorAddons::EasSelectorAddons:beforeCreate-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons:beforeCreate")
	protected published  boolean beforeCreate (org.radixware.kernel.server.types.Entity src) {
		seq = getNextSeq();
		onChange();
		return super.beforeCreate(src);
	}

	/*Radix::SelectorAddons::EasSelectorAddons:onChange-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons:onChange")
	private final  void onChange () {
		checkProperties();
		final DateTime currentTime = Arte::Arte.getCurrentTime();
		final String userName = Arte::Arte.getUserName();
		updateLastModifyTime(currentTime,userName);
		lastUpdateTime = currentTime;
		lastUpdateUser = userName;
	}

	/*Radix::SelectorAddons::EasSelectorAddons:getNextSeq-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons:getNextSeq")
	private final  Int getNextSeq () {
		final SelectorAddons.Db::GetMaxAddonSeqCursor cur = SelectorAddons.Db::GetMaxAddonSeqCursor.open(tableGuid, classGuid);
		try{
		  return Int.valueOf(cur.next() && cur.seq!=null ? cur.seq.intValue()+1 : 1);
		}finally{
		  cur.close();
		}
	}

	/*Radix::SelectorAddons::EasSelectorAddons:checkProperties-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons:checkProperties")
	protected  void checkProperties () {
		if (tableGuid!=null && !isTableExists())
		    throw new InvalidPropertyValueError(idof[EasSelectorAddons],idof[EasSelectorAddons:tableGuid],String.format("Table #%s not found",tableGuid));
	}

	/*Radix::SelectorAddons::EasSelectorAddons:isTableExists-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons:isTableExists")
	protected final  boolean isTableExists () {
		try{
		    Arte::Arte.getDefManager().getTableDef(Types::Id.Factory.loadFrom(tableGuid));    
		    return true;
		}
		catch(Exceptions::DefinitionError error){
		    return false;
		}
	}

	/*Radix::SelectorAddons::EasSelectorAddons:isValid-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons:isValid")
	protected  boolean isValid () {
		if (!this.isInDatabase(false))
		    return true;
		return tableGuid!=null && isTableExists();
	}

	/*Radix::SelectorAddons::EasSelectorAddons:getSelectorPresentationsByTable-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons:getSelectorPresentationsByTable")
	private final  org.radixware.ads.SelectorAddons.Dlg.common.SelectorAddonsInfoXsd.SelectorPresentationsListDocument getSelectorPresentationsByTable (Str tableGuid) {
		final Types::Id tableId = Types::Id.Factory.loadFrom(tableGuid);
		SelectorAddonsInfoXsd:SelectorPresentationsListDocument document = 
		    SelectorAddonsInfoXsd:SelectorPresentationsListDocument.Factory.newInstance();
		final SelectorAddonsInfoXsd:SelectorPresentationsListDocument.SelectorPresentationsList presentations = 
		    document.addNewSelectorPresentationsList();
		final java.util.Collection<Meta::ClassDef> classes;
		try{
		    classes = getArte().DefManager.getAllClassDefsBasedOnTableByTabId(tableId);
		}
		catch(Exceptions::DefinitionError err){
		    return document;
		}
		final java.util.Collection<Types::Id> presentationIds = new java.util.LinkedList<Types::Id>();
		for (Meta::ClassDef classDef: classes){
		    for(Meta::SelectorPresentationDef presentation: classDef.Presentation.getSelectorPresentations()){
		        if (!presentationIds.contains(presentation.getId())){            
		            presentationIds.add(presentation.getId());
		            final Meta::ClassDef ownerClass = presentation.ClassPresentation.getClassDef();
		            final SelectorAddonsInfoXsd:SelectorPresentationInfo presInfo = presentations.addNewSelectorPresentationInfo();
		            presInfo.Id = presentation.getId();
		            presInfo.TableId = tableId;
		            presInfo.OwnerClassId = ownerClass.getId();
		            presInfo.OwnerClassName = ownerClass.Name;
		            presInfo.Name = presentation.Name;
		            presInfo.FullName = presentation.QualifiedName;
		        }
		    }   
		}
		return document;
	}

	/*Radix::SelectorAddons::EasSelectorAddons:onCommand_moveDown-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons:onCommand_moveDown")
	private final  void onCommand_moveDown (org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValHandlersById) {
		move(false);
	}

	/*Radix::SelectorAddons::EasSelectorAddons:onCommand_moveUp-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons:onCommand_moveUp")
	private final  void onCommand_moveUp (org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValHandlersById) {
		move(true);
	}

	/*Radix::SelectorAddons::EasSelectorAddons:move-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons:move")
	private final  void move (boolean moveUp) {
		final SelectorAddons.Db::FindNeighboringAddonCursor cursor = SelectorAddons.Db::FindNeighboringAddonCursor.open(tableGuid, classGuid, seq, Bool.valueOf(moveUp));
		final Str neighborGuid;
		try{
		    neighborGuid = cursor.next() ? cursor.guid : null;
		}
		catch(Exceptions::DatabaseError error){
		    Arte::Trace.debug(Arte::Trace.exceptionStackToString(error),Arte::EventSource:App);
		    throw new AppError("Unable to change the setting procedure");
		}
		finally{
		    cursor.close();       
		}

		final Str curUser = Arte::Arte.getUserName();
		if (neighborGuid!=null){
		    SelectorAddons.Db::UpdateSeqFieldStmt.execute(guid, Long.valueOf(moveUp ? seq.longValue()-1 : seq.longValue()+1), curUser, Arte::Arte.getCurrentTime());
		    SelectorAddons.Db::UpdateSeqFieldStmt.execute(neighborGuid, seq,  curUser, Arte::Arte.getCurrentTime());
		    updateLastModifyTime(Arte::Arte.getCurrentTime(), Arte::Arte.getUserName());
		}
	}

	/*Radix::SelectorAddons::EasSelectorAddons:beforeDelete-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons:beforeDelete")
	protected published  boolean beforeDelete () {
		if (super.beforeDelete()){
		    final DateTime currentTime = Arte::Arte.getCurrentTime();
		    final String userName = Arte::Arte.getUserName();
		    SelectorAddons.Db::DecreaseSeqFieldStmt.execute(tableGuid, classGuid, seq, currentTime, userName);
		    updateLastModifyTime(currentTime, userName);
		    return true;
		}
		return false;
	}

	/*Radix::SelectorAddons::EasSelectorAddons:updateLastModifyTime-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons:updateLastModifyTime")
	private final  void updateLastModifyTime (java.sql.Timestamp time, Str userName) {
		SelectorAddons.Db::UpdateLastModifyTimeStmt.execute("LAST_MODIFY_TIME", time, userName);
	}

	/*Radix::SelectorAddons::EasSelectorAddons:getRestrictedTableIds-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons:getRestrictedTableIds")
	public  org.radixware.kernel.common.types.ArrStr getRestrictedTableIds () {
		if(!isOneForTable()){
		    return new ArrStr();
		}

		ArrStr result = new ArrStr();
		try {
		    SelectorAddons.Db::EasSelectorAddonsUsedTableCursor cursor = SelectorAddons.Db::EasSelectorAddonsUsedTableCursor.open(classGuid);
		    try {
		        while (cursor.next()) {
		            result.add(cursor.tableGuid);
		        }
		    } finally {
		        cursor.close();
		    }
		} catch (Exceptions::DatabaseError e) {
		    Arte::Trace.put(Arte::EventSeverity:Error,Utils::ExceptionTextFormatter.exceptionStackToString(e),Arte::EventSource:App);
		}
		return result;

	}

	/*Radix::SelectorAddons::EasSelectorAddons:isOneForTable-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons:isOneForTable")
	public  boolean isOneForTable () {
		return false;
	}

	/*Radix::SelectorAddons::EasSelectorAddons:getCfgReferenceExtGuid-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons:getCfgReferenceExtGuid")
	public published  Str getCfgReferenceExtGuid () {
		return guid;
	}

	/*Radix::SelectorAddons::EasSelectorAddons:getCfgReferencePropId-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons:getCfgReferencePropId")
	public published  org.radixware.kernel.common.types.Id getCfgReferencePropId () {
		return idof[EasSelectorAddons:guid];
	}






	@Override
	public org.radixware.kernel.server.types.FormHandler.NextDialogsRequest execCommand(org.radixware.kernel.common.types.Id cmdId, org.radixware.kernel.common.types.Id propId, org.apache.xmlbeans.XmlObject input, org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValsById, org.apache.xmlbeans.XmlObject output) throws org.radixware.kernel.common.exceptions.AppException,java.lang.InterruptedException{
		if(cmdId == cmd3NEA3JPZHBAYFHHYY73PDK57JE){
			final org.radixware.schemas.utils.RPCResponseDocument result =  rpc_callcmd3NEA3JPZHBAYFHHYY73PDK57JE_implementation((org.radixware.schemas.utils.RPCRequestDocument)org.radixware.kernel.common.utils.XmlObjectProcessor.getAppCommandInputData(getClass().getClassLoader(),input,org.radixware.schemas.utils.RPCRequestDocument.class));
			if(result != null)
				output.set(result);
			return null;
		} else if(cmdId == cmdF2IFUCZIRFCVZBS3PIE33CSZPE){
			onCommand_moveDown(newPropValsById);
			return null;
		} else if(cmdId == cmdLYPWZD46EBAFBFEOHLITMHP4LY){
			final org.radixware.schemas.utils.RPCResponseDocument result =  rpc_callcmdLYPWZD46EBAFBFEOHLITMHP4LY_implementation((org.radixware.schemas.utils.RPCRequestDocument)org.radixware.kernel.common.utils.XmlObjectProcessor.getAppCommandInputData(getClass().getClassLoader(),input,org.radixware.schemas.utils.RPCRequestDocument.class));
			if(result != null)
				output.set(result);
			return null;
		} else if(cmdId == cmdV47MKSYRSBD6ZN7RIDULX4WFSA){
			onCommand_moveUp(newPropValsById);
			return null;
		} else 
			return super.execCommand(cmdId,propId,input,newPropValsById,output);
	}
	private final org.radixware.schemas.utils.RPCResponseDocument rpc_callcmd3NEA3JPZHBAYFHHYY73PDK57JE_implementation(org.radixware.schemas.utils.RPCRequestDocument input){
		final org.radixware.kernel.common.utils.RPCProcessor.ServerSideInvocation invoker = new org.radixware.kernel.common.utils.RPCProcessor.ServerSideInvocation(input){
			@Override
			protected  org.radixware.kernel.common.enums.EValType getReturnType(){
				return org.radixware.kernel.common.enums.EValType.XML;
			}
			protected  Object invokeImpl(Object[] arguments){
				final Str p0 = arguments[0] == null ? null : (Str)arguments[0];
				Object $res$ =org.radixware.ads.SelectorAddons.Dlg.server.EasSelectorAddons.this.mth4T2RD5IRXBDMVJVLBVX52DOGY4(p0);
				return $res$;
			}
		};
		return invoker.invoke();
	}
	private final org.radixware.schemas.utils.RPCResponseDocument rpc_callcmdLYPWZD46EBAFBFEOHLITMHP4LY_implementation(org.radixware.schemas.utils.RPCRequestDocument input){
		final org.radixware.kernel.common.utils.RPCProcessor.ServerSideInvocation invoker = new org.radixware.kernel.common.utils.RPCProcessor.ServerSideInvocation(input){
			@Override
			protected  org.radixware.kernel.common.enums.EValType getReturnType(){
				return org.radixware.kernel.common.enums.EValType.ARR_STR;
			}
			protected  Object invokeImpl(Object[] arguments){
				Object $res$ =org.radixware.ads.SelectorAddons.Dlg.server.EasSelectorAddons.this.mthV2GVSJ7OVFG3RAWR5FH7NTQBFQ();
				return $res$;
			}
		};
		return invoker.invoke();
	}


}

/* Radix::SelectorAddons::EasSelectorAddons - Server Meta*/

/*Radix::SelectorAddons::EasSelectorAddons-Entity Class*/

package org.radixware.ads.SelectorAddons.Dlg.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class EasSelectorAddons_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHQSOHRWZH5ESLD2Y6IVF6XFBE4"),"EasSelectorAddons",null,

						org.radixware.kernel.common.enums.EClassType.ENTITY,

						/*Radix::SelectorAddons::EasSelectorAddons:Presentations-Entity Presentations*/
						/*Class presentation attributes*/
						 new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef(/*Owner Class Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHQSOHRWZH5ESLD2Y6IVF6XFBE4"),
							/*Owner Class Name*/
							"EasSelectorAddons",
							/*Title Id*/
							null,
							/*Property presentations*/

							/*Radix::SelectorAddons::EasSelectorAddons:Properties-Properties*/
							new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef[]{

									/*Radix::SelectorAddons::EasSelectorAddons:guid:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colTOTOEP36JFH35MA7BJYRU67GKU"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::SelectorAddons::EasSelectorAddons:isActive:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colZ23Q7UUURBEAHGCRRSQEKZH76I"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::SelectorAddons::EasSelectorAddons:lastUpdateTime:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colTZ42HZBXFFBPVL6P4MTIMOAHQE"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::SelectorAddons::EasSelectorAddons:lastUpdateUser:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colVTTB5PHSCFG5TAUCLOI27MO5XM"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::SelectorAddons::EasSelectorAddons:selPresentations:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colLR66PCM6LJGRNJMVM3ROBROYOY"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::SelectorAddons::EasSelectorAddons:tableGuid:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDCQEQLHEC5F6VO633LHAE6BDMQ"),org.radixware.kernel.common.enums.EEditPossibility.ON_CREATE,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::SelectorAddons::EasSelectorAddons:title:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col6QH5TILJHRDDTC4CHYLQ6HLECA"),org.radixware.kernel.common.enums.EEditPossibility.ONLY_IN_EDITOR,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::SelectorAddons::EasSelectorAddons:isValid:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdJWOOWTAQA5EPHJHYBAYNLMEKAU"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::SelectorAddons::EasSelectorAddons:selectorPresentationsList:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdU5D4C52EMBHWPGMWKFMJ7ZUW6Q"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::SelectorAddons::EasSelectorAddons:tableName:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdR2V6YU635RAWVK2EA2HXJIY4Q4"),org.radixware.kernel.common.enums.EEditPossibility.PROGRAMMATICALLY,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT))
							},
							/*Commands*/
							new org.radixware.kernel.server.meta.presentations.RadCommandDef[]{

									new org.radixware.kernel.server.meta.presentations.RadCommandDef(
									/*Radix::SelectorAddons::EasSelectorAddons:remoteCall_getSelectorPresentationsByTable-Remote Call Command*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("cmd3NEA3JPZHBAYFHHYY73PDK57JE"),"remoteCall_getSelectorPresentationsByTable",org.radixware.kernel.common.enums.ECommandScope.OBJECT,null,org.radixware.kernel.common.enums.ECommandAccessibility.ALWAYS,org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHQSOHRWZH5ESLD2Y6IVF6XFBE4"),false,true),

									new org.radixware.kernel.server.meta.presentations.RadCommandDef(
									/*Radix::SelectorAddons::EasSelectorAddons:moveUp-Object Command*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdV47MKSYRSBD6ZN7RIDULX4WFSA"),"moveUp",org.radixware.kernel.common.enums.ECommandScope.OBJECT,null,org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_FIXED,org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHQSOHRWZH5ESLD2Y6IVF6XFBE4"),false,true),

									new org.radixware.kernel.server.meta.presentations.RadCommandDef(
									/*Radix::SelectorAddons::EasSelectorAddons:moveDown-Object Command*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdF2IFUCZIRFCVZBS3PIE33CSZPE"),"moveDown",org.radixware.kernel.common.enums.ECommandScope.OBJECT,null,org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_FIXED,org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHQSOHRWZH5ESLD2Y6IVF6XFBE4"),false,true),

									new org.radixware.kernel.server.meta.presentations.RadCommandDef(
									/*Radix::SelectorAddons::EasSelectorAddons:remoteCall_getUsedTableIds-Remote Call Command*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdLYPWZD46EBAFBFEOHLITMHP4LY"),"remoteCall_getUsedTableIds",org.radixware.kernel.common.enums.ECommandScope.OBJECT,null,org.radixware.kernel.common.enums.ECommandAccessibility.ALWAYS,org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHQSOHRWZH5ESLD2Y6IVF6XFBE4"),false,true)
							},
							/*Sortings*/
							new org.radixware.kernel.server.meta.presentations.RadSortingDef[]{

									new org.radixware.kernel.server.meta.presentations.RadSortingDef(
									/*Radix::SelectorAddons::EasSelectorAddons:bySeq-Sorting*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("srtSN4J3KY4TNHE5IEJ6CCAX4WI3Y"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHQSOHRWZH5ESLD2Y6IVF6XFBE4"),"bySeq",null,new org.radixware.kernel.server.meta.presentations.RadSortingDef.Item[]{

											new org.radixware.kernel.server.meta.presentations.RadSortingDef.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDCQEQLHEC5F6VO633LHAE6BDMQ"),org.radixware.kernel.common.enums.EOrder.ASC),

											new org.radixware.kernel.server.meta.presentations.RadSortingDef.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("colYNFESMLQLBCGDCKG7FGDPYNPR4"),org.radixware.kernel.common.enums.EOrder.ASC)
									},"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware")
							},
							/*Filters*/
							null,
							/*Editor presentations*/
							new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef[]{

									new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef(
									/*Radix::SelectorAddons::EasSelectorAddons:AbstractBase-Editor Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprFGKCGE2H6FGXDD4YKLBKW77X3U"),
									"AbstractBase",
									org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
									null,
									144,

									/*Radix::SelectorAddons::EasSelectorAddons:AbstractBase:TitleFormat-Object Title Format*/

									new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHQSOHRWZH5ESLD2Y6IVF6XFBE4"),new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.TitleItem[]{

											new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.TitleItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHQSOHRWZH5ESLD2Y6IVF6XFBE4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("col6QH5TILJHRDDTC4CHYLQ6HLECA"),"{0}",org.radixware.kernel.common.enums.ETitleNullFormat.SAME_AS_NOT_NULL,null)
									},null,org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.DefinitionContextType.EDITOR_PRESENTATION),

									/*Radix::SelectorAddons::EasSelectorAddons:AbstractBase:Children-Explorer Items*/
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
									/*Radix::SelectorAddons::EasSelectorAddons:AbstractBase-Selector Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("sprIIEIWQINNRCG5OAYT2TYA6NNE4"),"AbstractBase",null,144,new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn[]{

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col6QH5TILJHRDDTC4CHYLQ6HLECA"),true)
									},new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.Addons(org.radixware.kernel.common.types.Id.Factory.loadFrom("srtSN4J3KY4TNHE5IEJ6CCAX4WI3Y"),false,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("srtSN4J3KY4TNHE5IEJ6CCAX4WI3Y")},false,null,false,new org.radixware.kernel.common.types.Id[]{},false,false,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprFGKCGE2H6FGXDD4YKLBKW77X3U")},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprFGKCGE2H6FGXDD4YKLBKW77X3U")},org.radixware.kernel.server.types.Restrictions.Factory.newInstance(262176,null,null,null),null)
							},
							/*Default selector presentation Id*/
							null,
							/*Presentation Map*/
							null,
							/*Object title format*/

							/*Radix::SelectorAddons::EasSelectorAddons:TitleFormat-Object Title Format*/

							new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHQSOHRWZH5ESLD2Y6IVF6XFBE4"),new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.TitleItem[]{

									new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.TitleItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHQSOHRWZH5ESLD2Y6IVF6XFBE4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("prdR2V6YU635RAWVK2EA2HXJIY4Q4"),"{0}~",org.radixware.kernel.common.enums.ETitleNullFormat.SAME_AS_NOT_NULL,null),

									new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.TitleItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHQSOHRWZH5ESLD2Y6IVF6XFBE4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("col6QH5TILJHRDDTC4CHYLQ6HLECA"),"{0}",org.radixware.kernel.common.enums.ETitleNullFormat.SAME_AS_NOT_NULL,null)
							},null,org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.DefinitionContextType.ENTITY),
							/*Class catalogs*/
							null,
							/*Restrictions*/
							org.radixware.kernel.server.types.Restrictions.Factory.newInstance(16384,null,null,null),null,0),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblHQSOHRWZH5ESLD2Y6IVF6XFBE4"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("pdcEntity____________________"),

						/*Radix::SelectorAddons::EasSelectorAddons:Properties-Properties*/
						new org.radixware.kernel.server.meta.clazzes.RadPropDef[]{

								/*Radix::SelectorAddons::EasSelectorAddons:classGuid-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col36K2MUPKPRGPPAGLVPAUJ52PQE"),"classGuid",null,org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::SelectorAddons::EasSelectorAddons:guid-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colTOTOEP36JFH35MA7BJYRU67GKU"),"guid",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5MCCHHCAMNDPHDQTUABTZVO5GA"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::SelectorAddons::EasSelectorAddons:isActive-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colZ23Q7UUURBEAHGCRRSQEKZH76I"),"isActive",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2S2FPBND7FHHJLNFIYL245IX7Y"),org.radixware.kernel.common.enums.EValType.BOOL,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("0")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::SelectorAddons::EasSelectorAddons:lastUpdateTime-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colTZ42HZBXFFBPVL6P4MTIMOAHQE"),"lastUpdateTime",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQ3X6COSR2RHEZNBCUHXFRBN62M"),org.radixware.kernel.common.enums.EValType.DATE_TIME,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::SelectorAddons::EasSelectorAddons:lastUpdateUser-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colVTTB5PHSCFG5TAUCLOI27MO5XM"),"lastUpdateUser",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWQGSWH6ZBFAX3HKR2NNPKNOIEM"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::SelectorAddons::EasSelectorAddons:selPresentations-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colLR66PCM6LJGRNJMVM3ROBROYOY"),"selPresentations",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDGWK7UCVPFD3JPBCZ3LP2EVO5I"),org.radixware.kernel.common.enums.EValType.ARR_STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::SelectorAddons::EasSelectorAddons:seq-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colYNFESMLQLBCGDCKG7FGDPYNPR4"),"seq",null,org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::SelectorAddons::EasSelectorAddons:tableGuid-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDCQEQLHEC5F6VO633LHAE6BDMQ"),"tableGuid",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsIERZCEITT5H3HFGH7FESNTSKJY"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::SelectorAddons::EasSelectorAddons:title-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col6QH5TILJHRDDTC4CHYLQ6HLECA"),"title",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDTKLTPBSZFFL3CPJXNY5A5KL5U"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::SelectorAddons::EasSelectorAddons:isValid-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdJWOOWTAQA5EPHJHYBAYNLMEKAU"),"isValid",null,org.radixware.kernel.common.enums.EValType.BOOL,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::SelectorAddons::EasSelectorAddons:selectorPresentationsList-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdU5D4C52EMBHWPGMWKFMJ7ZUW6Q"),"selectorPresentationsList",null,org.radixware.kernel.common.enums.EValType.XML,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::SelectorAddons::EasSelectorAddons:tableName-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdR2V6YU635RAWVK2EA2HXJIY4Q4"),"tableName",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYVKO5SUXT5CUZOWPZUEW3KRIEI"),org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE)
						},

						/*Radix::SelectorAddons::EasSelectorAddons:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthPT5PMP2WVZGUDB7OTLOE2SBWKE"),"beforeUpdate",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthFMXUFM5J25E4TNM4PR73F6V3E4"),"beforeCreate",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("src",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprSZHRTT4VOBDS7EF2JQTTTJEAUM"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthNVFM3UGLL5GP5I66TCWXWD2NZQ"),"onChange",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthUTQTFSKZTBHQLKLGF5XPPKTR2U"),"getNextSeq",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,null,org.radixware.kernel.common.enums.EValType.INT),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthWKZ3XBJ67ZB5TEPE4T6HDKKIZY"),"checkProperties",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthUV4ZHX7ONVC6RK22Q2QPHSF474"),"isTableExists",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthD6QMQHGF6FBKDLIX27CFDNFIUE"),"isValid",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth4T2RD5IRXBDMVJVLBVX52DOGY4"),"getSelectorPresentationsByTable",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("tableGuid",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr47BNCLGAUVGRLHQ3I3NMJR5MDI"))
								},org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthcmdF2IFUCZIRFCVZBS3PIE33CSZPE"),"onCommand_moveDown",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("newPropValHandlersById",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprBFRPX3SVAFG4VNT7H6FQYMOJXE"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthcmdV47MKSYRSBD6ZN7RIDULX4WFSA"),"onCommand_moveUp",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("newPropValHandlersById",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprRXPAMNHG2FEFDKSD6TV5AQFQQU"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthNCW3I7KVZZEFHMQHYTBVH3CHX4"),"move",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("moveUp",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprDXVFYBTIG5HVXGPIDNM4EBEXNQ"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthKNZ5Z7AS6RFRPIX6ZD5JDKEC34"),"beforeDelete",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthIP6EGNBUXRGPPJHZPJN4CVHPF4"),"updateLastModifyTime",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("time",org.radixware.kernel.common.enums.EValType.DATE_TIME,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprY25VCQSCFRHHBJR6JJVVB5NIUI")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("userName",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr5ASNQBC3GVG3PMKJKOYYZWQTOA"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthV2GVSJ7OVFG3RAWR5FH7NTQBFQ"),"getRestrictedTableIds",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.ARR_STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthZ7M55AIGH5BB3F2TP3PLQP7X3A"),"isOneForTable",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthQXRW7PZ4QBBEJAJL57JFPES7EQ"),"getCfgReferenceExtGuid",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthTIFZEEQC2NEUNPFIRAGRH2B7EY"),"getCfgReferencePropId",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.JAVA_CLASS)
						},
						null,
						org.radixware.kernel.common.enums.EAccessAreaType.NONE,null,null,false);
}

/* Radix::SelectorAddons::EasSelectorAddons - Desktop Executable*/

/*Radix::SelectorAddons::EasSelectorAddons-Entity Class*/

package org.radixware.ads.SelectorAddons.Dlg.explorer;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons")
public interface EasSelectorAddons {
	/*Group model adapter class. This is the base class for 
	all of selector presentation models of this entity*/
	public static class DefaultGroupModel extends org.radixware.kernel.common.client.models.GroupModel{
		public DefaultGroupModel(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadSelectorPresentationDef def){super(userSession,def);}





		public org.radixware.ads.SelectorAddons.Dlg.explorer.EasSelectorAddons.EasSelectorAddons_DefaultModel getEntity(int i) throws InterruptedException, org.radixware.kernel.common.client.exceptions.BrokenEntityObjectException, org.radixware.kernel.common.exceptions.ServiceClientException { return (org.radixware.ads.SelectorAddons.Dlg.explorer.EasSelectorAddons.EasSelectorAddons_DefaultModel )  super.getEntity(i);}
	}






















































































	/*Radix::SelectorAddons::EasSelectorAddons:title:title-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons:title:title")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons:title:title")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Title getTitle();
	/*Radix::SelectorAddons::EasSelectorAddons:tableGuid:tableGuid-Presentation Property*/


	public class TableGuid extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public TableGuid(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons:tableGuid:tableGuid")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons:tableGuid:tableGuid")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public TableGuid getTableGuid();
	/*Radix::SelectorAddons::EasSelectorAddons:selPresentations:selPresentations-Presentation Property*/


	public class SelPresentations extends org.radixware.kernel.common.client.models.items.properties.PropertyArrStr{
		public SelPresentations(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons:selPresentations:selPresentations")
		public  org.radixware.kernel.common.types.ArrStr getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons:selPresentations:selPresentations")
		public   void setValue(org.radixware.kernel.common.types.ArrStr val) {
			Value = val;
		}
	}
	public SelPresentations getSelPresentations();
	/*Radix::SelectorAddons::EasSelectorAddons:guid:guid-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons:guid:guid")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons:guid:guid")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Guid getGuid();
	/*Radix::SelectorAddons::EasSelectorAddons:lastUpdateTime:lastUpdateTime-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons:lastUpdateTime:lastUpdateTime")
		public  java.sql.Timestamp getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons:lastUpdateTime:lastUpdateTime")
		public   void setValue(java.sql.Timestamp val) {
			Value = val;
		}
	}
	public LastUpdateTime getLastUpdateTime();
	/*Radix::SelectorAddons::EasSelectorAddons:lastUpdateUser:lastUpdateUser-Presentation Property*/


	public class LastUpdateUser extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public LastUpdateUser(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons:lastUpdateUser:lastUpdateUser")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons:lastUpdateUser:lastUpdateUser")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public LastUpdateUser getLastUpdateUser();
	/*Radix::SelectorAddons::EasSelectorAddons:isActive:isActive-Presentation Property*/


	public class IsActive extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public IsActive(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons:isActive:isActive")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons:isActive:isActive")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public IsActive getIsActive();
	/*Radix::SelectorAddons::EasSelectorAddons:isValid:isValid-Presentation Property*/


	public class IsValid extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public IsValid(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons:isValid:isValid")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons:isValid:isValid")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public IsValid getIsValid();
	/*Radix::SelectorAddons::EasSelectorAddons:tableName:tableName-Presentation Property*/


	public class TableName extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public TableName(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons:tableName:tableName")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons:tableName:tableName")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public TableName getTableName();
	/*Radix::SelectorAddons::EasSelectorAddons:selectorPresentationsList:selectorPresentationsList-Presentation Property*/


	public class SelectorPresentationsList extends org.radixware.kernel.common.client.models.items.properties.PropertyXml{
		public SelectorPresentationsList(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Class<org.radixware.ads.SelectorAddons.Dlg.common.SelectorAddonsInfoXsd.SelectorPresentationsListDocument> getValClass(){
			return org.radixware.ads.SelectorAddons.Dlg.common.SelectorAddonsInfoXsd.SelectorPresentationsListDocument.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.ads.SelectorAddons.Dlg.common.SelectorAddonsInfoXsd.SelectorPresentationsListDocument dummy = x == null ? null : (org.radixware.ads.SelectorAddons.Dlg.common.SelectorAddonsInfoXsd.SelectorPresentationsListDocument)org.radixware.kernel.common.utils.XmlObjectProcessor.castToXmlClass(getClass().getClassLoader(),(org.apache.xmlbeans.XmlObject)x,org.radixware.ads.SelectorAddons.Dlg.common.SelectorAddonsInfoXsd.SelectorPresentationsListDocument.class);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons:selectorPresentationsList:selectorPresentationsList")
		public  org.radixware.ads.SelectorAddons.Dlg.common.SelectorAddonsInfoXsd.SelectorPresentationsListDocument getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons:selectorPresentationsList:selectorPresentationsList")
		public   void setValue(org.radixware.ads.SelectorAddons.Dlg.common.SelectorAddonsInfoXsd.SelectorPresentationsListDocument val) {
			Value = val;
		}
	}
	public SelectorPresentationsList getSelectorPresentationsList();
	public static class RemoteCall_getSelectorPresentationsByTable extends org.radixware.kernel.common.client.models.items.Command{
		protected RemoteCall_getSelectorPresentationsByTable(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public org.radixware.schemas.utils.RPCResponseDocument send(org.radixware.schemas.utils.RPCRequestDocument input ) throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,input);
			org.apache.xmlbeans.XmlObject xmlResponse = processResponse(response, null);
			return (org.radixware.schemas.utils.RPCResponseDocument)org.radixware.kernel.common.utils.XmlObjectProcessor.castToXmlClass(getClass().getClassLoader(),xmlResponse,org.radixware.schemas.utils.RPCResponseDocument.class);
		}

	}

	public static class MoveDown extends org.radixware.kernel.common.client.models.items.Command{
		protected MoveDown(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public void send() throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,null);
			processResponse(response, null);
		}

	}

	public static class RemoteCall_getUsedTableIds extends org.radixware.kernel.common.client.models.items.Command{
		protected RemoteCall_getUsedTableIds(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public org.radixware.schemas.utils.RPCResponseDocument send(org.radixware.schemas.utils.RPCRequestDocument input ) throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,input);
			org.apache.xmlbeans.XmlObject xmlResponse = processResponse(response, null);
			return (org.radixware.schemas.utils.RPCResponseDocument)org.radixware.kernel.common.utils.XmlObjectProcessor.castToXmlClass(getClass().getClassLoader(),xmlResponse,org.radixware.schemas.utils.RPCResponseDocument.class);
		}

	}

	public static class MoveUp extends org.radixware.kernel.common.client.models.items.Command{
		protected MoveUp(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public void send() throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,null);
			processResponse(response, null);
		}

	}



}

/* Radix::SelectorAddons::EasSelectorAddons - Desktop Meta*/

/*Radix::SelectorAddons::EasSelectorAddons-Entity Class*/

package org.radixware.ads.SelectorAddons.Dlg.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class EasSelectorAddons_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::SelectorAddons::EasSelectorAddons:Presentations-Entity Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHQSOHRWZH5ESLD2Y6IVF6XFBE4"),
			"Radix::SelectorAddons::EasSelectorAddons",
			null,
			null,
			null,
			null,org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2AYPJFZGPRCYHKHXPDLG7YH33Q"),null,16384,

			/*Radix::SelectorAddons::EasSelectorAddons:Properties-Properties*/
			new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

					/*Radix::SelectorAddons::EasSelectorAddons:guid:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colTOTOEP36JFH35MA7BJYRU67GKU"),
						"guid",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5MCCHHCAMNDPHDQTUABTZVO5GA"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHQSOHRWZH5ESLD2Y6IVF6XFBE4"),
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::SelectorAddons::EasSelectorAddons:guid:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,50,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::SelectorAddons::EasSelectorAddons:isActive:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colZ23Q7UUURBEAHGCRRSQEKZH76I"),
						"isActive",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2S2FPBND7FHHJLNFIYL245IX7Y"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHQSOHRWZH5ESLD2Y6IVF6XFBE4"),
						org.radixware.kernel.common.enums.EValType.BOOL,
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
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::SelectorAddons::EasSelectorAddons:isActive:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHQSOHRWZH5ESLD2Y6IVF6XFBE4"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::SelectorAddons::EasSelectorAddons:lastUpdateTime:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colTZ42HZBXFFBPVL6P4MTIMOAHQE"),
						"lastUpdateTime",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQ3X6COSR2RHEZNBCUHXFRBN62M"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHQSOHRWZH5ESLD2Y6IVF6XFBE4"),
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::SelectorAddons::EasSelectorAddons:lastUpdateTime:PropertyPresentation:Edit Options:-Edit Mask Date Time*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskDateTime(org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,null,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::SelectorAddons::EasSelectorAddons:lastUpdateUser:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colVTTB5PHSCFG5TAUCLOI27MO5XM"),
						"lastUpdateUser",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWQGSWH6ZBFAX3HKR2NNPKNOIEM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHQSOHRWZH5ESLD2Y6IVF6XFBE4"),
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::SelectorAddons::EasSelectorAddons:lastUpdateUser:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,250,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::SelectorAddons::EasSelectorAddons:selPresentations:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colLR66PCM6LJGRNJMVM3ROBROYOY"),
						"selPresentations",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDGWK7UCVPFD3JPBCZ3LP2EVO5I"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHQSOHRWZH5ESLD2Y6IVF6XFBE4"),
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
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						false,true,
						false,
						false,
						null,
						false,

						/*Radix::SelectorAddons::EasSelectorAddons:selPresentations:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZ5VDZ4EZFNEBBGAYBJYB3R73OY"),
						null,
						false,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::SelectorAddons::EasSelectorAddons:tableGuid:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDCQEQLHEC5F6VO633LHAE6BDMQ"),
						"tableGuid",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsIERZCEITT5H3HFGH7FESNTSKJY"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHQSOHRWZH5ESLD2Y6IVF6XFBE4"),
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
						org.radixware.kernel.common.enums.EEditPossibility.ON_CREATE,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::SelectorAddons::EasSelectorAddons:tableGuid:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,50,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::SelectorAddons::EasSelectorAddons:title:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col6QH5TILJHRDDTC4CHYLQ6HLECA"),
						"title",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDTKLTPBSZFFL3CPJXNY5A5KL5U"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHQSOHRWZH5ESLD2Y6IVF6XFBE4"),
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
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::SelectorAddons::EasSelectorAddons:title:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,100,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::SelectorAddons::EasSelectorAddons:isValid:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdJWOOWTAQA5EPHJHYBAYNLMEKAU"),
						"isValid",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHQSOHRWZH5ESLD2Y6IVF6XFBE4"),
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
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::SelectorAddons::EasSelectorAddons:isValid:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHQSOHRWZH5ESLD2Y6IVF6XFBE4"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::SelectorAddons::EasSelectorAddons:selectorPresentationsList:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdU5D4C52EMBHWPGMWKFMJ7ZUW6Q"),
						"selectorPresentationsList",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHQSOHRWZH5ESLD2Y6IVF6XFBE4"),
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

					/*Radix::SelectorAddons::EasSelectorAddons:tableName:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdR2V6YU635RAWVK2EA2HXJIY4Q4"),
						"tableName",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYVKO5SUXT5CUZOWPZUEW3KRIEI"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHQSOHRWZH5ESLD2Y6IVF6XFBE4"),
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::SelectorAddons::EasSelectorAddons:tableName:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false)
			},
			new org.radixware.kernel.common.client.meta.RadCommandDef[]{
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::SelectorAddons::EasSelectorAddons:remoteCall_getSelectorPresentationsByTable-Remote Call Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmd3NEA3JPZHBAYFHHYY73PDK57JE"),
						"remoteCall_getSelectorPresentationsByTable",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHQSOHRWZH5ESLD2Y6IVF6XFBE4"),
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
					/*Radix::SelectorAddons::EasSelectorAddons:moveUp-Object Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdV47MKSYRSBD6ZN7RIDULX4WFSA"),
						"moveUp",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHQSOHRWZH5ESLD2Y6IVF6XFBE4"),
						null,
						null,
						null,
						org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,
						false,
						false,
						false,
						org.radixware.kernel.common.enums.ECommandScope.OBJECT,
						null,
						org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_FIXED,
						true),
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::SelectorAddons::EasSelectorAddons:moveDown-Object Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdF2IFUCZIRFCVZBS3PIE33CSZPE"),
						"moveDown",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHQSOHRWZH5ESLD2Y6IVF6XFBE4"),
						null,
						null,
						null,
						org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,
						false,
						false,
						false,
						org.radixware.kernel.common.enums.ECommandScope.OBJECT,
						null,
						org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_FIXED,
						true),
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::SelectorAddons::EasSelectorAddons:remoteCall_getUsedTableIds-Remote Call Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdLYPWZD46EBAFBFEOHLITMHP4LY"),
						"remoteCall_getUsedTableIds",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHQSOHRWZH5ESLD2Y6IVF6XFBE4"),
						null,
						null,
						null,
						org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,
						false,
						false,
						false,
						org.radixware.kernel.common.enums.ECommandScope.RPC,
						null,
						org.radixware.kernel.common.enums.ECommandAccessibility.ALWAYS)
			},
			null,
			new org.radixware.kernel.common.client.meta.RadSortingDef[]{

					new org.radixware.kernel.common.client.meta.RadSortingDef(
					/*Radix::SelectorAddons::EasSelectorAddons:bySeq-Sorting*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("srtSN4J3KY4TNHE5IEJ6CCAX4WI3Y"),
						"bySeq",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHQSOHRWZH5ESLD2Y6IVF6XFBE4"),
						null,
						new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem[]{

								new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDCQEQLHEC5F6VO633LHAE6BDMQ"),
								false),

								new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colYNFESMLQLBCGDCKG7FGDPYNPR4"),
								false)
						})
			},
			new org.radixware.kernel.common.client.meta.RadReferenceDef[]{

					new org.radixware.kernel.common.client.meta.RadReferenceDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("ref6Q7RNEMDGBE3HFWGEDOZ4GPZWQ"),"EasSelectorAddons=>User (lastUpdateUser=>name)",org.radixware.kernel.common.types.Id.Factory.loadFrom("tblHQSOHRWZH5ESLD2Y6IVF6XFBE4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblSY4KIOLTGLNRDHRZABQAQH3XQ4"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colVTTB5PHSCFG5TAUCLOI27MO5XM")},new String[]{"lastUpdateUser"},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colEF6ODCYWY3NBDGMCABQAQH3XQ4")},new String[]{"name"})
			},
			new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprFGKCGE2H6FGXDD4YKLBKW77X3U"),org.radixware.kernel.common.types.Id.Factory.loadFrom("sprIIEIWQINNRCG5OAYT2TYA6NNE4")},
			true,false,false);
}

/* Radix::SelectorAddons::EasSelectorAddons - Web Meta*/

/*Radix::SelectorAddons::EasSelectorAddons-Entity Class*/

package org.radixware.ads.SelectorAddons.Dlg.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class EasSelectorAddons_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::SelectorAddons::EasSelectorAddons:Presentations-Entity Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHQSOHRWZH5ESLD2Y6IVF6XFBE4"),
			"Radix::SelectorAddons::EasSelectorAddons",
			null,
			null,
			null,
			null,org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2AYPJFZGPRCYHKHXPDLG7YH33Q"),null,16384,
			null,
			null,
			null,
			null,
			null,
			null,
			false,false,false);
}

/* Radix::SelectorAddons::EasSelectorAddons:AbstractBase - Desktop Meta*/

/*Radix::SelectorAddons::EasSelectorAddons:AbstractBase-Editor Presentation*/

package org.radixware.ads.SelectorAddons.Dlg.explorer;
public final class AbstractBase_mi{
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new org.radixware.kernel.common.client.meta.RadEditorPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprFGKCGE2H6FGXDD4YKLBKW77X3U"),
	"AbstractBase",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
	null,
	org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHQSOHRWZH5ESLD2Y6IVF6XFBE4"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("tblHQSOHRWZH5ESLD2Y6IVF6XFBE4"),
	null,
	null,

	/*Radix::SelectorAddons::EasSelectorAddons:AbstractBase:Editor Pages-Editor Presentation Pages*/
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

			/*Radix::SelectorAddons::EasSelectorAddons:AbstractBase:SelectorPresentations-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadCustomEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgZG6UTMVC6ND6TPIYVCGMT3YWIY"),"SelectorPresentations",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHQSOHRWZH5ESLD2Y6IVF6XFBE4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3IJMUTLE2RAXJDEU4KHQTGCOAU"),null,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("cepBICGCMQSJ5C2JDMYI7Z5GG2MQY"))
	},
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgZG6UTMVC6ND6TPIYVCGMT3YWIY"))}
	,

	/*Radix::SelectorAddons::EasSelectorAddons:AbstractBase:Children-Explorer Items*/
		null
	,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
	null,
	0,
	null,
	(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
	144,0,0,null);
}
/* Radix::SelectorAddons::EasSelectorAddons:AbstractBase:Model - Desktop Executable*/

/*Radix::SelectorAddons::EasSelectorAddons:AbstractBase:Model-Entity Model Class*/

package org.radixware.ads.SelectorAddons.Dlg.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons:AbstractBase:Model")
public class AbstractBase:Model  extends org.radixware.ads.SelectorAddons.Dlg.explorer.EasSelectorAddons.EasSelectorAddons_DefaultModel {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return AbstractBase:Model_mi.rdxMeta; }



	public AbstractBase:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadEditorPresentationDef def){super(userSession,def);}

	/*Radix::SelectorAddons::EasSelectorAddons:AbstractBase:Model:Nested classes-Nested Classes*/

	/*Radix::SelectorAddons::EasSelectorAddons:AbstractBase:Model:Properties-Properties*/

	/*Radix::SelectorAddons::EasSelectorAddons:AbstractBase:Model:selectorPresentationsForCurrentTable-Dynamic Property*/



	protected org.radixware.ads.SelectorAddons.Dlg.common.SelectorAddonsInfoXsd.SelectorPresentationsListDocument selectorPresentationsForCurrentTable=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons:AbstractBase:Model:selectorPresentationsForCurrentTable")
	private final  org.radixware.ads.SelectorAddons.Dlg.common.SelectorAddonsInfoXsd.SelectorPresentationsListDocument getSelectorPresentationsForCurrentTable() {
		return selectorPresentationsForCurrentTable;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons:AbstractBase:Model:selectorPresentationsForCurrentTable")
	private final   void setSelectorPresentationsForCurrentTable(org.radixware.ads.SelectorAddons.Dlg.common.SelectorAddonsInfoXsd.SelectorPresentationsListDocument val) {
		selectorPresentationsForCurrentTable = val;
	}

	/*Radix::SelectorAddons::EasSelectorAddons:AbstractBase:Model:tableGuid-Presentation Property*/




	public class TableGuid extends org.radixware.ads.SelectorAddons.Dlg.explorer.EasSelectorAddons.colDCQEQLHEC5F6VO633LHAE6BDMQ{
		public TableGuid(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons:AbstractBase:Model:tableGuid")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons:AbstractBase:Model:tableGuid")
		public   void setValue(Str val) {

			internal[tableGuid] = val;
			onChangeTableGuid();
		}
	}
	public TableGuid getTableGuid(){return (TableGuid)getProperty(colDCQEQLHEC5F6VO633LHAE6BDMQ);}








	/*Radix::SelectorAddons::EasSelectorAddons:AbstractBase:Model:Methods-Methods*/

	/*Radix::SelectorAddons::EasSelectorAddons:AbstractBase:Model:createPropertyEditor-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons:AbstractBase:Model:createPropertyEditor")
	public published  org.radixware.kernel.common.client.widgets.IModelWidget createPropertyEditor (org.radixware.kernel.common.types.Id propertyId) {
		if (idof[EasSelectorAddons:tableGuid].equals(propertyId)) {

		    ArrStr ids = null;
		    try {
		        ids = getRestrictedTableIds();
		    } catch (Exceptions::ServiceClientException e) {
		    } catch (InterruptedException e) {
		    }
		    final ArrStr restrictedIds = ids;
		    
		    final SelectorAddons.Dlg::PropSqmlTableEditor editor;
		    if (restrictedIds == null || restrictedIds.isEmpty()) {
		        editor = new PropSqmlTableEditor(tableGuid);
		    } else {
		        java.util.List<Explorer.Sqml::ISqmlDefinition> definitions = new java.util.ArrayList<Explorer.Sqml::ISqmlDefinition>();
		        final java.util.Collection<Explorer.Sqml::ISqmlTableDef> tables = Environment.getSqmlDefinitions().getTables();
		        for (Explorer.Sqml::ISqmlTableDef table : tables) {
		            Types::Id tableId = Types::Id.Factory.changePrefix(table.getId(), Meta::DefinitionIdPrefix:DDS_TABLE);
		            Types::Id clazzId = Types::Id.Factory.changePrefix(table.getId(), Meta::DefinitionIdPrefix:ADS_ENTITY_CLASS);

		            if (!(restrictedIds.contains(tableId.toString()) || restrictedIds.contains(clazzId.toString()))) {
		                definitions.add(table);                
		            }
		        }
		        editor = new PropSqmlTableEditor(tableGuid, definitions);
		    }


		    editor.setDefinitionsFilter(new Explorer.Sqml::ISqmlDefinitionsFilter() {
		        @Override
		        public boolean isAccepted(Explorer.Sqml::ISqmlDefinition definition, Explorer.Sqml::ISqmlDefinition ownerDefinition) {
		            if (restrictedIds != null && !restrictedIds.isEmpty() && restrictedIds.contains(definition.getId().toString())) {
		                return false;
		            }

		            return (definition.getId().getPrefix() == Meta::DefinitionIdPrefix:DDS_TABLE
		                    || definition.getId().getPrefix() == Meta::DefinitionIdPrefix:ADS_ENTITY_CLASS)
		                    && //Фильтр имеет смысл создавать только для таблиц, опубликованных в ADS.
		                    (definition instanceof Explorer.Sqml::ISqmlTableDef) && ((Explorer.Sqml::ISqmlTableDef) definition).hasEntityClass();
		        }
		    });

		    return editor;
		} else if (idof[EasSelectorAddons:selPresentations].equals(propertyId)) {
		    return new AllowedSelectorPresentationsEditor(this);
		} else {
		    return super.createPropertyEditor(propertyId);
		}
	}

	/*Radix::SelectorAddons::EasSelectorAddons:AbstractBase:Model:getSelectorRowStyle-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons:AbstractBase:Model:getSelectorRowStyle")
	public published  org.radixware.kernel.common.enums.ESelectorRowStyle getSelectorRowStyle () {
		if (!isValid.Value.booleanValue()){
		    return Explorer.Env::SelectorStyle:Bad;
		}
		else if (!isActive.Value.booleanValue()){
		    return Explorer.Env::SelectorStyle:Unimportant;
		}
		else{
		    return super.getSelectorRowStyle(); 
		}
	}

	/*Radix::SelectorAddons::EasSelectorAddons:AbstractBase:Model:afterRead-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons:AbstractBase:Model:afterRead")
	protected published  void afterRead () {
		super.afterRead();
		if (!isValid.Value.booleanValue()){
		    getRestrictions().setUpdateRestricted(true);
		    getRestrictions().setCopyRestricted(true);
		}
		if (!isInSelectorRowContext()){
		    selectorPresentationsForCurrentTable = selectorPresentationsList.Value;
		}
		getEditorPage(idof[EasSelectorAddons:AbstractBase:SelectorPresentations]).setEnabled(tableGuid.Value!=null && isTableExists());
	}

	/*Radix::SelectorAddons::EasSelectorAddons:AbstractBase:Model:remoteCall_getSelectorPresentationsByTable-Remote Procedure Call Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons:AbstractBase:Model:remoteCall_getSelectorPresentationsByTable")
	private final  org.radixware.ads.SelectorAddons.Dlg.common.SelectorAddonsInfoXsd.SelectorPresentationsListDocument remoteCall_getSelectorPresentationsByTable (Str tableGuid) throws org.radixware.kernel.common.exceptions.ServiceClientException,java.lang.InterruptedException {
		final org.radixware.ads.SelectorAddons.Dlg.explorer.EasSelectorAddons.RemoteCall_getSelectorPresentationsByTable _cmd_cmd3NEA3JPZHBAYFHHYY73PDK57JE_instance_ = (org.radixware.ads.SelectorAddons.Dlg.explorer.EasSelectorAddons.RemoteCall_getSelectorPresentationsByTable)getCommand(org.radixware.kernel.common.types.Id.Factory.loadFrom("cmd3NEA3JPZHBAYFHHYY73PDK57JE"));
		final java.util.List<org.radixware.kernel.common.utils.RPCProcessor.ArgumentInfo> $remote$call$arg$list$store$ = new java.util.LinkedList<org.radixware.kernel.common.utils.RPCProcessor.ArgumentInfo>();
		$remote$call$arg$list$store$.add(new org.radixware.kernel.common.utils.RPCProcessor.ArgumentInfo(org.radixware.kernel.common.enums.EValType.STR,tableGuid));
		final org.radixware.kernel.common.utils.RPCProcessor.ClientSideInvocation $invoker_instance$ = new org.radixware.kernel.common.utils.RPCProcessor.ClientSideInvocation($remote$call$arg$list$store$){
			protected org.radixware.schemas.utils.RPCResponseDocument invokeImpl(org.radixware.schemas.utils.RPCRequestDocument rq) throws org.radixware.kernel.common.exceptions.ServiceClientException, InterruptedException{
				return _cmd_cmd3NEA3JPZHBAYFHHYY73PDK57JE_instance_.send(rq);
			}
		};
		final Object $rpc$call$result$ = $invoker_instance$.invoke();
		return $rpc$call$result$ == null ? null : $rpc$call$result$ instanceof org.radixware.ads.SelectorAddons.Dlg.common.SelectorAddonsInfoXsd.SelectorPresentationsListDocument ? (org.radixware.ads.SelectorAddons.Dlg.common.SelectorAddonsInfoXsd.SelectorPresentationsListDocument) $rpc$call$result$ : (org.radixware.ads.SelectorAddons.Dlg.common.SelectorAddonsInfoXsd.SelectorPresentationsListDocument) org.radixware.kernel.common.utils.XmlObjectProcessor.castToXmlClass(getClass().getClassLoader(),(org.apache.xmlbeans.XmlObject)$rpc$call$result$,org.radixware.ads.SelectorAddons.Dlg.common.SelectorAddonsInfoXsd.SelectorPresentationsListDocument.class);

	}

	/*Radix::SelectorAddons::EasSelectorAddons:AbstractBase:Model:getSelectorPresentationsForCurrentTable-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons:AbstractBase:Model:getSelectorPresentationsForCurrentTable")
	public  org.radixware.ads.SelectorAddons.Dlg.common.SelectorAddonsInfoXsd.SelectorPresentationsListDocument getSelectorPresentationsForCurrentTable () {
		if (selectorPresentationsForCurrentTable==null && tableGuid.Value!=null && wasRead()){
		    try{
		        selectorPresentationsForCurrentTable = remoteCall_getSelectorPresentationsByTable(tableGuid.Value);        
		    }
		    catch(Exceptions::ServiceClientException ex){
		        showException(ex);
		    }
		    catch(Exceptions::InterruptedException ex){
		        
		    }        
		}
		return selectorPresentationsForCurrentTable;
	}

	/*Radix::SelectorAddons::EasSelectorAddons:AbstractBase:Model:onChangeTableGuid-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons:AbstractBase:Model:onChangeTableGuid")
	protected  void onChangeTableGuid () {
		selectorPresentationsForCurrentTable = null;
		selectorPresentationsList.Value = null;
		selectorPresentationsList.afterModify();
		selPresentations.Value = null;
		getEditorPage(idof[EasSelectorAddons:AbstractBase:SelectorPresentations]).setEnabled(tableGuid.Value!=null && isTableExists());
	}

	/*Radix::SelectorAddons::EasSelectorAddons:AbstractBase:Model:selectorPresentationsEditorPage_opened-Presentation Slot Method*/

	@org.radixware.kernel.common.lang.ReflectiveCallable
	@SuppressWarnings("unused")
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons:AbstractBase:Model:selectorPresentationsEditorPage_opened")
	public  void selectorPresentationsEditorPage_opened (com.trolltech.qt.gui.QWidget widget) {
		final com.trolltech.qt.gui.QVBoxLayout layout = new com.trolltech.qt.gui.QVBoxLayout(widget);
		final SelectorAddons.Dlg::AllowedSelectorPresentationsEditor editor = new AllowedSelectorPresentationsEditor(this);
		layout.addWidget(editor);
		editor.bind();
	}

	/*Radix::SelectorAddons::EasSelectorAddons:AbstractBase:Model:isTableExists-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons:AbstractBase:Model:isTableExists")
	protected final  boolean isTableExists () {
		return getEnvironment().getSqmlDefinitions().findTableById(Types::Id.Factory.loadFrom(tableGuid.Value))!=null;
	}

	/*Radix::SelectorAddons::EasSelectorAddons:AbstractBase:Model:onCommand_moveDown-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons:AbstractBase:Model:onCommand_moveDown")
	private final  void onCommand_moveDown (org.radixware.ads.SelectorAddons.Dlg.explorer.EasSelectorAddons.MoveDown command) {
		try{
		    command.send();
		}catch(Exceptions::InterruptedException  e){
		        showException(e);
		}catch(Exceptions::ServiceClientException  e){
		        showException(e);
		}
	}

	/*Radix::SelectorAddons::EasSelectorAddons:AbstractBase:Model:onCommand_moveUp-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons:AbstractBase:Model:onCommand_moveUp")
	private final  void onCommand_moveUp (org.radixware.ads.SelectorAddons.Dlg.explorer.EasSelectorAddons.MoveUp command) {
		try{
		    command.send();
		}catch(Exceptions::InterruptedException  e){
		        showException(e);
		}catch(Exceptions::ServiceClientException  e){
		        showException(e);
		}
	}

	/*Radix::SelectorAddons::EasSelectorAddons:AbstractBase:Model:getRestrictedTableIds-Remote Procedure Call Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons:AbstractBase:Model:getRestrictedTableIds")
	public  org.radixware.kernel.common.types.ArrStr getRestrictedTableIds () throws org.radixware.kernel.common.exceptions.ServiceClientException,java.lang.InterruptedException {
		final org.radixware.ads.SelectorAddons.Dlg.explorer.EasSelectorAddons.RemoteCall_getUsedTableIds _cmd_cmdLYPWZD46EBAFBFEOHLITMHP4LY_instance_ = (org.radixware.ads.SelectorAddons.Dlg.explorer.EasSelectorAddons.RemoteCall_getUsedTableIds)getCommand(org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdLYPWZD46EBAFBFEOHLITMHP4LY"));
		final java.util.List<org.radixware.kernel.common.utils.RPCProcessor.ArgumentInfo> $remote$call$arg$list$store$ = new java.util.LinkedList<org.radixware.kernel.common.utils.RPCProcessor.ArgumentInfo>();
		final org.radixware.kernel.common.utils.RPCProcessor.ClientSideInvocation $invoker_instance$ = new org.radixware.kernel.common.utils.RPCProcessor.ClientSideInvocation($remote$call$arg$list$store$){
			protected org.radixware.schemas.utils.RPCResponseDocument invokeImpl(org.radixware.schemas.utils.RPCRequestDocument rq) throws org.radixware.kernel.common.exceptions.ServiceClientException, InterruptedException{
				return _cmd_cmdLYPWZD46EBAFBFEOHLITMHP4LY_instance_.send(rq);
			}
		};
		final Object $rpc$call$result$ = $invoker_instance$.invoke();
		return $rpc$call$result$ == null ? null : (org.radixware.kernel.common.types.ArrStr)$rpc$call$result$;

	}
	public static class RemoteCall_getSelectorPresentationsByTable extends org.radixware.ads.SelectorAddons.Dlg.explorer.EasSelectorAddons.RemoteCall_getSelectorPresentationsByTable{
		protected RemoteCall_getSelectorPresentationsByTable(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public org.radixware.schemas.utils.RPCResponseDocument send(org.radixware.schemas.utils.RPCRequestDocument input ) throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,input);
			org.apache.xmlbeans.XmlObject xmlResponse = processResponse(response, null);
			return (org.radixware.schemas.utils.RPCResponseDocument)org.radixware.kernel.common.utils.XmlObjectProcessor.castToXmlClass(getClass().getClassLoader(),xmlResponse,org.radixware.schemas.utils.RPCResponseDocument.class);
		}

	}

	public final class MoveDown extends org.radixware.ads.SelectorAddons.Dlg.explorer.EasSelectorAddons.MoveDown{
		protected MoveDown(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_moveDown( this );
		}

	}

	public static class RemoteCall_getUsedTableIds extends org.radixware.ads.SelectorAddons.Dlg.explorer.EasSelectorAddons.RemoteCall_getUsedTableIds{
		protected RemoteCall_getUsedTableIds(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public org.radixware.schemas.utils.RPCResponseDocument send(org.radixware.schemas.utils.RPCRequestDocument input ) throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,input);
			org.apache.xmlbeans.XmlObject xmlResponse = processResponse(response, null);
			return (org.radixware.schemas.utils.RPCResponseDocument)org.radixware.kernel.common.utils.XmlObjectProcessor.castToXmlClass(getClass().getClassLoader(),xmlResponse,org.radixware.schemas.utils.RPCResponseDocument.class);
		}

	}

	public final class MoveUp extends org.radixware.ads.SelectorAddons.Dlg.explorer.EasSelectorAddons.MoveUp{
		protected MoveUp(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_moveUp( this );
		}

	}



















}

/* Radix::SelectorAddons::EasSelectorAddons:AbstractBase:Model - Desktop Meta*/

/*Radix::SelectorAddons::EasSelectorAddons:AbstractBase:Model-Entity Model Class*/

package org.radixware.ads.SelectorAddons.Dlg.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class AbstractBase:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aemFGKCGE2H6FGXDD4YKLBKW77X3U"),
						"AbstractBase:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::SelectorAddons::EasSelectorAddons:AbstractBase:Model:Properties-Properties*/
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

/* Radix::SelectorAddons::EasSelectorAddons:AbstractBase:SelectorPresentations:View - Desktop Executable*/

/*Radix::SelectorAddons::EasSelectorAddons:AbstractBase:SelectorPresentations:View-Custom Page Editor for Desktop*/

package org.radixware.ads.SelectorAddons.Dlg.explorer;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons:AbstractBase:SelectorPresentations:View")
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
		EditorPageView.opened.connect(model, "mthYLQMMD5J3RADXHR6EKQ6U3XW4U(com.trolltech.qt.gui.QWidget)");
		opened.emit(this);
	}
	public final org.radixware.ads.SelectorAddons.Dlg.explorer.AbstractBase:Model getModel() {
		return (org.radixware.ads.SelectorAddons.Dlg.explorer.AbstractBase:Model) super.getModel();
	}

}

/* Radix::SelectorAddons::EasSelectorAddons:AbstractBase:SelectorPresentations:WebView - Web Executable*/

/*Radix::SelectorAddons::EasSelectorAddons:AbstractBase:SelectorPresentations:WebView-Rwt Custom Page Editor*/

package org.radixware.ads.SelectorAddons.Dlg.web;
@SuppressWarnings({"unchecked","rawtypes"})
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons:AbstractBase:SelectorPresentations:WebView")
public class WebView extends org.radixware.wps.views.editor.EditorPageView{
	public WebView widget;
	public WebView getWidget(){ return  widget;}
	public WebView(org.radixware.kernel.common.client.IClientEnvironment env,org.radixware.kernel.common.client.views.IView view
	,org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef page){
		super(env,view,page);
	}
	public void open(org.radixware.kernel.common.client.models.Model model){
		super.open(model);
		//============ Radix::SelectorAddons::EasSelectorAddons:AbstractBase:SelectorPresentations:WebView:widget ==============
		this.widget = this;
		fireOpened();
	}
}

/* Radix::SelectorAddons::EasSelectorAddons:AbstractBase - Desktop Meta*/

/*Radix::SelectorAddons::EasSelectorAddons:AbstractBase-Selector Presentation*/

package org.radixware.ads.SelectorAddons.Dlg.explorer;
public final class AbstractBase_mi extends org.radixware.kernel.common.client.meta.RadSelectorPresentationDef{
	public static final org.radixware.kernel.common.client.meta.RadSelectorPresentationDef rdxMeta = new AbstractBase_mi();
	private AbstractBase_mi(){
		super(org.radixware.kernel.common.types.Id.Factory.loadFrom("sprIIEIWQINNRCG5OAYT2TYA6NNE4"),
		"AbstractBase",
		org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
		null,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHQSOHRWZH5ESLD2Y6IVF6XFBE4"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("tblHQSOHRWZH5ESLD2Y6IVF6XFBE4"),
		null,
		null,
		null,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("srtSN4J3KY4TNHE5IEJ6CCAX4WI3Y")},
		false,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("srtSN4J3KY4TNHE5IEJ6CCAX4WI3Y"),
		new org.radixware.kernel.common.types.Id[]{},
		false,
		false,
		null,
		262176,
		null,
		144,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprFGKCGE2H6FGXDD4YKLBKW77X3U")},
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprFGKCGE2H6FGXDD4YKLBKW77X3U")},
		false,true,false,0,0);
		columns = new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn[]{

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col6QH5TILJHRDDTC4CHYLQ6HLECA"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null)
		};
	}
	@Override
	protected org.radixware.kernel.common.client.models.Model createModelImpl(org.radixware.kernel.common.client.IClientEnvironment userSession) {    return new org.radixware.ads.SelectorAddons.Dlg.explorer.EasSelectorAddons.DefaultGroupModel(userSession,this);
	}
}
/* Radix::SelectorAddons::EasSelectorAddons - Localizing Bundle */
package org.radixware.ads.SelectorAddons.Dlg.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class EasSelectorAddons - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2AYPJFZGPRCYHKHXPDLG7YH33Q"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Enabled");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Доступен");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2S2FPBND7FHHJLNFIYL245IX7Y"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Selector Presentations");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Презентации селектора");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3IJMUTLE2RAXJDEU4KHQTGCOAU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"ID");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ид.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5MCCHHCAMNDPHDQTUABTZVO5GA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Table #%s not found");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Не найдена таблица #%s");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6WRE54ME6JDQZGMQXXPEX6JFGE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Selector presentations");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Презентации селектора");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDGWK7UCVPFD3JPBCZ3LP2EVO5I"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Name");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Название");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDTKLTPBSZFFL3CPJXNY5A5KL5U"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Table");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Таблица");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsIERZCEITT5H3HFGH7FESNTSKJY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Unable to change the setting procedure");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Невозможно изменить порядок настроек");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsL7VG7MT2VFCANABC3243L72HZE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Last updated");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Время последней модификации");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQ3X6COSR2RHEZNBCUHXFRBN62M"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Last updated by");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Автор последней модификации");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWQGSWH6ZBFAX3HKR2NNPKNOIEM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Table Name");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Имя таблицы");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYVKO5SUXT5CUZOWPZUEW3KRIEI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"All presentations");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Все презентации");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZ5VDZ4EZFNEBBGAYBJYB3R73OY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(EasSelectorAddons - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbaecHQSOHRWZH5ESLD2Y6IVF6XFBE4"),"EasSelectorAddons - Localizing Bundle",$$$items$$$);
}
