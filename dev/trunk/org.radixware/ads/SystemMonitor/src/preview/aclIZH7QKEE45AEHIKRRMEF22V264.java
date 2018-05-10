
/* Radix::SystemMonitor::Task.SysMon.MetricControl - Server Executable*/

/*Radix::SystemMonitor::Task.SysMon.MetricControl-Application Class*/

package org.radixware.ads.SystemMonitor.server;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::Task.SysMon.MetricControl")
public published class Task.SysMon.MetricControl  extends org.radixware.ads.Scheduling.server.Task.Atomic  implements org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {

	private static final long WAS_ABNORMAL_MASK = MetricControlAction:ReportError.longValue()
	        | MetricControlAction:ReportWarning.longValue()
	        | MetricControlAction:ReportWarningEscalation.longValue()
	        | MetricControlAction:ReportReturnToWarning.longValue();
	private static final long ERROR_LEVEL_MASK = MetricControlAction:ReportError.longValue() | MetricControlAction:ReportWarningEscalation.longValue();
	private static final long WARNING_LEVEL_MASK = MetricControlAction:ReportWarning.longValue() | MetricControlAction:ReportReturnToWarning.longValue();
	private SnmpUnitServiceXsd:NotifyRq notifyRq;

	void doExecute(final DateTime prevExecTime, final DateTime curExecTime) {
	    final MetricHistForControlCursor histCursor = MetricHistForControlCursor.open();
	    final MetricStateCursor stateCursor = MetricStateCursor.open();
	    MetricState metricState = null;
	    Int pendingHistStateId = null;
	    boolean historyFound = false;

	    SnmpUnitServiceWsdl:NotifyDocument notifyDoc = SnmpUnitServiceWsdl:NotifyDocument.Factory.newInstance();
	    notifyRq = notifyDoc.addNewNotify().addNewNotifyRq();

	    ControlState state = new ControlState();
	    try {
	        while (stateCursor.next() || stateCursor.isAfterLast()) {
	            if (metricState != null) {
	                updateMetricState(metricState, state);
	                if (stateCursor.isAfterLast()) {
	                    break;
	                }
	            }

	            metricState = MetricState.loadByPK(stateCursor.id, true);
	            if (metricState.metricType == null) {
	                continue;//metric was disabled
	            }
	//            System.out.println("Controlling metric #" + metricState.. + " " + metricState..);
	            historyFound = false;
	            state.type = getMetricType(metricState.metricType);
	            if (state.type == null) {
	                Arte::Trace.put(eventCode["Failed to determine the type of metric '%1'"], metricState.metricType.getKind().toString());
	            }
	            resetControlState(state, metricState);
	            if (pendingHistStateId == null) {
	                if (histCursor.next()) {
	                    pendingHistStateId = histCursor.stateId;
	                } else {
	                    pendingHistStateId = null;
	                }

	            }
	            state.traceArgs = new TraceArguments();
	            state.traceArgs.state = metricState;
	            state.traceArgs.totalDelaySec = null;
	            if (pendingHistStateId != metricState.id) {
	                if (state.type == MetricValueTypeEnum.STATISTIC) {
	                    continue;
	                }
	                if (!historyFound && metricState.endTime.Time <= curExecTime.Time) {
	                    state.saResult = calcSeverity(state, metricState.metricType);
	                    state.curSeverity = state.saResult.getSeverity();
	                    state.controlTime = curExecTime.Time;
	                    if (state.curSeverity == Arte::EventSeverity:Warning) {
	                        processWarning(metricState, state);
	                    } else if (state.curSeverity == Arte::EventSeverity:Error) {//error
	                        processError(metricState, state);
	                    }
	                }
	                if (state.newActionMask.get() > 0) {
	                    state.lastActionMask = state.newActionMask;
	                }
	//                state.lastControlTime = curExecTime.Time;
	                continue;
	            }
	            historyFound = true;
	            while (pendingHistStateId == metricState.id) {
	                state.begVal = histCursor.begVal;
	                state.endVal = histCursor.endVal;
	                state.minVal = histCursor.minVal;
	                state.maxVal = histCursor.maxVal;
	                state.avgVal = histCursor.avgVal;
	                state.saResult = calcSeverity(state, metricState.metricType);
	                state.curSeverity = state.saResult.getSeverity();
	                state.controlTime = histCursor.endTime.Time;
	//                if (metricState..longValue() == 85107) {
	//                    System.out.println(System.currentTimeMillis()
	//                            + " histCursor.begVal " + String.valueOf(histCursor.)
	//                            + " begVal " + String.valueOf(state.begVal)
	//                            + " endVal " + String.valueOf(state.endVal)
	//                            + " minVal " + String.valueOf(state.minVal)
	//                            + " maxVal " + String.valueOf(state.maxVal)
	//                            + " avgVal " + String.valueOf(state.avgVal)
	//                            + " curSeverity " + String.valueOf(state.curSeverity));
	//                }
	                if (state.type == MetricValueTypeEnum.STATISTIC) {
	                    state.valueSetupTime = histCursor.begTime.Time;
	                } else {
	                    state.valueSetupTime = histCursor.endTime.Time;
	                }
	                if (state.curSeverity == Arte::EventSeverity:Event) {
	                    processEvent(metricState, state);
	                } else if (state.curSeverity == Arte::EventSeverity:Warning) {
	                    processWarning(metricState, state);
	                } else {//error
	                    processError(metricState, state);
	                }
	                //end of current item processing
	                state.lastControlTime = state.controlTime;
	                state.lastProcessedHistId = histCursor.histId;
	                state.lastSeverity = state.curSeverity;
	                if (state.newActionMask.get() > 0) {
	                    state.lastActionMask.set(state.newActionMask.get());
	                }
	                state.newActionMask.clear();
	                if (histCursor.next()) {
	                    pendingHistStateId = histCursor.stateId;
	                } else {
	                    pendingHistStateId = null;
	                }
	            }
	        }
	    } finally {
	        try {
	            stateCursor.close();
	        } finally {
	            histCursor.close();
	        }
	    }

	    if (!notifyRq.MetricTypeList.isEmpty()) {
	        ActiveSnmpManagersCursor managersCur = ActiveSnmpManagersCursor.open();
	        try {
	            while (managersCur.next()) {
	                try {
	                    Arte::Arte.invokeInternalService(notifyDoc, SnmpUnitServiceWsdl:NotifyDocument.class, org.radixware.kernel.server.units.snmp.SnmpAgentSap.SERVICE_WSDL + "#" + managersCur.id, 0, 5, null);
	                } catch (Exception ex) {
	                    Arte::Trace.put(eventCode["Unable to send notification to SNMP Manager '%1': %2"], new ArrStr(managersCur.id + ") " + managersCur.title, Utils::ExceptionTextFormatter.throwableToString(ex)));
	                }
	            }
	        } finally {
	            managersCur.close();
	        }
	    }
	}

	private SnmpUnitServiceXsd:NotifyRq.MetricType getNotifyForType(final MetricType type) {
	    for (SnmpUnitServiceXsd:NotifyRq.MetricType xType : notifyRq.MetricTypeList) {
	        if (xType.Id == type.id) {
	            return xType;
	        }
	    }
	    SnmpUnitServiceXsd:NotifyRq.MetricType xType = notifyRq.addNewMetricType();
	    xType.Id = type.id;
	    xType.Kind = type.kind;
	    xType.Title = type.title;
	    return xType;
	}

	private void addNotify(MetricType type, Int stateId, Arte::EventSeverity oldSeverity, Arte::EventSeverity newSeverity, DateTime begTime, DateTime endTime, String valueAsStr, final long changeTimeMillis) {
	    addNotifyImpl(type, stateId, oldSeverity, newSeverity, begTime, endTime, valueAsStr, changeTimeMillis, false);
	}

	private void addNotifyEscalation(MetricType type, Int stateId, Arte::EventSeverity oldSeverity, Arte::EventSeverity newSeverity, DateTime begTime, DateTime endTime, String valueAsStr, final long changeTimeMillis) {
	    addNotifyImpl(type, stateId, oldSeverity, newSeverity, begTime, endTime, valueAsStr, changeTimeMillis, true);
	}


	private void addNotifyImpl(MetricType type, Int stateId, Arte::EventSeverity oldSeverity, Arte::EventSeverity newSeverity, DateTime begTime, DateTime endTime, String valueAsStr, final long changeTimeMillis, final boolean isEscalation) {
	    if (type.minSeverityForSnmpNtfy.Value.longValue() > newSeverity.Value.longValue()) {
	        return;
	    }
	    SnmpUnitServiceXsd:NotifyRq.MetricType.MetricState state = getNotifyForType(type).addNewMetricState();
	    state.Id = stateId;
	    state.BegTime = begTime;
	    state.EndTime = endTime;
	    if (oldSeverity != null) {
	        state.OldSeverity = oldSeverity.Value;
	    }
	    state.NewSeverity = newSeverity.Value;
	    state.ValueAsString = valueAsStr;
	    state.ChangeTime = new DateTime(changeTimeMillis);
	    state.IsEscalation = isEscalation;
	}

	private void processWarning(MetricState metricState, ControlState state) {
	    if (state.lastSeverity == Arte::EventSeverity:Event || (state.lastActionMask.contains(MetricControlAction:ReportError))) {
	        state.lastWarnTime = new DateTime(state.valueSetupTime);
	        state.lastWarnVal = state.saResult.getValue();
	    }
	    if (state.lastSeverity == Arte::EventSeverity:Error) {
	        if (state.type == MetricValueTypeEnum.EVENT) {
	            reportError(metricState, state, state.lastActionMask);
	        }
	        if (state.lastActionMask.contains(MetricControlAction:ReportError)) {
	            state.lastWarnTime = new DateTime(state.valueSetupTime);
	            state.lastWarnVal = state.saResult.getValue();
	            final String valStr = getValueAsString(state, metricState.metricType);
	            Arte::Trace.put(metricState.metricType.getReturnToWarningCode(), state.traceArgs.get(valStr, state.valueSetupTime));
	            addNotify(metricState.metricType, metricState.id, Arte::EventSeverity:Error, Arte::EventSeverity:Warning, metricState.begTime, metricState.endTime, valStr, state.valueSetupTime);
	            state.newActionMask.add(MetricControlAction:ReportReturnToWarning);
	        }
	    }
	    reportWarning(metricState, state, state.newActionMask);
	}

	private void processError(MetricState metricState, ControlState state) {
	    if (state.lastSeverity == Arte::EventSeverity:Event) {
	        state.lastWarnTime = new DateTime(state.valueSetupTime);
	        state.lastWarnVal = state.saResult.getValue();
	    }
	    if (state.lastSeverity != Arte::EventSeverity:Error) {
	        state.lastErrTime = new DateTime(state.valueSetupTime);
	        state.lastErrVal = state.saResult.getValue();
	    }
	    reportError(metricState, state, state.newActionMask);
	}

	private void processEvent(MetricState metricState, ControlState state) {
	    if (state.type == MetricValueTypeEnum.EVENT) {
	        if (state.lastSeverity == Arte::EventSeverity:Error) {
	            reportError(metricState, state, state.lastActionMask);
	        }
	        if (state.lastSeverity == Arte::EventSeverity:Warning) {
	            reportWarning(metricState, state, state.lastActionMask);
	        }
	    }
	    if ((state.lastActionMask.get() & WAS_ABNORMAL_MASK) != 0) {
	        final String valStr = getValueAsString(state, metricState.metricType);
	        Arte::Trace.put(metricState.metricType.getReturnToNormalCode(), state.traceArgs.get(valStr, state.valueSetupTime));
	        addNotify(metricState.metricType, metricState.id, (state.lastActionMask.get() & ERROR_LEVEL_MASK) > 0 ? Arte::EventSeverity:Error : Arte::EventSeverity:Warning, Arte::EventSeverity:Event, metricState.begTime, metricState.endTime, valStr, state.valueSetupTime);
	        state.newActionMask.add(MetricControlAction:ReportReturnToNormal);
	    }
	}

	private void reportWarning(MetricState metricState, ControlState state, ActionMask maskToUpdate) {
	    if (!state.lastActionMask.contains(MetricControlAction:ReportWarning)
	            && !state.lastActionMask.contains(MetricControlAction:ReportWarningEscalation)
	            && !state.lastActionMask.contains(MetricControlAction:ReportReturnToWarning)
	            && !state.newActionMask.contains(MetricControlAction:ReportReturnToWarning)
	            && state.controlTime - state.lastWarnTime.getTime() >= state.warnDelay) {
	        final String valStr = getValueAsString(state, metricState.metricType);
	        Arte::Trace.put(metricState.metricType.getWarningCode(), state.traceArgs.get(valStr, state.lastWarnTime.getTime()));
	        addNotify(metricState.metricType, metricState.id, Arte::EventSeverity:Event, Arte::EventSeverity:Warning, metricState.begTime, metricState.endTime, valStr, state.lastWarnTime.Time);
	        maskToUpdate.add(MetricControlAction:ReportWarning);
	    }
	    if (state.escDelay > 0 && !state.lastActionMask.contains(MetricControlAction:ReportWarningEscalation) && state.controlTime - state.lastWarnTime.getTime() >= state.warnDelay + state.escDelay) {
	        state.traceArgs.totalDelaySec = (state.warnDelay + state.escDelay) / 1000;
	        final long reportTimeMillis = state.lastWarnTime.getTime() + state.warnDelay + state.escDelay;
	        Arte::Trace.put(metricState.metricType.getEscalationCode(), state.traceArgs.get(getNumAsString(new Num(state.traceArgs.totalDelaySec)), reportTimeMillis));
	        addNotifyEscalation(metricState.metricType, metricState.id, Arte::EventSeverity:Warning, Arte::EventSeverity:Error, metricState.begTime, metricState.endTime, getValueAsString(state, metricState.metricType), reportTimeMillis);
	        maskToUpdate.add(MetricControlAction:ReportWarningEscalation);
	    }
	}

	private void reportError(MetricState metricState, ControlState state, ActionMask maskToUpdate) {
	    if (!state.lastActionMask.contains(MetricControlAction:ReportError) && state.controlTime - state.lastErrTime.Time >= state.errorDelay) {
	        final String valStr = getValueAsString(state, metricState.metricType);
	        Arte::Trace.put(metricState.metricType.getErrorCode(), state.traceArgs.get(valStr, state.lastErrTime.Time));
	        addNotify(metricState.metricType, metricState.id, (state.lastActionMask.get() & WARNING_LEVEL_MASK) > 0 ? Arte::EventSeverity:Warning : Arte::EventSeverity:Event, Arte::EventSeverity:Error, metricState.begTime, metricState.endTime, valStr, state.lastErrTime.Time);
	        maskToUpdate.add(MetricControlAction:ReportError);
	    }
	}

	private void resetControlState(final ControlState state, final MetricState metricState) {
	    if (metricState.controlData.lastControlSeverity != null) {
	        state.lastSeverity = Arte::EventSeverity.getForValue(metricState.controlData.lastControlSeverity);
	    } else {
	        state.lastSeverity = Arte::EventSeverity:Event;
	    }
	    state.lastErrTime = metricState.controlData.lastErrorTime;
	    state.lastWarnTime = metricState.controlData.lastWarnTime;
	    state.lastErrVal = metricState.controlData.lastErrorVal;
	    state.lastWarnVal = metricState.controlData.lastWarnVal;
	    state.warnDelay = metricState.metricType.warnDelay == null ? 0 : metricState.metricType.warnDelay.longValue() * 1000;
	    state.errorDelay = metricState.metricType.errorDelay == null ? 0 : metricState.metricType.errorDelay.longValue() * 1000;
	    state.escDelay = metricState.metricType.esclationDelay == null ? -1 : metricState.metricType.esclationDelay.longValue() * 1000;
	    state.lastControlTime = metricState.controlData.lastControlTime != null ? metricState.controlData.lastControlTime.Time : -1;
	    state.lastActionMask.set(metricState.controlData.lastControlActionMask != null ? metricState.controlData.lastControlActionMask.longValue() : 0);
	    state.lastProcessedHistId = metricState.controlData.lastProcessedHistId;
	    state.begVal = metricState.begVal;
	    state.endVal = metricState.endVal;
	    state.avgVal = metricState.avgVal;
	    state.minVal = metricState.minVal;
	    state.maxVal = metricState.maxVal;
	    state.newActionMask.clear();
	}

	private void updateMetricState(final MetricState metricState, final ControlState state) {
	    metricState.controlData.lastControlSeverity = state.lastSeverity != null ? state.lastSeverity.Value : Arte::EventSeverity:Event.Value;
	    metricState.controlData.lastWarnTime = state.lastWarnTime;
	    metricState.controlData.lastErrorTime = state.lastErrTime;
	    metricState.controlData.lastWarnVal = state.lastWarnVal;
	    metricState.controlData.lastErrorVal = state.lastErrVal;
	    metricState.controlData.lastControlTime = state.lastControlTime > 0 ? new DateTime(state.lastControlTime) : null;
	    metricState.controlData.lastControlActionMask = state.lastActionMask.get();
	    metricState.controlData.lastProcessedHistId = state.lastProcessedHistId;
	    metricState.update();
	    Arte::Arte.commit();
	}

	private static class ActionMask {

	    private long mask;

	    private void clear() {
	        this.mask = 0;
	    }

	    public long get() {
	        return mask;
	    }

	    public void add(MetricControlAction action) {
	        mask |= action.Value.longValue();
	    }

	    public boolean contains(MetricControlAction action) {
	        return (mask & action.longValue()) != 0;
	    }

	    public void set(long mask) {
	        this.mask = mask;
	    }
	}

	private static class TraceArguments {

	    public static final java.text.DateFormat DATE_FORMAT = new java.text.SimpleDateFormat("yyyy-MM-dd-HH:mm:ss:SSS");
	    private MetricState state;
	    private Int totalDelaySec;

	    public ArrStr get(Str value, long timeMillis) {
	        final ArrStr result = new ArrStr();
	        result.add(state.metricType.id + ") " + state.metricType.title);
	        result.add(state.metricType.kindTitle);
	        result.add(value);
	        result.add(state.metricType.id.toString());
	        result.add(state.metricType.kind.toString());
	        result.add(state.instanceId == null ? "<none>" : state.instanceId.toString());
	        result.add(state.unitId == null ? "<none>" : state.unitId.toString());
	        result.add(DATE_FORMAT.format(new java.util.Date(timeMillis)));
	        result.add(state.sensorTitle);
	        return result;
	    }
	}

	private static class ControlState {

	    Arte::EventSeverity curSeverity;
	    Arte::EventSeverity lastSeverity;
	    DateTime lastErrTime;
	    DateTime lastWarnTime;
	    Int lastProcessedHistId;
	    long controlTime = -1;
	    long lastControlTime = -1;
	    long valueSetupTime = -1;
	    long warnDelay = 0;
	    long errorDelay = 0;
	    long escDelay = -1;
	    Num avgVal;
	    Num minVal;
	    Num maxVal;
	    Num begVal;
	    Num endVal;
	    Num lastErrVal;
	    Num lastWarnVal;
	    ActionMask lastActionMask = new ActionMask();
	    ActionMask newActionMask = new ActionMask();
	    MetricValueTypeEnum type;
	    SeverityAnalyzeResult saResult;
	    TraceArguments traceArgs;
	}


	private SeverityAnalyzeResult calcSeverity(ControlState state, MetricType type) {

	    return MetricCommonUtils.calcSeverity(state.avgVal, state.minVal, state.maxVal, state.endVal, type.lowWarnVal, type.highWarnVal, type.lowErrorVal, type.highErrorVal, type.controlledValue);
	}

	private static String getValueAsString(ControlState state, final MetricType type) {
	    if (state == null) {
	        return "<null>";
	    }
	    if (type.getKind().isInDomain(idof[MetricValueTypeDomain:Statistic])) {
	        return "(min=" + getNumAsString(state.minVal) + ", max=" + getNumAsString(state.maxVal) + ", avg=" + getNumAsString(state.avgVal) + ")";
	    } else if (state.avgVal != null) {
	        return "(endVal=" + getNumAsString(state.endVal) + ", min=" + getNumAsString(state.minVal) + ", max=" + getNumAsString(state.maxVal) + ", avg=" + getNumAsString(state.avgVal) + ")";
	    } else {
	        return getNumAsString(state.endVal);
	    }
	}

	private static String getNumAsString(final Num value) {
	    String valueAsStr = value == null ? "<null>" : String.format("%.2f", value.doubleValue());
	    valueAsStr = valueAsStr.replace(",", ".");
	    if (valueAsStr.endsWith(".00")) {
	        valueAsStr = valueAsStr.substring(0, valueAsStr.length() - 3);
	    }
	    return valueAsStr;
	}
	/**Metainformation accessor method*/
	@SuppressWarnings("unused")
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return Task.SysMon.MetricControl_mi.rdxMeta;}

	/*Radix::SystemMonitor::Task.SysMon.MetricControl:Nested classes-Nested Classes*/

	/*Radix::SystemMonitor::Task.SysMon.MetricControl:Properties-Properties*/





























	/*Radix::SystemMonitor::Task.SysMon.MetricControl:Methods-Methods*/

	/*Radix::SystemMonitor::Task.SysMon.MetricControl:execute-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::Task.SysMon.MetricControl:execute")
	public published  void execute (java.sql.Timestamp prevExecTime, java.sql.Timestamp curExecTime) {
		doExecute(prevExecTime, curExecTime);
	}

	/*Radix::SystemMonitor::Task.SysMon.MetricControl:getMetricType-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::Task.SysMon.MetricControl:getMetricType")
	private final  org.radixware.kernel.server.monitoring.EMetricType getMetricType (org.radixware.ads.SystemMonitor.server.MetricType metricType) {
		MetricValueType t = metricType.getValueType();
		if (t == MetricValueType:Event || t == MetricValueType:Point) 
		    return MetricValueTypeEnum.EVENT;

		if(t == MetricValueType:Statistic) 
		    return MetricValueTypeEnum.STATISTIC;

		return null;
	}

	/*Radix::SystemMonitor::Task.SysMon.MetricControl:isSingletone-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::Task.SysMon.MetricControl:isSingletone")
	public published  boolean isSingletone () {
		return true;
	}


	@Override
	public org.radixware.kernel.server.types.FormHandler.NextDialogsRequest execCommand(org.radixware.kernel.common.types.Id cmdId, org.radixware.kernel.common.types.Id propId, org.apache.xmlbeans.XmlObject input, org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValsById, org.apache.xmlbeans.XmlObject output) throws org.radixware.kernel.common.exceptions.AppException,java.lang.InterruptedException{

			return super.execCommand(cmdId,propId,input,newPropValsById,output);
	}


}

/* Radix::SystemMonitor::Task.SysMon.MetricControl - Server Meta*/

/*Radix::SystemMonitor::Task.SysMon.MetricControl-Application Class*/

package org.radixware.ads.SystemMonitor.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Task.SysMon.MetricControl_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("aclIZH7QKEE45AEHIKRRMEF22V264"),"Task.SysMon.MetricControl",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBTMGAMCIURHPTPJUHT33ZJ6KGY"),

						org.radixware.kernel.common.enums.EClassType.APPLICATION,

						/*Radix::SystemMonitor::Task.SysMon.MetricControl:Presentations-Entity Object Presentations*/
						/*Class presentation attributes*/
						 new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef(/*Owner Class Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("aclIZH7QKEE45AEHIKRRMEF22V264"),
							/*Owner Class Name*/
							"Task.SysMon.MetricControl",
							/*Title Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBTMGAMCIURHPTPJUHT33ZJ6KGY"),
							/*Property presentations*/

							/*Radix::SystemMonitor::Task.SysMon.MetricControl:Properties-Properties*/
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
									/*Radix::SystemMonitor::Task.SysMon.MetricControl:Default-Dynamic Class Catalog*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eccVXWRKQLYJTOBDCIVAALOMT5GDM"),org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.ELinkMode.VIRTUAL,new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.Item[]{
										new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.ClassRef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aclIZH7QKEE45AEHIKRRMEF22V264"),org.radixware.kernel.common.types.Id.Factory.loadFrom("cctTU4RQWDDDZHDXKTRWIAPN7O6P4"),140.0,true,org.radixware.kernel.common.types.Id.Factory.loadFrom("aclIZH7QKEE45AEHIKRRMEF22V264"),null)}
									)
							},
							/*Restrictions*/
							null,null,0),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblWZB7K4HLJPOBDCIUAALOMT5GDM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclZDLN466RV5FE3BQIWSX56ZAETQ"),

						/*Radix::SystemMonitor::Task.SysMon.MetricControl:Properties-Properties*/
						null,

						/*Radix::SystemMonitor::Task.SysMon.MetricControl:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthN36NOGMSW5EGZIX2JAVCBWM2RU"),"execute",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("prevExecTime",org.radixware.kernel.common.enums.EValType.DATE_TIME,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprYV5N6MXCRRE2BIAIJSWP3CXDRM")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("curExecTime",org.radixware.kernel.common.enums.EValType.DATE_TIME,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprUB7QR4RQG5HPNLERHYHS5H6XVA"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthDMRMHQSWFBAVVEURXAVJA7MAYY"),"getMetricType",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("metricType",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprUWYQ7NC3HJHDZKR4QUAN3PWDPI"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthKIF62UYHLRDRPPGLIIMJRLXSUI"),"isSingletone",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE)
						},
						null,
						null,null,null,false);
}

/* Radix::SystemMonitor::Task.SysMon.MetricControl - Desktop Executable*/

/*Radix::SystemMonitor::Task.SysMon.MetricControl-Application Class*/

package org.radixware.ads.SystemMonitor.explorer;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::Task.SysMon.MetricControl")
public interface Task.SysMon.MetricControl   extends org.radixware.ads.Scheduling.explorer.Task.Atomic  {
























}

/* Radix::SystemMonitor::Task.SysMon.MetricControl - Desktop Meta*/

/*Radix::SystemMonitor::Task.SysMon.MetricControl-Application Class*/

package org.radixware.ads.SystemMonitor.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Task.SysMon.MetricControl_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::SystemMonitor::Task.SysMon.MetricControl:Presentations-Entity Object Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aclIZH7QKEE45AEHIKRRMEF22V264"),
			"Radix::SystemMonitor::Task.SysMon.MetricControl",
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aclZDLN466RV5FE3BQIWSX56ZAETQ"),
			null,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBTMGAMCIURHPTPJUHT33ZJ6KGY"),null,null,0,

			/*Radix::SystemMonitor::Task.SysMon.MetricControl:Properties-Properties*/
			null,
			null,
			null,
			null,
			null,
			null,
			true,true,false);
}

/* Radix::SystemMonitor::Task.SysMon.MetricControl - Web Meta*/

/*Radix::SystemMonitor::Task.SysMon.MetricControl-Application Class*/

package org.radixware.ads.SystemMonitor.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Task.SysMon.MetricControl_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::SystemMonitor::Task.SysMon.MetricControl:Presentations-Entity Object Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aclIZH7QKEE45AEHIKRRMEF22V264"),
			"Radix::SystemMonitor::Task.SysMon.MetricControl",
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aclZDLN466RV5FE3BQIWSX56ZAETQ"),
			null,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBTMGAMCIURHPTPJUHT33ZJ6KGY"),null,null,0,
			null,
			null,
			null,
			null,
			null,
			null,
			false,false,false);
}

/* Radix::SystemMonitor::Task.SysMon.MetricControl - Localizing Bundle */
package org.radixware.ads.SystemMonitor.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Task.SysMon.MetricControl - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Failed to determine the type of metric \'%1\'");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Не удалось определить тип метрики \'%1\'");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2LL3QTI3VNATLCNO5S4KH63OGY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.EVENT_CODE,org.radixware.kernel.common.enums.EEventSeverity.WARNING,"App.SystemMonitoring",null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Unable to send notification to SNMP Manager \'%1\': %2");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Не удалось отравить оповещение SNMP-менеджеру \'%1\': %2");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6SBZTV7FGVAV3GFJDUQYNNHJNY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.EVENT_CODE,org.radixware.kernel.common.enums.EEventSeverity.ERROR,"App.SystemMonitoring",null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"System Monitoring Metrics Control");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Контроль метрик системного мониторинга");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBTMGAMCIURHPTPJUHT33ZJ6KGY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(Task.SysMon.MetricControl - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbaclIZH7QKEE45AEHIKRRMEF22V264"),"Task.SysMon.MetricControl - Localizing Bundle",$$$items$$$);
}
