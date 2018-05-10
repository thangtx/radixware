
/* Radix::Reports::GenerateReportActionsProvider - Desktop Executable*/

/*Radix::Reports::GenerateReportActionsProvider-Client-Common Dynamic Class*/

package org.radixware.ads.Reports.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::GenerateReportActionsProvider")
public class GenerateReportActionsProvider  {

	private final String BASE_WINDOW_TITLE = "Choose Report Export Formats";

	/*Radix::Reports::GenerateReportActionsProvider:Nested classes-Nested Classes*/

	/*Radix::Reports::GenerateReportActionsProvider:Properties-Properties*/

	/*Radix::Reports::GenerateReportActionsProvider:enabledFormats-Dynamic Property*/



	protected org.radixware.ads.Reports.common.ReportExportFormat.Arr enabledFormats=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::GenerateReportActionsProvider:enabledFormats")
	private final  org.radixware.ads.Reports.common.ReportExportFormat.Arr getEnabledFormats() {
		return enabledFormats;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::GenerateReportActionsProvider:enabledFormats")
	private final   void setEnabledFormats(org.radixware.ads.Reports.common.ReportExportFormat.Arr val) {
		enabledFormats = val;
	}

	/*Radix::Reports::GenerateReportActionsProvider:generateReportCmd-Dynamic Property*/



	protected org.radixware.kernel.common.client.models.items.Command generateReportCmd=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::GenerateReportActionsProvider:generateReportCmd")
	private final  org.radixware.kernel.common.client.models.items.Command getGenerateReportCmd() {

		try {
		    if (isGenerateOpenMode != null && isGenerateOpenMode.booleanValue()) {
		        final ReportPub:General:Model reportPub2 = (ReportPub:General:Model) Explorer.Models::EntityModel.openContextlessModel(
		                environment,
		                reportPubPid,
		                idof[ReportPub],
		                idof[ReportPub:General]);
		        return reportPub2.getCommand(generateReportCmdId == null ? idof[ReportPub.Contextless:GenerateReport] : generateReportCmdId);
		    }
		} catch (Exceptions::InterruptedException ex) {
		// NOTHING - cancelled
		} catch (Exceptions::ServiceClientException ex) {
		    environment.processException(ex);
		}

		return internal[generateReportCmd];
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::GenerateReportActionsProvider:generateReportCmd")
	private final   void setGenerateReportCmd(org.radixware.kernel.common.client.models.items.Command val) {
		generateReportCmd = val;
	}

	/*Radix::Reports::GenerateReportActionsProvider:isGenerateOpenMode-Dynamic Property*/



	protected Bool isGenerateOpenMode=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::GenerateReportActionsProvider:isGenerateOpenMode")
	private final  Bool getIsGenerateOpenMode() {
		return isGenerateOpenMode;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::GenerateReportActionsProvider:isGenerateOpenMode")
	private final   void setIsGenerateOpenMode(Bool val) {
		isGenerateOpenMode = val;
	}

	/*Radix::Reports::GenerateReportActionsProvider:reportPub-Dynamic Property*/



	protected org.radixware.ads.Reports.explorer.General:Model reportPub=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::GenerateReportActionsProvider:reportPub")
	private final  org.radixware.ads.Reports.explorer.General:Model getReportPub() {

		try {
		    ReportPub:General:Model reportPub = (ReportPub:General:Model) Explorer.Models::EntityModel.openContextlessModel(
		            environment,
		            reportPubPid,
		            idof[ReportPub],
		            idof[ReportPub:General]);

		    return reportPub;
		} catch (Exceptions::InterruptedException ex) {
		// NOTHING - cancelled
		} catch (Exceptions::ServiceClientException ex) {    
		    environment.processException(ex);
		}

		return internal[reportPub];
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::GenerateReportActionsProvider:reportPub")
	private final   void setReportPub(org.radixware.ads.Reports.explorer.General:Model val) {
		reportPub = val;
	}

	/*Radix::Reports::GenerateReportActionsProvider:reportPubPid-Dynamic Property*/



	protected org.radixware.kernel.common.client.types.Pid reportPubPid=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::GenerateReportActionsProvider:reportPubPid")
	private final  org.radixware.kernel.common.client.types.Pid getReportPubPid() {
		return reportPubPid;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::GenerateReportActionsProvider:reportPubPid")
	private final   void setReportPubPid(org.radixware.kernel.common.client.types.Pid val) {
		reportPubPid = val;
	}

	/*Radix::Reports::GenerateReportActionsProvider:environment-Dynamic Property*/



	protected org.radixware.kernel.common.client.IClientEnvironment environment=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::GenerateReportActionsProvider:environment")
	private final  org.radixware.kernel.common.client.IClientEnvironment getEnvironment() {
		return environment;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::GenerateReportActionsProvider:environment")
	private final   void setEnvironment(org.radixware.kernel.common.client.IClientEnvironment val) {
		environment = val;
	}

	/*Radix::Reports::GenerateReportActionsProvider:exportReportCmdId-Dynamic Property*/



	protected org.radixware.kernel.common.types.Id exportReportCmdId=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::GenerateReportActionsProvider:exportReportCmdId")
	private final  org.radixware.kernel.common.types.Id getExportReportCmdId() {
		return exportReportCmdId;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::GenerateReportActionsProvider:exportReportCmdId")
	private final   void setExportReportCmdId(org.radixware.kernel.common.types.Id val) {
		exportReportCmdId = val;
	}

	/*Radix::Reports::GenerateReportActionsProvider:generateReportCmdId-Dynamic Property*/



	protected org.radixware.kernel.common.types.Id generateReportCmdId=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::GenerateReportActionsProvider:generateReportCmdId")
	private final  org.radixware.kernel.common.types.Id getGenerateReportCmdId() {
		return generateReportCmdId;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::GenerateReportActionsProvider:generateReportCmdId")
	private final   void setGenerateReportCmdId(org.radixware.kernel.common.types.Id val) {
		generateReportCmdId = val;
	}

	/*Radix::Reports::GenerateReportActionsProvider:exportReportCmd-Dynamic Property*/



	protected org.radixware.kernel.common.client.models.items.Command exportReportCmd=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::GenerateReportActionsProvider:exportReportCmd")
	private final  org.radixware.kernel.common.client.models.items.Command getExportReportCmd() {

		try {
		    if (isGenerateOpenMode != null && isGenerateOpenMode.booleanValue()) {
		        final ReportPub:General:Model reportPub2 = (ReportPub:General:Model) Explorer.Models::EntityModel.openContextlessModel(
		                environment,
		                reportPubPid,
		                idof[ReportPub],
		                idof[ReportPub:General]);
		        return reportPub2.getCommand(exportReportCmdId == null ? idof[ReportPub.Contextless:GenerateReport] : exportReportCmdId);
		    }
		} catch (Exceptions::InterruptedException ex) {
		// NOTHING - cancelled
		} catch (Exceptions::ServiceClientException ex) {
		    environment.processException(ex);
		}

		return internal[exportReportCmd];
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::GenerateReportActionsProvider:exportReportCmd")
	private final   void setExportReportCmd(org.radixware.kernel.common.client.models.items.Command val) {
		exportReportCmd = val;
	}

	/*Radix::Reports::GenerateReportActionsProvider:Methods-Methods*/

	/*Radix::Reports::GenerateReportActionsProvider:setEnabledFormats-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::GenerateReportActionsProvider:setEnabledFormats")
	public  void setEnabledFormats (org.radixware.ads.Reports.common.ReportExportFormat.Arr enabledFormats) {
		enabledFormats = enabledFormats;
	}

	/*Radix::Reports::GenerateReportActionsProvider:setGenerateReportCmd-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::GenerateReportActionsProvider:setGenerateReportCmd")
	public  void setGenerateReportCmd (org.radixware.kernel.common.client.models.items.Command generateReportCmd) {
		generateReportCmd = generateReportCmd;
	}

	/*Radix::Reports::GenerateReportActionsProvider:setIsGenerateOpenMode-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::GenerateReportActionsProvider:setIsGenerateOpenMode")
	public  void setIsGenerateOpenMode (Bool isGenerateOpenMode) {
		isGenerateOpenMode = isGenerateOpenMode;
	}

	/*Radix::Reports::GenerateReportActionsProvider:setReportPubPid-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::GenerateReportActionsProvider:setReportPubPid")
	public  void setReportPubPid (org.radixware.kernel.common.client.types.Pid reportPubPid) {
		reportPubPid = reportPubPid;
	}

	/*Radix::Reports::GenerateReportActionsProvider:export-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::GenerateReportActionsProvider:export")
	public  void export () {
		Explorer.Models::CommandModel cmd = generateReportCmd;
		if (!cmd.isEnabled()) {
		    environment.messageError("You have no rights to execute reports.");
		    return;
		}

		ChooseReportExportFormatsDialog dialog = new ChooseReportExportFormatsDialog(environment);
		ChooseReportExportFormatsDialog:Model dialogModel = dialog.getModel();

		dialogModel.setWindowTitle("Export. " + BASE_WINDOW_TITLE);
		dialogModel.setEnabledFormats(enabledFormats);
		dialogModel.setDefaultFileName(reportPub.getTitle());
		dialogModel.setReportPubPid(reportPubPid);

		if (dialog.execDialog() == Client.Views::DialogResult.ACCEPTED) {
		    if (!dialogModel.getSelectedFormats().isEmpty()) {
		        if (reportPub != null) {
		            boolean genResult = reportPub.generateReport(cmd, dialogModel.getDirectroryPath(), dialogModel.getFileName(), dialogModel.getSelectedFormats(), false);
		            if (genResult) {
		                environment.messageInformation("Export Complete", "The report was successfully exported");
		            }
		        }
		    }
		}
	}

	/*Radix::Reports::GenerateReportActionsProvider:viewAndExport-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::GenerateReportActionsProvider:viewAndExport")
	public  void viewAndExport () {
		Explorer.Models::CommandModel cmd = generateReportCmd;
		if (!cmd.isEnabled()) {
		    environment.messageError("You have no rights to execute reports.");
		    return;
		}

		ChooseReportExportFormatsDialog dialog = new ChooseReportExportFormatsDialog(environment);
		ChooseReportExportFormatsDialog:Model dialogModel = dialog.getModel();

		dialogModel.setWindowTitle("View and Export. " + BASE_WINDOW_TITLE);
		dialogModel.setEnabledFormats(enabledFormats);
		dialogModel.setDefaultFileName(reportPub.getTitle());
		dialogModel.setReportPubPid(reportPubPid);

		if (dialog.execDialog() == Client.Views::DialogResult.ACCEPTED) {
		    if (!dialogModel.getSelectedFormats().isEmpty()) {
		        if (reportPub != null) {
		            reportPub.generateReport(generateReportCmd, dialogModel.getDirectroryPath(), dialogModel.getFileName(), dialogModel.getSelectedFormats(), true);
		        }        
		    }
		}
	}

	/*Radix::Reports::GenerateReportActionsProvider:viewReport-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::GenerateReportActionsProvider:viewReport")
	public  void viewReport () {
		Explorer.Models::CommandModel cmd = generateReportCmd;
		if (cmd.isEnabled()) {
		    if (reportPub != null) {
		        reportPub.generateReport(cmd, null, null);
		    }
		} else {
		    environment.messageError("You have no rights to execute reports.");
		}
	}

	/*Radix::Reports::GenerateReportActionsProvider:GenerateReportActionsProvider-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::GenerateReportActionsProvider:GenerateReportActionsProvider")
	public  GenerateReportActionsProvider (org.radixware.kernel.common.client.IClientEnvironment environment) {
		environment = environment;
	}

	/*Radix::Reports::GenerateReportActionsProvider:isViewReportEnabled-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::GenerateReportActionsProvider:isViewReportEnabled")
	public  boolean isViewReportEnabled () {
		return reportPubPid != null
		        && (generateReportCmd != null || generateReportCmdId != null)
		        && enabledFormats != null && enabledFormats.contains(ReportExportFormat:PDF);
	}

	/*Radix::Reports::GenerateReportActionsProvider:isViewAndExportEnabled-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::GenerateReportActionsProvider:isViewAndExportEnabled")
	public  boolean isViewAndExportEnabled () {
		return isExportEnabled() && enabledFormats != null && enabledFormats.contains(ReportExportFormat:PDF);
	}

	/*Radix::Reports::GenerateReportActionsProvider:isExportEnabled-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::GenerateReportActionsProvider:isExportEnabled")
	public  boolean isExportEnabled () {
		return reportPubPid != null && (exportReportCmd != null || exportReportCmdId != null);
	}

	/*Radix::Reports::GenerateReportActionsProvider:setExportReportCmd-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::GenerateReportActionsProvider:setExportReportCmd")
	public  void setExportReportCmd (org.radixware.kernel.common.client.models.items.Command exportReportCmd) {
		exportReportCmd = exportReportCmd;
	}

	/*Radix::Reports::GenerateReportActionsProvider:setGenerateReportCmdId-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::GenerateReportActionsProvider:setGenerateReportCmdId")
	public  void setGenerateReportCmdId (org.radixware.kernel.common.types.Id id) {
		generateReportCmdId = id;
	}

	/*Radix::Reports::GenerateReportActionsProvider:setExportReportCmdId-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::GenerateReportActionsProvider:setExportReportCmdId")
	public  void setExportReportCmdId (org.radixware.kernel.common.types.Id id) {
		exportReportCmdId = id;
	}


}

/* Radix::Reports::GenerateReportActionsProvider - Desktop Meta*/

/*Radix::Reports::GenerateReportActionsProvider-Client-Common Dynamic Class*/

package org.radixware.ads.Reports.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class GenerateReportActionsProvider_mi{
}

/* Radix::Reports::GenerateReportActionsProvider - Web Executable*/

/*Radix::Reports::GenerateReportActionsProvider-Client-Common Dynamic Class*/

package org.radixware.ads.Reports.web;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::GenerateReportActionsProvider")
public class GenerateReportActionsProvider  {

	private final String BASE_WINDOW_TITLE = "Choose Report Export Formats";

	/*Radix::Reports::GenerateReportActionsProvider:Nested classes-Nested Classes*/

	/*Radix::Reports::GenerateReportActionsProvider:Properties-Properties*/

	/*Radix::Reports::GenerateReportActionsProvider:enabledFormats-Dynamic Property*/



	protected org.radixware.ads.Reports.common.ReportExportFormat.Arr enabledFormats=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::GenerateReportActionsProvider:enabledFormats")
	private final  org.radixware.ads.Reports.common.ReportExportFormat.Arr getEnabledFormats() {
		return enabledFormats;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::GenerateReportActionsProvider:enabledFormats")
	private final   void setEnabledFormats(org.radixware.ads.Reports.common.ReportExportFormat.Arr val) {
		enabledFormats = val;
	}

	/*Radix::Reports::GenerateReportActionsProvider:generateReportCmd-Dynamic Property*/



	protected org.radixware.kernel.common.client.models.items.Command generateReportCmd=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::GenerateReportActionsProvider:generateReportCmd")
	private final  org.radixware.kernel.common.client.models.items.Command getGenerateReportCmd() {

		try {
		    if (isGenerateOpenMode != null && isGenerateOpenMode.booleanValue()) {
		        final ReportPub:General:Model reportPub2 = (ReportPub:General:Model) Explorer.Models::EntityModel.openContextlessModel(
		                environment,
		                reportPubPid,
		                idof[ReportPub],
		                idof[ReportPub:General]);
		        return reportPub2.getCommand(generateReportCmdId == null ? idof[ReportPub.Contextless:GenerateReport] : generateReportCmdId);
		    }
		} catch (Exceptions::InterruptedException ex) {
		// NOTHING - cancelled
		} catch (Exceptions::ServiceClientException ex) {
		    environment.processException(ex);
		}

		return internal[generateReportCmd];
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::GenerateReportActionsProvider:generateReportCmd")
	private final   void setGenerateReportCmd(org.radixware.kernel.common.client.models.items.Command val) {
		generateReportCmd = val;
	}

	/*Radix::Reports::GenerateReportActionsProvider:isGenerateOpenMode-Dynamic Property*/



	protected Bool isGenerateOpenMode=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::GenerateReportActionsProvider:isGenerateOpenMode")
	private final  Bool getIsGenerateOpenMode() {
		return isGenerateOpenMode;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::GenerateReportActionsProvider:isGenerateOpenMode")
	private final   void setIsGenerateOpenMode(Bool val) {
		isGenerateOpenMode = val;
	}

	/*Radix::Reports::GenerateReportActionsProvider:reportPub-Dynamic Property*/



	protected org.radixware.ads.Reports.web.General:Model reportPub=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::GenerateReportActionsProvider:reportPub")
	private final  org.radixware.ads.Reports.web.General:Model getReportPub() {

		try {
		    ReportPub:General:Model reportPub = (ReportPub:General:Model) Explorer.Models::EntityModel.openContextlessModel(
		            environment,
		            reportPubPid,
		            idof[ReportPub],
		            idof[ReportPub:General]);

		    return reportPub;
		} catch (Exceptions::InterruptedException ex) {
		// NOTHING - cancelled
		} catch (Exceptions::ServiceClientException ex) {    
		    environment.processException(ex);
		}

		return internal[reportPub];
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::GenerateReportActionsProvider:reportPub")
	private final   void setReportPub(org.radixware.ads.Reports.web.General:Model val) {
		reportPub = val;
	}

	/*Radix::Reports::GenerateReportActionsProvider:reportPubPid-Dynamic Property*/



	protected org.radixware.kernel.common.client.types.Pid reportPubPid=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::GenerateReportActionsProvider:reportPubPid")
	private final  org.radixware.kernel.common.client.types.Pid getReportPubPid() {
		return reportPubPid;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::GenerateReportActionsProvider:reportPubPid")
	private final   void setReportPubPid(org.radixware.kernel.common.client.types.Pid val) {
		reportPubPid = val;
	}

	/*Radix::Reports::GenerateReportActionsProvider:environment-Dynamic Property*/



	protected org.radixware.kernel.common.client.IClientEnvironment environment=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::GenerateReportActionsProvider:environment")
	private final  org.radixware.kernel.common.client.IClientEnvironment getEnvironment() {
		return environment;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::GenerateReportActionsProvider:environment")
	private final   void setEnvironment(org.radixware.kernel.common.client.IClientEnvironment val) {
		environment = val;
	}

	/*Radix::Reports::GenerateReportActionsProvider:exportReportCmdId-Dynamic Property*/



	protected org.radixware.kernel.common.types.Id exportReportCmdId=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::GenerateReportActionsProvider:exportReportCmdId")
	private final  org.radixware.kernel.common.types.Id getExportReportCmdId() {
		return exportReportCmdId;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::GenerateReportActionsProvider:exportReportCmdId")
	private final   void setExportReportCmdId(org.radixware.kernel.common.types.Id val) {
		exportReportCmdId = val;
	}

	/*Radix::Reports::GenerateReportActionsProvider:generateReportCmdId-Dynamic Property*/



	protected org.radixware.kernel.common.types.Id generateReportCmdId=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::GenerateReportActionsProvider:generateReportCmdId")
	private final  org.radixware.kernel.common.types.Id getGenerateReportCmdId() {
		return generateReportCmdId;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::GenerateReportActionsProvider:generateReportCmdId")
	private final   void setGenerateReportCmdId(org.radixware.kernel.common.types.Id val) {
		generateReportCmdId = val;
	}

	/*Radix::Reports::GenerateReportActionsProvider:exportReportCmd-Dynamic Property*/



	protected org.radixware.kernel.common.client.models.items.Command exportReportCmd=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::GenerateReportActionsProvider:exportReportCmd")
	private final  org.radixware.kernel.common.client.models.items.Command getExportReportCmd() {

		try {
		    if (isGenerateOpenMode != null && isGenerateOpenMode.booleanValue()) {
		        final ReportPub:General:Model reportPub2 = (ReportPub:General:Model) Explorer.Models::EntityModel.openContextlessModel(
		                environment,
		                reportPubPid,
		                idof[ReportPub],
		                idof[ReportPub:General]);
		        return reportPub2.getCommand(exportReportCmdId == null ? idof[ReportPub.Contextless:GenerateReport] : exportReportCmdId);
		    }
		} catch (Exceptions::InterruptedException ex) {
		// NOTHING - cancelled
		} catch (Exceptions::ServiceClientException ex) {
		    environment.processException(ex);
		}

		return internal[exportReportCmd];
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::GenerateReportActionsProvider:exportReportCmd")
	private final   void setExportReportCmd(org.radixware.kernel.common.client.models.items.Command val) {
		exportReportCmd = val;
	}

	/*Radix::Reports::GenerateReportActionsProvider:Methods-Methods*/

	/*Radix::Reports::GenerateReportActionsProvider:setEnabledFormats-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::GenerateReportActionsProvider:setEnabledFormats")
	public  void setEnabledFormats (org.radixware.ads.Reports.common.ReportExportFormat.Arr enabledFormats) {
		enabledFormats = enabledFormats;
	}

	/*Radix::Reports::GenerateReportActionsProvider:setGenerateReportCmd-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::GenerateReportActionsProvider:setGenerateReportCmd")
	public  void setGenerateReportCmd (org.radixware.kernel.common.client.models.items.Command generateReportCmd) {
		generateReportCmd = generateReportCmd;
	}

	/*Radix::Reports::GenerateReportActionsProvider:setIsGenerateOpenMode-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::GenerateReportActionsProvider:setIsGenerateOpenMode")
	public  void setIsGenerateOpenMode (Bool isGenerateOpenMode) {
		isGenerateOpenMode = isGenerateOpenMode;
	}

	/*Radix::Reports::GenerateReportActionsProvider:setReportPubPid-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::GenerateReportActionsProvider:setReportPubPid")
	public  void setReportPubPid (org.radixware.kernel.common.client.types.Pid reportPubPid) {
		reportPubPid = reportPubPid;
	}

	/*Radix::Reports::GenerateReportActionsProvider:export-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::GenerateReportActionsProvider:export")
	public  void export () {
		Explorer.Models::CommandModel cmd = generateReportCmd;
		if (!cmd.isEnabled()) {
		    environment.messageError("You have no rights to execute reports.");
		    return;
		}

		ChooseReportExportFormatsDialogWeb dialog = new ChooseReportExportFormatsDialogWeb(environment);
		ChooseReportExportFormatsDialogWeb:Model dialogModel = dialog.getModel();

		dialogModel.setWindowTitle("Export. " + BASE_WINDOW_TITLE);
		dialogModel.setEnabledFormats(enabledFormats);
		dialogModel.setDefaultFileName(reportPub.getTitle());
		dialogModel.setReportPubPid(reportPubPid);

		if (dialog.execDialog() == Client.Views::DialogResult.ACCEPTED) {
		    if (!dialogModel.getSelectedFormats().isEmpty()) {
		        try {
		            java.io.File file = java.io.File.createTempFile("Report-", "Dir");
		            file.delete();
		            file.mkdirs();
		            Str fileName = file.AbsolutePath;

		            boolean genResult = reportPub.generateReport(cmd, fileName, dialogModel.getFileName(), dialogModel.getSelectedFormats(), false);
		            if (genResult) {
		                environment.messageInformation("Export Complete", "The report was successfully exported");
		            }
		        } catch (Exceptions::IOException e) {
		            environment.messageError("Export Failed", "Unable to create a temporary directory to export the report to");
		        }
		    }
		}
	}

	/*Radix::Reports::GenerateReportActionsProvider:viewAndExport-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::GenerateReportActionsProvider:viewAndExport")
	public  void viewAndExport () {
		// this action is not available for WEB
	}

	/*Radix::Reports::GenerateReportActionsProvider:viewReport-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::GenerateReportActionsProvider:viewReport")
	public  void viewReport () {
		Explorer.Models::CommandModel cmd = generateReportCmd;
		if (cmd.isEnabled()) {
		    if (reportPub != null) {
		        reportPub.generateReport(cmd, null, null);
		    }
		} else {
		    environment.messageError("You have no rights to execute reports.");
		}
	}

	/*Radix::Reports::GenerateReportActionsProvider:GenerateReportActionsProvider-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::GenerateReportActionsProvider:GenerateReportActionsProvider")
	public  GenerateReportActionsProvider (org.radixware.kernel.common.client.IClientEnvironment environment) {
		environment = environment;
	}

	/*Radix::Reports::GenerateReportActionsProvider:isViewReportEnabled-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::GenerateReportActionsProvider:isViewReportEnabled")
	public  boolean isViewReportEnabled () {
		return reportPubPid != null
		        && (generateReportCmd != null || generateReportCmdId != null)
		        && enabledFormats != null && enabledFormats.contains(ReportExportFormat:PDF);
	}

	/*Radix::Reports::GenerateReportActionsProvider:isViewAndExportEnabled-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::GenerateReportActionsProvider:isViewAndExportEnabled")
	public  boolean isViewAndExportEnabled () {
		return isExportEnabled() && enabledFormats != null && enabledFormats.contains(ReportExportFormat:PDF);
	}

	/*Radix::Reports::GenerateReportActionsProvider:isExportEnabled-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::GenerateReportActionsProvider:isExportEnabled")
	public  boolean isExportEnabled () {
		return reportPubPid != null && (exportReportCmd != null || exportReportCmdId != null);
	}

	/*Radix::Reports::GenerateReportActionsProvider:setExportReportCmd-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::GenerateReportActionsProvider:setExportReportCmd")
	public  void setExportReportCmd (org.radixware.kernel.common.client.models.items.Command exportReportCmd) {
		exportReportCmd = exportReportCmd;
	}

	/*Radix::Reports::GenerateReportActionsProvider:setGenerateReportCmdId-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::GenerateReportActionsProvider:setGenerateReportCmdId")
	public  void setGenerateReportCmdId (org.radixware.kernel.common.types.Id id) {
		generateReportCmdId = id;
	}

	/*Radix::Reports::GenerateReportActionsProvider:setExportReportCmdId-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::GenerateReportActionsProvider:setExportReportCmdId")
	public  void setExportReportCmdId (org.radixware.kernel.common.types.Id id) {
		exportReportCmdId = id;
	}


}

/* Radix::Reports::GenerateReportActionsProvider - Web Meta*/

/*Radix::Reports::GenerateReportActionsProvider-Client-Common Dynamic Class*/

package org.radixware.ads.Reports.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class GenerateReportActionsProvider_mi{
}

/* Radix::Reports::GenerateReportActionsProvider - Localizing Bundle */
package org.radixware.ads.Reports.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class GenerateReportActionsProvider - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Export. ");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Экспорт. ");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls575EJL2HXFEXDJ3AUDIAENBVMM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Export Complete");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Экспорт завершен");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBFR36GGPTNEMBCILFKOD63PJ4E"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"View and Export. ");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Просмотр и экспорт. ");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEZLL4KELGBAVXJPOUI5HA354YI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"The report was successfully exported");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Отчет был успешно экспортирован");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFMWG5TPP7ZDX3IQ6OWVVSYAPWQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"You have no rights to execute reports.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Нет прав на выполнение отчетов.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFNLUJXWQNNCM3GVSSEKJMTTJAQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"You have no rights to execute reports.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Нет прав на выполнение отчетов.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHIGS6CQXWZGYJI5LA364UB37XE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Unable to create a temporary directory to export the report to");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Не удалось создать временную директорию для экспорта отчета");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJHGNY7VPGNES3HAHPIRCYMPWXE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"You have no rights to execute reports.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Нет прав на выполнение отчетов.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsR65UJR6UTNEAPLDXRRIQLDCPBI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Export. ");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Экспорт. ");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsT7S7PPWM3FHYHJK7JETTMFWIJM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Export Failed");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Экспорт не удался");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUEIG5DUEWVDS5KQX6GHG3572BU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"The report was successfully exported");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Отчет был успешно экспортирован");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUUP7T5K2L5AVVKH3ZSCXDRV76Q"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"You have no rights to execute reports.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Нет прав на выполнение отчетов.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsW2722ZOA5NFRZCQK7SJ64AQO44"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Export Complete");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Экспорт завершен");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWKEJ4IPI7JHHDIQZU4OB53T45A"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Choose Report Export Formats");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Выберите форматы экспорта отчета");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYGWK27XHFFEI7GOD3F6OP5H4I4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(GenerateReportActionsProvider - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbadc34SD245TWFCONHHFFSLZAJ5J5E"),"GenerateReportActionsProvider - Localizing Bundle",$$$items$$$);
}
