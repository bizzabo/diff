import cbt._
import ai.x.build.{XdotaiFreeSoftwareBuild,team}
// cbt:https://github.com/cvogt/cbt.git#95728f6041a32e081a1f4129b8483ef622179b45
class Build(val context: cbt.Context) extends XdotaiFreeSoftwareBuild{
  def name = "diff"
  def defaultVersion = "1.2.1"
  def description = "diff tool for Scala data structures (nested case classes etc)"

  def inceptionYear = 2016
  def developers = Seq( team.cvogt )

  override def dependencies = super.dependencies ++
    Resolver( mavenCentral ).bind(
      "com.chuusai" %% "shapeless" % "2.3.1",
      "org.cvogt" %% "scala-extensions" % "0.5.1"
    )
}
