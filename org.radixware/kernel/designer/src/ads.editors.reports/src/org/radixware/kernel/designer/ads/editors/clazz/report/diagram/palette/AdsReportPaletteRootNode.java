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
package org.radixware.kernel.designer.ads.editors.clazz.report.diagram.palette;

import java.awt.Image;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.lookup.Lookups;
import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportForm;
import org.radixware.kernel.common.enums.EReportCellType;

public class AdsReportPaletteRootNode extends AbstractNode {

    private static class RootChildren extends org.openide.nodes.ChildFactory<Object> {

        @Override
        protected boolean createKeys(final List<Object> itemsKeys) {
            itemsKeys.add(new Object());
            return true;
        }

        @Override
        protected Node[] createNodesForKey(final Object key) {
            return new Node[]{
                new TopicNode("Cells", new CellsChildren()),
                new TopicNode("Subreport", Children.create(new SubreportChildren(), false)),
                new TopicNode("CellContainer", Children.create(new CellContainerChildren(), false))
            };
        }
    }

    private static class TopicNode extends AbstractNode {

        public TopicNode(final String name, final Children children) {
            super(children);
            setName(name);
        }
    }

    private static class CellsChildren extends Children.Keys<EReportCellType> implements ModeChangeListener {

        public CellsChildren() {
            setKeys(EnumSet.allOf(EReportCellType.class));
            AdsReportPaletteRootNode.addModeChangeListener(this);
        }

        @Override
        protected Node[] createNodes(EReportCellType t) {
            return new Node[]{
                new NewCellNode(t)
            };
        }

        @Override
        public void onModeChaged(AdsReportForm.Mode newMode) {
            if (newMode == AdsReportForm.Mode.GRAPHICS) {
                setKeys(EnumSet.allOf(EReportCellType.class));
            } else {
                Set<EReportCellType> keys = EnumSet.allOf(EReportCellType.class);
                keys.remove(EReportCellType.CHART);
                keys.remove(EReportCellType.IMAGE);
                keys.remove(EReportCellType.DB_IMAGE);
                setKeys(keys);
            }
        }
    }

    private static class SubreportChildren extends org.openide.nodes.ChildFactory<Object> {

        @Override
        protected boolean createKeys(final List<Object> itemsKeys) {
            itemsKeys.add(new Object());
            return true;
        }

        @Override
        protected Node[] createNodesForKey(final Object key) {
            return new Node[]{new NewPreReportNode(),/* new NewSubReportNode(),*/new NewPostReportNode()};
        }
    }

    private static class CellContainerChildren extends org.openide.nodes.ChildFactory<Object> {

        @Override
        protected boolean createKeys(final List<Object> itemsKeys) {
            itemsKeys.add(new Object());
            return true;
        }

        @Override
        protected Node[] createNodesForKey(final Object key) {
            return new Node[]{new NewCellContainerNode()};
        }
    }

    public static class NewCellNode extends AbstractNode {

        private final EReportCellType cellType;

        public NewCellNode(final EReportCellType cellType) {
            super(Children.LEAF, Lookups.fixed(AdsReportAddNewItemAction.Factory.newAddCellAction(cellType)));
            this.cellType = cellType;
            setName(cellType.getValue());
        }

        @Override
        public Image getIcon(final int type) {
            return AdsDefinitionIcon.getForReportCellType(cellType).getImage();
        }

    }

//    public static class NewSubReportNode extends AbstractNode {
//
//        public NewSubReportNode() {
//            super(Children.LEAF, Lookups.fixed(AdsReportAddNewItemAction.Factory.newAddSubreportAction()));
//            setName("Subreport");
//        }
//
//        @Override
//        public Image getIcon(final int type) {
//            return AdsDefinitionIcon.REPORT_SUB_REPORT.getImage();
//        }
//    }

    public static class NewPreReportNode extends AbstractNode {

        public NewPreReportNode() {
            super(Children.LEAF, Lookups.fixed(AdsReportAddNewItemAction.Factory.newAddPrereportAction()));
            setName("Pre-Report");
        }

        @Override
        public Image getIcon(final int type) {
            return AdsDefinitionIcon.REPORT_PRE_REPORT.getImage();
        }
    }
    public static class NewPostReportNode extends AbstractNode {

        public NewPostReportNode() {
            super(Children.LEAF, Lookups.fixed(AdsReportAddNewItemAction.Factory.newAddPostreportAction()));
            setName("Post-Report");
        }

        @Override
        public Image getIcon(final int type) {
            return AdsDefinitionIcon.REPORT_POST_REPORT.getImage();
        }
    }

    public static class NewCellContainerNode extends AbstractNode {

        public NewCellContainerNode() {
            super(Children.LEAF, Lookups.fixed(AdsReportAddNewItemAction.Factory.newAddReportContainerAction()));
            setName("CellContainer");
        }

        @Override
        public Image getIcon(final int type) {
            return AdsDefinitionIcon.WIDGETS.FRAME.getImage();
        }
    }

    public AdsReportPaletteRootNode() {
        super(Children.create(new RootChildren(), true));
    }

    private interface ModeChangeListener {

        void onModeChaged(AdsReportForm.Mode newMode);
    }

    private static final Map<ModeChangeListener, Object> listeners = new WeakHashMap<>();

    public static void fireModeChange(AdsReportForm.Mode currentMode) {
        synchronized (listeners) {
            for (ModeChangeListener l : listeners.keySet()) {
                l.onModeChaged(currentMode);
            }
        }
    }

    private static void addModeChangeListener(ModeChangeListener l) {
        synchronized (listeners) {
            listeners.put(l, null);
        }
    }
}
