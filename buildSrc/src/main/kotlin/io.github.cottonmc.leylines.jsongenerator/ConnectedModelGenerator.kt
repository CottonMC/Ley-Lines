package io.github.cottonmc.leylines.jsongenerator

import io.github.cottonmc.jsonfactory.data.Identifier
import io.github.cottonmc.jsonfactory.gens.AbstractContentGenerator
import io.github.cottonmc.jsonfactory.output.Output
import io.github.cottonmc.jsonfactory.output.model.ModelVariant
import io.github.cottonmc.jsonfactory.output.model.MultipartBlockState
import io.github.cottonmc.jsonfactory.output.model.MultipartBlockState.Multipart
import io.github.cottonmc.jsonfactory.output.model.MultipartBlockState.When

class ConnectedModelGenerator : AbstractContentGenerator("Connected Model", "blockstates", LeyLinesPlugin.INFO) {

    override fun generate(id: Identifier): List<Output> {
        val state = ArrayList<Multipart>()

        for (side in arrayOf("east", "west")) {
            for (rotation in intArrayOf(0, 90, 180, 270)) {
                state.add(
                        Multipart(
                                `when` = When(
                                        mapOf(side to "false")
                                ),
                                apply = ModelVariant(
                                        model = id.wrapPath("block/", "_edge"),
                                        x = rotation,
                                        y = if (side == "east") {
                                            180
                                        } else 0,
                                        uvlock = true
                                )
                        )
                )
            }
        }

        state.add(
                Multipart(
                        apply = ModelVariant(
                                model= id.wrapPath("block/","_center")
                        )
                )
        )

        return listOf(MultipartBlockState(multipart = state))
    }
}

