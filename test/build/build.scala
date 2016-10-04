import cbt._

// cbt:https://github.com/cvogt/cbt.git#95728f6041a32e081a1f4129b8483ef622179b45
class Build(val context: Context) extends ScalaParadise{
  override def dependencies = super.dependencies :+ DirectoryDependency(projectDirectory.getParentFile)
}
