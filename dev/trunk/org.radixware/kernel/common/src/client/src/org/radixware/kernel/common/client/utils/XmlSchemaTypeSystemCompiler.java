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

package org.radixware.kernel.common.client.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import org.apache.xmlbeans.*;
import org.apache.xmlbeans.impl.common.ResolverUtil;
import org.apache.xmlbeans.impl.common.XmlErrorWatcher;
import org.apache.xmlbeans.impl.config.BindingConfigImpl;
import org.apache.xmlbeans.impl.schema.*;
import org.apache.xmlbeans.impl.xb.xmlconfig.ConfigDocument.Config;
import org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument;
import org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.types.Id;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


public final class XmlSchemaTypeSystemCompiler {

    private static class XmlResolver implements EntityResolver {

        public InputSource resolveEntity(String string, String string1) throws SAXException, IOException {
            return ResolverUtil.getGlobalEntityResolver().resolveEntity(string, string1);
        }
    }

    private static class SchemaLoader implements ResourceLoader {

        public InputStream getResourceAsStream(String string) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void close() {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }
    private final XmlResolver xmlResolver = new XmlResolver();
    private final XmlErrorWatcher errorListener = new XmlErrorWatcher(new ArrayList());
    private final XmlOptions schemaParserOptions = new XmlOptions();
    private final XmlOptions schemaValidationOptions = new XmlOptions();
    private final XmlOptions schemaCompilationOptions = new XmlOptions();

    private XmlSchemaTypeSystemCompiler() {
        schemaParserOptions.setLoadLineNumbers();
        schemaParserOptions.setLoadMessageDigest();
        schemaParserOptions.setEntityResolver(xmlResolver);//???

        schemaValidationOptions.setErrorListener(errorListener);

        schemaCompilationOptions.setCompileNoValidation(); // already validated
        schemaCompilationOptions.setEntityResolver(xmlResolver);//???
    }
    private final static XmlSchemaTypeSystemCompiler INSTANCE = new XmlSchemaTypeSystemCompiler();
    private final static File[] EMPTY_FILES = new File[0];

    private static XmlSchemaTypeSystemCompiler getInstance() {
        return INSTANCE;
    }

    public static SchemaTypeSystem compile(ClassLoader classLoader, final String XmlSchemaAsStr, final List<String> errors) {
        final SchemaTypeSystem result =
                getInstance().load(Id.Factory.newInstance(EDefinitionIdPrefix.XML_SCHEME).toString(),
                XmlSchemaAsStr,
                classLoader);
        if (errors != null) {
            errors.addAll(getInstance().getErrors());
        }
        return result;
    }

    private SchemaTypeSystem load(final String name, final String XmlSchemaAsStr, final ClassLoader classLoader) {
        errorListener.clear();

        final StscState state = StscState.start();
        state.setErrorListener(errorListener);

        final SchemaTypeLoader loader = XmlBeans.typeLoaderForClassLoader(classLoader);
        try {
            final XmlObject schemadoc = loader.parse(XmlSchemaAsStr, null, schemaParserOptions);
            if (schemadoc instanceof SchemaDocument) {
                if (schemadoc.validate(schemaValidationOptions)) {
                    //final SchemaTypeLoader linkTo = SchemaTypeLoaderImpl.build(null, new SchemaLoader(), classLoader);
                    final SchemaTypeLoader linkTo = SchemaTypeLoaderImpl.build(null, null, classLoader);
                    final SchemaTypeSystemCompiler.Parameters params = new SchemaTypeSystemCompiler.Parameters();
                    params.setName(name);
                    params.setSchemas(new Schema[]{((SchemaDocument) schemadoc).getSchema()});
                    params.setConfig(BindingConfigImpl.forConfigDocuments(new Config[0], EMPTY_FILES, EMPTY_FILES));
                    params.setLinkTo(linkTo);
                    params.setOptions(schemaCompilationOptions);
                    params.setErrorListener(errorListener);
                    params.setJavaize(true);//???
                    params.setBaseURI(null);
                    params.setSourcesToCopyMap(new HashMap());
                    params.setSchemasDir(null);

                    return SchemaTypeSystemCompiler.compile(params);
                }
            } else {
                StscState.addError(errorListener, XmlErrorCodes.INVALID_DOCUMENT_TYPE,
                        new Object[]{"schema"}, schemadoc);
            }
        } catch (XmlException ex) {
            errorListener.add(ex.getError());
            return null;
        }
        return null;
    }

    private Collection<String> getErrors() {
        final Collection<String> result = new ArrayList<String>();
        final StringBuilder messageBuilder = new StringBuilder();
        for (Object obj : errorListener) {
            if (obj instanceof XmlError) {
                final XmlError error = (XmlError) obj;
                messageBuilder.setLength(0);
                if (error.getSourceName() != null) {
                    messageBuilder.append("In \'");
                    messageBuilder.append(error.getSourceName());
                    messageBuilder.append("\'");
                    if (error.getLine() > 0) {
                        messageBuilder.append(", ");
                    }

                }
                if (error.getLine() > 0) {
                    messageBuilder.append("line ");
                    messageBuilder.append(((XmlError) obj).getLine());
                }
                if (error.getColumn() > 0) {
                    messageBuilder.append(", column ");
                    messageBuilder.append(error.getColumn());
                }
                messageBuilder.append(" : ");
                messageBuilder.append(error.getMessage());
                result.add(messageBuilder.toString());
            } else if (obj != null) {
                result.add(String.valueOf(obj));
            }
        }
        return result;
    }
}
