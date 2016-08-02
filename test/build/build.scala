import cbt._

// cbt:https://github.com/cvogt/cbt.git#b5d86995128a45c33117ecfb7365f0eb2b450a61
class Build(val context: Context) extends ScalaParadise{
  override def dependencies = super.dependencies :+ DirectoryDependency(projectDirectory.getParentFile)
  //override def scalacOptions = super.scalacOptions ++ Seq( "-Xprint:typer")
}
