
/* Radix::SelectorAddons::EasSelectorAddons.ColorScheme - Server Executable*/

/*Radix::SelectorAddons::EasSelectorAddons.ColorScheme-Application Class*/

package org.radixware.ads.SelectorAddons.Dlg.server;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons.ColorScheme")
public published class EasSelectorAddons.ColorScheme  extends org.radixware.ads.SelectorAddons.Dlg.server.EasSelectorAddons  implements org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {


	/**Metainformation accessor method*/
	@SuppressWarnings("unused")
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return EasSelectorAddons.ColorScheme_mi.rdxMeta;}

	/*Radix::SelectorAddons::EasSelectorAddons.ColorScheme:Nested classes-Nested Classes*/

	/*Radix::SelectorAddons::EasSelectorAddons.ColorScheme:Properties-Properties*/

	/*Radix::SelectorAddons::EasSelectorAddons.ColorScheme:ufColor-User Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons.ColorScheme:ufColor",line=41)
	public published  org.radixware.ads.SelectorAddons.Dlg.server.UserFunc.Coloring getUfColor() {
		return ufColor;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons.ColorScheme:ufColor",line=47)
	public published   void setUfColor(org.radixware.ads.SelectorAddons.Dlg.server.UserFunc.Coloring val) {
		ufColor = val;
	}

	/*Radix::SelectorAddons::EasSelectorAddons.ColorScheme:isActive-Column-Based Property*/












































	/*Radix::SelectorAddons::EasSelectorAddons.ColorScheme:Methods-Methods*/

	/*Radix::SelectorAddons::EasSelectorAddons.ColorScheme:beforeCreate-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons.ColorScheme:beforeCreate",line=102)
	protected published  boolean beforeCreate (org.radixware.kernel.server.types.Entity src) {
		final Types::Id newSchemeId = Types::Id.Factory.newInstance(Meta::DefinitionIdPrefix:COLOR_SCHEME);
		guid = newSchemeId.toString();
		title = tableName;
		return super.beforeCreate(src);
	}

	/*Radix::SelectorAddons::EasSelectorAddons.ColorScheme:afterCreate-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons.ColorScheme:afterCreate",line=113)
	protected published  void afterCreate (org.radixware.kernel.server.types.Entity src) {
		super.afterCreate(src);

		if(ufColor==null){
		    UserFunc.Coloring uf = new UserFunc.Coloring();
		    uf.init();
		    uf.upDefId = idof[EasSelectorAddons.ColorScheme:ufColor].toString();
		    uf.upOwnerEntityId = idof[EasSelectorAddons.ColorScheme].toString();
		    uf.upOwnerPid = this.Pid.toString();
		    ufColor = uf;
		}

	}

	/*Radix::SelectorAddons::EasSelectorAddons.ColorScheme:color-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons.ColorScheme:color",line=130)
	public published  org.radixware.kernel.common.enums.ESelectorRowStyle color (org.radixware.kernel.server.types.Entity entity) {
		if(ufColor!=null){
		    return ufColor.color(entity);
		}
		return Explorer.Env::SelectorStyle:Normal;
	}

	/*Radix::SelectorAddons::EasSelectorAddons.ColorScheme:isOneForTable-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons.ColorScheme:isOneForTable",line=141)
	public  boolean isOneForTable () {
		return true;
	}


	@Override
	public org.radixware.kernel.server.types.FormHandler.NextDialogsRequest execCommand(org.radixware.kernel.common.types.Id cmdId, org.radixware.kernel.common.types.Id propId, org.apache.xmlbeans.XmlObject input, org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValsById, org.apache.xmlbeans.XmlObject output) throws org.radixware.kernel.common.exceptions.AppException,java.lang.InterruptedException{

			return super.execCommand(cmdId,propId,input,newPropValsById,output);
	}


}

/* Radix::SelectorAddons::EasSelectorAddons.ColorScheme - Server Meta*/

/*Radix::SelectorAddons::EasSelectorAddons.ColorScheme-Application Class*/

package org.radixware.ads.SelectorAddons.Dlg.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class EasSelectorAddons.ColorScheme_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("aclE65CQY2JMRHAVFOBY4ESCWHFPM"),"EasSelectorAddons.ColorScheme",null,

						org.radixware.kernel.common.enums.EClassType.APPLICATION,

						/*Radix::SelectorAddons::EasSelectorAddons.ColorScheme:Presentations-Entity Object Presentations*/
						/*Class presentation attributes*/
						 new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef(/*Owner Class Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("aclE65CQY2JMRHAVFOBY4ESCWHFPM"),
							/*Owner Class Name*/
							"EasSelectorAddons.ColorScheme",
							/*Title Id*/
							null,
							/*Property presentations*/

							/*Radix::SelectorAddons::EasSelectorAddons.ColorScheme:Properties-Properties*/
							new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef[]{

									/*Radix::SelectorAddons::EasSelectorAddons.ColorScheme:ufColor:PropertyPresentation-Object Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadParentTitlePropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruN6MY67NUT5AGJCCNHQ3UOFRGAA"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,null,null,null,null,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr7IKMB24XZHOBDCMTAALOMT5GDM")},null,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr7IKMB24XZHOBDCMTAALOMT5GDM")},java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.TITLE,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::SelectorAddons::EasSelectorAddons.ColorScheme:isActive:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colZ23Q7UUURBEAHGCRRSQEKZH76I"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.EDITING,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT))
							},
							/*Commands*/
							new org.radixware.kernel.server.meta.presentations.RadCommandDef[]{

									new org.radixware.kernel.server.meta.presentations.RadCommandDef(
									/*Radix::SelectorAddons::EasSelectorAddons.ColorScheme:export-Object Command*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdC2ZBUFTTGZFAJMIK2KU5BIEZLY"),"export",org.radixware.kernel.common.enums.ECommandScope.OBJECT,null,org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_FIXED,org.radixware.kernel.common.enums.ECommandNature.LOCAL,org.radixware.kernel.common.types.Id.Factory.loadFrom("aclE65CQY2JMRHAVFOBY4ESCWHFPM"),false,true)
							},
							/*Sortings*/
							null,
							/*Filters*/
							null,
							/*Editor presentations*/
							new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef[]{

									new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef(
									/*Radix::SelectorAddons::EasSelectorAddons.ColorScheme:Edit-Editor Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprP5IRGRSUH5BT3G2DLUSEU5ADGE"),
									"Edit",
									org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprFGKCGE2H6FGXDD4YKLBKW77X3U"),
									36002,
									null,

									/*Radix::SelectorAddons::EasSelectorAddons.ColorScheme:Edit:Children-Explorer Items*/
										null
									,
									null,
									(org.radixware.kernel.server.meta.presentations.RadPropertyPresentationAttributes[]) null,
									org.radixware.kernel.common.enums.EEditorPresentationRightsInheritanceMode.NONE,
									null),

									new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef(
									/*Radix::SelectorAddons::EasSelectorAddons.ColorScheme:Create-Editor Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("epr44NA5ZWFTRFQJLAURA7XH2IOSI"),
									"Create",
									org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprFGKCGE2H6FGXDD4YKLBKW77X3U"),
									36016,
									null,

									/*Radix::SelectorAddons::EasSelectorAddons.ColorScheme:Create:Children-Explorer Items*/
										null
									,
									null,
									(org.radixware.kernel.server.meta.presentations.RadPropertyPresentationAttributes[]) null,
									org.radixware.kernel.common.enums.EEditorPresentationRightsInheritanceMode.NONE,
									null)
							},
							/*Selector presentations*/
							new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef[]{

									new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef(
									/*Radix::SelectorAddons::EasSelectorAddons.ColorScheme:General-Selector Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("spr5KTSNVEHEJDPNIBLRWRMAU7NXY"),"General",org.radixware.kernel.common.types.Id.Factory.loadFrom("sprIIEIWQINNRCG5OAYT2TYA6NNE4"),42,new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn[]{

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdR2V6YU635RAWVK2EA2HXJIY4Q4"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col6QH5TILJHRDDTC4CHYLQ6HLECA"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdJWOOWTAQA5EPHJHYBAYNLMEKAU"),false),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colZ23Q7UUURBEAHGCRRSQEKZH76I"),false),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDCQEQLHEC5F6VO633LHAE6BDMQ"),false)
									},new org.radixware.kernel.server.meta.presentations.RadConditionDef("<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:PropSqlName TableId=\"aecHQSOHRWZH5ESLD2Y6IVF6XFBE4\" PropId=\"col36K2MUPKPRGPPAGLVPAUJ52PQE\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> = </xsc:Sql></xsc:Item><xsc:Item><xsc:Id Path=\"aclE65CQY2JMRHAVFOBY4ESCWHFPM\"/></xsc:Item></xsc:Sqml>","<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),null,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprP5IRGRSUH5BT3G2DLUSEU5ADGE")},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr44NA5ZWFTRFQJLAURA7XH2IOSI")},null,org.radixware.kernel.common.types.Id.Factory.loadFrom("eccSV4WQAEGMZGXJGAMUBWVTMQSW4"))
							},
							/*Default selector presentation Id*/
							null,
							/*Presentation Map*/
							org.radixware.kernel.common.utils.Maps.fromArrays(new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprFGKCGE2H6FGXDD4YKLBKW77X3U")},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprP5IRGRSUH5BT3G2DLUSEU5ADGE")}),
							/*Object title format*/
							null,
							/*Class catalogs*/
							new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog[]{

									new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog(
									/*Radix::SelectorAddons::EasSelectorAddons.ColorScheme:General-Static Class Catalog*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eccSV4WQAEGMZGXJGAMUBWVTMQSW4"),org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.ELinkMode.FINAL,new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.Item[]{
										new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.ClassRef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aclE65CQY2JMRHAVFOBY4ESCWHFPM"),null,10.0,false,org.radixware.kernel.common.types.Id.Factory.loadFrom("aclE65CQY2JMRHAVFOBY4ESCWHFPM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsSSLHQ4NBB5EFXGMZC7GO4RDZK4"))}
									)
							},
							/*Restrictions*/
							null,null,0),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblHQSOHRWZH5ESLD2Y6IVF6XFBE4"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHQSOHRWZH5ESLD2Y6IVF6XFBE4"),

						/*Radix::SelectorAddons::EasSelectorAddons.ColorScheme:Properties-Properties*/
						new org.radixware.kernel.server.meta.clazzes.RadPropDef[]{

								/*Radix::SelectorAddons::EasSelectorAddons.ColorScheme:ufColor-User Property*/

								new org.radixware.kernel.server.meta.clazzes.RadUserRefPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruN6MY67NUT5AGJCCNHQ3UOFRGAA"),"ufColor",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDNXNYRJEPND5NLJF3ON36TY6XI"),org.radixware.kernel.common.enums.EValType.OBJECT,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("tblHQSOHRWZH5ESLD2Y6IVF6XFBE4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aclMYBG7VIFNBEHHJR7PSH3F2IFHY"),false,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::SelectorAddons::EasSelectorAddons.ColorScheme:isActive-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colZ23Q7UUURBEAHGCRRSQEKZH76I"),"isActive",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsK5GGDWOAJZGO3DWPLP47B7RXSU"),org.radixware.kernel.common.enums.EValType.BOOL,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("0")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE)
						},

						/*Radix::SelectorAddons::EasSelectorAddons.ColorScheme:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthFMXUFM5J25E4TNM4PR73F6V3E4"),"beforeCreate",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("src",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprCPHUVB24KFAYVHDDHD7CLBD6HE"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth6KS7LCP4W5AW5OCCJHH5KGO4YQ"),"afterCreate",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("src",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprDQHEP3N3MVHHZB5QLKXPA5KVFI"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthEA22RKNUDNGEJBOX6JUBJTUT6E"),"color",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("entity",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprT7BDPKKW6VGUFAG3T3RB3AULPY"))
								},org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthZ7M55AIGH5BB3F2TP3PLQP7X3A"),"isOneForTable",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE)
						},
						null,
						null,null,null,false);
}

/* Radix::SelectorAddons::EasSelectorAddons.ColorScheme - Desktop Executable*/

/*Radix::SelectorAddons::EasSelectorAddons.ColorScheme-Application Class*/

package org.radixware.ads.SelectorAddons.Dlg.explorer;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons.ColorScheme")
public interface EasSelectorAddons.ColorScheme   extends org.radixware.ads.SelectorAddons.Dlg.explorer.EasSelectorAddons  {

































































	/*Radix::SelectorAddons::EasSelectorAddons.ColorScheme:ufColor:ufColor-Presentation Property*/


	public class UfColor extends org.radixware.kernel.common.client.models.items.properties.PropertyObject{
		public UfColor(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadParentRefPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons.ColorScheme:ufColor:ufColor",line=419)
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons.ColorScheme:ufColor:ufColor",line=425)
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public UfColor getUfColor();
	public static class Export extends org.radixware.kernel.common.client.models.items.Command{
		protected Export(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}

	}



}

/* Radix::SelectorAddons::EasSelectorAddons.ColorScheme - Desktop Meta*/

/*Radix::SelectorAddons::EasSelectorAddons.ColorScheme-Application Class*/

package org.radixware.ads.SelectorAddons.Dlg.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class EasSelectorAddons.ColorScheme_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::SelectorAddons::EasSelectorAddons.ColorScheme:Presentations-Entity Object Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aclE65CQY2JMRHAVFOBY4ESCWHFPM"),
			"Radix::SelectorAddons::EasSelectorAddons.ColorScheme",
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHQSOHRWZH5ESLD2Y6IVF6XFBE4"),
			null,
			null,
			null,null,null,0,

			/*Radix::SelectorAddons::EasSelectorAddons.ColorScheme:Properties-Properties*/
			new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

					/*Radix::SelectorAddons::EasSelectorAddons.ColorScheme:ufColor:PropertyPresentation-Object Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruN6MY67NUT5AGJCCNHQ3UOFRGAA"),
						"ufColor",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclE65CQY2JMRHAVFOBY4ESCWHFPM"),
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
						false,false,
						false,
						false,
						null,
						false,
						null,
						null,
						null,
						null,
						true,-1,-1,1,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclMYBG7VIFNBEHHJR7PSH3F2IFHY"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblJ6SOXKD3ZHOBDCMTAALOMT5GDM"),
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr7IKMB24XZHOBDCMTAALOMT5GDM")},
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr7IKMB24XZHOBDCMTAALOMT5GDM")},
						null,
						0L,
						0L,false,false),

					/*Radix::SelectorAddons::EasSelectorAddons.ColorScheme:isActive:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colZ23Q7UUURBEAHGCRRSQEKZH76I"),
						"isActive",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsK5GGDWOAJZGO3DWPLP47B7RXSU"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclE65CQY2JMRHAVFOBY4ESCWHFPM"),
						org.radixware.kernel.common.enums.EValType.BOOL,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						6,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("0"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::SelectorAddons::EasSelectorAddons.ColorScheme:isActive:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aclE65CQY2JMRHAVFOBY4ESCWHFPM"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false)
			},
			new org.radixware.kernel.common.client.meta.RadCommandDef[]{
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::SelectorAddons::EasSelectorAddons.ColorScheme:export-Object Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdC2ZBUFTTGZFAJMIK2KU5BIEZLY"),
						"export",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclE65CQY2JMRHAVFOBY4ESCWHFPM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYNRRXNV3SBGMDOO4GRGJR3NUAQ"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("imgYGRKUM6D5NFB7HJSZFG3KGBC6M"),
						null,
						org.radixware.kernel.common.enums.ECommandNature.LOCAL,
						false,
						false,
						false,
						org.radixware.kernel.common.enums.ECommandScope.OBJECT,
						null,
						org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_FIXED,
						true)
			},
			null,
			null,
			null,
			new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprP5IRGRSUH5BT3G2DLUSEU5ADGE"),org.radixware.kernel.common.types.Id.Factory.loadFrom("epr44NA5ZWFTRFQJLAURA7XH2IOSI"),org.radixware.kernel.common.types.Id.Factory.loadFrom("spr5KTSNVEHEJDPNIBLRWRMAU7NXY")},
			true,false,false);
}

/* Radix::SelectorAddons::EasSelectorAddons.ColorScheme - Web Meta*/

/*Radix::SelectorAddons::EasSelectorAddons.ColorScheme-Application Class*/

package org.radixware.ads.SelectorAddons.Dlg.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class EasSelectorAddons.ColorScheme_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::SelectorAddons::EasSelectorAddons.ColorScheme:Presentations-Entity Object Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aclE65CQY2JMRHAVFOBY4ESCWHFPM"),
			"Radix::SelectorAddons::EasSelectorAddons.ColorScheme",
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHQSOHRWZH5ESLD2Y6IVF6XFBE4"),
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

/* Radix::SelectorAddons::EasSelectorAddons.ColorScheme:Edit - Desktop Meta*/

/*Radix::SelectorAddons::EasSelectorAddons.ColorScheme:Edit-Editor Presentation*/

package org.radixware.ads.SelectorAddons.Dlg.explorer;
public final class Edit_mi{
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new org.radixware.kernel.common.client.meta.RadEditorPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprP5IRGRSUH5BT3G2DLUSEU5ADGE"),
	"Edit",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
	org.radixware.kernel.common.types.Id.Factory.loadFrom("eprFGKCGE2H6FGXDD4YKLBKW77X3U"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("aclE65CQY2JMRHAVFOBY4ESCWHFPM"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("tblHQSOHRWZH5ESLD2Y6IVF6XFBE4"),
	null,
	org.radixware.kernel.common.types.Id.Factory.loadFrom("img2T4NN2ADWJERPPHSWK3TWXEYLE"),

	/*Radix::SelectorAddons::EasSelectorAddons.ColorScheme:Edit:Editor Pages-Editor Presentation Pages*/
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

			/*Radix::SelectorAddons::EasSelectorAddons.ColorScheme:Edit:General-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgNGTPEA3UNBBS5GNBD3DZDDVPYQ"),"General",org.radixware.kernel.common.types.Id.Factory.loadFrom("aclE65CQY2JMRHAVFOBY4ESCWHFPM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5ZKYUPANWFEILHCLZXQT5WDZYI"),null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDCQEQLHEC5F6VO633LHAE6BDMQ"),0,0,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colZ23Q7UUURBEAHGCRRSQEKZH76I"),0,1,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colVTTB5PHSCFG5TAUCLOI27MO5XM"),0,2,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colTZ42HZBXFFBPVL6P4MTIMOAHQE"),0,3,1,false,false)
			},null),

			/*Radix::SelectorAddons::EasSelectorAddons.ColorScheme:Edit:Function-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadCustomEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgP4OZAQFJS5C5ZOAAVMNDPS24WU"),"Function",org.radixware.kernel.common.types.Id.Factory.loadFrom("aclE65CQY2JMRHAVFOBY4ESCWHFPM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVPEYCB43MJHINISGOXUBJDV6AM"),null,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("cepEISX76LKWFAGBK4ND7WGBSTTQI"))
	},
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgNGTPEA3UNBBS5GNBD3DZDDVPYQ")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgP4OZAQFJS5C5ZOAAVMNDPS24WU")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgZG6UTMVC6ND6TPIYVCGMT3YWIY"))}
	,

	/*Radix::SelectorAddons::EasSelectorAddons.ColorScheme:Edit:Children-Explorer Items*/
		null
	,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
	null,
	0,
	null,
	(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
	36002,0,0);
}
/* Radix::SelectorAddons::EasSelectorAddons.ColorScheme:Edit:Model - Desktop Executable*/

/*Radix::SelectorAddons::EasSelectorAddons.ColorScheme:Edit:Model-Entity Model Class*/

package org.radixware.ads.SelectorAddons.Dlg.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons.ColorScheme:Edit:Model")
public class Edit:Model  extends org.radixware.ads.SelectorAddons.Dlg.explorer.EasSelectorAddons.ColorScheme.EasSelectorAddons.ColorScheme_DefaultModel.eprFGKCGE2H6FGXDD4YKLBKW77X3U_ModelAdapter {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return Edit:Model_mi.rdxMeta; }



	public Edit:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadEditorPresentationDef def){super(userSession,def);}

	/*Radix::SelectorAddons::EasSelectorAddons.ColorScheme:Edit:Model:Nested classes-Nested Classes*/

	/*Radix::SelectorAddons::EasSelectorAddons.ColorScheme:Edit:Model:Properties-Properties*/

	/*Radix::SelectorAddons::EasSelectorAddons.ColorScheme:Edit:Model:Methods-Methods*/

	/*Radix::SelectorAddons::EasSelectorAddons.ColorScheme:Edit:Model:onCommand_export-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons.ColorScheme:Edit:Model:onCommand_export",line=650)
	public  void onCommand_export (org.radixware.ads.SelectorAddons.Dlg.explorer.EasSelectorAddons.ColorScheme.Export command) {
		/*final  fileFilter = ;        
		final  filePath = com.trolltech.qt.gui.QFileDialog.getSaveFileName(()EntityView, , com.trolltech.qt.core.QDir.homePath(),
		                     new com.trolltech.qt.gui.QFileDialog.Filter(String.format(fileFilter, "*.xml", "*.*")));
		if (filePath!=null && !filePath.isEmpty()){
		    final  document = .Factory.newInstance();
		    final  filters = document.addNewCustomFilters();
		    final  filterXml = filters.addNewFilter();
		    filterXml.Id = .(.Value);
		    filterXml.Name = .Value;
		    filterXml.IsActive = .Value==null ? true : .Value.booleanValue();
		    filterXml.DefinitionType = ;
		    filterXml.TableId = .(.Value);
		    filterXml.BaseFilterId = .(.Value);
		    if (.Value!=null){
		        final java.util.List<> allowedPresentations = new java.util.LinkedList<>();
		        for ( presentationId: .Value){
		            allowedPresentations.add(.(presentationId));
		        }
		        filterXml.SelectorPresentations = allowedPresentations;
		    }    
		    try{
		        if (.Value!=null && !.Value.isEmpty())
		            filterXml.Condition = .parse(.Value);
		        if (.Value!=null && !.Value.isEmpty())
		            filterXml.Parameters = .parse(.Value);
		    }
		    catch( ex){
		        (ex);
		        return;
		    }
		    try{
		        document.save(new java.io.File(filePath));
		    }
		    catch( ex){
		        (ex);
		    }
		}*/
	}

	/*Radix::SelectorAddons::EasSelectorAddons.ColorScheme:Edit:Model:afterOpenEditorPageView-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons.ColorScheme:Edit:Model:afterOpenEditorPageView",line=694)
	public published  void afterOpenEditorPageView (org.radixware.kernel.common.types.Id pageId) {
		super.afterOpenEditorPageView(pageId);
		/*if(pageId.equals()){
		     table= ().(). (.(.Value));
		    if(table!=null){
		        .Value=table.();
		    }
		}*/
	}
	public final class Export extends org.radixware.ads.SelectorAddons.Dlg.explorer.EasSelectorAddons.ColorScheme.Export{
		protected Export(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_export( this );
		}

	}













}

/* Radix::SelectorAddons::EasSelectorAddons.ColorScheme:Edit:Model - Desktop Meta*/

/*Radix::SelectorAddons::EasSelectorAddons.ColorScheme:Edit:Model-Entity Model Class*/

package org.radixware.ads.SelectorAddons.Dlg.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Edit:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aemP5IRGRSUH5BT3G2DLUSEU5ADGE"),
						"Edit:Model",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aemFGKCGE2H6FGXDD4YKLBKW77X3U"),
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::SelectorAddons::EasSelectorAddons.ColorScheme:Edit:Model:Properties-Properties*/
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

/* Radix::SelectorAddons::EasSelectorAddons.ColorScheme:Edit:Function:View - Desktop Executable*/

/*Radix::SelectorAddons::EasSelectorAddons.ColorScheme:Edit:Function:View-Custom Page Editor for Desktop*/

package org.radixware.ads.SelectorAddons.Dlg.explorer;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons.ColorScheme:Edit:Function:View")
public class View extends org.radixware.kernel.explorer.views.EditorPageView {
	final private com.trolltech.qt.gui.QFont DEFAULT_FONT = org.radixware.kernel.explorer.text.ExplorerTextOptions.Factory.getDefault().getQFont();
	public View EditorPageView;
	public View getEditorPageView(){ return EditorPageView;}
	public org.radixware.kernel.explorer.widgets.EmbeddedEditor embeddedEditor;
	public org.radixware.kernel.explorer.widgets.EmbeddedEditor getEmbeddedEditor(){ return embeddedEditor;}
	public View(org.radixware.kernel.common.client.IClientEnvironment userSession,
	org.radixware.kernel.common.client.views.IView parent, org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef page) {
		super(userSession,(org.radixware.kernel.explorer.views.IExplorerView)parent, page);
	}
	public void open(org.radixware.kernel.common.client.models.Model model) {
		super.open(model);
		EditorPageView = this;
		EditorPageView.setObjectName("EditorPageView");
		EditorPageView.setGeometry(new com.trolltech.qt.core.QRect(0, 0, 600, 400));
		EditorPageView.setFont(DEFAULT_FONT);
		final com.trolltech.qt.gui.QGridLayout $layout1 = new com.trolltech.qt.gui.QGridLayout(EditorPageView);
		$layout1.setObjectName("gridLayout");
		$layout1.setContentsMargins(9, 9, 9, 9);
		$layout1.setSizeConstraint(com.trolltech.qt.gui.QLayout.SizeConstraint.SetDefaultConstraint);
		$layout1.setHorizontalSpacing(6);
		$layout1.setVerticalSpacing(6);
		embeddedEditor = new org.radixware.kernel.explorer.widgets.EmbeddedEditor(model.getEnvironment(),(org.radixware.kernel.common.client.models.items.properties.PropertyReference)getModel().getProperty(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruN6MY67NUT5AGJCCNHQ3UOFRGAA")));
		embeddedEditor.setParent(EditorPageView);
		embeddedEditor.setObjectName("embeddedEditor");
		embeddedEditor.setGeometry(new com.trolltech.qt.core.QRect(34, 15, 200, 200));
		embeddedEditor.setFont(DEFAULT_FONT);
		embeddedEditor.bind();
		$layout1.addWidget(embeddedEditor, 0, 0, 1, 1);
		opened.emit(this);
	}
	public final org.radixware.ads.SelectorAddons.Dlg.explorer.Edit:Model getModel() {
		return (org.radixware.ads.SelectorAddons.Dlg.explorer.Edit:Model) super.getModel();
	}

}

/* Radix::SelectorAddons::EasSelectorAddons.ColorScheme:Edit:Function:WebView - Web Executable*/

/*Radix::SelectorAddons::EasSelectorAddons.ColorScheme:Edit:Function:WebView-Rwt Custom Page Editor*/

package org.radixware.ads.SelectorAddons.Dlg.web;
@SuppressWarnings({"unchecked","rawtypes"})
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons.ColorScheme:Edit:Function:WebView")
public class WebView extends org.radixware.wps.views.editor.EditorPageView{
	public WebView widget;
	public WebView getWidget(){ return  widget;}
	public org.radixware.wps.views.editor.PropertiesGrid widget;
	public org.radixware.wps.views.editor.PropertiesGrid getWidget(){ return  widget;}
	public WebView(org.radixware.kernel.common.client.IClientEnvironment env,org.radixware.kernel.common.client.views.IView view
	,org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef page){
		super(env,view,page);
	}
	public void open(org.radixware.kernel.common.client.models.Model model){
		super.open(model);
		//============ Radix::SelectorAddons::EasSelectorAddons.ColorScheme:Edit:Function:WebView:widget ==============
		this.widget = this;
		widget.setWidth(600);
		widget.setHeight(400);
		//============ Radix::SelectorAddons::EasSelectorAddons.ColorScheme:Edit:Function:WebView:widget:widget ==============
		this.widget = new org.radixware.wps.views.editor.PropertiesGrid();
		widget.setObjectName("widget");
		this.widget.add(this.widget);
		widget.getAnchors().setTop(new org.radixware.wps.rwt.UIObject.Anchors.Anchor(0.0f,0));
		widget.getAnchors().setLeft(new org.radixware.wps.rwt.UIObject.Anchors.Anchor(0.0f,0));
		widget.getAnchors().setBottom(new org.radixware.wps.rwt.UIObject.Anchors.Anchor(1.0f,0));
		widget.getAnchors().setRight(new org.radixware.wps.rwt.UIObject.Anchors.Anchor(1.0f,0));
		this.wdgH32YM54FJNCBZFRU7N5LD3RTFA.bind();
		fireOpened();
	}
}

/* Radix::SelectorAddons::EasSelectorAddons.ColorScheme:Create - Desktop Meta*/

/*Radix::SelectorAddons::EasSelectorAddons.ColorScheme:Create-Editor Presentation*/

package org.radixware.ads.SelectorAddons.Dlg.explorer;
public final class Create_mi{
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new org.radixware.kernel.common.client.meta.RadEditorPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epr44NA5ZWFTRFQJLAURA7XH2IOSI"),
	"Create",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
	org.radixware.kernel.common.types.Id.Factory.loadFrom("eprFGKCGE2H6FGXDD4YKLBKW77X3U"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("aclE65CQY2JMRHAVFOBY4ESCWHFPM"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("tblHQSOHRWZH5ESLD2Y6IVF6XFBE4"),
	null,
	null,

	/*Radix::SelectorAddons::EasSelectorAddons.ColorScheme:Create:Editor Pages-Editor Presentation Pages*/
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

			/*Radix::SelectorAddons::EasSelectorAddons.ColorScheme:Create:General-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgSC5LVJVZTZFRFA7SC2HXZSTLBA"),"General",org.radixware.kernel.common.types.Id.Factory.loadFrom("aclE65CQY2JMRHAVFOBY4ESCWHFPM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsK2N3JZNUBJF5DDS2F63URY5RYU"),null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDCQEQLHEC5F6VO633LHAE6BDMQ"),0,0,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colZ23Q7UUURBEAHGCRRSQEKZH76I"),0,1,1,false,false)
			},null)
	},
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgSC5LVJVZTZFRFA7SC2HXZSTLBA")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgZG6UTMVC6ND6TPIYVCGMT3YWIY"))}
	,

	/*Radix::SelectorAddons::EasSelectorAddons.ColorScheme:Create:Children-Explorer Items*/
		null
	,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
	null,
	0,
	null,
	(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
	36016,0,0);
}
/* Radix::SelectorAddons::EasSelectorAddons.ColorScheme:Create:Model - Desktop Executable*/

/*Radix::SelectorAddons::EasSelectorAddons.ColorScheme:Create:Model-Entity Model Class*/

package org.radixware.ads.SelectorAddons.Dlg.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons.ColorScheme:Create:Model")
public class Create:Model  extends org.radixware.ads.SelectorAddons.Dlg.explorer.EasSelectorAddons.ColorScheme.EasSelectorAddons.ColorScheme_DefaultModel.eprFGKCGE2H6FGXDD4YKLBKW77X3U_ModelAdapter {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return Create:Model_mi.rdxMeta; }



	public Create:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadEditorPresentationDef def){super(userSession,def);}

	/*Radix::SelectorAddons::EasSelectorAddons.ColorScheme:Create:Model:Nested classes-Nested Classes*/

	/*Radix::SelectorAddons::EasSelectorAddons.ColorScheme:Create:Model:Properties-Properties*/

	/*Radix::SelectorAddons::EasSelectorAddons.ColorScheme:Create:Model:Methods-Methods*/


}

/* Radix::SelectorAddons::EasSelectorAddons.ColorScheme:Create:Model - Desktop Meta*/

/*Radix::SelectorAddons::EasSelectorAddons.ColorScheme:Create:Model-Entity Model Class*/

package org.radixware.ads.SelectorAddons.Dlg.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Create:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aem44NA5ZWFTRFQJLAURA7XH2IOSI"),
						"Create:Model",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aemFGKCGE2H6FGXDD4YKLBKW77X3U"),
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::SelectorAddons::EasSelectorAddons.ColorScheme:Create:Model:Properties-Properties*/
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

/* Radix::SelectorAddons::EasSelectorAddons.ColorScheme:General - Desktop Meta*/

/*Radix::SelectorAddons::EasSelectorAddons.ColorScheme:General-Selector Presentation*/

package org.radixware.ads.SelectorAddons.Dlg.explorer;
public final class General_mi extends org.radixware.kernel.common.client.meta.RadSelectorPresentationDef{
	public static final org.radixware.kernel.common.client.meta.RadSelectorPresentationDef rdxMeta = new General_mi();
	private General_mi(){
		super(org.radixware.kernel.common.types.Id.Factory.loadFrom("spr5KTSNVEHEJDPNIBLRWRMAU7NXY"),
		"General",
		org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("sprIIEIWQINNRCG5OAYT2TYA6NNE4"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("aclE65CQY2JMRHAVFOBY4ESCWHFPM"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("tblHQSOHRWZH5ESLD2Y6IVF6XFBE4"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQZVWALWLVRBWTAA3MOS24C5IYY"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("img2T4NN2ADWJERPPHSWK3TWXEYLE"),
		null,
		null,
		false,
		null,
		null,
		false,
		false,
		null,
		0,null,
		42,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr44NA5ZWFTRFQJLAURA7XH2IOSI")},
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprP5IRGRSUH5BT3G2DLUSEU5ADGE")},
		false,true,false,0,0);
		columns = new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn[]{

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdR2V6YU635RAWVK2EA2HXJIY4Q4"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col6QH5TILJHRDDTC4CHYLQ6HLECA"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdJWOOWTAQA5EPHJHYBAYNLMEKAU"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colZ23Q7UUURBEAHGCRRSQEKZH76I"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDCQEQLHEC5F6VO633LHAE6BDMQ"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null)
		};
	}
}
/* Radix::SelectorAddons::EasSelectorAddons.ColorScheme:General:Model - Desktop Executable*/

/*Radix::SelectorAddons::EasSelectorAddons.ColorScheme:General:Model-Group Model Class*/

package org.radixware.ads.SelectorAddons.Dlg.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons.ColorScheme:General:Model")
public class General:Model  extends org.radixware.ads.SelectorAddons.Dlg.explorer.EasSelectorAddons.ColorScheme.DefaultGroupModel {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return General:Model_mi.rdxMeta; }



	public General:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadSelectorPresentationDef def){super(userSession,def);}

	/*Radix::SelectorAddons::EasSelectorAddons.ColorScheme:General:Model:Nested classes-Nested Classes*/

	/*Radix::SelectorAddons::EasSelectorAddons.ColorScheme:General:Model:Properties-Properties*/

	/*Radix::SelectorAddons::EasSelectorAddons.ColorScheme:General:Model:Methods-Methods*/

	/*Radix::SelectorAddons::EasSelectorAddons.ColorScheme:General:Model:export-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::EasSelectorAddons.ColorScheme:General:Model:export",line=996)
	public  void export (Str filePath, Str tableGuid) throws java.lang.InterruptedException,org.radixware.kernel.common.exceptions.ServiceClientException {
		/*final  input = .newInstance();
		final  parameters = input.addNewExportParameters();
		parameters.FilePath = filePath;
		parameters.TableGuid = tableGuid;
		().().(this, null, , null, input);  */
	}


}

/* Radix::SelectorAddons::EasSelectorAddons.ColorScheme:General:Model - Desktop Meta*/

/*Radix::SelectorAddons::EasSelectorAddons.ColorScheme:General:Model-Group Model Class*/

package org.radixware.ads.SelectorAddons.Dlg.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class General:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("agm5KTSNVEHEJDPNIBLRWRMAU7NXY"),
						"General:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::SelectorAddons::EasSelectorAddons.ColorScheme:General:Model:Properties-Properties*/
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

/* Radix::SelectorAddons::EasSelectorAddons.ColorScheme - Localizing Bundle */
package org.radixware.ads.SelectorAddons.Dlg.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class EasSelectorAddons.ColorScheme - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Export Color Scheme");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Экспорт цветовой схемы");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls44BMNAPLERBKLBIIBR3627CHXM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"C:\\distrib\\trunk\\org.radixware\\ads\\SelectorAddons"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"General");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Общее");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5ZKYUPANWFEILHCLZXQT5WDZYI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"C:\\distrib\\trunk\\org.radixware\\ads\\SelectorAddons"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"XML Document (%s);;All Files (%s)");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"XML Документ (%s);;Все файлы (%s)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsB3LWLY7ZF5GY5M5Q7VGZFCVNLM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"C:\\distrib\\trunk\\org.radixware\\ads\\SelectorAddons"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"General");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Общее");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsK2N3JZNUBJF5DDS2F63URY5RYU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"C:\\distrib\\trunk\\org.radixware\\ads\\SelectorAddons"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Used");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Используется");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsK5GGDWOAJZGO3DWPLP47B7RXSU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"C:\\distrib\\trunk\\org.radixware\\ads\\SelectorAddons"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Color Schemes");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Цветовые схемы");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQZVWALWLVRBWTAA3MOS24C5IYY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"C:\\distrib\\trunk\\org.radixware\\ads\\SelectorAddons"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Color Schemes");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Цветовые схемы");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsSSLHQ4NBB5EFXGMZC7GO4RDZK4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"C:\\distrib\\trunk\\org.radixware\\ads\\SelectorAddons"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Function");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Функция");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVPEYCB43MJHINISGOXUBJDV6AM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"C:\\distrib\\trunk\\org.radixware\\ads\\SelectorAddons"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Export color scheme");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Экспортировать цветовую схему");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYNRRXNV3SBGMDOO4GRGJR3NUAQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"C:\\distrib\\trunk\\org.radixware\\ads\\SelectorAddons"));
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(EasSelectorAddons.ColorScheme - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbaclE65CQY2JMRHAVFOBY4ESCWHFPM"),"EasSelectorAddons.ColorScheme - Localizing Bundle",$$$items$$$);
}
