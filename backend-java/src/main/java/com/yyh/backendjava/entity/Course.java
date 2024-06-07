package com.yyh.backendjava.entity;

import lombok.Data;

import org.springframework.data.neo4j.core.schema.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
@Data
@Node(labels = "course")
public class Course implements Serializable {
//    Neo4j数据库中的 Node 对象
    @Id
    @GeneratedValue
    private Long id;
    @Property
    private String name;
    @Property
    private boolean isFinished;
    @Relationship(type = "going",direction = Relationship.Direction.OUTGOING)
    private List<Course> branches  = new ArrayList<>();


    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", isFinished=" + isFinished +
                ", branches=" + branches +
                '}';
    }
}
