/*
 * Copyright (c) 2008-2015, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. This Source Code is distributed
 * WITHOUT ANY WARRANTY; including any implied warranties but not limited to
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * Mozilla Public License, v. 2.0. for more details.
 */

package org.radixware.kernel.server.utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.radixware.kernel.common.exceptions.NoConstItemWithSuchValueError;

/**
 * Class for configuration Oracle DB server trace.
 * 
 */
public final class OracleDbServerTraceConfig {

	public static enum Level {

		BASIC(1), BASIC_AND_BIND_PARAMS(4), BASIC_AND_WAIT_EVENTS(8), BASIC_AND_BIND_PARAMS_AND_WAIT_EVENTS(12);

		static Level valueOf(final int val) {
			for (Level e : Level.values()) {
				if (e.intVal == val) {
					return e;
				}
			}
			throw new NoConstItemWithSuchValueError("Unknown Oracle session trace level: " + String.valueOf(val) + " for event 10046",val);
		}
		private final int intVal;

		private Level(final int levelIntVal) {
			this.intVal = levelIntVal;
		}

		public final int intValue() {
			return intVal;
		}
	}

	public OracleDbServerTraceConfig(final Connection db) {
		this.db = db;
	}
	private String sessionId = null;

	public final String getSessionId() throws SQLException {
		if (sessionId == null) {
			sessionId = getSessionId(db);
		}
		return sessionId;
	}

	public final String getTraceFileName() throws SQLException {
		return execStmtReturningStr(
				db,
				"select value from SYS.V_$parameter where Lower(name) = 'user_dump_dest'");
	}

	public final String getTraceFileSize() throws SQLException {
		return execStmtReturningStr(
				db,
				"select value from SYS.V_$parameter where Lower(name) = 'max_dump_file_size'");
	}

//XXX: internet says it worked some time ago, but in 11g the only way to get actual trace level
//for session that i've found is oradebug (server side command line utility usable via sqlplus)
//		final CallableStatement call = db.prepareCall(
//				"declare " +
//				"event_level BINARY_INTEGER; " +
//				"begin " +
//				"sys.dbms_system.read_ev(10046, event_level); " +
//				"? := event_level; " +
//				"end;");
//		try {
//			call.registerOutParameter(1, java.sql.Types.BIGINT);
//			call.execute();
//			final int lvl = call.getInt(1);
//			if (lvl == 0) {
//				return null;
//			}
//			return Level.valueOf(lvl);
//		} finally {
//			call.close();
//		}
//	}

	public final void setSqlTraceOn(final Level level) throws SQLException {
		execStmt(db, "alter session set timed_statistics=true");
		execStmt(db, "alter session set events '10046 trace name context forever, level " + String.valueOf(level.intVal) + "'");
	}

	public final void setSqlTraceOff() throws SQLException {
		execStmt(db, "alter session set events '10046 trace name context off'");
		execStmt(db, "alter session set STATISTICS_LEVEL = BASIC");
		execStmt(db, "alter session set timed_statistics=false");
	}
	private Connection db;

	private final static String getSessionId(final Connection db) throws SQLException {
		return execStmtReturningStr(
				db,
				"select a.spid  " +
				"from SYS.V_$session b join SYS.V_$process a on a.addr = b.paddr " +
				"where  b.audsid = userenv('sessionid')");
	}

	private static String execStmtReturningStr(final Connection db, final String stmt) throws SQLException {
		final Statement st = db.createStatement();
		try {
			final ResultSet rs = st.executeQuery(stmt);
			try {
				if (rs.next()) {
					return rs.getString(1);
				}
				return null;
			} finally {
				rs.close();
			}
		} finally {
			st.close();
		}
	}

	private final static void execStmt(final Connection db, final String stmt) throws SQLException {
		Statement st = db.createStatement();
		try {
			st.execute(stmt);
		} finally {
			st.close();
		}
	}
}
