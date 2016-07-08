import cbt._

// cbt:https://github.com/cvogt/cbt.git#ec35ffeba2f5e8a2b1e61c3ba7da4276aa0c8211
class Build(val context: Context) extends ScalaParadise{
  override def dependencies = super.dependencies :+ DirectoryDependency(projectDirectory.getParentFile)
}
