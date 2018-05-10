/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.radixware.kernel.common.defs.ads.doc;

import java.io.File;
import java.io.IOException;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.types.Id;

/**
 *
 * @author dkurlyanov
 */
public class AdsDocDefTest {

    public AdsModule getUnitTestModule() {

        // Load Module in Branch
        Branch branch = null;
        try {
            File file = new File("C:\\RadixWare\\trunc");
            branch = Branch.Factory.loadFromDir(file);
        } catch (IOException ex) {

        }
        //mdlGMS766WABBHK7A3NCU6PPFVC54
        //mdlBCHQPEV7LBEAPMJFSIUVTNZ5OM
        Id idModule = Id.Factory.loadFrom("mdlGMS766WABBHK7A3NCU6PPFVC54");
        return (AdsModule) (branch.getLayers().get(2).getAds().getModules().getById(idModule));
    }

    public AdsDocMapDef getUnitTestMap() {
        AdsModule module = getUnitTestModule();
        return (AdsDocMapDef) module.getDefinitions().findById(Id.Factory.loadFrom("mapQ4ANWZHZ3BGRXAN63HKZD3GHMM"));

    }
    
    public AdsDocTopicDef getUnitTestTopic() {
        AdsModule module = getUnitTestModule();
        return (AdsDocTopicDef) module.getDefinitions().findById(Id.Factory.loadFrom("topGNOBWYL7MFAKLINKC3NFZJHN64"));

    }
}
