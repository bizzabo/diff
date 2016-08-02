package ai.x.diff
import scala.reflect.macros._

final class Abstract[T]
object Abstract{
  def checkMacro[T:c.WeakTypeTag](c: blackbox.Context): c.Expr[Abstract[T]] = {
    import c.universe._
    val T = c.weakTypeOf[T]
    if(
      !T.typeSymbol.isAbstract
    ) c.error(c.enclosingPosition,s"$T is not abstract")
    c.Expr[Abstract[T]](q"new _root_.ai.x.diff.Abstract[$T]")
  }
  /**
  fails compilation if T is not a singleton object class
  meaning this can be used as an implicit to check
  */
  implicit def check[T]: Abstract[T] = macro checkMacro[T]
}

final class Sealed[T]
object Sealed{
  def checkMacro[T:c.WeakTypeTag](c: blackbox.Context): c.Expr[Sealed[T]] = {
    import c.universe._
    val T = c.weakTypeOf[T]
    if(
      !T.typeSymbol.isClass || !T.typeSymbol.asClass.isSealed
    ) c.error(c.enclosingPosition,s"$T is not sealed")
    c.Expr[Sealed[T]](q"new _root_.ai.x.diff.Sealed[$T]")
  }
  /**
  fails compilation if T is not a singleton object class
  meaning this can be used as an implicit to check
  */
  implicit def check[T]: Sealed[T] = macro checkMacro[T]
}
