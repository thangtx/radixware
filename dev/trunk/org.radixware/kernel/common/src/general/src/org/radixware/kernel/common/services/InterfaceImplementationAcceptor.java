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

package org.radixware.kernel.common.services;

import java.util.List;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
//import javax.tools.Diagnostic.Kind;


public class InterfaceImplementationAcceptor implements IElementAcceptor {

    private String canonicalInterfaceName;
    private PublicConstructorAcceptor publicConstructorAcceptor = new PublicConstructorAcceptor();

    public InterfaceImplementationAcceptor(String serviceName) {
        this.canonicalInterfaceName = serviceName.replace('$', '.');
    }

    @Override
    public void accept(Element element, ProcessingEnvironment processingEnv) throws AcceptException {
        publicConstructorAcceptor.accept(element, processingEnv);
        TypeElement typeElement = (TypeElement) element;
        TypeElement reqTypeElement = processingEnv.getElementUtils().getTypeElement(canonicalInterfaceName);
        if (reqTypeElement == null) {
            throw new AcceptException(canonicalInterfaceName + "can not be resolved to element");
        }
        if (!isAssignable(typeElement, reqTypeElement, processingEnv)) {
            throw new AcceptException(element.asType().toString() + " is not assignable to " + canonicalInterfaceName);
        }
    }

    private boolean isAssignable(TypeElement e1, TypeElement e2, ProcessingEnvironment processingEnv) {
        if (e1 == null || e2 == null) {
            return false;
        }

        //String message = "Comparing " + e1.getQualifiedName().toString() + " and " + e2.getQualifiedName().toString();
        if (e1.getQualifiedName().equals(e2.getQualifiedName())) {
//            message = message + " result: true";
//            processingEnv.getMessager().printMessage(Kind.OTHER, message);
            return true;
        }

//        message = message + " result: false";
//        processingEnv.getMessager().printMessage(Kind.OTHER, message);

        List<? extends TypeMirror> directSuperTypes = processingEnv.getTypeUtils().directSupertypes(e1.asType());
        if (directSuperTypes != null && !directSuperTypes.isEmpty()) {
            for (TypeMirror superTypeMirror : directSuperTypes) {
                TypeElement superTypeElement = (TypeElement) processingEnv.getTypeUtils().asElement(superTypeMirror);
                if (isAssignable(superTypeElement, e2, processingEnv)) {
                    return true;
                }
            }
        }

        return false;
    }
}
