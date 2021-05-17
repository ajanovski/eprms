using System;
using System.Collections.Generic;

#nullable disable

namespace info.ajanovski.eprms.net.Models
{
    public partial class ActivityType
    {
        public ActivityType()
        {
            Activities = new HashSet<Activity>();
        }

        public long ActivityTypeId { get; set; }
        public string Description { get; set; }
        public string Title { get; set; }

        public virtual ICollection<Activity> Activities { get; set; }
    }
}
