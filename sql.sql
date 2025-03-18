CREATE TABLE company
(
    id integer NOT NULL,
    name character varying,
    CONSTRAINT company_pkey PRIMARY KEY (id)
);

CREATE TABLE person
(
    id integer NOT NULL,
    name character varying,
    company_id integer references company(id),
    CONSTRAINT person_pkey PRIMARY KEY (id)
);

INSERT INTO company (id, name) VALUES
(1, 'A'),
(2, 'B'),
(3, 'C'),
(4, 'D'),
(5, 'E');

INSERT INTO person (id, name, company_id) VALUES
(1, 'Alice', 1),
(2, 'Bob', 2),
(3, 'Charlie', 3),
(4, 'David', 5),
(5, 'Eve', NULL),
(6, 'Frank', 1),
(7, 'Grace', 2),
(8, 'Heidi', 3),
(9, 'Ivan', NULL);

SELECT p.name AS person_name,
       c.name AS company_name
FROM person p
         LEFT JOIN company c ON p.company_id = c.id;

SELECT p.name AS person_name
FROM person p
WHERE p.company_id <> 5;

SELECT c.name AS company_name, COUNT(p.id) AS person_count
FROM company c
         LEFT JOIN person p ON c.id = p.company_id
GROUP BY c.id, c.name
HAVING COUNT(p.id) = (SELECT MAX(person_count)
                      FROM (SELECT COUNT(*) AS person_count
                            FROM person
                            GROUP BY company_id) AS counts)
ORDER BY c.name;
