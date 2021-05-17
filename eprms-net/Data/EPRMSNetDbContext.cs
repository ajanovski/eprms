using Microsoft.EntityFrameworkCore;
using info.ajanovski.eprms.net.Models;

#nullable disable

namespace info.ajanovski.eprms.net.Data {
    public partial class EPRMSNetDbContext : DbContext {

        public EPRMSNetDbContext() {
        }

        public EPRMSNetDbContext(DbContextOptions<EPRMSNetDbContext> options)
            : base(options) {
        }

        public virtual DbSet<Activity> Activities { get; set; }
        public virtual DbSet<ActivityType> ActivityTypes { get; set; }
        public virtual DbSet<Course> Courses { get; set; }
        public virtual DbSet<CourseProject> CourseProjects { get; set; }
        public virtual DbSet<Database> Databases { get; set; }
        public virtual DbSet<Person> People { get; set; }
        public virtual DbSet<PersonRole> PersonRoles { get; set; }
        public virtual DbSet<Project> Projects { get; set; }
        public virtual DbSet<Repository> Repositories { get; set; }
        public virtual DbSet<Responsibility> Responsibilities { get; set; }
        public virtual DbSet<Role> Roles { get; set; }
        public virtual DbSet<Team> Teams { get; set; }
        public virtual DbSet<TeamMember> TeamMembers { get; set; }
        public virtual DbSet<WorkEvaluation> WorkEvaluations { get; set; }
        public virtual DbSet<WorkReport> WorkReports { get; set; }

        protected override void OnModelCreating(ModelBuilder modelBuilder) {
            modelBuilder.HasAnnotation("Relational:Collation", "en_US.UTF-8");

            modelBuilder.Entity<Activity>(entity => {
                entity.ToTable("activity", "epm_main");

                entity.Property(e => e.ActivityId).HasColumnName("activity_id");

                entity.Property(e => e.ActivityTypeId).HasColumnName("activity_type_id");

                entity.Property(e => e.Description)
                    .HasMaxLength(255)
                    .HasColumnName("description");

                entity.Property(e => e.ParentActivityId).HasColumnName("parent_activity_id");

                entity.Property(e => e.ProjectId).HasColumnName("project_id");

                entity.Property(e => e.Title)
                    .HasMaxLength(255)
                    .HasColumnName("title");

                entity.HasOne(d => d.ActivityType)
                    .WithMany(p => p.Activities)
                    .HasForeignKey(d => d.ActivityTypeId)
                    .OnDelete(DeleteBehavior.ClientSetNull)
                    .HasConstraintName("fk_activity_activity_type");

                entity.HasOne(d => d.ParentActivity)
                    .WithMany(p => p.InverseParentActivity)
                    .HasForeignKey(d => d.ParentActivityId)
                    .HasConstraintName("fk_activity_activity");

                entity.HasOne(d => d.Project)
                    .WithMany(p => p.Activities)
                    .HasForeignKey(d => d.ProjectId)
                    .OnDelete(DeleteBehavior.ClientSetNull)
                    .HasConstraintName("fk_activity_project");
            });

            modelBuilder.Entity<ActivityType>(entity => {
                entity.ToTable("activity_type", "epm_main");

                entity.Property(e => e.ActivityTypeId).HasColumnName("activity_type_id");

                entity.Property(e => e.Description)
                    .HasMaxLength(255)
                    .HasColumnName("description");

                entity.Property(e => e.Title)
                    .HasMaxLength(255)
                    .HasColumnName("title");
            });

            modelBuilder.Entity<Course>(entity => {
                entity.ToTable("course", "epm_main");

                entity.HasIndex(e => e.Title, "uk_msgoex7rold2eqqf1cllhk02i")
                    .IsUnique();

                entity.Property(e => e.CourseId).HasColumnName("course_id");

                entity.Property(e => e.Title)
                    .IsRequired()
                    .HasMaxLength(255)
                    .HasColumnName("title");
            });

            modelBuilder.Entity<CourseProject>(entity => {
                entity.ToTable("course_project", "epm_main");

                entity.Property(e => e.CourseProjectId).HasColumnName("course_project_id");

                entity.Property(e => e.CourseId).HasColumnName("course_id");

                entity.Property(e => e.ProjectId).HasColumnName("project_id");

                entity.HasOne(d => d.Course)
                    .WithMany(p => p.CourseProjects)
                    .HasForeignKey(d => d.CourseId)
                    .OnDelete(DeleteBehavior.ClientSetNull)
                    .HasConstraintName("fk_course_project_course");

                entity.HasOne(d => d.Project)
                    .WithMany(p => p.CourseProjects)
                    .HasForeignKey(d => d.ProjectId)
                    .OnDelete(DeleteBehavior.ClientSetNull)
                    .HasConstraintName("fk_course_project_project");
            });

            modelBuilder.Entity<Database>(entity => {
                entity.ToTable("database", "epm_main");

                entity.Property(e => e.DatabaseId).HasColumnName("database_id");

                entity.Property(e => e.DateCreated).HasColumnName("date_created");

                entity.Property(e => e.Name)
                    .HasMaxLength(255)
                    .HasColumnName("name");

                entity.Property(e => e.Owner)
                    .HasMaxLength(255)
                    .HasColumnName("owner");

                entity.Property(e => e.Password)
                    .HasMaxLength(255)
                    .HasColumnName("password");

                entity.Property(e => e.Port)
                    .HasMaxLength(255)
                    .HasColumnName("port");

                entity.Property(e => e.ProjectId).HasColumnName("project_id");

                entity.Property(e => e.Server)
                    .HasMaxLength(255)
                    .HasColumnName("server");

                entity.Property(e => e.TunnelPassword)
                    .HasMaxLength(255)
                    .HasColumnName("tunnel_password");

                entity.Property(e => e.TunnelServer)
                    .HasMaxLength(255)
                    .HasColumnName("tunnel_server");

                entity.Property(e => e.TunnelUser)
                    .HasMaxLength(255)
                    .HasColumnName("tunnel_user");

                entity.Property(e => e.Type)
                    .HasMaxLength(255)
                    .HasColumnName("type");

                entity.HasOne(d => d.Project)
                    .WithMany(p => p.Databases)
                    .HasForeignKey(d => d.ProjectId)
                    .OnDelete(DeleteBehavior.ClientSetNull)
                    .HasConstraintName("fk_database_project");
            });

            modelBuilder.Entity<Person>(entity => {
                entity.ToTable("person", "epm_main");

                entity.Property(e => e.PersonId).HasColumnName("person_id");

                entity.Property(e => e.AuthString)
                    .HasMaxLength(255)
                    .HasColumnName("auth_string");

                entity.Property(e => e.Email)
                    .HasMaxLength(255)
                    .HasColumnName("email");

                entity.Property(e => e.FirstName)
                    .HasMaxLength(255)
                    .HasColumnName("first_name");

                entity.Property(e => e.LastName)
                    .HasMaxLength(255)
                    .HasColumnName("last_name");

                entity.Property(e => e.UserName)
                    .HasMaxLength(255)
                    .HasColumnName("user_name");
            });

            modelBuilder.Entity<PersonRole>(entity => {
                entity.ToTable("person_role", "epm_util");

                entity.Property(e => e.PersonRoleId).HasColumnName("person_role_id");

                entity.Property(e => e.PersonId).HasColumnName("person_id");

                entity.Property(e => e.RoleId).HasColumnName("role_id");

                entity.HasOne(d => d.Person)
                    .WithMany(p => p.PersonRoles)
                    .HasForeignKey(d => d.PersonId)
                    .OnDelete(DeleteBehavior.ClientSetNull)
                    .HasConstraintName("fk_person_role_person");

                entity.HasOne(d => d.Role)
                    .WithMany(p => p.PersonRoles)
                    .HasForeignKey(d => d.RoleId)
                    .OnDelete(DeleteBehavior.ClientSetNull)
                    .HasConstraintName("fk_person_role_role");
            });

            modelBuilder.Entity<Project>(entity => {
                entity.ToTable("project", "epm_main");

                entity.Property(e => e.ProjectId).HasColumnName("project_id");

                entity.Property(e => e.Description)
                    .HasMaxLength(255)
                    .HasColumnName("description");

                entity.Property(e => e.FinishDate).HasColumnName("finish_date");

                entity.Property(e => e.StartDate).HasColumnName("start_date");

                entity.Property(e => e.Title)
                    .IsRequired()
                    .HasMaxLength(255)
                    .HasColumnName("title");
            });

            modelBuilder.Entity<Repository>(entity => {
                entity.ToTable("repository", "epm_main");

                entity.Property(e => e.RepositoryId).HasColumnName("repository_id");

                entity.Property(e => e.DateCreated).HasColumnName("date_created");

                entity.Property(e => e.PersonId).HasColumnName("person_id");

                entity.Property(e => e.ProjectId).HasColumnName("project_id");

                entity.Property(e => e.TeamId).HasColumnName("team_id");

                entity.Property(e => e.Title)
                    .HasMaxLength(255)
                    .HasColumnName("title");

                entity.Property(e => e.Type)
                    .HasMaxLength(255)
                    .HasColumnName("type");

                entity.Property(e => e.Url)
                    .HasMaxLength(255)
                    .HasColumnName("url");

                entity.HasOne(d => d.Person)
                    .WithMany(p => p.Repositories)
                    .HasForeignKey(d => d.PersonId)
                    .HasConstraintName("fk_repository_person");

                entity.HasOne(d => d.Project)
                    .WithMany(p => p.Repositories)
                    .HasForeignKey(d => d.ProjectId)
                    .HasConstraintName("fk_repository_project");

                entity.HasOne(d => d.Team)
                    .WithMany(p => p.Repositories)
                    .HasForeignKey(d => d.TeamId)
                    .HasConstraintName("fk_repository_team");
            });

            modelBuilder.Entity<Responsibility>(entity => {
                entity.ToTable("responsibility", "epm_main");

                entity.Property(e => e.ResponsibilityId).HasColumnName("responsibility_id");

                entity.Property(e => e.ProjectId).HasColumnName("project_id");

                entity.Property(e => e.TeamId).HasColumnName("team_id");

                entity.HasOne(d => d.Project)
                    .WithMany(p => p.Responsibilities)
                    .HasForeignKey(d => d.ProjectId)
                    .OnDelete(DeleteBehavior.ClientSetNull)
                    .HasConstraintName("fk_responsibility_project");

                entity.HasOne(d => d.Team)
                    .WithMany(p => p.Responsibilities)
                    .HasForeignKey(d => d.TeamId)
                    .OnDelete(DeleteBehavior.ClientSetNull)
                    .HasConstraintName("fk_responsibility_team");
            });

            modelBuilder.Entity<Role>(entity => {
                entity.ToTable("role", "epm_util");

                entity.Property(e => e.RoleId).HasColumnName("role_id");

                entity.Property(e => e.Name)
                    .HasMaxLength(255)
                    .HasColumnName("name");
            });

            modelBuilder.Entity<Team>(entity => {
                entity.ToTable("team", "epm_main");

                entity.Property(e => e.TeamId).HasColumnName("team_id");

                entity.Property(e => e.Name)
                    .HasMaxLength(255)
                    .HasColumnName("name");
            });

            modelBuilder.Entity<TeamMember>(entity => {
                entity.ToTable("team_member", "epm_main");

                entity.Property(e => e.TeamMemberId).HasColumnName("team_member_id");

                entity.Property(e => e.PersonId).HasColumnName("person_id");

                entity.Property(e => e.PositionNumber).HasColumnName("position_number");

                entity.Property(e => e.Role)
                    .HasMaxLength(255)
                    .HasColumnName("role");

                entity.Property(e => e.TeamId).HasColumnName("team_id");

                entity.HasOne(d => d.Person)
                    .WithMany(p => p.TeamMembers)
                    .HasForeignKey(d => d.PersonId)
                    .OnDelete(DeleteBehavior.ClientSetNull)
                    .HasConstraintName("fk_team_member_person");

                entity.HasOne(d => d.Team)
                    .WithMany(p => p.TeamMembers)
                    .HasForeignKey(d => d.TeamId)
                    .OnDelete(DeleteBehavior.ClientSetNull)
                    .HasConstraintName("fk_team_member_team");
            });

            modelBuilder.Entity<WorkEvaluation>(entity => {
                entity.ToTable("work_evaluation", "epm_main");

                entity.Property(e => e.WorkEvaluationId).HasColumnName("work_evaluation_id");

                entity.Property(e => e.Description)
                    .HasMaxLength(255)
                    .HasColumnName("description");

                entity.Property(e => e.EvaluationDate).HasColumnName("evaluation_date");

                entity.Property(e => e.PercentEvaluated).HasColumnName("percent_evaluated");

                entity.Property(e => e.PersonId).HasColumnName("person_id");

                entity.Property(e => e.Points).HasColumnName("points");

                entity.Property(e => e.Title)
                    .HasMaxLength(255)
                    .HasColumnName("title");

                entity.Property(e => e.WorkReportId).HasColumnName("work_report_id");

                entity.HasOne(d => d.Person)
                    .WithMany(p => p.WorkEvaluations)
                    .HasForeignKey(d => d.PersonId)
                    .HasConstraintName("fk_work_evaluation_person");

                entity.HasOne(d => d.WorkReport)
                    .WithMany(p => p.WorkEvaluations)
                    .HasForeignKey(d => d.WorkReportId)
                    .OnDelete(DeleteBehavior.ClientSetNull)
                    .HasConstraintName("fk_work_evaluation_work_report");
            });

            modelBuilder.Entity<WorkReport>(entity => {
                entity.ToTable("work_report", "epm_main");

                entity.Property(e => e.WorkReportId).HasColumnName("work_report_id");

                entity.Property(e => e.ActivityId).HasColumnName("activity_id");

                entity.Property(e => e.ContinuationOfWorkReportId).HasColumnName("continuation_of_work_report_id");

                entity.Property(e => e.Description)
                    .HasMaxLength(255)
                    .HasColumnName("description");

                entity.Property(e => e.PercentReported).HasColumnName("percent_reported");

                entity.Property(e => e.PersonId).HasColumnName("person_id");

                entity.Property(e => e.SubmissionDate).HasColumnName("submission_date");

                entity.Property(e => e.TeamId).HasColumnName("team_id");

                entity.Property(e => e.Title)
                    .HasMaxLength(255)
                    .HasColumnName("title");

                entity.HasOne(d => d.Activity)
                    .WithMany(p => p.WorkReports)
                    .HasForeignKey(d => d.ActivityId)
                    .OnDelete(DeleteBehavior.ClientSetNull)
                    .HasConstraintName("fk_work_report_activity");

                entity.HasOne(d => d.ContinuationOfWorkReport)
                    .WithMany(p => p.InverseContinuationOfWorkReport)
                    .HasForeignKey(d => d.ContinuationOfWorkReportId)
                    .HasConstraintName("fk_work_report_work_report");

                entity.HasOne(d => d.Person)
                    .WithMany(p => p.WorkReports)
                    .HasForeignKey(d => d.PersonId)
                    .HasConstraintName("fk_work_report_person");

                entity.HasOne(d => d.Team)
                    .WithMany(p => p.WorkReports)
                    .HasForeignKey(d => d.TeamId)
                    .HasConstraintName("fk_work_report_team");
            });

            OnModelCreatingPartial(modelBuilder);
        }

        partial void OnModelCreatingPartial(ModelBuilder modelBuilder);
    }
}
