/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.radixware.kernel.designer.common.general.merge;

import java.io.File;
import java.util.List;
import org.radixware.kernel.common.defs.Definition;

/**
 *
 * @author akrylov
 */
public class MergeEngineProvider {

    public interface MergeEngine {

        void doWithDefinitions(final List<Definition> list) throws Exception;

        void doWithFiles(final List<File> list) throws Exception;

        void freeSVNRepositoryOptions();

        boolean isAdsSelected(List<File> list);

    }

    private static MergeEngine instance;

    public static void register(MergeEngine engine) {
        instance = engine;
    }

    public static MergeEngine getEngine() {
        return instance;
    }
}
