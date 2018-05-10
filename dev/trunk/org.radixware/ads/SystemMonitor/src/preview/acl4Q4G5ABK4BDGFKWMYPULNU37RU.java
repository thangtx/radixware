
/* Radix::SystemMonitor::MetricType.NetChannel.ClientConnectedTimePercent - Server Executable*/

/*Radix::SystemMonitor::MetricType.NetChannel.ClientConnectedTimePercent-Application Class*/

package org.radixware.ads.SystemMonitor.server;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType.NetChannel.ClientConnectedTimePercent")
public class MetricType.NetChannel.ClientConnectedTimePercent  extends org.radixware.ads.SystemMonitor.server.MetricType.NetChannel.ClientConnected  implements org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {


	/**Metainformation accessor method*/
	@SuppressWarnings("unused")
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return MetricType.NetChannel.ClientConnectedTimePercent_mi.rdxMeta;}

	/*Radix::SystemMonitor::MetricType.NetChannel.ClientConnectedTimePercent:Nested classes-Nested Classes*/

	/*Radix::SystemMonitor::MetricType.NetChannel.ClientConnectedTimePercent:Properties-Properties*/

	/*Radix::SystemMonitor::MetricType.NetChannel.ClientConnectedTimePercent:controlledValue-Column-Based Property*/






































	/*Radix::SystemMonitor::MetricType.NetChannel.ClientConnectedTimePercent:Methods-Methods*/

	/*Radix::SystemMonitor::MetricType.NetChannel.ClientConnectedTimePercent:getKind-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType.NetChannel.ClientConnectedTimePercent:getKind")
	public published  org.radixware.ads.SystemMonitor.common.MetricKind getKind () {
		return MetricKind:Net.ClientConnectTimePercent;
	}

	/*Radix::SystemMonitor::MetricType.NetChannel.ClientConnectedTimePercent:isControlledValueEditable-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType.NetChannel.ClientConnectedTimePercent:isControlledValueEditable")
	protected published  boolean isControlledValueEditable () {
		return false;
	}


	@Override
	public org.radixware.kernel.server.types.FormHandler.NextDialogsRequest execCommand(org.radixware.kernel.common.types.Id cmdId, org.radixware.kernel.common.types.Id propId, org.apache.xmlbeans.XmlObject input, org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValsById, org.apache.xmlbeans.XmlObject output) throws org.radixware.kernel.common.exceptions.AppException,java.lang.InterruptedException{

			return super.execCommand(cmdId,propId,input,newPropValsById,output);
	}


}

/* Radix::SystemMonitor::MetricType.NetChannel.ClientConnectedTimePercent - Server Meta*/

/*Radix::SystemMonitor::MetricType.NetChannel.ClientConnectedTimePercent-Application Class*/

package org.radixware.ads.SystemMonitor.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class MetricType.NetChannel.ClientConnectedTimePercent_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("acl4Q4G5ABK4BDGFKWMYPULNU37RU"),"MetricType.NetChannel.ClientConnectedTimePercent",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUYLLI43GERFNVBV7VMVRCZCQMY"),

						org.radixware.kernel.common.enums.EClassType.APPLICATION,

						/*Radix::SystemMonitor::MetricType.NetChannel.ClientConnectedTimePercent:Presentations-Entity Object Presentations*/
						/*Class presentation attributes*/
						 new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef(/*Owner Class Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("acl4Q4G5ABK4BDGFKWMYPULNU37RU"),
							/*Owner Class Name*/
							"MetricType.NetChannel.ClientConnectedTimePercent",
							/*Title Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUYLLI43GERFNVBV7VMVRCZCQMY"),
							/*Property presentations*/

							/*Radix::SystemMonitor::MetricType.NetChannel.ClientConnectedTimePercent:Properties-Properties*/
							new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef[]{

									/*Radix::SystemMonitor::MetricType.NetChannel.ClientConnectedTimePercent:controlledValue:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPSRIBABNSJD45B3FB3UDKHACIM"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.TITLE,org.radixware.kernel.common.enums.EPropAttrInheritance.EDITING,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_SELECT_CONDITION,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_SELECTOR,org.radixware.kernel.common.enums.EPropAttrInheritance.HINT))
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
									/*Radix::SystemMonitor::MetricType.NetChannel.ClientConnectedTimePercent:Edit-Editor Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprH44GWC3B5BBA3JLBBZRNS72KFE"),
									"Edit",
									org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
									org.radixware.kernel.common.types.Id.Factory.loadFrom("epr5NYQFD3TZ5ATJL65ZK542GMR54"),
									40112,
									null,

									/*Radix::SystemMonitor::MetricType.NetChannel.ClientConnectedTimePercent:Edit:Children-Explorer Items*/
										null
									,
									null,
									(org.radixware.kernel.server.meta.presentations.RadPropertyPresentationAttributes[]) null,
									org.radixware.kernel.common.enums.EEditorPresentationRightsInheritanceMode.NONE,
									null)
							},
							/*Selector presentations*/
							null,
							/*Default selector presentation Id*/
							null,
							/*Presentation Map*/
							org.radixware.kernel.common.utils.Maps.fromArrays(new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr5NYQFD3TZ5ATJL65ZK542GMR54")},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprH44GWC3B5BBA3JLBBZRNS72KFE")}),
							/*Object title format*/
							null,
							/*Class catalogs*/
							new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog[]{

									new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog(
									/*Radix::SystemMonitor::MetricType.NetChannel.ClientConnectedTimePercent:Common-Dynamic Class Catalog*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eccXISAEUV6HRA7JNQ453IS2OBSFI"),org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.ELinkMode.VIRTUAL,new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.Item[]{
										new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.ClassRef(org.radixware.kernel.common.types.Id.Factory.loadFrom("acl4Q4G5ABK4BDGFKWMYPULNU37RU"),org.radixware.kernel.common.types.Id.Factory.loadFrom("cctF4SGPJDFJJFPFPAXLFD3GBKXTA"),235.0,true,org.radixware.kernel.common.types.Id.Factory.loadFrom("acl4Q4G5ABK4BDGFKWMYPULNU37RU"),null)}
									)
							},
							/*Restrictions*/
							null,null,0),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl2H6SULJHXJGFVIS6UVHRWQS4AM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclK6BVZGULL5E5NPHE42OK6I6NKU"),

						/*Radix::SystemMonitor::MetricType.NetChannel.ClientConnectedTimePercent:Properties-Properties*/
						new org.radixware.kernel.server.meta.clazzes.RadPropDef[]{

								/*Radix::SystemMonitor::MetricType.NetChannel.ClientConnectedTimePercent:controlledValue-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPSRIBABNSJD45B3FB3UDKHACIM"),"controlledValue",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDBPAH5NSRRBDJJDYOYSW3W2BMU"),org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("acs3JQBPC2NCJD37LVXZ6FUAXVNZ4"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("AVG")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE)
						},

						/*Radix::SystemMonitor::MetricType.NetChannel.ClientConnectedTimePercent:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthSK64PGWTZNFZFAJVJ5NHXBAC6M"),"getKind",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthWWMO5AMVONHYFPXVGHKONUKSPY"),"isControlledValueEditable",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE)
						},
						null,
						null,null,null,false);
}

/* Radix::SystemMonitor::MetricType.NetChannel.ClientConnectedTimePercent - Desktop Executable*/

/*Radix::SystemMonitor::MetricType.NetChannel.ClientConnectedTimePercent-Application Class*/

package org.radixware.ads.SystemMonitor.explorer;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType.NetChannel.ClientConnectedTimePercent")
public interface MetricType.NetChannel.ClientConnectedTimePercent   extends org.radixware.ads.SystemMonitor.explorer.MetricType.NetChannel.ClientConnected  {


















































}

/* Radix::SystemMonitor::MetricType.NetChannel.ClientConnectedTimePercent - Desktop Meta*/

/*Radix::SystemMonitor::MetricType.NetChannel.ClientConnectedTimePercent-Application Class*/

package org.radixware.ads.SystemMonitor.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class MetricType.NetChannel.ClientConnectedTimePercent_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::SystemMonitor::MetricType.NetChannel.ClientConnectedTimePercent:Presentations-Entity Object Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("acl4Q4G5ABK4BDGFKWMYPULNU37RU"),
			"Radix::SystemMonitor::MetricType.NetChannel.ClientConnectedTimePercent",
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aclK6BVZGULL5E5NPHE42OK6I6NKU"),
			null,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUYLLI43GERFNVBV7VMVRCZCQMY"),null,null,0,

			/*Radix::SystemMonitor::MetricType.NetChannel.ClientConnectedTimePercent:Properties-Properties*/
			new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

					/*Radix::SystemMonitor::MetricType.NetChannel.ClientConnectedTimePercent:controlledValue:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPSRIBABNSJD45B3FB3UDKHACIM"),
						"controlledValue",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acl4Q4G5ABK4BDGFKWMYPULNU37RU"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						63,
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acs3JQBPC2NCJD37LVXZ6FUAXVNZ4"),
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("AVG"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::SystemMonitor::MetricType.NetChannel.ClientConnectedTimePercent:controlledValue:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acs3JQBPC2NCJD37LVXZ6FUAXVNZ4"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.NONE,null),
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
			new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprH44GWC3B5BBA3JLBBZRNS72KFE")},
			true,false,false);
}

/* Radix::SystemMonitor::MetricType.NetChannel.ClientConnectedTimePercent - Web Meta*/

/*Radix::SystemMonitor::MetricType.NetChannel.ClientConnectedTimePercent-Application Class*/

package org.radixware.ads.SystemMonitor.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class MetricType.NetChannel.ClientConnectedTimePercent_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::SystemMonitor::MetricType.NetChannel.ClientConnectedTimePercent:Presentations-Entity Object Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("acl4Q4G5ABK4BDGFKWMYPULNU37RU"),
			"Radix::SystemMonitor::MetricType.NetChannel.ClientConnectedTimePercent",
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aclK6BVZGULL5E5NPHE42OK6I6NKU"),
			null,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUYLLI43GERFNVBV7VMVRCZCQMY"),null,null,0,
			null,
			null,
			null,
			null,
			null,
			null,
			false,false,false);
}

/* Radix::SystemMonitor::MetricType.NetChannel.ClientConnectedTimePercent:Edit - Desktop Meta*/

/*Radix::SystemMonitor::MetricType.NetChannel.ClientConnectedTimePercent:Edit-Editor Presentation*/

package org.radixware.ads.SystemMonitor.explorer;
public final class Edit_mi{
	private static final class Edit_DEF extends org.radixware.kernel.common.client.meta.RadEditorPresentationDef{
		Edit_DEF(){
			super(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprH44GWC3B5BBA3JLBBZRNS72KFE"),
			"Edit",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("epr5NYQFD3TZ5ATJL65ZK542GMR54"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("acl4Q4G5ABK4BDGFKWMYPULNU37RU"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl2H6SULJHXJGFVIS6UVHRWQS4AM"),
			null,
			null,
			null,
			null,

			/*Radix::SystemMonitor::MetricType.NetChannel.ClientConnectedTimePercent:Edit:Children-Explorer Items*/
				null
			,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
			null,
			0,
			null,
			(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
			40112,0,0);
		}
		@Override
		protected org.radixware.kernel.common.client.models.Model createModelImpl(org.radixware.kernel.common.client.IClientEnvironment userSession) {    return new org.radixware.ads.SystemMonitor.explorer.MetricType.NetChannel.ClientConnectedTimePercent.MetricType.NetChannel.ClientConnectedTimePercent_DefaultModel.epr5NYQFD3TZ5ATJL65ZK542GMR54_ModelAdapter(userSession,this);
		}
	}
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new Edit_DEF(); 
;
}
/* Radix::SystemMonitor::MetricType.NetChannel.ClientConnectedTimePercent - Localizing Bundle */
package org.radixware.ads.SystemMonitor.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class MetricType.NetChannel.ClientConnectedTimePercent - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Network Client - Time Percentage in Connected State");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Сетевой клиент - Процент времени в состоянии Connected");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUYLLI43GERFNVBV7VMVRCZCQMY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(MetricType.NetChannel.ClientConnectedTimePercent - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbacl4Q4G5ABK4BDGFKWMYPULNU37RU"),"MetricType.NetChannel.ClientConnectedTimePercent - Localizing Bundle",$$$items$$$);
}
