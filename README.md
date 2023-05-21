# TypeRegister

[![Maven Central](https://img.shields.io/maven-central/v/nl.devoxist/TypeRegister.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22nl.devoxist%22%20AND%20a:%22TypeRegister%22)
[![GitHub license](https://img.shields.io/github/license/Devoxist/TypeRegister)](https://github.com/Devoxist/TypeRegister/blob/master/LICENSE)

TypeRegister is a Java library that provides a flexible and centralized registry for type-provider links. It allows for
dynamic object instantiation based on types, making it easier to manage and access providers for various types within
your application.

## Features

TypeRegister library offers the following features:

- **Type-Provider Registration:** The TypeRegister library allows you to register type-provider links, associating
  specific types with their corresponding providers. This enables the system to retrieve the appropriate provider based
  on the type.
- **Customizable Providers:** You have the flexibility to create custom provider classes that implement specific
  functionality for different types. This allows you to define behavior tailored to each type, accommodating various
  implementation requirements.
- **Identifier Providers:**: The TypeRegister library introduces the concept of an identifier for type-provider links.
  An identifier allows you to associate a unique identifier with each type-provider link, providing a convenient way to
  retrieve providers based on their identifiers.
- **Multiple Registers:** The TypeRegister library supports the use of multiple registers, which act as independent
  containers for type-provider links. You can separate and organize type-provider links based on different contexts or
  modules within your application. Each register maintains its own set of type-provider links.
- **Merging Registers:** In situations where you need to combine type-provider links from different registers, the
  TypeRegister library enables you to merge registers together. This consolidation facilitates the management of
  providers across various contexts. When merging registers with different priorities, the resulting merged register
  retains the priority of the register with the highest priority among the merged registers.
- **Register Priority:** You can assign priorities to registers using the provided RegisterPriority enum. Register
  priority determines the order in which providers are retrieved for a given type. This empowers you to control the
  selection of providers when multiple providers are registered for the same type.
- **Flexible and Extensible:** The TypeRegister library provides a flexible and extensible framework for managing
  type-provider relationships. You can easily add, remove, or modify type-provider links based on your application's
  requirements, ensuring adaptability and scalability.
- **Central Registry:** The TypeRegister library acts as a central registry for managing type-provider links. It
  provides a centralized location for accessing and retrieving providers based on the registered types, promoting
  organization and accessibility.
- **Easy Integration:** The TypeRegister library seamlessly integrates into your application, offering straightforward
  incorporation of type-based functionality. It does not require complex configuration or setup, making it easy to
  integrate into new or existing projects.

The TypeRegister library empowers you to efficiently manage type-provider relationships, customize providers, control
provider selection, and enhance the flexibility and extensibility of your Java applications.

## Getting Started

### Installation

You can include the TypeRegister library in your Java project by adding the following Maven dependency:

```xml
<dependency>
    <groupId>com.example</groupId>
    <artifactId>typeregister</artifactId>
    <version>1.6.1</version>
</dependency>
```

## Usage

The TypeRegister library provides a convenient way to register and manage type-provider links. This section provides an
in-depth guide on how to use the library effectively.

## Registering Type-Provider Links

To register a type-provider link, you can use one of the following methods:

- **Method 1: Registering with Class and Provider Instances**

```java
TypeRegister.register(TypeProvider<T, P> typeProvider);
```

This method allows you to register a type-provider link by providing instances of the type and its corresponding
provider. It is suitable when you have pre-initialized instances of the type and provider.

- **Method 2: Registering with Class and Supplier Provider**

```java
TypeRegister.registerScoped(Class<T> typeCls, SerializableSupplier<P> provider);
```

This method is used to register a type-provider link by specifying the type class and a supplier provider. The supplier
provider is responsible for lazily providing instances of the type when needed.

- **Method 3: Registering with Class and Supplier Provider**

```java
TypeRegister.register(Class<T> typeCls, SerializableConsumer<X> builderConsumer);
```

This method allows you to register a type-provider link by specifying the type class and a builder consumer. The builder
consumer is responsible for constructing the provider using a `TypeProviderBuilder`. This method provides flexibility
when you need to configure the provider using a builder pattern.

The `register` method requires the (`IdentifiersBuilder<Car, Cars> settings`) parameter to configure the identifiers and
resolve settings for the specified class. By passing the `settings` object to the lambda expression, you can add the
scoped identifier using the `addIdentifier` method.

### Unregistering Type-Provider Links

If you no longer need a type-provider link, you can unregister it using the following methods:

- **Method 1: Unregistering by Type**

```java
TypeRegister.unregister(Class<T> typeCls);
```

This method unregisters the type-provider link based on the type class. It removes the association between the type and
its provider from the `TypeRegister`.

- **Method 2: Unregistering by TypeProvider**

```java
TypeRegister.unregister(TypeProvider<T, P> typeProvider);
```

This method unregisters the type-provider link based on the TypeProvider instance. It removes the association between
the type and its provider from the `TypeRegister`.

### Checking Type Registration

To check if a type has a registered provider, you can use the following method:

```java
boolean hasProvider = TypeRegister.hasProvider(Class<T> typeCls);
```

This method returns true if a provider is registered for the specified type, and false otherwise.

### Examples

Here are some additional usage examples to illustrate the features of the `TypeRegister`:

- **Example 1: Registering and Initializing Providers**

```java
// Define a type and its provider
TypeProvider<Car, CarProvider> carProvider = new TypeProvider<>(Car.class, CarProvider.class);
TypeProvider<Bus, BusProvider> busProvider = new TypeProvider<>(Bus.class, BusProvider.class);

// Register the type-provider links
TypeRegister.register(carProvider);
TypeRegister.register(busProvider);

// Initialize and retrieve the initialized instances
Car car = TypeRegister.getInitProvider(Car.class);
Bus bus = TypeRegister.getInitProvider(Bus.class);

// Use the initialized instances
car.drive();
bus.drive();
```

- **Example 2: Retrieving Providers by Type**

```java
// Register the type-provider links
TypeRegister.register(new TypeProvider<Rectangle, RectangleProvider>(Rectangle.class, RectangleProvider.class));
TypeRegister.register(new TypeProvider<Circle, CircleProvider>(Circle.class, CircleProvider.class));

// Retrieve providers by type
RectangleProvider rectangleProvider = TypeRegister.getProviderByType(Rectangle.class);
CircleProvider circleProvider = TypeRegister.getProviderByType(Circle.class);

// Use the providers
Rectangle rectangle = rectangleProvider.createRectangle(10, 20);
Circle circle = circleProvider.createCircle(5.0);

rectangle.draw();
circle.draw();
```

- **Example 3: Unregistering Type-Provider Links**

```java
// Register the type-provider links
TypeRegister.register(new TypeProvider<Animal, AnimalProvider>(Animal.class, AnimalProvider.class));
TypeRegister.register(new TypeProvider<Plant, PlantProvider>(Plant.class, PlantProvider.class));

// Unregister a type-provider link
TypeRegister.unregister(Animal.class);

// Retrieve providers by type
AnimalProvider animalProvider = TypeRegister.getProviderByType(Animal.class); // Returns null
PlantProvider plantProvider = TypeRegister.getProviderByType(Plant.class);

// Use the providers
Plant plant = plantProvider.createPlant("Rose");

plant.grow();
```

## Custom Type Providers

The `TypeProvider` class serves as a base class for creating custom type providers in the TypeRegister library. Custom
type providers allow you to define the behavior and instantiation logic for specific types. This section provides
guidance on creating and using custom type providers.

### Implementing a Custom Type Provider

To create a custom type provider, you need to extend the TypeProvider class and provide an implementation for the
`getInitProvider()` method. The TypeProvider class has the following structure:

```java
public abstract class TypeProvider<T, P> {
    private final Class<T> typeCls;
    private final P provider;

    public TypeProvider(@NotNull Class<T> typeCls, @NotNull P provider) {
        this.typeCls = typeCls;
        this.provider = provider;
    }

    public Class<T> getType() {
        return typeCls;
    }

    public P getProvider() {
        return provider;
    }

    public abstract T getInitProvider();
}
```

- **typeCls**: The class or interface representing the type of the provider.
- **provider**: The provided object of the type.

You need to implement the `getInitProvider()` method in your custom type provider class. This method is responsible for
returning the initialized object of the provider. You can customize the initialization logic based on your requirements.

Here's an example of a custom type provider implementation:

```java
public class CustomTypeProvider extends TypeProvider<CustomType, CustomTypeProviderImpl> {
    public CustomTypeProvider(CustomTypeProviderImpl provider) {
        super(CustomType.class, provider);
    }

    @Override
    public CustomType getInitProvider() {
        // Custom initialization logic here
        // ...
        return null;
    }

    // Custom provider implementation class
    public static class CustomTypeProviderImpl {
        // Provider implementation details
        // ...
    }
}
```

In the example above, we define a `CustomTypeProvider` class that extends the `TypeProvider` class. The constructor
takes an instance of the provider class (`CustomTypeProviderImpl`) and passes it to the superclass constructor using the
appropriate type class (`CustomType.class`).

The `getInitProvider()` method is implemented to provide the custom initialization logic for the custom type. You can
customize this method to create and return an initialized instance of the custom type.

### Registering Custom Type Providers

Once you have implemented your custom type provider, you can register it using the `TypeRegister.register()` method:

```java
TypeRegister.register(new CustomTypeProvider(new CustomTypeProviderImpl()));
```

In the example above, we create an instance of the `CustomTypeProvider` and pass an instance of the provider class (`
CustomTypeProviderImpl`) to the constructor. By registering the custom type provider, you make it available for
instantiation through the `TypeRegister`.

### Using Custom Type Providers

To retrieve an instance of a custom type from the TypeRegister, you can use the `TypeRegister.getInitProvider()` method:

```java
CustomType customType = TypeRegister.getInitProvider(CustomType.class);
```

In the example above, we use the `getInitProvider()` method to retrieve an initialized instance of the custom type (
`CustomType`). The `TypeRegister` internally uses the registered custom type provider to instantiate the custom type and
return the initialized instance.

You can now use the `customType` object in your application, leveraging the customized behavior and instantiation logic
provided by your custom type provider.

## Creating a Custom Register

The TypeRegister library allows you to create custom registers to manage type-provider links based on your specific
requirements. This section will guide you through the process of creating a custom register.

### Creating the Custom Register Class

To create a custom register, you need to create a new class that extends the `Register` class provided by
the `TypeRegister` library. This custom class will serve as your register implementation.

Here's an example of creating a custom register called `CustomRegister`:

```java
public class CustomRegister extends Register {
// Add custom methods and logic here
}
```

In the above example, we create a new class `CustomRegister` that extends the `Register` class. You can add any custom
methods and logic specific to your register implementation in this class.

### Registering Type-Provider Links

To register type-provider links in your custom register, you can utilize the methods inherited from the `Register`
class. These methods provide the functionality to register type-provider links based on different approaches, as
discussed earlier in the usage documentation.

You can override the `register()` method to provide your custom implementation for registering type-provider links.
Here's an example:

```java
public class CustomRegister extends Register {
    @Override
    public void register(TypeProvider<?, ?> typeProvider) {
        // Custom logic for registering type-provider links
        // ...
        super.register(typeProvider);
    }
}
```

In the above example, we override the `register()` method and add our custom logic for registering type-provider links.
You can modify the implementation based on your specific requirements.

### Unregistering Type-Provider Links

Similar to registering, you can override the `unregister()` method to provide custom logic for unregistering
type-provider links. Here's an example:

```java
public class CustomRegister extends Register {
    @Override
    public void unregister(TypeProvider<?, ?> typeProvider) {
        // Custom logic for unregistering type-provider links
        // ...
        super.unregister(typeProvider);
    }
}
```

In the above example, we override the `unregister()` method and add our custom logic for unregistering type-provider
links. You can customize the implementation as per your needs.

### Custom Register Usage

Once you have created your custom register, you can use it in your application by instantiating the `CustomRegister`
class and performing register-related operations using its methods. Here's an example:

```java
CustomRegister customRegister = new CustomRegister();

// Register type-provider links using custom register
customRegister.register(new TypeProvider<>(Car.class, CarProvider.class));

// Unregister type-provider links using custom register
customRegister.unregister(new TypeProvider<>(Bus.class, BusProvider.class));
```

In the above example, we instantiate the `CustomRegister` class and use it to register and unregister type-provider
links.

### Example: Custom Register with Additional Functionality

Here's an example showcasing a custom register with additional functionality:

```java
public class CustomRegister extends Register {
    private Map<String, List<String>> typeAliases;

    public CustomRegister() {
        typeAliases = new HashMap<>();
    }

    public void addTypeAlias(String type, String alias) {
        List<String> aliases = typeAliases.getOrDefault(type, new ArrayList<>());
        aliases.add(alias);
        typeAliases.put(type, aliases);
    }

    public List<String> getTypeAliases(String type) {
        return typeAliases.getOrDefault(type, Collections.emptyList());
    }
}
```

In the above example, the `CustomRegister` class extends the `Register` class and adds additional functionality to
manage type aliases. It provides methods to add type aliases (`addTypeAlias()`) and retrieve type
aliases (`getTypeAliases()`).

## Multiple Registers

In addition to creating a custom register, the TypeRegister library allows you to work with multiple registers
simultaneously. This can be useful in scenarios where you want to separate type-provider links based on different
contexts or modules within your application.

### Creating Multiple Registers

To create multiple registers, you can instantiate multiple instances of the `Register` or your custom register class.
Each register will maintain its own set of type-provider links.

Here's an example of creating multiple registers:

```java
Register register1 = new Register();
Register register2 = new Register();
```

In the above example, we create two separate instances of the `Register` class, namely `register1` and `register2`. Each
register will maintain its own set of type-provider links.

### Registering and Unregistering with Multiple Registers

When working with multiple registers, you can register and unregister type-provider links using the respective register
instances. Each register will handle its own set of type-provider links independently.

Here's an example illustrating the registration and unregistration with multiple registers:

```java
Register register1 = new Register();
Register register2 = new Register();

// Register type-provider links with register1
register1.register(new TypeProvider<>(Car.class, CarProvider.class));

// Register type-provider links with register2
register2.register(new TypeProvider<>(Bus.class, BusProvider.class));

// Unregister type-provider links from register1
register1.unregister(new TypeProvider<>(Car.class, CarProvider.class));

// Unregister type-provider links from register2
register2.unregister(new TypeProvider<>(Bus.class, BusProvider.class));
```

In the above example, we register type-provider links for the `Car` and `Bus` types using separate register instances.
We then unregister the respective type-provider links from their respective registers.

### Accessing Type Providers from Multiple Registers

When you have multiple registers, you can retrieve type providers from a specific register by using the respective
register instance.

Here's an example demonstrating how to access type providers from multiple registers:

```java
Register register1 = new Register();
Register register2 = new Register();

// Register type-provider links with register1
register1.register(new TypeProvider<>(Car.class, CarProvider.class));

// Register type-provider links with register2
register2.register(new TypeProvider<>(Bus.class, BusProvider.class));

// Retrieve the type provider for Car from register1
TypeProvider<Car, CarProvider> carProvider1 = register1.getProviderByType(Car.class);

// Retrieve the type provider for Bus from register2
TypeProvider<Bus, BusProvider> busProvider2 = register2.getProviderByType(Bus.class);
```

In the above example, we register type-provider links for the `Car` and `Bus` types using separate register instances.
We then retrieve the respective type providers from their respective registers.

### Working with Multiple Registers in Different Contexts

Using multiple registers can be beneficial when you want to manage type-provider links in different contexts or modules
within your application. Each register can represent a specific context or module, allowing you to isolate and organize
the type-provider links accordingly.

For example, you might have one register for the core functionality of your application and another register for a
plugin system. This separation can help maintain modularity and prevent conflicts between different sets of
type-provider links.

### Example: Multiple Registers with Custom Functionality

Here's an example showcasing the use of multiple registers with custom functionality:

```java
public class CustomRegister extends Register {
// Custom register implementation with additional functionality
// ...
}

CustomRegister register1 = new CustomRegister();
CustomRegister register2 = new CustomRegister();

// Register type-provider links with register1
register1.register(new TypeProvider<>(Car.class, CarProvider.class));

// Register type-provider links with register2
register2.register(new TypeProvider<>(Bus.class, BusProvider.class));

// Access custom functionality of register1
register1.addTypeAlias("Car", "Automobile");
List<String> aliases = register1.getTypeAliases("Car");

// Access custom functionality of register2
// ...
```` 

In the above example, we create two instances of the `CustomRegister` class, `register1` and `register2`. Each register
has its own set of type-provider links and can provide custom functionality specific to the `CustomRegister` class.

You can extend your custom register implementation with additional functionality as per your requirements and use
multiple registers in different parts of your application.

## Merging Registers

In addition to working with multiple registers independently, the TypeRegister library also provides the capability to
merge registers. Merging registers allows you to combine the type-provider links from multiple registers into a single
register, providing a unified view of all the registered types and providers.

### Merging Registers Using Constructor

To merge registers, you can use the constructor of the `Register` class that accepts multiple registers as arguments. By
passing the registers you want to merge, you create a new register instance that includes all the type-provider links
from the merged registers.

Here's an example of merging registers using the constructor:

```java
Register register1 = new Register();
Register register2 = new Register();

// Register type-provider links with register1
register1.register(new TypeProvider<>(Car.class, CarProvider.class));

// Register type-provider links with register2
register2.register(new TypeProvider<>(Bus.class, BusProvider.class));

// Merge register1 and register2 into a new register
Register mergedRegister = new Register(register1, register2);

```

In this example, we have three registers: `register1`, `register2`, and `register3`. Each register has its own set of
type-provider links. By using the `Register` constructor with the registers to merge, we create a new register called
`mergedRegister` that includes all the type-provider links from the merged registers.

### Accessing Type Providers from Merged Registers

Once you have merged registers, you can access the type providers through the merged register instance. The merged
register will provide a unified view of the registered providers, regardless of which original register they came from.

Here's an example demonstrating how to access type providers from merged registers:

```java
Register register1 = new Register();
Register register2 = new Register();
Register register3 = new Register();

// Register type-provider links with register1
register1.register(new TypeProvider<>(Car.class, CarProvider.class));

// Register type-provider links with register2
register2.register(new TypeProvider<>(Bus.class, BusProvider.class));

// Register type-provider links with register3
register3.register(new TypeProvider<>(Train.class, TrainProvider.class));

// Merge register1, register2, and register3 into a new register
Register mergedRegister = new Register(register1, register2, register3);

// Retrieve the type provider for Car from the merged register
TypeProvider<Car, CarProvider> carProvider = mergedRegister.getProviderByType(Car.class);

// Retrieve the type provider for Bus from the merged register
TypeProvider<Bus, BusProvider> busProvider = mergedRegister.getProviderByType(Bus.class);

// Retrieve the type provider for Train from the merged register
TypeProvider<Train, TrainProvider> trainProvider = mergedRegister.getProviderByType(Train.class);
```

In this example, we merge `register1`, `register2`, and `register3` into `mergedRegister`. We can then use the merged
register to retrieve the type providers for `Car`, `Bus`, and `Train` by calling the `getProviderByType()` method with
the respective type.

The merged register allows you to access all the type providers as if they were registered with a single register,
providing a consolidated view of the registered providers.

### Benefits of Register Merging

Merging registers can be beneficial in scenarios where you have separate modules, plugins, or contexts within your
application that each maintain their own set of type-provider links. By merging the registers, you create a centralized
view of the providers, making it easier to manage and retrieve them.

This approach promotes modularity, encapsulation, and flexibility, allowing different parts of your application to
define their own type-provider links independently. You can combine and merge registers as needed to build a
comprehensive and flexible dependency injection system.

### Note about Provider Selection with Merged Registers

When merging registers, it's important to note that if multiple providers are registered for the same type, the
selection of the provider will still be based on their priorities. The providers from the merged registers will retain
their original priorities, and the provider with the highest priority will be chosen when retrieving an instance.

By merging registers and setting priorities for them, you can control the selection and precedence of providers, even
when dealing with multiple registers and overlapping type-provider links.

## Register Priority and Provider Retrieval

The TypeRegister library allows you to assign a priority to registers, which determines the order in which the providers
are retrieved for a given type. This can be useful when you have multiple providers registered for the same type and you
want to control which provider is selected.

### Setting Register Priority

To set the priority of a register, you can use the `RegisterPriority` enum provided by the TypeRegister library when
creating a new register instance. The enum values include `LOW`, `MEDIUM`, and `HIGH`, representing different priority
levels.

Here's an example of creating registers with different priorities:

```java
Register lowPriorityRegister = new Register(RegisterPriority.LOW);
Register mediumPriorityRegister = new Register(RegisterPriority.MEDIUM);
Register highPriorityRegister = new Register(RegisterPriority.HIGH);
```

In this example, we create three register instances with different priorities: `lowPriorityRegister` with low priority,
`mediumPriorityRegister` with medium priority, and `highPriorityRegister` with high priority.

### Combined Priority with Merged Registers

When merging registers with different priorities, the resulting merged register will retain the priority of the register
with the highest priority among the merged registers.

Here's an example:

```java
Register highPriorityRegister = new Register(RegisterPriority.HIGH);
Register mediumPriorityRegister = new Register(RegisterPriority.MEDIUM);
Register lowPriorityRegister = new Register(RegisterPriority.LOW);

// Register type-provider links with the high priority register
highPriorityRegister.register(new TypeProvider<>(SomeType.class, HighPriorityProvider.class));

// Register type-provider links with the medium priority register
mediumPriorityRegister.register(new TypeProvider<>(SomeType.class, MediumPriorityProvider.class));

// Register type-provider links with the low priority register
lowPriorityRegister.register(new TypeProvider<>(SomeType.class, LowPriorityProvider.class));

// Merge registers with different priorities
Register mergedRegister = new Register(highPriorityRegister, mediumPriorityRegister, lowPriorityRegister);

// Retrieve the provider for SomeType from the merged register
TypeProvider<SomeType, ? extends SomeTypeProvider> provider = mergedRegister.getProviderByType(SomeType.class); // Returns HighPriorityProvider.class
```

In this example, we merge the registers `highPriorityRegister`, `mediumPriorityRegister`, and `lowPriorityRegister` into
a new register called `mergedRegister`. The resulting merged register will have the priority of the register with the
highest priority, which is `HIGH` in this case. Therefore, when retrieving the provider for `SomeType` from the merged
register, the provider from `HighPriorityProvider` class will be returned, as it has the highest priority.

By utilizing register priorities and merged registers, you can control the selection of providers and ensure that the
providers with the desired priority are retrieved for a given type.

## Autoconstructing Classes

Autoconstructing classes is a powerful feature provided by the ConstructorResolver class that allows you to
automatically create an instance of a specified class using its constructors. This feature eliminates the need for
manual instantiation and provides a convenient way to create objects with complex dependencies.

### ConstructionSettings

To control the autoconstruction process, `ConstructorResolver` utilizes the ConstructionSettings class. This class holds
the settings that determine how the constructor parameters are resolved and which constructors are eligible for
autoconstruction.

The `ConstructionSettings` class provides the following configuration options:

- **Registers**: The registers used to resolve the parameter types of the constructor. By default, the TypeRegister is
  used, but you can specify custom registers if needed.
- **Identifiers**: The identifier objects used by the IdentifierProvider or other key providers to choose the
  implementation. This allows for fine-grained control over which implementations are selected during autoconstruction.
- **Need Annotation**: Specifies whether the ConstructorResolving annotation is required on a constructor for it to be
  considered during the autoconstruction process.

By modifying these settings, you can customize the behavior of the autoconstruction process to fit your specific
requirements.

### Initializing Classes

The ConstructorResolver class provides a powerful and flexible way to initialize classes using the autoconstruction
feature. This feature automates the process of creating an instance of a specified class by leveraging the class's
constructors and resolving their parameter types. Let's explore how to use the initClass methods and see examples of
their usage.

### Using the `initClass` Methods

The ConstructorResolver class offers several initClass methods that handle the construction process and create instances
of the specified class. These methods provide flexibility in configuring the autoconstruction process based on your
specific needs. Let's explore these methods in more detail:

#### \`initClass(Class<T> constructionCls)`

This method constructs the class using the default TypeRegister to resolve the types of the constructor parameters. It
is suitable when you want to use the default register and do not need to modify the construction settings.

Example usage:

```java
Example example = ConstructorResolver.initClass(Example.class);
```

#### \`initClass(Class<T> constructionCls, Register... registers)`

If you require custom registers to resolve the parameter types of the constructor, you can use this method. It allows
you to specify one or more registers to be used during the autoconstruction process. This is useful when you have
specialized registers or want to override the default register.

Example usage:

```java
Register customRegister = new CustomRegister();
Example example = ConstructorResolver.initClass(Example.class, customRegister);
```

#### \`initClass(Class<T> constructionCls, boolean needAnnotation)`

Sometimes, you may want to enforce the presence of a specific annotation, such as `ConstructorResolving`, on a
constructor to consider it for autoconstruction. This method enables you to specify whether the annotation is required.
If set to true, only constructors with the specified annotation will be considered during the autoconstruction process.

Example usage:

```java
Example example = ConstructorResolver.initClass(Example.class, true);
```

#### \`initClass(Class<T> constructionCls, boolean needAnnotation, Register... registers)`

This method combines the flexibility of specifying the need for an annotation and providing custom registers. You can
control the presence of the annotation and use custom registers simultaneously.

Example usage:

```java
Register customRegister = new CustomRegister();
Example example = ConstructorResolver.initClass(Example.class, true, customRegister);
```

#### \`initClass(Class<T> constructionCls, Consumer<ConstructionSettings> constructionSettingsConsumer)`

When you require fine-grained control over the construction settings, this method allows you to apply custom
modifications using a `Consumer` function. You can access the `ConstructionSettings` object and adjust its properties
based on your specific requirements.

Example usage:

```java
Example example = ConstructorResolver.initClass(Example.class, settings -> {
    settings.setNeedAnnotation(true);
    settings.setRegisters(customRegister);
});
```

#### \`initClass(Class<T> constructionCls, ConstructionSettings constructionSettings)`

If you prefer a more direct approach to setting the construction settings, you can pass an instance of
`ConstructionSettings` directly to this method. This provides full control over the settings, including registers,
identifiers, and the need for an annotation.

Example usage:

```java
ConstructionSettings settings = new ConstructionSettings();
settings.setNeedAnnotation(true);
settings.setRegisters(customRegister);

Example example = ConstructorResolver.initClass(Example.class, settings);
```

By choosing the appropriate `initClass` method and adjusting the construction settings, you can tailor the
autoconstruction process to meet your specific needs and achieve the desired object initialization.

### Example Usage

Consider the following example that demonstrates the usage of the `initClass` methods:

```java
public class AutoConstructionExample {

    public static void main(String[] args) {
        // Register custom types
        TypeRegister.register(ResolvableType.class, ResolvableType::new);
        TypeRegister.register(StaticType.class, new StaticType());

        // Initialize Example class using default TypeRegister
        Example example1 = ConstructorResolver.initClass(Example.class);
        // Initialize Example class with custom registers
        Register customRegister = new CustomRegister();
        Example example2 = ConstructorResolver.initClass(Example.class, customRegister);
        // Initialize Example class with annotation requirement and custom registers
        Example example3 = ConstructorResolver.initClass(Example.class, true, customRegister);
        // Initialize Example class using construction settings consumer
        Example example4 = ConstructorResolver.initClass(Example.class, settings -> {
            settings.setNeedAnnotation(true);
            settings.setRegisters(customRegister);
        });
        // Initialize Example class using construction settings object
        ConstructionSettings settings = new ConstructionSettings();
        settings.setNeedAnnotation(true);
        settings.setRegisters(customRegister);
        Example example5 = ConstructorResolver.initClass(Example.class, settings);

        // Perform operations on initialized examples
        // ...

        // Unregister and re-register types
        TypeRegister.unregister(StaticType.class);
        TypeRegister.register(StaticType.class, StaticType::new);

        // Re-initialize Example class with updated types
        Example example6 = ConstructorResolver.initClass(Example.class);
        // ...
    }

    public static class Example {

        @ConstructorResolving
        public Example(ResolvableType type) {
            // Perform operations with ResolvableType instance
        }
    }

    public static class ResolvableType {
        // Class implementation
    }

    public static class StaticType {
        // Class implementation
    }
}
```

In this example, we start by registering custom types `ResolvableType` and `StaticType` in the `TypeRegister`. Then, we
demonstrate various ways to initialize the `Example` class using different `initClass` methods and construction
settings. Finally, we perform operations on the initialized examples and showcase how to unregister and re-register
types, followed by re-initializing the `Example` class with the updated types.

The example showcases the versatility of the `initClass` methods and how they can be used to automate the instantiation
process of classes with complex dependencies, while providing flexibility and customization options.

By leveraging the power of the `ConstructorResolver` class and the autoconstruction feature, you can simplify object
creation and initialization, making your code more concise and maintainable.

## Identifiers

Identifiers are objects that assist in identifying and selecting the appropriate implementation of a class during the
autoconstruction process. They provide a way to specify preferences or criteria for choosing the desired implementation
when multiple options are available.

### Registering Identifiers

To register identifiers in a `Register` object, you can use the `register` method. This method allows you to associate
identifiers with their corresponding implementations.

Here's an example of registering identifiers for the `Car` class:

```java
Register register = new Register();

register.register(
        Car.class,
        (IdentifiersBuilder<Car, Cars> settings) -> settings
                .addIdentifier(Cars.ONE, new CarOne())
                .addIdentifier(Cars.TWO, new CarTwo())
);

```

In this example, we create a `Register` object and register identifiers for the `Car` class. The `IdentifiersBuilder` is
used to associate identifiers (`Cars.ONE` and `Cars.TWO`) with their corresponding implementations (`CarOne`
and `CarTwo`).

The `register` method requires the (`IdentifiersBuilder<Car, Cars> settings`) parameter to configure the identifiers and
resolve settings for the specified class. By passing the `settings` object to the lambda expression, you can add the
scoped identifier using the `addIdentifier` method.

### Retrieving Providers from IdentifierProvider

To retrieve a provider based on an identifier, you can use the `getInitProvider` method of the `Register` object. This
method allows you to specify the identifier and retrieve the initialized provider.

Here's an example:

```java
Car car = register.getInitProvider(
        Car.class,
        (initProviderSettings) -> initProviderSettings.setIdentifiers(Cars.ONE)
);
```

In this example, the `getInitProvider` method is used to retrieve the initialized provider for the `Car` class. The
identifier `Cars.ONE` is set using the `setIdentifiers` method of the `InitProviderSettings` object. This retrieves an
instance of the desired implementation (`Car`) based on the provided identifier.

### Using Identifiers in Autoconstruction

To utilize identifiers during the autoconstruction process, you can set the desired identifiers in the
`ConstructionSettings` object. This helps the `IdentifierProvider` or other key providers to determine the appropriate
implementation based on the provided identifiers.

Here's an example of using identifiers in the autoconstruction process:

```java
ConstructionSettings settings = new ConstructionSettings();
settings.setIdentifiers(identifier1, identifier2);
```

In this example, we create a `ConstructionSettings` object and set the identifiers `identifier1` and `identifier2`.
These identifiers will be used during the autoconstruction process to select the appropriate implementation.

Please note that the specific implementation and usage of identifiers may vary depending on your codebase and
requirements. Identifiers can be any objects that assist in identifying a specific implementation, such as strings,
enums, or custom classes.

Remember to properly configure and handle identifiers within your codebase to ensure the appropriate implementation is
selected during the autoconstruction process.

### Autoconstructing with Identifiers

To perform autoconstruction with identifiers, you can use the `ConstructorResolver` class. After setting the identifiers
in the `ConstructionSettings` object, you can construct the desired class by calling the `initClass` method.

Here's an example:

```java
CarExporter carExporter = ConstructorResolver.constructClass(CarExporter.class)
        .setNeedAnnotation(false)
        .setIdentifiers(Cars.ONE)
        .setRegisters(register)
        .initClass();
```

In this example, we use the `ConstructorResolver` to construct an instance of the `CarExporter` class. We disable the
requirement for the `ConstructorResolving` annotation by setting the `needAnnotation` flag to false. We specify the
identifier `Cars.ONE` using the `setIdentifiers` method and provide the `Register` object using the `setRegisters`
method. Finally, we initialize the `CarExporter` instance using the `initClass` method, which automatically resolves the
appropriate implementation of `Car` based on the specified identifier.

### Scoped Identifiers

In addition to regular identifiers, the `IdentifiersBuilder` class also provides a way to add scoped identifiers using
the
`addScopedIdentifier` method. Scoped identifiers are associated with values that are supplied by
a `SerializableSupplier`. The supplier is invoked each time the identifier is used, allowing for dynamic retrieval of
values.

Here's an example of adding a scoped identifier using a SerializableSupplier:

```java
register.register(Car.class, (IdentifiersBuilder<Car, Cars> settings) -> {
    settings.addScopedIdentifier(Cars.TWO, CarTwo::new);
});
```

In this example, we register a scoped identifier `Cars.TWO` for the `Car` class. The `addScopedIdentifier` method is
used with a `SerializableSupplier` lambda expression to supply the value for the scoped identifier. The lambda
expression `CarTwo::new` creates a new instance of the `CarTwo` class each time the scoped identifier is used.

The `register` method requires the (`IdentifiersBuilder<Car, Cars> settings`) parameter to configure the identifiers and
resolve settings for the specified class. By passing the `settings` object to the lambda expression, you can add the
scoped identifier using the `addScopedIdentifier` method.

Scoped identifiers are useful when you want to retrieve different instances or values for the same identifier at
different points in time. This can be particularly handy when dealing with stateful or context-dependent objects.

Here's another example that demonstrates the usage of scoped identifiers with different supplier values:

```java
register.register(DatabaseConnection.class, (IdentifiersBuilder<DatabaseConnection, String> settings) -> {
    settings.addScopedIdentifier("local", LocalDatabaseConnection::new);
    settings.addScopedIdentifier("remote", RemoteDatabaseConnection::new);
});
```

In this example, we register scoped identifiers `"local"` and `"remote"` for the `DatabaseConnection` class. Depending
on the context or configuration, the appropriate connection instance will be retrieved when the corresponding scoped
identifier is used.

Scoped identifiers provide flexibility in retrieving values dynamically, allowing your code to adapt to changing
requirements or conditions.

Remember to appropriately handle the scoping and lifecycle of objects associated with scoped identifiers, ensuring that
they are created or retrieved when needed and properly managed to avoid resource leaks or inconsistencies.

### Example Usage

#### Example 1: Identifiers for Car Implementations

Suppose you have different implementations of the `Car` class, such as `SedanCar`, `SUVCar`, and `SportsCar`. You can
register identifiers to select the desired implementation based on specific criteria:

```java
register.register(Car.class, (IdentifiersBuilder<Car, String> settings) -> {
    settings
        .addIdentifier("sedan", new SedanCar())
        .addIdentifier("suv", new SUVCar())
        .addIdentifier("sports", new SportsCar());
});

```

In this example, identifiers "sedan", "suv", and "sports" are associated with the corresponding implementations. Later,
you can use these identifiers to retrieve the desired `Car` instance.

#### Example 2: Identifiers for Database Connections

Let's consider a scenario where you have two types of database connections: "local" and "remote". You can register
identifiers to handle different connection types:

```java
register.register(DatabaseConnection.class, (IdentifiersBuilder<DatabaseConnection, String> settings) -> {
    settings
        .addIdentifier("local", new LocalDatabaseConnection())
        .addIdentifier("remote", new RemoteDatabaseConnection());
});
```

In this example, identifiers "local" and "remote" are associated with the corresponding `DatabaseConnection`
implementations. This allows you to choose the appropriate connection based on the identifier.

#### Example 3: Scoped Identifiers for Logging Levels

Suppose you want to support different logging levels for your application, such as "debug", "info", and "error". You can
use scoped identifiers to dynamically retrieve the desired logging level:

```java
register.register(Logger.class, (IdentifiersBuilder<Logger, LogLevel> settings) -> {
    settings
        .addScopedIdentifier(LogLevel.DEBUG, () -> new DebugLogger())
        .addScopedIdentifier(LogLevel.INFO, () -> new InfoLogger())
        .addScopedIdentifier(LogLevel.ERROR, () -> new ErrorLogger());
});
```

In this example, scoped identifiers are associated with different logging levels. Each time the logging level is
required, the corresponding logger instance is dynamically retrieved based on the scoped identifier.

#### Example 4: Advanced Usages of Scoped Identifiers

In addition to normal identifiers, you can utilize scoped identifiers to dynamically retrieve implementations based on
specific conditions or context. Scoped identifiers are associated with suppliers that provide values for the identifiers
at runtime.

Here's an example that demonstrates the usage of scoped identifiers in combination with normal identifiers:

```java
register.register(DatabaseConnection.class, (IdentifiersBuilder<DatabaseConnection, String> settings) -> {
    settings
        .addIdentifier("local", new LocalDatabaseConnection())
        .addScopedIdentifier("userScoped", () -> {
            User user = getCurrentUser();
            return new UserScopedDatabaseConnection(user);
        });
});

```

In this example, we have registered two identifiers for the `DatabaseConnection` class: a normal identifier `"local"`
and a scoped identifier `"userScoped"`.

The `"local"` identifier is associated with a LocalDatabaseConnection instance, which represents a general connection
used for local databases.

The `"userScoped"` identifier is associated with a scoped supplier that dynamically retrieves a
`UserScopedDatabaseConnection` instance based on the current user. The `UserScopedDatabaseConnection` class represents a
connection that is scoped to the user and may have specific user-related configurations or permissions.

When using these identifiers, you can retrieve the appropriate connection instance based on the identifier:

```java
// Retrieving a local database connection
DatabaseConnection localConnection = register.getInitProvider(DatabaseConnection.class, (initProviderSettings) -> {
    initProviderSettings.setIdentifiers("local");
});

// Retrieving a user-scoped database connection
DatabaseConnection userScopedConnection = register.getInitProvider(DatabaseConnection.class, (initProviderSettings) -> {
    initProviderSettings.setIdentifiers("userScoped");
});
```

In this example, we retrieve a local database connection using the `"local"` identifier, which will give us the
`LocalDatabaseConnection` instance. We also retrieve a user-scoped database connection using the `"userScoped"`
identifier. This will dynamically invoke the scoped supplier, which retrieves the `UserScopedDatabaseConnection`
instance based on the current user.

Scoped identifiers provide a powerful mechanism to adapt the behavior of your code based on dynamic conditions or
contextual information. They allow for flexibility in selecting and configuring implementations at runtime.

Remember to handle the scoping and lifecycle of objects associated with scoped identifiers appropriately, ensuring they
are created or retrieved when needed and properly managed to avoid resource leaks or inconsistencies.

These examples demonstrate different aspects of using identifiers, including registering identifiers, retrieving
providers, using identifiers in autoconstruction, and autoconstructing with identifiers. You can adapt and customize
these examples based on your specific class and identifier requirements in your codebase.

##### Full Example

For the full examples go to the directory examples and find the examples.

### Contributors

+ Dev-Bjorn