
/* Radix::SystemMonitor::Task.SysMon.MetricCalculate - Server Executable*/

/*Radix::SystemMonitor::Task.SysMon.MetricCalculate-Application Class*/

package org.radixware.ads.SystemMonitor.server;

import org.radixware.kernel.server.monitoring.IStatValue;
import org.radixware.kernel.server.monitoring.IEventValue;
import org.radixware.kernel.server.exceptions.FilterParamNotDefinedException;
import java.util.HashMap;
import java.util.Map;
import org.radixware.kernel.server.types.QueryTranslateResult;
import org.radixware.kernel.server.monitoring.SensorCoordinates;
import org.radixware.kernel.server.monitoring.EMetricType;
import org.radixware.kernel.server.monitoring.MetricDescription;
import org.radixware.kernel.server.monitoring.MetricParameters;
import org.radixware.kernel.server.monitoring.MetricRecord;
import org.radixware.kernel.server.monitoring.MetricRecordWriter;
import org.radixware.kernel.server.monitoring.MonitoringDbQueries;
import org.radixware.kernel.server.dbq.SqmlTranslator;


@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::Task.SysMon.MetricCalculate")
public published class Task.SysMon.MetricCalculate  extends org.radixware.ads.Scheduling.server.Task.Atomic  implements org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {


	/**Metainformation accessor method*/
	@SuppressWarnings("unused")
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return Task.SysMon.MetricCalculate_mi.rdxMeta;}

	/*Radix::SystemMonitor::Task.SysMon.MetricCalculate:Nested classes-Nested Classes*/

	/*Radix::SystemMonitor::Task.SysMon.MetricCalculate:Properties-Properties*/





























	/*Radix::SystemMonitor::Task.SysMon.MetricCalculate:Methods-Methods*/

	/*Radix::SystemMonitor::Task.SysMon.MetricCalculate:execute-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::Task.SysMon.MetricCalculate:execute")
	public published  void execute (java.sql.Timestamp prevExecTime, java.sql.Timestamp curExecTime) {
		final MetricsToCalculateCursor metrics = MetricsToCalculateCursor.open(curExecTime);
		try {
		    final MonitoringDbQueries queries = new MonitoringDbQueries(Arte::Arte.getDbConnection(), Arte::Arte.getInstance().Trace, null);
		    try {
		        final MetricRecordWriter writer = new MetricRecordWriter(Arte::Arte.getDbConnection(), queries, Arte::Arte.createLocalTracer(Arte::EventSource:SystemMonitoring), MetricRecordWriter.ECacheMode.OFF, MetricRecordWriter.ECommitPolicy.NO_COMMIT);
		        try {
		            while (metrics.next()) {
		                try {
		                    final MetricParameters parameters;
		                    final MetricRecord record;
		                    final java.util.List<MetricValue> vals = MetricCalculateUtils.calculate(metrics.id, metrics.lastCalculationTime == null ? curExecTime : metrics.lastCalculationTime, curExecTime, null);
		                    if (vals == null || vals.isEmpty() || vals.get(0) == null) {
		                        Arte::Trace.put(eventCode["Calculation procedure for metric '%1' returned null"], String.valueOf(metrics.id) + ") " + metrics.title);
		                        continue;
		                    }
		                    MetricValue metricValue = vals.get(0);
		                    if (metricValue instanceof IStatValue) {
		                        parameters = new MetricParameters(new MetricDescription(metrics.kind, EMetricType.STATISTIC, SensorCoordinates.none()), metrics.id.longValue(), -1);
		                        record = new MetricRecord(parameters, (IStatValue) metricValue);
		                    } else {
		                        parameters = new MetricParameters(new MetricDescription(metrics.kind, EMetricType.EVENT, SensorCoordinates.none()), metrics.id.longValue(), -1);
		                        record = new MetricRecord(parameters, (IEventValue) metricValue);
		                    }
		                    writer.add(record);
		                    writer.flush();
		                } catch (Exceptions::Exception ex) {
		                    Arte::Trace.error("Metric #" + metrics.id.toString() + " calculation error: " + Arte::Trace.exceptionStackToString(ex), Arte::EventSource:SystemMonitoring);
		                }
		            }
		        } finally {
		            writer.setConnection(null);
		        }
		    } finally {
		        queries.setConnection(null);
		    }
		} finally {
		    metrics.close();
		}

	}

	/*Radix::SystemMonitor::Task.SysMon.MetricCalculate:calcUserQuery-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::Task.SysMon.MetricCalculate:calcUserQuery")
	public  java.lang.Double calcUserQuery (java.sql.Clob userQuery, Int metricTypeId, java.sql.Timestamp lastControlTime, java.sql.Timestamp curControlTime) {
		final org.radixware.kernel.server.sqml.Sqml sqml;
		try {
		    org.radixware.schemas.xscml.SqmlDocument sqmlDoc = org.radixware.schemas.xscml.SqmlDocument.Factory.parse(userQuery.getCharacterStream());
		    sqml = org.radixware.kernel.server.sqml.Sqml.Factory.loadFrom("", sqmlDoc.Sqml);
		} catch (Exception ex) {
		    Arte::Trace.put(eventCode["Error parsing user-defined SQML: %1"], new ArrStr(Arte::Trace.exceptionStackToString(ex)));
		    return null;   
		}
		QueryTranslateResult result = SqmlTranslator.translateSingleQuery(Arte::Arte.getInstance(), sqml, this.getEntityDefinitionId());
		final Map<Types::Id, Object> paramValues = new HashMap<Types::Id, Object>();
		paramValues.put(Types::Id.Factory.loadFrom(UserQueryParameterId:END_TIME.Value), curControlTime);
		paramValues.put(Types::Id.Factory.loadFrom(UserQueryParameterId:METRIC_ID.Value), metricTypeId);
		paramValues.put(Types::Id.Factory.loadFrom(UserQueryParameterId:BEG_TIME.Value), lastControlTime);
		try {
		    final java.sql.PreparedStatement statement = result.prepare(Arte::Arte.getInstance(), paramValues);
		    try {
		        java.sql.ResultSet rs = statement.executeQuery();
		        if (rs.next()) {
		            final Double d = rs.getDouble(1);
		            return d;
		        }
		    } finally {
		        statement.close();
		    }
		} catch (Exceptions::SQLException exception) {
		    Arte::Trace.error(Arte::Trace.exceptionStackToString(exception), Arte::EventSource:SystemMonitoring);
		} catch (FilterParamNotDefinedException exception) {
		    Arte::Trace.error(Arte::Trace.exceptionStackToString(exception), Arte::EventSource:SystemMonitoring);
		}
		return null;
	}

	/*Radix::SystemMonitor::Task.SysMon.MetricCalculate:calcAppMetric-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::Task.SysMon.MetricCalculate:calcAppMetric")
	public  org.radixware.kernel.server.monitoring.MetricValue calcAppMetric (Int metricTypeId, java.sql.Timestamp lastControlTime, java.sql.Timestamp curControlTime) {
		MetricType metricType = MetricType.loadByPK(metricTypeId, true);
		if(metricType instanceof MetricType.AppMetric.Event) {
		    MetricType.AppMetric.Event eventMetricType = (MetricType.AppMetric.Event) metricType;
		    return eventMetricType.calcValue(lastControlTime, curControlTime);
		} else {
		    MetricType.AppMetric.Stat statMetricType = (MetricType.AppMetric.Stat) metricType;
		    return statMetricType.calcValue(lastControlTime, curControlTime);
		}
	}

	/*Radix::SystemMonitor::Task.SysMon.MetricCalculate:isSingletone-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::Task.SysMon.MetricCalculate:isSingletone")
	public published  boolean isSingletone () {
		return true;
	}


	@Override
	public org.radixware.kernel.server.types.FormHandler.NextDialogsRequest execCommand(org.radixware.kernel.common.types.Id cmdId, org.radixware.kernel.common.types.Id propId, org.apache.xmlbeans.XmlObject input, org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValsById, org.apache.xmlbeans.XmlObject output) throws org.radixware.kernel.common.exceptions.AppException,java.lang.InterruptedException{

			return super.execCommand(cmdId,propId,input,newPropValsById,output);
	}


}

/* Radix::SystemMonitor::Task.SysMon.MetricCalculate - Server Meta*/

/*Radix::SystemMonitor::Task.SysMon.MetricCalculate-Application Class*/

package org.radixware.ads.SystemMonitor.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Task.SysMon.MetricCalculate_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("aclHOZS3MGIAFAATAS5OIYFAC5NYY"),"Task.SysMon.MetricCalculate",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWKOZB4XYMZDMZHNPNHY5OTYJM4"),

						org.radixware.kernel.common.enums.EClassType.APPLICATION,

						/*Radix::SystemMonitor::Task.SysMon.MetricCalculate:Presentations-Entity Object Presentations*/
						/*Class presentation attributes*/
						 new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef(/*Owner Class Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("aclHOZS3MGIAFAATAS5OIYFAC5NYY"),
							/*Owner Class Name*/
							"Task.SysMon.MetricCalculate",
							/*Title Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWKOZB4XYMZDMZHNPNHY5OTYJM4"),
							/*Property presentations*/

							/*Radix::SystemMonitor::Task.SysMon.MetricCalculate:Properties-Properties*/
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
									/*Radix::SystemMonitor::Task.SysMon.MetricCalculate:Default-Dynamic Class Catalog*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eccVXWRKQLYJTOBDCIVAALOMT5GDM"),org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.ELinkMode.VIRTUAL,new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.Item[]{
										new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.ClassRef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aclHOZS3MGIAFAATAS5OIYFAC5NYY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("cctTU4RQWDDDZHDXKTRWIAPN7O6P4"),150.0,true,org.radixware.kernel.common.types.Id.Factory.loadFrom("aclHOZS3MGIAFAATAS5OIYFAC5NYY"),null)}
									)
							},
							/*Restrictions*/
							null,null,0),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblWZB7K4HLJPOBDCIUAALOMT5GDM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclZDLN466RV5FE3BQIWSX56ZAETQ"),

						/*Radix::SystemMonitor::Task.SysMon.MetricCalculate:Properties-Properties*/
						null,

						/*Radix::SystemMonitor::Task.SysMon.MetricCalculate:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthN36NOGMSW5EGZIX2JAVCBWM2RU"),"execute",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("prevExecTime",org.radixware.kernel.common.enums.EValType.DATE_TIME,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr5T2TTRSZ3VG6NE3C5ZK7PAGXK4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("curExecTime",org.radixware.kernel.common.enums.EValType.DATE_TIME,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr4MDSU3P7QJCJNCWT3NCWCQD3OM"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthSFU7SOJ4XVC5TCTL2ZH6UQYVGU"),"calcUserQuery",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("userQuery",org.radixware.kernel.common.enums.EValType.CLOB,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprIG5Z6FR6W5CVTHWGIK4UHJCKEU")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("metricTypeId",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr2J57WMU2S5HKJDM77MAVILN5EI")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("lastControlTime",org.radixware.kernel.common.enums.EValType.DATE_TIME,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprEAXLERGKBJHZBOY4H4HHP4X7CI")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("curControlTime",org.radixware.kernel.common.enums.EValType.DATE_TIME,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr2RTWWXTPXZGCPD6Q5XM5NBHWLA"))
								},org.radixware.kernel.common.enums.EValType.JAVA_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthEWYN3DTWXFAMPMZOY6XJT43ARM"),"calcAppMetric",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("metricTypeId",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprU2WWZ6L2OJBUTALUPJL6CUJX7Q")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("lastControlTime",org.radixware.kernel.common.enums.EValType.DATE_TIME,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprW4YKAHBLNRDTBDQITIXXGHFB4E")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("curControlTime",org.radixware.kernel.common.enums.EValType.DATE_TIME,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprGLK36GULCFFVNEJUGFTGJ4RYZQ"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthKIF62UYHLRDRPPGLIIMJRLXSUI"),"isSingletone",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE)
						},
						null,
						null,null,null,false);
}

/* Radix::SystemMonitor::Task.SysMon.MetricCalculate - Desktop Executable*/

/*Radix::SystemMonitor::Task.SysMon.MetricCalculate-Application Class*/

package org.radixware.ads.SystemMonitor.explorer;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::Task.SysMon.MetricCalculate")
public interface Task.SysMon.MetricCalculate   extends org.radixware.ads.Scheduling.explorer.Task.Atomic  {
























}

/* Radix::SystemMonitor::Task.SysMon.MetricCalculate - Desktop Meta*/

/*Radix::SystemMonitor::Task.SysMon.MetricCalculate-Application Class*/

package org.radixware.ads.SystemMonitor.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Task.SysMon.MetricCalculate_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::SystemMonitor::Task.SysMon.MetricCalculate:Presentations-Entity Object Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aclHOZS3MGIAFAATAS5OIYFAC5NYY"),
			"Radix::SystemMonitor::Task.SysMon.MetricCalculate",
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aclZDLN466RV5FE3BQIWSX56ZAETQ"),
			null,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWKOZB4XYMZDMZHNPNHY5OTYJM4"),null,null,0,

			/*Radix::SystemMonitor::Task.SysMon.MetricCalculate:Properties-Properties*/
			null,
			null,
			null,
			null,
			null,
			null,
			true,true,false);
}

/* Radix::SystemMonitor::Task.SysMon.MetricCalculate - Web Meta*/

/*Radix::SystemMonitor::Task.SysMon.MetricCalculate-Application Class*/

package org.radixware.ads.SystemMonitor.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Task.SysMon.MetricCalculate_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::SystemMonitor::Task.SysMon.MetricCalculate:Presentations-Entity Object Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aclHOZS3MGIAFAATAS5OIYFAC5NYY"),
			"Radix::SystemMonitor::Task.SysMon.MetricCalculate",
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aclZDLN466RV5FE3BQIWSX56ZAETQ"),
			null,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWKOZB4XYMZDMZHNPNHY5OTYJM4"),null,null,0,
			null,
			null,
			null,
			null,
			null,
			null,
			false,false,false);
}

/* Radix::SystemMonitor::Task.SysMon.MetricCalculate - Localizing Bundle */
package org.radixware.ads.SystemMonitor.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Task.SysMon.MetricCalculate - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Error parsing user-defined SQML: %1");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ошибка при разборе пользовательского SQML: %1");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDGWZKA377JASRDRFZDILITKN6A"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.EVENT_CODE,org.radixware.kernel.common.enums.EEventSeverity.ERROR,"App.SystemMonitoring",null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Calculation procedure for metric \'%1\' returned null");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Процедура вычисления метрики \'%1\' вернула null");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPEKCVPDL6RBM7OGPICANNCT2II"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.EVENT_CODE,org.radixware.kernel.common.enums.EEventSeverity.WARNING,"App.SystemMonitoring",java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Application and User-Defined Metrics Calculation");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Вычисление прикладных и пользовательских метрик");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWKOZB4XYMZDMZHNPNHY5OTYJM4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(Task.SysMon.MetricCalculate - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbaclHOZS3MGIAFAATAS5OIYFAC5NYY"),"Task.SysMon.MetricCalculate - Localizing Bundle",$$$items$$$);
}
