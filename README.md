# Motivation

By default, the maximum size of any request payload a Spring boot application can read is `262144` bytes.

This value can be changed using the Spring property `spring.codec.max-in-memory-size`.
However, increasing the value of this property could expose the production controllers to some risks like a DDOS attacks.

Spring Actuator endpoints or the port they listen are not usually exposed to the outside, so it could be safe increasing this value in a controlled network.

This project allows to configure a different max-in-memory-size only for the management server (aka Spring Actuator endpoints).

# Usage

The property has been named `management.codec.max-in-memory-size` and can be configured as the following example.

```yml
management:
  codec:
    max-in-memory-size: 1MB
```

It is also possible to have different memory size in the same configuration:

```yml
# Management Server Codec configurations
management:
  codec:
    max-in-memory-size: 1MB

# (original) Server Codec configurations
spring:
  codec:
    max-in-memory-size: 1KB
```