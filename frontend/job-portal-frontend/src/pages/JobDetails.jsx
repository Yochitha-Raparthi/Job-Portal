
import { useEffect, useState } from "react";
import { Link, useParams } from "react-router-dom";

import { getJobById } from "../services/JobService";
import { applyForJob } from "../services/ApplicationService";
import { useAuth } from "../context/AuthContext";

import "./Jobs.css";

function JobDetails() {
  const { id } = useParams();
  const { user } = useAuth();

  const [job, setJob] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  const [resumeUrl, setResumeUrl] = useState("");
  const [applying, setApplying] = useState(false);
  const [applicationMessage, setApplicationMessage] = useState("");
  const [applicationError, setApplicationError] = useState("");

  useEffect(() => {
    const loadJob = async () => {
      setLoading(true);
      setError("");

      try {
        const response = await getJobById(id);
        setJob(response.data);
      } catch (err) {
        setError("Unable to load job details.");
      } finally {
        setLoading(false);
      }
    };

    loadJob();
  }, [id]);

  const handleApply = async (e) => {
    e.preventDefault();

    setApplicationMessage("");
    setApplicationError("");
    setApplying(true);

    const applicationData = {
      job: {
        id: Number(id)
      },
      status: "PENDING",
      resumeUrl: resumeUrl.trim()
    };

    try {
      await applyForJob(applicationData);

      setApplicationMessage("Application submitted successfully!");
      setResumeUrl("");

    } catch (err) {
      if (err.response?.status === 409) {
        setApplicationError(
          "You have already applied for this job."
        );
      } else {
        setApplicationError(
          err.response?.data?.message ||
          "Unable to submit your application."
        );
      }
    } finally {
      setApplying(false);
    }
  };

  if (loading) {
    return <p className="jobs-message">Loading job details...</p>;
  }

  if (error) {
    return <p className="jobs-message" role="alert">{error}</p>;
  }

  if (!job) {
    return <p className="jobs-message">Job not found.</p>;
  }

  return (
    <div className="job-details-page">
      <Link to="/jobs" className="back-link">
        ← Back to Jobs
      </Link>

      <div className="job-details-card">
        <div className="job-details-header">
          <span className="eyebrow">JOB OPPORTUNITY</span>
          <h1>{job.title}</h1>
          <p>{job.companyName}</p>
        </div>

        <div className="job-details-meta">
          <span>📍 {job.location}</span>
          <span>💼 {job.jobType}</span>
          <span>💰 ₹{job.salary?.toLocaleString()}</span>
        </div>

        <div className="job-description">
          <h2>Job Description</h2>
          <p>{job.description}</p>
        </div>

        {user?.role === "JOB_SEEKER" && (
          <div className="application-form-section">
            <h2>Apply for this Job</h2>
            <p>
              Enter the URL of your resume to submit
              your application.
            </p>

            <form onSubmit={handleApply}>
              <div className="form-group">
                <label htmlFor="resumeUrl">Resume URL</label>

                <input
                  id="resumeUrl"
                  type="url"
                  placeholder="https://example.com/my-resume.pdf"
                  value={resumeUrl}
                  onChange={(e) => setResumeUrl(e.target.value)}
                  maxLength={500}
                  required
                />
              </div>

              {applicationMessage && (
                <p className="application-success" role="status">
                  {applicationMessage}
                </p>
              )}

              {applicationError && (
                <p className="auth-error" role="alert">
                  {applicationError}
                </p>
              )}

              <button
                type="submit"
                className="apply-button"
                disabled={applying}
              >
                {applying ? "Submitting..." : "Apply Now"}
              </button>
            </form>
          </div>
        )}

        {user?.role === "RECRUITER" && (
          <p className="recruiter-info">
            Recruiters can view jobs but cannot apply for them.
          </p>
        )}
      </div>
    </div>
  );
}

export default JobDetails;
