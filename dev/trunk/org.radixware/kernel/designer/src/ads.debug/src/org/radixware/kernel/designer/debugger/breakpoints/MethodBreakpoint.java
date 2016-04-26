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

import com.sun.jdi.Method;
import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsTransparentMethodDef;
import org.radixware.kernel.common.defs.ads.src.IJavaSource;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport.CodeType;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport.UsagePurpose;
import org.radixware.kernel.common.defs.ads.type.AdsTransparence;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.repository.Segment;
import org.radixware.kernel.common.utils.CharOperations;


public class MethodBreakpoint extends RadixBreakpoint {

    private WeakReference<AdsMethodDef> method;
    static final String TYPE_ID = "METHOD";

    @Override
    String getTypeId() {
        return TYPE_ID;
    }

    MethodBreakpoint(AdsMethodDef method) {
        this.method = new WeakReference<AdsMethodDef>(method);
    }

    public AdsMethodDef getMethod() {
        if (method == null) {
            return null;
        }
        return method.get();
    }

    boolean isSameMethod(AdsMethodDef theMethod) {
        AdsMethodDef m = getMethod();
        if (m == null) {
            return false;
        } else {
            return theMethod == m;
        }
    }

    @Override
    String getConfigStr() {
        AdsMethodDef jmlOwner = getMethod();
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
        BreakpointInfo info = new BreakpointInfo(layer.getURI(), module.getId(), jmlOwner.getIdPath(), jmlOwner.getName(), null);
        return info.save();
    }

    @Override
    public boolean isInvalid() {
        return getMethod() == null;
    }

    public boolean isMatchToMethod(Method method) {
        AdsMethodDef adsMethod = getMethod();
        if (adsMethod == null) {
            return false;
        }
        String matchName;
        if (adsMethod instanceof AdsTransparentMethodDef) {
            matchName = String.valueOf(((AdsTransparentMethodDef) adsMethod).getPublishedMethodName());
        } else {
            matchName = adsMethod.getId().toString();
        }

        if (!matchName.equals(method.name())) {
            return false;
        }
        String methodSignature = method.signature();
        String adsSignature = String.valueOf(adsMethod.getProfile().getSignature(adsMethod.getOwnerClass()));
        if (!methodSignature.equals(adsSignature)) {
            return false;
        }
        return true;
    }

    @Override
    public List<String> getClassName() {
        AdsMethodDef adsMethod = getMethod();
        if (adsMethod == null) {
            return Collections.emptyList();
        }

        AdsClassDef clazz = adsMethod.getOwnerClass();
        AdsTransparence transparence = clazz.getTransparence();
        if (transparence != null && transparence.isTransparent()) {
            return Collections.singletonList(transparence.getPublishedName());
        } else {
            ERuntimeEnvironmentType env = adsMethod.getUsageEnvironment();
            IJavaSource sourceRoot = JavaSourceSupport.findJavaSourceRoot(adsMethod);
            if (sourceRoot != null) {
                if (env == ERuntimeEnvironmentType.COMMON_CLIENT && adsMethod.getOwnerClass().isDual()) {
                    List<String> list = new LinkedList<String>();
                    list.add(String.valueOf(CharOperations.merge(sourceRoot.getJavaSourceSupport().getMainClassName(UsagePurpose.getPurpose(ERuntimeEnvironmentType.EXPLORER, CodeType.EXCUTABLE)), '.')));
                    list.add(String.valueOf(CharOperations.merge(sourceRoot.getJavaSourceSupport().getMainClassName(UsagePurpose.getPurpose(ERuntimeEnvironmentType.WEB, CodeType.EXCUTABLE)), '.')));
                    return list;
                } else {
                    return Collections.singletonList(String.valueOf(CharOperations.merge(sourceRoot.getJavaSourceSupport().getMainClassName(UsagePurpose.getPurpose(env, CodeType.EXCUTABLE)), '.')));
                }
            } else {
                return Collections.emptyList();
            }
        }
    }

    @Override
    public RadixObject getRadixObject() {
        return getMethod();
    }

    @Override
    public String getDisplayName() {
        AdsMethodDef jml = getMethod();
        if (jml != null) {
            return "Method breakpoint at " + jml.getQualifiedName();
        } else {
            return "Invalid line breakpoint";
        }
    }

    @Override
    public String getShortDescription() {
        return null;
    }
}
