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

import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic.Kind;


@SupportedAnnotationTypes(value = "org.radixware.kernel.common.services.ServiceAnnotation")
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class ServiceAnnotationProcessor extends AbstractProcessor {

    private ServiceFileWriter writer;

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        if (writer == null) {
            writer = new ServiceFileWriter(processingEnv);
        }

        if (roundEnv.processingOver()) {
            writer.write();
            //processingEnv.getMessager().printMessage(Kind.OTHER, "Processing over in AnnotationRegistratorProcessor");
            return true;
        }

        //processingEnv.getMessager().printMessage(Kind.OTHER, "Round in AnnotationRegistratorProcessor");

        for (Element annotatedElement : roundEnv.getElementsAnnotatedWith(ServiceAnnotation.class)) {
            processingEnv.getMessager().printMessage(Kind.OTHER, "Processing service annotation on " + annotatedElement);
            if (!(annotatedElement instanceof TypeElement)) {
                processingEnv.getMessager().printMessage(Kind.WARNING, "TypeElement was expected", annotatedElement);
                continue;
            }
            TypeElement typeElement = (TypeElement) annotatedElement;

            String serviceName = annotatedElement.getAnnotation(ServiceAnnotation.class).service();
            processingEnv.getMessager().printMessage(Kind.OTHER, "Service for registration: " + serviceName);
            IElementAcceptor acceptor = new InterfaceImplementationAcceptor(serviceName);

            for (Element targetElement : roundEnv.getElementsAnnotatedWith(typeElement)) {
                processingEnv.getMessager().printMessage(Kind.OTHER, "Processing target element: " + targetElement);
                try {
                    acceptor.accept(targetElement, processingEnv);
                    writer.addEntry(serviceName, (TypeElement) targetElement);
                } catch (AcceptException ex) {
                    processingEnv.getMessager().printMessage(Kind.WARNING, "Can not process annotation for " + targetElement + ": " + ex.getMessage());
                }
            }
        }
        return true;
    }
}
