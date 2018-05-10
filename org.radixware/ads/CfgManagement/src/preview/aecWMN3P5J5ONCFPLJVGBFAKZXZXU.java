
/* Radix::CfgManagement::CfgPacket - Server Executable*/

/*Radix::CfgManagement::CfgPacket-Entity Class*/

package org.radixware.ads.CfgManagement.server;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgPacket")
public abstract published class CfgPacket  extends org.radixware.ads.Types.server.Entity  implements org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {


	/**Metainformation accessor method*/
	@SuppressWarnings("unused")
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return CfgPacket_mi.rdxMeta;}

	/*Radix::CfgManagement::CfgPacket:Nested classes-Nested Classes*/

	/*Radix::CfgManagement::CfgPacket:Properties-Properties*/

	/*Radix::CfgManagement::CfgPacket:acceptTime-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgPacket:acceptTime")
	public published  java.sql.Timestamp getAcceptTime() {
		return acceptTime;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgPacket:acceptTime")
	public published   void setAcceptTime(java.sql.Timestamp val) {
		acceptTime = val;
	}

	/*Radix::CfgManagement::CfgPacket:acceptUser-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgPacket:acceptUser")
	public published  Str getAcceptUser() {
		return acceptUser;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgPacket:acceptUser")
	public published   void setAcceptUser(Str val) {
		acceptUser = val;
	}

	/*Radix::CfgManagement::CfgPacket:classGuid-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgPacket:classGuid")
	public published  Str getClassGuid() {
		return classGuid;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgPacket:classGuid")
	public published   void setClassGuid(Str val) {
		classGuid = val;
	}

	/*Radix::CfgManagement::CfgPacket:id-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgPacket:id")
	public published  Int getId() {
		return id;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgPacket:id")
	public published   void setId(Int val) {
		id = val;
	}

	/*Radix::CfgManagement::CfgPacket:lastModifyTime-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgPacket:lastModifyTime")
	public published  java.sql.Timestamp getLastModifyTime() {
		return lastModifyTime;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgPacket:lastModifyTime")
	public published   void setLastModifyTime(java.sql.Timestamp val) {
		lastModifyTime = val;
	}

	/*Radix::CfgManagement::CfgPacket:lastModifyUser-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgPacket:lastModifyUser")
	public published  Str getLastModifyUser() {
		return lastModifyUser;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgPacket:lastModifyUser")
	public published   void setLastModifyUser(Str val) {
		lastModifyUser = val;
	}

	/*Radix::CfgManagement::CfgPacket:notes-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgPacket:notes")
	public published  Str getNotes() {
		return notes;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgPacket:notes")
	public published   void setNotes(Str val) {
		notes = val;
	}

	/*Radix::CfgManagement::CfgPacket:srcAppVer-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgPacket:srcAppVer")
	public published  Str getSrcAppVer() {
		return srcAppVer;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgPacket:srcAppVer")
	public published   void setSrcAppVer(Str val) {
		srcAppVer = val;
	}

	/*Radix::CfgManagement::CfgPacket:srcDbUrl-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgPacket:srcDbUrl")
	public published  Str getSrcDbUrl() {
		return srcDbUrl;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgPacket:srcDbUrl")
	public published   void setSrcDbUrl(Str val) {
		srcDbUrl = val;
	}

	/*Radix::CfgManagement::CfgPacket:srcExpTime-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgPacket:srcExpTime")
	public published  java.sql.Timestamp getSrcExpTime() {
		return srcExpTime;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgPacket:srcExpTime")
	public published   void setSrcExpTime(java.sql.Timestamp val) {
		srcExpTime = val;
	}

	/*Radix::CfgManagement::CfgPacket:srcExpUser-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgPacket:srcExpUser")
	public published  Str getSrcExpUser() {
		return srcExpUser;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgPacket:srcExpUser")
	public published   void setSrcExpUser(Str val) {
		srcExpUser = val;
	}

	/*Radix::CfgManagement::CfgPacket:srcPacketId-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgPacket:srcPacketId")
	public published  Int getSrcPacketId() {
		return srcPacketId;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgPacket:srcPacketId")
	public published   void setSrcPacketId(Int val) {
		srcPacketId = val;
	}

	/*Radix::CfgManagement::CfgPacket:title-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgPacket:title")
	public published  Str getTitle() {
		return title;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgPacket:title")
	public published   void setTitle(Str val) {
		title = val;
	}

	/*Radix::CfgManagement::CfgPacket:thisVersions-Dynamic Property*/



	protected static Str thisVersions=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgPacket:thisVersions")
	protected static  Str getThisVersions() {

		return Arte::Arte.getAllLayerVersionsAsString();
	}

	/*Radix::CfgManagement::CfgPacket:classTitle-Dynamic Property*/



	protected Str classTitle=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgPacket:classTitle")
	protected  Str getClassTitle() {

		return getClassDefinitionTitle();

	}

	/*Radix::CfgManagement::CfgPacket:pkgState-Column-Based Property*/








			















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgPacket:pkgState")
	public published  org.radixware.ads.CfgManagement.common.CfgPacketState getPkgState() {
		return pkgState;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgPacket:pkgState")
	public published   void setPkgState(org.radixware.ads.CfgManagement.common.CfgPacketState val) {
		pkgState = val;
	}

	/*Radix::CfgManagement::CfgPacket:stateChangelog-User Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgPacket:stateChangelog")
	  org.radixware.ads.CfgManagement.server.ChangeLog getStateChangelog() {
		return stateChangelog;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgPacket:stateChangelog")
	   void setStateChangelog(org.radixware.ads.CfgManagement.server.ChangeLog val) {
		stateChangelog = val;
	}



























































































































	/*Radix::CfgManagement::CfgPacket:Methods-Methods*/

	/*Radix::CfgManagement::CfgPacket:afterCreate-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgPacket:afterCreate")
	protected published  void afterCreate (org.radixware.kernel.server.types.Entity src) {
		super.afterCreate(src);

		if (src != null)
		    copyItems(((CfgPacket) src).id, id);    

		Arte::Trace.enterContext(Arte::EventContextType:CfgPackage, id);
		Arte::Trace.put(eventCode["Configuration package created"]);
		Arte::Trace.leaveContext(Arte::EventContextType:CfgPackage, id);

	}

	/*Radix::CfgManagement::CfgPacket:afterInit-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgPacket:afterInit")
	protected published  void afterInit (org.radixware.kernel.server.types.Entity src, org.radixware.kernel.common.enums.EEntityInitializationPhase phase) {
		super.afterInit(src, phase);

		onUpdate();
	}

	/*Radix::CfgManagement::CfgPacket:beforeUpdate-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgPacket:beforeUpdate")
	protected published  boolean beforeUpdate () {
		onUpdate();

		return super.beforeUpdate();
	}

	/*Radix::CfgManagement::CfgPacket:onUpdate-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgPacket:onUpdate")
	  void onUpdate () {
		lastModifyTime = Utils::Timing.getCurrentTime();
		lastModifyUser = Arte::Arte.getUserName();
	}

	/*Radix::CfgManagement::CfgPacket:loadByPK-System Method*/

	@SuppressWarnings("unused")
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgPacket:loadByPK")
	public static published  org.radixware.ads.CfgManagement.server.CfgPacket loadByPK (Int id, boolean checkExistance) {
	final java.util.HashMap<org.radixware.kernel.common.types.Id,Object> pkValsMap = new java.util.HashMap<org.radixware.kernel.common.types.Id,Object>(5);
			if(id==null) return null;
			pkValsMap.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDZOPCTNIURBCBPLCP75JMZCFYM"),id);
	org.radixware.kernel.server.types.Pid pid = new org.radixware.kernel.server.types.Pid(org.radixware.ads.mdlPEKYFVDRVZHGZCBQQDY2NOYFOY.server.pdcArte______________________.__getArteInstanceInternal(),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblWMN3P5J5ONCFPLJVGBFAKZXZXU"),pkValsMap);
		try{
		return (
		org.radixware.ads.CfgManagement.server.CfgPacket) org.radixware.ads.mdlPEKYFVDRVZHGZCBQQDY2NOYFOY.server.pdcArte______________________.__getArteInstanceInternal().getEntityObject(pid,null,checkExistance);
	}catch(org.radixware.kernel.server.exceptions.EntityObjectNotExistsError e){
	return null;
	}

	}

	/*Radix::CfgManagement::CfgPacket:checkPackageVersions-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgPacket:checkPackageVersions")
	 static  boolean checkPackageVersions (org.radixware.ads.CfgManagement.common.ImpExpXsd.PacketDocument xPktDoc) {
		java.util.ArrayList<ImpExpXsd:Item> itemsWithNotCurrentVersion = new java.util.ArrayList<>();
		if (!checkVersions(xPktDoc, itemsWithNotCurrentVersion)) {
		    Utils::Html htmlMessage = buildCheckVersionsHtml(xPktDoc.Packet.AppVer,
		            !CfgCommonUtils.compareLayersVersions(thisVersions, xPktDoc.Packet.AppVer),
		            itemsWithNotCurrentVersion);
		    try {
		        if (Client.Resources::MessageDialogResource.show(
		                Arte::Arte.getInstance(),
		                Client.Resources::DialogType:Warning, null,
		                java.util.Arrays.asList(new Client.Resources::DialogButtonType[]{
		                    Client.Resources::DialogButtonType:Ok,
		                    Client.Resources::DialogButtonType:Cancel}),
		                htmlMessage.toString()
		        ) == Client.Resources::DialogButtonType:Cancel)
		            return false;
		    } catch (Exception e) {
		        return false;
		    }
		}
		return true;
	}

	/*Radix::CfgManagement::CfgPacket:checkVersions-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgPacket:checkVersions")
	private static  boolean checkVersions (org.radixware.ads.CfgManagement.common.ImpExpXsd.PacketDocument xPktDoc, java.util.List<org.radixware.ads.CfgManagement.common.ImpExpXsd.Item> itemsWithNotCurrentVersion) {
		boolean checkResult = true;
		//1. Check packet version
		if (!CfgCommonUtils.compareLayersVersions(thisVersions, xPktDoc.Packet.AppVer)) {
		    checkResult = false;
		}
		//2. Check items versions
		if (xPktDoc.Packet.ItemList != null) {
		    checkItemsVersions(xPktDoc.Packet.ItemList, itemsWithNotCurrentVersion);
		}
		checkResult &= itemsWithNotCurrentVersion.isEmpty();
		return checkResult;
	}

	/*Radix::CfgManagement::CfgPacket:checkItemsVersions-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgPacket:checkItemsVersions")
	private static  void checkItemsVersions (java.util.List<org.radixware.ads.CfgManagement.common.ImpExpXsd.Item> items2Check, java.util.List<org.radixware.ads.CfgManagement.common.ImpExpXsd.Item> itemsRes) {
		for (ImpExpXsd:Item xItem : items2Check) {
		    if (xItem.isSetAppVer() && !CfgCommonUtils.compareLayersVersions(thisVersions, xItem.AppVer)) {
		        itemsRes.add(xItem);
		    }
		    if (xItem.isSetChildren()) {
		        checkItemsVersions(xItem.Children.ItemList, itemsRes);
		    }
		}
	}

	/*Radix::CfgManagement::CfgPacket:buildCheckVersionsHtml-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgPacket:buildCheckVersionsHtml")
	private static  org.radixware.kernel.common.html.Html buildCheckVersionsHtml (Str pktVersion, boolean printPktVersion, java.util.List<org.radixware.ads.CfgManagement.common.ImpExpXsd.Item> itemsWithNotCurrentVersion) {
		final Utils::Html htmlMessage = new Html("html");

		Utils::Html title = new Html("h4");
		title.setInnerText("There is version mismatches! Do you wish to continue?");
		htmlMessage.add(title);

		final Utils::HtmlTable layerTable = CfgCommonUtils.createHtmlTable();
		if (printPktVersion) {
		    Utils::HtmlTable.Row r = layerTable.addRow();
		    r.addCell().setInnerText("Source system version:");
		    r.addCell().setInnerText(pktVersion);
		}
		Utils::HtmlTable.Row r = layerTable.addRow();
		r.addCell().setInnerText("This system version:");
		r.addCell().setInnerText(thisVersions);
		htmlMessage.add(layerTable);

		if (!itemsWithNotCurrentVersion.isEmpty()) {
		    Utils::Html itemsTitle = new Html("h4");
		    itemsTitle.setInnerText("Items with version conflict:");
		    htmlMessage.add(itemsTitle);

		    final Utils::HtmlTable itemsTable = CfgCommonUtils.createHtmlTable();
		    Utils::HtmlTable.Row hr = itemsTable.addRow();
		    hr.addCell().setInnerText("Item");
		    hr.addCell().setInnerText("Version");
		    hr.setCss("font-weight", 600);
		    for (ImpExpXsd:Item xItem : itemsWithNotCurrentVersion) {
		        r = itemsTable.addRow();
		        r.addCell().setInnerText(xItem.Id + ") '" + xItem.ObjectTitle + "'");
		        r.addCell().setInnerText(xItem.AppVer);
		    }
		    htmlMessage.add(itemsTable);
		}
		return htmlMessage;
	}

	/*Radix::CfgManagement::CfgPacket:copyItems-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgPacket:copyItems")
	  void copyItems (Int fromPacketId, Int toPacketId) {
		//.(fromPacketId, toPacketId);
		//We should not use  statement because we need copy user properties.

		try (CfgItemsCursor c = CfgItemsCursor.open(fromPacketId, null, null)) {
		    java.util.Map<Int,Int> itemId2CopyItemId = new java.util.HashMap<>();
		    while (c.next()) {
		        final CfgItem item = c.item;
		        final CfgItem copyItem = (CfgItem) Arte::Arte.newObject(Types::Id.Factory.loadFrom(item.classGuid));
		        copyItem.init(null, item);
		        itemId2CopyItemId.put(item.id, copyItem.id);
		        copyItem.packetId = toPacketId;
		        if (item.parentId != null) {
		            copyItem.parentId = itemId2CopyItemId.get(item.parentId);
		        }
		        copyItem.create(c.item);
		    }
		}
	}


	@Override
	public org.radixware.kernel.server.types.FormHandler.NextDialogsRequest execCommand(org.radixware.kernel.common.types.Id cmdId, org.radixware.kernel.common.types.Id propId, org.apache.xmlbeans.XmlObject input, org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValsById, org.apache.xmlbeans.XmlObject output) throws org.radixware.kernel.common.exceptions.AppException,java.lang.InterruptedException{

			return super.execCommand(cmdId,propId,input,newPropValsById,output);
	}


}

/* Radix::CfgManagement::CfgPacket - Server Meta*/

/*Radix::CfgManagement::CfgPacket-Entity Class*/

package org.radixware.ads.CfgManagement.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class CfgPacket_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecWMN3P5J5ONCFPLJVGBFAKZXZXU"),"CfgPacket",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2G45JJWSARAG3NTBN3RFRLGW6M"),

						org.radixware.kernel.common.enums.EClassType.ENTITY,

						/*Radix::CfgManagement::CfgPacket:Presentations-Entity Presentations*/
						/*Class presentation attributes*/
						 new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef(/*Owner Class Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("aecWMN3P5J5ONCFPLJVGBFAKZXZXU"),
							/*Owner Class Name*/
							"CfgPacket",
							/*Title Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2G45JJWSARAG3NTBN3RFRLGW6M"),
							/*Property presentations*/

							/*Radix::CfgManagement::CfgPacket:Properties-Properties*/
							new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef[]{

									/*Radix::CfgManagement::CfgPacket:acceptTime:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEF5WPJPV7JBATHO6TXOMAGWJEI"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::CfgManagement::CfgPacket:acceptUser:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colLMJ5EPIAOVAPHJFUNG2BTNME6A"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::CfgManagement::CfgPacket:id:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDZOPCTNIURBCBPLCP75JMZCFYM"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::CfgManagement::CfgPacket:lastModifyTime:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMCRBKZP4GNADJJLP2J7ITEGELY"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::CfgManagement::CfgPacket:lastModifyUser:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col4ESP7TDW4JGG7FZ4PMVZ4IUO74"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::CfgManagement::CfgPacket:notes:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colCQOZYZGSGZBLNNG6KX255SLN5Q"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::CfgManagement::CfgPacket:srcAppVer:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNLZ5BGYZ7BGVRCQYC6WJWZIBM4"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::CfgManagement::CfgPacket:srcDbUrl:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colAJ6PVNFK3FBIVPACM2HG6NHSMU"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::CfgManagement::CfgPacket:srcExpTime:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colLQHEBRDALNHZHNCQB76A3BEP6U"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::CfgManagement::CfgPacket:srcExpUser:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col6GQBFOYQYFCABDAMANXVXFGU3Q"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::CfgManagement::CfgPacket:srcPacketId:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colCY7ILVPBW5FCRBUUHJPC7LRBJY"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::CfgManagement::CfgPacket:title:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colT6AXXF6DBVAQDFOGFG4QARZYJU"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::CfgManagement::CfgPacket:classTitle:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdT42SQ7RCQ5CYRIIBFO7TVMVA4A"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::CfgManagement::CfgPacket:pkgState:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDKBRCEEYTFDBBAQ3ZE5GOMIXR4"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::CfgManagement::CfgPacket:stateChangelog:PropertyPresentation-Object Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadParentTitlePropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruIV5OQZASNNB4XGWKPXUGHOHAAQ"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,null,null,null,null,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprVRZPY6RJKBHWFLK46ATZLXFW3U")},null,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprVRZPY6RJKBHWFLK46ATZLXFW3U")},java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.TITLE,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT))
							},
							/*Commands*/
							null,
							/*Sortings*/
							new org.radixware.kernel.server.meta.presentations.RadSortingDef[]{

									new org.radixware.kernel.server.meta.presentations.RadSortingDef(
									/*Radix::CfgManagement::CfgPacket:Id-Sorting*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("srtUNCGLTJZI5B55BYSKIWTLX25ZE"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecWMN3P5J5ONCFPLJVGBFAKZXZXU"),"Id",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2SQGUBEYBFDHBCYMYFYCEBI2XE"),new org.radixware.kernel.server.meta.presentations.RadSortingDef.Item[]{

											new org.radixware.kernel.server.meta.presentations.RadSortingDef.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDZOPCTNIURBCBPLCP75JMZCFYM"),org.radixware.kernel.common.enums.EOrder.ASC)
									},"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware")
							},
							/*Filters*/
							null,
							/*Editor presentations*/
							new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef[]{

									new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef(
									/*Radix::CfgManagement::CfgPacket:General-Editor Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprJHOQCOZIOBBWNABEIAT5XEG7M4"),
									"General",
									org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
									null,
									2192,
									null,

									/*Radix::CfgManagement::CfgPacket:General:Children-Explorer Items*/
										new org.radixware.kernel.server.meta.presentations.RadExplorerItemDef[]{

												/*Radix::CfgManagement::CfgPacket:General:EventLog-Entity Explorer Item*/

												new org.radixware.kernel.server.meta.presentations.RadTableExplorerItemDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiC3WF6WEXGZDUTAKXY4G3E4LNUE"),
													org.radixware.kernel.common.types.Id.Factory.loadFrom("aecWMN3P5J5ONCFPLJVGBFAKZXZXU"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
													org.radixware.kernel.common.types.Id.Factory.loadFrom("sprVJGCRERZOBDS7GULG6RLDVLG3A"),
													new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),
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
									/*Radix::CfgManagement::CfgPacket:General-Selector Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("sprJGWXV6TQQRD3PIY4DT4JJU63ZQ"),"General",null,144,new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn[]{

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDZOPCTNIURBCBPLCP75JMZCFYM"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdT42SQ7RCQ5CYRIIBFO7TVMVA4A"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colT6AXXF6DBVAQDFOGFG4QARZYJU"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDKBRCEEYTFDBBAQ3ZE5GOMIXR4"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMCRBKZP4GNADJJLP2J7ITEGELY"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col4ESP7TDW4JGG7FZ4PMVZ4IUO74"),true)
									},new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.Addons(org.radixware.kernel.common.types.Id.Factory.loadFrom("srtUNCGLTJZI5B55BYSKIWTLX25ZE"),true,null,true,null,true,null,false,true,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprJHOQCOZIOBBWNABEIAT5XEG7M4")},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprJHOQCOZIOBBWNABEIAT5XEG7M4")},org.radixware.kernel.server.types.Restrictions.Factory.newInstance(32,null,null,null),org.radixware.kernel.common.types.Id.Factory.loadFrom("eccMTCC7TKG2ZFXHHV6DYJ2SE3M34"))
							},
							/*Default selector presentation Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("sprJGWXV6TQQRD3PIY4DT4JJU63ZQ"),
							/*Presentation Map*/
							null,
							/*Object title format*/

							/*Radix::CfgManagement::CfgPacket:TitleFormat-Object Title Format*/

							new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecWMN3P5J5ONCFPLJVGBFAKZXZXU"),new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.TitleItem[]{

									new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.TitleItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecWMN3P5J5ONCFPLJVGBFAKZXZXU"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colDZOPCTNIURBCBPLCP75JMZCFYM"),"{0}) ",org.radixware.kernel.common.enums.ETitleNullFormat.SAME_AS_NOT_NULL,null),

									new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.TitleItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecWMN3P5J5ONCFPLJVGBFAKZXZXU"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colT6AXXF6DBVAQDFOGFG4QARZYJU"),"{0}",org.radixware.kernel.common.enums.ETitleNullFormat.SAME_AS_NOT_NULL,null)
							},null,org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.DefinitionContextType.ENTITY),
							/*Class catalogs*/
							new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog[]{

									new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog(
									/*Radix::CfgManagement::CfgPacket:General-Dynamic Class Catalog*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eccMTCC7TKG2ZFXHHV6DYJ2SE3M34"),org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.ELinkMode.VIRTUAL,new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.Item[]{
									}
									)
							},
							/*Restrictions*/
							org.radixware.kernel.server.types.Restrictions.Factory.newInstance(0,null,null,null),null,0),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblWMN3P5J5ONCFPLJVGBFAKZXZXU"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("pdcEntity____________________"),

						/*Radix::CfgManagement::CfgPacket:Properties-Properties*/
						new org.radixware.kernel.server.meta.clazzes.RadPropDef[]{

								/*Radix::CfgManagement::CfgPacket:acceptTime-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEF5WPJPV7JBATHO6TXOMAGWJEI"),"acceptTime",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPTPVRXE6QVFTDPNYWJFZY6KYFQ"),org.radixware.kernel.common.enums.EValType.DATE_TIME,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::CfgManagement::CfgPacket:acceptUser-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colLMJ5EPIAOVAPHJFUNG2BTNME6A"),"acceptUser",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFGCRFIF6XJHIXBU5FLBNLRMSMQ"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::CfgManagement::CfgPacket:classGuid-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKE4YQ3Y2LNEDBGUI6I3B4TBM7E"),"classGuid",null,org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::CfgManagement::CfgPacket:id-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDZOPCTNIURBCBPLCP75JMZCFYM"),"id",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsM4FRSKZIOJB4PLDZBYPZV5B4GM"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::CfgManagement::CfgPacket:lastModifyTime-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMCRBKZP4GNADJJLP2J7ITEGELY"),"lastModifyTime",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMHR2DKL4IRBOFG3ZIVCLFFQV4Q"),org.radixware.kernel.common.enums.EValType.DATE_TIME,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::CfgManagement::CfgPacket:lastModifyUser-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col4ESP7TDW4JGG7FZ4PMVZ4IUO74"),"lastModifyUser",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHOINA7GGDBEENOKBFSI6KO63NU"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::CfgManagement::CfgPacket:notes-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colCQOZYZGSGZBLNNG6KX255SLN5Q"),"notes",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKAGGDK7R3BF3VI3Y3DWLKZV74Y"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::CfgManagement::CfgPacket:srcAppVer-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNLZ5BGYZ7BGVRCQYC6WJWZIBM4"),"srcAppVer",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7NMAVEKT2ZF6FF4JXZO2TITPQQ"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::CfgManagement::CfgPacket:srcDbUrl-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colAJ6PVNFK3FBIVPACM2HG6NHSMU"),"srcDbUrl",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRYHKTWXKQ5CT5KD23NAI3G7GVY"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::CfgManagement::CfgPacket:srcExpTime-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colLQHEBRDALNHZHNCQB76A3BEP6U"),"srcExpTime",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQWMETCETYJHZDFGJT4GKYPQANY"),org.radixware.kernel.common.enums.EValType.DATE_TIME,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::CfgManagement::CfgPacket:srcExpUser-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col6GQBFOYQYFCABDAMANXVXFGU3Q"),"srcExpUser",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsSANFELSPIBGFLHPKNUIEW35C4A"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::CfgManagement::CfgPacket:srcPacketId-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colCY7ILVPBW5FCRBUUHJPC7LRBJY"),"srcPacketId",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNAZLRVZDGBB2XDS4V7X5F5CSKI"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::CfgManagement::CfgPacket:title-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colT6AXXF6DBVAQDFOGFG4QARZYJU"),"title",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJQQWOTISZRCQPDANF3S54KPD2I"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::CfgManagement::CfgPacket:thisVersions-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd4BAXENWMK5FTLOSFBUY5DFH2NM"),"thisVersions",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZIY6QFAWIVEPBK6FPIB6KFKRQU"),org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadAccessorProxy.R_ACCESSOR_INSTANCE),

								/*Radix::CfgManagement::CfgPacket:classTitle-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdT42SQ7RCQ5CYRIIBFO7TVMVA4A"),"classTitle",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsREB4DYXOSZAAPERWFHXFSTFL2A"),org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadAccessorProxy.R_ACCESSOR_INSTANCE),

								/*Radix::CfgManagement::CfgPacket:pkgState-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDKBRCEEYTFDBBAQ3ZE5GOMIXR4"),"pkgState",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZQETKL6GB5HEBHRTGQSJY5U5FE"),org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("acsKBHFAZGGC5AYTFAQNXXU4URWCE"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::CfgManagement::CfgPacket:stateChangelog-User Property*/

								new org.radixware.kernel.server.meta.clazzes.RadUserRefPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruIV5OQZASNNB4XGWKPXUGHOHAAQ"),"stateChangelog",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEYCDXBU5ZNEFNA7B6DEFBU42JI"),org.radixware.kernel.common.enums.EValType.OBJECT,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("tblWMN3P5J5ONCFPLJVGBFAKZXZXU"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecVHY5COSHWNA7PPJ65FW3OGJ4ZQ"),false,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE)
						},

						/*Radix::CfgManagement::CfgPacket:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth6KS7LCP4W5AW5OCCJHH5KGO4YQ"),"afterCreate",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("src",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprNAMXQQDLRJEYJNRQE6MOG363M4"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthD77F2HBL2FGSVPJHR3SEAMPWMA"),"afterInit",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("src",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprYPX7VJLY7ZHH7O2EKZIOJ6LNUU")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("phase",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprRFGIIL7ETVFRLGKZMFLZZ67III"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthPT5PMP2WVZGUDB7OTLOE2SBWKE"),"beforeUpdate",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthQ7CATQH4YFELHHLR7F5B5Q2YBA"),"onUpdate",false,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth_loadByPK_________________"),"loadByPK",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("id",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprDZOPCTNIURBCBPLCP75JMZCFYM")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("checkExistance",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprCheckExisstance___________"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthCBLCQUZHWBC7NJDTGCK2BHRJ7Y"),"checkPackageVersions",true,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("xPktDoc",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprMYD2B7SIWRFSBEX7S4AKDYV32M"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth6RGNLHODFJHPPMTDXXGHKEPFCA"),"checkVersions",true,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("xPktDoc",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprYOETOGN6ONGNPNFFVDDJWF7FRI")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("itemsWithNotCurrentVersion",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprQGPPOAYKTBDU3KH4JVV2BMLKPM"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthSN7NVP2MBVAXHFVF7LEXKBHAJ4"),"checkItemsVersions",true,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("items2Check",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprUVAQMUSGJNHQLCHBLVROZQQSKI")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("itemsRes",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprKFGM2WVJAZDXFBZ72PST3BPQQE"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth5O3LSTPUAVFWTE6JPBSKF76UQI"),"buildCheckVersionsHtml",true,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("pktVersion",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprEAAA6QDRDZCTHPAZKY3AHZWN4E")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("printPktVersion",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprDNRQ5ZQDARHKNOWFXGXXPQY6YM")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("itemsWithNotCurrentVersion",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprINXT67DDCZFC7CZAP43H2THO7M"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth4Z7GMKZDUVDHTKEWRVB4BU54HA"),"copyItems",false,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("fromPacketId",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprMIFTRCKAK5EIXP4J6PWP47HOK4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("toPacketId",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr7W55RC6T6VCQXP4DGBE4J3DL6I"))
								},null)
						},
						null,
						org.radixware.kernel.common.enums.EAccessAreaType.NONE,null,null,false);
}

/* Radix::CfgManagement::CfgPacket - Desktop Executable*/

/*Radix::CfgManagement::CfgPacket-Entity Class*/

package org.radixware.ads.CfgManagement.explorer;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgPacket")
public interface CfgPacket {
	/*Group model adapter class. This is the base class for 
	all of selector presentation models of this entity*/
	public static class DefaultGroupModel extends org.radixware.kernel.common.client.models.GroupModel{
		public DefaultGroupModel(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadSelectorPresentationDef def){super(userSession,def);}





		public org.radixware.ads.CfgManagement.explorer.CfgPacket.CfgPacket_DefaultModel getEntity(int i) throws InterruptedException, org.radixware.kernel.common.client.exceptions.BrokenEntityObjectException, org.radixware.kernel.common.exceptions.ServiceClientException { return (org.radixware.ads.CfgManagement.explorer.CfgPacket.CfgPacket_DefaultModel )  super.getEntity(i);}
	}

































































































	/*Radix::CfgManagement::CfgPacket:lastModifyUser:lastModifyUser-Presentation Property*/


	public class LastModifyUser extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public LastModifyUser(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgPacket:lastModifyUser:lastModifyUser")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgPacket:lastModifyUser:lastModifyUser")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public LastModifyUser getLastModifyUser();
	/*Radix::CfgManagement::CfgPacket:srcExpUser:srcExpUser-Presentation Property*/


	public class SrcExpUser extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public SrcExpUser(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgPacket:srcExpUser:srcExpUser")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgPacket:srcExpUser:srcExpUser")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public SrcExpUser getSrcExpUser();
	/*Radix::CfgManagement::CfgPacket:srcDbUrl:srcDbUrl-Presentation Property*/


	public class SrcDbUrl extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public SrcDbUrl(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgPacket:srcDbUrl:srcDbUrl")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgPacket:srcDbUrl:srcDbUrl")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public SrcDbUrl getSrcDbUrl();
	/*Radix::CfgManagement::CfgPacket:notes:notes-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgPacket:notes:notes")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgPacket:notes:notes")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Notes getNotes();
	/*Radix::CfgManagement::CfgPacket:srcPacketId:srcPacketId-Presentation Property*/


	public class SrcPacketId extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public SrcPacketId(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgPacket:srcPacketId:srcPacketId")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgPacket:srcPacketId:srcPacketId")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public SrcPacketId getSrcPacketId();
	/*Radix::CfgManagement::CfgPacket:pkgState:pkgState-Presentation Property*/


	public class PkgState extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public PkgState(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public void setServerValue(org.radixware.kernel.common.client.models.items.properties.PropertyValue val){
			Object x = val.getValue();
			org.radixware.ads.CfgManagement.common.CfgPacketState dummy = x == null ? null : (x instanceof org.radixware.ads.CfgManagement.common.CfgPacketState ? (org.radixware.ads.CfgManagement.common.CfgPacketState)x : org.radixware.ads.CfgManagement.common.CfgPacketState.getForValue((Str)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.STR)));
			val.refineValue(dummy);
			super.setServerValue(val);
		}
		@Override
		public Class<org.radixware.ads.CfgManagement.common.CfgPacketState> getValClass(){
			return org.radixware.ads.CfgManagement.common.CfgPacketState.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.ads.CfgManagement.common.CfgPacketState dummy = x == null ? null : (x instanceof org.radixware.ads.CfgManagement.common.CfgPacketState ? (org.radixware.ads.CfgManagement.common.CfgPacketState)x : org.radixware.ads.CfgManagement.common.CfgPacketState.getForValue((Str)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.STR)));
			setValue(dummy);
		}





				














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgPacket:pkgState:pkgState")
		public  org.radixware.ads.CfgManagement.common.CfgPacketState getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgPacket:pkgState:pkgState")
		public   void setValue(org.radixware.ads.CfgManagement.common.CfgPacketState val) {
			Value = val;
		}
	}
	public PkgState getPkgState();
	/*Radix::CfgManagement::CfgPacket:id:id-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgPacket:id:id")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgPacket:id:id")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public Id getId();
	/*Radix::CfgManagement::CfgPacket:acceptTime:acceptTime-Presentation Property*/


	public class AcceptTime extends org.radixware.kernel.common.client.models.items.properties.PropertyDateTime{
		public AcceptTime(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgPacket:acceptTime:acceptTime")
		public  java.sql.Timestamp getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgPacket:acceptTime:acceptTime")
		public   void setValue(java.sql.Timestamp val) {
			Value = val;
		}
	}
	public AcceptTime getAcceptTime();
	/*Radix::CfgManagement::CfgPacket:acceptUser:acceptUser-Presentation Property*/


	public class AcceptUser extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public AcceptUser(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgPacket:acceptUser:acceptUser")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgPacket:acceptUser:acceptUser")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public AcceptUser getAcceptUser();
	/*Radix::CfgManagement::CfgPacket:srcExpTime:srcExpTime-Presentation Property*/


	public class SrcExpTime extends org.radixware.kernel.common.client.models.items.properties.PropertyDateTime{
		public SrcExpTime(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgPacket:srcExpTime:srcExpTime")
		public  java.sql.Timestamp getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgPacket:srcExpTime:srcExpTime")
		public   void setValue(java.sql.Timestamp val) {
			Value = val;
		}
	}
	public SrcExpTime getSrcExpTime();
	/*Radix::CfgManagement::CfgPacket:lastModifyTime:lastModifyTime-Presentation Property*/


	public class LastModifyTime extends org.radixware.kernel.common.client.models.items.properties.PropertyDateTime{
		public LastModifyTime(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgPacket:lastModifyTime:lastModifyTime")
		public  java.sql.Timestamp getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgPacket:lastModifyTime:lastModifyTime")
		public   void setValue(java.sql.Timestamp val) {
			Value = val;
		}
	}
	public LastModifyTime getLastModifyTime();
	/*Radix::CfgManagement::CfgPacket:srcAppVer:srcAppVer-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgPacket:srcAppVer:srcAppVer")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgPacket:srcAppVer:srcAppVer")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public SrcAppVer getSrcAppVer();
	/*Radix::CfgManagement::CfgPacket:title:title-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgPacket:title:title")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgPacket:title:title")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Title getTitle();
	/*Radix::CfgManagement::CfgPacket:classTitle:classTitle-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgPacket:classTitle:classTitle")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgPacket:classTitle:classTitle")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ClassTitle getClassTitle();
	/*Radix::CfgManagement::CfgPacket:stateChangelog:stateChangelog-Presentation Property*/


	public class StateChangelog extends org.radixware.kernel.common.client.models.items.properties.PropertyObject{
		public StateChangelog(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadParentRefPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgPacket:stateChangelog:stateChangelog")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgPacket:stateChangelog:stateChangelog")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public StateChangelog getStateChangelog();


}

/* Radix::CfgManagement::CfgPacket - Desktop Meta*/

/*Radix::CfgManagement::CfgPacket-Entity Class*/

package org.radixware.ads.CfgManagement.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class CfgPacket_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::CfgManagement::CfgPacket:Presentations-Entity Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecWMN3P5J5ONCFPLJVGBFAKZXZXU"),
			"Radix::CfgManagement::CfgPacket",
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("imgEZIMROBN5VCVNPUOAIAP7L2BBU"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("sprJGWXV6TQQRD3PIY4DT4JJU63ZQ"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2G45JJWSARAG3NTBN3RFRLGW6M"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5DECPC3MHNGQTFSZKW5OOUUS7Y"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2G45JJWSARAG3NTBN3RFRLGW6M"),0,

			/*Radix::CfgManagement::CfgPacket:Properties-Properties*/
			new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

					/*Radix::CfgManagement::CfgPacket:acceptTime:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEF5WPJPV7JBATHO6TXOMAGWJEI"),
						"acceptTime",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPTPVRXE6QVFTDPNYWJFZY6KYFQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecWMN3P5J5ONCFPLJVGBFAKZXZXU"),
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
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::CfgManagement::CfgPacket:acceptTime:PropertyPresentation:Edit Options:-Edit Mask Date Time*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskDateTime(org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,null,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::CfgManagement::CfgPacket:acceptUser:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colLMJ5EPIAOVAPHJFUNG2BTNME6A"),
						"acceptUser",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFGCRFIF6XJHIXBU5FLBNLRMSMQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecWMN3P5J5ONCFPLJVGBFAKZXZXU"),
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

						/*Radix::CfgManagement::CfgPacket:acceptUser:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,250,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::CfgManagement::CfgPacket:id:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDZOPCTNIURBCBPLCP75JMZCFYM"),
						"id",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsM4FRSKZIOJB4PLDZBYPZV5B4GM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecWMN3P5J5ONCFPLJVGBFAKZXZXU"),
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

						/*Radix::CfgManagement::CfgPacket:id:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::CfgManagement::CfgPacket:lastModifyTime:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMCRBKZP4GNADJJLP2J7ITEGELY"),
						"lastModifyTime",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMHR2DKL4IRBOFG3ZIVCLFFQV4Q"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecWMN3P5J5ONCFPLJVGBFAKZXZXU"),
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

						/*Radix::CfgManagement::CfgPacket:lastModifyTime:PropertyPresentation:Edit Options:-Edit Mask Date Time*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskDateTime(org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,null,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::CfgManagement::CfgPacket:lastModifyUser:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col4ESP7TDW4JGG7FZ4PMVZ4IUO74"),
						"lastModifyUser",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHOINA7GGDBEENOKBFSI6KO63NU"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecWMN3P5J5ONCFPLJVGBFAKZXZXU"),
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

						/*Radix::CfgManagement::CfgPacket:lastModifyUser:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,250,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::CfgManagement::CfgPacket:notes:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colCQOZYZGSGZBLNNG6KX255SLN5Q"),
						"notes",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKAGGDK7R3BF3VI3Y3DWLKZV74Y"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecWMN3P5J5ONCFPLJVGBFAKZXZXU"),
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

						/*Radix::CfgManagement::CfgPacket:notes:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,true),
						null,
						null,
						null,
						true,-1,-1,1,
						true,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::CfgManagement::CfgPacket:srcAppVer:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNLZ5BGYZ7BGVRCQYC6WJWZIBM4"),
						"srcAppVer",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7NMAVEKT2ZF6FF4JXZO2TITPQQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecWMN3P5J5ONCFPLJVGBFAKZXZXU"),
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

						/*Radix::CfgManagement::CfgPacket:srcAppVer:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::CfgManagement::CfgPacket:srcDbUrl:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colAJ6PVNFK3FBIVPACM2HG6NHSMU"),
						"srcDbUrl",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRYHKTWXKQ5CT5KD23NAI3G7GVY"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecWMN3P5J5ONCFPLJVGBFAKZXZXU"),
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

						/*Radix::CfgManagement::CfgPacket:srcDbUrl:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::CfgManagement::CfgPacket:srcExpTime:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colLQHEBRDALNHZHNCQB76A3BEP6U"),
						"srcExpTime",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQWMETCETYJHZDFGJT4GKYPQANY"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecWMN3P5J5ONCFPLJVGBFAKZXZXU"),
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
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::CfgManagement::CfgPacket:srcExpTime:PropertyPresentation:Edit Options:-Edit Mask Date Time*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskDateTime(org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,null,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::CfgManagement::CfgPacket:srcExpUser:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col6GQBFOYQYFCABDAMANXVXFGU3Q"),
						"srcExpUser",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsSANFELSPIBGFLHPKNUIEW35C4A"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecWMN3P5J5ONCFPLJVGBFAKZXZXU"),
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

						/*Radix::CfgManagement::CfgPacket:srcExpUser:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,250,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::CfgManagement::CfgPacket:srcPacketId:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colCY7ILVPBW5FCRBUUHJPC7LRBJY"),
						"srcPacketId",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNAZLRVZDGBB2XDS4V7X5F5CSKI"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecWMN3P5J5ONCFPLJVGBFAKZXZXU"),
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

						/*Radix::CfgManagement::CfgPacket:srcPacketId:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::CfgManagement::CfgPacket:title:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colT6AXXF6DBVAQDFOGFG4QARZYJU"),
						"title",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJQQWOTISZRCQPDANF3S54KPD2I"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecWMN3P5J5ONCFPLJVGBFAKZXZXU"),
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

						/*Radix::CfgManagement::CfgPacket:title:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,200,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::CfgManagement::CfgPacket:classTitle:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdT42SQ7RCQ5CYRIIBFO7TVMVA4A"),
						"classTitle",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsREB4DYXOSZAAPERWFHXFSTFL2A"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecWMN3P5J5ONCFPLJVGBFAKZXZXU"),
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

						/*Radix::CfgManagement::CfgPacket:classTitle:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::CfgManagement::CfgPacket:pkgState:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDKBRCEEYTFDBBAQ3ZE5GOMIXR4"),
						"pkgState",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZQETKL6GB5HEBHRTGQSJY5U5FE"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecWMN3P5J5ONCFPLJVGBFAKZXZXU"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acsKBHFAZGGC5AYTFAQNXXU4URWCE"),
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

						/*Radix::CfgManagement::CfgPacket:pkgState:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acsKBHFAZGGC5AYTFAQNXXU4URWCE"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.NONE,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::CfgManagement::CfgPacket:stateChangelog:PropertyPresentation-Object Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruIV5OQZASNNB4XGWKPXUGHOHAAQ"),
						"stateChangelog",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecWMN3P5J5ONCFPLJVGBFAKZXZXU"),
						org.radixware.kernel.common.enums.EValType.OBJECT,
						org.radixware.kernel.common.enums.EPropNature.USER,
						5,
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
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecVHY5COSHWNA7PPJ65FW3OGJ4ZQ"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblVHY5COSHWNA7PPJ65FW3OGJ4ZQ"),
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprVRZPY6RJKBHWFLK46ATZLXFW3U")},
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprVRZPY6RJKBHWFLK46ATZLXFW3U")},
						null,
						0L,
						0L,false,false)
			},
			null,
			null,
			new org.radixware.kernel.common.client.meta.RadSortingDef[]{

					new org.radixware.kernel.common.client.meta.RadSortingDef(
					/*Radix::CfgManagement::CfgPacket:Id-Sorting*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("srtUNCGLTJZI5B55BYSKIWTLX25ZE"),
						"Id",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecWMN3P5J5ONCFPLJVGBFAKZXZXU"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2SQGUBEYBFDHBCYMYFYCEBI2XE"),
						new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem[]{

								new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDZOPCTNIURBCBPLCP75JMZCFYM"),
								false)
						})
			},
			null,
			new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprJHOQCOZIOBBWNABEIAT5XEG7M4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("sprJGWXV6TQQRD3PIY4DT4JJU63ZQ")},
			true,false,false);
}

/* Radix::CfgManagement::CfgPacket - Web Meta*/

/*Radix::CfgManagement::CfgPacket-Entity Class*/

package org.radixware.ads.CfgManagement.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class CfgPacket_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::CfgManagement::CfgPacket:Presentations-Entity Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecWMN3P5J5ONCFPLJVGBFAKZXZXU"),
			"Radix::CfgManagement::CfgPacket",
			null,
			null,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2G45JJWSARAG3NTBN3RFRLGW6M"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5DECPC3MHNGQTFSZKW5OOUUS7Y"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2G45JJWSARAG3NTBN3RFRLGW6M"),0,
			null,
			null,
			null,
			null,
			null,
			null,
			false,false,false);
}

/* Radix::CfgManagement::CfgPacket:General - Desktop Meta*/

/*Radix::CfgManagement::CfgPacket:General-Editor Presentation*/

package org.radixware.ads.CfgManagement.explorer;
public final class General_mi{
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new org.radixware.kernel.common.client.meta.RadEditorPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprJHOQCOZIOBBWNABEIAT5XEG7M4"),
	"General",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
	null,
	org.radixware.kernel.common.types.Id.Factory.loadFrom("aecWMN3P5J5ONCFPLJVGBFAKZXZXU"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("tblWMN3P5J5ONCFPLJVGBFAKZXZXU"),
	null,
	null,

	/*Radix::CfgManagement::CfgPacket:General:Editor Pages-Editor Presentation Pages*/
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

			/*Radix::CfgManagement::CfgPacket:General:General-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgLB467MBHIRAKXEEF2YUVLYF5GI"),"General",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecWMN3P5J5ONCFPLJVGBFAKZXZXU"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXSHBCHAZ2VEPNLWP7EMTGS6K5A"),null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDZOPCTNIURBCBPLCP75JMZCFYM"),0,0,2,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colT6AXXF6DBVAQDFOGFG4QARZYJU"),0,2,2,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colCQOZYZGSGZBLNNG6KX255SLN5Q"),0,3,2,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col4ESP7TDW4JGG7FZ4PMVZ4IUO74"),0,5,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMCRBKZP4GNADJJLP2J7ITEGELY"),1,5,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdT42SQ7RCQ5CYRIIBFO7TVMVA4A"),0,1,2,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDKBRCEEYTFDBBAQ3ZE5GOMIXR4"),0,4,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruIV5OQZASNNB4XGWKPXUGHOHAAQ"),1,4,1,false,false)
			},null),

			/*Radix::CfgManagement::CfgPacket:General:Events-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadContainerEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgROII5GGFWJGS3AHGCDQTDEQ3ME"),"Events",null,null,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiC3WF6WEXGZDUTAKXY4G3E4LNUE"))
	},
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgLB467MBHIRAKXEEF2YUVLYF5GI")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgROII5GGFWJGS3AHGCDQTDEQ3ME"))}
	,

	/*Radix::CfgManagement::CfgPacket:General:Children-Explorer Items*/
		new org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemDef[]{

				/*Radix::CfgManagement::CfgPacket:General:EventLog-Entity Explorer Item*/

				new org.radixware.kernel.common.client.meta.explorerItems.RadSelectorExplorerItemDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiC3WF6WEXGZDUTAKXY4G3E4LNUE"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aecWMN3P5J5ONCFPLJVGBFAKZXZXU"),null,
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("sprVJGCRERZOBDS7GULG6RLDVLG3A"),
					0,
					null,
					16560,false)
		}
	, new 
	org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[]{new org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprJHOQCOZIOBBWNABEIAT5XEG7M4"),
			new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiC3WF6WEXGZDUTAKXY4G3E4LNUE")},null)}
	,
	null,
	0,
	null,
	(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
	2192,0,0,null);
}
/* Radix::CfgManagement::CfgPacket:General:Model - Desktop Executable*/

/*Radix::CfgManagement::CfgPacket:General:Model-Entity Model Class*/

package org.radixware.ads.CfgManagement.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgPacket:General:Model")
public class General:Model  extends org.radixware.ads.CfgManagement.explorer.CfgPacket.CfgPacket_DefaultModel implements org.radixware.ads.System.common_client.IEventContextProvider  {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return General:Model_mi.rdxMeta; }



	public General:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadEditorPresentationDef def){super(userSession,def);}

	/*Radix::CfgManagement::CfgPacket:General:Model:Nested classes-Nested Classes*/

	/*Radix::CfgManagement::CfgPacket:General:Model:Properties-Properties*/

	/*Radix::CfgManagement::CfgPacket:General:Model:pkgState-Presentation Property*/




	public class PkgState extends org.radixware.ads.CfgManagement.explorer.CfgPacket.colDKBRCEEYTFDBBAQ3ZE5GOMIXR4{
		public PkgState(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public void setServerValue(org.radixware.kernel.common.client.models.items.properties.PropertyValue val){
			Object x = val.getValue();
			org.radixware.ads.CfgManagement.common.CfgPacketState dummy = x == null ? null : (x instanceof org.radixware.ads.CfgManagement.common.CfgPacketState ? (org.radixware.ads.CfgManagement.common.CfgPacketState)x : org.radixware.ads.CfgManagement.common.CfgPacketState.getForValue((Str)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.STR)));
			val.refineValue(dummy);
			super.setServerValue(val);
		}
		@Override
		public Class<org.radixware.ads.CfgManagement.common.CfgPacketState> getValClass(){
			return org.radixware.ads.CfgManagement.common.CfgPacketState.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.ads.CfgManagement.common.CfgPacketState dummy = x == null ? null : (x instanceof org.radixware.ads.CfgManagement.common.CfgPacketState ? (org.radixware.ads.CfgManagement.common.CfgPacketState)x : org.radixware.ads.CfgManagement.common.CfgPacketState.getForValue((Str)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.STR)));
			setValue(dummy);
		}

		/*Radix::CfgManagement::CfgPacket:General:Model:pkgState:EmbeddedClass-Property Presentation Embedded Class*/




		/*Radix::CfgManagement::CfgPacket:General:Model:pkgState:Nested classes-Nested Classes*/

		/*Radix::CfgManagement::CfgPacket:General:Model:pkgState:Properties-Properties*/

		/*Radix::CfgManagement::CfgPacket:General:Model:pkgState:Methods-Methods*/

		/*Radix::CfgManagement::CfgPacket:General:Model:pkgState:isVisible-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgPacket:General:Model:pkgState:isVisible")
		public published  boolean isVisible () {
			return super.isVisible() && !isNew();
		}






				














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgPacket:General:Model:pkgState")
		public  org.radixware.ads.CfgManagement.common.CfgPacketState getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgPacket:General:Model:pkgState")
		public   void setValue(org.radixware.ads.CfgManagement.common.CfgPacketState val) {
			Value = val;
		}
	}
	public PkgState getPkgState(){return (PkgState)getProperty(colDKBRCEEYTFDBBAQ3ZE5GOMIXR4);}








	/*Radix::CfgManagement::CfgPacket:General:Model:Methods-Methods*/

	/*Radix::CfgManagement::CfgPacket:General:Model:getEventContextId-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgPacket:General:Model:getEventContextId")
	public published  Str getEventContextId () {
		return id.Value.toString();

	}

	/*Radix::CfgManagement::CfgPacket:General:Model:getEventContextType-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgPacket:General:Model:getEventContextType")
	public published  Str getEventContextType () {
		return Arte::EventContextType:CfgPackage.Value;

	}


}

/* Radix::CfgManagement::CfgPacket:General:Model - Desktop Meta*/

/*Radix::CfgManagement::CfgPacket:General:Model-Entity Model Class*/

package org.radixware.ads.CfgManagement.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class General:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aemJHOQCOZIOBBWNABEIAT5XEG7M4"),
						"General:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::CfgManagement::CfgPacket:General:Model:Properties-Properties*/
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

/* Radix::CfgManagement::CfgPacket:General - Desktop Meta*/

/*Radix::CfgManagement::CfgPacket:General-Selector Presentation*/

package org.radixware.ads.CfgManagement.explorer;
public final class General_mi extends org.radixware.kernel.common.client.meta.RadSelectorPresentationDef{
	public static final org.radixware.kernel.common.client.meta.RadSelectorPresentationDef rdxMeta = new General_mi();
	private General_mi(){
		super(org.radixware.kernel.common.types.Id.Factory.loadFrom("sprJGWXV6TQQRD3PIY4DT4JJU63ZQ"),
		"General",
		org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
		null,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("aecWMN3P5J5ONCFPLJVGBFAKZXZXU"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("tblWMN3P5J5ONCFPLJVGBFAKZXZXU"),
		null,
		null,
		null,
		null,
		true,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("srtUNCGLTJZI5B55BYSKIWTLX25ZE"),
		null,
		false,
		true,
		null,
		32,
		null,
		144,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprJHOQCOZIOBBWNABEIAT5XEG7M4")},
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprJHOQCOZIOBBWNABEIAT5XEG7M4")},
		false,true,false,0,0);
		columns = new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn[]{

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDZOPCTNIURBCBPLCP75JMZCFYM"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdT42SQ7RCQ5CYRIIBFO7TVMVA4A"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colT6AXXF6DBVAQDFOGFG4QARZYJU"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.STRETCH,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDKBRCEEYTFDBBAQ3ZE5GOMIXR4"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMCRBKZP4GNADJJLP2J7ITEGELY"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col4ESP7TDW4JGG7FZ4PMVZ4IUO74"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null)
		};
	}
}
/* Radix::CfgManagement::CfgPacket:General:Model - Desktop Executable*/

/*Radix::CfgManagement::CfgPacket:General:Model-Group Model Class*/

package org.radixware.ads.CfgManagement.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgPacket:General:Model")
public class General:Model  extends org.radixware.ads.CfgManagement.explorer.CfgPacket.DefaultGroupModel {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return General:Model_mi.rdxMeta; }

	class UnexpectedXmlDocumentException extends Exceptions::Exception implements org.radixware.kernel.common.client.errors.IClientError {
	    
	    private final String title;

	    protected UnexpectedXmlDocumentException(final String title, final String message) {
	        super(message);
	        this.title = title;
	    }

	    protected UnexpectedXmlDocumentException(final String title, final String message, final Throwable cause) {
	        super(message, cause);
	        this.title = title;
	    }
	    
	    @Override
	    public String getTitle(Client.Env::MessageProvider mp) {
	        return title;
	    }

	    @Override
	    public String getDetailMessage(Client.Env::MessageProvider mp) {
	        return getMessage();
	    }

	    @Override
	    public String getLocalizedMessage(Client.Env::MessageProvider messageProvider) {
	        return getMessage();
	    }
	}

	public General:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadSelectorPresentationDef def){super(userSession,def);}

	/*Radix::CfgManagement::CfgPacket:General:Model:Nested classes-Nested Classes*/

	/*Radix::CfgManagement::CfgPacket:General:Model:Properties-Properties*/

	/*Radix::CfgManagement::CfgPacket:General:Model:Methods-Methods*/

	/*Radix::CfgManagement::CfgPacket:General:Model:onCommand_Import-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgPacket:General:Model:onCommand_Import")
	protected  void onCommand_Import (org.radixware.ads.CfgManagement.explorer.CfgPacketGroup.Import command) {
		try {
		    java.io.File file = DesktopUtils.openFileForImport(findNearestView(), command.getTitle());
		    if (file == null)
		        return;
		    final ImpExpXsd:PacketDocument xDoc = ImpExpXsd:PacketDocument.Factory.parse(file);
		    xDoc.Packet.FileName = file.getName();
		    Arte::TypesXsd:IntDocument rs = command.send(xDoc);
		    if (rs != null)
		        getGroupView().getSelectorWidget().rereadAndSetCurrent(new Pid(idof[CfgManagement::Packet], rs.Int));
		} catch (Exceptions::XmlException e) {
		    showException(new UnexpectedXmlDocumentException("Import Package", "The specified file does not match the configuration package format.", e));
		} catch (Exceptions::Exception e) {
		    showException(e);
		}

	}

	/*Radix::CfgManagement::CfgPacket:General:Model:onCommand_ExportUDS-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgPacket:General:Model:onCommand_ExportUDS")
	public published  void onCommand_ExportUDS (org.radixware.ads.CfgManagement.explorer.CfgPacketGroup.ExportUDS command) {
		Explorer.Dialogs::UdsModulesDownloader.download(getEnvironment(), (Explorer.Qt.Types::QWidget)findNearestView());
	}
	public final class ExportUDS extends org.radixware.ads.CfgManagement.explorer.CfgPacketGroup.ExportUDS{
		protected ExportUDS(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_ExportUDS( this );
		}

	}

	public final class Import extends org.radixware.ads.CfgManagement.explorer.CfgPacketGroup.Import{
		protected Import(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_Import( this );
		}

	}















}

/* Radix::CfgManagement::CfgPacket:General:Model - Desktop Meta*/

/*Radix::CfgManagement::CfgPacket:General:Model-Group Model Class*/

package org.radixware.ads.CfgManagement.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class General:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("agmJGWXV6TQQRD3PIY4DT4JJU63ZQ"),
						"General:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::CfgManagement::CfgPacket:General:Model:Properties-Properties*/
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

/* Radix::CfgManagement::CfgPacket - Localizing Bundle */
package org.radixware.ads.CfgManagement.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class CfgPacket - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Configuration Package");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Пакет конфигурации");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2G45JJWSARAG3NTBN3RFRLGW6M"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"By ID");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"По ид.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2SQGUBEYBFDHBCYMYFYCEBI2XE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Configuration Package");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2XGS24NALNGZ5DAWPXDP5UTOQQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Configuration package created");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Пакет конфигурации создан");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4RT7NJYSBFAQNI6UYB3ERBTK3U"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.EVENT_CODE,org.radixware.kernel.common.enums.EEventSeverity.EVENT,"App",null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Configuration Packages");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Пакеты конфигурации");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5DECPC3MHNGQTFSZKW5OOUUS7Y"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"The specified file does not match the configuration package format.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Указанный файл не соответствует формату пакета конфигурации.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5HJC3ZRDFJDF5MJA5SKYR2IDCU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Source system version:");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Версия системы источника:");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6BOFBGWRVZCBVMIDQOZBHQ5ZQM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Items with version conflict:");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Элементы с конфликтом версий:");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6JUFNYPCONGPJOWE7UHDGX2G6M"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Source system version");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Версия системы-источника");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7NMAVEKT2ZF6FF4JXZO2TITPQQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Accepted by");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Акцептован пользователем");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFGCRFIF6XJHIXBU5FLBNLRMSMQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Item");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Элемент");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGNR3IEKMSNCDRETV3MORTIRALY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Last updated by");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Автор последней модификации");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHOINA7GGDBEENOKBFSI6KO63NU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Name");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Название");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJQQWOTISZRCQPDANF3S54KPD2I"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Notes");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Заметки");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKAGGDK7R3BF3VI3Y3DWLKZV74Y"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"ID");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ид.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsM4FRSKZIOJB4PLDZBYPZV5B4GM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"There is version mismatches! Do you wish to continue?");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Обнаружено несовпадение версий! Вы хотите продолжить?");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsM4G5A4WS5JFT7GNYYZZBG5MJEU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Last updated");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Время последней модификации");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMHR2DKL4IRBOFG3ZIVCLFFQV4Q"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Package ID in source system");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ид. пакета в системе-источнике");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNAZLRVZDGBB2XDS4V7X5F5CSKI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Import Package");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Импорт пакета");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsO2TG4HMARZHHRLP737I3UEQQYI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Version");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Версия");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOKHBPJHSHJBSLNCD5FQEY2LKTE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Accepted");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Время акцептации");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPTPVRXE6QVFTDPNYWJFZY6KYFQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Exported");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Время экспорта");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQWMETCETYJHZDFGJT4GKYPQANY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Class");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Класс");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsREB4DYXOSZAAPERWFHXFSTFL2A"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Source system DB");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"База данных системы-источника");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRYHKTWXKQ5CT5KD23NAI3G7GVY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Exported by");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Экспортирован пользователем");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsSANFELSPIBGFLHPKNUIEW35C4A"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"General");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Общее");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXSHBCHAZ2VEPNLWP7EMTGS6K5A"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"This system version:");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Версия этой системы:");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZDHPHHFWYBDI7NKJFCX5WYW6IQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Version of this system");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Версия этой системы");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZIY6QFAWIVEPBK6FPIB6KFKRQU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"State");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Состояние");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZQETKL6GB5HEBHRTGQSJY5U5FE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(CfgPacket - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbaecWMN3P5J5ONCFPLJVGBFAKZXZXU"),"CfgPacket - Localizing Bundle",$$$items$$$);
}
