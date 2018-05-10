
/* Radix::SystemMonitor::MetricType.UserFunc.Stat - Server Executable*/

/*Radix::SystemMonitor::MetricType.UserFunc.Stat-Application Class*/

package org.radixware.ads.SystemMonitor.server;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType.UserFunc.Stat")
public published class MetricType.UserFunc.Stat  extends org.radixware.ads.SystemMonitor.server.MetricType.UserFunc  implements org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {


	/**Metainformation accessor method*/
	@SuppressWarnings("unused")
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return MetricType.UserFunc.Stat_mi.rdxMeta;}

	/*Radix::SystemMonitor::MetricType.UserFunc.Stat:Nested classes-Nested Classes*/

	/*Radix::SystemMonitor::MetricType.UserFunc.Stat:Properties-Properties*/

	/*Radix::SystemMonitor::MetricType.UserFunc.Stat:calcStatValueFunction-User Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType.UserFunc.Stat:calcStatValueFunction")
	public published  org.radixware.ads.SystemMonitor.server.UserFunc.CalcStatUserMetric getCalcStatValueFunction() {
		return calcStatValueFunction;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType.UserFunc.Stat:calcStatValueFunction")
	public published   void setCalcStatValueFunction(org.radixware.ads.SystemMonitor.server.UserFunc.CalcStatUserMetric val) {
		calcStatValueFunction = val;
	}



































	/*Radix::SystemMonitor::MetricType.UserFunc.Stat:Methods-Methods*/

	/*Radix::SystemMonitor::MetricType.UserFunc.Stat:getKind-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType.UserFunc.Stat:getKind")
	public published  org.radixware.ads.SystemMonitor.common.MetricKind getKind () {
		return MetricKind:User.Func.Stat;
	}

	/*Radix::SystemMonitor::MetricType.UserFunc.Stat:getValueType-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType.UserFunc.Stat:getValueType")
	protected published  org.radixware.ads.SystemMonitor.common.MetricValueType getValueType () {
		return MetricValueType:Statistic;
	}


	@Override
	public org.radixware.kernel.server.types.FormHandler.NextDialogsRequest execCommand(org.radixware.kernel.common.types.Id cmdId, org.radixware.kernel.common.types.Id propId, org.apache.xmlbeans.XmlObject input, org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValsById, org.apache.xmlbeans.XmlObject output) throws org.radixware.kernel.common.exceptions.AppException,java.lang.InterruptedException{

			return super.execCommand(cmdId,propId,input,newPropValsById,output);
	}


}

/* Radix::SystemMonitor::MetricType.UserFunc.Stat - Server Meta*/

/*Radix::SystemMonitor::MetricType.UserFunc.Stat-Application Class*/

package org.radixware.ads.SystemMonitor.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class MetricType.UserFunc.Stat_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("acl5EFI4BGH6JEFVEZWH6L2LSVQK4"),"MetricType.UserFunc.Stat",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEB4BPXE4OBD37MIZNXVK63IB3I"),

						org.radixware.kernel.common.enums.EClassType.APPLICATION,

						/*Radix::SystemMonitor::MetricType.UserFunc.Stat:Presentations-Entity Object Presentations*/
						/*Class presentation attributes*/
						 new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef(/*Owner Class Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("acl5EFI4BGH6JEFVEZWH6L2LSVQK4"),
							/*Owner Class Name*/
							"MetricType.UserFunc.Stat",
							/*Title Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEB4BPXE4OBD37MIZNXVK63IB3I"),
							/*Property presentations*/

							/*Radix::SystemMonitor::MetricType.UserFunc.Stat:Properties-Properties*/
							new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef[]{

									/*Radix::SystemMonitor::MetricType.UserFunc.Stat:calcStatValueFunction:PropertyPresentation-Object Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadParentTitlePropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pru6PA7ZCF6TVAM5HWW6FHC2T4J44"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,null,null,null,null,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr7IKMB24XZHOBDCMTAALOMT5GDM")},null,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr7IKMB24XZHOBDCMTAALOMT5GDM")},java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.TITLE,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT))
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
									/*Radix::SystemMonitor::MetricType.UserFunc.Stat:Edit-Editor Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("epr5FBR4QSZQZD3FBBGJ3ARKBXWLA"),
									"Edit",
									org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprSE464PGOZVAYPG5C7DZYJQ4P3E"),
									36016,
									null,

									/*Radix::SystemMonitor::MetricType.UserFunc.Stat:Edit:Children-Explorer Items*/
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
							org.radixware.kernel.common.utils.Maps.fromArrays(new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprSE464PGOZVAYPG5C7DZYJQ4P3E")},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr5FBR4QSZQZD3FBBGJ3ARKBXWLA")}),
							/*Object title format*/
							null,
							/*Class catalogs*/
							new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog[]{

									new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog(
									/*Radix::SystemMonitor::MetricType.UserFunc.Stat:Common-Dynamic Class Catalog*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eccXISAEUV6HRA7JNQ453IS2OBSFI"),org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.ELinkMode.VIRTUAL,new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.Item[]{
										new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.ClassRef(org.radixware.kernel.common.types.Id.Factory.loadFrom("acl5EFI4BGH6JEFVEZWH6L2LSVQK4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("cctDQ3TJ6OI6FGBZAA7VFXHRLRHIY"),130.0,true,org.radixware.kernel.common.types.Id.Factory.loadFrom("acl5EFI4BGH6JEFVEZWH6L2LSVQK4"),null)}
									)
							},
							/*Restrictions*/
							null,null,0),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl2H6SULJHXJGFVIS6UVHRWQS4AM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclGRHDCPLEO5DIDM2N4MFEDHESLM"),

						/*Radix::SystemMonitor::MetricType.UserFunc.Stat:Properties-Properties*/
						new org.radixware.kernel.server.meta.clazzes.RadPropDef[]{

								/*Radix::SystemMonitor::MetricType.UserFunc.Stat:calcStatValueFunction-User Property*/

								new org.radixware.kernel.server.meta.clazzes.RadUserRefPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pru6PA7ZCF6TVAM5HWW6FHC2T4J44"),"calcStatValueFunction",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6DEONCV2KREWNGOIFF43PBY5TY"),org.radixware.kernel.common.enums.EValType.OBJECT,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl2H6SULJHXJGFVIS6UVHRWQS4AM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aclZUPKE7H3JBCBPN7L6CCKBPB7ZA"),false,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE)
						},

						/*Radix::SystemMonitor::MetricType.UserFunc.Stat:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthSK64PGWTZNFZFAJVJ5NHXBAC6M"),"getKind",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthIT2CW4S27RB3XNEN22I7VDSY74"),"getValueType",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,org.radixware.kernel.common.enums.EValType.STR)
						},
						null,
						null,null,null,false);
}

/* Radix::SystemMonitor::MetricType.UserFunc.Stat - Desktop Executable*/

/*Radix::SystemMonitor::MetricType.UserFunc.Stat-Application Class*/

package org.radixware.ads.SystemMonitor.explorer;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType.UserFunc.Stat")
public interface MetricType.UserFunc.Stat   extends org.radixware.ads.SystemMonitor.explorer.MetricType.UserFunc  {























































	/*Radix::SystemMonitor::MetricType.UserFunc.Stat:calcStatValueFunction:calcStatValueFunction-Presentation Property*/


	public class CalcStatValueFunction extends org.radixware.kernel.common.client.models.items.properties.PropertyObject{
		public CalcStatValueFunction(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadParentRefPropertyDef def){
			super(model,def);
		}
		@Override
		public 
		org.radixware.ads.UserFunc.explorer.UserFunc.UserFunc_DefaultModel openEntityModel()  throws org.radixware.kernel.common.exceptions.ServiceClientException, InterruptedException{
		     return (org.radixware.ads.UserFunc.explorer.UserFunc.UserFunc_DefaultModel)super.openEntityModel();
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.client.types.Reference dummy = ((org.radixware.kernel.common.client.types.Reference)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType.UserFunc.Stat:calcStatValueFunction:calcStatValueFunction")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType.UserFunc.Stat:calcStatValueFunction:calcStatValueFunction")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public CalcStatValueFunction getCalcStatValueFunction();


}

/* Radix::SystemMonitor::MetricType.UserFunc.Stat - Desktop Meta*/

/*Radix::SystemMonitor::MetricType.UserFunc.Stat-Application Class*/

package org.radixware.ads.SystemMonitor.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class MetricType.UserFunc.Stat_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::SystemMonitor::MetricType.UserFunc.Stat:Presentations-Entity Object Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("acl5EFI4BGH6JEFVEZWH6L2LSVQK4"),
			"Radix::SystemMonitor::MetricType.UserFunc.Stat",
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aclGRHDCPLEO5DIDM2N4MFEDHESLM"),
			null,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEB4BPXE4OBD37MIZNXVK63IB3I"),null,null,0,

			/*Radix::SystemMonitor::MetricType.UserFunc.Stat:Properties-Properties*/
			new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

					/*Radix::SystemMonitor::MetricType.UserFunc.Stat:calcStatValueFunction:PropertyPresentation-Object Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pru6PA7ZCF6TVAM5HWW6FHC2T4J44"),
						"calcStatValueFunction",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acl5EFI4BGH6JEFVEZWH6L2LSVQK4"),
						org.radixware.kernel.common.enums.EValType.OBJECT,
						org.radixware.kernel.common.enums.EPropNature.USER,
						5,
						null,
						null,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						true,false,
						false,
						false,
						null,
						false,
						null,
						null,
						null,
						null,
						true,-1,-1,1,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclZUPKE7H3JBCBPN7L6CCKBPB7ZA"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblJ6SOXKD3ZHOBDCMTAALOMT5GDM"),
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr7IKMB24XZHOBDCMTAALOMT5GDM")},
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr7IKMB24XZHOBDCMTAALOMT5GDM")},
						null,
						0L,
						0L,false,false)
			},
			null,
			null,
			null,
			null,
			new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr5FBR4QSZQZD3FBBGJ3ARKBXWLA")},
			true,false,false);
}

/* Radix::SystemMonitor::MetricType.UserFunc.Stat - Web Meta*/

/*Radix::SystemMonitor::MetricType.UserFunc.Stat-Application Class*/

package org.radixware.ads.SystemMonitor.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class MetricType.UserFunc.Stat_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::SystemMonitor::MetricType.UserFunc.Stat:Presentations-Entity Object Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("acl5EFI4BGH6JEFVEZWH6L2LSVQK4"),
			"Radix::SystemMonitor::MetricType.UserFunc.Stat",
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aclGRHDCPLEO5DIDM2N4MFEDHESLM"),
			null,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEB4BPXE4OBD37MIZNXVK63IB3I"),null,null,0,
			null,
			null,
			null,
			null,
			null,
			null,
			false,false,false);
}

/* Radix::SystemMonitor::MetricType.UserFunc.Stat:Edit - Desktop Meta*/

/*Radix::SystemMonitor::MetricType.UserFunc.Stat:Edit-Editor Presentation*/

package org.radixware.ads.SystemMonitor.explorer;
public final class Edit_mi{
	private static final class Edit_DEF extends org.radixware.kernel.common.client.meta.RadEditorPresentationDef{
		Edit_DEF(){
			super(org.radixware.kernel.common.types.Id.Factory.loadFrom("epr5FBR4QSZQZD3FBBGJ3ARKBXWLA"),
			"Edit",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("eprSE464PGOZVAYPG5C7DZYJQ4P3E"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("acl5EFI4BGH6JEFVEZWH6L2LSVQK4"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl2H6SULJHXJGFVIS6UVHRWQS4AM"),
			null,
			null,

			/*Radix::SystemMonitor::MetricType.UserFunc.Stat:Edit:Editor Pages-Editor Presentation Pages*/
			new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

					/*Radix::SystemMonitor::MetricType.UserFunc.Stat:Edit:Additional-Editor Page*/

					 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgUASSP2BGKBHX7JZY3AEGJAVULA"),"Additional",org.radixware.kernel.common.types.Id.Factory.loadFrom("acl5EFI4BGH6JEFVEZWH6L2LSVQK4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBBMPQDJ4SNG7LAZQ5T6IL7WX4A"),null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("pru6PA7ZCF6TVAM5HWW6FHC2T4J44"),0,0,1,false,false)
					},null)
			},
			new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
				new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgBLOETUBE3RCINP7GTEZFWRN2KA")),
				new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgUASSP2BGKBHX7JZY3AEGJAVULA")),
				new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epg6O4DDW7XZRHEHF7JGHBYCEPBEQ"))}
			,

			/*Radix::SystemMonitor::MetricType.UserFunc.Stat:Edit:Children-Explorer Items*/
				null
			,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
			null,
			0,
			null,
			(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
			36016,0,0);
		}
		@Override
		protected org.radixware.kernel.common.client.models.Model createModelImpl(org.radixware.kernel.common.client.IClientEnvironment userSession) {    return new org.radixware.ads.SystemMonitor.explorer.MetricType.UserFunc.Stat.MetricType.UserFunc.Stat_DefaultModel.eprSE464PGOZVAYPG5C7DZYJQ4P3E_ModelAdapter(userSession,this);
		}
	}
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new Edit_DEF(); 
;
}
/* Radix::SystemMonitor::MetricType.UserFunc.Stat - Localizing Bundle */
package org.radixware.ads.SystemMonitor.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class MetricType.UserFunc.Stat - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Дополнительно");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Additional");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBBMPQDJ4SNG7LAZQ5T6IL7WX4A"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"/home/ayanichkin/ssd/tranzaxis/radix/org.radixware/ads/SystemMonitor"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Пользовательская метрика - статистическая функция");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"User-Defined Metric - Statistic Function");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEB4BPXE4OBD37MIZNXVK63IB3I"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"/home/ayanichkin/ssd/tranzaxis/radix/org.radixware/ads/SystemMonitor"));
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(MetricType.UserFunc.Stat - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbacl5EFI4BGH6JEFVEZWH6L2LSVQK4"),"MetricType.UserFunc.Stat - Localizing Bundle",$$$items$$$);
}
