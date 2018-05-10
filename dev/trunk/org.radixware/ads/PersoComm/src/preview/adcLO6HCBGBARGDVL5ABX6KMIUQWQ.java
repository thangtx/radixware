
/* Radix::PersoComm::NotificationSenderEvent - Server Executable*/

/*Radix::PersoComm::NotificationSenderEvent-Server Dynamic Class*/

package org.radixware.ads.PersoComm.server;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::NotificationSenderEvent")
public published class NotificationSenderEvent  implements org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {


	/**Metainformation accessor method*/
	@SuppressWarnings("unused")
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return NotificationSenderEvent_mi.rdxMeta;}

	/*Radix::PersoComm::NotificationSenderEvent:Nested classes-Nested Classes*/

	/*Radix::PersoComm::NotificationSenderEvent:Properties-Properties*/

	/*Radix::PersoComm::NotificationSenderEvent:entity-Dynamic Property*/



	protected org.radixware.ads.Types.server.Entity entity=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::NotificationSenderEvent:entity")
	public published  org.radixware.ads.Types.server.Entity getEntity() {
		return entity;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::NotificationSenderEvent:entity")
	private published   void setEntity(org.radixware.ads.Types.server.Entity val) {
		entity = val;
	}

	/*Radix::PersoComm::NotificationSenderEvent:args-Dynamic Property*/



	protected java.util.Map<Str,java.lang.Object> args=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::NotificationSenderEvent:args")
	public published  java.util.Map<Str,java.lang.Object> getArgs() {
		return args;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::NotificationSenderEvent:args")
	private published   void setArgs(java.util.Map<Str,java.lang.Object> val) {
		args = val;
	}

	/*Radix::PersoComm::NotificationSenderEvent:userGroup-Dynamic Property*/



	protected org.radixware.ads.Acs.server.UserGroup userGroup=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::NotificationSenderEvent:userGroup")
	public published  org.radixware.ads.Acs.server.UserGroup getUserGroup() {
		return userGroup;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::NotificationSenderEvent:userGroup")
	private published   void setUserGroup(org.radixware.ads.Acs.server.UserGroup val) {
		userGroup = val;
	}

	/*Radix::PersoComm::NotificationSenderEvent:user-Dynamic Property*/



	protected org.radixware.ads.Acs.server.User user=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::NotificationSenderEvent:user")
	public published  org.radixware.ads.Acs.server.User getUser() {
		return user;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::NotificationSenderEvent:user")
	private published   void setUser(org.radixware.ads.Acs.server.User val) {
		user = val;
	}

	/*Radix::PersoComm::NotificationSenderEvent:recipients-Dynamic Property*/



	protected java.util.List<org.radixware.kernel.common.utils.Pair<org.radixware.ads.PersoComm.common.ChannelKind,Str>> recipients=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::NotificationSenderEvent:recipients")
	public published  java.util.List<org.radixware.kernel.common.utils.Pair<org.radixware.ads.PersoComm.common.ChannelKind,Str>> getRecipients() {
		return recipients;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::NotificationSenderEvent:recipients")
	private published   void setRecipients(java.util.List<org.radixware.kernel.common.utils.Pair<org.radixware.ads.PersoComm.common.ChannelKind,Str>> val) {
		recipients = val;
	}

	/*Radix::PersoComm::NotificationSenderEvent:attachments-Dynamic Property*/



	protected java.util.List<org.radixware.ads.PersoComm.server.Attachment> attachments=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::NotificationSenderEvent:attachments")
	public published  java.util.List<org.radixware.ads.PersoComm.server.Attachment> getAttachments() {
		return attachments;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::NotificationSenderEvent:attachments")
	private published   void setAttachments(java.util.List<org.radixware.ads.PersoComm.server.Attachment> val) {
		attachments = val;
	}

	/*Radix::PersoComm::NotificationSenderEvent:sendCallbackClassName-Dynamic Property*/



	protected Str sendCallbackClassName=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::NotificationSenderEvent:sendCallbackClassName")
	public published  Str getSendCallbackClassName() {
		return sendCallbackClassName;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::NotificationSenderEvent:sendCallbackClassName")
	public published   void setSendCallbackClassName(Str val) {
		sendCallbackClassName = val;
	}

	/*Radix::PersoComm::NotificationSenderEvent:sendCallbackMethodName-Dynamic Property*/



	protected Str sendCallbackMethodName=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::NotificationSenderEvent:sendCallbackMethodName")
	public published  Str getSendCallbackMethodName() {
		return sendCallbackMethodName;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::NotificationSenderEvent:sendCallbackMethodName")
	public published   void setSendCallbackMethodName(Str val) {
		sendCallbackMethodName = val;
	}

	/*Radix::PersoComm::NotificationSenderEvent:sendCallbackData-Dynamic Property*/



	protected Str sendCallbackData=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::NotificationSenderEvent:sendCallbackData")
	public published  Str getSendCallbackData() {
		return sendCallbackData;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::NotificationSenderEvent:sendCallbackData")
	public published   void setSendCallbackData(Str val) {
		sendCallbackData = val;
	}

	/*Radix::PersoComm::NotificationSenderEvent:deliveryCallbackClassName-Dynamic Property*/



	protected Str deliveryCallbackClassName=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::NotificationSenderEvent:deliveryCallbackClassName")
	public published  Str getDeliveryCallbackClassName() {
		return deliveryCallbackClassName;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::NotificationSenderEvent:deliveryCallbackClassName")
	public published   void setDeliveryCallbackClassName(Str val) {
		deliveryCallbackClassName = val;
	}

	/*Radix::PersoComm::NotificationSenderEvent:deliveryCallbackMethodName-Dynamic Property*/



	protected Str deliveryCallbackMethodName=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::NotificationSenderEvent:deliveryCallbackMethodName")
	public published  Str getDeliveryCallbackMethodName() {
		return deliveryCallbackMethodName;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::NotificationSenderEvent:deliveryCallbackMethodName")
	public published   void setDeliveryCallbackMethodName(Str val) {
		deliveryCallbackMethodName = val;
	}

	/*Radix::PersoComm::NotificationSenderEvent:deliveryCallbackData-Dynamic Property*/



	protected Str deliveryCallbackData=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::NotificationSenderEvent:deliveryCallbackData")
	public published  Str getDeliveryCallbackData() {
		return deliveryCallbackData;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::NotificationSenderEvent:deliveryCallbackData")
	public published   void setDeliveryCallbackData(Str val) {
		deliveryCallbackData = val;
	}

	/*Radix::PersoComm::NotificationSenderEvent:deliveryTimeoutSeconds-Dynamic Property*/



	protected Int deliveryTimeoutSeconds=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::NotificationSenderEvent:deliveryTimeoutSeconds")
	public published  Int getDeliveryTimeoutSeconds() {
		return deliveryTimeoutSeconds;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::NotificationSenderEvent:deliveryTimeoutSeconds")
	public published   void setDeliveryTimeoutSeconds(Int val) {
		deliveryTimeoutSeconds = val;
	}











































































































	/*Radix::PersoComm::NotificationSenderEvent:Methods-Methods*/

	/*Radix::PersoComm::NotificationSenderEvent:NotificationSenderEvent-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::NotificationSenderEvent:NotificationSenderEvent")
	private  NotificationSenderEvent (org.radixware.ads.Types.server.Entity entity, org.radixware.ads.Acs.server.User user, org.radixware.ads.Acs.server.UserGroup userGroup, java.util.List<org.radixware.kernel.common.utils.Pair<org.radixware.ads.PersoComm.common.ChannelKind,Str>> recipients, java.util.List<org.radixware.ads.PersoComm.server.Attachment> attachments, java.util.Map<Str,java.lang.Object> args) {
		this.entity = entity;
		this.user = user;
		this.userGroup = userGroup;
		this.recipients = recipients;
		this.args = args == null ? null : java.util.Collections.unmodifiableMap(args);
		this.attachments = attachments;

	}

	/*Radix::PersoComm::NotificationSenderEvent:NotificationSenderEvent-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::NotificationSenderEvent:NotificationSenderEvent")
	public published  NotificationSenderEvent (org.radixware.ads.Types.server.Entity entity, org.radixware.ads.Acs.server.User user, java.util.Map<Str,java.lang.Object> args, java.util.List<org.radixware.ads.PersoComm.server.Attachment> attachments) {
		this(entity, user, null, null, attachments, args);

	}

	/*Radix::PersoComm::NotificationSenderEvent:NotificationSenderEvent-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::NotificationSenderEvent:NotificationSenderEvent")
	public published  NotificationSenderEvent (org.radixware.ads.Types.server.Entity entity, org.radixware.ads.Acs.server.UserGroup userGroup, java.util.Map<Str,java.lang.Object> args, java.util.List<org.radixware.ads.PersoComm.server.Attachment> attachments) {
		this(entity, null, userGroup, null, attachments, args);

	}

	/*Radix::PersoComm::NotificationSenderEvent:NotificationSenderEvent-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::NotificationSenderEvent:NotificationSenderEvent")
	public published  NotificationSenderEvent (org.radixware.ads.Types.server.Entity entity, java.util.List<org.radixware.kernel.common.utils.Pair<org.radixware.ads.PersoComm.common.ChannelKind,Str>> recipients, java.util.Map<Str,java.lang.Object> args, java.util.List<org.radixware.ads.PersoComm.server.Attachment> attachments) {
		this(entity, null, null, recipients, attachments, args);

	}

	/*Radix::PersoComm::NotificationSenderEvent:NotificationSenderEvent-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::NotificationSenderEvent:NotificationSenderEvent")
	public published  NotificationSenderEvent (org.radixware.ads.Types.server.Entity entity, java.util.Map<Str,java.lang.Object> args, java.util.List<org.radixware.ads.PersoComm.server.Attachment> attachments) {
		this(entity, null, null, null, attachments, args);

	}


}

/* Radix::PersoComm::NotificationSenderEvent - Server Meta*/

/*Radix::PersoComm::NotificationSenderEvent-Server Dynamic Class*/

package org.radixware.ads.PersoComm.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class NotificationSenderEvent_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("adcLO6HCBGBARGDVL5ABX6KMIUQWQ"),"NotificationSenderEvent",null,

						org.radixware.kernel.common.enums.EClassType.DYNAMIC,
						null,
						null,
						null,

						/*Radix::PersoComm::NotificationSenderEvent:Properties-Properties*/
						new org.radixware.kernel.server.meta.clazzes.RadPropDef[]{

								/*Radix::PersoComm::NotificationSenderEvent:entity-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdBOEIVAQK2NCLXJZ5FSLJPRQ4ZQ"),"entity",null,org.radixware.kernel.common.enums.EValType.USER_CLASS,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::NotificationSenderEvent:args-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdQJOMPXXQ25CVPPK3KF4VBL4UAM"),"args",null,org.radixware.kernel.common.enums.EValType.JAVA_CLASS,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::NotificationSenderEvent:userGroup-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicRefPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdRA5765HMEJCWPIYVICSQJMX4GE"),"userGroup",null,org.radixware.kernel.common.enums.EValType.USER_CLASS,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("aecQ23AYDTTGLNRDHRZABQAQH3XQ4"),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::NotificationSenderEvent:user-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicRefPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdQGBIELHJGNFIZG4DDUI6C6GMXI"),"user",null,org.radixware.kernel.common.enums.EValType.USER_CLASS,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::NotificationSenderEvent:recipients-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd5FCU2UMNMBGYJGPIOGVJE3ZVFU"),"recipients",null,org.radixware.kernel.common.enums.EValType.JAVA_CLASS,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::NotificationSenderEvent:attachments-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd5D6NATLXWBGMBC4S66P7CVWXVI"),"attachments",null,org.radixware.kernel.common.enums.EValType.JAVA_CLASS,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::NotificationSenderEvent:sendCallbackClassName-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd2Q2AQMEWSJFX5DKFDDQLGVSSBI"),"sendCallbackClassName",null,org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::NotificationSenderEvent:sendCallbackMethodName-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdNVVRMLEWYFAVREGBTFO5XS5ETU"),"sendCallbackMethodName",null,org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::NotificationSenderEvent:sendCallbackData-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdCIVXTUWYNJFSBG2XJIGVUWLFBY"),"sendCallbackData",null,org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::NotificationSenderEvent:deliveryCallbackClassName-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdI3WDYSNOXFCAHELOG7VSZWBNGY"),"deliveryCallbackClassName",null,org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::NotificationSenderEvent:deliveryCallbackMethodName-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdI3GWQDPTENBXTOFDOCWGVHICPQ"),"deliveryCallbackMethodName",null,org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::NotificationSenderEvent:deliveryCallbackData-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd2XL2DAMVZNFTPO3GBJRXAPYPNA"),"deliveryCallbackData",null,org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::NotificationSenderEvent:deliveryTimeoutSeconds-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd6G6VNH6J35DQVNCBH3LXRFCI4U"),"deliveryTimeoutSeconds",null,org.radixware.kernel.common.enums.EValType.INT,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE)
						},

						/*Radix::PersoComm::NotificationSenderEvent:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthJUVVZKSB2ZHPTLAUFFVIV6JXKQ"),"NotificationSenderEvent",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("entity",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprRG5NZWGCD5HPZNK5GD7AACADHY")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("user",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprCD5JBFJ7NZAINPQV52WW53ACHA")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("userGroup",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprZRIR5EK2WVA7HIDBH36RMFRTRQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("recipients",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr4TPXOKX3EZH5DD45O76X6NYZUY")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("attachments",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprQXUEHCCCSNDYZGHEWIL4NX45TE")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("args",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprITC4IEU3SBAHLK2IFGXDFZ7QXM"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth3MZS2CWDYJDRNKZZRCHM7Z4JRY"),"NotificationSenderEvent",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("entity",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprRG5NZWGCD5HPZNK5GD7AACADHY")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("user",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprCD5JBFJ7NZAINPQV52WW53ACHA")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("args",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprITC4IEU3SBAHLK2IFGXDFZ7QXM")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("attachments",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprQXUEHCCCSNDYZGHEWIL4NX45TE"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthXUEC4LJNYJAQZAGT6GYIDBJ4IA"),"NotificationSenderEvent",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("entity",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprRG5NZWGCD5HPZNK5GD7AACADHY")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("userGroup",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprZRIR5EK2WVA7HIDBH36RMFRTRQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("args",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprITC4IEU3SBAHLK2IFGXDFZ7QXM")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("attachments",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprQXUEHCCCSNDYZGHEWIL4NX45TE"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthYS3FXLA7GVHRXOSVNQNMCQHWB4"),"NotificationSenderEvent",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("entity",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprRG5NZWGCD5HPZNK5GD7AACADHY")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("recipients",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr4TPXOKX3EZH5DD45O76X6NYZUY")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("args",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprITC4IEU3SBAHLK2IFGXDFZ7QXM")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("attachments",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprQXUEHCCCSNDYZGHEWIL4NX45TE"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth64WVYOTXFZH5BONJEVML3XIP5M"),"NotificationSenderEvent",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("entity",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprRG5NZWGCD5HPZNK5GD7AACADHY")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("args",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprITC4IEU3SBAHLK2IFGXDFZ7QXM")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("attachments",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprQXUEHCCCSNDYZGHEWIL4NX45TE"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE)
						},
						null,
						null,null,null,false);
}

/* Radix::PersoComm::NotificationSenderEvent - Localizing Bundle */
package org.radixware.ads.PersoComm.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class NotificationSenderEvent - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(NotificationSenderEvent - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbadcLO6HCBGBARGDVL5ABX6KMIUQWQ"),"NotificationSenderEvent - Localizing Bundle",$$$items$$$);
}
