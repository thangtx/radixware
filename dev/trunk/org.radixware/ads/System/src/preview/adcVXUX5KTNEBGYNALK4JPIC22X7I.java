
/* Radix::System::SysManagementUtils - Server Executable*/

/*Radix::System::SysManagementUtils-Server Dynamic Class*/

package org.radixware.ads.System.server;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::SysManagementUtils")
public class SysManagementUtils  implements org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {


	/**Metainformation accessor method*/
	@SuppressWarnings("unused")
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return SysManagementUtils_mi.rdxMeta;}

	/*Radix::System::SysManagementUtils:Nested classes-Nested Classes*/

	/*Radix::System::SysManagementUtils:Properties-Properties*/





























	/*Radix::System::SysManagementUtils:Methods-Methods*/

	/*Radix::System::SysManagementUtils:getInstancesInfo-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::SysManagementUtils:getInstancesInfo")
	public static  org.radixware.ads.System.common.SysManagementXsd.SystemInstancesInfoDocument getInstancesInfo (org.radixware.kernel.common.types.ArrInt ids) {
		SysManagementXsd:SystemInstancesInfoDocument xDoc = SysManagementXsd:SystemInstancesInfoDocument.Factory.newInstance();

		xDoc.ensureSystemInstancesInfo();

		org.radixware.kernel.server.instance.VersionInfo repoVerInfo = null;

		try {
		    repoVerInfo = Arte::Arte.getInstance().getInstance().rereadAndGetRepoVersionInfo(10000);
		} catch (java.util.concurrent.TimeoutException ex) {
		    throw new AppError("Unable to get information about version in repository: timeout");
		}
		if (repoVerInfo != null) {
		    xDoc.getSystemInstancesInfo().RepoAppVersion = repoVerInfo.getAppVersion();
		    xDoc.getSystemInstancesInfo().RepoKernelVersion = repoVerInfo.getKernelVersion();
		    xDoc.getSystemInstancesInfo().RepoRevision = repoVerInfo.getRevision();
		}

		if (Arte::Arte.getInstance().getInstance().getDdsVersionInfo() != null) {
		    xDoc.getSystemInstancesInfo().DbVersion = Arte::Arte.getInstance().getInstance().getDdsVersionInfo().getVersionAsString();
		}

		if (Arte::Arte.getInstance().getInstance().getAadcManager().isInAadc()) {
		    try {
		        long[] stickedVersions = org.radixware.kernel.starter.Starter.readAadcStickedVersions();
		        xDoc.getSystemInstancesInfo().AadcMember1StickedVersion = stickedVersions[0];
		        xDoc.getSystemInstancesInfo().AadcMember2StickedVersion = stickedVersions[1];
		    } catch (Exception ex) {
		        throw new AppError("Unable to read AADC sticked versions", ex);
		    }
		}

		if (ids == null || ids.isEmpty()) {
		    return xDoc;
		}

		xDoc.ensureSystemInstancesInfo().ensureSystemInstances();

		java.util.Map<Int, Boolean> kernelCompatibilityMap = new java.util.HashMap<>();

		for (Int id : ids) {
		    final Instance instance = Instance.loadByPK(id, true);
		    final SysManagementXsd:SystemInstanceInfo info = xDoc.getSystemInstancesInfo().getSystemInstances().addNewSystemInstanceInfo();
		    info.setId(instance.id);
		    if (instance.title != null) {
		        info.setTitle(instance.title);
		    }
		    if (instance.kernelVersion != null) {
		        info.setKernelVersion(instance.kernelVersion);
		    }
		    if (instance.appVersion != null) {
		        info.setAppVersion(instance.appVersion);
		    }

		    if (Arte::Arte.getInstance().getInstance().getAadcManager().isInAadc() && instance.aadcMemberId != null) {
		        info.setAadcMemberId(instance.aadcMemberId);
		    }

		    Boolean kernelCompatible = null;

		    if (instance.revision != null) {
		        info.setRevision(instance.revision);
		        kernelCompatible = kernelCompatibilityMap.get(instance.revision);
		        if (kernelCompatible == null) {
		            try {
		                final org.radixware.kernel.starter.meta.RevisionMeta repoMeta = org.radixware.kernel.starter.radixloader.RadixLoader.getInstance().getRevisionMeta(repoVerInfo.Revision, org.radixware.kernel.starter.radixloader.ERevisionMetaType.LAYERS_ONLY);
		                final org.radixware.kernel.starter.meta.RevisionMeta instMeta = org.radixware.kernel.starter.radixloader.RadixLoader.getInstance().getRevisionMeta(instance.revision.longValue(), org.radixware.kernel.starter.radixloader.ERevisionMetaType.LAYERS_ONLY);
		                kernelCompatible = instMeta.isKernelCompatibleWhenUpgradingTo(repoMeta);
		            } catch (Exception ex) {
		                Arte::Trace.put(eventCode["Unable to determine kernel compatibility between repository revision %1 and revision %2 of Instance~%3: %4"],
		                        new ArrStr(
		                                Int.toString(repoVerInfo == null ? -1l : repoVerInfo.Revision),
		                                instance.revision.toString(),
		                                instance.id.toString(),
		                                Utils::ExceptionTextFormatter.throwableToString(ex)));
		                kernelCompatible = false;
		            }
		            kernelCompatibilityMap.put(instance.revision, kernelCompatible);
		        }
		    } else {
		        kernelCompatible = repoVerInfo != null && java.util.Objects.equals(instance.kernelVersion, repoVerInfo.getKernelVersion());
		    }

		    info.setKernelCompatibleToRepo(kernelCompatible);

		    if (Arte::Arte.getInstance().getInstance().getId() == id) {
		        info.setArteCount(Arte::Arte.getInstance().getInstance().getArtePool().getSize());
		        info.setAvgActiveArteCount(Arte::Arte.getInstance().getInstance().getInstanceMonitor().getAvgActiveArteCount());
		    } else {
		        info.setArteCount(instance.arteInstCount);
		        info.setAvgActiveArteCount(instance.avgActiveArteCount);
		    }
		    info.setActivityStatus(instance.activityStatus.Value);
		    if (instance.osPid != null) {
		        info.setOsPid(instance.osPid);
		    }
		    if (instance.startTimeMillis != null) {
		        info.setStartTimeMillis(instance.startTimeMillis);
		    }
		}

		return xDoc;

	}

	/*Radix::System::SysManagementUtils:doExecTask-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::SysManagementUtils:doExecTask")
	private static  org.radixware.kernel.server.types.FormHandler.NextDialogsRequest doExecTask (org.radixware.ads.System.common.SysManagementXsd.ManageInstancesTask task) {
		final InstanceManagementAction action = task.Action;

		final String actionTitle = InstanceManagementAction.getRadMeta().getItemByVal(action.Value).getTitle(Arte::Arte.getInstance());

		if (action == InstanceManagementAction:RESTART) {
		    int thisInstanceIdx = -1;
		    for (int i = 0; i < task.getTaskItemList().size(); i++) {
		        if (task.getTaskItemList().get(i).getId() == Arte::Arte.getInstance().getInstance().getId()) {
		            thisInstanceIdx = i;
		            break;
		        }
		    }

		    if (thisInstanceIdx >= 0 && task.getTaskItemList().get(task.getTaskItemList().size() - 1).Id != Arte::Arte.getInstance().getInstance().getId()) {
		        try {
		            task = (SysManagementXsd:ManageInstancesTask) task.copy();
		            final String title = "Reordering Required";
		            final String text = String.format("Instance processing this command ('%s') should be the last in restart order, do you agree to move it to the last position? If not, operation will be aborted.", Instance.loadByPK(Arte::Arte.getInstance().getInstance().getId(), true).calcTitle());
		            if (!Client.Resources::MessageDialogResource.confirmation(Arte::Arte.getInstance(), title, text)) {
		                return new FormHandlerNextDialogsRequest(new FormHandlerMessageBoxRequest(Client.Resources::DialogType:Error, null, null, null, "Operation has been aborted"), null);
		            } else {
		                task.addNewTaskItem().set(task.getTaskItemList().get(thisInstanceIdx));
		                task.getTaskItemList().remove(thisInstanceIdx);
		                if (thisInstanceIdx == 0) {
		                    Long maxDelaySec = null;
		                    for (SysManagementXsd:ManageInstancesTask.TaskItem item : task.getTaskItemList()) {
		                        if (maxDelaySec == null || (item.getDelaySec() != null && maxDelaySec.longValue() < item.getDelaySec().longValue())) {
		                            maxDelaySec = item.getDelaySec();
		                        }
		                    }
		                    task.getTaskItemList().get(0).setDelaySec(0);
		                    final Long thisDelaySec = task.getTaskItemList().get(task.getTaskItemList().size() - 1).getDelaySec();
		                    if ((thisDelaySec == null || thisDelaySec == 0) && maxDelaySec != null && maxDelaySec.longValue() > 0) {
		                        if (!Client.Resources::MessageDialogResource.confirmation(Arte::Arte.getInstance(), "Confirmation", String.format("Reordered instance had zero delay, now it's delay will be set to '%s' (maximum delay). Select 'No' for abort.", maxDelaySec))) {
		                            return new FormHandlerNextDialogsRequest(new FormHandlerMessageBoxRequest(Client.Resources::DialogType:Error, null, null, null, "Operation has been aborted"), null);
		                        } else {
		                            task.getTaskItemList().get(task.getTaskItemList().size() - 1).setDelaySec(maxDelaySec);
		                        }
		                    }
		                }

		            }
		        } catch (Exception ex) {
		            throw new RuntimeException("Error", ex);
		        }
		    }
		}

		final String warningColor = "darkorange";
		final String errorColor = "red";
		final String goodColor = "green";

		class Tracer {

		    final java.util.List<Meta::TraceItem> log = new java.util.ArrayList<Meta::TraceItem>();
		    final Client.Resources::ProgressDialogProcessResource process;
		    final java.text.SimpleDateFormat timeFormat = new java.text.SimpleDateFormat("HH:mm:ss.SSS");
		    final java.text.SimpleDateFormat dateTimeFormat = new java.text.SimpleDateFormat(Meta::TraceItem.getTimeFormat());

		    public Tracer(Client.Resources::ProgressDialogProcessResource process) {
		        this.process = process;
		    }

		    public void trace(org.radixware.schemas.eas.TraceLevelEnum.Enum level, Str message) throws Exceptions::ResourceUsageException {
		        Arte::EventSeverity sev = getForValue(level);
		        Meta::TraceItem item = new Meta::TraceItem(null, sev, null, new ArrStr(message), Arte::EventSource:Upgrade.Value);
		        log.add(item);
		        process.trace(level, timeFormat.format(new java.util.Date(item.time)) + " : " + message);
		        Arte::Trace.put(sev, message, Arte::EventSource:Upgrade);
		    }

		    public boolean flushTrace() throws Exceptions::ResourceUsageException, Exceptions::ResourceUsageTimeout, Exceptions::InterruptedException {
		        return process.flushTrace();
		    }

		    public java.util.List<Meta::TraceItem> getLog() {
		        return log;
		    }

		    private Arte::EventSeverity getForValue(org.radixware.schemas.eas.TraceLevelEnum.Enum level) {
		        if (level == Arte::EasXsd:TraceLevelEnum.ALARM) {
		            return Arte::EventSeverity:Alarm;
		        } else if (level == Arte::EasXsd:TraceLevelEnum.ERROR) {
		            return Arte::EventSeverity:Error;
		        } else if (level == Arte::EasXsd:TraceLevelEnum.WARNING) {
		            return Arte::EventSeverity:Warning;
		        } else if (level == Arte::EasXsd:TraceLevelEnum.EVENT) {
		            return Arte::EventSeverity:Event;
		        } else if (level == Arte::EasXsd:TraceLevelEnum.DEBUG) {
		            return Arte::EventSeverity:Debug;
		        } else if (level == Arte::EasXsd:TraceLevelEnum.NONE) {
		            return Arte::EventSeverity:None;
		        } else {
		            throw new IllegalArgumentException("Not defined severity type: " + level);
		        }
		    }

		    public String eventColor(Arte::EventSeverity sev) {
		        if (sev == null) {
		            return null;
		        }
		        switch (sev) {
		            case Arte::EventSeverity:Alarm:
		            case Arte::EventSeverity:Error:
		                return errorColor;
		            case Arte::EventSeverity:Warning:
		                return warningColor;
		            default:
		                return null;
		        }
		    }

		    public String[] dateAndTime(Meta::TraceItem item) {
		        return dateTimeFormat.format(new java.util.Date(item.time)).split(" ");
		    }

		}

		class InstanceInfo {

		    long id;
		    String title;
		    ActivityStatus activityStatus;
		    int startedUnitsCount;
		    int arteCount;
		    double avgActiveArteCount;
		    int dbSessionsCount;
		    String comment = "";
		    String commentColor = null;
		    long startTimeMillis;
		    long initialStartTimeMillis = -1;
		    boolean reachable = false;
		    long pid;
		    boolean restartSent = false;
		    boolean startUsingNewVerSent = false;
		    Tracer tracer;
		    long delayMillis = 0;
		    boolean preloadedNextVer = false;
		    boolean stoppedUsingCurVer = false;
		    boolean startedUsingNewVer = false;

		    public void check() {
		        try {
		            Instance instance = Instance.loadByPK(id, true);
		            instance.getStatus();//check availability
		            reachable = true;
		            commentColor = goodColor;
		            comment = "Control service is reachable";
		        } catch (Exception ex) {
		            comment = ex.getClass().getSimpleName() + ": " + ex.getMessage();
		            commentColor = errorColor;
		        }
		    }

		    public void sendRestart(Long hardStopDelaySec) throws Exception {
		        Instance instance = Instance.loadByPK(id, true);
		        instance.restartInstance(hardStopDelaySec);
		        comment = "Restart request has been sent";
		        commentColor = warningColor;
		        tracer.trace(Arte::EasXsd:TraceLevelEnum.EVENT, String.format("Restart request has been sent to the instance '%s'", title));
		        tracer.flushTrace();
		        restartSent = true;
		    }

		    public boolean sendStartUsingNewVer() throws Exception {
		        final Instance instance = Instance.loadByPK(id, true);
		        try {
		            if (!startUsingNewVerSent) {
		                InstanceControlServiceXsd:UpgradeInstanceMess mess = InstanceControlServiceXsd:UpgradeInstanceMess.Factory.newInstance();
		                mess.addNewStartUsingNextAppVersionMess().addNewStartUsingNextAppVersionRq().setUser(Arte::Arte.getUserName());
		                InstanceControlServiceXsd:UpgradeInstanceMess startUsingNewVer = instance.execUpgradeTask(mess);
		                comment = "Request for start using new application version sent" + " - " + startUsingNewVer.getStartUsingNextAppVersionMess().getStartUsingNextAppVersionRs().Result;
		                commentColor = warningColor;
		                tracer.trace(Arte::EasXsd:TraceLevelEnum.EVENT, String.format("Request for start using new application version has been sent to instance '%s'", title));
		                startUsingNewVerSent = true;
		            }
		            return true;
		        } catch (Exception ex) {
		            final String errorText = ex.getClass().getSimpleName() + ": " + ex.getMessage();
		            comment = "Error when sending request for start using new application version" + ": " + errorText;
		            commentColor = errorColor;
		            tracer.trace(Arte::EasXsd:TraceLevelEnum.ERROR, String.format("Error sending the request to start using the new application version to instance '%s': %s", title, errorText));
		            return false;
		        }
		    }

		    public boolean isRestartSent() {
		        return restartSent;
		    }

		    public boolean isRestarted() {
		        return startTimeMillis != initialStartTimeMillis;
		    }

		    public void reread() {
		        Instance instance = Instance.loadByPK(id, true);
		        instance.reread();
		        title = instance.calcTitle();
		        activityStatus = instance.activityStatus;
		        if (instance.startTimeMillis != null) {
		            startTimeMillis = instance.startTimeMillis.longValue();
		            if (initialStartTimeMillis < 0) {
		                initialStartTimeMillis = startTimeMillis;
		            }
		        }
		        pid = instance.osPid != null ? instance.osPid.longValue() : -1;
		        try (InstanceExtendedStateCur extStateCur = InstanceExtendedStateCur.open(id)) {
		            if (extStateCur.next()) {
		                startedUnitsCount = extStateCur.startedUnitsCount.intValue();
		                dbSessionsCount = extStateCur.dbSessionsCount.intValue();
		                if (id == Arte::Arte.getInstance().getInstance().getId()) {
		                    arteCount = Arte::Arte.getInstance().getInstance().getArtePool().getSize();
		                    avgActiveArteCount = Arte::Arte.getInstance().getInstance().getInstanceMonitor().getAvgActiveArteCount();
		                } else {
		                    arteCount = extStateCur.arteCount.intValue();
		                    avgActiveArteCount = extStateCur.avgActiveArteCount.doubleValue();
		                }
		            } else {
		                startedUnitsCount = -1;
		                arteCount = -1;
		                dbSessionsCount = -1;
		                avgActiveArteCount = -1;
		            }
		        }
		    }

		    public String getComment() {
		        return comment + (id == Arte::Arte.getInstance().getInstance().getId() ? " (" + "instance is processing this command" + ")" : "");
		    }
		}

		class InstanceInfos {

		    java.util.List<InstanceInfo> list = new java.util.ArrayList<InstanceInfo>();
		    String comment = "";
		    long startMillis = -1;
		    long timeoutMillis = -1;
		    SysManagementXsd:ManageInstancesTask task;
		    boolean needProcessThis = false;
		    Client.Resources::ProgressDialogProcessResource process;
		    Tracer tracer;
		    long firstStopUsingVerRqMillis = -1;

		    InstanceInfos(SysManagementXsd:ManageInstancesTask task) {
		        this.task = task;
		        for (SysManagementXsd:ManageInstancesTask.TaskItem item : task.getTaskItemList()) {
		            add(item.getId().longValue());
		            list.get(list.size() - 1).delayMillis = item.getDelaySec() == null ? 0 : item.getDelaySec().longValue() * 1000;
		        }
		    }

		    public void add(long id) {
		        InstanceInfo info = new InstanceInfo();
		        info.id = id;
		        info.reread();
		        info.check();
		        list.add(info);
		        if (id == Arte::Arte.getInstance().getInstance().getId()) {
		            needProcessThis = true;
		        }
		    }

		    public void rereadAll() {
		        for (InstanceInfo info : list) {
		            info.reread();
		        }
		    }

		    public void sendStopToAll() throws Exception {
		        for (InstanceInfo info : list) {
		            if (info.reachable) {
		                final Instance instance = Instance.loadByPK(info.id, true);
		                rereadAll();
		                update();
		                try {
		                    InstanceControlServiceXsd:StopInstanceRs stopInstanceRs = instance.stopInstance(timeoutMillis / 1000 + (instance.id == Arte::Arte.getInstance().getInstance().getId() ? 5 : 0));
		                    info.comment = "Stop request sent, result code" + ": " + stopInstanceRs.Result;
		                    info.commentColor = warningColor;
		                } catch (Exception ex) {
		                    info.comment = "Error after sending the request to stop" + ": " + ex.getClass().getSimpleName() + ": " + ex.getMessage();
		                    info.commentColor = errorColor;
		                }
		                rereadAll();
		                update();
		            }
		        }
		    }

		    public boolean sendPreloadToAll() throws Exception {
		        boolean ok = true;
		        for (InstanceInfo info : list) {
		            if (info.reachable) {
		                final Instance instance = Instance.loadByPK(info.id, true);
		                try {
		                    InstanceControlServiceXsd:UpgradeInstanceMess mess = InstanceControlServiceXsd:UpgradeInstanceMess.Factory.newInstance();
		                    mess.addNewPreloadNextVersionMess().addNewPreloadNextVersionRq().setUser(Arte::Arte.getUserName());
		                    InstanceControlServiceXsd:UpgradeInstanceMess preloadNextRs = instance.execUpgradeTask(mess);
		                    info.comment = "Request for downloading new version sent" + " - " + preloadNextRs.getPreloadNextVersionMess().getPreloadNextVersionRs().Result;
		                    info.commentColor = warningColor;
		                    tracer.trace(Arte::EasXsd:TraceLevelEnum.EVENT, String.format("Request for downloading new version has been sent to instance '%s'", info.title));
		                } catch (Exception ex) {
		                    final String errorText = ex.getClass().getSimpleName() + ": " + ex.getMessage();
		                    info.comment = "Error sending the request to download the new version" + ": " + errorText;
		                    info.commentColor = errorColor;
		                    tracer.trace(Arte::EasXsd:TraceLevelEnum.ERROR, String.format("Error when sending request for downloading new version to instance '%s': %s", info.title, errorText));
		                    ok = false;
		                }
		                rereadAll();
		                update();
		            }
		        }
		        return ok;
		    }

		    public boolean sendStopUsingCurVerToAll() throws Exception {
		        for (InstanceInfo info : list) {
		            if (info.reachable) {
		                final Instance instance = Instance.loadByPK(info.id, true);
		                try {
		                    InstanceControlServiceXsd:UpgradeInstanceMess mess = InstanceControlServiceXsd:UpgradeInstanceMess.Factory.newInstance();
		                    mess.addNewStopUsingCurAppVersionMess().addNewStopUsingCurAppVersionRq().setUser(Arte::Arte.getUserName());
		                    if (task.getHardStopDelaySec() != null) {
		                        mess.getStopUsingCurAppVersionMess().getStopUsingCurAppVersionRq().MaxWaitSec = task.getHardStopDelaySec();
		                    }
		                    InstanceControlServiceXsd:UpgradeInstanceMess stopUsingCurVer = instance.execUpgradeTask(mess);
		                    info.comment = "Request for stop using current application version sent" + " - " + stopUsingCurVer.getStopUsingCurAppVersionMess().getStopUsingCurAppVersionRs().Result;
		                    info.commentColor = warningColor;
		                    tracer.trace(Arte::EasXsd:TraceLevelEnum.EVENT, String.format("Request for stop using current application version has been sent to instance '%s'", info.title));
		                    if (firstStopUsingVerRqMillis == -1) {
		                        firstStopUsingVerRqMillis = System.currentTimeMillis();
		                    }
		                } catch (Exception ex) {
		                    final String errorText = ex.getClass().getSimpleName() + ": " + ex.getMessage();
		                    info.comment = "Error sending the request to stop using the current application version" + ": " + errorText;
		                    info.commentColor = errorColor;
		                    tracer.trace(Arte::EasXsd:TraceLevelEnum.ERROR, String.format("Error sending the request to stop using the current application version to instance '%s': %s", info.title, errorText));
		                    return false;
		                }

		            }
		        }
		        rereadAll();
		        update();
		        return true;
		    }

		    public Types::FormHandlerNextDialogsRequest checkStopIsDone() {
		        System.out.println("Check stop is done");
		        if (checkOnlyThisIsIsLeft()) {
		            String inactiveMarkedAsStopped = "";
		            if (task.MarkInactiveAsStopped != null && task.MarkInactiveAsStopped.booleanValue()) {
		                final java.lang.StringBuilder sb = new java.lang.StringBuilder();
		                for (InstanceInfo info : list) {
		                    if (info.activityStatus == ActivityStatus:INACTIVE) {
		                        SetInstanceStoppedWithUnitsCur.execute(info.id);
		                        sb.append(escapeHtml(info.title));
		                        sb.append("<br/>");
		                    }
		                }
		                if (sb.length() > 0) {
		                    inactiveMarkedAsStopped = "The following inactive instances were marked as stopped" + ":<br/><br/>" + sb.toString();
		                    rereadAll();
		                }

		            }
		            return prepareFinalResponse(inactiveMarkedAsStopped);

		        }
		        return null;
		    }

		    public Types::FormHandlerNextDialogsRequest prepareFinalResponse(final String prefix) {
		        return prepareFinalResponse(prefix, Client.Resources::DialogType:Information);
		    }

		    public Types::FormHandlerNextDialogsRequest prepareFinalResponse(final String prefix, Client.Resources::DialogType dialogType) {
		        final String endHtml = prefix
		                + "<br/>"
		                + (needProcessThis && action != InstanceManagementAction:SWITCH_VERSION ? "Instance processing the current command is still running and should stop soon. Please continue monitoring at the OS level." : "")
		                + "<br/>"
		                + "State of instances on command completion:";
		        comment = endHtml;

		        SysManagementResultForm form = new SysManagementResultForm(null);
		        form.htmlReport = toHtml(true);
		        return new FormHandlerNextDialogsRequest(null, form);
		    }

		    public boolean checkOnlyThisIsIsLeft() {
		        for (InstanceInfo info : list) {
		            if (info.id != Arte::Arte.getInstance().getInstance().getId()) {
		                if (info.activityStatus == ActivityStatus:RUNNING) {
		                    return false;
		                }
		                if (action == InstanceManagementAction:STOP) {
		                    info.comment = "Stopped";
		                    info.commentColor = errorColor;
		                }
		            } else {
		                if (info.reachable) {
		                    if (!stopAllInThisInstanceExceptThisArte()) {
		                        return false;
		                    }
		                } else {
		                    return false;
		                }
		            }
		        }
		        return true;
		    }

		    public boolean checkAllPreloadedNextVer() {
		        boolean ok = true;
		        for (InstanceInfo info : list) {
		            if (!info.preloadedNextVer) {
		                Instance inst = Instance.loadByPK(info.id, true);
		                try {
		                    InstanceControlServiceXsd:GetStatusRs rs = inst.getStatus();
		                    final int exceptThis = Arte::Arte.getInstance().getInstance().getId() == info.id ? 1 : 0;
		                    if (rs.isSetPendingAppVersion()) {
		                        if (rs.isSetArteWithoutPendingVersionCount() && (rs.getArteWithoutPendingVersionCount().longValue() - exceptThis <= 0)) {
		                            info.preloadedNextVer = true;
		                            info.comment = "New version loaded";
		                            info.commentColor = warningColor;
		                            tracer.trace(Arte::EasXsd:TraceLevelEnum.EVENT, String.format("Instance '%s' loaded new version", info.title));
		                        } else {
		                            ok = false;
		                            info.comment = String.format("ARTE count without new version: %s", rs.getArteWithoutPendingVersionCount().longValue() - exceptThis);
		                            info.commentColor = warningColor;
		                        }
		                    } else {
		                        ok = false;
		                    }
		                } catch (Exception ex) {
		                    info.comment = prepareErrorComment(ex);
		                    info.commentColor = errorColor;
		                    ok = false;
		                }
		            }
		        }
		        return ok;
		    }

		    public boolean checkAllStoppedUsingCurVer() {
		        boolean ok = true;
		        for (InstanceInfo info : list) {
		            if (!info.stoppedUsingCurVer) {
		                Instance inst = Instance.loadByPK(info.id, true);
		                try {
		                    InstanceControlServiceXsd:GetStatusRs rs = inst.getStatus();
		                    final int exceptThis = Arte::Arte.getInstance().getInstance().getId() == info.id ? 1 : 0;
		                    if (rs.isSetArteProcessingCurVerCount() && (rs.getArteProcessingCurVerCount().longValue() - exceptThis <= 0)) {
		                        info.stoppedUsingCurVer = true;
		                        info.comment = "Stopped using current version";
		                        info.commentColor = warningColor;
		                        tracer.trace(Arte::EasXsd:TraceLevelEnum.EVENT, String.format("Instance '%s' stopped using current version", info.title));
		                    } else {
		                        if (rs.isSetArteProcessingCurVerCount()) {
		                            info.comment = String.format("ARTE's processing requests in current version: %s", rs.getArteProcessingCurVerCount().longValue() - exceptThis);
		                            info.commentColor = warningColor;
		                        }
		                        ok = false;
		                    }
		                } catch (Exception ex) {
		                    info.comment = prepareErrorComment(ex);
		                    info.commentColor = errorColor;
		                    ok = false;
		                }
		            }
		        }
		        return ok;
		    }

		    public boolean checkAllUsingNewVer() {
		        boolean ok = true;
		        for (InstanceInfo info : list) {
		            if (!info.startUsingNewVerSent) {
		                ok = false;
		            } else if (!info.startedUsingNewVer) {
		                Instance inst = Instance.loadByPK(info.id, true);
		                try {
		                    InstanceControlServiceXsd:GetStatusRs rs = inst.getStatus();
		                    if (!rs.isSetPendingAppVersion()) {
		                        info.startedUsingNewVer = true;
		                        info.comment = "Switched to new version";
		                        info.commentColor = goodColor;
		                        tracer.trace(Arte::EasXsd:TraceLevelEnum.EVENT, String.format("Instance '%s' started using new version", info.title));
		                    }
		                } catch (Exception ex) {
		                    info.comment = prepareErrorComment(ex);
		                    info.commentColor = errorColor;
		                    ok = false;
		                }
		            }
		        }
		        return ok;
		    }

		    private String prepareErrorComment(final Throwable t) {
		        return "Error" + ": " + t.getClass().getName() + ": " + t.getMessage();
		    }

		    public boolean stopAllInThisInstanceExceptThisArte() {
		        int runningNonArteUnits = 0;
		        for (org.radixware.kernel.server.units.Unit u : Arte::Arte.getInstance().getInstance().getUnits()) {
		            if (u.getState() != org.radixware.kernel.server.units.UnitState.STOPPED && !(u instanceof org.radixware.kernel.server.units.arte.ArteUnit)) {
		                runningNonArteUnits++;
		            }
		        }
		        if (runningNonArteUnits > 0) {
		            return false;
		        }
		        Arte::Arte.getInstance().getInstance().getArtePool().shutdown(0);
		        if (Arte::Arte.getInstance().getInstance().getArtePool().getSize() > 1) {
		            return false;
		        }
		        return true;
		    }

		    public String toHtml(boolean includeLog) {
		        final StringBuilder sb = new StringBuilder();
		        sb.append("<html>");
		        sb.append("<head><meta charset=\"utf-8\"></head>");
		        sb.append("<div style=\"max-width:960px; margin:auto\">"); //content div to center tables
		        sb.append(comment);
		        sb.append("<table width=\"100%\" align=\"center\" border=1>");
		        sb.append("<tr>"
		                + "<th>" + "Instance" + "</th>"
		                + "<th>" + "Status" + "</th>"
		                + "<th>" + "Units Started" + "</th>"
		                + "<th>" + "Number of ARTEs" + "</th>"
		                + "<th>" + "Number of DB Sessions" + "</th>"
		                + "<th>" + "Comment" + "</th>"
		                + "</tr>"
		        );
		        final long curMillis = System.currentTimeMillis();
		        for (InstanceInfo info : list) {
		            final String statusStr = info.activityStatus.Title + (info.activityStatus == ActivityStatus:RUNNING && info.startTimeMillis >= 0 ? " (" + Utils::Timing.millisToDurationString(curMillis - info.startTimeMillis) + ", PID: " + info.pid + ")" : "");
		            sb.append("<tr>"
		                    + cell(info.title)
		                    + cell(statusStr, statusColor(info.activityStatus))
		                    + cell(String.valueOf(info.startedUnitsCount))
		                    + cell(String.valueOf(info.arteCount) + " (~ " + String.format("%.2f", info.avgActiveArteCount) + " " + "active" + ")")
		                    + cell(String.valueOf(info.dbSessionsCount))
		                    + cell(info.getComment(), info.commentColor)
		                    + "</tr>"
		            );
		        }
		        sb.append("</table>");
		        if (includeLog && tracer != null && !tracer.getLog().isEmpty()) {
		            sb.append("Operation log:");
		            sb.append("<table width=\"100%\" align=\"center\" border=1>");
		            sb.append("<tr>"
		                    + "<th>" + "Severity" + "</th>"
		                    + "<th>" + "Date" + "</th>"
		                    + "<th>" + "Time" + "</th>"
		                    + "<th>" + "Message" + "</th>"
		                    + "</tr>"
		            );
		            for (Meta::TraceItem it : tracer.getLog()) {
		                final String color = tracer.eventColor(it.severity);
		                final String[] dateAndTime = tracer.dateAndTime(it);
		                sb.append("<tr>")
		                        .append(cell(it.severity.Name, "left", color))
		                        .append(cell(dateAndTime[0], "left", color))
		                        .append(cell(dateAndTime[1], "left", color))
		                        .append(cell(it.getMess(), "left", color))
		                        .append("</tr>");
		            }
		            sb.append("</table>");
		        }
		        sb.append("</div>");
		        sb.append("</html>");
		        return sb.toString();
		    }

		    private String statusColor(ActivityStatus status) {
		        if (status == null) {
		            return null;
		        }
		        switch (status) {
		            case ActivityStatus:INACTIVE:
		                return warningColor;
		            case ActivityStatus:RUNNING:
		                return goodColor;
		            case ActivityStatus:STOPPED:
		                return errorColor;
		            default:
		                return null;
		        }
		    }

		    private String cell(final String text) {
		        return cell(text, "center", null);
		    }

		    private String cell(final String text, final String rgbColor) {
		        return cell(text, "center", rgbColor);
		    }

		    private String cell(final String text, final String align, final String rgbColor) {
		        return "<td align=\"" + align + "\">"
		                + (rgbColor == null ? "" : ("<font color=\"" + rgbColor + "\">"))
		                + getHtmlTextWithWrapWords(escapeHtml(text))
		                + (rgbColor == null ? "" : "</font>")
		                + "</td>";
		    }

		    public void update() throws Exception {
		        Float progress = timeoutMillis < 0 ? null : (startMillis == -1 ? null : 100 * Math.min((System.currentTimeMillis() - startMillis) / 1000f / timeoutMillis, 1f));
		        tracer.flushTrace();
		        if (process.set(toHtml(false), progress, true)) {
		            throw new AppError("Operation cancelled by user. Please note that initiated operations were not interrupted.");
		        }
		    }
		}

		InstanceInfos infos = new InstanceInfos(task);

		if (infos.list.isEmpty()) {
		    return infos.prepareFinalResponse("Instances list is empty");
		}

		try {
		    final java.lang.StringBuilder sb = new java.lang.StringBuilder();
		    for (InstanceInfo info : infos.list) {
		        if (!info.reachable) {
		            sb.append(escapeHtml(info.title));
		            sb.append("<br/>");
		        }
		    }
		    if (!Client.Resources::MessageDialogResource.confirmation(Arte::Arte.getInstance(), String.format("Confirm \"%s\" Operation", actionTitle), String.format("Do you really want to perform the \"%s\" operation on the following instances?", actionTitle) + "<br>" + (sb.length() > 0 ? "<br/><font color=\"" + warningColor + "\">" + "Please note that the following instances are unreachable, and operation will not be performed on them" + ":</font><br/>" + sb.toString() + "<br/>" : "") + infos.toHtml(false))) {
		        return null;
		    }
		} catch (Exception ex) {
		    throw new AppError("Error", ex);
		}

		infos.startMillis = System.currentTimeMillis();
		int timeoutMillis = action == InstanceManagementAction:STOP ? task.getHardStopDelaySec().intValue() * 1000 : -1;
		infos.timeoutMillis = timeoutMillis;

		Client.Resources::ProgressDialogResource progress = new ProgressDialogResource(Arte::Arte.getInstance(), actionTitle, action != InstanceManagementAction:STOP);

		try {
		    final Client.Resources::ProgressDialogProcessResource process = progress.startNewProcess(actionTitle, true, true);
		    final Tracer tracer = new Tracer(process);

		    try {

		        infos.process = process;
		        infos.tracer = tracer;
		        for (InstanceInfo info : infos.list) {
		            info.tracer = tracer;
		        }

		        if (action == InstanceManagementAction:STOP) {
		            infos.sendStopToAll();
		        }

		        int curIndex = 0;
		        long prevInstTimeMillis = System.currentTimeMillis();
		        long delayMillis = 0;
		        boolean preloadSent = false;
		        boolean stopUsingCurVerSent = false;
		        boolean startUsingNewVerSent = false;
		        final boolean rollingSwitch = task.SwitchRolling != null && task.SwitchRolling.booleanValue();

		        while (timeoutMillis < 0 || System.currentTimeMillis() - infos.startMillis < timeoutMillis) {
		            infos.rereadAll();

		            if (action == InstanceManagementAction:STOP) {
		                final FormHandlerNextDialogsRequest stopResult = infos.checkStopIsDone();
		                if (stopResult != null) {
		                    return stopResult;
		                }
		            }

		            if (action == InstanceManagementAction:RESTART) {
		                if (System.currentTimeMillis() - prevInstTimeMillis > delayMillis) {
		                    InstanceInfo info = infos.list.get(curIndex);
		                    if (!info.isRestartSent()) {
		                        info.sendRestart(task.getHardStopDelaySec());
		                    }
		                    boolean allDone = false;
		                    if (info.isRestarted()) {
		                        tracer.trace(Arte::EasXsd:TraceLevelEnum.EVENT, String.format("Instance '%s' has been restarted", info.title));
		                        tracer.flushTrace();
		                        info.comment = "Restarted";
		                        info.commentColor = goodColor;
		                        if (curIndex == infos.list.size() - 1) {
		                            allDone = true;
		                        } else {
		                            curIndex++;
		                            delayMillis = infos.list.get(curIndex).delayMillis;
		                            prevInstTimeMillis = System.currentTimeMillis();
		                        }
		                    }
		                    if (curIndex == infos.list.size() - 1 && infos.needProcessThis) {//this is always the last
		                        if (infos.stopAllInThisInstanceExceptThisArte()) {
		                            allDone = true;
		                            Arte::Arte.sleep(2000);//let ARTE pool on this instance stop to receive nice stats in final form (running ARTE = 1)
		                        }
		                    }
		                    if (allDone) {
		                        infos.rereadAll();
		                        return infos.prepareFinalResponse("");
		                    }
		                }
		            }

		            if (action == InstanceManagementAction:SWITCH_VERSION) {
		                if (!preloadSent) {
		                    preloadSent = infos.sendPreloadToAll();
		                    if (!preloadSent) {
		                        return infos.prepareFinalResponse("Failed to load the new version to all instances", Client.Resources::DialogType:Error);
		                    }
		                }
		                if (infos.checkAllPreloadedNextVer()) {
		                    if (rollingSwitch) {
		                        stopUsingCurVerSent = true;
		                    }
		                    if (!stopUsingCurVerSent) {
		                        stopUsingCurVerSent = infos.sendStopUsingCurVerToAll();
		                        if (!stopUsingCurVerSent) {
		                            return infos.prepareFinalResponse("Failed to initiate stop using current version on all instances", Client.Resources::DialogType:Error);
		                        }
		                    } else {
		                        if (rollingSwitch || infos.checkAllStoppedUsingCurVer()) {
		                            if (!startUsingNewVerSent) {
		                                if (rollingSwitch) {
		                                    infos.checkAllUsingNewVer();
		                                    if (System.currentTimeMillis() - prevInstTimeMillis > delayMillis) {
		                                        InstanceInfo info = infos.list.get(curIndex);
		                                        if (info.sendStartUsingNewVer()) {
		                                            curIndex++;
		                                            if (curIndex < infos.list.size()) {
		                                                delayMillis = infos.list.get(curIndex).delayMillis;
		                                                prevInstTimeMillis = System.currentTimeMillis();
		                                            } else {
		                                                startUsingNewVerSent = true;
		                                            }
		                                        }
		                                    }
		                                } else {
		                                    startUsingNewVerSent = true;
		                                    for (InstanceInfo info : infos.list) {
		                                        if (!info.startUsingNewVerSent && !info.sendStartUsingNewVer()) {
		                                            startUsingNewVerSent = false;
		                                        }
		                                    }
		                                }
		                            }
		                            if (startUsingNewVerSent && infos.checkAllUsingNewVer()) {
		                                return infos.prepareFinalResponse("All instances have been successfully switched to the new version" + (infos.firstStopUsingVerRqMillis != -1 ? (". " + String.format("Delay in processing: %s sec.", (System.currentTimeMillis() - infos.firstStopUsingVerRqMillis) / 1000)) : ""));
		                            }
		                        }
		                    }
		                }
		            }
		            infos.update();
		            Arte::Arte.sleep(1000);
		        }

		        return new FormHandlerNextDialogsRequest(new FormHandlerMessageBoxRequest(Client.Resources::DialogType:Error, null, null, null, "Operation was not completed in specified amount of time"), null);
		    } finally {
		        process.set(actionTitle, null, false);
		        process.close();
		    }
		} catch (Exception ex) {
		    throw new AppError(String.format("Error executing \"%s\" operation", actionTitle) + ": " + ex.getMessage(), ex);
		}
	}

	/*Radix::System::SysManagementUtils:execTask-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::SysManagementUtils:execTask")
	public static  org.radixware.kernel.server.types.FormHandler.NextDialogsRequest execTask (org.radixware.ads.System.common.SysManagementXsd.ManageInstancesTask task) {
		Arte::Arte.getInstance().getInstance().setUpgraderArte(Arte::Arte.getInstance());
		try {
		    return doExecTask(task);
		} finally {
		    Arte::Arte.getInstance().getInstance().setUpgraderArte(null);
		}

	}

	/*Radix::System::SysManagementUtils:getHtmlTextWithWrapWords-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::SysManagementUtils:getHtmlTextWithWrapWords")
	private static  Str getHtmlTextWithWrapWords (Str text) {
		final int lineLength = 80;
		final int minLineLength = 40;
		if (text.length() <= lineLength) {
		    return text;
		} else {
		    final java.lang.StringBuilder sb = new java.lang.StringBuilder();
		    int lineStart = 0;
		    int lineEnd = lineLength;
		    while (lineEnd < text.length()) {
		        for (int pos = lineEnd; pos >= lineStart + minLineLength; pos--) {
		            if (Char.isWhitespace(text.charAt(pos)) || pos == lineStart + minLineLength) {
		                if (pos == lineStart + minLineLength) {
		                    pos = lineEnd;
		                }
		                sb.append(text.substring(lineStart, pos));
		                sb.append("<br/>");
		                lineStart = pos;
		                lineEnd = lineStart + lineLength;
		                break;
		            }
		        }
		    }
		    sb.append(text.substring(lineStart));
		    return sb.toString();
		}
	}

	/*Radix::System::SysManagementUtils:escapeHtml-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::SysManagementUtils:escapeHtml")
	private static  Str escapeHtml (Str str) {
		//return org.apache.commons.lang.StringEscapeUtils.escapeHtml(str);
		//Html page charset always utf-8 in this case need to replace only: <, >, ', &, "
		if (str == null || !(str.contains("<") || str.contains(">") || str.contains("&") || str.contains("'") || str.contains("\""))) {
		    return str;
		}

		final java.lang.StringBuilder sb = new StringBuilder(str.length());
		char ch;
		for (int pos = 0; pos < str.length(); pos++) {
		    ch = str.charAt(pos);
		    if (ch == '<') {
		        sb.append("&lt;");
		    } else if (ch == '>') {
		        sb.append("&gt;");
		    } else if (ch == '&') {
		        sb.append("&amp;");
		    } else if (ch == '\'') {
		        sb.append("&apos;");
		    } else if (ch == '"') {
		        sb.append("&quot;");
		    } else {
		        sb.append(ch);
		    }
		}
		return sb.toString();
	}


}

/* Radix::System::SysManagementUtils - Server Meta*/

/*Radix::System::SysManagementUtils-Server Dynamic Class*/

package org.radixware.ads.System.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class SysManagementUtils_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("adcVXUX5KTNEBGYNALK4JPIC22X7I"),"SysManagementUtils",null,

						org.radixware.kernel.common.enums.EClassType.DYNAMIC,
						null,
						null,
						null,

						/*Radix::System::SysManagementUtils:Properties-Properties*/
						null,

						/*Radix::System::SysManagementUtils:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthTDEBS537GBG4BP37BVRQNFYONA"),"getInstancesInfo",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("ids",org.radixware.kernel.common.enums.EValType.ARR_INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprQ7K7OXTDK5GD5CBYMD5I3WYGCA"))
								},org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthPGEA7G32OFC2FDIN5SSNUEL334"),"doExecTask",true,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("task",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprNGNIJYV2JJA4NHBDFIOCLABDEE"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthAYBWXY4GJFG3XA2JVPTMZHOAV4"),"execTask",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("task",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprNGNIJYV2JJA4NHBDFIOCLABDEE"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthYWZPZQ3LHRDLLE3MROPDZEVGAI"),"getHtmlTextWithWrapWords",true,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("text",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprO2U6PFWNERFWVDXHNMDXUGS4KA"))
								},org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthPFB5IBUOOJGABLWF4RLZ6UQHRU"),"escapeHtml",true,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("str",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr74UG46X4RZEY5H4U6VPJ2T4H4Y"))
								},org.radixware.kernel.common.enums.EValType.STR)
						},
						null,
						null,null,null,false);
}

/* Radix::System::SysManagementUtils - Localizing Bundle */
package org.radixware.ads.System.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class SysManagementUtils - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Request for downloading new version sent");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Запрос на загрузку новой версии отправлен");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2AHALY4PWBFMBGB24UNSXFXYXI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Stopped using current version");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Прекращено использвание текущей версии");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2Y275BFOWFBJBIDCFH6FV5JT5Y"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"State of instances on command completion:");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Состояние инстанций на момент завершения команды:");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3NZUAFQZJ5DHTJ6T5GQCJW5FBE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Number of ARTEs");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Количество ARTE");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls47WO4OXBYNBRRMGKMU6JNQ2EDY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Reordering Required");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Необходимо переупорядочивание");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5SQSSZUIYVBPDICP2PVEUVKXFY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Switched to new version");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Переключена на новую версию");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5VVQ6AWXN5D4LHZBU5CTVQMKSI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Failed to load the new version to all instances");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Не удалось загрузить новую версию на всех инстанциях");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6VV745OUFBB2HE4M7KTZQFJK5I"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Instance \'%s\' stopped using current version");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Инстанция\'%s\' прекратила использовать текущую версию");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7AK3IRPFWRGSJBC3VQZZMASIBE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"New version loaded");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Новая версия загружена");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7TAJVEM6WFCVNFFA2WN7MSAGCA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Stop request sent, result code");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Запрос на остановку отправлен, код результата");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAMNPKWNGDBAAPAT7U6UXSMXOYM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Do you really want to perform the \"%s\" operation on the following instances?");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Вы хотите выполнить операцию \"%s\" над следующими инстанциями?");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAVNJHEN5A5AI7KST6QHWK4VP6Y"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Units Started");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Запущено модулей");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBHQQDZ2VFREQJNRWOW4KMT5BYU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"instance is processing this command");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"инстанция обрабатывает текущую команду");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCJ74GET67VCDZHWJDEN5QBNMKA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Unable to get information about version in repository: timeout");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Не удалось получить информацию о версии в репозитории: таймаут");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCLPL2V6IEBBFHL3GR74W3VUSTE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"ARTE\'s processing requests in current version: %s");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Кол-во ARTE, обрабатывающих запросы в текущей версии: %s");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCOXK4HZAZNEGXBUQMKGFCIUNKU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Restarted");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Перезапущена");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsD3SUXUGCQBAV7JAH2BWXSTBBIA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Restart request has been sent");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Запрос на перезапуск отправлен");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsD4RF5N64E5GJFN2FF6SKUKTO4I"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Confirmation");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Подтверждение");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsE5S6MFKV5BFT5GIJKUOKJ52RGI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Number of DB Sessions");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Количество сессий в БД");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEBAAW7VTKJDQLKOAIPGDD4FQTM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Comment");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Комментарий");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsG6J4BCJB2REG5HD3GLLBGAQGNQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Error sending the request to stop using the current application version");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ошибка при отправке запроса на прекращение использования текущей версии приложения");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGP2PGND5CBHGFI6HYUGD2FFGGE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Instance \'%s\' loaded new version");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Инстанция\'%s\' загрузила новую версию");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGQUSCGIBHJF6RPP2WTPE6DLBKI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Status");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Статус");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsH5IQMPT3PRA5TDJIX7YOQ24DNU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Instance \'%s\' has been restarted");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Инстанция \'%s\' перезапущена");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHGCC7OYYPFBCJFVN2Z7362DBWA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Error executing \"%s\" operation");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ошибка при выполнении операции \"%s\"");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHJXA72ZMIJFAFFT563EAZNO3ZQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Operation cancelled by user. Please note that initiated operations were not interrupted.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Операция отменена пользователем. Пожалуйста, учтите, что инициированные операции не были прерваны.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHOHDNFOUIRFODCLTUX2MOVZRPM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Failed to initiate stop using current version on all instances");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Не удалось инициировать прекращение использования текущей версии на всех инстанциях");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsIAAGTXW7CJGUFPE2NZX6SKQGHU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"ARTE count without new version: %s");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Кол-во ARTE, не загрузивших новую версию: %s");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsICKFLE3NRFFADPLAMRGF7NRAO4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"All instances have been successfully switched to the new version");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Все инстанции успешно переключились на использование новой версии");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJR5LB2MJYFGA3H2RTIN5LGKF7I"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Severity");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Серьёзность");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKC4WEQGBMZFLBCIGA76KZW47NA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Delay in processing: %s sec.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Задержка в обслуживании: %s с.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKRFZDJRDIRFRNFCL7TPAUIZPD4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Control service is reachable");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Сервис контроля доступен");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKTH3QXY2ZNGSZL3YA73NF5K37I"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Message");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Сообщение");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLJGNW25GPFFFTERNLUXUV6ZTYI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Operation has been aborted");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Операция прервана");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLKLL2KMEIBDM7KYUTFPWU3447I"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Instances list is empty");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Список инстанций пуст");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsM6PHH3B7DZFRFHKDEVCFTQWAIQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Stopped");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Остановлена");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMHTJUOMWWBA45M3OTXCIN67TNA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Please note that the following instances are unreachable, and operation will not be performed on them");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Пожалуйста, учтите, что следующие инстанции недоступны, и операция над ними не будет выполнена");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsML7JGL7HA5AZDOK6AHB3QPEAYA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Instance processing the current command is still running and should stop soon. Please continue monitoring at the OS level.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Только инстанция, обрабатывающая текущую команду, остается запущенной и скоро должна остановиться. Пожалуйста, продолжайте мониторинг на уровне ОС");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMZCHW2DH4NG6VOJREZLE3HRSNI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Confirm \"%s\" Operation");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Подтвердите операцию \"%s\"");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNHJTBXXGCBDQRN7ITWOK5PPKGA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Request for stop using current application version has been sent to instance \'%s\'");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Запрос на прекращение использования текущей версии приложения отправлен инстанции \'%s\'");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOJIKUDHFFFEG7C3VX7KTI2GL6Y"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"active");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"активных");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPMKSSTAPOJFCHH4MJWC3KVTY4I"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Operation was not completed in specified amount of time");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Операция не была завершена за отведенное время");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPNSCV6LJYZDJLJBWKUDO5ZFLC4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Restart request has been sent to the instance \'%s\'");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Запрос на перезапуск отправлен инстанции \'%s\'");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPTAN5GUY7FFRTNMGWRNKHFWYQM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Error");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ошибка");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRGEAL7XWDZHRZIVCKX2GEK726A"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Request for stop using current application version sent");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Запрос на прекращение использования текущей версии приложения отправлен");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRLRIRTSJUJC3PCIG24ZESKNWDA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Instance");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Инстанция");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTAYPQE56VZB3BKXJUPLSN7WVBU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Error sending the request to start using the new application version to instance \'%s\': %s");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ошибка при отправке запроса на начало использования новой версии приложения инстанции \'%s\': %s");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTJPPCYD5GNDBXERN4CUAW77BVE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Operation has been aborted");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Операция прервана");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsU22DWIVDLREC3IGJFRV7JCNCDY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Time");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Время");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsU7GPEGY5UBHD3ORIM2H424QORQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Error sending the request to download the new version");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ошибка после отправки запроса на загрузку новой версии");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUG4WPLLTIRG45LH22VZ3VW4S2A"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Operation log:");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Журнал операции:");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUOAWQNQAO5C3NNYELNK7CIO56Y"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Request for start using new application version has been sent to instance \'%s\'");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Запрос на начало использования новой версии приложения отправлен инстанции \'%s\'");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVNMA22ING5EPJHQQNIOAU5CSBQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Reordered instance had zero delay, now it\'s delay will be set to \'%s\' (maximum delay). Select \'No\' for abort.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Перемещенная в списке инстанция имела нулевую задержку, сейчас задержка будет выставлена в \'%s\' (максимальная задержка) . Выберите \'Нет\' для прерывания всей операции.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVPRSEIHY7FDVPFQEFYBKJXUKSU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Error after sending the request to stop");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ошибка после отправки запроса на остановку");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVQLZM6X7FNHT5IMB5DSWMI623Y"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Error sending the request to stop using the current application version to instance \'%s\': %s");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ошибка при отправке запроса на прекращение использования текущей версии приложения инстанции \'%s\': %s");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXCCMPKB4R5DDBNFB44PUOM2FOY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Instance \'%s\' started using new version");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Инстанция\'%s\' начала использовать новую версию");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXPIJ37IRJVD35HVRMFEIXBR6RM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Request for start using new application version sent");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Запрос на начало использования новой версии приложения отправлен");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXZGE37SXWVFCNNTHH5T2MGKOLU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Request for downloading new version has been sent to instance \'%s\'");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Запрос на загрузку новой версии отправлен инстанции \'%s\'");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYF37HBPLANEJXFGU6BE4WP3OGE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Instance processing this command (\'%s\') should be the last in restart order, do you agree to move it to the last position? If not, operation will be aborted.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Инстанция, обрабатывающая текущую команду (\'%s\'), должна быть перезапущена в последнюю очередь. Вы согласны переместить ее в конец списка? В противном случае операция будет прервана.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYFT6YLOVSVBOTCBVSKPL42L6QI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Error when sending request for downloading new version to instance \'%s\': %s");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ошибка при отправке запроса на загрузку новой версии инстанции \'%s\': %s");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYRIBJZ45RVFE7N5IKXVORN3HRQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Date");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Дата");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYTP54F5JNJHRLP3LOZOHX2DZJA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Error when sending request for start using new application version");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ошибка при отправке запроса на начало использования текущей версии приложения");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZIKF4DOB4ZDYFKAB7OZ7X5PXH4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"The following inactive instances were marked as stopped");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Следующие неактивные инстанции были помечены как остановленные");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZSBFYBNFDNG55GKEAOSYLAOOMU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Unable to determine kernel compatibility between repository revision %1 and revision %2 of Instance~%3: %4");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ошибка при определении совместимости ядра между ревизией репозитория %1 и ревизией %2 Инстанции~%3: %4");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZY25JKAXF5AQJA45UMKLU2YIEI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.EVENT_CODE,org.radixware.kernel.common.enums.EEventSeverity.WARNING,"App",java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(SysManagementUtils - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbadcVXUX5KTNEBGYNALK4JPIC22X7I"),"SysManagementUtils - Localizing Bundle",$$$items$$$);
}
