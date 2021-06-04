import ai.x.diff._
import ai.x.diff.conversions._
import scala.collection.immutable.SortedMap
import java.util.UUID
import org.scalatest.FunSuite

object Foo1 {
  object Foo2 {
    case object Foo3
  }
}

sealed trait Parent
case class Bar( s: String, i: Int ) extends Parent
case class Foo( bar: Bar, b: List[Int], parent: Option[Parent] ) extends Parent

case class Id( int: Int )
case class Row( id: Id, value: String )
class AllTests extends FunSuite {
  test( "all tests" ) {
    val bar = Bar( "test", 1 )
    val barAsParent: Parent = bar
    val barString = """Bar( i = 1, s = "test" )"""
    val foo = Foo( bar, Nil, None )
    val fooAsParent: Parent = foo
    val fooString = """Foo( b = Set(), bar = Bar( i = 1, s = "test" ), parent = None )"""

    def assertIdentical[T: DiffShow]( left: T, right: T, expectedOutput: String = null ) = {
      val c = DiffShow.diff( left, right )
      assert( c.isIdentical, c.toString )
      Option( expectedOutput ).foreach(
        e => assert( c.string == e, "Expected: " ++ e ++ " Found: " ++ c.string )
      )
      assert( c.string == DiffShow.show( left ), "Expected: " ++ DiffShow.show( left ) ++ " Found: " ++ c.string )
    }
    def assertNotIdentical[T: DiffShow]( left: T, right: T, expectedOutput: String = null ) = {
      val c = DiffShow.diff( left, right )
      assert( !c.isIdentical, c.toString )
      Option( expectedOutput ).foreach {
        e => assert( c.string == e, "Expected: " ++ e ++ " Found: " ++ c.string )
      }
    }

    assertIdentical( bar, bar, barString )
    assertIdentical( barAsParent, barAsParent, barString )
    assertIdentical( bar, barAsParent, barString )
    assertIdentical( barAsParent, bar, barString )
    assertIdentical( foo, foo, fooString )
    assertIdentical( foo, fooAsParent, fooString )
    assertIdentical( fooAsParent, foo, fooString )
    assertIdentical( fooAsParent, fooAsParent, fooString )

    assertNotIdentical[Parent]( bar, foo, showChange( bar, foo ) )
    assertNotIdentical( bar, fooAsParent, showChange( bar, foo ) )
    assertNotIdentical( barAsParent, foo, showChange( bar, foo ) )
    assertNotIdentical( barAsParent, fooAsParent, showChange( bar, foo ) )

    assertIdentical( Seq( bar ), Seq( bar ) )
    // Seqs are compared as Sets
    assertIdentical( Seq( bar ), Seq( bar, bar ) )

    assertNotIdentical[Seq[Parent]]( Seq( foo, bar ), Seq( bar ) )
    assertNotIdentical[Seq[Parent]]( Seq( foo ), Seq( bar ) )

    {
      val uuid1 = UUID.randomUUID()
      val uuidAs1 = UUID.fromString( uuid1.toString )
      val uuid2 = UUID.randomUUID()
      assertIdentical( uuid1, uuidAs1 )
      assertNotIdentical( uuid1, uuid2 )
    }

    {
      val leftEitherFoo = Left( "foo" )
      val leftEitherBar = Left( "bar" )
      val rightEitherFoo = Right( "foo" )
      val rightEitherBar = Right( "bar" )

      val eitherDiff1 = DiffShow.diff( leftEitherFoo, rightEitherFoo )
      assert( !eitherDiff1.isIdentical )
      assert( eitherDiff1.string == showChangeRaw( """Left( "foo" )""", """Right( "foo" )""" ), eitherDiff1.string )

      val eitherDiff2 = DiffShow.diff( leftEitherFoo, leftEitherBar )
      assert( !eitherDiff2.isIdentical )
      assert( eitherDiff2.string == s"""Left( ${showChangeRaw( "\"foo\"", "\"bar\"" )} )""", eitherDiff2.string )

      val eitherDiff3 = DiffShow.diff( rightEitherFoo, rightEitherBar )
      assert( !eitherDiff3.isIdentical )
      assert( eitherDiff3.string == s"""Right( ${showChangeRaw( "\"foo\"", "\"bar\"" )} )""" )

      assert( DiffShow.diff( leftEitherFoo, leftEitherFoo ).isIdentical )
      assert( DiffShow.diff( rightEitherFoo, rightEitherFoo ).isIdentical )
    }

    def ignore[T] = new DiffShow[T] {
      def show( t: T ) = t.toString
      def diff( left: T, right: T ) = Identical( "<not compared>" )
      override def diffable( left: T, right: T ) = true
    }

    {
      implicit val ignoreId = ignore[Id]
      assert( DiffShow.diff( Id( 1 ), Id( 1 ) ).isIdentical )
      assert( DiffShow.diff( Id( 1 ), Id( 2 ) ).isIdentical )

      val rowA = Row( Id( 1 ), "foo" )
      val rowB = Row( Id( 2 ), "foo" )
      assert( DiffShow.diff( rowA, rowB ).isIdentical )
      assert( DiffShow.diff( Seq( rowA ), Seq( rowB ) ).isIdentical )
    }

    assertIdentical( Id( 1 ), Id( 1 ) )
    assertNotIdentical( Id( 1 ), Id( 2 ) )

    val rowA = Row( Id( 1 ), "foo" )
    val rowB = Row( Id( 2 ), "foo" )
    assertNotIdentical( rowA, rowB )
    assertNotIdentical( Seq( rowA ), Seq( rowB ) )

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
      implicit def StringDiffShow: DiffShow[String] = new DiffShow[String] {
        def show( t: String ) = "\"" ++ t ++ "\""
        def diff( left: String, right: String ) = if ( left == right ) Identical( left ) else Different( left, right )( this, this )
        override def diffable( left: String, right: String ) = left.lift( 0 ) == right.lift( 0 )
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

    {
      // testing hack for https://issues.scala-lang.org/browse/SI-2034
      assertIdentical( Foo1.Foo2.Foo3, Foo1.Foo2.Foo3 )
    }

    {
      // testing issue when comparing Maps (https://github.com/xdotai/diff/issues/24)
      println(
        DiffShow[Map[String, String]](DiffShow.mapDiffShow).diff(
          Map("a" -> "b"),
          Map("a" -> "b", "c" -> "d")
        ).string
      )

      println(
        DiffShow[Map[String, String]](DiffShow.mapDiffShow).diff(
          Map("a" -> "b", "c" -> "d"),
          Map("a" -> "b")
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
}
