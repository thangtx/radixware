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
package org.radixware.kernel.designer.dds.script;

import java.io.OutputStream;
import java.util.Set;
import org.radixware.kernel.common.defs.dds.*;
import org.radixware.kernel.common.enums.EDatabaseType;
import org.radixware.kernel.common.scml.CodePrinter;

/**
 * <p>This interface describes SQL generator scripts functionality</p>
 */
public interface ScriptGenerator {
    /**
     * <p>Get definisiton script generator for the given DddDefinition class<p>
     * @param <T>Class to get script generator for
     * @param definition Class instance to get script generator for
     * @return Script generator for the given DdsDefinition
     */
    <T extends DdsDefinition> IDdsDefinitionScriptGenerator<T> getDefinitionScriptGenerator(T definition);
    
    
    /**
     * <p>Generate compatibility log for the script</p>
     * @param os printer to print log to
     */
    void generateCompatibilityLog(OutputStream os);
    
    /**
     * <p>Generate modification script for the script generator for associated data inside it</p>
     * @param cp code printer to store sql to
     */
    void generateModificationScript(CodePrinter cp);

    /**
     * <p>Generate role script for for associated data inside it</p>
     * @param cp code printer to store sql to
     */
    void generateRunRoleScript(CodePrinter cp);
    
    /**
     * <p>Generate modification script for the script generator for associated data inside it 9except explicitly defined in the set</p>
     * @param cp code printer to store sql to
     * @param exclude exclude the given definitions for generation process
     */
    void generateModificationScript(CodePrinter cp, Set<? extends DdsDefinition> exclude);

    /**
     * <p>Generate modification script for the script generator for associated data inside it 9except explicitly defined in the set</p>
     * @param cp code printer to store sql to
     * @param dbType database type to generate SQL for
     * @param exclude exclude the given definitions for generation process
     */
    void generateModificationScript(CodePrinter cp, EDatabaseType dbType, Set<? extends DdsDefinition> exclude);

    /**
     * <p>Generate drop script for the given entity</p>
     * @param cp code printer to store sql to
     * @param dbType database type to generate SQL for
     * @param definition definition to generate SQL for
     */
    void generateDropScript(CodePrinter cp, EDatabaseType dbType, DdsDefinition definition);

    /**
     * <p>Generate create script for the given entity</p>
     * @param cp code printer to store sql to
     * @param dbType database type to generate SQL for
     * @param definition definition to generate SQL for
     * @param createAfterModifyPK need create after modification primary key script
     */
    void generateCreateScript(CodePrinter cp, EDatabaseType dbType, DdsDefinition definition, boolean createAfterModifyPK);

    /**
     * <p>Generate alter script for the given entity</p>
     * @param cp code printer to store sql to
     * @param dbType database type to generate SQL for
     * @param oldDefinition old definition to generate SQL for
     * @param newDefinition new definition to generate SQL for
     */
    void generateAlterScript(CodePrinter cp, EDatabaseType dbType, DdsDefinition oldDefinition, DdsDefinition newDefinition);


    /**
     * <p>Generate compact modifications script for all supported database types</p>
     * @param cp code printer to store sql to
     * @param exclude exclude the given definitions for generation process
     */
    void generateCompactModificationScript(CodePrinter cp, Set<? extends DdsDefinition> exclude);
}
