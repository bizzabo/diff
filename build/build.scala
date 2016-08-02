import cbt._
import ai.x.build.{XdotaiFreeSoftwareBuild,team}
// cbt:https://github.com/cvogt/cbt.git#b5d86995128a45c33117ecfb7365f0eb2b450a61
class Build(val context: cbt.Context) extends XdotaiFreeSoftwareBuild{
  def name = "diff"
  def defaultVersion = "1.2.0"
  def description = "diff tool for Scala data structures (nested case classes etc)"

  override def runClass: String = "ai.x.diff.Test"

  def inceptionYear = 2016
  def developers = Seq( team.cvogt )

  override def dependencies = super.dependencies ++
    Resolver( mavenCentral ).bind(
      "com.chuusai" %% "shapeless" % "2.3.1",
      "org.cvogt" %% "scala-extensions" % "0.5.1"
    ) ++ Seq(
      DirectoryDependency(projectDirectory ++ "/macros")
    )
}
