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
package org.radixware.kernel.designer.environment.nodes;

import java.util.ArrayList;
import java.util.List;
import javax.swing.Action;
import javax.swing.SwingUtilities;
import org.openide.util.actions.SystemAction;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.kernel.common.defs.dds.DdsModelDef;
import org.radixware.kernel.common.defs.dds.DdsModelManager;
import org.radixware.kernel.common.defs.dds.DdsModule;
import org.radixware.kernel.common.repository.Segment;
import org.radixware.kernel.common.repository.ads.AdsSegment;
import org.radixware.kernel.common.repository.dds.DdsSegment;
import org.radixware.kernel.common.repository.uds.UdsSegment;
import org.radixware.kernel.common.utils.events.IRadixEventListener;
import org.radixware.kernel.common.utils.events.RadixEvent;
import org.radixware.kernel.designer.ads.build.actions.AbstractBuildAction.BuildCookie;
import org.radixware.kernel.designer.ads.build.actions.BuildAction;
import org.radixware.kernel.designer.ads.build.actions.CleanAction;
import org.radixware.kernel.designer.ads.build.actions.CleanAndBuildAction;
import org.radixware.kernel.designer.common.annotations.registrators.NodeFactoryRegistration;
import org.radixware.kernel.designer.common.dialogs.defs.module.creation.ModuleCreature;
import org.radixware.kernel.designer.common.general.creation.CreationSupport;
import org.radixware.kernel.designer.common.general.creation.ICreatureGroup;
import org.radixware.kernel.designer.common.general.creation.ICreatureGroup.ICreature;
import org.radixware.kernel.designer.common.general.nodes.INodeFactory;
import org.radixware.kernel.designer.common.tree.RadixObjectEditCookie;
import org.radixware.kernel.designer.common.tree.RadixObjectNode;
import org.radixware.kernel.designer.common.tree.RadixObjectsNodeSortedChildren;
import org.radixware.kernel.designer.common.tree.actions.RadixdocAction;
import org.radixware.kernel.designer.tree.actions.dds.DdsSegmentFixAction;

public class SegmentNode extends RadixObjectNode {

    private static class SegmentNodeChildren extends RadixObjectsNodeSortedChildren<RadixObject> {

        private final Segment segment;

        public SegmentNodeChildren(Segment segment) {
            super();
            this.segment = segment;
        }

        @Override
        protected RadixObjects<RadixObject> getRadixObjects() {
            return segment.getModules();
        }

        @Override
        protected List<RadixObject> getOrderedList() {
            final List<RadixObject> result = super.getOrderedList();
            if (segment instanceof DdsSegment) {
                if (!segment.isReadOnly()) {
                    result.add(((DdsSegment) segment).getScripts());
                }
            }
            return result;
        }
    }
    private final DdsSegmentFixAction.Cookie fixCookie;
    private final IRadixEventListener modelListener = new IRadixEventListener() {
        @Override
        public void onEvent(RadixEvent e) {
            if (SwingUtilities.isEventDispatchThread()) {
                updateLookupContent();
            } else {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        updateLookupContent();
                    }
                });
            }
        }
    };

    protected SegmentNode(final Segment segment) {
        super(segment, new SegmentNodeChildren(segment));
        addCookie(new BuildCookie(segment));
        if (segment instanceof AdsSegment) {
            addCookie(new RadixdocAction.RadixdocCookie(null, segment, null, null));
        }
        if (segment instanceof DdsSegment) {
            addCookie(new RadixdocAction.RadixdocCookie(null, (DdsSegment) segment, null, null));
            this.fixCookie = new DdsSegmentFixAction.Cookie((DdsSegment) segment);
            updateLookupContent();
            getDdsSegment().getModelSupport().addEventListener(modelListener);
        } else {
            this.fixCookie = null;
        }
    }

    @Override
    protected RadixObjectEditCookie createEditCookie() {
        return null;
    }

    @NodeFactoryRegistration
    public static final class Factory implements INodeFactory<Segment> {

        @Override
        public RadixObjectNode newInstance(Segment segment) {
            return new SegmentNode(segment);
        }
    }

    @Override
    public void addCustomActions(List<Action> actions) {
        super.addCustomActions(actions);
        if (getRadixObject() instanceof UdsSegment) {
            actions.add(SystemAction.get(BuildAction.class));
            actions.add(SystemAction.get(CleanAndBuildAction.class));
            actions.add(SystemAction.get(CleanAction.class));
            actions.add(null);
        } else if (getRadixObject() instanceof AdsSegment) {
            actions.add(SystemAction.get(BuildAction.class));
            actions.add(SystemAction.get(CleanAndBuildAction.class));
            actions.add(SystemAction.get(CleanAction.class));
            actions.add(SystemAction.get(RadixdocAction.class));
            actions.add(null);
        } else if (getDdsSegment() != null) {
            actions.add(SystemAction.get(DdsSegmentFixAction.class));
            actions.add(SystemAction.get(RadixdocAction.class));
        }
    }

    private DdsSegment getDdsSegment() {
        RadixObject obj = getRadixObject();
        if (obj instanceof DdsSegment) {
            return (DdsSegment) obj;
        }
        return null;
    }

    private void updateLookupContent() {
        DdsSegment dds = getDdsSegment();
        if (dds == null) {
            return;
        }
        removeCookie(fixCookie);
        for (DdsModule module : dds.getModules()) {
            final DdsModelManager modelManager = module.getModelManager();
            if (!module.isReadOnly() && modelManager.isInitialized()) {
                final DdsModelDef modifiedModel = modelManager.getModifiedModelIfLoaded();

                if (modifiedModel != null && modifiedModel.getModifierInfo().isOwn()) {
                    addCookie(fixCookie);
                    break;
                }
            }
        }
    }

    public static class ModuleCreationSupport extends CreationSupport {

        @Override
        public ICreatureGroup[] createCreatureGroups(final RadixObject object) {
            if (object instanceof Segment) {
                return new ICreatureGroup[]{
                    new ICreatureGroup() {
                        @Override
                        public List<ICreature> getCreatures() {
                            ArrayList<ICreature> list = new ArrayList<ICreature>();
                            list.add(new ModuleCreature((Segment) object));
                            return list;
                        }

                        @Override
                        public String getDisplayName() {
                            return "Repository";
                        }
                    }
                };
            } else {
                return null;
            }
        }
    }

    @Override
    protected CreationSupport createNewCreationSupport() {
        return new ModuleCreationSupport();
    }
}
