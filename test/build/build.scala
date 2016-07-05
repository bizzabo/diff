import cbt._
import java.net.URL
import java.io.File
import scala.collection.immutable.Seq

// cbt:https://github.com/cvogt/cbt.git#3c2310c9d092cc2589743a081a433103ab58e59b
class Build( context: Context ) extends BasicBuild( context ) with mixins.Test with mixins.ScalaParadise
