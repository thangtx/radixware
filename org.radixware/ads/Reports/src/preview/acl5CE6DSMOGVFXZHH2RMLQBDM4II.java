
/* Radix::Reports::UserFunc.CalcPdfSecurityOptions - Server Executable*/

/*Radix::Reports::UserFunc.CalcPdfSecurityOptions-Application Class*/

package org.radixware.ads.Reports.server;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::UserFunc.CalcPdfSecurityOptions")
public published class UserFunc.CalcPdfSecurityOptions  extends org.radixware.ads.UserFunc.server.UserFunc  implements org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {


	/**Metainformation accessor method*/
	@SuppressWarnings("unused")
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return UserFunc.CalcPdfSecurityOptions_mi.rdxMeta;}

	/*Radix::Reports::UserFunc.CalcPdfSecurityOptions:Nested classes-Nested Classes*/

	/*Radix::Reports::UserFunc.CalcPdfSecurityOptions:Properties-Properties*/





























	/*Radix::Reports::UserFunc.CalcPdfSecurityOptions:Methods-Methods*/

	/*Radix::Reports::UserFunc.CalcPdfSecurityOptions:calcPdfSecurityOption-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::UserFunc.CalcPdfSecurityOptions:calcPdfSecurityOption")
	public published  org.radixware.kernel.server.reports.PdfSecurityOptions calcPdfSecurityOption (java.util.Map<org.radixware.kernel.common.types.Id,java.lang.Object> params, org.radixware.ads.Types.server.Entity contextObj) {
		try {
		    return (PdfSecurityOptions)invoke(new Object[]{params, contextObj});
		} catch (Exceptions::Exception ex) {
		    if (ex instanceof Exceptions::RuntimeException) {
		        throw (Exceptions::RuntimeException) ex;
		    }
		    throw new UserFuncError(this, "error in calcPdfSecurityOption(Map<Id,Object> params, Entity contextObj) user function", ex);
		}
	}

	/*Radix::Reports::UserFunc.CalcPdfSecurityOptions:getMethodId-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::UserFunc.CalcPdfSecurityOptions:getMethodId")
	protected published  org.radixware.kernel.common.types.Id getMethodId () {
		return idof[UserFunc.CalcPdfSecurityOptions:calcPdfSecurityOption];
	}


	@Override
	public org.radixware.kernel.server.types.FormHandler.NextDialogsRequest execCommand(org.radixware.kernel.common.types.Id cmdId, org.radixware.kernel.common.types.Id propId, org.apache.xmlbeans.XmlObject input, org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValsById, org.apache.xmlbeans.XmlObject output) throws org.radixware.kernel.common.exceptions.AppException,java.lang.InterruptedException{

			return super.execCommand(cmdId,propId,input,newPropValsById,output);
	}


}

/* Radix::Reports::UserFunc.CalcPdfSecurityOptions - Server Meta*/

/*Radix::Reports::UserFunc.CalcPdfSecurityOptions-Application Class*/

package org.radixware.ads.Reports.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class UserFunc.CalcPdfSecurityOptions_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("acl5CE6DSMOGVFXZHH2RMLQBDM4II"),"UserFunc.CalcPdfSecurityOptions",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQEZ5JHCRGBENXMP3GT6NFZJQUM"),

						org.radixware.kernel.common.enums.EClassType.APPLICATION,

						/*Radix::Reports::UserFunc.CalcPdfSecurityOptions:Presentations-Entity Object Presentations*/
						/*Class presentation attributes*/
						 new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef(/*Owner Class Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("acl5CE6DSMOGVFXZHH2RMLQBDM4II"),
							/*Owner Class Name*/
							"UserFunc.CalcPdfSecurityOptions",
							/*Title Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQEZ5JHCRGBENXMP3GT6NFZJQUM"),
							/*Property presentations*/

							/*Radix::Reports::UserFunc.CalcPdfSecurityOptions:Properties-Properties*/
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

						/*Radix::Reports::UserFunc.CalcPdfSecurityOptions:Properties-Properties*/
						null,

						/*Radix::Reports::UserFunc.CalcPdfSecurityOptions:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthVD4LRN6NMJE5VDR6EIT5TWNJZM"),"calcPdfSecurityOption",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("params",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprHKCTOFYTYBHXXHBXZUIUBMVRMU")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("contextObj",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprBN4SLYCDNBAW5J3QNG7OW6YIZY"))
								},org.radixware.kernel.common.enums.EValType.JAVA_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthFOOX7DTCW5GA5CUK2VYOOVAC3Q"),"getMethodId",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,org.radixware.kernel.common.enums.EValType.USER_CLASS)
						},
						null,
						null,null,null,false);
}

/* Radix::Reports::UserFunc.CalcPdfSecurityOptions - Desktop Executable*/

/*Radix::Reports::UserFunc.CalcPdfSecurityOptions-Application Class*/

package org.radixware.ads.Reports.explorer;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::UserFunc.CalcPdfSecurityOptions")
public interface UserFunc.CalcPdfSecurityOptions   extends org.radixware.ads.UserFunc.explorer.UserFunc  {
























}

/* Radix::Reports::UserFunc.CalcPdfSecurityOptions - Desktop Meta*/

/*Radix::Reports::UserFunc.CalcPdfSecurityOptions-Application Class*/

package org.radixware.ads.Reports.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class UserFunc.CalcPdfSecurityOptions_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::Reports::UserFunc.CalcPdfSecurityOptions:Presentations-Entity Object Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("acl5CE6DSMOGVFXZHH2RMLQBDM4II"),
			"Radix::Reports::UserFunc.CalcPdfSecurityOptions",
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ6SOXKD3ZHOBDCMTAALOMT5GDM"),
			null,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQEZ5JHCRGBENXMP3GT6NFZJQUM"),null,null,0,

			/*Radix::Reports::UserFunc.CalcPdfSecurityOptions:Properties-Properties*/
			null,
			null,
			null,
			null,
			null,
			null,
			true,true,true);
}

/* Radix::Reports::UserFunc.CalcPdfSecurityOptions - Web Meta*/

/*Radix::Reports::UserFunc.CalcPdfSecurityOptions-Application Class*/

package org.radixware.ads.Reports.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class UserFunc.CalcPdfSecurityOptions_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::Reports::UserFunc.CalcPdfSecurityOptions:Presentations-Entity Object Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("acl5CE6DSMOGVFXZHH2RMLQBDM4II"),
			"Radix::Reports::UserFunc.CalcPdfSecurityOptions",
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ6SOXKD3ZHOBDCMTAALOMT5GDM"),
			null,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQEZ5JHCRGBENXMP3GT6NFZJQUM"),null,null,0,
			null,
			null,
			null,
			null,
			null,
			null,
			false,false,true);
}

/* Radix::Reports::UserFunc.CalcPdfSecurityOptions - Localizing Bundle */
package org.radixware.ads.Reports.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class UserFunc.CalcPdfSecurityOptions - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"PDF security protection function");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Функция настройки защиты PDF-документа");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQEZ5JHCRGBENXMP3GT6NFZJQUM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(UserFunc.CalcPdfSecurityOptions - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbacl5CE6DSMOGVFXZHH2RMLQBDM4II"),"UserFunc.CalcPdfSecurityOptions - Localizing Bundle",$$$items$$$);
}
