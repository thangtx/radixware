
/* Radix::CfgManagement::Revision - Server Executable*/

/*Radix::CfgManagement::Revision-Entity Class*/

package org.radixware.ads.CfgManagement.server;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::Revision")
public final published class Revision  extends org.radixware.ads.Types.server.Entity  implements org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {


	/**Metainformation accessor method*/
	@SuppressWarnings("unused")
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return Revision_mi.rdxMeta;}

	/*Radix::CfgManagement::Revision:Nested classes-Nested Classes*/

	/*Radix::CfgManagement::Revision:Properties-Properties*/

	/*Radix::CfgManagement::Revision:time-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::Revision:time")
	public published  java.sql.Timestamp getTime() {
		return time;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::Revision:time")
	public published   void setTime(java.sql.Timestamp val) {
		time = val;
	}

	/*Radix::CfgManagement::Revision:seq-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::Revision:seq")
	public published  Int getSeq() {
		return seq;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::Revision:seq")
	public published   void setSeq(Int val) {
		seq = val;
	}

	/*Radix::CfgManagement::Revision:appVer-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::Revision:appVer")
	public published  Str getAppVer() {
		return appVer;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::Revision:appVer")
	public published   void setAppVer(Str val) {
		appVer = val;
	}

	/*Radix::CfgManagement::Revision:author-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::Revision:author")
	public published  Str getAuthor() {
		return author;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::Revision:author")
	public published   void setAuthor(Str val) {
		author = val;
	}

	/*Radix::CfgManagement::Revision:description-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::Revision:description")
	public published  Str getDescription() {
		return description;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::Revision:description")
	public published   void setDescription(Str val) {
		description = val;
	}

	/*Radix::CfgManagement::Revision:refDoc-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::Revision:refDoc")
	public published  Str getRefDoc() {
		return refDoc;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::Revision:refDoc")
	public published   void setRefDoc(Str val) {
		refDoc = val;
	}

	/*Radix::CfgManagement::Revision:localNotes-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::Revision:localNotes")
	public published  Str getLocalNotes() {
		return localNotes;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::Revision:localNotes")
	public published   void setLocalNotes(Str val) {
		localNotes = val;
	}

	/*Radix::CfgManagement::Revision:upDefId-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::Revision:upDefId")
	public published  Str getUpDefId() {
		return upDefId;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::Revision:upDefId")
	public published   void setUpDefId(Str val) {
		upDefId = val;
	}

	/*Radix::CfgManagement::Revision:upOwnerEntityId-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::Revision:upOwnerEntityId")
	public published  Str getUpOwnerEntityId() {
		return upOwnerEntityId;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::Revision:upOwnerEntityId")
	public published   void setUpOwnerEntityId(Str val) {
		upOwnerEntityId = val;
	}

	/*Radix::CfgManagement::Revision:upOwnerPid-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::Revision:upOwnerPid")
	public published  Str getUpOwnerPid() {
		return upOwnerPid;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::Revision:upOwnerPid")
	public published   void setUpOwnerPid(Str val) {
		upOwnerPid = val;
	}

	/*Radix::CfgManagement::Revision:changeLog-Parent Reference*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::Revision:changeLog")
	 published  org.radixware.ads.CfgManagement.server.ChangeLog getChangeLog() {
		return changeLog;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::Revision:changeLog")
	 published   void setChangeLog(org.radixware.ads.CfgManagement.server.ChangeLog val) {
		changeLog = val;
	}

	/*Radix::CfgManagement::Revision:lastRevisionSeq-Parent Property*/















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::Revision:lastRevisionSeq")
	  Int getLastRevisionSeq() {
		return lastRevisionSeq;
	}

	/*Radix::CfgManagement::Revision:id-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::Revision:id")
	public published  Int getId() {
		return id;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::Revision:id")
	public published   void setId(Int val) {
		id = val;
	}

	/*Radix::CfgManagement::Revision:kind-Column-Based Property*/








			















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::Revision:kind")
	public published  org.radixware.kernel.common.enums.EChangelogItemKind getKind() {
		return kind;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::Revision:kind")
	public published   void setKind(org.radixware.kernel.common.enums.EChangelogItemKind val) {
		kind = val;
	}













































































































	/*Radix::CfgManagement::Revision:Methods-Methods*/

	/*Radix::CfgManagement::Revision:loadByPidStr-System Method*/

	@SuppressWarnings("unused")
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::Revision:loadByPidStr")
	public static published  org.radixware.ads.CfgManagement.server.Revision loadByPidStr (Str pidAsStr, boolean checkExistance) {
	org.radixware.kernel.server.types.Pid pid = new org.radixware.kernel.server.types.Pid(org.radixware.ads.mdlPEKYFVDRVZHGZCBQQDY2NOYFOY.server.pdcArte______________________.__getArteInstanceInternal(),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblFBB5BWCUZZHEDICOSYNTHZ3ZNE"),pidAsStr);
		try{
		return (
		org.radixware.ads.CfgManagement.server.Revision) org.radixware.ads.mdlPEKYFVDRVZHGZCBQQDY2NOYFOY.server.pdcArte______________________.__getArteInstanceInternal().getEntityObject(pid,null,checkExistance);
	}catch(org.radixware.kernel.server.exceptions.EntityObjectNotExistsError e){
	return null;
	}

	}

	/*Radix::CfgManagement::Revision:loadByPK-System Method*/

	@SuppressWarnings("unused")
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::Revision:loadByPK")
	public static published  org.radixware.ads.CfgManagement.server.Revision loadByPK (Str upDefId, Str upOwnerEntityId, Str upOwnerPid, Int Id, boolean checkExistance) {
	final java.util.HashMap<org.radixware.kernel.common.types.Id,Object> pkValsMap = new java.util.HashMap<org.radixware.kernel.common.types.Id,Object>(11);
			if(upDefId==null) return null;
			pkValsMap.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHZU5LSFUFRC23MSA5WTUAAB2YE"),upDefId);
			if(upOwnerEntityId==null) return null;
			pkValsMap.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("colZKMFHS6KSBDZFDQSL6XFKD5EZE"),upOwnerEntityId);
			if(upOwnerPid==null) return null;
			pkValsMap.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("colOJDV73QKYVG4PGCAC3UY2G6F74"),upOwnerPid);
			if(Id==null) return null;
			pkValsMap.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("colZQUNS4MKIBCGXE7CSG6PA7ROPU"),Id);
	org.radixware.kernel.server.types.Pid pid = new org.radixware.kernel.server.types.Pid(org.radixware.ads.mdlPEKYFVDRVZHGZCBQQDY2NOYFOY.server.pdcArte______________________.__getArteInstanceInternal(),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblFBB5BWCUZZHEDICOSYNTHZ3ZNE"),pkValsMap);
		try{
		return (
		org.radixware.ads.CfgManagement.server.Revision) org.radixware.ads.mdlPEKYFVDRVZHGZCBQQDY2NOYFOY.server.pdcArte______________________.__getArteInstanceInternal().getEntityObject(pid,null,checkExistance);
	}catch(org.radixware.kernel.server.exceptions.EntityObjectNotExistsError e){
	return null;
	}

	}

	/*Radix::CfgManagement::Revision:afterInit-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::Revision:afterInit")
	protected published  void afterInit (org.radixware.kernel.server.types.Entity src, org.radixware.kernel.common.enums.EEntityInitializationPhase phase) {
		if (seq == null)
		    seq = ChangelogUtils.getNextRevisionSeq(lastRevisionSeq);

		super.afterInit(src, phase);
	}

	/*Radix::CfgManagement::Revision:exportXml-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::Revision:exportXml")
	  org.radixware.schemas.commondef.ChangeLogItem exportXml (boolean exportLocals) {
		Types::CommonDefXsd:ChangeLogItem xRev = Types::CommonDefXsd:ChangeLogItem.Factory.newInstance();
		xRev.RevisionNumber = seq;
		xRev.Date = time;
		xRev.Description = description;
		xRev.RefDoc = refDoc;
		xRev.AppVer = appVer;
		xRev.Author = author;
		if (exportLocals) {
		    xRev.LocalNotes = localNotes;
		}
		xRev.Kind = kind;
		return xRev;
	}

	/*Radix::CfgManagement::Revision:importRevision-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::Revision:importRevision")
	 static  org.radixware.ads.CfgManagement.server.Revision importRevision (org.radixware.ads.CfgManagement.server.ChangeLog owner, org.radixware.schemas.commondef.ChangeLogItem xRev) {
		Revision rev = new Revision();
		rev.init();

		rev.importData(owner, xRev);

		return rev;
	}

	/*Radix::CfgManagement::Revision:importData-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::Revision:importData")
	  void importData (org.radixware.ads.CfgManagement.server.ChangeLog owner, org.radixware.schemas.commondef.ChangeLogItem xRev) {
		upDefId = owner.upDefId;
		upOwnerEntityId = owner.upOwnerEntityId;
		upOwnerPid = owner.upOwnerPid;
		seq = xRev.RevisionNumber;

		description = xRev.Description;
		time = xRev.Date;
		author = xRev.Author;
		refDoc = xRev.RefDoc;
		appVer = xRev.AppVer;
		localNotes = xRev.LocalNotes;
		kind = getKind(xRev);
	}

	/*Radix::CfgManagement::Revision:addImportRevision-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::Revision:addImportRevision")
	 static  org.radixware.ads.CfgManagement.server.Revision addImportRevision (org.radixware.ads.CfgManagement.server.ChangeLog owner, org.radixware.ads.CfgManagement.server.CfgPacket packet) {
		Revision rev = new Revision();
		rev.init();

		rev.upDefId = owner.upDefId;
		rev.upOwnerEntityId = owner.upOwnerEntityId;
		rev.upOwnerPid = owner.upOwnerPid;
		rev.seq = null;

		rev.description = "Import of '" + packet.title + "' package";
		rev.time = new DateTime(new java.util.Date().getTime());
		rev.author = packet.srcExpUser;
		rev.refDoc = packet.title;
		rev.appVer = Arte::Arte.getAllLayerVersionsAsString();
		rev.localNotes = null;
		rev.kind = ChangelogItemKind:IMPORT;
		return rev;
	}

	/*Radix::CfgManagement::Revision:getKind-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::Revision:getKind")
	 static  org.radixware.kernel.common.enums.EChangelogItemKind getKind (org.radixware.schemas.commondef.ChangeLogItem xRev) {
		if (xRev.Kind != null) {
		    return xRev.Kind;
		} else {
		    return xRev.RevisionNumber != null
		            ? ChangelogItemKind:MODIFY : ChangelogItemKind:IMPORT;
		}
	}

	/*Radix::CfgManagement::Revision:getRevisionTitle-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::Revision:getRevisionTitle")
	 static  Str getRevisionTitle (org.radixware.schemas.commondef.ChangeLogItem xRevision) {
		if (xRevision == null) {
		    return null;
		}
		return ChangelogUtils.getPresentationString(
		        xRevision.RevisionNumber,
		        xRevision.Date.Time, xRevision.Author,
		        xRevision.RefDoc, xRevision.Description);
	}

	/*Radix::CfgManagement::Revision:onCalcTitle-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::Revision:onCalcTitle")
	protected published  Str onCalcTitle (Str title) {
		return super.onCalcTitle(title)
		    + (author == null ? "" : " | " + author)
		    + (refDoc == null ? "" : " | [" + refDoc + "]")
		    + (description == null ? "" : (refDoc == null ? " | " : " ") + description.replaceAll("\n", "  "));
	}

	/*Radix::CfgManagement::Revision:createRevisionXml-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::Revision:createRevisionXml")
	 static  org.radixware.schemas.commondef.ChangeLogItem createRevisionXml (Str description) {
		Types::CommonDefXsd:ChangeLogItem xRev = Types::CommonDefXsd:ChangeLogItem.Factory.newInstance();
		xRev.AppVer = Arte::Arte.getAllLayerVersionsAsString();
		xRev.Author = Arte::Arte.getUserName();
		xRev.Date = new DateTime(new java.util.Date().getTime());
		xRev.Description = description;
		return xRev;
	}


	@Override
	public org.radixware.kernel.server.types.FormHandler.NextDialogsRequest execCommand(org.radixware.kernel.common.types.Id cmdId, org.radixware.kernel.common.types.Id propId, org.apache.xmlbeans.XmlObject input, org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValsById, org.apache.xmlbeans.XmlObject output) throws org.radixware.kernel.common.exceptions.AppException,java.lang.InterruptedException{

			return super.execCommand(cmdId,propId,input,newPropValsById,output);
	}


}

/* Radix::CfgManagement::Revision - Server Meta*/

/*Radix::CfgManagement::Revision-Entity Class*/

package org.radixware.ads.CfgManagement.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Revision_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFBB5BWCUZZHEDICOSYNTHZ3ZNE"),"Revision",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsM4JAOZQLUBEHZEE2XKARMPKUII"),

						org.radixware.kernel.common.enums.EClassType.ENTITY,

						/*Radix::CfgManagement::Revision:Presentations-Entity Presentations*/
						/*Class presentation attributes*/
						 new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef(/*Owner Class Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFBB5BWCUZZHEDICOSYNTHZ3ZNE"),
							/*Owner Class Name*/
							"Revision",
							/*Title Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsM4JAOZQLUBEHZEE2XKARMPKUII"),
							/*Property presentations*/

							/*Radix::CfgManagement::Revision:Properties-Properties*/
							new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef[]{

									/*Radix::CfgManagement::Revision:time:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colTOVFRCOSNRELJK73KAENVCQPW4"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::CfgManagement::Revision:seq:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colSGRNYKKFMZHS3JWOVWIAQUPNF4"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::CfgManagement::Revision:appVer:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDONV65JHGFHXZKRSXXSEKMLG54"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::CfgManagement::Revision:author:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colWDKSRUEFVJHQPGKLPTNSSBSMWI"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::CfgManagement::Revision:description:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colZGV3WOV765HFTIQY4ZA6PKRMWI"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::CfgManagement::Revision:refDoc:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colBXT3L65R7VDOXNVYQ2OIMRQTHU"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::CfgManagement::Revision:localNotes:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJ6GHATEATRAIZOGDWKVGLNLSDQ"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::CfgManagement::Revision:lastRevisionSeq:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEQMPFBKHRVAJNANUSOWLFQB33I"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.TITLE,org.radixware.kernel.common.enums.EPropAttrInheritance.EDITING,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_SELECT_CONDITION,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_SELECTOR,org.radixware.kernel.common.enums.EPropAttrInheritance.HINT)),

									/*Radix::CfgManagement::Revision:kind:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colZ7I2LKY7R5F2TINIRJ4FASI53A"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT))
							},
							/*Commands*/
							null,
							/*Sortings*/
							new org.radixware.kernel.server.meta.presentations.RadSortingDef[]{

									new org.radixware.kernel.server.meta.presentations.RadSortingDef(
									/*Radix::CfgManagement::Revision:SeqDesc-Sorting*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("srtECIR6H6WEVEUZORM57U46Z5IBU"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFBB5BWCUZZHEDICOSYNTHZ3ZNE"),"SeqDesc",null,new org.radixware.kernel.server.meta.presentations.RadSortingDef.Item[]{

											new org.radixware.kernel.server.meta.presentations.RadSortingDef.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("colSGRNYKKFMZHS3JWOVWIAQUPNF4"),org.radixware.kernel.common.enums.EOrder.DESC)
									},"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),

									new org.radixware.kernel.server.meta.presentations.RadSortingDef(
									/*Radix::CfgManagement::Revision:DateDesc-Sorting*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("srtQOB6Q3KIRFALTLIEHAGUFNWEOM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFBB5BWCUZZHEDICOSYNTHZ3ZNE"),"DateDesc",null,new org.radixware.kernel.server.meta.presentations.RadSortingDef.Item[]{

											new org.radixware.kernel.server.meta.presentations.RadSortingDef.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("colTOVFRCOSNRELJK73KAENVCQPW4"),org.radixware.kernel.common.enums.EOrder.DESC),

											new org.radixware.kernel.server.meta.presentations.RadSortingDef.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("colSGRNYKKFMZHS3JWOVWIAQUPNF4"),org.radixware.kernel.common.enums.EOrder.DESC)
									},"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware")
							},
							/*Filters*/
							null,
							/*Editor presentations*/
							new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef[]{

									new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef(
									/*Radix::CfgManagement::Revision:General-Editor Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprA3W5TMJ2P5ELTOQMDLGZ6VXS7Y"),
									"General",
									org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
									null,
									2192,
									null,

									/*Radix::CfgManagement::Revision:General:Children-Explorer Items*/
										null
									,
									org.radixware.kernel.server.types.Restrictions.Factory.newInstance(0,null,null,null),
									(org.radixware.kernel.server.meta.presentations.RadPropertyPresentationAttributes[]) null,
									org.radixware.kernel.common.enums.EEditorPresentationRightsInheritanceMode.NONE,
									null,null),

									new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef(
									/*Radix::CfgManagement::Revision:Create-Editor Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprOJBSAEAAPZCHTMSSKK6XCMO2XI"),
									"Create",
									org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
									null,
									2192,
									null,

									/*Radix::CfgManagement::Revision:Create:Children-Explorer Items*/
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
									/*Radix::CfgManagement::Revision:General-Selector Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("spr7KE3MVJCXBEMVBY2IOQ27CH3FU"),"General",null,144,new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn[]{

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colSGRNYKKFMZHS3JWOVWIAQUPNF4"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colTOVFRCOSNRELJK73KAENVCQPW4"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colWDKSRUEFVJHQPGKLPTNSSBSMWI"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colBXT3L65R7VDOXNVYQ2OIMRQTHU"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colZGV3WOV765HFTIQY4ZA6PKRMWI"),true)
									},new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.Addons(org.radixware.kernel.common.types.Id.Factory.loadFrom("srtQOB6Q3KIRFALTLIEHAGUFNWEOM"),true,null,false,null,false,new org.radixware.kernel.common.types.Id[]{},false,false,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprA3W5TMJ2P5ELTOQMDLGZ6VXS7Y")},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprOJBSAEAAPZCHTMSSKK6XCMO2XI")},org.radixware.kernel.server.types.Restrictions.Factory.newInstance(32,null,null,null),null)
							},
							/*Default selector presentation Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("spr7KE3MVJCXBEMVBY2IOQ27CH3FU"),
							/*Presentation Map*/
							null,
							/*Object title format*/

							/*Radix::CfgManagement::Revision:TitleFormat-Object Title Format*/

							new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFBB5BWCUZZHEDICOSYNTHZ3ZNE"),new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.TitleItem[]{

									new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.TitleItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFBB5BWCUZZHEDICOSYNTHZ3ZNE"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colSGRNYKKFMZHS3JWOVWIAQUPNF4"),"{0})",org.radixware.kernel.common.enums.ETitleNullFormat.SAME_AS_NOT_NULL,null),

									new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.TitleItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFBB5BWCUZZHEDICOSYNTHZ3ZNE"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colTOVFRCOSNRELJK73KAENVCQPW4"),"{0, date, dd MMM yyyy HH:mm:ss}",org.radixware.kernel.common.enums.ETitleNullFormat.SAME_AS_NOT_NULL,null)
							},null,org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.DefinitionContextType.ENTITY),
							/*Class catalogs*/
							null,
							/*Restrictions*/
							org.radixware.kernel.server.types.Restrictions.Factory.newInstance(0,null,null,null),null,0),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblFBB5BWCUZZHEDICOSYNTHZ3ZNE"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("pdcEntity____________________"),

						/*Radix::CfgManagement::Revision:Properties-Properties*/
						new org.radixware.kernel.server.meta.clazzes.RadPropDef[]{

								/*Radix::CfgManagement::Revision:time-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colTOVFRCOSNRELJK73KAENVCQPW4"),"time",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNHFPT6LV55BL5BIVP3KOZS5XNU"),org.radixware.kernel.common.enums.EValType.DATE_TIME,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::CfgManagement::Revision:seq-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colSGRNYKKFMZHS3JWOVWIAQUPNF4"),"seq",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJMQA2YF37RHRBLPP3R3THI6KQM"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::CfgManagement::Revision:appVer-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDONV65JHGFHXZKRSXXSEKMLG54"),"appVer",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsO7WLJD5VSJHWDMTU3YUD2LU4OY"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::CfgManagement::Revision:author-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colWDKSRUEFVJHQPGKLPTNSSBSMWI"),"author",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsC3LMVWRH3FBOJKUU7BP5LLAZWQ"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::CfgManagement::Revision:description-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colZGV3WOV765HFTIQY4ZA6PKRMWI"),"description",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLQTSV4EHT5CKLP5CM4WY5C6MHM"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::CfgManagement::Revision:refDoc-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colBXT3L65R7VDOXNVYQ2OIMRQTHU"),"refDoc",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJLLA4AISSREJ5IY5EXBIEY6CMI"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::CfgManagement::Revision:localNotes-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJ6GHATEATRAIZOGDWKVGLNLSDQ"),"localNotes",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFX7NCVW5UBGQXLHCEFO4H5H5EE"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::CfgManagement::Revision:upDefId-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHZU5LSFUFRC23MSA5WTUAAB2YE"),"upDefId",null,org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::CfgManagement::Revision:upOwnerEntityId-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colZKMFHS6KSBDZFDQSL6XFKD5EZE"),"upOwnerEntityId",null,org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::CfgManagement::Revision:upOwnerPid-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colOJDV73QKYVG4PGCAC3UY2G6F74"),"upOwnerPid",null,org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::CfgManagement::Revision:changeLog-Parent Reference*/

								new org.radixware.kernel.server.meta.clazzes.RadInnateRefPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIIO6FAXYWNHRBL3QKQXJPMWCL4"),"changeLog",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEYCDXBU5ZNEFNA7B6DEFBU42JI"),org.radixware.kernel.common.types.Id.Factory.loadFrom("refHZB6WVXTNVB73FJVURPGTGNCJY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecVHY5COSHWNA7PPJ65FW3OGJ4ZQ"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::CfgManagement::Revision:lastRevisionSeq-Parent Property*/

								new org.radixware.kernel.server.meta.clazzes.RadParentPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEQMPFBKHRVAJNANUSOWLFQB33I"),"lastRevisionSeq",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYKHQDVERLBC77FMZ6ZETVA5MAE"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colIIO6FAXYWNHRBL3QKQXJPMWCL4")},org.radixware.kernel.common.types.Id.Factory.loadFrom("prdCE2UEVQLLFCOLFACCXZPS263HM"),org.radixware.kernel.server.meta.clazzes.RadPropReadAccessorProxy.R_ACCESSOR_INSTANCE),

								/*Radix::CfgManagement::Revision:id-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colZQUNS4MKIBCGXE7CSG6PA7ROPU"),"id",null,org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::CfgManagement::Revision:kind-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colZ7I2LKY7R5F2TINIRJ4FASI53A"),"kind",null,org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("acsR5HPROEK5NDFBLF4RUO25UK3A4"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("MODIFY")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE)
						},

						/*Radix::CfgManagement::Revision:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth_loadByPidStr_____________"),"loadByPidStr",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("pidAsStr",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprPidAsStr__________________")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("checkExistance",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprCheckExisstance___________"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth_loadByPK_________________"),"loadByPK",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("upDefId",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprHZU5LSFUFRC23MSA5WTUAAB2YE")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("upOwnerEntityId",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprZKMFHS6KSBDZFDQSL6XFKD5EZE")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("upOwnerPid",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprOJDV73QKYVG4PGCAC3UY2G6F74")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("Id",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprZQUNS4MKIBCGXE7CSG6PA7ROPU")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("checkExistance",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprCheckExisstance___________"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthD77F2HBL2FGSVPJHR3SEAMPWMA"),"afterInit",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("src",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprVPPIYFZEOBG6PC2FBJO34EPSKM")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("phase",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr7HSAU3ZYT5DEVND3NZILPAA7VY"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthMGWAAZQTWVD4BFBXP5FJKXXQYQ"),"exportXml",false,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("exportLocals",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprA75TM54NUNFAFC74T65HEH5UXQ"))
								},org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthSGEDCDE76RAD5IJO5DWKHNJ7QU"),"importRevision",true,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("owner",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprMZC7V5LJ7NDHBGEHXYKFGK2TJU")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("xRev",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprKCU6MM5NINACFMRO4T2ZKILRU4"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth5Z3GFSVGONGTHM4KBYRD6LMX4M"),"importData",false,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("owner",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr6V6FF6FG6VCQJBKY6OCRL4EBPU")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("xRev",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprIR3XBTSDTFG27CMJEVE3RJA45A"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthTSFG22EBXFEA7JVDTEAE76RAPI"),"addImportRevision",true,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("owner",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprMZC7V5LJ7NDHBGEHXYKFGK2TJU")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("packet",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr6Y3U3ANQUFFKJF5QBJ6GTNVPYY"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthQ7D63WVJ5JGDFBYO24XIUZAM7E"),"getKind",true,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("xRev",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprHBFNTTSWUJABVLU3UBLEDDVD3Y"))
								},org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthBAQSY7Z2ARCNZFVEQ7QUQH5FGI"),"getRevisionTitle",true,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("xRevision",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprOMHUKYUHD5GQDIGM5TTY2VLW3M"))
								},org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth4D6ME6VX45BYVNJXZPBTAA7KP4"),"onCalcTitle",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("title",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprM3AJWY7R5BCTJPRDXFP47EHRIU"))
								},org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthD5EV2Q7IVNC2PPZHGXTOM2OI54"),"createRevisionXml",true,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("description",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr2QQR4ENCV5BDNEQ5NAVQ6W2AEQ"))
								},org.radixware.kernel.common.enums.EValType.XML)
						},
						null,
						org.radixware.kernel.common.enums.EAccessAreaType.NONE,null,null,false);
}

/* Radix::CfgManagement::Revision - Desktop Executable*/

/*Radix::CfgManagement::Revision-Entity Class*/

package org.radixware.ads.CfgManagement.explorer;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::Revision")
public interface Revision {
	/*Group model adapter class. This is the base class for 
	all of selector presentation models of this entity*/
	public static class DefaultGroupModel extends org.radixware.kernel.common.client.models.GroupModel{
		public DefaultGroupModel(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadSelectorPresentationDef def){super(userSession,def);}
		public org.radixware.ads.CfgManagement.explorer.Revision.Revision_DefaultModel getEntity(int i) throws InterruptedException, org.radixware.kernel.common.client.exceptions.BrokenEntityObjectException, org.radixware.kernel.common.exceptions.ServiceClientException { return (org.radixware.ads.CfgManagement.explorer.Revision.Revision_DefaultModel )  super.getEntity(i);}
	}



































































	/*Radix::CfgManagement::Revision:refDoc:refDoc-Presentation Property*/


	public class RefDoc extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public RefDoc(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::Revision:refDoc:refDoc")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::Revision:refDoc:refDoc")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public RefDoc getRefDoc();
	/*Radix::CfgManagement::Revision:appVer:appVer-Presentation Property*/


	public class AppVer extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public AppVer(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::Revision:appVer:appVer")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::Revision:appVer:appVer")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public AppVer getAppVer();
	/*Radix::CfgManagement::Revision:lastRevisionSeq:lastRevisionSeq-Presentation Property*/


	public class LastRevisionSeq extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public LastRevisionSeq(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::Revision:lastRevisionSeq:lastRevisionSeq")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::Revision:lastRevisionSeq:lastRevisionSeq")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public LastRevisionSeq getLastRevisionSeq();
	/*Radix::CfgManagement::Revision:localNotes:localNotes-Presentation Property*/


	public class LocalNotes extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public LocalNotes(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::Revision:localNotes:localNotes")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::Revision:localNotes:localNotes")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public LocalNotes getLocalNotes();
	/*Radix::CfgManagement::Revision:seq:seq-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::Revision:seq:seq")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::Revision:seq:seq")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public Seq getSeq();
	/*Radix::CfgManagement::Revision:time:time-Presentation Property*/


	public class Time extends org.radixware.kernel.common.client.models.items.properties.PropertyDateTime{
		public Time(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::Revision:time:time")
		public  java.sql.Timestamp getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::Revision:time:time")
		public   void setValue(java.sql.Timestamp val) {
			Value = val;
		}
	}
	public Time getTime();
	/*Radix::CfgManagement::Revision:author:author-Presentation Property*/


	public class Author extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public Author(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::Revision:author:author")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::Revision:author:author")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Author getAuthor();
	/*Radix::CfgManagement::Revision:kind:kind-Presentation Property*/


	public class Kind extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public Kind(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public void setServerValue(org.radixware.kernel.common.client.models.items.properties.PropertyValue val){
			Object x = val.getValue();
			org.radixware.kernel.common.enums.EChangelogItemKind dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EChangelogItemKind ? (org.radixware.kernel.common.enums.EChangelogItemKind)x : org.radixware.kernel.common.enums.EChangelogItemKind.getForValue((Str)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.STR)));
			val.refineValue(dummy);
			super.setServerValue(val);
		}
		@Override
		public Class<org.radixware.kernel.common.enums.EChangelogItemKind> getValClass(){
			return org.radixware.kernel.common.enums.EChangelogItemKind.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.enums.EChangelogItemKind dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EChangelogItemKind ? (org.radixware.kernel.common.enums.EChangelogItemKind)x : org.radixware.kernel.common.enums.EChangelogItemKind.getForValue((Str)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.STR)));
			setValue(dummy);
		}





				














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::Revision:kind:kind")
		public  org.radixware.kernel.common.enums.EChangelogItemKind getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::Revision:kind:kind")
		public   void setValue(org.radixware.kernel.common.enums.EChangelogItemKind val) {
			Value = val;
		}
	}
	public Kind getKind();
	/*Radix::CfgManagement::Revision:description:description-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::Revision:description:description")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::Revision:description:description")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Description getDescription();


}

/* Radix::CfgManagement::Revision - Desktop Meta*/

/*Radix::CfgManagement::Revision-Entity Class*/

package org.radixware.ads.CfgManagement.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Revision_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::CfgManagement::Revision:Presentations-Entity Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFBB5BWCUZZHEDICOSYNTHZ3ZNE"),
			"Radix::CfgManagement::Revision",
			null,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("spr7KE3MVJCXBEMVBY2IOQ27CH3FU"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsM4JAOZQLUBEHZEE2XKARMPKUII"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYGAKF6PASNCQ5K6R24IUH7CRPQ"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsM4JAOZQLUBEHZEE2XKARMPKUII"),0,

			/*Radix::CfgManagement::Revision:Properties-Properties*/
			new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

					/*Radix::CfgManagement::Revision:time:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colTOVFRCOSNRELJK73KAENVCQPW4"),
						"time",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNHFPT6LV55BL5BIVP3KOZS5XNU"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFBB5BWCUZZHEDICOSYNTHZ3ZNE"),
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
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::CfgManagement::Revision:time:PropertyPresentation:Edit Options:-Edit Mask Date Time*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskDateTime(org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,null,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::CfgManagement::Revision:seq:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colSGRNYKKFMZHS3JWOVWIAQUPNF4"),
						"seq",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJMQA2YF37RHRBLPP3R3THI6KQM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFBB5BWCUZZHEDICOSYNTHZ3ZNE"),
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

						/*Radix::CfgManagement::Revision:seq:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::CfgManagement::Revision:appVer:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDONV65JHGFHXZKRSXXSEKMLG54"),
						"appVer",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsO7WLJD5VSJHWDMTU3YUD2LU4OY"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFBB5BWCUZZHEDICOSYNTHZ3ZNE"),
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

						/*Radix::CfgManagement::Revision:appVer:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::CfgManagement::Revision:author:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colWDKSRUEFVJHQPGKLPTNSSBSMWI"),
						"author",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsC3LMVWRH3FBOJKUU7BP5LLAZWQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFBB5BWCUZZHEDICOSYNTHZ3ZNE"),
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

						/*Radix::CfgManagement::Revision:author:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,250,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::CfgManagement::Revision:description:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colZGV3WOV765HFTIQY4ZA6PKRMWI"),
						"description",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLQTSV4EHT5CKLP5CM4WY5C6MHM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFBB5BWCUZZHEDICOSYNTHZ3ZNE"),
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

						/*Radix::CfgManagement::Revision:description:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,false),
						null,
						null,
						null,
						true,-1,-1,1,
						true,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::CfgManagement::Revision:refDoc:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colBXT3L65R7VDOXNVYQ2OIMRQTHU"),
						"refDoc",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJLLA4AISSREJ5IY5EXBIEY6CMI"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFBB5BWCUZZHEDICOSYNTHZ3ZNE"),
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

						/*Radix::CfgManagement::Revision:refDoc:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,200,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::CfgManagement::Revision:localNotes:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJ6GHATEATRAIZOGDWKVGLNLSDQ"),
						"localNotes",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFX7NCVW5UBGQXLHCEFO4H5H5EE"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFBB5BWCUZZHEDICOSYNTHZ3ZNE"),
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

						/*Radix::CfgManagement::Revision:localNotes:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,true),
						null,
						null,
						null,
						true,-1,-1,1,
						true,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::CfgManagement::Revision:lastRevisionSeq:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEQMPFBKHRVAJNANUSOWLFQB33I"),
						"lastRevisionSeq",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFBB5BWCUZZHEDICOSYNTHZ3ZNE"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.PARENT_PROP,
						63,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecVHY5COSHWNA7PPJ65FW3OGJ4ZQ"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("prdCE2UEVQLLFCOLFACCXZPS263HM"),
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

						/*Radix::CfgManagement::Revision:lastRevisionSeq:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(null,null,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::CfgManagement::Revision:kind:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colZ7I2LKY7R5F2TINIRJ4FASI53A"),
						"kind",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFBB5BWCUZZHEDICOSYNTHZ3ZNE"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acsR5HPROEK5NDFBLF4RUO25UK3A4"),
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("MODIFY"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::CfgManagement::Revision:kind:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acsR5HPROEK5NDFBLF4RUO25UK3A4"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.NONE,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false)
			},
			null,
			null,
			new org.radixware.kernel.common.client.meta.RadSortingDef[]{

					new org.radixware.kernel.common.client.meta.RadSortingDef(
					/*Radix::CfgManagement::Revision:SeqDesc-Sorting*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("srtECIR6H6WEVEUZORM57U46Z5IBU"),
						"SeqDesc",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFBB5BWCUZZHEDICOSYNTHZ3ZNE"),
						null,
						new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem[]{

								new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colSGRNYKKFMZHS3JWOVWIAQUPNF4"),
								true)
						}),

					new org.radixware.kernel.common.client.meta.RadSortingDef(
					/*Radix::CfgManagement::Revision:DateDesc-Sorting*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("srtQOB6Q3KIRFALTLIEHAGUFNWEOM"),
						"DateDesc",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFBB5BWCUZZHEDICOSYNTHZ3ZNE"),
						null,
						new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem[]{

								new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colTOVFRCOSNRELJK73KAENVCQPW4"),
								true),

								new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colSGRNYKKFMZHS3JWOVWIAQUPNF4"),
								true)
						})
			},
			new org.radixware.kernel.common.client.meta.RadReferenceDef[]{

					new org.radixware.kernel.common.client.meta.RadReferenceDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("refHZB6WVXTNVB73FJVURPGTGNCJY"),"Revision=>ChangeLog (upDefId=>upDefId, upOwnerEntityId=>upOwnerEntityId, upOwnerPid=>upOwnerPid)",org.radixware.kernel.common.types.Id.Factory.loadFrom("tblFBB5BWCUZZHEDICOSYNTHZ3ZNE"),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblVHY5COSHWNA7PPJ65FW3OGJ4ZQ"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colZKMFHS6KSBDZFDQSL6XFKD5EZE"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colOJDV73QKYVG4PGCAC3UY2G6F74"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colHZU5LSFUFRC23MSA5WTUAAB2YE")},new String[]{"upOwnerEntityId","upOwnerPid","upDefId"},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colDNQQV5SM4VAWTCKEOAEDTUXAMI"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colRGHQSIYJKJALXJACD42AGVY2VI"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colYZPLJDVNTRBOZGTGEVP7OFHURE")},new String[]{"upOwnerEntityId","upOwnerPid","upDefId"})
			},
			new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprA3W5TMJ2P5ELTOQMDLGZ6VXS7Y"),org.radixware.kernel.common.types.Id.Factory.loadFrom("eprOJBSAEAAPZCHTMSSKK6XCMO2XI"),org.radixware.kernel.common.types.Id.Factory.loadFrom("spr7KE3MVJCXBEMVBY2IOQ27CH3FU")},
			false,false,false);
}

/* Radix::CfgManagement::Revision - Web Executable*/

/*Radix::CfgManagement::Revision-Entity Class*/

package org.radixware.ads.CfgManagement.web;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::Revision")
public interface Revision {
	/*Group model adapter class. This is the base class for 
	all of selector presentation models of this entity*/
	public static class DefaultGroupModel extends org.radixware.kernel.common.client.models.GroupModel{
		public DefaultGroupModel(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadSelectorPresentationDef def){super(userSession,def);}
		public org.radixware.ads.CfgManagement.web.Revision.Revision_DefaultModel getEntity(int i) throws InterruptedException, org.radixware.kernel.common.client.exceptions.BrokenEntityObjectException, org.radixware.kernel.common.exceptions.ServiceClientException { return (org.radixware.ads.CfgManagement.web.Revision.Revision_DefaultModel )  super.getEntity(i);}
	}



































































	/*Radix::CfgManagement::Revision:refDoc:refDoc-Presentation Property*/


	public class RefDoc extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public RefDoc(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::Revision:refDoc:refDoc")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::Revision:refDoc:refDoc")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public RefDoc getRefDoc();
	/*Radix::CfgManagement::Revision:appVer:appVer-Presentation Property*/


	public class AppVer extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public AppVer(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::Revision:appVer:appVer")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::Revision:appVer:appVer")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public AppVer getAppVer();
	/*Radix::CfgManagement::Revision:lastRevisionSeq:lastRevisionSeq-Presentation Property*/


	public class LastRevisionSeq extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public LastRevisionSeq(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::Revision:lastRevisionSeq:lastRevisionSeq")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::Revision:lastRevisionSeq:lastRevisionSeq")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public LastRevisionSeq getLastRevisionSeq();
	/*Radix::CfgManagement::Revision:localNotes:localNotes-Presentation Property*/


	public class LocalNotes extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public LocalNotes(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::Revision:localNotes:localNotes")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::Revision:localNotes:localNotes")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public LocalNotes getLocalNotes();
	/*Radix::CfgManagement::Revision:seq:seq-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::Revision:seq:seq")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::Revision:seq:seq")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public Seq getSeq();
	/*Radix::CfgManagement::Revision:time:time-Presentation Property*/


	public class Time extends org.radixware.kernel.common.client.models.items.properties.PropertyDateTime{
		public Time(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::Revision:time:time")
		public  java.sql.Timestamp getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::Revision:time:time")
		public   void setValue(java.sql.Timestamp val) {
			Value = val;
		}
	}
	public Time getTime();
	/*Radix::CfgManagement::Revision:author:author-Presentation Property*/


	public class Author extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public Author(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::Revision:author:author")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::Revision:author:author")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Author getAuthor();
	/*Radix::CfgManagement::Revision:kind:kind-Presentation Property*/


	public class Kind extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public Kind(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public void setServerValue(org.radixware.kernel.common.client.models.items.properties.PropertyValue val){
			Object x = val.getValue();
			org.radixware.kernel.common.enums.EChangelogItemKind dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EChangelogItemKind ? (org.radixware.kernel.common.enums.EChangelogItemKind)x : org.radixware.kernel.common.enums.EChangelogItemKind.getForValue((Str)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.STR)));
			val.refineValue(dummy);
			super.setServerValue(val);
		}
		@Override
		public Class<org.radixware.kernel.common.enums.EChangelogItemKind> getValClass(){
			return org.radixware.kernel.common.enums.EChangelogItemKind.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.enums.EChangelogItemKind dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EChangelogItemKind ? (org.radixware.kernel.common.enums.EChangelogItemKind)x : org.radixware.kernel.common.enums.EChangelogItemKind.getForValue((Str)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.STR)));
			setValue(dummy);
		}





				














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::Revision:kind:kind")
		public  org.radixware.kernel.common.enums.EChangelogItemKind getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::Revision:kind:kind")
		public   void setValue(org.radixware.kernel.common.enums.EChangelogItemKind val) {
			Value = val;
		}
	}
	public Kind getKind();
	/*Radix::CfgManagement::Revision:description:description-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::Revision:description:description")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::Revision:description:description")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Description getDescription();


}

/* Radix::CfgManagement::Revision - Web Meta*/

/*Radix::CfgManagement::Revision-Entity Class*/

package org.radixware.ads.CfgManagement.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Revision_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::CfgManagement::Revision:Presentations-Entity Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFBB5BWCUZZHEDICOSYNTHZ3ZNE"),
			"Radix::CfgManagement::Revision",
			null,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("spr7KE3MVJCXBEMVBY2IOQ27CH3FU"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsM4JAOZQLUBEHZEE2XKARMPKUII"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYGAKF6PASNCQ5K6R24IUH7CRPQ"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsM4JAOZQLUBEHZEE2XKARMPKUII"),0,

			/*Radix::CfgManagement::Revision:Properties-Properties*/
			new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

					/*Radix::CfgManagement::Revision:time:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colTOVFRCOSNRELJK73KAENVCQPW4"),
						"time",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNHFPT6LV55BL5BIVP3KOZS5XNU"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFBB5BWCUZZHEDICOSYNTHZ3ZNE"),
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
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::CfgManagement::Revision:time:PropertyPresentation:Edit Options:-Edit Mask Date Time*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskDateTime(org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,null,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::CfgManagement::Revision:seq:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colSGRNYKKFMZHS3JWOVWIAQUPNF4"),
						"seq",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJMQA2YF37RHRBLPP3R3THI6KQM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFBB5BWCUZZHEDICOSYNTHZ3ZNE"),
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

						/*Radix::CfgManagement::Revision:seq:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::CfgManagement::Revision:appVer:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDONV65JHGFHXZKRSXXSEKMLG54"),
						"appVer",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsO7WLJD5VSJHWDMTU3YUD2LU4OY"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFBB5BWCUZZHEDICOSYNTHZ3ZNE"),
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

						/*Radix::CfgManagement::Revision:appVer:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::CfgManagement::Revision:author:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colWDKSRUEFVJHQPGKLPTNSSBSMWI"),
						"author",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsC3LMVWRH3FBOJKUU7BP5LLAZWQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFBB5BWCUZZHEDICOSYNTHZ3ZNE"),
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

						/*Radix::CfgManagement::Revision:author:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,250,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::CfgManagement::Revision:description:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colZGV3WOV765HFTIQY4ZA6PKRMWI"),
						"description",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLQTSV4EHT5CKLP5CM4WY5C6MHM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFBB5BWCUZZHEDICOSYNTHZ3ZNE"),
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

						/*Radix::CfgManagement::Revision:description:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,false),
						null,
						null,
						null,
						true,-1,-1,1,
						true,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::CfgManagement::Revision:refDoc:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colBXT3L65R7VDOXNVYQ2OIMRQTHU"),
						"refDoc",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJLLA4AISSREJ5IY5EXBIEY6CMI"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFBB5BWCUZZHEDICOSYNTHZ3ZNE"),
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

						/*Radix::CfgManagement::Revision:refDoc:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,200,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::CfgManagement::Revision:localNotes:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJ6GHATEATRAIZOGDWKVGLNLSDQ"),
						"localNotes",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFX7NCVW5UBGQXLHCEFO4H5H5EE"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFBB5BWCUZZHEDICOSYNTHZ3ZNE"),
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

						/*Radix::CfgManagement::Revision:localNotes:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,true),
						null,
						null,
						null,
						true,-1,-1,1,
						true,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::CfgManagement::Revision:lastRevisionSeq:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEQMPFBKHRVAJNANUSOWLFQB33I"),
						"lastRevisionSeq",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFBB5BWCUZZHEDICOSYNTHZ3ZNE"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.PARENT_PROP,
						63,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecVHY5COSHWNA7PPJ65FW3OGJ4ZQ"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("prdCE2UEVQLLFCOLFACCXZPS263HM"),
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

						/*Radix::CfgManagement::Revision:lastRevisionSeq:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(null,null,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::CfgManagement::Revision:kind:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colZ7I2LKY7R5F2TINIRJ4FASI53A"),
						"kind",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFBB5BWCUZZHEDICOSYNTHZ3ZNE"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acsR5HPROEK5NDFBLF4RUO25UK3A4"),
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("MODIFY"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::CfgManagement::Revision:kind:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acsR5HPROEK5NDFBLF4RUO25UK3A4"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.NONE,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false)
			},
			null,
			null,
			new org.radixware.kernel.common.client.meta.RadSortingDef[]{

					new org.radixware.kernel.common.client.meta.RadSortingDef(
					/*Radix::CfgManagement::Revision:SeqDesc-Sorting*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("srtECIR6H6WEVEUZORM57U46Z5IBU"),
						"SeqDesc",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFBB5BWCUZZHEDICOSYNTHZ3ZNE"),
						null,
						new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem[]{

								new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colSGRNYKKFMZHS3JWOVWIAQUPNF4"),
								true)
						}),

					new org.radixware.kernel.common.client.meta.RadSortingDef(
					/*Radix::CfgManagement::Revision:DateDesc-Sorting*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("srtQOB6Q3KIRFALTLIEHAGUFNWEOM"),
						"DateDesc",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFBB5BWCUZZHEDICOSYNTHZ3ZNE"),
						null,
						new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem[]{

								new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colTOVFRCOSNRELJK73KAENVCQPW4"),
								true),

								new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colSGRNYKKFMZHS3JWOVWIAQUPNF4"),
								true)
						})
			},
			new org.radixware.kernel.common.client.meta.RadReferenceDef[]{

					new org.radixware.kernel.common.client.meta.RadReferenceDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("refHZB6WVXTNVB73FJVURPGTGNCJY"),"Revision=>ChangeLog (upDefId=>upDefId, upOwnerEntityId=>upOwnerEntityId, upOwnerPid=>upOwnerPid)",org.radixware.kernel.common.types.Id.Factory.loadFrom("tblFBB5BWCUZZHEDICOSYNTHZ3ZNE"),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblVHY5COSHWNA7PPJ65FW3OGJ4ZQ"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colZKMFHS6KSBDZFDQSL6XFKD5EZE"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colOJDV73QKYVG4PGCAC3UY2G6F74"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colHZU5LSFUFRC23MSA5WTUAAB2YE")},new String[]{"upOwnerEntityId","upOwnerPid","upDefId"},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colDNQQV5SM4VAWTCKEOAEDTUXAMI"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colRGHQSIYJKJALXJACD42AGVY2VI"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colYZPLJDVNTRBOZGTGEVP7OFHURE")},new String[]{"upOwnerEntityId","upOwnerPid","upDefId"})
			},
			new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprA3W5TMJ2P5ELTOQMDLGZ6VXS7Y"),org.radixware.kernel.common.types.Id.Factory.loadFrom("eprOJBSAEAAPZCHTMSSKK6XCMO2XI"),org.radixware.kernel.common.types.Id.Factory.loadFrom("spr7KE3MVJCXBEMVBY2IOQ27CH3FU")},
			false,false,false);
}

/* Radix::CfgManagement::Revision:General - Desktop Meta*/

/*Radix::CfgManagement::Revision:General-Editor Presentation*/

package org.radixware.ads.CfgManagement.explorer;
public final class General_mi{
	private static final class General_DEF extends org.radixware.kernel.common.client.meta.RadEditorPresentationDef{
		General_DEF(){
			super(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprA3W5TMJ2P5ELTOQMDLGZ6VXS7Y"),
			"General",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFBB5BWCUZZHEDICOSYNTHZ3ZNE"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("tblFBB5BWCUZZHEDICOSYNTHZ3ZNE"),
			null,
			null,

			/*Radix::CfgManagement::Revision:General:Editor Pages-Editor Presentation Pages*/
			new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

					/*Radix::CfgManagement::Revision:General:General-Editor Page*/

					 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgMSFGH35MJBE6LLTNAFJ2PM2XSU"),"General",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFBB5BWCUZZHEDICOSYNTHZ3ZNE"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsC667KCX46RE37IS4KJ73ADZK3E"),null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colWDKSRUEFVJHQPGKLPTNSSBSMWI"),0,1,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colZGV3WOV765HFTIQY4ZA6PKRMWI"),0,3,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colTOVFRCOSNRELJK73KAENVCQPW4"),0,2,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDONV65JHGFHXZKRSXXSEKMLG54"),0,4,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colBXT3L65R7VDOXNVYQ2OIMRQTHU"),0,5,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJ6GHATEATRAIZOGDWKVGLNLSDQ"),0,6,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colSGRNYKKFMZHS3JWOVWIAQUPNF4"),0,0,1,false,false)
					},null)
			},
			new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
				new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgMSFGH35MJBE6LLTNAFJ2PM2XSU"))}
			,

			/*Radix::CfgManagement::Revision:General:Children-Explorer Items*/
				null
			,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
			null,
			0,
			null,
			(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
			2192,0,0,null);
		}
		@Override
		protected org.radixware.kernel.common.client.models.Model createModelImpl(org.radixware.kernel.common.client.IClientEnvironment userSession) {    return new org.radixware.ads.CfgManagement.explorer.Revision.Revision_DefaultModel(userSession,this);
		}
	}
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new General_DEF(); 
;
}
/* Radix::CfgManagement::Revision:General - Web Meta*/

/*Radix::CfgManagement::Revision:General-Editor Presentation*/

package org.radixware.ads.CfgManagement.web;
public final class General_mi{
	private static final class General_DEF extends org.radixware.kernel.common.client.meta.RadEditorPresentationDef{
		General_DEF(){
			super(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprA3W5TMJ2P5ELTOQMDLGZ6VXS7Y"),
			"General",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFBB5BWCUZZHEDICOSYNTHZ3ZNE"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("tblFBB5BWCUZZHEDICOSYNTHZ3ZNE"),
			null,
			null,

			/*Radix::CfgManagement::Revision:General:Editor Pages-Editor Presentation Pages*/
			new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

					/*Radix::CfgManagement::Revision:General:General-Editor Page*/

					 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgMSFGH35MJBE6LLTNAFJ2PM2XSU"),"General",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFBB5BWCUZZHEDICOSYNTHZ3ZNE"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsC667KCX46RE37IS4KJ73ADZK3E"),null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colWDKSRUEFVJHQPGKLPTNSSBSMWI"),0,1,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colZGV3WOV765HFTIQY4ZA6PKRMWI"),0,3,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colTOVFRCOSNRELJK73KAENVCQPW4"),0,2,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDONV65JHGFHXZKRSXXSEKMLG54"),0,4,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colBXT3L65R7VDOXNVYQ2OIMRQTHU"),0,5,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJ6GHATEATRAIZOGDWKVGLNLSDQ"),0,6,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colSGRNYKKFMZHS3JWOVWIAQUPNF4"),0,0,1,false,false)
					},null)
			},
			new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
				new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgMSFGH35MJBE6LLTNAFJ2PM2XSU"))}
			,

			/*Radix::CfgManagement::Revision:General:Children-Explorer Items*/
				null
			,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
			null,
			0,
			null,
			(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
			2192,0,0,null);
		}
		@Override
		protected org.radixware.kernel.common.client.models.Model createModelImpl(org.radixware.kernel.common.client.IClientEnvironment userSession) {    return new org.radixware.ads.CfgManagement.web.Revision.Revision_DefaultModel(userSession,this);
		}
	}
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new General_DEF(); 
;
}
/* Radix::CfgManagement::Revision:Create - Desktop Meta*/

/*Radix::CfgManagement::Revision:Create-Editor Presentation*/

package org.radixware.ads.CfgManagement.explorer;
public final class Create_mi{
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new org.radixware.kernel.common.client.meta.RadEditorPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprOJBSAEAAPZCHTMSSKK6XCMO2XI"),
	"Create",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
	null,
	org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFBB5BWCUZZHEDICOSYNTHZ3ZNE"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("tblFBB5BWCUZZHEDICOSYNTHZ3ZNE"),
	null,
	null,

	/*Radix::CfgManagement::Revision:Create:Editor Pages-Editor Presentation Pages*/
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

			/*Radix::CfgManagement::Revision:Create:General-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgKTXX7BBPJNDMTO3GOTJMIKH6XA"),"General",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFBB5BWCUZZHEDICOSYNTHZ3ZNE"),null,null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDONV65JHGFHXZKRSXXSEKMLG54"),0,5,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colWDKSRUEFVJHQPGKLPTNSSBSMWI"),0,2,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colZGV3WOV765HFTIQY4ZA6PKRMWI"),0,3,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colBXT3L65R7VDOXNVYQ2OIMRQTHU"),0,4,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJ6GHATEATRAIZOGDWKVGLNLSDQ"),0,6,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colTOVFRCOSNRELJK73KAENVCQPW4"),0,1,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colSGRNYKKFMZHS3JWOVWIAQUPNF4"),0,0,1,false,false)
			},null)
	},
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgKTXX7BBPJNDMTO3GOTJMIKH6XA"))}
	,

	/*Radix::CfgManagement::Revision:Create:Children-Explorer Items*/
		null
	,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
	null,
	0,
	null,
	(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
	2192,0,0,null);
}
/* Radix::CfgManagement::Revision:Create - Web Meta*/

/*Radix::CfgManagement::Revision:Create-Editor Presentation*/

package org.radixware.ads.CfgManagement.web;
public final class Create_mi{
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new org.radixware.kernel.common.client.meta.RadEditorPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprOJBSAEAAPZCHTMSSKK6XCMO2XI"),
	"Create",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
	null,
	org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFBB5BWCUZZHEDICOSYNTHZ3ZNE"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("tblFBB5BWCUZZHEDICOSYNTHZ3ZNE"),
	null,
	null,

	/*Radix::CfgManagement::Revision:Create:Editor Pages-Editor Presentation Pages*/
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

			/*Radix::CfgManagement::Revision:Create:General-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgKTXX7BBPJNDMTO3GOTJMIKH6XA"),"General",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFBB5BWCUZZHEDICOSYNTHZ3ZNE"),null,null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDONV65JHGFHXZKRSXXSEKMLG54"),0,5,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colWDKSRUEFVJHQPGKLPTNSSBSMWI"),0,2,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colZGV3WOV765HFTIQY4ZA6PKRMWI"),0,3,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colBXT3L65R7VDOXNVYQ2OIMRQTHU"),0,4,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJ6GHATEATRAIZOGDWKVGLNLSDQ"),0,6,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colTOVFRCOSNRELJK73KAENVCQPW4"),0,1,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colSGRNYKKFMZHS3JWOVWIAQUPNF4"),0,0,1,false,false)
			},null)
	},
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgKTXX7BBPJNDMTO3GOTJMIKH6XA"))}
	,

	/*Radix::CfgManagement::Revision:Create:Children-Explorer Items*/
		null
	,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
	null,
	0,
	null,
	(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
	2192,0,0,null);
}
/* Radix::CfgManagement::Revision:Create:Model - Desktop Executable*/

/*Radix::CfgManagement::Revision:Create:Model-Entity Model Class*/

package org.radixware.ads.CfgManagement.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::Revision:Create:Model")
public class Create:Model  extends org.radixware.ads.CfgManagement.explorer.Revision.Revision_DefaultModel {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return Create:Model_mi.rdxMeta; }



	public Create:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadEditorPresentationDef def){super(userSession,def);}

	/*Radix::CfgManagement::Revision:Create:Model:Nested classes-Nested Classes*/

	/*Radix::CfgManagement::Revision:Create:Model:Properties-Properties*/

	/*Radix::CfgManagement::Revision:Create:Model:Methods-Methods*/

	/*Radix::CfgManagement::Revision:Create:Model:afterOpenEditorPageView-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::Revision:Create:Model:afterOpenEditorPageView")
	public published  void afterOpenEditorPageView (org.radixware.kernel.common.types.Id pageId) {
		super.afterOpenEditorPageView(pageId);

		seq.Value = ChangelogUtils.getNextRevisionSeq(lastRevisionSeq.Value);
		author.Value = java.lang.System.getProperty("user.name");
		time.Value = new DateTime(new java.util.Date().getTime());
		appVer.Value = getEnvironment().getDefManager().getClassLoader().getRevisionMeta().LayerVersionsString;
	}

	/*Radix::CfgManagement::Revision:Create:Model:afterCreate-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::Revision:Create:Model:afterCreate")
	protected published  void afterCreate () throws org.radixware.kernel.common.client.exceptions.ModelException {
		super.afterCreate();

		final Explorer.Models::EntityModel ownerLog = (Explorer.Models::EntityModel) findOwnerByClass(ChangeLog.class);
		if (ownerLog != null && ownerLog.isExists() && !ownerLog.isEdited()) {
		    try {
		        ownerLog.read();
		    } catch (Exceptions::InterruptedException | Exceptions::ServiceClientException ex) {
		        getEnvironment().getTracer().error(ex);
		    }
		}
	}


}

/* Radix::CfgManagement::Revision:Create:Model - Desktop Meta*/

/*Radix::CfgManagement::Revision:Create:Model-Entity Model Class*/

package org.radixware.ads.CfgManagement.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Create:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aemOJBSAEAAPZCHTMSSKK6XCMO2XI"),
						"Create:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::CfgManagement::Revision:Create:Model:Properties-Properties*/
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

/* Radix::CfgManagement::Revision:Create:Model - Web Executable*/

/*Radix::CfgManagement::Revision:Create:Model-Entity Model Class*/

package org.radixware.ads.CfgManagement.web;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::Revision:Create:Model")
public class Create:Model  extends org.radixware.ads.CfgManagement.web.Revision.Revision_DefaultModel {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return Create:Model_mi.rdxMeta; }



	public Create:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadEditorPresentationDef def){super(userSession,def);}

	/*Radix::CfgManagement::Revision:Create:Model:Nested classes-Nested Classes*/

	/*Radix::CfgManagement::Revision:Create:Model:Properties-Properties*/

	/*Radix::CfgManagement::Revision:Create:Model:Methods-Methods*/

	/*Radix::CfgManagement::Revision:Create:Model:afterOpenEditorPageView-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::Revision:Create:Model:afterOpenEditorPageView")
	public published  void afterOpenEditorPageView (org.radixware.kernel.common.types.Id pageId) {
		super.afterOpenEditorPageView(pageId);

		seq.Value = ChangelogUtils.getNextRevisionSeq(lastRevisionSeq.Value);
		author.Value = java.lang.System.getProperty("user.name");
		time.Value = new DateTime(new java.util.Date().getTime());
		appVer.Value = getEnvironment().getDefManager().getClassLoader().getRevisionMeta().LayerVersionsString;
	}

	/*Radix::CfgManagement::Revision:Create:Model:afterCreate-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::Revision:Create:Model:afterCreate")
	protected published  void afterCreate () throws org.radixware.kernel.common.client.exceptions.ModelException {
		super.afterCreate();

		final Explorer.Models::EntityModel ownerLog = (Explorer.Models::EntityModel) findOwnerByClass(ChangeLog.class);
		if (ownerLog != null && ownerLog.isExists() && !ownerLog.isEdited()) {
		    try {
		        ownerLog.read();
		    } catch (Exceptions::InterruptedException | Exceptions::ServiceClientException ex) {
		        getEnvironment().getTracer().error(ex);
		    }
		}
	}


}

/* Radix::CfgManagement::Revision:Create:Model - Web Meta*/

/*Radix::CfgManagement::Revision:Create:Model-Entity Model Class*/

package org.radixware.ads.CfgManagement.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Create:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aemOJBSAEAAPZCHTMSSKK6XCMO2XI"),
						"Create:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::CfgManagement::Revision:Create:Model:Properties-Properties*/
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

/* Radix::CfgManagement::Revision:General - Desktop Meta*/

/*Radix::CfgManagement::Revision:General-Selector Presentation*/

package org.radixware.ads.CfgManagement.explorer;
public final class General_mi extends org.radixware.kernel.common.client.meta.RadSelectorPresentationDef{
	public static final org.radixware.kernel.common.client.meta.RadSelectorPresentationDef rdxMeta = new General_mi();
	private General_mi(){
		super(org.radixware.kernel.common.types.Id.Factory.loadFrom("spr7KE3MVJCXBEMVBY2IOQ27CH3FU"),
		"General",
		org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
		null,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFBB5BWCUZZHEDICOSYNTHZ3ZNE"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("tblFBB5BWCUZZHEDICOSYNTHZ3ZNE"),
		null,
		null,
		null,
		null,
		false,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("srtQOB6Q3KIRFALTLIEHAGUFNWEOM"),
		new org.radixware.kernel.common.types.Id[]{},
		false,
		false,
		null,
		32,
		null,
		144,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprOJBSAEAAPZCHTMSSKK6XCMO2XI")},
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprA3W5TMJ2P5ELTOQMDLGZ6VXS7Y")},
		false,true,false,0,0);
		columns = new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn[]{

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colSGRNYKKFMZHS3JWOVWIAQUPNF4"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colTOVFRCOSNRELJK73KAENVCQPW4"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colWDKSRUEFVJHQPGKLPTNSSBSMWI"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colBXT3L65R7VDOXNVYQ2OIMRQTHU"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colZGV3WOV765HFTIQY4ZA6PKRMWI"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null)
		};
	}
	@Override
	protected org.radixware.kernel.common.client.models.Model createModelImpl(org.radixware.kernel.common.client.IClientEnvironment userSession) {    return new org.radixware.ads.CfgManagement.explorer.Revision.DefaultGroupModel(userSession,this);
	}
}
/* Radix::CfgManagement::Revision:General - Web Meta*/

/*Radix::CfgManagement::Revision:General-Selector Presentation*/

package org.radixware.ads.CfgManagement.web;
public final class General_mi extends org.radixware.kernel.common.client.meta.RadSelectorPresentationDef{
	public static final org.radixware.kernel.common.client.meta.RadSelectorPresentationDef rdxMeta = new General_mi();
	private General_mi(){
		super(org.radixware.kernel.common.types.Id.Factory.loadFrom("spr7KE3MVJCXBEMVBY2IOQ27CH3FU"),
		"General",
		org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
		null,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFBB5BWCUZZHEDICOSYNTHZ3ZNE"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("tblFBB5BWCUZZHEDICOSYNTHZ3ZNE"),
		null,
		null,
		null,
		null,
		false,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("srtQOB6Q3KIRFALTLIEHAGUFNWEOM"),
		new org.radixware.kernel.common.types.Id[]{},
		false,
		false,
		null,
		32,
		null,
		144,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprOJBSAEAAPZCHTMSSKK6XCMO2XI")},
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprA3W5TMJ2P5ELTOQMDLGZ6VXS7Y")},
		false,true,false,0,0);
		columns = new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn[]{

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colSGRNYKKFMZHS3JWOVWIAQUPNF4"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colTOVFRCOSNRELJK73KAENVCQPW4"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colWDKSRUEFVJHQPGKLPTNSSBSMWI"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colBXT3L65R7VDOXNVYQ2OIMRQTHU"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colZGV3WOV765HFTIQY4ZA6PKRMWI"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null)
		};
	}
	@Override
	protected org.radixware.kernel.common.client.models.Model createModelImpl(org.radixware.kernel.common.client.IClientEnvironment userSession) {    return new org.radixware.ads.CfgManagement.web.Revision.DefaultGroupModel(userSession,this);
	}
}
/* Radix::CfgManagement::Revision - Localizing Bundle */
package org.radixware.ads.CfgManagement.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Revision - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Created by");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Автор");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsC3LMVWRH3FBOJKUU7BP5LLAZWQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"General");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Общее");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsC667KCX46RE37IS4KJ73ADZK3E"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Local notes");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Локальные заметки");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFX7NCVW5UBGQXLHCEFO4H5H5EE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Reference document");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Документ-основание");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJLLA4AISSREJ5IY5EXBIEY6CMI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Revision number");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Номер ревизии");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJMQA2YF37RHRBLPP3R3THI6KQM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Description");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Описание");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLQTSV4EHT5CKLP5CM4WY5C6MHM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Revision");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ревизия");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsM4JAOZQLUBEHZEE2XKARMPKUII"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Created");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Дата");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNHFPT6LV55BL5BIVP3KOZS5XNU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Application version");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Версия приложения");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsO7WLJD5VSJHWDMTU3YUD2LU4OY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Revisions");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ревизии");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYGAKF6PASNCQ5K6R24IUH7CRPQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"null"));
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(Revision - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbaecFBB5BWCUZZHEDICOSYNTHZ3ZNE"),"Revision - Localizing Bundle",$$$items$$$);
}
