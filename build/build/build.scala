import cbt._
class Build( context: Context ) extends BuildBuild( context ){
  override def dependencies = (
    super.dependencies // don't forget super.dependencies here
    ++
    Seq(
      GitDependency("https://github.com/xdotai/free-software-build.git","389913f3a8e65315a146b6c6131964df6e1318c6")
    )
  )
}
