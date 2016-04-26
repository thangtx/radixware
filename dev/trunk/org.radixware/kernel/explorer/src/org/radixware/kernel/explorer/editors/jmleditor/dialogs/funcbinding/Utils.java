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
package org.radixware.kernel.explorer.editors.jmleditor.dialogs.funcbinding;

import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.meta.RadEnumPresentationDef;
import org.radixware.kernel.common.client.types.Pid;
import org.radixware.kernel.common.client.utils.ValueConverter;
import org.radixware.kernel.common.defs.ExtendableDefinitions;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityObjectClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.MethodParameter;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.defs.ads.userfunc.AdsUserFuncDef;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import static org.radixware.kernel.explorer.editors.jmleditor.dialogs.funcbinding.WizardFlow.TargetType.OBJECT;
import static org.radixware.kernel.explorer.editors.jmleditor.dialogs.funcbinding.WizardFlow.TargetType.OWNER;
import static org.radixware.kernel.explorer.editors.jmleditor.dialogs.funcbinding.WizardFlow.TargetType.PARAMETER;
import static org.radixware.kernel.explorer.editors.jmleditor.dialogs.funcbinding.WizardFlow.TargetType.PROPERTY;
import static org.radixware.kernel.explorer.editors.jmleditor.dialogs.funcbinding.WizardFlow.TargetType.VALUE;
import org.radixware.schemas.reports.ParametersBindingType;

public class Utils {

    private static final String STR_NULL = "<NULL>";

    public static EValType convertType(final AdsTypeDeclaration type) {
        EValType valType = type.getTypeId();
        if ((valType == EValType.JAVA_TYPE || valType == EValType.JAVA_CLASS) && (type.getExtStr() != null)) {
            switch (type.getExtStr()) {
                case "char":
                    valType = EValType.CHAR;
                    break;
                case "java.lang.String":
                    valType = EValType.STR;
                    break;
                case "byte":
                case "java.lang.Byte":
                    valType = EValType.STR;
                    break;
                case "int":
                case "long":
                case "short":
                case "java.lang.Integer":
                case "java.lang.Long":
                case "java.lang.Short":
                    valType = EValType.INT;
                    break;
                case "float":
                case "double":
                case "java.lang.Float":
                case "java.lang.Double":
                    valType = EValType.NUM;
                    break;
                case "boolean":
                case "java.lang.Boolean":
                    valType = EValType.BOOL;
                    break;
            }
        }
        return valType;
    }

    public static String getValueAsString(final IClientEnvironment env, final ParametersBindingType.ParameterBinding xBinging, final AdsUserFuncDef userFunc, final AdsTypeDeclaration targetValType) {
        if (xBinging == null) {
            return STR_NULL;
        }
        WizardFlow.TargetType targetType = WizardFlow.TargetType.UNKNOWN;
        if (xBinging.isSetParameter()) {
            targetType = WizardFlow.TargetType.PARAMETER;
        } else {
            if (xBinging.getExternalValue() != null) {
                if (xBinging.getExternalValue().getOwnerPID() != null) {
                    if (xBinging.getExternalValue().getValue() != null) {
                        targetType = WizardFlow.TargetType.PROPERTY;
                    } else {
                        targetType = WizardFlow.TargetType.OBJECT;
                    }
                } else {
                    if (xBinging.getExternalValue().getValue() != null) {
                        targetType = WizardFlow.TargetType.VALUE;
                    }
                }
            }
        }
        switch (targetType) {
            case PARAMETER:
                final AdsMethodDef.Profile profile = userFunc.findProfile();
                final MethodParameter p = profile.findParamById(xBinging.getParameter());
                return p == null ? "<Unknown Parameter>" : p.getName();
            case PROPERTY: {
                if (xBinging.getExternalValue() != null && xBinging.getExternalValue().getValue() != null) {
                    final AdsDefinition definition = AdsUserFuncDef.Lookup.findTopLevelDefinition(userFunc, xBinging.getExternalValue().getOwnerClassId());
                    if (definition instanceof AdsEntityObjectClassDef) {
                        final AdsPropertyDef prop = ((AdsEntityObjectClassDef) definition).getProperties().findById(xBinging.getExternalValue().getValue().getId(), ExtendableDefinitions.EScope.ALL).get();
                        if (prop != null) {
                            final Pid pid = new Pid(((AdsEntityObjectClassDef) definition).getEntityId(), xBinging.getExternalValue().getOwnerPID());
                            try {
                                return pid.getDefaultEntityTitle(env.getEasSession()) + "." + prop.getName();
                            } catch (ServiceClientException | InterruptedException ex) {
                                return "<Unknown object of class " + definition.getQualifiedName() + ">." + prop.getName();
                            }
                        } else {
                            return "<Unknown property>";
                        }
                    } else {
                        return "<Unknown property>";
                    }
                } else {
                    return STR_NULL;
                }
            }
            case OBJECT:
            case OWNER: {
                if (xBinging.getExternalValue() != null) {
                    if (userFunc.getOwnerClassId() == xBinging.getExternalValue().getOwnerClassId() && org.radixware.kernel.common.utils.Utils.equals(userFunc.getOwnerPid(), xBinging.getExternalValue().getOwnerPID())) {
                        return "Calling function owner";
                    }
                    final AdsDefinition definition = AdsUserFuncDef.Lookup.findTopLevelDefinition(userFunc, xBinging.getExternalValue().getOwnerClassId());
                    if (definition instanceof AdsClassDef) {
                        Pid pid = Pid.fromStr(xBinging.getExternalValue().getOwnerPID());
                        if (pid != null) {
                            return pidToStr(pid, env, definition);
                        } else {
                            pid = new Pid(xBinging.getExternalValue().getOwnerClassId(), xBinging.getExternalValue().getOwnerPID(), env.getDefManager());
                            return pidToStr(pid, env, definition);
                        }
                    } else {
                        return "<Unknown object>";
                    }
                } else {
                    return STR_NULL;
                }
            }
            case VALUE: {
                if (xBinging.getExternalValue() != null && xBinging.getExternalValue().getValue() != null) {
                    Object objVal = null;

                    final EValType convertedType = convertType(targetValType);

                    if (convertedType != EValType.USER_CLASS && convertedType != EValType.JAVA_CLASS && convertedType != EValType.JAVA_TYPE) {
                        objVal = ValueConverter.easPropXmlVal2ObjVal(xBinging.getExternalValue().getValue(), convertedType, null);
                    } else {
                        objVal = xBinging.getExternalValue().getValue();
                    }

                    RadEnumPresentationDef enumDef = null;
                    if (targetValType.getPath() != null && targetValType.getPath().getTargetId() != null && targetValType.getPath().getTargetId().getPrefix() == EDefinitionIdPrefix.ADS_ENUMERATION) {
                        enumDef = env.getDefManager().getEnumPresentationDef(targetValType.getPath().getTargetId());
                    }
                    if (objVal instanceof org.radixware.kernel.common.types.Arr) {
                        org.radixware.kernel.common.types.Arr arr = (org.radixware.kernel.common.types.Arr) objVal;
                        StringBuilder sb = new StringBuilder();
                        boolean first = true;
                        for (Object obj : arr) {
                            if (first) {
                                first = false;
                            } else {
                                sb.append(", ");
                            }
                            if (enumDef != null && obj instanceof Comparable) {
                                RadEnumPresentationDef.Item item = enumDef.findItemByValue((Comparable) obj);
                                if (item != null) {
                                    sb.append(item.getName());
                                    continue;
                                }
                            }
                            sb.append(String.valueOf(obj));
                        }
                        return sb.toString();
                    }
                    if (objVal instanceof Comparable && enumDef != null) {
                        RadEnumPresentationDef.Item item = enumDef.findItemByValue((Comparable) objVal);
                        if (item != null) {
                            return item.getName();
                        }
                    }
                    return objVal == null ? STR_NULL : objVal.toString();
                } else {
                    return STR_NULL;
                }
            }

        }
        return STR_NULL;
    }

    private static String pidToStr(final Pid pid, final IClientEnvironment env, final AdsDefinition definition) {
        try {
            return pid.getDefaultEntityTitle(env.getEasSession());
        } catch (ServiceClientException | InterruptedException ex) {
            return "<Unknown object of class " + definition.getQualifiedName() + ">";
        }
    }
}
