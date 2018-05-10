
/* Radix::SystemMonitor::MetricType.NetChannel - Server Executable*/

/*Radix::SystemMonitor::MetricType.NetChannel-Application Class*/

package org.radixware.ads.SystemMonitor.server;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType.NetChannel")
public abstract class MetricType.NetChannel  extends org.radixware.ads.SystemMonitor.server.MetricType  implements org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {


	/**Metainformation accessor method*/
	@SuppressWarnings("unused")
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return MetricType.NetChannel_mi.rdxMeta;}

	/*Radix::SystemMonitor::MetricType.NetChannel:Nested classes-Nested Classes*/

	/*Radix::SystemMonitor::MetricType.NetChannel:Properties-Properties*/

	/*Radix::SystemMonitor::MetricType.NetChannel:netChannel-Parent Reference*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType.NetChannel:netChannel")
	public  org.radixware.ads.Net.server.NetChannel getNetChannel() {
		return netChannel;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType.NetChannel:netChannel")
	public   void setNetChannel(org.radixware.ads.Net.server.NetChannel val) {
		netChannel = val;
	}

	/*Radix::SystemMonitor::MetricType.NetChannel:netListenerId-Column-Based Property*/












































	/*Radix::SystemMonitor::MetricType.NetChannel:Methods-Methods*/


	@Override
	public org.radixware.kernel.server.types.FormHandler.NextDialogsRequest execCommand(org.radixware.kernel.common.types.Id cmdId, org.radixware.kernel.common.types.Id propId, org.apache.xmlbeans.XmlObject input, org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValsById, org.apache.xmlbeans.XmlObject output) throws org.radixware.kernel.common.exceptions.AppException,java.lang.InterruptedException{

			return super.execCommand(cmdId,propId,input,newPropValsById,output);
	}


}

/* Radix::SystemMonitor::MetricType.NetChannel - Server Meta*/

/*Radix::SystemMonitor::MetricType.NetChannel-Application Class*/

package org.radixware.ads.SystemMonitor.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class MetricType.NetChannel_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("acl5V5QCN7ZP5DPFHWTMHEGX6HCXM"),"MetricType.NetChannel",null,

						org.radixware.kernel.common.enums.EClassType.APPLICATION,

						/*Radix::SystemMonitor::MetricType.NetChannel:Presentations-Entity Object Presentations*/
						/*Class presentation attributes*/
						 new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef(/*Owner Class Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("acl5V5QCN7ZP5DPFHWTMHEGX6HCXM"),
							/*Owner Class Name*/
							"MetricType.NetChannel",
							/*Title Id*/
							null,
							/*Property presentations*/

							/*Radix::SystemMonitor::MetricType.NetChannel:Properties-Properties*/
							new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef[]{

									/*Radix::SystemMonitor::MetricType.NetChannel:netChannel:PropertyPresentation-Parent Ref Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadParentTitlePropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKGZDKSPHXFHBHPGRL3JCWVGXSQ"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,null,null,new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),org.radixware.kernel.server.types.Restrictions.Factory.newInstance(133693439,null,null,null),null,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.TITLE,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_SELECTOR)),

									/*Radix::SystemMonitor::MetricType.NetChannel:netListenerId:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMVLLX5SQNVDBRMQ2ZYOQ6WDVH4"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.TITLE,org.radixware.kernel.common.enums.EPropAttrInheritance.EDITING,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_SELECT_CONDITION,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_SELECTOR,org.radixware.kernel.common.enums.EPropAttrInheritance.HINT))
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
									/*Radix::SystemMonitor::MetricType.NetChannel:Edit-Editor Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("epr5NYQFD3TZ5ATJL65ZK542GMR54"),
									"Edit",
									org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprSE464PGOZVAYPG5C7DZYJQ4P3E"),
									36016,
									null,

									/*Radix::SystemMonitor::MetricType.NetChannel:Edit:Children-Explorer Items*/
										null
									,
									null,
									(org.radixware.kernel.server.meta.presentations.RadPropertyPresentationAttributes[]) null,
									org.radixware.kernel.common.enums.EEditorPresentationRightsInheritanceMode.FROM_REPLACED,
									null),

									new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef(
									/*Radix::SystemMonitor::MetricType.NetChannel:Create-Editor Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("epr5QI7VNTATZGLBB3SADTLS3A56A"),
									"Create",
									org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
									org.radixware.kernel.common.types.Id.Factory.loadFrom("epr5NYQFD3TZ5ATJL65ZK542GMR54"),
									36016,
									null,

									/*Radix::SystemMonitor::MetricType.NetChannel:Create:Children-Explorer Items*/
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
							org.radixware.kernel.common.utils.Maps.fromArrays(new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprSE464PGOZVAYPG5C7DZYJQ4P3E"),org.radixware.kernel.common.types.Id.Factory.loadFrom("eprRG25UU3GFJAXXMVK7ZE4GQBL4I")},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr5NYQFD3TZ5ATJL65ZK542GMR54"),org.radixware.kernel.common.types.Id.Factory.loadFrom("epr5QI7VNTATZGLBB3SADTLS3A56A")}),
							/*Object title format*/
							null,
							/*Class catalogs*/
							null,
							/*Restrictions*/
							null,null,0),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl2H6SULJHXJGFVIS6UVHRWQS4AM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec2H6SULJHXJGFVIS6UVHRWQS4AM"),

						/*Radix::SystemMonitor::MetricType.NetChannel:Properties-Properties*/
						new org.radixware.kernel.server.meta.clazzes.RadPropDef[]{

								/*Radix::SystemMonitor::MetricType.NetChannel:netChannel-Parent Reference*/

								new org.radixware.kernel.server.meta.clazzes.RadInnateRefPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKGZDKSPHXFHBHPGRL3JCWVGXSQ"),"netChannel",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7TFCKOPWVXOBDCLVAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("ref2RXWMT2TDVERPBYVVIWPKUQGHY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHBE24OUE2DNRDB7AAALOMT5GDM"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::SystemMonitor::MetricType.NetChannel:netListenerId-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMVLLX5SQNVDBRMQ2ZYOQ6WDVH4"),"netListenerId",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsIJZ3VS3N3NHVHMFWWIG3DYBQYA"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE)
						},

						/*Radix::SystemMonitor::MetricType.NetChannel:Methods-Methods*/
						null,
						null,
						null,null,null,false);
}

/* Radix::SystemMonitor::MetricType.NetChannel - Desktop Executable*/

/*Radix::SystemMonitor::MetricType.NetChannel-Application Class*/

package org.radixware.ads.SystemMonitor.explorer;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType.NetChannel")
public interface MetricType.NetChannel   extends org.radixware.ads.SystemMonitor.explorer.MetricType  {























































	/*Radix::SystemMonitor::MetricType.NetChannel:netChannel:netChannel-Presentation Property*/


	public class NetChannel extends org.radixware.kernel.common.client.models.items.properties.PropertyRef{
		public NetChannel(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadParentRefPropertyDef def){
			super(model,def);
		}
		@Override
		public 
		org.radixware.ads.Net.explorer.NetChannel.NetChannel_DefaultModel openEntityModel()  throws org.radixware.kernel.common.exceptions.ServiceClientException, InterruptedException{
		     return (org.radixware.ads.Net.explorer.NetChannel.NetChannel_DefaultModel)super.openEntityModel();
		}
		@Override
		public 
		org.radixware.ads.Net.explorer.NetChannel.DefaultGroupModel openGroupModel(){
		     return (org.radixware.ads.Net.explorer.NetChannel.DefaultGroupModel)super.openGroupModel();
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.client.types.Reference dummy = ((org.radixware.kernel.common.client.types.Reference)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType.NetChannel:netChannel:netChannel")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType.NetChannel:netChannel:netChannel")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public NetChannel getNetChannel();


}

/* Radix::SystemMonitor::MetricType.NetChannel - Desktop Meta*/

/*Radix::SystemMonitor::MetricType.NetChannel-Application Class*/

package org.radixware.ads.SystemMonitor.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class MetricType.NetChannel_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::SystemMonitor::MetricType.NetChannel:Presentations-Entity Object Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("acl5V5QCN7ZP5DPFHWTMHEGX6HCXM"),
			"Radix::SystemMonitor::MetricType.NetChannel",
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aec2H6SULJHXJGFVIS6UVHRWQS4AM"),
			null,
			null,
			null,null,null,0,

			/*Radix::SystemMonitor::MetricType.NetChannel:Properties-Properties*/
			new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

					/*Radix::SystemMonitor::MetricType.NetChannel:netChannel:PropertyPresentation-Parent Ref Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKGZDKSPHXFHBHPGRL3JCWVGXSQ"),
						"netChannel",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acl5V5QCN7ZP5DPFHWTMHEGX6HCXM"),
						org.radixware.kernel.common.enums.EValType.PARENT_REF,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						21,
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
						133693439,false),

					/*Radix::SystemMonitor::MetricType.NetChannel:netListenerId:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMVLLX5SQNVDBRMQ2ZYOQ6WDVH4"),
						"netListenerId",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acl5V5QCN7ZP5DPFHWTMHEGX6HCXM"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						63,
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

						/*Radix::SystemMonitor::MetricType.NetChannel:netListenerId:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
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
			new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr5NYQFD3TZ5ATJL65ZK542GMR54"),org.radixware.kernel.common.types.Id.Factory.loadFrom("epr5QI7VNTATZGLBB3SADTLS3A56A")},
			true,false,false);
}

/* Radix::SystemMonitor::MetricType.NetChannel - Web Meta*/

/*Radix::SystemMonitor::MetricType.NetChannel-Application Class*/

package org.radixware.ads.SystemMonitor.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class MetricType.NetChannel_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::SystemMonitor::MetricType.NetChannel:Presentations-Entity Object Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("acl5V5QCN7ZP5DPFHWTMHEGX6HCXM"),
			"Radix::SystemMonitor::MetricType.NetChannel",
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

/* Radix::SystemMonitor::MetricType.NetChannel:Edit - Desktop Meta*/

/*Radix::SystemMonitor::MetricType.NetChannel:Edit-Editor Presentation*/

package org.radixware.ads.SystemMonitor.explorer;
public final class Edit_mi{
	private static final class Edit_DEF extends org.radixware.kernel.common.client.meta.RadEditorPresentationDef{
		Edit_DEF(){
			super(org.radixware.kernel.common.types.Id.Factory.loadFrom("epr5NYQFD3TZ5ATJL65ZK542GMR54"),
			"Edit",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("eprSE464PGOZVAYPG5C7DZYJQ4P3E"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("acl5V5QCN7ZP5DPFHWTMHEGX6HCXM"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl2H6SULJHXJGFVIS6UVHRWQS4AM"),
			null,
			null,

			/*Radix::SystemMonitor::MetricType.NetChannel:Edit:Editor Pages-Editor Presentation Pages*/
			new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

					/*Radix::SystemMonitor::MetricType.NetChannel:Edit:Parameters-Editor Page*/

					 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgBLOETUBE3RCINP7GTEZFWRN2KA"),"Parameters",org.radixware.kernel.common.types.Id.Factory.loadFrom("acl5V5QCN7ZP5DPFHWTMHEGX6HCXM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGTOFKSKWHVG7ZMMJKQEFB5KBU4"),null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKK3G7VOVNBESRPNV7SEYWFKLDQ"),0,3,1,false,false),

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

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKGZDKSPHXFHBHPGRL3JCWVGXSQ"),0,2,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJERRR2BH2ZD2XGDYMYUS67KOKY"),0,14,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPSRIBABNSJD45B3FB3UDKHACIM"),0,13,1,false,false)
					},null)
			},
			new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
				new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgBLOETUBE3RCINP7GTEZFWRN2KA")),
				new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epg6O4DDW7XZRHEHF7JGHBYCEPBEQ"))}
			,

			/*Radix::SystemMonitor::MetricType.NetChannel:Edit:Children-Explorer Items*/
				null
			,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
			null,
			0,
			null,
			(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
			36016,0,0);
		}
		@Override
		protected org.radixware.kernel.common.client.models.Model createModelImpl(org.radixware.kernel.common.client.IClientEnvironment userSession) {    return new org.radixware.ads.SystemMonitor.explorer.MetricType.NetChannel.MetricType.NetChannel_DefaultModel.eprSE464PGOZVAYPG5C7DZYJQ4P3E_ModelAdapter(userSession,this);
		}
	}
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new Edit_DEF(); 
;
}
/* Radix::SystemMonitor::MetricType.NetChannel:Create - Desktop Meta*/

/*Radix::SystemMonitor::MetricType.NetChannel:Create-Editor Presentation*/

package org.radixware.ads.SystemMonitor.explorer;
public final class Create_mi{
	private static final class Create_DEF extends org.radixware.kernel.common.client.meta.RadEditorPresentationDef{
		Create_DEF(){
			super(org.radixware.kernel.common.types.Id.Factory.loadFrom("epr5QI7VNTATZGLBB3SADTLS3A56A"),
			"Create",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("epr5NYQFD3TZ5ATJL65ZK542GMR54"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("acl5V5QCN7ZP5DPFHWTMHEGX6HCXM"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl2H6SULJHXJGFVIS6UVHRWQS4AM"),
			null,
			null,

			/*Radix::SystemMonitor::MetricType.NetChannel:Create:Editor Pages-Editor Presentation Pages*/
			null,
			new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
				new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgBLOETUBE3RCINP7GTEZFWRN2KA"))}
			,

			/*Radix::SystemMonitor::MetricType.NetChannel:Create:Children-Explorer Items*/
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
/* Radix::SystemMonitor::MetricType.NetChannel - Localizing Bundle */
package org.radixware.ads.SystemMonitor.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class MetricType.NetChannel - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<all>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<все>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsF2TKJDKNPFF5PH5D4VNRME7RLA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Parameters");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Параметры");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGTOFKSKWHVG7ZMMJKQEFB5KBU4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(MetricType.NetChannel - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbacl5V5QCN7ZP5DPFHWTMHEGX6HCXM"),"MetricType.NetChannel - Localizing Bundle",$$$items$$$);
}
