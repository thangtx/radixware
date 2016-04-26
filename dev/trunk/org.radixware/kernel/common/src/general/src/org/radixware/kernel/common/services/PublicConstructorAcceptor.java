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

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;


public class PublicConstructorAcceptor implements IElementAcceptor {

    @Override
    public void accept(Element element, ProcessingEnvironment processingEnv) throws AcceptException {
        switch (element.getKind()) {
            case CLASS:
                String className = processingEnv.getElementUtils().getBinaryName((TypeElement) element).toString();
                if (element.getModifiers().contains(Modifier.ABSTRACT)) {
                    throw new AcceptException(className + " must not be abstract");
                }
                if (!element.getModifiers().contains(Modifier.PUBLIC)) {
                    throw new AcceptException(className + " is not public");
                }
                boolean hasDefaultConstructor = false;
                for (ExecutableElement constructor : ElementFilter.constructorsIn(element.getEnclosedElements())) {
                    if (constructor.getParameters().isEmpty()) {
                        if (!constructor.getModifiers().contains(Modifier.PUBLIC)) {
                            throw new AcceptException("Default constructor of " + className + " is not public");
                        }
                        hasDefaultConstructor = true;
                        break;
                    }
                }
                if (!hasDefaultConstructor) {
                    throw new AcceptException(className + " must have a no-argument constructor");
                }

                break;

            default:
                throw new AcceptException("Annotated element is not class: " + element);
        }
    }
}
