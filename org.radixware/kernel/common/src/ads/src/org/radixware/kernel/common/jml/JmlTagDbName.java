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
package org.radixware.kernel.common.jml;

import java.nio.file.Path;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.check.RadixProblem;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.src.WriterUtils;
import org.radixware.kernel.common.defs.dds.*;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.xscml.JmlType;
import org.radixware.schemas.xscml.JmlType.Item;

public class JmlTagDbName extends JmlTagId {

    public static final class Factory {

        public static JmlTagDbName newInstance(DdsTableDef table) {
            return new JmlTagDbName(table.getIdPath());
        }

        public static JmlTagDbName newInstance(DdsColumnDef column) {
            return new JmlTagDbName(column.getIdPath());
        }

        public static JmlTagDbName newInstance(DdsIndexDef index) {
            return new JmlTagDbName(index.getIdPath());
        }

        public static JmlTagDbName newInstance(DdsSequenceDef sequence) {
            return new JmlTagDbName(sequence.getIdPath());
        }

        public static JmlTagDbName newInstance(DdsFunctionDef func) {
            return new JmlTagDbName(func.getIdPath());
        }

        public static JmlTagDbName newInstance(DdsReferenceDef ref) {
            return new JmlTagDbName(ref.getIdPath());
        }

        public static JmlTagDbName newInstance(DdsPackageDef ref) {
            return new JmlTagDbName(ref.getIdPath());
        }

        public static JmlTagDbName newInstance(DdsTypeDef ref) {
            return new JmlTagDbName(ref.getIdPath());
        }
        
        public static JmlTagDbName newInstance(Id[] path) {
            return new JmlTagDbName(path);
        }

        public static JmlTagDbName loadFrom(JmlType.Item.IdReference idRef) {
            return new JmlTagDbName(idRef);
        }
    }

    public JmlTagDbName(Id[] path) {
        super(path);
    }

    protected JmlTagDbName(JmlType.Item.IdReference path) {
        super(path);
    }

    @Override
    public void appendTo(Item item) {
        JmlType.Item.IdReference ref = item.addNewIdReference();
        appendTo(ref);
        ref.setPath(path.asList());
        ref.setDbName(true);
    }

    @Override
    public void check(IProblemHandler problemHandler, Jml.IHistory h) {
        Definition def = basicCheckImpl(problemHandler);
        if (def != null) {
            if (!(def instanceof IDdsDbDefinition)) {
                problemHandler.accept(RadixProblem.Factory.newError(this, "Definition " + def.getQualifiedName() + " is not a database definition"));
            }
        }
//        if (getOwnerJml().getUsageEnvironment() != ESystemComponent.SERVER) {
//            problemHandler.accept(RadixProblem.Factory.newError(this, "DBName tags are allowed in server side code only"));
//        }
    }

    @Override
    public String getDisplayName() {
        Definition def = resolve(getOwnerJml().getOwnerDefinition());
        if (def == null) {
            return "dbName[" + restoreDisplayName("unknown") + "]";
        } else {
            String name;
            if (def instanceof DdsColumnDef || def instanceof DdsIndexDef) {
                name = def.getName();
            } else {
                name = def.getQualifiedName();
            }
            return "dbName[" + rememberDisplayName(name) + "]";
        }
    }

    @Override
    public JavaSourceSupport getJavaSourceSupport() {
        return new JavaSourceSupport(this) {
            @Override
            public CodeWriter getCodeWriter(UsagePurpose purpose) {
                return new JmlTagWriter(null, UsagePurpose.SERVER_META, JmlTagDbName.this) {
                    @Override
                    public boolean writeCode(CodePrinter printer) {
                        super.writeCode(printer);
                        WriterUtils.enterHumanUnreadableBlock(printer);
                        try {
                            final Definition def = resolve(getOwnerJml().getOwnerDefinition());
                            if (!(def instanceof IDdsDbDefinition)) {
                                printer.printError();
                                return false;
                            }

                            if (def instanceof DdsFunctionDef) {
                                final DdsPlSqlObjectDef obj = ((DdsFunctionDef) def).getOwnerPlSqlObject();
                                if (obj == null) {
                                    return false;
                                }
                                printInvocation(printer, obj);
                                printer.print("+\".\"+");
                            }
                            printInvocation(printer, (IDdsDbDefinition) def);
                            return true;
                        } finally {
                            WriterUtils.leaveHumanUnreadableBlock(printer);
                        }
                    }

                    @Override
                    public void writeUsage(CodePrinter printer) {
                        //never use
                    }

                    private void printInvocation(CodePrinter printer, IDdsDbDefinition def) {
                        printer.printStringLiteral(def.getDbName());
                    }
                };
            }
        };
    }
}
