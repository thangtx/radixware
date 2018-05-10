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

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsApplicationClassDef;
import org.radixware.kernel.common.defs.ads.module.AdsSearcher;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.src.WriterUtils;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.enums.EAccess;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.types.Pid;
import org.radixware.schemas.xscml.JmlType.Item;


public class JmlTagDbEntity extends Jml.Tag {
    
    public static final char[] UF_OWNER_FIELD_NAME = "____________________$ufOwwer$".toCharArray();

    public static final class Factory {
        
        private Factory(){            
        }

        static JmlTagDbEntity loadFrom(Item.DbEntity xDef) {
            return new JmlTagDbEntity(xDef);
        }

        public static JmlTagDbEntity newInstance(Id entityId, String pidAsStr, boolean checkExistance) {
            return new JmlTagDbEntity(entityId, pidAsStr, checkExistance, false);
        }

        public static JmlTagDbEntity newInstanceForUFOwner(Id entityId, String pidAsStr) {
            return new JmlTagDbEntity(entityId, pidAsStr, false, true);
        }
    }
    
    private String pidAsStr;
    private String title;
    private Id entityId;
    private final boolean checkExistance;
    private final boolean isOwner;

    private JmlTagDbEntity(Item.DbEntity xDef) {
        super(xDef);
        if (xDef != null) {
            entityId = xDef.getEntityId();
            pidAsStr = xDef.getPidAsStr();
            checkExistance = xDef.getCheckExistance();
            isOwner = xDef.getIsUFOwner();
            title = xDef.getTitle();
        } else {
            this.entityId = null;
            this.pidAsStr = null;
            this.checkExistance = false;
            this.isOwner = false;
        }
    }

    private JmlTagDbEntity(Id entityId, String pidAsStr, boolean checkExistance, boolean isOwner) {
        super(null);
        this.entityId = entityId;//entityId.getPrefix() == EDefinitionIdPrefix.ADS_ENTITY_CLASS ? Id.Factory.changePrefix(entityId, EDefinitionIdPrefix.DDS_TABLE) : entityId;
        this.pidAsStr = pidAsStr;
        this.checkExistance = checkExistance;
        this.isOwner = isOwner;
    }

    public boolean isUFOwnerRef() {
        return isOwner;
    }

    public void update(Id entityId, String pidAsStr) {
        this.entityId = entityId;
        this.pidAsStr = pidAsStr;
        setEditState(EEditState.MODIFIED);
    }

    @Override
    public void appendTo(Item item) {
        Item.DbEntity xDef = item.addNewDbEntity();
        appendTo(xDef);
        xDef.setEntityId(entityId);
        xDef.setCheckExistance(checkExistance);
        xDef.setPidAsStr(pidAsStr);
        xDef.setTitle(title);
        if (isOwner) {
            xDef.setIsUFOwner(true);
        }
    }

    @Override
    public String getDisplayName() {
        StringBuilder builder = new StringBuilder();
        if (isOwner) {
            builder.append("OWNER[");
            builder.append(title);
            builder.append("]");
        } else {
            final AdsClassDef target = findClass();

            if (target != null) {
                builder.append(target.getQualifiedName());
            } else {
                builder.append("UNKNOWN");
            }
            builder.append("[");
            builder.append(title);
            builder.append("]");
        }
        return builder.toString();
    }

    @Override
    public void check(IProblemHandler problemHandler, Jml.IHistory h) {
        if (entityId == null) {
            error(problemHandler, "Class not specified");
        } else {
            final AdsClassDef target = findClass();
            if (target == null) {
                error(problemHandler, "Class not found: #" + entityId.toString());
            }
        }        
        if (pidAsStr == null && !isOwner) {
            error(problemHandler, "Object PID not specified");
        }
        
        if (h != null && h.getHistory() != null) {
            DbEntityTagsCheckContext context = (DbEntityTagsCheckContext) h.getHistory().get(DbEntityTagsCheckContext.class);
            if (context != null && !isUFOwnerRef()) {
                Pid pid = new Pid(getTableId(), getPidAsStr());
                LinkedHashSet<JmlTagDbEntity> tags = context.pids2dbTags.get(pid);
                if (tags == null) {
                    tags = new LinkedHashSet<>();
                    context.pids2dbTags.put(pid, tags);
                }
                tags.add(this);
            }
        }
    }

    public Id getTableId() {
        return getTableId(findClass());
    }

    private Id getTableId(AdsClassDef classDef) {
        if (classDef == null) {
            return null;
        }
        if (entityId.getPrefix() == EDefinitionIdPrefix.ADS_APPLICATION_CLASS) {
            Id entityClassId = ((AdsApplicationClassDef) classDef).getEntityId();
            return Id.Factory.changePrefix(entityClassId, EDefinitionIdPrefix.DDS_TABLE);
        } else {
            return Id.Factory.changePrefix(entityId, EDefinitionIdPrefix.DDS_TABLE);
        }
    }

    private DdsTableDef findTable(Id tableId) {
        if (tableId == null) {
            return null;
        }
        Definition def = getOwnerDefinition();
        if (def != null && def instanceof AdsDefinition) {
            return AdsSearcher.Factory.newDdsTableSearcher(def).findById(tableId).get();
        } else {
            return null;
        }
    }

    private AdsClassDef findClass() {
        if (entityId == null) {
            return null;
        }
        Definition def = getOwnerDefinition();
        if (def != null && def instanceof AdsDefinition) {
            return AdsSearcher.Factory.newAdsClassSearcher(((AdsDefinition) def).getModule()).findById(entityId).get();
        } else {
            return null;
        }
    }

    @Override
    public String getToolTip() {
        final StringBuilder sb = new StringBuilder();
        if (isOwner) {
            sb.append("<html><b>Function owner access tag</b>");
        } else {
            sb.append("<html><b>Object access tag</b>");
        }
        sb.append("<br>Class: ");
        AdsClassDef classDef = findClass();
        if (classDef != null) {
            sb.append(classDef.getQualifiedName());
        } else {
            sb.append(" Not found");
        }
        if (entityId.getPrefix() == EDefinitionIdPrefix.ADS_APPLICATION_CLASS) {
            sb.append("<br>Table: ");
            DdsTableDef table = findTable(getTableId(classDef));
            if (table != null) {
                sb.append(table.getQualifiedName());
            } else {
                sb.append(" Not found");
            }
        }
        sb.append("<br>Pid: ");
        sb.append(pidAsStr);
        return sb.toString();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getPidAsStr() {
        return pidAsStr;
    }

    public Id getEntityId() {
        return entityId;
    }    

    @Override
    public JavaSourceSupport getJavaSourceSupport() {
        return new JavaSourceSupport(this) {
            private boolean canUseTyping(AdsClassDef clazz) {
                return clazz != null && clazz.isPublished() && !clazz.getAccessFlags().getAccessMode().isLess(EAccess.PUBLIC);
            }

            @Override
            public CodeWriter getCodeWriter(UsagePurpose purpose) {
                return new JmlTagWriter(this, purpose, JmlTagDbEntity.this) {
                    @Override
                    public boolean writeCode(CodePrinter printer) {
                        AdsDefinition def = (AdsDefinition) getCurrentRoot();
                        if (def == null) {
                            return false;
                        }
                        //final DdsTableDef table = findTable();
                        //if (table == null) {
                        //    return false;
                        //} 
                        super.writeCode(printer);
                        WriterUtils.enterHumanUnreadableBlock(printer);
                        AdsClassDef classDef = AdsSearcher.Factory.newAdsClassSearcher(def.getModule()).findById(entityId).get();
                        Id tableId = getTableId(classDef);
                        boolean canUseTyping = canUseTyping(classDef);
                        if (canUseTyping) {
                            printer.print('(');
                            printer.print('(');
                            AdsType type = classDef.getType(EValType.USER_CLASS, null);
                            writeUsage(printer, type, def);
                            printer.print(')');
                        }

                        if (isOwner) {
                            printer.print(UF_OWNER_FIELD_NAME);
                        } else {

                            WriterUtils.writeServerArteAccessMethodInvocation(def, printer);
                            printer.print(".getEntityObject(new ");
                            printer.print(WriterUtils.RADIX_PID_CLASS_NAME);
                            printer.print('(');
                            WriterUtils.writeServerArteAccessMethodInvocation(def, printer);
                            printer.printComma();
                            WriterUtils.writeIdUsage(printer, tableId);
                            printer.printComma();
                            printer.printStringLiteral(pidAsStr);
                            printer.print("),null,");
                            printer.print(checkExistance);
                            printer.print(")");
                        }
                        if (canUseTyping) {
                            printer.print(')');
                        }
                        WriterUtils.leaveHumanUnreadableBlock(printer);
                        return true;
                    }

                    @Override
                    public void writeUsage(CodePrinter printer) {
                        //do nothing
                    }
                };
            }
        };
    }
    
    public static class DbEntityTagsCheckContext {

        public final LinkedHashMap<Pid, LinkedHashSet<JmlTagDbEntity>> pids2dbTags = new LinkedHashMap<>();
    }
}
