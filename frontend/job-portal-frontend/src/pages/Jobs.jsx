
import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { getAllJobs } from "../services/JobService";
import "./Jobs.css";

function Jobs() {

  const [jobs, setJobs] = useState([]);
  const [search, setSearch] = useState("");
  const [location, setLocation] = useState("");
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {

    const loadJobs = async () => {
      try {
        const response = await getAllJobs();
        setJobs(response.data);
      } catch (err) {
        setError("Unable to load jobs. Please try again.");
      } finally {
        setLoading(false);
      }
    };

    loadJobs();

  }, []);

  // Filter jobs based on title, company and location
  const filteredJobs = jobs.filter((job) => {

    const keyword = search.toLowerCase();

    const matchesSearch =
      (job.title || "").toLowerCase().includes(keyword) ||
      (job.companyName || "").toLowerCase().includes(keyword);

    const matchesLocation =
      (job.location || "")
        .toLowerCase()
        .includes(location.toLowerCase());

    return matchesSearch && matchesLocation;
  });

  return (
    <div className="jobs-page">

      <div className="jobs-banner">
        <span className="eyebrow">EXPLORE OPPORTUNITIES</span>
        <h1>Find Your Perfect Job</h1>
        <p>Explore career opportunities from different companies.</p>
      </div>

      <div className="jobs-container">

        <div className="jobs-filters">

          <input
            type="text"
            placeholder="Search job title or company..."
            value={search}
            onChange={(e) => setSearch(e.target.value)}
          />

          <input
            type="text"
            placeholder="Search location..."
            value={location}
            onChange={(e) => setLocation(e.target.value)}
          />

          <button
            onClick={() => {
              setSearch("");
              setLocation("");
            }}
          >
            Clear Filters
          </button>

        </div>

        <div className="jobs-results">
          <h2>Available Jobs</h2>
          {!loading && !error && (
            <p>{filteredJobs.length} jobs found</p>
          )}
        </div>

        {loading && (
          <p className="jobs-message">Loading jobs...</p>
        )}

        {error && (
          <p className="jobs-message" role="alert">{error}</p>
        )}

        {!loading && !error && filteredJobs.length === 0 && (
          <p className="jobs-message">
            No jobs found matching your search.
          </p>
        )}

        {!loading && !error && (
          <div className="jobs-list-grid">

            {filteredJobs.map((job) => (

              <div className="listing-card" key={job.id}>

                <div className="listing-top">

                  <div className="listing-company-logo">
                    {(job.companyName || "C").charAt(0)}
                  </div>

                  <span className="listing-type">
                    {job.jobType}
                  </span>

                </div>

                <h3>{job.title}</h3>

                <p className="listing-company">
                  {job.companyName}
                </p>

                <div className="listing-info">
                  <span>📍 {job.location}</span>
                  <span>💰 ₹{job.salary?.toLocaleString()}</span>
                </div>

                <div className="listing-footer">

                  <span>
                    {job.postedAt
                      ? new Date(job.postedAt).toLocaleDateString()
                      : "Recently posted"}
                  </span>

                  <Link
                    to={`/jobs/${job.id}`}
                    className="listing-view-btn"
                  >
                    View Details
                  </Link>

                </div>

              </div>

            ))}

          </div>
        )}

      </div>
    </div>
  );
}

export default Jobs;
