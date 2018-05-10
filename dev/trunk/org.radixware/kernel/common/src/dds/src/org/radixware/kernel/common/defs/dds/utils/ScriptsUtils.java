package org.radixware.kernel.common.defs.dds.utils;

import java.util.ArrayList;
import java.util.List;
import org.radixware.kernel.common.defs.HierarchyWalker;
import org.radixware.kernel.common.defs.dds.DdsDefinition;
import org.radixware.kernel.common.defs.dds.DdsModule;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.repository.dds.DdsScript;
import org.radixware.kernel.common.repository.dds.DdsScripts;
import org.radixware.kernel.common.repository.dds.DdsScriptsDir;
import org.radixware.kernel.common.repository.dds.DdsSegment;
import org.radixware.kernel.common.repository.dds.DdsUpdateInfo;
import org.radixware.kernel.common.types.Id;

public class ScriptsUtils {

    public static DdsUpdateInfo findInfo(DdsScript script) {
        DdsScripts.UpdatesInfo upInfo = script.getOwnerDatabaseScripts().getOwnerScripts().getUpdatesInfo();
        for (DdsUpdateInfo inf : upInfo) {
            if (inf.findScript() == script) {
                return inf;
            }
        }
        return null;
    }

    public static List<DdsScript> findIncompatible(Branch branch) {
        List<DdsScript> result = new ArrayList<>();
        for (Layer layer : branch.getLayers().getInOrder()) {
            if (layer.isReadOnly() || layer.isLocalizing() || !layer.isDistributable()) {
                continue;
            }

            DdsScriptsDir scripts = ((DdsSegment) layer.getDds()).getScripts().getDbScripts().getUpgradeScripts();
            for (DdsScript script : scripts) {
                final DdsUpdateInfo info = ScriptsUtils.findInfo(script);
                if (info != null) {
                    if (!info.isBackwardCompatible()) {
                        result.add(script);
                    }
                }
            }
        }
        return result;
    }
    
    /**
     * Find top level DDS definition by identifier, without dependencies, for
     * data movement.
     *
     * @return definition or null if not found.
     */
    public static DdsDefinition findTopLevelDdsDefById(Layer layer, final Id id) {

        return Layer.HierarchyWalker.walk(layer, new Layer.HierarchyWalker.Acceptor<DdsDefinition>() {
            @Override
            public void accept(HierarchyWalker.Controller<DdsDefinition> controller, Layer layer) {
                for (DdsModule ddsModule : ((DdsSegment) layer.getDds()).getModules()) {
                    final DdsDefinition def = (DdsDefinition) ddsModule.getDefinitionSearcher().findInsideById(id);
                    if (def != null) {
                        controller.setResultAndStop(def);
                    }
                }
            }
        });
    }
}
