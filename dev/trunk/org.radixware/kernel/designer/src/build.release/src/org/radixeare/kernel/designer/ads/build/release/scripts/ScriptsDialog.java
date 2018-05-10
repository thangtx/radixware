package org.radixeare.kernel.designer.ads.build.release.scripts;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import net.miginfocom.swing.MigLayout;
import org.radixware.kernel.common.builder.release.IScriptDialog;
import org.radixware.kernel.common.builder.release.ReleaseSettings;
import org.radixware.kernel.common.builder.release.ScriptFileInfo;
import org.radixware.kernel.common.builder.release.ScriptInfo;
import org.radixware.kernel.common.defs.dds.utils.ScriptsUtils;
import org.radixware.kernel.common.dialogs.db.DdsScriptUtils;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.repository.dds.DdsScript;
import org.radixware.kernel.common.repository.dds.DdsSegment;
import org.radixware.kernel.common.repository.dds.DdsUpdateInfo;
import org.radixware.kernel.common.svn.RadixSvnException;
import org.radixware.kernel.common.svn.SVNRepositoryAdapter;
import org.radixware.kernel.common.svn.SvnPathUtils;
import org.radixware.kernel.common.utils.RadixObjectsUtils;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.designer.common.dialogs.utils.ModalDisplayer;

public class ScriptsDialog implements IScriptDialog {

    @Override
    public boolean showDialog(final List<DdsScript> incompatibleScripts, final ReleaseSettings settings, final SVNRepositoryAdapter repository, long revision, List<ScriptFileInfo> modifiedFiles) {
        
        if (!incompatibleScripts.isEmpty()) {
            Map<Layer, List<ScriptInfo>> scripts = findAllScripts(incompatibleScripts, repository, settings, revision);
            JPanel panel = new JPanel();
            panel.setLayout(new MigLayout("fill"));

            final ScriptsLayersPanel scriptsBranchPanel;

            try {
                scriptsBranchPanel = new ScriptsLayersPanel(scripts, settings);
            } catch (RadixSvnException | URISyntaxException ex) {
                settings.getLogger().fatal(ex);
                return false;
            }
            JLabel warning = new JLabel(DdsScriptUtils.COMATIBILE_MESSAGE);
            warning.setForeground(Color.red);
            panel.add(warning, "growx, shrink, wrap");
            JLabel mess = new JLabel("<html>" + DdsScriptUtils.INCOMPATIBLE_COLUMN_MESSAGE.replaceAll("\\n", "<br>"));
            panel.add(mess, "growx, shrink, wrap");
            JScrollPane scroll = new JScrollPane(scriptsBranchPanel);
            panel.add(scroll, "grow, push");

            final ModalDisplayer modal = new ModalDisplayer(panel) {
                boolean isCanClose = true;

                @Override
                protected boolean canClose() {
                    return true;
                }

                private void beforeClose() {
                    isCanClose = true;
                    
                    if (!scriptsBranchPanel.isModified()) {
                        isCanClose = true;
                        return;
                    }

                    if (!DialogUtils.messageConfirmation("Do you really want to save and commit modified files?")) {
                        isCanClose = false;
                    }
                }

                @Override
                public void close(boolean modalResult) {
                    if (modalResult) {
                        beforeClose();
                    }
                    if (isCanClose){
                        super.close(modalResult);
                    }
                }

            };
            modal.setTitle("Scripts Backward Compatibility");
            boolean result = modal.showModal();
            if (!result) {
                settings.getLogger().fatal("Release is cancelled by user");
                return false;
            } else {
                if (!scriptsBranchPanel.isModified()) {
                    return true;
                }

                List<ScriptFileInfo> modified;
                try {
                    modified = scriptsBranchPanel.apply();
                } catch (IOException ex) {
                    settings.getLogger().fatal(ex);
                    return false;
                }
                if (modified == null || modified.isEmpty()) {
                    return true;
                }
                settings.getLogger().message("Committing modifided scripts and updating...");
                try {

                    final SVNRepositoryAdapter.Editor editor = repository.createEditor("Changing scripts backward compatibility before release " + settings.getNumber());
                    int cnt;
                    for (ScriptFileInfo info : modified) {
                        File file = info.getFile();
                        final String path = SvnPathUtils.getFilePath(settings.getSvnClientAdapter(), repository, file);
                        cnt = editor.openDirs(SvnPathUtils.getParentDir(path));
                        try (InputStream is = new FileInputStream(file)) {
                            editor.modifyFile(path, is);
                        } catch (IOException ex) {
                            settings.getLogger().fatal(ex);
                            return false;
                        }
                        editor.closeDirs(cnt);
                    }
                    editor.commit();
                    
                    if (modifiedFiles != null){
                        modifiedFiles.addAll(modified);
                    }
                } catch (RadixSvnException | URISyntaxException ex) {
                    settings.getLogger().fatal(ex);
                    return false;
                }
            }
            return result;
        }
        return true;
    }

    private Map<Layer, List<ScriptInfo>> findAllScripts(final List<DdsScript> incompatibleScripts, SVNRepositoryAdapter repository, final ReleaseSettings settings, long revision) {
        Map<Layer, List<ScriptInfo>> result = new HashMap<>();

        for (DdsScript script : incompatibleScripts) {
            Layer layer = script.getLayer();
            if (layer == null || result.containsKey(layer)){
                continue;
}
            List<ScriptInfo> infos = new ArrayList<>();

            List<DdsScript> scripts = ((DdsSegment) layer.getDds()).getScripts().getDbScripts().getUpgradeScripts().list();

            RadixObjectsUtils.sortByName(scripts);
            for (DdsScript s : scripts) {
                final DdsUpdateInfo info = ScriptsUtils.findInfo(s);
                if (info != null) {
                    infos.add(new ScriptInfo(s, info, settings, repository, revision));
                }
            }
            if (!infos.isEmpty()) {
                result.put(layer, infos);
            }
        }

        return result;
    }
}
