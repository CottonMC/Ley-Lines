package io.github.cottonmc.leylines.jsongenerator

import io.github.cottonmc.jsonfactory.builder.JsonFactoryBuilder
import io.github.cottonmc.jsonfactory.data.Identifier
import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.TaskAction
import java.io.File

open class GeneratorTask:DefaultTask() {

    @TaskAction
    fun run(){

        val target = File(project.rootDir.absolutePath + "/run/resourcepacks/devpack")
        println(target)

        JsonFactoryBuilder()
                .addGenerator(ConnectedModelGenerator())
                .addIdentifier(Identifier("leylines","connected_block_test"))
                .setTarget(target)
                .generate()
    }
}