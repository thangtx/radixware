
/* Radix::Scheduling::SyslogUploader - Server Executable*/

/*Radix::Scheduling::SyslogUploader-Server Dynamic Class*/

package org.radixware.ads.Scheduling.server;

import java.net.InetAddress;
import java.text.MessageFormat;
import java.util.List;
import java.util.ArrayList;
import org.apache.log4j.net.SyslogAppender;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::SyslogUploader")
public class SyslogUploader  implements org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {


	/**Metainformation accessor method*/
	@SuppressWarnings("unused")
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return SyslogUploader_mi.rdxMeta;}

	/*Radix::Scheduling::SyslogUploader:Nested classes-Nested Classes*/

	/*Radix::Scheduling::SyslogUploader:Properties-Properties*/

	/*Radix::Scheduling::SyslogUploader:appenders-Dynamic Property*/



	protected org.apache.log4j.net.SyslogAppender[] appenders=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::SyslogUploader:appenders")
	public  org.apache.log4j.net.SyslogAppender[] getAppenders() {
		return appenders;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::SyslogUploader:appenders")
	public   void setAppenders(org.apache.log4j.net.SyslogAppender[] val) {
		appenders = val;
	}

	/*Radix::Scheduling::SyslogUploader:availAddresses-Dynamic Property*/



	protected java.util.List<Str> availAddresses=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::SyslogUploader:availAddresses")
	public  java.util.List<Str> getAvailAddresses() {
		return availAddresses;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::SyslogUploader:availAddresses")
	public   void setAvailAddresses(java.util.List<Str> val) {
		availAddresses = val;
	}

	/*Radix::Scheduling::SyslogUploader:cachingSectionId-Dynamic Property*/



	protected Int cachingSectionId=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::SyslogUploader:cachingSectionId")
	public  Int getCachingSectionId() {
		return cachingSectionId;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::SyslogUploader:cachingSectionId")
	public   void setCachingSectionId(Int val) {
		cachingSectionId = val;
	}

	/*Radix::Scheduling::SyslogUploader:uploadConditionFalseCount-Dynamic Property*/



	protected int uploadConditionFalseCount=0;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::SyslogUploader:uploadConditionFalseCount")
	public  int getUploadConditionFalseCount() {
		return uploadConditionFalseCount;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::SyslogUploader:uploadConditionFalseCount")
	public   void setUploadConditionFalseCount(int val) {
		uploadConditionFalseCount = val;
	}

	/*Radix::Scheduling::SyslogUploader:uploadConditionTrueCount-Dynamic Property*/



	protected int uploadConditionTrueCount=0;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::SyslogUploader:uploadConditionTrueCount")
	public  int getUploadConditionTrueCount() {
		return uploadConditionTrueCount;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::SyslogUploader:uploadConditionTrueCount")
	public   void setUploadConditionTrueCount(int val) {
		uploadConditionTrueCount = val;
	}

	/*Radix::Scheduling::SyslogUploader:uploadedCount-Dynamic Property*/



	protected int uploadedCount=0;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::SyslogUploader:uploadedCount")
	public  int getUploadedCount() {
		return uploadedCount;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::SyslogUploader:uploadedCount")
	public   void setUploadedCount(int val) {
		uploadedCount = val;
	}

































































	/*Radix::Scheduling::SyslogUploader:Methods-Methods*/

	/*Radix::Scheduling::SyslogUploader:SyslogUploader-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::SyslogUploader:SyslogUploader")
	public  SyslogUploader (Str addresses) {
		final String[] allAddresses = addresses.split(";");
		final List<String> availAddresses = new ArrayList<>(allAddresses.length);
		for (String address: allAddresses) {
		    try {
		        address = address.trim();
		        InetAddress.getByName(address);
		        availAddresses.add(address);
		    } catch (Exceptions::UnknownHostException e) {
		        Arte::Trace.warning(MessageFormat.format("Address \"{0}\" does not exists or it is not available", address), Arte::EventSource:Task);
		    }
		}
		if (availAddresses.isEmpty()) {
		    throw new AppError("All addresses do not exist or are not available");
		}
		availAddresses = availAddresses;
	}

	/*Radix::Scheduling::SyslogUploader:canUpload-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::SyslogUploader:canUpload")
	public  boolean canUpload (Bool uploadConditionResult) {
		if (uploadConditionResult != null) {
		    if (uploadConditionResult == false) {
		        uploadConditionFalseCount++;
		        return false;
		    } else {
		        uploadConditionTrueCount++;
		    }
		}
		return true;
	}

	/*Radix::Scheduling::SyslogUploader:enterCachingSection-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::SyslogUploader:enterCachingSection")
	public  void enterCachingSection () {
		cachingSectionId = Arte::Arte.enterCachingSession();
	}

	/*Radix::Scheduling::SyslogUploader:leaveCachingSection-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::SyslogUploader:leaveCachingSection")
	public  void leaveCachingSection () {
		Arte::Arte.leaveCachingSession(cachingSectionId);
	}

	/*Radix::Scheduling::SyslogUploader:normalizeMessage-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::SyslogUploader:normalizeMessage")
	public  Str normalizeMessage (Str message) {
		message = Utils::Nvl.get(message, "");
		message = message.replace("\n", "\\n");
		message = message.replace("\r", "\\r");
		return message;
	}

	/*Radix::Scheduling::SyslogUploader:start-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::SyslogUploader:start")
	public  void start () {
		final org.apache.log4j.PatternLayout layout = new org.apache.log4j.PatternLayout(
		            "%d{ISO8601} %-5p %m%n" //see http://logging.apache.org/log4j/1.2/apidocs/org/apache/log4j/PatternLayout.html
		            /*org.apache.log4j.PatternLayout.DEFAULT_CONVERSION_PATTERN*/);
		appenders = new SyslogAppender[availAddresses.size()];

		for (int i = 0; i < appenders.length; ++i) {
		    appenders[i] = new SyslogAppender();
		    appenders[i].setSyslogHost(availAddresses.get(i));
		    appenders[i].setLayout(layout);
		}

	}

	/*Radix::Scheduling::SyslogUploader:upload-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::SyslogUploader:upload")
	public  void upload (long timeMillis, org.apache.log4j.Level severity, Str message) {
		for (SyslogAppender appender: appenders) {
		    appender.append(
		        new org.apache.log4j.spi.LoggingEvent(
		            RadMeta.Name,
		            org.apache.log4j.Logger.RootLogger,
		            timeMillis,
		            severity,
		            message,
		            null));
		}
		uploadedCount++;
	}

	/*Radix::Scheduling::SyslogUploader:finish-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::SyslogUploader:finish")
	public  void finish () {
		if (appenders != null) {
		    for (SyslogAppender appender: appenders) {
		        if (appender != null) {
		            appender.close();
		        }
		    }
		}

	}

	/*Radix::Scheduling::SyslogUploader:subtractSeconds-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::SyslogUploader:subtractSeconds")
	public  java.sql.Timestamp subtractSeconds (java.sql.Timestamp time, Int seconds) {
		if (seconds == null || seconds == 0) {
		    return time;
		}
		return new DateTime(time.Time - seconds.longValue() * 1000);

	}


}

/* Radix::Scheduling::SyslogUploader - Server Meta*/

/*Radix::Scheduling::SyslogUploader-Server Dynamic Class*/

package org.radixware.ads.Scheduling.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class SyslogUploader_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("adcKU7JP6VZMJC4ZH5TX4FHKKZHDI"),"SyslogUploader",null,

						org.radixware.kernel.common.enums.EClassType.DYNAMIC,
						null,
						null,
						null,

						/*Radix::Scheduling::SyslogUploader:Properties-Properties*/
						new org.radixware.kernel.server.meta.clazzes.RadPropDef[]{

								/*Radix::Scheduling::SyslogUploader:appenders-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdKBUD7PDMRJAPNNDZ4SAQHTU3FQ"),"appenders",null,org.radixware.kernel.common.enums.EValType.JAVA_CLASS,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::SyslogUploader:availAddresses-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdELX3QJFK5REH7FSWKNEXGL4QEM"),"availAddresses",null,org.radixware.kernel.common.enums.EValType.JAVA_CLASS,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::SyslogUploader:cachingSectionId-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd27XNCHAZSBBPVI3ZUXACUBGHKI"),"cachingSectionId",null,org.radixware.kernel.common.enums.EValType.INT,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::SyslogUploader:uploadConditionFalseCount-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdQA26JQZPENDFNPWNKLUMJKKAPE"),"uploadConditionFalseCount",null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::SyslogUploader:uploadConditionTrueCount-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd7ZTCEKZ4SRA6FGEAVSUZF7YPDQ"),"uploadConditionTrueCount",null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::SyslogUploader:uploadedCount-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd2LU3AB7GVVEAZNFFTXEUBURE2Y"),"uploadedCount",null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE)
						},

						/*Radix::Scheduling::SyslogUploader:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthODABPR7GRRGRXD24AH4RT66KHQ"),"SyslogUploader",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("addresses",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprEMCXF3HF7BGPFPSSDKD3232C3U"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthOK3L64UHQFDR5FHGUSM4WRM2HA"),"canUpload",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("uploadConditionResult",org.radixware.kernel.common.enums.EValType.BOOL,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprGZ54OU3OV5DQBE6MNGVXATA6CQ"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthEPCR7FESI5BFPBYDOSRCV7K22I"),"enterCachingSection",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthDZJYOLDKCFEUDDS4QSZHW2QFXY"),"leaveCachingSection",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthUVFWJWUCINE3VHP67FRZBWFR4M"),"normalizeMessage",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("message",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr5RTNC4KC5VGIFJL3JPSDX63IBA"))
								},org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthD6R44PD7ZJEKDFH5CMXZ2Z3D7M"),"start",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthDF4NAEQAYNGY5MAKU7IAHWWQ3U"),"upload",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("timeMillis",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprF4YS4VFLKJEXDGW4NKEXM2KWAU")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("severity",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprRELFWRFR2ZHU7NZUIGHCASPXPE")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("message",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprYFUSHFHXU5CHBCPFBPNAQBKJAQ"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthG2YIZDGGVZA3TKH3XUFCEWYUII"),"finish",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthO7HVQWUA6ZC6ZPNJWHLHRXLEAU"),"subtractSeconds",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("time",org.radixware.kernel.common.enums.EValType.DATE_TIME,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprOPS52H33BNCJ5OYHTIGW4KOBR4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("seconds",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprJY4VG7OQBZB5PPQASVZWNZD3LU"))
								},org.radixware.kernel.common.enums.EValType.DATE_TIME)
						},
						null,
						null,null,null,false);
}

/* Radix::Scheduling::SyslogUploader - Localizing Bundle */
package org.radixware.ads.Scheduling.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class SyslogUploader - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Address \"{0}\" does not exists or it is not available");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Адрес \"{0}\" не существует или недоступен");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls64N7R7AXMFEG5KQ5B3A56BV5FU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"All addresses do not exist or are not available");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Все адреса не существуют или недоступны");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUR73RC2PTVGHHEBWY3CZLX3U5I"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(SyslogUploader - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbadcKU7JP6VZMJC4ZH5TX4FHKKZHDI"),"SyslogUploader - Localizing Bundle",$$$items$$$);
}
