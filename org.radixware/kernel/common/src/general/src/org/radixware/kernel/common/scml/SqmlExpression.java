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

package org.radixware.kernel.common.scml;

import org.radixware.schemas.xscml.Sqml;
import org.radixware.schemas.xscml.SqmlDocument;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlString;
import org.radixware.kernel.common.enums.EPidTranslationMode;
import org.radixware.kernel.common.types.Id;

/**
 * Класс позволяет cоставить sql-выражение из основных инструкций языка sql
 * и получить его в виде xml.
 */
public class SqmlExpression {

    private enum SqlLiteralValueEnum {

        EQUAL(" = "),
        NOT_EQUAL(" <> "),
        GREATER(" > "),
        GREATER_OR_EQUAL(" >= "),
        LESS(" < "),
        LESS_OR_EQUAL(" <= "),
        AND(" and "),
        OR(" or "),
        IN(" in "),
        NOT(" not "),
        NOT_IN(" not in "),
        IS_NULL(" is null "),
        BETWEEN(" between "),
        LIKE(" like ");

        private String val;

        private SqlLiteralValueEnum(String val) {
            this.val = val;
        }

        public String value() {
            return val;
        }

    }

    private ArrayList<XmlObject> xmlObjs = new ArrayList<XmlObject>();

    public Sqml asXsqml() {
        final SqmlDocument document = SqmlDocument.Factory.newInstance();
        Sqml ret = document.addNewSqml();
        for (XmlObject item : xmlObjs)
            if (item instanceof XmlString)
                ret.addNewItem().setSql(((XmlString)item).getStringValue());
            else
                ret.addNewItem().set(item);
        return ret;
    }

    public static SqmlExpression valueOf(Sqml xml){
        if (xml==null)
            throw new IllegalArgumentException();
        SqmlExpression ret = new SqmlExpression();
        if (xml.getItemList()!=null)
            for (Sqml.Item item: xml.getItemList()){
                if (item.isSetSql() && item.getSql()!=null){
                    XmlString s = XmlString.Factory.newInstance();
                    s.setStringValue(item.getSql());
                    ret.xmlObjs.add(s);
                }
                else
                    ret.xmlObjs.add(item);
            }
        return ret;
    }

    public static SqmlExpression property(final Id tableId, final Id propertyId) {
        return getPropertySqml(tableId, propertyId, Sqml.Item.PropSqlName.Owner.TABLE);
    }

    public static SqmlExpression table(final Id tableId) {
        SqmlExpression ret = new SqmlExpression();
        Sqml.Item item = Sqml.Item.Factory.newInstance();
        Sqml.Item.TableSqlName tableSqlName = item.addNewTableSqlName();
        tableSqlName.setTableId(tableId);
        ret.xmlObjs.add(item);
        return ret;
    }

    public static SqmlExpression this_table() {
        SqmlExpression ret = new SqmlExpression();
        Sqml.Item item = Sqml.Item.Factory.newInstance();
        item.addNewThisTableId();
        ret.xmlObjs.add(item);
        return ret;
    }
    
    public static SqmlExpression this_pid_as_str() {
        SqmlExpression ret = new SqmlExpression();
        Sqml.Item item = Sqml.Item.Factory.newInstance();
        item.addNewThisTableRef().setPidTranslationMode(EPidTranslationMode.AS_STR);
        ret.xmlObjs.add(item);
        return ret;
    }

    public static SqmlExpression this_property(final Id tableId, final Id propertyId) {
        return getPropertySqml(tableId, propertyId, Sqml.Item.PropSqlName.Owner.THIS);
    }

    public static SqmlExpression child_property(final Id tableId, final Id propertyId) {
        return getPropertySqml(tableId, propertyId, Sqml.Item.PropSqlName.Owner.CHILD);
    }

    public static SqmlExpression parent_property(final Id tableId, final Id propertyId) {
        return getPropertySqml(tableId, propertyId, Sqml.Item.PropSqlName.Owner.PARENT);
    }
    
    public static SqmlExpression property_from_referenced_table(final Id propertyId, final Id... ddsReferencesId){
        SqmlExpression ret = new SqmlExpression();
        Sqml.Item item = Sqml.Item.Factory.newInstance();
        Sqml.Item.ParentRefPropSqlName propSqlName = item.addNewParentRefPropSqlName();
        propSqlName.getReferenceIdList().addAll(Arrays.asList(ddsReferencesId));
        ret.xmlObjs.add(item);
        return ret;
    }

    public static SqmlExpression valueStr(final String val) {
        return val==null ? getValueSqml(" null ") :  getValueSqml(" '" + val + "' ");
    }

    public static SqmlExpression sql(final String sql) {
        return getValueSqml(sql);
    }

    public static SqmlExpression valueInt(final long val) {
        return getValueSqml(String.valueOf(val));
    }

    public static SqmlExpression valueNum(final double val) {
        return getValueSqml(String.valueOf(val));
    }

    public static SqmlExpression typifiedValue(final Id tableId, final Id propertyId, final boolean isLiteral, final String value){
        SqmlExpression ret = new SqmlExpression();
        Sqml.Item item = Sqml.Item.Factory.newInstance();
        Sqml.Item.TypifiedValue typifiedValueTag = item.addNewTypifiedValue();
        typifiedValueTag.setTableId(tableId);
        typifiedValueTag.setPropId(propertyId);
        typifiedValueTag.setLiteral(isLiteral);
        typifiedValueTag.setValue(value);
        ret.xmlObjs.add(item);
        return ret;
    }

    public static SqmlExpression equal(final SqmlExpression property, final SqmlExpression value) {
        return getResult(property, value, SqlLiteralValueEnum.EQUAL);
    }

    public static SqmlExpression not_equal(final SqmlExpression property, final SqmlExpression value) {
        return getResult(property, value, SqlLiteralValueEnum.NOT_EQUAL);
    }

    public static SqmlExpression greater(final SqmlExpression property, final SqmlExpression value) {
        return getResult(property, value, SqlLiteralValueEnum.GREATER);
    }

    public static SqmlExpression greater_or_equal(final SqmlExpression property, final SqmlExpression value) {
        return getResult(property, value, SqlLiteralValueEnum.GREATER_OR_EQUAL);
    }

    public static SqmlExpression less(final SqmlExpression property, final SqmlExpression value) {
        return getResult(property, value, SqlLiteralValueEnum.LESS);
    }

    public static SqmlExpression less_or_equal(final SqmlExpression property, final SqmlExpression value) {
        return getResult(property, value, SqlLiteralValueEnum.LESS_OR_EQUAL);
    }

    public static SqmlExpression and(final SqmlExpression left, final SqmlExpression right) {
        return getResult(left, right, SqlLiteralValueEnum.AND);
    }

    public static SqmlExpression or(final SqmlExpression left, final SqmlExpression right) {
        return getResult(left, right, SqlLiteralValueEnum.OR);
    }

    public static SqmlExpression in(final SqmlExpression left, final SqmlExpression right) {
        return getResult(left, right, SqlLiteralValueEnum.IN);
    }

    public static SqmlExpression in(final SqmlExpression left, final List<SqmlExpression> right) {
        return listExpr(left, SqlLiteralValueEnum.IN, right);
    }

    public static SqmlExpression in(final SqmlExpression left, final SqmlExpression[] right) {
        return listExpr(left, SqlLiteralValueEnum.IN, Arrays.asList(right));
    }

    public static SqmlExpression not_in(final SqmlExpression left, final SqmlExpression right) {
        return getResult(left, right, SqlLiteralValueEnum.NOT_IN);
    }

    public static SqmlExpression not_in(final SqmlExpression left, final List<SqmlExpression> right) {
        return listExpr(left, SqlLiteralValueEnum.NOT_IN, right);
    }

    public static SqmlExpression not_in(final SqmlExpression left, final SqmlExpression[] right) {
        return listExpr(left, SqlLiteralValueEnum.NOT_IN, Arrays.asList(right));
    }

    private static SqmlExpression listExpr(final SqmlExpression left, final SqlLiteralValueEnum operator, final List<SqmlExpression> right) {
        SqmlExpression ret = new SqmlExpression();
        ret.append(prepare(left));

        XmlString sql = XmlString.Factory.newInstance();
        sql.setStringValue(operator.value());
        ret.xmlObjs.add(sql);

        sql = XmlString.Factory.newInstance();
        sql.setStringValue(" ( ");
        ret.xmlObjs.add(sql);
        if (right!=null && right.size()>0){
            for (int i=0;i<right.size();i++){
                if (i>0){
                    sql = XmlString.Factory.newInstance();
                    sql.setStringValue(", ");
                    ret.xmlObjs.add(sql);
                }
                ret.append(prepare(right.get(i)));
            }
        }
        sql = XmlString.Factory.newInstance();
        sql.setStringValue(" )");
        ret.xmlObjs.add(sql);
        return ret;
    }

    public static SqmlExpression not(final SqmlExpression expr) {
        SqmlExpression ret = new SqmlExpression();
        XmlString sql = XmlString.Factory.newInstance();
        sql.setStringValue(SqlLiteralValueEnum.NOT.value());
        ret.xmlObjs.add(sql);
        ret.append(prepare(expr));
        return ret;
    }

    public static SqmlExpression is_null(final SqmlExpression expr) {
        SqmlExpression ret = new SqmlExpression();
        ret.append(prepare(expr));
        XmlString sql = XmlString.Factory.newInstance();
        sql.setStringValue(SqlLiteralValueEnum.IS_NULL.value());
        ret.xmlObjs.add(sql);
        return ret;
    }

    public static SqmlExpression like(final SqmlExpression left, final SqmlExpression right) {
        return getResult(left, right, SqlLiteralValueEnum.LIKE);
    }

    public static SqmlExpression between(final SqmlExpression what, final SqmlExpression left, final SqmlExpression right) {
        SqmlExpression ret = new SqmlExpression();
        ret.append(prepare(what));
        XmlString sql = XmlString.Factory.newInstance();
        sql.setStringValue(SqlLiteralValueEnum.BETWEEN.value());
        ret.xmlObjs.add(sql);
        ret.append(prepare(left));
        sql = XmlString.Factory.newInstance();
        sql.setStringValue(SqlLiteralValueEnum.AND.value());
        ret.xmlObjs.add(sql);
        ret.append(prepare(right));
        return ret;
    }

    private static SqmlExpression getResult(final SqmlExpression left, final SqmlExpression right, final SqlLiteralValueEnum slv) {
        SqmlExpression ret = new SqmlExpression();
        ret.append(prepare(left));
        XmlString sql = XmlString.Factory.newInstance();
        sql.setStringValue(slv.value());
        ret.xmlObjs.add(sql);
        ret.append(prepare(right));
        return ret;
    }

    private static SqmlExpression getPropertySqml(final Id tableId,
            final Id propertyId, final Sqml.Item.PropSqlName.Owner.Enum owner) {
        SqmlExpression ret = new SqmlExpression();
        Sqml.Item item = Sqml.Item.Factory.newInstance();
        Sqml.Item.PropSqlName propSqlName = item.addNewPropSqlName();
        propSqlName.setTableId(tableId);
        propSqlName.setPropId(propertyId);
        propSqlName.setOwner(owner);
        ret.xmlObjs.add(item);
        return ret;
    }

    private static SqmlExpression getValueSqml(final String val) {
        SqmlExpression ret = new SqmlExpression();
        XmlString sql = XmlString.Factory.newInstance();
        sql.setStringValue(val);
        ret.xmlObjs.add(sql);
        return ret;
    }

    private static SqmlExpression prepare(final SqmlExpression expr) {
        SqmlExpression ret = new SqmlExpression();
        if (expr.xmlObjs.size() > 1) {
            XmlString sql = XmlString.Factory.newInstance();
            sql.setStringValue(" ( ");
            ret.xmlObjs.add(sql);
            ret.append(expr);
            sql = XmlString.Factory.newInstance();
            sql.setStringValue(" ) ");
            ret.xmlObjs.add(sql);
        } else if (expr.xmlObjs.size() > 0) {
            ret.xmlObjs.add(expr.xmlObjs.get(0));
        }
        return ret;
    }

    public SqmlExpression append(final SqmlExpression expr) {
        for (XmlObject obj : expr.xmlObjs)
            this.xmlObjs.add(obj);
        return this;
    }
    
    public static Sqml mergeConditions(Sqml condition1, Sqml condition2){

        if (condition1==null ||
            condition1.getItemList()==null ||
            condition1.getItemList().isEmpty())
            return condition2;

        if (condition2==null ||
            condition2.getItemList()==null ||
            condition2.getItemList().isEmpty())
            return condition1;

        Sqml result = (Sqml)condition1.copy();

        if (condition1.getItemList().size()>1){
            result.insertNewItem(0).setSql("( ");
            result.addNewItem().setSql(" ) AND ");
        }
        else
            result.addNewItem().setSql(" AND ");

        if (condition2.getItemList().size()>1)
            result.addNewItem().setSql("( ");

        for (Sqml.Item item : condition2.getItemList())
            if (item.isSetSql())
                result.addNewItem().setSql(item.getSql());
            else
                result.addNewItem().set(item);

        if (condition2.getItemList().size()>1)
            result.addNewItem().setSql(" )");

        return result;
    }

}
