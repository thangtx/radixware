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

package org.radixware.kernel.designer.ads.editors.refactoring.properties;

import org.openide.util.actions.SystemAction;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.designer.common.general.refactoring.RefactoringProvider;


public class PropertyRefactoringProvider extends RefactoringProvider {

    
    private final Class<?>[] classes;
    public PropertyRefactoringProvider(RadixObject context, Class<?>... classes) {
        super(context);
        
        this.classes = classes;
    }
    
    public PropertyRefactoringProvider(RadixObject context) {
        this(context, new Class[]{ PropertyPullUpAction.class, PropertyReplaceAction.class });
    }

    @Override
    public Class<? extends SystemAction>[] allowedActions() {
        return (Class<? extends SystemAction>[]) classes;
    }
}
