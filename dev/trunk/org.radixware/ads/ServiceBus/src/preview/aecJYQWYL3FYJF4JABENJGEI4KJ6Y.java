
/* Radix::ServiceBus::DataScheme - Server Executable*/

/*Radix::ServiceBus::DataScheme-Entity Class*/

package org.radixware.ads.ServiceBus.server;

import java.io.Writer;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::DataScheme")
public final published class DataScheme  extends org.radixware.ads.Types.server.Entity  implements org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {


	/**Metainformation accessor method*/
	@SuppressWarnings("unused")
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return DataScheme_mi.rdxMeta;}

	/*Radix::ServiceBus::DataScheme:Nested classes-Nested Classes*/

	/*Radix::ServiceBus::DataScheme:Properties-Properties*/

	/*Radix::ServiceBus::DataScheme:description-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::DataScheme:description")
	public published  Str getDescription() {
		return description;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::DataScheme:description")
	public published   void setDescription(Str val) {
		description = val;
	}

	/*Radix::ServiceBus::DataScheme:lastUpdateTime-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::DataScheme:lastUpdateTime")
	public published  java.sql.Timestamp getLastUpdateTime() {
		return lastUpdateTime;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::DataScheme:lastUpdateTime")
	public published   void setLastUpdateTime(java.sql.Timestamp val) {
		lastUpdateTime = val;
	}

	/*Radix::ServiceBus::DataScheme:lastUpdateUserName-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::DataScheme:lastUpdateUserName")
	public published  Str getLastUpdateUserName() {
		return lastUpdateUserName;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::DataScheme:lastUpdateUserName")
	public published   void setLastUpdateUserName(Str val) {
		lastUpdateUserName = val;
	}

	/*Radix::ServiceBus::DataScheme:scheme-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::DataScheme:scheme")
	public published  java.sql.Clob getScheme() {
		return scheme;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::DataScheme:scheme")
	public published   void setScheme(java.sql.Clob val) {
		scheme = val;
	}

	/*Radix::ServiceBus::DataScheme:title-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::DataScheme:title")
	public published  Str getTitle() {
		return title;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::DataScheme:title")
	public published   void setTitle(Str val) {
		title = val;
	}

	/*Radix::ServiceBus::DataScheme:type-Column-Based Property*/








			















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::DataScheme:type")
	public published  org.radixware.ads.System.common.DataSchemeType getType() {
		return type;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::DataScheme:type")
	public published   void setType(org.radixware.ads.System.common.DataSchemeType val) {
		type = val;
	}

	/*Radix::ServiceBus::DataScheme:uri-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::DataScheme:uri")
	public published  Str getUri() {
		return uri;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::DataScheme:uri")
	public published   void setUri(Str val) {
		uri = val;
	}

	/*Radix::ServiceBus::DataScheme:schemeTitle-Dynamic Property*/



	protected Str schemeTitle=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::DataScheme:schemeTitle")
	public published  Str getSchemeTitle() {
		return schemeTitle;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::DataScheme:schemeTitle")
	public published   void setSchemeTitle(Str val) {
		schemeTitle = val;
	}

	/*Radix::ServiceBus::DataScheme:caching-Dynamic Property*/



	protected org.radixware.ads.Utils.server.Caching caching=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::DataScheme:caching")
	private final  org.radixware.ads.Utils.server.Caching getCaching() {

		if (internal[caching] == null) {
		    internal[caching] = new Caching(this, Common::CachingPeriod:RecheckPeriod.getValue().intValue(), -1);
		} 
		return internal[caching];
	}

	/*Radix::ServiceBus::DataScheme:rootMsdlScheme-Dynamic Property*/



	protected org.radixware.kernel.common.msdl.RootMsdlScheme rootMsdlScheme=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::DataScheme:rootMsdlScheme")
	public published  org.radixware.kernel.common.msdl.RootMsdlScheme getRootMsdlScheme() {

		if (internal[rootMsdlScheme] == null) {
		    if (this.type == System::DataSchemeType:MSDL && scheme != null)
		        try {
		            final Meta::MsdlXsd:MessageElementDocument me = Meta::MsdlXsd:MessageElementDocument.Factory.parse(scheme.getCharacterStream());
		            internal[rootMsdlScheme] = new org.radixware.kernel.common.msdl.RootMsdlScheme(me.getMessageElement());
		        } catch (Exceptions::Exception e) {
		            internal[rootMsdlScheme] = null;
		        }
		}
		return internal[rootMsdlScheme];
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::DataScheme:rootMsdlScheme")
	public published   void setRootMsdlScheme(org.radixware.kernel.common.msdl.RootMsdlScheme val) {

		internal[rootMsdlScheme] = null;
	}

	/*Radix::ServiceBus::DataScheme:lastUpdateTimeGetter-Dynamic Property*/



	protected org.radixware.ads.Utils.server.ILastUpdateTimeGetter lastUpdateTimeGetter=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::DataScheme:lastUpdateTimeGetter")
	private final  org.radixware.ads.Utils.server.ILastUpdateTimeGetter getLastUpdateTimeGetter() {

		if (internal[lastUpdateTimeGetter] == null) {

		    internal[lastUpdateTimeGetter] = new Utils::ILastUpdateTimeGetter() {

		        public DateTime getLastUpdateTime() {
		            GetDataSchemeLastUpdateTime cursor = GetDataSchemeLastUpdateTime.open(uri);
		            try {
		                if (cursor.next()) {
		                    return cursor.lastUpdateTime;
		                } else {
		                    return null;
		                }
		            } finally {
		                cursor.close();
		            }
		        }
		    };
		}
		return internal[lastUpdateTimeGetter];
	}























































































	/*Radix::ServiceBus::DataScheme:Methods-Methods*/

	/*Radix::ServiceBus::DataScheme:beforeUpdate-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::DataScheme:beforeUpdate")
	protected published  boolean beforeUpdate () {
		lastUpdateTime = new DateTime(System.currentTimeMillis());
		lastUpdateUserName = Arte.UserName;
		return super.beforeUpdate();
	}

	/*Radix::ServiceBus::DataScheme:beforeCreate-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::DataScheme:beforeCreate")
	protected published  boolean beforeCreate (org.radixware.kernel.server.types.Entity src) {
		lastUpdateTime = new DateTime(System.currentTimeMillis());
		lastUpdateUserName = Arte.UserName;
		return super.beforeCreate(src);
	}

	/*Radix::ServiceBus::DataScheme:loadByPidStr-System Method*/

	@SuppressWarnings("unused")
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::DataScheme:loadByPidStr")
	public static published  org.radixware.ads.ServiceBus.server.DataScheme loadByPidStr (Str pidAsStr, boolean checkExistance) {
	org.radixware.kernel.server.types.Pid pid = new org.radixware.kernel.server.types.Pid(org.radixware.ads.mdlPEKYFVDRVZHGZCBQQDY2NOYFOY.server.pdcArte______________________.__getArteInstanceInternal(),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblJYQWYL3FYJF4JABENJGEI4KJ6Y"),pidAsStr);
		try{
		return (
		org.radixware.ads.ServiceBus.server.DataScheme) org.radixware.ads.mdlPEKYFVDRVZHGZCBQQDY2NOYFOY.server.pdcArte______________________.__getArteInstanceInternal().getEntityObject(pid,null,checkExistance);
	}catch(org.radixware.kernel.server.exceptions.EntityObjectNotExistsError e){
	return null;
	}

	}

	/*Radix::ServiceBus::DataScheme:loadByPK-System Method*/

	@SuppressWarnings("unused")
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::DataScheme:loadByPK")
	public static published  org.radixware.ads.ServiceBus.server.DataScheme loadByPK (Str uri, boolean checkExistance) {
	final java.util.HashMap<org.radixware.kernel.common.types.Id,Object> pkValsMap = new java.util.HashMap<org.radixware.kernel.common.types.Id,Object>(5);
			if(uri==null) return null;
			pkValsMap.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("colR7TMX2J72ZDF7PJMLUDXHHWNV4"),uri);
	org.radixware.kernel.server.types.Pid pid = new org.radixware.kernel.server.types.Pid(org.radixware.ads.mdlPEKYFVDRVZHGZCBQQDY2NOYFOY.server.pdcArte______________________.__getArteInstanceInternal(),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblJYQWYL3FYJF4JABENJGEI4KJ6Y"),pkValsMap);
		try{
		return (
		org.radixware.ads.ServiceBus.server.DataScheme) org.radixware.ads.mdlPEKYFVDRVZHGZCBQQDY2NOYFOY.server.pdcArte______________________.__getArteInstanceInternal().getEntityObject(pid,null,checkExistance);
	}catch(org.radixware.kernel.server.exceptions.EntityObjectNotExistsError e){
	return null;
	}

	}

	/*Radix::ServiceBus::DataScheme:getScheme-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::DataScheme:getScheme")
	public published  org.apache.xmlbeans.XmlObject getScheme () {
		if (scheme == null)
		    return null;

		try {
		    return Xml.Factory.parse(scheme.getCharacterStream());
		} catch (Exception e) {
		    throw new DatabaseError("Can't read scheme: " + e.getClass().getName() + (e.getMessage() != null ? ":" + e.getMessage() : ""), e);
		}

	}

	/*Radix::ServiceBus::DataScheme:setScheme-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::DataScheme:setScheme")
	public published  void setScheme (org.apache.xmlbeans.XmlObject scheme) {
		if (scheme != null)
		    try {
		        scheme = Arte::Arte.getDbConnection().createClob();
		        final Writer writer = scheme.setCharacterStream(1L);
		        scheme.save(writer); 
		        writer.close();
		    } catch (Exception e) {
		        throw new DatabaseError("Can't write scheme: " + e.getClass().getName() + (e.getMessage() != null ? ":" + e.getMessage() : ""), e);
		    }
		else
		    scheme = null;
	}

	/*Radix::ServiceBus::DataScheme:refreshCache-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::DataScheme:refreshCache")
	public published  boolean refreshCache () {
		boolean needRefresh = caching.refresh(lastUpdateTimeGetter);
		if (needRefresh)
		    rootMsdlScheme = null;
		return needRefresh;
	}

	/*Radix::ServiceBus::DataScheme:exportThis-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::DataScheme:exportThis")
	  void exportThis (org.radixware.ads.CfgManagement.server.CfgExportData data) {
		data.itemClassId = idof[CfgItem.DataSchemeSingle];
		data.object = this;
		ImpExpXsd:DataSchemeDocument s = exportToXml();
		data.data = s;
		data.fileContent = s;

	}

	/*Radix::ServiceBus::DataScheme:exportAll-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::DataScheme:exportAll")
	 static  void exportAll (org.radixware.ads.CfgManagement.server.CfgExportData data, java.util.Iterator<org.radixware.ads.ServiceBus.server.DataScheme> iter) {
		ImpExpXsd:DataSchemeGroupDocument groupDoc = ImpExpXsd:DataSchemeGroupDocument.Factory.newInstance();
		groupDoc.addNewDataSchemeGroup();

		if (iter == null) {
		    ServiceBus.Db::DataSchemeCursor c = ServiceBus.Db::DataSchemeCursor.open();
		    iter = new EntityCursorIterator(c, idof[ServiceBus.Db::DataSchemeCursor:scheme]);
		}


		try {
		    while (iter.hasNext()) {
		        DataScheme ds = iter.next();
		        ImpExpXsd:DataSchemeDocument singleDoc = ds.exportToXml();
		        data.children.add(new CfgExportData(ds, null, idof[CfgItem.DataSchemeSingle], singleDoc));
		        groupDoc.DataSchemeGroup.addNewItem().set(singleDoc.DataScheme);
		    }
		} finally {
		    EntityCursorIterator.closeIterator(iter);
		}

		data.itemClassId = idof[CfgItem.DataSchemeGroup];
		data.object = null;
		data.data = null;
		data.fileContent = groupDoc;
	}

	/*Radix::ServiceBus::DataScheme:exportToXml-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::DataScheme:exportToXml")
	private final  org.radixware.ads.ServiceBus.common.ImpExpXsd.DataSchemeDocument exportToXml () {
		ImpExpXsd:DataSchemeDocument doc = ImpExpXsd:DataSchemeDocument.Factory.newInstance();
		doc.addNewDataScheme();
		doc.DataScheme.Uri = uri;
		doc.DataScheme.Type = type;
		doc.DataScheme.Title = title;
		doc.DataScheme.Description = description;
		doc.DataScheme.Scheme = getScheme();

		return doc;
	}

	/*Radix::ServiceBus::DataScheme:importAll-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::DataScheme:importAll")
	 static  void importAll (org.radixware.ads.ServiceBus.common.ImpExpXsd.DataSchemeGroup xml, org.radixware.ads.CfgManagement.server.ICfgImportHelper helper) {
		for (ImpExpXsd:DataScheme s : xml.ItemList) {
		    importOne(s, helper);
		    if (helper.wasCancelled())
		        break;
		}

	}

	/*Radix::ServiceBus::DataScheme:importOne-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::DataScheme:importOne")
	 static  org.radixware.ads.ServiceBus.server.DataScheme importOne (org.radixware.ads.ServiceBus.common.ImpExpXsd.DataScheme xml, org.radixware.ads.CfgManagement.server.ICfgImportHelper helper) {
		if (xml == null)
		    return null;

		Str uri = xml.Uri;
		DataScheme obj = DataScheme.loadByPK(uri, true);
		if (obj == null) {
		    obj = create(uri);
		    obj.importThis(xml, helper);
		} else
		    switch (helper.getActionIfObjExists(obj)) {
		        case UPDATE:
		            obj.importThis(xml, helper);
		            break;
		        case NEW:
		            throw new AppError("Not supported");
		        case CANCELL:
		            helper.reportObjectCancelled(obj);
		            break;
		    }
		return obj;
	}

	/*Radix::ServiceBus::DataScheme:importThis-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::DataScheme:importThis")
	  void importThis (org.radixware.ads.ServiceBus.common.ImpExpXsd.DataScheme xml, org.radixware.ads.CfgManagement.server.ICfgImportHelper helper) {
		uri = xml.Uri;
		type = xml.Type;
		title = xml.Title;
		description = xml.Description;
		setScheme(xml.Scheme);

		helper.createOrUpdateAndReport(this);

	}

	/*Radix::ServiceBus::DataScheme:create-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::DataScheme:create")
	 static  org.radixware.ads.ServiceBus.server.DataScheme create (Str uri) {
		DataScheme obj = new DataScheme();
		obj.init();
		obj.uri = uri;
		return obj;
	}

	/*Radix::ServiceBus::DataScheme:updateFromCfgItem-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::DataScheme:updateFromCfgItem")
	  void updateFromCfgItem (org.radixware.ads.ServiceBus.server.CfgItem.DataSchemeSingle cfg, org.radixware.ads.CfgManagement.server.ICfgImportHelper helper) {
		importThis(cfg.myData.DataScheme, helper);


	}


	@Override
	public org.radixware.kernel.server.types.FormHandler.NextDialogsRequest execCommand(org.radixware.kernel.common.types.Id cmdId, org.radixware.kernel.common.types.Id propId, org.apache.xmlbeans.XmlObject input, org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValsById, org.apache.xmlbeans.XmlObject output) throws org.radixware.kernel.common.exceptions.AppException,java.lang.InterruptedException{

			return super.execCommand(cmdId,propId,input,newPropValsById,output);
	}


}

/* Radix::ServiceBus::DataScheme - Server Meta*/

/*Radix::ServiceBus::DataScheme-Entity Class*/

package org.radixware.ads.ServiceBus.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class DataScheme_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJYQWYL3FYJF4JABENJGEI4KJ6Y"),"DataScheme",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVS5HGR73AJGRDLRJ4SAG5PAYGI"),

						org.radixware.kernel.common.enums.EClassType.ENTITY,

						/*Radix::ServiceBus::DataScheme:Presentations-Entity Presentations*/
						/*Class presentation attributes*/
						 new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef(/*Owner Class Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJYQWYL3FYJF4JABENJGEI4KJ6Y"),
							/*Owner Class Name*/
							"DataScheme",
							/*Title Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVS5HGR73AJGRDLRJ4SAG5PAYGI"),
							/*Property presentations*/

							/*Radix::ServiceBus::DataScheme:Properties-Properties*/
							new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef[]{

									/*Radix::ServiceBus::DataScheme:description:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHG7MW2OSSZH2RBSTPLVOEKOQ2Q"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::ServiceBus::DataScheme:lastUpdateTime:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDUMEG6G7C5GG7CS7ULGQ4LV2ZY"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::ServiceBus::DataScheme:lastUpdateUserName:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJT4X3PEPLJHSDL4EXCGTM3OW7I"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::ServiceBus::DataScheme:scheme:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colFDCJLG47J5G6FAJTBU6ML7FSKI"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::ServiceBus::DataScheme:title:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGAU6BWQLNBEUXIFQONDJFEFYUY"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::ServiceBus::DataScheme:type:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colBQYGLCK26FB55JONW3UQQCL3IU"),org.radixware.kernel.common.enums.EEditPossibility.ON_CREATE,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::ServiceBus::DataScheme:uri:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colR7TMX2J72ZDF7PJMLUDXHHWNV4"),org.radixware.kernel.common.enums.EEditPossibility.ON_CREATE,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::ServiceBus::DataScheme:schemeTitle:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdK7VYRSQHC5BK7K6MERES43FPZM"),org.radixware.kernel.common.enums.EEditPossibility.PROGRAMMATICALLY,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT))
							},
							/*Commands*/
							new org.radixware.kernel.server.meta.presentations.RadCommandDef[]{

									new org.radixware.kernel.server.meta.presentations.RadCommandDef(
									/*Radix::ServiceBus::DataScheme:EditScheme-Property Command*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdIY7QJGQAPFG6TGJDUKUFQ4NSAA"),"EditScheme",org.radixware.kernel.common.enums.ECommandScope.PROPERTY,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("prdK7VYRSQHC5BK7K6MERES43FPZM")},org.radixware.kernel.common.enums.ECommandAccessibility.ALWAYS,org.radixware.kernel.common.enums.ECommandNature.LOCAL,org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJYQWYL3FYJF4JABENJGEI4KJ6Y"),false,true),

									new org.radixware.kernel.server.meta.presentations.RadCommandDef(
									/*Radix::ServiceBus::DataScheme:Export-Object Command*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdZECD22SIMJBULB74QWOF5H5IAM"),"Export",org.radixware.kernel.common.enums.ECommandScope.OBJECT,null,org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_FIXED,org.radixware.kernel.common.enums.ECommandNature.FORM_IN_OUT,org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJYQWYL3FYJF4JABENJGEI4KJ6Y"),false,true)
							},
							/*Sortings*/
							new org.radixware.kernel.server.meta.presentations.RadSortingDef[]{

									new org.radixware.kernel.server.meta.presentations.RadSortingDef(
									/*Radix::ServiceBus::DataScheme:Uri-Sorting*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("srt5QFVVERYFJAEXFKB7W75K6BKGE"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJYQWYL3FYJF4JABENJGEI4KJ6Y"),"Uri",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsX7DRHAXISJGFXPO27S7NFI6WGY"),new org.radixware.kernel.server.meta.presentations.RadSortingDef.Item[]{

											new org.radixware.kernel.server.meta.presentations.RadSortingDef.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("colR7TMX2J72ZDF7PJMLUDXHHWNV4"),org.radixware.kernel.common.enums.EOrder.ASC)
									},"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware")
							},
							/*Filters*/
							null,
							/*Editor presentations*/
							new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef[]{

									new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef(
									/*Radix::ServiceBus::DataScheme:Edit-Editor Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprF2IILUGBVBEBRKKDS2IW3Y4UWE"),
									"Edit",
									org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
									null,
									2192,
									null,

									/*Radix::ServiceBus::DataScheme:Edit:Children-Explorer Items*/
										null
									,
									org.radixware.kernel.server.types.Restrictions.Factory.newInstance(0,null,null,null),
									(org.radixware.kernel.server.meta.presentations.RadPropertyPresentationAttributes[]) null,
									org.radixware.kernel.common.enums.EEditorPresentationRightsInheritanceMode.NONE,
									null,null),

									new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef(
									/*Radix::ServiceBus::DataScheme:Create-Editor Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprUGQXRXITHBB3FFAKWZOYRBBDXA"),
									"Create",
									org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
									null,
									144,

									/*Radix::ServiceBus::DataScheme:Create:TitleFormat-Object Title Format*/

									new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJYQWYL3FYJF4JABENJGEI4KJ6Y"),new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.TitleItem[]{

											new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.TitleItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJYQWYL3FYJF4JABENJGEI4KJ6Y"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colGAU6BWQLNBEUXIFQONDJFEFYUY"),"{0} - ",org.radixware.kernel.common.enums.ETitleNullFormat.SAME_AS_NOT_NULL,null),

											new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.TitleItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJYQWYL3FYJF4JABENJGEI4KJ6Y"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colR7TMX2J72ZDF7PJMLUDXHHWNV4"),"{0}",org.radixware.kernel.common.enums.ETitleNullFormat.SAME_AS_NOT_NULL,null)
									},null,org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.DefinitionContextType.EDITOR_PRESENTATION),

									/*Radix::ServiceBus::DataScheme:Create:Children-Explorer Items*/
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
									/*Radix::ServiceBus::DataScheme:General-Selector Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("sprGBFMVNOXZBEUDNQ37R7ODFPEYA"),"General",null,144,new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn[]{

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colR7TMX2J72ZDF7PJMLUDXHHWNV4"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colBQYGLCK26FB55JONW3UQQCL3IU"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGAU6BWQLNBEUXIFQONDJFEFYUY"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHG7MW2OSSZH2RBSTPLVOEKOQ2Q"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJT4X3PEPLJHSDL4EXCGTM3OW7I"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDUMEG6G7C5GG7CS7ULGQ4LV2ZY"),true)
									},new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.Addons(org.radixware.kernel.common.types.Id.Factory.loadFrom("srt5QFVVERYFJAEXFKB7W75K6BKGE"),true,null,true,null,true,null,false,true,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprF2IILUGBVBEBRKKDS2IW3Y4UWE")},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprUGQXRXITHBB3FFAKWZOYRBBDXA")},org.radixware.kernel.server.types.Restrictions.Factory.newInstance(16416,null,null,null),null)
							},
							/*Default selector presentation Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("sprGBFMVNOXZBEUDNQ37R7ODFPEYA"),
							/*Presentation Map*/
							null,
							/*Object title format*/

							/*Radix::ServiceBus::DataScheme:TitleFormat-Object Title Format*/

							new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJYQWYL3FYJF4JABENJGEI4KJ6Y"),new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.TitleItem[]{

									new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.TitleItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJYQWYL3FYJF4JABENJGEI4KJ6Y"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colGAU6BWQLNBEUXIFQONDJFEFYUY"),"{0} - ",org.radixware.kernel.common.enums.ETitleNullFormat.SAME_AS_NOT_NULL,null),

									new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.TitleItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJYQWYL3FYJF4JABENJGEI4KJ6Y"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colR7TMX2J72ZDF7PJMLUDXHHWNV4"),"{0}",org.radixware.kernel.common.enums.ETitleNullFormat.SAME_AS_NOT_NULL,null)
							},null,org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.DefinitionContextType.ENTITY),
							/*Class catalogs*/
							null,
							/*Restrictions*/
							org.radixware.kernel.server.types.Restrictions.Factory.newInstance(0,null,null,null),null,0),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblJYQWYL3FYJF4JABENJGEI4KJ6Y"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("pdcEntity____________________"),

						/*Radix::ServiceBus::DataScheme:Properties-Properties*/
						new org.radixware.kernel.server.meta.clazzes.RadPropDef[]{

								/*Radix::ServiceBus::DataScheme:description-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHG7MW2OSSZH2RBSTPLVOEKOQ2Q"),"description",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMO6ZWPN7OVFNNFLFN2E7Y3MSRA"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::ServiceBus::DataScheme:lastUpdateTime-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDUMEG6G7C5GG7CS7ULGQ4LV2ZY"),"lastUpdateTime",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWLFQD3RTWNAEXNCGGLL572TBPA"),org.radixware.kernel.common.enums.EValType.DATE_TIME,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::ServiceBus::DataScheme:lastUpdateUserName-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJT4X3PEPLJHSDL4EXCGTM3OW7I"),"lastUpdateUserName",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsL3T2LP4X2FBTPFFW52LQ3ORGEY"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::ServiceBus::DataScheme:scheme-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colFDCJLG47J5G6FAJTBU6ML7FSKI"),"scheme",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsO6G3W56KSBHODJTBCBEIMP2RCY"),org.radixware.kernel.common.enums.EValType.CLOB,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::ServiceBus::DataScheme:title-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGAU6BWQLNBEUXIFQONDJFEFYUY"),"title",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOKUYNW73T5CFFAKSR5IZT6F5FA"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::ServiceBus::DataScheme:type-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colBQYGLCK26FB55JONW3UQQCL3IU"),"type",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEK2E3CCD2FA45PSAHOVSMFAI2E"),org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("acs4IA2QHCBZRDDDIHZCZHABTQDH4"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::ServiceBus::DataScheme:uri-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colR7TMX2J72ZDF7PJMLUDXHHWNV4"),"uri",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4WXBVRNZYFAD5HO5BLXIUN2PV4"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::ServiceBus::DataScheme:schemeTitle-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdK7VYRSQHC5BK7K6MERES43FPZM"),"schemeTitle",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPVPRR7LMAFDLPEFQX6MPWWZIWE"),org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::ServiceBus::DataScheme:caching-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdXQHNDBXYEBF2TAEXJ4YGPZTXTI"),"caching",null,org.radixware.kernel.common.enums.EValType.USER_CLASS,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadAccessorProxy.R_ACCESSOR_INSTANCE),

								/*Radix::ServiceBus::DataScheme:rootMsdlScheme-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdE6WLFOTSUZEIDO26QGINR3COEM"),"rootMsdlScheme",null,org.radixware.kernel.common.enums.EValType.JAVA_CLASS,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::ServiceBus::DataScheme:lastUpdateTimeGetter-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdYLYU44VTX5CJ3PBRLUJ2YKNULQ"),"lastUpdateTimeGetter",null,org.radixware.kernel.common.enums.EValType.USER_CLASS,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadAccessorProxy.R_ACCESSOR_INSTANCE)
						},

						/*Radix::ServiceBus::DataScheme:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthPT5PMP2WVZGUDB7OTLOE2SBWKE"),"beforeUpdate",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthFMXUFM5J25E4TNM4PR73F6V3E4"),"beforeCreate",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("src",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprNEQGNULMEZE7HEOCF3HIS56RWE"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth_loadByPidStr_____________"),"loadByPidStr",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("pidAsStr",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprENNH2YRLYNAWVDTCVLSTKM77IA")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("checkExistance",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr4VRGROBSF5A6LEMFC2AQSJTGBQ"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth_loadByPK_________________"),"loadByPK",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("uri",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprDNYGOMCVZND6TNHD43YQ5OF5QQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("checkExistance",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprC5YQE254DFBOZNYBTWL5BVOL24"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthS6OIOY5CSBHBFE5PEKYSTKKLLM"),"getScheme",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthUCHEDB4ENZBXJKAO2MNWKVWEL4"),"setScheme",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("scheme",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprWYUF7W7G5JEU7BB4KML4JKXO54"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthMNT4VPKL3VDFNKE2C4SASYAYJ4"),"refreshCache",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthR36GKZVOXZBX7CZGPE2VVT3R5A"),"exportThis",false,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("data",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprSY7BLG7O3BHKBIUZS7UE5KVS3Y"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthNSGWX6JD6JHI5DOGKSRXTH6ZXU"),"exportAll",true,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("data",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprSY7BLG7O3BHKBIUZS7UE5KVS3Y")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("iter",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprRPHP7M4MCFEQLPS4TWA7CKLUFA"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthIORGHHPW2VFOVI24TPVJEJ3TCM"),"exportToXml",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,null,org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthMSBHZE7LNJCFXL7MA55NKMF574"),"importAll",true,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("xml",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr2RLOK42HYZCS3LYDTUKUPR2RD4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("helper",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr5AEE44BN5VCFBEDIUMN2FNENKA"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthZWX4QVYDTJHLPJFS2KEEKGYRWM"),"importOne",true,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("xml",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr2RLOK42HYZCS3LYDTUKUPR2RD4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("helper",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprUAHGASFHCNFIBNEQK3K2GZ7XLQ"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthTT7SKVDFCRH4RGBJIDLP3YVV2M"),"importThis",false,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("xml",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr2RLOK42HYZCS3LYDTUKUPR2RD4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("helper",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprPK55TF3CIRCULIX6OX7FD4N2LQ"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthXB3OMHFUSJAF5EINNAN6XEICJA"),"create",true,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("uri",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprRKD4DQJZ45BCBCWQDGOHYJNCVY"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthPL4FHDWK7FESBFXD6DOBZ25FMQ"),"updateFromCfgItem",false,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("cfg",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprVAMRIOYK4ZGAJIMTDCHQBGSUZ4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("helper",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr5IUSIB24WNFVZOBXWL2TL57JUQ"))
								},null)
						},
						null,
						org.radixware.kernel.common.enums.EAccessAreaType.NONE,null,null,false);
}

/* Radix::ServiceBus::DataScheme - Desktop Executable*/

/*Radix::ServiceBus::DataScheme-Entity Class*/

package org.radixware.ads.ServiceBus.explorer;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::DataScheme")
public interface DataScheme {
	/*Group model adapter class. This is the base class for 
	all of selector presentation models of this entity*/
	public static class DefaultGroupModel extends org.radixware.kernel.common.client.models.GroupModel{
		public DefaultGroupModel(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadSelectorPresentationDef def){super(userSession,def);}





		public org.radixware.ads.ServiceBus.explorer.DataScheme.DataScheme_DefaultModel getEntity(int i) throws InterruptedException, org.radixware.kernel.common.client.exceptions.BrokenEntityObjectException, org.radixware.kernel.common.exceptions.ServiceClientException { return (org.radixware.ads.ServiceBus.explorer.DataScheme.DataScheme_DefaultModel )  super.getEntity(i);}
	}






































































	/*Radix::ServiceBus::DataScheme:type:type-Presentation Property*/


	public class Type extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public Type(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public void setServerValue(org.radixware.kernel.common.client.models.items.properties.PropertyValue val){
			Object x = val.getValue();
			org.radixware.ads.System.common.DataSchemeType dummy = x == null ? null : (x instanceof org.radixware.ads.System.common.DataSchemeType ? (org.radixware.ads.System.common.DataSchemeType)x : org.radixware.ads.System.common.DataSchemeType.getForValue((Str)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.STR)));
			val.refineValue(dummy);
			super.setServerValue(val);
		}
		@Override
		public Class<org.radixware.ads.System.common.DataSchemeType> getValClass(){
			return org.radixware.ads.System.common.DataSchemeType.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.ads.System.common.DataSchemeType dummy = x == null ? null : (x instanceof org.radixware.ads.System.common.DataSchemeType ? (org.radixware.ads.System.common.DataSchemeType)x : org.radixware.ads.System.common.DataSchemeType.getForValue((Str)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.STR)));
			setValue(dummy);
		}





				














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::DataScheme:type:type")
		public  org.radixware.ads.System.common.DataSchemeType getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::DataScheme:type:type")
		public   void setValue(org.radixware.ads.System.common.DataSchemeType val) {
			Value = val;
		}
	}
	public Type getType();
	/*Radix::ServiceBus::DataScheme:lastUpdateTime:lastUpdateTime-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::DataScheme:lastUpdateTime:lastUpdateTime")
		public  java.sql.Timestamp getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::DataScheme:lastUpdateTime:lastUpdateTime")
		public   void setValue(java.sql.Timestamp val) {
			Value = val;
		}
	}
	public LastUpdateTime getLastUpdateTime();
	/*Radix::ServiceBus::DataScheme:scheme:scheme-Presentation Property*/


	public class Scheme extends org.radixware.kernel.common.client.models.items.properties.PropertyClob{
		public Scheme(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::DataScheme:scheme:scheme")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::DataScheme:scheme:scheme")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Scheme getScheme();
	/*Radix::ServiceBus::DataScheme:title:title-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::DataScheme:title:title")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::DataScheme:title:title")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Title getTitle();
	/*Radix::ServiceBus::DataScheme:description:description-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::DataScheme:description:description")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::DataScheme:description:description")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Description getDescription();
	/*Radix::ServiceBus::DataScheme:lastUpdateUserName:lastUpdateUserName-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::DataScheme:lastUpdateUserName:lastUpdateUserName")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::DataScheme:lastUpdateUserName:lastUpdateUserName")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public LastUpdateUserName getLastUpdateUserName();
	/*Radix::ServiceBus::DataScheme:uri:uri-Presentation Property*/


	public class Uri extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public Uri(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::DataScheme:uri:uri")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::DataScheme:uri:uri")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Uri getUri();
	/*Radix::ServiceBus::DataScheme:schemeTitle:schemeTitle-Presentation Property*/


	public class SchemeTitle extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public SchemeTitle(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::DataScheme:schemeTitle:schemeTitle")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::DataScheme:schemeTitle:schemeTitle")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public SchemeTitle getSchemeTitle();
	public static class EditScheme extends org.radixware.kernel.common.client.models.items.Command{
		protected EditScheme(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}

	}

	public static class Export extends org.radixware.kernel.common.client.models.items.Command{
		protected Export(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}

	}



}

/* Radix::ServiceBus::DataScheme - Desktop Meta*/

/*Radix::ServiceBus::DataScheme-Entity Class*/

package org.radixware.ads.ServiceBus.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class DataScheme_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::ServiceBus::DataScheme:Presentations-Entity Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJYQWYL3FYJF4JABENJGEI4KJ6Y"),
			"Radix::ServiceBus::DataScheme",
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("imgWE5TTBZUPJCCPDKJGRG2W2SZ3M"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("sprGBFMVNOXZBEUDNQ37R7ODFPEYA"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVS5HGR73AJGRDLRJ4SAG5PAYGI"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMWDTFDTGEZH2PN2QDUM3G2CAWY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVS5HGR73AJGRDLRJ4SAG5PAYGI"),0,

			/*Radix::ServiceBus::DataScheme:Properties-Properties*/
			new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

					/*Radix::ServiceBus::DataScheme:description:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHG7MW2OSSZH2RBSTPLVOEKOQ2Q"),
						"description",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMO6ZWPN7OVFNNFLFN2E7Y3MSRA"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJYQWYL3FYJF4JABENJGEI4KJ6Y"),
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

						/*Radix::ServiceBus::DataScheme:description:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::ServiceBus::DataScheme:lastUpdateTime:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDUMEG6G7C5GG7CS7ULGQ4LV2ZY"),
						"lastUpdateTime",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWLFQD3RTWNAEXNCGGLL572TBPA"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJYQWYL3FYJF4JABENJGEI4KJ6Y"),
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

						/*Radix::ServiceBus::DataScheme:lastUpdateTime:PropertyPresentation:Edit Options:-Edit Mask Date Time*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskDateTime(org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,null,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::ServiceBus::DataScheme:lastUpdateUserName:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJT4X3PEPLJHSDL4EXCGTM3OW7I"),
						"lastUpdateUserName",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsL3T2LP4X2FBTPFFW52LQ3ORGEY"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJYQWYL3FYJF4JABENJGEI4KJ6Y"),
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

						/*Radix::ServiceBus::DataScheme:lastUpdateUserName:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,250,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::ServiceBus::DataScheme:scheme:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colFDCJLG47J5G6FAJTBU6ML7FSKI"),
						"scheme",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsO6G3W56KSBHODJTBCBEIMP2RCY"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJYQWYL3FYJF4JABENJGEI4KJ6Y"),
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
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::ServiceBus::DataScheme:scheme:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::ServiceBus::DataScheme:title:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGAU6BWQLNBEUXIFQONDJFEFYUY"),
						"title",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOKUYNW73T5CFFAKSR5IZT6F5FA"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJYQWYL3FYJF4JABENJGEI4KJ6Y"),
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

						/*Radix::ServiceBus::DataScheme:title:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::ServiceBus::DataScheme:type:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colBQYGLCK26FB55JONW3UQQCL3IU"),
						"type",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEK2E3CCD2FA45PSAHOVSMFAI2E"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJYQWYL3FYJF4JABENJGEI4KJ6Y"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acs4IA2QHCBZRDDDIHZCZHABTQDH4"),
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

						/*Radix::ServiceBus::DataScheme:type:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acs4IA2QHCBZRDDDIHZCZHABTQDH4"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.NONE,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::ServiceBus::DataScheme:uri:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colR7TMX2J72ZDF7PJMLUDXHHWNV4"),
						"uri",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4WXBVRNZYFAD5HO5BLXIUN2PV4"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJYQWYL3FYJF4JABENJGEI4KJ6Y"),
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

						/*Radix::ServiceBus::DataScheme:uri:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::ServiceBus::DataScheme:schemeTitle:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdK7VYRSQHC5BK7K6MERES43FPZM"),
						"schemeTitle",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPVPRR7LMAFDLPEFQX6MPWWZIWE"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJYQWYL3FYJF4JABENJGEI4KJ6Y"),
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

						/*Radix::ServiceBus::DataScheme:schemeTitle:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false)
			},
			new org.radixware.kernel.common.client.meta.RadCommandDef[]{
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::ServiceBus::DataScheme:EditScheme-Property Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdIY7QJGQAPFG6TGJDUKUFQ4NSAA"),
						"EditScheme",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJYQWYL3FYJF4JABENJGEI4KJ6Y"),
						null,
						null,
						null,
						org.radixware.kernel.common.enums.ECommandNature.LOCAL,
						true,
						false,
						false,
						org.radixware.kernel.common.enums.ECommandScope.PROPERTY,
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("prdK7VYRSQHC5BK7K6MERES43FPZM")},
						org.radixware.kernel.common.enums.ECommandAccessibility.ALWAYS),
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::ServiceBus::DataScheme:Export-Object Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdZECD22SIMJBULB74QWOF5H5IAM"),
						"Export",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJYQWYL3FYJF4JABENJGEI4KJ6Y"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWK4CX4OI7FDTHHXDMF44AKH6YI"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("imgYGRKUM6D5NFB7HJSZFG3KGBC6M"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("afhV7MMQ6EBSBHPTOCASDIAYDNYDY"),
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
			new org.radixware.kernel.common.client.meta.RadSortingDef[]{

					new org.radixware.kernel.common.client.meta.RadSortingDef(
					/*Radix::ServiceBus::DataScheme:Uri-Sorting*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("srt5QFVVERYFJAEXFKB7W75K6BKGE"),
						"Uri",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJYQWYL3FYJF4JABENJGEI4KJ6Y"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsX7DRHAXISJGFXPO27S7NFI6WGY"),
						new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem[]{

								new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colR7TMX2J72ZDF7PJMLUDXHHWNV4"),
								false)
						})
			},
			new org.radixware.kernel.common.client.meta.RadReferenceDef[]{

					new org.radixware.kernel.common.client.meta.RadReferenceDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("refZQUKRWNNQNAKDB65OZX5L5AYME"),"DataScheme=>User (lastUpdateUserName=>name)",org.radixware.kernel.common.types.Id.Factory.loadFrom("tblJYQWYL3FYJF4JABENJGEI4KJ6Y"),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblSY4KIOLTGLNRDHRZABQAQH3XQ4"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colJT4X3PEPLJHSDL4EXCGTM3OW7I")},new String[]{"lastUpdateUserName"},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colEF6ODCYWY3NBDGMCABQAQH3XQ4")},new String[]{"name"})
			},
			new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprF2IILUGBVBEBRKKDS2IW3Y4UWE"),org.radixware.kernel.common.types.Id.Factory.loadFrom("eprUGQXRXITHBB3FFAKWZOYRBBDXA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("sprGBFMVNOXZBEUDNQ37R7ODFPEYA")},
			false,false,false);
}

/* Radix::ServiceBus::DataScheme - Web Meta*/

/*Radix::ServiceBus::DataScheme-Entity Class*/

package org.radixware.ads.ServiceBus.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class DataScheme_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::ServiceBus::DataScheme:Presentations-Entity Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJYQWYL3FYJF4JABENJGEI4KJ6Y"),
			"Radix::ServiceBus::DataScheme",
			null,
			null,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVS5HGR73AJGRDLRJ4SAG5PAYGI"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMWDTFDTGEZH2PN2QDUM3G2CAWY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVS5HGR73AJGRDLRJ4SAG5PAYGI"),0,
			null,
			null,
			null,
			null,
			null,
			null,
			false,false,false);
}

/* Radix::ServiceBus::DataScheme:Edit - Desktop Meta*/

/*Radix::ServiceBus::DataScheme:Edit-Editor Presentation*/

package org.radixware.ads.ServiceBus.explorer;
public final class Edit_mi{
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new org.radixware.kernel.common.client.meta.RadEditorPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprF2IILUGBVBEBRKKDS2IW3Y4UWE"),
	"Edit",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
	null,
	org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJYQWYL3FYJF4JABENJGEI4KJ6Y"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("tblJYQWYL3FYJF4JABENJGEI4KJ6Y"),
	null,
	null,

	/*Radix::ServiceBus::DataScheme:Edit:Editor Pages-Editor Presentation Pages*/
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

			/*Radix::ServiceBus::DataScheme:Edit:General-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgTXGXUMORNBB7TNNZAQKJMZKGCE"),"General",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJYQWYL3FYJF4JABENJGEI4KJ6Y"),null,null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colR7TMX2J72ZDF7PJMLUDXHHWNV4"),0,0,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colBQYGLCK26FB55JONW3UQQCL3IU"),0,1,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGAU6BWQLNBEUXIFQONDJFEFYUY"),0,2,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHG7MW2OSSZH2RBSTPLVOEKOQ2Q"),0,4,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDUMEG6G7C5GG7CS7ULGQ4LV2ZY"),0,5,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJT4X3PEPLJHSDL4EXCGTM3OW7I"),0,6,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdK7VYRSQHC5BK7K6MERES43FPZM"),0,3,1,false,false)
			},null)
	},
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgTXGXUMORNBB7TNNZAQKJMZKGCE"))}
	,

	/*Radix::ServiceBus::DataScheme:Edit:Children-Explorer Items*/
		null
	,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
	null,
	0,
	null,
	(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
	2192,0,0,null);
}
/* Radix::ServiceBus::DataScheme:Edit:Model - Desktop Executable*/

/*Radix::ServiceBus::DataScheme:Edit:Model-Entity Model Class*/

package org.radixware.ads.ServiceBus.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::DataScheme:Edit:Model")
public class Edit:Model  extends org.radixware.ads.ServiceBus.explorer.DataScheme.DataScheme_DefaultModel {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return Edit:Model_mi.rdxMeta; }



	public Edit:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadEditorPresentationDef def){super(userSession,def);}

	/*Radix::ServiceBus::DataScheme:Edit:Model:Nested classes-Nested Classes*/

	/*Radix::ServiceBus::DataScheme:Edit:Model:Properties-Properties*/

	/*Radix::ServiceBus::DataScheme:Edit:Model:schemeTitle-Presentation Property*/




	public class SchemeTitle extends org.radixware.ads.ServiceBus.explorer.DataScheme.prdK7VYRSQHC5BK7K6MERES43FPZM{
		public SchemeTitle(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::DataScheme:Edit:Model:schemeTitle")
		public  Str getValue() {

			if (scheme.Value != null)
			    return "<defined>";
			return null;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::DataScheme:Edit:Model:schemeTitle")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public SchemeTitle getSchemeTitle(){return (SchemeTitle)getProperty(prdK7VYRSQHC5BK7K6MERES43FPZM);}








	/*Radix::ServiceBus::DataScheme:Edit:Model:Methods-Methods*/

	/*Radix::ServiceBus::DataScheme:Edit:Model:onCommand_EditScheme-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::DataScheme:Edit:Model:onCommand_EditScheme")
	public  void onCommand_EditScheme (org.radixware.ads.ServiceBus.explorer.DataScheme.EditScheme command, org.radixware.kernel.common.types.Id propertyId) {
		if (type.Value == System::DataSchemeType:MSDL) {
		    if (Utils::SystemTools.isOSX){
		        getEnvironment().messageInformation("Unsupported Operation","MSDL-schema editing is not supported on OSX platform");
		        return;
		    }
		    final Meta::MsdlXsd:MessageElementDocument element;
		    try {
		        if (scheme.Value != null)
		            element = Meta::MsdlXsd:MessageElementDocument.Factory.parse(scheme.Value);
		        else {
		            element = Meta::MsdlXsd:MessageElementDocument.Factory.newInstance();
		            element.addNewMessageElement().addNewStructure();
		            element.MessageElement.ClassTargetNamespace = uri.Value;
		        }
		    } catch (Exceptions::XmlException e) {
		        Environment.processException(e);
		        return;
		    }
		    
		    final Explorer.Dialogs::MsdlEditor editor = new MsdlEditor(element.MessageElement);
		    editor.setSchemeSearcher(getEnvironment().getDefManager());
		    Explorer.Env::Application.showModalSwingDialog(editor);
		    if (editor.getMsdlSchema() != null) {
		        element.MessageElement = editor.getMsdlSchema();
		        scheme.Value = element.xmlText();
		        schemeTitle.afterModify();
		    }
		} else {
		    final Explorer.Dialogs::XmlEditorDialog xmlEditor = new XmlEditorDialog(Environment,(Explorer.Qt.Types::QWidget) getEntityView(), "Schema Editor");

		    org.apache.xmlbeans.XmlObject schema = org.apache.xmlbeans.XmlObject.Factory.newInstance();
		    if (scheme.Value != null)
		        try {
		            schema = org.apache.xmlbeans.XmlObject.Factory.parse(scheme.Value);
		        } catch (Exceptions::XmlException e) {
		            Environment.processException(e);
		            return;
		        }

		    schema = xmlEditor.edit(schema, (Types::Id) null, false);
		    if (schema != null) {
		        scheme.Value = schema.xmlText();
		        schemeTitle.afterModify();
		    }
		}
	}
	public final class EditScheme extends org.radixware.ads.ServiceBus.explorer.DataScheme.EditScheme{
		protected EditScheme(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_EditScheme( this, propertyId );
		}

	}













}

/* Radix::ServiceBus::DataScheme:Edit:Model - Desktop Meta*/

/*Radix::ServiceBus::DataScheme:Edit:Model-Entity Model Class*/

package org.radixware.ads.ServiceBus.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Edit:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aemF2IILUGBVBEBRKKDS2IW3Y4UWE"),
						"Edit:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::ServiceBus::DataScheme:Edit:Model:Properties-Properties*/
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

/* Radix::ServiceBus::DataScheme:Create - Desktop Meta*/

/*Radix::ServiceBus::DataScheme:Create-Editor Presentation*/

package org.radixware.ads.ServiceBus.explorer;
public final class Create_mi{
	private static final class Create_DEF extends org.radixware.kernel.common.client.meta.RadEditorPresentationDef{
		Create_DEF(){
			super(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprUGQXRXITHBB3FFAKWZOYRBBDXA"),
			"Create",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJYQWYL3FYJF4JABENJGEI4KJ6Y"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("tblJYQWYL3FYJF4JABENJGEI4KJ6Y"),
			null,
			null,

			/*Radix::ServiceBus::DataScheme:Create:Editor Pages-Editor Presentation Pages*/
			new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

					/*Radix::ServiceBus::DataScheme:Create:General-Editor Page*/

					 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgT6KRDV4XNNB7BHVM2QCN46LMOE"),"General",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJYQWYL3FYJF4JABENJGEI4KJ6Y"),null,null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colR7TMX2J72ZDF7PJMLUDXHHWNV4"),0,0,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGAU6BWQLNBEUXIFQONDJFEFYUY"),0,2,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHG7MW2OSSZH2RBSTPLVOEKOQ2Q"),0,3,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colBQYGLCK26FB55JONW3UQQCL3IU"),0,1,1,false,false)
					},null)
			},
			new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
				new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgT6KRDV4XNNB7BHVM2QCN46LMOE"))}
			,

			/*Radix::ServiceBus::DataScheme:Create:Children-Explorer Items*/
				null
			,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
			null,
			0,
			null,
			(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
			144,0,0,null);
		}
		@Override
		protected org.radixware.kernel.common.client.models.Model createModelImpl(org.radixware.kernel.common.client.IClientEnvironment userSession) {    return new org.radixware.ads.ServiceBus.explorer.DataScheme.DataScheme_DefaultModel(userSession,this);
		}
	}
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new Create_DEF(); 
;
}
/* Radix::ServiceBus::DataScheme:General - Desktop Meta*/

/*Radix::ServiceBus::DataScheme:General-Selector Presentation*/

package org.radixware.ads.ServiceBus.explorer;
public final class General_mi extends org.radixware.kernel.common.client.meta.RadSelectorPresentationDef{
	public static final org.radixware.kernel.common.client.meta.RadSelectorPresentationDef rdxMeta = new General_mi();
	private General_mi(){
		super(org.radixware.kernel.common.types.Id.Factory.loadFrom("sprGBFMVNOXZBEUDNQ37R7ODFPEYA"),
		"General",
		org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
		null,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJYQWYL3FYJF4JABENJGEI4KJ6Y"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("tblJYQWYL3FYJF4JABENJGEI4KJ6Y"),
		null,
		null,
		null,
		null,
		true,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("srt5QFVVERYFJAEXFKB7W75K6BKGE"),
		null,
		false,
		true,
		null,
		16416,
		null,
		144,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprUGQXRXITHBB3FFAKWZOYRBBDXA")},
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprF2IILUGBVBEBRKKDS2IW3Y4UWE")},
		false,true,false,0,0);
		columns = new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn[]{

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colR7TMX2J72ZDF7PJMLUDXHHWNV4"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colBQYGLCK26FB55JONW3UQQCL3IU"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGAU6BWQLNBEUXIFQONDJFEFYUY"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHG7MW2OSSZH2RBSTPLVOEKOQ2Q"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJT4X3PEPLJHSDL4EXCGTM3OW7I"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.RESIZE_BY_CONTENT,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDUMEG6G7C5GG7CS7ULGQ4LV2ZY"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.RESIZE_BY_CONTENT,null)
		};
	}
}
/* Radix::ServiceBus::DataScheme:General:Model - Desktop Executable*/

/*Radix::ServiceBus::DataScheme:General:Model-Group Model Class*/

package org.radixware.ads.ServiceBus.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::DataScheme:General:Model")
public class General:Model  extends org.radixware.ads.ServiceBus.explorer.DataScheme.DefaultGroupModel {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return General:Model_mi.rdxMeta; }



	public General:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadSelectorPresentationDef def){super(userSession,def);}

	/*Radix::ServiceBus::DataScheme:General:Model:Nested classes-Nested Classes*/

	/*Radix::ServiceBus::DataScheme:General:Model:Properties-Properties*/

	/*Radix::ServiceBus::DataScheme:General:Model:Methods-Methods*/

	/*Radix::ServiceBus::DataScheme:General:Model:onCommand_Import-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::DataScheme:General:Model:onCommand_Import")
	public  void onCommand_Import (org.radixware.ads.ServiceBus.explorer.DataSchemeGroup.Import command) {
		java.io.File file = CfgManagement::DesktopUtils.openFileForImport(findNearestView(), command.getTitle());
		if (file == null)
		    return;

		try {
		    ImpExpXsd:DataSchemeGroupDocument xDoc = parseDataSchemes(file);
		    if (xDoc == null) {
		        return;
		    }
		    Common.Dlg::ClientUtils.viewImportResult(command.send(xDoc));
		    reread();
		} catch (Exceptions::XmlException | Exceptions::ServiceClientException | Exceptions::IOException e) {
		    showException(e);
		} catch (Exceptions::InterruptedException e) {
		}
	}

	/*Radix::ServiceBus::DataScheme:General:Model:parseDataSchemes-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::DataScheme:General:Model:parseDataSchemes")
	private final  org.radixware.ads.ServiceBus.common.ImpExpXsd.DataSchemeGroupDocument parseDataSchemes (java.io.File file) throws java.io.IOException,org.apache.xmlbeans.XmlException {
		ImpExpXsd:DataSchemeGroupDocument xDoc = null;
		try {
		    return ImpExpXsd:DataSchemeGroupDocument.Factory.parse(file);
		} catch (Exceptions::XmlException ex) {
		//try parse next type
		}

		try {
		    ImpExpXsd:DataSchemeDocument x = ImpExpXsd:DataSchemeDocument.Factory.parse(file);
		    xDoc = ImpExpXsd:DataSchemeGroupDocument.Factory.newInstance();
		    xDoc.addNewDataSchemeGroup().ItemList.add(x.DataScheme);
		    return xDoc;
		} catch (Exceptions::XmlException ex) {
		//try parse next type
		}

		Acs::ImpExpCoreXsd:MsdlSchemesDocument xSchemes = Acs::ImpExpCoreXsd:MsdlSchemesDocument.Factory.parse(file);
		if (xSchemes.getMsdlSchemes() != null) {
		    xDoc = ImpExpXsd:DataSchemeGroupDocument.Factory.newInstance();
		    xDoc.addNewDataSchemeGroup();
		    for (Acs::ImpExpCoreXsd:UserDefinedDefs.UserDefinedDef xDef : xSchemes.MsdlSchemes.UserDefinedDefList) {
		        if (xDef.Definition.isSetAdsMsdlSchemeDefinition()) {
		            final ImpExpXsd:DataScheme xDs = xDoc.DataSchemeGroup.addNewItem();
		            xDs.Type = System::DataSchemeType:MSDL;
		            xDs.Description = xDef.Description;
		            xDs.Title = xDef.Title;
		            xDs.Uri = xDef.Definition.AdsMsdlSchemeDefinition.TargetNamespace;
		            final Meta::MsdlXsd:MessageElementDocument xMess = Meta::MsdlXsd:MessageElementDocument.Factory.newInstance();
		            xMess.setMessageElement(xDef.Definition.AdsMsdlSchemeDefinition.MessageElement);
		            xDs.addNewScheme().set(xMess);
		        }
		    }
		    return xDoc;
		}

		return null;
	}
	public final class Import extends org.radixware.ads.ServiceBus.explorer.DataSchemeGroup.Import{
		protected Import(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_Import( this );
		}

	}













}

/* Radix::ServiceBus::DataScheme:General:Model - Desktop Meta*/

/*Radix::ServiceBus::DataScheme:General:Model-Group Model Class*/

package org.radixware.ads.ServiceBus.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class General:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("agmGBFMVNOXZBEUDNQ37R7ODFPEYA"),
						"General:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::ServiceBus::DataScheme:General:Model:Properties-Properties*/
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

/* Radix::ServiceBus::DataScheme - Localizing Bundle */
package org.radixware.ads.ServiceBus.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class DataScheme - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"URI");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"URI");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4WXBVRNZYFAD5HO5BLXIUN2PV4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Unsupported Operation");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Операция не поддерживается");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5TSZCIR4P5GVVHBYCTCNKYGBJI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<defined>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<задано>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsD35LSN5AOZALTI4E4F6SVSPWWU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Type");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Тип");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEK2E3CCD2FA45PSAHOVSMFAI2E"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Data Schemas");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Схемы данных");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEYIH3YU5GRBG5M4KANWKIGQFBE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Last updated by");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Последний модифицировавший пользователь");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsL3T2LP4X2FBTPFFW52LQ3ORGEY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Description");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Описание");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMO6ZWPN7OVFNNFLFN2E7Y3MSRA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Data Schemas");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Схемы данных");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMWDTFDTGEZH2PN2QDUM3G2CAWY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Schema");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Схема");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsO6G3W56KSBHODJTBCBEIMP2RCY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Name");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Название");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOKUYNW73T5CFFAKSR5IZT6F5FA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Schema");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Схема");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPVPRR7LMAFDLPEFQX6MPWWZIWE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Data Schemas");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Схемы данных");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUP2TWURQPVHF7CCBB4FMRWT6BA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Data Schema");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Схема данных");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVS5HGR73AJGRDLRJ4SAG5PAYGI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Export");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Экспортировать схему");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWK4CX4OI7FDTHHXDMF44AKH6YI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Last updated");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Время последнего обновления");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWLFQD3RTWNAEXNCGGLL572TBPA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"By URI");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"По URI");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsX7DRHAXISJGFXPO27S7NFI6WGY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"MSDL-schema editing is not supported on OSX platform");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Редактирование MSDL-схемы на платформе OSX не поддерживается.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYQGDKWVFAFBQNK3AVBNLWPX5JE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Data Schemas");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Схемы данных");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYZM2ZNTFERCWZEPX2NPWD5TUVU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Schema Editor");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Редактор схемы");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZYVSISWTQVFYTJPLBUNZ7OACLQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(DataScheme - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbaecJYQWYL3FYJF4JABENJGEI4KJ6Y"),"DataScheme - Localizing Bundle",$$$items$$$);
}
