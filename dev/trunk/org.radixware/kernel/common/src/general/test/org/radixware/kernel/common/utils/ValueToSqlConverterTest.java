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

import java.math.BigDecimal;
import java.util.Date;
import org.junit.Test;
import org.junit.Assert;
import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.exceptions.RadixError;

/**
 *
 * @author achernomyrdin
 */
public class ValueToSqlConverterTest {
    @Test
    public void basicTest() {
        Assert.assertEquals(ValueToSqlConverter.charToSql(' '),"' '");
        Assert.assertEquals(ValueToSqlConverter.charToSql('&'),"chr("+((int)'&')+")");
        Assert.assertEquals(ValueToSqlConverter.charToSql('\n'),"chr("+((int)'\n')+")");
        Assert.assertEquals(ValueToSqlConverter.charToSql('\''),"''''");
        
        Assert.assertEquals(ValueToSqlConverter.strToSql(" "),"' '");
        Assert.assertEquals(ValueToSqlConverter.strToSql("&"),"'' || chr("+((int)'&')+") || ''");
        Assert.assertEquals(ValueToSqlConverter.strToSql("\n"),"'' || chr("+((int)'\n')+") || ''");
        Assert.assertEquals(ValueToSqlConverter.strToSql("\'"),"''''");
        Assert.assertEquals(ValueToSqlConverter.strToSql(" \' \n"),"' '' ' || chr("+((int)'\n')+") || ''");

        try{ValueToSqlConverter.toSql((Object)null,null);
            Assert.fail("Mandtory exception was not detected (null 2-nd argument)");
        } catch (IllegalArgumentException exc) {
        }
        try{ValueToSqlConverter.toSql((ValAsStr)null,null);
            Assert.fail("Mandtory exception was not detected (null 2-nd argument)");
        } catch (IllegalArgumentException exc) {
        }
        try{ValueToSqlConverter.toSql(new Object(),EValType.ANY);
            Assert.fail("Mandtory exception was not detected (unsupported 2-nd argument)");
        } catch (IllegalStateException exc) {
        }
        try{ValueToSqlConverter.toSql(ValAsStr.Factory.loadFrom("123"),EValType.ANY);
            Assert.fail("Mandtory exception was not detected (unsupported 2-nd argument)");
        } catch (RadixError exc) {
        }
        
        Assert.assertEquals(ValueToSqlConverter.toSql((Object)null,EValType.ANY),"NULL");
        Assert.assertEquals(ValueToSqlConverter.toSql((ValAsStr)null,EValType.ANY),"NULL");

        try{ValueToSqlConverter.toSqlDebug((Object)null,null);
            Assert.fail("Mandtory exception was not detected (null 2-nd argument)");
        } catch (IllegalArgumentException exc) {
        }
        try{ValueToSqlConverter.toSqlDebug((ValAsStr)null,null);
            Assert.fail("Mandtory exception was not detected (null 2-nd argument)");
        } catch (IllegalArgumentException exc) {
        }
        try{ValueToSqlConverter.toSqlDebug(new Object(),EValType.ANY);
            Assert.fail("Mandtory exception was not detected (unsupported 2-nd argument)");
        } catch (IllegalStateException exc) {
        }
        try{ValueToSqlConverter.toSqlDebug(ValAsStr.Factory.loadFrom("123"),EValType.ANY);
            Assert.fail("Mandtory exception was not detected (unsupported 2-nd argument)");
        } catch (RadixError exc) {
        }
        
        Assert.assertEquals(ValueToSqlConverter.toSqlDebug((Object)null,EValType.ANY),"NULL");
        Assert.assertEquals(ValueToSqlConverter.toSqlDebug((ValAsStr)null,EValType.ANY),"NULL");
    }

    @Test
    public void complexTest() {
        Assert.assertEquals(ValueToSqlConverter.toSql("test string",EValType.STR),"'test string'");
        try{ValueToSqlConverter.toSql(new Object(),EValType.STR);
            Assert.fail("Mandtory exception was not detected (uncompatible types of object and value type)");
        } catch (IllegalArgumentException exc) {
        }
        
        Assert.assertEquals(ValueToSqlConverter.toSql('c',EValType.CHAR),"'c'");
        try{ValueToSqlConverter.toSql(new Object(),EValType.CHAR);
            Assert.fail("Mandtory exception was not detected (uncompatible types of object and value type)");
        } catch (IllegalArgumentException exc) {
        }

        Assert.assertEquals(ValueToSqlConverter.toSql(true,EValType.BOOL),"1");
        try{ValueToSqlConverter.toSql(new Object(),EValType.BOOL);
            Assert.fail("Mandtory exception was not detected (uncompatible types of object and value type)");
        } catch (IllegalArgumentException exc) {
        }

        Assert.assertEquals(ValueToSqlConverter.toSql(150L,EValType.INT),"150");
        Assert.assertEquals(ValueToSqlConverter.toSql(new BigDecimal("150"),EValType.NUM),"150");   // На самом деле в этом случае можно подсунуть любую херню...
        
        final Date  date = new Date(0);
        date.setYear(100);
        date.setMonth(1);
        date.setDate(1);
        date.setHours(2);
        date.setMinutes(3);
        date.setSeconds(4);
        
        Assert.assertEquals(ValueToSqlConverter.toSql(date,EValType.DATE_TIME),"TO_TIMESTAMP('01.02.2000 02:03:04.000', '"+ValueToSqlConverter.SQL_DATE_FORMAT_4ORACLE+"')");
        try{ValueToSqlConverter.toSql(new Object(),EValType.DATE_TIME);
            Assert.fail("Mandtory exception was not detected (uncompatible types of object and value type)");
        } catch (IllegalArgumentException exc) {
        }
        
        
    }
}

