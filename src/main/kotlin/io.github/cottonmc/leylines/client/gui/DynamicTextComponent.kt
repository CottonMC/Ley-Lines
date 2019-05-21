package io.github.cottonmc.leylines.client.gui

import net.minecraft.network.chat.BaseComponent
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.TextComponent

class DynamicTextComponent(private var text: String):BaseComponent() {
    override fun copyShallow(): Component {
        return DynamicTextComponent(text)
    }

    override fun getText(): String {
        return text
    }

    fun setText(text: String){
        this.text = text
    }

    fun getTextField(): String {
        return this.text
    }


    fun method_10992(): TextComponent {
        return TextComponent(this.text)
    }

    override fun equals(object_1: Any?): Boolean {
        if (this === object_1) {
            return true
        } else if (object_1 !is TextComponent) {
            return false
        } else {
            val textComponent_1 = object_1 as TextComponent?
            return this.text == textComponent_1!!.textField && super.equals(object_1)
        }
    }

    override fun toString(): String {
        return "TextComponent{text='" + this.text + '\''.toString() + ", siblings=" + this.siblings + ", style=" + this.style + '}'.toString()
    }
}