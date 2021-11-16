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

package info.ajanovski.eprms.model.util;

public class ModelConstants {

	public static final String BooleanTRUEasCHAR = "T";
	public static final String BooleanFALSEasCHAR = "F";

	public static final String EmailUnknown = "EMAIL@UNKNOWN";

	public static final String RoleAdministrator = "ADMINISTRATOR";
	public static final String RoleInstructor = "INSTRUCTOR";
	public static final String RoleStudent = "STUDENT";

	public static final String ProjectStatusProposed = "PROPOSED";
	public static final String ProjectStatusStarted = "STARTED";
	public static final String ProjectStatusActive = "ACTIVE";
	public static final String ProjectStatusPaused = "PAUSED";
	public static final String ProjectStatusFinished = "FINISHED";
	public static final String ProjectStatusStopped = "STOPPED";
	
	public static final String EvaluationStatusCreated = "CREATED";
	public static final String EvaluationStatusPublished = "PUBLISHED";
}
