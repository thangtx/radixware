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
package org.radixware.kernel.common.utils;


import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

public class ResultSetFactoryTest {
	private static final String[] COLUMN_NAMES = {"COLUMN1", "COLUMN2", "COLUMN3"}; 
	private static final String[] COLUMN_NAMES_INVERTED = {"COLUMN3", "COLUMN2", "COLUMN1"}; 
	
	@Test
	public void testExcelData() throws IOException, SQLException {
		try(final InputStream is = ResultSetFactoryTest.class.getResourceAsStream("test.xls");
		    final ResultSet rs = ResultSetFactory.buildResultSet(ResultSetContentType.XLS,is,COLUMN_NAMES)) {
                    
                    lifeCycle(rs,COLUMN_NAMES);
		}
		try(final InputStream is = ResultSetFactoryTest.class.getResourceAsStream("test.xls");
		    final ResultSet rs = ResultSetFactory.buildResultSet(ResultSetContentType.XLS,is,COLUMN_NAMES)) {
                    
                    lifeCycleNamed(rs,COLUMN_NAMES);
		}
                
		try(final InputStream is = ResultSetFactoryTest.class.getResourceAsStream("testWithNames.xls");
                    final ResultSet rs = ResultSetFactory.buildResultSet(ResultSetContentType.XLS,is,true)) {
		
                    lifeCycle(rs,COLUMN_NAMES);
		}
		try(final InputStream is = ResultSetFactoryTest.class.getResourceAsStream("testWithNames.xls");
                    final ResultSet rs = ResultSetFactory.buildResultSet(ResultSetContentType.XLS,is,true,COLUMN_NAMES_INVERTED)) {
		
                    lifeCycleNamed(rs,COLUMN_NAMES_INVERTED);
		}
                
		try(final InputStream is = ResultSetFactoryTest.class.getResourceAsStream("test.xlsx");
                    final ResultSet rs = ResultSetFactory.buildResultSet(ResultSetContentType.XLSX,is,COLUMN_NAMES)) {
		
                    lifeCycle(rs,COLUMN_NAMES);
		}
		try(final InputStream is = ResultSetFactoryTest.class.getResourceAsStream("test.xlsx");
                    final ResultSet rs = ResultSetFactory.buildResultSet(ResultSetContentType.XLSX,is,COLUMN_NAMES)) {
		
                    lifeCycleNamed(rs,COLUMN_NAMES);
		}
                
		try(final InputStream is = ResultSetFactoryTest.class.getResourceAsStream("testWithNames.xlsx");
                    final ResultSet rs = ResultSetFactory.buildResultSet(ResultSetContentType.XLSX,is,true)) {
		
                    lifeCycle(rs,COLUMN_NAMES);
		}
		try(final InputStream is = ResultSetFactoryTest.class.getResourceAsStream("testWithNames.xlsx");
                    final ResultSet rs = ResultSetFactory.buildResultSet(ResultSetContentType.XLSX,is,true,COLUMN_NAMES_INVERTED)) {
		
                    lifeCycleNamed(rs,COLUMN_NAMES_INVERTED);
		}
	}
	
	
	private void lifeCycle(final ResultSet rs, final String[] columns) throws SQLException {
		final ResultSetMetaData rsmd = rs.getMetaData();
		
		Assert.assertEquals(rsmd.getColumnCount(),3);
		for (int index = 0; index < COLUMN_NAMES.length; index++) {
			Assert.assertEquals(rsmd.getColumnName(index+1),COLUMN_NAMES[index]);
		}
		
		int	count = 0;
		double sumDouble = 0;
		long sumLong = 0;
		StringBuilder sb = new StringBuilder(), sbValue = new StringBuilder();
		
		while (rs.next()) {
			count++;
			sumLong += rs.getLong(1);
			sumDouble += rs.getDouble(1);
			sbValue.append(rs.getString(1));
			Assert.assertTrue(rs.getDate(1) instanceof Date);
                        
			sb.append(rs.getString(2));
                        
			Assert.assertTrue(rs.getLong(3) != 0);
			Assert.assertTrue(rs.getDouble(3) != 0);
			Assert.assertTrue(rs.getDate(3) instanceof Date);
		}
		Assert.assertEquals(count,2);
		Assert.assertEquals(sumLong,3L);
		Assert.assertEquals(sumDouble,3,0.001);
		Assert.assertEquals(sbValue.toString(),"1.02.0");
		Assert.assertEquals(sb.toString(),"test1test2");
	}

	private void lifeCycleNamed(final ResultSet rs, final String[] columns) throws SQLException {
		final ResultSetMetaData rsmd = rs.getMetaData();
		
		Assert.assertEquals(rsmd.getColumnCount(),3);
		for (int index = 0; index < columns.length; index++) {
                    Assert.assertEquals(rsmd.getColumnName(index+1),columns[index]);
		}
		
		int	count = 0;
		double sumDouble = 0;
		long sumLong = 0;
		StringBuilder sb = new StringBuilder(), sbValue = new StringBuilder();
		
		while (rs.next()) {
			count++;
			sumLong += rs.getLong("COLUMN1");
			sumDouble += rs.getDouble("COLUMN1");
			sbValue.append(rs.getString("COLUMN1"));
			Assert.assertTrue(rs.getDate("COLUMN1") instanceof Date);
                        
			sb.append(rs.getString("COLUMN2"));
                        
			Assert.assertTrue(rs.getLong("COLUMN3") != 0);
			Assert.assertTrue(rs.getDouble("COLUMN3") != 0);
			Assert.assertTrue(rs.getDate("COLUMN3") instanceof Date);
		}
		Assert.assertEquals(count,2);
		Assert.assertEquals(sumLong,3L);
		Assert.assertEquals(sumDouble,3,0.001);
		Assert.assertEquals(sbValue.toString(),"1.02.0");
		Assert.assertEquals(sb.toString(),"test1test2");
	}
}
