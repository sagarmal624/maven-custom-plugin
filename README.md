# Tutorial: How to build a Maven Plugin

This repository contains example code for an Apache Maven Plugin.  To see see how this was build, take a look at the corresponding blog post: [How to Build a Maven Plugin](https://developer.okta.com/blog/2019/09/23/tutorial-build-a-maven-plugin)

**Prerequisites:**

- [Java 8](https://adoptopenjdk.net/)
- [Maven 3](https://maven.apache.org/download.cgi)

## Getting Started

How to use Plugin:

Add below plugin in your project where you want to run this.
```
<plugin>
   <groupId>com.example</groupId>
   <artifactId>governance-maven-plugin</artifactId>
   <version>1.0-SNAPSHOT</version>
   <executions>
       <execution>
           <goals>
               <goal>validate</goal>
           </goals>
       </execution>
   </executions>
</plugin>

```


Command to trigger Plugin:
```
mvn governance:validate -Dpercentage=40 -DfilePath=<file system path of open api spec>
```
Percentage: it is an optional params to set minimum expected %.

filePath: in this you can pass your swagger openapi spec file path to validate it.






Plugin Internal Working Style:

   1) First it will fetch all drools rule from Database
   2)Write all rules in temp drl file which got from db.
   2)Extract Required Information from Swagger Specs to validate it
   3)fire all rules in swagger API’s
   4)Fetch result of rules (whenever rule passed then score will be increase by 1 and in case of error, it will fetch eros in messages list)
   5)calculate % based on actual scope and number of rule fired on that swagger api’s
   6)If actual % < Expected % then marking build FAIL otherwise it Will be PASS
