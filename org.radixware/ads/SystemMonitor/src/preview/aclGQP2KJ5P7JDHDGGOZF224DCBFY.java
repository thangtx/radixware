
/* Radix::SystemMonitor::MetricType.NetChannel.ConnCnt - Server Executable*/

/*Radix::SystemMonitor::MetricType.NetChannel.ConnCnt-Application Class*/

package org.radixware.ads.SystemMonitor.server;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType.NetChannel.ConnCnt")
public class MetricType.NetChannel.ConnCnt  extends org.radixware.ads.SystemMonitor.server.MetricType.NetChannel  implements org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {


	/**Metainformation accessor method*/
	@SuppressWarnings("unused")
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return MetricType.NetChannel.ConnCnt_mi.rdxMeta;}

	/*Radix::SystemMonitor::MetricType.NetChannel.ConnCnt:Nested classes-Nested Classes*/

	/*Radix::SystemMonitor::MetricType.NetChannel.ConnCnt:Properties-Properties*/

	/*Radix::SystemMonitor::MetricType.NetChannel.ConnCnt:netChannel-Parent Reference*/






































	/*Radix::SystemMonitor::MetricType.NetChannel.ConnCnt:Methods-Methods*/

	/*Radix::SystemMonitor::MetricType.NetChannel.ConnCnt:getKind-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType.NetChannel.ConnCnt:getKind")
	public published  org.radixware.ads.SystemMonitor.common.MetricKind getKind () {
		return MetricKind:Net.ServerConnect;
	}

	/*Radix::SystemMonitor::MetricType.NetChannel.ConnCnt:getSensorType-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType.NetChannel.ConnCnt:getSensorType")
	public published  org.radixware.kernel.common.enums.ESensorType getSensorType () {
		return SensorType:NET_CHANNEL;
	}


	@Override
	public org.radixware.kernel.server.types.FormHandler.NextDialogsRequest execCommand(org.radixware.kernel.common.types.Id cmdId, org.radixware.kernel.common.types.Id propId, org.apache.xmlbeans.XmlObject input, org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValsById, org.apache.xmlbeans.XmlObject output) throws org.radixware.kernel.common.exceptions.AppException,java.lang.InterruptedException{

			return super.execCommand(cmdId,propId,input,newPropValsById,output);
	}


}

/* Radix::SystemMonitor::MetricType.NetChannel.ConnCnt - Server Meta*/

/*Radix::SystemMonitor::MetricType.NetChannel.ConnCnt-Application Class*/

package org.radixware.ads.SystemMonitor.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class MetricType.NetChannel.ConnCnt_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("aclGQP2KJ5P7JDHDGGOZF224DCBFY"),"MetricType.NetChannel.ConnCnt",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBILRM7TI6BDP7CCGWOAEO4HP5U"),

						org.radixware.kernel.common.enums.EClassType.APPLICATION,

						/*Radix::SystemMonitor::MetricType.NetChannel.ConnCnt:Presentations-Entity Object Presentations*/
						/*Class presentation attributes*/
						 new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef(/*Owner Class Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("aclGQP2KJ5P7JDHDGGOZF224DCBFY"),
							/*Owner Class Name*/
							"MetricType.NetChannel.ConnCnt",
							/*Title Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBILRM7TI6BDP7CCGWOAEO4HP5U"),
							/*Property presentations*/

							/*Radix::SystemMonitor::MetricType.NetChannel.ConnCnt:Properties-Properties*/
							new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef[]{

									/*Radix::SystemMonitor::MetricType.NetChannel.ConnCnt:netChannel:PropertyPresentation-Parent Ref Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadParentTitlePropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKGZDKSPHXFHBHPGRL3JCWVGXSQ"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,null,null,new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),org.radixware.kernel.server.types.Restrictions.Factory.newInstance(133693439,null,null,null),null,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.TITLE,org.radixware.kernel.common.enums.EPropAttrInheritance.EDITING,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_SELECTOR,org.radixware.kernel.common.enums.EPropAttrInheritance.HINT))
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
									/*Radix::SystemMonitor::MetricType.NetChannel.ConnCnt:Edit-Editor Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprV4VWPUX3NFBD5EIS3RG3NIYPVU"),
									"Edit",
									org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
									org.radixware.kernel.common.types.Id.Factory.loadFrom("epr5NYQFD3TZ5ATJL65ZK542GMR54"),
									40112,
									null,

									/*Radix::SystemMonitor::MetricType.NetChannel.ConnCnt:Edit:Children-Explorer Items*/
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
							org.radixware.kernel.common.utils.Maps.fromArrays(new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr5NYQFD3TZ5ATJL65ZK542GMR54")},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprV4VWPUX3NFBD5EIS3RG3NIYPVU")}),
							/*Object title format*/
							null,
							/*Class catalogs*/
							new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog[]{

									new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog(
									/*Radix::SystemMonitor::MetricType.NetChannel.ConnCnt:Common-Dynamic Class Catalog*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eccXISAEUV6HRA7JNQ453IS2OBSFI"),org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.ELinkMode.VIRTUAL,new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.Item[]{
										new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.ClassRef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aclGQP2KJ5P7JDHDGGOZF224DCBFY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("cctF4SGPJDFJJFPFPAXLFD3GBKXTA"),215.0,true,org.radixware.kernel.common.types.Id.Factory.loadFrom("aclGQP2KJ5P7JDHDGGOZF224DCBFY"),null)}
									)
							},
							/*Restrictions*/
							null,null,0),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl2H6SULJHXJGFVIS6UVHRWQS4AM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acl5V5QCN7ZP5DPFHWTMHEGX6HCXM"),

						/*Radix::SystemMonitor::MetricType.NetChannel.ConnCnt:Properties-Properties*/
						new org.radixware.kernel.server.meta.clazzes.RadPropDef[]{

								/*Radix::SystemMonitor::MetricType.NetChannel.ConnCnt:netChannel-Parent Reference*/

								new org.radixware.kernel.server.meta.clazzes.RadInnateRefPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKGZDKSPHXFHBHPGRL3JCWVGXSQ"),"netChannel",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7TFCKOPWVXOBDCLVAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("ref2RXWMT2TDVERPBYVVIWPKUQGHY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHBE24OUE2DNRDB7AAALOMT5GDM"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE)
						},

						/*Radix::SystemMonitor::MetricType.NetChannel.ConnCnt:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthSK64PGWTZNFZFAJVJ5NHXBAC6M"),"getKind",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth7M6YK3J5OREYTFHKMRDGE42Z3Q"),"getSensorType",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.INT)
						},
						null,
						null,null,null,false);
}

/* Radix::SystemMonitor::MetricType.NetChannel.ConnCnt - Desktop Executable*/

/*Radix::SystemMonitor::MetricType.NetChannel.ConnCnt-Application Class*/

package org.radixware.ads.SystemMonitor.explorer;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType.NetChannel.ConnCnt")
public interface MetricType.NetChannel.ConnCnt   extends org.radixware.ads.SystemMonitor.explorer.MetricType.NetChannel  {


















































}

/* Radix::SystemMonitor::MetricType.NetChannel.ConnCnt - Desktop Meta*/

/*Radix::SystemMonitor::MetricType.NetChannel.ConnCnt-Application Class*/

package org.radixware.ads.SystemMonitor.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class MetricType.NetChannel.ConnCnt_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::SystemMonitor::MetricType.NetChannel.ConnCnt:Presentations-Entity Object Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aclGQP2KJ5P7JDHDGGOZF224DCBFY"),
			"Radix::SystemMonitor::MetricType.NetChannel.ConnCnt",
			org.radixware.kernel.common.types.Id.Factory.loadFrom("acl5V5QCN7ZP5DPFHWTMHEGX6HCXM"),
			null,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBILRM7TI6BDP7CCGWOAEO4HP5U"),null,null,0,

			/*Radix::SystemMonitor::MetricType.NetChannel.ConnCnt:Properties-Properties*/
			new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

					/*Radix::SystemMonitor::MetricType.NetChannel.ConnCnt:netChannel:PropertyPresentation-Parent Ref Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKGZDKSPHXFHBHPGRL3JCWVGXSQ"),
						"netChannel",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclGQP2KJ5P7JDHDGGOZF224DCBFY"),
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
			new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprV4VWPUX3NFBD5EIS3RG3NIYPVU")},
			true,false,false);
}

/* Radix::SystemMonitor::MetricType.NetChannel.ConnCnt - Web Meta*/

/*Radix::SystemMonitor::MetricType.NetChannel.ConnCnt-Application Class*/

package org.radixware.ads.SystemMonitor.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class MetricType.NetChannel.ConnCnt_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::SystemMonitor::MetricType.NetChannel.ConnCnt:Presentations-Entity Object Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aclGQP2KJ5P7JDHDGGOZF224DCBFY"),
			"Radix::SystemMonitor::MetricType.NetChannel.ConnCnt",
			org.radixware.kernel.common.types.Id.Factory.loadFrom("acl5V5QCN7ZP5DPFHWTMHEGX6HCXM"),
			null,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBILRM7TI6BDP7CCGWOAEO4HP5U"),null,null,0,
			null,
			null,
			null,
			null,
			null,
			null,
			false,false,false);
}

/* Radix::SystemMonitor::MetricType.NetChannel.ConnCnt:Edit - Desktop Meta*/

/*Radix::SystemMonitor::MetricType.NetChannel.ConnCnt:Edit-Editor Presentation*/

package org.radixware.ads.SystemMonitor.explorer;
public final class Edit_mi{
	private static final class Edit_DEF extends org.radixware.kernel.common.client.meta.RadEditorPresentationDef{
		Edit_DEF(){
			super(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprV4VWPUX3NFBD5EIS3RG3NIYPVU"),
			"Edit",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("epr5NYQFD3TZ5ATJL65ZK542GMR54"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aclGQP2KJ5P7JDHDGGOZF224DCBFY"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl2H6SULJHXJGFVIS6UVHRWQS4AM"),
			null,
			null,
			null,
			null,

			/*Radix::SystemMonitor::MetricType.NetChannel.ConnCnt:Edit:Children-Explorer Items*/
				null
			,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
			null,
			0,
			null,
			(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
			40112,0,0);
		}
		@Override
		protected org.radixware.kernel.common.client.models.Model createModelImpl(org.radixware.kernel.common.client.IClientEnvironment userSession) {    return new org.radixware.ads.SystemMonitor.explorer.MetricType.NetChannel.ConnCnt.MetricType.NetChannel.ConnCnt_DefaultModel.epr5NYQFD3TZ5ATJL65ZK542GMR54_ModelAdapter(userSession,this);
		}
	}
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new Edit_DEF(); 
;
}
/* Radix::SystemMonitor::MetricType.NetChannel.ConnCnt - Localizing Bundle */
package org.radixware.ads.SystemMonitor.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class MetricType.NetChannel.ConnCnt - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Network Server - Number of Active Connections");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Сетевой сервер - Количество активных соединений");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBILRM7TI6BDP7CCGWOAEO4HP5U"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(MetricType.NetChannel.ConnCnt - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbaclGQP2KJ5P7JDHDGGOZF224DCBFY"),"MetricType.NetChannel.ConnCnt - Localizing Bundle",$$$items$$$);
}
