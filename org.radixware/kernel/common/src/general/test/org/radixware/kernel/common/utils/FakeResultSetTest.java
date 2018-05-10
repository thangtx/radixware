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



import java.io.ByteArrayOutputStream;
import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.Ref;

import org.junit.Assert;
import org.junit.Test;

public class FakeResultSetTest {
	private static final ThrowedMethod[] THROWED = new ThrowedMethod[]{
											new ThrowedMethod("updateNull",int.class).values(1),
											new ThrowedMethod("updateBoolean",int.class,boolean.class).values(1,false),  
											new ThrowedMethod("updateByte",int.class,byte.class).values(1,(byte)1),  
											new ThrowedMethod("updateShort",int.class,short.class).values(1,(short)1),  
											new ThrowedMethod("updateInt",int.class,int.class).values(1,1),  
											new ThrowedMethod("updateLong",int.class,long.class).values(1,1L),  
											new ThrowedMethod("updateFloat",int.class,float.class).values(1,1.0f),  
											new ThrowedMethod("updateDouble",int.class,double.class).values(1,1.0),  
											new ThrowedMethod("updateBigDecimal",int.class,BigDecimal.class).values(1,null),    
											new ThrowedMethod("updateString",int.class,String.class).values(1,null),  
											new ThrowedMethod("updateBytes",int.class,byte[].class).values(1,null),  
											new ThrowedMethod("updateDate",int.class,Date.class).values(1,null),  
											new ThrowedMethod("updateTime",int.class,Time.class).values(1,null),
											new ThrowedMethod("updateTimestamp",int.class,Timestamp.class).values(1,null),  
											new ThrowedMethod("updateAsciiStream",int.class,InputStream.class,int.class).values(1,null,1),  
											new ThrowedMethod("updateBinaryStream",int.class,InputStream.class,int.class).values(1,null,1),  
											new ThrowedMethod("updateCharacterStream",int.class,Reader.class,int.class).values(1,null,1),  
											new ThrowedMethod("updateObject",int.class,Object.class,int.class).values(1,null,1),
											new ThrowedMethod("updateObject",int.class,Object.class).values(1,null),
											new ThrowedMethod("updateNull",String.class).values(new Object[]{null}),
											new ThrowedMethod("updateBoolean",String.class,boolean.class).values(null,false),  
											new ThrowedMethod("updateByte",String.class,byte.class).values(null,(byte)1),
											new ThrowedMethod("updateShort",String.class,short.class).values(null,(short)1),  
											new ThrowedMethod("updateInt",String.class,int.class).values(null,1),  
											new ThrowedMethod("updateLong",String.class,long.class).values(null,1L),  
											new ThrowedMethod("updateFloat",String.class,float.class).values(null,1.0f),  
											new ThrowedMethod("updateDouble",String.class,double.class).values(null,1.0),  
											new ThrowedMethod("updateBigDecimal",String.class,BigDecimal.class).values(null,null),    
											new ThrowedMethod("updateString",String.class,String.class).values(null,null),
											new ThrowedMethod("updateBytes",String.class,byte[].class).values(null,null),
											new ThrowedMethod("updateDate",String.class,Date.class).values(null,null),  
											new ThrowedMethod("updateTime",String.class,Time.class).values(null,null),  
											new ThrowedMethod("updateTimestamp",String.class,Timestamp.class).values(null,null),  
											new ThrowedMethod("updateAsciiStream",String.class,InputStream.class,int.class).values(null,null,1),  
											new ThrowedMethod("updateBinaryStream",String.class,InputStream.class,int.class).values(null,null,1),  
											new ThrowedMethod("updateCharacterStream",String.class,Reader.class,int.class).values(null,null,1),  
											new ThrowedMethod("updateObject",String.class,Object.class,int.class).values(null,null,1),  
											new ThrowedMethod("updateObject",String.class, Object.class).values(null,null),    
											new ThrowedMethod("insertRow"), 
											new ThrowedMethod("updateRow"), 
											new ThrowedMethod("deleteRow"), 
											new ThrowedMethod("cancelRowUpdates"), 
											new ThrowedMethod("moveToInsertRow"), 
											new ThrowedMethod("updateRef",int.class,Ref.class).values(1,null),    
											new ThrowedMethod("updateRef",String.class,Ref.class).values(null,null),  
											new ThrowedMethod("updateBlob",int.class,Blob.class).values(1,null),  
											new ThrowedMethod("updateBlob",String.class,Blob.class).values(null,null),  
											new ThrowedMethod("updateClob",int.class,Clob.class).values(1,null),  
											new ThrowedMethod("updateClob",String.class,Clob.class).values(null,null),  
											new ThrowedMethod("updateArray",int.class,Array.class).values(1,null),  
											new ThrowedMethod("updateArray",String.class,Array.class).values(null,null),  
											new ThrowedMethod("updateRowId",int.class,RowId.class).values(1,null),  
											new ThrowedMethod("updateRowId",String.class,RowId.class).values(null,null),  
											new ThrowedMethod("updateNString",int.class,String.class).values(1,null),
											new ThrowedMethod("updateNString",String.class,String.class).values(null,null),
											new ThrowedMethod("updateNClob",int.class,NClob.class).values(1,null),  
											new ThrowedMethod("updateNClob",String.class,NClob.class).values(null,null),   
											new ThrowedMethod("updateSQLXML",int.class,SQLXML.class).values(1,null),  
											new ThrowedMethod("updateSQLXML",String.class,SQLXML.class).values(null,null),  
											new ThrowedMethod("updateNCharacterStream",int.class,Reader.class,long.class).values(1,null,0L),  
											new ThrowedMethod("updateNCharacterStream",String.class,Reader.class,long.class).values(null,null,0L),  
											new ThrowedMethod("updateAsciiStream",int.class,InputStream.class,long.class).values(1,null,0L),  
											new ThrowedMethod("updateBinaryStream",int.class,InputStream.class,long.class).values(1,null,0L),  
											new ThrowedMethod("updateCharacterStream",int.class,Reader.class,long.class).values(1,null,0L),  
											new ThrowedMethod("updateAsciiStream",String.class,InputStream.class,long.class).values(null,null,0L),  
											new ThrowedMethod("updateBinaryStream",String.class, InputStream.class,long.class).values(null,null,0L),  
											new ThrowedMethod("updateCharacterStream",String.class,Reader.class,long.class).values(null,null,0L),  
											new ThrowedMethod("updateBlob",int.class,InputStream.class,long.class).values(1,null,0L),  
											new ThrowedMethod("updateBlob",String.class,InputStream.class,long.class).values(null,null,0L),  
											new ThrowedMethod("updateClob",int.class,Reader.class,long.class).values(1,null,0L),  
											new ThrowedMethod("updateClob",String.class,Reader.class,long.class).values(null,null,0L),  
											new ThrowedMethod("updateNClob",int.class,Reader.class,long.class).values(1,null,0L),  
											new ThrowedMethod("updateNClob",String.class,Reader.class,long.class).values(null,null,0L),  
											new ThrowedMethod("updateNCharacterStream",int.class,Reader.class).values(1,null),    
											new ThrowedMethod("updateNCharacterStream",String.class,Reader.class).values(null,null),  
											new ThrowedMethod("updateAsciiStream",int.class,InputStream.class).values(1,null),  
											new ThrowedMethod("updateBinaryStream",int.class,InputStream.class).values(1,null),  
											new ThrowedMethod("updateCharacterStream",int.class,Reader.class).values(1,null),  
											new ThrowedMethod("updateAsciiStream",String.class,InputStream.class).values(null,null),  
											new ThrowedMethod("updateBinaryStream",String.class,InputStream.class).values(null,null),  
											new ThrowedMethod("updateCharacterStream",String.class,Reader.class).values(null,null),  
											new ThrowedMethod("updateBlob",int.class,InputStream.class).values(1,null),  
											new ThrowedMethod("updateBlob",String.class,InputStream.class).values(null,null),  
											new ThrowedMethod("updateClob",int.class,Reader.class).values(1,null),  
											new ThrowedMethod("updateClob",String.class,Reader.class).values(null,null),  
											new ThrowedMethod("updateNClob",int.class,Reader.class).values(1,null),  
											new ThrowedMethod("updateNClob",String.class,Reader.class).values(null,null),    
											};
	
	
	private static final byte[] BYTE_CONTENT = new byte[]{1,2,3,4,5};
	private static final String STRING_CONTENT = "string";
	private static final String[] NAME_SET_1 = {"FIELD1", "FIELD2", "FIELD3", "FIELD4", "FIELD5", "FIELD6", "FIELD7"};
	private static final Object[][] VALUE_SET_1 = new Object[][]{{1,1.5,true,BYTE_CONTENT,STRING_CONTENT,new Date(100),null}};
	
	
	private static final String[] NAME_SET_2 = {"FIELD1", "FIELD2", "FIELD3", "FIELD4"};
	private static final Object[][] VALUE_SET_2 = new Object[][]{{"string1",new BigDecimal(100.5),new Date(100), false}
																,{"string2",new BigDecimal(200.5),new Date(200), true}
																,{"string3",new BigDecimal(300.5),null, null}
																};

	@Test
	public void basicTest() throws SQLException, IOException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException {
		try{new ArrayResultSet(null,ResultSet.TYPE_FORWARD_ONLY,new Object[0][0]);
			Assert.fail("Mandatory exception was not detected (null 1-st argument)");
		} catch (IllegalArgumentException exc) {
		}
		try{new ArrayResultSet(new String[]{"F1",null},666,new Object[0][0]);
			Assert.fail("Mandatory exception was not detected (1-st argument contains nulls)");
		} catch (IllegalArgumentException exc) {
		}
		try{new ArrayResultSet(new String[]{"F1",""},666,new Object[0][0]);
			Assert.fail("Mandatory exception was not detected (1-st argument contains empties)");
		} catch (IllegalArgumentException exc) {
		}
		try{new ArrayResultSet(new String[]{"F1"},666,new Object[0][0]);
			Assert.fail("Mandatory exception was not detected (2-nd argument out of range)");
		} catch (IllegalArgumentException exc) {
		}

		try(final ResultSet rs = new ArrayResultSet(NAME_SET_1,ResultSet.TYPE_FORWARD_ONLY,VALUE_SET_1)) {
			Assert.assertFalse(rs.isWrapperFor(String.class));
			Assert.assertNull(rs.unwrap(String.class));

			Assert.assertEquals(rs.getType(),ResultSet.TYPE_FORWARD_ONLY);
			Assert.assertEquals(rs.getConcurrency(),ResultSet.CONCUR_READ_ONLY);
			Assert.assertEquals(rs.getHoldability(),ResultSet.CLOSE_CURSORS_AT_COMMIT);
			
			Assert.assertEquals(rs.getFetchDirection(),ResultSet.FETCH_UNKNOWN);
			rs.setFetchDirection(ResultSet.FETCH_FORWARD);
			Assert.assertEquals(rs.getFetchDirection(),ResultSet.FETCH_FORWARD);
			try{rs.setFetchDirection(666);
				Assert.fail("Mandatory exception was not detected (illegal fetch direction)");
			} catch (SQLException exc) {
			}

			Assert.assertEquals(rs.getFetchSize(),1);
			rs.setFetchSize(2);
			Assert.assertEquals(rs.getFetchSize(),2);
			try{rs.setFetchSize(0);
				Assert.fail("Mandatory exception was not detected (illegal fetch size)");
			} catch (SQLException exc) {
			}

			Assert.assertFalse(rs.rowDeleted());
			Assert.assertFalse(rs.rowInserted());
			Assert.assertFalse(rs.rowUpdated());
			rs.refreshRow();
			rs.moveToCurrentRow();
			
			for (ThrowedMethod item : THROWED) {
				final Method m = rs.getClass().getMethod(item.name,item.parameters);
				
				try{if (item.values == null) {
						m.invoke(rs);
					}
					else {
						m.invoke(rs,item.values);
					}
					Assert.fail("Mandatory exception was not detected on "+item+" (SQLException is missing)");
				} catch (IllegalAccessException | IllegalArgumentException exc) {
					System.err.println("item="+item+", method = "+m);
					exc.printStackTrace();
					throw exc;
				} catch (InvocationTargetException exc) {
					Assert.assertTrue(exc.getTargetException() instanceof SQLException);
				}
			}

			Assert.assertNull(rs.getWarnings());
			rs.clearWarnings();
			Assert.assertNull(rs.getWarnings());
		}
	}
	
																
	@Test
	public void conversionsTest() throws SQLException, IOException {
		final Calendar cal = Calendar.getInstance();
		
		try(final ResultSet rs = new ArrayResultSet(NAME_SET_1,ResultSet.TYPE_FORWARD_ONLY,VALUE_SET_1)) {
			rs.next();
			
			try{rs.getObject(-1,String.class);
				Assert.fail("Mandatory exception was not detected (illegal column number)");
			} catch (SQLException exc) {
			}
			try{rs.getObject(1,(Class<?>)null);
				Assert.fail("Mandatory exception was not detected (null class)");
			} catch (SQLException exc) {
			}
			try{rs.getObject("unknown",String.class);
				Assert.fail("Mandatory exception was not detected (unknown name)");
			} catch (SQLException exc) {
			}
			
			try{rs.getObject(1,char.class);
				Assert.fail("Mandatory exception was not detected (unsupported conversion)");
			} catch (UnsupportedOperationException exc) {
			}
			try{rs.getObject(1,char[].class);
				Assert.fail("Mandatory exception was not detected (unsupported conversion)");
			} catch (UnsupportedOperationException exc) {
			}
			
			Assert.assertEquals(rs.getByte("FIELD1"),(byte)1);
			Assert.assertEquals(rs.getShort("FIELD1"),(short)1);
			Assert.assertEquals(rs.getInt("FIELD1"),1);
			Assert.assertEquals(rs.getLong("FIELD1"),1L);
			
			Assert.assertEquals(rs.getFloat("FIELD2"),1.5f,0.001f);
			Assert.assertEquals(rs.getDouble("FIELD2"),1.5,0.001);
			Assert.assertEquals(rs.getBigDecimal("FIELD2"),BigDecimal.valueOf(1.5));
			Assert.assertEquals(rs.getBigDecimal("FIELD2",1),BigDecimal.valueOf(1.5));
			Assert.assertNull(rs.getBigDecimal("FIELD7",1));
			
			try{rs.getLong("FIELD5");
				Assert.fail("Mandatory exception was not detected (conversion error)");
			} catch (SQLException exc) {
			}
			try{rs.getDouble("FIELD5");
				Assert.fail("Mandatory exception was not detected (conversion error)");
			} catch (SQLException exc) {
			}
			try{rs.getBigDecimal("FIELD5");
				Assert.fail("Mandatory exception was not detected (conversion error)");
			} catch (SQLException exc) {
			}
			
			Assert.assertTrue(rs.getBoolean("FIELD3"));
			
			Assert.assertArrayEquals(rs.getBytes("FIELD4"),BYTE_CONTENT);
			testContent(rs.getAsciiStream("FIELD4"),BYTE_CONTENT);
			testContent(rs.getUnicodeStream("FIELD4"),BYTE_CONTENT);
			testContent(rs.getBinaryStream("FIELD4"),BYTE_CONTENT);
			testContent(rs.getBlob("FIELD4").getBinaryStream(),BYTE_CONTENT);
			Assert.assertArrayEquals(rs.getRowId("FIELD4").getBytes(),BYTE_CONTENT);

			Assert.assertNull(rs.getBytes("FIELD7"));
			
			
			Assert.assertEquals(rs.getString("FIELD5"),STRING_CONTENT);
			Assert.assertEquals(rs.getNString("FIELD5"),STRING_CONTENT);
			Assert.assertEquals(rs.getObject("FIELD5"),STRING_CONTENT);
			Assert.assertEquals(rs.getObject("FIELD5",String.class),STRING_CONTENT);
//			Assert.assertEquals(rs.getObject("FIELD5",new HashMap<>()),STRING_CONTENT);
			testContent(rs.getCharacterStream("FIELD5"),STRING_CONTENT.toCharArray());
			testContent(rs.getNCharacterStream("FIELD5"),STRING_CONTENT.toCharArray());
			testContent(rs.getClob("FIELD5").getCharacterStream(),STRING_CONTENT.toCharArray());
			testContent(rs.getNClob("FIELD5").getCharacterStream(),STRING_CONTENT.toCharArray());
			
			Assert.assertEquals(rs.getDate("FIELD1"),new Date(1));
			Assert.assertEquals(rs.getDate("FIELD6"),new Date(100));
			Assert.assertEquals(rs.getTime("FIELD6"),new Time(100));
			Assert.assertEquals(rs.getTimestamp("FIELD6"),new Timestamp(100));
			Assert.assertEquals(rs.getDate("FIELD6",cal),new Date(100));
			Assert.assertEquals(rs.getTime("FIELD6",cal),new Time(100));
			Assert.assertEquals(rs.getTimestamp("FIELD6",cal),new Timestamp(100));

			try{rs.getDate("FIELD5");
				Assert.fail("Mandatory exception was not detected (conversion error)");
			} catch (SQLException exc) {
			}
			try{rs.getTime("FIELD5");
				Assert.fail("Mandatory exception was not detected (conversion error)");
			} catch (SQLException exc) {
			}
			try{rs.getTimestamp("FIELD5");
				Assert.fail("Mandatory exception was not detected (conversion error)");
			} catch (SQLException exc) {
			}
			
			Assert.assertFalse(rs.wasNull());
			Assert.assertNull(rs.getTimestamp("FIELD7"));
			Assert.assertTrue(rs.wasNull());
			Assert.assertNotNull(rs.getTimestamp("FIELD6"));
			Assert.assertFalse(rs.wasNull());

			try{rs.getRef("FIELD5");
				Assert.fail("Mandatory exception was not detected (unimplemented error)");
			} catch (SQLException exc) {
			}
			try{rs.getArray("FIELD5");
				Assert.fail("Mandatory exception was not detected (unimplemented error)");
			} catch (SQLException exc) {
			}
			try{rs.getURL("FIELD5");
				Assert.fail("Mandatory exception was not detected (unimplemented error)");
			} catch (SQLException exc) {
			}
			try{rs.getSQLXML("FIELD5");
				Assert.fail("Mandatory exception was not detected (unimplemented error)");
			} catch (SQLException exc) {
			}
			
		}
	}

	@Test
	public void blobAndClobTest() throws SQLException, IOException {
		try(final ResultSet rs = new ArrayResultSet(NAME_SET_1,ResultSet.TYPE_FORWARD_ONLY,VALUE_SET_1)) {
			rs.next();
			
			final Blob blob = rs.getBlob("FIELD4");

			Assert.assertEquals(blob.length(),BYTE_CONTENT.length);
			testContent(blob.getBinaryStream(),BYTE_CONTENT);
			Assert.assertEquals(blob.position(blob,0),-1);
			Assert.assertEquals(blob.position(BYTE_CONTENT,0),-1);
			testContent(blob.getBinaryStream(0,blob.length()),BYTE_CONTENT);
			Assert.assertNotNull(blob.toString());
			
			final Clob clob = rs.getClob("FIELD5");

			Assert.assertEquals(clob.length(),STRING_CONTENT.length());
			testContent(clob.getCharacterStream(),STRING_CONTENT.toCharArray());
			Assert.assertEquals(clob.position(clob,0),-1);
			Assert.assertEquals(clob.position(STRING_CONTENT,0),-1);
			testContent(clob.getCharacterStream(0,clob.length()),STRING_CONTENT.toCharArray());
			testContent(clob.getAsciiStream(),STRING_CONTENT.getBytes());
			Assert.assertNotNull(clob.toString());
		}
	}
	
	
	@Test
	public void movingTest() throws SQLException, IOException {
		try(final ResultSet rs = new ArrayResultSet(NAME_SET_1,ResultSet.TYPE_FORWARD_ONLY,VALUE_SET_1)) {
			Assert.assertTrue(rs.isBeforeFirst());
			Assert.assertFalse(rs.isFirst());
			Assert.assertFalse(rs.isLast());
			Assert.assertFalse(rs.isAfterLast());
			
			rs.next();
			Assert.assertFalse(rs.isBeforeFirst());
			Assert.assertTrue(rs.isFirst());
			Assert.assertTrue(rs.isLast());
			Assert.assertFalse(rs.isAfterLast());

			Assert.assertEquals(rs.getRow(),1);
			
			rs.next();
			Assert.assertFalse(rs.isBeforeFirst());
			Assert.assertFalse(rs.isFirst());
			Assert.assertFalse(rs.isLast());
			Assert.assertTrue(rs.isAfterLast());
		}

		try(final ResultSet rs = new ArrayResultSet(NAME_SET_2,ResultSet.TYPE_FORWARD_ONLY,VALUE_SET_2)) {
			Assert.assertTrue(rs.isBeforeFirst());
			Assert.assertFalse(rs.isFirst());
			Assert.assertFalse(rs.isLast());
			Assert.assertFalse(rs.isAfterLast());
			
			rs.next();
			Assert.assertFalse(rs.isBeforeFirst());
			Assert.assertTrue(rs.isFirst());
			Assert.assertFalse(rs.isLast());
			Assert.assertFalse(rs.isAfterLast());
			
			rs.next();
			Assert.assertFalse(rs.isBeforeFirst());
			Assert.assertFalse(rs.isFirst());
			Assert.assertFalse(rs.isLast());
			Assert.assertFalse(rs.isAfterLast());

			rs.next();
			Assert.assertFalse(rs.isBeforeFirst());
			Assert.assertFalse(rs.isFirst());
			Assert.assertTrue(rs.isLast());
			Assert.assertFalse(rs.isAfterLast());

			rs.next();
			Assert.assertFalse(rs.isBeforeFirst());
			Assert.assertFalse(rs.isFirst());
			Assert.assertFalse(rs.isLast());
			Assert.assertTrue(rs.isAfterLast());
			
			try{rs.previous();
				Assert.fail("Mandatory exception was not detected (any but next() on the FORWARD-ONLY)");
			} catch (SQLException exc) {				
			}
		}

		try(final ResultSet rs = new ArrayResultSet(NAME_SET_2,ResultSet.TYPE_SCROLL_INSENSITIVE,VALUE_SET_2)) {
			Assert.assertTrue(rs.isBeforeFirst());
			Assert.assertFalse(rs.isFirst());
			Assert.assertFalse(rs.isLast());
			Assert.assertFalse(rs.isAfterLast());
			
			rs.next();
			Assert.assertFalse(rs.isBeforeFirst());
			Assert.assertTrue(rs.isFirst());
			Assert.assertFalse(rs.isLast());
			Assert.assertFalse(rs.isAfterLast());
			
			rs.next();
			Assert.assertFalse(rs.isBeforeFirst());
			Assert.assertFalse(rs.isFirst());
			Assert.assertFalse(rs.isLast());
			Assert.assertFalse(rs.isAfterLast());

			rs.next();
			Assert.assertFalse(rs.isBeforeFirst());
			Assert.assertFalse(rs.isFirst());
			Assert.assertTrue(rs.isLast());
			Assert.assertFalse(rs.isAfterLast());

			rs.next();
			Assert.assertFalse(rs.isBeforeFirst());
			Assert.assertFalse(rs.isFirst());
			Assert.assertFalse(rs.isLast());
			Assert.assertTrue(rs.isAfterLast());

			rs.previous();
			Assert.assertFalse(rs.isBeforeFirst());
			Assert.assertFalse(rs.isFirst());
			Assert.assertTrue(rs.isLast());
			Assert.assertFalse(rs.isAfterLast());
			
			rs.previous();
			Assert.assertFalse(rs.isBeforeFirst());
			Assert.assertFalse(rs.isFirst());
			Assert.assertFalse(rs.isLast());
			Assert.assertFalse(rs.isAfterLast());

			rs.previous();
			Assert.assertFalse(rs.isBeforeFirst());
			Assert.assertTrue(rs.isFirst());
			Assert.assertFalse(rs.isLast());
			Assert.assertFalse(rs.isAfterLast());
		
			rs.previous();
			Assert.assertTrue(rs.isBeforeFirst());
			Assert.assertFalse(rs.isFirst());
			Assert.assertFalse(rs.isLast());
			Assert.assertFalse(rs.isAfterLast());
			
			rs.relative(1);
			Assert.assertFalse(rs.isBeforeFirst());
			Assert.assertTrue(rs.isFirst());
			Assert.assertFalse(rs.isLast());
			Assert.assertFalse(rs.isAfterLast());
			
			rs.absolute(2);
			Assert.assertFalse(rs.isBeforeFirst());
			Assert.assertFalse(rs.isFirst());
			Assert.assertFalse(rs.isLast());
			Assert.assertFalse(rs.isAfterLast());

			rs.first();
			Assert.assertFalse(rs.isBeforeFirst());
			Assert.assertTrue(rs.isFirst());
			Assert.assertFalse(rs.isLast());
			Assert.assertFalse(rs.isAfterLast());
			
			rs.beforeFirst();
			Assert.assertTrue(rs.isBeforeFirst());
			Assert.assertFalse(rs.isFirst());
			Assert.assertFalse(rs.isLast());
			Assert.assertFalse(rs.isAfterLast());

			rs.last();
			Assert.assertFalse(rs.isBeforeFirst());
			Assert.assertFalse(rs.isFirst());
			Assert.assertTrue(rs.isLast());
			Assert.assertFalse(rs.isAfterLast());
			
			rs.afterLast();
			Assert.assertFalse(rs.isBeforeFirst());
			Assert.assertFalse(rs.isFirst());
			Assert.assertFalse(rs.isLast());
			Assert.assertTrue(rs.isAfterLast());
		}
	}

	@Test
	public void metadataTest() throws SQLException, IOException {
		final Calendar cal = Calendar.getInstance();
		
		try(final ResultSet rs = new ArrayResultSet(NAME_SET_1,ResultSet.TYPE_FORWARD_ONLY,VALUE_SET_1)) {
			final Statement stmt = rs.getStatement();
			
			Assert.assertEquals(stmt.toString(),"Fake Statement proxy");
			try{stmt.isClosed();
				Assert.fail("Mandatory exception was not detected (fakestatement supports only restricted method list)");
			} catch (SQLException exc) {
			}
			
			Assert.assertEquals(rs.getCursorName(),rs.getClass().getSimpleName());
			
			final ResultSetMetaData rsmd = rs.getMetaData();
			
			Assert.assertEquals(rsmd.getColumnCount(),NAME_SET_1.length);
			Assert.assertFalse(rsmd.isWrapperFor(String.class));
			Assert.assertNull(rsmd.unwrap(String.class));
			
			for (int index = 0; index < NAME_SET_1.length; index++) {
				Assert.assertEquals(rsmd.getColumnLabel(index+1),NAME_SET_1[index]);
				
				Assert.assertFalse(rsmd.isAutoIncrement(index+1));
				Assert.assertFalse(rsmd.isCaseSensitive(index+1));
				Assert.assertFalse(rsmd.isWritable(index+1));
				Assert.assertFalse(rsmd.isDefinitelyWritable(index+1));
				Assert.assertFalse(rsmd.isSearchable(index+1));
				Assert.assertFalse(rsmd.isCaseSensitive(index+1));
				Assert.assertFalse(rsmd.isCurrency(index+1));
				
				Assert.assertTrue(rsmd.isReadOnly(index+1));
				Assert.assertTrue(rsmd.isSigned(index+1));
				Assert.assertTrue(rsmd.getColumnType(index+1) != 0);
				
				Assert.assertEquals(rsmd.getPrecision(index+1),2);
				Assert.assertEquals(rsmd.getScale(index+1),2);
				Assert.assertEquals(rsmd.getColumnDisplaySize(index+1),0);
				Assert.assertEquals(rsmd.isNullable(index+1),ResultSetMetaData.columnNullableUnknown);
				Assert.assertEquals(rsmd.getSchemaName(index+1),"RADIX");
				Assert.assertEquals(rsmd.getTableName(index+1),"TestTable");

				Assert.assertNull(rsmd.getCatalogName(index+1));
				Assert.assertNotNull(rsmd.getColumnTypeName(index+1));
				Assert.assertNotNull(rsmd.getColumnClassName(index+1));
			}
			try{rsmd.getCatalogName(0);
				Assert.fail("Mandatory exception was not detected (illegal column index)");
			} catch (SQLException exc) {
			}
			Assert.assertNotNull(rsmd.toString());
			
			Assert.assertEquals(rs.findColumn("FIELD2"),2);
			
			try{rs.findColumn(null);
				Assert.fail("Mandatory exception was not detected (null 1-st argument)");
			} catch (IllegalArgumentException exc) {
			}
			try{rs.findColumn("");
				Assert.fail("Mandatory exception was not detected (empty 1-st argument)");
			} catch (IllegalArgumentException exc) {
			}
			try{rs.findColumn("UNKNOWN");
                                Assert.fail("Mandatory exception was not detected (missing column name)");
			} catch (SQLException exc) {
			}
		}
	}
	
	
	@Test
	public void lifeCycleTest() throws SQLException {
		final ResultSet rsRef;
		
		try(final ResultSet rs = new ArrayResultSet(NAME_SET_2,ResultSet.TYPE_FORWARD_ONLY,VALUE_SET_2)) {
			rsRef = rs;
			
			int	count = 0, wasNullCount = 0;
			StringBuilder sb = new StringBuilder();
			BigDecimal result = new BigDecimal(0);

			Assert.assertTrue(rs.isBeforeFirst());
			Assert.assertFalse(rs.isAfterLast());
			try{rs.getString(1);
				Assert.fail("Mandatory exception was not detected (Cursor out of dataset)");
			} catch (SQLException exc) {
			}
			
			while (rs.next()) {
				count++;
				sb.append(rs.getString(1));
				if (rs.wasNull()) {
					wasNullCount++;
				}
				result = result.add(rs.getBigDecimal("FIELD2"));
				if (rs.wasNull()) {
					wasNullCount++;
				}
				final Date date = rs.getDate(3);
				if (rs.wasNull()) {
					wasNullCount++;
				}
				if (date != null) {
					Assert.assertTrue(date.getTime() == 100 || date.getTime() == 200);
				}
				final boolean val = rs.getBoolean("FIELD4");
				if (rs.wasNull()) {
					wasNullCount++;
				}
			}
			Assert.assertFalse(rs.isBeforeFirst());
			Assert.assertTrue(rs.isAfterLast());
			try{rs.getString(1);
				Assert.fail("Mandatory exception was not detected (Cursor out of dataset)");
			} catch (SQLException exc) {
			}
			
			Assert.assertFalse(rs.isClosed());
			
			Assert.assertEquals(sb.toString(),"string1string2string3");
			Assert.assertEquals(count,3);
			Assert.assertEquals(wasNullCount,2);
			Assert.assertEquals(result,BigDecimal.valueOf(601.5));
		}
		Assert.assertTrue(rsRef.isClosed());
		
		try{rsRef.getString(1);
			Assert.fail("Mandatory exception was not detected (Result set is closed)");
		} catch (SQLException exc) {
		}
		try{rsRef.next();
			Assert.fail("Mandatory exception was not detected (Result set is closed)");
		} catch (SQLException exc) {
		}
	}

	private void testContent(final InputStream is, final byte[] content) throws IOException {
		try(final ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
			final byte[] buffer = new byte[8192];
			int len;
			
			while ((len = is.read(buffer)) > 0) {
				baos.write(buffer,0,len);
			}
			baos.flush();
			
			Assert.assertArrayEquals(content,baos.toByteArray());
		}
	}

	private void testContent(final Reader is, final char[] content) throws IOException {
		try(final CharArrayWriter baos = new CharArrayWriter()) {
			final char[] buffer = new char[8192];
			int len;
			
			while ((len = is.read(buffer)) > 0) {
				baos.write(buffer,0,len);
			}
			baos.flush();
			
			Assert.assertArrayEquals(content,baos.toCharArray());
		}
	}
	
	private static class ThrowedMethod {
		final String name;
		final Class<?>[] parameters;
		Object[] values;
		
		public ThrowedMethod(final String name, final Class<?>... parameters) {
			this.name = name;
			this.parameters = parameters;
		}
		
		public ThrowedMethod values(final Object... values) {
			this.values = values;
			return this;
		}

		@Override
		public String toString() {
			return "ThrowedMethod [name=" + name + ", parameters=" + Arrays.toString(parameters) + ", values=" + Arrays.toString(values) + "]";
		}
	}
}

class ArrayResultSet extends FakeResultSet {
	private final Object[][] content;

	protected ArrayResultSet(final String[] columns, final int cursorType, final Object[][] content) {
		super("TestTable",columns,cursorType, FakeResultSet.DEFAULT_LOB_PROVIDER);
		this.content = content;
	}

	@Override
	protected int getResultSetSize() {
		return content.length;
	}

	@Override
	protected Object getCell(int row, int col) {
		return content[row][col];
	}

	@Override
	protected ColumnDescriptor[] getColumnDescriptors() {
		final ColumnDescriptor[] result= new ColumnDescriptor[this.columns.length];
		
		for (int index = 0; index < this.columns.length; index++) {
			if (content[0][index] != null) {
				result[index] = new ColumnDescriptor(index,this.columns[index],defineColumnType(content[0][index]),defineColumnSQLType(content[0][index]),content[0][index].getClass().getName(),false,true,2,2); 
			}
			else {
				result[index] = new ColumnDescriptor(index,this.columns[index],Types.VARCHAR,"VARCHAR",String.class.getName(),false,true,2,2); 
			}
		}
		return result;
	}
	
	private int defineColumnType(final Object value) {
		if (value instanceof String) {
			return Types.VARCHAR;
		}
		else if ((value instanceof BigDecimal) || (value instanceof Integer) || (value instanceof Double)) {
			return Types.NUMERIC;
		}
		else if (value instanceof Date) {
			return Types.DATE;
		}
		else if (value instanceof Boolean) {
			return Types.BOOLEAN;
		}
		else if (value instanceof byte[]) {
			return Types.BLOB;
		}
		else if (value != null) {
			throw new UnsupportedOperationException("Not implemented yet for ["+value.getClass()+"]!"); 
		}
		else {
			return Types.VARCHAR;
		}
	}

	private String defineColumnSQLType(final Object value) {
		if (value instanceof String) {
			return "VARCHAR";
		}
		else if ((value instanceof BigDecimal) || (value instanceof Integer) || (value instanceof Double)) {
			return "NUMERIC";
		}
		else if (value instanceof Date) {
			return "DATE";
		}
		else if (value instanceof Boolean) {
			return "BOOLEAN";
		}
		else if (value instanceof byte[]) {
			return "BLOB";
		}
		else if (value != null) {
			throw new UnsupportedOperationException("Not implemented yet for ["+value.getClass()+"]!"); 
		}
		else {
			return "VARCHAR";
		}
	}
}
