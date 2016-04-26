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

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic.Kind;
import javax.tools.FileObject;
import javax.tools.StandardLocation;

/**
 * Abstract class for RadixService registration processors.
 * Don't forget to add your implementation to META-INF/services!
 */
public abstract class AbstractServiceRegistrationProcessor extends AbstractProcessor {

    private ServiceFileWriter writer;

    public AbstractServiceRegistrationProcessor() {
    }

    protected abstract IElementAcceptor getAcceptor();

    protected abstract String getServiceName();

    private void initWriter() {
        if (writer == null) {
            writer = new ServiceFileWriter(processingEnv);
        }
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        //processingEnv.getMessager().printMessage(Kind.NOTE, "Round in " + getClass().getName() + "; processingOver: " + roundEnv.processingOver());

        initWriter();

        if (roundEnv.processingOver()) {
            writer.write();
            return true;
        }

        IElementAcceptor acceptor = getAcceptor();
        for (TypeElement annotation : annotations) {
            for (Element e : roundEnv.getElementsAnnotatedWith(annotation)) {
                try {
                    acceptor.accept(e, processingEnv);
                    writer.addEntry(getServiceName(), (TypeElement) e);
                } catch (AcceptException ex) {
                    processingEnv.getMessager().printMessage(Kind.WARNING, "Can not process annotation for " + e.toString() + ": " + ex.getMessage());
                }
            }
        }
        return true;
    }
}
