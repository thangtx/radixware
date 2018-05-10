
/* Radix::SystemMonitor::MetricType.NetChannel.Sync.BusyConnCnt - Server Executable*/

/*Radix::SystemMonitor::MetricType.NetChannel.Sync.BusyConnCnt-Application Class*/

package org.radixware.ads.SystemMonitor.server;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType.NetChannel.Sync.BusyConnCnt")
public class MetricType.NetChannel.Sync.BusyConnCnt  extends org.radixware.ads.SystemMonitor.server.MetricType.NetChannel  implements org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {


	/**Metainformation accessor method*/
	@SuppressWarnings("unused")
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return MetricType.NetChannel.Sync.BusyConnCnt_mi.rdxMeta;}

	/*Radix::SystemMonitor::MetricType.NetChannel.Sync.BusyConnCnt:Nested classes-Nested Classes*/

	/*Radix::SystemMonitor::MetricType.NetChannel.Sync.BusyConnCnt:Properties-Properties*/

	/*Radix::SystemMonitor::MetricType.NetChannel.Sync.BusyConnCnt:netChannel-Parent Reference*/






































	/*Radix::SystemMonitor::MetricType.NetChannel.Sync.BusyConnCnt:Methods-Methods*/

	/*Radix::SystemMonitor::MetricType.NetChannel.Sync.BusyConnCnt:getKind-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType.NetChannel.Sync.BusyConnCnt:getKind")
	public published  org.radixware.ads.SystemMonitor.common.MetricKind getKind () {
		return MetricKind:Net.Sync.BusyConnCnt;
	}

	/*Radix::SystemMonitor::MetricType.NetChannel.Sync.BusyConnCnt:getSensorType-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType.NetChannel.Sync.BusyConnCnt:getSensorType")
	public published  org.radixware.kernel.common.enums.ESensorType getSensorType () {
		return SensorType:NET_CHANNEL;
	}


	@Override
	public org.radixware.kernel.server.types.FormHandler.NextDialogsRequest execCommand(org.radixware.kernel.common.types.Id cmdId, org.radixware.kernel.common.types.Id propId, org.apache.xmlbeans.XmlObject input, org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValsById, org.apache.xmlbeans.XmlObject output) throws org.radixware.kernel.common.exceptions.AppException,java.lang.InterruptedException{

			return super.execCommand(cmdId,propId,input,newPropValsById,output);
	}


}

/* Radix::SystemMonitor::MetricType.NetChannel.Sync.BusyConnCnt - Server Meta*/

/*Radix::SystemMonitor::MetricType.NetChannel.Sync.BusyConnCnt-Application Class*/

package org.radixware.ads.SystemMonitor.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class MetricType.NetChannel.Sync.BusyConnCnt_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("aclN6R72ZALIZAWZN6AZ6Q3FDZD6Q"),"MetricType.NetChannel.Sync.BusyConnCnt",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3OOHQGXA7JBQVFVRGGJSF246RU"),

						org.radixware.kernel.common.enums.EClassType.APPLICATION,

						/*Radix::SystemMonitor::MetricType.NetChannel.Sync.BusyConnCnt:Presentations-Entity Object Presentations*/
						/*Class presentation attributes*/
						 new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef(/*Owner Class Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("aclN6R72ZALIZAWZN6AZ6Q3FDZD6Q"),
							/*Owner Class Name*/
							"MetricType.NetChannel.Sync.BusyConnCnt",
							/*Title Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3OOHQGXA7JBQVFVRGGJSF246RU"),
							/*Property presentations*/

							/*Radix::SystemMonitor::MetricType.NetChannel.Sync.BusyConnCnt:Properties-Properties*/
							new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef[]{

									/*Radix::SystemMonitor::MetricType.NetChannel.Sync.BusyConnCnt:netChannel:PropertyPresentation-Parent Ref Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadParentTitlePropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKGZDKSPHXFHBHPGRL3JCWVGXSQ"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,null,null,new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>",new org.radixware.kernel.server.meta.presentations.RadConditionDef.Prop2ValueCondition(new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colPRKQ2IQSPJDUJFZWAVDVZYN6ZI")},new String[]{null}),"org.radixware"),org.radixware.kernel.server.types.Restrictions.Factory.newInstance(133693439,null,null,null),null,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.TITLE,org.radixware.kernel.common.enums.EPropAttrInheritance.EDITING,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_SELECTOR,org.radixware.kernel.common.enums.EPropAttrInheritance.HINT))
							},
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
									/*Radix::SystemMonitor::MetricType.NetChannel.Sync.BusyConnCnt:Common-Dynamic Class Catalog*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eccXISAEUV6HRA7JNQ453IS2OBSFI"),org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.ELinkMode.VIRTUAL,new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.Item[]{
										new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.ClassRef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aclN6R72ZALIZAWZN6AZ6Q3FDZD6Q"),org.radixware.kernel.common.types.Id.Factory.loadFrom("cctF4SGPJDFJJFPFPAXLFD3GBKXTA"),245.0,true,org.radixware.kernel.common.types.Id.Factory.loadFrom("aclN6R72ZALIZAWZN6AZ6Q3FDZD6Q"),null)}
									)
							},
							/*Restrictions*/
							null,null,0),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl2H6SULJHXJGFVIS6UVHRWQS4AM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acl5V5QCN7ZP5DPFHWTMHEGX6HCXM"),

						/*Radix::SystemMonitor::MetricType.NetChannel.Sync.BusyConnCnt:Properties-Properties*/
						new org.radixware.kernel.server.meta.clazzes.RadPropDef[]{

								/*Radix::SystemMonitor::MetricType.NetChannel.Sync.BusyConnCnt:netChannel-Parent Reference*/

								new org.radixware.kernel.server.meta.clazzes.RadInnateRefPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKGZDKSPHXFHBHPGRL3JCWVGXSQ"),"netChannel",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7TFCKOPWVXOBDCLVAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("ref2RXWMT2TDVERPBYVVIWPKUQGHY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHBE24OUE2DNRDB7AAALOMT5GDM"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE)
						},

						/*Radix::SystemMonitor::MetricType.NetChannel.Sync.BusyConnCnt:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthSK64PGWTZNFZFAJVJ5NHXBAC6M"),"getKind",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth7M6YK3J5OREYTFHKMRDGE42Z3Q"),"getSensorType",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.INT)
						},
						null,
						null,null,null,false);
}

/* Radix::SystemMonitor::MetricType.NetChannel.Sync.BusyConnCnt - Desktop Executable*/

/*Radix::SystemMonitor::MetricType.NetChannel.Sync.BusyConnCnt-Application Class*/

package org.radixware.ads.SystemMonitor.explorer;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType.NetChannel.Sync.BusyConnCnt")
public interface MetricType.NetChannel.Sync.BusyConnCnt   extends org.radixware.ads.SystemMonitor.explorer.MetricType.NetChannel  {
























}

/* Radix::SystemMonitor::MetricType.NetChannel.Sync.BusyConnCnt - Desktop Meta*/

/*Radix::SystemMonitor::MetricType.NetChannel.Sync.BusyConnCnt-Application Class*/

package org.radixware.ads.SystemMonitor.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class MetricType.NetChannel.Sync.BusyConnCnt_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::SystemMonitor::MetricType.NetChannel.Sync.BusyConnCnt:Presentations-Entity Object Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aclN6R72ZALIZAWZN6AZ6Q3FDZD6Q"),
			"Radix::SystemMonitor::MetricType.NetChannel.Sync.BusyConnCnt",
			org.radixware.kernel.common.types.Id.Factory.loadFrom("acl5V5QCN7ZP5DPFHWTMHEGX6HCXM"),
			null,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3OOHQGXA7JBQVFVRGGJSF246RU"),null,null,0,

			/*Radix::SystemMonitor::MetricType.NetChannel.Sync.BusyConnCnt:Properties-Properties*/
			new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

					/*Radix::SystemMonitor::MetricType.NetChannel.Sync.BusyConnCnt:netChannel:PropertyPresentation-Parent Ref Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKGZDKSPHXFHBHPGRL3JCWVGXSQ"),
						"netChannel",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclN6R72ZALIZAWZN6AZ6Q3FDZD6Q"),
						org.radixware.kernel.common.enums.EValType.PARENT_REF,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						55,
						null,
						null,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						false,false,
						false,
						false,
						null,
						false,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsF2TKJDKNPFF5PH5D4VNRME7RLA"),
						null,
						null,
						true,-1,-1,1,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHBE24OUE2DNRDB7AAALOMT5GDM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblHBE24OUE2DNRDB7AAALOMT5GDM"),
						null,
						null,
						null,
						133693439,
						133693439,false)
			},
			null,
			null,
			null,
			null,
			null,
			true,false,false);
}

/* Radix::SystemMonitor::MetricType.NetChannel.Sync.BusyConnCnt - Web Meta*/

/*Radix::SystemMonitor::MetricType.NetChannel.Sync.BusyConnCnt-Application Class*/

package org.radixware.ads.SystemMonitor.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class MetricType.NetChannel.Sync.BusyConnCnt_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::SystemMonitor::MetricType.NetChannel.Sync.BusyConnCnt:Presentations-Entity Object Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aclN6R72ZALIZAWZN6AZ6Q3FDZD6Q"),
			"Radix::SystemMonitor::MetricType.NetChannel.Sync.BusyConnCnt",
			org.radixware.kernel.common.types.Id.Factory.loadFrom("acl5V5QCN7ZP5DPFHWTMHEGX6HCXM"),
			null,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3OOHQGXA7JBQVFVRGGJSF246RU"),null,null,0,
			null,
			null,
			null,
			null,
			null,
			null,
			false,false,false);
}

/* Radix::SystemMonitor::MetricType.NetChannel.Sync.BusyConnCnt - Localizing Bundle */
package org.radixware.ads.SystemMonitor.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class MetricType.NetChannel.Sync.BusyConnCnt - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Network Channel - Synchronous - Busy Connections Count");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Сетевой канал - Синхронный - Количество занятых соединений");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3OOHQGXA7JBQVFVRGGJSF246RU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(MetricType.NetChannel.Sync.BusyConnCnt - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbaclN6R72ZALIZAWZN6AZ6Q3FDZD6Q"),"MetricType.NetChannel.Sync.BusyConnCnt - Localizing Bundle",$$$items$$$);
}
