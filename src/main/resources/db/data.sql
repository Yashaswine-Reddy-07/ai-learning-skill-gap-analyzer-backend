-- Seed Data for AI-Powered Personalized Learning & Skill Gap Analyzer

-- 1. Skills
INSERT IGNORE INTO skills (skill_id, skill_name, category) VALUES
(1, 'Python', 'Programming'),
(2, 'SQL', 'Database'),
(3, 'Statistics', 'Mathematics'),
(4, 'Power BI', 'Business Intelligence'),
(5, 'Java', 'Programming'),
(6, 'Spring Boot', 'Framework'),
(7, 'DSA', 'Computer Science'),
(8, 'JavaScript', 'Programming'),
(9, 'React', 'Frontend Framework'),
(10, 'CSS', 'Styling'),
(11, 'Node.js', 'Backend Runtime');

-- 2. Roles
INSERT IGNORE INTO roles (role_id, role_name) VALUES
(1, 'Data Analyst'),
(2, 'Java Developer'),
(3, 'Web Developer');

-- 3. Role Requirements (Skill ID and Required Level 1-5)
-- Data Analyst: Python (4), SQL (4), Statistics (4), Power BI (4)
INSERT IGNORE INTO role_requirements (requirement_id, role_id, skill_id, required_level) VALUES
(1, 1, 1, 4),
(2, 1, 2, 4),
(3, 1, 3, 4),
(4, 1, 4, 4),

-- Java Developer: Java (4), Spring Boot (4), SQL (3), DSA (4)
(5, 2, 5, 4),
(6, 2, 6, 4),
(7, 2, 2, 3),
(8, 2, 7, 4),

-- Web Developer: JavaScript (4), React (4), CSS (3), Node.js (3)
(9, 3, 8, 4),
(10, 3, 9, 4),
(11, 3, 10, 3),
(12, 3, 11, 3);

-- 4. Question Bank

-- Skill 1: Python
INSERT IGNORE INTO questions (question_id, skill_id, question_text, option_a, option_b, option_c, option_d, correct_option, difficulty, topic) VALUES
(1, 1, 'Which built-in Python data structure is mutable and maintains ordered sequence?', 'tuple', 'list', 'set', 'frozenset', 'B', 'EASY', 'Syntax & Data Types'),
(2, 1, 'In Python, what is the output of [x**2 for x in range(4) if x % 2 == 0]?', '[0, 4]', '[0, 1, 4, 9]', '[4, 16]', '[1, 9]', 'A', 'MEDIUM', 'List Comprehensions'),
(3, 1, 'Which keyword in Python is used to define an anonymous function?', 'def', 'func', 'lambda', 'inline', 'C', 'EASY', 'Functions'),
(4, 1, 'In pandas, which method is primarily used to handle missing values by filling them?', 'dropna()', 'fillna()', 'replace_null()', 'clean()', 'B', 'MEDIUM', 'Pandas Basics'),
(5, 1, 'What block in Python is guaranteed to execute regardless of whether an exception occurred?', 'except', 'catch', 'finally', 'ensure', 'C', 'MEDIUM', 'Exception Handling'),
(6, 1, 'What is the purpose of Python *args in function definitions?', 'Specifies keyword arguments', 'Allows passing arbitrary positional arguments', 'Enforces type checking', 'Passes pointer references', 'B', 'HARD', 'Functions');

-- Skill 2: SQL
INSERT IGNORE INTO questions (question_id, skill_id, question_text, option_a, option_b, option_c, option_d, correct_option, difficulty, topic) VALUES
(7, 2, 'Which JOIN returns all rows from the left table and matched rows from the right table?', 'INNER JOIN', 'LEFT JOIN', 'FULL OUTER JOIN', 'CROSS JOIN', 'B', 'EASY', 'JOIN'),
(8, 2, 'Which SQL clause is used to filter aggregated grouped results?', 'WHERE', 'HAVING', 'GROUP FILTER', 'ORDER BY', 'B', 'MEDIUM', 'GROUP BY'),
(9, 2, 'A query nested inside another query SELECT, FROM, or WHERE clause is called a:', 'Superquery', 'Subquery', 'Composite query', 'Batch query', 'B', 'MEDIUM', 'Subqueries'),
(10, 2, 'Which normal form eliminates transitive dependencies between non-key attributes?', '1NF', '2NF', '3NF', 'BCNF', 'C', 'HARD', 'Normalization'),
(11, 2, 'Which window function computes the rank of each row within a partition without gaps?', 'RANK()', 'DENSE_RANK()', 'ROW_NUMBER()', 'NTILE()', 'B', 'HARD', 'Window Functions'),
(12, 2, 'Which SQL keyword combines the results of two SELECT statements removing duplicates?', 'JOIN', 'UNION', 'UNION ALL', 'MERGE', 'B', 'EASY', 'JOIN');

-- Skill 3: Statistics
INSERT IGNORE INTO questions (question_id, skill_id, question_text, option_a, option_b, option_c, option_d, correct_option, difficulty, topic) VALUES
(13, 3, 'Which measure of central tendency is least sensitive to extreme outliers?', 'Mean', 'Median', 'Mode', 'Variance', 'B', 'EASY', 'Descriptive Statistics'),
(14, 3, 'In a standard normal distribution, approximately what percentage of data falls within 1 standard deviation of the mean?', '50%', '68%', '95%', '99.7%', 'B', 'MEDIUM', 'Normal Distribution'),
(15, 3, 'What type of error occurs when a true null hypothesis is incorrectly rejected?', 'Type I Error', 'Type II Error', 'Standard Error', 'Sampling Bias', 'A', 'HARD', 'Hypothesis Testing'),
(16, 3, 'A Pearson correlation coefficient of -0.85 indicates:', 'Weak positive correlation', 'Strong negative linear correlation', 'No correlation', 'Non-linear dependency', 'B', 'MEDIUM', 'Correlation'),
(17, 3, 'The p-value in hypothesis testing represents the probability of:', 'The alternative hypothesis being true', 'Observing results at least as extreme assuming null hypothesis is true', 'A calculation error', 'The sample size being adequate', 'B', 'HARD', 'Hypothesis Testing'),
(18, 3, 'What is the square root of the variance called?', 'Mean Absolute Deviation', 'Standard Deviation', 'Interquartile Range', 'Covariance', 'B', 'EASY', 'Descriptive Statistics');

-- Skill 4: Power BI
INSERT IGNORE INTO questions (question_id, skill_id, question_text, option_a, option_b, option_c, option_d, correct_option, difficulty, topic) VALUES
(19, 4, 'Which DAX function alters the filter context on a calculation?', 'FILTER()', 'CALCULATE()', 'SUMX()', 'RELATED()', 'B', 'HARD', 'DAX Measures'),
(20, 4, 'What kind of schema design is recommended as best practice for Power BI data models?', 'Snowflake Schema', 'Star Schema', 'Flat single table', 'Hierarchical Schema', 'B', 'MEDIUM', 'Data Modeling'),
(21, 4, 'Where in Power BI do you perform ETL transformations before loading data into the model?', 'DAX Editor', 'Power Query (Power Query Editor)', 'Report View', 'Model View', 'B', 'EASY', 'Power Query'),
(22, 4, 'Which cardinal relationship is generally discouraged because of ambiguity in filter direction?', 'One-to-Many', 'Many-to-One', 'Many-to-Many', 'One-to-One', 'C', 'MEDIUM', 'Relationships'),
(23, 4, 'Which visual is best suited for showing parts-to-whole categorical distribution?', 'Scatter plot', 'Decomposition tree', 'Pie or Donut chart', 'KPI card', 'C', 'EASY', 'Visualizations'),
(24, 4, 'Which DAX function navigates an existing relationship to fetch a column from another table?', 'LOOKUPVALUE()', 'RELATED()', 'SELECTCOLUMNS()', 'MERGE()', 'B', 'HARD', 'DAX Measures');

-- Skill 5: Java
INSERT IGNORE INTO questions (question_id, skill_id, question_text, option_a, option_b, option_c, option_d, correct_option, difficulty, topic) VALUES
(25, 5, 'Which Java concept allows a subclass to provide a specific implementation of a parent class method?', 'Method Overloading', 'Method Overriding', 'Encapsulation', 'Abstraction', 'B', 'EASY', 'OOP & Polymorphism'),
(26, 5, 'Which interface in Java Collections does not allow duplicate elements and is unordered?', 'List', 'Set', 'Queue', 'Map', 'B', 'EASY', 'Collections Framework'),
(27, 5, 'In Java 8+ Streams, which operation is terminal?', 'map()', 'filter()', 'collect()', 'sorted()', 'C', 'MEDIUM', 'Streams API'),
(28, 5, 'Which Java keyword prevents a variable reference from being reassigned after initialization?', 'static', 'volatile', 'final', 'transient', 'C', 'EASY', 'OOP & Polymorphism'),
(29, 5, 'Where does the JVM allocate objects instantiated with the "new" keyword?', 'Stack Memory', 'Heap Memory', 'Metaspace', 'Program Counter', 'B', 'MEDIUM', 'Memory & Garbage Collection'),
(30, 5, 'Which concurrent mechanism in Java ensures thread-safe atomic operations without explicit locking?', 'AtomicInteger', 'synchronized block', 'volatile keyword', 'ThreadLocal', 'A', 'HARD', 'Multithreading');

-- Skill 6: Spring Boot
INSERT IGNORE INTO questions (question_id, skill_id, question_text, option_a, option_b, option_c, option_d, correct_option, difficulty, topic) VALUES
(31, 6, 'Which Spring annotation is used on a class to mark it as a RESTful web controller returning JSON?', '@Controller', '@RestController', '@Service', '@Endpoint', 'B', 'EASY', 'Spring MVC'),
(32, 6, 'What annotation combines @Configuration, @EnableAutoConfiguration, and @ComponentScan?', '@SpringBootApplication', '@EnableJpaRepositories', '@WebMvcTest', '@ComponentService', 'A', 'EASY', 'Actuator & Config'),
(33, 6, 'What is the default bean scope in the Spring ApplicationContext?', 'prototype', 'singleton', 'request', 'session', 'B', 'MEDIUM', 'Bean Lifecycle'),
(34, 6, 'Which annotation is used to inject dependencies into Spring-managed beans?', '@Autowired', '@InjectProperty', '@ComponentLink', '@ResourceMap', 'A', 'EASY', 'Dependency Injection'),
(35, 6, 'In Spring Data JPA, which interface provides built-in pagination and sorting support?', 'CrudRepository', 'PagingAndSortingRepository', 'QueryByExampleExecutor', 'SimpleJpaDao', 'B', 'MEDIUM', 'Spring Data JPA'),
(36, 6, 'Which Spring Boot module provides production-ready features such as health checks and metrics?', 'Spring Security', 'Spring Actuator', 'Spring DevTools', 'Spring Cloud', 'B', 'HARD', 'Actuator & Config');

-- Skill 7: DSA (Data Structures & Algorithms)
INSERT IGNORE INTO questions (question_id, skill_id, question_text, option_a, option_b, option_c, option_d, correct_option, difficulty, topic) VALUES
(37, 7, 'What is the average time complexity of searching an element in a balanced Binary Search Tree?', 'O(1)', 'O(log n)', 'O(n)', 'O(n log n)', 'B', 'EASY', 'Binary Trees'),
(38, 7, 'Which traversal algorithm utilizes a Queue to explore adjacent nodes layer by layer?', 'Depth First Search (DFS)', 'Breadth First Search (BFS)', 'Inorder traversal', 'Topological sort', 'B', 'MEDIUM', 'Graph Traversal'),
(39, 7, 'Which technique divides a problem into overlapping subproblems and stores results in a memo table?', 'Greedy Algorithm', 'Dynamic Programming', 'Divide and Conquer', 'Backtracking', 'B', 'HARD', 'Dynamic Programming'),
(40, 7, 'What is the worst-case time complexity of QuickSort?', 'O(n log n)', 'O(n^2)', 'O(log n)', 'O(n)', 'B', 'MEDIUM', 'Arrays & Two Pointers'),
(41, 7, 'Which data structure is most efficient for detecting cycles in an undirected graph using disjoint sets?', 'Binary Heap', 'Union-Find (Disjoint Set Union)', 'Trie', 'Segment Tree', 'B', 'HARD', 'Graph Traversal'),
(42, 7, 'What is the time complexity of inserting a node at the head of a Singly Linked List?', 'O(1)', 'O(n)', 'O(log n)', 'O(n^2)', 'A', 'EASY', 'Linked Lists');

-- Skill 8: JavaScript
INSERT IGNORE INTO questions (question_id, skill_id, question_text, option_a, option_b, option_c, option_d, correct_option, difficulty, topic) VALUES
(43, 8, 'What is the combination of a function bundled together with references to its surrounding state called?', 'Callback', 'Closure', 'Generator', 'Promise', 'B', 'MEDIUM', 'Closures'),
(44, 8, 'Which array method creates a new array populated with the results of calling a function on every element?', 'forEach()', 'map()', 'filter()', 'reduce()', 'B', 'EASY', 'ES6 Modules'),
(45, 8, 'What executes microtasks before macrotasks in the JavaScript runtime environment?', 'Call Stack', 'Event Loop', 'Web Worker', 'Garbage Collector', 'B', 'HARD', 'Event Loop & Promises'),
(46, 8, 'Which keyword defines a block-scoped variable that cannot be reassigned?', 'var', 'let', 'const', 'static', 'C', 'EASY', 'ES6 Modules'),
(47, 8, 'What does the Promise.all() method do when one of the passed promises rejects?', 'Ignores rejection', 'Immediately rejects with that reason', 'Resolves null', 'Retries rejected promise', 'B', 'HARD', 'Event Loop & Promises'),
(48, 8, 'Which method is used to attach an event handler to a DOM element without overriding existing listeners?', 'onclick = fn', 'addEventListener()', 'attachEvent()', 'bindEvent()', 'B', 'EASY', 'DOM Manipulation');

-- Skill 9: React
INSERT IGNORE INTO questions (question_id, skill_id, question_text, option_a, option_b, option_c, option_d, correct_option, difficulty, topic) VALUES
(49, 9, 'Which hook is used to manage mutable state in a functional component?', 'useEffect', 'useState', 'useRef', 'useMemo', 'B', 'EASY', 'Hooks & State'),
(50, 9, 'When does the cleanup function in useEffect run?', 'Only when component mounts', 'Before component unmounts and before the next effect execution', 'Immediately after every render', 'Never', 'B', 'MEDIUM', 'useEffect Lifecycle'),
(51, 9, 'Which hook should be used to cache expensive calculation results between re-renders?', 'useCallback', 'useMemo', 'useState', 'useContext', 'B', 'MEDIUM', 'Component Optimization'),
(52, 9, 'What mechanism does React use to batch and compute differences between UI renders efficiently?', 'Shadow DOM', 'Virtual DOM diffing', 'Web Components', 'Direct DOM replacement', 'B', 'EASY', 'Virtual DOM'),
(53, 9, 'Which hook allows passing data deeply through the component tree without prop drilling?', 'useReducer', 'useContext', 'useImperativeHandle', 'useLayoutEffect', 'B', 'EASY', 'Props & Context'),
(54, 9, 'What happens if you update state inside the body of a React functional component without an effect or event handler?', 'Infinite re-render loop', 'Syntax error', 'State freeze', 'Silent failure', 'A', 'HARD', 'Hooks & State');

-- Skill 10: CSS
INSERT IGNORE INTO questions (question_id, skill_id, question_text, option_a, option_b, option_c, option_d, correct_option, difficulty, topic) VALUES
(55, 10, 'Which CSS layout module is primarily designed for one-dimensional layouts?', 'Grid', 'Flexbox', 'Float', 'Position absolute', 'B', 'EASY', 'Flexbox'),
(56, 10, 'In the CSS box model, which property creates space outside the border of an element?', 'padding', 'margin', 'outline', 'gap', 'B', 'EASY', 'Box Model'),
(57, 10, 'Which CSS function allows evaluating custom properties (CSS variables)?', 'calc()', 'var()', 'attr()', 'env()', 'B', 'EASY', 'CSS Variables'),
(58, 10, 'Which CSS Grid property specifies the columns structure of a grid container?', 'grid-template-columns', 'grid-column-gap', 'grid-auto-flow', 'grid-items-column', 'A', 'MEDIUM', 'CSS Grid'),
(59, 10, 'What CSS at-rule is used to apply different styles depending on screen width or orientation?', '@keyframes', '@media', '@supports', '@container', 'B', 'MEDIUM', 'Media Queries'),
(60, 10, 'What does box-sizing: border-box do?', 'Excludes padding from width', 'Includes padding and border in element total width/height', 'Removes margins', 'Forces 3D rendering', 'B', 'HARD', 'Box Model');

-- Skill 11: Node.js
INSERT IGNORE INTO questions (question_id, skill_id, question_text, option_a, option_b, option_c, option_d, correct_option, difficulty, topic) VALUES
(61, 11, 'Which core Node.js module is used for handling file system operations?', 'path', 'fs', 'http', 'os', 'B', 'EASY', 'Streams & Buffers'),
(62, 11, 'In Express.js, what is the third parameter typically passed to a middleware function?', 'req', 'res', 'next', 'err', 'C', 'EASY', 'Express Middleware'),
(63, 11, 'Which phase of the Node.js event loop executes timers scheduled by setTimeout()?', 'poll phase', 'timers phase', 'check phase', 'close callbacks', 'B', 'HARD', 'Event Loop'),
(64, 11, 'What is the primary architectural advantage of Node.js non-blocking I/O?', 'Multi-threaded CPU bound speed', 'Handles high concurrent connections efficiently on single main thread', 'Compiled machine code speed', 'Native relational database storage', 'B', 'MEDIUM', 'Async Patterns'),
(65, 11, 'Which stream type in Node.js allows both reading and writing data?', 'Readable', 'Writable', 'Duplex', 'Transform', 'C', 'MEDIUM', 'Streams & Buffers'),
(66, 11, 'In REST API design, which HTTP method should be idempotent and used to update a complete resource?', 'POST', 'PUT', 'PATCH', 'CONNECT', 'B', 'MEDIUM', 'REST Architecture');
