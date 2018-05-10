
/* Radix::Explorer.Utils::UserDesignerLauncher - Desktop Executable*/

/*Radix::Explorer.Utils::UserDesignerLauncher-Desktop Dynamic Class*/

package org.radixware.ads.Explorer.Utils.explorer;

import org.radixware.kernel.utils.nblauncher.NbLauncher;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Explorer.Utils::UserDesignerLauncher")
public class UserDesignerLauncher  {



	/*Radix::Explorer.Utils::UserDesignerLauncher:Nested classes-Nested Classes*/

	/*Radix::Explorer.Utils::UserDesignerLauncher:Properties-Properties*/

	/*Radix::Explorer.Utils::UserDesignerLauncher:Methods-Methods*/

	/*Radix::Explorer.Utils::UserDesignerLauncher:start-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Explorer.Utils::UserDesignerLauncher:start")
	public static  void start (org.radixware.kernel.common.client.IClientEnvironment env, org.radixware.ads.Explorer.Utils.common.UserDesignerMode mode) {

		NbLauncher.ApplicationConfig config = new NbLauncher.ApplicationConfig("reporteditor");

		config.putValue(NbLauncher.ApplicationConfig.EAS_SESSION, env.getEasSession());

		config.putValue(NbLauncher.ApplicationConfig.FEATURES, mode.Value);
		config.putValue(NbLauncher.ApplicationConfig.TIMEOUT, new Integer(0));
		config.putValue(NbLauncher.ApplicationConfig.CONNECTION_NAME, env.getConnectionName());
		config.putValue("application", env.getApplication());

		Client.Types::IProgressHandle handle = env.getProgressHandleManager().newProgressHandle();

		int attempt = 0;

		while (true) {
		    try {
		        handle.startProgress("", false);
		        try {
		            if (!NbLauncher.launchNbApplication(config, env.getTracer())) {
		                switch (mode) {
		                    case UserDesignerMode:MsdlEditor:
		                        env.messageError("Failed to start the MSDL Editor.");
		                        break;
		                    case UserDesignerMode:ReportEditor:
		                        env.messageError("Failed to start the Report Designer.");
		                        break;
		                    case UserDesignerMode:RoleEditor:
		                        env.messageError("Failed to start the Role Definition Designer.");
		                        break;
		                    default:
		                        env.messageError("Failed to start the User-Defined Definition Designer.");
		                        break;
		                }
		            }
		            break;
		        } catch (NbLauncher.NbLauncherException e) {

		            if (e.getFailCause() == NbLauncher.ENbLauncherFailCause.APPLICATION_ALREADY_STARTED) {
		                attempt++;
		                if (attempt > 3) {
		                    env.messageError("Application is already running");
		                    break;
		                } else {
		                    try {
		                        Thread.sleep(attempt * 1000);
		                    } catch (InterruptedException ex) {
		                        break;
		                    }
		                }
		            } else if (e.getFailCause() == NbLauncher.ENbLauncherFailCause.STARTER_ACTUALIZE_TIMEOUT) {
		                env.messageWarning("Starter cache refresh takes too much time. User Definitions Designer will be launched without information about current user.");
		                break;
		            }
		        }
		    } finally {
		        handle.finishProgress();
		    }
		}
	}


}

/* Radix::Explorer.Utils::UserDesignerLauncher - Desktop Meta*/

/*Radix::Explorer.Utils::UserDesignerLauncher-Desktop Dynamic Class*/

package org.radixware.ads.Explorer.Utils.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class UserDesignerLauncher_mi{
}

/* Radix::Explorer.Utils::UserDesignerLauncher - Localizing Bundle */
package org.radixware.ads.Explorer.Utils.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class UserDesignerLauncher - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Failed to start the Report Designer.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Не удалось запустить редактор отчётов.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3T2MYWHI7FHV3GL4XX2RUJF7QQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Application is already running");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Приложение уже запущено");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls447CYX7QFREOBBWY46QOPX76NA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Failed to start the User-Defined Definition Designer.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Не удалось запустить редактор пользовательских дефиниций.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4WD32IJJCJGSFEW7PMPLIFON5M"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5JAZV5LCLNELVI3IMXHNU2XFUI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Failed to start the Role Definition Designer.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Не удалось запустить редактор ролей.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAR7D7H4BOJHQBN2POKBVZVYU7M"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Starter cache refresh takes too much time. User Definitions Designer will be launched without information about current user.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Процедура актуализации кеша стартера заняла слишком много времени. Дизайнер пользовательских дефиниций будет запущен без информации о текущем пользователе.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGZOZ3B5FTZGBFDKV2VGHX534RY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Failed to start the MSDL Editor.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Не удалось запустить редактор Msdl.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsIT3VTKIUXJDVLA65UZGVFFWU4M"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(UserDesignerLauncher - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbadcHEZJAXTI3JDGJHX3M3PJT6YIRE"),"UserDesignerLauncher - Localizing Bundle",$$$items$$$);
}
