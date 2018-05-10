/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.radixware.kernel.common.repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import org.junit.Test;
import org.junit.Assert;
import org.radixware.kernel.common.enums.ERepositorySegmentType;
import org.radixware.kernel.common.repository.Layer.TargetDatabase;

/**
 *
 * @author achernomyrdin
 */
public class LayerTest {
     @Test
     public void TargetDatabaseTest() {
        final Layer layer = new Layer(){
                        protected Segment createSegment(ERepositorySegmentType type) {
                            return null;
                        }
                    };
        final TargetDatabase tdb = new Layer.TargetDatabase(layer,"ORACLE","9,10,11",new ArrayList<Layer.DatabaseOption>(),new ArrayList<Layer.DatabaseOptionDependency>());

        Assert.assertEquals(tdb.getLayer(),layer);
        Assert.assertEquals(tdb.getType(),"ORACLE");
        Assert.assertEquals(tdb.getMinVersion(),new BigDecimal("9"));
        Assert.assertEquals(tdb.getSupportedVersions(),Arrays.asList(new BigDecimal("9"),new BigDecimal("10"),new BigDecimal("11")));
        Assert.assertEquals(tdb.getSupportedVersionsStr(),"9,10,11");
        Assert.assertEquals(tdb.getOptions().size(),0);
        Assert.assertEquals(tdb.getDependencies().size(),0);

        final TargetDatabase tdbNum = new Layer.TargetDatabase(layer,"ORACLE",Arrays.asList(new BigDecimal("9"),new BigDecimal("10"),new BigDecimal("11")),null,null);

        Assert.assertEquals(tdbNum.getLayer(),layer);
        Assert.assertEquals(tdbNum.getType(),"ORACLE");
        Assert.assertEquals(tdbNum.getMinVersion(),new BigDecimal("9"));
        Assert.assertEquals(tdbNum.getSupportedVersions(),Arrays.asList(new BigDecimal("9"),new BigDecimal("10"),new BigDecimal("11")));
        Assert.assertEquals(tdbNum.getSupportedVersionsStr(),"9,10,11");
        Assert.assertEquals(tdbNum.getOptions().size(),0);
        Assert.assertEquals(tdbNum.getDependencies().size(),0);
        
        try{new Layer.TargetDatabase(null,"ORACLE",Arrays.asList(new BigDecimal("9"),new BigDecimal("10"),new BigDecimal("11")),null,null);
            Assert.fail("Mandatory exception was not detected (null 1-st argument)");
        } catch (IllegalArgumentException exc) {
        }
        try{new Layer.TargetDatabase(layer,(String)null,Arrays.asList(new BigDecimal("9"),new BigDecimal("10"),new BigDecimal("11")),null,null);
            Assert.fail("Mandatory exception was not detected (null 2-nd argument)");
        } catch (IllegalArgumentException exc) {
        }
        try{new Layer.TargetDatabase(layer,"",Arrays.asList(new BigDecimal("9"),new BigDecimal("10"),new BigDecimal("11")),null,null);
            Assert.fail("Mandatory exception was not detected (empty 2-nd argument)");
        } catch (IllegalArgumentException exc) {
        }
        try{new Layer.TargetDatabase(layer,"UNKNOWN",Arrays.asList(new BigDecimal("9"),new BigDecimal("10"),new BigDecimal("11")),null,null);
            Assert.fail("Mandatory exception was not detected (unknown 2-nd argument value)");
        } catch (IllegalArgumentException exc) {
        }
     }
}
