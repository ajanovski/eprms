using System;
using System.Collections.Generic;

#nullable disable

namespace info.ajanovski.eprms.net.Models
{
    public partial class Course
    {
        public Course()
        {
            CourseProjects = new HashSet<CourseProject>();
        }

        public long CourseId { get; set; }
        public string Title { get; set; }

        public virtual ICollection<CourseProject> CourseProjects { get; set; }
    }
}
