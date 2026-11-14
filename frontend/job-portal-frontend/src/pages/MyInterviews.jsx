
import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { getMyApplications } from "../services/ApplicationService";
import { getInterviewsByApplication } from "../services/InterviewService";
import "./Interviews.css";

function MyInterviews() {
  const [interviews, setInterviews] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    let active = true;

    const loadInterviews = async () => {
      try {
        const applicationResponse = await getMyApplications();
        const applications = applicationResponse.data;

        const results = await Promise.all(
          applications.map((application) =>
            getInterviewsByApplication(application.id)
          )
        );

        const allInterviews = results.flatMap(
          (result) => result.data
        );

        allInterviews.sort(
          (a, b) =>
            new Date(a.interviewDate) -
            new Date(b.interviewDate)
        );

        if (active) {
          setInterviews(allInterviews);
        }
      } catch (err) {
        if (active) {
          setError(
            err.response?.data?.message ||
            "Unable to load your interviews."
          );
        }
      } finally {
        if (active) setLoading(false);
      }
    };

    loadInterviews();

    return () => {
      active = false;
    };
  }, []);

  return (
    <main className="interviews-page">
      <div className="interviews-header">
        <div>
          <span className="eyebrow">JOB SEEKER WORKSPACE</span>
          <h1>My Interviews</h1>
          <p>Track your interview schedules and updates.</p>
        </div>
      </div>

      {error && (
        <p className="iv-error" role="alert">{error}</p>
      )}

      {loading ? (
        <p>Loading your interviews...</p>
      ) : interviews.length === 0 ? (
        <div className="iv-empty">
          <h2>No interviews scheduled</h2>
          <p>
            Your interview details will appear here when
            recruiters schedule interviews for your applications.
          </p>
          <Link to="/my-applications">
            View My Applications
          </Link>
        </div>
      ) : (
        <div className="iv-grid">
          {interviews.map((interview) => (
            <article className="iv-card" key={interview.id}>
              <div className="iv-card-top">
                <span className="iv-pill">
                  {interview.status}
                </span>
              </div>

              <h3>{interview.jobTitle}</h3>

              <p>
                <strong>Interview Date:</strong>{" "}
                {interview.interviewDate
                  ? new Date(
                      interview.interviewDate
                    ).toLocaleString()
                  : "Not available"}
              </p>

              <p>
                <strong>Mode:</strong>{" "}
                {interview.interviewMode}
              </p>

              <p>
                <strong>
                  {interview.interviewMode === "Online"
                    ? "Meeting Link:"
                    : "Interview Venue:"}
                </strong>{" "}
                {interview.interviewMode === "Online" &&
                /^https?:\/\//i.test(interview.meetingLink || "") ? (
                  <a
                    href={interview.meetingLink}
                    target="_blank"
                    rel="noopener noreferrer"
                  >
                    Join Interview
                  </a>
                ) : (
                  interview.meetingLink
                )}
              </p>

              <div className="iv-actions">
                <Link
                  className="iv-primary"
                  to={`/jobs/${interview.jobId}`}
                >
                  View Job Details
                </Link>
              </div>
            </article>
          ))}
        </div>
      )}
    </main>
  );
}

export default MyInterviews;
