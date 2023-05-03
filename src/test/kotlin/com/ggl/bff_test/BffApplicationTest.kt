package com.ggl.bff_test

import com.intuit.karate.KarateOptions
import com.intuit.karate.Runner.parallel
import com.intuit.karate.junit5.Karate
import com.intuit.karate.junit5.Karate.Test
import net.masterthought.cucumber.Configuration
import net.masterthought.cucumber.ReportBuilder
import org.amshove.kluent.`should be equal to`
import org.junit.jupiter.api.BeforeAll
import java.io.File
import java.lang.System.getProperty
import java.lang.System.setProperty


@KarateOptions(tags = ["~@ignore"])
class BffApplicationTest {

  private companion object {
    private val jsonRegex = Regex(".*\\.json")
    private const val karateEnv = "karate.env"
    private const val defaultEnv = "pp"
    private const val projectName = "BffTest-$defaultEnv"
    private const val target = "target"
    private val configuration = Configuration(File(target),projectName)

    @BeforeAll
    @JvmStatic
    internal fun beforeAll() {
      setProperty(
        karateEnv, getProperty(
          karateEnv,
          defaultEnv
      ))
    }
  }

  @Test
  internal fun testBff(): Karate {
    val results = parallel(javaClass, 5)
    generateReport(results.reportDir)
    results.failedMap.orEmpty().forEach { (key, value) -> println("$key: $value")}
    results.failCount `should be equal to` 0
    return Karate().relativeTo(javaClass)
  }

  private fun generateReport(karateOutputPath: String) =
    ReportBuilder(getAllJsonFiles(karateOutputPath),
      configuration
    )
      .generateReports()

  @Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
  private fun getAllJsonFiles(karateOutputPath: String) =
    File(karateOutputPath)
      .listFiles { file -> file.length() > 0 && file.name.matches(jsonRegex) }
      .map { it.absolutePath }

}


