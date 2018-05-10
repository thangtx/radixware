
/* Radix::UserFunc::UserFuncUtils - Common Executable*/

/*Radix::UserFunc::UserFuncUtils-Common Dynamic Class*/

package org.radixware.ads.UserFunc.common;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFuncUtils")
public class UserFuncUtils  {



	/*Radix::UserFunc::UserFuncUtils:Nested classes-Nested Classes*/

	/*Radix::UserFunc::UserFuncUtils:Properties-Properties*/

	/*Radix::UserFunc::UserFuncUtils:CHANGE_LOG_DATE_FORMAT-Dynamic Property*/



	protected static java.text.SimpleDateFormat CHANGE_LOG_DATE_FORMAT=new java.text.SimpleDateFormat("dd-MM-YYYY HH:mm:ss");;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFuncUtils:CHANGE_LOG_DATE_FORMAT")
	private static final  java.text.SimpleDateFormat getCHANGE_LOG_DATE_FORMAT() {
		return CHANGE_LOG_DATE_FORMAT;
	}

	/*Radix::UserFunc::UserFuncUtils:XML_OPTIONS-Dynamic Property*/



	protected static org.apache.xmlbeans.XmlOptions XML_OPTIONS=new org.apache.xmlbeans.XmlOptions() {
	    {
	        this.setSaveNamespacesFirst();
	        this.setSaveAggressiveNamespaces();
	        this.setSavePrettyPrint();
	    }
	};;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFuncUtils:XML_OPTIONS")
	private static final  org.apache.xmlbeans.XmlOptions getXML_OPTIONS() {
		return XML_OPTIONS;
	}

	/*Radix::UserFunc::UserFuncUtils:Methods-Methods*/

	/*Radix::UserFunc::UserFuncUtils:usedDefinitionsListToStr-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFuncUtils:usedDefinitionsListToStr")
	public static  Str usedDefinitionsListToStr (java.util.List<org.radixware.kernel.common.defs.ads.AdsDefinition> usedWrappers) {
		if (usedWrappers != null) {
		    StringBuilder sb = new StringBuilder();
		    boolean first = true;
		    for (org.radixware.kernel.common.defs.ads.AdsDefinition def : usedWrappers) {
		        if (first)
		            first = false;
		        else
		            sb.append(' ');
		        sb.append(def.getId());
		    }
		    if (sb.length() > 0) {
		        return sb.toString();
		    } else {
		        return null;
		    }
		} else {
		    return null;
		}
	}

	/*Radix::UserFunc::UserFuncUtils:getCompileResultStr-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFuncUtils:getCompileResultStr")
	public static  Str getCompileResultStr (int totalFuncs, int totalFuncsWithErrors, int totalErrors, int totalWarnings) {
		return String.format("Total functions: %s, Functions with errors: %s, Total Errors: %s, Total Warnings: %s",
		        totalFuncs, totalFuncsWithErrors, totalErrors, totalWarnings);
	}

	/*Radix::UserFunc::UserFuncUtils:getDescriptionWithLastRevisionDate-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFuncUtils:getDescriptionWithLastRevisionDate")
	public static  Str getDescriptionWithLastRevisionDate (Str descr, java.sql.Timestamp lastRevDate) {
		if (descr != null || lastRevDate != null) {
		    final java.lang.StringBuilder sb = new java.lang.StringBuilder();
		    if (descr != null) {
		        sb.append(descr);
		    }
		    if (lastRevDate != null) {
		        if (sb.length() > 0) {
		            sb.append(' ');
		        }
		        sb.append('[').append(CHANGE_LOG_DATE_FORMAT.format(lastRevDate)).append(']');
		    }
		    return sb.toString();
		}
		return null;
	}

	/*Radix::UserFunc::UserFuncUtils:compareUserFuncSrc-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFuncUtils:compareUserFuncSrc")
	public static  boolean compareUserFuncSrc (org.radixware.schemas.adsdef.AdsUserFuncDefinitionDocument thisSrc, org.radixware.schemas.adsdef.AdsUserFuncDefinitionDocument otherSrc) throws java.io.IOException,org.apache.xmlbeans.XmlException {
		final boolean thisSrcNotNull = thisSrc != null && thisSrc.AdsUserFuncDefinition != null
		        && thisSrc.AdsUserFuncDefinition.Source != null;
		final boolean otherSrcNotNull = otherSrc != null && otherSrc.AdsUserFuncDefinition != null
		        && otherSrc.AdsUserFuncDefinition.Source != null;

		if (thisSrcNotNull && otherSrcNotNull) {
		    //Ugly but most simple way compare to xml elements
		    final Xml xSrc1 = org.radixware.kernel.common.utils.XmlUtils.removeRepeatedNamespaceDeclarations(thisSrc.AdsUserFuncDefinition.Source);
		    final Xml xSrc2 = org.radixware.kernel.common.utils.XmlUtils.removeRepeatedNamespaceDeclarations(otherSrc.AdsUserFuncDefinition.Source);
		    return java.util.Objects.equals(xSrc1.xmlText(XML_OPTIONS), xSrc2.xmlText(XML_OPTIONS));
		} else {
		    return thisSrcNotNull == otherSrcNotNull;
		}
	}

	/*Radix::UserFunc::UserFuncUtils:compareStreams-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFuncUtils:compareStreams")
	public static  boolean compareStreams (java.io.InputStream is1, java.io.InputStream is2) throws java.io.IOException {
		if (is1 == null) {
		    return is2 == null;
		}

		int b1 = 0;
		int b2 = 0;
		while (true) {
		    b1 = is1.read();
		    b2 = is2.read();
		    if (b1 != b2 || b1 == -1) {
		        break;
		    }
		}
		return b1 == b2;
	}


}

/* Radix::UserFunc::UserFuncUtils - Server Meta*/

/*Radix::UserFunc::UserFuncUtils-Common Dynamic Class*/

package org.radixware.ads.UserFunc.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class UserFuncUtils_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("adcP5L4LADYQJHTDOZI5NGN6NL2F4"),"UserFuncUtils",null,

						org.radixware.kernel.common.enums.EClassType.DYNAMIC,
						null,
						null,
						null,

						/*Radix::UserFunc::UserFuncUtils:Properties-Properties*/
						new org.radixware.kernel.server.meta.clazzes.RadPropDef[]{

								/*Radix::UserFunc::UserFuncUtils:CHANGE_LOG_DATE_FORMAT-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdK35BAMQ7ABHHVE6ITJHC22ZG4E"),"CHANGE_LOG_DATE_FORMAT",null,org.radixware.kernel.common.enums.EValType.JAVA_CLASS,null,null,null),

								/*Radix::UserFunc::UserFuncUtils:XML_OPTIONS-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdWYRGREQB45COJNJJCRNT6KZRI4"),"XML_OPTIONS",null,org.radixware.kernel.common.enums.EValType.JAVA_CLASS,null,null,null)
						},

						/*Radix::UserFunc::UserFuncUtils:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthY4IVWI3G2RA55FLEHJJWAZR4UU"),"usedDefinitionsListToStr",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("usedWrappers",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprZOJCUOLSXJE3FLS6E5QR5DXOF4"))
								},org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthH5WJASQQWNFDFP5KEM3VQ7QED4"),"getCompileResultStr",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("totalFuncs",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprGVNV42IHFVHTDKMD5KBV4NAOCQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("totalFuncsWithErrors",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprMDICNBKZPVGXBJOTMV2EID5SWQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("totalErrors",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprS6L4VNI5EBAH3CP6BF3UT7VEYQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("totalWarnings",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr7VMR6FADZJAFRG65VU3GRWIZLQ"))
								},org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthRNBDLVBY35FRDMRGLFTY6PLEUU"),"getDescriptionWithLastRevisionDate",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("descr",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprX5M57X2IBVGWJAFH73HM4FAQNM")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("lastRevDate",org.radixware.kernel.common.enums.EValType.DATE_TIME,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprDHPO6QLW6RCRNMPWO7VPRIFKGU"))
								},org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthLK3VPOSRJNHWFMNNKLP5EWEXCU"),"compareUserFuncSrc",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("thisSrc",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprSUAKSPLDENFYTJZ2NGFSAT74LA")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("otherSrc",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprJCIF63N3BJHLPCVV4SGC4MFYLA"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthRD6YFFNKZBBPRGS34JPZVF3YT4"),"compareStreams",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("is1",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprCQBCGAUZ4VFXLGOSAIKQCCACYU")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("is2",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprLLZ7VR4H5JCBNAWG6OQPLW6WQI"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE)
						},
						null,
						null,null,null,false);
}

/* Radix::UserFunc::UserFuncUtils - Client-Common Meta*/

/*Radix::UserFunc::UserFuncUtils-Common Dynamic Class*/

package org.radixware.ads.UserFunc.common_client;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class UserFuncUtils_mi{
}

/* Radix::UserFunc::UserFuncUtils - Desktop Meta*/

/*Radix::UserFunc::UserFuncUtils-Common Dynamic Class*/

package org.radixware.ads.UserFunc.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class UserFuncUtils_mi{
}

/* Radix::UserFunc::UserFuncUtils - Web Meta*/

/*Radix::UserFunc::UserFuncUtils-Common Dynamic Class*/

package org.radixware.ads.UserFunc.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class UserFuncUtils_mi{
}

/* Radix::UserFunc::UserFuncUtils - Localizing Bundle */
package org.radixware.ads.UserFunc.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class UserFuncUtils - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Всего функций: %s, Функций с ошибками: %s, Всего ошибок: %s, Всего предупреждений: %s");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Total functions: %s, Functions with errors: %s, Total Errors: %s, Total Warnings: %s");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWOSYA444QZECVNQFNDFPHACGDA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"C:\\distrib\\trunk\\org.radixware\\ads\\UserFunc"));
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(UserFuncUtils - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbadcP5L4LADYQJHTDOZI5NGN6NL2F4"),"UserFuncUtils - Localizing Bundle",$$$items$$$);
}
