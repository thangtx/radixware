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

package org.radixware.kernel.common.defs.ads.type;

import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.*;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.src.IJavaSource;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport.CodeWriter;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport.UsagePurpose;
import org.radixware.kernel.common.enums.EAccess;
import org.radixware.kernel.common.enums.EClassType;
import org.radixware.kernel.common.enums.EPropNature;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.utils.events.RadixEvent;
import org.radixware.kernel.common.utils.events.RadixEventSource;
import org.radixware.schemas.adsdef.AccessRules;

/**
 * Реализация модификаторов доступа объекта имеющего отражение в исходном тексте
 * на языке Java <p> Поддерживаются следующие типы модификаторов: <br> <ul>
 * <li>public</li> <li>protected</li> <li>private</li> <li>final</li>
 * <li>abstract</li> </ul> <br> Кроме того поддерживается аннотация
 *
 * @Deprecated
 */
public class AdsAccessFlags extends RadixObject implements IJavaSource {

    /**
     * Фабрика для создания и загрузки объектоа типа {@linkplain AdsAccessFlags}
     */
    public static final class Factory {

        /**
         * Создает новую инстанцию класса {@linkplain AdsAccessFlags} с
         * модификатором public
         */
        public static AdsAccessFlags newInstance(final AdsDefinition context) {
            return new AdsAccessFlags(context, false, false, false);
        }

        public static AdsAccessFlags newCopy(final AdsDefinition context, final AdsAccessFlags source) {
            return new AdsAccessFlags(context, source.isStatic, source.isDeprecated, source.isAbstract);
        }

        /**
         * Создает копию класса {@linkplain AdsAccessFlags} с постоянным
         * модификатором доступа {@code access}
         */
        public static AdsAccessFlags newCopy(final AdsDefinition context, final AdsAccessFlags source, final EAccess access) {
            return new AdsAccessFlags(context, source.isStatic, source.isDeprecated, source.isAbstract) {

                @Override
                public EAccess getAccessMode() {
                    return access;
                }
            };
        }

        /**
         * Восстанавливает инстанцию класса {@linkplain AdsAccessFlags} из xml
         *
         * @return Инстанцию заполненную информацией из xml документа если
         * параметр xDef не null. В противном случае вызывает newInstance();
         */
        public static AdsAccessFlags loadFrom(final AdsDefinition context, final AccessRules xDef) {
            if (xDef == null) {
                return newInstance(context);
            }
            return new AdsAccessFlags(context, xDef.isSetIsStatic() ? xDef.getIsStatic() : false, xDef.isSetIsDeprecated() ? xDef.getIsDeprecated() : false, xDef.isSetIsAbstract() ? xDef.getIsAbstract() : false);
        }
    }
    private RadixEventSource modifiersChangesSupport = null;
    private boolean isStatic;
    private boolean isDeprecated;
    private boolean isAbstract;

    private AdsAccessFlags(AdsDefinition context, boolean isStatic, boolean isDeprecated, boolean isAbstract) {
        setContainer(context);
        this.isDeprecated = isDeprecated;
        this.isStatic = isStatic;
        this.isAbstract = isAbstract;
    }

    public RadixEventSource getAccessFlagsChangesSupport() {
        synchronized (this) {
            if (modifiersChangesSupport == null) {
                modifiersChangesSupport = new RadixEventSource();
            }
            return modifiersChangesSupport;
        }
    }

    @SuppressWarnings("unchecked")
    private void notifyModified() {
        synchronized (this) {
            if (modifiersChangesSupport != null) {
                modifiersChangesSupport.fireEvent(new RadixEvent());
            }
        }
    }

    private AdsDefinition getOwnerDef() {
        return (AdsDefinition) getContainer();
    }

    public boolean isFinal() {
        return getOwnerDef().isFinal();
    }

    public boolean isStatic() {
        final AdsDefinition owner = getOwnerDef();

        if (owner instanceof AdsClassDef) {
            final AdsClassDef cls = (AdsClassDef) owner;
            final EClassType classType = cls.getClassDefType();
            if (classType == EClassType.INTERFACE || classType == EClassType.ENUMERATION) {
                return true;
            }

            if (cls.isNested() && owner.getOwnerDef() instanceof AdsInterfaceClassDef) {
                return true;
            }
        }

        if (isStatic) {
            return true;
        } else {
            return owner instanceof AdsPropertyDef && ((AdsPropertyDef) owner).getNature() == EPropNature.EVENT_CODE;
        }
    }

    public boolean isAbstract() {
        final AdsDefinition ownerDef = getOwnerDef();

        if (ownerDef instanceof AdsInterfaceClassDef) {
            return true;
        }

        if (ownerDef instanceof AdsMethodDef || ownerDef instanceof AdsPropertyDef) {
            final AdsDefinition ownerOwner = ownerDef.getOwnerDef();
            if (ownerOwner instanceof AdsInterfaceClassDef) {
                return true;
            }
        }

        return isAbstract;
    }

    public boolean isDeprecated() {
        // RADIX-7460
        final Module module = getModule();
        if (module != null && module.isDeprecated()) {
            return true;
        }
        
        if (!isDeprecated) {
            if (getOwnerDef() instanceof AdsClassMember) {
                AdsClassDef oc = ((AdsClassMember) getOwnerDef()).getOwnerClass();
                if (oc != null) {
                    return oc.getAccessFlags().isDeprecated;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    public boolean isPublic() {
        return getOwnerDef().getAccessMode() == EAccess.PUBLIC;
    }

    public boolean isPublished() {
        return getOwnerDef().isPublished();
    }

    public boolean isPrivate() {
        return getOwnerDef().getAccessMode() == EAccess.PRIVATE;
    }

    public boolean isProtected() {
        return getOwnerDef().getAccessMode() == EAccess.PROTECTED;
    }

    public void appendTo(AccessRules xDef) {
        if (isAbstract) {
            xDef.setIsAbstract(isAbstract);
        }
        if (isStatic) {
            xDef.setIsStatic(isStatic);
        }
        if (isDeprecated) {
            xDef.setIsDeprecated(isDeprecated);
        }
    }

    public void setAbstract(boolean isAbstract) {
        if (this.isAbstract != isAbstract) {
            this.isAbstract = isAbstract;
            setEditState(EEditState.MODIFIED);
            notifyModified();
        }
    }

    public void setDeprecated(boolean isDeprecated) {
        if (this.isDeprecated != isDeprecated) {
            this.isDeprecated = isDeprecated;
            setEditState(EEditState.MODIFIED);
            notifyModified();
        }
    }

    public void setFinal(boolean isFinal) {
        getOwnerDef().setFinal(isFinal);
        notifyModified();
    }

    public void setStatic(boolean isStatic) {
        if (this.isStatic != isStatic) {
            this.isStatic = isStatic;
            setEditState(EEditState.MODIFIED);
            notifyModified();
        }
    }

    public void setPublic() {
        if (getOwnerDef().getAccessMode() != EAccess.PUBLIC) {
            getOwnerDef().setAccessMode(EAccess.PUBLIC);
            notifyModified();
        }
    }

    public void setPrivate() {
        if (getOwnerDef().getAccessMode() != EAccess.PRIVATE) {
            getOwnerDef().setAccessMode(EAccess.PRIVATE);

            notifyModified();
        }
    }

    public void setProtected() {
        if (getOwnerDef().getAccessMode() != EAccess.PROTECTED) {
            getOwnerDef().setAccessMode(EAccess.PROTECTED);
            notifyModified();
        }
    }

    public EAccess getAccessMode() {
        return getOwnerDef().getAccessMode();
    }

    private class AccWriter extends JavaSourceSupport.CodeWriter {

        public AccWriter(JavaSourceSupport support, UsagePurpose usagePurpose) {
            super(support, usagePurpose);
        }

        @Override
        public boolean writeCode(CodePrinter printer) {
            AdsAccessFlags af = AdsAccessFlags.this;
            if (af.isDeprecated()) {
                printer.println("@Deprecated");
            }
            AdsDefinition owner = af.getOwnerDef();
            EAccess a = getAccessMode();//owner.getAccessMode();
            switch (owner.getDefinitionType()) {
                case CLASS_METHOD:
                    AdsClassDef clazz = ((AdsMethodDef) owner).getOwnerClass();
                    if (clazz instanceof AdsModelClassDef || clazz.getUsageEnvironment() == ERuntimeEnvironmentType.COMMON_CLIENT) {
                        switch (a) {
                            case DEFAULT:
                                a = EAccess.PROTECTED;
                        }

                    }
                    break;
                case CLASS_PROPERTY:
                    if (((AdsPropertyDef) owner).getNature() == EPropNature.PROPERTY_PRESENTATION) {
                        a = EAccess.PUBLIC;
                    }
                    break;
                case CLASS:
                    if (((AdsClassDef) owner).isNested()) {
                        break;
                    }
                default:
                    if (a == EAccess.PRIVATE || a == EAccess.PROTECTED) {
                        a = EAccess.DEFAULT;
                    }
            }

            switch (a) {
                case PUBLIC:
                    printer.print("public ");
                    break;
                case PROTECTED:
                    printer.print("protected ");
                    break;
                case PRIVATE:
                    printer.print("private ");
                    break;
                default:
                    switch (owner.getDefinitionType()) {
                        case CLASS:
                            AdsClassDef clazz = (AdsClassDef) owner;
                            if (clazz.getClassDefType() == EClassType.ALGORITHM) {
                                printer.print("public ");
                                break;
                            } else if (clazz.isNested()) {
                                printer.print(' ');
                                break;
                            }
                        case ENUMERATION:
                        case CLASS_METHOD:
                        case CLASS_PROPERTY:
                            if (owner.getUsageEnvironment() == ERuntimeEnvironmentType.COMMON) {
                                printer.print("public ");
                            } else {
                                printer.print(' ');
                            }
                            break;
                        default:
                            printer.print(' ');
                    }
            }

            if (af.isStatic() && !(getOwnerDef() instanceof AdsInterfaceClassDef) && !(getOwnerDef() instanceof AdsEnumClassDef)) {
                printer.print("static ");
            }
            if (af.isAbstract() && !(getOwnerDef() instanceof AdsInterfaceClassDef)) {
                printer.print("abstract ");
            }
            if (af.isFinal()) {
                printer.print("final ");
            }
            return true;
        }

        @Override
        public void writeUsage(CodePrinter printer) {
            writeCode(printer);
        }
    }

    @Override
    public JavaSourceSupport getJavaSourceSupport() {
        return new JavaSourceSupport(this) {

            @Override
            public CodeWriter getCodeWriter(UsagePurpose purpose) {
                return new AccWriter(this, purpose);
            }
        };
    }

    public String getRadixdocPresentation() {
        StringBuilder sb = new StringBuilder();
        if (isProtected()) {
            sb.append("protected ");
        }

        if (isStatic()) {
            sb.append("static ");
        }

        if (isFinal()) {
            sb.append("final ");
        }

        if (isAbstract()) {
            sb.append("abstract ");
        }
        return sb.toString();
    }
}
