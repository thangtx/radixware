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
package org.radixware.kernel.common.defs.ads.src.clazz.presentation;

import java.util.List;
import org.radixware.kernel.common.defs.ads.clazz.presentation.editmask.EditMask;
import org.radixware.kernel.common.defs.ads.clazz.presentation.editmask.EditMaskDateTime;
import org.radixware.kernel.common.defs.ads.clazz.presentation.editmask.EditMaskEnum;
import org.radixware.kernel.common.defs.ads.clazz.presentation.editmask.EditMaskInt;
import org.radixware.kernel.common.defs.ads.clazz.presentation.editmask.EditMaskFilePath;
import org.radixware.kernel.common.defs.ads.clazz.presentation.editmask.EditMaskList;
import org.radixware.kernel.common.defs.ads.clazz.presentation.editmask.EditMaskList.Item;
import org.radixware.kernel.common.defs.ads.clazz.presentation.editmask.EditMaskNum;
import org.radixware.kernel.common.defs.ads.clazz.presentation.editmask.EditMaskStr;
import org.radixware.kernel.common.defs.ads.clazz.presentation.editmask.EditMaskBool;
import org.radixware.kernel.common.defs.ads.clazz.presentation.editmask.EditMaskTimeInterval;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport.UsagePurpose;
import org.radixware.kernel.common.defs.ads.src.RadixObjectWriter;
import org.radixware.kernel.common.defs.ads.src.WriterUtils;
import org.radixware.kernel.common.enums.ECompatibleTypesForBool;
import org.radixware.kernel.common.enums.EDateTimeStyle;
import org.radixware.kernel.common.enums.EFileSelectionMode;
import org.radixware.kernel.common.enums.EMimeType;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.CharOperations;

public class EditMaskWriter extends RadixObjectWriter<EditMask> {

    private static final char[] PACKAGE_NAME = CharOperations.merge(WriterUtils.PRESENTATIONS_META_EXPLORER_PACKAGE_NAME, "mask".toCharArray(), '.');
    //private static final char[] PACKAGE_NAME_FOR_MASK_IMPL = "org.radixware.kernel.common.client.meta.mask".toCharArray();
    private static final char[] LIST_ITEM_CLASS_NAME = CharOperations.merge(PACKAGE_NAME, "EditMaskList.Item".toCharArray(), '.');
    private static final char[] VALIDATOR_FACTORY_ACESSOR_NAME = CharOperations.merge(PACKAGE_NAME, "validators.ValidatorsFactory".toCharArray(), '.');

    public EditMaskWriter(JavaSourceSupport support, EditMask target, UsagePurpose usagePurpose) {
        super(support, target, usagePurpose);
    }

    @Override
    public void writeUsage(CodePrinter printer) {
        //do nothing
    }

    @Override
    protected boolean writeMeta(CodePrinter printer) {
        switch (usagePurpose.getEnvironment()) {
            case WEB:
            case EXPLORER:
                printer.print("new ");
                printer.print(PACKAGE_NAME);
                switch (def.getType()) {
                    case DATE_TIME:

                        /**
                         * public EditMaskDateTime(String mask,//маска в формате
                         * qt Timestamp minValue, Timestamp maxValue )
                         */
                        printer.print(".EditMaskDateTime(");
                        EditMaskDateTime dt = (EditMaskDateTime) def;

                        if (dt.getDateStyle() == EDateTimeStyle.CUSTOM && dt.getTimeStyle() == EDateTimeStyle.CUSTOM) {
                            printer.printStringLiteral(dt.getMask());
                        } else {
                            WriterUtils.writeEnumFieldInvocation(printer, dt.getDateStyle());
                            printer.printComma();
                            WriterUtils.writeEnumFieldInvocation(printer, dt.getTimeStyle());
                        }
                        printer.printComma();
                        WriterUtils.writeTimestamp(printer, dt.getMinValue());
                        printer.printComma();
                        WriterUtils.writeTimestamp(printer, dt.getMaxValue());
                        break;

                    case BOOL:
                        printer.print(".EditMaskBool(");
                        EditMaskBool bool = (EditMaskBool) def;

                        //String trueVal = "\"" + bool.getTrueValueAsStr() + "\"";
                        // String falseVal = "\"" + bool.getFalseValueAsStr() + "\"";
                        //  String trueTitle = "\"" + bool.getTrueTitle() + "\"";
                        // String falseTitle = "\"" + bool.getFalseTitle() + "\"";
                        boolean valueTitleVis = bool.isValueTitleVisible();
                        ECompatibleTypesForBool type = bool.getCompatibleType();

                        if (bool.getTrueValueAsStr() == null) {
                            WriterUtils.writeNull(printer);
                        } else {
                            printer.printStringLiteral(bool.getTrueValueAsStr());
                        }
                        printer.printComma();

                        if (bool.getFalseValueAsStr() == null) {
                            WriterUtils.writeNull(printer);
                        } else {
                            printer.printStringLiteral(bool.getFalseValueAsStr());
                        }
                        printer.printComma();
                        if (type == null) {
                            WriterUtils.writeEnumFieldInvocation(printer, ECompatibleTypesForBool.DEFAULT);
                        } else {
                            WriterUtils.writeEnumFieldInvocation(printer, type);
                        }
                        printer.printComma();

                        if (bool.getTrueTitleId() != null) {
                            WriterUtils.writeNull(printer);
                        } else {
                            printer.printStringLiteral(bool.getTrueTitle());
                        }

                        printer.printComma();

                        if (bool.getFalseTitleId() != null) {
                            WriterUtils.writeNull(printer);
                        } else {
                            printer.printStringLiteral(bool.getFalseTitle());
                        }
                        printer.printComma();
                        WriterUtils.writeIdUsage(printer, bool.getOwnerDef().findTopLevelDef().getId());
                        printer.printComma();
                        WriterUtils.writeIdUsage(printer, bool.getTrueTitleId());
                        printer.printComma();
                        WriterUtils.writeIdUsage(printer, bool.getFalseTitleId());
                        printer.printComma();
                        WriterUtils.writeBoolean(printer, valueTitleVis);
                        break;
                    case ENUM:
                        printer.print(".EditMaskConstSet(");
                        EditMaskEnum ee = (EditMaskEnum) def;
                        /*
                         * public EditMaskConstSet(Id constSetId,
                         * EEditMaskEnumOrder order, EEditMaskEnumCorrection
                         * correction, Id[] correctionItems)
                         */
                        WriterUtils.writeIdUsage(printer, ee.getEnumId());
                        printer.printComma();
                        WriterUtils.writeEnumFieldInvocation(printer, ee.getOrderBy());
                        printer.printComma();
                        WriterUtils.writeEnumFieldInvocation(printer, ee.getCorrection());
                        printer.printComma();
                        WriterUtils.writeIdArrayUsage(printer, ee.getCorrectionItems());
                        break;
                    case INT:
                        /*
                         * public EditMaskInt(	long minValue, long maxValue,
                         * byte minLength, Character padChar, long stepSize,
                         * Character triadDelimeter, byte numberBase )
                         */
                        printer.print(".EditMaskInt(");
                        EditMaskInt ei = (EditMaskInt) def;
                        if (ei.getMinValue() == null) {
                            WriterUtils.writeNull(printer);
                        } else {
                            printer.print(ei.getMinValue());
                            printer.print('L');
                        }
                        printer.printComma();
                        if (ei.getMaxValue() == null) {
                            WriterUtils.writeNull(printer);
                        } else {
                            printer.print(ei.getMaxValue());
                            printer.print('L');
                        }

                        printer.printComma();
                        printer.print("(byte)");
                        if (ei.getMinLength() == null) {
                            printer.print(-1);
                        } else {
                            printer.print(ei.getMinLength());
                        }
                        printer.printComma();
                        WriterUtils.writeCharacter(printer, ei.getPadChar());
                        printer.printComma();
                        printer.print(ei.getStepSize());
                        printer.print('L');
                        printer.printComma();
                        WriterUtils.writeEnumFieldInvocation(printer, ei.getTriadDelimeterType());
                        printer.printComma();
                        WriterUtils.writeCharacter(printer, ei.getTriadDelimiter());
                        printer.printComma();
                        printer.print("(byte)");
                        printer.print(ei.getNumberBase());
                        break;
                    case LIST:
                        /**
                         * public EditMaskList(List<Item> items) public
                         * Item(String title,Object value)
                         */
                        printer.print(".EditMaskList(");
                        EditMaskList list = (EditMaskList) def;
                        final Id titleOwnerId = list.getOwnerDef().getLocalizingBundleId();
                        List<EditMaskList.Item> items = list.getItems();
                        if (!items.isEmpty()) {
                            final EValType ownerType = list.getContextValType();
                            new WriterUtils.SameObjectArrayWriter<EditMaskList.Item>(LIST_ITEM_CLASS_NAME) {
                                @Override
                                public void writeItemConstructorParams(CodePrinter printer, Item item) {
                                    if (item.getTitleId() != null) {
                                        WriterUtils.writeIdUsage(printer, titleOwnerId);
                                        printer.printComma();
                                        WriterUtils.writeIdUsage(printer, item.getTitleId());
                                    } else {
                                        printer.printStringLiteral(item.getTitle());
                                    }
                                    printer.printComma();
                                    //edited by yremizov:
                                    //old code
                                    //WriterUtils.writeLoadFromRadixValAsStr(printer, item.getValue(), ownerType);
                                    //new code:
                                    if (ownerType.isArrayType()) {
                                        WriterUtils.writeLoadFromRadixValAsStr(printer, item.getValue(), ownerType.getArrayItemType());
                                    } else {
                                        WriterUtils.writeLoadFromRadixValAsStr(printer, item.getValue(), ownerType);
                                    }
                                }
                            }.write(printer, items);
                        }
                        break;
                    case NUM:
                        /*
                         * public EditMaskNum(	BigDecimal minValue, BigDecimal
                         * maxValue, long scale,	//Масштаб Character
                         * triadDelimeter,	//Разделитель разрядов byte precision
                         * //Точность )
                         */
                        printer.print(".EditMaskNum(");
                        EditMaskNum en = (EditMaskNum) def;
                        WriterUtils.writeBigDecimal(printer, en.getMinValue());
                        printer.printComma();
                        WriterUtils.writeBigDecimal(printer, en.getMaxValue());
                        printer.printComma();
                        printer.print(en.getScale());
                        printer.print('L');
                        printer.printComma();
                        WriterUtils.writeEnumFieldInvocation(printer, en.getTriadDelimeterType());
                        printer.printComma();
                        WriterUtils.writeCharacter(printer, en.getTriadDelimiter());
                        printer.printComma();
                        WriterUtils.writeCharacter(printer, en.getDecimalDelimiter());
                        printer.printComma();
                        printer.print("(byte)");
                        if (en.getPrecision() == null) {
                            printer.print(-1);
                        } else {
                            printer.print(en.getPrecision());
                        }
                        break;
                    case STR:
                        /*
                         * public EditMaskStr(	//qt-маска ввода. Если задан
                         * значение inputMaskStringId игнорируется final String
                         * inputMask, final boolean isPassword, final int
                         * maximumLen )
                         */
                        printer.print(".EditMaskStr(");

                        EditMaskStr es = (EditMaskStr) def;

                        EditMaskStr.ValidatorDef validator = es.getValidator();
                        switch (validator.getType()) {
                            case INT:
                                printer.print(VALIDATOR_FACTORY_ACESSOR_NAME);
                                printer.print(".createLongValidator(");
                                EditMaskStr.IntValidatorDef iv = (EditMaskStr.IntValidatorDef) validator;
                                if (iv.getMinValue() == null) {
                                    WriterUtils.writeNull(printer);
                                } else {
                                    printer.print(iv.getMinValue());
                                    printer.print('L');
                                }
                                printer.printComma();
                                if (iv.getMaxValue() == null) {
                                    WriterUtils.writeNull(printer);
                                } else {
                                    printer.print(iv.getMaxValue());
                                    printer.print('L');
                                }

                                printer.print(')');
                                break;
                            case NUM:
                                printer.print(VALIDATOR_FACTORY_ACESSOR_NAME);
                                printer.print(".createBigDecimalValidator(");
                                EditMaskStr.NumValidatorDef nv = (EditMaskStr.NumValidatorDef) validator;
                                WriterUtils.writeBigDecimal(printer, nv.getMinValue());
                                printer.printComma();
                                WriterUtils.writeBigDecimal(printer, nv.getMaxValue());
                                printer.printComma();
                                printer.print(nv.getPrecision());
                                printer.print(')');
                                break;
                            case REGEX:
                                printer.print(VALIDATOR_FACTORY_ACESSOR_NAME);
                                printer.print(".createRegExpValidator(");
                                EditMaskStr.RegexValidatorDef rv = (EditMaskStr.RegexValidatorDef) validator;
                                printer.printStringLiteral(rv.getRegex());
                                printer.printComma();
                                printer.print(rv.isMatchCase());
                                printer.print(')');
                                break;
                            default://simple
                                EditMaskStr.DefaultValidatorDef dv = (EditMaskStr.DefaultValidatorDef) validator;
                                String mask = dv.getMask();
                                if (dv.isNoBlanckChar()) {
                                    mask += ";\u0000";
                                }
                                printer.printStringLiteral(mask);
                                printer.printComma();
                                printer.print(dv.isKeepSeparators());
                                break;
                        }

                        printer.printComma();
                        printer.print(es.isPassword());
                        printer.printComma();
                        printer.print(es.getMaxLen() == null ? -1 : es.getMaxLen());
                        printer.printComma();
                        printer.print(es.isAllowEmptyString());
                        break;
                    case TIME_INTERVAL:
                        /*
                         * public EditMaskTimeInterval(long scale, String mask,
                         * long minValue, long maxValue)
                         */
                        printer.print(".EditMaskTimeInterval(");
                        EditMaskTimeInterval ti = (EditMaskTimeInterval) def;
                        if (ti.getScale() == null) {
                            //changed by yremizov:
                            //                            WriterUtils.writeNull(printer);
                            printer.print(EditMaskTimeInterval.Scale.SECOND.longValue());
                        } else {
                            printer.print(ti.getScale().longValue());
                        }
                        printer.print('L');
                        printer.printComma();
                        printer.printStringLiteral(ti.getMask());
                        printer.printComma();
                        if (ti.getMinValue() == null) {
                            WriterUtils.writeNull(printer);
                        } else {
                            printer.print(ti.getMinValue());
                            printer.print('L');
                        }
                        printer.printComma();
                        if (ti.getMaxValue() == null) {
                            WriterUtils.writeNull(printer);
                        } else {
                            printer.print(ti.getMaxValue());
                            printer.print('L');
                        }
                        break;
                    case FILE_PATH:
                        printer.print(".EditMaskFilePath(");
                        EditMaskFilePath filePathMask = (EditMaskFilePath) def;
                        EFileSelectionMode mode = filePathMask.getSelectionMode();
                        boolean handleInput = filePathMask.getHandleInputAvailable();
                        EMimeType mimeType = filePathMask.getMimeType();

                        boolean storeLastPathInconfig = filePathMask.getStoreLastPathInConfig();
                        String title = "\"" + filePathMask.getFileDialogTitle() + "\"";
                        boolean checkIfPathExists = filePathMask.getCheckIfPathExists();

                        if (mode != null) {
                            WriterUtils.writeEnumFieldInvocation(printer, mode);
                        } else {
                            WriterUtils.writeEnumFieldInvocation(printer, EFileSelectionMode.SELECT_FILE);
                        }
                        printer.printComma();
                        printer.print(handleInput);
                        printer.printComma();
                        WriterUtils.writeEnumFieldInvocation(printer, mimeType);
                        printer.printComma();
                        printer.print(storeLastPathInconfig);
                        printer.printComma();
                        if (title != null) {
                            printer.print(title);
                        } else {
                            printer.print("\"File Dialog\"");
                        }
                        printer.printComma();
                        printer.print(checkIfPathExists);

                        break;
                    default:
                        printer.print(".EditMaskNone(");
                        /**
                         * EditMaskNone
                         */
                        return false;
                }
                printer.print(')');
                return true;
            default:
                return false;
        }
    }
}
