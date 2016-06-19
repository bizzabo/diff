import cbt._
// cbt:https://github.com/cvogt/cbt.git#bc2231720d3620b5e0459fa12c467bf675fcfdf5
class Build(val context: Context) extends BuildBuild{
  override def dependencies = (
    super.dependencies // don't forget super.dependencies here
    ++
    Seq(
      GitDependency("https://github.com/xdotai/free-software-build.git","ed68f308e211dd02cb92f1c5f98d78979b472ca8")
    )
  )
}
