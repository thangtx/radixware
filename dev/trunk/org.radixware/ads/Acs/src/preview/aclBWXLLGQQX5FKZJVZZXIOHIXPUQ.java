
/* Radix::Acs::PartitionGroup.DashConfig - Server Executable*/

/*Radix::Acs::PartitionGroup.DashConfig-Application Class*/

package org.radixware.ads.Acs.server;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::PartitionGroup.DashConfig")
public published class PartitionGroup.DashConfig  extends org.radixware.ads.Acs.server.PartitionGroup  implements org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {


	/**Metainformation accessor method*/
	@SuppressWarnings("unused")
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return PartitionGroup.DashConfig_mi.rdxMeta;}

	/*Radix::Acs::PartitionGroup.DashConfig:Nested classes-Nested Classes*/

	/*Radix::Acs::PartitionGroup.DashConfig:Properties-Properties*/





























	/*Radix::Acs::PartitionGroup.DashConfig:Methods-Methods*/

	/*Radix::Acs::PartitionGroup.DashConfig:getFamilySelectorPresentationId-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::PartitionGroup.DashConfig:getFamilySelectorPresentationId")
	public published  org.radixware.kernel.common.types.Id getFamilySelectorPresentationId () {
		return idof[SystemMonitor::DashConfig:ForPartitionsGrpup];
		//;
	}

	/*Radix::Acs::PartitionGroup.DashConfig:getFamilyId-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::PartitionGroup.DashConfig:getFamilyId")
	public published  org.radixware.kernel.common.types.Id getFamilyId () {
		return idof[SystemMonitor::DashConfig];
	}


	@Override
	public org.radixware.kernel.server.types.FormHandler.NextDialogsRequest execCommand(org.radixware.kernel.common.types.Id cmdId, org.radixware.kernel.common.types.Id propId, org.apache.xmlbeans.XmlObject input, org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValsById, org.apache.xmlbeans.XmlObject output) throws org.radixware.kernel.common.exceptions.AppException,java.lang.InterruptedException{

			return super.execCommand(cmdId,propId,input,newPropValsById,output);
	}


}

/* Radix::Acs::PartitionGroup.DashConfig - Server Meta*/

/*Radix::Acs::PartitionGroup.DashConfig-Application Class*/

package org.radixware.ads.Acs.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class PartitionGroup.DashConfig_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("aclBWXLLGQQX5FKZJVZZXIOHIXPUQ"),"PartitionGroup.DashConfig",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOW5OO2TNLNDFDH6QMUU6E5PLLQ"),

						org.radixware.kernel.common.enums.EClassType.APPLICATION,

						/*Radix::Acs::PartitionGroup.DashConfig:Presentations-Entity Object Presentations*/
						/*Class presentation attributes*/
						 new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef(/*Owner Class Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("aclBWXLLGQQX5FKZJVZZXIOHIXPUQ"),
							/*Owner Class Name*/
							"PartitionGroup.DashConfig",
							/*Title Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOW5OO2TNLNDFDH6QMUU6E5PLLQ"),
							/*Property presentations*/

							/*Radix::Acs::PartitionGroup.DashConfig:Properties-Properties*/
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
							new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog[]{

									new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog(
									/*Radix::Acs::PartitionGroup.DashConfig:General-Dynamic Class Catalog*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eccYCNCT2DEA5HNVJIPJ7IS55QHUM"),org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.ELinkMode.VIRTUAL,new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.Item[]{
										new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.ClassRef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aclBWXLLGQQX5FKZJVZZXIOHIXPUQ"),null,30.0,true,org.radixware.kernel.common.types.Id.Factory.loadFrom("aclBWXLLGQQX5FKZJVZZXIOHIXPUQ"),null)}
									)
							},
							/*Restrictions*/
							null,null,0),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblLNMBHO73GNE2LF5TXFR7QEMICM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecLNMBHO73GNE2LF5TXFR7QEMICM"),

						/*Radix::Acs::PartitionGroup.DashConfig:Properties-Properties*/
						null,

						/*Radix::Acs::PartitionGroup.DashConfig:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthXWHHTDS7H5GNNMBX22SAGYK54U"),"getFamilySelectorPresentationId",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthVD22TSAPWBFRXA7RKUIXAFWBJY"),"getFamilyId",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.USER_CLASS)
						},
						null,
						null,null,null,false);
}

/* Radix::Acs::PartitionGroup.DashConfig - Desktop Executable*/

/*Radix::Acs::PartitionGroup.DashConfig-Application Class*/

package org.radixware.ads.Acs.explorer;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::PartitionGroup.DashConfig")
public interface PartitionGroup.DashConfig   extends org.radixware.ads.Acs.explorer.PartitionGroup  {
























}

/* Radix::Acs::PartitionGroup.DashConfig - Desktop Meta*/

/*Radix::Acs::PartitionGroup.DashConfig-Application Class*/

package org.radixware.ads.Acs.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class PartitionGroup.DashConfig_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::Acs::PartitionGroup.DashConfig:Presentations-Entity Object Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aclBWXLLGQQX5FKZJVZZXIOHIXPUQ"),
			"Radix::Acs::PartitionGroup.DashConfig",
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aecLNMBHO73GNE2LF5TXFR7QEMICM"),
			null,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOW5OO2TNLNDFDH6QMUU6E5PLLQ"),null,null,0,

			/*Radix::Acs::PartitionGroup.DashConfig:Properties-Properties*/
			null,
			null,
			null,
			null,
			null,
			null,
			true,false,false);
}

/* Radix::Acs::PartitionGroup.DashConfig - Web Executable*/

/*Radix::Acs::PartitionGroup.DashConfig-Application Class*/

package org.radixware.ads.Acs.web;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::PartitionGroup.DashConfig")
public interface PartitionGroup.DashConfig   extends org.radixware.ads.Acs.web.PartitionGroup  {
























}

/* Radix::Acs::PartitionGroup.DashConfig - Web Meta*/

/*Radix::Acs::PartitionGroup.DashConfig-Application Class*/

package org.radixware.ads.Acs.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class PartitionGroup.DashConfig_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::Acs::PartitionGroup.DashConfig:Presentations-Entity Object Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aclBWXLLGQQX5FKZJVZZXIOHIXPUQ"),
			"Radix::Acs::PartitionGroup.DashConfig",
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aecLNMBHO73GNE2LF5TXFR7QEMICM"),
			null,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOW5OO2TNLNDFDH6QMUU6E5PLLQ"),null,null,0,

			/*Radix::Acs::PartitionGroup.DashConfig:Properties-Properties*/
			null,
			null,
			null,
			null,
			null,
			null,
			true,false,false);
}

/* Radix::Acs::PartitionGroup.DashConfig - Localizing Bundle */
package org.radixware.ads.Acs.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class PartitionGroup.DashConfig - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Partition group for \'Dashboard Configuration\' family");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Группа разделов для семейства \'Конфигурация приборной панели\'");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOW5OO2TNLNDFDH6QMUU6E5PLLQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(PartitionGroup.DashConfig - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbaclBWXLLGQQX5FKZJVZZXIOHIXPUQ"),"PartitionGroup.DashConfig - Localizing Bundle",$$$items$$$);
}
