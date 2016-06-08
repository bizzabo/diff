import ai.x.diff._
import shapeless._
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

  assert( DiffShow.diff( bar, bar ).isIdentical )
  assert( DiffShow.diff( barAsParent, barAsParent ).isIdentical )
  assert( DiffShow.diff( bar, barAsParent ).isIdentical )
  assert( DiffShow.diff( barAsParent, bar ).isIdentical )
  assert( DiffShow.diff( foo, foo ).isIdentical )
  assert( DiffShow.diff( foo, fooAsParent ).isIdentical )
  assert( DiffShow.diff( fooAsParent, foo ).isIdentical )
  assert( DiffShow.diff( fooAsParent, fooAsParent ).isIdentical )

  assert( !DiffShow.diff[Parent]( bar, foo ).isIdentical )
  assert( !DiffShow.diff( bar, fooAsParent ).isIdentical )
  assert( !DiffShow.diff( barAsParent, foo ).isIdentical )
  assert( !DiffShow.diff( barAsParent, fooAsParent ).isIdentical )

  assert( DiffShow.diff( Seq(bar), Seq(bar) ).isIdentical )
  // Seqs are compared as Sets
  assert( DiffShow.diff( Seq(bar), Seq(bar,bar) ).isIdentical )

  assert( !DiffShow.diff[Seq[Parent]]( Seq(foo,bar), Seq(bar) ).isIdentical )
  assert( !DiffShow.diff[Seq[Parent]]( Seq(foo), Seq(bar) ).isIdentical )

  def ignore[T] = new DiffShow[T] {
    def show( t: T ) = t.toString
    def diff( left: T, right: T ) = Identical( "<not compared>" )
    override def diffable( left: T, right: T ) = true
  }

  {
    implicit val ignoreId = ignore[Id]
    assert( DiffShow.diff( Id(1), Id(1) ).isIdentical )
    assert( DiffShow.diff( Id(1), Id(2) ).isIdentical )  

    val rowA = Row(Id(1),"foo")
    val rowB = Row(Id(2),"foo")
    assert( DiffShow.diff( rowA, rowB ).isIdentical )
    assert( DiffShow.diff( Seq(rowA), Seq(rowB) ).isIdentical )
  }

  assert( DiffShow.diff( Id(1), Id(1) ).isIdentical )
  assert( !DiffShow.diff( Id(1), Id(2) ).isIdentical )  

  val rowA = Row(Id(1),"foo")
  val rowB = Row(Id(2),"foo")
  assert( !DiffShow.diff( rowA, rowB ).isIdentical )
  assert( !DiffShow.diff( Seq(rowA), Seq(rowB) ).isIdentical )

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
        "adsf" :: "qwer" :: Nil,
        "axx" :: "yxcv" :: Nil
      ).string
    )
  }

  {
    implicit def HListDiffShow: DiffShow[HList] = new DiffShow[shapeless.HList] {
      def show(t: HList) = t.toString
      def diff(left: HList, right: HList) = if (left == right) Identical(left) else Different(left, right)
      override def diffable(left: HList, right: HList) =  left.runtimeLength == right.runtimeLength
    }

    println(
      DiffShow.diff(
        "abcd" :: 123 :: 'c' :: HNil,
        "abcd" :: 123 :: 'c' :: HNil
      )
    )

    println(
      DiffShow.diff(
        "abcd" :: 123 :: 'c' :: HNil,
        "abcd" :: 125 :: 'b' :: HNil
      )
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
