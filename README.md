# Type Resolver

[![Maven Central](https://img.shields.io/maven-central/v/nl.devoxist/TypeRegister.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22nl.devoxist%22%20AND%20a:%22TypeRegister%22)
[![GitHub license](https://img.shields.io/github/license/Devoxist/TypeRegister)](https://github.com/Devoxist/TypeRegister/blob/master/LICENSE)

### Getting Started

How to install this API? For all possible options to install this API [maven
central](https://search.maven.org/artifact/nl.devoxist/TypeRegister) and select the version.

#### Maven

Add this to your `pom.xml`.

```xml

<dependency>
    <groupId>nl.devoxist</groupId>
    <artifactId>TypeRegister</artifactId>
    <version>1.5.0</version>
</dependency>
```

### Usage

This API is able to auto construct classes. It is also possible to save a type and retrieve them later in the
application.

#### Registration

##### Supplier Types

How to register a type? This is mainly used to a type that can only be initialized once. For example the
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
TypeRegister.register(StaticType.class, new StaticType());
```

##### Unregister Types

How to unregister a type? The type of the registered `TypeProvider` is parameter of
the `TypeRegister#unregister(Class)`. The only condition of unregistering a type, that the type needs to be registered.

```java 
TypeRegister.register(StaticType.class, new StaticType());
TypeRegister.unregister(StaticType.class);
```

##### Custom Types

How to create a custom `TypeProvider`? In the example, the `CustomTypeProvider` is extended by `TypeProvider`. This is
how the custom types can be created.

```java 
public static class CustomTypeProvider<T, P extends T> extends TypeProvider<T, CustomType<P>> {

    public CustomTypeProvider(Class<T> typeCls, CustomType<P> provider) {
        super(typeCls, provider);
    }

    @Override
    public T getInitProvider() {
        // Put here your stuff
    }

    // Put here your stuff
}

public static class CustomType<P> {
    // Put here your stuff
}
```

How can this `CustomTypeProvider` be registered? Use the method `register(TypeProvider)` in `TypeRegister`.

```java
TypeRegister.register(new CustomTypeProvider(TestClass.class, new CustomType<TestClass>()));
```

##### Identifier Types

How to create an identifier type. This can be done through a `TypeProviderBuilder` or register the
`TypeKeyProvider`. In this example we use the `TypeProviderBuilder`. At first, we need to clarify which implementation
of the builder is used in the consumer, in this instance it will be the `IdentifierBuilder`. The `IdentifierBuilder` has
two generic parameters, a type and an identifier. Use the identifier parameter to define which identifier is expected.
After that use the methods provided in the builder to build to `TypeProvider`. Do not use the
`TypeProviderBuilder#buildProvider` is automatically done through the `Register`.

```java
register.register(Car.class, (IdentifiersBuilder<Car, Class<? extends Car>> settings) -> settings
                        .addIdentifier(CarOne.class, new CarOne())
                        .addIdentifier(CarTwo.class, new CarTwo())
                 );
```

How to retrieve this identifier type of the register? This can be done through the `Register#getInitProvider`. Use this
method which the settings and set the identifiers through the `setIdentifiers` method.

```java
 register.getInitProvider(Car.class, initProviderSettings -> initProviderSettings.setIdentifiers(CarOne.class));
```

#### InitProvider Settings

##### All Registries

Whether the search will go through all the provided registers in the construction of the `Register`. If this is false it
will only will search to the used `Register`.

##### Identifiers

The identifier objects that the `TypeKeyProvider` or other key providers need to use, to choose which implementation it 
will use. If not provided, and it needs an identifier, it will not find the implementation.

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
TypeRegister.register(ResolvableType.class, ResolvableType::new);
TypeRegister.register(StaticType.class, new StaticType());
```

After the type have been registered, it is possible to resolve the constructor.

```java 
Example example = ConstructorResolver.initClass(Example.class);
```

#### ConstructorResolver Options

##### No annotation

Use the `ConstructorResolver#initClass` with the parameter needAnnotation. This will remove the condition to use the
annotation `@ConstructorResolver` on the constructor. This will not sort the constructors on the priority and does not
keep track of the use of the annotation `@ConstructorResolver`. The default of this option is that it needs the
annotation `@ConstructorResolver`.

##### Identifiers

If there is a type with multiple implementations, like an interface with multiple implementations, it is possible to use
an identifier which will resolve the correct implementation of the type. For example there is an exporter, which exports
cars. There is a class that handles the car object, only it does not know which car it expects. Firstly we need to
create the objects.

```java
// Exporter
public record CarExporter(Car car) {
    // PUT YOUR STUFF HERE
}

// Car
public interface Car {
    // PUT YOUR STUFF HERE
}

// Implementation 1
public class CarOne implements Car {
    // PUT YOUR STUFF HERE
}

// Implementation 2
public class CarTwo implements Car {
    // PUT YOUR STUFF HERE
}
```

Secondly we need to register those objects. This can be done through a `TypeProviderBuilder` or register the
`TypeProvider`. In this example we use the `TypeProviderBuilder`. At first, we need to clarify which implementation
of the builder is used in the consumer, in this instance it will be the `IdentifierBuilder`. The `IdentifierBuilder` has
two generic parameters, a type and an identifier. Use the identifier parameter to define which identifier is expected.
After that use the methods provided in the builder to build to `TypeProvider`. Do not use the 
`TypeProviderBuilder#buildProvider` is automatically done through the `Register`.

```java
register.register(Car.class, (IdentifiersBuilder<Car, Class<? extends Car>> settings) -> settings
                        .addIdentifier(CarOne.class, new CarOne())
                        .addIdentifier(CarTwo.class, new CarTwo())
                 );
```

At last in use the ConstructorResolver with the settings consumer or use the builder like in the example below. Set the
identifiers through the `setIdentifiers` method, in the consumer or the builder. The builder needs
the `ConstructionSettingsBuilder#initClass` to initialize the class. The `ConstructorResolver#initClass` does the class
initialization automatically.

Method 1: 

```java
CarExporter carExporter = ConstructorResolver.constructClass(CarExporter.class)
        .setNeedAnnotation(false)
        .setIdentifiers(CarOneExporter.class)
        .setRegisters(register)
        .initClass();
```

Method 2:
```java
CarExporter carExporter2 = ConstructorResolver.initClass(CarExporter.class, constructionSettings -> {
    constructionSettings.setNeedAnnotation(false);
    constructionSettings.setIdentifiers(CarOneExporter.class);
    constructionSettings.setRegisters(register);
});
```

##### Others

For custom registers see the paragraph `Custom Registries`.

#### Custom Registries

How to create a custom registry? Construct a new `Register` object. Beside constructing the `Register`, it is also
possible to override the `Register` to make custom functions and override the current functions of the `Register`. 

```java
Register register = new Register();
```

All the same options which have been included in the `TypeRegister` are included in the Custom Register. Beside these
options is there also an option to "combine" registries. There are options included in some functions of the class which
allow the end-user to search and check between these "combined" registries. All the defaults for the functions are the
option which uses the non "combined" registries.

How to create combined registries? In the constructor of `Register` it is possible to add registries. These register
arguments of the constructor makes the combined registery.

```java
Register register = new Register();
Register secondRegister = new Register(register);
```

All the defaults of: `Register#hasProvider`, `Register#getProviderByType` and `Register#getInitProvider` takes the non
combined registery. Use the method with the option `allRegisters` and set it to `true` to use the combined registries
instead of the single registery.

In the construction phase of a `Register` there is also an option to add a priority. This priority option is used to
prioritize the combined registries. So if there are in two registries the same key the registry with the highest
priority will get retrieved.

```java
Register register = new Register(RegisteryPriority.HIGHEST);
Register secondRegister = new Register(register);
```

These custom registries can also be used in the `ConstructorResolver`. Use the method 
`ConstructorResolver#initClass(Class<?>,Register...)`or `ConstructionSettings/ConstructionSettingsBuilder#setRegisters` 
to choose which registries the resolver to use. If chosen register is `TypeRegister` use `TypeRegister#getRegister` and 
add this to the parameters. The default registry is the `TypeRegister`. This is automatically used if no registries are 
given.

##### Full Example

For the full examples go to the directory examples and find the examples.

### Contributors

+ Dev-Bjorn
