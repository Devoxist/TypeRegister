# Type Resolver

### Getting Started

How to install this API? Currently, it is only possible to add this API with maven.

#### Maven

Add this in your `pom.xml`.

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

How to register a type. This is mainly used to a type that can only be initialized once. For example the
class `ResolvableType`. The primitive types are not preferred to be registered. These types can change in different
classes. Therefor it is preferred of types which is everywhere the same.

```java
public class ResolvableType {
    // Put here your stuff
}
```

```java 
TypeRegister.register(ResolvableType.class, ResolvableType::new);
```

#### Auto construct classes

For example the class Example needs to be automatically constructed. On the constructor that needs to be automatically
resolved, needs to be the annotation `@ConstructorResolving`. As shown in the example below.

```java
import ConstructorResolving;

public class Example {

    @ConstructorResolving
    public Example(ResolvableType type) {
        // Put here your stuff
    }
}
```

```java
public class ResolvableType {
    // Put here your stuff
}
```

First u need to add the class ResolvableType to the type register. This can be done by the code below.

```java 
TypeRegister.register(ResolvableType.class, ResolvableType::new);
```

After the type have been registered, it is possible to resolve the constructor.

```java 
Example example = ConstructorResolver.initClass(Example.class);
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
        Example example = ConstructorResolver.initClass(Example.class);
    }


    public static class Example {

        @ConstructorResolving
        public Example(ResolvableType type) {
            // Put here your stuff
        }
    }

    public static class ResolvableType {
        // Put here your stuff
    }
}
```

### Contributors

+ Dev-Bjorn
