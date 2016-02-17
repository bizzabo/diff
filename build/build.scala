object Main{
  def main(args: Array[String]): Unit = {
    new cbt.Build(args(0),args.drop(1)){
      override lazy val dependencies: Array[String] = {
        val deps = (
          cbt.Dependency("com.chuusai","shapeless_2.11","2.3.0-RC1")
            .resolveRecursive().linearize
          ++
          cbt.Dependency("org.cvogt","scala-extensions_2.11","0.4.1")
            .resolveRecursive().linearize
        )
        deps.map(_.download)
        val jars = deps.map(_.jarFile).toArray
        //println(jars.toList)
        jars
      }
      override def compileArgs = Array(
        "-feature", "-deprecation", "-unchecked", "-language:experimental.macros"
      )
      override def version = "0.1-SNAPSHOT"
      override def artifactId = "diff"
      override def groupId = "org.cvogt"
      override def run = {
        cbt.Compiler.run( "xdotai.diff.Test", args.drop(1), compile )
      }
    }.main(Array())
  }
}
