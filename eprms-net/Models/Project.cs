using System;
using System.Collections.Generic;

#nullable disable

namespace info.ajanovski.eprms.net.Models
{
    public partial class Project
    {
        public Project()
        {
            Activities = new HashSet<Activity>();
            CourseProjects = new HashSet<CourseProject>();
            Databases = new HashSet<Database>();
            Repositories = new HashSet<Repository>();
            Responsibilities = new HashSet<Responsibility>();
        }

        public long ProjectId { get; set; }
        public string Description { get; set; }
        public string Title { get; set; }
        public DateTime? FinishDate { get; set; }
        public DateTime? StartDate { get; set; }

        public virtual ICollection<Activity> Activities { get; set; }
        public virtual ICollection<CourseProject> CourseProjects { get; set; }
        public virtual ICollection<Database> Databases { get; set; }
        public virtual ICollection<Repository> Repositories { get; set; }
        public virtual ICollection<Responsibility> Responsibilities { get; set; }
    }
}
