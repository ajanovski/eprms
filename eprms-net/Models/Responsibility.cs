using System;
using System.Collections.Generic;

#nullable disable

namespace info.ajanovski.eprms.net.Models
{
    public partial class Responsibility
    {
        public long ResponsibilityId { get; set; }
        public long ProjectId { get; set; }
        public long TeamId { get; set; }

        public virtual Project Project { get; set; }
        public virtual Team Team { get; set; }
    }
}
