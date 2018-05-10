
/* Radix::Scheduling::Task.UploadAuditLogToSyslog - Server Executable*/

/*Radix::Scheduling::Task.UploadAuditLogToSyslog-Application Class*/

package org.radixware.ads.Scheduling.server;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.UploadAuditLogToSyslog")
public class Task.UploadAuditLogToSyslog  extends org.radixware.ads.Scheduling.server.Task.Atomic  implements org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {


	/**Metainformation accessor method*/
	@SuppressWarnings("unused")
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return Task.UploadAuditLogToSyslog_mi.rdxMeta;}

	/*Radix::Scheduling::Task.UploadAuditLogToSyslog:Nested classes-Nested Classes*/

	/*Radix::Scheduling::Task.UploadAuditLogToSyslog:Properties-Properties*/

	/*Radix::Scheduling::Task.UploadAuditLogToSyslog:address-User Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.UploadAuditLogToSyslog:address")
	  Str getAddress() {
		return address;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.UploadAuditLogToSyslog:address")
	   void setAddress(Str val) {
		address = val;
	}

	/*Radix::Scheduling::Task.UploadAuditLogToSyslog:timeShift-User Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.UploadAuditLogToSyslog:timeShift")
	private final  Int getTimeShift() {
		return timeShift;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.UploadAuditLogToSyslog:timeShift")
	private final   void setTimeShift(Int val) {
		timeShift = val;
	}

	/*Radix::Scheduling::Task.UploadAuditLogToSyslog:eventType-User Property*/








			















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.UploadAuditLogToSyslog:eventType")
	public  org.radixware.ads.Audit.common.AuditEventType getEventType() {
		return eventType;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.UploadAuditLogToSyslog:eventType")
	public   void setEventType(org.radixware.ads.Audit.common.AuditEventType val) {
		eventType = val;
	}

	/*Radix::Scheduling::Task.UploadAuditLogToSyslog:user-User Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.UploadAuditLogToSyslog:user")
	public  Str getUser() {
		return user;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.UploadAuditLogToSyslog:user")
	public   void setUser(Str val) {
		user = val;
	}

	/*Radix::Scheduling::Task.UploadAuditLogToSyslog:station-User Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.UploadAuditLogToSyslog:station")
	public  Str getStation() {
		return station;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.UploadAuditLogToSyslog:station")
	public   void setStation(Str val) {
		station = val;
	}



























































	/*Radix::Scheduling::Task.UploadAuditLogToSyslog:Methods-Methods*/

	/*Radix::Scheduling::Task.UploadAuditLogToSyslog:execute-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.UploadAuditLogToSyslog:execute")
	public published  void execute (java.sql.Timestamp prevExecTime, java.sql.Timestamp curExecTime) {
		if (prevExecTime == null || curExecTime == null) // do nothing on first run
		    return;

		SyslogUploader syslog = new SyslogUploader(address);
		int eventsSkipped = 0;
		try {
		    syslog.start();
		    
		    try (final Audit::AuditLogCursor c = Audit::AuditLogCursor.open(
		                eventType,
		                user,
		                station,
		                syslog.subtractSeconds(prevExecTime, timeShift),
		                syslog.subtractSeconds(curExecTime, timeShift))
		        ) {
		        while (c.next()) {
		            syslog.enterCachingSection();
		            try {
		                if (c.logRecord == null || c.eventTime == null) {
		                    ++eventsSkipped;
		                    continue;
		                }
		                final String m = syslog.normalizeMessage(c.logRecord.changes);
		                
		                syslog.upload(
		                        c.eventTime.Time,
		                        org.apache.log4j.Level.INFO,
		                        "Table: " + c.logRecord.tableName + ", Object: " + c.logRecord.objectId + ", Class: " + c.logRecord.classTitle + ", Changes: " + m
		                        );
		            } finally {
		                syslog.leaveCachingSection();
		            }
		        }
		    }
		} finally {
		    Arte::Trace.debug(java.text.MessageFormat.format("Task {0} execution finished. Uploaded events count: {1}. Skipped events count: {2}", this.debugTitle, syslog.uploadedCount, eventsSkipped), Arte::EventSource:Task);
		    syslog.finish();
		}
	}

	/*Radix::Scheduling::Task.UploadAuditLogToSyslog:severityToLevel-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.UploadAuditLogToSyslog:severityToLevel")
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

/* Radix::Scheduling::Task.UploadAuditLogToSyslog - Server Meta*/

/*Radix::Scheduling::Task.UploadAuditLogToSyslog-Application Class*/

package org.radixware.ads.Scheduling.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Task.UploadAuditLogToSyslog_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("aclJGXIKMD2CJB2FIX4VCYXAMH2K4"),"Task.UploadAuditLogToSyslog",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPFOJCYK67FABXGAE5FKK6SPQQ4"),

						org.radixware.kernel.common.enums.EClassType.APPLICATION,

						/*Radix::Scheduling::Task.UploadAuditLogToSyslog:Presentations-Entity Object Presentations*/
						/*Class presentation attributes*/
						 new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef(/*Owner Class Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("aclJGXIKMD2CJB2FIX4VCYXAMH2K4"),
							/*Owner Class Name*/
							"Task.UploadAuditLogToSyslog",
							/*Title Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPFOJCYK67FABXGAE5FKK6SPQQ4"),
							/*Property presentations*/

							/*Radix::Scheduling::Task.UploadAuditLogToSyslog:Properties-Properties*/
							new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef[]{

									/*Radix::Scheduling::Task.UploadAuditLogToSyslog:address:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruE334JEY2SZHZJGXNNYO3CXUF3Q"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Scheduling::Task.UploadAuditLogToSyslog:timeShift:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruMOC23GAGTBGM7NXCD6GH3YHMOU"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Scheduling::Task.UploadAuditLogToSyslog:eventType:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruRX2OS4OZHRANJBR772W6UEB3TQ"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Scheduling::Task.UploadAuditLogToSyslog:user:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pru2BTBIESLYNGPZLEU2UYFAUATME"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Scheduling::Task.UploadAuditLogToSyslog:station:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruZIDKNWHWYZGJ5GNBJS5IPJP6XM"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT))
							},
							/*Commands*/
							new org.radixware.kernel.server.meta.presentations.RadCommandDef[]{

									new org.radixware.kernel.server.meta.presentations.RadCommandDef(
									/*Radix::Scheduling::Task.UploadAuditLogToSyslog:SelectObjectCmd-Property Command*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdAEIZI65YBRHWVAHN3GDZDVPVO4"),"SelectObjectCmd",org.radixware.kernel.common.enums.ECommandScope.PROPERTY,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("pru2BTBIESLYNGPZLEU2UYFAUATME"),org.radixware.kernel.common.types.Id.Factory.loadFrom("pruZIDKNWHWYZGJ5GNBJS5IPJP6XM")},org.radixware.kernel.common.enums.ECommandAccessibility.ALWAYS,org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,org.radixware.kernel.common.types.Id.Factory.loadFrom("aclJGXIKMD2CJB2FIX4VCYXAMH2K4"),false,true)
							},
							/*Sortings*/
							null,
							/*Filters*/
							null,
							/*Editor presentations*/
							new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef[]{

									new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef(
									/*Radix::Scheduling::Task.UploadAuditLogToSyslog:Edit-Editor Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprK3FEXBH46NHURJCLX57NMBFMQA"),
									"Edit",
									org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprUID3A6REAJCG3G2DONE3GU5KIQ"),
									40112,
									null,

									/*Radix::Scheduling::Task.UploadAuditLogToSyslog:Edit:Children-Explorer Items*/
										null
									,
									null,
									(org.radixware.kernel.server.meta.presentations.RadPropertyPresentationAttributes[]) null,
									org.radixware.kernel.common.enums.EEditorPresentationRightsInheritanceMode.NONE,
									null),

									new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef(
									/*Radix::Scheduling::Task.UploadAuditLogToSyslog:Create-Editor Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprUID3A6REAJCG3G2DONE3GU5KIQ"),
									"Create",
									org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprXGQ6C3VMQPOBDCKCAALOMT5GDM"),
									36016,
									null,

									/*Radix::Scheduling::Task.UploadAuditLogToSyslog:Create:Children-Explorer Items*/
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
							org.radixware.kernel.common.utils.Maps.fromArrays(new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprAIWBLDTSJTOBDCIVAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("eprXGQ6C3VMQPOBDCKCAALOMT5GDM")},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprK3FEXBH46NHURJCLX57NMBFMQA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("eprUID3A6REAJCG3G2DONE3GU5KIQ")}),
							/*Object title format*/
							null,
							/*Class catalogs*/
							new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog[]{

									new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog(
									/*Radix::Scheduling::Task.UploadAuditLogToSyslog:Default-Dynamic Class Catalog*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eccVXWRKQLYJTOBDCIVAALOMT5GDM"),org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.ELinkMode.VIRTUAL,new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.Item[]{
										new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.ClassRef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aclJGXIKMD2CJB2FIX4VCYXAMH2K4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("cctTU4RQWDDDZHDXKTRWIAPN7O6P4"),110.0,true,org.radixware.kernel.common.types.Id.Factory.loadFrom("aclJGXIKMD2CJB2FIX4VCYXAMH2K4"),null)}
									)
							},
							/*Restrictions*/
							null,null,0),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblWZB7K4HLJPOBDCIUAALOMT5GDM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclZDLN466RV5FE3BQIWSX56ZAETQ"),

						/*Radix::Scheduling::Task.UploadAuditLogToSyslog:Properties-Properties*/
						new org.radixware.kernel.server.meta.clazzes.RadPropDef[]{

								/*Radix::Scheduling::Task.UploadAuditLogToSyslog:address-User Property*/

								new org.radixware.kernel.server.meta.clazzes.RadUserPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruE334JEY2SZHZJGXNNYO3CXUF3Q"),"address",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKPW2SJDZGZGSLIRNLKWS5M5WS4"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("tblWZB7K4HLJPOBDCIUAALOMT5GDM"),false,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::Task.UploadAuditLogToSyslog:timeShift-User Property*/

								new org.radixware.kernel.server.meta.clazzes.RadUserPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruMOC23GAGTBGM7NXCD6GH3YHMOU"),"timeShift",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4R3WL62YSRANBBGKH6QC3OX73U"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DEFINE_ALWAYS,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("30")),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblWZB7K4HLJPOBDCIUAALOMT5GDM"),false,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::Task.UploadAuditLogToSyslog:eventType-User Property*/

								new org.radixware.kernel.server.meta.clazzes.RadUserPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruRX2OS4OZHRANJBR772W6UEB3TQ"),"eventType",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7HTAX7YRHRARXM7YURGHIK3H5M"),org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("acsAZO5RTQOG7NRDJH2ACQMTAIZT4"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("tblWZB7K4HLJPOBDCIUAALOMT5GDM"),false,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::Task.UploadAuditLogToSyslog:user-User Property*/

								new org.radixware.kernel.server.meta.clazzes.RadUserPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pru2BTBIESLYNGPZLEU2UYFAUATME"),"user",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTFGUFPZBSVCOJJR7D4LKEUY3PM"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("tblWZB7K4HLJPOBDCIUAALOMT5GDM"),false,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::Task.UploadAuditLogToSyslog:station-User Property*/

								new org.radixware.kernel.server.meta.clazzes.RadUserPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruZIDKNWHWYZGJ5GNBJS5IPJP6XM"),"station",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3P4424GC2BD3LI6EWOKLGFQCOQ"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("tblWZB7K4HLJPOBDCIUAALOMT5GDM"),false,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE)
						},

						/*Radix::Scheduling::Task.UploadAuditLogToSyslog:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthN36NOGMSW5EGZIX2JAVCBWM2RU"),"execute",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("prevExecTime",org.radixware.kernel.common.enums.EValType.DATE_TIME,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprS63AAHQ5TNFDRNHOYTMWMFBYBM")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("curExecTime",org.radixware.kernel.common.enums.EValType.DATE_TIME,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprN4O3WP52L5BMTJOMXMHGVCCC2E"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthVBFBFJGB25G2ZGFPADRT4GBHLM"),"severityToLevel",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("s",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprH4B3LV6QKVARPPORVC6YH7FZX4"))
								},org.radixware.kernel.common.enums.EValType.JAVA_CLASS)
						},
						null,
						null,null,null,false);
}

/* Radix::Scheduling::Task.UploadAuditLogToSyslog - Desktop Executable*/

/*Radix::Scheduling::Task.UploadAuditLogToSyslog-Application Class*/

package org.radixware.ads.Scheduling.explorer;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.UploadAuditLogToSyslog")
public interface Task.UploadAuditLogToSyslog   extends org.radixware.ads.Scheduling.explorer.Task.Atomic  {

















































































































	/*Radix::Scheduling::Task.UploadAuditLogToSyslog:user:user-Presentation Property*/


	public class User extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public User(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.UploadAuditLogToSyslog:user:user")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.UploadAuditLogToSyslog:user:user")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public User getUser();
	/*Radix::Scheduling::Task.UploadAuditLogToSyslog:address:address-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.UploadAuditLogToSyslog:address:address")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.UploadAuditLogToSyslog:address:address")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Address getAddress();
	/*Radix::Scheduling::Task.UploadAuditLogToSyslog:timeShift:timeShift-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.UploadAuditLogToSyslog:timeShift:timeShift")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.UploadAuditLogToSyslog:timeShift:timeShift")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public TimeShift getTimeShift();
	/*Radix::Scheduling::Task.UploadAuditLogToSyslog:eventType:eventType-Presentation Property*/


	public class EventType extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public EventType(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public void setServerValue(org.radixware.kernel.common.client.models.items.properties.PropertyValue val){
			Object x = val.getValue();
			org.radixware.ads.Audit.common.AuditEventType dummy = x == null ? null : (x instanceof org.radixware.ads.Audit.common.AuditEventType ? (org.radixware.ads.Audit.common.AuditEventType)x : org.radixware.ads.Audit.common.AuditEventType.getForValue((Str)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.STR)));
			val.refineValue(dummy);
			super.setServerValue(val);
		}
		@Override
		public Class<org.radixware.ads.Audit.common.AuditEventType> getValClass(){
			return org.radixware.ads.Audit.common.AuditEventType.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.ads.Audit.common.AuditEventType dummy = x == null ? null : (x instanceof org.radixware.ads.Audit.common.AuditEventType ? (org.radixware.ads.Audit.common.AuditEventType)x : org.radixware.ads.Audit.common.AuditEventType.getForValue((Str)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.STR)));
			setValue(dummy);
		}





				














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.UploadAuditLogToSyslog:eventType:eventType")
		public  org.radixware.ads.Audit.common.AuditEventType getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.UploadAuditLogToSyslog:eventType:eventType")
		public   void setValue(org.radixware.ads.Audit.common.AuditEventType val) {
			Value = val;
		}
	}
	public EventType getEventType();
	/*Radix::Scheduling::Task.UploadAuditLogToSyslog:station:station-Presentation Property*/


	public class Station extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public Station(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.UploadAuditLogToSyslog:station:station")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.UploadAuditLogToSyslog:station:station")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Station getStation();
	public static class SelectObjectCmd extends org.radixware.kernel.common.client.models.items.Command{
		protected SelectObjectCmd(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public void send(org.radixware.kernel.common.types.Id propertyId) throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),propertyId,null);
			processResponse(response, propertyId);
		}

	}



}

/* Radix::Scheduling::Task.UploadAuditLogToSyslog - Desktop Meta*/

/*Radix::Scheduling::Task.UploadAuditLogToSyslog-Application Class*/

package org.radixware.ads.Scheduling.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Task.UploadAuditLogToSyslog_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::Scheduling::Task.UploadAuditLogToSyslog:Presentations-Entity Object Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aclJGXIKMD2CJB2FIX4VCYXAMH2K4"),
			"Radix::Scheduling::Task.UploadAuditLogToSyslog",
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aclZDLN466RV5FE3BQIWSX56ZAETQ"),
			null,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPFOJCYK67FABXGAE5FKK6SPQQ4"),null,null,0,

			/*Radix::Scheduling::Task.UploadAuditLogToSyslog:Properties-Properties*/
			new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

					/*Radix::Scheduling::Task.UploadAuditLogToSyslog:address:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruE334JEY2SZHZJGXNNYO3CXUF3Q"),
						"address",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKPW2SJDZGZGSLIRNLKWS5M5WS4"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCNAJC6KS2BGNTDWMHLHHEQVTHA"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclJGXIKMD2CJB2FIX4VCYXAMH2K4"),
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

						/*Radix::Scheduling::Task.UploadAuditLogToSyslog:address:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Scheduling::Task.UploadAuditLogToSyslog:timeShift:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruMOC23GAGTBGM7NXCD6GH3YHMOU"),
						"timeShift",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4R3WL62YSRANBBGKH6QC3OX73U"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUTRVB6QPDFGDXC5GVHXECURRFA"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclJGXIKMD2CJB2FIX4VCYXAMH2K4"),
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

						/*Radix::Scheduling::Task.UploadAuditLogToSyslog:timeShift:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(null,null,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Scheduling::Task.UploadAuditLogToSyslog:eventType:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruRX2OS4OZHRANJBR772W6UEB3TQ"),
						"eventType",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7HTAX7YRHRARXM7YURGHIK3H5M"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclJGXIKMD2CJB2FIX4VCYXAMH2K4"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.USER,
						4,
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acsAZO5RTQOG7NRDJH2ACQMTAIZT4"),
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

						/*Radix::Scheduling::Task.UploadAuditLogToSyslog:eventType:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acsAZO5RTQOG7NRDJH2ACQMTAIZT4"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.NONE,null),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3JAPYCNWTVA47MC7MBDZL2HTMA"),
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Scheduling::Task.UploadAuditLogToSyslog:user:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pru2BTBIESLYNGPZLEU2UYFAUATME"),
						"user",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTFGUFPZBSVCOJJR7D4LKEUY3PM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclJGXIKMD2CJB2FIX4VCYXAMH2K4"),
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
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Scheduling::Task.UploadAuditLogToSyslog:user:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsASF6FOCXNVAQZBSBLNENJRHMC4"),
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Scheduling::Task.UploadAuditLogToSyslog:station:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruZIDKNWHWYZGJ5GNBJS5IPJP6XM"),
						"station",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3P4424GC2BD3LI6EWOKLGFQCOQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclJGXIKMD2CJB2FIX4VCYXAMH2K4"),
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
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Scheduling::Task.UploadAuditLogToSyslog:station:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDWQQBSMTCFGNPO46COY3QIOIAA"),
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false)
			},
			new org.radixware.kernel.common.client.meta.RadCommandDef[]{
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::Scheduling::Task.UploadAuditLogToSyslog:SelectObjectCmd-Property Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdAEIZI65YBRHWVAHN3GDZDVPVO4"),
						"SelectObjectCmd",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclJGXIKMD2CJB2FIX4VCYXAMH2K4"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsX3ZO2IBA2FGO7NNHBIGTXRVY7I"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("imgCALZHDDAD5GYZKEGO4UFSNNLRA"),
						null,
						org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,
						true,
						false,
						false,
						org.radixware.kernel.common.enums.ECommandScope.PROPERTY,
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("pru2BTBIESLYNGPZLEU2UYFAUATME"),org.radixware.kernel.common.types.Id.Factory.loadFrom("pruZIDKNWHWYZGJ5GNBJS5IPJP6XM")},
						org.radixware.kernel.common.enums.ECommandAccessibility.ALWAYS)
			},
			null,
			null,
			null,
			new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprK3FEXBH46NHURJCLX57NMBFMQA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("eprUID3A6REAJCG3G2DONE3GU5KIQ")},
			true,true,false);
}

/* Radix::Scheduling::Task.UploadAuditLogToSyslog - Web Meta*/

/*Radix::Scheduling::Task.UploadAuditLogToSyslog-Application Class*/

package org.radixware.ads.Scheduling.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Task.UploadAuditLogToSyslog_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::Scheduling::Task.UploadAuditLogToSyslog:Presentations-Entity Object Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aclJGXIKMD2CJB2FIX4VCYXAMH2K4"),
			"Radix::Scheduling::Task.UploadAuditLogToSyslog",
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aclZDLN466RV5FE3BQIWSX56ZAETQ"),
			null,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPFOJCYK67FABXGAE5FKK6SPQQ4"),null,null,0,
			null,
			null,
			null,
			null,
			null,
			null,
			false,false,false);
}

/* Radix::Scheduling::Task.UploadAuditLogToSyslog:Edit - Desktop Meta*/

/*Radix::Scheduling::Task.UploadAuditLogToSyslog:Edit-Editor Presentation*/

package org.radixware.ads.Scheduling.explorer;
public final class Edit_mi{
	private static final class Edit_DEF extends org.radixware.kernel.common.client.meta.RadEditorPresentationDef{
		Edit_DEF(){
			super(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprK3FEXBH46NHURJCLX57NMBFMQA"),
			"Edit",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("eprUID3A6REAJCG3G2DONE3GU5KIQ"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aclJGXIKMD2CJB2FIX4VCYXAMH2K4"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("tblWZB7K4HLJPOBDCIUAALOMT5GDM"),
			null,
			null,
			null,
			null,

			/*Radix::Scheduling::Task.UploadAuditLogToSyslog:Edit:Children-Explorer Items*/
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
/* Radix::Scheduling::Task.UploadAuditLogToSyslog:Create - Desktop Meta*/

/*Radix::Scheduling::Task.UploadAuditLogToSyslog:Create-Editor Presentation*/

package org.radixware.ads.Scheduling.explorer;
public final class Create_mi{
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new org.radixware.kernel.common.client.meta.RadEditorPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprUID3A6REAJCG3G2DONE3GU5KIQ"),
	"Create",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
	org.radixware.kernel.common.types.Id.Factory.loadFrom("eprXGQ6C3VMQPOBDCKCAALOMT5GDM"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("aclJGXIKMD2CJB2FIX4VCYXAMH2K4"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("tblWZB7K4HLJPOBDCIUAALOMT5GDM"),
	null,
	null,

	/*Radix::Scheduling::Task.UploadAuditLogToSyslog:Create:Editor Pages-Editor Presentation Pages*/
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

			/*Radix::Scheduling::Task.UploadAuditLogToSyslog:Create:Additional-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epg4URFSGTVLZBOXDPTVDMPS7IHHE"),"Additional",org.radixware.kernel.common.types.Id.Factory.loadFrom("aclJGXIKMD2CJB2FIX4VCYXAMH2K4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsV7Q7KZEPSBBCVKSIZGVWOOCIPI"),null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruE334JEY2SZHZJGXNNYO3CXUF3Q"),0,0,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruMOC23GAGTBGM7NXCD6GH3YHMOU"),0,4,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("pru2BTBIESLYNGPZLEU2UYFAUATME"),0,2,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruZIDKNWHWYZGJ5GNBJS5IPJP6XM"),0,3,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruRX2OS4OZHRANJBR772W6UEB3TQ"),0,1,1,false,false)
			},null)
	},
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgHHRMHELUJTOBDCIVAALOMT5GDM")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epg4URFSGTVLZBOXDPTVDMPS7IHHE")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgL6FUZ36EMVEU3BPQ4FLI3TZ6KA"))}
	,

	/*Radix::Scheduling::Task.UploadAuditLogToSyslog:Create:Children-Explorer Items*/
		null
	,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
	null,
	0,
	null,
	(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
	36016,0,0);
}
/* Radix::Scheduling::Task.UploadAuditLogToSyslog:Create:Model - Desktop Executable*/

/*Radix::Scheduling::Task.UploadAuditLogToSyslog:Create:Model-Entity Model Class*/

package org.radixware.ads.Scheduling.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.UploadAuditLogToSyslog:Create:Model")
public class Create:Model  extends org.radixware.ads.Scheduling.explorer.Task.UploadAuditLogToSyslog.Task.UploadAuditLogToSyslog_DefaultModel.eprXGQ6C3VMQPOBDCKCAALOMT5GDM_ModelAdapter {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return Create:Model_mi.rdxMeta; }



	public Create:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadEditorPresentationDef def){super(userSession,def);}

	/*Radix::Scheduling::Task.UploadAuditLogToSyslog:Create:Model:Nested classes-Nested Classes*/

	/*Radix::Scheduling::Task.UploadAuditLogToSyslog:Create:Model:SelectObjectCmd-Common Dynamic Class*/


	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.UploadAuditLogToSyslog:Create:Model:SelectObjectCmd")
	public class SelectObjectCmd  extends org.radixware.kernel.common.client.models.items.Command  {
			public void send(org.radixware.kernel.common.types.Id propertyId) throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
				org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),propertyId,null);
				processResponse(response, propertyId);
			}



		/*Radix::Scheduling::Task.UploadAuditLogToSyslog:Create:Model:SelectObjectCmd:Nested classes-Nested Classes*/

		/*Radix::Scheduling::Task.UploadAuditLogToSyslog:Create:Model:SelectObjectCmd:Properties-Properties*/

		/*Radix::Scheduling::Task.UploadAuditLogToSyslog:Create:Model:SelectObjectCmd:Methods-Methods*/

		/*Radix::Scheduling::Task.UploadAuditLogToSyslog:Create:Model:SelectObjectCmd:SelectObjectCmd-User Method*/

		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.UploadAuditLogToSyslog:Create:Model:SelectObjectCmd:SelectObjectCmd")
		public  SelectObjectCmd (org.radixware.kernel.common.client.models.Model model, org.radixware.kernel.common.client.meta.RadCommandDef command) {
			super(model,command);
		}

		/*Radix::Scheduling::Task.UploadAuditLogToSyslog:Create:Model:SelectObjectCmd:execute-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.UploadAuditLogToSyslog:Create:Model:SelectObjectCmd:execute")
		public published  void execute (org.radixware.kernel.common.types.Id propertyId) {
			super.execute(propertyId);
			try {    
			    Explorer.Models::EntityModel model;
			    Types::Id namePropertyId;
			    if (propertyId == idof[Task.UploadAuditLogToSyslog:user]) {
			        model = userRef.selectEntityModel();
			        namePropertyId = idof[Acs::User:name];
			    } else {
			        model = stationRef.selectEntityModel();
			        namePropertyId = idof[Acs::Station:name];
			    }
			    Task.UploadAuditLogToSyslog:Create:Model.this.getProperty(propertyId).setValueObject(model.getProperty(namePropertyId).getValueObject());
			} catch (Exceptions::Exception e) {
			    getEnvironment().messageException("Exception", e.getMessage(), e);
			}
		}


	}

	/*Radix::Scheduling::Task.UploadAuditLogToSyslog:Create:Model:Properties-Properties*/

	/*Radix::Scheduling::Task.UploadAuditLogToSyslog:Create:Model:userRef-Presentation Property*/




	public class UserRef extends org.radixware.kernel.common.client.models.items.properties.PropertyRef{
		public UserRef(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadParentRefPropertyDef def){
			super(model,def);
		}
		@Override
		public 
		org.radixware.ads.Acs.explorer.User.User_DefaultModel openEntityModel()  throws org.radixware.kernel.common.exceptions.ServiceClientException, InterruptedException{
		     return (org.radixware.ads.Acs.explorer.User.User_DefaultModel)super.openEntityModel();
		}
		@Override
		public 
		org.radixware.ads.Acs.explorer.User.DefaultGroupModel openGroupModel(){
		     return (org.radixware.ads.Acs.explorer.User.DefaultGroupModel)super.openGroupModel();
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.client.types.Reference dummy = ((org.radixware.kernel.common.client.types.Reference)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.UploadAuditLogToSyslog:Create:Model:userRef")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.UploadAuditLogToSyslog:Create:Model:userRef")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public UserRef getUserRef(){return (UserRef)getProperty(prdQJG6HX7JPNG6LN4JI32GANCGGQ);}

	/*Radix::Scheduling::Task.UploadAuditLogToSyslog:Create:Model:user-Presentation Property*/




	public class User extends org.radixware.ads.Scheduling.explorer.Task.UploadAuditLogToSyslog.Task.UploadAuditLogToSyslog_DefaultModel.eprXGQ6C3VMQPOBDCKCAALOMT5GDM_ModelAdapter.pru2BTBIESLYNGPZLEU2UYFAUATME{
		public User(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.UploadAuditLogToSyslog:Create:Model:user")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.UploadAuditLogToSyslog:Create:Model:user")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public User getUser(){return (User)getProperty(pru2BTBIESLYNGPZLEU2UYFAUATME);}

	/*Radix::Scheduling::Task.UploadAuditLogToSyslog:Create:Model:stationRef-Presentation Property*/




	public class StationRef extends org.radixware.kernel.common.client.models.items.properties.PropertyRef{
		public StationRef(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadParentRefPropertyDef def){
			super(model,def);
		}
		@Override
		public 
		org.radixware.ads.Acs.explorer.Station.Station_DefaultModel openEntityModel()  throws org.radixware.kernel.common.exceptions.ServiceClientException, InterruptedException{
		     return (org.radixware.ads.Acs.explorer.Station.Station_DefaultModel)super.openEntityModel();
		}
		@Override
		public 
		org.radixware.ads.Acs.explorer.Station.DefaultGroupModel openGroupModel(){
		     return (org.radixware.ads.Acs.explorer.Station.DefaultGroupModel)super.openGroupModel();
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.client.types.Reference dummy = ((org.radixware.kernel.common.client.types.Reference)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.UploadAuditLogToSyslog:Create:Model:stationRef")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.UploadAuditLogToSyslog:Create:Model:stationRef")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public StationRef getStationRef(){return (StationRef)getProperty(prdUJFIQBS2XVG3TCVETSOFFNUHZA);}

	/*Radix::Scheduling::Task.UploadAuditLogToSyslog:Create:Model:station-Presentation Property*/




	public class Station extends org.radixware.ads.Scheduling.explorer.Task.UploadAuditLogToSyslog.Task.UploadAuditLogToSyslog_DefaultModel.eprXGQ6C3VMQPOBDCKCAALOMT5GDM_ModelAdapter.pruZIDKNWHWYZGJ5GNBJS5IPJP6XM{
		public Station(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.UploadAuditLogToSyslog:Create:Model:station")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.UploadAuditLogToSyslog:Create:Model:station")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Station getStation(){return (Station)getProperty(pruZIDKNWHWYZGJ5GNBJS5IPJP6XM);}














	/*Radix::Scheduling::Task.UploadAuditLogToSyslog:Create:Model:Methods-Methods*/












}

/* Radix::Scheduling::Task.UploadAuditLogToSyslog:Create:Model - Desktop Meta*/

/*Radix::Scheduling::Task.UploadAuditLogToSyslog:Create:Model-Entity Model Class*/

package org.radixware.ads.Scheduling.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Create:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aemUID3A6REAJCG3G2DONE3GU5KIQ"),
						"Create:Model",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aemXGQ6C3VMQPOBDCKCAALOMT5GDM"),
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::Scheduling::Task.UploadAuditLogToSyslog:Create:Model:Properties-Properties*/
						new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

								/*Radix::Scheduling::Task.UploadAuditLogToSyslog:Create:Model:userRef:userRef:PropertyPresentation-Parent Ref Property Presentation*/
								new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdQJG6HX7JPNG6LN4JI32GANCGGQ"),
									"userRef",
									null,
									null,
									org.radixware.kernel.common.types.Id.Factory.loadFrom("aclJGXIKMD2CJB2FIX4VCYXAMH2K4"),
									org.radixware.kernel.common.enums.EValType.PARENT_REF,
									org.radixware.kernel.common.enums.EPropNature.VIRTUAL,
									20,
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
									org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),
									org.radixware.kernel.common.types.Id.Factory.loadFrom("tblSY4KIOLTGLNRDHRZABQAQH3XQ4"),
									null,
									null,
									null,
									133693439,
									133693439,false),

								/*Radix::Scheduling::Task.UploadAuditLogToSyslog:Create:Model:stationRef:stationRef:PropertyPresentation-Parent Ref Property Presentation*/
								new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdUJFIQBS2XVG3TCVETSOFFNUHZA"),
									"stationRef",
									null,
									null,
									org.radixware.kernel.common.types.Id.Factory.loadFrom("aclJGXIKMD2CJB2FIX4VCYXAMH2K4"),
									org.radixware.kernel.common.enums.EValType.PARENT_REF,
									org.radixware.kernel.common.enums.EPropNature.VIRTUAL,
									20,
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
									org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFDFDUXBHJ3NRDJIRACQMTAIZT4"),
									org.radixware.kernel.common.types.Id.Factory.loadFrom("tblFDFDUXBHJ3NRDJIRACQMTAIZT4"),
									null,
									null,
									null,
									133693439,
									133693439,false)
						},
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

/* Radix::Scheduling::Task.UploadAuditLogToSyslog - Localizing Bundle */
package org.radixware.ads.Scheduling.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Task.UploadAuditLogToSyslog - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Any");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Любой");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3JAPYCNWTVA47MC7MBDZL2HTMA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Station");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Рабочая станция");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3P4424GC2BD3LI6EWOKLGFQCOQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Backward time shift (s)");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Сдвиг времени назад (c)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4R3WL62YSRANBBGKH6QC3OX73U"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Event type");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Тип события");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7HTAX7YRHRARXM7YURGHIK3H5M"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Any");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Любой");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsASF6FOCXNVAQZBSBLNENJRHMC4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Addresses delimiter is \";\". Default port is 514.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Разделитель адресов - \";\". Порт по умолчанию - 514.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCNAJC6KS2BGNTDWMHLHHEQVTHA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Any");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Любая");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDWQQBSMTCFGNPO46COY3QIOIAA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Syslog server addresses");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Адреса серверов Syslog");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKPW2SJDZGZGSLIRNLKWS5M5WS4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Upload Audit Log to Syslog");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Выгрузка журнала аудита в Syslog");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPFOJCYK67FABXGAE5FKK6SPQQ4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"User");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Пользователь");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTFGUFPZBSVCOJJR7D4LKEUY3PM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Uploaded period:  from (previous execution time - backward time shift) inclusively to  (current execution time - backward time shift) exclusively");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Выгружаемый период:  с (предыдущее время выполнения - сдвиг времени назад) включительно по  (текущее время выполнения - сдвиг времени назад) исключительно");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUTRVB6QPDFGDXC5GVHXECURRFA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Additional");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Дополнительно");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsV7Q7KZEPSBBCVKSIZGVWOOCIPI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Select Object");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Выбрать объект");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsX3ZO2IBA2FGO7NNHBIGTXRVY7I"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(Task.UploadAuditLogToSyslog - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbaclJGXIKMD2CJB2FIX4VCYXAMH2K4"),"Task.UploadAuditLogToSyslog - Localizing Bundle",$$$items$$$);
}
