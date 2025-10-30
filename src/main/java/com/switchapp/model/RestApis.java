package com.switchapp.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Entity
@Table(name = "rest_apis")
@Getter
@Setter
@ToString
public class RestApis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private Long id;
    @Column
    private String urlPatternsPath;
    @Column
    private String httpMethodsName;
    @Column
    private String apiConsumes;
    @Column
    private String javaClassName;
    @Column
    private String returnType;
    @Column
    private String methodParamsSign;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_dt", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Date createdDt;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_dt", length = 19)
    private Date updatedDt;

    public static void main(String[] test) {

        Pattern pattern = Pattern.compile("<(\\w+)( +.+)*>((.*))</\\1>");
        System.out.println(pattern.matcher("<asd> TEST</asd>").find());
        System.out.println(pattern.matcher("<asd TEST</asd>").find());
        System.out.println(pattern.matcher("<asd attr='3'> TEST</asd>").find());
        System.out.println(pattern.matcher("<asd> <x>TEST<x>asd>").find());
        System.out.println("-------");
        Matcher matcher = pattern.matcher("<as x> TEST</as>");
        if (matcher.find()) {
            for (int i = 0; i <= matcher.groupCount(); i++) {
                System.out.println(i + ":" + matcher.group(i));
            }
        }

        String str = "test.test.test";
        System.out.println(StringUtils.countMatches(str, "."));
        System.out.println(org.apache.commons.lang3.StringUtils.countMatches(str, "."));

        str = "test.testtest";
        System.out.println(StringUtils.countMatches(str, "."));
        System.out.println(org.apache.commons.lang3.StringUtils.countMatches(str, "."));

        str = "test.testtest";
        System.out.println(StringUtils.countMatches(str, "."));
        System.out.println("...." + org.apache.commons.lang3.StringUtils.countMatches(str, "test"));

        long java8 = str.chars().filter(ch -> ch == '.').count();
        System.out.println("java8 = " + java8);

        System.out.println("hasMultipleDots : " + RestApis.hasMultipleDots(str, '.'));

    }

    public static boolean hasMultipleDots(String fileName, char input) {

        return StringUtils.countMatches(fileName, ".") > 1;
    }

    @PrePersist
    protected void onCreate() {
        createdDt = new Date();
        updatedDt = createdDt;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedDt = new Date();
    }

}
