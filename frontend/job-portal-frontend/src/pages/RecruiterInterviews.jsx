
import { useEffect, useState } from "react";
import { getAllApplications } from "../services/ApplicationService";

import {
  scheduleInterview,
  getAllInterviews,
  updateInterview,
  updateInterviewStatus,
  deleteInterview
} from "../services/InterviewService";

import "./Interviews.css";

const initialForm = {
  applicationId: "",
  interviewDate: "",
  interviewMode: "Online",
  meetingLink: ""
};

const getError = (err) => {
  const data = err.response?.data;

  if (typeof data === "string") return data;

  return (
    data?.message ||
    data?.detail ||
    "Request failed. Please try again."
  );
};

const toLocalInput = (dateString) =>
  dateString ? dateString.slice(0, 16) : "";

const displayDate = (dateString) =>
  dateString
    ? new Date(dateString).toLocaleString()
    : "Not available";

const isFinalized = (interview) =>
  interview.status === "COMPLETED" ||
  interview.status === "CANCELLED";

function RecruiterInterviews() {
  const [applications, setApplications] = useState([]);
  const [interviews, setInterviews] = useState([]);

  const [form, setForm] = useState({ ...initialForm });

  const [editingId, setEditingId] = useState(null);
  const [showForm, setShowForm] = useState(false);

  const [loading, setLoading] = useState(true);
  const [busy, setBusy] = useState(false);

  const [error, setError] = useState("");
  const [message, setMessage] = useState("");

  // Load applications and interviews.
  useEffect(() => {
    let active = true;

    const loadData = async () => {
      setLoading(true);

      try {
        const [applicationResult, interviewResult] =
          await Promise.all([
            getAllApplications(),
            getAllInterviews()
          ]);

        if (active) {
          setApplications(applicationResult.data);
          setInterviews(interviewResult.data);
        }
      } catch (err) {
        if (active) setError(getError(err));
      } finally {
        if (active) setLoading(false);
      }
    };

    loadData();

    return () => {
      active = false;
    };
  }, []);

  // Only shortlisted candidates are eligible.
  const shortlisted = applications.filter(
    (application) => application.status === "SHORTLISTED"
  );

  // Interview statistics.
  const totalCount = interviews.length;

  const scheduledCount = interviews.filter(
    (i) => i.status === "SCHEDULED"
  ).length;

  const rescheduledCount = interviews.filter(
    (i) => i.status === "RESCHEDULED"
  ).length;

  const completedCount = interviews.filter(
    (i) => i.status === "COMPLETED"
  ).length;

  const cancelledCount = interviews.filter(
    (i) => i.status === "CANCELLED"
  ).length;

  // Open schedule form.
  const openCreate = () => {
    setForm({ ...initialForm });
    setEditingId(null);
    setError("");
    setMessage("");
    setShowForm(true);
  };

  // Open edit form.
  const openEdit = (interview) => {
    if (isFinalized(interview)) {
      setError(
        "Completed or cancelled interviews cannot be edited."
      );
      return;
    }

    setForm({
      applicationId: String(interview.applicationId),
      interviewDate: toLocalInput(interview.interviewDate),
      interviewMode: interview.interviewMode,
      meetingLink: interview.meetingLink || ""
    });

    setEditingId(interview.id);
    setError("");
    setMessage("");
    setShowForm(true);

    window.scrollTo({
      top: 0,
      behavior: "smooth"
    });
  };

  // Handle form input changes.
  const handleChange = (e) => {
    const { name, value } = e.target;

    setForm((previous) => ({
      ...previous,
      [name]: value
    }));
  };

  // Close schedule/edit form.
  const closeForm = () => {
    setShowForm(false);
    setEditingId(null);
    setForm({ ...initialForm });
    setError("");
  };

  // Schedule or update interview.
  const handleSubmit = async (e) => {
    e.preventDefault();

    setError("");
    setMessage("");

    if (!form.applicationId) {
      setError("Please select a shortlisted candidate.");
      return;
    }

    const selectedDate = new Date(form.interviewDate);

    if (
      Number.isNaN(selectedDate.getTime()) ||
      selectedDate <= new Date()
    ) {
      setError(
        "Please select a future interview date and time."
      );
      return;
    }

    if (!form.meetingLink.trim()) {
      setError("Meeting link or venue is required.");
      return;
    }

    const data = {
      application: {
        id: Number(form.applicationId)
      },
      interviewDate: `${form.interviewDate}:00`,
      interviewMode: form.interviewMode,
      meetingLink: form.meetingLink.trim()
    };

    setBusy(true);

    try {
      let response;

      if (editingId !== null) {
        response = await updateInterview(editingId, data);
      } else {
        response = await scheduleInterview(data);
      }

      const savedInterview = response.data;

      if (editingId !== null) {
        setInterviews((previous) =>
          previous.map((interview) =>
            interview.id === editingId
              ? savedInterview
              : interview
          )
        );

        setMessage(
          savedInterview.status === "RESCHEDULED"
            ? "Interview rescheduled successfully."
            : "Interview updated successfully."
        );
      } else {
        setInterviews((previous) => [
          savedInterview,
          ...previous
        ]);

        setMessage("Interview scheduled successfully.");
      }

      closeForm();

    } catch (err) {
      setError(getError(err));
    } finally {
      setBusy(false);
    }
  };

  // Update interview status manually.
  const changeStatus = async (id, status) => {
    const interview = interviews.find(
      (item) => item.id === id
    );

    if (!interview || isFinalized(interview)) {
      return;
    }

    setBusy(true);
    setError("");
    setMessage("");

    try {
      const response = await updateInterviewStatus(
        id,
        status
      );

      setInterviews((previous) =>
        previous.map((item) =>
          item.id === id ? response.data : item
        )
      );

      setMessage(
        `Interview status updated to ${status}.`
      );

    } catch (err) {
      setError(getError(err));
    } finally {
      setBusy(false);
    }
  };

  // Delete interview.
  const handleDelete = async (id) => {
    if (!window.confirm("Delete this interview?")) {
      return;
    }

    setBusy(true);
    setError("");
    setMessage("");

    try {
      await deleteInterview(id);

      setInterviews((previous) =>
        previous.filter((interview) => interview.id !== id)
      );

      setMessage("Interview deleted successfully.");

    } catch (err) {
      setError(getError(err));
    } finally {
      setBusy(false);
    }
  };

  return (
    <main className="interviews-page">

      {/* PAGE HEADER */}
      <div className="interviews-header">
        <div>
          <span className="eyebrow">
            RECRUITER WORKSPACE
          </span>

          <h1>Interview Management</h1>

          <p>
            Schedule, reschedule and manage candidate interviews.
          </p>
        </div>

        <button
          type="button"
          className="iv-primary"
          onClick={openCreate}
          disabled={loading || busy}
        >
          + Schedule Interview
        </button>
      </div>

      {/* MESSAGES */}
      {error && (
        <p className="iv-error" role="alert">
          {error}
        </p>
      )}

      {message && (
        <p className="iv-success" role="status">
          {message}
        </p>
      )}

      {/* SCHEDULE / EDIT FORM */}
      {showForm && (
        <section className="iv-panel">

          <h2>
            {editingId !== null
              ? "Edit / Reschedule Interview"
              : "Schedule New Interview"}
          </h2>

          <form onSubmit={handleSubmit}>
            <div className="iv-form-grid">

              {/* SHORTLISTED CANDIDATE */}
              <div className="iv-field">
                <label htmlFor="applicationId">
                  Shortlisted Candidate
                </label>

                <select
                  id="applicationId"
                  name="applicationId"
                  value={form.applicationId}
                  onChange={handleChange}
                  disabled={editingId !== null}
                  required
                >
                  <option value="">
                    Select application
                  </option>

                  {editingId !== null &&
                    !shortlisted.some(
                      (a) =>
                        String(a.id) === form.applicationId
                    ) && (
                      <option value={form.applicationId}>
                        Existing application #{form.applicationId}
                      </option>
                    )}

                  {shortlisted.map((application) => (
                    <option
                      key={application.id}
                      value={application.id}
                    >
                      {application.applicantName}
                      {" — "}
                      {application.jobTitle}
                    </option>
                  ))}
                </select>
              </div>

              {/* INTERVIEW DATE */}
              <div className="iv-field">
                <label htmlFor="interviewDate">
                  Interview Date & Time
                </label>

                <input
                  id="interviewDate"
                  type="datetime-local"
                  name="interviewDate"
                  value={form.interviewDate}
                  onChange={handleChange}
                  required
                />
              </div>

              {/* INTERVIEW MODE */}
              <div className="iv-field">
                <label htmlFor="interviewMode">
                  Interview Mode
                </label>

                <select
                  id="interviewMode"
                  name="interviewMode"
                  value={form.interviewMode}
                  onChange={handleChange}
                  required
                >
                  <option value="Online">
                    Online
                  </option>

                  <option value="Offline">
                    Offline
                  </option>
                </select>
              </div>

              {/* MEETING LINK / VENUE */}
              <div className="iv-field">
                <label htmlFor="meetingLink">
                  {form.interviewMode === "Online"
                    ? "Meeting Link"
                    : "Interview Venue"}
                </label>

                <input
                  id="meetingLink"
                  type="text"
                  name="meetingLink"
                  value={form.meetingLink}
                  onChange={handleChange}
                  placeholder={
                    form.interviewMode === "Online"
                      ? "https://meet.google.com/..."
                      : "Office address or interview venue"
                  }
                  required
                />
              </div>

            </div>

            {/* FORM ACTIONS */}
            <div className="iv-actions">
              <button
                type="submit"
                className="iv-primary"
                disabled={busy}
              >
                {busy
                  ? "Saving..."
                  : editingId !== null
                    ? "Update Interview"
                    : "Schedule Interview"}
              </button>

              <button
                type="button"
                className="iv-secondary"
                onClick={closeForm}
                disabled={busy}
              >
                Cancel
              </button>
            </div>

          </form>
        </section>
      )}

      {/* INTERVIEW STATISTICS */}
      <div className="iv-summary">

        <div className="iv-stat">
          <span>Total Interviews</span>
          <strong>{totalCount}</strong>
        </div>

        <div className="iv-stat">
          <span>Scheduled</span>
          <strong>{scheduledCount}</strong>
        </div>

        <div className="iv-stat">
          <span>Rescheduled</span>
          <strong>{rescheduledCount}</strong>
        </div>

        <div className="iv-stat">
          <span>Completed</span>
          <strong>{completedCount}</strong>
        </div>

        <div className="iv-stat">
          <span>Cancelled</span>
          <strong>{cancelledCount}</strong>
        </div>

      </div>

      {/* INTERVIEW LIST */}
      <h2>All Interviews</h2>

      {loading ? (
        <p>Loading interviews...</p>
      ) : interviews.length === 0 ? (
        <div className="iv-empty">
          <h3>No interviews found</h3>
          <p>
            Schedule an interview for a shortlisted candidate.
          </p>
        </div>
      ) : (
        <div className="iv-grid">

          {interviews.map((interview) => {
            const finalized = isFinalized(interview);

            return (
              <article
                className="iv-card"
                key={interview.id}
              >
                <div className="iv-card-top">
                  <span className="iv-pill">
                    {interview.status}
                  </span>

                  <span>#{interview.id}</span>
                </div>

                <h3>{interview.applicantName}</h3>

                <p className="iv-job">
                  {interview.jobTitle}
                </p>

                <p>
                  <strong>Date:</strong>{" "}
                  {displayDate(interview.interviewDate)}
                </p>

                <p>
                  <strong>Mode:</strong>{" "}
                  {interview.interviewMode}
                </p>

                <p>
                  <strong>
                    {interview.interviewMode === "Online"
                      ? "Meeting:"
                      : "Venue:"}
                  </strong>{" "}

                  {interview.interviewMode === "Online" &&
                  /^https?:\/\//i.test(
                    interview.meetingLink || ""
                  ) ? (
                    <a
                      href={interview.meetingLink}
                      target="_blank"
                      rel="noopener noreferrer"
                    >
                      Open Meeting
                    </a>
                  ) : (
                    interview.meetingLink
                  )}
                </p>

                {/* INTERVIEW ACTIONS */}
                <div className="iv-actions">

                  <button
                    type="button"
                    className="iv-secondary"
                    onClick={() => openEdit(interview)}
                    disabled={busy || finalized}
                  >
                    Edit
                  </button>

                  <button
                    type="button"
                    className="iv-complete"
                    onClick={() =>
                      changeStatus(
                        interview.id,
                        "COMPLETED"
                      )
                    }
                    disabled={busy || finalized}
                  >
                    Complete
                  </button>

                  <button
                    type="button"
                    className="iv-warning"
                    onClick={() =>
                      changeStatus(
                        interview.id,
                        "CANCELLED"
                      )
                    }
                    disabled={busy || finalized}
                  >
                    Cancel Interview
                  </button>

                  <button
                    type="button"
                    className="iv-danger"
                    onClick={() =>
                      handleDelete(interview.id)
                    }
                    disabled={busy}
                  >
                    Delete
                  </button>

                </div>
              </article>
            );
          })}

        </div>
      )}
    </main>
  );
}

export default RecruiterInterviews;
