import api from "./api";

export const scheduleInterview = (data) =>
  api.post("/interview/schedule", data);

export const getAllInterviews = () =>
  api.get("/interview/all");

export const getInterviewById = (id) =>
  api.get(`/interview/${id}`);

export const getInterviewsByApplication = (applicationId) =>
  api.get(`/interview/application/${applicationId}`);

export const updateInterview = (id, data) =>
  api.put(`/interview/update/${id}`, data);

export const updateInterviewStatus = (id, status) =>
  api.patch(`/interview/updateStatus/${id}`, null, {
    params: { status }
  });

export const deleteInterview = (id) =>
  api.delete(`/interview/delete/${id}`);