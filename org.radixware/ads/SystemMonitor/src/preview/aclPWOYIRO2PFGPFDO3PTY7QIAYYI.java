
/* Radix::SystemMonitor::MetricType.Instance.Arte.SessCnt - Server Executable*/

/*Radix::SystemMonitor::MetricType.Instance.Arte.SessCnt-Application Class*/

package org.radixware.ads.SystemMonitor.server;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType.Instance.Arte.SessCnt")
public final class MetricType.Instance.Arte.SessCnt  extends org.radixware.ads.SystemMonitor.server.MetricType.Instance  implements org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {


	/**Metainformation accessor method*/
	@SuppressWarnings("unused")
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return MetricType.Instance.Arte.SessCnt_mi.rdxMeta;}

	/*Radix::SystemMonitor::MetricType.Instance.Arte.SessCnt:Nested classes-Nested Classes*/

	/*Radix::SystemMonitor::MetricType.Instance.Arte.SessCnt:Properties-Properties*/





























	/*Radix::SystemMonitor::MetricType.Instance.Arte.SessCnt:Methods-Methods*/

	/*Radix::SystemMonitor::MetricType.Instance.Arte.SessCnt:getKind-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType.Instance.Arte.SessCnt:getKind")
	public published  org.radixware.ads.SystemMonitor.common.MetricKind getKind () {
		return MetricKind:Inst.Arte.SessCnt;
	}


	@Override
	public org.radixware.kernel.server.types.FormHandler.NextDialogsRequest execCommand(org.radixware.kernel.common.types.Id cmdId, org.radixware.kernel.common.types.Id propId, org.apache.xmlbeans.XmlObject input, org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValsById, org.apache.xmlbeans.XmlObject output) throws org.radixware.kernel.common.exceptions.AppException,java.lang.InterruptedException{

			return super.execCommand(cmdId,propId,input,newPropValsById,output);
	}


}

/* Radix::SystemMonitor::MetricType.Instance.Arte.SessCnt - Server Meta*/

/*Radix::SystemMonitor::MetricType.Instance.Arte.SessCnt-Application Class*/

package org.radixware.ads.SystemMonitor.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class MetricType.Instance.Arte.SessCnt_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("aclPWOYIRO2PFGPFDO3PTY7QIAYYI"),"MetricType.Instance.Arte.SessCnt",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPMGNHCDGBVA4PLR4VZZ6UTQ47A"),

						org.radixware.kernel.common.enums.EClassType.APPLICATION,

						/*Radix::SystemMonitor::MetricType.Instance.Arte.SessCnt:Presentations-Entity Object Presentations*/
						/*Class presentation attributes*/
						 new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef(/*Owner Class Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("aclPWOYIRO2PFGPFDO3PTY7QIAYYI"),
							/*Owner Class Name*/
							"MetricType.Instance.Arte.SessCnt",
							/*Title Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPMGNHCDGBVA4PLR4VZZ6UTQ47A"),
							/*Property presentations*/

							/*Radix::SystemMonitor::MetricType.Instance.Arte.SessCnt:Properties-Properties*/
							null,
							/*Commands*/
							null,
							/*Sortings*/
							null,
							/*Filters*/
							null,
							/*Editor presentations*/
							new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef[]{

									new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef(
									/*Radix::SystemMonitor::MetricType.Instance.Arte.SessCnt:General-Editor Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprS3YTXYJHUNA7DEHGRMNGSXCXAE"),
									"General",
									org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprXUBD7FFHLRHI7AKIGMY546LNOM"),
									40112,
									null,

									/*Radix::SystemMonitor::MetricType.Instance.Arte.SessCnt:General:Children-Explorer Items*/
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
							org.radixware.kernel.common.utils.Maps.fromArrays(new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprXUBD7FFHLRHI7AKIGMY546LNOM")},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprS3YTXYJHUNA7DEHGRMNGSXCXAE")}),
							/*Object title format*/
							null,
							/*Class catalogs*/
							new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog[]{

									new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog(
									/*Radix::SystemMonitor::MetricType.Instance.Arte.SessCnt:Common-Dynamic Class Catalog*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eccXISAEUV6HRA7JNQ453IS2OBSFI"),org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.ELinkMode.VIRTUAL,new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.Item[]{
										new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.ClassRef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aclPWOYIRO2PFGPFDO3PTY7QIAYYI"),org.radixware.kernel.common.types.Id.Factory.loadFrom("cctHNCB6PXQSJDTFDWDBV7GGIJ7BY"),160.0,true,org.radixware.kernel.common.types.Id.Factory.loadFrom("aclPWOYIRO2PFGPFDO3PTY7QIAYYI"),null)}
									)
							},
							/*Restrictions*/
							null,null,0),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl2H6SULJHXJGFVIS6UVHRWQS4AM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclVDACCMUGFFBSJBQKINASFWDLJ4"),

						/*Radix::SystemMonitor::MetricType.Instance.Arte.SessCnt:Properties-Properties*/
						null,

						/*Radix::SystemMonitor::MetricType.Instance.Arte.SessCnt:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthSK64PGWTZNFZFAJVJ5NHXBAC6M"),"getKind",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.STR)
						},
						null,
						null,null,null,false);
}

/* Radix::SystemMonitor::MetricType.Instance.Arte.SessCnt - Desktop Executable*/

/*Radix::SystemMonitor::MetricType.Instance.Arte.SessCnt-Application Class*/

package org.radixware.ads.SystemMonitor.explorer;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType.Instance.Arte.SessCnt")
public interface MetricType.Instance.Arte.SessCnt   extends org.radixware.ads.SystemMonitor.explorer.MetricType.Instance  {


















































}

/* Radix::SystemMonitor::MetricType.Instance.Arte.SessCnt - Desktop Meta*/

/*Radix::SystemMonitor::MetricType.Instance.Arte.SessCnt-Application Class*/

package org.radixware.ads.SystemMonitor.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class MetricType.Instance.Arte.SessCnt_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::SystemMonitor::MetricType.Instance.Arte.SessCnt:Presentations-Entity Object Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aclPWOYIRO2PFGPFDO3PTY7QIAYYI"),
			"Radix::SystemMonitor::MetricType.Instance.Arte.SessCnt",
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aclVDACCMUGFFBSJBQKINASFWDLJ4"),
			null,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPMGNHCDGBVA4PLR4VZZ6UTQ47A"),null,null,0,

			/*Radix::SystemMonitor::MetricType.Instance.Arte.SessCnt:Properties-Properties*/
			null,
			null,
			null,
			null,
			null,
			new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprS3YTXYJHUNA7DEHGRMNGSXCXAE")},
			true,false,false);
}

/* Radix::SystemMonitor::MetricType.Instance.Arte.SessCnt - Web Meta*/

/*Radix::SystemMonitor::MetricType.Instance.Arte.SessCnt-Application Class*/

package org.radixware.ads.SystemMonitor.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class MetricType.Instance.Arte.SessCnt_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::SystemMonitor::MetricType.Instance.Arte.SessCnt:Presentations-Entity Object Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aclPWOYIRO2PFGPFDO3PTY7QIAYYI"),
			"Radix::SystemMonitor::MetricType.Instance.Arte.SessCnt",
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aclVDACCMUGFFBSJBQKINASFWDLJ4"),
			null,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPMGNHCDGBVA4PLR4VZZ6UTQ47A"),null,null,0,
			null,
			null,
			null,
			null,
			null,
			null,
			false,false,false);
}

/* Radix::SystemMonitor::MetricType.Instance.Arte.SessCnt:General - Desktop Meta*/

/*Radix::SystemMonitor::MetricType.Instance.Arte.SessCnt:General-Editor Presentation*/

package org.radixware.ads.SystemMonitor.explorer;
public final class General_mi{
	private static final class General_DEF extends org.radixware.kernel.common.client.meta.RadEditorPresentationDef{
		General_DEF(){
			super(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprS3YTXYJHUNA7DEHGRMNGSXCXAE"),
			"General",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("eprXUBD7FFHLRHI7AKIGMY546LNOM"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aclPWOYIRO2PFGPFDO3PTY7QIAYYI"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl2H6SULJHXJGFVIS6UVHRWQS4AM"),
			null,
			null,
			null,
			null,

			/*Radix::SystemMonitor::MetricType.Instance.Arte.SessCnt:General:Children-Explorer Items*/
				null
			,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
			null,
			0,
			null,
			(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
			40112,0,0);
		}
		@Override
		protected org.radixware.kernel.common.client.models.Model createModelImpl(org.radixware.kernel.common.client.IClientEnvironment userSession) {    return new org.radixware.ads.SystemMonitor.explorer.MetricType.Instance.Arte.SessCnt.MetricType.Instance.Arte.SessCnt_DefaultModel.eprXUBD7FFHLRHI7AKIGMY546LNOM_ModelAdapter(userSession,this);
		}
	}
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new General_DEF(); 
;
}
/* Radix::SystemMonitor::MetricType.Instance.Arte.SessCnt - Localizing Bundle */
package org.radixware.ads.SystemMonitor.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class MetricType.Instance.Arte.SessCnt - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Инстанция - Количество активных ARTE сессий");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Instance - Number of Active ARTE Sessions");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPMGNHCDGBVA4PLR4VZZ6UTQ47A"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"/home/ayanichkin/ssd/tranzaxis/radix/org.radixware/ads/SystemMonitor"));
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(MetricType.Instance.Arte.SessCnt - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbaclPWOYIRO2PFGPFDO3PTY7QIAYYI"),"MetricType.Instance.Arte.SessCnt - Localizing Bundle",$$$items$$$);
}
