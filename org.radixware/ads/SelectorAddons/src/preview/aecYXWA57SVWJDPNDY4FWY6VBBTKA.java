
/* Radix::SelectorAddons::EasSelectorAddonsTables - Server Executable*/

/*Radix::SelectorAddons::EasSelectorAddonsTables-Entity Class*/

package org.radixware.ads.SelectorAddons.Dlg.server;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddonsTables")
public final published class EasSelectorAddonsTables  extends org.radixware.ads.Types.server.Entity  implements org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {


	/**Metainformation accessor method*/
	@SuppressWarnings("unused")
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return EasSelectorAddonsTables_mi.rdxMeta;}

	/*Radix::SelectorAddons::EasSelectorAddonsTables:Nested classes-Nested Classes*/

	/*Radix::SelectorAddons::EasSelectorAddonsTables:Properties-Properties*/

	/*Radix::SelectorAddons::EasSelectorAddonsTables:addonClassGuid-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddonsTables:addonClassGuid")
	public  Str getAddonClassGuid() {
		return addonClassGuid;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddonsTables:addonClassGuid")
	public   void setAddonClassGuid(Str val) {
		addonClassGuid = val;
	}

	/*Radix::SelectorAddons::EasSelectorAddonsTables:tableGuid-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddonsTables:tableGuid")
	public  Str getTableGuid() {
		return tableGuid;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddonsTables:tableGuid")
	public   void setTableGuid(Str val) {
		tableGuid = val;
	}

	/*Radix::SelectorAddons::EasSelectorAddonsTables:tableName-Dynamic Property*/



	protected Str tableName=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddonsTables:tableName")
	public  Str getTableName() {
		return tableName;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddonsTables:tableName")
	public   void setTableName(Str val) {
		tableName = val;
	}

	/*Radix::SelectorAddons::EasSelectorAddonsTables:isValid-Dynamic Property*/



	protected Bool isValid=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddonsTables:isValid")
	public  Bool getIsValid() {

		try{
		    Arte::Arte.getDefManager().getTableDef(Types::Id.Factory.loadFrom(tableGuid));
		    return true;
		}
		catch(Exceptions::DefinitionError error){
		    return false;
		}

	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddonsTables:isValid")
	public   void setIsValid(Bool val) {
		isValid = val;
	}





















































	/*Radix::SelectorAddons::EasSelectorAddonsTables:Methods-Methods*/


	@Override
	public org.radixware.kernel.server.types.FormHandler.NextDialogsRequest execCommand(org.radixware.kernel.common.types.Id cmdId, org.radixware.kernel.common.types.Id propId, org.apache.xmlbeans.XmlObject input, org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValsById, org.apache.xmlbeans.XmlObject output) throws org.radixware.kernel.common.exceptions.AppException,java.lang.InterruptedException{

			return super.execCommand(cmdId,propId,input,newPropValsById,output);
	}


}

/* Radix::SelectorAddons::EasSelectorAddonsTables - Server Meta*/

/*Radix::SelectorAddons::EasSelectorAddonsTables-Entity Class*/

package org.radixware.ads.SelectorAddons.Dlg.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class EasSelectorAddonsTables_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecYXWA57SVWJDPNDY4FWY6VBBTKA"),"EasSelectorAddonsTables",null,

						org.radixware.kernel.common.enums.EClassType.ENTITY,

						/*Radix::SelectorAddons::EasSelectorAddonsTables:Presentations-Entity Presentations*/
						/*Class presentation attributes*/
						 new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef(/*Owner Class Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("aecYXWA57SVWJDPNDY4FWY6VBBTKA"),
							/*Owner Class Name*/
							"EasSelectorAddonsTables",
							/*Title Id*/
							null,
							/*Property presentations*/

							/*Radix::SelectorAddons::EasSelectorAddonsTables:Properties-Properties*/
							new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef[]{

									/*Radix::SelectorAddons::EasSelectorAddonsTables:addonClassGuid:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colCK67UCPXRFEJXGR4VIQ7NQYMWI"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::SelectorAddons::EasSelectorAddonsTables:tableGuid:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGPOVD3BBHNEH7P6WYVSAOLXPGE"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::SelectorAddons::EasSelectorAddonsTables:tableName:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd3VGC27C5PNA5RLCF2R4ULHAV2I"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::SelectorAddons::EasSelectorAddonsTables:isValid:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdURBSCVHWBRAWZKO67C5H7KTSEA"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT))
							},
							/*Commands*/
							null,
							/*Sortings*/
							new org.radixware.kernel.server.meta.presentations.RadSortingDef[]{

									new org.radixware.kernel.server.meta.presentations.RadSortingDef(
									/*Radix::SelectorAddons::EasSelectorAddonsTables:Default-Sorting*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("srtNVV2TAGKV5CSFHGHKPRAWTWA4U"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecYXWA57SVWJDPNDY4FWY6VBBTKA"),"Default",null,new org.radixware.kernel.server.meta.presentations.RadSortingDef.Item[]{

											new org.radixware.kernel.server.meta.presentations.RadSortingDef.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("colCK67UCPXRFEJXGR4VIQ7NQYMWI"),org.radixware.kernel.common.enums.EOrder.ASC),

											new org.radixware.kernel.server.meta.presentations.RadSortingDef.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGPOVD3BBHNEH7P6WYVSAOLXPGE"),org.radixware.kernel.common.enums.EOrder.ASC)
									},"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware")
							},
							/*Filters*/
							new org.radixware.kernel.server.meta.presentations.RadFilterDef[]{

									new org.radixware.kernel.server.meta.presentations.RadFilterDef(
									/*Radix::SelectorAddons::EasSelectorAddonsTables:ByTable-Filter*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("fltFSDKA7I7GZAGRIUY3XWZEMM4MI"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecYXWA57SVWJDPNDY4FWY6VBBTKA"),"ByTable",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDBYAOTHWXVCYNBBTK7KYIR75TU"),"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:PropSqlName TableId=\"tblYXWA57SVWJDPNDY4FWY6VBBTKA\" PropId=\"colGPOVD3BBHNEH7P6WYVSAOLXPGE\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> = </xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmXNHKWGPHGRCU3KXHU7XM2LWSW4\"/></xsc:Item></xsc:Sqml>",null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>",org.radixware.kernel.common.types.Id.Factory.loadFrom("srtNVV2TAGKV5CSFHGHKPRAWTWA4U"),false,new org.radixware.kernel.server.meta.presentations.RadFilterDef.EnabledSorting[]{

											new org.radixware.kernel.server.meta.presentations.RadFilterDef.EnabledSorting("<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>",org.radixware.kernel.common.types.Id.Factory.loadFrom("srtNVV2TAGKV5CSFHGHKPRAWTWA4U"),"org.radixware")
									},false,"org.radixware")
							},
							/*Editor presentations*/
							new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef[]{

									new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef(
									/*Radix::SelectorAddons::EasSelectorAddonsTables:Table-Editor Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("epr2SWO254RHVH2LFX2HDLLELPVX4"),
									"Table",
									org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
									null,
									144,

									/*Radix::SelectorAddons::EasSelectorAddonsTables:Table:TitleFormat-Object Title Format*/

									new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecYXWA57SVWJDPNDY4FWY6VBBTKA"),new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.TitleItem[]{

											new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.TitleItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecYXWA57SVWJDPNDY4FWY6VBBTKA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("prd3VGC27C5PNA5RLCF2R4ULHAV2I"),"{0}",org.radixware.kernel.common.enums.ETitleNullFormat.SAME_AS_NOT_NULL,null)
									},null,org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.DefinitionContextType.EDITOR_PRESENTATION),

									/*Radix::SelectorAddons::EasSelectorAddonsTables:Table:Children-Explorer Items*/
										new org.radixware.kernel.server.meta.presentations.RadExplorerItemDef[]{

												/*Radix::SelectorAddons::EasSelectorAddonsTables:Table:EasSelectorAddons.Filters-Entity Explorer Item*/

												new org.radixware.kernel.server.meta.presentations.RadTableExplorerItemDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiNLNOL4IF4BA2DFZWMCIL55MIPY"),
													org.radixware.kernel.common.types.Id.Factory.loadFrom("aecYXWA57SVWJDPNDY4FWY6VBBTKA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aclLQBIKZWZABEIXKZOG4W3L6I5XI"),
													org.radixware.kernel.common.types.Id.Factory.loadFrom("sprQWY42P52BFF3JGZLM2EPVKHC7U"),
													new org.radixware.kernel.server.meta.presentations.RadConditionDef("<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:PropSqlName TableId=\"tblHQSOHRWZH5ESLD2Y6IVF6XFBE4\" PropId=\"colDCQEQLHEC5F6VO633LHAE6BDMQ\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> = </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblYXWA57SVWJDPNDY4FWY6VBBTKA\" PropId=\"colGPOVD3BBHNEH7P6WYVSAOLXPGE\" Owner=\"PARENT\"/></xsc:Item></xsc:Sqml>","<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),
													null,
													null),

												/*Radix::SelectorAddons::EasSelectorAddonsTables:Table:EasSelectorAddons.AllFilters-Entity Explorer Item*/

												new org.radixware.kernel.server.meta.presentations.RadTableExplorerItemDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiAQXGXNBP5RDIDCKHSRLQVD4EX4"),
													org.radixware.kernel.common.types.Id.Factory.loadFrom("aecYXWA57SVWJDPNDY4FWY6VBBTKA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aclLQBIKZWZABEIXKZOG4W3L6I5XI"),
													org.radixware.kernel.common.types.Id.Factory.loadFrom("sprQWY42P52BFF3JGZLM2EPVKHC7U"),
													new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),
													null,
													null)
										}
									,
									org.radixware.kernel.server.types.Restrictions.Factory.newInstance(39,null,null,null),
									(org.radixware.kernel.server.meta.presentations.RadPropertyPresentationAttributes[]) null,
									org.radixware.kernel.common.enums.EEditorPresentationRightsInheritanceMode.NONE,
									null,null)
							},
							/*Selector presentations*/
							new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef[]{

									new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef(
									/*Radix::SelectorAddons::EasSelectorAddonsTables:Filters-Selector Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("sprDAS74UITUZEPHBK5LCBWXDZWY4"),"Filters",org.radixware.kernel.common.types.Id.Factory.loadFrom("spr56JKKZ43JBBZHDYQC7CW3QFFCI"),16424,new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn[]{

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGPOVD3BBHNEH7P6WYVSAOLXPGE"),false),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colCK67UCPXRFEJXGR4VIQ7NQYMWI"),false),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdURBSCVHWBRAWZKO67C5H7KTSEA"),false),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd3VGC27C5PNA5RLCF2R4ULHAV2I"),true)
									},new org.radixware.kernel.server.meta.presentations.RadConditionDef("<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:PropSqlName TableId=\"tblYXWA57SVWJDPNDY4FWY6VBBTKA\" PropId=\"colCK67UCPXRFEJXGR4VIQ7NQYMWI\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> = </xsc:Sql></xsc:Item><xsc:Item><xsc:Id Path=\"aclLQBIKZWZABEIXKZOG4W3L6I5XI\"/></xsc:Item></xsc:Sqml>","<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),null,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr2SWO254RHVH2LFX2HDLLELPVX4")},null,null,null),

									new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef(
									/*Radix::SelectorAddons::EasSelectorAddonsTables:Base-Selector Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("spr56JKKZ43JBBZHDYQC7CW3QFFCI"),"Base",null,144,new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn[]{

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGPOVD3BBHNEH7P6WYVSAOLXPGE"),false),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colCK67UCPXRFEJXGR4VIQ7NQYMWI"),false),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdURBSCVHWBRAWZKO67C5H7KTSEA"),false),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd3VGC27C5PNA5RLCF2R4ULHAV2I"),true)
									},new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.Addons(org.radixware.kernel.common.types.Id.Factory.loadFrom("srtNVV2TAGKV5CSFHGHKPRAWTWA4U"),false,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("srtNVV2TAGKV5CSFHGHKPRAWTWA4U")},false,null,false,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("fltFSDKA7I7GZAGRIUY3XWZEMM4MI")},false,false,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr2SWO254RHVH2LFX2HDLLELPVX4")},null,org.radixware.kernel.server.types.Restrictions.Factory.newInstance(281711,null,null,null),null)
							},
							/*Default selector presentation Id*/
							null,
							/*Presentation Map*/
							null,
							/*Object title format*/

							/*Radix::SelectorAddons::EasSelectorAddonsTables:TitleFormat-Object Title Format*/

							new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecYXWA57SVWJDPNDY4FWY6VBBTKA"),null,null,org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.DefinitionContextType.ENTITY),
							/*Class catalogs*/
							null,
							/*Restrictions*/
							org.radixware.kernel.server.types.Restrictions.Factory.newInstance(28679,null,null,null),null,0),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblYXWA57SVWJDPNDY4FWY6VBBTKA"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("pdcEntity____________________"),

						/*Radix::SelectorAddons::EasSelectorAddonsTables:Properties-Properties*/
						new org.radixware.kernel.server.meta.clazzes.RadPropDef[]{

								/*Radix::SelectorAddons::EasSelectorAddonsTables:addonClassGuid-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colCK67UCPXRFEJXGR4VIQ7NQYMWI"),"addonClassGuid",null,org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::SelectorAddons::EasSelectorAddonsTables:tableGuid-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGPOVD3BBHNEH7P6WYVSAOLXPGE"),"tableGuid",null,org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::SelectorAddons::EasSelectorAddonsTables:tableName-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd3VGC27C5PNA5RLCF2R4ULHAV2I"),"tableName",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2DUNOPW7H5F6XI6AAT2HAANJRY"),org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::SelectorAddons::EasSelectorAddonsTables:isValid-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdURBSCVHWBRAWZKO67C5H7KTSEA"),"isValid",null,org.radixware.kernel.common.enums.EValType.BOOL,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE)
						},

						/*Radix::SelectorAddons::EasSelectorAddonsTables:Methods-Methods*/
						null,
						null,
						org.radixware.kernel.common.enums.EAccessAreaType.NONE,null,null,false);
}

/* Radix::SelectorAddons::EasSelectorAddonsTables - Desktop Executable*/

/*Radix::SelectorAddons::EasSelectorAddonsTables-Entity Class*/

package org.radixware.ads.SelectorAddons.Dlg.explorer;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddonsTables")
public interface EasSelectorAddonsTables {
	/*Group model adapter class. This is the base class for 
	all of selector presentation models of this entity*/
	public static class DefaultGroupModel extends org.radixware.kernel.common.client.models.GroupModel{
		public DefaultGroupModel(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadSelectorPresentationDef def){super(userSession,def);}





		public org.radixware.ads.SelectorAddons.Dlg.explorer.EasSelectorAddonsTables.EasSelectorAddonsTables_DefaultModel getEntity(int i) throws InterruptedException, org.radixware.kernel.common.client.exceptions.BrokenEntityObjectException, org.radixware.kernel.common.exceptions.ServiceClientException { return (org.radixware.ads.SelectorAddons.Dlg.explorer.EasSelectorAddonsTables.EasSelectorAddonsTables_DefaultModel )  super.getEntity(i);}
	}










































	/*Radix::SelectorAddons::EasSelectorAddonsTables:addonClassGuid:addonClassGuid-Presentation Property*/


	public class AddonClassGuid extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public AddonClassGuid(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddonsTables:addonClassGuid:addonClassGuid")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddonsTables:addonClassGuid:addonClassGuid")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public AddonClassGuid getAddonClassGuid();
	/*Radix::SelectorAddons::EasSelectorAddonsTables:tableGuid:tableGuid-Presentation Property*/


	public class TableGuid extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public TableGuid(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddonsTables:tableGuid:tableGuid")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddonsTables:tableGuid:tableGuid")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public TableGuid getTableGuid();
	/*Radix::SelectorAddons::EasSelectorAddonsTables:tableName:tableName-Presentation Property*/


	public class TableName extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public TableName(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddonsTables:tableName:tableName")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddonsTables:tableName:tableName")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public TableName getTableName();
	/*Radix::SelectorAddons::EasSelectorAddonsTables:isValid:isValid-Presentation Property*/


	public class IsValid extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public IsValid(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Bool dummy = ((Bool)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddonsTables:isValid:isValid")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddonsTables:isValid:isValid")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public IsValid getIsValid();


}

/* Radix::SelectorAddons::EasSelectorAddonsTables - Desktop Meta*/

/*Radix::SelectorAddons::EasSelectorAddonsTables-Entity Class*/

package org.radixware.ads.SelectorAddons.Dlg.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class EasSelectorAddonsTables_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::SelectorAddons::EasSelectorAddonsTables:Presentations-Entity Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecYXWA57SVWJDPNDY4FWY6VBBTKA"),
			"Radix::SelectorAddons::EasSelectorAddonsTables",
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("img3QT4LG7KC5FXNKA7MR4ETTJKJY"),
			null,
			null,null,null,28679,

			/*Radix::SelectorAddons::EasSelectorAddonsTables:Properties-Properties*/
			new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

					/*Radix::SelectorAddons::EasSelectorAddonsTables:addonClassGuid:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colCK67UCPXRFEJXGR4VIQ7NQYMWI"),
						"addonClassGuid",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecYXWA57SVWJDPNDY4FWY6VBBTKA"),
						org.radixware.kernel.common.enums.EValType.STR,
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
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::SelectorAddons::EasSelectorAddonsTables:addonClassGuid:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,50,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::SelectorAddons::EasSelectorAddonsTables:tableGuid:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGPOVD3BBHNEH7P6WYVSAOLXPGE"),
						"tableGuid",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecYXWA57SVWJDPNDY4FWY6VBBTKA"),
						org.radixware.kernel.common.enums.EValType.STR,
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
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::SelectorAddons::EasSelectorAddonsTables:tableGuid:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,50,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::SelectorAddons::EasSelectorAddonsTables:tableName:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd3VGC27C5PNA5RLCF2R4ULHAV2I"),
						"tableName",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2DUNOPW7H5F6XI6AAT2HAANJRY"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecYXWA57SVWJDPNDY4FWY6VBBTKA"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::SelectorAddons::EasSelectorAddonsTables:tableName:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::SelectorAddons::EasSelectorAddonsTables:isValid:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdURBSCVHWBRAWZKO67C5H7KTSEA"),
						"isValid",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecYXWA57SVWJDPNDY4FWY6VBBTKA"),
						org.radixware.kernel.common.enums.EValType.BOOL,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::SelectorAddons::EasSelectorAddonsTables:isValid:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecYXWA57SVWJDPNDY4FWY6VBBTKA"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false)
			},
			null,
			new org.radixware.kernel.common.client.meta.filters.RadFilterDef[]{

					new org.radixware.kernel.common.client.meta.filters.RadFilterDef(
					/*Radix::SelectorAddons::EasSelectorAddonsTables:ByTable-Filter*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("fltFSDKA7I7GZAGRIUY3XWZEMM4MI"),
						"ByTable",
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecYXWA57SVWJDPNDY4FWY6VBBTKA"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDBYAOTHWXVCYNBBTK7KYIR75TU"),
						"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:PropSqlName TableId=\"tblYXWA57SVWJDPNDY4FWY6VBBTKA\" PropId=\"colGPOVD3BBHNEH7P6WYVSAOLXPGE\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> = </xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmXNHKWGPHGRCU3KXHU7XM2LWSW4\"/></xsc:Item></xsc:Sqml>",
						null,
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("srtNVV2TAGKV5CSFHGHKPRAWTWA4U")},
						false,
						false,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("srtNVV2TAGKV5CSFHGHKPRAWTWA4U"),
						new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef[]{

								new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmXNHKWGPHGRCU3KXHU7XM2LWSW4"),
								"tableGuid",
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXD7X4P6ESFHLTED6A362QM6MLA"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("aecYXWA57SVWJDPNDY4FWY6VBBTKA"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("tblYXWA57SVWJDPNDY4FWY6VBBTKA"),
								null,
								org.radixware.kernel.common.enums.EValType.STR,
								null,
								null,
								true,
								false,
								false,
								null,
								false,
								null,
								null,
								false,
								null,
								null)
						},

						/*Radix::SelectorAddons::EasSelectorAddonsTables:ByTable:Editor Pages-Filter Pages*/
						null,
						new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
						}
						,
						null)
			},
			new org.radixware.kernel.common.client.meta.RadSortingDef[]{

					new org.radixware.kernel.common.client.meta.RadSortingDef(
					/*Radix::SelectorAddons::EasSelectorAddonsTables:Default-Sorting*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("srtNVV2TAGKV5CSFHGHKPRAWTWA4U"),
						"Default",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecYXWA57SVWJDPNDY4FWY6VBBTKA"),
						null,
						new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem[]{

								new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colCK67UCPXRFEJXGR4VIQ7NQYMWI"),
								false),

								new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGPOVD3BBHNEH7P6WYVSAOLXPGE"),
								false)
						})
			},
			null,
			new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr2SWO254RHVH2LFX2HDLLELPVX4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("sprDAS74UITUZEPHBK5LCBWXDZWY4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("spr56JKKZ43JBBZHDYQC7CW3QFFCI")},
			false,false,false);
}

/* Radix::SelectorAddons::EasSelectorAddonsTables - Web Meta*/

/*Radix::SelectorAddons::EasSelectorAddonsTables-Entity Class*/

package org.radixware.ads.SelectorAddons.Dlg.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class EasSelectorAddonsTables_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::SelectorAddons::EasSelectorAddonsTables:Presentations-Entity Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecYXWA57SVWJDPNDY4FWY6VBBTKA"),
			"Radix::SelectorAddons::EasSelectorAddonsTables",
			null,
			null,
			null,
			null,null,null,28679,
			null,
			null,
			null,
			null,
			null,
			null,
			false,false,false);
}

/* Radix::SelectorAddons::EasSelectorAddonsTables:Table - Desktop Meta*/

/*Radix::SelectorAddons::EasSelectorAddonsTables:Table-Editor Presentation*/

package org.radixware.ads.SelectorAddons.Dlg.explorer;
public final class Table_mi{
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new org.radixware.kernel.common.client.meta.RadEditorPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epr2SWO254RHVH2LFX2HDLLELPVX4"),
	"Table",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
	null,
	org.radixware.kernel.common.types.Id.Factory.loadFrom("aecYXWA57SVWJDPNDY4FWY6VBBTKA"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("tblYXWA57SVWJDPNDY4FWY6VBBTKA"),
	null,
	null,

	/*Radix::SelectorAddons::EasSelectorAddonsTables:Table:Editor Pages-Editor Presentation Pages*/
	null,
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
	}
	,

	/*Radix::SelectorAddons::EasSelectorAddonsTables:Table:Children-Explorer Items*/
		new org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemDef[]{

				/*Radix::SelectorAddons::EasSelectorAddonsTables:Table:EasSelectorAddons.Filters-Entity Explorer Item*/

				new org.radixware.kernel.common.client.meta.explorerItems.RadSelectorExplorerItemDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiNLNOL4IF4BA2DFZWMCIL55MIPY"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aecYXWA57SVWJDPNDY4FWY6VBBTKA"),null,
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aclLQBIKZWZABEIXKZOG4W3L6I5XI"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("sprQWY42P52BFF3JGZLM2EPVKHC7U"),
					0,
					null,
					16560,false),

				/*Radix::SelectorAddons::EasSelectorAddonsTables:Table:EasSelectorAddons.AllFilters-Entity Explorer Item*/

				new org.radixware.kernel.common.client.meta.explorerItems.RadSelectorExplorerItemDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiAQXGXNBP5RDIDCKHSRLQVD4EX4"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aecYXWA57SVWJDPNDY4FWY6VBBTKA"),null,
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aclLQBIKZWZABEIXKZOG4W3L6I5XI"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("sprQWY42P52BFF3JGZLM2EPVKHC7U"),
					0,
					null,
					16560,false)
		}
	,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
	null,
	39,
	null,
	(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
	144,0,0,null);
}
/* Radix::SelectorAddons::EasSelectorAddonsTables:Table:Model - Desktop Executable*/

/*Radix::SelectorAddons::EasSelectorAddonsTables:Table:Model-Entity Model Class*/

package org.radixware.ads.SelectorAddons.Dlg.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddonsTables:Table:Model")
public class Table:Model  extends org.radixware.ads.SelectorAddons.Dlg.explorer.EasSelectorAddonsTables.EasSelectorAddonsTables_DefaultModel {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return Table:Model_mi.rdxMeta; }



	public Table:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadEditorPresentationDef def){super(userSession,def);}

	/*Radix::SelectorAddons::EasSelectorAddonsTables:Table:Model:Nested classes-Nested Classes*/

	/*Radix::SelectorAddons::EasSelectorAddonsTables:Table:Model:Properties-Properties*/

	/*Radix::SelectorAddons::EasSelectorAddonsTables:Table:Model:tableName-Presentation Property*/




	public class TableName extends org.radixware.ads.SelectorAddons.Dlg.explorer.EasSelectorAddonsTables.prd3VGC27C5PNA5RLCF2R4ULHAV2I{
		public TableName(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddonsTables:Table:Model:tableName")
		public  Str getValue() {

			final Explorer.Sqml::ISqmlTableDef table = getEnvironment().getSqmlDefinitions().findTableById(Types::Id.Factory.loadFrom(tableGuid.Value));
			return table == null ? "#"+tableGuid.Value : table.getShortName();
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddonsTables:Table:Model:tableName")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public TableName getTableName(){return (TableName)getProperty(prd3VGC27C5PNA5RLCF2R4ULHAV2I);}








	/*Radix::SelectorAddons::EasSelectorAddonsTables:Table:Model:Methods-Methods*/

	/*Radix::SelectorAddons::EasSelectorAddonsTables:Table:Model:getSelectorRowStyle-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddonsTables:Table:Model:getSelectorRowStyle")
	public published  org.radixware.kernel.common.enums.ESelectorRowStyle getSelectorRowStyle () {
		return isValid.Value.booleanValue() ? super.getSelectorRowStyle() : Explorer.Env::SelectorStyle:Bad;
	}


}

/* Radix::SelectorAddons::EasSelectorAddonsTables:Table:Model - Desktop Meta*/

/*Radix::SelectorAddons::EasSelectorAddonsTables:Table:Model-Entity Model Class*/

package org.radixware.ads.SelectorAddons.Dlg.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Table:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aem2SWO254RHVH2LFX2HDLLELPVX4"),
						"Table:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::SelectorAddons::EasSelectorAddonsTables:Table:Model:Properties-Properties*/
						null,
						null,
						null,
						null,
						null,
						null,
						false,
						false,
						false
						);
}

/* Radix::SelectorAddons::EasSelectorAddonsTables:Filters - Desktop Meta*/

/*Radix::SelectorAddons::EasSelectorAddonsTables:Filters-Selector Presentation*/

package org.radixware.ads.SelectorAddons.Dlg.explorer;
public final class Filters_mi extends org.radixware.kernel.common.client.meta.RadSelectorPresentationDef{
	public static final org.radixware.kernel.common.client.meta.RadSelectorPresentationDef rdxMeta = new Filters_mi();
	private Filters_mi(){
		super(org.radixware.kernel.common.types.Id.Factory.loadFrom("sprDAS74UITUZEPHBK5LCBWXDZWY4"),
		"Filters",
		org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("spr56JKKZ43JBBZHDYQC7CW3QFFCI"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("aecYXWA57SVWJDPNDY4FWY6VBBTKA"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("tblYXWA57SVWJDPNDY4FWY6VBBTKA"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZBU2CZWYKVFW7IOOI5J7CJNTS4"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("imgJAFMGUWJY5H5PFIPGPERL5ESMA"),
		null,
		null,
		false,
		null,
		null,
		false,
		false,
		null,
		0,null,
		16424,
		null,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr2SWO254RHVH2LFX2HDLLELPVX4")},
		false,true,false,0,0);
		columns = new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn[]{

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGPOVD3BBHNEH7P6WYVSAOLXPGE"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colCK67UCPXRFEJXGR4VIQ7NQYMWI"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdURBSCVHWBRAWZKO67C5H7KTSEA"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd3VGC27C5PNA5RLCF2R4ULHAV2I"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsI36YOGO6OZESLHIYFZII2KCWBU"))
		};
	}
}
/* Radix::SelectorAddons::EasSelectorAddonsTables:Filters:Model - Desktop Executable*/

/*Radix::SelectorAddons::EasSelectorAddonsTables:Filters:Model-Group Model Class*/

package org.radixware.ads.SelectorAddons.Dlg.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddonsTables:Filters:Model")
public class Filters:Model  extends org.radixware.ads.SelectorAddons.Dlg.explorer.Base:Model  {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return Filters:Model_mi.rdxMeta; }



	public Filters:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadSelectorPresentationDef def){super(userSession,def);}

	@Override
	@SuppressWarnings("deprecation")
	public org.radixware.kernel.common.client.views.IView createView() { return new 
	org.radixware.ads.SelectorAddons.Dlg.explorer.View(getEnvironment());}
	@Override
	public org.radixware.kernel.common.types.Id getCustomViewId() {return org.radixware.kernel.common.types.Id.Factory.loadFrom("cesDAS74UITUZEPHBK5LCBWXDZWY4");}


	/*Radix::SelectorAddons::EasSelectorAddonsTables:Filters:Model:Nested classes-Nested Classes*/

	/*Radix::SelectorAddons::EasSelectorAddonsTables:Filters:Model:Properties-Properties*/

	/*Radix::SelectorAddons::EasSelectorAddonsTables:Filters:Model:Methods-Methods*/

	/*Radix::SelectorAddons::EasSelectorAddonsTables:Filters:Model:Selector_opened-Presentation Slot Method*/

	@org.radixware.kernel.common.lang.ReflectiveCallable
	@SuppressWarnings("unused")
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddonsTables:Filters:Model:Selector_opened")
	private final  void Selector_opened (com.trolltech.qt.gui.QWidget widget) {
		final com.trolltech.qt.gui.QVBoxLayout layout = new com.trolltech.qt.gui.QVBoxLayout(widget);
		layout.setContentsMargins(0,0,0,0);

		final SelectorAddons.Dlg::FiltersTree tree = new FiltersTree(EasSelectorAddonsTables:Filters:View:Selector);
		layout.addWidget(tree);
		GroupView.setSelectorWidget(tree);
		tree.setFocus();
		{
		    final java.util.List<Types::Id> enabledCommandIds = getAccessibleCommandIds();
		    for (Types::Id commandId: enabledCommandIds){
		        getCommand(commandId).unsubscribeAll();
		    }

		    final com.trolltech.qt.gui.QToolBar toolBar = ((Explorer.Views::SelectorView)GroupView).getCommandBar();
		    toolBar.clear();
		    addCommandButton(toolBar,idof[EasSelectorAddonsTablesGroup:moveUp]);
		    addCommandButton(toolBar,idof[EasSelectorAddonsTablesGroup:moveDown]);
		    toolBar.addSeparator();
		    {
		        final Explorer.Models::CommandModel cmdExportFilters = getCommand(idof[EasSelectorAddonsTablesGroup:exportAllFilters]);
		        final org.radixware.kernel.common.client.widgets.ICommandToolButton toolButton = cmdExportFilters.createToolButton();
		        final Explorer.Qt.Types::QWidget toolButtonWidget = (Explorer.Qt.Types::QWidget)toolButton;
		        toolButtonWidget.setSizePolicy(com.trolltech.qt.gui.QSizePolicy.Policy.Fixed, com.trolltech.qt.gui.QSizePolicy.Policy.Preferred);
		        final com.trolltech.qt.gui.QAction action = new com.trolltech.qt.gui.QAction(toolButtonWidget);
		        action.setText("For current table only");
		        action.setIcon(Explorer.Icons::ExplorerIcon.getQIcon(cmdExportFilters.Icon));
		        action.triggered.connect(this,idof[EasSelectorAddonsTables:Filters:Filters:Model:exportCurrentTableFilters].toString()+"()");
		        ((Explorer.Qt.Types::QWidget)toolButton).addAction(action);
		        toolBar.addWidget(toolButtonWidget);
		        toolButton.bind();
		    }
		    addCommandButton(toolBar,idof[EasSelectorAddonsTablesGroup:importFilters]);    
		    toolBar.setVisible(true);
		}

	}

	/*Radix::SelectorAddons::EasSelectorAddonsTables:Filters:Model:onCommand_exportAll-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddonsTables:Filters:Model:onCommand_exportAll")
	private final  void onCommand_exportAll (org.radixware.ads.SelectorAddons.Dlg.explorer.EasSelectorAddonsTablesGroup.ExportAllFilters command) {
		if (GroupView!=null && GroupView.getCurrentEntity()!=null){
		    if (getEntitiesCount()==1){
		        exportCurrentTableFilters();
		    }
		    else{
		        exportFilters(null);
		    }
		}
	}

	/*Radix::SelectorAddons::EasSelectorAddonsTables:Filters:Model:afterRead-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddonsTables:Filters:Model:afterRead")
	protected published  void afterRead (java.util.List<org.radixware.kernel.common.client.models.EntityModel> newEntities, java.util.List<org.radixware.kernel.common.types.Id> serverDisabledCommands, java.util.EnumSet<org.radixware.kernel.common.enums.ERestriction> serverRestrictions) {
		super.afterRead(newEntities, serverDisabledCommands, serverRestrictions);
		getCommand(idof[EasSelectorAddonsTablesGroup:exportAllFilters]).setEnabled(!isEmpty());
	}

	/*Radix::SelectorAddons::EasSelectorAddonsTables:Filters:Model:exportFilters-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddonsTables:Filters:Model:exportFilters")
	private final  void exportFilters (Str tableGuid) {
		final Types::Id childId
		        = tableGuid == null ? idof[EasSelectorAddonsTables:Table:EasSelectorAddons.AllFilters] : idof[EasSelectorAddonsTables:Table:EasSelectorAddons.Filters];
		try {
		    Explorer.Models::EntityModel currentEntity = GroupView.getCurrentEntity();
		    if (currentEntity != null && !currentEntity.getClassId().equals(idof[EasSelectorAddonsTables])) {
		        currentEntity = Explorer.Context::Utils.findOwnerEntityModel(currentEntity, EasSelectorAddonsTables:Table:Model.class);
		    }
		    if (currentEntity != null) {
		        final EasSelectorAddons.Filters:General:Model filtersGroup
		                = (EasSelectorAddons.Filters:General:Model) currentEntity.getChildModel(childId);
		        filtersGroup.export(tableGuid);
		    }
		} catch (Exceptions::ServiceClientException ex) {
		    showException(ex);
		} catch (Exceptions::InterruptedException ex) {

		}
	}

	/*Radix::SelectorAddons::EasSelectorAddonsTables:Filters:Model:onCommand_importFilters-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddonsTables:Filters:Model:onCommand_importFilters")
	private final  void onCommand_importFilters (org.radixware.ads.SelectorAddons.Dlg.explorer.EasSelectorAddonsTablesGroup.ImportFilters command) {
		java.io.File file = CfgManagement::DesktopUtils.openFileForImport(findNearestView(), command.getTitle());
		if (file!=null){
		    //final  progress = ().().();
		    //progress.(,false);
		    try{        
		        SelectorAddonsImpExpXsd:ImportParametersDocument xIn = SelectorAddonsImpExpXsd:ImportParametersDocument.Factory.newInstance();
		        xIn.addNewImportParameters().FilePath = file.getAbsolutePath();
		        Common.Dlg::ClientUtils.viewImportResult(command.send(xIn));
		        reread();
		    }catch(Exceptions::InterruptedException  e){

		    }catch(Exceptions::ServiceClientException  e){
		            showException(e);
		    }
		    finally{
		        //progress.();
		    }
		}
	}

	/*Radix::SelectorAddons::EasSelectorAddonsTables:Filters:Model:exportCurrentTableFilters-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddonsTables:Filters:Model:exportCurrentTableFilters")
	protected final  void exportCurrentTableFilters () {
		if (GroupView!=null && GroupView.getCurrentEntity()!=null){
		    Explorer.Models::EntityModel currentEntity = GroupView.getCurrentEntity();    
		    if (currentEntity!=null && !currentEntity.getClassId().equals(idof[EasSelectorAddonsTables])){
		        currentEntity = Explorer.Context::Utils.findOwnerEntityModel(currentEntity,EasSelectorAddonsTables:Table:Model.class);
		    }
		    if (currentEntity!=null){
		        final Str tableGuid = ((EasSelectorAddonsTables:Table:Model)currentEntity).tableGuid.Value;
		        exportFilters(tableGuid);
		    }            
		}
	}

	/*Radix::SelectorAddons::EasSelectorAddonsTables:Filters:Model:addCommandButton-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddonsTables:Filters:Model:addCommandButton")
	public  void addCommandButton (com.trolltech.qt.gui.QToolBar toolBar, org.radixware.kernel.common.types.Id cmdId) {
		final Explorer.Models::CommandModel command = getCommand(cmdId);
		final org.radixware.kernel.common.client.widgets.ICommandToolButton toolButton = command.createToolButton();
		final Explorer.Qt.Types::QWidget toolButtonWidget = (Explorer.Qt.Types::QWidget)toolButton;
		toolButtonWidget.setSizePolicy(com.trolltech.qt.gui.QSizePolicy.Policy.Fixed, com.trolltech.qt.gui.QSizePolicy.Policy.Preferred);
		toolBar.addWidget(toolButtonWidget);
		toolButton.bind();
	}
	public final class ExportAllFilters extends org.radixware.ads.SelectorAddons.Dlg.explorer.EasSelectorAddonsTablesGroup.ExportAllFilters{
		protected ExportAllFilters(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_exportAllFilters( this );
		}

	}

	public final class ImportFilters extends org.radixware.ads.SelectorAddons.Dlg.explorer.EasSelectorAddonsTablesGroup.ImportFilters{
		protected ImportFilters(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_importFilters( this );
		}

	}




















}

/* Radix::SelectorAddons::EasSelectorAddonsTables:Filters:Model - Desktop Meta*/

/*Radix::SelectorAddons::EasSelectorAddonsTables:Filters:Model-Group Model Class*/

package org.radixware.ads.SelectorAddons.Dlg.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Filters:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("agmDAS74UITUZEPHBK5LCBWXDZWY4"),
						"Filters:Model",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("agm56JKKZ43JBBZHDYQC7CW3QFFCI"),
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::SelectorAddons::EasSelectorAddonsTables:Filters:Model:Properties-Properties*/
						null,
						null,
						null,
						null,
						null,
						null,
						false,
						false,
						false
						);
}

/* Radix::SelectorAddons::EasSelectorAddonsTables:Filters:View - Desktop Executable*/

/*Radix::SelectorAddons::EasSelectorAddonsTables:Filters:View-Custom Selector for Desktop*/

package org.radixware.ads.SelectorAddons.Dlg.explorer;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddonsTables:Filters:View")
public class View extends org.radixware.kernel.explorer.views.selector.Selector {
	final private com.trolltech.qt.gui.QFont DEFAULT_FONT = org.radixware.kernel.explorer.text.ExplorerTextOptions.Factory.getDefault().getQFont();
	public View Selector;
	public View getSelector(){ return Selector;}
	public View(org.radixware.kernel.common.client.IClientEnvironment userSession) {
		super(userSession);
	}
	public void open(org.radixware.kernel.common.client.models.Model model) {
		super.open(model);
		Selector = this;
		Selector.setObjectName("Selector");
		Selector.setFont(DEFAULT_FONT);
		Selector.opened.connect(model, "mth2RGPHPMYCRGABNWE56ZVKZV2UQ(com.trolltech.qt.gui.QWidget)");
		opened.emit(this.content);
	}
	public final org.radixware.ads.SelectorAddons.Dlg.explorer.Filters:Model getModel() {
		return (org.radixware.ads.SelectorAddons.Dlg.explorer.Filters:Model) super.getModel();
	}

}

/* Radix::SelectorAddons::EasSelectorAddonsTables:Base - Desktop Meta*/

/*Radix::SelectorAddons::EasSelectorAddonsTables:Base-Selector Presentation*/

package org.radixware.ads.SelectorAddons.Dlg.explorer;
public final class Base_mi extends org.radixware.kernel.common.client.meta.RadSelectorPresentationDef{
	public static final org.radixware.kernel.common.client.meta.RadSelectorPresentationDef rdxMeta = new Base_mi();
	private Base_mi(){
		super(org.radixware.kernel.common.types.Id.Factory.loadFrom("spr56JKKZ43JBBZHDYQC7CW3QFFCI"),
		"Base",
		org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
		null,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("aecYXWA57SVWJDPNDY4FWY6VBBTKA"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("tblYXWA57SVWJDPNDY4FWY6VBBTKA"),
		null,
		null,
		null,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("srtNVV2TAGKV5CSFHGHKPRAWTWA4U")},
		false,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("srtNVV2TAGKV5CSFHGHKPRAWTWA4U"),
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("fltFSDKA7I7GZAGRIUY3XWZEMM4MI")},
		false,
		false,
		null,
		281711,
		null,
		144,
		null,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr2SWO254RHVH2LFX2HDLLELPVX4")},
		false,true,false,0,0);
		columns = new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn[]{

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGPOVD3BBHNEH7P6WYVSAOLXPGE"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colCK67UCPXRFEJXGR4VIQ7NQYMWI"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdURBSCVHWBRAWZKO67C5H7KTSEA"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd3VGC27C5PNA5RLCF2R4ULHAV2I"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null)
		};
	}
}
/* Radix::SelectorAddons::EasSelectorAddonsTables:Base:Model - Desktop Executable*/

/*Radix::SelectorAddons::EasSelectorAddonsTables:Base:Model-Group Model Class*/

package org.radixware.ads.SelectorAddons.Dlg.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddonsTables:Base:Model")
public class Base:Model  extends org.radixware.ads.SelectorAddons.Dlg.explorer.EasSelectorAddonsTables.DefaultGroupModel implements org.radixware.kernel.common.client.views.ISelector.CurrentEntityHandler  {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return Base:Model_mi.rdxMeta; }



	public Base:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadSelectorPresentationDef def){super(userSession,def);}

	/*Radix::SelectorAddons::EasSelectorAddonsTables:Base:Model:Nested classes-Nested Classes*/

	/*Radix::SelectorAddons::EasSelectorAddonsTables:Base:Model:Properties-Properties*/

	/*Radix::SelectorAddons::EasSelectorAddonsTables:Base:Model:sqmlTablesLoaded-Dynamic Property*/



	protected static boolean sqmlTablesLoaded=false;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddonsTables:Base:Model:sqmlTablesLoaded")
	private static final  boolean getSqmlTablesLoaded() {
		return sqmlTablesLoaded;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddonsTables:Base:Model:sqmlTablesLoaded")
	private static final   void setSqmlTablesLoaded(boolean val) {
		sqmlTablesLoaded = val;
	}






	/*Radix::SelectorAddons::EasSelectorAddonsTables:Base:Model:Methods-Methods*/

	/*Radix::SelectorAddons::EasSelectorAddonsTables:Base:Model:beforeOpenView-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddonsTables:Base:Model:beforeOpenView")
	protected published  void beforeOpenView () {
		super.beforeOpenView();
		getCommand(idof[EasSelectorAddonsTablesGroup:moveUp]).setEnabled(false);
		getCommand(idof[EasSelectorAddonsTablesGroup:moveDown]).setEnabled(false);
		GroupView.addCurrentEntityHandler(this);
		if (!sqmlTablesLoaded){
		    getEnvironment().getSqmlDefinitions();
		    sqmlTablesLoaded = true;
		}
	}

	/*Radix::SelectorAddons::EasSelectorAddonsTables:Base:Model:readMore-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddonsTables:Base:Model:readMore")
	protected published  void readMore () throws org.radixware.kernel.common.exceptions.ServiceClientException,java.lang.InterruptedException {
		super.readMore();
		while(hasMoreRows()){
		    super.readMore();
		}
		final java.util.List<Explorer.Models::EntityModel> entities = 
		    new java.util.ArrayList<Explorer.Models::EntityModel>(getAllRows());
		if (!entities.isEmpty()){
		    final Java.Lang::Comparator<Explorer.Models::EntityModel> comparator = new Java.Lang::Comparator<Explorer.Models::EntityModel>(){
		        @Override
		        public int compare(Explorer.Models::EntityModel entity1, Explorer.Models::EntityModel entity2){
		            final EasSelectorAddonsTables:Table:Model tableModel1 = (EasSelectorAddonsTables:Table:Model)entity1;
		            final EasSelectorAddonsTables:Table:Model tableModel2 = (EasSelectorAddonsTables:Table:Model)entity2;
		            if (tableModel1.isValid.Value.booleanValue() && !tableModel2.isValid.Value.booleanValue()){
		                return -1;
		            }
		            if (!tableModel1.isValid.Value.booleanValue() && tableModel2.isValid.Value.booleanValue()){
		                return 1;
		            }
		            return tableModel1.tableName.Value.compareTo(tableModel2.tableName.Value);
		        }
		    };
		    java.util.Collections.sort(entities,comparator);
		    clearRows();
		    addRows(entities);
		}

	}

	/*Radix::SelectorAddons::EasSelectorAddonsTables:Base:Model:onCommand_moveDown-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddonsTables:Base:Model:onCommand_moveDown")
	private final  void onCommand_moveDown (org.radixware.ads.SelectorAddons.Dlg.explorer.EasSelectorAddonsTablesGroup.MoveDown command) {
		executeMoveCommand(idof[EasSelectorAddons:moveDown]);
	}

	/*Radix::SelectorAddons::EasSelectorAddonsTables:Base:Model:onCommand_moveUp-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddonsTables:Base:Model:onCommand_moveUp")
	private final  void onCommand_moveUp (org.radixware.ads.SelectorAddons.Dlg.explorer.EasSelectorAddonsTablesGroup.MoveUp command) {
		executeMoveCommand(idof[EasSelectorAddons:moveUp]);
	}

	/*Radix::SelectorAddons::EasSelectorAddonsTables:Base:Model:executeMoveCommand-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddonsTables:Base:Model:executeMoveCommand")
	private final  void executeMoveCommand (org.radixware.kernel.common.types.Id commandId) {
		if (isSelectorAddonSelected()){
		    final Client.Types::IProgressHandle handle = getEnvironment().getProgressHandleManager().newStandardProgressHandle();
		    handle.startProgress("Moving",true);
		    try {        
		        GroupView.getCurrentEntity().getCommand(commandId).execute();
		        if (!handle.wasCanceled()){
		            reread();
		        }
		    } catch(Exceptions::ServiceClientException e){
		        showException(e);
		    }
		    finally{
		        handle.finishProgress();
		    }
		}
	}

	/*Radix::SelectorAddons::EasSelectorAddonsTables:Base:Model:onLeaveCurrentEntity-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddonsTables:Base:Model:onLeaveCurrentEntity")
	public published  void onLeaveCurrentEntity () {
		if (isEmpty()){
		    getCommand(idof[EasSelectorAddonsTablesGroup:moveUp]).setEnabled(false);
		    getCommand(idof[EasSelectorAddonsTablesGroup:moveDown]).setEnabled(false);
		}
	}

	/*Radix::SelectorAddons::EasSelectorAddonsTables:Base:Model:onSetCurrentEntity-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddonsTables:Base:Model:onSetCurrentEntity")
	public published  void onSetCurrentEntity (org.radixware.kernel.common.client.models.EntityModel entity) {
		if (isSelectorAddonSelected()){         
		    final Explorer.Models::GroupModel currentGroup = GroupView.getGroupModel();
		    final boolean isUpdateRestricted = currentGroup.getRestrictions().getIsUpdateRestricted();
		    try {
		        if (isUpdateRestricted) {
		            getCommand(idof[EasSelectorAddonsTablesGroup:moveUp]).setEnabled(false);
		            getCommand(idof[EasSelectorAddonsTablesGroup:moveDown]).setEnabled(false);
		            return;
		        } else {
		            final Explorer.Types::Pid curPid = entity.getPid();
		            final Explorer.Types::Pid lastEntityPid = currentGroup.getEntity(currentGroup.getEntitiesCount()-1).getPid();
		            final boolean isFirst = currentGroup.getEntity(0).getPid().equals(curPid);
		            final boolean isLast = !currentGroup.hasMoreRows() && lastEntityPid.equals(curPid);
		            getCommand(idof[EasSelectorAddonsTablesGroup:moveUp]).setEnabled(!isFirst);
		            getCommand(idof[EasSelectorAddonsTablesGroup:moveDown]).setEnabled(!isLast);
		        }
		    } catch(Exception e) {
		        showException(e);
		    }
		}
		else{
		    getCommand(idof[EasSelectorAddonsTablesGroup:moveUp]).setEnabled(false);
		    getCommand(idof[EasSelectorAddonsTablesGroup:moveDown]).setEnabled(false);
		}
	}

	/*Radix::SelectorAddons::EasSelectorAddonsTables:Base:Model:isSelectorAddonSelected-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddonsTables:Base:Model:isSelectorAddonSelected")
	private final  boolean isSelectorAddonSelected () {
		return GroupView != null && (GroupView.getCurrentEntity() instanceof EasSelectorAddons:AbstractBase:Model);
	}
	public final class MoveUp extends org.radixware.ads.SelectorAddons.Dlg.explorer.EasSelectorAddonsTablesGroup.MoveUp{
		protected MoveUp(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_moveUp( this );
		}

	}

	public final class MoveDown extends org.radixware.ads.SelectorAddons.Dlg.explorer.EasSelectorAddonsTablesGroup.MoveDown{
		protected MoveDown(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_moveDown( this );
		}

	}



















}

/* Radix::SelectorAddons::EasSelectorAddonsTables:Base:Model - Desktop Meta*/

/*Radix::SelectorAddons::EasSelectorAddonsTables:Base:Model-Group Model Class*/

package org.radixware.ads.SelectorAddons.Dlg.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Base:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("agm56JKKZ43JBBZHDYQC7CW3QFFCI"),
						"Base:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::SelectorAddons::EasSelectorAddonsTables:Base:Model:Properties-Properties*/
						null,
						null,
						null,
						null,
						null,
						null,
						false,
						false,
						false
						);
}

/* Radix::SelectorAddons::EasSelectorAddonsTables:ByTable:Model - Desktop Executable*/

/*Radix::SelectorAddons::EasSelectorAddonsTables:ByTable:Model-Filter Model Class*/

package org.radixware.ads.SelectorAddons.Dlg.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddonsTables:ByTable:Model")
public class ByTable:Model  extends org.radixware.kernel.common.client.models.FilterModel  {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return ByTable:Model_mi.rdxMeta; }



	public ByTable:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.filters.RadFilterDef def){super(userSession,def);}

	/*Radix::SelectorAddons::EasSelectorAddonsTables:ByTable:Model:Nested classes-Nested Classes*/

	/*Radix::SelectorAddons::EasSelectorAddonsTables:ByTable:Model:Properties-Properties*/








	/*Radix::SelectorAddons::EasSelectorAddonsTables:ByTable:Model:Methods-Methods*/

	/*Radix::SelectorAddons::EasSelectorAddonsTables:ByTable:Model:createPropertyEditor-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddonsTables:ByTable:Model:createPropertyEditor")
	public published  org.radixware.kernel.common.client.widgets.IModelWidget createPropertyEditor (org.radixware.kernel.common.types.Id propertyId) {
		if (idof[EasSelectorAddonsTables:ByTable:tableGuid].equals(propertyId)){
		    return new PropSqmlTableEditor(tableGuid);
		}
		return super.createPropertyEditor(propertyId);
	}

	/*Radix::SelectorAddons::EasSelectorAddonsTables:ByTable:Model:afterApply-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddonsTables:ByTable:Model:afterApply")
	public published  void afterApply () {
		super.afterApply();
		final Explorer.Models::GroupModel group = getFilterContext().ownerGroup;
		if (!group.isEmpty()){    
		    final Explorer.Widgets::SelectorTree tree =  (Explorer.Widgets::SelectorTree)group.getGroupView().getSelectorWidget();
		    if (tree.currentIndex()!=null){
		        tree.expand(tree.currentIndex());
		    }
		}
	}

	/*Radix::SelectorAddons::EasSelectorAddonsTables:ByTable:tableGuid:tableGuid-Presentation Property*/




	public class TableGuid extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public TableGuid(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddonsTables:ByTable:tableGuid:tableGuid")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddonsTables:ByTable:tableGuid:tableGuid")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public TableGuid getTableGuid(){return (TableGuid)getProperty(prmXNHKWGPHGRCU3KXHU7XM2LWSW4);}






}

/* Radix::SelectorAddons::EasSelectorAddonsTables:ByTable:Model - Desktop Meta*/

/*Radix::SelectorAddons::EasSelectorAddonsTables:ByTable:Model-Filter Model Class*/

package org.radixware.ads.SelectorAddons.Dlg.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class ByTable:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("fmcFSDKA7I7GZAGRIUY3XWZEMM4MI"),
						"ByTable:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::SelectorAddons::EasSelectorAddonsTables:ByTable:Model:Properties-Properties*/
						null,
						null,
						null,
						null,
						null,
						null,
						false,
						false,
						false
						);
}

/* Radix::SelectorAddons::EasSelectorAddonsTables - Localizing Bundle */
package org.radixware.ads.SelectorAddons.Dlg.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class EasSelectorAddonsTables - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Table");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Таблица");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2DUNOPW7H5F6XI6AAT2HAANJRY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Moving");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Перемещение");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3EM3N6WQYNAZTAK5OWNTGE5JPE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"By table");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"По таблице");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDBYAOTHWXVCYNBBTK7KYIR75TU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Filters");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Фильтры");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsI36YOGO6OZESLHIYFZII2KCWBU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Import Filters");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Импорт фильтров");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsIUHTDUR5Z5BEDHL4S6EECATLRQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"For current table only");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Только для текущей таблицы");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLJRRCLS45JE2BCYE3KGEP24G2A"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Table");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Таблица");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXD7X4P6ESFHLTED6A362QM6MLA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Common Filters");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Общие фильтры");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZBU2CZWYKVFW7IOOI5J7CJNTS4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(EasSelectorAddonsTables - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbaecYXWA57SVWJDPNDY4FWY6VBBTKA"),"EasSelectorAddonsTables - Localizing Bundle",$$$items$$$);
}
