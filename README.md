# Type Resolver
[![Maven Central](https://img.shields.io/maven-central/v/nl.devoxist/TypeRegister.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22nl.devoxist%22%20AND%20a:%22TypeRegister%22)
[![GitHub license](https://img.shields.io/github/license/Devoxist/TypeRegister)](https://github.com/Devoxist/TypeRegister/blob/master/LICENSE)

### Getting Started

How to install this API? Currently, it is only possible to add this API with maven.

#### Maven

Add this to your `pom.xml`.

```xml

<dependency>
    <groupId>nl.devoxist</groupId>
    <artifactId>TypeRegister</artifactId>
    <version>1.0.0</version>
</dependency>
```

### Usage

This API is able to auto construct classes. It is also possible to save a type and retrieve them later in the
application.

#### Registration

How to register a type? This is mainly used to a type that can only be initialized once. For example the
class `ResolvableType`. The primitive types are not preferred to be registered. These types can change in different
classes. Therefor it is preferred of types which is everywhere the same.

```java
public class ResolvableType {
    // Put here your stuff
}
```

```java 
TypeRegister.register(ResolvableType.class,ResolvableType::new);
```

##### Static Types

How to register a static type? Instead of using a Supplier, use a static object. For example
the class `StaticType`. The same preferences still apply to this type of registered. So,
try to avoid using primitive types.

```java
public class StaticType {
    // Put here your stuff
}
```

```java 
TypeRegister.register(StaticType.class,new StaticType());
```

#### Auto construct classes

For example the class Example needs to be automatically constructed. On the constructor that needs to be automatically
resolved, needs to be the annotation `@ConstructorResolving`. As shown in the example below.

```java
import ConstructorResolving;

public class Example {

    @ConstructorResolving
    public Example(ResolvableType type, StaticType type) {
        // Put here your stuff
    }
}
```

```java
public class ResolvableType {
    // Put here your stuff
}
```

```java
public class StaticType {
    // Put here your stuff
}
```

First u need to add the class ResolvableType to the type register. This can be done by the code below.

```java 
TypeRegister.register(ResolvableType.class,ResolvableType::new);
        TypeRegister.register(StaticType.class,new StaticType());
```

After the type have been registered, it is possible to resolve the constructor.

```java 
Example example=ConstructorResolver.initClass(Example.class);
```

##### Full Example

```java
import TypeRegister;
import ConstructorResolver;
import ConstructorResolving;

public class Main {

    public static void main(String[] args)
            throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        TypeRegister.register(ResolvableType.class, ResolvableType::new);
        TypeRegister.register(StaticType.class, new StaticType());

        Example example = ConstructorResolver.initClass(Example.class);
    }


    public class Example {

        @ConstructorResolving
        public Example(ResolvableType type, StaticType staticType) {
            // Put here your stuff
        }
    }

    public class ResolvableType {
        // Put here your stuff
    }

    public class StaticType {
        // Put here your stuff
    }
}
```

### Contributors

+ Dev-Bjorn
