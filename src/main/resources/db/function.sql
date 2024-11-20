-- Получениие количества Реабилитационных лечений пациента
CREATE OR REPLACE FUNCTION public.record_patient(r_id_patient bigint, OUT r_report_cur refcursor)
 RETURNS refcursor
 LANGUAGE plpgsql
AS $function$
declare
	BEGIN
		OPEN r_report_cur FOR
		SELECT t.name AS name_solution, COUNT( u.rehabilitation_solution_id ) AS count_solution
		FROM Treatment u
		LEFT JOIN Rehabilitation_solution t ON t.id_rehabilitation_solution = u.rehabilitation_solution_id
		LEFT JOIN Card_patient c ON  c.id_card_patient =u.card_patient_id
		WHERE  c.id_card_patient  = r_id_patient  GROUP BY t.name;
	 RETURN ;

	END;
$function$
;

--Отчет по медикаментозным лечениям
CREATE OR REPLACE FUNCTION public.report_stat_drug(date_from timestamp without time zone, date_to timestamp without time zone, OUT r_report_stat_drug refcursor)
 RETURNS refcursor
 LANGUAGE plpgsql
AS $function$
	begin
		OPEN r_report_stat_drug FOR
		SELECT dt.name , COUNT( u.drug_id ) as count_drug_treatment, COUNT(DISTINCT u.card_patient_id) AS count_patient FROM Treatment u 
        LEFT JOIN Drug d ON d.id_drug = u.drug_id 
        LEFT JOIN Drug_treatment dt ON dt.id_drug_treatment = d.drug_treatment_id 
        WHERE u.time_start_treatment BETWEEN  date_from AND date_to GROUP BY dt.name;
        RETURN;
	END;
$function$
;

-- Отчет по виду ребилитационного лечения за период времени
CREATE OR REPLACE FUNCTION public.report_stat(date_from timestamp without time zone, date_to timestamp without time zone, OUT r_report_cur refcursor)
 RETURNS refcursor
 LANGUAGE plpgsql
AS $function$
	begin
		OPEN r_report_cur FOR
		SELECT t.name AS name_solution,
			   COUNT( u.rehabilitation_solution_id ) AS count_solution,
			   COUNT(DISTINCT u.card_patient_id) AS count_patient
		FROM Treatment u
        LEFT JOIN Rehabilitation_solution t ON t.id_rehabilitation_solution = u.rehabilitation_solution_id
        WHERE  u.time_start_treatment BETWEEN date_from AND date_to GROUP BY t.name;
		RETURN;
	END;
$function$
;

-- Отчет по виду ребилитационного лечения за период времени без курсора
CREATE OR REPLACE FUNCTION public.report_stat_two(
    date_from timestamp without time zone, 
    date_to timestamp without time zone
)
RETURNS TABLE(
    name_solution VARCHAR, 
    count_solution BIGINT, 
    count_patient BIGINT
) 
LANGUAGE plpgsql
AS $function$
BEGIN
    RETURN QUERY 
    SELECT 
        t.name AS name_solution,
        COUNT(u.rehabilitation_solution_id) AS count_solution,
        COUNT(DISTINCT u.card_patient_id) AS count_patient
    FROM 
        Treatment u
    LEFT JOIN 
        Rehabilitation_solution t ON t.id_rehabilitation_solution = u.rehabilitation_solution_id
    WHERE 
        u.time_start_treatment BETWEEN date_from AND date_to 
    GROUP BY 
        t.name;
END;
$function$;

--Отчет по медикаментозным лечениям без курсора
CREATE OR REPLACE FUNCTION public.report_stat_drug_two( date_from timestamp without time zone, date_to timestamp without time zone )
RETURNS TABLE(
	name VARCHAR,
	count_drug_treatment BIGINT,
	count_patient BIGINT
)
LANGUAGE plpgsql
AS $function$
BEGIN
	RETURN QUERY
	SELECT
		dt.name,
		COUNT( u.drug_id ) as count_drug_treatment,
		COUNT(DISTINCT u.card_patient_id) AS count_patient
	FROM
		Treatment u
	LEFT JOIN 
		Drug d ON d.id_drug = u.drug_id 
	LEFT JOIN
		Drug_treatment dt ON dt.id_drug_treatment = d.drug_treatment_id 
	WHERE 
		u.time_start_treatment BETWEEN  date_from AND date_to 
	GROUP BY 
		dt.name;
	END;
$function$;

-- Получениие количества Реабилитационных лечений пациента без курсора
CREATE OR REPLACE FUNCTION public.record_patient_two( r_id_patient bigint )
RETURNS TABLE(
	name_solution VARCHAR,
	count_solution BIGINT
)
 LANGUAGE plpgsql
AS $function$
BEGIN
		RETURN QUERY
		SELECT 
			t.name AS name_solution,
			COUNT( u.rehabilitation_solution_id ) AS count_solution
		FROM 
			Treatment u
		LEFT JOIN 
			Rehabilitation_solution t ON t.id_rehabilitation_solution = u.rehabilitation_solution_id
		LEFT JOIN 
			Card_patient c ON  c.id_card_patient =u.card_patient_id
		WHERE 
			c.id_card_patient  = r_id_patient  
		GROUP
			BY t.name;
	END;
$function