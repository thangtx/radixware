package org.radixware.kernel.designer.common.dialogs.components.localizing.html;

import java.util.List;
import org.radixware.kernel.common.types.IKernelIntEnum;
import org.radixware.kernel.common.types.Id;


public enum EHtmlEditorState  implements IKernelIntEnum {

        ALL(0),
        REPORT(1);
        private Long val;

        private EHtmlEditorState(long val) {
            this.val = Long.valueOf(val);
        }

        @Override
        public Long getValue() {
            return val;
        }

        @Override
        public String getName() {
            return this.name();
        }

        public static EHtmlEditorState getForValue(final long val) {
            for (EHtmlEditorState e : EHtmlEditorState.values()) {
                if (e.getValue().longValue() == val) {
                    return e;
                }
            }
            return null;
        }

        @Override
        public boolean isInDomain(Id id) {
            return false;
        }

        @Override
        public boolean isInDomains(List<Id> ids) {
            return false;
        }
}
