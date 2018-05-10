
/* Radix::Utils::ArteWaitStatsUtils - Server Executable*/

/*Radix::Utils::ArteWaitStatsUtils-Server Dynamic Class*/

package org.radixware.ads.Utils.server;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Utils::ArteWaitStatsUtils")
public published class ArteWaitStatsUtils  implements org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {


	/**Metainformation accessor method*/
	@SuppressWarnings("unused")
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return ArteWaitStatsUtils_mi.rdxMeta;}

	/*Radix::Utils::ArteWaitStatsUtils:Nested classes-Nested Classes*/

	/*Radix::Utils::ArteWaitStatsUtils:Properties-Properties*/

	/*Radix::Utils::ArteWaitStatsUtils:TO_MILLIS-Dynamic Property*/



	protected static long TO_MILLIS=1000000l;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Utils::ArteWaitStatsUtils:TO_MILLIS")
	private static final  long getTO_MILLIS() {
		return TO_MILLIS;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Utils::ArteWaitStatsUtils:TO_MILLIS")
	private static final   void setTO_MILLIS(long val) {
		TO_MILLIS = val;
	}



































	/*Radix::Utils::ArteWaitStatsUtils:Methods-Methods*/

	/*Radix::Utils::ArteWaitStatsUtils:toStr-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Utils::ArteWaitStatsUtils:toStr")
	public static published  Str toStr (long cpuNs, long dbNs, long extNs, long actNs) {
		final long cpuMs = cpuNs / TO_MILLIS;
		final long dbMs = dbNs / TO_MILLIS;
		final long extMs = extNs / TO_MILLIS;

		final java.lang.StringBuilder sb = new java.lang.StringBuilder();
		sb.append("CPU = ").append(cpuMs).append(" ms, ")
		        .append("DB = ").append(dbMs).append(" ms, ")
		        .append("EXT = ").append(extMs).append(" ms");

		final long totalMs;
		if (Arte::Arte.getInstance().getInstance().isUseActiveArteLimits()) {
		    final long actMs = actNs / TO_MILLIS;
		    sb.append(", ACT = ").append(actMs).append(" ms");
		    totalMs = cpuMs + dbMs + extMs + actMs;
		} else {
		    totalMs = cpuMs + dbMs + extMs;
		}

		sb.append(" (Total ").append(totalMs).append(" ms)");
		return sb.toString();
	}

	/*Radix::Utils::ArteWaitStatsUtils:toStr-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Utils::ArteWaitStatsUtils:toStr")
	public static published  Str toStr (org.radixware.kernel.server.monitoring.ArteWaitStats before, org.radixware.kernel.server.monitoring.ArteWaitStats after) {
		final org.radixware.kernel.server.monitoring.ArteWaitStats res = before.substractFrom(after);
		return toStr(res.CpuNanos, res.DbNanos, res.ExtNanos, res.QueueNanos);
	}

	/*Radix::Utils::ArteWaitStatsUtils:toStr-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Utils::ArteWaitStatsUtils:toStr")
	public static published  Str toStr (org.radixware.kernel.server.monitoring.ArteWaitStats before) {
		final org.radixware.kernel.server.monitoring.ArteWaitStats after = Arte::Arte.getInstance().getProfiler().WaitStatsSnapshot;
		return toStr(before, after);
	}


}

/* Radix::Utils::ArteWaitStatsUtils - Server Meta*/

/*Radix::Utils::ArteWaitStatsUtils-Server Dynamic Class*/

package org.radixware.ads.Utils.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class ArteWaitStatsUtils_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("adcMB3FLTCKPFADFCSCUWX32RVPFY"),"ArteWaitStatsUtils",null,

						org.radixware.kernel.common.enums.EClassType.DYNAMIC,
						null,
						null,
						null,

						/*Radix::Utils::ArteWaitStatsUtils:Properties-Properties*/
						new org.radixware.kernel.server.meta.clazzes.RadPropDef[]{

								/*Radix::Utils::ArteWaitStatsUtils:TO_MILLIS-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdSTQV2V5IIRGJ7IPJA7X4YOFLLY"),"TO_MILLIS",null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE)
						},

						/*Radix::Utils::ArteWaitStatsUtils:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthY47WTUD7YFF3VNPIO735JEWOVM"),"toStr",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("cpuNs",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprWFAU43AC2RC5VPKOA23F2ERATI")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("dbNs",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprLVZKOZOWHBGZZBCTVDIA274BZM")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("extNs",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprFLMA4ZDZONEHXNYL2U5LJQSILQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("actNs",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprNWDSCZV6BJB33H6MZPASM6EEZM"))
								},org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthNIDMSFG3VJBMZESWAFSWU744RI"),"toStr",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("before",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprBEIXFQF4OBBIVHC44YC3SWO5XU")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("after",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprXYXHTFHNLFDRRB7WAHTYXTXQVM"))
								},org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthB6JGX5HIZFB6LGS45VVQWHMPP4"),"toStr",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("before",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprBEIXFQF4OBBIVHC44YC3SWO5XU"))
								},org.radixware.kernel.common.enums.EValType.STR)
						},
						null,
						null,null,null,false);
}
