
import api from "./api";

// JOB SEEKER

export const applyForJob = (applicationData) => {
  return api.post("/application/create", applicationData);
};

export const getMyApplications = () => {
  return api.get("/application/myApplications");
};

export const withdrawApplication = (id) => {
  return api.delete(`/application/withdraw/${id}`);
};

// RECRUITER

export const getAllApplications = () => {
  return api.get("/application/allApplications");
};

export const getApplicationsByJob = (jobId) => {
  return api.get(`/application/getApplicationsByJob/${jobId}`);
};

export const updateApplicationStatus = (id, status) => {
  return api.patch(
    `/application/updateApplicationStatus/${id}`,
    null,
    {
      params: { status }
    }
  );
};
