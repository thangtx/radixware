/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.radixware.kernel.common.defs.ads.doc;

import org.junit.Test;
import org.radixware.kernel.common.enums.EIsoLanguage;

/**
 *
 * @author dkurlyanov
 */
public class DocTopicBodyTest extends AdsDocDefTest {
    
    @Test
    public void testCreate() {
        AdsDocTopicDef topic = this.getUnitTestTopic();
        //DocTopicBody body = new DocTopicBody(topic, EIsoLanguage.ENGLISH);
        //body.save();
    }

    @Test
    public void testSave() {
    }
    
}
