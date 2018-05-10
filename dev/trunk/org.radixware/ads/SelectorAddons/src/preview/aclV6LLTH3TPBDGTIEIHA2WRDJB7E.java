
/* Radix::SelectorAddons::CfgItem.EasFiltersSingle - Server Executable*/

/*Radix::SelectorAddons::CfgItem.EasFiltersSingle-Application Class*/

package org.radixware.ads.SelectorAddons.Dlg.server;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::CfgItem.EasFiltersSingle")
public published class CfgItem.EasFiltersSingle  extends org.radixware.ads.CfgManagement.server.CfgItem.Single  implements org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {


	/**Metainformation accessor method*/
	@SuppressWarnings("unused")
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return CfgItem.EasFiltersSingle_mi.rdxMeta;}

	/*Radix::SelectorAddons::CfgItem.EasFiltersSingle:Nested classes-Nested Classes*/

	/*Radix::SelectorAddons::CfgItem.EasFiltersSingle:Properties-Properties*/

	/*Radix::SelectorAddons::CfgItem.EasFiltersSingle:expObject-Dynamic Property*/



	protected org.radixware.ads.SelectorAddons.Dlg.server.EasSelectorAddons.Filters expObject=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::CfgItem.EasFiltersSingle:expObject")
	public published  org.radixware.ads.SelectorAddons.Dlg.server.EasSelectorAddons.Filters getExpObject() {

		return EasSelectorAddons.Filters.loadByPK(srcObjectPid, true);
	}

	/*Radix::SelectorAddons::CfgItem.EasFiltersSingle:impObject-User Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::CfgItem.EasFiltersSingle:impObject")
	public published  org.radixware.ads.SelectorAddons.Dlg.server.EasSelectorAddons.Filters getImpObject() {
		return impObject;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::CfgItem.EasFiltersSingle:impObject")
	public published   void setImpObject(org.radixware.ads.SelectorAddons.Dlg.server.EasSelectorAddons.Filters val) {

		internal[impObject] = val;
		objState = val == null ? CfgManagement::CfgObjState:New : CfgManagement::CfgObjState:Exists;
	}

	/*Radix::SelectorAddons::CfgItem.EasFiltersSingle:easFilterGuid-Dynamic Property*/



	protected Str easFilterGuid=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::CfgItem.EasFiltersSingle:easFilterGuid")
	private final  Str getEasFilterGuid() {

		return myData.CustomFilters.FilterList.get(0).Id.toString();
	}

	/*Radix::SelectorAddons::CfgItem.EasFiltersSingle:myData-Dynamic Property*/



	protected org.radixware.schemas.groupsettings.CustomFiltersDocument myData=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::CfgItem.EasFiltersSingle:myData")
	private final  org.radixware.schemas.groupsettings.CustomFiltersDocument getMyData() {

		if (internal[myData] == null && data != null)
		    try {
		        internal[myData] = GroupSettingsXsd:CustomFiltersDocument.Factory.parse(data.DomNode);
		    } catch (Exceptions::XmlException e) {
		        throw new AppError("Invalid data format:\n" + data.toString(), e);
		    }

		return internal[myData];


	}









































	/*Radix::SelectorAddons::CfgItem.EasFiltersSingle:Methods-Methods*/

	/*Radix::SelectorAddons::CfgItem.EasFiltersSingle:linkImpObject-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::CfgItem.EasFiltersSingle:linkImpObject")
	protected published  void linkImpObject () {
		impObject = EasSelectorAddons.Filters.loadByPK(easFilterGuid, true);
	}

	/*Radix::SelectorAddons::CfgItem.EasFiltersSingle:extractExportData-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::CfgItem.EasFiltersSingle:extractExportData")
	protected published  void extractExportData (org.radixware.ads.CfgManagement.server.CfgExportData data) {
		if (expObject != null)
		    expObject.exportThis(data);
		else 
		    throw new InvalidEasRequestClientFault(String.format("Common filter %s not found in the database", calcTitle()));
	}

	/*Radix::SelectorAddons::CfgItem.EasFiltersSingle:load-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::CfgItem.EasFiltersSingle:load")
	protected published  void load (org.radixware.ads.CfgManagement.server.ICfgImportHelper helper) throws org.radixware.kernel.common.exceptions.AppException {
		if (impObject == null) {
		    EasSelectorAddons.Filters filter = EasSelectorAddons.Filters.loadByPK(easFilterGuid, true);
		    if (filter != null) {
		        myData.CustomFilters.FilterList.get(0).Id = Types::Id.Factory.newInstance(Meta::DefinitionIdPrefix:FILTER);
		    }
		}
		try (java.io.InputStream xmlFile = myData.newInputStream()) {
		    final java.util.List<EasSelectorAddons.Filters> result = new java.util.ArrayList<>(1);
		    EasSelectorAddonsTablesGroup.importFiltersImpl(xmlFile, helper, result);
		    impObject = result.get(0);
		} catch (Exceptions::IOException exception){
		    Arte::Trace.error("Error importing the common filter" + ": " + Arte::Trace.exceptionStackToString(exception), Arte::EventSource:AppCfgPackage);
		}
	}


	@Override
	public org.radixware.kernel.server.types.FormHandler.NextDialogsRequest execCommand(org.radixware.kernel.common.types.Id cmdId, org.radixware.kernel.common.types.Id propId, org.apache.xmlbeans.XmlObject input, org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValsById, org.apache.xmlbeans.XmlObject output) throws org.radixware.kernel.common.exceptions.AppException,java.lang.InterruptedException{

			return super.execCommand(cmdId,propId,input,newPropValsById,output);
	}


}

/* Radix::SelectorAddons::CfgItem.EasFiltersSingle - Server Meta*/

/*Radix::SelectorAddons::CfgItem.EasFiltersSingle-Application Class*/

package org.radixware.ads.SelectorAddons.Dlg.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class CfgItem.EasFiltersSingle_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("aclV6LLTH3TPBDGTIEIHA2WRDJB7E"),"CfgItem.EasFiltersSingle",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNC6UXKDCRRECDHICOT73SMKV7Q"),

						org.radixware.kernel.common.enums.EClassType.APPLICATION,

						/*Radix::SelectorAddons::CfgItem.EasFiltersSingle:Presentations-Entity Object Presentations*/
						/*Class presentation attributes*/
						 new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef(/*Owner Class Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("aclV6LLTH3TPBDGTIEIHA2WRDJB7E"),
							/*Owner Class Name*/
							"CfgItem.EasFiltersSingle",
							/*Title Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNC6UXKDCRRECDHICOT73SMKV7Q"),
							/*Property presentations*/

							/*Radix::SelectorAddons::CfgItem.EasFiltersSingle:Properties-Properties*/
							new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef[]{

									/*Radix::SelectorAddons::CfgItem.EasFiltersSingle:expObject:PropertyPresentation-Parent Ref Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadParentTitlePropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdXTERDBX3ANHCJIVNS6MEFPP6PI"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("sprQWY42P52BFF3JGZLM2EPVKHC7U"),new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),org.radixware.kernel.server.types.Restrictions.Factory.newInstance(3668968,null,null,null),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr43LE34LJRVHNRPIZLU4ONQKJEA")},null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::SelectorAddons::CfgItem.EasFiltersSingle:impObject:PropertyPresentation-Parent Ref Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadParentTitlePropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruD3MBXA72D5DZBMBGMPATSZKLUQ"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("sprQWY42P52BFF3JGZLM2EPVKHC7U"),new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),org.radixware.kernel.server.types.Restrictions.Factory.newInstance(3668968,null,null,null),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr43LE34LJRVHNRPIZLU4ONQKJEA")},null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT))
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
									/*Radix::SelectorAddons::CfgItem.EasFiltersSingle:Export-Editor Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprFNVT5X74IJDTJBFIDMQ5XEPJME"),
									"Export",
									org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
									org.radixware.kernel.common.types.Id.Factory.loadFrom("epr6WJV434UWFAPZNKVEEL3GBROZY"),
									36018,
									null,

									/*Radix::SelectorAddons::CfgItem.EasFiltersSingle:Export:Children-Explorer Items*/
										null
									,
									null,
									(org.radixware.kernel.server.meta.presentations.RadPropertyPresentationAttributes[]) null,
									org.radixware.kernel.common.enums.EEditorPresentationRightsInheritanceMode.FROM_REPLACED,
									null),

									new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef(
									/*Radix::SelectorAddons::CfgItem.EasFiltersSingle:Import-Editor Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprZMMVHMMQSFAW7KFCJJXRBQ5CJ4"),
									"Import",
									org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
									org.radixware.kernel.common.types.Id.Factory.loadFrom("epr7SK5TWNTUFF35FC74WMYARRP2A"),
									101552,
									null,

									/*Radix::SelectorAddons::CfgItem.EasFiltersSingle:Import:Children-Explorer Items*/
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
							org.radixware.kernel.common.utils.Maps.fromArrays(new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprCUUYK4MIYVFVLCLQGXLUNZWINM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("eprO3XHJ6U74NDNHJ7GNTDUUNDEWE")},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprFNVT5X74IJDTJBFIDMQ5XEPJME"),org.radixware.kernel.common.types.Id.Factory.loadFrom("eprZMMVHMMQSFAW7KFCJJXRBQ5CJ4")}),
							/*Object title format*/
							null,
							/*Class catalogs*/
							null,
							/*Restrictions*/
							null,null,0),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblJ4WZRLJ23BGMBKQ2JBSOBEGBP4"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acl3O3QBSF6KRHMLE5WQLUB3N2OG4"),

						/*Radix::SelectorAddons::CfgItem.EasFiltersSingle:Properties-Properties*/
						new org.radixware.kernel.server.meta.clazzes.RadPropDef[]{

								/*Radix::SelectorAddons::CfgItem.EasFiltersSingle:expObject-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicRefPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdXTERDBX3ANHCJIVNS6MEFPP6PI"),"expObject",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQ2OIDOB6GFELVES23YLCSDFQTE"),org.radixware.kernel.common.enums.EValType.PARENT_REF,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("aclLQBIKZWZABEIXKZOG4W3L6I5XI"),org.radixware.kernel.server.meta.clazzes.RadPropReadAccessorProxy.R_ACCESSOR_INSTANCE),

								/*Radix::SelectorAddons::CfgItem.EasFiltersSingle:impObject-User Property*/

								new org.radixware.kernel.server.meta.clazzes.RadUserRefPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruD3MBXA72D5DZBMBGMPATSZKLUQ"),"impObject",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLBGDCDK6EFHWHGM4QVP5ODWU6U"),org.radixware.kernel.common.enums.EValType.PARENT_REF,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("tblJ4WZRLJ23BGMBKQ2JBSOBEGBP4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aclLQBIKZWZABEIXKZOG4W3L6I5XI"),false,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::SelectorAddons::CfgItem.EasFiltersSingle:easFilterGuid-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdZOIEJ35Q55CGNJ2Z25WAAT2ADU"),"easFilterGuid",null,org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadAccessorProxy.R_ACCESSOR_INSTANCE),

								/*Radix::SelectorAddons::CfgItem.EasFiltersSingle:myData-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdVSN3J63ZDFCUJPS72LRYRSWEMU"),"myData",null,org.radixware.kernel.common.enums.EValType.XML,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadAccessorProxy.R_ACCESSOR_INSTANCE)
						},

						/*Radix::SelectorAddons::CfgItem.EasFiltersSingle:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthEBTC42HN7RENPIGWWSTFCC2IOQ"),"linkImpObject",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth7Z323JY4JFB7PAHXRDBVOX2EE4"),"extractExportData",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("data",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprVFTCENXX3ZHWVCN6333IBEWEIM"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthHOVIQBNNZRERVBNIKFD56Y6OK4"),"load",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("helper",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprMOAOXLZGNFG53N3OMQZH2VBHGA"))
								},null)
						},
						null,
						null,null,null,false);
}

/* Radix::SelectorAddons::CfgItem.EasFiltersSingle - Desktop Executable*/

/*Radix::SelectorAddons::CfgItem.EasFiltersSingle-Application Class*/

package org.radixware.ads.SelectorAddons.Dlg.explorer;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::CfgItem.EasFiltersSingle")
public interface CfgItem.EasFiltersSingle   extends org.radixware.ads.CfgManagement.explorer.CfgItem.Single  {
















































































































	/*Radix::SelectorAddons::CfgItem.EasFiltersSingle:expObject:expObject-Presentation Property*/


	public class ExpObject extends org.radixware.kernel.common.client.models.items.properties.PropertyRef{
		public ExpObject(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadParentRefPropertyDef def){
			super(model,def);
		}
		@Override
		public 
		org.radixware.ads.SelectorAddons.Dlg.explorer.EasSelectorAddons.EasSelectorAddons_DefaultModel openEntityModel()  throws org.radixware.kernel.common.exceptions.ServiceClientException, InterruptedException{
		     return (org.radixware.ads.SelectorAddons.Dlg.explorer.EasSelectorAddons.EasSelectorAddons_DefaultModel)super.openEntityModel();
		}
		@Override
		public 
		org.radixware.ads.SelectorAddons.Dlg.explorer.EasSelectorAddons.DefaultGroupModel openGroupModel(){
		     return (org.radixware.ads.SelectorAddons.Dlg.explorer.EasSelectorAddons.DefaultGroupModel)super.openGroupModel();
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.client.types.Reference dummy = ((org.radixware.kernel.common.client.types.Reference)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::CfgItem.EasFiltersSingle:expObject:expObject")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::CfgItem.EasFiltersSingle:expObject:expObject")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public ExpObject getExpObject();
	/*Radix::SelectorAddons::CfgItem.EasFiltersSingle:impObject:impObject-Presentation Property*/


	public class ImpObject extends org.radixware.kernel.common.client.models.items.properties.PropertyRef{
		public ImpObject(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadParentRefPropertyDef def){
			super(model,def);
		}
		@Override
		public 
		org.radixware.ads.SelectorAddons.Dlg.explorer.EasSelectorAddons.EasSelectorAddons_DefaultModel openEntityModel()  throws org.radixware.kernel.common.exceptions.ServiceClientException, InterruptedException{
		     return (org.radixware.ads.SelectorAddons.Dlg.explorer.EasSelectorAddons.EasSelectorAddons_DefaultModel)super.openEntityModel();
		}
		@Override
		public 
		org.radixware.ads.SelectorAddons.Dlg.explorer.EasSelectorAddons.DefaultGroupModel openGroupModel(){
		     return (org.radixware.ads.SelectorAddons.Dlg.explorer.EasSelectorAddons.DefaultGroupModel)super.openGroupModel();
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.client.types.Reference dummy = ((org.radixware.kernel.common.client.types.Reference)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::CfgItem.EasFiltersSingle:impObject:impObject")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SelectorAddons::CfgItem.EasFiltersSingle:impObject:impObject")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public ImpObject getImpObject();


}

/* Radix::SelectorAddons::CfgItem.EasFiltersSingle - Desktop Meta*/

/*Radix::SelectorAddons::CfgItem.EasFiltersSingle-Application Class*/

package org.radixware.ads.SelectorAddons.Dlg.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class CfgItem.EasFiltersSingle_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::SelectorAddons::CfgItem.EasFiltersSingle:Presentations-Entity Object Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aclV6LLTH3TPBDGTIEIHA2WRDJB7E"),
			"Radix::SelectorAddons::CfgItem.EasFiltersSingle",
			org.radixware.kernel.common.types.Id.Factory.loadFrom("acl3O3QBSF6KRHMLE5WQLUB3N2OG4"),
			null,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNC6UXKDCRRECDHICOT73SMKV7Q"),null,null,0,

			/*Radix::SelectorAddons::CfgItem.EasFiltersSingle:Properties-Properties*/
			new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

					/*Radix::SelectorAddons::CfgItem.EasFiltersSingle:expObject:PropertyPresentation-Parent Ref Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdXTERDBX3ANHCJIVNS6MEFPP6PI"),
						"expObject",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQ2OIDOB6GFELVES23YLCSDFQTE"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclV6LLTH3TPBDGTIEIHA2WRDJB7E"),
						org.radixware.kernel.common.enums.EValType.PARENT_REF,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
						4,
						null,
						null,
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
						null,
						null,
						null,
						null,
						true,-1,-1,1,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclLQBIKZWZABEIXKZOG4W3L6I5XI"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblHQSOHRWZH5ESLD2Y6IVF6XFBE4"),
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr43LE34LJRVHNRPIZLU4ONQKJEA")},
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("sprQWY42P52BFF3JGZLM2EPVKHC7U"),
						3668968,
						3669995,false),

					/*Radix::SelectorAddons::CfgItem.EasFiltersSingle:impObject:PropertyPresentation-Parent Ref Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruD3MBXA72D5DZBMBGMPATSZKLUQ"),
						"impObject",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLBGDCDK6EFHWHGM4QVP5ODWU6U"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclV6LLTH3TPBDGTIEIHA2WRDJB7E"),
						org.radixware.kernel.common.enums.EValType.PARENT_REF,
						org.radixware.kernel.common.enums.EPropNature.USER,
						4,
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
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsSSUO4JZFWVGFBAYKCJX4HU2ARE"),
						null,
						null,
						true,-1,-1,1,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclLQBIKZWZABEIXKZOG4W3L6I5XI"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblHQSOHRWZH5ESLD2Y6IVF6XFBE4"),
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr43LE34LJRVHNRPIZLU4ONQKJEA")},
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("sprQWY42P52BFF3JGZLM2EPVKHC7U"),
						3668968,
						3669995,false)
			},
			null,
			null,
			null,
			null,
			new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprFNVT5X74IJDTJBFIDMQ5XEPJME"),org.radixware.kernel.common.types.Id.Factory.loadFrom("eprZMMVHMMQSFAW7KFCJJXRBQ5CJ4")},
			true,false,false);
}

/* Radix::SelectorAddons::CfgItem.EasFiltersSingle - Web Meta*/

/*Radix::SelectorAddons::CfgItem.EasFiltersSingle-Application Class*/

package org.radixware.ads.SelectorAddons.Dlg.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class CfgItem.EasFiltersSingle_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::SelectorAddons::CfgItem.EasFiltersSingle:Presentations-Entity Object Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aclV6LLTH3TPBDGTIEIHA2WRDJB7E"),
			"Radix::SelectorAddons::CfgItem.EasFiltersSingle",
			org.radixware.kernel.common.types.Id.Factory.loadFrom("acl3O3QBSF6KRHMLE5WQLUB3N2OG4"),
			null,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNC6UXKDCRRECDHICOT73SMKV7Q"),null,null,0,
			null,
			null,
			null,
			null,
			null,
			null,
			false,false,false);
}

/* Radix::SelectorAddons::CfgItem.EasFiltersSingle:Export - Desktop Meta*/

/*Radix::SelectorAddons::CfgItem.EasFiltersSingle:Export-Editor Presentation*/

package org.radixware.ads.SelectorAddons.Dlg.explorer;
public final class Export_mi{
	private static final class Export_DEF extends org.radixware.kernel.common.client.meta.RadEditorPresentationDef{
		Export_DEF(){
			super(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprFNVT5X74IJDTJBFIDMQ5XEPJME"),
			"Export",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("epr6WJV434UWFAPZNKVEEL3GBROZY"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aclV6LLTH3TPBDGTIEIHA2WRDJB7E"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("tblJ4WZRLJ23BGMBKQ2JBSOBEGBP4"),
			null,
			null,

			/*Radix::SelectorAddons::CfgItem.EasFiltersSingle:Export:Editor Pages-Editor Presentation Pages*/
			new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

					/*Radix::SelectorAddons::CfgItem.EasFiltersSingle:Export:General-Editor Page*/

					 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgGPI3VIR7FBDWPLGCB6KXZVZ4X4"),"General",org.radixware.kernel.common.types.Id.Factory.loadFrom("aclV6LLTH3TPBDGTIEIHA2WRDJB7E"),null,null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIUQVQZZKPFBRLG7DTABUYGLYBQ"),0,0,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKLNALIUEZJGQHKSQ22KH6YAQAI"),0,5,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdY3N4QO6NSRDXTCHP27YWP3WONM"),0,1,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJHVNNS3BSZCDHPM5MZET64JSGU"),0,6,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXUWWBUGM6VAXXM4LRGA7BJRC7U"),0,2,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colD6XH3IE6INFTHPWTRD6KMRUTBI"),0,3,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdXTERDBX3ANHCJIVNS6MEFPP6PI"),0,4,1,false,false)
					},null)
			},
			new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
				new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgGPI3VIR7FBDWPLGCB6KXZVZ4X4"))}
			,

			/*Radix::SelectorAddons::CfgItem.EasFiltersSingle:Export:Children-Explorer Items*/
				null
			,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
			null,
			0,
			null,
			(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
			36018,0,0);
		}
		@Override
		protected org.radixware.kernel.common.client.models.Model createModelImpl(org.radixware.kernel.common.client.IClientEnvironment userSession) {    return new org.radixware.ads.SelectorAddons.Dlg.explorer.CfgItem.EasFiltersSingle.CfgItem.EasFiltersSingle_DefaultModel.epr6WJV434UWFAPZNKVEEL3GBROZY_ModelAdapter(userSession,this);
		}
	}
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new Export_DEF(); 
;
}
/* Radix::SelectorAddons::CfgItem.EasFiltersSingle:Import - Desktop Meta*/

/*Radix::SelectorAddons::CfgItem.EasFiltersSingle:Import-Editor Presentation*/

package org.radixware.ads.SelectorAddons.Dlg.explorer;
public final class Import_mi{
	private static final class Import_DEF extends org.radixware.kernel.common.client.meta.RadEditorPresentationDef{
		Import_DEF(){
			super(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprZMMVHMMQSFAW7KFCJJXRBQ5CJ4"),
			"Import",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("epr7SK5TWNTUFF35FC74WMYARRP2A"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aclV6LLTH3TPBDGTIEIHA2WRDJB7E"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("tblJ4WZRLJ23BGMBKQ2JBSOBEGBP4"),
			null,
			null,

			/*Radix::SelectorAddons::CfgItem.EasFiltersSingle:Import:Editor Pages-Editor Presentation Pages*/
			new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

					/*Radix::SelectorAddons::CfgItem.EasFiltersSingle:Import:General-Editor Page*/

					 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgAWE3OWYG4RFZ5IZKOWTNNOMJZA"),"General",org.radixware.kernel.common.types.Id.Factory.loadFrom("aclV6LLTH3TPBDGTIEIHA2WRDJB7E"),null,null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIUQVQZZKPFBRLG7DTABUYGLYBQ"),0,0,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKLNALIUEZJGQHKSQ22KH6YAQAI"),0,10,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdY3N4QO6NSRDXTCHP27YWP3WONM"),0,1,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJHVNNS3BSZCDHPM5MZET64JSGU"),0,11,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXUWWBUGM6VAXXM4LRGA7BJRC7U"),0,2,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colD6XH3IE6INFTHPWTRD6KMRUTBI"),0,3,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJGXEDVUL4JA5JAKSJCAVSU2BSU"),0,4,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHUYIZ7QPTBE7FAGC4FA76QNHPY"),0,7,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3DNFCTYB3ZDJZCRY4OGG7I66YA"),0,5,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colOY3R4QKX2NEEHMDIS6QV4FMDQE"),0,9,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruD3MBXA72D5DZBMBGMPATSZKLUQ"),0,8,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHQUFLZVQBNDCHMCVIG7RQM7NDQ"),0,6,1,false,false)
					},null)
			},
			new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
				new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgAWE3OWYG4RFZ5IZKOWTNNOMJZA"))}
			,

			/*Radix::SelectorAddons::CfgItem.EasFiltersSingle:Import:Children-Explorer Items*/
				null
			,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
			null,
			0,
			null,
			(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
			101552,0,0);
		}
		@Override
		protected org.radixware.kernel.common.client.models.Model createModelImpl(org.radixware.kernel.common.client.IClientEnvironment userSession) {    return new org.radixware.ads.SelectorAddons.Dlg.explorer.CfgItem.EasFiltersSingle.CfgItem.EasFiltersSingle_DefaultModel.epr7SK5TWNTUFF35FC74WMYARRP2A_ModelAdapter(userSession,this);
		}
	}
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new Import_DEF(); 
;
}
/* Radix::SelectorAddons::CfgItem.EasFiltersSingle - Localizing Bundle */
package org.radixware.ads.SelectorAddons.Dlg.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class CfgItem.EasFiltersSingle - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Common filter %s not found in the database");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Общий фильтр %s не найден в базе данных");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls76K36QM2QVG7RPAAIKZY2XQYCM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Updated object");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Обновляемый объект");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLBGDCDK6EFHWHGM4QVP5ODWU6U"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Common Filter");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Общий фильтр");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNC6UXKDCRRECDHICOT73SMKV7Q"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Exported object");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Экспортируемый объект");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQ2OIDOB6GFELVES23YLCSDFQTE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Error importing the common filter");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ошибка при импорте общего фильтра");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsR3IOGURQVVHRRJMT3NAC6RXDGY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<will be created>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<будет создан новый>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsSSUO4JZFWVGFBAYKCJX4HU2ARE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(CfgItem.EasFiltersSingle - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbaclV6LLTH3TPBDGTIEIHA2WRDJB7E"),"CfgItem.EasFiltersSingle - Localizing Bundle",$$$items$$$);
}
