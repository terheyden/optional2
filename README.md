# optional2

_Optional with multiple values._

```java
// In Functional Programming, single values chain together to form a result.
// In this example: name -> UUID -> id string
String userIdStr = Optional.of("Cora")
    .map(name -> TestService.findUserId(name))
    .map(userId -> userId.toString())
    .get();

// However, oftentimes there are multiple results that you want to combine.
// In pure FP, you'd use memoization, or perhaps a monad or a tuple.
// In Java, it usually means abandoning FP for common simple cases.
// I suggest that we could try using Optional2:
//
// name -> UUID -> (name + UUID) = user

    TestUser goodUser = Optional2.of("Cora")                       // Optional2 stores: "Cora"
    .andOf(name -> TestService.findUserId(name))                   // Optional2 stores: "Cora" + UUID
    .reduce((name, userId) -> TestService.loginUser(userId, name)) // "Cora" + UUID = user obj
    .get();

// Notice that in the .reduce() call, the vars have strong names and strong types.
// You get a great blend of FP and Java.
//
// Let's do one more example:

    Optional2
    .ofNullable(NULL_STR)
    .ifEmpty(() -> LOG.warn("Name is null! Using backup source."))
    .or(() -> Optional.of("Cora"))
    .andOf(name -> null)
    .or(() -> Optional.of(UUID.randomUUID()))
    .ifPresent((name, userId) -> LOG.info("User: {} {}", name, userId));

// The Java Optional methods you're used to are all there, just extended to support
// a second value, and with a little more usability sprinkled in.
//
// .reduce() is the only newly-added method.
// It reduces down to a Java Optional, and any Optional2 can be converted
// back into a Java Optional at any time via .getOptional().
```
