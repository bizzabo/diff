import cbt._
// cbt:https://github.com/cvogt/cbt.git#b5d86995128a45c33117ecfb7365f0eb2b450a61
class Build(val context: cbt.Context) extends AdvancedScala{
  override def dependencies = super.dependencies ++
    Resolver( mavenCentral ).bind(
      "org.scala-lang" % "scala-compiler" % "2.11.8"
    )
}
