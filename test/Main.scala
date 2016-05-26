import ai.x.diff._
import scala.collection.immutable.SortedMap
object Main extends App {
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

  println(
    DiffShow.diff( before, after ).string
  )

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
