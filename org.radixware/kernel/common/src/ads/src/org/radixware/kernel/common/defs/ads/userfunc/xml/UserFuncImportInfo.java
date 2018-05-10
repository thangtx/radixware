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
package org.radixware.kernel.common.defs.ads.userfunc.xml;

import java.util.ArrayList;
import java.util.List;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodThrowsList;
import org.radixware.kernel.common.defs.ads.clazz.members.MethodParameter;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.adsdef.LocalizingBundleDefinition;
import org.radixware.schemas.adsdef.ParameterDeclaration;
import org.radixware.schemas.adsdef.UserFuncProfile;
import org.radixware.schemas.xscml.JmlFuncProfile;
import org.radixware.schemas.xscml.JmlParameterDeclaration;
import org.radixware.schemas.xscml.JmlType;
import org.w3c.dom.Element;

/**
 *
 * @author npopov
 */
public class UserFuncImportInfo extends ParseInfo {

    private final Id id;
    private final Id classId;
    private final Id methodId;
    private final Id ownerEntityId;
    private final Id ownerClassId;
    private final Id propId;
    private final String ownerPid;
    private final ProfileInfo profInfo;
    private final String profile;
    private final JmlType source;
    private final LocalizingBundleDefinition strings;
    private final String description;
    private final List<Integer> suppressedWarnings;

    public UserFuncImportInfo(Id id, String name, Id classId, Id methodId, Id ownerEntityId, Id ownerClassId, Id propId, String ownerPid, ProfileInfo profInfo, String profile, JmlType source, LocalizingBundleDefinition strings, String description, List<Integer> suppressedWarnings, String xPath, boolean isRoot) {
        super(name, xPath, isRoot);
        this.id = id;
        this.classId = classId;
        this.methodId = methodId;
        this.ownerEntityId = ownerEntityId;
        this.ownerClassId = ownerClassId;
        this.propId = propId;
        this.ownerPid = ownerPid;
        this.profInfo = profInfo;
        this.source = source;
        this.strings = strings;
        this.description = description;
        this.suppressedWarnings = suppressedWarnings;
        this.profile = profile;
    }

    public Id getId() {
        return id;
    }

    @Override
    public String getName() {
        if (profInfo != null) {
            return profInfo.getMethodName();
        }
        if (profile != null) {
            int start = profile.indexOf(' ');
            int end = profile.indexOf("(");
            if (start > 0 && end > 0 && start < end) {
                String name = profile.substring(start + 1, end);
                return name;
            }
        }
        String name = super.getName();
        if (name != null) {
            return name;
        }
        return null;
    }
    
    public String getDescription() {
        return description;
    }

    public JmlType getSource() {
        return source;
    }
    
    public LocalizingBundleDefinition getStrings() {
        return strings;
    }

    public Id getOwnerEntityId() {
        return ownerEntityId;
    }

    public Id getOwnerClassId() {
        return ownerClassId;
    }

    public String getUpOwnerPid() {
        return ownerPid;
    }

    public Id getPropId() {
        return propId;
    }

    public Id getClassId() {
        return classId;
    }

    public Id getMethodId() {
        return methodId;
    }

    public ProfileInfo getUserFuncProfile() {
        return profInfo;
    }

    public List<Integer> getSuppressedWarnings() {
        return suppressedWarnings;
    }

    public final static class ProfileInfo {

        private final String mthName;
        private final AdsTypeDeclaration retType;
        private final List<MethodParameter> params = new ArrayList<>();
        private final List<AdsMethodThrowsList.ThrowsListItem> exceptions = new ArrayList<>();

        public ProfileInfo(JmlFuncProfile xProf) {
            mthName = xProf.getMethodName();
            retType = AdsTypeDeclaration.Factory.loadFrom(xProf.getReturnType());
            if (xProf.getParameters() != null) {
                for (JmlParameterDeclaration xPar : xProf.getParameters().getParameterList()) {
                    params.add(MethodParameter.Factory.newInstance(xPar.getName(),
                            xPar.getDescription(),
                            xPar.getDescriptionId(), AdsTypeDeclaration.Factory.loadFrom(xPar.getType()),
                            xPar.getVariable()));
                }
            }
            if (xProf.getThrownExceptions() != null) {
                for (JmlFuncProfile.ThrownExceptions.Exception xExc : xProf.getThrownExceptions().getExceptionList()) {
                    exceptions.add(AdsMethodThrowsList.ThrowsListItem.Factory.newInstance(
                            AdsTypeDeclaration.Factory.loadFrom(xExc),
                            xExc.getDescription(), xExc.getDescriptionId()));
                }
            }
        }

        public ProfileInfo(UserFuncProfile xProf) {
            mthName = xProf.getMethodName();
            retType = AdsTypeDeclaration.Factory.loadFrom(xProf.getReturnType());
            for (ParameterDeclaration xPar : xProf.getParameters().getParameterList()) {
                params.add(MethodParameter.Factory.newInstance(xPar.getName(),
                        xPar.getDescription(),
                        xPar.getDescriptionId(), AdsTypeDeclaration.Factory.loadFrom(xPar.getType()),
                        xPar.getVariable()));
            }
            for (UserFuncProfile.ThrownExceptions.Exception xExc : xProf.getThrownExceptions().getExceptionList()) {
                exceptions.add(AdsMethodThrowsList.ThrowsListItem.Factory.newInstance(
                        AdsTypeDeclaration.Factory.loadFrom(xExc),
                        xExc.getDescription(), xExc.getDescriptionId()));
            }
        }

        public String getMethodName() {
            return mthName;
        }

        public AdsTypeDeclaration getRetType() {
            return retType;
        }

        public List<MethodParameter> getParams() {
            return params;
        }

        public List<AdsMethodThrowsList.ThrowsListItem> getExceptions() {
            return exceptions;
        }
    }
}
