
/* Radix::PersoComm::NotificationSenderRecipient - Server Executable*/

/*Radix::PersoComm::NotificationSenderRecipient-Server Dynamic Class*/

package org.radixware.ads.PersoComm.server;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::NotificationSenderRecipient")
public class NotificationSenderRecipient  implements org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {


	/**Metainformation accessor method*/
	@SuppressWarnings("unused")
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return NotificationSenderRecipient_mi.rdxMeta;}

	/*Radix::PersoComm::NotificationSenderRecipient:Nested classes-Nested Classes*/

	/*Radix::PersoComm::NotificationSenderRecipient:Properties-Properties*/

	/*Radix::PersoComm::NotificationSenderRecipient:channelKind-Dynamic Property*/



	protected org.radixware.ads.PersoComm.common.ChannelKind channelKind=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::NotificationSenderRecipient:channelKind")
	public  org.radixware.ads.PersoComm.common.ChannelKind getChannelKind() {
		return channelKind;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::NotificationSenderRecipient:channelKind")
	private   void setChannelKind(org.radixware.ads.PersoComm.common.ChannelKind val) {
		channelKind = val;
	}

	/*Radix::PersoComm::NotificationSenderRecipient:address-Dynamic Property*/



	protected Str address=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::NotificationSenderRecipient:address")
	public  Str getAddress() {
		return address;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::NotificationSenderRecipient:address")
	private   void setAddress(Str val) {
		address = val;
	}

	/*Radix::PersoComm::NotificationSenderRecipient:user-Dynamic Property*/



	protected org.radixware.ads.Acs.server.User user=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::NotificationSenderRecipient:user")
	public  org.radixware.ads.Acs.server.User getUser() {
		return user;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::NotificationSenderRecipient:user")
	private   void setUser(org.radixware.ads.Acs.server.User val) {
		user = val;
	}















































	/*Radix::PersoComm::NotificationSenderRecipient:Methods-Methods*/

	/*Radix::PersoComm::NotificationSenderRecipient:NotificationSenderRecipient-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::NotificationSenderRecipient:NotificationSenderRecipient")
	public  NotificationSenderRecipient (org.radixware.ads.PersoComm.common.ChannelKind channelKind, Str address, org.radixware.ads.Acs.server.User user) {
		this.channelKind = channelKind;
		this.address = address;
		this.user = user;
	}


}

/* Radix::PersoComm::NotificationSenderRecipient - Server Meta*/

/*Radix::PersoComm::NotificationSenderRecipient-Server Dynamic Class*/

package org.radixware.ads.PersoComm.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class NotificationSenderRecipient_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("adcDJ5VKJWCXNBGXFHTZ3Q3BICPX4"),"NotificationSenderRecipient",null,

						org.radixware.kernel.common.enums.EClassType.DYNAMIC,
						null,
						null,
						null,

						/*Radix::PersoComm::NotificationSenderRecipient:Properties-Properties*/
						new org.radixware.kernel.server.meta.clazzes.RadPropDef[]{

								/*Radix::PersoComm::NotificationSenderRecipient:channelKind-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdGTVUP6THPBHMBNAHF6U6UIJNFQ"),"channelKind",null,org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("acsZ5EEISSCVXOBDCLUAALOMT5GDM"),null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::NotificationSenderRecipient:address-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd3YKAM6HEMJFT7N7T5QCDH5TXXA"),"address",null,org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::NotificationSenderRecipient:user-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicRefPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd74G7KFBJWZGHREJBKE34HQS5UE"),"user",null,org.radixware.kernel.common.enums.EValType.USER_CLASS,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE)
						},

						/*Radix::PersoComm::NotificationSenderRecipient:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthQM3E2BPKJ5H2XME7TAHMMF2TTM"),"NotificationSenderRecipient",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("channelKind",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr2S2U74JJMFGPHEXJNNVFMUU4JQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("address",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprSWIYR56YFJA7JHPOAUVXR46X2A")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("user",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprYPZTJOP3NZGE5CN3TZIWWG3TNU"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE)
						},
						null,
						null,null,null,false);
}
