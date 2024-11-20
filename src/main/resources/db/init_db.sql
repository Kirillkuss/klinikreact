CREATE TABLE Document(
	id_document serial PRIMARY KEY,
	type_document VARCHAR ( 40 ) NOT NULL,
	seria VARCHAR ( 20 ) NOT NULL ,
	numar VARCHAR ( 20 ) NOT NULL UNIQUE,
	snils VARCHAR ( 20 ) UNIQUE,
	polis VARCHAR ( 20 ) UNIQUE
);

CREATE TABLE Patient(
	id_patient serial PRIMARY KEY,
	surname VARCHAR ( 30 ) NOT NULL,
	name VARCHAR ( 30 ) NOT NULL,
	full_name VARCHAR ( 30 ) NOT NULL,
	gender VARCHAR ( 5 ) NOT NULL,
	phone VARCHAR ( 13 ) NOT NULL UNIQUE,
	address VARCHAR ( 100 ) NOT NULL ,
	document_id int unique references public.Document ( id_document )
);

CREATE TABLE Doctor(
	id_doctor serial PRIMARY KEY,
	surname VARCHAR ( 60 ) NOT NULL,
	name VARCHAR ( 60 ) NOT NULL,
	full_name VARCHAR ( 60 ) NOT NULL
);

CREATE TABLE Complaint(
	id_complaint serial PRIMARY KEY,
	functional_impairment VARCHAR ( 100 ) NOT NULL UNIQUE
);

CREATE TABLE Rehabilitation_solution(
	id_rehabilitation_solution serial PRIMARY KEY,
	name VARCHAR ( 100 ) NOT NULL UNIQUE,
	survey_plan VARCHAR ( 255 ) NOT NULL
);

CREATE TABLE Card_patient(
	id_card_patient serial PRIMARY KEY,
	diagnosis VARCHAR ( 50 ) NOT NULL,
	allergy   bool NOT NULL,
	note VARCHAR ( 255 ),
	сonclusion VARCHAR ( 255 ),
	patient_id int unique references public.Patient ( id_patient )
);

CREATE TABLE Drug_treatment(
    id_drug_treatment serial PRIMARY KEY,
    name VARCHAR ( 255 ) NOT NULL
);

CREATE TABLE Drug(
	id_drug serial PRIMARY KEY,
	name VARCHAR ( 255 ) NOT NULL UNIQUE,
	drug_treatment_id int8 NOT NULL,
	FOREIGN KEY (drug_treatment_id) REFERENCES public.Drug_treatment(id_drug_treatment)
);

CREATE TABLE Treatment (
	id_treatment serial PRIMARY KEY,
	time_start_treatment timestamp(6) NOT NULL,
	end_time_treatment timestamp(6) NOT NULL,
	drug_id int8 NOT NULL,
	card_patient_id int8 NOT NULL,
	doctor_id int8 NOT NULL,						
	rehabilitation_solution_id int8 NOT NULL,
	FOREIGN KEY (rehabilitation_solution_id) REFERENCES public.Rehabilitation_solution(id_rehabilitation_solution),
	FOREIGN KEY (doctor_id) REFERENCES public.Doctor(id_doctor),
    FOREIGN KEY (card_patient_id) REFERENCES public.Card_patient(id_card_patient),
    FOREIGN KEY (drug_id) REFERENCES public.Drug(id_dr)
);

CREATE TABLE Record_patient(
	id_record serial PRIMARY KEY,
	date_record timestamp(6) NOT NULL,
	date_appointment timestamp(6) NOT NULL,
	number_room int4 NOT NULL,
	doctor_id int8 NOT NULL,
	card_patient_id  int8 NOT NULL,
	FOREIGN KEY (doctor_id) REFERENCES public.Doctor(id_doctor) ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY (card_patient_id) REFERENCES public.Card_patient(id_card_patient) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE Type_complaint(
	id_type_complaint serial PRIMARY KEY,
	name VARCHAR ( 150 ) NOT NULL UNIQUE,
	complaint_id int8 NOT NULL,
	FOREIGN KEY (complaint_id) REFERENCES public.Complaint(id_complaint)
);

CREATE TABLE Card_patient_Complaint(
	card_patient_id int8 NOT NULL,
	type_complaint_id int8 NOT NULL,
	PRIMARY KEY (card_patient_id, type_complaint_id),
	FOREIGN KEY (card_patient_id) REFERENCES Card_patient(id_card_patient),
	FOREIGN KEY (type_complaint_id) REFERENCES Type_complaint(id_type_complaint)
);

CREATE TABLE kl_user(
	id serial PRIMARY KEY,
	login VARCHAR( 50 ) NOT NULL,
	password VARCHAR( 250 ) NOT NULL,
	role VARCHAR( 5 ) NOT NULL,
	email VARCHAR( 50 ),
	salt VARCHAR( 250 ) NOT NULL,
	status BOOLEAN DEFAULT FALSE 
);

CREATE TABLE Key_Entity(
	id_key serial PRIMARY KEY,
	key_alice VARCHAR ( 255 ) NOT NULL,
	date_create timestamp(6) NOT NULL,
	key_public TEXT NOT NULL, 
	key_private TEXT  NOT NULL
);

CREATE TABLE User_Blocking(
	id_block serial PRIMARY KEY,
	date_block timestamp(6) NOT NULL,
	date_plan_unblock timestamp(6),
	date_unblock timestamp(6),
	user_id int8 NOT NULL,
	status_block int,
	status BOOLEAN,
	FOREIGN KEY (user_id) REFERENCES public.kl_user(id)
);

CREATE TABLE klinik_animal(
	id_animal serial PRIMARY KEY,
	name_animal VARCHAR( 100 ),
	amount int8 NOT NULL,
	count int4 NOT NULL,
	date_record timestamp(6) NOT NULL
);

