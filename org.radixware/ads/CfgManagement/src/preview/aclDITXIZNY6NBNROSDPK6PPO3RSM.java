
/* Radix::CfgManagement::CfgPacket.Export - Server Executable*/

/*Radix::CfgManagement::CfgPacket.Export-Application Class*/

package org.radixware.ads.CfgManagement.server;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgPacket.Export")
public published class CfgPacket.Export  extends org.radixware.ads.CfgManagement.server.CfgPacket  implements org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {


	/**Metainformation accessor method*/
	@SuppressWarnings("unused")
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return CfgPacket.Export_mi.rdxMeta;}

	/*Radix::CfgManagement::CfgPacket.Export:Nested classes-Nested Classes*/

	/*Radix::CfgManagement::CfgPacket.Export:Properties-Properties*/

	/*Radix::CfgManagement::CfgPacket.Export:preparedPkgStateChangedDoc-Dynamic Property*/



	protected org.radixware.schemas.commondef.ChangeLogItem preparedPkgStateChangedDoc=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgPacket.Export:preparedPkgStateChangedDoc")
	private final  org.radixware.schemas.commondef.ChangeLogItem getPreparedPkgStateChangedDoc() {
		return preparedPkgStateChangedDoc;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgPacket.Export:preparedPkgStateChangedDoc")
	private final   void setPreparedPkgStateChangedDoc(org.radixware.schemas.commondef.ChangeLogItem val) {
		preparedPkgStateChangedDoc = val;
	}

	/*Radix::CfgManagement::CfgPacket.Export:pkgState-Column-Based Property*/




	@Override
	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgPacket.Export:pkgState")
	public published   void setPkgState(org.radixware.ads.CfgManagement.common.CfgPacketState val) {

		if (!isNewObject() && val != null && val != internal[pkgState]) {
		    final Str description;
		    if (internal[pkgState] == null) {
		        description = Str.format("Package created with state '%s'", val.Title);
		    } else {
		        description = Str.format("Package state changed from '%s' to '%s'", internal[pkgState].Title, val.Title);
		    }
		    preparedPkgStateChangedDoc = Revision.createRevisionXml(description);
		}

		internal[pkgState] = val;
	}









































	/*Radix::CfgManagement::CfgPacket.Export:Methods-Methods*/

	/*Radix::CfgManagement::CfgPacket.Export:onCommand_ToImport-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgPacket.Export:onCommand_ToImport")
	public  void onCommand_ToImport (org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValHandlersById) {
		CfgPacket.Import dest = new CfgPacket.Import();
		dest.init();
		dest.srcPacketId = id;
		dest.srcExpTime = Utils::Timing.getCurrentTime();
		dest.srcExpUser = Arte::Arte.getUserName();
		dest.srcAppVer = Arte::Arte.getAllLayerVersionsAsString();
		dest.srcDbUrl = getDbUrl();
		dest.lastModifyTime = dest.srcExpTime;
		dest.lastModifyUser = dest.srcExpUser;
		dest.title = "Copy of '" + calcTitle() + "'";
		dest.notes = notes;
		dest.create();

		copyItems(id, dest.id);

		CfgItemsCursor c = CfgItemsCursor.open(dest.id, false, null);
		try {
		    while (c.next()) 
		        c.item.createRefs();
		} finally {
		    c.close();
		}

		Arte::Trace.enterContext(Arte::EventContextType:CfgPackage, dest.id);
		Arte::Trace.put(eventCode["Package copied from package '%1'"], calcTitle());
		Arte::Trace.leaveContext(Arte::EventContextType:CfgPackage, dest.id);

		dest.link();

	}

	/*Radix::CfgManagement::CfgPacket.Export:getDbUrl-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgPacket.Export:getDbUrl")
	private final  Str getDbUrl () {
		try {
		    return Arte::Arte.getDbConnection().getMetaData().getURL();
		} catch (Exceptions::SQLException e) {
		    throw new DatabaseError(e);
		}


	}

	/*Radix::CfgManagement::CfgPacket.Export:onCommand_Actualize-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgPacket.Export:onCommand_Actualize")
	  void onCommand_Actualize (org.radixware.schemas.types.MapStrStrDocument input, org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValHandlersById) {
		// .(); TWRBS-9776
		ArrInt itemsIds = null;
		boolean allowCreateAllAbsentItems = false;
		for (Arte::TypesXsd:MapStrStr.Entry e : input.MapStrStr.EntryList) {
		    if (e.Key.equals("ItemsIds")) {
		        itemsIds = ArrInt.fromValAsStr(e.Value);
		    } else if (e.Key.equals("AllowCreate")) {
		        allowCreateAllAbsentItems = Bool.valueOf(e.Value).booleanValue();
		    }
		}

		CfgSettingsProviderForActualize provider = new CfgSettingsProviderForActualize(allowCreateAllAbsentItems);
		try (CfgItemsByIdCursor c = CfgItemsByIdCursor.open(id, itemsIds, false)) {
		    while (c.next()) {
		        if (c.item.beforeActualizeExport()) {
		            c.item.actualizeExport(provider);
		        }
		    }
		    Arte::Trace.enterContext(Arte::EventContextType:CfgPackage, id);
		    Arte::Trace.put(eventCode["Package updated"]);
		    Arte::Trace.leaveContext(Arte::EventContextType:CfgPackage, id);
		}

		try (CfgItemsByIdCursor settingsCursor = CfgItemsByIdCursor.open(id, itemsIds, true)) {
		    while (settingsCursor.next()) {
		        if (!provider.wasItemActualized(settingsCursor.id)) {
		            settingsCursor.item.delete();
		        }
		    }
		}

		onItemsModified();
	}

	/*Radix::CfgManagement::CfgPacket.Export:getOrCreateItem-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgPacket.Export:getOrCreateItem")
	 final  org.radixware.ads.CfgManagement.server.CfgItem getOrCreateItem (org.radixware.ads.CfgManagement.server.CfgExportData data, org.radixware.ads.CfgManagement.server.CfgItem parent, org.radixware.ads.CfgManagement.server.ICfgImportHelper helper, org.radixware.ads.CfgManagement.server.ICfgSettingsProvider settingsProvider) {
		CfgItem item = null;

		CfgSettingsProviderForActualize actualizeProvider = null;
		if (settingsProvider instanceof CfgSettingsProviderForActualize) {
		    actualizeProvider = (CfgSettingsProviderForActualize) settingsProvider;
		    item = actualizeProvider.curItem;
		    actualizeProvider.curItem = null;
		}

		if (item == null && data.object != null) {
		    CfgItemsBySrcPidCursor cursor = CfgItemsBySrcPidCursor.open(id,
		            data.itemClassId.toString(),
		            data.object.getPid().toString());
		    try {
		        if (cursor.next()) {
		            item = cursor.item;
		        }
		    } finally {
		        cursor.close();
		    }
		}

		final boolean createNewItem;
		if (item == null) {
		    if (actualizeProvider != null && !actualizeProvider.isItemsCreationAllowed()) {
		        return null;
		    }
		    item = (CfgItem) Arte::Arte.newObject(data.itemClassId);
		    createNewItem = true;
		} else {
		    createNewItem = false;
		}

		if (createNewItem) {
		    item.packet = this;
		    item.parent = parent;
		    item.init();
		}

		if (data.object != null) {
		    item.srcObjectPid = data.object.getPid().toString();
		    item.srcObjectTitle = data.object.calcTitle();
		    item.extGuid = data.getExtGuid();
		}
		item.srcObjectRid = data.objectRid;
		item.data = data.data;
		if (settingsProvider != null) {
		    if (actualizeProvider != null) {
		        actualizeProvider.setItemActualizeStatus(item.id, true);
		    } else {
		        item.settings = settingsProvider.getSettings(item);
		    }
		}

		if (createNewItem) {
		    item.create();
		    if (helper != null) {
		        helper.reportObjectCreated(item);
		    }
		} else {
		    //item.();
		    item.updateAppVersion();
		    if (helper != null) {
		        helper.reportObjectUpdated(item);
		    }
		}

		for (CfgExportData d : data.children) {
		    getOrCreateItem(d, item, helper, settingsProvider);
		}

		return item;
	}

	/*Radix::CfgManagement::CfgPacket.Export:onCommand_Export-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgPacket.Export:onCommand_Export")
	  org.radixware.ads.CfgManagement.common.ImpExpXsd.PacketDocument onCommand_Export (org.radixware.schemas.types.StrDocument input, org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValHandlersById) {
		ImpExpXsd:PacketDocument doc = export();
		if (!CfgPacket.checkPackageVersions(doc)) {
		    return null;
		}
		pkgState = CfgPacketState:Exported;
		Arte::Trace.enterContext(Arte::EventContextType:CfgPackage, id);
		Arte::Trace.put(eventCode["Package exported to file '%1'"], input.Str);
		Arte::Trace.leaveContext(Arte::EventContextType:CfgPackage, id);
		return doc;

	}

	/*Radix::CfgManagement::CfgPacket.Export:export-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgPacket.Export:export")
	public published  org.radixware.ads.CfgManagement.common.ImpExpXsd.PacketDocument export () {
		ImpExpXsd:PacketDocument doc = ImpExpXsd:PacketDocument.Factory.newInstance();
		doc.addNewPacket();
		doc.Packet.Id = id;
		doc.Packet.ExpTime = Utils::Timing.getCurrentTime();
		doc.Packet.ExpUser = Arte::Arte.getUserName();
		doc.Packet.AppVer = Arte::Arte.getAllLayerVersionsAsString();
		doc.Packet.DbUrl = getDbUrl();
		doc.Packet.Title = title;
		doc.Packet.Notes = notes;

		CfgItemsCursor c = CfgItemsCursor.open(id, true, null);
		try {
		    while (c.next()) {
		        ImpExpXsd:Item xi = c.item.export();
		        doc.Packet.ItemList.add(xi);
		    }
		} finally {
		    c.close();
		}
		return doc;
	}

	/*Radix::CfgManagement::CfgPacket.Export:onItemsModified-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgPacket.Export:onItemsModified")
	  void onItemsModified () {
		//do nothing
	}

	/*Radix::CfgManagement::CfgPacket.Export:afterInit-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgPacket.Export:afterInit")
	protected published  void afterInit (org.radixware.kernel.server.types.Entity src, org.radixware.kernel.common.enums.EEntityInitializationPhase phase) {
		super.afterInit(src, phase);

		pkgState = CfgPacketState:UnderConstruction;

		preparedPkgStateChangedDoc = Revision.createRevisionXml(Str.format("Package created with state '%s'", pkgState.Title));;
		storePkgStateChangelog();
	}

	/*Radix::CfgManagement::CfgPacket.Export:beforeUpdate-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgPacket.Export:beforeUpdate")
	protected published  boolean beforeUpdate () {
		if  (super.beforeUpdate()) {
		    storePkgStateChangelog();
		    return true;
		}
		return false;
	}

	/*Radix::CfgManagement::CfgPacket.Export:storePkgStateChangelog-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgPacket.Export:storePkgStateChangelog")
	private final  void storePkgStateChangelog () {
		if (preparedPkgStateChangedDoc != null) {
		    if (stateChangelog == null) {
		        ChangeLog log = new ChangeLog();
		        log.init();
		        setProp(idof[CfgPacket:stateChangelog], log);
		    }

		    preparedPkgStateChangedDoc.RevisionNumber = ChangelogUtils.getNextRevisionSeq(stateChangelog.lastRevisionSeq);    
		    Revision.importRevision(stateChangelog, preparedPkgStateChangedDoc);
		    stateChangelog.lastRevision = null;//clear property to recalculate changelog title
		    preparedPkgStateChangedDoc = null;
		}
	}





	@Override
	public org.radixware.kernel.server.types.FormHandler.NextDialogsRequest execCommand(org.radixware.kernel.common.types.Id cmdId, org.radixware.kernel.common.types.Id propId, org.apache.xmlbeans.XmlObject input, org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValsById, org.apache.xmlbeans.XmlObject output) throws org.radixware.kernel.common.exceptions.AppException,java.lang.InterruptedException{
		if(cmdId == cmdAJI4BLGC7JFKVDPIFGGIVNHS4M){
			onCommand_Actualize((org.radixware.schemas.types.MapStrStrDocument)org.radixware.kernel.common.utils.XmlObjectProcessor.getAppCommandInputData(getClass().getClassLoader(),input,org.radixware.schemas.types.MapStrStrDocument.class),newPropValsById);
			return null;
		} else if(cmdId == cmdJPIUY2C3XZD3ZDAMIV4NECN5HI){
			org.radixware.ads.CfgManagement.common.ImpExpXsd.PacketDocument result = onCommand_Export((org.radixware.schemas.types.StrDocument)org.radixware.kernel.common.utils.XmlObjectProcessor.getAppCommandInputData(getClass().getClassLoader(),input,org.radixware.schemas.types.StrDocument.class),newPropValsById);
			if(result != null)
				output.set(result);
			return null;
		} else if(cmdId == cmdNVFWLJH5ZNBSXBMYK26WA66QJM){
			onCommand_ToImport(newPropValsById);
			return null;
		} else 
			return super.execCommand(cmdId,propId,input,newPropValsById,output);
	}


}

/* Radix::CfgManagement::CfgPacket.Export - Server Meta*/

/*Radix::CfgManagement::CfgPacket.Export-Application Class*/

package org.radixware.ads.CfgManagement.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class CfgPacket.Export_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("aclDITXIZNY6NBNROSDPK6PPO3RSM"),"CfgPacket.Export",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOEAUSTY7NNDLVJIKFNG6DWLGDQ"),

						org.radixware.kernel.common.enums.EClassType.APPLICATION,

						/*Radix::CfgManagement::CfgPacket.Export:Presentations-Entity Object Presentations*/
						/*Class presentation attributes*/
						 new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef(/*Owner Class Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("aclDITXIZNY6NBNROSDPK6PPO3RSM"),
							/*Owner Class Name*/
							"CfgPacket.Export",
							/*Title Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOEAUSTY7NNDLVJIKFNG6DWLGDQ"),
							/*Property presentations*/

							/*Radix::CfgManagement::CfgPacket.Export:Properties-Properties*/
							new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef[]{

									/*Radix::CfgManagement::CfgPacket.Export:pkgState:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDKBRCEEYTFDBBAQ3ZE5GOMIXR4"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.TITLE,org.radixware.kernel.common.enums.EPropAttrInheritance.EDITING,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_SELECT_CONDITION,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_SELECTOR,org.radixware.kernel.common.enums.EPropAttrInheritance.HINT))
							},
							/*Commands*/
							new org.radixware.kernel.server.meta.presentations.RadCommandDef[]{

									new org.radixware.kernel.server.meta.presentations.RadCommandDef(
									/*Radix::CfgManagement::CfgPacket.Export:ToImport-Object Command*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdNVFWLJH5ZNBSXBMYK26WA66QJM"),"ToImport",org.radixware.kernel.common.enums.ECommandScope.OBJECT,null,org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_FIXED,org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,org.radixware.kernel.common.types.Id.Factory.loadFrom("aclDITXIZNY6NBNROSDPK6PPO3RSM"),false,true),

									new org.radixware.kernel.server.meta.presentations.RadCommandDef(
									/*Radix::CfgManagement::CfgPacket.Export:Actualize-Object Command*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdAJI4BLGC7JFKVDPIFGGIVNHS4M"),"Actualize",org.radixware.kernel.common.enums.ECommandScope.OBJECT,null,org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_FIXED,org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,org.radixware.kernel.common.types.Id.Factory.loadFrom("aclDITXIZNY6NBNROSDPK6PPO3RSM"),false,true),

									new org.radixware.kernel.server.meta.presentations.RadCommandDef(
									/*Radix::CfgManagement::CfgPacket.Export:Export-Object Command*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdJPIUY2C3XZD3ZDAMIV4NECN5HI"),"Export",org.radixware.kernel.common.enums.ECommandScope.OBJECT,null,org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_FIXED,org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,org.radixware.kernel.common.types.Id.Factory.loadFrom("aclDITXIZNY6NBNROSDPK6PPO3RSM"),false,true)
							},
							/*Sortings*/
							null,
							/*Filters*/
							null,
							/*Editor presentations*/
							new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef[]{

									new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef(
									/*Radix::CfgManagement::CfgPacket.Export:General-Editor Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("epr3OHL2CWN7VG2RBOA3YDIPORKTM"),
									"General",
									org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprJHOQCOZIOBBWNABEIAT5XEG7M4"),
									36016,
									null,

									/*Radix::CfgManagement::CfgPacket.Export:General:Children-Explorer Items*/
										new org.radixware.kernel.server.meta.presentations.RadExplorerItemDef[]{

												/*Radix::CfgManagement::CfgPacket.Export:General:CfgItemExp-Child Ref Explorer Item*/

												new org.radixware.kernel.server.meta.presentations.RadChildRefExplorerItemDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiNLU3HH57UBGH5DOHCRVHMFXF2M"),
													org.radixware.kernel.common.types.Id.Factory.loadFrom("aclDITXIZNY6NBNROSDPK6PPO3RSM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ4WZRLJ23BGMBKQ2JBSOBEGBP4"),
													org.radixware.kernel.common.types.Id.Factory.loadFrom("spr4SSGPHYJVRAGLB7HGLSAGIAMIQ"),
													new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>",new org.radixware.kernel.server.meta.presentations.RadConditionDef.Prop2ValueCondition(new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colJ5L5OI3X4BHYBPJFGAWUO4MKZM")},new String[]{null}),"org.radixware"),
													org.radixware.kernel.common.types.Id.Factory.loadFrom("refF34ZWBF4TBBCHIUDOFBRI67OVY"),
													null,
													null)
										}
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
							org.radixware.kernel.common.utils.Maps.fromArrays(new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprJHOQCOZIOBBWNABEIAT5XEG7M4")},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr3OHL2CWN7VG2RBOA3YDIPORKTM")}),
							/*Object title format*/
							null,
							/*Class catalogs*/
							new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog[]{

									new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog(
									/*Radix::CfgManagement::CfgPacket.Export:General-Dynamic Class Catalog*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eccMTCC7TKG2ZFXHHV6DYJ2SE3M34"),org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.ELinkMode.VIRTUAL,new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.Item[]{
										new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.ClassRef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aclDITXIZNY6NBNROSDPK6PPO3RSM"),null,10.0,true,org.radixware.kernel.common.types.Id.Factory.loadFrom("aclDITXIZNY6NBNROSDPK6PPO3RSM"),null)}
									)
							},
							/*Restrictions*/
							null,null,0),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblWMN3P5J5ONCFPLJVGBFAKZXZXU"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecWMN3P5J5ONCFPLJVGBFAKZXZXU"),

						/*Radix::CfgManagement::CfgPacket.Export:Properties-Properties*/
						new org.radixware.kernel.server.meta.clazzes.RadPropDef[]{

								/*Radix::CfgManagement::CfgPacket.Export:preparedPkgStateChangedDoc-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdUOROBYWCXFFVPFXRDIGT72SX24"),"preparedPkgStateChangedDoc",null,org.radixware.kernel.common.enums.EValType.XML,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::CfgManagement::CfgPacket.Export:pkgState-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDKBRCEEYTFDBBAQ3ZE5GOMIXR4"),"pkgState",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZQETKL6GB5HEBHRTGQSJY5U5FE"),org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("acsKBHFAZGGC5AYTFAQNXXU4URWCE"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE)
						},

						/*Radix::CfgManagement::CfgPacket.Export:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthcmdNVFWLJH5ZNBSXBMYK26WA66QJM"),"onCommand_ToImport",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("newPropValHandlersById",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr2PDZF453NZA2DEH2F3P5OKHGT4"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth3HY2HRJ6BVCUTJUKAZD35HPMGQ"),"getDbUrl",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,null,org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthcmdAJI4BLGC7JFKVDPIFGGIVNHS4M"),"onCommand_Actualize",false,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("input",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprNWEA33KAAVGMXCTSRAOWSVJZLA")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("newPropValHandlersById",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprGPAYB5HLTZHYBEK6PEMALMA6TU"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthBNM53XYR2VHXDAPPS4E5GZWQAU"),"getOrCreateItem",false,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("data",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr4MQXHTBN6FECHE2Q7DQJFTTBTE")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("parent",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprAZ2M3EFCZVENTKA43ZYRSRSRFU")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("helper",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprYAZWRLW33JFQXC4H55EI2LCLYQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("settingsProvider",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprDUDHURQQXNDHPD3DSRMMBK2USI"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthcmdJPIUY2C3XZD3ZDAMIV4NECN5HI"),"onCommand_Export",false,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("input",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprLWJNAUJ4XJG5JCMTHZNNYVKI3Y")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("newPropValHandlersById",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprLTFL2IRADZEFHISTLIDINMBKNU"))
								},org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth43WN4YNM3BH5LGRZQ7OGQ2KZMA"),"export",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthWEAYZENWSZH3NLCAZAMDLI47FU"),"onItemsModified",false,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthD77F2HBL2FGSVPJHR3SEAMPWMA"),"afterInit",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("src",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprBRBX3O3RMBGUFD6DV6MBOE7S4E")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("phase",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprPWEOWQSSWVHRHJBBY3KCOAGIKA"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthPT5PMP2WVZGUDB7OTLOE2SBWKE"),"beforeUpdate",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthKNCPVZM6BJBS5PWZXWVAFDJKQM"),"storePkgStateChangelog",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,null,null)
						},
						null,
						null,null,null,false);
}

/* Radix::CfgManagement::CfgPacket.Export - Desktop Executable*/

/*Radix::CfgManagement::CfgPacket.Export-Application Class*/

package org.radixware.ads.CfgManagement.explorer;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgPacket.Export")
public interface CfgPacket.Export   extends org.radixware.ads.CfgManagement.explorer.CfgPacket  {

































































	public static class Actualize extends org.radixware.kernel.common.client.models.items.Command{
		protected Actualize(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public void send(org.radixware.schemas.types.MapStrStrDocument input ) throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,input);
			processResponse(response, null);
		}

	}

	public static class Export extends org.radixware.kernel.common.client.models.items.Command{
		protected Export(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public org.radixware.ads.CfgManagement.common.ImpExpXsd.PacketDocument send(org.radixware.schemas.types.StrDocument input ) throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,input);
			org.apache.xmlbeans.XmlObject xmlResponse = processResponse(response, null);
			return (org.radixware.ads.CfgManagement.common.ImpExpXsd.PacketDocument)org.radixware.kernel.common.utils.XmlObjectProcessor.castToXmlClass(getClass().getClassLoader(),xmlResponse,org.radixware.ads.CfgManagement.common.ImpExpXsd.PacketDocument.class);
		}

	}

	public static class ToImport extends org.radixware.kernel.common.client.models.items.Command{
		protected ToImport(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public void send() throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,null);
			processResponse(response, null);
		}

	}



}

/* Radix::CfgManagement::CfgPacket.Export - Desktop Meta*/

/*Radix::CfgManagement::CfgPacket.Export-Application Class*/

package org.radixware.ads.CfgManagement.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class CfgPacket.Export_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::CfgManagement::CfgPacket.Export:Presentations-Entity Object Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aclDITXIZNY6NBNROSDPK6PPO3RSM"),
			"Radix::CfgManagement::CfgPacket.Export",
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aecWMN3P5J5ONCFPLJVGBFAKZXZXU"),
			null,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOEAUSTY7NNDLVJIKFNG6DWLGDQ"),null,null,0,

			/*Radix::CfgManagement::CfgPacket.Export:Properties-Properties*/
			new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

					/*Radix::CfgManagement::CfgPacket.Export:pkgState:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDKBRCEEYTFDBBAQ3ZE5GOMIXR4"),
						"pkgState",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclDITXIZNY6NBNROSDPK6PPO3RSM"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						63,
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

						/*Radix::CfgManagement::CfgPacket.Export:pkgState:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acsKBHFAZGGC5AYTFAQNXXU4URWCE"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.NONE,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false)
			},
			new org.radixware.kernel.common.client.meta.RadCommandDef[]{
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::CfgManagement::CfgPacket.Export:ToImport-Object Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdNVFWLJH5ZNBSXBMYK26WA66QJM"),
						"ToImport",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclDITXIZNY6NBNROSDPK6PPO3RSM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUAWDVOPS4NF3ZJQSQZAKYT74VI"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("imgE5A5QE4ZHJF55BIWLBXLBDMSBU"),
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
					/*Radix::CfgManagement::CfgPacket.Export:Actualize-Object Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdAJI4BLGC7JFKVDPIFGGIVNHS4M"),
						"Actualize",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclDITXIZNY6NBNROSDPK6PPO3RSM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4TOEGSYWFNFEBL646XXSOAJPUI"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("imgFFKFYTGQMNG75FCVZSO3NP2H4I"),
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
					/*Radix::CfgManagement::CfgPacket.Export:Export-Object Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdJPIUY2C3XZD3ZDAMIV4NECN5HI"),
						"Export",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclDITXIZNY6NBNROSDPK6PPO3RSM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCDX7RWYI3FDZZH2J5XJQYKU4EI"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("imgVORNT5ZFORBEHLSYMJNUWJK6II"),
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
			null,
			null,
			new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr3OHL2CWN7VG2RBOA3YDIPORKTM")},
			true,false,false);
}

/* Radix::CfgManagement::CfgPacket.Export - Web Meta*/

/*Radix::CfgManagement::CfgPacket.Export-Application Class*/

package org.radixware.ads.CfgManagement.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class CfgPacket.Export_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::CfgManagement::CfgPacket.Export:Presentations-Entity Object Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aclDITXIZNY6NBNROSDPK6PPO3RSM"),
			"Radix::CfgManagement::CfgPacket.Export",
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aecWMN3P5J5ONCFPLJVGBFAKZXZXU"),
			null,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOEAUSTY7NNDLVJIKFNG6DWLGDQ"),null,null,0,
			null,
			null,
			null,
			null,
			null,
			null,
			false,false,false);
}

/* Radix::CfgManagement::CfgPacket.Export:General - Desktop Meta*/

/*Radix::CfgManagement::CfgPacket.Export:General-Editor Presentation*/

package org.radixware.ads.CfgManagement.explorer;
public final class General_mi{
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new org.radixware.kernel.common.client.meta.RadEditorPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epr3OHL2CWN7VG2RBOA3YDIPORKTM"),
	"General",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
	org.radixware.kernel.common.types.Id.Factory.loadFrom("eprJHOQCOZIOBBWNABEIAT5XEG7M4"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("aclDITXIZNY6NBNROSDPK6PPO3RSM"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("tblWMN3P5J5ONCFPLJVGBFAKZXZXU"),
	null,
	null,

	/*Radix::CfgManagement::CfgPacket.Export:General:Editor Pages-Editor Presentation Pages*/
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

			/*Radix::CfgManagement::CfgPacket.Export:General:Items-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadContainerEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgVLCTV3FR25BSDAHMLLZAH7ROAA"),"Items",null,null,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiNLU3HH57UBGH5DOHCRVHMFXF2M"))
	},
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgLB467MBHIRAKXEEF2YUVLYF5GI")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgVLCTV3FR25BSDAHMLLZAH7ROAA")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgROII5GGFWJGS3AHGCDQTDEQ3ME"))}
	,

	/*Radix::CfgManagement::CfgPacket.Export:General:Children-Explorer Items*/
		new org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemDef[]{

				/*Radix::CfgManagement::CfgPacket.Export:General:CfgItemExp-Child Ref Explorer Item*/

				new org.radixware.kernel.common.client.meta.explorerItems.RadChildRefExplorerItemDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiNLU3HH57UBGH5DOHCRVHMFXF2M"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aclDITXIZNY6NBNROSDPK6PPO3RSM"),null,
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ4WZRLJ23BGMBKQ2JBSOBEGBP4"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("spr4SSGPHYJVRAGLB7HGLSAGIAMIQ"),
					0,
					null,
					16560,false)
		}
	, new 
	org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[]{new org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings(org.radixware.kernel.common.types.Id.Factory.loadFrom("epr3OHL2CWN7VG2RBOA3YDIPORKTM"),
			new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiNLU3HH57UBGH5DOHCRVHMFXF2M"),org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiC3WF6WEXGZDUTAKXY4G3E4LNUE")},null)}
	,
	null,
	0,
	null,
	(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
	36016,0,0);
}
/* Radix::CfgManagement::CfgPacket.Export:General:Model - Desktop Executable*/

/*Radix::CfgManagement::CfgPacket.Export:General:Model-Entity Model Class*/

package org.radixware.ads.CfgManagement.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgPacket.Export:General:Model")
public class General:Model  extends org.radixware.ads.CfgManagement.explorer.CfgPacket.Export.CfgPacket.Export_DefaultModel.eprJHOQCOZIOBBWNABEIAT5XEG7M4_ModelAdapter {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return General:Model_mi.rdxMeta; }



	public General:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadEditorPresentationDef def){super(userSession,def);}

	/*Radix::CfgManagement::CfgPacket.Export:General:Model:Nested classes-Nested Classes*/

	/*Radix::CfgManagement::CfgPacket.Export:General:Model:Properties-Properties*/

	/*Radix::CfgManagement::CfgPacket.Export:General:Model:stateChangelog-Presentation Property*/




	public class StateChangelog extends org.radixware.ads.CfgManagement.explorer.CfgPacket.Export.CfgPacket.Export_DefaultModel.eprJHOQCOZIOBBWNABEIAT5XEG7M4_ModelAdapter.pruIV5OQZASNNB4XGWKPXUGHOHAAQ{
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

		/*Radix::CfgManagement::CfgPacket.Export:General:Model:stateChangelog:EmbeddedClass-Property Presentation Embedded Class*/




		/*Radix::CfgManagement::CfgPacket.Export:General:Model:stateChangelog:Nested classes-Nested Classes*/

		/*Radix::CfgManagement::CfgPacket.Export:General:Model:stateChangelog:Properties-Properties*/

		/*Radix::CfgManagement::CfgPacket.Export:General:Model:stateChangelog:Methods-Methods*/

		/*Radix::CfgManagement::CfgPacket.Export:General:Model:stateChangelog:isVisible-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgPacket.Export:General:Model:stateChangelog:isVisible")
		public published  boolean isVisible () {
			return super.isVisible() && !isNew();
		}













		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgPacket.Export:General:Model:stateChangelog")
		public final  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgPacket.Export:General:Model:stateChangelog")
		public final   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public StateChangelog getStateChangelog(){return (StateChangelog)getProperty(pruIV5OQZASNNB4XGWKPXUGHOHAAQ);}








	/*Radix::CfgManagement::CfgPacket.Export:General:Model:Methods-Methods*/

	/*Radix::CfgManagement::CfgPacket.Export:General:Model:onCommand_ToImport-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgPacket.Export:General:Model:onCommand_ToImport")
	protected  void onCommand_ToImport (org.radixware.ads.CfgManagement.explorer.CfgPacket.Export.ToImport command) {
		try {
		    command.send();
		    Explorer.Models::GroupModel groupModel = Explorer.Context::Utils.getOwnerGroup(this);
		    if (groupModel != null)
		        groupModel.reread();
		} catch (Exceptions::Exception e) {
		    Environment.processException(e);
		}

	}

	/*Radix::CfgManagement::CfgPacket.Export:General:Model:onCommand_Actualize-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgPacket.Export:General:Model:onCommand_Actualize")
	protected  void onCommand_Actualize (org.radixware.ads.CfgManagement.explorer.CfgPacket.Export.Actualize command) {
		try {
		    if (!DesktopUtils.checkAccessForModifyPacket(findNearestView(), pkgState.Value)) {
		        return;
		    }
		    
		    CfgItem.ChooseItemsDialog dialog = new CfgItem.ChooseItemsDialog(Environment);
		    dialog.Model.packetId = id.Value;
		    if (dialog.execDialog(findNearestView()) == Client.Views::DialogResult.ACCEPTED) {
		        Arte::TypesXsd:MapStrStrDocument doc = Arte::TypesXsd:MapStrStrDocument.Factory.newInstance();
		        doc.addNewMapStrStr();
		        
		        Arte::TypesXsd:MapStrStr.Entry xItemsIds = doc.MapStrStr.addNewEntry();
		        xItemsIds.Key = "ItemsIds";
		        xItemsIds.Value = dialog.Model.getSelectedIds().toString();
		        
		        final boolean createAbsentItems = dialog.Model.createAbsentItems.booleanValue();
		        Arte::TypesXsd:MapStrStr.Entry xAllowCreate = doc.MapStrStr.addNewEntry();
		        xAllowCreate.Key = "AllowCreate";
		        xAllowCreate.Value = Bool.toString(createAbsentItems);
		        
		        command.send(doc);
		        if (getView() != null)
		            getView().reread();
		    }
		} catch (Exceptions::InterruptedException e) {
		    showException(e);
		} catch (Exceptions::ServiceClientException e) {
		    showException(e);
		}
	}

	/*Radix::CfgManagement::CfgPacket.Export:General:Model:onCommand_Export-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgPacket.Export:General:Model:onCommand_Export")
	protected  void onCommand_Export (org.radixware.ads.CfgManagement.explorer.CfgPacket.Export.Export command) {
		java.io.File file = DesktopUtils.openFileForExport(findNearestView(), command.getTitle());
		if (file == null)
		    return;

		ImpExpXsd:PacketDocument doc = null;
		try {
		    Arte::TypesXsd:StrDocument input =  Arte::TypesXsd:StrDocument.Factory.newInstance();
		    input.Str = file.getName();
		    doc = command.send(input);
		    if (doc == null) {
		        return;
		    }
		} catch (Exceptions::InterruptedException e) {
		    showException(e);
		    return;
		} catch (Exceptions::ServiceClientException e) {
		    showException(e);
		    return;
		}

		try {
		    org.radixware.kernel.common.utils.XmlFormatter.save(doc, file);
		} catch (Exceptions::IOException e) {
		    showException(e);
		}


	}
	public final class Actualize extends org.radixware.ads.CfgManagement.explorer.CfgPacket.Export.Actualize{
		protected Actualize(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_Actualize( this );
		}

	}

	public final class Export extends org.radixware.ads.CfgManagement.explorer.CfgPacket.Export.Export{
		protected Export(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_Export( this );
		}

	}

	public final class ToImport extends org.radixware.ads.CfgManagement.explorer.CfgPacket.Export.ToImport{
		protected ToImport(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_ToImport( this );
		}

	}

















}

/* Radix::CfgManagement::CfgPacket.Export:General:Model - Desktop Meta*/

/*Radix::CfgManagement::CfgPacket.Export:General:Model-Entity Model Class*/

package org.radixware.ads.CfgManagement.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class General:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aem3OHL2CWN7VG2RBOA3YDIPORKTM"),
						"General:Model",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aemJHOQCOZIOBBWNABEIAT5XEG7M4"),
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::CfgManagement::CfgPacket.Export:General:Model:Properties-Properties*/
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

/* Radix::CfgManagement::CfgPacket.Export - Localizing Bundle */
package org.radixware.ads.CfgManagement.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class CfgPacket.Export - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Update");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Актуализировать");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4TOEGSYWFNFEBL646XXSOAJPUI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Export to File");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Экспорт в файл");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCDX7RWYI3FDZZH2J5XJQYKU4EI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Package exported to file \'%1\'");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Пакет экспортирован в файл \'%1\'");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOB723GWX7RGGTPZVZXUY5SHETQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.EVENT_CODE,org.radixware.kernel.common.enums.EEventSeverity.EVENT,"App",null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Export Configuration Package");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Пакет экспорта конфигурации");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOEAUSTY7NNDLVJIKFNG6DWLGDQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Package copied from package \'%1\'");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Пакет скопирован из пакета \'%1\'");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsSAMRVB6QKFCWDNOQPSWA2GIOZM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.EVENT_CODE,org.radixware.kernel.common.enums.EEventSeverity.EVENT,"App",null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Copy to Import Package");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Скопировать в пакет импорта");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUAWDVOPS4NF3ZJQSQZAKYT74VI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Package updated");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Пакет актуализирован");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWM652DACVJF7PKYG4EFLCVYNIE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.EVENT_CODE,org.radixware.kernel.common.enums.EEventSeverity.EVENT,"App",java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(CfgPacket.Export - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbaclDITXIZNY6NBNROSDPK6PPO3RSM"),"CfgPacket.Export - Localizing Bundle",$$$items$$$);
}
