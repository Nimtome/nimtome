package com.flyinpancake.dndspells

import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.ExperimentalMaterialApi
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.flyinpancake.dndspells.adapter.MainMenuRecyclerViewAdapter
import com.flyinpancake.dndspells.model.DndCharacter
import com.flyinpancake.dndspells.model.Spell
import com.flyinpancake.dndspells.model.SpellImporter
import com.flyinpancake.dndspells.databinding.ActivityMainBinding
import com.flyinpancake.dndspells.databinding.RecyclerviewMainMenuRowBinding
import com.flyinpancake.dndspells.viewmodel.CharacterViewModel
import com.flyinpancake.dndspells.viewmodel.SpellViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.*
import java.io.InputStream

class MainActivity : AppCompatActivity(), MainMenuRecyclerViewAdapter.SpellListItemClickListener {

    companion object {
        var PERMISSION_REQUEST_READ_EXTERNAL_STORAGE = 100
        var FILE_PICKER_REQUEST_CODE = 123
    }

    private lateinit var binding: ActivityMainBinding
    private lateinit var recyclerViewAdapter: MainMenuRecyclerViewAdapter
    private lateinit var characterViewModel: CharacterViewModel
    private val spellViewModel = SpellViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
//        binding.toolbar.title = title

        characterViewModel = ViewModelProvider(this)[CharacterViewModel::class.java]
        characterViewModel.allCharacters.observe(this ,{ characters ->
            recyclerViewAdapter.submitList(characters)
        })

        setupCharacterList()
    }

    private fun setupCharacterList() {
        recyclerViewAdapter = MainMenuRecyclerViewAdapter()
        recyclerViewAdapter.itemClickListener = this

        binding.rvCharacterList.adapter = recyclerViewAdapter

    }

    override fun onItemClick(character: DndCharacter, binding: RecyclerviewMainMenuRowBinding) {
        val intent = Intent(this, CharacterDetailsActivity::class.java)
        intent.putExtra(CharacterDetailsActivity.KEY_NAME, character.name)
        startActivity(intent)
    }

    override fun onItemLongClick(character: DndCharacter, binding: RecyclerviewMainMenuRowBinding) {
        val intent = Intent(this, CreateCharacterActivity::class.java)
        intent.putExtra(CreateCharacterActivity.KEY_CHARACTER_NAME, character.name)
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.import_xml -> {
                handleReadFilesPermission()
            }
            R.id.add_character -> {
                startActivity(Intent(this, CreateCharacterActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showRationaleDialog(
        @StringRes title: Int = R.string.rationale_title,
        @StringRes explanation: Int,
        onPositiveButton: () -> Unit,
        onNegativeButton: () -> Unit = this::finish
    ) {
        val alertDialog = AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(explanation)
            .setCancelable(false)
            .setPositiveButton(R.string.proceed) { dialog, _ ->
                dialog.cancel()
                onPositiveButton()
            }
            .setNegativeButton(R.string.cancel) { _, _ -> onNegativeButton() }
            .create()
        alertDialog.show()
    }

    private fun handleReadFilesPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                )
            ) {
                showRationaleDialog(
                    explanation = R.string.file_read_explanation,
                    onPositiveButton = { requestReadFilesystemPermission() }
                )
            } else {
                requestReadFilesystemPermission()
            }
        } else {
            openSpellsFile()
        }
    }

    private fun openSpellsFile() {
        // Open a file picker dialog
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            .setType("*/*")
            .addCategory(Intent.CATEGORY_OPENABLE)
        startActivityForResult(
            Intent.createChooser(intent, R.string.select_file.toString()),
            FILE_PICKER_REQUEST_CODE
        )
    }

    private fun loadSpellsFromFile(fis: InputStream, callback: (spellList: List<Spell>) -> Unit) {
        val importer = SpellImporter()
        val spellList = importer.importSpells(fis)
        Log.d("DDS_DEBUG", "The spell list is ${spellList.size} long")
        callback(spellList)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == FILE_PICKER_REQUEST_CODE && resultCode == RESULT_OK) {
            data?.data?.let { uri -> GlobalScope.launch { read(this@MainActivity, uri) } }
        }
    }

    private suspend fun read(context: Context, source: Uri) = withContext(Dispatchers.IO) {
        val resolver: ContentResolver = context.contentResolver

        resolver.openInputStream(source)
            ?.let { fis -> loadSpellsFromFile(fis) { result -> onSpellsRead(result) } }
            ?: throw IllegalStateException("could not open $source")
    }

    private fun onSpellsRead(spellList: List<Spell>) {
        Snackbar.make(binding.root, "Spells imported!", Snackbar.LENGTH_LONG).show()
        Log.d("DDS_DEBUG", "Spells arrived!")
        spellViewModel.nuke()
        spellList.forEach { spell -> spellViewModel.insert(spell) }
    }

    private fun requestReadFilesystemPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
            PERMISSION_REQUEST_READ_EXTERNAL_STORAGE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_REQUEST_READ_EXTERNAL_STORAGE)
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                openSpellsFile()

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


}