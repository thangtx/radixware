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

package org.radixware.kernel.designer.ads.editors.clazz.algo.workflow.widget;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.List;
import org.radixware.kernel.designer.ads.editors.clazz.algo.workflow.*;
import org.radixware.kernel.common.defs.ads.clazz.algo.object.*;


public class WidgetFactory {
    
    public static BaseWidget createWidgetFull(GraphSceneImpl scene, AdsBaseObject node) {
       assert scene != null && node != null;
       
       BaseWidget widget = (BaseWidget)scene.addNode(node);
       assert widget != null;       

       if (node.getBounds() != null) {
           Rectangle bounds = node.getBounds();
           if (bounds.height < 0 || bounds.width < 0)
               widget.setPreferredLocation(new Point(bounds.x, bounds.y));
           else {
               widget.setPreferredLocation(new Point(bounds.x, bounds.y));
               widget.setPreferredSize(new Dimension(bounds.width, bounds.height));
           }
       }

       for (AdsPin pin : node.getPins())
           scene.addPin(node, pin);
       
       widget.repaint();
       return widget;
    }

    public static BaseWidget createWidget(GraphSceneImpl scene, AdsBaseObject node) {
       assert scene != null && node != null;
       switch (node.getKind()) {
           case START:
               return new StartWidget(scene, (AdsStartObject)node);
           case CATCH:
               return new CatchWidget(scene, (AdsCatchObject)node);
           case EMPTY:
               return new EmptyWidget(scene, (AdsEmptyObject)node);
           case FINISH:
               return new FinishWidget(scene, (AdsFinishObject)node);
           case FORK:
               return new ForkWidget(scene, (AdsForkObject)node);
           case INCLUDE:
               return new IncludeWidget(scene, (AdsIncludeObject)node);
           case MERGE:
               return new MergeWidget(scene, (AdsMergeObject)node);
           case NOTE:
               return new NoteWidget(scene, (AdsNoteObject)node);
           case PROGRAM:
               return new ProgramWidget(scene, (AdsProgramObject)node);
           case RETURN:
               return new ReturnWidget(scene, (AdsReturnObject)node);
           case SCOPE:
               return new ScopeWidget(scene, (AdsScopeObject)node);
           case TERMINATE:
               return new TermWidget(scene, (AdsTermObject)node);
           case THROW:
               return new ThrowWidget(scene, (AdsThrowObject)node);
           case APP:
               return new AppWidget(scene, (AdsAppObject)node);
           case VAR:
               return new VarWidget(scene, (AdsVarObject)node);
           default:
               assert false : "Unknown node type = " + node.getKind();
       }
       return null;
    }

    public static EdgeWidget createEdgeFull(GraphSceneImpl scene, AdsEdge edge) {
       assert scene != null && edge != null;

       EdgeWidget widget = (EdgeWidget)scene.addEdge(edge);
       assert widget != null;

       if (edge.getPoints() != null) {
           List<Point> points = edge.getPoints();
           widget.setControlPoints(points, false);
       }

       scene.setEdgeSource(edge, edge.getSource());
       scene.setEdgeTarget(edge, edge.getTarget());

       widget.repaint();
       return widget;
    }

    public static EdgeWidget createEdge(GraphSceneImpl scene, AdsEdge edge) {
       assert scene != null && edge != null;
       return new EdgeWidget(scene, edge);
    }

}
