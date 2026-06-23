-- Seed data for Module 12: books with varying stock levels
-- Some with stock=1 (last copy) for the pessimistic locking test

INSERT INTO book (isbn, title, author, description, stock) VALUES
('978-0-13-468599-1', 'Effective Java',                  'Joshua Bloch',         'Best practices for the Java platform.', 10),
('978-0-13-468599-2', 'Java Concurrency in Practice',    'Brian Goetz',          'Foundations of concurrent programming.', 5),
('978-0-13-468599-3', 'Hibernate in Action',             'Christian Bauer',      'A guide to the popular ORM.', 3),
('978-0-13-468599-4', 'Spring in Action',                'Craig Walls',          'Spring framework essentials.', 8),
('978-0-13-468599-5', 'The Pragmatic Programmer',        'Andy Hunt',            'From journeyman to master.', 1),
('978-0-13-468599-6', 'Domain-Driven Design',            'Eric Evans',           'Tackling complexity in software.', 1),
('978-0-13-468599-7', 'Refactoring',                     'Martin Fowler',        'Improving the design of existing code.', 4),
('978-0-13-468599-8', 'Clean Code',                      'Robert C. Martin',     'A handbook of agile software craftsmanship.', 6);