pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven(url = "https://maven.aliyun.com/repository/public")
        maven(url = "https://maven.aliyun.com/repository/google")
        maven(url = "https://repo.huaweicloud.com/repository/maven")
        maven(url = "https://jitpack.io")
        maven(url = "https://s01.oss.sonatype.org/content/groups/public")
    }
}

rootProject.name = "WanDevelop"
include(":app")
