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

package org.radixware.kernel.common.defs.ads.src.enumeration;

import java.util.List;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumDef;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumItemDef;
import org.radixware.kernel.common.defs.ads.src.AbstractDefinitionWriter;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport.CodeWriter;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport.UsagePurpose;
import org.radixware.kernel.common.defs.ads.src.WriterUtils;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import org.radixware.kernel.common.defs.ads.type.RadixType;
import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.exceptions.NoConstItemWithSuchValueError;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.utils.CharOperations;


public class AdsEnumWriter extends AbstractDefinitionWriter<AdsEnumDef> {
    
    private static final char[] INT_ENUM_INTERFACE_NAME = CharOperations.merge(JavaSourceSupport.RADIX_COMMON_TYPES_PACKAGE_NAME, "IKernelIntEnum".toCharArray(), '.');
    private static final char[] CHAR_ENUM_INTERFACE_NAME = CharOperations.merge(JavaSourceSupport.RADIX_COMMON_TYPES_PACKAGE_NAME, "IKernelCharEnum".toCharArray(), '.');
    private static final char[] STR_ENUM_INTERFACE_NAME = CharOperations.merge(JavaSourceSupport.RADIX_COMMON_TYPES_PACKAGE_NAME, "IKernelStrEnum".toCharArray(), '.');
    private static final char[] TITLED_ENUM_INTERFACE_NAME = CharOperations.merge(JavaSourceSupport.RADIX_COMMON_TYPES_PACKAGE_NAME, "ITitledEnum".toCharArray(), '.');
    private static final char[] NO_CONST_ITEM_WITH_SUCH_VALUE_ERROR_NAME = NoConstItemWithSuchValueError.class.getName().toCharArray();
    final static char[] ENUM_META_CLASS_NAME = CharOperations.merge(WriterUtils.META_COMMON_PACKAGE_NAME, "RadEnumDef".toCharArray(), '.');
    private static final char[] ISO_LANG_CLASS_NAME = EIsoLanguage.class.getName().toCharArray();
    private final AdsType resolvedItemType;
    private final EValType itemType;
    
    public AdsEnumWriter(JavaSourceSupport support, AdsEnumDef target, UsagePurpose usagePurpose) {
        super(support, target, usagePurpose);
        resolvedItemType = RadixType.Factory.newInstance(def.getItemType());
        this.itemType = def.getItemType();
    }
    
    @Override
    public boolean writeExecutable(CodePrinter printer) {
        WriterUtils.writePackageDeclaration(printer, def, usagePurpose);
        CodeWriter writer = def.getType(def.getItemType(), null).getJavaSourceSupport().getCodeWriter(UsagePurpose.COMMON_META);
        printer.println();
        if (def.isDeprecated()) {
            printer.println("@Deprecated");
        }
        if (def.isPlatformEnumPublisher() && !def.isExtendable()) {
            printer.print("public class ");
            writeUsage(printer);
            printer.println('{');
            printer.enterBlock();
            writeEnvironmentAccessor(printer);
            printer.leaveBlock();
        } else {
            printer.print("public enum ");
            writeUsage(printer);
            printer.print(" implements ");
            switch (itemType) {
                case INT:
                    printer.print(INT_ENUM_INTERFACE_NAME);
                    break;
                case CHAR:
                    printer.print(CHAR_ENUM_INTERFACE_NAME);
                    break;
                case STR:
                    printer.print(STR_ENUM_INTERFACE_NAME);
                    break;
            }
            printer.printComma();
            printer.print(TITLED_ENUM_INTERFACE_NAME);
            
            String pecn = def.isPlatformEnumPublisher() ? def.getPublishedPlatformEnumName() : null;
            char[] platformEnumClassName = pecn == null ? null : pecn.toCharArray();
            
            printer.enterBlock(1);
            printer.println('{');
            
            
            boolean isFirst = true;
            for (AdsEnumItemDef item : itemsList()) {
                if (isFirst) {
                    isFirst = false;
                } else {
                    printer.printComma();
                    printer.println();
                }
                if (item.isDeprecated()) {
                    printer.println("@Deprecated");
                }
                printer.print(item.getId().toCharArray());
                printer.print('(');
                WriterUtils.writeIdUsage(printer, item.getId());
                printer.print(',');
                printer.printStringLiteral(item.getName());
                printer.printComma();
                
                ValAsStr val = item.getValue();
                
                Object obj = val.toObject(itemType);
                if (obj instanceof Long) {
                    printer.print("Int.valueOf(");
                    printer.print(String.valueOf(obj));
                    printer.print(")");
                } else if (obj instanceof String) {
                    printer.printStringLiteral((String) obj);
                } else if (obj instanceof Character) {
                    printer.print('\'');
                    printer.print(String.valueOf(obj));
                    printer.print('\'');
                } else {
                    WriterUtils.writeNull(printer);
                }
                printer.printComma();
                if (platformEnumClassName != null) {
                    String itemName = item.getPlatformItemName();
                    if (itemName != null) {
                        printer.print(platformEnumClassName);
                        printer.print('.');
                        printer.print(itemName);
                    } else {
                        WriterUtils.writeNull(printer);
                    }
                    printer.printComma();
                }
                
                WriterUtils.writeIdUsage(printer, item.getTitleId());
                printer.print(')');
            }
            printer.leaveBlock(1);
            printer.printlnSemicolon();
            
            printer.enterBlock(1);
            printer.println();
            //write private fields
            printer.print("private final ");
            printer.print(WriterUtils.RADIX_ID_CLASS_NAME);
            printer.println(" id;");
            
            printer.println("private final String name;");
            printer.print("private final ");
            printer.print(WriterUtils.RADIX_ID_CLASS_NAME);
            printer.println(" titleId;");
            printer.print("private ");
            writeUsage(printer, resolvedItemType);
            printer.println(" val;");
            
            
            writeEnvironmentAccessor(printer);
            //write enum constructor
            printer.print("private ");
            writeUsage(printer);
            printer.print('(');
            printer.print(WriterUtils.RADIX_ID_CLASS_NAME);
            printer.println(" id, String name,");
            writeUsage(printer, resolvedItemType);
            printer.enterBlock(1);
            printer.print(" value,");
            
            if (platformEnumClassName != null) {
                printer.print(platformEnumClassName);
                printer.print(" publishedItem,");
            }
            
            printer.print(WriterUtils.RADIX_ID_CLASS_NAME);
            printer.println(" titleId){");
            printer.println("this.id = id;");
            printer.println("this.name = name;");
            printer.println("this.titleId = titleId;");
            if (def.isPlatformEnumPublisher()) {
                printer.println("this.publishedItem = publishedItem;");
            }
            printer.println("this.val = value;");
            printer.leaveBlock(1);
            
            printer.println('}');
            //write getName() method

            printer.println("public String getName(){return name;}");
            if (def.getItemType() == EValType.INT) {
                printer.println("public int intValue(){return getValue().intValue();}");
                printer.println("public long longValue(){return getValue().longValue();}");
                printer.println("public byte byteValue(){return getValue().byteValue();}");
            }


            //write getValue() method
            printer.print("public ");
            writeUsage(printer, resolvedItemType);
            printer.println(" getValue(){return val;}");
            
            printer.print("public boolean isInDomain(");
            printer.print(WriterUtils.RADIX_ID_CLASS_NAME);
            printer.print(" domainId){return getItemMeta(id).getIsInDomain(");
            printer.print(ENV_STORAGE_NAME);
            printer.println(",domainId);}");
            
            printer.print("public boolean isInDomains(");
            printer.print(" java.util.List<");
            printer.print(WriterUtils.RADIX_ID_CLASS_NAME);
            printer.print(">ids){return getItemMeta(id).getIsInDomains(");
            printer.print(ENV_STORAGE_NAME);
            printer.println(",ids);}");


            //write getValue() method
            printer.println("/*Use {@linkplain #getValue()} instead*/");
            printer.println("@Deprecated");
            printer.print("public ");
            writeUsage(printer, resolvedItemType);
            printer.println(" get(){return getValue();}");
            
            printer.println("public String getTitle(){");
            printer.print("\treturn titleId==null?null:");
            WriterUtils.writeNLSInvocation(printer, def.getLocalizingBundleId(), "titleId", def, usagePurpose);
            printer.printlnSemicolon();
            printer.println("}");
            printer.print("public String getTitle(");
            printer.print(ISO_LANG_CLASS_NAME);
            printer.println(" lang){");
            printer.print("\treturn titleId==null?null:");
            WriterUtils.writeNLSInvocation(printer, def.getLocalizingBundleId(), "titleId", "lang", def, usagePurpose);
            printer.printlnSemicolon();
            printer.println("}");
            
            if (platformEnumClassName != null) {
                
                printer.print("private final ");
                printer.print(platformEnumClassName);
                printer.println(" publishedItem;");
                
                printer.print("public static final ");
                printer.print(platformEnumClassName);
                printer.enterBlock(1);
                printer.print(" downcast(");
                printer.print(def.getId().toCharArray());
                printer.println(" item){");
                printer.leaveBlock(1);
                printer.println("return item.publishedItem;");
                printer.println('}');
                
                printer.print("public static final ");
                printer.print(def.getId().toCharArray());
                printer.enterBlock(1);
                printer.print(" valueOf(");
                printer.print(platformEnumClassName);
                printer.println(" pitem){");
                printer.print("for(");
                printer.print(def.getId().toCharArray());
                printer.println(" item : values()){");
                printer.enterBlock();
                printer.println("if(item.publishedItem==pitem)return item;");
                printer.leaveBlock();
                printer.println('}');
                printer.println("return null;");
                printer.leaveBlock(1);
                printer.println('}');
            }
            
            
            printer.print("private ");
            printer.print(AdsEnumItemWriter.ITEM_META_CLASS_NAME);
            
            printer.print(" getItemMeta(");
            printer.print(WriterUtils.RADIX_ID_CLASS_NAME);
            printer.println(" id){");
            printer.enterBlock();
            printer.print(AdsEnumItemWriter.ITEM_META_CLASS_NAME);
            printer.print(" im = (");
            printer.print(AdsEnumItemWriter.ITEM_META_CLASS_NAME);
            printer.print(")");
            writer.writeUsage(printer);
            printer.println(".rdxMeta.getItems().findItemById(id,org.radixware.kernel.common.defs.ExtendableDefinitions.EScope.ALL);");
            printer.println("if(im == null)");
            printer.enterBlock();
            printer.println("throw new org.radixware.kernel.common.exceptions.DefinitionNotFoundError(id);");
            printer.leaveBlock();
            printer.println("else");
            printer.enterBlock();
            printer.println("return im;");
            printer.leaveBlock();
            printer.println("}");
            printer.leaveBlock();
            printer.println();




            //write get form value method

            printer.print("public static final ");
            writeUsage(printer);
            printer.print(" getForValue(final ");
            writeUsage(printer, resolvedItemType);
            printer.enterBlock(1);
            printer.println(" val) {");
            printer.enterBlock(1);
            printer.print("for (");
            writeUsage(printer);
            printer.print(" t : ");
            writeUsage(printer);
            printer.println(".values()) {");
            printer.enterBlock(1);
            printer.println("if (t.getValue() == null && val == null || t.getValue().equals(val)) {");
            printer.print("return t;");
            printer.leaveBlock(1);
            printer.println("}");
            printer.leaveBlock(1);
            printer.println("}");
            printer.print("throw new ");
            printer.print(NO_CONST_ITEM_WITH_SUCH_VALUE_ERROR_NAME);
            printer.print("(\"");
            printer.print(def.getQualifiedName());
            printer.leaveBlock(1);
            printer.print(" has no item with value: \" + String.valueOf(val),val);");
            printer.println('}');
            printer.leaveBlock(1);
            
        }
        
        printer.enterBlock();;
        printer.print("public static final ");
        printer.print(ENUM_META_CLASS_NAME);
        printer.print(" getRadMeta(){ return ");
        writer.writeUsage(printer);
        printer.println(".rdxMeta;}");
        printer.print("@Deprecated\npublic static final ");
        printer.print(ENUM_META_CLASS_NAME);
        
        printer.println(" meta = getRadMeta();");


        // writeMeta(printer);
        writeAddon(printer);
        printer.leaveBlock();
        printer.println('}');
        return true;
    }
    private List<AdsEnumItemDef> itemsList = null;
    
    private List<AdsEnumItemDef> itemsList() {
        if (itemsList == null) {
            itemsList = def.getItems().list(EScope.LOCAL_AND_OVERWRITE);
        }
        return itemsList;
    }
    
    @Override
    protected boolean writeMeta(CodePrinter printer) {
        /*
         * public RadEnumDef( final Id id, final String name, final EValType
         * valType, final IItem[] items)
         */
        WriterUtils.writePackageDeclaration(printer, def, usagePurpose);
        printer.print("public class ");
        writeUsage(printer);
        printer.println("_mi{");
        printer.enterBlock();
        
        
        writeEnvironmentAccessor(printer);
        
        printer.print("public static final ");
        printer.print(ENUM_META_CLASS_NAME);
        printer.println(" rdxMeta;");
        printer.println("static{ ");
        printer.println("@SuppressWarnings(\"deprecation\")");
        
        
        printer.print(AdsEnumItemWriter.ITEM_META_CLASS_NAME);
        printer.enterBlock(3);
        
        printer.println("[] item_meta_arr = ");
        
        List<AdsEnumItemDef> items = itemsList();
        
        new WriterUtils.ObjectArrayWriter<AdsEnumItemDef>(AdsEnumItemWriter.ITEM_META_CLASS_NAME) {
            @Override
            public void writeItemConstructor(CodePrinter printer, AdsEnumItemDef item) {
                item.getJavaSourceSupport().getCodeWriter(UsagePurpose.COMMON_META).writeCode(printer);
            }
        }.write(printer, items);
        printer.leaveBlock(3);
        
        printer.printlnSemicolon();


//        if (!def.isPlatformEnumPublisher() || def.isExtendable()) {
//            for (int i = 0, len = items.size(); i < len; i++) {
//                items.get(i).getJavaSourceSupport().getCodeWriter(UsagePurpose.getPurpose(usagePurpose.getEnvironment(), JavaSourceSupport.CodeType.EXCUTABLE)).writeUsage(printer);
//                //writeUsage(printer,items.get(i));
//                //printer.print(items.get(i).getId());
//                printer.print(".itemMeta = item_meta_arr[");
//                printer.print(i);
//                printer.println("];");
//            }
//        }

        printer.print(" rdxMeta = ");
        printer.print("new ");
        printer.enterBlock(3);
        
        printer.print(ENUM_META_CLASS_NAME);
        printer.print('(');
        WriterUtils.writeIdUsage(printer, def.getId());
        printer.printComma();
        printer.println();
        printer.printStringLiteral(def.getName());
        printer.printComma();
        printer.println();
        WriterUtils.writeEnumFieldInvocation(printer, itemType);
        printer.printComma();
        printer.print(def.isDeprecated());
        printer.printComma();
        printer.leaveBlock(3);
        printer.print("item_meta_arr,");
        printer.print(ENV_STORAGE_NAME);
        printer.println(");");
        printer.println('}');
        printer.leaveBlock();
        printer.println();
        
        printer.println('}');
        
        
        return true;
    }
    
    @Override
    protected boolean writeAddon(CodePrinter printer) {
        printer.enterBlock(1);
        printer.println();
        printer.print("public static class Arr");
        printer.print(" extends ");
        printer.print(JavaSourceSupport.RADIX_COMMON_TYPES_PACKAGE_NAME);
        printer.print(".Arr<");
        writeUsage(printer, def.getType(itemType, null), def);
        printer.print(">");
        printer.enterBlock(1);
        printer.println('{');
        printer.println("public static final long serialVersionUID = 0L;");
        printer.print("public Arr(){super();}\n");
        
        printer.print("public Arr(java.util.Collection<");
        writeUsage(printer, def.getType(itemType, null), def);
        printer.println("> collection){");
        printer.enterBlock();
        printer.println("if(collection==null)throw new NullPointerException();");
        printer.print("for(");
        //writeCode(printer, resolvedItemType);
        writeUsage(printer, def.getType(itemType, null), def);
        printer.println(" item : collection){");
        printer.enterBlock();
        printer.println("add(item);");
        printer.leaveBlock();
        printer.println("}");
        printer.leaveBlock();
        printer.print("}\n");
        
        
        printer.print("public Arr(");
        writeUsage(printer, def.getType(itemType, null), def);
        printer.println("[] array){");
        printer.enterBlock();
        printer.println("if(array==null)throw new NullPointerException();");
        printer.println("for(int i = 0; i < array.length; i ++){");
        printer.enterBlock();
        printer.println("add(array[i]);");
        printer.leaveBlock();
        printer.println("}");
        printer.leaveBlock();
        printer.print("}\n");
        printer.print("public Arr(");
        AdsType enumType = def.getType(def.getItemType(), null);
        AdsType itemArrType = RadixType.Factory.newInstance(def.getItemType().getArrayType());
        writeCode(printer, itemArrType);
        printer.enterBlock(1);
        printer.println(" array){");
        printer.println("if(array == null) throw new NullPointerException();");
        printer.print("for(");
        writeCode(printer, resolvedItemType);
        printer.enterBlock(1);
        printer.println(" item : array){");
        printer.print("add(item==null?null:");
        writeCode(printer, enumType);
        //printer.leaveBlock(1);
        printer.print(".getForValue(item");
        
        switch (def.getItemType()) {
            case STR:
                printer.println("));");
                break;
            case CHAR:
                printer.println(".charValue()));");
                break;
            case INT:
                printer.println(".longValue()));");
                break;
        }
        
        printer.leaveBlock(1);
        printer.println("}");
        printer.leaveBlock(1);
        printer.println("}");
        printer.print("public ");
        printer.print(EValType.class.getName());
        printer.enterBlock(1);
        printer.println(" getItemValType(){ ");
        printer.print("return ");
        WriterUtils.writeEnumFieldInvocation(printer, itemType);
        printer.leaveBlock(1);
        printer.printlnSemicolon();
        //printer.leaveBlock(1);
        printer.println('}');
        printer.leaveBlock(1);
        
        printer.println('}');
        printer.leaveBlock(1);
        return true;
    }
    private static final char[] ENV_STORAGE_NAME = "$env_instance_storage_for_internal_usage$".toCharArray();
    
    private void writeEnvironmentAccessor(CodePrinter printer) {
        WriterUtils.enterHumanUnreadableBlock(printer);
        printer.println("/*Internal arte accessor Used in generated code. Warning: never call this method directly*/");
        printer.println("@SuppressWarnings(\"unused\")");
        printer.print("private static final ");
        printer.print(WriterUtils.RADIX_ENVIRONMENT_CLASS_NAME);
        printer.printSpace();
        printer.print(ENV_STORAGE_NAME);
        printer.println(";");        
        printer.println("static{");
        printer.enterBlock();
        printer.print(ENV_STORAGE_NAME);
        printer.print(" = ");
        printer.print(def.getId());
        printer.print("_mi.class.getClassLoader() instanceof ");
        printer.print(WriterUtils.RADIX_CLASS_LOADER_CLASS_NAME);
        printer.print(" ? ");
        printer.print("((");
        printer.print(WriterUtils.RADIX_CLASS_LOADER_CLASS_NAME);
        printer.print(")");
        printer.print(def.getId());
        printer.print("_mi");        
        printer.println(".class.getClassLoader()).getEnvironment() : null;");
        printer.leaveBlock();
        printer.println("}");
        WriterUtils.leaveHumanUnreadableBlock(printer);
    }
}
