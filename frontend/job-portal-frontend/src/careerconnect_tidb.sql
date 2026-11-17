 CONSTRAINT `fk_interview_application` FOREIGN KEY (`application_id`) REFERENCES `application` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

INSERT INTO `user` (`id`,`created_at`,`email`,`name`,`password`,`role`) VALUES (1,'2026-11-05 23:03:01.777403','ramesh.demo@example.com','Ramesh Kumar','DISABLED_DEMO_ACCOUNT__Vj_GYkznnstvd2sO3bCMvu2nZdukaMWjxxZIyr-VOS8D_i4','RECRUITER');
INSERT INTO `user` (`id`,`created_at`,`email`,`name`,`password`,`role`) VALUES (2,'2026-11-05 23:03:01.777403','priya.demo@example.com','Priya Sharma','DISABLED_DEMO_ACCOUNT_v0XFOMYWJoSHs2NNXonVplQTYdgsAdwhj3Z5iIOtZhYq-I28','JOB_SEEKER');
INSERT INTO `user` (`id`,`created_at`,`email`,`name`,`password`,`role`) VALUES (3,'2026-11-05 23:03:01.777403','suresh.demo@example.com','Suresh Reddy','DISABLED_DEMO_ACCOUNT_7rBrKHfBaK96UmZh_RIIUa7xgqdYW3GtVl2IURUEawcWI4Su','RECRUITER');
INSERT INTO `user` (`id`,`created_at`,`email`,`name`,`password`,`role`) VALUES (4,'2026-11-05 23:03:01.777403','rahul.demo@example.com','Rahul Verma','DISABLED_DEMO_ACCOUNT_enpy-od6wseeGH4Bq9LPpFGhO-O3PhDYm027Mt_k3P4nS-n7','JOB_SEEKER');
INSERT INTO `user` (`id`,`created_at`,`email`,`name`,`password`,`role`) VALUES (5,'2026-11-05 23:03:01.777403','anjali.demo@example.com','Anjali Rao','DISABLED_DEMO_ACCOUNT_docGi0mE4C-nw0aoYX4l9hXoJl5d7mI6YMJFxkh-oFYw40Tm','JOB_SEEKER');
INSERT INTO `user` (`id`,`created_at`,`email`,`name`,`password`,`role`) VALUES (6,'2026-11-05 23:03:01.777403','recruiter.demo@example.com','Demo Recruiter','DISABLED_DEMO_ACCOUNT_okM_hYdQ-XRcWVEE_q2ND2-A9WIWx6Br4G31F1QUCx5aEKBC','RECRUITER');
INSERT INTO `user` (`id`,`created_at`,`email`,`name`,`password`,`role`) VALUES (7,'2026-11-05 23:03:01.777403','seeker.demo@example.com','Demo Seeker','DISABLED_DEMO_ACCOUNT_bsXgLX-QXlrx3fkassobrpixowKeNUzILDsAeCCbyZ7L0B4A','JOB_SEEKER');

INSERT INTO `company` VALUES (1,'TechGen Technologies','Software development company','Hyderabad','https://example.com',1);
INSERT INTO `company` VALUES (2,'NextGen Technologies','Software development company','Hyderabad','https://example.com',1);
INSERT INTO `company` VALUES (3,'TechNova Solutions','Enterprise applications and cloud solutions','Hyderabad','https://example.com',1);
INSERT INTO `company` VALUES (4,'DataSphere Technologies','Data analytics and machine learning','Bengaluru','https://example.com',3);
INSERT INTO `company` VALUES (5,'Infosys','Sample portfolio listing; not an official recruitment posting','Hyderabad','https://www.infosys.com/',1);

INSERT INTO `job` VALUES (1,'Looking for a Java developer with knowledge of Core Java, OOPs, Spring Boot, REST APIs, MySQL and Hibernate.','Full-time','Hyderabad','2026-11-05 23:12:16.600281',450000,'Java Backend Developer',1);
INSERT INTO `job` VALUES (2,'Looking for candidates with Python, Machine Learning, Pandas, NumPy and Scikit-learn knowledge.','Internship','Bangalore','2026-11-05 23:14:26.132531',20000,'AI and Machine Learning Intern',1);
INSERT INTO `job` VALUES (3,'Spring Boot and REST API development','Full Time','Hyderabad','2026-11-10 17:15:23.398274',500000,'Java Backend Developer',1);
INSERT INTO `job` VALUES (4,'Work on backend development, debugging, API integration and database management.','Full-Time','Remote','2026-11-10 22:12:42.879294',450000,'Junior Software Engineer',1);
INSERT INTO `job` VALUES (5,'Analyze business data using SQL, Python and Power BI. Build dashboards and generate actionable insights.','Full-Time','Bengaluru','2026-11-10 22:16:28.308545',500000,'Data Analyst',4);

INSERT INTO `application` VALUES (1,'2026-11-05 23:19:21.671518','https://example.com/demo-resume-1.pdf','REJECTED',2,1);
INSERT INTO `application` VALUES (2,'2026-11-10 22:19:09.818837','https://example.com/demo-resume-2.pdf','SHORTLISTED',2,2);
INSERT INTO `application` VALUES (3,'2026-11-10 22:20:44.661449','https://example.com/demo-resume-3.pdf','SHORTLISTED',4,3);
INSERT INTO `application` VALUES (4,'2026-11-10 22:21:56.924900','https://example.com/demo-resume-4.pdf','APPLIED',5,3);
INSERT INTO `application` VALUES (5,'2026-11-14 22:03:08.209538','https://example.com/demo-resume-5.pdf','PENDING',4,5);

INSERT INTO `interview` VALUES (1,'2026-11-17 10:30:00.000000','Online','https://example.com/demo-interview-1','RESCHEDULED',1);
INSERT INTO `interview` VALUES (3,'2026-11-17 11:00:00.000000','Online','https://example.com/demo-interview-3','SCHEDULED',4);
INSERT INTO `interview` VALUES (5,'2026-11-16 12:30:00.000000','Online','https://example.com/demo-interview-5','SCHEDULED',2);

-- Verification
SELECT COUNT(*) AS user_count FROM `user`;
SELECT COUNT(*) AS company_count FROM `company`;
SELECT COUNT(*) AS job_count FROM `job`;
SELECT COUNT(*) AS application_count FROM `application`;
SELECT COUNT(*) AS interview_count FROM `interview`;
