# An exercise in implementing authentication for a Dropwizard REST API

><b>DISCLAIMER</b>: I'm not a security expert, and this code is not reviewed by a security expert. For production, you should use a hardened library like Spring-Security.

[Full blog post](http://leanjava.co/2018/01/29/exploring-rest-api-authentication-mechanisms/) explaining rationale.

How to start the dropwizard-api-auth-example application
---

1. Run `mvn clean install` to build your application
1. Start application with `java -jar target/dropwizard-api-auth-example-0.1-SNAPSHOT.jar server config.yml`
1. To check that your application is running enter url `http://localhost:8080`
