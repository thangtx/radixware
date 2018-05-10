
/* Radix::Scheduling::Task.UploadTraceToFiles - Server Executable*/

/*Radix::Scheduling::Task.UploadTraceToFiles-Application Class*/

package org.radixware.ads.Scheduling.server;

import org.radixware.kernel.server.trace.FileLog;
import java.io.File;
import java.util.*;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.UploadTraceToFiles")
public class Task.UploadTraceToFiles  extends org.radixware.ads.Scheduling.server.Task.Atomic  implements org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {


	/**Metainformation accessor method*/
	@SuppressWarnings("unused")
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return Task.UploadTraceToFiles_mi.rdxMeta;}

	/*Radix::Scheduling::Task.UploadTraceToFiles:Nested classes-Nested Classes*/

	/*Radix::Scheduling::Task.UploadTraceToFiles:Properties-Properties*/

	/*Radix::Scheduling::Task.UploadTraceToFiles:minSeverity-User Property*/








			















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.UploadTraceToFiles:minSeverity")
	public  org.radixware.kernel.common.enums.EEventSeverity getMinSeverity() {
		return minSeverity;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.UploadTraceToFiles:minSeverity")
	public   void setMinSeverity(org.radixware.kernel.common.enums.EEventSeverity val) {
		minSeverity = val;
	}

	/*Radix::Scheduling::Task.UploadTraceToFiles:contextTypes-User Property*/








			















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.UploadTraceToFiles:contextTypes")
	public  org.radixware.ads.Arte.common.EventContextType.Arr getContextTypes() {
		return contextTypes;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.UploadTraceToFiles:contextTypes")
	public   void setContextTypes(org.radixware.ads.Arte.common.EventContextType.Arr val) {
		contextTypes = val;
	}

	/*Radix::Scheduling::Task.UploadTraceToFiles:timeShift-User Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.UploadTraceToFiles:timeShift")
	public  Int getTimeShift() {
		return timeShift;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.UploadTraceToFiles:timeShift")
	public   void setTimeShift(Int val) {
		timeShift = val;
	}

	/*Radix::Scheduling::Task.UploadTraceToFiles:traceFileDir-User Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.UploadTraceToFiles:traceFileDir")
	public  Str getTraceFileDir() {
		return traceFileDir;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.UploadTraceToFiles:traceFileDir")
	public   void setTraceFileDir(Str val) {
		traceFileDir = val;
	}

	/*Radix::Scheduling::Task.UploadTraceToFiles:rotateTraceFilesDaily-User Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.UploadTraceToFiles:rotateTraceFilesDaily")
	public  Bool getRotateTraceFilesDaily() {
		return rotateTraceFilesDaily;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.UploadTraceToFiles:rotateTraceFilesDaily")
	public   void setRotateTraceFilesDaily(Bool val) {
		rotateTraceFilesDaily = val;
	}

	/*Radix::Scheduling::Task.UploadTraceToFiles:maxTraceFileSizeKb-User Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.UploadTraceToFiles:maxTraceFileSizeKb")
	public  Int getMaxTraceFileSizeKb() {
		return maxTraceFileSizeKb;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.UploadTraceToFiles:maxTraceFileSizeKb")
	public   void setMaxTraceFileSizeKb(Int val) {
		maxTraceFileSizeKb = val;
	}

	/*Radix::Scheduling::Task.UploadTraceToFiles:maxTraceFileCnt-User Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.UploadTraceToFiles:maxTraceFileCnt")
	public  Int getMaxTraceFileCnt() {
		return maxTraceFileCnt;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.UploadTraceToFiles:maxTraceFileCnt")
	public   void setMaxTraceFileCnt(Int val) {
		maxTraceFileCnt = val;
	}

	/*Radix::Scheduling::Task.UploadTraceToFiles:deleteDebugMess-User Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.UploadTraceToFiles:deleteDebugMess")
	public  Bool getDeleteDebugMess() {
		return deleteDebugMess;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.UploadTraceToFiles:deleteDebugMess")
	public   void setDeleteDebugMess(Bool val) {
		deleteDebugMess = val;
	}

	/*Radix::Scheduling::Task.UploadTraceToFiles:splitByContexts-User Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.UploadTraceToFiles:splitByContexts")
	public  Bool getSplitByContexts() {
		return splitByContexts;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.UploadTraceToFiles:splitByContexts")
	public   void setSplitByContexts(Bool val) {
		splitByContexts = val;
	}

	/*Radix::Scheduling::Task.UploadTraceToFiles:maxContextFiles-User Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.UploadTraceToFiles:maxContextFiles")
	public  Int getMaxContextFiles() {
		return maxContextFiles;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.UploadTraceToFiles:maxContextFiles")
	public   void setMaxContextFiles(Int val) {
		maxContextFiles = val;
	}

	/*Radix::Scheduling::Task.UploadTraceToFiles:eventUploadConditionFunc-User Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.UploadTraceToFiles:eventUploadConditionFunc")
	public  org.radixware.ads.Scheduling.server.UserFunc.EventUploadCondition getEventUploadConditionFunc() {
		return eventUploadConditionFunc;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.UploadTraceToFiles:eventUploadConditionFunc")
	public   void setEventUploadConditionFunc(org.radixware.ads.Scheduling.server.UserFunc.EventUploadCondition val) {
		eventUploadConditionFunc = val;
	}































































































	/*Radix::Scheduling::Task.UploadTraceToFiles:Methods-Methods*/

	/*Radix::Scheduling::Task.UploadTraceToFiles:execute-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.UploadTraceToFiles:execute")
	public published  void execute (java.sql.Timestamp prevExecTime, java.sql.Timestamp curExecTime) {
		if (prevExecTime == null || curExecTime == null) // do nothing on first run
		    return;

		final Map<Str, FileLog> name2File = new HashMap<Str, FileLog>();

		System::EventLogCursor c = System::EventLogCursor.open(
		        minSeverity,
		        null,
		        false,
		        timeShift == null ? prevExecTime : new DateTime(prevExecTime.Time - timeShift.longValue() * 1000),
		        timeShift == null ? curExecTime : new DateTime(curExecTime.Time - timeShift.longValue() * 1000));

		final boolean doSplitByContexts = (splitByContexts == null || splitByContexts.booleanValue());

		final String singleFileName = "EventLog";
		final FileLog singleFile = doSplitByContexts == true ? null : createFileLog("", singleFileName);
		final long maxFiles = maxContextFiles == null ? 1000 : maxContextFiles.longValue();
		boolean maxFilesWarnReported = false;

		class UploadConditionFuncCaller {
		    Bool lastResult = null;
		    int countTrue = 0;
		    int countFalse = 0;
		    
		    boolean call(Task task, System::EventLog event) {
		        if (lastResult != null) {
		            return lastResult.booleanValue();
		        } else if (eventUploadConditionFunc == null) {
		            return true;
		        }
		        lastResult = eventUploadConditionFunc.check(task, event);
		        if (lastResult == true) {
		            countTrue++;
		        } else {
		            countFalse++;
		        }
		        return lastResult.booleanValue();
		    }
		};

		UploadConditionFuncCaller uploadConditionFunc = new UploadConditionFuncCaller();

		if (eventUploadConditionFunc != null) {
		    Arte::Trace.debug(eventUploadConditionFunc.getInvokeDescr() + " will be used for event upload condition check during task execution", Arte::EventSource:Task);
		}

		Meta::TraceItem item;
		try {
		    while (c.next()) {
		        final Int sectionId = Arte::Arte.enterCachingSession();
		        try {
		            final System::EventLog event = c.event;
		            uploadConditionFunc.lastResult = null;

		            boolean written = false;
		            item = null;
		                
		            if (doSplitByContexts) {

		                System::EventLogContextCursor cur = System::EventLogContextCursor.open(c.raiseTime, c.id);

		                try {
		                    while (cur.next()) {
		                        if (contextTypes != null && !contextTypes.contains(cur.type))
		                            continue;
		                        
		                        if (!uploadConditionFunc.call(this, event))
		                            continue;

		                        final Str name = cur.type.Value + "_#" + cur.id;
		                        FileLog file = name2File.get(name);
		                        if (file == null) {
		                            if (name2File.size() > maxFiles) {
		                                if (!maxFilesWarnReported) {
		                                    Arte::Trace.put(eventCode["Maximum number of different contexts for trace upload (%1) is reached, no new files will be generated"], String.valueOf(maxFiles));
		                                    maxFilesWarnReported = true;
		                                }
		                                continue;
		                            }
		                            file = createFileLog(name, name);
		                            name2File.put(name, file);
		                        }

		                        // write file data
		                        if (item == null) {
		                            item = createTraceItem(event);
		                        }
		                        file.log(item);
		                        written = true;
		                    }
		                } finally {
		                    cur.close();
		                }
		            } else if (uploadConditionFunc.call(this, event)) {
		                singleFile.log(createTraceItem(event));
		                written = true;
		            }
		            if (deleteDebugMess.booleanValue() && event.severity == Arte::EventSeverity:Debug && written) {
		                event.delete();
		            }
		        } finally {
		            Arte::Arte.leaveCachingSession(sectionId);
		        }
		    }
		} finally {
		    if (uploadConditionFunc.countTrue != 0 || uploadConditionFunc.countFalse != 0) {
		        Arte::Trace.debug(eventUploadConditionFunc.getInvokeDescr() + " invoke results: count(true) = " + uploadConditionFunc.countTrue + ", count(false) = " + uploadConditionFunc.countFalse, Arte::EventSource:Task);
		    }

		    c.close();
		    for (FileLog file : name2File.values()) {
		        file.close();
		    }
		    if (singleFile != null) {
		        singleFile.close();
		    }
		}

	}

	/*Radix::Scheduling::Task.UploadTraceToFiles:createTraceItem-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.UploadTraceToFiles:createTraceItem")
	private final  org.radixware.kernel.common.trace.TraceItem createTraceItem (org.radixware.ads.System.server.EventLog event) {
		Meta::TraceItem item = new Meta::TraceItem(
		        Arte::Arte.getInstance().MlsProcessor,
		        event.severity,
		        event.code,
		        (event.words == null ? null : event.words.subList(0, event.words.size())),
		        (event.component == null ? "" : event.component.Value),
		        false,
		        event.raiseTime.Time);
		return item;
	}

	/*Radix::Scheduling::Task.UploadTraceToFiles:createFileLog-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.UploadTraceToFiles:createFileLog")
	private final  org.radixware.kernel.server.trace.FileLog createFileLog (Str subDir, Str name) {
		return new FileLog(
		        new File(new File(traceFileDir), subDir),
		        name,
		        maxTraceFileSizeKb.intValue() * 1024,
		        maxTraceFileCnt.intValue(),
		        rotateTraceFilesDaily.booleanValue(),
		        new java.text.SimpleDateFormat("yyyy-MM-dd"),
		        FileLog.RotationBase.RECORD_TIME
		);
	}


	@Override
	public org.radixware.kernel.server.types.FormHandler.NextDialogsRequest execCommand(org.radixware.kernel.common.types.Id cmdId, org.radixware.kernel.common.types.Id propId, org.apache.xmlbeans.XmlObject input, org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValsById, org.apache.xmlbeans.XmlObject output) throws org.radixware.kernel.common.exceptions.AppException,java.lang.InterruptedException{

			return super.execCommand(cmdId,propId,input,newPropValsById,output);
	}


}

/* Radix::Scheduling::Task.UploadTraceToFiles - Server Meta*/

/*Radix::Scheduling::Task.UploadTraceToFiles-Application Class*/

package org.radixware.ads.Scheduling.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Task.UploadTraceToFiles_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("aclQR333WQPYFHBPOCPLTZWA7LE7U"),"Task.UploadTraceToFiles",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRWJ57G54NNDDBATZHI2DWVVD3Y"),

						org.radixware.kernel.common.enums.EClassType.APPLICATION,

						/*Radix::Scheduling::Task.UploadTraceToFiles:Presentations-Entity Object Presentations*/
						/*Class presentation attributes*/
						 new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef(/*Owner Class Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("aclQR333WQPYFHBPOCPLTZWA7LE7U"),
							/*Owner Class Name*/
							"Task.UploadTraceToFiles",
							/*Title Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRWJ57G54NNDDBATZHI2DWVVD3Y"),
							/*Property presentations*/

							/*Radix::Scheduling::Task.UploadTraceToFiles:Properties-Properties*/
							new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef[]{

									/*Radix::Scheduling::Task.UploadTraceToFiles:minSeverity:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruV67ZMIX3MVDSTODNNVZLTDTJHA"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Scheduling::Task.UploadTraceToFiles:contextTypes:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruJZ3MBG7LBBBEDL47EGZVQLNX4M"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Scheduling::Task.UploadTraceToFiles:timeShift:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruQGUAUSNCEZDB5HWV6EDZPULRX4"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Scheduling::Task.UploadTraceToFiles:traceFileDir:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruISFZYQNSZRGEBO37WJE34YLIO4"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Scheduling::Task.UploadTraceToFiles:rotateTraceFilesDaily:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruR4VBL67HCZEHREPZNGKKQIHUN4"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Scheduling::Task.UploadTraceToFiles:maxTraceFileSizeKb:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pru3TS5AZ7CEBAXFBIOMIX3OVE4PY"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Scheduling::Task.UploadTraceToFiles:maxTraceFileCnt:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruRV2BAKTB65AGDPVSAJGU574ZEM"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Scheduling::Task.UploadTraceToFiles:deleteDebugMess:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruAES7SOWWDBBURJUP3ITYWUALZI"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Scheduling::Task.UploadTraceToFiles:splitByContexts:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruXA2FRHNJHZEQDKBEVVX7X6HR3E"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Scheduling::Task.UploadTraceToFiles:maxContextFiles:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruIIQPMZKCF5BETMWH6DIDS2KBGE"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Scheduling::Task.UploadTraceToFiles:eventUploadConditionFunc:PropertyPresentation-Object Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadParentTitlePropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruOFVZJJFHHFAD5J5GZZRPONWIAA"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,null,null,null,null,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr7IKMB24XZHOBDCMTAALOMT5GDM")},null,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr7IKMB24XZHOBDCMTAALOMT5GDM")},java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.TITLE,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT))
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
									/*Radix::Scheduling::Task.UploadTraceToFiles:Edit-Editor Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprVCSWHQ6PDNA23KMOFEQ3RMC6ME"),
									"Edit",
									org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprQYXLRZJMHRBHPMBYLKEW24CA3I"),
									40112,
									null,

									/*Radix::Scheduling::Task.UploadTraceToFiles:Edit:Children-Explorer Items*/
										null
									,
									null,
									(org.radixware.kernel.server.meta.presentations.RadPropertyPresentationAttributes[]) null,
									org.radixware.kernel.common.enums.EEditorPresentationRightsInheritanceMode.NONE,
									null),

									new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef(
									/*Radix::Scheduling::Task.UploadTraceToFiles:Create-Editor Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprQYXLRZJMHRBHPMBYLKEW24CA3I"),
									"Create",
									org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprXGQ6C3VMQPOBDCKCAALOMT5GDM"),
									36016,
									null,

									/*Radix::Scheduling::Task.UploadTraceToFiles:Create:Children-Explorer Items*/
										null
									,
									null,
									(org.radixware.kernel.server.meta.presentations.RadPropertyPresentationAttributes[]) null,
									org.radixware.kernel.common.enums.EEditorPresentationRightsInheritanceMode.NONE,
									null)
							},
							/*Selector presentations*/
							null,
							/*Default selector presentation Id*/
							null,
							/*Presentation Map*/
							org.radixware.kernel.common.utils.Maps.fromArrays(new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprAIWBLDTSJTOBDCIVAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("eprXGQ6C3VMQPOBDCKCAALOMT5GDM")},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprVCSWHQ6PDNA23KMOFEQ3RMC6ME"),org.radixware.kernel.common.types.Id.Factory.loadFrom("eprQYXLRZJMHRBHPMBYLKEW24CA3I")}),
							/*Object title format*/
							null,
							/*Class catalogs*/
							new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog[]{

									new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog(
									/*Radix::Scheduling::Task.UploadTraceToFiles:Default-Dynamic Class Catalog*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eccVXWRKQLYJTOBDCIVAALOMT5GDM"),org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.ELinkMode.VIRTUAL,new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.Item[]{
										new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.ClassRef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aclQR333WQPYFHBPOCPLTZWA7LE7U"),org.radixware.kernel.common.types.Id.Factory.loadFrom("cctTU4RQWDDDZHDXKTRWIAPN7O6P4"),120.0,true,org.radixware.kernel.common.types.Id.Factory.loadFrom("aclQR333WQPYFHBPOCPLTZWA7LE7U"),null)}
									)
							},
							/*Restrictions*/
							null,null,0),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblWZB7K4HLJPOBDCIUAALOMT5GDM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclZDLN466RV5FE3BQIWSX56ZAETQ"),

						/*Radix::Scheduling::Task.UploadTraceToFiles:Properties-Properties*/
						new org.radixware.kernel.server.meta.clazzes.RadPropDef[]{

								/*Radix::Scheduling::Task.UploadTraceToFiles:minSeverity-User Property*/

								new org.radixware.kernel.server.meta.clazzes.RadUserPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruV67ZMIX3MVDSTODNNVZLTDTJHA"),"minSeverity",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsSGJRSH4NBRGURPNK44HFLZI624"),org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("acsFRO5RTQOG7NRDJH2ACQMTAIZT4"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("1")),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblWZB7K4HLJPOBDCIUAALOMT5GDM"),false,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::Task.UploadTraceToFiles:contextTypes-User Property*/

								new org.radixware.kernel.server.meta.clazzes.RadUserPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruJZ3MBG7LBBBEDL47EGZVQLNX4M"),"contextTypes",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWCMEQXOJXNC2VMFWON4PRNBA2A"),org.radixware.kernel.common.enums.EValType.ARR_STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("acsPVLIUELSKTNRDAQSABIFNQAAAE"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("tblWZB7K4HLJPOBDCIUAALOMT5GDM"),false,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::Task.UploadTraceToFiles:timeShift-User Property*/

								new org.radixware.kernel.server.meta.clazzes.RadUserPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruQGUAUSNCEZDB5HWV6EDZPULRX4"),"timeShift",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsL4KCQZPMAJEHJK2GY2W3U3BUT4"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DEFINE_ALWAYS,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("0")),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblWZB7K4HLJPOBDCIUAALOMT5GDM"),false,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::Task.UploadTraceToFiles:traceFileDir-User Property*/

								new org.radixware.kernel.server.meta.clazzes.RadUserPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruISFZYQNSZRGEBO37WJE34YLIO4"),"traceFileDir",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFZMODKH2XRCLBJIYYED2YUSDAI"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("./logs")),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblWZB7K4HLJPOBDCIUAALOMT5GDM"),false,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::Task.UploadTraceToFiles:rotateTraceFilesDaily-User Property*/

								new org.radixware.kernel.server.meta.clazzes.RadUserPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruR4VBL67HCZEHREPZNGKKQIHUN4"),"rotateTraceFilesDaily",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOBB63CVD4FHAZMPWEJSBNPPAEQ"),org.radixware.kernel.common.enums.EValType.BOOL,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("0")),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblWZB7K4HLJPOBDCIUAALOMT5GDM"),false,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::Task.UploadTraceToFiles:maxTraceFileSizeKb-User Property*/

								new org.radixware.kernel.server.meta.clazzes.RadUserPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pru3TS5AZ7CEBAXFBIOMIX3OVE4PY"),"maxTraceFileSizeKb",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsV2VJ2MNX6ZBVHBRP7K6XA4SNNY"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("1024")),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblWZB7K4HLJPOBDCIUAALOMT5GDM"),false,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::Task.UploadTraceToFiles:maxTraceFileCnt-User Property*/

								new org.radixware.kernel.server.meta.clazzes.RadUserPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruRV2BAKTB65AGDPVSAJGU574ZEM"),"maxTraceFileCnt",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsODFGNL2JXFAAZLUVNP5V22C5QU"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("10")),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblWZB7K4HLJPOBDCIUAALOMT5GDM"),false,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::Task.UploadTraceToFiles:deleteDebugMess-User Property*/

								new org.radixware.kernel.server.meta.clazzes.RadUserPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruAES7SOWWDBBURJUP3ITYWUALZI"),"deleteDebugMess",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMTKZZ6SIUFDUNBP6K5W4QE2ILU"),org.radixware.kernel.common.enums.EValType.BOOL,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("0")),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblWZB7K4HLJPOBDCIUAALOMT5GDM"),false,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::Task.UploadTraceToFiles:splitByContexts-User Property*/

								new org.radixware.kernel.server.meta.clazzes.RadUserPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruXA2FRHNJHZEQDKBEVVX7X6HR3E"),"splitByContexts",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMDEOSMSTHNBK7LNICEA4QIQNJY"),org.radixware.kernel.common.enums.EValType.BOOL,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("tblWZB7K4HLJPOBDCIUAALOMT5GDM"),false,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::Task.UploadTraceToFiles:maxContextFiles-User Property*/

								new org.radixware.kernel.server.meta.clazzes.RadUserPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruIIQPMZKCF5BETMWH6DIDS2KBGE"),"maxContextFiles",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNURF5ER2YZFBNCMCTPBEVFDNZ4"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("tblWZB7K4HLJPOBDCIUAALOMT5GDM"),false,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::Task.UploadTraceToFiles:eventUploadConditionFunc-User Property*/

								new org.radixware.kernel.server.meta.clazzes.RadUserRefPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruOFVZJJFHHFAD5J5GZZRPONWIAA"),"eventUploadConditionFunc",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEYV2CSEBAVBQDJ2Z4NFKEIDVYE"),org.radixware.kernel.common.enums.EValType.OBJECT,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("tblWZB7K4HLJPOBDCIUAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aclUA6HHOL2SBCINE6MOG2GHKBYSI"),false,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE)
						},

						/*Radix::Scheduling::Task.UploadTraceToFiles:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthN36NOGMSW5EGZIX2JAVCBWM2RU"),"execute",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("prevExecTime",org.radixware.kernel.common.enums.EValType.DATE_TIME,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprQU36BLBYXVBQLOD6RTETHWBXPM")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("curExecTime",org.radixware.kernel.common.enums.EValType.DATE_TIME,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprQDY7OMFF3FDWDDGGDEW4LHAHBE"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth6XYPDVSKJJBRBIEVJBEQ5CGGCY"),"createTraceItem",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("event",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprKU432DZFWZHEVKA2TK3ROBULHU"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthJDLLFVHRIFDQHPB6P3GMFQJWFI"),"createFileLog",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("subDir",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprLCAZBIE23BEWNFBISYTWAQVOT4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("name",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprCCYOIVYUT5BZFIXT3NN2M44HVI"))
								},org.radixware.kernel.common.enums.EValType.JAVA_CLASS)
						},
						null,
						null,null,null,false);
}

/* Radix::Scheduling::Task.UploadTraceToFiles - Desktop Executable*/

/*Radix::Scheduling::Task.UploadTraceToFiles-Application Class*/

package org.radixware.ads.Scheduling.explorer;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.UploadTraceToFiles")
public interface Task.UploadTraceToFiles   extends org.radixware.ads.Scheduling.explorer.Task.Atomic  {















































































































































































	/*Radix::Scheduling::Task.UploadTraceToFiles:maxTraceFileSizeKb:maxTraceFileSizeKb-Presentation Property*/


	public class MaxTraceFileSizeKb extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public MaxTraceFileSizeKb(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.UploadTraceToFiles:maxTraceFileSizeKb:maxTraceFileSizeKb")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.UploadTraceToFiles:maxTraceFileSizeKb:maxTraceFileSizeKb")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public MaxTraceFileSizeKb getMaxTraceFileSizeKb();
	/*Radix::Scheduling::Task.UploadTraceToFiles:deleteDebugMess:deleteDebugMess-Presentation Property*/


	public class DeleteDebugMess extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public DeleteDebugMess(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Bool dummy = ((Bool)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.UploadTraceToFiles:deleteDebugMess:deleteDebugMess")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.UploadTraceToFiles:deleteDebugMess:deleteDebugMess")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public DeleteDebugMess getDeleteDebugMess();
	/*Radix::Scheduling::Task.UploadTraceToFiles:maxContextFiles:maxContextFiles-Presentation Property*/


	public class MaxContextFiles extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public MaxContextFiles(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.UploadTraceToFiles:maxContextFiles:maxContextFiles")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.UploadTraceToFiles:maxContextFiles:maxContextFiles")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public MaxContextFiles getMaxContextFiles();
	/*Radix::Scheduling::Task.UploadTraceToFiles:traceFileDir:traceFileDir-Presentation Property*/


	public class TraceFileDir extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public TraceFileDir(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.UploadTraceToFiles:traceFileDir:traceFileDir")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.UploadTraceToFiles:traceFileDir:traceFileDir")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public TraceFileDir getTraceFileDir();
	/*Radix::Scheduling::Task.UploadTraceToFiles:contextTypes:contextTypes-Presentation Property*/


	public class ContextTypes extends org.radixware.kernel.common.client.models.items.properties.PropertyArrStr{
		public ContextTypes(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public void setServerValue(org.radixware.kernel.common.client.models.items.properties.PropertyValue val){
			Object x = val.getValue();
			org.radixware.ads.Arte.common.EventContextType.Arr dummy = x == null ? null : (x instanceof org.radixware.ads.Arte.common.EventContextType.Arr ? (org.radixware.ads.Arte.common.EventContextType.Arr)x : new org.radixware.ads.Arte.common.EventContextType.Arr((org.radixware.kernel.common.types.ArrStr)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.ARR_STR)));
			val.refineValue(dummy);
			super.setServerValue(val);
		}
		@Override
		public Class<org.radixware.ads.Arte.common.EventContextType.Arr> getValClass(){
			return org.radixware.ads.Arte.common.EventContextType.Arr.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.ads.Arte.common.EventContextType.Arr dummy = x == null ? null : (x instanceof org.radixware.ads.Arte.common.EventContextType.Arr ? (org.radixware.ads.Arte.common.EventContextType.Arr)x : new org.radixware.ads.Arte.common.EventContextType.Arr((org.radixware.kernel.common.types.ArrStr)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.ARR_STR)));
			setValue(dummy);
		}





				














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.UploadTraceToFiles:contextTypes:contextTypes")
		public  org.radixware.ads.Arte.common.EventContextType.Arr getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.UploadTraceToFiles:contextTypes:contextTypes")
		public   void setValue(org.radixware.ads.Arte.common.EventContextType.Arr val) {
			Value = val;
		}
	}
	public ContextTypes getContextTypes();
	/*Radix::Scheduling::Task.UploadTraceToFiles:eventUploadConditionFunc:eventUploadConditionFunc-Presentation Property*/


	public class EventUploadConditionFunc extends org.radixware.kernel.common.client.models.items.properties.PropertyObject{
		public EventUploadConditionFunc(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadParentRefPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.UploadTraceToFiles:eventUploadConditionFunc:eventUploadConditionFunc")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.UploadTraceToFiles:eventUploadConditionFunc:eventUploadConditionFunc")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public EventUploadConditionFunc getEventUploadConditionFunc();
	/*Radix::Scheduling::Task.UploadTraceToFiles:timeShift:timeShift-Presentation Property*/


	public class TimeShift extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public TimeShift(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.UploadTraceToFiles:timeShift:timeShift")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.UploadTraceToFiles:timeShift:timeShift")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public TimeShift getTimeShift();
	/*Radix::Scheduling::Task.UploadTraceToFiles:rotateTraceFilesDaily:rotateTraceFilesDaily-Presentation Property*/


	public class RotateTraceFilesDaily extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public RotateTraceFilesDaily(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Bool dummy = ((Bool)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.UploadTraceToFiles:rotateTraceFilesDaily:rotateTraceFilesDaily")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.UploadTraceToFiles:rotateTraceFilesDaily:rotateTraceFilesDaily")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public RotateTraceFilesDaily getRotateTraceFilesDaily();
	/*Radix::Scheduling::Task.UploadTraceToFiles:maxTraceFileCnt:maxTraceFileCnt-Presentation Property*/


	public class MaxTraceFileCnt extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public MaxTraceFileCnt(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.UploadTraceToFiles:maxTraceFileCnt:maxTraceFileCnt")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.UploadTraceToFiles:maxTraceFileCnt:maxTraceFileCnt")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public MaxTraceFileCnt getMaxTraceFileCnt();
	/*Radix::Scheduling::Task.UploadTraceToFiles:minSeverity:minSeverity-Presentation Property*/


	public class MinSeverity extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public MinSeverity(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public void setServerValue(org.radixware.kernel.common.client.models.items.properties.PropertyValue val){
			Object x = val.getValue();
			org.radixware.kernel.common.enums.EEventSeverity dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EEventSeverity ? (org.radixware.kernel.common.enums.EEventSeverity)x : org.radixware.kernel.common.enums.EEventSeverity.getForValue(((Int)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.INT)).longValue()));
			val.refineValue(dummy);
			super.setServerValue(val);
		}
		@Override
		public Class<org.radixware.kernel.common.enums.EEventSeverity> getValClass(){
			return org.radixware.kernel.common.enums.EEventSeverity.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.enums.EEventSeverity dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EEventSeverity ? (org.radixware.kernel.common.enums.EEventSeverity)x : org.radixware.kernel.common.enums.EEventSeverity.getForValue(((Int)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.INT)).longValue()));
			setValue(dummy);
		}





				














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.UploadTraceToFiles:minSeverity:minSeverity")
		public  org.radixware.kernel.common.enums.EEventSeverity getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.UploadTraceToFiles:minSeverity:minSeverity")
		public   void setValue(org.radixware.kernel.common.enums.EEventSeverity val) {
			Value = val;
		}
	}
	public MinSeverity getMinSeverity();
	/*Radix::Scheduling::Task.UploadTraceToFiles:splitByContexts:splitByContexts-Presentation Property*/


	public class SplitByContexts extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public SplitByContexts(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Bool dummy = ((Bool)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.UploadTraceToFiles:splitByContexts:splitByContexts")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.UploadTraceToFiles:splitByContexts:splitByContexts")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public SplitByContexts getSplitByContexts();


}

/* Radix::Scheduling::Task.UploadTraceToFiles - Desktop Meta*/

/*Radix::Scheduling::Task.UploadTraceToFiles-Application Class*/

package org.radixware.ads.Scheduling.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Task.UploadTraceToFiles_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::Scheduling::Task.UploadTraceToFiles:Presentations-Entity Object Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aclQR333WQPYFHBPOCPLTZWA7LE7U"),
			"Radix::Scheduling::Task.UploadTraceToFiles",
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aclZDLN466RV5FE3BQIWSX56ZAETQ"),
			null,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRWJ57G54NNDDBATZHI2DWVVD3Y"),null,null,0,

			/*Radix::Scheduling::Task.UploadTraceToFiles:Properties-Properties*/
			new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

					/*Radix::Scheduling::Task.UploadTraceToFiles:minSeverity:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruV67ZMIX3MVDSTODNNVZLTDTJHA"),
						"minSeverity",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsSGJRSH4NBRGURPNK44HFLZI624"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclQR333WQPYFHBPOCPLTZWA7LE7U"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.USER,
						4,
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acsFRO5RTQOG7NRDJH2ACQMTAIZT4"),
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("1"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Scheduling::Task.UploadTraceToFiles:minSeverity:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acsFRO5RTQOG7NRDJH2ACQMTAIZT4"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.NONE,null),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6OJYSEH7YNHKXM7Z5OEF4B4RU4"),
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Scheduling::Task.UploadTraceToFiles:contextTypes:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruJZ3MBG7LBBBEDL47EGZVQLNX4M"),
						"contextTypes",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWCMEQXOJXNC2VMFWON4PRNBA2A"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclQR333WQPYFHBPOCPLTZWA7LE7U"),
						org.radixware.kernel.common.enums.EValType.ARR_STR,
						org.radixware.kernel.common.enums.EPropNature.USER,
						4,
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acsPVLIUELSKTNRDAQSABIFNQAAAE"),
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						false,true,
						false,
						false,
						null,
						false,

						/*Radix::Scheduling::Task.UploadTraceToFiles:contextTypes:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acsPVLIUELSKTNRDAQSABIFNQAAAE"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.NONE,null),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGCSNEN3OBBHY7A37VLZRQFB2ZQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2URXUERY4JE23GLRJX7PFESWUQ"),
						false,1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Scheduling::Task.UploadTraceToFiles:timeShift:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruQGUAUSNCEZDB5HWV6EDZPULRX4"),
						"timeShift",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsL4KCQZPMAJEHJK2GY2W3U3BUT4"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEZVSIHAHXBFFFGZXL3BTWNPJJY"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclQR333WQPYFHBPOCPLTZWA7LE7U"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.USER,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("0"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Scheduling::Task.UploadTraceToFiles:timeShift:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(0L,999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Scheduling::Task.UploadTraceToFiles:traceFileDir:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruISFZYQNSZRGEBO37WJE34YLIO4"),
						"traceFileDir",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFZMODKH2XRCLBJIYYED2YUSDAI"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclQR333WQPYFHBPOCPLTZWA7LE7U"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.USER,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("./logs"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::Scheduling::Task.UploadTraceToFiles:traceFileDir:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Scheduling::Task.UploadTraceToFiles:rotateTraceFilesDaily:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruR4VBL67HCZEHREPZNGKKQIHUN4"),
						"rotateTraceFilesDaily",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOBB63CVD4FHAZMPWEJSBNPPAEQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclQR333WQPYFHBPOCPLTZWA7LE7U"),
						org.radixware.kernel.common.enums.EValType.BOOL,
						org.radixware.kernel.common.enums.EPropNature.USER,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("0"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::Scheduling::Task.UploadTraceToFiles:rotateTraceFilesDaily:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aclQR333WQPYFHBPOCPLTZWA7LE7U"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Scheduling::Task.UploadTraceToFiles:maxTraceFileSizeKb:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pru3TS5AZ7CEBAXFBIOMIX3OVE4PY"),
						"maxTraceFileSizeKb",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsV2VJ2MNX6ZBVHBRP7K6XA4SNNY"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclQR333WQPYFHBPOCPLTZWA7LE7U"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.USER,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("1024"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::Scheduling::Task.UploadTraceToFiles:maxTraceFileSizeKb:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(0L,null,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Scheduling::Task.UploadTraceToFiles:maxTraceFileCnt:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruRV2BAKTB65AGDPVSAJGU574ZEM"),
						"maxTraceFileCnt",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsODFGNL2JXFAAZLUVNP5V22C5QU"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclQR333WQPYFHBPOCPLTZWA7LE7U"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.USER,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("10"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::Scheduling::Task.UploadTraceToFiles:maxTraceFileCnt:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(0L,null,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Scheduling::Task.UploadTraceToFiles:deleteDebugMess:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruAES7SOWWDBBURJUP3ITYWUALZI"),
						"deleteDebugMess",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMTKZZ6SIUFDUNBP6K5W4QE2ILU"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclQR333WQPYFHBPOCPLTZWA7LE7U"),
						org.radixware.kernel.common.enums.EValType.BOOL,
						org.radixware.kernel.common.enums.EPropNature.USER,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("0"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::Scheduling::Task.UploadTraceToFiles:deleteDebugMess:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aclQR333WQPYFHBPOCPLTZWA7LE7U"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Scheduling::Task.UploadTraceToFiles:splitByContexts:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruXA2FRHNJHZEQDKBEVVX7X6HR3E"),
						"splitByContexts",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMDEOSMSTHNBK7LNICEA4QIQNJY"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclQR333WQPYFHBPOCPLTZWA7LE7U"),
						org.radixware.kernel.common.enums.EValType.BOOL,
						org.radixware.kernel.common.enums.EPropNature.USER,
						4,
						null,
						null,
						null,
						false,
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

						/*Radix::Scheduling::Task.UploadTraceToFiles:splitByContexts:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aclQR333WQPYFHBPOCPLTZWA7LE7U"),null,null,Boolean.FALSE),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDJDXQRMDMNDQLHS3J2FKJNY36I"),
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Scheduling::Task.UploadTraceToFiles:maxContextFiles:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruIIQPMZKCF5BETMWH6DIDS2KBGE"),
						"maxContextFiles",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNURF5ER2YZFBNCMCTPBEVFDNZ4"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMCWR4ZSEJNDC7HJINSN2GVOJ7U"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclQR333WQPYFHBPOCPLTZWA7LE7U"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.USER,
						4,
						null,
						null,
						null,
						false,
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

						/*Radix::Scheduling::Task.UploadTraceToFiles:maxContextFiles:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(0L,null,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEFDG4QW5DBFYXDZEOBYUISDLBU"),
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Scheduling::Task.UploadTraceToFiles:eventUploadConditionFunc:PropertyPresentation-Object Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruOFVZJJFHHFAD5J5GZZRPONWIAA"),
						"eventUploadConditionFunc",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclQR333WQPYFHBPOCPLTZWA7LE7U"),
						org.radixware.kernel.common.enums.EValType.OBJECT,
						org.radixware.kernel.common.enums.EPropNature.USER,
						5,
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
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclUA6HHOL2SBCINE6MOG2GHKBYSI"),
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
			new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprVCSWHQ6PDNA23KMOFEQ3RMC6ME"),org.radixware.kernel.common.types.Id.Factory.loadFrom("eprQYXLRZJMHRBHPMBYLKEW24CA3I")},
			true,true,false);
}

/* Radix::Scheduling::Task.UploadTraceToFiles - Web Meta*/

/*Radix::Scheduling::Task.UploadTraceToFiles-Application Class*/

package org.radixware.ads.Scheduling.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Task.UploadTraceToFiles_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::Scheduling::Task.UploadTraceToFiles:Presentations-Entity Object Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aclQR333WQPYFHBPOCPLTZWA7LE7U"),
			"Radix::Scheduling::Task.UploadTraceToFiles",
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aclZDLN466RV5FE3BQIWSX56ZAETQ"),
			null,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRWJ57G54NNDDBATZHI2DWVVD3Y"),null,null,0,
			null,
			null,
			null,
			null,
			null,
			null,
			false,false,false);
}

/* Radix::Scheduling::Task.UploadTraceToFiles:Edit - Desktop Meta*/

/*Radix::Scheduling::Task.UploadTraceToFiles:Edit-Editor Presentation*/

package org.radixware.ads.Scheduling.explorer;
public final class Edit_mi{
	private static final class Edit_DEF extends org.radixware.kernel.common.client.meta.RadEditorPresentationDef{
		Edit_DEF(){
			super(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprVCSWHQ6PDNA23KMOFEQ3RMC6ME"),
			"Edit",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("eprQYXLRZJMHRBHPMBYLKEW24CA3I"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aclQR333WQPYFHBPOCPLTZWA7LE7U"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("tblWZB7K4HLJPOBDCIUAALOMT5GDM"),
			null,
			null,
			null,
			null,

			/*Radix::Scheduling::Task.UploadTraceToFiles:Edit:Children-Explorer Items*/
				null
			,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
			null,
			0,
			null,
			(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
			40112,0,0);
		}
		@Override
		protected org.radixware.kernel.common.client.models.Model createModelImpl(org.radixware.kernel.common.client.IClientEnvironment userSession) {    return new org.radixware.ads.Scheduling.explorer.Create:Model(userSession,this);
		}
	}
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new Edit_DEF(); 
;
}
/* Radix::Scheduling::Task.UploadTraceToFiles:Create - Desktop Meta*/

/*Radix::Scheduling::Task.UploadTraceToFiles:Create-Editor Presentation*/

package org.radixware.ads.Scheduling.explorer;
public final class Create_mi{
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new org.radixware.kernel.common.client.meta.RadEditorPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprQYXLRZJMHRBHPMBYLKEW24CA3I"),
	"Create",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
	org.radixware.kernel.common.types.Id.Factory.loadFrom("eprXGQ6C3VMQPOBDCKCAALOMT5GDM"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("aclQR333WQPYFHBPOCPLTZWA7LE7U"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("tblWZB7K4HLJPOBDCIUAALOMT5GDM"),
	null,
	null,

	/*Radix::Scheduling::Task.UploadTraceToFiles:Create:Editor Pages-Editor Presentation Pages*/
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

			/*Radix::Scheduling::Task.UploadTraceToFiles:Create:Additional-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgF6NI32RQZNGCBAO5VOMJXQAT54"),"Additional",org.radixware.kernel.common.types.Id.Factory.loadFrom("aclQR333WQPYFHBPOCPLTZWA7LE7U"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsL7MRFK3E65BQDMXPESRZNA7T74"),null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruJZ3MBG7LBBBEDL47EGZVQLNX4M"),0,1,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruV67ZMIX3MVDSTODNNVZLTDTJHA"),0,3,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruRV2BAKTB65AGDPVSAJGU574ZEM"),0,6,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("pru3TS5AZ7CEBAXFBIOMIX3OVE4PY"),0,5,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruR4VBL67HCZEHREPZNGKKQIHUN4"),0,7,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruISFZYQNSZRGEBO37WJE34YLIO4"),0,4,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruAES7SOWWDBBURJUP3ITYWUALZI"),0,8,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruXA2FRHNJHZEQDKBEVVX7X6HR3E"),0,0,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruIIQPMZKCF5BETMWH6DIDS2KBGE"),0,2,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruQGUAUSNCEZDB5HWV6EDZPULRX4"),0,9,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruOFVZJJFHHFAD5J5GZZRPONWIAA"),0,10,1,false,false)
			},null)
	},
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgHHRMHELUJTOBDCIVAALOMT5GDM")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgF6NI32RQZNGCBAO5VOMJXQAT54")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgL6FUZ36EMVEU3BPQ4FLI3TZ6KA"))}
	,

	/*Radix::Scheduling::Task.UploadTraceToFiles:Create:Children-Explorer Items*/
		null
	,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
	null,
	0,
	null,
	(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
	36016,0,0);
}
/* Radix::Scheduling::Task.UploadTraceToFiles:Create:Model - Desktop Executable*/

/*Radix::Scheduling::Task.UploadTraceToFiles:Create:Model-Entity Model Class*/

package org.radixware.ads.Scheduling.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.UploadTraceToFiles:Create:Model")
public class Create:Model  extends org.radixware.ads.Scheduling.explorer.Task.UploadTraceToFiles.Task.UploadTraceToFiles_DefaultModel.eprXGQ6C3VMQPOBDCKCAALOMT5GDM_ModelAdapter {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return Create:Model_mi.rdxMeta; }



	public Create:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadEditorPresentationDef def){super(userSession,def);}

	/*Radix::Scheduling::Task.UploadTraceToFiles:Create:Model:Nested classes-Nested Classes*/

	/*Radix::Scheduling::Task.UploadTraceToFiles:Create:Model:Properties-Properties*/

	/*Radix::Scheduling::Task.UploadTraceToFiles:Create:Model:deleteDebugMess-Presentation Property*/




	public class DeleteDebugMess extends org.radixware.ads.Scheduling.explorer.Task.UploadTraceToFiles.Task.UploadTraceToFiles_DefaultModel.eprXGQ6C3VMQPOBDCKCAALOMT5GDM_ModelAdapter.pruAES7SOWWDBBURJUP3ITYWUALZI{
		public DeleteDebugMess(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Bool dummy = ((Bool)x);
			setValue(dummy);
		}

		/*Radix::Scheduling::Task.UploadTraceToFiles:Create:Model:deleteDebugMess:EmbeddedClass-Property Presentation Embedded Class*/




		/*Radix::Scheduling::Task.UploadTraceToFiles:Create:Model:deleteDebugMess:Nested classes-Nested Classes*/

		/*Radix::Scheduling::Task.UploadTraceToFiles:Create:Model:deleteDebugMess:Properties-Properties*/

		/*Radix::Scheduling::Task.UploadTraceToFiles:Create:Model:deleteDebugMess:Methods-Methods*/

		/*Radix::Scheduling::Task.UploadTraceToFiles:Create:Model:deleteDebugMess:isEnabled-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.UploadTraceToFiles:Create:Model:deleteDebugMess:isEnabled")
		public published  boolean isEnabled () {
			return minSeverity.Value == null || minSeverity.Value == Arte::EventSeverity:Debug;
		}













		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.UploadTraceToFiles:Create:Model:deleteDebugMess")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.UploadTraceToFiles:Create:Model:deleteDebugMess")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public DeleteDebugMess getDeleteDebugMess(){return (DeleteDebugMess)getProperty(pruAES7SOWWDBBURJUP3ITYWUALZI);}

	/*Radix::Scheduling::Task.UploadTraceToFiles:Create:Model:minSeverity-Presentation Property*/




	public class MinSeverity extends org.radixware.ads.Scheduling.explorer.Task.UploadTraceToFiles.Task.UploadTraceToFiles_DefaultModel.eprXGQ6C3VMQPOBDCKCAALOMT5GDM_ModelAdapter.pruV67ZMIX3MVDSTODNNVZLTDTJHA{
		public MinSeverity(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
			if (aemQYXLRZJMHRBHPMBYLKEW24CA3I.this.getDefinition().isPropertyDefExistsById(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruAES7SOWWDBBURJUP3ITYWUALZI"))) {
				this.addDependent(aemQYXLRZJMHRBHPMBYLKEW24CA3I.this.getProperty(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruAES7SOWWDBBURJUP3ITYWUALZI")));
			}
		}
		@Override
		public void setServerValue(org.radixware.kernel.common.client.models.items.properties.PropertyValue val){
			Object x = val.getValue();
			org.radixware.kernel.common.enums.EEventSeverity dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EEventSeverity ? (org.radixware.kernel.common.enums.EEventSeverity)x : org.radixware.kernel.common.enums.EEventSeverity.getForValue(((Int)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.INT)).longValue()));
			val.refineValue(dummy);
			super.setServerValue(val);
		}
		@Override
		public Class<org.radixware.kernel.common.enums.EEventSeverity> getValClass(){
			return org.radixware.kernel.common.enums.EEventSeverity.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.enums.EEventSeverity dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EEventSeverity ? (org.radixware.kernel.common.enums.EEventSeverity)x : org.radixware.kernel.common.enums.EEventSeverity.getForValue(((Int)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.INT)).longValue()));
			setValue(dummy);
		}





				














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.UploadTraceToFiles:Create:Model:minSeverity")
		public  org.radixware.kernel.common.enums.EEventSeverity getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.UploadTraceToFiles:Create:Model:minSeverity")
		public   void setValue(org.radixware.kernel.common.enums.EEventSeverity val) {
			Value = val;
		}
	}
	public MinSeverity getMinSeverity(){return (MinSeverity)getProperty(pruV67ZMIX3MVDSTODNNVZLTDTJHA);}

	/*Radix::Scheduling::Task.UploadTraceToFiles:Create:Model:splitByContexts-Presentation Property*/




	public class SplitByContexts extends org.radixware.ads.Scheduling.explorer.Task.UploadTraceToFiles.Task.UploadTraceToFiles_DefaultModel.eprXGQ6C3VMQPOBDCKCAALOMT5GDM_ModelAdapter.pruXA2FRHNJHZEQDKBEVVX7X6HR3E{
		public SplitByContexts(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Bool dummy = ((Bool)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.UploadTraceToFiles:Create:Model:splitByContexts")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.UploadTraceToFiles:Create:Model:splitByContexts")
		public   void setValue(Bool val) {

			internal[splitByContexts] = val;
			updateVisibility();
		}
	}
	public SplitByContexts getSplitByContexts(){return (SplitByContexts)getProperty(pruXA2FRHNJHZEQDKBEVVX7X6HR3E);}












	/*Radix::Scheduling::Task.UploadTraceToFiles:Create:Model:Methods-Methods*/

	/*Radix::Scheduling::Task.UploadTraceToFiles:Create:Model:updateVisibility-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.UploadTraceToFiles:Create:Model:updateVisibility")
	public published  void updateVisibility () {
		contextTypes.setVisible(splitByContexts.Value == null || splitByContexts.Value.booleanValue());
		maxContextFiles.setVisible(splitByContexts.Value == null || splitByContexts.Value.booleanValue());
		splitByContexts.setMandatory(splitByContexts.Value != null);
	}

	/*Radix::Scheduling::Task.UploadTraceToFiles:Create:Model:afterRead-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.UploadTraceToFiles:Create:Model:afterRead")
	protected published  void afterRead () {
		super.afterRead();
		if (!(getContext() instanceof Explorer.Context::SelectorRowContext)) {
		    updateVisibility();
		}
	}


}

/* Radix::Scheduling::Task.UploadTraceToFiles:Create:Model - Desktop Meta*/

/*Radix::Scheduling::Task.UploadTraceToFiles:Create:Model-Entity Model Class*/

package org.radixware.ads.Scheduling.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Create:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aemQYXLRZJMHRBHPMBYLKEW24CA3I"),
						"Create:Model",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aemXGQ6C3VMQPOBDCKCAALOMT5GDM"),
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::Scheduling::Task.UploadTraceToFiles:Create:Model:Properties-Properties*/
						null,
						null,
						null,
						null,
						null,
						null,
						false,
						false,
						false
						);
}

/* Radix::Scheduling::Task.UploadTraceToFiles - Localizing Bundle */
package org.radixware.ads.Scheduling.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Task.UploadTraceToFiles - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<none>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<нет>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2URXUERY4JE23GLRJX7PFESWUQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<any>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<любые>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6OJYSEH7YNHKXM7Z5OEF4B4RU4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<yes>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<да>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDJDXQRMDMNDQLHS3J2FKJNY36I"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<1000>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<1000>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEFDG4QW5DBFYXDZEOBYUISDLBU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Uploaded period:  from (previous execution time - backward time shift) inclusively to  (current execution time - backward time shift) exclusively");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Выгружаемый период:  с (предыдущее время выполнения - сдвиг времени назад) включительно по  (текущее время выполнения - сдвиг времени назад) исключительно");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEZVSIHAHXBFFFGZXL3BTWNPJJY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Trace file directory");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Каталог файлов трассировки");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFZMODKH2XRCLBJIYYED2YUSDAI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<all>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<все>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGCSNEN3OBBHY7A37VLZRQFB2ZQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Backward time shift (s)");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Сдвиг времени назад (c)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsL4KCQZPMAJEHJK2GY2W3U3BUT4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Additional");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Дополнительно");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsL7MRFK3E65BQDMXPESRZNA7T74"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Limits the number of different files, which affects memory consumption during upload procedure");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ограничивает количество файлов, влияющее на количество потребляемой памяти при выгрузке");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMCWR4ZSEJNDC7HJINSN2GVOJ7U"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Generate separate file for each context");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Генерировать отдельный файл для каждого контекста");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMDEOSMSTHNBK7LNICEA4QIQNJY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Delete written debug messages");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Удалять записанные отладочные сообщения");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMTKZZ6SIUFDUNBP6K5W4QE2ILU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Maximum number of different contexts");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Максимальное количество различных контекстов ");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNURF5ER2YZFBNCMCTPBEVFDNZ4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Create new trace files at the beginning of each day");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Создавать новые файлы трассировки в начале каждого дня");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOBB63CVD4FHAZMPWEJSBNPPAEQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Maximum number of trace files");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Максимальное количество файлов трассировки");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsODFGNL2JXFAAZLUVNP5V22C5QU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Maximum number of different contexts for trace upload (%1) is reached, no new files will be generated");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Превышено максимальное количество различных контекстов для выгрузки трассы (%1), новые файлы не будут создаваться");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsR7BGQXPXS5FZXA2LNJKZA25UJY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.EVENT_CODE,org.radixware.kernel.common.enums.EEventSeverity.WARNING,"App",null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Upload Event Log to Files");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Выгрузка журнала событий в файлы");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRWJ57G54NNDDBATZHI2DWVVD3Y"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Minimum severity");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Минимальная серьезность");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsSGJRSH4NBRGURPNK44HFLZI624"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Maximum size of trace file (KB)");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Максимальный размер файла трассировки (Кб)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsV2VJ2MNX6ZBVHBRP7K6XA4SNNY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Event context types");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Типы контекстов сообщений");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWCMEQXOJXNC2VMFWON4PRNBA2A"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(Task.UploadTraceToFiles - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbaclQR333WQPYFHBPOCPLTZWA7LE7U"),"Task.UploadTraceToFiles - Localizing Bundle",$$$items$$$);
}
