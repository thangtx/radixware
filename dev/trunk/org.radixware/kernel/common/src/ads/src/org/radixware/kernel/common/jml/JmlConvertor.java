package org.radixware.kernel.common.jml;

import org.radixware.kernel.common.scml.Scml;
import org.radixware.kernel.common.sqml.Sqml;
import org.radixware.kernel.common.sqml.tags.DbNameTag;
import org.radixware.kernel.common.sqml.tags.IdTag;

public class JmlConvertor {

    public static Sqml convertToSqml(Jml jml) {
        Sqml sqml = Sqml.Factory.newInstance();
        for (Scml.Item item : jml.getItems()) {
            // tagDbName
            if (item instanceof JmlTagDbName) {
                JmlTagDbName jmlTag = (JmlTagDbName) item;
                DbNameTag sqmlTag = DbNameTag.Factory.newInstance(jmlTag.getPath().asArray());
                sqml.getItems().add(sqmlTag);
                continue;
            }
            // tagId
            if (item instanceof JmlTagId) {
                JmlTagId jmlTag = (JmlTagId) item;
                IdTag sqmlTag = IdTag.Factory.newInstance(jmlTag.getPath().asArray(), jmlTag.isSoftReference());
                sqml.getItems().add(sqmlTag);
                continue;
            }
            // comman
            convertCommonItem(item, sqml);
        }
        return sqml;
    }

    public static Jml convertToJml(Sqml sqml) {
        Jml jml = Jml.Factory.newInstance();
        for (Scml.Item item : sqml.getItems()) {
            // tagDbName
            if (item instanceof DbNameTag) {
                DbNameTag sqmlTag = (DbNameTag) item;
                JmlTagDbName jmlTag = JmlTagDbName.Factory.newInstance(sqmlTag.getPath());
                jml.getItems().add(jmlTag);
                continue;
            }
            // tagId
            if (item instanceof IdTag) {
                IdTag sqmlTag = (IdTag) item;
                JmlTagId jmlTag = new JmlTagId(sqmlTag.getPath());
                jml.getItems().add(jmlTag);
                continue;
            }
            // comman
            convertCommonItem(item, jml);
        }
        return jml;
    }

    private static void convertCommonItem(Scml.Item sourceTag, Scml target) {
        // text
        if (sourceTag instanceof Scml.Text) {
            Scml.Text text = Scml.Text.Factory.newInstance(((Scml.Text) sourceTag).getText());
            target.getItems().add(text);
            return;
        }
        // otherTag
        if (sourceTag instanceof Scml.Tag) {
            Scml.Text text = Scml.Text.Factory.newInstance(sourceTag.toString());
            target.getItems().add(text);
            return;
        }
    }
}
