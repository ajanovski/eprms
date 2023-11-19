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
	public static final String ProjectStatusCreation = "CREATION";
	public static final String ProjectStatusStarted = "STARTED";
	public static final String ProjectStatusActive = "ACTIVE";
	public static final String ProjectStatusPaused = "PAUSED";
	public static final String ProjectStatusStopped = "STOPPED";
	public static final String ProjectStatusFinished = "FINISHED";
	public static final String ProjectStatusFailed = "FAILED";

	public static final String[] AllProjectStatuses = { ProjectStatusProposed, ProjectStatusCreation,
			ProjectStatusStarted, ProjectStatusActive, ProjectStatusPaused, ProjectStatusStopped, ProjectStatusFinished,
			ProjectStatusFailed };

	public static final String EvaluationStatusCreated = "CREATED";
	public static final String EvaluationStatusPublished = "PUBLISHED";

	public static final String[] AllEvaluationStatuses = { EvaluationStatusCreated, EvaluationStatusPublished };

	public static final String CourseUnknown = "CourseNA";

	public static final String TeamMemberRoleCoordinator = "COORDINATOR";
	public static final String TeamMemberRoleMember = "MEMBER";
	public static final String TeamMemberRoleSupervisor = "SUPERVISOR";

	public static final String[] AllTeamMemberRoles = { TeamMemberRoleCoordinator, TeamMemberRoleMember,
			TeamMemberRoleSupervisor };

	public static final String TeamStatusProposed = "PROPOSED";
	public static final String TeamStatusAccepted = "ACTIVE";
	public static final String TeamStatusPaused = "PAUSED";
	public static final String TeamStatusFinished = "FINISHED";

	public static final String[] AllTeamStatuses = { TeamStatusProposed, TeamStatusAccepted, TeamStatusPaused,
			TeamStatusFinished };

	public static final String TeamMemberStatusProposed = "PROPOSED";
	public static final String TeamMemberStatusAccepted = "ACTIVE";
	public static final String TeamMemberStatusPaused = "PAUSED";
	public static final String TeamMemberStatusFinished = "FINISHED";
	public static final String TeamMemberStatusQuit = "QUIT";

	public static final String[] AllTeamMemberStatuses = { TeamMemberStatusProposed, TeamMemberStatusAccepted,
			TeamMemberStatusPaused, TeamMemberStatusFinished, TeamMemberStatusQuit };

	public static final String DiscussionPostTypeBug = "BUG";
	public static final String DiscussionPostTypeImprovement = "IMPROVEMENT";
	public static final String DiscussionPostTypeTask = "TASK";
	public static final String DiscussionPostTypeOther = "OTHER";
	public static final String[] AllDiscussionPostTypes = { DiscussionPostTypeBug, DiscussionPostTypeImprovement,
			DiscussionPostTypeTask, DiscussionPostTypeOther };

	public static final String DiscussionPostEvaluationTypeIdea = "IDEA";
	public static final String DiscussionPostEvaluationTypeModel = "MODEL";
	public static final String DiscussionPostEvaluationTypeFunctionality = "FUNCTIONALITY";
	public static final String DiscussionPostEvaluationTypeBug = "BUG";
	public static final String DiscussionPostEvaluationTypeOther = "OTHER";
	public static final String[] AllDiscussionPostEvaluationTypes = { DiscussionPostEvaluationTypeIdea,
			DiscussionPostEvaluationTypeModel, DiscussionPostEvaluationTypeFunctionality,
			DiscussionPostEvaluationTypeBug, DiscussionPostEvaluationTypeOther };

}
