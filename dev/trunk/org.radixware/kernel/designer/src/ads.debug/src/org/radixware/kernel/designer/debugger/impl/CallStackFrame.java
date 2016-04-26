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

package org.radixware.kernel.designer.debugger.impl;

import com.sun.jdi.AbsentInformationException;
import com.sun.jdi.IncompatibleThreadStateException;
import com.sun.jdi.InvalidStackFrameException;
import com.sun.jdi.LocalVariable;
import com.sun.jdi.Location;
import com.sun.jdi.Method;
import com.sun.jdi.ObjectCollectedException;
import com.sun.jdi.ObjectReference;
import com.sun.jdi.ReferenceType;
import com.sun.jdi.StackFrame;
import com.sun.jdi.VMDisconnectedException;
import com.sun.jdi.Value;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import org.openide.text.Line;
import org.openide.util.lookup.Lookups;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.enums.ERepositorySegmentType;
import org.radixware.kernel.common.jml.IJmlSource;
import org.radixware.kernel.common.jml.Jml;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.scml.LineMatcher;
import org.radixware.kernel.common.scml.LineMatcher.LocationInfo;
import org.radixware.kernel.designer.common.general.editors.EditorsManager;
import org.radixware.kernel.designer.common.general.editors.OpenInfo;
import org.radixware.kernel.designer.debugger.RadixDebugger;
import org.radixware.kernel.designer.debugger.impl.ui.EditorManagerProxy;


public class CallStackFrame {

    private int index;
    private ThreadWrapper thread;
    private final Map<LocalVariableWrapper, Value> values = new WeakHashMap<LocalVariableWrapper, Value>();
    private final Map<LocalVariable, LocalVariableWrapper> variables = new WeakHashMap<LocalVariable, LocalVariableWrapper>();
    private final RadixDebugger debugger;

    CallStackFrame(RadixDebugger debugger, ThreadWrapper thread, int index) {
        this.index = index;
        this.thread = thread;
        this.debugger = debugger;
    }

    public boolean isCurrent() {
        return this == thread.getCurrentFrame();
    }

    public ThreadWrapper getOwnerThread() {
        return thread;
    }

    public StackFrame getFrame() {
        try {
            return thread.getThreadReference().frame(index);
        } catch (IncompatibleThreadStateException ex) {
            return null;
        } catch (Throwable e) {
            return null;
        }
    }

    public void setCurrent() {
        thread.setCurrentFrame(this);
    }

    ValueWrapper getValue(LocalVariableWrapper v) {
        try {
            StackFrame frame = getFrame();
            if (frame != null) {
                LocalVariable var = frame.visibleVariableByName(v.getVariableName());
                if (var != null) {
                    Value val = frame.getValue(var);
                    synchronized (values) {
                        Value oldVal = values.get(v);
                        if (oldVal != val) {                            
                            values.put(v, val);
                        }
                    }
                    return ValueWrapper.newInstance(debugger, val);
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } catch (AbsentInformationException ex) {
            return null;
        } catch (InvalidStackFrameException e) {
            return null;
        }
    }

    public ObjectReferenceWrapper getThisReference() {
        try {
            StackFrame frame = getFrame();
            if (frame != null) {
                ObjectReference ref = frame.thisObject();
                if (ref != null) {
                    return new ThisReferenceWrapper(debugger, ref);
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } catch (RuntimeException e) {
            return null;
        }
    }

    public ReferenceTypeWrapper getTypeReference() {
        try {
            StackFrame frame = getFrame();
            if (frame != null) {
                return debugger.getTypesCache().getReferenceType(frame.location().declaringType());
            } else {
                return null;
            }
        } catch (RuntimeException e) {
            return null;
        }
    }

    public List<LocalVariableWrapper> getLocals() {
        try {
            StackFrame frame = getFrame();
            if (frame != null) {
                List<LocalVariable> vars = new ArrayList<LocalVariable>(frame.visibleVariables());
                List<LocalVariableWrapper> wrappers = new LinkedList<LocalVariableWrapper>();
                synchronized (variables) {
                    for (LocalVariable var : vars) {
                        LocalVariableWrapper wrapper = variables.get(var);
                        if (wrapper == null) {
                            wrapper = new LocalVariableWrapper(this, var);
                            variables.put(var, wrapper);
                        }
                        wrappers.add(wrapper);
                    }
                }
                return wrappers;
            } else {
                return Collections.emptyList();
            }
        } catch (AbsentInformationException ex) {
            return Collections.emptyList();
        } catch (RuntimeException ex) {
            return Collections.emptyList();
        }
    }

    public String getDisplayName() {
        try {
            StackFrame frame = getFrame();
            if (frame != null) {
                Location location = frame.location();

                Method method = location.method();
                String lineNumberInfo = location.lineNumber() > 0 ? String.valueOf(location.lineNumber()) : " line number unavailable";
                if (method != null) {
                    method.declaringType().availableStrata();
                    StringBuilder methodName = new StringBuilder();
                    if (method.isStaticInitializer()) {
                        methodName.append(getClassName(location.declaringType())).append("-<static-initializer> : ").append(lineNumberInfo);
                    } else {
                        methodName.append(getMethodName(method)).
                                append("() : ");
                        if (method.isNative()) {
                            methodName.append("native method");
                        } else {
                            methodName.append(lineNumberInfo);
                        }
                    }
                    return methodName.toString();
                } else {
                    return getClassName(location.declaringType()) + " : " + lineNumberInfo;
                }
            } else {
                return "<invalid frame state>";
            }
        } catch (VMDisconnectedException e) {
            return "<disconnected from vm>";
        } catch (ObjectCollectedException e) {
            return "<Collected Object>";
        } catch (InvalidStackFrameException e) {
            return "<Thread Resumed>";
        }

    }

    private boolean ensureEditorOpened(Jml jml) {
        if (EditorManagerProxy.getCurrentScml() != jml) {
            return EditorsManager.getDefault().open(jml, new OpenInfo(jml, Lookups.fixed()));
        } else {
            return true;
        }
    }

    public Line tryToOpenContext() {
        final Branch branch = debugger.getNameResolver().getBranch();
        if (branch != null) {
            try {
                StackFrame frame = getFrame();
                if (frame != null) {
                    Location location = frame.location();
                    List<String> stratas = location.declaringType().availableStrata();
                    if (stratas.contains("Jml")) {
                        try {
                            final int lineNumber = location.lineNumber("Jml");
                            String methodNameStr = location.sourceName("Jml");
                            LocationInfo info = LineMatcher.decode(methodNameStr);
                            if (info != null && info.definitionPath != null && info.definitionPath.length != 0) {
                                Module module = info.findModule(branch, ERepositorySegmentType.ADS);
                                if (module instanceof AdsModule) {
                                    AdsDefinition root = ((AdsModule) module).getDefinitions().findById(info.definitionPath[0]);
                                    if (root != null) {
                                        Definition target = root.findComponentDefinition(info.definitionPath);
                                        if (target != null && target instanceof IJmlSource) {
                                            final Jml jml = ((IJmlSource) target).getSource(info.suffix1);
                                            if (jml != null) {

//                                                

                                                final boolean[] openResult = new boolean[]{false};
                                                final Runnable worker = new Runnable() {

                                                    @Override
                                                    public void run() {
                                                        openResult[0] = ensureEditorOpened(jml);
                                                    }
                                                };


                                                if (SwingUtilities.isEventDispatchThread()) {
                                                    worker.run();
                                                } else {
                                                    SwingUtilities.invokeAndWait(worker);
                                                }

                                                Line result = null;
                                                if (openResult[0]) {
                                                    long totalSleepTime = 0;

                                                    while (result == null && totalSleepTime < 500) {
                                                        try {
                                                            Thread.sleep(10);
                                                            totalSleepTime += 10;
                                                        } catch (InterruptedException e) {
                                                        }
                                                        result = EditorManagerProxy.getLine(jml, lineNumber - 1);
                                                    }
                                                    return result;

                                                } else {
                                                    return null;
                                                }

                                            }
                                        }
                                    }
                                }
                            }
                        } catch (Throwable ex) {
                            Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
                        }
                    }
                    return KernelSourcePathProvider.getInstance().openSourceFile(location.declaringType().name(), location.lineNumber());
                } else {
                    return null;
                }
            } catch (    VMDisconnectedException | ObjectCollectedException | InvalidStackFrameException ex) {
                Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
            }
            return null;
        } else {
            return null;
        }

    }

    private String getClassName(ReferenceType type) {
        return debugger.getNameResolver().className2DefinitionName(type.name());
    }

    private String getMethodName(Method method) {
        return debugger.getNameResolver().methodName(method.declaringType().name(), method.name());
    }

    public Line getLine() {
        return tryToOpenContext();
    }
}
