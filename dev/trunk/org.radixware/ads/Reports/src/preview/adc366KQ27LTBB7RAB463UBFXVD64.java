
/* Radix::Reports::ReportPubChecker - Server Executable*/

/*Radix::Reports::ReportPubChecker-Server Dynamic Class*/

package org.radixware.ads.Reports.server;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPubChecker")
public class ReportPubChecker  implements org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {


	/**Metainformation accessor method*/
	@SuppressWarnings("unused")
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return ReportPubChecker_mi.rdxMeta;}

	/*Radix::Reports::ReportPubChecker:Nested classes-Nested Classes*/

	/*Radix::Reports::ReportPubChecker:Properties-Properties*/





























	/*Radix::Reports::ReportPubChecker:Methods-Methods*/

	/*Radix::Reports::ReportPubChecker:check-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPubChecker:check")
	public static  void check (org.radixware.ads.Reports.server.ReportPub reportPub, org.radixware.ads.Reports.common.CommandsXsd.ReportPubCheckProblems problems) {
		final Types::Report report;

		CommandsXsd:ReportPubCheckProblemsEntry problemsEntry = problems.addNewEntry();
		Str contextInfo = getContextInfo(reportPub, problemsEntry);
		Str id = reportPub.id.toString();

		problemsEntry.ReportPub = id + ") " + reportPub.calcTitle();
		problemsEntry.ReportPubContext = contextInfo;


		try {
		    report = ReportsServerUtils.instantiateReportByClassId(Types::Id.Factory.loadFrom(reportPub.reportClassGuid));
		} catch (Throwable ex) {
		    Str message = "Unable to instantiate report class #" + String.valueOf(reportPub.reportClassGuid) + ". " + ex.getMessage();
		    addProblem(problemsEntry, message, Arte::EventSeverity:Error);

		    return;
		}

		ReportsXsd:ParametersBindingType parametersBinding = reportPub.getParametersBinding();

		if (parametersBinding != null) {
		    java.util.List<ReportsXsd:ParametersBindingType.ParameterBinding> paramBindings = parametersBinding.getParameterBindingList();
		    if (paramBindings != null) {
		        for (ReportsXsd:ParametersBindingType.ParameterBinding paramBinding : paramBindings) {
		            final Types::Id parameterId = paramBinding.ParameterId;
		            if (parameterId != report.ContextParameterId) {
		                try {
		                    ReportsServerUtils.loadParamFromXml(report, parameterId, paramBinding.Value);                    
		                } catch (Exceptions::DefinitionError er) {
		                    Str message = Utils::MessageFormatter.format("Parameter #{0} not found.", parameterId);
		                    addProblem(problemsEntry, message, Arte::EventSeverity:Warning);
		                } catch (Exceptions::Throwable er) {
		                    Str message = Utils::MessageFormatter.format("Parameter #{0} value not bounded.", parameterId) + er.getMessage();
		                    addProblem(problemsEntry, message, Arte::EventSeverity:Warning);
		                }
		            }
		        }
		    }
		}
	}

	/*Radix::Reports::ReportPubChecker:getContextInfo-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPubChecker:getContextInfo")
	private static  Str getContextInfo (org.radixware.ads.Reports.server.ReportPub reportPub, org.radixware.ads.Reports.common.CommandsXsd.ReportPubCheckProblemsEntry problemsEntry) {
		Str contextInfo = "";
		ReportPubList reportPubList = reportPub.reportPubList;

		if (reportPubList.pubContextClassGuid != null) {
		    Types::Id pubListContextId = Types::Id.Factory.loadFrom(reportPubList.pubContextClassGuid);
		    
		    boolean isAppClassContext = reportPubList.pubContextClassGuid.startsWith(Meta::DefinitionIdPrefix:ADS_APPLICATION_CLASS.getValue());
		    boolean isEntityClassContext = reportPubList.pubContextClassGuid.startsWith(Meta::DefinitionIdPrefix:ADS_ENTITY_CLASS.getValue());        
		    if ((isAppClassContext || isEntityClassContext) && reportPubList.contextPid != null) {        
		        
		        Meta::ClassDef pubListContextClass = Arte::Arte.getInstance().getDefManager().getClassDef(pubListContextId);        
		        Types::Entity pubListContextEntity = Types::Entity.load(pubListContextClass.getEntityId(), reportPubList.contextPid);
		        if (pubListContextEntity != null) {
		            if (pubListContextEntity.isInDatabase(true)) {
		                contextInfo = pubListContextClass.getTitle() + " / " + pubListContextEntity.calcTitle();
		            } else {
		                Str message = "Report publication context object does not exists.";
		                contextInfo = pubListContextClass.getTitle() + " / " + reportPubList.contextPid;
		                
		                addProblem(problemsEntry, message, Arte::EventSeverity:Error);
		            }
		        }
		    } else {
		        try {            
		            contextInfo = Arte::Arte.getInstance().getDefManager().getReleaseCache().getRelease().getDefTitleById(pubListContextId);
		        } catch (Exceptions::Exception ex) {
		            Arte::Trace.warning(Arte::Trace.exceptionStackToString(ex), Arte::EventSource:ArteReports);
		        }
		        
		//        contextInfo = reportPubList.;
		    }    
		}

		return contextInfo;
	}

	/*Radix::Reports::ReportPubChecker:addProblem-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPubChecker:addProblem")
	private static  void addProblem (org.radixware.ads.Reports.common.CommandsXsd.ReportPubCheckProblemsEntry problemEntry, Str problemText, org.radixware.kernel.common.enums.EEventSeverity problemSeverity) {
		CommandsXsd:ReportPubCheckProblem problem = problemEntry.addNewProblem();
		problem.ProblemText = problemText;
		problem.ProblemSeverity = problemSeverity;
	}


}

/* Radix::Reports::ReportPubChecker - Server Meta*/

/*Radix::Reports::ReportPubChecker-Server Dynamic Class*/

package org.radixware.ads.Reports.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class ReportPubChecker_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("adc366KQ27LTBB7RAB463UBFXVD64"),"ReportPubChecker",null,

						org.radixware.kernel.common.enums.EClassType.DYNAMIC,
						null,
						null,
						null,

						/*Radix::Reports::ReportPubChecker:Properties-Properties*/
						null,

						/*Radix::Reports::ReportPubChecker:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthISD7T4HGLVEPFJTUSNVNR45O7Q"),"check",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("reportPub",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprBXYRLABNKBCIPL2FA2LRS5FOZ4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("problems",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprK6XXFFQIPFC33GWT3OVXK53CKU"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthEQADJB7RNNFRLEVQUGHRFQHZAE"),"getContextInfo",true,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("reportPub",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr7BK7UEXYEVH73J6GZUQQ6UHRXE")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("problemsEntry",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprEUXD6U6R2ZFFXOO4RG7HRUNYWM"))
								},org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth6LYTIROFU5F33B2CANL4KHP2LA"),"addProblem",true,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("problemEntry",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprPROWYERHVFFY7ODQ4U5U36VHLY")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("problemText",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr2J62S7PETNFRBDYA5RLZDUJBOA")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("problemSeverity",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprD2IKMC5NL5HWVEUVI7AMAAQIWI"))
								},null)
						},
						null,
						null,null,null,false);
}

/* Radix::Reports::ReportPubChecker - Localizing Bundle */
package org.radixware.ads.Reports.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class ReportPubChecker - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Parameter #{0} value not bounded.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Значение параметра #{0} не ограничено.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsD4HNKLIZYFC6DFCKEUBPGAPXDY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Report publication context object does not exists.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Объект контекста публикации отчета не существует.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHFJUKFNLDJHYBE663DHBYRMDW4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Unable to instantiate report class #");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Не удается создать инстанцию класса отчета #");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJ6JOHVJOHRFKXHUNTPACA2M3I4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Parameter #{0} not found.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Параметр #{0} не найден.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOSXXP5BYMFCMNKSAPBAFD3RQWM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(ReportPubChecker - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbadc366KQ27LTBB7RAB463UBFXVD64"),"ReportPubChecker - Localizing Bundle",$$$items$$$);
}
