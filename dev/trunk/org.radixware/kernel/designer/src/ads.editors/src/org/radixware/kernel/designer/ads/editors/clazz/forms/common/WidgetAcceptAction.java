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

package org.radixware.kernel.designer.ads.editors.clazz.forms.common;

import org.netbeans.api.visual.action.AcceptProvider;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.action.WidgetAction.State;
import org.netbeans.api.visual.action.WidgetAction.WidgetDropTargetDragEvent;
import org.netbeans.api.visual.action.WidgetAction.WidgetDropTargetDropEvent;
import org.netbeans.api.visual.action.WidgetAction.WidgetDropTargetEvent;
import org.netbeans.api.visual.widget.Widget;
import org.netbeans.modules.visual.action.AcceptAction;


public final class WidgetAcceptAction extends WidgetAction.Adapter {

    public interface WidgetAcceptProvider extends AcceptProvider {
        void cancel(Widget widget);
    }
    
    private final AcceptAction aa;
    private final WidgetAcceptProvider provider;
    
    public WidgetAcceptAction(WidgetAcceptProvider provider) {
        this.aa = new AcceptAction(provider);
        this.provider = provider;
    }

    @Override
    public State dragEnter(Widget widget, WidgetDropTargetDragEvent event) {
        return aa.dragEnter(widget, event);
    }

    @Override
    public State dragExit(Widget widget, WidgetDropTargetEvent event) {
        provider.cancel(widget);
        return super.dragExit(widget, event);
    }

    @Override
    public State dragOver(Widget widget, WidgetDropTargetDragEvent event) {
        return aa.dragOver(widget, event);
    }

    @Override
    public State drop(Widget widget, WidgetDropTargetDropEvent event) {
        return aa.drop(widget, event);
    }

    @Override
    public State dropActionChanged(Widget widget, WidgetDropTargetDragEvent event) {
        return aa.dropActionChanged(widget, event);
    }
}