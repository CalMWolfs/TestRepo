import at.hannibal2.changelog.SkyHanniChangelogBuilder
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction

abstract class ChangelogVerification : DefaultTask() {

    @Input
    var prTitle: String = ""

    @Input
    var prBody: String = ""

    @get:Internal
    val prBodyLines get() = prBody.lines()

    private val prLink = "ignored"

    @TaskAction
    fun scanChangelog() {
        val (changes, bodyErrors) = SkyHanniChangelogBuilder.findChanges(prBodyLines, prLink)
        val titleErrors = SkyHanniChangelogBuilder.findPullRequestNameErrors(prTitle, changes)

        if (bodyErrors.isEmpty() && titleErrors.isEmpty()) {
            println("Changelog and title verification successful")
        } else {
            // todo do something with the errors
            println("Changelog and/or title verification failed")
            bodyErrors.forEach { println(it.message) }
            titleErrors.forEach { println(it.message) }
            throw GradleException("Changelog verification failed")
        }
    }
}