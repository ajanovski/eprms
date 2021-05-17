using System;
using System.Collections.Generic;

#nullable disable

namespace info.ajanovski.eprms.net.Models
{
    public partial class Team
    {
        public Team()
        {
            Repositories = new HashSet<Repository>();
            Responsibilities = new HashSet<Responsibility>();
            TeamMembers = new HashSet<TeamMember>();
            WorkReports = new HashSet<WorkReport>();
        }

        public long TeamId { get; set; }
        public string Name { get; set; }

        public virtual ICollection<Repository> Repositories { get; set; }
        public virtual ICollection<Responsibility> Responsibilities { get; set; }
        public virtual ICollection<TeamMember> TeamMembers { get; set; }
        public virtual ICollection<WorkReport> WorkReports { get; set; }
    }
}
