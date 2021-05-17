using System;
using System.Collections.Generic;

#nullable disable

namespace info.ajanovski.eprms.net.Models
{
    public partial class Person
    {
        public Person()
        {
            PersonRoles = new HashSet<PersonRole>();
            Repositories = new HashSet<Repository>();
            TeamMembers = new HashSet<TeamMember>();
            WorkEvaluations = new HashSet<WorkEvaluation>();
            WorkReports = new HashSet<WorkReport>();
        }

        public long PersonId { get; set; }
        public string Email { get; set; }
        public string FirstName { get; set; }
        public string LastName { get; set; }
        public string UserName { get; set; }
        public string AuthString { get; set; }

        public virtual ICollection<PersonRole> PersonRoles { get; set; }
        public virtual ICollection<Repository> Repositories { get; set; }
        public virtual ICollection<TeamMember> TeamMembers { get; set; }
        public virtual ICollection<WorkEvaluation> WorkEvaluations { get; set; }
        public virtual ICollection<WorkReport> WorkReports { get; set; }
    }
}
