CREATE TABLE IF NOT EXISTS public.time_off
(
    time_off_id SERIAL NOT NULL,
    name character varying(30) COLLATE pg_catalog."default" NOT NULL,
    description character varying(200) COLLATE pg_catalog."default" NOT NULL,
    amount integer NOT NULL,
    time_unit character varying(10) COLLATE pg_catalog."default" NOT NULL,
    is_active integer DEFAULT 1,
    CONSTRAINT time_off_pkey PRIMARY KEY (time_off_id)
);

INSERT INTO time_off (name, description, amount, time_unit, is_active)
VALUES 
('Holidays','Holiday pay compensates employees for time off during holidays',48,'HOURS',1),
('Sick Leave','Paid time off that an employee can use when they are sick or injured',32,'HOURS',1),
('Vacation Days','Paid time off given to employees to travel, spend time with family or friends, or take a break from work',48,'HOURS',1),
('Parental Leave','Paid time off away from work that employees can use for maternity leave, paternity leave, or adoption',30,'DAYS',1),
('Bereavement','Paid bereavement leave is time off employees receive when a family member or friend passes away.',7,'DAYS',1),
('Personal time','Employees can use personal time off to handle things like doctor’s appointments, car checkups, attending events 
 (e.g., parent-teacher conferences)',7,'DAYS',2);

CREATE TABLE IF NOT EXISTS public.time_off_employee
(
    time_off_employee_id SERIAL,
    time_off_id integer NOT NULL,
    employee_id integer NOT NULL,
    name character varying(30) COLLATE pg_catalog."default" NOT NULL,
    description character varying(200) COLLATE pg_catalog."default" NOT NULL,
    amount integer NOT NULL,
    time_unit character varying(10) COLLATE pg_catalog."default" NOT NULL,
    creation_date date DEFAULT now(),
    CONSTRAINT "Time_Off_Employee_pkey" PRIMARY KEY (time_off_employee_id),
    CONSTRAINT time_off_id FOREIGN KEY (time_off_id)
        REFERENCES public.time_off (time_off_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

INSERT INTO time_off_employee (time_off_id,employee_id,name,description,amount,time_unit,creation_date)
 VALUES
(1, 1, 'Holidays','Holiday pay compensates employees for time off during holidays',48,'HOURS','2022-01-01'),
(2, 1, 'Sick Leave','Paid time off that an employee can use when they are sick or injured',32,'HOURS','2022-01-01'),
(3, 2, 'Vacation Days','Paid time off given to employees to travel, spend time with family or friends, or take a break from work',48,'HOURS','2022-01-01'),
(4, 2,  'Parental Leave','Paid time off away from work that employees can use for maternity leave, paternity leave, or adoption',30,'DAYS','2022-01-01'),
(5, 3, 'Bereavement','Paid bereavement leave is time off employees receive when a family member or friend passes away.',7,'DAYS', '2022-01-01'),
(1, 3, 'Holidays','Holiday pay compensates employees for time off during holidays',48,'HOURS','2022-01-01'),
(2, 3, 'Sick Leave','Paid time off that an employee can use when they are sick or injured',32,'HOURS','2022-01-01'),
(3, 4, 'Vacation Days','Paid time off given to employees to travel, spend time with family or friends, or take a break from work',48,'HOURS','2022-01-01'),
(4, 4,  'Parental Leave','Paid time off away from work that employees can use for maternity leave, paternity leave, or adoption',30,'DAYS','2022-01-01'),
(5, 5, 'Bereavement','Paid bereavement leave is time off employees receive when a family member or friend passes away.',7,'DAYS', '2022-01-01'),
(1, 5, 'Holidays','Holiday pay compensates employees for time off during holidays',48,'HOURS','2022-01-01'),
(2, 5, 'Sick Leave','Paid time off that an employee can use when they are sick or injured',32,'HOURS','2022-01-01'),
(3, 5, 'Vacation Days','Paid time off given to employees to travel, spend time with family or friends, or take a break from work',48,'HOURS','2022-01-01'),
(4, 6,  'Parental Leave','Paid time off away from work that employees can use for maternity leave, paternity leave, or adoption',30,'DAYS','2022-01-01'),
(5, 6, 'Bereavement','Paid bereavement leave is time off employees receive when a family member or friend passes away.',7,'DAYS', '2022-01-01'),
(1, 7, 'Holidays','Holiday pay compensates employees for time off during holidays',48,'HOURS','2022-01-01'),
(2, 7, 'Sick Leave','Paid time off that an employee can use when they are sick or injured',32,'HOURS','2022-01-01'),
(3, 7, 'Vacation Days','Paid time off given to employees to travel, spend time with family or friends, or take a break from work',48,'HOURS','2022-01-01'),
(4, 8,  'Parental Leave','Paid time off away from work that employees can use for maternity leave, paternity leave, or adoption',30,'DAYS','2022-01-01'),
(5, 8, 'Bereavement','Paid bereavement leave is time off employees receive when a family member or friend passes away.',7,'DAYS', '2022-01-01'),
(1, 8, 'Holidays','Holiday pay compensates employees for time off during holidays',48,'HOURS','2022-01-01'),
(2, 9, 'Sick Leave','Paid time off that an employee can use when they are sick or injured',32,'HOURS','2022-01-01'),
(3, 9, 'Vacation Days','Paid time off given to employees to travel, spend time with family or friends, or take a break from work',48,'HOURS','2022-01-01'),
(4, 9,  'Parental Leave','Paid time off away from work that employees can use for maternity leave, paternity leave, or adoption',30,'DAYS','2022-01-01'),
(5, 10, 'Bereavement','Paid bereavement leave is time off employees receive when a family member or friend passes away.',7,'DAYS', '2022-01-01'),
(1, 10, 'Holidays','Holiday pay compensates employees for time off during holidays',48,'HOURS','2022-01-01'),
(2, 11, 'Sick Leave','Paid time off that an employee can use when they are sick or injured',32,'HOURS','2022-01-01'),
(3, 11, 'Vacation Days','Paid time off given to employees to travel, spend time with family or friends, or take a break from work',48,'HOURS','2022-01-01'),
(4, 12,  'Parental Leave','Paid time off away from work that employees can use for maternity leave, paternity leave, or adoption',30,'DAYS','2022-01-01'),
(5, 12, 'Bereavement','Paid bereavement leave is time off employees receive when a family member or friend passes away.',7,'DAYS', '2022-01-01');

CREATE TABLE IF NOT EXISTS public.time_off_requests
(
    time_off_request_id SERIAL,
    employee_id integer NOT NULL,
    manager_id integer NOT NULL,
    start_date date NOT NULL,
    end_date date NOT NULL,
    requested_time integer NOT NULL,
    status character varying(10) COLLATE pg_catalog."default" DEFAULT 1,
    time_off_employee_id integer,
    CONSTRAINT time_off_requests_pkey PRIMARY KEY (time_off_request_id)
);

INSERT INTO time_off_requests (employee_id,manager_id,start_date,end_date,requested_time,status,time_off_employee_id)
VALUES
(1,1,'2022-03-04','2022-03-05', 16,'APPROVED', 1),
(1,1,'2022-06-04','2022-06-05', 16,'PENDING', 2),
(1,1,'2022-02-04','2022-02-05', 16,'DENIED', 3),
(2,1,'2022-02-04','2022-02-05', 16,'APPROVED', 4),
(3,1,'2022-02-04','2022-02-05', 16,'PENDING', 5),
(4,1,'2022-12-04','2022-12-05', 16,'PENDING', 6),
(4,1,'2022-12-04','2022-12-05', 16,'PENDING', 7),
(5,1,'2022-11-04','2022-11-05', 16,'APPROVED', 8),
(6,1,'2022-01-04','2022-01-05', 16,'PENDING', 9),
(7,1,'2022-01-04','2022-01-05', 16,'DENIED', 10),
(8,1,'2022-11-04','2022-11-05', 16,'APPROVED', 11),
(9,1,'2022-08-04','2022-08-05', 16,'APPROVED', 12),
(10,1,'2022-09-04','2022-09-05', 16,'PENDING', 13),
(11,1,'2022-11-04','2022-11-05', 16,'APPROVED', 14),
(12,1,'2022-12-04','2022-12-05', 16,'DENIED', 15),
(7,1,'2022-06-04','2022-06-05', 16,'PENDING', 16),
(8,1,'2022-02-04','2022-02-05', 16,'APPROVED', 17),
(11,1,'2022-09-04','2022-09-05', 16,'APPROVED', 18);