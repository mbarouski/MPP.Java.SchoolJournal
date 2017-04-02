CREATE USER IF NOT EXISTS 'accessor'@'localhost' 
IDENTIFIED BY 'Ak.06428R6dt';
GRANT SELECT,INSERT,UPDATE,DELETE
ON school_journal_db.*
TO 'accessor'@'localhost';