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

package org.radixware.kernel.designer.debugger.breakpoints;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.ref.WeakReference;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import org.netbeans.api.editor.EditorRegistry;
import org.openide.text.Line;
import org.openide.text.NbDocument;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.defs.ads.src.IJavaSource;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport.CodeType;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport.UsagePurpose;
import org.radixware.kernel.common.enums.ERepositorySegmentType;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.jml.IJmlSource;
import org.radixware.kernel.common.jml.Jml;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.repository.Segment;
import org.radixware.kernel.common.scml.LineMatcher;
import org.radixware.kernel.common.utils.CharOperations;
import org.radixware.kernel.designer.common.dialogs.scmlnb.ScmlDocument;
import org.radixware.kernel.designer.common.dialogs.scmlnb.ScmlEditorPane;
import org.radixware.kernel.designer.common.general.editors.EditorsManager;
import org.radixware.kernel.designer.debugger.impl.ui.EditorManagerProxy;


public class JmlBreakpoint extends RadixBreakpoint {
    
    private final WeakReference<Jml> jmlRef;
    //private volatile Line line;
    private boolean isInvalid = false;
    private List<String> classNames;
    private int lineNumber;
    static final String TYPE_ID = "JML";
    
    @Override
    String getTypeId() {
        return TYPE_ID;
    }
    
    public JmlBreakpoint(Jml jml, Line line) {
        this.jmlRef = new WeakReference<Jml>(jml);
        //this.line = line;
        this.lineNumber = line.getLineNumber();
    }
    private final PropertyChangeListener editorsListener = new PropertyChangeListener() {
        
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            Jml jml = jmlRef.get();
            if (jml == null) {
                EditorsManager.getDefault().removePropertyChangeListener(this);
                //EditorRegistry.removePropertyChangeListener(this);
                return;
            }
            if (EditorsManager.PROP_EDITOR_OPENED.equals(evt.getPropertyName())) {
                RadixObject obj = (RadixObject) evt.getNewValue();
                if (obj == jml || obj.isParentOf(jml)) {
                    if (installAnnotations()) {
                        BreakpointAnnotationListener listener = BreakpointAnnotationListener.getInstance();
                        if (listener != null) {
                            listener.addAnnotation(JmlBreakpoint.this);
                            EditorsManager.getDefault().removePropertyChangeListener(this);
                            //  EditorRegistry.removePropertyChangeListener(this);
                        }
                    }
                }
            } else if (EditorRegistry.FOCUS_GAINED_PROPERTY.equals(evt.getPropertyName())) {
                Object value = evt.getNewValue();
                if (value instanceof ScmlEditorPane) {
                    if (installAnnotations()) {
                        BreakpointAnnotationListener listener = BreakpointAnnotationListener.getInstance();
                        if (listener != null) {
                            listener.addAnnotation(JmlBreakpoint.this);
                            EditorsManager.getDefault().removePropertyChangeListener(this);
                            //  EditorRegistry.removePropertyChangeListener(this);
                        }
                    }
                }
            }
        }
    };
    
    JmlBreakpoint(final Jml jml, int lineNumber) {
        this.jmlRef = new WeakReference<Jml>(jml);
        
        this.lineNumber = lineNumber;

        //editor might be already opened
        SwingUtilities.invokeLater(new Runnable() {
            
            @Override
            public void run() {
                EditorRegistry.addPropertyChangeListener(editorsListener);
                if (!installAnnotations()) {
                    
                    EditorsManager.getDefault().addPropertyChangeListener(editorsListener);
                }
            }
        });
        
    }
    
    private boolean installAnnotations() {
        final Jml jml = getJml();
        if (jml != null) {
            ScmlDocument doc = EditorManagerProxy.findDocument(jml);
            if (doc != null) {
                try {
                    int offset = NbDocument.findLineOffset(doc, JmlBreakpoint.this.lineNumber);
                    Line line = doc.getLine(offset);
                    if (line != null) {
                        //JmlBreakpoint.this.line = line;
                        return true;
                    }
                } catch (RuntimeException ex) {
                    Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
                }
            }
        }
        return false;
    }
    
    static JmlBreakpoint resolve(Branch branch, BreakpointInfo info) {
        Module module = info.findModule(branch, ERepositorySegmentType.ADS);
        if (module instanceof AdsModule) {
            AdsDefinition root = ((AdsModule) module).getDefinitions().findById(info.definitionPath[0]);
            if (root == null) {
                return null;
            }
            Definition owner = root.findComponentDefinition(info.definitionPath);
            if (owner instanceof IJmlSource) {
                Jml jml = ((IJmlSource) owner).getSource(info.suffix1);
                if (jml != null) {
                    return new JmlBreakpoint(jml, info.getLineNumber());
                }
            }
        }
        return null;
    }
    
    @Override
    String getConfigStr() {
        Jml jml = getJml();
        if (jml == null) {
            return null;
        }
        AdsDefinition jmlOwner = jml.getOwnerDef();
        if (jmlOwner == null) {
            return null;
        }
        Module module = jmlOwner.getModule();
        if (module == null) {
            return null;
        }
        Segment segment = module.getSegment();
        if (segment == null) {
            return null;
        }
        Layer layer = segment.getLayer();
        if (layer == null) {
            return null;
        }
        BreakpointInfo info = new BreakpointInfo(layer.getURI(), module.getId(), jmlOwner.getIdPath(), jml.getName(), String.valueOf(calcLineNumber()));
        return info.save();
    }
    
    public Jml getJml() {
        return jmlRef.get();
    }
    
    @Override
    public String getDisplayName() {
        Jml jml = getJml();
        if (jml != null) {
            return jml.getOwnerDef().getQualifiedName() + ":" + jml.getName() + " - " + (calcLineNumber() + 1);
        } else {
            return "Invalid line breakpoint";
        }
    }
    
    @Override
    public String getShortDescription() {
        return null;
    }
    
    private int calcLineNumber() {
        Line line = getLine();
        if (line != null) {
            return line.getLineNumber();
        } else {
            return lineNumber;
        }
    }
    
    public String getSourceName() {
        Jml jml = getJml();
        if (jml == null) {
            return "";
        }
        return LineMatcher.encode(jml.getLocationDescriptor());
    }
    
    private AdsClassDef getOwnerClass(Jml jml) {
        AdsDefinition def = jml.getOwnerDef();
        while (def != null) {
            if (def instanceof AdsClassDef) {
                return ((AdsClassDef) def);
            }
            def = def.getOwnerDef();
        }
        return null;
    }
    
    private int initialize() {
        isInvalid = false;
        Jml jml = getJml();
        if (jml == null) {
            isInvalid = true;
            return -1;
        }
//        ScmlDocument document = EditorManagerProxy.findDocument(jml);
//        if (document == null) {
//            isInvalid = true;
//            return -1;
//        }
        ERuntimeEnvironmentType env = jml.getUsageEnvironment();
        IJavaSource sourceRoot = JavaSourceSupport.findJavaSourceRoot(jml.getOwnerDef());
        if (sourceRoot != null) {
            //UsagePurpose up = UsagePurpose.getPurpose(env, CodeType.EXCUTABLE);

            
            Set<ERuntimeEnvironmentType> envs = EnumSet.noneOf(ERuntimeEnvironmentType.class);
            if (env == ERuntimeEnvironmentType.COMMON_CLIENT) {
                AdsClassDef clazz = getOwnerClass(jml);
                if (clazz != null && clazz.isDual()) {
                    
                    envs.add(ERuntimeEnvironmentType.EXPLORER);
                    //currently only explorer debug is supported
                    //envs.add(ERuntimeEnvironmentType.WEB);
                } else {
                    envs.add(env);
                }
            } else {
                envs.add(env);
            }
            classNames = new LinkedList<String>();
            for (ERuntimeEnvironmentType e : envs) {
                String s = String.valueOf(CharOperations.merge(sourceRoot.getJavaSourceSupport().getMainClassName(UsagePurpose.getPurpose(e, CodeType.EXCUTABLE)), '.'));
                if (s != null && !s.isEmpty()) {
                    classNames.add(s);
                }
            }
            
            if (classNames.isEmpty()) {
                isInvalid = true;
                return -1;
            } else {
                return calcLineNumber();
            }
        } else {
            isInvalid = true;
            return -1;
        }
    }
    
    public Line getLine() {
        final Jml jml = getJml();
        if (jml != null) {
            final ScmlDocument doc = EditorManagerProxy.findDocument(jml);
            if (doc != null) {
                try {
                    final int offset = NbDocument.findLineOffset(doc, JmlBreakpoint.this.lineNumber);
                    return doc.getLine(offset);
                    
                } catch (RuntimeException ex) {
                    Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
                }
            }
        }
        
        return null;
    }
    
    @Override
    public boolean isInvalid() {
        initialize();
        return isInvalid;
    }
    
    @Override
    public String toString() {
        Jml jml = getJml();
        if (jml == null) {
            return "No jml";
        }
        return jml.getOwnerDef().getQualifiedName();
    }
    
    public boolean isSameLocation(Jml jml, Line line) {
        Jml thisJml = getJml();
        if (thisJml == null) {
            return false;
        }
        return jml == thisJml && line.getLineNumber() == calcLineNumber();
    }
    
    @Override
    public List<String> getClassName() {
        initialize();
        if (isInvalid) {
            return null;
        } else {
            return classNames;
        }
    }
    
    public int getLineNumber() {
        initialize();
        if (isInvalid) {
            return -1;
        } else {
            return calcLineNumber();
        }
    }
    
    @Override
    public RadixObject getRadixObject() {
        return getJml();
    }
}
