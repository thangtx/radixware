
/* Radix::Arte::Trace - Server Executable*/

/*Radix::Arte::Trace-Server Dynamic Class*/

package org.radixware.ads.Arte.server;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::Trace")
public published class Trace  implements org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {

	private static final org.radixware.kernel.server.arte.Trace $trace$ = Arte.getInstance().getTrace();
	/**Metainformation accessor method*/
	@SuppressWarnings("unused")
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return Trace_mi.rdxMeta;}

	/*Radix::Arte::Trace:Nested classes-Nested Classes*/

	/*Radix::Arte::Trace:Properties-Properties*/





























	/*Radix::Arte::Trace:Methods-Methods*/

	/*Radix::Arte::Trace:enterContext-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::Trace:enterContext")
	public static published  void enterContext (org.radixware.ads.Arte.common.EventContextType type, Int id) {
		$trace$.enterContext(type.getValue(), id);
	}

	/*Radix::Arte::Trace:getMinSeverity-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::Trace:getMinSeverity")
	public static published  long getMinSeverity () {
		return $trace$.getMinSeverity();
	}

	/*Radix::Arte::Trace:put-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::Trace:put")
	public static published  void put (Str code, Str word1, Str word2) {
		$trace$.put(code, new ArrStr(word1, word2));
	}

	/*Radix::Arte::Trace:enterContext-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::Trace:enterContext")
	public static published  void enterContext (org.radixware.ads.Arte.common.EventContextType type, Str id) {
		$trace$.enterContext(type.getValue(), id);
	}

	/*Radix::Arte::Trace:leaveContext-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::Trace:leaveContext")
	public static published  void leaveContext (org.radixware.ads.Arte.common.EventContextType type, Str id) {
		$trace$.leaveContext(type.getValue(), id);
	}

	/*Radix::Arte::Trace:leaveContext-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::Trace:leaveContext")
	public static published  void leaveContext (org.radixware.ads.Arte.common.EventContextType type, Int id) {
		$trace$.leaveContext(type.getValue(), id);
	}

	/*Radix::Arte::Trace:flush-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::Trace:flush")
	public static published  void flush () {
		$trace$.flush();
	}

	/*Radix::Arte::Trace:error-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::Trace:error")
	public static published  void error (Str mess, org.radixware.ads.Arte.common.EventSource eventSource) {
		$trace$.put(EventSeverity:Error, mess, eventSource.getValue() );
	}

	/*Radix::Arte::Trace:isMinSeverityDebug-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::Trace:isMinSeverityDebug")
	public static published  boolean isMinSeverityDebug () {
		return $trace$.getMinSeverity()<=EventSeverity:Debug.getValue().longValue();
	}

	/*Radix::Arte::Trace:delContextProfile-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::Trace:delContextProfile")
	public static published  void delContextProfile (java.lang.Object contextHandler) {
		$trace$.delTarget(contextHandler);
	}

	/*Radix::Arte::Trace:exceptionStackToString-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::Trace:exceptionStackToString")
	public static published  Str exceptionStackToString (java.lang.Throwable e) {
		return Utils::ExceptionTextFormatter.exceptionStackToString(e);
	}

	/*Radix::Arte::Trace:warning-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::Trace:warning")
	public static published  void warning (Str mess, org.radixware.ads.Arte.common.EventSource eventSource) {
		$trace$.put(EventSeverity:Warning , 
		          mess, 
		          eventSource.getValue() );
	}

	/*Radix::Arte::Trace:put-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::Trace:put")
	public static published  void put (Str code) {
		$trace$.put(code, new ArrStr());
	}

	/*Radix::Arte::Trace:put-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::Trace:put")
	public static published  void put (Str code, Str word1, Str word2, Str word3) {
		$trace$.put(code, new ArrStr(word1, word2, word3));
	}

	/*Radix::Arte::Trace:event-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::Trace:event")
	public static published  void event (Str mess, org.radixware.ads.Arte.common.EventSource eventSource) {
		$trace$.put(EventSeverity:Event,
		          mess, 
		          eventSource.getValue() );
	}

	/*Radix::Arte::Trace:addContextProfile-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::Trace:addContextProfile")
	@Deprecated
	public static published  java.lang.Object addContextProfile (Str extraProfile) {
		return $trace$.addTargetLog(extraProfile);
	}

	/*Radix::Arte::Trace:debug-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::Trace:debug")
	public static published  void debug (Str mess, org.radixware.ads.Arte.common.EventSource eventSource) {
		$trace$.put(EventSeverity:Debug, mess, eventSource.getValue());
	}

	/*Radix::Arte::Trace:put-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::Trace:put")
	public static published  void put (Str code, org.radixware.kernel.common.types.ArrStr words) {
		$trace$.put(code, words);
	}

	/*Radix::Arte::Trace:put-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::Trace:put")
	public static published  void put (Str code, Str word1) {
		$trace$.put(code, new ArrStr(word1));
	}

	/*Radix::Arte::Trace:put-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::Trace:put")
	public static published  void put (org.radixware.kernel.common.enums.EEventSeverity severity, Str mess, org.radixware.ads.Arte.common.EventSource eventSource) {
		$trace$.put(severity,mess,eventSource.getValue());
	}

	/*Radix::Arte::Trace:getMinSeverity-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::Trace:getMinSeverity")
	public static published  long getMinSeverity (org.radixware.ads.Arte.common.EventSource eventSource) {
		return $trace$.getMinSeverity(eventSource.getValue());
	}

	/*Radix::Arte::Trace:isMinSeverityDebug-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::Trace:isMinSeverityDebug")
	public static published  boolean isMinSeverityDebug (org.radixware.ads.Arte.common.EventSource eventSource) {
		return $trace$.getMinSeverity(eventSource.getValue())<=EventSeverity:Debug.getValue().longValue();
	}

	/*Radix::Arte::Trace:putSensitive-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::Trace:putSensitive")
	public static published  void putSensitive (Str code, org.radixware.kernel.common.types.ArrStr words) {
		$trace$.put(code, words, true);
	}

	/*Radix::Arte::Trace:putSensitive-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::Trace:putSensitive")
	public static published  void putSensitive (org.radixware.kernel.common.enums.EEventSeverity severity, Str mess, org.radixware.ads.Arte.common.EventSource eventSource) {
		$trace$.put(severity,mess,eventSource.getValue(), true);
	}

	/*Radix::Arte::Trace:put-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::Trace:put")
	public static published  void put (Str code, org.radixware.kernel.common.types.ArrStr words, Int timeMillis) {
		$trace$.put(code, words, timeMillis == null ? -1 : timeMillis.longValue());
	}

	/*Radix::Arte::Trace:put-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::Trace:put")
	public static published  void put (Str code, Str word1, Int timeMillis) {
		$trace$.put(code, new ArrStr(word1), timeMillis == null ? -1 : timeMillis.longValue());
	}

	/*Radix::Arte::Trace:put-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::Trace:put")
	public static published  void put (Str code, Int timeMillis) {
		$trace$.put(code, new ArrStr(), timeMillis == null ? -1 : timeMillis.longValue());
	}

	/*Radix::Arte::Trace:putAndFlush-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::Trace:putAndFlush")
	public static published  Int putAndFlush (org.radixware.kernel.common.enums.EEventSeverity severity, Str mess, org.radixware.ads.Arte.common.EventSource eventSource, Int timeMillis) {
		$trace$.put(severity, null, new ArrStr(mess), eventSource.getValue(), false, timeMillis == null ? -1 : timeMillis.longValue(), true);
		return $trace$.getDbLog().getLastEventId();
	}

	/*Radix::Arte::Trace:getDbLog-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::Trace:getDbLog")
	public static published  org.radixware.kernel.server.trace.DbLog getDbLog () {
		return $trace$.getDbLog();
	}

	/*Radix::Arte::Trace:addContextProfile-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::Trace:addContextProfile")
	public static published  java.lang.Object addContextProfile (Str extraProfile, org.radixware.ads.Types.server.Entity contextObject) {
		return $trace$.addTargetLog(extraProfile, contextObject.getRadMeta().getName() + "[" + contextObject.getPid().toString() + "]");
	}


}

/* Radix::Arte::Trace - Server Meta*/

/*Radix::Arte::Trace-Server Dynamic Class*/

package org.radixware.ads.Arte.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Trace_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("pdcTrace_____________________"),"Trace",null,

						org.radixware.kernel.common.enums.EClassType.DYNAMIC,
						null,
						null,
						null,

						/*Radix::Arte::Trace:Properties-Properties*/
						null,

						/*Radix::Arte::Trace:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth3MY3U3VYSTNRDDXBABIFNQAAAE"),"enterContext",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("type",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprRYEB3VXM5RF5HDZ42SPXIBSXL4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("id",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprGKIK5FAKPRH6FCRWR45UAIPK7Y"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth437JREGOQPORDMNEABIFNQAABA"),"getMinSeverity",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth4LIUO5IRWLOBDLJLAAMPGXSZKU"),"put",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("code",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr66R2QKB7R5B6HLTXCHBLRD4YZQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("word1",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprWRG7JVS4BBEAXM3IEQ3QHXZSIU")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("word2",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprWJAIFBBD2RCDTOB2YGLWHOLJNM"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth65IS54NZSTNRDDXBABIFNQAAAE"),"enterContext",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("type",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr32OMMWYFIJGYVFZMYCOGE3N55I")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("id",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr4VHV5TBRZFCAJCIG4KIEOXEKVM"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth6QHX4XF2STNRDDXBABIFNQAAAE"),"leaveContext",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("type",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprV632UW62LZAWNIPJWFVJJVOUNE")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("id",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr4NMX22X46FCSHK7P4KGHORBOYU"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthA2VYUFF2STNRDDXBABIFNQAAAE"),"leaveContext",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("type",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprIHZTDAVYBBE2TMLEYBD4NBX4MQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("id",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr4JT7SX2EIRBP5AWVJGLLGJHHJI"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthC3JLOYL54HORDOFEABIFNQAABA"),"flush",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthD6XIGP2SRPNRDCISABIFNQAABA"),"error",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("mess",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprRWT2FLDO6FFZFHOVP3FD5NT4JY")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("eventSource",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprQ7BZI346SNFMBH4ODGN3M4DL7Y"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthFT5KSJQYDLPBDFHQABIFNQAABA"),"isMinSeverityDebug",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthHEJCEHKN6LNRDA2JAAMPGXSZKU"),"delContextProfile",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("contextHandler",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprXXGAKWFPSNDBZNI35JLJD3DPWI"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthI2EAXRIFRTNRDCISABIFNQAABA"),"exceptionStackToString",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("e",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprYPQ2JCZNLVFWJBRURYOCSQLMNA"))
								},org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthP66N3O2SRPNRDCISABIFNQAABA"),"warning",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("mess",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprL6AF7EINHZGI7PWG2YYCRAF5XA")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("eventSource",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprZM3X56VGPZCMVBYKLLBZ2VB6GQ"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthP6MDTEKFYDOBDPM5AAN7YHKUNI"),"put",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("code",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprX2CKAZ5SL5HCXDLSUH2CA6UJPY"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthPB2YHBYRWLOBDLJLAAMPGXSZKU"),"put",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("code",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprY6RT26RFGNHSROHEL6G6IJUM2M")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("word1",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprCT57RDMLQVGU5HRABUMD6USOCY")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("word2",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprRDFGC7I6ARA3VIHLDGDRSRHGTA")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("word3",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr2OV355CERRCMPOMTXM5C7YZFFU"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthQ63BL22SRPNRDCISABIFNQAABA"),"event",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("mess",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr37PZVFTCRZGA5JDY3CVGIXAP7E")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("eventSource",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprQPCCI4EHJFBGDDKWGPC65Z73XI"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthRLY3WC2M6LNRDA2JAAMPGXSZKU"),"addContextProfile",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("extraProfile",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprDNEUV756E5F3DHCSSBSVFBRXQA"))
								},org.radixware.kernel.common.enums.EValType.JAVA_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthS7A5F7KSRPNRDCISABIFNQAABA"),"debug",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("mess",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprYMM6EFXMPBBZ7CNCREFEWSSU6U")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("eventSource",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr34G3QFM2QJAUDI24UEFZN4KVMA"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthUALS3BVVSTNRDDXBABIFNQAAAE"),"put",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("code",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprLR736U5YZVBQRMKFVL342ET77M")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("words",org.radixware.kernel.common.enums.EValType.ARR_STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprQGL5QIHLMFE5LCV3BSAURF7FVY"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthUVXH2WARWLOBDLJLAAMPGXSZKU"),"put",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("code",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprUFMMH6ITYVDYDN3EQMO5E2QOMY")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("word1",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr4N2OOR4NDVC5XF7PW55AB5JICA"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthXVQVCB5YSTNRDDXBABIFNQAAAE"),"put",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("severity",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprNMPSAI4CZFD7VO6CRNYUK63EME")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("mess",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprTEL5YF6KLNF2TA3CUT47GHEQ4E")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("eventSource",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprZCFCMBBTJBGLDHJJEXGIKDVWCU"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthTY5OZ6WJOVETBO7TD5XVEWALEQ"),"getMinSeverity",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("eventSource",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprGP5OIADIJJBUZGOLDFV7BHNUD4"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthUYRFE2NUMBGLVEZAALXQRL7QJU"),"isMinSeverityDebug",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("eventSource",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprFQVXDS7ZEVB37NH2UKBNZSIUAE"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthNBTTEVVYKFBCJHWEKSM7ZA4NQI"),"putSensitive",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("code",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprLS4PXSZFNJA5NN5Y3UYCK6SMJA")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("words",org.radixware.kernel.common.enums.EValType.ARR_STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprVCHNRJQG5RCSZJ2I2PKKPTRQXM"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthGE2TROLTCFEZVJT2IVTMEHBWFE"),"putSensitive",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("severity",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprURLYMRUQNVBBXEILE4SICRQFSE")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("mess",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprZYRWGWIK4RHBZOM3XIWNOP7AX4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("eventSource",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr5YGZMUMDTZBXHCEFKUFTZ7CHCM"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthV3PTWJBEIVFCXOHTDIMKUBSTKE"),"put",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("code",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprELSRWIBAWFEAPBHQ42CMDNJNQA")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("words",org.radixware.kernel.common.enums.EValType.ARR_STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprAARCBOQOIREFHPFTP7URRM236Y")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("timeMillis",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprQIXTOXOSV5B6VF2C7TT5PJBFD4"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthBTHKZVV325D7JIHRVZHPM3JGME"),"put",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("code",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr47QED5QW5FAJFNHHWRSKI6YZFY")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("word1",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprW3BNPXUCEJF7LE5XW6BHKZKNDA")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("timeMillis",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprK7OLHHRNCJH3JIKEO4FU4EN5NQ"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthHHZF2VURANARLA6CHIWQO4QMHI"),"put",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("code",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprFXB4X5BBNNDFJHMHO7LOCRBDMQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("timeMillis",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprIYSZV72BFFH3FPN7CMCYDYK2X4"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthY3JQETPD45AFFMO4PDVM2PT4KA"),"putAndFlush",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("severity",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr2ABAPUHA7NABVAWUX7D4F5IO54")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("mess",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr5QTLPVU2I5G35B74FVUX43HSAI")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("eventSource",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprP4P4UKE34ZHVVNYQXKUK5SVNTU")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("timeMillis",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr2YE43DIUVRGVNJDX3FXQJERM34"))
								},org.radixware.kernel.common.enums.EValType.INT),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthYRVI6VDXD5AYRJHI5NZ6UJRQJM"),"getDbLog",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.JAVA_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthVXFEI2AAYFECNLRFANIAKR4DOM"),"addContextProfile",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("extraProfile",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprDNEUV756E5F3DHCSSBSVFBRXQA")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("contextObject",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprVH67HJTDTFAQDGAA3HDQPHLEK4"))
								},org.radixware.kernel.common.enums.EValType.JAVA_CLASS)
						},
						null,
						null,null,null,false);
}

/* Radix::Arte::Trace - Localizing Bundle */
package org.radixware.ads.Arte.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Trace - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Context type");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Тип контекста");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls24EOIWXIUBGMLDOLGFC3IYOWRM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Context type");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Тип контекста.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2ALH7NX7UVEY3ORSQKKRCEQCUE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Event source");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Источник события");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2N55Q3DXUVGP7B5DLORSUYFQCM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Message parameter #1");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Параметр сообщения #1");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2ZOMPXLRDNCZPPS3O3W2OOXDZI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Transforms the exception stack to text");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Представить стек исключения в виде текста");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3QUCZECGEFCUHDVQ4PPLJVJMII"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Adds the trace profile for the current context.\nDeprecated. Use prototype with contextObject instead.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Добавить профиль трассировки для текущего контекста.\nУстаревший метод. Используйте прототип с контекстным объектом.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls45BFUTGL3VEQ7O3GCRJPCMKXCM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Event source");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Источник события");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4A3A4IIXJNBF3KJ7I6JH54NAMY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Event severity level");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Уровень серьёзности события");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4KPPRGWAINGEXMADWYMGX54YTY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Message parameter #1");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Параметр сообщения #1");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4NFUNCYDJVCINIVQC3GEFZWVBE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Puts the message to trace");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Положить сообщение в трассу");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4OOTH26F2FCATIRX43G7M5AFVU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"true - enabled");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4Q64VAJZHZANHNACNTCEGFRM3E"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Event source");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Источник события");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4QICCST5AZCU5OR4EOBUFEKAHQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Additional profile");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Дополнительный профиль");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6A5ZF3BRKVEZLKM7GO4FZTEPPU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Writes the Error message");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Записать сообщения уровня Error");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6QWBOAG5VNEORCIIE5LVIGR4RQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Database log");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Лог сообщений базы данных");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls762NOCOQZJDMDBNVU5HXNRF5SA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Leaves the trace context.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Покинуть контекст трассы");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7DOOUSQRLZDKRLAT34QDKKOUUQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Puts the sensitive message to trace");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Положить в трассу сообщение с чувствительными данными.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7DP4NQRVMVDENH2ZEAH6CSTHUA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Message parameters");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Параметры сообщения");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7KF4SSBDEBDQVMQLB7Q5AUGN4I"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Message parameter #1");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Параметр сообщения #1");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7UX5SHU5VBGYFPDV2RSFW7B64A"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Trace message");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Текст сообщения");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7WXQ2JI2ERFQTDYTEY23YENTO4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Puts the message to trace");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Положить сообщение в трассу");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAIIGUV4YRVGIRDPFBSC6PA3PNM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Checks whether the debug trace is enabled for this event source");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Проверяет является включен ли уровень трассировки Debug для данного источника событий");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAK2L425KZJB5JPSFVSFFL6T3KQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Trace message");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Текст сообщения.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsATU2DN4P3RBV7EKFY3BNQP5RMQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"ID of the last event in the database log");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Идентификатор последнего события в логе базы данных");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBON23H5T3FAOXL7YHVP36TVOGA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Context ID");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Идентификатор контекста");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBT7MWFHTZJBUNGUTJRKVWWF4Z4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Enters the new trace context");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Войти в новый контекст трассы");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsC2DMQZRCCFBQRO5PFWZRD5VESY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Message parameters");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Параметры сообщения");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsC3BRUEDOFZAINM3B3TRSFCC5ZQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Trace message");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Текст сообщения");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsC72JDQ7RDVAOFK5SWYGTONJ5LQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Trace message");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Текст сообщения");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsD6TU4DPNFFBNPDBW72NHFVSIAM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Event source");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Источник события");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDDTWBLAX5VGPTGF6YHGMNVIOUU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Message parameter #3");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Параметр сообщения #3");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDHPWLEXWYRDY3FL2NKYV4DBD5I"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Event source");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Источник события");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDK7OFC62SNAOTH5YVYSBOTBTB4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Gets the database log writer");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Получить объект, записывающий сообщения в базу данных");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDN2N2TSD7BARNIWGHZYLE5HQZ4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Context type");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Тип контекста");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsE6CELXTNNBDXLIDJCMUBHJASAA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Exception");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Исключение");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEGP7JOP3NNH23OIOF2BPBFFE3A"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Event time (ms)");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Время возникновения события (мс)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsELNVRMKEKFACFNZSGJPAAVJ4LI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Trace message");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Текст сообщения");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFIXUJDSCNJDJTLDOKF4HS2J55M"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Context type");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Тип контекста");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFOM65Q2K7FGZ5APKZE7WD3MSAA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Event severity level");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Уровень серьёзности события");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFTFUJFROLRAKLOHPJLWE4LQ6MY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Context ID");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Идентификатор контекста");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFXMGUYBZUNEUFJILNGQTWZOJHM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Checks whether the debug trace is enabled for this event source");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Проверяет является включен ли уровень трассировки Debug для данного источника событий");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGJPMX2DITNBMHI5KYMZVSAGDD4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Puts the message to trace");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Положить сообщение в трассу");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGJV5JMQHFBHC3MW7ZBKNEBB4MA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Event time (ms)");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Время возникновения события (мс).");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGUB2R4PVRFEWDPVYE7HG3YIM6Y"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Puts the message to trace");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Положить сообщение в трассу");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHPTPA5QAGBED7GY7IW4NSECD3E"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Message code");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Код сообщения");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsIATNJGZGKJCZ3CDYDJBAIJ2DRY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Event source");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Источник события");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsII4SYNLSAVFBVFQIMKT5EIT5ME"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Context ID");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Идентификатор контекста.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsIY3OEO5N6JFXPCADAVZ3UTHCSE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Adds the trace profile for the current context.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Добавить профиль трассировки для текущего контекста.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJSC2VSPCSJEFVHN6ZFOK4IXYTY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Writes the Warning message to trace");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Записать в трассу сообщение с уровнем серьёзности Warning.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsK26V57VFOZBQXN6OY622DKM3CA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Writes the trace buffer to the database");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Записать буфер трассировки в базу данных");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKGAR2LBD25FFHOJGGCY372K6RU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Profile listener");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Обработчик профиля");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKSUO3T2QYJGTTPR72UCBBDOJEE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Context ID");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Идентификатор контекста");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLCUVC2PCRZD5RA222WM3NYTMEI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Event time (ms)");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Время возникновения события (мс)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLOVFFKRWP5F6POY4YU4OCOVMZQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Message parameter #2");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Параметр сообщения #2");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsM5MRWSFQUBFELAQCZPM5F2DE6Y"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Enters the new trace context");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Войти в новый контекст");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMSGUDILZYFBCFF4YSFDX24HQ7E"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Writes the Event message to trace");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Записать в трассу сообщения с уровнем серьёзности Event ");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNBDANPGAFBGRNFHNA5HHTNEODI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Message code");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Код сообщения");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOAYUAQIENJB3DPED3TP52UR4HI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Event source");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Источник события");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsP5W3IHRYG5BVZKMVFMCMSJEAY4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Writes the trace records");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Записывает трассировочные сообщения");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPERMV4S3MNC2FLYUTBRZCL3G2M"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Writes the Debug message to trace");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Записать сообщения уровня Debug в трассу.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQHAW7RVKNBFLNA2AGTXBBYDKY4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Listener for the added profile (TraceTarget).");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Обработчик для добавленного профиля (TraceTarget).");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQLNAEYQPKRGZXDUK67ZJOQMCFU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Puts the message to trace");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Положить сообщение в трассу");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQRFIRZ3TGVAVJKGWA2CV2GZXPA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Message code");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Код сообщения");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRUGNPOMS4VB2HPQ3YYAEMX2DLM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Trace message");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Текст сообщения");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRYIU27W2CFD53H2WG4EM5DP2WY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Additional profile");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Дополнительный профиль");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsS4YQ22XLVRBVTOQONFV6VVMZ5E"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Puts the message to trace");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Положить сообщение в трассу.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsSKWBVTVASVEOLCSWFDEM34GQ7Q"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Message code");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Код сообщения");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsSOSJS52XKZFFDAGIKVHV7Z4LG4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Puts the sensitive message to trace");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Положить в трассу сообщение с чувствительными данными.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsSSXKNIA5SBG2TLGIQHRUPBK4QU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Puts the message to trace and flushes the trace buffer to the database.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Положить событие в трассу и осуществить запись буфера сообщений данных в базу данных.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsSZLNQLWHCVHERHCUAORP24EA4Q"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Gets the minimum severity level of the messages written to trace");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Получить минимальный уровень серьёзности сообщений, записанных в трассу");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsT3KXMN4V6JDCRELO6JWI4NKWVU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Message parameter #1");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Параметр сообщения #1");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTDAPGKI6UJFB3BDGUODQ6FIRGI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"true - enabled");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTP3CL54PSFD47PYYR2N6ZF4EJY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Event parameters");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Параметры события");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTPVSSHYHVNFUJGPQDFBPPWF4PQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Listener for the added profile (TraceTarget).");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Обработчик для добавленного профиля (TraceTarget).");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUNTWEFZY65FWRMUXT7KTDC3W5E"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Message parameter #2");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Параметр сообщения #2");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUSYQ43XPEZD23IM6FNKL7IL52U"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Text representation of the exception stack");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Текстовое представление стека исключения");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUTSEZEHDCBHDTAR7DE7UQ4EZIM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Message code");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Код сообщения");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsV2AUG2TWSFBKZEZJDDTSJX3NVU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Message code");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Код сообщения");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsV6QB764RERC7PLC7D5ZZYWFOZI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Event severity");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Серьёзность события");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVOBOMKHBMNBODPZHNCVSKLYDAY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Leaves the trace context");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Покинуть контекст трассы");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVPQVVCOINBESBH5VUW4GDMW4V4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Integer value of severity level");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Целочисленное значение уровня серьёзности");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVT6I2Q5CPBFRXE2K5INCPPOJ5Y"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Trace message");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Текст сообщения");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVWJAWFI6ZZALPPRJNMN3CCB2DI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Message code");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Код сообщения");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVXGRH5QEYZA5BLIBRBTVD6IH2U"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Message source");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Источник сообщения");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWSIO3T6J2ZEQVMQ5HY4UH5KK44"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Event source");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Источник события");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsX2FHXQ5PUFBNRFDRP4DC44AMIE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Puts the message to trace");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Положить сообщение в трассу");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXA6AMR5V2FDAXFJAN2J42ZREUM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Puts the message to trace");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Положить сообщение в трассу.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXIRCBGW24RDW5DRJLAKHW5XASQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Deletes the previously added profile");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Удалить ранее добавленный профиль");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXWED4567RNEHPEKFLFLXC4QLDQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Message code");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Код события");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXWXYHZ7INFD6ZCBXT4OTW3FWFY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Integer value of severity level");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Целочисленное значение уровня серьёзности");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsY44DKHZGAJADXESXUSG3PA6Y4M"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Puts the message to trace");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Положить сообщение в трассу");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsY5E75JXQG5HRZPYBBKJZ2L7CIM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Gets the minimum severity level of messages written to trace ");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Получить минимальный уровень серьёзности сообщений, записанных в трассу");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYFASWYKG5BBD3PLFVYPLP24PFA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Message code");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Код сообщения");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYHVPI77NNRGT7IHT3X5WGF3VWY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Event time (ms)");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Время возникновения события (мс)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYKARS32MZNG2ZFE5I5M4GJ63XU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(Trace - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbpdcTrace_____________________"),"Trace - Localizing Bundle",$$$items$$$);
}
