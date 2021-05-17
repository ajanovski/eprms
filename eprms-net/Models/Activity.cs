using System;
using System.Collections.Generic;

#nullable disable

namespace info.ajanovski.eprms.net.Models
{
    public partial class Activity
    {
        public Activity()
        {
            InverseParentActivity = new HashSet<Activity>();
            WorkReports = new HashSet<WorkReport>();
        }

        public long ActivityId { get; set; }
        public string Description { get; set; }
        public string Title { get; set; }
        public long ActivityTypeId { get; set; }
        public long? ParentActivityId { get; set; }
        public long ProjectId { get; set; }

        public virtual ActivityType ActivityType { get; set; }
        public virtual Activity ParentActivity { get; set; }
        public virtual Project Project { get; set; }
        public virtual ICollection<Activity> InverseParentActivity { get; set; }
        public virtual ICollection<WorkReport> WorkReports { get; set; }
    }
}
