
/* Radix::PersoComm::Unit.Channel.DeliveryAck - Server Executable*/

/*Radix::PersoComm::Unit.Channel.DeliveryAck-Application Class*/

package org.radixware.ads.PersoComm.server;

import java.util.Collections;


@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.DeliveryAck")
public class Unit.Channel.DeliveryAck  extends org.radixware.ads.PersoComm.server.Unit.Channel.AbstractBase  implements org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {


	/**Metainformation accessor method*/
	@SuppressWarnings("unused")
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return Unit.Channel.DeliveryAck_mi.rdxMeta;}

	/*Radix::PersoComm::Unit.Channel.DeliveryAck:Nested classes-Nested Classes*/

	/*Radix::PersoComm::Unit.Channel.DeliveryAck:Properties-Properties*/

	/*Radix::PersoComm::Unit.Channel.DeliveryAck:deletedSapId-Dynamic Property*/



	protected Int deletedSapId=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.DeliveryAck:deletedSapId")
	public published  Int getDeletedSapId() {
		return deletedSapId;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.DeliveryAck:deletedSapId")
	public published   void setDeletedSapId(Int val) {
		deletedSapId = val;
	}

	/*Radix::PersoComm::Unit.Channel.DeliveryAck:keyAliases-Dynamic Property*/



	protected org.radixware.kernel.common.types.ArrStr keyAliases=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.DeliveryAck:keyAliases")
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
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.DeliveryAck:keyAliases")
	public published   void setKeyAliases(org.radixware.kernel.common.types.ArrStr val) {
		keyAliases = val;
	}

	/*Radix::PersoComm::Unit.Channel.DeliveryAck:sapPropsXml-Dynamic Property*/



	protected Str sapPropsXml=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.DeliveryAck:sapPropsXml")
	public published  Str getSapPropsXml() {
		return sapPropsXml;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.DeliveryAck:sapPropsXml")
	public published   void setSapPropsXml(Str val) {
		sapPropsXml = val;
	}

	/*Radix::PersoComm::Unit.Channel.DeliveryAck:trustedCertificateAliases-Dynamic Property*/



	protected org.radixware.kernel.common.types.ArrStr trustedCertificateAliases=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.DeliveryAck:trustedCertificateAliases")
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
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.DeliveryAck:trustedCertificateAliases")
	public published   void setTrustedCertificateAliases(org.radixware.kernel.common.types.ArrStr val) {
		trustedCertificateAliases = val;
	}

	/*Radix::PersoComm::Unit.Channel.DeliveryAck:deliveryAckSapId-Detail Column Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.DeliveryAck:deliveryAckSapId")
	public  Int getDeliveryAckSapId() {
		return deliveryAckSapId;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.DeliveryAck:deliveryAckSapId")
	public   void setDeliveryAckSapId(Int val) {
		deliveryAckSapId = val;
	}

	/*Radix::PersoComm::Unit.Channel.DeliveryAck:sap-Detail Reference*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.DeliveryAck:sap")
	public  org.radixware.ads.System.server.Sap getSap() {
		return sap;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.DeliveryAck:sap")
	public   void setSap(org.radixware.ads.System.server.Sap val) {
		sap = val;
	}

	/*Radix::PersoComm::Unit.Channel.DeliveryAck:type-Column-Based Property*/




	/*Radix::PersoComm::Unit.Channel.DeliveryAck:serviceAccessibility-Dynamic Property*/



	protected org.radixware.kernel.common.enums.EServiceAccessibility serviceAccessibility=org.radixware.kernel.common.enums.EServiceAccessibility.getForValue((Character)org.radixware.kernel.common.defs.value.ValAsStr.fromStr("I",org.radixware.kernel.common.enums.EValType.CHAR));











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.DeliveryAck:serviceAccessibility")
	public  org.radixware.kernel.common.enums.EServiceAccessibility getServiceAccessibility() {
		return serviceAccessibility;
	}

	/*Radix::PersoComm::Unit.Channel.DeliveryAck:serviceWsdl-Dynamic Property*/



	protected static Str serviceWsdl=(Str)org.radixware.kernel.common.defs.value.ValAsStr.fromStr("http://schemas.radixware.org/persocomm-delivery.wsdl",org.radixware.kernel.common.enums.EValType.STR);











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.DeliveryAck:serviceWsdl")
	public static  Str getServiceWsdl() {
		return serviceWsdl;
	}

	/*Radix::PersoComm::Unit.Channel.DeliveryAck:serviceUri-Dynamic Property*/



	protected Str serviceUri=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.DeliveryAck:serviceUri")
	public  Str getServiceUri() {

		return serviceWsdl + "#" + id;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.DeliveryAck:serviceUri")
	public   void setServiceUri(Str val) {
		serviceUri = val;
	}

	/*Radix::PersoComm::Unit.Channel.DeliveryAck:undefinedSapAddress-Dynamic Property*/



	protected Str undefinedSapAddress=(Str)org.radixware.kernel.common.defs.value.ValAsStr.fromStr("?",org.radixware.kernel.common.enums.EValType.STR);











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.DeliveryAck:undefinedSapAddress")
	protected  Str getUndefinedSapAddress() {
		return undefinedSapAddress;
	}

	/*Radix::PersoComm::Unit.Channel.DeliveryAck:currSapId-Dynamic Property*/



	protected Int currSapId=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.DeliveryAck:currSapId")
	public published  Int getCurrSapId() {

		return deliveryAckSapId;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.DeliveryAck:currSapId")
	public published   void setCurrSapId(Int val) {
		currSapId = val;
	}

	/*Radix::PersoComm::Unit.Channel.DeliveryAck:sap_AccessibilityEditable-Dynamic Property*/



	protected Bool sap_AccessibilityEditable=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.DeliveryAck:sap_AccessibilityEditable")
	public published  Bool getSap_AccessibilityEditable() {

		return serviceAccessibility == null; // !!!! false

	}

	/*Radix::PersoComm::Unit.Channel.DeliveryAck:kind-Detail Column Property*/




	/*Radix::PersoComm::Unit.Channel.DeliveryAck:sendAddress-Detail Column Property*/




	/*Radix::PersoComm::Unit.Channel.DeliveryAck:recvAddress-Detail Column Property*/
















































































































	/*Radix::PersoComm::Unit.Channel.DeliveryAck:Methods-Methods*/

	/*Radix::PersoComm::Unit.Channel.DeliveryAck:copyScpToSapLinks-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.DeliveryAck:copyScpToSapLinks")
	protected  void copyScpToSapLinks (org.radixware.kernel.server.types.Entity src) {
		Int srcSapId = ((Unit.Channel.DeliveryAck) src).deliveryAckSapId;
		if (srcSapId == null) {
		    return;
		}
		System::Scp2SapLinksCursor cursor = System::Scp2SapLinksCursor.open(srcSapId);
		try {
		    while (cursor.next()) {
		        System::Scp2Sap link = new System::Scp2Sap();
		        link.init(null, cursor.scp2Sap);
		        link.sapId = deliveryAckSapId;
		        link.create(cursor.scp2Sap);
		    }
		} finally {
		    cursor.close();
		}
	}

	/*Radix::PersoComm::Unit.Channel.DeliveryAck:getUsedAddresses111-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.DeliveryAck:getUsedAddresses111")
	protected published  java.util.Collection<org.radixware.ads.System.server.AddressInfo> getUsedAddresses111 () {
		if (deliveryAckSapId != null) {
		    return Collections.singletonList(new AddressInfo(instanceId, id, deliveryAckSapId));
		}
		return Collections.emptyList();
	}

	/*Radix::PersoComm::Unit.Channel.DeliveryAck:beforeCreate-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.DeliveryAck:beforeCreate")
	protected  boolean beforeCreate (org.radixware.kernel.server.types.Entity src) {
		Int srcSapId = null;
		if (src != null) {
		    srcSapId = ((Unit.Channel.DeliveryAck) src).deliveryAckSapId;
		    if (srcSapId != null && srcSapId != 0) {
		        deliveryAckSapId = 0;
		        sapPropsXml = System::ServerSapUtils.writePropsForCopy(srcSapId, sapPropsXml);
		    }
		}
		updateService();
		if (srcSapId != null) {
		    System::ServerSapUtils.copyScpLinks(srcSapId, deliveryAckSapId);
		}

		return super.beforeCreate(src);
	}

	/*Radix::PersoComm::Unit.Channel.DeliveryAck:beforeDelete-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.DeliveryAck:beforeDelete")
	protected  boolean beforeDelete () {
		deletedSapId = deliveryAckSapId;
		return super.beforeDelete();
	}

	/*Radix::PersoComm::Unit.Channel.DeliveryAck:beforeUpdate-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.DeliveryAck:beforeUpdate")
	protected published  boolean beforeUpdate () {
		updateService();
		return super.beforeUpdate();
	}

	/*Radix::PersoComm::Unit.Channel.DeliveryAck:deleteService-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.DeliveryAck:deleteService")
	protected published  void deleteService () {
		//Вызывать из afterDelete

		System::Sap sap = System::Sap.loadByPK(deletedSapId, true);
		if (sap != null) {
		    //if (() == true) {
		        System::Service service = sap.service;
		        service.delete();
		    //}
		    //else delete cascade since RADIX-8531
		}

	}

	/*Radix::PersoComm::Unit.Channel.DeliveryAck:isOwn-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.DeliveryAck:isOwn")
	protected published  Bool isOwn () {
		//собственный ли сервис

		return true;
	}

	/*Radix::PersoComm::Unit.Channel.DeliveryAck:needUpdateSap-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.DeliveryAck:needUpdateSap")
	protected published  boolean needUpdateSap () {
		return true;
	}

	/*Radix::PersoComm::Unit.Channel.DeliveryAck:updateSap-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.DeliveryAck:updateSap")
	protected published  void updateSap (org.radixware.ads.System.server.Sap sap) {
		if (sap != null && (sap.address == null || sap.address.isEmpty())) {
		    if (sap.channelType == System::SapChannelType:INTERNAL_PIPE) {
		        sap.address = Utils::AddressUtils.getSapAddress(sap.id);
		    } else {
		        sap.address = undefinedSapAddress;
		    }
		}

		if (sap != null && sap.id == deliveryAckSapId && !java.util.Objects.equals(sap.uri, serviceUri)) {
		    sap.uri = serviceUri;
		}

	}

	/*Radix::PersoComm::Unit.Channel.DeliveryAck:updateService-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.DeliveryAck:updateService")
	protected published  void updateService () {
		//вызывать из beforeCreate()  
		//и из beforeUpdate()

		//if (!()) { // true
		//    return;
		//}

		System::Service service = null;
		System::Sap sap;

		final boolean isSapExists = deliveryAckSapId != null && deliveryAckSapId != 0;

		if (isSapExists) {
		    sap = System::Sap.loadByPK(deliveryAckSapId, true);
		    System::ServerSapUtils.fillProps(sap, sapPropsXml);
		} else {
		//    if (() == false) //если не собственный, пытаемся найти
		//        service = .(1, (), true);

		    if (service == null) {
		        service = new System::Service();
		        service.init();
		        service.systemId = 1;
		        service.accessibility = serviceAccessibility;
		        service.implementedInArte = false;
		        service.uri = serviceUri;
		        service.wsdlUri = serviceWsdl;
		        service.title = "Service of unit #" + id;
		        service.create();
		    }

		    sap = new System::Sap();
		    sap.init();
		    System::ServerSapUtils.fillProps(sap, sapPropsXml);
		    sap.service = service;

		//    if (() != null) {
		        sap.accessibility = serviceAccessibility;
		//    }

		}
		sap.title = System::Sap.createTitleForUnitSap(sap.id, id);
		updateSap(sap);
		sap.isActive = use != null && started != null && use.booleanValue() && started.booleanValue();
		if (isSapExists) {
		    sap.update();
		} else {
		    sap.systemUnitId = id;
		    sap.create();
		    deliveryAckSapId = sap.id;
		}
	}

	/*Radix::PersoComm::Unit.Channel.DeliveryAck:afterDelete-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.DeliveryAck:afterDelete")
	protected published  void afterDelete () {
		deleteService();
		super.afterDelete();
	}


	@Override
	public org.radixware.kernel.server.types.FormHandler.NextDialogsRequest execCommand(org.radixware.kernel.common.types.Id cmdId, org.radixware.kernel.common.types.Id propId, org.apache.xmlbeans.XmlObject input, org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValsById, org.apache.xmlbeans.XmlObject output) throws org.radixware.kernel.common.exceptions.AppException,java.lang.InterruptedException{

			return super.execCommand(cmdId,propId,input,newPropValsById,output);
	}


}

/* Radix::PersoComm::Unit.Channel.DeliveryAck - Server Meta*/

/*Radix::PersoComm::Unit.Channel.DeliveryAck-Application Class*/

package org.radixware.ads.PersoComm.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Unit.Channel.DeliveryAck_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("aclYOEXMYXHGZCJRILV2QNYE7GSKY"),"Unit.Channel.DeliveryAck",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUT7P3U2Q6NAOVI6B7A2MIUKG6U"),

						org.radixware.kernel.common.enums.EClassType.APPLICATION,

						/*Radix::PersoComm::Unit.Channel.DeliveryAck:Presentations-Entity Object Presentations*/
						/*Class presentation attributes*/
						 new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef(/*Owner Class Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("aclYOEXMYXHGZCJRILV2QNYE7GSKY"),
							/*Owner Class Name*/
							"Unit.Channel.DeliveryAck",
							/*Title Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUT7P3U2Q6NAOVI6B7A2MIUKG6U"),
							/*Property presentations*/

							/*Radix::PersoComm::Unit.Channel.DeliveryAck:Properties-Properties*/
							new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef[]{

									/*Radix::PersoComm::Unit.Channel.DeliveryAck:keyAliases:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd4EIRGZ5DIJHLHBJHXIHPUD6PFE"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,true,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::PersoComm::Unit.Channel.DeliveryAck:sapPropsXml:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdTCEJYXCNH5E5ZJO56FB3QY5XFY"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::PersoComm::Unit.Channel.DeliveryAck:trustedCertificateAliases:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdRWQH4YEGVBHB5IUGL3LCWRGXDY"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,true,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::PersoComm::Unit.Channel.DeliveryAck:deliveryAckSapId:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQS73KCGHKBA5LAKVLERC3CIARU"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::PersoComm::Unit.Channel.DeliveryAck:sap:PropertyPresentation-Parent Ref Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadParentTitlePropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNEBX3MILRVH4JJOYEVDU5D62IQ"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,null,null,new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),org.radixware.kernel.server.types.Restrictions.Factory.newInstance(133693439,null,null,null),null,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.TITLE,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_SELECTOR)),

									/*Radix::PersoComm::Unit.Channel.DeliveryAck:type:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEWJT6TIHR5VDBNSIAAUMFADAIA"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.TITLE,org.radixware.kernel.common.enums.EPropAttrInheritance.EDITING,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_SELECT_CONDITION,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_SELECTOR,org.radixware.kernel.common.enums.EPropAttrInheritance.HINT)),

									/*Radix::PersoComm::Unit.Channel.DeliveryAck:serviceAccessibility:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdRI6AO4BL3BFD7CJ7JW2XUHZ62M"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::PersoComm::Unit.Channel.DeliveryAck:serviceUri:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdYPXAP4LRANHV7NTSKIVC5XANVQ"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::PersoComm::Unit.Channel.DeliveryAck:undefinedSapAddress:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdZ7Q3SMDCDNFGZKKTZ6TAZKX42A"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::PersoComm::Unit.Channel.DeliveryAck:currSapId:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd5JDAXHJWTBFYTDMTGQABVPT3VY"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::PersoComm::Unit.Channel.DeliveryAck:sap_AccessibilityEditable:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd3THR2Q236BENBCBJJARMXCFDTA"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::PersoComm::Unit.Channel.DeliveryAck:kind:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col62V3FXUDVTOBDCLTAALOMT5GDM"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.TITLE,org.radixware.kernel.common.enums.EPropAttrInheritance.EDITING,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_SELECT_CONDITION,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_SELECTOR,org.radixware.kernel.common.enums.EPropAttrInheritance.HINT)),

									/*Radix::PersoComm::Unit.Channel.DeliveryAck:sendAddress:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXJZDYDMEVTOBDCLTAALOMT5GDM"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.TITLE,org.radixware.kernel.common.enums.EPropAttrInheritance.EDITING,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_SELECT_CONDITION,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_SELECTOR,org.radixware.kernel.common.enums.EPropAttrInheritance.HINT)),

									/*Radix::PersoComm::Unit.Channel.DeliveryAck:recvAddress:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXNZDYDMEVTOBDCLTAALOMT5GDM"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.TITLE,org.radixware.kernel.common.enums.EPropAttrInheritance.EDITING,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_SELECT_CONDITION,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_SELECTOR,org.radixware.kernel.common.enums.EPropAttrInheritance.HINT))
							},
							/*Commands*/
							null,
							/*Sortings*/
							null,
							/*Filters*/
							null,
							/*Editor presentations*/
							new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef[]{

									new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef(
									/*Radix::PersoComm::Unit.Channel.DeliveryAck:Create-Editor Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprVYRWUXCOHBAQ7FZM25SPC52P4A"),
									"Create",
									org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprGVZBTVH6VXOBDCLVAALOMT5GDM"),
									36016,
									null,

									/*Radix::PersoComm::Unit.Channel.DeliveryAck:Create:Children-Explorer Items*/
										null
									,
									null,
									(org.radixware.kernel.server.meta.presentations.RadPropertyPresentationAttributes[]) null,
									org.radixware.kernel.common.enums.EEditorPresentationRightsInheritanceMode.FROM_REPLACED,
									null),

									new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef(
									/*Radix::PersoComm::Unit.Channel.DeliveryAck:Edit-Editor Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprSFSCKIRJQZB5BOH4ABCZJESOI4"),
									"Edit",
									org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprVYRWUXCOHBAQ7FZM25SPC52P4A"),
									40114,
									null,

									/*Radix::PersoComm::Unit.Channel.DeliveryAck:Edit:Children-Explorer Items*/
										null
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
							org.radixware.kernel.common.utils.Maps.fromArrays(new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprS6PW3X2EKJFUNPGCXYDBACXY7A"),org.radixware.kernel.common.types.Id.Factory.loadFrom("eprGVZBTVH6VXOBDCLVAALOMT5GDM")},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprVYRWUXCOHBAQ7FZM25SPC52P4A"),org.radixware.kernel.common.types.Id.Factory.loadFrom("eprSFSCKIRJQZB5BOH4ABCZJESOI4")}),
							/*Object title format*/
							null,
							/*Class catalogs*/
							new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog[]{

									new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog(
									/*Radix::PersoComm::Unit.Channel.DeliveryAck:Default-Dynamic Class Catalog*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eccKOGFFVPJAHOBDA26AAMPGXSZKU"),org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.ELinkMode.VIRTUAL,new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.Item[]{
										new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.ClassRef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aclYOEXMYXHGZCJRILV2QNYE7GSKY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("cctC4I7K35XCFFEDEJ4E3TVGTP4G4"),167.5,true,org.radixware.kernel.common.types.Id.Factory.loadFrom("aclYOEXMYXHGZCJRILV2QNYE7GSKY"),null)}
									)
							},
							/*Restrictions*/
							null,null,0),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl5HP4XTP3EGWDBRCRAAIT4AGD7E"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclVCLH3TEDVTOBDCLTAALOMT5GDM"),

						/*Radix::PersoComm::Unit.Channel.DeliveryAck:Properties-Properties*/
						new org.radixware.kernel.server.meta.clazzes.RadPropDef[]{

								/*Radix::PersoComm::Unit.Channel.DeliveryAck:deletedSapId-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdEFQ4QV5PAVFKZI3I2EXT67RVK4"),"deletedSapId",null,org.radixware.kernel.common.enums.EValType.INT,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::Unit.Channel.DeliveryAck:keyAliases-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd4EIRGZ5DIJHLHBJHXIHPUD6PFE"),"keyAliases",null,org.radixware.kernel.common.enums.EValType.ARR_STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::Unit.Channel.DeliveryAck:sapPropsXml-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdTCEJYXCNH5E5ZJO56FB3QY5XFY"),"sapPropsXml",null,org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::Unit.Channel.DeliveryAck:trustedCertificateAliases-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdRWQH4YEGVBHB5IUGL3LCWRGXDY"),"trustedCertificateAliases",null,org.radixware.kernel.common.enums.EValType.ARR_STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::Unit.Channel.DeliveryAck:deliveryAckSapId-Detail Column Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDetailPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQS73KCGHKBA5LAKVLERC3CIARU"),"deliveryAckSapId",null,org.radixware.kernel.common.enums.EValType.INT,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("refLJSTGVRFVLOBDCLSAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colLU4DBVH3ZVFFNHZVGWBDA5WGWE"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::Unit.Channel.DeliveryAck:sap-Detail Reference*/

								new org.radixware.kernel.server.meta.clazzes.RadDetailParentRefPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNEBX3MILRVH4JJOYEVDU5D62IQ"),"sap",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5XBVKUK4OXOBDFKUAAMPGXUWTQ"),org.radixware.kernel.common.types.Id.Factory.loadFrom("refLJSTGVRFVLOBDCLSAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("ref3B3IDEGGWBADLHL5DHOGW4BL3E"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecR7FXMYDVVHWDBROXAAIT4AGD7E"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::Unit.Channel.DeliveryAck:type-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEWJT6TIHR5VDBNSIAAUMFADAIA"),"type",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4QUFQOK4OXOBDFKUAAMPGXUWTQ"),org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("acsLSG35D4GIHNRDJIEACQMTAIZT4"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("4014")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::Unit.Channel.DeliveryAck:serviceAccessibility-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdRI6AO4BL3BFD7CJ7JW2XUHZ62M"),"serviceAccessibility",null,org.radixware.kernel.common.enums.EValType.CHAR,org.radixware.kernel.common.types.Id.Factory.loadFrom("acs3A7RUNYYRJCKPBZVNZD23FALXI"),org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("I")),org.radixware.kernel.server.meta.clazzes.RadPropReadAccessorProxy.R_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::Unit.Channel.DeliveryAck:serviceWsdl-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdB45JVWK4FJA5ZG4X2RL4I6VM3Y"),"serviceWsdl",null,org.radixware.kernel.common.enums.EValType.STR,null,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("http://schemas.radixware.org/persocomm-delivery.wsdl")),org.radixware.kernel.server.meta.clazzes.RadPropReadAccessorProxy.R_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::Unit.Channel.DeliveryAck:serviceUri-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdYPXAP4LRANHV7NTSKIVC5XANVQ"),"serviceUri",null,org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::Unit.Channel.DeliveryAck:undefinedSapAddress-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdZ7Q3SMDCDNFGZKKTZ6TAZKX42A"),"undefinedSapAddress",null,org.radixware.kernel.common.enums.EValType.STR,null,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("?")),org.radixware.kernel.server.meta.clazzes.RadPropReadAccessorProxy.R_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::Unit.Channel.DeliveryAck:currSapId-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd5JDAXHJWTBFYTDMTGQABVPT3VY"),"currSapId",null,org.radixware.kernel.common.enums.EValType.INT,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::Unit.Channel.DeliveryAck:sap_AccessibilityEditable-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd3THR2Q236BENBCBJJARMXCFDTA"),"sap_AccessibilityEditable",null,org.radixware.kernel.common.enums.EValType.BOOL,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadAccessorProxy.R_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::Unit.Channel.DeliveryAck:kind-Detail Column Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDetailPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col62V3FXUDVTOBDCLTAALOMT5GDM"),"kind",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZVCKAEK2VXOBDCLUAALOMT5GDM"),org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("acsZ5EEISSCVXOBDCLUAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("refLJSTGVRFVLOBDCLSAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colB2VQZVZFVLOBDCLSAALOMT5GDM"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("DeliveryAck")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::Unit.Channel.DeliveryAck:sendAddress-Detail Column Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDetailPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXJZDYDMEVTOBDCLTAALOMT5GDM"),"sendAddress",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKOCJ3B22VXOBDCLUAALOMT5GDM"),org.radixware.kernel.common.enums.EValType.STR,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("refLJSTGVRFVLOBDCLSAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colWASXULBLVLOBDCLSAALOMT5GDM"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("<not used>")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::Unit.Channel.DeliveryAck:recvAddress-Detail Column Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDetailPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXNZDYDMEVTOBDCLTAALOMT5GDM"),"recvAddress",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsD6UAIN22VXOBDCLUAALOMT5GDM"),org.radixware.kernel.common.enums.EValType.STR,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("refLJSTGVRFVLOBDCLSAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colNOHTUMZLVLOBDCLSAALOMT5GDM"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("<not used>")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE)
						},

						/*Radix::PersoComm::Unit.Channel.DeliveryAck:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthVILVWOB5UJBOHB3ZHDUSTFPOAU"),"copyScpToSapLinks",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("src",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprHNAC7ZG54VBWNHXNTFOWQW26TM"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth5VNWH73FNNBEVEJVDO5ZTCFL64"),"getUsedAddresses111",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,org.radixware.kernel.common.enums.EValType.JAVA_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthFMXUFM5J25E4TNM4PR73F6V3E4"),"beforeCreate",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("src",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprZPY4DXQDJ5CYZLRSJH2N77T5LA"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthKNZ5Z7AS6RFRPIX6ZD5JDKEC34"),"beforeDelete",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthPT5PMP2WVZGUDB7OTLOE2SBWKE"),"beforeUpdate",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthYOIGFK6AHFHOJPGOYWCJONWLAY"),"deleteService",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthAMMOXQRI7RGKRKAQWNOO36VKJM"),"isOwn",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,org.radixware.kernel.common.enums.EValType.BOOL),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthVVF2VHJAU5AJNF7TYQLKEINHT4"),"needUpdateSap",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth4MYLFUHL65C4LBSERE5FQ4ZB2Q"),"updateSap",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("sap",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprJSSGNHKU4JAPFHQXPCHOMVDEEI"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthRNH7KHG5HRBSBHLT2TIEGD6AO4"),"updateService",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth2UCNHZSLQVGXPG7Z4SJQJPPSAY"),"afterDelete",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,null)
						},
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("refLJSTGVRFVLOBDCLSAALOMT5GDM")},
						null,null,null,false);
}

/* Radix::PersoComm::Unit.Channel.DeliveryAck - Desktop Executable*/

/*Radix::PersoComm::Unit.Channel.DeliveryAck-Application Class*/

package org.radixware.ads.PersoComm.explorer;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.DeliveryAck")
public interface Unit.Channel.DeliveryAck   extends org.radixware.ads.PersoComm.explorer.Unit.Channel.AbstractBase  {



































































































































































	/*Radix::PersoComm::Unit.Channel.DeliveryAck:sap:sap-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.DeliveryAck:sap:sap")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.DeliveryAck:sap:sap")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public Sap getSap();
	/*Radix::PersoComm::Unit.Channel.DeliveryAck:deliveryAckSapId:deliveryAckSapId-Presentation Property*/


	public class DeliveryAckSapId extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public DeliveryAckSapId(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.DeliveryAck:deliveryAckSapId:deliveryAckSapId")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.DeliveryAck:deliveryAckSapId:deliveryAckSapId")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public DeliveryAckSapId getDeliveryAckSapId();
	/*Radix::PersoComm::Unit.Channel.DeliveryAck:sap_AccessibilityEditable:sap_AccessibilityEditable-Presentation Property*/


	public class Sap_AccessibilityEditable extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public Sap_AccessibilityEditable(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.DeliveryAck:sap_AccessibilityEditable:sap_AccessibilityEditable")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.DeliveryAck:sap_AccessibilityEditable:sap_AccessibilityEditable")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public Sap_AccessibilityEditable getSap_AccessibilityEditable();
	/*Radix::PersoComm::Unit.Channel.DeliveryAck:keyAliases:keyAliases-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.DeliveryAck:keyAliases:keyAliases")
		public  org.radixware.kernel.common.types.ArrStr getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.DeliveryAck:keyAliases:keyAliases")
		public   void setValue(org.radixware.kernel.common.types.ArrStr val) {
			Value = val;
		}
	}
	public KeyAliases getKeyAliases();
	/*Radix::PersoComm::Unit.Channel.DeliveryAck:currSapId:currSapId-Presentation Property*/


	public class CurrSapId extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public CurrSapId(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.DeliveryAck:currSapId:currSapId")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.DeliveryAck:currSapId:currSapId")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public CurrSapId getCurrSapId();
	/*Radix::PersoComm::Unit.Channel.DeliveryAck:serviceAccessibility:serviceAccessibility-Presentation Property*/


	public class ServiceAccessibility extends org.radixware.kernel.common.client.models.items.properties.PropertyChar{
		public ServiceAccessibility(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.DeliveryAck:serviceAccessibility:serviceAccessibility")
		public  org.radixware.kernel.common.enums.EServiceAccessibility getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.DeliveryAck:serviceAccessibility:serviceAccessibility")
		public   void setValue(org.radixware.kernel.common.enums.EServiceAccessibility val) {
			Value = val;
		}
	}
	public ServiceAccessibility getServiceAccessibility();
	/*Radix::PersoComm::Unit.Channel.DeliveryAck:trustedCertificateAliases:trustedCertificateAliases-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.DeliveryAck:trustedCertificateAliases:trustedCertificateAliases")
		public  org.radixware.kernel.common.types.ArrStr getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.DeliveryAck:trustedCertificateAliases:trustedCertificateAliases")
		public   void setValue(org.radixware.kernel.common.types.ArrStr val) {
			Value = val;
		}
	}
	public TrustedCertificateAliases getTrustedCertificateAliases();
	/*Radix::PersoComm::Unit.Channel.DeliveryAck:sapPropsXml:sapPropsXml-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.DeliveryAck:sapPropsXml:sapPropsXml")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.DeliveryAck:sapPropsXml:sapPropsXml")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public SapPropsXml getSapPropsXml();
	/*Radix::PersoComm::Unit.Channel.DeliveryAck:serviceUri:serviceUri-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.DeliveryAck:serviceUri:serviceUri")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.DeliveryAck:serviceUri:serviceUri")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ServiceUri getServiceUri();
	/*Radix::PersoComm::Unit.Channel.DeliveryAck:undefinedSapAddress:undefinedSapAddress-Presentation Property*/


	public class UndefinedSapAddress extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public UndefinedSapAddress(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.DeliveryAck:undefinedSapAddress:undefinedSapAddress")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.DeliveryAck:undefinedSapAddress:undefinedSapAddress")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public UndefinedSapAddress getUndefinedSapAddress();


}

/* Radix::PersoComm::Unit.Channel.DeliveryAck - Desktop Meta*/

/*Radix::PersoComm::Unit.Channel.DeliveryAck-Application Class*/

package org.radixware.ads.PersoComm.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Unit.Channel.DeliveryAck_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::PersoComm::Unit.Channel.DeliveryAck:Presentations-Entity Object Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aclYOEXMYXHGZCJRILV2QNYE7GSKY"),
			"Radix::PersoComm::Unit.Channel.DeliveryAck",
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aclVCLH3TEDVTOBDCLTAALOMT5GDM"),
			null,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUT7P3U2Q6NAOVI6B7A2MIUKG6U"),null,null,0,

			/*Radix::PersoComm::Unit.Channel.DeliveryAck:Properties-Properties*/
			new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

					/*Radix::PersoComm::Unit.Channel.DeliveryAck:keyAliases:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd4EIRGZ5DIJHLHBJHXIHPUD6PFE"),
						"keyAliases",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclYOEXMYXHGZCJRILV2QNYE7GSKY"),
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

						/*Radix::PersoComm::Unit.Channel.DeliveryAck:keyAliases:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls36VVCBNTN5FUXCGS6RVYVDGF7Y"),
						null,
						null,
						false,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::Unit.Channel.DeliveryAck:sapPropsXml:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdTCEJYXCNH5E5ZJO56FB3QY5XFY"),
						"sapPropsXml",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclYOEXMYXHGZCJRILV2QNYE7GSKY"),
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

						/*Radix::PersoComm::Unit.Channel.DeliveryAck:sapPropsXml:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::Unit.Channel.DeliveryAck:trustedCertificateAliases:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdRWQH4YEGVBHB5IUGL3LCWRGXDY"),
						"trustedCertificateAliases",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclYOEXMYXHGZCJRILV2QNYE7GSKY"),
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

						/*Radix::PersoComm::Unit.Channel.DeliveryAck:trustedCertificateAliases:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBJR35CKQVRC3XEKLVQURPKCZAU"),
						null,
						null,
						false,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::Unit.Channel.DeliveryAck:deliveryAckSapId:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQS73KCGHKBA5LAKVLERC3CIARU"),
						"deliveryAckSapId",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclYOEXMYXHGZCJRILV2QNYE7GSKY"),
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
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::PersoComm::Unit.Channel.DeliveryAck:deliveryAckSapId:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::Unit.Channel.DeliveryAck:sap:PropertyPresentation-Parent Ref Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNEBX3MILRVH4JJOYEVDU5D62IQ"),
						"sap",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclYOEXMYXHGZCJRILV2QNYE7GSKY"),
						org.radixware.kernel.common.enums.EValType.PARENT_REF,
						org.radixware.kernel.common.enums.EPropNature.DETAIL_PROP,
						21,
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
						133693439,
						133693439,false),

					/*Radix::PersoComm::Unit.Channel.DeliveryAck:type:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEWJT6TIHR5VDBNSIAAUMFADAIA"),
						"type",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclYOEXMYXHGZCJRILV2QNYE7GSKY"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						63,
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acsLSG35D4GIHNRDJIEACQMTAIZT4"),
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("4014"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::System::Unit:type:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acsLSG35D4GIHNRDJIEACQMTAIZT4"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.EXCLUDE,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("aciPVU3ZQLQB5CMFEEPH7S4LO67EA")}),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::Unit.Channel.DeliveryAck:serviceAccessibility:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdRI6AO4BL3BFD7CJ7JW2XUHZ62M"),
						"serviceAccessibility",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclYOEXMYXHGZCJRILV2QNYE7GSKY"),
						org.radixware.kernel.common.enums.EValType.CHAR,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
						4,
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acs3A7RUNYYRJCKPBZVNZD23FALXI"),
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("I"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::PersoComm::Unit.Channel.DeliveryAck:serviceAccessibility:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acs3A7RUNYYRJCKPBZVNZD23FALXI"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.NONE,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::Unit.Channel.DeliveryAck:serviceUri:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdYPXAP4LRANHV7NTSKIVC5XANVQ"),
						"serviceUri",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclYOEXMYXHGZCJRILV2QNYE7GSKY"),
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

						/*Radix::PersoComm::Unit.Channel.DeliveryAck:serviceUri:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::Unit.Channel.DeliveryAck:undefinedSapAddress:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdZ7Q3SMDCDNFGZKKTZ6TAZKX42A"),
						"undefinedSapAddress",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclYOEXMYXHGZCJRILV2QNYE7GSKY"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("?"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::PersoComm::Unit.Channel.DeliveryAck:undefinedSapAddress:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::Unit.Channel.DeliveryAck:currSapId:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd5JDAXHJWTBFYTDMTGQABVPT3VY"),
						"currSapId",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclYOEXMYXHGZCJRILV2QNYE7GSKY"),
						org.radixware.kernel.common.enums.EValType.INT,
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

						/*Radix::PersoComm::Unit.Channel.DeliveryAck:currSapId:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(null,null,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::Unit.Channel.DeliveryAck:sap_AccessibilityEditable:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd3THR2Q236BENBCBJJARMXCFDTA"),
						"sap_AccessibilityEditable",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclYOEXMYXHGZCJRILV2QNYE7GSKY"),
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

						/*Radix::PersoComm::Unit.Channel.DeliveryAck:sap_AccessibilityEditable:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aclYOEXMYXHGZCJRILV2QNYE7GSKY"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::Unit.Channel.DeliveryAck:kind:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col62V3FXUDVTOBDCLTAALOMT5GDM"),
						"kind",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclYOEXMYXHGZCJRILV2QNYE7GSKY"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.DETAIL_PROP,
						63,
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acsZ5EEISSCVXOBDCLUAALOMT5GDM"),
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("DeliveryAck"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::PersoComm::Unit.Channel.DeliveryAck:kind:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acsZ5EEISSCVXOBDCLUAALOMT5GDM"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.NONE,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::Unit.Channel.DeliveryAck:sendAddress:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXJZDYDMEVTOBDCLTAALOMT5GDM"),
						"sendAddress",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclYOEXMYXHGZCJRILV2QNYE7GSKY"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.DETAIL_PROP,
						63,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("<not used>"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::PersoComm::Unit.Channel.DeliveryAck:sendAddress:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,500,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::Unit.Channel.DeliveryAck:recvAddress:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXNZDYDMEVTOBDCLTAALOMT5GDM"),
						"recvAddress",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclYOEXMYXHGZCJRILV2QNYE7GSKY"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.DETAIL_PROP,
						63,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("<not used>"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::PersoComm::Unit.Channel.DeliveryAck:recvAddress:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,500,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false)
			},
			null,
			null,
			null,
			null,
			new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprVYRWUXCOHBAQ7FZM25SPC52P4A"),org.radixware.kernel.common.types.Id.Factory.loadFrom("eprSFSCKIRJQZB5BOH4ABCZJESOI4")},
			true,true,false);
}

/* Radix::PersoComm::Unit.Channel.DeliveryAck - Web Executable*/

/*Radix::PersoComm::Unit.Channel.DeliveryAck-Application Class*/

package org.radixware.ads.PersoComm.web;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.DeliveryAck")
public interface Unit.Channel.DeliveryAck   extends org.radixware.ads.PersoComm.web.Unit.Channel.AbstractBase  {



































































































































































	/*Radix::PersoComm::Unit.Channel.DeliveryAck:sap:sap-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.DeliveryAck:sap:sap")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.DeliveryAck:sap:sap")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public Sap getSap();
	/*Radix::PersoComm::Unit.Channel.DeliveryAck:deliveryAckSapId:deliveryAckSapId-Presentation Property*/


	public class DeliveryAckSapId extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public DeliveryAckSapId(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.DeliveryAck:deliveryAckSapId:deliveryAckSapId")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.DeliveryAck:deliveryAckSapId:deliveryAckSapId")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public DeliveryAckSapId getDeliveryAckSapId();
	/*Radix::PersoComm::Unit.Channel.DeliveryAck:sap_AccessibilityEditable:sap_AccessibilityEditable-Presentation Property*/


	public class Sap_AccessibilityEditable extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public Sap_AccessibilityEditable(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.DeliveryAck:sap_AccessibilityEditable:sap_AccessibilityEditable")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.DeliveryAck:sap_AccessibilityEditable:sap_AccessibilityEditable")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public Sap_AccessibilityEditable getSap_AccessibilityEditable();
	/*Radix::PersoComm::Unit.Channel.DeliveryAck:keyAliases:keyAliases-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.DeliveryAck:keyAliases:keyAliases")
		public  org.radixware.kernel.common.types.ArrStr getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.DeliveryAck:keyAliases:keyAliases")
		public   void setValue(org.radixware.kernel.common.types.ArrStr val) {
			Value = val;
		}
	}
	public KeyAliases getKeyAliases();
	/*Radix::PersoComm::Unit.Channel.DeliveryAck:currSapId:currSapId-Presentation Property*/


	public class CurrSapId extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public CurrSapId(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.DeliveryAck:currSapId:currSapId")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.DeliveryAck:currSapId:currSapId")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public CurrSapId getCurrSapId();
	/*Radix::PersoComm::Unit.Channel.DeliveryAck:serviceAccessibility:serviceAccessibility-Presentation Property*/


	public class ServiceAccessibility extends org.radixware.kernel.common.client.models.items.properties.PropertyChar{
		public ServiceAccessibility(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.DeliveryAck:serviceAccessibility:serviceAccessibility")
		public  org.radixware.kernel.common.enums.EServiceAccessibility getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.DeliveryAck:serviceAccessibility:serviceAccessibility")
		public   void setValue(org.radixware.kernel.common.enums.EServiceAccessibility val) {
			Value = val;
		}
	}
	public ServiceAccessibility getServiceAccessibility();
	/*Radix::PersoComm::Unit.Channel.DeliveryAck:trustedCertificateAliases:trustedCertificateAliases-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.DeliveryAck:trustedCertificateAliases:trustedCertificateAliases")
		public  org.radixware.kernel.common.types.ArrStr getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.DeliveryAck:trustedCertificateAliases:trustedCertificateAliases")
		public   void setValue(org.radixware.kernel.common.types.ArrStr val) {
			Value = val;
		}
	}
	public TrustedCertificateAliases getTrustedCertificateAliases();
	/*Radix::PersoComm::Unit.Channel.DeliveryAck:sapPropsXml:sapPropsXml-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.DeliveryAck:sapPropsXml:sapPropsXml")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.DeliveryAck:sapPropsXml:sapPropsXml")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public SapPropsXml getSapPropsXml();
	/*Radix::PersoComm::Unit.Channel.DeliveryAck:serviceUri:serviceUri-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.DeliveryAck:serviceUri:serviceUri")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.DeliveryAck:serviceUri:serviceUri")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ServiceUri getServiceUri();
	/*Radix::PersoComm::Unit.Channel.DeliveryAck:undefinedSapAddress:undefinedSapAddress-Presentation Property*/


	public class UndefinedSapAddress extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public UndefinedSapAddress(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.DeliveryAck:undefinedSapAddress:undefinedSapAddress")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.DeliveryAck:undefinedSapAddress:undefinedSapAddress")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public UndefinedSapAddress getUndefinedSapAddress();


}

/* Radix::PersoComm::Unit.Channel.DeliveryAck - Web Meta*/

/*Radix::PersoComm::Unit.Channel.DeliveryAck-Application Class*/

package org.radixware.ads.PersoComm.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Unit.Channel.DeliveryAck_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::PersoComm::Unit.Channel.DeliveryAck:Presentations-Entity Object Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aclYOEXMYXHGZCJRILV2QNYE7GSKY"),
			"Radix::PersoComm::Unit.Channel.DeliveryAck",
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aclVCLH3TEDVTOBDCLTAALOMT5GDM"),
			null,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUT7P3U2Q6NAOVI6B7A2MIUKG6U"),null,null,0,

			/*Radix::PersoComm::Unit.Channel.DeliveryAck:Properties-Properties*/
			new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

					/*Radix::PersoComm::Unit.Channel.DeliveryAck:keyAliases:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd4EIRGZ5DIJHLHBJHXIHPUD6PFE"),
						"keyAliases",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclYOEXMYXHGZCJRILV2QNYE7GSKY"),
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

						/*Radix::PersoComm::Unit.Channel.DeliveryAck:keyAliases:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls36VVCBNTN5FUXCGS6RVYVDGF7Y"),
						null,
						null,
						false,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::Unit.Channel.DeliveryAck:sapPropsXml:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdTCEJYXCNH5E5ZJO56FB3QY5XFY"),
						"sapPropsXml",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclYOEXMYXHGZCJRILV2QNYE7GSKY"),
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

						/*Radix::PersoComm::Unit.Channel.DeliveryAck:sapPropsXml:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::Unit.Channel.DeliveryAck:trustedCertificateAliases:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdRWQH4YEGVBHB5IUGL3LCWRGXDY"),
						"trustedCertificateAliases",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclYOEXMYXHGZCJRILV2QNYE7GSKY"),
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

						/*Radix::PersoComm::Unit.Channel.DeliveryAck:trustedCertificateAliases:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBJR35CKQVRC3XEKLVQURPKCZAU"),
						null,
						null,
						false,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::Unit.Channel.DeliveryAck:deliveryAckSapId:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQS73KCGHKBA5LAKVLERC3CIARU"),
						"deliveryAckSapId",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclYOEXMYXHGZCJRILV2QNYE7GSKY"),
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
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::PersoComm::Unit.Channel.DeliveryAck:deliveryAckSapId:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::Unit.Channel.DeliveryAck:sap:PropertyPresentation-Parent Ref Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNEBX3MILRVH4JJOYEVDU5D62IQ"),
						"sap",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclYOEXMYXHGZCJRILV2QNYE7GSKY"),
						org.radixware.kernel.common.enums.EValType.PARENT_REF,
						org.radixware.kernel.common.enums.EPropNature.DETAIL_PROP,
						21,
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
						133693439,
						133693439,false),

					/*Radix::PersoComm::Unit.Channel.DeliveryAck:type:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEWJT6TIHR5VDBNSIAAUMFADAIA"),
						"type",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclYOEXMYXHGZCJRILV2QNYE7GSKY"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						63,
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acsLSG35D4GIHNRDJIEACQMTAIZT4"),
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("4014"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::System::Unit:type:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acsLSG35D4GIHNRDJIEACQMTAIZT4"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.EXCLUDE,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("aciPVU3ZQLQB5CMFEEPH7S4LO67EA")}),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::Unit.Channel.DeliveryAck:serviceAccessibility:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdRI6AO4BL3BFD7CJ7JW2XUHZ62M"),
						"serviceAccessibility",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclYOEXMYXHGZCJRILV2QNYE7GSKY"),
						org.radixware.kernel.common.enums.EValType.CHAR,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
						4,
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acs3A7RUNYYRJCKPBZVNZD23FALXI"),
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("I"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::PersoComm::Unit.Channel.DeliveryAck:serviceAccessibility:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acs3A7RUNYYRJCKPBZVNZD23FALXI"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.NONE,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::Unit.Channel.DeliveryAck:serviceUri:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdYPXAP4LRANHV7NTSKIVC5XANVQ"),
						"serviceUri",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclYOEXMYXHGZCJRILV2QNYE7GSKY"),
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

						/*Radix::PersoComm::Unit.Channel.DeliveryAck:serviceUri:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::Unit.Channel.DeliveryAck:undefinedSapAddress:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdZ7Q3SMDCDNFGZKKTZ6TAZKX42A"),
						"undefinedSapAddress",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclYOEXMYXHGZCJRILV2QNYE7GSKY"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("?"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::PersoComm::Unit.Channel.DeliveryAck:undefinedSapAddress:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::Unit.Channel.DeliveryAck:currSapId:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd5JDAXHJWTBFYTDMTGQABVPT3VY"),
						"currSapId",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclYOEXMYXHGZCJRILV2QNYE7GSKY"),
						org.radixware.kernel.common.enums.EValType.INT,
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

						/*Radix::PersoComm::Unit.Channel.DeliveryAck:currSapId:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(null,null,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::Unit.Channel.DeliveryAck:sap_AccessibilityEditable:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd3THR2Q236BENBCBJJARMXCFDTA"),
						"sap_AccessibilityEditable",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclYOEXMYXHGZCJRILV2QNYE7GSKY"),
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

						/*Radix::PersoComm::Unit.Channel.DeliveryAck:sap_AccessibilityEditable:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aclYOEXMYXHGZCJRILV2QNYE7GSKY"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::Unit.Channel.DeliveryAck:kind:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col62V3FXUDVTOBDCLTAALOMT5GDM"),
						"kind",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclYOEXMYXHGZCJRILV2QNYE7GSKY"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.DETAIL_PROP,
						63,
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acsZ5EEISSCVXOBDCLUAALOMT5GDM"),
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("DeliveryAck"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::PersoComm::Unit.Channel.DeliveryAck:kind:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acsZ5EEISSCVXOBDCLUAALOMT5GDM"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.NONE,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::Unit.Channel.DeliveryAck:sendAddress:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXJZDYDMEVTOBDCLTAALOMT5GDM"),
						"sendAddress",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclYOEXMYXHGZCJRILV2QNYE7GSKY"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.DETAIL_PROP,
						63,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("<not used>"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::PersoComm::Unit.Channel.DeliveryAck:sendAddress:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,500,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::Unit.Channel.DeliveryAck:recvAddress:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXNZDYDMEVTOBDCLTAALOMT5GDM"),
						"recvAddress",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclYOEXMYXHGZCJRILV2QNYE7GSKY"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.DETAIL_PROP,
						63,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("<not used>"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::PersoComm::Unit.Channel.DeliveryAck:recvAddress:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,500,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false)
			},
			null,
			null,
			null,
			null,
			null,
			true,true,false);
}

/* Radix::PersoComm::Unit.Channel.DeliveryAck:Create - Desktop Meta*/

/*Radix::PersoComm::Unit.Channel.DeliveryAck:Create-Editor Presentation*/

package org.radixware.ads.PersoComm.explorer;
public final class Create_mi{
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new org.radixware.kernel.common.client.meta.RadEditorPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprVYRWUXCOHBAQ7FZM25SPC52P4A"),
	"Create",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
	org.radixware.kernel.common.types.Id.Factory.loadFrom("eprGVZBTVH6VXOBDCLVAALOMT5GDM"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("aclYOEXMYXHGZCJRILV2QNYE7GSKY"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl5HP4XTP3EGWDBRCRAAIT4AGD7E"),
	null,
	null,

	/*Radix::PersoComm::Unit.Channel.DeliveryAck:Create:Editor Pages-Editor Presentation Pages*/
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

			/*Radix::PersoComm::Unit.Channel.DeliveryAck:Create:Service-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadCustomEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgUDCLZSFPZ5CMTFKX5I6SQOWEEU"),"Service",org.radixware.kernel.common.types.Id.Factory.loadFrom("aclYOEXMYXHGZCJRILV2QNYE7GSKY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3O4UWH56SVFNDKJBBO2G7RIYDY"),null,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("cepHU3G6LGCC5EJZKCJL6FFQ2L3TE"))
	},
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgOCDVZAOTBXOBDNTPAAMPGXSZKU")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgUDCLZSFPZ5CMTFKX5I6SQOWEEU")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgUJB2RCH6KZHD5IMTQTE76DQSZA")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgJ4BHY7HBLBBBXIIQZEPCKTW2MU"))}
	,

	/*Radix::PersoComm::Unit.Channel.DeliveryAck:Create:Children-Explorer Items*/
		null
	,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
	null,
	0,
	null,
	(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
	36016,0,0);
}
/* Radix::PersoComm::Unit.Channel.DeliveryAck:Create:Model - Desktop Executable*/

/*Radix::PersoComm::Unit.Channel.DeliveryAck:Create:Model-Entity Model Class*/

package org.radixware.ads.PersoComm.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.DeliveryAck:Create:Model")
public class Create:Model  extends org.radixware.ads.PersoComm.explorer.Unit.Channel.DeliveryAck.Unit.Channel.DeliveryAck_DefaultModel.eprGVZBTVH6VXOBDCLVAALOMT5GDM_ModelAdapter {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return Create:Model_mi.rdxMeta; }



	public Create:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadEditorPresentationDef def){super(userSession,def);}

	/*Radix::PersoComm::Unit.Channel.DeliveryAck:Create:Model:Nested classes-Nested Classes*/

	/*Radix::PersoComm::Unit.Channel.DeliveryAck:Create:Model:Properties-Properties*/

	/*Radix::PersoComm::Unit.Channel.DeliveryAck:Create:Model:sapEditor-Dynamic Property*/



	protected org.radixware.ads.System.common_client.ISapEditorModel sapEditor=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.DeliveryAck:Create:Model:sapEditor")
	public published  org.radixware.ads.System.common_client.ISapEditorModel getSapEditor() {
		return sapEditor;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.DeliveryAck:Create:Model:sapEditor")
	public published   void setSapEditor(org.radixware.ads.System.common_client.ISapEditorModel val) {
		sapEditor = val;
	}

	/*Radix::PersoComm::Unit.Channel.DeliveryAck:Create:Model:serviceEditorEnabled-Dynamic Property*/



	protected boolean serviceEditorEnabled=true;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.DeliveryAck:Create:Model:serviceEditorEnabled")
	private final  boolean getServiceEditorEnabled() {
		return serviceEditorEnabled;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.DeliveryAck:Create:Model:serviceEditorEnabled")
	private final   void setServiceEditorEnabled(boolean val) {
		serviceEditorEnabled = val;
	}






	/*Radix::PersoComm::Unit.Channel.DeliveryAck:Create:Model:Methods-Methods*/

	/*Radix::PersoComm::Unit.Channel.DeliveryAck:Create:Model:checkSapEditor-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.DeliveryAck:Create:Model:checkSapEditor")
	protected published  boolean checkSapEditor () {
		Explorer.Utils::Trace.error(getEnvironment(), "check sap editor");
		if (serviceEditorEnabled && !isInSelectorRowContext()) {
		    if (sapEditor == null) {
		        getEditorPage(idof[Unit.Channel.DeliveryAck:Create:Service]).setFocused();
		    }
		    try {
		        sapEditor.check();
		    } catch (Exception ex) {
		        getEditorPage(idof[Unit.Channel.DeliveryAck:Create:Service]).setFocused();
		        showException(ex);
		        return false;
		    }
		}
		return true;

	}

	/*Radix::PersoComm::Unit.Channel.DeliveryAck:Create:Model:createSapEditor-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.DeliveryAck:Create:Model:createSapEditor")
	public published  void createSapEditor () {
		Explorer.Utils::Trace.error(getEnvironment(), "createSapEditor()");
		final Explorer.Qt.Types::QWidget widget = getSapEditorView();
		if (widget != null) {
		    sapEditor = System::ClientSapUtils.initSapEditor(currSapId.Value, sapPropsXml, widget);
		    sapEditor.setAvailableCertAliases(trustedCertificateAliases.Value);
		    sapEditor.setAvailableKeyAliases(keyAliases.Value);
		    System::ClientSapUtils.configureEditorForUnitServiceSap(sapEditor);
		    if (sap_AccessibilityEditable.Value == Boolean.FALSE) {
		        sapEditor.setPredefinedAccessibility( serviceAccessibility.Value);
		    }
		    sapEditor.setParentPage(getEditorPage(idof[Unit.Channel.DeliveryAck:Create:Service]));
		    fixupSapEditor();
		}

	}

	/*Radix::PersoComm::Unit.Channel.DeliveryAck:Create:Model:discardSapEditor-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.DeliveryAck:Create:Model:discardSapEditor")
	public published  void discardSapEditor () {
		Explorer.Qt.Types::QWidget widget = getSapEditorView();

		if (widget == null) {
		    return;
		}

		if (widget.children().size() > 1) {
		    Explorer.Widgets::EmbeddedEditor oldEditor = (Explorer.Widgets::EmbeddedEditor) widget.children().get(1);
		    oldEditor.setSynchronizedWithParentView(false);
		    oldEditor.close();
		    widget.layout().removeWidget(oldEditor);
		    widget.children().remove(oldEditor);
		    oldEditor.dispose();
		    sapEditor = null;
		}

	}

	/*Radix::PersoComm::Unit.Channel.DeliveryAck:Create:Model:fixupSapEditor-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.DeliveryAck:Create:Model:fixupSapEditor")
	public published  void fixupSapEditor () {
		if (sapEditor != null && java.util.Objects.equals(undefinedSapAddress.Value, sapEditor.getPropValue(idof[System::Sap:address]))) {
		    if (sapEditor instanceof Explorer.Models::EntityModel && ((Explorer.Models::EntityModel) sapEditor).isNew() && sapEditor.getPropValue(idof[System::Sap:channelType]) == System::SapChannelType:INTERNAL_PIPE)  {
		        sapEditor.setPropValue(idof[System::Sap:address], null);
		        sapEditor.setPropMandatory(idof[System::Sap:address], false);
		    }
		}
	}

	/*Radix::PersoComm::Unit.Channel.DeliveryAck:Create:Model:getSapEditorView-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.DeliveryAck:Create:Model:getSapEditorView")
	public  com.trolltech.qt.gui.QWidget getSapEditorView () {
		try {
		    return Unit.Channel.DeliveryAck:Create:Service:View:EditorPageView;
		} catch (RuntimeException ex) {
		    return null;
		}
	}

	/*Radix::PersoComm::Unit.Channel.DeliveryAck:Create:Model:recreateSapEditor-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.DeliveryAck:Create:Model:recreateSapEditor")
	public published  void recreateSapEditor () {
		discardSapEditor();
		createSapEditor();

	}

	/*Radix::PersoComm::Unit.Channel.DeliveryAck:Create:Model:sapEditorPageClosed-Presentation Slot Method*/

	@org.radixware.kernel.common.lang.ReflectiveCallable
	@SuppressWarnings("unused")
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.DeliveryAck:Create:Model:sapEditorPageClosed")
	public published  void sapEditorPageClosed () {
		discardSapEditor();

	}

	/*Radix::PersoComm::Unit.Channel.DeliveryAck:Create:Model:sapEditorPageOpened-Presentation Slot Method*/

	@org.radixware.kernel.common.lang.ReflectiveCallable
	@SuppressWarnings("unused")
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.DeliveryAck:Create:Model:sapEditorPageOpened")
	public published  void sapEditorPageOpened (com.trolltech.qt.gui.QWidget widget) {
		createSapEditor();

	}

	/*Radix::PersoComm::Unit.Channel.DeliveryAck:Create:Model:setServiceEditorEnabled-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.DeliveryAck:Create:Model:setServiceEditorEnabled")
	public published  void setServiceEditorEnabled (boolean enabled) {
		serviceEditorEnabled = enabled;
		getEditorPage(idof[Unit.Channel.DeliveryAck:Create:Service]).setVisible(enabled);
		if (enabled && sapEditor == null) {
		    createSapEditor();
		}
		if (!enabled && sapEditor != null) {
		    discardSapEditor();
		}
	}

	/*Radix::PersoComm::Unit.Channel.DeliveryAck:Create:Model:afterUpdate-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.DeliveryAck:Create:Model:afterUpdate")
	protected published  void afterUpdate () throws org.radixware.kernel.common.client.exceptions.ModelException {
		super.afterUpdate();
		Explorer.Utils::Trace.error(getEnvironment(), "after update");
		if (sapEditor instanceof Explorer.Models::EntityModel) {
		    Explorer.Models::EntityModel sapModel = ((Explorer.Models::EntityModel) sapEditor);
		    if (sapModel.isNew()) {
		        recreateSapEditor();
		    } else {
		        sapModel.activate(String.valueOf(currSapId.Value), "", sapModel.getClassId(), null);
		        try {
		            sapModel.read();
		        } catch (Exception ex) {
		            throw new RuntimeException(ex);
		        }
		    }
		}
	}

	/*Radix::PersoComm::Unit.Channel.DeliveryAck:Create:Model:beforeCreate-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.DeliveryAck:Create:Model:beforeCreate")
	protected published  boolean beforeCreate () throws org.radixware.kernel.common.client.exceptions.ModelException {
		Explorer.Utils::Trace.error(getEnvironment(), "before create");
		return checkSapEditor() && super.beforeCreate();
	}

	/*Radix::PersoComm::Unit.Channel.DeliveryAck:Create:Model:beforeUpdate-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.DeliveryAck:Create:Model:beforeUpdate")
	protected published  boolean beforeUpdate () throws org.radixware.kernel.common.client.exceptions.ModelException {
		Explorer.Utils::Trace.error(getEnvironment(), "before update");
		return checkSapEditor() && super.beforeUpdate();

	}







}

/* Radix::PersoComm::Unit.Channel.DeliveryAck:Create:Model - Desktop Meta*/

/*Radix::PersoComm::Unit.Channel.DeliveryAck:Create:Model-Entity Model Class*/

package org.radixware.ads.PersoComm.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Create:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aemVYRWUXCOHBAQ7FZM25SPC52P4A"),
						"Create:Model",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aemGVZBTVH6VXOBDCLVAALOMT5GDM"),
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::PersoComm::Unit.Channel.DeliveryAck:Create:Model:Properties-Properties*/
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

/* Radix::PersoComm::Unit.Channel.DeliveryAck:Create:Service:View - Desktop Executable*/

/*Radix::PersoComm::Unit.Channel.DeliveryAck:Create:Service:View-Custom Page Editor for Desktop*/

package org.radixware.ads.PersoComm.explorer;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.DeliveryAck:Create:Service:View")
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
		EditorPageView.setGeometry(new com.trolltech.qt.core.QRect(0, 0, 600, 400));
		EditorPageView.setFont(DEFAULT_FONT);
		final com.trolltech.qt.gui.QGridLayout $layout1 = new com.trolltech.qt.gui.QGridLayout(EditorPageView);
		$layout1.setObjectName("gridLayout");
		$layout1.setContentsMargins(9, 9, 9, 9);
		$layout1.setSizeConstraint(com.trolltech.qt.gui.QLayout.SizeConstraint.SetDefaultConstraint);
		$layout1.setHorizontalSpacing(6);
		$layout1.setVerticalSpacing(6);
		EditorPageView.opened.connect(model, "mth6JLK5CZABRCPJEP4LANS5UHCSU(com.trolltech.qt.gui.QWidget)");
		EditorPageView.closed.connect(model, "mth4GE7IYDRKFF5ZFYZFVJNKTOHCE()");
		opened.emit(this);
	}
	public final org.radixware.ads.PersoComm.explorer.Create:Model getModel() {
		return (org.radixware.ads.PersoComm.explorer.Create:Model) super.getModel();
	}

}

/* Radix::PersoComm::Unit.Channel.DeliveryAck:Create:Service:WebView - Web Executable*/

/*Radix::PersoComm::Unit.Channel.DeliveryAck:Create:Service:WebView-Rwt Custom Page Editor*/

package org.radixware.ads.PersoComm.web;
@SuppressWarnings({"unchecked","rawtypes"})
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.DeliveryAck:Create:Service:WebView")
public class WebView extends org.radixware.wps.views.editor.EditorPageView{
	public WebView widget;
	public WebView getWidget(){ return  widget;}
	public org.radixware.wps.views.editor.PropertiesGrid widget;
	public org.radixware.wps.views.editor.PropertiesGrid getWidget(){ return  widget;}
	public WebView(org.radixware.kernel.common.client.IClientEnvironment env,org.radixware.kernel.common.client.views.IView view
	,org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef page){
		super(env,view,page);
	}
	public void open(org.radixware.kernel.common.client.models.Model model){
		super.open(model);
		//============ Radix::PersoComm::Unit.Channel.DeliveryAck:Create:Service:WebView:widget ==============
		this.widget = this;
		widget.setWidth(600);
		widget.setHeight(400);
		//============ Radix::PersoComm::Unit.Channel.DeliveryAck:Create:Service:WebView:widget:widget ==============
		this.widget = new org.radixware.wps.views.editor.PropertiesGrid();
		widget.setObjectName("widget");
		this.widget.add(this.widget);
		widget.getAnchors().setTop(new org.radixware.wps.rwt.UIObject.Anchors.Anchor(0.0f,0));
		widget.getAnchors().setLeft(new org.radixware.wps.rwt.UIObject.Anchors.Anchor(0.0f,0));
		widget.getAnchors().setBottom(new org.radixware.wps.rwt.UIObject.Anchors.Anchor(1.0f,0));
		widget.getAnchors().setRight(new org.radixware.wps.rwt.UIObject.Anchors.Anchor(1.0f,0));
		this.wdgEF6FEYHLT5AZHNW6OXITI5AL4Y.bind();
		fireOpened();
	}
}

/* Radix::PersoComm::Unit.Channel.DeliveryAck:Edit - Desktop Meta*/

/*Radix::PersoComm::Unit.Channel.DeliveryAck:Edit-Editor Presentation*/

package org.radixware.ads.PersoComm.explorer;
public final class Edit_mi{
	private static final class Edit_DEF extends org.radixware.kernel.common.client.meta.RadEditorPresentationDef{
		Edit_DEF(){
			super(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprSFSCKIRJQZB5BOH4ABCZJESOI4"),
			"Edit",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("eprVYRWUXCOHBAQ7FZM25SPC52P4A"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aclYOEXMYXHGZCJRILV2QNYE7GSKY"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl5HP4XTP3EGWDBRCRAAIT4AGD7E"),
			null,
			null,
			null,
			null,

			/*Radix::PersoComm::Unit.Channel.DeliveryAck:Edit:Children-Explorer Items*/
				null
			,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
			null,
			0,
			null,
			(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
			40114,0,0);
		}
		@Override
		protected org.radixware.kernel.common.client.models.Model createModelImpl(org.radixware.kernel.common.client.IClientEnvironment userSession) {    return new org.radixware.ads.PersoComm.explorer.Create:Model(userSession,this);
		}
	}
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new Edit_DEF(); 
;
}
/* Radix::PersoComm::Unit.Channel.DeliveryAck - Localizing Bundle */
package org.radixware.ads.PersoComm.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Unit.Channel.DeliveryAck - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<any>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<любые>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls36VVCBNTN5FUXCGS6RVYVDGF7Y"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Service");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Сервис");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3O4UWH56SVFNDKJBBO2G7RIYDY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Error reading the key aliases from the keystore: ");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ошибка чтения списка ключей из хранилища: ");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4BKJRJRD6NF33N4HFN5LX6T6OQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<any>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<любые>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBJR35CKQVRC3XEKLVQURPKCZAU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Source address");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Адрес отправителя");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLJXPTCW22RD6POZAWB4SBJ45VE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Gateway address");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Адрес шлюза");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMEHNUUL62RGO5ERYS77LIORZ2Q"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Error reading the trusted certificate aliases from the keystore: ");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ошибка чтения списка доверенных сертификатов из хранилища: ");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMW67A6LBTRFXDCRP7BSAF6WALM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Error reading the key aliases from the keystore: ");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ошибка чтения списка ключей из хранилища: ");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQCDB5P5N4RBIZERS6JVZ6OSHHI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Delivery Acknowledgement Communication Channel");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Коммуникационный канал - Подтверждение доставки");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUT7P3U2Q6NAOVI6B7A2MIUKG6U"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Error reading the trusted certificate aliases from the keystore:");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ошибка чтения списка доверенных сертификатов из хранилища: ");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZTQBCAEAKRE25JC3NCNPCF4H4Y"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(Unit.Channel.DeliveryAck - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbaclYOEXMYXHGZCJRILV2QNYE7GSKY"),"Unit.Channel.DeliveryAck - Localizing Bundle",$$$items$$$);
}
