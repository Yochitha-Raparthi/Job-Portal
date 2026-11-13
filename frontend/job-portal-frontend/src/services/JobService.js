import api from "./api";

export const getAllJobs = () => {
  return api.get("/job/getAll");
};

export const getJobById = (id) => {
  return api.get(`/job/getJobById/${id}`);
};

export const getJobsByCompany = (companyId) => {
  return api.get(`/job/getJobByCompany/${companyId}`);
};

export const searchJobsByTitle = (title) => {
  return api.get(`/job/getJobByTitle/${encodeURIComponent(title)}`);
};