package org.radixware.kernel.common.builder.release;

import java.util.List;
import org.radixware.kernel.common.repository.dds.DdsScript;
import org.radixware.kernel.common.svn.SVNRepositoryAdapter;

public interface IScriptDialog {

    public boolean showDialog(final List<DdsScript> layers, ReleaseSettings settings, SVNRepositoryAdapter repository, long revision, List<ScriptFileInfo> modifiedFiles);
    
}
