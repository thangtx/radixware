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

import java.sql.Timestamp;
import java.util.Calendar;
import org.radixware.kernel.common.enums.EGender;
import org.radixware.kernel.common.enums.EIsoLanguage;

/**
 * RADIX-3275
 */
public class DateConverter {

    private static String[] sDayRu = {null, "первое", "второе", "третье", "четвертое", "пятое", "шестое", "седьмое", "восьмое", "девятое", "десятое", "одиннадцатое", "двенадцатое", "тринадцатое", "четырнадцатое", "пятнадцатое", "шестнадцатое", "семнадцатое", "восемнадцатое", "девятнадцатое", "двадцатое", null, null, null, null, null, null, null, null, null, "тридцатое"};
    private static String[] sMonthRu = {null, "января", "февраля", "марта", "апреля", "мая", "июня", "июля", "августа", "сентября", "октября", "ноября", "декабря"};
    private static String[] sDigitFRu = {null, "одна", "две"};
    private static String[] sDigitMRu = {null, "первого", "второго", "третьего", "четвертого", "пятого", "шестого", "седьмого", "восьмого", "девятого", "десятого", "одиннадцатого", "двенадцатого", "тринадцатого", "четырнадцатого", "пятнадцатого", "шестнадцатого", "семнадцатого", "восемнадцатого", "девятнадцатого"};
    private static String[] sTenRuR = {null, null, "двадцатого", "тридцатого", "сорокового", "пятидесятого", "шестидесятого", "семидесятого", "восьмидесятого", "девяностого"};
    private static String[] sHundredRuR = {null, "сотого", "двухсотого", "трехсотого", "четырехсотого", "пятисотого", "шестисотого", "семисотого", "восьмисотого", "девятисотого"};
    //
    private static String[] sDayUa = {null, "перше", "друге", "третє", "четверте", "п'яте", "шосте", "сьоме", "восьмое", "дев'яте", "десяте", "одинадцяте", "дванадцяте", "тринадцяте", "чотирнадцяте", "п'ятнадцяте", "шiстнадцяте", "сiмнадцяте", "вiсiмнадцяте", "дев'ятнадцяте", "двадцяте", null, null, null, null, null, null, null, null, null, "тридцяте"};
    private static String[] sMonthUa = {null, "сiчня", "лютого", "березня", "квiтня", "травня", "червня", "липня", "серпня", "вересня", "жовтня", "листопаду", "грудня"};
    private static String[] sDigitFUa = {null, "одна", "двi"};
    private static String[] sDigitMUa = {null, "першого", "другого", "третього", "четвертого", "п'ятого", "шостого", "сьомого", "восьмого", "дев'ятого", "десятого", "одинадцятого", "дванадцятого", "тринадцятого", "чотирнадцятого", "п'ятнадцятого", "шiстнадцятого", "сiмнадцятого", "вiсiмнадцятого", "дев'ятнадцятого"};
    private static String[] sTenUaR = {null, null, "двадцятого ", "тридцятого ", "сорокового", "п'ятидесятого ", "шiстдесятого ", "сiмдесятого ", "вiсiмдесятого", "дев'яностого "};
    //
    private static String[] sDayEn = {null, "first", "second", "third", "fourth", "fifth", "sixth", "seventh", "eighth", "ninth", "tenth", "eleventh", "twelfth", "thirteenth", "fourteenth", "fifteenth", "sixteenth", "seventeenth", "eightteenth", "ninteenth", "twentieth", null, null, null, null, null, null, null, null, null, "thirtieth"};
    private static String[] sMonthEn = {null, "january", "february", "march", "april", "may", "june", "july", "august", "september", "october", "november", "december"};
    private static String[] sTenEn = {null, null, "twenty", "thirty"};

    public static String convertDate(Timestamp date, EIsoLanguage lang) {
        if (lang == EIsoLanguage.RUSSIAN) {
            return convertDateRu(date);
        } else if (lang == EIsoLanguage.UKRAINIAN) {
            return convertDateUa(date);
        } else {
            return convertDateEn(date);
        }
    }

    private static String convertDateRu(Timestamp date) {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date.getTime());
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        
        String result;
        int digit;

        //days
        if (day <= 20 || day == 30) {
            result = sDayRu[day] + ' ';
        } else {
            digit = (int) (day / 10);
            day = day - digit * 10;
            result = NumericConverter.sTenRu[digit] + ' ' + sDayRu[day] + ' ';
        }

        // months
        result += sMonthRu[month] + ' ';
        //years
        if (year == 2000) {
            result += "двухтысячного";
            return result.trim();
        } else {
            digit = (int) (year / 1000);
            if (digit != 0) {
                year = year - digit * 1000;
                result += subConvertNumberRu(digit, EGender.FEMININE);
                int ten = digit % 100;
                digit = digit % 10;
                if (digit == 1 && ten != 11) {
                    result += "тысяча ";
                } else if ((digit == 2 || digit == 3 || digit == 4) && (ten != 12 && ten != 13 && ten != 14)) {
                    result += "тысячи ";
                } else {
                    result += "тысяч ";
                }
            }
        }

        result += subConvertNumberRu(year, EGender.MASCULINE);
        return result.trim();
    }

    private static String convertDateUa(Timestamp date) {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date.getTime());
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        
        String result;
        int digit;

        // days
        if (day <= 20 || day == 30) {
            result = sDayUa[day] + ' ';
        } else {
            digit = (int) (day / 10);
            day = day - digit * 10;
            result = NumericConverter.sTenUa[digit] + ' ' + sDayUa[day] + ' ';
        }

        // months
        result += sMonthUa[month] + ' ';
        // years
        if (year == 2000) {
            result += "двохтисячного";
            return result.trim();
        } else {
            digit = (int) (year / 1000);
            if (digit != 0) {
                year = year - digit * 1000;
                result += subConvertNumberUa(digit, EGender.FEMININE);
                int ten = digit % 100;
                digit = digit % 10;
                if (digit == 1 && ten != 11) {
                    result += "тисяча ";
                } else if ((digit == 2 || digit == 3 || digit == 4) && (ten != 12 && ten != 13 && ten != 14)) {
                    result += "тисячi ";
                } else {
                    result += "тисяч ";
                }
            }
        }

        result += subConvertNumberUa(year, EGender.MASCULINE);
        return result.trim();
    }

    private static String convertDateEn(Timestamp date) {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date.getTime());
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        
        String result;
        int digit;

        // days
        if (day <= 20 || day == 30) {
            result = sDayEn[day] + ' ';
        } else {
            digit = (int) (day / 10);
            day = day - digit * 10;
            result = sTenEn[digit] + ' ' + sDayEn[day] + ' ';
        }
        // months
        result += "of " + sMonthEn[month];
        result += ", " + NumericConverter.convertNumber(year, EIsoLanguage.ENGLISH);
        return result.trim();
    }

    private static String subConvertNumberRu(int number, EGender gender) {
        String result = "";
        int digit = (int) (number / 100);

        if (digit != 0) {
            number = number - digit * 100;
            if (number == 0) {
                result += sHundredRuR[digit] + ' ';
            } else {
                result += NumericConverter.sHundredRu[digit] + ' ';
            }
        }

        if (number >= 20) {
            digit = (int) (number / 10);
            if (digit != 0) {
                number = number - digit * 10;
                if (number == 0) {
                    result += sTenRuR[digit] + ' ';
                } else {
                    result += NumericConverter.sTenRu[digit] + ' ';
                }
            }
        }

        if (number != 0) {
            if (gender == EGender.MASCULINE || number > 2) {
                result += sDigitMRu[number] + ' ';
            } else {
                result += sDigitFRu[number] + ' ';
            }
        }

        return result;
    }

    private static String subConvertNumberUa(int number, EGender pSex) {
        String result = "";
        int digit = (int) (number / 100);

        if (digit != 0) {
            number = number - digit * 100;
            result += NumericConverter.sHundredUa[digit] + ' ';
        }

        if (number >= 20) {
            digit = (int) (number / 10);
            if (digit != 0) {
                number = number - digit * 10;
                if (number == 0) {
                    result += sTenUaR[digit] + ' ';
                } else {
                    result += NumericConverter.sTenUa[digit] + ' ';
                }
            }
        }

        if (number != 0) {
            if (pSex == EGender.MASCULINE || number > 2) {
                result += sDigitMUa[number] + ' ';
            } else {
                result += sDigitFUa[number] + ' ';
            }
        }

        return result;
    }
}
