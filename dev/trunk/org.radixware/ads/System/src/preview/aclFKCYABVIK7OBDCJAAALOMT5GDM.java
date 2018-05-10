
/* Radix::System::Unit.Arte - Server Executable*/

/*Radix::System::Unit.Arte-Application Class*/

package org.radixware.ads.System.server;

import java.util.Collections;


@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit.Arte")
public published class Unit.Arte  extends org.radixware.ads.System.server.Unit  implements org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {


	/**Metainformation accessor method*/
	@SuppressWarnings("unused")
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return Unit.Arte_mi.rdxMeta;}

	/*Radix::System::Unit.Arte:Nested classes-Nested Classes*/

	/*Radix::System::Unit.Arte:Properties-Properties*/

	/*Radix::System::Unit.Arte:highArteInstCount-Detail Column Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit.Arte:highArteInstCount")
	public published  Int getHighArteInstCount() {
		return highArteInstCount;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit.Arte:highArteInstCount")
	public published   void setHighArteInstCount(Int val) {
		highArteInstCount = val;
	}

	/*Radix::System::Unit.Arte:unitId-Detail Column Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit.Arte:unitId")
	public published  Int getUnitId() {
		return unitId;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit.Arte:unitId")
	public published   void setUnitId(Int val) {
		unitId = val;
	}

	/*Radix::System::Unit.Arte:sapId-Detail Column Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit.Arte:sapId")
	public published  Int getSapId() {
		return sapId;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit.Arte:sapId")
	public published   void setSapId(Int val) {
		sapId = val;
	}

	/*Radix::System::Unit.Arte:serviceUri-Detail Column Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit.Arte:serviceUri")
	public published  Str getServiceUri() {
		return serviceUri;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit.Arte:serviceUri")
	public published   void setServiceUri(Str val) {
		serviceUri = val;
	}

	/*Radix::System::Unit.Arte:sap-Detail Reference*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit.Arte:sap")
	public published  org.radixware.ads.System.server.Sap getSap() {
		return sap;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit.Arte:sap")
	public published   void setSap(org.radixware.ads.System.server.Sap val) {
		sap = val;
	}

	/*Radix::System::Unit.Arte:service-Dynamic Property*/



	protected org.radixware.ads.System.server.Service service=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit.Arte:service")
	public published  org.radixware.ads.System.server.Service getService() {

		if (serviceUri == null)
		    return null;
		return Service.loadByPK(1, serviceUri, true);
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit.Arte:service")
	public published   void setService(org.radixware.ads.System.server.Service val) {

		if(val!=null)
		  serviceUri = val.uri;
		else
		  serviceUri = null;

		internal[service] = val;
	}

	/*Radix::System::Unit.Arte:deletedSapId-Dynamic Property*/



	protected Int deletedSapId=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit.Arte:deletedSapId")
	public published  Int getDeletedSapId() {
		return deletedSapId;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit.Arte:deletedSapId")
	public published   void setDeletedSapId(Int val) {
		deletedSapId = val;
	}

	/*Radix::System::Unit.Arte:service.Accessibility-Parent Property*/






















@SuppressWarnings({"unused","unchecked"})
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit.Arte:service.Accessibility")
public published  org.radixware.kernel.common.enums.EServiceAccessibility getService.Accessibility() {
	return service.Accessibility;
}

@SuppressWarnings({"unused","unchecked"})
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit.Arte:service.Accessibility")
public published   void setService.Accessibility(org.radixware.kernel.common.enums.EServiceAccessibility val) {
	service.Accessibility = val;
}

/*Radix::System::Unit.Arte:keyAliases-Dynamic Property*/



protected org.radixware.kernel.common.types.ArrStr keyAliases=null;











@SuppressWarnings({"unused","unchecked"})
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit.Arte:keyAliases")
public published  org.radixware.kernel.common.types.ArrStr getKeyAliases() {

	if (internal[keyAliases]==null){
	    try{
	        internal[keyAliases] = new ArrStr(Pki::KeystoreController.getServerKeystoreKeyAliases());
	    } catch (Exceptions::InvalidServerKeystoreSettingsException e){
	        Arte::Trace.debug("Error reading the key aliases from the keystore: "+e.toString(), Arte::EventSource:App);
	    } catch (Exceptions::Throwable e){
	        Arte::Trace.error("Error reading the key aliases from the keystore: "+e.toString(), Arte::EventSource:App);
	    }
	}

	return internal[keyAliases];
}

@SuppressWarnings({"unused","unchecked"})
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit.Arte:keyAliases")
public published   void setKeyAliases(org.radixware.kernel.common.types.ArrStr val) {
	keyAliases = val;
}

/*Radix::System::Unit.Arte:trustedCertificateAliases-Dynamic Property*/



protected org.radixware.kernel.common.types.ArrStr trustedCertificateAliases=null;











@SuppressWarnings({"unused","unchecked"})
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit.Arte:trustedCertificateAliases")
public published  org.radixware.kernel.common.types.ArrStr getTrustedCertificateAliases() {

	if (internal[trustedCertificateAliases]==null){
	    try{
	        internal[trustedCertificateAliases] = new ArrStr(Pki::KeystoreController.getServerKeystoreTrustedCertAliases());
	    } catch (Exceptions::InvalidServerKeystoreSettingsException e){
	        Arte::Trace.debug("Error reading the trusted certificate aliases from the keystore:"+e.toString(), Arte::EventSource:App);
	    } catch (Exceptions::Throwable e){
	        Arte::Trace.error("Error reading the trusted certificate aliases from the keystore: "+e.toString(), Arte::EventSource:App);
	    }
	}
	return internal[trustedCertificateAliases];
}

@SuppressWarnings({"unused","unchecked"})
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit.Arte:trustedCertificateAliases")
public published   void setTrustedCertificateAliases(org.radixware.kernel.common.types.ArrStr val) {
	trustedCertificateAliases = val;
}

/*Radix::System::Unit.Arte:avgActiveArteCount-Detail Column Property*/
















@SuppressWarnings({"unused","unchecked"})
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit.Arte:avgActiveArteCount")
public published  Num getAvgActiveArteCount() {
	return avgActiveArteCount;
}

@SuppressWarnings({"unused","unchecked"})
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit.Arte:avgActiveArteCount")
public published   void setAvgActiveArteCount(Num val) {
	avgActiveArteCount = val;
}

/*Radix::System::Unit.Arte:threadPriority-Detail Column Property*/








		















@SuppressWarnings({"unused","unchecked"})
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit.Arte:threadPriority")
public published  org.radixware.kernel.common.enums.EPriority getThreadPriority() {
	return threadPriority;
}

@SuppressWarnings({"unused","unchecked"})
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit.Arte:threadPriority")
public published   void setThreadPriority(org.radixware.kernel.common.enums.EPriority val) {
	threadPriority = val;
}

/*Radix::System::Unit.Arte:sapPropsXml-Dynamic Property*/



protected Str sapPropsXml=null;











@SuppressWarnings({"unused","unchecked"})
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit.Arte:sapPropsXml")
public published  Str getSapPropsXml() {
	return sapPropsXml;
}

@SuppressWarnings({"unused","unchecked"})
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit.Arte:sapPropsXml")
public published   void setSapPropsXml(Str val) {
	sapPropsXml = val;
}

/*Radix::System::Unit.Arte:isInAadc-Dynamic Property*/



protected Bool isInAadc=null;











@SuppressWarnings({"unused","unchecked"})
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit.Arte:isInAadc")
private final  Bool getIsInAadc() {

	return Arte::AadcManager.isInAadc();

}

/*Radix::System::Unit.Arte:primaryUnit-Parent Reference*/






















































































































/*Radix::System::Unit.Arte:Methods-Methods*/

/*Radix::System::Unit.Arte:afterDelete-User Method*/

@Override
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit.Arte:afterDelete")
protected published  void afterDelete () {
	deleteService();
	super.afterDelete();
}

/*Radix::System::Unit.Arte:afterInit-User Method*/

@Override
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit.Arte:afterInit")
protected published  void afterInit (org.radixware.kernel.server.types.Entity src, org.radixware.kernel.common.enums.EEntityInitializationPhase phase) {
	super.afterInit(src, phase);
	type = UnitType:Arte;
}

/*Radix::System::Unit.Arte:beforeDelete-User Method*/

@Override
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit.Arte:beforeDelete")
protected published  boolean beforeDelete () {
	deletedSapId = sapId;
	return super.beforeDelete();
}

/*Radix::System::Unit.Arte:updateService-User Method*/

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit.Arte:updateService")
protected published  void updateService () {
	final Sap sap;
	final boolean isSapExists = sapId != null && sapId != 0;

	if (isSapExists) {
	    sap = sap;
	    ServerSapUtils.fillProps(sap, sapPropsXml);
	} else {
	    sap = new Sap();
	    sap.init();
	    ServerSapUtils.fillProps(sap, sapPropsXml);
	    sap.systemId = 1;
	    sap.uri = appendIdToServiceUri() ? serviceUri + "#" + id : serviceUri;
	    sap.title = Sap.createTitleForUnitSap(sap.id, id);
	    if (service.Accessibility != null) {
	        sap.accessibility = service.Accessibility;
	    }
	}
	sap.title = Sap.createTitleForUnitSap(sap.id, id);
	updateSap(sap);

	if (!isSapExists) {
	    sap.systemUnitId = id;
	    sap.create();
	    sapId = sap.id;
	} else {
	    sap.update();
	}
}

/*Radix::System::Unit.Arte:deleteService-User Method*/

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit.Arte:deleteService")
@Deprecated
protected published  void deleteService () {
	//delete cascade since RADIX-8531
}

/*Radix::System::Unit.Arte:getUsedAddresses-User Method*/

@Override
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit.Arte:getUsedAddresses")
protected published  java.util.Collection<org.radixware.ads.System.server.AddressInfo> getUsedAddresses () {
	if (sapId != null) {
	    return Collections.singletonList(new AddressInfo(instanceId, id, sapId));
	}
	return Collections.emptyList();
}

/*Radix::System::Unit.Arte:copyScpToSapLinks-User Method*/

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit.Arte:copyScpToSapLinks")
protected  void copyScpToSapLinks (org.radixware.kernel.server.types.Entity src) {
	Int srcSapId = ((Unit.Arte) src).sapId;
	if (srcSapId == null) {
	    return;
	}
	Scp2SapLinksCursor cursor = Scp2SapLinksCursor.open(srcSapId);
	try {
	    while (cursor.next()) {
	        Scp2Sap link = new Scp2Sap();
	        link.init(null, cursor.scp2Sap);
	        link.sapId = sapId;
	        link.create(cursor.scp2Sap);
	    }
	} finally {
	    cursor.close();
	}
}

/*Radix::System::Unit.Arte:beforeCreate-User Method*/

@Override
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit.Arte:beforeCreate")
protected published  boolean beforeCreate (org.radixware.kernel.server.types.Entity src) {
	Int srcSapId = null;
	if (src != null) {
	    srcSapId = ((Unit.Arte) src).sapId;
	    sapId = 0;
	    sapPropsXml = ServerSapUtils.writePropsForCopy(srcSapId, sapPropsXml);
	}
	updateService();
	if (srcSapId != null) {
	    ServerSapUtils.copyScpLinks(srcSapId, sapId);
	}
	return super.beforeCreate(src);
}

/*Radix::System::Unit.Arte:beforeUpdate-User Method*/

@Override
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit.Arte:beforeUpdate")
protected published  boolean beforeUpdate () {
	updateService();
	return super.beforeUpdate();
}

/*Radix::System::Unit.Arte:appendIdToServiceUri-User Method*/

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit.Arte:appendIdToServiceUri")
protected published  boolean appendIdToServiceUri () {
	return false;
}

/*Radix::System::Unit.Arte:updateSap-User Method*/

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit.Arte:updateSap")
protected published  void updateSap (org.radixware.ads.System.server.Sap sap) {
	return;
}

/*Radix::System::Unit.Arte:onCommand_getLoad-Command Handler Method*/

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit.Arte:onCommand_getLoad")
public  org.radixware.schemas.types.NumDocument onCommand_getLoad (org.radixware.schemas.types.StrDocument input, org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValHandlersById) {
	//test for RADIX-7311
	try {
	    org.radixware.schemas.systeminstancecontrol.UnitCommandRs rs = Instance.loadByPK(instanceId, true).invokeUnitCommand(id, input, 5);
	    org.radixware.schemas.types.NumDocument numDoc = org.radixware.schemas.types.NumDocument.Factory.parse(rs.Response.DomNode);
	    return numDoc;
	} catch (Exception ex) {
	    throw new AppError("Error", ex);
	}
}



@Override
public org.radixware.kernel.server.types.FormHandler.NextDialogsRequest execCommand(org.radixware.kernel.common.types.Id cmdId, org.radixware.kernel.common.types.Id propId, org.apache.xmlbeans.XmlObject input, org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValsById, org.apache.xmlbeans.XmlObject output) throws org.radixware.kernel.common.exceptions.AppException,java.lang.InterruptedException{
	if(cmdId == cmdB6JWJ2Z7RZBIREDEKBIYLS2G6I){
		org.radixware.schemas.types.NumDocument result = onCommand_getLoad((org.radixware.schemas.types.StrDocument)org.radixware.kernel.common.utils.XmlObjectProcessor.getAppCommandInputData(getClass().getClassLoader(),input,org.radixware.schemas.types.StrDocument.class),newPropValsById);
		if(result != null)
			output.set(result);
		return null;
	} else 
		return super.execCommand(cmdId,propId,input,newPropValsById,output);
}


}

/* Radix::System::Unit.Arte - Server Meta*/

/*Radix::System::Unit.Arte-Application Class*/

package org.radixware.ads.System.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Unit.Arte_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("aclFKCYABVIK7OBDCJAAALOMT5GDM"),"Unit.Arte",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4EUVQOK4OXOBDFKUAAMPGXUWTQ"),

						org.radixware.kernel.common.enums.EClassType.APPLICATION,

						/*Radix::System::Unit.Arte:Presentations-Entity Object Presentations*/
						/*Class presentation attributes*/
						 new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef(/*Owner Class Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("aclFKCYABVIK7OBDCJAAALOMT5GDM"),
							/*Owner Class Name*/
							"Unit.Arte",
							/*Title Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4EUVQOK4OXOBDFKUAAMPGXUWTQ"),
							/*Property presentations*/

							/*Radix::System::Unit.Arte:Properties-Properties*/
							new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef[]{

									/*Radix::System::Unit.Arte:highArteInstCount:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colU77W6DFOK7OBDCJAAALOMT5GDM"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::Unit.Arte:sapId:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colUT7W6DFOK7OBDCJAAALOMT5GDM"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::Unit.Arte:serviceUri:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colUX7W6DFOK7OBDCJAAALOMT5GDM"),org.radixware.kernel.common.enums.EEditPossibility.ON_CREATE,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_SELECTOR)),

									/*Radix::System::Unit.Arte:sap:PropertyPresentation-Parent Ref Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadParentTitlePropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colZVIWAULWLPOBDCJEAALOMT5GDM"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,null,null,new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),org.radixware.kernel.server.types.Restrictions.Factory.newInstance(262143,null,null,null),null,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_SELECTOR)),

									/*Radix::System::Unit.Arte:service:PropertyPresentation-Parent Ref Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadParentTitlePropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd4BMIX7VU2FEYVHRPZNE2U4HW4A"),org.radixware.kernel.common.enums.EEditPossibility.ON_CREATE,true,false,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("sprQJ2Z7MLPLPOBDCJEAALOMT5GDM"),new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),org.radixware.kernel.server.types.Restrictions.Factory.newInstance(16439,null,null,null),null,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::Unit.Arte:service.Accessibility:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colY3MB2SZHCVGOTM5YOMBYKEEQDQ"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::Unit.Arte:keyAliases:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdZA2BGW6W7NBHREFDUWD4BURWTY"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,true,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::Unit.Arte:trustedCertificateAliases:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdI74BB6ZV6RHTFP4KBURNEVRD3U"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,true,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::Unit.Arte:avgActiveArteCount:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col4Z6HA6NC5ZADBCPDEBBNET2IRA"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::Unit.Arte:threadPriority:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col2I6B5AN7CNEK3KRJDLEDG4UXZY"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::Unit.Arte:sapPropsXml:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdZ7WFELXCVJBLBLM3SIZOXR4KAU"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::Unit.Arte:isInAadc:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd7QNFT7PBDJGQTO7Y7O6OL2D4IU"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::Unit.Arte:primaryUnit:PropertyPresentation-Parent Ref Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadParentTitlePropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colT3XHLBWPY5GGRBETJTUPM3WC54"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,null,null,new org.radixware.kernel.server.meta.presentations.RadConditionDef("<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:PropSqlName TableId=\"aec5HP4XTP3EGWDBRCRAAIT4AGD7E\" PropId=\"colF2TCBU6YX7NRDB6TAALOMT5GDM\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> = </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tbl5HP4XTP3EGWDBRCRAAIT4AGD7E\" PropId=\"colF2TCBU6YX7NRDB6TAALOMT5GDM\" Owner=\"CHILD\"/></xsc:Item><xsc:Item><xsc:Sql>\nand (select </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblXOKR7CSUNFG3DCVQGB5LWIWIIU\" PropId=\"colAQ5QDYFUHRCB7KJ5TDUEMJMRYI\" Owner=\"TABLE\"/></xsc:Item><xsc:Item><xsc:Sql> from </xsc:Sql></xsc:Item><xsc:Item><xsc:TableSqlName TableId=\"tblXOKR7CSUNFG3DCVQGB5LWIWIIU\"/></xsc:Item><xsc:Item><xsc:Sql> where </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblXOKR7CSUNFG3DCVQGB5LWIWIIU\" PropId=\"colSP2LUGR37RE77MFFOTM77YJNQI\" Owner=\"TABLE\"/></xsc:Item><xsc:Item><xsc:Sql> = </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aec5HP4XTP3EGWDBRCRAAIT4AGD7E\" PropId=\"colEVLUYADHR5VDBNSIAAUMFADAIA\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>)\n= (select </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblXOKR7CSUNFG3DCVQGB5LWIWIIU\" PropId=\"colAQ5QDYFUHRCB7KJ5TDUEMJMRYI\" Owner=\"TABLE\"/></xsc:Item><xsc:Item><xsc:Sql> from </xsc:Sql></xsc:Item><xsc:Item><xsc:TableSqlName TableId=\"tblXOKR7CSUNFG3DCVQGB5LWIWIIU\"/></xsc:Item><xsc:Item><xsc:Sql> where </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblXOKR7CSUNFG3DCVQGB5LWIWIIU\" PropId=\"colSP2LUGR37RE77MFFOTM77YJNQI\" Owner=\"TABLE\"/></xsc:Item><xsc:Item><xsc:Sql> = </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tbl5HP4XTP3EGWDBRCRAAIT4AGD7E\" PropId=\"colEVLUYADHR5VDBNSIAAUMFADAIA\" Owner=\"CHILD\"/></xsc:Item><xsc:Item><xsc:Sql>)\nand (</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aec5HP4XTP3EGWDBRCRAAIT4AGD7E\" PropId=\"colEVLUYADHR5VDBNSIAAUMFADAIA\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> &lt;> </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tbl5HP4XTP3EGWDBRCRAAIT4AGD7E\" PropId=\"colEVLUYADHR5VDBNSIAAUMFADAIA\" Owner=\"CHILD\"/></xsc:Item><xsc:Item><xsc:Sql>)</xsc:Sql></xsc:Item></xsc:Sqml>","<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),org.radixware.kernel.server.types.Restrictions.Factory.newInstance(66583529,null,null,null),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprXCFCDDGTRHNRDB5MAALOMT5GDM")},null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.TITLE,org.radixware.kernel.common.enums.EPropAttrInheritance.EDITING,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_SELECTOR,org.radixware.kernel.common.enums.EPropAttrInheritance.HINT))
							},
							/*Commands*/
							new org.radixware.kernel.server.meta.presentations.RadCommandDef[]{

									new org.radixware.kernel.server.meta.presentations.RadCommandDef(
									/*Radix::System::Unit.Arte:editKeystoreAliases-Property Command*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdZ44E4AY7YNBKPGRICPK2IVE2BE"),"editKeystoreAliases",org.radixware.kernel.common.enums.ECommandScope.PROPERTY,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("prd4UPD4GSLZ5GEPELEOMVXKLYL2U"),org.radixware.kernel.common.types.Id.Factory.loadFrom("prdFLVEHYM64RC5FHQR26KNKMRK3I"),org.radixware.kernel.common.types.Id.Factory.loadFrom("prdPB4QLAROQVDTPGUJVKFGGWO76I"),org.radixware.kernel.common.types.Id.Factory.loadFrom("prd2WMYZEX6TJGZ5I2B76BRZH7C5U")},org.radixware.kernel.common.enums.ECommandAccessibility.ALWAYS,org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,org.radixware.kernel.common.types.Id.Factory.loadFrom("aclFKCYABVIK7OBDCJAAALOMT5GDM"),false,true),

									new org.radixware.kernel.server.meta.presentations.RadCommandDef(
									/*Radix::System::Unit.Arte:getLoad-Object Command*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdB6JWJ2Z7RZBIREDEKBIYLS2G6I"),"getLoad",org.radixware.kernel.common.enums.ECommandScope.OBJECT,null,org.radixware.kernel.common.enums.ECommandAccessibility.ALWAYS,org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,org.radixware.kernel.common.types.Id.Factory.loadFrom("aclFKCYABVIK7OBDCJAAALOMT5GDM"),false,true)
							},
							/*Sortings*/
							null,
							/*Filters*/
							null,
							/*Editor presentations*/
							new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef[]{

									new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef(
									/*Radix::System::Unit.Arte:Create-Editor Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("epr2NLJ6YFMK7OBDCJAAALOMT5GDM"),
									"Create",
									org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprXCFCDDGTRHNRDB5MAALOMT5GDM"),
									3216,
									null,

									/*Radix::System::Unit.Arte:Create:Children-Explorer Items*/
										null
									,
									org.radixware.kernel.server.types.Restrictions.Factory.newInstance(0,null,null,null),
									(org.radixware.kernel.server.meta.presentations.RadPropertyPresentationAttributes[]) null,
									org.radixware.kernel.common.enums.EEditorPresentationRightsInheritanceMode.FROM_REPLACED,
									null),

									new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef(
									/*Radix::System::Unit.Arte:Edit-Editor Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprZZ6KB4TXLPOBDCJEAALOMT5GDM"),
									"Edit",
									org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
									org.radixware.kernel.common.types.Id.Factory.loadFrom("epr2NLJ6YFMK7OBDCJAAALOMT5GDM"),
									3216,
									null,

									/*Radix::System::Unit.Arte:Edit:Children-Explorer Items*/
										null
									,
									org.radixware.kernel.server.types.Restrictions.Factory.newInstance(2152,null,null,null),
									(org.radixware.kernel.server.meta.presentations.RadPropertyPresentationAttributes[]) null,
									org.radixware.kernel.common.enums.EEditorPresentationRightsInheritanceMode.FROM_REPLACED,
									null)
							},
							/*Selector presentations*/
							null,
							/*Default selector presentation Id*/
							null,
							/*Presentation Map*/
							org.radixware.kernel.common.utils.Maps.fromArrays(new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprS6PW3X2EKJFUNPGCXYDBACXY7A"),org.radixware.kernel.common.types.Id.Factory.loadFrom("eprXCFCDDGTRHNRDB5MAALOMT5GDM")},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr2NLJ6YFMK7OBDCJAAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("eprZZ6KB4TXLPOBDCJEAALOMT5GDM")}),
							/*Object title format*/
							null,
							/*Class catalogs*/
							new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog[]{

									new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog(
									/*Radix::System::Unit.Arte:Default-Dynamic Class Catalog*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eccKOGFFVPJAHOBDA26AAMPGXSZKU"),org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.ELinkMode.VIRTUAL,new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.Item[]{
										new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.ClassRef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aclFKCYABVIK7OBDCJAAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("cctIM3THFAESQAESFLHAAJZREMVEY"),0.0,true,org.radixware.kernel.common.types.Id.Factory.loadFrom("aclFKCYABVIK7OBDCJAAALOMT5GDM"),null)}
									)
							},
							/*Restrictions*/
							null,null,0),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl5HP4XTP3EGWDBRCRAAIT4AGD7E"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),

						/*Radix::System::Unit.Arte:Properties-Properties*/
						new org.radixware.kernel.server.meta.clazzes.RadPropDef[]{

								/*Radix::System::Unit.Arte:highArteInstCount-Detail Column Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDetailPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colU77W6DFOK7OBDCJAAALOMT5GDM"),"highArteInstCount",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWQUFQOK4OXOBDFKUAAMPGXUWTQ"),org.radixware.kernel.common.enums.EValType.INT,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("refAIKHITFNK7OBDCJAAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colJP6YRRR6BVD73IIDYKAYED5HMQ"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::Unit.Arte:unitId-Detail Column Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDetailPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colUP7W6DFOK7OBDCJAAALOMT5GDM"),"unitId",null,org.radixware.kernel.common.enums.EValType.INT,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("refAIKHITFNK7OBDCJAAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colSP2LUGR37RE77MFFOTM77YJNQI"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::Unit.Arte:sapId-Detail Column Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDetailPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colUT7W6DFOK7OBDCJAAALOMT5GDM"),"sapId",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWIUFQOK4OXOBDFKUAAMPGXUWTQ"),org.radixware.kernel.common.enums.EValType.INT,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("refAIKHITFNK7OBDCJAAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("col5JGDIKHSWNA3HPXU6RW7KBGWVI"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::Unit.Arte:serviceUri-Detail Column Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDetailPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colUX7W6DFOK7OBDCJAAALOMT5GDM"),"serviceUri",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWEUFQOK4OXOBDFKUAAMPGXUWTQ"),org.radixware.kernel.common.enums.EValType.STR,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("refAIKHITFNK7OBDCJAAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colAQ5QDYFUHRCB7KJ5TDUEMJMRYI"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::Unit.Arte:sap-Detail Reference*/

								new org.radixware.kernel.server.meta.clazzes.RadDetailParentRefPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colZVIWAULWLPOBDCJEAALOMT5GDM"),"sap",null,org.radixware.kernel.common.types.Id.Factory.loadFrom("refAIKHITFNK7OBDCJAAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("refU3KVGCHBNFG7ZOT63JDLHQAB6Y"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecR7FXMYDVVHWDBROXAAIT4AGD7E"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::Unit.Arte:service-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicRefPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd4BMIX7VU2FEYVHRPZNE2U4HW4A"),"service",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6N6MJFIM4JA7RAXGRCX2S7E5DY"),org.radixware.kernel.common.enums.EValType.PARENT_REF,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("aecC2OWQGDVVHWDBROXAAIT4AGD7E"),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::Unit.Arte:deletedSapId-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdDAKAZXR4LTOBDCJFAALOMT5GDM"),"deletedSapId",null,org.radixware.kernel.common.enums.EValType.INT,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::Unit.Arte:service.Accessibility-Parent Property*/

								new org.radixware.kernel.server.meta.clazzes.RadParentPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colY3MB2SZHCVGOTM5YOMBYKEEQDQ"),"service.Accessibility",null,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("prd4BMIX7VU2FEYVHRPZNE2U4HW4A")},org.radixware.kernel.common.types.Id.Factory.loadFrom("colXXBGA3KXGZEP5EQS26UU6KWTEA"),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::Unit.Arte:keyAliases-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdZA2BGW6W7NBHREFDUWD4BURWTY"),"keyAliases",null,org.radixware.kernel.common.enums.EValType.ARR_STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::Unit.Arte:trustedCertificateAliases-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdI74BB6ZV6RHTFP4KBURNEVRD3U"),"trustedCertificateAliases",null,org.radixware.kernel.common.enums.EValType.ARR_STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::Unit.Arte:avgActiveArteCount-Detail Column Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDetailPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col4Z6HA6NC5ZADBCPDEBBNET2IRA"),"avgActiveArteCount",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAF5V3G7GNZEABK5MLM2CRLZ7WQ"),org.radixware.kernel.common.enums.EValType.NUM,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("refAIKHITFNK7OBDCJAAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colTF5C4IM5EFG4ZMEFUFVQRUBAQE"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("0")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::Unit.Arte:threadPriority-Detail Column Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDetailPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col2I6B5AN7CNEK3KRJDLEDG4UXZY"),"threadPriority",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsT2FPRYFMP5DJFCQFGUKLLDYX2U"),org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("acsYING7OYUJ3NRDAQSABIFNQAAAE"),org.radixware.kernel.common.types.Id.Factory.loadFrom("refAIKHITFNK7OBDCJAAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colIV22HNY76VCYJAP7FVHQ6MNW24"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::Unit.Arte:sapPropsXml-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdZ7WFELXCVJBLBLM3SIZOXR4KAU"),"sapPropsXml",null,org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::Unit.Arte:isInAadc-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd7QNFT7PBDJGQTO7Y7O6OL2D4IU"),"isInAadc",null,org.radixware.kernel.common.enums.EValType.BOOL,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadAccessorProxy.R_ACCESSOR_INSTANCE),

								/*Radix::System::Unit.Arte:primaryUnit-Parent Reference*/

								new org.radixware.kernel.server.meta.clazzes.RadInnateRefPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colT3XHLBWPY5GGRBETJTUPM3WC54"),"primaryUnit",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7LIIT6LQPZGP5D7EY74FKGFSIA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("refI3HCWMKRANDRDA42ZXKFHOFLGY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE)
						},

						/*Radix::System::Unit.Arte:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth2UCNHZSLQVGXPG7Z4SJQJPPSAY"),"afterDelete",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthD77F2HBL2FGSVPJHR3SEAMPWMA"),"afterInit",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("src",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprT37AW2NFRZB2VGAYFVEK3WEHZU")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("phase",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr353DMLPCYJEDFD5WN7FVJDZWFU"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthKNZ5Z7AS6RFRPIX6ZD5JDKEC34"),"beforeDelete",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthHMFWOHVALPOBDCJEAALOMT5GDM"),"updateService",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthHQFWOHVALPOBDCJEAALOMT5GDM"),"deleteService",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthQIF7CPRFE5GUTIY26PT5SYUD6E"),"getUsedAddresses",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,org.radixware.kernel.common.enums.EValType.JAVA_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthALOADLETQVD6XG5IUARIWPD6O4"),"copyScpToSapLinks",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("src",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprHNAC7ZG54VBWNHXNTFOWQW26TM"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthFMXUFM5J25E4TNM4PR73F6V3E4"),"beforeCreate",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("src",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprC23VYM7RNZBRVHFQZDGRJFNETI"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthPT5PMP2WVZGUDB7OTLOE2SBWKE"),"beforeUpdate",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthJ2H3SDBOTNCRHEZAMLXGNWMIGA"),"appendIdToServiceUri",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthM4HJNKZJCJD35DU66KRBE4BOLI"),"updateSap",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("sap",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprLSMHWPXSRBEILP4ZCYFI7SHHZU"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthcmdB6JWJ2Z7RZBIREDEKBIYLS2G6I"),"onCommand_getLoad",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("input",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprSIHKDJK4ZZCRPOW5OSM6W64L74")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("newPropValHandlersById",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr76YEEP6JSFCQXJ4KC55XD4VTTM"))
								},org.radixware.kernel.common.enums.EValType.XML)
						},
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("refAIKHITFNK7OBDCJAAALOMT5GDM")},
						null,null,null,false);
}

/* Radix::System::Unit.Arte - Desktop Executable*/

/*Radix::System::Unit.Arte-Application Class*/

package org.radixware.ads.System.explorer;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit.Arte")
public interface Unit.Arte   extends org.radixware.ads.System.explorer.Unit  {











































































































































































































	/*Radix::System::Unit.Arte:threadPriority:threadPriority-Presentation Property*/


	public class ThreadPriority extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public ThreadPriority(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public void setServerValue(org.radixware.kernel.common.client.models.items.properties.PropertyValue val){
			Object x = val.getValue();
			org.radixware.kernel.common.enums.EPriority dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EPriority ? (org.radixware.kernel.common.enums.EPriority)x : org.radixware.kernel.common.enums.EPriority.getForValue(((Int)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.INT)).longValue()));
			val.refineValue(dummy);
			super.setServerValue(val);
		}
		@Override
		public Class<org.radixware.kernel.common.enums.EPriority> getValClass(){
			return org.radixware.kernel.common.enums.EPriority.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.enums.EPriority dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EPriority ? (org.radixware.kernel.common.enums.EPriority)x : org.radixware.kernel.common.enums.EPriority.getForValue(((Int)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.INT)).longValue()));
			setValue(dummy);
		}





				














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit.Arte:threadPriority:threadPriority")
		public  org.radixware.kernel.common.enums.EPriority getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit.Arte:threadPriority:threadPriority")
		public   void setValue(org.radixware.kernel.common.enums.EPriority val) {
			Value = val;
		}
	}
	public ThreadPriority getThreadPriority();
	/*Radix::System::Unit.Arte:avgActiveArteCount:avgActiveArteCount-Presentation Property*/


	public class AvgActiveArteCount extends org.radixware.kernel.common.client.models.items.properties.PropertyNum{
		public AvgActiveArteCount(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Num dummy = ((Num)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit.Arte:avgActiveArteCount:avgActiveArteCount")
		public  Num getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit.Arte:avgActiveArteCount:avgActiveArteCount")
		public   void setValue(Num val) {
			Value = val;
		}
	}
	public AvgActiveArteCount getAvgActiveArteCount();
	/*Radix::System::Unit.Arte:highArteInstCount:highArteInstCount-Presentation Property*/


	public class HighArteInstCount extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public HighArteInstCount(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit.Arte:highArteInstCount:highArteInstCount")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit.Arte:highArteInstCount:highArteInstCount")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public HighArteInstCount getHighArteInstCount();
	/*Radix::System::Unit.Arte:sapId:sapId-Presentation Property*/


	public class SapId extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public SapId(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit.Arte:sapId:sapId")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit.Arte:sapId:sapId")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public SapId getSapId();
	/*Radix::System::Unit.Arte:serviceUri:serviceUri-Presentation Property*/


	public class ServiceUri extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public ServiceUri(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit.Arte:serviceUri:serviceUri")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit.Arte:serviceUri:serviceUri")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ServiceUri getServiceUri();
	/*Radix::System::Unit.Arte:service.Accessibility:service.Accessibility-Presentation Property*/


	public class Service.Accessibility extends org.radixware.kernel.common.client.models.items.properties.PropertyChar{
		public Service.Accessibility(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public void setServerValue(org.radixware.kernel.common.client.models.items.properties.PropertyValue val){
			Object x = val.getValue();
			org.radixware.kernel.common.enums.EServiceAccessibility dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EServiceAccessibility ? (org.radixware.kernel.common.enums.EServiceAccessibility)x : org.radixware.kernel.common.enums.EServiceAccessibility.getForValue((Character)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.CHAR)));
			val.refineValue(dummy);
			super.setServerValue(val);
		}
		@Override
		public Class<org.radixware.kernel.common.enums.EServiceAccessibility> getValClass(){
			return org.radixware.kernel.common.enums.EServiceAccessibility.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.enums.EServiceAccessibility dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EServiceAccessibility ? (org.radixware.kernel.common.enums.EServiceAccessibility)x : org.radixware.kernel.common.enums.EServiceAccessibility.getForValue((Character)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.CHAR)));
			setValue(dummy);
		}





				














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit.Arte:service.Accessibility:service.Accessibility")
		public  org.radixware.kernel.common.enums.EServiceAccessibility getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit.Arte:service.Accessibility:service.Accessibility")
		public   void setValue(org.radixware.kernel.common.enums.EServiceAccessibility val) {
			Value = val;
		}
	}
	public Service.Accessibility getService.Accessibility();
	/*Radix::System::Unit.Arte:sap:sap-Presentation Property*/


	public class Sap extends org.radixware.kernel.common.client.models.items.properties.PropertyRef{
		public Sap(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadParentRefPropertyDef def){
			super(model,def);
		}
		@Override
		public 
		org.radixware.ads.System.explorer.Sap.Sap_DefaultModel openEntityModel()  throws org.radixware.kernel.common.exceptions.ServiceClientException, InterruptedException{
		     return (org.radixware.ads.System.explorer.Sap.Sap_DefaultModel)super.openEntityModel();
		}
		@Override
		public 
		org.radixware.ads.System.explorer.Sap.DefaultGroupModel openGroupModel(){
		     return (org.radixware.ads.System.explorer.Sap.DefaultGroupModel)super.openGroupModel();
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.client.types.Reference dummy = ((org.radixware.kernel.common.client.types.Reference)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit.Arte:sap:sap")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit.Arte:sap:sap")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public Sap getSap();
	/*Radix::System::Unit.Arte:service:service-Presentation Property*/


	public class Service extends org.radixware.kernel.common.client.models.items.properties.PropertyRef{
		public Service(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadParentRefPropertyDef def){
			super(model,def);
		}
		@Override
		public 
		org.radixware.ads.System.explorer.Service.Service_DefaultModel openEntityModel()  throws org.radixware.kernel.common.exceptions.ServiceClientException, InterruptedException{
		     return (org.radixware.ads.System.explorer.Service.Service_DefaultModel)super.openEntityModel();
		}
		@Override
		public 
		org.radixware.ads.System.explorer.Service.DefaultGroupModel openGroupModel(){
		     return (org.radixware.ads.System.explorer.Service.DefaultGroupModel)super.openGroupModel();
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.client.types.Reference dummy = ((org.radixware.kernel.common.client.types.Reference)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit.Arte:service:service")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit.Arte:service:service")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public Service getService();
	/*Radix::System::Unit.Arte:isInAadc:isInAadc-Presentation Property*/


	public class IsInAadc extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public IsInAadc(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit.Arte:isInAadc:isInAadc")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit.Arte:isInAadc:isInAadc")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public IsInAadc getIsInAadc();
	/*Radix::System::Unit.Arte:trustedCertificateAliases:trustedCertificateAliases-Presentation Property*/


	public class TrustedCertificateAliases extends org.radixware.kernel.common.client.models.items.properties.PropertyArrStr{
		public TrustedCertificateAliases(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit.Arte:trustedCertificateAliases:trustedCertificateAliases")
		public  org.radixware.kernel.common.types.ArrStr getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit.Arte:trustedCertificateAliases:trustedCertificateAliases")
		public   void setValue(org.radixware.kernel.common.types.ArrStr val) {
			Value = val;
		}
	}
	public TrustedCertificateAliases getTrustedCertificateAliases();
	/*Radix::System::Unit.Arte:sapPropsXml:sapPropsXml-Presentation Property*/


	public class SapPropsXml extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public SapPropsXml(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit.Arte:sapPropsXml:sapPropsXml")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit.Arte:sapPropsXml:sapPropsXml")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public SapPropsXml getSapPropsXml();
	/*Radix::System::Unit.Arte:keyAliases:keyAliases-Presentation Property*/


	public class KeyAliases extends org.radixware.kernel.common.client.models.items.properties.PropertyArrStr{
		public KeyAliases(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit.Arte:keyAliases:keyAliases")
		public  org.radixware.kernel.common.types.ArrStr getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit.Arte:keyAliases:keyAliases")
		public   void setValue(org.radixware.kernel.common.types.ArrStr val) {
			Value = val;
		}
	}
	public KeyAliases getKeyAliases();
	public static class GetLoad extends org.radixware.kernel.common.client.models.items.Command{
		protected GetLoad(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public org.radixware.schemas.types.NumDocument send(org.radixware.schemas.types.StrDocument input ) throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,input);
			org.apache.xmlbeans.XmlObject xmlResponse = processResponse(response, null);
			return (org.radixware.schemas.types.NumDocument)org.radixware.kernel.common.utils.XmlObjectProcessor.castToXmlClass(getClass().getClassLoader(),xmlResponse,org.radixware.schemas.types.NumDocument.class);
		}

	}

	public static class EditKeystoreAliases extends org.radixware.kernel.common.client.models.items.Command{
		protected EditKeystoreAliases(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public void send(org.radixware.kernel.common.types.Id propertyId) throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),propertyId,null);
			processResponse(response, propertyId);
		}

	}



}

/* Radix::System::Unit.Arte - Desktop Meta*/

/*Radix::System::Unit.Arte-Application Class*/

package org.radixware.ads.System.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Unit.Arte_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::System::Unit.Arte:Presentations-Entity Object Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aclFKCYABVIK7OBDCJAAALOMT5GDM"),
			"Radix::System::Unit.Arte",
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),
			null,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4EUVQOK4OXOBDFKUAAMPGXUWTQ"),null,null,0,

			/*Radix::System::Unit.Arte:Properties-Properties*/
			new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

					/*Radix::System::Unit.Arte:highArteInstCount:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colU77W6DFOK7OBDCJAAALOMT5GDM"),
						"highArteInstCount",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWQUFQOK4OXOBDFKUAAMPGXUWTQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclFKCYABVIK7OBDCJAAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.DETAIL_PROP,
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

						/*Radix::System::Unit.Arte:highArteInstCount:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(0L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Unit.Arte:sapId:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colUT7W6DFOK7OBDCJAAALOMT5GDM"),
						"sapId",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWIUFQOK4OXOBDFKUAAMPGXUWTQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclFKCYABVIK7OBDCJAAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.DETAIL_PROP,
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

						/*Radix::System::Unit.Arte:sapId:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Unit.Arte:serviceUri:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colUX7W6DFOK7OBDCJAAALOMT5GDM"),
						"serviceUri",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWEUFQOK4OXOBDFKUAAMPGXUWTQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclFKCYABVIK7OBDCJAAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.DETAIL_PROP,
						20,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ON_CREATE,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::System::Unit.Arte:serviceUri:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,250,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Unit.Arte:sap:PropertyPresentation-Parent Ref Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colZVIWAULWLPOBDCJEAALOMT5GDM"),
						"sap",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclFKCYABVIK7OBDCJAAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.PARENT_REF,
						org.radixware.kernel.common.enums.EPropNature.DETAIL_PROP,
						20,
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
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecR7FXMYDVVHWDBROXAAIT4AGD7E"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblR7FXMYDVVHWDBROXAAIT4AGD7E"),
						null,
						null,
						null,
						262143,
						262143,false),

					/*Radix::System::Unit.Arte:service:PropertyPresentation-Parent Ref Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd4BMIX7VU2FEYVHRPZNE2U4HW4A"),
						"service",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6N6MJFIM4JA7RAXGRCX2S7E5DY"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclFKCYABVIK7OBDCJAAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.PARENT_REF,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
						4,
						null,
						null,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ON_CREATE,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,
						null,
						null,
						null,
						null,
						true,-1,-1,1,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecC2OWQGDVVHWDBROXAAIT4AGD7E"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblC2OWQGDVVHWDBROXAAIT4AGD7E"),
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("sprQJ2Z7MLPLPOBDCJEAALOMT5GDM"),
						16439,
						20,false),

					/*Radix::System::Unit.Arte:service.Accessibility:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colY3MB2SZHCVGOTM5YOMBYKEEQDQ"),
						"service.Accessibility",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclFKCYABVIK7OBDCJAAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.CHAR,
						org.radixware.kernel.common.enums.EPropNature.PARENT_PROP,
						4,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecC2OWQGDVVHWDBROXAAIT4AGD7E"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("colXXBGA3KXGZEP5EQS26UU6KWTEA"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acs3A7RUNYYRJCKPBZVNZD23FALXI"),
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

						/*Radix::System::Unit.Arte:service.Accessibility:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acs3A7RUNYYRJCKPBZVNZD23FALXI"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.NONE,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Unit.Arte:keyAliases:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdZA2BGW6W7NBHREFDUWD4BURWTY"),
						"keyAliases",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclFKCYABVIK7OBDCJAAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.ARR_STR,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
						4,
						null,
						null,
						null,
						true,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,true,
						false,
						false,
						null,
						false,

						/*Radix::System::Unit.Arte:keyAliases:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMRC36O2AA5AF7CXOJGUJXAZKZQ"),
						null,
						null,
						false,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Unit.Arte:trustedCertificateAliases:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdI74BB6ZV6RHTFP4KBURNEVRD3U"),
						"trustedCertificateAliases",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclFKCYABVIK7OBDCJAAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.ARR_STR,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
						4,
						null,
						null,
						null,
						true,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,true,
						false,
						false,
						null,
						false,

						/*Radix::System::Unit.Arte:trustedCertificateAliases:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXK5GM5FUHJH6HB4JWKG3OIIKCY"),
						null,
						null,
						false,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Unit.Arte:avgActiveArteCount:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col4Z6HA6NC5ZADBCPDEBBNET2IRA"),
						"avgActiveArteCount",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAF5V3G7GNZEABK5MLM2CRLZ7WQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclFKCYABVIK7OBDCJAAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.NUM,
						org.radixware.kernel.common.enums.EPropNature.DETAIL_PROP,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("0"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::System::Unit.Arte:avgActiveArteCount:PropertyPresentation:Edit Options:-Edit Mask Num*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskNum(null,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.DEFAULT,null,null,(byte)-1),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Unit.Arte:threadPriority:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col2I6B5AN7CNEK3KRJDLEDG4UXZY"),
						"threadPriority",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsT2FPRYFMP5DJFCQFGUKLLDYX2U"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclFKCYABVIK7OBDCJAAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.DETAIL_PROP,
						4,
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acsYING7OYUJ3NRDAQSABIFNQAAAE"),
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

						/*Radix::System::Unit.Arte:threadPriority:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acsYING7OYUJ3NRDAQSABIFNQAAAE"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.NONE,null),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDJF32UENEBFT3MP2MM6PTGZY4E"),
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Unit.Arte:sapPropsXml:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdZ7WFELXCVJBLBLM3SIZOXR4KAU"),
						"sapPropsXml",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclFKCYABVIK7OBDCJAAALOMT5GDM"),
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
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::System::Unit.Arte:sapPropsXml:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Unit.Arte:isInAadc:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd7QNFT7PBDJGQTO7Y7O6OL2D4IU"),
						"isInAadc",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclFKCYABVIK7OBDCJAAALOMT5GDM"),
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::System::Unit.Arte:isInAadc:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aclFKCYABVIK7OBDCJAAALOMT5GDM"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Unit.Arte:primaryUnit:PropertyPresentation-Parent Ref Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colT3XHLBWPY5GGRBETJTUPM3WC54"),
						"primaryUnit",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclFKCYABVIK7OBDCJAAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.PARENT_REF,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						55,
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
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl5HP4XTP3EGWDBRCRAAIT4AGD7E"),
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprXCFCDDGTRHNRDB5MAALOMT5GDM")},
						null,
						null,
						66583529,
						66584555,false)
			},
			new org.radixware.kernel.common.client.meta.RadCommandDef[]{
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::System::Unit.Arte:editKeystoreAliases-Property Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdZ44E4AY7YNBKPGRICPK2IVE2BE"),
						"editKeystoreAliases",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclFKCYABVIK7OBDCJAAALOMT5GDM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUDX7BDD4FFHP7GMIFUVFANAMGU"),
						null,
						null,
						org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,
						true,
						false,
						false,
						org.radixware.kernel.common.enums.ECommandScope.PROPERTY,
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("prd4UPD4GSLZ5GEPELEOMVXKLYL2U"),org.radixware.kernel.common.types.Id.Factory.loadFrom("prdFLVEHYM64RC5FHQR26KNKMRK3I"),org.radixware.kernel.common.types.Id.Factory.loadFrom("prdPB4QLAROQVDTPGUJVKFGGWO76I"),org.radixware.kernel.common.types.Id.Factory.loadFrom("prd2WMYZEX6TJGZ5I2B76BRZH7C5U")},
						org.radixware.kernel.common.enums.ECommandAccessibility.ALWAYS),
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::System::Unit.Arte:getLoad-Object Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdB6JWJ2Z7RZBIREDEKBIYLS2G6I"),
						"getLoad",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclFKCYABVIK7OBDCJAAALOMT5GDM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLO6W2ZOBPJBVDNFXM4X3E72HNE"),
						null,
						null,
						org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,
						false,
						false,
						false,
						org.radixware.kernel.common.enums.ECommandScope.OBJECT,
						null,
						org.radixware.kernel.common.enums.ECommandAccessibility.ALWAYS)
			},
			null,
			null,
			null,
			new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr2NLJ6YFMK7OBDCJAAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("eprZZ6KB4TXLPOBDCJEAALOMT5GDM")},
			true,true,false);
}

/* Radix::System::Unit.Arte - Web Executable*/

/*Radix::System::Unit.Arte-Application Class*/

package org.radixware.ads.System.web;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit.Arte")
public interface Unit.Arte   extends org.radixware.ads.System.web.Unit  {











































































































































































































	/*Radix::System::Unit.Arte:threadPriority:threadPriority-Presentation Property*/


	public class ThreadPriority extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public ThreadPriority(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public void setServerValue(org.radixware.kernel.common.client.models.items.properties.PropertyValue val){
			Object x = val.getValue();
			org.radixware.kernel.common.enums.EPriority dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EPriority ? (org.radixware.kernel.common.enums.EPriority)x : org.radixware.kernel.common.enums.EPriority.getForValue(((Int)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.INT)).longValue()));
			val.refineValue(dummy);
			super.setServerValue(val);
		}
		@Override
		public Class<org.radixware.kernel.common.enums.EPriority> getValClass(){
			return org.radixware.kernel.common.enums.EPriority.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.enums.EPriority dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EPriority ? (org.radixware.kernel.common.enums.EPriority)x : org.radixware.kernel.common.enums.EPriority.getForValue(((Int)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.INT)).longValue()));
			setValue(dummy);
		}





				














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit.Arte:threadPriority:threadPriority")
		public  org.radixware.kernel.common.enums.EPriority getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit.Arte:threadPriority:threadPriority")
		public   void setValue(org.radixware.kernel.common.enums.EPriority val) {
			Value = val;
		}
	}
	public ThreadPriority getThreadPriority();
	/*Radix::System::Unit.Arte:avgActiveArteCount:avgActiveArteCount-Presentation Property*/


	public class AvgActiveArteCount extends org.radixware.kernel.common.client.models.items.properties.PropertyNum{
		public AvgActiveArteCount(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Num dummy = ((Num)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit.Arte:avgActiveArteCount:avgActiveArteCount")
		public  Num getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit.Arte:avgActiveArteCount:avgActiveArteCount")
		public   void setValue(Num val) {
			Value = val;
		}
	}
	public AvgActiveArteCount getAvgActiveArteCount();
	/*Radix::System::Unit.Arte:highArteInstCount:highArteInstCount-Presentation Property*/


	public class HighArteInstCount extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public HighArteInstCount(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit.Arte:highArteInstCount:highArteInstCount")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit.Arte:highArteInstCount:highArteInstCount")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public HighArteInstCount getHighArteInstCount();
	/*Radix::System::Unit.Arte:sapId:sapId-Presentation Property*/


	public class SapId extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public SapId(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit.Arte:sapId:sapId")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit.Arte:sapId:sapId")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public SapId getSapId();
	/*Radix::System::Unit.Arte:serviceUri:serviceUri-Presentation Property*/


	public class ServiceUri extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public ServiceUri(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit.Arte:serviceUri:serviceUri")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit.Arte:serviceUri:serviceUri")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ServiceUri getServiceUri();
	/*Radix::System::Unit.Arte:service.Accessibility:service.Accessibility-Presentation Property*/


	public class Service.Accessibility extends org.radixware.kernel.common.client.models.items.properties.PropertyChar{
		public Service.Accessibility(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public void setServerValue(org.radixware.kernel.common.client.models.items.properties.PropertyValue val){
			Object x = val.getValue();
			org.radixware.kernel.common.enums.EServiceAccessibility dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EServiceAccessibility ? (org.radixware.kernel.common.enums.EServiceAccessibility)x : org.radixware.kernel.common.enums.EServiceAccessibility.getForValue((Character)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.CHAR)));
			val.refineValue(dummy);
			super.setServerValue(val);
		}
		@Override
		public Class<org.radixware.kernel.common.enums.EServiceAccessibility> getValClass(){
			return org.radixware.kernel.common.enums.EServiceAccessibility.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.enums.EServiceAccessibility dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EServiceAccessibility ? (org.radixware.kernel.common.enums.EServiceAccessibility)x : org.radixware.kernel.common.enums.EServiceAccessibility.getForValue((Character)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.CHAR)));
			setValue(dummy);
		}





				














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit.Arte:service.Accessibility:service.Accessibility")
		public  org.radixware.kernel.common.enums.EServiceAccessibility getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit.Arte:service.Accessibility:service.Accessibility")
		public   void setValue(org.radixware.kernel.common.enums.EServiceAccessibility val) {
			Value = val;
		}
	}
	public Service.Accessibility getService.Accessibility();
	/*Radix::System::Unit.Arte:sap:sap-Presentation Property*/


	public class Sap extends org.radixware.kernel.common.client.models.items.properties.PropertyRef{
		public Sap(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadParentRefPropertyDef def){
			super(model,def);
		}
		@Override
		public 
		org.radixware.ads.System.web.Sap.Sap_DefaultModel openEntityModel()  throws org.radixware.kernel.common.exceptions.ServiceClientException, InterruptedException{
		     return (org.radixware.ads.System.web.Sap.Sap_DefaultModel)super.openEntityModel();
		}
		@Override
		public 
		org.radixware.ads.System.web.Sap.DefaultGroupModel openGroupModel(){
		     return (org.radixware.ads.System.web.Sap.DefaultGroupModel)super.openGroupModel();
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.client.types.Reference dummy = ((org.radixware.kernel.common.client.types.Reference)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit.Arte:sap:sap")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit.Arte:sap:sap")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public Sap getSap();
	/*Radix::System::Unit.Arte:service:service-Presentation Property*/


	public class Service extends org.radixware.kernel.common.client.models.items.properties.PropertyRef{
		public Service(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadParentRefPropertyDef def){
			super(model,def);
		}
		@Override
		public 
		org.radixware.ads.System.web.Service.Service_DefaultModel openEntityModel()  throws org.radixware.kernel.common.exceptions.ServiceClientException, InterruptedException{
		     return (org.radixware.ads.System.web.Service.Service_DefaultModel)super.openEntityModel();
		}
		@Override
		public 
		org.radixware.ads.System.web.Service.DefaultGroupModel openGroupModel(){
		     return (org.radixware.ads.System.web.Service.DefaultGroupModel)super.openGroupModel();
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.client.types.Reference dummy = ((org.radixware.kernel.common.client.types.Reference)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit.Arte:service:service")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit.Arte:service:service")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public Service getService();
	/*Radix::System::Unit.Arte:isInAadc:isInAadc-Presentation Property*/


	public class IsInAadc extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public IsInAadc(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit.Arte:isInAadc:isInAadc")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit.Arte:isInAadc:isInAadc")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public IsInAadc getIsInAadc();
	/*Radix::System::Unit.Arte:trustedCertificateAliases:trustedCertificateAliases-Presentation Property*/


	public class TrustedCertificateAliases extends org.radixware.kernel.common.client.models.items.properties.PropertyArrStr{
		public TrustedCertificateAliases(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit.Arte:trustedCertificateAliases:trustedCertificateAliases")
		public  org.radixware.kernel.common.types.ArrStr getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit.Arte:trustedCertificateAliases:trustedCertificateAliases")
		public   void setValue(org.radixware.kernel.common.types.ArrStr val) {
			Value = val;
		}
	}
	public TrustedCertificateAliases getTrustedCertificateAliases();
	/*Radix::System::Unit.Arte:sapPropsXml:sapPropsXml-Presentation Property*/


	public class SapPropsXml extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public SapPropsXml(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit.Arte:sapPropsXml:sapPropsXml")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit.Arte:sapPropsXml:sapPropsXml")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public SapPropsXml getSapPropsXml();
	/*Radix::System::Unit.Arte:keyAliases:keyAliases-Presentation Property*/


	public class KeyAliases extends org.radixware.kernel.common.client.models.items.properties.PropertyArrStr{
		public KeyAliases(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit.Arte:keyAliases:keyAliases")
		public  org.radixware.kernel.common.types.ArrStr getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit.Arte:keyAliases:keyAliases")
		public   void setValue(org.radixware.kernel.common.types.ArrStr val) {
			Value = val;
		}
	}
	public KeyAliases getKeyAliases();
	public static class GetLoad extends org.radixware.kernel.common.client.models.items.Command{
		protected GetLoad(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public org.radixware.schemas.types.NumDocument send(org.radixware.schemas.types.StrDocument input ) throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,input);
			org.apache.xmlbeans.XmlObject xmlResponse = processResponse(response, null);
			return (org.radixware.schemas.types.NumDocument)org.radixware.kernel.common.utils.XmlObjectProcessor.castToXmlClass(getClass().getClassLoader(),xmlResponse,org.radixware.schemas.types.NumDocument.class);
		}

	}

	public static class EditKeystoreAliases extends org.radixware.kernel.common.client.models.items.Command{
		protected EditKeystoreAliases(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public void send(org.radixware.kernel.common.types.Id propertyId) throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),propertyId,null);
			processResponse(response, propertyId);
		}

	}



}

/* Radix::System::Unit.Arte - Web Meta*/

/*Radix::System::Unit.Arte-Application Class*/

package org.radixware.ads.System.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Unit.Arte_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::System::Unit.Arte:Presentations-Entity Object Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aclFKCYABVIK7OBDCJAAALOMT5GDM"),
			"Radix::System::Unit.Arte",
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),
			null,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4EUVQOK4OXOBDFKUAAMPGXUWTQ"),null,null,0,

			/*Radix::System::Unit.Arte:Properties-Properties*/
			new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

					/*Radix::System::Unit.Arte:highArteInstCount:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colU77W6DFOK7OBDCJAAALOMT5GDM"),
						"highArteInstCount",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWQUFQOK4OXOBDFKUAAMPGXUWTQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclFKCYABVIK7OBDCJAAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.DETAIL_PROP,
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

						/*Radix::System::Unit.Arte:highArteInstCount:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(0L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Unit.Arte:sapId:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colUT7W6DFOK7OBDCJAAALOMT5GDM"),
						"sapId",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWIUFQOK4OXOBDFKUAAMPGXUWTQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclFKCYABVIK7OBDCJAAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.DETAIL_PROP,
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

						/*Radix::System::Unit.Arte:sapId:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Unit.Arte:serviceUri:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colUX7W6DFOK7OBDCJAAALOMT5GDM"),
						"serviceUri",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWEUFQOK4OXOBDFKUAAMPGXUWTQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclFKCYABVIK7OBDCJAAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.DETAIL_PROP,
						20,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ON_CREATE,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::System::Unit.Arte:serviceUri:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,250,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Unit.Arte:sap:PropertyPresentation-Parent Ref Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colZVIWAULWLPOBDCJEAALOMT5GDM"),
						"sap",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclFKCYABVIK7OBDCJAAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.PARENT_REF,
						org.radixware.kernel.common.enums.EPropNature.DETAIL_PROP,
						20,
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
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecR7FXMYDVVHWDBROXAAIT4AGD7E"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblR7FXMYDVVHWDBROXAAIT4AGD7E"),
						null,
						null,
						null,
						262143,
						262143,false),

					/*Radix::System::Unit.Arte:service:PropertyPresentation-Parent Ref Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd4BMIX7VU2FEYVHRPZNE2U4HW4A"),
						"service",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6N6MJFIM4JA7RAXGRCX2S7E5DY"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclFKCYABVIK7OBDCJAAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.PARENT_REF,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
						4,
						null,
						null,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ON_CREATE,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,
						null,
						null,
						null,
						null,
						true,-1,-1,1,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecC2OWQGDVVHWDBROXAAIT4AGD7E"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblC2OWQGDVVHWDBROXAAIT4AGD7E"),
						null,
						null,
						null,
						16439,
						20,false),

					/*Radix::System::Unit.Arte:service.Accessibility:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colY3MB2SZHCVGOTM5YOMBYKEEQDQ"),
						"service.Accessibility",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclFKCYABVIK7OBDCJAAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.CHAR,
						org.radixware.kernel.common.enums.EPropNature.PARENT_PROP,
						4,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecC2OWQGDVVHWDBROXAAIT4AGD7E"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("colXXBGA3KXGZEP5EQS26UU6KWTEA"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acs3A7RUNYYRJCKPBZVNZD23FALXI"),
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

						/*Radix::System::Unit.Arte:service.Accessibility:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acs3A7RUNYYRJCKPBZVNZD23FALXI"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.NONE,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Unit.Arte:keyAliases:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdZA2BGW6W7NBHREFDUWD4BURWTY"),
						"keyAliases",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclFKCYABVIK7OBDCJAAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.ARR_STR,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
						4,
						null,
						null,
						null,
						true,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,true,
						false,
						false,
						null,
						false,

						/*Radix::System::Unit.Arte:keyAliases:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMRC36O2AA5AF7CXOJGUJXAZKZQ"),
						null,
						null,
						false,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Unit.Arte:trustedCertificateAliases:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdI74BB6ZV6RHTFP4KBURNEVRD3U"),
						"trustedCertificateAliases",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclFKCYABVIK7OBDCJAAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.ARR_STR,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
						4,
						null,
						null,
						null,
						true,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,true,
						false,
						false,
						null,
						false,

						/*Radix::System::Unit.Arte:trustedCertificateAliases:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXK5GM5FUHJH6HB4JWKG3OIIKCY"),
						null,
						null,
						false,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Unit.Arte:avgActiveArteCount:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col4Z6HA6NC5ZADBCPDEBBNET2IRA"),
						"avgActiveArteCount",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAF5V3G7GNZEABK5MLM2CRLZ7WQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclFKCYABVIK7OBDCJAAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.NUM,
						org.radixware.kernel.common.enums.EPropNature.DETAIL_PROP,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("0"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::System::Unit.Arte:avgActiveArteCount:PropertyPresentation:Edit Options:-Edit Mask Num*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskNum(null,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.DEFAULT,null,null,(byte)-1),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Unit.Arte:threadPriority:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col2I6B5AN7CNEK3KRJDLEDG4UXZY"),
						"threadPriority",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsT2FPRYFMP5DJFCQFGUKLLDYX2U"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclFKCYABVIK7OBDCJAAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.DETAIL_PROP,
						4,
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acsYING7OYUJ3NRDAQSABIFNQAAAE"),
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

						/*Radix::System::Unit.Arte:threadPriority:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acsYING7OYUJ3NRDAQSABIFNQAAAE"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.NONE,null),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDJF32UENEBFT3MP2MM6PTGZY4E"),
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Unit.Arte:sapPropsXml:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdZ7WFELXCVJBLBLM3SIZOXR4KAU"),
						"sapPropsXml",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclFKCYABVIK7OBDCJAAALOMT5GDM"),
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
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::System::Unit.Arte:sapPropsXml:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Unit.Arte:isInAadc:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd7QNFT7PBDJGQTO7Y7O6OL2D4IU"),
						"isInAadc",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclFKCYABVIK7OBDCJAAALOMT5GDM"),
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::System::Unit.Arte:isInAadc:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aclFKCYABVIK7OBDCJAAALOMT5GDM"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Unit.Arte:primaryUnit:PropertyPresentation-Parent Ref Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colT3XHLBWPY5GGRBETJTUPM3WC54"),
						"primaryUnit",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclFKCYABVIK7OBDCJAAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.PARENT_REF,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						55,
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
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl5HP4XTP3EGWDBRCRAAIT4AGD7E"),
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprXCFCDDGTRHNRDB5MAALOMT5GDM")},
						null,
						null,
						66583529,
						66584555,false)
			},
			new org.radixware.kernel.common.client.meta.RadCommandDef[]{
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::System::Unit.Arte:editKeystoreAliases-Property Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdZ44E4AY7YNBKPGRICPK2IVE2BE"),
						"editKeystoreAliases",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclFKCYABVIK7OBDCJAAALOMT5GDM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUDX7BDD4FFHP7GMIFUVFANAMGU"),
						null,
						null,
						org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,
						true,
						false,
						false,
						org.radixware.kernel.common.enums.ECommandScope.PROPERTY,
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("prd4UPD4GSLZ5GEPELEOMVXKLYL2U"),org.radixware.kernel.common.types.Id.Factory.loadFrom("prdFLVEHYM64RC5FHQR26KNKMRK3I"),org.radixware.kernel.common.types.Id.Factory.loadFrom("prdPB4QLAROQVDTPGUJVKFGGWO76I"),org.radixware.kernel.common.types.Id.Factory.loadFrom("prd2WMYZEX6TJGZ5I2B76BRZH7C5U")},
						org.radixware.kernel.common.enums.ECommandAccessibility.ALWAYS),
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::System::Unit.Arte:getLoad-Object Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdB6JWJ2Z7RZBIREDEKBIYLS2G6I"),
						"getLoad",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclFKCYABVIK7OBDCJAAALOMT5GDM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLO6W2ZOBPJBVDNFXM4X3E72HNE"),
						null,
						null,
						org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,
						false,
						false,
						false,
						org.radixware.kernel.common.enums.ECommandScope.OBJECT,
						null,
						org.radixware.kernel.common.enums.ECommandAccessibility.ALWAYS)
			},
			null,
			null,
			null,
			null,
			true,true,false);
}

/* Radix::System::Unit.Arte:Create - Desktop Meta*/

/*Radix::System::Unit.Arte:Create-Editor Presentation*/

package org.radixware.ads.System.explorer;
public final class Create_mi{
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new org.radixware.kernel.common.client.meta.RadEditorPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epr2NLJ6YFMK7OBDCJAAALOMT5GDM"),
	"Create",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
	org.radixware.kernel.common.types.Id.Factory.loadFrom("eprXCFCDDGTRHNRDB5MAALOMT5GDM"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("aclFKCYABVIK7OBDCJAAALOMT5GDM"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl5HP4XTP3EGWDBRCRAAIT4AGD7E"),
	null,
	null,

	/*Radix::System::Unit.Arte:Create:Editor Pages-Editor Presentation Pages*/
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

			/*Radix::System::Unit.Arte:Create:SAP-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadCustomEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epg4EOJB4NOK7OBDCJAAALOMT5GDM"),"SAP",org.radixware.kernel.common.types.Id.Factory.loadFrom("aclFKCYABVIK7OBDCJAAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAUUVQOK4OXOBDFKUAAMPGXUWTQ"),null,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("cepP2LIUXEW35FX5MDGJROQV7F2CE")),

			/*Radix::System::Unit.Arte:Create:Additional-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgMOBNTCKSSZA7LDNYGVDW27NF4E"),"Additional",org.radixware.kernel.common.types.Id.Factory.loadFrom("aclFKCYABVIK7OBDCJAAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7JJ5W4XH3NC73HGMVD7LWUROAU"),null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd4BMIX7VU2FEYVHRPZNE2U4HW4A"),0,0,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col2I6B5AN7CNEK3KRJDLEDG4UXZY"),0,1,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colU77W6DFOK7OBDCJAAALOMT5GDM"),0,2,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col4Z6HA6NC5ZADBCPDEBBNET2IRA"),0,3,1,false,false)
			},null)
	},
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgOCDVZAOTBXOBDNTPAAMPGXSZKU")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgMOBNTCKSSZA7LDNYGVDW27NF4E")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epg4EOJB4NOK7OBDCJAAALOMT5GDM")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgUJB2RCH6KZHD5IMTQTE76DQSZA"))}
	,

	/*Radix::System::Unit.Arte:Create:Children-Explorer Items*/
		null
	,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
	null,
	0,
	null,
	(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
	3216,0,0);
}
/* Radix::System::Unit.Arte:Create - Web Meta*/

/*Radix::System::Unit.Arte:Create-Editor Presentation*/

package org.radixware.ads.System.web;
public final class Create_mi{
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new org.radixware.kernel.common.client.meta.RadEditorPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epr2NLJ6YFMK7OBDCJAAALOMT5GDM"),
	"Create",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
	org.radixware.kernel.common.types.Id.Factory.loadFrom("eprXCFCDDGTRHNRDB5MAALOMT5GDM"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("aclFKCYABVIK7OBDCJAAALOMT5GDM"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl5HP4XTP3EGWDBRCRAAIT4AGD7E"),
	null,
	null,

	/*Radix::System::Unit.Arte:Create:Editor Pages-Editor Presentation Pages*/
	null,
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
	}
	,

	/*Radix::System::Unit.Arte:Create:Children-Explorer Items*/
		null
	,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
	null,
	0,
	null,
	(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
	3216,0,0);
}
/* Radix::System::Unit.Arte:Create:Model - Desktop Executable*/

/*Radix::System::Unit.Arte:Create:Model-Entity Model Class*/

package org.radixware.ads.System.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit.Arte:Create:Model")
public class Create:Model  extends org.radixware.ads.System.explorer.Unit.Arte.Unit.Arte_DefaultModel.eprXCFCDDGTRHNRDB5MAALOMT5GDM_ModelAdapter {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return Create:Model_mi.rdxMeta; }



	public Create:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadEditorPresentationDef def){super(userSession,def);}

	/*Radix::System::Unit.Arte:Create:Model:Nested classes-Nested Classes*/

	/*Radix::System::Unit.Arte:Create:Model:Properties-Properties*/

	/*Radix::System::Unit.Arte:Create:Model:sapEditor-Dynamic Property*/



	protected org.radixware.ads.System.common_client.ISapEditorModel sapEditor=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit.Arte:Create:Model:sapEditor")
	protected published  org.radixware.ads.System.common_client.ISapEditorModel getSapEditor() {
		return sapEditor;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit.Arte:Create:Model:sapEditor")
	protected published   void setSapEditor(org.radixware.ads.System.common_client.ISapEditorModel val) {
		sapEditor = val;
	}

	/*Radix::System::Unit.Arte:Create:Model:service-Presentation Property*/




	public class Service extends org.radixware.ads.System.explorer.Unit.Arte.Unit.Arte_DefaultModel.eprXCFCDDGTRHNRDB5MAALOMT5GDM_ModelAdapter.prd4BMIX7VU2FEYVHRPZNE2U4HW4A{
		public Service(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadParentRefPropertyDef def){
			super(model,def);
		}
		@Override
		public 
		org.radixware.ads.System.explorer.Service.Service_DefaultModel openEntityModel()  throws org.radixware.kernel.common.exceptions.ServiceClientException, InterruptedException{
		     return (org.radixware.ads.System.explorer.Service.Service_DefaultModel)super.openEntityModel();
		}
		@Override
		public 
		org.radixware.ads.System.explorer.Service.DefaultGroupModel openGroupModel(){
		     return (org.radixware.ads.System.explorer.Service.DefaultGroupModel)super.openGroupModel();
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.client.types.Reference dummy = ((org.radixware.kernel.common.client.types.Reference)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit.Arte:Create:Model:service")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit.Arte:Create:Model:service")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {

			internal[service] = val;
			updateSapAcessibilityEditable();
		}
	}
	public Service getService(){return (Service)getProperty(prd4BMIX7VU2FEYVHRPZNE2U4HW4A);}








	/*Radix::System::Unit.Arte:Create:Model:Methods-Methods*/

	/*Radix::System::Unit.Arte:Create:Model:sapEditorPageOpened-Presentation Slot Method*/

	@org.radixware.kernel.common.lang.ReflectiveCallable
	@SuppressWarnings("unused")
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit.Arte:Create:Model:sapEditorPageOpened")
	public published  void sapEditorPageOpened (com.trolltech.qt.gui.QWidget widget) {
		sapEditor = ClientSapUtils.initSapEditor(sapId.Value, sapPropsXml, widget);
		sapEditor.setAvailableCertAliases(trustedCertificateAliases.Value);
		sapEditor.setAvailableKeyAliases(keyAliases.Value);
		ClientSapUtils.configureEditorForUnitServiceSap(sapEditor);
		updateSapAcessibilityEditable();
		sapEditor.setParentPage(getEditorPage(idof[Unit.Arte:Create:SAP]));

	}

	/*Radix::System::Unit.Arte:Create:Model:updateSapAcessibilityEditable-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit.Arte:Create:Model:updateSapAcessibilityEditable")
	public published  void updateSapAcessibilityEditable () {
		if (sapEditor != null) {
		    sapEditor.setAccessibilityEdtable(service.Accessibility.Value == null);
		    sapEditor.setService(serviceUri.Value);
		}
	}

	/*Radix::System::Unit.Arte:Create:Model:sapEditorPageClosed-Presentation Slot Method*/

	@org.radixware.kernel.common.lang.ReflectiveCallable
	@SuppressWarnings("unused")
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit.Arte:Create:Model:sapEditorPageClosed")
	public published  void sapEditorPageClosed () {
		sapEditor = null;
	}

	/*Radix::System::Unit.Arte:Create:Model:beforeCreate-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit.Arte:Create:Model:beforeCreate")
	protected published  boolean beforeCreate () throws org.radixware.kernel.common.client.exceptions.ModelException {
		return checkSapEditorsBeforeCreate() &&  super.beforeCreate();
	}

	/*Radix::System::Unit.Arte:Create:Model:checkSapEditorsBeforeCreate-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit.Arte:Create:Model:checkSapEditorsBeforeCreate")
	protected published  boolean checkSapEditorsBeforeCreate () {
		if (sapEditor == null) {
		    getEditorPage(idof[Unit.Arte:Create:SAP]).setFocused();
		    try {
		        checkPropertyValues();
		    } catch (Exception ex) {
		        showException(ex);
		        return false;
		    }
		}
		return true;

	}

	/*Radix::System::Unit.Arte:Create:Model:updateVisibility-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit.Arte:Create:Model:updateVisibility")
	protected published  void updateVisibility () {
		super.updateVisibility();
		primaryUnit.setVisible(isInAadc.Value == true);
	}


}

/* Radix::System::Unit.Arte:Create:Model - Desktop Meta*/

/*Radix::System::Unit.Arte:Create:Model-Entity Model Class*/

package org.radixware.ads.System.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Create:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aem2NLJ6YFMK7OBDCJAAALOMT5GDM"),
						"Create:Model",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aemXCFCDDGTRHNRDB5MAALOMT5GDM"),
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::System::Unit.Arte:Create:Model:Properties-Properties*/
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

/* Radix::System::Unit.Arte:Create:Model - Web Executable*/

/*Radix::System::Unit.Arte:Create:Model-Entity Model Class*/

package org.radixware.ads.System.web;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit.Arte:Create:Model")
public class Create:Model  extends org.radixware.ads.System.web.Unit.Arte.Unit.Arte_DefaultModel.eprXCFCDDGTRHNRDB5MAALOMT5GDM_ModelAdapter {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return Create:Model_mi.rdxMeta; }



	public Create:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadEditorPresentationDef def){super(userSession,def);}

	/*Radix::System::Unit.Arte:Create:Model:Nested classes-Nested Classes*/

	/*Radix::System::Unit.Arte:Create:Model:Properties-Properties*/

	/*Radix::System::Unit.Arte:Create:Model:sapEditor-Dynamic Property*/



	protected org.radixware.ads.System.common_client.ISapEditorModel sapEditor=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit.Arte:Create:Model:sapEditor")
	protected published  org.radixware.ads.System.common_client.ISapEditorModel getSapEditor() {
		return sapEditor;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit.Arte:Create:Model:sapEditor")
	protected published   void setSapEditor(org.radixware.ads.System.common_client.ISapEditorModel val) {
		sapEditor = val;
	}

	/*Radix::System::Unit.Arte:Create:Model:service-Presentation Property*/




	public class Service extends org.radixware.ads.System.web.Unit.Arte.Unit.Arte_DefaultModel.eprXCFCDDGTRHNRDB5MAALOMT5GDM_ModelAdapter.prd4BMIX7VU2FEYVHRPZNE2U4HW4A{
		public Service(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadParentRefPropertyDef def){
			super(model,def);
		}
		@Override
		public 
		org.radixware.ads.System.web.Service.Service_DefaultModel openEntityModel()  throws org.radixware.kernel.common.exceptions.ServiceClientException, InterruptedException{
		     return (org.radixware.ads.System.web.Service.Service_DefaultModel)super.openEntityModel();
		}
		@Override
		public 
		org.radixware.ads.System.web.Service.DefaultGroupModel openGroupModel(){
		     return (org.radixware.ads.System.web.Service.DefaultGroupModel)super.openGroupModel();
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.client.types.Reference dummy = ((org.radixware.kernel.common.client.types.Reference)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit.Arte:Create:Model:service")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit.Arte:Create:Model:service")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {

			internal[service] = val;
			updateSapAcessibilityEditable();
		}
	}
	public Service getService(){return (Service)getProperty(prd4BMIX7VU2FEYVHRPZNE2U4HW4A);}








	/*Radix::System::Unit.Arte:Create:Model:Methods-Methods*/

	/*Radix::System::Unit.Arte:Create:Model:updateSapAcessibilityEditable-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit.Arte:Create:Model:updateSapAcessibilityEditable")
	public published  void updateSapAcessibilityEditable () {
		if (sapEditor != null) {
		    sapEditor.setAccessibilityEdtable(service.Accessibility.Value == null);
		    sapEditor.setService(serviceUri.Value);
		}
	}

	/*Radix::System::Unit.Arte:Create:Model:beforeCreate-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit.Arte:Create:Model:beforeCreate")
	protected published  boolean beforeCreate () throws org.radixware.kernel.common.client.exceptions.ModelException {
		return checkSapEditorsBeforeCreate() &&  super.beforeCreate();
	}

	/*Radix::System::Unit.Arte:Create:Model:checkSapEditorsBeforeCreate-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit.Arte:Create:Model:checkSapEditorsBeforeCreate")
	protected published  boolean checkSapEditorsBeforeCreate () {
		if (sapEditor == null) {
		    getEditorPage(idof[Unit.Arte:Create:SAP]).setFocused();
		    try {
		        checkPropertyValues();
		    } catch (Exception ex) {
		        showException(ex);
		        return false;
		    }
		}
		return true;

	}

	/*Radix::System::Unit.Arte:Create:Model:updateVisibility-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit.Arte:Create:Model:updateVisibility")
	protected published  void updateVisibility () {
		super.updateVisibility();
		primaryUnit.setVisible(isInAadc.Value == true);
	}


}

/* Radix::System::Unit.Arte:Create:Model - Web Meta*/

/*Radix::System::Unit.Arte:Create:Model-Entity Model Class*/

package org.radixware.ads.System.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Create:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aem2NLJ6YFMK7OBDCJAAALOMT5GDM"),
						"Create:Model",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aemXCFCDDGTRHNRDB5MAALOMT5GDM"),
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::System::Unit.Arte:Create:Model:Properties-Properties*/
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

/* Radix::System::Unit.Arte:Create:SAP:View - Desktop Executable*/

/*Radix::System::Unit.Arte:Create:SAP:View-Custom Page Editor for Desktop*/

package org.radixware.ads.System.explorer;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit.Arte:Create:SAP:View")
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
		EditorPageView.setGeometry(new com.trolltech.qt.core.QRect(0, 0, 242, 192));
		EditorPageView.setFont(DEFAULT_FONT);
		final com.trolltech.qt.gui.QVBoxLayout $layout1 = new com.trolltech.qt.gui.QVBoxLayout(EditorPageView);
		$layout1.setObjectName("verticalLayout");
		$layout1.setContentsMargins(9, 9, 9, 9);
		$layout1.setSizeConstraint(com.trolltech.qt.gui.QLayout.SizeConstraint.SetDefaultConstraint);
		$layout1.setSpacing(6);
		EditorPageView.opened.connect(model, "mthQYGE7TLCYJEZRIUF46CSFIMCGU(com.trolltech.qt.gui.QWidget)");
		EditorPageView.closed.connect(model, "mthNXSCQRQI3BADXDERASDCLOUJLA()");
		opened.emit(this);
	}
	public final org.radixware.ads.System.explorer.Create:Model getModel() {
		return (org.radixware.ads.System.explorer.Create:Model) super.getModel();
	}

}

/* Radix::System::Unit.Arte:Create:SAP:WebView - Web Executable*/

/*Radix::System::Unit.Arte:Create:SAP:WebView-Rwt Custom Page Editor*/

package org.radixware.ads.System.web;
@SuppressWarnings({"unchecked","rawtypes"})
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit.Arte:Create:SAP:WebView")
public class WebView extends org.radixware.wps.views.editor.EditorPageView{
	public WebView widget;
	public WebView getWidget(){ return  widget;}
	public WebView(org.radixware.kernel.common.client.IClientEnvironment env,org.radixware.kernel.common.client.views.IView view
	,org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef page){
		super(env,view,page);
	}
	public void open(org.radixware.kernel.common.client.models.Model model){
		super.open(model);
		//============ Radix::System::Unit.Arte:Create:SAP:WebView:widget ==============
		this.widget = this;
		fireOpened();
	}
}

/* Radix::System::Unit.Arte:Edit - Desktop Meta*/

/*Radix::System::Unit.Arte:Edit-Editor Presentation*/

package org.radixware.ads.System.explorer;
public final class Edit_mi{
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new org.radixware.kernel.common.client.meta.RadEditorPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprZZ6KB4TXLPOBDCJEAALOMT5GDM"),
	"Edit",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
	org.radixware.kernel.common.types.Id.Factory.loadFrom("epr2NLJ6YFMK7OBDCJAAALOMT5GDM"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("aclFKCYABVIK7OBDCJAAALOMT5GDM"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl5HP4XTP3EGWDBRCRAAIT4AGD7E"),
	null,
	null,

	/*Radix::System::Unit.Arte:Edit:Editor Pages-Editor Presentation Pages*/
	null,
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgOCDVZAOTBXOBDNTPAAMPGXSZKU")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgMOBNTCKSSZA7LDNYGVDW27NF4E")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epg4EOJB4NOK7OBDCJAAALOMT5GDM")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgUJB2RCH6KZHD5IMTQTE76DQSZA")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgJ4BHY7HBLBBBXIIQZEPCKTW2MU"))}
	,

	/*Radix::System::Unit.Arte:Edit:Children-Explorer Items*/
		null
	,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
	null,
	2152,
	null,
	(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
	3216,0,0);
}
/* Radix::System::Unit.Arte:Edit - Web Meta*/

/*Radix::System::Unit.Arte:Edit-Editor Presentation*/

package org.radixware.ads.System.web;
public final class Edit_mi{
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new org.radixware.kernel.common.client.meta.RadEditorPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprZZ6KB4TXLPOBDCJEAALOMT5GDM"),
	"Edit",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
	org.radixware.kernel.common.types.Id.Factory.loadFrom("epr2NLJ6YFMK7OBDCJAAALOMT5GDM"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("aclFKCYABVIK7OBDCJAAALOMT5GDM"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl5HP4XTP3EGWDBRCRAAIT4AGD7E"),
	null,
	null,

	/*Radix::System::Unit.Arte:Edit:Editor Pages-Editor Presentation Pages*/
	null,
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
	}
	,

	/*Radix::System::Unit.Arte:Edit:Children-Explorer Items*/
		null
	,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
	null,
	2152,
	null,
	(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
	3216,0,0);
}
/* Radix::System::Unit.Arte:Edit:Model - Desktop Executable*/

/*Radix::System::Unit.Arte:Edit:Model-Entity Model Class*/

package org.radixware.ads.System.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit.Arte:Edit:Model")
public class Edit:Model  extends org.radixware.ads.System.explorer.Create:Model  {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return Edit:Model_mi.rdxMeta; }



	public Edit:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadEditorPresentationDef def){super(userSession,def);}

	/*Radix::System::Unit.Arte:Edit:Model:Nested classes-Nested Classes*/

	/*Radix::System::Unit.Arte:Edit:Model:Properties-Properties*/

	/*Radix::System::Unit.Arte:Edit:Model:Methods-Methods*/

	/*Radix::System::Unit.Arte:Edit:Model:onCommand_getLoad-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit.Arte:Edit:Model:onCommand_getLoad")
	public  void onCommand_getLoad (org.radixware.ads.System.explorer.Unit.Arte.GetLoad command) {
		//Test for RADIX-7311
		try {
		    org.radixware.schemas.types.StrDocument strDoc = org.radixware.schemas.types.StrDocument.Factory.newInstance();
		    strDoc.Str = "load";
		    org.radixware.schemas.types.NumDocument rsDoc = command.send(strDoc);
		    getEnvironment().messageInformation("Load", String.valueOf(rsDoc.Num));
		} catch (Exception ex) {
		    getEnvironment().messageException("Error", "Error", ex);
		}
	}
	public final class GetLoad extends org.radixware.ads.System.explorer.Unit.Arte.GetLoad{
		protected GetLoad(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_getLoad( this );
		}

	}













}

/* Radix::System::Unit.Arte:Edit:Model - Desktop Meta*/

/*Radix::System::Unit.Arte:Edit:Model-Entity Model Class*/

package org.radixware.ads.System.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Edit:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aemZZ6KB4TXLPOBDCJEAALOMT5GDM"),
						"Edit:Model",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aem2NLJ6YFMK7OBDCJAAALOMT5GDM"),
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::System::Unit.Arte:Edit:Model:Properties-Properties*/
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

/* Radix::System::Unit.Arte:Edit:Model - Web Executable*/

/*Radix::System::Unit.Arte:Edit:Model-Entity Model Class*/

package org.radixware.ads.System.web;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit.Arte:Edit:Model")
public class Edit:Model  extends org.radixware.ads.System.web.Create:Model  {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return Edit:Model_mi.rdxMeta; }



	public Edit:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadEditorPresentationDef def){super(userSession,def);}

	/*Radix::System::Unit.Arte:Edit:Model:Nested classes-Nested Classes*/

	/*Radix::System::Unit.Arte:Edit:Model:Properties-Properties*/

	/*Radix::System::Unit.Arte:Edit:Model:Methods-Methods*/

	/*Radix::System::Unit.Arte:Edit:Model:onCommand_getLoad-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit.Arte:Edit:Model:onCommand_getLoad")
	public  void onCommand_getLoad (org.radixware.ads.System.web.Unit.Arte.GetLoad command) {
		//Test for RADIX-7311
		try {
		    org.radixware.schemas.types.StrDocument strDoc = org.radixware.schemas.types.StrDocument.Factory.newInstance();
		    strDoc.Str = "load";
		    org.radixware.schemas.types.NumDocument rsDoc = command.send(strDoc);
		    getEnvironment().messageInformation("Load", String.valueOf(rsDoc.Num));
		} catch (Exception ex) {
		    getEnvironment().messageException("Error", "Error", ex);
		}
	}
	public final class GetLoad extends org.radixware.ads.System.web.Unit.Arte.GetLoad{
		protected GetLoad(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_getLoad( this );
		}

	}













}

/* Radix::System::Unit.Arte:Edit:Model - Web Meta*/

/*Radix::System::Unit.Arte:Edit:Model-Entity Model Class*/

package org.radixware.ads.System.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Edit:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aemZZ6KB4TXLPOBDCJEAALOMT5GDM"),
						"Edit:Model",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aem2NLJ6YFMK7OBDCJAAALOMT5GDM"),
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::System::Unit.Arte:Edit:Model:Properties-Properties*/
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

/* Radix::System::Unit.Arte - Localizing Bundle */
package org.radixware.ads.System.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Unit.Arte - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Error reading the key aliases from the keystore: ");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ошибка чтения списка ключей из хранилища: ");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4CFH2IVWG5FIHLQLUDFBAQ5AK4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"ARTE");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"ARTE");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4EUVQOK4OXOBDFKUAAMPGXUWTQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Error reading the key aliases from the keystore: ");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ошибка чтения списка ключей из хранилища: ");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls53ZESOCQL5AL5NHOXQ6UAV3ADY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Implemented service");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Реализуемый сервис");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6N6MJFIM4JA7RAXGRCX2S7E5DY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Additional");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Дополнительно");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7JJ5W4XH3NC73HGMVD7LWUROAU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Average number of ARTEs in use");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Среднее количество используемых ARTE");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAF5V3G7GNZEABK5MLM2CRLZ7WQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"SAP Parameters");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Параметры точки доступа");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAUUVQOK4OXOBDFKUAAMPGXUWTQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<normal>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<нормальный>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDJF32UENEBFT3MP2MM6PTGZY4E"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Error reading the trusted certificate aliases from the keystore: ");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ошибка чтения списка доверенных сертификатов из хранилища: ");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEVVBLU26KFEMNHWSJQF2N45NQ4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"GetLoad");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"GetLoad");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLO6W2ZOBPJBVDNFXM4X3E72HNE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<any>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<любые>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMRC36O2AA5AF7CXOJGUJXAZKZQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Default priority for ARTE requests");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Приоритет по умолчанию для ARTE запросов");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsT2FPRYFMP5DJFCQFGUKLLDYX2U"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Edit");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Редактировать");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUDX7BDD4FFHP7GMIFUVFANAMGU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Service URI");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"URI сервиса");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWEUFQOK4OXOBDFKUAAMPGXUWTQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"SAP ID");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ид. SAP");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWIUFQOK4OXOBDFKUAAMPGXUWTQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Maximum number of ARTEs");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Максимальное число ARTE");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWQUFQOK4OXOBDFKUAAMPGXUWTQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Error reading the trusted certificate aliases from the keystore:");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ошибка чтения списка доверенных сертификатов из хранилища: ");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXANRSLAJV5EHBFDC5GOEWWKVNE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<any>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<любые>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXK5GM5FUHJH6HB4JWKG3OIIKCY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Test for RADIX-7311 (hidden but left as example of implementation)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZT6Z4PO5SZFVXABXDSGIWQGCWA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH),null));
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(Unit.Arte - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbaclFKCYABVIK7OBDCJAAALOMT5GDM"),"Unit.Arte - Localizing Bundle",$$$items$$$);
}
