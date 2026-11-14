
import { BrowserRouter, Routes, Route } from "react-router-dom";

import Navbar from "./components/Navbar";
import Footer from "./components/Footer";
import ProtectedRoute from "./components/ProtectedRoute";

import Home from "./pages/Home";
import Login from "./pages/Login";
import Register from "./pages/Register";

import JobSeekerDashboard from "./pages/JobSeekerDashboard";
import RecruiterDashboard from "./pages/RecruiterDashboard";
import RecruiterJobs from "./pages/RecruiterJobs";
import RecruiterApplications from "./pages/RecruiterApplications";

import Jobs from "./pages/Jobs";
import JobDetails from "./pages/JobDetails";
import MyApplications from "./pages/MyApplications";
import MyCompanies from "./pages/MyCompanies";

import RecruiterInterviews from "./pages/RecruiterInterviews";
import MyInterviews from "./pages/MyInterviews";

import "./App.css";

function App() {
  return (
    <BrowserRouter>
      <Navbar />

      <Routes>
        {/* Public Routes */}
        <Route path="/" element={<Home />} />
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />

        {/* Jobs - Both roles can access */}
        <Route element={<ProtectedRoute />}>
          <Route path="/jobs" element={<Jobs />} />
          <Route path="/jobs/:id" element={<JobDetails />} />
        </Route>

        {/* Job Seeker Routes */}
        <Route element={<ProtectedRoute allowedRole="JOB_SEEKER" />}>
          <Route
            path="/seeker/dashboard"
            element={<JobSeekerDashboard />}
          />

          <Route
            path="/my-applications"
            element={<MyApplications />}
          />

          <Route
            path="/my-interviews"
            element={<MyInterviews />}
          />
        </Route>

        {/* Recruiter Routes */}
        <Route element={<ProtectedRoute allowedRole="RECRUITER" />}>
          <Route
            path="/recruiter/dashboard"
            element={<RecruiterDashboard />}
          />
          <Route
              path="/recruiter/companies"
              element={<MyCompanies />}
          />
          <Route
             path="/recruiter/jobs"
             element={<RecruiterJobs />}
          />
          <Route
            path="/recruiter/applications"
            element={<RecruiterApplications />}
          />
          <Route
            path="/recruiter/interviews"
            element={<RecruiterInterviews />}
          />
        </Route>
      </Routes>

      <Footer />
    </BrowserRouter>
  );
}

export default App;
