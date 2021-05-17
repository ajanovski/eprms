using System;
using System.Collections.Generic;

#nullable disable

namespace info.ajanovski.eprms.net.Models
{
    public partial class Repository
    {
        public long RepositoryId { get; set; }
        public string Title { get; set; }
        public string Url { get; set; }
        public long? PersonId { get; set; }
        public long? ProjectId { get; set; }
        public long? TeamId { get; set; }
        public DateTime? DateCreated { get; set; }
        public string Type { get; set; }

        public virtual Person Person { get; set; }
        public virtual Project Project { get; set; }
        public virtual Team Team { get; set; }
    }
}
