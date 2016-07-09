// Comment to get more information during initialization
logLevel := Level.Debug

resolvers += Resolver.url("scalasbt", new URL("http://scalasbt.artifactoryonline.com/scalasbt/sbt-plugin-releases"))(Resolver.ivyStylePatterns)

resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"

resolvers += "sonatype-releases" at "https://oss.sonatype.org/content/repositories/releases/"

addSbtPlugin("org.scalastyle" %% "scalastyle-sbt-plugin" % "0.7.0")

addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.4.6")

libraryDependencies ++= Seq(
    "org.slf4j" % "slf4j-api"       % "1.7.13" force(),
    "org.slf4j" % "slf4j-nop"       % "1.7.13" force(),
    "org.slf4j" % "slf4j-jdk14"     % "1.7.13" force(),
    "org.slf4j" % "jcl-over-slf4j"  % "1.7.13" force()
)
