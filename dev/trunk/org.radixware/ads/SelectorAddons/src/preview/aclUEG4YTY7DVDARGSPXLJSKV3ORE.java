
/* Radix::SelectorAddons::CfgItem.EasFiltersGroup - Server Executable*/

/*Radix::SelectorAddons::CfgItem.EasFiltersGroup-Application Class*/

package org.radixware.ads.SelectorAddons.Dlg.server;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::CfgItem.EasFiltersGroup")
public published class CfgItem.EasFiltersGroup  extends org.radixware.ads.CfgManagement.server.CfgItem.Group  implements org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {


	/**Metainformation accessor method*/
	@SuppressWarnings("unused")
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return CfgItem.EasFiltersGroup_mi.rdxMeta;}

	/*Radix::SelectorAddons::CfgItem.EasFiltersGroup:Nested classes-Nested Classes*/

	/*Radix::SelectorAddons::CfgItem.EasFiltersGroup:Properties-Properties*/

	/*Radix::SelectorAddons::CfgItem.EasFiltersGroup:tableIdDoc-Dynamic Property*/



	protected org.radixware.schemas.types.StrDocument tableIdDoc=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::CfgItem.EasFiltersGroup:tableIdDoc",line=40)
	private final  org.radixware.schemas.types.StrDocument getTableIdDoc() {

		if (internal[tableIdDoc] == null && data != null)
		    try {
		        internal[tableIdDoc] = Arte::TypesXsd:StrDocument.Factory.parse(data.DomNode);
		    } catch (Exceptions::XmlException e) {
		        throw new AppError("Invalid data format:\n" + data.toString(), e);
		    }

		return internal[tableIdDoc];
	}































	/*Radix::SelectorAddons::CfgItem.EasFiltersGroup:Methods-Methods*/

	/*Radix::SelectorAddons::CfgItem.EasFiltersGroup:linkImpObject-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::CfgItem.EasFiltersGroup:linkImpObject",line=88)
	protected published  void linkImpObject () {
		;
	}

	/*Radix::SelectorAddons::CfgItem.EasFiltersGroup:extractExportData-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::CfgItem.EasFiltersGroup:extractExportData",line=96)
	protected published  void extractExportData (org.radixware.ads.CfgManagement.server.CfgExportData data) {
		EasSelectorAddons.Filters.exportAll(data, null, tableIdDoc != null ? tableIdDoc.Str : null);
	}

	/*Radix::SelectorAddons::CfgItem.EasFiltersGroup:load-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::CfgItem.EasFiltersGroup:load",line=104)
	protected published  void load (org.radixware.ads.CfgManagement.server.ICfgImportHelper helper) throws org.radixware.kernel.common.exceptions.AppException {
		;
	}


	@Override
	public org.radixware.kernel.server.types.FormHandler.NextDialogsRequest execCommand(org.radixware.kernel.common.types.Id cmdId, org.radixware.kernel.common.types.Id propId, org.apache.xmlbeans.XmlObject input, org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValsById, org.apache.xmlbeans.XmlObject output) throws org.radixware.kernel.common.exceptions.AppException,java.lang.InterruptedException{

			return super.execCommand(cmdId,propId,input,newPropValsById,output);
	}


}

/* Radix::SelectorAddons::CfgItem.EasFiltersGroup - Server Meta*/

/*Radix::SelectorAddons::CfgItem.EasFiltersGroup-Application Class*/

package org.radixware.ads.SelectorAddons.Dlg.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class CfgItem.EasFiltersGroup_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("aclUEG4YTY7DVDARGSPXLJSKV3ORE"),"CfgItem.EasFiltersGroup",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZBQS22C2V5EZRANU62VMFFFCSA"),

						org.radixware.kernel.common.enums.EClassType.APPLICATION,

						/*Radix::SelectorAddons::CfgItem.EasFiltersGroup:Presentations-Entity Object Presentations*/
						/*Class presentation attributes*/
						 new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef(/*Owner Class Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("aclUEG4YTY7DVDARGSPXLJSKV3ORE"),
							/*Owner Class Name*/
							"CfgItem.EasFiltersGroup",
							/*Title Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZBQS22C2V5EZRANU62VMFFFCSA"),
							/*Property presentations*/

							/*Radix::SelectorAddons::CfgItem.EasFiltersGroup:Properties-Properties*/
							null,
							/*Commands*/
							null,
							/*Sortings*/
							null,
							/*Filters*/
							null,
							/*Editor presentations*/
							new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef[]{

									new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef(
									/*Radix::SelectorAddons::CfgItem.EasFiltersGroup:Export-Editor Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("epr7GTWK6HHOZGOPEYFRB74NXZ2BU"),
									"Export",
									org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprJ2YTMJMIYZHL3HKDSW272LXO2M"),
									40114,
									null,

									/*Radix::SelectorAddons::CfgItem.EasFiltersGroup:Export:Children-Explorer Items*/
										null
									,
									null,
									(org.radixware.kernel.server.meta.presentations.RadPropertyPresentationAttributes[]) null,
									org.radixware.kernel.common.enums.EEditorPresentationRightsInheritanceMode.FROM_REPLACED,
									null),

									new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef(
									/*Radix::SelectorAddons::CfgItem.EasFiltersGroup:Import-Editor Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprW4R6UTRD6NAJPMMA3VREL5PUEQ"),
									"Import",
									org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprIFGJZ5IMOBC5BOPWMWBGQ6GI3M"),
									105648,
									null,

									/*Radix::SelectorAddons::CfgItem.EasFiltersGroup:Import:Children-Explorer Items*/
										null
									,
									null,
									(org.radixware.kernel.server.meta.presentations.RadPropertyPresentationAttributes[]) null,
									null,
									null)
							},
							/*Selector presentations*/
							null,
							/*Default selector presentation Id*/
							null,
							/*Presentation Map*/
							org.radixware.kernel.common.utils.Maps.fromArrays(new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprCUUYK4MIYVFVLCLQGXLUNZWINM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("eprIFGJZ5IMOBC5BOPWMWBGQ6GI3M")},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr7GTWK6HHOZGOPEYFRB74NXZ2BU"),org.radixware.kernel.common.types.Id.Factory.loadFrom("eprW4R6UTRD6NAJPMMA3VREL5PUEQ")}),
							/*Object title format*/
							null,
							/*Class catalogs*/
							null,
							/*Restrictions*/
							null,null,0),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblJ4WZRLJ23BGMBKQ2JBSOBEGBP4"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclUCWMPHKY3JETLBF7GYCXUMVJ4E"),

						/*Radix::SelectorAddons::CfgItem.EasFiltersGroup:Properties-Properties*/
						new org.radixware.kernel.server.meta.clazzes.RadPropDef[]{

								/*Radix::SelectorAddons::CfgItem.EasFiltersGroup:tableIdDoc-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd6PPZBKYBARHPREP5AQ7F5QYTCM"),"tableIdDoc",null,org.radixware.kernel.common.enums.EValType.XML,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadAccessorProxy.R_ACCESSOR_INSTANCE)
						},

						/*Radix::SelectorAddons::CfgItem.EasFiltersGroup:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthEBTC42HN7RENPIGWWSTFCC2IOQ"),"linkImpObject",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth7Z323JY4JFB7PAHXRDBVOX2EE4"),"extractExportData",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("data",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprBUZWPTFKMBFTRHUXT6N6ODNYEQ"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthHOVIQBNNZRERVBNIKFD56Y6OK4"),"load",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("helper",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr5TINUT3NSBFD5ATONDMO6XIO24"))
								},null)
						},
						null,
						null,null,null,false);
}

/* Radix::SelectorAddons::CfgItem.EasFiltersGroup - Desktop Executable*/

/*Radix::SelectorAddons::CfgItem.EasFiltersGroup-Application Class*/

package org.radixware.ads.SelectorAddons.Dlg.explorer;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::CfgItem.EasFiltersGroup")
public interface CfgItem.EasFiltersGroup   extends org.radixware.ads.CfgManagement.explorer.CfgItem.Group  {


































































}

/* Radix::SelectorAddons::CfgItem.EasFiltersGroup - Desktop Meta*/

/*Radix::SelectorAddons::CfgItem.EasFiltersGroup-Application Class*/

package org.radixware.ads.SelectorAddons.Dlg.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class CfgItem.EasFiltersGroup_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::SelectorAddons::CfgItem.EasFiltersGroup:Presentations-Entity Object Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aclUEG4YTY7DVDARGSPXLJSKV3ORE"),
			"Radix::SelectorAddons::CfgItem.EasFiltersGroup",
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aclUCWMPHKY3JETLBF7GYCXUMVJ4E"),
			null,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZBQS22C2V5EZRANU62VMFFFCSA"),null,null,0,

			/*Radix::SelectorAddons::CfgItem.EasFiltersGroup:Properties-Properties*/
			null,
			null,
			null,
			null,
			null,
			new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr7GTWK6HHOZGOPEYFRB74NXZ2BU"),org.radixware.kernel.common.types.Id.Factory.loadFrom("eprW4R6UTRD6NAJPMMA3VREL5PUEQ")},
			true,false,false);
}

/* Radix::SelectorAddons::CfgItem.EasFiltersGroup - Web Meta*/

/*Radix::SelectorAddons::CfgItem.EasFiltersGroup-Application Class*/

package org.radixware.ads.SelectorAddons.Dlg.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class CfgItem.EasFiltersGroup_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::SelectorAddons::CfgItem.EasFiltersGroup:Presentations-Entity Object Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aclUEG4YTY7DVDARGSPXLJSKV3ORE"),
			"Radix::SelectorAddons::CfgItem.EasFiltersGroup",
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aclUCWMPHKY3JETLBF7GYCXUMVJ4E"),
			null,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZBQS22C2V5EZRANU62VMFFFCSA"),null,null,0,
			null,
			null,
			null,
			null,
			null,
			null,
			false,false,false);
}

/* Radix::SelectorAddons::CfgItem.EasFiltersGroup:Export - Desktop Meta*/

/*Radix::SelectorAddons::CfgItem.EasFiltersGroup:Export-Editor Presentation*/

package org.radixware.ads.SelectorAddons.Dlg.explorer;
public final class Export_mi{
	private static final class Export_DEF extends org.radixware.kernel.common.client.meta.RadEditorPresentationDef{
		Export_DEF(){
			super(org.radixware.kernel.common.types.Id.Factory.loadFrom("epr7GTWK6HHOZGOPEYFRB74NXZ2BU"),
			"Export",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("eprJ2YTMJMIYZHL3HKDSW272LXO2M"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aclUEG4YTY7DVDARGSPXLJSKV3ORE"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("tblJ4WZRLJ23BGMBKQ2JBSOBEGBP4"),
			null,
			null,
			null,
			null,

			/*Radix::SelectorAddons::CfgItem.EasFiltersGroup:Export:Children-Explorer Items*/
				null
			,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
			null,
			0,
			null,
			(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
			40114,0,0);
		}
		@Override
		protected org.radixware.kernel.common.client.models.Model createModelImpl(org.radixware.kernel.common.client.IClientEnvironment userSession) {    return new org.radixware.ads.SelectorAddons.Dlg.explorer.CfgItem.EasFiltersGroup.CfgItem.EasFiltersGroup_DefaultModel.eprJ2YTMJMIYZHL3HKDSW272LXO2M_ModelAdapter(userSession,this);
		}
	}
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new Export_DEF(); 
;
}
/* Radix::SelectorAddons::CfgItem.EasFiltersGroup:Import - Desktop Meta*/

/*Radix::SelectorAddons::CfgItem.EasFiltersGroup:Import-Editor Presentation*/

package org.radixware.ads.SelectorAddons.Dlg.explorer;
public final class Import_mi{
	private static final class Import_DEF extends org.radixware.kernel.common.client.meta.RadEditorPresentationDef{
		Import_DEF(){
			super(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprW4R6UTRD6NAJPMMA3VREL5PUEQ"),
			"Import",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("eprIFGJZ5IMOBC5BOPWMWBGQ6GI3M"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aclUEG4YTY7DVDARGSPXLJSKV3ORE"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("tblJ4WZRLJ23BGMBKQ2JBSOBEGBP4"),
			null,
			null,
			null,
			null,

			/*Radix::SelectorAddons::CfgItem.EasFiltersGroup:Import:Children-Explorer Items*/
				null
			,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
			null,
			0,
			null,
			(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
			105648,0,0);
		}
		@Override
		protected org.radixware.kernel.common.client.models.Model createModelImpl(org.radixware.kernel.common.client.IClientEnvironment userSession) {    return new org.radixware.ads.SelectorAddons.Dlg.explorer.CfgItem.EasFiltersGroup.CfgItem.EasFiltersGroup_DefaultModel.eprIFGJZ5IMOBC5BOPWMWBGQ6GI3M_ModelAdapter(userSession,this);
		}
	}
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new Import_DEF(); 
;
}
/* Radix::SelectorAddons::CfgItem.EasFiltersGroup - Localizing Bundle */
package org.radixware.ads.SelectorAddons.Dlg.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class CfgItem.EasFiltersGroup - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Common Filter Group");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Группа общих фильтров");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZBQS22C2V5EZRANU62VMFFFCSA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"C:\\distrib\\trunk\\org.radixware\\ads\\SelectorAddons"));
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(CfgItem.EasFiltersGroup - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbaclUEG4YTY7DVDARGSPXLJSKV3ORE"),"CfgItem.EasFiltersGroup - Localizing Bundle",$$$items$$$);
}
