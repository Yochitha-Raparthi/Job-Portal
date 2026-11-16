import api from "./api";

export const getRecruiterDashboard = () => {
  return api.get("/dashboard/recruiter");
};

export const getSeekerDashboard = () => {
  return api.get("/dashboard/seeker");
};