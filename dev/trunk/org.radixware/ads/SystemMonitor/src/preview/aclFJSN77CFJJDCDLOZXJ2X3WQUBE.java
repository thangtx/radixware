
/* Radix::SystemMonitor::MetricType.Unit - Server Executable*/

/*Radix::SystemMonitor::MetricType.Unit-Application Class*/

package org.radixware.ads.SystemMonitor.server;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType.Unit")
public abstract published class MetricType.Unit  extends org.radixware.ads.SystemMonitor.server.MetricType  implements org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {


	/**Metainformation accessor method*/
	@SuppressWarnings("unused")
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return MetricType.Unit_mi.rdxMeta;}

	/*Radix::SystemMonitor::MetricType.Unit:Nested classes-Nested Classes*/

	/*Radix::SystemMonitor::MetricType.Unit:Properties-Properties*/

	/*Radix::SystemMonitor::MetricType.Unit:unitId-Column-Based Property*/




	/*Radix::SystemMonitor::MetricType.Unit:unit-Parent Reference*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType.Unit:unit")
	public published  org.radixware.ads.System.server.Unit getUnit() {
		return unit;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType.Unit:unit")
	public published   void setUnit(org.radixware.ads.System.server.Unit val) {
		unit = val;
	}









































	/*Radix::SystemMonitor::MetricType.Unit:Methods-Methods*/

	/*Radix::SystemMonitor::MetricType.Unit:getSensorType-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType.Unit:getSensorType")
	public published  org.radixware.kernel.common.enums.ESensorType getSensorType () {
		return SensorType:UNIT;
	}


	@Override
	public org.radixware.kernel.server.types.FormHandler.NextDialogsRequest execCommand(org.radixware.kernel.common.types.Id cmdId, org.radixware.kernel.common.types.Id propId, org.apache.xmlbeans.XmlObject input, org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValsById, org.apache.xmlbeans.XmlObject output) throws org.radixware.kernel.common.exceptions.AppException,java.lang.InterruptedException{

			return super.execCommand(cmdId,propId,input,newPropValsById,output);
	}


}

/* Radix::SystemMonitor::MetricType.Unit - Server Meta*/

/*Radix::SystemMonitor::MetricType.Unit-Application Class*/

package org.radixware.ads.SystemMonitor.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class MetricType.Unit_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("aclFJSN77CFJJDCDLOZXJ2X3WQUBE"),"MetricType.Unit",null,

						org.radixware.kernel.common.enums.EClassType.APPLICATION,

						/*Radix::SystemMonitor::MetricType.Unit:Presentations-Entity Object Presentations*/
						/*Class presentation attributes*/
						 new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef(/*Owner Class Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("aclFJSN77CFJJDCDLOZXJ2X3WQUBE"),
							/*Owner Class Name*/
							"MetricType.Unit",
							/*Title Id*/
							null,
							/*Property presentations*/

							/*Radix::SystemMonitor::MetricType.Unit:Properties-Properties*/
							new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef[]{

									/*Radix::SystemMonitor::MetricType.Unit:unitId:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colFYLCZQEXTJG5JGNSHCWLJTQUSU"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::SystemMonitor::MetricType.Unit:unit:PropertyPresentation-Parent Ref Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadParentTitlePropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGSFAITPT5JEYNOOD7WASUJ3LOU"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,null,null,new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),org.radixware.kernel.server.types.Restrictions.Factory.newInstance(133693439,null,null,null),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprXCFCDDGTRHNRDB5MAALOMT5GDM")},null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_SELECTOR))
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
									/*Radix::SystemMonitor::MetricType.Unit:Edit-Editor Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprQZBCBFUUOZHCJL2XJ2L3PATCRM"),
									"Edit",
									org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprSE464PGOZVAYPG5C7DZYJQ4P3E"),
									36016,
									null,

									/*Radix::SystemMonitor::MetricType.Unit:Edit:Children-Explorer Items*/
										null
									,
									null,
									(org.radixware.kernel.server.meta.presentations.RadPropertyPresentationAttributes[]) null,
									org.radixware.kernel.common.enums.EEditorPresentationRightsInheritanceMode.FROM_REPLACED,
									null),

									new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef(
									/*Radix::SystemMonitor::MetricType.Unit:Create-Editor Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprXWL3KL7FWBECBBN7RJIHT3Y35U"),
									"Create",
									org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprQZBCBFUUOZHCJL2XJ2L3PATCRM"),
									36016,
									null,

									/*Radix::SystemMonitor::MetricType.Unit:Create:Children-Explorer Items*/
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
							org.radixware.kernel.common.utils.Maps.fromArrays(new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprSE464PGOZVAYPG5C7DZYJQ4P3E"),org.radixware.kernel.common.types.Id.Factory.loadFrom("eprRG25UU3GFJAXXMVK7ZE4GQBL4I")},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprQZBCBFUUOZHCJL2XJ2L3PATCRM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("eprXWL3KL7FWBECBBN7RJIHT3Y35U")}),
							/*Object title format*/
							null,
							/*Class catalogs*/
							new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog[]{

									new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog(
									/*Radix::SystemMonitor::MetricType.Unit:Common-Dynamic Class Catalog*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eccXISAEUV6HRA7JNQ453IS2OBSFI"),org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.ELinkMode.VIRTUAL,new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.Item[]{
										new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.Topic(org.radixware.kernel.common.types.Id.Factory.loadFrom("cctTNNO3THJ4BEYTKUDKXWCKXRAP4"),null,190.0,org.radixware.kernel.common.types.Id.Factory.loadFrom("aclFJSN77CFJJDCDLOZXJ2X3WQUBE"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZKZUV72EWBDKNPU5BXRSUIBIQA"))}
									)
							},
							/*Restrictions*/
							null,null,0),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl2H6SULJHXJGFVIS6UVHRWQS4AM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec2H6SULJHXJGFVIS6UVHRWQS4AM"),

						/*Radix::SystemMonitor::MetricType.Unit:Properties-Properties*/
						new org.radixware.kernel.server.meta.clazzes.RadPropDef[]{

								/*Radix::SystemMonitor::MetricType.Unit:unitId-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colFYLCZQEXTJG5JGNSHCWLJTQUSU"),"unitId",null,org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::SystemMonitor::MetricType.Unit:unit-Parent Reference*/

								new org.radixware.kernel.server.meta.clazzes.RadInnateRefPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGSFAITPT5JEYNOOD7WASUJ3LOU"),"unit",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEJKBPK6MXFHTZBI6UEO7EXYKSE"),org.radixware.kernel.common.types.Id.Factory.loadFrom("refBRBSH532FRC3BOQAMSVBUJQA74"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE)
						},

						/*Radix::SystemMonitor::MetricType.Unit:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth7M6YK3J5OREYTFHKMRDGE42Z3Q"),"getSensorType",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.INT)
						},
						null,
						null,null,null,false);
}

/* Radix::SystemMonitor::MetricType.Unit - Desktop Executable*/

/*Radix::SystemMonitor::MetricType.Unit-Application Class*/

package org.radixware.ads.SystemMonitor.explorer;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType.Unit")
public interface MetricType.Unit   extends org.radixware.ads.SystemMonitor.explorer.MetricType  {























































	/*Radix::SystemMonitor::MetricType.Unit:unit:unit-Presentation Property*/


	public class Unit extends org.radixware.kernel.common.client.models.items.properties.PropertyRef{
		public Unit(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadParentRefPropertyDef def){
			super(model,def);
		}
		@Override
		public 
		org.radixware.ads.System.explorer.Unit.Unit_DefaultModel openEntityModel()  throws org.radixware.kernel.common.exceptions.ServiceClientException, InterruptedException{
		     return (org.radixware.ads.System.explorer.Unit.Unit_DefaultModel)super.openEntityModel();
		}
		@Override
		public 
		org.radixware.ads.System.explorer.Unit.DefaultGroupModel openGroupModel(){
		     return (org.radixware.ads.System.explorer.Unit.DefaultGroupModel)super.openGroupModel();
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.client.types.Reference dummy = ((org.radixware.kernel.common.client.types.Reference)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType.Unit:unit:unit")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType.Unit:unit:unit")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public Unit getUnit();


}

/* Radix::SystemMonitor::MetricType.Unit - Desktop Meta*/

/*Radix::SystemMonitor::MetricType.Unit-Application Class*/

package org.radixware.ads.SystemMonitor.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class MetricType.Unit_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::SystemMonitor::MetricType.Unit:Presentations-Entity Object Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aclFJSN77CFJJDCDLOZXJ2X3WQUBE"),
			"Radix::SystemMonitor::MetricType.Unit",
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aec2H6SULJHXJGFVIS6UVHRWQS4AM"),
			null,
			null,
			null,null,null,0,

			/*Radix::SystemMonitor::MetricType.Unit:Properties-Properties*/
			new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

					/*Radix::SystemMonitor::MetricType.Unit:unitId:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colFYLCZQEXTJG5JGNSHCWLJTQUSU"),
						"unitId",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclFJSN77CFJJDCDLOZXJ2X3WQUBE"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
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

						/*Radix::SystemMonitor::MetricType.Unit:unitId:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::SystemMonitor::MetricType.Unit:unit:PropertyPresentation-Parent Ref Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGSFAITPT5JEYNOOD7WASUJ3LOU"),
						"unit",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEJKBPK6MXFHTZBI6UEO7EXYKSE"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclFJSN77CFJJDCDLOZXJ2X3WQUBE"),
						org.radixware.kernel.common.enums.EValType.PARENT_REF,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						20,
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
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls43QMEDG2SVAY5MEQ6XPTEKCYTM"),
						null,
						null,
						true,-1,-1,1,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl5HP4XTP3EGWDBRCRAAIT4AGD7E"),
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprXCFCDDGTRHNRDB5MAALOMT5GDM")},
						null,
						null,
						133693439,
						133693439,false)
			},
			null,
			null,
			null,
			null,
			new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprQZBCBFUUOZHCJL2XJ2L3PATCRM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("eprXWL3KL7FWBECBBN7RJIHT3Y35U")},
			true,false,false);
}

/* Radix::SystemMonitor::MetricType.Unit - Web Meta*/

/*Radix::SystemMonitor::MetricType.Unit-Application Class*/

package org.radixware.ads.SystemMonitor.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class MetricType.Unit_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::SystemMonitor::MetricType.Unit:Presentations-Entity Object Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aclFJSN77CFJJDCDLOZXJ2X3WQUBE"),
			"Radix::SystemMonitor::MetricType.Unit",
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aec2H6SULJHXJGFVIS6UVHRWQS4AM"),
			null,
			null,
			null,null,null,0,
			null,
			null,
			null,
			null,
			null,
			null,
			false,false,false);
}

/* Radix::SystemMonitor::MetricType.Unit:Edit - Desktop Meta*/

/*Radix::SystemMonitor::MetricType.Unit:Edit-Editor Presentation*/

package org.radixware.ads.SystemMonitor.explorer;
public final class Edit_mi{
	private static final class Edit_DEF extends org.radixware.kernel.common.client.meta.RadEditorPresentationDef{
		Edit_DEF(){
			super(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprQZBCBFUUOZHCJL2XJ2L3PATCRM"),
			"Edit",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("eprSE464PGOZVAYPG5C7DZYJQ4P3E"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aclFJSN77CFJJDCDLOZXJ2X3WQUBE"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl2H6SULJHXJGFVIS6UVHRWQS4AM"),
			null,
			null,

			/*Radix::SystemMonitor::MetricType.Unit:Edit:Editor Pages-Editor Presentation Pages*/
			new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

					/*Radix::SystemMonitor::MetricType.Unit:Edit:Parameters-Editor Page*/

					 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgBLOETUBE3RCINP7GTEZFWRN2KA"),"Parameters",org.radixware.kernel.common.types.Id.Factory.loadFrom("aclFJSN77CFJJDCDLOZXJ2X3WQUBE"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRNFYGY3ZWBANNI7EYZXIV4OWTQ"),null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKK3G7VOVNBESRPNV7SEYWFKLDQ"),0,2,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colP5GRYC4WIRFDVCOV3AYNBAPYY4"),0,6,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXHO3TMONUZFWVLGCBFATASRCZ4"),0,7,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colSYQ72HEL2RD3VFYFLWVG6GFZEA"),0,8,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col2RJUTHZLBFGDPHT5NR6S6APSYM"),0,9,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colI5HMBXQKAZBVTJSJ5YVNSGCR5Y"),0,10,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNWD3JCD37ZDAVKKCLTUEPMO3TQ"),0,11,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col4YKCVXTHZVETTGSLJZFJB6PPEY"),0,12,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col773ODXVAM5DIZIORVOBGIBYK7Y"),0,4,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colUH6LIE6A55C5BEX56Y7GPPZMHQ"),0,15,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNS4TO66OSNH3DCZS64EDNL7VDU"),0,0,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQKG6VKSKMBFG7G2WMCOW2DCD2A"),0,5,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdJHAQ5ULMWBAVTHGBDBL34ZHGME"),0,1,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGSFAITPT5JEYNOOD7WASUJ3LOU"),0,3,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJERRR2BH2ZD2XGDYMYUS67KOKY"),0,14,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPSRIBABNSJD45B3FB3UDKHACIM"),0,13,1,false,false)
					},null)
			},
			new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
				new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgBLOETUBE3RCINP7GTEZFWRN2KA")),
				new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epg6O4DDW7XZRHEHF7JGHBYCEPBEQ"))}
			,

			/*Radix::SystemMonitor::MetricType.Unit:Edit:Children-Explorer Items*/
				null
			,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
			null,
			0,
			null,
			(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
			36016,0,0);
		}
		@Override
		protected org.radixware.kernel.common.client.models.Model createModelImpl(org.radixware.kernel.common.client.IClientEnvironment userSession) {    return new org.radixware.ads.SystemMonitor.explorer.MetricType.Unit.MetricType.Unit_DefaultModel.eprSE464PGOZVAYPG5C7DZYJQ4P3E_ModelAdapter(userSession,this);
		}
	}
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new Edit_DEF(); 
;
}
/* Radix::SystemMonitor::MetricType.Unit:Create - Desktop Meta*/

/*Radix::SystemMonitor::MetricType.Unit:Create-Editor Presentation*/

package org.radixware.ads.SystemMonitor.explorer;
public final class Create_mi{
	private static final class Create_DEF extends org.radixware.kernel.common.client.meta.RadEditorPresentationDef{
		Create_DEF(){
			super(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprXWL3KL7FWBECBBN7RJIHT3Y35U"),
			"Create",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("eprQZBCBFUUOZHCJL2XJ2L3PATCRM"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aclFJSN77CFJJDCDLOZXJ2X3WQUBE"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl2H6SULJHXJGFVIS6UVHRWQS4AM"),
			null,
			null,

			/*Radix::SystemMonitor::MetricType.Unit:Create:Editor Pages-Editor Presentation Pages*/
			null,
			new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
				new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgBLOETUBE3RCINP7GTEZFWRN2KA"))}
			,

			/*Radix::SystemMonitor::MetricType.Unit:Create:Children-Explorer Items*/
				null
			,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
			null,
			0,
			null,
			(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
			36016,0,0);
		}
		@Override
		protected org.radixware.kernel.common.client.models.Model createModelImpl(org.radixware.kernel.common.client.IClientEnvironment userSession) {    return new org.radixware.ads.SystemMonitor.explorer.Edit:Model(userSession,this);
		}
	}
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new Create_DEF(); 
;
}
/* Radix::SystemMonitor::MetricType.Unit - Localizing Bundle */
package org.radixware.ads.SystemMonitor.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class MetricType.Unit - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<все>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<all>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls43QMEDG2SVAY5MEQ6XPTEKCYTM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"/home/ayanichkin/ssd/tranzaxis/radix/org.radixware/ads/SystemMonitor"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Модуль системы");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"System unit");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEJKBPK6MXFHTZBI6UEO7EXYKSE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"/home/ayanichkin/ssd/tranzaxis/radix/org.radixware/ads/SystemMonitor"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Параметры");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Parameters");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRNFYGY3ZWBANNI7EYZXIV4OWTQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"/home/ayanichkin/ssd/tranzaxis/radix/org.radixware/ads/SystemMonitor"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Units");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Модули");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZKZUV72EWBDKNPU5BXRSUIBIQA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"/home/ayanichkin/ssd/tranzaxis/radix/org.radixware/ads/SystemMonitor"));
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(MetricType.Unit - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbaclFJSN77CFJJDCDLOZXJ2X3WQUBE"),"MetricType.Unit - Localizing Bundle",$$$items$$$);
}
