
/* Radix::SystemMonitor::MetricType.NetChannel.ClientConnected - Server Executable*/

/*Radix::SystemMonitor::MetricType.NetChannel.ClientConnected-Application Class*/

package org.radixware.ads.SystemMonitor.server;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType.NetChannel.ClientConnected")
public class MetricType.NetChannel.ClientConnected  extends org.radixware.ads.SystemMonitor.server.MetricType.NetChannel  implements org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {


	/**Metainformation accessor method*/
	@SuppressWarnings("unused")
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return MetricType.NetChannel.ClientConnected_mi.rdxMeta;}

	/*Radix::SystemMonitor::MetricType.NetChannel.ClientConnected:Nested classes-Nested Classes*/

	/*Radix::SystemMonitor::MetricType.NetChannel.ClientConnected:Properties-Properties*/

	/*Radix::SystemMonitor::MetricType.NetChannel.ClientConnected:netChannel-Parent Reference*/






































	/*Radix::SystemMonitor::MetricType.NetChannel.ClientConnected:Methods-Methods*/

	/*Radix::SystemMonitor::MetricType.NetChannel.ClientConnected:getKind-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType.NetChannel.ClientConnected:getKind")
	public published  org.radixware.ads.SystemMonitor.common.MetricKind getKind () {
		return MetricKind:Net.ClientConnect;
	}

	/*Radix::SystemMonitor::MetricType.NetChannel.ClientConnected:getSensorType-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType.NetChannel.ClientConnected:getSensorType")
	public published  org.radixware.kernel.common.enums.ESensorType getSensorType () {
		return SensorType:NET_CHANNEL;
	}


	@Override
	public org.radixware.kernel.server.types.FormHandler.NextDialogsRequest execCommand(org.radixware.kernel.common.types.Id cmdId, org.radixware.kernel.common.types.Id propId, org.apache.xmlbeans.XmlObject input, org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValsById, org.apache.xmlbeans.XmlObject output) throws org.radixware.kernel.common.exceptions.AppException,java.lang.InterruptedException{

			return super.execCommand(cmdId,propId,input,newPropValsById,output);
	}


}

/* Radix::SystemMonitor::MetricType.NetChannel.ClientConnected - Server Meta*/

/*Radix::SystemMonitor::MetricType.NetChannel.ClientConnected-Application Class*/

package org.radixware.ads.SystemMonitor.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class MetricType.NetChannel.ClientConnected_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("aclK6BVZGULL5E5NPHE42OK6I6NKU"),"MetricType.NetChannel.ClientConnected",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsO6QMYJ3NQVEN7KEKDIHKHL3IZ4"),

						org.radixware.kernel.common.enums.EClassType.APPLICATION,

						/*Radix::SystemMonitor::MetricType.NetChannel.ClientConnected:Presentations-Entity Object Presentations*/
						/*Class presentation attributes*/
						 new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef(/*Owner Class Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("aclK6BVZGULL5E5NPHE42OK6I6NKU"),
							/*Owner Class Name*/
							"MetricType.NetChannel.ClientConnected",
							/*Title Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsO6QMYJ3NQVEN7KEKDIHKHL3IZ4"),
							/*Property presentations*/

							/*Radix::SystemMonitor::MetricType.NetChannel.ClientConnected:Properties-Properties*/
							new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef[]{

									/*Radix::SystemMonitor::MetricType.NetChannel.ClientConnected:netChannel:PropertyPresentation-Parent Ref Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadParentTitlePropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKGZDKSPHXFHBHPGRL3JCWVGXSQ"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,null,null,new org.radixware.kernel.server.meta.presentations.RadConditionDef("<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:PropSqlName TableId=\"aecHBE24OUE2DNRDB7AAALOMT5GDM\" PropId=\"colW7RFZLTFEZECHEAFABQSI62TGY\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> = 0 and </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecHBE24OUE2DNRDB7AAALOMT5GDM\" PropId=\"colF3F5Y5EF2DNRDB7AAALOMT5GDM\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> &lt;= 1</xsc:Sql></xsc:Item></xsc:Sqml>","<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),org.radixware.kernel.server.types.Restrictions.Factory.newInstance(133693439,null,null,null),null,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.EDITING,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_SELECTOR,org.radixware.kernel.common.enums.EPropAttrInheritance.HINT))
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
									/*Radix::SystemMonitor::MetricType.NetChannel.ClientConnected:Edit-Editor Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprC7MOIWCXQBDVLBP74IF5XWSMAI"),
									"Edit",
									org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
									org.radixware.kernel.common.types.Id.Factory.loadFrom("epr5NYQFD3TZ5ATJL65ZK542GMR54"),
									40112,
									null,

									/*Radix::SystemMonitor::MetricType.NetChannel.ClientConnected:Edit:Children-Explorer Items*/
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
							org.radixware.kernel.common.utils.Maps.fromArrays(new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr5NYQFD3TZ5ATJL65ZK542GMR54")},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprC7MOIWCXQBDVLBP74IF5XWSMAI")}),
							/*Object title format*/
							null,
							/*Class catalogs*/
							new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog[]{

									new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog(
									/*Radix::SystemMonitor::MetricType.NetChannel.ClientConnected:Common-Dynamic Class Catalog*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eccXISAEUV6HRA7JNQ453IS2OBSFI"),org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.ELinkMode.VIRTUAL,new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.Item[]{
										new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.ClassRef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aclK6BVZGULL5E5NPHE42OK6I6NKU"),org.radixware.kernel.common.types.Id.Factory.loadFrom("cctF4SGPJDFJJFPFPAXLFD3GBKXTA"),225.0,true,org.radixware.kernel.common.types.Id.Factory.loadFrom("aclK6BVZGULL5E5NPHE42OK6I6NKU"),null)}
									)
							},
							/*Restrictions*/
							null,null,0),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl2H6SULJHXJGFVIS6UVHRWQS4AM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acl5V5QCN7ZP5DPFHWTMHEGX6HCXM"),

						/*Radix::SystemMonitor::MetricType.NetChannel.ClientConnected:Properties-Properties*/
						new org.radixware.kernel.server.meta.clazzes.RadPropDef[]{

								/*Radix::SystemMonitor::MetricType.NetChannel.ClientConnected:netChannel-Parent Reference*/

								new org.radixware.kernel.server.meta.clazzes.RadInnateRefPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKGZDKSPHXFHBHPGRL3JCWVGXSQ"),"netChannel",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLWR7R7IPJBGUNC2IPU6H3WYL2Q"),org.radixware.kernel.common.types.Id.Factory.loadFrom("ref2RXWMT2TDVERPBYVVIWPKUQGHY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHBE24OUE2DNRDB7AAALOMT5GDM"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE)
						},

						/*Radix::SystemMonitor::MetricType.NetChannel.ClientConnected:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthSK64PGWTZNFZFAJVJ5NHXBAC6M"),"getKind",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth7M6YK3J5OREYTFHKMRDGE42Z3Q"),"getSensorType",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.INT)
						},
						null,
						null,null,null,false);
}

/* Radix::SystemMonitor::MetricType.NetChannel.ClientConnected - Desktop Executable*/

/*Radix::SystemMonitor::MetricType.NetChannel.ClientConnected-Application Class*/

package org.radixware.ads.SystemMonitor.explorer;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType.NetChannel.ClientConnected")
public interface MetricType.NetChannel.ClientConnected   extends org.radixware.ads.SystemMonitor.explorer.MetricType.NetChannel  {


















































}

/* Radix::SystemMonitor::MetricType.NetChannel.ClientConnected - Desktop Meta*/

/*Radix::SystemMonitor::MetricType.NetChannel.ClientConnected-Application Class*/

package org.radixware.ads.SystemMonitor.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class MetricType.NetChannel.ClientConnected_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::SystemMonitor::MetricType.NetChannel.ClientConnected:Presentations-Entity Object Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aclK6BVZGULL5E5NPHE42OK6I6NKU"),
			"Radix::SystemMonitor::MetricType.NetChannel.ClientConnected",
			org.radixware.kernel.common.types.Id.Factory.loadFrom("acl5V5QCN7ZP5DPFHWTMHEGX6HCXM"),
			null,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsO6QMYJ3NQVEN7KEKDIHKHL3IZ4"),null,null,0,

			/*Radix::SystemMonitor::MetricType.NetChannel.ClientConnected:Properties-Properties*/
			new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

					/*Radix::SystemMonitor::MetricType.NetChannel.ClientConnected:netChannel:PropertyPresentation-Parent Ref Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKGZDKSPHXFHBHPGRL3JCWVGXSQ"),
						"netChannel",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLWR7R7IPJBGUNC2IPU6H3WYL2Q"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclK6BVZGULL5E5NPHE42OK6I6NKU"),
						org.radixware.kernel.common.enums.EValType.PARENT_REF,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						54,
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
			new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprC7MOIWCXQBDVLBP74IF5XWSMAI")},
			true,false,false);
}

/* Radix::SystemMonitor::MetricType.NetChannel.ClientConnected - Web Meta*/

/*Radix::SystemMonitor::MetricType.NetChannel.ClientConnected-Application Class*/

package org.radixware.ads.SystemMonitor.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class MetricType.NetChannel.ClientConnected_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::SystemMonitor::MetricType.NetChannel.ClientConnected:Presentations-Entity Object Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aclK6BVZGULL5E5NPHE42OK6I6NKU"),
			"Radix::SystemMonitor::MetricType.NetChannel.ClientConnected",
			org.radixware.kernel.common.types.Id.Factory.loadFrom("acl5V5QCN7ZP5DPFHWTMHEGX6HCXM"),
			null,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsO6QMYJ3NQVEN7KEKDIHKHL3IZ4"),null,null,0,
			null,
			null,
			null,
			null,
			null,
			null,
			false,false,false);
}

/* Radix::SystemMonitor::MetricType.NetChannel.ClientConnected:Edit - Desktop Meta*/

/*Radix::SystemMonitor::MetricType.NetChannel.ClientConnected:Edit-Editor Presentation*/

package org.radixware.ads.SystemMonitor.explorer;
public final class Edit_mi{
	private static final class Edit_DEF extends org.radixware.kernel.common.client.meta.RadEditorPresentationDef{
		Edit_DEF(){
			super(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprC7MOIWCXQBDVLBP74IF5XWSMAI"),
			"Edit",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("epr5NYQFD3TZ5ATJL65ZK542GMR54"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aclK6BVZGULL5E5NPHE42OK6I6NKU"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl2H6SULJHXJGFVIS6UVHRWQS4AM"),
			null,
			null,
			null,
			null,

			/*Radix::SystemMonitor::MetricType.NetChannel.ClientConnected:Edit:Children-Explorer Items*/
				null
			,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
			null,
			0,
			null,
			(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
			40112,0,0);
		}
		@Override
		protected org.radixware.kernel.common.client.models.Model createModelImpl(org.radixware.kernel.common.client.IClientEnvironment userSession) {    return new org.radixware.ads.SystemMonitor.explorer.MetricType.NetChannel.ClientConnected.MetricType.NetChannel.ClientConnected_DefaultModel.epr5NYQFD3TZ5ATJL65ZK542GMR54_ModelAdapter(userSession,this);
		}
	}
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new Edit_DEF(); 
;
}
/* Radix::SystemMonitor::MetricType.NetChannel.ClientConnected - Localizing Bundle */
package org.radixware.ads.SystemMonitor.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class MetricType.NetChannel.ClientConnected - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Network client");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Сетевой клиент");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLWR7R7IPJBGUNC2IPU6H3WYL2Q"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Network Client - Connected");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Сетевой клиент - Соединение установлено");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsO6QMYJ3NQVEN7KEKDIHKHL3IZ4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(MetricType.NetChannel.ClientConnected - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbaclK6BVZGULL5E5NPHE42OK6I6NKU"),"MetricType.NetChannel.ClientConnected - Localizing Bundle",$$$items$$$);
}
