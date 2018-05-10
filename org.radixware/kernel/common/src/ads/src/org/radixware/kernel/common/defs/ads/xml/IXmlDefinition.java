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

package org.radixware.kernel.common.defs.ads.xml;

import java.util.Collection;
import java.util.List;
import org.apache.xmlbeans.XmlObject;

import org.radixware.kernel.common.defs.ads.src.IJavaSource;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.defs.ads.src.xml.AdsXmlJavaSourceSupport;
import org.radixware.kernel.common.defs.ads.src.xml.XBeansTypeSystem;
import org.radixware.kernel.common.defs.ads.type.IAdsTypeSource;


public interface IXmlDefinition extends IJavaSource, IAdsTypeSource, IPureXmlDefinition {

    String getTargetNamespace();

    List<IXmlDefinition> getImportedDefinitions();

    List<String> getImportedNamespaces();

    XmlObject getXmlContent();

    boolean isTransparent();

    String getSchemaFileType();

    XmlObject getXmlDocument();

    ERuntimeEnvironmentType getTargetEnvironment();

    @Override
    AdsXmlJavaSourceSupport getJavaSourceSupport();

    Collection<String> getSchemaTypeList();

    String getJavaPackageName(boolean isHumanReadable);

    boolean isReadOnly();

    XBeansTypeSystem getSchemaTypeSystem();
}
