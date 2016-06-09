import cbt._
import java.net.URL
import java.io.File
import scala.collection.immutable.Seq

// cbt:https://github.com/cvogt/cbt.git#f56a035e5df98bc4e2bf90a1be5a7317be7ef667
class Build( context: Context ) extends BasicBuild( context ) with mixins.Test with mixins.ScalaParadise
