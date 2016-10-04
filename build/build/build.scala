import cbt._
// cbt:https://github.com/cvogt/cbt.git#95728f6041a32e081a1f4129b8483ef622179b45
class Build(val context: Context) extends BuildBuild{
  override def dependencies = (
    super.dependencies // don't forget super.dependencies here
    ++
    Seq(
      GitDependency("https://github.com/xdotai/free-software-build.git","289323da60f93869bc8b7e691ea22430ff5c84a8")
    )
  )
}
