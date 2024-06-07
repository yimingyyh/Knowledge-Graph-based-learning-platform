package com.yyh.backendjava.dao;

import com.yyh.backendjava.entity.Course;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Repository
public interface CourseRepository extends Neo4jRepository<Course,Long> {
//    @Query("MATCH (n:course) RETURN n ")
//    List<Course> findAll();

    @Query("MATCH (n:course {name: $name})-[:going]->(relatedCourse) " +
            "RETURN n AS course, collect(relatedCourse) AS branches")
    Course findCourseWithBranchesByName(String name);

    //学生学习完毕后改变学习状态为finished
    @Query("match (n) where id(n)= $courseId set n.isFinished = true")
    Object updateStatusById(@RequestParam("courseId") Long courseId);

    @Query("match (n) where id(n)= $courseId set n.isFinished = false")
    Object updateStatusInitial(@RequestParam("courseId") Long courseId);
    @Query("match (n:course) where n.name = $courseName return n")
    Optional<Course> findCourseByName(@RequestParam("courseName") String courseName);

    @Query("MATCH (c:Course)-[r*0..1]->(other) WHERE ID(c) = $id RETURN c, collect(r), collect(other)")
    Course findCourseWithRelationshipsById(Long id);

    @Query("MERGE (n:course {name: $name1}) " +
            "ON CREATE SET n.isFinished = false " +
            "MERGE (m:course {name: $name2}) " +
            "ON CREATE SET m.isFinished = false " +
            "MERGE (n)-[:going]->(m)")
    void createNodesAndRelationship(String name1, String name2);
}
