import { useEffect, useState } from "react";
import { Link } from "react-router-dom";

import { getMyCompanies } from "../services/CompanyService";

import {
  getJobsByCompany,
  createJob,
  updateJob,
  deleteJob
} from "../services/JobService";

import "./RecruiterJobs.css";

const emptyJob = {
  title: "",
  description: "",
  location: "",
  salary: "",
  jobType: "FULL_TIME",
  companyId: ""
};

function RecruiterJobs() {
  const [companies, setCompanies] = useState([]);
  const [jobs, setJobs] = useState([]);
  const [selectedCompany, setSelectedCompany] = useState("ALL");

  const [form, setForm] = useState({ ...emptyJob });
  const [editingId, setEditingId] = useState(null);
  const [showForm, setShowForm] = useState(false);

  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [deletingId, setDeletingId] = useState(null);
  const [error, setError] = useState("");
  const [message, setMessage] = useState("");

  const loadData = async () => {
    setLoading(true);
    setError("");

    try {
      const companyResponse = await getMyCompanies();
      const myCompanies = companyResponse.data;

      setCompanies(myCompanies);

      if (myCompanies.length === 0) {
        setJobs([]);
        return;
      }

      const responses = await Promise.all(
        myCompanies.map((company) =>
          getJobsByCompany(company.id)
        )
      );

      const allJobs = responses.flatMap(
        (response) => response.data
      );

      const uniqueJobs = Array.from(
        new Map(allJobs.map((job) => [job.id, job])).values()
      );

      setJobs(uniqueJobs);

    } catch (err) {
      setError("Unable to load your jobs.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadData();
  }, []);

  const handleChange = (e) => {
    const { name, value } = e.target;

    setForm((previous) => ({
      ...previous,
      [name]: value
    }));
  };

  const openCreateForm = () => {
    setEditingId(null);

    setForm({
      ...emptyJob,
      companyId:
        selectedCompany !== "ALL"
          ? String(selectedCompany)
          : companies.length === 1
          ? String(companies[0].id)
          : ""
    });

    setMessage("");
    setError("");
    setShowForm(true);
  };

  const openEditForm = (job) => {
    setEditingId(job.id);

    setForm({
      title: job.title || "",
      description: job.description || "",
      location: job.location || "",
      salary: job.salary ?? "",
      jobType: job.jobType || "FULL_TIME",
      companyId: String(job.companyId)
    });

    setMessage("");
    setError("");
    setShowForm(true);
    window.scrollTo({ top: 0, behavior: "smooth" });
  };

  const closeForm = () => {
    setShowForm(false);
    setEditingId(null);
    setForm({ ...emptyJob });
    setError("");
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    setError("");
    setMessage("");

    if (!form.companyId) {
      setError("Please select a company.");
      return;
    }

    const salary = Number(form.salary);

    if (!Number.isFinite(salary) || salary <= 0) {
      setError("Salary must be greater than zero.");
      return;
    }

    const jobData = {
      title: form.title.trim(),
      description: form.description.trim(),
      location: form.location.trim(),
      salary,
      jobType: form.jobType,
      company: {
        id: Number(form.companyId)
      }
    };

    setSaving(true);

    try {
      if (editingId !== null) {
        await updateJob(editingId, jobData);
        setMessage("Job updated successfully!");
      } else {
        await createJob(jobData);
        setMessage("Job posted successfully!");
      }

      setShowForm(false);
      setEditingId(null);
      setForm({ ...emptyJob });

      await loadData();

    } catch (err) {
      setError(
        err.response?.data?.message ||
        "Unable to save job. Please check your details."
      );
    } finally {
      setSaving(false);
    }
  };

  const handleDelete = async (id) => {
    const confirmed = window.confirm(
      "Are you sure you want to delete this job?"
    );

    if (!confirmed) return;

    setDeletingId(id);
    setError("");
    setMessage("");

    try {
      await deleteJob(id);

      setJobs((previous) =>
        previous.filter((job) => job.id !== id)
      );

      setMessage("Job deleted successfully!");

    } catch (err) {
      setError(
        err.response?.data?.message ||
        "Unable to delete job. It may have related applications."
      );
    } finally {
      setDeletingId(null);
    }
  };

  const filteredJobs =
    selectedCompany === "ALL"
      ? jobs
      : jobs.filter(
          (job) => String(job.companyId) === selectedCompany
        );

  return (
    <div className="recruiter-jobs-page">

      <div className="recruiter-jobs-heading">
        <div>
          <span className="eyebrow">RECRUITER WORKSPACE</span>
          <h1>Manage Jobs</h1>
          <p>Post and manage jobs for your companies.</p>
        </div>

        {companies.length > 0 && !showForm && (
          <button
            type="button"
            className="rj-primary-btn"
            onClick={openCreateForm}
          >
            + Post New Job
          </button>
        )}
      </div>

      {message && (
        <p className="rj-success" role="status">{message}</p>
      )}

      {error && (
        <p className="rj-error" role="alert">{error}</p>
      )}

      {!loading && companies.length === 0 && (
        <div className="rj-empty">
          <h2>Create a company first</h2>
          <p>
            You need to create a company before posting jobs.
          </p>
          <Link to="/recruiter/companies">
            Go to My Companies
          </Link>
        </div>
      )}

      {showForm && (
        <div className="rj-form-card">
          <h2>
            {editingId !== null ? "Edit Job" : "Post New Job"}
          </h2>

          <form onSubmit={handleSubmit}>
            <div className="rj-form-grid">

              <div className="rj-field">
                <label htmlFor="title">Job Title</label>
                <input
                  id="title"
                  name="title"
                  value={form.title}
                  onChange={handleChange}
                  maxLength={100}
                  required
                />
              </div>

              <div className="rj-field">
                <label htmlFor="companyId">Company</label>
                <select
                  id="companyId"
                  name="companyId"
                  value={form.companyId}
                  onChange={handleChange}
                  required
                >
                  <option value="">Select Company</option>

                  {companies.map((company) => (
                    <option key={company.id} value={company.id}>
                      {company.companyName}
                    </option>
                  ))}
                </select>
              </div>

              <div className="rj-field">
                <label htmlFor="location">Location</label>
                <input
                  id="location"
                  name="location"
                  value={form.location}
                  onChange={handleChange}
                  required
                />
              </div>

              <div className="rj-field">
                <label htmlFor="salary">Salary (₹)</label>
                <input
                  id="salary"
                  name="salary"
                  type="number"
                  min="1"
                  step="any"
                  value={form.salary}
                  onChange={handleChange}
                  required
                />
              </div>

              <div className="rj-field">
                <label htmlFor="jobType">Job Type</label>
                <select
                  id="jobType"
                  name="jobType"
                  value={form.jobType}
                  onChange={handleChange}
                  required
                >
                  <option value="FULL_TIME">Full Time</option>
                  <option value="PART_TIME">Part Time</option>
                  <option value="INTERNSHIP">Internship</option>
                  <option value="CONTRACT">Contract</option>
                  <option value="REMOTE">Remote</option>
                </select>
              </div>

              <div className="rj-field rj-full-width">
                <label htmlFor="description">Job Description</label>
                <textarea
                  id="description"
                  name="description"
                  rows={6}
                  value={form.description}
                  onChange={handleChange}
                  maxLength={2000}
                  required
                />
              </div>

            </div>

            <div className="rj-form-actions">
              <button
                type="submit"
                className="rj-primary-btn"
                disabled={saving}
              >
                {saving
                  ? "Saving..."
                  : editingId !== null
                  ? "Update Job"
                  : "Post Job"}
              </button>

              <button
                type="button"
                className="rj-cancel-btn"
                onClick={closeForm}
                disabled={saving}
              >
                Cancel
              </button>
            </div>
          </form>
        </div>
      )}

      {!loading && companies.length > 0 && (
        <>
          <div className="rj-toolbar">
            <h2>My Job Listings</h2>

            <select
              value={selectedCompany}
              onChange={(e) =>
                setSelectedCompany(e.target.value)
              }
            >
              <option value="ALL">All Companies</option>

              {companies.map((company) => (
                <option key={company.id} value={company.id}>
                  {company.companyName}
                </option>
              ))}
            </select>
          </div>

          <p className="rj-count">
            {filteredJobs.length} jobs found
          </p>

          {filteredJobs.length === 0 ? (
            <div className="rj-empty">
              <h2>No jobs found</h2>
              <p>Post your first job to get started.</p>
            </div>
          ) : (
            <div className="rj-grid">

              {filteredJobs.map((job) => (
                <div className="rj-card" key={job.id}>

                  <div className="rj-card-header">
                    <span className="rj-job-type">
                      {job.jobType}
                    </span>

                    <span className="rj-date">
                      {job.postedAt
                        ? new Date(job.postedAt).toLocaleDateString()
                        : "Recently posted"}
                    </span>
                  </div>

                  <h3>{job.title}</h3>
                  <p className="rj-company">
                    {job.companyName}
                  </p>

                  <div className="rj-details">
                    <span>📍 {job.location}</span>
                    <span>
                      💰 ₹{job.salary?.toLocaleString()}
                    </span>
                  </div>

                  <p className="rj-description">
                    {job.description}
                  </p>

                  <div className="rj-card-actions">
                    <Link to={`/jobs/${job.id}`}>
                      View
                    </Link>

                    <button
                      type="button"
                      className="rj-edit-btn"
                      onClick={() => openEditForm(job)}
                    >
                      Edit
                    </button>

                    <button
                      type="button"
                      className="rj-delete-btn"
                      onClick={() => handleDelete(job.id)}
                      disabled={deletingId === job.id}
                    >
                      {deletingId === job.id
                        ? "Deleting..."
                        : "Delete"}
                    </button>
                  </div>

                </div>
              ))}

            </div>
          )}
        </>
      )}

      {loading && (
        <p className="rj-loading">Loading jobs...</p>
      )}

    </div>
  );
}

export default RecruiterJobs;
