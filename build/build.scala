import cbt._
import ai.x.build.{XdotaiFreeSoftwareBuild,team}
import scala.collection.immutable.Seq
// cbt:https://github.com/cvogt/cbt.git#25e4d66e6abe5ef285849e710851ef84dc3ac700
class Build(context: cbt.Context) extends cbt.PublishBuild(context) with XdotaiFreeSoftwareBuild{
  def name = "diff"
  def defaultVersion = "1.0.2"
  def description = "diff tool for Scala data structures (nested case classes etc)"

  override def runClass: String = "ai.x.diff.Test"

  def inceptionYear = 2016
  def developers = Seq( team.cvogt )

  override def dependencies = super.dependencies ++
    Resolver( mavenCentral ).bind(
      "com.chuusai" %% "shapeless" % "2.3.1",
      "org.cvogt" %% "scala-extensions" % "0.4.1"
    )
}
