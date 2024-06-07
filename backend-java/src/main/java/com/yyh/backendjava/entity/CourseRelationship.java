package com.yyh.backendjava.entity;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.*;

/*
* 关系属性
* */
@Data
@RelationshipProperties
public class CourseRelationship {
    @Id
    @GeneratedValue
    private Long id;

    @TargetNode
    private Course course;

    public CourseRelationship(Course course) {
        this.course = course;
    }

}
