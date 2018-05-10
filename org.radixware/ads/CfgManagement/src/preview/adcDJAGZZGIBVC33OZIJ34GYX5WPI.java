
/* Radix::CfgManagement::CfgUserFuncImportHelper - Server Executable*/

/*Radix::CfgManagement::CfgUserFuncImportHelper-Server Dynamic Class*/

package org.radixware.ads.CfgManagement.server;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgUserFuncImportHelper")
public class CfgUserFuncImportHelper  implements org.radixware.ads.CfgManagement.server.ICfgUserFuncImportHelper,org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {


	/**Metainformation accessor method*/
	@SuppressWarnings("unused")
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return CfgUserFuncImportHelper_mi.rdxMeta;}

	/*Radix::CfgManagement::CfgUserFuncImportHelper:Nested classes-Nested Classes*/

	/*Radix::CfgManagement::CfgUserFuncImportHelper:Properties-Properties*/

	/*Radix::CfgManagement::CfgUserFuncImportHelper:funcsToDeferredCompile-Dynamic Property*/



	protected org.radixware.kernel.server.types.ArrEntity<org.radixware.ads.mdlU7Z6KRE4ZBGKPHT5ZUVW4UBNMM.server.aecJ6SOXKD3ZHOBDCMTAALOMT5GDM> funcsToDeferredCompile=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgUserFuncImportHelper:funcsToDeferredCompile")
	private final  org.radixware.kernel.server.types.ArrEntity<org.radixware.ads.mdlU7Z6KRE4ZBGKPHT5ZUVW4UBNMM.server.aecJ6SOXKD3ZHOBDCMTAALOMT5GDM> getFuncsToDeferredCompile() {
		return funcsToDeferredCompile;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgUserFuncImportHelper:funcsToDeferredCompile")
	private final   void setFuncsToDeferredCompile(org.radixware.kernel.server.types.ArrEntity<org.radixware.ads.mdlU7Z6KRE4ZBGKPHT5ZUVW4UBNMM.server.aecJ6SOXKD3ZHOBDCMTAALOMT5GDM> val) {
		funcsToDeferredCompile = val;
	}

	/*Radix::CfgManagement::CfgUserFuncImportHelper:helper-Dynamic Property*/



	protected org.radixware.ads.CfgManagement.server.ICfgImportHelper helper=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgUserFuncImportHelper:helper")
	private final  org.radixware.ads.CfgManagement.server.ICfgImportHelper getHelper() {
		return helper;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgUserFuncImportHelper:helper")
	private final   void setHelper(org.radixware.ads.CfgManagement.server.ICfgImportHelper val) {
		helper = val;
	}

	/*Radix::CfgManagement::CfgUserFuncImportHelper:isCompileDeferred-Dynamic Property*/



	protected boolean isCompileDeferred=false;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgUserFuncImportHelper:isCompileDeferred")
	private final  boolean getIsCompileDeferred() {
		return isCompileDeferred;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgUserFuncImportHelper:isCompileDeferred")
	private final   void setIsCompileDeferred(boolean val) {
		isCompileDeferred = val;
	}

	/*Radix::CfgManagement::CfgUserFuncImportHelper:initialSize-Dynamic Property*/



	protected int initialSize=0;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgUserFuncImportHelper:initialSize")
	private final  int getInitialSize() {
		return initialSize;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgUserFuncImportHelper:initialSize")
	private final   void setInitialSize(int val) {
		initialSize = val;
	}

	/*Radix::CfgManagement::CfgUserFuncImportHelper:func2Title-Dynamic Property*/



	protected java.util.Map<org.radixware.ads.UserFunc.server.UserFunc,Str> func2Title=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgUserFuncImportHelper:func2Title")
	private final  java.util.Map<org.radixware.ads.UserFunc.server.UserFunc,Str> getFunc2Title() {
		return func2Title;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgUserFuncImportHelper:func2Title")
	private final   void setFunc2Title(java.util.Map<org.radixware.ads.UserFunc.server.UserFunc,Str> val) {
		func2Title = val;
	}



























































	/*Radix::CfgManagement::CfgUserFuncImportHelper:Methods-Methods*/

	/*Radix::CfgManagement::CfgUserFuncImportHelper:isCompileDeferred-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgUserFuncImportHelper:isCompileDeferred")
	public published  boolean isCompileDeferred () {
		return isCompileDeferred;
	}

	/*Radix::CfgManagement::CfgUserFuncImportHelper:compile-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgUserFuncImportHelper:compile")
	public  void compile (org.radixware.kernel.server.arte.resources.ProgressDialogResource.Process progressDlg) {
		initialSize = funcsToDeferredCompile.size();

		UserFunc::UserFuncCompiler compiler = new UserFuncCompiler();
		compiler.useSharedWorkspace
		        = org.radixware.kernel.common.utils.SystemPropUtils.
		        getBooleanSystemProp(UserFuncCompiler.USE_SHARED_WORKSPACE_ON_CFG_IMPORT, true);

		try {
		    while (!funcsToDeferredCompile.isEmpty()) {
		        final UserFunc::UserFunc func = funcsToDeferredCompile.remove(0);
		        if (checkIsValidState(func)) {
		            compileFunc(func, progressDlg, compiler);
		        }
		    }
		} finally {
		    org.radixware.kernel.common.repository.Branch branch = null;
		    try {
		        branch = Arte::Arte.getDefManager().ReleaseCache.Release.Repository.getBranch();
		    } catch (Exceptions::IOException e) {
		    }
		    compiler.close(branch);
		}
	}

	/*Radix::CfgManagement::CfgUserFuncImportHelper:CfgUserFuncImportHelper-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgUserFuncImportHelper:CfgUserFuncImportHelper")
	public  CfgUserFuncImportHelper (org.radixware.ads.CfgManagement.server.ICfgImportHelper helper) {
		funcsToDeferredCompile = new ArrParentRef(Arte::Arte.getInstance());
		func2Title = new java.util.HashMap<>();
		isCompileDeferred = false;
		helper = helper;
	}

	/*Radix::CfgManagement::CfgUserFuncImportHelper:scheduleDeferredCompile-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgUserFuncImportHelper:scheduleDeferredCompile")
	public published  void scheduleDeferredCompile (org.radixware.ads.UserFunc.server.UserFunc userFunc) {
		if (!funcsToDeferredCompile.contains(userFunc)) {
		    funcsToDeferredCompile.add(userFunc);
		    func2Title.put(userFunc, userFunc.path);
		}
	}

	/*Radix::CfgManagement::CfgUserFuncImportHelper:compileFunc-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgUserFuncImportHelper:compileFunc")
	private final  void compileFunc (org.radixware.ads.UserFunc.server.UserFunc func, org.radixware.kernel.server.arte.resources.ProgressDialogResource.Process progrProc, org.radixware.ads.UserFunc.common.UserFuncCompiler compiler) {
		if (func.isLinkUsed == null || !func.isLinkUsed.booleanValue()) {
		    final UserFunc::UserFunc thisFunc = func;
		    org.radixware.kernel.common.check.IProblemHandler diagnoseHandler = new org.radixware.kernel.common.check.IProblemHandler() {
		        public void accept(org.radixware.kernel.common.check.RadixProblem p) {
		            if (p.Severity == org.radixware.kernel.common.check.RadixProblem.ESeverity.ERROR) {
		                Arte::Trace.error(UserFunc::UserFunc.getCompilationErrorMessage(thisFunc, p), Arte::EventSource:UserFunc);
		            }
		        }
		    };
		    if (progrProc != null) {
		        final float progress = (initialSize - funcsToDeferredCompile.size()) / (float) initialSize * 100;
		        try {
		            progrProc.set(Str.format("Compile UDF (%d of %d complied): %s",
		                    initialSize - funcsToDeferredCompile.size(), initialSize, func.path),
		                    progress, true);
		            if (progrProc.checkIsCancelled())
		                throw new InvalidEasRequestClientFault("Canceled");
		        } catch (Exceptions::InterruptedException | Exceptions::ResourceUsageException | Exceptions::ResourceUsageTimeout ex) {
		            throw new AppError("Error on user function compilation", ex);
		        }
		    }
		    try {
		        func.compile(compiler, diagnoseHandler, true, false);
		    } catch (Exceptions::Exception ex) {
		        Arte::Trace.error(Str.format("Error on compile '%s':%n%s", func.path, Arte::Trace.exceptionStackToString(ex)),
		                Arte::EventSource:AppCfgPackage);
		    }
		} else {
		    UserFunc::LibUserFunc linkedFunc = func.linkedLibFunc;
		    if (linkedFunc != null) {
		        int index = 0;
		        int fcnToCompileIdx = -1;
		        final Int linkedFuncId = linkedFunc.upUserFunc.id;
		        for (UserFunc::UserFunc fcn : funcsToDeferredCompile) {
		            if (fcn.id.equals(linkedFuncId)) {
		                fcnToCompileIdx = index;
		                break;
		            }
		            index++;
		        }
		        if (fcnToCompileIdx != -1) {
		            compileFunc(funcsToDeferredCompile.remove(fcnToCompileIdx), progrProc, compiler);
		        }
		    }
		}

		func.actualizeUsedDefinitions();

		if (helper != null) {
		    func.reportIsStateValid(helper);
		}
	}

	/*Radix::CfgManagement::CfgUserFuncImportHelper:setCompileDeferred-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgUserFuncImportHelper:setCompileDeferred")
	public published  void setCompileDeferred (boolean enable) {
		isCompileDeferred = enable;
	}

	/*Radix::CfgManagement::CfgUserFuncImportHelper:checkIsValidState-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgUserFuncImportHelper:checkIsValidState")
	private final  boolean checkIsValidState (org.radixware.ads.UserFunc.server.UserFunc func) {
		if (func.isDiscarded() || func.isDeleted()) {
		    Arte::Trace.warning(Str.format("During compilation on package import found function '%s' with not correct state '%s'. Probably function import multiple times.",
		            func2Title.get(func), func.isDiscarded() ? "DISCARDED" : "DELETED"),
		            Arte::EventSource:AppCfgPackage);
		    return false;
		}
		return true;
	}


}

/* Radix::CfgManagement::CfgUserFuncImportHelper - Server Meta*/

/*Radix::CfgManagement::CfgUserFuncImportHelper-Server Dynamic Class*/

package org.radixware.ads.CfgManagement.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class CfgUserFuncImportHelper_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("adcDJAGZZGIBVC33OZIJ34GYX5WPI"),"CfgUserFuncImportHelper",null,

						org.radixware.kernel.common.enums.EClassType.DYNAMIC,
						null,
						null,
						null,

						/*Radix::CfgManagement::CfgUserFuncImportHelper:Properties-Properties*/
						new org.radixware.kernel.server.meta.clazzes.RadPropDef[]{

								/*Radix::CfgManagement::CfgUserFuncImportHelper:funcsToDeferredCompile-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicRefPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd2HW67HV2ORGYPKN53IU7QNVEDU"),"funcsToDeferredCompile",null,org.radixware.kernel.common.enums.EValType.ARR_REF,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ6SOXKD3ZHOBDCMTAALOMT5GDM"),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::CfgManagement::CfgUserFuncImportHelper:helper-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdRXZH2J3QDJD7ZIB7UC543KYAMU"),"helper",null,org.radixware.kernel.common.enums.EValType.USER_CLASS,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::CfgManagement::CfgUserFuncImportHelper:isCompileDeferred-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdDRWBD3UAIFFANJZATTFRWDVPOI"),"isCompileDeferred",null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::CfgManagement::CfgUserFuncImportHelper:initialSize-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdHQPOVS3OUVHCRCXW46EALU5Q2Y"),"initialSize",null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::CfgManagement::CfgUserFuncImportHelper:func2Title-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdXDHDRA4D5RAXNBBWOXBVNVABRA"),"func2Title",null,org.radixware.kernel.common.enums.EValType.JAVA_CLASS,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE)
						},

						/*Radix::CfgManagement::CfgUserFuncImportHelper:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthN47TNEHC4FAHZNSCA3IGFJDZYI"),"isCompileDeferred",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthHMCQINZVHFEUXJQXWQKY3O3RGY"),"compile",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("progressDlg",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr5GA2H6NIYNG73KKEIJW7XPITT4"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthLZMFPBWEQZCBLAB7OLZJYXJDTU"),"CfgUserFuncImportHelper",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("helper",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprGL6ZLI4IWVAKBJAA4TMQXV3P6E"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthCXWZSHVP7ZCE7H6TO5FKQQIUNU"),"scheduleDeferredCompile",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("userFunc",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprPICSQ5GE25E53GDB7MWGYEJ4YQ"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth6VQWOBTGSJFGTEHGQAE3VH5NQU"),"compileFunc",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("func",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprWIZLP6F4XVCE3PD7OBNXODWUEU")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("progrProc",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprASPASYMQPJDTDNS3ZZ6WNX6SDQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("compiler",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprJ7M3Y3KEIJEKDFIDT52RBNVH6E"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth2BNW4O2S3ZE6RAQ2VCWDYP6V4Y"),"setCompileDeferred",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("enable",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr4NABRJF2RFDSZEDTSRTXCD27FU"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthLOOG5AHYH5BWTLAJRV5MQJ23AI"),"checkIsValidState",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("func",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprIWTHEAMGSRANNKVQAQH42QQVDE"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE)
						},
						null,
						null,null,null,false);
}

/* Radix::CfgManagement::CfgUserFuncImportHelper - Localizing Bundle */
package org.radixware.ads.CfgManagement.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class CfgUserFuncImportHelper - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Компиляция UDF (%d из %d скомпилировано): %s");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Compile UDF (%d of %d complied): %s");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4IN4U7DRQRGK3FMN3RKG7MYSFA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"C:\\distrib\\trunk\\org.radixware\\ads\\CfgManagement"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Прервано");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Canceled");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAH22PVQLYFCF5M75535XSBCSY4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"C:\\distrib\\trunk\\org.radixware\\ads\\CfgManagement"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"В процессе компиляции при импорте пакета найдена функция \'%s\' с некорректным состоянием \'%s\'. Вероятно, функция импортирована несколько раз.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"During compilation on package import found function \'%s\' with not correct state \'%s\'. Probably function import multiple times.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGWRQMFSAFFFUTICI3EJJMM2VLE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.EVENT_CODE,org.radixware.kernel.common.enums.EEventSeverity.EVENT,"App",java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"C:\\distrib\\trunk\\org.radixware\\ads\\CfgManagement"));
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(CfgUserFuncImportHelper - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbadcDJAGZZGIBVC33OZIJ34GYX5WPI"),"CfgUserFuncImportHelper - Localizing Bundle",$$$items$$$);
}
