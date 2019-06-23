package io.github.cottonmc.leylines.jsongenerator

import org.gradle.api.Plugin
import org.gradle.api.Project

class GeneratorPlugin:Plugin<Project> {
    override fun apply(target: Project) {
        target.tasks.create("generateModels",GeneratorTask::class.java)
    }
}