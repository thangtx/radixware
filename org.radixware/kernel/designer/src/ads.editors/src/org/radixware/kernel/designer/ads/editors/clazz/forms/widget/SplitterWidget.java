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

package org.radixware.kernel.designer.ads.editors.clazz.forms.widget;

import java.util.Arrays;
import java.util.List;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.EditProvider;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.widget.Widget;
import org.radixware.kernel.common.defs.Definitions;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObjects.ContainerChangedEvent;
import org.radixware.kernel.common.defs.RadixObjects.EChangeType;
import org.radixware.kernel.common.defs.ads.ui.AdsUIItemDef;
import org.radixware.kernel.common.defs.ads.ui.AdsUIUtil;
import org.radixware.kernel.common.defs.ads.ui.AdsWidgetDef;
import org.radixware.kernel.common.defs.ads.ui.WidgetAdapter;
import org.radixware.kernel.common.defs.ads.ui.rwt.AdsRwtWidgetDef;
import org.radixware.kernel.designer.ads.editors.clazz.forms.GraphSceneImpl;
import org.radixware.kernel.designer.ads.editors.clazz.forms.common.LayoutProcessor;
import org.radixware.kernel.designer.ads.editors.clazz.forms.common.LayoutUtil;


public class SplitterWidget extends BaseWidget {

    public SplitterWidget(GraphSceneImpl scene, AdsWidgetDef node) {
        super(scene, node);
        setLayoutProcessor(LayoutProcessor.Factory.newInstance(this, AdsUIUtil.currentWidget(node)));
    }

    public SplitterWidget(GraphSceneImpl scene, AdsRwtWidgetDef node) {
        super(scene, node);
        setLayoutProcessor(LayoutProcessor.Factory.newInstance(this, AdsUIUtil.currentWidget(node)));
    }

    @Override
    protected List<WidgetAction> getInitialActions(GraphSceneImpl scene, RadixObject node) {
        List<WidgetAction> actions = super.getInitialActions(scene, node);
        actions.add(ActionFactory.createEditAction(new EditProvider() {

            @Override
            public void edit(Widget widget) {
            }
        }));
        return actions;
    }

    @Override
    public void onEvent(final ContainerChangedEvent e) {
        if (getNode() instanceof AdsUIItemDef) {

            final WidgetAdapter splitterAdapter = new WidgetAdapter((AdsUIItemDef) getNode());
            final Definitions<? extends AdsUIItemDef> widgets = splitterAdapter.getWidgets();

//            if (e.changeType == EChangeType.ENLARGE) {
//                final double weight = 1. / size;
//
//                for (final AdsUIItemDef wdg : widgets) {
//                    final WidgetAdapter adapter = new WidgetAdapter(wdg);
//                    adapter.setWeight(adapter.getWeight() * (1 - weight));
//                }
//                objectAdapter.setWeight(weight);
//            }

            if (e.changeType == EChangeType.SHRINK) {
                final double weight = LayoutUtil.getSplitterItemWeight((AdsUIItemDef) e.object);
                final double[] shrink = shrink(getArray(widgets, null), weight);

                for (int i = 0; i < shrink.length; ++i) {
                    LayoutUtil.setSplitterItemWeight(widgets.get(i), shrink[i]);
                }
            }
        }
        super.onEvent(e);
    }

    @Override
    public boolean canChangeLayout() {
        return false;
    }

    public static double[] getArray(Definitions<? extends AdsUIItemDef> widgets, List<AdsUIItemDef> exclude) {
        final double[] size = new double[widgets.size()];

        for (int i = 0; i < size.length; ++i) {

            if (exclude == null || !exclude.contains(widgets.get(i))) {
                size[i] = LayoutUtil.getSplitterItemWeight(widgets.get(i));
            }
        }
        return size;
    }

    public static double[] enlarge(double[] items, double w) {

        final double[] size = new double[items.length];

        if (checkSum(items)) {

            for (int i = 0; i < items.length; ++i) {
                size[i] = items[i] * (1 - w);
            }
        } else {
            // некорректные значения весов
            Arrays.fill(size, (1.0 - w) / items.length);
        }

        return size;
    }

    public static double[] shrink(double[] items, double w) {
        final double[] size = new double[items.length];

        if (checkSum(items, w)) {
            for (int i = 0; i < items.length; ++i) {
                size[i] = items[i] * (1 + w / (1 - w));
            }
        } else {
            // некорректные значения весов
            Arrays.fill(size, 1.0 / items.length);
        }

        return size;
    }

    static boolean checkSum(double[] items, double... add) {
        double sum = 0;
        for (final double i : items) {
            sum += i;
        }

        for (final double i : add) {
            sum += i;
        }

        return Math.abs(sum - 1) <= 0.01;
    }
}
