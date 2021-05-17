using System;
using System.Collections.Generic;

#nullable disable

namespace info.ajanovski.eprms.net.Models
{
    public partial class TeamMember
    {
        public long TeamMemberId { get; set; }
        public long? PositionNumber { get; set; }
        public string Role { get; set; }
        public long PersonId { get; set; }
        public long TeamId { get; set; }

        public virtual Person Person { get; set; }
        public virtual Team Team { get; set; }
    }
}
