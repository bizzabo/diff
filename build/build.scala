import cbt._
import ai.x.build.{XdotaiFreeSoftwareBuild,team}
// cbt:https://github.com/cvogt/cbt.git#ec35ffeba2f5e8a2b1e61c3ba7da4276aa0c8211
class Build(val context: cbt.Context) extends XdotaiFreeSoftwareBuild{
  def name = "diff"
  def defaultVersion = "1.1.0"
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
