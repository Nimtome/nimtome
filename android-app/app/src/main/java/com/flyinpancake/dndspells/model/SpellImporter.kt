package com.flyinpancake.dndspells.model

import android.util.Xml
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException
import java.io.InputStream


class SpellImporter {
    fun importSpells(fis: InputStream): List<Spell> {
        return parse(fis)
    }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun parse(inputStream: InputStream): List<Spell> {
        inputStream.use { inputStream ->
            val parser = Xml.newPullParser()
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
            parser.setInput(inputStream, null)
            parser.nextTag()
            return readCompendium(parser)
        }
    }

    private fun readCompendium(parser: XmlPullParser): List<Spell> {
        val spells = mutableListOf<Spell>()
        parser.require(XmlPullParser.START_TAG, null, "compendium")
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG)
                continue
            if (parser.name == "spell")
                spells.add(readSpell(parser))
            else
                skip(parser)
        }

        return spells
    }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun skip(parser: XmlPullParser) {
        if (parser.eventType != XmlPullParser.START_TAG) {
            throw IllegalStateException()
        }
        var depth = 1
        while (depth != 0) {
            when (parser.next()) {
                XmlPullParser.END_TAG -> depth--
                XmlPullParser.START_TAG -> depth++
            }
        }
    }


    private fun readSpell(parser: XmlPullParser): Spell {
        parser.require(XmlPullParser.START_TAG, null, "spell")
        var name: String? = null
        var desc = ""
        var level: Int? = null
        var components: String? = null
        var range: String? = null
        var time: String? = null
        var school: String? = null
        var ritual: Boolean? = null
        var duration: String? = null
        var classes: String? = null
        var roll: String? = null


        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG)
                continue
            when (parser.name) {
                "name" -> name = readName(parser)
                "level" -> level = readLevel(parser)
                "school" -> school = readSchool(parser)
                "ritual" -> ritual = readRitual(parser)
                "time" -> time = readTime(parser)
                "range" -> range = readRange(parser)
                "components" -> components = readComponents(parser)
                "duration" -> duration = readDuration(parser)
                "classes" -> classes = readClasses(parser)
                "text" -> desc += readText(parser)
                "roll" -> roll = readRoll(parser)
                else -> skip(parser)
            }
        }

        //maybe throw something if the XML is corrupt

        return Spell(
            name!!,
            desc,
            level?: 0,
            components?:"",
            range?: "",
            time?: "",
            school ?: "",
            ritual ?: false,
            duration ?: "",
            classes?: "Any",
            roll
        )
    }

    @Throws(IOException::class, XmlPullParserException::class)
    private fun readInsideTags(parser: XmlPullParser, tag: String): String {
        parser.require(XmlPullParser.START_TAG, null, tag)
        val text = readRawText(parser)
        parser.require(XmlPullParser.END_TAG, null, tag)
        return text
    }

    @Throws(IOException::class, XmlPullParserException::class)
    private fun readClasses(parser: XmlPullParser): String {
        return readInsideTags(parser, "classes")
    }

    @Throws(IOException::class, XmlPullParserException::class)
    private fun readRoll(parser: XmlPullParser): String {
        return readInsideTags(parser, "roll")
    }

    @Throws(IOException::class, XmlPullParserException::class)
    private fun readText(parser: XmlPullParser): String {
        var text = readInsideTags(parser, "text")
        if (text.isEmpty())
            text = "\n"
        return text
    }

    @Throws(IOException::class, XmlPullParserException::class)
    private fun readDuration(parser: XmlPullParser): String {
        return readInsideTags(parser, "duration")
    }

    @Throws(IOException::class, XmlPullParserException::class)
    private fun readComponents(parser: XmlPullParser): String {
        return readInsideTags(parser, "components")
    }

    @Throws(IOException::class, XmlPullParserException::class)
    private fun readRange(parser: XmlPullParser): String {
        return readInsideTags(parser, "range")
    }

    @Throws(IOException::class, XmlPullParserException::class)
    private fun readTime(parser: XmlPullParser): String {
        return readInsideTags(parser, "time")
    }

    @Throws(IOException::class, XmlPullParserException::class)
    private fun readRitual(parser: XmlPullParser): Boolean {
        return readInsideTags(parser, "ritual") == "YES"
    }

    @Throws(IOException::class, XmlPullParserException::class)
    private fun readSchool(parser: XmlPullParser): String {
        return readInsideTags(parser, "school")
    }

    @Throws(IOException::class, XmlPullParserException::class)
    private fun readLevel(parser: XmlPullParser): Int {
        return readInsideTags(parser, "level").toInt()
    }

    @Throws(IOException::class, XmlPullParserException::class)
    private fun readName(parser: XmlPullParser): String {
        return readInsideTags(parser, "name")
    }

    @Throws(IOException::class, XmlPullParserException::class)
    private fun readRawText(parser: XmlPullParser): String {
        var result = ""
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.text
            parser.nextTag()
        }
        return result
    }

}