import ai.x.diff._
import ai.x.diff.conversions._
import scala.collection.immutable.SortedMap
sealed trait Parent
case class Bar( s: String, i: Int ) extends Parent
case class Foo( bar: Bar, b: List[Int], parent: Option[Parent] ) extends Parent

case class Id(int: Int)
case class Row(id: Id, value: String)
object Main extends App {
  val bar = Bar("test",1)
  val barAsParent: Parent = bar
  val foo = Foo( bar, Nil, None )
  val fooAsParent: Parent = foo

  def assertIdentical[T:DiffShow](left: T, right: T) = {
    val c = DiffShow.diff(left, right)
    assert(c.isIdentical, c.toString)
  }
  def assertNotIdentical[T:DiffShow](left: T, right: T) = {
    val c = DiffShow.diff(left, right)
    assert(!c.isIdentical, c.toString)
  }

  assertIdentical( bar, bar )
  assertIdentical( barAsParent, barAsParent )
  assertIdentical( bar, barAsParent )
  assertIdentical( barAsParent, bar )
  assertIdentical( foo, foo )
  assertIdentical( foo, fooAsParent )
  assertIdentical( fooAsParent, foo )
  assertIdentical( fooAsParent, fooAsParent )

  assertNotIdentical[Parent]( bar, foo )
  assertNotIdentical( bar, fooAsParent )
  assertNotIdentical( barAsParent, foo )
  assertNotIdentical( barAsParent, fooAsParent )

  assertIdentical( Seq(bar), Seq(bar) )
  // Seqs are compared as Sets
  assertIdentical( Seq(bar), Seq(bar,bar) )

  assertNotIdentical[Seq[Parent]]( Seq(foo,bar), Seq(bar) )
  assertNotIdentical[Seq[Parent]]( Seq(foo), Seq(bar) )

  def ignore[T] = new DiffShow[T] {
    def show( t: T ) = t.toString
    def diff( left: T, right: T ) = Identical( "<not compared>" )
    override def diffable( left: T, right: T ) = true
  }

  {
    implicit val ignoreId = ignore[Id]
    assertIdentical( Id(1), Id(1) )
    assertIdentical( Id(1), Id(2) )

    val rowA = Row(Id(1),"foo")
    val rowB = Row(Id(2),"foo")
    assertIdentical( rowA, rowB )
    assertIdentical( Seq(rowA), Seq(rowB) )
  }

  assertIdentical( Id(1), Id(1) )
  assertNotIdentical( Id(1), Id(2) )

  val rowA = Row(Id(1),"foo")
  val rowB = Row(Id(2),"foo")
  assertNotIdentical( rowA, rowB )
  assertNotIdentical( Seq(rowA), Seq(rowB) )

  /*
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

  println(
    DiffShow.diff( before, after ).string
  )
  */

  {
    implicit def StringDiffShow = new DiffShow[String] {
      def show( t: String ) = t
      def diff( left: String, right: String ) = if(left == right) Identical(left) else Different(left, right)
      override def diffable( left: String, right: String ) = left.lift(0) == right.lift(0)
    }

    println(
      DiffShow.diff(
        "x" :: Nil,
        "x" :: Nil
      ).string
    )
    println(
      DiffShow.diff(
        "x" :: Nil,
        Nil
      ).string
    )
    println(
      DiffShow.diff(
        Nil,
        "x" :: Nil
      ).string
    )
    println(
      DiffShow.diff(
        "adsf" :: "qwer" :: "x" :: Nil,
        "axx" :: "yxcv" :: "x" :: Nil
      ).string
    )
    println(
      DiffShow.diff(
        "adsf" :: "qwer" :: Nil,
        "axx" :: "yxcv" :: Nil
      ).string
    )
  }

  /*

  //import pprint.Config.Defaults._

  val actual = compare( before, after )
  val expected = Different(
    Tree( before ),
    Tree( after ),
    SortedMap(
      "bar" -> Different(
        Tree( Bar( "asdf", 5 ) ),
        Tree( Bar( "asdf", 66 ) ),
        SortedMap(
          "s" -> Identical( Leaf( "asdf" ) ),
          "i" -> Different(
            Leaf( 5 ), Leaf( 66 ), SortedMap()
          )
        )
      ),
      "b" -> Different(
        Leaf( 123 :: 1234 :: Nil ), Leaf( 1234 :: Nil ), SortedMap()
      )
    )
  )
  //pprint.pprintln( ( Generic[Bar] to Bar( "asdf", 5 ) ) delta ( Generic[Bar] to Bar( "asdf", 66 ) ) )
  assert( actual == expected, "expected\n:" + pprint.tokenize( expected ).mkString + "\n\nactual:\n" + pprint.tokenize( actual ).mkString )

  println( expected.show() )

  """
  Foo(
    b   = List( 123 +, 1234 ),
    bar = Bar(
      s = asdf,
      i = -5 +66
    )
  )
  """
  */
}
