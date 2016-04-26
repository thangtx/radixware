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

package org.radixware.kernel.designer.common.dialogs.scmlnb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.Position;
import org.openide.util.Exceptions;

import org.radixware.kernel.common.check.RadixProblem;
import org.radixware.kernel.common.check.RadixProblemRegistry;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.scml.Scml;


class ScmlAnnotations implements RadixProblemRegistry.IChangeListener {

    private final HashMap<RadixProblem, ArrayList<ScmlAnnotation>> annotations = new HashMap<RadixProblem, ArrayList<ScmlAnnotation>>();

    private Scml getScml() {
        return doc.getScml();
    }

    private ScmlDocument getScmlDocument() {
        return doc;
    }
    private final ScmlDocument doc;

    ScmlAnnotations(ScmlDocument doc) {
        this.doc = doc;
        RadixProblemRegistry.getDefault().addChangeListener(this);
    }

    public List<ScmlAnnotation> getProblemAnnotations(RadixProblem problem) {
        return annotations.get(problem);
    }

    private int findObject(RadixObject object) {
        if (object instanceof Scml.Item) {
            return getScml().getItems().indexOf((Scml.Item) object);
        }
        return -1;
    }

    @Override
    public void onEvent(RadixProblemRegistry.ChangedEvent e) {
        synchronized (annotations) {
            switch (e.getChangeType()) {
                case REMOVED:
                    for (RadixProblem problem : e.getChangedProblems()) {
                        deannotate(problem);
                    }
                    break;
                case ADDED:
                    for (RadixProblem problem : e.getChangedProblems()) {
                        AnnotationInfo[] infos = computeItemInfo(problem);
                        if (infos != null) {
                            for (int i = 0; i < infos.length; i++) {
                                if (infos[i] != null) {
                                    annotate(infos[i]);
                                }
                            }
                        }
                    }
                    break;
                case CLEARED:
                    clearAnnotations();
                    break;
            }
        }
    }

    private void addAnnotation(RadixProblem p, ScmlAnnotation a) {
        ArrayList<ScmlAnnotation> list = annotations.get(p);
        if (list == null) {
            list = new ArrayList<ScmlAnnotation>();
            annotations.put(p, list);
        }
        list.add(a);
    }

    private void annotate(final AnnotationInfo info) {
        synchronized (annotations) {
            final int[] offsetAndLen = new int[2];
            offsetAndLen[0] = info.range[0];
            offsetAndLen[1] = info.range[1];
            final ScmlAnnotation a = new ScmlAnnotation(info.problem);

            if (SwingUtilities.isEventDispatchThread()) {
                try {

                    Position pos = getScmlDocument().createPosition(offsetAndLen[0]);
                    getScmlDocument().addAnnotation(pos, offsetAndLen[1], a);
                    addAnnotation(info.problem, a);
                } catch (BadLocationException ex) {
                    Exceptions.printStackTrace(ex);
                }
            } else {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        synchronized (annotations) {
                            try {
                                Position pos = getScmlDocument().createPosition(offsetAndLen[0]);
                                getScmlDocument().addAnnotation(pos, offsetAndLen[1], a);
                                addAnnotation(info.problem, a);
                            } catch (BadLocationException ex) {
                                Exceptions.printStackTrace(ex);
                            }
                        }
                    }
                });
            }

        }
    }

    private void clearAnnotations() {
        synchronized (annotations) {
            final HashMap<RadixProblem, ArrayList<ScmlAnnotation>> oldAnnotations = new HashMap<RadixProblem, ArrayList<ScmlAnnotation>>();
            oldAnnotations.putAll(annotations);

            if (SwingUtilities.isEventDispatchThread()) {
                for (Map.Entry<RadixProblem, ArrayList<ScmlAnnotation>> a : oldAnnotations.entrySet()) {
                    for (ScmlAnnotation ann : a.getValue()) {
                        getScmlDocument().removeAnnotation(ann);
                    }
                    annotations.remove(a.getKey());
                }
            } else {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        synchronized (annotations) {
                            for (Map.Entry<RadixProblem, ArrayList<ScmlAnnotation>> a : oldAnnotations.entrySet()) {
                                for (ScmlAnnotation ann : a.getValue()) {
                                    if (ann != null) {
                                        try {
                                            getScmlDocument().removeAnnotation(ann);
                                        } catch (Throwable ex) {
                                            Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
                                        }
                                    }
                                }
                                annotations.remove(a.getKey());
                            }
                        }
                    }
                });
            }
        }
    }

    private void deannotate(final RadixProblem p) {
        synchronized (annotations) {
            final ArrayList<ScmlAnnotation> a = annotations.get(p);
            if (a != null) {

                if (SwingUtilities.isEventDispatchThread()) {
                    for (ScmlAnnotation ann : a) {
                        getScmlDocument().removeAnnotation(ann);
                    }
                    this.annotations.remove(p);
                } else {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            synchronized (annotations) {
                                for (ScmlAnnotation ann : a) {
                                    getScmlDocument().removeAnnotation(ann);
                                }
                                annotations.remove(p);
                            }
                        }
                    });
                }
            }
        }
    }

    private static class AnnotationInfo {

        RadixProblem problem;
        int[] range;

        AnnotationInfo(RadixProblem problem, int[] range) {
            this.problem = problem;
            this.range = range;
        }
    }

    void update() {
        synchronized (annotations) {
            ArrayList<RadixProblem> old_problems = new ArrayList<RadixProblem>(annotations.keySet());
            for (RadixProblem p : old_problems) {
                deannotate(p);
            }

            final RadixProblemRegistry pr = RadixProblemRegistry.getDefault();

            ArrayList<RadixObject> problemSources = new ArrayList<RadixObject>();
            RadixObject obj = getScml();

            while (obj != null) {
                problemSources.add(obj);
                if (obj instanceof Definition) {
                    break;
                }
                obj = obj.getContainer();

            }
            for (Scml.Item item : getScml().getItems()) {
                problemSources.add(item);
            }

            for (RadixObject test : problemSources) {
                Set<RadixProblem> problems = pr.getProblemSet(test);
                for (RadixProblem p : problems) {
                    AnnotationInfo[] infos = computeItemInfo(p);
                    if (infos != null) {
                        for (int i = 0; i < infos.length; i++) {
                            if (infos[i] != null) {
                                annotate(infos[i]);
                            }
                        }
                    }
                }
            }
        }
    }

//    private boolean documentIsValid() {
//        int lastIdx = getScml().getItems().size() - 1;
//        boolean valid = true;
//        try {
//            getScmlDocument().itemOffsetAndLength(lastIdx);
//        } catch (NullPointerException ex) {
//            valid = false;
//        }
//        return valid;
//    }
    private AnnotationInfo[] computeItemInfo(RadixProblem p) {
        if (!getScmlDocument().isValid()) {
            //document is outdated
            return null;
        }
        int[] offsetAndLength = null;
        RadixProblem.IAnnotation problemAnnotation = p.getAnnotation();
        if (problemAnnotation instanceof Scml.ScmlAreaInfo) {
            Scml.ScmlAreaInfo info = (Scml.ScmlAreaInfo) problemAnnotation;
            if (info.getScml() == getScml()) {
                Scml.Item startItem = info.getStartJmlItem();
                if (startItem == null) {
                    return null;
                }
//                if (!documentIsValid()) {
//                    return null;
//                }
                int start = getScmlDocument().getItemStartOffset(getScml().getItems().indexOf(startItem));
                if (startItem instanceof Scml.Text) {
                    start += info.getSourceStartOffset();
                }
                Scml.Item endItem = info.getEndJmlItem();
                if (endItem == null) {
                    return null;
                }
                int end;
                int[] tagInfo = getScmlDocument().itemOffsetAndLength(getScml().getItems().indexOf(endItem));
                if (tagInfo != null) {
                    if (endItem instanceof Scml.Text) {
                        end = tagInfo[0] + info.getSourceEndOffset();
                    } else {
                        end = tagInfo[0] + tagInfo[1];
                    }
                    offsetAndLength = new int[]{start, end > start ? end - start : 1};
                }

            }
        } else {
            int index = findObject(p.getSource());
            if (index >= 0) {
                offsetAndLength = getScmlDocument().itemOffsetAndLength(index);
            }
        }
        if (offsetAndLength != null) {
            try {
                String text = getScmlDocument().getText(offsetAndLength[0], offsetAndLength[1]);

                String[] parts = text.split("\n");
                AnnotationInfo[] infos = new AnnotationInfo[parts.length];
                int offset = offsetAndLength[0];
                for (int i = 0; i < parts.length; i++) {
                    if (parts[i].isEmpty()) {
                        offset++;
                        continue;
                    }

                    int len = parts[i].length();
                    infos[i] = new AnnotationInfo(p, new int[]{offset, len});
                    offset += len + 1;
                }
                return infos;

            } catch (BadLocationException ex) {
                return null;
            }

        }
        return null;
    }
}
