
/* Radix::Utils::Debug - Server Executable*/

/*Radix::Utils::Debug-Server Dynamic Class*/

package org.radixware.ads.Utils.server;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Utils::Debug")
public published class Debug  implements org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {

	public static final String DATE_FORMAT_STRING = "yyyy-MM-dd-HH:mm:ss.SSS";
	public static final java.text.SimpleDateFormat DATE_FORMAT = new java.text.SimpleDateFormat(DATE_FORMAT_STRING);
	/**Metainformation accessor method*/
	@SuppressWarnings("unused")
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return Debug_mi.rdxMeta;}

	/*Radix::Utils::Debug:Nested classes-Nested Classes*/

	/*Radix::Utils::Debug:Properties-Properties*/





























	/*Radix::Utils::Debug:Methods-Methods*/

	/*Radix::Utils::Debug:sout-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Utils::Debug:sout")
	public static published  void sout (Str source, Str message) {
		System.out.println(DATE_FORMAT.format(new java.util.Date()) + ": " + source + " : " + message);
	}

	/*Radix::Utils::Debug:printQuery-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Utils::Debug:printQuery")
	public static published  void printQuery (Str query, java.io.PrintStream printStream) {
		try {
		    final java.sql.PreparedStatement statement = Arte::Arte.getDbConnection().prepareStatement(query);
		    try {
		        final java.sql.ResultSet rs = statement.executeQuery();
		        try {
		            if (printStream != null) {
		                printStream.println("Query: " + query);
		            }
		            while (rs.next()) {
		                for (int i = 0; i < rs.MetaData.ColumnCount; i++) {
		                    if (printStream != null) {
		                        printStream.print(rs.getObject(i + 1));
		                        printStream.print(" ");
		                    }
		                }
		                if (printStream != null) {
		                    printStream.println();
		                }
		            }
		        } finally {
		            rs.close();
		        }
		    } finally {
		        statement.close();
		    }
		    if (printStream != null) {
		        printStream.println();
		    }
		} catch (Exceptions::SQLException ex) {
		    throw new DatabaseError(ex);
		}
	}

	/*Radix::Utils::Debug:printDbmsOutput-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Utils::Debug:printDbmsOutput")
	public static published  void printDbmsOutput (java.io.PrintStream arg) {
		try {
		    arg.println("Dbms output:");
		    java.sql.CallableStatement stmt = Arte::Arte.getDbConnection().prepareCall("begin dbms_output.get_line(?, ?); end;");
		    stmt.registerOutParameter(1, java.sql.Types.VARCHAR);
		    stmt.registerOutParameter(2, java.sql.Types.INTEGER);
		    try {
		        for (;;) {
		            stmt.execute();
		            if (stmt.getInt(2) != 0) {
		                break;
		            }
		            arg.println(stmt.getString(1));
		        }
		    } finally {
		        stmt.close();
		    }
		    arg.println();
		} catch (Exceptions::SQLException ex) {
		    throw new DatabaseError(ex);
		}
	}

	/*Radix::Utils::Debug:sout-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Utils::Debug:sout")
	public static published  void sout (Str message) {
		System.out.println(DATE_FORMAT.format(new java.util.Date()) + ": " + message);
	}

	/*Radix::Utils::Debug:execute-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Utils::Debug:execute")
	public static published  void execute (Str query) {
		executeWithParams(query);
	}

	/*Radix::Utils::Debug:executeWithParams-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Utils::Debug:executeWithParams")
	public static published  void executeWithParams (Str query, java.lang.Object... params) {
		try {
		    final java.sql.PreparedStatement statement = Arte::Arte.getDbConnection().prepareStatement(query);
		    try {
		        for (int i = 0; i < (params == null ? 0 : params.length); i++) {
		            statement.setObject(i + 1, params[i]);
		        }
		        statement.execute();
		    } finally {
		        statement.close();
		    }
		} catch (Exceptions::SQLException ex) {
		    throw new DatabaseError(ex);
		}
	}

	/*Radix::Utils::Debug:printQueryToStr-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Utils::Debug:printQueryToStr")
	public static published  Str printQueryToStr (Str query, java.lang.Object... params) {
		java.lang.StringBuilder sb = new java.lang.StringBuilder();
		try {
		    final java.sql.PreparedStatement statement = Arte::Arte.getDbConnection().prepareStatement(query);
		    try {
		        final java.sql.ResultSet rs = statement.executeQuery();
		        try {
		            sb.append("Query: " + query + "\n");
		            while (rs.next()) {
		                for (int i = 0; i < rs.MetaData.ColumnCount; i++) {

		                    sb.append(rs.getObject(i + 1));
		                    sb.append(" ");
		                }
		                sb.append("\n");

		            }
		        } finally {
		            rs.close();
		        }
		    } finally {
		        statement.close();
		    }
		    sb.append("\n");
		    return sb.toString();
		} catch (Exceptions::SQLException ex) {
		    throw new DatabaseError(ex);
		}
	}


}

/* Radix::Utils::Debug - Server Meta*/

/*Radix::Utils::Debug-Server Dynamic Class*/

package org.radixware.ads.Utils.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Debug_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("adc6HO4C4BH2RCYPP7RFT3SFIGHEU"),"Debug",null,

						org.radixware.kernel.common.enums.EClassType.DYNAMIC,
						null,
						null,
						null,

						/*Radix::Utils::Debug:Properties-Properties*/
						null,

						/*Radix::Utils::Debug:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthDUIF3TJTJ5HXHF6DA7JPMD7H54"),"sout",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("source",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprQ74SKZSBEJBM7MZIOQXQWVBTLA")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("message",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprZUXXB472BZATZHTQPQB27UJJHY"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthC4XTWTYS25HH5IJVKFVZPVPQDU"),"printQuery",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("query",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprWCCXBD2I5BGNRNFDEKWCZV5U3A")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("printStream",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprKGQ2XUUNPNFRHPP5LGEN5TO4XE"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthPAXIBVMIX5DB3LNYNMUXS74JIM"),"printDbmsOutput",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("arg",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprN3QOBTDSBVA33MYX62JNMH6Y4U"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth2UJY7VT5ZFF2VKU3F772VL7LYQ"),"sout",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("message",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprDHZWODWCFRC2BIFAQHOVI5AUDE"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthMIUZJZ5VEBDAPDZ2LDQP5A53Z4"),"execute",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("query",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprJ3ANPS6LFBGFDFO5FG635VC64E"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthNL44WOB33FHF3JG4TP2CTTGD24"),"executeWithParams",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("query",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprJ3ANPS6LFBGFDFO5FG635VC64E")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("params",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprYBCJKG3LWVBDLDJAOEVWIEXJSQ"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthANABZQACZNC6NBFKGGQKW3BIWI"),"printQueryToStr",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("query",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprZBHF64HMG5H6LAJZE2DVGMGY44")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("params",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprKMFRJL55SNEQPIVLFXHIZTWIMY"))
								},org.radixware.kernel.common.enums.EValType.STR)
						},
						null,
						null,null,null,false);
}

/* Radix::Utils::Debug - Localizing Bundle */
package org.radixware.ads.Utils.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Debug - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"SQL query string.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Строка SQL-запроса.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3O6R32MN3ZHSVM5Z7AMH4TQI64"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<code>PrintStream</code> to which the text of the SQL query and query result are printed.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<code>PrintStream</code>, в который будет напечатан текст SQL-запроса и его результат.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls762VEPTRL5DOLKPPUSELRX6UVY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Message to be output.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Сообщение, которое вы хотите вывести.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAPBWZP7QB5EQBO22TLLUSIRORE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Prints the database management system (DBMS) output to a given <code>PrintStream</code>.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Печатает вывод системы менеджмента базы данных (DBMS) в данный <code>PrintStream</code>.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBQHUINXYABDSLAVGINYZSM56QE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"SQL query to be executed and printed.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"SQL-запрос, который будет выполнен и напечатан.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHUIVEEVTSNBOVMTWHRVRQCQGWY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Prints the text of the SQL query and query result to a given <code>PrintStream</code>.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Печатает текст SQL-запроса и его результат в данный <code>PrintStream</code>.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsL3ZUE72EGFBQRKTC2KQEKIPLME"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Executes the SQL query obtained as a string.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Метод, который позволяет выполнить SQL-запрос, полученный в виде строки.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMJ5K6EZ3JRBYVIAGVHX6N4OZKM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Outputs the current date and <i>message</i> to <code>System.out</code>.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Выводит текущую дату и сообщение <i>message</i> в <code>System.out</code>.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOCPN7TLZMZGERK4BP5G5SORZUA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Message source.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Источник сообщения.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOOFOH2UEIFHTPNZQHI6LCL322I"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Query parameters");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPMGVIFLEWJHVVDDDJ7IWXOH5BU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Outputs the current date, message source <i>source</i> and <i>message</i> to <code>System.out</code>.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Выводит текущую дату, источник сообщения <i>source</i> и сообщение <i>message</i> в <code>System.out</code>.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRFDUPFO25RCPRKXC2DVHDSUCYM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"SQL query string.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Строка SQL-запроса.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWKTCI7Q74ZHETHAQLRKTWLYIWE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Class provides methods required for the application debugging.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Класс, который предоставляет методы, полезные при отладке приложения.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYDBP7Q7VQNE6RD6DBYO44QAJ3Q"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<code>PrintStream</code> to which the DBMS output is printed.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<code>PrintStream</code>, в который будет напечатан вывод DBSM.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZ4DTWNSW6VDKZEQTWZY573ZL2M"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Message to be output.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Сообщение, которое вы хотите вывести.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZCIDJ65P2ZFP7CLT3UEJQEN6KY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Executes the SQL query with the parameters. This function DOES NOT perform the commit.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Метод, который позволяет выполнить SQL-запрос с параметрами. Данная функция НЕ ВЫПОЛНЯЕТ коммит.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZNR6PALNR5C5DBCXE2U5K6NQEE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(Debug - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbadc6HO4C4BH2RCYPP7RFT3SFIGHEU"),"Debug - Localizing Bundle",$$$items$$$);
}
