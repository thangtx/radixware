
/* Radix::Scheduling::Task.UploadTraceToSyslog - Server Executable*/

/*Radix::Scheduling::Task.UploadTraceToSyslog-Application Class*/

package org.radixware.ads.Scheduling.server;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.UploadTraceToSyslog")
public class Task.UploadTraceToSyslog  extends org.radixware.ads.Scheduling.server.Task.Atomic  implements org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {


	/**Metainformation accessor method*/
	@SuppressWarnings("unused")
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return Task.UploadTraceToSyslog_mi.rdxMeta;}

	/*Radix::Scheduling::Task.UploadTraceToSyslog:Nested classes-Nested Classes*/

	/*Radix::Scheduling::Task.UploadTraceToSyslog:Properties-Properties*/

	/*Radix::Scheduling::Task.UploadTraceToSyslog:address-User Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.UploadTraceToSyslog:address")
	  Str getAddress() {
		return address;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.UploadTraceToSyslog:address")
	   void setAddress(Str val) {
		address = val;
	}

	/*Radix::Scheduling::Task.UploadTraceToSyslog:minSeverity-User Property*/








			















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.UploadTraceToSyslog:minSeverity")
	private final  org.radixware.kernel.common.enums.EEventSeverity getMinSeverity() {
		return minSeverity;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.UploadTraceToSyslog:minSeverity")
	private final   void setMinSeverity(org.radixware.kernel.common.enums.EEventSeverity val) {
		minSeverity = val;
	}

	/*Radix::Scheduling::Task.UploadTraceToSyslog:eventSource-User Property*/








			















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.UploadTraceToSyslog:eventSource")
	private final  org.radixware.ads.Arte.common.EventSource getEventSource() {
		return eventSource;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.UploadTraceToSyslog:eventSource")
	private final   void setEventSource(org.radixware.ads.Arte.common.EventSource val) {
		eventSource = val;
	}

	/*Radix::Scheduling::Task.UploadTraceToSyslog:timeShift-User Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.UploadTraceToSyslog:timeShift")
	private final  Int getTimeShift() {
		return timeShift;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.UploadTraceToSyslog:timeShift")
	private final   void setTimeShift(Int val) {
		timeShift = val;
	}

	/*Radix::Scheduling::Task.UploadTraceToSyslog:eventUploadConditionFunc-User Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.UploadTraceToSyslog:eventUploadConditionFunc")
	public  org.radixware.ads.Scheduling.server.UserFunc.EventUploadCondition getEventUploadConditionFunc() {
		return eventUploadConditionFunc;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.UploadTraceToSyslog:eventUploadConditionFunc")
	public   void setEventUploadConditionFunc(org.radixware.ads.Scheduling.server.UserFunc.EventUploadCondition val) {
		eventUploadConditionFunc = val;
	}



























































	/*Radix::Scheduling::Task.UploadTraceToSyslog:Methods-Methods*/

	/*Radix::Scheduling::Task.UploadTraceToSyslog:execute-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.UploadTraceToSyslog:execute")
	public published  void execute (java.sql.Timestamp prevExecTime, java.sql.Timestamp curExecTime) {
		if (prevExecTime == null || curExecTime == null) // do nothing on first run
		    return;

		SyslogUploader syslog = new SyslogUploader(address);

		if (eventUploadConditionFunc != null) {
		    Arte::Trace.debug(eventUploadConditionFunc.getInvokeDescr() + " will be used for event upload condition check during task execution", Arte::EventSource:Task);
		}

		try {
		    syslog.start();
		    
		    try (final System::EventLogCursor c = System::EventLogCursor.open(
		                minSeverity,
		                eventSource,
		                false,
		                syslog.subtractSeconds(prevExecTime, timeShift),
		                syslog.subtractSeconds(curExecTime, timeShift))
		        ) {
		                
		        while (c.next()) {
		            syslog.enterCachingSection();
		            try {
		                final System::EventLog event = c.event;
		                
		                final Boolean uploadConditionResult = eventUploadConditionFunc == null ? null : eventUploadConditionFunc.check(this, event);
		                if (!syslog.canUpload(uploadConditionResult)) {
		                    continue;
		                }
		                
		                final String m = syslog.normalizeMessage(event.message);
		                
		                String component;
		                try {
		                    component = c.component == null ? null : c.component.Value;
		                } catch (Exceptions::NoConstItemWithSuchValueError ex) {
		                    component = ex.value.toString();
		               }
		                
		                syslog.upload(
		                        c.raiseTime.Time,
		                        severityToLevel(event.severity),
		                        (component != null ? (component + ": ") : "") + m
		                        );
		            } finally {
		                syslog.leaveCachingSection();
		            }
		        }
		    }
		} finally {
		    if (eventUploadConditionFunc != null && (syslog.uploadConditionTrueCount != 0 || syslog.uploadConditionFalseCount != 0)) {
		        Arte::Trace.debug(eventUploadConditionFunc.getInvokeDescr() + " invoke results: uploadConditionTrue = " + syslog.uploadConditionTrueCount + ", uploadConditionFalse = " + syslog.uploadConditionFalseCount, Arte::EventSource:Task);
		    }
		    Arte::Trace.debug(java.text.MessageFormat.format("Task {0} execution finished. Uploaded events count: {1}", this.debugTitle, syslog.uploadedCount), Arte::EventSource:Task);
		    syslog.finish();
		}
	}

	/*Radix::Scheduling::Task.UploadTraceToSyslog:severityToLevel-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.UploadTraceToSyslog:severityToLevel")
	private final  org.apache.log4j.Level severityToLevel (org.radixware.kernel.common.enums.EEventSeverity s) {
		switch (s) {
		    case Arte::EventSeverity:Alarm:
		        return org.apache.log4j.Level.FATAL;
		    case Arte::EventSeverity:Error:
		        return org.apache.log4j.Level.ERROR;
		    case Arte::EventSeverity:Warning:
		        return org.apache.log4j.Level.WARN;
		    case Arte::EventSeverity:Event:
		        return org.apache.log4j.Level.INFO;
		    case Arte::EventSeverity:Debug:
		        return org.apache.log4j.Level.DEBUG;
		    default:
		        return null;
		}

	}


	@Override
	public org.radixware.kernel.server.types.FormHandler.NextDialogsRequest execCommand(org.radixware.kernel.common.types.Id cmdId, org.radixware.kernel.common.types.Id propId, org.apache.xmlbeans.XmlObject input, org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValsById, org.apache.xmlbeans.XmlObject output) throws org.radixware.kernel.common.exceptions.AppException,java.lang.InterruptedException{

			return super.execCommand(cmdId,propId,input,newPropValsById,output);
	}


}

/* Radix::Scheduling::Task.UploadTraceToSyslog - Server Meta*/

/*Radix::Scheduling::Task.UploadTraceToSyslog-Application Class*/

package org.radixware.ads.Scheduling.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Task.UploadTraceToSyslog_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("aclTZYNXJ3TMFBQPNGMVCPUNFDNNY"),"Task.UploadTraceToSyslog",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsT33BZIFZ7ZAENOEWCW7MMWYZ3Y"),

						org.radixware.kernel.common.enums.EClassType.APPLICATION,

						/*Radix::Scheduling::Task.UploadTraceToSyslog:Presentations-Entity Object Presentations*/
						/*Class presentation attributes*/
						 new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef(/*Owner Class Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("aclTZYNXJ3TMFBQPNGMVCPUNFDNNY"),
							/*Owner Class Name*/
							"Task.UploadTraceToSyslog",
							/*Title Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsT33BZIFZ7ZAENOEWCW7MMWYZ3Y"),
							/*Property presentations*/

							/*Radix::Scheduling::Task.UploadTraceToSyslog:Properties-Properties*/
							new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef[]{

									/*Radix::Scheduling::Task.UploadTraceToSyslog:address:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pru6GKNSOMR6JA4DB4L4O2UMRXE6E"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Scheduling::Task.UploadTraceToSyslog:minSeverity:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pru2JLVD46SQJF5FL2L6BYTFWAXJY"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Scheduling::Task.UploadTraceToSyslog:eventSource:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruPWXWWIIPEJA63L2Z4G3NW4EDIM"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Scheduling::Task.UploadTraceToSyslog:timeShift:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pru6VGJSDYSTNBZVGJ3QWDW3SNOFE"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Scheduling::Task.UploadTraceToSyslog:eventUploadConditionFunc:PropertyPresentation-Object Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadParentTitlePropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pru6X5BSGIDPFCJXFMRHSL6WI7G44"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,null,null,null,null,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr7IKMB24XZHOBDCMTAALOMT5GDM")},null,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr7IKMB24XZHOBDCMTAALOMT5GDM")},java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.TITLE,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT))
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
									/*Radix::Scheduling::Task.UploadTraceToSyslog:Edit-Editor Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprX5IS7EOCGJET7G4SUCNGJT7EPE"),
									"Edit",
									org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprHYCKW7GHDJD2ZILCGRQL5FML3Q"),
									40112,
									null,

									/*Radix::Scheduling::Task.UploadTraceToSyslog:Edit:Children-Explorer Items*/
										null
									,
									null,
									(org.radixware.kernel.server.meta.presentations.RadPropertyPresentationAttributes[]) null,
									org.radixware.kernel.common.enums.EEditorPresentationRightsInheritanceMode.NONE,
									null),

									new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef(
									/*Radix::Scheduling::Task.UploadTraceToSyslog:Create-Editor Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprHYCKW7GHDJD2ZILCGRQL5FML3Q"),
									"Create",
									org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprXGQ6C3VMQPOBDCKCAALOMT5GDM"),
									36016,
									null,

									/*Radix::Scheduling::Task.UploadTraceToSyslog:Create:Children-Explorer Items*/
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
							org.radixware.kernel.common.utils.Maps.fromArrays(new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprAIWBLDTSJTOBDCIVAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("eprXGQ6C3VMQPOBDCKCAALOMT5GDM")},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprX5IS7EOCGJET7G4SUCNGJT7EPE"),org.radixware.kernel.common.types.Id.Factory.loadFrom("eprHYCKW7GHDJD2ZILCGRQL5FML3Q")}),
							/*Object title format*/
							null,
							/*Class catalogs*/
							new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog[]{

									new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog(
									/*Radix::Scheduling::Task.UploadTraceToSyslog:Default-Dynamic Class Catalog*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eccVXWRKQLYJTOBDCIVAALOMT5GDM"),org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.ELinkMode.VIRTUAL,new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.Item[]{
										new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.ClassRef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aclTZYNXJ3TMFBQPNGMVCPUNFDNNY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("cctTU4RQWDDDZHDXKTRWIAPN7O6P4"),100.0,true,org.radixware.kernel.common.types.Id.Factory.loadFrom("aclTZYNXJ3TMFBQPNGMVCPUNFDNNY"),null)}
									)
							},
							/*Restrictions*/
							null,null,0),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblWZB7K4HLJPOBDCIUAALOMT5GDM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclZDLN466RV5FE3BQIWSX56ZAETQ"),

						/*Radix::Scheduling::Task.UploadTraceToSyslog:Properties-Properties*/
						new org.radixware.kernel.server.meta.clazzes.RadPropDef[]{

								/*Radix::Scheduling::Task.UploadTraceToSyslog:address-User Property*/

								new org.radixware.kernel.server.meta.clazzes.RadUserPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pru6GKNSOMR6JA4DB4L4O2UMRXE6E"),"address",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMC537N56CFBUVOKO5A236WYAPQ"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("tblWZB7K4HLJPOBDCIUAALOMT5GDM"),false,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::Task.UploadTraceToSyslog:minSeverity-User Property*/

								new org.radixware.kernel.server.meta.clazzes.RadUserPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pru2JLVD46SQJF5FL2L6BYTFWAXJY"),"minSeverity",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsR5JETUGR6JCS5NZ6YDHYGF45XA"),org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("acsFRO5RTQOG7NRDJH2ACQMTAIZT4"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("1")),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblWZB7K4HLJPOBDCIUAALOMT5GDM"),false,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::Task.UploadTraceToSyslog:eventSource-User Property*/

								new org.radixware.kernel.server.meta.clazzes.RadUserPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruPWXWWIIPEJA63L2Z4G3NW4EDIM"),"eventSource",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls32KTQNW255HB7LEXMOGHWET5WE"),org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("acsTNLJBZADHTNRDIPGABQAQNO6EY"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("tblWZB7K4HLJPOBDCIUAALOMT5GDM"),false,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::Task.UploadTraceToSyslog:timeShift-User Property*/

								new org.radixware.kernel.server.meta.clazzes.RadUserPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pru6VGJSDYSTNBZVGJ3QWDW3SNOFE"),"timeShift",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAQMODBBPOJBB3C3RY2B6KNDXWQ"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DEFINE_ALWAYS,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("30")),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblWZB7K4HLJPOBDCIUAALOMT5GDM"),false,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::Task.UploadTraceToSyslog:eventUploadConditionFunc-User Property*/

								new org.radixware.kernel.server.meta.clazzes.RadUserRefPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pru6X5BSGIDPFCJXFMRHSL6WI7G44"),"eventUploadConditionFunc",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEYV2CSEBAVBQDJ2Z4NFKEIDVYE"),org.radixware.kernel.common.enums.EValType.OBJECT,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("tblWZB7K4HLJPOBDCIUAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aclUA6HHOL2SBCINE6MOG2GHKBYSI"),false,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE)
						},

						/*Radix::Scheduling::Task.UploadTraceToSyslog:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthN36NOGMSW5EGZIX2JAVCBWM2RU"),"execute",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("prevExecTime",org.radixware.kernel.common.enums.EValType.DATE_TIME,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprS63AAHQ5TNFDRNHOYTMWMFBYBM")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("curExecTime",org.radixware.kernel.common.enums.EValType.DATE_TIME,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprN4O3WP52L5BMTJOMXMHGVCCC2E"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthJU2AXKC3YNBMLGMBJUXAG6NHEA"),"severityToLevel",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("s",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprH4B3LV6QKVARPPORVC6YH7FZX4"))
								},org.radixware.kernel.common.enums.EValType.JAVA_CLASS)
						},
						null,
						null,null,null,false);
}

/* Radix::Scheduling::Task.UploadTraceToSyslog - Desktop Executable*/

/*Radix::Scheduling::Task.UploadTraceToSyslog-Application Class*/

package org.radixware.ads.Scheduling.explorer;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.UploadTraceToSyslog")
public interface Task.UploadTraceToSyslog   extends org.radixware.ads.Scheduling.explorer.Task.Atomic  {





































































































	/*Radix::Scheduling::Task.UploadTraceToSyslog:minSeverity:minSeverity-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.UploadTraceToSyslog:minSeverity:minSeverity")
		public  org.radixware.kernel.common.enums.EEventSeverity getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.UploadTraceToSyslog:minSeverity:minSeverity")
		public   void setValue(org.radixware.kernel.common.enums.EEventSeverity val) {
			Value = val;
		}
	}
	public MinSeverity getMinSeverity();
	/*Radix::Scheduling::Task.UploadTraceToSyslog:address:address-Presentation Property*/


	public class Address extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public Address(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.UploadTraceToSyslog:address:address")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.UploadTraceToSyslog:address:address")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Address getAddress();
	/*Radix::Scheduling::Task.UploadTraceToSyslog:timeShift:timeShift-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.UploadTraceToSyslog:timeShift:timeShift")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.UploadTraceToSyslog:timeShift:timeShift")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public TimeShift getTimeShift();
	/*Radix::Scheduling::Task.UploadTraceToSyslog:eventUploadConditionFunc:eventUploadConditionFunc-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.UploadTraceToSyslog:eventUploadConditionFunc:eventUploadConditionFunc")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.UploadTraceToSyslog:eventUploadConditionFunc:eventUploadConditionFunc")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public EventUploadConditionFunc getEventUploadConditionFunc();
	/*Radix::Scheduling::Task.UploadTraceToSyslog:eventSource:eventSource-Presentation Property*/


	public class EventSource extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public EventSource(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public void setServerValue(org.radixware.kernel.common.client.models.items.properties.PropertyValue val){
			Object x = val.getValue();
			org.radixware.ads.Arte.common.EventSource dummy = x == null ? null : (x instanceof org.radixware.ads.Arte.common.EventSource ? (org.radixware.ads.Arte.common.EventSource)x : org.radixware.ads.Arte.common.EventSource.getForValue((Str)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.STR)));
			val.refineValue(dummy);
			super.setServerValue(val);
		}
		@Override
		public Class<org.radixware.ads.Arte.common.EventSource> getValClass(){
			return org.radixware.ads.Arte.common.EventSource.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.ads.Arte.common.EventSource dummy = x == null ? null : (x instanceof org.radixware.ads.Arte.common.EventSource ? (org.radixware.ads.Arte.common.EventSource)x : org.radixware.ads.Arte.common.EventSource.getForValue((Str)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.STR)));
			setValue(dummy);
		}





				














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.UploadTraceToSyslog:eventSource:eventSource")
		public  org.radixware.ads.Arte.common.EventSource getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.UploadTraceToSyslog:eventSource:eventSource")
		public   void setValue(org.radixware.ads.Arte.common.EventSource val) {
			Value = val;
		}
	}
	public EventSource getEventSource();


}

/* Radix::Scheduling::Task.UploadTraceToSyslog - Desktop Meta*/

/*Radix::Scheduling::Task.UploadTraceToSyslog-Application Class*/

package org.radixware.ads.Scheduling.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Task.UploadTraceToSyslog_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::Scheduling::Task.UploadTraceToSyslog:Presentations-Entity Object Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aclTZYNXJ3TMFBQPNGMVCPUNFDNNY"),
			"Radix::Scheduling::Task.UploadTraceToSyslog",
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aclZDLN466RV5FE3BQIWSX56ZAETQ"),
			null,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsT33BZIFZ7ZAENOEWCW7MMWYZ3Y"),null,null,0,

			/*Radix::Scheduling::Task.UploadTraceToSyslog:Properties-Properties*/
			new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

					/*Radix::Scheduling::Task.UploadTraceToSyslog:address:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pru6GKNSOMR6JA4DB4L4O2UMRXE6E"),
						"address",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMC537N56CFBUVOKO5A236WYAPQ"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFT2EFW6VA5DE5P2RIGL4TTLXGE"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclTZYNXJ3TMFBQPNGMVCPUNFDNNY"),
						org.radixware.kernel.common.enums.EValType.STR,
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
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::Scheduling::Task.UploadTraceToSyslog:address:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Scheduling::Task.UploadTraceToSyslog:minSeverity:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pru2JLVD46SQJF5FL2L6BYTFWAXJY"),
						"minSeverity",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsR5JETUGR6JCS5NZ6YDHYGF45XA"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclTZYNXJ3TMFBQPNGMVCPUNFDNNY"),
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

						/*Radix::Scheduling::Task.UploadTraceToSyslog:minSeverity:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acsFRO5RTQOG7NRDJH2ACQMTAIZT4"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.NONE,null),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsN66KJCS7MBFQTDNWWUYR6Z3QEA"),
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Scheduling::Task.UploadTraceToSyslog:eventSource:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruPWXWWIIPEJA63L2Z4G3NW4EDIM"),
						"eventSource",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls32KTQNW255HB7LEXMOGHWET5WE"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclTZYNXJ3TMFBQPNGMVCPUNFDNNY"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.USER,
						4,
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acsTNLJBZADHTNRDIPGABQAQNO6EY"),
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

						/*Radix::Scheduling::Task.UploadTraceToSyslog:eventSource:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acsTNLJBZADHTNRDIPGABQAQNO6EY"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_TITLE,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.NONE,null),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBDORHX33QFBNLD4CABE76B3UBA"),
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Scheduling::Task.UploadTraceToSyslog:timeShift:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pru6VGJSDYSTNBZVGJ3QWDW3SNOFE"),
						"timeShift",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAQMODBBPOJBB3C3RY2B6KNDXWQ"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOHHPXJGACJA5TF632FRR5XLLPY"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclTZYNXJ3TMFBQPNGMVCPUNFDNNY"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.USER,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("30"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Scheduling::Task.UploadTraceToSyslog:timeShift:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(1L,999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Scheduling::Task.UploadTraceToSyslog:eventUploadConditionFunc:PropertyPresentation-Object Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pru6X5BSGIDPFCJXFMRHSL6WI7G44"),
						"eventUploadConditionFunc",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclTZYNXJ3TMFBQPNGMVCPUNFDNNY"),
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
			new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprX5IS7EOCGJET7G4SUCNGJT7EPE"),org.radixware.kernel.common.types.Id.Factory.loadFrom("eprHYCKW7GHDJD2ZILCGRQL5FML3Q")},
			true,true,false);
}

/* Radix::Scheduling::Task.UploadTraceToSyslog - Web Meta*/

/*Radix::Scheduling::Task.UploadTraceToSyslog-Application Class*/

package org.radixware.ads.Scheduling.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Task.UploadTraceToSyslog_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::Scheduling::Task.UploadTraceToSyslog:Presentations-Entity Object Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aclTZYNXJ3TMFBQPNGMVCPUNFDNNY"),
			"Radix::Scheduling::Task.UploadTraceToSyslog",
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aclZDLN466RV5FE3BQIWSX56ZAETQ"),
			null,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsT33BZIFZ7ZAENOEWCW7MMWYZ3Y"),null,null,0,
			null,
			null,
			null,
			null,
			null,
			null,
			false,false,false);
}

/* Radix::Scheduling::Task.UploadTraceToSyslog:Edit - Desktop Meta*/

/*Radix::Scheduling::Task.UploadTraceToSyslog:Edit-Editor Presentation*/

package org.radixware.ads.Scheduling.explorer;
public final class Edit_mi{
	private static final class Edit_DEF extends org.radixware.kernel.common.client.meta.RadEditorPresentationDef{
		Edit_DEF(){
			super(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprX5IS7EOCGJET7G4SUCNGJT7EPE"),
			"Edit",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("eprHYCKW7GHDJD2ZILCGRQL5FML3Q"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aclTZYNXJ3TMFBQPNGMVCPUNFDNNY"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("tblWZB7K4HLJPOBDCIUAALOMT5GDM"),
			null,
			null,
			null,
			null,

			/*Radix::Scheduling::Task.UploadTraceToSyslog:Edit:Children-Explorer Items*/
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
/* Radix::Scheduling::Task.UploadTraceToSyslog:Create - Desktop Meta*/

/*Radix::Scheduling::Task.UploadTraceToSyslog:Create-Editor Presentation*/

package org.radixware.ads.Scheduling.explorer;
public final class Create_mi{
	private static final class Create_DEF extends org.radixware.kernel.common.client.meta.RadEditorPresentationDef{
		Create_DEF(){
			super(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprHYCKW7GHDJD2ZILCGRQL5FML3Q"),
			"Create",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("eprXGQ6C3VMQPOBDCKCAALOMT5GDM"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aclTZYNXJ3TMFBQPNGMVCPUNFDNNY"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("tblWZB7K4HLJPOBDCIUAALOMT5GDM"),
			null,
			null,

			/*Radix::Scheduling::Task.UploadTraceToSyslog:Create:Editor Pages-Editor Presentation Pages*/
			new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

					/*Radix::Scheduling::Task.UploadTraceToSyslog:Create:Additional-Editor Page*/

					 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgUUF6FL7CRZEW7JQ3XQHNJDJZTM"),"Additional",org.radixware.kernel.common.types.Id.Factory.loadFrom("aclTZYNXJ3TMFBQPNGMVCPUNFDNNY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWOVP4Q2CFBELBILWTJSRHCENXY"),null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("pru6GKNSOMR6JA4DB4L4O2UMRXE6E"),0,0,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruPWXWWIIPEJA63L2Z4G3NW4EDIM"),0,1,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("pru6VGJSDYSTNBZVGJ3QWDW3SNOFE"),0,3,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("pru2JLVD46SQJF5FL2L6BYTFWAXJY"),0,2,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("pru6X5BSGIDPFCJXFMRHSL6WI7G44"),0,4,1,false,false)
					},null)
			},
			new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
				new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgHHRMHELUJTOBDCIVAALOMT5GDM")),
				new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgUUF6FL7CRZEW7JQ3XQHNJDJZTM")),
				new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgL6FUZ36EMVEU3BPQ4FLI3TZ6KA"))}
			,

			/*Radix::Scheduling::Task.UploadTraceToSyslog:Create:Children-Explorer Items*/
				null
			,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
			null,
			0,
			null,
			(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
			36016,0,0);
		}
		@Override
		protected org.radixware.kernel.common.client.models.Model createModelImpl(org.radixware.kernel.common.client.IClientEnvironment userSession) {    return new org.radixware.ads.Scheduling.explorer.Task.UploadTraceToSyslog.Task.UploadTraceToSyslog_DefaultModel.eprXGQ6C3VMQPOBDCKCAALOMT5GDM_ModelAdapter(userSession,this);
		}
	}
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new Create_DEF(); 
;
}
/* Radix::Scheduling::Task.UploadTraceToSyslog - Localizing Bundle */
package org.radixware.ads.Scheduling.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Task.UploadTraceToSyslog - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Event source");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Источник событий");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls32KTQNW255HB7LEXMOGHWET5WE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Backward time shift (s)");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Сдвиг времени назад (c)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAQMODBBPOJBB3C3RY2B6KNDXWQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<any>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<любые>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBDORHX33QFBNLD4CABE76B3UBA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Addresses delimiter is \";\". Default port is 514.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Разделитель адресов - \";\". Порт по умолчанию - 514.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFT2EFW6VA5DE5P2RIGL4TTLXGE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Syslog server addresses");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Адреса серверов Syslog");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMC537N56CFBUVOKO5A236WYAPQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<any>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<любые>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsN66KJCS7MBFQTDNWWUYR6Z3QEA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Uploaded period:  from (previous execution time - backward time shift) inclusively to  (current execution time - backward time shift) exclusively");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Выгружаемый период:  с (предыдущее время выполнения - сдвиг времени назад) включительно по  (текущее время выполнения - сдвиг времени назад) исключительно");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOHHPXJGACJA5TF632FRR5XLLPY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Minimum severity");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Минимальная серьезность");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsR5JETUGR6JCS5NZ6YDHYGF45XA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Upload Event Log to Syslog");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Выгрузка журнала событий в Syslog");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsT33BZIFZ7ZAENOEWCW7MMWYZ3Y"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Additional");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Дополнительно");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWOVP4Q2CFBELBILWTJSRHCENXY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(Task.UploadTraceToSyslog - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbaclTZYNXJ3TMFBQPNGMVCPUNFDNNY"),"Task.UploadTraceToSyslog - Localizing Bundle",$$$items$$$);
}
