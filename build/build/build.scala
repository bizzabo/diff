import cbt._
// cbt:https://github.com/cvogt/cbt.git#ec35ffeba2f5e8a2b1e61c3ba7da4276aa0c8211
class Build(val context: Context) extends BuildBuild{
  override def dependencies = (
    super.dependencies // don't forget super.dependencies here
    ++
    Seq(
      GitDependency("https://github.com/xdotai/free-software-build.git","3a2125d4ce25f097c417457ba191d2924a331a9d")
    )
  )
}
