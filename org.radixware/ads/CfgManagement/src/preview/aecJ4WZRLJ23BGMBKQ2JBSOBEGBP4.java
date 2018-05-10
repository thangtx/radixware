
/* Radix::CfgManagement::CfgItem - Server Executable*/

/*Radix::CfgManagement::CfgItem-Entity Class*/

package org.radixware.ads.CfgManagement.server;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItem")
public abstract published class CfgItem  extends org.radixware.ads.Types.server.Entity  implements org.radixware.ads.CfgManagement.server.ICfgReferencedObject,org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {


	/**Metainformation accessor method*/
	@SuppressWarnings("unused")
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return CfgItem_mi.rdxMeta;}

	/*Radix::CfgManagement::CfgItem:Nested classes-Nested Classes*/

	/*Radix::CfgManagement::CfgItem:Properties-Properties*/

	/*Radix::CfgManagement::CfgItem:allRefsState-Column-Based Property*/








			















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItem:allRefsState")
	public published  org.radixware.ads.CfgManagement.common.RefState getAllRefsState() {
		return allRefsState;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItem:allRefsState")
	public published   void setAllRefsState(org.radixware.ads.CfgManagement.common.RefState val) {
		allRefsState = val;
	}

	/*Radix::CfgManagement::CfgItem:data-Column-Based Property*/










































	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItem:data")
	public published  org.apache.xmlbeans.XmlObject getData() {
		return data;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItem:data")
	public published   void setData(org.apache.xmlbeans.XmlObject val) {
		data = val;
	}

	/*Radix::CfgManagement::CfgItem:classGuid-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItem:classGuid")
	public published  Str getClassGuid() {
		return classGuid;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItem:classGuid")
	public published   void setClassGuid(Str val) {
		classGuid = val;
	}

	/*Radix::CfgManagement::CfgItem:parentId-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItem:parentId")
	public published  Int getParentId() {
		return parentId;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItem:parentId")
	public published   void setParentId(Int val) {
		parentId = val;
	}

	/*Radix::CfgManagement::CfgItem:objState-Column-Based Property*/








			















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItem:objState")
	public published  org.radixware.ads.CfgManagement.common.CfgObjState getObjState() {
		return objState;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItem:objState")
	public published   void setObjState(org.radixware.ads.CfgManagement.common.CfgObjState val) {
		objState = val;
	}

	/*Radix::CfgManagement::CfgItem:packetId-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItem:packetId")
	public published  Int getPacketId() {
		return packetId;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItem:packetId")
	public published   void setPacketId(Int val) {
		packetId = val;
	}

	/*Radix::CfgManagement::CfgItem:id-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItem:id")
	public published  Int getId() {
		return id;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItem:id")
	public published   void setId(Int val) {
		id = val;
	}

	/*Radix::CfgManagement::CfgItem:skip-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItem:skip")
	public published  Bool getSkip() {
		return skip;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItem:skip")
	public published   void setSkip(Bool val) {
		skip = val;
	}

	/*Radix::CfgManagement::CfgItem:srcObjectPid-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItem:srcObjectPid")
	public published  Str getSrcObjectPid() {
		return srcObjectPid;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItem:srcObjectPid")
	public published   void setSrcObjectPid(Str val) {
		srcObjectPid = val;
	}

	/*Radix::CfgManagement::CfgItem:srcObjectTitle-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItem:srcObjectTitle")
	public published  Str getSrcObjectTitle() {
		return srcObjectTitle;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItem:srcObjectTitle")
	public published   void setSrcObjectTitle(Str val) {
		srcObjectTitle = val;
	}

	/*Radix::CfgManagement::CfgItem:parent-Parent Reference*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItem:parent")
	public published  org.radixware.ads.CfgManagement.server.CfgItem getParent() {
		return parent;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItem:parent")
	public published   void setParent(org.radixware.ads.CfgManagement.server.CfgItem val) {
		parent = val;
	}

	/*Radix::CfgManagement::CfgItem:packet-Parent Reference*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItem:packet")
	public published  org.radixware.ads.CfgManagement.server.CfgPacket getPacket() {
		return packet;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItem:packet")
	public published   void setPacket(org.radixware.ads.CfgManagement.server.CfgPacket val) {
		packet = val;
	}

	/*Radix::CfgManagement::CfgItem:notes-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItem:notes")
	public published  Str getNotes() {
		return notes;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItem:notes")
	public published   void setNotes(Str val) {
		notes = val;
	}

	/*Radix::CfgManagement::CfgItem:classTitle-Dynamic Property*/



	protected Str classTitle=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItem:classTitle")
	public published  Str getClassTitle() {

		return getClassDefinitionTitle();
	}

	/*Radix::CfgManagement::CfgItem:hasChildren-Dynamic Property*/



	protected Bool hasChildren=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItem:hasChildren")
	protected  Bool getHasChildren() {

		CfgItemsCursor c = CfgItemsCursor.open(packetId, true, id);
		try {
		    return c.next();
		} finally {
		    c.close();
		}

	}

	/*Radix::CfgManagement::CfgItem:importPacket-Dynamic Property*/



	protected org.radixware.ads.CfgManagement.server.CfgPacket.Import importPacket=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItem:importPacket")
	protected published  org.radixware.ads.CfgManagement.server.CfgPacket.Import getImportPacket() {

		return (CfgPacket.Import) packet;
	}

	/*Radix::CfgManagement::CfgItem:srcObjectRid-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItem:srcObjectRid")
	public published  Str getSrcObjectRid() {
		return srcObjectRid;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItem:srcObjectRid")
	public published   void setSrcObjectRid(Str val) {
		srcObjectRid = val;
	}

	/*Radix::CfgManagement::CfgItem:settings-Column-Based Property*/










































	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItem:settings")
	public published  org.radixware.ads.CfgManagement.common.ImpExpXsd.SettingsDocument getSettings() {
		return settings;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItem:settings")
	public published   void setSettings(org.radixware.ads.CfgManagement.common.ImpExpXsd.SettingsDocument val) {
		settings = val;
	}

	/*Radix::CfgManagement::CfgItem:settingsWrapper-Dynamic Property*/



	protected org.radixware.ads.CfgManagement.common.ICfgImportSettings settingsWrapper=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItem:settingsWrapper")
	private final  org.radixware.ads.CfgManagement.common.ICfgImportSettings getSettingsWrapper() {
		return settingsWrapper;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItem:settingsWrapper")
	private final   void setSettingsWrapper(org.radixware.ads.CfgManagement.common.ICfgImportSettings val) {
		settingsWrapper = val;
	}

	/*Radix::CfgManagement::CfgItem:srcAppVer-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItem:srcAppVer")
	public published  Str getSrcAppVer() {
		return srcAppVer;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItem:srcAppVer")
	public published   void setSrcAppVer(Str val) {
		srcAppVer = val;
	}

	/*Radix::CfgManagement::CfgItem:srcExtGuid-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItem:srcExtGuid")
	public published  Str getSrcExtGuid() {
		return srcExtGuid;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItem:srcExtGuid")
	public published   void setSrcExtGuid(Str val) {
		srcExtGuid = val;
	}















































































































































	/*Radix::CfgManagement::CfgItem:Methods-Methods*/

	/*Radix::CfgManagement::CfgItem:linkImpObject-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItem:linkImpObject")
	protected abstract published  void linkImpObject ();

	/*Radix::CfgManagement::CfgItem:onCommand_Relink-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItem:onCommand_Relink")
	  void onCommand_Relink (org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValHandlersById) {
		link();

	}

	/*Radix::CfgManagement::CfgItem:extractExportData-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItem:extractExportData")
	protected abstract published  void extractExportData (org.radixware.ads.CfgManagement.server.CfgExportData data);

	/*Radix::CfgManagement::CfgItem:actualizeExport-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItem:actualizeExport")
	 final  void actualizeExport (org.radixware.ads.CfgManagement.server.CfgSettingsProviderForActualize provider) {
		CfgExportData data = new CfgExportData();
		extractExportData(data);
		provider.curItem = this;
		((CfgPacket.Export)packet).getOrCreateItem(data, parent, null, provider);
	}

	/*Radix::CfgManagement::CfgItem:load-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItem:load")
	protected abstract published  void load (org.radixware.ads.CfgManagement.server.ICfgImportHelper helper) throws org.radixware.kernel.common.exceptions.AppException;

	/*Radix::CfgManagement::CfgItem:afterUpdate-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItem:afterUpdate")
	protected published  void afterUpdate () {
		packet.onUpdate();
		super.afterUpdate();
	}

	/*Radix::CfgManagement::CfgItem:afterCreate-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItem:afterCreate")
	protected published  void afterCreate (org.radixware.kernel.server.types.Entity src) {
		packet.onUpdate();
		super.afterCreate(src);
	}

	/*Radix::CfgManagement::CfgItem:beforeDelete-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItem:beforeDelete")
	protected published  boolean beforeDelete () {
		packet.onUpdate();
		return super.beforeDelete();
	}

	/*Radix::CfgManagement::CfgItem:createRefs-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItem:createRefs")
	protected published  void createRefs () {

	}

	/*Radix::CfgManagement::CfgItem:loadByPidStr-System Method*/

	@SuppressWarnings("unused")
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItem:loadByPidStr")
	 static  org.radixware.ads.CfgManagement.server.CfgItem loadByPidStr (Str pidAsStr, boolean checkExistance) {
	org.radixware.kernel.server.types.Pid pid = new org.radixware.kernel.server.types.Pid(org.radixware.ads.mdlPEKYFVDRVZHGZCBQQDY2NOYFOY.server.pdcArte______________________.__getArteInstanceInternal(),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblJ4WZRLJ23BGMBKQ2JBSOBEGBP4"),pidAsStr);
		try{
		return (
		org.radixware.ads.CfgManagement.server.CfgItem) org.radixware.ads.mdlPEKYFVDRVZHGZCBQQDY2NOYFOY.server.pdcArte______________________.__getArteInstanceInternal().getEntityObject(pid,null,checkExistance);
	}catch(org.radixware.kernel.server.exceptions.EntityObjectNotExistsError e){
	return null;
	}

	}

	/*Radix::CfgManagement::CfgItem:loadByPK-System Method*/

	@SuppressWarnings("unused")
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItem:loadByPK")
	 static  org.radixware.ads.CfgManagement.server.CfgItem loadByPK (Int id, boolean checkExistance) {
	final java.util.HashMap<org.radixware.kernel.common.types.Id,Object> pkValsMap = new java.util.HashMap<org.radixware.kernel.common.types.Id,Object>(5);
			if(id==null) return null;
			pkValsMap.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIUQVQZZKPFBRLG7DTABUYGLYBQ"),id);
	org.radixware.kernel.server.types.Pid pid = new org.radixware.kernel.server.types.Pid(org.radixware.ads.mdlPEKYFVDRVZHGZCBQQDY2NOYFOY.server.pdcArte______________________.__getArteInstanceInternal(),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblJ4WZRLJ23BGMBKQ2JBSOBEGBP4"),pkValsMap);
		try{
		return (
		org.radixware.ads.CfgManagement.server.CfgItem) org.radixware.ads.mdlPEKYFVDRVZHGZCBQQDY2NOYFOY.server.pdcArte______________________.__getArteInstanceInternal().getEntityObject(pid,null,checkExistance);
	}catch(org.radixware.kernel.server.exceptions.EntityObjectNotExistsError e){
	return null;
	}

	}

	/*Radix::CfgManagement::CfgItem:calcAllRefsState-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItem:calcAllRefsState")
	protected published  void calcAllRefsState () {
		RefState res = null;
		CfgItemRef refs[] = getAllRefs();
		if (refs != null) {
		    loop:
		    for (CfgItemRef r : refs) {
		        if (r == null)
		            continue;
		        if (r.type == RefType:Internal)
		            try {
		                if (r.intRef != null)
		                    r.intRef.getDdsMeta(); //access to check if broken
		            } catch (Exceptions::EntityObjectNotExistsError e) {
		                r.state = RefState:Broken;
		            }
		        if (r.state != null)
		            switch (r.state) {
		                case RefState:Broken:
		                    res = RefState:Broken;
		                    break loop;
		                case RefState:Ambiguity:
		                    res = RefState:Ambiguity;
		                    break;
		                default:
		                    if (res != RefState:Ambiguity)
		                        res = RefState:Ok;
		                    break;
		            }
		    }
		}
		allRefsState = res;
	}

	/*Radix::CfgManagement::CfgItem:getAllRefs-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItem:getAllRefs")
	protected published  org.radixware.ads.CfgManagement.server.CfgItemRef[] getAllRefs () {
		return null;

	}

	/*Radix::CfgManagement::CfgItem:link-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItem:link")
	protected published  void link () {
		objState = null;
		allRefsState = null;

		CfgItemRef refs[] = getAllRefs();
		if (refs != null)
		    for (CfgItemRef r : refs)
		        if (r != null)
		            r.link();
		  
		linkImpObject();

		calcAllRefsState();


	}

	/*Radix::CfgManagement::CfgItem:shouldSkip-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItem:shouldSkip")
	  boolean shouldSkip () {
		return skip == true || parent != null && parent.shouldSkip();

	}

	/*Radix::CfgManagement::CfgItem:onCommand_RelinkChildren-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItem:onCommand_RelinkChildren")
	public  void onCommand_RelinkChildren (org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValHandlersById) {
		linkChildren();
	}

	/*Radix::CfgManagement::CfgItem:linkChildren-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItem:linkChildren")
	protected published  void linkChildren () {
		CfgItemsCursor c = CfgItemsCursor.open(packetId, true, id);
		try {
		    while (c.next()) {
		        c.item.link();
		        c.item.linkChildren();
		    }
		} finally {
		    c.close();
		}


	}

	/*Radix::CfgManagement::CfgItem:getInternalDependencies-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItem:getInternalDependencies")
	protected published  java.util.List<org.radixware.ads.CfgManagement.server.CfgItem> getInternalDependencies () {
		return null;
	}

	/*Radix::CfgManagement::CfgItem:isEmptyItem-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItem:isEmptyItem")
	  boolean isEmptyItem () {
		return false;
	}

	/*Radix::CfgManagement::CfgItem:beforeActualizeExport-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItem:beforeActualizeExport")
	protected published  boolean beforeActualizeExport () {
		    return true;
	}

	/*Radix::CfgManagement::CfgItem:getSettings-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItem:getSettings")
	public  org.radixware.ads.CfgManagement.common.ICfgImportSettings getSettings () {
		if (settingsWrapper == null) {
		    settingsWrapper = new CfgImportSettingsWrapper(this);
		}
		return settingsWrapper;
	}

	/*Radix::CfgManagement::CfgItem:updateAppVersion-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItem:updateAppVersion")
	protected  void updateAppVersion () {

	}

	/*Radix::CfgManagement::CfgItem:getCfgReferenceExtGuid-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItem:getCfgReferenceExtGuid")
	public published  Str getCfgReferenceExtGuid () {
		return srcExtGuid;
	}

	/*Radix::CfgManagement::CfgItem:getCfgReferencePropId-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItem:getCfgReferencePropId")
	public published  org.radixware.kernel.common.types.Id getCfgReferencePropId () {
		return idof[CfgItem:srcExtGuid];
	}

	/*Radix::CfgManagement::CfgItem:export-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItem:export")
	 final  org.radixware.ads.CfgManagement.common.ImpExpXsd.Item export () {
		ImpExpXsd:Item xi = ImpExpXsd:Item.Factory.newInstance();
		xi.Id = id;
		xi.ClassId = getClassDefinitionId();
		xi.ObjectPid = srcObjectPid;
		xi.ObjectRid = srcObjectRid;
		xi.ObjectTitle = srcObjectTitle;
		xi.ObjectExtGuid = srcExtGuid;
		xi.Data = data;
		xi.Notes = notes;
		xi.Settings = settings != null ? settings.Settings : null;
		if (srcAppVer != null) {
		    xi.AppVer = srcAppVer;
		}

		CfgItemsCursor c = CfgItemsCursor.open(packetId, true, id);
		try {
		    while (c.next()) {
		        ImpExpXsd:Item ci = c.item.export();
		        xi.ensureChildren().ItemList.add(ci);
		    }
		} finally {
		    c.close();
		}

		return xi;


	}

	/*Radix::CfgManagement::CfgItem:import-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItem:import")
	 static  org.radixware.ads.CfgManagement.server.CfgItem import (org.radixware.ads.CfgManagement.common.ImpExpXsd.Item xi, org.radixware.ads.CfgManagement.server.CfgPacket.Import packet, org.radixware.ads.CfgManagement.server.CfgItem parent) {
		CfgItem item = (CfgItem) Arte::Arte.newObject(xi.ClassId);
		item.init();
		item.packet = packet;
		item.parent = parent;
		item.srcObjectPid = xi.ObjectPid;
		item.srcObjectRid = xi.ObjectRid;
		item.srcObjectTitle = xi.ObjectTitle;
		item.srcExtGuid = xi.ObjectExtGuid;
		item.notes = xi.Notes;
		if (xi.getSettings() != null && !xi.getSettings().isNil()) {
		    item.settings = ImpExpXsd:SettingsDocument.Factory.newInstance();
		    item.settings.addNewSettings().set(xi.getSettings());
		}
		item.srcAppVer = xi.isSetAppVer() ? xi.AppVer : null;

		if (xi.Data != null)
		    try {
		        item.data = Xml.Factory.parse(Utils::XmlObjectProcessor.getXmlObjectFirstChild(xi.Data).DomNode);
		    } catch (Exceptions::XmlException e) {
		        throw new AppError("Invalid data format:\n" + xi.Data.toString(), e);
		    }
		item.create();
		item.createRefs();

		if (xi.Children != null)
		    for (ImpExpXsd:Item ci : xi.Children.ItemList) {
		        CfgItem.import(ci, packet, item);
		    }

		return item;
	}




	@Override
	public org.radixware.kernel.server.types.FormHandler.NextDialogsRequest execCommand(org.radixware.kernel.common.types.Id cmdId, org.radixware.kernel.common.types.Id propId, org.apache.xmlbeans.XmlObject input, org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValsById, org.apache.xmlbeans.XmlObject output) throws org.radixware.kernel.common.exceptions.AppException,java.lang.InterruptedException{
		if(cmdId == cmdGSYHPNMK3JCPTD6MVPSWZKCYLA){
			onCommand_Relink(newPropValsById);
			return null;
		} else if(cmdId == cmdYSTUTAIG7FAIRMIPBQTCTOZOGQ){
			onCommand_RelinkChildren(newPropValsById);
			return null;
		} else 
			return super.execCommand(cmdId,propId,input,newPropValsById,output);
	}


}

/* Radix::CfgManagement::CfgItem - Server Meta*/

/*Radix::CfgManagement::CfgItem-Entity Class*/

package org.radixware.ads.CfgManagement.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class CfgItem_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ4WZRLJ23BGMBKQ2JBSOBEGBP4"),"CfgItem",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6SY6L3HI6JGJNIZAIOYXCHTCVQ"),

						org.radixware.kernel.common.enums.EClassType.ENTITY,

						/*Radix::CfgManagement::CfgItem:Presentations-Entity Presentations*/
						/*Class presentation attributes*/
						 new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef(/*Owner Class Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ4WZRLJ23BGMBKQ2JBSOBEGBP4"),
							/*Owner Class Name*/
							"CfgItem",
							/*Title Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6SY6L3HI6JGJNIZAIOYXCHTCVQ"),
							/*Property presentations*/

							/*Radix::CfgManagement::CfgItem:Properties-Properties*/
							new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef[]{

									/*Radix::CfgManagement::CfgItem:allRefsState:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colW3X3MBNTEBADJKAZZQ4QEOMSKY"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::CfgManagement::CfgItem:data:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJHVNNS3BSZCDHPM5MZET64JSGU"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::CfgManagement::CfgItem:parentId:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJ5L5OI3X4BHYBPJFGAWUO4MKZM"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::CfgManagement::CfgItem:objState:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colOY3R4QKX2NEEHMDIS6QV4FMDQE"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::CfgManagement::CfgItem:packetId:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colTUL7C3425VCCNC5ELKDFZBWJOY"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::CfgManagement::CfgItem:id:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIUQVQZZKPFBRLG7DTABUYGLYBQ"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::CfgManagement::CfgItem:skip:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJGXEDVUL4JA5JAKSJCAVSU2BSU"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::CfgManagement::CfgItem:srcObjectPid:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3DNFCTYB3ZDJZCRY4OGG7I66YA"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::CfgManagement::CfgItem:srcObjectTitle:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHUYIZ7QPTBE7FAGC4FA76QNHPY"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::CfgManagement::CfgItem:parent:PropertyPresentation-Parent Ref Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadParentTitlePropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colD6XH3IE6INFTHPWTRD6KMRUTBI"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,null,null,new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),org.radixware.kernel.server.types.Restrictions.Factory.newInstance(133693439,null,null,null),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprC6NQ7NSN2JDVHLEER72AUDY6II")},null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_SELECTOR)),

									/*Radix::CfgManagement::CfgItem:packet:PropertyPresentation-Parent Ref Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadParentTitlePropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXUWWBUGM6VAXXM4LRGA7BJRC7U"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,null,null,new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),org.radixware.kernel.server.types.Restrictions.Factory.newInstance(133693439,null,null,null),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprJHOQCOZIOBBWNABEIAT5XEG7M4")},null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_SELECTOR)),

									/*Radix::CfgManagement::CfgItem:notes:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKLNALIUEZJGQHKSQ22KH6YAQAI"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::CfgManagement::CfgItem:classTitle:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdY3N4QO6NSRDXTCHP27YWP3WONM"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::CfgManagement::CfgItem:hasChildren:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd2CKT7GHGJNAEJM42ENCONBLJOE"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::CfgManagement::CfgItem:srcObjectRid:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHQUFLZVQBNDCHMCVIG7RQM7NDQ"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::CfgManagement::CfgItem:settings:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colM4MU5QWZUVHADCVXB2GL47BQXI"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::CfgManagement::CfgItem:srcAppVer:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIRYZTYXM2NBDNOQQ5XMAGM3H6Y"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::CfgManagement::CfgItem:srcExtGuid:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKF2PFJVIG5FDZJLBPDQR2RN4GA"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT))
							},
							/*Commands*/
							new org.radixware.kernel.server.meta.presentations.RadCommandDef[]{

									new org.radixware.kernel.server.meta.presentations.RadCommandDef(
									/*Radix::CfgManagement::CfgItem:Relink-Object Command*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdGSYHPNMK3JCPTD6MVPSWZKCYLA"),"Relink",org.radixware.kernel.common.enums.ECommandScope.OBJECT,null,org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_FIXED,org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ4WZRLJ23BGMBKQ2JBSOBEGBP4"),false,true),

									new org.radixware.kernel.server.meta.presentations.RadCommandDef(
									/*Radix::CfgManagement::CfgItem:RelinkChildren-Object Command*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdYSTUTAIG7FAIRMIPBQTCTOZOGQ"),"RelinkChildren",org.radixware.kernel.common.enums.ECommandScope.OBJECT,null,org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_FIXED,org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ4WZRLJ23BGMBKQ2JBSOBEGBP4"),false,true)
							},
							/*Sortings*/
							new org.radixware.kernel.server.meta.presentations.RadSortingDef[]{

									new org.radixware.kernel.server.meta.presentations.RadSortingDef(
									/*Radix::CfgManagement::CfgItem:Id-Sorting*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("srtUSWUP7RXBJFXRMQNHD67PC2JIY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ4WZRLJ23BGMBKQ2JBSOBEGBP4"),"Id",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQPP64NALWBEBFHQCT3GQOJN5TQ"),new org.radixware.kernel.server.meta.presentations.RadSortingDef.Item[]{

											new org.radixware.kernel.server.meta.presentations.RadSortingDef.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIUQVQZZKPFBRLG7DTABUYGLYBQ"),org.radixware.kernel.common.enums.EOrder.ASC)
									},"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware")
							},
							/*Filters*/
							new org.radixware.kernel.server.meta.presentations.RadFilterDef[]{

									new org.radixware.kernel.server.meta.presentations.RadFilterDef(
									/*Radix::CfgManagement::CfgItem:NewObj-Filter*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("fltMZ5IEUI3RRB6HFDHHLHAFWJPAU"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ4WZRLJ23BGMBKQ2JBSOBEGBP4"),"NewObj",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6S2464ZPMFFB5GLBXRDHQ6KKFI"),"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:Sql>exists (\nselect 1 from </xsc:Sql></xsc:Item><xsc:Item><xsc:TableSqlName TableId=\"tblJ4WZRLJ23BGMBKQ2JBSOBEGBP4\"/></xsc:Item><xsc:Item><xsc:Sql>  \nwhere </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecJ4WZRLJ23BGMBKQ2JBSOBEGBP4\" PropId=\"colOY3R4QKX2NEEHMDIS6QV4FMDQE\" Owner=\"TABLE\"/></xsc:Item><xsc:Item><xsc:Sql> = </xsc:Sql></xsc:Item><xsc:Item><xsc:ConstValue EnumId=\"acsWJELU3IWQVEKXFS4OLX6INWGTY\" ItemId=\"aciI5CUH35A4VEJTKCMGUD2UDENTA\"/></xsc:Item><xsc:Item><xsc:Sql>\nstart with </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecJ4WZRLJ23BGMBKQ2JBSOBEGBP4\" PropId=\"colIUQVQZZKPFBRLG7DTABUYGLYBQ\" Owner=\"TABLE\"/></xsc:Item><xsc:Item><xsc:Sql> = </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecJ4WZRLJ23BGMBKQ2JBSOBEGBP4\" PropId=\"colIUQVQZZKPFBRLG7DTABUYGLYBQ\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> connect by </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecJ4WZRLJ23BGMBKQ2JBSOBEGBP4\" PropId=\"colJ5L5OI3X4BHYBPJFGAWUO4MKZM\" Owner=\"TABLE\"/></xsc:Item><xsc:Item><xsc:Sql> = prior </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecJ4WZRLJ23BGMBKQ2JBSOBEGBP4\" PropId=\"colIUQVQZZKPFBRLG7DTABUYGLYBQ\" Owner=\"TABLE\"/></xsc:Item><xsc:Item><xsc:Sql>\n)</xsc:Sql></xsc:Item></xsc:Sqml>",null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>",org.radixware.kernel.common.types.Id.Factory.loadFrom("srtUSWUP7RXBJFXRMQNHD67PC2JIY"),true,null,true,"org.radixware"),

									new org.radixware.kernel.server.meta.presentations.RadFilterDef(
									/*Radix::CfgManagement::CfgItem:Invalid-Filter*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("fltNIWKPNVHINEJBIOBJXX5HVQTNQ"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ4WZRLJ23BGMBKQ2JBSOBEGBP4"),"Invalid",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls73SIYHAFPRAU3A5IE3TAN5JEDA"),"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:Sql>exists (\nselect 1 from </xsc:Sql></xsc:Item><xsc:Item><xsc:TableSqlName TableId=\"tblJ4WZRLJ23BGMBKQ2JBSOBEGBP4\"/></xsc:Item><xsc:Item><xsc:Sql> \nwhere \n    </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecJ4WZRLJ23BGMBKQ2JBSOBEGBP4\" PropId=\"colW3X3MBNTEBADJKAZZQ4QEOMSKY\" Owner=\"TABLE\"/></xsc:Item><xsc:Item><xsc:Sql> &lt;> </xsc:Sql></xsc:Item><xsc:Item><xsc:ConstValue EnumId=\"acsUT33HR7H2BCG5NXG3J52FIV5AA\" ItemId=\"aciP5K5II6QT5BAFFRHUVLN3OEYGU\"/></xsc:Item><xsc:Item><xsc:Sql>\n    or \n    </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecJ4WZRLJ23BGMBKQ2JBSOBEGBP4\" PropId=\"colOY3R4QKX2NEEHMDIS6QV4FMDQE\" Owner=\"TABLE\"/></xsc:Item><xsc:Item><xsc:Sql> = </xsc:Sql></xsc:Item><xsc:Item><xsc:ConstValue EnumId=\"acsWJELU3IWQVEKXFS4OLX6INWGTY\" ItemId=\"aciIXJSDSZVTJDMBGMYADEPMDPIMU\"/></xsc:Item><xsc:Item><xsc:Sql>\nstart with </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecJ4WZRLJ23BGMBKQ2JBSOBEGBP4\" PropId=\"colIUQVQZZKPFBRLG7DTABUYGLYBQ\" Owner=\"TABLE\"/></xsc:Item><xsc:Item><xsc:Sql> = </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecJ4WZRLJ23BGMBKQ2JBSOBEGBP4\" PropId=\"colIUQVQZZKPFBRLG7DTABUYGLYBQ\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> connect by </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecJ4WZRLJ23BGMBKQ2JBSOBEGBP4\" PropId=\"colJ5L5OI3X4BHYBPJFGAWUO4MKZM\" Owner=\"TABLE\"/></xsc:Item><xsc:Item><xsc:Sql> = prior </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecJ4WZRLJ23BGMBKQ2JBSOBEGBP4\" PropId=\"colIUQVQZZKPFBRLG7DTABUYGLYBQ\" Owner=\"TABLE\"/></xsc:Item><xsc:Item><xsc:Sql>     \n)\n\n</xsc:Sql></xsc:Item></xsc:Sqml>",null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>",org.radixware.kernel.common.types.Id.Factory.loadFrom("srtUSWUP7RXBJFXRMQNHD67PC2JIY"),true,null,true,"org.radixware"),

									new org.radixware.kernel.server.meta.presentations.RadFilterDef(
									/*Radix::CfgManagement::CfgItem:ByExtGuid-Filter*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("flt7HADR6IUNJCAXCROQ6V2PCLNJM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ4WZRLJ23BGMBKQ2JBSOBEGBP4"),"ByExtGuid",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsA7XD4C2TRJCRLG7SF5QLNPHLWM"),"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:Sql>(</xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmG754KGK6GFCPHNAU2OY7BPT2EE\"/></xsc:Item><xsc:Item><xsc:Sql> is null and </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecJ4WZRLJ23BGMBKQ2JBSOBEGBP4\" PropId=\"colKF2PFJVIG5FDZJLBPDQR2RN4GA\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> is null) \nor  \n(</xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmG754KGK6GFCPHNAU2OY7BPT2EE\"/></xsc:Item><xsc:Item><xsc:Sql> is not null and </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecJ4WZRLJ23BGMBKQ2JBSOBEGBP4\" PropId=\"colKF2PFJVIG5FDZJLBPDQR2RN4GA\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> like \'%\' || </xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmG754KGK6GFCPHNAU2OY7BPT2EE\"/></xsc:Item><xsc:Item><xsc:Sql> || \'%\')</xsc:Sql></xsc:Item></xsc:Sqml>",null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>",org.radixware.kernel.common.types.Id.Factory.loadFrom("srtUSWUP7RXBJFXRMQNHD67PC2JIY"),true,null,false,"org.radixware")
							},
							/*Editor presentations*/
							new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef[]{

									new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef(
									/*Radix::CfgManagement::CfgItem:Export-Editor Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprCUUYK4MIYVFVLCLQGXLUNZWINM"),
									"Export",
									org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprC6NQ7NSN2JDVHLEER72AUDY6II"),
									40114,
									null,

									/*Radix::CfgManagement::CfgItem:Export:Children-Explorer Items*/
										new org.radixware.kernel.server.meta.presentations.RadExplorerItemDef[]{

												/*Radix::CfgManagement::CfgItem:Export:Children-Child Ref Explorer Item*/

												new org.radixware.kernel.server.meta.presentations.RadChildRefExplorerItemDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiVICI3JOSCZFBLNFNTHVZJO3FYE"),
													org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ4WZRLJ23BGMBKQ2JBSOBEGBP4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ4WZRLJ23BGMBKQ2JBSOBEGBP4"),
													org.radixware.kernel.common.types.Id.Factory.loadFrom("spr4SSGPHYJVRAGLB7HGLSAGIAMIQ"),
													new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),
													org.radixware.kernel.common.types.Id.Factory.loadFrom("refSAINKHTF3ZBCZGDGBCAMV4P76E"),
													null,
													null)
										}
									,
									null,
									(org.radixware.kernel.server.meta.presentations.RadPropertyPresentationAttributes[]) null,
									org.radixware.kernel.common.enums.EEditorPresentationRightsInheritanceMode.NONE,
									null),

									new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef(
									/*Radix::CfgManagement::CfgItem:Import-Editor Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprO3XHJ6U74NDNHJ7GNTDUUNDEWE"),
									"Import",
									org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprC6NQ7NSN2JDVHLEER72AUDY6II"),
									40114,
									null,

									/*Radix::CfgManagement::CfgItem:Import:Children-Explorer Items*/
										new org.radixware.kernel.server.meta.presentations.RadExplorerItemDef[]{

												/*Radix::CfgManagement::CfgItem:Import:Children-Child Ref Explorer Item*/

												new org.radixware.kernel.server.meta.presentations.RadChildRefExplorerItemDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiB5VEHK7NEBD4LEO6LR3MOP3XS4"),
													org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ4WZRLJ23BGMBKQ2JBSOBEGBP4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ4WZRLJ23BGMBKQ2JBSOBEGBP4"),
													org.radixware.kernel.common.types.Id.Factory.loadFrom("sprE6NT4GPHVREKJJMNK6YDGWAZUQ"),
													new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),
													org.radixware.kernel.common.types.Id.Factory.loadFrom("refSAINKHTF3ZBCZGDGBCAMV4P76E"),
													null,
													null)
										}
									,
									null,
									(org.radixware.kernel.server.meta.presentations.RadPropertyPresentationAttributes[]) null,
									org.radixware.kernel.common.enums.EEditorPresentationRightsInheritanceMode.NONE,
									null),

									new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef(
									/*Radix::CfgManagement::CfgItem:Base-Editor Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprC6NQ7NSN2JDVHLEER72AUDY6II"),
									"Base",
									org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
									null,
									2192,
									null,

									/*Radix::CfgManagement::CfgItem:Base:Children-Explorer Items*/
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
									/*Radix::CfgManagement::CfgItem:Export-Selector Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("spr4SSGPHYJVRAGLB7HGLSAGIAMIQ"),"Export",org.radixware.kernel.common.types.Id.Factory.loadFrom("spr4HURLRTA4JFT7KQTNLYMG3C63I"),16531,new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn[]{

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIUQVQZZKPFBRLG7DTABUYGLYBQ"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdY3N4QO6NSRDXTCHP27YWP3WONM"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKF2PFJVIG5FDZJLBPDQR2RN4GA"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHUYIZ7QPTBE7FAGC4FA76QNHPY"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIRYZTYXM2NBDNOQQ5XMAGM3H6Y"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKLNALIUEZJGQHKSQ22KH6YAQAI"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd2CKT7GHGJNAEJM42ENCONBLJOE"),false)
									},null,new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.Addons(org.radixware.kernel.common.types.Id.Factory.loadFrom("srtUSWUP7RXBJFXRMQNHD67PC2JIY"),true,null,false,null,false,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("flt7HADR6IUNJCAXCROQ6V2PCLNJM")},false,true,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprCUUYK4MIYVFVLCLQGXLUNZWINM")},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprCUUYK4MIYVFVLCLQGXLUNZWINM")},org.radixware.kernel.server.types.Restrictions.Factory.newInstance(278529,null,null,null),null),

									new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef(
									/*Radix::CfgManagement::CfgItem:Import-Selector Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("sprE6NT4GPHVREKJJMNK6YDGWAZUQ"),"Import",org.radixware.kernel.common.types.Id.Factory.loadFrom("spr4HURLRTA4JFT7KQTNLYMG3C63I"),16531,new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn[]{

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIUQVQZZKPFBRLG7DTABUYGLYBQ"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdY3N4QO6NSRDXTCHP27YWP3WONM"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKF2PFJVIG5FDZJLBPDQR2RN4GA"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHUYIZ7QPTBE7FAGC4FA76QNHPY"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIRYZTYXM2NBDNOQQ5XMAGM3H6Y"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKLNALIUEZJGQHKSQ22KH6YAQAI"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJGXEDVUL4JA5JAKSJCAVSU2BSU"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colW3X3MBNTEBADJKAZZQ4QEOMSKY"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colOY3R4QKX2NEEHMDIS6QV4FMDQE"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd2CKT7GHGJNAEJM42ENCONBLJOE"),false)
									},null,new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.Addons(org.radixware.kernel.common.types.Id.Factory.loadFrom("srtUSWUP7RXBJFXRMQNHD67PC2JIY"),true,null,false,null,true,null,false,true,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprO3XHJ6U74NDNHJ7GNTDUUNDEWE")},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprO3XHJ6U74NDNHJ7GNTDUUNDEWE")},org.radixware.kernel.server.types.Restrictions.Factory.newInstance(278529,null,null,null),null),

									new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef(
									/*Radix::CfgManagement::CfgItem:BaseTree-Selector Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("spr4HURLRTA4JFT7KQTNLYMG3C63I"),"BaseTree",org.radixware.kernel.common.types.Id.Factory.loadFrom("spr2PZI4TZ34JEQNKVR5AXO6PWPNI"),16562,new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn[]{

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIUQVQZZKPFBRLG7DTABUYGLYBQ"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJ5L5OI3X4BHYBPJFGAWUO4MKZM"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd2CKT7GHGJNAEJM42ENCONBLJOE"),false),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdY3N4QO6NSRDXTCHP27YWP3WONM"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHUYIZ7QPTBE7FAGC4FA76QNHPY"),true)
									},new org.radixware.kernel.server.meta.presentations.RadConditionDef("<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:PropSqlName TableId=\"agcJ4WZRLJ23BGMBKQ2JBSOBEGBP4\" PropId=\"pgpY3JT7QYNN5ATNNCBXPNCPTDFB4\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> is null or </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecJ4WZRLJ23BGMBKQ2JBSOBEGBP4\" PropId=\"colTUL7C3425VCCNC5ELKDFZBWJOY\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> = </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"agcJ4WZRLJ23BGMBKQ2JBSOBEGBP4\" PropId=\"pgpY3JT7QYNN5ATNNCBXPNCPTDFB4\" Owner=\"THIS\"/></xsc:Item></xsc:Sqml>","<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.Addons(org.radixware.kernel.common.types.Id.Factory.loadFrom("srtUSWUP7RXBJFXRMQNHD67PC2JIY"),false,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("srtUSWUP7RXBJFXRMQNHD67PC2JIY")},false,null,false,new org.radixware.kernel.common.types.Id[]{},false,false,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprC6NQ7NSN2JDVHLEER72AUDY6II")},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprC6NQ7NSN2JDVHLEER72AUDY6II")},null,null),

									new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef(
									/*Radix::CfgManagement::CfgItem:Base-Selector Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("spr2PZI4TZ34JEQNKVR5AXO6PWPNI"),"Base",null,144,new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn[]{

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIUQVQZZKPFBRLG7DTABUYGLYBQ"),true)
									},new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.Addons(null,false,new org.radixware.kernel.common.types.Id[]{},false,null,false,new org.radixware.kernel.common.types.Id[]{},false,false,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprC6NQ7NSN2JDVHLEER72AUDY6II")},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprC6NQ7NSN2JDVHLEER72AUDY6II")},org.radixware.kernel.server.types.Restrictions.Factory.newInstance(32,null,null,null),null),

									new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef(
									/*Radix::CfgManagement::CfgItem:InternalLinks-Selector Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("sprMQEXFCGPZJBLVA7BZDI5QHQKRY"),"InternalLinks",org.radixware.kernel.common.types.Id.Factory.loadFrom("spr2PZI4TZ34JEQNKVR5AXO6PWPNI"),16561,new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn[]{

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIUQVQZZKPFBRLG7DTABUYGLYBQ"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdY3N4QO6NSRDXTCHP27YWP3WONM"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3DNFCTYB3ZDJZCRY4OGG7I66YA"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHUYIZ7QPTBE7FAGC4FA76QNHPY"),true)
									},null,new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.Addons(org.radixware.kernel.common.types.Id.Factory.loadFrom("srtUSWUP7RXBJFXRMQNHD67PC2JIY"),true,null,false,null,true,null,false,true,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprO3XHJ6U74NDNHJ7GNTDUUNDEWE")},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprO3XHJ6U74NDNHJ7GNTDUUNDEWE")},null,null)
							},
							/*Default selector presentation Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("spr2PZI4TZ34JEQNKVR5AXO6PWPNI"),
							/*Presentation Map*/
							null,
							/*Object title format*/

							/*Radix::CfgManagement::CfgItem:TitleFormat-Object Title Format*/

							new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ4WZRLJ23BGMBKQ2JBSOBEGBP4"),new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.TitleItem[]{

									new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.TitleItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ4WZRLJ23BGMBKQ2JBSOBEGBP4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colIUQVQZZKPFBRLG7DTABUYGLYBQ"),"{0}) ",org.radixware.kernel.common.enums.ETitleNullFormat.SAME_AS_NOT_NULL,null),

									new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.TitleItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ4WZRLJ23BGMBKQ2JBSOBEGBP4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("prdY3N4QO6NSRDXTCHP27YWP3WONM"),"{0}: ",org.radixware.kernel.common.enums.ETitleNullFormat.SAME_AS_NOT_NULL,null),

									new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.TitleItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ4WZRLJ23BGMBKQ2JBSOBEGBP4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colHUYIZ7QPTBE7FAGC4FA76QNHPY"),"{0}",org.radixware.kernel.common.enums.ETitleNullFormat.SAME_AS_NOT_NULL,null)
							},null,org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.DefinitionContextType.ENTITY),
							/*Class catalogs*/
							null,
							/*Restrictions*/
							org.radixware.kernel.server.types.Restrictions.Factory.newInstance(0,null,null,null),null,0),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblJ4WZRLJ23BGMBKQ2JBSOBEGBP4"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("pdcEntity____________________"),

						/*Radix::CfgManagement::CfgItem:Properties-Properties*/
						new org.radixware.kernel.server.meta.clazzes.RadPropDef[]{

								/*Radix::CfgManagement::CfgItem:allRefsState-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colW3X3MBNTEBADJKAZZQ4QEOMSKY"),"allRefsState",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAN46CQAYZBGSTLC7OGX5OMSPHU"),org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("acsUT33HR7H2BCG5NXG3J52FIV5AA"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::CfgManagement::CfgItem:data-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJHVNNS3BSZCDHPM5MZET64JSGU"),"data",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4O4JVRF2SZEPJFFIAOZILXAQDQ"),org.radixware.kernel.common.enums.EValType.XML,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::CfgManagement::CfgItem:classGuid-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJQK7LYSZW5FS7NYEXFUKCIR4O4"),"classGuid",null,org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::CfgManagement::CfgItem:parentId-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJ5L5OI3X4BHYBPJFGAWUO4MKZM"),"parentId",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsABQRNFHXYVB7FEVP36PUEISEAQ"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::CfgManagement::CfgItem:objState-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colOY3R4QKX2NEEHMDIS6QV4FMDQE"),"objState",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQTUICRWBPVASTJHSPIAROKHM4Y"),org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("acsWJELU3IWQVEKXFS4OLX6INWGTY"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::CfgManagement::CfgItem:packetId-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colTUL7C3425VCCNC5ELKDFZBWJOY"),"packetId",null,org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::CfgManagement::CfgItem:id-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIUQVQZZKPFBRLG7DTABUYGLYBQ"),"id",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDFKMMSN4INAUDCNMBXFAA2INFM"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::CfgManagement::CfgItem:skip-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJGXEDVUL4JA5JAKSJCAVSU2BSU"),"skip",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsS2RWDNGY2VGTHNTKDR3GR26LLU"),org.radixware.kernel.common.enums.EValType.BOOL,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("0")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::CfgManagement::CfgItem:srcObjectPid-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3DNFCTYB3ZDJZCRY4OGG7I66YA"),"srcObjectPid",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTL4GYON4IVAA3FQLTLPV2DMDD4"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::CfgManagement::CfgItem:srcObjectTitle-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHUYIZ7QPTBE7FAGC4FA76QNHPY"),"srcObjectTitle",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJHNMAVJLAJCQ5LK2PMKMS4VYGI"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::CfgManagement::CfgItem:parent-Parent Reference*/

								new org.radixware.kernel.server.meta.clazzes.RadInnateRefPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colD6XH3IE6INFTHPWTRD6KMRUTBI"),"parent",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOZLSBAP3TZDDBM4AP5RUEGAIJ4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("refSAINKHTF3ZBCZGDGBCAMV4P76E"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ4WZRLJ23BGMBKQ2JBSOBEGBP4"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::CfgManagement::CfgItem:packet-Parent Reference*/

								new org.radixware.kernel.server.meta.clazzes.RadInnateRefPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXUWWBUGM6VAXXM4LRGA7BJRC7U"),"packet",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTHUGRUSSAFBAHPO3KAY6IGRPUA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("refF34ZWBF4TBBCHIUDOFBRI67OVY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecWMN3P5J5ONCFPLJVGBFAKZXZXU"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::CfgManagement::CfgItem:notes-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKLNALIUEZJGQHKSQ22KH6YAQAI"),"notes",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCSLRLP6FJZCK7ALAZ4IO7W7OIY"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::CfgManagement::CfgItem:classTitle-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdY3N4QO6NSRDXTCHP27YWP3WONM"),"classTitle",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQBOW4TDPKZBXPASLYA7JL2WMJM"),org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadAccessorProxy.R_ACCESSOR_INSTANCE),

								/*Radix::CfgManagement::CfgItem:hasChildren-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd2CKT7GHGJNAEJM42ENCONBLJOE"),"hasChildren",null,org.radixware.kernel.common.enums.EValType.BOOL,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadAccessorProxy.R_ACCESSOR_INSTANCE),

								/*Radix::CfgManagement::CfgItem:importPacket-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicRefPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd4T2CN4H7S5B5VKWED6GMVAYI2I"),"importPacket",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMGGRKV6IA5DBXJFVXV36IS3YIQ"),org.radixware.kernel.common.enums.EValType.USER_CLASS,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("acl6YTUW6RSTFHKHO3LGTUIADUAT4"),org.radixware.kernel.server.meta.clazzes.RadPropReadAccessorProxy.R_ACCESSOR_INSTANCE),

								/*Radix::CfgManagement::CfgItem:srcObjectRid-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHQUFLZVQBNDCHMCVIG7RQM7NDQ"),"srcObjectRid",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4HPWC5L7GVA7PISXIBJJZE5YEY"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::CfgManagement::CfgItem:settings-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colM4MU5QWZUVHADCVXB2GL47BQXI"),"settings",null,org.radixware.kernel.common.enums.EValType.XML,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::CfgManagement::CfgItem:settingsWrapper-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdIQCM5XQA6NCTLNWD26TOH67DPE"),"settingsWrapper",null,org.radixware.kernel.common.enums.EValType.USER_CLASS,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::CfgManagement::CfgItem:srcAppVer-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIRYZTYXM2NBDNOQQ5XMAGM3H6Y"),"srcAppVer",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCOKELC4X3ZCMDNCTO3YSWRZ45Y"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::CfgManagement::CfgItem:srcExtGuid-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKF2PFJVIG5FDZJLBPDQR2RN4GA"),"srcExtGuid",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsI7INV5WLYNDYXPBVLJQS6STZGA"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE)
						},

						/*Radix::CfgManagement::CfgItem:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthEBTC42HN7RENPIGWWSTFCC2IOQ"),"linkImpObject",false,true,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthcmdGSYHPNMK3JCPTD6MVPSWZKCYLA"),"onCommand_Relink",false,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("newPropValHandlersById",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprFQPIRKIMURDLNAF6XFEGFQS6EE"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth7Z323JY4JFB7PAHXRDBVOX2EE4"),"extractExportData",false,true,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("data",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprPYLAMXSMVNHQZG5U5MWWFZXYTE"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth6WZPXBGL5FDDJCGLBKX67LUMRA"),"actualizeExport",false,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("provider",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprC5YN5GECKFEP3ELVMO6OYHWHIY"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthHOVIQBNNZRERVBNIKFD56Y6OK4"),"load",false,true,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("helper",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprT6WEWXAL25C73DTDUINDI26GZQ"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth5W3TRDKMDRCGFJYNWSP72FQKSE"),"afterUpdate",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth6KS7LCP4W5AW5OCCJHH5KGO4YQ"),"afterCreate",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("src",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprXN4SL4FDFZHFHMCPZKVTHRF6GM"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthKNZ5Z7AS6RFRPIX6ZD5JDKEC34"),"beforeDelete",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthSUD4BBW5H5AH7GNAMZP26U34CA"),"createRefs",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth_loadByPidStr_____________"),"loadByPidStr",true,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("pidAsStr",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprPidAsStr__________________")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("checkExistance",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprCheckExisstance___________"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth_loadByPK_________________"),"loadByPK",true,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("id",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprIUQVQZZKPFBRLG7DTABUYGLYBQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("checkExistance",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprCheckExisstance___________"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth5CX3E2B6VVDZZPYVZI3N4A2DCY"),"calcAllRefsState",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthAVXV247UWFG5DE5CKM4NCJZN2E"),"getAllRefs",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth23CTGQ2WX5A6HBLYXEDIE7LPTU"),"link",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthERW2EFYKVVH7FPL45QREPH77WU"),"shouldSkip",false,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthcmdYSTUTAIG7FAIRMIPBQTCTOZOGQ"),"onCommand_RelinkChildren",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("newPropValHandlersById",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprMUJAOW3HPFBFBDH6Q2IRGGWLII"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthWBGDEWH5XJHLPDZHKX7QY7NXUI"),"linkChildren",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthWHLKG554SZFZPC4OANQK7CFRRU"),"getInternalDependencies",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,org.radixware.kernel.common.enums.EValType.JAVA_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthGLHCTQ732VCBNFCNV6TRJYYX6M"),"isEmptyItem",false,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthW7BLKGMLTZHK7JTSGZZNDI4UXY"),"beforeActualizeExport",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth33MAT5WMZJGBBIFRNUGDFLNBVA"),"getSettings",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthXN4P2IHDOVHEVFNPVWBNKNLK4Y"),"updateAppVersion",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthQXRW7PZ4QBBEJAJL57JFPES7EQ"),"getCfgReferenceExtGuid",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthTIFZEEQC2NEUNPFIRAGRH2B7EY"),"getCfgReferencePropId",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.JAVA_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthNVJDYFV7XNAJHGYR66OY66LJWQ"),"export",false,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,null,org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthHEFALT2SYJHYNL27DBSDW5JDSE"),"import",true,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("xi",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr3UBNLG53OBBJNHDB5AKHSFFNBA")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("packet",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprJQEOBMNFQFCGBBUWG42ZGJLNS4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("parent",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprFHM2FBC5CRDQDGDNJHXMCWJTAM"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS)
						},
						null,
						org.radixware.kernel.common.enums.EAccessAreaType.NONE,null,null,false);
}

/* Radix::CfgManagement::CfgItem - Desktop Executable*/

/*Radix::CfgManagement::CfgItem-Entity Class*/

package org.radixware.ads.CfgManagement.explorer;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItem")
public interface CfgItem {
	/*Group model adapter class. This is the base class for 
	all of selector presentation models of this entity*/
	public static class DefaultGroupModel extends org.radixware.kernel.common.client.models.GroupModel{
		public DefaultGroupModel(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadSelectorPresentationDef def){super(userSession,def);}

		/*Radix::CfgManagement::CfgItemGroup:packetIdParam:packetIdParam-Presentation Property*/




		public class PacketIdParam extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
			public PacketIdParam(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
			@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItemGroup:packetIdParam:packetIdParam")
			public  Int getValue() {
				return Value;
			}

			@SuppressWarnings({"unused","unchecked"})
			@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItemGroup:packetIdParam:packetIdParam")
			public   void setValue(Int val) {
				Value = val;
			}
		}
		public PacketIdParam getPacketIdParam(){return (PacketIdParam)getProperty(pgpY3JT7QYNN5ATNNCBXPNCPTDFB4);}

		/*Radix::CfgManagement::CfgItemGroup:parentClassGuid:parentClassGuid-Presentation Property*/




		public class ParentClassGuid extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
			public ParentClassGuid(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
			@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItemGroup:parentClassGuid:parentClassGuid")
			public  Str getValue() {
				return Value;
			}

			@SuppressWarnings({"unused","unchecked"})
			@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItemGroup:parentClassGuid:parentClassGuid")
			public   void setValue(Str val) {
				Value = val;
			}
		}
		public ParentClassGuid getParentClassGuid(){return (ParentClassGuid)getProperty(pgpXQE2DLBEPNCATJZGJOIWVMIL2U);}









		public org.radixware.ads.CfgManagement.explorer.CfgItem.CfgItem_DefaultModel getEntity(int i) throws InterruptedException, org.radixware.kernel.common.client.exceptions.BrokenEntityObjectException, org.radixware.kernel.common.exceptions.ServiceClientException { return (org.radixware.ads.CfgManagement.explorer.CfgItem.CfgItem_DefaultModel )  super.getEntity(i);}
	}
























































































































	/*Radix::CfgManagement::CfgItem:srcObjectPid:srcObjectPid-Presentation Property*/


	public class SrcObjectPid extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public SrcObjectPid(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItem:srcObjectPid:srcObjectPid")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItem:srcObjectPid:srcObjectPid")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public SrcObjectPid getSrcObjectPid();
	/*Radix::CfgManagement::CfgItem:parent:parent-Presentation Property*/


	public class Parent extends org.radixware.kernel.common.client.models.items.properties.PropertyRef{
		public Parent(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadParentRefPropertyDef def){
			super(model,def);
		}
		@Override
		public 
		org.radixware.ads.CfgManagement.explorer.CfgItem.CfgItem_DefaultModel openEntityModel()  throws org.radixware.kernel.common.exceptions.ServiceClientException, InterruptedException{
		     return (org.radixware.ads.CfgManagement.explorer.CfgItem.CfgItem_DefaultModel)super.openEntityModel();
		}
		@Override
		public 
		org.radixware.ads.CfgManagement.explorer.CfgItem.DefaultGroupModel openGroupModel(){
		     return (org.radixware.ads.CfgManagement.explorer.CfgItem.DefaultGroupModel)super.openGroupModel();
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.client.types.Reference dummy = ((org.radixware.kernel.common.client.types.Reference)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItem:parent:parent")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItem:parent:parent")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public Parent getParent();
	/*Radix::CfgManagement::CfgItem:srcObjectRid:srcObjectRid-Presentation Property*/


	public class SrcObjectRid extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public SrcObjectRid(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItem:srcObjectRid:srcObjectRid")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItem:srcObjectRid:srcObjectRid")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public SrcObjectRid getSrcObjectRid();
	/*Radix::CfgManagement::CfgItem:srcObjectTitle:srcObjectTitle-Presentation Property*/


	public class SrcObjectTitle extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public SrcObjectTitle(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItem:srcObjectTitle:srcObjectTitle")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItem:srcObjectTitle:srcObjectTitle")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public SrcObjectTitle getSrcObjectTitle();
	/*Radix::CfgManagement::CfgItem:srcAppVer:srcAppVer-Presentation Property*/


	public class SrcAppVer extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public SrcAppVer(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItem:srcAppVer:srcAppVer")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItem:srcAppVer:srcAppVer")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public SrcAppVer getSrcAppVer();
	/*Radix::CfgManagement::CfgItem:id:id-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItem:id:id")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItem:id:id")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public Id getId();
	/*Radix::CfgManagement::CfgItem:parentId:parentId-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItem:parentId:parentId")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItem:parentId:parentId")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public ParentId getParentId();
	/*Radix::CfgManagement::CfgItem:skip:skip-Presentation Property*/


	public class Skip extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public Skip(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItem:skip:skip")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItem:skip:skip")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public Skip getSkip();
	/*Radix::CfgManagement::CfgItem:data:data-Presentation Property*/


	public class Data extends org.radixware.kernel.common.client.models.items.properties.PropertyXml{
		public Data(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Class<org.apache.xmlbeans.XmlObject> getValClass(){
			return org.apache.xmlbeans.XmlObject.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.apache.xmlbeans.XmlObject dummy = x == null ? null : (org.apache.xmlbeans.XmlObject)org.radixware.kernel.common.utils.XmlObjectProcessor.castToXmlClass(getClass().getClassLoader(),(org.apache.xmlbeans.XmlObject)x,org.apache.xmlbeans.XmlObject.class);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItem:data:data")
		public  org.apache.xmlbeans.XmlObject getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItem:data:data")
		public   void setValue(org.apache.xmlbeans.XmlObject val) {
			Value = val;
		}
	}
	public Data getData();
	/*Radix::CfgManagement::CfgItem:srcExtGuid:srcExtGuid-Presentation Property*/


	public class SrcExtGuid extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public SrcExtGuid(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItem:srcExtGuid:srcExtGuid")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItem:srcExtGuid:srcExtGuid")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public SrcExtGuid getSrcExtGuid();
	/*Radix::CfgManagement::CfgItem:notes:notes-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItem:notes:notes")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItem:notes:notes")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Notes getNotes();
	/*Radix::CfgManagement::CfgItem:settings:settings-Presentation Property*/


	public class Settings extends org.radixware.kernel.common.client.models.items.properties.PropertyXml{
		public Settings(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Class<org.radixware.ads.CfgManagement.common.ImpExpXsd.SettingsDocument> getValClass(){
			return org.radixware.ads.CfgManagement.common.ImpExpXsd.SettingsDocument.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.ads.CfgManagement.common.ImpExpXsd.SettingsDocument dummy = x == null ? null : (org.radixware.ads.CfgManagement.common.ImpExpXsd.SettingsDocument)org.radixware.kernel.common.utils.XmlObjectProcessor.castToXmlClass(getClass().getClassLoader(),(org.apache.xmlbeans.XmlObject)x,org.radixware.ads.CfgManagement.common.ImpExpXsd.SettingsDocument.class);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItem:settings:settings")
		public  org.radixware.ads.CfgManagement.common.ImpExpXsd.SettingsDocument getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItem:settings:settings")
		public   void setValue(org.radixware.ads.CfgManagement.common.ImpExpXsd.SettingsDocument val) {
			Value = val;
		}
	}
	public Settings getSettings();
	/*Radix::CfgManagement::CfgItem:objState:objState-Presentation Property*/


	public class ObjState extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public ObjState(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public void setServerValue(org.radixware.kernel.common.client.models.items.properties.PropertyValue val){
			Object x = val.getValue();
			org.radixware.ads.CfgManagement.common.CfgObjState dummy = x == null ? null : (x instanceof org.radixware.ads.CfgManagement.common.CfgObjState ? (org.radixware.ads.CfgManagement.common.CfgObjState)x : org.radixware.ads.CfgManagement.common.CfgObjState.getForValue((Str)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.STR)));
			val.refineValue(dummy);
			super.setServerValue(val);
		}
		@Override
		public Class<org.radixware.ads.CfgManagement.common.CfgObjState> getValClass(){
			return org.radixware.ads.CfgManagement.common.CfgObjState.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.ads.CfgManagement.common.CfgObjState dummy = x == null ? null : (x instanceof org.radixware.ads.CfgManagement.common.CfgObjState ? (org.radixware.ads.CfgManagement.common.CfgObjState)x : org.radixware.ads.CfgManagement.common.CfgObjState.getForValue((Str)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.STR)));
			setValue(dummy);
		}





				














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItem:objState:objState")
		public  org.radixware.ads.CfgManagement.common.CfgObjState getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItem:objState:objState")
		public   void setValue(org.radixware.ads.CfgManagement.common.CfgObjState val) {
			Value = val;
		}
	}
	public ObjState getObjState();
	/*Radix::CfgManagement::CfgItem:packetId:packetId-Presentation Property*/


	public class PacketId extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public PacketId(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItem:packetId:packetId")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItem:packetId:packetId")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public PacketId getPacketId();
	/*Radix::CfgManagement::CfgItem:allRefsState:allRefsState-Presentation Property*/


	public class AllRefsState extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public AllRefsState(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public void setServerValue(org.radixware.kernel.common.client.models.items.properties.PropertyValue val){
			Object x = val.getValue();
			org.radixware.ads.CfgManagement.common.RefState dummy = x == null ? null : (x instanceof org.radixware.ads.CfgManagement.common.RefState ? (org.radixware.ads.CfgManagement.common.RefState)x : org.radixware.ads.CfgManagement.common.RefState.getForValue((Str)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.STR)));
			val.refineValue(dummy);
			super.setServerValue(val);
		}
		@Override
		public Class<org.radixware.ads.CfgManagement.common.RefState> getValClass(){
			return org.radixware.ads.CfgManagement.common.RefState.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.ads.CfgManagement.common.RefState dummy = x == null ? null : (x instanceof org.radixware.ads.CfgManagement.common.RefState ? (org.radixware.ads.CfgManagement.common.RefState)x : org.radixware.ads.CfgManagement.common.RefState.getForValue((Str)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.STR)));
			setValue(dummy);
		}





				














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItem:allRefsState:allRefsState")
		public  org.radixware.ads.CfgManagement.common.RefState getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItem:allRefsState:allRefsState")
		public   void setValue(org.radixware.ads.CfgManagement.common.RefState val) {
			Value = val;
		}
	}
	public AllRefsState getAllRefsState();
	/*Radix::CfgManagement::CfgItem:packet:packet-Presentation Property*/


	public class Packet extends org.radixware.kernel.common.client.models.items.properties.PropertyRef{
		public Packet(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadParentRefPropertyDef def){
			super(model,def);
		}
		@Override
		public 
		org.radixware.ads.CfgManagement.explorer.CfgPacket.CfgPacket_DefaultModel openEntityModel()  throws org.radixware.kernel.common.exceptions.ServiceClientException, InterruptedException{
		     return (org.radixware.ads.CfgManagement.explorer.CfgPacket.CfgPacket_DefaultModel)super.openEntityModel();
		}
		@Override
		public 
		org.radixware.ads.CfgManagement.explorer.CfgPacket.DefaultGroupModel openGroupModel(){
		     return (org.radixware.ads.CfgManagement.explorer.CfgPacket.DefaultGroupModel)super.openGroupModel();
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.client.types.Reference dummy = ((org.radixware.kernel.common.client.types.Reference)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItem:packet:packet")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItem:packet:packet")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public Packet getPacket();
	/*Radix::CfgManagement::CfgItem:hasChildren:hasChildren-Presentation Property*/


	public class HasChildren extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public HasChildren(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItem:hasChildren:hasChildren")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItem:hasChildren:hasChildren")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public HasChildren getHasChildren();
	/*Radix::CfgManagement::CfgItem:classTitle:classTitle-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItem:classTitle:classTitle")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItem:classTitle:classTitle")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ClassTitle getClassTitle();
	public static class Relink extends org.radixware.kernel.common.client.models.items.Command{
		protected Relink(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public void send() throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,null);
			processResponse(response, null);
		}

	}

	public static class RelinkChildren extends org.radixware.kernel.common.client.models.items.Command{
		protected RelinkChildren(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public void send() throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,null);
			processResponse(response, null);
		}

	}



}

/* Radix::CfgManagement::CfgItem - Desktop Meta*/

/*Radix::CfgManagement::CfgItem-Entity Class*/

package org.radixware.ads.CfgManagement.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class CfgItem_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::CfgManagement::CfgItem:Presentations-Entity Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ4WZRLJ23BGMBKQ2JBSOBEGBP4"),
			"Radix::CfgManagement::CfgItem",
			null,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("spr2PZI4TZ34JEQNKVR5AXO6PWPNI"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6SY6L3HI6JGJNIZAIOYXCHTCVQ"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZV2E2CQV3ZAP7NL6Q5IZLENPQQ"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6SY6L3HI6JGJNIZAIOYXCHTCVQ"),0,

			/*Radix::CfgManagement::CfgItem:Properties-Properties*/
			new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

					/*Radix::CfgManagement::CfgItem:allRefsState:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colW3X3MBNTEBADJKAZZQ4QEOMSKY"),
						"allRefsState",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAN46CQAYZBGSTLC7OGX5OMSPHU"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ4WZRLJ23BGMBKQ2JBSOBEGBP4"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acsUT33HR7H2BCG5NXG3J52FIV5AA"),
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

						/*Radix::CfgManagement::CfgItem:allRefsState:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acsUT33HR7H2BCG5NXG3J52FIV5AA"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.NONE,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::CfgManagement::CfgItem:data:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJHVNNS3BSZCDHPM5MZET64JSGU"),
						"data",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4O4JVRF2SZEPJFFIAOZILXAQDQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ4WZRLJ23BGMBKQ2JBSOBEGBP4"),
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
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::CfgManagement::CfgItem:parentId:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJ5L5OI3X4BHYBPJFGAWUO4MKZM"),
						"parentId",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsABQRNFHXYVB7FEVP36PUEISEAQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ4WZRLJ23BGMBKQ2JBSOBEGBP4"),
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::CfgManagement::CfgItem:parentId:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::CfgManagement::CfgItem:objState:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colOY3R4QKX2NEEHMDIS6QV4FMDQE"),
						"objState",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQTUICRWBPVASTJHSPIAROKHM4Y"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ4WZRLJ23BGMBKQ2JBSOBEGBP4"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acsWJELU3IWQVEKXFS4OLX6INWGTY"),
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

						/*Radix::CfgManagement::CfgItem:objState:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acsWJELU3IWQVEKXFS4OLX6INWGTY"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.NONE,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::CfgManagement::CfgItem:packetId:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colTUL7C3425VCCNC5ELKDFZBWJOY"),
						"packetId",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ4WZRLJ23BGMBKQ2JBSOBEGBP4"),
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::CfgManagement::CfgItem:packetId:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::CfgManagement::CfgItem:id:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIUQVQZZKPFBRLG7DTABUYGLYBQ"),
						"id",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDFKMMSN4INAUDCNMBXFAA2INFM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ4WZRLJ23BGMBKQ2JBSOBEGBP4"),
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::CfgManagement::CfgItem:id:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::CfgManagement::CfgItem:skip:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJGXEDVUL4JA5JAKSJCAVSU2BSU"),
						"skip",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsS2RWDNGY2VGTHNTKDR3GR26LLU"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ4WZRLJ23BGMBKQ2JBSOBEGBP4"),
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

						/*Radix::CfgManagement::CfgItem:skip:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ4WZRLJ23BGMBKQ2JBSOBEGBP4"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::CfgManagement::CfgItem:srcObjectPid:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3DNFCTYB3ZDJZCRY4OGG7I66YA"),
						"srcObjectPid",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTL4GYON4IVAA3FQLTLPV2DMDD4"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ4WZRLJ23BGMBKQ2JBSOBEGBP4"),
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

						/*Radix::CfgManagement::CfgItem:srcObjectPid:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::CfgManagement::CfgItem:srcObjectTitle:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHUYIZ7QPTBE7FAGC4FA76QNHPY"),
						"srcObjectTitle",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJHNMAVJLAJCQ5LK2PMKMS4VYGI"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ4WZRLJ23BGMBKQ2JBSOBEGBP4"),
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

						/*Radix::CfgManagement::CfgItem:srcObjectTitle:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::CfgManagement::CfgItem:parent:PropertyPresentation-Parent Ref Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colD6XH3IE6INFTHPWTRD6KMRUTBI"),
						"parent",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOZLSBAP3TZDDBM4AP5RUEGAIJ4"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ4WZRLJ23BGMBKQ2JBSOBEGBP4"),
						org.radixware.kernel.common.enums.EValType.PARENT_REF,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						20,
						null,
						null,
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
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ4WZRLJ23BGMBKQ2JBSOBEGBP4"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblJ4WZRLJ23BGMBKQ2JBSOBEGBP4"),
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprC6NQ7NSN2JDVHLEER72AUDY6II")},
						null,
						null,
						133693439,
						133693439,false),

					/*Radix::CfgManagement::CfgItem:packet:PropertyPresentation-Parent Ref Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXUWWBUGM6VAXXM4LRGA7BJRC7U"),
						"packet",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTHUGRUSSAFBAHPO3KAY6IGRPUA"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ4WZRLJ23BGMBKQ2JBSOBEGBP4"),
						org.radixware.kernel.common.enums.EValType.PARENT_REF,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						20,
						null,
						null,
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
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecWMN3P5J5ONCFPLJVGBFAKZXZXU"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblWMN3P5J5ONCFPLJVGBFAKZXZXU"),
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprJHOQCOZIOBBWNABEIAT5XEG7M4")},
						null,
						null,
						133693439,
						3669995,false),

					/*Radix::CfgManagement::CfgItem:notes:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKLNALIUEZJGQHKSQ22KH6YAQAI"),
						"notes",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCSLRLP6FJZCK7ALAZ4IO7W7OIY"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ4WZRLJ23BGMBKQ2JBSOBEGBP4"),
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::CfgManagement::CfgItem:notes:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,true),
						null,
						null,
						null,
						true,-1,-1,1,
						true,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::CfgManagement::CfgItem:classTitle:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdY3N4QO6NSRDXTCHP27YWP3WONM"),
						"classTitle",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQBOW4TDPKZBXPASLYA7JL2WMJM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ4WZRLJ23BGMBKQ2JBSOBEGBP4"),
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::CfgManagement::CfgItem:classTitle:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::CfgManagement::CfgItem:hasChildren:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd2CKT7GHGJNAEJM42ENCONBLJOE"),
						"hasChildren",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ4WZRLJ23BGMBKQ2JBSOBEGBP4"),
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::CfgManagement::CfgItem:hasChildren:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ4WZRLJ23BGMBKQ2JBSOBEGBP4"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::CfgManagement::CfgItem:srcObjectRid:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHQUFLZVQBNDCHMCVIG7RQM7NDQ"),
						"srcObjectRid",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4HPWC5L7GVA7PISXIBJJZE5YEY"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ4WZRLJ23BGMBKQ2JBSOBEGBP4"),
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

						/*Radix::CfgManagement::CfgItem:srcObjectRid:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::CfgManagement::CfgItem:settings:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colM4MU5QWZUVHADCVXB2GL47BQXI"),
						"settings",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ4WZRLJ23BGMBKQ2JBSOBEGBP4"),
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
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::CfgManagement::CfgItem:srcAppVer:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIRYZTYXM2NBDNOQQ5XMAGM3H6Y"),
						"srcAppVer",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCOKELC4X3ZCMDNCTO3YSWRZ45Y"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ4WZRLJ23BGMBKQ2JBSOBEGBP4"),
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

						/*Radix::CfgManagement::CfgItem:srcAppVer:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::CfgManagement::CfgItem:srcExtGuid:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKF2PFJVIG5FDZJLBPDQR2RN4GA"),
						"srcExtGuid",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsI7INV5WLYNDYXPBVLJQS6STZGA"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ4WZRLJ23BGMBKQ2JBSOBEGBP4"),
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

						/*Radix::CfgManagement::CfgItem:srcExtGuid:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,100,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false)
			},
			new org.radixware.kernel.common.client.meta.RadCommandDef[]{
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::CfgManagement::CfgItem:Relink-Object Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdGSYHPNMK3JCPTD6MVPSWZKCYLA"),
						"Relink",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ4WZRLJ23BGMBKQ2JBSOBEGBP4"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAW3EHYRWJNDG7MO5JKMTYRXTFQ"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("imgTHZUHZZKPBA63KXOYUIXHM74VQ"),
						null,
						org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,
						true,
						true,
						false,
						org.radixware.kernel.common.enums.ECommandScope.OBJECT,
						null,
						org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_FIXED,
						true),
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::CfgManagement::CfgItem:RelinkChildren-Object Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdYSTUTAIG7FAIRMIPBQTCTOZOGQ"),
						"RelinkChildren",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ4WZRLJ23BGMBKQ2JBSOBEGBP4"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPLOBWSYLSVBZNEBLQCKGV242BM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("imgK7WOSX72KFDGDPUCPTHJ7PNCHA"),
						null,
						org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,
						true,
						true,
						false,
						org.radixware.kernel.common.enums.ECommandScope.OBJECT,
						null,
						org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_FIXED,
						true)
			},
			new org.radixware.kernel.common.client.meta.filters.RadFilterDef[]{

					new org.radixware.kernel.common.client.meta.filters.RadFilterDef(
					/*Radix::CfgManagement::CfgItem:NewObj-Filter*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("fltMZ5IEUI3RRB6HFDHHLHAFWJPAU"),
						"NewObj",
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ4WZRLJ23BGMBKQ2JBSOBEGBP4"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6S2464ZPMFFB5GLBXRDHQ6KKFI"),
						"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:Sql>exists (\nselect 1 from </xsc:Sql></xsc:Item><xsc:Item><xsc:TableSqlName TableId=\"tblJ4WZRLJ23BGMBKQ2JBSOBEGBP4\"/></xsc:Item><xsc:Item><xsc:Sql>  \nwhere </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecJ4WZRLJ23BGMBKQ2JBSOBEGBP4\" PropId=\"colOY3R4QKX2NEEHMDIS6QV4FMDQE\" Owner=\"TABLE\"/></xsc:Item><xsc:Item><xsc:Sql> = </xsc:Sql></xsc:Item><xsc:Item><xsc:ConstValue EnumId=\"acsWJELU3IWQVEKXFS4OLX6INWGTY\" ItemId=\"aciI5CUH35A4VEJTKCMGUD2UDENTA\"/></xsc:Item><xsc:Item><xsc:Sql>\nstart with </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecJ4WZRLJ23BGMBKQ2JBSOBEGBP4\" PropId=\"colIUQVQZZKPFBRLG7DTABUYGLYBQ\" Owner=\"TABLE\"/></xsc:Item><xsc:Item><xsc:Sql> = </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecJ4WZRLJ23BGMBKQ2JBSOBEGBP4\" PropId=\"colIUQVQZZKPFBRLG7DTABUYGLYBQ\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> connect by </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecJ4WZRLJ23BGMBKQ2JBSOBEGBP4\" PropId=\"colJ5L5OI3X4BHYBPJFGAWUO4MKZM\" Owner=\"TABLE\"/></xsc:Item><xsc:Item><xsc:Sql> = prior </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecJ4WZRLJ23BGMBKQ2JBSOBEGBP4\" PropId=\"colIUQVQZZKPFBRLG7DTABUYGLYBQ\" Owner=\"TABLE\"/></xsc:Item><xsc:Item><xsc:Sql>\n)</xsc:Sql></xsc:Item></xsc:Sqml>",
						null,
						null,
						true,
						true,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("srtUSWUP7RXBJFXRMQNHD67PC2JIY"),
						null,

						/*Radix::CfgManagement::CfgItem:NewObj:Editor Pages-Filter Pages*/
						null,
						new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
						}
						,
						null),

					new org.radixware.kernel.common.client.meta.filters.RadFilterDef(
					/*Radix::CfgManagement::CfgItem:Invalid-Filter*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("fltNIWKPNVHINEJBIOBJXX5HVQTNQ"),
						"Invalid",
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ4WZRLJ23BGMBKQ2JBSOBEGBP4"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls73SIYHAFPRAU3A5IE3TAN5JEDA"),
						"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:Sql>exists (\nselect 1 from </xsc:Sql></xsc:Item><xsc:Item><xsc:TableSqlName TableId=\"tblJ4WZRLJ23BGMBKQ2JBSOBEGBP4\"/></xsc:Item><xsc:Item><xsc:Sql> \nwhere \n    </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecJ4WZRLJ23BGMBKQ2JBSOBEGBP4\" PropId=\"colW3X3MBNTEBADJKAZZQ4QEOMSKY\" Owner=\"TABLE\"/></xsc:Item><xsc:Item><xsc:Sql> &lt;> </xsc:Sql></xsc:Item><xsc:Item><xsc:ConstValue EnumId=\"acsUT33HR7H2BCG5NXG3J52FIV5AA\" ItemId=\"aciP5K5II6QT5BAFFRHUVLN3OEYGU\"/></xsc:Item><xsc:Item><xsc:Sql>\n    or \n    </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecJ4WZRLJ23BGMBKQ2JBSOBEGBP4\" PropId=\"colOY3R4QKX2NEEHMDIS6QV4FMDQE\" Owner=\"TABLE\"/></xsc:Item><xsc:Item><xsc:Sql> = </xsc:Sql></xsc:Item><xsc:Item><xsc:ConstValue EnumId=\"acsWJELU3IWQVEKXFS4OLX6INWGTY\" ItemId=\"aciIXJSDSZVTJDMBGMYADEPMDPIMU\"/></xsc:Item><xsc:Item><xsc:Sql>\nstart with </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecJ4WZRLJ23BGMBKQ2JBSOBEGBP4\" PropId=\"colIUQVQZZKPFBRLG7DTABUYGLYBQ\" Owner=\"TABLE\"/></xsc:Item><xsc:Item><xsc:Sql> = </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecJ4WZRLJ23BGMBKQ2JBSOBEGBP4\" PropId=\"colIUQVQZZKPFBRLG7DTABUYGLYBQ\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> connect by </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecJ4WZRLJ23BGMBKQ2JBSOBEGBP4\" PropId=\"colJ5L5OI3X4BHYBPJFGAWUO4MKZM\" Owner=\"TABLE\"/></xsc:Item><xsc:Item><xsc:Sql> = prior </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecJ4WZRLJ23BGMBKQ2JBSOBEGBP4\" PropId=\"colIUQVQZZKPFBRLG7DTABUYGLYBQ\" Owner=\"TABLE\"/></xsc:Item><xsc:Item><xsc:Sql>     \n)\n\n</xsc:Sql></xsc:Item></xsc:Sqml>",
						null,
						null,
						true,
						true,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("srtUSWUP7RXBJFXRMQNHD67PC2JIY"),
						null,

						/*Radix::CfgManagement::CfgItem:Invalid:Editor Pages-Filter Pages*/
						null,
						new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
						}
						,
						null),

					new org.radixware.kernel.common.client.meta.filters.RadFilterDef(
					/*Radix::CfgManagement::CfgItem:ByExtGuid-Filter*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("flt7HADR6IUNJCAXCROQ6V2PCLNJM"),
						"ByExtGuid",
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ4WZRLJ23BGMBKQ2JBSOBEGBP4"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsA7XD4C2TRJCRLG7SF5QLNPHLWM"),
						"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:Sql>(</xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmG754KGK6GFCPHNAU2OY7BPT2EE\"/></xsc:Item><xsc:Item><xsc:Sql> is null and </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecJ4WZRLJ23BGMBKQ2JBSOBEGBP4\" PropId=\"colKF2PFJVIG5FDZJLBPDQR2RN4GA\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> is null) \nor  \n(</xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmG754KGK6GFCPHNAU2OY7BPT2EE\"/></xsc:Item><xsc:Item><xsc:Sql> is not null and </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecJ4WZRLJ23BGMBKQ2JBSOBEGBP4\" PropId=\"colKF2PFJVIG5FDZJLBPDQR2RN4GA\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> like \'%\' || </xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmG754KGK6GFCPHNAU2OY7BPT2EE\"/></xsc:Item><xsc:Item><xsc:Sql> || \'%\')</xsc:Sql></xsc:Item></xsc:Sqml>",
						null,
						null,
						false,
						true,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("srtUSWUP7RXBJFXRMQNHD67PC2JIY"),
						new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef[]{

								new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmG754KGK6GFCPHNAU2OY7BPT2EE"),
								"extGuid",
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5MSGQ7W7CBG45MHJQRU64OMJOY"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ4WZRLJ23BGMBKQ2JBSOBEGBP4"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("tblJ4WZRLJ23BGMBKQ2JBSOBEGBP4"),
								null,
								org.radixware.kernel.common.enums.EValType.STR,
								null,
								null,
								false,
								false,
								false,
								null,
								false,
								null,
								null,
								false,
								null,
								null)
						},

						/*Radix::CfgManagement::CfgItem:ByExtGuid:Editor Pages-Filter Pages*/
						null,
						new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
						}
						,
						null)
			},
			new org.radixware.kernel.common.client.meta.RadSortingDef[]{

					new org.radixware.kernel.common.client.meta.RadSortingDef(
					/*Radix::CfgManagement::CfgItem:Id-Sorting*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("srtUSWUP7RXBJFXRMQNHD67PC2JIY"),
						"Id",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ4WZRLJ23BGMBKQ2JBSOBEGBP4"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQPP64NALWBEBFHQCT3GQOJN5TQ"),
						new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem[]{

								new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIUQVQZZKPFBRLG7DTABUYGLYBQ"),
								false)
						})
			},
			new org.radixware.kernel.common.client.meta.RadReferenceDef[]{

					new org.radixware.kernel.common.client.meta.RadReferenceDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("refF34ZWBF4TBBCHIUDOFBRI67OVY"),"Item=>Packet (packetId=>id)",org.radixware.kernel.common.types.Id.Factory.loadFrom("tblJ4WZRLJ23BGMBKQ2JBSOBEGBP4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblWMN3P5J5ONCFPLJVGBFAKZXZXU"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colTUL7C3425VCCNC5ELKDFZBWJOY")},new String[]{"packetId"},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colDZOPCTNIURBCBPLCP75JMZCFYM")},new String[]{"id"}),

					new org.radixware.kernel.common.client.meta.RadReferenceDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("refSAINKHTF3ZBCZGDGBCAMV4P76E"),"Item=>Item (parentId=>id)",org.radixware.kernel.common.types.Id.Factory.loadFrom("tblJ4WZRLJ23BGMBKQ2JBSOBEGBP4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblJ4WZRLJ23BGMBKQ2JBSOBEGBP4"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colJ5L5OI3X4BHYBPJFGAWUO4MKZM")},new String[]{"parentId"},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colIUQVQZZKPFBRLG7DTABUYGLYBQ")},new String[]{"id"})
			},
			new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprCUUYK4MIYVFVLCLQGXLUNZWINM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("eprO3XHJ6U74NDNHJ7GNTDUUNDEWE"),org.radixware.kernel.common.types.Id.Factory.loadFrom("eprC6NQ7NSN2JDVHLEER72AUDY6II"),org.radixware.kernel.common.types.Id.Factory.loadFrom("spr4SSGPHYJVRAGLB7HGLSAGIAMIQ"),org.radixware.kernel.common.types.Id.Factory.loadFrom("sprE6NT4GPHVREKJJMNK6YDGWAZUQ"),org.radixware.kernel.common.types.Id.Factory.loadFrom("spr4HURLRTA4JFT7KQTNLYMG3C63I"),org.radixware.kernel.common.types.Id.Factory.loadFrom("spr2PZI4TZ34JEQNKVR5AXO6PWPNI"),org.radixware.kernel.common.types.Id.Factory.loadFrom("sprMQEXFCGPZJBLVA7BZDI5QHQKRY")},
			true,false,false);
}

/* Radix::CfgManagement::CfgItem - Web Meta*/

/*Radix::CfgManagement::CfgItem-Entity Class*/

package org.radixware.ads.CfgManagement.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class CfgItem_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::CfgManagement::CfgItem:Presentations-Entity Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ4WZRLJ23BGMBKQ2JBSOBEGBP4"),
			"Radix::CfgManagement::CfgItem",
			null,
			null,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6SY6L3HI6JGJNIZAIOYXCHTCVQ"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZV2E2CQV3ZAP7NL6Q5IZLENPQQ"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6SY6L3HI6JGJNIZAIOYXCHTCVQ"),0,
			null,
			null,
			null,
			null,
			null,
			null,
			false,false,false);
}

/* Radix::CfgManagement::CfgItem:Export - Desktop Meta*/

/*Radix::CfgManagement::CfgItem:Export-Editor Presentation*/

package org.radixware.ads.CfgManagement.explorer;
public final class Export_mi{
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new org.radixware.kernel.common.client.meta.RadEditorPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprCUUYK4MIYVFVLCLQGXLUNZWINM"),
	"Export",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
	org.radixware.kernel.common.types.Id.Factory.loadFrom("eprC6NQ7NSN2JDVHLEER72AUDY6II"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ4WZRLJ23BGMBKQ2JBSOBEGBP4"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("tblJ4WZRLJ23BGMBKQ2JBSOBEGBP4"),
	null,
	null,
	null,
	null,

	/*Radix::CfgManagement::CfgItem:Export:Children-Explorer Items*/
		new org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemDef[]{

				/*Radix::CfgManagement::CfgItem:Export:Children-Child Ref Explorer Item*/

				new org.radixware.kernel.common.client.meta.explorerItems.RadChildRefExplorerItemDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiVICI3JOSCZFBLNFNTHVZJO3FYE"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ4WZRLJ23BGMBKQ2JBSOBEGBP4"),null,
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ4WZRLJ23BGMBKQ2JBSOBEGBP4"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("spr4SSGPHYJVRAGLB7HGLSAGIAMIQ"),
					0,
					null,
					16560,false)
		}
	,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
	null,
	0,
	null,
	(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
	40114,0,0);
}
/* Radix::CfgManagement::CfgItem:Export:Model - Desktop Executable*/

/*Radix::CfgManagement::CfgItem:Export:Model-Entity Model Class*/

package org.radixware.ads.CfgManagement.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItem:Export:Model")
public class Export:Model  extends org.radixware.ads.CfgManagement.explorer.Base:Model  {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return Export:Model_mi.rdxMeta; }



	public Export:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadEditorPresentationDef def){super(userSession,def);}

	/*Radix::CfgManagement::CfgItem:Export:Model:Nested classes-Nested Classes*/

	/*Radix::CfgManagement::CfgItem:Export:Model:Properties-Properties*/

	/*Radix::CfgManagement::CfgItem:Export:Model:Methods-Methods*/

	/*Radix::CfgManagement::CfgItem:Export:Model:afterRead-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItem:Export:Model:afterRead")
	protected published  void afterRead () {
		super.afterRead();
		getCommand(idof[CfgItem:Relink]).setVisible(false);
		getCommand(idof[CfgItem:RelinkChildren]).setVisible(false);

	}


}

/* Radix::CfgManagement::CfgItem:Export:Model - Desktop Meta*/

/*Radix::CfgManagement::CfgItem:Export:Model-Entity Model Class*/

package org.radixware.ads.CfgManagement.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Export:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aemCUUYK4MIYVFVLCLQGXLUNZWINM"),
						"Export:Model",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aemC6NQ7NSN2JDVHLEER72AUDY6II"),
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::CfgManagement::CfgItem:Export:Model:Properties-Properties*/
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

/* Radix::CfgManagement::CfgItem:Import - Desktop Meta*/

/*Radix::CfgManagement::CfgItem:Import-Editor Presentation*/

package org.radixware.ads.CfgManagement.explorer;
public final class Import_mi{
	private static final class Import_DEF extends org.radixware.kernel.common.client.meta.RadEditorPresentationDef{
		Import_DEF(){
			super(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprO3XHJ6U74NDNHJ7GNTDUUNDEWE"),
			"Import",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("eprC6NQ7NSN2JDVHLEER72AUDY6II"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ4WZRLJ23BGMBKQ2JBSOBEGBP4"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("tblJ4WZRLJ23BGMBKQ2JBSOBEGBP4"),
			null,
			null,
			null,
			null,

			/*Radix::CfgManagement::CfgItem:Import:Children-Explorer Items*/
				new org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemDef[]{

						/*Radix::CfgManagement::CfgItem:Import:Children-Child Ref Explorer Item*/

						new org.radixware.kernel.common.client.meta.explorerItems.RadChildRefExplorerItemDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiB5VEHK7NEBD4LEO6LR3MOP3XS4"),
							org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ4WZRLJ23BGMBKQ2JBSOBEGBP4"),null,
							org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ4WZRLJ23BGMBKQ2JBSOBEGBP4"),
							org.radixware.kernel.common.types.Id.Factory.loadFrom("sprE6NT4GPHVREKJJMNK6YDGWAZUQ"),
							0,
							null,
							16560,false)
				}
			,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
			null,
			0,
			null,
			(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
			40114,0,0);
		}
		@Override
		protected org.radixware.kernel.common.client.models.Model createModelImpl(org.radixware.kernel.common.client.IClientEnvironment userSession) {    return new org.radixware.ads.CfgManagement.explorer.Base:Model(userSession,this);
		}
	}
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new Import_DEF(); 
;
}
/* Radix::CfgManagement::CfgItem:Base - Desktop Meta*/

/*Radix::CfgManagement::CfgItem:Base-Editor Presentation*/

package org.radixware.ads.CfgManagement.explorer;
public final class Base_mi{
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new org.radixware.kernel.common.client.meta.RadEditorPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprC6NQ7NSN2JDVHLEER72AUDY6II"),
	"Base",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
	null,
	org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ4WZRLJ23BGMBKQ2JBSOBEGBP4"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("tblJ4WZRLJ23BGMBKQ2JBSOBEGBP4"),
	null,
	null,

	/*Radix::CfgManagement::CfgItem:Base:Editor Pages-Editor Presentation Pages*/
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

			/*Radix::CfgManagement::CfgItem:Base:General-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgUEDGSU2RXZAUFORLOPJPDXPI64"),"General",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ4WZRLJ23BGMBKQ2JBSOBEGBP4"),null,null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIUQVQZZKPFBRLG7DTABUYGLYBQ"),0,0,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKLNALIUEZJGQHKSQ22KH6YAQAI"),0,4,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdY3N4QO6NSRDXTCHP27YWP3WONM"),0,2,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJHVNNS3BSZCDHPM5MZET64JSGU"),0,5,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXUWWBUGM6VAXXM4LRGA7BJRC7U"),0,3,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKF2PFJVIG5FDZJLBPDQR2RN4GA"),0,1,1,false,false)
			},null)
	},
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgUEDGSU2RXZAUFORLOPJPDXPI64"))}
	,

	/*Radix::CfgManagement::CfgItem:Base:Children-Explorer Items*/
		null
	,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
	null,
	0,
	null,
	(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
	2192,0,0,null);
}
/* Radix::CfgManagement::CfgItem:Base:Model - Desktop Executable*/

/*Radix::CfgManagement::CfgItem:Base:Model-Entity Model Class*/

package org.radixware.ads.CfgManagement.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItem:Base:Model")
public class Base:Model  extends org.radixware.ads.CfgManagement.explorer.CfgItem.CfgItem_DefaultModel {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return Base:Model_mi.rdxMeta; }

	private final String THIS_VERSION = Environment.getDefManager().getClassLoader().getRevisionMeta().LayerVersionsString;

	public Base:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadEditorPresentationDef def){super(userSession,def);}

	/*Radix::CfgManagement::CfgItem:Base:Model:Nested classes-Nested Classes*/

	/*Radix::CfgManagement::CfgItem:Base:Model:Properties-Properties*/

	/*Radix::CfgManagement::CfgItem:Base:Model:srcAppVer-Presentation Property*/




	public class SrcAppVer extends org.radixware.ads.CfgManagement.explorer.CfgItem.colIRYZTYXM2NBDNOQQ5XMAGM3H6Y{
		public SrcAppVer(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}

		/*Radix::CfgManagement::CfgItem:Base:Model:srcAppVer:EmbeddedClass-Property Presentation Embedded Class*/




		/*Radix::CfgManagement::CfgItem:Base:Model:srcAppVer:Nested classes-Nested Classes*/

		/*Radix::CfgManagement::CfgItem:Base:Model:srcAppVer:Properties-Properties*/

		/*Radix::CfgManagement::CfgItem:Base:Model:srcAppVer:Methods-Methods*/

		/*Radix::CfgManagement::CfgItem:Base:Model:srcAppVer:getTextOptions-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItem:Base:Model:srcAppVer:getTextOptions")
		protected published  org.radixware.kernel.common.client.text.ITextOptions getTextOptions (java.util.EnumSet<org.radixware.kernel.common.client.enums.ETextOptionsMarker> markers, org.radixware.kernel.common.client.text.ITextOptions options) {
			if (srcAppVer.Value != null && isInSelectorRowContext()
			        && !CfgCommonUtils.compareLayersVersions(THIS_VERSION, srcAppVer.Value)) {
			    return options.changeForegroundColor(Utils::Color.RED);
			}
			return options;
		}













		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItem:Base:Model:srcAppVer")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItem:Base:Model:srcAppVer")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public SrcAppVer getSrcAppVer(){return (SrcAppVer)getProperty(colIRYZTYXM2NBDNOQQ5XMAGM3H6Y);}








	/*Radix::CfgManagement::CfgItem:Base:Model:Methods-Methods*/

	/*Radix::CfgManagement::CfgItem:Base:Model:onCommand_Relink-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItem:Base:Model:onCommand_Relink")
	protected  void onCommand_Relink (org.radixware.ads.CfgManagement.explorer.CfgItem.Relink command) {
		try {
		    command.send();
		} catch (Exceptions::InterruptedException e) {
		    showException(e);
		} catch (Exceptions::ServiceClientException e) {
		    showException(e);
		}
	}

	/*Radix::CfgManagement::CfgItem:Base:Model:onCommand_RelinkChildren-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItem:Base:Model:onCommand_RelinkChildren")
	protected  void onCommand_RelinkChildren (org.radixware.ads.CfgManagement.explorer.CfgItem.RelinkChildren command) {
		try {
		    command.send();
		    if (isInSelectorRowContext())
		        ((Explorer.Context::SelectorRowContext) getContext()).parentGroupModel.getGroupView().reread();
		} catch (Exceptions::InterruptedException e) {
		    showException(e);
		} catch (Exceptions::ServiceClientException e) {
		    showException(e);
		}
	}
	public final class Relink extends org.radixware.ads.CfgManagement.explorer.CfgItem.Relink{
		protected Relink(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_Relink( this );
		}

	}

	public final class RelinkChildren extends org.radixware.ads.CfgManagement.explorer.CfgItem.RelinkChildren{
		protected RelinkChildren(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_RelinkChildren( this );
		}

	}















}

/* Radix::CfgManagement::CfgItem:Base:Model - Desktop Meta*/

/*Radix::CfgManagement::CfgItem:Base:Model-Entity Model Class*/

package org.radixware.ads.CfgManagement.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Base:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aemC6NQ7NSN2JDVHLEER72AUDY6II"),
						"Base:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::CfgManagement::CfgItem:Base:Model:Properties-Properties*/
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

/* Radix::CfgManagement::CfgItem:Export - Desktop Meta*/

/*Radix::CfgManagement::CfgItem:Export-Selector Presentation*/

package org.radixware.ads.CfgManagement.explorer;
public final class Export_mi extends org.radixware.kernel.common.client.meta.RadSelectorPresentationDef{
	public static final org.radixware.kernel.common.client.meta.RadSelectorPresentationDef rdxMeta = new Export_mi();
	private Export_mi(){
		super(org.radixware.kernel.common.types.Id.Factory.loadFrom("spr4SSGPHYJVRAGLB7HGLSAGIAMIQ"),
		"Export",
		org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("spr4HURLRTA4JFT7KQTNLYMG3C63I"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ4WZRLJ23BGMBKQ2JBSOBEGBP4"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("tblJ4WZRLJ23BGMBKQ2JBSOBEGBP4"),
		null,
		null,
		null,
		null,
		false,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("srtUSWUP7RXBJFXRMQNHD67PC2JIY"),
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("flt7HADR6IUNJCAXCROQ6V2PCLNJM")},
		false,
		true,
		null,
		278529,
		null,
		16531,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprCUUYK4MIYVFVLCLQGXLUNZWINM")},
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprCUUYK4MIYVFVLCLQGXLUNZWINM")},
		false,true,false,0,0);
		columns = new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn[]{

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIUQVQZZKPFBRLG7DTABUYGLYBQ"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.RESIZE_BY_CONTENT,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdY3N4QO6NSRDXTCHP27YWP3WONM"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKF2PFJVIG5FDZJLBPDQR2RN4GA"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHUYIZ7QPTBE7FAGC4FA76QNHPY"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIRYZTYXM2NBDNOQQ5XMAGM3H6Y"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKLNALIUEZJGQHKSQ22KH6YAQAI"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd2CKT7GHGJNAEJM42ENCONBLJOE"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null)
		};
	}
}
/* Radix::CfgManagement::CfgItem:Export:Model - Desktop Executable*/

/*Radix::CfgManagement::CfgItem:Export:Model-Group Model Class*/

package org.radixware.ads.CfgManagement.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItem:Export:Model")
public class Export:Model  extends org.radixware.ads.CfgManagement.explorer.BaseTree:Model  {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return Export:Model_mi.rdxMeta; }



	public Export:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadSelectorPresentationDef def){super(userSession,def);}

	/*Radix::CfgManagement::CfgItem:Export:Model:Nested classes-Nested Classes*/

	/*Radix::CfgManagement::CfgItem:Export:Model:ExportTreeSelector-Desktop Dynamic Class*/


	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItem:Export:Model:ExportTreeSelector")
	public class ExportTreeSelector  extends org.radixware.kernel.explorer.views.selector.Selector  {



		/*Radix::CfgManagement::CfgItem:Export:Model:ExportTreeSelector:Nested classes-Nested Classes*/

		/*Radix::CfgManagement::CfgItem:Export:Model:ExportTreeSelector:Properties-Properties*/

		/*Radix::CfgManagement::CfgItem:Export:Model:ExportTreeSelector:Methods-Methods*/

		/*Radix::CfgManagement::CfgItem:Export:Model:ExportTreeSelector:ExportTreeSelector-User Method*/

		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItem:Export:Model:ExportTreeSelector:ExportTreeSelector")
		public  ExportTreeSelector () {
			super(Explorer.Env::Application.Instance.Environment );
		}

		/*Radix::CfgManagement::CfgItem:Export:Model:ExportTreeSelector:open-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItem:Export:Model:ExportTreeSelector:open")
		public published  void open (org.radixware.kernel.common.client.models.Model model) {
			super.open(model);

			Explorer.Views.Wraps::StandardSelectorTreeModel treeModel =
			        new TreeSelectorModel(
			        getGroupModel(),
			        idof[CfgItem:Export:Children]        
			        );

			Explorer.Widgets::SelectorTree selectorWidget =
			        new SelectorTree(
			        this,
			        treeModel);

			com.trolltech.qt.gui.QVBoxLayout layout = new com.trolltech.qt.gui.QVBoxLayout(this.content);
			this.content.setLayout(layout);
			layout.setContentsMargins(0, 0, 0, 0);
			layout.addWidget(selectorWidget);

			this.setSelectorWidget(selectorWidget);

		}


	}

	/*Radix::CfgManagement::CfgItem:Export:Model:Properties-Properties*/

	/*Radix::CfgManagement::CfgItem:Export:Model:Methods-Methods*/

	/*Radix::CfgManagement::CfgItem:Export:Model:createView-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItem:Export:Model:createView")
	public published  org.radixware.kernel.common.client.views.IView createView () {
		return new ExportTreeSelector();
	}


}

/* Radix::CfgManagement::CfgItem:Export:Model - Desktop Meta*/

/*Radix::CfgManagement::CfgItem:Export:Model-Group Model Class*/

package org.radixware.ads.CfgManagement.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Export:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("agm4SSGPHYJVRAGLB7HGLSAGIAMIQ"),
						"Export:Model",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("agm4HURLRTA4JFT7KQTNLYMG3C63I"),
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::CfgManagement::CfgItem:Export:Model:Properties-Properties*/
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

/* Radix::CfgManagement::CfgItem:Import - Desktop Meta*/

/*Radix::CfgManagement::CfgItem:Import-Selector Presentation*/

package org.radixware.ads.CfgManagement.explorer;
public final class Import_mi extends org.radixware.kernel.common.client.meta.RadSelectorPresentationDef{
	public static final org.radixware.kernel.common.client.meta.RadSelectorPresentationDef rdxMeta = new Import_mi();
	private Import_mi(){
		super(org.radixware.kernel.common.types.Id.Factory.loadFrom("sprE6NT4GPHVREKJJMNK6YDGWAZUQ"),
		"Import",
		org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("spr4HURLRTA4JFT7KQTNLYMG3C63I"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ4WZRLJ23BGMBKQ2JBSOBEGBP4"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("tblJ4WZRLJ23BGMBKQ2JBSOBEGBP4"),
		null,
		null,
		null,
		null,
		false,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("srtUSWUP7RXBJFXRMQNHD67PC2JIY"),
		null,
		false,
		true,
		null,
		278529,
		null,
		16531,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprO3XHJ6U74NDNHJ7GNTDUUNDEWE")},
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprO3XHJ6U74NDNHJ7GNTDUUNDEWE")},
		false,true,false,0,0);
		columns = new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn[]{

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIUQVQZZKPFBRLG7DTABUYGLYBQ"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.RESIZE_BY_CONTENT,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdY3N4QO6NSRDXTCHP27YWP3WONM"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKF2PFJVIG5FDZJLBPDQR2RN4GA"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHUYIZ7QPTBE7FAGC4FA76QNHPY"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIRYZTYXM2NBDNOQQ5XMAGM3H6Y"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKLNALIUEZJGQHKSQ22KH6YAQAI"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJGXEDVUL4JA5JAKSJCAVSU2BSU"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.RESIZE_BY_CONTENT,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colW3X3MBNTEBADJKAZZQ4QEOMSKY"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.RESIZE_BY_CONTENT,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colOY3R4QKX2NEEHMDIS6QV4FMDQE"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.RESIZE_BY_CONTENT,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd2CKT7GHGJNAEJM42ENCONBLJOE"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null)
		};
	}
}
/* Radix::CfgManagement::CfgItem:Import:Model - Desktop Executable*/

/*Radix::CfgManagement::CfgItem:Import:Model-Group Model Class*/

package org.radixware.ads.CfgManagement.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItem:Import:Model")
public class Import:Model  extends org.radixware.ads.CfgManagement.explorer.BaseTree:Model  {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return Import:Model_mi.rdxMeta; }



	public Import:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadSelectorPresentationDef def){super(userSession,def);}

	/*Radix::CfgManagement::CfgItem:Import:Model:Nested classes-Nested Classes*/

	/*Radix::CfgManagement::CfgItem:Import:Model:ImportTreeSelector-Desktop Dynamic Class*/


	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItem:Import:Model:ImportTreeSelector")
	public class ImportTreeSelector  extends org.radixware.kernel.explorer.views.selector.Selector  {



		/*Radix::CfgManagement::CfgItem:Import:Model:ImportTreeSelector:Nested classes-Nested Classes*/

		/*Radix::CfgManagement::CfgItem:Import:Model:ImportTreeSelector:Properties-Properties*/

		/*Radix::CfgManagement::CfgItem:Import:Model:ImportTreeSelector:Methods-Methods*/

		/*Radix::CfgManagement::CfgItem:Import:Model:ImportTreeSelector:ImportTreeSelector-User Method*/

		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItem:Import:Model:ImportTreeSelector:ImportTreeSelector")
		public  ImportTreeSelector () {
			super(Explorer.Env::Application.Instance.Environment );
		}

		/*Radix::CfgManagement::CfgItem:Import:Model:ImportTreeSelector:open-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItem:Import:Model:ImportTreeSelector:open")
		public published  void open (org.radixware.kernel.common.client.models.Model model) {
			super.open(model);

			Explorer.Views.Wraps::StandardSelectorTreeModel treeModel =
			        new TreeSelectorModel(
			        getGroupModel(),
			        idof[CfgItem:Import:Children]        
			        );

			Explorer.Widgets::SelectorTree selectorWidget =
			        new SelectorTree(
			        this,
			        treeModel);

			com.trolltech.qt.gui.QVBoxLayout layout = new com.trolltech.qt.gui.QVBoxLayout(this.content);
			this.content.setLayout(layout);
			layout.setContentsMargins(0, 0, 0, 0);
			layout.addWidget(selectorWidget);

			this.setSelectorWidget(selectorWidget);

		}


	}

	/*Radix::CfgManagement::CfgItem:Import:Model:Properties-Properties*/

	/*Radix::CfgManagement::CfgItem:Import:Model:Methods-Methods*/

	/*Radix::CfgManagement::CfgItem:Import:Model:createView-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItem:Import:Model:createView")
	public published  org.radixware.kernel.common.client.views.IView createView () {
		return new ImportTreeSelector();
	}


}

/* Radix::CfgManagement::CfgItem:Import:Model - Desktop Meta*/

/*Radix::CfgManagement::CfgItem:Import:Model-Group Model Class*/

package org.radixware.ads.CfgManagement.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Import:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("agmE6NT4GPHVREKJJMNK6YDGWAZUQ"),
						"Import:Model",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("agm4HURLRTA4JFT7KQTNLYMG3C63I"),
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::CfgManagement::CfgItem:Import:Model:Properties-Properties*/
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

/* Radix::CfgManagement::CfgItem:BaseTree - Desktop Meta*/

/*Radix::CfgManagement::CfgItem:BaseTree-Selector Presentation*/

package org.radixware.ads.CfgManagement.explorer;
public final class BaseTree_mi extends org.radixware.kernel.common.client.meta.RadSelectorPresentationDef{
	public static final org.radixware.kernel.common.client.meta.RadSelectorPresentationDef rdxMeta = new BaseTree_mi();
	private BaseTree_mi(){
		super(org.radixware.kernel.common.types.Id.Factory.loadFrom("spr4HURLRTA4JFT7KQTNLYMG3C63I"),
		"BaseTree",
		org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("spr2PZI4TZ34JEQNKVR5AXO6PWPNI"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ4WZRLJ23BGMBKQ2JBSOBEGBP4"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("tblJ4WZRLJ23BGMBKQ2JBSOBEGBP4"),
		null,
		null,
		null,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("srtUSWUP7RXBJFXRMQNHD67PC2JIY")},
		false,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("srtUSWUP7RXBJFXRMQNHD67PC2JIY"),
		new org.radixware.kernel.common.types.Id[]{},
		false,
		false,
		null,
		0,null,
		16562,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprC6NQ7NSN2JDVHLEER72AUDY6II")},
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprC6NQ7NSN2JDVHLEER72AUDY6II")},
		false,true,false,0,0);
		columns = new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn[]{

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIUQVQZZKPFBRLG7DTABUYGLYBQ"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJ5L5OI3X4BHYBPJFGAWUO4MKZM"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd2CKT7GHGJNAEJM42ENCONBLJOE"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdY3N4QO6NSRDXTCHP27YWP3WONM"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHUYIZ7QPTBE7FAGC4FA76QNHPY"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null)
		};
	}
}
/* Radix::CfgManagement::CfgItem:BaseTree:Model - Desktop Executable*/

/*Radix::CfgManagement::CfgItem:BaseTree:Model-Group Model Class*/

package org.radixware.ads.CfgManagement.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItem:BaseTree:Model")
public class BaseTree:Model  extends org.radixware.ads.CfgManagement.explorer.CfgItem.DefaultGroupModel {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return BaseTree:Model_mi.rdxMeta; }



	public BaseTree:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadSelectorPresentationDef def){super(userSession,def);}

	/*Radix::CfgManagement::CfgItem:BaseTree:Model:Nested classes-Nested Classes*/

	/*Radix::CfgManagement::CfgItem:BaseTree:Model:TreeSelectorModel-Desktop Dynamic Class*/


	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItem:BaseTree:Model:TreeSelectorModel")
	public class TreeSelectorModel  extends org.radixware.kernel.explorer.widgets.selector.StandardSelectorTreeModel  {



		/*Radix::CfgManagement::CfgItem:BaseTree:Model:TreeSelectorModel:Nested classes-Nested Classes*/

		/*Radix::CfgManagement::CfgItem:BaseTree:Model:TreeSelectorModel:Properties-Properties*/

		/*Radix::CfgManagement::CfgItem:BaseTree:Model:TreeSelectorModel:Methods-Methods*/

		/*Radix::CfgManagement::CfgItem:BaseTree:Model:TreeSelectorModel:TreeSelectorModel-User Method*/

		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItem:BaseTree:Model:TreeSelectorModel:TreeSelectorModel")
		public  TreeSelectorModel (org.radixware.kernel.common.client.models.GroupModel rootModel, org.radixware.kernel.common.types.Id xpiId) {
			super(rootModel, xpiId);
		}

		/*Radix::CfgManagement::CfgItem:BaseTree:Model:TreeSelectorModel:getHasChildrenPropertyId-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItem:BaseTree:Model:TreeSelectorModel:getHasChildrenPropertyId")
		protected published  org.radixware.kernel.common.types.Id getHasChildrenPropertyId (org.radixware.kernel.common.client.models.EntityModel parent) {
			return idof[CfgItem:hasChildren];
		}


	}

	/*Radix::CfgManagement::CfgItem:BaseTree:Model:Properties-Properties*/

	/*Radix::CfgManagement::CfgItem:BaseTree:Model:Methods-Methods*/

	/*Radix::CfgManagement::CfgItem:BaseTree:Model:onCommand_SkipAll-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItem:BaseTree:Model:onCommand_SkipAll")
	public  void onCommand_SkipAll (org.radixware.ads.CfgManagement.explorer.CfgItemGroup.SkipAll command) {
		try {
		    final CfgPacket packet = (CfgPacket) findOwnerByClass(CfgPacket.class);
		    if (packet != null) {
		        final Arte::TypesXsd:IntDocument xPacketId = Arte::TypesXsd:IntDocument.Factory.newInstance();
		        xPacketId.Int = packet.id.getValue();
		        command.send(xPacketId);
		        getView().reread();
		    }
		} catch (Exceptions::InterruptedException ex) {
		    showException(ex);
		} catch (Exceptions::ServiceClientException ex) {
		    showException(ex);
		}
	}

	/*Radix::CfgManagement::CfgItem:BaseTree:Model:isCommandAccessible-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItem:BaseTree:Model:isCommandAccessible")
	protected published  boolean isCommandAccessible (org.radixware.kernel.common.client.meta.RadCommandDef commandDef) {
		if (commandDef.getId() == idof[CfgItemGroup:SkipAll] || commandDef.getId() == idof[CfgItemGroup:UnskipAll]) {
		    return super.isCommandAccessible(commandDef) && 
		            idof[CfgItem:Import] == this.getGroupContext().SelectorPresentationDef.getId();
		}
		return super.isCommandAccessible(commandDef);
	}

	/*Radix::CfgManagement::CfgItem:BaseTree:Model:onCommand_UnskipAll-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItem:BaseTree:Model:onCommand_UnskipAll")
	public  void onCommand_UnskipAll (org.radixware.ads.CfgManagement.explorer.CfgItemGroup.UnskipAll command) {
		try {
		    final CfgPacket packet = (CfgPacket) findOwnerByClass(CfgPacket.class);
		    if (packet != null) {
		        final Arte::TypesXsd:IntDocument xPacketId = Arte::TypesXsd:IntDocument.Factory.newInstance();
		        xPacketId.Int = packet.id.getValue();
		        command.send(xPacketId);
		        getView().reread();
		    }
		} catch (Exceptions::InterruptedException ex) {
		    showException(ex);
		} catch (Exceptions::ServiceClientException ex) {
		    showException(ex);
		}
	}
	public final class SkipAll extends org.radixware.ads.CfgManagement.explorer.CfgItemGroup.SkipAll{
		protected SkipAll(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_SkipAll( this );
		}

	}

	public final class UnskipAll extends org.radixware.ads.CfgManagement.explorer.CfgItemGroup.UnskipAll{
		protected UnskipAll(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_UnskipAll( this );
		}

	}















}

/* Radix::CfgManagement::CfgItem:BaseTree:Model - Desktop Meta*/

/*Radix::CfgManagement::CfgItem:BaseTree:Model-Group Model Class*/

package org.radixware.ads.CfgManagement.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class BaseTree:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("agm4HURLRTA4JFT7KQTNLYMG3C63I"),
						"BaseTree:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::CfgManagement::CfgItem:BaseTree:Model:Properties-Properties*/
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

/* Radix::CfgManagement::CfgItem:Base - Desktop Meta*/

/*Radix::CfgManagement::CfgItem:Base-Selector Presentation*/

package org.radixware.ads.CfgManagement.explorer;
public final class Base_mi extends org.radixware.kernel.common.client.meta.RadSelectorPresentationDef{
	public static final org.radixware.kernel.common.client.meta.RadSelectorPresentationDef rdxMeta = new Base_mi();
	private Base_mi(){
		super(org.radixware.kernel.common.types.Id.Factory.loadFrom("spr2PZI4TZ34JEQNKVR5AXO6PWPNI"),
		"Base",
		org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
		null,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ4WZRLJ23BGMBKQ2JBSOBEGBP4"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("tblJ4WZRLJ23BGMBKQ2JBSOBEGBP4"),
		null,
		null,
		null,
		new org.radixware.kernel.common.types.Id[]{},
		false,
		null,
		new org.radixware.kernel.common.types.Id[]{},
		false,
		false,
		null,
		32,
		null,
		144,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprC6NQ7NSN2JDVHLEER72AUDY6II")},
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprC6NQ7NSN2JDVHLEER72AUDY6II")},
		false,true,false,0,0);
		columns = new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn[]{

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIUQVQZZKPFBRLG7DTABUYGLYBQ"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null)
		};
	}
	@Override
	protected org.radixware.kernel.common.client.models.Model createModelImpl(org.radixware.kernel.common.client.IClientEnvironment userSession) {    return new org.radixware.ads.CfgManagement.explorer.CfgItem.DefaultGroupModel(userSession,this);
	}
}
/* Radix::CfgManagement::CfgItem:InternalLinks - Desktop Meta*/

/*Radix::CfgManagement::CfgItem:InternalLinks-Selector Presentation*/

package org.radixware.ads.CfgManagement.explorer;
public final class InternalLinks_mi extends org.radixware.kernel.common.client.meta.RadSelectorPresentationDef{
	public static final org.radixware.kernel.common.client.meta.RadSelectorPresentationDef rdxMeta = new InternalLinks_mi();
	private InternalLinks_mi(){
		super(org.radixware.kernel.common.types.Id.Factory.loadFrom("sprMQEXFCGPZJBLVA7BZDI5QHQKRY"),
		"InternalLinks",
		org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("spr2PZI4TZ34JEQNKVR5AXO6PWPNI"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ4WZRLJ23BGMBKQ2JBSOBEGBP4"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("tblJ4WZRLJ23BGMBKQ2JBSOBEGBP4"),
		null,
		null,
		null,
		null,
		false,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("srtUSWUP7RXBJFXRMQNHD67PC2JIY"),
		null,
		false,
		true,
		null,
		0,null,
		16561,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprO3XHJ6U74NDNHJ7GNTDUUNDEWE")},
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprO3XHJ6U74NDNHJ7GNTDUUNDEWE")},
		false,true,false,0,0);
		columns = new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn[]{

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIUQVQZZKPFBRLG7DTABUYGLYBQ"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdY3N4QO6NSRDXTCHP27YWP3WONM"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3DNFCTYB3ZDJZCRY4OGG7I66YA"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHUYIZ7QPTBE7FAGC4FA76QNHPY"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.STRETCH,null)
		};
	}
	@Override
	protected org.radixware.kernel.common.client.models.Model createModelImpl(org.radixware.kernel.common.client.IClientEnvironment userSession) {    return new org.radixware.ads.CfgManagement.explorer.CfgItem.DefaultGroupModel(userSession,this);
	}
}
/* Radix::CfgManagement::CfgItem:NewObj:Model - Desktop Executable*/

/*Radix::CfgManagement::CfgItem:NewObj:Model-Filter Model Class*/

package org.radixware.ads.CfgManagement.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItem:NewObj:Model")
public class NewObj:Model  extends org.radixware.kernel.common.client.models.FilterModel  {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return NewObj:Model_mi.rdxMeta; }



	public NewObj:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.filters.RadFilterDef def){super(userSession,def);}

	/*Radix::CfgManagement::CfgItem:NewObj:Model:Nested classes-Nested Classes*/

	/*Radix::CfgManagement::CfgItem:NewObj:Model:Properties-Properties*/






	/*Radix::CfgManagement::CfgItem:NewObj:Model:Methods-Methods*/


}

/* Radix::CfgManagement::CfgItem:NewObj:Model - Desktop Meta*/

/*Radix::CfgManagement::CfgItem:NewObj:Model-Filter Model Class*/

package org.radixware.ads.CfgManagement.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class NewObj:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("fmcMZ5IEUI3RRB6HFDHHLHAFWJPAU"),
						"NewObj:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::CfgManagement::CfgItem:NewObj:Model:Properties-Properties*/
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

/* Radix::CfgManagement::CfgItem:Invalid:Model - Desktop Executable*/

/*Radix::CfgManagement::CfgItem:Invalid:Model-Filter Model Class*/

package org.radixware.ads.CfgManagement.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItem:Invalid:Model")
public class Invalid:Model  extends org.radixware.kernel.common.client.models.FilterModel  {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return Invalid:Model_mi.rdxMeta; }



	public Invalid:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.filters.RadFilterDef def){super(userSession,def);}

	/*Radix::CfgManagement::CfgItem:Invalid:Model:Nested classes-Nested Classes*/

	/*Radix::CfgManagement::CfgItem:Invalid:Model:Properties-Properties*/






	/*Radix::CfgManagement::CfgItem:Invalid:Model:Methods-Methods*/


}

/* Radix::CfgManagement::CfgItem:Invalid:Model - Desktop Meta*/

/*Radix::CfgManagement::CfgItem:Invalid:Model-Filter Model Class*/

package org.radixware.ads.CfgManagement.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Invalid:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("fmcNIWKPNVHINEJBIOBJXX5HVQTNQ"),
						"Invalid:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::CfgManagement::CfgItem:Invalid:Model:Properties-Properties*/
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

/* Radix::CfgManagement::CfgItem:ByExtGuid:Model - Desktop Executable*/

/*Radix::CfgManagement::CfgItem:ByExtGuid:Model-Filter Model Class*/

package org.radixware.ads.CfgManagement.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItem:ByExtGuid:Model")
public class ByExtGuid:Model  extends org.radixware.kernel.common.client.models.FilterModel  {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return ByExtGuid:Model_mi.rdxMeta; }



	public ByExtGuid:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.filters.RadFilterDef def){super(userSession,def);}

	/*Radix::CfgManagement::CfgItem:ByExtGuid:Model:Nested classes-Nested Classes*/

	/*Radix::CfgManagement::CfgItem:ByExtGuid:Model:Properties-Properties*/








	/*Radix::CfgManagement::CfgItem:ByExtGuid:Model:Methods-Methods*/

	/*Radix::CfgManagement::CfgItem:ByExtGuid:extGuid:extGuid-Presentation Property*/




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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItem:ByExtGuid:extGuid:extGuid")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItem:ByExtGuid:extGuid:extGuid")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ExtGuid getExtGuid(){return (ExtGuid)getProperty(prmG754KGK6GFCPHNAU2OY7BPT2EE);}


}

/* Radix::CfgManagement::CfgItem:ByExtGuid:Model - Desktop Meta*/

/*Radix::CfgManagement::CfgItem:ByExtGuid:Model-Filter Model Class*/

package org.radixware.ads.CfgManagement.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class ByExtGuid:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("fmc7HADR6IUNJCAXCROQ6V2PCLNJM"),
						"ByExtGuid:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::CfgManagement::CfgItem:ByExtGuid:Model:Properties-Properties*/
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

/* Radix::CfgManagement::CfgItem - Localizing Bundle */
package org.radixware.ads.CfgManagement.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class CfgItem - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Source object RID");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"RID объекта-источника");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4HPWC5L7GVA7PISXIBJJZE5YEY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Configuration Item");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Элемент конфигурации");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4IQTWLSCMBHWPKSXUKAW6CLNBA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Data");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Данные");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4O4JVRF2SZEPJFFIAOZILXAQDQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Ext Guid");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ext Guid");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5MSGQ7W7CBG45MHJQRU64OMJOY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Configuration Item");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5TCNLPBS3ZEUPJSV4C2ED2AVQM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"New Objects");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Новые объекты");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6S2464ZPMFFB5GLBXRDHQ6KKFI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Configuration Item");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Элемент конфигурации");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6SY6L3HI6JGJNIZAIOYXCHTCVQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Invalid");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Проблемные");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls73SIYHAFPRAU3A5IE3TAN5JEDA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"By Ext Guid");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"По Ext Guid");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsA7XD4C2TRJCRLG7SF5QLNPHLWM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Parent Id");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Parent Id");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsABQRNFHXYVB7FEVP36PUEISEAQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"References state");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Состояние связей");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAN46CQAYZBGSTLC7OGX5OMSPHU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Re-link");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Связать");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAW3EHYRWJNDG7MO5JKMTYRXTFQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Application version");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Версия приложения");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCOKELC4X3ZCMDNCTO3YSWRZ45Y"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Notes");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Заметки");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCSLRLP6FJZCK7ALAZ4IO7W7OIY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"ID");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ид.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDFKMMSN4INAUDCNMBXFAA2INFM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Ext Guid");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ext Guid");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsI7INV5WLYNDYXPBVLJQS6STZGA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Source object name");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Название объекта-источника");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJHNMAVJLAJCQ5LK2PMKMS4VYGI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Parent item");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Родительский элемент");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOZLSBAP3TZDDBM4AP5RUEGAIJ4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Re-link Child Items");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Связать дочерние элементы");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPLOBWSYLSVBZNEBLQCKGV242BM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Class");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Класс");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQBOW4TDPKZBXPASLYA7JL2WMJM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"By ID");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"По ид.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQPP64NALWBEBFHQCT3GQOJN5TQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Object state");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Состояние объекта");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQTUICRWBPVASTJHSPIAROKHM4Y"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Skip");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Не загружать");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsS2RWDNGY2VGTHNTKDR3GR26LLU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Configuration Item");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Элемент конфигурации");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTEIUJG7XOFHTLDV4QOAJHIUHGI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Package");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Пакет");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTHUGRUSSAFBAHPO3KAY6IGRPUA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Source object PID");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"PID объекта-источника");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTL4GYON4IVAA3FQLTLPV2DMDD4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Configuration Items");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Элементы конфигурации");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZV2E2CQV3ZAP7NL6Q5IZLENPQQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(CfgItem - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbaecJ4WZRLJ23BGMBKQ2JBSOBEGBP4"),"CfgItem - Localizing Bundle",$$$items$$$);
}
