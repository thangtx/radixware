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

package org.radixware.kernel.common.utils;

import org.radixware.kernel.common.enums.EGender;
import org.radixware.kernel.common.enums.EIsoLanguage;

/**
 * RADIX-3275
 *
 */
public class NumericConverter {

    private static final String const1Ru = "один";
    private static final String const2Ru = "два";
    private static final String const3Ru = "три";
    private static final String[] sName1Ru = {null, "триллион ", "миллиард ", "миллион ", "тысяча "};
    private static final String[] sName2Ru = {null, "триллиона ", "миллиарда ", "миллиона ", "тысячи "};
    private static final String[] sName3Ru = {null, "триллионов ", "миллиардов ", "миллионов ", "тысяч "};
    //
    private static final String[] sName1Ua = {null, "мiльйон ", "тисяча "};
    private static final String[] sName2Ua = {null, "мiльйона ", "тисячi "};
    private static final String[] sName3Ua = {null, "мiльйонiв ", "тисяч "};
    //
    private static final String[] sNameEn = {null, "million ", "thousand "};
    //
    private static final String[] sNamePt = {null, "trilhão ", "bilhão ", "milhão ", "mil "};
    private static final String[] sNamePt1 = {null, "trilhões ", "bilhões ", "milhões "};
    //
    private static final String[] sDigitNRu = {null, "одно", const2Ru};
    private static final String[] sDigitFRu = {null, "одна", "две"};
    private static final String[] sDigitMRu = {null, const1Ru, const2Ru, const3Ru, "четыре", "пять", "шесть", "семь", "восемь", "девять", "десять", "одиннадцать", "двенадцать", "тринадцать", "четырнадцать", "пятнадцать", "шестнадцать", "семнадцать", "восемнадцать", "девятнадцать"};
    static final String[] sTenRu = {null, null, "двадцать", "тридцать", "сорок", "пятьдесят", "шестьдесят", "семьдесят", "восемьдесят", "девяносто"};
    static final String[] sHundredRu = {null, "сто", "двести", "триста", "четыреста", "пятьсот", "шестьсот", "семьсот", "восемьсот", "девятьсот"};
    //
    private static final String[] sDigitNUa = {null, "одне", const2Ru};
    private static final String[] sDigitFUa = {null, "одна", "двi"};
    private static final String[] sDigitMUa = {null, const1Ru, const2Ru, const3Ru, "чотири", "п'ять", "шiсть", "сiм", "вiсiм", "дев'ять", "десять", "одинадцять", "дванадцять", "тринадцять", "чотирнадцять", "п'ятнадцять", "шiстнадцять", "сiмнадцять", "вiсiмнадцять", "дев'ятнадцять"};
    static final String[] sTenUa = {null, null, "двадцять", "тридцять", "сорок", "п'ятдесят", "шiстдесят", "сiмдесят", "вiсiмдесят", "дев'яносто"};
    static final String[] sHundredUa = {null, "сто", "двiстi", "триста", "чотириста", "п'ятсот", "шiстсот", "сiмсот", "вiсiмсот", "дев'ятсот"};
    //
    private static final String[] sDigitEn = {null, "one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten", "eleven", "twelve", "thirteen", "fourteen", "fifteen", "sixteen", "seventeen", "eighteen", "nineteen"};
    private static final String[] sTenEn = {null, null, "twenty", "thirty", "forty", "fifty", "sixty", "seventy", "eighty", "ninety"};
    //
    private static final String[] sDigitFPt = {null, "uma", "duas"};
    private static final String[] sDigitMPt = {null, "um", "dois", "tres", "quatro", "cinco", "seis", "sete", "oito", "nove", "dez", "onze", "doze", "treze", "quatorze", "quinze", "dezesseis", "dezessete", "dezoito", "dezenove"};
    private static final String[] sTenPt = {null, null, "vinte", "trinta", "quarenta", "cinquenta", "sessenta", "setenta", "oitenta", "noventa"};
    private static final String[] sHundredPt = {null, "cento", "duzentos", "trezentos", "quatrocentos", "quinhentos", "seiscentos", "setecentos", "oitocentos", "novecentos"};

    public static String convertNumber(final long number, final EIsoLanguage lang, final EGender gender) {
        if (lang == EIsoLanguage.RUSSIAN) {
            return convertNumberRu(number, gender);
        } else if (lang == EIsoLanguage.UKRAINIAN) {
            return convertNumberUa(number, gender);
        } else if (lang == EIsoLanguage.PORTUGUESE) {
            return convertNumberPt(number, gender);
        } else {
            return convertNumberEn(number);
        }
    }

    public static String convertNumber(final long number, final EIsoLanguage lang) {
        return convertNumber(number, lang, EGender.MASCULINE);
    }

    private static long pow(final long n, final int e) {
        long result = 1;
        for (long i = 1; i <= e; i++) {
            result = result * n;
        }
        return result;
    }

    private static String convertNumberRu(long number, final EGender gender) {
        if (number > 999999999999999L) { // 15
            throw new IllegalArgumentException("Переполнение: " + String.valueOf(number));
        }

        if (number < 0) {
            throw new IllegalArgumentException("Отрицательное: " + String.valueOf(number));
        }

        if (number == 0) {
            return "ноль";
        }

        String result = "";
        final int max = 4;

        for (int i = max; i >= 1; i--) {
            long digit = (long) (number / pow(1000, i));
            if (digit != 0) {
                number = number - digit * pow(1000, i);
                if (i != 1) {
                    result += subConvertNumberRu(digit, EGender.MASCULINE);
                } else {
                    result += subConvertNumberRu(digit, EGender.FEMININE);
                }

                long ten = digit % 100;
                digit = digit % 10;

                if (digit == 1 && ten != 11) {
                    result += sName1Ru[max + 1 - i];
                } else if ((digit == 2 || digit == 3 || digit == 4) && (ten != 12 && ten != 13 && ten != 14)) {
                    result += sName2Ru[max + 1 - i];
                } else {
                    result += sName3Ru[max + 1 - i];
                }
            }
        }

        result += subConvertNumberRu(number, gender);
        return result.trim();
    }

    private static String convertNumberUa(long number, final EGender gender) {
        if (number > 999999999L) { // 9
            throw new IllegalArgumentException("Переполнение: " + String.valueOf(number));
        }

        if (number < 0) {
            throw new IllegalArgumentException("Негативний: " + String.valueOf(number));
        }

        if (number == 0) {
            return "нуль";
        }

        String result = "";
        final int max = 2;

        for (int i = max; i >= 1; i--) {
            long digit = (long) (number / pow(1000, i));
            if (digit != 0) {
                number = number - digit * pow(1000, i) /*1000000000000*/;
                if (i != 1) {
                    result += subConvertNumberUa(digit, EGender.MASCULINE);
                } else {
                    result += subConvertNumberUa(digit, EGender.FEMININE);
                }

                long ten = digit % 100;
                digit = digit % 10;

                if (digit == 1 && ten != 11) {
                    result += sName1Ua[max + 1 - i];
                } else if ((digit == 2 || digit == 3 || digit == 4) && (ten != 12 && ten != 13 && ten != 14)) {
                    result += sName2Ua[max + 1 - i];
                } else {
                    result += sName3Ua[max + 1 - i];
                }
            }
        }

        result += subConvertNumberUa(number, gender);
        return result.trim();
    }

    private static String convertNumberEn(long number) {
        if (number > 999999999L) { // 9
            throw new IllegalArgumentException("Overflow: " + String.valueOf(number));
        }

        if (number < 0) {
            throw new IllegalArgumentException("Negative: " + String.valueOf(number));
        }

        if (number == 0) {
            return "zero";
        }

        String result = "";
        final int max = 2;

        for (int i = max; i >= 1; i--) {
            long digit = (long) (number / pow(1000, i));
            if (digit != 0) {
                number = number - digit * pow(1000, i);
                result += subConvertNumberEn(digit);
                result += sNameEn[max + 1 - i];
            }
        }

        result += subConvertNumberEn(number);
        return result.trim();
    }

    private static String convertNumberPt(long number, final EGender gender) {
        if (number > 999999999L) { // 9
            throw new IllegalArgumentException("Estouro: " + String.valueOf(number));
        }

        if (number < 0) {
            throw new IllegalArgumentException("Negativo: " + String.valueOf(number));
        }

        if (number == 0) {
            return "zero";
        }

        String result = "";
        final int max = 4;

        for (int i = max; i >= 1; i--) {
            long digit = (long) (number / pow(1000, i));
            if (digit != 0) {
                result += isNeedE(result, number) ? "e " : "";
                number = number - digit * pow(1000, i);
                if (!(i == 1 && digit == 1)) {
                    result += subConvertNumberPt(digit, EGender.MASCULINE);
                }
                if(digit > 1 && i>1 ){
                    result += sNamePt1[max + 1 - i];
                }else{
                    result += sNamePt[max + 1 - i];
                }
            }
        }

        if (number != 0) {
            result += isNeedE(result, number) ? "e " : "";
        }
        result += subConvertNumberPt(number, gender);
        return result.trim();
    }

    private static boolean isNeedE(String result, long digit) {
        if (!"".equals(result)) {
            if (digit < 100) {
                return true;
            } else {
                String s = String.valueOf(digit);
                if (s.length() >= 3 && s.substring(0, 3).endsWith("00")) {
                    return true;
                }
            }
        }
        return false;
    }

    private static String subConvertNumberRu(final long number2convert, final EGender gender) {
        long number = number2convert;
        String result = "";
        long digit = (long) (number / 100);

        if (digit != 0) {
            number = number - digit * 100;
            result += sHundredRu[(int) digit] + ' ';
        }

        if (number >= 20) {
            digit = (long) (number / 10);
            if (digit != 0) {
                number = number - digit * 10;
                result += sTenRu[(int) digit] + ' ';
            }
        }

        if (number != 0) {
            if (gender == EGender.MASCULINE || number > 2) {
                result += sDigitMRu[(int) number] + ' ';
            } else if (gender == EGender.FEMININE) {
                result += sDigitFRu[(int) number] + ' ';
            } else {
                result += sDigitNRu[(int) number] + ' ';
            }
        }

        return result;
    }

    private static String subConvertNumberUa(long number, final EGender gender) {
        String result = "";
        long digit = (long) (number / 100);

        if (digit != 0) {
            number = number - digit * 100;
            result += sHundredUa[(int) digit] + ' ';
        }

        if (number >= 20) {
            digit = (long) (number / 10);
            if (digit != 0) {
                number = number - digit * 10;
                result += sTenUa[(int) digit] + ' ';
            }
        }

        if (number != 0) {
            if (gender == EGender.MASCULINE || number > 2) {
                result += sDigitMUa[(int) number] + ' ';
            } else if (gender == EGender.FEMININE) {
                result += sDigitFUa[(int) number] + ' ';
            } else {
                result += sDigitNUa[(int) number] + ' ';
            }
        }
        return result;
    }

    private static String subConvertNumberEn(long number) {
        String result = "";
        long digit = (long) (number / 100);

        if (digit != 0) {
            number = number - digit * 100;
            if (number != 0) {
                result += sDigitEn[(int) digit] + " hundred and ";
            } else {
                result += sDigitEn[(int) digit] + " hundred ";
            }
        }

        if (number >= 20) {
            digit = (long) (number / 10);
            if (digit != 0) {
                number = number - digit * 10;
                if (number != 0) {
                    result += sTenEn[(int) digit] + '-';
                } else {
                    result += sTenEn[(int) digit] + ' ';
                }
            }
        }

        if (number != 0) {
            result += sDigitEn[(int) number] + ' ';
        }

        return result;
    }

    private static String subConvertNumberPt(long number, EGender gender) {
        String result = "";
        long digit = (long) (number / 100);

        if (digit != 0) {
            number = number - digit * 100;
            if (digit == 1 && number == 0) {
                result += " cem ";
            } else {
                result += sHundredPt[(int) digit] + ' ';
            }
            if (number > 0) {
                result += "e ";
            }
        }

        if (number >= 20) {
            digit = (long) (number / 10);
            if (digit != 0) {
                number = number - digit * 10;
                result += sTenPt[(int) digit] + ' ';
                if (number > 0) {
                    result += "e ";
                }
            }
        }

        if (number != 0) {
            if (gender == EGender.FEMININE && number <= 2) {
                result += sDigitFPt[(int) number] + ' ';
            } else {
                result += sDigitMPt[(int) number] + ' ';
            }
        }

        return result;
    }
}
