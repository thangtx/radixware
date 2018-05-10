
/* Radix::SystemMonitor::MetricType.Unit.Job.WaitCnt - Server Executable*/

/*Radix::SystemMonitor::MetricType.Unit.Job.WaitCnt-Application Class*/

package org.radixware.ads.SystemMonitor.server;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType.Unit.Job.WaitCnt")
public final class MetricType.Unit.Job.WaitCnt  extends org.radixware.ads.SystemMonitor.server.MetricType.Unit.Job  implements org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {


	/**Metainformation accessor method*/
	@SuppressWarnings("unused")
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return MetricType.Unit.Job.WaitCnt_mi.rdxMeta;}

	/*Radix::SystemMonitor::MetricType.Unit.Job.WaitCnt:Nested classes-Nested Classes*/

	/*Radix::SystemMonitor::MetricType.Unit.Job.WaitCnt:Properties-Properties*/





























	/*Radix::SystemMonitor::MetricType.Unit.Job.WaitCnt:Methods-Methods*/

	/*Radix::SystemMonitor::MetricType.Unit.Job.WaitCnt:getKind-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType.Unit.Job.WaitCnt:getKind")
	public published  org.radixware.ads.SystemMonitor.common.MetricKind getKind () {
		return MetricKind:Unit.Job.WaitCnt;
	}


	@Override
	public org.radixware.kernel.server.types.FormHandler.NextDialogsRequest execCommand(org.radixware.kernel.common.types.Id cmdId, org.radixware.kernel.common.types.Id propId, org.apache.xmlbeans.XmlObject input, org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValsById, org.apache.xmlbeans.XmlObject output) throws org.radixware.kernel.common.exceptions.AppException,java.lang.InterruptedException{

			return super.execCommand(cmdId,propId,input,newPropValsById,output);
	}


}

/* Radix::SystemMonitor::MetricType.Unit.Job.WaitCnt - Server Meta*/

/*Radix::SystemMonitor::MetricType.Unit.Job.WaitCnt-Application Class*/

package org.radixware.ads.SystemMonitor.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class MetricType.Unit.Job.WaitCnt_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("aclWF447DR3NRAV7B7VAMHXMY3ELI"),"MetricType.Unit.Job.WaitCnt",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKTUZVVQUZNA6NCOM5ZY53GDTII"),

						org.radixware.kernel.common.enums.EClassType.APPLICATION,

						/*Radix::SystemMonitor::MetricType.Unit.Job.WaitCnt:Presentations-Entity Object Presentations*/
						/*Class presentation attributes*/
						 new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef(/*Owner Class Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("aclWF447DR3NRAV7B7VAMHXMY3ELI"),
							/*Owner Class Name*/
							"MetricType.Unit.Job.WaitCnt",
							/*Title Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKTUZVVQUZNA6NCOM5ZY53GDTII"),
							/*Property presentations*/

							/*Radix::SystemMonitor::MetricType.Unit.Job.WaitCnt:Properties-Properties*/
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
									/*Radix::SystemMonitor::MetricType.Unit.Job.WaitCnt:General-Editor Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprUIT2QHHT5NHUHP24ILAP4LFYFA"),
									"General",
									org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprQZBCBFUUOZHCJL2XJ2L3PATCRM"),
									40112,
									null,

									/*Radix::SystemMonitor::MetricType.Unit.Job.WaitCnt:General:Children-Explorer Items*/
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
							org.radixware.kernel.common.utils.Maps.fromArrays(new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprQZBCBFUUOZHCJL2XJ2L3PATCRM")},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprUIT2QHHT5NHUHP24ILAP4LFYFA")}),
							/*Object title format*/
							null,
							/*Class catalogs*/
							new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog[]{

									new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog(
									/*Radix::SystemMonitor::MetricType.Unit.Job.WaitCnt:Common-Dynamic Class Catalog*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eccXISAEUV6HRA7JNQ453IS2OBSFI"),org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.ELinkMode.VIRTUAL,new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.Item[]{
										new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.ClassRef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aclWF447DR3NRAV7B7VAMHXMY3ELI"),org.radixware.kernel.common.types.Id.Factory.loadFrom("cctTNNO3THJ4BEYTKUDKXWCKXRAP4"),130.0,true,org.radixware.kernel.common.types.Id.Factory.loadFrom("aclWF447DR3NRAV7B7VAMHXMY3ELI"),null)}
									)
							},
							/*Restrictions*/
							null,null,0),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl2H6SULJHXJGFVIS6UVHRWQS4AM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclZASFFDTKAVHKPOF7OGGXVFDMNY"),

						/*Radix::SystemMonitor::MetricType.Unit.Job.WaitCnt:Properties-Properties*/
						null,

						/*Radix::SystemMonitor::MetricType.Unit.Job.WaitCnt:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthSK64PGWTZNFZFAJVJ5NHXBAC6M"),"getKind",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.STR)
						},
						null,
						null,null,null,false);
}

/* Radix::SystemMonitor::MetricType.Unit.Job.WaitCnt - Desktop Executable*/

/*Radix::SystemMonitor::MetricType.Unit.Job.WaitCnt-Application Class*/

package org.radixware.ads.SystemMonitor.explorer;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType.Unit.Job.WaitCnt")
public interface MetricType.Unit.Job.WaitCnt   extends org.radixware.ads.SystemMonitor.explorer.MetricType.Unit.Job  {


















































}

/* Radix::SystemMonitor::MetricType.Unit.Job.WaitCnt - Desktop Meta*/

/*Radix::SystemMonitor::MetricType.Unit.Job.WaitCnt-Application Class*/

package org.radixware.ads.SystemMonitor.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class MetricType.Unit.Job.WaitCnt_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::SystemMonitor::MetricType.Unit.Job.WaitCnt:Presentations-Entity Object Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aclWF447DR3NRAV7B7VAMHXMY3ELI"),
			"Radix::SystemMonitor::MetricType.Unit.Job.WaitCnt",
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aclZASFFDTKAVHKPOF7OGGXVFDMNY"),
			null,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKTUZVVQUZNA6NCOM5ZY53GDTII"),null,null,0,

			/*Radix::SystemMonitor::MetricType.Unit.Job.WaitCnt:Properties-Properties*/
			null,
			null,
			null,
			null,
			null,
			new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprUIT2QHHT5NHUHP24ILAP4LFYFA")},
			true,false,false);
}

/* Radix::SystemMonitor::MetricType.Unit.Job.WaitCnt - Web Meta*/

/*Radix::SystemMonitor::MetricType.Unit.Job.WaitCnt-Application Class*/

package org.radixware.ads.SystemMonitor.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class MetricType.Unit.Job.WaitCnt_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::SystemMonitor::MetricType.Unit.Job.WaitCnt:Presentations-Entity Object Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aclWF447DR3NRAV7B7VAMHXMY3ELI"),
			"Radix::SystemMonitor::MetricType.Unit.Job.WaitCnt",
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aclZASFFDTKAVHKPOF7OGGXVFDMNY"),
			null,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKTUZVVQUZNA6NCOM5ZY53GDTII"),null,null,0,
			null,
			null,
			null,
			null,
			null,
			null,
			false,false,false);
}

/* Radix::SystemMonitor::MetricType.Unit.Job.WaitCnt:General - Desktop Meta*/

/*Radix::SystemMonitor::MetricType.Unit.Job.WaitCnt:General-Editor Presentation*/

package org.radixware.ads.SystemMonitor.explorer;
public final class General_mi{
	private static final class General_DEF extends org.radixware.kernel.common.client.meta.RadEditorPresentationDef{
		General_DEF(){
			super(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprUIT2QHHT5NHUHP24ILAP4LFYFA"),
			"General",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("eprQZBCBFUUOZHCJL2XJ2L3PATCRM"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aclWF447DR3NRAV7B7VAMHXMY3ELI"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl2H6SULJHXJGFVIS6UVHRWQS4AM"),
			null,
			null,
			null,
			null,

			/*Radix::SystemMonitor::MetricType.Unit.Job.WaitCnt:General:Children-Explorer Items*/
				null
			,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
			null,
			0,
			null,
			(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
			40112,0,0);
		}
		@Override
		protected org.radixware.kernel.common.client.models.Model createModelImpl(org.radixware.kernel.common.client.IClientEnvironment userSession) {    return new org.radixware.ads.SystemMonitor.explorer.MetricType.Unit.Job.WaitCnt.MetricType.Unit.Job.WaitCnt_DefaultModel.eprQZBCBFUUOZHCJL2XJ2L3PATCRM_ModelAdapter(userSession,this);
		}
	}
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new General_DEF(); 
;
}
/* Radix::SystemMonitor::MetricType.Unit.Job.WaitCnt - Localizing Bundle */
package org.radixware.ads.SystemMonitor.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class MetricType.Unit.Job.WaitCnt - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Исполнитель заданий - Количество заданий, ожидающих выполнения");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Job Executor - Number of Pending Jobs");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKTUZVVQUZNA6NCOM5ZY53GDTII"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"/home/ayanichkin/ssd/tranzaxis/radix/org.radixware/ads/SystemMonitor"));
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(MetricType.Unit.Job.WaitCnt - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbaclWF447DR3NRAV7B7VAMHXMY3ELI"),"MetricType.Unit.Job.WaitCnt - Localizing Bundle",$$$items$$$);
}
