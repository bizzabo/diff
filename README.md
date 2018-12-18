## diff

[![Join the chat at https://gitter.im/xdotai/diff](https://badges.gitter.im/xdotai/diff.svg)](https://gitter.im/xdotai/diff?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

A tool to visually compare Scala data structures with out of the box support for arbitrary case classes.

Be aware: Collections (List, Seq, etc.) are compared like sets, i.e. ignoring order.

### SBT Dependencies

#### Scala 2.11/2.12

```scala
"ai.x" %% "diff" % "2.0.1"
```

#### Scala 2.10

Stopped working in 1.2.0 due to what seems like a Scala compiler bug.
See https://github.com/xdotai/diff/issues/18

<!--
```scala
"ai.x" %% "diff" % "1.1.0"
compilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)
```
-->

### Usage

```scala
import ai.x.diff.DiffShow
import ai.x.diff.conversions._
println(  DiffShow.diff[Foo]( before, after ).string  )
```

Be aware that diff throws an Exception if a DiffShow type class instance for some field
can't be found rather than a type error.
If you use diff in a testing or debugging scenario that's usually not a problem.
The advantage is that the Exception can tell exactly which instance wasn't found. A type error
can only point to the outer most class (`Foo` in this case) even if it is actually one of it's deeply nested fields that is lacking an instance for it's type. Knowing only `Foo` would not be very helpful to pin point
which instance is missing.

#### Output

<img width="422" alt="example-output" src="https://cloud.githubusercontent.com/assets/274947/15580477/e46957e6-2336-11e6-919c-3eaf00f60cff.png">

#### Code

```scala
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
```

### Helpful tips

```scala
import ai.x.diff._
```

#### Custom comparison

Sometimes you may need to write your own type class instances. For example for non-case classes that don't compare well using ==.

```scala
import ai.x.diff._
import org.joda.time.LocalTime

implicit def localTimeDiffShow: DiffShow[LocalTime] = new DiffShow[LocalTime] {
  def show ( d: LocalTime ) = "LocalTime(" + d.toString + ")"
  def diff( l: LocalTime, r: LocalTime ) =
    if ( l isEqual r ) Identical( l ) else Different( l, r )
}
```

#### Ignore parts of data

Sometimes you may want to ignore some parts of your data during comparison.
You can do so by type, e.g. for non-deterministic parts like ObjectId, which always differ.

```scala
def ignore[T]: DiffShow[T] = new DiffShow[T] {
  def show( t: T ) = t.toString
  def diff( left: T, right: T ) = Identical( "<not compared>" )
  override def diffable( left: T, right: T ) = true
}
implicit def LocationIdShow: DiffShow[LocationId] = ignore[LocationId]
```

#### Influence comparison in collections

When comparing collections you can influence if two elements should be compared or treated as completely different.
Comparing elements shows their partial differences. Not comparing them shows them as added or removed.

```scala
implicit def PersonDiffShow[L <: HList](
  implicit
  labelled:  LabelledGeneric.Aux[Person, L],
  hlistShow: Lazy[DiffShowFields[L]]
): DiffShow[Person] = new CaseClassDiffShow[Person, L] {
  override def diffable( left: Person, right: Person ) = left._id === right._id
}
```
