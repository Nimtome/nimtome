package com.nimtome.app.services.spellSourceGistService.models

import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root
class SpellSourcesGist() {
    @field:ElementList(name = "list", inline = true)
    lateinit var list: List<SpellSourceGistEntry>
}
