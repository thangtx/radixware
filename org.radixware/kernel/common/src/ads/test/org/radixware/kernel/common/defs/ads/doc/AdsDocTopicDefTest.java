/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.radixware.kernel.common.defs.ads.doc;

import java.io.IOException;
import org.junit.Test;
import static org.junit.Assert.*;
import org.radixware.kernel.common.enums.EIsoLanguage;

/**
 *
 * @author dkurlyanov
 */
public class AdsDocTopicDefTest extends AdsDocDefTest {

    @Test
    public void testPreView() throws IOException {

        AdsDocTopicDef topic = AdsDocTopicDef.Factory.newInstation();

        // simple
        topic.setTitle(EIsoLanguage.ENGLISH, "Test title");
        //topic.getBody().setContent("# Topic title\n" + "# Second title\n" + "**test**");

        assertNotNull(topic.generateDocFile(AdsDocDef.Format.PDF));
        assertNotNull(topic.generateDocFile(AdsDocDef.Format.HTML));
        assertNotNull(topic.generateDocFile(AdsDocDef.Format.DOC));

//        // rus
//        topic.getBody().setContent("Русский текст");
//
//        assertNotNull(topic.generateDocFile(AdsDocDef.Format.PDF));
//        assertNotNull(topic.generateDocFile(AdsDocDef.Format.HTML));
//        assertNotNull(topic.generateDocFile(AdsDocDef.Format.DOC));
    }
}
