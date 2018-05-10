
/* Radix::Acs::PartitionGroup.UserGroup - Server Executable*/

/*Radix::Acs::PartitionGroup.UserGroup-Application Class*/

package org.radixware.ads.Acs.server;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::PartitionGroup.UserGroup")
public published class PartitionGroup.UserGroup  extends org.radixware.ads.Acs.server.PartitionGroup  implements org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {


	/**Metainformation accessor method*/
	@SuppressWarnings("unused")
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return PartitionGroup.UserGroup_mi.rdxMeta;}

	/*Radix::Acs::PartitionGroup.UserGroup:Nested classes-Nested Classes*/

	/*Radix::Acs::PartitionGroup.UserGroup:Properties-Properties*/





























	/*Radix::Acs::PartitionGroup.UserGroup:Methods-Methods*/

	/*Radix::Acs::PartitionGroup.UserGroup:getFamilySelectorPresentationId-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::PartitionGroup.UserGroup:getFamilySelectorPresentationId")
	public published  org.radixware.kernel.common.types.Id getFamilySelectorPresentationId () {
		return idof[UserGroup:General];

	}

	/*Radix::Acs::PartitionGroup.UserGroup:getFamilyId-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::PartitionGroup.UserGroup:getFamilyId")
	public published  org.radixware.kernel.common.types.Id getFamilyId () {
		return idof[Acs::UserGroup];
	}


	@Override
	public org.radixware.kernel.server.types.FormHandler.NextDialogsRequest execCommand(org.radixware.kernel.common.types.Id cmdId, org.radixware.kernel.common.types.Id propId, org.apache.xmlbeans.XmlObject input, org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValsById, org.apache.xmlbeans.XmlObject output) throws org.radixware.kernel.common.exceptions.AppException,java.lang.InterruptedException{

			return super.execCommand(cmdId,propId,input,newPropValsById,output);
	}


}

/* Radix::Acs::PartitionGroup.UserGroup - Server Meta*/

/*Radix::Acs::PartitionGroup.UserGroup-Application Class*/

package org.radixware.ads.Acs.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class PartitionGroup.UserGroup_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("acl26XYRH2LI5ADTP4JPLQ2ZWMCGE"),"PartitionGroup.UserGroup",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEPJUQ5SMCVEDVCHQLGPP2JVBUE"),

						org.radixware.kernel.common.enums.EClassType.APPLICATION,

						/*Radix::Acs::PartitionGroup.UserGroup:Presentations-Entity Object Presentations*/
						/*Class presentation attributes*/
						 new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef(/*Owner Class Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("acl26XYRH2LI5ADTP4JPLQ2ZWMCGE"),
							/*Owner Class Name*/
							"PartitionGroup.UserGroup",
							/*Title Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEPJUQ5SMCVEDVCHQLGPP2JVBUE"),
							/*Property presentations*/

							/*Radix::Acs::PartitionGroup.UserGroup:Properties-Properties*/
							null,
							/*Commands*/
							null,
							/*Sortings*/
							null,
							/*Filters*/
							null,
							/*Editor presentations*/
							null,
							/*Selector presentations*/
							null,
							/*Default selector presentation Id*/
							null,
							/*Presentation Map*/
							null,
							/*Object title format*/
							null,
							/*Class catalogs*/
							new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog[]{

									new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog(
									/*Radix::Acs::PartitionGroup.UserGroup:General-Dynamic Class Catalog*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eccYCNCT2DEA5HNVJIPJ7IS55QHUM"),org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.ELinkMode.VIRTUAL,new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.Item[]{
										new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.ClassRef(org.radixware.kernel.common.types.Id.Factory.loadFrom("acl26XYRH2LI5ADTP4JPLQ2ZWMCGE"),null,20.0,true,org.radixware.kernel.common.types.Id.Factory.loadFrom("acl26XYRH2LI5ADTP4JPLQ2ZWMCGE"),null)}
									)
							},
							/*Restrictions*/
							null,null,0),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblLNMBHO73GNE2LF5TXFR7QEMICM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecLNMBHO73GNE2LF5TXFR7QEMICM"),

						/*Radix::Acs::PartitionGroup.UserGroup:Properties-Properties*/
						null,

						/*Radix::Acs::PartitionGroup.UserGroup:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthXWHHTDS7H5GNNMBX22SAGYK54U"),"getFamilySelectorPresentationId",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthVD22TSAPWBFRXA7RKUIXAFWBJY"),"getFamilyId",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.USER_CLASS)
						},
						null,
						null,null,null,false);
}

/* Radix::Acs::PartitionGroup.UserGroup - Desktop Executable*/

/*Radix::Acs::PartitionGroup.UserGroup-Application Class*/

package org.radixware.ads.Acs.explorer;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::PartitionGroup.UserGroup")
public interface PartitionGroup.UserGroup   extends org.radixware.ads.Acs.explorer.PartitionGroup  {
























}

/* Radix::Acs::PartitionGroup.UserGroup - Desktop Meta*/

/*Radix::Acs::PartitionGroup.UserGroup-Application Class*/

package org.radixware.ads.Acs.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class PartitionGroup.UserGroup_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::Acs::PartitionGroup.UserGroup:Presentations-Entity Object Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("acl26XYRH2LI5ADTP4JPLQ2ZWMCGE"),
			"Radix::Acs::PartitionGroup.UserGroup",
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aecLNMBHO73GNE2LF5TXFR7QEMICM"),
			null,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEPJUQ5SMCVEDVCHQLGPP2JVBUE"),null,null,0,

			/*Radix::Acs::PartitionGroup.UserGroup:Properties-Properties*/
			null,
			null,
			null,
			null,
			null,
			null,
			true,false,false);
}

/* Radix::Acs::PartitionGroup.UserGroup - Web Executable*/

/*Radix::Acs::PartitionGroup.UserGroup-Application Class*/

package org.radixware.ads.Acs.web;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::PartitionGroup.UserGroup")
public interface PartitionGroup.UserGroup   extends org.radixware.ads.Acs.web.PartitionGroup  {
























}

/* Radix::Acs::PartitionGroup.UserGroup - Web Meta*/

/*Radix::Acs::PartitionGroup.UserGroup-Application Class*/

package org.radixware.ads.Acs.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class PartitionGroup.UserGroup_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::Acs::PartitionGroup.UserGroup:Presentations-Entity Object Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("acl26XYRH2LI5ADTP4JPLQ2ZWMCGE"),
			"Radix::Acs::PartitionGroup.UserGroup",
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aecLNMBHO73GNE2LF5TXFR7QEMICM"),
			null,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEPJUQ5SMCVEDVCHQLGPP2JVBUE"),null,null,0,

			/*Radix::Acs::PartitionGroup.UserGroup:Properties-Properties*/
			null,
			null,
			null,
			null,
			null,
			null,
			true,false,false);
}

/* Radix::Acs::PartitionGroup.UserGroup - Localizing Bundle */
package org.radixware.ads.Acs.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class PartitionGroup.UserGroup - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Partition group for \'User Group\' family");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Группа разделов для семейства \'Группа пользователей\'");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEPJUQ5SMCVEDVCHQLGPP2JVBUE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(PartitionGroup.UserGroup - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbacl26XYRH2LI5ADTP4JPLQ2ZWMCGE"),"PartitionGroup.UserGroup - Localizing Bundle",$$$items$$$);
}
