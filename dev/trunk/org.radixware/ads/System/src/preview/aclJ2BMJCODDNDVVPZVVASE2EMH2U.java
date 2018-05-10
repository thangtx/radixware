
/* Radix::System::Task.DeleteDebugTrace - Server Executable*/

/*Radix::System::Task.DeleteDebugTrace-Application Class*/

package org.radixware.ads.System.server;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Task.DeleteDebugTrace")
public class Task.DeleteDebugTrace  extends org.radixware.ads.Scheduling.server.Task.Atomic  implements org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {


	/**Metainformation accessor method*/
	@SuppressWarnings("unused")
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return Task.DeleteDebugTrace_mi.rdxMeta;}

	/*Radix::System::Task.DeleteDebugTrace:Nested classes-Nested Classes*/

	/*Radix::System::Task.DeleteDebugTrace:Properties-Properties*/

	/*Radix::System::Task.DeleteDebugTrace:storePeriod-User Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Task.DeleteDebugTrace:storePeriod")
	public  Int getStorePeriod() {
		return storePeriod;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Task.DeleteDebugTrace:storePeriod")
	public   void setStorePeriod(Int val) {
		storePeriod = val;
	}

	/*Radix::System::Task.DeleteDebugTrace:sysdate-Expression Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Task.DeleteDebugTrace:sysdate")
	public  java.sql.Timestamp getSysdate() {
		return sysdate;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Task.DeleteDebugTrace:sysdate")
	public   void setSysdate(java.sql.Timestamp val) {
		sysdate = val;
	}









































	/*Radix::System::Task.DeleteDebugTrace:Methods-Methods*/

	/*Radix::System::Task.DeleteDebugTrace:execute-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Task.DeleteDebugTrace:execute")
	public published  void execute (java.sql.Timestamp prevExecTime, java.sql.Timestamp curExecTime) {
		reread();//fetch actual sysdate

		if (storePeriod == null) {
		    Arte::Trace.warning("Debug message store period is undefined. Debug message cleanup task cancelled", Arte::EventSource:App);
		    return;
		}

		//store period contains time interval in minutes;
		DateTime endTime = new DateTime(sysdate.Time - storePeriod.longValue() * 60 * 1000);

		DeleteDebugStmt q;
		int totalDeletedCount = 0;
		do {
		    q = DeleteDebugStmt.execute(null, endTime, 10000);
		    Arte::Arte.commit();
		    totalDeletedCount += q.deletedCount == null ? 0 : q.deletedCount.intValue();
		} while (q.deletedCount != null && q.deletedCount.intValue() > 0);
		Arte::Trace.debug("Debug records removed: " + totalDeletedCount, Arte::EventSource:App);

	}


	@Override
	public org.radixware.kernel.server.types.FormHandler.NextDialogsRequest execCommand(org.radixware.kernel.common.types.Id cmdId, org.radixware.kernel.common.types.Id propId, org.apache.xmlbeans.XmlObject input, org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValsById, org.apache.xmlbeans.XmlObject output) throws org.radixware.kernel.common.exceptions.AppException,java.lang.InterruptedException{

			return super.execCommand(cmdId,propId,input,newPropValsById,output);
	}


}

/* Radix::System::Task.DeleteDebugTrace - Server Meta*/

/*Radix::System::Task.DeleteDebugTrace-Application Class*/

package org.radixware.ads.System.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Task.DeleteDebugTrace_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("aclJ2BMJCODDNDVVPZVVASE2EMH2U"),"Task.DeleteDebugTrace",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5EHURA6HFFARXOVOIYTE4BIA64"),

						org.radixware.kernel.common.enums.EClassType.APPLICATION,

						/*Radix::System::Task.DeleteDebugTrace:Presentations-Entity Object Presentations*/
						/*Class presentation attributes*/
						 new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef(/*Owner Class Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("aclJ2BMJCODDNDVVPZVVASE2EMH2U"),
							/*Owner Class Name*/
							"Task.DeleteDebugTrace",
							/*Title Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5EHURA6HFFARXOVOIYTE4BIA64"),
							/*Property presentations*/

							/*Radix::System::Task.DeleteDebugTrace:Properties-Properties*/
							new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef[]{

									/*Radix::System::Task.DeleteDebugTrace:storePeriod:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruIBXAUMA5GFDYZNREIS2WQHLJTM"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT))
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
									/*Radix::System::Task.DeleteDebugTrace:Edit-Editor Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprWB3AAS57VJC7BNVCKVDKAULV5I"),
									"Edit",
									org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprAIWBLDTSJTOBDCIVAALOMT5GDM"),
									36016,
									null,

									/*Radix::System::Task.DeleteDebugTrace:Edit:Children-Explorer Items*/
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
							org.radixware.kernel.common.utils.Maps.fromArrays(new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprAIWBLDTSJTOBDCIVAALOMT5GDM")},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprWB3AAS57VJC7BNVCKVDKAULV5I")}),
							/*Object title format*/
							null,
							/*Class catalogs*/
							new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog[]{

									new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog(
									/*Radix::System::Task.DeleteDebugTrace:Default-Dynamic Class Catalog*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eccVXWRKQLYJTOBDCIVAALOMT5GDM"),org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.ELinkMode.VIRTUAL,new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.Item[]{
										new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.ClassRef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aclJ2BMJCODDNDVVPZVVASE2EMH2U"),org.radixware.kernel.common.types.Id.Factory.loadFrom("cctBVFUCASJQBEZJA7YW67UOIRROI"),120.0,true,org.radixware.kernel.common.types.Id.Factory.loadFrom("aclJ2BMJCODDNDVVPZVVASE2EMH2U"),null)}
									)
							},
							/*Restrictions*/
							null,null,0),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblWZB7K4HLJPOBDCIUAALOMT5GDM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclZDLN466RV5FE3BQIWSX56ZAETQ"),

						/*Radix::System::Task.DeleteDebugTrace:Properties-Properties*/
						new org.radixware.kernel.server.meta.clazzes.RadPropDef[]{

								/*Radix::System::Task.DeleteDebugTrace:storePeriod-User Property*/

								new org.radixware.kernel.server.meta.clazzes.RadUserPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruIBXAUMA5GFDYZNREIS2WQHLJTM"),"storePeriod",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRBFTJOWX5BCPDODZJQ3KJARVHY"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("1440")),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblWZB7K4HLJPOBDCIUAALOMT5GDM"),false,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::Task.DeleteDebugTrace:sysdate-Expression Property*/

								new org.radixware.kernel.server.meta.clazzes.RadSqmlPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colWHSIDUVTZ5BD3NHIM6J5QZV35I"),"sysdate",null,org.radixware.kernel.common.enums.EValType.DATE_TIME,"DATE",null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:Sql>sysdate</xsc:Sql></xsc:Item></xsc:Sqml>",true,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE)
						},

						/*Radix::System::Task.DeleteDebugTrace:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthN36NOGMSW5EGZIX2JAVCBWM2RU"),"execute",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("prevExecTime",org.radixware.kernel.common.enums.EValType.DATE_TIME,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprCAC5LMUS7RBGTKTR6P4RVDYYAE")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("curExecTime",org.radixware.kernel.common.enums.EValType.DATE_TIME,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprVCON6I3F2BE7FGXX3YHKQM4UVU"))
								},null)
						},
						null,
						null,null,null,false);
}

/* Radix::System::Task.DeleteDebugTrace - Desktop Executable*/

/*Radix::System::Task.DeleteDebugTrace-Application Class*/

package org.radixware.ads.System.explorer;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Task.DeleteDebugTrace")
public interface Task.DeleteDebugTrace   extends org.radixware.ads.Scheduling.explorer.Task.Atomic  {























































	/*Radix::System::Task.DeleteDebugTrace:storePeriod:storePeriod-Presentation Property*/


	public class StorePeriod extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public StorePeriod(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Int dummy = ((Int)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Task.DeleteDebugTrace:storePeriod:storePeriod")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Task.DeleteDebugTrace:storePeriod:storePeriod")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public StorePeriod getStorePeriod();


}

/* Radix::System::Task.DeleteDebugTrace - Desktop Meta*/

/*Radix::System::Task.DeleteDebugTrace-Application Class*/

package org.radixware.ads.System.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Task.DeleteDebugTrace_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::System::Task.DeleteDebugTrace:Presentations-Entity Object Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aclJ2BMJCODDNDVVPZVVASE2EMH2U"),
			"Radix::System::Task.DeleteDebugTrace",
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aclZDLN466RV5FE3BQIWSX56ZAETQ"),
			null,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5EHURA6HFFARXOVOIYTE4BIA64"),null,null,0,

			/*Radix::System::Task.DeleteDebugTrace:Properties-Properties*/
			new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

					/*Radix::System::Task.DeleteDebugTrace:storePeriod:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruIBXAUMA5GFDYZNREIS2WQHLJTM"),
						"storePeriod",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRBFTJOWX5BCPDODZJQ3KJARVHY"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5NYIVENBC5EJPFTB6ANASDTQCA"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclJ2BMJCODDNDVVPZVVASE2EMH2U"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.USER,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("1440"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::System::Task.DeleteDebugTrace:storePeriod:PropertyPresentation:Edit Options:-Edit Mask Time Interval*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskTimeInterval(60000L,"",null,null),
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
			new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprWB3AAS57VJC7BNVCKVDKAULV5I")},
			true,true,false);
}

/* Radix::System::Task.DeleteDebugTrace - Web Meta*/

/*Radix::System::Task.DeleteDebugTrace-Application Class*/

package org.radixware.ads.System.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Task.DeleteDebugTrace_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::System::Task.DeleteDebugTrace:Presentations-Entity Object Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aclJ2BMJCODDNDVVPZVVASE2EMH2U"),
			"Radix::System::Task.DeleteDebugTrace",
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aclZDLN466RV5FE3BQIWSX56ZAETQ"),
			null,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5EHURA6HFFARXOVOIYTE4BIA64"),null,null,0,
			null,
			null,
			null,
			null,
			null,
			null,
			false,false,false);
}

/* Radix::System::Task.DeleteDebugTrace:Edit - Desktop Meta*/

/*Radix::System::Task.DeleteDebugTrace:Edit-Editor Presentation*/

package org.radixware.ads.System.explorer;
public final class Edit_mi{
	private static final class Edit_DEF extends org.radixware.kernel.common.client.meta.RadEditorPresentationDef{
		Edit_DEF(){
			super(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprWB3AAS57VJC7BNVCKVDKAULV5I"),
			"Edit",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("eprAIWBLDTSJTOBDCIVAALOMT5GDM"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aclJ2BMJCODDNDVVPZVVASE2EMH2U"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("tblWZB7K4HLJPOBDCIUAALOMT5GDM"),
			null,
			null,

			/*Radix::System::Task.DeleteDebugTrace:Edit:Editor Pages-Editor Presentation Pages*/
			new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

					/*Radix::System::Task.DeleteDebugTrace:Edit:Setup-Editor Page*/

					 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgCCZ6JGBWOZGIBI5M3PEHATRPHU"),"Setup",org.radixware.kernel.common.types.Id.Factory.loadFrom("aclJ2BMJCODDNDVVPZVVASE2EMH2U"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMPP45LW2AJGSFBRV4IITYCABP4"),null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruIBXAUMA5GFDYZNREIS2WQHLJTM"),0,0,1,false,false)
					},null)
			},
			new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
				new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgHHRMHELUJTOBDCIVAALOMT5GDM")),
				new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgCCZ6JGBWOZGIBI5M3PEHATRPHU")),
				new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgL6FUZ36EMVEU3BPQ4FLI3TZ6KA"))}
			,

			/*Radix::System::Task.DeleteDebugTrace:Edit:Children-Explorer Items*/
				null
			,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
			null,
			0,
			null,
			(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
			36016,0,0);
		}
		@Override
		protected org.radixware.kernel.common.client.models.Model createModelImpl(org.radixware.kernel.common.client.IClientEnvironment userSession) {    return new org.radixware.ads.System.explorer.Task.DeleteDebugTrace.Task.DeleteDebugTrace_DefaultModel.eprAIWBLDTSJTOBDCIVAALOMT5GDM_ModelAdapter(userSession,this);
		}
	}
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new Edit_DEF(); 
;
}
/* Radix::System::Task.DeleteDebugTrace - Localizing Bundle */
package org.radixware.ads.System.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Task.DeleteDebugTrace - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Delete Debug Messages from Trace");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Удаление отладочной информации из трассы");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5EHURA6HFFARXOVOIYTE4BIA64"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"d hh:mm");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"d hh:mm");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5NYIVENBC5EJPFTB6ANASDTQCA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Parameters");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Параметры");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMPP45LW2AJGSFBRV4IITYCABP4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Storage period for message");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Срок хранения сообщений");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRBFTJOWX5BCPDODZJQ3KJARVHY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(Task.DeleteDebugTrace - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbaclJ2BMJCODDNDVVPZVVASE2EMH2U"),"Task.DeleteDebugTrace - Localizing Bundle",$$$items$$$);
}
