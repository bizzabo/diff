import cbt._
// cbt:https://github.com/cvogt/cbt.git#b5d86995128a45c33117ecfb7365f0eb2b450a61
class Build(val context: Context) extends BuildBuild{
  override def dependencies = (
    super.dependencies // don't forget super.dependencies here
    ++
    Seq(
      GitDependency("https://github.com/xdotai/free-software-build.git","58794ba2e3f0a478dbc4c85e2bd92d4802d08b48")
    )
  )
}
