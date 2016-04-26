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

package org.radixware.kernel.designer.common.annotations;

import java.lang.annotation.Annotation;
import java.util.Set;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import org.openide.filesystems.annotations.LayerBuilder;
import org.openide.filesystems.annotations.LayerGeneratingProcessor;
import org.openide.filesystems.annotations.LayerGenerationException;


public abstract class AbstractRegistrationProcessor extends LayerGeneratingProcessor {

    private final String requiredClass;
    private final String sectionName;
    private final Class<? extends Annotation> requiredAnnotationClass;

    public AbstractRegistrationProcessor(Class<? extends Annotation> requiredAnnotationClass, String requiredClassName, String sectionName) {
        this.requiredClass = requiredClassName;
        this.sectionName = sectionName;
        this.requiredAnnotationClass = requiredAnnotationClass;
    }

    @Override
    protected boolean handleProcess(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) throws LayerGenerationException {
        if (roundEnv.processingOver()) {
            return false;
        }
        for (Element element : roundEnv.getElementsAnnotatedWith(requiredAnnotationClass)) {
            register(element);
        }
        return true;
    }

    private void register(Element element) throws LayerGenerationException {
        final String className;
        final TypeMirror declaredType = processingEnv.getTypeUtils().getDeclaredType(
                processingEnv.getElementUtils().getTypeElement(requiredClass));

        switch (element.getKind()) {
            case CLASS:
                className = processingEnv.getElementUtils().getBinaryName((TypeElement) element).toString();
                if (element.getModifiers().contains(Modifier.ABSTRACT)) {
                    throw new LayerGenerationException(className + " must not be abstract", element);
                }
                if (!element.getModifiers().contains(Modifier.PUBLIC)) {
                    throw new LayerGenerationException(className + " is not public", element);
                }
                boolean hasDefaultConstructor = false;
                for (ExecutableElement constructor : ElementFilter.constructorsIn(element.getEnclosedElements())) {
                    if (constructor.getParameters().isEmpty()) {
                        if (!constructor.getModifiers().contains(Modifier.PUBLIC)) {
                            throw new LayerGenerationException("Default constructor of " + className + " is not public", element);
                        }
                        hasDefaultConstructor = true;
                        break;
                    }
                }
                if (!hasDefaultConstructor) {
                    throw new LayerGenerationException(className + " must have a no-argument constructor", element);
                }

                if (!processingEnv.getTypeUtils().isAssignable(element.asType(), declaredType)) {
                    throw new LayerGenerationException(className + " is not assignable to " + requiredClass, element);
                }

                break;

            default:
                throw new IllegalArgumentException("Annotated element is not class: " + element);

        }

        final int lastDollarPos = className.lastIndexOf('$');
        final int lastDotPos = className.lastIndexOf('.');
        final String radixClassName = className.substring(lastDotPos + 1, (lastDollarPos > 0 ? lastDollarPos : className.length()) - sectionName.length());
        final StringBuilder filePath = new StringBuilder(50);
        filePath.append("RadixWareDesigner/");
        filePath.append(sectionName);
        filePath.append("/");
        filePath.append(radixClassName);
        filePath.append("/");
        filePath.append(className);
        filePath.append(".instance");
        final LayerBuilder.File file = layer(element).file(filePath.toString());
        file.write();
    }
}
