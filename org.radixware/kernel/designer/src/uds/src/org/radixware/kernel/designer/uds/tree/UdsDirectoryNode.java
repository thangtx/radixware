package org.radixware.kernel.designer.uds.tree;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import javax.swing.Action;
import org.openide.util.actions.SystemAction;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.uds.files.UdsDirectory;
import org.radixware.kernel.common.defs.uds.files.UdsDirectory.UdsFiles;
import org.radixware.kernel.designer.ads.build.actions.AbstractBuildAction;
import org.radixware.kernel.designer.common.general.creation.CreationSupport;
import org.radixware.kernel.designer.common.general.creation.ICreatureGroup;
import org.radixware.kernel.designer.common.tree.RadixObjectsNodeSortedChildren;
import org.radixware.kernel.designer.uds.creation.UdsDirectoryCreature;
import org.radixware.kernel.designer.uds.creation.UdsReportCreature;
import org.radixware.kernel.designer.uds.creation.UdsUserFuncCreature;
import org.radixware.kernel.designer.uds.tree.actions.ImportFileAction;

public class UdsDirectoryNode extends UdsFileNode {

    public UdsDirectoryNode(UdsDirectory udsDirectory) {
        super(udsDirectory, new RadixObjectsNodeSortedChildren(udsDirectory.getFiles()));
        addCookie(new ImportFileAction.Cookie(udsDirectory.getFiles()));
    }

    @Override
    public void addCustomActions(List<Action> actions) {
        super.addCustomActions(actions);
        actions.add(SystemAction.get(ImportFileAction.class));
    }
    
    public UdsDirectory getUdsDirectory() {
        return (UdsDirectory) getRadixObject();
    }
    
     @Override
    protected CreationSupport createNewCreationSupport() {
        return new CreationSupport() {
            @Override
            public ICreatureGroup[] createCreatureGroups(RadixObject object) {
                List<ICreatureGroup> groups = new LinkedList<ICreatureGroup>();
                groups.add(new ICreatureGroup() {
                    @Override
                    public List<ICreature> getCreatures() {
                        UdsFiles udsFiles = getUdsDirectory().getFiles();
                        return Arrays.asList(new ICreature[]{
                            new UdsDirectoryCreature(udsFiles)
                        });
                    }

                    @Override
                    public String getDisplayName() {
                        return "Uds Files";
                    }
                });
                groups.add(new ICreatureGroup() {
                    @Override
                    public List<ICreatureGroup.ICreature> getCreatures() {
                        UdsFiles files = getUdsDirectory().getFiles();
                        return Arrays.asList(new ICreatureGroup.ICreature[]{
                            new UdsUserFuncCreature(files),
                            new UdsReportCreature(files)
                        });
                    }

                    @Override
                    public String getDisplayName() {
                        return "User Definitions";
                    }
                });
                return groups.toArray(
                        new ICreatureGroup[groups.size()]);
            }
        };
    }
    
    public boolean canCompile(){
        return true;
    }
    
}
