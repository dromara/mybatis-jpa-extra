CREATE TABLE IF NOT EXISTS `students` (
  `id` varchar(100) NOT NULL,
  `stdno` varchar(100) DEFAULT NULL,
  `stdname` varchar(100) DEFAULT NULL,
  `stdgender` varchar(100) DEFAULT NULL,
  `stdage` int DEFAULT NULL,
  `stdmajor` varchar(100) DEFAULT NULL,
  `stdclass` varchar(100) DEFAULT NULL,
  `password` varchar(250) DEFAULT NULL,
  `images` blob,
  `status` int DEFAULT NULL,
  `modifydate` datetime DEFAULT NULL,
  `deleted` varchar(1) DEFAULT NULL
);




CREATE TABLE  IF NOT EXISTS  `scores` (
  `id` varchar(45) NOT NULL,
  `std_no` varchar(45) DEFAULT NULL,
  `std_name` varchar(45) DEFAULT NULL,
  `course_id` varchar(45) DEFAULT NULL,
  `course_name` varchar(45) DEFAULT NULL,
  `grade` int DEFAULT NULL,
  `modify_by` varchar(45) DEFAULT NULL,
  `modify_date` timestamp NULL DEFAULT NULL,
  `deleted` varchar(2) DEFAULT NULL,
  PRIMARY KEY (`id`)
);
