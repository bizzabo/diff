import cbt._
import ai.x.build.{XdotaiFreeSoftwareBuild,team}
import scala.collection.immutable.Seq
// cbt:https://github.com/cvogt/cbt.git#bc2231720d3620b5e0459fa12c467bf675fcfdf5
class Build(val context: cbt.Context) extends XdotaiFreeSoftwareBuild{
  def name = "diff"
  def defaultVersion = "1.1.0-RC1"
  def description = "diff tool for Scala data structures (nested case classes etc)"

  override def runClass: String = "ai.x.diff.Test"

  def inceptionYear = 2016
  def developers = Seq( team.cvogt )

  override def dependencies = super.dependencies ++
    Resolver( mavenCentral ).bind(
      "com.chuusai" %% "shapeless" % "2.3.1",
      "org.cvogt" %% "scala-extensions" % "0.5.0"
    )
}
