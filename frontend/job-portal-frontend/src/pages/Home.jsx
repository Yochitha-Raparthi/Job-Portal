import { useState } from "react";

const sampleJobs = [
  {
    id: 1,
    title: "Java Backend Developer",
    company: "Tech Solutions",
    location: "Hyderabad",
    type: "Full Time",
    salary: "₹5–8 LPA"
  },
  {
    id: 2,
    title: "React Developer",
    company: "Innovate Labs",
    location: "Bangalore",
    type: "Full Time",
    salary: "₹4–7 LPA"
  },
  {
    id: 3,
    title: "Data Analyst",
    company: "DataWorks",
    location: "Chennai",
    type: "Remote",
    salary: "₹4–6 LPA"
  }
];

function Home() {
  const [search, setSearch] = useState("");

  const filteredJobs = sampleJobs.filter((job) =>
    `${job.title} ${job.company} ${job.location}`
      .toLowerCase()
      .includes(search.toLowerCase())
  );

  return (
    <main>
      <section className="hero">
        <div className="hero-content">
          <span className="hero-label">YOUR CAREER STARTS HERE</span>
          <h1>
            Find Your Next <span>Dream Job</span>
          </h1>
          <p>
            Discover exciting career opportunities and connect
            with companies looking for talent like you.
          </p>

          <div className="search-box">
            <input
              type="text"
              placeholder="Search job title, company or location..."
              value={search}
              onChange={(e) => setSearch(e.target.value)}
            />
            <button onClick={() =>
              document.getElementById("featured-jobs")
                ?.scrollIntoView({ behavior: "smooth" })
            }>
              Find Jobs
            </button>
          </div>
        </div>
      </section>

      <section className="jobs-section" id="featured-jobs">
        <div className="section-heading">
          <div>
            <span className="eyebrow">OPPORTUNITIES</span>
            <h2>Featured Jobs</h2>
            <p>Explore opportunities that match your skills.</p>
          </div>
        </div>

        <div className="job-grid">
          {filteredJobs.map((job) => (
            <div className="job-card" key={job.id}>
              <div className="job-card-top">
                <div className="company-avatar">
                  {job.company.charAt(0)}
                </div>
                <span className="job-type">{job.type}</span>
              </div>

              <h3>{job.title}</h3>
              <p className="company-name">{job.company}</p>
              <p className="job-location">📍 {job.location}</p>

              <div className="job-card-bottom">
                <strong>{job.salary}</strong>
                <button
                  onClick={() => alert(
                    "Job details will be available soon!"
                  )}
                >
                  View Details
                </button>
              </div>
            </div>
          ))}
        </div>

        {filteredJobs.length === 0 && (
          <p className="no-jobs">No matching jobs found.</p>
        )}
      </section>
    </main>
  );
}

export default Home;