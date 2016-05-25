## Installation

```scala
libraryDependencies += "ai.x" %% "diff" % "1.0-SNAPSHOT"
resolvers += Resolver.sonatypeRepo("snapshots")
```

## Usage

```
import ai.x.diff._
sealed trait Parent
case class Bar( s: String, i: Int ) extends Parent
case class Foo( bar: Bar, b: List[Int], parent: Option[Parent] ) extends Parent

val before: Foo = Foo(
  Bar( "asdf", 5 ),
  List( 123, 1234 ),
  Some( Bar( "asdf", 5 ) )
)
val after: Foo = Foo(
  Bar( "asdf", 66 ),
  List( 1234 ),
  Some( Bar( "qwer", 5 ) )
)

println(DiffShow.diff( before, after ).string)
```
