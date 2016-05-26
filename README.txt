

// for non-case classes that don’t compare well with == you may end up writing your own type class instances:
implicit def localTimeDiffShow = {
  val show = ( d: org.joda.time.LocalTime ) => "LocalTime(" + d.toString + ")"
  DiffShow.create[org.joda.time.LocalTime]( show, ( l, r ) =>
    if ( l isEqual r ) Identical( show( l ) ) else Different( showChange( show( l ), show( r ) ) ) )
}

// Here is how you can ignore certain types that are different on every run (non-determinism, like id generation):
def ignore[T] = new DiffShow[T] {
  def show( t: T ) = t.toString
  def diff( left: T, right: T ) = Identical( "<not compared>" )
  override def diffable( left: T, right: T ) = true
}
implicit def LocationIdShow = ignore[LocationId]

// here is how you tell the diff tool when you want to compare two person entry in a list rather than treating them as completely rmoved / added:
implicit def PersonDiffShow[L <: HList](
  implicit
  labelled:  LabelledGeneric.Aux[Person, L],
  hlistShow: Lazy[DiffShowFields[L]]
) = new CaseClassDiffShow[Person, L] {
  override def diffable( left: Person, right: Person ) = left._id === right._id
}
