
/* Radix::PersoComm::UserFunc.NotificationSender.ArgsPreparation - Server Executable*/

/*Radix::PersoComm::UserFunc.NotificationSender.ArgsPreparation-Application Class*/

package org.radixware.ads.PersoComm.server;

import java.util.Map;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::UserFunc.NotificationSender.ArgsPreparation")
public published class UserFunc.NotificationSender.ArgsPreparation  extends org.radixware.ads.UserFunc.server.UserFunc  implements org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {


	/**Metainformation accessor method*/
	@SuppressWarnings("unused")
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return UserFunc.NotificationSender.ArgsPreparation_mi.rdxMeta;}

	/*Radix::PersoComm::UserFunc.NotificationSender.ArgsPreparation:Nested classes-Nested Classes*/

	/*Radix::PersoComm::UserFunc.NotificationSender.ArgsPreparation:Properties-Properties*/





























	/*Radix::PersoComm::UserFunc.NotificationSender.ArgsPreparation:Methods-Methods*/

	/*Radix::PersoComm::UserFunc.NotificationSender.ArgsPreparation:prepareArgs-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::UserFunc.NotificationSender.ArgsPreparation:prepareArgs")
	public published  java.util.Map<Str,java.lang.Object> prepareArgs (org.radixware.ads.PersoComm.server.NotificationSenderEvent event, org.radixware.ads.PersoComm.server.OutMessage message, org.radixware.ads.Acs.server.User user) {
		try {
		    return (Map<Str, Object>)invoke(new Object[]{ event, message, user });
		} catch (Exceptions::Exception e) {
		    if (e instanceof Exceptions::RuntimeException)
		        throw (Exceptions::RuntimeException) e;
		    throw new UserFuncError(this, "'Args preparation' user function error", e);
		}

	}

	/*Radix::PersoComm::UserFunc.NotificationSender.ArgsPreparation:getMethodId-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::UserFunc.NotificationSender.ArgsPreparation:getMethodId")
	protected published  org.radixware.kernel.common.types.Id getMethodId () {
		return idof[UserFunc.NotificationSender.ArgsPreparation:prepareArgs];
	}


	@Override
	public org.radixware.kernel.server.types.FormHandler.NextDialogsRequest execCommand(org.radixware.kernel.common.types.Id cmdId, org.radixware.kernel.common.types.Id propId, org.apache.xmlbeans.XmlObject input, org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValsById, org.apache.xmlbeans.XmlObject output) throws org.radixware.kernel.common.exceptions.AppException,java.lang.InterruptedException{

			return super.execCommand(cmdId,propId,input,newPropValsById,output);
	}


}

/* Radix::PersoComm::UserFunc.NotificationSender.ArgsPreparation - Server Meta*/

/*Radix::PersoComm::UserFunc.NotificationSender.ArgsPreparation-Application Class*/

package org.radixware.ads.PersoComm.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class UserFunc.NotificationSender.ArgsPreparation_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("acl6IPBFCSAYJAM5LVYGALSEZ5XXY"),"UserFunc.NotificationSender.ArgsPreparation",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVIMN6HV37VFA5LVYQQDNNRZDQE"),

						org.radixware.kernel.common.enums.EClassType.APPLICATION,

						/*Radix::PersoComm::UserFunc.NotificationSender.ArgsPreparation:Presentations-Entity Object Presentations*/
						/*Class presentation attributes*/
						 new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef(/*Owner Class Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("acl6IPBFCSAYJAM5LVYGALSEZ5XXY"),
							/*Owner Class Name*/
							"UserFunc.NotificationSender.ArgsPreparation",
							/*Title Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVIMN6HV37VFA5LVYQQDNNRZDQE"),
							/*Property presentations*/

							/*Radix::PersoComm::UserFunc.NotificationSender.ArgsPreparation:Properties-Properties*/
							null,
							/*Commands*/
							null,
							/*Sortings*/
							null,
							/*Filters*/
							null,
							/*Editor presentations*/
							null,
							/*Selector presentations*/
							null,
							/*Default selector presentation Id*/
							null,
							/*Presentation Map*/
							null,
							/*Object title format*/
							null,
							/*Class catalogs*/
							null,
							/*Restrictions*/
							null,null,0),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblJ6SOXKD3ZHOBDCMTAALOMT5GDM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ6SOXKD3ZHOBDCMTAALOMT5GDM"),

						/*Radix::PersoComm::UserFunc.NotificationSender.ArgsPreparation:Properties-Properties*/
						null,

						/*Radix::PersoComm::UserFunc.NotificationSender.ArgsPreparation:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth5RG6OBJLY5E7HD6M4WBUZ7OOSE"),"prepareArgs",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("event",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprSWHU5ZDIE5BYTHZYAATUNEJY3E")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("message",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprJ43R2WOPFRDFNAGSLFPEUDB4AI")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("user",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprCEF2LN2OMJHTXIKVU462PVPMBY"))
								},org.radixware.kernel.common.enums.EValType.JAVA_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthFOOX7DTCW5GA5CUK2VYOOVAC3Q"),"getMethodId",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,org.radixware.kernel.common.enums.EValType.USER_CLASS)
						},
						null,
						null,null,null,false);
}

/* Radix::PersoComm::UserFunc.NotificationSender.ArgsPreparation - Desktop Executable*/

/*Radix::PersoComm::UserFunc.NotificationSender.ArgsPreparation-Application Class*/

package org.radixware.ads.PersoComm.explorer;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::UserFunc.NotificationSender.ArgsPreparation")
public interface UserFunc.NotificationSender.ArgsPreparation   extends org.radixware.ads.UserFunc.explorer.UserFunc  {
























}

/* Radix::PersoComm::UserFunc.NotificationSender.ArgsPreparation - Desktop Meta*/

/*Radix::PersoComm::UserFunc.NotificationSender.ArgsPreparation-Application Class*/

package org.radixware.ads.PersoComm.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class UserFunc.NotificationSender.ArgsPreparation_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::PersoComm::UserFunc.NotificationSender.ArgsPreparation:Presentations-Entity Object Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("acl6IPBFCSAYJAM5LVYGALSEZ5XXY"),
			"Radix::PersoComm::UserFunc.NotificationSender.ArgsPreparation",
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ6SOXKD3ZHOBDCMTAALOMT5GDM"),
			null,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVIMN6HV37VFA5LVYQQDNNRZDQE"),null,null,0,

			/*Radix::PersoComm::UserFunc.NotificationSender.ArgsPreparation:Properties-Properties*/
			null,
			null,
			null,
			null,
			null,
			null,
			true,true,true);
}

/* Radix::PersoComm::UserFunc.NotificationSender.ArgsPreparation - Web Meta*/

/*Radix::PersoComm::UserFunc.NotificationSender.ArgsPreparation-Application Class*/

package org.radixware.ads.PersoComm.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class UserFunc.NotificationSender.ArgsPreparation_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::PersoComm::UserFunc.NotificationSender.ArgsPreparation:Presentations-Entity Object Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("acl6IPBFCSAYJAM5LVYGALSEZ5XXY"),
			"Radix::PersoComm::UserFunc.NotificationSender.ArgsPreparation",
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ6SOXKD3ZHOBDCMTAALOMT5GDM"),
			null,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVIMN6HV37VFA5LVYQQDNNRZDQE"),null,null,0,
			null,
			null,
			null,
			null,
			null,
			null,
			false,false,true);
}

/* Radix::PersoComm::UserFunc.NotificationSender.ArgsPreparation - Localizing Bundle */
package org.radixware.ads.PersoComm.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class UserFunc.NotificationSender.ArgsPreparation - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Arguments Preparation Function");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Функция подготовки аргументов");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVIMN6HV37VFA5LVYQQDNNRZDQE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(UserFunc.NotificationSender.ArgsPreparation - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbacl6IPBFCSAYJAM5LVYGALSEZ5XXY"),"UserFunc.NotificationSender.ArgsPreparation - Localizing Bundle",$$$items$$$);
}
