using System;
using System.Collections.Generic;

#nullable disable

namespace info.ajanovski.eprms.net.Models
{
    public partial class CourseProject
    {
        public long CourseProjectId { get; set; }
        public long CourseId { get; set; }
        public long ProjectId { get; set; }

        public virtual Course Course { get; set; }
        public virtual Project Project { get; set; }
    }
}
