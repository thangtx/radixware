
/* Radix::SystemMonitor::MetricType.UserFunc.Point - Server Executable*/

/*Radix::SystemMonitor::MetricType.UserFunc.Point-Application Class*/

package org.radixware.ads.SystemMonitor.server;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType.UserFunc.Point")
public published class MetricType.UserFunc.Point  extends org.radixware.ads.SystemMonitor.server.MetricType.UserFunc  implements org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {


	/**Metainformation accessor method*/
	@SuppressWarnings("unused")
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return MetricType.UserFunc.Point_mi.rdxMeta;}

	/*Radix::SystemMonitor::MetricType.UserFunc.Point:Nested classes-Nested Classes*/

	/*Radix::SystemMonitor::MetricType.UserFunc.Point:Properties-Properties*/

	/*Radix::SystemMonitor::MetricType.UserFunc.Point:calcEventValueFunction-User Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType.UserFunc.Point:calcEventValueFunction")
	public published  org.radixware.ads.SystemMonitor.server.UserFunc.CalcPointUserMetric getCalcEventValueFunction() {
		return calcEventValueFunction;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType.UserFunc.Point:calcEventValueFunction")
	public published   void setCalcEventValueFunction(org.radixware.ads.SystemMonitor.server.UserFunc.CalcPointUserMetric val) {
		calcEventValueFunction = val;
	}



































	/*Radix::SystemMonitor::MetricType.UserFunc.Point:Methods-Methods*/

	/*Radix::SystemMonitor::MetricType.UserFunc.Point:getKind-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType.UserFunc.Point:getKind")
	public published  org.radixware.ads.SystemMonitor.common.MetricKind getKind () {
		return MetricKind:User.Func.Point;
	}

	/*Radix::SystemMonitor::MetricType.UserFunc.Point:getValueType-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType.UserFunc.Point:getValueType")
	protected published  org.radixware.ads.SystemMonitor.common.MetricValueType getValueType () {
		return MetricValueType:Point;
	}


	@Override
	public org.radixware.kernel.server.types.FormHandler.NextDialogsRequest execCommand(org.radixware.kernel.common.types.Id cmdId, org.radixware.kernel.common.types.Id propId, org.apache.xmlbeans.XmlObject input, org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValsById, org.apache.xmlbeans.XmlObject output) throws org.radixware.kernel.common.exceptions.AppException,java.lang.InterruptedException{

			return super.execCommand(cmdId,propId,input,newPropValsById,output);
	}


}

/* Radix::SystemMonitor::MetricType.UserFunc.Point - Server Meta*/

/*Radix::SystemMonitor::MetricType.UserFunc.Point-Application Class*/

package org.radixware.ads.SystemMonitor.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class MetricType.UserFunc.Point_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("aclZFOH5AAO5ZCRVDLVJJGQEPXTMU"),"MetricType.UserFunc.Point",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNIYK2JFHIBF6FEIKIG4LSGFXEE"),

						org.radixware.kernel.common.enums.EClassType.APPLICATION,

						/*Radix::SystemMonitor::MetricType.UserFunc.Point:Presentations-Entity Object Presentations*/
						/*Class presentation attributes*/
						 new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef(/*Owner Class Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("aclZFOH5AAO5ZCRVDLVJJGQEPXTMU"),
							/*Owner Class Name*/
							"MetricType.UserFunc.Point",
							/*Title Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNIYK2JFHIBF6FEIKIG4LSGFXEE"),
							/*Property presentations*/

							/*Radix::SystemMonitor::MetricType.UserFunc.Point:Properties-Properties*/
							new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef[]{

									/*Radix::SystemMonitor::MetricType.UserFunc.Point:calcEventValueFunction:PropertyPresentation-Object Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadParentTitlePropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruLQHRIQUNLZC2DJXETRM5QKOWMY"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,null,null,null,null,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr7IKMB24XZHOBDCMTAALOMT5GDM")},null,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr7IKMB24XZHOBDCMTAALOMT5GDM")},java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.TITLE,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT))
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
									/*Radix::SystemMonitor::MetricType.UserFunc.Point:Edit-Editor Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprIQXM6IGQ7FD65BRPFWPC2JYUWI"),
									"Edit",
									org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprSE464PGOZVAYPG5C7DZYJQ4P3E"),
									36016,
									null,

									/*Radix::SystemMonitor::MetricType.UserFunc.Point:Edit:Children-Explorer Items*/
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
							org.radixware.kernel.common.utils.Maps.fromArrays(new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprSE464PGOZVAYPG5C7DZYJQ4P3E")},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprIQXM6IGQ7FD65BRPFWPC2JYUWI")}),
							/*Object title format*/
							null,
							/*Class catalogs*/
							new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog[]{

									new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog(
									/*Radix::SystemMonitor::MetricType.UserFunc.Point:Common-Dynamic Class Catalog*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eccXISAEUV6HRA7JNQ453IS2OBSFI"),org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.ELinkMode.VIRTUAL,new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.Item[]{
										new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.ClassRef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aclZFOH5AAO5ZCRVDLVJJGQEPXTMU"),org.radixware.kernel.common.types.Id.Factory.loadFrom("cctDQ3TJ6OI6FGBZAA7VFXHRLRHIY"),120.0,true,org.radixware.kernel.common.types.Id.Factory.loadFrom("aclZFOH5AAO5ZCRVDLVJJGQEPXTMU"),null)}
									)
							},
							/*Restrictions*/
							null,null,0),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl2H6SULJHXJGFVIS6UVHRWQS4AM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclGRHDCPLEO5DIDM2N4MFEDHESLM"),

						/*Radix::SystemMonitor::MetricType.UserFunc.Point:Properties-Properties*/
						new org.radixware.kernel.server.meta.clazzes.RadPropDef[]{

								/*Radix::SystemMonitor::MetricType.UserFunc.Point:calcEventValueFunction-User Property*/

								new org.radixware.kernel.server.meta.clazzes.RadUserRefPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruLQHRIQUNLZC2DJXETRM5QKOWMY"),"calcEventValueFunction",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsY7APHU6RTVGILIB2D24AP657BY"),org.radixware.kernel.common.enums.EValType.OBJECT,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl2H6SULJHXJGFVIS6UVHRWQS4AM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aclAPQMCEE7LJD5BJR2BJQWYGKJNE"),false,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE)
						},

						/*Radix::SystemMonitor::MetricType.UserFunc.Point:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthSK64PGWTZNFZFAJVJ5NHXBAC6M"),"getKind",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthIT2CW4S27RB3XNEN22I7VDSY74"),"getValueType",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,org.radixware.kernel.common.enums.EValType.STR)
						},
						null,
						null,null,null,false);
}

/* Radix::SystemMonitor::MetricType.UserFunc.Point - Desktop Executable*/

/*Radix::SystemMonitor::MetricType.UserFunc.Point-Application Class*/

package org.radixware.ads.SystemMonitor.explorer;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType.UserFunc.Point")
public interface MetricType.UserFunc.Point   extends org.radixware.ads.SystemMonitor.explorer.MetricType.UserFunc  {























































	/*Radix::SystemMonitor::MetricType.UserFunc.Point:calcEventValueFunction:calcEventValueFunction-Presentation Property*/


	public class CalcEventValueFunction extends org.radixware.kernel.common.client.models.items.properties.PropertyObject{
		public CalcEventValueFunction(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadParentRefPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType.UserFunc.Point:calcEventValueFunction:calcEventValueFunction")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType.UserFunc.Point:calcEventValueFunction:calcEventValueFunction")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public CalcEventValueFunction getCalcEventValueFunction();


}

/* Radix::SystemMonitor::MetricType.UserFunc.Point - Desktop Meta*/

/*Radix::SystemMonitor::MetricType.UserFunc.Point-Application Class*/

package org.radixware.ads.SystemMonitor.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class MetricType.UserFunc.Point_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::SystemMonitor::MetricType.UserFunc.Point:Presentations-Entity Object Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aclZFOH5AAO5ZCRVDLVJJGQEPXTMU"),
			"Radix::SystemMonitor::MetricType.UserFunc.Point",
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aclGRHDCPLEO5DIDM2N4MFEDHESLM"),
			null,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNIYK2JFHIBF6FEIKIG4LSGFXEE"),null,null,0,

			/*Radix::SystemMonitor::MetricType.UserFunc.Point:Properties-Properties*/
			new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

					/*Radix::SystemMonitor::MetricType.UserFunc.Point:calcEventValueFunction:PropertyPresentation-Object Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruLQHRIQUNLZC2DJXETRM5QKOWMY"),
						"calcEventValueFunction",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclZFOH5AAO5ZCRVDLVJJGQEPXTMU"),
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
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclAPQMCEE7LJD5BJR2BJQWYGKJNE"),
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
			new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprIQXM6IGQ7FD65BRPFWPC2JYUWI")},
			true,false,false);
}

/* Radix::SystemMonitor::MetricType.UserFunc.Point - Web Meta*/

/*Radix::SystemMonitor::MetricType.UserFunc.Point-Application Class*/

package org.radixware.ads.SystemMonitor.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class MetricType.UserFunc.Point_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::SystemMonitor::MetricType.UserFunc.Point:Presentations-Entity Object Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aclZFOH5AAO5ZCRVDLVJJGQEPXTMU"),
			"Radix::SystemMonitor::MetricType.UserFunc.Point",
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aclGRHDCPLEO5DIDM2N4MFEDHESLM"),
			null,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNIYK2JFHIBF6FEIKIG4LSGFXEE"),null,null,0,
			null,
			null,
			null,
			null,
			null,
			null,
			false,false,false);
}

/* Radix::SystemMonitor::MetricType.UserFunc.Point:Edit - Desktop Meta*/

/*Radix::SystemMonitor::MetricType.UserFunc.Point:Edit-Editor Presentation*/

package org.radixware.ads.SystemMonitor.explorer;
public final class Edit_mi{
	private static final class Edit_DEF extends org.radixware.kernel.common.client.meta.RadEditorPresentationDef{
		Edit_DEF(){
			super(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprIQXM6IGQ7FD65BRPFWPC2JYUWI"),
			"Edit",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("eprSE464PGOZVAYPG5C7DZYJQ4P3E"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aclZFOH5AAO5ZCRVDLVJJGQEPXTMU"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl2H6SULJHXJGFVIS6UVHRWQS4AM"),
			null,
			null,

			/*Radix::SystemMonitor::MetricType.UserFunc.Point:Edit:Editor Pages-Editor Presentation Pages*/
			new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

					/*Radix::SystemMonitor::MetricType.UserFunc.Point:Edit:Additional-Editor Page*/

					 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgMM3JTUXA7BHAJBEBXRINI6UQFE"),"Additional",org.radixware.kernel.common.types.Id.Factory.loadFrom("aclZFOH5AAO5ZCRVDLVJJGQEPXTMU"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYTXVOHK2LRCP5IOXA45EETGV2A"),null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruLQHRIQUNLZC2DJXETRM5QKOWMY"),0,0,1,false,false)
					},null)
			},
			new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
				new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgBLOETUBE3RCINP7GTEZFWRN2KA")),
				new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgMM3JTUXA7BHAJBEBXRINI6UQFE")),
				new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epg6O4DDW7XZRHEHF7JGHBYCEPBEQ"))}
			,

			/*Radix::SystemMonitor::MetricType.UserFunc.Point:Edit:Children-Explorer Items*/
				null
			,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
			null,
			0,
			null,
			(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
			36016,0,0);
		}
		@Override
		protected org.radixware.kernel.common.client.models.Model createModelImpl(org.radixware.kernel.common.client.IClientEnvironment userSession) {    return new org.radixware.ads.SystemMonitor.explorer.MetricType.UserFunc.Point.MetricType.UserFunc.Point_DefaultModel.eprSE464PGOZVAYPG5C7DZYJQ4P3E_ModelAdapter(userSession,this);
		}
	}
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new Edit_DEF(); 
;
}
/* Radix::SystemMonitor::MetricType.UserFunc.Point - Localizing Bundle */
package org.radixware.ads.SystemMonitor.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class MetricType.UserFunc.Point - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Пользовательская метрика - точечная функция");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"User-Defined Metric - Point Function");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNIYK2JFHIBF6FEIKIG4LSGFXEE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"/home/ayanichkin/ssd/tranzaxis/radix/org.radixware/ads/SystemMonitor"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Дополнительно");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Additional");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYTXVOHK2LRCP5IOXA45EETGV2A"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"/home/ayanichkin/ssd/tranzaxis/radix/org.radixware/ads/SystemMonitor"));
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(MetricType.UserFunc.Point - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbaclZFOH5AAO5ZCRVDLVJJGQEPXTMU"),"MetricType.UserFunc.Point - Localizing Bundle",$$$items$$$);
}
