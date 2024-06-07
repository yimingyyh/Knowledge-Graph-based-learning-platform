package com.yyh.backendjava.dao;

import com.yyh.backendjava.entity.CourseRelationship;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface CourseRelationshipRepository extends Neo4jRepository<CourseRelationship,Long> {

}
