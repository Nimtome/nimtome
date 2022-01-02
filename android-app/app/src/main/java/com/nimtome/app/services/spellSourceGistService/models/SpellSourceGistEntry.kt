package com.nimtome.app.services.spellSourceGistService.models

import com.nimtome.app.model.SpellSource
import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "entry", strict = false)
class SpellSourceGistEntry {
    @field:Element(name = "name")
    lateinit var name: String

    @field:Element(name = "description")
    lateinit var description: String

    @field:Element(name = "gistId")
    lateinit var gistId: String

    fun toSpellSource(): SpellSource {
        return SpellSource(
            this.name,
            this.description,
            this.gistId,
            false,
        )
    }
}

