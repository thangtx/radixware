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
package org.radixware.kernel.common.builder.check.common;

import java.sql.Connection;

public class CheckOptions {

    private boolean checkSqlClassQuerySyntax;
    private boolean checkModuleDependences;
    private boolean checkAllOvrPathes;
    private Connection useDbConnection;
    private int maxSqlQueryVariants;
    private boolean checkDocumentation;
    private boolean checkUDSRelatedDocumentationOnly;

    public CheckOptions() {
    }

    public CheckOptions(boolean checkSqlClassQuerySyntax, boolean checkAllOvrPathes, Connection useDbConnection) {
        this.checkSqlClassQuerySyntax = checkSqlClassQuerySyntax;
        this.checkAllOvrPathes = checkAllOvrPathes;
        this.useDbConnection = useDbConnection;        
    }

    public boolean isCheckSqlClassQuerySyntax() {
        return checkSqlClassQuerySyntax;
    }

    public int getMaxSqlQueryVariants() {
        return maxSqlQueryVariants;
    }

    public void setMaxSqlQueryVariants(int maxSqlQueryVariants) {
        this.maxSqlQueryVariants = maxSqlQueryVariants;
    }

    public void setCheckSqlClassQuerySyntax(boolean checkSqlClassQuerySyntax) {
        this.checkSqlClassQuerySyntax = checkSqlClassQuerySyntax;
    }

    public boolean isCheckAllOvrPathes() {
        return checkAllOvrPathes;
    }

    public boolean isCheckModuleDependences() {
        return checkModuleDependences;
    }

    public void setCheckModuleDependences(boolean check) {
        checkModuleDependences = check;
    }

    public void setCheckAllOvrPathes(boolean checkAllOvrPathes) {
        this.checkAllOvrPathes = checkAllOvrPathes;
    }

    public Connection getDbConnection() {
        return useDbConnection;
    }

    public void setDbConnection(Connection useDbConnection) {
        this.useDbConnection = useDbConnection;
    }

    public void accept(CheckOptions options) {
        if (options != null) {
            this.checkAllOvrPathes = options.checkAllOvrPathes;
            this.checkSqlClassQuerySyntax = options.checkSqlClassQuerySyntax;
            this.useDbConnection = options.useDbConnection;
            this.maxSqlQueryVariants = options.maxSqlQueryVariants;
            this.checkModuleDependences = options.checkModuleDependences;
            this.checkDocumentation = options.checkDocumentation;
            this.checkUDSRelatedDocumentationOnly = options.checkUDSRelatedDocumentationOnly;
        }
    }

    public Connection getUseDbConnection() {
        return useDbConnection;
    }

    public void setUseDbConnection(Connection useDbConnection) {
        this.useDbConnection = useDbConnection;
    }

    public boolean isCheckDocumentation() {
        return checkDocumentation;
    }

    public void setCheckDocumentation(boolean checkDocumentation) {
        this.checkDocumentation = checkDocumentation;
    }

    public boolean isCheckUDSRelatedDocumentationOnly() {
        return checkUDSRelatedDocumentationOnly;
    }

    public void setCheckUDSRelatedDocumentationOnly(boolean checkUDSRelatedDocumentationOnly) {
        this.checkUDSRelatedDocumentationOnly = checkUDSRelatedDocumentationOnly;
    }

}
