/*******************************************************************************
 * Copyright (C) 2021 Vangel V. Ajanovski
 *     
 * This file is part of the EPRMS - Educational Project and Resource 
 * Management System (hereinafter: EPRMS).
 *     
 * EPRMS is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *     
 * EPRMS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *     
 * You should have received a copy of the GNU General Public License
 * along with EPRMS.  If not, see <https://www.gnu.org/licenses/>.
 ******************************************************************************/

package info.ajanovski.eprms.tap.pages.admin;

import java.util.List;

import org.apache.tapestry5.EventContext;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.hibernate.annotations.CommitAfter;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.slf4j.Logger;

import info.ajanovski.eprms.model.entities.SystemParameter;
import info.ajanovski.eprms.tap.annotations.AdministratorPage;
import info.ajanovski.eprms.tap.annotations.InstructorPage;
import info.ajanovski.eprms.tap.services.GenericService;
import info.ajanovski.eprms.tap.util.UserInfo;

@InstructorPage
@AdministratorPage
public class SystemParameters {
	@SessionState
	@Property
	private UserInfo userInfo;

	@Inject
	private GenericService genericService;

	@Inject
	Logger logger;

	@InjectComponent
	private Form frmCreateSystemParameter;

	@InjectComponent
	private Form updateForm;

	public enum Mode {
		CREATE, UPDATE
	}

	// activation context

	@Property
	private Mode editorMode;

	@Property
	private Long editorSystemParamId;

	// properties

	@Property
	private SystemParameter listedSystemParameter;

	@Property
	private SystemParameter editorSystemParam;

	public boolean isModeCreate() {
		return editorMode == Mode.CREATE;
	}

	public boolean isModeUpdate() {
		return editorMode == Mode.UPDATE;
	}

	// The code

	void onActivate(EventContext ec) {
		if (ec.getCount() == 0) {
			editorMode = null;
			editorSystemParamId = null;
		} else if (ec.getCount() == 1) {
			editorMode = ec.get(Mode.class, 0);
			editorSystemParamId = null;
		} else {
			editorMode = ec.get(Mode.class, 0);
			editorSystemParamId = ec.get(Long.class, 1);
		}
	}

	Object[] onPassivate() {
		if (editorMode == null) {
			return null;
		} else if (editorMode == Mode.CREATE) {
			return new Object[] { editorMode };
		} else if (editorMode == Mode.UPDATE) {
			return new Object[] { editorMode, editorSystemParamId };
		} else {
			throw new IllegalStateException(editorMode.toString());
		}
	}

	void onActionFromNewSystemParameter() {
		editorMode = Mode.CREATE;
		editorSystemParamId = null;
	}

	// CREATE

	void onCancelNewSystemParameter() {
		editorMode = null;
		editorSystemParamId = null;
	}

	void onPrepareForRenderFromFrmCreateSystemParameter() throws Exception {
		if (frmCreateSystemParameter.isValid()) {
			editorMode = Mode.CREATE;
			editorSystemParam = new SystemParameter();
		}
	}

	void onPrepareForSubmitFromFrmCreateSystemParameter() throws Exception {
		editorMode = Mode.CREATE;
		editorSystemParam = new SystemParameter();
	}

	@CommitAfter
	void onValidateFromFrmCreateSystemParameter() {
		if (frmCreateSystemParameter.getHasErrors()) {
			return;
		}
		try {
			genericService.saveOrUpdate(editorSystemParam);
		} catch (Exception e) {
			frmCreateSystemParameter.recordError(e.getMessage());
		}
	}

	void onSuccessFromFrmCreateSystemParameter() {
		editorMode = null;
		editorSystemParamId = editorSystemParam.getSystemParameterId();
	}

	// UPDATE

	void onCancelUpdate(Long systemParameterId) {
		editorMode = null;
		editorSystemParamId = systemParameterId;
	}

	void onPrepareForRenderFromUpdateForm() {
		if (updateForm.isValid()) {
			editorMode = Mode.UPDATE;
			editorSystemParam = genericService.getByPK(SystemParameter.class, editorSystemParamId);
		}
	}

	void onPrepareForSubmitFromUpdateForm() {
		editorMode = Mode.UPDATE;
		editorSystemParam = genericService.getByPK(SystemParameter.class, editorSystemParamId);
		if (editorSystemParam == null) {
			updateForm.recordError("Param has been deleted by another process.");
			editorSystemParam = new SystemParameter();
		}
	}

	@CommitAfter
	void onValidateFromUpdateForm() {
		logger.info("ONVALIDATE");
		if (updateForm.getHasErrors()) {
			return;
		}
		try {
			genericService.saveOrUpdate(editorSystemParam);
		} catch (Exception e) {
			updateForm.recordError(e.getMessage());
		}
	}

	void onSuccessFromUpdateForm() {
		editorMode = null;
		editorSystemParamId = editorSystemParam.getSystemParameterId();
	}

	//////////////////////////////

	public List<SystemParameter> getSystemParameters() {
		return (List<SystemParameter>) genericService.getAll(SystemParameter.class);
	}

	void onActionFromEditParameter(SystemParameter sp) {
		editorMode = Mode.UPDATE;
		editorSystemParamId = sp.getSystemParameterId();
	}

	@CommitAfter
	void onActionFromDeleteParameter(SystemParameter sp) {
		genericService.delete(sp);
	}

}
