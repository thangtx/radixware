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

package org.radixware.kernel.server.arte.services.eas;

import org.apache.xmlbeans.XmlObject;

import org.radixware.kernel.common.enums.ECommandNature;
import org.radixware.kernel.common.enums.EDefType;
import org.radixware.kernel.common.exceptions.AppException;
import org.radixware.kernel.common.exceptions.DefinitionNotFoundError;
import org.radixware.kernel.common.exceptions.ServiceProcessClientFault;
import org.radixware.kernel.common.exceptions.ServiceProcessFault;
import org.radixware.kernel.common.exceptions.ServiceProcessServerFault;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.server.meta.presentations.RadContextlessCommandDef;
import org.radixware.kernel.server.types.FormHandler;
import org.radixware.schemas.eas.ContextlessCommandMess;
import org.radixware.schemas.eas.ContextlessCommandRq;
import org.radixware.schemas.eas.ContextlessCommandRs;
import org.radixware.schemas.eas.Definition;
import org.radixware.schemas.easWsdl.ContextlessCommandDocument;


final class ContextlessCommandRequest extends SessionRequest {

	ContextlessCommandRequest(final ExplorerAccessService presenter) {
		super(presenter);
	}

	final ContextlessCommandDocument process(final ContextlessCommandMess request) throws ServiceProcessFault, InterruptedException {
		//trace("CommandRequest.process() started");
		//IN
		final ContextlessCommandRq rqParams = request.getContextlessCommandRq();
		final Definition cmdXml = rqParams.getCommand();
		final RadContextlessCommandDef cmd;
		try {
			cmd = getArte().getDefManager().getContextlessCommandDef(cmdXml.getId());
		} catch (DefinitionNotFoundError e) {
			throw EasFaults.newDefWithIdNotFoundFault("Command", rqParams.getDomNode().getNodeName(), cmdXml.getId());
		}

		if (!cmd.getCurUserCanAccess(getArte())) {
			throw EasFaults.newDefinitionAccessViolationFault(getArte(), 
                                                                          Messages.MLS_ID_INSUF_PRIV_TO_EXECUTE_CNTXLESS_CMD, 
                                                                          "\"" + cmd.getName() + "\" (#" + cmd.getId() + ")",
                                                                          EDefType.CONTEXTLESS_COMMAND,
                                                                          new Id[]{cmd.getId()});
		}
		final ContextlessCommandDocument res = ContextlessCommandDocument.Factory.newInstance();
		final ContextlessCommandRs rsStruct = res.addNewContextlessCommand().addNewContextlessCommandRs();
		try {
			final FormHandler.NextDialogsRequest nxDlg;
			if (cmd.getNature() == ECommandNature.FORM_IN_OUT || rqParams.isSetForm()) {
				if (!rqParams.isSetForm()) {
					throw EasFaults.newParamRequiedFault("Form", rqParams.getDomNode().getNodeName());
				}
				final FormHandler fh = getFormHandler(rqParams.getForm(), true);
				nxDlg = fh.onSubmit(new FormHandler.Context(cmd.getId()));
			} else {
				if (cmd.getCmdExecutor() == null) {
					throw EasFaults.newDefinitionAccessViolationFault(getArte(), 
                                                                                          Messages.MLS_ID_INSUF_PRIV_TO_EXECUTE_CNTXLESS_CMD, 
                                                                                          "\"" + cmd.getName() + "\" (#" + cmd.getId() + ")",
                                                                                          EDefType.CONTEXTLESS_COMMAND,
                                                                                          new Id[]{cmd.getId()});
				}
				final XmlObject output = cmd.getNature() == ECommandNature.XML_IN_OUT ? rsStruct.addNewOutput() : null;
				nxDlg = cmd.getCmdExecutor().execCommand(getArte(), rqParams.getInput(), output);
			}
			if (nxDlg != null) {
				writeTo(nxDlg, rsStruct.addNewNextDialog());
			}
		} catch (AppException e) {
			throw EasFaults.exception2Fault(getArte(), e, "Command execution raise application exception");
		} catch (Throwable e) {
			throw EasFaults.exception2Fault(getArte(), e, "Can't execute the command");
		}
        //RADIX-4400 "User '%1' executed contextless command '%2'", EAS, Debug
        if (getUsrActionsIsTraced()) {
            getArte().getTrace().put(
                Messages.MLS_ID_EAS_CNTXTLESS_COMMAND,
                new ArrStr (
                    getArte().getUserName(),
                    String.valueOf(cmd.getName()) + " (#" + cmd.getId().toString() + ")")
            );
        }
		return res;
	}

	@Override
	void prepare(final XmlObject rqXml) throws ServiceProcessServerFault, ServiceProcessClientFault {
		prepare(((ContextlessCommandMess) rqXml).getContextlessCommandRq());
	}

	@Override
	XmlObject process(final XmlObject rq) throws ServiceProcessFault, InterruptedException {
            final ContextlessCommandDocument doc = process((ContextlessCommandMess) rq);
            postProcess(rq, doc.getContextlessCommand().getContextlessCommandRs());
            return doc;
	}
}
