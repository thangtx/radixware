
/* Radix::Acs::ReportPub.User - Server Executable*/

/*Radix::Acs::ReportPub.User-Application Class*/

package org.radixware.ads.Acs.server;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::ReportPub.User")
public published class ReportPub.User  extends org.radixware.ads.Reports.server.ReportPub  implements org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {


	/**Metainformation accessor method*/
	@SuppressWarnings("unused")
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return ReportPub.User_mi.rdxMeta;}

	/*Radix::Acs::ReportPub.User:Nested classes-Nested Classes*/

	/*Radix::Acs::ReportPub.User:Properties-Properties*/

	/*Radix::Acs::ReportPub.User:condition-User Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::ReportPub.User:condition")
	public published  org.radixware.ads.Acs.server.UserFunc.Report.User getCondition() {
		return condition;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::ReportPub.User:condition")
	public published   void setCondition(org.radixware.ads.Acs.server.UserFunc.Report.User val) {
		condition = val;
	}

	/*Radix::Acs::ReportPub.User:preExecFunc-User Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::ReportPub.User:preExecFunc")
	public published  org.radixware.ads.Acs.server.UserFunc.Report.User getPreExecFunc() {
		return preExecFunc;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::ReportPub.User:preExecFunc")
	public published   void setPreExecFunc(org.radixware.ads.Acs.server.UserFunc.Report.User val) {
		preExecFunc = val;
	}









































	/*Radix::Acs::ReportPub.User:Methods-Methods*/

	/*Radix::Acs::ReportPub.User:check-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::ReportPub.User:check")
	public published  boolean check (org.radixware.ads.Types.server.Entity contextObject, boolean isPreExecCheck) {
		final User user = (User) contextObject; // before - to check type (throw ClassCastException if required)
		if (!isPreExecCheck)
		    return condition == null || condition.check(user, this);
		else 
		    if (preExecFunc != null && !preExecFunc.check(user, this))
		        throw new InvalidEasRequestClientFault("Report is not available");
		    else
		        return true;






	}

	/*Radix::Acs::ReportPub.User:getContextObjectClassId-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::ReportPub.User:getContextObjectClassId")
	public published  org.radixware.kernel.common.types.Id getContextObjectClassId () {
		return idof[User];
	}


	@Override
	public org.radixware.kernel.server.types.FormHandler.NextDialogsRequest execCommand(org.radixware.kernel.common.types.Id cmdId, org.radixware.kernel.common.types.Id propId, org.apache.xmlbeans.XmlObject input, org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValsById, org.apache.xmlbeans.XmlObject output) throws org.radixware.kernel.common.exceptions.AppException,java.lang.InterruptedException{

			return super.execCommand(cmdId,propId,input,newPropValsById,output);
	}


}

/* Radix::Acs::ReportPub.User - Server Meta*/

/*Radix::Acs::ReportPub.User-Application Class*/

package org.radixware.ads.Acs.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class ReportPub.User_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("acl6GSK7KORVVBUHGZRECBHL2TNHQ"),"ReportPub.User",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsY6XXKX5UHNCMFGDMB6B6ZCEWXI"),

						org.radixware.kernel.common.enums.EClassType.APPLICATION,

						/*Radix::Acs::ReportPub.User:Presentations-Entity Object Presentations*/
						/*Class presentation attributes*/
						 new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef(/*Owner Class Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("acl6GSK7KORVVBUHGZRECBHL2TNHQ"),
							/*Owner Class Name*/
							"ReportPub.User",
							/*Title Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsY6XXKX5UHNCMFGDMB6B6ZCEWXI"),
							/*Property presentations*/

							/*Radix::Acs::ReportPub.User:Properties-Properties*/
							new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef[]{

									/*Radix::Acs::ReportPub.User:condition:PropertyPresentation-Object Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadParentTitlePropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruBXCEOV3JZNCKHLICX562ZJARBI"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,null,null,null,null,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr7IKMB24XZHOBDCMTAALOMT5GDM")},null,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr7IKMB24XZHOBDCMTAALOMT5GDM")},java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Acs::ReportPub.User:preExecFunc:PropertyPresentation-Object Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadParentTitlePropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pru6WGNVWXVJZHPJCUGC7FN2MFTN4"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,null,null,null,null,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr7IKMB24XZHOBDCMTAALOMT5GDM")},null,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr7IKMB24XZHOBDCMTAALOMT5GDM")},java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT))
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
									/*Radix::Acs::ReportPub.User:General-Editor Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprSQ7YBCDRTFBD5GAH5GSRLOKWO4"),
									"General",
									org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprFP5NARHOA5E7LNMKWHBCS5Y3FM"),
									36016,
									null,

									/*Radix::Acs::ReportPub.User:General:Children-Explorer Items*/
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
							org.radixware.kernel.common.utils.Maps.fromArrays(new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprFP5NARHOA5E7LNMKWHBCS5Y3FM")},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprSQ7YBCDRTFBD5GAH5GSRLOKWO4")}),
							/*Object title format*/
							null,
							/*Class catalogs*/
							new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog[]{

									new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog(
									/*Radix::Acs::ReportPub.User:User-Dynamic Class Catalog*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("ecc7NHL6YGQQ5HFDP345BF4PBSQEI"),org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.ELinkMode.VIRTUAL,new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.Item[]{
										new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.ClassRef(org.radixware.kernel.common.types.Id.Factory.loadFrom("acl6GSK7KORVVBUHGZRECBHL2TNHQ"),null,10.0,true,org.radixware.kernel.common.types.Id.Factory.loadFrom("acl6GSK7KORVVBUHGZRECBHL2TNHQ"),null)}
									)
							},
							/*Restrictions*/
							null,null,0),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblUNC67IF5OBCX7NOPNLQOCUG374"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecUNC67IF5OBCX7NOPNLQOCUG374"),

						/*Radix::Acs::ReportPub.User:Properties-Properties*/
						new org.radixware.kernel.server.meta.clazzes.RadPropDef[]{

								/*Radix::Acs::ReportPub.User:condition-User Property*/

								new org.radixware.kernel.server.meta.clazzes.RadUserRefPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruBXCEOV3JZNCKHLICX562ZJARBI"),"condition",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAO6T2HMZ6NBTZGMHPD3SS6ZHVE"),org.radixware.kernel.common.enums.EValType.OBJECT,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DEFINE_ALWAYS,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("tblUNC67IF5OBCX7NOPNLQOCUG374"),org.radixware.kernel.common.types.Id.Factory.loadFrom("acl7DUGTYQEJFB5XJKDUQL3TR4SSY"),false,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Acs::ReportPub.User:preExecFunc-User Property*/

								new org.radixware.kernel.server.meta.clazzes.RadUserRefPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pru6WGNVWXVJZHPJCUGC7FN2MFTN4"),"preExecFunc",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAVFAMRUXQ5HSFDJGRSMC66WJRU"),org.radixware.kernel.common.enums.EValType.OBJECT,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DEFINE_ALWAYS,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("tblUNC67IF5OBCX7NOPNLQOCUG374"),org.radixware.kernel.common.types.Id.Factory.loadFrom("acl7DUGTYQEJFB5XJKDUQL3TR4SSY"),false,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE)
						},

						/*Radix::Acs::ReportPub.User:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthHR2SSO3ZF5D5FC5YCVP3AP5YPA"),"check",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("contextObject",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprV7FLUZWERVCU3INM7C44EFIOHY")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("isPreExecCheck",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprPQ6VK2BOHNG2XGF44QQ5OLRW7E"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthU2QN6PMEORASHGRBEXZDXVR2JI"),"getContextObjectClassId",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.USER_CLASS)
						},
						null,
						null,null,null,false);
}

/* Radix::Acs::ReportPub.User - Desktop Executable*/

/*Radix::Acs::ReportPub.User-Application Class*/

package org.radixware.ads.Acs.explorer;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::ReportPub.User")
public interface ReportPub.User   extends org.radixware.ads.Reports.explorer.ReportPub  {



































































	/*Radix::Acs::ReportPub.User:preExecFunc:preExecFunc-Presentation Property*/


	public class PreExecFunc extends org.radixware.kernel.common.client.models.items.properties.PropertyObject{
		public PreExecFunc(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadParentRefPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::ReportPub.User:preExecFunc:preExecFunc")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::ReportPub.User:preExecFunc:preExecFunc")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public PreExecFunc getPreExecFunc();
	/*Radix::Acs::ReportPub.User:condition:condition-Presentation Property*/


	public class Condition extends org.radixware.kernel.common.client.models.items.properties.PropertyObject{
		public Condition(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadParentRefPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::ReportPub.User:condition:condition")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::ReportPub.User:condition:condition")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public Condition getCondition();


}

/* Radix::Acs::ReportPub.User - Desktop Meta*/

/*Radix::Acs::ReportPub.User-Application Class*/

package org.radixware.ads.Acs.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class ReportPub.User_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::Acs::ReportPub.User:Presentations-Entity Object Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("acl6GSK7KORVVBUHGZRECBHL2TNHQ"),
			"Radix::Acs::ReportPub.User",
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aecUNC67IF5OBCX7NOPNLQOCUG374"),
			null,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsY6XXKX5UHNCMFGDMB6B6ZCEWXI"),null,null,0,

			/*Radix::Acs::ReportPub.User:Properties-Properties*/
			new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

					/*Radix::Acs::ReportPub.User:condition:PropertyPresentation-Object Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruBXCEOV3JZNCKHLICX562ZJARBI"),
						"condition",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAO6T2HMZ6NBTZGMHPD3SS6ZHVE"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acl6GSK7KORVVBUHGZRECBHL2TNHQ"),
						org.radixware.kernel.common.enums.EValType.OBJECT,
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
						null,
						null,
						null,
						true,-1,-1,1,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acl7DUGTYQEJFB5XJKDUQL3TR4SSY"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblJ6SOXKD3ZHOBDCMTAALOMT5GDM"),
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr7IKMB24XZHOBDCMTAALOMT5GDM")},
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr7IKMB24XZHOBDCMTAALOMT5GDM")},
						null,
						0L,
						0L,false,false),

					/*Radix::Acs::ReportPub.User:preExecFunc:PropertyPresentation-Object Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pru6WGNVWXVJZHPJCUGC7FN2MFTN4"),
						"preExecFunc",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAVFAMRUXQ5HSFDJGRSMC66WJRU"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acl6GSK7KORVVBUHGZRECBHL2TNHQ"),
						org.radixware.kernel.common.enums.EValType.OBJECT,
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
						null,
						null,
						null,
						true,-1,-1,1,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acl7DUGTYQEJFB5XJKDUQL3TR4SSY"),
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
			new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprSQ7YBCDRTFBD5GAH5GSRLOKWO4")},
			true,false,false);
}

/* Radix::Acs::ReportPub.User - Web Executable*/

/*Radix::Acs::ReportPub.User-Application Class*/

package org.radixware.ads.Acs.web;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::ReportPub.User")
public interface ReportPub.User   extends org.radixware.ads.Reports.web.ReportPub  {













































}

/* Radix::Acs::ReportPub.User - Web Meta*/

/*Radix::Acs::ReportPub.User-Application Class*/

package org.radixware.ads.Acs.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class ReportPub.User_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::Acs::ReportPub.User:Presentations-Entity Object Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("acl6GSK7KORVVBUHGZRECBHL2TNHQ"),
			"Radix::Acs::ReportPub.User",
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aecUNC67IF5OBCX7NOPNLQOCUG374"),
			null,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsY6XXKX5UHNCMFGDMB6B6ZCEWXI"),null,null,0,

			/*Radix::Acs::ReportPub.User:Properties-Properties*/
			new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

					/*Radix::Acs::ReportPub.User:condition:PropertyPresentation-Object Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruBXCEOV3JZNCKHLICX562ZJARBI"),
						"condition",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAO6T2HMZ6NBTZGMHPD3SS6ZHVE"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acl6GSK7KORVVBUHGZRECBHL2TNHQ"),
						org.radixware.kernel.common.enums.EValType.OBJECT,
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
						null,
						null,
						null,
						true,-1,-1,1,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acl7DUGTYQEJFB5XJKDUQL3TR4SSY"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblJ6SOXKD3ZHOBDCMTAALOMT5GDM"),
						null,
						null,
						null,
						0L,
						0L,false,false),

					/*Radix::Acs::ReportPub.User:preExecFunc:PropertyPresentation-Object Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pru6WGNVWXVJZHPJCUGC7FN2MFTN4"),
						"preExecFunc",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAVFAMRUXQ5HSFDJGRSMC66WJRU"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acl6GSK7KORVVBUHGZRECBHL2TNHQ"),
						org.radixware.kernel.common.enums.EValType.OBJECT,
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
						null,
						null,
						null,
						true,-1,-1,1,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acl7DUGTYQEJFB5XJKDUQL3TR4SSY"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblJ6SOXKD3ZHOBDCMTAALOMT5GDM"),
						null,
						null,
						null,
						0L,
						0L,false,false)
			},
			null,
			null,
			null,
			null,
			new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprSQ7YBCDRTFBD5GAH5GSRLOKWO4")},
			true,false,false);
}

/* Radix::Acs::ReportPub.User:General - Desktop Meta*/

/*Radix::Acs::ReportPub.User:General-Editor Presentation*/

package org.radixware.ads.Acs.explorer;
public final class General_mi{
	private static final class General_DEF extends org.radixware.kernel.common.client.meta.RadEditorPresentationDef{
		General_DEF(){
			super(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprSQ7YBCDRTFBD5GAH5GSRLOKWO4"),
			"General",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("eprFP5NARHOA5E7LNMKWHBCS5Y3FM"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("acl6GSK7KORVVBUHGZRECBHL2TNHQ"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("tblUNC67IF5OBCX7NOPNLQOCUG374"),
			null,
			null,

			/*Radix::Acs::ReportPub.User:General:Editor Pages-Editor Presentation Pages*/
			new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

					/*Radix::Acs::ReportPub.User:General:Common-Editor Page*/

					 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgA4JVIX47OFEH5NN2N6O7WB7R6I"),"Common",org.radixware.kernel.common.types.Id.Factory.loadFrom("acl6GSK7KORVVBUHGZRECBHL2TNHQ"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4EPHCGHB2VES7MBE36NNJ4HBRE"),org.radixware.kernel.common.types.Id.Factory.loadFrom("imgZV433DCBJRFDTFK7PCI7ZJURGU"),new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col5Z77DJW25NE3NOMCZ6G7JSIGSQ"),0,0,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKRIJCTPH4BBTJACPINFNMGHFOU"),0,1,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXU7SJ5KBINF27AV7GLWZLCIO4E"),0,4,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNCCJD67UNVD4LM7B3IKHJPER5Y"),0,6,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colLOAH3AWYW5DLBHJSUCBMFDJEHY"),0,7,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colX5QBHA4M3NABRLT52FMFOWQVYQ"),0,10,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGPFZICKAKZCNXCW7XNDNUCERJM"),0,2,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd3CGHHRLW55CQJNQJXE6DBUPWQY"),0,3,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdFD67H7DF35E6DHJIDDOFGPQD4E"),0,5,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colVP3HR4T4VRDJXE2SQKI3CUOSJI"),0,15,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruBXCEOV3JZNCKHLICX562ZJARBI"),0,8,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("pru6WGNVWXVJZHPJCUGC7FN2MFTN4"),0,9,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruFPX3ORINZ5ADVJBS6BMJPBRPLA"),0,11,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("pru2IHMCWS4PBBZLGPMBLXPQVCACM"),0,12,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruXBSUGY7HRNDEJJYTZBOJ7BGNR4"),0,13,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colCFIB7BM27NH67GFPFCDMY7Y7OQ"),0,14,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPS2XXJEY6ZHUXMSQRBWX5CKMNU"),0,16,1,false,false)
					},null)
			},
			new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
				new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgA4JVIX47OFEH5NN2N6O7WB7R6I")),
				new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epg3E4TYYDJNJCVHGUXVRIA3SM6VQ")),
				new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epg2STT5XKWHBFA5EYRDCDJB3QP3Y"))}
			,

			/*Radix::Acs::ReportPub.User:General:Children-Explorer Items*/
				null
			,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
			null,
			0,
			null,
			(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
			36016,0,0);
		}
		@Override
		protected org.radixware.kernel.common.client.models.Model createModelImpl(org.radixware.kernel.common.client.IClientEnvironment userSession) {    return new org.radixware.ads.Acs.explorer.ReportPub.User.ReportPub.User_DefaultModel.eprFP5NARHOA5E7LNMKWHBCS5Y3FM_ModelAdapter(userSession,this);
		}
	}
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new General_DEF(); 
;
}
/* Radix::Acs::ReportPub.User:General - Web Meta*/

/*Radix::Acs::ReportPub.User:General-Editor Presentation*/

package org.radixware.ads.Acs.web;
public final class General_mi{
	private static final class General_DEF extends org.radixware.kernel.common.client.meta.RadEditorPresentationDef{
		General_DEF(){
			super(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprSQ7YBCDRTFBD5GAH5GSRLOKWO4"),
			"General",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("eprFP5NARHOA5E7LNMKWHBCS5Y3FM"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("acl6GSK7KORVVBUHGZRECBHL2TNHQ"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("tblUNC67IF5OBCX7NOPNLQOCUG374"),
			null,
			null,

			/*Radix::Acs::ReportPub.User:General:Editor Pages-Editor Presentation Pages*/
			new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

					/*Radix::Acs::ReportPub.User:General:Common-Editor Page*/

					 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgA4JVIX47OFEH5NN2N6O7WB7R6I"),"Common",org.radixware.kernel.common.types.Id.Factory.loadFrom("acl6GSK7KORVVBUHGZRECBHL2TNHQ"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4EPHCGHB2VES7MBE36NNJ4HBRE"),org.radixware.kernel.common.types.Id.Factory.loadFrom("imgZV433DCBJRFDTFK7PCI7ZJURGU"),new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col5Z77DJW25NE3NOMCZ6G7JSIGSQ"),0,0,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKRIJCTPH4BBTJACPINFNMGHFOU"),0,1,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXU7SJ5KBINF27AV7GLWZLCIO4E"),0,4,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNCCJD67UNVD4LM7B3IKHJPER5Y"),0,6,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colLOAH3AWYW5DLBHJSUCBMFDJEHY"),0,7,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colX5QBHA4M3NABRLT52FMFOWQVYQ"),0,10,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGPFZICKAKZCNXCW7XNDNUCERJM"),0,2,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd3CGHHRLW55CQJNQJXE6DBUPWQY"),0,3,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdFD67H7DF35E6DHJIDDOFGPQD4E"),0,5,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colVP3HR4T4VRDJXE2SQKI3CUOSJI"),0,15,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruBXCEOV3JZNCKHLICX562ZJARBI"),0,8,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("pru6WGNVWXVJZHPJCUGC7FN2MFTN4"),0,9,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruFPX3ORINZ5ADVJBS6BMJPBRPLA"),0,11,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("pru2IHMCWS4PBBZLGPMBLXPQVCACM"),0,12,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruXBSUGY7HRNDEJJYTZBOJ7BGNR4"),0,13,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colCFIB7BM27NH67GFPFCDMY7Y7OQ"),0,14,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPS2XXJEY6ZHUXMSQRBWX5CKMNU"),0,16,1,false,false)
					},null)
			},
			new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
				new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgA4JVIX47OFEH5NN2N6O7WB7R6I")),
				new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epg3E4TYYDJNJCVHGUXVRIA3SM6VQ"))}
			,

			/*Radix::Acs::ReportPub.User:General:Children-Explorer Items*/
				null
			,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
			null,
			0,
			null,
			(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
			36016,0,0);
		}
		@Override
		protected org.radixware.kernel.common.client.models.Model createModelImpl(org.radixware.kernel.common.client.IClientEnvironment userSession) {    return new org.radixware.ads.Acs.web.ReportPub.User.ReportPub.User_DefaultModel.eprFP5NARHOA5E7LNMKWHBCS5Y3FM_ModelAdapter(userSession,this);
		}
	}
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new General_DEF(); 
;
}
/* Radix::Acs::ReportPub.User - Localizing Bundle */
package org.radixware.ads.Acs.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class ReportPub.User - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Publication");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Публикация");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4EPHCGHB2VES7MBE36NNJ4HBRE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Availability condition");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Условие доступности");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAO6T2HMZ6NBTZGMHPD3SS6ZHVE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Pre-execution condition");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Предусловие исполнения");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAVFAMRUXQ5HSFDJGRSMC66WJRU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Report is not available");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Отчет не доступен");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTZWWTHPLZZH7HDA4CC7DNK46AA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"User Report Publication");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Публикация отчета по пользователю");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsY6XXKX5UHNCMFGDMB6B6ZCEWXI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(ReportPub.User - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbacl6GSK7KORVVBUHGZRECBHL2TNHQ"),"ReportPub.User - Localizing Bundle",$$$items$$$);
}
