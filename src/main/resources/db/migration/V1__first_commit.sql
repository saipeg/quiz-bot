CREATE TABLE "questions"
(
    id       SERIAL PRIMARY KEY,
    question TEXT default NULL,
    answer   TEXT default NULL
);

CREATE TABLE "kubernetes_questions"
(
    id       SERIAL PRIMARY KEY,
    question TEXT default NULL,
    answer   TEXT default NULL
);