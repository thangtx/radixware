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

package org.radixware.kernel.designer.uds.tree;

import java.awt.datatransfer.Transferable;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import javax.swing.Action;
import org.openide.filesystems.FileAttributeEvent;
import org.openide.filesystems.FileChangeListener;
import org.openide.filesystems.FileEvent;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileRenameEvent;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;
import org.openide.nodes.Children;
import org.openide.nodes.FilterNode;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.util.actions.SystemAction;
import org.openide.util.datatransfer.ExTransferable;
import org.openide.util.datatransfer.PasteType;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObject.RemovedEvent;
import org.radixware.kernel.common.defs.RadixObjects.ContainerChangedEvent;
import org.radixware.kernel.common.defs.RadixObjects.ContainerChangesListener;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportClassDef;
import org.radixware.kernel.common.defs.ads.localization.AdsLocalizingBundleDef;
import org.radixware.kernel.common.defs.uds.module.UdsFiles;
import org.radixware.kernel.common.defs.uds.module.UdsModule;
import org.radixware.kernel.common.defs.uds.report.UdsExchangeReport;
import org.radixware.kernel.common.defs.uds.userfunc.UdsUserFuncDef;
import org.radixware.kernel.designer.ads.build.actions.AbstractBuildAction.BuildCookie;
import org.radixware.kernel.designer.ads.build.actions.BuildAction;
import org.radixware.kernel.designer.ads.build.actions.CleanAction;
import org.radixware.kernel.designer.ads.build.actions.CleanAndBuildAction;
import org.radixware.kernel.designer.common.annotations.registrators.NodeFactoryRegistration;
import org.radixware.kernel.designer.common.general.creation.CreationSupport;
import org.radixware.kernel.designer.common.general.creation.ICreatureGroup;
import org.radixware.kernel.designer.common.general.creation.ICreatureGroup.ICreature;
import org.radixware.kernel.designer.common.general.nodes.INodeFactory;
import org.radixware.kernel.designer.common.general.nodes.NodesManager;
import org.radixware.kernel.designer.common.tree.RadixObjectNode;
import org.radixware.kernel.designer.uds.creation.UdsDirectoryCreature;
import org.radixware.kernel.designer.uds.creation.UdsReportCreature;
import org.radixware.kernel.designer.uds.creation.UdsUserFuncCreature;
import org.radixware.kernel.designer.uds.tree.actions.ImportFileAction;


public class UdsModuleNode extends RadixObjectNode {

    protected static final class Key {

        private RadixObject def;
        private String fsDirName;

        public Key(RadixObject def) {
            this.def = def;
        }

        public Key(String fsDirName) {
            this.fsDirName = fsDirName;
        }
    }

    private static class UdsModuleNodeChildren extends Children.Keys<Key> {

        private final UdsModule module;
        private static final Key ETC = new Key("etc");
        private final java.util.Map<Key, Node> cache = new HashMap<Key, Node>();
        private final RadixObject.IRemoveListener removeListener = new RadixObject.IRemoveListener() {
            @Override
            public void onEvent(RemovedEvent e) {
                updateKeys();
            }
        };
        private final ContainerChangesListener moduleListener = new ContainerChangesListener() {
            @Override
            public void onEvent(ContainerChangedEvent e) {
                updateKeys();
            }
        };
        private FileObject modileDirObject;
        private final FileChangeListener listener = new FileChangeListener() {
            @Override
            public void fileFolderCreated(FileEvent fe) {
                handleFileEvent(fe, true, false);
            }

            @Override
            public void fileDataCreated(FileEvent fe) {
                handleFileEvent(fe, true, false);
            }

            @Override
            public void fileChanged(FileEvent fe) {
                handleFileEvent(fe, false, false);
            }

            @Override
            public void fileDeleted(FileEvent fe) {
                handleFileEvent(fe, false, true);
            }

            @Override
            public void fileRenamed(FileRenameEvent fre) {
                handleFileEvent(fre, true, true);
            }

            @Override
            public void fileAttributeChanged(FileAttributeEvent fae) {
                handleFileEvent(fae, false, true);
            }
        };

        private void handleFileEvent(FileEvent e, boolean addListener, boolean removeListener) {
            //  FileObject obj = e.getFile();
//            if (obj != null) {
//                if (removeListener) {
//                    obj.removeFileChangeListener(listener);
//                }
//                if (addListener) {
//                    obj.addFileChangeListener(listener);
//                }
//
//            }
            e.runWhenDeliveryOver(new Runnable() {
                @Override
                public void run() {
                    updateKeys();
                }
            });
        }

        public UdsModuleNodeChildren(UdsModule module) {
            this.module = module;
            this.module.getDefinitions().getContainerChangesSupport().addEventListener(moduleListener);
            this.module.getUdsFiles().getContainerChangesSupport().addEventListener(moduleListener);
            try {
                modileDirObject = FileUtil.createFolder(module.getDirectory());
//                modileDirObject.addRecursiveListener(FileUtil.weakFileChangeListener(listener, listener));
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
            updateKeys();
        }

        @Override
        protected Node[] createNodes(Key t) {
            synchronized (cache) {
                Node node = cache.get(t);
                if (node == null) {
                    if (t.def instanceof UdsUserFuncDef) {
                        node = new UserFuncNode((UdsUserFuncDef) t.def);
                        t.def.getRemoveSupport().addEventListener(removeListener);
                        cache.put(t, node);

                    } else if (t == ETC) {
//                        DataObject data = getEtcDataObject();
//                        node = data.getNodeDelegate();
//                        UdsDirectoryNode folderNode = new UdsDirectoryNode(node);
//                        folderNode.setDisplayName("Files");
//                        
//                        return new Node[]{folderNode};
                    } else if (t.def instanceof AdsDefinition && !(t.def instanceof AdsLocalizingBundleDef)) {
                        if (t.def instanceof AdsReportClassDef) {
                            node = new UserReportNode((AdsReportClassDef) t.def);
                        } else {
                            node = NodesManager.findOrCreateNode(t.def);
                        }
                        t.def.getRemoveSupport().addEventListener(removeListener);
                        cache.put(t, node);
                    } else if (t.def instanceof RadixObject || t.def instanceof UdsExchangeReport){
                        node = NodesManager.findOrCreateNode(t.def);
                        t.def.getRemoveSupport().addEventListener(removeListener);
                        cache.put(t, node);
                    }
                }

                if (node != null) {
                    return new Node[]{new FilterNode(node)};
                } else {
                    return new Node[]{};
                }
            }
        }

        private DataObject getEtcDataObject() {
            File file = module.getEtcDir();
            if (file.isDirectory()) {
                try {
                    FileObject obj = FileUtil.createFolder(file);
                    return DataObject.find(obj);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            return null;
        }

        private boolean keyExists(RadixObject def) {
            for (Key key : cache.keySet()) {
                if (key.def == def) {
                    return true;
                }
            }
            return false;
        }

        private void updateKeys() {//module.getDefinitions().list()
            final List<RadixObject> defs = new ArrayList<RadixObject>();
            defs.addAll(module.getUdsFiles().list());
            List<Key> updatedKeys = new ArrayList<Key>();
            synchronized (cache) {
                final List<Key> toRemove = new LinkedList<Key>();
                for (Key key : cache.keySet()) {
                    if (!defs.contains(key.def)) {
                        toRemove.add(key);
                    }
                }

                for (Key key : toRemove) {
                    cache.remove(key);
                }
                for (RadixObject def : defs) {
                    if (!keyExists(def)) {
                        cache.put(new Key(def), null);
                    }
                }
//                DataObject data = getEtcDataObject();
//                if (data == null) {
//                    cache.remove(ETC);
//                } else {
//                    if (!cache.containsKey(ETC)) {
//                        cache.put(ETC, null);
//                    }
//                }

                updatedKeys.addAll(cache.keySet());
                Collections.sort(updatedKeys, new Comparator<Key>() {
                    @Override
                    public int compare(Key o1, Key o2) {
                        if (o1.def != null && o2.def != null) {
                            return o1.def.getName().compareTo(o2.def.getName());
                        } else {
                            if (o1.def == null && o2.def == null) {
                                return o1.fsDirName.compareTo(o2.fsDirName);
                            } else {
                                if (o1.def == null) {
                                    return 1;
                                } else {
                                    return -1;
                                }
                            }
                        }
                    }
                });
                setKeys(updatedKeys);
            }
        }
    }

    public UdsModuleNode(UdsModule radixObject) {
        super(radixObject, new UdsModuleNodeChildren(radixObject));
        addCookie(new BuildCookie(radixObject));
        addCookie(new ImportFileAction.Cookie(radixObject.getUdsFiles()));
    }

    public UdsModule getModule() {
        return (UdsModule) getRadixObject();
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
                        UdsFiles udsFiles = getModule().getUdsFiles();
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
                    public List<ICreature> getCreatures() {
                        UdsFiles udsFiles = getModule().getUdsFiles();
                        return Arrays.asList(new ICreature[]{
                            new UdsUserFuncCreature(udsFiles),
                            new UdsReportCreature(udsFiles)
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

    @NodeFactoryRegistration
    public static final class Factory implements INodeFactory<UdsModule> {

        @Override // Registered in layer.xml
        public RadixObjectNode newInstance(UdsModule ddsModule) {
            return new UdsModuleNode(ddsModule);
        }
    }

    @Override
    public void addCustomActions(List<Action> actions) {
        super.addCustomActions(actions);
        actions.add(SystemAction.get(ImportFileAction.class));
        actions.add(null);
        actions.add(SystemAction.get(BuildAction.class));
        actions.add(SystemAction.get(CleanAndBuildAction.class));
        actions.add(SystemAction.get(CleanAction.class));
        actions.add(null);
        //actions.add(SystemAction.get(CreateFileStorageAction.class));

    }

    @Override
    protected void createPasteTypes(final Transferable transferable, final List<PasteType> pasteTypes) {
        pasteTypes.add(new PasteType() {
            @Override
            public Transferable paste() throws IOException {
                return ExTransferable.EMPTY;
            }
        });
    }
}
