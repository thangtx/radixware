
/* Radix::Jms::Unit.JmsHandler - Server Executable*/

/*Radix::Jms::Unit.JmsHandler-Application Class*/

package org.radixware.ads.Jms.server;

import java.util.*;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Jms::Unit.JmsHandler")
@Deprecated
public abstract published class Unit.JmsHandler  extends org.radixware.ads.System.server.Unit.AbstractServiceDriver  implements org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {


	/**Metainformation accessor method*/
	@SuppressWarnings("unused")
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return Unit.JmsHandler_mi.rdxMeta;}

	/*Radix::Jms::Unit.JmsHandler:Nested classes-Nested Classes*/

	/*Radix::Jms::Unit.JmsHandler:Properties-Properties*/

	/*Radix::Jms::Unit.JmsHandler:jmsConnectProps-Detail Column Property*/


















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Jms::Unit.JmsHandler:jmsConnectProps")
	@Deprecated
	public published  java.sql.Clob getJmsConnectProps() {
		return jmsConnectProps;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Jms::Unit.JmsHandler:jmsConnectProps")
	@Deprecated
	public published   void setJmsConnectProps(java.sql.Clob val) {
		jmsConnectProps = val;
	}

	/*Radix::Jms::Unit.JmsHandler:jmsMessProps-Detail Column Property*/


















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Jms::Unit.JmsHandler:jmsMessProps")
	@Deprecated
	public published  java.sql.Clob getJmsMessProps() {
		return jmsMessProps;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Jms::Unit.JmsHandler:jmsMessProps")
	@Deprecated
	public published   void setJmsMessProps(java.sql.Clob val) {
		jmsMessProps = val;
	}

	/*Radix::Jms::Unit.JmsHandler:jmsMessFormat-Detail Column Property*/









			
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Jms::Unit.JmsHandler:jmsMessFormat")
	@Deprecated
	public published  org.radixware.kernel.common.enums.EJmsMessageFormat getJmsMessFormat() {
		return jmsMessFormat;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Jms::Unit.JmsHandler:jmsMessFormat")
	@Deprecated
	public published   void setJmsMessFormat(org.radixware.kernel.common.enums.EJmsMessageFormat val) {
		jmsMessFormat = val;
	}

	/*Radix::Jms::Unit.JmsHandler:jmsLogin-Detail Column Property*/


















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Jms::Unit.JmsHandler:jmsLogin")
	@Deprecated
	public published  Str getJmsLogin() {
		return jmsLogin;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Jms::Unit.JmsHandler:jmsLogin")
	@Deprecated
	public published   void setJmsLogin(Str val) {
		jmsLogin = val;
	}

	/*Radix::Jms::Unit.JmsHandler:jmsPassword-Detail Column Property*/


















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Jms::Unit.JmsHandler:jmsPassword")
	@Deprecated
	public published  Str getJmsPassword() {
		return jmsPassword;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Jms::Unit.JmsHandler:jmsPassword")
	@Deprecated
	public published   void setJmsPassword(Str val) {
		jmsPassword = val;
	}

	/*Radix::Jms::Unit.JmsHandler:msRqQueueName-Detail Column Property*/


















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Jms::Unit.JmsHandler:msRqQueueName")
	@Deprecated
	public published  Str getMsRqQueueName() {
		return msRqQueueName;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Jms::Unit.JmsHandler:msRqQueueName")
	@Deprecated
	public published   void setMsRqQueueName(Str val) {
		msRqQueueName = val;
	}

	/*Radix::Jms::Unit.JmsHandler:msRsQueueName-Detail Column Property*/


















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Jms::Unit.JmsHandler:msRsQueueName")
	@Deprecated
	public published  Str getMsRsQueueName() {
		return msRsQueueName;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Jms::Unit.JmsHandler:msRsQueueName")
	@Deprecated
	public published   void setMsRsQueueName(Str val) {
		msRsQueueName = val;
	}

	/*Radix::Jms::Unit.JmsHandler:outSeanceCnt-Detail Column Property*/


















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Jms::Unit.JmsHandler:outSeanceCnt")
	@Deprecated
	public published  Int getOutSeanceCnt() {
		return outSeanceCnt;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Jms::Unit.JmsHandler:outSeanceCnt")
	@Deprecated
	public published   void setOutSeanceCnt(Int val) {
		outSeanceCnt = val;
	}

	/*Radix::Jms::Unit.JmsHandler:rsTimeout-Detail Column Property*/


















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Jms::Unit.JmsHandler:rsTimeout")
	@Deprecated
	public published  Int getRsTimeout() {
		return rsTimeout;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Jms::Unit.JmsHandler:rsTimeout")
	@Deprecated
	public published   void setRsTimeout(Int val) {
		rsTimeout = val;
	}

	/*Radix::Jms::Unit.JmsHandler:inSeanceCnt-Detail Column Property*/


















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Jms::Unit.JmsHandler:inSeanceCnt")
	@Deprecated
	public published  Int getInSeanceCnt() {
		return inSeanceCnt;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Jms::Unit.JmsHandler:inSeanceCnt")
	@Deprecated
	public published   void setInSeanceCnt(Int val) {
		inSeanceCnt = val;
	}

	/*Radix::Jms::Unit.JmsHandler:sapId-Detail Column Property*/


















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Jms::Unit.JmsHandler:sapId")
	@Deprecated
	public published  Int getSapId() {
		return sapId;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Jms::Unit.JmsHandler:sapId")
	@Deprecated
	public published   void setSapId(Int val) {
		sapId = val;
	}

	/*Radix::Jms::Unit.JmsHandler:isClient-Detail Column Property*/


















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Jms::Unit.JmsHandler:isClient")
	@Deprecated
	public published  Bool getIsClient() {
		return isClient;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Jms::Unit.JmsHandler:isClient")
	@Deprecated
	public published   void setIsClient(Bool val) {
		isClient = val;
	}





































































































	/*Radix::Jms::Unit.JmsHandler:Methods-Methods*/

	/*Radix::Jms::Unit.JmsHandler:afterInit-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Jms::Unit.JmsHandler:afterInit")
	@Deprecated
	protected  void afterInit (org.radixware.kernel.server.types.Entity src, org.radixware.kernel.common.enums.EEntityInitializationPhase phase) {
		super.afterInit(src, phase);
		type = System::UnitType:JmsHandler;
	}

	/*Radix::Jms::Unit.JmsHandler:onRequest-User Method*/

	@org.radixware.kernel.common.lang.ReflectiveCallable
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Jms::Unit.JmsHandler:onRequest")
	@Deprecated
	public abstract published  org.radixware.schemas.jmshandler.MessageDocument onRequest (org.radixware.schemas.jmshandler.MessageDocument.Message mess);

	/*Radix::Jms::Unit.JmsHandler:bundle-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Jms::Unit.JmsHandler:bundle")
	@Deprecated
	protected published  void bundle () {
		//мультиязычные строки используемые в пакете org.radixware.kernel.server.units.jms
		Str[] s = new Str[10];
		s[0] = eventCode["Error starting JMS connection of %1: %2"];
		s[1] = eventCode["Error sending/receiving JMS \"%1\": %2"];
		s[2] = eventCode["Limit of requests from external system (%1) exceeded in \"%2\""];
		s[3] = eventCode["Limit of requests to external system (%1) exceeded in \"%2\""];
		s[4] = eventCode["Error reading parameters of service \"%1\": %2"];
		s[5] = eventCode["Service Server: timeout of response from external system exceeded (ID=%1)"];
		s[6] = eventCode["Service Server: uncorrelated response received (id=%1)"];
		s[7] = eventCode["JMS properties parsing error: %1"];

	}

	/*Radix::Jms::Unit.JmsHandler:getSapId-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Jms::Unit.JmsHandler:getSapId")
	@Deprecated
	protected published  Int getSapId () {
		return sapId;
	}

	/*Radix::Jms::Unit.JmsHandler:getServiceAccessibility-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Jms::Unit.JmsHandler:getServiceAccessibility")
	@Deprecated
	protected published  org.radixware.kernel.common.enums.EServiceAccessibility getServiceAccessibility () {
		return System::ServiceAccessibility:INTRA_SYSTEM;
	}

	/*Radix::Jms::Unit.JmsHandler:getUri-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Jms::Unit.JmsHandler:getUri")
	@Deprecated
	protected published  Str getUri () {
		return getWsdl() + "#" + id.toString();
	}

	/*Radix::Jms::Unit.JmsHandler:getWsdl-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Jms::Unit.JmsHandler:getWsdl")
	@Deprecated
	protected published  Str getWsdl () {
		return "http://schemas.radixware.org/jmshandler.wsdl";
	}

	/*Radix::Jms::Unit.JmsHandler:setSapId-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Jms::Unit.JmsHandler:setSapId")
	@Deprecated
	protected published  void setSapId (Int sapId) {
		sapId = sapId;
	}

	/*Radix::Jms::Unit.JmsHandler:getUsedAddresses-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Jms::Unit.JmsHandler:getUsedAddresses")
	@Deprecated
	protected published  java.util.Collection<org.radixware.ads.System.server.AddressInfo> getUsedAddresses () {
		final List<System::AddressInfo> addresses = new ArrayList<System::AddressInfo>();
		if (sapId != null) {
		    addresses.add(new AddressInfo(instanceId, id, sapId));
		}
		return addresses;
	}


	@Override
	public org.radixware.kernel.server.types.FormHandler.NextDialogsRequest execCommand(org.radixware.kernel.common.types.Id cmdId, org.radixware.kernel.common.types.Id propId, org.apache.xmlbeans.XmlObject input, org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValsById, org.apache.xmlbeans.XmlObject output) throws org.radixware.kernel.common.exceptions.AppException,java.lang.InterruptedException{

			return super.execCommand(cmdId,propId,input,newPropValsById,output);
	}


}

/* Radix::Jms::Unit.JmsHandler - Server Meta*/

/*Radix::Jms::Unit.JmsHandler-Application Class*/

package org.radixware.ads.Jms.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Unit.JmsHandler_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("aclMU4BLUT6FJCZLDQ2WL6SCH43NE"),"Unit.JmsHandler",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsSKMLZMFF4ZGCDEQD3BOVCLB4XE"),

						org.radixware.kernel.common.enums.EClassType.APPLICATION,

						/*Radix::Jms::Unit.JmsHandler:Presentations-Entity Object Presentations*/
						/*Class presentation attributes*/
						 new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef(/*Owner Class Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("aclMU4BLUT6FJCZLDQ2WL6SCH43NE"),
							/*Owner Class Name*/
							"Unit.JmsHandler",
							/*Title Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsSKMLZMFF4ZGCDEQD3BOVCLB4XE"),
							/*Property presentations*/

							/*Radix::Jms::Unit.JmsHandler:Properties-Properties*/
							new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef[]{

									/*Radix::Jms::Unit.JmsHandler:jmsConnectProps:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colBC6VGIU6S5CFNAN6V4PG3DKCUQ"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Jms::Unit.JmsHandler:jmsMessProps:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col2YNMUHO7AJCVTMJW2K5OPQ3GQQ"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Jms::Unit.JmsHandler:jmsMessFormat:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colA2LTDQXERRBJ5HAYLJT3KMSBEY"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Jms::Unit.JmsHandler:jmsLogin:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colO77HLWLCFFFWVIKV7MKDDEZY5A"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Jms::Unit.JmsHandler:jmsPassword:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colC5VS2BZB3RAHFNHR5SWKU5LBA4"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Jms::Unit.JmsHandler:msRqQueueName:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colRH4YQQJF4NCVNIMAN3HXIQSWLY"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Jms::Unit.JmsHandler:msRsQueueName:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col37TNUN2QIVAWJE2HLAOG6OY4YA"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Jms::Unit.JmsHandler:outSeanceCnt:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKFSA4DVNEZEWFLARG7YYJFLVSE"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Jms::Unit.JmsHandler:rsTimeout:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colCJFJTBWSSJH55NNIHQSUHEWUDY"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Jms::Unit.JmsHandler:inSeanceCnt:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKHT276DSLVD7FKJLBW3IWOQMFM"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Jms::Unit.JmsHandler:sapId:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKFFC226QSVEULHVCVM4KTLVGOA"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT))
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
									/*Radix::Jms::Unit.JmsHandler:Edit-Editor Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprJV6IPHCDKBFMJHMMQLL2HSSSLE"),
									"Edit",
									org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprE7BZV3MWA7OBDCGDAALOMT5GDM"),
									35984,
									null,

									/*Radix::Jms::Unit.JmsHandler:Edit:Children-Explorer Items*/
										null
									,
									org.radixware.kernel.server.types.Restrictions.Factory.newInstance(16,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdF3SRJU4KOXOBDCJRAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdTZE2P7EKOXOBDCJRAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdQWDXKHRSO7OBDCJTAALOMT5GDM")},null,null),
									(org.radixware.kernel.server.meta.presentations.RadPropertyPresentationAttributes[]) null,
									org.radixware.kernel.common.enums.EEditorPresentationRightsInheritanceMode.FROM_REPLACED,
									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprE7BZV3MWA7OBDCGDAALOMT5GDM")),

									new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef(
									/*Radix::Jms::Unit.JmsHandler:Create-Editor Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprRADZIYXO75DFDKFEM2XQPRSQOE"),
									"Create",
									org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprJV6IPHCDKBFMJHMMQLL2HSSSLE"),
									40114,
									null,

									/*Radix::Jms::Unit.JmsHandler:Create:Children-Explorer Items*/
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
							org.radixware.kernel.common.utils.Maps.fromArrays(new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprXCFCDDGTRHNRDB5MAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("eprS6PW3X2EKJFUNPGCXYDBACXY7A")},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprJV6IPHCDKBFMJHMMQLL2HSSSLE"),org.radixware.kernel.common.types.Id.Factory.loadFrom("eprRADZIYXO75DFDKFEM2XQPRSQOE")}),
							/*Object title format*/
							null,
							/*Class catalogs*/
							null,
							/*Restrictions*/
							null,null,0),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl5HP4XTP3EGWDBRCRAAIT4AGD7E"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclXIUFMDMMA7OBDCGDAALOMT5GDM"),

						/*Radix::Jms::Unit.JmsHandler:Properties-Properties*/
						new org.radixware.kernel.server.meta.clazzes.RadPropDef[]{

								/*Radix::Jms::Unit.JmsHandler:jmsConnectProps-Detail Column Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDetailPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colBC6VGIU6S5CFNAN6V4PG3DKCUQ"),"jmsConnectProps",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKD76YOIMTVG4ZBIBJ533FVFSNI"),org.radixware.kernel.common.enums.EValType.CLOB,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("refKCN6JHUWCRA5DIGFJONLLABUJA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colEUISHHRET5HX7GXPBXR6HZINFA"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Jms::Unit.JmsHandler:jmsMessProps-Detail Column Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDetailPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col2YNMUHO7AJCVTMJW2K5OPQ3GQQ"),"jmsMessProps",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3V4NOXZFWZCYRPBUR2SYTERCOM"),org.radixware.kernel.common.enums.EValType.CLOB,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("refKCN6JHUWCRA5DIGFJONLLABUJA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colKVAXOF2ZTRDQFFMUAUFLXK2DXQ"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Jms::Unit.JmsHandler:jmsMessFormat-Detail Column Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDetailPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colA2LTDQXERRBJ5HAYLJT3KMSBEY"),"jmsMessFormat",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUW2EX6KF5ZD7XA3CPPLJ5S5D7U"),org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("acsQEYA5D55Q5C7VDLY3SYRSDJ3HA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("refKCN6JHUWCRA5DIGFJONLLABUJA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("col6TV2TQKDM5EFDFKQO77YI7OMQI"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("2")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Jms::Unit.JmsHandler:jmsLogin-Detail Column Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDetailPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colO77HLWLCFFFWVIKV7MKDDEZY5A"),"jmsLogin",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWSFJFAUEY5AVXEE54MKNPMEL54"),org.radixware.kernel.common.enums.EValType.STR,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("refKCN6JHUWCRA5DIGFJONLLABUJA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("col5XKHYVCPXJDXBHEYV4FGBESTOU"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Jms::Unit.JmsHandler:jmsPassword-Detail Column Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDetailPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colC5VS2BZB3RAHFNHR5SWKU5LBA4"),"jmsPassword",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMQMULH6N6VFAZCBENJ23WHN4XU"),org.radixware.kernel.common.enums.EValType.STR,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("refKCN6JHUWCRA5DIGFJONLLABUJA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colJWUOO3WV2VAJJIXBS4T2LG5QCA"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Jms::Unit.JmsHandler:msRqQueueName-Detail Column Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDetailPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colRH4YQQJF4NCVNIMAN3HXIQSWLY"),"msRqQueueName",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPHBST3ZF3FH47MQ5662ELGLS64"),org.radixware.kernel.common.enums.EValType.STR,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("refKCN6JHUWCRA5DIGFJONLLABUJA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colYDA6H4CVPJHHJMMKIP2GA63ABQ"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Jms::Unit.JmsHandler:msRsQueueName-Detail Column Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDetailPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col37TNUN2QIVAWJE2HLAOG6OY4YA"),"msRsQueueName",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsE5BNZPJV55AMRPKBHF6HB33PHM"),org.radixware.kernel.common.enums.EValType.STR,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("refKCN6JHUWCRA5DIGFJONLLABUJA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colSANDUD6QPFBG3PXQRM23LQJNLQ"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Jms::Unit.JmsHandler:outSeanceCnt-Detail Column Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDetailPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKFSA4DVNEZEWFLARG7YYJFLVSE"),"outSeanceCnt",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJPVATRJI7BBWDKEDR45FCBF5DM"),org.radixware.kernel.common.enums.EValType.INT,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("refKCN6JHUWCRA5DIGFJONLLABUJA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colUFAQRTDFJ5ENTMBSDOAEEBMH6A"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("30")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Jms::Unit.JmsHandler:rsTimeout-Detail Column Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDetailPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colCJFJTBWSSJH55NNIHQSUHEWUDY"),"rsTimeout",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsSZAQUZMWWZH3PIGLX576AG3N5Y"),org.radixware.kernel.common.enums.EValType.INT,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("refKCN6JHUWCRA5DIGFJONLLABUJA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colQ3WOAVF2J5HF3FZSVEYR273QQM"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Jms::Unit.JmsHandler:inSeanceCnt-Detail Column Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDetailPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKHT276DSLVD7FKJLBW3IWOQMFM"),"inSeanceCnt",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5Z7FKVGSZBAQND3R6ASXATOXP4"),org.radixware.kernel.common.enums.EValType.INT,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("refKCN6JHUWCRA5DIGFJONLLABUJA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colRSQIP5ZZYRAIRGEUHVSSA4JFK4"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("30")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Jms::Unit.JmsHandler:sapId-Detail Column Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDetailPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKFFC226QSVEULHVCVM4KTLVGOA"),"sapId",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCTD3VQJMYJFKHLVWVCGKUX7Q6I"),org.radixware.kernel.common.enums.EValType.INT,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("refKCN6JHUWCRA5DIGFJONLLABUJA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colFMGGFQ6MORC67LQVLCPFLZHOJM"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Jms::Unit.JmsHandler:isClient-Detail Column Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDetailPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colWAQMCEU35BG3TP4YDKKTVOE2LU"),"isClient",null,org.radixware.kernel.common.enums.EValType.BOOL,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("refKCN6JHUWCRA5DIGFJONLLABUJA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colA2PFHZGUNRBXPPOKMGSZI2W4CI"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("1")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE)
						},

						/*Radix::Jms::Unit.JmsHandler:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthD77F2HBL2FGSVPJHR3SEAMPWMA"),"afterInit",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("src",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprIUGXG2HERRE3HLLCI5N2RKQ2QU")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("phase",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprDCU54EXEXNCGROD2A2GQGH466I"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth5QSCSEQ3ZJFXVKMUX7VRMSKGLQ"),"onRequest",false,true,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("mess",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprZKR6G2XISRAKDBR5FVYFBIRH7M"))
								},org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthLOJFVBBFEJAOJMQNL7JEFFHGPA"),"bundle",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthH3KBLJ4UA7OBDCGDAALOMT5GDM"),"getSapId",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,org.radixware.kernel.common.enums.EValType.INT),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthQETN7HP66VD2FCEUQ7LE43SVTM"),"getServiceAccessibility",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,org.radixware.kernel.common.enums.EValType.CHAR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthACRIT5KA6ZCVHNUXCLLEQCS72M"),"getUri",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthGREMDFUPA7OBDCGDAALOMT5GDM"),"getWsdl",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthTD7AAPMUA7OBDCGDAALOMT5GDM"),"setSapId",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("sapId",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprHNPNHJ6OZJGI5FVAXQAOTZAKDE"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthQIF7CPRFE5GUTIY26PT5SYUD6E"),"getUsedAddresses",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,org.radixware.kernel.common.enums.EValType.JAVA_CLASS)
						},
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("refKCN6JHUWCRA5DIGFJONLLABUJA")},
						null,null,null,true);
}

/* Radix::Jms::Unit.JmsHandler - Desktop Executable*/

/*Radix::Jms::Unit.JmsHandler-Application Class*/

package org.radixware.ads.Jms.explorer;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Jms::Unit.JmsHandler")
public interface Unit.JmsHandler   extends org.radixware.ads.System.explorer.Unit.AbstractServiceDriver  {















































































































































































	/*Radix::Jms::Unit.JmsHandler:jmsMessProps:jmsMessProps-Presentation Property*/


	public class JmsMessProps extends org.radixware.kernel.common.client.models.items.properties.PropertyClob{
		public JmsMessProps(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Deprecated
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Jms::Unit.JmsHandler:jmsMessProps:jmsMessProps")
		@Deprecated
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Jms::Unit.JmsHandler:jmsMessProps:jmsMessProps")
		@Deprecated
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public JmsMessProps getJmsMessProps();
	/*Radix::Jms::Unit.JmsHandler:msRsQueueName:msRsQueueName-Presentation Property*/


	public class MsRsQueueName extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public MsRsQueueName(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Deprecated
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Jms::Unit.JmsHandler:msRsQueueName:msRsQueueName")
		@Deprecated
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Jms::Unit.JmsHandler:msRsQueueName:msRsQueueName")
		@Deprecated
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public MsRsQueueName getMsRsQueueName();
	/*Radix::Jms::Unit.JmsHandler:jmsMessFormat:jmsMessFormat-Presentation Property*/


	public class JmsMessFormat extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public JmsMessFormat(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Deprecated
		@Override
		public void setServerValue(org.radixware.kernel.common.client.models.items.properties.PropertyValue val){
			Object x = val.getValue();
			org.radixware.kernel.common.enums.EJmsMessageFormat dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EJmsMessageFormat ? (org.radixware.kernel.common.enums.EJmsMessageFormat)x : org.radixware.kernel.common.enums.EJmsMessageFormat.getForValue(((Int)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.INT)).longValue()));
			val.refineValue(dummy);
			super.setServerValue(val);
		}
		@Deprecated
		@Override
		public Class<org.radixware.kernel.common.enums.EJmsMessageFormat> getValClass(){
			return org.radixware.kernel.common.enums.EJmsMessageFormat.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Deprecated
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.enums.EJmsMessageFormat dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EJmsMessageFormat ? (org.radixware.kernel.common.enums.EJmsMessageFormat)x : org.radixware.kernel.common.enums.EJmsMessageFormat.getForValue(((Int)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.INT)).longValue()));
			setValue(dummy);
		}






				















		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Jms::Unit.JmsHandler:jmsMessFormat:jmsMessFormat")
		@Deprecated
		public  org.radixware.kernel.common.enums.EJmsMessageFormat getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Jms::Unit.JmsHandler:jmsMessFormat:jmsMessFormat")
		@Deprecated
		public   void setValue(org.radixware.kernel.common.enums.EJmsMessageFormat val) {
			Value = val;
		}
	}
	public JmsMessFormat getJmsMessFormat();
	/*Radix::Jms::Unit.JmsHandler:jmsConnectProps:jmsConnectProps-Presentation Property*/


	public class JmsConnectProps extends org.radixware.kernel.common.client.models.items.properties.PropertyClob{
		public JmsConnectProps(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Deprecated
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Jms::Unit.JmsHandler:jmsConnectProps:jmsConnectProps")
		@Deprecated
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Jms::Unit.JmsHandler:jmsConnectProps:jmsConnectProps")
		@Deprecated
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public JmsConnectProps getJmsConnectProps();
	/*Radix::Jms::Unit.JmsHandler:jmsPassword:jmsPassword-Presentation Property*/


	public class JmsPassword extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public JmsPassword(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Deprecated
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Jms::Unit.JmsHandler:jmsPassword:jmsPassword")
		@Deprecated
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Jms::Unit.JmsHandler:jmsPassword:jmsPassword")
		@Deprecated
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public JmsPassword getJmsPassword();
	/*Radix::Jms::Unit.JmsHandler:rsTimeout:rsTimeout-Presentation Property*/


	public class RsTimeout extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public RsTimeout(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Deprecated
		@Override
		public void setValueObject(final Object x){
			Int dummy = ((Int)x);
			setValue(dummy);
		}














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Jms::Unit.JmsHandler:rsTimeout:rsTimeout")
		@Deprecated
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Jms::Unit.JmsHandler:rsTimeout:rsTimeout")
		@Deprecated
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public RsTimeout getRsTimeout();
	/*Radix::Jms::Unit.JmsHandler:sapId:sapId-Presentation Property*/


	public class SapId extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public SapId(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Deprecated
		@Override
		public void setValueObject(final Object x){
			Int dummy = ((Int)x);
			setValue(dummy);
		}














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Jms::Unit.JmsHandler:sapId:sapId")
		@Deprecated
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Jms::Unit.JmsHandler:sapId:sapId")
		@Deprecated
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public SapId getSapId();
	/*Radix::Jms::Unit.JmsHandler:outSeanceCnt:outSeanceCnt-Presentation Property*/


	public class OutSeanceCnt extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public OutSeanceCnt(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Deprecated
		@Override
		public void setValueObject(final Object x){
			Int dummy = ((Int)x);
			setValue(dummy);
		}














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Jms::Unit.JmsHandler:outSeanceCnt:outSeanceCnt")
		@Deprecated
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Jms::Unit.JmsHandler:outSeanceCnt:outSeanceCnt")
		@Deprecated
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public OutSeanceCnt getOutSeanceCnt();
	/*Radix::Jms::Unit.JmsHandler:inSeanceCnt:inSeanceCnt-Presentation Property*/


	public class InSeanceCnt extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public InSeanceCnt(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Deprecated
		@Override
		public void setValueObject(final Object x){
			Int dummy = ((Int)x);
			setValue(dummy);
		}














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Jms::Unit.JmsHandler:inSeanceCnt:inSeanceCnt")
		@Deprecated
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Jms::Unit.JmsHandler:inSeanceCnt:inSeanceCnt")
		@Deprecated
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public InSeanceCnt getInSeanceCnt();
	/*Radix::Jms::Unit.JmsHandler:jmsLogin:jmsLogin-Presentation Property*/


	public class JmsLogin extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public JmsLogin(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Deprecated
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Jms::Unit.JmsHandler:jmsLogin:jmsLogin")
		@Deprecated
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Jms::Unit.JmsHandler:jmsLogin:jmsLogin")
		@Deprecated
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public JmsLogin getJmsLogin();
	/*Radix::Jms::Unit.JmsHandler:msRqQueueName:msRqQueueName-Presentation Property*/


	public class MsRqQueueName extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public MsRqQueueName(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Deprecated
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Jms::Unit.JmsHandler:msRqQueueName:msRqQueueName")
		@Deprecated
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Jms::Unit.JmsHandler:msRqQueueName:msRqQueueName")
		@Deprecated
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public MsRqQueueName getMsRqQueueName();


}

/* Radix::Jms::Unit.JmsHandler - Desktop Meta*/

/*Radix::Jms::Unit.JmsHandler-Application Class*/

package org.radixware.ads.Jms.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Unit.JmsHandler_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::Jms::Unit.JmsHandler:Presentations-Entity Object Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aclMU4BLUT6FJCZLDQ2WL6SCH43NE"),
			"Radix::Jms::Unit.JmsHandler",
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aclXIUFMDMMA7OBDCGDAALOMT5GDM"),
			null,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsSKMLZMFF4ZGCDEQD3BOVCLB4XE"),null,null,0,

			/*Radix::Jms::Unit.JmsHandler:Properties-Properties*/
			new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

					/*Radix::Jms::Unit.JmsHandler:jmsConnectProps:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colBC6VGIU6S5CFNAN6V4PG3DKCUQ"),
						"jmsConnectProps",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKD76YOIMTVG4ZBIBJ533FVFSNI"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclMU4BLUT6FJCZLDQ2WL6SCH43NE"),
						org.radixware.kernel.common.enums.EValType.CLOB,
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

						/*Radix::Jms::Unit.JmsHandler:jmsConnectProps:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						true,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,true),

					/*Radix::Jms::Unit.JmsHandler:jmsMessProps:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col2YNMUHO7AJCVTMJW2K5OPQ3GQQ"),
						"jmsMessProps",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3V4NOXZFWZCYRPBUR2SYTERCOM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclMU4BLUT6FJCZLDQ2WL6SCH43NE"),
						org.radixware.kernel.common.enums.EValType.CLOB,
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

						/*Radix::Jms::Unit.JmsHandler:jmsMessProps:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						true,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,true),

					/*Radix::Jms::Unit.JmsHandler:jmsMessFormat:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colA2LTDQXERRBJ5HAYLJT3KMSBEY"),
						"jmsMessFormat",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUW2EX6KF5ZD7XA3CPPLJ5S5D7U"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclMU4BLUT6FJCZLDQ2WL6SCH43NE"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.DETAIL_PROP,
						4,
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acsQEYA5D55Q5C7VDLY3SYRSDJ3HA"),
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("2"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::Jms::Unit.JmsHandler:jmsMessFormat:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acsQEYA5D55Q5C7VDLY3SYRSDJ3HA"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.NONE,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,true),

					/*Radix::Jms::Unit.JmsHandler:jmsLogin:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colO77HLWLCFFFWVIKV7MKDDEZY5A"),
						"jmsLogin",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWSFJFAUEY5AVXEE54MKNPMEL54"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclMU4BLUT6FJCZLDQ2WL6SCH43NE"),
						org.radixware.kernel.common.enums.EValType.STR,
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

						/*Radix::Jms::Unit.JmsHandler:jmsLogin:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,100,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,true),

					/*Radix::Jms::Unit.JmsHandler:jmsPassword:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colC5VS2BZB3RAHFNHR5SWKU5LBA4"),
						"jmsPassword",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMQMULH6N6VFAZCBENJ23WHN4XU"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclMU4BLUT6FJCZLDQ2WL6SCH43NE"),
						org.radixware.kernel.common.enums.EValType.STR,
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

						/*Radix::Jms::Unit.JmsHandler:jmsPassword:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,true,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,true),

					/*Radix::Jms::Unit.JmsHandler:msRqQueueName:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colRH4YQQJF4NCVNIMAN3HXIQSWLY"),
						"msRqQueueName",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPHBST3ZF3FH47MQ5662ELGLS64"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclMU4BLUT6FJCZLDQ2WL6SCH43NE"),
						org.radixware.kernel.common.enums.EValType.STR,
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

						/*Radix::Jms::Unit.JmsHandler:msRqQueueName:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,150,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,true),

					/*Radix::Jms::Unit.JmsHandler:msRsQueueName:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col37TNUN2QIVAWJE2HLAOG6OY4YA"),
						"msRsQueueName",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsE5BNZPJV55AMRPKBHF6HB33PHM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclMU4BLUT6FJCZLDQ2WL6SCH43NE"),
						org.radixware.kernel.common.enums.EValType.STR,
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

						/*Radix::Jms::Unit.JmsHandler:msRsQueueName:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,150,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,true),

					/*Radix::Jms::Unit.JmsHandler:outSeanceCnt:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKFSA4DVNEZEWFLARG7YYJFLVSE"),
						"outSeanceCnt",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJPVATRJI7BBWDKEDR45FCBF5DM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclMU4BLUT6FJCZLDQ2WL6SCH43NE"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.DETAIL_PROP,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("30"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Jms::Unit.JmsHandler:outSeanceCnt:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,true),

					/*Radix::Jms::Unit.JmsHandler:rsTimeout:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colCJFJTBWSSJH55NNIHQSUHEWUDY"),
						"rsTimeout",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsSZAQUZMWWZH3PIGLX576AG3N5Y"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclMU4BLUT6FJCZLDQ2WL6SCH43NE"),
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

						/*Radix::Jms::Unit.JmsHandler:rsTimeout:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999999999999L,999999999999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,true),

					/*Radix::Jms::Unit.JmsHandler:inSeanceCnt:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKHT276DSLVD7FKJLBW3IWOQMFM"),
						"inSeanceCnt",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5Z7FKVGSZBAQND3R6ASXATOXP4"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclMU4BLUT6FJCZLDQ2WL6SCH43NE"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.DETAIL_PROP,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("30"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Jms::Unit.JmsHandler:inSeanceCnt:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,true),

					/*Radix::Jms::Unit.JmsHandler:sapId:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKFFC226QSVEULHVCVM4KTLVGOA"),
						"sapId",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCTD3VQJMYJFKHLVWVCGKUX7Q6I"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclMU4BLUT6FJCZLDQ2WL6SCH43NE"),
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

						/*Radix::Jms::Unit.JmsHandler:sapId:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,true)
			},
			null,
			null,
			null,
			null,
			new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprJV6IPHCDKBFMJHMMQLL2HSSSLE"),org.radixware.kernel.common.types.Id.Factory.loadFrom("eprRADZIYXO75DFDKFEM2XQPRSQOE")},
			true,true,false);
}

/* Radix::Jms::Unit.JmsHandler - Web Executable*/

/*Radix::Jms::Unit.JmsHandler-Application Class*/

package org.radixware.ads.Jms.web;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Jms::Unit.JmsHandler")
public interface Unit.JmsHandler   extends org.radixware.ads.System.web.Unit.AbstractServiceDriver  {















































































































































































	/*Radix::Jms::Unit.JmsHandler:jmsMessProps:jmsMessProps-Presentation Property*/


	public class JmsMessProps extends org.radixware.kernel.common.client.models.items.properties.PropertyClob{
		public JmsMessProps(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Deprecated
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Jms::Unit.JmsHandler:jmsMessProps:jmsMessProps")
		@Deprecated
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Jms::Unit.JmsHandler:jmsMessProps:jmsMessProps")
		@Deprecated
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public JmsMessProps getJmsMessProps();
	/*Radix::Jms::Unit.JmsHandler:msRsQueueName:msRsQueueName-Presentation Property*/


	public class MsRsQueueName extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public MsRsQueueName(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Deprecated
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Jms::Unit.JmsHandler:msRsQueueName:msRsQueueName")
		@Deprecated
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Jms::Unit.JmsHandler:msRsQueueName:msRsQueueName")
		@Deprecated
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public MsRsQueueName getMsRsQueueName();
	/*Radix::Jms::Unit.JmsHandler:jmsMessFormat:jmsMessFormat-Presentation Property*/


	public class JmsMessFormat extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public JmsMessFormat(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Deprecated
		@Override
		public void setServerValue(org.radixware.kernel.common.client.models.items.properties.PropertyValue val){
			Object x = val.getValue();
			org.radixware.kernel.common.enums.EJmsMessageFormat dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EJmsMessageFormat ? (org.radixware.kernel.common.enums.EJmsMessageFormat)x : org.radixware.kernel.common.enums.EJmsMessageFormat.getForValue(((Int)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.INT)).longValue()));
			val.refineValue(dummy);
			super.setServerValue(val);
		}
		@Deprecated
		@Override
		public Class<org.radixware.kernel.common.enums.EJmsMessageFormat> getValClass(){
			return org.radixware.kernel.common.enums.EJmsMessageFormat.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Deprecated
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.enums.EJmsMessageFormat dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EJmsMessageFormat ? (org.radixware.kernel.common.enums.EJmsMessageFormat)x : org.radixware.kernel.common.enums.EJmsMessageFormat.getForValue(((Int)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.INT)).longValue()));
			setValue(dummy);
		}






				















		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Jms::Unit.JmsHandler:jmsMessFormat:jmsMessFormat")
		@Deprecated
		public  org.radixware.kernel.common.enums.EJmsMessageFormat getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Jms::Unit.JmsHandler:jmsMessFormat:jmsMessFormat")
		@Deprecated
		public   void setValue(org.radixware.kernel.common.enums.EJmsMessageFormat val) {
			Value = val;
		}
	}
	public JmsMessFormat getJmsMessFormat();
	/*Radix::Jms::Unit.JmsHandler:jmsConnectProps:jmsConnectProps-Presentation Property*/


	public class JmsConnectProps extends org.radixware.kernel.common.client.models.items.properties.PropertyClob{
		public JmsConnectProps(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Deprecated
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Jms::Unit.JmsHandler:jmsConnectProps:jmsConnectProps")
		@Deprecated
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Jms::Unit.JmsHandler:jmsConnectProps:jmsConnectProps")
		@Deprecated
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public JmsConnectProps getJmsConnectProps();
	/*Radix::Jms::Unit.JmsHandler:jmsPassword:jmsPassword-Presentation Property*/


	public class JmsPassword extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public JmsPassword(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Deprecated
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Jms::Unit.JmsHandler:jmsPassword:jmsPassword")
		@Deprecated
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Jms::Unit.JmsHandler:jmsPassword:jmsPassword")
		@Deprecated
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public JmsPassword getJmsPassword();
	/*Radix::Jms::Unit.JmsHandler:rsTimeout:rsTimeout-Presentation Property*/


	public class RsTimeout extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public RsTimeout(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Deprecated
		@Override
		public void setValueObject(final Object x){
			Int dummy = ((Int)x);
			setValue(dummy);
		}














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Jms::Unit.JmsHandler:rsTimeout:rsTimeout")
		@Deprecated
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Jms::Unit.JmsHandler:rsTimeout:rsTimeout")
		@Deprecated
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public RsTimeout getRsTimeout();
	/*Radix::Jms::Unit.JmsHandler:sapId:sapId-Presentation Property*/


	public class SapId extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public SapId(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Deprecated
		@Override
		public void setValueObject(final Object x){
			Int dummy = ((Int)x);
			setValue(dummy);
		}














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Jms::Unit.JmsHandler:sapId:sapId")
		@Deprecated
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Jms::Unit.JmsHandler:sapId:sapId")
		@Deprecated
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public SapId getSapId();
	/*Radix::Jms::Unit.JmsHandler:outSeanceCnt:outSeanceCnt-Presentation Property*/


	public class OutSeanceCnt extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public OutSeanceCnt(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Deprecated
		@Override
		public void setValueObject(final Object x){
			Int dummy = ((Int)x);
			setValue(dummy);
		}














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Jms::Unit.JmsHandler:outSeanceCnt:outSeanceCnt")
		@Deprecated
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Jms::Unit.JmsHandler:outSeanceCnt:outSeanceCnt")
		@Deprecated
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public OutSeanceCnt getOutSeanceCnt();
	/*Radix::Jms::Unit.JmsHandler:inSeanceCnt:inSeanceCnt-Presentation Property*/


	public class InSeanceCnt extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public InSeanceCnt(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Deprecated
		@Override
		public void setValueObject(final Object x){
			Int dummy = ((Int)x);
			setValue(dummy);
		}














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Jms::Unit.JmsHandler:inSeanceCnt:inSeanceCnt")
		@Deprecated
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Jms::Unit.JmsHandler:inSeanceCnt:inSeanceCnt")
		@Deprecated
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public InSeanceCnt getInSeanceCnt();
	/*Radix::Jms::Unit.JmsHandler:jmsLogin:jmsLogin-Presentation Property*/


	public class JmsLogin extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public JmsLogin(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Deprecated
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Jms::Unit.JmsHandler:jmsLogin:jmsLogin")
		@Deprecated
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Jms::Unit.JmsHandler:jmsLogin:jmsLogin")
		@Deprecated
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public JmsLogin getJmsLogin();
	/*Radix::Jms::Unit.JmsHandler:msRqQueueName:msRqQueueName-Presentation Property*/


	public class MsRqQueueName extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public MsRqQueueName(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Deprecated
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Jms::Unit.JmsHandler:msRqQueueName:msRqQueueName")
		@Deprecated
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Jms::Unit.JmsHandler:msRqQueueName:msRqQueueName")
		@Deprecated
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public MsRqQueueName getMsRqQueueName();


}

/* Radix::Jms::Unit.JmsHandler - Web Meta*/

/*Radix::Jms::Unit.JmsHandler-Application Class*/

package org.radixware.ads.Jms.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Unit.JmsHandler_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::Jms::Unit.JmsHandler:Presentations-Entity Object Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aclMU4BLUT6FJCZLDQ2WL6SCH43NE"),
			"Radix::Jms::Unit.JmsHandler",
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aclXIUFMDMMA7OBDCGDAALOMT5GDM"),
			null,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsSKMLZMFF4ZGCDEQD3BOVCLB4XE"),null,null,0,

			/*Radix::Jms::Unit.JmsHandler:Properties-Properties*/
			new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

					/*Radix::Jms::Unit.JmsHandler:jmsConnectProps:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colBC6VGIU6S5CFNAN6V4PG3DKCUQ"),
						"jmsConnectProps",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKD76YOIMTVG4ZBIBJ533FVFSNI"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclMU4BLUT6FJCZLDQ2WL6SCH43NE"),
						org.radixware.kernel.common.enums.EValType.CLOB,
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

						/*Radix::Jms::Unit.JmsHandler:jmsConnectProps:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						true,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,true),

					/*Radix::Jms::Unit.JmsHandler:jmsMessProps:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col2YNMUHO7AJCVTMJW2K5OPQ3GQQ"),
						"jmsMessProps",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3V4NOXZFWZCYRPBUR2SYTERCOM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclMU4BLUT6FJCZLDQ2WL6SCH43NE"),
						org.radixware.kernel.common.enums.EValType.CLOB,
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

						/*Radix::Jms::Unit.JmsHandler:jmsMessProps:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						true,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,true),

					/*Radix::Jms::Unit.JmsHandler:jmsMessFormat:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colA2LTDQXERRBJ5HAYLJT3KMSBEY"),
						"jmsMessFormat",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUW2EX6KF5ZD7XA3CPPLJ5S5D7U"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclMU4BLUT6FJCZLDQ2WL6SCH43NE"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.DETAIL_PROP,
						4,
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acsQEYA5D55Q5C7VDLY3SYRSDJ3HA"),
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("2"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::Jms::Unit.JmsHandler:jmsMessFormat:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acsQEYA5D55Q5C7VDLY3SYRSDJ3HA"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.NONE,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,true),

					/*Radix::Jms::Unit.JmsHandler:jmsLogin:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colO77HLWLCFFFWVIKV7MKDDEZY5A"),
						"jmsLogin",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWSFJFAUEY5AVXEE54MKNPMEL54"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclMU4BLUT6FJCZLDQ2WL6SCH43NE"),
						org.radixware.kernel.common.enums.EValType.STR,
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

						/*Radix::Jms::Unit.JmsHandler:jmsLogin:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,100,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,true),

					/*Radix::Jms::Unit.JmsHandler:jmsPassword:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colC5VS2BZB3RAHFNHR5SWKU5LBA4"),
						"jmsPassword",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMQMULH6N6VFAZCBENJ23WHN4XU"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclMU4BLUT6FJCZLDQ2WL6SCH43NE"),
						org.radixware.kernel.common.enums.EValType.STR,
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

						/*Radix::Jms::Unit.JmsHandler:jmsPassword:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,true,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,true),

					/*Radix::Jms::Unit.JmsHandler:msRqQueueName:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colRH4YQQJF4NCVNIMAN3HXIQSWLY"),
						"msRqQueueName",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPHBST3ZF3FH47MQ5662ELGLS64"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclMU4BLUT6FJCZLDQ2WL6SCH43NE"),
						org.radixware.kernel.common.enums.EValType.STR,
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

						/*Radix::Jms::Unit.JmsHandler:msRqQueueName:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,150,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,true),

					/*Radix::Jms::Unit.JmsHandler:msRsQueueName:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col37TNUN2QIVAWJE2HLAOG6OY4YA"),
						"msRsQueueName",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsE5BNZPJV55AMRPKBHF6HB33PHM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclMU4BLUT6FJCZLDQ2WL6SCH43NE"),
						org.radixware.kernel.common.enums.EValType.STR,
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

						/*Radix::Jms::Unit.JmsHandler:msRsQueueName:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,150,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,true),

					/*Radix::Jms::Unit.JmsHandler:outSeanceCnt:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKFSA4DVNEZEWFLARG7YYJFLVSE"),
						"outSeanceCnt",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJPVATRJI7BBWDKEDR45FCBF5DM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclMU4BLUT6FJCZLDQ2WL6SCH43NE"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.DETAIL_PROP,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("30"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Jms::Unit.JmsHandler:outSeanceCnt:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,true),

					/*Radix::Jms::Unit.JmsHandler:rsTimeout:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colCJFJTBWSSJH55NNIHQSUHEWUDY"),
						"rsTimeout",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsSZAQUZMWWZH3PIGLX576AG3N5Y"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclMU4BLUT6FJCZLDQ2WL6SCH43NE"),
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

						/*Radix::Jms::Unit.JmsHandler:rsTimeout:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999999999999L,999999999999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,true),

					/*Radix::Jms::Unit.JmsHandler:inSeanceCnt:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKHT276DSLVD7FKJLBW3IWOQMFM"),
						"inSeanceCnt",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5Z7FKVGSZBAQND3R6ASXATOXP4"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclMU4BLUT6FJCZLDQ2WL6SCH43NE"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.DETAIL_PROP,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("30"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Jms::Unit.JmsHandler:inSeanceCnt:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,true),

					/*Radix::Jms::Unit.JmsHandler:sapId:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKFFC226QSVEULHVCVM4KTLVGOA"),
						"sapId",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCTD3VQJMYJFKHLVWVCGKUX7Q6I"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclMU4BLUT6FJCZLDQ2WL6SCH43NE"),
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

						/*Radix::Jms::Unit.JmsHandler:sapId:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,true)
			},
			null,
			null,
			null,
			null,
			null,
			true,true,false);
}

/* Radix::Jms::Unit.JmsHandler:Edit - Desktop Meta*/

/*Radix::Jms::Unit.JmsHandler:Edit-Editor Presentation*/

package org.radixware.ads.Jms.explorer;
public final class Edit_mi{
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new org.radixware.kernel.common.client.meta.RadEditorPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprJV6IPHCDKBFMJHMMQLL2HSSSLE"),
	"Edit",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
	org.radixware.kernel.common.types.Id.Factory.loadFrom("eprE7BZV3MWA7OBDCGDAALOMT5GDM"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("aclMU4BLUT6FJCZLDQ2WL6SCH43NE"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl5HP4XTP3EGWDBRCRAAIT4AGD7E"),
	null,
	null,

	/*Radix::Jms::Unit.JmsHandler:Edit:Editor Pages-Editor Presentation Pages*/
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

			/*Radix::Jms::Unit.JmsHandler:Edit:General-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgOCDVZAOTBXOBDNTPAAMPGXSZKU"),"General",org.radixware.kernel.common.types.Id.Factory.loadFrom("aclMU4BLUT6FJCZLDQ2WL6SCH43NE"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mls36UC6GXBT5HSNKJYXNELU4V2JE"),null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEVLUYADHR5VDBNSIAAUMFADAIA"),0,0,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdU7QY4J3ZXDORDF72ABIFNQAABA"),0,2,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colI6NERFZYS5VDBFCXAAUMFADAIA"),0,3,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colY5NZZOJBXTNRDB6QAALOMT5GDM"),0,4,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEVX5YH2SS5VDBN2IAAUMFADAIA"),0,5,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIREKNEWHBDWDRD25AAYQQMVFBB"),0,6,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdP2JMQNROX5GE5POX2SH6R2DLLA"),0,7,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colVA2Y4NZKIZAS7DIOUYDAT6VCQI"),0,1,1,false,false)
			},null),

			/*Radix::Jms::Unit.JmsHandler:Edit:TraceOptions-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgUJB2RCH6KZHD5IMTQTE76DQSZA"),"TraceOptions",org.radixware.kernel.common.types.Id.Factory.loadFrom("aclMU4BLUT6FJCZLDQ2WL6SCH43NE"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLXWAXVDJZNH45PPT3CA7C4YRTM"),null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col33BGOAFDTZGY3IZ3IUZLVC2UPM"),0,0,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colZF4DFPFNQZFWXENBIJJQ7HAYSY"),0,1,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col6L4O4LIEOFAGTJ4KMHSY5RMTZE"),0,2,1,false,false)
			},null),

			/*Radix::Jms::Unit.JmsHandler:Edit:Settings-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgCSEXGV454FGTVDPBTUAL7ZM2HY"),"Settings",org.radixware.kernel.common.types.Id.Factory.loadFrom("aclMU4BLUT6FJCZLDQ2WL6SCH43NE"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPIXIMA6OQZGKNIADU77YDQIMCU"),null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKHT276DSLVD7FKJLBW3IWOQMFM"),0,7,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colBC6VGIU6S5CFNAN6V4PG3DKCUQ"),0,0,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colO77HLWLCFFFWVIKV7MKDDEZY5A"),0,3,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colA2LTDQXERRBJ5HAYLJT3KMSBEY"),0,2,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col2YNMUHO7AJCVTMJW2K5OPQ3GQQ"),0,1,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colC5VS2BZB3RAHFNHR5SWKU5LBA4"),0,4,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colRH4YQQJF4NCVNIMAN3HXIQSWLY"),0,5,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col37TNUN2QIVAWJE2HLAOG6OY4YA"),0,6,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKFSA4DVNEZEWFLARG7YYJFLVSE"),0,8,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colCJFJTBWSSJH55NNIHQSUHEWUDY"),0,9,1,false,false)
			},null)
	},
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgOCDVZAOTBXOBDNTPAAMPGXSZKU")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgCSEXGV454FGTVDPBTUAL7ZM2HY")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgUJB2RCH6KZHD5IMTQTE76DQSZA")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgJ4BHY7HBLBBBXIIQZEPCKTW2MU"))}
	,

	/*Radix::Jms::Unit.JmsHandler:Edit:Children-Explorer Items*/
		null
	,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
	null,
	16,
	new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdF3SRJU4KOXOBDCJRAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdTZE2P7EKOXOBDCJRAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdQWDXKHRSO7OBDCJTAALOMT5GDM")},
	(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
	35984,0,0);
}
/* Radix::Jms::Unit.JmsHandler:Edit - Web Meta*/

/*Radix::Jms::Unit.JmsHandler:Edit-Editor Presentation*/

package org.radixware.ads.Jms.web;
public final class Edit_mi{
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new org.radixware.kernel.common.client.meta.RadEditorPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprJV6IPHCDKBFMJHMMQLL2HSSSLE"),
	"Edit",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
	org.radixware.kernel.common.types.Id.Factory.loadFrom("eprE7BZV3MWA7OBDCGDAALOMT5GDM"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("aclMU4BLUT6FJCZLDQ2WL6SCH43NE"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl5HP4XTP3EGWDBRCRAAIT4AGD7E"),
	null,
	null,

	/*Radix::Jms::Unit.JmsHandler:Edit:Editor Pages-Editor Presentation Pages*/
	null,
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
	}
	,

	/*Radix::Jms::Unit.JmsHandler:Edit:Children-Explorer Items*/
		null
	,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
	null,
	16,
	new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdF3SRJU4KOXOBDCJRAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdTZE2P7EKOXOBDCJRAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdQWDXKHRSO7OBDCJTAALOMT5GDM")},
	(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
	35984,0,0);
}
/* Radix::Jms::Unit.JmsHandler:Edit:Model - Desktop Executable*/

/*Radix::Jms::Unit.JmsHandler:Edit:Model-Entity Model Class*/

package org.radixware.ads.Jms.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Jms::Unit.JmsHandler:Edit:Model")
public class Edit:Model  extends org.radixware.ads.Jms.explorer.Unit.JmsHandler.Unit.JmsHandler_DefaultModel.eprE7BZV3MWA7OBDCGDAALOMT5GDM_ModelAdapter {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return Edit:Model_mi.rdxMeta; }



	public Edit:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadEditorPresentationDef def){super(userSession,def);}

	/*Radix::Jms::Unit.JmsHandler:Edit:Model:Nested classes-Nested Classes*/

	/*Radix::Jms::Unit.JmsHandler:Edit:Model:Properties-Properties*/

	/*Radix::Jms::Unit.JmsHandler:Edit:Model:Methods-Methods*/


}

/* Radix::Jms::Unit.JmsHandler:Edit:Model - Desktop Meta*/

/*Radix::Jms::Unit.JmsHandler:Edit:Model-Entity Model Class*/

package org.radixware.ads.Jms.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Edit:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aemJV6IPHCDKBFMJHMMQLL2HSSSLE"),
						"Edit:Model",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aemE7BZV3MWA7OBDCGDAALOMT5GDM"),
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::Jms::Unit.JmsHandler:Edit:Model:Properties-Properties*/
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

/* Radix::Jms::Unit.JmsHandler:Edit:Model - Web Executable*/

/*Radix::Jms::Unit.JmsHandler:Edit:Model-Entity Model Class*/

package org.radixware.ads.Jms.web;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Jms::Unit.JmsHandler:Edit:Model")
public class Edit:Model  extends org.radixware.ads.Jms.web.Unit.JmsHandler.Unit.JmsHandler_DefaultModel.eprE7BZV3MWA7OBDCGDAALOMT5GDM_ModelAdapter {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return Edit:Model_mi.rdxMeta; }



	public Edit:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadEditorPresentationDef def){super(userSession,def);}

	/*Radix::Jms::Unit.JmsHandler:Edit:Model:Nested classes-Nested Classes*/

	/*Radix::Jms::Unit.JmsHandler:Edit:Model:Properties-Properties*/

	/*Radix::Jms::Unit.JmsHandler:Edit:Model:Methods-Methods*/


}

/* Radix::Jms::Unit.JmsHandler:Edit:Model - Web Meta*/

/*Radix::Jms::Unit.JmsHandler:Edit:Model-Entity Model Class*/

package org.radixware.ads.Jms.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Edit:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aemJV6IPHCDKBFMJHMMQLL2HSSSLE"),
						"Edit:Model",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aemE7BZV3MWA7OBDCGDAALOMT5GDM"),
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::Jms::Unit.JmsHandler:Edit:Model:Properties-Properties*/
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

/* Radix::Jms::Unit.JmsHandler:Create - Desktop Meta*/

/*Radix::Jms::Unit.JmsHandler:Create-Editor Presentation*/

package org.radixware.ads.Jms.explorer;
public final class Create_mi{
	private static final class Create_DEF extends org.radixware.kernel.common.client.meta.RadEditorPresentationDef{
		Create_DEF(){
			super(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprRADZIYXO75DFDKFEM2XQPRSQOE"),
			"Create",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("eprJV6IPHCDKBFMJHMMQLL2HSSSLE"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aclMU4BLUT6FJCZLDQ2WL6SCH43NE"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl5HP4XTP3EGWDBRCRAAIT4AGD7E"),
			null,
			null,
			null,
			null,

			/*Radix::Jms::Unit.JmsHandler:Create:Children-Explorer Items*/
				null
			,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
			null,
			0,
			null,
			(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
			40114,0,0);
		}
		@Override
		protected org.radixware.kernel.common.client.models.Model createModelImpl(org.radixware.kernel.common.client.IClientEnvironment userSession) {    return new org.radixware.ads.Jms.explorer.Edit:Model(userSession,this);
		}
	}
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new Create_DEF(); 
;
}
/* Radix::Jms::Unit.JmsHandler:Create - Web Meta*/

/*Radix::Jms::Unit.JmsHandler:Create-Editor Presentation*/

package org.radixware.ads.Jms.web;
public final class Create_mi{
	private static final class Create_DEF extends org.radixware.kernel.common.client.meta.RadEditorPresentationDef{
		Create_DEF(){
			super(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprRADZIYXO75DFDKFEM2XQPRSQOE"),
			"Create",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("eprJV6IPHCDKBFMJHMMQLL2HSSSLE"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aclMU4BLUT6FJCZLDQ2WL6SCH43NE"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl5HP4XTP3EGWDBRCRAAIT4AGD7E"),
			null,
			null,
			null,
			null,

			/*Radix::Jms::Unit.JmsHandler:Create:Children-Explorer Items*/
				null
			,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
			null,
			0,
			null,
			(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
			40114,0,0);
		}
		@Override
		protected org.radixware.kernel.common.client.models.Model createModelImpl(org.radixware.kernel.common.client.IClientEnvironment userSession) {    return new org.radixware.ads.Jms.web.Edit:Model(userSession,this);
		}
	}
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new Create_DEF(); 
;
}
/* Radix::Jms::Unit.JmsHandler - Localizing Bundle */
package org.radixware.ads.Jms.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Unit.JmsHandler - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"General");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Общее");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls36UC6GXBT5HSNKJYXNELU4V2JE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Limit of requests to external system (%1) exceeded in \"%2\"");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"В \"%2\" превышен лимит количества запросов к внешней системе (%1)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3POFK2JNWFCXPPCDMU2CLIDXVY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.EVENT_CODE,org.radixware.kernel.common.enums.EEventSeverity.ERROR,"App",null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Message properties");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Свойства сообщения");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3V4NOXZFWZCYRPBUR2SYTERCOM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"JMS properties parsing error: %1");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ошибка при разборе свойств JMS: %1");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5WUQPFRVUVG3THPEAL3OE5TVA4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.EVENT_CODE,org.radixware.kernel.common.enums.EEventSeverity.ERROR,"App",null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Maximum session count");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Максимальное количество сеансов");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5Z7FKVGSZBAQND3R6ASXATOXP4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"SAP ID");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"SAP ID");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCTD3VQJMYJFKHLVWVCGKUX7Q6I"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Responses queue name");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Имя очереди ответов");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsE5BNZPJV55AMRPKBHF6HB33PHM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Limit of requests from external system (%1) exceeded in \"%2\"");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"В \"%2\" превышен лимит количества запросов от внешней системы (%1)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFVGZTPQLB5CPBPABI5SYPSICH4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.EVENT_CODE,org.radixware.kernel.common.enums.EEventSeverity.ERROR,"App",null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Maximum session count");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Максимальное количество сеансов");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJPVATRJI7BBWDKEDR45FCBF5DM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Connection properties");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Свойства соединения");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKD76YOIMTVG4ZBIBJ533FVFSNI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Error reading parameters of service \"%1\": %2");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ошибка при чтении параметров сервиса \"%1\": %2");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKYLUGGXB7BF7JLGCTXDMDTPEIA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.EVENT_CODE,org.radixware.kernel.common.enums.EEventSeverity.ERROR,"Server.Unit.JmsHandler",null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Trace Settings");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Настройки трассы");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLXWAXVDJZNH45PPT3CA7C4YRTM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Password");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Пароль");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMQMULH6N6VFAZCBENJ23WHN4XU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Service method.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Служебный метод.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMYXDDPWJCRCHLOZD24YYDBZCHE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Service Server: uncorrelated response received (id=%1)");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Сервер сервиса: получен нескоpрелированный ответ  (id=%1)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsP6IS4CUI5VEXRJLB3UUZ55CATU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.EVENT_CODE,org.radixware.kernel.common.enums.EEventSeverity.ERROR,"App",null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Requests queue name");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Имя очереди запросов");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPHBST3ZF3FH47MQ5662ELGLS64"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Settings");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Настройки");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPIXIMA6OQZGKNIADU77YDQIMCU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Service Server: timeout of response from external system exceeded (ID=%1)");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Сервер сервиса: тай-маут приема от внешней системы  (ID=%1)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQWV54QYK3ZAEFF7YUBM5EQIXSY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.EVENT_CODE,org.radixware.kernel.common.enums.EEventSeverity.ERROR,"App",null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"JMS");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"JMS");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsSKMLZMFF4ZGCDEQD3BOVCLB4XE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Response timeout (s)");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Время ожидания ответов (с)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsSZAQUZMWWZH3PIGLX576AG3N5Y"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Error sending/receiving JMS \"%1\": %2");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ошибка при отправке-получении JMS \"%1\": %2");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTK4JYYAUQBBPZLAKYYIKZN4EFU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.EVENT_CODE,org.radixware.kernel.common.enums.EEventSeverity.ERROR,"App",null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Message format");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Формат сообщения");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUW2EX6KF5ZD7XA3CPPLJ5S5D7U"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Error starting JMS connection of %1: %2");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ошибка при запуске JMS соединения %1: %2");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWGRHNJL575FRRKLIK2VCUN3QDM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.EVENT_CODE,org.radixware.kernel.common.enums.EEventSeverity.ERROR,"App",null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Login");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Пользователь");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWSFJFAUEY5AVXEE54MKNPMEL54"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"JMS message handler.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Обработчик JMS-сообщений.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXGAND6YLEVBFFIH5LHJSQNN3LI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(Unit.JmsHandler - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbaclMU4BLUT6FJCZLDQ2WL6SCH43NE"),"Unit.JmsHandler - Localizing Bundle",$$$items$$$);
}
